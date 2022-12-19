package com.inov8.microbank.webapp.action.portal.ola;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.integration.common.model.LimitModel;
import com.inov8.integration.common.model.LimitTypeModel;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.microbank.common.model.StakeholderBankInfoModel;
import com.inov8.microbank.common.model.stakeholdermodule.StakehldBankInfoListViewModel;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.vo.olacustomeraccounttype.LimitTypeVo;
import com.inov8.microbank.common.vo.olacustomeraccounttype.OlaCustomerAccountTypeVo;
import com.inov8.microbank.server.facade.CommonFacade;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;
import com.inov8.ola.server.facade.AccountFacade;
import com.inov8.ola.server.facade.LimitFacade;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import com.inov8.ola.util.LimitTypeConstants;
import com.inov8.ola.util.TransactionTypeConstants;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Feb 1, 2013 7:29:27 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class CustomerAccountTypeFormController extends AdvanceFormController
{
    //Autowired
    private AccountFacade accountFacade;
    //Autowired
    private CommonFacade commonFacade;
    
    private LimitFacade limitFacade;

	private StakeholderBankInfoManager stakeholderBankInfoManager;
	private AbstractFinancialInstitution phoenixFinancialInstitution;

    public CustomerAccountTypeFormController()
    {
        setCommandName( "olaCustomerAccountTypeVo" );
        setCommandClass( OlaCustomerAccountTypeVo.class );
    }

    @Override
    protected Map<String,Object> loadReferenceData( HttpServletRequest request ) throws Exception
    {
    	Map<String,Object> refDataMap = new HashMap<>(1);

    	List<OlaCustomerAccountTypeModel> accountTypeModelList = accountFacade.searchParentOlaCustomerAccountTypes(
    			CustomerAccountTypeConstants.SETTLEMENT,CustomerAccountTypeConstants.WALK_IN_CUSTOMER);
   		refDataMap.put("accountTypeModelList", accountTypeModelList);

        return refDataMap;
    }

    @Override
    protected OlaCustomerAccountTypeVo loadFormBackingObject( HttpServletRequest request ) throws Exception
    {
        OlaCustomerAccountTypeVo olaCustomerAccountTypeVo = new OlaCustomerAccountTypeVo();
        Boolean isSubAccountType = Boolean.FALSE;

        String customerAccountTypeIdStr = ServletRequestUtils.getStringParameter( request, "customerAccountTypeId" );
        List<LimitTypeVo> limitTypeVoList = null;
		if( GenericValidator.isBlankOrNull( customerAccountTypeIdStr ) ) //Create Case
        {
			olaCustomerAccountTypeVo.setActive(true);
			limitTypeVoList = loadLimitTypeModelList(null);
            olaCustomerAccountTypeVo.setLimitTypeVoList( limitTypeVoList );
         }
         else
         {
        	 Long customerAccountTypeId = Long.valueOf(customerAccountTypeIdStr);

        	limitTypeVoList = loadLimitTypeModelList(customerAccountTypeId);
            olaCustomerAccountTypeVo.setLimitTypeVoList( limitTypeVoList );
            
            OlaCustomerAccountTypeModel olaCustomerAccountTypeModel = new OlaCustomerAccountTypeModel( customerAccountTypeId );
            SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
            searchBaseWrapper.setBasePersistableModel( olaCustomerAccountTypeModel );
            try
            {
                searchBaseWrapper = accountFacade.searchOlaCustomerAccountTypes( searchBaseWrapper );
                CustomList<OlaCustomerAccountTypeModel> customList = searchBaseWrapper.getCustomList();
                if( customList != null )
                {
                    List<OlaCustomerAccountTypeModel> olaCustomerAccountTypeModelList = customList.getResultsetList();
                    olaCustomerAccountTypeVo = new OlaCustomerAccountTypeVo();
                    for(OlaCustomerAccountTypeModel model : olaCustomerAccountTypeModelList){
                    	
                    	olaCustomerAccountTypeVo.setName(model.getName());
                    	olaCustomerAccountTypeVo.setActive(model.getActive());
                    	olaCustomerAccountTypeVo.setIsCategoryCustomerAccountType(model.getIsCustomerAccountType()); // added by Turab
                    	olaCustomerAccountTypeVo.setCustomerAccountTypeId(model.getCustomerAccountTypeId());
                    	olaCustomerAccountTypeVo.setName(model.getName());
                    	olaCustomerAccountTypeVo.setParentAccountTypeId(model.getParentAccountTypeId());
                    	
                    	olaCustomerAccountTypeVo.setDormantMarkingEnabled(model.getDormantMarkingEnabled());
                    	olaCustomerAccountTypeVo.setDormantTimePeriod(model.getDormantTimePeriod());
                    	
                    	if(model.getParentAccountTypeId() != null)
                    	{
                    		isSubAccountType = Boolean.TRUE;
						}

                    	searchBaseWrapper =new SearchBaseWrapperImpl();
						StakehldBankInfoListViewModel stakehldBankInfoListViewModel =new StakehldBankInfoListViewModel();
						stakehldBankInfoListViewModel.setCustomerAccountTypeId(model.getCustomerAccountTypeId());
						stakehldBankInfoListViewModel.setBankId(CommissionConstantsInterface.BANK_ID);
						searchBaseWrapper.setBasePersistableModel(stakehldBankInfoListViewModel);
						
						searchBaseWrapper = this.stakeholderBankInfoManager.searchStakeHolderBankInfo(searchBaseWrapper);
						
						if(null != searchBaseWrapper.getCustomList() && searchBaseWrapper.getCustomList().getResultsetList()!=null && searchBaseWrapper.getCustomList().getResultsetList().size()>0)
						{
							List<StakehldBankInfoListViewModel> resultList	=	(List<StakehldBankInfoListViewModel>) searchBaseWrapper.getCustomList().getResultsetList();
							olaCustomerAccountTypeVo.setAccountNo(resultList.get(0).getAccountNo());
							olaCustomerAccountTypeVo.setAccountNick(resultList.get(0).getName());
						}
						
                    	List<LimitModel> limitsList = limitFacade.getLimitsByCustomerAccountType(model.getCustomerAccountTypeId());
                    	Map<Long, LimitTypeVo> limitVoMap = new HashMap<Long, LimitTypeVo>();
                    	
                    	for (LimitTypeVo p: limitTypeVoList) { 
                    		limitVoMap.put(p.getLimitTypeId(), p); 
                    	}
                    	
                    	for(LimitModel limit : limitsList){
                    		LimitTypeVo vo = limitVoMap.get(limit.getLimitTypeId());
                    		if(vo == null){
                    			vo = new LimitTypeVo();
                    			vo.setLimitTypeId(limit.getLimitTypeId());
                    			limitVoMap.put(limit.getLimitTypeId(), vo);
                    		}
                    		
                    		vo.setApplicable(limit.getIsApplicable());
                    		 
                    		if(TransactionTypeConstants.DEBIT.longValue() == limit.getTransactionTypeId().longValue()){
                    			 vo.setDebitLimit(limit.getMaximum());
                    			 vo.setDebitLimitId(limit.getLimitId());
                             }else if(TransactionTypeConstants.CREDIT.longValue() == limit.getTransactionTypeId().longValue()){
                            	 vo.setCreditLimit(limit.getMaximum());
                            	 vo.setCreditLimitId(limit.getLimitId());
                             }
                    	}
                    	
                    	limitTypeVoList = new ArrayList<LimitTypeVo>();
                    	limitTypeVoList.addAll(limitVoMap.values());
                    	olaCustomerAccountTypeVo.setLimitTypeVoList(limitTypeVoList);
                    }
                }
            }
            catch( Exception e )
            {
                log.error( e.getMessage(), e );
            }
        }
		request.setAttribute("isSubAccountType", isSubAccountType);
        return olaCustomerAccountTypeVo;
    }

    @Override
    protected ModelAndView onCreate( HttpServletRequest request, HttpServletResponse response,
                                     Object command, BindException errors ) throws Exception
    {
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
        baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long( PortalConstants.CUST_ACT_TYPE_CREATE_UPDATE_USECASE_ID ) );

        OlaCustomerAccountTypeVo olaCustomerAccountTypeVo = (OlaCustomerAccountTypeVo) command;
        OlaCustomerAccountTypeModel olaCustomerAccountTypeModel = new OlaCustomerAccountTypeModel();
        olaCustomerAccountTypeModel.setName( olaCustomerAccountTypeVo.getName() );
        olaCustomerAccountTypeModel.setParentAccountTypeId(olaCustomerAccountTypeVo.getParentAccountTypeId());
        olaCustomerAccountTypeModel.setActive( olaCustomerAccountTypeVo.getActive() );
        olaCustomerAccountTypeModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
        olaCustomerAccountTypeModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
        olaCustomerAccountTypeModel.setCreatedOn(new Date());
        olaCustomerAccountTypeModel.setUpdatedOn(new Date());
        //turab
        olaCustomerAccountTypeModel.setIsCustomerAccountType(olaCustomerAccountTypeVo.getIsCategoryCustomerAccountType());
        
        olaCustomerAccountTypeModel.setDormantMarkingEnabled(olaCustomerAccountTypeVo.getDormantMarkingEnabled());
        olaCustomerAccountTypeModel.setDormantTimePeriod(olaCustomerAccountTypeVo.getDormantTimePeriod());

        if(isCustomerAccountTypeAlreadyExists(olaCustomerAccountTypeVo.getName(), olaCustomerAccountTypeVo.getCustomerAccountTypeId(), olaCustomerAccountTypeVo.getIsCategoryCustomerAccountType() )){
        	this.saveMessage(request, "Account Type already exists with this name");
            return super.showForm(request, response, errors);
        }
        
        List<LimitTypeVo> limitTypeVoList =  olaCustomerAccountTypeVo.getLimitTypeVoList();
        if( limitTypeVoList != null && !limitTypeVoList.isEmpty() )
        {
            LimitModel limitModel = null;
            for( LimitTypeVo limitTypeVo : limitTypeVoList )
            {
                if( LimitTypeConstants.MAXIMUM.longValue() != limitTypeVo.getLimitTypeId().longValue() )
                {
                	limitModel = buildLimitModel(limitTypeVo, TransactionTypeConstants.DEBIT, limitTypeVo.getDebitLimit());
                	olaCustomerAccountTypeModel.addCustomerAccountTypeIdLimitModel( limitModel );
                }
                limitModel = buildLimitModel(limitTypeVo, TransactionTypeConstants.CREDIT, limitTypeVo.getCreditLimit());
                olaCustomerAccountTypeModel.addCustomerAccountTypeIdLimitModel( limitModel );
            }
        }

        if( olaCustomerAccountTypeModel.getParentAccountTypeId() != null ) //Make sure that Account sub-type limits do not exceed than parent type 
        {
        	List<LimitModel> parentTypeLimitModelList = limitFacade.getLimitsByCustomerAccountType(olaCustomerAccountTypeModel.getParentAccountTypeId());
        	List<LimitModel> subtypeLimitModelList = (List<LimitModel>) olaCustomerAccountTypeModel.getCustomerAccountTypeIdLimitModelList();
        	boolean areLimitsValid = validateSubtypeLimits(request, parentTypeLimitModelList, subtypeLimitModelList);
        	if( !areLimitsValid )
        	{
        		return super.showForm(request, response, errors);
			}
        }


		// Added by atif hussain
		try {
			StakeholderBankInfoModel stakeholderBankInfoModelCB = new StakeholderBankInfoModel();
			stakeholderBankInfoModelCB.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			stakeholderBankInfoModelCB.setCreatedByAppUserModel(UserUtils.getCurrentUser());
			stakeholderBankInfoModelCB.setUpdatedOn(new Date());
			stakeholderBankInfoModelCB.setCreatedOn(new Date());
			stakeholderBankInfoModelCB.setBankId(CommissionConstantsInterface.BANK_ID);
			stakeholderBankInfoModelCB.setAccountNo(olaCustomerAccountTypeVo.getAccountNo());
			stakeholderBankInfoModelCB.setName(olaCustomerAccountTypeVo.getAccountNick());
			//SETTING NULL ON NIDA REQUEST
			//stakeholderBankInfoModelCB.setCommissionStakeholderId(CommissionConstantsInterface.BANK_STAKE_HOLDER_ID);
			stakeholderBankInfoModelCB.setActive(olaCustomerAccountTypeVo.getActive());
			stakeholderBankInfoModelCB.setCmshaccttypeId(1L);
			stakeholderBankInfoModelCB.setAccountType("OF_SET");
			
			baseWrapper.putObject("STAKE_HOLDER_BANK_INFO_MODEL_CB", stakeholderBankInfoModelCB);
			baseWrapper.setBasePersistableModel(olaCustomerAccountTypeModel);
			baseWrapper = accountFacade.saveOlaCustomerAccountType(baseWrapper);
		}
		catch(Exception ex)
		{	
			ex.printStackTrace();
        	Map<String,Object> modelMap = new HashMap<>(2);
     		modelMap.put("actionId", 3);
    		modelMap.put("customerAccountTypeId", olaCustomerAccountTypeModel.getCustomerAccountTypeId());
			super.saveMessage(request, "Record could not be saved.");
			return new ModelAndView("redirect:p_customeraccounttypeform.html",modelMap);
		}
        this.saveMessage(request, "Account type and limits saved successfully");
        return new ModelAndView( getSuccessView() );
	}

    @Override
    protected ModelAndView onUpdate( HttpServletRequest request, HttpServletResponse response,
                                     Object command, BindException errors ) throws Exception
    {
    	BaseWrapper baseWrapper = new BaseWrapperImpl();
    	baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
    	baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long( PortalConstants.CUST_ACT_TYPE_CREATE_UPDATE_USECASE_ID ) );
    	OlaCustomerAccountTypeVo olaCustomerAccountTypeVo = (OlaCustomerAccountTypeVo) command;
    	if ( request.getParameter("isCustomerAccountType") != null ){
    		boolean isCustomerAccountType = BooleanUtils.toBoolean(request.getParameter("isCustomerAccountType"));
    		olaCustomerAccountTypeVo.setIsCategoryCustomerAccountType( isCustomerAccountType );
    	}
    	Long customerAccountTypeId = olaCustomerAccountTypeVo.getCustomerAccountTypeId();
    	
    	if(isCustomerAccountTypeAlreadyExists(olaCustomerAccountTypeVo.getName(), olaCustomerAccountTypeVo.getCustomerAccountTypeId(), olaCustomerAccountTypeVo.getIsCategoryCustomerAccountType())){
    		this.saveMessage(request, "Account Type already exists with this name");
    	}
    	
    	if(olaCustomerAccountTypeVo.getActive()){
    		if(olaCustomerAccountTypeVo.getParentAccountTypeId() != null && !olaCustomerAccountTypeVo.getIsCategoryCustomerAccountType())
    		{
    			//Handler/Sub Account Type is to be de-activated.
    			OlaCustomerAccountTypeModel model = accountFacade.findAccountTypeById(olaCustomerAccountTypeVo.getParentAccountTypeId());
    			if( !model.getActive() )
    			{
    				this.saveMessage(request, "This Account type can not be activated because its parent account type is inactive.");
    			}
    		}
    	} else {
    		boolean isAccociated = accountFacade.isAssociatedWithAgentCustomerOrHandler(olaCustomerAccountTypeVo.getCustomerAccountTypeId(), olaCustomerAccountTypeVo.getParentAccountTypeId(), olaCustomerAccountTypeVo.getIsCategoryCustomerAccountType());
        	if(isAccociated){
        		 this.saveMessage(request, "This Account type can not be deactivated because it has one or more customers/agents/handlers associated to it.");
        	} else if(olaCustomerAccountTypeVo.getParentAccountTypeId() == null && !olaCustomerAccountTypeVo.getIsCategoryCustomerAccountType()) {
				boolean activeSubtypeExists = accountFacade.hasActiveAccountSubtypes(olaCustomerAccountTypeVo.getCustomerAccountTypeId());
				if( activeSubtypeExists )
				{
					this.saveMessage(request, "This Account type can not be deactivated because it has one or more active sub account types.");
				}
        	}
    	}

    	if(this.hasMessages(request))
    	{
    		Map<String,Object> modelMap = new HashMap<>(2);
    		modelMap.put("actionId", 3);
   		 	modelMap.put("customerAccountTypeId", customerAccountTypeId);
   		 	return new ModelAndView("redirect:p_customeraccounttypeform.html",modelMap);
    	}
        OlaCustomerAccountTypeModel olaCustomerAccountTypeModel = new OlaCustomerAccountTypeModel( customerAccountTypeId );
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setBasePersistableModel( olaCustomerAccountTypeModel );
        
        searchBaseWrapper = accountFacade.searchOlaCustomerAccountTypes( searchBaseWrapper );
        CustomList<OlaCustomerAccountTypeModel> customList = searchBaseWrapper.getCustomList();
        if( customList != null )
        {
        	List<OlaCustomerAccountTypeModel> olaCustomerAccountTypeModelList = customList.getResultsetList();
//            olaCustomerAccountTypeVo = new OlaCustomerAccountTypeVo();
            List<LimitModel> limitsList = null;
            for(OlaCustomerAccountTypeModel model : olaCustomerAccountTypeModelList){
            
            	olaCustomerAccountTypeModel = model;
            	limitsList = limitFacade.getLimitsByCustomerAccountType(model.getCustomerAccountTypeId());
            }
            
            Map<Long, LimitModel> limitMap = new HashMap<Long, LimitModel>();
            for(LimitModel limitModel : limitsList){
            	limitMap.put(limitModel.getLimitId(), limitModel); 
            }
        	
            olaCustomerAccountTypeModel.setCustomerAccountTypeId(olaCustomerAccountTypeVo.getCustomerAccountTypeId());
        	olaCustomerAccountTypeModel.setName( olaCustomerAccountTypeVo.getName() );
            olaCustomerAccountTypeModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
            olaCustomerAccountTypeModel.setUpdatedOn(new Date());
            olaCustomerAccountTypeModel.setActive( olaCustomerAccountTypeVo.getActive() );
            olaCustomerAccountTypeVo.setParentAccountTypeId(olaCustomerAccountTypeModel.getParentAccountTypeId());//Disabled fields are not populated in VO by default
            
            olaCustomerAccountTypeModel.setDormantMarkingEnabled(olaCustomerAccountTypeVo.getDormantMarkingEnabled());
            olaCustomerAccountTypeModel.setDormantTimePeriod(olaCustomerAccountTypeVo.getDormantTimePeriod());
            
            List<LimitTypeVo> limitTypeVoList =  olaCustomerAccountTypeVo.getLimitTypeVoList();
            if( limitTypeVoList != null && !limitTypeVoList.isEmpty() )
            {
                for( LimitTypeVo limitTypeVo : limitTypeVoList )
                {
                    if( LimitTypeConstants.MAXIMUM.longValue() != limitTypeVo.getLimitTypeId().longValue() )
                    {
                    	updateLimitModel(limitMap.get(limitTypeVo.getDebitLimitId()), limitTypeVo, TransactionTypeConstants.DEBIT, limitTypeVo.getDebitLimit());
                    }
                    updateLimitModel(limitMap.get(limitTypeVo.getCreditLimitId()), limitTypeVo, TransactionTypeConstants.CREDIT, limitTypeVo.getCreditLimit());
                }

            boolean areLimitsValid = false;

            if( olaCustomerAccountTypeModel.getParentAccountTypeId() != null ) //Make sure that Account sub-type limits do not exceed than parent type 
            {
            	List<LimitModel> parentTypeLimitModelList = limitFacade.getLimitsByCustomerAccountType(olaCustomerAccountTypeModel.getParentAccountTypeId());
            	areLimitsValid = validateSubtypeLimits(request, parentTypeLimitModelList, limitsList);
                
                if( !areLimitsValid )
            	{
                	Boolean isSubAccountType = Boolean.FALSE;
                	if(olaCustomerAccountTypeModel.getParentAccountTypeId() != null)
                	{
                		isSubAccountType = Boolean.TRUE;
                	} 
                	request.setAttribute("isSubAccountType", isSubAccountType);
            		return super.showForm(request, response, errors);
    			}
            }
            
            if(!olaCustomerAccountTypeVo.getIsCategoryCustomerAccountType() && olaCustomerAccountTypeVo.getParentAccountTypeId() == null)
            {
            	//Make sure that parent account type's limits do not exceed than Sub Account type's limits  
            	List<OlaCustomerAccountTypeModel> subtypeModelList = accountFacade.searchSubtypesAndLimits(olaCustomerAccountTypeVo.getCustomerAccountTypeId());
            	areLimitsValid = validateParentTypeLimits(request, limitsList, subtypeModelList);
                
                if( !areLimitsValid )
            	{
                	Boolean isSubAccountType = Boolean.FALSE;
                	if(olaCustomerAccountTypeModel.getParentAccountTypeId() != null)
                	{
                		isSubAccountType = Boolean.TRUE;
                	} 
                	request.setAttribute("isSubAccountType", isSubAccountType);
            		return super.showForm(request, response, errors);
    			}
            }

            searchBaseWrapper =new SearchBaseWrapperImpl();
			StakehldBankInfoListViewModel stakehldBankInfoListViewModel =new StakehldBankInfoListViewModel();
			stakehldBankInfoListViewModel.setCustomerAccountTypeId(olaCustomerAccountTypeVo.getCustomerAccountTypeId());
			stakehldBankInfoListViewModel.setBankId(CommissionConstantsInterface.BANK_ID.longValue());
			
			searchBaseWrapper.setBasePersistableModel(stakehldBankInfoListViewModel);
			searchBaseWrapper = this.stakeholderBankInfoManager.searchStakeHolderBankInfo(searchBaseWrapper);
			
			CustomList<StakehldBankInfoListViewModel> stakehldBankInfoListViewModelCustomList	=	(CustomList<StakehldBankInfoListViewModel>) searchBaseWrapper.getCustomList();

			
			StakeholderBankInfoModel stakeholderBankInfoModelCB =null;

			if(null != stakehldBankInfoListViewModelCustomList && stakehldBankInfoListViewModelCustomList.getResultsetList().size()>0)
			{
				baseWrapper = new BaseWrapperImpl();
				stakehldBankInfoListViewModel	=	stakehldBankInfoListViewModelCustomList.getResultsetList().get(0);
				
				Long pk	=stakehldBankInfoListViewModel.getStakeholderBankInfoId();
				stakeholderBankInfoModelCB=new StakeholderBankInfoModel();
				stakeholderBankInfoModelCB.setPrimaryKey(pk);
				
				searchBaseWrapper =new SearchBaseWrapperImpl();
				searchBaseWrapper.setBasePersistableModel(stakeholderBankInfoModelCB);
				searchBaseWrapper=stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper);
				stakeholderBankInfoModelCB =	(StakeholderBankInfoModel)searchBaseWrapper.getBasePersistableModel();
				stakeholderBankInfoModelCB.setAccountNo(olaCustomerAccountTypeVo.getAccountNo());
				stakeholderBankInfoModelCB.setName(olaCustomerAccountTypeVo.getAccountNick());
				stakeholderBankInfoModelCB.setActive(olaCustomerAccountTypeVo.getActive());
				stakeholderBankInfoModelCB.setAccountType("OF_SET");
				
				baseWrapper.putObject("STAKE_HOLDER_BANK_INFO_MODEL_CB", stakeholderBankInfoModelCB);
			}
			else
			{
				stakeholderBankInfoModelCB = new StakeholderBankInfoModel();

				stakeholderBankInfoModelCB.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
				stakeholderBankInfoModelCB.setCreatedByAppUserModel(UserUtils.getCurrentUser());
				stakeholderBankInfoModelCB.setUpdatedOn(new Date());
				stakeholderBankInfoModelCB.setCreatedOn(new Date());
				stakeholderBankInfoModelCB.setBankId(CommissionConstantsInterface.BANK_ID);
				stakeholderBankInfoModelCB.setAccountNo(olaCustomerAccountTypeVo.getAccountNo());
				stakeholderBankInfoModelCB.setName(olaCustomerAccountTypeVo.getAccountNick());
				//commenting on nida request
				//stakeholderBankInfoModelCB.setCommissionStakeholderId(CommissionConstantsInterface.BANK_STAKE_HOLDER_ID);
				stakeholderBankInfoModelCB.setActive(olaCustomerAccountTypeVo.getActive());
				stakeholderBankInfoModelCB.setCmshaccttypeId(1L);
				stakeholderBankInfoModelCB.setAccountType("OF_SET");

				baseWrapper.putObject("STAKE_HOLDER_BANK_INFO_MODEL_CB", stakeholderBankInfoModelCB);
				baseWrapper.setBasePersistableModel(olaCustomerAccountTypeModel);
			}
            
        	baseWrapper.setBasePersistableModel( olaCustomerAccountTypeModel );
        	try {
        		baseWrapper = accountFacade.saveOlaCustomerAccountType( baseWrapper );
        		
        		limitFacade.updateLimitsList(limitsList);
        		this.saveMessage(request, "Account type and limits updated successfully");
			} 
			catch(FrameworkCheckedException ex){
				ex.printStackTrace();
	        	Map<String,Object> modelMap = new HashMap<>(2);
         		modelMap.put("actionId", 3);
        		modelMap.put("customerAccountTypeId", customerAccountTypeId);
    			return new ModelAndView("redirect:p_customeraccounttypeform.html",modelMap);
			}
        }
        }
        return new ModelAndView( getSuccessView());
    }
    
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
    	super.initBinder(request, binder);
        NumberFormat numFormat = new DecimalFormat("##");
        binder.registerCustomEditor(Double.class, new CustomNumberEditor(Double.class, numFormat, true));
    }

    private boolean validateParentTypeLimits(HttpServletRequest request, List<LimitModel> parentTypeLimitModelList, List<OlaCustomerAccountTypeModel> subtypeModelList)
    {
		boolean isValid = true;
		if( CollectionUtils.isNotEmpty(parentTypeLimitModelList) && CollectionUtils.isNotEmpty(subtypeModelList) )
    	{
			StringBuilder msgBuilder = new StringBuilder(200);
			for(OlaCustomerAccountTypeModel olaCustomerAccountTypeModel : subtypeModelList)
			{
				List<LimitModel> subtypeLimitModelList = (List<LimitModel>) olaCustomerAccountTypeModel.getCustomerAccountTypeIdLimitModelList();
				for(LimitModel parentLimitModel : parentTypeLimitModelList)
	    		{
	    			for(LimitModel limitModel : subtypeLimitModelList)
	        		{
	        			if(parentLimitModel.getLimitTypeId().longValue() == limitModel.getLimitTypeId()
	        					&& parentLimitModel.getTransactionTypeId().longValue() == limitModel.getTransactionTypeId() )
	        			{
	        				if( limitModel.getMaximum() != null && parentLimitModel.getMaximum() != null
	        						&& parentLimitModel.getMaximum().doubleValue() < limitModel.getMaximum() )
	        				{
	        					String limitAndTxTypeName = parentLimitModel.getLimitTypeIdLimitTypeModel().getName() + " " + parentLimitModel.getTransactionTypeIdTransactionTypeModel().getName() + " Limit";
	        					msgBuilder.append(limitAndTxTypeName)
	        					//.append('(').append(limitModel.getMaximum()).append(')')
	        					.append(" cannot be less than Sub Account Type")
	        					.append('(').append(olaCustomerAccountTypeModel.getName()).append(") ")
	        					.append(limitAndTxTypeName);
	        					//.append('(').append(parentLimitModel.getMaximum()).append(')');
								this.saveMessage(request, msgBuilder.toString());
								isValid = false;
								msgBuilder.setLength(0);
								break;
							}
						}
					}
				}
			}
    	}
		return isValid;
	}

    private boolean validateSubtypeLimits(HttpServletRequest request, List<LimitModel> parentTypeLimitModelList, List<LimitModel> subtypeLimitModelList )
    {
		boolean isValid = true;
		if( CollectionUtils.isNotEmpty(parentTypeLimitModelList) && CollectionUtils.isNotEmpty(subtypeLimitModelList) )
    	{
			StringBuilder msgBuilder = new StringBuilder(200);
    		for(LimitModel parentLimitModel : parentTypeLimitModelList)
    		{
    			for(LimitModel limitModel : subtypeLimitModelList)
        		{
        			if(parentLimitModel.getLimitTypeId().longValue() == limitModel.getLimitTypeId()
        					&& parentLimitModel.getTransactionTypeId().longValue() == limitModel.getTransactionTypeId() )
        			{
        				if( limitModel.getMaximum() != null && parentLimitModel.getMaximum() != null
        						&& limitModel.getMaximum().doubleValue() > parentLimitModel.getMaximum() )
        				{
        					String limitAndTxTypeName = parentLimitModel.getLimitTypeIdLimitTypeModel().getName() + " " + parentLimitModel.getTransactionTypeIdTransactionTypeModel().getName() + " Limit";
        					msgBuilder.append(limitAndTxTypeName)
        					//.append('(').append(limitModel.getMaximum()).append(')')
        					.append(" cannot be greater than Parent Account Type's ")
        					.append(limitAndTxTypeName);
        					//.append('(').append(parentLimitModel.getMaximum()).append(')');
							this.saveMessage(request, msgBuilder.toString());
							isValid = false;
							msgBuilder.setLength(0);
							break;
						}
					}
				}
			}
    	}
		return isValid;
	}

    private List<LimitTypeVo> loadLimitTypeModelList(Long customerAccountTypeId) throws FrameworkCheckedException
    {
    	boolean isWalkinCustAcType = customerAccountTypeId != null && UserTypeConstantsInterface.WALKIN_CUSTOMER.longValue() == customerAccountTypeId.longValue();
        List<LimitTypeModel> limitTypeModelList = null;
        List<LimitTypeVo> limitTypeVoList = null;

        LimitTypeModel limitTypeModel = new LimitTypeModel();
		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl( limitTypeModel, "limitTypeId", SortingOrder.ASC );
		commonFacade.getReferenceData( referenceDataWrapper );
		limitTypeModelList = referenceDataWrapper.getReferenceDataList();

		if (isWalkinCustAcType)
		{
			limitTypeModelList = retainLimitTypeModel( LimitTypeConstants.THROUGHPUT, LimitTypeConstants.BVS_THROUGHPUT, limitTypeModelList );
		}
		else
    	{
    		limitTypeModelList = removeLimitTypeModel( LimitTypeConstants.THROUGHPUT, limitTypeModelList );
    		limitTypeModelList = removeLimitTypeModel( LimitTypeConstants.BVS_THROUGHPUT, limitTypeModelList );
		}
        limitTypeVoList = buildLimitTypeVoList( limitTypeModelList );
        return limitTypeVoList;
    }

    /**
     * @param limitTypeIdToRemove Removes all limit types except specified limitTypeId 
     * @param limitTypeModelList The limitTypeModelList from which limitTypeId should be retained 
     * @return updated limitTypeModelList
     */
    private List<LimitTypeModel> retainLimitTypeModel(Long limitTypeIdToRetain1, Long limitTypeIdToRetain2, List<LimitTypeModel> limitTypeModelList)
    {
        Iterator<LimitTypeModel> itrLimitTypeModelList = limitTypeModelList.iterator();
        while( itrLimitTypeModelList.hasNext() )
        {
            LimitTypeModel limitTypeModel = itrLimitTypeModelList.next();
            if ( limitTypeIdToRetain1.longValue() != limitTypeModel.getLimitTypeId().longValue() && 
            		 limitTypeIdToRetain2.longValue() != limitTypeModel.getLimitTypeId().longValue() )
            {
                itrLimitTypeModelList.remove();
            }
        }
        return limitTypeModelList;
    }

    /**
     * @param limitTypeIdToRemove Removes specified limitTypeId 
     * @param limitTypeModelList The limitTypeModelList from which limitTypeId should be removed 
     * @return updated limitTypeModelList
     */
    private List<LimitTypeModel> removeLimitTypeModel(Long limitTypeIdToRemove, List<LimitTypeModel> limitTypeModelList)
    {
        Iterator<LimitTypeModel> itrLimitTypeModelList = limitTypeModelList.iterator();
        while( itrLimitTypeModelList.hasNext() )
        {
            LimitTypeModel limitTypeModel = itrLimitTypeModelList.next();
            if ( limitTypeIdToRemove.longValue() == limitTypeModel.getLimitTypeId().longValue() )
            {
                itrLimitTypeModelList.remove();
                break;
            }
        }
        return limitTypeModelList;
    }

    private List<LimitTypeVo> buildLimitTypeVoList(List<LimitTypeModel> limitTypeModelList)
    {
        List<LimitTypeVo> limitTypeVoList = new ArrayList<>( limitTypeModelList.size() );
        for( LimitTypeModel limitTypeModel : limitTypeModelList )
        {
            LimitTypeVo limitTypeVo = new LimitTypeVo(limitTypeModel.getLimitTypeId(), limitTypeModel.getName(), true ); 
            limitTypeVoList.add( limitTypeVo );
        }
        return limitTypeVoList;
    }

    private LimitModel buildLimitModel(LimitTypeVo limitTypeVo, Long transactionType, Double limitAmount )
    {
        LimitModel limitModel = new LimitModel( PortalConstants.MIN_DEBIT_CREDIT_LIMIT, limitAmount );
        limitModel.setLimitTypeId( limitTypeVo.getLimitTypeId() );
        limitModel.setIsApplicable(limitTypeVo.getApplicable());
        limitModel.setTransactionTypeId( transactionType );
        limitModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
        limitModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
        limitModel.setCreatedOn(new Date());
        limitModel.setUpdatedOn(new Date());
        
        return limitModel;
    }
    
    private void updateLimitModel(LimitModel limitModel, LimitTypeVo limitTypeVo, Long transactionType, Double limitAmount )
    {
    	if ( limitModel != null ){ //null check added by Turab
	    	limitModel.setMaximum(limitAmount);
	        limitModel.setIsApplicable(limitTypeVo.getApplicable());
	        //limitModel.setTransactionTypeId( transactionType );
	        limitModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
	        limitModel.setUpdatedOn(new Date());
    	}
        
    }
    
    private boolean isCustomerAccountTypeAlreadyExists(String name, Long customerAccountTypeId, boolean isCustomerAccountType) throws FrameworkCheckedException{
    	
    	OlaCustomerAccountTypeModel olaCustomerAccountTypeModel = new OlaCustomerAccountTypeModel();
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setBasePersistableModel( olaCustomerAccountTypeModel );
        searchBaseWrapper = accountFacade.searchOlaCustomerAccountTypes( searchBaseWrapper );
        CustomList<OlaCustomerAccountTypeModel> customList = searchBaseWrapper.getCustomList();
        if( customList != null ) {
            List<OlaCustomerAccountTypeModel> olaCustomerAccountTypeModelList = customList.getResultsetList();
            for(OlaCustomerAccountTypeModel model : olaCustomerAccountTypeModelList){
            	if ( customerAccountTypeId == null ){
            		if(model.getName().equalsIgnoreCase(name) && model.getIsCustomerAccountType().equals(isCustomerAccountType) ){
                		return true;
            		}
            	}
            	if(model.getName().equalsIgnoreCase(name) && (customerAccountTypeId != null && customerAccountTypeId.longValue() != model.getCustomerAccountTypeId().longValue()) && model.getIsCustomerAccountType().equals(isCustomerAccountType)){
            		return true;
            	}
            }
            
        }
    	
    	return false;
    }

    public void setCommonFacade( CommonFacade commonFacade )
    {
        this.commonFacade = commonFacade;
    }

    public void setAccountFacade( AccountFacade accountFacade )
    {
        this.accountFacade = accountFacade;
    }

	public void setLimitFacade(LimitFacade limitFacade) {
		this.limitFacade = limitFacade;
	}

	public void setStakeholderBankInfoManager(
			StakeholderBankInfoManager stakeholderBankInfoManager) {
		this.stakeholderBankInfoManager = stakeholderBankInfoManager;
	}
	
	public void setPhoenixFinancialInstitution(
			AbstractFinancialInstitution phoenixFinancialInstitution) {
		this.phoenixFinancialInstitution = phoenixFinancialInstitution;
	}
}
