package com.inov8.microbank.tax.dao;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.tax.model.WHTExemptionModel;

import java.util.List;

public interface WHTExemptionDAO extends BaseDAO<WHTExemptionModel, Long> {

	public WHTExemptionModel loadValidWHTExemption(Long appUserId);

	public List<WHTExemptionModel> loadWHTExemptionByAppUserId(Long appUserId);
	public WHTExemptionModel loadValidWHTExemptionForScheduler(Long appUserId);
}
