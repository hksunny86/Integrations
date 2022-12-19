package com.inov8.microbank.server.dao.servicemodule;

import java.util.List;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.ServiceModel;
import com.inov8.microbank.common.util.LabelValueBean;

public interface ServiceDAO extends BaseDAO<ServiceModel, Long>{
	
	public List<ServiceModel> loadSeviceModelListByType(ServiceModel serviceModel);

	List<LabelValueBean> getServiceLabels(Long... pk);

}
