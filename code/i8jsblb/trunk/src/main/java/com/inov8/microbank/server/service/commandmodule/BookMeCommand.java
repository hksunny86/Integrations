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
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.fonepay.common.FonePayConstants;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import com.inov8.microbank.server.service.integration.vo.BookMeTransactionVO;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.integration.vo.UtilityBillVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Date;

import static com.inov8.microbank.common.util.XMLConstants.*;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_DATE;

public class BookMeCommand extends BaseCommand {

    private final Log logger = LogFactory.getLog(BookMeCommand.class);

    private AppUserModel appUserModel;
    private String productId;
    private String txProcessingAmount;
    private String deviceTypeId;
    private String commissionAmount;
    private String totalAmount;
    private String billAmount;
    private Long transactionTypeId = null;
    private TransactionModel transactionModel;
    private ProductModel productModel;
    private BaseWrapper baseWrapper;
    private WorkFlowWrapper workFlowWrapper;
    private String channelId;
    private String thirdPartyTransactionId;
    private Double balance = 0D;
    private String accountTitle = "";
    private String accountId;
    private UserDeviceAccountsModel userDeviceAccountsModel;
    private String consumerNumber;
    private String custMobileNo;
    private ProductVO billPaymentVO = null;

    //    private ProductVO billPaymentVO = null;
    private Long segmentId = CommissionConstantsInterface.DEFAULT_SEGMENT_ID;
    private String terminalId;
    private String orderReferId;
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
        appUserModel = ThreadLocalAppUser.getAppUserModel();
        deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
        custMobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
        billAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BILL_AMOUNT);
        txProcessingAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_PROCESS_AMNT);
        commissionAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_COMM_AMOUNT);
        totalAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TOTAL_AMOUNT);
        orderReferId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ORDER_ID);
        serviceType = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_SERVICE_TYPE);
        serviceProviderName = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_SERVICE_PROVIDER_NAME);
        baseFare=this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_BASE_FARE);
        totalApproxAmount=this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_TOTAL_APPROX_AMOUNT);
        discount=this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_DISCOUNT_AMOUNT);
        taxes=this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_TAX);
        fee=this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_FEE);
        bookMeCnic = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BOOKME_CNIC);
        bookMeName = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BOOKME_NAME);
        bookMeEmail = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BOOKME_EMAIL);
        bookMeMobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BOOKME_MOBILE_NO);
        this.baseWrapper = baseWrapper;

        logger.info("[BookMe.prepare] Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + custMobileNo);
    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        if (!StringUtil.isNullOrEmpty(channelId) && channelId.equals(FonePayConstants.APIGEE_CHANNEL)) {
            BaseWrapper bWrapper = new BaseWrapperImpl();
            ProductModel productModel = new ProductModel();
            productModel.setProductCode(productId);
            productModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
            productModel.setActive(true);
            bWrapper.setBasePersistableModel(productModel);
            try {
                bWrapper = getCommonCommandManager().searchProduct(bWrapper);
            } catch (FrameworkCheckedException e) {
                e.printStackTrace();
                throw new CommandException("Product not found against given product code " + productId, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
            }
            productModel = (ProductModel) bWrapper.getBasePersistableModel();
            if (productModel.getProductId() != null) {
                productId = productModel.getProductId().toString();
            }
        } else {
            validationErrors = ValidatorWrapper.doRequired(productId, validationErrors, "Product");
            validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
            validationErrors = ValidatorWrapper.doRequired(billAmount, validationErrors, "Bill Amount");
            validationErrors = ValidatorWrapper.doRequired(custMobileNo, validationErrors, "Customer Mobile Number");
            if (!validationErrors.hasValidationErrors()) {
                validationErrors = ValidatorWrapper.doNumeric(productId, validationErrors, "Product");
                validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
            }
        }
        return validationErrors;
    }

    @Override
    public void execute() throws CommandException {
        CommonCommandManager commonCommandManager = this.getCommonCommandManager();
        workFlowWrapper = new WorkFlowWrapperImpl();

        try {
            ValidationErrors validationErrors = commonCommandManager.checkActiveAppUser(appUserModel);
            if (validationErrors.hasValidationErrors()) {
                logger.error("[BookMe.prepare] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + custMobileNo);
                throw new CommandException(validationErrors.getErrors(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
            }
            productModel = new ProductModel();
            productModel.setProductId(Long.parseLong(productId));
            CustomerModel customerModel = null;
//			AppUserModel customerAppUserModel = this.getAppUserModel(custMobileNo);
            if (appUserModel != null) {
                customerModel = new CustomerModel();
                customerModel.setCustomerId(appUserModel.getCustomerId());

                BaseWrapper bWrapper = new BaseWrapperImpl();
                bWrapper.setBasePersistableModel(customerModel);
                bWrapper = this.getCommonCommandManager().loadCustomer(bWrapper);
                if (bWrapper.getBasePersistableModel() != null) {
                    customerModel = (CustomerModel) bWrapper.getBasePersistableModel();
                    segmentId = customerModel.getSegmentId();
                }
                workFlowWrapper.setTransactionAmount(Double.parseDouble(billAmount));
                if (!StringUtil.isNullOrEmpty(txProcessingAmount)) {
                    workFlowWrapper.setTxProcessingAmount(Double.parseDouble(txProcessingAmount));
                } else {
                    workFlowWrapper.setTxProcessingAmount(0.0D);
                }
                if (!StringUtil.isNullOrEmpty(billAmount)) {
                    workFlowWrapper.setBillAmount(Double.parseDouble(billAmount));
                } else {
                    workFlowWrapper.setBillAmount(0.0D);
                }
                if (!StringUtil.isNullOrEmpty(commissionAmount)) {
                    workFlowWrapper.setTotalCommissionAmount(Double.parseDouble(commissionAmount));
                } else {
                    workFlowWrapper.setTotalCommissionAmount(0.0D);
                }
                baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, productId);
                billPaymentVO = commonCommandManager.loadProductVO(baseWrapper);

                productModel = (ProductModel) baseWrapper.getBasePersistableModel();
//                if(billPaymentVO == null) {
//                    throw new CommandException(this.getMessageSource().getMessage("billPaymentCommand.productVoLoadedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
//                }
//                else if(productModel != null && Double.parseDouble(billAmount) < productModel.getMinLimit())
//                    throw new CommandException("Bill Amount must be Greater/Equal to " + productModel.getMinLimit().toString(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
//                else if(productModel != null && Double.parseDouble(billAmount) > productModel.getMaxLimit())
//                    throw new CommandException("Bill Amount must be Lesser/Equal to " + productModel.getMaxLimit().toString(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());

                BookMeTransactionVO bookMeTransactionVO = (BookMeTransactionVO) billPaymentVO;

                bookMeTransactionVO.setTransactionAmount(Double.valueOf(billAmount));
                bookMeTransactionVO.setTotalAmount(Double.valueOf(totalAmount));
                bookMeTransactionVO.setTransactionProcessingAmount(Double.valueOf(txProcessingAmount));
                bookMeTransactionVO.setCustomerName(appUserModel.getFirstName());
                bookMeTransactionVO.setTax(Double.valueOf(taxes));
                bookMeTransactionVO.setBaseFare(Double.parseDouble(baseFare));
                bookMeTransactionVO.setFee(Double.valueOf(fee));
                bookMeTransactionVO.setDiscount(Double.parseDouble(discount));
                bookMeTransactionVO.setBookMeCustomerCnic(bookMeCnic);
                bookMeTransactionVO.setBookMeCustomerEmail(bookMeEmail);
                bookMeTransactionVO.setBookMeCustomerMobileNo(bookMeMobileNo);
                bookMeTransactionVO.setBookMeCustomerName(bookMeName);

                workFlowWrapper.setCustomerAppUserModel(appUserModel);
                SegmentModel segmentModel = new SegmentModel();
                segmentModel.setSegmentId(segmentId);
                workFlowWrapper.setSegmentModel(segmentModel);
                workFlowWrapper.setProductVO(bookMeTransactionVO);
                workFlowWrapper.setIsCustomerInitiatedTransaction(true);
                transactionTypeId = TransactionTypeConstantsInterface.BOOK_ME_TX;
                //**********************************************************************
                TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
                transactionTypeModel.setTransactionTypeId(transactionTypeId);
                //*********************************************************************
                DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
                deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));
                //DistributorModel distributorModel = commonCommandManager.loadDistributor(appUserModel);
//                commonCommandManager.checkProductLimit(segmentId, productModel.getProductId(), appUserModel.getMobileNo(), deviceTypeModel.getDeviceTypeId(), Double.parseDouble(billAmount), productModel, distributorModel, workFlowWrapper.getHandlerModel());
                SmartMoneyAccountModel olaSmartMoneyAccountModel = getsmartMoneyAccountModel(appUserModel, PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
                AccountInfoModel accountInfoModel = null;
                accountInfoModel = getCommonCommandManager().getAccountInfoModel(appUserModel.getCustomerId(), PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
                if (olaSmartMoneyAccountModel == null) {
                    throw new CommandException("Branchless Banking Account is not defined", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.HIGH, new Throwable());
                } else {
                    accountId = olaSmartMoneyAccountModel.getSmartMoneyAccountId().toString();
                    accountTitle = olaSmartMoneyAccountModel.getName();
                }
                SwitchWrapper switchWrapper = new SwitchWrapperImpl();
                long accountType = customerModel.getCustomerAccountTypeId();
                String userId = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId();
                this.checkVelocityCondition(segmentId, accountType, userId);
                workFlowWrapper.setSwitchWrapper(switchWrapper);
                workFlowWrapper.setProductModel(productModel);
                workFlowWrapper.setCustomerAppUserModel(appUserModel);
                workFlowWrapper.setCustomerModel(customerModel);
                workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
                workFlowWrapper.setSmartMoneyAccountModel(olaSmartMoneyAccountModel);
                workFlowWrapper.setOlaSmartMoneyAccountModel(olaSmartMoneyAccountModel);
                workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
                workFlowWrapper.setAccountInfoModel(accountInfoModel);
                workFlowWrapper.setAppUserModel(appUserModel);
                workFlowWrapper.putObject(CommandFieldConstants.KEY_CNIC, appUserModel.getNic());
                transactionModel = new TransactionModel();
                transactionModel.setTransactionAmount(Double.parseDouble(billAmount));
                workFlowWrapper.setTransactionModel(transactionModel);
                logger.info("[BookMeCommand.execute] Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() +
                        " Customer Mobile No:" + custMobileNo);
                //Boolean isPayByCustomerAccount = ((Integer)workFlowWrapper.getCustomField()).intValue() == 0;
                workFlowWrapper.setCustomField(0);
                workFlowWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, terminalId);
                workFlowWrapper.putObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE, thirdPartyTransactionId);
                //*************************************************************************************************************
                //*************************************************************************************************************


//                I8SBSwitchControllerRequestVO requestVO = ESBAdapter.prepareRequestVoForJsBookMe(I8SBConstants.RequestType_BookMeIPN);
////                requestVO.setOrderRefId(orderReferId);
//                requestVO.setType(serviceType);
//                requestVO.setMobileNumber(custMobileNo);
//                requestVO.setAmount(billAmount);
//                requestVO.setStatus("confirm");
////Params To be Added
//                SwitchWrapper sWrapper = new SwitchWrapperImpl();
//                sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
//                I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
//                sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
//                workFlowWrapper.putObject("SWITCH_WRAPPER", sWrapper);

                workFlowWrapper = commonCommandManager.executeSaleCreditTransaction(workFlowWrapper);
                //*************************************************************************************************************
                //*************************************************************************************************************
//                isInclusiveCharges = workFlowWrapper.getCommissionAmountsHolder().getIsInclusiveCharges();
                transactionModel = workFlowWrapper.getTransactionModel();
                productModel = workFlowWrapper.getProductModel();
                //Customer Balance
                balance = workFlowWrapper.getOLASwitchWrapper().getOlavo().getFromBalanceAfterTransaction();
                //consumerNumber = ((UtilityBillVO) workFlowWrapper.getProductVO()).getConsumerNo();
                commonCommandManager.sendSMS(workFlowWrapper);

            } else {

                throw new CommandException("Customer does not exist against mobile number " + custMobileNo, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
            }
        } catch (FrameworkCheckedException ex) {
            ex.printStackTrace();
            throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
        } catch (WorkFlowException wex) {
            throw new CommandException(wex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, wex);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
        }


    }

    private SmartMoneyAccountModel getsmartMoneyAccountModel(AppUserModel _appUserModel, Long paymentModeId) throws Exception {
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        SmartMoneyAccountModel customerSMAModel = new SmartMoneyAccountModel();
        customerSMAModel.setCustomerId(appUserModel.getCustomerId());
        customerSMAModel.setActive(Boolean.TRUE);
        customerSMAModel.setDeleted(Boolean.FALSE);
        customerSMAModel.setDefAccount(Boolean.TRUE);
        customerSMAModel.setPaymentModeId(paymentModeId);
        searchBaseWrapper.setBasePersistableModel(customerSMAModel);

        searchBaseWrapper = getCommonCommandManager().loadSMAExactMatch(searchBaseWrapper);

        @SuppressWarnings("rawtypes")
        CustomList smaList = searchBaseWrapper.getCustomList();
        if (smaList != null && smaList.getResultsetList() != null && smaList.getResultsetList().size() > 0) {
            customerSMAModel = (SmartMoneyAccountModel) smaList.getResultsetList().get(0);
        }

        return customerSMAModel;
    }


    void checkVelocityCondition(Long segmentId, Long accountType, String userId) throws FrameworkCheckedException {

        BaseWrapper bWrapper = new BaseWrapperImpl();
        bWrapper.putObject(CommandConstants.VELOCITY_PRODUCT_ID, Long.parseLong(productId));
        bWrapper.putObject(CommandConstants.VELOCITY_DEVICE_TYPE_ID, Long.parseLong(deviceTypeId));
        bWrapper.putObject(CommandConstants.VELOCITY_DISTRIBUTOR_ID, -1L);
        bWrapper.putObject(CommandConstants.VELOCITY_AGENT_TYPE, -1L);
        bWrapper.putObject(CommandConstants.VELOCITY_TRANSACTION_AMOUNT, Double.parseDouble(billAmount));
//		bWrapper.putObject(CommandConstants.VELOCITY_ACCOUNT_TYPE, -1L);

        if (segmentId != null && CommissionConstantsInterface.DEFAULT_SEGMENT_ID.longValue() != segmentId.longValue()) {
            bWrapper.putObject(CommandConstants.VELOCITY_SEGMENT_ID, segmentId);
        }

        if (accountType != null) {
            bWrapper.putObject(CommandConstants.VELOCITY_ACCOUNT_TYPE, accountType);
        }
        if (userId != null) {
            bWrapper.putObject(CommandConstants.VELOCITY_CUSTOMER_ID, userId);
        }
        getCommonCommandManager().checkVelocityCondition(bWrapper);
    }


    @Override
    public String response() {
        return toXML();
    }

    private String toXML()
    {

        TransactionModel transactionModel = workFlowWrapper.getTransactionModel();
        ArrayList<LabelValueBean> params = new ArrayList<LabelValueBean>();
        params.add(new LabelValueBean(ATTR_TRXID,replaceNullWithEmpty(workFlowWrapper.getTransactionCodeModel().getCode())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_BILL_AMOUNT, replaceNullWithEmpty(billAmount)));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_FORMATED_BILL_AMOUNT, Formatter.formatNumbers(replaceNullWithZero(Double.parseDouble(billAmount)))));
        params.add(new LabelValueBean(ATTR_CAMT, replaceNullWithEmpty(transactionModel.getTotalCommissionAmount()+"")));
        params.add(new LabelValueBean(ATTR_CAMTF, Formatter.formatNumbers(replaceNullWithZero(transactionModel.getTotalCommissionAmount()))));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_CUSTOMER_MOBILE, replaceNullWithEmpty(custMobileNo)));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_PRODUCT, replaceNullWithEmpty(productModel.getDescription())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_PESSENGER_NAME,  replaceNullWithEmpty(productModel.getName())));
        params.add(new LabelValueBean(ATTR_TAMT, replaceNullWithEmpty(transactionModel.getTotalAmount()+"")));
        params.add(new LabelValueBean(ATTR_TAMTF, Formatter.formatNumbers(transactionModel.getTotalAmount())));
        params.add(new LabelValueBean(ATTR_TPAM, replaceNullWithEmpty(workFlowWrapper.getCommissionAmountsHolder().getTransactionProcessingAmount()+"")));
        params.add(new LabelValueBean(ATTR_TPAMF, Formatter.formatNumbers(workFlowWrapper.getCommissionAmountsHolder().getTransactionProcessingAmount())));
        params.add(new LabelValueBean(ATTR_TXAM, replaceNullWithEmpty(transactionModel.getTransactionAmount()+"")));
        params.add(new LabelValueBean(ATTR_TXAMF, Formatter.formatNumbers(transactionModel.getTransactionAmount())));
        params.add(new LabelValueBean(ATTR_TIMEF, Formatter.formatTime(transactionModel.getCreatedOn())));
        params.add(new LabelValueBean(ATTR_DATE, replaceNullWithEmpty(transactionModel.getCreatedOn()+"")));
        params.add(new LabelValueBean(ATTR_DATEF, PortalDateUtils.formatDate(transactionModel.getCreatedOn(),PortalDateUtils.SHORT_DATE_TIME_FORMAT3)));
        params.add(new LabelValueBean(ATTR_BALF, Formatter.formatNumbers(balance)));

        return MiniXMLUtil.createResponseXMLByParams(params);


    }
}
