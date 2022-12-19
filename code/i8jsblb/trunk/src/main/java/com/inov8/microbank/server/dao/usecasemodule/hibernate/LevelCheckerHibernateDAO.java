package com.inov8.microbank.server.dao.usecasemodule.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.usecasemodule.LevelCheckerModel;
import com.inov8.microbank.common.model.usecasemodule.UsecaseLevelModel;
import com.inov8.microbank.server.dao.usecasemodule.LevelCheckerDAO;

public class LevelCheckerHibernateDAO extends BaseHibernateDAO<LevelCheckerModel, Long, LevelCheckerDAO> implements LevelCheckerDAO {

	@Override
	public List<LevelCheckerModel> getAsociatedCheckers(List<Long> usecaseLevels) throws FrameworkCheckedException {
			
		Criterion criteria = null;		
		criteria = Restrictions.in("relationUsecaselevelIdUsecaseLevelModel.usecaseLevelId", usecaseLevels);
	    CustomList<LevelCheckerModel> customList= findByCriteria(criteria);
	    if(null!=customList.getResultsetList()){
	    	
	    	return customList.getResultsetList();
	    }
	    else 
	    	return null;
	}

}
