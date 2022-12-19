package com.inov8.microbank.server.service.blbreports;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.portal.blbreports.*;
import com.inov8.microbank.server.dao.blbreports.*;

import java.util.List;

public class BLBReportsManagerImpl implements BLBReportsManager{

    private BdeKpiReportDAO bdeKpiReportDAO;
    private DebitCardChargesReportDAO debitCardChargesReportDAO;
    private HandlerActivityReportDAO handlerActivityReportDAO;
    private AgentParentChildReportDAO agentParentChildReportDAO;
    private DormancyReportDAO dormancyReportDAO;
    private AccountDormancyReportDAO accountDormancyReportDAO;
    private CnicExpiredReportDAO cnicExpiredReportDAO;

    @Override
    public SearchBaseWrapper searchBdeKpiReportView(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
        BdeKpiReportViewModel model = (BdeKpiReportViewModel) wrapper.getBasePersistableModel();
        CustomList<BdeKpiReportViewModel> customList;
        customList = bdeKpiReportDAO.findByExample(model, wrapper.getPagingHelperModel(),wrapper.getSortingOrderMap(),
                wrapper.getDateRangeHolderModel());

        wrapper.setCustomList( customList );
        return wrapper;
    }

    @Override
    public SearchBaseWrapper searchDebitCardChargesView(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
        DebitCardChargesViewModel model = (DebitCardChargesViewModel) wrapper.getBasePersistableModel();
        CustomList<DebitCardChargesViewModel> customList;
        customList = debitCardChargesReportDAO.findByExample(model, wrapper.getPagingHelperModel(),wrapper.getSortingOrderMap(),
                wrapper.getDateRangeHolderModel());

        wrapper.setCustomList( customList );
        return wrapper;
    }

    @Override
    public SearchBaseWrapper searchHandlerActivityReport(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
        HandlerActivityReportModel model = (HandlerActivityReportModel) wrapper.getBasePersistableModel();
        CustomList<HandlerActivityReportModel> customList;
        customList = handlerActivityReportDAO.findByExample(model, wrapper.getPagingHelperModel(),wrapper.getSortingOrderMap(),
                wrapper.getDateRangeHolderModel());

        wrapper.setCustomList( customList );
        return wrapper;
    }

    @Override
    public SearchBaseWrapper searchParentAgentChildReport(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
        AgentParentChildReportModel model = (AgentParentChildReportModel) wrapper.getBasePersistableModel();
        CustomList<AgentParentChildReportModel> customList;
        customList = agentParentChildReportDAO.findByExample(model, wrapper.getPagingHelperModel(),wrapper.getSortingOrderMap(),
                wrapper.getDateRangeHolderModel());

        wrapper.setCustomList( customList );
        return wrapper;
    }

    @Override
    public List<BdeKpiReportViewModel> loadBdeKpiReportViewModel(BdeKpiReportViewModel bdeKpiReportViewModel) throws FrameworkCheckedException {
        return bdeKpiReportDAO.loadBdeKpiReportViewModel(bdeKpiReportViewModel);
    }

    @Override
    public SearchBaseWrapper searchDormancyReport(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
        DormancyReportViewModel model = (DormancyReportViewModel) wrapper.getBasePersistableModel();
        CustomList<DormancyReportViewModel> customList;
        customList = dormancyReportDAO.findByExample(model, wrapper.getPagingHelperModel(),wrapper.getSortingOrderMap(),
                wrapper.getDateRangeHolderModel());

        wrapper.setCustomList( customList );
        return wrapper;
    }

    @Override
    public SearchBaseWrapper searchAccountDormancyReport(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
        AccountDormancyReportViewModel model = (AccountDormancyReportViewModel) wrapper.getBasePersistableModel();
        CustomList<AccountDormancyReportViewModel> customList;
        customList = accountDormancyReportDAO.findByExample(model, wrapper.getPagingHelperModel(),wrapper.getSortingOrderMap(),
                wrapper.getDateRangeHolderModelList());

        wrapper.setCustomList( customList );
        return wrapper;
    }

    @Override
    public SearchBaseWrapper searchCnicExpiredReport(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
        CnicExpiredViewModel model = (CnicExpiredViewModel) wrapper.getBasePersistableModel();
        CustomList<CnicExpiredViewModel> customList;
        customList = cnicExpiredReportDAO.findByExample(model, wrapper.getPagingHelperModel(),wrapper.getSortingOrderMap(),
                wrapper.getDateRangeHolderModel());

        wrapper.setCustomList( customList );
        return wrapper;
    }

    public void setBdeKpiReportDAO(BdeKpiReportDAO bdeKpiReportDAO) {
        this.bdeKpiReportDAO = bdeKpiReportDAO;
    }

    public void setDebitCardChargesReportDAO(DebitCardChargesReportDAO debitCardChargesReportDAO) {
        this.debitCardChargesReportDAO = debitCardChargesReportDAO;
    }

    public void setHandlerActivityReportDAO(HandlerActivityReportDAO handlerActivityReportDAO) {
        this.handlerActivityReportDAO = handlerActivityReportDAO;
    }

    public void setAgentParentChildReportDAO(AgentParentChildReportDAO agentParentChildReportDAO) {
        this.agentParentChildReportDAO = agentParentChildReportDAO;
    }

    public void setDormancyReportDAO(DormancyReportDAO dormancyReportDAO) {
        this.dormancyReportDAO = dormancyReportDAO;
    }

    public void setCnicExpiredReportDAO(CnicExpiredReportDAO cnicExpiredReportDAO) {
        this.cnicExpiredReportDAO = cnicExpiredReportDAO;
    }

    public void setAccountDormancyReportDAO(AccountDormancyReportDAO accountDormancyReportDAO) {
        this.accountDormancyReportDAO = accountDormancyReportDAO;
    }
}
