package com.inov8.microbank.server.dao.blbreports;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.portal.blbreports.BdeKpiReportViewModel;

import java.util.List;

public interface BdeKpiReportDAO extends BaseDAO<BdeKpiReportViewModel, Long> {
    public List<BdeKpiReportViewModel> loadBdeKpiReportViewModel(BdeKpiReportViewModel bdeKpiReportViewModel) throws FrameworkCheckedException;
}
