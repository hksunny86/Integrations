package com.inov8.microbank.webapp.action.commissionmodule;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.agenthierarchy.DistributorCommissionShareDetailModel;
import com.inov8.microbank.common.model.agenthierarchy.DistributorCommissionShareModel;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.agenthierarchy.AgentNetworkCommShareManager;
import com.inov8.microbank.server.service.integration.vo.DistributorCommShareVO;

public class DistributorCommShareFormController extends AdvanceFormController {

	private AgentNetworkCommShareManager agentNetworkCommShareManager;
	private ReferenceDataManager referenceDataManager;

	public DistributorCommShareFormController() {
		setCommandClass(DistributorCommShareVO.class);
		setCommandName("distributorCommShareVO");
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest arg0)
			throws Exception {
		return new DistributorCommShareVO();
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest request)
			throws Exception {

		Map<String, Object> referenceDataMap = new HashMap<String, Object>();
		List<DistributorModel> distributorModelList = null;
		List<ProductModel> productModelList = null;

		if (log.isDebugEnabled()) {
			log.debug("Inside reference data");
		}

		ReferenceDataWrapper referenceDataWrapper = null;

		try {
			// Load All Distributors
			referenceDataWrapper = new ReferenceDataWrapperImpl(
					new DistributorModel(), "name", SortingOrder.ASC);
			referenceDataManager.getReferenceData(referenceDataWrapper);
			distributorModelList = referenceDataWrapper.getReferenceDataList();

			referenceDataWrapper = new ReferenceDataWrapperImpl(
					new ProductModel(), "name", SortingOrder.ASC);
			referenceDataManager.getReferenceData(referenceDataWrapper);
			productModelList = referenceDataWrapper.getReferenceDataList();
		} catch (Exception ex) {
			log.error("Error getting product data in loadReferenceData", ex);
			ex.printStackTrace();
		}

		referenceDataMap.put("distributorModelList", distributorModelList);
		referenceDataMap.put("productModelList", productModelList);

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
	protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object object,
			BindException bindException) throws Exception {

		DistributorCommShareVO commShareVO = (DistributorCommShareVO) object;
		
		DistributorCommissionShareModel model = new DistributorCommissionShareModel();
		
		if(commShareVO.getDistributorCommShareId()!=null)
		{
			model.setDistributorCommissionShareId(commShareVO.getDistributorCommShareId());
			BaseWrapper baseWrapper=new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(model);
			model	=	(DistributorCommissionShareModel) this.agentNetworkCommShareManager.getDistributorCommissionShare(baseWrapper).getBasePersistableModel();
		}
		else
		{
			model.setDistributorCommissionShareId(commShareVO.getDistributorCommShareId());
			model.setCreatedOn(new Date());
			model.setCreatedByAppUserModel(UserUtils.getCurrentUser());
			model.setCurrentDistributorLevelId(commShareVO.getCurrentLevelId());
			model.setDistributorIdModel(new DistributorModel(commShareVO.getDistributorId()));
			model.setProductId(commShareVO.getProductId());
			model.setRegionId(commShareVO.getRegionId());
		}

		model.setUpdatedOn(new Date());
		model.setUpdatedByAppUserModel(UserUtils.getCurrentUser());

		if(commShareVO.getCommissionShare()==null)
		{
			model.setCommissionShare(0D);
		}
		else
		{
			model.setCommissionShare(commShareVO.getCommissionShare());
		}
		
		for (DistributorCommissionShareDetailModel item : commShareVO.getDistCommShareDtlModelList()) {
			item.setCreatedOn(new Date());
			item.setUpdatedOn(new Date());
			item.setCreatedByAppUserModel(UserUtils.getCurrentUser());
			item.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		}
		
		BaseWrapper baseWrapper=new BaseWrapperImpl(); 
		baseWrapper.setBasePersistableModel(model);
		baseWrapper.putObject("DistributorCommissionShareDetailModelList", (Serializable)commShareVO.getDistCommShareDtlModelList());
    	baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
    	baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long( PortalConstants.COMM_SH_SHARES_CREATE_UPDATE_USECASE_ID ) );

		this.agentNetworkCommShareManager.saveDistributorCommissionShare(baseWrapper);
        this.saveMessage(request, "Record saved successfully");
        return new ModelAndView(getSuccessView());
	}
	
	public ReferenceDataManager getReferenceDataManager() {
		return referenceDataManager;
	}

	public void setReferenceDataManager(
			ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public AgentNetworkCommShareManager getAgentNetworkCommShareManager() {
		return agentNetworkCommShareManager;
	}

	public void setAgentNetworkCommShareManager(
			AgentNetworkCommShareManager agentNetworkCommShareManager) {
		this.agentNetworkCommShareManager = agentNetworkCommShareManager;
	}
}
