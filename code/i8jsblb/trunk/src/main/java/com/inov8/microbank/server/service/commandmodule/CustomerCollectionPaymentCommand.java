package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.fonepay.common.FonePayConstants;
import com.inov8.microbank.server.service.integration.vo.UtilityBillVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.springframework.dao.DataAccessException;

import java.sql.SQLException;
import java.util.ArrayList;

import static com.inov8.microbank.common.util.XMLConstants.*;

/**
 * Created by Omar Butt on 1/8/2018.
 */
public class CustomerCollectionPaymentCommand extends BaseCommand {

    protected final Log logger = LogFactory.getLog(CustomerCollectionPaymentCommand.class);

    private AppUserModel appUserModel;
    private String productId;
    private String txProcessingAmount;
    private String totalAmount;
    private String deviceTypeId;
    private String commissionAmount;
    private String billAmount;

    private TransactionModel transactionModel;
    private ProductModel productModel;
    private BaseWrapper baseWrapper;
    private WorkFlowWrapper workFlowWrapper;

    private Double balance = 0D;
    private String consumerNumber;
    private String custMobileNo;
//    private ProductVO billPaymentVO = null;
    private Long segmentId = CommissionConstantsInterface.DEFAULT_SEGMENT_ID;
//    private Date billDueDate;
//    private Double billingAmount;
//    private Double lateBillAmount;
private String channelId;
    private String terminalId;
    private String thirdPartyTransactionId;
    private String stan;

    @Override
    public void prepare(BaseWrapper baseWrapper) {
        this.baseWrapper = baseWrapper;
        appUserModel = ThreadLocalAppUser.getAppUserModel();

        deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
        custMobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
        consumerNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CSCD);
        billAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BILL_AMOUNT);
        txProcessingAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_PROCESS_AMNT);
        totalAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TOTAL_AMOUNT);
        commissionAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_COMM_AMOUNT);
        channelId=this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_CHANNEL_ID);
        terminalId=this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_TERMINAL_ID);
        thirdPartyTransactionId = getCommandParameter(baseWrapper, FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE);
        stan=getCommandParameter(baseWrapper,CommandFieldConstants.KEY_STAN);

        logger.info("[CustomerCollectionPaymentCommand.prepare] Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + custMobileNo);
    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {

        validationErrors = ValidatorWrapper.doRequired(productId,validationErrors,"Product");
        validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
        if(!productId.equals(ProductConstantsInterface.CUSTOMER_ET_COLLECTION.toString()))
            validationErrors = ValidatorWrapper.doRequired(consumerNumber, validationErrors, "Consumer Number");
        validationErrors = ValidatorWrapper.doRequired(custMobileNo, validationErrors, "Customer Mobile Number");
        validationErrors = ValidatorWrapper.doRequired(billAmount,validationErrors,"Bill Amount");

        if(!validationErrors.hasValidationErrors()){
            validationErrors = ValidatorWrapper.doNumeric(productId,validationErrors,"Product");
            validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
        }

        return validationErrors;
    }

    @Override
    public void execute() throws CommandException {

        CommonCommandManager commonCommandManager = this.getCommonCommandManager();
        workFlowWrapper = new WorkFlowWrapperImpl();

        try {
            ValidationErrors validationErrors = commonCommandManager.checkActiveAppUser(appUserModel);
            if(validationErrors.hasValidationErrors()) {
                logger.error("[CustomerCollectionPaymentCommand.prepare] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + custMobileNo);
                throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
            }
            productModel = new ProductModel();
            productModel.setProductId(Long.parseLong(productId));
            baseWrapper.setBasePersistableModel(productModel);
            baseWrapper = commonCommandManager.loadProduct(baseWrapper);
            productModel = (ProductModel) baseWrapper.getBasePersistableModel();
            if (productModel == null) {
                throw new CommandException("Product not loaded", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
            }

            CustomerModel customerModel = null;
            if(appUserModel != null) {

                customerModel = new CustomerModel();
                customerModel.setCustomerId(appUserModel.getCustomerId());
                String productCode = productModel.getProductCode();
                if(consumerNumber!=null && productId != null && productCode !=null)
                {
                    logger.info("Validating Challan for Consumer No : " +consumerNumber+" product Code: "+productCode);
                    boolean flag =this.getCommonCommandManager().validateChallanParams(consumerNumber,productCode);
                    //Params are valid check for challan status
                    if (flag){
                        boolean billInQueue = this.getCommonCommandManager().getChallanStatus(consumerNumber,productCode);
                        if (billInQueue)
                            throw new CommandException("Transaction is already Processing.",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);
                        //Check challan payment in TransactionDetailMaster and throws exception if exists
                        /*long paidCount = this.getCommonCommandManager().getPaidChallan(consumerNumber,productCode);
                        // throws exception according to the count of paidCount, if paidCount >0
                        if(paidCount>0)
                            this.getCommonCommandManager().throwsChallanException(paidCount);
*/
                        //challan is not in process or paid add to bill_status
                        this.getCommonCommandManager().addChallanToQueue(consumerNumber,productCode);

                    }

                }
                BaseWrapper bWrapper = new BaseWrapperImpl();
                bWrapper.setBasePersistableModel(customerModel);
                bWrapper = this.getCommonCommandManager().loadCustomer(bWrapper);

                if(bWrapper.getBasePersistableModel() != null) {
                    customerModel = (CustomerModel) bWrapper.getBasePersistableModel();
                    segmentId = customerModel.getSegmentId();
                }

                workFlowWrapper.setTransactionAmount(Double.parseDouble(billAmount));

                if(!StringUtil.isNullOrEmpty(txProcessingAmount)) {
                    workFlowWrapper.setTxProcessingAmount(Double.parseDouble(txProcessingAmount));
                } else {
                    workFlowWrapper.setTxProcessingAmount(0.0D);
                }

                if(!StringUtil.isNullOrEmpty(billAmount)) {
                    workFlowWrapper.setBillAmount(Double.parseDouble(billAmount));
                } else {
                    workFlowWrapper.setBillAmount(0.0D);
                }

                if(!StringUtil.isNullOrEmpty(commissionAmount)) {
                    workFlowWrapper.setTotalCommissionAmount(Double.parseDouble(commissionAmount));
                } else {
                    workFlowWrapper.setTotalCommissionAmount(0.0D);
                }
                // load UtilityBillVO from session which was saved at info command
//                UtilityBillVO billInfoFromSession = ThreadLocalBillInfo.getBillInfo();
//                if(billInfoFromSession != null){
//                    billingAmount = billInfoFromSession.getBillAmount();
//                    lateBillAmount = billInfoFromSession.getLateBillAmount();
//                    billDueDate = billInfoFromSession.getDueDate();
//                }

//                billPaymentVO = commonCommandManager.loadProductVO(baseWrapper);
//                productModel = (ProductModel) baseWrapper.getBasePersistableModel();
//                if(billPaymentVO == null) {
//                    throw new CommandException(this.getMessageSource().getMessage("billPaymentCommand.productVoLoadedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
//                }

                UtilityBillVO utilityBillVO = new UtilityBillVO();
                utilityBillVO.setConsumerNo(consumerNumber);
                utilityBillVO.setBillAmount(Double.parseDouble(billAmount));

                workFlowWrapper.setCustomerAppUserModel(appUserModel);

                SegmentModel segmentModel = new SegmentModel();
                segmentModel.setSegmentId(segmentId);
                workFlowWrapper.setSegmentModel(segmentModel);
                workFlowWrapper.setFromSegmentId(segmentId);
                workFlowWrapper.setIsCustomerInitiatedTransaction(true);

                TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
                transactionTypeModel.setTransactionTypeId( TransactionTypeConstantsInterface.CUSTOMER_COLLECTION_PAYMENT_TX );

                DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
                deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));

                SmartMoneyAccountModel olaSmartMoneyAccountModel = getsmartMoneyAccountModel(appUserModel, PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);

                if(olaSmartMoneyAccountModel == null) {
                    throw new CommandException("Branchless Banking Account is not defined" ,ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.HIGH, new Throwable());
                }

                commonCommandManager.validateBalance(appUserModel, olaSmartMoneyAccountModel, Double.valueOf(billAmount), true);

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
                workFlowWrapper.setProductVO(utilityBillVO);
                workFlowWrapper.setAppUserModel(appUserModel);

                transactionModel = new TransactionModel();
                transactionModel.setTransactionAmount(Double.parseDouble(billAmount));
                workFlowWrapper.setTransactionModel(transactionModel);
                workFlowWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID,terminalId);
                workFlowWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID,channelId);
                workFlowWrapper.putObject(CommandFieldConstants.KEY_STAN,stan);
                workFlowWrapper.putObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE,thirdPartyTransactionId);

                logger.info("[CustomerCollectionPaymentCommand.execute] Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() +
                        " Customer Mobile No:" + custMobileNo );

                //*************************************************************************************************************
                //*************************************************************************************************************
                workFlowWrapper = commonCommandManager.executeSaleCreditTransaction(workFlowWrapper);
                //*************************************************************************************************************
                //*************************************************************************************************************
                transactionModel = workFlowWrapper.getTransactionModel();
                productModel = workFlowWrapper.getProductModel();

                //Customer Balance
                balance = workFlowWrapper.getOLASwitchWrapper().getOlavo().getFromBalanceAfterTransaction();

                commonCommandManager.sendSMS(workFlowWrapper);
                commonCommandManager.novaAlertMessage(workFlowWrapper);

                //Remove challan row from billStatus
                //Data will be delete by CLEAN_BILL_STATUS_JOB
                /*if(consumerNumber!=null && productCode != null)
                    this.getCommonCommandManager().removeChallanFromQueue(consumerNumber,productCode);*/
            } else {
                throw new CommandException("Customer does not exist against mobile number "+custMobileNo ,ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);
            }
        }catch(Exception ex) {
            logger.error("Exception occurred at CustomerCollectionPayment.execute", ex);
            if(ex instanceof NullPointerException
                    || ex instanceof HibernateException
                    || ex instanceof SQLException
                    || ex instanceof DataAccessException
                    || (ex.getMessage() != null && ex.getMessage().indexOf("Exception") != -1) ){

                logger.error("Converting Exception ("+ex.getClass()+") to generic error message...");
                throw new CommandException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG,ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
            }else{
                throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
            }
        }

    }

    @Override
    public String response() {
        return toXML();
    }

    private String toXML(){

        TransactionModel transactionModel = workFlowWrapper.getTransactionModel();
        ArrayList<LabelValueBean> params = new ArrayList<LabelValueBean>();
        params.add(new LabelValueBean(ATTR_TRXID,replaceNullWithEmpty(workFlowWrapper.getTransactionCodeModel().getCode())));
        params.add(new LabelValueBean(ATTR_BALF, Formatter.formatNumbers(replaceNullWithZero((balance)))));
        params.add(new LabelValueBean(CommandFieldConstants.CMD_AGNETMATE_CONSUMER_NUMBER, consumerNumber));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_BILL_AMOUNT, billAmount));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_FORMATED_BILL_AMOUNT, Formatter.formatNumbers(Double.parseDouble(billAmount))));
        params.add(new LabelValueBean(ATTR_CAMT, replaceNullWithEmpty(workFlowWrapper.getTransactionDetailMasterModel().getExclusiveCharges() + "")));
        params.add(new LabelValueBean(ATTR_CAMTF, Formatter.formatNumbers(workFlowWrapper.getTransactionDetailMasterModel().getExclusiveCharges())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_CUSTOMER_MOBILE, custMobileNo));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_CNIC, appUserModel.getNic()));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_PRODUCT, replaceNullWithEmpty(productModel.getDescription())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_PRODUCT_NAME,  replaceNullWithEmpty(productModel.getName())));
        params.add(new LabelValueBean(ATTR_TAMT, replaceNullWithEmpty(transactionModel.getTotalAmount()+"")));
        params.add(new LabelValueBean(ATTR_TAMTF, Formatter.formatNumbers(transactionModel.getTotalAmount())));

        params.add(new LabelValueBean(ATTR_DATEF, PortalDateUtils.formatDate(transactionModel.getCreatedOn(),PortalDateUtils.SHORT_DATE_TIME_FORMAT3)));

        params.add(new LabelValueBean(ATTR_TXAM, replaceNullWithEmpty(transactionModel.getTransactionAmount()+"")));
        params.add(new LabelValueBean(ATTR_TXAMF, Formatter.formatNumbers(transactionModel.getTransactionAmount())));
        params.add(new LabelValueBean(ATTR_TIMEF, Formatter.formatTime(transactionModel.getCreatedOn())));

        params.add(new LabelValueBean(ATTR_DATE, replaceNullWithEmpty(transactionModel.getCreatedOn()+"")));


        return MiniXMLUtil.createResponseXMLByParams(params);
    }



    private SmartMoneyAccountModel getsmartMoneyAccountModel(AppUserModel _appUserModel, Long paymentModeId) throws Exception
    {
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        SmartMoneyAccountModel customerSMAModel =  new SmartMoneyAccountModel();
        customerSMAModel.setCustomerId(appUserModel.getCustomerId());
        customerSMAModel.setActive(Boolean.TRUE);
        customerSMAModel.setDeleted(Boolean.FALSE);
        customerSMAModel.setDefAccount(Boolean.TRUE);
        customerSMAModel.setPaymentModeId(paymentModeId);
        searchBaseWrapper.setBasePersistableModel(customerSMAModel);

        searchBaseWrapper = getCommonCommandManager().loadSMAExactMatch(searchBaseWrapper);

        @SuppressWarnings("rawtypes")
        CustomList smaList = searchBaseWrapper.getCustomList();
        if(smaList != null && smaList.getResultsetList() != null && smaList.getResultsetList().size() > 0){
            customerSMAModel = (SmartMoneyAccountModel)smaList.getResultsetList().get(0);
        }

        return customerSMAModel;
    }


    void checkVelocityCondition( Long segmentId, Long accountType, String userId) throws FrameworkCheckedException {

        BaseWrapper bWrapper = new BaseWrapperImpl();
        bWrapper.putObject(CommandConstants.VELOCITY_PRODUCT_ID, Long.parseLong(productId));
        bWrapper.putObject(CommandConstants.VELOCITY_DEVICE_TYPE_ID,Long.parseLong(deviceTypeId));
        bWrapper.putObject(CommandConstants.VELOCITY_DISTRIBUTOR_ID, -1L);
        bWrapper.putObject(CommandConstants.VELOCITY_AGENT_TYPE, -1L);
        bWrapper.putObject(CommandConstants.VELOCITY_TRANSACTION_AMOUNT, Double.parseDouble(billAmount));
        bWrapper.putObject(CommandConstants.VELOCITY_ACCOUNT_TYPE, accountType);

        if(segmentId != null && CommissionConstantsInterface.DEFAULT_SEGMENT_ID.longValue() != segmentId.longValue()) {
            bWrapper.putObject(CommandConstants.VELOCITY_SEGMENT_ID, segmentId);
        }

        if(accountType != null) {
            bWrapper.putObject(CommandConstants.VELOCITY_ACCOUNT_TYPE, accountType);
        }

        if(userId != null) {
            bWrapper.putObject(CommandConstants.VELOCITY_CUSTOMER_ID, userId);
        }
        getCommonCommandManager().checkVelocityCondition(bWrapper);
    }


}

