package com.inov8.microbank.webapp.action.portal.mfsaccountmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.agenthierarchy.AreaLevelModel;
import com.inov8.microbank.common.model.agenthierarchy.RegionModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.server.facade.RetailerFacade;
import com.inov8.microbank.server.facade.portal.mfsaccountmodule.MfsAccountFacade;
import com.inov8.microbank.server.service.agenthierarchy.AgentHierarchyManager;
import com.inov8.microbank.server.service.distributormodule.DistributorLevelManager;
import com.inov8.microbank.server.service.transactionmodule.TransactionDetailMasterManager;
import com.inov8.ola.server.facade.AccountFacade;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgentMerchantDetailsFormController extends AdvanceFormController
{
	private ReferenceDataManager referenceDataManager;
    private MfsAccountFacade mfsAccountFacade;
    private AccountFacade accountFacade;
    private AgentHierarchyManager agentHierarchyManager;
    private DistributorLevelManager distributorLevelManager;
	private RetailerFacade retailerFacade;
	private TransactionDetailMasterManager transactionDetailMasterManager;
 
    public AgentMerchantDetailsFormController()
	{
		setCommandName("agentMerchantDetailModel");
		setCommandClass(AgentMerchantDetailModel.class);
	}

	@Override
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
	{
		super.initBinder(request, binder);
		CommonUtils.bindCustomDateEditor(binder);
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest request) throws Exception
	{
		Map<String, Object> referenceDataMap = new HashMap<String, Object>(7);
		AgentMerchantDetailModel agentMerchantDetailModel = new AgentMerchantDetailModel();
		String initAppFormNumber	=	ServletRequestUtils.getStringParameter(request, "initAppFormNumber");

		if(initAppFormNumber!=null) {
			agentMerchantDetailModel.setInitialAppFormNo(initAppFormNumber);
			
			BaseWrapper baseWrapper=new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(agentMerchantDetailModel);
			mfsAccountFacade.findAgentMerchantByInitialAppFormNo(baseWrapper);
			agentMerchantDetailModel=(AgentMerchantDetailModel) baseWrapper.getBasePersistableModel();
          
			if(null==agentMerchantDetailModel.getParentAgentId()){
				agentMerchantDetailModel.setIsHead("true");
			}
			else
			{
				agentMerchantDetailModel.setIsHead("false");
			}
			
			SearchBaseWrapper wrapper = agentHierarchyManager.findRegionsByDistributorId( agentMerchantDetailModel.getDistributorId() );
            CustomList<RegionModel> regionModelList = wrapper.getCustomList();
            if( regionModelList != null ){
                List<RegionModel> regionList = regionModelList.getResultsetList();
                referenceDataMap.put("regionList", regionList);
            }
            
            if(null!=agentMerchantDetailModel.getRegionId())
           {
            	wrapper = agentHierarchyManager.findAreaLevelsByRegionId( agentMerchantDetailModel.getRegionId() );
	           CustomList<AreaLevelModel> areaLevelModelList = wrapper.getCustomList();
	           if( areaLevelModelList != null ){
	                List<AreaLevelModel> areaLevelList = areaLevelModelList.getResultsetList();
	                referenceDataMap.put("areaLevelList", areaLevelList);
	           }
           }
            
            if(null!=agentMerchantDetailModel.getRegionId())
           {
	           wrapper = agentHierarchyManager.findAgentLevelsByRegionId( agentMerchantDetailModel.getRegionId() );
	           CustomList<DistributorLevelModel> distributorLevelModelList = wrapper.getCustomList();
	           if( distributorLevelModelList != null )
	           {
	               List<DistributorLevelModel> distributorLevelList = distributorLevelModelList.getResultsetList();
	               referenceDataMap.put("agentTypeModelList", distributorLevelList);
	           }
           }
           
         /*   if(null!=agentMerchantDetailModel.getRetailerId() && agentMerchantDetailModel.getRetailerId().intValue()!=-2)
           {
	           wrapper = new SearchBaseWrapperImpl();
	           RetailerModel retailerModel = new RetailerModel();
	           retailerModel.setRetailerId(agentMerchantDetailModel.getRetailerId());

	           wrapper.setBasePersistableModel(retailerModel);
	           wrapper = this.retailerManager.loadRetailer(wrapper);
	           retailerModel = (RetailerModel) wrapper.getBasePersistableModel();
	           List<RetailerModel> retailerList = new ArrayList<>();
	           retailerList.add(retailerModel);
	           referenceDataMap.put("retailerModelList", retailerList);
           }*/
            
            if(null!=agentMerchantDetailModel.getAreaLevelId())
           {
	          wrapper = agentHierarchyManager.findAreaNamesByAreaLevelId( agentMerchantDetailModel.getAreaLevelId() );
	          CustomList<AreaModel> areaModelList = wrapper.getCustomList();
	          if( areaModelList != null ) {
	               List<AreaModel> areaList = areaModelList.getResultsetList();
	               referenceDataMap.put("areaList", areaList);
	          }
           }
          
          baseWrapper = new BaseWrapperImpl();
        /* DistributorLevelModel distributorLevelModel = new DistributorLevelModel();
          
         if(null!=agentMerchantDetailModel.getDistributorLevelId())
         {
	          distributorLevelModel.setDistributorLevelId(agentMerchantDetailModel.getDistributorLevelId());
	          baseWrapper.setBasePersistableModel(distributorLevelModel);
	          baseWrapper = distributorLevelManager.loadDistributorLevel(baseWrapper);
	          distributorLevelModel =  (DistributorLevelModel) baseWrapper.getBasePersistableModel();
	          Long parentAgentLevel = distributorLevelModel.getManagingLevelId();
				if(parentAgentLevel != null) 
				{
					SearchBaseWrapper searchBaseWrapper = agentHierarchyManager.findParentAgents(parentAgentLevel, agentMerchantDetailModel.getRetailerId());
					if(searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0) {
						List<RetailerContactModel> parentAgents = searchBaseWrapper.getCustomList().getResultsetList();
						referenceDataMap.put("parentAgents", parentAgents);
					}
				}
         }*/
			
           /* if(null!=agentMerchantDetailModel.getRetailerId() && agentMerchantDetailModel.getRetailerId().intValue()!=-2)
           {
				wrapper = agentHierarchyManager.findPartnerGroupsByRetailer( agentMerchantDetailModel.getRetailerId() );
	            CustomList<PartnerGroupModel> partnerGroupModelList = wrapper.getCustomList();
	            if( partnerGroupModelList != null )
	            {
	                List<PartnerGroupModel> partnerGroupList = partnerGroupModelList.getResultsetList();
	                referenceDataMap.put("partnerGroupModelList", partnerGroupList);
	            }
           }*/
		}
		else
		{
			
			AgentMerchantDetailModel agentMerchantDetailReqModel = (AgentMerchantDetailModel) request.getAttribute("commandObject");
			request.removeAttribute("commandObject");
			if(null!= agentMerchantDetailReqModel){
				SearchBaseWrapper wrapper = agentHierarchyManager.findRegionsByDistributorId( agentMerchantDetailReqModel.getDistributorId() );
	            CustomList<RegionModel> regionModelList = wrapper.getCustomList();
	            if( regionModelList != null ){
	                List<RegionModel> regionList = regionModelList.getResultsetList();
	                referenceDataMap.put("regionList", regionList);
	            }
	            
	            if(null!=agentMerchantDetailReqModel.getRegionId())
	           {
	            	wrapper = agentHierarchyManager.findAreaLevelsByRegionId( agentMerchantDetailReqModel.getRegionId() );
		           CustomList<AreaLevelModel> areaLevelModelList = wrapper.getCustomList();
		           if( areaLevelModelList != null ){
		                List<AreaLevelModel> areaLevelList = areaLevelModelList.getResultsetList();
		                referenceDataMap.put("areaLevelList", areaLevelList);
		           }
	           }
	            
	            if(null!=agentMerchantDetailReqModel.getRegionId())
	           {
		           wrapper = agentHierarchyManager.findAgentLevelsByRegionId( agentMerchantDetailReqModel.getRegionId() );
		           CustomList<DistributorLevelModel> distributorLevelModelList = wrapper.getCustomList();
		           if( distributorLevelModelList != null )
		           {
		               List<DistributorLevelModel> distributorLevelList = distributorLevelModelList.getResultsetList();
		               referenceDataMap.put("agentTypeModelList", distributorLevelList);
		           }
	           }
	         
	            
	            if(null!=agentMerchantDetailReqModel.getAreaLevelId())
	           {
		          wrapper = agentHierarchyManager.findAreaNamesByAreaLevelId( agentMerchantDetailReqModel.getAreaLevelId() );
		          CustomList<AreaModel> areaModelList = wrapper.getCustomList();
		          if( areaModelList != null ) {
		               List<AreaModel> areaList = areaModelList.getResultsetList();
		               referenceDataMap.put("areaList", areaList);
		          }
	           }
			}
			else
			{
	            referenceDataMap.put("regionList", new ArrayList<RegionModel>());
	            referenceDataMap.put("areaLevelList", new ArrayList<AreaLevelModel>());
	            referenceDataMap.put("agentTypeModelList", new ArrayList<DistributorLevelModel>());
	           /* referenceDataMap.put("retailerModelList", new ArrayList<RetailerModel>());*/
	            referenceDataMap.put("areaList", new ArrayList<AreaModel>());
				referenceDataMap.put("parentAgents", new ArrayList<RetailerContactModel>());
	           /* referenceDataMap.put("partnerGroupModelList", new ArrayList<PartnerGroupModel>());*/
			}
		
			
			
		}
		
		ProductCatalogModel productCatalogModel= new ProductCatalogModel();

		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(productCatalogModel, "name", SortingOrder.ASC);
		referenceDataManager.getReferenceData(referenceDataWrapper);
		List<AgentRelationshipTypeModel> agentRelationshipTypeModelList = referenceDataWrapper.getReferenceDataList();
		referenceDataMap.put("agentRelationshipTypeModelList", agentRelationshipTypeModelList);

		referenceDataWrapper = new ReferenceDataWrapperImpl(new DistributorModel(), "distributorId", SortingOrder.ASC);
		referenceDataManager.getReferenceData(referenceDataWrapper);
		List<DistributorModel> distributorModelList = referenceDataWrapper.getReferenceDataList();
		referenceDataMap.put("distributorModelList", distributorModelList);
		
		SearchBaseWrapper searchBaseWrapper=accountFacade.getAllAgentAccountTypes();
		
		if(searchBaseWrapper.getCustomList()!=null)
		{
			List<LabelValueBean> acLevelQualificationModelList=new ArrayList<LabelValueBean>();
			List<OlaCustomerAccountTypeModel>	olaCustomerAccountTypeModelList	=	searchBaseWrapper.getCustomList().getResultsetList();
			
			for (OlaCustomerAccountTypeModel accountTypeModel : olaCustomerAccountTypeModelList)
			{
				acLevelQualificationModelList.add(new LabelValueBean(accountTypeModel.getName(), String.valueOf(accountTypeModel.getCustomerAccountTypeId())));
			}
			
			referenceDataMap.put("acLevelQualificationModelList", acLevelQualificationModelList);
		}
		
		return referenceDataMap;
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest request) throws Exception
	{
		AgentMerchantDetailModel agentMerchantDetailModel = new AgentMerchantDetailModel();
		agentMerchantDetailModel.setIsHead("false");
		String initAppFormNumber	=	ServletRequestUtils.getStringParameter(request, "initAppFormNumber");
		
		if(initAppFormNumber!=null){
			agentMerchantDetailModel.setInitialAppFormNo(initAppFormNumber);
			BaseWrapper baseWrapper=new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(agentMerchantDetailModel);
			mfsAccountFacade.findAgentMerchantByInitialAppFormNo(baseWrapper);
			agentMerchantDetailModel=(AgentMerchantDetailModel) baseWrapper.getBasePersistableModel();
			agentMerchantDetailModel.setConfirmPassword(agentMerchantDetailModel.getPassword());
			agentMerchantDetailModel.setIsPasswordChanged(false);
			AppUserModel temp = new AppUserModel();
			temp.setUsername(agentMerchantDetailModel.getUserName());
			/*AppUserModel appUserModel = mfsAccountFacade.getAppUserModel(temp);
			if(appUserModel != null && (appUserModel.getRegistrationStateId().equals(RegistrationStateConstantsInterface.VERIFIED)
					|| appUserModel.getRegistrationStateId().equals(RegistrationStateConstantsInterface.BULK_RQST_RCVD)
					|| appUserModel.getRegistrationStateId().equals(RegistrationStateConstantsInterface.RQST_RCVD))
					&& !isChildAgentExist(agentMerchantDetailModel.getInitialAppFormNo())
					&& transactionDetailMasterManager.findAgentPendingTrxByCNIC(appUserModel.getNic()) == 0)
			{
				agentMerchantDetailModel.setActionId(PortalConstants.ACTION_UPDATE);
				agentMerchantDetailModel.setIsRegionChangeAllowed(true);
			}
			else
			{
				agentMerchantDetailModel.setIsRegionChangeAllowed(false);
			}*/
			agentMerchantDetailModel.setIsRegionChangeAllowed(true);
		}
		
		if(agentMerchantDetailModel.getRetailerId()!=null){
			agentMerchantDetailModel.setCreatedFranchise(true);
		}
		else{
			agentMerchantDetailModel.setCreatedFranchise(false);
		}
		
		if(null!=agentMerchantDetailModel.getParentAgentId())
			agentMerchantDetailModel.setIsHead("false");
		else
			agentMerchantDetailModel.setIsHead("true");
		
		if(agentMerchantDetailModel.getAgentMerchantDetailId()!=null){
			agentMerchantDetailModel.setActionId(PortalConstants.ACTION_UPDATE);
			/*agentMerchantDetailModel.setIsRegionChangeAllowed(false);*/
		}
		else{
			agentMerchantDetailModel.setActionId(PortalConstants.ACTION_CREATE);
			agentMerchantDetailModel.setIsRegionChangeAllowed(true);
			agentMerchantDetailModel.setUsecaseId(new Long(PortalConstants.LEVEL3_AGENT_MERCHANT_USECASE_ID));
		}
		agentMerchantDetailModel.setDirectChangedToSub(Boolean.FALSE);
		return agentMerchantDetailModel;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        AgentMerchantDetailModel agentMerchantDetailModel= (AgentMerchantDetailModel) command;
        ModelAndView modelAndView = new ModelAndView(new RedirectView("p_agentmerchantdetailsform.html"));
        try {
        	
        	if(!validateRequiredFields(agentMerchantDetailModel, request)){
        		request.setAttribute("commandObject",command);
        		return super.showForm(request, response, errors);
        	}
        	agentMerchantDetailModel.setPassword("@Branchless2");
        	agentMerchantDetailModel.setConfirmPassword("@Branchless2");
//        	agentMerchantDetailModel.setUserName(agentMerchantDetailModel.getInitialAppFormNo());
        	
	        /*Checking passsword policy*/
	       /* PasswordInputDTO passwordInputDTO = new PasswordInputDTO( );
			PasswordResultDTO passwordResultDTO = new PasswordResultDTO( );
			passwordInputDTO.setPassword(agentMerchantDetailModel.getPassword());
			passwordInputDTO.setUserName(agentMerchantDetailModel.getUserName());
			passwordInputDTO.setFirstName(agentMerchantDetailModel.getUserName());
			passwordInputDTO.setLastName(agentMerchantDetailModel.getUserName());
			//check whether password is of minimum 8 characters and combinations of digits, special characters and alphabets.
			passwordResultDTO = PasswordConfigUtil.isValidPasswordStrength(passwordInputDTO);
			if ( !passwordResultDTO.getIsValid() ) { 
				this.saveMessage(request, "Password must be at least 8 Alphanumeric and 1 special character");
				agentMerchantDetailModel.setIsHead("false");
				agentMerchantDetailModel.setError(true);
				request.getSession().setAttribute("agentMerchantDetailModel", agentMerchantDetailModel);
				return modelAndView;
			}
			 //check whether supplied password is same as of user ID
			passwordResultDTO = PasswordConfigUtil.isPasswordContainsUserIDOrName(passwordInputDTO);
			if (!passwordResultDTO.getIsValid()) {
				this.saveMessage(request, "User Name cannot be the password.");
				agentMerchantDetailModel.setIsHead("false");
				agentMerchantDetailModel.setError(true);
				request.getSession().setAttribute("agentMerchantDetailModel", agentMerchantDetailModel);
				return modelAndView;
			}*/
	        
	     
			
	        boolean proceed=false;
       
            Boolean isUnique=mfsAccountFacade.isAlreadyExistInitAppFormNumber(agentMerchantDetailModel.getInitialAppFormNo());
            if(isUnique!=null && isUnique.booleanValue()==true) {
    	        
            	 
            	 /**
    	         * check if Reference No already exist.
    	         */
            	
            	if(null!=agentMerchantDetailModel.getReferenceNo()){
            		Boolean isReferenceNoUnique=mfsAccountFacade.isAlreadyExistReferenceNumber(agentMerchantDetailModel.getReferenceNo(),agentMerchantDetailModel.getInitialAppFormNo());
            		if(isReferenceNoUnique!=null && isReferenceNoUnique.booleanValue()!=true)
            			throw new FrameworkCheckedException("Reference No. already exist.");
            	}
            		
            	
            	/**
    	         * check if username already exist.
    	         */
    		
    			if(mfsAccountFacade.loadAgentMerchantDetailModelByUserName(agentMerchantDetailModel.getUserName())!=null){
    				
    				throw new FrameworkCheckedException("A user with User Name '"+agentMerchantDetailModel.getUserName()+"' already exist.");
    			}
    		
    			if(mfsAccountFacade.isAppUserExist(agentMerchantDetailModel.getUserName())){
    				
    				throw new FrameworkCheckedException("A user with User Name '"+agentMerchantDetailModel.getUserName()+"' already exist.");

    			}
            	
            	
            	String userPasswordToShowIncaseError	=	agentMerchantDetailModel.getPassword();
    	       	
    	        agentMerchantDetailModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
    	        agentMerchantDetailModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
    	        Date dateNow=new Date();
    	        agentMerchantDetailModel.setCreatedOn(dateNow);
    	        agentMerchantDetailModel.setUpdatedOn(dateNow);
    	        
    	        agentMerchantDetailModel.setPassword(EncoderUtils.encodeToSha(agentMerchantDetailModel.getPassword()));
    	        agentMerchantDetailModel.setConfirmPassword(EncoderUtils.encodeToSha(agentMerchantDetailModel.getPassword()));
            
    	        baseWrapper.setBasePersistableModel(agentMerchantDetailModel);
            	
    			baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, agentMerchantDetailModel.getActionId());
    			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, agentMerchantDetailModel.getUsecaseId());
                mfsAccountFacade.createAgentMerchant(baseWrapper);
                this.saveMessage(request, "Form saved successfully.");
                proceed=true;
               
            } else {
                throw new FrameworkCheckedException("Initial Application Form No. already exist.");
            }
            
            if(proceed) 
            {
            	String MNG_L3_KYC_CREATE=PortalConstants.MNG_L3_KYC_CREATE;
        		boolean securityCheck=AuthenticationUtil.checkRightsIfAny(MNG_L3_KYC_CREATE);
        		
        		if(securityCheck){
                	modelAndView = new ModelAndView("redirect:p_l3_kyc_form.html?actionId=1&initAppFormNumber="+agentMerchantDetailModel.getInitialAppFormNo());
        		}
    		} 
            else
            {
            	throw new FrameworkCheckedException(MessageUtil.getMessage("6075"));
    		}
        } catch (FrameworkCheckedException ex) {
        	ex.printStackTrace();
        	request.setAttribute("commandObject",command);
        	if(null!=ex.getMessage() && ex.getMessage().length()>100)
        		this.saveMessage(request,MessageUtil.getMessage("6075"));
        	else
        		this.saveMessage(request,ex.getMessage());
            return super.showForm(request, response, errors);
        }
        catch (Exception ex) {
        	ex.printStackTrace();
        	request.setAttribute("commandObject",command);
            this.saveMessage(request,MessageUtil.getMessage("6075"));
            return super.showForm(request, response, errors);
        }
        return modelAndView;
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception
	{
		ModelAndView modelAndView=null;	
		BaseWrapper baseWrapper = new BaseWrapperImpl();
        AgentMerchantDetailModel agentMerchantDetailModel= (AgentMerchantDetailModel) command;
        if(agentMerchantDetailModel.getDirectChangedToSub()) {
			if(isChildAgentExist(agentMerchantDetailModel.getInitialAppFormNo())){

				super.saveMessage(request, "There exists Child Agents against this Agent, You cannot convert this Agent to Sub Agent!");
				return new ModelAndView(new RedirectView("p_agentmerchantdetailsform.html?initAppFormNumber="+agentMerchantDetailModel.getInitialAppFormNo()));
			}
		}AppUserModel appUserModel_1 = new AppUserModel();
		appUserModel_1.setUsername(agentMerchantDetailModel.getUserName());
		appUserModel_1.setAccountClosedUnsettled(false);
		AppUserModel appUserModel = mfsAccountFacade.getAppUserModel(appUserModel_1);
		if(appUserModel != null)
		{
			if(transactionDetailMasterManager.findAgentPendingTrxByCNIC(appUserModel.getNic()) > 0)
			{
				super.saveMessage(request, "Agent has Pending Transactions, You cannot Migrate Agent Network !");
				return new ModelAndView(new RedirectView("p_agentmerchantdetailsform.html?initAppFormNumber="+agentMerchantDetailModel.getInitialAppFormNo()));
			}
		}
    
     try{   
        baseWrapper.setBasePersistableModel(agentMerchantDetailModel);
        baseWrapper	= this.mfsAccountFacade.findAgentMerchantByInitialAppFormNo(baseWrapper);
        AgentMerchantDetailModel queryObj =  (AgentMerchantDetailModel) baseWrapper.getBasePersistableModel();
		
        //Check if Agent Type Change is Allowed
        if(queryObj.getDistributorLevelId().intValue()!=agentMerchantDetailModel.getDistributorLevelId().intValue()){
        	if(isChildAgentExist(agentMerchantDetailModel.getInitialAppFormNo()))
        		throw new FrameworkCheckedException("Agent Type Cannot be changed from "+queryObj.getDistributorLevelIdDistributorLevelModel().getName()+" as child Agent/s exists.");
        }
        //End Check if Agent Type Change is Allowed
        agentMerchantDetailModel.setVersionNo(queryObj.getVersionNo());

		 /**
		  * check if username already exist.
		  */
		 AgentMerchantDetailModel model = mfsAccountFacade.loadAgentMerchantDetailModelByUserName(agentMerchantDetailModel.getUserName());
		 if(model != null && model.getAgentMerchantDetailId().longValue() != agentMerchantDetailModel.getAgentMerchantDetailId()){

			 throw new FrameworkCheckedException("A user with User Name '"+agentMerchantDetailModel.getUserName()+"' already exist.");
		 }

		 if(!mfsAccountFacade.isAppUserUsernameUniqueForUpdate(agentMerchantDetailModel.getUserName(),agentMerchantDetailModel.getInitialAppFormNo())){

			 throw new FrameworkCheckedException("A user with User Name '"+agentMerchantDetailModel.getUserName()+"' already exist");

		 }

		 modelAndView = new ModelAndView(new RedirectView("p_agentmerchantdetailsform.html?initAppFormNumber="+agentMerchantDetailModel.getInitialAppFormNo()));
        
        	
        	if(!validateRequiredFields(agentMerchantDetailModel, request)){
        		request.setAttribute("commandObject",command);
        		return super.showForm(request, response, errors);
        	}
        	
        	if(null!=agentMerchantDetailModel.getReferenceNo()){
        		Boolean isReferenceNoUnique=mfsAccountFacade.isAlreadyExistReferenceNumber(agentMerchantDetailModel.getReferenceNo(),agentMerchantDetailModel.getInitialAppFormNo());
        		if(isReferenceNoUnique!=null && isReferenceNoUnique.booleanValue()!=true)
        			throw new FrameworkCheckedException("Reference No. already exist.");
        	}
        	
	        /*Checking passsword policy*/
	       /* PasswordInputDTO passwordInputDTO = new PasswordInputDTO( );
			PasswordResultDTO passwordResultDTO = new PasswordResultDTO( );
			passwordInputDTO.setPassword(agentMerchantDetailModel.getPassword());
			passwordInputDTO.setUserName(agentMerchantDetailModel.getUserName());
			passwordInputDTO.setFirstName(agentMerchantDetailModel.getUserName());
			passwordInputDTO.setLastName(agentMerchantDetailModel.getUserName());
			//check whether password is of minimum 8 characters and combinations of digits, special characters and alphabets.
			if(agentMerchantDetailModel.getIsPasswordChanged()){
				passwordResultDTO = PasswordConfigUtil.isValidPasswordStrength(passwordInputDTO);
				if ( !passwordResultDTO.getIsValid() ){ 
					this.saveMessage(request, "Password must be at least 8 Alphanumeric and 1 special character");
					agentMerchantDetailModel.setIsHead("false");
					agentMerchantDetailModel.setError(true);
					request.getSession().setAttribute("agentMerchantDetailModel", agentMerchantDetailModel);
					return modelAndView;
				}
				 //check whether supplied password is same as of user ID
				passwordResultDTO = PasswordConfigUtil.isPasswordContainsUserIDOrName(passwordInputDTO);
				if (!passwordResultDTO.getIsValid()){
					this.saveMessage(request, "User Name cannot be the password.");
					agentMerchantDetailModel.setIsHead("false");
					agentMerchantDetailModel.setError(true);
					request.getSession().setAttribute("agentMerchantDetailModel", agentMerchantDetailModel);
					return modelAndView;
				}
				agentMerchantDetailModel.setPassword(EncoderUtils.encodeToSha(agentMerchantDetailModel.getPassword()));
				agentMerchantDetailModel.setConfirmPassword(EncoderUtils.encodeToSha(agentMerchantDetailModel.getPassword()));
			}*/
		
	        agentMerchantDetailModel.setUpdatedOn(new Date());
	        
	        baseWrapper.setBasePersistableModel(agentMerchantDetailModel);
	        boolean proceed=false;
	            mfsAccountFacade.updateAgentMerchant(baseWrapper);
	            this.saveMessage(request, "Form updated successfully.");
	            proceed=true;

	        if(proceed)
			{
	        	String l3KycPermission=PortalConstants.MNG_L3_KYC_CREATE+","+PortalConstants.MNG_L3_KYC_UPDATE+","+PortalConstants.MNG_L3_KYC_READ;
        		boolean securityCheck=AuthenticationUtil.checkRightsIfAny(l3KycPermission);
        		
        		if(securityCheck){
                	modelAndView = new ModelAndView("redirect:p_l3_kyc_form.html?actionId=1&initAppFormNumber="+agentMerchantDetailModel.getInitialAppFormNo());
        		}
			}
	        else
			{
	        	request.setAttribute("commandObject",command);
	        	throw new FrameworkCheckedException(MessageUtil.getMessage("6075"));
			}
        } catch (FrameworkCheckedException fce) {
        	fce.printStackTrace();
        	request.setAttribute("commandObject",command);
        	if(null!=fce.getMessage() && fce.getMessage().length()>100)
        		this.saveMessage(request,MessageUtil.getMessage("6075"));
        	else
        		this.saveMessage(request,fce.getMessage());
        	return super.showForm(request, response, errors);
        }catch (Exception ex) {
        	ex.printStackTrace();
        	request.setAttribute("commandObject",command);
            this.saveMessage(request,MessageUtil.getMessage("6075"));
            return super.showForm(request, response, errors);
        }
        
        return modelAndView;
	}
	
	private boolean validateRequiredFields(AgentMerchantDetailModel agentMerchantDetailModel, HttpServletRequest request){
		boolean flag=true;
		
		if(agentMerchantDetailModel.getIsHead().equals("false")){
			if(agentMerchantDetailModel.getRetailerId()==null)
			{
				super.saveMessage(request, "Franchise/Branch: is required.");
				flag=false;
			}
			
			/*if(agentMerchantDetailModel.getPartnerGroupId()==null)
			{
				super.saveMessage(request, "User Group: is required.");
				flag=false;
			}*/
			if(agentMerchantDetailModel.getParentAgentId()==null)
			{
				super.saveMessage(request, "Parent Agent: is required.");
				flag=false;
			}
		}
		
		
		return flag;
	}
	
	
	
	private Boolean isChildAgentExist(String initAppFormNumber) throws FrameworkCheckedException{
		RetailerContactModel retailerContactModel = new RetailerContactModel();
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		retailerContactModel.setInitialAppFormNo(initAppFormNumber);
		searchBaseWrapper.setBasePersistableModel(retailerContactModel);
		retailerFacade.loadRetailerByInitialAppFormNo(searchBaseWrapper);
		
		if(null!=searchBaseWrapper.getCustomList().getResultsetList() && searchBaseWrapper.getCustomList().getResultsetList().size()>0){
			retailerContactModel = (RetailerContactModel) searchBaseWrapper.getCustomList().getResultsetList().get(0);
			List<RetailerContactModel> retailerContactModelList = agentHierarchyManager.findChildRetailerContactsById(retailerContactModel.getRetailerContactId());
			
			if(null!=retailerContactModelList && retailerContactModelList.size()>0)
				return true;
			else
				return false;
		}
		else
			return false;
	}
	

	public ReferenceDataManager getReferenceDataManager()
	{
		return referenceDataManager;
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
	{
		this.referenceDataManager = referenceDataManager;
	}

    public void setMfsAccountFacade(MfsAccountFacade mfsAccountFacade) {
        this.mfsAccountFacade = mfsAccountFacade;
    }

    public MfsAccountFacade getMfsAccountFacade() {
        return mfsAccountFacade;
    }

	public void setAccountFacade(AccountFacade accountFacade)
	{
		this.accountFacade = accountFacade;
	}

	public void setAgentHierarchyManager(AgentHierarchyManager agentHierarchyManager)
	{
		this.agentHierarchyManager = agentHierarchyManager;
	}

	public void setDistributorLevelManager(
			DistributorLevelManager distributorLevelManager) {
		if (distributorLevelManager != null) {
			this.distributorLevelManager = distributorLevelManager;
		}
	}

	public RetailerFacade getRetailerFacade() {
		return retailerFacade;
	}

	public void setRetailerFacade(RetailerFacade retailerFacade) {
		this.retailerFacade = retailerFacade;
	}

	public void setTransactionDetailMasterManager(TransactionDetailMasterManager transactionDetailMasterManager) {
		this.transactionDetailMasterManager = transactionDetailMasterManager;
	}
}
