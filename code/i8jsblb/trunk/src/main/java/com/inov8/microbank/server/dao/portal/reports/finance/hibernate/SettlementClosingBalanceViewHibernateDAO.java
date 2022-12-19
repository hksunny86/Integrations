package com.inov8.microbank.server.dao.portal.reports.finance.hibernate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.portal.financereportsmodule.SettlementClosingBalanceViewModel;
import com.inov8.microbank.server.dao.portal.reports.finance.SettlementClosingBalanceViewDAO;

/**
 * 
 * @author AtifHu
 * 
 */

public class SettlementClosingBalanceViewHibernateDAO extends
		BaseHibernateDAO<SettlementClosingBalanceViewModel, Long, SettlementClosingBalanceViewDAO>
		implements SettlementClosingBalanceViewDAO {

	@Override
	public List<Object[]>	searchSettlementClosingBalance(SettlementClosingBalanceViewModel settClosingBalViewModel, 
			DateRangeHolderModel dateRangeHolderModel, LinkedHashMap<String, SortingOrder> sortingOrderMap, PagingHelperModel pagingHelperModel) {
		
		List<Object> values=new ArrayList<Object>();
		
		StringBuilder mainQuery = new StringBuilder();
		
		mainQuery.append("select scbv.blbAccountNumber,scbv.ofAccountNumber,scbv.bbAccountTitle, scbv.coreAccountTitle, ");

		mainQuery.append("SUM(scbv.debitMovement), SUM(scbv.creditMovement), scbv.stakeHolderBankInfoId ");

		mainQuery.append("from SettlementClosingBalanceViewModel scbv where scbv.ofAccountNumber <> ? ");
		//and scbv.blbAccountNumber<>null and scbv.ofAccountNumber<>null 
		values.add("-1");
		
		Long accountType = settClosingBalViewModel.getAccountType();
		
		if(accountType!=null && accountType > 1)
		{
			if(accountType==2L)//agent
			{
				mainQuery.append("and scbv.accountType IN( select ola.customerAccountTypeId from OlaCustomerAccountTypeModel ola ");
				mainQuery.append("where ola.isCustomerAccountType=? ) ");
				values.add(false);
			}
			else if(accountType==3L)//customer
			{
				mainQuery.append("and scbv.accountType IN( select ola.customerAccountTypeId from OlaCustomerAccountTypeModel ola ");
				mainQuery.append("where ola.isCustomerAccountType=? ) ");
				values.add(true);
			}
			else if(accountType==4L)//internal
			{
				mainQuery.append("and (scbv.commStakeHolderId <> ? OR scbv.commStakeHolderId IS NULL) and scbv.accountType IS NULL ");
				values.add(50020L);
			}
			else if(accountType==5L)//commission
			{
				mainQuery.append("and scbv.cmshAcctTypeId=? and scbv.commStakeHolderId=? ");
				values.add(3L);
				values.add(50020L);
			}
		}
		if (settClosingBalViewModel.getStakeHolderBankInfoId()!=null && settClosingBalViewModel.getStakeHolderBankInfoId()!=-2L){
			mainQuery.append("and scbv.stakeHolderBankInfoId =? ");
			values.add(settClosingBalViewModel.getStakeHolderBankInfoId());
		}
		if(dateRangeHolderModel.getFromDate()!=null){
			mainQuery.append("and scbv.statsDate >=? ");
			values.add(dateRangeHolderModel.getFromDate());
		}
		if(dateRangeHolderModel.getToDate()!=null){
			mainQuery.append("and scbv.statsDate <=? ");
			values.add(dateRangeHolderModel.getToDate());
		}

		mainQuery.append("group by scbv.blbAccountNumber, scbv.ofAccountNumber, scbv.bbAccountTitle, scbv.coreAccountTitle, scbv.stakeHolderBankInfoId ");
		
		String key	= (String)sortingOrderMap.keySet().toArray()[0];
		SortingOrder sortingOrder = sortingOrderMap.get(key);
		
		mainQuery.append("order by scbv.").append(key).append(" ").append(sortingOrder.name());
		
		List<Object[]>	querylist	=	getHibernateTemplate().find(mainQuery.toString(),values.toArray());
		
		List<Object[]> result	=	new ArrayList<Object[]>();
		
		Object[] columns=new Object[8];
		
		for (Object[] queryObject : querylist)
		{			

			mainQuery = new StringBuilder();
			mainQuery.append("select scbv.openingBalance ");
			mainQuery.append("from SettlementClosingBalanceViewModel scbv where stakeHolderBankInfoId=? ");
			mainQuery.append("and scbv.statsDate =? ");
			values=new ArrayList<Object>();
			values.add(queryObject[6]);
			values.add(dateRangeHolderModel.getFromDate());
			List<Object>	openingBalance	=	getHibernateTemplate().find(mainQuery.toString(),values.toArray());
			

			mainQuery = new StringBuilder();
			mainQuery.append("select scbv.closingBalance ");
			mainQuery.append("from SettlementClosingBalanceViewModel scbv where stakeHolderBankInfoId=? ");
			mainQuery.append("and scbv.statsDate =? ");
			values=new ArrayList<Object>();
			values.add(queryObject[6]);
			values.add(dateRangeHolderModel.getToDate());
			List<Object>	closingBalance	=	getHibernateTemplate().find(mainQuery.toString(),values.toArray());
			
			columns=new Object[8];
			columns[0]=queryObject[0];
			columns[1]=queryObject[1];
			columns[2]=queryObject[2];
			columns[3]=queryObject[3];
			columns[4]=queryObject[4];
			columns[5]=queryObject[5];
			
			if(openingBalance!=null && openingBalance.size()>0 && openingBalance.get(0)!=null){
				Double	val=	(Double)openingBalance.get(0);
				if(val!=null){
					columns[6]=	val.doubleValue();
				}else{
					columns[6]=0.0;
				}
			}
			else{
				columns[6]=0.0;
			}
			if(closingBalance!=null && closingBalance.size()>0 && closingBalance.get(0)!=null){
				Double	val=	(Double)closingBalance.get(0);
				if(val!=null){
					columns[7]=	val.doubleValue();
				}else{
					columns[7]=0.0;
				}
			}
			else{
				columns[7]=0.0;
			}
			
			result.add(columns);
		}
		
		return result;
	}
}