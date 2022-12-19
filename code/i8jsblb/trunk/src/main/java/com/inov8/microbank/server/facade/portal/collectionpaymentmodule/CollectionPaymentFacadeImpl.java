package com.inov8.microbank.server.facade.portal.collectionpaymentmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.portal.collectionpaymentmodule.CollectionPaymentManager;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Jul 11, 2013 11:12:44 AM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class CollectionPaymentFacadeImpl implements CollectionPaymentFacade
{
    //Autowired
    private CollectionPaymentManager collectionPaymentManager;

    //Autowired
    private FrameworkExceptionTranslator frameworkExceptionTranslator;

    @Override
    public SearchBaseWrapper searchCollectionPaymentsView( SearchBaseWrapper searchBaseWrapper ) throws FrameworkCheckedException
    {
        try
        {
            return collectionPaymentManager.searchCollectionPaymentsView( searchBaseWrapper );
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
    }

    public void setCollectionPaymentManager( CollectionPaymentManager collectionPaymentManager )
    {
        this.collectionPaymentManager = collectionPaymentManager;
    }

    public void setFrameworkExceptionTranslator( FrameworkExceptionTranslator frameworkExceptionTranslator )
    {
        this.frameworkExceptionTranslator = frameworkExceptionTranslator;
    }

}
