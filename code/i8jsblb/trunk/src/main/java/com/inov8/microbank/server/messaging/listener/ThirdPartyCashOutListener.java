package com.inov8.microbank.server.messaging.listener;

import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.microbank.common.model.BISPCustNadraVerificationModel;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.server.dao.postedtransactionreportmodule.PostedTransactionReportDAO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.transactionreversal.TransactionReversalManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.MatchMode;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Attique on 9/5/2018.
 */
public class ThirdPartyCashOutListener implements MessageListener {

    private static Log logger = LogFactory.getLog(ThirdPartyCashOutListener.class);

    private TransactionReversalManager transactionReversalManager;
    private CommonCommandManager commonCommandManager;
    private PostedTransactionReportDAO postedTransactionReportDAO;


    public void onMessage(Message message) {
        SwitchWrapper switchWrapper=null;
        try {
            switchWrapper = (SwitchWrapper) ((ObjectMessage) message).getObject();

            if(checkAlreadySuccessful(switchWrapper)){
//                logger.info("Core Advice already successful in posted_trx... so ignoring trx_code_Id:"+middlewareAdviceVO.getMicrobankTransactionCodeId());
                // Update status from Failed to Successful
                transactionReversalManager.updateRetryAdviceReportStatus(Long.valueOf(switchWrapper.getI8SBSwitchControllerRequestVO().getTransactionId()));
                return;
            }

        //    SwitchWrapper switchWrapper = prepareSwitchWrapper(middlewareAdviceVO);
            transactionReversalManager.makeThirdPartyCashOutReversalAdvice(switchWrapper);
            BISPCustNadraVerificationModel custNadraVerificationModel = new BISPCustNadraVerificationModel();
            custNadraVerificationModel.setTransactionCode(switchWrapper.getI8SBSwitchControllerRequestVO().getTranCode());
            ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
            exampleConfigHolderModel.setEnableLike(Boolean.FALSE);
            exampleConfigHolderModel.setMatchMode(MatchMode.EXACT);
            List<BISPCustNadraVerificationModel> list = commonCommandManager.getBispCustNadraVerificationDAO().findByExample(custNadraVerificationModel,null,null,
                    exampleConfigHolderModel).getResultsetList();
            if(list != null && !list.isEmpty()){
                custNadraVerificationModel = list.get(0);
            }
            custNadraVerificationModel.setResponseCode("I8SB-200");
            try {
                custNadraVerificationModel.setBusinessDate(PortalDateUtils.formatDate(custNadraVerificationModel.getBusinessDate().split(" ")[0],"yyyyy-MM-dd",
                        "dd-MMM-yyyy"));
            } catch (ParseException e) {
                logger.error("Error while Inserting Business Date in BISP NADRA Verification");
                e.printStackTrace();
            }
            commonCommandManager.getBispCustNadraVerificationDAO().saveOrUpdate(custNadraVerificationModel);

        }catch (JMSException ex){
            logger.error(ex);
            throw new RuntimeException(ex.getMessage(), ex);
        }catch (Exception ex){
            ex.printStackTrace();
           // logger.error("Exception occured CoreAdviceListener...TransactionCode:"+middlewareAdviceVO.getMicrobankTransactionCode());
            throw new RuntimeException(ex.getMessage(), ex);
        }


    }

    public boolean checkAlreadySuccessful(SwitchWrapper switchWrapper){
        boolean result = false;
        try{
            /*PostedTransactionReportModel model = new PostedTransactionReportModel();
            model.setTransactionCodeId(middlewareAdviceVO.getMicrobankTransactionCodeId());
            model.setProductId(middlewareAdviceVO.getProductId());
            if(!"".equals(middlewareAdviceVO.getStan()) && null!=middlewareAdviceVO.getStan()) {
                model.setSystemTraceAuditNumber(middlewareAdviceVO.getStan());

            }
            model.setResponseCode("00");


            CustomList<PostedTransactionReportModel> customList = postedTransactionReportDAO.findByExample(model, null, null, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);

            List<PostedTransactionReportModel> list = customList.getResultsetList();

            if(list != null && list.size() > 0){
                result = true;
            }*/
        }catch (Exception ex){
            //not throwing exception
            logger.error("Exception occured while CoreAdviceListener.checkAlreadySuccessful...TransactionCode:", ex);
        }

        return result;
    }

    public void setTransactionReversalManager(TransactionReversalManager transactionReversalManager) {
        this.transactionReversalManager = transactionReversalManager;
    }

    public void setCommonCommandManager(CommonCommandManager commonCommandManager) {
        this.commonCommandManager = commonCommandManager;
    }

    public void setPostedTransactionReportDAO( PostedTransactionReportDAO postedTransactionReportDAO ) {
        this.postedTransactionReportDAO = postedTransactionReportDAO;
    }


}
