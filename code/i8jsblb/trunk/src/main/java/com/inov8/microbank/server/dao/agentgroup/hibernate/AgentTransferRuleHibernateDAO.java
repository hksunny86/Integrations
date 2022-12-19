package com.inov8.microbank.server.dao.agentgroup.hibernate;

import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.AgentTransferRuleModel;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.server.dao.agentgroup.AgentTransferRuleDAO;

public class AgentTransferRuleHibernateDAO extends BaseHibernateDAO<AgentTransferRuleModel, Long, AgentTransferRuleDAO> 
	implements AgentTransferRuleDAO {

	public void deleteAllExistingRecords() throws FrameworkCheckedException{
		
		String stringQuery = "DELETE FROM AgentTransferRuleModel";
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query sessionQuery = session.createQuery(stringQuery);
		
		sessionQuery.executeUpdate();
		
		SessionFactoryUtils.releaseSession(session, getSessionFactory());
	}
	
	public AgentTransferRuleModel findAgentTransferRule(Long deviceTypeId,
															Double transactionAmount,
															Long senderAppUserId,
															Long recipientAppUserId) throws FrameworkCheckedException{
		AgentTransferRuleModel ruleModel = null;
		
		//Added by tayyab
		
		String hql1 = "from AgentTransferRuleModel atrm "
				+ " WHERE atrm.deviceTypeModel.deviceTypeId = ?"
				+ " and("
				+ "  (atrm.transferTypeId in (1,2) and atrm.senderGroupId = ("
				+ "  select agentTaggingId from AgentTaggingModel where appUserId=? and status = 1)"
				+ " ) OR ("
				+ " atrm.transferTypeId in (3,4) and atrm.senderGroupId = ("
				+ " select agentTaggingId from AgentTaggingChildrenModel where appUserModelId=?)"
				+ " )"
				+ " )and("
				+ " ("
				+ " atrm.transferTypeId in (1,3) and atrm.receiverGroupId = ("
				+ " select agentTaggingId from AgentTaggingModel where appUserId=? and status = 1)"
				+ " ) OR ("
				+ " atrm.transferTypeId in (2,4) and atrm.receiverGroupId = ("
				+ " select agentTaggingId from AgentTaggingChildrenModel where appUserModelId=?)"
				+ " )"
				+ ")";
		
		
		//end		
		
		String hql = "from AgentTransferRuleModel atrm "
					+ " WHERE atrm.deviceTypeModel.deviceTypeId = ? AND ( ? >= atrm.rangeStarts AND ? <= atrm.rangeEnds)"
					+ " and("
					+ "  (atrm.transferTypeId in (1,2) and atrm.senderGroupId = ("
					+ "  select agentTaggingId from AgentTaggingModel where appUserId=? and status = 1)"
					+ " ) OR ("
					+ " atrm.transferTypeId in (3,4) and atrm.senderGroupId = ("
					+ " select agentTaggingId from AgentTaggingChildrenModel where appUserModelId=?)"
					+ " )"
					+ " )and("
					+ " ("
					+ " atrm.transferTypeId in (1,3) and atrm.receiverGroupId = ("
					+ " select agentTaggingId from AgentTaggingModel where appUserId=? and status = 1)"
					+ " ) OR ("
					+ " atrm.transferTypeId in (2,4) and atrm.receiverGroupId = ("
					+ " select agentTaggingId from AgentTaggingChildrenModel where appUserModelId=?)"
					+ " )"
					+ ")";
		
		Object[] values = new Object[]{deviceTypeId, transactionAmount, transactionAmount ,senderAppUserId, senderAppUserId, recipientAppUserId, recipientAppUserId};
		List<AgentTransferRuleModel> list = this.getHibernateTemplate().find(hql.toString(), values);
		if(list != null && list.size() > 0){
			if(list.size() > 1){
				Collection<Long> ids = CollectionUtils.collect(list, TransformerUtils.invokerTransformer("getAgentTransferRuleId"));
				logger.error("Multiple agent transfer rules found against criteria "
								+ "[senderAppUserId:"+senderAppUserId
								+ " recipientAppUserId:"+recipientAppUserId
								+ " deviceTypeId:"+deviceTypeId
								+ " trxAmount:"+transactionAmount+"] "
								+ "... Loaded Agent Transfer Rule IDs:"+ids);
				throw new FrameworkCheckedException(MessageUtil.getMessage(WorkFlowErrorCodeConstants.GENERAL_ERROR)); 
			}


			ruleModel = list.get(0);
		}else{
			Object[] values1 = new Object[]{deviceTypeId,senderAppUserId, senderAppUserId, recipientAppUserId, recipientAppUserId};
			List<AgentTransferRuleModel> list1 = this.getHibernateTemplate().find(hql1.toString(), values1);
			if(null != list1 && list1.size() > 0){
				Collection<Long> ids1 = CollectionUtils.collect(list1, TransformerUtils.invokerTransformer("getAgentTransferRuleId"));
				logger.error("Entered amount is not in defined range "
						+ "[senderAppUserId:"+senderAppUserId
						+ " recipientAppUserId:"+recipientAppUserId
						+ " deviceTypeId:"+deviceTypeId
						+ " trxAmount:"+transactionAmount+"] "
						+ "... Loaded Agent Transfer Rule IDs:"+ids1);
		throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.AGENT_TO_AGENT_TRNSFR_RANGE_ERROR);
			}
		}

		return ruleModel;
	}
	
	public boolean checkIfAgentTransferRuleExists(Long agentTaggingId) throws FrameworkCheckedException{
		boolean dataExists = false;
		String hql = "from AgentTransferRuleModel WHERE senderGroupId=? OR receiverGroupId=?";
		Object[] values = new Object[]{agentTaggingId, agentTaggingId};
		List<AgentTransferRuleModel> list = this.getHibernateTemplate().find(hql.toString(), values);
		if(list != null && list.size() > 0){
			dataExists = true;
		}
		return dataExists;
	}	
	
}
