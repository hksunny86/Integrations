package com.inov8.microbank.server.service.blbreports;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.portal.blbreports.BdeKpiReportViewModel;

import java.util.List;

public interface BLBReportsManager {

    public SearchBaseWrapper searchBdeKpiReportView(SearchBaseWrapper wrapper ) throws FrameworkCheckedException;
    public SearchBaseWrapper searchDebitCardChargesView(SearchBaseWrapper wrapper ) throws FrameworkCheckedException;
    public SearchBaseWrapper searchHandlerActivityReport(SearchBaseWrapper wrapper ) throws FrameworkCheckedException;
    public SearchBaseWrapper searchParentAgentChildReport(SearchBaseWrapper wrapper ) throws FrameworkCheckedException;
    public List<BdeKpiReportViewModel> loadBdeKpiReportViewModel(BdeKpiReportViewModel bdeKpiReportViewModel) throws FrameworkCheckedException;
    public SearchBaseWrapper searchDormancyReport(SearchBaseWrapper wrapper ) throws FrameworkCheckedException;
    public SearchBaseWrapper searchAccountDormancyReport(SearchBaseWrapper wrapper ) throws FrameworkCheckedException;
    public SearchBaseWrapper searchCnicExpiredReport(SearchBaseWrapper wrapper ) throws FrameworkCheckedException;

}
