package com.inov8.microbank.account.controller;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.account.model.BlacklistMarkingViewModel;
import com.inov8.microbank.account.service.AccountControlManager;
import com.inov8.microbank.account.vo.BlacklistMarkingVo;
import com.inov8.microbank.common.model.AppUserTypeModel;
import com.inov8.microbank.common.model.RegistrationStateModel;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.RegistrationStateConstantsInterface;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Malik on 8/10/2016.
 */
public class BlacklistMarkingController extends BaseFormSearchController
{
    private ReferenceDataManager referenceDataManager;
    private AccountControlManager accountControlManager;

    public BlacklistMarkingController()
    {
        setCommandName("blacklistMarkingViewModel");
        setCommandClass(BlacklistMarkingViewModel.class);
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception
    {
        Map<String,Object> referenceDataMap=new HashMap<>(2);
        RegistrationStateModel registrationStateModel = new RegistrationStateModel();
        List<RegistrationStateModel> registrationStateList = null;
        AppUserTypeModel appUserTypeModel = new AppUserTypeModel();
        List<AppUserTypeModel> appUserTypeModelList = null;

        ReferenceDataWrapper registrationStateDataWrapper = new ReferenceDataWrapperImpl(registrationStateModel, "name", SortingOrder.ASC);
        registrationStateList= (List<RegistrationStateModel>) referenceDataManager.getReferenceData(registrationStateDataWrapper).getReferenceDataList();
        referenceDataMap.put("registrationStateList",registrationStateList);

        ReferenceDataWrapper appUserTypeDataWrapper = new ReferenceDataWrapperImpl(appUserTypeModel, "name", SortingOrder.ASC);
        appUserTypeModelList= (List<AppUserTypeModel>) referenceDataManager.getReferenceData(appUserTypeDataWrapper, UserTypeConstantsInterface.CUSTOMER,UserTypeConstantsInterface.RETAILER,UserTypeConstantsInterface.WALKIN_CUSTOMER,UserTypeConstantsInterface.HANDLER).getReferenceDataList();
        referenceDataMap.put("appUserTypeModelList",appUserTypeModelList);

        return referenceDataMap;
    }

    @Override
    protected ModelAndView onSearch(HttpServletRequest req, HttpServletResponse httpServletResponse, Object model, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception
    {
        Long actionId = ServletRequestUtils.getRequiredLongParameter(req, PortalConstants.KEY_ACTION_ID);       
        String cnicList = ServletRequestUtils.getStringParameter(req, "cnicList");   
        
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        
        searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
        BlacklistMarkingViewModel blacklistMarkingViewModel = (BlacklistMarkingViewModel) model;
        
       //************************************************************************************* 
        // List of All records in a chunk i.e. 20, 50, 100
        List<BlacklistMarkingVo> voList = blacklistMarkingViewModel.getBlacklistMarkingVoList();
        // List of only modified CNICS
        List<BlacklistMarkingVo> newVoList = new ArrayList<BlacklistMarkingVo>();
        List<String> changedCnicArrayList = null;
        boolean found = false;  
        if(cnicList != null){
        	changedCnicArrayList = Arrays.asList(cnicList.split(","));        	
        	          
        	for(int i=0; i < changedCnicArrayList.size(); i++ ){       		
        		for(int j=0;j< voList.size(); j++){      			
        			if(changedCnicArrayList.get(i).equals(voList.get(j).getCnicNo())){
        				found = true;
        				BlacklistMarkingVo vo = voList.get(j);
        				newVoList.add(vo);
        				
        				String cnic =  changedCnicArrayList.get(i);
         				
        				
        			}
        			 			
        		}
         	}
         
        }
        
        System.out.println(newVoList);
        blacklistMarkingViewModel.setBlacklistMarkingVoList(newVoList);
        
        //************************************************************************************
        searchBaseWrapper.setBasePersistableModel(blacklistMarkingViewModel);
        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel();
        
        searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);

        if (sortingOrderMap.isEmpty()) {
            sortingOrderMap.put("appUserId", SortingOrder.DESC);
        }

        searchBaseWrapper.setSortingOrderMap(sortingOrderMap);

        if(!blacklistMarkingViewModel.getBlacklistMarkingVoList().isEmpty() && actionId.equals(PortalConstants.ACTION_UPDATE))
        {
            String regStateId = ServletRequestUtils.getStringParameter(req, PortalConstants.KEY_REG_STATE_ID);
            String cnic = ServletRequestUtils.getStringParameter(req, PortalConstants.KEY_CNIC_NO);
            String mobNo = ServletRequestUtils.getStringParameter(req, PortalConstants.KEY_MOBILE_NO);
            String appUserTypeId = ServletRequestUtils.getStringParameter(req, PortalConstants.KEY_APP_USER_TYPE_ID);
            String userId = ServletRequestUtils.getStringParameter(req, PortalConstants.KEY_USER_ID);
            if(regStateId!=null && regStateId!="") {
                blacklistMarkingViewModel.setRegStateId(Long.parseLong(regStateId));
            }
            blacklistMarkingViewModel.setCnic(cnic);
            blacklistMarkingViewModel.setMobileNo(mobNo);
            if(appUserTypeId!=null && appUserTypeId!="") {
                blacklistMarkingViewModel.setAppUserTypeId(Long.parseLong(appUserTypeId));
            }
            blacklistMarkingViewModel.setUserId(userId);

            searchBaseWrapper.putObject(PortalConstants.KEY_ACTION_ID, actionId);
            //this.accountControlManager.saveBlacklistMarkingViewModelWithAuthorization(searchBaseWrapper);
            this.accountControlManager.saveBlacklistMarkingViewModel(searchBaseWrapper);
            super.saveMessage(req, MessageUtil.getMessage(MessageUtil.GenericUpdateSuccessMessage));
        }
  
        	 searchBaseWrapper = this.accountControlManager.searchBlacklistMarkingViewModel(searchBaseWrapper);
        
       
        List<BlacklistMarkingViewModel> resultList = searchBaseWrapper.getCustomList().getResultsetList();
        if(resultList.isEmpty())
            pagingHelperModel.setTotalRecordsCount(0);
        else
            pagingHelperModel.setTotalRecordsCount(resultList.size());

        String successView = StringUtil.trimExtension( req.getServletPath() );
        return new ModelAndView(successView,"blacklistMarkingViewModelList", resultList);
    }


    public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
    {
        this.referenceDataManager = referenceDataManager;
    }


    public void setAccountControlManager(AccountControlManager accountControlManager)
    {
        this.accountControlManager = accountControlManager;
    }


}
