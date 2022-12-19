package com.inov8.microbank.server.service.consumercommandmodule;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.integration.vo.NadraIntegrationVO;
import com.inov8.integration.webservice.vo.WebServiceVO;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.nadraVerisys.model.VerisysDataModel;
import com.inov8.microbank.server.service.commandmodule.BaseCommand;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;

import static com.inov8.microbank.common.util.XMLConstants.*;

//import com.inov8.integration.vo.CRMMessageVO;

/**
 * Created by Afridikht on 9/26/2016.
 */
public class CustomerSelfRegistrationBaseCommand extends BaseCommand{

	private final Log logger = LogFactory.getLog(CustomerSelfRegistrationBaseCommand.class);
	
    protected String mobileNo;
    protected String cnic;
    protected String name;
    protected String birthPlace;
    protected String motherMaidenName;
    protected String dob;
    protected String cnicStatus;
    protected String cnicExpiry;
	protected String cnicIssueDate;
	protected String emailAddress;
	protected String presentAddress;
    protected String accountType;
    protected String customerPhoto,cnicFrontPhoto;
    private String gender;
    
    protected BaseWrapper baseWrapper;
    private WebServiceVO webServiceVO;
    private boolean isConsumerApp = true;
    private String isDiscrepant;
	private String isUpgrade;
	private String responseMessage;
	private String isNewAccount;
	private Boolean isNewAccountOpening=Boolean.FALSE;

	private String customerMobileNetwork;


	@Override
    public void execute() throws CommandException
    {
    	
    	if(logger.isDebugEnabled())
		{
			logger.debug("Start of CustomerSelfRegistrationBaseCommand.execute()");
		}
    	
        try
		{
//			if(!CommonUtils.isValidNicExpiry(cnicExpiry))
//				throw new CommandException(MessageUtil.getMessage("customer.nic.expired"),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			webServiceVO = new WebServiceVO();
            if(isNewAccount.equals("1")) {
//                mobileNo = mobileNo + "/" + customerMobileNetwork;
				webServiceVO.setCustomerMobileNetwork(customerMobileNetwork);
                webServiceVO.setMobileNo(mobileNo);
                webServiceVO.setCnicNo(cnic);
                webServiceVO.setBirthPlace(birthPlace);
                webServiceVO.setMotherMaiden(motherMaidenName);
                webServiceVO.setDateOfBirth(dob);
                webServiceVO.setCnicStatus(cnicStatus);
                webServiceVO.setCnicExpiry(cnicExpiry);
                webServiceVO.setPresentAddress(presentAddress);
                webServiceVO.setAccountType(accountType);
//                webServiceVO.setCustomerPhoto(customerPhoto);
//                webServiceVO.setCnicFrontPhoto(cnicFrontPhoto);
                webServiceVO.setConsumerName(name);
//                webServiceVO.setGender(gender);
                webServiceVO.setReserved1("1");
				//Below parameter are set to change cnic issuance Date format change  for api dd-mm-yyyy to yyyy-mm-dd
                webServiceVO.setReserved10(deviceTypeId);
                webServiceVO.setCnicIssuanceDate(cnicIssueDate);
                webServiceVO.setEmailAddress(emailAddress);
                webServiceVO.setCustomerMobileNetwork(customerMobileNetwork);
                webServiceVO.setReserved9("");
                webServiceVO = this.getCommonCommandManager().getFonePayManager().createCustomer(webServiceVO,isConsumerApp);
            }
                if(isUpgrade.equals("0") )
			{
				mobileNo = mobileNo + "/" + customerMobileNetwork;
				webServiceVO.setMobileNo(mobileNo);
				webServiceVO.setCnicNo(cnic);
				webServiceVO.setBirthPlace(birthPlace);
				webServiceVO.setMotherMaiden(motherMaidenName);
				webServiceVO.setDateOfBirth(dob);
				webServiceVO.setCnicStatus(cnicStatus);
				webServiceVO.setCnicExpiry(cnicExpiry);
				webServiceVO.setPresentAddress(presentAddress);
				webServiceVO.setAccountType(accountType);
				webServiceVO.setCustomerPhoto(customerPhoto);
				webServiceVO.setCnicFrontPhoto(cnicFrontPhoto);
				webServiceVO.setConsumerName(name);
				webServiceVO.setGender(gender);
				webServiceVO.setReserved2("1");
				//Below parameter are set to change cnic issuance Date format change  for api dd-mm-yyyy to yyyy-mm-dd
				webServiceVO.setReserved10(deviceTypeId);
				webServiceVO.setCnicIssuanceDate(cnicIssueDate);
				webServiceVO.setEmailAddress(emailAddress);
				webServiceVO.setCustomerMobileNetwork(customerMobileNetwork);
				webServiceVO = this.getCommonCommandManager().getFonePayManager().createCustomer(webServiceVO,isConsumerApp);
			}
			else if(isUpgrade.equals("1"))
			{
				BaseWrapper baseWrapper1 = new BaseWrapperImpl();

				Long[] appUserTypes = {AppConstants.CONSUMER_APP};
				AppUserModel appUserModel = this.commonCommandManager.loadAppUserByCNICAndAccountType(this.cnic,appUserTypes);

				if(name.contains(" "))
				{
					String[] parts= name.split(" ");
					appUserModel.setFirstName(parts[0]);
					appUserModel.setLastName(parts[1]);
				}
				else
					appUserModel.setFirstName(name);

				appUserModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_COLD);
				//appUserModel.setCountryId(1L);

				baseWrapper1.putObject(CommandFieldConstants.KEY_APP_USER_MODEL,appUserModel);

				Long customerId=appUserModel.getCustomerId();

				CustomerModel customerModel =this.commonCommandManager.getCustomerModelById(customerId);
				customerModel.setCustomerAccountTypeId(CustomerAccountTypeConstants.LEVEL_1);

				baseWrapper1.putObject(CommandFieldConstants.KEY_CUSTOMER_MODEL,customerModel);

				SmartMoneyAccountModel smartMoneyAccountModel = this.commonCommandManager.getSmartMoneyAccountByAppUserModelAndPaymentModId(appUserModel
						,PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
				smartMoneyAccountModel.setRegistrationStateId(RegistrationStateConstants.VERIFIED);
				smartMoneyAccountModel.setUpdatedBy(appUserModel.getAppUserId());

				baseWrapper1.setBasePersistableModel(smartMoneyAccountModel);

				VerisysDataModel verisysDataModel=new VerisysDataModel();
				verisysDataModel.setCnic(appUserModel.getNic());
				verisysDataModel.setName(this.name);
				verisysDataModel.setMotherMaidenName(this.motherMaidenName);
				verisysDataModel.setPlaceOfBirth(this.birthPlace);
				verisysDataModel.setCurrentAddress(appUserModel.getAddress1());
				verisysDataModel.setPermanentAddress(appUserModel.getAddress2());
				verisysDataModel.setTranslated(false);
				verisysDataModel.setCreatedOn(new Date());
				verisysDataModel.setUpdatedOn(new Date());

				baseWrapper1.putObject(CommandFieldConstants.KEY_VARISYS_DATA_MODEL, verisysDataModel);

				this.commonCommandManager.saveOrUpdateCustomerAccountL0ToL1(baseWrapper1);
				responseMessage = "success";
			}
		}
		catch(Exception e) {
            handleException(e);
        }
        
    	if(logger.isDebugEnabled())
		{
			logger.debug("End of CustomerSelfRegistrationBaseCommand.execute()");
		}

//		if(ThreadLocalAppUser.getAppUserModel() != null){
//    		ThreadLocalAppUser.remove();
//		}

    }

    @Override
    public void prepare(BaseWrapper baseWrapper){
    	
    	if(logger.isDebugEnabled())
		{
			logger.debug("Start of CustomerSelfRegistrationBaseCommand.prepare()");
		}
    	
        this.baseWrapper = baseWrapper;
		this.customerMobileNetwork = getCommandParameter(baseWrapper,"CUST_MOB_NETWORK");
		cnic = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CNIC);
		birthPlace = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BIRTH_PLACE);
		mobileNo = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
		name = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_NAME);
		accountType = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUST_ACC_TYPE);
		motherMaidenName = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOTHER_MAIDEN);
		if(motherMaidenName != null && motherMaidenName.isEmpty()) {
			motherMaidenName = "Mother";
		}

		cnicIssueDate = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CNIC_ISSUE_DATE);
		dob = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CDOB);
		isNewAccount = getCommandParameter(baseWrapper,"IS_NEW_ACCOUNT");
		isUpgrade = getCommandParameter(baseWrapper, "IS_UPGRADE");

		if(isNewAccount.equals("1")){
			mobileNo = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
			cnic = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CNIC);
			cnicIssueDate = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CNIC_ISSUE_DATE);
			String[] parts = cnicIssueDate.split("-");
			String d = parts[0];
			String m = parts[1];
			if(Integer.parseInt(d) < 10)
				d = "0" + d;

			if(Integer.parseInt(m) < 10)
				m = "0" + m;

			cnicIssueDate = d + "-" + m + "-" + parts[2];
			emailAddress = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_EMAIL_ADDRESS);
			this.customerMobileNetwork = getCommandParameter(baseWrapper,"CUST_MOB_NETWORK");

		}

		if(isUpgrade.equals("0"))
		{
			isDiscrepant = getCommandParameter(baseWrapper, "IS_DISCREPANT");

			if(accountType.equals("1")){ // for L0 account opening
				customerPhoto = getCommandParameter(baseWrapper,"CUSTOMER_PHOTO");
				cnicFrontPhoto = getCommandParameter(baseWrapper, "CNIC_FRONT_PHOTO");
			}else{
				birthPlace = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BIRTH_PLACE);
				motherMaidenName = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOTHER_MAIDEN);
				presentAddress = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PRESENT_ADDR);
				cnicStatus = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CNIC_STATUS);


			}

			dob = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CDOB);
			cnicExpiry = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CNIC_EXPIRY);
			gender = getCommandParameter(baseWrapper, "GENDER");

			if(StringUtil.isNullOrEmpty(this.cnicExpiry)){
				this.cnicExpiry = "2099-01-01";
			}
		}

		AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();
        if(appUserModel == null){
        	appUserModel = new AppUserModel();
        	appUserModel.setAppUserId(PortalConstants.WEB_SERVICE_APP_USER_ID);
            ThreadLocalAppUser.setAppUserModel(appUserModel);
        }

        deviceTypeId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        
    	if(logger.isDebugEnabled())
		{
			logger.debug("End of CustomerSelfRegistrationBaseCommand.prepare()");
		}

    }

    @Override
    public void doValidate() throws CommandException{
    	
      	if(logger.isDebugEnabled())
    		{
    			logger.debug("Start of CustomerSelfRegistrationBaseCommand.doValidate()");
    		}
    	
    	ValidationErrors validationErrors = new ValidationErrors();

      	if(isNewAccount.equals("1")){
			ValidatorWrapper.doRequired(mobileNo, validationErrors, "Mobile No");
			ValidatorWrapper.doRequired(cnic, validationErrors, "CNIC");
			ValidatorWrapper.doRequired(cnicIssueDate, validationErrors, "CNIC_ISSUE_DATE");
			ValidatorWrapper.doRequired(emailAddress, validationErrors,"EMAIL_ADDRESS");
			ValidatorWrapper.doRequired(customerMobileNetwork,validationErrors,"Customer Mobile Network");
			isNewAccountOpening = Boolean.TRUE;
		}

		if(isNewAccountOpening=Boolean.FALSE) {

			if (isUpgrade.equals("0")) {
				ValidatorWrapper.doRequired(customerMobileNetwork, validationErrors, "Customer Mobile Network");
				ValidatorWrapper.doRequired(accountType, validationErrors, "ACCOUNT_TYPE");
				if (accountType.equals("2")) {
					ValidatorWrapper.doRequired(birthPlace, validationErrors, "BIRTH_PLACE");
					ValidatorWrapper.doRequired(cnicStatus, validationErrors, "CNIC_STATUS");
					ValidatorWrapper.doRequired(presentAddress, validationErrors, "PRESENT_ADDRESS");
				}
				if (isDiscrepant.equals("0")) {
					ValidatorWrapper.doRequired(mobileNo, validationErrors, "Mobile No");
					ValidatorWrapper.doRequired(cnic, validationErrors, "CNIC");
					ValidatorWrapper.doRequired(name, validationErrors, "NAME");
					ValidatorWrapper.doRequired(dob, validationErrors, "DOB");
				}

				if (isDiscrepant.equals("1") && accountType.equals("2")) {
					ValidatorWrapper.doRequired(mobileNo, validationErrors, "Mobile No");
					ValidatorWrapper.doRequired(cnic, validationErrors, "CNIC");
					ValidatorWrapper.doRequired(name, validationErrors, "NAME");
					ValidatorWrapper.doRequired(dob, validationErrors, "DOB");
				}

				ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "DEVICE_TYPE_ID");
				ValidatorWrapper.doRequired(cnicExpiry, validationErrors, "CNIC_EXPIRY");
			} else if (isUpgrade.equals("1")) {
				ValidatorWrapper.doRequired(cnic, validationErrors, "CNIC");
				ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "DEVICE_TYPE_ID");
			}
		}
      	if(validationErrors.hasValidationErrors()){
			throw new CommandException(validationErrors.getErrors(), ErrorCodes.VALIDATION_ERROR, ErrorLevel.HIGH,new Throwable());
		}
      	
    	if(logger.isDebugEnabled())
		{
			logger.debug("End of CustomerSelfRegistrationBaseCommand.doValidate()");
		}
      	
    }
    
    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
    	
        return new ValidationErrors();
    }

    @Override
    public String response() {

    	if(logger.isDebugEnabled())
		{
			logger.debug("Start of CustomerSelfRegistrationBaseCommand.response()");
		}

    	StringBuilder response = new StringBuilder();
    	String message = "";

    	if(webServiceVO.getReserved9().equals(RegistrationStateConstants.CLSPENDING.toString())) {
    	    message = "Account has been opened in Pending State. Kindly activate your account.";
        }
        else {
    	    message = "Account has been opened successfully. You will receive your login PIN via SMS. Please generate your MPIN after login JS Wallet mobile App.";
        }
        if(isUpgrade.equals("1") && responseMessage != null && responseMessage.equals("success"))
		{
			message = "Account Upgraded from L0 to L1 successfully";
			response.append(TAG_SYMBOL_OPEN).append(TAG_MESGS).append(TAG_SYMBOL_CLOSE).append(TAG_SYMBOL_OPEN).append(TAG_MESG)
					.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
					.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
					.append(CommandFieldConstants.KEY_RESP_XML_MSG)
					.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
					.append(message).append(TAG_SYMBOL_OPEN)
					.append(TAG_SYMBOL_SLASH).append(TAG_MESG)
					.append(TAG_SYMBOL_CLOSE).append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH).append(TAG_MESGS).append(TAG_SYMBOL_CLOSE);
		}
    	else if(this.accountType.equals("2")){
   	 	 	 response.append(TAG_SYMBOL_OPEN).append(TAG_MESGS).append(TAG_SYMBOL_CLOSE).append(TAG_SYMBOL_OPEN).append(TAG_MESG)
			.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
			.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
			.append(CommandFieldConstants.KEY_RESP_XML_MSG)
			.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
			.append(message).append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH).append(TAG_MESG)
			.append(TAG_SYMBOL_CLOSE).append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH).append(TAG_MESGS).append(TAG_SYMBOL_CLOSE);
    	}else{
    	 	 response.append(TAG_SYMBOL_OPEN).append(TAG_MESGS).append(TAG_SYMBOL_CLOSE).append(TAG_SYMBOL_OPEN).append(TAG_MESG)
			.append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
			.append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
			.append(CommandFieldConstants.KEY_RESP_XML_MSG)
			.append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
			.append(message).append(TAG_SYMBOL_OPEN)
			.append(TAG_SYMBOL_SLASH).append(TAG_MESG)
			.append(TAG_SYMBOL_CLOSE).append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH).append(TAG_MESGS).append(TAG_SYMBOL_CLOSE);
    	}
    	
    	
    	if(logger.isDebugEnabled())
		{
			logger.debug("End of CustomerSelfRegistrationBaseCommand.response()");
		}
    	
        return response.toString();
    }

    //---------------------------------------------------------------------

}
