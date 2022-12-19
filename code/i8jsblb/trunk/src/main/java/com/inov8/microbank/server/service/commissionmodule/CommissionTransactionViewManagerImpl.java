package com.inov8.microbank.server.service.commissionmodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.commissionmodule.CommissionTransactionViewModel;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.server.dao.commissionmodule.CommissionTransactionViewDao;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Apr 26, 2013 3:38:21 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class CommissionTransactionViewManagerImpl implements CommissionTransactionViewManager
{
    //Autowired
    private CommissionTransactionViewDao commissionTransactionViewDao;

    public CommissionTransactionViewManagerImpl()
    {
    }

    @Override
    public List<CommissionTransactionViewModel> searchCommissionTransactionView( SearchBaseWrapper wrapper ) throws FrameworkCheckedException
    {
        List<CommissionTransactionViewModel> list = null;

        CommissionTransactionViewModel model = (CommissionTransactionViewModel) wrapper.getBasePersistableModel();
        CustomList<CommissionTransactionViewModel> customList = commissionTransactionViewDao.findByExample( model, wrapper.getPagingHelperModel(), wrapper.getSortingOrderMap(), wrapper.getDateRangeHolderModel() );
        if( customList != null )
        {
            wrapper.setCustomList( customList );
            list = customList.getResultsetList();
        }
        return list;
    }
    @Override
    public List<CommissionTransactionViewModel> searchCommissionTransactionView( CommissionTransactionViewModel commissionTransactionViewModel ) throws FrameworkCheckedException
    {
        List<CommissionTransactionViewModel> list = null;

        CustomList<CommissionTransactionViewModel> customList = commissionTransactionViewDao.findByExample( commissionTransactionViewModel,null,null,PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
        if( customList != null )
        {
            list = customList.getResultsetList();
        }
        return list;
    }

    public void setCommissionTransactionViewDao( CommissionTransactionViewDao commissionTransactionViewDao )
    {
        this.commissionTransactionViewDao = commissionTransactionViewDao;
    }

}
