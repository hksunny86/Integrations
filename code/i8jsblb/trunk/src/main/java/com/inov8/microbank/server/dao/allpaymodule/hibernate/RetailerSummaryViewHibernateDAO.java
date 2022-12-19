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
import com.inov8.microbank.common.model.allpaymodule.RetailerSummaryViewModel;
import com.inov8.microbank.server.dao.allpaymodule.RetailerSummaryViewDAO;

public class RetailerSummaryViewHibernateDAO extends BaseHibernateDAO<RetailerSummaryViewModel, Long, RetailerSummaryViewDAO> implements RetailerSummaryViewDAO 
{
	
	
	public SearchBaseWrapper getRetailerSummary( SearchBaseWrapper searchBaseWrapper )
	{
		RetailerSummaryViewModel distHeadModel = (RetailerSummaryViewModel)searchBaseWrapper.getBasePersistableModel() ;
		DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		DateFormat formatInput = new SimpleDateFormat("dd/MM/yyyy");
		
//		String hql = " SELECT distributorContactId, distributorUserId, accountNick, accountNo, SUM(stockReceived), region, " +
//        " location, SUM(headIssuedAmount) FROM RetailerSummaryViewModel WHERE txDate >= '" + distHeadModel.getStartDate() + "' AND txDate <= '" +
//        distHeadModel.getEndDate() + "' AND distributorContactId = " + distHeadModel.getDistributorContactId() +
//		" GROUP BY  distributorContactId, distributorUserId, accountNick, accountNo, region, location " ;
		
		
		if( distHeadModel.getEndDate() == null )
			distHeadModel.setEndDate( format.format(new Date()) ) ;
		if( distHeadModel.getStartDate() == null )
			distHeadModel.setStartDate( format.format(new Date()) ) ;
		
		
		String hql;
		try
		{
			hql = "SELECT retailerId, retailerContactId, userId, accountNick, accountNo, sum(stockReceived), region, location, "
			+ " sum(billCollection) FROM RetailerSummaryViewModel WHERE to_date(txDate,'mm/dd/yyyy') >= to_date('" + format.format( formatInput.parse(distHeadModel.getStartDate()) ) + "','mm/dd/yyyy')  AND to_date(txDate,'mm/dd/yyyy') <= to_date('" +
				format.format( formatInput.parse(distHeadModel.getEndDate()) ) + "','mm/dd/yyyy') AND retailerContactId = " + distHeadModel.getRetailerContactId() +
			" GROUP BY  retailerId, retailerContactId, userId, accountNick, accountNo, region, location ";
		}
		catch (ParseException e)
		{
			hql = "" ;
			e.printStackTrace();
		}


		List<Object> returnList = this.getHibernateTemplate().find(hql, new Object[]{}) ;
		List<RetailerSummaryViewModel> distHeadList = new ArrayList<RetailerSummaryViewModel>();
		
		for( Object summaryModel : returnList )
		{
			RetailerSummaryViewModel distHead = new RetailerSummaryViewModel();
			
//			distHead.setTxDate( (String)((Object[])summaryModel)[0] ) ;
			distHead.setRetailerId( (Long)((Object[])summaryModel)[0] ) ;			
			distHead.setRetailerContactId( (Long)((Object[])summaryModel)[1] ) ;
			
			distHead.setUserId( (String)((Object[])summaryModel)[2] ) ;
			distHead.setAccountNick( (String)((Object[])summaryModel)[3] ) ;
			distHead.setAccountNo( (String)((Object[])summaryModel)[4] ) ;
			distHead.setStockReceived( (Double)((Object[])summaryModel)[5] ) ;
			distHead.setRegion( (String)((Object[])summaryModel)[6] ) ;
			distHead.setLocation( (String)((Object[])summaryModel)[7] ) ;
			distHead.setBillCollection( (Double)((Object[])summaryModel)[8] ) ;
			
			distHeadList.add(distHead) ;
		}
		
		CustomList<RetailerSummaryViewModel> customList = new CustomList<RetailerSummaryViewModel>();
		customList.setResultsetList(distHeadList) ;
		
		searchBaseWrapper.setCustomList(customList);
		
		return searchBaseWrapper;
		
	}

	
	
	
}
