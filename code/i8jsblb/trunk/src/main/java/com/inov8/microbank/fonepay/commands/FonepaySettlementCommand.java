package com.inov8.microbank.fonepay.commands;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.fonepay.common.FonePayConstants;
import com.inov8.microbank.fonepay.common.FonePayResponseCodes;
import com.inov8.microbank.fonepay.service.FonePayManager;
import com.inov8.microbank.server.service.commandmodule.BaseCommand;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by Attique on 10/16/2017.
 */
public class FonepaySettlementCommand extends BaseCommand {


    protected final Log logger = LogFactory.getLog(FonepaySettlementCommand.class);
    private AppUserModel appUserModel = null;
    private String mobileNo;
    private String dateTime;
    private String amount;
    private String rrn;
    protected String cnic;
    protected String transactionType;
    protected String paymentType;
    protected String settelmentType;
    protected String fonepayTransactionCode;

    protected String transactionId;
    protected String accNo;
    private Long deviceTypeId;
    private String commissionAmount = null;
    protected BaseWrapper preparedBaseWrapper;
    private Double TPAM = 0.0D;
    private FonePayManager fonepayManager;
    private String transactionCode;

    @Override
    public void prepare(BaseWrapper baseWrapper) {

        if (logger.isDebugEnabled()){
            logger.debug("Start of FonepayPaymentCommand.prepare()");
        }

        mobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOB_NO);
        dateTime = this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_DATE);
        amount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AMOUNT);
        rrn = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_RRN);
        deviceTypeId = DeviceTypeConstantsInterface.WEB_SERVICE;
       // mpin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
        transactionType = this.getCommandParameter(baseWrapper, FonePayConstants.TRANSACTION_TYPE);
        paymentType = this.getCommandParameter(baseWrapper, FonePayConstants.PAYMENT_TYPE);

        settelmentType = this.getCommandParameter(baseWrapper, FonePayConstants.KEY_FONEPAY_SETTLEMENT_TYPE);
        fonepayTransactionCode = this.getCommandParameter(baseWrapper, FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE);

        try {
            appUserModel = getCommonCommandManager().loadAppUserByMobileAndType(mobileNo);
            if(appUserModel != null){
                logger.debug("[FonepayPaymentCommand.execute] AppUserModel loader wil AppUserId:"+appUserModel.getAppUserId());
                ThreadLocalAppUser.setAppUserModel(appUserModel);
                cnic = appUserModel.getNic();
            }
        } catch (Exception e) {
            logger.error("[FonepayPaymentCommand.execute] Unable to Load AppUserModel by mobile: " + mobileNo + "\nSetting Default appUserModel in ThreadLocal. appUserId:" + PortalConstants.SCHEDULER_APP_USER_ID+"\n",e);
        }


    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {

        validationErrors = ValidatorWrapper.doRequired(mobileNo, validationErrors, "Mobile No");
        validationErrors = ValidatorWrapper.doRequired(cnic, validationErrors, "CNIC");
        validationErrors = ValidatorWrapper.doRequired(amount, validationErrors, "Amount");
        validationErrors = ValidatorWrapper.doRequired(paymentType, validationErrors, "Payment Type");
        if(!validationErrors.hasValidationErrors()){
            validationErrors = ValidatorWrapper.doNumeric(mobileNo,validationErrors,"Mobile No");
            validationErrors = ValidatorWrapper.doNumeric(cnic, validationErrors, "CNIC");
            validationErrors = ValidatorWrapper.doNumeric(amount, validationErrors, "Amount");
        }

        return validationErrors;
    }

    @Override
    public void execute() throws CommandException {
        logger.info("Start of FonepayPaymentCommand.execute()");

        Double _commissionAmount = 0.0D;
        WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        try {
            CommonCommandManager commonCommandManager = this.getCommonCommandManager();
            SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
            SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();

            //Always Aggent Id will be set in case of Fonepay transaction.
            smartMoneyAccountModel.setRetailerContactId(appUserModel.getRetailerContactId());
            searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);

            searchBaseWrapper = commonCommandManager.loadSmartMoneyAccount(searchBaseWrapper);
            smartMoneyAccountModel = (SmartMoneyAccountModel)searchBaseWrapper.getCustomList().getResultsetList().get(0);

            ValidationErrors validationErrors = commonCommandManager.checkSmartMoneyAccount(searchBaseWrapper);
            if(validationErrors.hasValidationErrors()){
                throw new CommandException(validationErrors.getErrors(), FonePayResponseCodes.PIN_CHANGE_REQUIRED,ErrorLevel.MEDIUM,new Throwable());
            }
            //*******************************************************************************
            //All Validations goes here
            ProductModel productModel = new ProductModel();
            productModel.setProductId(ProductConstantsInterface.FONEPAY_AGENT_PAYMENT);
            baseWrapper.setBasePersistableModel(productModel);
            baseWrapper = commonCommandManager.loadProduct(baseWrapper);

            productModel = (ProductModel) baseWrapper.getBasePersistableModel();
            if(productModel == null){
                logger.error("[FonepayPaymentCommand.execute()] Unable to load productModel. mobileNo:" + mobileNo);
                throw new CommandException("Unable to load productModel", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);
            }
            workFlowWrapper.setProductModel(productModel);

            AppUserModel agentAppUserModel = new AppUserModel();
            agentAppUserModel.setMobileNo(mobileNo);
            agentAppUserModel.setAppUserTypeId(UserTypeConstantsInterface.RETAILER);
            agentAppUserModel.setRetailerContactId(appUserModel.getRetailerContactId());

            //Product Limit Check
            getCommonCommandManager().checkProductLimit(null, productModel.getProductId(), appUserModel.getMobileNo(),
                    deviceTypeId, Double.parseDouble(amount), productModel, null, null);

            //*****************************************************************************************************
            BaseWrapper bWrapper = new BaseWrapperImpl();
            bWrapper.putObject(CommandConstants.VELOCITY_PRODUCT_ID, new Long(productModel.getProductId()));
            bWrapper.putObject(CommandConstants.VELOCITY_DEVICE_TYPE_ID,new Long(deviceTypeId));
            bWrapper.putObject(CommandConstants.VELOCITY_TRANSACTION_AMOUNT, Double.parseDouble(amount));

            bWrapper.putObject(CommandConstants.VELOCITY_SEGMENT_ID, new Long(CommissionConstantsInterface.DEFAULT_SEGMENT_ID));
            boolean result = commonCommandManager.checkVelocityCondition(bWrapper);

            baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
            workFlowWrapper.setAccountInfoModel(new AccountInfoModel());
            workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
            SwitchWrapper switchWrapper = new SwitchWrapperImpl();
            switchWrapper.setTransactionAmount(Double.valueOf(amount));
            switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT.longValue());
            switchWrapper.setBankId(BankConstantsInterface.OLA_BANK_ID);
            switchWrapper.setWorkFlowWrapper(workFlowWrapper);
            switchWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);

            baseWrapper.putObject(CommandFieldConstants.KEY_MOB_NO, mobileNo);
            baseWrapper.putObject(CommandFieldConstants.KEY_AMOUNT, amount);

            TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
            transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.AGENT_SATTELMENT_PAYMENT_TX);

            TransactionModel trxnModel = new TransactionModel();
            trxnModel.setTransactionAmount(Double.parseDouble(amount));

            DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
            deviceTypeModel.setDeviceTypeId(deviceTypeId);
            workFlowWrapper.setRetailerAppUserModel(agentAppUserModel);
            workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
            workFlowWrapper.setTransactionModel(trxnModel);
            workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
            workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
            //workFlowWrapper.setProductVO(productVo);
            workFlowWrapper.setTxProcessingAmount(Double.parseDouble(amount));
            workFlowWrapper.setTotalAmount(Double.parseDouble(amount));
            workFlowWrapper.setTransactionAmount(Double.parseDouble(amount));

            workFlowWrapper.setAppUserModel(appUserModel);

            workFlowWrapper.putObject(CommandFieldConstants.KEY_CNIC, appUserModel.getNic());
            workFlowWrapper.setSwitchWrapper(switchWrapper);
            workFlowWrapper.setOlaSmartMoneyAccountModel(smartMoneyAccountModel);

            workFlowWrapper.putObject(CommandFieldConstants.KEY_DATE, dateTime);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_RRN, rrn);
            workFlowWrapper.setIsCustomerInitiatedTransaction(true);

            workFlowWrapper.putObject(FonePayConstants.KEY_FONEPAY_SETTLEMENT_TYPE,settelmentType);
            workFlowWrapper.putObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE,fonepayTransactionCode);
            //**************************************************************************************//
            //						Calling of Transaction											//
            //**************************************************************************************//

            workFlowWrapper = commonCommandManager.executeSaleCreditTransaction(workFlowWrapper);
            transactionCode = workFlowWrapper.getTransactionDetailMasterModel().getTransactionCode();

            //**************************************************************************************//

        }catch (Exception e) {
            logger.error("[FonepayPaymentCommand.execute()] Exception occured for mobileNo:" + mobileNo + "\n" + ExceptionProcessorUtility.prepareExceptionStackTrace(e));
            throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
        }


    }

    @Override
    public String response() {

        return transactionCode;
    }

    public FonePayManager getFonepayManager() {
        return fonepayManager;
    }

    public void setFonepayManager(FonePayManager fonepayManager) {
        this.fonepayManager = fonepayManager;
    }
}
