package com.inov8.microbank.server.dao.safrepo;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.SafRepoCoreModel;

public interface SafRepoCoreDao extends BaseDAO<SafRepoCoreModel,Long>{
	
	public void updateStatus(SafRepoCoreModel model);

}
