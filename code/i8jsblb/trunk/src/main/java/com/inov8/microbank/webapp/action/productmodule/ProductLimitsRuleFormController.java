/**
 * @author AtifHu
 */

package com.inov8.microbank.webapp.action.productmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.*;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.vo.product.ProductLimitRuleVo;
import com.inov8.microbank.server.service.devicemodule.DeviceTypeManager;
import com.inov8.microbank.server.service.distributormodule.DistributorManager;
import com.inov8.microbank.server.service.productmodule.ProductCatalogManager;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.ola.server.facade.AccountFacade;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.*;

public class ProductLimitsRuleFormController extends
		AdvanceFormController {

	@Autowired
    private AccountFacade accountFacade;
	
	private ProductManager productManager;
	private ReferenceDataManager referenceDataManager;
	private ProductCatalogManager catalogManager;
	private DeviceTypeManager deviceTypeManager;
	private DistributorManager distributorManager;

	public ProductLimitsRuleFormController() {
		setCommandName("productLimitRuleVo");
		setCommandClass(ProductLimitRuleVo.class);
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest request)
			throws Exception {

		if(request.getParameter("productId")==null)
		{
			return new ProductLimitRuleVo ();
		}
		

		long productId = ServletRequestUtils.getRequiredLongParameter(request,"productId");

		ProductLimitRuleModel productLimitRuleModel = new ProductLimitRuleModel();
		productLimitRuleModel.setProductId(productId);
		productLimitRuleModel.setActive(true);

		SearchBaseWrapper searchBaseWrapper= new SearchBaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel(productLimitRuleModel);

		List<ProductLimitRuleModel> list = productManager.loadProductLimitRule(searchBaseWrapper);
		
		ProductLimitRuleVo limitRuleVo = new ProductLimitRuleVo();
		if(!list.isEmpty())
			limitRuleVo.setProductLimitRuleModelList(list);

		limitRuleVo.setProductId(productId);
		return limitRuleVo;
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest request)
			throws Exception {

		Map<String, Object> referenceDataMap = new HashMap<String, Object>(3);
		List<DeviceTypeModel> deviceTypeModelList = null;
		List<SegmentModel> segmentModelList = null;
		List<DistributorModel> distributorModelList = distributorManager.findAllDistributor();

		DistributorModel distributorModel = new DistributorModel();
		distributorModel.setActive(Boolean.TRUE);

		ProductModel productModel = new ProductModel();
		productModel.setActive(Boolean.TRUE);

		try {
				// loading device type list
			// DeviceTypeConstantsInterface.SCO_APP,
			
				deviceTypeModelList = deviceTypeManager.searchDeviceTypes(DeviceTypeConstantsInterface.ALL_PAY,
						DeviceTypeConstantsInterface.BANKING_MIDDLEWARE,DeviceTypeConstantsInterface.WEB_SERVICE,
						DeviceTypeConstantsInterface.ALLPAY_WEB,DeviceTypeConstantsInterface.USSD);

				// loading segment list
				ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
					new SegmentModel(), "name", SortingOrder.ASC);
				referenceDataManager.getReferenceData(referenceDataWrapper);
				segmentModelList = referenceDataWrapper.getReferenceDataList();

				// distributor list
				/*referenceDataWrapper = new ReferenceDataWrapperImpl(distributorModel, "name", SortingOrder.ASC);
				referenceDataManager.getReferenceData(referenceDataWrapper);
				distributorModelList = referenceDataWrapper.getReferenceDataList();*/


				List<OlaCustomerAccountTypeModel>	customerAccountTypeModellist=null;
				SearchBaseWrapper	searchBaseWrapper	=	accountFacade.getAllActiveCustomerAccountTypes();
				if(searchBaseWrapper.getCustomList()!=null && searchBaseWrapper.getCustomList().getResultsetList().size()>0)
				{
					customerAccountTypeModellist	=	searchBaseWrapper.getCustomList().getResultsetList();
					
					Iterator<OlaCustomerAccountTypeModel> itt	=	customerAccountTypeModellist.iterator();
					OlaCustomerAccountTypeModel model;
					while(itt.hasNext()){
						model=itt.next();
						if(model.getCustomerAccountTypeId().longValue()==CustomerAccountTypeConstants.WALK_IN_CUSTOMER
							|| model.getCustomerAccountTypeId().longValue()==CustomerAccountTypeConstants.SETTLEMENT)
						{
							itt.remove();
						}
					}

					customerAccountTypeModellist	=	sortAccountTypes(customerAccountTypeModellist);
				}
				
				referenceDataMap.put("accountTypeModelList",customerAccountTypeModellist);
				referenceDataMap.put("distributorModelList",distributorModelList);
				referenceDataMap.put("deviceTypeModelList", deviceTypeModelList);
				referenceDataMap.put("segmentModelList", segmentModelList);
			
		} catch (FrameworkCheckedException ex) {
			logger.error(ex.getMessage(), ex);
		}

		return referenceDataMap;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, BindException arg3)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object arg2,
			BindException errors) throws Exception {

		ProductLimitRuleVo productLimitRuleVo = (ProductLimitRuleVo) arg2;
		List<ProductLimitRuleModel> list = (List<ProductLimitRuleModel>) productLimitRuleVo
				.getProductLimitRuleModelList();

		if(productLimitRuleVo.getProductId()==null)
		{
			httpServletResponse.sendRedirect("productsearch.html");
			this.saveMessage(httpServletRequest, "Please select a product first");
			return super.showForm(httpServletRequest, httpServletResponse, errors);
		}
		
		String productName = httpServletRequest.getParameter("productName");
		
		Map<String,Object> modelMap = new HashMap<>(2);
		modelMap.put("productId", productLimitRuleVo.getProductId());
		modelMap.put("productName", productName);
		
		//Remove empty records
		Iterator<ProductLimitRuleModel> iterator=	list.iterator();
		
		ProductLimitRuleModel ittModel;

		while (iterator.hasNext()) {
			ittModel = iterator.next();
			if (!ittModel.getActive() && ittModel.getProductLimitRuleId() == null)
				iterator.remove();
		}
		if(list != null && list.isEmpty())
		{
			this.saveMessage(httpServletRequest,"No rule Defined.");
			return super.showForm(httpServletRequest, httpServletResponse, errors,modelMap);
		}
		//Check for duplicate records
		boolean recordMatched = false;
		for (int x = 0; x < list.size(); x++) {

			ProductLimitRuleModel parent = list.get(x);

			for (int y = 0; y < list.size(); y++) {
				ProductLimitRuleModel child = list.get(y);
				if (x != y && parent.equals(child)) {
					recordMatched = true;
					break;
				}
			}
			if (recordMatched)
			{
				break;
			}
		}
		if (recordMatched) {
			this.saveMessage(httpServletRequest,"Two or more rows are same, please review rules");
			return super.showForm(httpServletRequest, httpServletResponse, errors,modelMap);
		}
		if(!validateParentChildLimits(list)){
			this.saveMessage(httpServletRequest,"Account sub type limits should be within the range of parent account type limits.");
			return super.showForm(httpServletRequest, httpServletResponse, errors,modelMap);
		}
		else
		{
			for (ProductLimitRuleModel productLimitRuleModel : productLimitRuleVo
					.getProductLimitRuleModelList()) {

				productLimitRuleModel.setProductId(productLimitRuleVo.getProductId());
				productLimitRuleModel.setCreatedByAppUserModel(UserUtils
						.getCurrentUser());
				productLimitRuleModel.setCreatedOn(new Date());
				productLimitRuleModel.setUpdatedByAppUserModel(UserUtils
						.getCurrentUser());
				productLimitRuleModel.setUpdatedOn(new Date());

				if (productLimitRuleModel.getDeviceTypeId().longValue() == -1)
					productLimitRuleModel.setDeviceTypeId(null);

				if (productLimitRuleModel.getSegmentId().longValue() == -1)
					productLimitRuleModel.setSegmentId(null);

				if (productLimitRuleModel.getDistributorId().longValue() == -1)
					productLimitRuleModel.setDistributorId(null);
				if(UserUtils.getCurrentUser().getAppUserTypeId().equals(UserTypeConstantsInterface.MNO) )
					productLimitRuleModel.setMnoId(50028L);
				else
					productLimitRuleModel.setMnoId(50027L);
			}

			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.putObject(PortalConstants.KEY_ACTION_ID,PortalConstants.ACTION_UPDATE);
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID,PortalConstants.PRODUCT_UPDATE_USECASE_ID);
			
			baseWrapper.setBasePersistableModel(new ProductLimitRuleModel());
			baseWrapper.putObject("ProductLimitRuleModelList", (Serializable) productLimitRuleVo.getProductLimitRuleModelList());
			baseWrapper.putObject("ProductId", productLimitRuleVo.getProductId());
			baseWrapper.putObject("ProductName", productName);
			try 
			{
				productManager.createOrUpdateProductLimitRule(baseWrapper);
			} catch (Exception e) {
				e.printStackTrace();
				httpServletResponse.sendRedirect("productsearch.html");
				this.saveMessage(httpServletRequest, MessageUtil.getMessage("6075"));
				return super.showForm(httpServletRequest, httpServletResponse, errors);
			}
		}

		this.saveMessage(httpServletRequest, "Record saved successfully");
		return new ModelAndView("redirect:productlimitsruleform.html",modelMap);
	}
	
	private boolean validateParentChildLimits(List<ProductLimitRuleModel> list){
		
		SearchBaseWrapper	searchBaseWrapper=new SearchBaseWrapperImpl();
		searchBaseWrapper.putObject("PRODUCT-LIMIT-RULE-MODEL-LIST", (Serializable)list);
		try {
			searchBaseWrapper	=	accountFacade.getParentOlaCustomerAccountTypes(searchBaseWrapper);
		} catch (FrameworkCheckedException e) {
			e.printStackTrace();
		}
		
		Map<Long, Long> accountTypeMap=new HashMap<Long, Long>();
		
		if(searchBaseWrapper.getCustomList()!=null && searchBaseWrapper.getCustomList().getResultsetList().size()>0)
		{
			List<OlaCustomerAccountTypeModel>	accountTypeModelList=	searchBaseWrapper.getCustomList().getResultsetList();
			for (OlaCustomerAccountTypeModel accountTypeModel : accountTypeModelList) {
				accountTypeMap.put(accountTypeModel.getCustomerAccountTypeId(), accountTypeModel.getParentAccountTypeId());
			}
		}
		
		ProductLimitRuleModel outerProductLimitRuleModel;
		ProductLimitRuleModel innerProductLimitRuleModel;
		
		for (int x=0;x<list.size();x++) {
			
			outerProductLimitRuleModel	=	list.get(x);
			
			for (int y=0;y<list.size();y++) {
				
				innerProductLimitRuleModel	=	list.get(y);
				
				if(x==y)
				continue;
				
				if(outerProductLimitRuleModel.getAccountTypeId() !=null && innerProductLimitRuleModel.getAccountTypeModel()!=null && accountTypeMap.get(innerProductLimitRuleModel.getAccountTypeId())!=null)
				{
					if(outerProductLimitRuleModel.getAccountTypeId().longValue()==accountTypeMap.get(innerProductLimitRuleModel.getAccountTypeId()).longValue())
					{
						if(innerProductLimitRuleModel.getMinLimit() < outerProductLimitRuleModel.getMinLimit() || innerProductLimitRuleModel.getMaxLimit() > outerProductLimitRuleModel.getMaxLimit())
						{
							return false;
						}
					}
				}
			}
		}
		
		return true;
	}

	private List<OlaCustomerAccountTypeModel> sortAccountTypes(List<OlaCustomerAccountTypeModel>  list){
		
		List<OlaCustomerAccountTypeModel> parentList	=	new ArrayList<>();
		List<OlaCustomerAccountTypeModel> childList	=	new ArrayList<>();
		
		for (OlaCustomerAccountTypeModel model : list) {
			if(model.getParentAccountTypeId()==null){
				parentList.add(model);
			}
			else{
				childList.add(model);
			}
		}
		
		list=new ArrayList<OlaCustomerAccountTypeModel>();
		
		for (OlaCustomerAccountTypeModel parent : parentList) {
			
			list.add(parent);
			
			for (OlaCustomerAccountTypeModel child : childList) {
				
				if(child.getParentAccountTypeId()!=null && child.getParentAccountTypeId().longValue()==parent.getCustomerAccountTypeId().longValue()){
					child.setName("--- "+child.getName());
					list.add(child);
				}
			}
		}
		
		return list;
	}
	
	
	public ProductManager getProductManager() {
		return productManager;
	}

	public void setProductManager(ProductManager productManager) {
		this.productManager = productManager;
	}

	public ReferenceDataManager getReferenceDataManager() {
		return referenceDataManager;
	}

	public void setReferenceDataManager(
			ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public ProductCatalogManager getCatalogManager() {
		return catalogManager;
	}

	public void setCatalogManager(ProductCatalogManager catalogManager) {
		this.catalogManager = catalogManager;
	}

	public void setDeviceTypeManager(DeviceTypeManager deviceTypeManager) {
		this.deviceTypeManager = deviceTypeManager;
	}

	public void setAccountFacade(AccountFacade accountFacade) {
		this.accountFacade = accountFacade;
	}

	public void setDistributorManager(DistributorManager distributorManager) {
		this.distributorManager = distributorManager;
	}
}
