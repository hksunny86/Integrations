package com.inov8.microbank.server.facade.portal.financereportsmodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.portal.financereportsmodule.CustomerBalanceReportViewModel;
import com.inov8.microbank.common.model.portal.ola.BBSettlementAccountsViewModel;
import com.inov8.microbank.server.service.portal.financereportsmodule.FinanceReportsManager;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Jan 11, 2013 4:41:44 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class FinanceReportsFacacdeImpl implements FinanceReportsFacacde
{

    //Autowired
    private FinanceReportsManager financeReportsManager;
    //Autowired
    private FrameworkExceptionTranslator frameworkExceptionTranslator;

    @Override
    public SearchBaseWrapper searchAgentClosingBalanceView( SearchBaseWrapper searchBaseWrapper ) throws FrameworkCheckedException
    {
        try
        {
            return financeReportsManager.searchAgentClosingBalanceView( searchBaseWrapper );
        }
        catch( Exception e )
        {
            throw this.frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
    }
    @Override
   	public SearchBaseWrapper searchCustomerClosingBalanceView(SearchBaseWrapper searchBaseWrapper)throws FrameworkCheckedException {
       	 try
            {
                return financeReportsManager.searchCustomerClosingBalanceView(searchBaseWrapper);
            }
            catch( Exception e )
            {
                throw this.frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
            }
   	}

    @Override
    public SearchBaseWrapper searchCustomerBalanceReportView( SearchBaseWrapper searchBaseWrapper ) throws FrameworkCheckedException
    {
        try
        {
            return financeReportsManager.searchCustomerBalanceReportView( searchBaseWrapper );
        }
        catch( Exception e )
        {
            throw this.frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
    }

    @Override
    public SearchBaseWrapper searchCommissionSummaryReportView( SearchBaseWrapper searchBaseWrapper ) throws FrameworkCheckedException
    {
        try
        {
            return financeReportsManager.searchCommissionSummaryReportView( searchBaseWrapper );
        }
        catch( Exception e )
        {
            throw this.frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
    }

    @Override
    public SearchBaseWrapper searchCustomerBalanceSummaryReport( SearchBaseWrapper searchBaseWrapper ) throws FrameworkCheckedException
    {
        try
        {
            return financeReportsManager.searchCustomerBalanceSummaryReport(searchBaseWrapper);
        }
        catch( Exception e )
        {
            throw this.frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
    }

	@Override
	public BaseWrapper updateDailyCustomerBalanceSummary(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		 try
	        {
	            return financeReportsManager.updateDailyCustomerBalanceSummary(baseWrapper);
	        }
	        catch( Exception e )
	        {
	            throw this.frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.INSERT_ACTION );
	        }
	}

	@Override
	public List<CustomerBalanceReportViewModel> customSearchCustomerBalanceReportView(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
		 try
	        {
	            return financeReportsManager.customSearchCustomerBalanceReportView(wrapper);
	        }
	        catch( Exception e )
	        {
	            throw this.frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
	        }
		
	}
	@Override
	public SearchBaseWrapper searchDailyCustomerBalanceReportView(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
		 try
	        {
	            return financeReportsManager.searchDailyCustomerBalanceReportView(wrapper);
	        }
	        catch( Exception e )
	        {
	            throw this.frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
	        }
		
	}
	@Override
	public SearchBaseWrapper searchBBSettlementAccountsReport(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		 try
	        {
	            return financeReportsManager.searchBBSettlementAccountsReport(searchBaseWrapper);
	        }
	        catch( Exception e )
	        {
	            throw this.frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
	        }
		
	}

	@Override
	public SearchBaseWrapper searchSettlementClosingBalanceView(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try
        {
            return financeReportsManager.searchSettlementClosingBalanceView(searchBaseWrapper);
        }
        catch( Exception e )
        {
            throw this.frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
	}
	
	public void setFinanceReportsManager( FinanceReportsManager financeReportsManager )
	{
		this.financeReportsManager = financeReportsManager;
	}
	
	public void setFrameworkExceptionTranslator( FrameworkExceptionTranslator frameworkExceptionTranslator )
	{
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}
}
