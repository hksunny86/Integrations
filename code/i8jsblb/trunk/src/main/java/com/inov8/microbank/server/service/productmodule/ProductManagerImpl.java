package com.inov8.microbank.server.service.productmodule;
  
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import com.inov8.microbank.cardconfiguration.vo.ProductThresholdChargesVO;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.server.dao.productmodule.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.integration.common.model.AccountModel;
import com.inov8.microbank.common.model.portal.authorizationreferencedata.CommissionShSharesDefaultModelVO;
import com.inov8.microbank.common.model.portal.authorizationreferencedata.ProductModelVO;
import com.inov8.microbank.common.model.productmodule.DiscreteProdUnitListViewModel;
import com.inov8.microbank.common.model.productmodule.ProductLimitRuleViewModel;
import com.inov8.microbank.common.model.productmodule.ProductListViewModel;
import com.inov8.microbank.common.vo.product.ProductChargesRuleVo;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.dao.inventorymodule.ProductUnitDAO;
import com.inov8.microbank.server.dao.inventorymodule.ShipmentDAO;
import com.inov8.microbank.server.dao.stakeholdermodule.StakeholderBankInfoDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;
import com.inov8.ola.server.dao.account.AccountDAO;
import com.inov8.ola.server.dao.accountholder.AccountHolderDAO;



/**
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backend Application for POS terminals.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Asad Hayat
 * @version 1.0
 */
public class ProductManagerImpl
    implements ProductManager
{
	protected final Log log = LogFactory.getLog(getClass());

  private ProductDAO productDAO;
  private ProductLimitRuleDAO productLimitRuleDAO;
  private ProductListViewDAO productListViewDAO;
  private ProductUnitDAO productUnitDAO;
  private DiscreteProdUnitListViewDAO discreteProdUnitListViewDAO;
  private ShipmentDAO shipmentDAO;
  private ActionLogManager actionLogManager;
  private ProductLimitDAO productLimitDAO;
  private StakeholderBankInfoDAO stakeholderBankInfoDAO;
  //********************************************************************
  private StakeholderBankInfoManager stakeholderBankInfoManager;
  private ReferenceDataManager referenceDataManager;
  //********************************************************************
  @Autowired
  private ProductChargesRuleDao productChargesRuleDao;
    @Autowired
  private ProductThresholdChargesDAO productThresholdChargesDAO;
  private ProductCatalogDAO productCatalogDAO;
  private ProductCatalogDetailDAO productCatalogDetailDAO;
  
  @Autowired
  private AccountDAO	accountDAO;
  
  @Autowired
  private AccountHolderDAO	accountHolderDAO;
  
  
  public ProductManagerImpl()
  {
  }

  public SearchBaseWrapper loadProduct(SearchBaseWrapper searchBaseWrapper)
  {
    ProductModel productModel = (ProductModel)this.productDAO.findByPrimaryKey( (
        searchBaseWrapper.getBasePersistableModel()).getPrimaryKey());
    searchBaseWrapper.setBasePersistableModel(productModel);
    return searchBaseWrapper;
  }

  public BaseWrapper loadProduct(BaseWrapper baseWrapper)
  {
    ProductModel productModel = (ProductModel)this.
        productDAO.findByPrimaryKey( (baseWrapper.getBasePersistableModel()).
                                    getPrimaryKey());
    baseWrapper.setBasePersistableModel(productModel);
    return baseWrapper;
  }

  public SearchBaseWrapper searchProduct(SearchBaseWrapper searchBaseWrapper)
  {
	  CustomList<ProductListViewModel> customList = null;
	  if (((ProductListViewModel) searchBaseWrapper.getBasePersistableModel()).getProductId()==null){
		  customList = this.productListViewDAO.findByExample( (ProductListViewModel)
				  								searchBaseWrapper.getBasePersistableModel(),
										        searchBaseWrapper.getPagingHelperModel(),
										        searchBaseWrapper.getSortingOrderMap());
	  }
	  else{
		  ProductListViewModel product  = (ProductListViewModel) this.productListViewDAO.findByPrimaryKey
				  						  (((ProductListViewModel) searchBaseWrapper.getBasePersistableModel()).getProductId());
		  List<ProductListViewModel> list = new ArrayList<ProductListViewModel>(1);
		  list.add(product);
		  customList = new CustomList<ProductListViewModel>(list);
		  searchBaseWrapper.getPagingHelperModel().setTotalRecordsCount(list.size());
	  }
	  searchBaseWrapper.setCustomList(customList);
	  //TODO RSO agent product to be removed based on madihah's discussion. need to revert back when it is going to production with RSO changes.
	  if(null != customList && null != customList.getResultsetList() && customList.getResultsetList().size() > 0){
		  Iterator<ProductListViewModel> i = customList.getResultsetList().iterator();

		  while (i.hasNext()) {
			  ProductListViewModel model = i.next();
		      if (model.getProductId().equals(new Long(50014))) {
		          i.remove();
		          break;
		      }
		  }
	  }
	  
    return searchBaseWrapper;
  }

  public BaseWrapper createOrUpdateProduct(BaseWrapper baseWrapper)throws FrameworkCheckedException
  {

	ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
	ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
	
	ProductModel newProductModel = new ProductModel();
    ProductModel productModel = (ProductModel) baseWrapper.getBasePersistableModel();
    newProductModel.setName(productModel.getName());
    ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
	exampleHolder.setMatchMode(MatchMode.EXACT);
    int recordCount = productDAO.countByExample(newProductModel,exampleHolder);
    //***Check if name already exists
     if (recordCount == 0 || productModel.getPrimaryKey() != null)
     {
       productModel = this.productDAO.saveOrUpdate( (
           ProductModel) baseWrapper.getBasePersistableModel());
       
	   	/*added by atif hussain*/
	   	StakeholderBankInfoModel stakeholderBankInfoModel;
	   	if(baseWrapper.getObject("STAKE_HOLDER_BANK_INFO_MODEL_CB")!=null)
	    {
	   		stakeholderBankInfoModel =	(StakeholderBankInfoModel) baseWrapper.getObject("STAKE_HOLDER_BANK_INFO_MODEL_CB");
	   	    stakeholderBankInfoDAO.saveOrUpdate(stakeholderBankInfoModel);
			
	   	    Long ofSettlementId=stakeholderBankInfoModel.getStakeholderBankInfoId();
	   	    
	   	    stakeholderBankInfoModel =	(StakeholderBankInfoModel) baseWrapper.getObject("STAKE_HOLDER_BANK_INFO_MODEL_BB");
	   		stakeholderBankInfoModel.setOfSettlementStakeholderBankInfoModelId(ofSettlementId);
	   		stakeholderBankInfoDAO.saveOrUpdate(stakeholderBankInfoModel);
	   	    
	   		if(baseWrapper.getObject("OLA_ACCOUNT_MODEL")!=null)
	   		{
	   	    	AccountModel accountModel = (AccountModel) baseWrapper.getObject("OLA_ACCOUNT_MODEL");
	   	    	accountDAO.saveOrUpdate(accountModel);
	   		}
	   	}
	   	/*added by atif hussain*/

       baseWrapper.setBasePersistableModel(productModel);
       
       actionLogModel.setCustomField1(productModel.getProductId().toString());
       actionLogModel.setCustomField11(productModel.getName());
       this.actionLogManager.completeActionLog(actionLogModel);
       return baseWrapper;

     }
     else
     {
       //set baseWrapper to null if record exists
       baseWrapper.setBasePersistableModel(null);
       return baseWrapper;
     }

//    ProductModel productModel = this.productDAO.saveOrUpdate( (ProductModel)
//        baseWrapper.getBasePersistableModel());
//
//    baseWrapper.setBasePersistableModel(productModel);
//    return baseWrapper;
  }

  
  //********************************************************************************************************************
  //********************************************************************************************************************
  
  public BaseWrapper addOrUpdateProduct(BaseWrapper baseWrapper) throws FrameworkCheckedException
  {

      ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
      ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

      Long actionId = (Long) baseWrapper.getObject(PortalConstants.KEY_ACTION_ID);

      ProductModelVO productModelVO = (ProductModelVO) baseWrapper.getBasePersistableModel();
      ProductModel productModel = this.getProductModelFromVO(productModelVO);



      StakeholderBankInfoModel sbiModel = stakeholderBankInfoManager.loadBLBStakeholderBankInfoModel(productModelVO.getAccountNo());    
      if(sbiModel == null) {
          throw new FrameworkCheckedException("Unable to find given Account for Third Party Inclusive Charges.");
      }

      productModel.setThirdPartyStakeholderBankInfoModel(sbiModel);

      if(null!=(productModel.getProductId())){
      	 ProductModel existingProductModel =new ProductModel(); 
           existingProductModel.setProductId(productModel.getProductId());
           SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
           searchBaseWrapper.setBasePersistableModel(existingProductModel);
           searchBaseWrapper = this.loadProduct(searchBaseWrapper); 
            
           existingProductModel=(ProductModel) searchBaseWrapper.getBasePersistableModel();
           CommonUtils.copyNotNullBeanProperties(productModel, existingProductModel);

           productModel = this.productDAO.saveOrUpdate(existingProductModel);
          
      }
      
      else
      	productModel = this.productDAO.saveOrUpdate(productModel);
     
      this.productDAO.setUpdatedProductNameInTxDetailMaster(productModel.getName(),productModel.getProductId());
	

      baseWrapper.setBasePersistableModel(productModel);
     
     /*Saving default charges
      * */
      CommissionRateDefaultModel commissionRateDefaultModel = new CommissionRateDefaultModel();
      commissionRateDefaultModel.setProductId(productModel.getProductId());

      if(actionId.equals(PortalConstants.ACTION_UPDATE)) {
          ReferenceDataWrapper commissionRateDefaultDataWrapper = new ReferenceDataWrapperImpl(commissionRateDefaultModel);
          commissionRateDefaultDataWrapper.setBasePersistableModel(commissionRateDefaultModel);
          try {
              referenceDataManager.getReferenceData(commissionRateDefaultDataWrapper);
              if(commissionRateDefaultDataWrapper.getReferenceDataList() != null && CollectionUtils.isNotEmpty(commissionRateDefaultDataWrapper.getReferenceDataList())) {
                  productModel.setProductIdCommissionRateDefaultModelList(commissionRateDefaultDataWrapper.getReferenceDataList());
                  commissionRateDefaultModel = (CommissionRateDefaultModel) commissionRateDefaultDataWrapper.getReferenceDataList().get(0);
              }
          } catch(Exception e) {
              e.printStackTrace();
          }
      }
    
      commissionRateDefaultModel.setInclusiveFixAmount(productModel.getInclusiveFixAmount());
      commissionRateDefaultModel.setInclusivePercentAmount(productModel.getInclusivePercentAmount());
      commissionRateDefaultModel.setExclusiveFixAmount(productModel.getExclusiveFixAmount());
      commissionRateDefaultModel.setExclusivePercentAmount(productModel.getExclusivePercentAmount());
      productModel.addProductIdCommissionRateDefaultModel(commissionRateDefaultModel);
      commissionRateDefaultModel.setUpdatedOn(new Date());
      commissionRateDefaultModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
      if(commissionRateDefaultModel.getCommissionRateDefaultId() == null) {
          commissionRateDefaultModel.setCreatedOn(new Date());
          commissionRateDefaultModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
      }

      this.saveUpdateCommissionRateDefault(commissionRateDefaultModel);

      //update CommissionShSharesDefaultModel for this product
      List<CommissionShSharesDefaultModel> commissions = null;
      try {
          commissions = (List<CommissionShSharesDefaultModel>) productModel.getProductIdCommissionShSharesDefaultModelList();
           //*************************************************************************************************************** 
          if(actionId.equals(PortalConstants.ACTION_CREATE)) {   
        	  Long productId = productModel.getProductId();
	        //apply FED if selected
	          for (CommissionShSharesDefaultModel commissionShSharesDefaultModel : commissions) {
	        	  commissionShSharesDefaultModel.setProductId(productId);
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
	            	  commissionShSharesDefaultModel.setIsFedApplicable(true);
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
	            	  commissionShSharesDefaultModel.setIsWhtApplicable(true);
	              }
	              commissions.add(commissionShSharesDefaultModel);
	          }
          
          }
    //********************************************************************************************************************************      

          
          
      } catch(Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
          throw new FrameworkCheckedException(e.getMessage());
      }

      
     /* //commissionShShareDefaultModel.getCommissionStakeholderIdCommissionStakeholderModel().getCommissionStakeholderId());
      if(actionId.equals(PortalConstants.ACTION_CREATE)) {
          Long productId = productModel.getProductId();
          //Long stakeholderId = productModel.getCommissionStakeHolderId();

          for(CommissionShSharesDefaultModel commissionShSharesDefaultModel : commissions) {
              commissionShSharesDefaultModel.setProductId(productId);
              //commissionShSharesDefaultModel.setCommissionStakeholderId(stakeholderId);
              
          }
      }*/
      

      this.saveUpdateCommissionShSharesDefaul(commissions);

      actionLogModel.setCustomField1(productModel.getProductId().toString());
      actionLogModel.setCustomField11(productModel.getName());
      this.actionLogManager.completeActionLog(actionLogModel);
      return baseWrapper;

  }
  
  
  
  
  @Override
  public ProductModel getProductModelFromVO(ProductModelVO productModelVO)
  {
      ProductModel productModel = new ProductModel();

      productModel.setProductId(productModelVO.getProductId());
      productModel.setName(productModelVO.getName());
      productModel.setDescription(productModelVO.getDescription());
      productModel.setActive(productModelVO.getActive());
      productModel.setTaxable(productModelVO.getTaxable());
      productModel.setUpdatedOn(productModelVO.getUpdatedOn());
      productModel.setCreatedOn(productModelVO.getCreatedOn());
      productModel.setVersionNo(productModelVO.getVersionNo());
      productModel.setProductCode(productModelVO.getProductCode());
      productModel.setMinLimit(productModelVO.getMinLimit());
      productModel.setMaxLimit(productModelVO.getMaxLimit());
      productModel.setInclChargesCheck(productModelVO.getInclChargesCheck());
      productModel.setConsumerLabel(productModelVO.getConsumerLabel());
      productModel.setConsumerInputType(productModelVO.getConsumerInputType());
      productModel.setAmtRequired(productModelVO.getAmtRequired());
      productModel.setMultiples(productModelVO.getMultiples());
      productModel.setAccountNo(productModelVO.getAccountNo());
      productModel.setAccountNick(productModelVO.getAccountNick());

      productModel.setExclusiveFixAmount(productModelVO.getExclusiveFixAmount());
      productModel.setExclusivePercentAmount(productModelVO.getExclusivePercentAmount());
      productModel.setInclusiveFixAmount(productModelVO.getInclusiveFixAmount());
      productModel.setInclusivePercentAmount(productModelVO.getInclusivePercentAmount());
      //********************************************************
      productModel.setFedShare(productModelVO.getFedShare());
      productModel.setWithHoldingShare(productModelVO.getWithHoldingShare());
      productModel.setIsFed(productModelVO.getIsFed());
      //*********************************************************************
      productModel.setSupplierId(productModelVO.getSupplierId());
      productModel.setServiceId(productModelVO.getServiceId());
      productModel.setCommissionStakeHolderId(productModelVO.getCommissionStakeholderId());
      productModel.setWhtConfigId(productModelVO.getwHTConfigId());
      productModel.setCreatedBy(productModelVO.getCreatedByID());
      productModel.setUpdatedBy(productModelVO.getUpdatedByID());


      productModel.setProductIntgModuleInfoId(productModelVO.getProductIntgModuleInfoId());
      productModel.setProductIntgVoId(productModelVO.getProductIntgVoId());
      productModel.setInstructionId(productModelVO.getInstructionId());
      productModel.setSuccessMessageId(productModelVO.getSuccessMessageId());
      productModel.setFailureMessageId(productModelVO.getFailureMessageId());
      productModel.setHelpLine(productModelVO.getHelpLine());
      productModel.setCostPrice(productModelVO.getCostPrice());
      productModel.setUnitPrice(productModelVO.getUnitPrice());
      productModel.setFixedDiscount(productModelVO.getFixedDiscount());
      productModel.setPercentDiscount(productModelVO.getPercentDiscount());
      productModel.setMinimumStockLevel(productModelVO.getMinimumStockLevel());
      productModel.setBatchMode(productModelVO.getBatchMode());
      productModel.setDoValidate(productModelVO.getDoValidate());
      productModel.setCategoryCode(productModelVO.getCategoryCode());
      productModel.setCategoryId(productModelVO.getCategoryId());
      productModel.setUspProductidCheck(productModelVO.getUspProductidCheck());
     // productModel.setAppUserTypeId(productModelVO.getAppUserTypeId());

      try {
          for(CommissionShSharesDefaultModelVO model : productModelVO.getProductIdCommissionShSharesDefaultModelList()) {
              CommissionShSharesDefaultModel newModel = new CommissionShSharesDefaultModel();
              newModel.setCommissionShSharesDefaultId(model.getCommissionShSharesDefaultId());
              newModel.setCommissionStakeholderId(model.getCommissionStakeholderIdPlain());
              newModel.setProductId(model.getProductIdPlain());
              newModel.setCommissionShare(model.getCommissionShare());
              newModel.setIsWhtApplicable(model.getIsWhtApplicable());

              productModel.addProductIdCommissionShShareDefaultModel(newModel);
          }
      } catch(Exception e) {
          e.printStackTrace();
      }

      return productModel;

  }
  
  
  
  
  
  //********************************************************************************************************************
  //********************************************************************************************************************
  
  
    @Override
	public List<ProductChargesRuleModel> searchProductChargesRules(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
        ProductChargesRuleModel model = (ProductChargesRuleModel) searchBaseWrapper.getBasePersistableModel();

        List parameterList = new ArrayList();
        parameterList.add(model.getProductId());
        parameterList.add(Boolean.FALSE);
	    StringBuilder findHQL = new StringBuilder();

	    findHQL.append("from ");
	    findHQL.append("ProductChargesRuleModel charges ");
        findHQL.append(" where charges.productModel.productId = ? ");
        findHQL.append(" and charges.isDeleted = ? ");

        if(UserUtils.getCurrentUser().getMnoId() != null && UserUtils.getCurrentUser().getMnoId().equals(50028L))
        {
            findHQL.append(" and charges.relationMnoModel.mnoId = ? ");
            parameterList.add(50028L);
        }
        else
        {
            findHQL.append(" and ( charges.relationMnoModel.mnoId is null or charges.relationMnoModel.mnoId = ?)");
            parameterList.add(50027L);
        }
        if(searchBaseWrapper.getSortingOrderMap() != null)
            findHQL.append(" order by charges.deviceTypeModel.deviceTypeId,charges.segmentModel.segmentId,charges.distributorModel.distributorId,rangeStarts ASC ");

        Object[] parameters = parameterList.toArray();
		List<ProductChargesRuleModel> list = productChargesRuleDao.loadProductChargesRulesByServiceOpId(findHQL.toString(),parameters);
		return list;
	}

    @Override
    public List<ProductThresholdChargesModel> searchProductThresholdChargesRules(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {

        List parameterList = new ArrayList();
//        parameterList.add(productThresholdChargesModel.getProductId());
        parameterList.add(Boolean.FALSE);

        StringBuilder findHQL = new StringBuilder();

        findHQL.append("from ");
        findHQL.append("ProductThresholdChargesModel charges ");
//        findHQL.append(" where charges.productModel.productId = ? ");
        findHQL.append(" where charges.isDeleted = ? ");

        if(searchBaseWrapper.getSortingOrderMap() != null)
            findHQL.append(" order by charges.deviceTypeModel.deviceTypeId,charges.segmentModel.segmentId,charges.distributorModel.distributorId,thresholdAmount ASC ");

        Object[] parameters = parameterList.toArray();
        List<ProductThresholdChargesModel> list = productThresholdChargesDAO.loadProductThresholdCharges(findHQL.toString(),parameters);
//        List<ProductThresholdChargesModel> list = productThresholdChargesDAO.loadProductThresholdCharges(productThresholdChargesModel);
        return list;
    }

    @SuppressWarnings("unchecked")
	@Override
	public void saveOrUpdateAllProductChargesRules(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

		ProductChargesRuleVo productChargesRuleVo = (ProductChargesRuleVo) baseWrapper.getObject(ProductChargesRuleVo.class.getSimpleName());

		List<ProductChargesRuleModel> existingProductChargesRuleModelList = (List<ProductChargesRuleModel>) baseWrapper.getObject("existingChargesRules");
		
		productChargesRuleDao.updateAndSaveProductChargesRules(existingProductChargesRuleModelList, productChargesRuleVo.getProductChargesRuleModelList());
		
		actionLogModel.setCustomField1(productChargesRuleVo.getProductId().toString());
		actionLogModel.setCustomField11(productChargesRuleVo.getProductName());
		this.actionLogManager.completeActionLogRequiresNewTransaction(actionLogModel);
	}

    @Override
    public void saveOrUpdateAllProductThresholdCharges(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
        ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

        ProductThresholdChargesVO productThresholdChargesVO = (ProductThresholdChargesVO) baseWrapper.getObject
                (ProductThresholdChargesVO.class.getSimpleName());

        List<ProductThresholdChargesModel> existingProductThresholdChargesModelList = (List<ProductThresholdChargesModel>) baseWrapper.getObject("existingThresholdChargesRules");

        productThresholdChargesDAO.updateAndSaveProductThresholdCharges(existingProductThresholdChargesModelList, productThresholdChargesVO.getProductThresholdChargesModelList());

        actionLogModel.setCustomField1(productThresholdChargesVO.getProductThresholdChargesModelList().get(0).getProductId().toString());
        actionLogModel.setCustomField11(productThresholdChargesVO.getProductThresholdChargesModelList().get(0).getProductModel().getName());
        this.actionLogManager.completeActionLogRequiresNewTransaction(actionLogModel);
    }

    public void setProductDAO(ProductDAO productDAO)
  {
    this.productDAO = productDAO;
  }

  public void setProductListViewDAO(ProductListViewDAO productListViewDAO)
  {
    this.productListViewDAO = productListViewDAO;
  }

  public void setProductUnitDAO(ProductUnitDAO productUnitDAO)
  {
    this.productUnitDAO = productUnitDAO;
  }

  public void setDiscreteProdUnitListViewDAO(DiscreteProdUnitListViewDAO
                                             discreteProdUnitListViewDAO)
  {
    this.discreteProdUnitListViewDAO = discreteProdUnitListViewDAO;
  }

  public void setShipmentDAO(ShipmentDAO shipmentDAO)
  {
    this.shipmentDAO = shipmentDAO;
  }

  public void setActionLogManager(ActionLogManager actionLogManager)
  {
		this.actionLogManager = actionLogManager;
  }

  public BaseWrapper removeProduct(BaseWrapper baseWrapper)
  {

    return null;
  }

  public WorkFlowWrapper sellDiscreteProduct(WorkFlowWrapper wrapper)throws FrameworkCheckedException
  {
    AuditLogModel failureLogModel = wrapper.getFailureLogModel();
    Long updatedBy = 0L ;

    ProductUnitModel productUnitModel = (ProductUnitModel) wrapper.getBasePersistableModel();
    DiscreteProdUnitListViewModel discreteProdUnitListViewModel = new DiscreteProdUnitListViewModel();
    discreteProdUnitListViewModel.setProductId( productUnitModel.getProductId() );
    SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();

    // Either Retailer or Customer can update the Product Unit
    if( wrapper.getAppUserModel() != null && wrapper.getAppUserModel().getAppUserId() > 0 )
      updatedBy = wrapper.getAppUserModel().getAppUserId() ;
    else
      updatedBy = wrapper.getFromRetailerContactAppUserModel().getAppUserId() ;

    searchBaseWrapper.setBasePersistableModel(discreteProdUnitListViewModel);
    productUnitModel.setSold( false ) ;
    searchBaseWrapper = discreteProdUnitListViewDAO.getUnsoldProductUnits(searchBaseWrapper);

    if(null != searchBaseWrapper.getBasePersistableModel())
    {
      discreteProdUnitListViewModel = (DiscreteProdUnitListViewModel)searchBaseWrapper.getBasePersistableModel();

      // ----------- Load product unit model ------------------------------------------------------------
      productUnitModel = (ProductUnitModel)this.productUnitDAO.findByPrimaryKeyAndLock( discreteProdUnitListViewModel.getProductUnitId()) ;
      productUnitModel.setUpdatedBy( updatedBy );
      productUnitModel.setUpdatedOn( new Date() );
      productUnitModel.setSold(true);
      productUnitModel = productUnitDAO.saveOrUpdate(productUnitModel);

      wrapper.setProductModel(productUnitModel.getProductIdProductModel());

      // -*--------------------- Update shipment record ----------------------------------------------------
      ShipmentModel shipmentModel = new ShipmentModel();
      shipmentModel.setPrimaryKey( productUnitModel.getShipmentId() );
      shipmentModel = this.shipmentDAO.findByPrimaryKey( shipmentModel.getPrimaryKey() ) ;
      shipmentModel.setOutstandingCredit( shipmentModel.getOutstandingCredit() - wrapper.getProductModel().getUnitPrice() );
      shipmentModel.setUpdatedBy( updatedBy );
      shipmentModel.setUpdatedOn( new Date() );
      productUnitModel.setShipmentIdShipmentModel( this.shipmentDAO.saveOrUpdate( shipmentModel ) );
      // ---------------------------------------------------------------------------------------------------

      wrapper.setBasePersistableModel(productUnitModel);
    }
    else
    {
     wrapper.setBasePersistableModel(null);

    }
    return wrapper;
  }

  
  public ProductLimitRuleViewModel getProductLimitRuleViewModel(ProductLimitRuleViewModel productLimitRuleViewModel) throws FrameworkCheckedException {
	  
	  log.info("\n\n@ Searching Product Limit Rule View with Parameters "+
			   "\n@ Product Id     = "+productLimitRuleViewModel.getProductId()+
			   "\n@ DeviceType Id  = "+productLimitRuleViewModel.getDeviceTypeId() +
			   "\n@ Segment Id     = "+productLimitRuleViewModel.getSegmentId()+
			   "\n@ Distributor Id = "+productLimitRuleViewModel.getDistributorId()+
	   		   "\n@ Handler Acc Type Id = "+productLimitRuleViewModel.getHandlerAccountTypeId());

	  if(productLimitRuleViewModel.getDeviceTypeId() == null) {
		  throw new FrameworkCheckedException("Missing Channel/Device Type for Product Limit.");
	  }
	  
	  if(productLimitRuleViewModel.getProductId() == null) {
		  throw new FrameworkCheckedException("Missing Product ID for Product Limit.");
	  }
	  
	  ProductLimitRuleViewModel _productLimitRuleViewModel = null;
	  
	  List<ProductLimitRuleViewModel> productLimitRuleViewList = productLimitDAO.getProductLimitRuleViewModel(productLimitRuleViewModel);
	  
	  if(productLimitRuleViewList != null  && !productLimitRuleViewList.isEmpty()) {
		  
		  if(productLimitRuleViewList.size() > 1) {
			  
			  throw new FrameworkCheckedException("Found "+productLimitRuleViewList.size()+" Entries Against Criteria.");
		  }
		  
		  _productLimitRuleViewModel = productLimitRuleViewList.get(0);
	  }
	  
	  return _productLimitRuleViewModel;
  }
  

  public WorkFlowWrapper sellVariableProduct(WorkFlowWrapper workFlowWrapper)throws FrameworkCheckedException
  {
    Long updatedBy = 0L ;
    List resultSetList ;

    // Either Retailer or Customer can update the Product Unit
    if( workFlowWrapper.getAppUserModel() != null && workFlowWrapper.getAppUserModel().getAppUserId() > 0 )
      updatedBy = workFlowWrapper.getAppUserModel().getAppUserId() ;
    else
      updatedBy = workFlowWrapper.getFromRetailerContactAppUserModel().getAppUserId() ;

    try
	{
		resultSetList = this.shipmentDAO.getVariableProdShipment( workFlowWrapper.getProductModel().getProductId(), workFlowWrapper.getTransactionAmount() );
	}
	catch (DataAccessException e)
	{		
		throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.GENERAL_ERROR_MSG);
	}

    if(null != resultSetList && resultSetList.size() > 0 )
    {
      // -*--------------------- Update shipment record ----------------------------------------------------
      ShipmentModel shipmentModel = new ShipmentModel();
      shipmentModel.setPrimaryKey( ((Long)resultSetList.get(0)));
      shipmentModel = this.shipmentDAO.findByPrimaryKey( shipmentModel.getPrimaryKey() ) ;
      shipmentModel.setOutstandingCredit( shipmentModel.getOutstandingCredit() - workFlowWrapper.getTransactionAmount() );
      shipmentModel.setUpdatedBy( updatedBy );
      shipmentModel.setUpdatedOn( new Date() );
      workFlowWrapper.setBasePersistableModel(this.shipmentDAO.saveOrUpdate( shipmentModel )) ;
      // ---------------------------------------------------------------------------------------------------
    }
    else
    {
     workFlowWrapper.setBasePersistableModel(null);
    }
    return workFlowWrapper;
  }

@Override
public List<ProductModel> loadProductsWithNoStakeholderShares()  throws FrameworkCheckedException{
	List<ProductModel> productModelList = null;


	productModelList = this.productDAO.loadProductsWithNoStakeholderShares();


	return productModelList;
}

		@Override
		public List<ProductModel> loadProductsByIds(String propertyToSortBy, SortingOrder sortingOrder, Long... productIds) throws FrameworkCheckedException
		{
			return productDAO.loadProductsByIds(propertyToSortBy, sortingOrder, productIds);
		}

	public void setProductLimitDAO(ProductLimitDAO productLimitDAO) {
		this.productLimitDAO = productLimitDAO;
	}

	public void setProductChargesRuleDao(ProductChargesRuleDao productChargesRuleDao) {
		this.productChargesRuleDao = productChargesRuleDao;
	}

    public void setProductThresholdChargesDAO(ProductThresholdChargesDAO productThresholdChargesDAO) {
        this.productThresholdChargesDAO = productThresholdChargesDAO;
    }

	@Override
	public void saveUpdateCommissionRateDefault ( CommissionRateDefaultModel commissionRateDefaultModel ) throws FrameworkCheckedException{
		productDAO.saveUpdateCommissionRateDefault(commissionRateDefaultModel);
	}
 
	@Override
	public void saveUpdateCommissionShSharesDefaul ( List <CommissionShSharesDefaultModel> commissionShSharesDefaultModels )  throws FrameworkCheckedException{
		Collection <CommissionShSharesDefaultModel> commissionShSharesDefaultCollection = new ArrayList<CommissionShSharesDefaultModel>();
		Collection <CommissionShSharesDefaultModel> commissionShSharesDefaultDeleteCollection = new ArrayList<CommissionShSharesDefaultModel>();
		for (CommissionShSharesDefaultModel commissionShSharesDefaultModel : commissionShSharesDefaultModels) {
			if ( commissionShSharesDefaultModel.getCommissionShare() != null  && commissionShSharesDefaultModel.getCommissionShare() != 0.0){
				if ( commissionShSharesDefaultModel.getCommissionShSharesDefaultId() != null ){
					Boolean isFed = commissionShSharesDefaultModel.getIsFedApplicable();
					Boolean isWH  = commissionShSharesDefaultModel.getIsWhtApplicable();
					Double shareValue = commissionShSharesDefaultModel.getCommissionShare();
					commissionShSharesDefaultModel = productDAO.getDefaultSharesRateById(commissionShSharesDefaultModel.getCommissionShSharesDefaultId());
					
					commissionShSharesDefaultModel.setIsFedApplicable(isFed);
					commissionShSharesDefaultModel.setIsWhtApplicable(isWH);
					commissionShSharesDefaultModel.setCommissionShare(shareValue);
				}
				commissionShSharesDefaultModel.setCreatedOn(new Date());
				commissionShSharesDefaultModel.setUpdatedOn(new Date());
				commissionShSharesDefaultModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
				commissionShSharesDefaultModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
				commissionShSharesDefaultModel.setIsWhtApplicable(commissionShSharesDefaultModel.getIsWhtApplicable() == null ? false : commissionShSharesDefaultModel.getIsWhtApplicable());
				commissionShSharesDefaultModel.setIsFedApplicable(commissionShSharesDefaultModel.getIsFedApplicable() == null ? false :commissionShSharesDefaultModel.getIsFedApplicable());
				commissionShSharesDefaultModel.setVersionNo(0);
				commissionShSharesDefaultCollection.add(commissionShSharesDefaultModel);
			}else if(commissionShSharesDefaultModel.getCommissionShSharesDefaultId() != null){
				Boolean isFed = commissionShSharesDefaultModel.getIsFedApplicable();
				Boolean isWH  = commissionShSharesDefaultModel.getIsWhtApplicable();
				Double shareValue = commissionShSharesDefaultModel.getCommissionShare();
				commissionShSharesDefaultModel = productDAO.getDefaultSharesRateById(commissionShSharesDefaultModel.getCommissionShSharesDefaultId());
				
				commissionShSharesDefaultModel.setIsFedApplicable(isFed);
				commissionShSharesDefaultModel.setIsWhtApplicable(isWH);
				commissionShSharesDefaultModel.setCommissionShare(shareValue);
				commissionShSharesDefaultModel.setCreatedOn(new Date());
				commissionShSharesDefaultModel.setUpdatedOn(new Date());
				commissionShSharesDefaultModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
				commissionShSharesDefaultModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
				commissionShSharesDefaultModel.setIsWhtApplicable(commissionShSharesDefaultModel.getIsWhtApplicable() == null ? false : commissionShSharesDefaultModel.getIsWhtApplicable());
				commissionShSharesDefaultModel.setIsFedApplicable(commissionShSharesDefaultModel.getIsFedApplicable() == null ? false :commissionShSharesDefaultModel.getIsFedApplicable());
				commissionShSharesDefaultModel.setVersionNo(0);
				if(shareValue != null && shareValue != 0.0){
					commissionShSharesDefaultCollection.add(commissionShSharesDefaultModel);
				}else{
					commissionShSharesDefaultDeleteCollection.add(commissionShSharesDefaultModel);
				}
			}
		}
		productDAO.saveUpdateCommissionShSharesDefault(commissionShSharesDefaultCollection);
		productDAO.deleteCommissionShSharesDefault(commissionShSharesDefaultDeleteCollection);
	}
	
	@Override
	public void saveUpdateCommissionShSharesRuleModels( List<CommissionShSharesRuleModel> commissionShSharesRuleModelList ) throws FrameworkCheckedException{
		
		boolean isFedAdded = false;
		boolean isWHAdded = false;
		Long	productId=null;
		Long	segmentId=null;
		Long	deviceTypeId=null;
		Long	distributorId=null;
		int versionNo=0;
		
		CommissionShSharesRuleModel fed = new CommissionShSharesRuleModel();
		CommissionShSharesRuleModel wh = new CommissionShSharesRuleModel();
		
		for(CommissionShSharesRuleModel model : commissionShSharesRuleModelList){
			
			/*productId = model.getProductId();
			segmentId = model.getSegmentId();
			deviceTypeId = model.getDeviceTypeId();
			distributorId = model.getDistributorId();
			versionNo = model.getVersionNo();*/
			
			if(model.getCommissionShare() == null || model.getCommissionShare() == 0.0){
				model.setCommissionShare(0.0);
				model.setIsFedApplicable(false);
				model.setIsWhtApplicable(false);
			}
			if(UserUtils.getCurrentUser().getAppUserTypeId().equals(UserTypeConstantsInterface.MNO))
			    model.setMnoId(50028L);
			/*if(model.getCommissionStakeholderId().longValue() == CommissionConstantsInterface.FED_STAKE_HOLDER_ID){
				if(!model.getIsDeleted() || model.getCommissionShare()==0.0){
					model.setIsDeleted(false);
					isFedAdded = true;
				}
			}
			if(model.getCommissionStakeholderId().longValue() == CommissionConstantsInterface.WHT_STAKE_HOLDER_ID && !model.getIsDeleted()){
				if(!model.getIsDeleted() || model.getCommissionShare()==0.0){
					model.setIsDeleted(false);
					isWHAdded = true;
				}
			}
			*/
			model.setIsFedApplicable(model.getIsFedApplicable()==null?false:model.getIsFedApplicable());
			model.setIsWhtApplicable(model.getIsWhtApplicable()==null?false:model.getIsWhtApplicable());
			if(model.getIsDeleted() == null || !model.getIsDeleted() ){
				model.setIsDeleted(false);
			}
			
			
		}
		
		/*if(!isFedAdded){
			
			fed.setProductId(productId);
			fed.setCommissionShare(0.0);
			fed.setCommissionStakeholderId(CommissionConstantsInterface.FED_STAKE_HOLDER_ID);
			fed.setSegmentId(segmentId);
			fed.setDistributorId(distributorId);
			fed.setDeviceTypeId(deviceTypeId);
			fed.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
			fed.setCreatedOn(new Date());
			fed.setUpdatedOn(new Date());
			fed.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
			fed.setIsDeleted(false);

		}
		if(!isWHAdded){
	
			wh.setProductId(productId);
			wh.setCommissionStakeholderId(CommissionConstantsInterface.WHT_STAKE_HOLDER_ID);
			wh.setCommissionShare(0.0);
			wh.setSegmentId(segmentId);
			wh.setDistributorId(distributorId);
			wh.setDeviceTypeId(deviceTypeId);
			wh.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
			wh.setCreatedOn(new Date());
			wh.setUpdatedOn(new Date());
			wh.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
			wh.setIsDeleted(false);
			
		}
		
		if(!isWHAdded){
			wh.setVersionNo(versionNo+1);
			commissionShSharesRuleModelList.add(wh);
		}
		if(!isFedAdded){
			fed.setVersionNo(versionNo+1);
			commissionShSharesRuleModelList.add(fed);
		}*/
		productDAO.saveUpdateCommissionShSharesRuleModel(commissionShSharesRuleModelList);
	}
	
	/**
	 * @author AtifHu
	 */
	@Override
	public BaseWrapper createOrUpdateProductLimitRule(
			BaseWrapper baseWrapper) throws FrameworkCheckedException {
		
		ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
		
		Collection<ProductLimitRuleModel>list	=	(Collection<ProductLimitRuleModel>) baseWrapper.getObject("ProductLimitRuleModelList");
		long productId=	(Long) baseWrapper.getObject("ProductId");
		String productName=	(String) baseWrapper.getObject("ProductName");
		
		try{
			//productLimitRuleDAO.deleteProductLimitRule(productId);
			productLimitRuleDAO.createOrUpdateProductLimitRule(list);
		}
		catch(FrameworkCheckedException exception)
		{
			exception.printStackTrace();
		}
		actionLogModel.setCustomField1(String.valueOf(productId));
		actionLogModel.setCustomField11(productName);
    	this.actionLogManager.completeActionLog(actionLogModel);
		
		return baseWrapper;
	}

	/**
	 * @author AtifHu
	 */
	@Override
	public List<ProductLimitRuleModel> loadProductLimitRule(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
        ProductLimitRuleModel model = (ProductLimitRuleModel) searchBaseWrapper.getBasePersistableModel();
        List parameterList = new ArrayList();
        parameterList.add(Boolean.TRUE);
        parameterList.add(model.getProductId());
	    StringBuilder findHQL = new StringBuilder();
        findHQL.append(" from ");
        findHQL.append("ProductLimitRuleModel rule ");
        findHQL.append(" where active = ? and rule.productIdModel.productId = ? ");
        if(UserUtils.getCurrentUser().getAppUserTypeId().equals(UserTypeConstantsInterface.MNO))
        {
            findHQL.append(" and rule.relationMnoModel.mnoId = ? ");
            parameterList.add(50028L);
        }
        else
        {
            findHQL.append(" and ( rule.relationMnoModel.mnoId is null or rule.relationMnoModel.mnoId = ?)");
            parameterList.add(50027L);
        }
        Object[] parameters = parameterList.toArray();

        List<ProductLimitRuleModel> list = productLimitRuleDAO.loadProductLimitRulesByServiceOpId(findHQL.toString(), parameters);
		return list;
	}
	
	
	/**
	 * @author AtifHu
	 */
	@Override
	public SearchBaseWrapper updateProductCatalogVersion(SearchBaseWrapper searchBaseWrapper)throws FrameworkCheckedException {
		ProductModel productModel = (ProductModel)searchBaseWrapper.getBasePersistableModel();
		ProductCatalogDetailModel productCatalogDetailModel = new ProductCatalogDetailModel();
		productCatalogDetailModel.setProductId(productModel.getProductId());
	
		CustomList<ProductCatalogDetailModel> list = productCatalogDetailDAO.findByExample(productCatalogDetailModel);
		
		if(list != null && list.getResultsetList().size() > 0){			
			List<ProductCatalogDetailModel> productCatalogDetailModels = list.getResultsetList();
			for (ProductCatalogDetailModel model : productCatalogDetailModels) {
				ProductCatalogModel productCatalogModel = model.getProductCatalogIdProductCatalogModel();

			    AppUserModel appUserModel = UserUtils.getCurrentUser();
			    Long updatedBy = null;
			    if(appUserModel != null){
			    	updatedBy = appUserModel.getAppUserId();			    	
			    	productCatalogModel.setUpdatedBy(updatedBy);
			    	productCatalogModel.setUpdatedOn(new Date());
			    	try {						
			    		productCatalogDAO.saveOrUpdate(productCatalogModel);
					} catch (Exception e) {
						e.printStackTrace();
					}
			    }				
			}
		}
	
		return searchBaseWrapper;
	}
	
    @Override
    public ProductModel loadProductByProductId(Long productId)
    {
        ProductModel productModel = productDAO.loadProductByProductId(productId);
        productModel.setSupplierIdSupplierModel((SupplierModel) productModel.getSupplierIdSupplierModel().clone());
        productModel.setServiceIdServiceModel((ServiceModel) productModel.getServiceIdServiceModel().clone());
        return productModel;
    }
    
    @Override
    public SearchBaseWrapper searchProductModels(SearchBaseWrapper searchBaseWrapper)
    {
        LinkedHashMap<String, SortingOrder> sortingOrderMap = new LinkedHashMap<String, SortingOrder>();
        if(sortingOrderMap.isEmpty()) {
            sortingOrderMap.put("name", SortingOrder.ASC);
        }
        CustomList<ProductModel> customList = this.productDAO.findByExample((ProductModel) searchBaseWrapper.getBasePersistableModel(), null, sortingOrderMap);
        searchBaseWrapper.setCustomList(customList);
        return searchBaseWrapper;
    }

	@Override
	public boolean isProductNameUnique(ProductModel productModel) throws FrameworkCheckedException{
		return productDAO.isProductNameUnique(productModel);
	} 
	
	
	@Override
	public void markProductShSharesRuleDeleted(List<CommissionShSharesRuleModel> deleteList) throws FrameworkCheckedException{
		productDAO.saveUpdateCommissionShSharesRuleModel(deleteList);
	}
	
	@Override
	public void deleteProductShSharesRule(Collection<CommissionShSharesRuleModel> commissionShSharesDefaultModels) throws FrameworkCheckedException{
		productDAO.deleteCommissionShSharesRules(commissionShSharesDefaultModels);
	}
	
	public void setProductLimitRuleDAO(ProductLimitRuleDAO productLimitRuleDAO) {
		this.productLimitRuleDAO = productLimitRuleDAO;
	}

	public void setStakeholderBankInfoDAO(
			StakeholderBankInfoDAO stakeholderBankInfoDAO) {
		this.stakeholderBankInfoDAO = stakeholderBankInfoDAO;
	}

	public ProductCatalogDAO getProductCatalogDAO() {
		return productCatalogDAO;
	}

	public void setProductCatalogDAO(ProductCatalogDAO productCatalogDAO) {
		this.productCatalogDAO = productCatalogDAO;
	}

	public ProductCatalogDetailDAO getProductCatalogDetailDAO() {
		return productCatalogDetailDAO;
	}

	public void setProductCatalogDetailDAO(
			ProductCatalogDetailDAO productCatalogDetailDAO) {
		this.productCatalogDetailDAO = productCatalogDetailDAO;
	}

	public void setAccountDAO(AccountDAO accountDAO) {
		this.accountDAO = accountDAO;
	}

	public void setAccountHolderDAO(AccountHolderDAO accountHolderDAO) {
		this.accountHolderDAO = accountHolderDAO;
	}

    @Override
    public List<ProductModel> loadProductListByService(Long[] serviceIdList) throws FrameworkCheckedException
    {
        return productDAO.loadProductListByService(serviceIdList);
    }

    @Override
    public List<LabelValueBean> getProductLabelsByReferencedClass(Class clazz, Long pk) throws Exception {
        return productDAO.getProductLabelsByReferencedClass(clazz, pk);
    }
//*******************************************************************************
	public void setStakeholderBankInfoManager(
			StakeholderBankInfoManager stakeholderBankInfoManager) {
		this.stakeholderBankInfoManager = stakeholderBankInfoManager;
	}
    
	public void setReferenceDataManager(ReferenceDataManager referenceDataManager){
	    this.referenceDataManager = referenceDataManager;
	}
    
    

}
