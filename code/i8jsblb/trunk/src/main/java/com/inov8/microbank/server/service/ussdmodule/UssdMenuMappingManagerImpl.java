package com.inov8.microbank.server.service.ussdmodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.model.UssdMenuMappingModel;
import com.inov8.microbank.server.dao.ussdmodule.UssdMenuMappingDAO;

public class UssdMenuMappingManagerImpl implements UssdMenuMappingManager {
	private UssdMenuMappingDAO ussdMenuMappingDAO;
	private GenericDao genericDAO;

	public BaseWrapper findMenuMapping(BaseWrapper param)throws FrameworkCheckedException{
		BaseWrapper retVal=null;
		CustomList<UssdMenuMappingModel> customList = ussdMenuMappingDAO.findByExample((UssdMenuMappingModel)param.getBasePersistableModel());
		List<UssdMenuMappingModel> userMenuMappingList= customList.getResultsetList();
		if(userMenuMappingList!=null && userMenuMappingList.size() >0){
			param.setBasePersistableModel(userMenuMappingList.get(0)); 
			retVal=param;
		}
		return retVal;
	}
	public UssdMenuMappingDAO getUssdMenuMappingDAO() {
		return ussdMenuMappingDAO;
	}
	public void setUssdMenuMappingDAO(UssdMenuMappingDAO ussdMenuMappingDAO) {
		this.ussdMenuMappingDAO = ussdMenuMappingDAO;
	}
	public GenericDao getGenericDAO() {
		return genericDAO;
	}
	public void setGenericDAO(GenericDao genericDAO) {
		this.genericDAO = genericDAO;
	}
	

}
