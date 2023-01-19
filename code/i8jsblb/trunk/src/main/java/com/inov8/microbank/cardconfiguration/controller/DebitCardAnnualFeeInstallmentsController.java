package com.inov8.microbank.cardconfiguration.controller;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.*;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.cardconfiguration.model.CardFeeRuleModel;
import com.inov8.microbank.cardconfiguration.model.CardFeeTypeModel;
import com.inov8.microbank.cardconfiguration.service.CardConfigurationManager;
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.server.facade.CommonFacade;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DebitCardAnnualFeeInstallmentsController extends AdvanceFormController {
    private CommonFacade commonFacade;
    private ReferenceDataManager referenceDataManager;
    private CardConfigurationManager cardConfigurationManager;
    private String cardFeeRuleId = null;

    public DebitCardAnnualFeeInstallmentsController()
    {
        setCommandName("cardFeeRuleModel");
        setCommandClass( CardFeeRuleModel.class );
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        Map<String,Object> refDataMap = new HashMap<>(8);
        ReferenceDataWrapper refDataWrapper = null;
        List<CardFeeTypeModel> cardFeeTypeModelList = null;

        CardFeeTypeModel cardFeeTypeModel = new CardFeeTypeModel();
        refDataWrapper = new ReferenceDataWrapperImpl(cardFeeTypeModel, "name", SortingOrder.ASC);
        refDataWrapper = commonFacade.getReferenceData(refDataWrapper, 1L, 2L, 3L);
        cardFeeTypeModelList = refDataWrapper.getReferenceDataList();

        SegmentModel segmentModel = new SegmentModel();
        List<SegmentModel> segmentModelList = null;
        segmentModel.setIsActive(true);
        refDataWrapper = new ReferenceDataWrapperImpl(
                segmentModel, "name", SortingOrder.ASC);
        referenceDataManager.getReferenceData(refDataWrapper);
        if (refDataWrapper.getReferenceDataList() != null) {
            segmentModelList = refDataWrapper.getReferenceDataList();
        }

        refDataMap.put("cardFeeTypeModelList", cardFeeTypeModelList);
        refDataMap.put("segmentModelList", segmentModelList);

        httpServletRequest.setAttribute("cardFeeRuleId", cardFeeRuleId);
//        httpServletRequest.

        cardFeeRuleId = httpServletRequest.getParameter("cardFeeRuleId");

        return refDataMap;
    }

    @Override
    protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws Exception {
        return new CardFeeRuleModel();
    }

    @Override
    protected ModelAndView onCreate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object,
                                    BindException bindException) throws Exception {
        ModelAndView modelAndView = null;
        CardFeeRuleModel cardFeeRuleModel = (CardFeeRuleModel) object;

        String ruleId = cardFeeRuleId;
        cardFeeRuleModel.setCardFeeRuleId(Long.valueOf(ruleId));
        try {
            CardFeeRuleModel cardFeeRuleModel1 = cardConfigurationManager.searchCardFeeRule(cardFeeRuleModel);
            if (cardFeeRuleModel1 != null) {
                cardFeeRuleModel.setIsInstallments(true);
                if(cardFeeRuleModel.getInstallmentPlan().equals("QUARTERLY")){
                    cardFeeRuleModel.setNoOfInstallments(4L);
                    cardFeeRuleModel.setInstallmentPlan("QUARTERLY");
                    cardFeeRuleModel.setInstallmentAmount(cardFeeRuleModel1.getAmount()/4);
                }
                else if(cardFeeRuleModel.getInstallmentPlan().equals("BI-ANNUAL")){
                    cardFeeRuleModel.setNoOfInstallments(2L);
                    cardFeeRuleModel.setInstallmentPlan("BI-ANNUAL");
                    cardFeeRuleModel.setInstallmentAmount(cardFeeRuleModel1.getAmount()/2);
                }
                else{
                    cardFeeRuleModel.setNoOfInstallments(1L);
                    cardFeeRuleModel.setInstallmentPlan("ANNUAL");
                    cardFeeRuleModel.setInstallmentAmount(cardFeeRuleModel1.getAmount());
                }
//                cardFeeRuleModel.setNoOfInstallments(cardFeeRuleModel.getNoOfInstallments());
                cardConfigurationManager.saveCardFeeRuleModel(cardFeeRuleModel);
            }

            modelAndView = new ModelAndView(this.getSuccessView());
            super.saveMessage(httpServletRequest, "Record saved successfully.");
            return new ModelAndView("redirect:p_debitcardannualfeeinstallments.html?cardFeeRuleId =" +cardFeeRuleId);
        }
        catch(FrameworkCheckedException ex)
        {
            logger.error("Error while saving Debit Card Fee Rules in onCreate() :: " + ex.getMessage(),ex);
            super.saveMessage(httpServletRequest, "Record could not be saved.");
            modelAndView = super.showForm(httpServletRequest, httpServletResponse, bindException);
        }
        return modelAndView;
    }

    @Override
    protected ModelAndView onUpdate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {
        return null;
    }

    public void setCommonFacade(CommonFacade commonFacade) {
        this.commonFacade = commonFacade;
    }

    public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
        this.referenceDataManager = referenceDataManager;
    }

    public void setCardConfigurationManager(CardConfigurationManager cardConfigurationManager) {
        this.cardConfigurationManager = cardConfigurationManager;
    }
}
