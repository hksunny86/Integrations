package com.inov8.microbank.debitcard.controller;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.integration.controller.IBFTSwitchController;
import com.inov8.integration.vo.MiddlewareMessageVO;
import com.inov8.microbank.cardconfiguration.common.CardConstantsInterface;
import com.inov8.microbank.common.model.TransactionDetailMasterModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.debitcard.model.DebitCardModel;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.transactionreversal.TransactionReversalManager;
import com.inov8.microbank.webapp.action.ajax.AjaxController;
import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

public class DebitCardDeniedAjaxController extends AjaxController {

    @Autowired
    private CommonCommandManager commonCommandManager;
    private TransactionReversalManager transactionReversalManager;

    @Autowired
    private IBFTSwitchController ibftSwitchController;

    protected final Log logger = LogFactory.getLog(DebitCardDeniedAjaxController.class);

    @Override
    public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("Debit Card Request Initiated");
        AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();

        Long currentTime = System.currentTimeMillis();

        String param = (String) request.getParameter("debitCardId");

        if (!StringUtil.isNullOrEmpty(param) && StringUtil.isNumeric(param)) {
            Long debitCardId = Long.parseLong(param);

            try {
                String msgToText = "";
                String mobileNo = "";

                DebitCardModel debitCardModel = new DebitCardModel();
                debitCardModel = commonCommandManager.getDebitCardModelDao().getDebitCardModelByDebitCardId(debitCardId);

                if (debitCardModel != null) {
                    List<ActionAuthorizationModel> existingReqList = null;
                    ActionAuthorizationModel actionAuthorizationModel = new ActionAuthorizationModel();
                    actionAuthorizationModel.setReferenceId(debitCardModel.getMobileNo());
                    existingReqList = commonCommandManager.getActionAuthorizationFacade().checkExistingRequest(actionAuthorizationModel).getResultsetList();
                    if (existingReqList != null && !existingReqList.isEmpty()) {
                        throw new FrameworkCheckedException("Debit Card Change Info Request is already in pending state");
                    }

                    mobileNo = debitCardModel.getMobileNo();
                    TransactionDetailMasterModel tempModel = new TransactionDetailMasterModel();
                    tempModel.setTransactionCode(debitCardModel.getTransactionCode());
                    BaseWrapper wrapper = new BaseWrapperImpl();
                    wrapper.setBasePersistableModel(tempModel);
                    try {
                        logger.info("Transaction Code: " + tempModel.getTransactionCode());
                        tempModel = transactionReversalManager.loadTDMForReversalByTransactionCode(debitCardModel.getTransactionCode());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

//                    logger.info("Transaction Code: "+ tempModel.getTransactionCode());

                    //
                    if (tempModel != null) {
                        MiddlewareMessageVO middlewareMessageVO = new MiddlewareMessageVO();
                        middlewareMessageVO.setProductId(tempModel.getProductId());
                        middlewareMessageVO.setReserved1(tempModel.getTransactionCodeId().toString());
                        middlewareMessageVO.setPAN(debitCardModel.getCardNo());
                        logger.info("Debit Card Reversal to be Initiated");
                        ibftSwitchController.cashWithDrawalReversal(middlewareMessageVO);
                        logger.info("Debit Card Reversal Ended");

                        if (middlewareMessageVO.getResponseDescription() != null && middlewareMessageVO.getResponseDescription().contains("Transaction is already Reversed.")) {
                            ajaxXmlBuilder.addItem("mesg", middlewareMessageVO.getResponseDescription());
                        } else {
                            if (middlewareMessageVO.getResponseCode().equals("00")) {
                                if (debitCardModel.getReissuance() != null && debitCardModel.getReissuance().equals("1")) {
                                    debitCardModel.setIsReIssuanceApprovedDenied("1");
                                    debitCardModel.setDeniedOn(new Date());
                                    debitCardModel.setReIssuanceStatus(CardConstantsInterface.CARD_STATUS_REJECTED);
                                    msgToText = MessageUtil.getMessage("debit.card.req.reissuance.rejected");
                                } else {
                                    debitCardModel.setCardStatusId(CardConstantsInterface.CARD_STATUS_REJECTED);
                                    debitCardModel.setIsApprovedDenied("1");
                                    debitCardModel.setDeniedOn(new Date());
                                    debitCardModel.setCnic(debitCardModel.getCnic() + PortalConstants.PREFIX_SETTLED_ACCOUNT + currentTime);
                                    debitCardModel.setMobileNo(debitCardModel.getMobileNo() + PortalConstants.PREFIX_SETTLED_ACCOUNT + currentTime);
                                    debitCardModel.setCardNo(debitCardModel.getCardNo() + PortalConstants.PREFIX_SETTLED_ACCOUNT + currentTime);
                                    msgToText = MessageUtil.getMessage("debit.card.req.rejected");
                                }
                                debitCardModel.setUpdatedOn(new Date());
                                commonCommandManager.getDebitCardModelDao().saveOrUpdate(debitCardModel);

                                ajaxXmlBuilder.addItem("mesg", "Debit Card request has been denied");
                            }
                        }
                    } else {
                        if (debitCardModel.getReissuance() != null && debitCardModel.getReissuance().equals("1")) {
                            debitCardModel.setIsReIssuanceApprovedDenied("1");
                            debitCardModel.setDeniedOn(new Date());
                            debitCardModel.setReIssuanceStatus(CardConstantsInterface.CARD_STATUS_REJECTED);
                            msgToText = MessageUtil.getMessage("debit.card.req.reissuance.rejected");
                        } else {
                            debitCardModel.setCardStatusId(CardConstantsInterface.CARD_STATUS_REJECTED);
                            debitCardModel.setIsApprovedDenied("1");
                            debitCardModel.setDeniedOn(new Date());
                            debitCardModel.setCnic(debitCardModel.getCnic() + PortalConstants.PREFIX_SETTLED_ACCOUNT + currentTime);
                            debitCardModel.setMobileNo(debitCardModel.getMobileNo() + PortalConstants.PREFIX_SETTLED_ACCOUNT + currentTime);
                            debitCardModel.setCardNo(debitCardModel.getCardNo() + PortalConstants.PREFIX_SETTLED_ACCOUNT + currentTime);
                            msgToText = MessageUtil.getMessage("debit.card.req.rejected");
                        }
                        debitCardModel.setUpdatedOn(new Date());
                        commonCommandManager.getDebitCardModelDao().saveOrUpdate(debitCardModel);
                        ajaxXmlBuilder.addItem("mesg", "Debit Card request has been denied");
                    }

                    BaseWrapper msgBaseWrapper = new BaseWrapperImpl();
                    msgBaseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, new SmsMessage(mobileNo, msgToText));
                    try {
                        this.commonCommandManager.sendSMSToUser(msgBaseWrapper);
                    } catch (FrameworkCheckedException e) {
                        e.printStackTrace();
                    }
                }
                ajaxXmlBuilder.addItem("mesg", "No Data found");
            } catch (FrameworkCheckedException ex) {
                ex.printStackTrace();
                if (ex.getMessage().equals("Data Already Approved")
                        || ex.getMessage().equals("Already Successful, you cannot process this request")) {

                    ajaxXmlBuilder.addItem("mesg", ex.getMessage());
                } else if (ex.getMessage().contains("Debit Card Change Info Request is already in pending state")) {
                    ajaxXmlBuilder.addItem("mesg", "Debit Card Change Info Request is already in pending state.");

                } else {
                    ajaxXmlBuilder.addItem("mesg", "Some error has occured while accessing Data");
                }
            }
        }
        return ajaxXmlBuilder.toString();
    }

    public void setCommonCommandManager(CommonCommandManager commonCommandManager) {
        this.commonCommandManager = commonCommandManager;
    }

    public void setTransactionReversalManager(TransactionReversalManager transactionReversalManager) {
        this.transactionReversalManager = transactionReversalManager;
    }

    public void setIbftSwitchController(IBFTSwitchController ibftSwitchController) {
        this.ibftSwitchController = ibftSwitchController;
    }
}
