package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.viewer.Command;

import java.util.ArrayList;

import static com.inov8.microbank.common.util.XMLConstants.*;

public class AdvanceSalaryLoanCommand extends BaseCommand {
    private String customerCNIC, customerMobileNo;
    private String productId;
    BaseWrapper baseWrapper;
    private ProductModel productModel;
    private ESBAdapter esbAdapter;
    private AppUserModel appUserModel;
    TransactionModel transactionModel;
    private String withdrawalAmount,txProcessingAmount,commissionAmount,totalAmount,thirdPartyRRN;
    UserDeviceAccountsModel userDeviceAccountsModel;
    CommissionAmountsHolder commissionAmountsHolder;
    RetailerContactModel fromRetailerContactModel;

    private final Log logger = LogFactory.getLog(AdvanceSalaryLoanCommand.class);
    private String logClassName = "AdvanceSalaryLoanCommand";

    @Override
    public void prepare(BaseWrapper baseWrapper) {
        String classNMethodName = "[AdvanceSalaryLoanCommand.prepare] ";
        customerMobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
        customerCNIC = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CNIC);
        deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
        withdrawalAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_AMOUNT);
        txProcessingAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_PROCESS_AMNT);
        commissionAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_COMM_AMOUNT);
        totalAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TOTAL_AMOUNT);
        thirdPartyRRN = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_THIRD_PARTY_RRN);

        try{
            BaseWrapper bWrapper = new BaseWrapperImpl();
            productModel = new ProductModel();
            productModel.setProductId(Long.parseLong(productId));
            bWrapper.setBasePersistableModel(productModel);
            bWrapper = getCommonCommandManager().loadProduct(bWrapper);
            productModel = (ProductModel)bWrapper.getBasePersistableModel();
            this.baseWrapper=baseWrapper;
        }

        catch(Exception ex){
            logger.error(classNMethodName +" Product model not found: " + ex.getStackTrace());
        }
    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        validationErrors = ValidatorWrapper.doRequired(productId,validationErrors,"Product");
        validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
        validationErrors = ValidatorWrapper.doRequired(customerCNIC,validationErrors,"Customer CNIC");
        validationErrors = ValidatorWrapper.doRequired(customerMobileNo,validationErrors,"Customer Mobile");
        validationErrors = ValidatorWrapper.doRequired(withdrawalAmount,validationErrors,"Amount");
        return validationErrors;
    }

    @Override
    public void execute() throws CommandException {
        CommonCommandManager commonCommandManager = this.getCommonCommandManager();
        WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
        appUserModel = ThreadLocalAppUser.getAppUserModel();

        if(appUserModel.getCustomerId() != null) {
            try {
                ValidationErrors validationErrors = commonCommandManager.checkActiveAppUser(appUserModel);
                if (!validationErrors.hasValidationErrors()) {

                    //Check User Device Accounts health
                    BaseWrapper baseWrapper = new BaseWrapperImpl();
                    baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.MOBILE);
                    baseWrapper.setBasePersistableModel(appUserModel);
                    validationErrors = getCommonCommandManager().checkCustomerCredentials(baseWrapper);

                    if (validationErrors.hasValidationErrors()) {
                        throw new CommandException(validationErrors.getErrors(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                    }

                    SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
                    SmartMoneyAccountModel customerSMAModel = new SmartMoneyAccountModel();
                    customerSMAModel.setCustomerId(appUserModel.getCustomerId());
                    customerSMAModel.setActive(Boolean.TRUE);
                    customerSMAModel.setDeleted(Boolean.FALSE);
                    customerSMAModel.setDefAccount(Boolean.TRUE);
                    searchBaseWrapper.setBasePersistableModel(customerSMAModel);

                    searchBaseWrapper = getCommonCommandManager().loadSMAExactMatch(searchBaseWrapper);

                    CustomList smaList = searchBaseWrapper.getCustomList();
                    if (smaList != null && smaList.getResultsetList() != null && smaList.getResultsetList().size() > 0) {
                        customerSMAModel = (SmartMoneyAccountModel) smaList.getResultsetList().get(0);
                    }

                    baseWrapper.setBasePersistableModel(customerSMAModel);
                    validationErrors = commonCommandManager.checkSmartMoneyAccount(baseWrapper);

                    productModel = new ProductModel();
                    productModel.setProductId(Long.parseLong(productId));
                    workFlowWrapper.setProductModel(productModel);

                    workFlowWrapper.setTxProcessingAmount(Double.parseDouble(txProcessingAmount));
                    workFlowWrapper.setTotalAmount(Double.parseDouble(totalAmount));
                    workFlowWrapper.setBillAmount(Double.parseDouble(withdrawalAmount));
                    workFlowWrapper.setTransactionAmount(Double.parseDouble(withdrawalAmount));
                    workFlowWrapper.setTotalCommissionAmount(Double.parseDouble(commissionAmount));
                    workFlowWrapper.setCommissionAmountsHolder(commissionAmountsHolder);
                    workFlowWrapper.setRetailerContactModel(fromRetailerContactModel);
                    workFlowWrapper.setSmartMoneyAccountModel(customerSMAModel);
                    workFlowWrapper.setOlaSmartMoneyAccountModel(customerSMAModel);
                    workFlowWrapper.setSenderSmartMoneyAccountModel(customerSMAModel);
                    workFlowWrapper.putObject(CommandFieldConstants.KEY_THIRD_PARTY_RRN, thirdPartyRRN);
                    workFlowWrapper.putObject(CommandFieldConstants.KEY_TXAM, withdrawalAmount);

                    workFlowWrapper.setAppUserModel(appUserModel);
                    loadCustomerAndSegment(workFlowWrapper, appUserModel.getCustomerId());

                    try{
                        BaseWrapper bWrapper = new BaseWrapperImpl();
                        productModel = new ProductModel();
                        productModel.setProductId(Long.parseLong(productId));
                        bWrapper.setBasePersistableModel(productModel);
                        bWrapper = getCommonCommandManager().loadProduct(bWrapper);
                        productModel = (ProductModel)bWrapper.getBasePersistableModel();
                    }
                    catch(Exception ex){
                        logger.error("Product model not found: " + ex.getStackTrace().toString());
                    }


                    TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
                    transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.ADVANCE_SALARY_LOAN_TX);

                    DistributorModel distributorModel = new DistributorModel();
                    distributorModel.setDistributorId(DistributorConstants.DEFAULT_DISTRIBUTOR_ID);

                    commonCommandManager.checkProductLimit(workFlowWrapper.getSegmentModel().getSegmentId(), productModel.getProductId(),
                            appUserModel.getMobileNo(), Long.valueOf(deviceTypeId), Double.valueOf(withdrawalAmount), productModel, distributorModel, null);

                    String userId = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId();
                    // Velocity validation - start
                    BaseWrapper bWrapper = new BaseWrapperImpl();
                    bWrapper.putObject(CommandConstants.VELOCITY_PRODUCT_ID, Long.parseLong(productId));
                    bWrapper.putObject(CommandConstants.VELOCITY_DEVICE_TYPE_ID,Long.parseLong(deviceTypeId));
                    bWrapper.putObject(CommandConstants.VELOCITY_DISTRIBUTOR_ID, new Long(-1)); //-1 is used to load data with null value only
                    bWrapper.putObject(CommandConstants.VELOCITY_AGENT_TYPE, new Long(-1)); //-1 is used to load data with null value only
                    bWrapper.putObject(CommandConstants.VELOCITY_TRANSACTION_AMOUNT, Double.parseDouble(withdrawalAmount));
                    bWrapper.putObject(CommandConstants.VELOCITY_SEGMENT_ID, workFlowWrapper.getSegmentModel().getSegmentId());
                    bWrapper.putObject(CommandConstants.VELOCITY_ACCOUNT_TYPE, -1L);
                    bWrapper.putObject(CommandConstants.VELOCITY_CUSTOMER_ID, userId);
//                    bWrapper.putObject(CommandConstants.VELOCITY_RETAILER_ID, retailerId);

                    commonCommandManager.checkVelocityCondition(bWrapper);
                    // Velocity validation - end

                    AccountInfoModel accountInfoModel = new AccountInfoModel();

                    DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
                    deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));
                    workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
                    workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
                    workFlowWrapper.setAccountInfoModel(accountInfoModel);
                    workFlowWrapper.putObject(CommandFieldConstants.KEY_CNIC, appUserModel.getNic());
                    workFlowWrapper.setIsCustomerInitiatedTransaction(true);

                    if (validationErrors.hasValidationErrors()) {
                        logger.error("[" + logClassName + ".execute] Throwing Exception in Product ID: " + productId + " - Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Mobile No:" + customerMobileNo);
                        throw new CommandException(validationErrors.getErrors(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                    }

                    workFlowWrapper.setSmartMoneyAccountModel(customerSMAModel);

                    logger.info("[AdvanceSalaryLoanCommand.execute] Going to execute Transaction flow. Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + customerMobileNo);
                    workFlowWrapper = getCommonCommandManager().executeSaleCreditTransaction(workFlowWrapper);

                    transactionModel = workFlowWrapper.getTransactionModel();
                    productModel = workFlowWrapper.getProductModel();
                    userDeviceAccountsModel =  workFlowWrapper.getUserDeviceAccountModel();
                    commissionAmountsHolder = workFlowWrapper.getCommissionAmountsHolder();

                    commonCommandManager.sendSMS(workFlowWrapper);

                }
                else{
                    logger.error("[AdvanceSalaryLoanCommand.execute] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + customerMobileNo + " Trx Amount: " + withdrawalAmount + " Commission: " + commissionAmount);
                    throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
                }
            } catch (CommandException e) {
                logger.error("[" + logClassName + ".execute] Exception Occured for Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Product ID: " + productId + " Exception Message:" + e.getMessage());
                throw e;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            logger.error("[AdvanceSalaryLoanCommand.execute] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + customerMobileNo + " Trx Amount: " + withdrawalAmount + " Commission: " + commissionAmount);
            throw new CommandException(this.getMessageSource().getMessage("billPaymentCommand.invalidAppUserType", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
        }
    }

    @Override
    public String response() {
        return toXML();
    }
    private String toXML(){

        logger.info("Start of CustomerInitiatedAccountToAccountCommand.toXML()");

        ArrayList<LabelValueBean> params = new ArrayList<LabelValueBean>();
        params.add(new LabelValueBean(ATTR_TRXID,replaceNullWithEmpty(transactionModel.getTransactionCodeIdTransactionCodeModel().getCode())));
        params.add(new LabelValueBean(ATTR_DATE, replaceNullWithEmpty(transactionModel.getCreatedOn()+"")));
        params.add(new LabelValueBean(ATTR_DATEF, PortalDateUtils.formatDate(transactionModel.getCreatedOn(),PortalDateUtils.SHORT_DATE_TIME_FORMAT3)));
        params.add(new LabelValueBean(ATTR_TIMEF, Formatter.formatTime(transactionModel.getCreatedOn())));
        params.add(new LabelValueBean(ATTR_PROD, replaceNullWithEmpty(productModel.getName())));
        params.add(new LabelValueBean(ATTR_CAMT, replaceNullWithEmpty(transactionModel.getTotalCommissionAmount()+"")));
        params.add(new LabelValueBean(ATTR_CAMTF, Formatter.formatNumbers(transactionModel.getTotalCommissionAmount())));
        params.add(new LabelValueBean(ATTR_TPAM, replaceNullWithEmpty(txProcessingAmount)));
        params.add(new LabelValueBean(ATTR_TPAMF, Formatter.formatNumbers(Double.parseDouble(txProcessingAmount))));
        params.add(new LabelValueBean(ATTR_TAMT, replaceNullWithEmpty(transactionModel.getTotalAmount()+"")));
        params.add(new LabelValueBean(ATTR_TAMTF, Formatter.formatNumbers(transactionModel.getTotalAmount())));
        params.add(new LabelValueBean(ATTR_TXAM, replaceNullWithEmpty(transactionModel.getTransactionAmount()+"")));
        params.add(new LabelValueBean(ATTR_TXAMF, Formatter.formatNumbers(transactionModel.getTransactionAmount())));

        logger.info("End of CustomerInitiatedAccountToAccountCommand.toXML()");

        return MiniXMLUtil.createResponseXMLByParams(params);

    }
    private void loadCustomerAndSegment(WorkFlowWrapper workFlowWrapper, Long customerId) throws Exception{
        CustomerModel custModel = new CustomerModel();
        custModel.setCustomerId(customerId);
        BaseWrapper bWrapper = new BaseWrapperImpl();
        bWrapper.setBasePersistableModel(custModel);
        bWrapper = this.getCommonCommandManager().loadCustomer(bWrapper);
        if(null != bWrapper.getBasePersistableModel()){
            custModel = (CustomerModel) bWrapper.getBasePersistableModel();
            workFlowWrapper.setCustomerModel(custModel);
            workFlowWrapper.setSegmentModel(custModel.getSegmentIdSegmentModel());
        }else{
            logger.error("[CustomerBBToCoreCommand.loadCustomerAndSegment] Error while loading customer model... against customerId:"+customerId);
        }
    }
}
