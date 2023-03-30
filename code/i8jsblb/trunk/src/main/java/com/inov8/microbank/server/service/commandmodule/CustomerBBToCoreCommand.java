package com.inov8.microbank.server.service.commandmodule;

import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.bankmodule.MemberBankModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.fonepay.common.FonePayConstants;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.switchmodule.iris.model.CustomerAccount;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.integration.vo.BBToCoreVO;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.hibernate.HibernateException;
import org.springframework.dao.DataAccessException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerBBToCoreCommand extends BaseCommand {
    private AppUserModel appUserModel;
    private String productId;

    private String customerMobileNo;
    private String receiverMobileNo;

    private String deviceTypeId;
    private String transactionAmount;
    private String txProcessingAmount;
    private String commissionAmount;
    private String totalAmount;

    private String coreAccountNo;
    private String accountTitle;

    protected String receiverBankName;
    protected String receiverBranchName;
    protected String receiverIBAN;
    protected String crDr;
    protected String purposeOfPayment;
    //
    private String terminalId;
    private String channelId;
    private String thirdPartyTransactionId;

    TransactionModel transactionModel;
    ProductModel productModel;
    BaseWrapper baseWrapper;
    SmartMoneyAccountModel customerSMAModel;
    UserDeviceAccountsModel userDeviceAccountsModel;
    CommissionAmountsHolder commissionAmountsHolder;
    protected String toBankImd;
    WorkFlowWrapper workFlowWrapper;
    private String stan;

    protected final Log logger = LogFactory.getLog(CustomerBBToCoreCommand.class);

    @Override
    public void execute() throws CommandException {

        CommonCommandManager commonCommandManager = this.getCommonCommandManager();
        workFlowWrapper = new WorkFlowWrapperImpl();

        if (appUserModel.getCustomerId() != null) {
            try {
                CustomerModel customerModel = new CustomerModel();
                customerModel = commonCommandManager.getCustomerModelById(appUserModel.getCustomerId());
                ValidationErrors validationErrors = commonCommandManager.checkActiveAppUser(appUserModel);

                if (!validationErrors.hasValidationErrors()) {
                    SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
                    customerSMAModel = new SmartMoneyAccountModel();
                    customerSMAModel.setCustomerId(appUserModel.getCustomerId());
                    customerSMAModel.setActive(Boolean.TRUE);
                    customerSMAModel.setDeleted(Boolean.FALSE);
                    customerSMAModel.setDefAccount(Boolean.TRUE);
                    searchBaseWrapper.setBasePersistableModel(customerSMAModel);

                    searchBaseWrapper = commonCommandManager.loadSMAExactMatch(searchBaseWrapper);
                    if (searchBaseWrapper.getCustomList() != null && searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0) {
                        customerSMAModel = (SmartMoneyAccountModel) searchBaseWrapper.getCustomList().getResultsetList().get(0);
                    }

                    if ((this.channelId.equals("PAYFAST") || this.channelId.equals("PAYFAST-COMM") || this.channelId.equals("PAYFAST-UBPS") || this.channelId.equals("PAYFAST-WTOW")) && customerModel.getSegmentId() != 10372L) {
                        throw new CommandException("PayFast Channel Not Allowed Other Segment Transaction", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.HIGH, new Throwable());
                    }
                    AccountInfoModel accountInfoModel = new AccountInfoModel();
                    accountInfoModel.setOldPin("");//should not be used
                    accountInfoModel.setCustomerId(appUserModel.getAppUserId());
                    accountInfoModel.setAccountNick(customerSMAModel.getName());
                    workFlowWrapper.setAccountInfoModel(accountInfoModel);

                    productModel = new ProductModel();
                    productModel.setProductId(Long.parseLong(productId));
                    workFlowWrapper.setProductModel(productModel);

                    workFlowWrapper.setSmartMoneyAccountModel(customerSMAModel);
                    workFlowWrapper.setOlaSmartMoneyAccountModel(customerSMAModel);
                    workFlowWrapper.setSenderSmartMoneyAccountModel(customerSMAModel);

                    workFlowWrapper.setAppUserModel(appUserModel);
                    workFlowWrapper.setSenderAppUserModel(appUserModel);
                    workFlowWrapper.setCustomerAppUserModel(appUserModel);

                    workFlowWrapper.setTxProcessingAmount(Double.parseDouble(txProcessingAmount));
                    workFlowWrapper.setTotalAmount(Double.parseDouble(totalAmount));
                    workFlowWrapper.setBillAmount(Double.parseDouble(transactionAmount));
                    workFlowWrapper.setTotalCommissionAmount(Double.parseDouble(commissionAmount));


                    ProductVO productVo = commonCommandManager.loadProductVO(baseWrapper);
                    if (productVo == null) {
                        throw new CommandException(this.getMessageSource().getMessage("billPaymentCommand.productVoLoadedError", null, null), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                    }

                    ((BBToCoreVO) productVo).setAccountNumber(coreAccountNo);
                    workFlowWrapper.setProductVO(productVo);

                    loadCustomerAndSegment(workFlowWrapper, appUserModel.getCustomerId());

                    workFlowWrapper.setWalkInCustomerMob(receiverMobileNo);

                    try {
                        BaseWrapper bWrapper = new BaseWrapperImpl();
                        productModel = new ProductModel();
                        productModel.setProductId(Long.parseLong(productId));
                        bWrapper.setBasePersistableModel(productModel);
                        bWrapper = getCommonCommandManager().loadProduct(bWrapper);
                        productModel = (ProductModel) bWrapper.getBasePersistableModel();
                    } catch (Exception ex) {
                        logger.error("Product model not found: " + ex.getStackTrace().toString());
                    }

                    TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
                    transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.CUSTOMER_INITIATED_BB_TO_CORE_ACCOUNT_TX);

                    DistributorModel distributorModel = new DistributorModel();
                    distributorModel.setDistributorId(DistributorConstants.DEFAULT_DISTRIBUTOR_ID);

                    if (!(productId.equalsIgnoreCase(ProductConstantsInterface.RELIEF_FUND_PRODUCT.toString()))) {
                        commonCommandManager.checkProductLimit(workFlowWrapper.getSegmentModel().getSegmentId(), productModel.getProductId(),
                                appUserModel.getMobileNo(), Long.valueOf(deviceTypeId), Double.valueOf(transactionAmount), productModel, distributorModel, null);
                    }
                    String userId = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId();

                    // Velocity validation - start
                    BaseWrapper bWrapper = new BaseWrapperImpl();
                    bWrapper.putObject(CommandConstants.VELOCITY_PRODUCT_ID, Long.parseLong(productId));
                    bWrapper.putObject(CommandConstants.VELOCITY_DEVICE_TYPE_ID, Long.parseLong(deviceTypeId));
                    bWrapper.putObject(CommandConstants.VELOCITY_DISTRIBUTOR_ID, new Long(-1)); //-1 is used to load data with null value only
                    bWrapper.putObject(CommandConstants.VELOCITY_AGENT_TYPE, new Long(-1)); //-1 is used to load data with null value only
                    bWrapper.putObject(CommandConstants.VELOCITY_TRANSACTION_AMOUNT, Double.parseDouble(transactionAmount));
                    bWrapper.putObject(CommandConstants.VELOCITY_SEGMENT_ID, workFlowWrapper.getSegmentModel().getSegmentId());
                    bWrapper.putObject(CommandConstants.VELOCITY_ACCOUNT_TYPE, customerModel.getCustomerAccountTypeId());
                    bWrapper.putObject(CommandConstants.VELOCITY_CUSTOMER_ID, userId);
//                    bWrapper.putObject(CommandConstants.VELOCITY_RETAILER_ID, retailerId);
                    commonCommandManager.checkVelocityCondition(bWrapper);
                    // Velocity validation - end

                    DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
                    deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));
                    workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
                    workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
                    workFlowWrapper.putObject(CommandFieldConstants.KEY_CNIC, appUserModel.getNic());
                    workFlowWrapper.setIsCustomerInitiatedTransaction(true);

                    CustomerAccount customerAccount = new CustomerAccount(coreAccountNo, null, null, null);
                    customerAccount.setFromBankImd("603733");
                    if (accountTitle != null && !accountTitle.equals(""))
                        customerAccount.setTitleOfTheAccount(accountTitle);
                    customerAccount.setBankName(receiverBankName);
                    customerAccount.setBranchName(receiverBranchName);
                    customerAccount.setBenificieryIBAN(receiverIBAN);
                    customerAccount.setTransactionType(crDr);
                    customerAccount.setToBankImd(toBankImd);
                    workFlowWrapper.setCustomerAccount(customerAccount);
                    workFlowWrapper.putObject(CommandFieldConstants.KEY_ACCOUNT_TITLE_AGNETMATE, accountTitle);
                    workFlowWrapper.putObject(CommandFieldConstants.KEY_CC_TO_BANK_IMD, toBankImd);
                    workFlowWrapper.putObject(CommandFieldConstants.KEY_TRANS_PURPOSE_CODE, purposeOfPayment);

                    MemberBankModel model = null;
                    MemberBankModel memberBankModel = new MemberBankModel();
                    memberBankModel.setBankImd(toBankImd);
                    List<MemberBankModel> list = commonCommandManager.getMemberBankDao().findByExample(memberBankModel).getResultsetList();
                    if (list != null && !list.isEmpty()) {
                        model = list.get(0);
                    }
                    if (model == null) {
                        throw new CommandException("Member Bank not found", ErrorCodes.MEMBER_BANK_NOT_FOUND, ErrorLevel.MEDIUM, new Throwable());
                    }
                    workFlowWrapper.putObject("BANK_SHORT_NAME", model.getBankShortName());
                    workFlowWrapper.setMemberBankModel(memberBankModel);
                    workFlowWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, channelId);
                    workFlowWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, terminalId);
                    workFlowWrapper.putObject(CommandFieldConstants.KEY_STAN, stan);
                    workFlowWrapper.putObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE, thirdPartyTransactionId);

                    logger.info("[CustomerBBToCoreCommand.execute] Going to execute Transaction flow. Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + customerMobileNo);
                    workFlowWrapper = commonCommandManager.executeSaleCreditTransaction(workFlowWrapper);

                    transactionModel = workFlowWrapper.getTransactionModel();
                    productModel = workFlowWrapper.getProductModel();
                    userDeviceAccountsModel = workFlowWrapper.getUserDeviceAccountModel();
                    commissionAmountsHolder = workFlowWrapper.getCommissionAmountsHolder();
                    workFlowWrapper.putObject("productTile",productModel.getName());
                    commonCommandManager.sendSMS(workFlowWrapper);
                    commonCommandManager.novaAlertMessage(workFlowWrapper);
                } else {
                    logger.error("[CustomerBBToCoreCommand.execute] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + customerMobileNo + " Trx Amount: " + transactionAmount + " Commission: " + commissionAmount);
                    throw new CommandException(validationErrors.getErrors(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                }
            } catch (WorkFlowException wex) {
                if (wex.getMessage() != null && wex.getMessage().equalsIgnoreCase(WorkFlowErrorCodeConstants.DAILY_DEBIT_LIMIT_MESSAGE)) {
                    ivrErrorCode = String.valueOf(8062L);
                    throw new CommandException(wex.getMessage(), Long.valueOf(ivrErrorCode), ErrorLevel.MEDIUM, wex);
                } else if (wex.getMessage() != null && wex.getMessage().equalsIgnoreCase(WorkFlowErrorCodeConstants.MONTHLY_DEBIT_LIMIT_MESSAGE)) {
                    ivrErrorCode = String.valueOf(8064L);
                    throw new CommandException(wex.getMessage(), Long.valueOf(ivrErrorCode), ErrorLevel.MEDIUM, wex);
                } else if (wex.getMessage() != null && wex.getMessage().equalsIgnoreCase(WorkFlowErrorCodeConstants.MONTHLY_CREDIT_LIMIT_MESSAGE)) {
                    ivrErrorCode = String.valueOf(8063L);
                    throw new CommandException(wex.getMessage(), Long.valueOf(ivrErrorCode), ErrorLevel.MEDIUM, wex);
                } else if (wex.getMessage() != null && wex.getMessage().equalsIgnoreCase(WorkFlowErrorCodeConstants.DAILY_CREDIT_LIMIT_MESSAGE)) {
                    ivrErrorCode = String.valueOf(8061L);
                    throw new CommandException(wex.getMessage(), Long.valueOf(ivrErrorCode), ErrorLevel.MEDIUM, wex);
                } else if (wex.getMessage() != null && wex.getMessage().equalsIgnoreCase(WorkFlowErrorCodeConstants.INSUFFICIENT_ACC_BALANCE_MESSAGE)) {
                    ivrErrorCode = String.valueOf(8059L);
                    throw new CommandException(wex.getMessage(), Long.valueOf(ivrErrorCode), ErrorLevel.MEDIUM, wex);
                } else {
                    ivrErrorCode = ErrorCodes.COMMAND_EXECUTION_ERROR.toString();
                    throw new CommandException(wex.getMessage(), Long.valueOf(ivrErrorCode), ErrorLevel.MEDIUM, wex);
                }
            } catch (CommandException ce) {
                logger.error("Going to throw CommandException... Details:..", ce);
                throw ce;
            } catch (Exception ex) {
                logger.error("Exception catched on Command... Details:..", ex);
                if (ex instanceof NullPointerException
                        || ex instanceof HibernateException
                        || ex instanceof SQLException
                        || ex instanceof DataAccessException
                        || (ex.getMessage() != null && ex.getMessage().indexOf("Exception") != -1)) {

                    logger.error("Converting Exception (" + ex.getClass() + ") to generic error message...");
                    throw new CommandException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
                } else {
                    throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
                }
            }
        } else {
            logger.error("[CustomerBBToCoreCommand.execute] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + customerMobileNo + " Trx Amount: " + transactionAmount + " Commission: " + commissionAmount);
            throw new CommandException(this.getMessageSource().getMessage("billPaymentCommand.invalidAppUserType", null, null), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        }
    }

    @Override
    public void prepare(BaseWrapper baseWrapper) {
        this.baseWrapper = baseWrapper;
        appUserModel = ThreadLocalAppUser.getAppUserModel();

        thirdPartyTransactionId = getCommandParameter(baseWrapper, FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE);
        channelId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CHANNEL_ID);
        terminalId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TERMINAL_ID);
        productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
//        productId = String.valueOf(ProductConstantsInterface.CUSTOMER_BB_TO_IBFT);
        deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        customerMobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
        coreAccountNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CORE_ACC_NO);
        transactionAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_AMOUNT);
        txProcessingAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_PROCESS_AMNT);
        commissionAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_COMM_AMOUNT);
        totalAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TOTAL_AMOUNT);
        stan = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_STAN);
        accountTitle = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_TITLE_AGNETMATE);
        receiverMobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE);

        receiverBankName = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BENE_BANK_NAME);
        receiverBranchName = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BENE_BRANCH_NAME);
        receiverIBAN = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BENE_IBAN);
        crDr = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BENE_TRX_TYPE);
        toBankImd = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CC_TO_BANK_IMD);
        purposeOfPayment = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TRANS_PURPOSE_CODE);
        logger.info("[CustomerBBToCoreCommand.prepare] Logged In AppUserModel: " + appUserModel.getAppUserId() +
                " Product ID:" + productId + " deviceTypeId: " + deviceTypeId +
                " customerMobileNo: " + customerMobileNo + " transactionAmount:" + transactionAmount);

    }

    @Override
    public String response() {
        return MiniXMLUtil.createResponseXMLByParams(this.toXML());
    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        validationErrors = ValidatorWrapper.doRequired(productId, validationErrors, "Product");
        validationErrors = ValidatorWrapper.doRequired(coreAccountNo, validationErrors, "Account Number");
        validationErrors = ValidatorWrapper.doRequired(customerMobileNo, validationErrors, "Customer Mobile Number");
        validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
        validationErrors = ValidatorWrapper.doRequired(txProcessingAmount, validationErrors, "Tx Amount");
        validationErrors = ValidatorWrapper.doRequired(totalAmount, validationErrors, "Total Amount");
        validationErrors = ValidatorWrapper.doRequired(commissionAmount, validationErrors, "Commission Amount");
        validationErrors = ValidatorWrapper.doRequired(transactionAmount, validationErrors, "Transaction Amount");
        if (productId.equals(ProductConstantsInterface.CUSTOMER_BB_TO_IBFT)) {
            validationErrors = ValidatorWrapper.doRequired(toBankImd, validationErrors, "Bank ");
        }

        if (!validationErrors.hasValidationErrors()) {
            validationErrors = ValidatorWrapper.doNumeric(productId, validationErrors, "Product");
            validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
            validationErrors = ValidatorWrapper.doNumeric(txProcessingAmount, validationErrors, "Tx Amount");
        }

        return validationErrors;
    }

    private List<LabelValueBean> toXML() {

        List<LabelValueBean> params = new ArrayList<LabelValueBean>(20);
        params.add(new LabelValueBean(CommandFieldConstants.KEY_TX_ID, replaceNullWithEmpty(transactionModel.getTransactionCodeIdTransactionCodeModel().getCode())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_CUSTOMER_MOBILE, replaceNullWithEmpty(customerMobileNo)));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE, replaceNullWithEmpty(receiverMobileNo)));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_CORE_ACC_NO, replaceNullWithEmpty(coreAccountNo)));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_ACCOUNT_TITLE_AGNETMATE, StringEscapeUtils.escapeXml(accountTitle)));

        params.add(new LabelValueBean(CommandFieldConstants.KEY_TX_AMOUNT, "" + replaceNullWithZero(commissionAmountsHolder.getTransactionAmount())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_FORMATTED_TX_AMOUNT, "" + replaceNullWithZero(commissionAmountsHolder.getTransactionAmount())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_COMM_AMOUNT, "" + replaceNullWithZero(commissionAmountsHolder.getTotalCommissionAmount())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_FORMATED_COMM_AMOUNT, "" + replaceNullWithZero(commissionAmountsHolder.getTotalCommissionAmount())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_TX_PROCESS_AMNT, "" + replaceNullWithZero(commissionAmountsHolder.getTransactionProcessingAmount())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_FORMATED_TX_PROCESS_AMNT, "" + replaceNullWithZero(commissionAmountsHolder.getTransactionProcessingAmount())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_TOTAL_AMOUNT, "" + replaceNullWithZero(commissionAmountsHolder.getTotalAmount())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_FORMATED_TOTAL_AMOUNT, "" + replaceNullWithZero(commissionAmountsHolder.getTotalAmount())));

        params.add(new LabelValueBean(CommandFieldConstants.KEY_DATEF, PortalDateUtils.formatDate(workFlowWrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_TIME_FORMAT3)));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_DATE, workFlowWrapper.getTransactionModel().getCreatedOn().toString()));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_TIMEF, Formatter.formatTime(workFlowWrapper.getTransactionModel().getCreatedOn())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_BAL, "" + workFlowWrapper.getOLASwitchWrapper().getOlavo().getFromBalanceAfterTransaction()));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_FORMATED_BAL, "" + Formatter.formatDouble(workFlowWrapper.getOLASwitchWrapper().getOlavo().getFromBalanceAfterTransaction())));

        return params;
    }

    private void loadCustomerAndSegment(WorkFlowWrapper workFlowWrapper, Long customerId) throws Exception {
        CustomerModel custModel = new CustomerModel();
        custModel.setCustomerId(customerId);
        BaseWrapper bWrapper = new BaseWrapperImpl();
        bWrapper.setBasePersistableModel(custModel);
        bWrapper = this.getCommonCommandManager().loadCustomer(bWrapper);
        if (null != bWrapper.getBasePersistableModel()) {
            custModel = (CustomerModel) bWrapper.getBasePersistableModel();
            workFlowWrapper.setCustomerModel(custModel);
            workFlowWrapper.setSegmentModel(custModel.getSegmentIdSegmentModel());
        } else {
            logger.error("[CustomerBBToCoreCommand.loadCustomerAndSegment] Error while loading customer model... against customerId:" + customerId);
        }
    }

}