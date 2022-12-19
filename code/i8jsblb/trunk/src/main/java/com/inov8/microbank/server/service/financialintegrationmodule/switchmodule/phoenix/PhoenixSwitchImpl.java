package com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix;

/**
 * Project Name: 			Microbank	
 * @author 					Jawwad Farooq
 * Creation Date: 			February 2008  			
 * Description:				
 */

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.XMLUtil;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AuditLogModel;
import com.inov8.microbank.common.model.OperatorModel;
import com.inov8.microbank.common.model.SwitchUtilityMappingModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.HttpInvokerUtil;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.SwitchConstants;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.common.util.XPathConstants;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.server.facade.SwitchUtilityMappingFacade;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchProcessor;
import com.inov8.microbank.server.service.switchmodule.IntegrationMessageVO;
import com.inov8.microbank.server.service.switchmodule.iris.IntegrationConstants;
import com.inov8.microbank.server.service.switchmodule.iris.SwitchController;
import com.inov8.microbank.server.service.switchmodule.iris.model.PhoenixIntegrationMessageVO;
import com.thoughtworks.xstream.XStream;

public class PhoenixSwitchImpl extends SwitchProcessor {

	protected static Log logger = LogFactory.getLog(PhoenixSwitchImpl.class);
	protected ApplicationContext ctx;
	private MessageSource messageSource;

	public PhoenixSwitchImpl(ApplicationContext ctx) {
		this.ctx = ctx;
	}

	@Override
	public SwitchWrapper changeDeliveryChannel(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
		if (logger.isDebugEnabled()) {
			logger.debug("Inside changeDeliveryChannel method of PhoenixSwitchImpl");
		}
		AuditLogModel auditLogModel = this.auditLogBeforeCall(switchWrapper, "");
		XStream xStream = new XStream();

		PhoenixIntegrationMessageVO phoenixIntegrationMessageVO = new PhoenixIntegrationMessageVO(false, switchWrapper.getSwitchSwitchModel()
				.getPaymentGatewayCode(), null);

		if (switchWrapper != null) {
			try {
				if (switchWrapper != null) {
					phoenixIntegrationMessageVO.setCustomerIdentification((String) switchWrapper.getObject(CommandFieldConstants.KEY_CNIC));
					phoenixIntegrationMessageVO = prepareCNIC(phoenixIntegrationMessageVO);
					phoenixIntegrationMessageVO.setPAN(phoenixIntegrationMessageVO.getCustomerIdentification());

					phoenixIntegrationMessageVO.setReqChannelStatus((String) switchWrapper.getObject(CommandFieldConstants.KEY_DELIVERY_CHNL_STATUS));
				} else {
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_ACCOUNT_INFO_MISSING);
				}

				auditLogModel.setInputParam(XMLUtil.replaceElementsUsingXPath(xStream.toXML(phoenixIntegrationMessageVO),
						XPathConstants.PhoenixAuditLogInputParamLocationSteps));

				SwitchController integrationSwitch = getIntegrationSwitch(switchWrapper);

				if (integrationSwitch == null) {
					if (logger.isDebugEnabled()) {
						logger.debug("Phoenix switch down..escalating the exception");
					}
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
				}
				if (logger.isDebugEnabled()) {
					logger.debug("Hitting PHOENIX to changeDeliveryChannel..");
				}

				phoenixIntegrationMessageVO = (PhoenixIntegrationMessageVO) integrationSwitch
						.activateAccount((IntegrationMessageVO) phoenixIntegrationMessageVO);

				auditLogModel.setCustomField1(PhoenixConstantsInterface.SUCCESS);
				auditLogModel.setIntegrationPartnerIdentifier(phoenixIntegrationMessageVO.getSystemTraceAuditNumber());

				if (phoenixIntegrationMessageVO != null) {
					switchWrapper.setIntegrationMessageVO(phoenixIntegrationMessageVO);
					parseResponseCode(phoenixIntegrationMessageVO);
				} else {
					logger.error("PHOENIX is down....."
							+ ExceptionProcessorUtility.prepareExceptionStackTrace(new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG)));
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
				}

				// if
				// (!phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
				// IntegrationConstants.IRISResponseCodes.OK.getResponseCodeValue()))
				// {
				// logger.error("Payment Services Processing Failed....."
				// + ExceptionProcessorUtility.prepareExceptionStackTrace(new
				// WorkFlowException(
				// WorkFlowErrorCodeConstants.PAYMENT_PROCESSING_FAILED)));
				// throw new
				// WorkFlowException(WorkFlowErrorCodeConstants.PAYMENT_PROCESSING_FAILED);
				// }
			} catch (Exception ex) {
				logger.error("Exception occured in microbank PhoenixSwitchImpl.... " + ex);
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
				}
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Ending changeDeliveryChannel method of PhoenixSwitchImpl");
			}
		}
		return switchWrapper;
	}

	public SwitchWrapper titleFetch(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
		if (logger.isDebugEnabled()) {
			logger.debug("Inside titleFetch method of PhoenixSwitchImpl");
		}
		// AuditLogModel auditLogModel = this.auditLogBeforeCall(switchWrapper,
		// "");
		XStream xStream = new XStream();

		PhoenixIntegrationMessageVO phoenixIntegrationMessageVO = new PhoenixIntegrationMessageVO(false, switchWrapper.getSwitchSwitchModel()
				.getPaymentGatewayCode(), null);

		if (switchWrapper != null) {
			try {
				if (switchWrapper != null) {
					phoenixIntegrationMessageVO.setCustomerIdentification((String) switchWrapper.getObject(CommandFieldConstants.KEY_CNIC));
					phoenixIntegrationMessageVO = prepareCNIC(phoenixIntegrationMessageVO);
					phoenixIntegrationMessageVO.setAccountNumber(switchWrapper.getCustomerAccount().getNumber());
					phoenixIntegrationMessageVO.setAccountType(switchWrapper.getCustomerAccount().getType());
					phoenixIntegrationMessageVO.setAccountCurrency(switchWrapper.getCustomerAccount().getCurrency());
				} else {
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_ACCOUNT_INFO_MISSING);
				}

				// auditLogModel.setInputParam(XMLUtil.replaceElementsUsingXPath(xStream.toXML(phoenixIntegrationMessageVO),
				// XPathConstants.PhoenixAuditLogInputParamLocationSteps));

				// auditLogModel.setActionLogId(
				// ThreadLocalActionLog.getActionLogId() );

				SwitchController integrationSwitch = getIntegrationSwitch(switchWrapper);

				if (integrationSwitch == null) {
					if (logger.isDebugEnabled()) {
						logger.debug("Phoenix switch down..escalating the exception");
					}
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
				}
				if (logger.isDebugEnabled()) {
					logger.debug("Hitting PHOENIX to titleFetch..");
				}

				phoenixIntegrationMessageVO = (PhoenixIntegrationMessageVO) integrationSwitch.verifyAccount((IntegrationMessageVO) phoenixIntegrationMessageVO);

				// auditLogModel.setCustomField1(PhoenixConstants.SUCCESS);
				// auditLogModel.setIntegrationPartnerIdentifier(phoenixIntegrationMessageVO.getSystemTraceAuditNumber());

				if (phoenixIntegrationMessageVO != null) {
					switchWrapper.setIntegrationMessageVO(phoenixIntegrationMessageVO);
					parseResponseCode(phoenixIntegrationMessageVO);

					if (phoenixIntegrationMessageVO.getTitleOfTheAccount() != null) {
						switchWrapper.getCustomerAccount().setTitleOfTheAccount(phoenixIntegrationMessageVO.getTitleOfTheAccount());

					} else {
						throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_INVALID_ACCOUNT);
					}
				} else {
					logger.error("PHOENIX is down....."
							+ ExceptionProcessorUtility.prepareExceptionStackTrace(new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG)));
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
				}

				// if
				// (!phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
				// IntegrationConstants.IRISResponseCodes.OK.getResponseCodeValue()))
				// {
				// logger.error("Payment Services Processing Failed....."
				// + ExceptionProcessorUtility.prepareExceptionStackTrace(new
				// WorkFlowException(
				// WorkFlowErrorCodeConstants.PAYMENT_PROCESSING_FAILED)));
				// throw new
				// WorkFlowException(WorkFlowErrorCodeConstants.PAYMENT_PROCESSING_FAILED);
				// }
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error("Exception occured in microbank PhoenixSwitchImpl.... " + ex);
				// auditLogModel.setCustomField1(PhoenixConstants.FAILURE);
				// auditLogModel.setCustomField2(ex.getMessage());

				if (ex instanceof WorkFlowException)
					throw (WorkFlowException) ex;
				else if (ex instanceof IOException)
					throw new WorkFlowException(MessageUtil.getMessage("genericErrorMessage"));
				else if (ex instanceof RemoteAccessException)
					throw new WorkFlowException(MessageUtil.getMessage("genericErrorMessage"));
				else
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
			}
			// finally
			// {
			// if (phoenixIntegrationMessageVO!= null)
			// {
			// phoenixIntegrationMessageVO.setMessageAsEdi("");
			// this.auditLogAfterCall(auditLogModel,
			// XMLUtil.replaceElementsUsingXPath(xStream
			// .toXML(phoenixIntegrationMessageVO),
			// XPathConstants.PhoenixAuditLogInputParamLocationSteps));
			//
			// }
			// }
			if (logger.isDebugEnabled()) {
				logger.debug("Ending titleFetch method of PhoenixSwitchImpl");
			}
		}
		return switchWrapper;
	}

	@Override
	public SwitchWrapper customerProfile(SwitchWrapper switchWrapper) throws FrameworkCheckedException {
		if (logger.isDebugEnabled()) {
			logger.debug("Inside customerProfile method of PhoenixSwitchImpl");
		}
		// AuditLogModel auditLogModel = this.auditLogBeforeCall(switchWrapper,
		// "");
		XStream xStream = new XStream();

		PhoenixIntegrationMessageVO phoenixIntegrationMessageVO = new PhoenixIntegrationMessageVO(false, switchWrapper.getSwitchSwitchModel()
				.getPaymentGatewayCode(), null);

		if (switchWrapper != null) {
			try {
				if (switchWrapper != null) {
					phoenixIntegrationMessageVO.setCustomerIdentification((String) switchWrapper.getObject(CommandFieldConstants.KEY_CNIC));
					phoenixIntegrationMessageVO = prepareCNIC(phoenixIntegrationMessageVO);
					// if( switchWrapper.getWorkFlowWrapper() != null )
					// phoenixIntegrationMessageVO.setSecureVerificationData(switchWrapper.getWorkFlowWrapper().getMPin());
				} else {
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_ACCOUNT_INFO_MISSING);
				}

				// auditLogModel.setInputParam(XMLUtil.replaceElementsUsingXPath(xStream.toXML(phoenixIntegrationMessageVO),
				// XPathConstants.PhoenixAuditLogInputParamLocationSteps));

				SwitchController integrationSwitch = getIntegrationSwitch(switchWrapper);

				if (integrationSwitch == null) {
					if (logger.isDebugEnabled()) {
						logger.debug("Phoenix switch down..escalating the exception");
					}
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
				}
				if (logger.isDebugEnabled()) {
					logger.debug("Hitting PHOENIX to Extended Customer Profile Inquiry..");
				}

				phoenixIntegrationMessageVO = (PhoenixIntegrationMessageVO) integrationSwitch
						.extendedCustomerProfileInquiry((IntegrationMessageVO) phoenixIntegrationMessageVO);

				// auditLogModel.setCustomField1(PhoenixConstants.SUCCESS);
				// auditLogModel.setIntegrationPartnerIdentifier(phoenixIntegrationMessageVO.getSystemTraceAuditNumber());

				if (phoenixIntegrationMessageVO != null) {

					switchWrapper.setIntegrationMessageVO(phoenixIntegrationMessageVO);
					// phoenixIntegrationMessageVO.setResponseCode("00");
					// phoenixIntegrationMessageVO.setMobileChannelStatus("00");

					try {
						parseResponseCode(phoenixIntegrationMessageVO);
					} catch (Exception e) {
						if (phoenixIntegrationMessageVO.getResponseCode() != null
								&& phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
										IntegrationConstants.PhoenixResponseCodes.ACC_INACTIVE.getResponseCodeValue())) {
							throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_ACCOUNT_INACTIVE);
						} else if (phoenixIntegrationMessageVO.getResponseCode() != null
								&& phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
										IntegrationConstants.PhoenixResponseCodes.ACC_STATUS_INVALID.getResponseCodeValue())) {
							throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_INVALID_ACCOUNT_STATUS);
						} else if (phoenixIntegrationMessageVO.getResponseCode() != null
								&& phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
										IntegrationConstants.PhoenixResponseCodes.ACC_LOCKED.getResponseCodeValue())) {
							throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_ACCOUNT_LOCKED);
						}
						// else if( switchWrapper.getPaymentModeId().longValue()
						// == PaymentModeConstantsInterface.CREDIT_CARD )
						{
							if (phoenixIntegrationMessageVO.getResponseCode() != null
									&& phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
											IntegrationConstants.PhoenixResponseCodes.CARD_STATUS_ERROR.getResponseCodeValue())) {
								throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_CARD_STATUS_ERROR);
							} else if (phoenixIntegrationMessageVO.getResponseCode() != null
									&& phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
											IntegrationConstants.PhoenixResponseCodes.WARM_CARD.getResponseCodeValue())) {
								throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_WARM_CARD);
							} else if (phoenixIntegrationMessageVO.getResponseCode() != null
									&& phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
											IntegrationConstants.PhoenixResponseCodes.HOT_CARD.getResponseCodeValue())) {
								throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_HOT_CARD);
							} else if (phoenixIntegrationMessageVO.getResponseCode() != null
									&& phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
											IntegrationConstants.PhoenixResponseCodes.BAD_CARD_STATUS.getResponseCodeValue())) {
								throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_BAD_CARD_STATUS);
							}
						}

						throw e;
					}
				} else {
					logger.error("PHOENIX is down....."
							+ ExceptionProcessorUtility.prepareExceptionStackTrace(new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG)));
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
				}

				// if
				// (!phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
				// IntegrationConstants.IRISResponseCodes.OK.getResponseCodeValue()))
				// {
				// logger.error("Payment Services Processing Failed....."
				// + ExceptionProcessorUtility.prepareExceptionStackTrace(new
				// WorkFlowException(
				// WorkFlowErrorCodeConstants.PAYMENT_PROCESSING_FAILED)));
				// throw new
				// WorkFlowException(WorkFlowErrorCodeConstants.PAYMENT_PROCESSING_FAILED);
				// }
			} catch (Exception ex) {
				logger.error("Exception occured in microbank PhoenixSwitchImpl.... " + ex);
				// auditLogModel.setCustomField1(PhoenixConstants.FAILURE);
				// auditLogModel.setCustomField2(ex.getMessage());
				ex.printStackTrace();
				if (ex instanceof WorkFlowException)
					throw (WorkFlowException) ex;
				else if (ex instanceof IOException)
					throw new WorkFlowException(MessageUtil.getMessage("genericErrorMessage"));
				else if (ex instanceof RemoteAccessException)
					throw new WorkFlowException(MessageUtil.getMessage("genericErrorMessage"));
				else
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
			}
			// finally
			// {
			// if (phoenixIntegrationMessageVO!= null)
			// {
			// phoenixIntegrationMessageVO.setMessageAsEdi("");
			// this.auditLogAfterCall(auditLogModel,
			// XMLUtil.replaceElementsUsingXPath(xStream
			// .toXML(phoenixIntegrationMessageVO),
			// XPathConstants.PhoenixAuditLogInputParamLocationSteps));
			//
			// }
			// }
			if (logger.isDebugEnabled()) {
				logger.debug("Ending customerProfile method of PhoenixSwitchImpl");
			}
		}
		return switchWrapper;

	}

	@Override
	public SwitchWrapper miniStatement(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
		// TODO Implement the logic - JFS
		if (logger.isDebugEnabled()) {
			logger.debug("Inside miniStatement method of PhoenixSwitchImpl");
		}
		AuditLogModel auditLogModel = this.auditLogBeforeCall(switchWrapper, "");
		XStream xStream = new XStream();

		PhoenixIntegrationMessageVO phoenixIntegrationMessageVO = new PhoenixIntegrationMessageVO(false, switchWrapper.getSwitchSwitchModel()
				.getPaymentGatewayCode(), null);

		if (switchWrapper != null) {
			try {
				if (switchWrapper != null && switchWrapper.getWorkFlowWrapper() != null) {
					if (switchWrapper.getAccountInfoModel() != null) {
						// TODO JFS- Change and implement the following

//						if (switchWrapper.getPaymentModeId().longValue() == PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT) {
//							phoenixIntegrationMessageVO.setAccountNumber(switchWrapper.getAccountInfoModel().getAccountNo());
//							phoenixIntegrationMessageVO.setAccountType(switchWrapper.getAccountInfoModel().getAccountTypeIdAccountTypeModel()
//									.getAccountTypeCode());
//							phoenixIntegrationMessageVO.setAccountCurrency(switchWrapper.getAccountInfoModel().getCurrencyCodeIdCurrencyCodeModel()
//									.getCurrencyCode());
//
//							phoenixIntegrationMessageVO = prepareAccountDetailForBA(phoenixIntegrationMessageVO);
//						} else if (switchWrapper.getPaymentModeId().longValue() == PaymentModeConstantsInterface.CREDIT_CARD) {
//							phoenixIntegrationMessageVO.setCardNumber(switchWrapper.getAccountInfoModel().getCardNo());
//							phoenixIntegrationMessageVO.setCardExpiry(switchWrapper.getAccountInfoModel().getCardExpiryDate());
//							phoenixIntegrationMessageVO = prepareAccountDetailForCC(phoenixIntegrationMessageVO);
//						}

						phoenixIntegrationMessageVO.setAccountNumber(switchWrapper.getAccountInfoModel().getAccountNo());
						phoenixIntegrationMessageVO.setAccountType(switchWrapper.getFromAccountType());
						phoenixIntegrationMessageVO.setAccountCurrency(switchWrapper.getCurrencyCode());
						phoenixIntegrationMessageVO.setCustomerIdentification(PhoenixConstantsInterface.ASKARI_BANK_IMD);

						
//						phoenixIntegrationMessageVO.setAccountNumber(switchWrapper.getAccountInfoModel().getAccountNo());
//						phoenixIntegrationMessageVO.setAccountType(switchWrapper.getAccountInfoModel().getAccountTypeIdAccountTypeModel().getAccountTypeCode());
//						phoenixIntegrationMessageVO.setAccountCurrency(switchWrapper.getAccountInfoModel().getCurrencyCodeIdCurrencyCodeModel().getCurrencyCode());
//						phoenixIntegrationMessageVO = prepareAccountDetailForBA(phoenixIntegrationMessageVO);
						
//						phoenixIntegrationMessageVO.setCustomerIdentification((String) switchWrapper.getObject(CommandFieldConstants.KEY_CNIC));
//						phoenixIntegrationMessageVO = prepareCNIC(phoenixIntegrationMessageVO);
//						phoenixIntegrationMessageVO.setSecureVerificationData(switchWrapper.getWorkFlowWrapper().getMPin());
					} else {
						throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_ACCOUNT_INFO_MISSING);
					}
				} else {
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_ACCOUNT_INFO_MISSING);
				}

				auditLogModel.setInputParam(XMLUtil.replaceElementsUsingXPath(xStream.toXML(phoenixIntegrationMessageVO),
						XPathConstants.PhoenixAuditLogInputParamLocationSteps));

				SwitchController integrationSwitch = getIntegrationSwitch(switchWrapper);

				if (integrationSwitch == null) {
					if (logger.isDebugEnabled()) {
						logger.debug("Phoenix switch down..escalating the exception");
					}
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
				}
				if (logger.isDebugEnabled()) {
					logger.debug("Hitting Phoenix to check balance..");
				}

				phoenixIntegrationMessageVO = (PhoenixIntegrationMessageVO) integrationSwitch
						.getMiniStatement((IntegrationMessageVO) phoenixIntegrationMessageVO);

				auditLogModel.setCustomField1(PhoenixConstantsInterface.SUCCESS);
				auditLogModel.setIntegrationPartnerIdentifier(phoenixIntegrationMessageVO.getSystemTraceAuditNumber());
				switchWrapper.getWorkFlowWrapper().getTransactionModel().setBankResponseCode(phoenixIntegrationMessageVO.getResponseCode());

				if (phoenixIntegrationMessageVO != null) {
					switchWrapper.setIntegrationMessageVO(phoenixIntegrationMessageVO);
					// phoenixIntegrationMessageVO.setResponseCode("00");
					// phoenixIntegrationMessageVO.setAdditionalData("31MAR PURCHASE               80.00      31MAR PURCHASE               80.00      31MAR PURCHASE              200.00      31MAR PURCHASE               20.00      31MAR CASH ADVANCE           40.00      31MAR CASH ADVANCE           40.00      31MAR CASH ADVANCE          200.00      31MAR CASH ADVANCE          200.00      31MAR CASH ADVANCE           40.00                                              ");

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
								&& phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
										IntegrationConstants.PhoenixResponseCodes.LIMIT_EXCEEDED.getResponseCodeValue())) {
							throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN);
						} else if (switchWrapper.getPaymentModeId().longValue() == PaymentModeConstantsInterface.CREDIT_CARD) {
							if (phoenixIntegrationMessageVO.getResponseCode() != null
									&& phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
											IntegrationConstants.PhoenixResponseCodes.CARD_STATUS_ERROR.getResponseCodeValue())) {
								throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_CARD_STATUS_ERROR);
							} else if (phoenixIntegrationMessageVO.getResponseCode() != null
									&& phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
											IntegrationConstants.PhoenixResponseCodes.WARM_CARD.getResponseCodeValue())) {
								throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_WARM_CARD);
							} else if (phoenixIntegrationMessageVO.getResponseCode() != null
									&& phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
											IntegrationConstants.PhoenixResponseCodes.HOT_CARD.getResponseCodeValue())) {
								throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_HOT_CARD);
							} else if (phoenixIntegrationMessageVO.getResponseCode() != null
									&& phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
											IntegrationConstants.PhoenixResponseCodes.BAD_CARD_STATUS.getResponseCodeValue())) {
								throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_BAD_CARD_STATUS);
							} else
								throw e;
						} else if (phoenixIntegrationMessageVO.getResponseCode() != null
								&& switchWrapper.getPaymentModeId().longValue() == PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT
								&& (phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
										IntegrationConstants.PhoenixResponseCodes.CARD_STATUS_ERROR.getResponseCodeValue())
										|| phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
												IntegrationConstants.PhoenixResponseCodes.WARM_CARD.getResponseCodeValue())
										|| phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
												IntegrationConstants.PhoenixResponseCodes.HOT_CARD.getResponseCodeValue()) || phoenixIntegrationMessageVO
										.getResponseCode().equalsIgnoreCase(IntegrationConstants.PhoenixResponseCodes.BAD_CARD_STATUS.getResponseCodeValue()))) {
							throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN);
						} else
							throw e;
					}

					switchWrapper.putObject(CommandFieldConstants.KEY_STATEMENTS, new ArrayList(phoenixIntegrationMessageVO.getTransactionsMap()));
				} else {
					logger.error("Phoenix is down....."
							+ ExceptionProcessorUtility.prepareExceptionStackTrace(new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG)));
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
				}

				// if
				// (!phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
				// IntegrationConstants.IRISResponseCodes.OK.getResponseCodeValue()))
				// {
				// logger.error("Payment Services Processing Failed....."
				// + ExceptionProcessorUtility.prepareExceptionStackTrace(new
				// WorkFlowException(
				// WorkFlowErrorCodeConstants.PAYMENT_PROCESSING_FAILED)));
				// throw new
				// WorkFlowException(WorkFlowErrorCodeConstants.PAYMENT_PROCESSING_FAILED);
				// }
			} catch (Exception ex) {
				logger.error("Exception occured in microbank PhoenixSwitchImpl.... " + ex);
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

				}
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Ending MiniStatement method of PhoenixSwitchImpl");
			}
		}
		return switchWrapper;
	}

	public SwitchWrapper checkBalance(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException{
		// if (logger.isDebugEnabled())
		// {
		// logger.debug("Inside generatePIN method of PhoenixSwitchImpl");
		// }
		// AuditLogModel auditLogModel = this.auditLogBeforeCall(switchWrapper,
		// "");
		// XStream xStream = new XStream();
		//
		// PhoenixIntegrationMessageVO phoenixIntegrationMessageVO = new
		// PhoenixIntegrationMessageVO(false, switchWrapper
		// .getSwitchSwitchModel().getPaymentGatewayCode(), null);
		//
		// if (switchWrapper != null)
		// {
		// try
		// {
		// if (switchWrapper != null )
		// {
		// phoenixIntegrationMessageVO.setCustomerIdentification((String)
		// switchWrapper
		// .getObject(CommandFieldConstants.KEY_CNIC));
		// phoenixIntegrationMessageVO =
		// prepareCNIC(phoenixIntegrationMessageVO);
		// phoenixIntegrationMessageVO
		// .setNewPIN((String)switchWrapper.getObject(CommandFieldConstants.KEY_NEW_PIN));
		// }
		// else
		// {
		// throw new
		// WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_ACCOUNT_INFO_MISSING);
		// }
		//
		// auditLogModel.setInputParam(XMLUtil.replaceElementsUsingXPath(xStream.toXML(phoenixIntegrationMessageVO),
		// XPathConstants.PhoenixAuditLogInputParamLocationSteps));
		//
		// SwitchController integrationSwitch =
		// getIntegrationSwitch(switchWrapper);
		//
		// if (integrationSwitch == null)
		// {
		// if (logger.isDebugEnabled())
		// {
		// logger.debug("Phoenix switch down..escalating the exception");
		// }
		// throw new
		// WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN);
		// }
		// if (logger.isDebugEnabled())
		// {
		// logger.debug("Hitting Phoenix to check balance..");
		// }
		//
		// phoenixIntegrationMessageVO = (PhoenixIntegrationMessageVO)
		// integrationSwitch
		// .changeMPIN((IntegrationMessageVO) phoenixIntegrationMessageVO);
		//
		// auditLogModel.setCustomField1(PhoenixConstants.SUCCESS);
		// auditLogModel.setIntegrationPartnerIdentifier(phoenixIntegrationMessageVO.getSystemTraceAuditNumber());
		// //
		// switchWrapper.getWorkFlowWrapper().getTransactionModel().setBankResponseCode(
		// // phoenixIntegrationMessageVO.getResponseCode());
		//
		// if (phoenixIntegrationMessageVO != null)
		// {
		// parseResponseCode(phoenixIntegrationMessageVO);
		// switchWrapper.setIntegrationMessageVO(phoenixIntegrationMessageVO) ;
		// }
		// else
		// {
		// logger.error("Phoenix is down....."
		// + ExceptionProcessorUtility.prepareExceptionStackTrace(new
		// WorkFlowException(
		// WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN)));
		// throw new
		// WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN);
		// }
		//
		// // if
		// (!phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
		// // IntegrationConstants.IRISResponseCodes.OK.getResponseCodeValue()))
		// // {
		// // logger.error("Payment Services Processing Failed....."
		// // + ExceptionProcessorUtility.prepareExceptionStackTrace(new
		// WorkFlowException(
		// // WorkFlowErrorCodeConstants.PAYMENT_PROCESSING_FAILED)));
		// // throw new
		// WorkFlowException(WorkFlowErrorCodeConstants.PAYMENT_PROCESSING_FAILED);
		// // }
		// }
		// catch (Exception ex)
		// {
		// logger.error("Exception occured in microbank PhoenixSwitchImpl.... " +
		// ex);
		// auditLogModel.setCustomField1(PhoenixConstants.FAILURE);
		// auditLogModel.setCustomField2(ex.getMessage());
		//
		// if (ex instanceof WorkFlowException)
		// throw (WorkFlowException) ex;
		// else if (ex instanceof IOException)
		// throw new
		// WorkFlowException(MessageUtil.getMessage("genericErrorMessage"));
		// else if (ex instanceof RemoteAccessException)
		// throw new
		// WorkFlowException(MessageUtil.getMessage("genericErrorMessage"));
		// else
		// throw new
		// WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN);
		// }
		// finally
		// {
		// if (phoenixIntegrationMessageVO!= null)
		// {
		// phoenixIntegrationMessageVO.setMessageAsEdi("");
		// this.auditLogAfterCall(auditLogModel,
		// XMLUtil.replaceElementsUsingXPath(xStream
		// .toXML(phoenixIntegrationMessageVO),
		// XPathConstants.PhoenixAuditLogInputParamLocationSteps));
		//
		// }
		// }
		// if (logger.isDebugEnabled())
		// {
		// logger.debug("Ending checkBalance method of PhoenixSwitchImpl");
		// }
		// }
		return switchWrapper;
	}

	@Override
	public SwitchWrapper changePIN(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {

		PhoenixIntegrationMessageVO phoenixIntegrationMessageVO = null;

		try {
			switchWrapper = this.customerProfile(switchWrapper);
			phoenixIntegrationMessageVO = (PhoenixIntegrationMessageVO) switchWrapper.getIntegrationMessageVO();
		} catch (Exception e) {
			phoenixIntegrationMessageVO = (PhoenixIntegrationMessageVO) switchWrapper.getIntegrationMessageVO();

			/** @todo change it **/
			if (phoenixIntegrationMessageVO != null) {
				if (phoenixIntegrationMessageVO.getResponseCode() != null
						&& phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
								IntegrationConstants.PhoenixResponseCodes.ACC_INACTIVE.getResponseCodeValue())) {
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_ACCOUNT_INACTIVE);
				} else if (phoenixIntegrationMessageVO.getResponseCode() != null
						&& phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
								IntegrationConstants.PhoenixResponseCodes.ACC_STATUS_INVALID.getResponseCodeValue())) {
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_INVALID_ACCOUNT_STATUS);
				} else if (phoenixIntegrationMessageVO.getResponseCode() != null
						&& phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
								IntegrationConstants.PhoenixResponseCodes.ACC_LOCKED.getResponseCodeValue())) {
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_ACCOUNT_LOCKED);
				}
				// else if( switchWrapper.getPaymentModeId().longValue() ==
				// PaymentModeConstantsInterface.CREDIT_CARD )
				// {
				else if (phoenixIntegrationMessageVO.getResponseCode() != null
						&& phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
								IntegrationConstants.PhoenixResponseCodes.CARD_STATUS_ERROR.getResponseCodeValue())) {
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_CARD_STATUS_ERROR);
				} else if (phoenixIntegrationMessageVO.getResponseCode() != null
						&& phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
								IntegrationConstants.PhoenixResponseCodes.WARM_CARD.getResponseCodeValue())) {
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_WARM_CARD);
				} else if (phoenixIntegrationMessageVO.getResponseCode() != null
						&& phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
								IntegrationConstants.PhoenixResponseCodes.HOT_CARD.getResponseCodeValue())) {
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_HOT_CARD);
				} else if (phoenixIntegrationMessageVO.getResponseCode() != null
						&& phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
								IntegrationConstants.PhoenixResponseCodes.BAD_CARD_STATUS.getResponseCodeValue())) {
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_BAD_CARD_STATUS);
				} else {
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
				}
				// }
			} else {
				throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
			}

			// if( phoenixIntegrationMessageVO != null &&
			// phoenixIntegrationMessageVO.getResponseCode() != null &&
			// !phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
			// IntegrationConstants.PhoenixResponseCodes.OK.getResponseCodeValue()
			// ) )
			// {
			// throw new
			// WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
			// }
			// throw new WorkFlowException(e.getMessage(),e);
		}

		// ************************************************* Checking mobile
		// channel delivery status
		if (phoenixIntegrationMessageVO != null
				&& !phoenixIntegrationMessageVO.getMobileChannelStatus().equalsIgnoreCase(
						IntegrationConstants.PhoenixDeliveryChannelStatusCodes.OK.getDeliveryChannelStatusCodeValue())) {
			throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_CUSTOMER_PROFILE_NOT_FOUND_MSG);
		}

		/**************************************************/
		if (logger.isDebugEnabled()) {
			logger.debug("Inside changePIN method of PhoenixSwitchImpl");
		}
		AuditLogModel auditLogModel = this.auditLogBeforeCall(switchWrapper, "");
		XStream xStream = new XStream();
		phoenixIntegrationMessageVO = new PhoenixIntegrationMessageVO(false, switchWrapper.getSwitchSwitchModel().getPaymentGatewayCode(), null);

		if (switchWrapper != null) {
			try {
				OperatorModel operatorModel = (OperatorModel) switchWrapper.getObject(CommandFieldConstants.KEY_OPERATOR_MODEL);
				String pin = this.decryptPin((String) switchWrapper.getObject(CommandFieldConstants.KEY_PIN), operatorModel);
				String newPin = this.decryptPin((String) switchWrapper.getObject(CommandFieldConstants.KEY_NEW_PIN), operatorModel);

				phoenixIntegrationMessageVO.setCustomerIdentification((String) switchWrapper.getObject(CommandFieldConstants.KEY_CNIC));
				phoenixIntegrationMessageVO = prepareCNIC(phoenixIntegrationMessageVO);
				phoenixIntegrationMessageVO.setPAN(phoenixIntegrationMessageVO.getCustomerIdentification());
				phoenixIntegrationMessageVO.setNewPIN(newPin);
				phoenixIntegrationMessageVO.setSecureVerificationData(pin);

				auditLogModel.setInputParam(XMLUtil.replaceElementsUsingXPath(xStream.toXML(phoenixIntegrationMessageVO),
						XPathConstants.PhoenixAuditLogInputParamLocationSteps));

				// TODO uncomment JFS
				SwitchController integrationSwitch = getIntegrationSwitch(switchWrapper);

				if (integrationSwitch == null) {
					if (logger.isDebugEnabled()) {
						logger.debug("Phoenix switch down..escalating the exception");
					}
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
				}
				if (logger.isDebugEnabled()) {
					logger.debug("Hitting Phoenix to check balance..");
				}

				phoenixIntegrationMessageVO = (PhoenixIntegrationMessageVO) integrationSwitch.changeMPIN((IntegrationMessageVO) phoenixIntegrationMessageVO);

				auditLogModel.setCustomField1(PhoenixConstantsInterface.SUCCESS);
				auditLogModel.setIntegrationPartnerIdentifier(phoenixIntegrationMessageVO.getSystemTraceAuditNumber());
				// switchWrapper.getWorkFlowWrapper().getTransactionModel().setBankResponseCode(
				// phoenixIntegrationMessageVO.getResponseCode());

				if (phoenixIntegrationMessageVO != null) {
					switchWrapper.setIntegrationMessageVO(phoenixIntegrationMessageVO);

					try {
						parseResponseCode(phoenixIntegrationMessageVO);
					} catch (Exception e) {
						if (phoenixIntegrationMessageVO.getResponseCode() != null
								&& phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
										IntegrationConstants.PhoenixResponseCodes.INCORRECT_PIN.getResponseCodeValue())) {
							throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_INCORRECT_PIN);
						} else if (phoenixIntegrationMessageVO.getResponseCode() != null
								&& phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
										IntegrationConstants.PhoenixResponseCodes.PIN_RETRY_EXHAUSTED.getResponseCodeValue())) {
							throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_PIN_RETRIES_EXHAUSTED);
						} else if (phoenixIntegrationMessageVO.getResponseCode() != null
								&& phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
										IntegrationConstants.PhoenixResponseCodes.PIN_EXPIRED.getResponseCodeValue())) {
							throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_PIN_EXPIRED);
						}

						throw e;
					}

				} else {
					logger.error("Phoenix is down....."
							+ ExceptionProcessorUtility.prepareExceptionStackTrace(new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG)));
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
				}

				// if
				// (!phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
				// IntegrationConstants.IRISResponseCodes.OK.getResponseCodeValue()))
				// {
				// logger.error("Payment Services Processing Failed....."
				// + ExceptionProcessorUtility.prepareExceptionStackTrace(new
				// WorkFlowException(
				// WorkFlowErrorCodeConstants.PAYMENT_PROCESSING_FAILED)));
				// throw new
				// WorkFlowException(WorkFlowErrorCodeConstants.PAYMENT_PROCESSING_FAILED);
				// }
			} catch (Exception ex) {
				logger.error("Exception occured in microbank PhoenixSwitchImpl.... " + ex);
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

				}
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Ending checkBalance method of PhoenixSwitchImpl");
			}
		}

		/*****************************************************/

		return switchWrapper;
	}

	@Override
	public SwitchWrapper generatePIN(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {

		PhoenixIntegrationMessageVO phoenixIntegrationMessageVO = null;

		try {
			switchWrapper = this.customerProfile(switchWrapper);
			phoenixIntegrationMessageVO = (PhoenixIntegrationMessageVO) switchWrapper.getIntegrationMessageVO();

		} catch (Exception e) {
			phoenixIntegrationMessageVO = (PhoenixIntegrationMessageVO) switchWrapper.getIntegrationMessageVO();

			/** @todo change it **/
			if (phoenixIntegrationMessageVO != null) {
				if (phoenixIntegrationMessageVO.getResponseCode() != null
						&& phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
								IntegrationConstants.PhoenixResponseCodes.ACC_INACTIVE.getResponseCodeValue())) {
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_ACCOUNT_INACTIVE);
				} else if (phoenixIntegrationMessageVO.getResponseCode() != null
						&& phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
								IntegrationConstants.PhoenixResponseCodes.ACC_STATUS_INVALID.getResponseCodeValue())) {
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_INVALID_ACCOUNT_STATUS);
				} else if (phoenixIntegrationMessageVO.getResponseCode() != null
						&& phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
								IntegrationConstants.PhoenixResponseCodes.ACC_LOCKED.getResponseCodeValue())) {
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_ACCOUNT_LOCKED);
				}
				// else if( switchWrapper.getPaymentModeId().longValue() ==
				// PaymentModeConstantsInterface.CREDIT_CARD )
				// {
				else if (phoenixIntegrationMessageVO.getResponseCode() != null
						&& phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
								IntegrationConstants.PhoenixResponseCodes.CARD_STATUS_ERROR.getResponseCodeValue())) {
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_CARD_STATUS_ERROR);
				} else if (phoenixIntegrationMessageVO.getResponseCode() != null
						&& phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
								IntegrationConstants.PhoenixResponseCodes.WARM_CARD.getResponseCodeValue())) {
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_WARM_CARD);
				} else if (phoenixIntegrationMessageVO.getResponseCode() != null
						&& phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
								IntegrationConstants.PhoenixResponseCodes.HOT_CARD.getResponseCodeValue())) {
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_HOT_CARD);
				} else if (phoenixIntegrationMessageVO.getResponseCode() != null
						&& phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
								IntegrationConstants.PhoenixResponseCodes.BAD_CARD_STATUS.getResponseCodeValue())) {
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_BAD_CARD_STATUS);
				} else {
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
				}
				// }

			} else {
				throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
			}

		}

		// ************************************************* Checking mobile
		// channel delivery status
		if (phoenixIntegrationMessageVO != null
				&& !phoenixIntegrationMessageVO.getMobileChannelStatus().equalsIgnoreCase(
						IntegrationConstants.PhoenixDeliveryChannelStatusCodes.FIRST_TIME_LOGIN.getDeliveryChannelStatusCodeValue())) {
			throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_PIN_ALREADY_GENERATED_MSG);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Inside generatePIN method of PhoenixSwitchImpl");
		}
		AuditLogModel auditLogModel = this.auditLogBeforeCall(switchWrapper, "");
		XStream xStream = new XStream();

		phoenixIntegrationMessageVO = new PhoenixIntegrationMessageVO(false, switchWrapper.getSwitchSwitchModel().getPaymentGatewayCode(), null);

		if (switchWrapper != null) {
			try {
				if (switchWrapper != null) {
					phoenixIntegrationMessageVO.setCustomerIdentification((String) switchWrapper.getObject(CommandFieldConstants.KEY_CNIC));
					phoenixIntegrationMessageVO = prepareCNIC(phoenixIntegrationMessageVO);
					phoenixIntegrationMessageVO.setPAN(phoenixIntegrationMessageVO.getCustomerIdentification());

					phoenixIntegrationMessageVO.setNewPIN((String) switchWrapper.getObject(CommandFieldConstants.KEY_NEW_PIN));
					// phoenixIntegrationMessageVO
					// .setCustomerPINData((String)switchWrapper.getObject(CommandFieldConstants.KEY_NEW_PIN));
					// phoenixIntegrationMessageVO.setSecureVerificationData((String)switchWrapper.getObject(CommandFieldConstants.KEY_NEW_PIN));
				} else {
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_ACCOUNT_INFO_MISSING);
				}

				// auditLogModel.setInputParam(XMLUtil.replaceElementsUsingXPath(xStream.toXML(iso8583VO),

				// TODO ADD THE CODE FOR REPLACING CRITICAL INFORMATION WITH
				// ASTERICS

				auditLogModel.setInputParam(xStream.toXML(phoenixIntegrationMessageVO));

				SwitchController integrationSwitch = getIntegrationSwitch(switchWrapper);

				if (integrationSwitch == null) {
					if (logger.isDebugEnabled()) {
						logger.debug("Phoenix switch down..escalating the exception");
					}
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
				}
				if (logger.isDebugEnabled()) {
					logger.debug("Hitting Phoenix to check balance..");
				}

				phoenixIntegrationMessageVO = (PhoenixIntegrationMessageVO) integrationSwitch.generateMPIN((IntegrationMessageVO) phoenixIntegrationMessageVO);

				auditLogModel.setCustomField1(PhoenixConstantsInterface.SUCCESS);
				auditLogModel.setIntegrationPartnerIdentifier(phoenixIntegrationMessageVO.getSystemTraceAuditNumber());
				// switchWrapper.getWorkFlowWrapper().getTransactionModel().setBankResponseCode(
				// phoenixIntegrationMessageVO.getResponseCode());

				if (phoenixIntegrationMessageVO != null) {
					// phoenixIntegrationMessageVO.setResponseCode("00");
					switchWrapper.setIntegrationMessageVO(phoenixIntegrationMessageVO);

					try {
						parseResponseCode(phoenixIntegrationMessageVO);
					} catch (Exception e) {
						if (phoenixIntegrationMessageVO.getResponseCode() != null
								&& phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
										IntegrationConstants.PhoenixResponseCodes.INCORRECT_PIN.getResponseCodeValue())) {
							throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_INCORRECT_PIN);
						} else if (phoenixIntegrationMessageVO.getResponseCode() != null
								&& phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
										IntegrationConstants.PhoenixResponseCodes.PIN_RETRY_EXHAUSTED.getResponseCodeValue())) {
							throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_PIN_RETRIES_EXHAUSTED);
						} else if (phoenixIntegrationMessageVO.getResponseCode() != null
								&& phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
										IntegrationConstants.PhoenixResponseCodes.PIN_EXPIRED.getResponseCodeValue())) {
							throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_PIN_EXPIRED);
						}

						throw e;
					}
				} else {
					logger.error("Phoenix is down....."
							+ ExceptionProcessorUtility.prepareExceptionStackTrace(new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN)));
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
				}
			} catch (Exception ex) {
				logger.error("Exception occured in microbank PhoenixSwitchImpl.... " + ex);
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

				}
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Ending checkBalance method of PhoenixSwitchImpl");
			}
		}
		return switchWrapper;
	}

	public SwitchWrapper rollback(SwitchWrapper transactions) throws WorkFlowException {
		// TODO Implement the logic - JFS
		return null;
	}

	public SwitchWrapper transaction(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
		if (logger.isDebugEnabled()) {
			logger.debug("Inside transaction method of PhoenixSwitchImpl");
		}

		try {
			if (switchWrapper != null) {
				PhoenixIntegrationMessageVO phoenixIntegrationMessageVO = new PhoenixIntegrationMessageVO(false, switchWrapper.getSwitchSwitchModel()
						.getPaymentGatewayCode(), null);

				// ChannelUserIdentification
				phoenixIntegrationMessageVO.setCustomerIdentification((String) switchWrapper.getObject(CommandFieldConstants.KEY_CNIC));
				phoenixIntegrationMessageVO = prepareCNIC(phoenixIntegrationMessageVO);

				// Amount Paid
				if (switchWrapper.getTransactionTransactionModel() != null) {
					phoenixIntegrationMessageVO.setAmountPaid(String.valueOf(switchWrapper.getTransactionTransactionModel().getTotalAmount().intValue() * 100));
				} else if (switchWrapper.getWorkFlowWrapper().getTransactionModel() != null) {
					phoenixIntegrationMessageVO.setAmountPaid(String.valueOf(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount()
							.intValue() * 100));
				}

				if (switchWrapper.getWorkFlowWrapper() != null) {
					// Consummer Number
					if (switchWrapper.getWorkFlowWrapper().getTransactionTypeModel() != null) {
						if (switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionCodeIdTransactionCodeModel() != null
								&& switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionCodeIdTransactionCodeModel().getCode() != null) {
							phoenixIntegrationMessageVO.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionModel()
									.getTransactionCodeIdTransactionCodeModel().getCode());
						}
					}

					phoenixIntegrationMessageVO.setSecureVerificationData(switchWrapper.getWorkFlowWrapper().getMPin());
				}

				if (TransactionTypeConstantsInterface.BILL_SALE_TX == switchWrapper.getWorkFlowWrapper().getTransactionTypeModel().getTransactionTypeId()) {
					if (switchWrapper.getWorkFlowWrapper().getTransactionDetailModel() != null) {
						phoenixIntegrationMessageVO.setConsumerNumber(switchWrapper.getWorkFlowWrapper().getTransactionDetailModel().getConsumerNo());
					}
				} else {
					if (switchWrapper.getWorkFlowWrapper().getCustomerAppUserModel() != null) {
						phoenixIntegrationMessageVO.setConsumerNumber(switchWrapper.getWorkFlowWrapper().getCustomerAppUserModel().getMobileNo());
					}
				}

				SwitchUtilityMappingFacade switchUtilityMappingFacade = (SwitchUtilityMappingFacade) ctx
						.getBean(SwitchConstants.KEY_SWITCH_UTILITY_MAPPING_BEAN);

				SwitchUtilityMappingModel switchUtilityMappingModel = new SwitchUtilityMappingModel();
				switchUtilityMappingModel.setSwitchId(switchWrapper.getSwitchSwitchModel().getSwitchId());
				if (switchWrapper.getWorkFlowWrapper().getProductModel() != null) {
					switchUtilityMappingModel.setProductId(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId());
					BaseWrapper baseWrapper = new BaseWrapperImpl();
					baseWrapper.setBasePersistableModel(switchUtilityMappingModel);
					baseWrapper = switchUtilityMappingFacade.findUtilityCompanyCodeByExample(baseWrapper);

					switchUtilityMappingModel = (SwitchUtilityMappingModel) baseWrapper.getBasePersistableModel();

					if (switchUtilityMappingModel != null && switchUtilityMappingModel.getUtilityCompanyCode() != null) {
						// Utility Company Code
						phoenixIntegrationMessageVO.setUtilityCompanyId(switchUtilityMappingModel.getUtilityCompanyCode());
					} else {
						logger.error("Utility Company Code is missing.");
						throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_UTILITY_COMPANY_CODE);
					}
				} else
					phoenixIntegrationMessageVO.setUtilityCompanyId("MOBI0002");

				switchWrapper.setIntegrationMessageVO(phoenixIntegrationMessageVO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN);
		}

		return switchWrapper;
	}

	protected SwitchController getIntegrationSwitch(SwitchWrapper switchWrapper) {
		return HttpInvokerUtil.getHttpInvokerFactoryBean(SwitchController.class, switchWrapper.getSwitchSwitchModel().getUrl());
	}

	protected String getPhoenixAmount(String strAmount) {
		String sign = StringUtils.left(strAmount, 1);
		strAmount = StringUtils.removeStart(strAmount, sign);
		double amount = Double.parseDouble(strAmount);
		amount = amount / 100;
		strAmount = sign + String.valueOf(amount);
		return strAmount;
	}

	protected double getPhoenixAmountInDouble(String strAmount) {
		String sign = StringUtils.left(strAmount, 1);
		strAmount = StringUtils.removeStart(strAmount, sign);
		double amount = Double.parseDouble(strAmount);
		amount = amount / 100;
		return amount;
	}

	protected Date parsePhoenixDate(String date) {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		try {
			return dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected void parseResponseCode(PhoenixIntegrationMessageVO phoenixIntegrationMessageVO, boolean isTransaction) {
		if (phoenixIntegrationMessageVO.getResponseCode() == null) {
			logger.error("PHOENIX is down....."
					+ ExceptionProcessorUtility.prepareExceptionStackTrace(new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN)));
			throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN);
		} else if (!phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(IntegrationConstants.PhoenixResponseCodes.OK.getResponseCodeValue())) {
			logger.error("Payment Services Processing Failed....."
					+ ExceptionProcessorUtility.prepareExceptionStackTrace(new WorkFlowException(WorkFlowErrorCodeConstants.PAYMENT_PROCESSING_FAILED)));

			logger.error("========> Response code from PHOENIX : " + phoenixIntegrationMessageVO.getResponseCode());

			String message = "";

			if (isTransaction)
				message = "phoenix.trans." + phoenixIntegrationMessageVO.getResponseCode();
			else
				message = "phoenix.req." + phoenixIntegrationMessageVO.getResponseCode();

			try {
				message = MessageUtil.getMessage(message);
			} catch (NoSuchMessageException e) {
				message = WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG;
			}

			throw new WorkFlowException(message);
		}
	}

	protected void parseResponseCode(PhoenixIntegrationMessageVO phoenixIntegrationMessageVO) {
		parseResponseCode(phoenixIntegrationMessageVO, true);
	}

	protected PhoenixIntegrationMessageVO prepareAccountDetailForBA(PhoenixIntegrationMessageVO phoenixIntegrationMessageVO) {
		String accountDetail = "";

		accountDetail = StringUtils.rightPad(phoenixIntegrationMessageVO.getAccountNumber(), 20);
		accountDetail += StringUtils.rightPad(phoenixIntegrationMessageVO.getAccountType(), 2);
		accountDetail += StringUtils.rightPad(phoenixIntegrationMessageVO.getAccountCurrency(), 3);

		phoenixIntegrationMessageVO.setAccountDetail(accountDetail);

		return phoenixIntegrationMessageVO;
	}

	protected PhoenixIntegrationMessageVO prepareCNIC(PhoenixIntegrationMessageVO phoenixIntegrationMessageVO) {
		String CNIC = phoenixIntegrationMessageVO.getCustomerIdentification();
		CNIC = CNIC.substring(0, 5) + "-" + CNIC.substring(5, 12) + "-" + CNIC.substring(12);

		CNIC = StringUtils.rightPad(CNIC, 20);
		phoenixIntegrationMessageVO.setCustomerIdentification(CNIC);

		// System.out.println("CNIC" + CNIC);

		return phoenixIntegrationMessageVO;
	}

	protected PhoenixIntegrationMessageVO prepareAccountDetailForCC(PhoenixIntegrationMessageVO phoenixIntegrationMessageVO) {
		String accountDetail = "";

		accountDetail = StringUtils.rightPad(phoenixIntegrationMessageVO.getCardNumber(), 20);
		String YY = phoenixIntegrationMessageVO.getCardExpiry().substring(3);
		String MM = phoenixIntegrationMessageVO.getCardExpiry().substring(0, 2);
		accountDetail += YY + MM;
		accountDetail += " ";

		phoenixIntegrationMessageVO.setAccountDetail(accountDetail);

		return phoenixIntegrationMessageVO;
	}

	public String decryptPin(String pin, OperatorModel operatorModel) {
		if (null != pin && null != operatorModel.getKey() && !pin.equals("")) {
			pin = StringUtil.replaceSpacesWithPlus(pin);
			pin = EncryptionUtil.doDecrypt(operatorModel.getKey(), pin);
		}
		return pin;
	}

	/*
	 * public static void main ( String []a ) {
	 * 
	 * String cnic = "3620363367591" ; System.out.println( cnic.substring(0,5)
	 * ); System.out.println( cnic.substring(5,12) ); System.out.println(
	 * cnic.substring(12) ); }
	 */

	public SwitchWrapper getLedger(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException, Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
