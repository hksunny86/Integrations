package com.inov8.microbank.server.service.portal.bbcustomersmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.portal.bbcustomers.BbCustomerAccountsViewModel;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.server.dao.portal.bbcustomersmodule.BbCustomerAccountsViewDao;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import org.hibernate.criterion.MatchMode;

import java.util.List;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Feb 1, 2013 6:58:45 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class BbCustomerAccountsViewManagerImpl implements BbCustomerAccountsViewManager
{

    private BbCustomerAccountsViewDao bbCustomerAccountsViewDao;
    private ActionLogManager actionLogManager;

    @Override
    public SearchBaseWrapper searchBbCustomerAccountsView( SearchBaseWrapper wrapper ) throws FrameworkCheckedException
    {
        ActionLogModel actionLogModel = new ActionLogModel();
        actionLogModel.setUsecaseId(PortalConstants.KEY_BLB_ACCOUNTS_REPORT_USECASE_ID);
        actionLogModel = actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(wrapper,null,actionLogModel);
        BbCustomerAccountsViewModel model = (BbCustomerAccountsViewModel) wrapper.getBasePersistableModel();
        CustomList<BbCustomerAccountsViewModel> customList = bbCustomerAccountsViewDao.findByExample( model, wrapper.getPagingHelperModel(), wrapper.getSortingOrderMap(), wrapper.getDateRangeHolderModel(), new ExampleConfigHolderModel(false, true, false, MatchMode.EXACT));
        /*if(customList != null && !customList.getResultsetList().isEmpty())
        {
            List<BbCustomerAccountsViewModel> bbList = customList.getResultsetList();
            BbCustomerAccountsViewModel bbModel = bbList.get(0);
            actionLogModel.setCustomField11(bbModel.getMobileNumber());
        }*/
        wrapper.setCustomList( customList );
        actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(wrapper,null,actionLogModel);
        return wrapper;
    }

    public void setBbCustomerAccountsViewDao( BbCustomerAccountsViewDao bbCustomerAccountsViewDao )
    {
        this.bbCustomerAccountsViewDao = bbCustomerAccountsViewDao;
    }

    public void setActionLogManager(ActionLogManager actionLogManager) {
        this.actionLogManager = actionLogManager;
    }
}
