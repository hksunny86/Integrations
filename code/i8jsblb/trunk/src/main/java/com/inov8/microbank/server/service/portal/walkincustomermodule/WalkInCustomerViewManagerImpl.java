package com.inov8.microbank.server.service.portal.walkincustomermodule;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.portal.walkincustomermodule.WalkInCustomerViewModel;
import com.inov8.microbank.server.dao.portal.walkincustomermodule.WalkInCustomerViewDao;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Aug 1, 2012 6:27:34 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class WalkInCustomerViewManagerImpl implements WalkInCustomerViewManager
{
    private WalkInCustomerViewDao walkInCustomerViewDao;

    public CustomList<WalkInCustomerViewModel> searchWalkInCustomerView( SearchBaseWrapper wrapper )
    {
        WalkInCustomerViewModel model = (WalkInCustomerViewModel) wrapper.getBasePersistableModel();
        return walkInCustomerViewDao.findByExample( model, wrapper.getPagingHelperModel(), wrapper.getSortingOrderMap(), wrapper.getDateRangeHolderModel() );
    }

    public void setWalkInCustomerViewDao( WalkInCustomerViewDao walkInCustomerViewDao )
    {
        this.walkInCustomerViewDao = walkInCustomerViewDao;
    }

}
