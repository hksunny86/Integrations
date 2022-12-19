package com.inov8.microbank.webapp.action.productmodule;

import com.atomikos.logging.Logger;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.*;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.DeviceTypeModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.ProductChargesRuleModel;
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.vo.product.ProductChargesRuleVo;
import com.inov8.microbank.server.facade.CommonFacade;
import com.inov8.microbank.server.facade.ProductFacade;
import com.inov8.microbank.server.service.devicemodule.DeviceTypeManager;

import com.inov8.microbank.server.service.distributormodule.DistributorManager;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Aug 19, 2013 11:0:00 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class ProductChargesRuleFormController extends AdvanceFormController
{
    @Autowired
    private CommonFacade commonFacade;

    @Autowired
    private ProductFacade productFacade;
    
    @Autowired
    private DeviceTypeManager deviceTypeManager;

    @Autowired
	private
	DistributorManager distributorManager;

    public ProductChargesRuleFormController()
    {
        setCommandName( "productChargesRuleVo" );
        setCommandClass( ProductChargesRuleVo.class );
    }

    @SuppressWarnings("unchecked")
	@Override
    protected Map<String,Object> loadReferenceData( HttpServletRequest request ) throws Exception
    {
    	Map<String,Object> refDataMap = new HashMap<>(3);
    	List<DeviceTypeModel> deviceTypeModelList = null;
    	List<SegmentModel> segmentModelList = null;
    	List<DistributorModel> distributorModelList = distributorManager.findAllDistributor();
    	ReferenceDataWrapper refDataWrapper = null;

    	deviceTypeModelList = deviceTypeManager.searchDeviceTypes(DeviceTypeConstantsInterface.ALL_PAY, DeviceTypeConstantsInterface.BANKING_MIDDLEWARE,
				DeviceTypeConstantsInterface.WEB_SERVICE,DeviceTypeConstantsInterface.ALLPAY_WEB,DeviceTypeConstantsInterface.USSD,DeviceTypeConstantsInterface.ATM);
		

    	SegmentModel segmentModel = new SegmentModel();
    	segmentModel.setIsActive(Boolean.TRUE);
    	refDataWrapper = new ReferenceDataWrapperImpl(segmentModel, "name", SortingOrder.ASC);
    	refDataWrapper = commonFacade.getReferenceData(refDataWrapper);
    	segmentModelList = refDataWrapper.getReferenceDataList();

    	refDataMap.put("deviceTypeModelList", deviceTypeModelList);
    	refDataMap.put("segmentModelList", segmentModelList);
    	refDataMap.put("distributorModelList", distributorModelList);
    	
        return refDataMap;
    }

    @Override
    protected ProductChargesRuleVo loadFormBackingObject( HttpServletRequest request ) throws Exception
    {
    	Long productId = ServletRequestUtils.getRequiredLongParameter(request, "productId");
    	String productName = ServletRequestUtils.getRequiredStringParameter(request, "productName");

    	ProductChargesRuleVo productChargesRuleVo = new ProductChargesRuleVo(productId, productName);

    	ProductChargesRuleModel productChargesRuleModel = new ProductChargesRuleModel();
    	productChargesRuleModel.setProductId(productId);
    	//productChargesRuleModel.setIsDeleted(false);

		LinkedHashMap<String, SortingOrder> sortingOrderMap = new LinkedHashMap<String, SortingOrder>();
		sortingOrderMap.put("deviceTypeModel.deviceTypeId", SortingOrder.ASC);
		sortingOrderMap.put("segmentModel.segmentId", SortingOrder.ASC);
		sortingOrderMap.put("distributorModel.distributorId", SortingOrder.ASC);
		sortingOrderMap.put("rangeStarts", SortingOrder.ASC);
		
		
    	SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
    	searchBaseWrapper.setBasePersistableModel(productChargesRuleModel);
    	List<ProductChargesRuleModel> list = productFacade.searchProductChargesRules(searchBaseWrapper);
    	if( !list.isEmpty())
    	{
    		List<ProductChargesRuleModel> productChargesRuleModelList =  this.filterCommissionShSharesRuleModelList(list);
    		if( CollectionUtils.isNotEmpty(productChargesRuleModelList) )
    		{
    			productChargesRuleVo.setProductChargesRuleModelList(productChargesRuleModelList);
    		}
    	}

    	//Add 1 object to make sure that table on screen has atleast one row
    	if( CollectionUtils.isEmpty(productChargesRuleVo.getProductChargesRuleModelList()) )
		{
			productChargesRuleVo.addProductChargesRuleModel(new ProductChargesRuleModel(productId));
		}
        return productChargesRuleVo;
    }

    @Override
    protected ModelAndView onCreate( HttpServletRequest request, HttpServletResponse response,
                                     Object command, BindException errors ) throws Exception
    {
    	ProductChargesRuleVo productChargesRuleVo = (ProductChargesRuleVo) command;
    	Date now = new Date();
    	List<ProductChargesRuleModel> productChargesRuleModelList = productChargesRuleVo.getProductChargesRuleModelList();
    	for(ProductChargesRuleModel productChargesRuleModel : productChargesRuleModelList)
    	{
    		productChargesRuleModel.setCreatedOn(now);
    		productChargesRuleModel.setUpdatedOn(now);
    		productChargesRuleModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
			productChargesRuleModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			productChargesRuleModel.setProductId(productChargesRuleVo.getProductId());
			if(UserUtils.getCurrentUser().getAppUserTypeId().equals(UserTypeConstantsInterface.MNO))
				productChargesRuleModel.setMnoId(50028L);
			else
				productChargesRuleModel.setMnoId(50027L);
		}

    	ProductChargesRuleModel productChargesRuleModel = new ProductChargesRuleModel();
    	productChargesRuleModel.setProductId(productChargesRuleVo.getProductId());
    	try {
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			searchBaseWrapper.setBasePersistableModel(productChargesRuleModel);
			List<ProductChargesRuleModel> existingChargesRules = productFacade.searchProductChargesRules(searchBaseWrapper);

			//List<ProductChargesRuleModel> existingChargesRules = this.filterCommissionShSharesRuleModelList(referenceDataWrapper.getReferenceDataList());
			for (ProductChargesRuleModel model : existingChargesRules) {
				model.setIsDeleted(true);
				model.setUpdatedOn(new Date());
				model.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
			}
			//List<ProductChargesRuleModel> mergedList = existingChargesRules;
			//if(productChargesRuleVo.getProductChargesRuleModelList().size() != existingChargesRules.size()){
			/*for(ProductChargesRuleModel existingModel : existingChargesRules){
				boolean isExist = false;
				for(ProductChargesRuleModel model : productChargesRuleVo.getProductChargesRuleModelList()){
					if(model.getProductChargesRuleId()!=null && existingModel.getProductChargesRuleId().longValue() == model.getProductChargesRuleId().longValue()){
						isExist = true;
						break;
					}
				}
				if(!isExist){
					existingModel.setIsDeleted(true);
					mergedList.add(existingModel);
					isExist = false;
				}
			}*/
			/*mergedList.addAll(productChargesRuleVo.getProductChargesRuleModelList());
			Set<ProductChargesRuleModel> set= new HashSet<ProductChargesRuleModel>();
			set.addAll(mergedList);
			mergedList=new ArrayList<>();
			mergedList.addAll(set);
			productChargesRuleVo.setProductChargesRuleModelList(mergedList);*/
			//}
			for (ProductChargesRuleModel model : productChargesRuleModelList) {
				model.setProductChargesRuleId(null);
				model.setVersionNo(null);
			}

			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long( PortalConstants.PRODUCT_UPDATE_USECASE_ID ) );
			baseWrapper.putObject("existingChargesRules", (ArrayList<ProductChargesRuleModel>) existingChargesRules );
			baseWrapper.putObject(ProductChargesRuleVo.class.getSimpleName(), productChargesRuleVo);
			productFacade.saveOrUpdateAllProductChargesRules(baseWrapper);
		} catch (Exception e) {
			e.printStackTrace();
			this.saveMessage(request, MessageUtil.getMessage("6075"));
	        return super.showForm(request, response, errors);
		}
    	
    	this.saveMessage(request, "Product Charges Rules saved successfully.");
    	Map<String, Object> modelMap = new HashMap<>(2);
    	modelMap.put("productId", productChargesRuleVo.getProductId());
    	modelMap.put("productName", productChargesRuleVo.getProductName());
    	return new ModelAndView(getSuccessView(), modelMap );
    }

    @Override
    protected ModelAndView onUpdate( HttpServletRequest request, HttpServletResponse response,
                                     Object command, BindException errors ) throws Exception
    {
    	ProductChargesRuleVo productChargesRuleVo = (ProductChargesRuleVo) command;
    	//productChargesRuleVo.setProductChargesRuleModelList(null);
    	Date now = new Date();
    	List<ProductChargesRuleModel> productChargesRuleModelList = productChargesRuleVo.getProductChargesRuleModelList();

		Iterator<ProductChargesRuleModel> iterator	=	productChargesRuleModelList.iterator();
		while(iterator.hasNext()){
			ProductChargesRuleModel	 obj	=iterator.next();
			if(obj.getProductChargesRuleId()==null && obj.getIsDeleted()!=null && obj.getIsDeleted().booleanValue()==Boolean.TRUE){
				iterator.remove();
			}
		}

		for(ProductChargesRuleModel productChargesRuleModel : productChargesRuleModelList)
    	{
    		productChargesRuleModel.setCreatedOn(now);
    		productChargesRuleModel.setUpdatedOn(now);
    		productChargesRuleModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
			productChargesRuleModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			if(UserUtils.getCurrentUser().getAppUserTypeId().equals(UserTypeConstantsInterface.MNO))
				productChargesRuleModel.setMnoId(50028L);
			else
				productChargesRuleModel.setMnoId(50027L);
		}

    	BaseWrapper baseWrapper = new BaseWrapperImpl();
    	baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
    	baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long( PortalConstants.PRODUCT_UPDATE_USECASE_ID ) );
    	baseWrapper.putObject(ProductChargesRuleVo.class.getSimpleName(), productChargesRuleVo);
    	try 
    	{
			productFacade.saveOrUpdateAllProductChargesRules(baseWrapper);
		} catch (Exception e) 
		{
			e.printStackTrace();
			this.saveMessage(request, MessageUtil.getMessage("6075"));
	        return super.showForm(request, response, errors);
		}
    	this.saveMessage(request, "All Product Charges Rules removed successfully.");

    	Map<String, Object> modelMap = new HashMap<>(2);
    	modelMap.put("productId", productChargesRuleVo.getProductId());
    	modelMap.put("productName", productChargesRuleVo.getProductName());
    	return new ModelAndView(getSuccessView(), modelMap );
    }
    
    private List<ProductChargesRuleModel> filterCommissionShSharesRuleModelList(List<ProductChargesRuleModel> productChargesRuleModelList){
		List<ProductChargesRuleModel> filterredList = new ArrayList<ProductChargesRuleModel>();
		for(ProductChargesRuleModel productChargesRuleModel : productChargesRuleModelList){
			if(productChargesRuleModel.getIsDeleted() == null || !productChargesRuleModel.getIsDeleted()){
				filterredList.add(productChargesRuleModel);
			}
		}
		
		return filterredList;
	}

    public void setCommonFacade( CommonFacade commonFacade )
    {
        this.commonFacade = commonFacade;
    }

    public void setProductFacade(ProductFacade productFacade)
    {
		this.productFacade = productFacade;
	}

	public void setDeviceTypeManager(DeviceTypeManager deviceTypeManager) {
		this.deviceTypeManager = deviceTypeManager;
	}

	public void setDistributorManager(DistributorManager distributorManager) {
		this.distributorManager = distributorManager;
	}
}
