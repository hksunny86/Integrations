package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.integration.vo.MiddlewareMessageVO;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.switchmodule.iris.model.CustomerAccount;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.apache.commons.lang.StringEscapeUtils;
import static com.inov8.microbank.common.util.XMLConstants.*;
import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAMS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;

public abstract class IBFTBaseCommand extends BaseCommand {

    protected String deviceTypeId;
    protected String coreAccountNumber;
    protected String productId;
    protected String toBankImd;
    protected String transactionAmount, totalAmount;
    protected String purposeOfPayment;
    protected Double _commissionAmount = 0.0D;
    protected Double TPAM = 0.0D;
    protected String commissionAmount;
    protected String coreAccountNick = null;
    protected String bbAccountNick = null;
    protected String oneLinkCharges;
    protected StringBuilder response;
    protected String receiverBankName;
    protected String receiverBranchName;
    protected String receiverIBAN;
    protected String crDr;

    protected AppUserModel appUserModel;
    protected ProductModel productModel;
    protected RetailerContactModel retailerContactModel = null;
    protected WorkFlowWrapper workFlowWrapper;
    protected TransactionModel transactionModel;
    protected SmartMoneyAccountModel smartMoneyAccountModel;
    protected AccountInfoModel accountInfoModel;

    @Override
    public void prepare(BaseWrapper baseWrapper) {
        deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        coreAccountNumber = this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_CORE_ACC_NO);
        coreAccountNick = this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_CORE_ACC_TITLE);
        productId = this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_PROD_ID);
        toBankImd = this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_TO_BANK_IMD);
        transactionAmount = this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_TX_AMOUNT);
        totalAmount = this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_TOTAL_AMOUNT);
        purposeOfPayment = this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_TRANS_PURPOSE_CODE);
        String tPam = this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_TPAM);
        receiverBankName = this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_BENE_BANK_NAME);
        receiverBranchName = this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_BENE_BRANCH_NAME);
        receiverIBAN = this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_BENE_IBAN);
        crDr = this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_BENE_TRX_TYPE);
        if(tPam != null && !tPam.equals(""))
            TPAM = Double.valueOf(tPam);
        appUserModel = ThreadLocalAppUser.getAppUserModel();
        oneLinkCharges = "";
        if(oneLinkCharges != null && !oneLinkCharges.equals(""))
            TPAM += Double.valueOf(oneLinkCharges);
        workFlowWrapper = new WorkFlowWrapperImpl();
        workFlowWrapper.setAppUserModel(appUserModel);
        try {
            BaseWrapper bWrapper = new BaseWrapperImpl();
            ProductModel model = new ProductModel();
            model.setProductId(Long.parseLong(productId));
            bWrapper.setBasePersistableModel(model);
            bWrapper = getCommonCommandManager().loadProduct(bWrapper);
            productModel = (ProductModel) bWrapper.getBasePersistableModel();

            RetailerContactModel retContactModel = new RetailerContactModel();
            retContactModel.setRetailerContactId(appUserModel.getRetailerContactId());

            bWrapper = new BaseWrapperImpl();
            bWrapper.setBasePersistableModel(retContactModel);
            CommonCommandManager commonCommandManager = this.getCommonCommandManager();
            bWrapper = commonCommandManager.loadRetailerContact(bWrapper);
            retailerContactModel = (RetailerContactModel) bWrapper.getBasePersistableModel();
        } catch (FrameworkCheckedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        validationErrors = ValidatorWrapper.doRequired(coreAccountNumber,validationErrors,"Core Account No ");
        validationErrors = ValidatorWrapper.doRequired(toBankImd,validationErrors,"Bank ");
        validationErrors = ValidatorWrapper.doRequired(transactionAmount,validationErrors,"Transaction Amount ");
        validationErrors = ValidatorWrapper.doRequired(purposeOfPayment,validationErrors,"Transaction Purpose ");
        return validationErrors;
    }

    @Override
    public void doExecute() throws CommandException {
        try{
            validationErrors = getCommonCommandManager().checkActiveAppUser(appUserModel);
            if(validationErrors.hasValidationErrors()) {
                throw new CommandException(validationErrors.getErrors(), ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM, new Throwable());
            }
            if(appUserModel.getAppUserTypeId().longValue() != UserTypeConstantsInterface.RETAILER.longValue()) {
                throw new CommandException(validationErrors.getErrors(), ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM, new Throwable());
            }
            SmartMoneyAccountModel agentSMAModel =  new SmartMoneyAccountModel();
            agentSMAModel.setRetailerContactId(retailerContactModel.getRetailerContactId());
            agentSMAModel.setActive(Boolean.TRUE);
            agentSMAModel.setDeleted(Boolean.FALSE);
            agentSMAModel.setDefAccount(Boolean.TRUE);
            SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
            searchBaseWrapper.setBasePersistableModel(agentSMAModel);
            searchBaseWrapper = getCommonCommandManager().loadSMAExactMatch(searchBaseWrapper);
            CustomList<SmartMoneyAccountModel> smaList = searchBaseWrapper.getCustomList();
            if(smaList != null && smaList.getResultsetList() != null && smaList.getResultsetList().size() > 0){
                smartMoneyAccountModel = (SmartMoneyAccountModel)smaList.getResultsetList().get(0);
            }
            workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
            workFlowWrapper.setOlaSmartMoneyAccountModel(smartMoneyAccountModel);
            workFlowWrapper.setProductModel(productModel);
            workFlowWrapper.setFromRetailerContactModel(retailerContactModel);
            accountInfoModel = getCommonCommandManager().getAccountInfoModel(appUserModel.getAppUserId(),smartMoneyAccountModel.getName());
            accountInfoModel.setOldPin(""); // This PIN should never be used for validation
            workFlowWrapper.setAccountInfoModel(accountInfoModel);
            DistributorModel distributorModel = getCommonCommandManager().loadDistributor(appUserModel);
            getCommonCommandManager().checkProductLimit(null, productModel.getProductId(), appUserModel.getMobileNo(), Long.valueOf(deviceTypeId), Double.valueOf(transactionAmount), productModel, distributorModel, handlerModel);

            String userId = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId();

            BaseWrapper bWrapper = new BaseWrapperImpl();
            bWrapper.putObject(CommandConstants.VELOCITY_PRODUCT_ID, Long.parseLong(productId));
            bWrapper.putObject(CommandConstants.VELOCITY_DEVICE_TYPE_ID,Long.parseLong(deviceTypeId));
            bWrapper.putObject(CommandConstants.VELOCITY_DISTRIBUTOR_ID, distributorModel.getDistributorId());
            bWrapper.putObject(CommandConstants.VELOCITY_AGENT_TYPE, retailerContactModel.getDistributorLevelId());
            bWrapper.putObject(CommandConstants.VELOCITY_TRANSACTION_AMOUNT, Double.parseDouble(transactionAmount));
            bWrapper.putObject(CommandConstants.VELOCITY_SEGMENT_ID, CommissionConstantsInterface.DEFAULT_SEGMENT_ID);
            bWrapper.putObject(CommandConstants.VELOCITY_ACCOUNT_TYPE, retailerContactModel.getOlaCustomerAccountTypeModel().getCustomerAccountTypeId());
            bWrapper.putObject(CommandConstants.VELOCITY_CUSTOMER_ID, userId);
//            bWrapper.putObject(CommandConstants.VELOCITY_RETAILER_ID, retailerId);
            getCommonCommandManager().checkVelocityCondition(bWrapper);
            //
            CustomerAccount custAcct = new CustomerAccount();
            custAcct.setFromBankImd("603733");
            if(coreAccountNick != null && !coreAccountNick.equals(""))
                custAcct.setTitleOfTheAccount(coreAccountNick);
            custAcct.setNumber(coreAccountNumber);
            custAcct.setBankName(receiverBankName);
            custAcct.setBranchName(receiverBranchName);
            custAcct.setBenificieryIBAN(receiverIBAN);
            custAcct.setTransactionType(crDr);
            custAcct.setToBankImd(toBankImd);
            workFlowWrapper.setCustomerAccount(custAcct);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_ACCOUNT_TITLE_AGNETMATE,coreAccountNick);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_TRANS_PURPOSE_CODE,purposeOfPayment);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_TO_BANK_IMD,toBankImd);
            workFlowWrapper.setTransactionAmount(Double.valueOf(transactionAmount));
            if(coreAccountNick != null && coreAccountNick.equals("")){
                coreAccountNick = commonCommandManager.fetchAccountTitle(workFlowWrapper);//.titleFetch(appUserModel);
                MiddlewareMessageVO middlewareMessageVO = workFlowWrapper.getSwitchWrapper().getMiddlewareIntegrationMessageVO();
                if(middlewareMessageVO != null){
                    receiverBankName = middlewareMessageVO.getAccountBankName();
                    receiverBranchName = middlewareMessageVO.getAccountBranchName();
                    receiverIBAN = middlewareMessageVO.getBenificieryIBAN();
                    crDr = middlewareMessageVO.getCrdr();
                }
            }
            bbAccountNick = appUserModel.getFirstName() + " " + appUserModel.getLastName();
            SegmentModel segmentModel = new SegmentModel();
            segmentModel.setSegmentId(CommissionConstantsInterface.DEFAULT_SEGMENT_ID);
            workFlowWrapper.setSegmentModel(segmentModel);
            DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
            deviceTypeModel.setDeviceTypeId(Long.valueOf(deviceTypeId));
            workFlowWrapper.setRetailerContactModel(retailerContactModel);
            workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
            workFlowWrapper.setTaxRegimeModel(retailerContactModel.getTaxRegimeIdTaxRegimeModel());
            transactionModel = new TransactionModel();
            transactionModel.setTransactionAmount(Double.valueOf(transactionAmount));
            TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
            transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.AGENT_IBFT_TX);
            workFlowWrapper.setTransactionModel(transactionModel);
            workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
            workFlowWrapper.setTxProcessingAmount(TPAM);
            Double totalAmount = Double.parseDouble(transactionAmount);
            if(oneLinkCharges != null && !oneLinkCharges.equals(""))
                totalAmount += Double.valueOf(oneLinkCharges);
            workFlowWrapper.setTotalAmount(totalAmount);
            workFlowWrapper.setTransactionAmount(Double.parseDouble(transactionAmount));
            workFlowWrapper.setAppUserModel(appUserModel);
            workFlowWrapper.setHandlerModel(handlerModel);
            workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);
            workFlowWrapper.setSegmentModel(new SegmentModel());
            workFlowWrapper.setUserDeviceAccountModel(ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel());
            workFlowWrapper.setRetailerContactModel(retailerContactModel);
            workFlowWrapper.setLeg2Transaction(Boolean.FALSE);
            //
            execute();
        }
        catch(Exception ex){
            ex.printStackTrace();
            throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
        }
    }

    @Override
    public String doResponse() {
        String productName = productModel.getName();

        response = new StringBuilder();
        String resp = response();
        if(resp != null && resp.equals("")){
            if (Long.parseLong(deviceTypeId) == DeviceTypeConstantsInterface.ALLPAY_WEB.longValue()) {
                response.append("<msg id='256'><params>");
                response.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_PROD_ID, productModel.getProductId().toString()));
                response.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_NAME, productName));
                response.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_ACCOUNT_NUMBER, this.coreAccountNick));
                response.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_ACCOUNT_NUMBER_BB, this.bbAccountNick));
                response.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TX_AMOUNT, this.transactionAmount));
                response.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CAMT, this.commissionAmount));
                response.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_BENE_BANK_NAME, this.receiverBankName));
                response.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_BENE_BRANCH_NAME, this.receiverBranchName));
                response.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_BENE_IBAN, this.receiverIBAN));
                response.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_BENE_TRX_TYPE, this.crDr));
                response.append("</params></msg>");

            }   else if (Long.parseLong(deviceTypeId) == DeviceTypeConstantsInterface.ALL_PAY.longValue()|| deviceTypeId.equals(DeviceTypeConstantsInterface.WEB_SERVICE.toString())) {

                response.append(toMobileXMLResponse());
            }
        }
        else
            return resp;

        return response.toString();
    }

    private String toMobileXMLResponse() {
        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CORE_ACC_NO, StringEscapeUtils.escapeXml(this.coreAccountNumber)));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CORE_ACC_TITLE, StringEscapeUtils.escapeXml(this.coreAccountNick)));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_ACCOUNT_NUMBER_BB_AGNETMATE, this.bbAccountNick));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TX_AMOUNT, this.transactionAmount));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATTED_TX_AMOUNT, Formatter.formatDoubleByPattern(Double.valueOf(transactionAmount),
                "#,###.00")));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TPAM, String.valueOf(TPAM)));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TPAMF, Formatter.formatDouble(TPAM)));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CAMT, this.commissionAmount));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CAMTF, Formatter.formatDoubleByPattern(Double.valueOf(commissionAmount), "#,###.00")));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TAMT, String.valueOf(Double.parseDouble(transactionAmount) + TPAM)));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TAMTF, Formatter.formatDoubleByPattern(Double.valueOf(Double.parseDouble(transactionAmount) + TPAM), "#,###.00")));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_BENE_BANK_NAME, this.receiverBankName));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_BENE_BRANCH_NAME, this.receiverBranchName));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_BENE_IBAN, this.receiverIBAN));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_BENE_TRX_TYPE, this.crDr));
        responseBuilder.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);
        return responseBuilder.toString();
    }
}
