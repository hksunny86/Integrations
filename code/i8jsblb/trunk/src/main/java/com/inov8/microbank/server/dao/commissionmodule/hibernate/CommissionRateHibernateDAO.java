package com.inov8.microbank.server.dao.commissionmodule.hibernate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.vo.product.CommissionRateVO;
import com.inov8.microbank.common.vo.rowmappers.CommissionRateRowMapper;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.CommissionRateModel;
import com.inov8.microbank.server.dao.commissionmodule.CommissionRateDAO;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CommissionRateHibernateDAO
    extends BaseHibernateDAO<CommissionRateModel, Long, CommissionRateDAO>
    implements CommissionRateDAO
{
	private JdbcTemplate jdbcTemplate;
	/**
	 *  Check date overlaping error
	 */
	
	public List<CommissionRateModel> loadCommissionRateList(CommissionRateModel vo){
		logger.info("Starting CommissionRateHibernateDAO.loadCommissionRateList(CommissionRateVO) for Product :: " + vo.getProductId() + " at Time :: " + new Date());
		long isDeleted = 0l;
		if(vo.getIsDeleted())
			isDeleted = 1l;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM COMMISSION_RATE WHERE IS_DELETED = " + isDeleted);
		if(vo.getProductId() != null)
			sql.append(" AND (PRODUCT_ID IS NULL OR PRODUCT_ID = " + vo.getProductId() + " )");

		if(vo.getDeviceTypeId() != null)
			sql.append(" AND (DEVICE_TYPE_ID IS NULL OR DEVICE_TYPE_ID = " + vo.getDeviceTypeId() + " )");

		if(vo.getSegmentId() != null)
			sql.append(" AND (SEGMENT_ID IS NULL OR SEGMENT_ID = " + vo.getSegmentId() + " )");

		if(vo.getDistributorId() != null)
			sql.append(" AND (DISTRIBUTOR_ID IS NULL OR DISTRIBUTOR_ID = " + vo.getDistributorId() + " )");

		if(vo.getMnoId() != null && vo.getMnoId().equals(50028L))
			sql.append(" AND SERVICE_OP_ID = " + vo.getMnoId());
		else
			sql.append(" AND (SERVICE_OP_ID IS NULL OR SERVICE_OP_ID = " + vo.getMnoId() + " )");

		logger.info("Query to CommissionRateHibernateDAO.loadCommissionRateList(): " + sql);
		List<CommissionRateModel> list = (List<CommissionRateModel>) jdbcTemplate.query(sql.toString(), new CommissionRateModel());
		int size = 0;
		if(!list.isEmpty())
			size = list.size();
		logger.info("Total " + size + " Records Fetched in CommissionRateHibernateDAO.loadCommissionRateList(CommissionRateVO) for Product :: " + vo.getProductId() + " at Time :: " + new Date());
		return list;
	}
		public boolean getDuplicateCommissionRateRecords(CommissionRateModel commissionRateModel){
			
			//String hql = "from CommissionRateModel crm where crm.commissionRateId != "+commissionRateModel.getCommissionRateId()+" and crm.relationProductIdProductModel.productId="+commissionRateModel.getProductId()+" and crm.relationCommissionReasonIdCommissionReasonModel.commissionReasonId="+commissionRateModel.getCommissionReasonId()+" and crm.relationCommissionStakeholderIdCommissionStakeholderModel.commissionStakeholderId="+commissionRateModel.getCommissionStakeholderId()+" and crm.relationCommissionTypeIdCommissionTypeModel.commissionTypeId="+commissionRateModel.getCommissionTypeId()+" and ";

			String hQL1 = "from CommissionRateModel crm where crm.relationProductIdProductModel.productId="+commissionRateModel.getProductId()+" and crm.relationCommissionReasonIdCommissionReasonModel.commissionReasonId="+commissionRateModel.getCommissionReasonId()+" and crm.relationCommissionStakeholderIdCommissionStakeholderModel.commissionStakeholderId="+commissionRateModel.getCommissionStakeholderId()+" and crm.relationCommissionTypeIdCommissionTypeModel.commissionTypeId="+commissionRateModel.getCommissionTypeId()+" and ";
			if(commissionRateModel.getCommissionRateId()!=null)
				hQL1 += " crm.commissionRateId != "+commissionRateModel.getCommissionRateId()+" and ";
			
			String hQL2 = hQL1;
			String hQL3 = hQL1;
			String hQL4 = hQL1;
			
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			String fromdate = sdf.format(commissionRateModel.getFromDate());
			String toDate = "";
			if(commissionRateModel.getToDate()!=null)
				toDate = sdf.format(commissionRateModel.getToDate());
						
			List list = null;
			
			if(commissionRateModel.getToDate()!=null){			
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
				
//				System.out.println("--------------hQL1----------------");
//				System.out.println(hQL1);
								
				
				list = this.getHibernateTemplate().find(hQL1);
//				System.out.println("------------------------------"+list.size());
				if(list!=null && list.size() > 0)
					return true;
				
				hQL2 += " crm.toDate is null and ";
				hQL2 += " (crm.fromDate <= to_date('"+fromdate+"','mm/dd/yyyy') or crm.fromDate <= to_date('"+toDate+"','mm/dd/yyyy'))";
				
//				System.out.println("--------------hQL2----------------");
//				System.out.println(hQL2);								
				
				list = this.getHibernateTemplate().find(hQL2);
//				System.out.println("------------------------------"+list.size());
				if(list!=null && list.size() > 0)
					return true;				
			}
			else{
				//hql += " crm.fromDate =  to_date('"+fromdate+"','mm/dd/yyyy')";
				hQL3 += "  crm.toDate is not null and";																	
				hQL3 += " (crm.toDate >= to_date('"+fromdate+"','mm/dd/yyyy') or crm.fromDate >= to_date('"+fromdate+"','mm/dd/yyyy'))";

//				System.out.println("--------------hQL3----------------");
//				System.out.println(hQL3);												
				
				list = this.getHibernateTemplate().find(hQL3);
//				System.out.println("------------------------------"+list.size());
				if(list!=null && list.size() > 0)
					return true;				

				hQL4 += " crm.toDate is null and crm.fromDate is not null ";
				
//				System.out.println("--------------hQL4----------------");
//				System.out.println(hQL4);																
				
				list = this.getHibernateTemplate().find(hQL4);
//				System.out.println("------------------------------"+list.size());
				if(list!=null && list.size() > 0)
					return true;																	
			}
			
//			
//			this.getHibernateTemplate().find(hql);
//			
//			System.out.println("*************************"+list.size());
//			
//			if(list!=null && list.size() == 0)
//				return false;
			
			return false;
		}
		
		@Override
		public boolean isCommissionRangeValid(CommissionRateModel commissionRateModel) throws FrameworkCheckedException{
			
			String hql = " from CommissionRateModel crm where crm.relationProductIdProductModel.productId="+commissionRateModel.getProductId()+" and crm.relationCommissionStakeholderIdCommissionStakeholderModel.commissionStakeholderId="+commissionRateModel.getCommissionStakeholderId()+" and crm.relationCommissionTypeIdCommissionTypeModel.commissionTypeId="+commissionRateModel.getCommissionTypeId()+" and crm.relationSegmentIdSegmentModel.segmentId ="+commissionRateModel.getSegmentId() + " and crm.active = 1";
			StringBuilder validateRangeHql = new StringBuilder(hql+" and ("+commissionRateModel.getRangeStarts()+" between crm.rangeStarts and crm.rangeEnds or "+commissionRateModel.getRangeEnds()+" between crm.rangeStarts and crm.rangeEnds or crm.rangeStarts between "+commissionRateModel.getRangeStarts()+" and "+commissionRateModel.getRangeEnds()+" or crm.rangeEnds between "+commissionRateModel.getRangeStarts()+" and "+commissionRateModel.getRangeEnds()+")");  
			if(commissionRateModel.getCommissionRateId()!=null){
				validateRangeHql.append(" and crm.commissionRateId != "+commissionRateModel.getCommissionRateId()); }
			List list = this.getHibernateTemplate().find(validateRangeHql.toString());
			if(list != null && list.size() > 0){
				throw new FrameworkCheckedException("UniqueKeyViolated");
			}
			else
			{	
				StringBuilder validateRatehql = new StringBuilder(hql+" and crm.rate="+ commissionRateModel.getRate());	
				if(commissionRateModel.getCommissionRateId()!=null){
					validateRatehql.append(" and crm.commissionRateId != "+commissionRateModel.getCommissionRateId()) ;}
				list = this.getHibernateTemplate().find(validateRatehql.toString());
				if(list != null && list.size() > 0){
					throw new FrameworkCheckedException("SlabRateExists");
				}
			}
			return true;
		}

	public void setDataSource(DataSource dataSource)
	{
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
}
