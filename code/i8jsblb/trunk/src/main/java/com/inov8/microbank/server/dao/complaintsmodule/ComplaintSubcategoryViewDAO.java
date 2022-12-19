package com.inov8.microbank.server.dao.complaintsmodule;

import java.util.List;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.ComplaintSubcategoryViewModel;

public interface ComplaintSubcategoryViewDAO extends BaseDAO<ComplaintSubcategoryViewModel, Long> {

	public List<ComplaintSubcategoryViewModel> searchComplaintNature(ComplaintSubcategoryViewModel model);
}