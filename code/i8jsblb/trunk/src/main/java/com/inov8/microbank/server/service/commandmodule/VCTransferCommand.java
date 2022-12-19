package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.TransactionTypeModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

public class VCTransferCommand extends BaseCommand{
    private final Log logger = LogFactory.getLog(VCTransferCommand.class);
    private BaseWrapper baseWrapper;
    private String productId, transactionAmount;
    private WorkFlowWrapper workFlowWrapper;

    @Override
    public void prepare(BaseWrapper baseWrapper) {
        String classNMethodName = "[DebitCardCashWithDrawlCommand.prepare] ";
        this.logger.info("Start of " + classNMethodName);
        this.baseWrapper = baseWrapper;
        productId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
        transactionAmount = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AMOUNT);

        logger.info("End of CustomerInitiatedAccountToAccountCommand.prepare()");

    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        logger.info("Start of CustomerInitiatedAccountToAccountCommand.validate()");
        logger.info("End of CustomerInitiatedAccountToAccountCommand.validate()");
        return validationErrors;
    }

    @Override
    public void execute() throws CommandException {
        logger.info("Start of CustomerInitiatedAccountToAccountCommand.execute()");
        try {
        CommonCommandManager commonCommandManager = this.getCommonCommandManager();

        ProductModel productModel = new ProductModel();
        BaseWrapper baseWrapper1 = new BaseWrapperImpl();
        productModel = new ProductModel();
        productModel.setProductId(ProductConstantsInterface.VC_TRANSFER_PRODUCT);
        baseWrapper1.setBasePersistableModel(productModel);
        baseWrapper1 = getCommonCommandManager().loadProduct(baseWrapper1);
        productModel = (ProductModel) baseWrapper1.getBasePersistableModel();

        workFlowWrapper = new WorkFlowWrapperImpl();
        workFlowWrapper.setProductModel(productModel);
        workFlowWrapper.setTransactionAmount(Double.valueOf(transactionAmount));
        logger.info("[VCTransferCommand]Amount to be transferred is: "+ transactionAmount);
        TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
        transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.VC_TRANSFER_TX);
        workFlowWrapper.setTransactionTypeModel(transactionTypeModel);

        workFlowWrapper = commonCommandManager.executeSaleCreditTransaction(workFlowWrapper);

        } catch (WorkFlowException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            logger.error("Exception catched on VCTransferCommand... Details:..", e);
            e.printStackTrace();
        }
    }

    @Override
    public String response() {
        return MiniXMLUtil.createResponseXMLByParams(this.toXML());
    }

    private List<LabelValueBean> toXML() {

        List<LabelValueBean> params = new ArrayList<LabelValueBean>(20);
        params.add(new LabelValueBean(CommandFieldConstants.KEY_TX_AMOUNT, "" + replaceNullWithZero(workFlowWrapper.getTransactionAmount())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_FORMATTED_TX_AMOUNT, "" + replaceNullWithZero(workFlowWrapper.getTransactionAmount())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_FORMATED_TOTAL_AMOUNT, "" + replaceNullWithZero(workFlowWrapper.getTotalAmount())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_DATEF, Formatter.formatDate(workFlowWrapper.getTransactionModel().getCreatedOn())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_DATE, workFlowWrapper.getTransactionModel().getCreatedOn().toString()));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_TIMEF, Formatter.formatTime(workFlowWrapper.getTransactionModel().getCreatedOn())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_TX_ID, workFlowWrapper.getTransactionDetailMasterModel().getTransactionCode()));
        return params;
    }

}
