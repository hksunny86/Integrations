package com.inov8.microbank.fonepay.service;

import com.inov8.common.util.RandomUtils;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.integration.common.model.LedgerModel;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.middleware.controller.NadraIntegrationController;
import com.inov8.integration.vo.MiddlewareMessageVO;
import com.inov8.integration.vo.NadraIntegrationVO;
import com.inov8.integration.webservice.vo.Transaction;
import com.inov8.integration.webservice.vo.WebServiceVO;
import com.inov8.microbank.app.dao.AppInfoDAO;
import com.inov8.microbank.app.model.AppInfoModel;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.customermodule.BlinkCustomerPictureModel;
import com.inov8.microbank.common.model.customermodule.CustomerPictureModel;
import com.inov8.microbank.common.model.messagemodule.NovaAlertMessage;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.MiniStatementListViewModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.vo.ussd.UserDeviceAccountsModelVO;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.fonepay.common.*;
import com.inov8.microbank.fonepay.dao.FonePayLogDAO;
import com.inov8.microbank.fonepay.dao.FonePayTransactionDetailViewDAO;
import com.inov8.microbank.fonepay.dao.PinRetryDAO;
import com.inov8.microbank.fonepay.dao.hibernate.VirtualCardHibernateDAO;
import com.inov8.microbank.fonepay.dao.hibernate.VirtualCardViewHibernateDAO;
import com.inov8.microbank.fonepay.model.*;
import com.inov8.microbank.nadraVerisys.dao.VerisysDataDAO;
import com.inov8.microbank.nadraVerisys.model.VerisysDataModel;
import com.inov8.microbank.server.dao.customermodule.CustomerDAO;
import com.inov8.microbank.server.dao.customermodule.CustomerPictureDAO;
import com.inov8.microbank.server.dao.mfsmodule.UserDeviceAccountsDAO;
import com.inov8.microbank.server.dao.portal.bbaccountsview.BBAccountsViewDao;
import com.inov8.microbank.server.dao.portal.ola.OlaCustomerAccountTypeDao;
import com.inov8.microbank.server.dao.portal.taxregimemodule.TaxRegimeDAO;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;
import com.inov8.microbank.server.dao.securitymodule.hibernate.AppUserHibernateDAO;
import com.inov8.microbank.server.dao.transactionmodule.MiniTransactionDAO;
import com.inov8.microbank.server.dao.transactionmodule.TransactionDetailMasterDAO;
import com.inov8.microbank.server.facade.CoreAdviceQueingPreProcessor;
import com.inov8.microbank.server.service.clspendingblinkcustomermodule.ClsDebitCreditManager;
import com.inov8.microbank.server.service.clspendingblinkcustomermodule.dao.ClsDebitCreditDAO;
import com.inov8.microbank.server.service.commandmodule.CommandManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchController;
import com.inov8.microbank.server.service.integration.vo.MiddlewareAdviceVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.settlementmodule.SettlementManager;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;
import com.inov8.microbank.server.service.transactionmodule.TransactionDetailMasterManager;
import com.inov8.microbank.server.service.transactionmodule.TransactionModuleManager;
import com.inov8.microbank.server.service.transactionreversal.TransactionReversalManager;
import com.inov8.ola.integration.vo.OLAInfo;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.ola.server.dao.ledger.LedgerDAO;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import com.inov8.ola.util.EncryptionUtil;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.hibernate.criterion.MatchMode;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.portlet.HandlerExceptionResolver;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class FonePayManagerImpl implements FonePayManager {

    protected static Log logger = LogFactory.getLog(FonePayManagerImpl.class);
    @Autowired
    CustomerDAO customerDAO;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy");
    //@Autowired
    //private CommonCommandManager commonCommandManager;
    @Autowired
    private UserDeviceAccountsDAO userDeviceAccountsDAO;
    @Autowired
    private SmsSender smsSender;
    @Autowired
    private MiniTransactionDAO miniTransactionDAO;
    @Autowired
    private FonePayLogDAO fonePayLogDAO;
    @Autowired
    private VirtualCardHibernateDAO virtualCardHibernateDAO;
    @Autowired
    private VirtualCardViewHibernateDAO virtualCardViewHibernateDAO;
    @Autowired
    private TransactionDetailMasterDAO transactionDetailMasterDAO;
    @Autowired
    private LedgerDAO ledgerDAO;
    @Autowired
    private BBAccountsViewDao bbAccountsViewDao;

    @Autowired
    private TransactionDetailMasterManager transactionDetailMasterManager;
    private TransactionModuleManager transactionModuleManager;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private CustomerPictureDAO customerPictureDAO;
    @Autowired
    private SwitchController switchController;
    private SettlementManager settlementManager;
    private StakeholderBankInfoManager stakeholderBankInfoManager;
    private MessageSource messageSource;
    @Autowired
    private PinRetryDAO pinRetryDAO;
    @Autowired
    private VerisysDataDAO verisysDataHibernateDAO;
    private AppUserHibernateDAO appUserHibernateDAO;
    @Autowired
    private AppInfoDAO appInfoDAO;
    private TaxRegimeDAO taxRegimeDAO;
    @Autowired
    private FonePayTransactionDetailViewDAO fonePayTransactionDetailViewDAO;
    private OlaCustomerAccountTypeDao olaCustomerAccountTypeDao;
    private ESBAdapter esbAdapter;
    private TransactionReversalManager transactionReversalManager;
    private CoreAdviceQueingPreProcessor coreAdviceQueingPreProcessor;
    private GenericDao genericDAO;
    private ClsDebitCreditManager clsDebitCreditManager;
    private ClsDebitCreditDAO clsDebitCreditDAO;

    public void setAppInfoDAO(AppInfoDAO appInfoDAO) {
        this.appInfoDAO = appInfoDAO;
    }

    public CommandManager getCommandManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (CommandManager) applicationContext.getBean("cmdManager");
    }

    private void validateInstantAccOpeningRequest(FonePayMessageVO messageVO) throws CommandException {

        ValidationErrors validationErrors = new ValidationErrors();
        validationErrors = ValidatorWrapper.doRequired(messageVO.getCustomerAccountyType(), validationErrors, "Customer Account Type");
        validationErrors = ValidatorWrapper.doRequired(messageVO.getCustomerName(), validationErrors, "Customer Name");
        validationErrors = ValidatorWrapper.doRequired(messageVO.getMobileNo(), validationErrors, "Customer Mobile No");
        validationErrors = ValidatorWrapper.doRequired(messageVO.getCnic(), validationErrors, "CNIC");
        validationErrors = ValidatorWrapper.doValidateCNIC(messageVO.getCnic(), validationErrors, "CNIC");
        validationErrors = ValidatorWrapper.doRequired(messageVO.getDob(), validationErrors, "Date of Birth");
        validationErrors = ValidatorWrapper.doRequired(messageVO.getCnicExpiry(), validationErrors, "CNIC Expiry");

        if (validationErrors.hasValidationErrors()) {
            throw new CommandException(validationErrors.getErrors(), ErrorCodes.VALIDATION_ERROR, ErrorLevel.HIGH, new Throwable());
        }

    }


    @Override
    public WebServiceVO createCustomer(WebServiceVO webServiceVO, boolean isConsumerApp) throws FrameworkCheckedException {
        FonePayMessageVO messageVO = new FonePayMessageVO();
        VerisysDataModel verisysDataModel = new VerisysDataModel();
        if (StringUtil.isNullOrEmpty(webServiceVO.getCnicExpiry())) {
            webServiceVO.setCnicExpiry("2099-01-01");
        }

        AppUserModel appEmailUserModel = null;
        appEmailUserModel = this.isEmailUnique(webServiceVO.getEmailAddress());

        if (null != appEmailUserModel) {
            throw new CommandException("Email Address Already Exists", ErrorCodes.EMAIL_ADDRESS_ALREADY_EXISTS, ErrorLevel.MEDIUM, null);
        }

        boolean isConventional = false, accountUpdated = false, accountCreated = false;
        Date nowDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        long accountType = NumberUtils.toLong(webServiceVO.getAccountType());
        ArrayList<CustomerPictureModel> arrayCustomerPictures = new ArrayList<CustomerPictureModel>();
        AppUserModel testModel1 = new AppUserModel();
        List<AppUserModel> modelList = new ArrayList<>();
        AppUserModel aUserModel = null;
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
        try {
            accountType = NumberUtils.toLong(webServiceVO.getAccountType());
            ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
            exampleHolder.setMatchMode(MatchMode.EXACT);
            testModel1.setMobileNo(webServiceVO.getMobileNo());
            testModel1.setNic(webServiceVO.getCnicNo());
            testModel1.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
            modelList = appUserHibernateDAO.findByExample(testModel1, null, null, exampleHolder).getResultsetList();
            if (modelList != null && modelList.size() > 0) {
                aUserModel = modelList.get(0);
            }
            if (aUserModel != null && RegistrationStateConstants.DISCREPANT.equals(aUserModel.getRegistrationStateId())) {
                if (accountType == CustomerAccountTypeConstants.LEVEL_0) {
                    if (isConsumerApp) {
                        if (!webServiceVO.getCustomerPhoto().equals("")) {
                            addCustomerPicture(PictureTypeConstants.CUSTOMER_PHOTO, webServiceVO.getCustomerPhoto(), false, arrayCustomerPictures);
                        }
                        if (!webServiceVO.getCnicFrontPhoto().equals("")) {
                            addCustomerPicture(PictureTypeConstants.ID_FRONT_SNAPSHOT, webServiceVO.getCnicFrontPhoto(), false, arrayCustomerPictures);
                        }

                    } else {
                        if (!webServiceVO.getCustomerPhoto().equals("")) {
                            addCustomerPicture(PictureTypeConstants.CUSTOMER_PHOTO, arrayCustomerPictures, nowDate, webServiceVO.getCustomerPhoto());
                        }
                        if (!webServiceVO.getCnicFrontPhoto().equals("")) {
                            addCustomerPicture(PictureTypeConstants.ID_FRONT_SNAPSHOT, arrayCustomerPictures, nowDate, webServiceVO.getCnicFrontPhoto());
                        }
                    }

                }
                if (accountType == CustomerAccountTypeConstants.LEVEL_1) {
                    VerisysDataModel vo = new VerisysDataModel();
                    if (isConsumerApp)
                        vo.setName(CommonUtils.escapeUnicode(webServiceVO.getConsumerName()));
                    else
                        vo.setName(CommonUtils.escapeUnicode(webServiceVO.getFirstName()) + " " + CommonUtils.escapeUnicode(webServiceVO.getLastName()));
                    vo.setMotherMaidenName(CommonUtils.escapeUnicode(webServiceVO.getMotherMaiden()));
                    vo.setCurrentAddress(CommonUtils.escapeUnicode(webServiceVO.getPresentAddress()));
                    vo.setPlaceOfBirth(CommonUtils.escapeUnicode(webServiceVO.getBirthPlace()));
                    vo.setCnic(webServiceVO.getCnicNo());
                    vo.setAccountClosed(false);
                    vo.setAppUserId(aUserModel.getAppUserId());
                    vo.setCreatedOn(new Date());
                    vo.setUpdatedOn(new Date());
                    vo.setTranslated(false);
                    vo.setPermanentAddress(CommonUtils.escapeUnicode(webServiceVO.getPresentAddress()));
                    getCommonCommandManager().getVerisysDataHibernateDAO().saveNadraData(vo);
                }
                getCommonCommandManager().updateOpenCustomerL0Request(webServiceVO, aUserModel, arrayCustomerPictures, isConsumerApp);
                accountUpdated = true;
            } else {
                if (accountType == CustomerAccountTypeConstants.LEVEL_0) {
                    isConventional = true;
                    if (isConsumerApp) {
                        logger.info("Account Opening Method id is : " + AccountOpeningMethodConstantsInterface.SELF_REGISTERATION);
                        logger.info("[FonePayManagerImpl.createCustomer] Conventional Account Opening Flow started based on given accountType:" + webServiceVO.getAccountType());
                    } else {
                        logger.info("Account Opening Method id is : " + AccountOpeningMethodConstantsInterface.FONEPAY);
                        logger.info("[FonePayManagerImpl.createCustomer] Conventional Account Opening Flow started based on given accountType:" + webServiceVO.getAccountType());

                    }
                } else {
                    if (isConsumerApp) {
                        logger.info("Account Opening Method id is : " + AccountOpeningMethodConstantsInterface.SELF_REGISTERATION);
                        logger.info("[FonePayManagerImpl.createCustomer] Paysys Account Opening Flow started based on given accountType:" + webServiceVO.getAccountType());
                    } else {
                        logger.info("Account Opening Method id is : " + AccountOpeningMethodConstantsInterface.FONEPAY);
                        logger.info("[FonePayManagerImpl.createCustomer] Paysys Account Opening Flow started based on given accountType:" + webServiceVO.getAccountType());
                    }
                }

                String newAccountFlag = webServiceVO.getReserved1();
                NadraIntegrationVO iVo = new NadraIntegrationVO();
                if (newAccountFlag.equals("1")) {
                    String mobileNo = webServiceVO.getMobileNo();
                    String cnic = webServiceVO.getCnicNo();
                    String cnicIssueDate = webServiceVO.getCnicIssuanceDate();
                    iVo.setCnicIssuanceDate(cnicIssueDate);
                    iVo.setContactNo(mobileNo);
                    iVo.setCitizenNumber(cnic);
                    iVo.setAreaName("Punjab");
                    logger.info("Nadra Info: ");
                    iVo = this.getNadraIntegrationController().getCitizenData(iVo);
                    if (!iVo.getResponseCode().equals("100"))
                        throw new CommandException(iVo.getResponseDescription(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);
                    logger.info("Nadra Verfication data for NIC: " + cnic + " Mother Name: " + iVo.getMotherName());
                    messageVO.setCustomerName(iVo.getFullName());
                    messageVO.setDob(iVo.getDateOfBirth());
                    messageVO.setPresentAddress(iVo.getPresentAddress());
                    messageVO.setBirthPlace(iVo.getBirthPlace());
                    messageVO.setFatherHusbandName(iVo.getFatherName());
                    messageVO.setCnicExpiry(iVo.getCardExpire());
                    messageVO.setPermanentAddress(iVo.getPermanentAddress());
                    messageVO.setMotherName(iVo.getMotherName());

                    if (iVo.getMotherName() == null) {
                        iVo.setMotherName("Mother");
                    }
//                    //temp
//
//                    messageVO.setCustomerName("ahsan khan");
//                    messageVO.setDob("1990-08-19");
//                    iVo.setResponseCode("100");
//                    messageVO.setPresentAddress("bahwalpur house no 8g");
//                    messageVO.setBirthPlace("bahwalpur");
//                    messageVO.setFatherHusbandName("javed");
//                    messageVO.setCnicExpiry("2025-01-01");
//                    messageVO.setPermanentAddress("bahwalpur");
//                    messageVO.setMotherName("Nusrat bano");
//                    iVo.setDateOfBirth("1990-08-19");
//                    iVo.setCardExpire("2020-05-01");
//                    iVo.setMotherName("Nusrat Bano");
//                    iVo.setFullName("Ahsan Raza");
//                    iVo.setGender("MALE");
                }

//                if (webServiceVO.getReserved10().equals(DeviceTypeConstantsInterface.WEB_SERVICE.toString())) {
//                    SimpleDateFormat formatter3 = new SimpleDateFormat("yyyy");
//                    Date dateOfBirth = dateFormat.parse(webServiceVO.getDateOfBirth());
////                    String dateOfBirth = formatter3.format(webServiceVO.getDateOfBirth());
//                    String transmissionDateTime = new SimpleDateFormat("yyyyMMddHHss").format(new Date());
//                    String stan = String.valueOf((new Random().nextInt(90000000)));
//                    requestVO = ESBAdapter.prepareCLSRequest(I8SBConstants.RequestType_CLSJS_ImportScreening);
//                    requestVO.setName(webServiceVO.getConsumerName());
//                    requestVO.setCNIC(webServiceVO.getCnicNo());
//                    requestVO.setDateOfBirth(new SimpleDateFormat("yyyy").format(dateOfBirth));
//                    requestVO.setNationality("Pakistan");
//                    requestVO.setRequestId(transmissionDateTime + stan);
//                    requestVO.setMobileNumber(webServiceVO.getMobileNo());
//                    requestVO.setCity("");
//
//                    SwitchWrapper sWrapper = new SwitchWrapperImpl();
//                    sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
//                    sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
//                    sWrapper = esbAdapter.makeI8SBCall(sWrapper);
//                    ESBAdapter.processI8sbResponseCode(sWrapper.getI8SBSwitchControllerResponseVO(), false);
//                    responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();
//
//                    if (!responseVO.getResponseCode().equals("I8SB-200"))
//                        throw new CommandException(responseVO.getDescription(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);
//                } else {
//                    if (iVo.getResponseCode().equals("100")) {
//                        SimpleDateFormat formatter3 = new SimpleDateFormat("yyyy");
//                        Date dateOfBirth = dateFormat.parse(messageVO.getDob());
////                        String dateOfBirth = formatter3.format(messageVO.getDob());
//                        String transmissionDateTime = new SimpleDateFormat("yyyyMMddHHss").format(new Date());
//                        String stan = String.valueOf((new Random().nextInt(90000000)));
//                        requestVO = ESBAdapter.prepareCLSRequest(I8SBConstants.RequestType_CLSJS_ImportScreening);
//                        requestVO.setName(messageVO.getCustomerName());
//                        requestVO.setCNIC(webServiceVO.getCnicNo());
//                        requestVO.setDateOfBirth(new SimpleDateFormat("yyyy").format(dateOfBirth));
//                        requestVO.setNationality("Pakistan");
//                        requestVO.setRequestId(transmissionDateTime + stan);
//                        requestVO.setMobileNumber(webServiceVO.getMobileNo());
//                        if (iVo.getBirthPlace() != null) {
//                            requestVO.setCity(iVo.getBirthPlace());
//                        } else {
//                            requestVO.setCity("");
//                        }
//
//                        SwitchWrapper sWrapper = new SwitchWrapperImpl();
//                        sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
//                        sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
//                        sWrapper = esbAdapter.makeI8SBCall(sWrapper);
//                        ESBAdapter.processI8sbResponseCode(sWrapper.getI8SBSwitchControllerResponseVO(), false);
//                        responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();
//
//                        if (!responseVO.getResponseCode().equals("I8SB-200"))
//                            throw new CommandException(responseVO.getDescription(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);
////                    if(responseVO.getStatus().equals("Open Alert")){
////                        if(responseVO.getStatus().equals("PEP/EDD-Open|Private-Open")){
////                            String pendingStatus= String.valueOf(RegistrationStateConstants.CLSPENDING);
////                        }
////                    }
//
//                    }
//                }
                messageVO.setCustomerAccountyType(String.valueOf(accountType));
                messageVO.setCnic(webServiceVO.getCnicNo());
                String customerMobileNetwork = null;
                if (webServiceVO.getMobileNo().contains("/")) {
                    String[] parts = webServiceVO.getMobileNo().split("/");
                    customerMobileNetwork = parts[1];
                    webServiceVO.setMobileNo(parts[0]);
                }
                messageVO.setMobileNo(webServiceVO.getMobileNo());
                messageVO.setCustomerName(webServiceVO.getConsumerName());


                if (accountType == CustomerAccountTypeConstants.LEVEL_0) {
                    if (newAccountFlag.equals("1")) {
                        messageVO.setCustomerName(iVo.getFullName());
                        messageVO.setPresentAddress(iVo.getPresentAddress());
                        messageVO.setBirthPlace(iVo.getBirthPlace());
                        messageVO.setFatherHusbandName(iVo.getFatherName());
                        messageVO.setMotherName(iVo.getMotherName());

                    } else {
                        messageVO.setPresentAddress(webServiceVO.getPresentAddress());
                        messageVO.setBirthPlace(webServiceVO.getBirthPlace());
                        messageVO.setFatherHusbandName(webServiceVO.getFatherHusbandName());
                        messageVO.setMotherName(webServiceVO.getMotherMaiden());
                    }
                }

                if (newAccountFlag.equals("1")) {
                    messageVO.setDob(iVo.getDateOfBirth());
                } else {
                    messageVO.setDob(webServiceVO.getDateOfBirth());
                }

                if (newAccountFlag.equals("1")) {
                    messageVO.setCnicExpiry(iVo.getCardExpire());
                } else {
                    if (!(webServiceVO.getCnicExpiry().equals("Lifetime"))) {
                        messageVO.setCnicExpiry(webServiceVO.getCnicExpiry());
                    } else
                        messageVO.setCnicExpiry("2099-01-01");
                }
                if (!isConventional) {
                    messageVO.setBirthPlace(webServiceVO.getBirthPlace());
                    messageVO.setMotherName(webServiceVO.getMotherMaiden());
                    messageVO.setPresentAddress(webServiceVO.getPresentAddress());
                    verisysDataModel.setCnic(webServiceVO.getCnicNo());
                    verisysDataModel.setPlaceOfBirth(webServiceVO.getBirthPlace());
                    verisysDataModel.setMotherMaidenName(webServiceVO.getMotherMaiden());
                    verisysDataModel.setName(webServiceVO.getConsumerName());
                    verisysDataModel.setCurrentAddress(webServiceVO.getPresentAddress());
                    verisysDataModel.setPermanentAddress(webServiceVO.getPresentAddress());
                    verisysDataModel.setTranslated(false);
                    verisysDataModel.setCreatedOn(new Date());
                    verisysDataModel.setUpdatedOn(new Date());
                    //messageVO.setPermanentAddress(webServiceVO.getPermanentAddress());
                }
                BaseWrapper baseWrapper = new BaseWrapperImpl();


                /*****************************************************************************************
                 * Validating input params
                 */


                try {
                    this.validateInstantAccOpeningRequest(messageVO);

                    if (isConventional) {
                        if (accountType != CustomerAccountTypeConstants.LEVEL_0) {
                            if (StringUtil.isNullOrEmpty(webServiceVO.getCustomerPhoto())) {
                                throw new CommandException("Customer picture is required", ErrorCodes.VALIDATION_ERROR, ErrorLevel.MEDIUM);
                            }
                            if (StringUtil.isNullOrEmpty(webServiceVO.getCnicFrontPhoto())) {
                                throw new CommandException("CNIC front picture is required", ErrorCodes.VALIDATION_ERROR, ErrorLevel.MEDIUM);
                            }
                        }
                    }
                } catch (CommandException e) {
                    if (isConsumerApp) {
                        logger.error("[FonePayManagerImpl.createCustomer] Validation Failed:" + e.getMessage());
                    } else {
                        logger.error("[FonePayManagerImpl.createCustomer] Validation Failed:" + e.getMessage());
                    }
                    webServiceVO.setResponseCode(FonePayResponseCodes.ACCOUNT_OPENING_VALIDATION_FAILED);
                    webServiceVO.setResponseCodeDescription(e.getMessage());
                    throw new FrameworkCheckedException(e.getMessage());
                }

                try {


                    if (isConventional) {
                        if (isConsumerApp) {
                            if (!newAccountFlag.equals("1")) {
                                addCustomerPicture(PictureTypeConstants.CUSTOMER_PHOTO, webServiceVO.getCustomerPhoto(), false, arrayCustomerPictures);
                                addCustomerPicture(PictureTypeConstants.ID_FRONT_SNAPSHOT, webServiceVO.getCnicFrontPhoto(), false, arrayCustomerPictures);
                            }
                        } else if (webServiceVO.getCustomerPhoto() != null && webServiceVO.getCnicFrontPhoto() != null) {
                            addCustomerPicture(PictureTypeConstants.CUSTOMER_PHOTO, arrayCustomerPictures, nowDate, webServiceVO.getCustomerPhoto());
                            addCustomerPicture(PictureTypeConstants.ID_FRONT_SNAPSHOT, arrayCustomerPictures, nowDate, webServiceVO.getCnicFrontPhoto());
                        } else if (webServiceVO.getReserved4() != null && webServiceVO.getReserved4().equals("1")) {
                            if (webServiceVO.getMinorCustomerPic() != null) {
                                addCustomerPicture(PictureTypeConstants.CUSTOMER_PHOTO, arrayCustomerPictures, nowDate, webServiceVO.getMinorCustomerPic());
                            }
                            if (webServiceVO.getbFormPic() != null) {
                                addCustomerPicture(PictureTypeConstants.B_FORM_SNAPSHOT, arrayCustomerPictures, nowDate, webServiceVO.getbFormPic());
                            }
                            if (webServiceVO.getParentCnicPic() != null) {
                                addCustomerPicture(PictureTypeConstants.PARENT_CNIC_SNAPSHOT, arrayCustomerPictures, nowDate, webServiceVO.getParentCnicPic());
                            }
                            if (webServiceVO.getSnicPic() != null) {
                                addCustomerPicture(PictureTypeConstants.ID_FRONT_SNAPSHOT, arrayCustomerPictures, nowDate, webServiceVO.getSnicPic());
                            }
                            if (webServiceVO.getReserved5() != null) {
                                addCustomerPicture(PictureTypeConstants.ID_BACK_SNAPSHOT, arrayCustomerPictures, nowDate, webServiceVO.getReserved5());
                            }
                            if (webServiceVO.getReserved6() != null) {
                                addCustomerPicture(PictureTypeConstants.PARENT_CNIC_BACK_SNAPSHOT, arrayCustomerPictures, nowDate, webServiceVO.getReserved6());
                            }


                        } else {

                            //Saving Empty Pictures
                            addEmptyPicture(PictureTypeConstants.TERMS_AND_CONDITIONS_COPY, arrayCustomerPictures, nowDate);
                            addEmptyPicture(PictureTypeConstants.SIGNATURE_SNAPSHOT, arrayCustomerPictures, nowDate);
                            addEmptyPicture(PictureTypeConstants.ID_BACK_SNAPSHOT, arrayCustomerPictures, nowDate);
                            addEmptyPicture(PictureTypeConstants.AC_OPENING_FORM_SNAPSHOT, arrayCustomerPictures, nowDate);
                        }
                    } else {
                        //Saving all Empty Pictures
                        addEmptyPicture(PictureTypeConstants.TERMS_AND_CONDITIONS_COPY, arrayCustomerPictures, nowDate);
                        addEmptyPicture(PictureTypeConstants.CUSTOMER_PHOTO, arrayCustomerPictures, nowDate);
                        addEmptyPicture(PictureTypeConstants.SIGNATURE_SNAPSHOT, arrayCustomerPictures, nowDate);
                        addEmptyPicture(PictureTypeConstants.ID_FRONT_SNAPSHOT, arrayCustomerPictures, nowDate);
                        addEmptyPicture(PictureTypeConstants.ID_BACK_SNAPSHOT, arrayCustomerPictures, nowDate);
                        addEmptyPicture(PictureTypeConstants.AC_OPENING_FORM_SNAPSHOT, arrayCustomerPictures, nowDate);

                    }


                    baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_PICTURES_COLLECTION, arrayCustomerPictures);


                    UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();

                    //***************************************************************************************
                    // * Populating the Customer Model

                    CustomerModel customerModel = new CustomerModel();
                    customerModel.setNadraTrackingId(webServiceVO.getTrackingId());
                    customerModel.setRegister(true);
                    customerModel.setCreatedByAppUserModel(ThreadLocalAppUser.getAppUserModel());
                    customerModel.setUpdatedByAppUserModel(ThreadLocalAppUser.getAppUserModel());
                    customerModel.setCreatedOn(nowDate);
                    customerModel.setUpdatedOn(nowDate);
                    customerModel.setCustomerAccountTypeId(Long.valueOf(messageVO.getCustomerAccountyType()));
                    customerModel.setApplicationN0(getCommonCommandManager().getDeviceApplicationNoGenerator().nextLongValue().toString());


                    customerModel.setContactNo(messageVO.getMobileNo());
                    customerModel.setName(messageVO.getCustomerName());


                    customerModel.setMobileNo(messageVO.getMobileNo());
                    customerModel.setFatherHusbandName(messageVO.getFatherHusbandName());
                    customerModel.setRelationAskari(0);
                    customerModel.setRelationZong(0);
                    customerModel.setBirthPlace(messageVO.getBirthPlace());
                    customerModel.setEmail(webServiceVO.getEmailAddress());
                    if (webServiceVO.getReserved4() != null && webServiceVO.getReserved4().equals("1")) {
                        customerModel.setFatherCnicIssuanceDate(dateFormat.parse(webServiceVO.getFatherCnicIssuanceDate()));
                        customerModel.setFatherCnicExpiryDate(dateFormat.parse(webServiceVO.getFatherCnicExpiryDate()));
                        customerModel.setFatherMotherMobileNo(webServiceVO.getFatherMotherMobileNumber());
                        customerModel.setFatherCnicNo(webServiceVO.getFatherCnic());
                        customerModel.setMotherCnicNo(webServiceVO.getMotherCnic());
                    }
                    customerModel.setCompanyName(webServiceVO.getReserved2());
                    if (isConsumerApp || FonePayConstants.APIGEE_CHANNEL.equals(webServiceVO.getChannelId())) {
                        customerModel.setSegmentId(CommissionConstantsInterface.DEFAULT_SEGMENT_ID);
                    } else if (isConsumerApp || MessageUtil.getMessage("ChannelId").equals(webServiceVO.getChannelId())) {
                        if (webServiceVO.getReserved4() != null && webServiceVO.getReserved4().equals("1")) {
                            customerModel.setSegmentId(Long.valueOf(MessageUtil.getMessage("Minor_segment_id")));

                        }
                        else if (webServiceVO.getReserved3() != null && webServiceVO.getReserved3().equals("MERCHANT")) {
                            customerModel.setSegmentId(Long.valueOf(MessageUtil.getMessage("Merchant_segment_id")));

                        }
                        else {
                            customerModel.setSegmentId(Long.valueOf(MessageUtil.getMessage("SegmentId")));
                        }
                    } else if (isConsumerApp || MessageUtil.getMessage("EcofinChannelId").equals(webServiceVO.getChannelId())) {
                        if (webServiceVO.getReserved4() != null && webServiceVO.getReserved4().equals("1")) {
                            customerModel.setSegmentId(Long.valueOf(MessageUtil.getMessage("Minor_segment_id")));

                        }
                        else if (webServiceVO.getReserved3() != null && webServiceVO.getReserved4().equals("MERCHANT")) {
                            customerModel.setSegmentId(Long.valueOf(MessageUtil.getMessage("Merchant_segment_id")));

                        }
                        else {
                            customerModel.setSegmentId(Long.valueOf(MessageUtil.getMessage("EcofinSegmentId")));
                        }
                    } else if (isConsumerApp || MessageUtil.getMessage("AmaChannelId").equals(webServiceVO.getChannelId())) {
                        if (webServiceVO.getReserved4() != null && webServiceVO.getReserved4().equals("1")) {
                            customerModel.setSegmentId(Long.valueOf(MessageUtil.getMessage("Minor_segment_id")));

                        }
                        else if (webServiceVO.getReserved3() != null && webServiceVO.getReserved4().equals("MERCHANT")) {
                            customerModel.setSegmentId(Long.valueOf(MessageUtil.getMessage("Merchant_segment_id")));

                        }
                        else {
                            customerModel.setSegmentId(Long.valueOf(MessageUtil.getMessage("AmaSegmentId")));
                            customerModel.setAccountOpenedByAma(true);
                            customerModel.setAmaCustomerContsent("1");
                        }

                    } else if (isConsumerApp || MessageUtil.getMessage("PayFastChannelId").equals(webServiceVO.getChannelId())) {
                        if (webServiceVO.getReserved4() != null && webServiceVO.getReserved4().equals("1")) {
                            customerModel.setSegmentId(Long.valueOf(MessageUtil.getMessage("Minor_segment_id")));

                        }
                        else if (webServiceVO.getReserved3() != null && webServiceVO.getReserved4().equals("Merchant")) {
                            customerModel.setSegmentId(Long.valueOf(MessageUtil.getMessage("Merchant_segment_id")));

                        }
                        else {
                            customerModel.setSegmentId(Long.valueOf(MessageUtil.getMessage("PayFastSegmentId")));
                        }
                    }
                    else if (isConsumerApp || MessageUtil.getMessage("BrandVerseChannelId").equals(webServiceVO.getChannelId())) {
                        if (webServiceVO.getReserved4() != null && webServiceVO.getReserved4().equals("1")) {
                            customerModel.setSegmentId(Long.valueOf(MessageUtil.getMessage("Minor_segment_id")));

                        }
                        else if (webServiceVO.getReserved3() != null && webServiceVO.getReserved3().equals("MERCHANT")) {
                            customerModel.setSegmentId(Long.valueOf(MessageUtil.getMessage("Merchant_segment_id")));
                        }
                        else {
                            customerModel.setSegmentId(Long.valueOf(MessageUtil.getMessage("BrandVerseSegmentId")));
                        }
                    }

                    else {
                        customerModel.setSegmentId(CommissionConstantsInterface.FONEPAY_SEGMENT_ID);
                    }
                    customerModel.setCustomerTypeId(CustomerTypeConstants.CUSTOMER_TYPE_MARKETED);
                    customerModel.setIsCnicSeen(false);
                    customerModel.setClsResponseCode(responseVO.getCaseStatus());

                    if (!isConsumerApp) {
                        customerModel.setIsCnicSeen(true);
                        customerModel.setWebServiceEnabled(true);
                    } else
                        customerModel.setWebServiceEnabled(true);

                    customerModel.setScreeningPerformed(Boolean.FALSE);
                    customerModel.setIsMPINGenerated(Boolean.FALSE);

                    if (isConventional) {
                        customerModel.setVerisysDone(false);
                    } else {
                        customerModel.setVerisysDone(true);
                    }

                    if (isConsumerApp) {
                        customerModel.setAccountMethodId(AccountOpeningMethodConstantsInterface.SELF_REGISTERATION);
                    } else {
                        if (FonePayConstants.APIGEE_CHANNEL.equals(webServiceVO.getChannelId())) {
                            customerModel.setAccountMethodId(AccountOpeningMethodConstantsInterface.APIGEE);
                        } else {
                            customerModel.setAccountMethodId(AccountOpeningMethodConstantsInterface.FONEPAY);
                            customerModel.setFonePayEnabled(true);
                        }
                    }
                    if (webServiceVO.getGender() != "" && null != webServiceVO.getGender()) {
                        if (webServiceVO.getGender().toUpperCase().equals("FEMALE") || webServiceVO.getGender().toUpperCase().equals("F")) {
                            customerModel.setGender("F");
                        } else if (webServiceVO.getGender().toUpperCase().equals("MALE") || webServiceVO.getGender().toUpperCase().equals("M")) {
                            customerModel.setGender("M");
                        } else if (webServiceVO.getGender().toUpperCase().startsWith("K") || webServiceVO.getGender().toUpperCase().equals("K")) {
                            customerModel.setGender("K");
                        }
                    }
                    if (newAccountFlag.equals("1")) {
                        if (iVo.getGender().toUpperCase().equals("FEMALE") || iVo.getGender().toUpperCase().equals("F")) {
                            customerModel.setGender("F");
                        } else if (iVo.getGender().toUpperCase().equals("MALE") || iVo.getGender().toUpperCase().equals("M")) {
                            customerModel.setGender("M");
                        } else if (iVo.getGender().toUpperCase().startsWith("K") || iVo.getGender().toUpperCase().equals("K")) {
                            customerModel.setGender("K");
                        }
                    }

                    customerModel.setTaxRegimeId(TaxRegimeConstants.FEDERAL);
                    TaxRegimeModel taxRegimeModel = new TaxRegimeModel();
                    taxRegimeModel = this.taxRegimeDAO.findByPrimaryKey(TaxRegimeConstants.FEDERAL);
                    if (taxRegimeModel != null) {
                        customerModel.setFed(taxRegimeModel.getFed());
                    }


                    baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MODEL, customerModel);

                    //***************************************************************************************
                    // * Populating the AppUserModel Model

                    AppUserModel appUserModel = new AppUserModel();

                    String[] nameArray = messageVO.getCustomerName().split(" ");
                    appUserModel.setFirstName(nameArray[0]);
                    if (nameArray.length > 1) {
                        appUserModel.setLastName(messageVO.getCustomerName().substring(
                                appUserModel.getFirstName().length() + 1));
                    }

                    else {
                        appUserModel.setLastName(nameArray[0]);
//                        appUserModel.setLastName(" ");
                    }
                    appUserModel.setAddress1(messageVO.getPresentAddress());
                    appUserModel.setAddress2(messageVO.getPermanentAddress());
                    if (newAccountFlag.equals("1")) {
                        appUserModel.setCustomerMobileNetwork(webServiceVO.getCustomerMobileNetwork());
                    } else {
                        appUserModel.setCustomerMobileNetwork(customerMobileNetwork);
                    }
                    appUserModel.setMobileNo(messageVO.getMobileNo());
                    String nicWithoutHyphins = messageVO.getCnic().replace("-", "");
                    appUserModel.setNic(nicWithoutHyphins);
                    appUserModel.setNicExpiryDate(dateFormat.parse(messageVO.getCnicExpiry()));
                    appUserModel.setMobileTypeId(1L);
                    appUserModel.setPasswordChangeRequired(true);
                    if (newAccountFlag.equals("1")) {
                        appUserModel.setDob(dateFormat.parse(iVo.getDateOfBirth()));
                    } else {
                        appUserModel.setDob(dateFormat.parse(messageVO.getDob()));
                    }
                    if (webServiceVO.getChannelId().equalsIgnoreCase(MessageUtil.getMessage("EcofinChannelId"))) {

                        AppUserModel agentAppUserModel = this.getCommonCommandManager().loadAppUserByMobileAndType(MessageUtil.getMessage("EcofinMobileNumberR"), UserTypeConstantsInterface.RETAILER);

                        appUserModel.setCreatedBy(agentAppUserModel.getAppUserId());
                    } else {
                        appUserModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
                    }
                    appUserModel.setCreatedOn(nowDate);
                    appUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
                    appUserModel.setMotherMaidenName(messageVO.getMotherName());
                    appUserModel.setEmail(webServiceVO.getEmailAddress());
                    //Below parameter are set to change cnic issuance Date format change  for api dd-mm-yyyy to yyyy-mm-dd
                    if ((!webServiceVO.getCnicIssuanceDate().equals("") || webServiceVO.getCnicIssuanceDate() == null) && (webServiceVO.getReserved10().equals(DeviceTypeConstantsInterface.WEB_SERVICE.toString()))) {
                        appUserModel.setCnicIssuanceDate(dateFormat.parse(webServiceVO.getCnicIssuanceDate()));
                    } else if (!webServiceVO.getCnicIssuanceDate().equals("") || webServiceVO.getCnicIssuanceDate() == null) {
                        appUserModel.setCnicIssuanceDate(dateFormat1.parse(webServiceVO.getCnicIssuanceDate()));
                    }

                    //just for mock
//                    if(responseVO.getCaseStatus().equals("No Matches")){
//                        responseVO.setCaseStatus("True Match-Compliance");
//                    }


//                    if(responseVO.getCaseStatus().equals("PEP/EDD-Open|Private-Open") || responseVO.getCaseStatus().equals("GWL-Open|PEP/EDD-Open|Private-Open") ||
//                            responseVO.getCaseStatus().equals("PEP/EDD-Open") || responseVO.getCaseStatus().equals("GWL-Open") ||
//                            responseVO.getCaseStatus().equals("Private-Open") || responseVO.getCaseStatus().equals("Revert to Branch") ||
//                            responseVO.getCaseStatus().equals("True Match-Compliance") || responseVO.getCaseStatus().equals("ERROR-999"))
//                    if (responseVO.getCaseStatus().equalsIgnoreCase("ERROR-999") || responseVO.getCaseStatus().equalsIgnoreCase("ERROR-998")) {
//                        throw new CommandException("Account Opening Request Rejected Due To Compliance ", ErrorCodes.ACCOUNT_OPENING_FAILED, ErrorLevel.MEDIUM, null);
//                    }
//                    if (!(responseVO.getCaseStatus().equalsIgnoreCase("No Matches") || responseVO.getCaseStatus().
//                            equalsIgnoreCase("Passed By Rule") || responseVO.getCaseStatus().
//                            equalsIgnoreCase("False Positive Match") ||
//                            responseVO.getCaseStatus().equalsIgnoreCase("GWL-Passed by Rules") ||
//                            responseVO.getCaseStatus().equalsIgnoreCase("GWL-Passed by Rules|PEP/EDD-False Positive|Private-False Positive") ||
//                            responseVO.getCaseStatus().equalsIgnoreCase("Private-False Positive") ||
//                            responseVO.getCaseStatus().equalsIgnoreCase("GWL-Passed by Rules|Private-Passed by Rules") ||
//                            responseVO.getCaseStatus().equalsIgnoreCase("Private-Passed by Rules") ||
//                            responseVO.getCaseStatus().equalsIgnoreCase("No Match")
//                            || responseVO.getCaseStatus().equalsIgnoreCase("GWL-Passed by Rules|PEP/EDD-Passed by Rules|Private-False Positive") ||
//                            responseVO.getCaseStatus().equalsIgnoreCase("GWL-Passed by Rules|PEP/EDD-Passed by Rules|Private-Passed by Rules"))) {
//                        appUserModel.setVerified(false);
//                        appUserModel.setRegistrationStateId(RegistrationStateConstants.CLSPENDING);
//                        appUserModel.setAccountStateId(AccountStateConstantsInterface.CLS_STATE_BLOCKED);
//                        webServiceVO.setReserved9(String.valueOf(appUserModel.getRegistrationStateId()));
//                        webServiceVO.setReserved8("Account Opened in Pending State. Kindly activate your account");
//
//                        if(webServiceVO.getChannelId().equals(MessageUtil.getMessage("AmaChannelId"))) {
//                            webServiceVO.setReserved7(FonePayResponseCodes.AMA_CHANNEL);
//                        }
//
////                        ClsDebitCreditBlockModel clsDebitCreditBlockModel = new ClsDebitCreditBlockModel();
//
//                        List<ClsDebitCreditBlockModel> list = clsDebitCreditDAO.loadClsDebitCreditModel();
//                        if (list != null && !list.isEmpty()) {
//                            for (ClsDebitCreditBlockModel model : list) {
//                                if (model.getState().equals("DEBIT") && model.getStatus().equals("1")) {
//                                    customerModel.setClsDebitBlock("1");
//                                }
//
//                                if (model.getState().equals("CREDIT") && model.getStatus().equals("1")) {
//                                    customerModel.setClsCreditBlock("1");
//                                }
//                            }
//
//                            customerDAO.saveOrUpdate(customerModel);
//                        }
//                    } else if (responseVO.getCaseStatus().equals("True Match") || responseVO.getCaseStatus().equals("True Match-Compliance")) {
//                        appUserModel.setVerified(false);
//                        appUserModel.setRegistrationStateId(RegistrationStateConstants.REJECTED);
//                        appUserModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_REJECTED);
//                        webServiceVO.setReserved9(String.valueOf(appUserModel.getRegistrationStateId()));
//                        webServiceVO.setReserved8("Account Opened in Pending State. Kindly activate your account");
//                    } else {
                    if (isConventional) {
                        appUserModel.setVerified(false);
                        if (newAccountFlag.equals("1")) {
                            appUserModel.setRegistrationStateId(RegistrationStateConstants.VERIFIED);
                            appUserModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_COLD);
                        } else {
                            if (MessageUtil.getMessage("ChannelId").equals(webServiceVO.getChannelId())) {
                                appUserModel.setRegistrationStateId(RegistrationStateConstants.VERIFIED);
                                appUserModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_COLD);
                            } else if (webServiceVO.getChannelId().equals(MessageUtil.getMessage("AmaChannelId"))) {
                                appUserModel.setRegistrationStateId(RegistrationStateConstants.VERIFIED);
                                appUserModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_COLD);
                                webServiceVO.setReserved7(FonePayResponseCodes.AMA_CHANNEL);
                            } else {
                                appUserModel.setRegistrationStateId(RegistrationStateConstants.REQUEST_RECEIVED);
                                appUserModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_WARM);
                            }
                        }
                    } else {
                        appUserModel.setVerified(true);
                        appUserModel.setRegistrationStateId(RegistrationStateConstants.VERIFIED);
                        appUserModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_COLD);
                    }
//                    }

                    baseWrapper.putObject(CommandFieldConstants.KEY_CASE_STATUS, responseVO.getCaseStatus());
                    appUserModel.setCountryId(1L);
                    appUserModel.setAccountEnabled(true);
                    appUserModel.setAccountExpired(false);
                    appUserModel.setAccountLocked(false);
                    appUserModel.setCredentialsExpired(false);
                    appUserModel.setAccountClosedUnsettled(false);
                    appUserModel.setAccountClosedSettled(false);

                    appUserModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
                    appUserModel.setUpdatedOn(nowDate);

                    String mfsId = computeMfsId();
                    String username = mfsId;

                    String password = "1231";
                    String randomPin = "";
                    if (isConsumerApp) {
                        randomPin = RandomUtils.generateRandom(4, false, true);
                        password = com.inov8.microbank.common.util.EncryptionUtil.encryptWithAES(XMLConstants.AES_ENCRYPTION_KEY, randomPin);//EncoderUtils.encodeToSha(randomPin);
                        if (null == password) {
                            password = "1231";
                        }
                        appUserModel.setUsername(username);
                        appUserModel.setPassword(password);
                    }

                    appUserModel.setUsername(username);
                    appUserModel.setPassword(password);

                    //As per new implementation suggested by Sir Zulfiqar
                    webServiceVO.setReserved8("Account Opened in Pending State. Kindly activate your account");
                    appUserModel.setRegistrationStateId(RegistrationStateConstants.CLSPENDING);
                    appUserModel.setAccountStateId(AccountStateConstantsInterface.CLS_STATE_BLOCKED);
                    webServiceVO.setReserved9(String.valueOf(appUserModel.getRegistrationStateId()));
                    baseWrapper.putObject(CommandFieldConstants.KEY_APP_USER_MODEL, appUserModel);


                    if (messageVO.getPresentAddress() != null && !"".equals(messageVO.getPresentAddress())) {
                        AddressModel customerPresentAddressModel = new AddressModel();
                        customerPresentAddressModel.setHouseNo(messageVO.getPresentAddress());
                        customerPresentAddressModel.setFullAddress(messageVO.getPresentAddress());
                        baseWrapper.putObject(CommandFieldConstants.KEY_PRESENT_ADDR, customerPresentAddressModel);
                    }
                    if (messageVO.getPermanentAddress() != null && !"".equals(messageVO.getPermanentAddress())) {
                        AddressModel customerPermanentAddressModel = new AddressModel();
                        customerPermanentAddressModel.setHouseNo(messageVO.getPermanentAddress());
                        customerPermanentAddressModel.setFullAddress(messageVO.getPermanentAddress());
                        baseWrapper.putObject(CommandFieldConstants.KEY_PERMANENT_ADDR, customerPermanentAddressModel);
                    }

                    //***************************************************************************************
                    //** Populating the UserDeviceAccountsModel

                    userDeviceAccountsModel.setAccountEnabled(true);

                    userDeviceAccountsModel.setCommissioned(false);
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
                    userDeviceAccountsModel.setPin(password);
                    userDeviceAccountsModel.setProdCatalogId(PortalConstants.CUSTOMER_DEFAULT_CATALOG);

                    baseWrapper.putObject(CommandFieldConstants.KEY_USER_DEVICE_ACCOUNT_MODEL, userDeviceAccountsModel);

                    Long bankId = getCommonCommandManager().getOlaBankMadal().getBankId();

                    SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
                    smartMoneyAccountModel.setBankId(bankId);
                    smartMoneyAccountModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
                    smartMoneyAccountModel.setCreatedOn(new Date());
                    smartMoneyAccountModel.setUpdatedOn(new Date());
                    smartMoneyAccountModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
                    smartMoneyAccountModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
                    smartMoneyAccountModel.setActive(true);
                    smartMoneyAccountModel.setStatusId(OlaStatusConstants.ACCOUNT_STATUS_ACTIVE);
                    smartMoneyAccountModel.setChangePinRequired(false);
                    smartMoneyAccountModel.setDefAccount(true);
                    smartMoneyAccountModel.setDeleted(false);
                    smartMoneyAccountModel.setName("i8_bb_" + mfsId);
                    smartMoneyAccountModel.setAccountClosedUnsetteled(0L);
                    smartMoneyAccountModel.setAccountClosedSetteled(0L);

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
                    if (newAccountFlag.equals("1")) {
                        olaVo.setDob(dateFormat.parse(iVo.getDateOfBirth()));
                    } else {
                        olaVo.setDob(dateFormat.parse(messageVO.getDob()));

                    }
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

                    if (isConventional) {
                        baseWrapper.putObject("isBvsAccount", Boolean.FALSE); // PAYSYS Not Performed
                    } else {
                        baseWrapper.putObject("isBvsAccount", Boolean.TRUE); // PAYSYS Already Performed
                    }
                    if (!isConventional) {
                        baseWrapper.putObject(CommandFieldConstants.KEY_VARISYS_DATA_MODEL, verisysDataModel);
                    }

                    if (isConsumerApp) {
                        baseWrapper.putObject(CommandFieldConstants.KEY_APP_ID, "2");
                    }

                    //Validation Cnic Expire

//                    CommonUtils.checkCnicExpiry(messageVO.getCnicExpiry());

                    SimpleDateFormat formatter1 = new SimpleDateFormat("d");
                    SimpleDateFormat formatter2 = new SimpleDateFormat("M");
                    SimpleDateFormat formatter3 = new SimpleDateFormat("yyyy");
                    String d = formatter1.format(appUserModel.getDob());
                    String m = formatter2.format(appUserModel.getDob());
                    String y = formatter3.format(appUserModel.getDob());
                    System.out.println(y + "," + m + "," + d);
                    if (!(webServiceVO.getReserved4() != null && webServiceVO.getReserved4().equals("1"))) {
                        CommonUtils.checkAgeLimit(y, m, d, 18);
                    }

                    baseWrapper = getCommonCommandManager().saveOrUpdateAccountOpeningL0Request(baseWrapper);

                    //data insertion on cls pending account opening Table
//                    ClsPendingAccountOpeningModel clsPendingAccountOpeningModel = new ClsPendingAccountOpeningModel();
//                    clsPendingAccountOpeningModel.setMobileNo(webServiceVO.getMobileNo());
//                    clsPendingAccountOpeningModel.setCnic(webServiceVO.getCnicNo());
//                    clsPendingAccountOpeningModel.setConsumerName(webServiceVO.getConsumerName());
//                    clsPendingAccountOpeningModel.setFatherHusbandName(webServiceVO.getFatherHusbandName());
//                    clsPendingAccountOpeningModel.setMotherMaidenName(webServiceVO.getMotherMaiden());
//                    clsPendingAccountOpeningModel.setCaseStatus(responseVO.getCaseStatus());
//                    clsPendingAccountOpeningModel.setCaseID(responseVO.getCaseId());
//                    clsPendingAccountOpeningModel.setGender(webServiceVO.getGender());
//                    clsPendingAccountOpeningModel.setCnicIssuanceDate(dateFormat.parse(webServiceVO.getCnicIssuanceDate()));
//                    if (webServiceVO.getReserved10().equals(DeviceTypeConstantsInterface.WEB_SERVICE.toString())) {
//                        clsPendingAccountOpeningModel.setDob(dateFormat.parse(webServiceVO.getDateOfBirth()));
//                    } else {
//                        clsPendingAccountOpeningModel.setDob(dateFormat.parse(messageVO.getDob()));
//                    }
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
//                    if (appUserModel.getRegistrationStateId().equals(RegistrationStateConstants.VERIFIED)) {
//                        clsPendingAccountOpeningModel.setClsBotStatus(1);
//                        clsPendingAccountOpeningModel.setIsCompleted("1");
//                        clsPendingAccountOpeningModel.setIsSmsRequired(false);
//                    }
//                    this.genericDAO.createEntity(clsPendingAccountOpeningModel);

                    accountCreated = true;

//                    if(isConsumerApp) {
//                        if (webServiceVO.getChannelId().equals(FonePayConstants.NOVA_CHANNEL)) {
//                            String customerSMS = this.getMessageSource().getMessage("smsCommand.act_sms_jsbl_nova", null, null);
//                            baseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, new SmsMessage(webServiceVO.getMobileNo(), customerSMS));
//                            getCommonCommandManager().sendSMSToUser(baseWrapper);
//                        } else {
//                            sendSMSToUsers(webServiceVO.getMobileNo(), null, isConsumerApp);
//                        }
//                    }
//
//                    sendSMSToUsers(webServiceVO.getMobileNo(), randomPin, isConsumerApp);

                    if (isConsumerApp && !appUserModel.getRegistrationStateId().equals(RegistrationStateConstants.CLSPENDING)) {
                        sendSMSToUsers(webServiceVO.getMobileNo(), randomPin, isConsumerApp, appUserModel.getRegistrationStateId());
                    }
//                    else{
                    if (webServiceVO.getChannelId() != null) {
                        if (webServiceVO.getChannelId().equals(FonePayConstants.NOVA_CHANNEL)) {
                            String customerSMS = this.getMessageSource().getMessage("smsCommand.act_sms_jsbl_nova", null, null);
                            baseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, new SmsMessage(webServiceVO.getMobileNo(), customerSMS));
                            getCommonCommandManager().sendSMSToUser(baseWrapper);
                        } else {
                            sendSMSToUsers(webServiceVO.getMobileNo(), randomPin, isConsumerApp, appUserModel.getRegistrationStateId());
                        }
                    } else {
                        sendSMSToUsers(webServiceVO.getMobileNo(), randomPin, isConsumerApp, appUserModel.getRegistrationStateId());
                    }
//                    }

//					IvrRequestDTO ivrDTO = new IvrRequestDTO();
//					ivrDTO.setCustomerMobileNo(appUserModel.getMobileNo());
//					ivrDTO.setRetryCount(0);
//					ivrDTO.setProductId(new Long(CommandFieldConstants.CREATE_PIN_IVR));
//					try {
//						getCommonCommandManager().initiateUserGeneratedPinIvrCall(ivrDTO);
//					}catch(Exception e){
//						if(isConsumerApp){
//							logger.error("[createCustomer] Error occurred while routing IVR call for PIN generation. Message:"+e.getMessage(), e);
//						}else{
//							logger.error("[createCustomer] Error occurred while routing IVR call for PIN generation. Message:"+e.getMessage(), e);
//						}
//
//						throw e;
//					}
                    if (isConsumerApp || !webServiceVO.getChannelId().equals(FonePayConstants.NOVA_CHANNEL)) {
                        MiddlewareAdviceVO middlewareAdviceVO = new MiddlewareAdviceVO();
                        middlewareAdviceVO.setStan(String.valueOf((100000 + new Random().nextInt(900000))));
                        middlewareAdviceVO.setRequestTime(new Date());
                        middlewareAdviceVO.setDateTimeLocalTransaction(new Date());
                        middlewareAdviceVO.setTransmissionTime(new Date());
                        middlewareAdviceVO.setAdviceType(CoreAdviceUtil.ACCOUNNT_OPENING_ADVICE);
                        middlewareAdviceVO.setProductId(ProductConstantsInterface.CUST_ACCOUNT_OPENING);
                        middlewareAdviceVO.setConsumerNo(appUserModel.getMobileNo());
                        middlewareAdviceVO.setCnicNo(appUserModel.getNic());

                        transactionReversalManager.sendCoreReversalRequiresNewTransaction(middlewareAdviceVO);

//                        SwitchWrapper switchWrapper = new SwitchWrapperImpl();
//                        switchWrapper.setBaseWrapper(baseWrapper);

                        WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();

                        workFlowWrapper.putObject(CommandFieldConstants.KEY_APP_USER_MODEL, appUserModel);
                        workFlowWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MODEL, customerModel);
                        workFlowWrapper.putObject(CommandFieldConstants.KEY_SMART_MONEY_ACCOUNT_MODEL, smartMoneyAccountModel);
                        workFlowWrapper.setProductId(ProductConstantsInterface.CUST_ACCOUNT_OPENING);

                        loadAndForwardAccountToQueue(workFlowWrapper);


                    }
                } catch (Exception ex) {
                    String errorMessage = ex.getMessage();
                    if (accountCreated) {
                        if (isConsumerApp) {
                            logger.error("Customer Creation via Self Registeration - Successful , but exception occurred... Customer Mobile No:" + messageVO.getMobileNo() + " errorMessage:" + errorMessage + "\n Exception: ", ex);
                        } else {
                            logger.error("Customer Creation via FonePay - Successful , but exception occurred... Customer Mobile No:" + messageVO.getMobileNo() + " errorMessage:" + errorMessage + "\n Exception: ", ex);
                        }

                    } else {
                        if (isConsumerApp) {
                            logger.error("Customer Creation via Self Registeration - Failed ... Customer Mobile No:" + messageVO.getMobileNo() + " errorMessage:" + errorMessage + "\n Exception: ", ex);
                        } else {
                            logger.error("Customer Creation via FonePay - Failed ... Customer Mobile No:" + messageVO.getMobileNo() + " errorMessage:" + errorMessage + "\n Exception: ", ex);
                        }

                    }

                    if (errorMessage == null ||
                            (!StringUtil.isNullOrEmpty(errorMessage) && (errorMessage.contains("Hibernate")
                                    || errorMessage.contains("Exception")
                                    || errorMessage.contains("SQLException")))) {
//                        if (errorMessage.equals(WorkFlowErrorCodeConstants.CNIC_ALREARY_EXIST)) {
//                            webServiceVO.setResponseCode(FonePayResponseCodes.CUSTOMER_CNIC_ALREADY_EXIST);
//                            webServiceVO.setResponseCodeDescription(FonePayUtils.getResponceCodeDescription(errorMessage));
//                        }
//                        else {

                            webServiceVO.setResponseCode(FonePayResponseCodes.ACCOUNT_OPENING_FAILED);
                            webServiceVO.setResponseCodeDescription(FonePayUtils.getResponceCodeDescription(FonePayResponseCodes.ACCOUNT_OPENING_FAILED));
//                        }
                    } else if (errorMessage == null ||
                            (!StringUtil.isNullOrEmpty(errorMessage) && (errorMessage.equals(MessageUtil.getMessage("MINOR.ACCOUNT.OPENING"))))) {
                        webServiceVO.setResponseCode(FonePayResponseCodes.ACCOUNT_OPENING_AGE_LIMIT_FAILED);
                        webServiceVO.setResponseCodeDescription(errorMessage);
                    } else if (errorMessage == null ||
                            (!StringUtil.isNullOrEmpty(errorMessage) && (errorMessage.equals(MessageUtil.getMessage("CNIC.EXPIRE.ACCOUNT.OPENING"))))) {
                        webServiceVO.setResponseCode(FonePayResponseCodes.ACCOUNT_OPENING_CNIC_EXPIRY_FAILED);
                        webServiceVO.setResponseCodeDescription(errorMessage);

                    }
//                    else  if (errorMessage.equals(WorkFlowErrorCodeConstants.CNIC_ALREARY_EXIST)) {
//                        webServiceVO.setResponseCode(FonePayResponseCodes.CUSTOMER_CNIC_ALREADY_EXIST);
//                        webServiceVO.setResponseCodeDescription(FonePayUtils.getResponceCodeDescription(errorMessage));
//                    }
                    else {
                        webServiceVO.setResponseCode(FonePayResponseCodes.ACCOUNT_OPENING_FAILED);
                        webServiceVO.setResponseCodeDescription(errorMessage);
                    }
                }

                if (accountCreated) {
                    webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                    webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
                } else {
                    throw new FrameworkCheckedException(FonePayUtils.getResponceCodeDescription(webServiceVO.getResponseCode()));
                }
            }
        } catch (CommandException ce) {
            throw new CommandException(ce.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);
        }

/*				if(isConsumerApp){
					logger.info("[FonePayManagerImpl.createCustomerBySelfRegisteration] Returning Response Code: "+webServiceVO.getResponseCode() + ", RespCodeDesc:" + webServiceVO.getResponseCodeDescription());


				}else{
					logger.info("[FonePayManagerImpl.createCustomerViaFonePay] Returning Response Code: "+webServiceVO.getResponseCode() + ", RespCodeDesc:" + webServiceVO.getResponseCodeDescription());

				}*/ catch (Exception ex) {
            String errorMessage = ex.getMessage();

            if (accountUpdated) {
                if (isConsumerApp) {
                    logger.error("Customer Updation via Self Registeration - Successful , but exception occurred... Customer Mobile No:" + messageVO.getMobileNo() + " errorMessage:" + errorMessage + "\n Exception: ", ex);
                } else {
                    logger.error("Customer Updation via FonePay - Successful , but exception occurred... Customer Mobile No:" + messageVO.getMobileNo() + " errorMessage:" + errorMessage + "\n Exception: ", ex);
                }

            } else {
                if (isConsumerApp) {
                    logger.error("Customer Updation via Self Registeration - Failed ... Customer Mobile No:" + messageVO.getMobileNo() + " errorMessage:" + errorMessage + "\n Exception: ", ex);
                } else {
                    logger.error("Customer Updation via FonePay - Failed ... Customer Mobile No:" + messageVO.getMobileNo() + " errorMessage:" + errorMessage + "\n Exception: ", ex);
                }

            }

            if (errorMessage == null ||
                    (!StringUtil.isNullOrEmpty(errorMessage) && (errorMessage.contains("Hibernate")
                            || errorMessage.contains("Exception")
                            || errorMessage.contains("SQLException")))) {

                webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
                webServiceVO.setResponseCodeDescription(FonePayUtils.getResponceCodeDescription(FonePayResponseCodes.ACCOUNT_OPENING_FAILED));
            } else if (errorMessage == null || (!StringUtil.isNullOrEmpty(errorMessage) && (errorMessage.equals(MessageUtil.getMessage("MINOR.ACCOUNT.OPENING"))))) {
                webServiceVO.setResponseCode(FonePayResponseCodes.ACCOUNT_OPENING_AGE_LIMIT_FAILED);
                webServiceVO.setResponseCodeDescription(errorMessage);
            } else if (errorMessage == null ||
                    (!StringUtil.isNullOrEmpty(errorMessage) && (errorMessage.equals(MessageUtil.getMessage("CNIC.EXPIRE.ACCOUNT.OPENING"))))) {
                webServiceVO.setResponseCode(FonePayResponseCodes.ACCOUNT_OPENING_CNIC_EXPIRY_FAILED);
                webServiceVO.setResponseCodeDescription(errorMessage);

            }
//            else  if (errorMessage.equals(MessageUtil.getMessage("fonepay.error.24"))) {
//                webServiceVO.setResponseCode(FonePayResponseCodes.CUSTOMER_CNIC_ALREADY_EXIST);
//                webServiceVO.setResponseCodeDescription(WorkFlowErrorCodeConstants.CNIC_ALREARY_EXIST);
//            }
            else {
                webServiceVO.setResponseCode(FonePayResponseCodes.ACCOUNT_OPENING_FAILED);
                webServiceVO.setResponseCodeDescription(errorMessage);
            }
        }

        if (accountCreated) {

        } else {
            if (accountUpdated) {
                webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
            } else {
                throw new FrameworkCheckedException(FonePayUtils.getResponceCodeDescription(webServiceVO.getResponseCode()));
            }
        }


        return webServiceVO;
    }

    @Override
    public WebServiceVO bulkCreateCustomer(WebServiceVO webServiceVO, long segmentId) throws FrameworkCheckedException {
        FonePayMessageVO messageVO = new FonePayMessageVO();
        VerisysDataModel verisysDataModel = new VerisysDataModel();
        if (StringUtil.isNullOrEmpty(webServiceVO.getCnicExpiry())) {
            webServiceVO.setCnicExpiry("2099-01-01");
        }

        if(webServiceVO.getEmailAddress() != null) {
            AppUserModel appEmailUserModel = null;
            appEmailUserModel = this.isEmailUnique(webServiceVO.getEmailAddress());

            if (null != appEmailUserModel) {
                throw new CommandException("Email Address Already Exists", ErrorCodes.EMAIL_ADDRESS_ALREADY_EXISTS, ErrorLevel.MEDIUM, null);
            }
        }

        AppUserModel existingAppUserModel = new AppUserModel();
        existingAppUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);

        if(existingAppUserModel != null){
            throw new CommandException("Mobile Number Already Exists", ErrorCodes.INVALID_USER, ErrorLevel.MEDIUM, null);
        }

        AppUserModel existingNicAppUserModel = new AppUserModel();
        existingNicAppUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByCnicAndType(webServiceVO.getCnicNo());

        if(existingNicAppUserModel != null){
            throw new CommandException("Cnic Already Exists", ErrorCodes.INVALID_USER, ErrorLevel.MEDIUM, null);
        }

        if (this.getCommonCommandManager().isCnicBlacklisted(webServiceVO.getCnicNo())) {
            throw new CommandException("Cnic is Blacklisted", ErrorCodes.INVALID_USER, ErrorLevel.MEDIUM, null);
        }

        boolean isConventional = false, accountUpdated = false, accountCreated = false;
        Date nowDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        long accountType = CustomerAccountTypeConstants.LEVEL_0;
        ArrayList<CustomerPictureModel> arrayCustomerPictures = new ArrayList<CustomerPictureModel>();
        AppUserModel testModel1 = new AppUserModel();
        List<AppUserModel> modelList = new ArrayList<>();
        AppUserModel aUserModel = null;
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
        try {
//            accountType = NumberUtils.toLong(webServiceVO.getAccountType());
            ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
            exampleHolder.setMatchMode(MatchMode.EXACT);
            testModel1.setMobileNo(webServiceVO.getMobileNo());
            testModel1.setNic(webServiceVO.getCnicNo());
            testModel1.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
            modelList = appUserHibernateDAO.findByExample(testModel1, null, null, exampleHolder).getResultsetList();
            if (modelList != null && modelList.size() > 0) {
                aUserModel = modelList.get(0);
            }

            isConventional = true;

            logger.info("Account Opening Method id is : " + AccountOpeningMethodConstantsInterface.FONEPAY);
            logger.info("[FonePayManagerImpl.createCustomer] Conventional Account Opening Flow started based on given accountType:" + webServiceVO.getAccountType());

            NadraIntegrationVO iVo = new NadraIntegrationVO();
//                if (newAccountFlag.equals("1")) {
            String mobileNo = webServiceVO.getMobileNo();
            String cnic = webServiceVO.getCnicNo();
            String cnicIssueDate = webServiceVO.getCnicIssuanceDate();
            iVo.setCnicIssuanceDate(cnicIssueDate);
            iVo.setContactNo(mobileNo);
            iVo.setCitizenNumber(cnic);
            iVo.setAreaName("Punjab");
            logger.info("Nadra Info: ");

//            messageVO.setCustomerName("ahsan khan");
//            messageVO.setDob("1990-08-19");
//            iVo.setResponseCode("100");
//            messageVO.setPresentAddress("bahwalpur house no 8g");
//            messageVO.setBirthPlace("bahwalpur");
//            messageVO.setFatherHusbandName("javed");
//            messageVO.setCnicExpiry("2025-01-01");
//            messageVO.setPermanentAddress("bahwalpur");
//            messageVO.setMotherName("Nusrat bano");
//            iVo.setDateOfBirth("1990-08-19");
//            iVo.setCardExpire("2020-05-01");
//            iVo.setMotherName("Nusrat Bano");
//            iVo.setFullName("Ahsan Raza");
//            iVo.setGender("MALE");

            iVo = this.getNadraIntegrationController().getCitizenData(iVo);
            if (!iVo.getResponseCode().equals("100"))
                throw new CommandException(iVo.getResponseDescription(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);
            logger.info("Nadra Verfication data for NIC: " + cnic + " Mother Name: " + iVo.getMotherName());
            messageVO.setCustomerName(iVo.getFullName());
            messageVO.setDob(iVo.getDateOfBirth());
            messageVO.setPresentAddress(iVo.getPresentAddress());
            messageVO.setBirthPlace(iVo.getBirthPlace());
            messageVO.setFatherHusbandName(iVo.getFatherName());
            messageVO.setCnicExpiry(iVo.getCardExpire());
            messageVO.setPermanentAddress(iVo.getPermanentAddress());
            messageVO.setMotherName(iVo.getMotherName());

            if (iVo.getMotherName() == null) {
                iVo.setMotherName("Mother");
            }
//                    //temp
            messageVO.setCustomerAccountyType(String.valueOf(accountType));
            messageVO.setCnic(webServiceVO.getCnicNo());
            String customerMobileNetwork = null;
            if (webServiceVO.getMobileNo().contains("/")) {
                String[] parts = webServiceVO.getMobileNo().split("/");
                customerMobileNetwork = parts[1];
                webServiceVO.setMobileNo(parts[0]);
            }
            messageVO.setMobileNo(webServiceVO.getMobileNo());
            messageVO.setCustomerName(webServiceVO.getConsumerName());


            if (accountType == CustomerAccountTypeConstants.LEVEL_0) {
//                    if (newAccountFlag.equals("1")) {
                messageVO.setCustomerName(iVo.getFullName());
                messageVO.setPresentAddress(iVo.getPresentAddress());
                messageVO.setBirthPlace(iVo.getBirthPlace());
                messageVO.setFatherHusbandName(iVo.getFatherName());
                messageVO.setMotherName(iVo.getMotherName());

            }

            messageVO.setDob(iVo.getDateOfBirth());

            messageVO.setCnicExpiry(iVo.getCardExpire());

            if (!isConventional) {
                messageVO.setBirthPlace(webServiceVO.getBirthPlace());
                messageVO.setMotherName(webServiceVO.getMotherMaiden());
                messageVO.setPresentAddress(webServiceVO.getPresentAddress());
                verisysDataModel.setCnic(webServiceVO.getCnicNo());
                verisysDataModel.setPlaceOfBirth(webServiceVO.getBirthPlace());
                verisysDataModel.setMotherMaidenName(webServiceVO.getMotherMaiden());
                verisysDataModel.setName(webServiceVO.getConsumerName());
                verisysDataModel.setCurrentAddress(webServiceVO.getPresentAddress());
                verisysDataModel.setPermanentAddress(webServiceVO.getPresentAddress());
                verisysDataModel.setTranslated(false);
                verisysDataModel.setCreatedOn(new Date());
                verisysDataModel.setUpdatedOn(new Date());
                //messageVO.setPermanentAddress(webServiceVO.getPermanentAddress());
            }
            BaseWrapper baseWrapper = new BaseWrapperImpl();


            /*****************************************************************************************
             * Validating input params
             */


            try {
                this.validateInstantAccOpeningRequest(messageVO);

                if (isConventional) {
                    if (accountType != CustomerAccountTypeConstants.LEVEL_0) {
                        if (StringUtil.isNullOrEmpty(webServiceVO.getCustomerPhoto())) {
                            throw new CommandException("Customer picture is required", ErrorCodes.VALIDATION_ERROR, ErrorLevel.MEDIUM);
                        }
                        if (StringUtil.isNullOrEmpty(webServiceVO.getCnicFrontPhoto())) {
                            throw new CommandException("CNIC front picture is required", ErrorCodes.VALIDATION_ERROR, ErrorLevel.MEDIUM);
                        }
                    }
                }
            } catch (CommandException e) {
//                    if (isConsumerApp) {
                logger.error("[FonePayManagerImpl.createCustomer] Validation Failed:" + e.getMessage());
//                    } else {
//                        logger.error("[FonePayManagerImpl.createCustomer] Validation Failed:" + e.getMessage());
//                    }
                webServiceVO.setResponseCode(FonePayResponseCodes.ACCOUNT_OPENING_VALIDATION_FAILED);
                webServiceVO.setResponseCodeDescription(e.getMessage());
                throw new FrameworkCheckedException(e.getMessage());
            }

            try {
                UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();

                //***************************************************************************************
                // * Populating the Customer Model

                CustomerModel customerModel = new CustomerModel();
                customerModel.setNadraTrackingId(webServiceVO.getTrackingId());
                customerModel.setRegister(true);
                customerModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                customerModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                customerModel.setCreatedOn(nowDate);
                customerModel.setUpdatedOn(nowDate);
                customerModel.setCustomerAccountTypeId(Long.valueOf(messageVO.getCustomerAccountyType()));
                customerModel.setApplicationN0(getCommonCommandManager().getDeviceApplicationNoGenerator().nextLongValue().toString());
                customerModel.setContactNo(messageVO.getMobileNo());
                customerModel.setName(messageVO.getCustomerName());
                customerModel.setMobileNo(messageVO.getMobileNo());
                customerModel.setFatherHusbandName(messageVO.getFatherHusbandName());
                customerModel.setRelationAskari(0);
                customerModel.setRelationZong(0);
                customerModel.setBirthPlace(messageVO.getBirthPlace());
                customerModel.setEmail(webServiceVO.getEmailAddress());

                customerModel.setCompanyName(webServiceVO.getReserved2());

                customerModel.setSegmentId(segmentId);
                customerModel.setCustomerTypeId(CustomerTypeConstants.CUSTOMER_TYPE_MARKETED);
                customerModel.setIsCnicSeen(false);
                customerModel.setClsResponseCode(responseVO.getCaseStatus());

                customerModel.setWebServiceEnabled(true);

                customerModel.setScreeningPerformed(Boolean.FALSE);
                customerModel.setIsMPINGenerated(Boolean.FALSE);

                if (isConventional) {
                    customerModel.setVerisysDone(false);
                } else {
                    customerModel.setVerisysDone(true);
                }

//                    if (isConsumerApp) {
                customerModel.setAccountMethodId(AccountOpeningMethodConstantsInterface.SELF_REGISTERATION);

                if (iVo.getGender().toUpperCase().equals("FEMALE") || iVo.getGender().toUpperCase().equals("F")) {
                    customerModel.setGender("F");
                } else if (iVo.getGender().toUpperCase().equals("MALE") || iVo.getGender().toUpperCase().equals("M")) {
                    customerModel.setGender("M");
                } else if (iVo.getGender().toUpperCase().startsWith("K") || iVo.getGender().toUpperCase().equals("K")) {
                    customerModel.setGender("K");
                }
//                    }

                customerModel.setTaxRegimeId(TaxRegimeConstants.FEDERAL);
                TaxRegimeModel taxRegimeModel = new TaxRegimeModel();
                taxRegimeModel = this.taxRegimeDAO.findByPrimaryKey(TaxRegimeConstants.FEDERAL);
                if (taxRegimeModel != null) {
                    customerModel.setFed(taxRegimeModel.getFed());
                }


                baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MODEL, customerModel);

                //***************************************************************************************
                // * Populating the AppUserModel Model

                AppUserModel appUserModel = new AppUserModel();

                String[] nameArray = messageVO.getCustomerName().split(" ");
                appUserModel.setFirstName(nameArray[0]);
                if (nameArray.length > 1) {
                    appUserModel.setLastName(messageVO.getCustomerName().substring(
                            appUserModel.getFirstName().length() + 1));
                } else {
                    appUserModel.setLastName(nameArray[0]);
                }
                appUserModel.setAddress1(messageVO.getPresentAddress());
                appUserModel.setAddress2(messageVO.getPermanentAddress());
//                    if (newAccountFlag.equals("1")) {
//                        appUserModel.setCustomerMobileNetwork(webServiceVO.getCustomerMobileNetwork());
//                    } else {
                appUserModel.setCustomerMobileNetwork(customerMobileNetwork);
//                    }
                appUserModel.setMobileNo(messageVO.getMobileNo());
                String nicWithoutHyphins = messageVO.getCnic().replace("-", "");
                appUserModel.setNic(nicWithoutHyphins);
                appUserModel.setNicExpiryDate(dateFormat.parse(messageVO.getCnicExpiry()));
                appUserModel.setMobileTypeId(1L);
                appUserModel.setPasswordChangeRequired(true);
                appUserModel.setDob(dateFormat.parse(iVo.getDateOfBirth()));

                appUserModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
//                    }
                appUserModel.setCreatedOn(nowDate);
                appUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
                appUserModel.setMotherMaidenName(messageVO.getMotherName());
                appUserModel.setEmail(webServiceVO.getEmailAddress());
                //Below parameter are set to change cnic issuance Date format change  for api dd-mm-yyyy to yyyy-mm-dd

                appUserModel.setCnicIssuanceDate(dateFormat1.parse(webServiceVO.getCnicIssuanceDate()));

                appUserModel.setVerified(true);
                appUserModel.setRegistrationStateId(RegistrationStateConstants.VERIFIED);
                appUserModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_COLD);
                appUserModel.setCountryId(1L);
                appUserModel.setAccountEnabled(true);
                appUserModel.setAccountExpired(false);
                appUserModel.setAccountLocked(false);
                appUserModel.setCredentialsExpired(false);
                appUserModel.setAccountClosedUnsettled(false);
                appUserModel.setAccountClosedSettled(false);

                appUserModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
                appUserModel.setUpdatedOn(nowDate);

                String mfsId = computeMfsId();
                String username = mfsId;

                String password = "1231";
                String randomPin = "";
//                    if (isConsumerApp) {
//                        randomPin = RandomUtils.generateRandom(4, false, true);
                password = com.inov8.microbank.common.util.EncryptionUtil.encryptWithAES(XMLConstants.AES_ENCRYPTION_KEY, randomPin);//EncoderUtils.encodeToSha(randomPin);

                appUserModel.setUsername(username);
                appUserModel.setPassword(password);

                //As per new implementation suggested by Sir Zulfiqar

                baseWrapper.putObject(CommandFieldConstants.KEY_APP_USER_MODEL, appUserModel);

                if (messageVO.getPresentAddress() != null && !"".equals(messageVO.getPresentAddress())) {
                    AddressModel customerPresentAddressModel = new AddressModel();
                    customerPresentAddressModel.setHouseNo(messageVO.getPresentAddress());
                    customerPresentAddressModel.setFullAddress(messageVO.getPresentAddress());
                    baseWrapper.putObject(CommandFieldConstants.KEY_PRESENT_ADDR, customerPresentAddressModel);
                }
                if (messageVO.getPermanentAddress() != null && !"".equals(messageVO.getPermanentAddress())) {
                    AddressModel customerPermanentAddressModel = new AddressModel();
                    customerPermanentAddressModel.setHouseNo(messageVO.getPermanentAddress());
                    customerPermanentAddressModel.setFullAddress(messageVO.getPermanentAddress());
                    baseWrapper.putObject(CommandFieldConstants.KEY_PERMANENT_ADDR, customerPermanentAddressModel);
                }

                //***************************************************************************************
                //** Populating the UserDeviceAccountsModel

                userDeviceAccountsModel.setAccountEnabled(true);

                userDeviceAccountsModel.setCommissioned(false);
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
                userDeviceAccountsModel.setPin(password);
                userDeviceAccountsModel.setProdCatalogId(PortalConstants.CUSTOMER_DEFAULT_CATALOG);

                baseWrapper.putObject(CommandFieldConstants.KEY_USER_DEVICE_ACCOUNT_MODEL, userDeviceAccountsModel);

                Long bankId = getCommonCommandManager().getOlaBankMadal().getBankId();

                SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
                smartMoneyAccountModel.setBankId(bankId);
                smartMoneyAccountModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
                smartMoneyAccountModel.setCreatedOn(new Date());
                smartMoneyAccountModel.setUpdatedOn(new Date());
                smartMoneyAccountModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
                smartMoneyAccountModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
                smartMoneyAccountModel.setActive(true);
                smartMoneyAccountModel.setStatusId(OlaStatusConstants.ACCOUNT_STATUS_ACTIVE);
                smartMoneyAccountModel.setChangePinRequired(true);
                smartMoneyAccountModel.setDefAccount(true);
                smartMoneyAccountModel.setDeleted(false);
                smartMoneyAccountModel.setName("i8_bb_" + mfsId);
                smartMoneyAccountModel.setAccountClosedUnsetteled(0L);
                smartMoneyAccountModel.setAccountClosedSetteled(0L);

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
//                    if (newAccountFlag.equals("1")) {
                olaVo.setDob(dateFormat.parse(iVo.getDateOfBirth()));
//                    } else {
//                        olaVo.setDob(dateFormat.parse(messageVO.getDob()));
//
//                    }
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

//                    if (isConventional) {
                baseWrapper.putObject("isBvsAccount", Boolean.FALSE); // PAYSYS Not Performed

                if (!isConventional) {
                    baseWrapper.putObject(CommandFieldConstants.KEY_VARISYS_DATA_MODEL, verisysDataModel);
                }

//                    SimpleDateFormat formatter1 = new SimpleDateFormat("d");
//                    SimpleDateFormat formatter2 = new SimpleDateFormat("M");
//                    SimpleDateFormat formatter3 = new SimpleDateFormat("yyyy");
//                    String d = formatter1.format(appUserModel.getDob());
//                    String m = formatter2.format(appUserModel.getDob());
//                    String y = formatter3.format(appUserModel.getDob());
//                    System.out.println(y + "," + m + "," + d);
//                    if (!(webServiceVO.getReserved4() != null && webServiceVO.getReserved4().equals("1"))) {
//                        CommonUtils.checkAgeLimit(y, m, d, 18);
//                    }

                baseWrapper = getCommonCommandManager().saveOrUpdateAccountOpeningL0Request(baseWrapper);

                //data insertion on cls pending account opening Table
                accountCreated = true;

                String customerSMS = this.getMessageSource().getMessage("smsCommand.act_sms_jsbl_nova", null, null);
                baseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, new SmsMessage(webServiceVO.getMobileNo(), customerSMS));
                getCommonCommandManager().sendSMSToUser(baseWrapper);

            } catch (Exception ex) {
                String errorMessage = ex.getMessage();
                if (accountCreated) {
//                        if (isConsumerApp) {
                    logger.error("Customer Creation via Self Registeration - Successful , but exception occurred... Customer Mobile No:" + messageVO.getMobileNo() + " errorMessage:" + errorMessage + "\n Exception: ", ex);
//                        } else {
//                            logger.error("Customer Creation via FonePay - Successful , but exception occurred... Customer Mobile No:" + messageVO.getMobileNo() + " errorMessage:" + errorMessage + "\n Exception: ", ex);
//                        }

                } else {
                    logger.error("Customer Creation via FonePay - Failed ... Customer Mobile No:" + messageVO.getMobileNo() + " errorMessage:" + errorMessage + "\n Exception: ", ex);
                }

                if (errorMessage == null ||
                        (!StringUtil.isNullOrEmpty(errorMessage) && (errorMessage.contains("Hibernate")
                                || errorMessage.contains("Exception")
                                || errorMessage.contains("SQLException")))) {

                    webServiceVO.setResponseCode(FonePayResponseCodes.ACCOUNT_OPENING_FAILED);
                    webServiceVO.setResponseCodeDescription(FonePayUtils.getResponceCodeDescription(FonePayResponseCodes.ACCOUNT_OPENING_FAILED));

                } else if (errorMessage == null ||
                        (!StringUtil.isNullOrEmpty(errorMessage) && (errorMessage.equals(MessageUtil.getMessage("MINOR.ACCOUNT.OPENING"))))) {
                    webServiceVO.setResponseCode(FonePayResponseCodes.ACCOUNT_OPENING_AGE_LIMIT_FAILED);
                    webServiceVO.setResponseCodeDescription(errorMessage);
                } else if (errorMessage == null ||
                        (!StringUtil.isNullOrEmpty(errorMessage) && (errorMessage.equals(MessageUtil.getMessage("CNIC.EXPIRE.ACCOUNT.OPENING"))))) {
                    webServiceVO.setResponseCode(FonePayResponseCodes.ACCOUNT_OPENING_CNIC_EXPIRY_FAILED);
                    webServiceVO.setResponseCodeDescription(errorMessage);

                }
                else {
                    webServiceVO.setResponseCode(FonePayResponseCodes.ACCOUNT_OPENING_FAILED);
                    webServiceVO.setResponseCodeDescription(errorMessage);
                }
            }

            if (accountCreated) {
                webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
            } else {
                throw new FrameworkCheckedException(FonePayUtils.getResponceCodeDescription(webServiceVO.getResponseCode()));
            }
//            }
        } catch (CommandException ce) {
            throw new CommandException(ce.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);
        }

        catch (Exception ex) {
            String errorMessage = ex.getMessage();

            if (accountUpdated) {
//                if (isConsumerApp) {
                logger.error("Customer Updation via Self Registeration - Successful , but exception occurred... Customer Mobile No:" + messageVO.getMobileNo() + " errorMessage:" + errorMessage + "\n Exception: ", ex);

            } else {
                logger.error("Customer Updation via FonePay - Failed ... Customer Mobile No:" + messageVO.getMobileNo() + " errorMessage:" + errorMessage + "\n Exception: ", ex);
            }

            if (errorMessage == null ||
                    (!StringUtil.isNullOrEmpty(errorMessage) && (errorMessage.contains("Hibernate")
                            || errorMessage.contains("Exception")
                            || errorMessage.contains("SQLException")))) {

                webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
                webServiceVO.setResponseCodeDescription(FonePayUtils.getResponceCodeDescription(FonePayResponseCodes.ACCOUNT_OPENING_FAILED));
            } else if (errorMessage == null || (!StringUtil.isNullOrEmpty(errorMessage) && (errorMessage.equals(MessageUtil.getMessage("MINOR.ACCOUNT.OPENING"))))) {
                webServiceVO.setResponseCode(FonePayResponseCodes.ACCOUNT_OPENING_AGE_LIMIT_FAILED);
                webServiceVO.setResponseCodeDescription(errorMessage);
            } else if (errorMessage == null ||
                    (!StringUtil.isNullOrEmpty(errorMessage) && (errorMessage.equals(MessageUtil.getMessage("CNIC.EXPIRE.ACCOUNT.OPENING"))))) {
                webServiceVO.setResponseCode(FonePayResponseCodes.ACCOUNT_OPENING_CNIC_EXPIRY_FAILED);
                webServiceVO.setResponseCodeDescription(errorMessage);

            }

            else {
                webServiceVO.setResponseCode(FonePayResponseCodes.ACCOUNT_OPENING_FAILED);
                webServiceVO.setResponseCodeDescription(errorMessage);
            }
        }

        if (accountCreated) {

        } else {
            if (accountUpdated) {
                webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
            } else {
                throw new FrameworkCheckedException(FonePayUtils.getResponceCodeDescription(webServiceVO.getResponseCode()));
            }
        }


        return webServiceVO;
    }

    @Override
    public WebServiceVO createL2Customer(WebServiceVO webServiceVO, boolean isConsumerApp) throws FrameworkCheckedException {
        FonePayMessageVO messageVO = new FonePayMessageVO();
        VerisysDataModel verisysDataModel = new VerisysDataModel();
        if (StringUtil.isNullOrEmpty(webServiceVO.getCnicExpiry())) {
            webServiceVO.setCnicExpiry("2099-01-01");
        }
        boolean isConventional = false, accountUpdated = false, accountCreated = false;
        Date nowDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        long accountType = NumberUtils.toLong(webServiceVO.getAccountType());
        ArrayList<CustomerPictureModel> arrayCustomerPictures = new ArrayList<CustomerPictureModel>();
        AppUserModel testModel1 = new AppUserModel();
        List<AppUserModel> modelList = new ArrayList<>();
        AppUserModel aUserModel = null;
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
        try {
            accountType = NumberUtils.toLong(webServiceVO.getAccountType());

            ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
            exampleHolder.setMatchMode(MatchMode.EXACT);
            testModel1.setMobileNo(webServiceVO.getMobileNo());
            testModel1.setNic(webServiceVO.getCnicNo());
            testModel1.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
            modelList = appUserHibernateDAO.findByExample(testModel1, null, null, exampleHolder).getResultsetList();
            if (modelList != null && modelList.size() > 0) {
                aUserModel = modelList.get(0);
            }
            if (aUserModel != null && RegistrationStateConstants.DISCREPANT.equals(aUserModel.getRegistrationStateId())) {

                if (accountType == CustomerAccountTypeConstants.LEVEL_1) {
                    VerisysDataModel vo = new VerisysDataModel();
                    if (isConsumerApp)
                        vo.setName(CommonUtils.escapeUnicode(webServiceVO.getConsumerName()));
                    else
                        vo.setName(CommonUtils.escapeUnicode(webServiceVO.getFirstName()) + " " + CommonUtils.escapeUnicode(webServiceVO.getLastName()));
                    vo.setMotherMaidenName(CommonUtils.escapeUnicode(webServiceVO.getMotherMaiden()));
                    vo.setCurrentAddress(CommonUtils.escapeUnicode(webServiceVO.getPresentAddress()));
                    vo.setPlaceOfBirth(CommonUtils.escapeUnicode(webServiceVO.getBirthPlace()));
                    vo.setCnic(webServiceVO.getCnicNo());
                    vo.setAccountClosed(false);
                    vo.setAppUserId(aUserModel.getAppUserId());
                    vo.setCreatedOn(new Date());
                    vo.setUpdatedOn(new Date());
                    vo.setTranslated(false);
                    vo.setPermanentAddress(CommonUtils.escapeUnicode(webServiceVO.getPresentAddress()));
                    getCommonCommandManager().getVerisysDataHibernateDAO().saveNadraData(vo);
                }
                getCommonCommandManager().updateOpenCustomerL0Request(webServiceVO, aUserModel, arrayCustomerPictures, isConsumerApp);
                accountUpdated = true;
            } else {
                if (accountType == CustomerAccountTypeConstants.LEVEL_0) {
                    isConventional = true;
                    if (isConsumerApp) {
                        logger.info("Account Opening Method id is : " + AccountOpeningMethodConstantsInterface.SELF_REGISTERATION);
                        logger.info("[FonePayManagerImpl.createCustomer] Conventional Account Opening Flow started based on given accountType:" + webServiceVO.getAccountType());
                    } else {
                        logger.info("Account Opening Method id is : " + AccountOpeningMethodConstantsInterface.FONEPAY);
                        logger.info("[FonePayManagerImpl.createCustomer] Conventional Account Opening Flow started based on given accountType:" + webServiceVO.getAccountType());

                    }
                } else {
                    if (isConsumerApp) {
                        logger.info("Account Opening Method id is : " + AccountOpeningMethodConstantsInterface.SELF_REGISTERATION);
                        logger.info("[FonePayManagerImpl.createCustomer] Paysys Account Opening Flow started based on given accountType:" + webServiceVO.getAccountType());
                    } else {
                        logger.info("Account Opening Method id is : " + AccountOpeningMethodConstantsInterface.FONEPAY);
                        logger.info("[FonePayManagerImpl.createCustomer] Paysys Account Opening Flow started based on given accountType:" + webServiceVO.getAccountType());
                    }
                }

                String newAccountFlag = webServiceVO.getReserved1();
                NadraIntegrationVO iVo = new NadraIntegrationVO();
                if (newAccountFlag.equals("1")) {
                    String mobileNo = webServiceVO.getMobileNo();
                    String cnic = webServiceVO.getCnicNo();
                    String cnicIssueDate = webServiceVO.getCnicIssuanceDate();
                    iVo.setCnicIssuanceDate(cnicIssueDate);
                    iVo.setContactNo(mobileNo);
                    iVo.setCitizenNumber(cnic);
                    iVo.setAreaName("Punjab");
                    logger.info("Nadra Info: ");
                    iVo = this.getNadraIntegrationController().getCitizenData(iVo);
                    if (!iVo.getResponseCode().equals("100"))
                        throw new CommandException(iVo.getResponseDescription(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);
                    logger.info("Nadra Verfication data for NIC: " + cnic + " Mother Name: " + iVo.getMotherName());
                    messageVO.setCustomerName(iVo.getFullName());
                    messageVO.setDob(iVo.getDateOfBirth());
                    messageVO.setPresentAddress(iVo.getPresentAddress());
                    messageVO.setBirthPlace(iVo.getBirthPlace());
                    messageVO.setFatherHusbandName(iVo.getFatherName());
                    messageVO.setCnicExpiry(iVo.getCardExpire());
                    messageVO.setPermanentAddress(iVo.getPermanentAddress());
                    messageVO.setMotherName(iVo.getMotherName());

                    //temp
//
//                    messageVO.setCustomerName("ahsan khan");
//                    messageVO.setDob("1994-08-19");
//                    messageVO.setPresentAddress("bahwalpur house no 8g");
//                    messageVO.setBirthPlace("bahwalpur");
//                    messageVO.setFatherHusbandName("javed");
//                    messageVO.setCnicExpiry("2025-01-01");
//                    messageVO.setPermanentAddress("bahwalpur");
//                    messageVO.setMotherName("Nusrat bano");
//                    iVo.setDateOfBirth("1995-08-19");
//                    iVo.setCardExpire("2025-05-01");
//                    iVo.setMotherName("Nusrat Bano");

                    if (iVo.getMotherName() == null) {
                        iVo.setMotherName("Mother");
                    }
                }

                if (webServiceVO.getReserved10().equals(DeviceTypeConstantsInterface.WEB_SERVICE.toString())) {
                    String transmissionDateTime = new SimpleDateFormat("yyyyMMddHHss").format(new Date());
                    String stan = String.valueOf((new Random().nextInt(90000000)));
                    requestVO = ESBAdapter.prepareCLSRequest(I8SBConstants.RequestType_CLSJS_ImportScreening);
                    requestVO.setName(webServiceVO.getConsumerName());
                    requestVO.setCNIC(webServiceVO.getCnicNo());
                    requestVO.setDateOfBirth(webServiceVO.getDateOfBirth());
                    requestVO.setNationality("Pakistan");
                    requestVO.setRequestId(transmissionDateTime + stan);
                    requestVO.setMobileNumber(webServiceVO.getMobileNo());
//                    requestVO.setFatherName(UserUtils.getCurrentUser().getCustomerIdCustomerModel().getFatherHusbandName());

                    SwitchWrapper sWrapper = new SwitchWrapperImpl();
                    sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                    sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
                    sWrapper = esbAdapter.makeI8SBCall(sWrapper);
                    ESBAdapter.processI8sbResponseCode(sWrapper.getI8SBSwitchControllerResponseVO(), false);
                    responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();

//                    if (!responseVO.getResponseCode().equals("I8SB-200"))
//                        throw new CommandException(responseVO.getDescription(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);

                } else {
                    if (iVo.getResponseCode().equals("100")) {
                        String transmissionDateTime = new SimpleDateFormat("yyyyMMddHHss").format(new Date());
                        String stan = String.valueOf((new Random().nextInt(90000000)));
                        requestVO = ESBAdapter.prepareCLSRequest(I8SBConstants.RequestType_CLSJS_ImportScreening);
                        requestVO.setName(messageVO.getCustomerName());
                        requestVO.setCNIC(webServiceVO.getCnicNo());
                        requestVO.setDateOfBirth(messageVO.getDob());
                        requestVO.setNationality("Pakistan");
                        requestVO.setRequestId(transmissionDateTime + stan);
                        requestVO.setMobileNumber(webServiceVO.getMobileNo());
//                        requestVO.setFatherName(UserUtils.getCurrentUser().getCustomerIdCustomerModel().getFatherHusbandName());

                        SwitchWrapper sWrapper = new SwitchWrapperImpl();
                        sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                        sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
                        sWrapper = esbAdapter.makeI8SBCall(sWrapper);
                        ESBAdapter.processI8sbResponseCode(sWrapper.getI8SBSwitchControllerResponseVO(), false);
                        responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();

//                    if(responseVO.getStatus().equals("Open Alert")){
//                        if(responseVO.getStatus().equals("PEP/EDD-Open|Private-Open")){
//                            String pendingStatus= String.valueOf(RegistrationStateConstants.CLSPENDING);
//                        }
//                    }

                    }
                }
                messageVO.setCustomerAccountyType(String.valueOf(accountType));
                messageVO.setCnic(webServiceVO.getCnicNo());
                String customerMobileNetwork = null;
                if (webServiceVO.getMobileNo().contains("/")) {
                    String[] parts = webServiceVO.getMobileNo().split("/");
                    customerMobileNetwork = parts[1];
                    webServiceVO.setMobileNo(parts[0]);
                }
                messageVO.setMobileNo(webServiceVO.getMobileNo());
                messageVO.setCustomerName(webServiceVO.getConsumerName());


                if (newAccountFlag.equals("1")) {

                    messageVO.setCustomerName(iVo.getFullName());
                    messageVO.setPresentAddress(iVo.getPresentAddress());
                    messageVO.setBirthPlace(iVo.getBirthPlace());
                    messageVO.setFatherHusbandName(iVo.getFatherName());
                    messageVO.setMotherName(iVo.getMotherName());
                } else {
                    messageVO.setPresentAddress(webServiceVO.getPresentAddress());
                    messageVO.setBirthPlace(webServiceVO.getBirthPlace());
                    messageVO.setFatherHusbandName(webServiceVO.getFatherHusbandName());
                    messageVO.setMotherName(webServiceVO.getMotherMaiden());
                }

                if (newAccountFlag.equals("1")) {
                    messageVO.setDob(iVo.getDateOfBirth());
                } else {
                    messageVO.setDob(webServiceVO.getDateOfBirth());
                }

                if (newAccountFlag.equals("1")) {
                    messageVO.setCnicExpiry(iVo.getCardExpire());
                } else {
                    if (!(webServiceVO.getCnicExpiry().equals("Lifetime"))) {
                        messageVO.setCnicExpiry(webServiceVO.getCnicExpiry());
                    } else
                        messageVO.setCnicExpiry("2099-01-01");
                }
//                if (!isConventional) {
//                    messageVO.setBirthPlace(webServiceVO.getBirthPlace());
//                    messageVO.setMotherName(webServiceVO.getMotherMaiden());
//                    messageVO.setPresentAddress(webServiceVO.getPresentAddress());
//                    verisysDataModel.setCnic(webServiceVO.getCnicNo());
//                    verisysDataModel.setPlaceOfBirth(webServiceVO.getBirthPlace());
//                    verisysDataModel.setMotherMaidenName(webServiceVO.getMotherMaiden());
//                    verisysDataModel.setName(webServiceVO.getConsumerName());
//                    verisysDataModel.setCurrentAddress(webServiceVO.getPresentAddress());
//                    verisysDataModel.setPermanentAddress(webServiceVO.getPresentAddress());
//                    verisysDataModel.setTranslated(false);
//                    verisysDataModel.setCreatedOn(new Date());
//                    verisysDataModel.setUpdatedOn(new Date());
//                    //messageVO.setPermanentAddress(webServiceVO.getPermanentAddress());
//                }
                BaseWrapper baseWrapper = new BaseWrapperImpl();

                /*****************************************************************************************
                 * Validating input params
                 */
                try {
                    this.validateInstantAccOpeningRequest(messageVO);

                    if (accountType != CustomerAccountTypeConstants.LEVEL_0) {
                        if (StringUtil.isNullOrEmpty(webServiceVO.getCustomerPhoto())) {
                            throw new CommandException("Customer picture is required", ErrorCodes.VALIDATION_ERROR, ErrorLevel.MEDIUM);
                        }
                        if (StringUtil.isNullOrEmpty(webServiceVO.getCnicFrontPhoto())) {
                            throw new CommandException("CNIC front picture is required", ErrorCodes.VALIDATION_ERROR, ErrorLevel.MEDIUM);
                        }
                    }

                } catch (CommandException e) {
                    if (isConsumerApp) {
                        logger.error("[FonePayManagerImpl.createCustomer] Validation Failed:" + e.getMessage());
                    } else {
                        logger.error("[FonePayManagerImpl.createCustomer] Validation Failed:" + e.getMessage());
                    }
                    webServiceVO.setResponseCode(FonePayResponseCodes.ACCOUNT_OPENING_VALIDATION_FAILED);
                    webServiceVO.setResponseCodeDescription(e.getMessage());
                    throw new FrameworkCheckedException(e.getMessage());
                }

                try {
                    addCustomerPicture(PictureTypeConstants.CUSTOMER_PHOTO, arrayCustomerPictures, nowDate, webServiceVO.getCustomerPhoto());
                    addCustomerPicture(PictureTypeConstants.ID_FRONT_SNAPSHOT, arrayCustomerPictures, nowDate, webServiceVO.getCnicFrontPhoto());
                    addCustomerPicture(PictureTypeConstants.ID_BACK_SNAPSHOT, arrayCustomerPictures, nowDate, webServiceVO.getCnicBackPhoto());
                    addCustomerPicture(PictureTypeConstants.SOURCE_OF_INCOME_SNAPSHOT, arrayCustomerPictures, nowDate, webServiceVO.getReserved2());
                    addCustomerPicture(PictureTypeConstants.PROOF_OF_PROFESSION_SNAPSHOT, arrayCustomerPictures, nowDate, webServiceVO.getReserved3());
                    addCustomerPicture(PictureTypeConstants.ID_FRONT_SNAPSHOT, arrayCustomerPictures, nowDate, webServiceVO.getSnicPic());
                    addCustomerPicture(PictureTypeConstants.PARENT_CNIC_SNAPSHOT, arrayCustomerPictures, nowDate, webServiceVO.getParentCnicPic());
                    addCustomerPicture(PictureTypeConstants.B_FORM_SNAPSHOT, arrayCustomerPictures, nowDate, webServiceVO.getbFormPic());


                    baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_PICTURES_COLLECTION, arrayCustomerPictures);
                    UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
                    //***************************************************************************************
                    // * Populating the Customer Model

                    CustomerModel customerModel = new CustomerModel();
                    customerModel.setNadraTrackingId(webServiceVO.getTrackingId());
                    customerModel.setRegister(true);
                    customerModel.setCreatedByAppUserModel(ThreadLocalAppUser.getAppUserModel());
                    customerModel.setUpdatedByAppUserModel(ThreadLocalAppUser.getAppUserModel());
                    customerModel.setCreatedOn(nowDate);
                    customerModel.setUpdatedOn(nowDate);
                    customerModel.setCustomerAccountTypeId(Long.valueOf(messageVO.getCustomerAccountyType()));
                    customerModel.setApplicationN0(getCommonCommandManager().getDeviceApplicationNoGenerator().nextLongValue().toString());
                    customerModel.setContactNo(messageVO.getMobileNo());
                    customerModel.setName(messageVO.getCustomerName());
                    customerModel.setMobileNo(messageVO.getMobileNo());
                    customerModel.setFatherHusbandName(messageVO.getFatherHusbandName());
                    customerModel.setRelationAskari(0);
                    customerModel.setRelationZong(0);
                    customerModel.setAccountPurposeId(1l);
                    customerModel.setLatitude(webServiceVO.getLatitude());
                    customerModel.setLongitude(webServiceVO.getLongitude());
                    customerModel.setMonthlyTurnOver(webServiceVO.getExpectedMonthlyTurnover());
                    customerModel.setFundsSourceNarration(webServiceVO.getSourceOfIncome());
                    customerModel.setNokName(webServiceVO.getNextOfKin());
                    customerModel.setBirthPlace(messageVO.getBirthPlace());

                    if (!responseVO.getResponseCode().equals("I8SB-200")) {
                        throw new CommandException(responseVO.getDescription(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);

                    } else {
                        customerModel.setClsResponseCode(responseVO.getCaseStatus());
                    }
                    customerModel.setEmail(webServiceVO.getEmailAddress());
//                    customerModel.setCompanyName(webServiceVO.getReserved2());
                    if (isConsumerApp || FonePayConstants.APIGEE_CHANNEL.equals(webServiceVO.getChannelId())) {

                        customerModel.setSegmentId(CommissionConstantsInterface.DEFAULT_SEGMENT_ID);

                    } else if (isConsumerApp || MessageUtil.getMessage("ChannelId").equals(webServiceVO.getChannelId())) {

                        customerModel.setSegmentId(Long.valueOf(MessageUtil.getMessage("SegmentId")));
                    } else {
                        customerModel.setSegmentId(CommissionConstantsInterface.FONEPAY_SEGMENT_ID);
                    }
                    customerModel.setCustomerTypeId(CustomerTypeConstants.CUSTOMER_TYPE_MARKETED);
                    customerModel.setIsCnicSeen(false);

                    if (!isConsumerApp) {
                        customerModel.setIsCnicSeen(true);
                        customerModel.setWebServiceEnabled(true);
                    } else
                        customerModel.setWebServiceEnabled(true);

                    customerModel.setScreeningPerformed(Boolean.FALSE);
                    customerModel.setIsMPINGenerated(Boolean.FALSE);

                    if (isConventional) {
                        customerModel.setVerisysDone(false);
                    } else {
                        customerModel.setVerisysDone(true);
                    }

                    if (isConsumerApp) {
                        customerModel.setAccountMethodId(AccountOpeningMethodConstantsInterface.SELF_REGISTERATION);
                    } else {
                        if (FonePayConstants.APIGEE_CHANNEL.equals(webServiceVO.getChannelId())) {
                            customerModel.setAccountMethodId(AccountOpeningMethodConstantsInterface.APIGEE);
                        } else {
                            customerModel.setAccountMethodId(AccountOpeningMethodConstantsInterface.FONEPAY);
                            customerModel.setFonePayEnabled(true);
                        }
                    }
                    if (webServiceVO.getGender() != "" && null != webServiceVO.getGender()) {
                        if (webServiceVO.getGender().toUpperCase().equals("FEMALE") || webServiceVO.getGender().toUpperCase().equals("F")) {
                            customerModel.setGender("F");
                        } else if (webServiceVO.getGender().toUpperCase().equals("MALE") || webServiceVO.getGender().toUpperCase().equals("M")) {
                            customerModel.setGender("M");
                        } else if (webServiceVO.getGender().toUpperCase().startsWith("K") || webServiceVO.getGender().toUpperCase().equals("K")) {
                            customerModel.setGender("K");
                        }
                    }
                    if (newAccountFlag.equals("1")) {
                        if (iVo.getGender().toUpperCase().equals("FEMALE") || iVo.getGender().toUpperCase().equals("F")) {
                            customerModel.setGender("F");
                        } else if (iVo.getGender().toUpperCase().equals("MALE") || iVo.getGender().toUpperCase().equals("M")) {
                            customerModel.setGender("M");
                        } else if (iVo.getGender().toUpperCase().startsWith("K") || iVo.getGender().toUpperCase().equals("K")) {
                            customerModel.setGender("K");
                        }
                    }

                    customerModel.setTaxRegimeId(TaxRegimeConstants.FEDERAL);
                    TaxRegimeModel taxRegimeModel = new TaxRegimeModel();
                    taxRegimeModel = this.taxRegimeDAO.findByPrimaryKey(TaxRegimeConstants.FEDERAL);
                    if (taxRegimeModel != null) {
                        customerModel.setFed(taxRegimeModel.getFed());
                    }


                    baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MODEL, customerModel);

                    //***************************************************************************************
                    // * Populating the AppUserModel Model

                    AppUserModel appUserModel = new AppUserModel();

                    String[] nameArray = messageVO.getCustomerName().split(" ");
                    appUserModel.setFirstName(nameArray[0]);
                    if (nameArray.length > 1) {
                        appUserModel.setLastName(messageVO.getCustomerName().substring(
                                appUserModel.getFirstName().length() + 1));
                    }

                    else {
                        appUserModel.setLastName(nameArray[0]);

//                        appUserModel.setLastName(" ");
                    }
                    appUserModel.setAddress1(messageVO.getPresentAddress());
                    appUserModel.setAddress2(messageVO.getPermanentAddress());
                    if (newAccountFlag.equals("1")) {
                        appUserModel.setCustomerMobileNetwork(webServiceVO.getCustomerMobileNetwork());
                    } else {
                        appUserModel.setCustomerMobileNetwork(customerMobileNetwork);
                    }
                    appUserModel.setMobileNo(messageVO.getMobileNo());
                    String nicWithoutHyphins = messageVO.getCnic().replace("-", "");
                    appUserModel.setNic(nicWithoutHyphins);
                    appUserModel.setNicExpiryDate(dateFormat.parse(messageVO.getCnicExpiry()));
                    appUserModel.setMobileTypeId(1L);
                    appUserModel.setPasswordChangeRequired(true);
                    if (newAccountFlag.equals("1")) {
                        appUserModel.setDob(dateFormat.parse(iVo.getDateOfBirth()));
                    } else {
                        appUserModel.setDob(dateFormat.parse(messageVO.getDob()));
                    }
                    appUserModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
                    appUserModel.setCreatedOn(nowDate);
                    appUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
                    appUserModel.setMotherMaidenName(messageVO.getMotherName());
                    appUserModel.setEmail(webServiceVO.getEmailAddress());

                    //Below parameter are set to change cnic issuance Date format change  for api dd-mm-yyyy to yyyy-mm-dd
//                    if ((!webServiceVO.getCnicIssuanceDate().equals("") || webServiceVO.getCnicIssuanceDate() == null) && (webServiceVO.getReserved10().equals(DeviceTypeConstantsInterface.WEB_SERVICE.toString()))) {
                    appUserModel.setCnicIssuanceDate(dateFormat1.parse(webServiceVO.getCnicIssuanceDate()));
//                    } else if (!webServiceVO.getCnicIssuanceDate().equals("") || webServiceVO.getCnicIssuanceDate() == null) {
//                        appUserModel.setCnicIssuanceDate(dateFormat1.parse(webServiceVO.getCnicIssuanceDate()));
//                    }

                    //just for mock
//                    if(responseVO.getCaseStatus().equals("No Matches")){
//                        responseVO.setCaseStatus("True Match-Compliance");
//                    }


//                    if(responseVO.getCaseStatus().equals("PEP/EDD-Open|Private-Open") || responseVO.getCaseStatus().equals("GWL-Open|PEP/EDD-Open|Private-Open") ||
//                            responseVO.getCaseStatus().equals("PEP/EDD-Open") || responseVO.getCaseStatus().equals("GWL-Open") ||
//                            responseVO.getCaseStatus().equals("Private-Open") || responseVO.getCaseStatus().equals("Revert to Branch") ||
//                            responseVO.getCaseStatus().equals("True Match-Compliance"))
//                    {
//                        appUserModel.setVerified(false);
//                        appUserModel.setRegistrationStateId(RegistrationStateConstants.CLSPENDING);
//                        appUserModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_WARM);
//                        webServiceVO.setReserved9(String.valueOf(appUserModel.getRegistrationStateId()));
//                        webServiceVO.setReserved8("Account Opened in Pending State. Kindly activate your account");
//                    }
//                    else {
//                    if (isConventional) {
//                        appUserModel.setVerified(false);
//                        if (newAccountFlag.equals("1")) {
//                            appUserModel.setRegistrationStateId(RegistrationStateConstants.VERIFIED);
//                            appUserModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_COLD);
//                        } else {
//                            if (MessageUtil.getMessage("ChannelId").equals(webServiceVO.getChannelId())) {
//                                appUserModel.setRegistrationStateId(RegistrationStateConstants.VERIFIED);
//                                appUserModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_COLD);
//                            } else {
//                                appUserModel.setRegistrationStateId(RegistrationStateConstants.REQUEST_RECEIVED);
//                                appUserModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_WARM);
//                            }
//                        }
//                    } else {
                    appUserModel.setVerified(true);
                    if (accountType == CustomerAccountTypeConstants.BLINK) {
                        appUserModel.setRegistrationStateId(RegistrationStateConstants.BLINK_PENDING);
                    } else {
                        appUserModel.setRegistrationStateId(RegistrationStateConstants.REQUEST_RECEIVED);
                    }
                    appUserModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_WARM);
//                    }
//                    }

                    baseWrapper.putObject(CommandFieldConstants.KEY_CASE_STATUS, responseVO.getCaseStatus());
                    appUserModel.setCountryId(1L);
                    appUserModel.setAccountEnabled(true);
                    appUserModel.setAccountExpired(false);
                    appUserModel.setAccountLocked(false);
                    appUserModel.setCredentialsExpired(false);
                    appUserModel.setAccountClosedUnsettled(false);
                    appUserModel.setAccountClosedSettled(false);


                    appUserModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
                    appUserModel.setUpdatedOn(nowDate);

                    String mfsId = computeMfsId();
                    String username = mfsId;

                    String password = "1231";
                    String randomPin = "";
                    if (isConsumerApp || accountType == CustomerAccountTypeConstants.BLINK) {
                        randomPin = RandomUtils.generateRandom(4, false, true);
                        password = com.inov8.microbank.common.util.EncryptionUtil.encryptWithAES(XMLConstants.AES_ENCRYPTION_KEY, randomPin);//EncoderUtils.encodeToSha(randomPin);
                        if (null == password) {
                            password = "1231";
                        }
                        appUserModel.setUsername(username);
                        appUserModel.setPassword(password);
                    }

                    appUserModel.setUsername(username);
                    appUserModel.setPassword(password);
                    baseWrapper.putObject(CommandFieldConstants.KEY_APP_USER_MODEL, appUserModel);


                    if (messageVO.getPresentAddress() != null && !"".equals(messageVO.getPresentAddress())) {
                        AddressModel customerPresentAddressModel = new AddressModel();
                        customerPresentAddressModel.setHouseNo(messageVO.getPresentAddress());
                        customerPresentAddressModel.setFullAddress(messageVO.getPresentAddress());
                        baseWrapper.putObject(CommandFieldConstants.KEY_PRESENT_ADDR, customerPresentAddressModel);
                    }
                    if (messageVO.getPermanentAddress() != null && !"".equals(messageVO.getPermanentAddress())) {
                        AddressModel customerPermanentAddressModel = new AddressModel();
                        customerPermanentAddressModel.setHouseNo(messageVO.getPermanentAddress());
                        customerPermanentAddressModel.setFullAddress(messageVO.getPermanentAddress());
                        baseWrapper.putObject(CommandFieldConstants.KEY_PERMANENT_ADDR, customerPermanentAddressModel);
                    }

                    //***************************************************************************************
                    //** Populating the UserDeviceAccountsModel

                    userDeviceAccountsModel.setAccountEnabled(true);

                    userDeviceAccountsModel.setCommissioned(false);
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
                    userDeviceAccountsModel.setPin(password);
                    userDeviceAccountsModel.setProdCatalogId(PortalConstants.CUSTOMER_DEFAULT_CATALOG);

                    baseWrapper.putObject(CommandFieldConstants.KEY_USER_DEVICE_ACCOUNT_MODEL, userDeviceAccountsModel);

                    Long bankId = getCommonCommandManager().getOlaBankMadal().getBankId();

                    SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
                    smartMoneyAccountModel.setBankId(bankId);
                    smartMoneyAccountModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
                    smartMoneyAccountModel.setCreatedOn(new Date());
                    smartMoneyAccountModel.setUpdatedOn(new Date());
                    smartMoneyAccountModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
                    smartMoneyAccountModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
                    smartMoneyAccountModel.setActive(true);
                    smartMoneyAccountModel.setStatusId(OlaStatusConstants.ACCOUNT_STATUS_ACTIVE);
                    smartMoneyAccountModel.setChangePinRequired(false);
                    smartMoneyAccountModel.setDefAccount(true);
                    smartMoneyAccountModel.setDeleted(false);
                    smartMoneyAccountModel.setName("i8_bb_" + mfsId);
                    smartMoneyAccountModel.setAccountClosedUnsetteled(0L);
                    smartMoneyAccountModel.setAccountClosedSetteled(0L);

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
                    if (newAccountFlag.equals("1")) {
                        olaVo.setDob(dateFormat.parse(iVo.getDateOfBirth()));
                    } else {
                        olaVo.setDob(dateFormat.parse(messageVO.getDob()));

                    }
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

                    if (isConventional) {
                        baseWrapper.putObject("isBvsAccount", Boolean.FALSE); // PAYSYS Not Performed
                    } else {
                        baseWrapper.putObject("isBvsAccount", Boolean.TRUE); // PAYSYS Already Performed
                    }
                    if (!isConventional) {
                        baseWrapper.putObject(CommandFieldConstants.KEY_VARISYS_DATA_MODEL, verisysDataModel);
                    }

                    if (isConsumerApp) {
                        baseWrapper.putObject(CommandFieldConstants.KEY_APP_ID, "2");
                    }

                    baseWrapper = getCommonCommandManager().saveOrUpdateAccountOpeningL0Request(baseWrapper);

                    accountCreated = true;

//                    if(isConsumerApp) {
//                        if (webServiceVO.getChannelId().equals(FonePayConstants.NOVA_CHANNEL)) {
//                            String customerSMS = this.getMessageSource().getMessage("smsCommand.act_sms_jsbl_nova", null, null);
//                            baseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, new SmsMessage(webServiceVO.getMobileNo(), customerSMS));
//                            getCommonCommandManager().sendSMSToUser(baseWrapper);
//                        } else {
//                            sendSMSToUsers(webServiceVO.getMobileNo(), null, isConsumerApp);
//                        }
//                    }
//
//                    sendSMSToUsers(webServiceVO.getMobileNo(), randomPin, isConsumerApp);

//                    if(isConsumerApp && !appUserModel.getRegistrationStateId().equals(RegistrationStateConstants.CLSPENDING)){
//                        sendSMSToUsers(webServiceVO.getMobileNo(), randomPin, isConsumerApp, appUserModel.getRegistrationStateId());
//                    }
//                    else{
                    if (webServiceVO.getChannelId() != null) {
                        if (webServiceVO.getChannelId().equals(FonePayConstants.NOVA_CHANNEL)) {
                            String customerSMS = this.getMessageSource().getMessage("smsCommand.act_sms_jsbl_nova", null, null);
                            baseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, new SmsMessage(webServiceVO.getMobileNo(), customerSMS));
                            getCommonCommandManager().sendSMSToUser(baseWrapper);
                        }
                    } else {
                        sendSMSToUsers(webServiceVO.getMobileNo(), randomPin, isConsumerApp, appUserModel.getRegistrationStateId());
                    }
//                    }

//					IvrRequestDTO ivrDTO = new IvrRequestDTO();
//					ivrDTO.setCustomerMobileNo(appUserModel.getMobileNo());
//					ivrDTO.setRetryCount(0);
//					ivrDTO.setProductId(new Long(CommandFieldConstants.CREATE_PIN_IVR));
//					try {
//						getCommonCommandManager().initiateUserGeneratedPinIvrCall(ivrDTO);
//					}catch(Exception e){
//						if(isConsumerApp){
//							logger.error("[createCustomer] Error occurred while routing IVR call for PIN generation. Message:"+e.getMessage(), e);
//						}else{
//							logger.error("[createCustomer] Error occurred while routing IVR call for PIN generation. Message:"+e.getMessage(), e);
//						}
//
//						throw e;
//					}
//                    if(isConsumerApp || !webServiceVO.getChannelId().equals(FonePayConstants.NOVA_CHANNEL)) {
//                        MiddlewareAdviceVO middlewareAdviceVO = new MiddlewareAdviceVO();
//                        middlewareAdviceVO.setStan(String.valueOf((100000 + new Random().nextInt(900000))));
//                        middlewareAdviceVO.setRequestTime(new Date());
//                        middlewareAdviceVO.setDateTimeLocalTransaction(new Date());
//                        middlewareAdviceVO.setTransmissionTime(new Date());
//                        middlewareAdviceVO.setAdviceType(CoreAdviceUtil.ACCOUNNT_OPENING_ADVICE);
//                        middlewareAdviceVO.setProductId(ProductConstantsInterface.CUST_ACCOUNT_OPENING);
//                        middlewareAdviceVO.setConsumerNo(appUserModel.getMobileNo());
//                        middlewareAdviceVO.setCnicNo(appUserModel.getNic());
//
//                        transactionReversalManager.sendCoreReversalRequiresNewTransaction(middlewareAdviceVO);
//
////                        SwitchWrapper switchWrapper = new SwitchWrapperImpl();
////                        switchWrapper.setBaseWrapper(baseWrapper);
//
//                        WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
//
//                        workFlowWrapper.putObject(CommandFieldConstants.KEY_APP_USER_MODEL, appUserModel);
//                        workFlowWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MODEL, customerModel);
//                        workFlowWrapper.putObject(CommandFieldConstants.KEY_SMART_MONEY_ACCOUNT_MODEL, smartMoneyAccountModel);
//                        workFlowWrapper.setProductId(ProductConstantsInterface.CUST_ACCOUNT_OPENING);
//
//                        loadAndForwardAccountToQueue(workFlowWrapper);
//
//
//                    }
                } catch (Exception ex) {
                    String errorMessage = ex.getMessage();
                    if (accountCreated) {
                        if (isConsumerApp) {
                            logger.error("Customer Creation via Self Registeration - Successful , but exception occurred... Customer Mobile No:" + messageVO.getMobileNo() + " errorMessage:" + errorMessage + "\n Exception: ", ex);
                        } else {
                            logger.error("Customer Creation via FonePay - Successful , but exception occurred... Customer Mobile No:" + messageVO.getMobileNo() + " errorMessage:" + errorMessage + "\n Exception: ", ex);
                        }

                    } else {
                        if (isConsumerApp) {
                            logger.error("Customer Creation via Self Registeration - Failed ... Customer Mobile No:" + messageVO.getMobileNo() + " errorMessage:" + errorMessage + "\n Exception: ", ex);
                        } else {
                            logger.error("Customer Creation via FonePay - Failed ... Customer Mobile No:" + messageVO.getMobileNo() + " errorMessage:" + errorMessage + "\n Exception: ", ex);
                        }

                    }

                    if (errorMessage == null ||
                            (!StringUtil.isNullOrEmpty(errorMessage) && (errorMessage.contains("Hibernate")
                                    || errorMessage.contains("Exception")
                                    || errorMessage.contains("SQLException")))) {

                        webServiceVO.setResponseCode(FonePayResponseCodes.ACCOUNT_OPENING_FAILED);
                        webServiceVO.setResponseCodeDescription(FonePayUtils.getResponceCodeDescription(FonePayResponseCodes.ACCOUNT_OPENING_FAILED));
                    } else {
                        webServiceVO.setResponseCode(FonePayResponseCodes.ACCOUNT_OPENING_FAILED);
                        webServiceVO.setResponseCodeDescription(errorMessage);
                    }
                }

                if (accountCreated) {
                    webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                    webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
                } else {
                    throw new FrameworkCheckedException(FonePayUtils.getResponceCodeDescription(FonePayResponseCodes.ACCOUNT_OPENING_FAILED));
                }
            }
        } catch (CommandException ce) {
            throw new CommandException(ce.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);
        }

/*				if(isConsumerApp){
					logger.info("[FonePayManagerImpl.createCustomerBySelfRegisteration] Returning Response Code: "+webServiceVO.getResponseCode() + ", RespCodeDesc:" + webServiceVO.getResponseCodeDescription());


				}else{
					logger.info("[FonePayManagerImpl.createCustomerViaFonePay] Returning Response Code: "+webServiceVO.getResponseCode() + ", RespCodeDesc:" + webServiceVO.getResponseCodeDescription());

				}*/ catch (Exception ex) {
            String errorMessage = ex.getMessage();

            if (accountUpdated) {
                if (isConsumerApp) {
                    logger.error("Customer Updation via Self Registeration - Successful , but exception occurred... Customer Mobile No:" + messageVO.getMobileNo() + " errorMessage:" + errorMessage + "\n Exception: ", ex);
                } else {
                    logger.error("Customer Updation via FonePay - Successful , but exception occurred... Customer Mobile No:" + messageVO.getMobileNo() + " errorMessage:" + errorMessage + "\n Exception: ", ex);
                }

            } else {
                if (isConsumerApp) {
                    logger.error("Customer Updation via Self Registeration - Failed ... Customer Mobile No:" + messageVO.getMobileNo() + " errorMessage:" + errorMessage + "\n Exception: ", ex);
                } else {
                    logger.error("Customer Updation via FonePay - Failed ... Customer Mobile No:" + messageVO.getMobileNo() + " errorMessage:" + errorMessage + "\n Exception: ", ex);
                }

            }

            if (errorMessage == null ||
                    (!StringUtil.isNullOrEmpty(errorMessage) && (errorMessage.contains("Hibernate")
                            || errorMessage.contains("Exception")
                            || errorMessage.contains("SQLException")))) {

                webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
                webServiceVO.setResponseCodeDescription(FonePayUtils.getResponceCodeDescription(FonePayResponseCodes.ACCOUNT_OPENING_FAILED));
            } else {
                webServiceVO.setResponseCode(FonePayResponseCodes.ACCOUNT_OPENING_FAILED);
                webServiceVO.setResponseCodeDescription(errorMessage);
            }
        }

        if (accountCreated) {

        } else {
            if (accountUpdated) {
                webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
            } else {
                throw new FrameworkCheckedException(FonePayUtils.getResponceCodeDescription(FonePayResponseCodes.ACCOUNT_OPENING_FAILED));
            }
        }


        return webServiceVO;
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
            nextLongValue = getCommonCommandManager().getSequenceGenerator().nextLongValue();
            appUserModel = new AppUserModel();
            goldenNosModel = new GoldenNosModel();
            appUserModel.setUsername(String.valueOf(nextLongValue));
            goldenNosModel.setGoldenNumber(String.valueOf(nextLongValue));
            int countAppUser = getCommonCommandManager().countByExample(appUserModel);
            int countGoldenNos = getCommonCommandManager().countByExample(goldenNosModel);
            if (countAppUser == 0 && countGoldenNos == 0) {
                flag = false;
            }
        }

        return String.valueOf(nextLongValue);
    }

    private void addEmptyPicture(Long pictureTypeId, ArrayList<CustomerPictureModel> arrayCustomerPictures, Date nowDate) throws IOException, CommandException {
        CustomerPictureModel customerPictureModel = new CustomerPictureModel();
        try {
            File imageFile = getCommonCommandManager().loadImage("images/no_photo_icon.png");
            Path path = imageFile.toPath();
            imageFile.getTotalSpace();
            imageFile.getCanonicalPath();
            imageFile.length();
            String ext = FilenameUtils.getExtension(imageFile.getName());
            byte[] imageFileBytes = Files.readAllBytes(path);


            customerPictureModel.setPicture(imageFileBytes);
            customerPictureModel.setPictureTypeId(pictureTypeId);
            customerPictureModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_1);
            customerPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
            customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
            customerPictureModel.setCreatedOn(nowDate);
            customerPictureModel.setUpdatedOn(nowDate);
            //customerPictureModel.setPictureExtension(ext);
        } catch (IOException e) {
            throw new CommandException(MessageUtil.getMessage("newMfsAccount.unknown"), ErrorCodes.FILE_NOT_FOUND, ErrorLevel.MEDIUM);
        }

        if (arrayCustomerPictures != null) {
            arrayCustomerPictures.add(customerPictureModel);
        }
    }

    private void addCustomerPicture(Long pictureTypeId, ArrayList<CustomerPictureModel> arrayCustomerPictures, Date nowDate, String pictureAsString) throws IOException, CommandException {
        CustomerPictureModel customerPictureModel = new CustomerPictureModel();
        try {
            pictureAsString = pictureAsString.replace(" ", "+");
            byte[] imageFileBytes = org.apache.commons.codec.binary.Base64.decodeBase64(pictureAsString.getBytes());

            customerPictureModel.setPicture(imageFileBytes);
            customerPictureModel.setPictureTypeId(pictureTypeId);
            customerPictureModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_1);
            customerPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
            customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
            customerPictureModel.setCreatedOn(nowDate);
            customerPictureModel.setUpdatedOn(nowDate);
        } catch (Exception e) {
            logger.error("Unable to decode image bytes...", e);
            throw new CommandException(MessageUtil.getMessage("newMfsAccount.unknown"), ErrorCodes.FILE_NOT_FOUND, ErrorLevel.MEDIUM);
        }

        if (arrayCustomerPictures != null) {
            arrayCustomerPictures.add(customerPictureModel);
        }
    }

/*	public void setCommonCommandManager(CommonCommandManager commonCommandManager) {
		this.commonCommandManager = commonCommandManager;
	}*/

    @Override
    public WebServiceVO makeExistingCustomerVerification(WebServiceVO webServiceVO) {
        String mobileNumber = webServiceVO.getMobileNo();
        String cnic = webServiceVO.getCnicNo();
        String otpReqType = webServiceVO.getReserved2();
        String isValidationRequired = webServiceVO.getReserved1();

        try {
            AppUserModel customerAppUserModel = getCommonCommandManager().loadAppUserByMobileAndType(mobileNumber, UserTypeConstantsInterface.CUSTOMER);
            if (customerAppUserModel == null) {
                AppUserModel aUserModel = null;
                aUserModel = this.isCNICUnique(webServiceVO.getCnicNo());
                if (aUserModel != null) {
                    webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUSTOMER_CNIC_ALREADY_EXIST_AS_CUSTOMER);
                } else {
                    webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUSTOMER_NOT_FOUND);
                }
                return webServiceVO;
            }
//            else if (customerAppUserModel != null) {
//                AppUserModel aUserModel = null;
//                aUserModel = this.isCNICUnique(webServiceVO.getCnicNo());
//                if (aUserModel != null) {
//                    webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUSTOMER_CNIC_ALREADY_EXIST_AS_CUSTOMER);
//                } else {
//                    webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUSTOMER_MOBILE_ALREADY_EXIST_AS_CUSTOMER);
//
//                }
//                return webServiceVO;
//            }

            AppUserModel customerAppUserModelbyCnic = getCommonCommandManager().loadAppUserByCnicAndType(webServiceVO.getCnicNo());
            if (customerAppUserModelbyCnic == null) {
                AppUserModel aUserModel = null;
                aUserModel = this.isMobileNumUnique(webServiceVO.getMobileNo());
                if (aUserModel != null) {
                    webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUSTOMER_MOBILE_ALREADY_EXIST_AS_CUSTOMER);
                }
                return webServiceVO;
            }
//            else if (customerAppUserModelbyCnic != null) {
//                AppUserModel aUserModel = null;
//                aUserModel = this.isMobileNumUnique(webServiceVO.getMobileNo());
//                if (aUserModel != null) {
//                    webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUSTOMER_MOBILE_ALREADY_EXIST_AS_CUSTOMER);
//                } else {
//                    webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUSTOMER_CNIC_ALREADY_EXIST_AS_CUSTOMER);
//
//                }
//                return webServiceVO;
//
//            }
            SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
            SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
            smartMoneyAccountModel.setCustomerId(customerAppUserModel.getCustomerId());
            searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);

            searchBaseWrapper = getCommonCommandManager().loadSmartMoneyAccount(searchBaseWrapper);
            smartMoneyAccountModel = (SmartMoneyAccountModel) searchBaseWrapper.getCustomList().getResultsetList().get(0);


            FonePayMessageVO fonePayMessageVO = new FonePayMessageVO();
            fonePayMessageVO.setMobileNo(mobileNumber);
            fonePayMessageVO.setCnic(cnic);
            fonePayMessageVO.setValidateMobileCnic(true);
            fonePayMessageVO = this.makevalidateExistingCustomer(fonePayMessageVO);
            if (!fonePayMessageVO.getResponseCode().equals("00")) {
//                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUSTOMER_NOT_FOUND);
                webServiceVO.setResponseCode(fonePayMessageVO.getResponseCode());
                webServiceVO.setResponseCodeDescription(fonePayMessageVO.getResponseCodeDescription());
                return webServiceVO;
            }
            OlaCustomerAccountTypeModel ola = new OlaCustomerAccountTypeModel();
            ola = getCommonCommandManager().getOlaCustomerAccountTypeDao().findByPrimaryKey(Long.valueOf(fonePayMessageVO.getAccountType()));
            webServiceVO.setResponseCode(fonePayMessageVO.getResponseCode());
            webServiceVO.setAccountTitle(fonePayMessageVO.getAccountTitle());
            webServiceVO.setResponseCodeDescription(fonePayMessageVO.getResponseCodeDescription());
            webServiceVO.setAccountStatus(fonePayMessageVO.getAccountStatus());
            if (smartMoneyAccountModel.getChangePinRequired() == true) {
                webServiceVO.setOtpPin("1");
            } else
                webServiceVO.setOtpPin("0");

//			webServiceVO.setOtpPin(String.valueOf(smartMoneyAccountModel.getChangePinRequired()));

            if (FonePayResponseCodes.SUCCESS_RESPONSE_CODE.equals(fonePayMessageVO.getResponseCode())) {
                webServiceVO.setCustomerStatus(fonePayMessageVO.getCustomerStatus());
                webServiceVO.setCnicExpiry(fonePayMessageVO.getCnicExpiry());
                webServiceVO.setDateOfBirth(fonePayMessageVO.getDob());
                webServiceVO.setFirstName(fonePayMessageVO.getFirstName());
                webServiceVO.setLastName(fonePayMessageVO.getLastName());
                if (ola.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_0)) {
                    webServiceVO.setAccountType("L0");
                } else
                    webServiceVO.setAccountType("L1");


//                webServiceVO.setAccountType(ola.getName());
                ///Generate OTP and store in MiniTransaction
                logger.info("[verifyExistingCustomer] Customer verified( resp_code = 00 ) against mobile:" + mobileNumber + " , cnic:" + cnic);

                if (isValidationRequired != null && isValidationRequired.equals("02")) {

                    if (otpReqType != null && otpReqType.equals("01")) {
                        String otp = CommonUtils.generateOneTimePin(5);
                        String encryptedPin = EncoderUtils.encodeToSha(otp);
                        this.createMiniTransactionModel(encryptedPin, mobileNumber, webServiceVO.getChannelId(), CommandFieldConstants.KEY_CMD_ACC_LINK);
                        String smsText = MessageUtil.getMessage("otpSms", new String[]{"link account", otp});
                        SmsMessage smsMessage = new SmsMessage(mobileNumber, smsText);
                        smsSender.send(smsMessage);
                        logger.info("link account" + smsText);
                    } else if (otpReqType != null && otpReqType.equals("02")) {
                        String otp = CommonUtils.generateOneTimePin(5);
                        String encryptedPin = EncoderUtils.encodeToSha(otp);
                        this.createMiniTransactionModel(encryptedPin, mobileNumber, webServiceVO.getChannelId(), CommandFieldConstants.KEY_CMD_ACC_DELINK);

                        String smsText = MessageUtil.getMessage("otpSms", new String[]{"De-link account", otp});
                        SmsMessage smsMessage = new SmsMessage(mobileNumber, smsText);
                        smsSender.send(smsMessage);
                        logger.info("De-link account" + smsText);
                    } else {
                        String otp = CommonUtils.generateOneTimePin(5);
                        String encryptedPin = EncoderUtils.encodeToSha(otp);
                        this.createMiniTransactionModel(encryptedPin, mobileNumber, webServiceVO.getChannelId(), CommandFieldConstants.CMD_OTP_VERIFICATION);

                        String smsText = MessageUtil.getMessage("otpSms", new String[]{"link account", otp});
                        SmsMessage smsMessage = new SmsMessage(mobileNumber, smsText);
                        smsSender.send(smsMessage);
                        logger.info("Link account" + smsText);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("[FonePayManagerImpl.makeExistingCustomerVerification] Exception occured: " + e.getMessage(), e);
            webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setAccountTitle(null);
        }

        logger.info("[FonePayManagerImpl.makeExistingCustomerVerification] Sending Response Code: " + webServiceVO.getResponseCode());

        return webServiceVO;

    }

    public WebServiceVO verifyNewCustomer(WebServiceVO webServiceVO) {
        String mobileNumber = webServiceVO.getMobileNo();
        String cnic = webServiceVO.getCnicNo();
        String isValidationRequired = webServiceVO.getReserved1();
        AppUserModel appUserModel = new AppUserModel();
        Boolean isValid = Boolean.FALSE;

        if (isValidationRequired != null) {
            isValid = Boolean.TRUE;
        }
        try {

            AppUserModel testModel1 = new AppUserModel();
            List<AppUserModel> modelList = new ArrayList<>();
            AppUserModel testModel = null;
            testModel1.setMobileNo(mobileNumber);
            testModel1.setNic(cnic);
            testModel1.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
            testModel1.setAccountClosedSettled(false);
            modelList = appUserHibernateDAO.findByExample(testModel1).getResultsetList();
            if (modelList != null && modelList.size() > 0) {
                testModel = modelList.get(0);
            }
            if (testModel != null && RegistrationStateConstants.DISCREPANT.equals(testModel.getRegistrationStateId())) {

                List<CustomerPictureModel> customerPictureModelList = new ArrayList<>();
                webServiceVO.setFirstName(testModel.getFirstName());
                webServiceVO.setLastName(testModel.getLastName());
                if (testModel.getNicExpiryDate() != null) {
                    webServiceVO.setCnicExpiry(dateFormat.format(testModel.getNicExpiryDate()));
                }
                webServiceVO.setDateOfBirth(dateFormat.format(testModel.getDob()));
                if (testModel.getCustomerIdCustomerModel().getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_0)) {
                    webServiceVO.setAccountType(FonePayConstants.L0_CUSTOMER);
                } else if (testModel.getCustomerIdCustomerModel().getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_1)) {
                    webServiceVO.setAccountType(FonePayConstants.L1_CUSTOMER);
                }
                webServiceVO.setAccountType("0" + testModel.getCustomerIdCustomerModel().getCustomerAccountTypeId().toString());
                CustomerPictureModel customerPictureModel = new CustomerPictureModel();
                customerPictureModel.setCustomerId(testModel.getCustomerId());
                customerPictureModel.setDiscrepant(true);
                customerPictureModelList = this.customerPictureDAO.findByExample(customerPictureModel).getResultsetList();
                if (customerPictureModelList != null && customerPictureModelList.size() > 0) {
                    if (customerPictureModelList.size() >= 2) {
                        webServiceVO.setCustomerStatus("4");
                    } else {
                        if (PictureTypeConstants.ID_FRONT_SNAPSHOT.equals(customerPictureModelList.get(0).getPictureTypeId())) {
                            webServiceVO.setCustomerStatus("3");
                        } else {
                            webServiceVO.setCustomerStatus("2");
                        }
                    }
                }
            } else {
                //Is MOBILE No Unique - Validation
                AppUserModel apUserModel = this.isMobileNumUnique(mobileNumber);

                if (null != apUserModel) {
                    if (apUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER) {
                        return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUSTOMER_MOBILE_ALREADY_EXIST_AS_CUSTOMER);
                    } else if (apUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER) {
                        return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUSTOMER_MOBILE_ALREADY_EXIST_AS_RETAILER);
                    }
                    if (apUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.HANDLER) {
                        return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUSTOMER_MOBILE_ALREADY_EXIST_AS_HANDLER);
                    }
                }


                //Is CNIC Unique - Validation
                AppUserModel aUserModel = null;
                aUserModel = this.isCNICUnique(cnic);

                if (null != aUserModel) {
                    if (aUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER) {
                        return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUSTOMER_CNIC_ALREADY_EXIST_AS_CUSTOMER);
                    } else if (aUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER) {
                        return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUSTOMER_CNIC_ALREADY_EXIST_AS_RETAILER);
                    }
                    if (aUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.HANDLER) {
                        return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUSTOMER_CNIC_ALREADY_EXIST_AS_HANDLER);
                    }
                }
            }


            //Is CNIC Blacklisted - Validation
            if (getCommonCommandManager().isCnicBlacklisted(cnic)) {
                return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUSTOMER_CNIC_BLACKLISTED);
            }

            if (isValid) {
                if (isValidationRequired.equals("02")) {
                    ///Generate OTP and store in MiniTransaction
                    String otp = CommonUtils.generateOneTimePin(5);
                    logger.info("The plain otp is " + otp);
                    String encryptedPin = EncoderUtils.encodeToSha(otp);
                    this.createMiniTransactionModel(encryptedPin, mobileNumber, webServiceVO.getChannelId(), CommandFieldConstants.CMD_OTP_VERIFICATION);

                    //Send OTP via SMS to Customer
                    String smsText = MessageUtil.getMessage("otpSmsAccOpening", new String[]{"JS Mobile account opening", "account opening", otp});
                    SmsMessage smsMessage = new SmsMessage(mobileNumber, smsText);
                    smsSender.send(smsMessage);
                }
            } else {
                ///Generate OTP and store in MiniTransaction
                String otp = CommonUtils.generateOneTimePin(5);
                logger.info("The plain otp is " + otp);
                String encryptedPin = EncoderUtils.encodeToSha(otp);
                this.createMiniTransactionModel(encryptedPin, mobileNumber, webServiceVO.getChannelId(), CommandFieldConstants.CMD_OTP_VERIFICATION);

                //Send OTP via SMS to Customer
                String smsText = MessageUtil.getMessage("otpSmsAccOpening", new String[]{"JS Mobile account opening", "account opening", otp});
                SmsMessage smsMessage = new SmsMessage(mobileNumber, smsText);
                smsSender.send(smsMessage);
            }
            //Prepare SUCCESS MESSAGE
            webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
            webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);

        } catch (Exception e) {
            logger.error("[FonePayManagerImpl.verifyNewCustomer] Exception occured: " + e.getMessage(), e);
            webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR);
        }

        logger.info("[FonePayManagerImpl.verifyNewCustomer] Sending Response Code: " + webServiceVO.getResponseCode());

        return webServiceVO;

    }


    @Override
    public WebServiceVO verifyLoginCustomer(WebServiceVO webServiceVO) {
        String mobileNumber = webServiceVO.getMobileNo();

        CustomerModel customerModel = new CustomerModel();
        AppUserModel appUserModel = new AppUserModel();

        SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();

        try {

            AppUserModel testModel1 = new AppUserModel();
            List<AppUserModel> modelList = new ArrayList<>();
            AppUserModel testModel = null;
            testModel1.setMobileNo(mobileNumber);
            testModel1.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
            testModel1.setAccountClosedSettled(false);
            modelList = appUserHibernateDAO.findByExample(testModel1).getResultsetList();
            if (modelList != null && modelList.size() > 0) {
                testModel = modelList.get(0);

                SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
                smartMoneyAccountModel.setCustomerId(testModel.getCustomerId());
                searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);

                searchBaseWrapper = getCommonCommandManager().loadSmartMoneyAccount(searchBaseWrapper);
                smartMoneyAccountModel = (SmartMoneyAccountModel) searchBaseWrapper.getCustomList().getResultsetList().get(0);

                if (smartMoneyAccountModel == null) {
                    return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.SMA_NOT_LOADED);
                } else if (!smartMoneyAccountModel.getActive()) {
                    return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.SMA_NOT_ACTIVE);
                }

                customerModel = getCommonCommandManager().getCustomerModelById(testModel.getCustomerId());

//            if (testModel != null && RegistrationStateConstants.DISCREPANT.equals(testModel.getRegistrationStateId())) {
//
//                List<CustomerPictureModel> customerPictureModelList = new ArrayList<>();
//                webServiceVO.setFirstName(testModel.getFirstName());
//                webServiceVO.setLastName(testModel.getLastName());
//                if (testModel.getNicExpiryDate() != null) {
//                    webServiceVO.setCnicExpiry(dateFormat.format(testModel.getNicExpiryDate()));
//                }
//                webServiceVO.setDateOfBirth(dateFormat.format(testModel.getDob()));
//                if (testModel.getCustomerIdCustomerModel().getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_0)) {
//                    webServiceVO.setAccountType(FonePayConstants.L0_CUSTOMER);
//                } else if (testModel.getCustomerIdCustomerModel().getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_1)) {
//                    webServiceVO.setAccountType(FonePayConstants.L1_CUSTOMER);
//                }
//                webServiceVO.setAccountType("0" + testModel.getCustomerIdCustomerModel().getCustomerAccountTypeId().toString());
//                CustomerPictureModel customerPictureModel = new CustomerPictureModel();
//                customerPictureModel.setCustomerId(testModel.getCustomerId());
//                customerPictureModel.setDiscrepant(true);
//                customerPictureModelList = this.customerPictureDAO.findByExample(customerPictureModel).getResultsetList();
//                if (customerPictureModelList != null && customerPictureModelList.size() > 0) {
//                    if (customerPictureModelList.size() >= 2) {
//                        webServiceVO.setCustomerStatus("4");
//                    } else {
//                        if (PictureTypeConstants.ID_FRONT_SNAPSHOT.equals(customerPictureModelList.get(0).getPictureTypeId())) {
//                            webServiceVO.setCustomerStatus("3");
//                        } else {
//                            webServiceVO.setCustomerStatus("2");
//                        }
//                    }
//                }
//            }
//            else {
//                //Is MOBILE No Unique - Validation
//                AppUserModel apUserModel = this.isMobileNumUnique(mobileNumber);
//
//                if (null != apUserModel) {
//                    if (apUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER) {
//                        return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUSTOMER_MOBILE_ALREADY_EXIST_AS_CUSTOMER);
//                    } else if (apUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER) {
//                        return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUSTOMER_MOBILE_ALREADY_EXIST_AS_RETAILER);
//                    }
//                    if (apUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.HANDLER) {
//                        return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUSTOMER_MOBILE_ALREADY_EXIST_AS_HANDLER);
//                    }
//                }
//
//
//                //Is CNIC Unique - Validation
//                AppUserModel aUserModel = null;
//                aUserModel = this.isCNICUnique(testModel.getNic());
//
//                if (null != aUserModel) {
//                    if (aUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER) {
//                        return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUSTOMER_CNIC_ALREADY_EXIST_AS_CUSTOMER);
//                    } else if (aUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER) {
//                        return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUSTOMER_CNIC_ALREADY_EXIST_AS_RETAILER);
//                    }
//                    if (aUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.HANDLER) {
//                        return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUSTOMER_CNIC_ALREADY_EXIST_AS_HANDLER);
//                    }
//                }
//            }


                //Is CNIC Blacklisted - Validation
//            if (getCommonCommandManager().isCnicBlacklisted(testModel.getNic())) {
//                return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUSTOMER_CNIC_BLACKLISTED);
//            }

                //Prepare SUCCESS MESSAGE
                if (smartMoneyAccountModel.getChangePinRequired().equals(false)) {
                    webServiceVO.setOtpPin("true");
                } else {
                    webServiceVO.setOtpPin("false");


                }
                webServiceVO.setAccountStatus(String.valueOf(testModel.getAccountStateId()));
                webServiceVO.setAccountTitle(testModel.getFirstName() + " " + testModel.getLastName());
                if (customerModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_0)) {
                    webServiceVO.setAccountType("L0");
                } else if (customerModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_1)) {
                    webServiceVO.setAccountType("L1");
                } else if (customerModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.BLINK)) {
                    webServiceVO.setAccountType("blink");
                } else if (customerModel.getCustomerAccountTypeId().equals(56L)) {
                    webServiceVO.setAccountType("Ultra-Signature");

                }

                webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
            }else {
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUSTOMER_NOT_FOUND);

            }
        } catch (Exception e) {
            logger.error("[FonePayManagerImpl.verifyNewCustomer] Exception occured: " + e.getMessage(), e);
            webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR);
        }

        logger.info("[FonePayManagerImpl.verifyNewCustomer] Sending Response Code: " + webServiceVO.getResponseCode());

        return webServiceVO;
    }

    @Override
    public WebServiceVO makeVirtualCardTagging(WebServiceVO integrationMessageVO) {
        try {
            FonePayMessageVO fonePayMessageVO = new FonePayMessageVO();
            fonePayMessageVO.setMobileNo(integrationMessageVO.getMobileNo());
            fonePayMessageVO.setCnic(integrationMessageVO.getCnicNo());
            fonePayMessageVO.setValidateMobileCnic(true);
            fonePayMessageVO.setCardNo(integrationMessageVO.getCardNo());
            fonePayMessageVO.setValidateWebServiceEnabled(true);
            fonePayMessageVO.setValidateFonePayEnabled(true);


            fonePayMessageVO = this.validateCardInfo(fonePayMessageVO);
            if (FonePayResponseCodes.SUCCESS_RESPONSE_CODE.equals(fonePayMessageVO.getResponseCode())) {
                fonePayMessageVO = this.makevalidateExistingCustomer(fonePayMessageVO);
                //integrationMessageVO=this.validateFonePayCustomer(integrationMessageVO);
                if (FonePayResponseCodes.SUCCESS_RESPONSE_CODE.equals(fonePayMessageVO.getResponseCode())) {
                    String cardNo = integrationMessageVO.getCardNo();
                    String cardExpiry = integrationMessageVO.getCardExpiry();
                    String firstName = integrationMessageVO.getFirstName();
                    String lastName = integrationMessageVO.getLastName();
                    String mobileNo = integrationMessageVO.getMobileNo();
                    String cnic = integrationMessageVO.getCnicNo();
                    VirtualCardModel virtualCardModel = new VirtualCardModel();
                    virtualCardModel.setCardExpiry(cardExpiry);
                    virtualCardModel.setCardNo(cardNo);
                    virtualCardModel.setCnicNo(cnic);
                    virtualCardModel.setCreatedBy(2L);
                    virtualCardModel.setCreatedOn(new Date());
                    virtualCardModel.setUpdatedOn(new Date());
                    virtualCardModel.setFirstName(firstName);
                    virtualCardModel.setLastName(lastName);
                    virtualCardModel.setMobileNo(mobileNo);
                    virtualCardModel.setUpdatedBy(2L);
                    virtualCardModel.setIsBlocked(false);
                    virtualCardModel.setAppUserId(fonePayMessageVO.getAppUserId());
                    virtualCardModel.setCustomerId(fonePayMessageVO.getCustomerId());
                    virtualCardModel.setDeleted(false);
                    this.virtualCardHibernateDAO.saveOrUpdate(virtualCardModel);
                    integrationMessageVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
                    integrationMessageVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
//					integrationMessageVO = FonePayUtils.prepareErrorResponse(integrationMessageVO, FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                } else {
//					integrationMessageVO.setResponseCode(fonePayMessageVO.getResponseCode());
                    integrationMessageVO = FonePayUtils.prepareErrorResponse(integrationMessageVO, fonePayMessageVO.getResponseCode());
                }
            }
            return integrationMessageVO;


        } catch (Exception e) {
            logger.error("[FonePayManagerImpl.makeVirtualCardTagging] Exception occured: " + e.getMessage(), e);
            integrationMessageVO = FonePayUtils.prepareErrorResponse(integrationMessageVO, FonePayResponseCodes.GENERAL_ERROR);
            integrationMessageVO.setAccountTitle(null);
        }
        return integrationMessageVO;
    }

    @Override
    public CustomList<VirtualCardReportModel> searchCards(SearchBaseWrapper wrapper) {
        VirtualCardReportModel model = (VirtualCardReportModel) wrapper.getBasePersistableModel();
        return virtualCardViewHibernateDAO.findByExample(model, wrapper.getPagingHelperModel(), wrapper.getSortingOrderMap(), wrapper.getDateRangeHolderModel());
    }

    @Override
    public VirtualCardModel searchCardById(VirtualCardModel virtualCardModel) {
        virtualCardModel = virtualCardHibernateDAO.findByPrimaryKey(virtualCardModel.getVirtualCardId());
        return virtualCardModel;
    }

    @Override
    public void updateVirtualCardStatusWithAuthorization(BaseWrapper baseWrapper) {
        VirtualCardEnableDisableVO virtualCardEnableDisableVO = (VirtualCardEnableDisableVO) baseWrapper.getBasePersistableModel();
        VirtualCardModel virtualCardModel = new VirtualCardModel();
        virtualCardModel.setCardNo(virtualCardEnableDisableVO.getCardNo());
        virtualCardModel = virtualCardHibernateDAO.findByExample(virtualCardModel).getResultsetList().get(0);
        if (virtualCardModel.getIsBlocked())
            virtualCardModel.setIsBlocked(Boolean.FALSE);
        else
            virtualCardModel.setIsBlocked(Boolean.TRUE);
        virtualCardHibernateDAO.saveOrUpdate(virtualCardModel);
    }

    @Override
    public FonePayMessageVO makevalidateExistingCustomer(FonePayMessageVO fonePayMessageVO) throws Exception {
        String mobileNumber = fonePayMessageVO.getMobileNo();
        String cnic = fonePayMessageVO.getCnic();
        String decPin = null;
        String pin = null;
        if (fonePayMessageVO.getMpin() != null) {
            pin = com.inov8.microbank.common.util.EncryptionUtil.decryptWithAES(XMLConstants.AES_ENCRYPTION_KEY, pin);
        }

        AppUserModel customerAppUserModel = getCommonCommandManager().loadAppUserByMobileAndType(mobileNumber, UserTypeConstantsInterface.CUSTOMER);
        if (customerAppUserModel != null) {
            //Found Customer
            //validate its status

            //Mobile-CNIC - Validation
            if (fonePayMessageVO.isValidateMobileCnic() && !customerAppUserModel.getNic().equals(cnic)) {
                return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.CUSTOMER_NOT_FOUND);
            }


            if (null != pin && pin != "") {
                if (pin.equals("JSMPASS-1")) {

                } else {
                    //Web Service Channel - Validation
                    if (fonePayMessageVO.isValidateWebServiceEnabled() && !customerAppUserModel.getCustomerIdCustomerModel().getWebServiceEnabled()) {
                        return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.WEB_SERVICE_NOT_ENABLED);
                    }

                    //FonePay Channel - Validation
                    if (fonePayMessageVO.isValidateFonePayEnabled() && !customerAppUserModel.getCustomerIdCustomerModel().getFonePayEnabled()) {
                        return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.FONEPAY_NOT_ENABLED);
                    }
                }
            }

            //Is CNIC Blacklisted - Validation
            if (getCommonCommandManager().isCnicBlacklisted(customerAppUserModel.getNic())) {
                return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.CUSTOMER_CNIC_BLACKLISTED);
            }

            //Account Closed - Validation
            if (customerAppUserModel.getAccountClosedUnsettled() || customerAppUserModel.getAccountClosedSettled()) {
                return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.CUSTOMER_ACCOUNT_CLOSED);
            }

            //Account Discrepant Check on Transaction Request
            if (fonePayMessageVO.isValidateDiscripent()) {
                if (!(customerAppUserModel.getRegistrationStateId().equals(RegistrationStateConstants.VERIFIED)) && !(customerAppUserModel.getRegistrationStateId().equals(RegistrationStateConstants.REQUEST_RECEIVED))) {
                    return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.ACCOUNT_INVALID_STATE);
                }
            }

            // Smart Money Account - Validation
            SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
            SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
            smartMoneyAccountModel.setCustomerId(customerAppUserModel.getCustomerId());
            searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);

            searchBaseWrapper = getCommonCommandManager().loadSmartMoneyAccount(searchBaseWrapper);
            smartMoneyAccountModel = (SmartMoneyAccountModel) searchBaseWrapper.getCustomList().getResultsetList().get(0);

            if (smartMoneyAccountModel == null) {
                return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.SMA_NOT_LOADED);
            } else if (!smartMoneyAccountModel.getActive()) {
                return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.SMA_NOT_ACTIVE);
            }

            //UserDeviceAccount - Validation
            UserDeviceAccountsModel userDeviceAccountModel = new UserDeviceAccountsModel();
            userDeviceAccountModel.setAppUserId(customerAppUserModel.getAppUserId());
            userDeviceAccountModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);

            searchBaseWrapper = new SearchBaseWrapperImpl();
            searchBaseWrapper.setBasePersistableModel(userDeviceAccountModel);
            //searchBaseWrapper = checkLogin(searchBaseWrapper);
            CustomList<UserDeviceAccountsModel> customList = this.userDeviceAccountsDAO.findByExample(userDeviceAccountModel, null, null, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
            List<UserDeviceAccountsModel> list = customList.getResultsetList();
            if (list != null && list.size() > 0) {
                userDeviceAccountModel = list.get(0);
                if (userDeviceAccountModel.getUserId() != null) {
                    boolean messageSeparatorFlag = false;
                    if (userDeviceAccountModel.getAccountExpired()) {
                        return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.CUSTOMER_ACCOUNT_EXPIRED);
                    } else if (userDeviceAccountModel.getAccountLocked()) {
                        return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.CUSTOMER_ACCOUNT_BLOCKED);
                    } else if (userDeviceAccountModel.getCredentialsExpired()) {
                        return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.CUSTOMER_ACCOUNT_CREDENTIALS_EXP);
                    } else if (!userDeviceAccountModel.getAccountEnabled()) {
                        // return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.CUSTOMER_ACCOUNT_DEACTIVATED);
                        Long stateId = customerAppUserModel.getAccountStateId();
                        fonePayMessageVO.setResponseCode(FonePayResponseCodes.CUSTOMER_ACCOUNT_DEACTIVATED);
                        if (stateId.equals(AccountStateConstantsInterface.ACCOUNT_STATE_HOT)) {
                            fonePayMessageVO.setResponseCodeDescription(FonePayResponseCodes.ACCOUNT_STATE_HOT);
                        } else if (stateId.equals(AccountStateConstantsInterface.ACCOUNT_STATE_WARM)) {
                            fonePayMessageVO.setResponseCodeDescription(FonePayResponseCodes.ACCOUNT_STATE_WARM);
                        } else if (stateId.equals(AccountStateConstantsInterface.ACCOUNT_STATE_COLD)) {
                            fonePayMessageVO.setResponseCodeDescription(FonePayResponseCodes.ACCOUNT_STATE_COLD);
                        } else if (stateId.equals(AccountStateConstantsInterface.ACCOUNT_STATE_DECEASED)) {
                            fonePayMessageVO.setResponseCodeDescription(FonePayResponseCodes.ACCOUNT_STATE_DECEASED);
                        } else if (stateId.equals(AccountStateConstantsInterface.ACCOUNT_STATE_DORMANT)) {
                            fonePayMessageVO.setResponseCodeDescription(FonePayResponseCodes.ACCOUNT_STATE_DORMANT);
                        } else if (stateId.equals(AccountStateConstantsInterface.ACCOUNT_STATE_CLOSED)) {
                            fonePayMessageVO.setResponseCodeDescription(FonePayResponseCodes.ACCOUNT_STATE_CLOSED);
                        }
                        return fonePayMessageVO;
                    }
                } else {
                    return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.CUSTOMER_ACCOUNT_CREDENTIALS_INVALID);
                }
            } else {
                return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.CUSTOMER_ACCOUNT_CREDENTIALS_MISSING);
            }

            fonePayMessageVO.setCustomerId(list.get(0).getUserId());
        } else {
            return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.CUSTOMER_NOT_FOUND);
        }


        //Prepare SUCCESS MESSAGE
        String accountTitle = StringUtils.trim(customerAppUserModel.getFirstName() + " " + customerAppUserModel.getLastName());
        fonePayMessageVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
        fonePayMessageVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
        fonePayMessageVO.setAccountTitle(accountTitle);
        fonePayMessageVO.setAccountStatus(FonePayConstants.CUSTOMER_STATUS_ACTIVE);
        fonePayMessageVO.setAppUserId(customerAppUserModel.getAppUserId());
        fonePayMessageVO.setAccountType(String.valueOf(customerAppUserModel.getCustomerIdCustomerModel().getCustomerAccountTypeId()));

        if (RegistrationStateConstants.DISCREPANT.equals(customerAppUserModel.getRegistrationStateId())) {
            //fonePayMessageVO.setCustomerStatus("2");
            if (customerAppUserModel.getNicExpiryDate() != null) {
                fonePayMessageVO.setCnicExpiry(customerAppUserModel.getNicExpiryDate().toString());
            }
            fonePayMessageVO.setFirstName(customerAppUserModel.getFirstName());
            fonePayMessageVO.setLastName(customerAppUserModel.getLastName());
            fonePayMessageVO.setDob(customerAppUserModel.getDob().toString());
            fonePayMessageVO.setCustomerAccountyType(customerAppUserModel.getCustomerIdCustomerModel().getCustomerAccountTypeId().toString());
            List<CustomerPictureModel> customerPictureModelList = new ArrayList<>();
            CustomerPictureModel customerPictureModel = new CustomerPictureModel();
            customerPictureModel.setCustomerId(customerAppUserModel.getCustomerId());
            customerPictureModel.setDiscrepant(true);
            customerPictureModelList = this.customerPictureDAO.findByExample(customerPictureModel).getResultsetList();
            if (customerPictureModelList != null && customerPictureModelList.size() > 0) {
                if (customerPictureModelList.size() >= 2) {
                    fonePayMessageVO.setCustomerStatus("4");
                } else {
                    if (PictureTypeConstants.ID_FRONT_SNAPSHOT.equals(customerPictureModelList.get(0).getPictureTypeId())) {
                        fonePayMessageVO.setCustomerStatus("3");
                    } else {
                        fonePayMessageVO.setCustomerStatus("2");
                    }
                }
            }
        }

        return fonePayMessageVO;
    }

    @Override
    public WebServiceVO makevalidateCustomer(WebServiceVO fonePayMessageVO) throws Exception {
        String mobileNumber = fonePayMessageVO.getMobileNo();
        String cnic = fonePayMessageVO.getCnicNo();

        AppUserModel customerAppUserModel = getCommonCommandManager().loadAppUserByMobileAndType(mobileNumber, UserTypeConstantsInterface.CUSTOMER);
        CustomerModel customerModel = getCommonCommandManager().getCustomerModelById(customerAppUserModel.getCustomerId());

        if (customerAppUserModel != null) {
            //Found Customer
            //validate its status

            //Mobile-CNIC - Validation
            if (!customerAppUserModel.getNic().equals(cnic)) {
                return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.CUSTOMER_NOT_FOUND);
            }

            //Web Service Channel - Validation
            if (!customerAppUserModel.getCustomerIdCustomerModel().getWebServiceEnabled()) {
                return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.WEB_SERVICE_NOT_ENABLED);
            }


            //Is CNIC Blacklisted - Validation
            if (getCommonCommandManager().isCnicBlacklisted(customerAppUserModel.getNic())) {
                return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.CUSTOMER_CNIC_BLACKLISTED);
            }

            //Account Closed - Validation
            if (customerAppUserModel.getAccountClosedUnsettled() || customerAppUserModel.getAccountClosedSettled()) {
                return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.CUSTOMER_ACCOUNT_CLOSED);
            }

            //Account Discrepant Check on Transaction Request
            if (!(customerAppUserModel.getRegistrationStateId().equals(RegistrationStateConstants.VERIFIED)) &&
                    !(customerAppUserModel.getRegistrationStateId().equals(RegistrationStateConstants.REQUEST_RECEIVED))

                    //comment check for cls
                    &&
                    !(customerAppUserModel.getRegistrationStateId().equals(RegistrationStateConstants.CLSPENDING))) {
                if (!(customerModel.getSegmentId().equals(Long.parseLong(MessageUtil.getMessage("Minor_segment_id"))))) {

                    return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.ACCOUNT_INVALID_STATE);
                }
            }

            // Smart Money Account - Validation

            Long paymentModeId = null;
            if (fonePayMessageVO.getPaymentMode() != null && fonePayMessageVO.getPaymentMode().equals("1"))
                paymentModeId = PaymentModeConstantsInterface.HOME_REMMITTANCE_ACCOUNT;
            else
                paymentModeId = PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT;

            SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
            SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
            smartMoneyAccountModel.setCustomerId(customerAppUserModel.getCustomerId());
            smartMoneyAccountModel.setPaymentModeId(paymentModeId);
            searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);

            searchBaseWrapper = getCommonCommandManager().loadSmartMoneyAccount(searchBaseWrapper);
            smartMoneyAccountModel = (SmartMoneyAccountModel) searchBaseWrapper.getCustomList().getResultsetList().get(0);

            if (smartMoneyAccountModel == null) {
                return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.SMA_NOT_LOADED);
            } else if (!smartMoneyAccountModel.getActive()) {
                return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.SMA_NOT_ACTIVE);
            }

            //UserDeviceAccount - Validation
            UserDeviceAccountsModel userDeviceAccountModel = new UserDeviceAccountsModel();
            userDeviceAccountModel.setAppUserId(customerAppUserModel.getAppUserId());
            userDeviceAccountModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);

            searchBaseWrapper = new SearchBaseWrapperImpl();
            searchBaseWrapper.setBasePersistableModel(userDeviceAccountModel);
            //searchBaseWrapper = checkLogin(searchBaseWrapper);
            CustomList<UserDeviceAccountsModel> customList = this.userDeviceAccountsDAO.findByExample(userDeviceAccountModel, null, null, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
            List<UserDeviceAccountsModel> list = customList.getResultsetList();
            if (list != null && list.size() > 0) {
                userDeviceAccountModel = list.get(0);
                if (userDeviceAccountModel.getUserId() != null) {
                    boolean messageSeparatorFlag = false;
                    if (userDeviceAccountModel.getAccountExpired()) {
                        return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.CUSTOMER_ACCOUNT_EXPIRED);
                    } else if (userDeviceAccountModel.getAccountLocked()) {
                        return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.CUSTOMER_ACCOUNT_BLOCKED);
                    } else if (userDeviceAccountModel.getCredentialsExpired()) {
                        return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.CUSTOMER_ACCOUNT_CREDENTIALS_EXP);
                    } else if (!userDeviceAccountModel.getAccountEnabled()) {
                        return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.CUSTOMER_ACCOUNT_DEACTIVATED);
                    }
                } else {
                    return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.CUSTOMER_ACCOUNT_CREDENTIALS_INVALID);
                }
            } else {
                return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.CUSTOMER_ACCOUNT_CREDENTIALS_MISSING);
            }

        } else {
            return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.CUSTOMER_NOT_FOUND);
        }

        //Prepare SUCCESS MESSAGE
        String accountTitle = StringUtils.trim(customerAppUserModel.getFirstName() + " " + customerAppUserModel.getLastName());
        fonePayMessageVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
        fonePayMessageVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
        fonePayMessageVO.setAccountTitle(accountTitle);

        return fonePayMessageVO;
    }

    @Override
    public WebServiceVO makevalidateCustomerForResetPinAPI(WebServiceVO fonePayMessageVO) throws Exception {
        String mobileNumber = fonePayMessageVO.getMobileNo();
        String cnic = fonePayMessageVO.getCnicNo();

        AppUserModel customerAppUserModel = getCommonCommandManager().loadAppUserByMobileAndType(mobileNumber, UserTypeConstantsInterface.CUSTOMER);
        if (customerAppUserModel != null) {
            //Found Customer
            //validate its status

            //Mobile-CNIC - Validation
            if (!customerAppUserModel.getNic().equals(cnic)) {
                return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.CUSTOMER_NOT_FOUND);
            }

            //Web Service Channel - Validation
            if (!customerAppUserModel.getCustomerIdCustomerModel().getWebServiceEnabled()) {
                return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.WEB_SERVICE_NOT_ENABLED);
            }


            //Is CNIC Blacklisted - Validation
            if (getCommonCommandManager().isCnicBlacklisted(customerAppUserModel.getNic())) {
                return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.CUSTOMER_CNIC_BLACKLISTED);
            }

            //Account Closed - Validation
            if (customerAppUserModel.getAccountClosedUnsettled() || customerAppUserModel.getAccountClosedSettled()) {
                return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.CUSTOMER_ACCOUNT_CLOSED);
            }

            //Account Discrepant Check on Transaction Request
            if (!(customerAppUserModel.getRegistrationStateId().equals(RegistrationStateConstants.VERIFIED)) && !(customerAppUserModel.getRegistrationStateId().equals(RegistrationStateConstants.REQUEST_RECEIVED)) && !(customerAppUserModel.getRegistrationStateId().equals(RegistrationStateConstants.CLSPENDING))) {
                return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.ACCOUNT_INVALID_STATE);
            }

            // Smart Money Account - Validation

            Long paymentModeId = null;
            if (fonePayMessageVO.getPaymentMode() != null && fonePayMessageVO.getPaymentMode().equals("1"))
                paymentModeId = PaymentModeConstantsInterface.HOME_REMMITTANCE_ACCOUNT;
            else
                paymentModeId = PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT;

            SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
            SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
            smartMoneyAccountModel.setCustomerId(customerAppUserModel.getCustomerId());
            smartMoneyAccountModel.setPaymentModeId(paymentModeId);
            searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);

            searchBaseWrapper = getCommonCommandManager().loadSmartMoneyAccount(searchBaseWrapper);
            smartMoneyAccountModel = (SmartMoneyAccountModel) searchBaseWrapper.getCustomList().getResultsetList().get(0);

            if (smartMoneyAccountModel == null) {
                return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.SMA_NOT_LOADED);
            } else if (!smartMoneyAccountModel.getActive()) {
                return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.SMA_NOT_ACTIVE);
            }

            //UserDeviceAccount - Validation
            UserDeviceAccountsModel userDeviceAccountModel = new UserDeviceAccountsModel();
            userDeviceAccountModel.setAppUserId(customerAppUserModel.getAppUserId());
            userDeviceAccountModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);

            searchBaseWrapper = new SearchBaseWrapperImpl();
            searchBaseWrapper.setBasePersistableModel(userDeviceAccountModel);
            //searchBaseWrapper = checkLogin(searchBaseWrapper);
            CustomList<UserDeviceAccountsModel> customList = this.userDeviceAccountsDAO.findByExample(userDeviceAccountModel, null, null, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
            List<UserDeviceAccountsModel> list = customList.getResultsetList();
            if (list != null && list.size() > 0) {
                userDeviceAccountModel = list.get(0);
                if (userDeviceAccountModel.getUserId() != null) {
                    boolean messageSeparatorFlag = false;
                    if (userDeviceAccountModel.getAccountExpired()) {
                        return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.CUSTOMER_ACCOUNT_EXPIRED);
//                    } else if (userDeviceAccountModel.getAccountLocked()) {
//                        return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.CUSTOMER_ACCOUNT_BLOCKED);
                    } else if (userDeviceAccountModel.getCredentialsExpired()) {
                        return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.CUSTOMER_ACCOUNT_CREDENTIALS_EXP);
                    } else if (!userDeviceAccountModel.getAccountEnabled()) {
                        return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.CUSTOMER_ACCOUNT_DEACTIVATED);
                    }
                } else {
                    return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.CUSTOMER_ACCOUNT_CREDENTIALS_INVALID);
                }
            } else {
                return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.CUSTOMER_ACCOUNT_CREDENTIALS_MISSING);
            }

        } else {
            return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.CUSTOMER_NOT_FOUND);
        }

        //Prepare SUCCESS MESSAGE
        String accountTitle = StringUtils.trim(customerAppUserModel.getFirstName() + " " + customerAppUserModel.getLastName());
        fonePayMessageVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
        fonePayMessageVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
        fonePayMessageVO.setAccountTitle(accountTitle);

        return fonePayMessageVO;

    }

    @Override
    public WebServiceVO getMiniStatment(WebServiceVO webServiceVO) throws Exception {
        try {
            webServiceVO = this.makevalidateCustomer(webServiceVO);

            Long paymentModeId = null;
            if (webServiceVO.getPaymentMode() != null && webServiceVO.getPaymentMode().equals("1"))
                paymentModeId = PaymentModeConstantsInterface.HOME_REMMITTANCE_ACCOUNT;
            else
                paymentModeId = PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT;

            AppUserModel appUserModel = new AppUserModel();
            if (webServiceVO.getResponseCode() == FonePayResponseCodes.SUCCESS_RESPONSE_CODE) {
                List<MiniStatementListViewModel> list = new ArrayList<>();
                List<Transaction> miniTransactionList = new ArrayList<>();
                appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
                Long type = appUserModel.getAppUserTypeId().longValue();
                MiniStatementListViewModel viewModel = new MiniStatementListViewModel();
                viewModel.setUserType(type);
                String mfsId;

                if (type == UserTypeConstantsInterface.CUSTOMER) {
                    mfsId = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId();
                    viewModel.setRecipientMfsId(mfsId);
                    viewModel.setMfsId(mfsId);
                    viewModel.setPaymentModeId(paymentModeId);
                }
                list = getCommonCommandManager().getMiniStatementListViewModelList(viewModel, Integer.valueOf(5));

                for (MiniStatementListViewModel l1 :
                        list) {
                    Transaction t1 = new Transaction();
                    t1.setDate(l1.getCreatedOn().toString());
                    t1.setAmount(l1.getTotalAmount().toString());
                    t1.setDescription(l1.getProductName());
                    miniTransactionList.add(t1);
                }

                webServiceVO.setTransactions(miniTransactionList);
                webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);

            }

        } catch (Exception e) {
            logger.error("[FonePayManagerImpl.getMiniStatment] Exception occured: " + e.getMessage(), e);
            webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR);
            throw new Exception(e.getMessage());
        }
        return webServiceVO;

    }


    @Override
    public FonePayMessageVO makevalidateAgent(FonePayMessageVO fonePayMessageVO) throws Exception {
        String mobileNumber = fonePayMessageVO.getMobileNo();
        String cnic = fonePayMessageVO.getCnic();

        AppUserModel customerAppUserModel = getCommonCommandManager().loadAppUserByMobileAndType(mobileNumber, UserTypeConstantsInterface.RETAILER);
        if (customerAppUserModel != null) {
            //Found Customer
            //validate its status

            //Mobile-CNIC - Validation
            if (fonePayMessageVO.isValidateMobileCnic() && !customerAppUserModel.getNic().equals(cnic)) {
                return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.CUSTOMER_NOT_FOUND);
            }

            //Is CNIC Blacklisted - Validation
            if (getCommonCommandManager().isCnicBlacklisted(customerAppUserModel.getNic())) {
                return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.CNIC_BLACKLISTED);
            }

            //Account Closed - Validation
            if (customerAppUserModel.getAccountClosedUnsettled() || customerAppUserModel.getAccountClosedSettled()) {
                return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.ACCOUNT_CLOSED);
            }

            //Account Discrepant Check on Transaction Request
            if (fonePayMessageVO.isValidateDiscripent()) {
                if (!(customerAppUserModel.getRegistrationStateId().equals(RegistrationStateConstants.VERIFIED)) && !(customerAppUserModel.getRegistrationStateId().equals(RegistrationStateConstants.REQUEST_RECEIVED))) {
                    return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.ACCOUNT_INVALID_STATE);
                }
            }

            // Smart Money Account - Validation
            SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
            SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
            smartMoneyAccountModel.setRetailerContactId(customerAppUserModel.getRetailerContactId());
            searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);

            searchBaseWrapper = getCommonCommandManager().loadSmartMoneyAccount(searchBaseWrapper);
            smartMoneyAccountModel = (SmartMoneyAccountModel) searchBaseWrapper.getCustomList().getResultsetList().get(0);

            if (smartMoneyAccountModel == null) {
                return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.SMA_NOT_LOADED);
            } else if (!smartMoneyAccountModel.getActive()) {
                return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.SMA_NOT_ACTIVE);
            }

            //UserDeviceAccount - Validation
            UserDeviceAccountsModel userDeviceAccountModel = new UserDeviceAccountsModel();
            userDeviceAccountModel.setAppUserId(customerAppUserModel.getAppUserId());
            userDeviceAccountModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);

            searchBaseWrapper = new SearchBaseWrapperImpl();
            searchBaseWrapper.setBasePersistableModel(userDeviceAccountModel);
            //searchBaseWrapper = checkLogin(searchBaseWrapper);
            CustomList<UserDeviceAccountsModel> customList = this.userDeviceAccountsDAO.findByExample(userDeviceAccountModel, null, null, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
            List<UserDeviceAccountsModel> list = customList.getResultsetList();
            if (list != null && list.size() > 0) {
                userDeviceAccountModel = list.get(0);
                if (userDeviceAccountModel.getUserId() != null) {
                    boolean messageSeparatorFlag = false;
                    if (userDeviceAccountModel.getAccountExpired()) {
                        return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.ACCOUNT_EXPIRED);
                    } else if (userDeviceAccountModel.getAccountLocked()) {
                        return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.ACCOUNT_BLOCKED);
                    } else if (userDeviceAccountModel.getCredentialsExpired()) {
                        return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.ACCOUNT_CREDENTIALS_EXP);
                    } else if (!userDeviceAccountModel.getAccountEnabled()) {
                        return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.ACCOUNT_DEACTIVATED);
                    }
                } else {
                    return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.ACCOUNT_CREDENTIALS_INVALID);
                }
            } else {
                return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.ACCOUNT_CREDENTIALS_MISSING);
            }

            fonePayMessageVO.setCustomerId(list.get(0).getUserId());
        } else {
            return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.AGENT_NOT_FOUND);
        }

        //Prepare SUCCESS MESSAGE
        String accountTitle = StringUtils.trim(customerAppUserModel.getFirstName() + " " + customerAppUserModel.getLastName());
        fonePayMessageVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
        fonePayMessageVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
        fonePayMessageVO.setAccountTitle(accountTitle);
        fonePayMessageVO.setAccountStatus(FonePayConstants.CUSTOMER_STATUS_ACTIVE);
        fonePayMessageVO.setAppUserId(customerAppUserModel.getAppUserId());
        return fonePayMessageVO;
    }

    public FonePayMessageVO validateCardInfo(FonePayMessageVO fonePayMessageVO) throws Exception {
        VirtualCardModel virtualCardModel = new VirtualCardModel();
        virtualCardModel.setCardNo(fonePayMessageVO.getCardNo());
        virtualCardModel.setIsBlocked(false);
        virtualCardModel.setDeleted(false);
        List<VirtualCardModel> virtualCardModelList = virtualCardHibernateDAO.findByExample(virtualCardModel).getResultsetList();
        if (virtualCardModelList.size() > 0) {
            fonePayMessageVO.setResponseCode("51");
            return FonePayUtils.prepareErrorResponse(fonePayMessageVO, FonePayResponseCodes.CARD_ALREADY_TAG);
        }
        fonePayMessageVO.setResponseCode("00");
        return fonePayMessageVO;
    }

    public WebServiceVO validateFonePayCustomer(WebServiceVO integrationMessageVO) throws Exception {
        FonePayMessageVO fonePayMessageVO = new FonePayMessageVO();
        fonePayMessageVO.setMobileNo(integrationMessageVO.getMobileNo());
        fonePayMessageVO.setCnic(integrationMessageVO.getCnicNo());
        fonePayMessageVO.setValidateWebServiceEnabled(true);
        fonePayMessageVO = this.makevalidateExistingCustomer(fonePayMessageVO);
        String mobileNumber = integrationMessageVO.getAccountNo1();
        String cnic = integrationMessageVO.getCnicNo();
        if (FonePayResponseCodes.SUCCESS_RESPONSE_CODE.equals(integrationMessageVO.getResponseCode())) {
            AppUserModel customerAppUserModel = getCommonCommandManager().loadAppUserByMobileAndType(mobileNumber, UserTypeConstantsInterface.CUSTOMER);
            if (customerAppUserModel != null
                    && customerAppUserModel.getNic() != null
                    && customerAppUserModel.getNic().equals(cnic)) {
                CustomerModel customerModel = customerAppUserModel.getCustomerIdCustomerModel();
                if (!customerModel.getWebServiceEnabled()) {
                    return FonePayUtils.prepareErrorResponse(integrationMessageVO, FonePayResponseCodes.WEB_SERVICE_NOT_ENABLED);
                }
            }
        }
        return integrationMessageVO;

    }

    @Override
    public WebServiceVO makeLinkDelinkAccount(WebServiceVO webServiceVO) throws FrameworkCheckedException {
        String mobileNumber = webServiceVO.getMobileNo();
        String cnic = webServiceVO.getCnicNo();
        String transactionType = webServiceVO.getTransactionType();
        logger.info("[FonePayManagerImpl.makeLinkDelinkAccount] mob:" + mobileNumber + " cnic:" + cnic + " transactionType:" + transactionType);
        if (StringUtil.isNullOrEmpty(transactionType) || !(transactionType.equals(FonePayConstants.ACCOUNT_LINK) || transactionType.equals(FonePayConstants.ACCOUNT_DELINK))) {
            logger.error("[makeLinkDelinkAccount] Invalid Input transactionType:" + transactionType + " [ Expected value is " + FonePayConstants.ACCOUNT_LINK + " / " + FonePayConstants.ACCOUNT_DELINK + " ]");
            return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INPUT_ERROR);
        }

        boolean isLinkRequest = false;
        if (transactionType.equals(FonePayConstants.ACCOUNT_LINK)) {
            isLinkRequest = true;
        }

        AppUserModel customerAppUserModel = getCommonCommandManager().loadAppUserByMobileAndType(mobileNumber, UserTypeConstantsInterface.CUSTOMER);
        if (customerAppUserModel != null
                && customerAppUserModel.getNic() != null
                && customerAppUserModel.getNic().equals(cnic)) {

            CustomerModel customerModel = customerAppUserModel.getCustomerIdCustomerModel();
            if (webServiceVO.getChannelId().equals(FonePayConstants.APIGEE_CHANNEL)) {
                if (customerModel.getWebServiceEnabled().equals(false) && !isLinkRequest) {
                    return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.ACC_ALREADY_DELINKED);
                }
//				if (customerModel.getWebServiceEnabled().equals(true) && isLinkRequest) {
//					return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.ACC_ALREADY_LINKED);
//				}
            }
            if (!webServiceVO.getChannelId().equals(FonePayConstants.APIGEE_CHANNEL)) {
                if (customerModel.getFonePayEnabled().equals(false) && !isLinkRequest) {
                    return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.ACC_ALREADY_DELINKED);
                }
//				if (customerModel.getFonePayEnabled().equals(true) && isLinkRequest) {
//					return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.ACC_ALREADY_LINKED);
//				}
            }

            String action = "";
            if (webServiceVO.getChannelId().equals(FonePayConstants.APIGEE_CHANNEL)) {
                if (isLinkRequest) {// link request for fonepay
                    action = "linked";
                    customerModel.setWebServiceEnabled(true);
                } else { // delink request
                    action = "delinked";
                    customerModel.setWebServiceEnabled(false);
                }
            }
            if (!webServiceVO.getChannelId().equals(FonePayConstants.APIGEE_CHANNEL)) {
                if (isLinkRequest) {
                    action = "linked";
                    customerModel.setFonePayEnabled(true);
                } else {
                    action = "delinked";
                    customerModel.setFonePayEnabled(false);
                }
            }
            getCommonCommandManager().saveOrUpdateCustomerModel(customerModel);

            webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
            webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);

            logger.info("Mobile:" + mobileNumber + " cnic:" + cnic + " " + action + " successfully.");

        } else {
            return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUSTOMER_NOT_FOUND);
        }

        return webServiceVO;
    }


    private AppUserModel isMobileNumUnique(String mobileNo) throws NoSuchMessageException, FrameworkCheckedException {
        AppUserModel appUserModel = null;
        try {
            Long[] appUserTypes = new Long[]{UserTypeConstantsInterface.CUSTOMER, UserTypeConstantsInterface.RETAILER, UserTypeConstantsInterface.HANDLER};
            appUserModel = this.getCommonCommandManager().loadAppUserByMobileAndType(mobileNo, appUserTypes);
        } catch (Exception e) {
            this.logger.error("[FonePayManagerImpl.isMobileNumUnique] Exception: " + e.getMessage(), e);
        }
        return appUserModel;
    }

    private AppUserModel isCNICUnique(String cnic) {
        AppUserModel appUserModel = null;
        try {
            appUserModel = this.getCommonCommandManager().loadAppUserByCnicAndType(cnic);
        } catch (Exception e) {
            this.logger.error("[FonePayManagerImpl.isCNICUnique] Exception: " + e.getMessage(), e);
        }
        return appUserModel;
    }

    private AppUserModel isEmailUnique(String email) {
        AppUserModel appUserModel = null;
        try {
            appUserModel = this.getCommonCommandManager().loadAppUserModelByEmailAddress(email);
        } catch (Exception e) {
            this.logger.error("[FonePayManagerImpl.isCNICUnique] Exception: " + e.getMessage(), e);
        }
        return appUserModel;
    }

    @Override
    public void createMiniTransactionModel(String encryptedPin, String mobileNo, String channelId, String commandId) throws Exception {
        MiniTransactionModel miniTransactionModel = new MiniTransactionModel();
        try {
            // mark all previous records having same app user id, mobile and command id in state PIN_SENT
            MiniTransactionModel mtModel = new MiniTransactionModel();
            mtModel.setMobileNo(mobileNo);
            mtModel.setCommandId(Long.valueOf(commandId));
            mtModel.setMiniTransactionStateId(MiniTransactionStateConstant.PIN_SENT);
            miniTransactionDAO.updateMiniTransactionModelsFonepay(mtModel, MiniTransactionStateConstant.EXPIRED);

            miniTransactionModel.setAppUserId(PortalConstants.SCHEDULER_APP_USER_ID);
            miniTransactionModel.setTimeDate(new Date());
            miniTransactionModel.setCommandId(Long.valueOf(commandId));
            miniTransactionModel.setMobileNo(mobileNo);
            miniTransactionModel.setMiniTransactionStateId(MiniTransactionStateConstant.PIN_SENT);
            miniTransactionModel.setActionLogId(ThreadLocalActionLog.getActionLogId());
            miniTransactionModel.setOneTimePin(encryptedPin);
            miniTransactionModel.setIsManualOTPin(Boolean.FALSE);
            miniTransactionModel.setCreatedBy(PortalConstants.SCHEDULER_APP_USER_ID);
            miniTransactionModel.setUpdatedBy(PortalConstants.SCHEDULER_APP_USER_ID);
            miniTransactionModel.setCreatedOn(new Date());
            miniTransactionModel.setUpdatedOn(new Date());
            miniTransactionModel.setChannelId(channelId);

            miniTransactionDAO.saveOrUpdate(miniTransactionModel);
        } catch (Exception e) {
            logger.error("[FonePayManagerImpl.createMiniTransactionModel] Exception while saving MiniTransactionModel. ErrorMessage:" + e.getMessage());
            throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
        }
    }

    @Override
    public SearchBaseWrapper loadVirtualCard(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        CustomList<VirtualCardModel>
                list = this.virtualCardHibernateDAO.findByExample((VirtualCardModel)
                        searchBaseWrapper.getBasePersistableModel(),
                searchBaseWrapper.getPagingHelperModel(),
                searchBaseWrapper.getSortingOrderMap(), searchBaseWrapper.getDateRangeHolderModel());

        if (list != null) {
            searchBaseWrapper.setCustomList(list);
        }
        return searchBaseWrapper;

    }


    @Override
    public FonePayLogModel saveFonePayIntegrationLogModel(WebServiceVO webServiceVO, String reqType)
            throws Exception {
        FonePayLogModel fonePayLogModel = new FonePayLogModel();
        Date date = new Date();
        Timestamp ts_now = new Timestamp(date.getTime());

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(Inclusion.NON_NULL);
        JSONObject json = null;
        String jsonInString = "";
//||reqType.equals(FonePayConstants.REQ_ACCOUNT_OPENING_L2)
        if (reqType.equals(FonePayConstants.REQ_ACCOUNT_OPENING_CONVENTIONAL) || reqType.equals(FonePayConstants.REQ_ACCOUNT_OPENING_L2) ||
                reqType.equals(FonePayConstants.REQ_L2_UPGRADE) || reqType.equals(FonePayConstants.REQ_ACCOUNT_OPENING) || reqType.equals("Minor Account Opening") || reqType.equals("UpdateMinorAccount")) {
            WebServiceVO cloneVoObject = new WebServiceVO();
            BeanUtils.copyProperties(webServiceVO, cloneVoObject, new String[]{"customerPhoto", "cnicFrontPhoto",
                    "cnicBackPhoto", "signaturePhoto", "termsPhoto", "sourceOfIncomePic", "reserved3", "reserved2", "reserved5", "reserved6", "parentCnicPic", "bFormPic", "snicPic", "minorCustomerPic", "sNicBackPic", "parentNicBackPic"});
            jsonInString = mapper.writeValueAsString(cloneVoObject);
        } else {
            jsonInString = mapper.writeValueAsString(webServiceVO);
        }

        if (reqType.equals(FonePayConstants.REQ_L2_UPGRADE)) {
            json = new JSONObject(jsonInString);
        } else {
            String inputparam = CommonUtils.getJSON(webServiceVO);
            json = new JSONObject(inputparam);
        }

        if (webServiceVO.getMobilePin() != null && !webServiceVO.getMobilePin().equals("")) {
            json.put("mobilePin", "****");
            jsonInString = String.valueOf(json);
        }

        if (webServiceVO.getOtpPin() != null && !webServiceVO.getOtpPin().equals("")) {
            json.put("otpPin", "****");
            jsonInString = String.valueOf(json);
        }
        if (!reqType.equals(FonePayConstants.REQ_ACCOUNT_OPENING_L2)) {
            if (webServiceVO.getReserved2() != null && !webServiceVO.getReserved2().equals("")) {
                fonePayLogModel.setStan(webServiceVO.getReserved2());
            } else {
                String str = webServiceVO.getRetrievalReferenceNumber();
                StringBuilder strBuilder = new StringBuilder();
                Integer lengt = str.length();
                for (int i = 6; i >= 1; i--) {
                    strBuilder.append(str.charAt(lengt - i));
                }
                fonePayLogModel.setStan(strBuilder.toString());
            }
        }
        fonePayLogModel.setCnic(webServiceVO.getCnicNo());
        fonePayLogModel.setMobile_no(webServiceVO.getMobileNo());
        fonePayLogModel.setRequestType(reqType);
        fonePayLogModel.setResponse_code(webServiceVO.getResponseCode());
        fonePayLogModel.setResponse_description(webServiceVO.getResponseCodeDescription());
        fonePayLogModel.setRrn(webServiceVO.getRetrievalReferenceNumber());
        fonePayLogModel.setTransactionId(webServiceVO.getTransactionId());

        fonePayLogModel.setCreated_on(ts_now);
        fonePayLogModel.setUpdated_on(ts_now);
        fonePayLogModel.setInput(jsonInString);

        fonePayLogModel = fonePayLogDAO.saveOrUpdate(fonePayLogModel);

        return fonePayLogModel;
    }

    @Override
    public void updateFonePayIntegrationLogModel(FonePayLogModel model, WebServiceVO webServiceVO) {

        if (model == null || model.getPrimaryKey() == null || webServiceVO == null) {
            return;
        }

        Date date = new Date();
        Timestamp ts_now = new Timestamp(date.getTime());

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(Inclusion.NON_NULL);
        JSONObject json = null;
        String jsonInString = "";
        String jsonOutString = null;
        try {
            //||model.getRequestType().equals(FonePayConstants.REQ_ACCOUNT_OPENING_L2)
            if (model.getRequestType().equals(FonePayConstants.REQ_ACCOUNT_OPENING_CONVENTIONAL) || model.getRequestType().equals(FonePayConstants.REQ_ACCOUNT_OPENING_L2)
                    || model.getRequestType().equals(FonePayConstants.REQ_L2_UPGRADE) || model.getRequestType().equals(FonePayConstants.REQ_ACCOUNT_OPENING) || model.getRequestType().equals("Minor Account Opening") || model.getRequestType().equals("UpdateMinorAccount")) {
                WebServiceVO cloneVoObject = new WebServiceVO();
                BeanUtils.copyProperties(webServiceVO, cloneVoObject, new String[]{"customerPhoto", "cnicFrontPhoto", "cnicBackPhoto",
                        "signaturePhoto", "termsPhoto", "sourceOfIncomePic", "reserved2", "reserved3", "reserved6", "reserved5", "parentCnicPic", "bFormPic", "snicPic", "minorCustomerPic", "sNicBackPic", "parentNicBackPic"});
                jsonOutString = mapper.writeValueAsString(cloneVoObject);
            } else {
                jsonOutString = mapper.writeValueAsString(webServiceVO);

                if (model.getRequestType().equals(FonePayConstants.REQ_L2_UPGRADE)) {
                    String inputparam = CommonUtils.getJSON(jsonInString);
                    json = new JSONObject(inputparam);
                } else {
                    String inputparam = CommonUtils.getJSON(webServiceVO);
                    json = new JSONObject(inputparam);
                }

                if (webServiceVO.getMobilePin() != null && !webServiceVO.getMobilePin().equals("")) {
                    json.put("mobilePin", "****");
                    jsonOutString = String.valueOf(json);
                }

                if (webServiceVO.getOtpPin() != null && !webServiceVO.getOtpPin().equals("")) {
                    json.put("otpPin", "****");
                    jsonOutString = String.valueOf(json);
                }

            }
        } catch (Exception e) {
            logger.error("Problem occurred during converting  WebServiceVO into Json String.", e);
        }

        model.setResponse_code(webServiceVO.getResponseCode());
        model.setResponse_description(webServiceVO.getResponseCodeDescription());

        model.setOutput(jsonOutString);
        model.setUpdated_on(ts_now);
        model.setTransactionId(webServiceVO.getMicrobankTransactionCode());

        try {
            fonePayLogDAO.saveOrUpdate(model);
        } catch (Exception exp) {
            logger.error("[updateFonePayIntegrationLogModel] Problem occurred during updating  : Response Code : " + webServiceVO.getResponseCode() + " in FonePayLogModel", exp);
        }
    }

    @Override
    public SearchBaseWrapper searchFonePayLogModels(
            SearchBaseWrapper searchBaseWrapper) {

        FonePayLogModel fonePayLogModel = new FonePayLogModel();
        fonePayLogModel = (FonePayLogModel) searchBaseWrapper.getBasePersistableModel();

        CustomList<FonePayLogModel> fonePayLogModelList = fonePayLogDAO.findByExample(fonePayLogModel, searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap(), searchBaseWrapper.getDateRangeHolderModel(), null);

        SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
        wrapper.setCustomList(fonePayLogModelList);

        return wrapper;
    }

    @Override
    public void updateCustomerModelWithAuthorization(BaseWrapper baseWrapper)
            throws FrameworkCheckedException {
        CustomerVO customerVO = (CustomerVO) baseWrapper.getBasePersistableModel();
        CustomerModel customerModel = new CustomerModel();

        customerModel = customerDAO.findByPrimaryKey(Long.parseLong(customerVO.getCustomerId()));

        customerModel.setUpdatedOn(new Date());
        customerModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
        customerModel.setWebServiceEnabled(customerVO.getIsWebServiceEnabled());


        customerDAO.saveOrUpdate(customerModel);
    }


    @Override
    public WebServiceVO makeChecqueBookStatus(WebServiceVO webServiceVO) throws Exception {
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        TransactionDetailMasterModel txDetailMaster = new TransactionDetailMasterModel();
        txDetailMaster.setTransactionCode(webServiceVO.getMicrobankTransactionCode());
        baseWrapper.setBasePersistableModel(txDetailMaster);
        baseWrapper = transactionDetailMasterManager.loadAndLockTransactionDetailMasterModel(baseWrapper);
        txDetailMaster = (TransactionDetailMasterModel) baseWrapper.getBasePersistableModel();

        if (txDetailMaster.getPk() != null) {
            if ((txDetailMaster.getSupProcessingStatusId().equals(SupplierProcessingStatusConstants.COMPLETED) && (txDetailMaster.getProductId().equals(Long.parseLong(MessageUtil.getMessage("ChecqueBook.productid")))))) {
                webServiceVO.setResponseCode("00");
                webServiceVO.setResponseCodeDescription("SuccessFull");
            } else {
                logger.info("[FonePayManagerImpl.makeChecqueBookStatus] Trx Code Not Found: ");
                webServiceVO.setResponseCode("10");
                webServiceVO.setResponseCodeDescription("Trx Code Not Found ");
            }
        } else {
            logger.info("[FonePayManagerImpl.makeChecqueBookStatus] Trx Code Not Found: ");
            webServiceVO.setResponseCode("10");
            webServiceVO.setResponseCodeDescription("Trx Code Not Found");
        }

        return webServiceVO;
    }

    @Override
    public WebServiceVO makeTransactionReversal(WebServiceVO webServiceVO) throws Exception {
        WorkFlowWrapper wrapper = new WorkFlowWrapperImpl();
        Long reasonId, category;
        String mobileNo, trxId, amount;
        AppUserModel appUserModel = new AppUserModel();
        AppUserModel appUserModel1 = new AppUserModel();

        OLAVO olaVO = new OLAVO();
        List<OLAInfo> debitList = new ArrayList<OLAInfo>();
        List<OLAInfo> creditList = new ArrayList<OLAInfo>();
        List<LedgerModel> ledgerModelList = new ArrayList<>();
        LedgerModel ledgerModel = new LedgerModel();

        BaseWrapper baseWrapper = new BaseWrapperImpl();
        TransactionDetailMasterModel txDetailMaster = new TransactionDetailMasterModel();
        txDetailMaster.setTransactionCode(webServiceVO.getMicrobankTransactionCode());
//        if (webServiceVO.getPaymentType().equals(FonePayConstants.WEB_SERVICE_PAYMENT)) {
//            if (webServiceVO.getChannelId().equals(FonePayConstants.APIGEE_CHANNEL)) {
//                txDetailMaster.setProductId(ProductConstantsInterface.APIGEE_PAYMENT);
//            } else {
//                txDetailMaster.setProductId(ProductConstantsInterface.WEB_SERVICE_PAYMENT);
//            }
//        } else if (webServiceVO.getPaymentType().equals(FonePayConstants.VIRTUAL_CARD_PAYMENT)) {
//            txDetailMaster.setProductId(ProductConstantsInterface.VIRTUAL_CARD_PAYMENT);
//        } else {
//            txDetailMaster.setProductId(ProductConstantsInterface.FONEPAY_AGENT_PAYMENT);
//        }

        baseWrapper.setBasePersistableModel(txDetailMaster);
        baseWrapper = transactionDetailMasterManager.loadAndLockTransactionDetailMasterModel(baseWrapper);
        txDetailMaster = (TransactionDetailMasterModel) baseWrapper.getBasePersistableModel();
        if (txDetailMaster.getPk() != null) {
            if (txDetailMaster.getSupProcessingStatusId().equals(SupplierProcessingStatusConstants.COMPLETED)) {

//                if (txDetailMaster.getSaleMobileNo() != null || txDetailMaster.getRecipientMobileNo() != null) {

//
//                    appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(txDetailMaster.getSaleMobileNo());
//                    appUserModel1 = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(txDetailMaster.getRecipientMobileNo());
//
//                    if (appUserModel == null||appUserModel1==null) {
//                        return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.ACCOUNT_CLOSED);
//                    }
//                }

                if (txDetailMaster.getSaleMobileNo() != null) {
                    appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(txDetailMaster.getSaleMobileNo());
                    if (appUserModel == null || appUserModel1 == null) {
                        return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.ACCOUNT_CLOSED);
                    }
                }
                if (txDetailMaster.getRecipientMobileNo() != null) {
                    appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(txDetailMaster.getRecipientMobileNo());
                    if (appUserModel == null || appUserModel1 == null) {
                        return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.ACCOUNT_CLOSED);
                    }
                }

                mobileNo = txDetailMaster.getSaleMobileNo();
                trxId = txDetailMaster.getTransactionCode();
                amount = txDetailMaster.getTransactionAmount().toString();
                wrapper.setTransactionDetailMasterModel(txDetailMaster);
                wrapper.setProductModel(new ProductModel(txDetailMaster.getProductId(), txDetailMaster.getProductName()));
                TransactionCodeModel transactionCodeModel = new TransactionCodeModel(txDetailMaster.getTransactionCode());
                transactionCodeModel.setTransactionCodeId(txDetailMaster.getTransactionCodeId());
                wrapper.setTransactionCodeModel(transactionCodeModel);


                SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
                TransactionModel trxModel = new TransactionModel();
                trxModel.setTransactionId(txDetailMaster.getTransactionId());
                searchBaseWrapper.setBasePersistableModel(trxModel);
                searchBaseWrapper = this.transactionModuleManager.loadTransaction(searchBaseWrapper);

                if (searchBaseWrapper.getBasePersistableModel() != null) {
                    trxModel = (TransactionModel) searchBaseWrapper.getBasePersistableModel();
                    wrapper.setTransactionModel(trxModel);
                } else {
                    logger.error("Unable to load Transaction against transaction.transaction_id:" + txDetailMaster.getTransactionId());
                    throw new FrameworkCheckedException("Unable to load Transaction Details");
                }
                SwitchWrapper switchWrapper = new SwitchWrapperImpl();
                switchWrapper.setWorkFlowWrapper(wrapper);
                reasonId = ReasonConstants.REVERSAL;
                category = TransactionConstantsInterface.AUTO_REVERSAL_CATEGORY_ID;
                ledgerModel.setMicrobankTransactionCode(txDetailMaster.getTransactionCode());
                ledgerModelList = this.ledgerDAO.findByExample(ledgerModel).getResultsetList();
                boolean isDebit = false;
                for (LedgerModel l1 : ledgerModelList) {
                    BBAccountsViewModel bbAccountsViewModel = new BBAccountsViewModel();
                    OLAInfo olaInfo = new OLAInfo();

                    olaInfo.setReasonId(reasonId);
                    olaInfo.setMicrobankTransactionCode(txDetailMaster.getTransactionCode());
                    olaInfo.setIsAgent(false);
                    isDebit = false;
                    if (l1.getToAccountId().equals(PoolAccountConstantsInterface.LEDGER_SETTLEMENT_ACCOUNT_ID) || l1.getToAccountId().equals(PoolAccountConstantsInterface.UNCLAIMED_C2C_SUNDARY_ACCOUNT)) {
                        isDebit = false;
                        bbAccountsViewModel.setAccountId(l1.getAccountId());
                        bbAccountsViewModel = bbAccountsViewDao.findByPrimaryKey(l1.getAccountId());
                        olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
                    } else {
                        isDebit = true;
                        bbAccountsViewModel.setAccountId(l1.getToAccountId());
                        bbAccountsViewModel = bbAccountsViewDao.findByPrimaryKey(l1.getToAccountId());
                        olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
                    }
                    olaInfo.setCustomerAccountTypeId(bbAccountsViewModel.getAccountTypeId());
                    String s = EncryptionUtil.decryptAccountNo(bbAccountsViewModel.getAccountNumber());
                    olaInfo.setPayingAccNo(s);
                    olaInfo.setBalance(l1.getTransactionAmount());
                    if (isDebit) {
                        debitList.add(olaInfo);
                    } else {
                        creditList.add(olaInfo);
                    }

                }
                olaVO.setDebitAccountList(debitList);
                olaVO.setCreditAccountList(creditList);
                olaVO.setCategory(category);
                switchWrapper.setBankId(BankConstantsInterface.OLA_BANK_ID);
                switchWrapper.setOlavo(olaVO);
                switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT.longValue());
                switchWrapper.setSkipPostedTrxEntry(false);
                ThreadLocalAppUser.setAppUserModel(UserUtils.getCurrentUser());

                switchWrapper = this.switchController.debitCreditAccount(switchWrapper);
                txDetailMaster.setSupProcessingStatusId(SupplierProcessingStatusConstants.REVERSE_COMPLETED);
                txDetailMaster.setProcessingStatusName(SupplierProcessingStatusConstants.REVERSE_COMPLETED_NAME);
                txDetailMaster.setUpdatedOn(new Date());
                transactionDetailMasterDAO.saveOrUpdate(txDetailMaster);

                //Settlement Of Accounts

                Long fromAccountInfoId = null;
                Long toAccountInfoId = null;

                //TransactionDetailMasterModel txDetailMaster = switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel();
                Long transactionId = txDetailMaster.getTransactionId();
                Long productId = txDetailMaster.getProductId();
                StakeholderBankInfoModel model = new StakeholderBankInfoModel();

                for (OLAInfo debitOlaInfo : switchWrapper.getOlavo().getDebitAccountList()) {
                    if (debitOlaInfo.getCustomerAccountTypeId().longValue() == 3) {
                        model = new StakeholderBankInfoModel();
                        model.setAccountNo(debitOlaInfo.getPayingAccNo());
                        model = stakeholderBankInfoManager.loadStakeholderBankInfoModel(model);
                        fromAccountInfoId = null;
                        if (null != model) {
                            fromAccountInfoId = model.getOfSettlementStakeholderBankInfoModelId();
                        }
                        if (null != fromAccountInfoId) {
                            prepareAndSaveSettlementTransaction(transactionId, productId, debitOlaInfo.getBalance(), fromAccountInfoId, PoolAccountConstantsInterface.JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID, null, false);
                        } else {
                            throw new FrameworkCheckedException("OF Settlement Account Not Found against account No:" + debitOlaInfo.getPayingAccNo());
                        }
                    } else {
                        fromAccountInfoId = settlementManager.getStakeholderBankInfoId(debitOlaInfo.getCustomerAccountTypeId());
                        prepareAndSaveSettlementTransaction(transactionId, productId, debitOlaInfo.getBalance(), fromAccountInfoId, PoolAccountConstantsInterface.JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID, debitOlaInfo.getIsAgent(), true);
                    }
                }

                for (OLAInfo creditOlaInfo : switchWrapper.getOlavo().getCreditAccountList()) {
                    if (creditOlaInfo.getCustomerAccountTypeId().longValue() == 3) {
                        model = new StakeholderBankInfoModel();
                        model.setAccountNo(creditOlaInfo.getPayingAccNo());
                        model = stakeholderBankInfoManager.loadStakeholderBankInfoModel(model);
                        toAccountInfoId = null;
                        if (null != model) {
                            toAccountInfoId = model.getOfSettlementStakeholderBankInfoModelId();
                        }
                        if (null != toAccountInfoId) {
                            prepareAndSaveSettlementTransaction(transactionId, productId, creditOlaInfo.getBalance(), PoolAccountConstantsInterface.JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID, toAccountInfoId, null, false);
                        } else {
                            throw new FrameworkCheckedException("OF Settlement Account Not Found against account No:" + creditOlaInfo.getPayingAccNo());
                        }
                    } else {
                        toAccountInfoId = settlementManager.getStakeholderBankInfoId(creditOlaInfo.getCustomerAccountTypeId());
                        prepareAndSaveSettlementTransaction(transactionId, productId, creditOlaInfo.getBalance(), PoolAccountConstantsInterface.JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID, toAccountInfoId, creditOlaInfo.getIsAgent(), false);
                    }
                }
                if ((MessageUtil.getMessage("WEBSERVICE.SENDSMS.CHANNEL").contains(webServiceVO.getChannelId())) && !(txDetailMaster.getProductId().equals(Long.parseLong(MessageUtil.getMessage("Z-Spin.product.id"))))) {
                    DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy");
                    DateTimeFormatter tf = DateTimeFormat.forPattern("h:mm a");
                    String smsText = MessageUtil.getMessage("trxreversal.sms", new String[]{
                            trxId,
                            dtf.print(new DateTime()),
                            tf.print(new LocalTime()),});
                    NovaAlertMessage novaAlertMessage=new NovaAlertMessage(mobileNo,smsText,"","","","");
                    SmsMessage smsMessage = new SmsMessage(mobileNo, smsText);
                    smsSender.send(smsMessage);
                    smsSender.alertNovaMessage(novaAlertMessage);

                } else if ((webServiceVO.getChannelId().equals("NOVA")) && !(txDetailMaster.getProductId().equals(Long.parseLong(MessageUtil.getMessage("Z-spin.Product.Id"))))) {
                    String smsText = MessageUtil.getMessage("trxreversal.sender.sms", new String[]{
                            trxId,
                            String.valueOf(wrapper.getTransactionDetailMasterModel().getTransactionAmount()),
                            txDetailMaster.getSaleMobileNo(),
                            txDetailMaster.getProductName(),
                            PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),
                            PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),

                    });
                    NovaAlertMessage novaAlertMessage=new NovaAlertMessage(mobileNo,smsText,"","","","");
                    SmsMessage smsMessage = new SmsMessage(mobileNo, smsText);
                    smsSender.send(smsMessage);
                    smsSender.alertNovaMessage(novaAlertMessage);
                }

                String channelId = MessageUtil.getMessage("merchantCamping.channel.ids");
                List<String> channelIds = Arrays.asList(channelId.split("\\s*,\\s*"));
                if (webServiceVO.getChannelId() != null && channelIds.contains(webServiceVO.getChannelId())) {
                    I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
                    I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
                    requestVO = new I8SBSwitchControllerRequestVO();
                    responseVO = new I8SBSwitchControllerResponseVO();
                    requestVO = ESBAdapter.prepareMerchantCampingRequest(I8SBConstants.RequestType_TransactionStatus);
                    requestVO.setUserId(String.valueOf(appUserModel.getAppUserId()));
                    requestVO.setTransactionCode(txDetailMaster.getTransactionCode());
                    requestVO.setAvailableBalance("");
                    requestVO.setMobileNumber(appUserModel.getMobileNo());
                    requestVO.setTransactionDate(String.valueOf(new Date()));
                    requestVO.setRRN(txDetailMaster.getFonepayTransactionCode());
                    requestVO.setSTAN(txDetailMaster.getStan());
                    requestVO.setStatus("R");
                    requestVO.setTransactionAmount(String.valueOf(txDetailMaster.getTransactionAmount()));
                    requestVO.setTransactionType("M");
                    SwitchWrapper sWrapper = new SwitchWrapperImpl();
                    sWrapper = new SwitchWrapperImpl();
                    sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                    sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
                    sWrapper = esbAdapter.makeI8SBCall(sWrapper);
                    ESBAdapter.processI8sbResponseCode(sWrapper.getI8SBSwitchControllerResponseVO(), false);
                    responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();
                }
            } else if (txDetailMaster.getSupProcessingStatusId().equals(SupplierProcessingStatusConstants.REVERSE_COMPLETED)) {
                logger.info("[FonePayManagerImpl.makeTransactionReversal] Reversal Successful: ");

                this.transactionDetailMasterManager.updateTransactionDetailMaster(txDetailMaster);
                webServiceVO.setResponseCode("10");
                webServiceVO.setResponseCodeDescription("Transaction Already Reversed");
            } else if (txDetailMaster.getSupProcessingStatusId().equals(SupplierProcessingStatusConstants.FAILED)) {
                logger.info("[FonePayManagerImpl.makeTransactionReversal] Reversal Successful: ");

                this.transactionDetailMasterManager.updateTransactionDetailMaster(txDetailMaster);
                webServiceVO.setResponseCode("10");
                webServiceVO.setResponseCodeDescription("Transaction Is In Failed Status Thats Why Not Reversed");
            } else if (txDetailMaster.getSupProcessingStatusId().equals(SupplierProcessingStatusConstants.PROCESSING)) {
                logger.info("[FonePayManagerImpl.makeTransactionReversal] Reversal Successful: ");

                this.transactionDetailMasterManager.updateTransactionDetailMaster(txDetailMaster);
                webServiceVO.setResponseCode("10");
                webServiceVO.setResponseCodeDescription("Transaction Is In Processing Status Thats Why Not Reversed");
            } else if (txDetailMaster.getSupProcessingStatusId().equals(SupplierProcessingStatusConstants.UNCLAIMED)) {
                logger.info("[FonePayManagerImpl.makeTransactionReversal] Reversal Successful: ");

                this.transactionDetailMasterManager.updateTransactionDetailMaster(txDetailMaster);
                webServiceVO.setResponseCode("10");
                webServiceVO.setResponseCodeDescription("Transaction Is In Unclaimed Status Thats Why Not Reversed");
            } else if (txDetailMaster.getSupProcessingStatusId().equals(SupplierProcessingStatusConstants.PENDING_ACTION_AUTH)) {
                logger.info("[FonePayManagerImpl.makeTransactionReversal] Reversal Successful: ");

                this.transactionDetailMasterManager.updateTransactionDetailMaster(txDetailMaster);
                webServiceVO.setResponseCode("10");
                webServiceVO.setResponseCodeDescription("Transaction Is Pending For Authorization Status Thats Why Not Reversed");
            } else if (txDetailMaster.getSupProcessingStatusId().equals(SupplierProcessingStatusConstants.REVERSED)) {
                logger.info("[FonePayManagerImpl.makeTransactionReversal] Reversal Successful: ");

                this.transactionDetailMasterManager.updateTransactionDetailMaster(txDetailMaster);
                webServiceVO.setResponseCode("10");
                webServiceVO.setResponseCodeDescription("Transaction Is In Failed Status Thats Why Not Reversed");
            } else if (txDetailMaster.getSupProcessingStatusId().equals(SupplierProcessingStatusConstants.IN_PROGRESS)) {
                logger.info("[FonePayManagerImpl.makeTransactionReversal] Reversal Successful: ");

                this.transactionDetailMasterManager.updateTransactionDetailMaster(txDetailMaster);
                webServiceVO.setResponseCode("10");
                webServiceVO.setResponseCodeDescription("Transaction Is In Progress");
            }

        } else {
            logger.info("[FonePayManagerImpl.makeTransactionReversal] Trx Code Not Found: ");
            webServiceVO.setResponseCode("10");
            webServiceVO.setResponseCodeDescription("Trx Code Not Found For Payment Reversal");
        }
        webServiceVO.setMicrobankTransactionCode(txDetailMaster.getTransactionCode());
        return webServiceVO;
    }


    @Override
    public WebServiceVO makeDebitPaymentTransactionReversal(WebServiceVO webServiceVO) throws Exception {
        WorkFlowWrapper wrapper = new WorkFlowWrapperImpl();
        Long reasonId, category;
        String mobileNo, trxId, amount;
        AppUserModel appUserModel = new AppUserModel();
        Date dt = new Date();
        AppUserModel appUserModel1 = new AppUserModel();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddhhmmss");
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");

        if (webServiceVO.getReserved5() != null && webServiceVO.getReserved5().equals("1")) {
            dt = formatter2.parse(webServiceVO.getDateTime());
        } else {
            dt = formatter.parse(webServiceVO.getDateTime());
        }
        OLAVO olaVO = new OLAVO();
        List<OLAInfo> debitList = new ArrayList<OLAInfo>();
        List<OLAInfo> creditList = new ArrayList<OLAInfo>();
        List<LedgerModel> ledgerModelList = new ArrayList<>();
        LedgerModel ledgerModel = new LedgerModel();

        BaseWrapper baseWrapper = new BaseWrapperImpl();
        TransactionDetailMasterModel txDetailMaster = new TransactionDetailMasterModel();
        txDetailMaster.setTransactionCode(webServiceVO.getMicrobankTransactionCode());
//        if (webServiceVO.getPaymentType().equals(FonePayConstants.WEB_SERVICE_PAYMENT)) {
//            if (webServiceVO.getChannelId().equals(FonePayConstants.APIGEE_CHANNEL)) {
//                txDetailMaster.setProductId(ProductConstantsInterface.APIGEE_PAYMENT);
//            } else {
//                txDetailMaster.setProductId(ProductConstantsInterface.WEB_SERVICE_PAYMENT);
//            }
//        } else if (webServiceVO.getPaymentType().equals(FonePayConstants.VIRTUAL_CARD_PAYMENT)) {
//            txDetailMaster.setProductId(ProductConstantsInterface.VIRTUAL_CARD_PAYMENT);
//        } else {
//            txDetailMaster.setProductId(ProductConstantsInterface.FONEPAY_AGENT_PAYMENT);
//        }

        baseWrapper.setBasePersistableModel(txDetailMaster);
        baseWrapper = transactionDetailMasterManager.loadAndLockTransactionDetailMasterModel(baseWrapper);
        txDetailMaster = (TransactionDetailMasterModel) baseWrapper.getBasePersistableModel();
        if (txDetailMaster.getPk() != null) {
            if (txDetailMaster.getSupProcessingStatusId().equals(SupplierProcessingStatusConstants.COMPLETED)) {

//                if (txDetailMaster.getSaleMobileNo() != null || txDetailMaster.getRecipientMobileNo() != null) {

//
//                    appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(txDetailMaster.getSaleMobileNo());
//                    appUserModel1 = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(txDetailMaster.getRecipientMobileNo());
//
//                    if (appUserModel == null||appUserModel1==null) {
//                        return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.ACCOUNT_CLOSED);
//                    }
//                }

//                if (txDetailMaster.getSaleMobileNo() != null) {
//                    appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(txDetailMaster.getSaleMobileNo());
//                    if (appUserModel == null || appUserModel1 == null) {
//                        return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.ACCOUNT_CLOSED);
//                    }
//                }
//                if (txDetailMaster.getRecipientMobileNo() != null) {
//                    appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(txDetailMaster.getRecipientMobileNo());
//                    if (appUserModel == null || appUserModel1 == null) {
//                        return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.ACCOUNT_CLOSED);
//                    }
//                }

                mobileNo = txDetailMaster.getSaleMobileNo();
                trxId = txDetailMaster.getTransactionCode();
                amount = txDetailMaster.getTransactionAmount().toString();
                wrapper.setTransactionDetailMasterModel(txDetailMaster);
                wrapper.setProductModel(new ProductModel(txDetailMaster.getProductId(), txDetailMaster.getProductName()));
                TransactionCodeModel transactionCodeModel = new TransactionCodeModel(txDetailMaster.getTransactionCode());
                transactionCodeModel.setTransactionCodeId(txDetailMaster.getTransactionCodeId());
                wrapper.setTransactionCodeModel(transactionCodeModel);


                SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
                TransactionModel trxModel = new TransactionModel();
                trxModel.setTransactionId(txDetailMaster.getTransactionId());
                searchBaseWrapper.setBasePersistableModel(trxModel);
                searchBaseWrapper = this.transactionModuleManager.loadTransaction(searchBaseWrapper);

                if (searchBaseWrapper.getBasePersistableModel() != null) {
                    trxModel = (TransactionModel) searchBaseWrapper.getBasePersistableModel();
                    wrapper.setTransactionModel(trxModel);
                } else {
                    logger.error("Unable to load Transaction against transaction.transaction_id:" + txDetailMaster.getTransactionId());
                    throw new FrameworkCheckedException("Unable to load Transaction Details");
                }
                SwitchWrapper switchWrapper = new SwitchWrapperImpl();
                switchWrapper.setWorkFlowWrapper(wrapper);
                reasonId = ReasonConstants.REVERSAL;
                category = TransactionConstantsInterface.AUTO_REVERSAL_CATEGORY_ID;
                ledgerModel.setMicrobankTransactionCode(txDetailMaster.getTransactionCode());
                ledgerModelList = this.ledgerDAO.findByExample(ledgerModel).getResultsetList();
                boolean isDebit = false;
                for (LedgerModel l1 : ledgerModelList) {
                    BBAccountsViewModel bbAccountsViewModel = new BBAccountsViewModel();
                    OLAInfo olaInfo = new OLAInfo();

                    olaInfo.setReasonId(reasonId);
                    olaInfo.setMicrobankTransactionCode(txDetailMaster.getTransactionCode());
                    olaInfo.setIsAgent(false);
                    isDebit = false;
                    if (l1.getToAccountId().equals(PoolAccountConstantsInterface.LEDGER_SETTLEMENT_ACCOUNT_ID) || l1.getToAccountId().equals(PoolAccountConstantsInterface.UNCLAIMED_C2C_SUNDARY_ACCOUNT)) {
                        isDebit = false;
                        bbAccountsViewModel.setAccountId(l1.getAccountId());
                        bbAccountsViewModel = bbAccountsViewDao.findByPrimaryKey(l1.getAccountId());
                        olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
                    } else {
                        isDebit = true;
                        bbAccountsViewModel.setAccountId(l1.getToAccountId());
                        bbAccountsViewModel = bbAccountsViewDao.findByPrimaryKey(l1.getToAccountId());
                        olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
                    }
                    olaInfo.setCustomerAccountTypeId(bbAccountsViewModel.getAccountTypeId());
                    String s = EncryptionUtil.decryptAccountNo(bbAccountsViewModel.getAccountNumber());
                    olaInfo.setPayingAccNo(s);
                    olaInfo.setBalance(l1.getTransactionAmount());
                    if (isDebit) {
                        debitList.add(olaInfo);
                    } else {
                        creditList.add(olaInfo);
                    }

                }
                olaVO.setDebitAccountList(debitList);
                olaVO.setCreditAccountList(creditList);
                olaVO.setCategory(category);
                switchWrapper.setBankId(BankConstantsInterface.OLA_BANK_ID);
                switchWrapper.setOlavo(olaVO);
                switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT.longValue());
                switchWrapper.setSkipPostedTrxEntry(false);
                ThreadLocalAppUser.setAppUserModel(UserUtils.getCurrentUser());

                switchWrapper = this.switchController.debitCreditAccount(switchWrapper);
                txDetailMaster.setSupProcessingStatusId(SupplierProcessingStatusConstants.REVERSE_COMPLETED);
                txDetailMaster.setProcessingStatusName(SupplierProcessingStatusConstants.REVERSE_COMPLETED_NAME);
                txDetailMaster.setUpdatedOn(new Date());
                transactionDetailMasterDAO.saveOrUpdate(txDetailMaster);

                transactionReversalManager.updateIBFTStatus(webServiceVO.getReserved2(),
                        dt,
                        PortalConstants.IBFT_STATUS_SUCCESS,
                        wrapper.getTransactionCodeModel().getCode());


                //Settlement Of Accounts

                Long fromAccountInfoId = null;
                Long toAccountInfoId = null;

                //TransactionDetailMasterModel txDetailMaster = switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel();
                Long transactionId = txDetailMaster.getTransactionId();
                Long productId = txDetailMaster.getProductId();
                StakeholderBankInfoModel model = new StakeholderBankInfoModel();

                for (OLAInfo debitOlaInfo : switchWrapper.getOlavo().getDebitAccountList()) {
                    if (debitOlaInfo.getCustomerAccountTypeId().longValue() == 3) {
                        model = new StakeholderBankInfoModel();
                        model.setAccountNo(debitOlaInfo.getPayingAccNo());
                        model = stakeholderBankInfoManager.loadStakeholderBankInfoModel(model);
                        fromAccountInfoId = null;
                        if (null != model) {
                            fromAccountInfoId = model.getOfSettlementStakeholderBankInfoModelId();
                        }
                        if (null != fromAccountInfoId) {
                            prepareAndSaveSettlementTransaction(transactionId, productId, debitOlaInfo.getBalance(), fromAccountInfoId, PoolAccountConstantsInterface.JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID, null, false);
                        } else {
                            throw new FrameworkCheckedException("OF Settlement Account Not Found against account No:" + debitOlaInfo.getPayingAccNo());
                        }
                    } else {
                        fromAccountInfoId = settlementManager.getStakeholderBankInfoId(debitOlaInfo.getCustomerAccountTypeId());
                        prepareAndSaveSettlementTransaction(transactionId, productId, debitOlaInfo.getBalance(), fromAccountInfoId, PoolAccountConstantsInterface.JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID, debitOlaInfo.getIsAgent(), true);
                    }
                }

                for (OLAInfo creditOlaInfo : switchWrapper.getOlavo().getCreditAccountList()) {
                    if (creditOlaInfo.getCustomerAccountTypeId().longValue() == 3) {
                        model = new StakeholderBankInfoModel();
                        model.setAccountNo(creditOlaInfo.getPayingAccNo());
                        model = stakeholderBankInfoManager.loadStakeholderBankInfoModel(model);
                        toAccountInfoId = null;
                        if (null != model) {
                            toAccountInfoId = model.getOfSettlementStakeholderBankInfoModelId();
                        }
                        if (null != toAccountInfoId) {
                            prepareAndSaveSettlementTransaction(transactionId, productId, creditOlaInfo.getBalance(), PoolAccountConstantsInterface.JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID, toAccountInfoId, null, false);
                        } else {
                            throw new FrameworkCheckedException("OF Settlement Account Not Found against account No:" + creditOlaInfo.getPayingAccNo());
                        }
                    } else {
                        toAccountInfoId = settlementManager.getStakeholderBankInfoId(creditOlaInfo.getCustomerAccountTypeId());
                        prepareAndSaveSettlementTransaction(transactionId, productId, creditOlaInfo.getBalance(), PoolAccountConstantsInterface.JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID, toAccountInfoId, creditOlaInfo.getIsAgent(), false);
                    }
                }

                DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy");
                DateTimeFormatter tf = DateTimeFormat.forPattern("h:mm a");
                String smsText = MessageUtil.getMessage("trxreversal.sms", new String[]{
                        trxId,
                        dtf.print(new DateTime()),
                        tf.print(new LocalTime()),});
                SmsMessage smsMessage = new SmsMessage(mobileNo, smsText);
                smsSender.send(smsMessage);

                String channelId = MessageUtil.getMessage("merchantCamping.channel.ids");
                List<String> channelIds = Arrays.asList(channelId.split("\\s*,\\s*"));
                if (webServiceVO.getChannelId() != null && channelIds.contains(webServiceVO.getChannelId())) {
                    I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
                    I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
                    requestVO = new I8SBSwitchControllerRequestVO();
                    responseVO = new I8SBSwitchControllerResponseVO();
                    requestVO = ESBAdapter.prepareMerchantCampingRequest(I8SBConstants.RequestType_TransactionStatus);
                    requestVO.setUserId(String.valueOf(appUserModel.getAppUserId()));
                    requestVO.setTransactionCode(txDetailMaster.getTransactionCode());
                    requestVO.setAvailableBalance("");
                    requestVO.setMobileNumber(appUserModel.getMobileNo());
                    requestVO.setTransactionDate(String.valueOf(new Date()));
                    requestVO.setRRN(txDetailMaster.getFonepayTransactionCode());
                    requestVO.setSTAN(txDetailMaster.getStan());
                    requestVO.setStatus("R");
                    requestVO.setTransactionAmount(String.valueOf(txDetailMaster.getTransactionAmount()));
                    requestVO.setTransactionType("M");
                    SwitchWrapper sWrapper = new SwitchWrapperImpl();
                    sWrapper = new SwitchWrapperImpl();
                    sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                    sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
                    sWrapper = esbAdapter.makeI8SBCall(sWrapper);
                    ESBAdapter.processI8sbResponseCode(sWrapper.getI8SBSwitchControllerResponseVO(), false);
                    responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();
                }

            } else if (txDetailMaster.getSupProcessingStatusId().equals(SupplierProcessingStatusConstants.REVERSE_COMPLETED)) {
                logger.info("[FonePayManagerImpl.makeTransactionReversal] Reversal Successful: ");

                this.transactionDetailMasterManager.updateTransactionDetailMaster(txDetailMaster);
                webServiceVO.setResponseCode("10");
                webServiceVO.setResponseCodeDescription("Transaction Already Reversed");
            } else if (txDetailMaster.getSupProcessingStatusId().equals(SupplierProcessingStatusConstants.FAILED)) {
                logger.info("[FonePayManagerImpl.makeTransactionReversal] Reversal Successful: ");

                this.transactionDetailMasterManager.updateTransactionDetailMaster(txDetailMaster);
                webServiceVO.setResponseCode("10");
                webServiceVO.setResponseCodeDescription("Transaction Is In Failed Status Thats Why Not Reversed");
            } else if (txDetailMaster.getSupProcessingStatusId().equals(SupplierProcessingStatusConstants.PROCESSING)) {
                logger.info("[FonePayManagerImpl.makeTransactionReversal] Reversal Successful: ");

                this.transactionDetailMasterManager.updateTransactionDetailMaster(txDetailMaster);
                webServiceVO.setResponseCode("10");
                webServiceVO.setResponseCodeDescription("Transaction Is In Processing Status Thats Why Not Reversed");
            } else if (txDetailMaster.getSupProcessingStatusId().equals(SupplierProcessingStatusConstants.UNCLAIMED)) {
                logger.info("[FonePayManagerImpl.makeTransactionReversal] Reversal Successful: ");

                this.transactionDetailMasterManager.updateTransactionDetailMaster(txDetailMaster);
                webServiceVO.setResponseCode("10");
                webServiceVO.setResponseCodeDescription("Transaction Is In Unclaimed Status Thats Why Not Reversed");
            } else if (txDetailMaster.getSupProcessingStatusId().equals(SupplierProcessingStatusConstants.PENDING_ACTION_AUTH)) {
                logger.info("[FonePayManagerImpl.makeTransactionReversal] Reversal Successful: ");

                this.transactionDetailMasterManager.updateTransactionDetailMaster(txDetailMaster);
                webServiceVO.setResponseCode("10");
                webServiceVO.setResponseCodeDescription("Transaction Is Pending For Authorization Status Thats Why Not Reversed");
            } else if (txDetailMaster.getSupProcessingStatusId().equals(SupplierProcessingStatusConstants.REVERSED)) {
                logger.info("[FonePayManagerImpl.makeTransactionReversal] Reversal Successful: ");

                this.transactionDetailMasterManager.updateTransactionDetailMaster(txDetailMaster);
                webServiceVO.setResponseCode("10");
                webServiceVO.setResponseCodeDescription("Transaction Is In Failed Status Thats Why Not Reversed");
            } else if (txDetailMaster.getSupProcessingStatusId().equals(SupplierProcessingStatusConstants.IN_PROGRESS)) {
                logger.info("[FonePayManagerImpl.makeTransactionReversal] Reversal Successful: ");

                this.transactionDetailMasterManager.updateTransactionDetailMaster(txDetailMaster);
                webServiceVO.setResponseCode("10");
                webServiceVO.setResponseCodeDescription("Transaction Is In Progress");
            }

        } else {
            logger.info("[FonePayManagerImpl.makeTransactionReversal] Trx Code Not Found: ");
            throw new FrameworkCheckedException("Unable to load Transaction Details");
        }
        webServiceVO.setMicrobankTransactionCode(txDetailMaster.getTransactionCode());
        return webServiceVO;
    }


    @Override
    public WebServiceVO updateCardInfo(WebServiceVO webServiceVO) throws Exception {
        VirtualCardModel virtualCardModel = new VirtualCardModel();
        try {
            virtualCardModel.setCardNo(webServiceVO.getCardNo());
            virtualCardModel.setMobileNo(webServiceVO.getMobileNo());
            virtualCardModel.setDeleted(false);
            List<VirtualCardModel> virtualCardModelList = new ArrayList<>();
            virtualCardModelList = this.virtualCardHibernateDAO.findByExample(virtualCardModel).getResultsetList();
            if (virtualCardModelList == null || virtualCardModelList.size() == 0) {
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CARD_NOT_FOUND);
            } else {
                virtualCardModel = virtualCardModelList.get(0);
                if (webServiceVO.getTransactionType().equals(FonePayConstants.CARD_DEACTIVATE)) {
                    virtualCardModel.setIsBlocked(true);
                } else if (webServiceVO.getTransactionType().equals(FonePayConstants.CARD_ACTIVATE)) {
                    virtualCardModel.setIsBlocked(false);
                } else {
                    virtualCardModel.setDeleted(true);
                }
                this.virtualCardHibernateDAO.saveOrUpdate(virtualCardModel);
                webServiceVO.setResponseCode("00");
                webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
            }
        } catch (Exception e) {
            logger.error("[FonePayManagerImpl.updateCardInfo] Exception occured: " + e.getMessage(), e);
            webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR);
        }
        return webServiceVO;
    }

    private void prepareAndSaveSettlementTransaction(Long transactionId, Long productId, Double amount, Long fromAccountInfoId, Long toAccountInfoId, Boolean isAgent, boolean debitPoolAcc) throws Exception {
        AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();
        SettlementTransactionModel settlementModel = new SettlementTransactionModel();
        settlementModel.setTransactionID(transactionId);
        settlementModel.setProductID(productId);
        settlementModel.setCreatedBy(appUserModel.getAppUserId());
        settlementModel.setUpdatedBy(appUserModel.getAppUserId());
        settlementModel.setCreatedOn(new Date());
        settlementModel.setUpdatedOn(new Date());
        settlementModel.setStatus(0L);
        settlementModel.setFromBankInfoID(fromAccountInfoId);
        settlementModel.setToBankInfoID(toAccountInfoId);
        settlementModel.setAmount(amount);

        this.settlementManager.saveSettlementTransactionModel(settlementModel);

        if (isAgent != null) {
            Long poolBankInfoId = null;

            settlementModel = new SettlementTransactionModel();
            if (isAgent) {
                poolBankInfoId = PoolAccountConstantsInterface.AGENT_POOL_ACCOUNT_ID;
            } else {
                poolBankInfoId = PoolAccountConstantsInterface.CUSTOMER_POOL_ACCOUNT_ID;
            }

            settlementModel.setTransactionID(transactionId);
            settlementModel.setProductID(productId);
            settlementModel.setCreatedBy(appUserModel.getAppUserId());
            settlementModel.setUpdatedBy(appUserModel.getAppUserId());
            settlementModel.setCreatedOn(new Date());
            settlementModel.setUpdatedOn(new Date());
            settlementModel.setStatus(0L);
            settlementModel.setFromBankInfoID((debitPoolAcc) ? poolBankInfoId : fromAccountInfoId);
            settlementModel.setToBankInfoID((!debitPoolAcc) ? poolBankInfoId : toAccountInfoId);
            settlementModel.setAmount(amount);
            this.settlementManager.saveSettlementTransactionModel(settlementModel);

        }
    }

    @Override
    public WebServiceVO makeCashIninquiry(WebServiceVO webServiceVO) throws Exception {
        webServiceVO = this.makevalidateCustomer(webServiceVO);
        if (webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE)) {
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE);
            baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, webServiceVO.getMobileNo());
            baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.CASH_DEPOSIT);
            baseWrapper.putObject(CommandFieldConstants.KEY_TX_AMOUNT, webServiceVO.getTransactionAmount());
            getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.CMD_WEB_SERVICE_INFO_COMMAND);


            String otp = CommonUtils.generateOneTimePin(5);
            logger.info("The plain otp is " + otp);
            String encryptedPin = EncoderUtils.encodeToSha(otp);
            this.createMiniTransactionModel(encryptedPin, webServiceVO.getMobileNo(), webServiceVO.getChannelId(), CommandFieldConstants.CASH_DEPOSIT_INFO_COMMAND);
            webServiceVO.setOtpPin(otp);

            this.sendOtpSms(otp, "Cash Deposit", webServiceVO.getMobileNo());

        }


        return webServiceVO;
    }

    @Override
    public FonePayLogModel saveFonePayIntegrationLogModelForDebitCardReq(MiddlewareMessageVO webServiceVO, String reqType) throws Exception {
        FonePayLogModel fonePayLogModel = new FonePayLogModel();
        Date date = new Date();
        Timestamp ts_now = new Timestamp(date.getTime());

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(Inclusion.NON_NULL);

        String jsonInString = mapper.writeValueAsString(webServiceVO);
        fonePayLogModel.setCnic(webServiceVO.getCnicNo());
        if (webServiceVO.getMobileNo() != null)
            fonePayLogModel.setMobile_no(webServiceVO.getMobileNo());
        fonePayLogModel.setRequestType(reqType);
        fonePayLogModel.setResponse_code(webServiceVO.getResponseCode());
        fonePayLogModel.setResponse_description(webServiceVO.getResponseDescription());
        fonePayLogModel.setRrn(webServiceVO.getRetrievalReferenceNumber());

        if (webServiceVO.getTransactionId() != null)
            fonePayLogModel.setTransactionId(webServiceVO.getTransactionId());

        fonePayLogModel.setCreated_on(ts_now);
        fonePayLogModel.setUpdated_on(ts_now);
        fonePayLogModel.setInput(jsonInString);

        fonePayLogModel = fonePayLogDAO.saveOrUpdate(fonePayLogModel);

        return fonePayLogModel;
    }

    @Override
    public void updateFonePayIntegrationLogModelForDebitCard(FonePayLogModel model, MiddlewareMessageVO webServiceVO) {
        if (model == null || model.getPrimaryKey() == null || webServiceVO == null) {
            return;
        }

        Date date = new Date();
        Timestamp ts_now = new Timestamp(date.getTime());

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(Inclusion.NON_NULL);

        String jsonOutString = null;
        try {
            jsonOutString = mapper.writeValueAsString(webServiceVO);
        } catch (Exception e) {
            logger.error("Problem occurred during converting  WebServiceVO into Json String.", e);
        }

        model.setResponse_code(webServiceVO.getResponseCode());
        model.setResponse_description(webServiceVO.getResponseDescription());

        model.setOutput(jsonOutString);
        model.setUpdated_on(ts_now);
        model.setTransactionId(webServiceVO.getMicrobankTransactionCode());

        try {
            fonePayLogDAO.saveOrUpdate(model);
        } catch (Exception exp) {
            logger.error("[updateFonePayIntegrationLogModel] Problem occurred during updating  : Response Code : " + webServiceVO.getResponseCode() + " in FonePayLogModel", exp);
        }
    }

    @Override
    public Boolean validateApiGeeRRN(WebServiceVO webServiceVO) throws FrameworkCheckedException {
        return fonePayLogDAO.validateApiGeeRRN(webServiceVO);
    }

    @Override
    public FonePayMessageVO validateMPINRetryCount(WebServiceVO webServiceVO, BaseWrapper pinWrapper) throws Exception {

        logger.debug("[Start of FonePayManagerImpl.validateMPINRetryCount()]");

        AppUserModel appUserModel = null;
        FonePayMessageVO fonePayMessageVO = new FonePayMessageVO();

        String mobileNo = webServiceVO.getAccountNo1();
        String channelId = webServiceVO.getChannelId();
        String cnic = webServiceVO.getCnicNo();

        Long appUserId = null;
        PinRetryModel pinRetryModel = new PinRetryModel();
        CustomList<PinRetryModel> pinRetryModelsList = null;
        appUserModel = ThreadLocalAppUser.getAppUserModel();
        appUserId = appUserModel.getAppUserId();

        try {
            // Verifying MPIN by calling VerifyPINCommand
            String resCode = getCommandManager().executeCommand(pinWrapper, CommandFieldConstants.CMD_VERIFY_PIN);

            if (appUserModel == null) {
                fonePayMessageVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
                fonePayMessageVO.setResponseCodeDescription(MessageUtil.getMessage("fonepay.error." + FonePayResponseCodes.GENERAL_ERROR));
                return fonePayMessageVO;
            }

            pinRetryModel.setAppUserId(appUserId);

            pinRetryModelsList = pinRetryDAO.findByExample(pinRetryModel, null);

            if (pinRetryModelsList.getResultsetList().size() > 0) {
                pinRetryModel = pinRetryModelsList.getResultsetList().get(0);
                if (pinRetryModel.getPinRetryCount() > 0) {
                    pinRetryModel.setPinRetryCount((long) 0);
                    pinRetryModel.setUpdated_on(new Date());

                    pinRetryDAO.saveOrUpdate(pinRetryModel);
                }

            }
            fonePayMessageVO.setResponseCode("00");
            fonePayMessageVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);


        } catch (CommandException cexp) {
            logger.error("CommandException occurred @ validateMPINRetryCount()...", cexp);

            if (appUserModel == null) {
                fonePayMessageVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
                fonePayMessageVO.setResponseCodeDescription(MessageUtil.getMessage("fonepay.error." + FonePayResponseCodes.GENERAL_ERROR));
                return fonePayMessageVO;
            }

            pinRetryModel.setAppUserId(appUserId);

            pinRetryModelsList = pinRetryDAO.findByExample(pinRetryModel);
            Boolean isMpinExpiredReset = false;
            Long count = (long) 0;
            Long retCount = (long) 0;
            Date lastRetryTime = new Date();

            if (pinRetryModelsList == null || pinRetryModelsList.getResultsetList().size() == 0) {
                pinRetryModel.setAppUserId(appUserId);
                pinRetryModel.setChannelId(channelId);
                pinRetryModel.setCnic(cnic);
                pinRetryModel.setIsBlocked(false);
                pinRetryModel.setMobileNO(mobileNo);
                pinRetryModel.setPinRefreshTime(FonePayConstants.MPIN_REFRESH_TIME.toString());
                pinRetryModel.setPinRetryCount((long) 1);
                pinRetryModel.setUpdated_on(new Date());

                pinRetryModel = pinRetryDAO.saveOrUpdate(pinRetryModel);

            } else {
                pinRetryModel = pinRetryModelsList.getResultsetList().get(0);
                count = pinRetryModel.getPinRetryCount();

                if (count > 0 && count < 3) {                                       //MPIN Refresh Time related code

                    lastRetryTime = pinRetryModel.getUpdated_on();
                    Long retryinMillis = lastRetryTime.getTime();
                    Long diffInMilis = System.currentTimeMillis() - retryinMillis;
                    double diffInMinutes = diffInMilis / (1000 * 60);
                    if (diffInMinutes > FonePayConstants.MPIN_REFRESH_TIME) {
                        count = (long) 1;
                        retCount = count;
                        isMpinExpiredReset = true;

                        pinRetryModel.setUpdated_on(new Date());
                        pinRetryModel.setIsBlocked(false);
                        pinRetryModel.setPinRetryCount(count);
                        pinRetryModel = pinRetryDAO.saveOrUpdate(pinRetryModel);
                    } else {

                    }

                }                                                               //**********************************

                if (isMpinExpiredReset == false) {
                    count = count + 1;
                    retCount = count;

                    if (count == 3) {
                        UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel(); // UserDeviceAccount is updated when MPIN Retry Limit EXHAUSTED
                        userDeviceAccountsModel.setAppUserId(appUserId);
                        userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);
                        SearchBaseWrapper sbWrapper = new SearchBaseWrapperImpl();
                        sbWrapper.setBasePersistableModel(userDeviceAccountsModel);

                        sbWrapper = getCommonCommandManager().loadUserDeviceAccounts(sbWrapper);
                        if (null != sbWrapper.getBasePersistableModel()) {
                            if (sbWrapper.getCustomList() != null && sbWrapper.getCustomList().getResultsetList().size() > 0) {
                                userDeviceAccountsModel = (UserDeviceAccountsModel) sbWrapper.getCustomList().getResultsetList().get(0);
                                userDeviceAccountsModel.setCredentialsExpired(true);
                                userDeviceAccountsModel.setAccountLocked(true);
                                BaseWrapper baseWrapper = new BaseWrapperImpl();
                                baseWrapper.setBasePersistableModel(userDeviceAccountsModel);

                                logger.info("[FonePayManagerImpl.validateMPINRetryCount()] Blocking User Device Account after retry count exhausted. Mobile No:" + mobileNo + " and retry count is " + count);

                                getCommonCommandManager().updateUserDeviceAccounts(baseWrapper);

                                count = (long) 0;
                                pinRetryModel.setIsBlocked(true);
                                pinRetryModel.setPinRetryCount(count);
                                pinRetryModel.setUpdated_on(new Date());

                                pinRetryModel = pinRetryDAO.saveOrUpdate(pinRetryModel);

                            } else {
                                logger.error("[FonePayManagerImpl.validateMPINRetryCount()] No Entry found in User Device Accounts against Mobile No: " + mobileNo);
                            }
                        } else {
                            logger.error("[FonePayManagerImpl.validateMPINRetryCount()] No Entry found in User Device Accounts against Mobile No: " + mobileNo);
                        }                                                                                          //*******************************************
                    } else if (count < 3) {
                        pinRetryModel.setPinRetryCount(count);
                        pinRetryModel.setIsBlocked(false);
                        pinRetryModel.setUpdated_on(new Date());
                        pinRetryModel = pinRetryDAO.saveOrUpdate(pinRetryModel);
                    }
                }
            }

            if (retCount == 3) {
                fonePayMessageVO.setResponseCode(FonePayResponseCodes.RETRY_LIMIT_EXHAUSTED);
                fonePayMessageVO.setResponseCodeDescription(MessageUtil.getMessage("fonepay.error." + FonePayResponseCodes.RETRY_LIMIT_EXHAUSTED));
            } else if (cexp.getErrorCode() == FonePayResponseCodes.INVALID_PIN) {
                fonePayMessageVO.setResponseCode(String.valueOf(cexp.getErrorCode()));
                fonePayMessageVO.setResponseCodeDescription(MessageUtil.getMessage("fonepay.error." + String.valueOf(cexp.getErrorCode())));
            } else {
                fonePayMessageVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
                fonePayMessageVO.setResponseCodeDescription(MessageUtil.getMessage("fonepay.error." + FonePayResponseCodes.GENERAL_ERROR));
            }

        } catch (Exception exp) {
            logger.error("Exception occurred @ validateMPINRetryCount()...", exp);

            fonePayMessageVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            fonePayMessageVO.setResponseCodeDescription(MessageUtil.getMessage("fonepay.error." + FonePayResponseCodes.GENERAL_ERROR));
        }

        logger.debug("[End of FonePayManagerImpl.validateMPINRetryCount()]");
        logger.info("Replying with Response Code " + fonePayMessageVO.getResponseCode());

        return fonePayMessageVO;
    }

    public WebServiceVO getOlaBalance(WebServiceVO webServiceVO) throws FrameworkCheckedException {

        CommonCommandManager commonCommandManager = this.getCommonCommandManager();
        Double balance = 0.0D;

        try {
            ValidationErrors validationError = commonCommandManager.checkActiveAppUser(ThreadLocalAppUser.getAppUserModel());
            if (!validationError.hasValidationErrors()) {
                Long paymentModeId = null;
                if (webServiceVO.getPaymentMode() != null && !webServiceVO.getPaymentMode().equals("") && webServiceVO.getPaymentMode().equals("HRA"))
                    paymentModeId = PaymentModeConstantsInterface.HOME_REMMITTANCE_ACCOUNT;
                else
                    paymentModeId = PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT;
                SmartMoneyAccountModel bbSmartMoneyAccountModel = this.getSmartMoneyAccountModel(ThreadLocalAppUser.getAppUserModel(), paymentModeId);

                if (bbSmartMoneyAccountModel != null) {
                    SearchBaseWrapperImpl searchBaseWrapper = new SearchBaseWrapperImpl();

                    CustomList customList = new CustomList();
                    ArrayList arrList = new ArrayList();
                    arrList.add(bbSmartMoneyAccountModel);
                    customList.setResultsetList(arrList);
                    searchBaseWrapper.setCustomList(customList);
                    //validationErrors = commonCommandManager.checkSmartMoneyAccount(searchBaseWrapper);
                }
                BaseWrapper baseWrapper = new BaseWrapperImpl();
                baseWrapper.setBasePersistableModel(bbSmartMoneyAccountModel);
                AbstractFinancialInstitution olaFinancialInstitution = commonCommandManager.loadFinancialInstitution(baseWrapper);

                TransactionModuleManager transactionModuleManager = (TransactionModuleManager) getBean("transactionModuleManager");

                WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
                workFlowWrapper.setTransactionCodeModel(new TransactionCodeModel());
                workFlowWrapper = transactionModuleManager.generateTransactionCodeRequiresNewTransaction(workFlowWrapper);
                workFlowWrapper.setSmartMoneyAccountModel(bbSmartMoneyAccountModel);

                TransactionModel transactionModel = new TransactionModel();
                transactionModel.setTransactionCodeIdTransactionCodeModel(workFlowWrapper.getTransactionCodeModel());

                workFlowWrapper.setTransactionModel(transactionModel);

                Long customerId = null;
                if (ThreadLocalAppUser.getAppUserModel().getAppUserTypeId() == UserTypeConstantsInterface.CUSTOMER.longValue()) {
                    customerId = ThreadLocalAppUser.getAppUserModel().getCustomerId();
                } else {
                    customerId = ThreadLocalAppUser.getAppUserModel().getAppUserId();
                }
                //ThreadLocalActionLog.setActionLogId(2L);
                AccountInfoModel olaAccountInfoModel = olaFinancialInstitution.getAccountInfoModelBySmartMoneyAccount(bbSmartMoneyAccountModel, customerId, workFlowWrapper.getTransactionCodeModel().getTransactionCodeId());

                SwitchWrapper _switchWrapper = new SwitchWrapperImpl();
                //_switchWrapper.putObject(CommandFieldConstants.KEY_PIN, pin);
                _switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());
                _switchWrapper.setWorkFlowWrapper(workFlowWrapper);
                _switchWrapper.setAccountInfoModel(olaAccountInfoModel);
                _switchWrapper.setTransactionTransactionModel(transactionModel);
                _switchWrapper.setBasePersistableModel(bbSmartMoneyAccountModel);

                olaFinancialInstitution.checkBalanceWithoutPin(_switchWrapper);

                //paymentModeId = PaymentModeConstants
                // Interface.BRANCHLESS_BANKING_ACCOUNT;
                balance = _switchWrapper.getBalance();
                webServiceVO.setBalance(String.valueOf(balance));
                webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
            }

        } catch (Exception e) {
            logger.error("Error in getOlaBalance. Exception message is :: " + e.getMessage());
            webServiceVO.setResponseCodeDescription(e.getMessage());
            throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        }


        return webServiceVO;
    }


    private void sendSMSToUsers(String userName, String pin, boolean isConsumerApp, long registrationStateId) {
        try {
            AppInfoModel appInfoModel = new AppInfoModel();
            appInfoModel.setAppId(AppConstants.CONSUMER_APP);
            List<AppInfoModel> appInfoModelList = new ArrayList<>();
            appInfoModelList = appInfoDAO.findByExample(appInfoModel).getResultsetList();
            ArrayList<String> urls = new ArrayList<>();
            if (appInfoModel != null) {
                for (AppInfoModel model : appInfoModelList
                ) {
                    urls.add(model.getUrl());
                }
            }

            BaseWrapper baseWrapper = new BaseWrapperImpl();

            String brandName = MessageUtil.getMessage("jsbl.brandName");

            //Message to customer
            String customerSMS;
//            if (isConsumerApp && registrationStateId != RegistrationStateConstants.CLSPENDING) {
//                customerSMS = this.getMessageSource().getMessage("smsCommand.act_sms_jsbl_con_app", new Object[]{userName, pin}, null);
//            } else {
//					customerSMS = this.getMessageSource().getMessage("smsCommand.act_sms_jsbl_fonepay",
//							new Object[] { brandName,urls.get(0),urls.get(1), pin}, null);
//            }

            if (registrationStateId == RegistrationStateConstants.CLSPENDING) {
                customerSMS = this.getMessageSource().getMessage("smsCommand.act_sms_jsbl_con_app.pending", new Object[]{userName, pin}, null);
            } else {
                customerSMS = this.getMessageSource().getMessage("smsCommand.act_sms_jsbl_con_app", new Object[]{userName, pin}, null);
            }
            baseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, new SmsMessage(userName, customerSMS));
            getCommonCommandManager().sendSMSToUser(baseWrapper);


        } catch (FrameworkCheckedException e) {
            e.printStackTrace();
        }
    }


    public void setUserDeviceAccountsDAO(UserDeviceAccountsDAO userDeviceAccountsDAO) {
        this.userDeviceAccountsDAO = userDeviceAccountsDAO;
    }

    public void setSmsSender(SmsSender smsSender) {
        this.smsSender = smsSender;
    }

    public void setMiniTransactionDAO(MiniTransactionDAO miniTransactionDAO) {
        this.miniTransactionDAO = miniTransactionDAO;
    }

    public void setVirtualCardHibernateDAO(VirtualCardHibernateDAO virtualCardHibernateDAO) {
        this.virtualCardHibernateDAO = virtualCardHibernateDAO;
    }

    public void setVirtualCardViewHibernateDAO(VirtualCardViewHibernateDAO virtualCardViewHibernateDAO) {
        this.virtualCardViewHibernateDAO = virtualCardViewHibernateDAO;
    }

    public void setFonePayLogDAO(FonePayLogDAO fonePayLogDAO) {
        this.fonePayLogDAO = fonePayLogDAO;
    }


    public void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public void setTransactionDetailMasterDAO(TransactionDetailMasterDAO transactionDetailMasterDAO) {
        this.transactionDetailMasterDAO = transactionDetailMasterDAO;
    }


    public void setLedgerDAO(LedgerDAO ledgerDAO) {
        this.ledgerDAO = ledgerDAO;
    }

    public void setBbAccountsViewDao(BBAccountsViewDao bbAccountsViewDao) {
        this.bbAccountsViewDao = bbAccountsViewDao;
    }

    public void setTransactionDetailMasterManager(TransactionDetailMasterManager transactionDetailMasterManager) {
        this.transactionDetailMasterManager = transactionDetailMasterManager;
    }

    public void setTransactionModuleManager(TransactionModuleManager transactionModuleManager) {
        this.transactionModuleManager = transactionModuleManager;
    }


    public void setSwitchController(SwitchController switchController) {
        this.switchController = switchController;
    }

    public void setSettlementManager(SettlementManager settlementManager) {
        this.settlementManager = settlementManager;
    }

    public void setStakeholderBankInfoManager(StakeholderBankInfoManager stakeholderBankInfoManager) {
        this.stakeholderBankInfoManager = stakeholderBankInfoManager;
    }

    public void setPinRetryDAO(PinRetryDAO pinRetryDAO) {
        this.pinRetryDAO = pinRetryDAO;
    }

    public CommonCommandManager getCommonCommandManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (CommonCommandManager) applicationContext.getBean("commonCommandManager");
    }

    private void addCustomerPicture(Long pictureTypeId, String pictureName, boolean isSkipped, ArrayList<CustomerPictureModel> arrayCustomerPictures) throws IOException, CommandException {
        CustomerPictureModel customerPictureModel = new CustomerPictureModel();
        try {
            System.out.println("Picture Name: " + pictureName);
            File imageFile;
            if (!isSkipped) {
                imageFile = getCommonCommandManager().loadImage("images/upload_dir/" + pictureName);
            } else {
                imageFile = getCommonCommandManager().loadImage("images/no_photo_icon.png");
            }

            imageFile.getAbsolutePath();
            imageFile.getTotalSpace();
            imageFile.getCanonicalPath();
            imageFile.length();
            Path path = imageFile.toPath();

            String s = FilenameUtils.getExtension(String.valueOf(imageFile));
            logger.info(FilenameUtils.getExtension(String.valueOf(imageFile)));
            //String ext=
            byte[] imageFileBytes = Files.readAllBytes(path);

            customerPictureModel.setPicture(imageFileBytes);
            customerPictureModel.setPictureTypeId(pictureTypeId);
            customerPictureModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_1);
            customerPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
            customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
            customerPictureModel.setCreatedOn(new Date());
            customerPictureModel.setUpdatedOn(new Date());
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

    public SearchBaseWrapper searchFonePayTransactionDetail(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        CustomList<FonePayTransactionDetailViewModel>
                list = this.fonePayTransactionDetailViewDAO.findByExample((FonePayTransactionDetailViewModel)
                        searchBaseWrapper.
                                getBasePersistableModel(),
                searchBaseWrapper.
                        getPagingHelperModel(),
                searchBaseWrapper.
                        getSortingOrderMap(), searchBaseWrapper.getDateRangeHolderModelList());
        if (list != null) {
            searchBaseWrapper.setCustomList(list);
        }
        return searchBaseWrapper;
    }

    @Override
    public void sendOtpSms(String otp, String messageType, String mobileNo) throws FrameworkCheckedException {
        //Send OTP via SMS to Customer
        String smsText = MessageUtil.getMessage("otpSms", new String[]{messageType, otp});
        SmsMessage smsMessage = new SmsMessage(mobileNo, smsText);
        smsSender.send(smsMessage);
    }


    public MessageSource getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setCustomerPictureDAO(CustomerPictureDAO customerPictureDAO) {
        this.customerPictureDAO = customerPictureDAO;
    }

    public void setAppUserHibernateDAO(AppUserHibernateDAO appUserHibernateDAO) {
        this.appUserHibernateDAO = appUserHibernateDAO;
    }

    public void setTaxRegimeDAO(TaxRegimeDAO taxRegimeDAO) {
        this.taxRegimeDAO = taxRegimeDAO;
    }


    public void setOlaCustomerAccountTypeDao(OlaCustomerAccountTypeDao olaCustomerAccountTypeDao) {
        this.olaCustomerAccountTypeDao = olaCustomerAccountTypeDao;
    }

    private NadraIntegrationController getNadraIntegrationController() {
        return HttpInvokerUtil.getHttpInvokerFactoryBean(NadraIntegrationController.class,
                MessageUtil.getMessage("NadraIntegrationURL"));
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setVerisysDataHibernateDAO(VerisysDataDAO verisysDataHibernateDAO) {
        this.verisysDataHibernateDAO = verisysDataHibernateDAO;
    }

    public void setFonePayTransactionDetailViewDAO(
            FonePayTransactionDetailViewDAO fonePayTransactionDetailViewDAO) {
        this.fonePayTransactionDetailViewDAO = fonePayTransactionDetailViewDAO;
    }

    private SmartMoneyAccountModel getSmartMoneyAccountModel(AppUserModel appUserModel, Long paymentModeId) throws FrameworkCheckedException {

        SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
        if (appUserModel.getAppUserTypeId() == 2L) {
            smartMoneyAccountModel.setCustomerId(appUserModel.getCustomerId());
        } else {
            smartMoneyAccountModel.setRetailerContactId(appUserModel.getRetailerContactId());
        }
        smartMoneyAccountModel.setPaymentModeId(paymentModeId);
        smartMoneyAccountModel.setDeleted(Boolean.FALSE);
        smartMoneyAccountModel.setActive(Boolean.TRUE);

        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
        searchBaseWrapper = getCommonCommandManager().loadSmartMoneyAccount(searchBaseWrapper);

        if (searchBaseWrapper.getCustomList().getResultsetList().size() > 0) {
            return (SmartMoneyAccountModel) searchBaseWrapper.getCustomList().getResultsetList().get(0);
        }

        return null;
    }

    public Object getBean(String beanName) {
        return ContextLoader.getCurrentWebApplicationContext().getBean(beanName);
    }

    public void setEsbAdapter(ESBAdapter esbAdapter) {
        this.esbAdapter = esbAdapter;
    }

    public void setTransactionReversalManager(TransactionReversalManager transactionReversalManager) {
        this.transactionReversalManager = transactionReversalManager;
    }

    private void loadAndForwardAccountToQueue(final WorkFlowWrapper workFlowWrapper) throws InterruptedException {
        coreAdviceQueingPreProcessor.startProcessing(workFlowWrapper);
    }

    public void setCoreAdviceQueingPreProcessor(CoreAdviceQueingPreProcessor coreAdviceQueingPreProcessor) {
        this.coreAdviceQueingPreProcessor = coreAdviceQueingPreProcessor;
    }

    public void setGenericDAO(GenericDao genericDAO) {
        this.genericDAO = genericDAO;
    }

    public void setClsDebitCreditManager(ClsDebitCreditManager clsDebitCreditManager) {
        this.clsDebitCreditManager = clsDebitCreditManager;
    }

    public void setClsDebitCreditDAO(ClsDebitCreditDAO clsDebitCreditDAO) {
        this.clsDebitCreditDAO = clsDebitCreditDAO;
    }

}