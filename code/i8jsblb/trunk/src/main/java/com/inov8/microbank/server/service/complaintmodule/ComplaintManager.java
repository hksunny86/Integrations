package com.inov8.microbank.server.service.complaintmodule;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.inov8.framework.common.exception.FrameworkCheckedException;
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

public interface ComplaintManager {

	public void saveUpdateAllHistory(List<ComplaintHistoryModel> list) throws FrameworkCheckedException;
	public void saveUpdate(ComplaintModel complaintModel, ComplaintReportModel reportModel) throws FrameworkCheckedException;
	public List<ComplaintHistoryModel> getComplaintHistoryModelList(ComplaintHistoryModel complaintHistoryModel) throws FrameworkCheckedException;
	public List<ComplaintHistoryVO> getComplaintHistoryModelList(Long complaintId) throws FrameworkCheckedException;
	public ComplaintModel getComplaintModelByPrimaryKey(Long primaryKey) throws FrameworkCheckedException;
	public ComplaintReportModel loadComplaintReportByComplaintId(Long complaintId) throws FrameworkCheckedException;
	public Map<String, Object> getTurnedAroundTime(Integer displayOrder, Long complaintId) throws FrameworkCheckedException;
	public void sendEscalationEmail(List<ComplaintHistoryModel> complaintHistoryModelEmailList) throws FrameworkCheckedException;
	public BaseWrapper createComplaint(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public void createComplaint(Long complaintCategoryId, Long appUserIdCustomer) throws FrameworkCheckedException;
	public void createComplaint(Long complaintCategoryId, Long appUserIdCustomer, String comments, Date updationTime) throws FrameworkCheckedException;
	public CustomList<ComplaintListViewModel> searchComplaintList(SearchBaseWrapper wrapper ) throws FrameworkCheckedException;
	public CustomList<ComplaintReportModel> searchComplaintReportList(SearchBaseWrapper wrapper ) throws FrameworkCheckedException;
	public CustomList<ComplaintHistoryViewModel> searchComplaintHistory(SearchBaseWrapper wrapper ) throws FrameworkCheckedException;
	public List<LabelValueBean> loadAssigneeList() throws FrameworkCheckedException;
	public List<LabelValueBean> loadL0AssigneeList() throws FrameworkCheckedException;
	public List<LabelValueBean> loadL1AssigneeList() throws FrameworkCheckedException;
	public List<LabelValueBean> loadL2AssigneeList() throws FrameworkCheckedException;
	public List<LabelValueBean> loadL3AssigneeList() throws FrameworkCheckedException;
	public ComplaintSubcategoryModel getComplaintSubcategoryModel(Long complaintSubcategoryId) throws FrameworkCheckedException;
	public ComplaintCategoryModel getComplaintCategoryModel(Long complaintCategoryId) throws FrameworkCheckedException;
	public Map getComplaintParamValueMap(Long complaintId);
	public void updateComplaintStatus(Map<Long, String> updateStatusMap, Map<Long, String> updateEscStatusMap, List<ComplaintReportModel> complaintReportModelList);
	public String getAssigneeName(Long appUserId) throws FrameworkCheckedException;
	public void updateComplaint(ComplaintDetailVO complaintDetailVO) throws FrameworkCheckedException;
	public Date calcTurnedAroundTime(Integer assigneeTurnedAroundTimeHour, Calendar calendar) throws FrameworkCheckedException;
	public List<ComplaintReportModel> searchComplaintByConsumerNo(SearchBaseWrapper wrapper) throws FrameworkCheckedException;
	public List<ComplaintReportModel> searchUserComplaintHistory(Long appUserId) throws FrameworkCheckedException;
	public Date calcExpectedTat(Integer totalTat, Calendar calendar) throws FrameworkCheckedException;
	public BaseWrapper saveComplaintSubcategory(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public List<LabelValueBean> loadBankUserList() throws FrameworkCheckedException;
	public BaseWrapper loadComplaintSubcategory(BaseWrapper wrapper ) throws FrameworkCheckedException;
	public List<ComplaintSubcategoryViewModel> searchComplaintSubcategoryList(SearchBaseWrapper wrapper ) throws FrameworkCheckedException;
}