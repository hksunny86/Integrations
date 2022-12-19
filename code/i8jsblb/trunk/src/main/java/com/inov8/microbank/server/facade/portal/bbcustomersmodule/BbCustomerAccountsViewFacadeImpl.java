package com.inov8.microbank.server.facade.portal.bbcustomersmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.portal.bbcustomersmodule.BbCustomerAccountsViewManager;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Feb 1, 2013 7:12:40 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class BbCustomerAccountsViewFacadeImpl implements BbCustomerAccountsViewFacade
{

    //Autowired
    private FrameworkExceptionTranslator frameworkExceptionTranslator;

    private BbCustomerAccountsViewManager bbCustomerAccountsViewManager;

    //Autowired
    @Override
    public SearchBaseWrapper searchBbCustomerAccountsView( SearchBaseWrapper searchBaseWrapper ) throws FrameworkCheckedException
    {
        try
        {
            return bbCustomerAccountsViewManager.searchBbCustomerAccountsView( searchBaseWrapper );
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
    }

    public void setFrameworkExceptionTranslator( FrameworkExceptionTranslator frameworkExceptionTranslator )
    {
        this.frameworkExceptionTranslator = frameworkExceptionTranslator;
    }

    public void setBbCustomerAccountsViewManager( BbCustomerAccountsViewManager bbCustomerAccountsViewManager )
    {
        this.bbCustomerAccountsViewManager = bbCustomerAccountsViewManager;
    }

}
