package com.inov8.microbank.server.dao.allpaymodule.hibernate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.allpaymodule.DistHeadSummaryViewModel;
import com.inov8.microbank.server.dao.allpaymodule.DistHeadSummaryViewDAO;

public class DistHeadSummaryViewHibernateDAO 
extends BaseHibernateDAO<DistHeadSummaryViewModel, Long, DistHeadSummaryViewDAO>
implements DistHeadSummaryViewDAO
{
	
	public SearchBaseWrapper getDistHeadSummary( SearchBaseWrapper searchBaseWrapper )
	{
		DistHeadSummaryViewModel distHeadModel = (DistHeadSummaryViewModel)searchBaseWrapper.getBasePersistableModel() ;
		
		DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		DateFormat formatInput = new SimpleDateFormat("dd/MM/yyyy");
		
		
		if( distHeadModel.getEndDate() == null )
			distHeadModel.setEndDate( formatInput.format(new Date())) ;
		if( distHeadModel.getStartDate() == null )
			distHeadModel.setStartDate( formatInput.format(new Date())) ;
		
		 
//		String hql = "SELECT strtAcc.accountId, strtAcc.startDayBalance, endAcc.endDayBalance, endAcc.accountNumber "
//			+ " FROM AccountsStatsRangeListViewModel strtAcc, AccountsStatsRangeListViewModel endAcc " 
//			+ " WHERE strtAcc.accountId = endAcc.accountId AND strtAcc.statsDate  between  '" + dateStr
//			+ "' AND '" + endDateStr + "' " ;
	 
				
//				"FROM ShipmentModel sm, ProductModel pm WHERE pm.productId = ? and"
//		        + " sm.relationProductIdProductModel.productId = pm.productId AND sm.purchaseDate = ( select MIN(smm.purchaseDate) from ShipmentModel smm"
//		        + " where smm.active = true and smm.outstandingCredit >= ?"
//		        + " and smm.relationProductIdProductModel.productId = pm.productId and Decode( smm.expiryDate, null, sysdate , to_date(smm.expiryDate)) >= sysdate ) " ;

		
		String hql;
		try
		{
			if( distHeadModel.getStartDate() == null || distHeadModel.getEndDate() == null )
				hql = " SELECT distributorContactId, distributorUserId, accountNick, accountNo, SUM(stockReceived) " +
				" FROM DistHeadSummaryViewModel WHERE txDate >= TO_DATE('" + distHeadModel.getStartDate() + "', 'mm/dd/yyyy') AND txDate <= TO_DATE('" +
				distHeadModel.getEndDate() + "', 'mm/dd/yyyy') AND distributorContactId = " + distHeadModel.getDistributorContactId() +
				" GROUP BY  distributorContactId, distributorUserId, accountNick, accountNo ";
			else
				hql = " SELECT distributorContactId, distributorUserId, accountNick, accountNo, SUM(stockReceived) " +
				" FROM DistHeadSummaryViewModel WHERE txDate >= TO_DATE('" + format.format( formatInput.parse(distHeadModel.getStartDate()) ) + "', 'mm/dd/yyyy') AND txDate <= TO_DATE('" +
				format.format( formatInput.parse(distHeadModel.getEndDate()) ) + "', 'mm/dd/yyyy') AND distributorContactId = " + distHeadModel.getDistributorContactId() +
				" GROUP BY  distributorContactId, distributorUserId, accountNick, accountNo ";
		}
		catch (ParseException e)
		{
			hql = "" ;
			e.printStackTrace();
		}
		
		
		String hqlForStock;
		try
		{
			if( distHeadModel.getStartDate() == null || distHeadModel.getEndDate() == null )
				hqlForStock = " SELECT region, " +
				" location, SUM(headIssuedAmount) FROM DistHeadSummaryViewModel WHERE txDate >= TO_DATE('" + distHeadModel.getStartDate() + "', 'MM/DD/YYYY') AND txDate <= TO_DATE('" +
				distHeadModel.getEndDate() + "', 'MM/DD/YYYY') AND distributorContactId = " + distHeadModel.getDistributorContactId() +
				" GROUP BY region, location ";
			else
				hqlForStock = " SELECT region, " +
				" location, SUM(headIssuedAmount) FROM DistHeadSummaryViewModel WHERE txDate >= TO_DATE('" + format.format( formatInput.parse(distHeadModel.getStartDate()) ) + "', 'MM/DD/YYYY') AND txDate <= TO_DATE('" +
				format.format( formatInput.parse(distHeadModel.getEndDate()) ) + "', 'MM/DD/YYYY') AND distributorContactId = " + distHeadModel.getDistributorContactId() +
				" GROUP BY region, location ";
		}
		catch (ParseException e)
		{
			hqlForStock = "" ;
			e.printStackTrace();
		}

		
//		 IQuery q = session.CreateQuery(hql.ToString());
//		   q.SetMaxResults(resultLimit);
//
//		   return q.List();

		
		List<Object> returnList = this.getHibernateTemplate().find(hql, new Object[]{}) ;		
		List<Object> returnListLocation = this.getHibernateTemplate().find(hqlForStock, new Object[]{}) ;
		
		List<DistHeadSummaryViewModel> distHeadList = new ArrayList<DistHeadSummaryViewModel>();
		
		for( Object summaryModel : returnListLocation )
		{
			DistHeadSummaryViewModel distHead = new DistHeadSummaryViewModel();
			
			if( returnList.get(0) != null )
			{
				distHead.setDistributorContactId( (Long)((Object[])returnList.get(0))[0] ) ;
				distHead.setDistributorUserId( (String)((Object[])returnList.get(0))[1] ) ;
				distHead.setAccountNick( (String)((Object[])returnList.get(0))[2] ) ;
				distHead.setAccountNo( (String)((Object[])returnList.get(0))[3] ) ;
				distHead.setStockReceived( (Double)((Object[])returnList.get(0))[4] ) ;
			}
			
			distHead.setRegion( (String)((Object[])summaryModel)[0] ) ;
			distHead.setLocation( (String)((Object[])summaryModel)[1] ) ;
			distHead.setHeadIssuedAmount( (Double)((Object[])summaryModel)[2] ) ;
			
			distHeadList.add(distHead) ;
		}
		
		CustomList<DistHeadSummaryViewModel> customList = new CustomList<DistHeadSummaryViewModel>();
		customList.setResultsetList(distHeadList) ;
		
		searchBaseWrapper.setCustomList(customList);
		
		return searchBaseWrapper;
		
	}
	

}
