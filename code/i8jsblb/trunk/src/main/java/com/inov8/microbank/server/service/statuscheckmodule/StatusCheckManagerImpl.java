package com.inov8.microbank.server.service.statuscheckmodule;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.SimpleMailMessage;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.messagemodule.EmailMessage;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.MailEngine;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.IntegrationModuleModel;
import com.inov8.microbank.common.util.CreditAccountQueueSender;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.ServiceTypeConstantsInterface;
import com.inov8.microbank.common.util.StatusCheckConstants;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.financialintegrationmodule.OLAVeriflyFinancialInstitutionImpl;
import com.inov8.microbank.server.service.integrationmodule.IntegrationModuleManager;
import com.inov8.ola.integration.vo.OLAVO;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: inov8 Limited</p>
 *
 * @author Jawwad Farooq
 * @version 1.0
 */
public class StatusCheckManagerImpl
    implements StatusCheckManager
{
  private IntegrationModuleManager integrationModuleManager;
  private CreditAccountQueueSender creditAccountQueueSender;
  private MailEngine mailEngine;
  private SimpleMailMessage mailMessage;
  private static long lastAlertSentOn = -1;

  protected final Log logger = LogFactory.getLog(StatusCheckManagerImpl.class);

  public StatusCheckManagerImpl()
  {}

  /**
   * This method gets the status of the integration modules from database.
   * @param workFlowWrapper WorkFlowWrapper
   * @return WorkFlowWrapper
   */
  public SearchBaseWrapper getIMStatus(WorkFlowWrapper workFlowWrapper) throws
      FrameworkCheckedException
  {
    Long transactionTypeId = workFlowWrapper.getTransactionTypeModel().getTransactionTypeId();
    SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
    IntegrationModuleModel integrationModule = new IntegrationModuleModel();
    List temporaryList = new ArrayList();

    if (TransactionTypeConstantsInterface.DIST_TO_DIST_TX.equals(transactionTypeId) ||
    	TransactionTypeConstantsInterface.DIST_TO_RET_TX.equals(transactionTypeId) ||
    	TransactionTypeConstantsInterface.OPERATOR_TO_DISTR_TX.equals(transactionTypeId) ||
//        transactionTypeId == TransactionTypeConstantsInterface.RET_HEAD_TO_RET_TX ||
    	TransactionTypeConstantsInterface.RET_TO_RET_TX.equals(transactionTypeId))
    {
      // No need to check the integration model's status
    }
    else if (TransactionTypeConstantsInterface.RET_TO_CUSTOMER_TX.equals(transactionTypeId) 
             /*|| transactionTypeId == TransactionTypeConstantsInterface.OPERATOR_TO_CUSTOMER_TX*/)
    {
      String paymentMode = workFlowWrapper.getPaymentModeModel().getName();

      logger.info("********************* Payment Mode : " + paymentMode);

      // Get the Switch's status( which depends on the payment mode )

      // Here we have to get the reference of the Switch based on the payment mode

      // Then we will check the Switch module status

      //Following logic should be replaced
      integrationModule.setName(paymentMode);
      searchBaseWrapper.setBasePersistableModel(integrationModule);
      searchBaseWrapper = this.integrationModuleManager.searchIntegrationModule(
          searchBaseWrapper);
      temporaryList.addAll(searchBaseWrapper.getCustomList().getResultsetList());

        logger.info("workFlowWrapper.getServiceTypeModel().getName()  " +
                         workFlowWrapper.getServiceTypeModel().getName());

      // Check the status of the Supplier only if the product's Service type is 'VARIABLE'
      if(ServiceTypeConstantsInterface.SERVICE_TYPE_VARIABLE.equals(workFlowWrapper.getServiceTypeModel().getPrimaryKey()))
      {
        String supplierName = workFlowWrapper.getProductModel().
            getSupplierIdSupplierModel().getName();

        // Get the supplier module's status
        integrationModule.setName(supplierName);
        searchBaseWrapper.setBasePersistableModel(integrationModule);
        searchBaseWrapper = this.integrationModuleManager.
            searchIntegrationModule(searchBaseWrapper);
        searchBaseWrapper.getCustomList().getResultsetList().addAll(
            temporaryList);
      }
    }

    return searchBaseWrapper;
  }

  public boolean isAllIntegrationModulesAlive(SearchBaseWrapper
                                              searchBaseWrapper,
                                              WorkFlowWrapper workFlowWrapper)
  {
    boolean response = true;

    if (null != searchBaseWrapper.getCustomList())
    {
      List<IntegrationModuleModel> integrationModuleModelList =
          searchBaseWrapper.
          getCustomList().getResultsetList();

      Long transactionTypeId = workFlowWrapper.getTransactionTypeModel().getTransactionTypeId();

     if (TransactionTypeConstantsInterface.DIST_TO_DIST_TX.equals(transactionTypeId) ||
 		TransactionTypeConstantsInterface.DIST_TO_RET_TX.equals(transactionTypeId) ||
 		TransactionTypeConstantsInterface.OPERATOR_TO_DISTR_TX.equals(transactionTypeId) ||
//        transactionTypeId == TransactionTypeConstantsInterface.RET_HEAD_TO_RET_TX ||
 		TransactionTypeConstantsInterface.RET_TO_RET_TX.equals(transactionTypeId))
    {
        return true;
      }
      else if (TransactionTypeConstantsInterface.RET_TO_CUSTOMER_TX.equals(transactionTypeId) 
               /*|| transactionTypeId == TransactionTypeConstantsInterface.OPERATOR_TO_CUSTOMER_TX */ )
      {
        for (IntegrationModuleModel integrationModuleModel :
             integrationModuleModelList)
        {
          if (integrationModuleModel.getStatus() != null &&	integrationModuleModel.getStatus().intValue() == StatusCheckConstants.NOT_ALIVE)
          {
            response = false;
          }
        }
        return response;
      }
      return response;
    }
    return response;
  }

  public void setIntegrationModuleManager(IntegrationModuleManager
                                          integrationModuleManager)
  {
    this.integrationModuleManager = integrationModuleManager;
  }

  		@Override
  		public void checkActiveMQStatus() throws WorkFlowException {
  			try{
  				OLAVO testOlaVo = new OLAVO();
  				testOlaVo.setLedgerId(-99L);
  				logger.info("[ActiveMQ-Health-Check] START");
  				
  				creditAccountQueueSender.send(testOlaVo);
		  
  				logger.info("[ActiveMQ-Health-Check] SUCCESSFUL");
  			}catch(Exception ex){
  				logger.error("[ActiveMQ-Health-Check] FAILED ... so going to fail the transaction before generating transactioCode",ex);
  				this.sendAlertEmail();
  				throw new WorkFlowException(WorkFlowErrorCodeConstants.GENERAL_ERROR);
  			}
  		}
  		
  		private boolean isOneHourPassed(long now) {
  			return lastAlertSentOn == -1 || now - lastAlertSentOn > 1800000L; // 30*60*1000 = one hour
		}
  		
		private void sendAlertEmail(){
			String sender = MessageUtil.getMessage("jsm.failure.alert.from");
			String recipients = MessageUtil.getMessage("jsm.failure.alert.recipients");
			String emailText = MessageUtil.getMessage("jsm.failure.alert.message");
			String subject = MessageUtil.getMessage("jsm.failure.alert.subject");
			
			Long startTime = Long.valueOf(System.currentTimeMillis());
			logger.info("[ActiveMQ-Health-Check-Failed-sendAlertEmail] Start:\n[Message ID:" + startTime + "]");							
			
			mailMessage.setFrom(sender);
			mailMessage.setTo(recipients.split(";"));
			mailMessage.setSubject(subject);
			mailMessage.setText(emailText);
			mailMessage.setSentDate(new Date());
			
			try{
				if (isOneHourPassed(startTime)) {
					
					mailEngine.send(mailMessage);
					
					lastAlertSentOn = startTime;
					
					Long endTime = System.currentTimeMillis();
					logger.info("[ActiveMQ-Health-Check-sendAlertEmail] Complete:"+
							"\n[Message ID(Start Time in millis):" + startTime + ", Message Completed in (seconds):"+ ((endTime-startTime)/1000) + "]");
			    
				}else{
					Long endTime = System.currentTimeMillis();
					logger.info("[ActiveMQ-Health-Check-Failed-sendAlertEmail] Skipped as time passed since last alert < 1 hour:"+
							"\n[Message ID(Start Time in millis):" + startTime + ", Message Completed in (seconds):"+ ((endTime-startTime)/1000) + "]");
			    }
			}catch(Exception ex){
				logger.error("[ActiveMQ-Health-Check-Failed-sendAlertEmail] ... Failed to send alert email",ex);
			}
			
		}
  
public void setCreditAccountQueueSender(
		CreditAccountQueueSender creditAccountQueueSender) {
	this.creditAccountQueueSender = creditAccountQueueSender;
}
public void setMailEngine(MailEngine mailEngine) {
	this.mailEngine = mailEngine;
}
public void setMailMessage(SimpleMailMessage mailMessage) {
	this.mailMessage = mailMessage;
}

}
