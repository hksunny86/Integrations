package com.inov8.microbank.debitcard.controller;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.cardconfiguration.common.CardConstantsInterface;
import com.inov8.microbank.common.model.CardProdCodeModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.debitcard.model.DebitCardModel;
import com.inov8.microbank.server.dao.fetchcardtype.FetchCardTypeDAO;
import com.inov8.microbank.server.dao.smartmoneymodule.SmartMoneyAccountDAO;
import com.inov8.microbank.server.facade.portal.transactionreversal.TransactionReversalFacade;
import com.inov8.microbank.server.service.commandmodule.UpgradeL2AccountCommand;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.webapp.action.ajax.AjaxController;
import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

public class DebitCardManagementAjaxController extends AjaxController {

    @Autowired
    private CommonCommandManager commonCommandManager;
    @Autowired
    private SmartMoneyAccountDAO smartMoneyAccountDAO;
    @Autowired
    private FetchCardTypeDAO fetchCardTypeDAO;
    @Autowired
    private ESBAdapter esbAdapter;

    protected final Log logger = LogFactory.getLog(DebitCardManagementAjaxController.class);

    @Override
    public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("Debit Card Request Initiated");
        AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();

        String param = (String) request.getParameter("debitCardId");

        if (!StringUtil.isNullOrEmpty(param) && StringUtil.isNumeric(param)) {
            Long debitCardId = Long.parseLong(param);

            try {
                String msgToText = "";
                DebitCardModel debitCardModel = new DebitCardModel();
                debitCardModel = commonCommandManager.getDebitCardModelDao().getDebitCardModelByDebitCardId(debitCardId);

                if (debitCardModel != null) {
                    List<ActionAuthorizationModel> existingReqList = null;
                    ActionAuthorizationModel actionAuthorizationModel = new ActionAuthorizationModel();
                    actionAuthorizationModel.setReferenceId(debitCardModel.getMobileNo());
                    existingReqList = commonCommandManager.getActionAuthorizationFacade().checkExistingRequest(actionAuthorizationModel).getResultsetList();
                    if (existingReqList != null && !existingReqList.isEmpty()) {
                        throw new Exception("Debit Card Change Info Request is already in pending state");
                    }

                    if (debitCardModel.getReissuance() != null && !debitCardModel.getReissuance().equals("")) {
                        if (debitCardModel.getReissuance().equals("0")) {
                            debitCardModel.setCardStatusId(CardConstantsInterface.CARD_STATUS_APPROVED);
                            debitCardModel.setUpdatedOn(new Date());
                            debitCardModel.setIsApproved("1");
                            debitCardModel.setApprovedOn(new Date());
                            commonCommandManager.getDebitCardModelDao().saveOrUpdate(debitCardModel);
                            msgToText = MessageUtil.getMessage("debit.card.req.successful");
                        } else {
                            I8SBSwitchControllerRequestVO requestVO = ESBAdapter.prepareRequestVoForDebitCardReissuance
                                    (I8SBConstants.RequestType_JSDEBITCARD_CardReissuance);
                            requestVO.setCardNumber(debitCardModel.getCardNo());
                            requestVO.setRelationshipNumber(debitCardModel.getCnic());
                            requestVO.setMobileNumber(debitCardModel.getMobileNo());
                            requestVO.setCardEmborsingName(debitCardModel.getDebitCardEmbosingName());
                            requestVO.setCardBranchCode(MessageUtil.getMessage("debit.card.branch.code"));
                            requestVO.setIssuedDate(String.valueOf(debitCardModel.getIssuanceDate()));
                            requestVO.setExpiryDate(String.valueOf(debitCardModel.getExpiryDate()));

                            long smaId = debitCardModel.getSmartMoneyAccountId();

                            SmartMoneyAccountModel sma = smartMoneyAccountDAO.findByPrimaryKey(smaId);

                            CardProdCodeModel cardProdCodeModel = fetchCardTypeDAO.findByPrimaryKey(sma.getCardProdId());

                            requestVO.setCardTypeCode(cardProdCodeModel.getCardTypeCode());
                            requestVO.setCardProdCode(cardProdCodeModel.getCardProductCode());
                            requestVO.setRequestId(CardConstantsInterface.DEBIT_CARD_ONLINE_RQST_TYPE_CODE);

                            SwitchWrapper i8sbSwitchWrapper = new SwitchWrapperImpl();

                            i8sbSwitchWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                            requestVO = i8sbSwitchWrapper.getI8SBSwitchControllerRequestVO();
                            I8SBSwitchControllerResponseVO responseVO = requestVO.getI8SBSwitchControllerResponseVO();
                            i8sbSwitchWrapper = this.esbAdapter.makeI8SBCall(i8sbSwitchWrapper);
                            responseVO = i8sbSwitchWrapper.getI8SBSwitchControllerResponseVO();
                            ESBAdapter.processI8sbResponseCode(responseVO, false);
                            if (responseVO.getResponseCode().equals("I8SB-200")) {
                                debitCardModel.setIsReIssuanceApproved("1");
                                debitCardModel.setApprovedOn(new Date());
                                debitCardModel.setReIssuanceStatus(CardConstantsInterface.CARD_STATUS_IN_PROCESS);
                                commonCommandManager.getDebitCardModelDao().saveOrUpdate(debitCardModel);
                                msgToText = MessageUtil.getMessage("debit.card.req.reissuance.successful");
                            }
                        }
                    } else {
                        debitCardModel.setIsApproved("1");
                        debitCardModel.setApprovedOn(new Date());
                        debitCardModel.setCardStatusId(CardConstantsInterface.CARD_STATUS_APPROVED);
                        debitCardModel.setUpdatedOn(new Date());
                        commonCommandManager.getDebitCardModelDao().saveOrUpdate(debitCardModel);
                        msgToText = MessageUtil.getMessage("debit.card.req.successful");
                    }

                    BaseWrapper msgBaseWrapper = new BaseWrapperImpl();
                    msgBaseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, new SmsMessage(debitCardModel.getMobileNo(), msgToText));
                    try {
                        this.commonCommandManager.sendSMSToUser(msgBaseWrapper);
                    } catch (FrameworkCheckedException e) {
                        e.printStackTrace();
                    }

                    ajaxXmlBuilder.addItem("mesg", "Debit Card data has been approved");
                }

                ajaxXmlBuilder.addItem("mesg", "No Data found");

            } catch (Exception ex) {
                ex.printStackTrace();
                if (ex.getMessage().equals("Data Already approved")
                        || ex.getMessage().equals("Already Successful, you cannot process this request")) {

                    ajaxXmlBuilder.addItem("mesg", ex.getMessage());
                } else if (ex.getMessage().contains("Debit Card Change Info Request is already in pending state")) {
                    ajaxXmlBuilder.addItem("mesg", "Debit Card Change Info Request is already in pending state.");
                } else {
                    ajaxXmlBuilder.addItem("mesg", "Debit Card Approval Denied");
                }
            }
        }

        return ajaxXmlBuilder.toString();
    }

    public void setCommonCommandManager(CommonCommandManager commonCommandManager) {
        this.commonCommandManager = commonCommandManager;
    }

    public void setSmartMoneyAccountDAO(SmartMoneyAccountDAO smartMoneyAccountDAO) {
        this.smartMoneyAccountDAO = smartMoneyAccountDAO;
    }

    public void setFetchCardTypeDAO(FetchCardTypeDAO fetchCardTypeDAO) {
        this.fetchCardTypeDAO = fetchCardTypeDAO;
    }

    public void setEsbAdapter(ESBAdapter esbAdapter) {
        this.esbAdapter = esbAdapter;
    }
}
