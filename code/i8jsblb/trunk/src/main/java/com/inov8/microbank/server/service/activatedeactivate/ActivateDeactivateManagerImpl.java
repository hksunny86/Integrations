/**
 * 
 */
package com.inov8.microbank.server.service.activatedeactivate;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.AlertsConfigModel;
import com.inov8.microbank.common.model.AppUserPartnerGroupModel;
import com.inov8.microbank.common.model.CommissionRateModel;
import com.inov8.microbank.common.model.ConcernPartnerModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.PartnerGroupModel;
import com.inov8.microbank.common.model.ProductCatalogModel;
import com.inov8.microbank.common.model.RetailerModel;
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.common.model.StakeholderBankInfoModel;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.dao.appuserpartnergroupmodule.AppUserPartnerGroupDAO;
import com.inov8.microbank.server.dao.customermodule.CustomerDAO;
import com.inov8.microbank.server.dao.portal.concernmodule.ConcernPartnerDAO;
import com.inov8.microbank.server.dao.stakeholdermodule.StakeholderBankInfoDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.productmodule.ProductCatalogManager;
import com.inov8.ola.server.service.account.AccountManager;

/**
 * Project Name: 			Microbank	
 * @author Imran Sarwar
 * Creation Date: 			May 3, 2007
 * Creation Time: 			5:59:28 PM
 * Description:				
 */
public class ActivateDeactivateManagerImpl implements ActivateDeactivateManager
{
	protected GenericDao genericDao;
	private ConcernPartnerDAO concernPartnerDAO;
	private AppUserPartnerGroupDAO appUserPartnerGroupDAO;
	private StakeholderBankInfoDAO stakeholderBankInfoDAO;
	private ActionLogManager actionLogManager;
	private CustomerDAO customerDAO;
	private AccountManager accountManager;
	
	@Autowired
	private ProductCatalogManager	productCatalogManager;
	
	@SuppressWarnings("rawtypes")
	public BaseWrapper activateDeactivate(BaseWrapper  baseWrapper)
			throws FrameworkCheckedException
	{
		ActionLogModel actionLogModel = null;
		BasePersistableModel model = (BasePersistableModel)baseWrapper.getBasePersistableModel();
		final String propertyName = (String) baseWrapper.getObject(KEY_PROPERTY_NAME);
		final String modelName = (String)baseWrapper.getObject(ActivateDeactivateManager.KEY_MODEL_NAME);
		model = genericDao.getEntityByPrimaryKey(model);
		
		try
		{
//			Method getterMethod = model.getClass().getDeclaredMethod(
//						StringUtils.uncapitalize(StringUtils.substringAfter(propertyName,"get")), 
//					(Class[])null);
//			Boolean isActivate = (Boolean)getterMethod.invoke(model, (Object[]) null);
			
//			Method setterMethod = model.getClass().getDeclaredMethod(
//					StringUtils.uncapitalize(StringUtils.substringAfter(propertyName,"set")), 
//				(Class[])null);
			
			BeanUtilsBean beanUtils = BeanUtilsBean.getInstance();
			//ConvertUtils.register(new BooleanConverter(), Boolean.class);
			//ConvertUtils.register(UserUtils.getAppUserConverter(), AppUserModel.class);

			Boolean isActivate  = Boolean.valueOf(beanUtils.getProperty(model, propertyName));
			
			if(isActivate == false && "com.inov8.microbank.common.model.StakeholderBankInfoModel".equals(modelName)){
				String commissionStakeholderId = beanUtils.getProperty(model, "commissionStakeholderId");
				String cmshaccttypeId = beanUtils.getProperty(model, "cmshaccttypeId");
				
				StakeholderBankInfoModel mod = new StakeholderBankInfoModel();
				mod.setCommissionStakeholderId(Long.valueOf(commissionStakeholderId));
				mod.setCmshaccttypeId(Long.valueOf(cmshaccttypeId));
				mod.setActive(true);
				
				CustomList<StakeholderBankInfoModel> customList =  stakeholderBankInfoDAO.findByExample(mod);
				List<StakeholderBankInfoModel> list = customList.getResultsetList();
				if(list.size() != 0){
					throw new FrameworkCheckedException("Only 1 Account of a Stakeholder with same type can be Activated at a time. Please Deactivate all others to Activate this one.");
				}
			}
			
			beanUtils.setProperty(model, "updatedOn", new Date());
			beanUtils.setProperty(model, "updatedBy", UserUtils.getCurrentUser().getAppUserId());

			beanUtils.setProperty(model, propertyName, !isActivate);
			
			
			if("com.inov8.microbank.common.model.PartnerGroupModel".equals(modelName))
			{
				baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
				baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.USER_GROUP_ACT_DEACT_USECASE_ID);
				
				PartnerGroupModel partnerGroupModel = (PartnerGroupModel) model;
				
				actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
				actionLogModel.setCustomField11(partnerGroupModel.getName());
				ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

				AppUserPartnerGroupModel appUserPartnerGroupModel = new AppUserPartnerGroupModel();
				appUserPartnerGroupModel.setPartnerGroupId(model.getPrimaryKey());
				
				ExampleConfigHolderModel exampleConfigHolder = new ExampleConfigHolderModel();
				exampleConfigHolder.setEnableLike(false);
				
				Integer appUserPartnerGroupCount = appUserPartnerGroupDAO.countByExample(appUserPartnerGroupModel,exampleConfigHolder);
				
				if(appUserPartnerGroupCount == null || appUserPartnerGroupCount.intValue() > 0)
				{
					throw new FrameworkCheckedException("This group has some users associated to it so this group can not be Deactivated");
				}
			}
			else if("com.inov8.integration.common.model.OlaCustomerAccountTypeModel".equals(modelName)){
				
				baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
				baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.CUST_ACT_TYPE_CREATE_UPDATE_USECASE_ID);
				
				actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
				OlaCustomerAccountTypeModel olaCustomerAccountTypeModel = (OlaCustomerAccountTypeModel) model;
				actionLogModel.setCustomField11(olaCustomerAccountTypeModel.getName());
				ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

				String errorMsg = null;

				if(olaCustomerAccountTypeModel.getActive()){
		    		if(olaCustomerAccountTypeModel.getParentAccountTypeId() != null && !olaCustomerAccountTypeModel.getIsCustomerAccountType())
		    		{
		    			//Handler/Sub Account Type is to be de-activated.
		    			OlaCustomerAccountTypeModel parentAccountTypeModel = accountManager.findAccountTypeById(olaCustomerAccountTypeModel.getParentAccountTypeId());
		    			if( !parentAccountTypeModel.getActive() )
		    			{
		    				errorMsg = "This Account type can not be activated because its parent account type is inactive.";
		    			}
		    		}
		    	} else {
		    		boolean isAccociated = accountManager.isAssociatedWithAgentCustomerOrHandler(olaCustomerAccountTypeModel.getCustomerAccountTypeId(), olaCustomerAccountTypeModel.getParentAccountTypeId(), olaCustomerAccountTypeModel.getIsCustomerAccountType());
		        	if(isAccociated){
		        		errorMsg = "This Account type can not be deactivated because it has one or more customers/agents/handlers associated to it.";
		        	} else if(olaCustomerAccountTypeModel.getParentAccountTypeId() == null && !olaCustomerAccountTypeModel.getIsCustomerAccountType()) {
						boolean activeSubtypeExists = accountManager.hasActiveAccountSubtypes(olaCustomerAccountTypeModel.getCustomerAccountTypeId());
						if( activeSubtypeExists )
						{
							errorMsg = "This Account type can not be deactivated because it has one or more active sub account types.";
						}
		        	}
		    	}

				//end by turab
				if( errorMsg != null )
				{
					throw new FrameworkCheckedException(errorMsg);
				}
			}
			else if("com.inov8.microbank.common.model.SegmentModel".equals(modelName)){
				
				baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
				baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.CUSTOMER_SEGMENT_USECASE_ID);
				
				actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
				SegmentModel segmentModel = (SegmentModel) model;
				actionLogModel.setCustomField11(segmentModel.getName());
				ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
				
				CustomerModel customerModel = new CustomerModel();
				customerModel.setSegmentId(model.getPrimaryKey());
				
				
				ExampleConfigHolderModel exampleConfigHolder = new ExampleConfigHolderModel();
				exampleConfigHolder.setEnableLike(false);
				
				Integer customerCount = customerDAO.countByExample(customerModel, exampleConfigHolder);
				if(customerCount == null || customerCount.intValue() > 0)
				{
					throw new FrameworkCheckedException("This segment has some customers associated to it so this segment can not be Deactivated");
				}
			}
			else if( isActivate == false && "com.inov8.microbank.common.model.CommissionRateModel".equals(modelName)){
						
				
				CommissionRateModel commissionRateModel = (CommissionRateModel) model;					
				String hql = "from CommissionRateModel crm where crm.relationProductIdProductModel.productId="+commissionRateModel.getProductId()+" and crm.relationCommissionStakeholderIdCommissionStakeholderModel.commissionStakeholderId="+commissionRateModel.getCommissionStakeholderId()+" and crm.relationCommissionTypeIdCommissionTypeModel.commissionTypeId="+commissionRateModel.getCommissionTypeId()+"and crm.relationSegmentIdSegmentModel.segmentId ="+commissionRateModel.getSegmentId();
				StringBuilder validateRangeHql = new StringBuilder(hql+" and ("+commissionRateModel.getRangeStarts()+" between crm.rangeStarts and crm.rangeEnds or "+commissionRateModel.getRangeEnds()+" between crm.rangeStarts and crm.rangeEnds or crm.rangeStarts between "+commissionRateModel.getRangeStarts()+" and "+commissionRateModel.getRangeEnds()+" or crm.rangeEnds between "+commissionRateModel.getRangeStarts()+" and "+commissionRateModel.getRangeEnds()+") and crm.active=1 ");  
				
				if(commissionRateModel.getCommissionRateId()!=null){
					validateRangeHql.append(" and crm.commissionRateId != "+commissionRateModel.getCommissionRateId()); 
				}
				
				StringBuilder validateRatehql = new StringBuilder(hql+" and crm.rate="+ commissionRateModel.getRate()+" and crm.active=1 ");	
				if(commissionRateModel.getCommissionRateId()!=null){
					validateRatehql.append(" and crm.commissionRateId != "+commissionRateModel.getCommissionRateId()) ;
				}
				
				List list = genericDao.findByHQL(validateRatehql.toString());
				if(list != null && list.size() > 0){
					throw new FrameworkCheckedException("Record cannot be activated as slab rate overlaps with other record(s).");
				}else{	
					list = genericDao.findByHQL(validateRangeHql.toString());
					if(list != null && list.size() > 0){
						throw new FrameworkCheckedException("Record cannot be activated as slab range overlaps with other record(s).");
					}
				}
			}
			
			
			else if("com.inov8.microbank.common.model.RetailerModel".equals(modelName)){
				baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
				baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.RETAILER_ACT_DEACT_USECASE_ID);
				
				RetailerModel retailerModel = (RetailerModel) model;
				actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
				actionLogModel.setCustomField11(retailerModel.getName());
				ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
			}
			else if("com.inov8.microbank.common.model.CommissionRateModel".equals(modelName)){
				baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
				baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.TX_CHARGES_USECASE_ID);
				
				actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
				ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
			}else if ("com.inov8.microbank.common.model.AlertsConfigModel".equals(modelName)){
				baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
				baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.UPDATE_ALERT_CONFIG_USECASE_ID);
				
				AlertsConfigModel alertsConfigModel = (AlertsConfigModel) model;
				actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
				actionLogModel.setCustomField11(alertsConfigModel.getAlertName());
				ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
			}
			//added by atif hussain
			else if( isActivate == true && "com.inov8.microbank.common.model.ProductCatalogModel".equals(modelName)){
				ProductCatalogModel productCatalogModel = (ProductCatalogModel) model;
				Long productCatalogId = productCatalogModel.getProductCatalogId();
				
				ProductCatalogModel proCatModel = new ProductCatalogModel();
				baseWrapper = new BaseWrapperImpl();
				proCatModel.setProductCatalogId(productCatalogModel.getProductCatalogId());
				baseWrapper.setBasePersistableModel(proCatModel);
				baseWrapper = this.productCatalogManager.loadCatalog(baseWrapper);
				proCatModel = (ProductCatalogModel) baseWrapper.getBasePersistableModel();
				
				if((proCatModel.getActive()!=null && proCatModel.getActive().booleanValue()==true) &&  productCatalogModel.getActive()==null || !productCatalogModel.getActive())
				{
					if(productCatalogManager.isAssociatedWithAgentOrHandler(productCatalogId))
					{
						throw new FrameworkCheckedException("This product catalog is associated with agent(s)/handler(s), you cannot deactivate this.");
					}
				}
			}
			
			genericDao.updateEntity(model);
			baseWrapper.setBasePersistableModel(model);
			baseWrapper.putObject(KEY_RESPONSE, !isActivate);
			

			//Start incorporate concern_partner table changes as on 11092007
			Long concernPartnerTypeId = 0l;
			
			if("com.inov8.microbank.common.model.MnoModel".equals(modelName)){
				concernPartnerTypeId = 1l;
			}else if("com.inov8.microbank.common.model.BankModel".equals(modelName)){
					concernPartnerTypeId = 2l;
			}else if("com.inov8.microbank.common.model.SupplierModel".equals(modelName)){
				concernPartnerTypeId = 4l;
			} 
 
			if(concernPartnerTypeId != 0){
		          ConcernPartnerModel concernPartnerModel = new ConcernPartnerModel();
		          concernPartnerModel.setConcernPartnerTypeId(concernPartnerTypeId);
		          
		          if(concernPartnerTypeId.longValue() == 1){
		        	  //mno
		        	  concernPartnerModel.setMnoId(model.getPrimaryKey());		        	  
		          }else if(concernPartnerTypeId.longValue() == 2){
		        	  //bank
		        	  concernPartnerModel.setBankId(model.getPrimaryKey());		        	  
		          }else {
		        	  //supplier
		        	  concernPartnerModel.setSupplierId(model.getPrimaryKey());
		          }

		          
		          ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		          exampleHolder.setMatchMode(MatchMode.EXACT);
		          
		          CustomList<ConcernPartnerModel>concernCList = concernPartnerDAO.findByExample(concernPartnerModel,null,null,exampleHolder);
		          List <ConcernPartnerModel>list = concernCList.getResultsetList();

		          if(!list.isEmpty()){
		        	  concernPartnerModel = list.get(0);
		        	  concernPartnerModel.setActive(!isActivate);
		              concernPartnerModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
		              concernPartnerModel.setUpdatedOn(new Date());
		              concernPartnerDAO.saveOrUpdate(concernPartnerModel);
		          }
			}
			//End incorporate concern_partner table changes as on 11092007			
			
			if(actionLogModel != null && model.getPrimaryKey() != null){
				actionLogModel.setCustomField1(""+model.getPrimaryKey());
				this.actionLogManager.completeActionLog(actionLogModel);
			}
			
			return baseWrapper;
		}
		catch(InvocationTargetException iex)
		{
			throw new FrameworkCheckedException("InvocationTargetException =>", iex);
		}
		catch(NoSuchMethodException nex)
		{
			throw new FrameworkCheckedException("NoSuchMethodException =>", nex);
		}
		catch(IllegalAccessException ilex)
		{
			throw new FrameworkCheckedException("IllegalAccessException =>", ilex);
		}
		
	}

	/**
	 * @return the genericDao
	 */
	public GenericDao getGenericDao()
	{
		return genericDao;
	}

	/**
	 * @param genericDao the genericDao to set
	 */
	public void setGenericDao(GenericDao genericDao)
	{
		this.genericDao = genericDao;
	}

	public void setConcernPartnerDAO(ConcernPartnerDAO concernPartnerDAO) {
		this.concernPartnerDAO = concernPartnerDAO;
	}

	public void setAppUserPartnerGroupDAO(
			AppUserPartnerGroupDAO appUserPartnerGroupDAO) {
		this.appUserPartnerGroupDAO = appUserPartnerGroupDAO;
	}

	public void setStakeholderBankInfoDAO(
			StakeholderBankInfoDAO stakeholderBankInfoDAO) {
		this.stakeholderBankInfoDAO = stakeholderBankInfoDAO;
	}

	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}

	public void setCustomerDAO(CustomerDAO customerDAO)
	{
		this.customerDAO = customerDAO;
	}

	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}

	public void setProductCatalogManager(ProductCatalogManager productCatalogManager) {
		this.productCatalogManager = productCatalogManager;
	}

}
