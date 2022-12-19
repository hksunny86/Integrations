package com.inov8.microbank.webapp.action.productmodule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.server.service.commissionstakeholdermodule.CommissionStakeholderManager;
import com.inov8.microbank.server.service.distributormodule.DistributorManager;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.CommissionShSharesRuleModel;
import com.inov8.microbank.common.model.CommissionStakeholderModel;
import com.inov8.microbank.common.model.DeviceTypeModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.devicemodule.DeviceTypeManager;
import com.inov8.microbank.server.service.integration.vo.ProductShSharesRuleVO;
import com.inov8.microbank.server.service.productmodule.ProductManager;

/**
 * @author 	Abu Turab
 * @date	20-08-2014
 * @description
 *
 */
public class ProductCustomizedShareRateFormController extends AdvanceFormController{

	private ProductManager 						productManager;
	private DeviceTypeManager 					deviceTypeManager;
	private ReferenceDataManager 				referenceDataManager;
	private DistributorManager distributorManager;
//	private Long 								productId;
//	private ProductShSharesRuleVO				productShSharesRuleVo = new ProductShSharesRuleVO();

	public ProductCustomizedShareRateFormController(){
		setCommandName("productShSharesRuleVo");
		setCommandClass(ProductShSharesRuleVO.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Object loadFormBackingObject(HttpServletRequest httpServletRequest)
			throws Exception {
		Long 								productId;
		ProductShSharesRuleVO				productShSharesRuleVo = new ProductShSharesRuleVO();
		ProductModel productModel = null;
		productId = ServletRequestUtils.getLongParameter(httpServletRequest, "productId");
		httpServletRequest.setAttribute("productId", productId);
		String actionName = ServletRequestUtils.getStringParameter(httpServletRequest, "actionName");
		Long deviceTypeId = null;
		if(ServletRequestUtils.getStringParameter(httpServletRequest, "deviceTypeId") != null && !"".equals(ServletRequestUtils.getStringParameter(httpServletRequest, "deviceTypeId"))){
			deviceTypeId = ServletRequestUtils.getLongParameter(httpServletRequest, "deviceTypeId");
		}
		Long segmentId = 	null;
		if(ServletRequestUtils.getStringParameter(httpServletRequest, "segmentId") != null && !"".equals(ServletRequestUtils.getStringParameter(httpServletRequest, "segmentId"))){
			segmentId = ServletRequestUtils.getLongParameter(httpServletRequest, "segmentId");
		}
		productId = 	ServletRequestUtils.getLongParameter(httpServletRequest, "productId");
		Long distributorId = null;
		if(ServletRequestUtils.getStringParameter(httpServletRequest, "distributorId") != null && !"".equals(ServletRequestUtils.getStringParameter(httpServletRequest, "distributorId"))){
			distributorId = ServletRequestUtils.getLongParameter(httpServletRequest, "distributorId");
		}

		if(actionName != null && "deleteRules".equalsIgnoreCase(actionName)){
			//delete the shares rule against the devicetype, segment and distributor for this product
			productShSharesRuleVo = new ProductShSharesRuleVO();
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			productShSharesRuleVo = new ProductShSharesRuleVO();
			productModel = new ProductModel();
			productModel.setPrimaryKey(productId);
			searchBaseWrapper.setBasePersistableModel(productModel);
			searchBaseWrapper = this.productManager.loadProduct(searchBaseWrapper);
			productModel=(ProductModel) searchBaseWrapper.getBasePersistableModel();

			productShSharesRuleVo.setProductName(productModel.getName());
			productShSharesRuleVo.setProductId(productModel.getProductId());

			List<CommissionShSharesRuleModel> listToDelete = new ArrayList<CommissionShSharesRuleModel>( );
			CommissionShSharesRuleModel commissionShSharesRuleModel = new CommissionShSharesRuleModel();

			if(segmentId == null && deviceTypeId == null && distributorId == null){
				super.saveMessage(httpServletRequest, "Nothing was selected to delete..");
				return productShSharesRuleVo;
			}

			commissionShSharesRuleModel.setProductId(productId);

			if(segmentId != null){
				commissionShSharesRuleModel.setSegmentId(segmentId);
			}
			if(deviceTypeId != null){
				commissionShSharesRuleModel.setDeviceTypeId(deviceTypeId);
			}
			if(distributorId != null){
				commissionShSharesRuleModel.setDistributorId(distributorId);
			}
			if(UserUtils.getCurrentUser().getAppUserTypeId().equals(UserTypeConstantsInterface.MNO))
				commissionShSharesRuleModel.setMnoId(50028L);
			else
				commissionShSharesRuleModel.setMnoId(50027L);

			ReferenceDataWrapper commissionShSharesRuleModelDataWrapper = new ReferenceDataWrapperImpl(commissionShSharesRuleModel);
			referenceDataManager.getReferenceData(commissionShSharesRuleModelDataWrapper);
			List<CommissionShSharesRuleModel> list = commissionShSharesRuleModelDataWrapper.getReferenceDataList();

			for (CommissionShSharesRuleModel deleteCommissionShSharesRuleModel : list) {
				if(!deleteCommissionShSharesRuleModel.getIsDeleted()){
					deleteCommissionShSharesRuleModel.setIsDeleted(true);
					if(UserUtils.getCurrentUser().getAppUserTypeId().equals(UserTypeConstantsInterface.MNO))
						deleteCommissionShSharesRuleModel.setMnoId(50028L);
					else
						deleteCommissionShSharesRuleModel.setMnoId(50027L);
					listToDelete.add(deleteCommissionShSharesRuleModel);
				}
			}

			commissionShSharesRuleModel.setIsDeleted(Boolean.TRUE);
			if(UserUtils.getCurrentUser().getAppUserTypeId().equals(UserTypeConstantsInterface.MNO))
				commissionShSharesRuleModel.setMnoId(50028L);
			else
				commissionShSharesRuleModel.setMnoId(50027L);
			commissionShSharesRuleModelDataWrapper = new ReferenceDataWrapperImpl(commissionShSharesRuleModel);
			referenceDataManager.getReferenceData(commissionShSharesRuleModelDataWrapper);
			Collection<CommissionShSharesRuleModel> hardDeleteList = new ArrayList<CommissionShSharesRuleModel>();
			hardDeleteList = commissionShSharesRuleModelDataWrapper.getReferenceDataList();

			if(CollectionUtils.isNotEmpty(hardDeleteList)){
				productManager.deleteProductShSharesRule(hardDeleteList);
			}
			productManager.markProductShSharesRuleDeleted(listToDelete);
			super.saveMessage(httpServletRequest, "Product Shares Rules are deleted successfully");
			return productShSharesRuleVo;
		}

		if (null != productId)
		{
			if (log.isDebugEnabled())
			{
				log.debug("id is not null....retrieving object from DB");
			}

			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			productShSharesRuleVo = new ProductShSharesRuleVO();
			productModel = new ProductModel();
			productModel.setPrimaryKey(productId);
			searchBaseWrapper.setBasePersistableModel(productModel);
			searchBaseWrapper = this.productManager.loadProduct(searchBaseWrapper);
			productModel=(ProductModel) searchBaseWrapper.getBasePersistableModel();

			productShSharesRuleVo.setProductName(productModel.getName());
			productShSharesRuleVo.setProductId(productModel.getProductId());

			//fetch CommissionShSharesRuleModel on basis of ProductID
/*		      CommissionShSharesRuleModel commissionShSharesRuleModel = new CommissionShSharesRuleModel();
		      commissionShSharesRuleModel.setProductId(productModel.getProductId());
		      commissionShSharesRuleModel.setIsDeleted(false);

		      ReferenceDataWrapper commissionShSharesRuleModelDataWrapper = new ReferenceDataWrapperImpl(commissionShSharesRuleModel);
		      referenceDataManager.getReferenceData(commissionShSharesRuleModelDataWrapper);
 */
			productShSharesRuleVo.setProductId(productId);

			productShSharesRuleVo.setProductName(productModel.getName());
			productShSharesRuleVo.setSupplierId(productModel.getSupplierId());
			productShSharesRuleVo.setSupplierName(productModel.getSupplierIdSupplierModel().getName());

		}else
		{
			if (log.isDebugEnabled())
			{
				log.debug("id is null....creating new instance of Model");
			}

			return new ProductShSharesRuleVO ();
		}

		if(actionName!= null && actionName.equalsIgnoreCase("EditRules")){ //Edit action being performed
			CommissionShSharesRuleModel ruleModel = new CommissionShSharesRuleModel();
			List<CommissionShSharesRuleModel> commissionShShares = new ArrayList<CommissionShSharesRuleModel>(0);
			ruleModel.setDeviceTypeId(deviceTypeId);
			ruleModel.setSegmentId(segmentId);
			ruleModel.setDistributorId(distributorId);
			ruleModel.setProductId(productId);
			ruleModel.setIsDeleted(false);
			ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(ruleModel);
			referenceDataManager.getReferenceData(referenceDataWrapper);
			List<CommissionShSharesRuleModel> commissionShSharesRuleModels = (List<CommissionShSharesRuleModel>)referenceDataWrapper.getReferenceDataList();
			if(commissionShSharesRuleModels != null && CollectionUtils.isNotEmpty(commissionShSharesRuleModels)){
				productShSharesRuleVo = new ProductShSharesRuleVO();
				productShSharesRuleVo.setSegmentId(segmentId);
				productShSharesRuleVo.setDistributorId(distributorId);
				productShSharesRuleVo.setDeviceTypeId(deviceTypeId);
				productShSharesRuleVo.setProductId(productId);
				productShSharesRuleVo.setProductName(productModel.getName());
				commissionShShares = this.filterCommissionShSharesRuleModelList(commissionShSharesRuleModels);
				commissionShShares = this.filterCommissionShSharesRuleModelListByCrietaria(commissionShShares, productShSharesRuleVo);
			}
			//now fetch FED and WH for this combination and set to productshsharerulevo
			for(CommissionShSharesRuleModel commShSharesRule : commissionShShares){
				if ( commShSharesRule.getCommissionStakeholderId().longValue() == CommissionConstantsInterface.FED_STAKE_HOLDER_ID
						&& ((productShSharesRuleVo.getSegmentId() == null && commShSharesRule.getSegmentId() == null ) || commShSharesRule.getSegmentId().equals(productShSharesRuleVo.getSegmentId()))
						&& ((productShSharesRuleVo.getDistributorId() == null && commShSharesRule.getDistributorId() == null ) || commShSharesRule.getDistributorId().equals(productShSharesRuleVo.getDistributorId()))
						&& ((productShSharesRuleVo.getDeviceTypeId() == null && commShSharesRule.getDeviceTypeId() == null ) || commShSharesRule.getDeviceTypeId().equals(productShSharesRuleVo.getDeviceTypeId()))){ //FED
					//productShSharesRuleVo.setFedApply(true);
					productShSharesRuleVo.setFedShare(commShSharesRule.getCommissionShare() == 0.0 ? null : commShSharesRule.getCommissionShare());
				}else if(commShSharesRule.getCommissionStakeholderId().longValue() == CommissionConstantsInterface.WHT_STAKE_HOLDER_ID
						&& ((productShSharesRuleVo.getSegmentId() == null && commShSharesRule.getSegmentId() == null ) || commShSharesRule.getSegmentId().equals(productShSharesRuleVo.getSegmentId()))
						&& ((productShSharesRuleVo.getDistributorId() == null && commShSharesRule.getDistributorId() == null ) || commShSharesRule.getDistributorId().equals(productShSharesRuleVo.getDistributorId()))
						&& ((productShSharesRuleVo.getDeviceTypeId() == null && commShSharesRule.getDeviceTypeId() == null ) || commShSharesRule.getDeviceTypeId().equals(productShSharesRuleVo.getDeviceTypeId()))){ //W.H.
					productShSharesRuleVo.setWhApply(true);
					productShSharesRuleVo.setWhShare(commShSharesRule.getCommissionShare() == 0.0 ? null : commShSharesRule.getCommissionShare());
				}else if(commShSharesRule.getIsFedApplicable()){
					productShSharesRuleVo.setFedApply(true);
				}

			}
			commissionShShares = this.excludeFEDWH(commissionShShares);
			productShSharesRuleVo.setCommissionShSharesRuleModel(commissionShShares);
			productShSharesRuleVo.setIsEdit(true);
		}

		productShSharesRuleVo.setOldDeviceTypeId(deviceTypeId);
		productShSharesRuleVo.setOldDistributorId(distributorId);
		productShSharesRuleVo.setOldSegmentId(segmentId);
		HttpSession session = httpServletRequest.getSession();
		session.setAttribute("productShSharesRuleVo", productShSharesRuleVo);

		return productShSharesRuleVo;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Map<String,Object> loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
		Map<String, Object> referenceDataMap = new HashMap<String, Object>(3);
		List<CommissionStakeholderModel> 	commStakeholderModelList 		= null;
		List<SegmentModel>			   		segmentModelList 				= null;
		List<DeviceTypeModel>				channelModelList				= null;
		List<CommissionShSharesRuleModel> 	commissionShSharesRuleModelList = new ArrayList<CommissionShSharesRuleModel>(0);
		Long productId = ServletRequestUtils.getLongParameter(httpServletRequest, "productId");

		SegmentModel	segmentModel	  = new SegmentModel();
		segmentModel.setIsActive(Boolean.TRUE);

		DeviceTypeModel	deviceTypeModel	  = new DeviceTypeModel();
		deviceTypeModel.setActive(Boolean.TRUE);

		ReferenceDataWrapper referenceDataWrapper;

		List<DistributorModel> distributorModelList = distributorManager.findAllDistributor();

		referenceDataWrapper = new ReferenceDataWrapperImpl(new CommissionStakeholderModel(), "name", SortingOrder.ASC);
		referenceDataManager.getReferenceData(referenceDataWrapper);
		commStakeholderModelList = referenceDataWrapper.getReferenceDataList();
		commStakeholderModelList = this.filterStakeholderModelList(commStakeholderModelList);

		referenceDataWrapper = new ReferenceDataWrapperImpl(segmentModel, "name", SortingOrder.ASC);
		referenceDataManager.getReferenceData(referenceDataWrapper);
		segmentModelList = referenceDataWrapper.getReferenceDataList();

		channelModelList = deviceTypeManager.searchDeviceTypes(DeviceTypeConstantsInterface.ALL_PAY,DeviceTypeConstantsInterface.BANKING_MIDDLEWARE,
				DeviceTypeConstantsInterface.ALLPAY_WEB,DeviceTypeConstantsInterface.USSD,DeviceTypeConstantsInterface.WEB_SERVICE);

		CommissionShSharesRuleModel commissionShSharesRuleModel = new CommissionShSharesRuleModel();
		commissionShSharesRuleModel.setProductId(productId);
		commissionShSharesRuleModel.setIsDeleted(Boolean.FALSE);
	    /*if(UserUtils.getCurrentUser().getAppUserTypeId().equals(UserTypeConstantsInterface.MNO))
	    	commissionShSharesRuleModel.setMnoId(50028L);
	    else
	    	commissionShSharesRuleModel.setMnoId(50027L);*/

		///commissionShSharesRuleModelList = commissionStakeholderManager.loadCommissionShSharesList(commissionShSharesRuleModel);
		referenceDataWrapper = new ReferenceDataWrapperImpl(commissionShSharesRuleModel);
		referenceDataManager.getReferenceData(referenceDataWrapper);
		commissionShSharesRuleModelList = this.filterCommissionShSharesRuleModelList(referenceDataWrapper.getReferenceDataList());

		referenceDataMap.put("commissionShSharesRuleModelList", commissionShSharesRuleModelList);
		referenceDataMap.put("commStakeholderModelList", commStakeholderModelList);
		referenceDataMap.put("segmentModelList", segmentModelList);
		referenceDataMap.put("channelModelList", channelModelList);
		referenceDataMap.put("distributorModelList", distributorModelList);

		return referenceDataMap;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest httpServletRequest,
									HttpServletResponse arg1, Object command, BindException bindException)
			throws Exception {
		return null;
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest httpServletRequest,
									HttpServletResponse httpServletResponse, Object command, BindException bindException)
			throws Exception {

		ReferenceDataWrapper referenceDataWrapper;

		ProductShSharesRuleVO productShSharesRuleVo = (ProductShSharesRuleVO) command;
		productShSharesRuleVo.setProductId(Long.parseLong(httpServletRequest.getParameter("productId")));

		HttpSession session = httpServletRequest.getSession();

		ProductShSharesRuleVO productShSharesRuleVoReq = (ProductShSharesRuleVO) session.getAttribute("productShSharesRuleVo");


		productShSharesRuleVo.setOldDeviceTypeId(productShSharesRuleVoReq.getOldDeviceTypeId());
		productShSharesRuleVo.setOldDistributorId(productShSharesRuleVoReq.getOldDistributorId());
		productShSharesRuleVo.setOldSegmentId(productShSharesRuleVoReq.getOldSegmentId());
		productShSharesRuleVo.setIsEdit(productShSharesRuleVoReq.getIsEdit());
		List<CommissionShSharesRuleModel> commissionShSharesRuleModelList = new ArrayList<CommissionShSharesRuleModel>(0);

		for(CommissionShSharesRuleModel ruleModel : productShSharesRuleVo.getCommissionShSharesRuleModel()){
			ruleModel.setDeviceTypeId(productShSharesRuleVo.getDeviceTypeId());
			ruleModel.setSegmentId(productShSharesRuleVo.getSegmentId());
			ruleModel.setDistributorId(productShSharesRuleVo.getDistributorId());
			ruleModel.setProductId(productShSharesRuleVo.getProductId());
			if (ruleModel.getCommissionShSharesRuleId() == null){
				ruleModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
				ruleModel.setCreatedOn(new Date());
				ruleModel.setUpdatedOn(new Date());
				ruleModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
				if (productShSharesRuleVo.getFedApply() !=null && productShSharesRuleVo.getFedApply() == true){//add FED
					ruleModel.setIsFedApplicable(true);
				}
				ruleModel.setVersionNo(0);
			}else{
				ruleModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
				ruleModel.setCreatedOn(new Date());
				ruleModel.setUpdatedOn(new Date());
				ruleModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
				//ruleModel.setVersionNo(0);
				if (productShSharesRuleVo.getFedApply() !=null && productShSharesRuleVo.getFedApply() == true){//add FED
					ruleModel.setIsFedApplicable(true);
				}
			}
			if(UserUtils.getCurrentUser().getAppUserTypeId().equals(UserTypeConstantsInterface.MNO))
				ruleModel.setMnoId(50028L);
			else
				ruleModel.setMnoId(50027L);

			commissionShSharesRuleModelList.add(ruleModel);
		}

		if(productShSharesRuleVo.getFedApply() != null && productShSharesRuleVo.getFedApply() == true){//fetch FED if found update otherwise add
			CommissionShSharesRuleModel fed = new CommissionShSharesRuleModel();
			fed.setProductId(productShSharesRuleVo.getProductId());
			fed.setCommissionStakeholderId(CommissionConstantsInterface.FED_STAKE_HOLDER_ID);
//				fed.setIsDeleted(false);
			if(productShSharesRuleVo.getIsEdit() != null && productShSharesRuleVo.getIsEdit()){
				fed.setDeviceTypeId(productShSharesRuleVo.getOldDeviceTypeId());
				fed.setSegmentId(productShSharesRuleVo.getOldSegmentId());
				fed.setDistributorId(productShSharesRuleVo.getOldDistributorId());
				referenceDataWrapper = new ReferenceDataWrapperImpl(fed);
				referenceDataManager.getReferenceData(referenceDataWrapper);
				if(referenceDataWrapper.getReferenceDataList()!=null && CollectionUtils.isNotEmpty(referenceDataWrapper.getReferenceDataList())){
					//fed = (CommissionShSharesRuleModel) referenceDataWrapper.getReferenceDataList().get(0);
					List<CommissionShSharesRuleModel> list = referenceDataWrapper.getReferenceDataList();
					for(CommissionShSharesRuleModel fed1 : list){
						if( ((productShSharesRuleVo.getOldSegmentId() == null && fed1.getSegmentId() == null) || (productShSharesRuleVo.getOldSegmentId() != null && fed1.getSegmentId() != null && productShSharesRuleVo.getOldSegmentId().equals(fed1.getSegmentId()))) &&
								((productShSharesRuleVo.getOldDistributorId() == null && fed1.getDistributorId() == null) || (productShSharesRuleVo.getOldDistributorId() != null && fed1.getDistributorId() != null && productShSharesRuleVo.getOldDistributorId().equals(fed1.getDistributorId()))) &&
								((productShSharesRuleVo.getOldDeviceTypeId() == null && fed1.getDeviceTypeId() == null) || (productShSharesRuleVo.getOldDeviceTypeId() != null && fed1.getDeviceTypeId() != null && productShSharesRuleVo.getOldDeviceTypeId().equals(fed1.getDeviceTypeId())))){
							fed = fed1;
							fed.setProductId(productShSharesRuleVo.getProductId());
							fed.setCommissionShare(productShSharesRuleVo.getFedShare());
							fed.setSegmentId(productShSharesRuleVo.getSegmentId());
							fed.setDistributorId(productShSharesRuleVo.getDistributorId());
							fed.setDeviceTypeId(productShSharesRuleVo.getDeviceTypeId());
							fed.setUpdatedOn(new Date());
							fed.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
							fed.setIsDeleted(false);
						}/*else{
							fed = new CommissionShSharesRuleModel();
							fed.setCommissionStakeholderId(CommissionConstantsInterface.FED_STAKE_HOLDER_ID);
							fed.setProductId(productShSharesRuleVo.getProductId());
							fed.setCommissionShare(productShSharesRuleVo.getFedShare());
							fed.setSegmentId(productShSharesRuleVo.getSegmentId());
							fed.setDistributorId(productShSharesRuleVo.getDistributorId());
							fed.setDeviceTypeId(productShSharesRuleVo.getDeviceTypeId());
							fed.setUpdatedOn(new Date());
							fed.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
							fed.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
							fed.setCreatedOn(new Date());
							fed.setIsDeleted(false);
							fed.setVersionNo(0);
						}*/
					}
				}else{//add new FED
					fed.setCommissionStakeholderId(CommissionConstantsInterface.FED_STAKE_HOLDER_ID);
					fed.setProductId(productShSharesRuleVo.getProductId());
					fed.setCommissionShare(productShSharesRuleVo.getFedShare());
					fed.setSegmentId(productShSharesRuleVo.getSegmentId());
					fed.setDistributorId(productShSharesRuleVo.getDistributorId());
					fed.setDeviceTypeId(productShSharesRuleVo.getDeviceTypeId());
					fed.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
					fed.setCreatedOn(new Date());
					fed.setUpdatedOn(new Date());
					fed.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
					fed.setVersionNo(0);
					fed.setIsDeleted(false);
				}
			}else{
				fed.setCommissionStakeholderId(CommissionConstantsInterface.FED_STAKE_HOLDER_ID);
				fed.setProductId(productShSharesRuleVo.getProductId());
				fed.setCommissionShare(productShSharesRuleVo.getFedShare());
				fed.setSegmentId(productShSharesRuleVo.getSegmentId());
				fed.setDistributorId(productShSharesRuleVo.getDistributorId());
				fed.setDeviceTypeId(productShSharesRuleVo.getDeviceTypeId());
				fed.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
				fed.setCreatedOn(new Date());
				fed.setUpdatedOn(new Date());
				fed.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
				fed.setVersionNo(0);
				fed.setIsDeleted(false);
			}

			if(UserUtils.getCurrentUser().getAppUserTypeId().equals(UserTypeConstantsInterface.MNO))
				fed.setMnoId(50028L);
			else
				fed.setMnoId(50027L);
			commissionShSharesRuleModelList.add(fed);
		}else{
			//set isdelete=true for existing FED; user has unchecked isFED apply
			CommissionShSharesRuleModel fed = new CommissionShSharesRuleModel();
			fed.setProductId(productShSharesRuleVo.getProductId());
			fed.setCommissionStakeholderId(CommissionConstantsInterface.FED_STAKE_HOLDER_ID);
			fed.setIsDeleted(false);
			fed.setDeviceTypeId(productShSharesRuleVo.getOldDeviceTypeId());
			fed.setSegmentId(productShSharesRuleVo.getOldSegmentId());
			fed.setDistributorId(productShSharesRuleVo.getOldDistributorId());
			referenceDataWrapper = new ReferenceDataWrapperImpl(fed);
			referenceDataManager.getReferenceData(referenceDataWrapper);
			if(referenceDataWrapper.getReferenceDataList()!=null && CollectionUtils.isNotEmpty(referenceDataWrapper.getReferenceDataList())){
				fed = (CommissionShSharesRuleModel) referenceDataWrapper.getReferenceDataList().get(0);

				if( ((productShSharesRuleVo.getOldSegmentId() == null && fed.getSegmentId() == null) || (productShSharesRuleVo.getOldSegmentId() != null && fed.getSegmentId() != null && productShSharesRuleVo.getOldSegmentId().equals(fed.getSegmentId()))) &&
						((productShSharesRuleVo.getOldDistributorId() == null && fed.getDistributorId() == null) || (productShSharesRuleVo.getOldDistributorId() != null && fed.getDistributorId() != null && productShSharesRuleVo.getOldDistributorId().equals(fed.getDistributorId()))) &&
						((productShSharesRuleVo.getOldDeviceTypeId()== null && fed.getDeviceTypeId() == null) || (productShSharesRuleVo.getOldDeviceTypeId() != null && fed.getDeviceTypeId() != null && productShSharesRuleVo.getOldDeviceTypeId().equals(fed.getDeviceTypeId())))){

					fed.setProductId(productShSharesRuleVo.getProductId());
					fed.setCommissionShare(productShSharesRuleVo.getFedShare());
					fed.setSegmentId(productShSharesRuleVo.getSegmentId());
					fed.setDistributorId(productShSharesRuleVo.getDistributorId());
					fed.setDeviceTypeId(productShSharesRuleVo.getDeviceTypeId());
					fed.setUpdatedOn(new Date());
					fed.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
					if(fed.getCommissionShare() != null && fed.getCommissionShare() > 0){
						fed.setIsDeleted(true);
					}
					if(UserUtils.getCurrentUser().getAppUserTypeId().equals(UserTypeConstantsInterface.MNO))
						fed.setMnoId(50028L);
					else
						fed.setMnoId(50027L);
					commissionShSharesRuleModelList.add(fed);
				}else{
					fed = new CommissionShSharesRuleModel();
					fed.setCommissionStakeholderId(CommissionConstantsInterface.FED_STAKE_HOLDER_ID);
					fed.setProductId(productShSharesRuleVo.getProductId());
					fed.setCommissionShare(0.0);
					fed.setSegmentId(productShSharesRuleVo.getSegmentId());
					fed.setDistributorId(productShSharesRuleVo.getDistributorId());
					fed.setDeviceTypeId(productShSharesRuleVo.getDeviceTypeId());
					fed.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
					fed.setCreatedOn(new Date());
					fed.setUpdatedOn(new Date());
					fed.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
					fed.setIsDeleted(false);
					fed.setVersionNo(0);
					if(UserUtils.getCurrentUser().getAppUserTypeId().equals(UserTypeConstantsInterface.MNO))
						fed.setMnoId(50028L);
					else
						fed.setMnoId(50027L);

					commissionShSharesRuleModelList.add(fed);
				}

			}else{ //this is new else part for fed with zero 11-28-2014
				fed = new CommissionShSharesRuleModel();
				fed.setCommissionStakeholderId(CommissionConstantsInterface.FED_STAKE_HOLDER_ID);
				fed.setProductId(productShSharesRuleVo.getProductId());
				fed.setCommissionShare(0.0);
				fed.setSegmentId(productShSharesRuleVo.getSegmentId());
				fed.setDistributorId(productShSharesRuleVo.getDistributorId());
				fed.setDeviceTypeId(productShSharesRuleVo.getDeviceTypeId());
				fed.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
				fed.setCreatedOn(new Date());
				fed.setUpdatedOn(new Date());
				fed.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
				fed.setIsDeleted(false);
				if(UserUtils.getCurrentUser().getAppUserTypeId().equals(UserTypeConstantsInterface.MNO))
					fed.setMnoId(50028L);
				else
					fed.setMnoId(50027L);

				commissionShSharesRuleModelList.add(fed);
			}
		}

		if(productShSharesRuleVo.getWhShare() != null && productShSharesRuleVo.getWhShare() > 0){//fetch W.H. if found update otherwise add
			CommissionShSharesRuleModel wh = new CommissionShSharesRuleModel();
			wh.setProductId(productShSharesRuleVo.getProductId());
			wh.setCommissionStakeholderId(CommissionConstantsInterface.WHT_STAKE_HOLDER_ID);
//				wh.setIsDeleted(false);
			if(productShSharesRuleVo.getIsEdit() != null && productShSharesRuleVo.getIsEdit()){
				wh.setDeviceTypeId(productShSharesRuleVo.getOldDeviceTypeId());
				wh.setSegmentId(productShSharesRuleVo.getOldSegmentId());
				wh.setDistributorId(productShSharesRuleVo.getOldDistributorId());
				referenceDataWrapper = new ReferenceDataWrapperImpl(wh);
				referenceDataManager.getReferenceData(referenceDataWrapper);
				if(referenceDataWrapper.getReferenceDataList()!=null && CollectionUtils.isNotEmpty(referenceDataWrapper.getReferenceDataList())){
					//wh = (CommissionShSharesRuleModel) referenceDataWrapper.getReferenceDataList().get(0);
					List<CommissionShSharesRuleModel> list = referenceDataWrapper.getReferenceDataList();
					for(CommissionShSharesRuleModel wh1 : list){
						if( ((productShSharesRuleVo.getOldSegmentId() == null && wh1.getSegmentId() == null) || (productShSharesRuleVo.getOldSegmentId() != null && wh1.getSegmentId() != null && productShSharesRuleVo.getOldSegmentId().equals(wh1.getSegmentId()))) &&
								((productShSharesRuleVo.getOldDistributorId() == null && wh1.getDistributorId() == null) || (productShSharesRuleVo.getOldDistributorId() != null && wh1.getDistributorId() != null && productShSharesRuleVo.getOldDistributorId().equals(wh1.getDistributorId()))) &&
								((productShSharesRuleVo.getOldDeviceTypeId()== null && wh1.getDeviceTypeId() == null) || (productShSharesRuleVo.getOldDeviceTypeId() != null && wh1.getDeviceTypeId() != null && productShSharesRuleVo.getOldDeviceTypeId().equals(wh1.getDeviceTypeId())))){
							wh = wh1;
							wh.setProductId(productShSharesRuleVo.getProductId());
							wh.setCommissionShare(productShSharesRuleVo.getWhShare());
							wh.setSegmentId(productShSharesRuleVo.getSegmentId());
							wh.setDistributorId(productShSharesRuleVo.getDistributorId());
							wh.setDeviceTypeId(productShSharesRuleVo.getDeviceTypeId());
							wh.setUpdatedOn(new Date());
							wh.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
							wh.setIsDeleted(false);
						}/*else{
							wh= new CommissionShSharesRuleModel();
							wh.setCommissionStakeholderId(CommissionConstantsInterface.WHT_STAKE_HOLDER_ID);
							wh.setProductId(productShSharesRuleVo.getProductId());
							wh.setCommissionShare(productShSharesRuleVo.getWhShare());
							wh.setSegmentId(productShSharesRuleVo.getSegmentId());
							wh.setDistributorId(productShSharesRuleVo.getDistributorId());
							wh.setDeviceTypeId(productShSharesRuleVo.getDeviceTypeId());
							wh.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
							wh.setCreatedOn(new Date());
							wh.setUpdatedOn(new Date());
							wh.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
							wh.setVersionNo(0);
							wh.setIsDeleted(false);
						}*/
					}
				}else{//add new WH
					wh.setCommissionStakeholderId(CommissionConstantsInterface.WHT_STAKE_HOLDER_ID);
					wh.setProductId(productShSharesRuleVo.getProductId());
					wh.setCommissionShare(productShSharesRuleVo.getWhShare());
					wh.setSegmentId(productShSharesRuleVo.getSegmentId());
					wh.setDistributorId(productShSharesRuleVo.getDistributorId());
					wh.setDeviceTypeId(productShSharesRuleVo.getDeviceTypeId());
					wh.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
					wh.setCreatedOn(new Date());
					wh.setUpdatedOn(new Date());
					wh.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
					wh.setVersionNo(0);
					wh.setIsDeleted(false);
				}

			}else{
				wh.setCommissionStakeholderId(CommissionConstantsInterface.WHT_STAKE_HOLDER_ID);
				wh.setProductId(productShSharesRuleVo.getProductId());
				wh.setCommissionShare(productShSharesRuleVo.getWhShare());
				wh.setSegmentId(productShSharesRuleVo.getSegmentId());
				wh.setDistributorId(productShSharesRuleVo.getDistributorId());
				wh.setDeviceTypeId(productShSharesRuleVo.getDeviceTypeId());
				wh.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
				wh.setCreatedOn(new Date());
				wh.setUpdatedOn(new Date());
				wh.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
				wh.setVersionNo(0);
				wh.setIsDeleted(false);
			}
			if(UserUtils.getCurrentUser().getAppUserTypeId().equals(UserTypeConstantsInterface.MNO))
				wh.setMnoId(50028L);
			else
				wh.setMnoId(50027L);

			commissionShSharesRuleModelList.add(wh);
		}else{ //set isdelete=true for existing wh; user has unchecked WH
			CommissionShSharesRuleModel wh = new CommissionShSharesRuleModel();
			wh.setProductId(productShSharesRuleVo.getProductId());
			wh.setCommissionStakeholderId(CommissionConstantsInterface.WHT_STAKE_HOLDER_ID);
			wh.setIsDeleted(false);
			wh.setDeviceTypeId(productShSharesRuleVo.getOldDeviceTypeId());
			wh.setSegmentId(productShSharesRuleVo.getOldSegmentId());
			wh.setDistributorId(productShSharesRuleVo.getOldDistributorId());
			referenceDataWrapper = new ReferenceDataWrapperImpl(wh);
			referenceDataManager.getReferenceData(referenceDataWrapper);
			if(referenceDataWrapper.getReferenceDataList()!=null && CollectionUtils.isNotEmpty(referenceDataWrapper.getReferenceDataList())){
				wh = (CommissionShSharesRuleModel) referenceDataWrapper.getReferenceDataList().get(0);
				if( ((productShSharesRuleVo.getOldSegmentId() == null && wh.getSegmentId() == null) || (productShSharesRuleVo.getOldSegmentId() != null && wh.getSegmentId() != null && productShSharesRuleVo.getOldSegmentId().equals(wh.getSegmentId()))) &&
						((productShSharesRuleVo.getOldDistributorId() == null && wh.getDistributorId() == null) || (productShSharesRuleVo.getOldDistributorId() != null && wh.getDistributorId() != null && productShSharesRuleVo.getOldDistributorId().equals(wh.getDistributorId()))) &&
						((productShSharesRuleVo.getOldDeviceTypeId()== null && wh.getDeviceTypeId() == null) || (productShSharesRuleVo.getOldDeviceTypeId() != null && wh.getDeviceTypeId() != null && productShSharesRuleVo.getOldDeviceTypeId().equals(wh.getDeviceTypeId())))){
					wh.setProductId(productShSharesRuleVo.getProductId());
					wh.setCommissionShare(productShSharesRuleVo.getWhShare());
					wh.setSegmentId(productShSharesRuleVo.getSegmentId());
					wh.setDistributorId(productShSharesRuleVo.getDistributorId());
					wh.setDeviceTypeId(productShSharesRuleVo.getDeviceTypeId());
					wh.setUpdatedOn(new Date());
					wh.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
					if(wh.getCommissionShare() != null && wh.getCommissionShare() > 0){
						wh.setIsDeleted(true);
					}
					if(UserUtils.getCurrentUser().getAppUserTypeId().equals(UserTypeConstantsInterface.MNO))
						wh.setMnoId(50028L);
					else
						wh.setMnoId(50027L);
					commissionShSharesRuleModelList.add(wh);
				}else{
					wh = new CommissionShSharesRuleModel();
					wh.setCommissionStakeholderId(CommissionConstantsInterface.WHT_STAKE_HOLDER_ID);
					wh.setProductId(productShSharesRuleVo.getProductId());
					wh.setCommissionShare(0.0);
					wh.setSegmentId(productShSharesRuleVo.getSegmentId());
					wh.setDistributorId(productShSharesRuleVo.getDistributorId());
					wh.setDeviceTypeId(productShSharesRuleVo.getDeviceTypeId());
					wh.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
					wh.setCreatedOn(new Date());
					wh.setUpdatedOn(new Date());
					wh.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
					wh.setIsDeleted(false);
					if(UserUtils.getCurrentUser().getAppUserTypeId().equals(UserTypeConstantsInterface.MNO))
						wh.setMnoId(50028L);
					else
						wh.setMnoId(50027L);

					commissionShSharesRuleModelList.add(wh);
				}
			}else{///turab 11-28-2014
				wh.setProductId(productShSharesRuleVo.getProductId());
				wh.setCommissionShare(0.0);
				wh.setSegmentId(productShSharesRuleVo.getSegmentId());
				wh.setDistributorId(productShSharesRuleVo.getDistributorId());
				wh.setDeviceTypeId(productShSharesRuleVo.getDeviceTypeId());
				wh.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
				wh.setCreatedOn(new Date());
				wh.setUpdatedOn(new Date());
				wh.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
				wh.setIsDeleted(false);
				if(UserUtils.getCurrentUser().getAppUserTypeId().equals(UserTypeConstantsInterface.MNO))
					wh.setMnoId(50028L);
				else
					wh.setMnoId(50027L);
				commissionShSharesRuleModelList.add(wh);
			}
		}
		Map<String,Object> modelMap = new HashMap<>(1);
		modelMap.put("productId", productShSharesRuleVo.getProductId());
		/*List<CommissionShSharesRuleModel> translatedList = this.isExist(commissionShSharesRuleModelList, productShSharesRuleVo);
		 */
		try{
			productManager.saveUpdateCommissionShSharesRuleModels(commissionShSharesRuleModelList);
			super.saveMessage(httpServletRequest, "Product Customized Share Rules are saved/updated successfully");
			session.removeAttribute("productShSharesRuleVo");
		}catch(Exception e){
			if(e.getCause() instanceof ConstraintViolationException){
				super.saveMessage(httpServletRequest, "You can not define two identical rules.");
				logger.error(e.getMessage(),e);
				return new ModelAndView("redirect:p_productcustomizeshare.html", modelMap);
			}else{
				super.saveMessage(httpServletRequest, "There were some error while saving rules.");
				logger.error(e.getMessage(),e);
				return new ModelAndView("redirect:p_productcustomizeshare.html", modelMap);
			}
		}

		return new ModelAndView("redirect:p_productcustomizeshare.html", modelMap);
	}

	public List<CommissionShSharesRuleModel> isExist(List<CommissionShSharesRuleModel> rulesList, ProductShSharesRuleVO productShSharesRuleVo){
		boolean result = false;
		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl();
		CommissionShSharesRuleModel commissionShSharesRuleModel = new CommissionShSharesRuleModel();
		Boolean isUpdate = false;
		CommissionShSharesRuleModel rule = rulesList.get(0);
		if(rule.getCommissionShSharesRuleId() != null){
			isUpdate = true;
		}
		commissionShSharesRuleModel.setDeviceTypeId(productShSharesRuleVo.getDeviceTypeId());
		commissionShSharesRuleModel.setSegmentId(productShSharesRuleVo.getSegmentId());
		commissionShSharesRuleModel.setDistributorId(productShSharesRuleVo.getDistributorId());
		commissionShSharesRuleModel.setProductId(productShSharesRuleVo.getProductId());
		commissionShSharesRuleModel.setIsDeleted(false);

		referenceDataWrapper.setBasePersistableModel(commissionShSharesRuleModel);
		//fetch existing share rules from DB against the given chanel, segment and device type
		try {
			referenceDataManager.getReferenceData(referenceDataWrapper);
		} catch (FrameworkCheckedException e) {
			logger.error(e.getMessage(),e);
		}

		List<CommissionShSharesRuleModel> existingCommissionShSharesRuleList = this.filterCommissionShSharesRuleModelList(referenceDataWrapper.getReferenceDataList());
		List<CommissionShSharesRuleModel> mergedList = populateMergedList(rulesList, existingCommissionShSharesRuleList);
		return mergedList;
	}

	private List<CommissionStakeholderModel> filterStakeholderModelList(List<CommissionStakeholderModel> commissionStakeholderModelList){
		List<CommissionStakeholderModel> filterredList = new ArrayList<CommissionStakeholderModel>(0);
		for (CommissionStakeholderModel commissionStakeholderModel : commissionStakeholderModelList) {
			if (commissionStakeholderModel.getCommissionStakeholderId().longValue() != CommissionConstantsInterface.WHT_STAKE_HOLDER_ID &&
					commissionStakeholderModel.getCommissionStakeholderId().longValue() != CommissionConstantsInterface.FED_STAKE_HOLDER_ID &&
					commissionStakeholderModel.getCommissionStakeholderId().longValue() != CommissionConstantsInterface.HIERARCHY_STAKE_HOLDER_ID &&
					commissionStakeholderModel.getCommissionStakeholderId().longValue() != CommissionConstantsInterface.HIERARCHY2_STAKE_HOLDER_ID
					&& (commissionStakeholderModel.getDisplayOnProductScreen() != null && commissionStakeholderModel.getDisplayOnProductScreen())){
				filterredList.add(commissionStakeholderModel);
			}
		}
		return filterredList;
	}

	private List<CommissionShSharesRuleModel> filterCommissionShSharesRuleModelList(List<CommissionShSharesRuleModel> commissionShSharesRuleModelList){
		List<CommissionShSharesRuleModel> filterredList = new ArrayList<CommissionShSharesRuleModel>();
		Long appUserTypeId = UserUtils.getCurrentUser().getAppUserTypeId();
		for(CommissionShSharesRuleModel commissionShSharesRuleModel : commissionShSharesRuleModelList){
			if(commissionShSharesRuleModel.getIsDeleted() == null || !commissionShSharesRuleModel.getIsDeleted()){
				if(appUserTypeId.equals(UserTypeConstantsInterface.MNO) && commissionShSharesRuleModel.getMnoModel() != null &&
						commissionShSharesRuleModel.getMnoModel().getMnoId().equals(50028L))
					filterredList.add(commissionShSharesRuleModel);
				else if(appUserTypeId.equals(UserTypeConstantsInterface.BANK))
				{
					if(commissionShSharesRuleModel.getMnoModel() == null || (commissionShSharesRuleModel.getMnoModel() != null && commissionShSharesRuleModel.getMnoModel().getMnoId().equals(50027L)))
						filterredList.add(commissionShSharesRuleModel);
				}
			}
		}

		return filterredList;
	}

	private List<CommissionShSharesRuleModel> filterCommissionShSharesRuleModelListByCrietaria(List<CommissionShSharesRuleModel> commissionShSharesRuleModelList, ProductShSharesRuleVO productShSharesRuleVo){
		List<CommissionShSharesRuleModel> filterredList = new ArrayList<CommissionShSharesRuleModel>();
		for(CommissionShSharesRuleModel commissionShSharesRuleModel : commissionShSharesRuleModelList){
			if(((productShSharesRuleVo.getSegmentId() == null && commissionShSharesRuleModel.getSegmentId() == null ) || commissionShSharesRuleModel.getSegmentId().equals(productShSharesRuleVo.getSegmentId()))
					&& ((productShSharesRuleVo.getDistributorId() == null && commissionShSharesRuleModel.getDistributorId() == null ) || commissionShSharesRuleModel.getDistributorId().equals(productShSharesRuleVo.getDistributorId()))
					&& ((productShSharesRuleVo.getDeviceTypeId() == null && commissionShSharesRuleModel.getDeviceTypeId() == null ) || commissionShSharesRuleModel.getDeviceTypeId().equals(productShSharesRuleVo.getDeviceTypeId()))){
				filterredList.add(commissionShSharesRuleModel);
			}
		}

		return filterredList;
	}

	private List<CommissionShSharesRuleModel> populateMergedList(List<CommissionShSharesRuleModel> rulesList, List<CommissionShSharesRuleModel> existingCommissionShSharesRuleList){
		List<CommissionShSharesRuleModel> mergedList = new ArrayList<CommissionShSharesRuleModel>( );
		List<CommissionShSharesRuleModel> channelMergedList = new ArrayList<CommissionShSharesRuleModel>( );
		List<CommissionShSharesRuleModel> segmentMergedList = new ArrayList<CommissionShSharesRuleModel>( );
		List<CommissionShSharesRuleModel> agentNetworkMergedList = new ArrayList<CommissionShSharesRuleModel>( );

		channelMergedList = translateAllChannels(rulesList);
		segmentMergedList = translateAllSegments(channelMergedList);
		agentNetworkMergedList = translateAllDistributor(segmentMergedList);
		mergedList.addAll(agentNetworkMergedList);
		return mergedList;
	}

	private List<CommissionShSharesRuleModel> translateAllChannels(List<CommissionShSharesRuleModel> rulesList){
		List<DeviceTypeModel>				channelModelList				= null;
		List<CommissionShSharesRuleModel> translateChannelList = new ArrayList<CommissionShSharesRuleModel>();
		try{
			channelModelList = deviceTypeManager.searchDeviceTypes(DeviceTypeConstantsInterface.ALL_PAY,DeviceTypeConstantsInterface.BANKING_MIDDLEWARE);
		}catch(Exception e){
			logger.error(e.getCause(),e);
		}
		for (CommissionShSharesRuleModel commissionShSharesRuleModel : rulesList) {
			if(commissionShSharesRuleModel.getDeviceTypeId() == null){
				for(DeviceTypeModel channel : channelModelList){
					CommissionShSharesRuleModel model = new CommissionShSharesRuleModel();
					model = commissionShSharesRuleModel;
					model.setDeviceTypeId(channel.getDeviceTypeId());
					translateChannelList.add(model);
				}
			}else{
				return rulesList;
			}
		}

		return 	translateChannelList;
	}

	private List<CommissionShSharesRuleModel> translateAllSegments(List<CommissionShSharesRuleModel> channelMergedList){
		List<SegmentModel>				segmentModelList				= null;
		CommissionShSharesRuleModel model;
		List<CommissionShSharesRuleModel> translateSegmentList = new ArrayList<CommissionShSharesRuleModel>();
		SegmentModel segmentModel = new SegmentModel();
		segmentModel.setIsActive(Boolean.TRUE);
		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(segmentModel, "name", SortingOrder.ASC);
		try{
			referenceDataManager.getReferenceData(referenceDataWrapper);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		segmentModelList = referenceDataWrapper.getReferenceDataList();
		for(SegmentModel segment : segmentModelList){
			for (CommissionShSharesRuleModel commissionShSharesRuleModel : channelMergedList) {
				if(commissionShSharesRuleModel.getSegmentId() == null){
					model = new CommissionShSharesRuleModel();
					model = commissionShSharesRuleModel;
					model.setSegmentId(segment.getSegmentId());
					translateSegmentList.add(model);
				}else{
					translateSegmentList.add(commissionShSharesRuleModel);
				}
			}
		}

		return translateSegmentList;
	}

	private List<CommissionShSharesRuleModel> translateAllDistributor(List<CommissionShSharesRuleModel> segmentMergedList){
		List<DistributorModel>				distributorModels				= null;
		List<CommissionShSharesRuleModel>	translateDistributorList		= new ArrayList<CommissionShSharesRuleModel>();
		DistributorModel distributorModel = new DistributorModel();
		distributorModel.setActive(Boolean.TRUE);
		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(distributorModel, "name", SortingOrder.ASC);
		try{
			referenceDataManager.getReferenceData(referenceDataWrapper);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		distributorModels = referenceDataWrapper.getReferenceDataList();
		for (CommissionShSharesRuleModel commissionShSharesRuleModel : segmentMergedList) {
			if(commissionShSharesRuleModel.getDistributorId() == null){
				for(DistributorModel distributor : distributorModels){
					CommissionShSharesRuleModel model = new CommissionShSharesRuleModel();
					model = commissionShSharesRuleModel;
					model.setDistributorId(distributor.getDistributorId());
					translateDistributorList.add(model);
				}
			}else{
				return segmentMergedList;
			}
		}

		return translateDistributorList;
	}

	private List<CommissionShSharesRuleModel> excludeFEDWH(List<CommissionShSharesRuleModel> commissionShShares){
		List<CommissionShSharesRuleModel> list = new ArrayList<CommissionShSharesRuleModel>();
		for(CommissionShSharesRuleModel model : commissionShShares){
			if(model.getCommissionStakeholderId().longValue() != CommissionConstantsInterface.FED_STAKE_HOLDER_ID &&
					model.getCommissionStakeholderId().longValue() != CommissionConstantsInterface.WHT_STAKE_HOLDER_ID){
				list.add(model);
			}
		}

		return list;
	}

	public void setProductManager(ProductManager productManager) {
		this.productManager = productManager;
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	/*public ProductShSharesRuleVO getProductShSharesRuleVo() {
		return productShSharesRuleVo;
	}

	public void setProductShSharesRuleVo(ProductShSharesRuleVO productShSharesRuleVo) {
		this.productShSharesRuleVo = productShSharesRuleVo;
	}*/

	public void setDeviceTypeManager(DeviceTypeManager deviceTypeManager) {
		this.deviceTypeManager = deviceTypeManager;
	}

	public void setDistributorManager(DistributorManager distributorManager) {
		this.distributorManager = distributorManager;
	}

}
