package com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix;

/**
 * Project Name: 			Microbank	
 * @author 					Jawwad Farooq
 * Creation Date: 			February 2008  			
 * Description:				
 */

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.remoting.RemoteAccessException;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.XMLUtil;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AuditLogModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.TransactionConstantsInterface;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.common.util.XPathConstants;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.server.service.switchmodule.IntegrationMessageVO;
import com.inov8.microbank.server.service.switchmodule.iris.IntegrationConstants;
import com.inov8.microbank.server.service.switchmodule.iris.SwitchController;
import com.inov8.microbank.server.service.switchmodule.iris.model.PhoenixIntegrationMessageVO;
import com.thoughtworks.xstream.XStream;


public class PhoenixCCSwitchImpl extends PhoenixSwitchImpl
{
	
	public PhoenixCCSwitchImpl(ApplicationContext ctx)
	{
		super(ctx);
	}
	
	public SwitchWrapper transaction(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Inside transaction method of PhoenixSwitchImpl");
		}
		AuditLogModel auditLogModel = this.auditLogBeforeCall(switchWrapper, "");
		XStream xStream = new XStream();
		
		switchWrapper = super.transaction(switchWrapper);

		if (switchWrapper != null)
		{
			PhoenixIntegrationMessageVO phoenixIntegrationMessageVO = (PhoenixIntegrationMessageVO)switchWrapper.getIntegrationMessageVO() ;

			try
			{
					if (switchWrapper.getAccountInfoModel() != null)
					{						
						phoenixIntegrationMessageVO.setCardNumber(switchWrapper.getAccountInfoModel().getCardNo());
						phoenixIntegrationMessageVO.setCardExpiry(switchWrapper.getAccountInfoModel().getCardExpiryDate());	
						phoenixIntegrationMessageVO = this.prepareAccountDetailForCC(phoenixIntegrationMessageVO);
												
					}
					if( switchWrapper.getWorkFlowWrapper() != null && switchWrapper.getWorkFlowWrapper().getTransactionCodeModel() != null )
						phoenixIntegrationMessageVO.setMicrobankTransactionCode( switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode() );

				auditLogModel.setInputParam(XMLUtil.replaceElementsUsingXPath(xStream.toXML(phoenixIntegrationMessageVO),
				 XPathConstants.PhoenixAuditLogInputParamLocationSteps));

				SwitchController integrationSwitch = getIntegrationSwitch(switchWrapper);

				if (integrationSwitch == null)
				{
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN);
				}

				if (logger.isDebugEnabled())
				{
					logger.debug("Going to make transaction at Phoenix");
				}

				phoenixIntegrationMessageVO = (PhoenixIntegrationMessageVO) integrationSwitch
						.transaction((IntegrationMessageVO) phoenixIntegrationMessageVO);

				parseResponseCode(phoenixIntegrationMessageVO);
				
				String amount = phoenixIntegrationMessageVO.getAmountPaid();
				if (!StringUtils.isBlank(amount))
				{
				}
				if(switchWrapper.getCommissioned())
				{
					phoenixIntegrationMessageVO.setAmountPaid((new Double(amount).doubleValue() - switchWrapper.getDiscountAmount())+"");
				}
				else
				{
					phoenixIntegrationMessageVO.setAmountPaid(amount);
				}
				

				auditLogModel.setCustomField1(TransactionConstantsInterface.SUCCESS);

				if (logger.isDebugEnabled())
				{
					logger.debug("Response from Phoenix : " + phoenixIntegrationMessageVO.getResponseCode());
				}

				auditLogModel.setIntegrationPartnerIdentifier(phoenixIntegrationMessageVO.getSystemTraceAuditNumber()
						+ phoenixIntegrationMessageVO.getTransmissionDateAndTime());
				switchWrapper.getWorkFlowWrapper().getTransactionModel().setBankResponseCode(
						phoenixIntegrationMessageVO.getRetrievalReferenceNumber());

////				if (phoenixIntegrationMessageVO.getResponseCode() == null)
////				{
////					logger.error("Payment Services Processing Failed....."
////							+ ExceptionProcessorUtility.prepareExceptionStackTrace(new WorkFlowException(
////									WorkFlowErrorCodeConstants.IRIS_SERVICE_DOWN)));
////					throw new WorkFlowException(WorkFlowErrorCodeConstants.IRIS_SERVICE_DOWN);
////				}
////				else if (!phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
////						IntegrationConstants.IRISResponseCodes.OK.getResponseCodeValue()))
////				{
////					String msg = getMessageFromResource(phoenixIntegrationMessageVO.getResponseCode());
////					logger.error(msg);
////					throw new WorkFlowException(msg);
////				}
//
//				else if (phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
//						IntegrationConstants.IRISResponseCodes.OK.getResponseCodeValue()))
//				{
//					return switchWrapper;
//				}
			}
			catch (Exception ex)
			{
				logger.error("Exception occured in PhoenixBASwitchImpl.... "
						+ ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
				auditLogModel.setCustomField1(TransactionConstantsInterface.FAILURE);
				auditLogModel.setCustomField2(ex.getMessage());
				if (ex instanceof WorkFlowException)
					throw (WorkFlowException) ex;
				else if (ex instanceof FrameworkCheckedException)
					throw (FrameworkCheckedException) ex;
				else if (ex instanceof IOException)
					throw new FrameworkCheckedException(MessageUtil.getMessage("genericErrorMessage"));
				else if (ex instanceof RemoteAccessException)
					throw new FrameworkCheckedException(MessageUtil.getMessage("genericErrorMessage"));
				else
					throw new WorkFlowException(WorkFlowErrorCodeConstants.IRIS_SERVICE_DOWN);
			}
			finally
			{
				if (phoenixIntegrationMessageVO!= null)				
				{
					phoenixIntegrationMessageVO.setMessageAsEdi("");
					this.auditLogAfterCall(auditLogModel, XMLUtil.replaceElementsUsingXPath(xStream
							.toXML(phoenixIntegrationMessageVO),
							XPathConstants.PhoenixAuditLogInputParamLocationSteps));

				}
			}
			 if(logger.isDebugEnabled())
			 {
			 logger.debug("Ending transaction method of PhoenixSwitchImpl");			
			 }
		}
		
		return switchWrapper;
	}
	
	public SwitchWrapper checkBalance(SwitchWrapper switchWrapper) throws WorkFlowException
	{
		
		if (logger.isDebugEnabled())
		{
			logger.debug("Inside checkBalance method of PhoenixSwitchImpl");
		}
		//AuditLogModel auditLogModel = this.auditLogBeforeCall(switchWrapper, "");
		XStream xStream = new XStream();
		 
		PhoenixIntegrationMessageVO phoenixIntegrationMessageVO = new PhoenixIntegrationMessageVO(false, switchWrapper
				.getSwitchSwitchModel().getPaymentGatewayCode(), null);

		if (switchWrapper != null)
		{
			try
			{
				if (switchWrapper != null && switchWrapper.getWorkFlowWrapper() != null)
				{
					phoenixIntegrationMessageVO.setCardNumber(switchWrapper.getAccountInfoModel().getCardNo());
					phoenixIntegrationMessageVO.setCardExpiry(switchWrapper.getAccountInfoModel().getCardExpiryDate());
											
						phoenixIntegrationMessageVO.setCustomerIdentification((String) switchWrapper
								.getObject(CommandFieldConstants.KEY_CNIC));
						phoenixIntegrationMessageVO = prepareCNIC(phoenixIntegrationMessageVO);
						
						if( switchWrapper.getWorkFlowWrapper() != null && switchWrapper.getWorkFlowWrapper().getDeviceTypeModel() != null 
								&& switchWrapper.getWorkFlowWrapper().getDeviceTypeModel().getDeviceTypeId().longValue() == DeviceTypeConstantsInterface.WEB.longValue())
						{
							phoenixIntegrationMessageVO.setSecureVerificationData(PhoenixConstantsInterface.PORTAL_USER_PIN);
						}
						else
							phoenixIntegrationMessageVO
								.setSecureVerificationData(switchWrapper.getWorkFlowWrapper().getMPin());
				}
				else
				{
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_ACCOUNT_INFO_MISSING);
				}

				//auditLogModel.setInputParam(XMLUtil.replaceElementsUsingXPath(xStream.toXML(phoenixIntegrationMessageVO),
				// XPathConstants.PhoenixAuditLogInputParamLocationSteps));

				SwitchController integrationSwitch = getIntegrationSwitch(switchWrapper);

				if (integrationSwitch == null)
				{
					if (logger.isDebugEnabled())
					{
						logger.debug("Phoenix switch down..escalating the exception");
					}
					throw new WorkFlowException(WorkFlowErrorCodeConstants.IRIS_SERVICE_DOWN);
				}
				if (logger.isDebugEnabled())
				{
					logger.debug("Hitting PHOENIX to check balance..");
				}

				phoenixIntegrationMessageVO = (PhoenixIntegrationMessageVO) integrationSwitch
						.checkBalanceForCreditCard((IntegrationMessageVO) phoenixIntegrationMessageVO);

				//phoenixIntegrationMessageVO.setResponseCode("00");
				//auditLogModel.setIntegrationPartnerIdentifier(phoenixIntegrationMessageVO.getSystemTraceAuditNumber());
				switchWrapper.getWorkFlowWrapper().getTransactionModel().setBankResponseCode(
						phoenixIntegrationMessageVO.getResponseCode());

				if (phoenixIntegrationMessageVO != null)
				{
					
					try
					{
						switchWrapper.setIntegrationMessageVO( phoenixIntegrationMessageVO );
						parseResponseCode(phoenixIntegrationMessageVO);
					}
					catch (Exception e)
					{
						if( phoenixIntegrationMessageVO.getResponseCode() != null && 
								phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase( IntegrationConstants.PhoenixResponseCodes.ACC_INACTIVE.getResponseCodeValue() ) )
						{
							throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_ACCOUNT_INACTIVE);
						}
						else if( phoenixIntegrationMessageVO.getResponseCode() != null && 
								phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase( IntegrationConstants.PhoenixResponseCodes.INCORRECT_PIN.getResponseCodeValue() ) )
						{
							throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_INCORRECT_PIN);
						}
						else if( phoenixIntegrationMessageVO.getResponseCode() != null && 
								phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase( IntegrationConstants.PhoenixResponseCodes.PIN_RETRY_EXHAUSTED.getResponseCodeValue() ) )
						{
							throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_PIN_RETRIES_EXHAUSTED);
						}
						else if( phoenixIntegrationMessageVO.getResponseCode() != null && 
								phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase( IntegrationConstants.PhoenixResponseCodes.ACC_STATUS_INVALID.getResponseCodeValue() ) )
						{
							throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_INVALID_ACCOUNT_STATUS);
						}
						else if( phoenixIntegrationMessageVO.getResponseCode() != null && 
								phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase( IntegrationConstants.PhoenixResponseCodes.ACC_LOCKED.getResponseCodeValue() ) )
						{
							throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_ACCOUNT_LOCKED);
						}
						else if( phoenixIntegrationMessageVO.getResponseCode() != null && 
								phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase( IntegrationConstants.PhoenixResponseCodes.PIN_EXPIRED.getResponseCodeValue() ) )
						{
							throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_PIN_EXPIRED);
						}
						else if( phoenixIntegrationMessageVO.getResponseCode() != null && 
								phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase( IntegrationConstants.PhoenixResponseCodes.LIMIT_EXCEEDED.getResponseCodeValue() ) )
						{
							throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN);
						}
//						else if( switchWrapper.getPaymentModeId().longValue() == PaymentModeConstantsInterface.CREDIT_CARD  )
//						{
							if( phoenixIntegrationMessageVO.getResponseCode() != null && 
									phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase( IntegrationConstants.PhoenixResponseCodes.CARD_STATUS_ERROR.getResponseCodeValue() ) )
							{
								throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_CARD_STATUS_ERROR);
							}
							else if( phoenixIntegrationMessageVO.getResponseCode() != null && 
									phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase( IntegrationConstants.PhoenixResponseCodes.WARM_CARD.getResponseCodeValue() ) )
							{
								throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_WARM_CARD);
							}
							else if( phoenixIntegrationMessageVO.getResponseCode() != null && 
									phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase( IntegrationConstants.PhoenixResponseCodes.HOT_CARD.getResponseCodeValue() ) )
							{
								throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_HOT_CARD);
							}
							else if( phoenixIntegrationMessageVO.getResponseCode() != null && 
									phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase( IntegrationConstants.PhoenixResponseCodes.BAD_CARD_STATUS.getResponseCodeValue() ) )
							{
								throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_BAD_CARD_STATUS);
							}
//						}
						else
							throw e;
					}	
					
						String amountDue = phoenixIntegrationMessageVO.getCurrentBalance() ;
						if (!StringUtils.isBlank(amountDue))
						{			
							switchWrapper.setAmountDue(this.getPhoenixAmountInDouble(amountDue));
						}
						
						String creditLimit = phoenixIntegrationMessageVO.getAvailableCreditLimit();
						if (!StringUtils.isBlank(creditLimit))
						{							
							switchWrapper.setCreditLimit(this.getPhoenixAmountInDouble(creditLimit));
						}

						String minAmountDue = phoenixIntegrationMessageVO.getMinPaymentDue();
						if (!StringUtils.isBlank(minAmountDue))
						{							
							switchWrapper.setMinAmountDue(this.getPhoenixAmountInDouble(minAmountDue));
						}

						String dueDate = phoenixIntegrationMessageVO.getDueDate() ;
						
						if (!StringUtils.isBlank(dueDate))
						{
							switchWrapper.setDueDate( this.parsePhoenixDate(dueDate) );
						}
					

					if (phoenixIntegrationMessageVO.getResponseCode() == null)
					{
						logger.error("PHOENIX is down....."
								+ ExceptionProcessorUtility.prepareExceptionStackTrace(new WorkFlowException(
										WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN)));
						throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN);
					}

					else if (!phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
							IntegrationConstants.PhoenixResponseCodes.OK.getResponseCodeValue()))
					{
						String msg = getMessageFromResource(phoenixIntegrationMessageVO.getResponseCode());
						logger.error(msg);
						throw new WorkFlowException(msg);
					}

				}
				else
				{
					logger.error("PHOENIX is down....."
							+ ExceptionProcessorUtility.prepareExceptionStackTrace(new WorkFlowException(
									WorkFlowErrorCodeConstants.IRIS_SERVICE_DOWN)));
					throw new WorkFlowException(WorkFlowErrorCodeConstants.IRIS_SERVICE_DOWN);
				}

				// TODO Check at what point the following code will be executed
				if (!phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
						IntegrationConstants.PhoenixResponseCodes.OK.getResponseCodeValue()))
				{
					logger.error("Payment Services Processing Failed....."
							+ ExceptionProcessorUtility.prepareExceptionStackTrace(new WorkFlowException(
									WorkFlowErrorCodeConstants.PAYMENT_PROCESSING_FAILED)));
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PAYMENT_PROCESSING_FAILED);
				}
			}
			catch (Exception ex)
			{
				logger.error("Exception occured in PhoenixBASwitchImpl.... " + ex);
				//auditLogModel.setCustomField2(ex.getMessage());

				if (ex instanceof WorkFlowException)
					throw (WorkFlowException) ex;
				else if (ex instanceof IOException)
					throw new WorkFlowException(MessageUtil.getMessage("genericErrorMessage"));
				else if (ex instanceof RemoteAccessException)
					throw new WorkFlowException(MessageUtil.getMessage("genericErrorMessage"));
				else
					throw new WorkFlowException(WorkFlowErrorCodeConstants.IRIS_SERVICE_DOWN);
			}
			finally
			{
				if (phoenixIntegrationMessageVO!= null)				
				{
					phoenixIntegrationMessageVO.setMessageAsEdi("");
					//this.auditLogAfterCall(auditLogModel, XMLUtil.replaceElementsUsingXPath(xStream
						//	.toXML(phoenixIntegrationMessageVO),
						//	XPathConstants.PhoenixAuditLogInputParamLocationSteps));

				}
			}
			if (logger.isDebugEnabled())
			{
				logger.debug("Ending checkBalance method of PhoenixSwitchImpl");
			}
		}
		return switchWrapper;

	}


}
