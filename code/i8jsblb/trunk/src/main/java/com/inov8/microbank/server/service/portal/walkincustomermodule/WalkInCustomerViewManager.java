package com.inov8.microbank.server.service.portal.walkincustomermodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.portal.walkincustomermodule.WalkInCustomerViewModel;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Aug 1, 2012 6:24:04 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public interface WalkInCustomerViewManager
{
    CustomList<WalkInCustomerViewModel> searchWalkInCustomerView( SearchBaseWrapper wrapper ) throws FrameworkCheckedException;
}
