package com.inov8.microbank.server.service.switchutilitymappingmodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.model.SwitchUtilityMappingModel;
import com.inov8.microbank.server.dao.switchutilitymappingmodule.SwitchUtilityMappingDAO;

public class SwitchUtilityMappingManagerImpl implements SwitchUtilityMappingManager
{

	private GenericDao genericDao;
	private SwitchUtilityMappingDAO switchUtilityMappingDAO;
	

	public BaseWrapper findUtilityCompanyCodeByExample(BaseWrapper baseWrapper)
			throws FrameworkCheckedException
	{
		SwitchUtilityMappingModel switchUtilityMappingModel = (SwitchUtilityMappingModel)baseWrapper.getBasePersistableModel();
//		CriteriaConfiguration criteriaConfiguration = new CriteriaConfiguration();
//		criteriaConfiguration.setEnableLike(false);
//		criteriaConfiguration.setMatchMode(MatchMode.EXACT);
		
		CustomList<SwitchUtilityMappingModel> customList = this.switchUtilityMappingDAO.findByExample(switchUtilityMappingModel, null);

		if(customList != null && customList.getResultsetList() != null)
		{
			List<SwitchUtilityMappingModel> list = customList.getResultsetList();
			if(list != null && list.size() > 0)
			{
				baseWrapper.setBasePersistableModel(list.get(0));
			}
		}
		//List<SwitchUtilityMappingModel> list = this.genericDao.findEntityByExample(switchUtilityMappingModel, criteriaConfiguration);
		return baseWrapper;
	}

	public SearchBaseWrapper findUtilityCompanyCodeByExample(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
		SwitchUtilityMappingModel switchUtilityMappingModel = (SwitchUtilityMappingModel)searchBaseWrapper.getBasePersistableModel();
		List<SwitchUtilityMappingModel> list = this.genericDao.findEntityByExample(switchUtilityMappingModel, null);
		CustomList<SwitchUtilityMappingModel> customList = new CustomList<SwitchUtilityMappingModel>();
		customList.setResultsetList(list);
		searchBaseWrapper.setCustomList(customList);
		return searchBaseWrapper;
	}
	
	public void setGenericDao(GenericDao genericDao)
	{
		this.genericDao = genericDao;
	}

	public void setSwitchUtilityMappingDAO(SwitchUtilityMappingDAO switchUtilityMappingDAO)
	{
		this.switchUtilityMappingDAO = switchUtilityMappingDAO;
	}

}
