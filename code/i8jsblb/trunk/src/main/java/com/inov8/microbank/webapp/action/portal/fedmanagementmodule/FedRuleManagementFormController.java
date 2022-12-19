package com.inov8.microbank.webapp.action.portal.fedmanagementmodule;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.ServiceModel;
import com.inov8.microbank.common.model.TaxRegimeModel;
import com.inov8.microbank.common.util.ActionAuthorizationConstantsInterface;
import com.inov8.microbank.common.util.LabelValueBean;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.facade.taxregimemodule.TaxRegimeFacade;
import com.inov8.microbank.tax.model.FEDRuleModel;
import com.inov8.microbank.tax.vo.FedRuleManagementVO;

/**
 * Created by Abu Turab
 */
public class FedRuleManagementFormController extends AdvanceFormController {

	private ReferenceDataManager referenceDataManager;
	private TaxRegimeFacade taxRegimeFacade;

	public FedRuleManagementFormController() {
		setCommandName("fedRuleManagementVO");
		setCommandClass(FedRuleManagementVO.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Map loadReferenceData(HttpServletRequest httpServletRequest)
			throws Exception {
		Map<String, Object> referenceDataMap = new HashMap<String, Object>(7);

		List<ServiceModel> serviceModelList = null;
		List<ProductModel> productModelList = null;
		List<TaxRegimeModel> taxRegimeModelList = null;
		List<LabelValueBean> typeList = null;

		ReferenceDataWrapper referenceDataWrapper;

		ServiceModel serviceModel = new ServiceModel();
		serviceModel.setActive(true);
		referenceDataWrapper = new ReferenceDataWrapperImpl(serviceModel,
				"name", SortingOrder.ASC);
		referenceDataManager.getReferenceData(referenceDataWrapper);
		serviceModelList = referenceDataWrapper.getReferenceDataList();

		ProductModel productModel = new ProductModel();
		productModel.setActive(true);
		referenceDataWrapper = new ReferenceDataWrapperImpl(productModel,
				"name", SortingOrder.ASC);
		referenceDataManager.getReferenceData(referenceDataWrapper);
		productModelList = referenceDataWrapper.getReferenceDataList();

		TaxRegimeModel taxRegimeModel = new TaxRegimeModel();
		taxRegimeModel.setIsActive(true);
		referenceDataWrapper = new ReferenceDataWrapperImpl(taxRegimeModel,
				"taxRegimeId", SortingOrder.ASC);
		referenceDataManager.getReferenceData(referenceDataWrapper);
		taxRegimeModelList = referenceDataWrapper.getReferenceDataList();

		LabelValueBean type = new LabelValueBean("Inclusive", "1");
		typeList = new ArrayList<LabelValueBean>(2);
		typeList.add(type);
		type = new LabelValueBean("Exclusive", "0");
		typeList.add(type);

		referenceDataMap.put("typeList", typeList);
		referenceDataMap.put("taxRegimeModelList", taxRegimeModelList);
		referenceDataMap.put("serviceModelList", serviceModelList);
		referenceDataMap.put("productModelList", productModelList);

		return referenceDataMap;
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest httpServletRequest)
			throws Exception {
		FedRuleManagementVO fedRuleManagementVO = new FedRuleManagementVO();
	
		ProductModel productModel = new ProductModel();
		productModel.setActive(true);
		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
				productModel, "name", SortingOrder.ASC);
		referenceDataManager.getReferenceData(referenceDataWrapper);
		List<ProductModel> productModelList = referenceDataWrapper
				.getReferenceDataList();

		List<FEDRuleModel> list = taxRegimeFacade.loadFEDRuleModelList();

		if (org.apache.commons.collections.CollectionUtils.isEmpty(list)) {
			FEDRuleModel ruleModel = new FEDRuleModel();
			list = new ArrayList<>();
			list.add(ruleModel);
		}

		for (FEDRuleModel item : list) {

			for (ProductModel product : productModelList) {
				if (product.getServiceId().equals(item.getServiceId())) {
					fedRuleManagementVO.getProductModelList().add(product);
				}
			}

		}

		fedRuleManagementVO.setFedRuleModelList(list);

		return fedRuleManagementVO;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object object,
			BindException errors) throws Exception {
		FedRuleManagementVO vo = (FedRuleManagementVO) object;
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		for (FEDRuleModel fedRule : vo.getFedRuleModelList()) {
			fedRule.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			fedRule.setCreatedBy(fedRule.getCreatedBy() == null ? UserUtils.getCurrentUser().getAppUserId() : fedRule.getCreatedBy());
			fedRule.setCreatedOn(fedRule.getCreatedOn() == null ? new Date(): fedRule.getCreatedOn());
			fedRule.setUpdatedOn(fedRule.getUpdatedOn() == null ? new Date(): fedRule.getUpdatedOn());
			fedRule.setActive(fedRule.getActive() == null ? false : fedRule.getActive());
			fedRule.setInclusive(true);
			fedRule.setProductModel(fedRule.getProductId() == null ? null : fedRule.getProductModel());
			if (fedRule.getTaxRegimeId() == null){
				fedRule.setTaxRegimeModel(null);
				//fedRule.setRate(null);
			}
		}

		baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
		baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long( PortalConstants.KEY_CREATE_UPDATE_FED_RULE_MGMT_USECASE_ID ) );
		baseWrapper.setBasePersistableModel(vo);
		
    	//populateAuthenticationParams(baseWrapper,httpServletRequest,vo);
	
		baseWrapper = taxRegimeFacade.saveUpdateFedRules(baseWrapper);

		String msg = (String) baseWrapper
				.getObject(ActionAuthorizationConstantsInterface.KEY_AUTHORIZATION_MSG);
		if (null == msg) {
			msg ="FED Rules saved successfully";
		}

		this.saveMessage(httpServletRequest, msg);

		return new ModelAndView(getSuccessView());
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object object,
			BindException errors) throws Exception {
		super.showForm(httpServletRequest, httpServletResponse, errors);
		return null;
	}
	
	@Override
    protected void populateAuthenticationParams(BaseWrapper baseWrapper, HttpServletRequest req,Object model) throws FrameworkCheckedException
    {

		FedRuleManagementVO fedRuleManagementVO= (FedRuleManagementVO) model;

		for (FEDRuleModel fedRule : fedRuleManagementVO.getFedRuleModelList()) {
			fedRule.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			fedRule.setCreatedBy(fedRule.getCreatedBy() == null ? UserUtils
					.getCurrentUser().getAppUserId() : fedRule.getCreatedBy());
			fedRule.setCreatedOn(fedRule.getCreatedOn() == null ? new Date()
					: fedRule.getCreatedOn());
			fedRule.setUpdatedOn(fedRule.getUpdatedOn() == null ? new Date()
					: fedRule.getUpdatedOn());
			fedRule.setVersionNo(fedRule.getVersionNo() == null ? 0 : fedRule
					.getVersionNo());
		}
		
        ObjectMapper mapper = new ObjectMapper();
        Long actionAuthorizationId =null;
        try
        {
            String strActionAuthorizationId = ServletRequestUtils.getStringParameter(req,"actionAuthorizationId");
            if(null != strActionAuthorizationId && !"".equals(strActionAuthorizationId)){
            	actionAuthorizationId = Long.parseLong(strActionAuthorizationId);
            }
        } catch (ServletRequestBindingException e1) {
            e1.printStackTrace();
        }

        //Setting date format for Date Fields when parsed as String. Jakson parse only standard string value to date object
        DateFormat df = new SimpleDateFormat(PortalDateUtils.SHORT_DATE_FORMAT_2);
        mapper.setDateFormat(df);

        String modelJsonString =null;
        String initialModelJsonString =null;
        try
        {
            modelJsonString = mapper.writeValueAsString(fedRuleManagementVO);
        } catch (IOException e)
        {
            e.printStackTrace();
            throw new FrameworkCheckedException(MessageUtil.getMessage(ActionAuthorizationConstantsInterface.REF_DATA_EXCEPTION_MSG));
        }

        FedRuleManagementVO initialModel = null;
        try {
            initialModel = (FedRuleManagementVO) this.loadFormBackingObject(req);
        } catch(Exception e) {
            e.printStackTrace();
        }

        try
        {
            initialModelJsonString = mapper.writeValueAsString(initialModel);

        } catch (IOException e) {
            e.printStackTrace();
            throw new FrameworkCheckedException(MessageUtil.getMessage(ActionAuthorizationConstantsInterface.REF_DATA_EXCEPTION_MSG));
        }
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_METHODE_NAME,MessageUtil.getMessage("FedRuleManagementVO.methodeName"));

        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_OLD_MODEL_STRING, initialModelJsonString);
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS,FedRuleManagementVO.class.getSimpleName());
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS_QUALIFIED_NAME,FedRuleManagementVO.class.getName());
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_STRING, modelJsonString);
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_REQ_REF_ID,null);

        baseWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID,actionAuthorizationId);
        baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
        baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.KEY_CREATE_UPDATE_FED_RULE_MGMT_USECASE_ID);
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MANAGER_BEAN_NAME,MessageUtil.getMessage("FedRuleManagementVO.Manager"));
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_FORM_NAME,this.getFormView());
    }

	public void setReferenceDataManager(
			ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public void setTaxRegimeFacade(TaxRegimeFacade taxRegimeFacade) {
		this.taxRegimeFacade = taxRegimeFacade;
	}
}
