package com.inov8.microbank.webapp.action.portal.complaintmodule;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.ComplaintCategoryModel;
import com.inov8.microbank.common.model.ComplaintSubcategoryModel;
import com.inov8.microbank.common.util.LabelValueBean;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.complaintmodule.ComplaintManager;

public class ComplaintSubcategoryFormController extends AdvanceFormController{
    
	private ComplaintManager complaintManager;
    private ReferenceDataManager referenceDataManager;
    
	public ComplaintSubcategoryFormController() {
		setCommandName("complaintSubcategoryModel");
		setCommandClass(ComplaintSubcategoryModel.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Map<String, Object> loadReferenceData(HttpServletRequest arg0) throws Exception {
		Map<String, Object> referenceDataMap = new HashMap<String, Object>(3);
	    ComplaintCategoryModel categoryModel = new ComplaintCategoryModel();
	    List<ComplaintCategoryModel> categoryModelList = null;
	    List<LabelValueBean> bankUserList = null;
	    categoryModel.setIsActive(true);
	    categoryModel.setIsAuto(false);
	    ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(categoryModel, "name", SortingOrder.ASC);
	    referenceDataManager.getReferenceData(referenceDataWrapper);
	    
	    if (referenceDataWrapper.getReferenceDataList() != null){
	    	categoryModelList = referenceDataWrapper.getReferenceDataList();
	    }
	    
	    bankUserList = this.complaintManager.loadBankUserList();
	    
	    referenceDataMap.put("categoryModelList", categoryModelList);
	    referenceDataMap.put("bankUserList", bankUserList);
	    
	    return referenceDataMap;
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest req) throws Exception {
		String subcategoryId = ServletRequestUtils.getStringParameter(req, "subcategoryId");
		ComplaintSubcategoryModel model = new ComplaintSubcategoryModel();
		if (null != subcategoryId && subcategoryId.trim().length() > 0) {
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			model.setComplaintSubcategoryId(new Long(subcategoryId));
			baseWrapper.setBasePersistableModel(model);
			baseWrapper = this.complaintManager.loadComplaintSubcategory(baseWrapper);
			model = (ComplaintSubcategoryModel) baseWrapper.getBasePersistableModel();
		}
		return model;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest req, HttpServletResponse res, Object model, BindException errors) throws Exception {
		Date nowDate = new Date();
		ComplaintSubcategoryModel complaintSubcategoryModel = (ComplaintSubcategoryModel) model;

		complaintSubcategoryModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		complaintSubcategoryModel.setCreatedOn(nowDate);
		complaintSubcategoryModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		complaintSubcategoryModel.setUpdatedOn(nowDate);
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(complaintSubcategoryModel);
		baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
		baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.CREATE_COMPLAINT_NATURE_USECASE_ID));
		try{
			baseWrapper = this.complaintManager.saveComplaintSubcategory(baseWrapper);
			complaintSubcategoryModel = (ComplaintSubcategoryModel) baseWrapper.getBasePersistableModel();
			
			String msg = super.getText("complaintsubcategory.add.success", req.getLocale());
			this.saveMessage(req, msg);
		}catch(FrameworkCheckedException fce){
			fce.printStackTrace();
			super.saveMessage(req, super.getText("complaintsubcategory.add.failure", req.getLocale()));
			return super.showForm(req, res, errors);
		}catch(Exception fce){
			fce.printStackTrace();
			super.saveMessage(req, MessageUtil.getMessage("6075"));
			return super.showForm(req, res, errors);
		}
		
		ModelAndView modelAndView = new ModelAndView( new RedirectView("p_complaintsubcategorysearch.html?actionId=2"));
		return modelAndView;
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest req, HttpServletResponse res, Object obj, BindException errors) throws Exception {
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		Date nowDate = new Date();
		ComplaintSubcategoryModel model = null;
		ComplaintSubcategoryModel tempModel = null;
		try{
			Long complaintSubcategoryId = new Long(ServletRequestUtils.getStringParameter(req, "subcategoryId"));			
			model = new ComplaintSubcategoryModel();
			tempModel = new ComplaintSubcategoryModel();
			model = (ComplaintSubcategoryModel) obj;
			model.setComplaintSubcategoryId(complaintSubcategoryId);
			baseWrapper.setBasePersistableModel(model);
			baseWrapper = this.complaintManager.loadComplaintSubcategory(baseWrapper);
			tempModel = (ComplaintSubcategoryModel) baseWrapper.getBasePersistableModel();
			
			model.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			model.setUpdatedOn(nowDate);
			model.setComplaintCategoryId(tempModel.getComplaintCategoryId());
			model.setCreatedOn(tempModel.getCreatedOn());
			model.setCreatedByAppUserModel(tempModel.getCreatedByAppUserModel());
			model.setVersionNo(tempModel.getVersionNo());
			baseWrapper.setBasePersistableModel(model);
			baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.UPDATE_COMPLAINT_NATURE_USECASE_ID));
		    baseWrapper = this.complaintManager.saveComplaintSubcategory(baseWrapper);
		   
		    String msg = super.getText("complaintsubcategory.update.success", req.getLocale());
			this.saveMessage(req, msg);
		}catch(FrameworkCheckedException exception){
			exception.printStackTrace();
			super.saveMessage(req, super.getText("complaintsubcategory.update.failure", req.getLocale()));
			return super.showForm(req, res, errors);
		}   
		catch(Exception fce){
			fce.printStackTrace();
			super.saveMessage(req, MessageUtil.getMessage("6075"));
			return super.showForm(req, res, errors);
		}
		ModelAndView modelAndView = new ModelAndView( new RedirectView("p_complaintsubcategorysearch.html?actionId=2"));
		return modelAndView;
	}
	
	public ReferenceDataManager getReferenceDataManager() {
		return referenceDataManager;
	}
	
	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public ComplaintManager getComplaintManager() {
		return complaintManager;
	}
	
	public void setComplaintManager(ComplaintManager complaintManager) {
		this.complaintManager = complaintManager;
	}
}