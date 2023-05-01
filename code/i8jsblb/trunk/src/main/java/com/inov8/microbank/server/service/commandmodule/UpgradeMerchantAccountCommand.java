package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.app.service.AppManager;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.MerchantAccountModel;
import com.inov8.microbank.common.model.customermodule.MerchantAccountPictureModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.ola.integration.vo.OLAVO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;

public class UpgradeMerchantAccountCommand extends BaseCommand {
    protected final Log logger = LogFactory.getLog(UpgradeMerchantAccountCommand.class);
    protected AppUserModel appUserModel;
    protected AppManager appManager;
    protected BaseWrapper preparedBaseWrapper;
    String latitude;
    String longitude;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy");
    private String customerPhoto, cNicFrontPhoto, cNicBackPhoto, sourceofincomePic, proofOfProfession, cName, cMsisdn, cNic, cRegState, cRegStateId, transactionId;
    private ArrayList<MerchantAccountPictureModel> arrayCustomerPictures = new ArrayList<MerchantAccountPictureModel>();
    private CommonCommandManager commonCommandManager;
    private String expectedMonthlyTurnover;
    private String city;
    private String businessName;
    private String businessAddress;
    private String typeOfBusiness;
    private Long tillId;
    private Long idType;
    private Long idN;
    private String channelId;
    private String terminalId;
    String customerAccountyType;
    private CustTransManager custTransManager;



    ////************************/////
    private String signaturePicture;
    private ESBAdapter esbAdapter;

    @Override
    public void prepare(BaseWrapper baseWrapper) {
        this.logger.info("Start of UpgradeL2AccountCommand.prepare()");

        this.deviceTypeId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        this.cMsisdn = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
        this.channelId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CHANNEL_ID);
        this.terminalId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TERMINAL_ID);
        this.cNic = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CNIC);
        this.cName = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_NAME);
        this.cNicFrontPhoto = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CNIC_FRONT_PHOTO);
        this.customerPhoto = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_PHOTO);
        this.cNicBackPhoto=getCommandParameter(baseWrapper,CommandFieldConstants.KEY_CNIC_BACK_PHOTO);
        this.latitude = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_LATITUDE);
        this.longitude = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_LONGITUDE);
        this.businessAddress = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PRESENT_ADDR);
        this.typeOfBusiness = getCommandParameter(baseWrapper, "INCOME_SOURCE");
        this.businessName = getCommandParameter(baseWrapper, "BUSINESS_NAME");
        this.city = getCommandParameter(baseWrapper, "CITY");
        this.expectedMonthlyTurnover = getCommandParameter(baseWrapper, "EXPECTED_MONTHLY_TURNOVER");
        this.tillId = Long.valueOf((getCommandParameter(baseWrapper, "TILL_ID")));
        this.idType = Long.valueOf((getCommandParameter(baseWrapper, "ID_TYPE")));
        this.idN = Long.valueOf((getCommandParameter(baseWrapper, "ID_N")));
        this.customerAccountyType = getCommandParameter(baseWrapper, "CUST_ACC_TYPE");


        this.logger.info("End of UpgradeMerchantAccountCommand.prepare()");

    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        this.logger.info("Start of UpgradeMerchantAccountCommand.validate()");
        validationErrors = new ValidationErrors();

        validationErrors = ValidatorWrapper.doRequired(this.cName, validationErrors, "Customer Name");

        validationErrors = ValidatorWrapper.doRequired(this.cMsisdn, validationErrors, "Customer Mobile No");
        validationErrors = ValidatorWrapper.doRequired(this.cNic, validationErrors, "CNIC");
        validationErrors = ValidatorWrapper.doRequired(this.deviceTypeId, validationErrors, "Device Type");

        if (!validationErrors.hasValidationErrors()) {
            if (this.getCommonCommandManager().isCnicBlacklisted(this.cNic)) {
                validationErrors.getStringBuilder().append(MessageUtil.getMessage("walkinAccountBlacklisted"));
                throw new CommandException(validationErrors.getErrors(), ErrorCodes.TERMINATE_EXECUTION_FLOW, ErrorLevel.MEDIUM, new Throwable());
            }
        }
        if (validationErrors.hasValidationErrors()) {
            throw new CommandException(validationErrors.getErrors(), ErrorCodes.VALIDATION_ERROR, ErrorLevel.HIGH, new Throwable());
        }
        this.logger.info("End of UpgradeMerchantAccountCommand.validate()");

        return validationErrors;
    }

    @Override
    public void execute() throws CommandException {
        this.logger.info("Start of UpgradeMerchantAccountCommand.execute()");
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        BaseWrapper baseWrapper1 = new BaseWrapperImpl();

        try {
            Date nowDate = new Date();
            commonCommandManager = this.getCommonCommandManager();
            MerchantAccountModel merchantAccountModel = new MerchantAccountModel();
            merchantAccountModel = this.getCommonCommandManager().loadMerchantCustomerByMobileAndAccUpdate(cMsisdn, 1L);

            if (merchantAccountModel == null) {
                AppUserModel appUserModel = this.commonCommandManager.loadAppUserByCnicAndType(this.cNic);

                if (appUserModel != null) {

                    CustomerModel customerModel = new CustomerModel();
                    customerModel = this.getCommonCommandManager().getCustomerModelById(appUserModel.getCustomerId());
                    merchantAccountModel = new MerchantAccountModel();
                    merchantAccountModel.setMobileNo(cMsisdn);
                    merchantAccountModel.setCnic(cNic);
                    merchantAccountModel.setConsumerName(cName);
                    merchantAccountModel.setLatitude(latitude);
                    merchantAccountModel.setLongitude(longitude);
                    merchantAccountModel.setRegistrationStatus(BlinkCustomerRegistrationStateConstantsInterface.APPROVED.toString());
                    merchantAccountModel.setCity(city);
                    merchantAccountModel.setTillNumber(tillId);
                    merchantAccountModel.setIdName(idN);
                    merchantAccountModel.setIdType(idType);
                    merchantAccountModel.setBusinessName(businessName);
                    merchantAccountModel.setBusinessAddress(businessAddress);
                    merchantAccountModel.setTypeOfBusiness(typeOfBusiness);
                    merchantAccountModel.setExpectedMonthlySalary(expectedMonthlyTurnover);
                    merchantAccountModel.setAccUpdate(1L);
                    merchantAccountModel.setCustomerId(appUserModel.getCustomerId());
                    merchantAccountModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                    merchantAccountModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                    merchantAccountModel.setUpdatedOn(new Date());
                    merchantAccountModel.setCreatedOn(new Date());
                    getCommonCommandManager().createMerchantAccountModel(merchantAccountModel);


                    if (customerModel != null) {
                        customerModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
                        customerModel.setUpdatedOn(new Date());
                        customerModel.setName(cName);
                        customerModel.setMobileNo(cMsisdn);
                        customerModel.setMonthlyTurnOver(expectedMonthlyTurnover);
                        customerModel.setLatitude(latitude);
                        customerModel.setLongitude(longitude);
                        customerModel.setCustomerAccountTypeId(Long.valueOf(customerAccountyType));
                        /**
                         * Saving the CustomerModel
                         */
                        baseWrapper = new BaseWrapperImpl();
                        baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MODEL, customerModel);
                        OLAVO olaVo = new OLAVO();
                        olaVo.setFirstName(appUserModel.getFirstName());
                        olaVo.setCnic(appUserModel.getNic());
                        olaVo.setCustomerAccountTypeId(customerModel.getCustomerAccountTypeId());
                        olaVo.setLandlineNumber(appUserModel.getMobileNo());
                        olaVo.setMobileNumber(appUserModel.getMobileNo());
                        baseWrapper.putObject(CommandFieldConstants.KEY_ONLINE_ACCOUNT_MODEL, olaVo);
                        baseWrapper.putObject(CommandFieldConstants.KEY_APP_USER_MODEL, appUserModel);
                        baseWrapper = this.getCommonCommandManager().saveOrUpdateMerchantRequest(baseWrapper);
                    }

                    if (this.customerPhoto != null && !this.customerPhoto.equals("")) {
                        addCustomerPicture(PictureTypeConstants.CUSTOMER_PHOTO, arrayCustomerPictures, nowDate, this.customerPhoto, appUserModel.getCustomerId());
                    }

                    if (this.cNicFrontPhoto != null && !this.cNicFrontPhoto.equals("")) {
                        addCustomerPicture(PictureTypeConstants.ID_FRONT_SNAPSHOT, arrayCustomerPictures, nowDate, this.cNicFrontPhoto, appUserModel.getCustomerId());
                    }
                    if (this.cNicBackPhoto != null && !this.cNicBackPhoto.equals("")) {
                        addCustomerPicture(PictureTypeConstants.ID_BACK_SNAPSHOT, arrayCustomerPictures, nowDate, this.cNicBackPhoto, appUserModel.getCustomerId());
                    }



                    for (MerchantAccountPictureModel merchantAccountPictureModel : arrayCustomerPictures) {
                        merchantAccountPictureModel.setCustomerId(appUserModel.getCustomerId());
                    }

                    this.getCommonCommandManager().saveOrUpdateMerchantAccountPictureModel(arrayCustomerPictures);
                } else {
                    throw new CommandException("Account does not exist", 9001L, ErrorLevel.MEDIUM);
                }
            } else {
                throw new CommandException("Request Already Exists against the given mobile number.", 1024L, ErrorLevel.MEDIUM);
            }
        } catch (Exception ex) {
            if (ex.getMessage().contains("Request Already Exists against the given mobile number")) {
                throw new CommandException("Request Already Exists against the given mobile number", 1024L, ErrorLevel.MEDIUM, null);
            }
            ex.printStackTrace();
            throw new CommandException(MessageUtil.getMessage("newMfsAccount.unknown"), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);
        }
    }

    private void addCustomerPicture(Long pictureTypeId, ArrayList<MerchantAccountPictureModel> arrayCustomerPictures, Date nowDate, String pictureAsString, Long customerId) throws IOException, CommandException {
        MerchantAccountPictureModel merchantAccountPictureModel = new MerchantAccountPictureModel();
        try {
            merchantAccountPictureModel = commonCommandManager.getMerchantAccountPictureByTypeId(pictureTypeId, customerId);

            if (merchantAccountPictureModel != null) {
                pictureAsString = pictureAsString.replace(" ", "+");
                byte[] imageFileBytes = org.apache.commons.codec.binary.Base64.decodeBase64(pictureAsString.getBytes());

                merchantAccountPictureModel.setPicture(imageFileBytes);
                merchantAccountPictureModel.setPictureTypeId(pictureTypeId);
                merchantAccountPictureModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_1);
                merchantAccountPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                merchantAccountPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                merchantAccountPictureModel.setCreatedOn(new Date());
                merchantAccountPictureModel.setUpdatedOn(new Date());
                this.commonCommandManager.saveOrUpdateMerchantAccountPictureModel(merchantAccountPictureModel);
            } else {
                merchantAccountPictureModel = new MerchantAccountPictureModel();
                pictureAsString = pictureAsString.replace(" ", "+");
                byte[] imageFileBytes = org.apache.commons.codec.binary.Base64.decodeBase64(pictureAsString.getBytes());

                merchantAccountPictureModel.setPicture(imageFileBytes);
                merchantAccountPictureModel.setPictureTypeId(pictureTypeId);
                merchantAccountPictureModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_1);
                merchantAccountPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                merchantAccountPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                merchantAccountPictureModel.setCreatedOn(new Date());
                merchantAccountPictureModel.setUpdatedOn(new Date());

            }
        } catch (Exception e) {
            logger.error("Unable to decode image bytes...", e);
            throw new CommandException(MessageUtil.getMessage("newMfsAccount.unknown"), ErrorCodes.FILE_NOT_FOUND, ErrorLevel.MEDIUM);
        }

        if (arrayCustomerPictures != null) {
            arrayCustomerPictures.add(merchantAccountPictureModel);
        }
    }

    @Override
    public String response() {
        this.logger.info("Start of UpgradeMerchantAccountCommand.toXML()");

        String message = "";
        message = "Account Upgraded Successfully";

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

        this.logger.info("End of UpgradeL2AccountCommand.toXML()");

        return xmlBuilder.toString();
    }
}
