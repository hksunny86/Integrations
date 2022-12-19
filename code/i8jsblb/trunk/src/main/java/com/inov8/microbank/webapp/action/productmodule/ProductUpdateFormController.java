package com.inov8.microbank.webapp.action.productmodule;
    
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.ExceptionErrorCodes;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.integration.common.model.AccountHolderModel;
import com.inov8.integration.common.model.AccountModel;
import com.inov8.microbank.common.model.CommissionRateDefaultModel;
import com.inov8.microbank.common.model.CommissionShSharesDefaultModel;
import com.inov8.microbank.common.model.CommissionStakeholderModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.ServiceModel;
import com.inov8.microbank.common.model.StakeholderBankInfoModel;
import com.inov8.microbank.common.model.SupplierModel;
import com.inov8.microbank.tax.model.WHTConfigModel;
import com.inov8.microbank.common.model.commissionmodule.CommStakeholderListViewModel;
import com.inov8.microbank.common.model.portal.authorizationreferencedata.CommissionShSharesDefaultModelVO;
import com.inov8.microbank.common.model.portal.authorizationreferencedata.ProductModelVO;
import com.inov8.microbank.common.model.stakeholdermodule.StakehldBankInfoListViewModel;
import com.inov8.microbank.common.util.ActionAuthorizationConstantsInterface;
import com.inov8.microbank.common.util.BankConstantsInterface;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.ServiceTypeConstantsInterface;
import com.inov8.microbank.common.util.StakeholderAccountTypeConstants;
import com.inov8.microbank.common.util.SupplierConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.productmodule.ProductCatalogManager;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;
import com.inov8.ola.server.service.account.AccountManager;
import com.inov8.ola.util.AccountNumberGenerator;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import com.inov8.ola.util.EncryptionUtil;
import com.inov8.ola.util.StatusConstants;


public class ProductUpdateFormController
    extends AdvanceFormController
{
  private ProductManager productManager;
  private ReferenceDataManager referenceDataManager;
  private ProductCatalogManager catalogManager;
  @Autowired
  private AccountManager	accountManager;	
  private StakeholderBankInfoManager stakeholderBankInfoManager;
  private Long id;

  public ProductUpdateFormController()
  {
    setCommandName("productModel");
    setCommandClass(ProductModel.class);
  }

  @SuppressWarnings("unchecked")
@Override
  protected Map<String,Object> loadReferenceData(HttpServletRequest request) throws Exception {
	  Map<String, Object> referenceDataMap = new HashMap<String, Object>(2);
	  List<ServiceModel> serviceModelList = null;
	  List<ServiceModel> allServiceList = null;
	  List<SupplierModel> supplierModelList = null;
	  List<WHTConfigModel> wHTConfigModelList = null;
	  List<CommissionStakeholderModel> commStakeholderListViewModelList = null;
	  List<CommissionStakeholderModel> commissionStakeholderModelCorporateTypeList = null;
	  List<CommissionStakeholderModel> commissionStakeholderModelCommissionTypeList = null;
	  List<CommissionStakeholderModel> commissionStakeholderModelFiltered = null;
  	 /**
       * code fragment to load reference data for SupplierModel
       */
      SupplierModel supplierModel = new SupplierModel();
      supplierModel.setActive(true);
      ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
          supplierModel, "name", SortingOrder.ASC);
      
      try
      {
         //referenceDataManager.getReferenceData(referenceDataWrapper);
        
        Long productId = ServletRequestUtils.getLongParameter(request, "productId");
      	if(null == productId){
      		referenceDataManager.getReferenceData(referenceDataWrapper, SupplierConstants.BRANCHLESS_BANKING_SUPPLIER);
      	}else{
      		referenceDataManager.getReferenceData(referenceDataWrapper);
      	}
        
        if (referenceDataWrapper.getReferenceDataList() != null)
        {
        	supplierModelList =(List<SupplierModel>) referenceDataWrapper.getReferenceDataList();
        	
        	referenceDataWrapper = new ReferenceDataWrapperImpl(new CommissionStakeholderModel(), "name", SortingOrder.ASC);
        	referenceDataManager.getReferenceData(referenceDataWrapper);
        	commStakeholderListViewModelList = referenceDataWrapper.getReferenceDataList();
        	commStakeholderListViewModelList = this.filterStakeholderModelList(commStakeholderListViewModelList);
        
        
        
	        /**
	         * code fragment to load reference data for ServiceList
	         */
	        ServiceModel serviceModel = new ServiceModel();
	        serviceModel.setActive(true);
	        serviceModel.setServiceTypeId(ServiceTypeConstantsInterface.SERVICE_TYPE_BULK_PAYMENTS);
	        referenceDataWrapper = new ReferenceDataWrapperImpl(serviceModel, "name", SortingOrder.ASC);
	        referenceDataManager.getReferenceData(referenceDataWrapper);
	        serviceModelList = referenceDataWrapper.getReferenceDataList();
	        
	        
	        
	        serviceModel = new ServiceModel();
	        serviceModel.setActive(true);
	        referenceDataWrapper = new ReferenceDataWrapperImpl(serviceModel, "name", SortingOrder.ASC);
	        referenceDataManager.getReferenceData(referenceDataWrapper);
	        allServiceList = referenceDataWrapper.getReferenceDataList();
	        /**
	         * code fragment to load reference data for CorporateTypeList
	         */
	        
	        CommissionStakeholderModel commissionStakeholderModelCorporateType=new CommissionStakeholderModel();
			commissionStakeholderModelCorporateType.setCmshaccttypeId(StakeholderAccountTypeConstants.CORPORATE_CLIENTS);
	    	referenceDataWrapper = new ReferenceDataWrapperImpl(commissionStakeholderModelCorporateType, "name", SortingOrder.ASC);
	    	referenceDataManager.getReferenceData(referenceDataWrapper);
			commissionStakeholderModelCorporateTypeList = referenceDataWrapper.getReferenceDataList();
			
			
			CommissionStakeholderModel commissionStakeholderModelCommissionType=new CommissionStakeholderModel();
			commissionStakeholderModelCommissionType.setCmshaccttypeId(StakeholderAccountTypeConstants.COMMISSION_ACCOUNT);
			referenceDataWrapper = new ReferenceDataWrapperImpl(commissionStakeholderModelCommissionType, "name", SortingOrder.ASC);
			referenceDataManager.getReferenceData(referenceDataWrapper);
			commissionStakeholderModelCommissionTypeList = referenceDataWrapper.getReferenceDataList();
	    	commissionStakeholderModelFiltered = this.filterStakeholderModelList(commissionStakeholderModelCommissionTypeList);
	    	
	    	 WHTConfigModel wHTConfigModel = new WHTConfigModel();
	         wHTConfigModel.setActive(true);
	         referenceDataWrapper = new ReferenceDataWrapperImpl(wHTConfigModel, "title", SortingOrder.ASC);
	         referenceDataManager.getReferenceData(referenceDataWrapper);
	         wHTConfigModelList = referenceDataWrapper.getReferenceDataList();
	         wHTConfigModelList = getWHTConfigFilteredList(wHTConfigModelList);
	       
        }
        
        
      }
      catch (FrameworkCheckedException ex)
      {
    	  logger.error(ex.getMessage(), ex);
      }

      referenceDataMap.put("SupplierModelList", supplierModelList);
      referenceDataMap.put("allServiceList", allServiceList);
      referenceDataMap.put("serviceModelList", serviceModelList);
      referenceDataMap.put("commissionStakeholderModelCorporateTypeList", commissionStakeholderModelCorporateTypeList);
      referenceDataMap.put("commStakeholderListViewModelList", commStakeholderListViewModelList);
      referenceDataMap.put("wHTConfigModelList", wHTConfigModelList);
      
      //referenceDataMap.put("commStakeholderListViewModelList", commissionStakeholderModelFiltered);
  	return referenceDataMap;
  }
  
  @SuppressWarnings("unchecked")
@Override
  protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws
      Exception
  {
    id = ServletRequestUtils.getLongParameter(httpServletRequest, "productId");
    if (null != id)
    {
      if (log.isDebugEnabled())
      {
        log.debug("id is not null....retrieving object from DB");
      }
      List<CommStakeholderListViewModel> commStakeholderListViewModelList = null;
      SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
      ProductModel productModel = new ProductModel();
      productModel.setPrimaryKey(id);
      searchBaseWrapper.setBasePersistableModel(productModel);
      searchBaseWrapper = this.productManager.loadProduct(searchBaseWrapper);
      productModel=(ProductModel) searchBaseWrapper.getBasePersistableModel();
      
      CommissionRateDefaultModel commissionRateDefaultModel = new CommissionRateDefaultModel();
      commissionRateDefaultModel.setProductId(productModel.getProductId());

      ReferenceDataWrapper commissionRateDefaultDataWrapper = new ReferenceDataWrapperImpl(commissionRateDefaultModel);
      commissionRateDefaultDataWrapper.setBasePersistableModel(commissionRateDefaultModel);
	    try
	    {
	      referenceDataManager.getReferenceData(commissionRateDefaultDataWrapper);
	      if ( commissionRateDefaultDataWrapper.getReferenceDataList() != null && CollectionUtils.isNotEmpty(commissionRateDefaultDataWrapper.getReferenceDataList()) ){
	    	  productModel.setProductIdCommissionRateDefaultModelList( commissionRateDefaultDataWrapper.getReferenceDataList() );
	    	  commissionRateDefaultModel = (CommissionRateDefaultModel) commissionRateDefaultDataWrapper.getReferenceDataList().get(0);
	    	  productModel.setInclusiveFixAmount(commissionRateDefaultModel.getInclusiveFixAmount());
	    	  productModel.setInclusivePercentAmount(commissionRateDefaultModel.getInclusivePercentAmount());
	    	  productModel.setExclusiveFixAmount(commissionRateDefaultModel.getExclusiveFixAmount());
	    	  productModel.setExclusivePercentAmount(commissionRateDefaultModel.getExclusivePercentAmount());
	    	  productModel.setCommissionRateDefaultModelId(commissionRateDefaultModel.getCommissionRateDefaultId());
	      }
	    }
	    catch (Exception e)
	    {
	    	logger.error(e.getMessage(), e);
	    }
 
	    /*ADDED BY ATIF HUSSAIN*/
		searchBaseWrapper =new SearchBaseWrapperImpl();
		StakehldBankInfoListViewModel stakehldBankInfoListViewModel =new StakehldBankInfoListViewModel();
		stakehldBankInfoListViewModel.setProductId(productModel.getProductId());
		searchBaseWrapper.setBasePersistableModel(stakehldBankInfoListViewModel);
		
		CustomList<StakehldBankInfoListViewModel> customList= this.stakeholderBankInfoManager.searchStakeHolderBankInfo(searchBaseWrapper).getCustomList();
		if(null != customList && customList.getResultsetList()!=null && customList.getResultsetList().size()>0)
		{
			stakehldBankInfoListViewModel	=	(StakehldBankInfoListViewModel) customList.getResultsetList().get(0);
			productModel.setAccountNick(stakehldBankInfoListViewModel.getName());
			productModel.setAccountNo(stakehldBankInfoListViewModel.getAccountNo());
		}
		/*end of addition by atif*/
	    
	    CommissionShSharesDefaultModel commissionShShareDefaultModel = new CommissionShSharesDefaultModel( );
	    commissionShShareDefaultModel.setProductId(productModel.getProductId());
	    ReferenceDataWrapper commissionShSharesDefaultDataWrapper = new ReferenceDataWrapperImpl(commissionShShareDefaultModel);
	    commissionRateDefaultDataWrapper.setBasePersistableModel(commissionShShareDefaultModel);
		    try
		    {
		      referenceDataManager.getReferenceData(commissionShSharesDefaultDataWrapper);
		      if ( commissionShSharesDefaultDataWrapper.getReferenceDataList() != null && CollectionUtils.isNotEmpty(commissionShSharesDefaultDataWrapper.getReferenceDataList()) ){
		    	  List<CommissionShSharesDefaultModel> commissionShSharesDefaultModelList = commissionShSharesDefaultDataWrapper.getReferenceDataList();
		    	  commissionShShareDefaultModel = (CommissionShSharesDefaultModel) commissionShSharesDefaultDataWrapper.getReferenceDataList().get(0);
		    	  productModel.setStakeHolderId(commissionShShareDefaultModel.getCommissionStakeholderIdCommissionStakeholderModel().getCommissionStakeholderId());
		    	  ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(new CommStakeholderListViewModel(), "commissionStakeholderName", SortingOrder.ASC);
		          referenceDataManager.getReferenceData(referenceDataWrapper);

		          commStakeholderListViewModelList = referenceDataWrapper.getReferenceDataList();
		          commStakeholderListViewModelList = this.filterStakeholderList(commStakeholderListViewModelList, productModel);
		          List<CommissionShSharesDefaultModel> destCommissionShSharesDefaultModelList = new ArrayList<CommissionShSharesDefaultModel>(commStakeholderListViewModelList.size());
		          for(CommStakeholderListViewModel commStakeholderListViewModel : commStakeholderListViewModelList)
		          {
		        	  CommissionShSharesDefaultModel destCommissionShSharesDefaultModel = new CommissionShSharesDefaultModel();
		        	  for(CommissionShSharesDefaultModel commissionShSharesDefaultModel : commissionShSharesDefaultModelList)
		        	  {
		        		  if(commStakeholderListViewModel.getCommissionStakeholderId().longValue() == commissionShSharesDefaultModel.getCommissionStakeholderId().longValue())
		        		  {
		        			  destCommissionShSharesDefaultModel.setCommissionShSharesDefaultId(commissionShSharesDefaultModel.getCommissionShSharesDefaultId());
		        			  destCommissionShSharesDefaultModel.setCommissionStakeholderId(commissionShSharesDefaultModel.getCommissionStakeholderId());
		        			  destCommissionShSharesDefaultModel.setCommissionShare(commissionShSharesDefaultModel.getCommissionShare());
		        			  destCommissionShSharesDefaultModel.setIsWhtApplicable(commissionShSharesDefaultModel.getIsWhtApplicable() == null ? false : commissionShSharesDefaultModel.getIsWhtApplicable());
		        			  destCommissionShSharesDefaultModel.setIsFedApplicable(commissionShSharesDefaultModel.getIsFedApplicable() == null ? false :commissionShSharesDefaultModel.getIsFedApplicable());
		        			  destCommissionShSharesDefaultModel.setProductId(commissionShSharesDefaultModel.getProductId());
		        			  if ( commissionShSharesDefaultModel.getIsFedApplicable() != null && commissionShSharesDefaultModel.getIsFedApplicable() ){
		        				  productModel.setIsFed(true);
		        				  
		        			  }
		        			  if ( commissionShSharesDefaultModel.getIsWhtApplicable() != null && commissionShSharesDefaultModel.getIsWhtApplicable() ){
		        				  productModel.setIsWithHolding(true);
		        				  
		        			  }
		        			  if(commissionShSharesDefaultModel.getCommissionStakeholderId().longValue() == CommissionConstantsInterface.FED_STAKE_HOLDER_ID){//fed
		        				  productModel.setFedShare(commissionShSharesDefaultModel.getCommissionShare());
		        			  }
		        			  if(commissionShSharesDefaultModel.getCommissionStakeholderId().longValue() == CommissionConstantsInterface.WHT_STAKE_HOLDER_ID){//w.h
		        				  productModel.setWithHoldingShare(commissionShSharesDefaultModel.getCommissionShare());
		        			  }
		        			  //break;
						  }else
						  {
							 destCommissionShSharesDefaultModel.setProductId(productModel.getProductId());
							 destCommissionShSharesDefaultModel.setCommissionStakeholderId(commStakeholderListViewModel.getCommissionStakeholderId());
						  }
//		        		  destCommissionShSharesDefaultModelList.add(destCommissionShSharesDefaultModel);
					  }
		        	  destCommissionShSharesDefaultModelList.add(destCommissionShSharesDefaultModel);
				  }
		          //now fetch the FED
		          commissionShShareDefaultModel = new CommissionShSharesDefaultModel();
		          commissionShShareDefaultModel.setCommissionStakeholderId(CommissionConstantsInterface.FED_STAKE_HOLDER_ID);//fed
		          commissionShShareDefaultModel.setProductId(productModel.getProductId());
		          referenceDataWrapper = new ReferenceDataWrapperImpl();
		          referenceDataWrapper.setBasePersistableModel(commissionShShareDefaultModel);
		          referenceDataManager.getReferenceData(referenceDataWrapper);
		          if (referenceDataWrapper.getReferenceDataList() != null && CollectionUtils.isNotEmpty(referenceDataWrapper.getReferenceDataList())){
		        	  commissionShShareDefaultModel =(CommissionShSharesDefaultModel) referenceDataWrapper.getReferenceDataList().get(0);
		        	  productModel.setFedShare(commissionShShareDefaultModel.getCommissionShare());
		          }
		          //now w.h
		          commissionShShareDefaultModel = new CommissionShSharesDefaultModel();
		          commissionShShareDefaultModel.setCommissionStakeholderId(CommissionConstantsInterface.WHT_STAKE_HOLDER_ID);//fed
		          commissionShShareDefaultModel.setProductId(productModel.getProductId());
		          referenceDataWrapper = new ReferenceDataWrapperImpl();
		          referenceDataWrapper.setBasePersistableModel(commissionShShareDefaultModel);
		          referenceDataManager.getReferenceData(referenceDataWrapper);
		          if (referenceDataWrapper.getReferenceDataList() != null && CollectionUtils.isNotEmpty(referenceDataWrapper.getReferenceDataList())){
		        	  commissionShShareDefaultModel =(CommissionShSharesDefaultModel) referenceDataWrapper.getReferenceDataList().get(0);
		        	  productModel.setWithHoldingShare(commissionShShareDefaultModel.getCommissionShare());
		          }
		          productModel.setProductIdCommissionShSharesDefaultModelList(destCommissionShSharesDefaultModelList);
		      }else{
		    	  List <CommissionShSharesDefaultModel> list = new ArrayList<CommissionShSharesDefaultModel>(0);
		    	  productModel.setProductIdCommissionShSharesDefaultModelList(list);
		    	  ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(new CommStakeholderListViewModel(), "commissionStakeholderName", SortingOrder.ASC);
		          referenceDataManager.getReferenceData(referenceDataWrapper);
		    	  commStakeholderListViewModelList = referenceDataWrapper.getReferenceDataList();
		    	  commStakeholderListViewModelList = this.filterStakeholderList(commStakeholderListViewModelList, productModel);
		          List<CommissionShSharesDefaultModel> destCommissionShSharesDefaultModelList = new ArrayList<>(commStakeholderListViewModelList.size());
		          for(CommStakeholderListViewModel commStakeholderListViewModel : commStakeholderListViewModelList)
		          {
		        	  CommissionShSharesDefaultModel destCommissionShSharesDefaultModel = new CommissionShSharesDefaultModel();
		        	  
		        		  
		        			  
		        			  destCommissionShSharesDefaultModel.setCommissionStakeholderId(commStakeholderListViewModel.getCommissionStakeholderId());
		        			  
		        			  destCommissionShSharesDefaultModel.setProductId(productModel.getProductId());
		        			
					  
		        	  destCommissionShSharesDefaultModelList.add(destCommissionShSharesDefaultModel);
				  }
		          productModel.setProductIdCommissionShSharesDefaultModelList(destCommissionShSharesDefaultModelList);
		      }
		    }
		    catch (Exception e)
		    {
		    	e.printStackTrace();
		    	logger.error(e.getMessage(), e);
		    	throw new FrameworkCheckedException(e.getMessage());
		    }
	 
	    
      return productModel;
    }
    else
    {
      if (log.isDebugEnabled())
      {
        log.debug("id is null....creating new instance of Model");
      }

      return new ProductModel();
    }
  }
//**********************************************************************************************************
	@Override
	protected ModelAndView onCreate(HttpServletRequest request, 
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		
		try
	    { 
		 ProductModel productModel = (ProductModel) command;
		 BaseWrapper baseWrapper = new BaseWrapperImpl();
	     baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
		 baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.PRODUCT_CREATE_USECASE_ID));
		 
	     long theDate = new Date().getTime();
	      
	      //Check unique product Name
	      if(!this.productManager.isProductNameUnique(productModel))
	        throw new FrameworkCheckedException("Product with the same name already exists.");
	      
	      productModel.setCreatedOn(new Date(theDate));
	      productModel.setUpdatedOn(new Date(theDate));
	      productModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
	      productModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
	      //**********************************
	      productModel.setBatchMode(false);
	      productModel.setFixedDiscount(0D);
	      productModel.setPercentDiscount(0D);
	      productModel.setUspProductidCheck(0);
	      productModel.setHelpLine(1L);
			if(null==productModel.getBvsConfigId()){
				productModel.setBvsConfigId(2L);
			}
	      //**********************************
	      	      
	      ServiceModel serviceModel = new ServiceModel();
	      serviceModel.setServiceId(productModel.getServiceId());
	      ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(serviceModel, "name", SortingOrder.ASC);
	      referenceDataManager.getReferenceData(referenceDataWrapper);
	      List<ServiceModel> allServiceList = referenceDataWrapper.getReferenceDataList();
	      
	      if(null!=allServiceList && allServiceList.size()>0)
	    	  serviceModel = allServiceList.get(0);
	      //productModel.setProductIntgModuleInfoId(serviceModel.getProductIntgModuleInfoId());
	      //productModel.setProductIntgVoId(serviceModel.getProductIntgVoId()); 
	      	
	      ProductModelVO voModel = getProductModelVO(productModel);
	      baseWrapper.setBasePersistableModel(voModel);
	      populateAuthenticationParams(baseWrapper, request, voModel);
	      baseWrapper = this.productManager.addOrUpdateProduct(baseWrapper);
		
	      if (null != baseWrapper.getBasePersistableModel())
	      {	
	    	String msg = (String) baseWrapper.getObject(ActionAuthorizationConstantsInterface.KEY_AUTHORIZATION_MSG);
	  		if (null == msg) {
	  			msg = "Record saved successfully";
	  		}  
	    	
	  		this.saveMessage(request,msg );
	  		Long productId = null;
	  		Object basePersistableModel = baseWrapper.getBasePersistableModel();
	  		if(basePersistableModel instanceof ProductModel){
	  			ProductModel tempProductModel = (ProductModel)basePersistableModel;
	  			productId = tempProductModel.getProductId();
	  			
	  			ModelAndView modelAndView = new ModelAndView("redirect:productupdateform.html?productId="+productId);
	 	        return modelAndView;
	  		}
	  		else
	  		{
	  			ModelAndView modelAndView = new ModelAndView(getSuccessView());
	 	        return modelAndView;
	  		}
	  		
	  		
	      }
	      else{
		        this.saveMessage(request, "Product with the same name already exists.");
		        return super.showForm(request, response, errors);
		  }
	    }
	    catch (FrameworkCheckedException ex)
	    {
	      if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION ==
	          ex.getErrorCode())
	      {

	        super.saveMessage(request, "Record could not be saved. "+ ex.getMessage());
	        return super.showForm(request, response, errors);
	      }
	      else
	      {
	    	  super.saveMessage(request, ex.getMessage());
	          return super.showForm(request, response, errors); 
	      }

	    }
	    catch (Exception ex)
	    {
	        super.saveMessage(request, "Record could not be saved."+ ex);
	        return super.showForm(request, response, errors);
	    }
		
		
	}
//**********************************************************************************************************
  @Override
  protected ModelAndView onUpdate(HttpServletRequest httpServletRequest,
                                  HttpServletResponse httpServletResponse,
                                  Object object, BindException bindException) throws
      Exception
  {
	ProductModel productModel = (ProductModel)object;
	 /* if(null==productModel.getBvsConfigId()){
		  productModel.setBvsConfigId(2L);
	  }*/
	productModel.setUspProductidCheck(productModel.getUspProductidCheck() == null ? 0 : productModel.getUspProductidCheck() );
    productModel.setFixedDiscount(productModel.getFixedDiscount() == null ? 0 : productModel.getFixedDiscount());
    productModel.setPercentDiscount(productModel.getPercentDiscount() == null ? 0 : productModel.getPercentDiscount());	
    return this.createOrUpdate(httpServletRequest, httpServletResponse, productModel,
                                 bindException);
  }

  private ModelAndView createOrUpdate(HttpServletRequest request,
                                      HttpServletResponse response,
                                      ProductModel productModel,
                                      BindException errors) throws Exception
  {
    try
    {         	
      BaseWrapper baseWrapper = new BaseWrapperImpl();
      baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
	  baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.PRODUCT_UPDATE_USECASE_ID));
      
      SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
      

      long theDate = new Date().getTime();
      
      if (null != id)
      {
    	ProductModel tempProductModel;    	
        searchBaseWrapper.setBasePersistableModel(productModel);
        searchBaseWrapper = this.productManager.loadProduct(searchBaseWrapper); 
        
        //Check unique product Name
        if(!this.productManager.isProductNameUnique(productModel))
        	throw new FrameworkCheckedException("Product with the same name already exists.");
        
        tempProductModel=(ProductModel) searchBaseWrapper.getBasePersistableModel();
        productModel.setCreatedOn( tempProductModel.getCreatedOn() );
        productModel.setCreatedBy( tempProductModel.getCreatedBy() );
        productModel.setUpdatedOn(new Date());
        productModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
        productModel.setVersionNo(tempProductModel.getVersionNo());
        productModel.setAppUserTypeId(tempProductModel.getAppUserTypeId());
        productModel.setUrl(tempProductModel.getUrl());

	  }
      else
      {
        productModel.setCreatedOn(new Date(theDate));
        productModel.setUpdatedOn(new Date(theDate));
        productModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
        productModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
      }
      
      /**
       * added by atif hussain
       */
      if(productModel.getRelationCategoryIdCategoryModel().getCategoryId()==null)
      {
    	  productModel.setRelationCategoryIdCategoryModel(null);
      }
      
      CommissionRateDefaultModel commissionRateDefaultModel = new CommissionRateDefaultModel( );
      commissionRateDefaultModel.setProductId(productModel.getProductId());
      ReferenceDataWrapper commissionRateDefaultDataWrapper = new ReferenceDataWrapperImpl(commissionRateDefaultModel);
      commissionRateDefaultDataWrapper.setBasePersistableModel(commissionRateDefaultModel);
	    try
	    {
	      referenceDataManager.getReferenceData(commissionRateDefaultDataWrapper);
	      if ( commissionRateDefaultDataWrapper.getReferenceDataList() != null && CollectionUtils.isNotEmpty(commissionRateDefaultDataWrapper.getReferenceDataList()) ){
	    	  productModel.setProductIdCommissionRateDefaultModelList( commissionRateDefaultDataWrapper.getReferenceDataList() );
	    	  commissionRateDefaultModel = (CommissionRateDefaultModel) commissionRateDefaultDataWrapper.getReferenceDataList().get(0);
	    	  
	      }
	    }
	    catch (Exception e)
	    {
	    	logger.error(e.getMessage(), e);
	    }
 
      commissionRateDefaultModel.setInclusiveFixAmount(productModel.getInclusiveFixAmount());
      commissionRateDefaultModel.setInclusivePercentAmount(productModel.getInclusivePercentAmount());
      commissionRateDefaultModel.setExclusiveFixAmount(productModel.getExclusiveFixAmount());
      commissionRateDefaultModel.setExclusivePercentAmount(productModel.getExclusivePercentAmount());
      productModel.addProductIdCommissionRateDefaultModel(commissionRateDefaultModel);
      commissionRateDefaultModel.setUpdatedOn(new Date());
      commissionRateDefaultModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
      if ( commissionRateDefaultModel.getCommissionRateDefaultId() == null ){
    	  commissionRateDefaultModel.setCreatedOn(new Date());
    	  commissionRateDefaultModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
      }
      
      this.productManager.saveUpdateCommissionRateDefault(commissionRateDefaultModel);
      
      //update CommissionShSharesDefaultModel for this product
      List < CommissionShSharesDefaultModel > commissions = (List<CommissionShSharesDefaultModel>) productModel.getProductIdCommissionShSharesDefaultModelList();
      //apply FED if selected
      for (CommissionShSharesDefaultModel commissionShSharesDefaultModel : commissions) {
    	  commissionShSharesDefaultModel.setIsFedApplicable(productModel.getIsFed() == null ? false : productModel.getIsFed());
      }
      
      if (productModel.getFedShare()!=null){//get FED
    	  CommissionShSharesDefaultModel commissionShSharesDefaultModel = new CommissionShSharesDefaultModel();
    	  commissionShSharesDefaultModel.setCommissionStakeholderId(CommissionConstantsInterface.FED_STAKE_HOLDER_ID);
    	  commissionShSharesDefaultModel.setProductId(productModel.getProductId());
    	  ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl();
          referenceDataWrapper.setBasePersistableModel(commissionShSharesDefaultModel);
          referenceDataManager.getReferenceData(referenceDataWrapper);
          if (referenceDataWrapper.getReferenceDataList() != null && CollectionUtils.isNotEmpty(referenceDataWrapper.getReferenceDataList())){
        	  commissionShSharesDefaultModel =(CommissionShSharesDefaultModel) referenceDataWrapper.getReferenceDataList().get(0);
        	  commissionShSharesDefaultModel.setCommissionShare(productModel.getFedShare());
          }else{//newly added fed
        	  commissionShSharesDefaultModel.setCommissionShare(productModel.getFedShare());
        	  //commissionShSharesDefaultModel.setIsFedApplicable(true);
          }
          commissions.add(commissionShSharesDefaultModel);
      }
      
      if (productModel.getWithHoldingShare() == null || productModel.getWithHoldingShare() != null){//get W.H
    	  CommissionShSharesDefaultModel commissionShSharesDefaultModel = new CommissionShSharesDefaultModel();
    	  commissionShSharesDefaultModel.setCommissionStakeholderId(CommissionConstantsInterface.WHT_STAKE_HOLDER_ID);
    	  commissionShSharesDefaultModel.setProductId(productModel.getProductId());
    	  ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl();
          referenceDataWrapper.setBasePersistableModel(commissionShSharesDefaultModel);
          referenceDataManager.getReferenceData(referenceDataWrapper);
          if (referenceDataWrapper.getReferenceDataList() != null && CollectionUtils.isNotEmpty(referenceDataWrapper.getReferenceDataList())){
        	  commissionShSharesDefaultModel =(CommissionShSharesDefaultModel) referenceDataWrapper.getReferenceDataList().get(0);
        	  commissionShSharesDefaultModel.setCommissionShare(productModel.getWithHoldingShare());
          }else{//newly added w.h.
        	  commissionShSharesDefaultModel.setCommissionShare(productModel.getWithHoldingShare());
        	  //commissionShSharesDefaultModel.setIsWhtApplicable(true);
          }
          commissions.add(commissionShSharesDefaultModel);
      }
      
      this.productManager.saveUpdateCommissionShSharesDefaul(commissions);
      
      
      productModel.setActive( productModel.getActive() == null ? false : productModel.getActive() );
      productModel.setBatchMode( productModel.getBatchMode() == null ? false : productModel.getBatchMode() );
  		
      /*ADDED BY ATIF HUSSAIN*/
      try {
    	  baseWrapper = updateCoreAndBBAccounts(
				productModel.getProductId(),
				productModel.getName(),
				productModel.getInclChargesCheck(),
				productModel.getAccountNick(),
				productModel.getAccountNo(), baseWrapper);
      } catch (Exception e) {
		super.saveMessage(request, "Record could not be saved." + e);
		return super.showForm(request, response, errors);
      }
      /*end of added by atif hussain*/

      baseWrapper.setBasePersistableModel(productModel);
      baseWrapper = this.productManager.createOrUpdateProduct(baseWrapper);
 
      if (null != baseWrapper.getBasePersistableModel())
      {
        this.saveMessage(request, "Record saved successfully");

      /*  ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
        return modelAndView;*/
        return super.showForm(request, response, errors);
      }
      else
      {
        this.saveMessage(request, "Product with the same name already exists.");
        return super.showForm(request, response, errors);
      }
    }
    catch (FrameworkCheckedException ex)
    {
      if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION ==
          ex.getErrorCode())
      {

        super.saveMessage(request, "Record could not be saved. "+ ex.getMessage());
        return super.showForm(request, response, errors);
      }
      else
      {
    	  super.saveMessage(request, ex.getMessage());
          return super.showForm(request, response, errors); 
      }

    }
    catch (Exception ex)
    {
        super.saveMessage(request, "Record could not be saved."+ ex);
        return super.showForm(request, response, errors);
    }

  }

  private void createUpdateCoreAccount(ProductModel	productModel, BaseWrapper	baseWrapper) throws FrameworkCheckedException
  {

    	StakeholderBankInfoModel stakeholderBankInfoModelCB = new StakeholderBankInfoModel();
		
    	SearchBaseWrapper	searchBaseWrapper =new SearchBaseWrapperImpl();
		StakehldBankInfoListViewModel stakehldBankInfoListViewModel =new StakehldBankInfoListViewModel();
		stakehldBankInfoListViewModel.setProductId(productModel.getProductId());
		stakehldBankInfoListViewModel.setBankId(CommissionConstantsInterface.BANK_ID);
		searchBaseWrapper.setBasePersistableModel(stakehldBankInfoListViewModel);
		
		CustomList<StakehldBankInfoListViewModel> customList= this.stakeholderBankInfoManager.searchStakeHolderBankInfo(searchBaseWrapper).getCustomList();
		if(null != customList && customList.getResultsetList()!=null && customList.getResultsetList().size()>0)
		{
			stakehldBankInfoListViewModel	=	(StakehldBankInfoListViewModel) customList.getResultsetList().get(0);
			Long pk	=stakehldBankInfoListViewModel.getStakeholderBankInfoId();
			stakeholderBankInfoModelCB.setPrimaryKey(pk);
			searchBaseWrapper =new SearchBaseWrapperImpl();
			searchBaseWrapper.setBasePersistableModel(stakeholderBankInfoModelCB);
			searchBaseWrapper=stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper);
			
			stakeholderBankInfoModelCB =	(StakeholderBankInfoModel)searchBaseWrapper.getBasePersistableModel();
			
			if(!productModel.getInclChargesCheck())
			{
				stakeholderBankInfoModelCB.setActive(Boolean.FALSE);
			}
			else
			{
				stakeholderBankInfoModelCB.setActive(Boolean.TRUE);
				stakeholderBankInfoModelCB.setName(productModel.getAccountNick());
				stakeholderBankInfoModelCB.setAccountNo(productModel.getAccountNo());
			}
		}
		else//incase of new record
		{
			stakeholderBankInfoModelCB.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			stakeholderBankInfoModelCB.setCreatedByAppUserModel(UserUtils.getCurrentUser());
			stakeholderBankInfoModelCB.setUpdatedOn(new Date());
			stakeholderBankInfoModelCB.setCreatedOn(new Date());
			stakeholderBankInfoModelCB.setBankId(CommissionConstantsInterface.BANK_ID);
			stakeholderBankInfoModelCB.setActive(true);
			stakeholderBankInfoModelCB.setCmshaccttypeId(1L);
			stakeholderBankInfoModelCB.setProductId(productModel.getProductId());
			stakeholderBankInfoModelCB.setAccountNo(productModel.getAccountNo());
			stakeholderBankInfoModelCB.setName(productModel.getAccountNick());
		}
		
		baseWrapper.putObject("STAKE_HOLDER_BANK_INFO_MODEL_CB", stakeholderBankInfoModelCB);
  }

  	private void createUpdateBBAccount(ProductModel	productModel, BaseWrapper	baseWrapper) throws Exception
  	{
  		StakeholderBankInfoModel stakeholderBankInfoModelBB = new StakeholderBankInfoModel();
  		AccountModel accountModel = new AccountModel();
  		
  		SearchBaseWrapper	searchBaseWrapper =new SearchBaseWrapperImpl();
		StakehldBankInfoListViewModel stakehldBankInfoListViewModel =new StakehldBankInfoListViewModel();
		stakehldBankInfoListViewModel.setProductId(productModel.getProductId());
		stakehldBankInfoListViewModel.setBankId(BankConstantsInterface.OLA_BANK_ID);
		searchBaseWrapper.setBasePersistableModel(stakehldBankInfoListViewModel);
		
		CustomList<StakehldBankInfoListViewModel> customList= this.stakeholderBankInfoManager.searchStakeHolderBankInfo(searchBaseWrapper).getCustomList();
		if(null != customList && customList.getResultsetList()!=null && customList.getResultsetList().size()>0)
		{
			stakehldBankInfoListViewModel	=	(StakehldBankInfoListViewModel) customList.getResultsetList().get(0);
			Long pk	=stakehldBankInfoListViewModel.getStakeholderBankInfoId();
			stakeholderBankInfoModelBB.setPrimaryKey(pk);
			searchBaseWrapper =new SearchBaseWrapperImpl();
			searchBaseWrapper.setBasePersistableModel(stakeholderBankInfoModelBB);
			searchBaseWrapper=stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper);
			
			stakeholderBankInfoModelBB =	(StakeholderBankInfoModel)searchBaseWrapper.getBasePersistableModel();
			stakeholderBankInfoModelBB.setActive(productModel.getInclChargesCheck());
			
			if(productModel.getInclChargesCheck())
			{
				stakeholderBankInfoModelBB.setName(productModel.getAccountNick()+" -BLB");
			}
			
	  		searchBaseWrapper =new SearchBaseWrapperImpl();
			accountModel = new AccountModel();
			accountModel.setAccountNumber(EncryptionUtil.encryptAccountNo(stakeholderBankInfoModelBB.getAccountNo()));
			searchBaseWrapper.setBasePersistableModel(accountModel);
			
			CustomList<AccountModel> accountCustomList= this.accountManager.searchAccount(searchBaseWrapper).getCustomList();
			if(null == accountCustomList || accountCustomList.getResultsetList()==null || accountCustomList.getResultsetList().size()==0)
			{
				Date dateNow = new Date();
				
				AccountHolderModel accountHolderModel = new AccountHolderModel() ;
				
				accountHolderModel.setFirstName(productModel.getAccountNick()+" -BLB");
				accountHolderModel.setMiddleName("-");
				accountHolderModel.setLastName("-");
				accountHolderModel.setFatherName("-");
//				accountHolderModel.setCnic(EncryptionUtil.encryptPin("-"));
				accountHolderModel.setCnic("-");
				accountHolderModel.setAddress("-");
				accountHolderModel.setLandlineNumber("-");
				accountHolderModel.setMobileNumber("-");
				
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");			
//				accountHolderModel.setDob( EncryptionUtil.encryptPin(dateFormat.format(new Date())));
				accountHolderModel.setDob(dateFormat.format(new Date()));
				accountHolderModel.setLandlineNumber("-");
				accountHolderModel.setCreatedOn(dateNow);
				accountHolderModel.setUpdatedOn(dateNow);
				
				accountModel = new AccountModel() ;
				accountModel.setAccountNumber(EncryptionUtil.encryptAccountNo(stakeholderBankInfoModelBB.getAccountNo()));
				accountModel.setStatusId(StatusConstants.ACTIVE) ;
				accountModel.setCreatedOn(dateNow);
				accountModel.setUpdatedOn(dateNow);
				accountModel.setVersionNo(0);
				accountModel.setCustomerAccountTypeId(CustomerAccountTypeConstants.SETTLEMENT);
				
				if(productModel.getInclChargesCheck())
				{
					accountModel.setStatusId(StatusConstants.ACTIVE);
				}else
				{
					accountModel.setStatusId(StatusConstants.INACTIVE);
				}
				
//				accountModel.setBalance(EncryptionUtil.encryptPin("0"));
				accountModel.setBalance("0");
				accountHolderModel.addAccountHolderIdAccountModel(accountModel) ;
			}
			else{
				accountModel	=	accountCustomList.getResultsetList().get(0);
				AccountHolderModel accountHolderModel=accountModel.getRelationAccountHolderIdAccountHolderModel();
				accountHolderModel.setActive(productModel.getInclChargesCheck());
				
				if(productModel.getInclChargesCheck())
				{
					accountModel.setStatusId(StatusConstants.ACTIVE);
					accountHolderModel.setFirstName(productModel.getAccountNick()+" -BLB");
				}
				else
				{
					 accountModel.setStatusId(StatusConstants.INACTIVE);
				}
			}
		}
		else
		{
			Date dateNow = new Date();
			
			AccountHolderModel accountHolderModel = new AccountHolderModel() ;
			
			accountHolderModel.setFirstName(productModel.getAccountNick()+" -BLB");
			accountHolderModel.setMiddleName("-");
			accountHolderModel.setLastName("-");
			accountHolderModel.setFatherName("-");
//			accountHolderModel.setCnic(EncryptionUtil.encryptPin("-"));
			accountHolderModel.setCnic("-");
			accountHolderModel.setAddress("-");
			accountHolderModel.setLandlineNumber("-");
			accountHolderModel.setMobileNumber("-");
			
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");			
//			accountHolderModel.setDob( EncryptionUtil.encryptPin(dateFormat.format(new Date())));
			accountHolderModel.setDob( dateFormat.format(new Date()));
			accountHolderModel.setLandlineNumber("-");
			accountHolderModel.setCreatedOn(dateNow);
			accountHolderModel.setUpdatedOn(dateNow);
			
			String generatedAccountNumber	=	AccountNumberGenerator.getAccountNumber();
			
			accountModel = new AccountModel() ;
			accountModel.setAccountNumber(EncryptionUtil.encryptAccountNo(generatedAccountNumber)) ;
			accountModel.setStatusId(StatusConstants.ACTIVE) ;
			accountModel.setCreatedOn(dateNow);
			accountModel.setUpdatedOn(dateNow);
			accountModel.setVersionNo(0);
			accountModel.setCustomerAccountTypeId(CustomerAccountTypeConstants.SETTLEMENT);
			
			if(productModel.getInclChargesCheck())
			{
				accountModel.setStatusId(StatusConstants.ACTIVE);
			}else
			{
				accountModel.setStatusId(StatusConstants.INACTIVE);
			}
			
//			accountModel.setBalance(EncryptionUtil.encryptPin("0"));
			accountModel.setBalance("0");
			accountHolderModel.addAccountHolderIdAccountModel(accountModel) ;
			
			//make new stake holder for BB
			stakeholderBankInfoModelBB.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			stakeholderBankInfoModelBB.setCreatedByAppUserModel(UserUtils.getCurrentUser());
			stakeholderBankInfoModelBB.setUpdatedOn(new Date());
			stakeholderBankInfoModelBB.setCreatedOn(new Date());
			stakeholderBankInfoModelBB.setBankId(BankConstantsInterface.OLA_BANK_ID);
			stakeholderBankInfoModelBB.setActive(true);
			stakeholderBankInfoModelBB.setCmshaccttypeId(1L);
			stakeholderBankInfoModelBB.setProductId(productModel.getProductId());
			stakeholderBankInfoModelBB.setAccountNo(generatedAccountNumber);
			stakeholderBankInfoModelBB.setName(productModel.getAccountNick()+" -BLB");
		}
  		
		baseWrapper.putObject("OLA_ACCOUNT_MODEL", accountModel);
		baseWrapper.putObject("STAKE_HOLDER_BANK_INFO_MODEL_BB", stakeholderBankInfoModelBB);
  	}

	private List<CommStakeholderListViewModel> filterStakeholderList(List<CommStakeholderListViewModel> commStakeholderListViewModelList, ProductModel productModel){
		List<CommStakeholderListViewModel> filterredList = new ArrayList<CommStakeholderListViewModel>(0);
		for (CommStakeholderListViewModel commissionStakeholderModel : commStakeholderListViewModelList) {
			if (commissionStakeholderModel.getCommissionStakeholderId().longValue() != CommissionConstantsInterface.WHT_STAKE_HOLDER_ID &&
				 commissionStakeholderModel.getCommissionStakeholderId().longValue() != CommissionConstantsInterface.FED_STAKE_HOLDER_ID &&
				 commissionStakeholderModel.getCommissionStakeholderId().longValue() != CommissionConstantsInterface.HIERARCHY2_STAKE_HOLDER_ID &&
				 commissionStakeholderModel.getCommissionStakeholderId().longValue() != CommissionConstantsInterface.HIERARCHY_STAKE_HOLDER_ID
					&& (commissionStakeholderModel.getDisplayOnProductScreen() != null && commissionStakeholderModel.getDisplayOnProductScreen())){

				filterredList.add(commissionStakeholderModel);
			}
		}
		return filterredList;
	}
	
	private List<CommissionStakeholderModel> filterStakeholderModelList(List<CommissionStakeholderModel> commissionStakeholderModelList){
		List<CommissionStakeholderModel> filterredList = new ArrayList<CommissionStakeholderModel>(0);
		for (CommissionStakeholderModel commissionStakeholderModel : commissionStakeholderModelList) {
			if (commissionStakeholderModel.getCommissionStakeholderId().longValue() != CommissionConstantsInterface.WHT_STAKE_HOLDER_ID &&
				 commissionStakeholderModel.getCommissionStakeholderId().longValue() != CommissionConstantsInterface.FED_STAKE_HOLDER_ID &&
				 commissionStakeholderModel.getCommissionStakeholderId().longValue() != CommissionConstantsInterface.HIERARCHY2_STAKE_HOLDER_ID &&
				 commissionStakeholderModel.getCommissionStakeholderId().longValue() != CommissionConstantsInterface.HIERARCHY_STAKE_HOLDER_ID
				 && (commissionStakeholderModel.getDisplayOnProductScreen() != null && commissionStakeholderModel.getDisplayOnProductScreen())){
				
				filterredList.add(commissionStakeholderModel);
			}
		}
		return filterredList;
	}

	private BaseWrapper 	updateCoreAndBBAccounts(Long productId,String productName,boolean inclusiveCharges, String accountTitle, String oracleNumber, BaseWrapper baseWrapper) throws Exception{

		List<StakeholderBankInfoModel> stakeholderBankInfoModelList	= this.stakeholderBankInfoManager.getStakeholderBankInfoForProduct(productId);
		
		int accountSize	=	0;
		
		if(stakeholderBankInfoModelList!=null)
		{
			accountSize	=	stakeholderBankInfoModelList.size();
		}

		if(accountSize==0 && inclusiveCharges)//NEW CASE
		{
			StakeholderBankInfoModel coreAccount=new StakeholderBankInfoModel();
			Date dateNow=new Date();
			
			coreAccount.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			coreAccount.setCreatedByAppUserModel(UserUtils.getCurrentUser());
			coreAccount.setUpdatedOn(dateNow);
			coreAccount.setCreatedOn(dateNow);
			coreAccount.setBankId(CommissionConstantsInterface.BANK_ID);
			coreAccount.setActive(Boolean.TRUE);
			coreAccount.setCmshaccttypeId(1L);
			coreAccount.setProductId(productId);
			coreAccount.setAccountNo(oracleNumber);
			coreAccount.setName(accountTitle);

			String generatedBBAccountNumber	=	AccountNumberGenerator.getAccountNumber();
			AccountModel	accountModel	=	this.populateAccountHolder(accountTitle,productName,generatedBBAccountNumber);
			
			StakeholderBankInfoModel bbAccount=new StakeholderBankInfoModel();				
			bbAccount.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			bbAccount.setCreatedByAppUserModel(UserUtils.getCurrentUser());
			bbAccount.setUpdatedOn(dateNow);
			bbAccount.setCreatedOn(dateNow);
			bbAccount.setBankId(BankConstantsInterface.OLA_BANK_ID);
			bbAccount.setActive(true);
			bbAccount.setCmshaccttypeId(1L);
			bbAccount.setProductId(productId);
			bbAccount.setAccountNo(generatedBBAccountNumber);
			bbAccount.setName(accountTitle+" -BLB "+productName);
			
			//added by Hassan
			bbAccount.setAccountType("BLB");
			coreAccount.setAccountType("OF_SET");
			
			baseWrapper.putObject("STAKE_HOLDER_BANK_INFO_MODEL_CB", coreAccount);
			baseWrapper.putObject("STAKE_HOLDER_BANK_INFO_MODEL_BB", bbAccount);
			baseWrapper.putObject("OLA_ACCOUNT_MODEL", accountModel);
		}
		if(accountSize ==1)//UPDATE CASE OLD RECORDS
		{
			StakeholderBankInfoModel	coreAccount	=	null;
			StakeholderBankInfoModel	bbAccount	=	null;
			AccountModel accountModel = new AccountModel();

			if (stakeholderBankInfoModelList.get(0).getBankId().longValue() == BankConstantsInterface.OLA_BANK_ID.longValue()) {
				bbAccount = stakeholderBankInfoModelList.get(0);
			} else {
				coreAccount = stakeholderBankInfoModelList.get(0);
			}
			
			if(bbAccount==null)
			{
				Date dateNow=new Date();
				
				String generatedBBAccountNumber	=	AccountNumberGenerator.getAccountNumber();
				accountModel = this.populateAccountHolder(accountTitle,productName,generatedBBAccountNumber);

				bbAccount=new StakeholderBankInfoModel();				
				bbAccount.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
				bbAccount.setCreatedByAppUserModel(UserUtils.getCurrentUser());
				bbAccount.setUpdatedOn(dateNow);
				bbAccount.setCreatedOn(dateNow);
				bbAccount.setBankId(BankConstantsInterface.OLA_BANK_ID);
				bbAccount.setCmshaccttypeId(1L);
				bbAccount.setProductId(productId);
				bbAccount.setAccountNo(generatedBBAccountNumber);
				
				if(inclusiveCharges){
					bbAccount.setName(accountTitle+" -BLB "+productName);
					bbAccount.setActive(Boolean.TRUE);
					coreAccount.setName(accountTitle);
					coreAccount.setActive(Boolean.TRUE);
					coreAccount.setAccountNo(oracleNumber);
					accountModel.setStatusId(StatusConstants.ACTIVE);
					accountModel.getAccountHolderIdAccountHolderModel().setActive(Boolean.TRUE);
				}else{
					bbAccount.setActive(Boolean.FALSE);
					bbAccount.setName(coreAccount.getName()+" -BLB "+productName);
					coreAccount.setActive(Boolean.FALSE);
					accountModel.setStatusId(StatusConstants.INACTIVE);
					accountModel.getAccountHolderIdAccountHolderModel().setActive(Boolean.FALSE);
				}
				
				//added by Hassan
				bbAccount.setAccountType("BLB");
				coreAccount.setAccountType("OF_SET");
				
				baseWrapper.putObject("STAKE_HOLDER_BANK_INFO_MODEL_CB", coreAccount);
				baseWrapper.putObject("STAKE_HOLDER_BANK_INFO_MODEL_BB", bbAccount);
				baseWrapper.putObject("OLA_ACCOUNT_MODEL", accountModel);
			}
		}
		if(accountSize==2)//UPDATE CASE
		{
			StakeholderBankInfoModel	coreAccount=new StakeholderBankInfoModel();
			StakeholderBankInfoModel	bbAccount=new StakeholderBankInfoModel();
			AccountModel accountModel = new AccountModel();

			if (stakeholderBankInfoModelList.get(0).getBankId().longValue() == BankConstantsInterface.OLA_BANK_ID.longValue()) {
				bbAccount = stakeholderBankInfoModelList.get(0);
			} else {
				coreAccount = stakeholderBankInfoModelList.get(0);
			}

			if (stakeholderBankInfoModelList.get(1).getBankId().longValue() == BankConstantsInterface.OLA_BANK_ID.longValue()) {
				bbAccount = stakeholderBankInfoModelList.get(1);
			} else {
				coreAccount = stakeholderBankInfoModelList.get(1);
			}
			
			SearchBaseWrapperImpl searchBaseWrapper =new SearchBaseWrapperImpl();
			accountModel.setAccountNumber(EncryptionUtil.encryptAccountNo(bbAccount.getAccountNo()));
			searchBaseWrapper.setBasePersistableModel(accountModel);
			
			CustomList<AccountModel> accountCustomList= this.accountManager.searchAccount(searchBaseWrapper).getCustomList();
			
			if(inclusiveCharges){
				bbAccount.setActive(Boolean.TRUE);
				coreAccount.setActive(Boolean.TRUE);
				
				bbAccount.setName(accountTitle+" -BLB "+productName);
				coreAccount.setName(accountTitle);
				coreAccount.setAccountNo(oracleNumber);
				
				if(accountCustomList.getResultsetList().size() > 0)
				{
					accountModel	=	accountCustomList.getResultsetList().get(0);
					accountModel.setStatusId(StatusConstants.ACTIVE);
					accountModel.getAccountHolderIdAccountHolderModel().setActive(Boolean.TRUE);
					accountModel.getAccountHolderIdAccountHolderModel().setFirstName(accountTitle+"-BLB "+productName);
				}
			}
			else
			{
				bbAccount.setActive(Boolean.FALSE);
				coreAccount.setActive(Boolean.FALSE);
				
				if(accountCustomList.getResultsetList().size() > 0)
				{
					accountModel	=	accountCustomList.getResultsetList().get(0);
					accountModel.setStatusId(StatusConstants.INACTIVE);
					accountModel.getAccountHolderIdAccountHolderModel().setActive(Boolean.FALSE);
				}
			}
			
			//added by atif hussain
			bbAccount.setAccountType("BLB");
			coreAccount.setAccountType("OF_SET");
			
			baseWrapper.putObject("STAKE_HOLDER_BANK_INFO_MODEL_CB", coreAccount);
			baseWrapper.putObject("STAKE_HOLDER_BANK_INFO_MODEL_BB", bbAccount);
			baseWrapper.putObject("OLA_ACCOUNT_MODEL", accountModel);
		}

		return baseWrapper;
	}
	

	public AccountModel populateAccountHolder(String accountTitle,String productName,String generatedBBAccountNumber) throws Exception
	{
		Date dateNow=new Date();
		
		AccountHolderModel accountHolderModel = new AccountHolderModel() ;
		
		accountHolderModel.setFirstName(accountTitle+"-BLB "+productName);
		accountHolderModel.setMiddleName("-");
		accountHolderModel.setLastName("-");
		accountHolderModel.setFatherName("-");
//		accountHolderModel.setCnic(EncryptionUtil.encryptPin("-"));
		//Code Added by Attique Butt on 29/09/2017 to avoid Cnic Unique index constraint on account holder
		accountHolderModel.setCnic("-"+generatedBBAccountNumber+"-");

		accountHolderModel.setAddress("-");
		accountHolderModel.setLandlineNumber("-");
		accountHolderModel.setMobileNumber("-");
		accountHolderModel.setActive(Boolean.TRUE);
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");			
//		accountHolderModel.setDob( EncryptionUtil.encryptPin(dateFormat.format(new Date())));
		accountHolderModel.setDob(dateFormat.format(new Date()));
		accountHolderModel.setLandlineNumber("-");
		accountHolderModel.setCreatedOn(dateNow);
		accountHolderModel.setUpdatedOn(dateNow);
		
		String encryptedAccountNumber	=	EncryptionUtil.encryptAccountNo(generatedBBAccountNumber);
		
		AccountModel accountModel = new AccountModel() ;
		accountModel.setAccountNumber(encryptedAccountNumber) ;
		accountModel.setCreatedOn(dateNow);
		accountModel.setUpdatedOn(dateNow);
		accountModel.setVersionNo(0);
		accountModel.setCustomerAccountTypeId(CustomerAccountTypeConstants.SETTLEMENT);
		accountModel.setStatusId(StatusConstants.ACTIVE);
//		accountModel.setBalance(EncryptionUtil.encryptPin("0"));
		accountModel.setBalance("0");
		accountHolderModel.addAccountHolderIdAccountModel(accountModel);
		return accountModel;
	}
	
	
	
	
	private ProductModelVO  getProductModelVO(ProductModel productModel) {
		ProductModelVO productModelVO= new ProductModelVO();
		
		productModelVO.setProductId(productModel.getProductId());
		productModelVO.setName(productModel.getName()); 
		productModelVO.setDescription(productModel.getDescription());
		productModelVO.setActive(productModel.getActive());
		productModelVO.setTaxable(productModel.getTaxable());
		productModelVO.setUpdatedOn(productModel.getUpdatedOn());
		productModelVO.setCreatedOn(productModel.getCreatedOn());
		productModelVO.setVersionNo(productModel.getVersionNo());
		productModelVO.setProductCode(productModel.getProductCode());
		productModelVO.setMinLimit(productModel.getMinLimit());
		productModelVO.setMaxLimit(productModel.getMaxLimit());
		productModelVO.setInclChargesCheck(productModel.getInclChargesCheck());
		productModelVO.setConsumerLabel(productModel.getConsumerLabel());
		productModelVO.setConsumerInputType(productModel.getConsumerInputType());
		productModelVO.setAmtRequired(productModel.getAmtRequired());
		productModelVO.setMultiples(productModel.getMultiples());
		productModelVO.setAccountNo(productModel.getAccountNo());
		productModelVO.setAccountNick(productModel.getAccountNick());
		 
		productModelVO.setExclusiveFixAmount(productModel.getExclusiveFixAmount());
		productModelVO.setExclusivePercentAmount(productModel.getExclusivePercentAmount());
		productModelVO.setInclusiveFixAmount(productModel.getInclusiveFixAmount());
		productModelVO.setInclusivePercentAmount(productModel.getInclusivePercentAmount());
		//********************************************************************************
		productModelVO.setFedShare(productModel.getFedShare());
		productModelVO.setWithHoldingShare(productModel.getWithHoldingShare());
		productModelVO.setIsFed(productModel.getIsFed());
		//******************************************************************************** 
	    productModelVO.setSupplierId(productModel.getSupplierId());
	    productModelVO.setServiceId(productModel.getServiceId());
		productModelVO.setwHTConfigId(productModel.getWhtConfigId());
		productModelVO.setCommissionStakeholderId(productModel.getCommissionStakeHolderId());
		productModelVO.setCreatedByID(productModel.getCreatedBy());
		productModelVO.setUpdatedByID(productModel.getUpdatedBy());
		
		productModelVO.setProductIntgModuleInfoId(productModel.getProductIntgModuleInfoId());
		productModelVO.setProductIntgVoId(productModel.getProductIntgVoId());
		productModelVO.setInstructionId(productModel.getInstructionId());
		productModelVO.setSuccessMessageId(productModel.getSuccessMessageId());
		productModelVO.setFailureMessageId(productModel.getFailureMessageId());
		productModelVO.setHelpLine(productModel.getHelpLine());
		productModelVO.setCostPrice(productModel.getCostPrice());
		productModelVO.setUnitPrice(productModel.getUnitPrice());
		
		productModelVO.setFixedDiscount(productModel.getFixedDiscount());
		productModelVO.setPercentDiscount(productModel.getPercentDiscount());
		productModelVO.setMinimumStockLevel(productModel.getMinimumStockLevel());
		productModelVO.setBatchMode(productModel.getBatchMode());
		productModelVO.setDoValidate(productModel.getDoValidate());
		productModelVO.setCategoryCode(productModel.getCategoryCode());
		productModelVO.setCategoryId(productModel.getCategoryId());
		productModelVO.setUspProductidCheck(productModel.getUspProductidCheck());
		//productModelVO.setAppUserTypeId(productModel.getAppUserTypeId());

	
		
		try {
			for (CommissionShSharesDefaultModel model : productModel.getProductIdCommissionShSharesDefaultModelList()) {
				CommissionShSharesDefaultModelVO newModel = new CommissionShSharesDefaultModelVO();
				newModel.setCommissionShSharesDefaultId(model.getCommissionShSharesDefaultId());
				newModel.setCommissionStakeholderIdPlain(model.getCommissionStakeholderId());
				newModel.setProductIdPlain(model.getProductId());
				newModel.setCommissionShare(model.getCommissionShare());
				newModel.setIsWhtApplicable(model.getIsWhtApplicable());
				
				productModelVO.getProductIdCommissionShSharesDefaultModelList().add(newModel);
			}
		} catch (Exception e) {e.printStackTrace();}
		
		return productModelVO;
	}
	
	
	private List<WHTConfigModel> getWHTConfigFilteredList(List<WHTConfigModel> wHTConfigModelList) {
		List<WHTConfigModel> filteredList = new ArrayList<WHTConfigModel>();
		
		for (WHTConfigModel whtConfigModel : wHTConfigModelList) {
			
			if(whtConfigModel.getWhtConfigId().equals(2L) || whtConfigModel.getWhtConfigId().equals(3L))
				filteredList.add(whtConfigModel);
		}	
		return filteredList;
	}
	
	
	
	public void setProductManager(ProductManager productManager) {
		this.productManager = productManager;
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public void setCatalogManager(ProductCatalogManager catalogManager) {
		this.catalogManager = catalogManager;
	}

	public ProductCatalogManager getCatalogManager() {
		return catalogManager;
	}

	public StakeholderBankInfoManager getStakeholderBankInfoManager() {
		return stakeholderBankInfoManager;
	}

	public void setStakeholderBankInfoManager(
			StakeholderBankInfoManager stakeholderBankInfoManager) {
		this.stakeholderBankInfoManager = stakeholderBankInfoManager;
	}
	
	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}
}
