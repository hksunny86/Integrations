package com.inov8.microbank.server.service.portal.ola;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.SortingOrder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.TransactionDetailMasterModel;
import com.inov8.microbank.common.model.portal.agentgroup.AgentTaggingViewModel;
import com.inov8.microbank.common.model.portal.ola.AgentBBStatementViewModel;
import com.inov8.microbank.common.model.portal.ola.BbStatementAllViewModel;
import com.inov8.microbank.common.model.portal.ola.CustomerBbStatementViewModel;
import com.inov8.microbank.common.model.portal.ola.OlaCustomerTxLimitViewModel;
import com.inov8.microbank.common.model.portal.ola.SettlementBbStatementViewModel;
import com.inov8.microbank.common.model.portal.reportmodule.WHTAgentReportViewModel;
import com.inov8.microbank.common.model.veriflymodule.TaggedAgentTransactionModel;
import com.inov8.microbank.common.model.veriflymodule.TaggedAgentTransactionsVOModel;
import com.inov8.microbank.common.model.veriflymodule.TaggedAgentsListViewModel;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.server.dao.agentgroup.AgentTaggingViewDAO;
import com.inov8.microbank.server.dao.agentgroup.TaggedAgentTransactionDetailDAO;
import com.inov8.microbank.server.dao.agentgroup.TaggedAgentsViewDAO;
import com.inov8.microbank.server.dao.portal.ola.AgentBBStatementViewDAO;
import com.inov8.microbank.server.dao.portal.ola.BbStatementAllViewDao;
import com.inov8.microbank.server.dao.portal.ola.CustomerBbStatementViewDao;
import com.inov8.microbank.server.dao.portal.ola.OlaCustomerTxLimitViewDao;
import com.inov8.microbank.server.dao.portal.ola.SettlementBbStatementViewDao;
import com.inov8.microbank.server.dao.transactionmodule.TransactionDetailMasterDAO;
import com.inov8.ola.util.EncryptionUtil;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : May 17, 2013 3:27:19 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class PortalOlaManagerImpl implements PortalOlaManager
{	
	private final Log logger = LogFactory.getLog(PortalOlaManagerImpl.class);

    //Autowired
    private OlaCustomerTxLimitViewDao olaCustomerTxLimitViewDao;
    //Autowired
    private CustomerBbStatementViewDao customerBbStatementViewDao;
    //Autowired
    private SettlementBbStatementViewDao settlementBbStatementViewDao;
    //Autowired
    private BbStatementAllViewDao bbStatementAllViewDao;
    @Autowired
	private TaggedAgentsViewDAO taggedAgentsDAO;

    
	private AgentBBStatementViewDAO agentBBStatementViewDAO;
	@Autowired
	private AgentTaggingViewDAO agentTaggingViewDAO;
	@Autowired
	private TransactionDetailMasterDAO transactionDetailMasterDAO;
	@Autowired
	private TaggedAgentTransactionDetailDAO taggedAgentTransactionDetailDAO;
	
   

	public void setTaggedAgentTransactionDetailDAO(
			TaggedAgentTransactionDetailDAO taggedAgentTransactionDetailDAO) {
		this.taggedAgentTransactionDetailDAO = taggedAgentTransactionDetailDAO;
	}

	public void setTransactionDetailMasterDAO(
			TransactionDetailMasterDAO transactionDetailMasterDAO) {
		this.transactionDetailMasterDAO = transactionDetailMasterDAO;
	}

	public void setAgentTaggingViewDAO(AgentTaggingViewDAO agentTaggingViewDAO) {
		this.agentTaggingViewDAO = agentTaggingViewDAO;
	}

	public PortalOlaManagerImpl()
    {
    }

    @Override
	public SearchBaseWrapper searchTaggingParentDetail(
			SearchBaseWrapper searchBaseWrapper) {
    	
    	AgentTaggingViewModel agentTaggingViewModel = this.agentTaggingViewDAO.findByPrimaryKey(searchBaseWrapper.getBasePersistableModel().getPrimaryKey());
		searchBaseWrapper.setBasePersistableModel(agentTaggingViewModel);
		return searchBaseWrapper;
	}
    
    @Override
    public SearchBaseWrapper searchAgentBBStatementView( SearchBaseWrapper wrapper ) throws FrameworkCheckedException
    {
        AgentBBStatementViewModel model = (AgentBBStatementViewModel) wrapper.getBasePersistableModel();
        CustomList<AgentBBStatementViewModel> customList = agentBBStatementViewDAO.findByExample( model, wrapper.getPagingHelperModel(), wrapper.getSortingOrderMap(), wrapper.getDateRangeHolderModel() );
        wrapper.setCustomList( customList );
        return wrapper;
    }

    @Override
    public List<OlaCustomerTxLimitViewModel> searchOlaCustomerTxLimitView( SearchBaseWrapper wrapper ) throws FrameworkCheckedException
    {
        List<OlaCustomerTxLimitViewModel> list = null;

        OlaCustomerTxLimitViewModel olaCustomerTxLimitViewModel = (OlaCustomerTxLimitViewModel) wrapper.getBasePersistableModel();
        CustomList<OlaCustomerTxLimitViewModel> customList = olaCustomerTxLimitViewDao.findByExample( olaCustomerTxLimitViewModel, wrapper.getPagingHelperModel(), wrapper.getSortingOrderMap() );
        wrapper.setCustomList( customList );

        if( customList != null )
        {
            list = customList.getResultsetList();
        }

        return list;
    }

    @Override
    public SearchBaseWrapper searchCustomerBbStatementView( SearchBaseWrapper wrapper ) throws FrameworkCheckedException
    {
        CustomerBbStatementViewModel model = (CustomerBbStatementViewModel) wrapper.getBasePersistableModel();
        CustomList<CustomerBbStatementViewModel> customList = customerBbStatementViewDao.findByExample( model, wrapper.getPagingHelperModel(), wrapper.getSortingOrderMap(), wrapper.getDateRangeHolderModel() );
        wrapper.setCustomList( customList );
        return wrapper;
    }

    @Override
    public SearchBaseWrapper searchSettlementBbStatementView( SearchBaseWrapper wrapper ) throws FrameworkCheckedException
    {
        SettlementBbStatementViewModel model = (SettlementBbStatementViewModel) wrapper.getBasePersistableModel();
        CustomList<SettlementBbStatementViewModel> customList = settlementBbStatementViewDao.searchSettlementbbStatement(wrapper);
        customList.setPagingHelperModel(wrapper.getPagingHelperModel());
        //CustomList<SettlementBbStatementViewModel> customList = settlementBbStatementViewDao.findByExample( model, wrapper.getPagingHelperModel(), wrapper.getSortingOrderMap(), wrapper.getDateRangeHolderModel() );
        //settlementBbStatementViewDao.findByExample( model, wrapper.getPagingHelperModel(), wrapper.getSortingOrderMap(), wrapper.getDateRangeHolderModel() );
        wrapper.setCustomList( customList );
        return wrapper;
    }

    @Override
    public  CustomList<SettlementBbStatementViewModel> searchSettlementBbStatementView( SettlementBbStatementViewModel settlementBbStatementViewModel ) throws FrameworkCheckedException
    {
        CustomList<SettlementBbStatementViewModel> customList = settlementBbStatementViewDao.findByExample(settlementBbStatementViewModel);
        return customList;
    }
    
    public Double getBalanceByDate(Date calendar, Long accountId) throws Exception {
    	
    	String balance = settlementBbStatementViewDao.getBalanceByDate(calendar, accountId);
//    	String balance = EncryptionUtil.decryptPin(balanceEnc);
    	
    	if(StringUtil.isNullOrEmpty(balance)) {
    		
    		balance = "0.0";
    		logger.warn("Found Null Balance or Balance not Available.");
    	}
    	
    	return Double.valueOf(balance) ;
    }
    
    @Override
    public  CustomList<BbStatementAllViewModel> searchBbStatementAllView(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
    {
    	BbStatementAllViewModel model = (BbStatementAllViewModel) searchBaseWrapper.getBasePersistableModel();

        CustomList<BbStatementAllViewModel> customList = bbStatementAllViewDao.findByExample(model, null, searchBaseWrapper.getSortingOrderMap(),PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
        return customList;
    }
    
   @Override
    public  SearchBaseWrapper searchTaggedAgents(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
    {
    	 TaggedAgentsListViewModel model = (TaggedAgentsListViewModel) searchBaseWrapper.getBasePersistableModel();
         List<TaggedAgentsListViewModel> list = taggedAgentsDAO.loadTaggedagentReport(model, 
        		 searchBaseWrapper.getPagingHelperModel(),
        		 searchBaseWrapper.getDateRangeHolderModel());
         
         CustomList<TaggedAgentsListViewModel> customList = new CustomList<TaggedAgentsListViewModel>();
         customList.setResultsetList(list);
         searchBaseWrapper.setCustomList( customList );
         return searchBaseWrapper;
    }
    
    public void setTaggedAgentsDAO(TaggedAgentsViewDAO taggedAgentsDAO) {
		this.taggedAgentsDAO = taggedAgentsDAO;
	}

	public void setOlaCustomerTxLimitViewDao( OlaCustomerTxLimitViewDao olaCustomerTxLimitViewDao )
    {
        this.olaCustomerTxLimitViewDao = olaCustomerTxLimitViewDao;
    }

    public void setCustomerBbStatementViewDao( CustomerBbStatementViewDao customerBbStatementViewDao )
    {
        this.customerBbStatementViewDao = customerBbStatementViewDao;
    }

    public void setSettlementBbStatementViewDao( SettlementBbStatementViewDao settlementBbStatementViewDao )
    {
        this.settlementBbStatementViewDao = settlementBbStatementViewDao;
    }

	public void setAgentBBStatementViewDAO(
			AgentBBStatementViewDAO agentBBStatementViewDAO) {
		this.agentBBStatementViewDAO = agentBBStatementViewDAO;
	}

	public void setBbStatementAllViewDao(BbStatementAllViewDao bbStatementAllViewDao) {
		this.bbStatementAllViewDao = bbStatementAllViewDao;
	}

	@Override
	public TaggedAgentsListViewModel searchTaggedAgentDetail(
			String taggedAgentId) throws FrameworkCheckedException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public SearchBaseWrapper searchTaggedAgentTransactionDetail(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		
		TaggedAgentTransactionModel model = new TaggedAgentTransactionModel();
		model = (TaggedAgentTransactionModel) searchBaseWrapper.getBasePersistableModel();
		CustomList <TaggedAgentTransactionModel> list = (CustomList<TaggedAgentTransactionModel>) taggedAgentTransactionDetailDAO.getTaggedAgentTransactions(model, searchBaseWrapper.getPagingHelperModel(),searchBaseWrapper.getDateRangeHolderModel());
		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;
	}
	

    @Override
    public List<CustomerBbStatementViewModel> searchBBStatementViewByPaymentModeId(CustomerBbStatementViewModel customerBbStatementViewModel,SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
    {
        return customerBbStatementViewDao.searchBBStatementViewByPaymentModeId(customerBbStatementViewModel,searchBaseWrapper);
    }
	
}
