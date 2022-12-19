package com.inov8.microbank.server.service.commandmodule;

import com.inov8.common.util.RandomUtils;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.app.service.AppManager;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.customermodule.CustomerPictureModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.nadraVerisys.model.VerisysDataModel;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.springframework.dao.DataAccessException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;

/**
 * Created by Inov8 on 6/19/2018.
 */
public class OpenCustomerHRAAccountCommand extends BaseCommand {
    protected final Log logger = LogFactory.getLog(OpenCustomerHRAAccountCommand.class);

    protected AppUserModel appUserModel;
    protected AppManager appManager;


    protected BaseWrapper preparedBaseWrapper;
    private String termsPhoto, signaturePhoto, customerPhoto, cNicFrontPhoto,cNicBackPhoto, cName, cMsisdn, cNic, cRegState, cRegStateId, transactionId;
    private ArrayList<CustomerPictureModel> arrayCustomerPictures;
    private CommonCommandManager commonCommandManager;
    private Date nowDate;
    String mfsId;
    String customerAccountyType;
    String cdob;
    String cnicExpiry;
    String depositAmount;
    String l1FormPhoto;
    String isCnicSeen;
    private String encryptionType;
    private String agentMobileNo;
    private String agentPIN;
    private String fatherHusbandName;

    private String birthPlace;
    private String gender;
    private String motherName;
    private String presentAddress;
    private String permanentAddress;
    boolean isBvsAccount = false;
    boolean isHRA = false;
    private String depositAmountFlag;
    boolean initialDepositErrorCheck = false;
    boolean pendingTransErrorCheck = false;
    boolean accountCreated = false;
    boolean accountUpdated = false;
    private String postCreationerrorMessage;
    //RetailerContactModel retailerContactModel;
    ////********HRA*************/////
    private String transactionPurpose;
    private String occupation;
    private String nextOfKinMobile;
    private String sourceOfIncome;
    private String kinName;
    private String kinCNIC;
    private String kinRelationship;
    private ArrayList<CustomerRemitterModel> customerRemitterModelList = new ArrayList<CustomerRemitterModel>();
    ////************************/////

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    WorkFlowWrapper workFlowWrapper;

    private boolean isConsumerApp = false;

    public void prepare(BaseWrapper baseWrapper) {
        if (this.logger.isDebugEnabled())
            this.logger.debug("Start of OpenCustomerHRAAccountCommand.prepare()");

        this.cName = getCommandParameter(baseWrapper, "CNAME");
        this.cMsisdn = getCommandParameter(baseWrapper, "CMOB");
        this.cNic = getCommandParameter(baseWrapper, "CNIC");
        this.cdob = getCommandParameter(baseWrapper, "CDOB");
        this.fatherHusbandName = getCommandParameter(baseWrapper, "FATHER_HUSBND_NAME");
        this.agentMobileNo = getCommandParameter(baseWrapper, "AMOB");
        ///************************HRA***********************************///
        this.isHRA = true;
        this.transactionPurpose = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TRANS_PURPOSE);
        this.occupation = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_OCCUPATION);
        this.nextOfKinMobile = getCommandParameter(baseWrapper, "Kin_MOB_NO");
        this.sourceOfIncome = getCommandParameter(baseWrapper, "SOI");
        this.kinName = getCommandParameter(baseWrapper, "KIN_NAME");
        this.kinCNIC = getCommandParameter(baseWrapper, "KIN_CNIC");
        this.kinRelationship = getCommandParameter(baseWrapper, "KIN_RELATIONSHIP");
        this.motherName = getCommandParameter(baseWrapper, "KIN_RELATIONSHIP");

        for (int i = 1; i <= 5; i++) {
            CustomerRemitterModel customerRemitterModel = new CustomerRemitterModel();
            String _location, _relationship = null;
            _location = getCommandParameter(baseWrapper, "ORG_LOC" + i);
            _relationship = getCommandParameter(baseWrapper, "ORG_REL" + i);
            if (StringUtil.isNullOrEmpty(_location) || StringUtil.isNullOrEmpty(_relationship)) {
                break;
            } else {
                customerRemitterModel.setRemittanceLocation(_location);
                customerRemitterModel.setRelationship(_relationship);
                customerRemitterModel.setIsActive(1L);
                customerRemitterModelList.add(customerRemitterModel);
            }
        }

        ///*************************HRA***********************************//

        this.preparedBaseWrapper = baseWrapper;
        this.deviceTypeId = getCommandParameter(baseWrapper, "DTID");

        encryptionType = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ENCRYPTION_TYPE);
        agentMobileNo = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AGENT_MOBILE);
        /**
         * added by muhammad atif
         */
        //Load again app user because previous app user from thread local has been changed in FonePayOTPVerification Command
        //if (ThreadLocalAppUser.getAppUserModel().getAppUserId() == PortalConstants.WEB_SERVICE_APP_USER_ID) {
            try {
                if(this.cMsisdn.equals(""))
                {
                    appUserModel = this.getCommonCommandManager().loadAppUserByCnicAndType(this.cNic);
                    isConsumerApp = true;
                }
                else
                    appUserModel = getCommonCommandManager().loadAppUserByMobileAndType(this.cMsisdn);
            } catch (FrameworkCheckedException e) {
                e.printStackTrace();
            }

            /*if (appUserModel != null) {
                logger.debug("[FonepayPaymentCommand.execute] AppUserModel loader wil AppUserId:" + appUserModel.getAppUserId());
                ThreadLocalAppUser.setAppUserModel(appUserModel);
            }*/
        /*} else {
            appUserModel = ThreadLocalAppUser.getAppUserModel();
        }*/
        /*BaseWrapper bWrapper = new BaseWrapperImpl();
        RetailerContactModel retailerContactModel = new RetailerContactModel();
        if(!isConsumerApp)
        {
            retailerContactModel.setRetailerContactId(appUserModel.getRetailerContactId());

            bWrapper.setBasePersistableModel(retailerContactModel);

            try {
                bWrapper = this.getCommonCommandManager().loadRetailerContact(bWrapper);
                this.retailerContactModel = (RetailerContactModel) bWrapper.getBasePersistableModel();

            } catch (Exception ex) {
                logger.error("[OpenCustomerL0AccountCommand.prepare] Unable to load RetailerContact info... " + ex.getMessage(), ex);
            }
        }*/


        if (this.logger.isDebugEnabled())
            this.logger.debug("End of OpenCustomerL0AccountCommand.prepare()");
    }

    @Override
    public void doValidate() throws CommandException {
        if (this.logger.isDebugEnabled())
            this.logger.debug("Start of OpenCustomerL0AccountCommand.doValidate()");

        ValidationErrors validationErrors = new ValidationErrors();

        if(!isConsumerApp&&!deviceTypeId.equals(DeviceTypeConstantsInterface.WEB_SERVICE.toString()))
        {
            validationErrors = ValidatorWrapper.doRequired(this.cMsisdn, validationErrors, "Customer Mobile No");
            ValidatorWrapper.doRequired(encryptionType, validationErrors, "Encryption Type");
            ValidatorWrapper.doRequired(agentMobileNo, validationErrors, "Agent Mobile No");
        }
        validationErrors = ValidatorWrapper.doRequired(this.cName, validationErrors, "Customer Name");

        validationErrors = ValidatorWrapper.doRequired(this.cNic, validationErrors, "CNIC");
        validationErrors = ValidatorWrapper.doRequired(this.deviceTypeId, validationErrors, "Device Type");
        validationErrors = ValidatorWrapper.doRequired(this.cdob, validationErrors, "Date of Birth");


        //********************************************************************************************************************
        //										Check if cnic is blacklisted
        //********************************************************************************************************************
        if (!validationErrors.hasValidationErrors()) {
            if (this.getCommonCommandManager().isCnicBlacklisted(this.cNic)) {
                validationErrors.getStringBuilder().append(MessageUtil.getMessage("walkinAccountBlacklisted"));
                throw new CommandException(validationErrors.getErrors(), ErrorCodes.TERMINATE_EXECUTION_FLOW, ErrorLevel.MEDIUM, new Throwable());
            }
        }
        //**********************************************************************************************************************
        //ValidatorWrapper.doNumeric(depositAmount, validationErrors, "Initial Deposit");

        if (!validationErrors.hasValidationErrors()) {
        }

        if (validationErrors.hasValidationErrors()) {
            throw new CommandException(validationErrors.getErrors(), ErrorCodes.VALIDATION_ERROR, ErrorLevel.HIGH, new Throwable());
        }

        if (this.logger.isDebugEnabled())
            this.logger.debug("End of OpenCustomerL0AccountCommand.doValidate()");
    }

    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        return new ValidationErrors();
    }

    public void execute() throws CommandException {
        if (this.logger.isDebugEnabled())
            this.logger.debug("Start of OpenCustomerL0AccountCommand.execute()");

        VerisysDataModel verisysDataModel=new VerisysDataModel();
        BaseWrapper baseWrapper = new BaseWrapperImpl();

        try
        {
            if(isConsumerApp)
                this.cMsisdn = appUserModel.getMobileNo();
            //added by muhammad atif
            commonCommandManager	=	this.getCommonCommandManager();

            workFlowWrapper = new WorkFlowWrapperImpl();

            baseWrapper.putObject(CommandFieldConstants.CUSTOMER_REMITTENCE_KEY,customerRemitterModelList);

            nowDate = new Date();


        //AppUserModel appUserModel=this.commonCommandManager.getAppUserWithRegistrationState(this.cMsisdn,this.cNic, RegistrationStateConstants.VERIFIED);
            if(appUserModel == null)
                throw new CommandException( "No Record Found Against this Mobile and CNIC" , ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null ) ;

            baseWrapper.putObject(CommandFieldConstants.KEY_APP_USER_MODEL,appUserModel);

            CustomerModel customerModel=this.commonCommandManager.getCustomerModelById(appUserModel.getCustomerId());

            //customerModel.setCustomerId(cModel.getCustomerId());
            customerModel.setNokRelationship(this.kinRelationship);
            customerModel.setNokContactNo(this.nextOfKinMobile);
            customerModel.setNokName(this.kinName);
            customerModel.setHraTrxnPurpose(this.transactionPurpose);
            customerModel.setHraOccupation(this.occupation);
            baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MODEL, customerModel);

            String mfsId = computeMfsId();

            Long bankId = this.getCommonCommandManager().getOlaBankMadal().getBankId();

            SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
            smartMoneyAccountModel.setBankId(bankId);
            smartMoneyAccountModel.setPaymentModeId(PaymentModeConstantsInterface.HOME_REMMITTANCE_ACCOUNT);
            smartMoneyAccountModel.setCreatedOn(new Date());
            smartMoneyAccountModel.setUpdatedOn(new Date());
            smartMoneyAccountModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
            smartMoneyAccountModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
            smartMoneyAccountModel.setActive(true);
            smartMoneyAccountModel.setChangePinRequired(false);
            smartMoneyAccountModel.setDefAccount(true);
            smartMoneyAccountModel.setDeleted(false);
            smartMoneyAccountModel.setName( "i8_hra_" + mfsId);
            smartMoneyAccountModel.setCustomerId(appUserModel.getCustomerId());
            smartMoneyAccountModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_COLD);
            smartMoneyAccountModel.setRegistrationStateId(RegistrationStateConstants.VERIFIED);
            smartMoneyAccountModel.setStatusId(OlaStatusConstants.ACCOUNT_STATUS_ACTIVE);
            smartMoneyAccountModel.setAccountClosedUnsetteled(0L);
            smartMoneyAccountModel.setAccountClosedSetteled(0L);

            baseWrapper.putObject(CommandFieldConstants.KEY_SMART_MONEY_ACCOUNT_MODEL, smartMoneyAccountModel);

            OLAVO olaVo = new OLAVO();
            olaVo.setFirstName(appUserModel.getFirstName());
            olaVo.setMiddleName(" ");
            olaVo.setLastName(appUserModel.getLastName());
            olaVo.setFatherName(appUserModel.getLastName());
            olaVo.setCnic(this.cNic);
            olaVo.setCustomerAccountTypeId(CustomerAccountTypeConstants.HRA);
            olaVo.setAddress("Lahore");
            olaVo.setLandlineNumber(this.cMsisdn);
            olaVo.setMobileNumber(this.cMsisdn);
            olaVo.setDob(dateFormat.parse(this.cdob));
            olaVo.setStatusId(OlaStatusConstants.ACCOUNT_STATUS_ACTIVE);
            baseWrapper.putObject(CommandFieldConstants.KEY_ONLINE_ACCOUNT_MODEL, olaVo);

            AccountInfoModel accountInfoModel = new AccountInfoModel();
            accountInfoModel.setAccountNick(smartMoneyAccountModel.getName());
            accountInfoModel.setActive(smartMoneyAccountModel.getActive());
            accountInfoModel.setCreatedOn(new Date());
            accountInfoModel.setUpdatedOn(new Date());
            accountInfoModel.setCustomerMobileNo(this.cMsisdn);
            accountInfoModel.setFirstName(appUserModel.getFirstName());
            accountInfoModel.setLastName(appUserModel.getLastName());
            accountInfoModel.setPaymentModeId(PaymentModeConstantsInterface.HOME_REMMITTANCE_ACCOUNT);
            accountInfoModel.setDeleted(Boolean.FALSE);
            accountInfoModel.setCustomerId(smartMoneyAccountModel.getCustomerId());
            //Added after HSM Integration
            accountInfoModel.setPan(PanGenerator.generatePAN());
            // End HSM Integration Change

            baseWrapper.putObject(CommandFieldConstants.KEY_ACCOUNT_INFO_MODEL, accountInfoModel);
            verisysDataModel.setCnic(this.cNic);
            verisysDataModel.setPlaceOfBirth(this.birthPlace);
            verisysDataModel.setMotherMaidenName(this.motherName);
            verisysDataModel.setName(this.cName);
            verisysDataModel.setCurrentAddress(this.presentAddress);
            verisysDataModel.setPermanentAddress(this.permanentAddress);
            verisysDataModel.setTranslated(false);
            verisysDataModel.setCreatedOn(new Date());
            verisysDataModel.setUpdatedOn(new Date());
            baseWrapper.putObject(CommandFieldConstants.KEY_VARISYS_DATA_MODEL, verisysDataModel);
            try{

                baseWrapper = this.getCommonCommandManager().saveOrUpdateAccountOpeningHRARequest(baseWrapper);

            }catch(Exception ex){
                logger.error("Exception in occurred in saveOrUpdateAccountOpeningHRARequest... Exception.getMessage():"+ex.getMessage());
                logger.error("Exception in occurred in saveOrUpdateAccountOpeningHRARequest... Exception.getMessage()..",ex);
                if(ex instanceof NullPointerException
                        || ex instanceof HibernateException
                        || ex instanceof SQLException
                        || ex instanceof DataAccessException
                        || (ex.getMessage() != null && ex.getMessage().indexOf("Exception") != -1) ){

                    logger.error("Converting Exception ("+ex.getClass()+") to generic error message...");
                    throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
                }else{
                    throw ex;
                }
            }

            accountCreated = true;
            //Sending SMS (with links android/iOS) to Users
            String msgToText = "Your HRA Account has been Opened successfully and you can use your existing MPIN for financial transactions.";
            BaseWrapper msgBaseWrapper = new BaseWrapperImpl();
            msgBaseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, new SmsMessage(this.cMsisdn , msgToText));
            commonCommandManager.sendSMSToUser(msgBaseWrapper);
            /*****************************************************************************************
             * Logging the dataexe
             */
            ActionLogModel actionLogModel = new ActionLogModel();
            actionLogModel.setActionId(PortalConstants.ACTION_CREATE);
            actionLogModel.setUsecaseId(PortalConstants.MFS_ACCOUNT_CREATE_USECASE_ID);
            actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_START); // the process is starting
            actionLogModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
            actionLogModel.setUserName(UserUtils.getCurrentUser().getUsername());
            actionLogModel.setStartTime(new Timestamp(new Date().getTime()));
            actionLogModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);
            actionLogModel.setCustomField11(mfsId);
            try {
                actionLogModel = commonCommandManager.logAction(actionLogModel);
            } catch (FrameworkCheckedException e1) {
                e1.printStackTrace();
            }
            ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
        }
        catch(FrameworkCheckedException ex)
        {
            ex.printStackTrace();
            String errorMessage = prepareErrorMessage(ex, initialDepositErrorCheck, pendingTransErrorCheck, accountCreated, true);
            if(!accountCreated){
                throw new CommandException(errorMessage,(ex.getErrorCode() == 0)? ErrorCodes.COMMAND_EXECUTION_ERROR:ex.getErrorCode(), ErrorLevel.MEDIUM,ex);
            }else{
                postCreationerrorMessage = errorMessage;
                logger.error(errorMessage,ex);
            }
        }
        catch(WorkFlowException wex)
        {
            wex.printStackTrace();
            String errorMessage = prepareErrorMessage(wex, initialDepositErrorCheck, pendingTransErrorCheck, accountCreated, true);
            if(!accountCreated){
                throw new CommandException(errorMessage, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM,wex);
            }else{
                postCreationerrorMessage = errorMessage;
                logger.error(errorMessage,wex);
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            String errorMessage = prepareErrorMessage(ex, initialDepositErrorCheck, pendingTransErrorCheck, accountCreated, false);
            if(!accountCreated){
                throw new CommandException(errorMessage, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.HIGH,ex);
            }else{
                postCreationerrorMessage = errorMessage;
                logger.error(errorMessage,ex);
            }
        }

        if (this.logger.isDebugEnabled()){
            this.logger.debug("End of OpenCustomerL0AccountCommand.execute()");
        }
        /*throw new CommandException("Account Created Successfully",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.HIGH,null);*/
    }

    public String response() {
        if (this.logger.isDebugEnabled())
            this.logger
                    .debug("Start of OpenCustomerL0AccountCommand.response()");
        return toXML();
    }

    private String prepareErrorMessage(Exception ex, boolean initialDepositErrorCheck, boolean pendingTransErrorCheck, boolean accountCreated, boolean addReason){
        String errorMessage = ex.getMessage();

        if(accountCreated){
            if(initialDepositErrorCheck){
                errorMessage = this.getMessageSource().getMessage("openCustomerL0AccountCommand.initialDeposit.failed", null,null);
            }else if(pendingTransErrorCheck){
                errorMessage = this.getMessageSource().getMessage("openCustomerL0AccountCommand.pendingTransaction.failed", null,null);
            }

            if((initialDepositErrorCheck || pendingTransErrorCheck) && addReason){
                errorMessage = errorMessage + " Reason: " + ex.getMessage();
            }
        }else{
            // [04 June 2015 omar butt] This check is added to avoid a special error scenario same customer creation from two agent simultaneously
            // (one successful and second shows SQL Exception)
            if(!StringUtil.isNullOrEmpty(errorMessage) && (errorMessage.contains("Hibernate")
                    || errorMessage.contains("Exception")
                    || errorMessage.contains("SQLException") )){

                errorMessage = this.getMessageSource().getMessage("newMfsAccount.unknown", null,null);

            }

        }

        return errorMessage;
    }

    private String toXML() {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of OpenCustomerL0AccountCommand.toXML()");
        }
        String message = "";
        if(accountCreated)
            message="HRA Account created Successfully";
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append(XMLConstants.TAG_SYMBOL_OPEN)
                .append(XMLConstants.TAG_MESGS)
                .append(XMLConstants.TAG_SYMBOL_CLOSE)

                .append(XMLConstants.TAG_SYMBOL_OPEN)
                .append(XMLConstants.TAG_MESG)
                .append(XMLConstants.TAG_SYMBOL_SPACE)
                .append(XMLConstants.ATTR_LEVEL)
                .append(XMLConstants.TAG_SYMBOL_EQUAL)
                .append(XMLConstants.TAG_SYMBOL_QUOTE)
                .append(XMLConstants.ATTR_LEVEL_ONE)
                .append(XMLConstants.TAG_SYMBOL_QUOTE)
                .append(XMLConstants.TAG_SYMBOL_CLOSE)
                .append(message)
                .append(XMLConstants.TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH)
                .append(XMLConstants.TAG_MESG)
                .append(XMLConstants.TAG_SYMBOL_CLOSE);

        xmlBuilder.append(XMLConstants.TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH)
                .append(XMLConstants.TAG_MESGS)
                .append(XMLConstants.TAG_SYMBOL_CLOSE);

        if (this.logger.isDebugEnabled()) {
            this.logger.debug("End of OpenCustomerL0AccountCommand.toXML()");
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return xmlBuilder.toString();
    }





    private void addCustomerPicture(Long pictureTypeId, String pictureName, boolean isSkipped) throws IOException, CommandException {
        CustomerPictureModel customerPictureModel = new CustomerPictureModel();
        try
        {
            System.out.println("Picture Name: " + pictureName);
            File imageFile;
            if(!isSkipped){
                imageFile = commonCommandManager.loadImage("images/upload_dir/" + pictureName);
            }else{
                imageFile = commonCommandManager.loadImage("images/no_photo_icon.png");
            }

            imageFile.getAbsolutePath();
            imageFile.getTotalSpace();
            imageFile.getCanonicalPath();
            imageFile.length();
            Path path = imageFile.toPath();
            byte[] imageFileBytes = Files.readAllBytes(path);

            customerPictureModel.setPicture(imageFileBytes);
            customerPictureModel.setPictureTypeId(pictureTypeId);
            customerPictureModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_1);
            customerPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
            customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
            customerPictureModel.setCreatedOn(nowDate);
            customerPictureModel.setUpdatedOn(nowDate);
        }
        catch (IOException e)
        {
			/* Special Scenario Handling [Special Request of Humayun Naveed for Fraud Prevention JSBLMFS-72]
			 * Implementation:
			 *		File Not Found Error will move the AgentMate from Account Opening to Home Screen by using New ErrorCode 9018
			 * Scenario:
			 * 		The scenario is two different agents are trying to register same customer simultaneously.
			 * 		On agent 1 it is successful and on agent 2 it displayed 'File Not Found' (As the pictures
			 * 		have already been removed after successful registration at agent1)
			 */
            throw new CommandException(this.getMessageSource().getMessage("newMfsAccount.unknown", null,null), ErrorCodes.FILE_NOT_FOUND, ErrorLevel.MEDIUM);
        }
    }

    /**
     * This method computes the next mfsId / username, it checks
     * the table App_User, and Golden_Nums are checked.
     */
    private String computeMfsId() {
        AppUserModel appUserModel;
        GoldenNosModel goldenNosModel;
        Long nextLongValue = null;
        boolean flag = true;

        while(flag) {
            nextLongValue = commonCommandManager.getSequenceGenerator().nextLongValue();
            appUserModel = new AppUserModel();
            goldenNosModel = new GoldenNosModel();
            appUserModel.setUsername(String.valueOf(nextLongValue));
            goldenNosModel.setGoldenNumber(String.valueOf(nextLongValue));
            int countAppUser = commonCommandManager.countByExample(appUserModel);
            int countGoldenNos = commonCommandManager.countByExample(goldenNosModel);
            if(countAppUser == 0 && countGoldenNos == 0 ) {
                flag = false;
            }
        }

        return String.valueOf(nextLongValue);
    }

    private void sendSMSToUsers(String userId, String pin) {
        try {
            BaseWrapper baseWrapper  = new BaseWrapperImpl();

            String brandName = MessageUtil.getMessage("jsbl.brandName");

            //Message to customer
            String customerSMS = null;
            baseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, new SmsMessage(cMsisdn, customerSMS));
            getCommonCommandManager().sendSMSToUser(baseWrapper);

            //Message to agent
            String agentSMS;

            getCommonCommandManager().sendSMSToUser(baseWrapper);
        }

        catch (FrameworkCheckedException e) {
            e.printStackTrace();
        }
    }

    public static String escapeUnicode(String input) {
        StringBuilder b = new StringBuilder(input.length());
        java.util.Formatter f = new java.util.Formatter(b);
        for (char c : input.toCharArray()) {
            if (c < 128) {
                b.append(c);
            } else {
                f.format("\\%04x", (int) c);
            }
        }
        return b.toString();
    }

    public AppManager getAppManager() {
        return appManager;
    }

    public void setAppManager(AppManager appManager) {
        this.appManager = appManager;
    }
}
