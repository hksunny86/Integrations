package com.inov8.microbank.server.service.portal.bbcustomersmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Feb 1, 2013 6:56:34 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public interface BbCustomerAccountsViewManager
{
    SearchBaseWrapper searchBbCustomerAccountsView(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
}
