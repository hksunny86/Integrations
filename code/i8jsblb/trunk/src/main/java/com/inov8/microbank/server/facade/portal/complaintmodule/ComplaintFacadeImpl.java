package com.inov8.microbank.server.facade.portal.complaintmodule;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.ComplaintCategoryModel;
import com.inov8.microbank.common.model.ComplaintHistoryModel;
import com.inov8.microbank.common.model.ComplaintModel;
import com.inov8.microbank.common.model.ComplaintReportModel;
import com.inov8.microbank.common.model.ComplaintSubcategoryModel;
import com.inov8.microbank.common.model.ComplaintSubcategoryViewModel;
import com.inov8.microbank.common.model.portal.complaint.ComplaintDetailVO;
import com.inov8.microbank.common.model.portal.complaint.ComplaintHistoryVO;
import com.inov8.microbank.common.model.portal.complaint.ComplaintHistoryViewModel;
import com.inov8.microbank.common.model.portal.complaint.ComplaintListViewModel;
import com.inov8.microbank.common.util.LabelValueBean;
import com.inov8.microbank.server.service.complaintmodule.ComplaintManager;

public class ComplaintFacadeImpl implements ComplaintFacade {
	private ComplaintManager complaintManager;
	private FrameworkExceptionTranslator frameworkExceptionTranslator;

	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	public void setComplaintManager(ComplaintManager complaintManager) {
		this.complaintManager = complaintManager;
	}

	public void saveUpdateAllHistory(List<ComplaintHistoryModel> list) throws FrameworkCheckedException {
		complaintManager.saveUpdateAllHistory(list);
	}

	public void saveUpdate(ComplaintModel complaintModel, ComplaintReportModel reportModel) throws FrameworkCheckedException {
		complaintManager.saveUpdate(complaintModel,reportModel);
	}

	public List<ComplaintHistoryModel> getComplaintHistoryModelList(ComplaintHistoryModel complaintHistoryModel) throws FrameworkCheckedException{
		
		try {
			
			return complaintManager.getComplaintHistoryModelList(complaintHistoryModel);
			
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	@Override
	public ComplaintModel getComplaintModelByPrimaryKey(Long primaryKey) throws FrameworkCheckedException {
		return complaintManager.getComplaintModelByPrimaryKey(primaryKey);
	}
	
	@Override
	public ComplaintReportModel loadComplaintReportByComplaintId(Long complaintId) throws FrameworkCheckedException {
		return complaintManager.loadComplaintReportByComplaintId(complaintId);
	}

	@Override
	public Map<String, Object> getTurnedAroundTime(Integer displayOrder, Long complaintId) throws FrameworkCheckedException {
		return complaintManager.getTurnedAroundTime(displayOrder, complaintId);
	}


	public BaseWrapper createComplaint(BaseWrapper baseWrapper) throws FrameworkCheckedException{
		try {
			return complaintManager.createComplaint(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.INSERT_ACTION);
		}
	}


	public void sendEscalationEmail(List<ComplaintHistoryModel> list) throws FrameworkCheckedException {
		complaintManager.sendEscalationEmail(list);
	}

	@Override
	public CustomList<ComplaintListViewModel> searchComplaintList(
			SearchBaseWrapper wrapper) throws FrameworkCheckedException {
		try {
			return complaintManager.searchComplaintList(wrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	@Override
	public CustomList<ComplaintReportModel> searchComplaintReportList(
			SearchBaseWrapper wrapper) throws FrameworkCheckedException {
		try {
			return complaintManager.searchComplaintReportList(wrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	@Override
	public List<LabelValueBean> loadAssigneeList() throws FrameworkCheckedException {
		try {
			return complaintManager.loadAssigneeList();
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}
	
	@Override
	public List<LabelValueBean> loadL0AssigneeList() throws FrameworkCheckedException {
		try {
			return complaintManager.loadL0AssigneeList();
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}
	
	@Override
	public List<LabelValueBean> loadL1AssigneeList() throws FrameworkCheckedException {
		try {
			return complaintManager.loadL1AssigneeList();
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}
	
	@Override
	public List<LabelValueBean> loadL2AssigneeList() throws FrameworkCheckedException {
		try {
			return complaintManager.loadL2AssigneeList();
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}
	
	@Override
	public List<LabelValueBean> loadL3AssigneeList() throws FrameworkCheckedException {
		try {
			return complaintManager.loadL3AssigneeList();
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	@Override
	public CustomList<ComplaintHistoryViewModel> searchComplaintHistory(
			SearchBaseWrapper wrapper) throws FrameworkCheckedException {
		try {
			return complaintManager.searchComplaintHistory(wrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	@Override
	public List<ComplaintHistoryVO> getComplaintHistoryModelList(Long complaintId) throws FrameworkCheckedException {
		return complaintManager.getComplaintHistoryModelList(complaintId);
	}

	@Override
	public ComplaintSubcategoryModel getComplaintSubcategoryModel(Long complaintSubcategoryId) throws FrameworkCheckedException {

		return complaintManager.getComplaintSubcategoryModel(complaintSubcategoryId);
	}

	@Override
	public ComplaintCategoryModel getComplaintCategoryModel(Long complaintCategoryId) throws FrameworkCheckedException {

		return complaintManager.getComplaintCategoryModel(complaintCategoryId);
	}

	@Override
	public Map getComplaintParamValueMap(Long complaintId) {

		return complaintManager.getComplaintParamValueMap(complaintId);
	}

	@Override
	public void updateComplaintStatus(Map<Long, String> updateStatusMap, Map<Long, String> updateEscStatusMap, List<ComplaintReportModel> complaintReportModelList) {
		
		complaintManager.updateComplaintStatus(updateStatusMap, updateEscStatusMap, complaintReportModelList);
	}

	@Override
	public void createComplaint(Long complaintCategoryId, Long appUserIdCustomer) throws FrameworkCheckedException {

		complaintManager.createComplaint(complaintCategoryId, appUserIdCustomer);
	}

	@Override
	public void createComplaint(Long complaintCategoryId, Long appUserIdCustomer, String comments, Date updationTime) throws FrameworkCheckedException {

		complaintManager.createComplaint(complaintCategoryId, appUserIdCustomer, comments, updationTime);
	}

	@Override
	public String getAssigneeName(Long appUserId) throws FrameworkCheckedException {
		return complaintManager.getAssigneeName(appUserId);
	}
	
	@Override
	public void updateComplaint(ComplaintDetailVO complaintDetailVO) throws FrameworkCheckedException {
		complaintManager.updateComplaint(complaintDetailVO);
	}

	@Override
	public Date calcTurnedAroundTime(Integer assigneeTurnedAroundTimeHour, Calendar calendar) throws FrameworkCheckedException {
		
		return complaintManager.calcTurnedAroundTime(assigneeTurnedAroundTimeHour, calendar);
	}

	@Override
	public List<ComplaintReportModel> searchComplaintByConsumerNo(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
		return complaintManager.searchComplaintByConsumerNo(wrapper);
	}

	@Override
	public List<ComplaintReportModel> searchUserComplaintHistory(Long appUserId)
			throws FrameworkCheckedException {
		try{
			return complaintManager.searchUserComplaintHistory(appUserId);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	@Override
	public BaseWrapper saveComplaintSubcategory(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		try{
			return complaintManager.saveComplaintSubcategory(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.INSERT_ACTION);
		}
	}

	@Override
	public List<LabelValueBean> loadBankUserList() throws FrameworkCheckedException {
		try{
			return complaintManager.loadBankUserList();
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	@Override
	public List<ComplaintSubcategoryViewModel> searchComplaintSubcategoryList(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
		try{
			return complaintManager.searchComplaintSubcategoryList(wrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	@Override
	public BaseWrapper loadComplaintSubcategory(BaseWrapper wrapper) throws FrameworkCheckedException {
		try {
			return complaintManager.loadComplaintSubcategory(wrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	@Override
	public Date calcExpectedTat(Integer totalTat, Calendar calendar) throws FrameworkCheckedException {

		return complaintManager.calcExpectedTat(totalTat, calendar);
	}

}
