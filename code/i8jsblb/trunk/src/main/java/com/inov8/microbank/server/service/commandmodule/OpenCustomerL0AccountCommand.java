package com.inov8.microbank.server.service.commandmodule;

import com.inov8.common.util.RandomUtils;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.app.model.AppInfoModel;
import com.inov8.microbank.app.service.AppManager;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.customermodule.CustomerPictureModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.fonepay.common.FonePayConstants;
import com.inov8.microbank.nadraVerisys.model.VerisysDataModel;
import com.inov8.microbank.server.facade.CoreAdviceQueingPreProcessor;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import com.inov8.microbank.server.service.integration.vo.MiddlewareAdviceVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.transactionreversal.TransactionReversalManager;
import com.inov8.microbank.server.webserviceclient.ivr.IvrRequestDTO;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.web.context.ContextLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;

/**
 * @author Soofiafa
 */

public class OpenCustomerL0AccountCommand extends BaseCommand {
    protected final Log logger = LogFactory.getLog(OpenCustomerL0AccountCommand.class);
    protected AppUserModel appUserModel;
    protected AppManager appManager;
    private ESBAdapter esbAdapter;


    protected BaseWrapper preparedBaseWrapper;
    private String termsPhoto, signaturePhoto, customerPhoto, cNicFrontPhoto, cNicBackPhoto, cName, cMsisdn, cNic, cRegState, cRegStateId, transactionId;
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
    RetailerContactModel retailerContactModel;
    ////********HRA*************/////
    private String transactionPurpose;
    private String occupation;
    private String nextOfKinMobile;
    private ArrayList<CustomerRemitterModel> customerRemitterModelList = new ArrayList<CustomerRemitterModel>();
    ////************************/////
    private String segmentId;
    private String customerMobileNetwork;
    private TransactionReversalManager transactionReversalManager;
    private CoreAdviceQueingPreProcessor coreAdviceQueingPreProcessor;

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    WorkFlowWrapper workFlowWrapper;
    private String successMessage;

    private Date dateOfBirth;
    private Date cnicExpiryDate;

    public void prepare(BaseWrapper baseWrapper) {
        if (this.logger.isDebugEnabled())
            this.logger.debug("Start of OpenCustomerL0AccountCommand.prepare()");

        this.cNic = getCommandParameter(baseWrapper, "CNIC");
        try {
            Long[] appUserTypes = new Long[]{UserTypeConstantsInterface.CUSTOMER};
            appUserModel = getCommonCommandManager().loadAppUserByCNICAndAccountType(this.cNic, appUserTypes);
            /*appUserModel = getCommonCommandManager().getAppUserModelByCNIC(this.cNic);*/
            if (appUserModel != null && appUserModel.getRegistrationStateId() != null)
                this.cRegStateId = appUserModel.getRegistrationStateId().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (appUserModel == null || appUserModel.getAppUserId() == null || cRegStateId == null) {
            this.customerMobileNetwork = getCommandParameter(baseWrapper, "CUST_MOB_NETWORK");
            this.termsPhoto = getCommandParameter(baseWrapper, "TERMS_PHOTO");
            this.signaturePhoto = getCommandParameter(baseWrapper, "SIGNATURE_PHOTO");
            this.customerPhoto = getCommandParameter(baseWrapper, "CUSTOMER_PHOTO");
            this.cNicFrontPhoto = getCommandParameter(baseWrapper, "CNIC_FRONT_PHOTO");
            this.cNicBackPhoto = getCommandParameter(baseWrapper, "CNIC_BACK_PHOTO");
            this.l1FormPhoto = getCommandParameter(baseWrapper, "L1_FORM_PHOTO");
            this.isCnicSeen = getCommandParameter(baseWrapper, "IS_CNIC_SEEN");

            this.cName = getCommandParameter(baseWrapper, "CNAME");
            this.cMsisdn = getCommandParameter(baseWrapper, "CMOB");
            this.cRegState = getCommandParameter(baseWrapper, "CREG_STATE");
            this.cRegStateId = getCommandParameter(baseWrapper, "CREG_STATE_ID");

            this.birthPlace = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BIRTH_PLACE);
            this.motherName = getCommandParameter(baseWrapper, "MOTHER_MAIDEN");
            this.presentAddress = getCommandParameter(baseWrapper, "PRESENT_ADDR");
            this.permanentAddress = getCommandParameter(baseWrapper, "PERMANENT_ADDR");
            this.depositAmountFlag = getCommandParameter(baseWrapper, "DEPOSIT_AMT_FLAG");
            this.gender = getCommandParameter(baseWrapper, "GENDER");
            this.segmentId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_SEGMENT_ID);

        /*Map<String, String> nadraMap = MiniXMLUtil.nadraMap.get(this.cNic);
        if (null != nadraMap) {
            this.cName = nadraMap.get("NAME");
            this.permanentAddress = nadraMap.get("PERMANENT_ADDR");
            this.motherName = nadraMap.get("MOTHER_MAIDEN");
            this.presentAddress = nadraMap.get("PRESENT_ADDR");
            this.birthPlace = nadraMap.get("BIRTH_PLACE");
        }*/

            if (null != getCommandParameter(baseWrapper, "IS_BVS_ACCOUNT") && !"".equals(getCommandParameter(baseWrapper, "IS_BVS_ACCOUNT"))) {
                this.isBvsAccount = getCommandParameter(baseWrapper, "IS_BVS_ACCOUNT").equals("1") ? true : false;
            } else {
                this.isBvsAccount = false;
            }

            if (this.cRegStateId != null && this.cRegStateId.equals("null")) {
                this.cRegStateId = null;
            }

            this.customerAccountyType = getCommandParameter(baseWrapper, "CUST_ACC_TYPE");
            this.cdob = getCommandParameter(baseWrapper, "CDOB");
            this.cnicExpiry = getCommandParameter(baseWrapper, "CNIC_EXP");
            if (StringUtil.isNullOrEmpty(this.cnicExpiry)) {
                this.cnicExpiry = "2020-01-01";
            } else {
                if (this.cnicExpiry.equals("Lifetime")) {
                    this.cnicExpiry = "2099-01-01";
                }
            }

            ///************************HRA***********************************///
            if (getCommandParameter(baseWrapper, "IS_HRA").equals("1")) {
                this.isHRA = true;
                this.isBvsAccount = true;
            } else {
                this.isHRA = false;
            }
            //this.isHRA =  this.isBvsAccount = getCommandParameter(baseWrapper, "IS_HRA").equals("1") ? true : false;
            this.transactionPurpose = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TRANS_PURPOSE);
            this.occupation = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_OCCUPATION);
            this.nextOfKinMobile = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_NEXT_OF_KIN_MOB);

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
                    customerRemitterModelList.add(customerRemitterModel);
                }
            }

            ///*************************HRA***********************************//


            this.depositAmount = getCommandParameter(baseWrapper, "DEPOSIT_AMT");

            this.preparedBaseWrapper = baseWrapper;
            this.deviceTypeId = getCommandParameter(baseWrapper, "DTID");

            encryptionType = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ENCRYPTION_TYPE);
            agentMobileNo = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AGENT_MOBILE);
            agentPIN = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);

            /**
             * added by muhammad atif
             */
            this.transactionId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_ID);
            //Load again app user because previous app user from thread local has been changed in FonePayOTPVerification Command
            if (ThreadLocalAppUser.getAppUserModel().getAppUserId() == PortalConstants.WEB_SERVICE_APP_USER_ID) {
                try {
                    appUserModel = getCommonCommandManager().loadAppUserByMobileAndType(agentMobileNo);
                } catch (FrameworkCheckedException e) {
                    e.printStackTrace();
                }

                if (appUserModel != null) {
                    logger.debug("[FonepayPaymentCommand.execute] AppUserModel loader wil AppUserId:" + appUserModel.getAppUserId());
                    ThreadLocalAppUser.setAppUserModel(appUserModel);
                }
            } else {
                appUserModel = ThreadLocalAppUser.getAppUserModel();
            }
            BaseWrapper bWrapper = new BaseWrapperImpl();
            RetailerContactModel retailerContactModel = new RetailerContactModel();
            retailerContactModel.setRetailerContactId(appUserModel.getRetailerContactId());

            bWrapper.setBasePersistableModel(retailerContactModel);

            try {
                bWrapper = this.getCommonCommandManager().loadRetailerContact(bWrapper);
                this.retailerContactModel = (RetailerContactModel) bWrapper.getBasePersistableModel();

            } catch (Exception ex) {
                logger.error("[OpenCustomerL0AccountCommand.prepare] Unable to load RetailerContact info... " + ex.getMessage(), ex);
            }

        } else {
            this.cName = getCommandParameter(baseWrapper, "CNAME");
            this.birthPlace = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BIRTH_PLACE);
            this.motherName = getCommandParameter(baseWrapper, "MOTHER_MAIDEN");

        }
        if (motherName != null && motherName.isEmpty()) {
            motherName = "Mother";
        }

        if (this.logger.isDebugEnabled())
            this.logger.debug("End of OpenCustomerL0AccountCommand.prepare()");
    }

    @Override
    public void doValidate() throws CommandException {
        if (this.logger.isDebugEnabled())
            this.logger.debug("Start of OpenCustomerL0AccountCommand.doValidate()");

        ValidationErrors validationErrors = new ValidationErrors();
        if (appUserModel != null && appUserModel.getRegistrationStateId() != null && appUserModel.getRegistrationStateId().equals(RegistrationStateConstantsInterface.VERIFIED)) {
            validationErrors = ValidatorWrapper.doRequired(this.cNic, validationErrors, "CNIC");
            validationErrors = ValidatorWrapper.doValidateCNIC(this.cNic, validationErrors, "CNIC");
        } else if (appUserModel == null) {
            validationErrors = ValidatorWrapper.doRequired(this.customerMobileNetwork, validationErrors, "Customer Mobile Network");
            validationErrors = ValidatorWrapper.doRequired(this.customerAccountyType, validationErrors, "Customer Account Type");

            validationErrors = ValidatorWrapper.doRequired(this.cName, validationErrors, "Customer Name");

            validationErrors = ValidatorWrapper.doRequired(this.cMsisdn, validationErrors, "Customer Mobile No");
            validationErrors = ValidatorWrapper.doRequired(this.cNic, validationErrors, "CNIC");
            validationErrors = ValidatorWrapper.doValidateCNIC(this.cNic, validationErrors, "CNIC");
            validationErrors = ValidatorWrapper.doRequired(this.deviceTypeId, validationErrors, "Device Type");


            validationErrors = ValidatorWrapper.doRequired(this.cdob, validationErrors, "Date of Birth");
            validationErrors = ValidatorWrapper.doRequired(this.cnicExpiry, validationErrors, "CNIC Expiry");
            validationErrors = ValidatorWrapper.doRequired(this.isCnicSeen, validationErrors, "Is CNIC Seen");

            if (customerAccountyType.equals(String.valueOf(CustomerAccountTypeConstants.LEVEL_1))) {
                validationErrors = ValidatorWrapper.doRequired(String.valueOf(this.isBvsAccount), validationErrors, "Is BVS_ACCOUNT");
            }


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
            if (transactionId == null || transactionId.trim().length() == 0) {
                transactionId = null;// if transactionId is empty string.
                //validationErrors = ValidatorWrapper.doRequired(this.depositAmount,validationErrors, "Initial Deposit");
            }

            ValidatorWrapper.doRequired(encryptionType, validationErrors, "Encryption Type");
            ValidatorWrapper.doRequired(agentMobileNo, validationErrors, "Agent Mobile No");
            ValidatorWrapper.doRequired(agentPIN, validationErrors, "Product Id");
            //ValidatorWrapper.doNumeric(depositAmount, validationErrors, "Initial Deposit");

            if (!validationErrors.hasValidationErrors()) {
                if (!this.isBvsAccount && (this.cRegStateId == null || this.cRegStateId.equals(String.valueOf(RegistrationStateConstants.BULK_REQUEST_RECEIVED)))) {
                    //validationErrors = ValidatorWrapper.doRequired(this.cNicBackPhoto,validationErrors, "CNIC Back Photo");
                    validationErrors = ValidatorWrapper.doRequired(this.cNicFrontPhoto, validationErrors, "CNIC Front Photo");
                    //validationErrors = ValidatorWrapper.doRequired(this.termsPhoto,validationErrors, "Terms Photo");
                    validationErrors = ValidatorWrapper.doRequired(this.customerPhoto, validationErrors, "Customer Photo");
                    //validationErrors = ValidatorWrapper.doRequired(this.signaturePhoto,validationErrors, "Signature Photo");

                    if (this.customerAccountyType.equals(String.valueOf(CustomerAccountTypeConstants.LEVEL_1))) {
                        validationErrors = ValidatorWrapper.doRequired(this.l1FormPhoto, validationErrors, "L1 Form Photo");
                    }
                }
            }
        }
//		validationErrors = ValidatorWrapper.doRequired(this.motherName,validationErrors,"Mother Name");

        if (validationErrors.hasValidationErrors()) {
            throw new CommandException(validationErrors.getErrors(), ErrorCodes.VALIDATION_ERROR, ErrorLevel.HIGH, new Throwable());
        }

        if (this.logger.isDebugEnabled())
            this.logger.debug("End of OpenCustomerL0AccountCommand.doValidate()");
    }

    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        return new ValidationErrors();
    }

    private Long loadMobileNetworkId(String mobileNetwork) {
        Long mobileNetworkId = null;
        if (mobileNetwork.equalsIgnoreCase(MobileNetworkTypeConstantsInterface.ZONG_NAME))
            mobileNetworkId = MobileNetworkTypeConstantsInterface.ZONG;
        else if (mobileNetwork.equalsIgnoreCase(MobileNetworkTypeConstantsInterface.TELENOR_NAME))
            mobileNetworkId = MobileNetworkTypeConstantsInterface.TELENOR;
        else if (mobileNetwork.equalsIgnoreCase(MobileNetworkTypeConstantsInterface.MOBILINK_NAME))
            mobileNetworkId = MobileNetworkTypeConstantsInterface.MOBILINK;
        else if (mobileNetwork.equalsIgnoreCase(MobileNetworkTypeConstantsInterface.UFONE_NAME))
            mobileNetworkId = MobileNetworkTypeConstantsInterface.UFONE;
        else if (mobileNetwork.equalsIgnoreCase(MobileNetworkTypeConstantsInterface.WARID_NAME))
            mobileNetworkId = MobileNetworkTypeConstantsInterface.WARID;
        return mobileNetworkId;
    }

    public void execute() throws CommandException {
        if (this.logger.isDebugEnabled())
            this.logger.debug("Start of OpenCustomerL0AccountCommand.execute()");

        VerisysDataModel verisysDataModel = new VerisysDataModel();

        BaseWrapper baseWrapper = new BaseWrapperImpl();
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
        if (cdob != null) {
            try {
                SimpleDateFormat formatter1 = new SimpleDateFormat("d");
                SimpleDateFormat formatter2 = new SimpleDateFormat("M");
                SimpleDateFormat formatter3 = new SimpleDateFormat("yyyy");

                try {
                    dateOfBirth = dateFormat.parse(cdob);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String d = formatter1.format(dateOfBirth);
                String m = formatter2.format(dateOfBirth);
                String y = formatter3.format(dateOfBirth);
                System.out.println(y + "," + m + "," + d);
                CommonUtils.checkAgeLimit(y, m, d, 18);
            } catch (CommandException e) {
                throw new CommandException(MessageUtil.getMessage("MINOR.ACCOUNT.OPENING"), ErrorCodes.AGELIMIT_ACCOUNT_BLOCK, ErrorLevel.MEDIUM);
            }

            try {
                CommonUtils.checkCnicExpiry(cnicExpiry);
            } catch (CommandException e) {
                throw new CommandException(MessageUtil.getMessage("CNIC.EXPIRE.ACCOUNT.OPENING"), ErrorCodes.AGELIMIT_ACCOUNT_BLOCK, ErrorLevel.MEDIUM);
            }
        }


        try {
            if (isHRA && !isBvsAccount) {
                throw new CommandException("HRA Account Opening is not allowed for Level 0", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
            }

//		String transmissionDateTime = new SimpleDateFormat("yyyyMMddHHss").format(new Date());
//		String stan = String.valueOf((new Random().nextInt(90000000)));
//		this.esbAdapter = new ESBAdapter();
//		requestVO = ESBAdapter.prepareCLSRequest(I8SBConstants.RequestType_CLSJS_ImportScreening);
//		requestVO.setName(cName);
//		requestVO.setCNIC(cNic);
//		requestVO.setDateOfBirth(cdob);
//		requestVO.setNationality("Pakistan");
//		requestVO.setRequestId(transmissionDateTime + stan);
//		requestVO.setMobileNumber(cMsisdn);
//		requestVO.setCity(birthPlace);
//
//		SwitchWrapper sWrapper = new SwitchWrapperImpl();
//		sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
//		sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
//		sWrapper = esbAdapter.makeI8SBCall(sWrapper);
//		responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();
//
//		if (!responseVO.getResponseCode().equals("I8SB-200"))
//			throw new CommandException(responseVO.getDescription(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);

            //Added by Sheheryaar Nawaz
            if (transactionId != null && transactionId.equals(""))
                transactionId = null;
            //added by muhammad atif
            //if pending transactions exist, then dont allow account opening
            commonCommandManager = this.getCommonCommandManager();
            int pendingTransactionCount = commonCommandManager.countCustomerPendingTrx(this.cNic);

            if (transactionId != null && !transactionId.equals("") && pendingTransactionCount == 0) //  Receive Cash Flow
            {
                throw new CommandException("Customer has no pending transactions.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
            } else if ((transactionId == null || transactionId.equals("")) && pendingTransactionCount > 0) //  Normal Flow
            {
                throw new CommandException("Customer has pending transactions.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
            } else if (transactionId != null && !transactionId.equals("") && pendingTransactionCount == 0) //  Receive Cash Flow
            {
                throw new CommandException("Customer has no pending transactions.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
            }

            if (this.cMsisdn != null && !this.cMsisdn.equals("")) {
                pendingTransactionCount = commonCommandManager.countCustomerPendingTrxByMobile(this.cMsisdn);

                if (transactionId != null && !transactionId.equals("") && pendingTransactionCount == 0) //  Receive Cash Flow
                {
                    throw new CommandException("Customer has no pending transactions.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                } else if (transactionId == null && pendingTransactionCount > 0) //  Normal Flow
                {
                    throw new CommandException("Customer has pending transactions.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                }
			/*else if(transactionId == null && !transactionId.equals("") && pendingTransactionCount == 0) //  Receive Cash Flow
			{
				throw new CommandException("Customer has no pending transactions.", ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
			}*/
            }
            //end of added by muhammad atif

            workFlowWrapper = new WorkFlowWrapperImpl();
            workFlowWrapper.setHandlerModel(handlerModel);
            workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);

            nowDate = new Date();
            UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
            // -------------------------------------------------------------------------------------
            /***************************************************************************************
             // -------------------------------------------------------------------------------------
             /*
             * Populating the Customer Picture Model
             */
            // Setting Images
            try {

                if (this.cRegStateId == null || this.cRegStateId.equals(String.valueOf(RegistrationStateConstants.BULK_REQUEST_RECEIVED))) {
                    /*added by muhammad atif*/
                    if (transactionId == null) // Normal Flow
                    {

                        Double agentBalanceThreshold = 0.0D;

                        if (depositAmountFlag.equals("1")) {
                            //Calculate Commission - start
                            agentBalanceThreshold = Double.valueOf(depositAmount);

                            try {
                                workFlowWrapper.setRetailerContactModel(retailerContactModel);

                                SegmentModel segmentModel = new SegmentModel();
                                if (segmentId != null) {
                                    this.logger.info("Segment Id is: " + this.segmentId);
                                    segmentModel.setSegmentId(Long.valueOf(segmentId));
                                } else {
                                    segmentModel.setSegmentId(CommissionConstantsInterface.DEFAULT_SEGMENT_ID);
                                }
                                workFlowWrapper.setSegmentModel(segmentModel);

                                ProductModel productModel = new ProductModel();
                                productModel.setProductId(ProductConstantsInterface.ACCOUNT_OPENING);
                                productModel.setServiceId(ServiceConstantsInterface.PAYMENT_SERVICE);
                                workFlowWrapper.setProductModel(productModel);

                                TransactionModel transactionModel = new TransactionModel();
                                transactionModel.setTransactionAmount(Double.valueOf(depositAmount));
                                workFlowWrapper.setTransactionModel(transactionModel);

                                DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
                                deviceTypeModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);
                                workFlowWrapper.setDeviceTypeModel(deviceTypeModel);


                                workFlowWrapper.setTaxRegimeModel(retailerContactModel.getTaxRegimeIdTaxRegimeModel());

                                CommissionWrapper commissionWrapper = this.getCommonCommandManager().calculateCommission(workFlowWrapper);
                                CommissionAmountsHolder commissionAmountsHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
                                agentBalanceThreshold = commissionAmountsHolder.getTotalAmount() - commissionAmountsHolder.getAgent1CommissionAmount();
                            } catch (Exception ex) {
                                logger.error("Unable to calculateCommission for OpenCustomerL0AccountCommand: " + ex.getMessage(), ex);
                            }
                        }

                        //Calculate Commission - end

                        SwitchWrapper switchWrapper = this.getCommonCommandManager().checkAgentBalance();
                        Double balance = switchWrapper.getBalance();

                        if (balance == null || balance <= agentBalanceThreshold.doubleValue()) {
                            logger.error("[OpenCusomerL0AccountCommand.execute] Your balance in insufficient to make this transaction.");
                            throw new CommandException(this.getMessageSource().getMessage(WorkFlowErrorCodeConstants.INSUFFICIENT_BALANCE, null, null), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());

                        }
                    }


                    AppUserModel appUserModel = UserUtils.getCurrentUser();
                    try {
                        AccountInfoModel accountInfoModel = new AccountInfoModel();
                        accountInfoModel.setOldPin(this.agentPIN);
                        workFlowWrapper.setAccountInfoModel(accountInfoModel);

                        //this.commonCommandManager.verifyPIN(appUserModel, this.agentPIN, workFlowWrapper);
                    } catch (Exception ex) {
                        logger.error("[OpenCusomerL0AccountCommand.execute] " + ex.getMessage());
                        throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                    }

                    SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
                    UserDeviceAccountsModel userDeviceAccount = new UserDeviceAccountsModel();
                    userDeviceAccount.setAppUserId(appUserModel.getAppUserId());
                    searchBaseWrapper.setBasePersistableModel(userDeviceAccount);
                    try {
                        this.commonCommandManager.checkLogin(searchBaseWrapper);
                        List<UserDeviceAccountsModel> list = searchBaseWrapper.getCustomList().getResultsetList();

                        if (list != null && list.size() > 0) {
                            userDeviceAccount = list.get(0);

                            // [11 Feb 2015 OmarButt ]: Handler check is added to stop validating parent agent's UserDeviceAcc / SmartMoneyAccount health (PIN change required/ expired etc)
                            if (userDeviceAccount.getAccountExpired() && handlerModel == null) {
                                throw new CommandException(this.getMessageSource().getMessage("LoginCommand.accountExpired", null, null), ErrorCodes.INVALID_USER, ErrorLevel.MEDIUM, new Throwable());
                            }
                            if (userDeviceAccount.getAccountLocked() && handlerModel == null) {
                                throw new CommandException(this.getMessageSource().getMessage("LoginCommand.accountLocked", null, null), ErrorCodes.INVALID_USER, ErrorLevel.MEDIUM, new Throwable());
                            }
                            if (userDeviceAccount.getCredentialsExpired() && handlerModel == null) {
                                throw new CommandException(this.getMessageSource().getMessage("LoginCommand.credentialsExpired", null, null), ErrorCodes.INVALID_USER, ErrorLevel.MEDIUM, new Throwable());
                            }
                            if (!userDeviceAccount.getAccountEnabled()) {
                                throw new CommandException(this.getMessageSource().getMessage("LoginCommand.accountDisabled", null, null), ErrorCodes.INVALID_USER, ErrorLevel.MEDIUM, new Throwable());
                            }
                            if (userDeviceAccount.getPinChangeRequired() && handlerModel == null) {
                                throw new CommandException(this.getMessageSource().getMessage("openCustomerL0AccountCommand.LoginChangeIsRequired", null, null), ErrorCodes.INVALID_USER, ErrorLevel.MEDIUM, new Throwable());
                            }
                        }

                        // [11 Feb 2015 OmarButt ]: Handler check is added to stop validating parent agent's UserDeviceAcc / SmartMoneyAccount health (PIN change required/ expired etc)
                        if (handlerModel == null) {

                            SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
                            smartMoneyAccountModel.setRetailerContactId(appUserModel.getRetailerContactId());
                            smartMoneyAccountModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
                            smartMoneyAccountModel.setActive(Boolean.TRUE);
                            smartMoneyAccountModel.setDeleted(Boolean.FALSE);
                            searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);

                            searchBaseWrapper = this.commonCommandManager.loadSmartMoneyAccount(searchBaseWrapper);
                            smartMoneyAccountModel = (SmartMoneyAccountModel) searchBaseWrapper.getCustomList().getResultsetList().get(0);
                            if (smartMoneyAccountModel.getChangePinRequired()) {
                                throw new CommandException(this.getMessageSource().getMessage("openCustomerL0AccountCommand.MPINChangeIsRequired", null, null), ErrorCodes.INVALID_USER, ErrorLevel.MEDIUM, new Throwable());
                            }
                        }
                    } catch (Exception ex) {
                        logger.error("[OpenCusomerL0AccountCommand.execute] " + ex.getMessage());
                        throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                    }

                } else if (this.cRegStateId.equals(RegistrationStateConstantsInterface.VERIFIED.toString()) || this.cRegStateId.equals(RegistrationStateConstants.CLSPENDING.toString()))//&& customerAccountyType.equals(CustomerAccountTypeConstants.LEVEL_0)
                {
                    BaseWrapper baseWrapper1 = new BaseWrapperImpl();
                    AppUserModel appUserModel = this.commonCommandManager.loadAppUserByCnicAndType(this.cNic);

                    appUserModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_COLD);
                    appUserModel.setAccountEnabled(Boolean.TRUE);

                    baseWrapper1.putObject(CommandFieldConstants.KEY_APP_USER_MODEL, appUserModel);

                    Long customerId = appUserModel.getCustomerId();

                    CustomerModel customerModel = this.commonCommandManager.getCustomerModelById(customerId);
                    if (customerModel.getSegmentId().equals(Long.valueOf(MessageUtil.getMessage("Minor_segment_id"))))
                        throw new CommandException("Minor Account Cannot be Upgerade to L1", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                    customerModel.setCustomerAccountTypeId(CustomerAccountTypeConstants.LEVEL_1);

                    baseWrapper1.putObject(CommandFieldConstants.KEY_CUSTOMER_MODEL, customerModel);

                    SmartMoneyAccountModel smartMoneyAccountModel = this.commonCommandManager.getSmartMoneyAccountByAppUserModelAndPaymentModId(appUserModel
                            , PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
                    smartMoneyAccountModel.setRegistrationStateId(RegistrationStateConstants.VERIFIED);
                    smartMoneyAccountModel.setStatusId(OlaStatusConstants.ACCOUNT_STATUS_ACTIVE);
                    smartMoneyAccountModel.setAccountClosedSetteled(0L);
                    smartMoneyAccountModel.setAccountClosedUnsetteled(0L);
                    smartMoneyAccountModel.setUpdatedBy(appUserModel.getAppUserId());

                    baseWrapper1.setBasePersistableModel(smartMoneyAccountModel);

                    verisysDataModel.setCnic(appUserModel.getNic());
                    verisysDataModel.setName(this.cName);
                    verisysDataModel.setMotherMaidenName(this.motherName);
                    verisysDataModel.setPlaceOfBirth(this.birthPlace);
                    verisysDataModel.setCurrentAddress(appUserModel.getAddress1());
                    verisysDataModel.setPermanentAddress(appUserModel.getAddress2());
                    verisysDataModel.setTranslated(false);
                    verisysDataModel.setCreatedOn(new Date());
                    verisysDataModel.setUpdatedOn(new Date());

                    baseWrapper1.putObject(CommandFieldConstants.KEY_VARISYS_DATA_MODEL, verisysDataModel);

                    this.commonCommandManager.saveOrUpdateCustomerAccountL0ToL1(baseWrapper1);

                    String msgToText = "Your Account has been Upgraded From L0 to L1 successfully.";
                    BaseWrapper msgBaseWrapper = new BaseWrapperImpl();
                    msgBaseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, new SmsMessage(appUserModel.getMobileNo(), msgToText));
                    commonCommandManager.sendSMSToUser(msgBaseWrapper);

                    successMessage = "success";
                    return;
                }
                //**********************************************************************************************//
                //*****************CNIC Bank, Terms and L1 Form photo removed in new discussion*****************//
                //**********************************************************************************************//

                arrayCustomerPictures = new ArrayList<CustomerPictureModel>();
                if (customerAccountyType.equals(String.valueOf(CustomerAccountTypeConstants.LEVEL_0))) {
				
	           /* if(this.termsPhoto != null && !this.termsPhoto.equals("")) {
	                addCustomerPicture(PictureTypeConstants.TERMS_AND_CONDITIONS_COPY, this.termsPhoto, false);
	            }else {
	                if (this.cRegStateId == null || !this.cRegStateId.equals(RegistrationStateConstants.DISCREPANT.toString())) {
	                    addCustomerPicture(PictureTypeConstants.TERMS_AND_CONDITIONS_COPY, this.termsPhoto, true);
	                }
	            }*/
                    if (this.customerPhoto != null && !this.customerPhoto.equals("")) {
                        addCustomerPicture(PictureTypeConstants.CUSTOMER_PHOTO, this.customerPhoto, false);
                    } else {
                        if (this.cRegStateId == null || !this.cRegStateId.equals(RegistrationStateConstants.DISCREPANT.toString())) {
                            addCustomerPicture(PictureTypeConstants.CUSTOMER_PHOTO, this.customerPhoto, true);
                        }
                    }
	            /* if(this.signaturePhoto != null && !this.signaturePhoto.equals("")) {
	                addCustomerPicture(PictureTypeConstants.SIGNATURE_SNAPSHOT, this.signaturePhoto, false);
	            }else {
	                if (this.cRegStateId == null || !this.cRegStateId.equals(RegistrationStateConstants.DISCREPANT.toString())) {
	                    addCustomerPicture(PictureTypeConstants.SIGNATURE_SNAPSHOT, this.signaturePhoto, true);
	                }
	            }*/
                    if (this.cNicFrontPhoto != null && !this.cNicFrontPhoto.equals("")) {
                        addCustomerPicture(PictureTypeConstants.ID_FRONT_SNAPSHOT, this.cNicFrontPhoto, false);
                    } else {
                        if (this.cRegStateId == null || !this.cRegStateId.equals(RegistrationStateConstants.DISCREPANT.toString())) {
                            addCustomerPicture(PictureTypeConstants.ID_FRONT_SNAPSHOT, this.cNicFrontPhoto, true);
                        }
                    }
	            /*if(this.cNicBackPhoto != null && !this.cNicBackPhoto.equals("")) {
	                addCustomerPicture(PictureTypeConstants.ID_BACK_SNAPSHOT, this.cNicBackPhoto, false);
	            }else {
	                if (this.cRegStateId == null || !this.cRegStateId.equals(RegistrationStateConstants.DISCREPANT.toString())) {
	                    addCustomerPicture(PictureTypeConstants.ID_BACK_SNAPSHOT, this.cNicBackPhoto, true);
	                }
	            }
	            if(this.l1FormPhoto != null && !this.l1FormPhoto.equals("")) {
	                addCustomerPicture(PictureTypeConstants.AC_OPENING_FORM_SNAPSHOT, this.l1FormPhoto, false);
	            }else {
	                if (this.cRegStateId == null || !this.cRegStateId.equals(RegistrationStateConstants.DISCREPANT.toString())) {
	                    addCustomerPicture(PictureTypeConstants.AC_OPENING_FORM_SNAPSHOT, this.l1FormPhoto, true);
	                }
	            }*/
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_PICTURES_COLLECTION, arrayCustomerPictures);

            /***************************************************************************************
             * Populating the Customer Model
             */
            CustomerModel customerModel = new CustomerModel();
            customerModel.setRegister(true);
            customerModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
            customerModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
            customerModel.setCreatedOn(nowDate);
            customerModel.setUpdatedOn(nowDate);

            if (isHRA) {
                customerModel.setCustomerAccountTypeId(CustomerAccountTypeConstants.HRA);
            } else {
                customerModel.setCustomerAccountTypeId(Long.valueOf(this.customerAccountyType));
            }

            customerModel.setApplicationN0(commonCommandManager.getDeviceApplicationNoGenerator().nextLongValue().toString());
            customerModel.setContactNo(this.cMsisdn);
            customerModel.setName(this.cName);
            customerModel.setMobileNo(this.cMsisdn);
            customerModel.setRelationAskari(0);
            customerModel.setRelationZong(0);
            customerModel.setInitialDeposit(depositAmount);
            customerModel.setBirthPlace(this.birthPlace);

            if (isHRA) {
                customerModel.setSegmentId(CommissionConstantsInterface.HRA_SEGMENT_ID);
            } else if (segmentId != null) {
                logger.info("Segment Id is: " + segmentId);
                customerModel.setSegmentId(Long.valueOf(segmentId));
            } else {
                logger.info("Segment Id is: " + segmentId);
                customerModel.setSegmentId(CommissionConstantsInterface.DEFAULT_SEGMENT_ID);
            }


            customerModel.setCustomerTypeId(CustomerTypeConstants.CUSTOMER_TYPE_MARKETED);
            customerModel.setIsCnicSeen(this.isCnicSeen.equals("1") ? Boolean.TRUE : Boolean.FALSE);
            customerModel.setScreeningPerformed(Boolean.FALSE);
            customerModel.setIsMPINGenerated(Boolean.FALSE);
            customerModel.setWebServiceEnabled(true);

            //***************Setting HRA Values******************//
            if (isHRA) {
                customerModel.setHraOccupation(this.occupation);
                customerModel.setHraNokMob(this.nextOfKinMobile);
                customerModel.setHraTrxnPurpose(this.transactionPurpose);

                baseWrapper.putObject(CommandFieldConstants.CUSTOMER_REMITTENCE_KEY, customerRemitterModelList);

            }
            //***************************************************//

            if (isBvsAccount) {
                customerModel.setVerisysDone(true);
                customerModel.setAccountMethodId(AccountOpeningMethodConstantsInterface.BVS);
            } else {
                customerModel.setVerisysDone(false);
                customerModel.setAccountMethodId(AccountOpeningMethodConstantsInterface.CONVENTIONAL);
            }
            if (this.gender.toUpperCase().equals("MALE") || this.gender.toUpperCase().equals("M")) {
                customerModel.setGender("M");
            } else if (this.gender.toUpperCase().equals("FEMALE") || this.gender.toUpperCase().equals("F")) {
                customerModel.setGender("F");
            }


            customerModel.setTaxRegimeIdTaxRegimeModel(retailerContactModel.getTaxRegimeIdTaxRegimeModel());
            baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MODEL, customerModel);


            /***************************************************************************************
             * Populating the AppUserModel Model
             */
            AppUserModel appUserModel = new AppUserModel();
            if (this.cRegStateId != null && this.cRegStateId.equals(String.valueOf(RegistrationStateConstants.DISCREPANT))) {
                appUserModel = getCommonCommandManager().getAppUserWithRegistrationState(this.cMsisdn, this.cNic.replace("-", ""), RegistrationStateConstants.BULK_REQUEST_RECEIVED);

            }
            String[] nameArray = this.cName.split(" ");
            appUserModel.setFirstName(nameArray[0]);
            if (nameArray.length > 1) {
                appUserModel.setLastName(this.cName.substring(
                        appUserModel.getFirstName().length() + 1));
            } else {
                appUserModel.setLastName(nameArray[0]);
            }
            appUserModel.setAddress1(" ");
            appUserModel.setAddress2(" ");
            if (isBvsAccount == true) {
                appUserModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_COLD);
            } else {
                appUserModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_WARM);
            }

            appUserModel.setCustomerMobileNetwork(customerMobileNetwork);
            appUserModel.setMobileNetworkId(this.loadMobileNetworkId(customerMobileNetwork));

            appUserModel.setMobileNo(this.cMsisdn);
            String nicWithoutHyphins = this.cNic.replace("-", "");
            appUserModel.setNic(nicWithoutHyphins);
            appUserModel.setNicExpiryDate(dateFormat.parse(this.cnicExpiry));
            appUserModel.setMobileTypeId(1L);
            appUserModel.setPasswordChangeRequired(true);
            appUserModel.setDob(dateFormat.parse(this.cdob));
            appUserModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
            appUserModel.setCreatedOn(nowDate);
            appUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
            if (customerAccountyType.equals(String.valueOf(CustomerAccountTypeConstants.LEVEL_0))) {
                appUserModel.setAccountEnabled(false);
                appUserModel.setVerified(false);
            } else {
                appUserModel.setAccountEnabled(true);
                appUserModel.setVerified(true);
            }
            appUserModel.setAccountExpired(false);
            appUserModel.setAccountLocked(false);
            appUserModel.setCredentialsExpired(false);
            appUserModel.setAccountClosedUnsettled(false);
            appUserModel.setAccountClosedSettled(false);
            appUserModel.setMotherMaidenName(motherName);


            String mfsId = computeMfsId();
            String username = mfsId;

            String randomPin = RandomUtils.generateRandom(4, false, true);
            String password = EncoderUtils.encodeToSha(randomPin);
            String randomPinEncrypted = EncryptionUtil.encryptWithAES(XMLConstants.AES_ENCRYPTION_KEY, randomPin);


            if (this.isBvsAccount) {
                baseWrapper.putObject("isBvsAccount", isBvsAccount);
            }
            //****************************************************************************
            // In case of L0 Discrepant flow, this If's part would run.
            if (this.cRegStateId != null && this.cRegStateId.equals(String.valueOf(RegistrationStateConstants.DISCREPANT))) {
                CustomerModel discrepantCustomerModel = new CustomerModel();
                discrepantCustomerModel = new CustomerModel();
                discrepantCustomerModel.setCustomerId(appUserModel.getCustomerId());


                baseWrapper.setBasePersistableModel(discrepantCustomerModel);
                commonCommandManager.loadCustomer(baseWrapper);
                discrepantCustomerModel = (CustomerModel) baseWrapper.getBasePersistableModel();
                discrepantCustomerModel.setRegister(true);
                discrepantCustomerModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
                discrepantCustomerModel.setUpdatedOn(nowDate);
                if (isBvsAccount) {
                    discrepantCustomerModel.setVerisysDone(true);
                    discrepantCustomerModel.setAccountMethodId(AccountOpeningMethodConstantsInterface.BVS);
                } else {
                    discrepantCustomerModel.setVerisysDone(false);
                    discrepantCustomerModel.setAccountMethodId(AccountOpeningMethodConstantsInterface.CONVENTIONAL);
                }
                if (this.gender.toUpperCase().equals("MALE") || this.gender.toUpperCase().equals("M")) {
                    discrepantCustomerModel.setGender("M");
                } else if (this.gender.toUpperCase().equals("FEMALE") || this.gender.toUpperCase().equals("F")) {
                    discrepantCustomerModel.setGender("F");
                }

                discrepantCustomerModel.setCustomerId(appUserModel.getCustomerId());
                discrepantCustomerModel.setName(appUserModel.getFullName());
                discrepantCustomerModel.setMobileNo(appUserModel.getMobileNo());
                discrepantCustomerModel.setRelationAskari(0);
                discrepantCustomerModel.setRelationZong(0);
                discrepantCustomerModel.setBirthPlace(this.birthPlace);
                discrepantCustomerModel.setCustomerAccountTypeId(Long.valueOf(this.customerAccountyType));
                baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MODEL, discrepantCustomerModel);


                if (this.presentAddress != null && !"".equals(this.presentAddress)) {
                    AddressModel customerPresentAddressModel = new AddressModel();
                    customerPresentAddressModel.setHouseNo(this.presentAddress);
                    customerPresentAddressModel.setFullAddress(this.presentAddress);
                    baseWrapper.putObject(CommandFieldConstants.KEY_PRESENT_ADDR, customerPresentAddressModel);
                }
                if (this.permanentAddress != null && !"".equals(this.permanentAddress)) {
                    AddressModel customerPermanentAddressModel = new AddressModel();
                    customerPermanentAddressModel.setHouseNo(this.permanentAddress);
                    customerPermanentAddressModel.setFullAddress(this.permanentAddress);
                    baseWrapper.putObject(CommandFieldConstants.KEY_PERMANENT_ADDR, customerPermanentAddressModel);
                }
                appUserModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
                appUserModel.setUpdatedOn(nowDate);
                baseWrapper.putObject(CommandFieldConstants.KEY_APP_USER_MODEL, appUserModel);

                OLAVO olaVo = new OLAVO();
                olaVo.setFirstName(appUserModel.getFirstName());
                olaVo.setMiddleName(" ");
                olaVo.setLastName(appUserModel.getLastName());
                olaVo.setFatherName(appUserModel.getLastName());
                olaVo.setCnic(appUserModel.getNic());
                olaVo.setCustomerAccountTypeId(customerModel.getCustomerAccountTypeId());
                olaVo.setAddress(this.permanentAddress);
                olaVo.setLandlineNumber(appUserModel.getMobileNo());
                olaVo.setMobileNumber(appUserModel.getMobileNo());
                olaVo.setDob(dateFormat.parse(this.cdob));
                olaVo.setStatusId(1l);
                baseWrapper.putObject(CommandFieldConstants.KEY_ONLINE_ACCOUNT_MODEL, olaVo);

            }
            //****************************************************************************
            //If there is new account opening request then this flow would run
            else {

                if (this.cRegStateId != null && this.cRegStateId.equals(String.valueOf(RegistrationStateConstants.DISCREPANT))) {
                    appUserModel.setRegistrationStateId(RegistrationStateConstants.DISCREPANT);
                } else if (this.cRegStateId != null && this.cRegStateId.equals(String.valueOf(RegistrationStateConstants.BULK_REQUEST_RECEIVED))) {
                    appUserModel.setRegistrationStateId(RegistrationStateConstants.BULK_REQUEST_RECEIVED);
                } else {
                    if (isBvsAccount == true) {
                        appUserModel.setRegistrationStateId(RegistrationStateConstants.VERIFIED);
                    } else {
                        appUserModel.setRegistrationStateId(RegistrationStateConstants.REQUEST_RECEIVED);
                    }
                }

//                just for mock
//                    if(responseVO.getCaseStatus().equals("No Matches")){
//                        responseVO.setCaseStatus("True Match-Compliance");
//                    }

//                if (responseVO.getCaseStatus().equalsIgnoreCase("ERROR-999")||responseVO.getCaseStatus().equalsIgnoreCase("ERROR-998")) {
//                    throw new CommandException("Account Opening Request Rejected Due To Compliance ", ErrorCodes.ACCOUNT_OPENING_FAILED, ErrorLevel.MEDIUM, null);
//                }

//                if (!(responseVO.getCaseStatus().equalsIgnoreCase("No Matches") || responseVO.getCaseStatus().
//                        equalsIgnoreCase("Passed By Rule") || responseVO.getCaseStatus().
//                        equalsIgnoreCase("False Positive Match") ||
//                        responseVO.getCaseStatus().equalsIgnoreCase("GWL-Passed by Rules") ||
//                        responseVO.getCaseStatus().equalsIgnoreCase("GWL-Passed by Rules|PEP/EDD-False Positive|Private-False Positive") ||
//                        responseVO.getCaseStatus().equalsIgnoreCase("Private-False Positive") ||
//                        responseVO.getCaseStatus().equalsIgnoreCase("GWL-Passed by Rules|Private-Passed by Rules") ||
//                        responseVO.getCaseStatus().equalsIgnoreCase("Private-Passed by Rules") ||
//                        responseVO.getCaseStatus().equalsIgnoreCase("No Match")
//                        || responseVO.getCaseStatus().equalsIgnoreCase("GWL-Passed by Rules|PEP/EDD-Passed by Rules|Private-False Positive") ||
//                        responseVO.getCaseStatus().equalsIgnoreCase("GWL-Passed by Rules|PEP/EDD-Passed by Rules|Private-Passed by Rules"))) {
//				appUserModel.setRegistrationStateId(RegistrationStateConstants.CLSPENDING);
//				appUserModel.setAccountStateId(AccountStateConstants.CLS_STATE_BLOCKED);
//				preparedBaseWrapper.putObject(CommandFieldConstants.KEY_REGISTRATION_STATE_ID, RegistrationStateConstants.CLSPENDING);
//
//                List<ClsDebitCreditBlockModel> list = getCommonCommandManager().loadClsDebitCreditModel();
//                if(list!=null && !list.isEmpty()){
//                    for(ClsDebitCreditBlockModel model:list)
//                    {
//                        if(model.getState().equals("DEBIT") && model.getStatus().equals("1")){
//                            customerModel.setClsDebitBlock("1");
//                        }
//
//                        if(model.getState().equals("CREDIT") && model.getStatus().equals("1")){
//                            customerModel.setClsCreditBlock("1");
//                        }
//                    }
//                }
//			}
//            else if(responseVO.getCaseStatus().equals("True Match")){
//                appUserModel.setVerified(false);
//                appUserModel.setRegistrationStateId(RegistrationStateConstants.REJECTED);
//                appUserModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_REJECTED);
//            }

                appUserModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
                appUserModel.setUpdatedOn(nowDate);


                baseWrapper.putObject(CommandFieldConstants.KEY_CASE_STATUS, responseVO.getCaseStatus());
                appUserModel.setUsername(username);
                appUserModel.setPassword(password);

                appUserModel.setRegistrationStateId(RegistrationStateConstants.CLSPENDING);
                appUserModel.setAccountStateId(AccountStateConstantsInterface.CLS_STATE_BLOCKED);
                baseWrapper.putObject(CommandFieldConstants.KEY_APP_USER_MODEL, appUserModel);

                if (this.presentAddress != null && !"".equals(this.presentAddress)) {
                    AddressModel customerPresentAddressModel = new AddressModel();
                    customerPresentAddressModel.setHouseNo(this.presentAddress);
                    customerPresentAddressModel.setFullAddress(this.presentAddress);
                    baseWrapper.putObject(CommandFieldConstants.KEY_PRESENT_ADDR, customerPresentAddressModel);
                }
                if (this.permanentAddress != null && !"".equals(this.permanentAddress)) {
                    AddressModel customerPermanentAddressModel = new AddressModel();
                    customerPermanentAddressModel.setHouseNo(this.permanentAddress);
                    customerPermanentAddressModel.setFullAddress(this.permanentAddress);
                    baseWrapper.putObject(CommandFieldConstants.KEY_PERMANENT_ADDR, customerPermanentAddressModel);
                }

                if (this.cRegStateId == null || this.cRegStateId.equals("")) {
                    /**
                     * Populating the UserDeviceAccountsModel
                     */
                    userDeviceAccountsModel.setCommissioned(false);
                    if (isBvsAccount == true) {
                        userDeviceAccountsModel.setAccountEnabled(true);
                    } else {
                        userDeviceAccountsModel.setAccountEnabled(true);
                    }
                    // by default customer should be created in de-activated state
                    userDeviceAccountsModel.setAccountExpired(false);
                    userDeviceAccountsModel.setAccountLocked(false);
                    userDeviceAccountsModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
                    userDeviceAccountsModel.setCreatedOn(nowDate);
                    userDeviceAccountsModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
                    userDeviceAccountsModel.setUpdatedOn(nowDate);
                    userDeviceAccountsModel.setPinChangeRequired(true);
                    userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);
                    userDeviceAccountsModel.setCredentialsExpired(false);
                    userDeviceAccountsModel.setPasswordChangeRequired(false);
                    userDeviceAccountsModel.setUserId(mfsId);
//                    userDeviceAccountsModel.setPin(randomPinEncrypted);
                    if (isHRA) {
                        userDeviceAccountsModel.setProdCatalogId(PortalConstants.HRA_CUSTOMER_CATALOG);
                    } else {
                        userDeviceAccountsModel.setProdCatalogId(PortalConstants.CUSTOMER_DEFAULT_CATALOG);
                    }


                    baseWrapper.putObject(CommandFieldConstants.KEY_USER_DEVICE_ACCOUNT_MODEL, userDeviceAccountsModel);

                    Long bankId = this.getCommonCommandManager().getOlaBankMadal().getBankId();

                    SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
                    smartMoneyAccountModel.setBankId(bankId);
                    smartMoneyAccountModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
                    smartMoneyAccountModel.setCreatedOn(new Date());
                    smartMoneyAccountModel.setUpdatedOn(new Date());
                    smartMoneyAccountModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
                    smartMoneyAccountModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
                    smartMoneyAccountModel.setActive(true);
                    smartMoneyAccountModel.setStatusId(OlaStatusConstants.ACCOUNT_STATUS_ACTIVE);
                    smartMoneyAccountModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_COLD);
                    //pehla ya false tha ab ya true kar raha hu ta ka zindigi app sa mpin set ho saka as per waqar bahi
                    smartMoneyAccountModel.setChangePinRequired(true);
                    smartMoneyAccountModel.setDefAccount(true);
                    smartMoneyAccountModel.setDeleted(false);
                    smartMoneyAccountModel.setName("i8_bb_" + mfsId);

                    baseWrapper.putObject(CommandFieldConstants.KEY_SMART_MONEY_ACCOUNT_MODEL, smartMoneyAccountModel);

                    OLAVO olaVo = new OLAVO();
                    olaVo.setFirstName(appUserModel.getFirstName());
                    olaVo.setMiddleName(" ");
                    olaVo.setLastName(appUserModel.getLastName());
                    olaVo.setFatherName(appUserModel.getLastName());
                    olaVo.setCnic(appUserModel.getNic());
                    olaVo.setCustomerAccountTypeId(customerModel.getCustomerAccountTypeId());
                    olaVo.setAddress("Lahore");
                    olaVo.setLandlineNumber(appUserModel.getMobileNo());
                    olaVo.setMobileNumber(appUserModel.getMobileNo());
                    olaVo.setDob(dateFormat.parse(this.cdob));
                    olaVo.setStatusId(1l);
                    baseWrapper.putObject(CommandFieldConstants.KEY_ONLINE_ACCOUNT_MODEL, olaVo);

                    AccountInfoModel accountInfoModel = new AccountInfoModel();
                    accountInfoModel.setAccountNick(smartMoneyAccountModel.getName());
                    accountInfoModel.setActive(smartMoneyAccountModel.getActive());
                    accountInfoModel.setCreatedOn(smartMoneyAccountModel.getCreatedOn());
                    accountInfoModel.setUpdatedOn(smartMoneyAccountModel.getUpdatedOn());
                    accountInfoModel.setCustomerMobileNo(appUserModel.getMobileNo());
                    accountInfoModel.setFirstName(appUserModel.getFirstName());
                    accountInfoModel.setLastName(appUserModel.getLastName());
                    accountInfoModel.setPaymentModeId(smartMoneyAccountModel.getPaymentModeId());
                    accountInfoModel.setDeleted(Boolean.FALSE);
                    accountInfoModel.setIsMigrated(1L);
                    //Added after HSM Integration
                    accountInfoModel.setPan(PanGenerator.generatePAN());
                    // End HSM Integration Change

                    baseWrapper.putObject(CommandFieldConstants.KEY_ACCOUNT_INFO_MODEL, accountInfoModel);
                }
            }

            if (isBvsAccount == true) {
                verisysDataModel.setCnic(this.cNic);
                verisysDataModel.setPlaceOfBirth(this.birthPlace);
                verisysDataModel.setMotherMaidenName(this.motherName);
                verisysDataModel.setName(this.cName);
                verisysDataModel.setCurrentAddress(this.presentAddress);
                verisysDataModel.setPermanentAddress(this.presentAddress);
                verisysDataModel.setTranslated(false);
                verisysDataModel.setCreatedOn(new Date());
                verisysDataModel.setUpdatedOn(new Date());
                baseWrapper.putObject(CommandFieldConstants.KEY_VARISYS_DATA_MODEL, verisysDataModel);
            }

            baseWrapper.putObject(CommandFieldConstants.KEY_DEPOSIT_AMT, depositAmount);
            baseWrapper.putObject(CommandFieldConstants.KEY_APP_ID, "1");
            baseWrapper.putObject(CommandFieldConstants.KEY_AGENT_MOBILE, agentMobileNo);
            try {

                if (this.cRegStateId != null && this.cRegStateId.equals(String.valueOf(RegistrationStateConstants.DISCREPANT))) {
                    //In-case of descrepant account opening request for LO, L1
                    baseWrapper = this.getCommonCommandManager().saveOrUpdateDescrepentAccountOpening(baseWrapper);
                    accountUpdated = true;
                } else {
                    //In-case of normal account opening request for LO, L1
                    baseWrapper = this.getCommonCommandManager().saveOrUpdateAccountOpeningL0Request(baseWrapper);


                    //data insertion on cls pending account opening Table
//                    ClsPendingAccountOpeningModel clsPendingAccountOpeningModel = new ClsPendingAccountOpeningModel();
//                    clsPendingAccountOpeningModel.setMobileNo(cMsisdn);
//                    clsPendingAccountOpeningModel.setCnic(cNic);
//                    clsPendingAccountOpeningModel.setConsumerName(cName);
////                    clsPendingAccountOpeningModel.setFatherHusbandName(webServiceVO.getFatherHusbandName());
//                    clsPendingAccountOpeningModel.setMotherMaidenName(motherName);
//                    clsPendingAccountOpeningModel.setCaseStatus(responseVO.getCaseStatus());
//                    clsPendingAccountOpeningModel.setCaseID(responseVO.getCaseId());
////                    clsPendingAccountOpeningModel.setClsBotStatus(0);
//                    clsPendingAccountOpeningModel.setGender(gender);
////                    clsPendingAccountOpeningModel.setCnicIssuanceDate(dateFormat.parse(webServiceVO.getCnicIssuanceDate()));
//                    clsPendingAccountOpeningModel.setDob(dateOfBirth);
//                    clsPendingAccountOpeningModel.setCustomerId(customerModel.getCustomerId());
//                    clsPendingAccountOpeningModel.setAppUserId(appUserModel.getAppUserId());
//                    clsPendingAccountOpeningModel.setRegistrationStateId(appUserModel.getRegistrationStateId());
//                    clsPendingAccountOpeningModel.setAccountStateId(appUserModel.getAccountStateId());
//                    clsPendingAccountOpeningModel.setCustomerAccountTypeId(customerModel.getCustomerAccountTypeId());
//                    clsPendingAccountOpeningModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
//                    clsPendingAccountOpeningModel.setCreatedOn(new Date());
//                    clsPendingAccountOpeningModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
//                    clsPendingAccountOpeningModel.setUpdatedOn(new Date());
//                    clsPendingAccountOpeningModel.setClsComments(null);
////                    clsPendingAccountOpeningModel.setIsCompleted("0");
//                    this.getCommonCommandManager().clsPendingAccountOpening(clsPendingAccountOpeningModel);
				/*if(isBvsAccount==true){
					String fname;
					String lName;
					fname=nameArray[0];
					if (nameArray.length > 1) {
						lName=this.cName.substring(
								fname.length() + 1);
					} else {
						lName=nameArray[0];
					}
					fname=escapeUnicode(fname);
					lName=escapeUnicode(lName);
					String motherMaidenName="";
					if(this.motherName!=null) {
						motherMaidenName = escapeUnicode(this.motherName);
					}
					else{
						motherMaidenName="";
					}
					String address=escapeUnicode(this.presentAddress);
					String birthPlace=escapeUnicode(this.birthPlace);


					CustomerModel cModel=(CustomerModel)baseWrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_MODEL);
					AppUserModel aModel=(AppUserModel)baseWrapper.getObject(CommandFieldConstants.KEY_APP_USER_MODEL);
					AccountInfoModel aInfoModel=(AccountInfoModel)baseWrapper.getObject(CommandFieldConstants.KEY_ACCOUNT_INFO_MODEL);
					OLAVO olaModel=(OLAVO)baseWrapper.getObject(CommandFieldConstants.KEY_ONLINE_ACCOUNT_MODEL);
					AddressModel addressModel=new AddressModel();
					if(null != baseWrapper.getObject(CommandFieldConstants.KEY_PRESENT_ADDR)){
						addressModel=(AddressModel) baseWrapper.getObject(CommandFieldConstants.KEY_PRESENT_ADDR);
					}
					else{
						addressModel=(AddressModel)baseWrapper.getObject(CommandFieldConstants.KEY_PERMANENT_ADDR);
					}


					NADRADataVO nadraDataVO=new NADRADataVO();
					nadraDataVO.setAccountInfoId(aInfoModel.getAccountInfoId());
					nadraDataVO.setAccountHolderId(olaModel.getAccountHolderId());
					nadraDataVO.setAddressId(addressModel.getAddressId());
					nadraDataVO.setAppUserId(aModel.getAppUserId());
					nadraDataVO.setCustomerId(cModel.getCustomerId());
					nadraDataVO.setfName(fname);
					nadraDataVO.setlName(lName);
					nadraDataVO.setAddress(address);
					nadraDataVO.setMotherMaidenName(motherMaidenName);
					nadraDataVO.setBirthPlace(birthPlace);

					try{
						this.getCommonCommandManager().getVirtualCardHibernateDAO().updateRegData(nadraDataVO);
					}
					catch (SQLException e){
						e.printStackTrace();
					}

				}*/
                }

            } catch (Exception ex) {
                logger.error("Exception in occurred in saveOrUpdateAccountOpeningL0Request... Exception.getMessage():" + ex.getMessage());
                logger.error("Exception in occurred in saveOrUpdateAccountOpeningL0Request... Exception.getMessage()..", ex);
                if (ex instanceof NullPointerException
                        || ex instanceof HibernateException
                        || ex instanceof SQLException
                        || ex instanceof DataAccessException
                        || (ex.getMessage() != null && ex.getMessage().indexOf("Exception") != -1)) {

                    logger.error("Converting Exception (" + ex.getClass() + ") to generic error message...");
                    throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
                } else {
                    throw ex;
                }
            }

            accountCreated = true;

            String generatedPin = (String) baseWrapper.getObject(CommandFieldConstants.KEY_PIN);
            logger.info("System Generated Pin: " + generatedPin);
//            BaseWrapper bWrapper = new BaseWrapperImpl();
//            this.logger.info("Third Party MPIN Registration for Mobile # :: " + appUserModel.getMobileNo());
//            bWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.MOBILE.toString());
//            bWrapper.putObject(CommandFieldConstants.KEY_NEW_PIN, EncryptionUtil.encryptWithAES("682ede816988e58fb6d057d9d85605e0", randomPin));
//            bWrapper.putObject(CommandFieldConstants.KEY_CONF_PIN, EncryptionUtil.encryptWithAES("682ede816988e58fb6d057d9d85605e0", randomPin));
//            bWrapper.putObject(CommandFieldConstants.KEY_ENCRYPTION_TYPE, "1");
//            ThreadLocalAppUser.setAppUserModel(appUserModel);
//            bWrapper.putObject("IS_FORCEFUL", "1");
//            getCommandManager().executeCommand(bWrapper, CommandFieldConstants.CMD_MIGRATED_PIN_CHG);
            if (accountUpdated == false) {
                // This would be new account creation scenario, so ivr call would be generated

                //Sending SMS (with links android/iOS) to Users
                if (appUserModel.getRegistrationStateId().equals(RegistrationStateConstants.CLSPENDING)) {
                    sendSMSToUsers(mfsId, "", appUserModel.getRegistrationStateId());
                } else {
                    commonCommandManager.getAppManager().sendSMSToUsers(appUserModel.getMobileNo(), "", false);
                }


//			IvrRequestDTO ivrDTO = new IvrRequestDTO();
//	    	ivrDTO.setCustomerMobileNo(appUserModel.getMobileNo());
//			ivrDTO.setPin(generatedPin);
//	    	ivrDTO.setRetryCount(0);
//			ivrDTO.setProductId(new Long(CommandFieldConstants.CREATE_PIN_IVR));
//			try {
//				this.getCommonCommandManager().initiateUserGeneratedPinIvrCall(ivrDTO);
//			}catch(Exception e){
//				e.printStackTrace();
//				throw new CommandException( e.getLocalizedMessage() ,ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM, null ) ;
//			}
            }
//            if (ThreadLocalAppUser.getAppUserModel().getAppUserId() == PortalConstants.WEB_SERVICE_APP_USER_ID) {
//                AppUserModel agentAppUserModel = new AppUserModel();
//                try {
//                    agentAppUserModel = getCommonCommandManager().loadAppUserByMobileAndType(agentMobileNo);
//                } catch (FrameworkCheckedException e) {
//                    e.printStackTrace();
//                }
//
//                if (appUserModel != null) {
//                    logger.debug("[FonepayPaymentCommand.execute] AppUserModel loader wil AppUserId:" + appUserModel.getAppUserId());
//                    ThreadLocalAppUser.setAppUserModel(agentAppUserModel);
//                }
//            } else {
//                AppUserModel agentAppUserModel = getCommonCommandManager().loadAppUserByMobileAndType(agentMobileNo);
//                ThreadLocalAppUser.setAppUserModel(agentAppUserModel);
//
//            }


            File imageFile = null;
		/*if(this.termsPhoto != null && !this.termsPhoto.equals(""))
		{
			imageFile = commonCommandManager.loadImage("images/upload_dir/" + this.termsPhoto);
			imageFile.delete();	
		}*/
            if (this.customerPhoto != null && !this.customerPhoto.equals("")) {
                imageFile = commonCommandManager.loadImage("images/upload_dir/" + this.customerPhoto);
                imageFile.delete();
            }
		/*if(this.signaturePhoto != null && !this.signaturePhoto.equals(""))
		{
			imageFile = commonCommandManager.loadImage("images/upload_dir/" + this.signaturePhoto);
			imageFile.delete();	
		}*/
            if (this.cNicFrontPhoto != null && !this.cNicFrontPhoto.equals("")) {
                imageFile = commonCommandManager.loadImage("images/upload_dir/" + this.cNicFrontPhoto);
                imageFile.delete();
            }
		/*if(this.cNicBackPhoto != null && !this.cNicBackPhoto.equals(""))
		{
			imageFile = commonCommandManager.loadImage("images/upload_dir/" + this.cNicBackPhoto);		
			imageFile.delete();	
		}
		if(this.l1FormPhoto != null && !this.l1FormPhoto.equals(""))
		{
			imageFile = commonCommandManager.loadImage("images/upload_dir/" + this.l1FormPhoto);		
			imageFile.delete();
		}*/

            if (transactionId == null) // normal flow of account opening
            {
                if (this.cRegStateId == null || this.cRegStateId.equals("") || this.cRegStateId.equals(String.valueOf(RegistrationStateConstants.BULK_REQUEST_RECEIVED))) {
                    //Before executing Initial Deposit Command - Set HandlerAppUserModel in ThreadLocal
                    if (handlerModel != null && handlerAppUserModel != null) {
                        ThreadLocalAppUser.setAppUserModel(handlerAppUserModel);
                    }

                    // execute initial deposit command
                    BaseWrapper idWrapper = new BaseWrapperImpl();
                    idWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, deviceTypeId);
                    idWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.ACCOUNT_OPENING);
                    idWrapper.putObject(CommandFieldConstants.KEY_ENCRYPTION_TYPE, encryptionType);
                    idWrapper.putObject(CommandFieldConstants.KEY_AGENT_MOBILE, agentMobileNo);
                    idWrapper.putObject(CommandFieldConstants.KEY_PIN, agentPIN);
                    idWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, cMsisdn);
                    idWrapper.putObject(CommandFieldConstants.KEY_TXAM, depositAmount);
                    if (this.isBvsAccount) {
                        idWrapper.putObject("IS_BVS_ACCOUNT", "1");
                    }
                    try {
                        //Commenting it out for further working of initial deposit in agent mate phase 2
                        //Double initialDepositAmount =  0.0D;
					/*if(depositAmount != null && !depositAmount.equals("") ){
						initialDepositAmount = Double.valueOf(depositAmount);
					}*/
					
					
					/*ProductModel productModel = new ProductModel();
					Double exclusiveFixAmount = 0.0D;
					Double exclusivePercentAmount = 0.0D;
					Double inclusiveCharges = 0.0D;
					Double inclusivePercentChargess = 0.0D;
					boolean isThirdParty = false;
					
					productModel.setProductId(ProductConstantsInterface.ACCOUNT_OPENING);	
					baseWrapper.setBasePersistableModel(productModel);
					baseWrapper = commonCommandManager.loadProduct(baseWrapper);
					productModel = (ProductModel) baseWrapper.getBasePersistableModel();
					
					if(null != productModel.getExclusiveFixAmount()){
						exclusiveFixAmount = productModel.getExclusiveFixAmount();
					}
					if(null != productModel.getExclusivePercentAmount()){
						exclusivePercentAmount = productModel.getExclusivePercentAmount();	
					}
					if(null != productModel.getInclusiveFixAmount()){
						inclusiveCharges = productModel.getInclusiveFixAmount();
					}				
					if(null != productModel.getInclusivePercentAmount()){
						inclusivePercentChargess = productModel.getInclusivePercentAmount();
					}
					if(null != productModel.getInclChargesCheck()){
						isThirdParty = productModel.getInclChargesCheck();
					}*/


                        if (depositAmountFlag.equals("1")) {
                            getCommandManager().executeCommand(idWrapper, CommandFieldConstants.CMD_INITIAL_DEPOSIT);

                        }
                    } catch (Exception ex) {
                        initialDepositErrorCheck = true;
                        throw ex;
                    }
                    // send SMS to customer & agent
                    if (!appUserModel.getRegistrationStateId().equals(RegistrationStateConstants.CLSPENDING)) {
                        sendSMSToUsers(mfsId, generatedPin, appUserModel.getRegistrationStateId());
                    }
//                    sendSMSForCLSUsers(username, generatedPin, appUserModel.getRegistrationStateId());

                    MiddlewareAdviceVO middlewareAdviceVO = new MiddlewareAdviceVO();
                    middlewareAdviceVO.setStan(String.valueOf((100000 + new Random().nextInt(900000))));
                    middlewareAdviceVO.setRequestTime(new Date());
                    middlewareAdviceVO.setDateTimeLocalTransaction(new Date());
                    middlewareAdviceVO.setTransmissionTime(new Date());
                    middlewareAdviceVO.setAdviceType(CoreAdviceUtil.ACCOUNNT_OPENING_ADVICE);
                    middlewareAdviceVO.setProductId(ProductConstantsInterface.ACCOUNT_OPENING);
                    middlewareAdviceVO.setConsumerNo(appUserModel.getMobileNo());
                    middlewareAdviceVO.setCnicNo(appUserModel.getNic());

                    getTransactionReversalManager().sendCoreReversalRequiresNewTransaction(middlewareAdviceVO);

//                        SwitchWrapper switchWrapper = new SwitchWrapperImpl();
//                        switchWrapper.setBaseWrapper(baseWrapper);

                    WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();

                    workFlowWrapper.putObject(CommandFieldConstants.KEY_APP_USER_MODEL, appUserModel);
                    workFlowWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MODEL, customerModel);
//					workFlowWrapper.putObject(CommandFieldConstants.KEY_SMART_MONEY_ACCOUNT_MODEL, smartMoneyAccountModel);
                    workFlowWrapper.setProductId(ProductConstantsInterface.ACCOUNT_OPENING);

                    loadAndForwardAccountToQueue(workFlowWrapper);


                }
            }

            if (this.transactionId != null && this.cNic != null)//ifot the case of normal account opening.
            {
                try {
                    //Commenting it out for further working of initial deposit in agent mate phase 2
                    commonCommandManager.makeCustomerTrxByTransactionCode(transactionId, this.cNic);
                    commonCommandManager.loadAndForwardAccountToQueue(transactionId);

                    sendSMSToUsers(mfsId, generatedPin, appUserModel.getRegistrationStateId());
                } catch (Exception ex) {
                    pendingTransErrorCheck = true;
                    throw ex;
                }
            }

            /*****************************************************************************************
             * Logging the data
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
        } catch (FrameworkCheckedException ex) {
            ex.printStackTrace();
            String errorMessage = prepareErrorMessage(ex, initialDepositErrorCheck, pendingTransErrorCheck, accountCreated, true);
            if (!accountCreated) {
                throw new CommandException(errorMessage, (ex.getErrorCode() == 0) ? ErrorCodes.COMMAND_EXECUTION_ERROR : ex.getErrorCode(), ErrorLevel.MEDIUM, ex);
            } else {
                postCreationerrorMessage = errorMessage;
                logger.error(errorMessage, ex);
            }
        } catch (WorkFlowException wex) {
            wex.printStackTrace();
            String errorMessage = prepareErrorMessage(wex, initialDepositErrorCheck, pendingTransErrorCheck, accountCreated, true);
            if (!accountCreated) {
                throw new CommandException(errorMessage, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, wex);
            } else {
                postCreationerrorMessage = errorMessage;
                logger.error(errorMessage, wex);
            }
        } catch (IOException ioex) {
            System.out.println("Files are not deleted.");
        } catch (Exception ex) {
            ex.printStackTrace();
            String errorMessage = prepareErrorMessage(ex, initialDepositErrorCheck, pendingTransErrorCheck, accountCreated, false);
            if (!accountCreated) {
                throw new CommandException(errorMessage, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.HIGH, ex);
            } else {
                postCreationerrorMessage = errorMessage;
                logger.error(errorMessage, ex);
            }
        }

        if (this.logger.isDebugEnabled()) {
            this.logger.debug("End of OpenCustomerL0AccountCommand.execute()");
        }

    }// End of execute method...

    public String response() {
        if (this.logger.isDebugEnabled())
            this.logger
                    .debug("Start of OpenCustomerL0AccountCommand.response()");
        return toXML();
    }

    private String prepareErrorMessage(Exception ex, boolean initialDepositErrorCheck, boolean pendingTransErrorCheck, boolean accountCreated, boolean addReason) {
        String errorMessage = ex.getMessage();

        if (accountCreated) {
            if (initialDepositErrorCheck) {
                errorMessage = this.getMessageSource().getMessage("openCustomerL0AccountCommand.initialDeposit.failed", null, null);
            } else if (pendingTransErrorCheck) {
                errorMessage = this.getMessageSource().getMessage("openCustomerL0AccountCommand.pendingTransaction.failed", null, null);
            }

            if ((initialDepositErrorCheck || pendingTransErrorCheck) && addReason) {
                errorMessage = errorMessage + " Reason: " + ex.getMessage();
            }
        } else {
            // [04 June 2015 omar butt] This check is added to avoid a special error scenario same customer creation from two agent simultaneously
            // (one successful and second shows SQL Exception)
            if (!StringUtil.isNullOrEmpty(errorMessage) && (errorMessage.contains("Hibernate")
                    || errorMessage.contains("Exception")
                    || errorMessage.contains("SQLException"))) {

                errorMessage = this.getMessageSource().getMessage("newMfsAccount.unknown", null, null);

            }

        }

        return errorMessage;
    }

    private String toXML() {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of OpenCustomerL0AccountCommand.toXML()");
        }
        String message = "";
        if ((this.cRegStateId.equals(RegistrationStateConstantsInterface.VERIFIED.toString()) || this.cRegStateId.equals(RegistrationStateConstants.CLSPENDING.toString())) && this.successMessage.equals("success"))
            message = "Account Upgraded Successfully";
        else if (accountCreated && !StringUtil.isNullOrEmpty(postCreationerrorMessage)) {
            message = postCreationerrorMessage;
        } else if (preparedBaseWrapper.getObject(CommandFieldConstants.KEY_REGISTRATION_STATE_ID) != null) {
            if (preparedBaseWrapper.getObject(CommandFieldConstants.KEY_REGISTRATION_STATE_ID).equals(RegistrationStateConstants.CLSPENDING)) {
                message = "Customer request has been submitted successfully. Kindly activate your account";
            }
        } else {
            message = XMLConstants.MESSAGE_SENT_SUCCESS;
        }

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

        return xmlBuilder.toString();
    }


    private void addCustomerPicture(Long pictureTypeId, String pictureName, boolean isSkipped) throws IOException, CommandException {
        CustomerPictureModel customerPictureModel = new CustomerPictureModel();
        try {
            System.out.println("Picture Name: " + pictureName);
            File imageFile;
            if (!isSkipped) {
                imageFile = commonCommandManager.loadImage("images/upload_dir/" + pictureName);
            } else {
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
        } catch (IOException e) {
            /* Special Scenario Handling [Special Request of Humayun Naveed for Fraud Prevention JSBLMFS-72]
             * Implementation:
             *		File Not Found Error will move the AgentMate from Account Opening to Home Screen by using New ErrorCode 9018
             * Scenario:
             * 		The scenario is two different agents are trying to register same customer simultaneously.
             * 		On agent 1 it is successful and on agent 2 it displayed 'File Not Found' (As the pictures
             * 		have already been removed after successful registration at agent1)
             */
            throw new CommandException(this.getMessageSource().getMessage("newMfsAccount.unknown", null, null), ErrorCodes.FILE_NOT_FOUND, ErrorLevel.MEDIUM);
        }

        if (arrayCustomerPictures != null) {
            arrayCustomerPictures.add(customerPictureModel);
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

        while (flag) {
            nextLongValue = commonCommandManager.getSequenceGenerator().nextLongValue();
            appUserModel = new AppUserModel();
            goldenNosModel = new GoldenNosModel();
            appUserModel.setUsername(String.valueOf(nextLongValue));
            goldenNosModel.setGoldenNumber(String.valueOf(nextLongValue));
            int countAppUser = commonCommandManager.countByExample(appUserModel);
            int countGoldenNos = commonCommandManager.countByExample(goldenNosModel);
            if (countAppUser == 0 && countGoldenNos == 0) {
                flag = false;
            }
        }

        return String.valueOf(nextLongValue);
    }

    private void sendSMSToUsers(String userId, String pin, long registrationStateId) {
        try {
            BaseWrapper baseWrapper = new BaseWrapperImpl();

            String brandName = MessageUtil.getMessage("jsbl.brandName");

            //Message to customer
            String customerSMS;
            if (depositAmount == null || depositAmount.trim().equalsIgnoreCase("")) {
                customerSMS = this.getMessageSource().getMessage("smsCommand.act_sms15", new Object[]{this.cMsisdn, MessageUtil.getMessage("HELP_LINE_NUMBER")}, null);
            } else {
                customerSMS = this.getMessageSource().getMessage("smsCommand.act_sms6b",
                        new Object[]{brandName, this.cMsisdn, Formatter.formatNumbers(Double.valueOf(depositAmount))}, null);
            }

            if (registrationStateId == RegistrationStateConstants.CLSPENDING) {
                customerSMS = MessageUtil.getMessage("smsCommand.act_sms_jsbl_con_app.pending.agentMate");

//                this.getMessageSource().getMessage("smsCommand.act_sms_jsbl_con_app.pending.agentMate", new Object[]{cMsisdn, pin}, null);
            }

            baseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, new SmsMessage(cMsisdn, customerSMS));
            getCommonCommandManager().sendSMSToUser(baseWrapper);

            //Message to agent
            String agentSMS;
            if (depositAmount == null || depositAmount.trim().equalsIgnoreCase("")) {
                agentSMS = this.getMessageSource().getMessage("smsCommand.act_sms16", new Object[]{cMsisdn}, null);
            } else {
                agentSMS = this.getMessageSource().getMessage("smsCommand.act_sms13",
                        new Object[]{
                                cMsisdn,
                                Formatter.formatNumbers(Double.valueOf(depositAmount))}, null);
            }

            baseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, new SmsMessage(agentMobileNo, agentSMS));
            getCommonCommandManager().sendSMSToUser(baseWrapper);
        } catch (FrameworkCheckedException e) {
            e.printStackTrace();
        }
    }

    private void sendSMSForCLSUsers(String userName, String pin, long registrationStateId) {
        try {
            BaseWrapper baseWrapper = new BaseWrapperImpl();

            String brandName = MessageUtil.getMessage("jsbl.brandName");

            //Message to customer
            String customerSMS = "";
//            if (isConsumerApp && registrationStateId != RegistrationStateConstants.CLSPENDING) {
//                customerSMS = this.getMessageSource().getMessage("smsCommand.act_sms_jsbl_con_app", new Object[]{userName, pin}, null);
//            } else {
//					customerSMS = this.getMessageSource().getMessage("smsCommand.act_sms_jsbl_fonepay",
//							new Object[] { brandName,urls.get(0),urls.get(1), pin}, null);
//            customerSMS = this.getMessageSource().getMessage("smsCommand.act_sms_jsbl_fonepay", new Object[]{userName, pin}, null);
//            }

            if (registrationStateId == RegistrationStateConstants.CLSPENDING) {
                customerSMS = this.getMessageSource().getMessage("smsCommand.act_sms_jsbl_con_app.pending", new Object[]{userName, pin}, null);
            }
            baseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, new SmsMessage(userName, customerSMS));
            getCommonCommandManager().sendSMSToUser(baseWrapper);


        } catch (FrameworkCheckedException e) {
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

    private void loadAndForwardAccountToQueue(final WorkFlowWrapper workFlowWrapper) throws InterruptedException {
        getCoreAdviceQueingPreProcessor().startProcessing(workFlowWrapper);
    }

    public void setTransactionReversalManager(TransactionReversalManager transactionReversalManager) {
        this.transactionReversalManager = transactionReversalManager;
    }

    public void setCoreAdviceQueingPreProcessor(CoreAdviceQueingPreProcessor coreAdviceQueingPreProcessor) {
        this.coreAdviceQueingPreProcessor = coreAdviceQueingPreProcessor;
    }

    public TransactionReversalManager getTransactionReversalManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (TransactionReversalManager) applicationContext.getBean("transactionReversalManager");
    }

    public CoreAdviceQueingPreProcessor getCoreAdviceQueingPreProcessor() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (CoreAdviceQueingPreProcessor) applicationContext.getBean("coreAdviceQueingPreProcessor");
    }

//	public void setEsbAdapter(ESBAdapter esbAdapter) {
//		this.esbAdapter = esbAdapter;
//	}
//	public ESBAdapter getEsbAdapter() {
//		return this.esbAdapter ;
//	}
}