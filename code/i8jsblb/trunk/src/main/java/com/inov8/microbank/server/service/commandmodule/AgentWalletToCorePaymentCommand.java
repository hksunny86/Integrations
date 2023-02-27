package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.RetailerModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.bankmodule.MemberBankModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.verifly.common.constants.FailureReasonConstants;
import com.inov8.verifly.common.exceptions.InvalidDataException;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.apache.commons.lang.StringEscapeUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AgentWalletToCorePaymentCommand extends IBFTBaseCommand {

    @Override
    public void execute() throws CommandException {
        try{
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            RetailerModel retailerModel = new RetailerModel();
            retailerModel.setRetailerId(retailerContactModel.getRetailerId());
            baseWrapper.setBasePersistableModel(retailerModel);
            baseWrapper =  commonCommandManager.loadRetailer(baseWrapper);
            retailerModel = (RetailerModel) baseWrapper.getBasePersistableModel();
            workFlowWrapper.setRetailerModel(retailerModel);
            baseWrapper = new BaseWrapperImpl();
            DistributorModel distributorModel = new DistributorModel();
            distributorModel.setDistributorId(retailerModel.getDistributorId());
            baseWrapper.setBasePersistableModel(distributorModel);
            baseWrapper = getCommonCommandManager().loadDistributor(baseWrapper);
            distributorModel = (DistributorModel) baseWrapper.getBasePersistableModel();
            workFlowWrapper.setDistributorModel(distributorModel);
            Long customerId = ThreadLocalAppUser.getAppUserModel().getAppUserId();
            AccountInfoModel accountInfoModel = commonCommandManager.getAccountInfoModel(customerId, smartMoneyAccountModel.getName());
            if (accountInfoModel == null) {
                //logger.error("Could not load AccountInfo in OLAVeriflyFinancialInstitution.transferFunds()");
                throw new InvalidDataException(String.valueOf(FailureReasonConstants.RECORD_IS_INACTIVE));
            }
            workFlowWrapper.setAccountInfoModel(accountInfoModel);
            MemberBankModel model = null;
            MemberBankModel memberBankModel = new MemberBankModel();
            memberBankModel.setBankImd(toBankImd);
            List<MemberBankModel> list = commonCommandManager.getMemberBankDao().findByExample(memberBankModel).getResultsetList();
            if(list != null && !list.isEmpty()){
                model = list.get(0);
            }
            workFlowWrapper.putObject("MEMBER_BANK",model);
            workFlowWrapper.setMemberBankModel(memberBankModel);

            commonCommandManager.executeSaleCreditTransaction(workFlowWrapper);
            //transactionMtraodel = workFlowWrapper.getTransactionModel();
            CommissionAmountsHolder commissionAmountsHolder = workFlowWrapper.getCommissionAmountsHolder();
            commissionAmount = commissionAmountsHolder.getTotalCommissionAmount().toString();
            TPAM += commissionAmountsHolder.getTransactionProcessingAmount();
            workFlowWrapper.putObject("productTile",productModel.getName());
            commonCommandManager.sendSMS(workFlowWrapper);
        }
        catch (Exception ex){
            ex.printStackTrace();
            throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
        }
    }

    @Override
    public String response() {
        response = new StringBuilder();
        return response.append(toMobileXMLResponse()).toString();
    }

    private String toMobileXMLResponse() {
        transactionModel = workFlowWrapper.getTransactionModel();
        List<LabelValueBean> lvbs = new ArrayList<LabelValueBean>();
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_ID, transactionModel.getTransactionCodeIdTransactionCodeModel().getCode().toString()));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_AGENT_MOBILE, appUserModel.getMobileNo()));
        lvbs.add(new LabelValueBean("BBACID", bbAccountNick));
        lvbs.add(new LabelValueBean("COREACID", coreAccountNick));
        lvbs.add(new LabelValueBean("COREACNUMBER", coreAccountNumber));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_DATE, transactionModel.getCreatedOn().toString()));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_DATEF, Formatter.formatDate(transactionModel.getCreatedOn())));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_TIMEF, Formatter.formatTime(transactionModel.getCreatedOn())));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_PRODUCT, productModel.getName()));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_TX_ID, transactionModel.getTransactionCodeIdTransactionCodeModel().getCode()));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_TXAM, replaceNullWithZero(transactionModel.getTransactionAmount()).toString()));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_TXAMF, Formatter.formatNumbers(replaceNullWithZero(transactionModel.getTransactionAmount()))));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_CAMT, replaceNullWithEmpty(transactionModel.getTotalCommissionAmount().toString())));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_CAMTF, Formatter.formatNumbers(transactionModel.getTotalCommissionAmount())));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_TPAM, Double.toString(TPAM)));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_TPAMF, Formatter.formatNumbers(TPAM)));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_TAMT, String.valueOf(transactionModel.getTotalAmount().doubleValue())));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_TAMTF, Formatter.formatNumbers(transactionModel.getTotalAmount())));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_BENE_BANK_NAME, receiverBankName));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_BENE_BRANCH_NAME, receiverBranchName));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_BENE_IBAN, receiverIBAN));
        return MiniXMLUtil.createResponseXMLByParams(lvbs);
    }
}
