package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.fonepay.common.FonePayConstants;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.integration.dispenser.BillPaymentProductDispenser;
import com.inov8.microbank.server.service.integration.vo.BillPaymentVO;
import com.inov8.microbank.server.service.integration.vo.UtilityBillVO;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.inov8.microbank.common.util.XMLConstants.*;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;

public class BookMeInfoCommand extends BaseCommand {

    private final Log logger = LogFactory.getLog(BookMeInfoCommand.class);

    private String customerMobileNo;
    private String transactionAmount;
    private String productId;
    private String paymentType;
    private String billAmount;
    protected String accountId;
    private String customerCNIC;
    private String serviceProvider;
    private WorkFlowWrapper workFlowWrapper;
    private String orderReferId;
    private Boolean isPayByAccount = Boolean.FALSE;
    private AppUserModel appUserModel;
    private UserDeviceAccountsModel userDeviceAccountsModel;
    private ProductModel productModel;
    private BillPaymentVO billPaymentVO;
    private BaseWrapper baseWrapper;
    private CommissionAmountsHolder commissionAmountsHolder;
    private String serviceType;
    private String serviceProviderName;
    private String baseFare;
    private String totalApproxAmount;
    private String discount;
    private String taxes;
    private String fee;
    private String bookMeName;
    private String bookMeCnic;
    private String bookMeMobileNo;
    private String bookMeEmail;

    @Override
    public void prepare(BaseWrapper baseWrapper) {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CustomerBookMeInfoCommand.prepare()");
        }

        //********************************Request Parameters**************************************************
        deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
        customerMobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
        paymentType = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PAYMENT_TYPE);
        billAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BILL_AMOUNT);
        orderReferId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ORDER_ID);
        serviceType = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_SERVICE_TYPE);
        serviceProviderName = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_SERVICE_PROVIDER_NAME);
        baseFare = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BASE_FARE);
        totalApproxAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TOTAL_APPROX_AMOUNT);
        discount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DISCOUNT_AMOUNT);
        taxes = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TAX);
        fee = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_FEE);
        bookMeCnic = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BOOKME_CNIC);
        bookMeName = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BOOKME_NAME);
        bookMeEmail = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BOOKME_EMAIL);
        bookMeMobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BOOKME_MOBILE_NO);
//        channelId=this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_CHANNEL_ID);
        //****************************************************************************************************
        baseWrapper = baseWrapper;
        appUserModel = ThreadLocalAppUser.getAppUserModel();
        ThreadLocalAppUser.setAppUserModel(appUserModel);

        logger.info("[CustomerBookMeInfoCommand.prepare] \nLogged In AppUserModel: " + appUserModel.getAppUserId() +
                "\n Product ID:" + productId + " AccountId: " + accountId +
                "\n deviceTypeId: " + deviceTypeId +
                "\n customerMobileNumber: " + customerMobileNo);

        if (logger.isDebugEnabled()) {
            logger.debug("End of CustomerBookMeInfoCommand.prepare()");
        }
    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CustomerBookMeInfoCommand.validate()");
        }

        validationErrors = ValidatorWrapper.doRequired(customerMobileNo, validationErrors, "Customer Mobile No");
        validationErrors = ValidatorWrapper.doRequired(billAmount, validationErrors, "Bill Amount");
        validationErrors = ValidatorWrapper.doRequired(productId, validationErrors, "Product");
        validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");

        if (!StringUtil.isNullOrEmpty(paymentType) && Integer.parseInt(paymentType) == 0) {

            validationErrors = ValidatorWrapper.doRequired(customerMobileNo, validationErrors, "Customer Mobile Number");
        }

        if (!validationErrors.hasValidationErrors()) {
            validationErrors = ValidatorWrapper.doNumeric(customerMobileNo, validationErrors, "Customer Mobile No");
            validationErrors = ValidatorWrapper.doNumeric(billAmount, validationErrors, "Bill Amount");
            validationErrors = ValidatorWrapper.doNumeric(productId, validationErrors, "Product");
            validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");

        }

        if (logger.isDebugEnabled()) {
            logger.debug("End of CustomerBookMeInfoCommand.validate()");
        }

        return validationErrors;
    }

    @Override
    public void execute() throws CommandException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CustomerBookMeInfoCommand.execute()");
        }

        ThreadLocalAppUser.setAppUserModel(appUserModel);

        workFlowWrapper = new WorkFlowWrapperImpl();
        workFlowWrapper.setHandlerModel(handlerModel);
        workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);

        CommissionWrapper commissionWrapper;
        baseWrapper = new BaseWrapperImpl();


        if (appUserModel.getCustomerId() != null) {
            try {
                ValidationErrors validationErrors = getCommonCommandManager().checkActiveAppUser(appUserModel);

                if (!validationErrors.hasValidationErrors()) {

                    SmartMoneyAccountModel smartMoneyAccountModel = getSmartMoneyAccountModel(appUserModel.getCustomerId());

                    if (smartMoneyAccountModel.getName() != null &&

                            smartMoneyAccountModel.getCustomerId().equals(appUserModel.getCustomerId())) {
                        productModel = new ProductModel();
                        baseWrapper.getDataMap().putAll(baseWrapper.getDataMap());
                        baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, productId);
                        productModel.setProductId(Long.parseLong(productId));
                        baseWrapper.setBasePersistableModel(productModel);
                        baseWrapper = commonCommandManager.loadProduct(baseWrapper);
                        productModel = (ProductModel) baseWrapper.getBasePersistableModel();
                        if (productModel == null) {
                            throw new CommandException("Product not loaded", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                        }
                        workFlowWrapper.setProductModel(productModel);
                        AppUserModel customerAppUserModel = null;
                        CustomerModel customerModel = null;

                        Long segmentId = CommissionConstantsInterface.DEFAULT_SEGMENT_ID;
                        AccountInfoModel accountInfoModel = null;
                        if (!StringUtil.isNullOrEmpty(customerMobileNo)) {

                            customerAppUserModel = this.getCustomerAppUserModel(customerMobileNo);
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

                                        if (DeviceTypeConstantsInterface.ALL_PAY.toString().equals(deviceTypeId)) {
                                            checkCustomerCredentials(customerAppUserModel);
                                        }
                                        segmentId = customerModel.getSegmentId();

                                        accountInfoModel = getCommonCommandManager().getAccountInfoModel(customerAppUserModel.getCustomerId(), PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);

                                    } else {
                                        throw new CommandException("Customer does not exist against mobile number " + customerMobileNo, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
                                    }

                                } else {

                                    accountInfoModel = getCommonCommandManager().getAccountInfoModel(appUserModel.getCustomerId(), PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
                                }

                            } else {

                                if (isPayByAccount) {//done

                                    throw new CommandException("Customer does not exist against mobile number " + customerMobileNo, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
                                }

                                accountInfoModel = getCommonCommandManager().getAccountInfoModel(ThreadLocalAppUser.getAppUserModel().getAppUserId(), PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);

                                customerCNIC = "-1";
                            }

                            if ((DeviceTypeConstantsInterface.ALL_PAY.toString().equals(deviceTypeId) || DeviceTypeConstantsInterface.WEB_SERVICE.toString().equals(deviceTypeId)) && customerAppUserModel != null && customerAppUserModel.getAccountEnabled() != null) {

                                if (!customerAppUserModel.getAccountEnabled() || customerAppUserModel.getAccountLocked() || customerAppUserModel.getAccountExpired()) {
                                    throw new CommandException("Transaction cannot be processed. The customer account is locked / deactivated.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                                }
                            }


                        } else {

                            customerAppUserModel = new AppUserModel();
                            customerAppUserModel.setMobileNo(customerMobileNo);
                            //customerAppUserModel.setNic(walkInCustomerCNIC);
                            accountInfoModel = getCommonCommandManager().getAccountInfoModel(ThreadLocalAppUser.getAppUserModel().getAppUserId(), PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
                        }
                        SegmentModel segmentModel = new SegmentModel();
                        segmentModel.setSegmentId(segmentId);
                        workFlowWrapper.setProductModel(productModel);
                        SwitchWrapper switchWrapper = new SwitchWrapperImpl();
                        switchWrapper.setFromAccountNo(accountInfoModel.getAccountNo());


                        if (isPayByAccount) {

                            switchWrapper.setSenderCNIC(customerCNIC);
                        } else {

                            switchWrapper.setSenderCNIC(appUserModel.getNic());
                        }

                        workFlowWrapper.setSwitchWrapper(switchWrapper);
                        switchWrapper.setWorkFlowWrapper(workFlowWrapper);
                        this.getCommonCommandManager().checkCustomerBalance(customerMobileNo, Double.parseDouble(billAmount));
                        TransactionModel transactionModel = new TransactionModel();
                        transactionModel.setTransactionAmount(Double.valueOf(billAmount));
                        TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
                        transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.BOOK_ME_TX);//bookme transaction constant add karna ha
                        workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
                        workFlowWrapper.setTransactionModel(transactionModel);
                        workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
                        workFlowWrapper.setFromRetailerContactAppUserModel(appUserModel);
//                        if(!(channelId.equals(FonePayConstants.APIGEE_CHANNEL))) {
//                            workFlowWrapper.setCustomField(Integer.valueOf(paymentType));
//                        }
                        workFlowWrapper.setCustomerAppUserModel(customerAppUserModel);
                        workFlowWrapper.setSegmentModel(segmentModel);


                        commissionWrapper = commonCommandManager.calculateCommission(workFlowWrapper);
                        commissionAmountsHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
                        userDeviceAccountsModel = (UserDeviceAccountsModel) ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();


                    } else {
                        logger.error("[CustomerBookMeInfoCommand.execute] Throwing Exception in Product ID: " + productId + " - Agent AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
                        throw new CommandException(this.getMessageSource().getMessage("checkAccountBalanceCommand.invalidAccountDetail", null, null), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                    }
                } else {
                    logger.error("[CustomerBookMeInfoCommand.execute] Throwing Exception in Product ID: " + productId + " - Customer AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
                    throw new CommandException(validationErrors.getErrors(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                }
            } catch (WorkFlowException wex) {
                wex.printStackTrace();
                logger.error("[CustomerBookMeInfoCommand.execute] Throwing Exception in Product ID: " + productId + " - Customer AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
                throw new CommandException(wex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, wex);
            } catch (ClassCastException e) {
                logger.error("[CustomerBookMeInfoCommand.execute] Throwing Exception in Product ID: " + productId + " - Customer AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
                throw new CommandException(this.getMessageSource().getMessage("command.unexpectedError", null, null), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
            } catch (Exception ex) {
                ex.printStackTrace();
                if (ex.getMessage() != null && !"".equals(ex.getMessage()) && ex.getMessage().indexOf("JTA") != -1) {
                    logger.error("[CustomerBookMeInfoCommand.execute] Exception in Product ID: " + productId + " - Customer AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
                    throw new CommandException(this.getMessageSource().getMessage("command.unexpectedError", null, null), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
                } else {
                    logger.error("[CustomerBookMeInfoCommand.execute] Throwing Exception in Product ID: " + productId + " - Customer AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
                    throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
                }
            }
        } else {
            logger.error("[CustomerBookMeInfoCommand.execute] Throwing Exception in Product ID: " + productId + " - Agent AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
            throw new CommandException(this.getMessageSource().getMessage("getSupplierInfoCommand.invalidAppUserType", null, null), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("End of CustomerBookMeInfoCommand.execute()");
        }
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


    private SmartMoneyAccountModel getSmartMoneyAccountModel(Long customerId) throws FrameworkCheckedException {


        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        SmartMoneyAccountModel customerSMAModel = new SmartMoneyAccountModel();
        customerSMAModel.setCustomerId(appUserModel.getCustomerId());
        customerSMAModel.setActive(Boolean.TRUE);
        customerSMAModel.setDeleted(Boolean.FALSE);
        customerSMAModel.setDefAccount(Boolean.TRUE);
        searchBaseWrapper.setBasePersistableModel(customerSMAModel);

        searchBaseWrapper = getCommonCommandManager().loadSMAExactMatch(searchBaseWrapper);

        @SuppressWarnings("rawtypes")
        CustomList smaList = searchBaseWrapper.getCustomList();
        if (smaList != null && smaList.getResultsetList() != null && smaList.getResultsetList().size() > 0) {
            customerSMAModel = (SmartMoneyAccountModel) smaList.getResultsetList().get(0);
        }

        return customerSMAModel;
    }

    @Override
    public String response() {
        String response = "";


        response = toXML();

        return response;
    }


    private String toXML() {

        if (logger.isDebugEnabled()) {
            logger.debug("Start of CustomerBillPaymentsInfoCommand.toXML()");
        }

        double billAmount1 = StringUtil.isNullOrEmpty(billAmount) ? 0.0D : Double.parseDouble(billAmount);

        StringBuilder responseBuilder = new StringBuilder();


        responseBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);

        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CUSTOMER_MOBILE, customerMobileNo));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_PRODUCT_NAME, productModel.getName()));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_BILL_AMOUNT, String.valueOf(replaceNullWithZero(billAmount1))));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_BILL_AMOUNT, Formatter.formatDoubleByPattern(billAmount1, "#,###.00")));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TX_PROCESS_AMNT, "" + replaceNullWithZero(commissionAmountsHolder.getTransactionProcessingAmount())));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_COMM_AMOUNT, "" + replaceNullWithZero(commissionAmountsHolder.getTotalCommissionAmount())));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TOTAL_AMOUNT, "" + replaceNullWithZero(commissionAmountsHolder.getTotalAmount())));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_ORDER_ID, orderReferId));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_SERVICE_PROVIDER_NAME, serviceProviderName));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_SERVICE_TYPE, serviceType));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_BOOKME_NAME, bookMeName));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_BOOKME_CNIC, bookMeCnic));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_BOOKME_EMAIL, bookMeEmail));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_BOOKME_MOBILE_NO, bookMeMobileNo));
        responseBuilder.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);

        return responseBuilder.toString();
    }
}
