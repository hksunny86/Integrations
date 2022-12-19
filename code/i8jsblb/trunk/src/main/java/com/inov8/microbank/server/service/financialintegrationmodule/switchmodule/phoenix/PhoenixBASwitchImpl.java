package com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix;

/**
 * Project Name: 			Microbank	
 * @author 					Jawwad Farooq
 * Creation Date: 			February 2008  			
 * Description:				
 */

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.remoting.RemoteAccessException;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.XMLUtil;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.integration.util.FormatUtils;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AuditLogModel;
import com.inov8.microbank.common.util.BillAuthorizationSender;
import com.inov8.microbank.common.util.InternetCompanyEnum;
import com.inov8.microbank.common.util.IntgTransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.NadraCompanyEnum;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.TransactionConstantsInterface;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.UtilityCompanyEnum;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.common.util.XPathConstants;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.server.service.integration.vo.CreditCardPaymentVO;
import com.inov8.microbank.server.service.switchmodule.IntegrationMessageVO;
import com.inov8.microbank.server.service.switchmodule.PhoenixTransactionVO;
import com.inov8.microbank.server.service.switchmodule.iris.IntegrationConstants;
import com.inov8.microbank.server.service.switchmodule.iris.SwitchController;
import com.inov8.microbank.server.service.switchmodule.iris.model.PhoenixIntegrationMessageVO;
import com.thoughtworks.xstream.XStream;

public class PhoenixBASwitchImpl extends PhoenixSwitchImpl {

	private BillAuthorizationSender billAuthorizationSender;
	
	public PhoenixBASwitchImpl(ApplicationContext ctx) {
		super(ctx);
		billAuthorizationSender = (BillAuthorizationSender) ctx.getBean("billAuthorizationSender");
	}

	@Override
	public SwitchWrapper billPayment(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
		
		logger.debug("Inside bill Payment method of PhoenixSwitchImpl");
		
		AuditLogModel auditLogModel = this.auditLogBeforeCall(switchWrapper, "");
		BaseWrapper baseWrapper = createOrUpdatePostedTransactionBeforeCall(switchWrapper, IntgTransactionTypeConstantsInterface.BILL_PAYMENT_PHOENIX);
		XStream xStream = new XStream();
		
		String paymentGatewayCode = null;
	    Long productId = switchWrapper.getWorkFlowWrapper().getProductModel().getProductId();
		
		if(switchWrapper.getSwitchSwitchModel() != null) {
			
			paymentGatewayCode = switchWrapper.getSwitchSwitchModel().getPaymentGatewayCode();
		}
		
		PhoenixIntegrationMessageVO phoenixIntegrationMessageVO = new PhoenixIntegrationMessageVO(Boolean.FALSE, paymentGatewayCode, null);

		try {
			//added by mudassir - set Transaction code and transaction Code Id in PhoenixIntegrationMessageVO
			phoenixIntegrationMessageVO.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
			phoenixIntegrationMessageVO.setMicrobankTransactionCodeId(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());
			
			phoenixIntegrationMessageVO.setUtilityCompanyId(switchWrapper.getUtilityCompanyId());
			phoenixIntegrationMessageVO.setConsumerNumber(switchWrapper.getConsumerNumber());
			phoenixIntegrationMessageVO.setAmountPaid(switchWrapper.getAmountPaid());

			phoenixIntegrationMessageVO.setAccountNumber(switchWrapper.getFromAccountNo());
			phoenixIntegrationMessageVO.setAccountType(switchWrapper.getFromAccountType());
			phoenixIntegrationMessageVO.setAccountCurrency(switchWrapper.getCurrencyCode());
			phoenixIntegrationMessageVO.setCustomerIdentification(PhoenixConstantsInterface.ASKARI_BANK_IMD);
//			phoenixIntegrationMessageVO = prepareCNIC(phoenixIntegrationMessageVO);

	        Long transactionTypeId = switchWrapper.getWorkFlowWrapper().getTransactionTypeModel().getTransactionTypeId();
	        String serviceUrl = null;
	            
	        if(transactionTypeId != null && 
	         		(transactionTypeId.longValue() == TransactionTypeConstantsInterface.AGENT_NADRA_BILL_SALE_TX.longValue() ||
	          			transactionTypeId.longValue() == TransactionTypeConstantsInterface.CUSTOMER_NADRA_BILL_SALE_TX.longValue() ||
	          				NadraCompanyEnum.contains(String.valueOf(productId)))) {

	           	serviceUrl = switchWrapper.getWorkFlowWrapper().getProductModel().getProductIntgModuleInfoIdProductIntgModuleInfoModel().getUrl();
	           	logger.info("NADRA_BILL_SALE_TX URL : "+serviceUrl);
	           	
	        } else if(transactionTypeId != null && transactionTypeId.longValue() == TransactionTypeConstantsInterface.UTILITY_BILL_SALE.longValue()
	        		 || UtilityCompanyEnum.contains(String.valueOf(productId)) || InternetCompanyEnum.contains(String.valueOf(productId))) {
	        			 
	           	serviceUrl = switchWrapper.getSwitchSwitchModel().getUrl();
	           	logger.info("UTILITY_BILL_SALE_TX URL : "+serviceUrl);
	        }			

			phoenixIntegrationMessageVO.setServiceUrl(serviceUrl);
				
			auditLogModel.setInputParam(XMLUtil.replaceElementsUsingXPath(xStream.toXML(phoenixIntegrationMessageVO), XPathConstants.PhoenixAuditLogInputParamLocationSteps));
			auditLogModel.setActionLogId(ThreadLocalActionLog.getActionLogId());

//				SwitchController integrationSwitch = getIntegrationSwitch(switchWrapper);
//
//				if (integrationSwitch == null) {
//					logger.error("Phoenix switch down..escalating the exception");
//					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
//				}
				

				// DISABLED DIRECT / REALTIME billPaymentAdvice.
//				logger.debug("Making call to PHOENIX for billPayment... ");
//				phoenixIntegrationMessageVO = (PhoenixIntegrationMessageVO) integrationSwitch.billPaymentAdvice(phoenixIntegrationMessageVO);
				
				
			authorizeBillPayment(phoenixIntegrationMessageVO);
			
			auditLogModel.setCustomField1(PhoenixConstantsInterface.SUCCESS);
			auditLogModel.setIntegrationPartnerIdentifier(phoenixIntegrationMessageVO.getSystemTraceAuditNumber());

			if (phoenixIntegrationMessageVO != null) {
				phoenixIntegrationMessageVO.setResponseCode("00");
				switchWrapper.setIntegrationMessageVO(phoenixIntegrationMessageVO);
				parseResponseCode(phoenixIntegrationMessageVO);
			}
		} catch (Exception ex) {

			logger.error("Exception occured in PhoenixBASwitchImpl.... " + ex);
			auditLogModel.setCustomField1(PhoenixConstantsInterface.FAILURE);
			auditLogModel.setCustomField2(ex.getMessage());

			if (ex instanceof WorkFlowException) {
				throw (WorkFlowException) ex;
			} else if (ex instanceof IOException) {
					throw new WorkFlowException(MessageUtil.getMessage("genericErrorMessage"));
			} else if (ex instanceof RemoteAccessException) {
					throw new WorkFlowException(MessageUtil.getMessage("genericErrorMessage"));
			} else {
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
			}
		} finally {
			
			if (phoenixIntegrationMessageVO != null) {
				phoenixIntegrationMessageVO.setMessageAsEdi("");
				this.auditLogAfterCall(auditLogModel, XMLUtil.replaceElementsUsingXPath(xStream.toXML(phoenixIntegrationMessageVO),	XPathConstants.PhoenixAuditLogInputParamLocationSteps));
				
				if(baseWrapper != null) {
					createOrUpdatePostedTransactionAfterCall(switchWrapper, baseWrapper);
				}
			}
			
			logger.debug("Ending billPayment method of PhoenixSwitchImpl");
		}
		
		return switchWrapper;
	}
	
	
	
	/**
	 * @param phoenixIntegrationMessageVO
	 * @throws FrameworkCheckedException
	 */
	private void authorizeBillPayment(PhoenixIntegrationMessageVO phoenixIntegrationMessageVO) throws FrameworkCheckedException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("SENDING billPaymentAdvice VIA Queue \n URI > "+phoenixIntegrationMessageVO.getServiceUrl());
		}

		billAuthorizationSender.send(phoenixIntegrationMessageVO);
	}


	@Override
	public SwitchWrapper billInquiry(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
		if (logger.isDebugEnabled()) {
			logger.debug("Inside billInquiry method of PhoenixSwitchImpl");
		}
		AuditLogModel auditLogModel = this.auditLogBeforeCall(switchWrapper, "");
		BaseWrapper baseWrapper = createOrUpdatePostedTransactionBeforeCall(switchWrapper, IntgTransactionTypeConstantsInterface.BILL_INQUIRY_PHOENIX);	
		
		XStream xStream = new XStream();

		PhoenixIntegrationMessageVO phoenixIntegrationMessageVO = new PhoenixIntegrationMessageVO(false, switchWrapper.getSwitchSwitchModel()
				.getPaymentGatewayCode(), null);

		if (switchWrapper != null) {
			try {
				phoenixIntegrationMessageVO.setUtilityCompanyId(switchWrapper.getUtilityCompanyId());
				phoenixIntegrationMessageVO.setConsumerNumber(switchWrapper.getConsumerNumber());

				phoenixIntegrationMessageVO.setCustomerIdentification(PhoenixConstantsInterface.ASKARI_BANK_IMD);
//				phoenixIntegrationMessageVO = prepareCNIC(phoenixIntegrationMessageVO);

				auditLogModel.setInputParam(XMLUtil.replaceElementsUsingXPath(xStream.toXML(phoenixIntegrationMessageVO),
						XPathConstants.PhoenixAuditLogInputParamLocationSteps));
				auditLogModel.setActionLogId(ThreadLocalActionLog.getActionLogId());

				SwitchController integrationSwitch = getIntegrationSwitch(switchWrapper);

				if (switchWrapper.getWorkFlowWrapper() != null && switchWrapper.getWorkFlowWrapper().getTransactionCodeModel() != null){
					phoenixIntegrationMessageVO.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
					phoenixIntegrationMessageVO.setMicrobankTransactionCodeId(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());
				}

				if (integrationSwitch == null) {
					logger.error("Phoenix switch down..escalating the exception");
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
				}

				logger.info("[PhoenixBASwitchImpl.billInquiry] Hitting PHOENIX for Bill Inquiry.. Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
				long startTime = Calendar.getInstance().getTimeInMillis();
				
				phoenixIntegrationMessageVO = (PhoenixIntegrationMessageVO) integrationSwitch.billInquiry(phoenixIntegrationMessageVO);

				long timeConsumed = Calendar.getInstance().getTimeInMillis() - startTime;
				
				auditLogModel.setCustomField1(PhoenixConstantsInterface.SUCCESS);
				auditLogModel.setIntegrationPartnerIdentifier(phoenixIntegrationMessageVO.getSystemTraceAuditNumber());

				if (phoenixIntegrationMessageVO != null) {
					
					if(timeConsumed == 0l){//to avoid divide by zero exception..
						timeConsumed = 1l;
					}
					
					logger.info("[PhoenixBASwitchImpl.billInquiry] Bill Inquiry Response: " + phoenixIntegrationMessageVO.getResponseCode() + ". Processing Time: "+ (timeConsumed/1000d) + " Seconds.  Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());

					switchWrapper.setIntegrationMessageVO(phoenixIntegrationMessageVO);
					parseResponseCode(phoenixIntegrationMessageVO);

				}
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error("Exception occured in PhoenixBASwitchImpl.... " + ex);
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
					this.auditLogAfterCall(auditLogModel, XMLUtil.replaceElementsUsingXPath(xStream.toXML(phoenixIntegrationMessageVO),	XPathConstants.PhoenixAuditLogInputParamLocationSteps));
					
					if(baseWrapper != null) {
						createOrUpdatePostedTransactionAfterCall(switchWrapper, baseWrapper);
					}
				}
								
				
				
				
				
			}
			String responseCode = phoenixIntegrationMessageVO.getResponseCode();

			if (responseCode == null) {
				logger.error("PHOENIX is down....."
						+ ExceptionProcessorUtility.prepareExceptionStackTrace(new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN)));
				throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN);
			} else {
				if (responseCode.equalsIgnoreCase(IntegrationConstants.PhoenixResponseCodes.OK.getResponseCodeValue())) {
					switchWrapper.setSubscriberName(phoenixIntegrationMessageVO.getSubscriberName());
					switchWrapper.setBillingMonth(phoenixIntegrationMessageVO.getBillingMonth());
					switchWrapper.setDueDatePayableAmount(phoenixIntegrationMessageVO.getDueDatePayableAmount());
					switchWrapper.setPaymentDueDate(phoenixIntegrationMessageVO.getPaymentDueDate());
					switchWrapper.setPaymentAfterDueDate(phoenixIntegrationMessageVO.getPaymentAfterDueDate());
					switchWrapper.setBillStatus(phoenixIntegrationMessageVO.getBillStatus());
					switchWrapper.setPaymentAuthResponseId(phoenixIntegrationMessageVO.getPaymentAuthResponseId());
					switchWrapper.setNetCED(phoenixIntegrationMessageVO.getNetCED());
					switchWrapper.setNetWithholdingTAX(phoenixIntegrationMessageVO.getNetWithholdingTAX());
				} else {
					String msg = getMessageFromResource(phoenixIntegrationMessageVO.getResponseCode());
					logger.error(msg);
					throw new WorkFlowException(msg);
				}
			}

			logger.debug("Ending billInquiry method of PhoenixSwitchImpl");
		}
		return switchWrapper;
	}

	public SwitchWrapper titleFetch(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
		if (logger.isDebugEnabled()) {
			logger.debug("Inside titleFetch method of PhoenixSwitchImpl");
		}
		AuditLogModel auditLogModel = this.auditLogBeforeCall(switchWrapper, "");
		BaseWrapper baseWrapper = createOrUpdatePostedTransactionBeforeCall(switchWrapper, IntgTransactionTypeConstantsInterface.TITLE_FETCH_PHOENIX);
		XStream xStream = new XStream();

		PhoenixIntegrationMessageVO phoenixIntegrationMessageVO = new PhoenixIntegrationMessageVO(false, switchWrapper.getSwitchSwitchModel()
				.getPaymentGatewayCode(), null);

		if (switchWrapper != null) {
			try {
				phoenixIntegrationMessageVO.setAccountNumber(switchWrapper.getCustomerAccount().getNumber());
				phoenixIntegrationMessageVO.setAccountType(switchWrapper.getCustomerAccount().getType());
				phoenixIntegrationMessageVO.setAccountCurrency(switchWrapper.getCustomerAccount().getCurrency());

				phoenixIntegrationMessageVO.setCustomerIdentification(PhoenixConstantsInterface.ASKARI_BANK_IMD);
				phoenixIntegrationMessageVO.setAccountBankIMD(PhoenixConstantsInterface.ASKARI_BANK_IMD);
//				phoenixIntegrationMessageVO = prepareCNIC(phoenixIntegrationMessageVO);

				auditLogModel.setInputParam(XMLUtil.replaceElementsUsingXPath(xStream.toXML(phoenixIntegrationMessageVO),
						XPathConstants.PhoenixAuditLogInputParamLocationSteps));
				auditLogModel.setActionLogId(ThreadLocalActionLog.getActionLogId());

				SwitchController integrationSwitch = getIntegrationSwitch(switchWrapper);

				if (integrationSwitch == null) {
					logger.error("Phoenix switch down..escalating the exception");
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
				}

				logger.info("[PhoenixBASwitchImpl.titleFetch] Hitting PHOENIX for Title Fetch.. Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
				long startTime = Calendar.getInstance().getTimeInMillis();
				
				phoenixIntegrationMessageVO = (PhoenixIntegrationMessageVO) integrationSwitch.titleFetch(phoenixIntegrationMessageVO);

				long timeConsumed = Calendar.getInstance().getTimeInMillis() - startTime;
				
				auditLogModel.setCustomField1(PhoenixConstantsInterface.SUCCESS);
				auditLogModel.setIntegrationPartnerIdentifier(phoenixIntegrationMessageVO.getSystemTraceAuditNumber());

				if (phoenixIntegrationMessageVO != null) {
					
					if(timeConsumed == 0l){//to avoid divide by zero exception..
						timeConsumed = 1l;
					}
					
					logger.info("[PhoenixBASwitchImpl.titleFetch] Title Fetch Response: " + phoenixIntegrationMessageVO.getResponseCode() + ". Processing Time: "+ (timeConsumed/1000d) + " Seconds.  Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());

					switchWrapper.setIntegrationMessageVO(phoenixIntegrationMessageVO);
					parseResponseCode(phoenixIntegrationMessageVO);

					if (phoenixIntegrationMessageVO.getTitleOfTheAccount() != null) {
						switchWrapper.getCustomerAccount().setTitleOfTheAccount(phoenixIntegrationMessageVO.getTitleOfTheAccount());

					} else {
						throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_INVALID_ACCOUNT);
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error("Exception occured in PhoenixBASwitchImpl.... " + ex);
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
//				if (phoenixIntegrationMessageVO != null) {
//					phoenixIntegrationMessageVO.setMessageAsEdi("");
//					this.auditLogAfterCall(auditLogModel, XMLUtil.replaceElementsUsingXPath(xStream.toXML(phoenixIntegrationMessageVO),
//							XPathConstants.PhoenixAuditLogInputParamLocationSteps));
//				}

				if(baseWrapper != null){
					createOrUpdatePostedTransactionAfterCall(switchWrapper, baseWrapper);
				}
			}
			logger.debug("Ending titleFetch method of PhoenixSwitchImpl");
		}
		return switchWrapper;
	}

	public SwitchWrapper getPhoenixTransactions(SwitchWrapper switchWrapper) throws Exception{
		
		List<PhoenixTransactionVO> transList = new ArrayList<PhoenixTransactionVO>();
		if (switchWrapper != null) {
			try {
				SwitchController integrationSwitch = getIntegrationSwitch(switchWrapper);

				if (integrationSwitch == null) {
					logger.error("Phoenix switch down..escalating the exception");
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
				}
				logger.debug("Making call to PHOENIX for getPhoenixTransactions...");
				transList = integrationSwitch.getPhoenixTransactions(switchWrapper.getPhoenixTransactionVO());
				switchWrapper.setPhoenixTransactionList(transList);
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error("Exception occured in PhoenixBASwitchImpl.... " + ex);
				if (ex instanceof WorkFlowException)
					throw (WorkFlowException) ex;
				else if (ex instanceof IOException)
					throw new WorkFlowException(MessageUtil.getMessage("genericErrorMessage"));
				else if (ex instanceof RemoteAccessException)
					throw new WorkFlowException(MessageUtil.getMessage("genericErrorMessage"));
				else
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
			}
			logger.debug("Ending getPhoenixTransactions method of PhoenixSwitchImpl");
		}
		return switchWrapper;
	}

	@Override
	public SwitchWrapper debitCreditAccount(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
		logger.debug("Inside doOpenAccountFundTransfer(debitCreditAccount()) method of PhoenixSwitchImpl");
		
		AuditLogModel auditLogModel = this.auditLogBeforeCall(switchWrapper, "");
		BaseWrapper baseWrapper = createOrUpdatePostedTransactionBeforeCall(switchWrapper, IntgTransactionTypeConstantsInterface.CREDIT_ACCOUNT_ADVICE_CORE);
		XStream xStream = new XStream();

		if (switchWrapper != null) {
			PhoenixIntegrationMessageVO phoenixIntegrationMessageVO = (PhoenixIntegrationMessageVO) switchWrapper.getIntegrationMessageVO();

			try {
				
					// FROM ACCOUNT INFO
					phoenixIntegrationMessageVO.setAccountNumber(switchWrapper.getFromAccountNo());
					phoenixIntegrationMessageVO.setAccountType(switchWrapper.getFromAccountType());
					phoenixIntegrationMessageVO.setToAccountCurrency(switchWrapper.getFromCurrencyCode());

					// TO ACCOUNT INFO
					phoenixIntegrationMessageVO.setToAccountNumber(switchWrapper.getToAccountNo());
					phoenixIntegrationMessageVO.setToAccountType(switchWrapper.getToAccountType());
					phoenixIntegrationMessageVO.setAccountCurrency(switchWrapper.getToCurrencyCode());

					BigDecimal totalAmnt = new BigDecimal(switchWrapper.getTransactionAmount());
					phoenixIntegrationMessageVO.setTransactionAmount(FormatUtils.formatAmount(totalAmnt));
//					phoenixIntegrationMessageVO.setTransactionAmount(switchWrapper.getTransactionAmount().toString());
					phoenixIntegrationMessageVO.setTransactionCurrency(switchWrapper.getCurrencyCode());

//					phoenixIntegrationMessageVO.setCustomerPINData((String) switchWrapper.getObject(CommandFieldConstants.KEY_PIN));
					phoenixIntegrationMessageVO.setCustomerIdentification(PhoenixConstantsInterface.ASKARI_BANK_IMD);
//					phoenixIntegrationMessageVO = prepareCNIC(phoenixIntegrationMessageVO);

					if (switchWrapper.getWorkFlowWrapper() != null && switchWrapper.getWorkFlowWrapper().getTransactionCodeModel() != null){
						phoenixIntegrationMessageVO.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
						phoenixIntegrationMessageVO.setMicrobankTransactionCodeId(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());
					}

					auditLogModel.setInputParam(XMLUtil.replaceElementsUsingXPath(xStream.toXML(phoenixIntegrationMessageVO),
							XPathConstants.PhoenixAuditLogInputParamLocationSteps));

					SwitchController integrationSwitch = getIntegrationSwitch(switchWrapper);

					if (integrationSwitch == null) {
						throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN);
					}

					logger.info("[PhoenixBASwitchImpl.debitCreditAccount] Hitting PHOENIX. Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
					
					long startTime=Calendar.getInstance().getTimeInMillis();
					
					phoenixIntegrationMessageVO = (PhoenixIntegrationMessageVO) integrationSwitch.openAccountFundTransfer(phoenixIntegrationMessageVO);
					switchWrapper.setIntegrationMessageVO(phoenixIntegrationMessageVO);
					
					long timeConsumed = Calendar.getInstance().getTimeInMillis()-startTime;
					
					auditLogModel.setCustomField1(PhoenixConstantsInterface.SUCCESS);
					auditLogModel.setIntegrationPartnerIdentifier(phoenixIntegrationMessageVO.getRetrievalReferenceNumber());

					if (phoenixIntegrationMessageVO != null) {
						String responseCode = null;
						try {
							responseCode = phoenixIntegrationMessageVO.getResponseCode();
							
							if(timeConsumed == 0){//to avoid divide by zero exception..
								timeConsumed = 1;
							}
							
							logger.info("[PhoenixBASwitchImpl.debitCreditAccount] Response code from PHOENIX: " + responseCode + ". Processing Time: "+ (timeConsumed/1000d) + " Seconds. Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());

							parseResponseCode(phoenixIntegrationMessageVO);
						} catch (Exception e) {
							if (responseCode == null) {
								logger.error("PHOENIX is down....."
										+ ExceptionProcessorUtility.prepareExceptionStackTrace(new WorkFlowException(
												WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN)));
								throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN);
							} else if (responseCode.equalsIgnoreCase(IntegrationConstants.PhoenixResponseCodes.ACC_INACTIVE.getResponseCodeValue())) {
								throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_ACCOUNT_INACTIVE);
							} else if (responseCode.equalsIgnoreCase(IntegrationConstants.PhoenixResponseCodes.ACC_STATUS_INVALID.getResponseCodeValue())) {
								throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_INVALID_ACCOUNT_STATUS);
							} else if (responseCode.equalsIgnoreCase(IntegrationConstants.PhoenixResponseCodes.ACC_LOCKED.getResponseCodeValue())) {
								throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_ACCOUNT_LOCKED);
							}else {
								throw e;
							}
						}

					}

			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error("Exception occured in PhoenixBASwitchImpl.... " + ex);
				auditLogModel.setCustomField1(TransactionConstantsInterface.FAILURE);
				auditLogModel.setCustomField2(ex.getMessage());

				if (ex instanceof WorkFlowException)
					throw (WorkFlowException) ex;
				else if (ex instanceof IOException)
					throw new WorkFlowException(MessageUtil.getMessage("genericErrorMessage"));
				else if (ex instanceof RemoteAccessException)
					throw new WorkFlowException(MessageUtil.getMessage("genericErrorMessage"));
				else
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN);
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

		}

		return switchWrapper;
	}

	@Override
	public SwitchWrapper checkBalance(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException{

		if (logger.isDebugEnabled()) {
			logger.debug("Inside checkBalance method of PhoenixBASwitchImpl");
		}
		AuditLogModel auditLogModel = this.auditLogBeforeCall(switchWrapper, "");
		switchWrapper.setFromAccountNo(switchWrapper.getAccountInfoModel().getAccountNo());
		BaseWrapper baseWrapper = createOrUpdatePostedTransactionBeforeCall(switchWrapper, IntgTransactionTypeConstantsInterface.CHECK_BALANCE_PHOENIX);
		XStream xStream = new XStream();

		PhoenixIntegrationMessageVO phoenixIntegrationMessageVO = new PhoenixIntegrationMessageVO(false, switchWrapper.getSwitchSwitchModel()
				.getPaymentGatewayCode(), null);

		if (switchWrapper != null) {
			try {
				if (switchWrapper != null && switchWrapper.getWorkFlowWrapper() != null) {
					phoenixIntegrationMessageVO.setAccountNumber(switchWrapper.getAccountInfoModel().getAccountNo());
					phoenixIntegrationMessageVO.setAccountType(switchWrapper.getFromAccountType());
					phoenixIntegrationMessageVO.setAccountCurrency(switchWrapper.getCurrencyCode());

					phoenixIntegrationMessageVO.setCustomerIdentification(PhoenixConstantsInterface.ASKARI_BANK_IMD);
//					phoenixIntegrationMessageVO = prepareCNIC(phoenixIntegrationMessageVO);
//					phoenixIntegrationMessageVO.setSecureVerificationData(switchWrapper.getWorkFlowWrapper().getMPin());

					if (switchWrapper.getWorkFlowWrapper().getTransactionCodeModel() != null){
						phoenixIntegrationMessageVO.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
						phoenixIntegrationMessageVO.setMicrobankTransactionCodeId(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());
					}

				} else {
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_ACCOUNT_INFO_MISSING);
				}

				auditLogModel.setInputParam(XMLUtil.replaceElementsUsingXPath(xStream.toXML(phoenixIntegrationMessageVO),
						XPathConstants.PhoenixAuditLogInputParamLocationSteps));

				SwitchController integrationSwitch = getIntegrationSwitch(switchWrapper);

				if (integrationSwitch == null) {
					if (logger.isDebugEnabled()) {
						logger.debug("PHOENIX switch down... escalating the exception");
					}
					throw new WorkFlowException(WorkFlowErrorCodeConstants.IRIS_SERVICE_DOWN);
				}
				if (logger.isDebugEnabled()) {
					logger.debug("Hitting PHOENIX to check balance..");
				}
				
				logger.info("[PhoenixBASwitchImpl.CheckBalance] Hitting PHOENIX to check balance.. Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
				long startTime=Calendar.getInstance().getTimeInMillis();
				
				phoenixIntegrationMessageVO = (PhoenixIntegrationMessageVO) integrationSwitch.checkBalance((IntegrationMessageVO) phoenixIntegrationMessageVO);

				long timeConsumed = Calendar.getInstance().getTimeInMillis()-startTime;
				
				auditLogModel.setCustomField1(PhoenixConstantsInterface.SUCCESS);
				auditLogModel.setIntegrationPartnerIdentifier(/*phoenixIntegrationMessageVO.getSystemTraceAuditNumber()phoenixIntegrationMessageVO.getRetrievalReferenceNumber()*/"1234567890");
				switchWrapper.getWorkFlowWrapper().getTransactionModel().setBankResponseCode(phoenixIntegrationMessageVO.getResponseCode());

				if (phoenixIntegrationMessageVO != null) {
					
					if(timeConsumed == 0){//to avoid divide by zero exception..
						timeConsumed = 1;
					}
					
					logger.info("[PhoenixBASwitchImpl.CheckBalance] CheckBalance Response: " + phoenixIntegrationMessageVO.getResponseCode() + ". Processing Time: "+ (timeConsumed/1000d) + " Seconds.  Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());

					try {
						parseResponseCode(phoenixIntegrationMessageVO);
					} catch (Exception e) {
						if (phoenixIntegrationMessageVO.getResponseCode() != null
								&& phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
										IntegrationConstants.PhoenixResponseCodes.ACC_INACTIVE.getResponseCodeValue())) {
							throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_ACCOUNT_INACTIVE);
						} else if (phoenixIntegrationMessageVO.getResponseCode() != null
								&& phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
										IntegrationConstants.PhoenixResponseCodes.INCORRECT_PIN.getResponseCodeValue())) {
							throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_INCORRECT_PIN);
						} else if (phoenixIntegrationMessageVO.getResponseCode() != null
								&& phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
										IntegrationConstants.PhoenixResponseCodes.PIN_RETRY_EXHAUSTED.getResponseCodeValue())) {
							throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_PIN_RETRIES_EXHAUSTED);
						} else if (phoenixIntegrationMessageVO.getResponseCode() != null
								&& phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
										IntegrationConstants.PhoenixResponseCodes.ACC_STATUS_INVALID.getResponseCodeValue())) {
							throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_INVALID_ACCOUNT_STATUS);
						} else if (phoenixIntegrationMessageVO.getResponseCode() != null
								&& phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
										IntegrationConstants.PhoenixResponseCodes.ACC_LOCKED.getResponseCodeValue())) {
							throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_ACCOUNT_LOCKED);
						} else if (phoenixIntegrationMessageVO.getResponseCode() != null
								&& phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
										IntegrationConstants.PhoenixResponseCodes.PIN_EXPIRED.getResponseCodeValue())) {
							throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_PIN_EXPIRED);
						} else if (phoenixIntegrationMessageVO.getResponseCode() != null
								&& (phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
										IntegrationConstants.PhoenixResponseCodes.CARD_STATUS_ERROR.getResponseCodeValue())
										|| phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
												IntegrationConstants.PhoenixResponseCodes.WARM_CARD.getResponseCodeValue())
										|| phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
												IntegrationConstants.PhoenixResponseCodes.HOT_CARD.getResponseCodeValue())
										|| phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
												IntegrationConstants.PhoenixResponseCodes.BAD_CARD_STATUS.getResponseCodeValue()) || phoenixIntegrationMessageVO
										.getResponseCode().equalsIgnoreCase(IntegrationConstants.PhoenixResponseCodes.LIMIT_EXCEEDED.getResponseCodeValue()))) {
							throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN);
						} else
							throw e;
					}

					if (phoenixIntegrationMessageVO.getAvailableBalance() != null) {
						String availableBal = phoenixIntegrationMessageVO.getAvailableBalance();
						if (!StringUtils.isBlank(availableBal)) {
							switchWrapper.setBalance(Double.parseDouble(availableBal));
						} else {
							// switchWrapper.setBalance(availableBal);
						}
					} else {
						throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_INVALID_ACCOUNT);
					}
					if (phoenixIntegrationMessageVO.getResponseCode() == null) {
						logger.error("PHOENIX is down....."
								+ ExceptionProcessorUtility.prepareExceptionStackTrace(new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN)));
						throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN);
					}

					else if (!phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
							IntegrationConstants.PhoenixResponseCodes.OK.getResponseCodeValue())) {
						String msg = getMessageFromResource(phoenixIntegrationMessageVO.getResponseCode());
						logger.error(msg);
						throw new WorkFlowException(msg);
					}

				}

				// TODO Check at what point the following code will be executed
				if (!phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(IntegrationConstants.PhoenixResponseCodes.OK.getResponseCodeValue())) {
					logger.error("Payment Services Processing Failed....."
							+ ExceptionProcessorUtility.prepareExceptionStackTrace(new WorkFlowException(WorkFlowErrorCodeConstants.PAYMENT_PROCESSING_FAILED)));
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PAYMENT_PROCESSING_FAILED);
				}
			} catch (Exception ex) {
				logger.error("Exception occured in PhoenixBASwitchImpl.... " + ex);
				auditLogModel.setCustomField1(TransactionConstantsInterface.FAILURE);
				auditLogModel.setCustomField2(ex.getMessage());

				if (ex instanceof WorkFlowException)
					throw (WorkFlowException) ex;
				else if (ex instanceof IOException)
					throw new WorkFlowException(MessageUtil.getMessage("genericErrorMessage"));
				else if (ex instanceof RemoteAccessException)
					throw new WorkFlowException(MessageUtil.getMessage("genericErrorMessage"));
				else
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN);
			} finally {
				if (phoenixIntegrationMessageVO != null) {
					phoenixIntegrationMessageVO.setMessageAsEdi("");
					this.auditLogAfterCall(auditLogModel, XMLUtil.replaceElementsUsingXPath(xStream.toXML(phoenixIntegrationMessageVO),
							XPathConstants.PhoenixAuditLogInputParamLocationSteps));
					if(baseWrapper != null){
						switchWrapper.setIntegrationMessageVO(phoenixIntegrationMessageVO);
						createOrUpdatePostedTransactionAfterCall(switchWrapper, baseWrapper);
					}
				}
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Ending checkBalance method of PhoenixSwitchImpl");
			}
		}
		return switchWrapper;

	}
	
	public SwitchWrapper reverseFundTransfer(SwitchWrapper switchWrapper) throws Exception{
		return debitCreditAccount(switchWrapper);
		/*AuditLogModel auditLogModel = this.auditLogBeforeCall(switchWrapper, "");
		BaseWrapper baseWrapper = createOrUpdatePostedTransactionBeforeCall(switchWrapper, IntgTransactionTypeConstantsInterface.DEBIT_CREDIT_ACCOUNT_PHOENIX);
		XStream xStream = new XStream();

		if (switchWrapper != null) {
			PhoenixIntegrationMessageVO phoenixIntegrationMessageVO = (PhoenixIntegrationMessageVO) switchWrapper.getIntegrationMessageVO();
			
			try {
				

				SwitchController integrationSwitch = getIntegrationSwitch(switchWrapper);
				if (integrationSwitch == null) {
					logger.error("Phoenix switch down..escalating the exception");
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
				}
				logger.debug("Making call to PHOENIX for reverseFundTransfer...");
				phoenixIntegrationMessageVO = (PhoenixIntegrationMessageVO) integrationSwitch.reverseFundTransfer(switchWrapper.getIntegrationMessageVO());
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error("Exception occured in PhoenixBASwitchImpl.... " + ex);
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
			logger.debug("Ending reverseFundTransfer() method of PhoenixSwitchImpl");
		}
		return switchWrapper;*/
	}
	
	public SwitchWrapper creditCardBillPayment(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {

		logger.debug("Inside creditCardBillPayment() method of PhoenixSwitchImpl");
		
		AuditLogModel auditLogModel = this.auditLogBeforeCall(switchWrapper, "");
		BaseWrapper baseWrapper = createOrUpdatePostedTransactionBeforeCall(switchWrapper, IntgTransactionTypeConstantsInterface.CREDIT_CARD_BILL_PAYMENT_PHOENIX);
		XStream xStream = new XStream();

		if (switchWrapper != null) {
			PhoenixIntegrationMessageVO phoenixIntegrationMessageVO = new PhoenixIntegrationMessageVO(false, switchWrapper.getSwitchSwitchModel().getPaymentGatewayCode(), null);

			try {
				phoenixIntegrationMessageVO.setCardNumber(((CreditCardPaymentVO)switchWrapper.getWorkFlowWrapper().getProductVO()).getCardNumber());
				phoenixIntegrationMessageVO.setCustomerIdentification(PhoenixConstantsInterface.ASKARI_BANK_IMD);
				phoenixIntegrationMessageVO.setTransactionAmount(switchWrapper.getTransactionAmount().toString());
				phoenixIntegrationMessageVO.setTransactionCurrency(switchWrapper.getCurrencyCode());

				// FROM ACCOUNT INFO
				phoenixIntegrationMessageVO.setFromAccountNumber(switchWrapper.getFromAccountNo());
				phoenixIntegrationMessageVO.setFromAccountType(switchWrapper.getFromAccountType());
				phoenixIntegrationMessageVO.setFromAccountCurrency(switchWrapper.getFromCurrencyCode());

				if (switchWrapper.getWorkFlowWrapper() != null && switchWrapper.getWorkFlowWrapper().getTransactionCodeModel() != null){
					phoenixIntegrationMessageVO.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
					phoenixIntegrationMessageVO.setMicrobankTransactionCodeId(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());
				}


// -- real time credit card payment without queue

				auditLogModel.setInputParam(XMLUtil.replaceElementsUsingXPath(xStream.toXML(phoenixIntegrationMessageVO), XPathConstants.PhoenixAuditLogInputParamLocationSteps));
				SwitchController integrationSwitch = getIntegrationSwitch(switchWrapper);

				if (integrationSwitch == null) {
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN);
				}

				logger.info("[PhoenixBASwitchImpl.creditCardBillPayment] Hitting PHOENIX. Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
				
				long startTime=Calendar.getInstance().getTimeInMillis();
				
				phoenixIntegrationMessageVO = (PhoenixIntegrationMessageVO) integrationSwitch.creditCardtransaction(phoenixIntegrationMessageVO);
				switchWrapper.setIntegrationMessageVO(phoenixIntegrationMessageVO);
				
				long timeConsumed = Calendar.getInstance().getTimeInMillis()-startTime;
				
				auditLogModel.setCustomField1(PhoenixConstantsInterface.SUCCESS);
				auditLogModel.setIntegrationPartnerIdentifier(phoenixIntegrationMessageVO.getRetrievalReferenceNumber());

				if (phoenixIntegrationMessageVO != null) {
					String responseCode = phoenixIntegrationMessageVO.getResponseCode();
					logger.info("[PhoenixBASwitchImpl.creditCardBillPayment] Response code from PHOENIX: " + responseCode + ". Processing Time: "+ (timeConsumed/1000d) + " Seconds. Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
					parseResponseCode(phoenixIntegrationMessageVO);
				}
// -- real time end
				

/* -- credit card payment via QUEUE

				phoenixIntegrationMessageVO.setServiceUrl(switchWrapper.getSwitchSwitchModel().getUrl());
				
				auditLogModel.setInputParam(XMLUtil.replaceElementsUsingXPath(xStream.toXML(phoenixIntegrationMessageVO), XPathConstants.PhoenixAuditLogInputParamLocationSteps));
				auditLogModel.setActionLogId(ThreadLocalActionLog.getActionLogId());
					
				authorizeBillPayment(phoenixIntegrationMessageVO);
				
				auditLogModel.setCustomField1(PhoenixConstantsInterface.SUCCESS);
				auditLogModel.setIntegrationPartnerIdentifier(phoenixIntegrationMessageVO.getSystemTraceAuditNumber());

				if (phoenixIntegrationMessageVO != null) {
					phoenixIntegrationMessageVO.setResponseCode("00");
					switchWrapper.setIntegrationMessageVO(phoenixIntegrationMessageVO);
					parseResponseCode(phoenixIntegrationMessageVO);
				}
*/				
				
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error("Exception occured in PhoenixBASwitchImpl.creditCardBillPayment: " + ex);
				auditLogModel.setCustomField1(TransactionConstantsInterface.FAILURE);
				auditLogModel.setCustomField2(ex.getMessage());

				if (ex instanceof WorkFlowException)
					throw (WorkFlowException) ex;
				else if (ex instanceof IOException)
					throw new WorkFlowException(MessageUtil.getMessage("genericErrorMessage"));
				else if (ex instanceof RemoteAccessException)
					throw new WorkFlowException(MessageUtil.getMessage("genericErrorMessage"));
				else
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN);
			} finally {
				if (phoenixIntegrationMessageVO != null) {
					phoenixIntegrationMessageVO.setMessageAsEdi("");
					this.auditLogAfterCall(auditLogModel, XMLUtil.replaceElementsUsingXPath(xStream.toXML(phoenixIntegrationMessageVO), XPathConstants.PhoenixAuditLogInputParamLocationSteps));
					if(baseWrapper != null){
						createOrUpdatePostedTransactionAfterCall(switchWrapper, baseWrapper);
					}
				}
			}

		}

		return switchWrapper;
	
	}
	

}
