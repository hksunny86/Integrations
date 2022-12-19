package com.inov8.microbank.webapp.action.transactiondetailinfomodule;

import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.AuditLogModel;
import com.inov8.microbank.common.model.CommissionTransactionModel;
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.server.service.transactiondetailinfomodule.TransactionDetInfoListViewManager;

public class TransactionDetailSearchController extends BaseSearchController{

	
	
	private TransactionDetInfoListViewManager transactionDetInfoListViewManager;
	private Long id;
	
	public TransactionDetailSearchController() {

		super.setFilterSearchCommandClass(TransactionCodeModel.class);
	}

	public void setTransactionDetInfoListViewManager(
			TransactionDetInfoListViewManager transactionDetInfoListViewManager) {
		this.transactionDetInfoListViewManager = transactionDetInfoListViewManager;
	}
	@Override
	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object object, HttpServletRequest request, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {
		String  id = ServletRequestUtils.getStringParameter(request, "transactionCodeId");
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		SearchBaseWrapper searchBaseWrapperTransactionDetail = new SearchBaseWrapperImpl();
		SearchBaseWrapper searchBaseWrapperTransaction = new SearchBaseWrapperImpl();
		SearchBaseWrapper searchBaseWrapperCommissionTransaction = new SearchBaseWrapperImpl();
		SearchBaseWrapper searchBaseWrapperAuditLog = new SearchBaseWrapperImpl();
		
		TransactionCodeModel transactionCodeListViewModel =  (TransactionCodeModel)(object);
		//transactionCodeListViewModel.setTransactionCodeId(id);
		List <TransactionModel>listTransaction =null;
		List <TransactionCodeModel>listTransactionCode=null;
		List <TransactionDetailModel>listTransactionDetail=null;
		List <CommissionTransactionModel>listCommissionTransaction=null;
		List <AuditLogModel>listAuditLog=null;
	//////////////////////////////////////////////////////////transaction Code//////////////////////////////	
		transactionCodeListViewModel.setCode(id);
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		searchBaseWrapper.setBasePersistableModel(transactionCodeListViewModel);
		
		
		

		
		searchBaseWrapper = this.transactionDetInfoListViewManager
				.searchTransactionCode(searchBaseWrapper);

		
		
		listTransactionCode =searchBaseWrapper.getCustomList().getResultsetList();
		
		if (!listTransactionCode.isEmpty() && listTransactionCode!=null)
		{
		//////////////////////////////////transaction///////////////////////////////////////
		TransactionCodeModel transactionCodeModel=(TransactionCodeModel) listTransactionCode.get(0);
		
		listTransactionCode.clear();
		listTransactionCode.add(transactionCodeModel);
		{
			request.setAttribute("transactionCodeListViewModelList", listTransactionCode);		
			AuditLogModel auditLogModel = new AuditLogModel();
			auditLogModel.setTransactionCodeId(transactionCodeModel.getTransactionCodeId());
//			searchBaseWrapperAuditLog.setPagingHelperModel(pagingHelperModel);
			searchBaseWrapperAuditLog.setSortingOrderMap(sortingOrderMap);
			searchBaseWrapperAuditLog.setBasePersistableModel(auditLogModel);
			searchBaseWrapperAuditLog = this.transactionDetInfoListViewManager
			.searchAuditLog(searchBaseWrapperAuditLog);
			listAuditLog=searchBaseWrapperAuditLog.getCustomList().getResultsetList();
			if (!listAuditLog.isEmpty() && listAuditLog!=null)
			{	
			request.setAttribute("auditLogListViewModelList", listAuditLog);
			}
			
		}
		
		searchBaseWrapperTransaction.setPagingHelperModel(pagingHelperModel);
		searchBaseWrapperTransaction.setSortingOrderMap(sortingOrderMap);
		TransactionModel transactionModel = new TransactionModel();		
		transactionModel.setTransactionCodeId(transactionCodeModel.getTransactionCodeId());
		searchBaseWrapperTransaction.setBasePersistableModel(transactionModel);
		searchBaseWrapperTransaction = this.transactionDetInfoListViewManager
		.searchTransaction(searchBaseWrapperTransaction);
		listTransaction =searchBaseWrapperTransaction.getCustomList().getResultsetList();
		
		
		//////////////////////////////////////TransactionDetail///////////////////////////////////////
		
		
		if (!listTransaction.isEmpty() && listTransaction!=null)
		{	
			
			TransactionModel transactionListModel=(TransactionModel) listTransaction.get(0);
			//TODO:
			String bankAccNo = transactionListModel.getBankAccountNo();
			if(bankAccNo!=null){
			   bankAccNo = bankAccNo.substring(bankAccNo.length()-5, bankAccNo.length());
			   transactionListModel.setBankAccountNo(bankAccNo);
			}
			listTransaction.clear();
			listTransaction.add(transactionListModel);
			
			request.setAttribute("transactionListViewModel", listTransaction);
		////////////////////////////////////////////////////////////////////////////////////////
		
		
		searchBaseWrapperTransactionDetail.setPagingHelperModel(pagingHelperModel);
		searchBaseWrapperTransactionDetail.setSortingOrderMap(sortingOrderMap);
		TransactionDetailModel transactionDetailModel = new TransactionDetailModel();
		
		TransactionModel transModel=(TransactionModel) listTransaction.get(0);
		transactionDetailModel.setTransactionId(transModel.getTransactionId());
		searchBaseWrapperTransactionDetail.setBasePersistableModel(transactionDetailModel);
		searchBaseWrapperTransactionDetail = this.transactionDetInfoListViewManager
		.searchTransactionDetail(searchBaseWrapperTransactionDetail);
		listTransactionDetail =searchBaseWrapperTransactionDetail.getCustomList().getResultsetList();
		if (!listTransactionDetail.isEmpty()&& listTransactionDetail!=null)
		{
		
			
			request.setAttribute("transactionDetailListViewModel", listTransactionDetail);
		CommissionTransactionModel commissionTransactionModel =new CommissionTransactionModel();
		TransactionDetailModel transDetailModel=(TransactionDetailModel) listTransactionDetail.get(0);
		commissionTransactionModel.setTransactionDetailId(transDetailModel.getTransactionDetailId());
		
		searchBaseWrapperCommissionTransaction.setPagingHelperModel(pagingHelperModel);
		searchBaseWrapperCommissionTransaction.setSortingOrderMap(sortingOrderMap);
		searchBaseWrapperCommissionTransaction.setBasePersistableModel(commissionTransactionModel);
		searchBaseWrapperCommissionTransaction = this.transactionDetInfoListViewManager
		.searchCommissionTransaction(searchBaseWrapperCommissionTransaction);
		listCommissionTransaction =searchBaseWrapperCommissionTransaction.getCustomList().getResultsetList();
		if (!listCommissionTransaction.isEmpty()&& listCommissionTransaction!=null)
		{
		
			/*CommissionTransactionModel commissionTransactionListModel=(CommissionTransactionModel) listCommissionTransaction.get(0);
			
				listCommissionTransaction.clear();
			listCommissionTransaction.add(commissionTransactionListModel);*/
			

			request.setAttribute("commissionTransactionListViewModel", listCommissionTransaction);
		}
		/*if (transDetailModel.getTransactionDetailIdCommissionTransactionModelList()!=null &&transDetailModel.getTransactionDetailIdCommissionTransactionModelList().size()>0)
		{
			commissionTransactionModel=(CommissionTransactionModel)transDetailModel.getTransactionDetailIdCommissionTransactionModelList();
			if (commissionTransactionModel!=null)
			{
				request.setAttribute("commissionTransactionListViewModel", commissionTransactionModel);
			
			}
		}
		*/
		
		
		
		//searchBaseWrapperTransactionDetail = this.transactionDetInfoListViewManager
		//.searchTransactionDetail(searchBaseWrapperTransactionDetail);
		//listTransactionDetail =searchBaseWrapperTransactionDetail.getCustomList().getResultsetList();
		
		}
		}
		///////////////////////////////////////////////////////////////////////////////////////
		
		
		}
		
		return new ModelAndView(getSearchView()/*, "transactionCodeListViewModelList",
				listTransactionCode*/);		

	}
	
	private List getList(SearchBaseWrapper searchBaseWrapper)
	{
		List list=searchBaseWrapper.getCustomList().getResultsetList();
		if (list.isEmpty())
		{
			return null;
			
		}
		
		 return list;	
	}
	
	

}
