package com.inov8.microbank.server.service.portal.financereportsmodule;

import java.util.List;

import com.inov8.framework.common.model.ExampleConfigHolderModel;
import org.apache.log4j.Logger;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.portal.financereportsmodule.AgentClosingBalanceViewModel;
import com.inov8.microbank.common.model.portal.financereportsmodule.CommissionSummaryReportViewModel;
import com.inov8.microbank.common.model.portal.financereportsmodule.CustomerBalanceReportSummaryModel;
import com.inov8.microbank.common.model.portal.financereportsmodule.CustomerBalanceReportViewModel;
import com.inov8.microbank.common.model.portal.financereportsmodule.CustomerClosingBalanceViewModel;
import com.inov8.microbank.common.model.portal.financereportsmodule.SettlementClosingBalanceViewModel;
import com.inov8.microbank.common.model.portal.ola.BBSettlementAccountsViewModel;
import com.inov8.microbank.common.util.CustomerBalanceReportConstantsInterface;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.server.dao.dailyaccountstats.DailyAccountStatsDao;
import com.inov8.microbank.server.dao.portal.ola.BBSettlementAccountsViewDAO;
import com.inov8.microbank.server.dao.portal.reports.finance.AgentClosingBalanceViewDao;
import com.inov8.microbank.server.dao.portal.reports.finance.CommissionSummaryReportViewDao;
import com.inov8.microbank.server.dao.portal.reports.finance.CustomerBalanceReportViewDao;
import com.inov8.microbank.server.dao.portal.reports.finance.CustomerBalanceSummaryReportDao;
import com.inov8.microbank.server.dao.portal.reports.finance.CustomerClosingBalanceViewDao;
import com.inov8.microbank.server.dao.portal.reports.finance.SettlementClosingBalanceViewDAO;
import org.hibernate.criterion.MatchMode;

/**
 * Created By    : Naseer Ullah <br>
 * Creation Date : Jan 11, 2013 4:31:55 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class FinanceReportsManagerImpl implements FinanceReportsManager
{
	private static final Logger LOGGER = Logger.getLogger( FinanceReportsManagerImpl.class );
    //Autowired
    private AgentClosingBalanceViewDao agentClosingBalanceViewDao;
    //Autowired
    private CustomerClosingBalanceViewDao customerClosingBalanceViewDao;
    //Autowired
    private CustomerBalanceReportViewDao customerBalanceReportViewDao;
    //Autowired
    private CommissionSummaryReportViewDao commissionSummaryReportViewDao;  
    //Autowired
    private CustomerBalanceSummaryReportDao customerBalanceReportSummaryDao;
    //Autowired
    private DailyAccountStatsDao dailyAccountStatsDao;
    //Autowired
    private BBSettlementAccountsViewDAO bbSettlementAccountsViewDAO;
    //Autowired
    private SettlementClosingBalanceViewDAO settlementClosingBalanceViewDAO;

	@Override
    public SearchBaseWrapper searchAgentClosingBalanceView( SearchBaseWrapper wrapper ) throws FrameworkCheckedException
    {
        AgentClosingBalanceViewModel model = (AgentClosingBalanceViewModel) wrapper.getBasePersistableModel();
        CustomList<AgentClosingBalanceViewModel> customList = agentClosingBalanceViewDao.findByExample( model, wrapper.getPagingHelperModel(),
                                    wrapper.getSortingOrderMap(), wrapper.getDateRangeHolderModel() );
        wrapper.setCustomList( customList );
        return wrapper;
    }

    @Override
    public SearchBaseWrapper searchCustomerBalanceReportView( SearchBaseWrapper wrapper ) throws FrameworkCheckedException
    {
        CustomerBalanceReportViewModel model = (CustomerBalanceReportViewModel) wrapper.getBasePersistableModel();
        CustomList<CustomerBalanceReportViewModel> customList = customerBalanceReportViewDao.findByExample( model, wrapper.getPagingHelperModel(),
                                    wrapper.getSortingOrderMap(), wrapper.getDateRangeHolderModel() );
        wrapper.setCustomList( customList );
        return wrapper;
    }
    @Override
    public List<CustomerBalanceReportViewModel> customSearchCustomerBalanceReportView( SearchBaseWrapper wrapper ) throws FrameworkCheckedException
    {
    	String decryptionSchedulerStatus = (String)wrapper.getObject(CustomerBalanceReportConstantsInterface.KEY_SCHEDULER_STATUS);
    	Boolean isEndDayBalanceNull = (Boolean) wrapper.getObject(CustomerBalanceReportConstantsInterface.KEY_IS_END_DAY_BALANCE_NULL);
		Integer chunkSize = (Integer) wrapper.getObject(CustomerBalanceReportConstantsInterface.KEY_CHUNK_SIZE);
        List<CustomerBalanceReportViewModel> list = customerBalanceReportViewDao.customSearchCustomerBalanceReportView(decryptionSchedulerStatus,isEndDayBalanceNull,chunkSize);
        return list;
    }
    @Override
    public SearchBaseWrapper searchDailyCustomerBalanceReportView( SearchBaseWrapper wrapper ) throws FrameworkCheckedException
    {
        CustomList<CustomerBalanceReportViewModel> customList = customerBalanceReportViewDao.searchDailyCustomerBalanceReportView(wrapper);
        wrapper.setCustomList( customList );
        return wrapper;
    }
    
    @Override
    public SearchBaseWrapper searchCommissionSummaryReportView( SearchBaseWrapper wrapper ) throws FrameworkCheckedException
    {
        CommissionSummaryReportViewModel model = (CommissionSummaryReportViewModel) wrapper.getBasePersistableModel();
        CustomList<CommissionSummaryReportViewModel> customList = commissionSummaryReportViewDao.findByExample( model, wrapper.getPagingHelperModel(),
                                    wrapper.getSortingOrderMap(), wrapper.getDateRangeHolderModel() );
        wrapper.setCustomList( customList );
        return wrapper;
    }
    
    @Override
	public SearchBaseWrapper searchCustomerBalanceSummaryReport(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		
    	CustomerBalanceReportSummaryModel model = (CustomerBalanceReportSummaryModel) searchBaseWrapper.getBasePersistableModel();
         CustomList<CustomerBalanceReportSummaryModel> customList = customerBalanceReportSummaryDao.findByExample( model, searchBaseWrapper.getPagingHelperModel(),
        		 searchBaseWrapper.getSortingOrderMap(), searchBaseWrapper.getDateRangeHolderModel() );
         searchBaseWrapper.setCustomList( customList );
		return searchBaseWrapper;
	}
    
    @Override
	public SearchBaseWrapper searchCustomerClosingBalanceView(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		
    	CustomerClosingBalanceViewModel model = (CustomerClosingBalanceViewModel) searchBaseWrapper.getBasePersistableModel();
         CustomList<CustomerClosingBalanceViewModel> customList = customerClosingBalanceViewDao.findByExample( model, searchBaseWrapper.getPagingHelperModel(),
        		 searchBaseWrapper.getSortingOrderMap(), searchBaseWrapper.getDateRangeHolderModel() );
         searchBaseWrapper.setCustomList( customList );
		return searchBaseWrapper;
	}
    
    @SuppressWarnings("unchecked")
	@Override
   	public BaseWrapper updateDailyCustomerBalanceSummary(BaseWrapper baseWrapper) throws FrameworkCheckedException 
   	{
    	List<CustomerBalanceReportSummaryModel> list = (List<CustomerBalanceReportSummaryModel>)baseWrapper.getObject(CustomerBalanceReportConstantsInterface.KEY_COLLECTION);
    	Long actionId = (Long)baseWrapper.getObject(PortalConstants.KEY_ACTION_ID);

 		customerBalanceReportSummaryDao.updateDailyCustomerBalanceSummary(list, actionId);

 		Boolean isEndDayBalanceNull = (Boolean) baseWrapper.getObject(CustomerBalanceReportConstantsInterface.KEY_IS_END_DAY_BALANCE_NULL);
 		String oldStatus = (String)baseWrapper.getObject(CustomerBalanceReportConstantsInterface.KEY_SCHEDULER_STATUS);
 		String newStatus = (String)baseWrapper.getObject(CustomerBalanceReportConstantsInterface.KEY_SCHEDULER_STATUS_UPDATED);
		Long fromDailyAccountStatsId = list.get(list.size() -1).getDailyAccountStatsId();
		Long toDailyAccountStatsId = list.get(0).getDailyAccountStatsId();

		int updateCount = dailyAccountStatsDao.updateDailyAccountStats(isEndDayBalanceNull, oldStatus, newStatus, fromDailyAccountStatsId, toDailyAccountStatsId);
		LOGGER.info(updateCount + " records updated in DAILY_ACCOUNT_STATS.");

   		return baseWrapper;
   	}

    @Override
	public SearchBaseWrapper searchBBSettlementAccountsReport(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        ExampleConfigHolderModel configHolderModel=new ExampleConfigHolderModel();
        configHolderModel.setMatchMode(MatchMode.EXACT);
        configHolderModel.setEnableLike(Boolean.FALSE);
        configHolderModel.setIgnoreCase(Boolean.FALSE);
    	BBSettlementAccountsViewModel model = (BBSettlementAccountsViewModel) searchBaseWrapper.getBasePersistableModel();
         CustomList<BBSettlementAccountsViewModel> customList = bbSettlementAccountsViewDAO.findByExample( model, searchBaseWrapper.getPagingHelperModel(),
        		 searchBaseWrapper.getSortingOrderMap(), searchBaseWrapper.getDateRangeHolderModel(),configHolderModel);
         searchBaseWrapper.setCustomList( customList );
		return searchBaseWrapper;
	}

    /**
     * @author AtifHussain
     */
    @Override
    public SearchBaseWrapper searchSettlementClosingBalanceView(
    		SearchBaseWrapper searchBaseWrapper)throws FrameworkCheckedException {
		
    	List<Object[]> list=	
				settlementClosingBalanceViewDAO.searchSettlementClosingBalance((SettlementClosingBalanceViewModel) searchBaseWrapper.getBasePersistableModel()
    			,searchBaseWrapper.getDateRangeHolderModel(), searchBaseWrapper.getSortingOrderMap(), searchBaseWrapper.getPagingHelperModel());
    	 searchBaseWrapper.setCustomList(new CustomList<>(list));
    	 return searchBaseWrapper;
    }
    
    public void setAgentClosingBalanceViewDao( AgentClosingBalanceViewDao agentClosingBalanceViewDao )
    {
        this.agentClosingBalanceViewDao = agentClosingBalanceViewDao;
    }

    public void setCustomerClosingBalanceViewDao(
			CustomerClosingBalanceViewDao customerClosingBalanceViewDao) {
		this.customerClosingBalanceViewDao = customerClosingBalanceViewDao;
	}

	public void setCustomerBalanceReportViewDao( CustomerBalanceReportViewDao customerBalanceReportViewDao )
    {
        this.customerBalanceReportViewDao = customerBalanceReportViewDao;
    }

    public void setCommissionSummaryReportViewDao( CommissionSummaryReportViewDao commissionSummaryReportViewDao )
    {
        this.commissionSummaryReportViewDao = commissionSummaryReportViewDao;
    }

	public void setCustomerBalanceReportSummaryDao(CustomerBalanceSummaryReportDao customerBalanceReportSummaryDao) {
		this.customerBalanceReportSummaryDao = customerBalanceReportSummaryDao;
	}

	public void setDailyAccountStatsDao(DailyAccountStatsDao dailyAccountStatsDao)
	{
		this.dailyAccountStatsDao = dailyAccountStatsDao;
	}

	public void setBbSettlementAccountsViewDAO(BBSettlementAccountsViewDAO bbSettlementAccountsViewDAO)
	{
		this.bbSettlementAccountsViewDAO = bbSettlementAccountsViewDAO;
	}

	public void setSettlementClosingBalanceViewDAO(
			SettlementClosingBalanceViewDAO settlementClosingBalanceViewDAO) {
		this.settlementClosingBalanceViewDAO = settlementClosingBalanceViewDAO;
	}
}
