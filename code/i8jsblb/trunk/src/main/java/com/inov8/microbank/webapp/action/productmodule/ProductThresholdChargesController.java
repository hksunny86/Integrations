package com.inov8.microbank.webapp.action.productmodule;

import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.*;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.integration.common.model.LimitTypeModel;
import com.inov8.microbank.cardconfiguration.vo.ProductThresholdChargesVO;
import com.inov8.microbank.common.model.*;
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

public class ProductThresholdChargesController extends AdvanceFormController {

    @Autowired
    private CommonFacade commonFacade;

    @Autowired
    private ProductFacade productFacade;

    @Autowired
    private DeviceTypeManager deviceTypeManager;

    @Autowired
    private
    DistributorManager distributorManager;

    public ProductThresholdChargesController()
    {
        setCommandName( "productThresholdChargesVO" );
        setCommandClass( ProductThresholdChargesVO.class );
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        Map<String,Object> refDataMap = new HashMap<>(3);
        List<DeviceTypeModel> deviceTypeModelList = null;
        List<SegmentModel> segmentModelList = null;
        List<ProductModel> productModelList = null;
//        List<DistributorModel> distributorModelList = distributorManager.findAllDistributor();
        List<DistributorModel> distributorModelList =null;
        List<LimitTypeModel> limitTypeModelList = null;
        ReferenceDataWrapper refDataWrapper = null;

        deviceTypeModelList = deviceTypeManager.searchDeviceTypes(DeviceTypeConstantsInterface.ALL_PAY, DeviceTypeConstantsInterface.BANKING_MIDDLEWARE,
                DeviceTypeConstantsInterface.WEB_SERVICE,DeviceTypeConstantsInterface.ALLPAY_WEB,DeviceTypeConstantsInterface.USSD,DeviceTypeConstantsInterface.ATM);


        SegmentModel segmentModel = new SegmentModel();
        segmentModel.setIsActive(Boolean.TRUE);
        refDataWrapper = new ReferenceDataWrapperImpl(segmentModel, "name", SortingOrder.ASC);
        refDataWrapper = commonFacade.getReferenceData(refDataWrapper);
        segmentModelList = refDataWrapper.getReferenceDataList();

        ProductModel productModel = new ProductModel();
        refDataWrapper = new ReferenceDataWrapperImpl(productModel, "name", SortingOrder.ASC);
        refDataWrapper = commonFacade.getReferenceData(refDataWrapper);
        productModelList = refDataWrapper.getReferenceDataList();


        LimitTypeModel limitTypeModel = new LimitTypeModel();
        refDataWrapper = new ReferenceDataWrapperImpl( limitTypeModel, "name", SortingOrder.ASC );
        commonFacade.getReferenceData( refDataWrapper );
        limitTypeModelList = refDataWrapper.getReferenceDataList();

        DistributorModel distributorModel = new DistributorModel();
        refDataWrapper = new ReferenceDataWrapperImpl(distributorModel, "name", SortingOrder.ASC);
        refDataWrapper = commonFacade.getReferenceData(refDataWrapper);
        distributorModelList = refDataWrapper.getReferenceDataList();

        refDataMap.put("deviceTypeModelList", deviceTypeModelList);
        refDataMap.put("segmentModelList", segmentModelList);
        refDataMap.put("productModelList", productModelList);
        refDataMap.put("distributorModelList", distributorModelList);
        refDataMap.put("limitTypeModelList", limitTypeModelList);

        return refDataMap;
    }

    @Override
    protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws Exception {

//        Long productId = ServletRequestUtils.getRequiredLongParameter(httpServletRequest, "productId");

//        ProductThresholdChargesVO productThresholdChargesVO = new ProductThresholdChargesVO(productId);

        ProductThresholdChargesVO productThresholdChargesVO = new ProductThresholdChargesVO();
        ProductThresholdChargesModel productThresholdChargesModel = new ProductThresholdChargesModel();
        productThresholdChargesModel.setIsDeleted(false);

        LinkedHashMap<String, SortingOrder> sortingOrderMap = new LinkedHashMap<String, SortingOrder>();
        sortingOrderMap.put("deviceTypeModel.deviceTypeId", SortingOrder.ASC);
        sortingOrderMap.put("segmentModel.segmentId", SortingOrder.ASC);
        sortingOrderMap.put("distributorModel.distributorId", SortingOrder.ASC);
        sortingOrderMap.put("thresholdAmount", SortingOrder.ASC);

        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
        searchBaseWrapper.setBasePersistableModel(productThresholdChargesModel);
        List<ProductThresholdChargesModel> list = productFacade.searchProductThresholdChargesRules(searchBaseWrapper);
//        CustomList<CardFeeRuleModel> customList = searchBaseWrapper.getCustomList();


//        Long productId = ServletRequestUtils.getRequiredLongParameter(httpServletRequest, "productId");
//        String productName = ServletRequestUtils.getRequiredStringParameter(httpServletRequest, "productName");

//        ProductThresholdChargesVo productThresholdChargesVo = new ProductThresholdChargesVo(productId, productName);
//
//        ProductThresholdChargesModel productThresholdChargesModel = new ProductThresholdChargesModel();
//        productThresholdChargesModel.setProductId(productId);
        //productChargesRuleModel.setIsDeleted(false);

        if( !list.isEmpty())
        {
            List<ProductThresholdChargesModel> productThresholdChargesModelList =  this.filterCommissionShSharesRuleModelList(list);
            if( CollectionUtils.isNotEmpty(productThresholdChargesModelList) )
            {
                productThresholdChargesVO.setProductThresholdChargesModelList(productThresholdChargesModelList);
            }
        }

        //Add 1 object to make sure that table on screen has atleast one row
        if( CollectionUtils.isEmpty(productThresholdChargesVO.getProductThresholdChargesModelList()) )
        {
            productThresholdChargesVO.addProductThresholdChargesModel(new ProductThresholdChargesModel());
            productThresholdChargesVO.setProductId(productThresholdChargesModel.getProductId());
        }
        return productThresholdChargesVO;
    }

    @Override
    protected ModelAndView onCreate( HttpServletRequest request, HttpServletResponse response,
                                     Object command, BindException errors ) throws Exception {
        ProductThresholdChargesVO productThresholdChargesVO = (ProductThresholdChargesVO) command;
        Date now = new Date();
        List<ProductThresholdChargesModel> productThresholdChargesModelList = productThresholdChargesVO.getProductThresholdChargesModelList();
        for(ProductThresholdChargesModel productThresholdChargesModel : productThresholdChargesModelList)
        {
            productThresholdChargesModel.setCreatedOn(now);
            productThresholdChargesModel.setUpdatedOn(now);
            productThresholdChargesModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
            productThresholdChargesModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
            productThresholdChargesModel.setProductId(productThresholdChargesModel.getProductId());
        }

        ProductThresholdChargesModel productThresholdChargesModel = new ProductThresholdChargesModel();
//        productThresholdChargesModel.setProductId(productThresholdChargesVO.getProductId());
        try {
            SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
            searchBaseWrapper.setBasePersistableModel(productThresholdChargesModel);
            List<ProductThresholdChargesModel> existingThresholdChargesRules = productFacade.searchProductThresholdChargesRules(searchBaseWrapper);

            //List<ProductChargesRuleModel> existingChargesRules = this.filterCommissionShSharesRuleModelList(referenceDataWrapper.getReferenceDataList());
             for (ProductThresholdChargesModel model : existingThresholdChargesRules) {
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
            for (ProductThresholdChargesModel model : productThresholdChargesModelList) {
                model.setProductThresholdChargesId(null);
                model.setVersionNo(null);
            }

            BaseWrapper baseWrapper = new BaseWrapperImpl();
            baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
            baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long( PortalConstants.PRODUCT_UPDATE_USECASE_ID ) );
            baseWrapper.putObject("existingThresholdChargesRules", (ArrayList<ProductThresholdChargesModel>) existingThresholdChargesRules );
            baseWrapper.putObject(ProductThresholdChargesVO.class.getSimpleName(), productThresholdChargesVO);
            productFacade.saveOrUpdateAllProductThresholdCharges(baseWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            this.saveMessage(request, MessageUtil.getMessage("6075"));
            return super.showForm(request, response, errors);
        }
        this.saveMessage(request, "Product Threshold Charges Rules saved successfully.");
        Map<String, Object> modelMap = new HashMap<>(2);
        modelMap.put("productId", productThresholdChargesVO.getProductId());
        modelMap.put("productName", productThresholdChargesVO.getProductName());
        return new ModelAndView(getSuccessView(), modelMap );
    }

    @Override
    protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response,
                                    Object command, BindException errors ) throws Exception {
        ProductThresholdChargesVO productThresholdChargesVO = (ProductThresholdChargesVO) command;
        //productChargesRuleVo.setProductChargesRuleModelList(null);
        Date now = new Date();
        List<ProductThresholdChargesModel> productThresholdChargesModelList = productThresholdChargesVO.getProductThresholdChargesModelList();

        Iterator<ProductThresholdChargesModel> iterator	=	productThresholdChargesModelList.iterator();
        while(iterator.hasNext()){
            ProductThresholdChargesModel	 obj	=iterator.next();
            if(obj.getProductThresholdChargesId()==null && obj.getIsDeleted()!=null && obj.getIsDeleted().booleanValue()==Boolean.TRUE){
                iterator.remove();
            }
        }

        for(ProductThresholdChargesModel productThresholdChargesModel : productThresholdChargesModelList)
        {
            productThresholdChargesModel.setCreatedOn(now);
            productThresholdChargesModel.setUpdatedOn(now);
            productThresholdChargesModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
            productThresholdChargesModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
        }

        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
        baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long( PortalConstants.PRODUCT_UPDATE_USECASE_ID ) );
        baseWrapper.putObject(ProductThresholdChargesVO.class.getSimpleName(), productThresholdChargesVO);
        try
        {
            productFacade.saveOrUpdateAllProductThresholdCharges(baseWrapper);
        } catch (Exception e)
        {
            e.printStackTrace();
            this.saveMessage(request, MessageUtil.getMessage("6075"));
            return super.showForm(request, response, errors);
        }
        this.saveMessage(request, "All Product Threshold Charges Rules removed successfully.");

        Map<String, Object> modelMap = new HashMap<>(2);
        modelMap.put("productId", productThresholdChargesVO.getProductId());
        modelMap.put("productName", productThresholdChargesVO.getProductName());
        return new ModelAndView(getSuccessView(), modelMap );
    }

    private List<ProductThresholdChargesModel> filterCommissionShSharesRuleModelList(List<ProductThresholdChargesModel> productThresholdChargesModelList){
        List<ProductThresholdChargesModel> filterredList = new ArrayList<ProductThresholdChargesModel>();
        for(ProductThresholdChargesModel productThresholdChargesModel : productThresholdChargesModelList){
            if(productThresholdChargesModel.getIsDeleted() == null || !productThresholdChargesModel.getIsDeleted()){
                filterredList.add(productThresholdChargesModel);
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
