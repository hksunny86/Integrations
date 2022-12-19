package com.inov8.microbank.server.service.commissionmodule;

/** 	
 * @author 					Jawwad Farooq
 * Project Name: 			Microbank
 * Creation Date: 			December 2008  			
 * Description:				
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.DateUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.model.AllpayCommissionRateModel;
import com.inov8.microbank.common.model.AllpayCommissionTransactionModel;
import com.inov8.microbank.common.model.CommissionRateModel;
import com.inov8.microbank.common.model.CommissionTransactionModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.UnsettledAgentCommModel;
import com.inov8.microbank.common.util.AllPayCommissionReasonConstants;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.TransactionProductEnum;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.dao.allpaymodule.AllPayCommissionTransactionDAO;
import com.inov8.microbank.server.dao.allpaymodule.AllpayCommissionRatesDAO;
import com.inov8.microbank.server.service.AllpayModule.AllpayCommissionRatesManager;
import com.inov8.microbank.server.service.distributormodule.DistributorContactManager;


public class AllPayCommissionManagerImpl implements CommissionManager
{
	private AllpayCommissionRatesManager allpayCommissionRatesManager;
	private AllpayCommissionRatesDAO allpayCommissionRatesDAO;
	private AllPayCommissionTransactionDAO allpayCommissionTransactionDAO;
	private CommissionManager commissionManager ;
	private DistributorContactManager distributorContactManager;
	

	public CommissionWrapper calculateCommission(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException
	{		
		
		CommissionWrapper commissionWrapper = this.commissionManager.calculateCommission(workFlowWrapper);
		List<CommissionRateModel> commissionRateList = (List<CommissionRateModel>) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_RATE_LIST);
		
		if( commissionRateList != null && commissionRateList.size() > 0 )
		{
		
			List<AllpayCommissionRateModel> ruleList = this.getCommissionRule(workFlowWrapper) ;
			List<AllpayCommissionTransactionModel> commissionTransactionList = new ArrayList<AllpayCommissionTransactionModel>() ;
			CommissionAmountsHolder commissionAmountsHolder = (CommissionAmountsHolder)commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
			
			if( ruleList != null && ruleList.size() > 0 )
			{
				for( AllpayCommissionRateModel allpayCommissionRateModel : ruleList )
				{		
					
					if( (allpayCommissionRateModel.getActive() && allpayCommissionRateModel.getToDate() != null && !(new Date().after(DateUtils.getDayEndDate(allpayCommissionRateModel.getToDate()))))
				    		  || (allpayCommissionRateModel.getActive() && allpayCommissionRateModel.getToDate() == null &&
				       (allpayCommissionRateModel.getFromDate() != null && !(new Date().before(DateUtils.getDayStartDate(allpayCommissionRateModel.getFromDate()))))))
				      {
					
						AllpayCommissionTransactionModel allpayCommTx = new AllpayCommissionTransactionModel() ;
						
						if( allpayCommissionRateModel.getDistributorId() == null )
							allpayCommTx.setDistributorId( workFlowWrapper.getFromRetailerContactModel().getRetailerIdRetailerModel().getDistributorId() ) ;
						else
							allpayCommTx.setDistributorId( allpayCommissionRateModel.getDistributorId() ) ;
						
						if( allpayCommissionRateModel.getRetailerId() == null )
							allpayCommTx.setRetailerId( workFlowWrapper.getFromRetailerContactModel().getRetailerId() ) ;
						else							
							allpayCommTx.setRetailerId( allpayCommissionRateModel.getRetailerId() ) ;
						
						allpayCommTx.setNationalDistributorId( allpayCommissionRateModel.getNationalDistributorId() ) ;
						allpayCommTx.setDistributorShare( allpayCommissionRateModel.getDistributorRate() ) ;
						allpayCommTx.setRetailerShare( allpayCommissionRateModel.getRetailerRate() ) ;
						allpayCommTx.setNationalDistShare( allpayCommissionRateModel.getNationalDistributorRate() ) ;
						allpayCommTx.setAllpayCommissionReasonId(allpayCommissionRateModel.getAllpayCommissionReasonId());
						allpayCommTx.setCreatedBy(ThreadLocalAppUser.getAppUserModel().getAppUserId());
						allpayCommTx.setUpdatedBy(ThreadLocalAppUser.getAppUserModel().getAppUserId());
						allpayCommTx.setCreatedOn(new Date());
						allpayCommTx.setUpdatedOn(new Date());
						allpayCommTx.setTransactionAmount(commissionAmountsHolder.getTransactionAmount());
						allpayCommTx.setProductId(allpayCommissionRateModel.getProductId());
						allpayCommTx.setDistributorCommSettled(false);
						allpayCommTx.setRetailerCommSettled(false);
						allpayCommTx.setNationalDistCommSettled(false);
						
						if( allpayCommissionRateModel.getAllpayCommissionReasonId().longValue() == AllPayCommissionReasonConstants.ALLPAY_SERVICE_CHARGES_COMMISSION.longValue() )
						{
							Double serviceCharges = commissionAmountsHolder.getServiceCharges() ;
							
							if( serviceCharges > 0 )
							{
								allpayCommTx.setNationalDistCalculatedComm(this.calculateVariableCommissionAmount(allpayCommissionRateModel.getNationalDistributorRate(), serviceCharges));
								allpayCommTx.setDistributorCalculatedComm(this.calculateVariableCommissionAmount(allpayCommissionRateModel.getDistributorRate(), serviceCharges));
								allpayCommTx.setRetailerCalculatedComm(this.calculateVariableCommissionAmount(allpayCommissionRateModel.getRetailerRate(), serviceCharges));
								
								commissionTransactionList.add(allpayCommTx);
								commissionAmountsHolder.setI8ServiceCharges(commissionAmountsHolder.getI8ServiceCharges() - (allpayCommTx.getNationalDistCalculatedComm()
										+ allpayCommTx.getRetailerCalculatedComm() + allpayCommTx.getDistributorCalculatedComm())); 
							}					
						}
						else if( allpayCommissionRateModel.getAllpayCommissionReasonId().longValue() == AllPayCommissionReasonConstants.ALLPAY_SUPPLIER_COMMISSION.longValue() )
						{
							Double supplierCommission = commissionAmountsHolder.getSupplierCharges() ;
							
							if( supplierCommission > 0 )
							{
								allpayCommTx.setNationalDistCalculatedComm(this.calculateVariableCommissionAmount(allpayCommissionRateModel.getNationalDistributorRate(), supplierCommission));
								allpayCommTx.setDistributorCalculatedComm(this.calculateVariableCommissionAmount(allpayCommissionRateModel.getDistributorRate(), supplierCommission));
								allpayCommTx.setRetailerCalculatedComm(this.calculateVariableCommissionAmount(allpayCommissionRateModel.getRetailerRate(), supplierCommission));
							
								commissionTransactionList.add(allpayCommTx);
								commissionAmountsHolder.setI8SupplierCharges(commissionAmountsHolder.getI8SupplierCharges() - (allpayCommTx.getNationalDistCalculatedComm()
										+ allpayCommTx.getRetailerCalculatedComm() + allpayCommTx.getDistributorCalculatedComm()));
							}
						}
						
						
					}
				}
				commissionWrapper.putObject(CommissionConstantsInterface.ALLPAY_COMMISSION_TRANSACTION_LIST, (Serializable)commissionTransactionList);
			}
		}
		
		return commissionWrapper;
	}
	
	private List<AllpayCommissionRateModel> getCommissionRule(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException
	{
		Long nationalDistributorID = null ;
		Long distributorID = workFlowWrapper.getFromRetailerContactModel().getRetailerIdRetailerModel().getDistributorId() ;
		Long retailerID = workFlowWrapper.getFromRetailerContactModel().getRetailerId() ;
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		BaseWrapper baseWrapper = new BaseWrapperImpl() ;
		
		baseWrapper = this.distributorContactManager.getNationalDistributor() ;
		if( baseWrapper.getBasePersistableModel() != null )
			nationalDistributorID = ((DistributorModel)baseWrapper.getBasePersistableModel()).getDistributorId() ;
		
		System.out.println("-----+> National Distributor Id :  " + nationalDistributorID );
		
		AllpayCommissionRateModel allPayCommissionRateModel = new AllpayCommissionRateModel();
		allPayCommissionRateModel.setNationalDistributorId(nationalDistributorID);
		allPayCommissionRateModel.setDistributorId(distributorID);
		allPayCommissionRateModel.setRetailerId(retailerID);
		allPayCommissionRateModel.setProductId(workFlowWrapper.getProductModel().getProductId()) ;
		
//		searchBaseWrapper.setBasePersistableModel(allPayCommissionRateModel) ;
//		searchBaseWrapper = this.allpayCommissionRatesManager.searchAllPayCommissionRate(searchBaseWrapper) ;
//		allPayCommissionRateModel = (AllpayCommissionRateModel)searchBaseWrapper.getBasePersistableModel() ;
		
		List<AllpayCommissionRateModel> list = this.allpayCommissionRatesDAO.findByExample(allPayCommissionRateModel, null).getResultsetList() ;
		
		if( list == null || list.size() == 0 )
		{
			//Start searching for some Rule applied at National Distributor and Distributor level
			allPayCommissionRateModel.setRetailerId(null) ;
			
			baseWrapper.setBasePersistableModel(allPayCommissionRateModel);
			
			list = this.allpayCommissionRatesDAO.getNationalDistAndDistLevelComm(baseWrapper) ;
			
			if( list == null || list.size() == 0 )
			{	
				allPayCommissionRateModel.setDistributorId(null) ;
				baseWrapper.setBasePersistableModel(allPayCommissionRateModel);
				
				list = this.allpayCommissionRatesDAO.getNationalDistAndDistLevelComm(baseWrapper) ;
			}
		}
		
		return list;		
	}
	
	private double calculateVariableCommissionAmount(double commissionPercentage, double amount) 
	{
		return (amount * commissionPercentage) / 100;
	}

	
	public void setAllpayCommissionRatesManager(AllpayCommissionRatesManager allpayCommissionRatesManager)
	{
		this.allpayCommissionRatesManager = allpayCommissionRatesManager;
	}


	
	public void setAllpayCommissionRatesDAO(AllpayCommissionRatesDAO allpayCommissionRatesDAO)
	{
		this.allpayCommissionRatesDAO = allpayCommissionRatesDAO;
	}

	public void setAllpayCommissionTransactionDAO(AllPayCommissionTransactionDAO allpayCommissionTransactionDAO)
	{
		this.allpayCommissionTransactionDAO = allpayCommissionTransactionDAO;
	}

	public void setCommissionManager(CommissionManager commissionManager)
	{
		this.commissionManager = commissionManager;
	}

	public void setDistributorContactManager(DistributorContactManager distributorContactManager)
	{
		this.distributorContactManager = distributorContactManager;
	}

	public SearchBaseWrapper getCommissionTransactionModel(SearchBaseWrapper baseWrapper) throws FrameworkCheckedException {
		throw new FrameworkCheckedException("Operation Not Supported...");
	}

	public Boolean removeCommissionTransactionModel(CommissionTransactionModel commissionTxModel) throws FrameworkCheckedException {
		throw new FrameworkCheckedException("Operation Not Supported...");
	}

	@Override
	public BaseWrapper loadAgentCommission(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		

		return commissionManager.loadAgentCommission(baseWrapper);
	}

	@Override
	public SearchBaseWrapper getCommissionRateData(CommissionRateModel commissionRateModel) {
		// TODO Auto-generated method stub
		return commissionManager.getCommissionRateData(commissionRateModel);
	}

	@Override
	public Boolean updateCommissionTransaction(Boolean isSettled, Boolean isPosted, Object[] params, TransactionProductEnum productEnum, Integer legNumber) {

		return commissionManager.updateCommissionTransaction(isSettled, isPosted, params, productEnum, legNumber);
	}

	@Override
	public CommissionAmountsHolder loadCommissionDetails(Long transactionDetailId) throws FrameworkCheckedException {
		throw new FrameworkCheckedException("Operation Not Supported...");
	}
	
	@Override
	public CommissionAmountsHolder loadCommissionDetailsUnsettled(Long transactionDetailId) throws FrameworkCheckedException {
		throw new FrameworkCheckedException("Operation Not Supported...");
	}

	public void calculateHierarchyCommission(WorkFlowWrapper workFlowWrapper)
			throws FrameworkCheckedException {
	}

	@Override
	public void makeAgent2CommissionSettlement(WorkFlowWrapper wrapper) throws Exception {
		throw new FrameworkCheckedException("Operation Not Supported...");
	}

	@Override
	public void saveUnsettledCommission(UnsettledAgentCommModel model, Long agentAppUserId) throws FrameworkCheckedException {
		throw new FrameworkCheckedException("Operation Not Supported...");
	}
	
	@Override
	public SearchBaseWrapper searchUnsettledAgentCommission(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		throw new FrameworkCheckedException("Operation Not Supported...");		
	}
}
