package com.inov8.microbank.server.service.commandmodule;

import static com.inov8.microbank.common.util.XMLConstants.ATTR_PARAM_NAME;
import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAM;
import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAMS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_EQUAL;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_OPEN;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_QUOTE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SPACE;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.inov8.microbank.common.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.StakeholderBankInfoModel;
import com.inov8.microbank.common.model.SupplierBankInfoModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.TransactionTypeModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.integration.dispenser.BillPaymentProductDispenser;
import com.inov8.microbank.server.service.integration.vo.BillPaymentVO;
import com.inov8.microbank.server.service.integration.vo.UtilityBillVO;
import com.inov8.verifly.common.model.AccountInfoModel;


public class AllPayBillInfoCommand extends BaseCommand {

    private final Log logger = LogFactory.getLog(AllPayBillInfoCommand.class);

    private BaseWrapper baseWrapper;
    private ProductModel productModel;
    private BillPaymentVO billPaymentVO;
    private AppUserModel appUserModel;
    private BaseWrapper preparedBaseWrapper;
    private DateTimeFormatter dateTimeFormatter;
    ;
    private UserDeviceAccountsModel userDeviceAccountsModel;
    private CommissionAmountsHolder commissionAmountsHolder;


    private Long reasonId = -1L;
    private String accountId;
    private Double discountAmount;
    private String productId;
    private String deviceTypeId;
    private String walkInCustomerCNIC;
    private String walkInCustomerMobileNumber;
    private String retailerMobileNumber;
    private String successMessage;
    private Double billAmount;
    private String customerCNIC;
    private String customerMobileNumber;
    private String consumerNumber;
    private String paymentType = "0";
    private Boolean isPayByAccount = Boolean.FALSE;

    protected String billAmount1;

    public AllPayBillInfoCommand() {

        dateTimeFormatter = DateTimeFormat.forPattern("dd/MM/yyyy");
    }


    @Override
    public void execute() throws CommandException {

        if (logger.isDebugEnabled()) {

            logger.debug("Start of AllPayBillInfoCommand.execute()");
        }

        ThreadLocalAppUser.setAppUserModel(appUserModel);

        WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
        workFlowWrapper.setHandlerModel(handlerModel);
        workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);

        CommissionWrapper commissionWrapper;
        baseWrapper = new BaseWrapperImpl();

        if (appUserModel.getRetailerContactId() != null) {

            try {

                ValidationErrors validationErrors = getCommonCommandManager().checkActiveAppUser(appUserModel);

                if (!validationErrors.hasValidationErrors()) {

                    SmartMoneyAccountModel smartMoneyAccountModel = getSmartMoneyAccountModel(appUserModel.getRetailerContactId());

                    if (smartMoneyAccountModel.getName() != null &&
                            smartMoneyAccountModel.getRetailerContactId().toString().equals(appUserModel.getRetailerContactId().toString())) {

                        baseWrapper.getDataMap().putAll(preparedBaseWrapper.getDataMap());
                        baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, productId);
                        UtilityBillVO productVO = (UtilityBillVO) getCommonCommandManager().loadProductVO(baseWrapper);
                        productVO.setConsumerNo(this.consumerNumber);

                        if (productVO != null) {

                            productModel = (ProductModel) baseWrapper.getBasePersistableModel();

                            if (productModel.getProductIntgModuleInfoId() == null) {

                                throw new CommandException("Product Intg Module Infois not loaded", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                            }
                        } else {
                            throw new CommandException("ProductVo is not loaded", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                        }

                        workFlowWrapper.setProductModel(productModel);

                        AppUserModel customerAppUserModel = null;
                        CustomerModel customerModel = null;

                        Long segmentId = CommissionConstantsInterface.DEFAULT_SEGMENT_ID;

                        AccountInfoModel accountInfoModel = null;

                        if (!StringUtil.isNullOrEmpty(customerMobileNumber)) {

                            customerAppUserModel = this.getCustomerAppUserModel(customerMobileNumber);

                            if (customerAppUserModel != null) {

                                customerCNIC = customerAppUserModel.getNic();

                                customerModel = new CustomerModel();
                                customerModel.setCustomerId(customerAppUserModel.getCustomerId());

                                BaseWrapper baseWrapper = new BaseWrapperImpl();
                                baseWrapper.setBasePersistableModel(customerModel);

                                baseWrapper = this.getCommonCommandManager().loadCustomer(baseWrapper);

                                if (baseWrapper.getBasePersistableModel() != null) {
                                    customerModel = (CustomerModel) baseWrapper.getBasePersistableModel();
                                }

                                if (isPayByAccount) {//done

                                    if (customerModel != null) {

                                        checkCustomerCredentials(customerAppUserModel);
                                        segmentId = customerModel.getSegmentId();

                                        accountInfoModel = getCommonCommandManager().getAccountInfoModel(customerAppUserModel.getCustomerId(), PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);

                                    } else {
                                        throw new CommandException("Customer does not exist against mobile number " + customerMobileNumber, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
                                    }

                                } else {

                                    accountInfoModel = getCommonCommandManager().getAccountInfoModel(ThreadLocalAppUser.getAppUserModel().getAppUserId(), PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
                                }

                            } else {

                                if (isPayByAccount) {//done

                                    throw new CommandException("Customer does not exist against mobile number " + customerMobileNumber, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
                                }

                                accountInfoModel = getCommonCommandManager().getAccountInfoModel(ThreadLocalAppUser.getAppUserModel().getAppUserId(), PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);

                                customerCNIC = "-1";
                            }

                            if (DeviceTypeConstantsInterface.ALL_PAY.toString().equals(deviceTypeId) && customerAppUserModel != null && customerAppUserModel.getAccountEnabled() != null) {

                                if (!customerAppUserModel.getAccountEnabled() || customerAppUserModel.getAccountLocked() || customerAppUserModel.getAccountExpired()) {
                                    throw new CommandException("Transaction cannot be processed. The customer account is locked / deactivated.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                                }
                            }

                        } else {

                            customerAppUserModel = new AppUserModel();
                            customerAppUserModel.setMobileNo(walkInCustomerMobileNumber);
                            customerAppUserModel.setNic(walkInCustomerCNIC);

                            accountInfoModel = getCommonCommandManager().getAccountInfoModel(ThreadLocalAppUser.getAppUserModel().getAppUserId(), PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
                        }

                        SegmentModel segmentModel = new SegmentModel();
                        segmentModel.setSegmentId(segmentId);

                        BillPaymentProductDispenser billPaymentProductDispenser = (BillPaymentProductDispenser) getCommonCommandManager().loadProductDispense(workFlowWrapper);

                        workFlowWrapper.setProductVO(productVO);
                        workFlowWrapper.setProductModel(productModel);
							/*Long utilityBillPoolAccountId = PoolAccountConstantsInterface.UTILITY_BILL_POOL_T24_ACCOUNT_ID;
							if(OneBillProductEnum.contains(productId))
								utilityBillPoolAccountId = PoolAccountConstantsInterface.UTILITY_ONE_BILL_POOL_T24_ACCOUNT_ID;
							StakeholderBankInfoModel stakeholderBankInfoModel = new StakeholderBankInfoModel();
							stakeholderBankInfoModel.setStakeholderBankInfoId(utilityBillPoolAccountId);
							SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
							searchBaseWrapper.setBasePersistableModel(stakeholderBankInfoModel);	
							searchBaseWrapper = this.getCommonCommandManager().loadStakeHolderBankInfo(searchBaseWrapper);	
							stakeholderBankInfoModel = (StakeholderBankInfoModel) searchBaseWrapper.getBasePersistableModel();
							
							SupplierBankInfoModel supplierBankInfoModel = new SupplierBankInfoModel();
							supplierBankInfoModel.setSupplierBankInfoId(PoolAccountConstantsInterface.CUSTOMER_POOL_ACCOUNT_ID);
							BaseWrapper baseWrapper = new BaseWrapperImpl();
							baseWrapper.setBasePersistableModel(supplierBankInfoModel);
							supplierBankInfoModel = this.getCommonCommandManager().loadSupplierBankInfo(baseWrapper);*/
                        //fokdjfkdjk

                        SwitchWrapper switchWrapper = new SwitchWrapperImpl();
                        switchWrapper.setFromAccountNo(accountInfoModel.getAccountNo());
//							switchWrapper.setToAccountNo(supplierBankInfoModel.getAccountNo());

                        if (isPayByAccount) {

                            switchWrapper.setSenderCNIC(customerCNIC);
                        } else {

                            switchWrapper.setSenderCNIC(appUserModel.getNic());
                        }

                        switchWrapper.setConsumerNumber(productVO.getConsumerNo());
                        switchWrapper.setUtilityCompanyId(null);
                        workFlowWrapper.setSwitchWrapper(switchWrapper);
                        switchWrapper.setWorkFlowWrapper(workFlowWrapper);

                        workFlowWrapper = getCommonCommandManager().getBillInfo(billPaymentProductDispenser, workFlowWrapper);

                        BillPaymentVO billSaleVO = (BillPaymentVO) workFlowWrapper.getProductVO();

                        if (isPayByAccount) {
//this below line comment for bill info successfull if balance is zero
//                            this.getCommonCommandManager().checkCustomerBalance(customerMobileNumber, billSaleVO.getCurrentBillAmount());
                        }

                        TransactionModel transactionModel = new TransactionModel();
                        transactionModel.setTransactionAmount(billSaleVO.getCurrentBillAmount());
                        TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
                        transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.BILL_SALE_TX);
                        workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
                        workFlowWrapper.setTransactionModel(transactionModel);
                        workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
                        workFlowWrapper.setFromRetailerContactAppUserModel(appUserModel);
                        workFlowWrapper.setCustomField(Integer.valueOf(paymentType));
                        workFlowWrapper.setCustomerAppUserModel(customerAppUserModel);
                        workFlowWrapper.setSegmentModel(segmentModel);


                        RetailerContactModel retailerContactModel = new RetailerContactModel();
                        retailerContactModel.setRetailerContactId(appUserModel.getRetailerContactId());
                        baseWrapper.setBasePersistableModel(retailerContactModel);
                        baseWrapper = getCommonCommandManager().loadRetailerContact(baseWrapper);
                        retailerContactModel = (RetailerContactModel) baseWrapper.getBasePersistableModel();

                        workFlowWrapper.setRetailerContactModel(retailerContactModel);

                        workFlowWrapper.setTaxRegimeModel(retailerContactModel.getTaxRegimeIdTaxRegimeModel());

                        commissionWrapper = getCommonCommandManager().calculateCommission(workFlowWrapper);
                        commissionAmountsHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);

                        SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
                        sma.setCustomerId(appUserModel.getCustomerId());

                        sma.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);

//                        SmartMoneyAccountModel customerSMA = commonCommandManager.getSmartMoneyAccountByCustomerIdAndPaymentModeId(sma);
//                        commonCommandManager.validateBalance(customerAppUserModel, customerSMA, commissionAmountsHolder.getTotalAmount(), true);

                        reasonId = (commissionAmountsHolder.getIsInclusiveCharges() ? 4L : 5L);

                        userDeviceAccountsModel = (UserDeviceAccountsModel) ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();

                        billPaymentVO = billSaleVO;
                        //Following line is to be used in J2meController to be saved in session. This is ultimately used in Bill Payment command
                        ThreadLocalBillInfo.setBillInfo((UtilityBillVO) billSaleVO);
                    } else {
                        logger.error("[AllPayBillInfoCommand.execute] Throwing Exception in Product ID: " + productId + " - Agent AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
                        throw new CommandException(this.getMessageSource().getMessage("checkAccountBalanceCommand.invalidAccountDetail", null, null), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                    }

                } else {
                    logger.error("[AllPayBillInfoCommand.execute] Throwing Exception in Product ID: " + productId + " - Agent AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
                    throw new CommandException(validationErrors.getErrors(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                }

            } catch (WorkFlowException wex) {
                wex.printStackTrace();
                logger.error("[AllPayBillInfoCommand.execute] Throwing Exception in Product ID: " + productId + " - Agent AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
                throw new CommandException(wex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, wex);
            } catch (ClassCastException e) {
                logger.error("[AllPayBillInfoCommand.execute] Throwing Exception in Product ID: " + productId + " - Agent AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
                throw new CommandException(this.getMessageSource().getMessage("command.unexpectedError", null, null), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
            } catch (Exception ex) {
                ex.printStackTrace();
                if (ex.getMessage() != null && !"".equals(ex.getMessage()) && ex.getMessage().indexOf("JTA") != -1) {
                    logger.error("[AllPayBillInfoCommand.execute] Exception in Product ID: " + productId + " - Agent AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
                    throw new CommandException(this.getMessageSource().getMessage("command.unexpectedError", null, null), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
                } else {
                    logger.error("[AllPayBillInfoCommand.execute] Throwing Exception in Product ID: " + productId + " - Agent AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
                    throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
                }
            }
        } else {
            logger.error("[AllPayBillInfoCommand.execute] Throwing Exception in Product ID: " + productId + " - Agent AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
            throw new CommandException(this.getMessageSource().getMessage("getSupplierInfoCommand.invalidAppUserType", null, null), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        }

        if (logger.isDebugEnabled()) {
            logger.debug("End of AllPayBillInfoCommand.execute()");
        }
    }


    public void prepareAgentMateParams(BaseWrapper baseWrapper) {

        retailerMobileNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.CMD_AGNETMATE_AGENT_MOBILE_NUMBER);
        customerMobileNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.CMD_AGNETMATE_CUSTOMER_MOBILE_NUMBER);
        consumerNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.CMD_AGNETMATE_CONSUMER_NUMBER);
        billAmount1 = this.getCommandParameter(baseWrapper, "BAMT");

        baseWrapper.putObject(CommandFieldConstants.KEY_CSCD, consumerNumber);

        appUserModel = ThreadLocalAppUser.getAppUserModel();

        if (appUserModel == null) {
            appUserModel = getAppUserModel(retailerMobileNumber);
        }
    }

    @Override
    public void prepare(BaseWrapper baseWrapper) {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of AllPayBillInfoCommand.prepare()");
        }

        deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        paymentType = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PAYMENT_TYPE);
        productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
//        paymentType = "0";
//        deviceTypeId = "5";
        preparedBaseWrapper = baseWrapper;

        if (DeviceTypeConstantsInterface.ALL_PAY.toString().equals(deviceTypeId) || DeviceTypeConstantsInterface.WEB_SERVICE.toString().equals(deviceTypeId)) {

            prepareAgentMateParams(baseWrapper);
            return;
        }


        consumerNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CSCD);
        retailerMobileNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOB_NO);

        appUserModel = ThreadLocalAppUser.getAppUserModel();

        accountId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACC_ID);
        deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        customerMobileNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOB_NO);
        walkInCustomerCNIC = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CNIC);
        walkInCustomerMobileNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CD_CUSTOMER_MOBILE);

        logger.info("[AllPayBillInfoCommand.prepare] \nLogged In AppUserModel: " + appUserModel.getAppUserId() +
                "\n Product ID:" + productId + " AccountId: " + accountId +
                "\n deviceTypeId: " + deviceTypeId +
                "\n customerMobileNumber: " + customerMobileNumber +
                "\n Walkin Customer CNIC:" + walkInCustomerCNIC +
                "\n Walkin Customer Mobile:" + walkInCustomerMobileNumber);

        if (logger.isDebugEnabled()) {
            logger.debug("End of AllPayBillInfoCommand.prepare()");
        }
    }


    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of AllPayBillInfoCommand.validate()");
        }
        if (!StringUtil.isNullOrEmpty(deviceTypeId) && deviceTypeId.equals(DeviceTypeConstantsInterface.WEB_SERVICE.toString())) {
            BaseWrapper bWrapper = new BaseWrapperImpl();
            ProductModel productModel = new ProductModel();
            productModel.setProductId(Long.valueOf(productId));
//            productModel.setAppUserTypeId(UserTypeConstantsInterface.RETAILER);
//            productModel.setActive(true);
            bWrapper.setBasePersistableModel(productModel);

            try {
                bWrapper = getCommonCommandManager().loadProduct(bWrapper);
            } catch (FrameworkCheckedException e) {
                e.printStackTrace();
                throw new CommandException("Product not found against given product code " + productId, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
            }
            productModel = (ProductModel) bWrapper.getBasePersistableModel();
            if (productModel != null && productModel.getAppUserTypeId() != null && !productModel.getAppUserTypeId().equals(UserTypeConstantsInterface.RETAILER)) {
                throw new CommandException("Product not found against given product id for agent " + productId, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
            }

        } else {
            validationErrors = ValidatorWrapper.doRequired(productId, validationErrors, "Product");
            validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
//		validationErrors = ValidatorWrapper.doRequired(accountId,validationErrors,"Account Id");
//		validationErrors = ValidatorWrapper.doRequired(customerCode,validationErrors,"Customer Code");

            if (!StringUtil.isNullOrEmpty(paymentType) && Integer.parseInt(paymentType) == 0) {//done

                validationErrors = ValidatorWrapper.doRequired(customerMobileNumber, validationErrors, "Customer Mobile Number");
            }

            if (!validationErrors.hasValidationErrors()) {
                validationErrors = ValidatorWrapper.doNumeric(productId, validationErrors, "Product");
                validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
            }

            isPayByAccount = paymentType.equalsIgnoreCase("0") ? Boolean.TRUE : Boolean.FALSE;
        }
        return validationErrors;
    }


    @Override
    public String response() {

        String response = "";

        if (Long.parseLong(deviceTypeId) == DeviceTypeConstantsInterface.ALL_PAY.longValue() || Long.parseLong(deviceTypeId) == DeviceTypeConstantsInterface.USSD.longValue() || Long.parseLong(deviceTypeId) == DeviceTypeConstantsInterface.WEB_SERVICE.longValue()) {

            response = toMobileXMLResponse();

        } else {

            response = toXML();
        }

        return response;
    }


    private String toMobileXMLResponse() {

        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMMMM yyyy hh:mm aaa");
        UtilityBillVO utilityBillVO = ((UtilityBillVO) billPaymentVO);
        Date dueDate = utilityBillVO.getDueDate();

        String BPAID = (utilityBillVO.isBillPaid() ? "1" : "0");

        StringBuilder responseBuilder = new StringBuilder();

        if (utilityBillVO != null && utilityBillVO.getBillAmount() == null) {
            utilityBillVO.setBillAmount(0D);
        }
        if (utilityBillVO != null && utilityBillVO.getLateBillAmount() == null) {
            utilityBillVO.setLateBillAmount(0D);
        }
        String bamt = "";
        String bamtf = "";

        if (productModel.getAmtRequired() != null && productModel.getAmtRequired()) {
            bamt = StringUtil.isNullOrEmpty(billAmount1) ? "0.0" : billAmount1;
            bamtf = Formatter.formatDoubleByPattern(Double.parseDouble(bamt), "#,###.00");
        } else {
            bamt = String.valueOf(replaceNullWithZero(utilityBillVO.getBillAmount()));
            bamtf = Formatter.formatDoubleByPattern(utilityBillVO.getBillAmount(), "#,###.00");
        }
        responseBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);

        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.CMD_AGNETMATE_CUSTOMER_MOBILE_NUMBER, customerMobileNumber));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_PESSENGER_NAME, productModel.getName()));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.CMD_AGNETMATE_CONSUMER_NUMBER, consumerNumber));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_BILL_AMOUNT, bamt));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_BILL_AMOUNT, bamtf));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_LATE_BILL_AMT, utilityBillVO.getLateBillAmount().toString()));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_LATE_BILL_AMT, Formatter.formatDoubleByPattern(utilityBillVO.getLateBillAmount(), "#,###.00")));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_BILL_PAID, BPAID));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CNIC, customerCNIC));

        if (dueDate != null) {

            DateTime nowDate = new DateTime();
            DateTime _dueDate = new DateTime(dueDate).withTime(23, 59, 59, 0);

            responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.CMD_AGNETMATE_DUEDATER, dateTimeFormatter.print(_dueDate)));
            responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.CMD_AGNETMATE_DUEDATEF, sdf.format(_dueDate.toDate())));
            responseBuilder.append(MiniXMLUtil.createXMLParameterTag("ISOVERDUE", nowDate.isAfter(_dueDate) ? "1" : "0"));
        } else {
            responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.CMD_AGNETMATE_DUEDATER, "N/A"));
            responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.CMD_AGNETMATE_DUEDATEF, "N/A"));
            responseBuilder.append(MiniXMLUtil.createXMLParameterTag("ISOVERDUE", "1"));
        }


        responseBuilder.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);

        logger.info(responseBuilder.toString());
        return responseBuilder.toString();
    }

    private String toXML() {

        if (logger.isDebugEnabled()) {
            logger.debug("Start of AllPayBillInfoCommand.toXML()");
        }

        StringBuilder strBuilder = new StringBuilder();

        if (commissionAmountsHolder != null) {

            strBuilder.append(TAG_SYMBOL_OPEN)
                    .append(TAG_PARAMS)
                    .append(TAG_SYMBOL_CLOSE)
                    .append(TAG_SYMBOL_OPEN)
                    .append(TAG_PARAM)
                    .append(TAG_SYMBOL_SPACE)
                    .append(ATTR_PARAM_NAME)
                    .append(TAG_SYMBOL_EQUAL)
                    .append(TAG_SYMBOL_QUOTE)
                    .append(CommandFieldConstants.KEY_COMM_AMOUNT)
                    .append(TAG_SYMBOL_QUOTE)
                    .append(TAG_SYMBOL_CLOSE)

                    .append(replaceNullWithZero((commissionAmountsHolder.getTotalCommissionAmount())))

                    .append(TAG_SYMBOL_OPEN)
                    .append(TAG_SYMBOL_SLASH)
                    .append(TAG_PARAM)
                    .append(TAG_SYMBOL_CLOSE)
                    .append(TAG_SYMBOL_OPEN)
                    .append(TAG_PARAM)
                    .append(TAG_SYMBOL_SPACE)
                    .append(ATTR_PARAM_NAME)
                    .append(TAG_SYMBOL_EQUAL)
                    .append(TAG_SYMBOL_QUOTE)
                    .append(CommandFieldConstants.KEY_TX_PROCESS_AMNT)
                    .append(TAG_SYMBOL_QUOTE)
                    .append(TAG_SYMBOL_CLOSE)

                    .append(replaceNullWithZero(commissionAmountsHolder.getTransactionProcessingAmount()))

                    .append(TAG_SYMBOL_OPEN)
                    .append(TAG_SYMBOL_SLASH)
                    .append(TAG_PARAM)
                    .append(TAG_SYMBOL_CLOSE);


            /////////////////////////adding customer CNIC////////////////////

            if (null != customerCNIC && !"".equals(customerCNIC)) {

                strBuilder.append(TAG_SYMBOL_OPEN)
                        .append(TAG_PARAM)
                        .append(TAG_SYMBOL_SPACE)
                        .append(ATTR_PARAM_NAME)
                        .append(TAG_SYMBOL_EQUAL)
                        .append(TAG_SYMBOL_QUOTE)
                        .append(CommandFieldConstants.KEY_CNIC)
                        .append(TAG_SYMBOL_QUOTE)
                        .append(TAG_SYMBOL_CLOSE)
                        .append(customerCNIC)
                        .append(TAG_SYMBOL_OPEN)
                        .append(TAG_SYMBOL_SLASH)
                        .append(TAG_PARAM)
                        .append(TAG_SYMBOL_CLOSE);

            }


            if (UtilityCompanyEnum.contains(productId) ||
                    InternetCompanyEnum.WATEEN.equals(productId) || InternetCompanyEnum.WITRIBE.equals(productId) ||
                    InternetCompanyEnum.EVO_POSTPAID.equals(productId) || InternetCompanyEnum.ZONG_POSTPAID.equals(productId)) {

                Date dueDate = ((UtilityBillVO) billPaymentVO).getDueDate();
                strBuilder.append(TAG_SYMBOL_OPEN)
                        .append(TAG_PARAM)
                        .append(TAG_SYMBOL_SPACE)
                        .append(ATTR_PARAM_NAME)
                        .append(TAG_SYMBOL_EQUAL)
                        .append(TAG_SYMBOL_QUOTE)
                        .append(CommandFieldConstants.KEY_BILL_DUE_DATE)
                        .append(TAG_SYMBOL_QUOTE)
                        .append(TAG_SYMBOL_CLOSE)
                        .append(dateTimeFormatter.print(dueDate.getTime()))
                        .append(TAG_SYMBOL_OPEN)
                        .append(TAG_SYMBOL_SLASH)
                        .append(TAG_PARAM)
                        .append(TAG_SYMBOL_CLOSE);
                strBuilder.append(billPaymentVO.responseXML());

            } else {
                strBuilder.append(billPaymentVO.responseXML());
            }

            strBuilder.append(TAG_SYMBOL_OPEN)
                    .append(TAG_PARAM)
                    .append(TAG_SYMBOL_SPACE)
                    .append(ATTR_PARAM_NAME)
                    .append(TAG_SYMBOL_EQUAL)
                    .append(TAG_SYMBOL_QUOTE)
                    .append(CommandFieldConstants.KEY_TOTAL_AMOUNT)
                    .append(TAG_SYMBOL_QUOTE)
                    .append(TAG_SYMBOL_CLOSE)

                    .append(replaceNullWithZero(commissionAmountsHolder.getTotalAmount()))

                    .append(TAG_SYMBOL_OPEN)
                    .append(TAG_SYMBOL_SLASH)
                    .append(TAG_PARAM)
                    .append(TAG_SYMBOL_CLOSE)

                    .append(TAG_SYMBOL_OPEN)
                    .append(TAG_PARAM)
                    .append(TAG_SYMBOL_SPACE)
                    .append(ATTR_PARAM_NAME)
                    .append(TAG_SYMBOL_EQUAL)
                    .append(TAG_SYMBOL_QUOTE)
                    .append(CommandFieldConstants.KEY_ACC_ID)
                    .append(TAG_SYMBOL_QUOTE)
                    .append(TAG_SYMBOL_CLOSE)

                    .append(accountId)

                    .append(TAG_SYMBOL_OPEN)
                    .append(TAG_SYMBOL_SLASH)
                    .append(TAG_PARAM)
                    .append(TAG_SYMBOL_CLOSE)


                    .append(TAG_SYMBOL_OPEN)
                    .append(TAG_PARAM)
                    .append(TAG_SYMBOL_SPACE)
                    .append(ATTR_PARAM_NAME)
                    .append(TAG_SYMBOL_EQUAL)
                    .append(TAG_SYMBOL_QUOTE)
                    .append(CommandFieldConstants.KEY_FORMATED_COMM_AMOUNT)
                    .append(TAG_SYMBOL_QUOTE)
                    .append(TAG_SYMBOL_CLOSE)

                    .append(Formatter.formatNumbers(commissionAmountsHolder.getTotalCommissionAmount()))

                    .append(TAG_SYMBOL_OPEN)
                    .append(TAG_SYMBOL_SLASH)
                    .append(TAG_PARAM)
                    .append(TAG_SYMBOL_CLOSE)
                    .append(TAG_SYMBOL_OPEN)
                    .append(TAG_PARAM)
                    .append(TAG_SYMBOL_SPACE)
                    .append(ATTR_PARAM_NAME)
                    .append(TAG_SYMBOL_EQUAL)
                    .append(TAG_SYMBOL_QUOTE)
                    .append(CommandFieldConstants.KEY_FORMATED_TX_PROCESS_AMNT)
                    .append(TAG_SYMBOL_QUOTE)
                    .append(TAG_SYMBOL_CLOSE)

                    .append(Formatter.formatNumbers(commissionAmountsHolder.getTransactionProcessingAmount()))

                    .append(TAG_SYMBOL_OPEN)
                    .append(TAG_SYMBOL_SLASH)
                    .append(TAG_PARAM)
                    .append(TAG_SYMBOL_CLOSE)

                    .append(TAG_SYMBOL_OPEN)
                    .append(TAG_PARAM)
                    .append(TAG_SYMBOL_SPACE)
                    .append(ATTR_PARAM_NAME)
                    .append(TAG_SYMBOL_EQUAL)
                    .append(TAG_SYMBOL_QUOTE)
                    .append(CommandFieldConstants.KEY_FORMATED_TOTAL_AMOUNT)
                    .append(TAG_SYMBOL_QUOTE)
                    .append(TAG_SYMBOL_CLOSE)

                    .append(Formatter.formatNumbers(commissionAmountsHolder.getTotalAmount()))

                    .append(TAG_SYMBOL_OPEN)
                    .append(TAG_SYMBOL_SLASH)
                    .append(TAG_PARAM)
                    .append(TAG_SYMBOL_CLOSE)


                    .append(TAG_SYMBOL_OPEN)
                    .append(TAG_PARAM)
                    .append(TAG_SYMBOL_SPACE)
                    .append(ATTR_PARAM_NAME)
                    .append(TAG_SYMBOL_EQUAL)
                    .append(TAG_SYMBOL_QUOTE)
                    .append("REASON_ID")
                    .append(TAG_SYMBOL_QUOTE)
                    .append(TAG_SYMBOL_CLOSE)

                    .append(this.reasonId)

                    .append(TAG_SYMBOL_OPEN)
                    .append(TAG_SYMBOL_SLASH)
                    .append(TAG_PARAM)
                    .append(TAG_SYMBOL_CLOSE);

            if (null != userDeviceAccountsModel && userDeviceAccountsModel.getCommissioned()) {

                userDeviceAccountsModel = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
                strBuilder.append(TAG_SYMBOL_OPEN)
                        .append(TAG_PARAM)
                        .append(TAG_SYMBOL_SPACE)
                        .append(ATTR_PARAM_NAME)
                        .append(TAG_SYMBOL_EQUAL)
                        .append(TAG_SYMBOL_QUOTE)
                        .append(CommandFieldConstants.KEY_DISCOUNT_AMOUNT)
                        .append(TAG_SYMBOL_QUOTE)
                        .append(TAG_SYMBOL_CLOSE)

                        .append(replaceNullWithZero((discountAmount)))

                        .append(TAG_SYMBOL_OPEN)
                        .append(TAG_SYMBOL_SLASH)
                        .append(TAG_PARAM)
                        .append(TAG_SYMBOL_CLOSE)
                        .append(TAG_SYMBOL_OPEN)
                        .append(TAG_PARAM)
                        .append(TAG_SYMBOL_SPACE)
                        .append(ATTR_PARAM_NAME)
                        .append(TAG_SYMBOL_EQUAL)
                        .append(TAG_SYMBOL_QUOTE)
                        .append(CommandFieldConstants.KEY_FORMATED_DISCOUNT_AMOUNT)
                        .append(TAG_SYMBOL_QUOTE)
                        .append(TAG_SYMBOL_CLOSE)

                        .append(Formatter.formatNumbers(discountAmount))

                        .append(TAG_SYMBOL_OPEN)
                        .append(TAG_SYMBOL_SLASH)
                        .append(TAG_PARAM)
                        .append(TAG_SYMBOL_CLOSE);
            }

            UtilityBillVO utilityBillVO = (UtilityBillVO) billPaymentVO;

            AFTER_DUE_DATE:
            {
                strBuilder.append(TAG_SYMBOL_OPEN)
                        .append(TAG_PARAM)
                        .append(TAG_SYMBOL_SPACE)
                        .append(ATTR_PARAM_NAME)
                        .append(TAG_SYMBOL_EQUAL)
                        .append(TAG_SYMBOL_QUOTE)
                        .append("AFTER_DUE_DATE")
                        .append(TAG_SYMBOL_QUOTE)
                        .append(TAG_SYMBOL_CLOSE)
                        .append(utilityBillVO.getLateBillAmount())
                        .append(TAG_SYMBOL_OPEN)
                        .append(TAG_SYMBOL_SLASH)
                        .append(TAG_PARAM)
                        .append(TAG_SYMBOL_CLOSE);
            }

            strBuilder.append(TAG_SYMBOL_OPEN)
                    .append(TAG_SYMBOL_SLASH)
                    .append(TAG_PARAMS)
                    .append(TAG_SYMBOL_CLOSE);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("End of AllPayBillInfoCommand.toXML()");
        }
        return strBuilder.toString();
    }


    private SmartMoneyAccountModel getSmartMoneyAccountModel(Long retailerContactId) throws FrameworkCheckedException {

        SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
        smartMoneyAccountModel.setRetailerContactId(appUserModel.getRetailerContactId());
        smartMoneyAccountModel.setChangePinRequired(Boolean.TRUE);
        smartMoneyAccountModel.setDeleted(Boolean.FALSE);
        smartMoneyAccountModel.setActive(Boolean.TRUE);

        baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
        baseWrapper = getCommonCommandManager().loadOLASmartMoneyAccount(baseWrapper);
        return (SmartMoneyAccountModel) baseWrapper.getBasePersistableModel();
    }


    private AppUserModel getAppUserModel(String _retailerMobileNumber) {

        AppUserModel _appUserModel = new AppUserModel();
        _appUserModel.setMobileNo(_retailerMobileNumber);
        _appUserModel.setAppUserTypeId(UserTypeConstantsInterface.RETAILER);

        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setBasePersistableModel(_appUserModel);

        try {

            searchBaseWrapper = getCommonCommandManager().loadAppUserByMobileNumberAndType(searchBaseWrapper);

        } catch (FrameworkCheckedException e) {

            e.printStackTrace();
        }

        return (AppUserModel) searchBaseWrapper.getBasePersistableModel();
    }

    private AppUserModel getCustomerAppUserModel(String mobileNumber) throws FrameworkCheckedException {

        AppUserModel customerAppUserModel = new AppUserModel();
        customerAppUserModel.setMobileNo(mobileNumber);
        customerAppUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);

        SearchBaseWrapper sBaseWrapper = new SearchBaseWrapperImpl();
        sBaseWrapper.setBasePersistableModel(customerAppUserModel);
        sBaseWrapper = getCommonCommandManager().loadAppUserByMobileNumberAndType(sBaseWrapper);

        customerAppUserModel = (AppUserModel) sBaseWrapper.getBasePersistableModel();

        if (customerAppUserModel != null && customerAppUserModel.getAccountEnabled() != null && isPayByAccount) {

            if (!customerAppUserModel.getAccountEnabled() || customerAppUserModel.getAccountLocked() || customerAppUserModel.getAccountExpired()) {
                throw new CommandException("Transaction cannot be processed. The customer account is locked / deactivated.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
            }
        }

        return customerAppUserModel;
    }


    private void checkCustomerCredentials(AppUserModel senderAppUserModel) throws FrameworkCheckedException {

        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.MOBILE);
        baseWrapper.setBasePersistableModel(senderAppUserModel);

        validationErrors = getCommonCommandManager().checkCustomerCredentials(baseWrapper);

        if (validationErrors.hasValidationErrors()) {

            throw new CommandException(validationErrors.getErrors(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        }
    }
}
