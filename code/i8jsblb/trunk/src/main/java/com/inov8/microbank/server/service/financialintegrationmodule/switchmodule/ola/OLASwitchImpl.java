package com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ola;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.integration.enums.ResponseCodeEnum;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.vo.MiddlewareMessageVO;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.jms.DestinationConstants;
import com.inov8.microbank.common.model.AuditLogModel;
import com.inov8.microbank.common.model.StakeholderBankInfoModel;
import com.inov8.microbank.common.model.messagemodule.OLAMessage;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchProcessor;
import com.inov8.microbank.server.service.integration.vo.MiddlewareAdviceVO;
import com.inov8.microbank.server.service.jms.JmsProducer;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;
import com.inov8.microbank.server.service.switchmodule.ola.OLASwitch;
import com.inov8.ola.integration.service.OLAService;
import com.inov8.ola.integration.vo.LedgerModel;
import com.inov8.ola.integration.vo.OLALedgerVO;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.ola.util.TransactionTypeConstants;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.viewer.Command;
import org.joda.time.DateTimeUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.remoting.RemoteAccessException;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OLASwitchImpl extends SwitchProcessor {

    protected static Log logger = LogFactory.getLog(OLASwitchImpl.class);
    private ApplicationContext ctx;
    OLAService olaService;
    ESBAdapter esbAdapter;
    private StakeholderBankInfoManager stakeholderBankInfoManager;

    public OLASwitchImpl(ApplicationContext ctx) {
        this.ctx = ctx;
        olaService = (OLAService) ctx.getBean("olaManagerService");
        esbAdapter = (ESBAdapter) ctx.getBean("esbAdapter");
        stakeholderBankInfoManager = (StakeholderBankInfoManager) ctx.getBean("stakeholderBankInfoManager");
    }

    public SwitchWrapper checkBalance(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Inside checkBalance of OLASwitchImpl");
        }
        AuditLogModel auditLogModel = this.auditLogBeforeCall(switchWrapper, "");
        BaseWrapper baseWrapper = createOrUpdatePostedTransactionBeforeCall(switchWrapper, IntgTransactionTypeConstantsInterface.CHECK_BALANCE_OLA);
        XStream xStream = new XStream();
        OLAVO olaVO = new OLAVO();
        try {

		      /*if(switchWrapper.getCommissioned())
		      {
		    	  logger.debug("Before discount the amount turns out to be "+ switchWrapper.getTransactionTransactionModel().getTotalAmount());
		    	  olaVO.setAmount((switchWrapper.getTransactionTransactionModel().getTotalAmount() - switchWrapper.getDiscountAmount()));
		    	  logger.debug("After discount the amount turns out to be "+ (switchWrapper.getTransactionTransactionModel().getTotalAmount() - switchWrapper.getDiscountAmount()));

		      }
		      else
		      {
		    	  olaVO.setAmount(switchWrapper.getTransactionTransactionModel().getTotalAmount());
		      }
		      */

            olaVO.setPayingAccNo(switchWrapper.getAccountInfoModel().getAccountNo());
            auditLogModel.setInputParam(xStream.toXML(olaVO));
            OLASwitch olaSwitch = this.getOlaSwitch(switchWrapper);
            if (olaSwitch == null) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.CONNECTION_FAILED_UBL_SWITCH);
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Hitting OLA to process transaction ");
            }
            //Maqsood Shahzad -- commenting the call to check balance because OLA in unstable

//		      olaVO.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerAccountTypeId());

            olaVO = olaSwitch.checkBalance(olaVO);

            processOLAResponseCode(olaVO.getResponseCode());

            switchWrapper.setOlavo(olaVO);
            switchWrapper.setBalance(olaVO.getBalance());
        } catch (Exception ex) {
            logger.error("Exception occured " + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
            auditLogModel.setCustomField1(WorkFlowErrorCodeConstants.FAILURE);
            auditLogModel.setCustomField2(ex.getMessage());
            if (ex instanceof RemoteAccessException) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.SWITCH_INACTIVE);
            } else if (ex instanceof RemoteAccessException) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.SWITCH_INACTIVE);
            } else if (ex instanceof WorkFlowException)
                throw (WorkFlowException) ex;
            else
                throw new WorkFlowException(WorkFlowErrorCodeConstants.CONNECTION_FAILED_UBL_SWITCH);
        } finally {
            if (null != auditLogModel.getCustomField1() && auditLogModel.getCustomField1().equals(WorkFlowErrorCodeConstants.FAILURE) && (olaVO == null || olaVO.getResponseCode() == null || olaVO.getResponseCode().equals(""))) {
                this.auditLogAfterCall(auditLogModel, "");
            } else {
                this.auditLogAfterCall(auditLogModel, xStream.toXML(olaVO));
                if (baseWrapper != null) {
                    createOrUpdatePostedTransactionAfterCall(switchWrapper, baseWrapper);
                }
            }
        }

        return switchWrapper;
    }

    public SwitchWrapper rollback(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Inside rollback of OLASwitchImpl");
        }
        AuditLogModel auditLogModel = this.auditLogBeforeCall(switchWrapper, "");
        BaseWrapper baseWrapper = createOrUpdatePostedTransactionBeforeCall(switchWrapper, IntgTransactionTypeConstantsInterface.ROLL_BACK_OLA);
        XStream xStream = new XStream();
        OLAVO olaVO = switchWrapper.getOlavo();
        olaVO.setIsBillPayment(true);
        try {
            auditLogModel.setInputParam(xStream.toXML(olaVO));
            OLASwitch olaSwitch = this.getOlaSwitch(switchWrapper);
            if (olaSwitch == null) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.CONNECTION_FAILED_UBL_SWITCH);
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Hitting OLA to process ROLLBACK transaction ");
            }

            olaVO.setResponseCode("");
            if (null == olaVO.getReasonId() || (null != olaVO.getReasonId() && olaVO.getReasonId().equals("")))
                olaVO.setReasonId(OLATransactionReasonsInterface.REVERSAL);

            olaVO = olaSwitch.reversal(olaVO);

            processOLAResponseCode(olaVO.getResponseCode());

            switchWrapper.setOlavo(olaVO);

        } catch (Exception ex) {
            logger.error("Exception occured " + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
            auditLogModel.setCustomField1(WorkFlowErrorCodeConstants.FAILURE);
            auditLogModel.setCustomField2(ex.getMessage());
            if (ex instanceof RemoteAccessException) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.SWITCH_INACTIVE);
            } else if (ex instanceof WorkFlowException)
                throw (WorkFlowException) ex;
            else
                throw new WorkFlowException(WorkFlowErrorCodeConstants.CONNECTION_FAILED_UBL_SWITCH);
        } finally {
            if (null != auditLogModel.getCustomField1() && auditLogModel.getCustomField1().equals(WorkFlowErrorCodeConstants.FAILURE) && (olaVO == null || olaVO.getResponseCode() == null || olaVO.getResponseCode().equals(""))) {
                this.auditLogAfterCall(auditLogModel, "");
            } else {
                this.auditLogAfterCall(auditLogModel, xStream.toXML(olaVO));
                if (baseWrapper != null) {
                    createOrUpdatePostedTransactionAfterCall(switchWrapper, baseWrapper);
                }
            }
        }

        return switchWrapper;
    }


    public SwitchWrapper transaction(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {

        if (logger.isDebugEnabled()) {
            logger.debug("Inside checkBalance of OLASwitchImpl");
        }
        AuditLogModel auditLogModel = this.auditLogBeforeCall(switchWrapper, "");
        BaseWrapper baseWrapper = createOrUpdatePostedTransactionBeforeCall(switchWrapper, IntgTransactionTypeConstantsInterface.DEBIT_OLA);
        XStream xStream = new XStream();
        OLAVO olaVO = switchWrapper.getOlavo();
        try {
            auditLogModel.setInputParam(xStream.toXML(olaVO));
            OLASwitch olaSwitch = this.getOlaSwitch(switchWrapper);
            if (olaSwitch == null) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.CONNECTION_FAILED_UBL_SWITCH);
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Hitting OLA to process transaction ");
            }
            olaVO = olaSwitch.transaction(olaVO);

            auditLogModel.setIntegrationPartnerIdentifier(olaVO.getAuthCode());
            switchWrapper.getWorkFlowWrapper().getTransactionModel().setBankResponseCode(olaVO.getAuthCode());
		      /*OLAVO tempOla = olaSwitch.checkBalance(olaVO);
		      switchWrapper.setBalance(tempOla.getBalance());*/
            switchWrapper.setBalance(olaVO.getBalance());//Added by Mudassir - Bug 909 Fix

            processOLAResponseCode(olaVO.getResponseCode());

            switchWrapper.setOlavo(olaVO);

        } catch (Exception ex) {
            logger.error("Exception occured " + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
            auditLogModel.setCustomField1(WorkFlowErrorCodeConstants.FAILURE);
            auditLogModel.setCustomField2(ex.getMessage());
            if (ex instanceof RemoteAccessException) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.SWITCH_INACTIVE);
            }
            if (ex instanceof CommandException) {
                ex.printStackTrace();
                throw (FrameworkCheckedException) ex;
            } else if (ex instanceof WorkFlowException)
                throw (WorkFlowException) ex;
            else
                throw new WorkFlowException(WorkFlowErrorCodeConstants.CONNECTION_FAILED_UBL_SWITCH);
        } finally {
            if (null != auditLogModel.getCustomField1() && auditLogModel.getCustomField1().equals(WorkFlowErrorCodeConstants.FAILURE) && (olaVO == null || olaVO.getResponseCode() == null || olaVO.getResponseCode().equals(""))) {
                this.auditLogAfterCall(auditLogModel, "");
            } else {
                this.auditLogAfterCall(auditLogModel, xStream.toXML(olaVO));
            }
            if (baseWrapper != null) {
                createOrUpdatePostedTransactionAfterCall(switchWrapper, baseWrapper);
            }
        }

        return switchWrapper;

    }


    public SwitchWrapper debit(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {

        if (logger.isDebugEnabled()) {
            logger.debug("Inside debit of OLASwitchImpl");
        }

        OLAVO olaVO = switchWrapper.getOlavo();

        OLAMessage olaMessage = new OLAMessage();
        if (switchWrapper.getOlavo() != null) {
            olaVO.setIsBillPayment(switchWrapper.getOlavo().getIsBillPayment());
        }
        olaMessage.setOlaVO(olaVO);
        olaMessage.setActionLogId(ThreadLocalActionLog.getActionLogId());
        olaMessage.setServiceURL(switchWrapper.getSwitchSwitchModel().getUrl());
//		olaMessage.setServiceURL("http://localhost:8080/olaintegration/ws/olaSwitch");
        olaMessage.setTransactionCodeId(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getPrimaryKey());
        if (olaVO.getCustomerAccountTypeId() == null) {
            olaVO.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerAccountTypeId());
        }
        olaVO.setTransactionDateTime(new Date());
//		System.out.println("*******Inside Debit method of OLASwitchImpl the ACCOUNT TYPE IS :: "+olaVO.getCustomerAccountTypeId()+" ***********");
//		System.out.println( "$$$$$$$$$$$$$$$$$ NO QUEUE $$$$$$$$$$$$$$$$$ " );

        //Maqsood - moving from queue to real time
        AuditLogModel auditLogModel = this.auditLogBeforeCall(switchWrapper, "");
        BaseWrapper baseWrapper = createOrUpdatePostedTransactionBeforeCall(switchWrapper, IntgTransactionTypeConstantsInterface.CREDIT_OLA);
        XStream xStream = new XStream();
        //-----------------------------------------------------------------------------


        try {
            auditLogModel.setInputParam(xStream.toXML(olaVO));
            OLASwitch olaSwitch = this.getOlaSwitch(switchWrapper);
            if (olaSwitch == null) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.CONNECTION_FAILED_UBL_SWITCH);
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Hitting OLA to process transaction ");
            }
            olaVO = olaSwitch.transaction(olaVO);
            auditLogModel.setIntegrationPartnerIdentifier(olaVO.getAuthCode());
            switchWrapper.getWorkFlowWrapper().getTransactionModel().setBankResponseCode(olaVO.getAuthCode());
//		      OLAVO tempOla = olaSwitch.checkBalance(olaVO);
//		      switchWrapper.setBalance(tempOla.getBalance());

            processOLAResponseCode(olaVO.getResponseCode());

            switchWrapper.setOlavo(olaVO);
        } catch (Exception ex) {
            logger.error("Exception occured " + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
            auditLogModel.setCustomField1(WorkFlowErrorCodeConstants.FAILURE);
            auditLogModel.setCustomField2(ex.getMessage());
            if (ex instanceof CommandException) {
                ex.printStackTrace();
                throw (FrameworkCheckedException) ex;
            } else if (ex instanceof RemoteAccessException) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.SWITCH_INACTIVE);
            } else if (ex instanceof WorkFlowException)
                throw (WorkFlowException) ex;
            else
                throw new WorkFlowException(WorkFlowErrorCodeConstants.CONNECTION_FAILED_UBL_SWITCH);
        } finally {
            if (null != auditLogModel.getCustomField1() && auditLogModel.getCustomField1().equals(WorkFlowErrorCodeConstants.FAILURE) && (olaVO == null || olaVO.getResponseCode() == null || olaVO.getResponseCode().equals(""))) {
                this.auditLogAfterCall(auditLogModel, "");
            } else {
                this.auditLogAfterCall(auditLogModel, xStream.toXML(olaVO));
                if (baseWrapper != null) {
                    createOrUpdatePostedTransactionAfterCall(switchWrapper, baseWrapper);
                }
            }

        }



/*		JmsProducer jmsProducer = (JmsProducer)ctx.getBean("jmsProducer") ;

		if(logger.isDebugEnabled())
		{
			logger.debug("Publishing the message on "+DestinationConstants.OLA_DEBIT_DESTINATION);
		}

		jmsProducer.produce(olaMessage, DestinationConstants.OLA_DEBIT_DESTINATION);

	*/
        return switchWrapper;
    }


    /**
     * This method is used to settle commission in OLA
     *
     * @author Jawwad Farooq
     * @date December 2008
     */

    @Override
    public SwitchWrapper settleCommission(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Inside debit of OLASwitchImpl");
        }

        OLAVO olaVO = switchWrapper.getOlavo();
        OLAMessage olaMessage = new OLAMessage();

        olaMessage.setOlaVO(olaVO);
        olaMessage.setActionLogId(ThreadLocalActionLog.getActionLogId());
        olaMessage.setServiceURL(switchWrapper.getSwitchSwitchModel().getUrl());
        olaMessage.setTransactionCodeId(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getPrimaryKey());
        olaMessage.setAllpayCommTransId((Long) switchWrapper.getObject(WorkFlowErrorCodeConstants.ALLPAY_COMM_TRANS_ID));

        if (switchWrapper.getCommissionStakeHolderType().equals(CommissionConstantsInterface.NATIONAL_DISTRIBUTOR_STAKEHOLDER))
            olaMessage.setNationalDistributor(true);
        else if (switchWrapper.getCommissionStakeHolderType().equals(CommissionConstantsInterface.DISTRIBUTOR_STAKEHOLDER))
            olaMessage.setDistributor(true);
        else if (switchWrapper.getCommissionStakeHolderType().equals(CommissionConstantsInterface.RETAILER_STAKEHOLDER))
            olaMessage.setRetailer(true);

//		System.out.println( "%%%%%%%%%%" + switchWrapper.getSwitchSwitchModel().getUrl() );

        JmsProducer jmsProducer = (JmsProducer) ctx.getBean("jmsProducer");

        if (logger.isDebugEnabled()) {
            logger.debug("Publishing the message on " + DestinationConstants.OLA_COMMISSION_DESTINATION);
        }

        jmsProducer.produce(olaMessage, DestinationConstants.OLA_COMMISSION_DESTINATION);

        return switchWrapper;
    }

    @Override
    public SwitchWrapper settleInov8Commission(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Inside settleInov8Commission of OLASwitchImpl");
        }

        OLAVO olaVO = switchWrapper.getOlavo();
        OLAMessage olaMessage = new OLAMessage();

        olaMessage.setOlaVO(olaVO);
        olaMessage.setActionLogId(ThreadLocalActionLog.getActionLogId());
        olaMessage.setServiceURL(switchWrapper.getSwitchSwitchModel().getUrl());
        olaMessage.setTransactionCodeId(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getPrimaryKey());

//		System.out.println( "%%%%%%%%%%" + switchWrapper.getSwitchSwitchModel().getUrl() );

        JmsProducer jmsProducer = (JmsProducer) ctx.getBean("jmsProducer");

        if (logger.isDebugEnabled()) {
            logger.debug("Publishing the message on " + DestinationConstants.OLA_COMMISSION_DESTINATION);
        }

        jmsProducer.produce(olaMessage, DestinationConstants.OLA_COMMISSION_DESTINATION);

        return switchWrapper;
    }


    public OLASwitch getOlaSwitch(SwitchWrapper switchWrapper) {
        if (logger.isDebugEnabled()) {
            logger.debug("Inside getOLASwitch method of OLASwitchImpl");
        }
        try {
            return olaService;

        } catch (Exception ex) {
            logger.error("Exception occured while getting switch " + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
            return null;
        }

    }

    public SwitchWrapper createAccount(SwitchWrapper switchWrapper) throws WorkFlowException {
        if (logger.isDebugEnabled()) {
            logger.debug("Inside checkBalance of OLASwitchImpl");
        }
        AuditLogModel auditLogModel = this.auditLogBeforeCall(switchWrapper, "");
        XStream xStream = new XStream();
        OLAVO olaVO = switchWrapper.getOlavo();
        try {
            auditLogModel.setInputParam(xStream.toXML(olaVO));
            OLASwitch olaSwitch = this.getOlaSwitch(switchWrapper);
            if (olaSwitch == null) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.CONNECTION_FAILED_UBL_SWITCH);
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Hitting OLA to process transaction ");
            }
            olaVO = olaSwitch.createAccount(olaVO);

            if (!olaVO.getResponseCode().equals("07")) {
                processOLAResponseCode(olaVO.getResponseCode());
            }

            switchWrapper.setOlavo(olaVO);

        } catch (Exception ex) {
            logger.error("Exception occured " + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
            auditLogModel.setCustomField1(WorkFlowErrorCodeConstants.FAILURE);
            auditLogModel.setCustomField2(ex.getMessage());
            if (ex instanceof RemoteAccessException) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.SWITCH_INACTIVE);
            } else if (ex instanceof WorkFlowException)
                throw (WorkFlowException) ex;
            else
                throw new WorkFlowException(WorkFlowErrorCodeConstants.CONNECTION_FAILED_UBL_SWITCH);
        } finally {
            if (null != auditLogModel.getCustomField1() && auditLogModel.getCustomField1().equals(WorkFlowErrorCodeConstants.FAILURE) && (olaVO == null || olaVO.getResponseCode() == null || olaVO.getResponseCode().equals(""))) {
                this.auditLogAfterCall(auditLogModel, "");
            } else {
                this.auditLogAfterCall(auditLogModel, xStream.toXML(olaVO));
            }

        }

        return switchWrapper;

    }


    public SwitchWrapper getAllOlaAccounts(SwitchWrapper switchWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Inside checkBalance of OLASwitchImpl");
        }
        AuditLogModel auditLogModel = this.auditLogBeforeCall(switchWrapper, "");
        XStream xStream = new XStream();
        List<OLAVO> list = null;
        OLAVO olaVO = switchWrapper.getOlavo();
        try {
            auditLogModel.setInputParam(xStream.toXML(switchWrapper));
            OLASwitch olaSwitch = this.getOlaSwitch(switchWrapper);
            if (olaSwitch == null) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.CONNECTION_FAILED_UBL_SWITCH);
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Hitting OLA to process transaction ");
            }

            list = olaSwitch.getAllAccounts(olaVO);


            if (null == list) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.NO_ACCOUNTS_FOUND);
            }
            switchWrapper.setOlaAccountsList(list);

        } catch (Exception ex) {
            logger.error("Exception occured " + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
            auditLogModel.setCustomField1(WorkFlowErrorCodeConstants.FAILURE);
            auditLogModel.setCustomField2(ex.getMessage());
            if (ex instanceof RemoteAccessException) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.SWITCH_INACTIVE);
            } else if (ex instanceof WorkFlowException)
                throw (WorkFlowException) ex;
            else
                throw new WorkFlowException(WorkFlowErrorCodeConstants.CONNECTION_FAILED_UBL_SWITCH);
        } finally {
            if (null != auditLogModel.getCustomField1() && auditLogModel.getCustomField1().equals(WorkFlowErrorCodeConstants.FAILURE) && (list == null)) {
                this.auditLogAfterCall(auditLogModel, "");
            } else {
                this.auditLogAfterCall(auditLogModel, xStream.toXML(list));
            }

        }

        return switchWrapper;

    }


    public SwitchWrapper updateAccountInfo(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Inside updateAccount of OLASwitchImpl");
        }
        AuditLogModel auditLogModel = this.auditLogBeforeCall(switchWrapper, "");
        XStream xStream = new XStream();
        OLAVO olaVO = switchWrapper.getOlavo();
        try {
            auditLogModel.setInputParam(xStream.toXML(olaVO));
            OLASwitch olaSwitch = this.getOlaSwitch(switchWrapper);
            if (olaSwitch == null) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.CONNECTION_FAILED_UBL_SWITCH);
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Hitting OLA to process transaction ");
            }
            olaVO = olaSwitch.changeAccountStatus(olaVO);


            if (!olaVO.getResponseCode().equals("07")) {
                processOLAResponseCode(olaVO.getResponseCode());
            }

            switchWrapper.setOlavo(olaVO);

        } catch (Exception ex) {
            logger.error("Exception occured " + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
            auditLogModel.setCustomField1(WorkFlowErrorCodeConstants.FAILURE);
            auditLogModel.setCustomField2(ex.getMessage());
            if (ex instanceof RemoteAccessException) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.SWITCH_INACTIVE);
            } else if (ex instanceof WorkFlowException)
                throw (WorkFlowException) ex;
            else
                throw new WorkFlowException(WorkFlowErrorCodeConstants.CONNECTION_FAILED_UBL_SWITCH);
        } finally {
            if (null != auditLogModel.getCustomField1() && auditLogModel.getCustomField1().equals(WorkFlowErrorCodeConstants.FAILURE) && (olaVO == null || olaVO.getResponseCode() == null || olaVO.getResponseCode().equals(""))) {
                this.auditLogAfterCall(auditLogModel, "");
            } else {
                this.auditLogAfterCall(auditLogModel, xStream.toXML(olaVO));
            }

        }

        return switchWrapper;
    }

    public SwitchWrapper changeAccountDetails(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
        logger.debug("Inside changeAccountDetails of OLASwitchImpl");
        AuditLogModel auditLogModel = this.auditLogBeforeCall(switchWrapper, "");
        OLAVO olaVO = switchWrapper.getOlavo();
        try {
            auditLogModel.setInputParam(CommonUtils.getJSON(olaVO));
            OLASwitch olaSwitch = this.getOlaSwitch(switchWrapper);
            if (olaSwitch == null) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.CONNECTION_FAILED_UBL_SWITCH);
            }

            logger.debug("Hitting OLA to process transaction ");
            olaVO = olaSwitch.changeAccountDetails(olaVO);


            if (!olaVO.getResponseCode().equals("07")) {
                processOLAResponseCode(olaVO.getResponseCode());
            }

            switchWrapper.setOlavo(olaVO);

        } catch (Exception ex) {
            logger.error("Exception occured " + ex);
            auditLogModel.setCustomField1(WorkFlowErrorCodeConstants.FAILURE);
            auditLogModel.setCustomField2(ex.getMessage());
            if (ex instanceof RemoteAccessException) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.SWITCH_INACTIVE);
            } else if (ex instanceof WorkFlowException)
                throw (WorkFlowException) ex;
            else
                throw new WorkFlowException(WorkFlowErrorCodeConstants.CONNECTION_FAILED_UBL_SWITCH);
        } finally {
            if (null != auditLogModel.getCustomField1() && auditLogModel.getCustomField1().equals(WorkFlowErrorCodeConstants.FAILURE) && (olaVO == null || olaVO.getResponseCode() == null || olaVO.getResponseCode().equals(""))) {
                this.auditLogAfterCall(auditLogModel, "");
            } else {
                this.auditLogAfterCall(auditLogModel, CommonUtils.getJSON(olaVO));
            }

        }

        return switchWrapper;
    }


    public SwitchWrapper getAccountInfo(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Inside updateAccount of OLASwitchImpl");
        }
        AuditLogModel auditLogModel = this.auditLogBeforeCall(switchWrapper, "");
        OLAVO olaVO = switchWrapper.getOlavo();
        try {
            auditLogModel.setInputParam(CommonUtils.getJSON(olaVO));
            OLASwitch olaSwitch = this.getOlaSwitch(switchWrapper);
            if (olaSwitch == null) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.CONNECTION_FAILED_UBL_SWITCH);
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Hitting OLA to process transaction ");
            }

            Long customerAcctTypeID = olaVO.getCustomerAccountTypeId();
            String cnic = olaVO.getCnic();
            olaVO = olaSwitch.getAccountInfo(olaVO);


//			      if(olaVO.getResponseCode().equals("02"))
//			      {
//			    	  throw new WorkFlowException( WorkFlowErrorCodeConstants.INACTIVE_ACCOUNT ) ;
//			      }
            if (olaVO.getResponseCode().equals("03")) {
                if (customerAcctTypeID != null && customerAcctTypeID.equals(UserTypeConstantsInterface.WALKIN_CUSTOMER)) {
                    logger.info("[OLASwitchImpl.getAccountInfo] Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Walkin Customer Account for CNIC:" + cnic + "not found in OLA");
                    switchWrapper.setOlavo(olaVO);
                } else {
                    logger.error(olaVO.getPayingAccNo() + "." + olaVO.getReceivingAccNo());
                    throw new WorkFlowException(WorkFlowErrorCodeConstants.DELETED_ACCOUNT);
                }
            } else if (olaVO.getResponseCode().equals("04")) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.DATA_TYPE_MISMATCH);
            } else if (olaVO.getResponseCode().equals("05")) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.TRANSACTION_TIMEOUT);
            } else if (!olaVO.getResponseCode().equals("00") && !olaVO.getResponseCode().equals("02")) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.TRANSACTION_TIMEOUT);
            }

            switchWrapper.setOlavo(olaVO);

        } catch (Exception ex) {
            logger.error("Exception occured " + ex);
            auditLogModel.setCustomField1(WorkFlowErrorCodeConstants.FAILURE);
            auditLogModel.setCustomField2(ex.getMessage());
            if (ex instanceof RemoteAccessException) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.SWITCH_INACTIVE);
            } else if (ex instanceof WorkFlowException)
                throw (WorkFlowException) ex;
            else
                throw new WorkFlowException(WorkFlowErrorCodeConstants.CONNECTION_FAILED_UBL_SWITCH);
        } finally {
            if (null != auditLogModel.getCustomField1() && auditLogModel.getCustomField1().equals(WorkFlowErrorCodeConstants.FAILURE) && (olaVO == null || olaVO.getResponseCode() == null || olaVO.getResponseCode().equals(""))) {
                this.auditLogAfterCall(auditLogModel, "");
            } else {
                this.auditLogAfterCall(auditLogModel, CommonUtils.getJSON(olaVO));
            }

        }

        return switchWrapper;

    }

    public SwitchWrapper creditTransfer(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Inside creditTransfer of OLASwitchImpl");
        }
        AuditLogModel auditLogModel = this.auditLogBeforeCall(switchWrapper, "");
        OLAVO olaVO = switchWrapper.getOlavo();
        try {
            auditLogModel.setInputParam(CommonUtils.getJSON(olaVO));
            OLASwitch olaSwitch = this.getOlaSwitch(switchWrapper);
            if (olaSwitch == null) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.CONNECTION_FAILED_UBL_SWITCH);
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Hitting OLA to process transaction ");
            }
            olaVO = olaSwitch.creditTransfer(olaVO);
            switchWrapper.getWorkFlowWrapper().getTransactionModel().setBankResponseCode(olaVO.getAuthCode());

            processOLAResponseCode(olaVO.getResponseCode());

            switchWrapper.setOlavo(olaVO);

        } catch (Exception ex) {
            logger.error("Exception occured " + ex.getMessage());
            auditLogModel.setCustomField1(WorkFlowErrorCodeConstants.FAILURE);
            auditLogModel.setCustomField2(ex.getMessage());
            if (ex instanceof RemoteAccessException) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.SWITCH_INACTIVE);
            } else if (ex instanceof WorkFlowException)
                throw (WorkFlowException) ex;
            else
                throw new WorkFlowException(WorkFlowErrorCodeConstants.CONNECTION_FAILED_UBL_SWITCH);
        } finally {
            if (null != auditLogModel.getCustomField1() && auditLogModel.getCustomField1().equals(WorkFlowErrorCodeConstants.FAILURE) && (olaVO == null || olaVO.getResponseCode() == null || olaVO.getResponseCode().equals(""))) {
                this.auditLogAfterCall(auditLogModel, "");
            } else {
                this.auditLogAfterCall(auditLogModel, CommonUtils.getJSON(olaVO));
            }
        }

        return switchWrapper;

    }


    public Map<Long, String> getStatusCodes() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }


    public SwitchWrapper getLedger(SwitchWrapper switchWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Inside getLedger of OLASwitchImpl");
        }
        AuditLogModel auditLogModel = this.auditLogBeforeCall(switchWrapper, "");
        BaseWrapper baseWrapper = createOrUpdatePostedTransactionBeforeCall(switchWrapper, IntgTransactionTypeConstantsInterface.LEDGER_OLA);
        XStream xStream = new XStream();
        List<LedgerModel> list = null;
        OLALedgerVO olaLedgerVO = switchWrapper.getLedgerVO();
        try {
            auditLogModel.setInputParam(xStream.toXML(switchWrapper));
            OLASwitch olaSwitch = this.getOlaSwitch(switchWrapper);
            if (olaSwitch == null) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.CONNECTION_FAILED_UBL_SWITCH);
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Hitting OLA to process transaction ");
            }

            list = olaSwitch.getLegder(olaLedgerVO);

            switchWrapper.setOlavo(new OLAVO());        //temporary assignment for posted transaction report
            //	response code and auth code should came from ola and set in switchWrapper.
            if (null == list) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.NO_ACCOUNTS_FOUND);
            }
            switchWrapper.getOlavo().setResponseCode("00");
            switchWrapper.setLedgerModelList(list);
        } catch (Exception ex) {
            logger.error("Exception occured " + ex.getMessage());
            auditLogModel.setCustomField1(WorkFlowErrorCodeConstants.FAILURE);
            auditLogModel.setCustomField2(ex.getMessage());
            if (ex instanceof RemoteAccessException) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.SWITCH_INACTIVE);
            } else if (ex instanceof WorkFlowException)
                throw (WorkFlowException) ex;
            else
                throw new WorkFlowException(WorkFlowErrorCodeConstants.CONNECTION_FAILED_UBL_SWITCH);
        } finally {
            if (null != auditLogModel.getCustomField1() && auditLogModel.getCustomField1().equals(WorkFlowErrorCodeConstants.FAILURE) && (list == null)) {
                this.auditLogAfterCall(auditLogModel, "");
            } else {
                this.auditLogAfterCall(auditLogModel, xStream.toXML(list));
            }
            if (baseWrapper != null) {
                createOrUpdatePostedTransactionAfterCall(switchWrapper, baseWrapper);
            }
        }
        return switchWrapper;
    }

    public SwitchWrapper getAllAccountsWithStats(SwitchWrapper switchWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Inside checkBalance of OLASwitchImpl");
        }
        AuditLogModel auditLogModel = this.auditLogBeforeCall(switchWrapper, "");
        XStream xStream = new XStream();
        HashMap<String, OLAVO> list = null;
        OLAVO olaVO = switchWrapper.getOlavo();
        try {
            auditLogModel.setInputParam(xStream.toXML(switchWrapper));
            OLASwitch olaSwitch = this.getOlaSwitch(switchWrapper);
            if (olaSwitch == null) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.CONNECTION_FAILED_UBL_SWITCH);
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Hitting OLA to process transaction ");
            }

            list = olaSwitch.getAllAccountsWithStats(olaVO.getStatsDate());


            if (null == list) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.NO_ACCOUNTS_FOUND);
            }
            switchWrapper.setOlaAccountsWithStatsHashMap(list);

        } catch (Exception ex) {
            logger.error("Exception occured " + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
            auditLogModel.setCustomField1(WorkFlowErrorCodeConstants.FAILURE);
            auditLogModel.setCustomField2(ex.getMessage());
            if (ex instanceof RemoteAccessException) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.SWITCH_INACTIVE);
            } else if (ex instanceof WorkFlowException)
                throw (WorkFlowException) ex;
            else
                throw new WorkFlowException(WorkFlowErrorCodeConstants.CONNECTION_FAILED_UBL_SWITCH);
        } finally {
            if (null != auditLogModel.getCustomField1() && auditLogModel.getCustomField1().equals(WorkFlowErrorCodeConstants.FAILURE) && (list == null)) {
                this.auditLogAfterCall(auditLogModel, "");
            } else {
                this.auditLogAfterCall(auditLogModel, xStream.toXML(list));
            }

        }

        return switchWrapper;

    }


    public SwitchWrapper getAllAccountsStatsWithRange(SwitchWrapper switchWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Inside getAllAccountsStatsWithRange of OLASwitchImpl");
        }
        AuditLogModel auditLogModel = this.auditLogBeforeCall(switchWrapper, "");
        XStream xStream = new XStream();
        HashMap<String, OLAVO> list = null;
        OLAVO olaVO = switchWrapper.getOlavo();
        try {
            auditLogModel.setInputParam(xStream.toXML(switchWrapper));
            OLASwitch olaSwitch = this.getOlaSwitch(switchWrapper);
            if (olaSwitch == null) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.CONNECTION_FAILED_UBL_SWITCH);
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Hitting OLA to process transaction ");
            }

            list = olaSwitch.getAllAccountsStatsWithRange(olaVO.getStatsStartDate(), olaVO.getStatsEndDate());


            if (null == list) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.NO_ACCOUNTS_FOUND);
            }
            switchWrapper.setOlaAccountsWithStatsHashMap(list);

        } catch (Exception ex) {
            logger.error("Exception occured " + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
            auditLogModel.setCustomField1(WorkFlowErrorCodeConstants.FAILURE);
            auditLogModel.setCustomField2(ex.getMessage());
            if (ex instanceof RemoteAccessException) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.SWITCH_INACTIVE);
            } else if (ex instanceof WorkFlowException)
                throw (WorkFlowException) ex;
            else
                throw new WorkFlowException(WorkFlowErrorCodeConstants.CONNECTION_FAILED_UBL_SWITCH);
        } finally {
            if (null != auditLogModel.getCustomField1() && auditLogModel.getCustomField1().equals(WorkFlowErrorCodeConstants.FAILURE) && (list == null)) {
                this.auditLogAfterCall(auditLogModel, "");
            } else {
                this.auditLogAfterCall(auditLogModel, xStream.toXML(list));
            }

        }

        return switchWrapper;

    }

    public SwitchWrapper verifyWalkinCustomerThroughputLimits(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException, Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("Inside checkBalance of OLASwitchImpl");
        }
        AuditLogModel auditLogModel = this.auditLogBeforeCall(switchWrapper, "");
        OLAVO olaVO = switchWrapper.getOlavo();
        olaVO.setExcludeInProcessTx(CommonUtils.convertToBoolean((String) switchWrapper.getObject("EXCLUDE_INPROCESS_TX")));
        try {
            auditLogModel.setInputParam(CommonUtils.getJSON(olaVO));
            OLASwitch olaSwitch = this.getOlaSwitch(switchWrapper);
            if (olaSwitch == null) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.CONNECTION_FAILED_UBL_SWITCH);
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Hitting OLA to process transaction ");
            }
            olaVO = olaSwitch.verifyWalkinCustomerThroughputLimits(olaVO);
            Map<String, Object> responseCodeMap = olaVO.getResponseCodeMap();
            processOLAResponseCode((String) responseCodeMap.get(TransactionTypeConstants.KEY_RESPONSE_CODE));
            switchWrapper.setOlavo(olaVO);
        } catch (Exception ex) {
            logger.error("Exception occured " + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
            auditLogModel.setCustomField1(WorkFlowErrorCodeConstants.FAILURE);
            auditLogModel.setCustomField2(ex.getMessage());
            if (ex instanceof CommandException) {
                ex.printStackTrace();
                throw (FrameworkCheckedException) ex;
            } else if (ex instanceof RemoteAccessException) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.SWITCH_INACTIVE);
            } else if (ex instanceof WorkFlowException)
                throw (WorkFlowException) ex;
            else
                throw new WorkFlowException(WorkFlowErrorCodeConstants.CONNECTION_FAILED_UBL_SWITCH);
        } finally {
            if (null != auditLogModel.getCustomField1() && auditLogModel.getCustomField1().equals(WorkFlowErrorCodeConstants.FAILURE) && (olaVO == null || olaVO.getResponseCode() == null || olaVO.getResponseCode().equals(""))) {
                this.auditLogAfterCall(auditLogModel, "");
            } else {
                this.auditLogAfterCall(auditLogModel, CommonUtils.getJSON(olaVO));
            }

        }

        return switchWrapper;

    }


    public SwitchWrapper saveWalkinCustomerLedgerEntry(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException, Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("Inside checkBalance of OLASwitchImpl");
        }
        AuditLogModel auditLogModel = this.auditLogBeforeCall(switchWrapper, "");
        OLAVO olaVO = switchWrapper.getOlavo();
        try {
            auditLogModel.setInputParam(CommonUtils.getJSON(olaVO));
            OLASwitch olaSwitch = this.getOlaSwitch(switchWrapper);
            if (olaSwitch == null) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.CONNECTION_FAILED_UBL_SWITCH);
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Hitting OLA to process transaction ");
            }
            olaVO = olaSwitch.saveWalkinCustomerLedgerEntry(olaVO);

            processOLAResponseCode(olaVO.getResponseCode());

            switchWrapper.setOlavo(olaVO);

        } catch (Exception ex) {
            logger.error("Exception occured " + ex);
            auditLogModel.setCustomField1(WorkFlowErrorCodeConstants.FAILURE);
            auditLogModel.setCustomField2(ex.getMessage());
            if (ex instanceof RemoteAccessException) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.SWITCH_INACTIVE);
            } else if (ex instanceof WorkFlowException)
                throw (WorkFlowException) ex;
            else
                throw new WorkFlowException(WorkFlowErrorCodeConstants.CONNECTION_FAILED_UBL_SWITCH);
        } finally {
            if (null != auditLogModel.getCustomField1() && auditLogModel.getCustomField1().equals(WorkFlowErrorCodeConstants.FAILURE) && (olaVO == null || olaVO.getResponseCode() == null || olaVO.getResponseCode().equals(""))) {
                this.auditLogAfterCall(auditLogModel, "");
            } else {
                this.auditLogAfterCall(auditLogModel, CommonUtils.getJSON(olaVO));
            }

        }

        return switchWrapper;

    }


    public SwitchWrapper rollbackWalkinCustomer(SwitchWrapper switchWrapper) throws WorkFlowException {
        if (logger.isDebugEnabled()) {
            logger.debug("Inside rollbackWalkinCustomer() of OLASwitchImpl");
        }
        AuditLogModel auditLogModel = this.auditLogBeforeCall(switchWrapper, "");
        OLAVO olaVO = switchWrapper.getOlavo();
        olaVO.setIsBillPayment(true);
        try {
            auditLogModel.setInputParam(CommonUtils.getJSON(olaVO));
            OLASwitch olaSwitch = this.getOlaSwitch(switchWrapper);
            if (olaSwitch == null) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.CONNECTION_FAILED_UBL_SWITCH);
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Hitting OLA to process ROLLBACK Walk-in Customr transaction ");
            }

            olaVO.setResponseCode("");
            olaVO.setReasonId(OLATransactionReasonsInterface.ROLLBACK_WALKIN_CUSTOMER);

            olaVO = olaSwitch.rollbackWalkinCustomer(olaVO);


		      /*if(olaVO.getResponseCode().equals("01"))
		      {
		    	  throw new WorkFlowException( WorkFlowErrorCodeConstants.INSUFFICIENT_ACC_BALANCE ) ;

		      }else */
            if (!olaVO.getResponseCode().equals("00")) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.TRANSACTION_TIMEOUT);
            }

            switchWrapper.setOlavo(olaVO);

        } catch (Exception ex) {
            logger.error("Exception occured " + ex);
            auditLogModel.setCustomField1(WorkFlowErrorCodeConstants.FAILURE);
            auditLogModel.setCustomField2(ex.getMessage());
            if (ex instanceof RemoteAccessException) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.SWITCH_INACTIVE);
            } else if (ex instanceof WorkFlowException)
                throw (WorkFlowException) ex;
            else
                throw new WorkFlowException(WorkFlowErrorCodeConstants.CONNECTION_FAILED_UBL_SWITCH);
        } finally {
            if (null != auditLogModel.getCustomField1() && auditLogModel.getCustomField1().equals(WorkFlowErrorCodeConstants.FAILURE) && (olaVO == null || olaVO.getResponseCode() == null || olaVO.getResponseCode().equals(""))) {
                this.auditLogAfterCall(auditLogModel, "");
            } else {
                this.auditLogAfterCall(auditLogModel, CommonUtils.getJSON(olaVO));
            }
        }

        return switchWrapper;
    }


    public SwitchWrapper updateLedger(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Inside updateLedger of OLASwitchImpl");
        }
        AuditLogModel auditLogModel = this.auditLogBeforeCall(switchWrapper, "");
        OLAVO olaVO = switchWrapper.getOlavo();
        try {
            auditLogModel.setInputParam(CommonUtils.getJSON(olaVO));
            OLASwitch olaSwitch = this.getOlaSwitch(switchWrapper);
            if (olaSwitch == null) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.CONNECTION_FAILED_UBL_SWITCH);
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Hitting OLA to process transaction ");
            }
            olaVO = olaSwitch.updateLedger(olaVO);

            if (!olaVO.getResponseCode().equals("00")) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.TRANSACTION_TIMEOUT);
            }
            switchWrapper.setOlavo(olaVO);

        } catch (Exception ex) {
            logger.error("Exception occured " + ex);
            auditLogModel.setCustomField1(WorkFlowErrorCodeConstants.FAILURE);
            auditLogModel.setCustomField2(ex.getMessage());
            if (ex instanceof RemoteAccessException) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.SWITCH_INACTIVE);
            } else if (ex instanceof WorkFlowException)
                throw (WorkFlowException) ex;
            else
                throw new WorkFlowException(WorkFlowErrorCodeConstants.CONNECTION_FAILED_UBL_SWITCH);
        } finally {
            if (null != auditLogModel.getCustomField1() && auditLogModel.getCustomField1().equals(WorkFlowErrorCodeConstants.FAILURE) && (olaVO == null || olaVO.getResponseCode() == null || olaVO.getResponseCode().equals(""))) {
                this.auditLogAfterCall(auditLogModel, "");
            } else {
                this.auditLogAfterCall(auditLogModel, CommonUtils.getJSON(olaVO));
            }

        }

        return switchWrapper;

    }

    @Override
    public SwitchWrapper debitCreditAccount(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Inside DebitCreditAccount of OLASwitchImpl");
        }

        OLAVO olaVO = switchWrapper.getOlavo();
        Long productId = null;
        if (olaVO != null && olaVO.getProductId() != null)
            productId = olaVO.getProductId();

        Long sTime = DateTimeUtils.currentTimeMillis();
        Long eTime;
        logger.info("Product ID ( " + productId + " ) The Start of [OLASwitchImpl.debitCreditAccount()] at the time :: " + new Date());
        AuditLogModel auditLogModel = this.auditLogBeforeCall(switchWrapper, "");
        BaseWrapper baseWrapper = null;
        Long intgTransactionType = null;
        if(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId() != null &&
                (switchWrapper.getWorkFlowWrapper().getProductModel().getProductId().equals(ProductConstantsInterface.VC_TRANSFER_PRODUCT))){
            I8SBSwitchControllerRequestVO requestVO = ESBAdapter.prepareFundsTransferForSalaryDisbursement
                    (I8SBConstants.RequestType_InterBankFundTransfer);

            I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
            requestVO.setTransactionAmount(String.valueOf(switchWrapper.getWorkFlowWrapper().getTransactionAmount()));
            requestVO.setAccountId1(EncryptionUtil.encryptWithAES("682ede816988e58fb6d057d9d85605e0", switchWrapper.getToAccountNo()));
            requestVO.setAccountId2(EncryptionUtil.encryptWithAES("682ede816988e58fb6d057d9d85605e0", switchWrapper.getFromAccountNo()));
            requestVO.setReserved2("VCSETT-");
            SwitchWrapper i8sbSwitchWrapper = new SwitchWrapperImpl();

            i8sbSwitchWrapper.setI8SBSwitchControllerRequestVO(requestVO);
            requestVO = i8sbSwitchWrapper.getI8SBSwitchControllerRequestVO();
            responseVO = requestVO.getI8SBSwitchControllerResponseVO();
            i8sbSwitchWrapper = this.esbAdapter.makeI8SBCall(i8sbSwitchWrapper);
            responseVO = i8sbSwitchWrapper.getI8SBSwitchControllerResponseVO();
            ESBAdapter.processI8sbResponseCode(responseVO, false);
            if (!responseVO.getResponseCode().equals("I8SB-200")) {
                throw new WorkFlowException("Error in Funds Transfer");
            }
            MiddlewareMessageVO middlewareMessageVO = new MiddlewareMessageVO();
            middlewareMessageVO.setAccountNo1(switchWrapper.getToAccountNo());
            middlewareMessageVO.setAccountNo2(switchWrapper.getFromAccountNo());
            middlewareMessageVO.setTransactionAmount(String.valueOf(switchWrapper.getWorkFlowWrapper().getTransactionAmount()));
            middlewareMessageVO.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            middlewareMessageVO.setStan(responseVO.getSTAN());
            middlewareMessageVO.setResponseCode(responseVO.getResponseCode());
            switchWrapper.setMiddlewareIntegrationMessageVO(middlewareMessageVO);

        }
        if (productId != null && (productId.equals(ProductConstantsInterface.DEBIT_CARD_CASH_WITHDRAWAL_OFF_US)
                || productId.equals(ProductConstantsInterface.DEBIT_CARD_CASH_WITHDRAWAL_ON_US)
                ||productId.equals(ProductConstantsInterface.DEBIT_CARD_LESS_CASH_WITHDRAWAL)
                || productId.equals(ProductConstantsInterface.POS_DEBIT_CARD_CASH_WITHDRAWAL)
                || productId.equals(ProductConstantsInterface.POS_DEBIT_CARD_REFUND)
                || productId.equals(ProductConstantsInterface.INTERNATIONAL_DEBIT_CARD_CASH_WITHDRAWAL_OFF_US)
                || productId.equals(ProductConstantsInterface.International_POS_DEBIT_CARD_CASH_WITHDRAWAL)))
            intgTransactionType = IntgTransactionTypeConstantsInterface.DEBIT_CARD;
        else if (switchWrapper.getWorkFlowWrapper().getProductModel().getProductId() != null &&
                (switchWrapper.getWorkFlowWrapper().getProductModel().getProductId().equals(ProductConstantsInterface.VC_TRANSFER_PRODUCT))){
            intgTransactionType = IntgTransactionTypeConstantsInterface.TRANSFER_IN_CORE;
        }
            else{
            intgTransactionType = IntgTransactionTypeConstantsInterface.DEBIT_CREDIT_OLA;
        }
        if (!switchWrapper.getSkipPostedTrxEntry()) {
            baseWrapper = createOrUpdatePostedTransactionBeforeCall(switchWrapper, intgTransactionType);
        }
        olaVO.setTransactionDateTime(new Date());
        try {
            auditLogModel.setInputParam(CommonUtils.getJSON(olaVO));
            OLASwitch olaSwitch = this.getOlaSwitch(switchWrapper);
            if (olaSwitch == null) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.CONNECTION_FAILED_UBL_SWITCH);
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Hitting OLA to process transaction ");
            }
            eTime = DateTimeUtils.currentTimeMillis();
            logger.info("Product ID ( " + productId + " ) The Start of creditTransfer() [OLASwitchImpl.debitCreditAccount()] " +
                    "at the time :: " + String.valueOf(eTime));
            olaVO.setDeviceTypeId(String.valueOf(switchWrapper.getWorkFlowWrapper().getObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID)));
            if (switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder() != null) {
                olaVO.setCommissionAmount(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getTotalCommissionAmount());
                olaVO.setTransactionProcessingAmount(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getTransactionProcessingAmount());
                olaVO.setTotalAmount(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getTotalAmount());
            }

            if (switchWrapper.getWorkFlowWrapper().getAppUserModel() != null) {
                olaVO.setMobileNumber(switchWrapper.getWorkFlowWrapper().getAppUserModel().getMobileNo());
                olaVO.setCnic(switchWrapper.getWorkFlowWrapper().getAppUserModel().getNic());
            }
            olaVO.setReceiverMobileNumber(String.valueOf(switchWrapper.getWorkFlowWrapper().getObject(CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE)));
            if (switchWrapper.getWorkFlowWrapper().getReceiverAppUserModel() != null) {
                olaVO.setReceiverCnic(switchWrapper.getWorkFlowWrapper().getReceiverAppUserModel().getNic());
            }

            if(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId() != null &&
                    (switchWrapper.getWorkFlowWrapper().getProductModel().getProductId().equals(ProductConstantsInterface.INTERNATIONAL_DEBIT_CARD_CASH_WITHDRAWAL_OFF_US)
                            || switchWrapper.getWorkFlowWrapper().getProductModel().getProductId().
                            equals(ProductConstantsInterface.International_POS_DEBIT_CARD_CASH_WITHDRAWAL))){
                if(switchWrapper.getWorkFlowWrapper().getFilerRate() != null && switchWrapper.getWorkFlowWrapper().getFilerRate() > 0.0){
                    StakeholderBankInfoModel stakeholderBankInfoModel = stakeholderBankInfoManager.loadStakeholderBankInfoModel
                            (PoolAccountConstantsInterface.ADVANCE_TAX_FILER_TO_T24);
                    switchWrapper.setToAccountNo(stakeholderBankInfoModel.getAccountNo());

                    StakeholderBankInfoModel stakeholderBankInfoModel1 = stakeholderBankInfoManager.loadStakeholderBankInfoModel
                            (PoolAccountConstantsInterface.ADVANCE_TAX_FILER_FROM_T24);
                    switchWrapper.setFromAccountNo(stakeholderBankInfoModel1.getAccountNo());
                }
                if(switchWrapper.getWorkFlowWrapper().getNonFilerRate() != null && switchWrapper.getWorkFlowWrapper().getNonFilerRate() > 0.0){
                    StakeholderBankInfoModel stakeholderBankInfoModel = stakeholderBankInfoManager.loadStakeholderBankInfoModel
                            (PoolAccountConstantsInterface.ADVANCE_TAX_NON_FILER_TO_T24);
                    switchWrapper.setToAccountNo(stakeholderBankInfoModel.getAccountNo());

                    StakeholderBankInfoModel stakeholderBankInfoModel1 = stakeholderBankInfoManager.loadStakeholderBankInfoModel
                            (PoolAccountConstantsInterface.ADVANCE_TAX_NON_FILER_FROM_T24);
                    switchWrapper.setFromAccountNo(stakeholderBankInfoModel1.getAccountNo());
                }

                I8SBSwitchControllerRequestVO requestVO = ESBAdapter.prepareFundsTransferForSalaryDisbursement
                        (I8SBConstants.RequestType_InterBankFundTransfer);

                I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
                if(switchWrapper.getWorkFlowWrapper().getFilerRate() != null && switchWrapper.getWorkFlowWrapper().getFilerRate() > 0.0){
                    requestVO.setTransactionAmount(String.valueOf(switchWrapper.getWorkFlowWrapper().getFilerRate()));
                    requestVO.setReserved2("AdvTax236Y1");
                }
                else{
                    requestVO.setTransactionAmount(String.valueOf(switchWrapper.getWorkFlowWrapper().getNonFilerRate()));
                    requestVO.setReserved2("AdvTax236Y3");
                }
                requestVO.setAccountId1(EncryptionUtil.encryptWithAES("682ede816988e58fb6d057d9d85605e0", switchWrapper.getToAccountNo()));
                requestVO.setAccountId2(EncryptionUtil.encryptWithAES("682ede816988e58fb6d057d9d85605e0", switchWrapper.getFromAccountNo()));
                SwitchWrapper i8sbSwitchWrapper = new SwitchWrapperImpl();

                i8sbSwitchWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                requestVO = i8sbSwitchWrapper.getI8SBSwitchControllerRequestVO();
                responseVO = requestVO.getI8SBSwitchControllerResponseVO();

//                responseVO.setResponseCode("00");
//                responseVO.setSTAN("214587");
                i8sbSwitchWrapper = this.esbAdapter.makeI8SBCall(i8sbSwitchWrapper);
                responseVO = i8sbSwitchWrapper.getI8SBSwitchControllerResponseVO();
                ESBAdapter.processI8sbResponseCode(responseVO, false);
                if (!responseVO.getResponseCode().equals("I8SB-200")) {
                    throw new WorkFlowException("Error in Funds Transfer");
                }
                MiddlewareMessageVO middlewareMessageVO = new MiddlewareMessageVO();
                middlewareMessageVO.setAccountNo1(switchWrapper.getToAccountNo());
                middlewareMessageVO.setAccountNo2(switchWrapper.getFromAccountNo());
                middlewareMessageVO.setTransactionAmount(String.valueOf(switchWrapper.getWorkFlowWrapper().getTransactionAmount()));
                middlewareMessageVO.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
                middlewareMessageVO.setStan(responseVO.getSTAN());
                middlewareMessageVO.setTransactionAmount(requestVO.getTransactionAmount());
                middlewareMessageVO.setResponseCode(responseVO.getResponseCode());
                switchWrapper.setMiddlewareIntegrationMessageVO(middlewareMessageVO);
                switchWrapper.getWorkFlowWrapper().putObject(CommandFieldConstants.KEY_STAN, middlewareMessageVO.getStan());
                baseWrapper = createOrUpdatePostedTransactionBeforeCall(switchWrapper, intgTransactionType);
            }

            olaVO = olaSwitch.creditTransfer(olaVO);
            logger.info("Product ID ( " + productId + " ) Total time taken to execute creditTransfer() [OLASwitchImpl.debitCreditAccount()] " +
                    ":: " + String.valueOf(DateTimeUtils.currentTimeMillis() - eTime));
            auditLogModel.setIntegrationPartnerIdentifier(olaVO.getAuthCode());
            if(switchWrapper.getWorkFlowWrapper().getTransactionModel() != null) {
                switchWrapper.getWorkFlowWrapper().getTransactionModel().setBankResponseCode(olaVO.getAuthCode());
            }
            switchWrapper.setBalance((olaVO.getBalance() != null) ? olaVO.getBalance() : 0);

            Double agentBalanceAfterTransaction = olaVO.getAgentBalanceAfterTransaction();

            agentBalanceAfterTransaction = (agentBalanceAfterTransaction == null ? 0.0D : agentBalanceAfterTransaction);

            switchWrapper.setAgentBalance(agentBalanceAfterTransaction);
            processOLAResponseCode(olaVO.getResponseCode());

            switchWrapper.setOlavo(olaVO);

        } catch (Exception ex) {
            logger.error("Exception occured " + ex);
            auditLogModel.setCustomField1(WorkFlowErrorCodeConstants.FAILURE);
            auditLogModel.setCustomField2(ex.getMessage());
            if (ex instanceof RemoteAccessException) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.SWITCH_INACTIVE);
            }
            if (ex instanceof CommandException) {
                ex.printStackTrace();
                throw (FrameworkCheckedException) ex;
            } else if (ex instanceof WorkFlowException) {
                switchWrapper.getWorkFlowWrapper().setErrorCode(ex.getMessage());
                throw (WorkFlowException) ex;

            } else
                throw new WorkFlowException(WorkFlowErrorCodeConstants.CONNECTION_FAILED_UBL_SWITCH);
        } finally {
            if (null != auditLogModel.getCustomField1() && auditLogModel.getCustomField1().equals(WorkFlowErrorCodeConstants.FAILURE) && (olaVO == null || olaVO.getResponseCode() == null || olaVO.getResponseCode().equals(""))) {
                this.auditLogAfterCall(auditLogModel, "");
            } else {
                this.auditLogAfterCall(auditLogModel, CommonUtils.getJSON(olaVO));
            }
            if (baseWrapper != null) {
                createOrUpdatePostedTransactionAfterCall(switchWrapper, baseWrapper);
            }
        }
        logger.info("Product ID ( " + productId + " ) End of [OLASwitchImpl.debitCreditAccount()] at the time :: " + new Date());
        eTime = DateTimeUtils.currentTimeMillis();
        logger.info("Product ID ( " + productId + " ) Total time taken to execute [OLASwitchImpl.debitCreditAccount()] :: " + String.valueOf(eTime - sTime));
        return switchWrapper;
    }


    /* (non-Javadoc)
     * @see com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchProcessor#billPayment(com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper)
     */
    @Override
    public SwitchWrapper billPayment(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {

        return  debitCreditAccount(switchWrapper);
    }

    @Override
    public SwitchWrapper getAccountTitle(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {

        if (logger.isDebugEnabled()) {
            logger.debug("Inside getAccountTitle of OLASwitchImpl");
        }
        AuditLogModel auditLogModel = this.auditLogBeforeCall(switchWrapper, "");
        OLAVO olaVO = switchWrapper.getOlavo();
        try {
            auditLogModel.setInputParam(CommonUtils.getJSON(olaVO));
            OLASwitch olaSwitch = this.getOlaSwitch(switchWrapper);
            if (olaSwitch == null) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.CONNECTION_FAILED_UBL_SWITCH);
            }

            olaVO = olaService.getAccountTitle(olaVO);

            processOLAResponseCode(olaVO.getResponseCode());

            switchWrapper.setOlavo(olaVO);

        } catch (Exception ex) {
            logger.error("Exception occured " + ex);
            auditLogModel.setCustomField1(WorkFlowErrorCodeConstants.FAILURE);
            auditLogModel.setCustomField2(ex.getMessage());
            if (ex instanceof RemoteAccessException) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.SWITCH_INACTIVE);
            } else if (ex instanceof WorkFlowException)
                throw (WorkFlowException) ex;
            else
                throw new WorkFlowException(WorkFlowErrorCodeConstants.CONNECTION_FAILED_UBL_SWITCH);
        } finally {
            if (null != auditLogModel.getCustomField1() && auditLogModel.getCustomField1().equals(WorkFlowErrorCodeConstants.FAILURE) && (olaVO == null || olaVO.getResponseCode() == null || olaVO.getResponseCode().equals(""))) {
                this.auditLogAfterCall(auditLogModel, "");
            } else {
                this.auditLogAfterCall(auditLogModel, CommonUtils.getJSON(olaVO));
            }

        }

        return switchWrapper;
    }

    /*
     * Helper method to translate OLA Response Codes.
     *
     * if Response Codes is other than "00" then send appropriate error message
     */
    private void processOLAResponseCode(String responseCode) throws WorkFlowException {

        logger.info("[OLASwitchImpl.processOLAResponseCode] ==> OLA Resp Code:" + responseCode);

        switch (responseCode) {

            case "00":
                break;

            case "01":
                throw new WorkFlowException(WorkFlowErrorCodeConstants.INSUFFICIENT_ACC_BALANCE);

            case "02":
                throw new WorkFlowException(WorkFlowErrorCodeConstants.INACTIVE_ACCOUNT);

            case "03":
                throw new WorkFlowException(WorkFlowErrorCodeConstants.DELETED_ACCOUNT);

            case "04":
                throw new WorkFlowException(WorkFlowErrorCodeConstants.DATA_TYPE_MISMATCH);

// Following handled in default case
//			case "05" : throw new WorkFlowException( WorkFlowErrorCodeConstants.TRANSACTION_TIMEOUT );
//			case "06" : throw new WorkFlowException( WorkFlowErrorCodeConstants.TRANSACTION_TIMEOUT );
//			case "07" : break;


            case "08":
                throw new WorkFlowException(WorkFlowErrorCodeConstants.GENERAL_ERROR);

            case "09":
                throw new WorkFlowException(WorkFlowErrorCodeConstants.DAILY_CREDIT_LIMIT_BUSTED);

            case "10":
                throw new WorkFlowException(WorkFlowErrorCodeConstants.GENERAL_ERROR);

            case "11":
                throw new WorkFlowException(WorkFlowErrorCodeConstants.MONTHLY_CREDIT_LIMIT_BUSTED);

            case "12":
                throw new WorkFlowException(WorkFlowErrorCodeConstants.GENERAL_ERROR);

            case "13":
                throw new WorkFlowException(WorkFlowErrorCodeConstants.YEARLY_CREDIT_LIMIT_BUSTED);

            case "14":
                throw new WorkFlowException(WorkFlowErrorCodeConstants.GENERAL_ERROR);

            case "15":
                throw new WorkFlowException(WorkFlowErrorCodeConstants.BALANCE_LIMIT_BUSTED);

            case "16":
                throw new WorkFlowException(WorkFlowErrorCodeConstants.GENERAL_ERROR);

            case "17":
                throw new WorkFlowException(WorkFlowErrorCodeConstants.DAILY_DEBIT_LIMIT_BUSTED);

            case "18":
                throw new WorkFlowException(WorkFlowErrorCodeConstants.GENERAL_ERROR);

            case "19":
                throw new WorkFlowException(WorkFlowErrorCodeConstants.MONTHLY_DEBIT_LIMIT_BUSTED);

            case "20":
                throw new WorkFlowException(WorkFlowErrorCodeConstants.GENERAL_ERROR);

            case "21":
                throw new WorkFlowException(WorkFlowErrorCodeConstants.YEARLY_DEBIT_LIMIT_BUSTED);


            case "29":
                throw new WorkFlowException(WorkFlowErrorCodeConstants.MONTHLY_THROUGHPUT_LIMIT_NOT_DEFINED);

            case "30":
                throw new WorkFlowException(WorkFlowErrorCodeConstants.MONTHLY_CREDIT_THROUGHPUT_LIMIT_BUSTED);

            case "31":
                throw new WorkFlowException(WorkFlowErrorCodeConstants.MONTHLY_THROUGHPUT_LIMIT_EXCEPTION);

            case "32":
                throw new WorkFlowException(WorkFlowErrorCodeConstants.MONTHLY_DEBIT_THROUGHPUT_LIMIT_BUSTED);

            case "33":
                throw new WorkFlowException(WorkFlowErrorCodeConstants.MONTHLY_BVS_LIMIT_NOT_DEFINED);

            case "50":
                throw new WorkFlowException(WorkFlowErrorCodeConstants.LEG2_ALREADY_PERFORMED);

            case "51":
                throw new WorkFlowException(WorkFlowErrorCodeConstants.DEBIT_CREDIT_NOT_EQUAL);


            default: {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.TRANSACTION_TIMEOUT);
            }


        }
    }

    public SwitchWrapper verifyLimits(SwitchWrapper switchWrapper) throws Exception {

        OLAVO olaVO = olaService.verifyLimits(switchWrapper.getOlavo());

        if (olaVO != null && !StringUtil.isNullOrEmpty(olaVO.getResponseCode()) &&
                !olaVO.getResponseCode().equalsIgnoreCase(ResponseCodeEnum.PROCESSED_OK.getValue())) {

            processOLAResponseCode(olaVO.getResponseCode());
        } else {

        }

        return switchWrapper;
    }
}
