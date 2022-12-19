package com.inov8.microbank.cardconfiguration.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inov8.microbank.cardconfiguration.service.CardConfigurationManager;
import com.inov8.microbank.cardconfiguration.service.CardConfigurationManagerImpl;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.cardconfiguration.model.CardFeeRuleModel;
import com.inov8.microbank.cardconfiguration.model.CardFeeTypeModel;
import com.inov8.microbank.debitcard.service.DebitCardManager;
import com.inov8.microbank.cardconfiguration.vo.CardFeeRuleVO;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.facade.CommonFacade;

/**
 * Created By    : Sheharyaar Nawaz <br>
 * Creation Date : 13-09-2019<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class DebitCardFeeRuleController extends AdvanceFormController
{
    private CommonFacade commonFacade;
    private DebitCardManager debitCardManager;
    private CardConfigurationManager cardConfigurationManager;

    public DebitCardFeeRuleController()
    {
        setCommandName( "cardFeeRuleVO" );
        setCommandClass( CardFeeRuleVO.class );
    }

    @Override
    protected Map<String,Object> loadReferenceData( HttpServletRequest request ) throws Exception
    {
        Map<String,Object> refDataMap = new HashMap<>(8);
        ReferenceDataWrapper refDataWrapper = null;
        List<CardTypeModel> cardTypeModelList = null;
        List<CardProdCodeModel> cardProdCodeModelList = null;
        List<AppUserTypeModel> appUserTypeModelList = null;
        List<SegmentModel> segmentModelList = null;
        List<OlaCustomerAccountTypeModel> customerAccountTypeModelList = null;
        List<OlaCustomerAccountTypeModel> agentAccountTypeModelList = null;
        List<OlaCustomerAccountTypeModel> allAccountTypeModelList = null;
        List<CardFeeTypeModel> cardFeeTypeModelList = null;
        List<DistributorModel> distributorModelList =null;

            CardTypeModel cardTypeModel = new CardTypeModel();
        refDataWrapper = new ReferenceDataWrapperImpl(cardTypeModel, "name", SortingOrder.ASC);
        refDataWrapper = commonFacade.getReferenceData(refDataWrapper);
        cardTypeModelList = refDataWrapper.getReferenceDataList();

        CardProdCodeModel cardProdCodeModel = new CardProdCodeModel();
        refDataWrapper = new ReferenceDataWrapperImpl(cardProdCodeModel, "cardProductName", SortingOrder.ASC);
        refDataWrapper = commonFacade.getReferenceData(refDataWrapper);
        cardProdCodeModelList = refDataWrapper.getReferenceDataList();

        AppUserTypeModel appUserTypeModel = new AppUserTypeModel();
        refDataWrapper = new ReferenceDataWrapperImpl(appUserTypeModel, "name", SortingOrder.ASC);
        refDataWrapper = commonFacade.getReferenceData(refDataWrapper, UserTypeConstantsInterface.CUSTOMER);
        appUserTypeModelList = refDataWrapper.getReferenceDataList();

        SegmentModel segmentModel = new SegmentModel();
        segmentModel.setIsActive(true);
        refDataWrapper = new ReferenceDataWrapperImpl(segmentModel, "name", SortingOrder.ASC);
        refDataWrapper = commonFacade.getReferenceData(refDataWrapper);
        segmentModelList = refDataWrapper.getReferenceDataList();

        OlaCustomerAccountTypeModel accountTypeModel = new OlaCustomerAccountTypeModel();
        accountTypeModel.setActive(true);
        accountTypeModel.setIsCustomerAccountType(true);
        refDataWrapper = new ReferenceDataWrapperImpl(accountTypeModel, "name", SortingOrder.ASC);
        refDataWrapper = commonFacade.getReferenceDataByExcluding(refDataWrapper, CustomerAccountTypeConstants.SETTLEMENT, CustomerAccountTypeConstants.WALK_IN_CUSTOMER);
        customerAccountTypeModelList = refDataWrapper.getReferenceDataList();

        accountTypeModel.setIsCustomerAccountType(false);
        refDataWrapper = new ReferenceDataWrapperImpl(accountTypeModel, "name", SortingOrder.ASC);
        refDataWrapper = commonFacade.getReferenceDataByExcluding(refDataWrapper, CustomerAccountTypeConstants.SETTLEMENT, CustomerAccountTypeConstants.WALK_IN_CUSTOMER);
        agentAccountTypeModelList = refDataWrapper.getReferenceDataList();

        allAccountTypeModelList = new ArrayList<>(customerAccountTypeModelList.size() + agentAccountTypeModelList.size());
        allAccountTypeModelList.addAll(customerAccountTypeModelList);
        allAccountTypeModelList.addAll(agentAccountTypeModelList);


        CardFeeTypeModel cardFeeTypeModel = new CardFeeTypeModel();
        refDataWrapper = new ReferenceDataWrapperImpl(cardFeeTypeModel, "name", SortingOrder.ASC);
        refDataWrapper = commonFacade.getReferenceData(refDataWrapper);
        cardFeeTypeModelList = refDataWrapper.getReferenceDataList();


        DistributorModel distributorModel = new DistributorModel();
        refDataWrapper = new ReferenceDataWrapperImpl(distributorModel, "name", SortingOrder.ASC);
        refDataWrapper = commonFacade.getReferenceData(refDataWrapper);
        distributorModelList = refDataWrapper.getReferenceDataList();

        refDataMap.put("cardTypeModelList",cardTypeModelList);
        refDataMap.put("cardProdCodeModelList",cardProdCodeModelList);
        refDataMap.put("appUserTypeModelList", appUserTypeModelList);
        refDataMap.put("segmentModelList", segmentModelList);
        refDataMap.put("customerAccountTypeModelList", customerAccountTypeModelList);
        refDataMap.put("agentAccountTypeModelList", agentAccountTypeModelList);
        refDataMap.put("allAccountTypeModelList", allAccountTypeModelList);
        refDataMap.put("cardFeeTypeModelList", cardFeeTypeModelList);
        refDataMap.put("distributorModelList", distributorModelList);

        return refDataMap;
    }

    @Override
    protected CardFeeRuleVO loadFormBackingObject( HttpServletRequest request ) throws Exception
    {
        CardFeeRuleVO cardFeeRuleVO = new CardFeeRuleVO();
        CardFeeRuleModel cardFeeRuleModel = new CardFeeRuleModel();
        cardFeeRuleModel.setIsDeleted(false);

        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setBasePersistableModel(cardFeeRuleModel);
        searchBaseWrapper = cardConfigurationManager.searchCardFeeRuleConfiguration(searchBaseWrapper);
        CustomList<CardFeeRuleModel> customList = searchBaseWrapper.getCustomList();
        if( customList != null )
        {
            List<CardFeeRuleModel> cardFeeRuleModelList = customList.getResultsetList();

            if( CollectionUtils.isNotEmpty(cardFeeRuleModelList) )
            {
                cardFeeRuleVO.setCardFeeRuleModelList(cardFeeRuleModelList);
            }
        }

        //Add 1 object to make sure that table on screen has atleast one row
        if( CollectionUtils.isEmpty(cardFeeRuleVO.getCardFeeRuleModelList()) )
        {
            cardFeeRuleVO.addCardFeeRuleModel(new CardFeeRuleModel());
        }
        return cardFeeRuleVO;
    }

    @Override
    protected ModelAndView onCreate( HttpServletRequest request, HttpServletResponse response,
                                     Object command, BindException errors ) throws Exception
    {
        ModelAndView modelAndView = null;
        CardFeeRuleVO cardFeeRuleVO = (CardFeeRuleVO) command;
        Date now = new Date();
        List<CardFeeRuleModel> cardFeeRuleModelList = cardFeeRuleVO.getCardFeeRuleModelList();
        for(CardFeeRuleModel cardFeeRuleModel : cardFeeRuleModelList)
        {
            if(UserUtils.getCurrentUser().getMnoId() != null && UserUtils.getCurrentUser().getMnoId().equals(50028L))
                cardFeeRuleModel.setMnoId(50028L);
            else
                cardFeeRuleModel.setMnoId(50027L);
            cardFeeRuleModel.setCreatedOn(now);
            cardFeeRuleModel.setUpdatedOn(now);
            cardFeeRuleModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
            cardFeeRuleModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
        }
        CardFeeRuleModel cardFeeRuleModel = new CardFeeRuleModel();
        List<CardFeeRuleModel> existingCardFeeRuleModelList =null;
        cardFeeRuleModel.setIsDeleted(false);
        if(UserUtils.getCurrentUser().getMnoId() != null && UserUtils.getCurrentUser().getMnoId().equals(50028L))
            cardFeeRuleModel.setMnoId(50028L);
        else
            cardFeeRuleModel.setMnoId(50027L);
        try
        {
            SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
            searchBaseWrapper.setBasePersistableModel(cardFeeRuleModel);
            searchBaseWrapper = cardConfigurationManager.searchCardFeeRuleConfiguration(searchBaseWrapper);
            CustomList<CardFeeRuleModel> customList = searchBaseWrapper.getCustomList();
            if( customList != null )
            {
                existingCardFeeRuleModelList = customList.getResultsetList();
            }
            List<CardFeeRuleModel> mergedList = new ArrayList<CardFeeRuleModel>();
            for(CardFeeRuleModel existingModel : existingCardFeeRuleModelList){
                boolean isExist = false;
                for(CardFeeRuleModel model : cardFeeRuleModelList){
                    if( model.getCardFeeRuleId() != null && existingModel.getCardFeeRuleId().longValue() == model.getCardFeeRuleId().longValue()){
                        isExist = true;
                        break;
                    }
                }
                if(!isExist){
                    existingModel.setIsDeleted(true);
                    existingModel.setUpdatedOn(now);
                    existingModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
                    mergedList.add(existingModel);
                }
            }
            mergedList.addAll(cardFeeRuleModelList);
            cardFeeRuleVO.setCardFeeRuleModelList(mergedList);
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
            baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, "");
            baseWrapper.putObject(CardFeeRuleVO.class.getSimpleName(), cardFeeRuleVO);
            cardConfigurationManager.saveOrUpdateAllCardFeeRules(baseWrapper);
            this.saveMessage(request, "Debit Card Fee rules saved successfully.");
            modelAndView = new ModelAndView(this.getSuccessView(),"cardFeeRuleModelList",cardFeeRuleVO.getCardFeeRuleModelList());
        }
        catch(HibernateOptimisticLockingFailureException ex)
        {
            logger.error("HibernateOptimisticLockingFailureException Error while saving Debit Card Fee Rules in onCreate() :: " + ex.getMessage(),ex);
            super.saveMessage(request, "Debit Card Fee Configuration has been updated by another user. Kindly try again.");
            modelAndView = new ModelAndView(getSuccessView());
        }
        catch(Exception ex)
        {
            logger.error("Error while saving Debit Card Fee Rules in onCreate() :: " + ex.getMessage(),ex);
            super.saveMessage(request, "Record could not be saved.");
            modelAndView = super.showForm(request, response, errors);
        }
        return modelAndView;
    }

    @Override
    protected ModelAndView onUpdate( HttpServletRequest request, HttpServletResponse response,
                                     Object command, BindException errors ) throws Exception
    {
        ModelAndView modelAndView;
        try
        {
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_DELETE);
            baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, "");
            cardConfigurationManager.removeAllCardFeeRules(baseWrapper);
            this.saveMessage(request, "Debit card fee rules removed successfully.");
            modelAndView = new ModelAndView(this.getSuccessView());
        }catch(Exception ex)
        {
            logger.error(ex.getMessage(),ex);

            super.saveMessage(request, "Record could not be saved.");
            modelAndView = super.showForm(request, response, errors);
        }

        return modelAndView;
    }

    public void setCommonFacade( CommonFacade commonFacade )
    {
        this.commonFacade = commonFacade;
    }

    public void setDebitCardManager(DebitCardManager debitCardManager) {
        this.debitCardManager = debitCardManager;
    }

    public void setCardConfigurationManager(CardConfigurationManager cardConfigurationManager) {
        this.cardConfigurationManager = cardConfigurationManager;
    }
}



