package com.inov8.microbank.server.dao.complaintsmodule;

import java.util.List;
import java.util.Map;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.ComplaintSubcategoryModel;
import com.inov8.microbank.common.util.LabelValueBean;

public interface ComplaintSubcategoryDAO extends BaseDAO<ComplaintSubcategoryModel, Long> {

	public static final String TOTAL_TAT = "TOTAL_TAT";	
	public static final String DISPLAY_ORDER = "DISPLAY_ORDER";	

	public static final String LEVEL0_ASSIGNEE_TAT = "LEVEL0_ASSIGNEE_TAT";	
	public static final String LEVEL1_ASSIGNEE_TAT = "LEVEL1_ASSIGNEE_TAT";	
	public static final String LEVEL2_ASSIGNEE_TAT = "LEVEL2_ASSIGNEE_TAT";	
	public static final String LEVEL3_ASSIGNEE_TAT = "LEVEL3_ASSIGNEE_TAT";	

	public static final String LEVEL0_ASSIGNEE = "LEVEL0_ASSIGNEE";	
	public static final String LEVEL1_ASSIGNEE = "LEVEL1_ASSIGNEE";	
	public static final String LEVEL2_ASSIGNEE = "LEVEL2_ASSIGNEE";	
	public static final String LEVEL3_ASSIGNEE = "LEVEL3_ASSIGNEE";	

	public static final String LEVEL0_ASSIGNEE_NAME = "LEVEL0_ASSIGNEE_NAME";	
	public static final String LEVEL1_ASSIGNEE_NAME = "LEVEL1_ASSIGNEE_NAME";	
	public static final String LEVEL2_ASSIGNEE_NAME = "LEVEL2_ASSIGNEE_NAME";	
	public static final String LEVEL3_ASSIGNEE_NAME = "LEVEL3_ASSIGNEE_NAME";	

	public static final String LEVEL0_ASSIGNEE_EMAIL = "LEVEL0_ASSIGNEE_EMAIL";
	public static final String LEVEL1_ASSIGNEE_EMAIL = "LEVEL1_ASSIGNEE_EMAIL";	
	public static final String LEVEL2_ASSIGNEE_EMAIL = "LEVEL2_ASSIGNEE_EMAIL";	
	public static final String LEVEL3_ASSIGNEE_EMAIL = "LEVEL3_ASSIGNEE_EMAIL";	

	public static final String LAST_TAT_END_TIME = "LAST_TAT_END_TIME";
	
	public static final String LEVEL_ASSIGNEE_TAT_PARAM = "LEVEL_ASSIGNEE_TAT_PARAM";
	public static final String LEVEL_ASSIGNEE_PARAM = "LEVEL_ASSIGNEE_PARAM";
	public static final String LEVEL_ASSIGNEE_NAME_PARAM = "LEVEL_ASSIGNEE_NAME_PARAM";
	public static final String LEVEL_ASSIGNEE_EMAIL_PARAM = "LEVEL_ASSIGNEE_EMAIL_PARAM";

	public static final String CATEGORY = "CATEGORY";
	public static final String COMPLAINT_DESC = "COMPLAINT_DESC";
	public static final String COMPLAINT_CODE = "COMPLAINT_CODE";
	public static final String COMPALINT_EXPECTED_TAT_DATE = "COMPALINT_EXPECTED_TAT_DATE";
	
	public Map getComplaintSubcategoryByComplaintId(Long complaintId);
	public List<LabelValueBean> loadAssigneeList();
	public List<LabelValueBean> loadl0AssigneeList();
	public List<LabelValueBean> loadl1AssigneeList();
	public List<LabelValueBean> loadl2AssigneeList();
	public List<LabelValueBean> loadl3AssigneeList();
	public String getAssigneeName(Long appUserId) throws FrameworkCheckedException;
	public List loadBankUsers() throws FrameworkCheckedException;
}