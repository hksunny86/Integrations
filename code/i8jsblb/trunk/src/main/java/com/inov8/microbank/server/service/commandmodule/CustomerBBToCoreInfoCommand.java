package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.integration.vo.MiddlewareMessageVO;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.integration.vo.BBToCoreVO;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.switchmodule.iris.model.CustomerAccount;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import static com.inov8.microbank.common.util.XMLConstants.*;

public class CustomerBBToCoreInfoCommand extends BaseCommand
{
    private AppUserModel appUserModel;
    private String productId;
    private BaseWrapper peparedBaseWrapper;

    ProductModel productModel;
    BaseWrapper baseWrapper;
    CommissionAmountsHolder commissionAmountsHolder;
    BBToCoreVO bbToCoreVO ;

    private String customerMobileNo;
    private String receiverMobileNo;
    private String coreAccountNo;
    private String accountTitle;
    private String transactionAmount;

    protected String receiverBankName;
    protected String receiverBranchName;
    protected String receiverIBAN;
    protected String crDr;
    protected String toBankImd;
    protected String purposeOfPayment;



    private final Log logger = LogFactory.getLog(CustomerBBToCoreInfoCommand.class);
    private String logClassName = "CustomerBBToCoreInfoCommand";

    @Override
    public void execute() throws CommandException
    {
        CommonCommandManager commonCommandManager = this.getCommonCommandManager();
        WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();

        CommissionWrapper commissionWrapper;
        baseWrapper = new BaseWrapperImpl();

        try{
            ValidationErrors validationErrors = commonCommandManager.checkActiveAppUser(appUserModel);
            if(!validationErrors.hasValidationErrors()){

                //Check User Device Accounts health
                BaseWrapper baseWrapper = new BaseWrapperImpl();
                baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.MOBILE);
                baseWrapper.setBasePersistableModel(appUserModel);
                validationErrors = getCommonCommandManager().checkCustomerCredentials(baseWrapper);

                if(validationErrors.hasValidationErrors()) {
                    throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
                }

                SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
                SmartMoneyAccountModel customerSMAModel =  new SmartMoneyAccountModel();
                customerSMAModel.setCustomerId(appUserModel.getCustomerId());
                customerSMAModel.setActive(Boolean.TRUE);
                customerSMAModel.setDeleted(Boolean.FALSE);
                customerSMAModel.setDefAccount(Boolean.TRUE);
                searchBaseWrapper.setBasePersistableModel(customerSMAModel);

                searchBaseWrapper = getCommonCommandManager().loadSMAExactMatch(searchBaseWrapper);

                @SuppressWarnings("rawtypes")
                CustomList smaList = searchBaseWrapper.getCustomList();
                if(smaList != null && smaList.getResultsetList() != null && smaList.getResultsetList().size() > 0){
                    customerSMAModel = (SmartMoneyAccountModel)smaList.getResultsetList().get(0);
                }

                baseWrapper.setBasePersistableModel(customerSMAModel);
                validationErrors = commonCommandManager.checkSmartMoneyAccount(baseWrapper);

                if(validationErrors.hasValidationErrors()){
                    logger.error("["+logClassName+".execute] Throwing Exception in Product ID: " +  productId + " - Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId()+ " Mobile No:" + customerMobileNo);
                    throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
                }

                workFlowWrapper.setSmartMoneyAccountModel(customerSMAModel);


                TransactionModel transactionModel = new TransactionModel();
                productModel = new ProductModel();
                productModel.setProductId(Long.parseLong(productId));
                baseWrapper.setBasePersistableModel(productModel);
                baseWrapper = commonCommandManager.loadProduct(baseWrapper);
                productModel = (ProductModel)baseWrapper.getBasePersistableModel();

                //load/Populate VO
                if(productModel.getProductIntgModuleInfoId() != null && !"".equals(productModel.getProductIntgModuleInfoId()) && productModel.getProductIntgVoId() != null && !"".equals(productModel.getProductIntgVoId())){
                    ProductVO productVO = commonCommandManager.loadProductVO(peparedBaseWrapper);
                    if(productVO == null){
                        throw new CommandException("ProductVo is not loaded",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
                    }

                    productVO.populateVO(productVO, peparedBaseWrapper);

                    workFlowWrapper.setProductModel(productModel);
                    workFlowWrapper.setProductVO(productVO);

                    TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
                    transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.CUSTOMER_INITIATED_BB_TO_CORE_ACCOUNT_TX);
                    workFlowWrapper.setProductModel(productModel);
                    workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
                    workFlowWrapper.setDeviceTypeModel(new DeviceTypeModel(Long.parseLong(deviceTypeId)));

                    BBToCoreVO vo = (BBToCoreVO)workFlowWrapper.getProductVO();
                    transactionModel.setTransactionAmount(vo.getCurrentBillAmount());
                    workFlowWrapper.setTransactionModel(transactionModel);

                    //titleFetch call
                    CustomerAccount customerAccount = new CustomerAccount(coreAccountNo,null,null,null);
                    customerAccount.setFromBankImd("603733");
                    customerAccount.setBankName(receiverBankName);
                    customerAccount.setBranchName(receiverBranchName);
                    customerAccount.setBenificieryIBAN(receiverIBAN);
                    customerAccount.setTransactionType(crDr);
                    workFlowWrapper.setCustomerAccount(customerAccount);
                    customerAccount.setToBankImd(toBankImd);
                    workFlowWrapper.setCustomerAccount(customerAccount);
                    workFlowWrapper.putObject(CommandFieldConstants.KEY_CC_TO_BANK_IMD,toBankImd);
                    workFlowWrapper.putObject(CommandFieldConstants.KEY_TRANS_PURPOSE_CODE,purposeOfPayment);

                    workFlowWrapper = commonCommandManager.getBBToCoreAccInfo(workFlowWrapper);

                    MiddlewareMessageVO middlewareMessageVO = workFlowWrapper.getSwitchWrapper().getMiddlewareIntegrationMessageVO();
                    if(middlewareMessageVO != null){
                        receiverBankName = middlewareMessageVO.getAccountBankName();
                        receiverBranchName = middlewareMessageVO.getAccountBranchName();
                        receiverIBAN = middlewareMessageVO.getBenificieryIBAN();
                        crDr = middlewareMessageVO.getCrdr();
                    }

                    logger.info("["+logClassName+".execute] Product ID: " +  productId + " - Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());

                }else{
                    logger.error("["+logClassName+".execute] Unable to load Product VO. Throwing Exception in Product ID: " +  productId + " - Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
                    throw new CommandException(this.getMessageSource().getMessage("billPaymentCommand.productVoLoadedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
                }

                TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
                transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.CUSTOMER_INITIATED_BB_TO_CORE_ACCOUNT_TX);
                workFlowWrapper.setProductModel(productModel);
                workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
                workFlowWrapper.setDeviceTypeModel(new DeviceTypeModel(Long.parseLong(deviceTypeId)));

                BBToCoreVO vo = (BBToCoreVO)workFlowWrapper.getProductVO();
                transactionModel.setTransactionAmount(vo.getCurrentBillAmount());
                workFlowWrapper.setTransactionModel(transactionModel);

                loadCustomerAndSegment(workFlowWrapper, appUserModel.getCustomerId());

                DistributorModel distributorModel = new DistributorModel();
                distributorModel.setDistributorId(DistributorConstants.DEFAULT_DISTRIBUTOR_ID);

                if (!(productId.equalsIgnoreCase(ProductConstantsInterface.RELIEF_FUND_PRODUCT.toString()))) {
                    commonCommandManager.checkProductLimit(workFlowWrapper.getSegmentModel().getSegmentId(), productModel.getProductId(), appUserModel.getMobileNo(), Long.valueOf(deviceTypeId), Double.valueOf(transactionAmount), productModel, distributorModel, null);
                }
                workFlowWrapper.setTaxRegimeModel(workFlowWrapper.getCustomerModel().getTaxRegimeIdTaxRegimeModel());
                workFlowWrapper.setIsCustomerInitiatedTransaction(true);
                commissionWrapper = commonCommandManager.calculateCommission(workFlowWrapper);
                commissionAmountsHolder = (CommissionAmountsHolder)commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);

                commonCommandManager.validateBalance(appUserModel, customerSMAModel , commissionAmountsHolder.getTotalAmount(), true);

                bbToCoreVO = vo;
                accountTitle = vo.getAccountTitle();
            }else{
                logger.error("["+logClassName+".execute] User Validation Failed. Throwing Exception in Product ID: " +  productId + " - Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
                throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
            }
        }catch(CommandException e){
            logger.error("["+logClassName+".execute] Exception Occured for Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Product ID: " +  productId + " Exception Message:" + e.getMessage());
            throw e;
        }catch(WorkFlowException wex){
            logger.error("["+logClassName+".execute] Exception Occured for Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Product ID: " +  productId+ " Exception Message:" + wex.getMessage());
            if(StringUtil.isFailureReasonId(wex.getMessage())){
                throw new CommandException(MessageUtil.getMessage(wex.getMessage()),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,wex);
            }else{
                throw new CommandException(wex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,wex);
            }
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("["+logClassName+".execute] Exception Occured for Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Product ID: " +  productId +" Exception Message:" + ex.getMessage());
            if(ex.getMessage() != null && (ex.getMessage().indexOf("JTA") != -1 || ex.getMessage().indexOf("Exception") != -1)){
                throw new CommandException(this.getMessageSource().getMessage("command.unexpectedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
            }
            else if (ex.getMessage().equals("Your account is debit blocked.")){
                throw new CommandException(ex.getMessage(),ErrorCodes.DEBIT_BLOCKED,ErrorLevel.MEDIUM,ex);
            }
            else{
                if(ex.getMessage().equals("The entered Account# is invalid. Please retry with a valid Account#.")){
                    throw new CommandException(ex.getMessage(),ErrorCodes.INVALID_ACCOUNT_NUMBER,ErrorLevel.MEDIUM,ex);
                }
                else
                    throw new CommandException(ex.getMessage(),ErrorCodes.TRANSACTION_TIME_OUT,ErrorLevel.MEDIUM,ex);
            }
        }
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

    @Override
    public void prepare(BaseWrapper baseWrapper)
    {
        appUserModel = ThreadLocalAppUser.getAppUserModel();
        peparedBaseWrapper = baseWrapper;

        productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
//        productId = String.valueOf(ProductConstantsInterface.CUSTOMER_BB_TO_IBFT);
        deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        customerMobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
        receiverMobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE);
        coreAccountNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CORE_ACC_NO);
        transactionAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_AMOUNT);
        toBankImd = this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_CC_TO_BANK_IMD);
        purposeOfPayment = this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_TRANS_PURPOSE_CODE);
        logger.info("["+logClassName+".prepare] Logged In AppUserModel: " + appUserModel.getAppUserId() + " Product ID:" + productId + " deviceTypeId: " + deviceTypeId);
    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException{
        validationErrors = ValidatorWrapper.doRequired(productId,validationErrors,"Product");
        validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
        validationErrors = ValidatorWrapper.doRequired(customerMobileNo,validationErrors,"Customer Mobile No");
//        validationErrors = ValidatorWrapper.doRequired(receiverMobileNo,validationErrors,"Receiver Mobile No");
        validationErrors = ValidatorWrapper.doRequired(coreAccountNo,validationErrors,"Account Number");
        validationErrors = ValidatorWrapper.doRequired(transactionAmount,validationErrors,"Amount");

        if(!validationErrors.hasValidationErrors()) {
            validationErrors = ValidatorWrapper.doNumeric(transactionAmount,validationErrors,"Withdrawal Amount");
        }

        return validationErrors;
    }

    @Override
    public String response()
    {
        return toXML();
    }

    private String toXML()
    {

        StringBuilder strBuilder = new StringBuilder();

        if(commissionAmountsHolder != null){

            strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);

            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_PROD_ID, productId));
            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_ACCOUNT_TITLE_AGNETMATE, StringEscapeUtils.escapeXml(accountTitle)));
            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CORE_ACC_NO, coreAccountNo));

            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_COMM_AMOUNT, ""+replaceNullWithZero(commissionAmountsHolder.getTotalCommissionAmount())));
            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_COMM_AMOUNT, Formatter.formatNumbers(commissionAmountsHolder.getTotalCommissionAmount())));
            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TX_PROCESS_AMNT, ""+replaceNullWithZero(commissionAmountsHolder.getTransactionProcessingAmount())));
            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_TX_PROCESS_AMNT, Formatter.formatNumbers(commissionAmountsHolder.getTransactionProcessingAmount())));
            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TOTAL_AMOUNT, ""+replaceNullWithZero(commissionAmountsHolder.getTotalAmount())));
            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_TOTAL_AMOUNT, Formatter.formatNumbers(commissionAmountsHolder.getTotalAmount())));
            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TX_AMOUNT, ""+replaceNullWithZero(commissionAmountsHolder.getTransactionAmount())));
            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATTED_TX_AMOUNT, ""+Formatter.formatNumbers(commissionAmountsHolder.getTransactionAmount())));

            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CUSTOMER_MOBILE, customerMobileNo));
            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE, replaceNullWithEmpty(receiverMobileNo)));
            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_BENE_BANK_NAME, this.receiverBankName));
            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_BENE_BRANCH_NAME, this.receiverBranchName));
            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_BENE_IBAN, this.receiverIBAN));
            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_BENE_TRX_TYPE, this.crDr));

            strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);
        }

        return strBuilder.toString();

    }
}
