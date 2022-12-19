package com.inov8.microbank.server.dao.allpaymodule;

import java.text.SimpleDateFormat;
import java.util.List;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.AllpayCommissionRateModel;

public class AllpayCommissionRatesHibernateDAO extends
		BaseHibernateDAO<AllpayCommissionRateModel, Long, AllpayCommissionRatesDAO> implements AllpayCommissionRatesDAO
{

	public boolean getDuplicateCommissionRateRecords(AllpayCommissionRateModel allpayCommissionRateModel)
	{

		
		//String hql = "from CommissionRateModel crm where crm.commissionRateId != "+commissionRateModel.getCommissionRateId()+" and crm.relationProductIdProductModel.productId="+commissionRateModel.getProductId()+" and crm.relationCommissionReasonIdCommissionReasonModel.commissionReasonId="+commissionRateModel.getCommissionReasonId()+" and crm.relationCommissionStakeholderIdCommissionStakeholderModel.commissionStakeholderId="+commissionRateModel.getCommissionStakeholderId()+" and crm.relationCommissionTypeIdCommissionTypeModel.commissionTypeId="+commissionRateModel.getCommissionTypeId()+" and ";

		String hQL1 = "from AllpayCommissionRateModel crm where crm.relationProductIdProductModel.productId="+allpayCommissionRateModel.getProductId()+" and " +
				"crm.relationDistributorIdDistributorModel.distributorId = "+ allpayCommissionRateModel.getDistributorId() + " and " +
						"crm.relationRetailerIdRetailerModel.retailerId = " + allpayCommissionRateModel.getRetailerId() + " and " +
						"crm.relationAllpayCommissionReasonIdAllpayCommissionReasonModel.allpayCommissionReasonId = "+ allpayCommissionRateModel.getAllpayCommissionReasonId()+ 
						" and crm.relationNationalDistributorIdDistributorModel.distributorId = "+ allpayCommissionRateModel.getNationalDistributorId() +" and ";
		if(allpayCommissionRateModel.getAllpayCommissionRateId()!=null)
			hQL1 += " crm.allpayCommissionRateId != "+allpayCommissionRateModel.getAllpayCommissionRateId()+" and ";
		
		String hQL2 = hQL1;
		String hQL3 = hQL1;
		String hQL4 = hQL1;
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		String fromdate = sdf.format(allpayCommissionRateModel.getFromDate());
		String toDate = "";
		if(allpayCommissionRateModel.getToDate()!=null)
			toDate = sdf.format(allpayCommissionRateModel.getToDate());
					
		List list = null;
		
		if(allpayCommissionRateModel.getToDate()!=null){			
			//hql += " crm.fromDate =  to_date('"+fromdate+"','mm/dd/yyyy') and crm.toDate = to_date('"+toDate+"','mm/dd/yyyy')";
			hQL1 += " crm.toDate is not null and ";
							
			hQL1 += "((crm.fromDate >= to_date('"+fromdate+"','mm/dd/yyyy') and  crm.toDate >= to_date('"+toDate+"','mm/dd/yyyy') and crm.fromDate <= to_date('"+toDate+"','mm/dd/yyyy')) or ";
			hQL1 += "(crm.fromDate >= to_date('"+fromdate+"','mm/dd/yyyy') and crm.toDate <= to_date('"+toDate+"','mm/dd/yyyy')) or ";
			hQL1 += "(crm.fromDate <= to_date('"+fromdate+"','mm/dd/yyyy') and crm.toDate <= to_date('"+toDate+"','mm/dd/yyyy') and crm.toDate >= to_date('"+fromdate+"','mm/dd/yyyy')) or "; 
			hQL1 += "(crm.fromDate <= to_date('"+fromdate+"','mm/dd/yyyy') and crm.toDate >= to_date('"+toDate+"','mm/dd/yyyy')) or ";
							
			hQL1 +=	"  crm.fromDate = to_date('"+fromdate+"','mm/dd/yyyy') or " +
				    "  crm.toDate = to_date('"+fromdate+"','mm/dd/yyyy') or " +
					"  crm.fromDate = to_date('"+toDate+"','mm/dd/yyyy') or " +
					"  crm.toDate = to_date('"+toDate+"','mm/dd/yyyy'))";
			
//			System.out.println("--------------hQL1----------------");
//			System.out.println(hQL1);
							
			
			list = this.getHibernateTemplate().find(hQL1);
//			System.out.println("------------------------------"+list.size());
			if(list!=null && list.size() > 0)
				return true;
			
			hQL2 += " crm.toDate is null and ";
			hQL2 += " (crm.fromDate <= to_date('"+fromdate+"','mm/dd/yyyy') or crm.fromDate <= to_date('"+toDate+"','mm/dd/yyyy'))";
			
//			System.out.println("--------------hQL2----------------");
//			System.out.println(hQL2);								
			
			list = this.getHibernateTemplate().find(hQL2);
//			System.out.println("------------------------------"+list.size());
			if(list!=null && list.size() > 0)
				return true;				
		}
		else{
			//hql += " crm.fromDate =  to_date('"+fromdate+"','mm/dd/yyyy')";
			hQL3 += "  crm.toDate is not null and";																	
			hQL3 += " (crm.toDate >= to_date('"+fromdate+"','mm/dd/yyyy') or crm.fromDate >= to_date('"+fromdate+"','mm/dd/yyyy'))";

//			System.out.println("--------------hQL3----------------");
//			System.out.println(hQL3);												
			
			list = this.getHibernateTemplate().find(hQL3);
//			System.out.println("------------------------------"+list.size());
			if(list!=null && list.size() > 0)
				return true;				

			hQL4 += " crm.toDate is null and crm.fromDate is not null ";
			
//			System.out.println("--------------hQL4----------------");
//			System.out.println(hQL4);																
			
			list = this.getHibernateTemplate().find(hQL4);
//			System.out.println("------------------------------"+list.size());
			if(list!=null && list.size() > 0)
				return true;																	
		}
		
//		
//		this.getHibernateTemplate().find(hql);
//		
//		System.out.println("*************************"+list.size());
//		
//		if(list!=null && list.size() == 0)
//			return false;
		
		return false;
	}

	public List<AllpayCommissionRateModel> getNationalDistAndDistLevelComm(BaseWrapper baseWrapper)
	{
		AllpayCommissionRateModel allpayCommissionRateModel = (AllpayCommissionRateModel)baseWrapper.getBasePersistableModel() ;
		
		System.out.println( " ------> National Distributor id : " + allpayCommissionRateModel.getNationalDistributorId() );
		System.out.println( " ------> Distributor id : " + allpayCommissionRateModel.getDistributorId() );
		System.out.println( " ------> Retailer id : " + allpayCommissionRateModel.getRetailerId() );
		System.out.println( " ------> Product id : " + allpayCommissionRateModel.getProductId() );
		
		String hql = " FROM AllpayCommissionRateModel acr WHERE acr.relationProductIdProductModel.productId = "
			+ " ? AND acr.relationRetailerIdRetailerModel IS NULL " ;
		
		if( allpayCommissionRateModel.getNationalDistributorId() != null )
		{
			hql += " AND acr.relationNationalDistributorIdDistributorModel.distributorId = ? " ;
		}
		if( allpayCommissionRateModel.getDistributorId() != null )
		{
			hql += " AND acr.relationDistributorIdDistributorModel.distributorId = " + allpayCommissionRateModel.getDistributorId() ;
		}
		else
		{
			hql += " AND acr.relationDistributorIdDistributorModel.distributorId IS NULL " ;
		}
		
//		Criteria commissionRateCriteria = this.getSession().createCriteria( AllpayCommissionRateModel.class ) ;
//		Criteria productCriteria = commissionRateCriteria.createCriteria( "relationProductIdProductModel" ) ;
//		//Criteria retailerCriteria = commissionRateCriteria.createCriteria( "relationRetailerIdRetailerModel" ) ;
//		
//		if( allpayCommissionRateModel.getNationalDistributorId() != null )
//		{
//			Criteria nationalDistributorCriteria = commissionRateCriteria.createCriteria( "relationNationalDistributorIdDistributorModel" ) ;
//			Criterion nationalDistCri = Restrictions.like("distributorId", allpayCommissionRateModel.getNationalDistributorId());
//			nationalDistributorCriteria.add(nationalDistCri) ;
//		}
//		if( allpayCommissionRateModel.getDistributorId() != null )
//		{
//			Criteria distributorCriteria = commissionRateCriteria.createCriteria( "relationDistributorIdDistributorModel" ) ;
//			Criterion distCri = Restrictions.like("distributorId", allpayCommissionRateModel.getDistributorId() ) ;
//			distributorCriteria.add(distCri) ;
//		}
//		else
//		{
//			Criteria distributorCriteria = commissionRateCriteria.createCriteria( "relationDistributorIdDistributorModel" ) ;
//			Criterion disCri = Restrictions.isNull("distributorId" ) ;
//			distributorCriteria.add(disCri) ;
//		}
//		if( allpayCommissionRateModel.getProductId() != null )
//		{
//			Criterion prodCri = Restrictions.like("productId", allpayCommissionRateModel.getProductId() ) ;
//			productCriteria.add(prodCri) ;
//		}
//		
//		Criterion retCri = Restrictions.isNull("relationRetailerIdRetailerModel" ) ;
//		commissionRateCriteria.add(retCri) ;
		
		System.out.println( "-----------> HQL " + hql );
		
		List<AllpayCommissionRateModel> results = this.getHibernateTemplate().find(hql, new Object[]{allpayCommissionRateModel.getProductId(), allpayCommissionRateModel.getNationalDistributorId()}) ;
		
		System.out.println( "Results : " + results.size() );
		
		return results;
	}

}
