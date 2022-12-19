package com.inov8.microbank.common.util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.XMLUtil;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CommissionRateModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.MiniTransactionModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.vo.ussd.UserState;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.dao.commissionmodule.CommissionRateDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.commandmodule.CommandManager;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.webservice.bean.USSDInputDTO;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.model.LogModel;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl;
import com.inov8.verifly.server.service.mainmodule.VeriflyManager;
import com.thoughtworks.xstream.XStream;

public class UssdValidator {

	protected final Log logger = LogFactory.getLog(UssdValidator.class);

	public String validateGeneralInput(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, UserState userState) {
		if(logger.isDebugEnabled()){
			logger.debug("UssdValidator.validateGeneralInput Starts");
		}
		String retVal=null;
			if(!userState.getValidInputs().contains(i8SBSwitchControllerRequestVO.getuSSDRequestString())){
				retVal="Invalid Input\n";
			}
			if(retVal==null){
				if(CustomInputTypeContants.UTILITY_TELEPHONE.equals(userState.getCutomInput())){
					retVal=validateUtilityTelephone(i8SBSwitchControllerRequestVO, userState);
				}else if(CustomInputTypeContants.UTILITY_GAS.equals(userState.getCutomInput())){
					retVal=validateUtilityGas(i8SBSwitchControllerRequestVO, userState);
				}else if(CustomInputTypeContants.UTILITY_ELECTRICITY.equals(userState.getCutomInput())){
					retVal=validateUtilityElectricity(i8SBSwitchControllerRequestVO, userState);
				}else if(CustomInputTypeContants.UTILITY_INTERNET.equals(userState.getCutomInput())){
					retVal=validateUtilityInternet(i8SBSwitchControllerRequestVO, userState);
				}else if(CustomInputTypeContants.UTILITY_WATER.equals(userState.getCutomInput())){
					retVal=validateUtilityWater(i8SBSwitchControllerRequestVO, userState);
				}else if(CustomInputTypeContants.UTILITY_MUNICIPAL.equals(userState.getCutomInput())){
					retVal=validateUtilityMunicipal(i8SBSwitchControllerRequestVO, userState);
				}
				else if(CustomInputTypeContants.BAL_CHECK.equals(userState.getCutomInput())){
					validateBalanceCheck(i8SBSwitchControllerRequestVO, userState);
				}
				else if(CustomInputTypeContants.UTILITY_PREPAID.equals(userState.getCutomInput())){
					validatePrepaid(i8SBSwitchControllerRequestVO, userState);
				}
				else if(CustomInputTypeContants.UTILITY_POSTPAID.equals(userState.getCutomInput())){
					validatePostpaid(i8SBSwitchControllerRequestVO, userState);
				}
				else if(CustomInputTypeContants.PAYMENT_MODE.equals(userState.getCutomInput())) {
					validatePaymentMode(i8SBSwitchControllerRequestVO, userState);
				}
				else if(CustomInputTypeContants.GOV_PAYMENTS.equals(userState.getCutomInput())){
					validateGovPayments(i8SBSwitchControllerRequestVO, userState);
				}
			}

			if(logger.isDebugEnabled()){
				logger.debug("UssdValidator.validateGeneralInput Ends");
			}

		return retVal;
	}



	public String validateCustomInput(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, CommonCommandManager commonCommandManager, UserState userState, CommandManager commandManager, ActionLogManager actionLogManager,I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO) {
		if (logger.isDebugEnabled()) {
			logger.debug("UssdValidator.validateCustomInput starts");
		}
		String retVal = null;
		if (userState.getCutomInput() != null) {
			if (CustomInputTypeContants.AGENT_MSISDN.equals(userState.getCutomInput())) {
				retVal = validateAgentMobile(i8SBSwitchControllerRequestVO, commonCommandManager, userState, actionLogManager, commandManager);
			} else if (CustomInputTypeContants.CUSTOMER_MSISDN.equals(userState.getCutomInput())) {
				retVal = validateCustomerMobile(i8SBSwitchControllerRequestVO, userState, commonCommandManager);
			} else if (CustomInputTypeContants.TRANSFER_IN_AMOUNT.equals(userState.getCutomInput())) {
				retVal = validateTransferInAmount(i8SBSwitchControllerRequestVO, userState, commonCommandManager);
			} else if (CustomInputTypeContants.TRANSFER_OUT_AMOUNT.equals(userState.getCutomInput())) {
				retVal = validateTransferOutAmount(i8SBSwitchControllerRequestVO, userState, commonCommandManager);
			} else if (CustomInputTypeContants.PIN.equals(userState.getCutomInput())) {
				retVal = validatePin(i8SBSwitchControllerRequestVO, userState, commonCommandManager, actionLogManager,i8SBSwitchControllerResponseVO);
			} else if (CustomInputTypeContants.JCASH_ACCOUNT_NO.equals(userState.getCutomInput())) {
				validateJCashAccount(i8SBSwitchControllerRequestVO, userState);
			} else if (CustomInputTypeContants.NEW_PIN.equals(userState.getCutomInput())) {
				retVal = validateNewPin(i8SBSwitchControllerRequestVO, userState);
			} else if (CustomInputTypeContants.CONFIRM_NEW_PIN.equals(userState.getCutomInput())) {
				retVal = validateConfirmNewPin(i8SBSwitchControllerRequestVO, userState);
			} else if (CustomInputTypeContants.LPIN.equals(userState.getCutomInput())) {
				retVal = validateLoginPin(i8SBSwitchControllerRequestVO, userState);
			} else if (CustomInputTypeContants.CHALLANID.equals(userState.getCutomInput())) {
				retVal = validateChallanID(i8SBSwitchControllerRequestVO, userState);
			} else if (CustomInputTypeContants.RECEIVER_ACCOUNT_NO.equals(userState.getCutomInput())) {
				retVal = validateReceiverAccountNo(i8SBSwitchControllerRequestVO, userState);
			} else if (CustomInputTypeContants.BP_CONSUMER_NUMBER.equals(userState.getCutomInput())) {
				retVal = validateConsumerNumber(i8SBSwitchControllerRequestVO, userState);
			}
			else if (CustomInputTypeContants.BILL_AMOUNT.equals(userState.getCutomInput())) {
				retVal = validateBillAmount(i8SBSwitchControllerRequestVO, userState);
			}
			else if (CustomInputTypeContants.CUSTOMER_BILL_AMOUNT.equals(userState.getCutomInput())) {
				retVal = validateBillAmount(i8SBSwitchControllerRequestVO, userState);
			}
			else if (CustomInputTypeContants.VERIFY_OTP.equals(userState.getCutomInput())) {
				retVal = validateOTP(i8SBSwitchControllerRequestVO, userState, commonCommandManager, actionLogManager, i8SBSwitchControllerResponseVO);
			}
			else if (CustomInputTypeContants.MSISDN.equals(userState.getCutomInput())) {
				retVal = validateBillPaymentMobile(i8SBSwitchControllerRequestVO, userState);
			}
			else if (CustomInputTypeContants.RECEIVER_CNIC.equals(userState.getCutomInput())) {
				retVal = validateCNIC(i8SBSwitchControllerRequestVO, userState);
			}
			else if (CustomInputTypeContants.CNIC_AMOUNT.equals(userState.getCutomInput())) {
				retVal = validateCNICAmount(i8SBSwitchControllerRequestVO, userState);
			}
			else if (CustomInputTypeContants.WALLET_AMOUNT.equals(userState.getCutomInput())) {
				retVal = validateWalletAmount(i8SBSwitchControllerRequestVO, userState);
			}
            else if (CustomInputTypeContants.CORE_AMOUNT.equals(userState.getCutomInput())) {
                retVal = validateCoreAmount(i8SBSwitchControllerRequestVO, userState);
            }
            else if (CustomInputTypeContants.CORE_ACC_NO.equals(userState.getCutomInput())) {
                retVal = validateCoreAccount(i8SBSwitchControllerRequestVO, userState);
            }
		} else {
			retVal = "Invalid Input\n";
		}

		if(logger.isDebugEnabled()){
			logger.debug("UssdValidator.validateCustomInput Ends");
		}
		return retVal;
	}

	private String validateBillAmount(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, UserState userState) {
		if(logger.isDebugEnabled()){
			logger.debug("UssdValidator.validateBPAmount Start");
		}

		// logger.info("[UssdValidator.validateTransferInAmount] Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Amount:" + ussdInputDTO.getMessege());

		String retVal=null;

		if(i8SBSwitchControllerRequestVO.getuSSDRequestString() != null &&  !i8SBSwitchControllerRequestVO.getuSSDRequestString().equals(""))
		{
			if(StringUtil.isNumeric(i8SBSwitchControllerRequestVO.getuSSDRequestString()))
			{
				userState.setTransferAmount(i8SBSwitchControllerRequestVO.getuSSDRequestString());
				userState.setUtilityBillAmount(i8SBSwitchControllerRequestVO.getuSSDRequestString());
				i8SBSwitchControllerRequestVO.setuSSDRequestString("-1");
			}
			else
			{
				retVal = "Invalid Amount Entered.";

			}
		}
		else
			retVal = "Invalid Amount Entered.";

		return retVal;
	}

	private String validateOTP(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, UserState userState, CommonCommandManager commonCommandManager, ActionLogManager actionLogManager, I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO) {

		String retVal = null;
		if( i8SBSwitchControllerRequestVO.getuSSDRequestString() != null && !i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("")) {


			MiniTransactionModel miniTransactionModel = new MiniTransactionModel();
			miniTransactionModel.setMobileNo(userState.getMsisdn());
			miniTransactionModel.setAppUserId(userState.getCustomerAppUserModel().getAppUserId());
			miniTransactionModel.setCommandId(Long.valueOf(CommandFieldConstants.CMD_CASH_DEPOSIT));
			miniTransactionModel.setMiniTransactionStateId(MiniTransactionStateConstant.PIN_SENT);
			String PIN = EncryptionUtil.encryptWithAES(XMLConstants.OTP_ENCRYPTION_KEY, i8SBSwitchControllerRequestVO.getuSSDRequestString());
			try {
				logger.info("[USSDValidator] Going to Validate OTP");
				Long errorCode = commonCommandManager.getMiniTransactionManager().verifyOTP(miniTransactionModel,PIN);

				if (errorCode == -1) {
					logger.info("[USSDValidator] OTP Successfully Validated");
					userState.setUserOTP(i8SBSwitchControllerRequestVO.getuSSDRequestString());
					i8SBSwitchControllerRequestVO.setuSSDRequestString("-1");
				}
				if (ErrorCodes.OTP_EXPIRED.longValue() == errorCode)
					throw new FrameworkCheckedException(MessageUtil.getMessage(ErrorCodes.DEVICE_OTP_EXPIRED), null, ErrorCodes.DEVICE_OTP_EXPIRED, null);

				else if (ErrorCodes.OTP_INVALID.longValue() == errorCode)
					throw new FrameworkCheckedException(MessageUtil.getMessage(ErrorCodes.DEVICE_OTP_INVALID), null, ErrorCodes.DEVICE_OTP_INVALID, null);
			} catch (FrameworkCheckedException e) {
				logger.error(e.getMessage());
				retVal = e.getMessage();
				logger.info("[USSDValidator]"+e.getMessage());
			}
		}
		else
			retVal = "Invalid OTP Length";

		return retVal;
	}

	public boolean validateMobile(String cellNumber) {
		if(logger.isDebugEnabled()){
			logger.debug("UssdValidator.validateMobile starts");
		}
		boolean retVal = false;
		if (cellNumber.startsWith("+92") && cellNumber.length() == 13 && StringUtil.isInteger(cellNumber.substring(3))) {
			retVal = true;
		} else if (cellNumber.startsWith("0092") && cellNumber.length() == 14 && StringUtil.isInteger(cellNumber)) {
			retVal = true;
		} else if (cellNumber.startsWith("3") && cellNumber.length() == 10 && StringUtil.isInteger(cellNumber)) {
			retVal = true;
		} else if (cellNumber.startsWith("92") && cellNumber.length() == 12 && StringUtil.isInteger(cellNumber)) {
			retVal = true;
		} else if (cellNumber.startsWith("03") && cellNumber.length() == 11 && StringUtil.isInteger(cellNumber)) {
			retVal = true;
		}

		if(logger.isDebugEnabled()){
			logger.debug("UssdValidator.validateMobile End");
		}
		return retVal;
	}

	public String formatMobileNo(String cellNumber) {
		if(logger.isDebugEnabled()){
			logger.debug("UssdValidator.formatMobileNo starts");
		}
		String retVal = null;
		if (cellNumber.startsWith("+92") && cellNumber.length() == 13 && StringUtil.isInteger(cellNumber.substring(3))) {
			retVal = cellNumber.replaceFirst("\\+92", "0");
		} else if (cellNumber.startsWith("0092") && cellNumber.length() == 14 && StringUtil.isInteger(cellNumber)) {
			retVal = cellNumber.replaceFirst("0092", "0");
		} else if (cellNumber.startsWith("3") && cellNumber.length() == 10 && StringUtil.isInteger(cellNumber)) {
			retVal = cellNumber.replaceFirst("3", "03");
		} else if (cellNumber.startsWith("92") && cellNumber.length() == 12 && StringUtil.isInteger(cellNumber)) {
			retVal = cellNumber.replaceFirst("92", "0");
		}else if (cellNumber.startsWith("03") && cellNumber.length() == 11 && StringUtil.isInteger(cellNumber)) {
			retVal = cellNumber;
		}

		logger.debug("Mobile No Formatted========="+retVal);
		if(logger.isDebugEnabled()){
			logger.debug("UssdValidator.formatMobileNo End");
		}
		return retVal;
	}
	public boolean validateConsumerNumber(String consumerNumber){
		boolean retVal=false;
		if(consumerNumber.length()>=5)
			retVal=true;//TODO: validate consumer number
		return retVal;
	}

	public String validateCNIC(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO,UserState userState) {
		if(logger.isDebugEnabled()){
			logger.debug("UssdValidator.validateCNIC starts");
		}
		boolean retVal;
		String retVal1 = null;
		retVal=Pattern.matches("^[0-9+]{5}-[0-9+]{7}-[0-9]{1}$", i8SBSwitchControllerRequestVO.getuSSDRequestString())
		|| Pattern.matches("^[0-9+]{5}[0-9+]{7}[0-9]{1}$",  i8SBSwitchControllerRequestVO.getuSSDRequestString());

		if(retVal == true)
		{
			userState.setWalkinReceiverCNIC(i8SBSwitchControllerRequestVO.getuSSDRequestString()); ;
			i8SBSwitchControllerRequestVO.setuSSDRequestString("-1");
		}
		else
			retVal1 =  "Invalid CNIC Length";

		if(logger.isDebugEnabled()){
			logger.debug("UssdValidator.validateCNIC End");
		}
		return retVal1;
	}

	public String validateConsumerNumber(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, UserState userState) {
		if(logger.isDebugEnabled()){
			logger.debug("UssdValidator.validateConsumerNumber starts");
		}
		String retVal=null;
		if(validateConsumerNumber(i8SBSwitchControllerRequestVO.getuSSDRequestString())){
			userState.setBillPaymentConsumerNumber(i8SBSwitchControllerRequestVO.getuSSDRequestString()) ;
			i8SBSwitchControllerRequestVO.setuSSDRequestString("-1");
		}else{
			retVal = "Invalid Consumer number\n";
		}
		if(logger.isDebugEnabled()){
			logger.debug("UssdValidator.validateConsumerNumber End");
		}
		return retVal;
	}
	public String validateUtilityTelephone(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, UserState userState) {

		if(logger.isDebugEnabled()){
			logger.debug("UssdValidator.validateUtilityPTCL starts");
		}
		String retVal=null;
		Long appUserType = ThreadLocalAppUser.getAppUserModel().getAppUserTypeId().longValue();
		if( appUserType == UserTypeConstantsInterface.RETAILER.longValue() || appUserType == UserTypeConstantsInterface.CUSTOMER) {

			switch (i8SBSwitchControllerRequestVO.getuSSDRequestString())
			{
				case "1":
					userState.setUtilityCompanyID(ProductConstantsInterface.PTCL_VFONE.toString());
					userState.setUtilityCompanyName("PTCL VFONE");
                    break;
				case "2":
					userState.setUtilityCompanyID(ProductConstantsInterface.PTCL_LANDLINE.toString());
					userState.setUtilityCompanyName("PTCL LANDLINE ");
					break;
				case "3":
					userState.setUtilityCompanyID(ProductConstantsInterface.WATEEN.toString());
					userState.setUtilityCompanyName("WATEEN");
					break;
				case "4":
					userState.setUtilityCompanyID(ProductConstantsInterface.WITRIBE.toString());
					userState.setUtilityCompanyName("WITRIBE");
					break;
				case "5":
					userState.setUtilityCompanyID(ProductConstantsInterface.PTCL_EVO_PREPAID.toString());
					userState.setUtilityCompanyName("PTCL EVO PREPAID");
					break;
				case "6":
					userState.setUtilityCompanyID(ProductConstantsInterface.PTCL_EVO_POSTPAID.toString());
					userState.setUtilityCompanyName("PTCL EVO POSTPAID");
					break;
				case "7":
					userState.setUtilityCompanyID(ProductConstantsInterface.PTCL_DEFAULTER.toString());
					userState.setUtilityCompanyName("PTCL DEFAULTER");
					break;
				case "8":
					userState.setUtilityCompanyID(ProductConstantsInterface.QUEBEE_CONSUMER.toString());
					userState.setUtilityCompanyName("QUEBEE CONSUMER");
					break;
				case "9":
					userState.setUtilityCompanyID(ProductConstantsInterface.QUEBEE_DISTRIBUTOR.toString());
					userState.setUtilityCompanyName("QUEBEE DISTRIBUTOR");
					break;

			}
		}
		if(logger.isDebugEnabled()){
			logger.debug("UssdValidator.validateUtilityPTCL End");
		}
		return retVal;
	}

	public String validateUtilityMobile(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, UserState userState) {
		if(logger.isDebugEnabled()){
			logger.debug("UssdValidator.validateUtilityMobile starts");
		}
		String retVal=null;
		if(ThreadLocalAppUser.getAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER.longValue())
		{
			if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("1")){
				userState.setUtilityCompanyID(InternetCompanyEnum.ZONG_POSTPAID.getValue());

			}else if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("2")){
				userState.setUtilityCompanyID(InternetCompanyEnum.ZONG_PREPAID.getValue());

			}
		}
		else // customer case
		{
			if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("1")){
				userState.setUtilityCompanyID(InternetCompanyEnum.ZONG_POSTPAID_BILL.getValue());

			}else if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("2")){
				userState.setUtilityCompanyID(InternetCompanyEnum.ZONG_PREPAID_BILL.getValue());

			}
		}
		userState.setUtilityCompanyName("Mobile");

		if(logger.isDebugEnabled()){
			logger.debug("UssdValidator.validateUtilityMobile End");
		}
		return retVal;
	}
	public String validateUtilityGas(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, UserState userState) {
		if(logger.isDebugEnabled()){
			logger.debug("UssdValidator.validateUtilityGas starts");
		}
		String retVal=null;
		if(ThreadLocalAppUser.getAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER.longValue())
		{
			if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("1")){
				userState.setUtilityCompanyID(UtilityCompanyEnum.SSGC.getValue());
				userState.setUtilityCompanyName("SSGC");
			}else if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("2")){
				userState.setUtilityCompanyID(UtilityCompanyEnum.SNGPL.getValue());
				userState.setUtilityCompanyName("SNGPL");
			}
		}
		else
		{
			if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("1")){
				userState.setUtilityCompanyID(UtilityCompanyEnum.SSGC.getValue());
				userState.setUtilityCompanyName("SSGC");
			}else if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("2")){
				userState.setUtilityCompanyID(UtilityCompanyEnum.SNGPL.getValue());
				userState.setUtilityCompanyName("SNGPL");
			}
		}
		if(logger.isDebugEnabled()){
			logger.debug("UssdValidator.validateUtilityGas End");
		}
		return retVal;
	}
	public String validateUtilityInternet(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, UserState userState) {
		if(logger.isDebugEnabled()){
			logger.debug("UssdValidator.validateUtilityInternet starts");
		}
		String retVal=null;

			if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("1")){
				if(ThreadLocalAppUser.getAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER){
					userState.setUtilityCompanyID(InternetCompanyEnum.WATEEN_BILL.getValue());
				}else{
					userState.setUtilityCompanyID(InternetCompanyEnum.WATEEN.getValue());
				}

			}else if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("2")){
				if(ThreadLocalAppUser.getAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER){
					userState.setUtilityCompanyID(InternetCompanyEnum.WITRIBE_BILL.getValue());
				}else{
					userState.setUtilityCompanyID(InternetCompanyEnum.WITRIBE.getValue());
				}
				//userState.setUtilityCompanyName("WI-TRIBE");
			}
			userState.setUtilityCompanyName("Internet");
		if(logger.isDebugEnabled()){
			logger.debug("UssdValidator.validateUtilityInternet End");
		}
		return retVal;
	}
	public String validateUtilityElectricity(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, UserState userState) {
		if(logger.isDebugEnabled()){
			logger.debug("UssdValidator.validateUtilityElectricity starts");
		}
		String retVal=null;
		if(ThreadLocalAppUser.getAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER.longValue())
		{
			if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("1")){
				userState.setUtilityCompanyID(UtilityCompanyEnum.KESC.getValue());
				userState.setUtilityCompanyName("KESC");
			}else if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("2")){
				userState.setUtilityCompanyID(UtilityCompanyEnum.LESCO.getValue());
				userState.setUtilityCompanyName("LESCO");
			}else if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("3")){
				userState.setUtilityCompanyID(UtilityCompanyEnum.HESCO.getValue());
				userState.setUtilityCompanyName("HESCO");
			}else if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("4")){
				userState.setUtilityCompanyID(UtilityCompanyEnum.MEPCO.getValue());
				userState.setUtilityCompanyName("MEPCO");
			}else if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("5")){
				userState.setUtilityCompanyID(UtilityCompanyEnum.QESCO.getValue());
				userState.setUtilityCompanyName("QESCO");
			}else if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("6")){
				userState.setUtilityCompanyID(UtilityCompanyEnum.SEPCO.getValue());
				userState.setUtilityCompanyName("SEPCO");
			}else if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("7")){
				userState.setUtilityCompanyID(UtilityCompanyEnum.FESCO.getValue());
				userState.setUtilityCompanyName("FESCO");
			}
			else if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("8")){
				userState.setUtilityCompanyID(UtilityCompanyEnum.GEPCO.getValue());
				userState.setUtilityCompanyName("GEPCO");
			}
			else if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("9")){
				userState.setUtilityCompanyID(UtilityCompanyEnum.PESCO.getValue());
				userState.setUtilityCompanyName("PESCO");
			}
		}

		else // customer case
		{
			logger.info("[UssdValidator] Validating Bill Menus for Customer");
			if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("1")){
				userState.setUtilityCompanyID(UtilityCompanyEnum.KESC.getValue());
				userState.setUtilityCompanyName("KESC");
			}else if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("2")){
				userState.setUtilityCompanyID(UtilityCompanyEnum.LESCO.getValue());
				userState.setUtilityCompanyName("LESCO");
			}else if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("3")){
				userState.setUtilityCompanyID(UtilityCompanyEnum.HESCO.getValue());
				userState.setUtilityCompanyName("HESCO");
			}else if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("4")){
				userState.setUtilityCompanyID(UtilityCompanyEnum.MEPCO.getValue());
				userState.setUtilityCompanyName("MEPCO");
			}else if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("5")){
				userState.setUtilityCompanyID(UtilityCompanyEnum.QESCO.getValue());
				userState.setUtilityCompanyName("QESCO");
			}else if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("6")){
				userState.setUtilityCompanyID(UtilityCompanyEnum.SEPCO.getValue());
				userState.setUtilityCompanyName("SEPCO");
			}else if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("7")){
				userState.setUtilityCompanyID(UtilityCompanyEnum.FESCO.getValue());
				userState.setUtilityCompanyName("FESCO");
			}
			else if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("8")){
				userState.setUtilityCompanyID(UtilityCompanyEnum.GEPCO.getValue());
				userState.setUtilityCompanyName("GEPCO");
			}
			else if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("9")){
				userState.setUtilityCompanyID(UtilityCompanyEnum.PESCO.getValue());
				userState.setUtilityCompanyName("PESCO");
			}
			else if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("10")){
				userState.setUtilityCompanyID(UtilityCompanyEnum.IESCO.getValue());
				userState.setUtilityCompanyName("IESCO");
			}
		}

		if(logger.isDebugEnabled()){
			logger.debug("UssdValidator.validateUtilityElectricity End");
		}
		return retVal;
	}

	public String validateUtilityWater(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, UserState userState) {
		if(logger.isDebugEnabled()){
			logger.debug("UssdValidator.validateUtilityWater starts");
		}
		String retVal=null;
		if(ThreadLocalAppUser.getAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER.longValue())
		{
			if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("1")){
				userState.setUtilityCompanyID(UtilityCompanyEnum.MWASA.getValue());
				userState.setUtilityCompanyName("MWASA");
			}else if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("2")){
				userState.setUtilityCompanyID(UtilityCompanyEnum.FWASA.getValue());
				userState.setUtilityCompanyName("FWASA");
			}else if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("3")){
				userState.setUtilityCompanyID(UtilityCompanyEnum.GWASA.getValue());
				userState.setUtilityCompanyName("GWASA");
			}else if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("4")){
				userState.setUtilityCompanyID(UtilityCompanyEnum.RWASA.getValue());
				userState.setUtilityCompanyName("RWASA");
			}else if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("5")){
				userState.setUtilityCompanyID(UtilityCompanyEnum.LWASA.getValue());
				userState.setUtilityCompanyName("LWASA");
			}
		}
		else
		{
			// For Customer:
			if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("1")){
				userState.setUtilityCompanyID(UtilityCompanyEnum.BWASA.getValue());
				userState.setUtilityCompanyName("BWASA");
			}else if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("2")){
				userState.setUtilityCompanyID(UtilityCompanyEnum.FWASA.getValue());
				userState.setUtilityCompanyName("FWASA");
			}else if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("3")){
				userState.setUtilityCompanyID(UtilityCompanyEnum.GWASA.getValue());
				userState.setUtilityCompanyName("GWASA");
			}else if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("4")){
				userState.setUtilityCompanyID(UtilityCompanyEnum.KWSB.getValue());
				userState.setUtilityCompanyName("KWSB");
			}
			else if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("5")){
				userState.setUtilityCompanyID(UtilityCompanyEnum.LWASA.getValue());
				userState.setUtilityCompanyName("LWASA");
			}
			else if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("6")){
				userState.setUtilityCompanyID(UtilityCompanyEnum.MWASA.getValue());
				userState.setUtilityCompanyName("MWASA");
			}else if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("7")){
				userState.setUtilityCompanyID(UtilityCompanyEnum.RWASA.getValue());
				userState.setUtilityCompanyName("RWASA");
			}
		}
		if(logger.isDebugEnabled()){
			logger.debug("UssdValidator.validateUtilityWater End");
		}
		return retVal;
	}
	private void validatePrepaid(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, UserState userState) {
		if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("1")){
			userState.setUtilityCompanyID(ProductConstantsInterface.WARID_PREPAID.toString());
			userState.setUtilityCompanyName("WARID PREPAID");
		}
		else if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("2")){
			userState.setUtilityCompanyID(ProductConstantsInterface.MOBILINK_PREPAID.toString());
			userState.setUtilityCompanyName("MOBLINK PREPAID");
		}
		else if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("3")){
			userState.setUtilityCompanyID(ProductConstantsInterface.TELENOR_PREPAID.toString());
			userState.setUtilityCompanyName("TELENOR PREPAID");
		}
		else if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("4")){
			userState.setUtilityCompanyID(ProductConstantsInterface.ZONG_PREPAID.toString());
			userState.setUtilityCompanyName("ZONG PREPAID");
		}
		else if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("5")){
			userState.setUtilityCompanyID(ProductConstantsInterface.UFONE_PREPAID.toString());
			userState.setUtilityCompanyName("UFONE PREPAID");
		}
		userState.setPrepaidLoad(true);

	}

	private void validatePostpaid(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, UserState userState) {
		if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("1")){
			userState.setUtilityCompanyID(ProductConstantsInterface.WARID_POSTPAID.toString());
			userState.setUtilityCompanyName("WARID POSTPAID");
		}
		else if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("2")){
			userState.setUtilityCompanyID(ProductConstantsInterface.MOBILINK_POSTPAID.toString());
			userState.setUtilityCompanyName("MOBILINK POSTPAID");
		}
		else if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("3")){
			userState.setUtilityCompanyID(ProductConstantsInterface.TELENOR_POSTPAID.toString());
			userState.setUtilityCompanyName("TELENOR POSTPAID");
		}
		else if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("4")){
			userState.setUtilityCompanyID(ProductConstantsInterface.ZONG_POSTPAID.toString());
			userState.setUtilityCompanyName("ZONG POSTPAID");
		}
		else if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("5")){
			userState.setUtilityCompanyID(ProductConstantsInterface.UFONE_POSTPAID.toString());
			userState.setUtilityCompanyName("UFONE POSTPAID");
		}
	}

	private void validatePaymentMode(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, UserState userState)
	{
		if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("1")){
			userState.setPaymentMode("0");
		}
		else if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("2")){
			userState.setPaymentMode("1");
		}
	}
	public String validateUtilityMunicipal(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, UserState userState) {
		if(logger.isDebugEnabled()){
			logger.debug("UssdValidator.validateUtilityMunicipal starts");
		}
		String retVal=null;
		if(ThreadLocalAppUser.getAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER.longValue())
		{
			if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("1")){
				userState.setUtilityCompanyID(UtilityCompanyEnum.CDGK.getValue());
				userState.setUtilityCompanyName("CDGK");
			}
		}
		else
		{
			if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("1")){
				userState.setUtilityCompanyID(UtilityCompanyEnum.CDGK.getValue());
				userState.setUtilityCompanyName("CDGK");
			}
		}

		if(logger.isDebugEnabled()){
			logger.debug("UssdValidator.validateUtilityMunicipal End");
		}
		return retVal;
	}
	private void validateGovPayments(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, UserState userState) {
		if(ThreadLocalAppUser.getAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()) {
			if (i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("1")) {
				userState.setProductID(ProductConstantsInterface.VRG_CUSTOMER_CHALLAN_PAYMENT);
				userState.setGovPaymentType("ITP EChallan");
			} else if (i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("2")) {
				userState.setProductID(ProductConstantsInterface.CUSTOMER_KP_CHALLAN_COLLECTION);
				userState.setGovPaymentType("KPK Challan");
			}
			else if (i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("2")) {
				userState.setProductID(ProductConstantsInterface.CUSTOMER_VALLENCIA_COLLECTION);
				userState.setGovPaymentType("VALENCIA Challan");
			}

			else if (i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("3")) {
				userState.setProductID(ProductConstantsInterface.LICENSE_FEE_COLLECTION);
				userState.setGovPaymentType("Driving License Fee");
			} else if (i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("4")) {
				userState.setProductID(ProductConstantsInterface.CUSTOMER_ET_COLLECTION);
				userState.setGovPaymentType("Excise and Taxation Fee");
			}
		}
		else{
			if (i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("1")) {
				userState.setProductID(ProductConstantsInterface.VRG_CHALLAN_PAYMENT);
				userState.setGovPaymentType("ITP EChallan");
			} else if (i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("2")) {
				userState.setProductID(ProductConstantsInterface.AGENT_KP_CHALLAN_COLLECTION);
				userState.setGovPaymentType("KPK Challan");
			}
			else if (i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("2")) {
				userState.setProductID(ProductConstantsInterface.AGENT_VALLENCIA_COLLECTION);
				userState.setGovPaymentType("VALLENCIA Challan");
			}
			else if (i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("3")) {
				userState.setProductID(ProductConstantsInterface.AGENT_LICENSE_FEE_COLLECTION);
				userState.setGovPaymentType("Driving License Fee");
			} else if (i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("4")) {
				userState.setProductID(ProductConstantsInterface.AGENT_ET_COLLECTION);
				userState.setGovPaymentType("Excise and Taxation Fee");
			}

		}
	}
	public String validateCDAmount(USSDInputDTO ussdInputDTO, UserState userState, CommandManager commandManager, CommonCommandManager commonCommandManager) {
		//TODO: validation required for Amount of Cash Deposit
		if(logger.isDebugEnabled()){
			logger.debug("UssdValidator.validateCDAmount starts");
		}
		String retVal=null;
		String response=null;
		if(StringUtil.isNumeric(ussdInputDTO.getMessege())){
			Double amount = Double.parseDouble(ussdInputDTO.getMessege());
			ProductModel productModel = new ProductModel();
			productModel.setPrimaryKey(50002L);
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(productModel);
			try
			{
				baseWrapper = commonCommandManager.loadProduct(baseWrapper);
			}
			catch(Exception e)
			{
				e.printStackTrace();//e.printStackTrace();
				response = MessageUtil.getMessage("MINI.GeneralError");
			}

			productModel = (ProductModel) baseWrapper.getBasePersistableModel();
			logger.info("@ Previous Product Limit Check Was Here for Product "+ productModel.getName());
				USSDXMLUtils ussdxmlUtils=new USSDXMLUtils();
				//Maqsood - Calling first step of cash in command
				//TODO: Rehan to move this code segment to executeCommand
				baseWrapper = new BaseWrapperImpl();
				baseWrapper.putObject( CommandFieldConstants.KEY_PROD_ID, 50002L) ;
				baseWrapper.putObject( CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.USSD ) ;
				baseWrapper.putObject( CommandFieldConstants.KEY_CSCD, userState.getMsisdn() ) ;
				baseWrapper.putObject( CommandFieldConstants.KEY_MOB_NO, userState.getMsisdn() ) ;
				baseWrapper.putObject( CommandFieldConstants.KEY_TX_AMOUNT, ussdInputDTO.getMessege() ) ;
				baseWrapper.putObject( CommandFieldConstants.KEY_CNIC, userState.getWalkinCustomerCNIC() ) ;
				baseWrapper.putObject( CommandFieldConstants.KEY_CD_CUSTOMER_MOBILE, userState.getWalkinCustomerMSISDN() ) ;
				try
				{
					logger.info("[UssdValidator.validateCDAmount] Cash Deposit - Going to execute Command flow.  Customer CNIC: " + userState.getCustomerCNIC() + " Customer Mobile: " + userState.getMsisdn() + " Agent AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());

					response = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CASH_DEPOSIT_INFO_COMMAND);
	//				ussdxmlUtils.populateCDCustomerDetail(response, userState);
					if(null!=response){
						String TXAM= MiniXMLUtil.getTagTextValue(response, "//params/param[@name='TXAM']");
						if(null!=TXAM && !"".equals(TXAM)){
							userState.setTXAM(TXAM);
						}
						String CAMT= MiniXMLUtil.getTagTextValue(response, "//params/param[@name='CAMT']");
						if(null!=CAMT && !"".equals(CAMT)){
							userState.setCAMT(CAMT);
						}
						String TAMT= MiniXMLUtil.getTagTextValue(response, "//params/param[@name='TAMT']");
						if(null!=TAMT && !"".equals(TAMT)){
							userState.setTAMT(TAMT);
						}
						String TPAM= MiniXMLUtil.getTagTextValue(response, "//params/param[@name='TPAM']");
						if(null!=TPAM && !"".equals(TPAM)){
							userState.setTPAM(TPAM);
						}
						String BAMT= ussdInputDTO.getMessege();
						if(null!=BAMT && !"".equals(BAMT)){
							userState.setBAMT(BAMT);
						}
						String A1CAMT= MiniXMLUtil.getTagTextValue(response, "//params/param[@name='A1CAMT']");
						if(null!=A1CAMT && !"".equals(A1CAMT)){
							userState.setA1CAMT(A1CAMT);
						}
						String A2CAMT= MiniXMLUtil.getTagTextValue(response, "//params/param[@name='A2CAMT']");
						if(null!=A2CAMT && !"".equals(A2CAMT)){
							userState.setA2CAMT(A2CAMT);
						}

					}
				}
				catch(Exception e)
				{
					e.printStackTrace();//e.printStackTrace();
					response = MessageUtil.getMessage("MINI.GeneralError");
				}


				//Maqsood - First Step of Cash In Ends
				userState.setAmount( Double.parseDouble(ussdInputDTO.getMessege()));
				ussdInputDTO.setMessege("-1");

		}else{
			retVal = "Invalid Amount\n";
		}

		if(logger.isDebugEnabled()){
			logger.debug("UssdValidator.validateCDAmount End");
		}
		return retVal;
	}

	public String validatePin(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, UserState userState, CommonCommandManager commonCommandManager, ActionLogManager actionLogManager,I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO) {
		//TODO: validation required for PIN
		if(logger.isDebugEnabled()){
			logger.debug("UssdValidator.validatePin starts");
		}

		logger.info("[UssdValidator.validatePin] Going to validate Pin. LoggedIn AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
		//this.insertActionLog(userState, actionLogManager);
		String retVal=null;
//		if(validatePin(ussdInputDTO)){
			String pin = MfsWebUtil.encryptPin(i8SBSwitchControllerRequestVO.getuSSDRequestString());
			userState.setPin(pin);
			try
			{
				Boolean notError = this.validatePin(i8SBSwitchControllerRequestVO.getuSSDRequestString()) && this.verifyPIN(userState, commonCommandManager,i8SBSwitchControllerResponseVO);
				if(!notError)
				{
					int invalidPinAttempts=userState.getInvalidPinAttempts();
					if(invalidPinAttempts==2){
						UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
						userDeviceAccountsModel.setUserDeviceAccountsId(ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserDeviceAccountsId());
						BaseWrapper bWrapper = new BaseWrapperImpl();
						bWrapper.setBasePersistableModel(userDeviceAccountsModel);

						bWrapper = commonCommandManager.loadUserDeviceAccount(bWrapper);
						if(null != bWrapper.getBasePersistableModel())
						{
							userDeviceAccountsModel = (UserDeviceAccountsModel) bWrapper.getBasePersistableModel();
						}
						userDeviceAccountsModel.setCredentialsExpired(true);
						BaseWrapper baseWrapper = new BaseWrapperImpl();
						baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
						commonCommandManager.updateUserDeviceAccounts(baseWrapper);
						if(ThreadLocalAppUser.getAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue())
						{
							UserDeviceAccountsModel udModel = new UserDeviceAccountsModel();
							udModel.setUserId(userDeviceAccountsModel.getUserId());
							udModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);
							SearchBaseWrapper sBaseWrapper = new SearchBaseWrapperImpl();
							sBaseWrapper.setBasePersistableModel(udModel);
							commonCommandManager.loadUserDeviceAccounts(sBaseWrapper);
							if(null != sBaseWrapper.getCustomList() && null != sBaseWrapper.getCustomList().getResultsetList())
							{
								udModel = (UserDeviceAccountsModel) sBaseWrapper.getCustomList().getResultsetList().get(0);
							}
							udModel.setCredentialsExpired(true);
							baseWrapper = new BaseWrapperImpl();
							baseWrapper.setBasePersistableModel(udModel);
							commonCommandManager.updateUserDeviceAccounts(baseWrapper);
						}
						retVal= "PIN retry limit exhausted, Account has been blocked. Call 03112221333\n";
					}else{
						userState.setInvalidPinAttempts(invalidPinAttempts + 1);
						retVal= "Incorrect PIN, Please retry\n";
					}


				}
			}
			catch(Exception e)
			{
//				e.printStackTrace();//e.printStackTrace();

				logger.error("[UssdValidator.validatePin] Incorrect Pin Entered. LoggedIn AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Exception:" + e.getMessage());
				retVal= "Incorrect PIN, Please retry\n";
			}
			if(retVal==null){
				userState.setPin(pin);
				i8SBSwitchControllerRequestVO.setuSSDRequestString("-1");
			}else{
				userState.setPin(null);
			}
		/*}else{
			int invalidPinAttempts=userState.getInvalidPinAttempts();
			if(invalidPinAttempts==2){
				retVal= "PIN retry limit exhausted, Account has been blocked. Call 03112221333\n";
			}else{
				userState.setInvalidPinAttempts(invalidPinAttempts + 1);
				retVal = "Incorrect PIN, Please retry\n";
			}
		}*/

			if(logger.isDebugEnabled()){
				logger.debug("UssdValidator.validatePin End");
			}
		return retVal;
	}

	private void validateBalanceCheck(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, UserState userState) {
		if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("1")){
			userState.setAccountTypeId("0");
		}
		else if(i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("2")){
			userState.setAccountTypeId("1");
		}
	}

	private boolean validatePin(String plainPIN) {
		return plainPIN!=null && !"".equals(plainPIN)&& StringUtil.isInteger(plainPIN) && plainPIN.length()==4;
	}
	public String validateNewPin(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, UserState userState) {
		if(logger.isDebugEnabled()){
			logger.debug("UssdValidator.validateNewPin starts");
		}
		String retVal=null;
		if(validatePin(i8SBSwitchControllerRequestVO.getuSSDRequestString())){
			String newPin = MfsWebUtil.encryptPin(i8SBSwitchControllerRequestVO.getuSSDRequestString());
			userState.setNewPin(newPin);
			i8SBSwitchControllerRequestVO.setuSSDRequestString("-1");
		}else{
			retVal = "Incorrect PIN, Please retry\n";
		}
		if(logger.isDebugEnabled()){
			logger.debug("UssdValidator.validateNewPin End");
		}
		return retVal;
	}
	public String validateConfirmNewPin(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, UserState userState) {
		if(logger.isDebugEnabled()){
			logger.debug("UssdValidator.validateConfirmNewPin Start");
		}
		String retVal=null;
		if(validatePin(i8SBSwitchControllerRequestVO.getuSSDRequestString())){
			String confirmNewPin = MfsWebUtil.encryptPin(i8SBSwitchControllerRequestVO.getuSSDRequestString());
			if(confirmNewPin.equals(userState.getNewPin())){
				userState.setConfirmNewPin(confirmNewPin);
				i8SBSwitchControllerRequestVO.setuSSDRequestString("-1");
			}else{
				retVal = "Pin does not match\n";
			}
		}else{
			retVal = "Incorrect PIN, Please retry\n";
		}
		if(logger.isDebugEnabled()){
			logger.debug("UssdValidator.validateConfirmNewPin End");
		}
		return retVal;
	}

     public String validateLoginPin( I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, UserState userState)
     {
         logger.info("[UssdValidator] going to validate Login PIN");
         String retVal=null;
          if(i8SBSwitchControllerRequestVO.getuSSDRequestString()  != null && !i8SBSwitchControllerRequestVO.getuSSDRequestString().equals(""))
          {
              String loginPIN = MfsWebUtil.encryptPin(i8SBSwitchControllerRequestVO.getuSSDRequestString());
              UserDeviceAccountsModel userDeviceAccountsModel = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();

              if(userDeviceAccountsModel != null)
			  {
			  	String actualPIN = userDeviceAccountsModel.getPin();
				  if( actualPIN!= null && actualPIN .equals(loginPIN))
				  {
					  logger.info("[UssdValidator] Login PIN validated");
					  userState.setLoginPIN(loginPIN);
					  i8SBSwitchControllerRequestVO.setuSSDRequestString("-1");
				  }
				  else
					  retVal = "Incorrect Login PIN, Please retry\n";
			  }
			  else
				  retVal = "Incorrect Login PIN, Please retry\n";

          }
          else{
             retVal = "Incorrect Login PIN, Please retry\n";
         }
         if(logger.isDebugEnabled()){
             logger.debug("UssdValidator.validateConfirmNewPin End");
         }
         return retVal;

     }

    public String validateChallanID( I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, UserState userState)
    {
        if(logger.isDebugEnabled()){
            logger.debug("UssdValidator.validateConfirmNewPin Start");
        }
        String retVal=null;
        if(i8SBSwitchControllerRequestVO.getuSSDRequestString()  != null && !i8SBSwitchControllerRequestVO.getuSSDRequestString().equals(""))
        {
            userState.setChallanID(i8SBSwitchControllerRequestVO.getuSSDRequestString());
            i8SBSwitchControllerRequestVO.setuSSDRequestString("-1");
        }
        else{
            retVal = "Incorrect ChallanID, Please retry\n";
        }
        if(logger.isDebugEnabled()){
            logger.debug("UssdValidator.validateConfirmNewPin End");
        }
        return retVal;

    }

    public String validateReceiverAccountNo(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, UserState userState)
    {

        String retVal=null;
        if(i8SBSwitchControllerRequestVO.getuSSDRequestString()  != null && !i8SBSwitchControllerRequestVO.getuSSDRequestString().equals(""))
        {
            userState.setReceiverAccountNo(i8SBSwitchControllerRequestVO.getuSSDRequestString());
            i8SBSwitchControllerRequestVO.setuSSDRequestString("-1");
        }
        else{
            retVal = "Incorrect AccountID, Please retry\n";
        }

        return retVal;

    }

    public String validateCWAmount(USSDInputDTO ussdInputDTO, UserState userState, CommonCommandManager commonCommandManager) {
		if(logger.isDebugEnabled()){
			logger.debug("UssdValidator.validateCWAmount Start");
		}

		logger.info("[UssdValidator.validateCWAmount] Validating Amount. Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());

		String retVal=null;
		//TODO: validation required for Amount of Cash Withdrawal
		if(StringUtil.isNumeric(ussdInputDTO.getMessege())){
			Double amount = Double.parseDouble(ussdInputDTO.getMessege());
			ProductModel productModel = new ProductModel();
			productModel.setPrimaryKey(50006L);
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(productModel);
			try
			{
				baseWrapper = commonCommandManager.loadProduct(baseWrapper);
			}
			catch(Exception e)
			{
//				e.printStackTrace();//e.printStackTrace();
				logger.error("[UssdValidator.validateCWAmount] Exception occured. Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Exception Msg:" + e.getMessage());
				retVal = MessageUtil.getMessage("MINI.GeneralError");
			}
		    productModel = (ProductModel) baseWrapper.getBasePersistableModel();
		    logger.info("@ Previous Product Limit Check Was Here for Product "+ productModel.getName());
			userState.setAmount(Double.parseDouble(ussdInputDTO.getMessege()));

				try {
					CustomerModel customerModel = new CustomerModel();
					customerModel.setCustomerId(ThreadLocalAppUser.getAppUserModel().getCustomerId());
					baseWrapper.setBasePersistableModel(customerModel);
					baseWrapper = commonCommandManager.loadCustomer(baseWrapper);
					customerModel = (CustomerModel) baseWrapper.getBasePersistableModel();
					WorkFlowWrapper wrapper = new WorkFlowWrapperImpl();
					wrapper.setProductModel(productModel);
					//			commonCommandManager.
					/*long segmentID=ThreadLocalAppUser.getAppUserModel().getCustomerIdCustomerModel().getSegmentId();
					
					SegmentModel segmentModel=new SegmentModel();
					segmentModel.setSegmentId(segmentID);*/
					wrapper.setSegmentModel(customerModel.getSegmentIdSegmentModel());
					TransactionModel transactionModel=new TransactionModel();
					transactionModel.setTransactionAmount(userState.getAmount());
					wrapper.setTransactionModel(transactionModel);
					CommissionAmountsHolder commissionAmountHolder = new CommissionAmountsHolder();
					commissionAmountHolder.setTotalAmount(userState.getAmount());
					commissionAmountHolder.setTransactionProcessingAmount(0D);
					commissionAmountHolder.setTransactionAmount(userState.getAmount());
					wrapper.setCommissionAmountsHolder(commissionAmountHolder);
					CommissionWrapper commissionWrapper=commonCommandManager.calculateCommission(wrapper);
					CommissionAmountsHolder commissionAmountsHolder=(CommissionAmountsHolder)commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
					userState.setTPAM(Formatter.formatDouble(commissionAmountsHolder.getTransactionProcessingAmount()));

				} catch (Exception e) {
					// TODO: handle exception
//					e.printStackTrace();
					logger.error("[UssdValidator.validateCWAmount] Exception occured. Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Exception Msg:" + e.getMessage());

				}
				ussdInputDTO.setMessege("-1");

		}else{
			retVal = "Invalid Amount\n";
		}
		if(logger.isDebugEnabled()){
			logger.debug("UssdValidator.validateCWAmount End");
		}
		return retVal;
	}
	public String validateCustomerMobile(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, UserState userState, CommonCommandManager commonCommandManager) {
		if(logger.isDebugEnabled()){
			logger.debug("UssdValidator.validateCustomerMobile Start");
		}

		String retVal=null;
		//TODO: validation required for Customer MSISDN	
		logger.info("[UssdValidator.validateCustomerMobile] Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Mobile No:" + i8SBSwitchControllerRequestVO.getuSSDRequestString());
		if(validateMobile(i8SBSwitchControllerRequestVO.getuSSDRequestString())){

			if(CommandConstants.CASH_IN_COMMAND_CODE.equals(userState.getCommandCode())){
				logger.info("[UssdValidator.validateCustmerMobile] Validating Customer Mobile for CASH DEPOSIT "+i8SBSwitchControllerRequestVO.getuSSDRequestString());
			}else if(CommandConstants.P2P_COMMAND_CODE.equals(userState.getCommandCode())){
				logger.info("[UssdValidator.validateCustmerMobile] Validating Customer Mobile in P2P " + i8SBSwitchControllerRequestVO.getuSSDRequestString());
				if(i8SBSwitchControllerRequestVO.getuSSDRequestString().trim().equals(ThreadLocalAppUser.getAppUserModel().getMobileNo().trim()))
				{
					retVal = "Own account transfer is not allowed.\n";
					logger.info("[UssdValidator.validateCustmerMobile] Validating Customer Mobile in P2P " + i8SBSwitchControllerRequestVO.getuSSDRequestString() + ". Error: " + retVal);
					return retVal;
				}
			}
			i8SBSwitchControllerRequestVO.setuSSDRequestString(formatMobileNo(i8SBSwitchControllerRequestVO.getuSSDRequestString()));
			AppUserModel custModel = new AppUserModel();
			custModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
			custModel.setMobileNo(i8SBSwitchControllerRequestVO.getuSSDRequestString());
			SearchBaseWrapper sBaseWrapper = new SearchBaseWrapperImpl();
			sBaseWrapper.setBasePersistableModel(custModel);
			try
			{
				sBaseWrapper = commonCommandManager.searchAppUserByExample(sBaseWrapper);
			}
			catch(Exception e)
			{
//				e.printStackTrace();
				retVal = "You have entered an Incorrect Mobile Number\n";
				logger.error("[UssdValidator.validateCustmerMobile] Error occured in validation for Mobile:" + i8SBSwitchControllerRequestVO.getuSSDRequestString() + ". Exception:" + e.getMessage());
				return retVal;
			}
			if (null == sBaseWrapper.getBasePersistableModel())
			{
				retVal = "You have entered an Incorrect Mobile Number\n";
				logger.error("[UssdValidator.validateCustmerMobile] Error occured in validation for Mobile:" + i8SBSwitchControllerRequestVO.getuSSDRequestString() + ". Error:" + retVal);
			}
			else
			{
				UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
			try
			{
				custModel = (AppUserModel)sBaseWrapper.getBasePersistableModel();

				if(custModel.getAccountClosedUnsettled() || custModel.getAccountClosedSettled())
				{
					retVal = "Transaction cannot be processed.\nCustomer account is closed.\n";
					logger.error("[UssdValidator.validateCustmerMobile] Error occured in validation for Mobile:" + i8SBSwitchControllerRequestVO.getuSSDRequestString() + ". Error:" + retVal);
					return retVal;
				}

//				userDeviceAccountsModel.setAppUserId(custModel.getAppUserId());
//				userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.USSD);
//				SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
//				searchBaseWrapper.setBasePersistableModel(userDeviceAccountsModel);
//
//				searchBaseWrapper = commonCommandManager.loadUserDeviceAccounts(searchBaseWrapper);
//				if(null != searchBaseWrapper.getCustomList() && searchBaseWrapper.getCustomList().getResultsetList().size() > 0)
//				{
//
//					userDeviceAccountsModel = (UserDeviceAccountsModel) searchBaseWrapper.getCustomList().getResultsetList().get(0);
//					if(userDeviceAccountsModel.getAccountEnabled() == Boolean.FALSE)
//					{
//						retVal = "Transaction cannot be processed.\nCustomer account is deactivated.\n";
//						logger.error("[UssdValidator.validateCustmerMobile] Error occured in validation for Mobile:" + i8SBSwitchControllerRequestVO.getuSSDRequestString() + ". Error:" + retVal);
//						return retVal;
//					}
//					else if(userDeviceAccountsModel.getAccountLocked() == Boolean.TRUE)
//					{
//						retVal = "Transaction cannot be processed.\nCustomer account is locked.\n";
//						logger.error("[UssdValidator.validateCustmerMobile] Error occured in validation for Mobile:" + i8SBSwitchControllerRequestVO.getuSSDRequestString() + ". Error:" + retVal);
//						return retVal;
//					}
//					else if(userDeviceAccountsModel.getCredentialsExpired() == Boolean.TRUE)
//					{
//						retVal = "Transaction cannot be processed.\nCustomer account credentails are expired.\n";
//						logger.error("[UssdValidator.validateCustmerMobile] Error occured in validation for Mobile:" + i8SBSwitchControllerRequestVO.getuSSDRequestString() + ". Error:" + retVal);
//						return retVal;
//					}
//
//				}
//				else
//				{
//					retVal = "No Customer found against mobile number.\n";
//				}
			}
			catch(Exception e)
			{
//				e.printStackTrace();//e.printStackTrace();
				retVal = "No Customer found against mobile number.\n";
				logger.error("[UssdValidator.validateCustmerMobile] Error occured in validation for Mobile:" + i8SBSwitchControllerRequestVO.getuSSDRequestString() + ". Exception:" + e.getMessage());
			}
				userState.setMsisdn(custModel.getMobileNo());
				userState.setCustomerCNIC(custModel.getNic());
				userState.setCustomerName(custModel.getFirstName()+" "+custModel.getLastName());
				userState.setCustomerAppUserModel(custModel);
				i8SBSwitchControllerRequestVO.setuSSDRequestString("-1");

		}
		}else{
			retVal = "You have entered an Incorrect Mobile Number\n";
			logger.error("[UssdValidator.validateCustmerMobile] Error occured in validation for Mobile:" + i8SBSwitchControllerRequestVO.getuSSDRequestString() + ". Error:" + retVal);
		}
		if(logger.isDebugEnabled()){
			logger.debug("UssdValidator.validateCustomerMobile End");
		}
		return retVal;
	}

	String validateBillPaymentMobile(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, UserState userState) {
		logger.info("Validating BillPayment Mobile No");
		String retVal = null;
		if (validateMobile(i8SBSwitchControllerRequestVO.getuSSDRequestString())) {
			i8SBSwitchControllerRequestVO.setuSSDRequestString(formatMobileNo(i8SBSwitchControllerRequestVO.getuSSDRequestString()));
			userState.setMsisdn(i8SBSwitchControllerRequestVO.getuSSDRequestString());
            i8SBSwitchControllerRequestVO.setuSSDRequestString("-1");
		}
		else{
			   retVal = "InValid Mobile Number";
			}
			return retVal;
	   }
	public String validateAgentMobile(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, CommonCommandManager commonCommandManager, UserState userState, ActionLogManager actionLogManager, CommandManager commandManager) {
		if(logger.isDebugEnabled()){
			logger.debug("UssdValidator.validateAgentMobile Start");
		}
		String retVal=null;
		
		logger.info("[UssdValidator.validateAgentMobile] Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Validating Agent Mobile No:" + i8SBSwitchControllerRequestVO.getuSSDRequestString());
		if ( validateMobile(i8SBSwitchControllerRequestVO.getuSSDRequestString())) {

			// Maqsood Shahzad
			i8SBSwitchControllerRequestVO.setuSSDRequestString(formatMobileNo(i8SBSwitchControllerRequestVO.getuSSDRequestString()));
			AppUserModel retModel = new AppUserModel();
			retModel.setAppUserTypeId(UserTypeConstantsInterface.RETAILER);
			retModel.setMobileNo(i8SBSwitchControllerRequestVO.getuSSDRequestString());
			SearchBaseWrapper sBaseWrapper = new SearchBaseWrapperImpl();
			sBaseWrapper.setBasePersistableModel(retModel);
			try
			{
				sBaseWrapper = commonCommandManager.searchAppUserByExample(sBaseWrapper);
			}
			catch(Exception e)
			{
//				e.printStackTrace();
				retVal = "Invalid Agent Mobile Number\n";
				logger.error("[UssdValidator.validateAgentMobile] Exception occured. Invalid Agent Mobile. Logged In AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId()+ ". Mobile No:" + retModel.getMobileNo() + ". Exception Msg:"	+ e.getMessage());
				return retVal;
			}
			
			if (null == sBaseWrapper.getBasePersistableModel()) {
				retVal = "Invalid Agent Mobile Number\n";
				logger.error("[UssdValidator.validateAgentMobile] Invalid Agent Mobile. Logged In AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId()+ ". Mobile No:" + retModel.getMobileNo());
				
			} else {
				retModel = (AppUserModel)sBaseWrapper.getBasePersistableModel();
				
				//////////////////////////////////////////////////////////////////////////////////
			
				if(CommandConstants.AGENT_TO_AGENT_TRANSFER.equals(userState.getCommandCode()))
				{
					
					String response = "";
					if(StringUtil.isNumeric(i8SBSwitchControllerRequestVO.getuSSDRequestString())){
					
					Double amount = Double.parseDouble(i8SBSwitchControllerRequestVO.getuSSDRequestString());
					if(ThreadLocalAppUser.getAppUserModel().getMobileNo().equals(i8SBSwitchControllerRequestVO.getuSSDRequestString()))
					{
						retVal = "Funds transfer to own account is not allowed.\n";
						logger.error("[UssdValidator.validateAgentMobile] Funds transfer to own account is not allowed for Logged In AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId()+ ". Mobile No:" + retModel.getMobileNo());
						return retVal;
					}
					ProductModel productModel = new ProductModel();
					productModel.setPrimaryKey(50013L);
					BaseWrapper baseWrapper = new BaseWrapperImpl();
					baseWrapper.setBasePersistableModel(productModel);
					try
					{
						baseWrapper = commonCommandManager.loadProduct(baseWrapper);
					}
					catch(Exception e)
					{
						e.printStackTrace();//e.printStackTrace();
						response = MessageUtil.getMessage("MINI.GeneralError");
					}
				    productModel = (ProductModel) baseWrapper.getBasePersistableModel();
				   
				    logger.info("@ Previous Product Limit Check Was Here for Product "+ productModel.getName());					
					USSDXMLUtils ussdxmlUtils=new USSDXMLUtils();				
					try
					{
//						this.populateUserStateWithBankInfo(userState,commandManager);
						baseWrapper = new BaseWrapperImpl();
						baseWrapper = new BaseWrapperImpl();
						baseWrapper.putObject( CommandFieldConstants.KEY_PROD_ID, 50013L) ;
						baseWrapper.putObject( CommandFieldConstants.KEY_CURRENT_REQ_TIME, System.currentTimeMillis()) ;
						baseWrapper.putObject( CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.USSD ) ;
						baseWrapper.putObject( CommandFieldConstants.KEY_CSCD, i8SBSwitchControllerRequestVO.getuSSDRequestString() ) ;
						baseWrapper.putObject( CommandFieldConstants.KEY_MOB_NO, i8SBSwitchControllerRequestVO.getuSSDRequestString()) ;
//						baseWrapper.putObject( CommandFieldConstants.KEY_TX_AMOUNT, ussdInputDTO.getMessege() ) ;
						baseWrapper.putObject( CommandFieldConstants.KEY_BANK_ID, userState.getBankId() ) ;
						baseWrapper.putObject( CommandFieldConstants.KEY_ACC_ID, userState.getAccId() ) ;
						
						logger.info("[UssdValidator.validateMobile] Going to Execute Info Command for Logged in AppUserID:"+ ThreadLocalAppUser.getAppUserModel().getAppUserId() + 
								". Recipient Agent Mobile NO:" + i8SBSwitchControllerRequestVO.getuSSDRequestString());
						
						this.insertActionLog(userState, actionLogManager);
						response = commandManager.executeCommand(baseWrapper, CommandFieldConstants.AGENT_TO_AGENT_INFO);
//						userState.setTXAM(ussdInputDTO.getMessege());
						ussdxmlUtils.populateCDCustomerDetail(response, userState); // TODO: will have to write a separate method to populate p2p details 
						
					}
					catch(CommandException ce)
					{
						ce.printStackTrace();
						retVal = ce.getMessage();
					}
					catch(Exception e)
					{
						e.printStackTrace();//e.printStackTrace();
						retVal = MessageUtil.getMessage("MINI.GeneralError");
						 
					}
					
					
					
//					userState.setAmount(Double.parseDouble(ussdInputDTO.getMessege()));
//					ussdInputDTO.setMessege("-1");
					
				
				}else{
					retVal = "Invalid Amount\n";
				}
				
				if(logger.isDebugEnabled()){
					logger.debug("UssdValidator.validateATATAmount End");
				}

			}else if(CommandConstants.RSO_AGENT_TO_AGENT_TRANSFER.equals(userState.getCommandCode())) {
				
				String response = "";
				if(StringUtil.isNumeric(i8SBSwitchControllerRequestVO.getuSSDRequestString())){
				
				if(ThreadLocalAppUser.getAppUserModel().getMobileNo().equals(i8SBSwitchControllerRequestVO.getuSSDRequestString()))
				{
					retVal = "Funds transfer to own account is not allowed.\n";
					logger.error("[UssdValidator.validateAgentMobile] Funds transfer to own account is not allowed for Logged In AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId()+ ". Mobile No:" + retModel.getMobileNo());
					return retVal;
				}
				ProductModel productModel = new ProductModel();
				productModel.setPrimaryKey(50014L);
				BaseWrapper baseWrapper = new BaseWrapperImpl();
				baseWrapper.setBasePersistableModel(productModel);
				try
				{
					baseWrapper = commonCommandManager.loadProduct(baseWrapper);
				}
				catch(Exception e)
				{
					e.printStackTrace();//e.printStackTrace();
					response = MessageUtil.getMessage("MINI.GeneralError");
				}
			    productModel = (ProductModel) baseWrapper.getBasePersistableModel();
			    logger.info("@ Previous Product Limit Check Was Here for Product "+ productModel.getName());					

				
				USSDXMLUtils ussdxmlUtils=new USSDXMLUtils();				
				try
				{
					baseWrapper = new BaseWrapperImpl();
					baseWrapper = new BaseWrapperImpl();
					baseWrapper.putObject( CommandFieldConstants.KEY_PROD_ID, 50014L) ;
					baseWrapper.putObject( CommandFieldConstants.KEY_CURRENT_REQ_TIME, System.currentTimeMillis()) ;
					baseWrapper.putObject( CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.USSD ) ;
					baseWrapper.putObject( CommandFieldConstants.KEY_CSCD, i8SBSwitchControllerRequestVO.getuSSDRequestString()) ;
					baseWrapper.putObject( CommandFieldConstants.KEY_MOB_NO, i8SBSwitchControllerRequestVO.getuSSDRequestString() ) ;
					baseWrapper.putObject( CommandFieldConstants.KEY_BANK_ID, userState.getBankId() ) ;
					baseWrapper.putObject( CommandFieldConstants.KEY_ACC_ID, userState.getAccId() ) ;
					
					logger.info("[UssdValidator.validateMobile] Going to Execute Info Command for Logged in AppUserID:"+ ThreadLocalAppUser.getAppUserModel().getAppUserId() + 
							". Recipient Agent Mobile NO:" + i8SBSwitchControllerRequestVO.getuSSDRequestString());
					
					this.insertActionLog(userState, actionLogManager);
					response = commandManager.executeCommand(baseWrapper, CommandFieldConstants.RSO_AGENT_TO_AGENT_INFO);
					ussdxmlUtils.populateCDCustomerDetail(response, userState); // TODO: will have to write a separate method to populate Agent 2 Agent details 
					
				}
				catch(CommandException ce)
				{
					ce.printStackTrace();
					retVal = ce.getMessage();
				}
				catch(Exception e)
				{
					e.printStackTrace();//e.printStackTrace();
					retVal = MessageUtil.getMessage("MINI.GeneralError");
					 
				}
				
				
				
//				userState.setAmount(Double.parseDouble(ussdInputDTO.getMessege()));
//				ussdInputDTO.setMessege("-1");
				
			
			}else{
				retVal = "Invalid Amount\n";
			}
			
			if(logger.isDebugEnabled()){
				logger.debug("UssdValidator.validateATATAmount End");
			}
			}
				//////////////////////////////////////////////////////////////////////////////////
				
				else if(CommandConstants.CASH_OUT_COMMAND_CODE.equals(userState.getCommandCode()) || CommandConstants.CUSTOMER_RETAIL_PAYMENT.equals(userState.getCommandCode()))
				{
					if(retModel.getRetailerContactIdRetailerContactModel() != null && retModel.getRetailerContactIdRetailerContactModel().getRso()){
						retVal = "This transaction cannot be carried out by this agent.\n";
						return retVal;
					}
					
					UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
					userDeviceAccountsModel.setAppUserId(retModel.getAppUserId());
					userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.USSD);
					SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
					searchBaseWrapper.setBasePersistableModel(userDeviceAccountsModel);
					try
					{
						searchBaseWrapper = commonCommandManager.loadUserDeviceAccounts(searchBaseWrapper);
						if(null != searchBaseWrapper.getCustomList() && null != searchBaseWrapper.getCustomList().getResultsetList() && searchBaseWrapper.getCustomList().getResultsetList().size() > 0 )
						{
							userDeviceAccountsModel = (UserDeviceAccountsModel) searchBaseWrapper.getCustomList().getResultsetList().get(0); 
							if(userDeviceAccountsModel.getAccountLocked() || (!userDeviceAccountsModel.getAccountEnabled()) || userDeviceAccountsModel.getCredentialsExpired())
							{
								retVal = "This transaction cannot be carried out by this agent.\n";
								return retVal;
							}
							else
							{
								SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
								smartMoneyAccountModel.setRetailerContactId(retModel.getRetailerContactId());
								smartMoneyAccountModel.setDeleted(false);
								smartMoneyAccountModel.setActive(true);
								searchBaseWrapper = new SearchBaseWrapperImpl();
								searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
								searchBaseWrapper = commonCommandManager.loadSmartMoneyAccount(searchBaseWrapper);
								if(null != searchBaseWrapper.getCustomList() && null != searchBaseWrapper.getCustomList().getResultsetList() && searchBaseWrapper.getCustomList().getResultsetList().size() > 0 )
								{
									// Smart Money Account Model exists for this Agent
								}
								else
								{
									retVal = "This transaction cannot be carried out by this agent.\n";
									return retVal;
								}
							}
						}
						else
						{
							retVal = "This transaction cannot be carried out by this agent.\n";
							return retVal;
						}
					}
					catch(Exception e)
					{
//						e.printStackTrace();//e.printStackTrace();
						logger.error("[UssdValidator.validateAgentMobile] Expception occured. Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Exception message:" + e.getMessage());
						retVal = "This transaction cannot be carried out by this agent.\n";
						return retVal;
					}
				}
				userState.setMsisdn(i8SBSwitchControllerRequestVO.getuSSDRequestString());
				userState.setAgentName(retModel.getFirstName()+" "+retModel.getLastName());
				i8SBSwitchControllerRequestVO.setuSSDRequestString("-1");
			}
		} else {
			
			logger.error("[UssdValidator.validateAgentMobile] Invalid Agent Mobile Number Logged In AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId()+ ". Mobile No:" + i8SBSwitchControllerRequestVO.getuSSDRequestString());
			retVal = "Invalid Agent Mobile Number\n";
		}
		if(logger.isDebugEnabled()){
			logger.debug("UssdValidator.validateAgentMobile End");
		}
		return retVal;
	}


    public String validateTransferInAmount(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, UserState userState,CommonCommandManager commonCommandManager) {
        return validateAmount(i8SBSwitchControllerRequestVO,userState);
    }
    public String validateTransferOutAmount(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, UserState userState,CommonCommandManager commonCommandManager) {

        return validateAmount(i8SBSwitchControllerRequestVO,userState);
    }
	public String validateCNICAmount(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, UserState userState) {

        return validateAmount(i8SBSwitchControllerRequestVO,userState);
	}

	public String validateWalletAmount(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, UserState userState) {

        return validateAmount(i8SBSwitchControllerRequestVO,userState);
	}

    public String validateCoreAmount(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, UserState userState) {

      return validateAmount(i8SBSwitchControllerRequestVO,userState);
    }

    public String validateAmount(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, UserState userState) {

        String retVal = null;

        if(i8SBSwitchControllerRequestVO.getuSSDRequestString() != null &&  !i8SBSwitchControllerRequestVO.getuSSDRequestString().equals(""))
        {
            if(StringUtil.isNumeric(i8SBSwitchControllerRequestVO.getuSSDRequestString())) {

                userState.setAmount(Double.parseDouble(i8SBSwitchControllerRequestVO.getuSSDRequestString()));
                userState.setTransferAmount(i8SBSwitchControllerRequestVO.getuSSDRequestString());
                i8SBSwitchControllerRequestVO.setuSSDRequestString("-1");
            }
            else
                retVal = "Invalid  Amount Entered.";
        }
        else
            retVal = "Invalid Amount Entered.";
        return retVal;
    }
    public String validateCoreAccount(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, UserState userState)
    {
        if(logger.isDebugEnabled()){
            logger.debug("UssdValidator.validate CoreAccount No Start");
        }
        String retVal=null;
        if(i8SBSwitchControllerRequestVO.getuSSDRequestString()  != null && !i8SBSwitchControllerRequestVO.getuSSDRequestString().equals(""))
        {
            userState.setCoreAccountID(i8SBSwitchControllerRequestVO.getuSSDRequestString());
            i8SBSwitchControllerRequestVO.setuSSDRequestString("-1");
        }
        else{
            retVal = "Incorrect CoreAccountNo, Please retry\n";
        }
        if(logger.isDebugEnabled()){
            logger.debug("UssdValidator.validate CoreAccountNo End");
        }
        return retVal;
    }


        private Boolean verifyPIN(UserState userState, CommonCommandManager commonCommandManager, I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO)throws Exception
	{
//		VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();
		SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
		AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();
		if( ThreadLocalAppUser.getAppUserModel().getCustomerId() != null )
			sma.setCustomerId(appUserModel.getCustomerId());
		else
			sma.setRetailerContactId(appUserModel.getRetailerContactId());
		
		logger.debug("[UssdValidator] verifyPIN() AppUserID: " + appUserModel.getAppUserId());
		
		sma.setDeleted(false);
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel(sma);
		searchBaseWrapper = commonCommandManager.loadSmartMoneyAccount(searchBaseWrapper);
		if(null != searchBaseWrapper.getCustomList() && searchBaseWrapper.getCustomList().getResultsetList().size() > 0)
		{
			sma = (SmartMoneyAccountModel) searchBaseWrapper.getCustomList().getResultsetList().get(0);
		}
		AccountInfoModel accountInfoModel = new AccountInfoModel();
		if( ThreadLocalAppUser.getAppUserModel().getCustomerId() != null )
			accountInfoModel.setCustomerId(appUserModel.getCustomerId());
		else
			accountInfoModel.setCustomerId(appUserModel.getAppUserId());
		
		accountInfoModel.setAccountNick(sma.getName());
		accountInfoModel.setOldPin(userState.getPin());
		
		LogModel logModel = new LogModel();
		logModel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
		logModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());
//		logModel.setTransactionCodeId(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionCodeId());

		VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();
		
		veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
		veriflyBaseWrapper.setLogModel(logModel);

		veriflyBaseWrapper.setBasePersistableModel(sma);
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(sma);
		veriflyBaseWrapper.setBasePersistableModel(null);
		VeriflyManager veriflyManager = commonCommandManager.loadVeriflyManagerByAccountId(baseWrapper);
//		veriflyManager.verifyPIN(veriflyBaseWrapper);

		veriflyBaseWrapper = commonCommandManager.verifyVeriflyPin(veriflyManager, veriflyBaseWrapper);

		if(veriflyBaseWrapper.isErrorStatus())
		{
			i8SBSwitchControllerResponseVO.setuSSDResponseString(veriflyBaseWrapper.getErrorMessage());
		}
		/*if(veriflyBaseWrapper.isErrorStatus()){
			setThreadLocalAccountInfoModel(userState, veriflyBaseWrapper.getAccountInfoModel());
		}*/
		logger.info("[VerifyPIn] Response "+ veriflyBaseWrapper.getErrorMessage());
		if(logger.isDebugEnabled()){
			logger.debug("UssdValidator.verifyPIN End");
		}
		return veriflyBaseWrapper.isErrorStatus();
		
	}

	private void insertActionLog(UserState userState, ActionLogManager actionLogManager)
	{
		XStream xstream = new XStream();
		ActionLogModel actionLogModel = new ActionLogModel();
		
		actionLogModel.setInputXml(XMLUtil.replaceElementsUsingXPath(xstream.toXML(userState), XPathConstants.actionLogInputXMLLocationSteps));
		this.actionLogBeforeStart(actionLogModel, actionLogManager);
		if(actionLogModel.getActionLogId() != null)
		{
			userState.setActionLogId(actionLogModel.getActionLogId());
		}
	}
	
	private void actionLogBeforeStart(ActionLogModel actionLogModel, ActionLogManager actionLogManager)
	{
		
		
		actionLogModel.setActionStatusId(ActionStatusConstantsInterface.START_PROCESSING);
		actionLogModel.setStartTime(new Timestamp( new java.util.Date().getTime() ));
		actionLogModel = insertActionLogRequiresNewTransaction(actionLogModel, actionLogManager);
		if(actionLogModel.getActionLogId() != null)
		{
			ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
		}
	}
	
	private ActionLogModel insertActionLogRequiresNewTransaction(ActionLogModel actionLogModel, ActionLogManager actionLogManager)
	{
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(actionLogModel);
		try
		{
			baseWrapper = actionLogManager.createOrUpdateActionLogRequiresNewTransaction(baseWrapper);
			actionLogModel = (ActionLogModel)baseWrapper.getBasePersistableModel();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();

		}
		return actionLogModel;
	}
	
	public String validateCreditCardNumber(USSDInputDTO ussdInputDTO, UserState userState, CommandManager commandManager, CommonCommandManager commonCommandManager) {
		if(logger.isDebugEnabled()){
			logger.debug("UssdValidator.validateCreditCardNumber Start");
		}		
			String retVal=null;
			if(validateCreditCard(ussdInputDTO.getMessege())){
				userState.setCreditCardNumber(ussdInputDTO.getMessege()) ;
				ussdInputDTO.setMessege("-1");
			}else{
				retVal = "Invalid Credit Card Number\n";
			}
			
			if(logger.isDebugEnabled()){
				logger.debug("UssdValidator.validateCreditCardNumber End");
			}
			return retVal;
		}
	
	public boolean validateCreditCard(String creditCard) {
		if(logger.isDebugEnabled()){
			logger.debug("UssdValidator.validateCreditCardNumber starts");
		}
		boolean retVal;
		retVal=Pattern.matches("^[0-9+]{16}$", creditCard);
		if(logger.isDebugEnabled()){
			logger.debug("UssdValidator.validateCreditCardNumber End");
		}
		return retVal;
	}

	public String validateJCashAccount(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, UserState userState)
	{
		String retVal=null;
		if( i8SBSwitchControllerRequestVO.getuSSDRequestString() != null && !i8SBSwitchControllerRequestVO.getuSSDRequestString().equals("")){
			userState.setjCashAccountId(i8SBSwitchControllerRequestVO.getuSSDRequestString());
			i8SBSwitchControllerRequestVO.setuSSDRequestString("-1");
		}else{
			retVal = "Invalid Account No\n";
		}

		if(logger.isDebugEnabled()){
			logger.debug("UssdValidator.validateJCashAccount End");
		}
		return retVal;
	}

	/*private void setThreadLocalAccountInfoModel(UserState userState, AccountInfoModel accountInfoModel){
		String commandCode = userState.getCommandCode();
		
		StringBuffer accountInfoModelIds = new StringBuffer()
		.append(" [ AccountInfoModel.customerId " + accountInfoModel.getCustomerId() + " ")
		.append(" Account No: " + accountInfoModel.getAccountNo() + " ")
		.append(" Nick: " + accountInfoModel.getAccountNick() + " ] ");

		StringBuffer userCmdids = new StringBuffer()
		.append(" Transaction ID: " + userState.getTransactionID() + " ")
		.append(" [ CommandCode: " + commandCode + " ] ");
		
		logger.debug("[UssdValidator] Putting AccountInfoModel in ThreadLocal. " + accountInfoModelIds.toString() + " " + userCmdids.toString());
		
		if(commandCode.equals("CO")){//Cash Withdrawl - Customer
		
			ThreadLocalAccountInfo.setLoggedInCustomerAccountInfo(accountInfoModel);
		
		}else if(commandCode.equals("PC")){//Cash Withdrawl - Agent
		
			ThreadLocalAccountInfo.setLoggedInAgentAccountInfo(accountInfoModel);
		
		}if(commandCode.equals("CI")){//Cash Deposit
		
			ThreadLocalAccountInfo.setLoggedInAgentAccountInfo(accountInfoModel);
		
		}else if(commandCode.equals("CAC")){//Cash Withdrawl - Customer
		
			ThreadLocalAccountInfo.setLoggedInCustomerAccountInfo(accountInfoModel);
		
		}else if(commandCode.equals("P2P")){//Cash Withdrawl - Customer
		
			ThreadLocalAccountInfo.setLoggedInCustomerAccountInfo(accountInfoModel);
		
		}else if(commandCode.equals("ACT")){//Cash to Cash Leg 1
		
			ThreadLocalAccountInfo.setLoggedInAgentAccountInfo(accountInfoModel);
		
		}else if(commandCode.equals("UBPC")){//Customer Bill Payment
		
			ThreadLocalAccountInfo.setLoggedInCustomerAccountInfo(accountInfoModel);
		
		}else if(commandCode.equals("UBPA")){//Customer Bill Payment
		
			ThreadLocalAccountInfo.setLoggedInAgentAccountInfo(accountInfoModel);
		
		}
		else if(commandCode.equals("ATAT")){//Agent To Agent Transfer
			
				ThreadLocalAccountInfo.setLoggedInAgentAccountInfo(accountInfoModel);
		
		}else if(commandCode.equals("BCA")){//Check Balance - Agent
		
			ThreadLocalAccountInfo.setLoggedInAgentAccountInfo(accountInfoModel);
		
		}else if(commandCode.equals("BCC")){//Check Balance - Agent
		
			ThreadLocalAccountInfo.setLoggedInCustomerAccountInfo(accountInfoModel);
		
		}
		
	}*/
}
