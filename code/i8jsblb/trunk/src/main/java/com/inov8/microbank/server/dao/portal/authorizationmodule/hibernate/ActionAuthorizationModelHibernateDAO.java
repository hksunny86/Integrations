package com.inov8.microbank.server.dao.portal.authorizationmodule.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.model.usecasemodule.LevelCheckerModel;
import com.inov8.microbank.common.util.ActionAuthorizationConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.dao.portal.authorizationmodule.ActionAuthorizationModelDAO;

public class ActionAuthorizationModelHibernateDAO extends BaseHibernateDAO<ActionAuthorizationModel,Long,ActionAuthorizationModelDAO> implements ActionAuthorizationModelDAO {

	@Override
	public CustomList<ActionAuthorizationModel> searchConflictedAuthorizationRequests(SearchBaseWrapper searchBaseWrapper) {
		
		ActionAuthorizationModel model = (ActionAuthorizationModel) searchBaseWrapper.getBasePersistableModel();
		Long modifiedEscalationLevels = (Long) searchBaseWrapper.getObject("modifiedEscalationLevels");
		Long currentEscalationLevels = (Long) searchBaseWrapper.getObject("currentEscalationLevels");
		
		List<Long> escalationlevels = new ArrayList<Long>();  
		
		for(Long i = modifiedEscalationLevels+1; i <=currentEscalationLevels;i++ ){
			escalationlevels.add(i);
		}
		
		Criterion criteria = null;			
		criteria = Restrictions.in("escalationLevel", escalationlevels);
	    CustomList<ActionAuthorizationModel> customList= findByCriteria(criteria, model, searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap());
		return customList;
	}
	@Override
	public CustomList<ActionAuthorizationModel> search(SearchBaseWrapper searchBaseWrapper) {

		ActionAuthorizationModel model = (ActionAuthorizationModel) searchBaseWrapper.getBasePersistableModel();
		CustomList<ActionAuthorizationModel> customList =null;

		List<Long> actionStatus = new ArrayList<Long>();
		actionStatus.add(ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK);
		actionStatus.add(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL);
		actionStatus.add(ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVAL_DENIED);
		actionStatus.add(ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED);
		actionStatus.add(ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED_INTIMATED);
		actionStatus.add(ActionAuthorizationConstantsInterface.ACTION_STATUS_RESOLVED);

		Criterion actionStatuscriteria = null;
		actionStatuscriteria = Restrictions.in("relationActionStatusIdActionStatusModel.actionStatusId", actionStatus);

		if(null!=model.getActionAuthorizationId() && null!=model.getSegmentId()){///Doing this as actionAuthorizationId is Primary key findByExample can't help
			Criterion criteria = null;
			Criterion criterion2 = null;
			Criterion criterion3 = null;
			Criterion criterion4 = null;
			criteria = Restrictions.eq("actionAuthorizationId", model.getActionAuthorizationId());
			criterion3 = Restrictions.eq("relationSegmentIdSegmentModel.segmentId", model.getSegmentId());
			criterion2 = Restrictions.and(criteria, actionStatuscriteria);
			criterion4 = Restrictions.and(criterion2, criterion3);
			customList= findByCriteria(criterion4, model, searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap(),searchBaseWrapper.getDateRangeHolderModel());
		}
		else if(null!=model.getActionAuthorizationId()){///Doing this as actionAuthorizationId is Primary key findByExample can't help
			Criterion criteria = null;
			Criterion criterion2 = null;
			criteria = Restrictions.eq("actionAuthorizationId", model.getActionAuthorizationId());
			criterion2 = Restrictions.and(criteria, actionStatuscriteria);
			customList= findByCriteria(criterion2, model, searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap(),searchBaseWrapper.getDateRangeHolderModel());
		}

		else if(null!=model.getSegmentId()){///Doing this as actionAuthorizationId is Primary key findByExample can't help
			Criterion criteria = null;
			Criterion criterion2 = null;
			criteria = Restrictions.eq("relationSegmentIdSegmentModel.segmentId", model.getSegmentId());
			criterion2 = Restrictions.and(criteria, actionStatuscriteria);
			customList= findByCriteria(criterion2, model, searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap(),searchBaseWrapper.getDateRangeHolderModel());
		}

		else
		{
			customList= findByCriteria(actionStatuscriteria, model, searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap(),searchBaseWrapper.getDateRangeHolderModel());
		}

			return customList;
	}
	public CustomList<ActionAuthorizationModel> checkExistingRequest(ActionAuthorizationModel actionAuthorizationModel){
		ActionAuthorizationModel model = new ActionAuthorizationModel();
		List<Long> actionStatus = new ArrayList<Long>();  

		model.setReferenceId(actionAuthorizationModel.getReferenceId());
		model.setUsecaseId(actionAuthorizationModel.getUsecaseId());
		actionStatus.add(ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK);
		actionStatus.add(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL);
		
		Criterion criteria = null;			
		criteria = Restrictions.in("relationActionStatusIdActionStatusModel.actionStatusId", actionStatus);
	    CustomList<ActionAuthorizationModel> customList= findByCriteria(criteria, model);
		return customList;
		
	}
	@Override
	public CustomList<ActionAuthorizationModel> searchMyRequests(SearchBaseWrapper searchBaseWrapper) {
		
		ActionAuthorizationModel model = (ActionAuthorizationModel) searchBaseWrapper.getBasePersistableModel();
		
		String hql = "from LevelCheckerModel  where relationCheckerIdAppUserModel.appUserId="
				+UserUtils.getCurrentUser().getAppUserId();
		List<LevelCheckerModel> levelCheckerModelList = getHibernateTemplate().find(hql);
		
		List<ActionAuthorizationModel> allActionAuthorizationModelList=new ArrayList<ActionAuthorizationModel>();
		Disjunction disjunction = Restrictions.disjunction();
		CustomList<ActionAuthorizationModel> customList = new CustomList<>();
		
		
		List<Long> actionStatus = new ArrayList<Long>();
		actionStatus.add(ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK);
		actionStatus.add(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL);
		actionStatus.add(ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVAL_DENIED);
		actionStatus.add(ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED);
		actionStatus.add(ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED_INTIMATED);
		actionStatus.add(ActionAuthorizationConstantsInterface.ACTION_STATUS_RESOLVED);
		
		Criterion actionStatuscriteria = null;			
		actionStatuscriteria = Restrictions.in("relationActionStatusIdActionStatusModel.actionStatusId", actionStatus);
		
		
		

		if(null!=levelCheckerModelList && levelCheckerModelList.size()>0){
			
			for (LevelCheckerModel levelCheckerModel2 : levelCheckerModelList) 
			{	
				Long level = levelCheckerModel2.getUsecaselevelIdUsecaseLevelModel().getLevelNo();
				Long usecaseId = levelCheckerModel2.getUsecaselevelIdUsecaseLevelModel().getUsecaseId();
				List<Long> levelList = new ArrayList<>();
				
				for(long i=1;i<=level;i++)
					levelList.add(new Long(i));
					
				Criterion criteria = null;			
				criteria = Restrictions.and(Restrictions.eq("relationUsecaseIdUsecaseModel.usecaseId", usecaseId),Restrictions.in("escalationLevel", levelList));
				disjunction.add(criteria);
			}

			if(null!=model.getActionAuthorizationId() && null!=model.getSegmentId()){///Doing this as actionAuthorizationId is Primary key findByExample can't help
				Criterion criteria = null;
				Criterion criterion2 = null;
				Criterion criterion3 = null;
				Criterion criterion4 = null;
				criteria = Restrictions.eq("actionAuthorizationId", model.getActionAuthorizationId());
				criterion3 = Restrictions.eq("relationSegmentIdSegmentModel.segmentId", model.getSegmentId());
				criterion2 = Restrictions.and(criteria, actionStatuscriteria);
				criterion4 = Restrictions.and(criterion2, criterion3);
				customList= findByCriteria(criterion4, model, searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap(),searchBaseWrapper.getDateRangeHolderModel());
			}

			if (null!=model.getActionAuthorizationId()){
				Criterion criterion=null;
				Criterion criterion2=null;
				criterion = Restrictions.and(disjunction,Restrictions.eq("actionAuthorizationId", model.getActionAuthorizationId()));
				criterion2 = Restrictions.and(criterion, actionStatuscriteria);
				customList= findByCriteria(criterion2,model, searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap(),searchBaseWrapper.getDateRangeHolderModel());
			}
			else if(null!=model.getSegmentId()){///Doing this as actionAuthorizationId is Primary key findByExample can't help
				Criterion criteria = null;
				Criterion criterion2 = null;
				criteria = Restrictions.eq("relationSegmentIdSegmentModel.segmentId", model.getSegmentId());
				criterion2 = Restrictions.and(criteria, actionStatuscriteria);
				customList= findByCriteria(criterion2, model, searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap(),searchBaseWrapper.getDateRangeHolderModel());
			}

			else{
				Criterion criterion=null;
				criterion = Restrictions.and(disjunction,actionStatuscriteria);
				customList= findByCriteria(criterion,model, searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap(),searchBaseWrapper.getDateRangeHolderModel());
			}
			
		}
		
		return customList;
	}

}
