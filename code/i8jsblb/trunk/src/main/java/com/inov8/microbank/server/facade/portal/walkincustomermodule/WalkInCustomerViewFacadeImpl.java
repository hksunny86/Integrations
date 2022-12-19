package com.inov8.microbank.server.facade.portal.walkincustomermodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.portal.walkincustomermodule.WalkInCustomerViewModel;
import com.inov8.microbank.server.service.portal.walkincustomermodule.WalkInCustomerViewManager;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Aug 1, 2012 6:35:41 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class WalkInCustomerViewFacadeImpl implements WalkInCustomerViewFacade
{

    private WalkInCustomerViewManager walkInCustomerViewManager;
    private FrameworkExceptionTranslator frameworkExceptionTranslator;

    public CustomList<WalkInCustomerViewModel> searchWalkInCustomerView( SearchBaseWrapper wrapper )  throws FrameworkCheckedException
    {
        try
        {
            return walkInCustomerViewManager.searchWalkInCustomerView( wrapper );
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
    }

    public void setWalkInCustomerViewManager( WalkInCustomerViewManager walkInCustomerViewManager )
    {
        this.walkInCustomerViewManager = walkInCustomerViewManager;
    }

    public void setFrameworkExceptionTranslator( FrameworkExceptionTranslator frameworkExceptionTranslator )
    {
        this.frameworkExceptionTranslator = frameworkExceptionTranslator;
    }

}
