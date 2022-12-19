package com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.NADRA;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.XMLUtil;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AuditLogModel;
import com.inov8.microbank.common.util.HttpInvokerUtil;
import com.inov8.microbank.common.util.IntgTransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.NADRAConstantsInterface;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.common.util.XPathConstants;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchProcessor;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix.PhoenixConstantsInterface;
import com.inov8.microbank.server.service.integration.vo.UtilityBillVO;
import com.inov8.microbank.server.service.switchmodule.iris.SwitchController;
import com.inov8.microbank.server.service.switchmodule.iris.model.PhoenixIntegrationMessageVO;
import com.thoughtworks.xstream.XStream;

public class NADRASwitchImpl extends SwitchProcessor{
	protected static Log logger = LogFactory.getLog(NADRASwitchImpl.class);
	protected ApplicationContext ctx;
	private MessageSource messageSource;
	
	public NADRASwitchImpl(ApplicationContext ctx) {
		this.ctx = ctx;
	}
	public NADRASwitchImpl() {
		
	}
	
	@Override
	public SwitchWrapper payBill(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
		logger.debug("Inside bill Payment method of NADRASwitchImpl");
		AuditLogModel auditLogModel = this.auditLogBeforeCall(switchWrapper, "");
		BaseWrapper baseWrapper = createOrUpdatePostedTransactionBeforeCall(switchWrapper, IntgTransactionTypeConstantsInterface.BILL_PAYMENT_NADRA);
		XStream xStream = new XStream();
		PhoenixIntegrationMessageVO phoenixIntegrationMessageVO = null;
		if(ThreadLocalAppUser.getAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue())
		{
			phoenixIntegrationMessageVO = new PhoenixIntegrationMessageVO(false, null, null);
		}else{
			phoenixIntegrationMessageVO = new PhoenixIntegrationMessageVO(false, switchWrapper.getSwitchSwitchModel().getPaymentGatewayCode(), null);
		}
				

		if (switchWrapper != null) {
			try {
				//added by mudassir - set Transaction code and transaction Code Id in PhoenixIntegrationMessageVO
				phoenixIntegrationMessageVO.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
				phoenixIntegrationMessageVO.setMicrobankTransactionCodeId(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

				phoenixIntegrationMessageVO.setCompanyName(switchWrapper.getUtilityCompanyId());
				phoenixIntegrationMessageVO.setConsumerNumber(switchWrapper.getConsumerNumber());
				phoenixIntegrationMessageVO.setBillAmount(switchWrapper.getAmountPaid());
				phoenixIntegrationMessageVO.setTrackingId(NADRAConstantsInterface.KIOSK_ID+"00"+switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
				
				
				String billMonth = ((UtilityBillVO)(switchWrapper.getWorkFlowWrapper().getProductVO())).getBillingMonth();
				SimpleDateFormat df = new SimpleDateFormat("MMM yyyy");
				Date billingMonthDate = df.parse(billMonth);
				LocalDate lc = new LocalDate(billingMonthDate);
				DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM");
				String month = formatter.print(lc);
				phoenixIntegrationMessageVO.setBillMonth(month);
//				phoenixIntegrationMessageVO.setAccountNumber(switchWrapper.getFromAccountNo());
//				phoenixIntegrationMessageVO.setAccountType(switchWrapper.getFromAccountType());
//				phoenixIntegrationMessageVO.setAccountCurrency(switchWrapper.getCurrencyCode());

//				phoenixIntegrationMessageVO.setCustomerIdentification(PhoenixConstantsInterface.ASKARI_BANK_IMD);
//				phoenixIntegrationMessageVO = prepareCNIC(phoenixIntegrationMessageVO);

				auditLogModel.setInputParam(XMLUtil.replaceElementsUsingXPath(xStream.toXML(phoenixIntegrationMessageVO),
						XPathConstants.PhoenixAuditLogInputParamLocationSteps));
				auditLogModel.setActionLogId(ThreadLocalActionLog.getActionLogId());

				SwitchController integrationSwitch = getIntegrationSwitch(switchWrapper);

				if (integrationSwitch == null) {
					logger.error("Phoenix switch down..escalating the exception");
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
				}
				logger.debug("Making call to NADRA for billPayment...");

				phoenixIntegrationMessageVO = (PhoenixIntegrationMessageVO) integrationSwitch.payBill(phoenixIntegrationMessageVO);
				phoenixIntegrationMessageVO.setRetrievalReferenceNumber(phoenixIntegrationMessageVO.getTrackingId());
				phoenixIntegrationMessageVO.setResponseCode(phoenixIntegrationMessageVO.getStatus());

				auditLogModel.setCustomField1(PhoenixConstantsInterface.SUCCESS);
				auditLogModel.setIntegrationPartnerIdentifier(phoenixIntegrationMessageVO.getTrackingId());

				if (phoenixIntegrationMessageVO != null) {
					switchWrapper.setIntegrationMessageVO(phoenixIntegrationMessageVO);
					parseResponseCode(phoenixIntegrationMessageVO);

				}
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error("Exception occured in NADRASwitchImpl.... " + ex);
				auditLogModel.setCustomField1(PhoenixConstantsInterface.FAILURE);
				auditLogModel.setCustomField2(ex.getMessage());

				if (ex instanceof WorkFlowException)
					throw (WorkFlowException) ex;
				else if (ex instanceof IOException)
					throw new WorkFlowException(MessageUtil.getMessage("genericErrorMessage"));
				else if (ex instanceof RemoteAccessException)
					throw new WorkFlowException(MessageUtil.getMessage("genericErrorMessage"));
				else
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
			} finally {
				if (phoenixIntegrationMessageVO != null) {
					phoenixIntegrationMessageVO.setMessageAsEdi("");
					this.auditLogAfterCall(auditLogModel, XMLUtil.replaceElementsUsingXPath(xStream.toXML(phoenixIntegrationMessageVO),
							XPathConstants.PhoenixAuditLogInputParamLocationSteps));
					if(baseWrapper != null){
						createOrUpdatePostedTransactionAfterCall(switchWrapper, baseWrapper);
					}
				}
			}

			logger.debug("Ending billPayment method of PhoenixSwitchImpl");
		}
		return switchWrapper;
	}
	
	protected SwitchController getIntegrationSwitch(SwitchWrapper switchWrapper) {
		return HttpInvokerUtil.getHttpInvokerFactoryBean(SwitchController.class, 
					switchWrapper.getWorkFlowWrapper().getProductModel().getProductIntgModuleInfoIdProductIntgModuleInfoModel().getUrl());

	}
	
	protected void parseResponseCode(PhoenixIntegrationMessageVO phoenixIntegrationMessageVO) {
		if (phoenixIntegrationMessageVO.getStatus() == null) {
			logger.error("PHOENIX is down....."
					+ ExceptionProcessorUtility.prepareExceptionStackTrace(new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN)));
			throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN);
		} else if (!phoenixIntegrationMessageVO.getStatus().equalsIgnoreCase("ok")) {
			logger.error("Payment Services Processing Failed....."
					+ ExceptionProcessorUtility.prepareExceptionStackTrace(new WorkFlowException(WorkFlowErrorCodeConstants.PAYMENT_PROCESSING_FAILED)));

			logger.error("========> Response code from NADRA : " + phoenixIntegrationMessageVO.getStatus());

			String message = "";

			

			try {
				message = MessageUtil.getMessage("genericErrorMessage");
			} catch (NoSuchMessageException e) {
				message = WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG;
			}

			throw new WorkFlowException(message);
		}
	}

	@Override
	public SwitchWrapper checkBalance(SwitchWrapper accountInfo)
			throws WorkFlowException, FrameworkCheckedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SwitchWrapper transaction(SwitchWrapper transactions)
			throws WorkFlowException, FrameworkCheckedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SwitchWrapper rollback(SwitchWrapper transactions)
			throws WorkFlowException, FrameworkCheckedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SwitchWrapper getLedger(SwitchWrapper switchWrapper)
			throws WorkFlowException, FrameworkCheckedException, Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
