package com.inov8.microbank.webapp.action.portal.complaintmodule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.ComplaintCategoryModel;
import com.inov8.microbank.common.model.ComplaintModel;
import com.inov8.microbank.common.model.ComplaintSubcategoryModel;
import com.inov8.microbank.common.model.portal.complaint.ComplaintDetailVO;
import com.inov8.microbank.common.model.portal.complaint.ComplaintHistoryVO;
import com.inov8.microbank.common.util.LabelValueBean;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.dao.complaintsmodule.ComplaintsModuleConstants;
import com.inov8.microbank.server.service.complaintmodule.ComplaintManager;
import com.inov8.microbank.server.service.complaintmodule.ComplaintStatusEnum;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;

public class ComplaintDetailFormController extends AdvanceFormController{
    
	public final static String COMPLAINT_ID = "complaintId";
	private final static Logger logger = Logger.getLogger(ComplaintDetailFormController.class);
	
	private ComplaintManager complaintManager;
	private ReferenceDataManager referenceDataManager;
	private MfsAccountManager mfsAccountManager;
	private RetailerContactManager retailerContactManager;
	private AppUserManager appUserManager;
	
	public ComplaintDetailFormController() {
		setCommandName("complaintDetailVO");
		setCommandClass(ComplaintDetailVO.class);
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest request) throws Exception {
		
		ComplaintDetailVO complaintDetailVO = new ComplaintDetailVO();

		Long complaintId = ServletRequestUtils.getLongParameter(request, COMPLAINT_ID);

		ComplaintModel complaintModel = complaintManager.getComplaintModelByPrimaryKey(complaintId);		

		ComplaintCategoryModel complaintCategoryModel = complaintManager.getComplaintCategoryModel(complaintModel.getComplaintCategoryId());
		
		ComplaintSubcategoryModel complaintSubcategoryModel = null;
		
		if(complaintModel.getComplaintSubcategoryId() != null) {
		
			complaintSubcategoryModel = complaintManager.getComplaintSubcategoryModel(complaintModel.getComplaintSubcategoryId());
		}
		
		List<ComplaintHistoryVO> complaintHistoryList = complaintManager.getComplaintHistoryModelList(complaintId);
		
		if(complaintModel.getInitAppUserId() != null && complaintModel.getInitAppUserId().longValue() > 0){
			AppUserModel  _appUserModel = appUserManager.getUser(String.valueOf(complaintModel.getInitAppUserId()));
			if(_appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()){
				complaintDetailVO.setIsCustomer(true);
			}else if(_appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER.longValue()){
				complaintDetailVO.setIsAgent(true);
			}
		}else{
			complaintDetailVO.setIsWalkin(true);
		}
		Map complaintParamValueMap = complaintManager.getComplaintParamValueMap(complaintId);
		
		complaintDetailVO.setComplaintModel(complaintModel);
		complaintDetailVO.setComplaintCategoryModel(complaintCategoryModel);
		complaintDetailVO.setComplaintSubcategoryModel(complaintSubcategoryModel);		
		complaintDetailVO.setComplaintHistoryList(complaintHistoryList);
//		complaintDetailVO.setAppUserModel(_appUserModel);
		complaintDetailVO.setComplaintParamValueMap(complaintParamValueMap);
		complaintDetailVO.setLoggedByName(complaintManager.getAssigneeName(complaintModel.getCreatedBy()));
		
		for(ComplaintHistoryVO vo : complaintHistoryList) {
			
			if(vo.getStatus().equalsIgnoreCase(ComplaintStatusEnum.ASSIGNED.getValue()) ||
					vo.getStatus().equalsIgnoreCase(ComplaintStatusEnum.OVERDUE.getValue())) {
				
				if(vo.getAssigneeAppUserId().longValue() == UserUtils.getCurrentUser().getAppUserId().longValue()) {
					
					complaintDetailVO.setIsSameAssigneeUser(Boolean.TRUE);
					break;
				}
			}
		}

		List<LabelValueBean> escalationList = new ArrayList<LabelValueBean>(4);
		if(ComplaintsModuleConstants.ESC_STATUS_DEFAULT.equalsIgnoreCase(complaintModel.getEscalationStatus())){
			escalationList.add(new LabelValueBean("Level 1 (" + complaintManager.getAssigneeName(complaintSubcategoryModel.getLevel1Assignee())+")","1"));
			escalationList.add(new LabelValueBean("Level 2 (" + complaintManager.getAssigneeName(complaintSubcategoryModel.getLevel2Assignee())+")","2"));
			escalationList.add(new LabelValueBean("Level 3 (" + complaintManager.getAssigneeName(complaintSubcategoryModel.getLevel3Assignee())+")","3"));
		}else if(ComplaintsModuleConstants.ESC_STATUS_LEVEL_1.equalsIgnoreCase(complaintModel.getEscalationStatus())){
			escalationList.add(new LabelValueBean("Level 2 (" + complaintManager.getAssigneeName(complaintSubcategoryModel.getLevel2Assignee())+")","2"));
			escalationList.add(new LabelValueBean("Level 3 (" + complaintManager.getAssigneeName(complaintSubcategoryModel.getLevel3Assignee())+")","3"));
		}else if(ComplaintsModuleConstants.ESC_STATUS_LEVEL_2.equalsIgnoreCase(complaintModel.getEscalationStatus())){
			escalationList.add(new LabelValueBean("Level 3 (" + complaintManager.getAssigneeName(complaintSubcategoryModel.getLevel3Assignee())+")","3"));
		}
		complaintDetailVO.setEscalationList(escalationList);
		return complaintDetailVO;
	}
	

	@Override
	protected Map loadReferenceData(HttpServletRequest req) throws Exception {

		return new HashMap();
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse res, Object model, BindException errors) throws Exception {

		return onUpdate(request, res, model, errors);
	}
	

	@Override
	protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse res, Object model, BindException errors) throws Exception {
		ComplaintDetailVO complaintDetailVO = (ComplaintDetailVO) model;
		try {
			complaintManager.updateComplaint(complaintDetailVO);
			this.saveMessage(request, super.getText("complaint.update.success", request.getLocale()));
		} catch (FrameworkCheckedException e) {
			logger.error(e);
			super.saveMessage(request, super.getText("complaint.update.failure", request.getLocale()));
			super.showForm(request, res, errors);			
		}
		catch (Exception e) {
			logger.error(e);
			super.saveMessage(request, MessageUtil.getMessage("6075"));
			super.showForm(request, res, errors);			
		}
		return new ModelAndView(new RedirectView("p_mycomplaints.html?actionId=2"));
	}
	
	public void setComplaintManager(ComplaintManager complaintManager) {
		this.complaintManager = complaintManager;
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public void setMfsAccountManager(MfsAccountManager mfsAccountManager) {
		this.mfsAccountManager = mfsAccountManager;
	}

	public void setRetailerContactManager(RetailerContactManager retailerContactManager) {
		this.retailerContactManager = retailerContactManager;
	}
	
	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}

}
