package com.inov8.microbank.server.messaging.listener;

import java.util.Date;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.model.MessageQueueLogModel;
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.util.HttpInvokerUtil;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstants;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.dao.messagelog.MessageQueueLogDAO;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.OLAVeriflyFinancialInstitutionImpl;
import com.inov8.microbank.server.service.switchmodule.iris.SwitchController;
import com.inov8.microbank.server.service.switchmodule.iris.model.PhoenixIntegrationMessageVO;
import com.inov8.microbank.server.service.transactionmodule.TransactionModuleManager;
import com.inov8.ola.integration.vo.OLAVO;
import com.thoughtworks.xstream.XStream;

public class PhoenixBillPaymentListener implements MessageListener {

    private static Log logger = LogFactory.getLog(PhoenixBillPaymentListener.class);
    
    private AbstractFinancialInstitution olaVeriflyFinancialInstitution;
	private TransactionModuleManager transactionManager ;
	private MessageQueueLogDAO messageQueueLogDAO;
    private String timeToLive;    
    
    
    /* (non-Javadoc)
     * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
     */
    public void onMessage(Message message) {
            	
        if (message instanceof ObjectMessage) {         
            
        	PhoenixIntegrationMessageVO phoenixIntegrationMessage = null;
        	
            try {
            
                Object object = ((ObjectMessage) message).getObject();
                
                if(object != null) {
                    
                    if (object instanceof PhoenixIntegrationMessageVO) {
                        
                        XStream xStream = new XStream();
                        
                        phoenixIntegrationMessage = (PhoenixIntegrationMessageVO) object;
                        
                        SwitchController phoenixSwitch = getSwitchController(phoenixIntegrationMessage.getServiceUrl());

                        logger.debug( "***** Sending Bill Payment Advice to Phoenix/NADRA for Bill :" + phoenixIntegrationMessage.getConsumerNumber()+" & Account # : "+phoenixIntegrationMessage.getAccountNumber()+" Service URI = "+phoenixIntegrationMessage.getServiceUrl());

                        phoenixSwitch.billPayment(phoenixIntegrationMessage);                                                
                        
                        if(phoenixIntegrationMessage != null && "00".equals(phoenixIntegrationMessage.getResponseCode())) {

//                        	updateMessageQueueLogModel(phoenixIntegrationMessage, messageQueueLogModel); 
                            updateTransactionModel(phoenixIntegrationMessage);
                            logger.info( "***** Successfull Response of product provising from Phoenix/NADRA :" + phoenixIntegrationMessage.getResponseCode() +" Alert : "+ phoenixIntegrationMessage.getMessageAsEdi());       

                        }
                    }
                }
                
            } catch (JMSException ex) {
                
            	throw new RuntimeException(ex.getMessage(), ex);
                
            } catch (Exception ex) {
            	
            	ex.printStackTrace();
            	
            } finally {

            	
            	if(phoenixIntegrationMessage != null && !"00".equals(phoenixIntegrationMessage.getResponseCode())) {
            		
                    logger.info("Response Code of product provising from Phoenix/NADRA :" + phoenixIntegrationMessage.getResponseCode());
            	
	            	MessageQueueLogModel messageQueueLogModel = createMessageQueueLogModel(phoenixIntegrationMessage);
	                
	            	updateMessageQueueLogModel(phoenixIntegrationMessage, messageQueueLogModel);
            	}
            }
        }   
    }

    /**
     * @param phoenixIntegrationMessage
     * @return
     */
    private MessageQueueLogModel createMessageQueueLogModel(PhoenixIntegrationMessageVO phoenixIntegrationMessage) {
    	
    	MessageQueueLogModel messageQueueLogModel = getMessageQueueLogModel(phoenixIntegrationMessage.getMicrobankTransactionCode());
    	
    	messageQueueLogModel.setServiceUrl(phoenixIntegrationMessage.getServiceUrl());
    	messageQueueLogModel.setConsumerNumber(phoenixIntegrationMessage.getConsumerNumber());
    	messageQueueLogModel.setUtilityCompanyId(phoenixIntegrationMessage.getUtilityCompanyId());
    	messageQueueLogModel.setToBankAccountNumber(phoenixIntegrationMessage.getAccountNumber());
    	messageQueueLogModel.setTotalAmount(Double.parseDouble(phoenixIntegrationMessage.getAmountPaid()));
    	messageQueueLogModel.setCreatedOn(new Date());
    	messageQueueLogModel.setCreatedBy(2L);
    	
//    	messageQueueLogModel = this.messageQueueLogDAO.saveOrUpdate(messageQueueLogModel);
    	
    	return messageQueueLogModel;
    }
    
    
    /**
     * @param phoenixIntegrationMessage
     * @param messageQueueLogModel
     */
    private void updateMessageQueueLogModel(PhoenixIntegrationMessageVO phoenixIntegrationMessage, MessageQueueLogModel messageQueueLogModel) {
    	
    	messageQueueLogModel.setResponseCode(phoenixIntegrationMessage.getResponseCode());
    	messageQueueLogModel.setUpdatedOn(new Date());
    	messageQueueLogModel.setUpdatedBy(2L);
    	
    	this.messageQueueLogDAO.saveOrUpdate(messageQueueLogModel);
    }


    /**
     * @param transactionCode
     * @return
     */
    private MessageQueueLogModel getMessageQueueLogModel(String transactionCode) {

    	MessageQueueLogModel messageQueueLogModel = new MessageQueueLogModel();
    	messageQueueLogModel.setTransactionCode(transactionCode);
    	
    	CustomList<MessageQueueLogModel> customList = null;  //messageQueueLogDAO.findByExample(messageQueueLogModel, null);
    	
    	List<MessageQueueLogModel> list = null;  // customList.getResultsetList();
    	
    	if(list != null && !list.isEmpty()) {
    		
    		messageQueueLogModel = list.get(0);
    	
    	} else {
    	
    		messageQueueLogModel = new MessageQueueLogModel();
        	messageQueueLogModel.setTransactionCode(transactionCode);
        	messageQueueLogModel.setRetryCount(-1);
    	}

    	messageQueueLogModel.setRetryCount(messageQueueLogModel.getRetryCount() + 1);
    	
    	return messageQueueLogModel;
    }
    
    
    /**
     * @param url
     * @return
     */
    private SwitchController getSwitchController(String url) {
		return HttpInvokerUtil.getHttpInvokerFactoryBean(SwitchController.class, url);
    }

    
    /**
     * @param phoenixIntegrationMessage
     * @throws FrameworkCheckedException
     */
    private void updateTransactionModel(PhoenixIntegrationMessageVO phoenixIntegrationMessage) throws FrameworkCheckedException {
        
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        TransactionCodeModel transactionCodeModel = new TransactionCodeModel();
        transactionCodeModel.setTransactionCodeId(phoenixIntegrationMessage.getMicrobankTransactionCodeId());
        searchBaseWrapper.setBasePersistableModel(transactionCodeModel);
        searchBaseWrapper = this.transactionManager.loadTransactionByTransactionCode(searchBaseWrapper);    
        
        if(searchBaseWrapper.getBasePersistableModel() != null) {
            
            TransactionModel transactionModel = (TransactionModel) searchBaseWrapper.getBasePersistableModel();
            transactionModel.setSupProcessingStatusId(SupplierProcessingStatusConstants.COMPLETED);
            BaseWrapper wrapper = new BaseWrapperImpl();
            wrapper.setBasePersistableModel(transactionModel);
            wrapper = this.transactionManager.saveTransactionModel(wrapper);
        }
    }   
    
    /**
     * @param phoenixIntegrationMessage
     * @throws JMSException
     * @throws FrameworkCheckedException
     */
    private void failTransaction(PhoenixIntegrationMessageVO phoenixIntegrationMessage) throws JMSException, FrameworkCheckedException {
        
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        TransactionCodeModel txCodeModel = new TransactionCodeModel();
        txCodeModel.setTransactionCodeId(phoenixIntegrationMessage.getMicrobankTransactionCodeId());
        baseWrapper.setBasePersistableModel(txCodeModel);
        
        baseWrapper = this.transactionManager.failTransaction(baseWrapper);

        TransactionModel transactionModel = (TransactionModel) baseWrapper.getBasePersistableModel();
        
        try {
			rollback(phoenixIntegrationMessage, transactionModel);
		} catch (Exception e) {

			e.printStackTrace();
		}
    }


	/**
	 * @param phoenixIntegrationMessageVO
	 * @param transactionModel
	 * @throws Exception
	 */
	public void rollback(PhoenixIntegrationMessageVO phoenixIntegrationMessageVO, TransactionModel transactionModel) throws Exception {
		
		OLAVeriflyFinancialInstitutionImpl olaVeriflyFinancialInstitution = (OLAVeriflyFinancialInstitutionImpl) this.olaVeriflyFinancialInstitution;
					
		String olaFromAccount = phoenixIntegrationMessageVO.getToAccountCurrency();
		String olaToAccount = phoenixIntegrationMessageVO.getFromAccountCurrency();

		if(!StringUtil.isNullOrEmpty(olaFromAccount) && !StringUtil.isNullOrEmpty(olaToAccount)) {
			
			OLAVO olaVO = new OLAVO();
			olaVO.setPayingAccNo(olaToAccount);
			olaVO.setReceivingAccNo(olaFromAccount);
			olaVO.setBalance(Double.valueOf(phoenixIntegrationMessageVO.getAmountPaid()));						
			
			TransactionCodeModel transactionCodeModel = new TransactionCodeModel();
			transactionCodeModel.setCode(phoenixIntegrationMessageVO.getMicrobankTransactionCode());
			transactionCodeModel.setTransactionCodeId(phoenixIntegrationMessageVO.getMicrobankTransactionCodeId());
			
			WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
			workFlowWrapper.setTransactionCodeModel(transactionCodeModel);
			
			SwitchWrapper switchWrapper = new SwitchWrapperImpl();	
			switchWrapper.setWorkFlowWrapper(workFlowWrapper);
			switchWrapper.setUtilityCompanyId(phoenixIntegrationMessageVO.getUtilityCompanyId());
			switchWrapper.setConsumerNumber(phoenixIntegrationMessageVO.getConsumerNumber());
			switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
			switchWrapper.setAmountPaid(phoenixIntegrationMessageVO.getAmountPaid());
			switchWrapper.setFromAccountNo(phoenixIntegrationMessageVO.getAccountNumber());
			switchWrapper.setFromAccountType(phoenixIntegrationMessageVO.getAccountType());
			switchWrapper.setCurrencyCode(phoenixIntegrationMessageVO.getAccountCurrency());						
			switchWrapper.setTransactionAmount(Double.valueOf(phoenixIntegrationMessageVO.getAmountPaid()));		
			switchWrapper.setWorkFlowWrapper(workFlowWrapper);
			switchWrapper.setFromAccountNo(olaToAccount);
			switchWrapper.setToAccountNo(olaFromAccount);		
			switchWrapper.setOlavo(olaVO);
			
			olaVeriflyFinancialInstitution.rollback(switchWrapper);
		}			
	}


    public void setTransactionManager(TransactionModuleManager transactionManager) {
        this.transactionManager = transactionManager;
    }
    public void setOlaVeriflyFinancialInstitution(AbstractFinancialInstitution olaVeriflyFinancialInstitution) {
		this.olaVeriflyFinancialInstitution = olaVeriflyFinancialInstitution;
	}
    public void setMessageQueueLogDAO(MessageQueueLogDAO messageQueueLogDAO) {
		this.messageQueueLogDAO = messageQueueLogDAO;
	}
    public void setTimeToLive(String timeToLive) {
        this.timeToLive = timeToLive;
    }
        
}