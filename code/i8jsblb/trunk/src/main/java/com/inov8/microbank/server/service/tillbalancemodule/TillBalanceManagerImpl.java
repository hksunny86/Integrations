
package com.inov8.microbank.server.service.tillbalancemodule;
       

import org.hibernate.criterion.MatchMode;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.AgentOpeningBalModel;
import com.inov8.microbank.common.model.CashBankMappingModel;
import com.inov8.microbank.server.dao.tillbalancemodule.AgentOpeningBalanceDAO;
import com.inov8.microbank.server.dao.tillbalancemodule.CashBankMappingDAO;

public class TillBalanceManagerImpl implements TillBalanceManager {
	
	private AgentOpeningBalanceDAO agentOpeningBalanceDAO;
	private CashBankMappingDAO cashBankMappingDAO;

	@Override
	public SearchBaseWrapper loadAgentOpeningBalance( SearchBaseWrapper searchBaseWrapper ) throws FrameworkCheckedException {
		ExampleConfigHolderModel configHolderModel = new ExampleConfigHolderModel(false, false, false, MatchMode.EXACT);
		CustomList<AgentOpeningBalModel> list = this.agentOpeningBalanceDAO.findByExample((AgentOpeningBalModel) searchBaseWrapper
				.getBasePersistableModel(), searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap(), searchBaseWrapper.getDateRangeHolderModel(), configHolderModel );
		searchBaseWrapper.setCustomList(list);

		return searchBaseWrapper;
		
	}

	@Override
	public BaseWrapper loadAgentOpeningBalance(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		// TODO Auto-generated method stub
		
		
		AgentOpeningBalModel agentOpeningBalanceModel = this.agentOpeningBalanceDAO.findByPrimaryKey(baseWrapper.
		        getBasePersistableModel().
		        getPrimaryKey());
		    baseWrapper.setBasePersistableModel(agentOpeningBalanceModel);
		    return baseWrapper;
	}

	@Override
	public SearchBaseWrapper loadCashBankMapping(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		CustomList<CashBankMappingModel> list = this.cashBankMappingDAO
		.findByExample((CashBankMappingModel) searchBaseWrapper
				.getBasePersistableModel(), searchBaseWrapper
				.getPagingHelperModel(), searchBaseWrapper
				.getSortingOrderMap());
					searchBaseWrapper.setCustomList(list);
				
		return searchBaseWrapper;
	}

	@Override
	public BaseWrapper loadCashBankMapping(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		
		CashBankMappingModel cashBankMappingModel = this.cashBankMappingDAO.findByPrimaryKey(baseWrapper.
		        getBasePersistableModel().
		        getPrimaryKey());
		    baseWrapper.setBasePersistableModel(cashBankMappingModel);
		    return baseWrapper;
		
	}

	@Override
	public BaseWrapper updateAgentOpeningBalance(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		AgentOpeningBalModel agentOpeningBalanceModel = (AgentOpeningBalModel)baseWrapper.getBasePersistableModel();
		agentOpeningBalanceModel = agentOpeningBalanceDAO.saveOrUpdate(agentOpeningBalanceModel);
		baseWrapper.setBasePersistableModel(agentOpeningBalanceModel);
		return baseWrapper;
	}

	@Override
	public BaseWrapper createAgentOpeningBalance(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		
		AgentOpeningBalModel agentOpeningBalanceModel = (AgentOpeningBalModel)baseWrapper.getBasePersistableModel();
		agentOpeningBalanceModel = agentOpeningBalanceDAO.saveOrUpdate(agentOpeningBalanceModel);
		baseWrapper.setBasePersistableModel(agentOpeningBalanceModel);
		return baseWrapper;	
		
	}

	@Override
	public SearchBaseWrapper searchAgentOpeningBalanceByExample(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		AgentOpeningBalModel agentOpeningBalModel = (AgentOpeningBalModel) searchBaseWrapper.
        getBasePersistableModel();
		
//		DateRangeHolderModel dateRangeModel = new DateRangeHolderModel("cretedOn",agentOpeningBalModel.getCreatedOn(), agentOpeningBalModel.getCreatedOn());
		
		 
		
		CustomList<AgentOpeningBalModel>
        list = this.agentOpeningBalanceDAO.findByExample(
                                                  agentOpeningBalModel,
                                                  searchBaseWrapper.
                                                  getPagingHelperModel(),
                                                  searchBaseWrapper.
                                                  getSortingOrderMap());
    searchBaseWrapper.setCustomList(list);
    return searchBaseWrapper;
	}

	@Override
	public BaseWrapper updateCashBankMapping(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		
		CashBankMappingModel cashBankMappingModel = (CashBankMappingModel)baseWrapper.getBasePersistableModel();
		cashBankMappingModel = cashBankMappingDAO.saveOrUpdate(cashBankMappingModel);
		baseWrapper.setBasePersistableModel(cashBankMappingModel);
		return baseWrapper;
	}

	@Override
	public BaseWrapper createCashBankMapping(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		
		CashBankMappingModel cashBankMappingModel = (CashBankMappingModel)baseWrapper.getBasePersistableModel();
		cashBankMappingModel = cashBankMappingDAO.saveOrUpdate(cashBankMappingModel);
		baseWrapper.setBasePersistableModel(cashBankMappingModel);
		return baseWrapper;
	}

	@Override
	public SearchBaseWrapper searchCashBankMappingByExample(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		CustomList<CashBankMappingModel>
        list = this.cashBankMappingDAO.findByExample( (CashBankMappingModel)
                                                  searchBaseWrapper.
                                                  getBasePersistableModel(),
                                                  searchBaseWrapper.
                                                  getPagingHelperModel(),
                                                  searchBaseWrapper.
                                                  getSortingOrderMap());
    searchBaseWrapper.setCustomList(list);
    return searchBaseWrapper;
	}

	public void setAgentOpeningBalanceDAO(
			AgentOpeningBalanceDAO agentOpeningBalanceDAO) {
		this.agentOpeningBalanceDAO = agentOpeningBalanceDAO;
	}

	public void setCashBankMappingDAO(CashBankMappingDAO cashBankMappingDAO) {
		this.cashBankMappingDAO = cashBankMappingDAO;
	}

	@Override
	public BaseWrapper agentOpeningBalanceRequiresNewTransaction(
			BaseWrapper baseWrapper) throws FrameworkCheckedException {
		AgentOpeningBalModel agentOpeningBalanceModel = (AgentOpeningBalModel)baseWrapper.getBasePersistableModel();
		agentOpeningBalanceModel = agentOpeningBalanceDAO.saveOrUpdate(agentOpeningBalanceModel);
		baseWrapper.setBasePersistableModel(agentOpeningBalanceModel);
		return baseWrapper;	
	}
	
	

}
