package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.app.service.AppManager;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.BlinkCustomerModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.customermodule.BlinkCustomerPictureModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;

public class UpgradeL2AccountCommand extends BaseCommand {
    protected final Log logger = LogFactory.getLog(UpgradeL2AccountCommand.class);
    protected AppUserModel appUserModel;
    protected AppManager appManager;
    protected BaseWrapper preparedBaseWrapper;
    String cdob;
    String cnicIssuanceDate;
    String latitude;
    String longitude;
    String dualNationality;
    String usCitizen;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy");
    private String customerPhoto, cNicFrontPhoto, cNicBackPhoto, sourceofincomePic, proofOfProfession, cName, cMsisdn, cNic, cRegState, cRegStateId, transactionId;
    private ArrayList<BlinkCustomerPictureModel> arrayCustomerPictures = new ArrayList<BlinkCustomerPictureModel>();
    private CommonCommandManager commonCommandManager;
    private String birthPlace;
    private String gender;
    private String motherName;
    private String emailAddress;
    private String fatherName;
    private String presentAddress;
    private String permanentAddress;
    private String channelId;
    private String terminalId;
    private String fingerIndex;
    private String fingerTemplate;
    private String templateType;
    ////********HRA*************/////
    private String transactionPurpose;
    private String incomeSource;
    private String expectedMonthlyTurnover;
    private String nextOfKin;
    private String motherMedianName;

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
//        this.fingerIndex = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_FINGER_INDEX);
//        this.fingerTemplate = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_FINGER_TEMPLATE);
//        this.templateType = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TEMPLATE_TYPE);
        this.cName = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_NAME);
        this.fatherName = getCommandParameter(baseWrapper, "FATHER_HUSBND_NAME");
        this.gender = getCommandParameter(baseWrapper, "GENDER");
        this.cnicIssuanceDate = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CNIC_ISSUE_DATE);
        this.cdob = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CDOB);
        this.birthPlace = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BIRTH_PLACE);
        this.motherName = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOTHER_MAIDEN);
        this.emailAddress = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_EMAIL_ADDRESS);
        this.presentAddress = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PRESENT_ADDR);
        this.permanentAddress = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PERMANENT_ADDR);
        this.transactionPurpose = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TRANS_PURPOSE);
        this.incomeSource = getCommandParameter(baseWrapper, "INCOME_SOURCE");
        this.expectedMonthlyTurnover = getCommandParameter(baseWrapper, "EXPECTED_MONTHLY_TURNOVER");
        this.nextOfKin = getCommandParameter(baseWrapper, "NEXT_OF_KIN");
        this.cNicFrontPhoto = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CNIC_FRONT_PHOTO);
        this.cNicBackPhoto = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CNIC_BACK_PHOTO);
        this.customerPhoto = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_PHOTO);
        this.sourceofincomePic = getCommandParameter(baseWrapper, "INCOME_SOURCE_PIC");
        this.proofOfProfession = getCommandParameter(baseWrapper, "PROOF_OF_PROFESSION");
        this.motherMedianName = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOTHER_MAIDEN);
        this.signaturePicture = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_SIGNATURE_PHOTO);
        this.latitude = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_LATITUDE);
        this.longitude = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_LONGITUDE);
        this.dualNationality = getCommandParameter(baseWrapper, "DUAL_NATIONALITY");
        this.usCitizen = getCommandParameter(baseWrapper, "US_CITIZEN");

        this.logger.info("End of UpgradeL2AccountCommand.prepare()");

    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        this.logger.info("Start of UpgradeL2AccountCommand.validate()");
        validationErrors = new ValidationErrors();

        validationErrors = ValidatorWrapper.doRequired(this.cName, validationErrors, "Customer Name");

        validationErrors = ValidatorWrapper.doRequired(this.cMsisdn, validationErrors, "Customer Mobile No");
        validationErrors = ValidatorWrapper.doRequired(this.cNic, validationErrors, "CNIC");
        validationErrors = ValidatorWrapper.doRequired(this.deviceTypeId, validationErrors, "Device Type");
        validationErrors = ValidatorWrapper.doRequired(this.cdob, validationErrors, "Date of Birth");

        if (!validationErrors.hasValidationErrors()) {
            if (this.getCommonCommandManager().isCnicBlacklisted(this.cNic)) {
                validationErrors.getStringBuilder().append(MessageUtil.getMessage("walkinAccountBlacklisted"));
                throw new CommandException(validationErrors.getErrors(), ErrorCodes.TERMINATE_EXECUTION_FLOW, ErrorLevel.MEDIUM, new Throwable());
            }
        }
        if (validationErrors.hasValidationErrors()) {
            throw new CommandException(validationErrors.getErrors(), ErrorCodes.VALIDATION_ERROR, ErrorLevel.HIGH, new Throwable());
        }
        this.logger.info("End of UpgradeL2AccountCommand.validate()");

        return validationErrors;
    }

    @Override
    public void execute() throws CommandException {
        this.logger.info("Start of UpgradeL2AccountCommand.execute()");
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        BaseWrapper baseWrapper1 = new BaseWrapperImpl();

        try {
            Date nowDate = new Date();
            commonCommandManager = this.getCommonCommandManager();
            BlinkCustomerModel blinkCustomerModel = new BlinkCustomerModel();
            blinkCustomerModel = this.getCommonCommandManager().loadBlinkCustomerByMobileAndAccUpdate(cMsisdn, 1L);

            if (blinkCustomerModel == null) {
                AppUserModel appUserModel = this.commonCommandManager.loadAppUserByCnicAndType(this.cNic);
//            String[] nameArray = this.cName.split(" ");
//            appUserModel.setFirstName(nameArray[0]);
                if (appUserModel != null) {

                    CustomerModel customerModel = new CustomerModel();
                    customerModel = this.getCommonCommandManager().getCustomerModelById(appUserModel.getCustomerId());

//                if (customerModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_0)) {
//                    VerisysDataModel verisysDataModel = new VerisysDataModel();
//
//                    verisysDataModel.setCnic(appUserModel.getNic());
//                    verisysDataModel.setName(this.cName);
//                    verisysDataModel.setMotherMaidenName(this.motherName);
//                    verisysDataModel.setPlaceOfBirth(this.birthPlace);
//                    verisysDataModel.setCurrentAddress(appUserModel.getAddress1());
//                    verisysDataModel.setPermanentAddress(appUserModel.getAddress2());
//                    verisysDataModel.setTranslated(false);
//                    verisysDataModel.setCreatedOn(new Date());
//                    verisysDataModel.setUpdatedOn(new Date());
//                    verisysDataModel.setName(CommonUtils.escapeUnicode(verisysDataModel.getName()));
//                    if (appUserModel.getMotherMaidenName() != null)
//                        verisysDataModel.setMotherMaidenName(CommonUtils.escapeUnicode(verisysDataModel.getMotherMaidenName()));
//                    if (appUserModel.getAddress1() != null)
//                        verisysDataModel.setCurrentAddress(CommonUtils.escapeUnicode(verisysDataModel.getCurrentAddress()));
//                    if (customerModel.getBirthPlace() != null)
//                        verisysDataModel.setPlaceOfBirth(CommonUtils.escapeUnicode(verisysDataModel.getPlaceOfBirth()));
//                    verisysDataModel.setCnic(appUserModel.getNic());
//                    verisysDataModel.setAccountClosed(false);
//                    verisysDataModel.setAppUserId(appUserModel.getAppUserId());
//                    verisysDataModel.setCreatedOn(new Date());
//                    verisysDataModel.setUpdatedOn(new Date());
//                    verisysDataModel.setTranslated(false);
//                    if (appUserModel.getAddress2() != null)
//                        verisysDataModel.setPermanentAddress(CommonUtils.escapeUnicode(verisysDataModel.getPermanentAddress()));
//                    verisysDataModel.setAppUserId(appUserModel.getAppUserId());
//                    getCommonCommandManager().getVerisysDataHibernateDAO().saveNadraData(verisysDataModel);
//                }
//                    Date cdobYear = dateFormat.parse(cdob);
//                    I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
//                    I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
//                    esbAdapter = new ESBAdapter();
//                    String transmissionDateTime = new SimpleDateFormat("yyyyMMddHHss").format(new Date());
//                    String stan = String.valueOf((new Random().nextInt(90000000)));
//                    requestVO = esbAdapter.prepareCLSRequest(I8SBConstants.RequestType_CLSJS_ImportScreening);
//                    requestVO.setName(cName);
//                    requestVO.setCNIC(cNic);
//                    requestVO.setDateOfBirth(String.valueOf(cdobYear.getYear()));
//                    requestVO.setNationality("Pakistan");
//                    requestVO.setRequestId(transmissionDateTime + stan);
//                    requestVO.setMobileNumber(cMsisdn);
//
//                    SwitchWrapper sWrapper = new SwitchWrapperImpl();
//                    sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
//                    sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
//                    sWrapper = esbAdapter.makeI8SBCall(sWrapper);
//                    ESBAdapter.processI8sbResponseCode(sWrapper.getI8SBSwitchControllerResponseVO(), false);
//                    responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();
////                   responseVO.setResponseCode("I8SB-200");
//                    if (!responseVO.getResponseCode().equals("I8SB-200")) {
//                        throw new CommandException(responseVO.getDescription(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);
//                    } else {
//                        customerModel.setClsResponseCode(responseVO.getCaseStatus());
//                    }

                    blinkCustomerModel = new BlinkCustomerModel();
                    blinkCustomerModel.setMobileNo(cMsisdn);
                    blinkCustomerModel.setCnic(cNic);
                    blinkCustomerModel.setConsumerName(cName);
                    blinkCustomerModel.setFatherHusbandName(fatherName);
                    blinkCustomerModel.setGender(gender);
                    blinkCustomerModel.setCnicIssuanceDate(dateFormat1.parse(cnicIssuanceDate));
                    blinkCustomerModel.setDob(dateFormat1.parse(cdob));
                    blinkCustomerModel.setBirthPlace(birthPlace);
                    blinkCustomerModel.setMotherMaidenName(motherMedianName);
                    blinkCustomerModel.setEmailAddress(emailAddress);
                    blinkCustomerModel.setMailingAddress(presentAddress);
                    blinkCustomerModel.setPermanentAddress(permanentAddress);
                    blinkCustomerModel.setPurposeOfAccount(transactionPurpose);
                    blinkCustomerModel.setSourceOfIncome(incomeSource);
                    blinkCustomerModel.setExpectedMonthlyTurnOver(expectedMonthlyTurnover);
                    blinkCustomerModel.setNextOfKin(nextOfKin);
                    blinkCustomerModel.setLatitude(latitude);
                    blinkCustomerModel.setLongitude(longitude);
                    blinkCustomerModel.setDualNationality(dualNationality);
                    blinkCustomerModel.setUsCitizen(usCitizen);
                    blinkCustomerModel.setRegistrationStatus(BlinkCustomerRegistrationStateConstantsInterface.RQST_RCVD.toString());
                    blinkCustomerModel.setAccUpdate(1L);
                    blinkCustomerModel.setCustomerId(appUserModel.getCustomerId());
                    blinkCustomerModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                    blinkCustomerModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                    blinkCustomerModel.setUpdatedOn(new Date());
                    blinkCustomerModel.setCreatedOn(new Date());
//                    blinkCustomerModel.setClsStatus(responseVO.getCaseStatus());
//                    blinkCustomerModel.setPictureTypeId(1L);
                    blinkCustomerModel.setCustomerAccountTypeId(customerModel.getCustomerAccountTypeId());

                    getCommonCommandManager().createBlinkCustomerModel(blinkCustomerModel);

                    if (this.customerPhoto != null && !this.customerPhoto.equals("")) {
                        addCustomerPicture(PictureTypeConstants.CUSTOMER_PHOTO, arrayCustomerPictures, nowDate, this.customerPhoto, appUserModel.getCustomerId());
                    }

                    if (this.cNicFrontPhoto != null && !this.cNicFrontPhoto.equals("")) {
                        addCustomerPicture(PictureTypeConstants.ID_FRONT_SNAPSHOT, arrayCustomerPictures, nowDate, this.cNicFrontPhoto, appUserModel.getCustomerId());
                    }

                    if (this.cNicBackPhoto != null && !this.cNicBackPhoto.equals("")) {
                        addCustomerPicture(PictureTypeConstants.ID_BACK_SNAPSHOT, arrayCustomerPictures, nowDate, this.cNicBackPhoto, appUserModel.getCustomerId());
                    }

                    if (this.sourceofincomePic != null && !this.sourceofincomePic.equals("")) {
                        addCustomerPicture(PictureTypeConstants.SOURCE_OF_INCOME_SNAPSHOT, arrayCustomerPictures, nowDate, this.sourceofincomePic, appUserModel.getCustomerId());
                    }

                    if (this.proofOfProfession != null && !this.proofOfProfession.equals("")) {
                        addCustomerPicture(PictureTypeConstants.PROOF_OF_PROFESSION_SNAPSHOT, arrayCustomerPictures, nowDate, this.proofOfProfession, appUserModel.getCustomerId());
                    }
                    if (this.signaturePicture != null && !this.signaturePicture.equals("")) {
                        addCustomerPicture(PictureTypeConstants.SIGNATURE_SNAPSHOT, arrayCustomerPictures, nowDate, this.signaturePicture, appUserModel.getCustomerId());
                    }

                    for (BlinkCustomerPictureModel blinkCustomerPictureModel : arrayCustomerPictures) {
                        blinkCustomerPictureModel.setCustomerId(appUserModel.getCustomerId());
                    }

                    this.getCommonCommandManager().saveOrUpdateBlinkCustomerPictureModel(arrayCustomerPictures);

//                Boolean isAlreadyExists = this.getCommonCommandManager().getCustomerPictureDAO().isCustomerIdExists(appUserModel.getCustomerId());
//
//                if(isAlreadyExists){
//                    for (CustomerPictureModel customerPictureModel : arrayCustomerPictures) {
//                        this.getCommonCommandManager().getCustomerPictureDAO().updateCustomerPictureModel(customerPictureModel.getPictureTypeId(),
//                                customerPictureModel.getPicture(), customerPictureModel.getCustomerId());
//                    }
//                }
//                else{
//                    this.getCommonCommandManager().saveOrUpdateCustomerPictureModel(arrayCustomerPictures);
//                }
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

    private void addCustomerPicture(Long pictureTypeId, ArrayList<BlinkCustomerPictureModel> arrayCustomerPictures, Date nowDate, String pictureAsString, Long customerId) throws IOException, CommandException {
        BlinkCustomerPictureModel blinkCustomerPictureModel = new BlinkCustomerPictureModel();
        try {
            blinkCustomerPictureModel = commonCommandManager.getBlinkCustomerPictureByTypeId(pictureTypeId, customerId);

            if (blinkCustomerPictureModel != null) {
                pictureAsString = pictureAsString.replace(" ", "+");
                byte[] imageFileBytes = org.apache.commons.codec.binary.Base64.decodeBase64(pictureAsString.getBytes());

                blinkCustomerPictureModel.setPicture(imageFileBytes);
                blinkCustomerPictureModel.setPictureTypeId(pictureTypeId);
                blinkCustomerPictureModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_1);
                blinkCustomerPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                blinkCustomerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                blinkCustomerPictureModel.setCreatedOn(new Date());
                blinkCustomerPictureModel.setUpdatedOn(new Date());
                this.commonCommandManager.saveOrUpdateBlinkCustomerPictureModel(blinkCustomerPictureModel);
            } else {
                blinkCustomerPictureModel = new BlinkCustomerPictureModel();
                pictureAsString = pictureAsString.replace(" ", "+");
                byte[] imageFileBytes = org.apache.commons.codec.binary.Base64.decodeBase64(pictureAsString.getBytes());

                blinkCustomerPictureModel.setPicture(imageFileBytes);
                blinkCustomerPictureModel.setPictureTypeId(pictureTypeId);
                blinkCustomerPictureModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_1);
                blinkCustomerPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                blinkCustomerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                blinkCustomerPictureModel.setCreatedOn(new Date());
                blinkCustomerPictureModel.setUpdatedOn(new Date());

            }
        } catch (Exception e) {
            logger.error("Unable to decode image bytes...", e);
            throw new CommandException(MessageUtil.getMessage("newMfsAccount.unknown"), ErrorCodes.FILE_NOT_FOUND, ErrorLevel.MEDIUM);
        }

        if (arrayCustomerPictures != null) {
            arrayCustomerPictures.add(blinkCustomerPictureModel);
        }
    }

    @Override
    public String response() {
        this.logger.info("Start of UpgradeL2AccountCommand.toXML()");

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
