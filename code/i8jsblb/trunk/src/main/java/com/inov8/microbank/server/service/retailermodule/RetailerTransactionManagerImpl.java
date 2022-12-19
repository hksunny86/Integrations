package com.inov8.microbank.server.service.retailermodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.portal.retailertransactionmodule.RetailerTransactionViewModel;
import com.inov8.microbank.common.model.retailermodule.RetailerTransactionListViewModel;
import com.inov8.microbank.server.dao.retailermodule.RetailerTransactionListViewDAO;
import com.inov8.microbank.server.dao.retailermodule.RetailerTransactionViewDao;

/**
 *
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backened application for POS terminal</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Ltd</p>
 *
 * @author Ahmad Iqbal
 * @version 1.0
 *
 */

public class RetailerTransactionManagerImpl implements RetailerTransactionManager
{
    private RetailerTransactionListViewDAO retailerTransactionListViewDAO;

    //Autowired
    private RetailerTransactionViewDao     retailerTransactionViewDao;

    public SearchBaseWrapper searchRetailerTransaction( SearchBaseWrapper searchBaseWrapper ) throws FrameworkCheckedException
    {
        CustomList<RetailerTransactionListViewModel> list = this.retailerTransactionListViewDAO
            .findByExample( (RetailerTransactionListViewModel) searchBaseWrapper.getBasePersistableModel(),
                searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap() );
        searchBaseWrapper.setCustomList( list );
        return searchBaseWrapper;
    }

    @Override
    public SearchBaseWrapper searchRetailerTransactionView( SearchBaseWrapper wrapper ) throws FrameworkCheckedException
    {
        RetailerTransactionViewModel model = (RetailerTransactionViewModel) wrapper.getBasePersistableModel();
        CustomList<RetailerTransactionViewModel> customList = retailerTransactionViewDao.findByExample( model, wrapper.getPagingHelperModel(), wrapper.getSortingOrderMap(), wrapper.getDateRangeHolderModel() );
        wrapper.setCustomList( customList );
        return wrapper;
    }

    @Override
    public SearchBaseWrapper fetchRegionalRetailActivitySummary( SearchBaseWrapper wrapper ) throws FrameworkCheckedException
    {
        return retailerTransactionViewDao.fetchRegionalRetailActivitySummary( wrapper );
    }
 
    public void setRetailerTransactionListViewDAO( RetailerTransactionListViewDAO retailerTransactionListViewDAO )
    {
        this.retailerTransactionListViewDAO = retailerTransactionListViewDAO;
    }

    public void setRetailerTransactionViewDao( RetailerTransactionViewDao retailerTransactionViewDao )
    {
        this.retailerTransactionViewDao = retailerTransactionViewDao;
    }

}
