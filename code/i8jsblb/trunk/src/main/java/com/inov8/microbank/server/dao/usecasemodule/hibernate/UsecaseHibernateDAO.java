package com.inov8.microbank.server.dao.usecasemodule.hibernate;

///////////////////////////////

// Author: Hassan javaid    ///
// Date  : 23-5-2014 	
///////////////////////////////

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.util.ActionAuthorizationConstantsInterface;
import com.inov8.microbank.server.dao.usecasemodule.UsecaseDAO;


public class UsecaseHibernateDAO extends BaseHibernateDAO<UsecaseModel, Long, UsecaseDAO > implements UsecaseDAO {
	
	public CustomList<UsecaseModel> searchAuthorizationEnabledUsecases( SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException{
	
	UsecaseModel model = (UsecaseModel) searchBaseWrapper.getBasePersistableModel();
	Criterion criteria = null;	
	Long[] usecaseIds = {1001L,1002L,1004L,1005L,1007L,1012L,1013L,1017L,1036L,1037L,
						1039L,1044L,1079L,1041L,1105L,1106L,1107L,1108L,1109L,1110L,
						1111L,1112L,1113L,1114L,1098L,1099L,1206L,1207L,1208L,1209L,
						1210L, 1091L, 1092L, 1115L, 1211L, 1212L, 1213L,1217L,1219L, 1221L, 1227L, 1228L,1256L,1258L,
						1260L,1262L,1252L,1254L,1263L,1241L,1264L,1265L,1267L,
						1512L,1514L,1516L,1237L,1238L,1518L,1520L,1522L,1524L, 1093L, 1526L,1528L,1530L,1532L,1534L};






	criteria = Restrictions.in("usecaseId", usecaseIds);
    CustomList<UsecaseModel> customList= findByCriteria(criteria, model, searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap());
	return customList;
	}

	public CustomList<UsecaseModel> searchUsecase( SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException{
		/*
		 * removed 1083:Balance Decryption Scheduler by atif hussain
		 * 
		 */
		
		UsecaseModel model = (UsecaseModel) searchBaseWrapper.getBasePersistableModel();
		Criterion criteria = null;	
		Long[] usecaseIds = { 1001L, 1002L, 1004L, 1005L, 1006L, 1007L, 1012L, 1013L, 1017L, 1018L, 
							  1019L, 1036L, 1037L, 1039L, 1040L, 1041L, 1043L, 1044L, 1045L, 1060L, 
							  1061L, 1062L, 1063L, 1064L, 1065L, 1066L, 1070L, 1071L, 1072L, 1073L, 
							  1079L, 1084L, 1085L, 1090L, 1091L, 1092L, 1093L, 1094L, 1098L, 1099L, 
							  1100L, 1103L, 1104L, 1105L, 1106L, 1107L, 1108L, 1109L, 1110L, 1111L, 
							  1112L, 1113L, 1114L, 1115L, 1116L, 1117L, 1118L, 1119L, 1200L, 1201L,
							  1202L, 1203L, 1206L, 1207L, 1208L, 1209L,1210L, 1211L, 1212L, 1213L, 1035L, 1210L,
				 			  1221L, 1227L, 1228L,1252L,1253L,1254L,1256L,1258L,1260L,1262L,1263L,1241L,1264L,
							  1265L,1267L,1237L,1238L,1518L,1520L,1522L,1524L, 1526L,1528L,1530L,1532L,1534L};
		criteria = Restrictions.in("usecaseId", usecaseIds);
	    CustomList<UsecaseModel> customList= findByCriteria(criteria, model, searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap());
		return customList;
		}
	public UsecaseModel saveOrUpdateUsecase(UsecaseModel usecaseModel){
		String hql = "delete from LevelCheckerModel  where relationUsecaselevelIdUsecaseLevelModel.usecaseLevelId in (select usecaseLevelId from UsecaseLevelModel where relationUsecaseIdUsecaseModel.usecaseId =?))";
		getHibernateTemplate().bulkUpdate(hql, usecaseModel.getUsecaseId());
		hql = "delete from UsecaseLevelModel  where relationUsecaseIdUsecaseModel.usecaseId =?";
		getHibernateTemplate().bulkUpdate(hql, usecaseModel.getUsecaseId());
		usecaseModel = saveOrUpdate(usecaseModel);	
		return usecaseModel;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean getAffectedAuthorizationRequests(UsecaseModel usecaseModel) throws FrameworkCheckedException {
		String hql = "from ActionAuthorizationModel where relationActionStatusIdActionStatusModel.actionStatusId = ? and relationUsecaseIdUsecaseModel.usecaseId = ? and escalationLevel > ?";
		List<ActionAuthorizationModel> actionAuthorizationModelList = getHibernateTemplate().find(hql, new Object[]{ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL, usecaseModel.getUsecaseId(), usecaseModel.getEscalationLevels()});
		return actionAuthorizationModelList.size()>0;
	}

	@Override
	public List<UsecaseModel> getAllUseCases() throws FrameworkCheckedException {
		String hql = "FROM UsecaseModel";
		return getHibernateTemplate().find(hql);
	}
}

