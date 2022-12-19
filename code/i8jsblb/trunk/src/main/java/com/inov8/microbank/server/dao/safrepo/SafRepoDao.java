package com.inov8.microbank.server.dao.safrepo;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.SafRepoModel;

public interface SafRepoDao extends BaseDAO<SafRepoModel,Long>{
	
	public void updateSafRepoStatus(SafRepoModel model);
	public void updateSafRepoByQueue(SafRepoModel model);

}
