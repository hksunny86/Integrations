package com.inov8.microbank.server.dao.dailyjob;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.TopupSchedulerExecModel;
import com.inov8.microbank.common.model.postedrransactionreportmodule.PostedTransactionReportModel;
import com.inov8.microbank.common.util.StringUtil;

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
public class TopupSchedulerExecHibernateDAO
extends BaseHibernateDAO<TopupSchedulerExecModel, Long, TopupSchedulerExecDAO> implements TopupSchedulerExecDAO {
	public TopupSchedulerExecHibernateDAO()	{	}

	@SuppressWarnings("unchecked")
	public Map<Long, String> getAllDisputedEntries(List<Long> trxCodesList){
		
		Map<Long, String> rrnCodeMap = new HashMap<Long, String>();
//		response_code <> '00' and [Response code not needed in query because i8 needs to verify response code of all disputed entries]
		String trxCodesString = StringUtil.getCommaSeparatedStringFromLongList(trxCodesList);
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PostedTransactionReportModel.class);
		detachedCriteria.add(Restrictions.in("transactionCode.transactionCodeId", trxCodesList) );
		List<PostedTransactionReportModel> postedTransactionsList = getHibernateTemplate().findByCriteria( detachedCriteria );
		
		List<String> rrnList = new ArrayList<String>();
		
		for (PostedTransactionReportModel model : postedTransactionsList) {
			String refCode = model.getRefCode();
			if(refCode != null && refCode.length() > 12){//when response code is 404, then length is 20.
				refCode = refCode.substring(8);
			}
			rrnList.add(refCode);
		}
		
		String rrnString = StringUtil.getCommaSeparatedStringFromList(rrnList);
		
//		String sql = "select * from phx_transaction_log where substr(rrn, 9) IN ( " + rrnString + ")";
		String sql = " select posted_trx_rep.transaction_code_id, tlog.rrn, tlog.response_code " + 
					 " from phx_transaction_log tlog, posted_transaction_report posted_trx_rep " + 
					 " WHERE posted_trx_rep.ref_code = "+
					 " CASE WHEN length(posted_trx_rep.ref_code) = 20 then rrn " + 
					 " else SUBSTR(rrn, 9) " +
					 " end " +
					 " and substr(rrn, 9) IN ( " + rrnString + " )";
		
//					 " where substr(rrn, 9) = posted_trx_rep.ref_code " + 
//					 " and substr(rrn, 9) IN ( " + rrnString + " )";
		
//		getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery();
		Session session = getSession();
        Query query  = session.createSQLQuery(sql);

		List<?> result = query.list();
		SessionFactoryUtils.releaseSession(session, getSessionFactory());
		
		for(Object obj : result){
			Object[] objArray = (Object[])obj;
			
			Long trxCodeId = ((BigDecimal)objArray[0]).longValue();
			String rrn = (String)objArray[1];
			String responseCode = (String)objArray[2];
			
			rrnCodeMap.put(trxCodeId, responseCode);
		}
		
		return rrnCodeMap;
	}
	
	/*public List<String> getFailedTransactionstrxCodesList(){
		String sql = new String();
		
		sql = " SELECT ";
	}*/
}
