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
import com.inov8.microbank.common.model.allpaymodule.RegionalHeadSummaryViewModel;
import com.inov8.microbank.server.dao.allpaymodule.RegionalHeadSummaryViewDAO;

public class RegionalHeadSummaryViewHibernateDAO 
extends BaseHibernateDAO<RegionalHeadSummaryViewModel, Long, RegionalHeadSummaryViewDAO>
implements RegionalHeadSummaryViewDAO
{
	
	
	public SearchBaseWrapper getRegionalHeadSummary( SearchBaseWrapper searchBaseWrapper )
	{
		RegionalHeadSummaryViewModel distHeadModel = (RegionalHeadSummaryViewModel)searchBaseWrapper.getBasePersistableModel() ;
		DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		DateFormat formatInput = new SimpleDateFormat("dd/MM/yyyy");
		
//		String hql = "SELECT strtAcc.accountId, strtAcc.startDayBalance, endAcc.endDayBalance, endAcc.accountNumber "
//			+ " FROM AccountsStatsRangeListViewModel strtAcc, AccountsStatsRangeListViewModel endAcc " 
//			+ " WHERE strtAcc.accountId = endAcc.accountId AND strtAcc.statsDate  between  '" + dateStr
//			+ "' AND '" + endDateStr + "' " ;
	 
				
//				"FROM ShipmentModel sm, ProductModel pm WHERE pm.productId = ? and"
//		        + " sm.relationProductIdProductModel.productId = pm.productId AND sm.purchaseDate = ( select MIN(smm.purchaseDate) from ShipmentModel smm"
//		        + " where smm.active = true and smm.outstandingCredit >= ?"
//		        + " and smm.relationProductIdProductModel.productId = pm.productId and Decode( smm.expiryDate, null, sysdate , to_date(smm.expiryDate)) >= sysdate ) " ;


		if( distHeadModel.getEndDate() == null )
			distHeadModel.setEndDate( format.format(new Date()) ) ;
		if( distHeadModel.getStartDate() == null )
			distHeadModel.setStartDate( format.format(new Date()) ) ;
		
		
		String hql;
		try
		{
			hql = " SELECT retailerId, regionalHeadId, regionalHeadUserId, accountNick, accountNo, sum(stockReceived), region, " +
			" location, SUM(retailerIssuedAmount) FROM RegionalHeadSummaryViewModel WHERE to_date(txDate,'mm/dd/yyyy') >= to_date('" + format.format( formatInput.parse(distHeadModel.getStartDate()) ) + "','mm/dd/yyyy')  AND to_date(txDate,'mm/dd/yyyy') <= to_date('" +
			format.format( formatInput.parse(distHeadModel.getEndDate()) ) + "','mm/dd/yyyy')  AND regionalHeadId = " + distHeadModel.getRegionalHeadId() +
			" GROUP BY  retailerId, regionalHeadId, regionalHeadUserId, accountNick, accountNo, region, location ";
		}
		catch (ParseException e)
		{
			hql = "" ;
			e.printStackTrace();
		}

		
//		 IQuery q = session.CreateQuery(hql.ToString());
//		   q.SetMaxResults(resultLimit);
//
//		   return q.List();

		
		List<Object> returnList = this.getHibernateTemplate().find(hql, new Object[]{}) ;
		List<RegionalHeadSummaryViewModel> distHeadList = new ArrayList<RegionalHeadSummaryViewModel>();
		
		for( Object summaryModel : returnList )
		{
			RegionalHeadSummaryViewModel distHead = new RegionalHeadSummaryViewModel();
			
			distHead.setRetailerId( (Long)((Object[])summaryModel)[0] ) ;
			distHead.setRegionalHeadId( (Long)((Object[])summaryModel)[1] ) ;
			distHead.setRegionalHeadUserId( (String)((Object[])summaryModel)[2] ) ;
			distHead.setAccountNick( (String)((Object[])summaryModel)[3] ) ;
			distHead.setAccountNo( (String)((Object[])summaryModel)[4] ) ;
			distHead.setStockReceived( (Double)((Object[])summaryModel)[5] ) ;
			distHead.setRegion( (String)((Object[])summaryModel)[6] ) ;
			distHead.setLocation( (String)((Object[])summaryModel)[7] ) ;
			distHead.setRetailerIssuedAmount( (Double)((Object[])summaryModel)[8] ) ;
			
			distHeadList.add(distHead) ;
		}
		
		CustomList<RegionalHeadSummaryViewModel> customList = new CustomList<RegionalHeadSummaryViewModel>();
		customList.setResultsetList(distHeadList) ;
		
		searchBaseWrapper.setCustomList(customList);
		
		return searchBaseWrapper;
		
	}

	
	
	
}
