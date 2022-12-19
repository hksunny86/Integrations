package com.inov8.microbank.server.facade.portal.commissionmodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.commissionmodule.CommissionTransactionViewModel;
import com.inov8.microbank.server.service.commissionmodule.CommissionTransactionViewManager;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Apr 26, 2013 3:54:32 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class CommissionTransactionViewFacadeImpl implements CommissionTransactionViewFacade
{
    //Autowired
    private CommissionTransactionViewManager commissionTransactionViewManager;
    //Autowired
    private FrameworkExceptionTranslator frameworkExceptionTranslator;

    public void CommissionTransactionViewManagerImpl()
    {
    }

    @Override
    public List<CommissionTransactionViewModel> searchCommissionTransactionView( SearchBaseWrapper wrapper ) throws FrameworkCheckedException
    {
        try
        {
            return commissionTransactionViewManager.searchCommissionTransactionView( wrapper );
        }
        catch( FrameworkCheckedException e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
    }
    @Override
	public List<CommissionTransactionViewModel> searchCommissionTransactionView(CommissionTransactionViewModel commissionTransactionViewModel) throws FrameworkCheckedException {	
	 try
        {
		 return commissionTransactionViewManager.searchCommissionTransactionView(commissionTransactionViewModel);
        }
        catch( FrameworkCheckedException e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
	}

    public CommissionTransactionViewManager getCommissionTransactionViewManager()
    {
        return commissionTransactionViewManager;
    }

    public void setCommissionTransactionViewManager( CommissionTransactionViewManager commissionTransactionViewManager )
    {
        this.commissionTransactionViewManager = commissionTransactionViewManager;
    }

}
