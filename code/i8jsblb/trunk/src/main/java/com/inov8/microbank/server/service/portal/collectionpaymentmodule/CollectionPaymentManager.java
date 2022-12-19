package com.inov8.microbank.server.service.portal.collectionpaymentmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Jul 11, 2013 11:05:17 AM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public interface CollectionPaymentManager
{
    SearchBaseWrapper searchCollectionPaymentsView(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
}
