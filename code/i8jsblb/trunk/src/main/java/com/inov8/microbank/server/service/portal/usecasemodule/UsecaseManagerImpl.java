package com.inov8.microbank.server.service.portal.usecasemodule;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.usecasemodule.LevelCheckerModel;
import com.inov8.microbank.common.model.usecasemodule.UsecaseLevelModel;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.server.dao.usecasemodule.LevelCheckerDAO;
import com.inov8.microbank.server.dao.usecasemodule.UsecaseDAO;
import com.inov8.microbank.server.dao.usecasemodule.UsecaseLevelDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;

public class UsecaseManagerImpl implements UsecaseManager {
	
	private UsecaseDAO usecaseDAO;
	private UsecaseLevelDAO usecaseLevelDAO;
	private LevelCheckerDAO levelCheckerDAO;
	private ActionLogManager actionLogManager;
	
	
	public SearchBaseWrapper searchUsecase(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		 
 		CustomList<UsecaseModel> list = this.usecaseDAO.searchUsecase(searchBaseWrapper);  
 	    searchBaseWrapper.setCustomList(list);
 	    return searchBaseWrapper;
 	  }
	public SearchBaseWrapper searchAuthorizationEnableUsecase(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		 
 		CustomList<UsecaseModel> list = this.usecaseDAO.searchAuthorizationEnabledUsecases(searchBaseWrapper);  
 	    searchBaseWrapper.setCustomList(list);
 	    return searchBaseWrapper;
 	  }
	
	public UsecaseModel loadUsecase(Long usecaseId) throws FrameworkCheckedException
	  {
	    UsecaseModel usecaseModel = (UsecaseModel)this.usecaseDAO.findByPrimaryKey(usecaseId); 
	    return usecaseModel;
	  }
	
	public BaseWrapper saveOrUpdateUsecase(BaseWrapper baseWrapper) throws FrameworkCheckedException
	  {
		
		
		ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
		
		String[] allLevelcheckerKeys = (String[])((UsecaseModel) baseWrapper.getBasePersistableModel()).getLevelcheckers();
		List<LevelCheckerModel> levelCheckerModelList= new ArrayList<>();
		Map<String,LevelCheckerModel> levelCheckerModelMap = (Map<String, LevelCheckerModel>) baseWrapper.getObject("checkerMap");

		UsecaseModel usecaseModel =  usecaseDAO.saveOrUpdateUsecase((UsecaseModel) baseWrapper.getBasePersistableModel());   
	    List<UsecaseLevelModel> usecaseLevelModelList = null;
		try {
			usecaseLevelModelList = (List<UsecaseLevelModel>) usecaseModel.getUsecaseIdLevelModelList();
		} catch (Exception e) {
			
			e.printStackTrace();
		}   	
		for(int i=0;i<usecaseModel.getEscalationLevels();i++ ){
	
			String[] levelCheckerkeys =  allLevelcheckerKeys[i].split(",");	
			for(int j=0; j< levelCheckerkeys.length;j++){			
				LevelCheckerModel levelCheckerModel = new LevelCheckerModel();
				levelCheckerModel = levelCheckerModelMap.get(levelCheckerkeys[j]);
				levelCheckerModel.setSeqNo((long) (i+1));
				levelCheckerModel.setUsecaselevelIdUsecaseLevelModel(usecaseLevelModelList.get(i));
				levelCheckerModelList.add(levelCheckerModel);
			}
		}
		
		levelCheckerModelList = (List<LevelCheckerModel>) levelCheckerDAO.saveOrUpdateCollection(levelCheckerModelList); 
		
		actionLogModel.setCustomField1(usecaseModel.getUsecaseId().toString());
		actionLogModel.setCustomField11(usecaseModel.getName());
    	this.actionLogManager.completeActionLog(actionLogModel);
		
	    baseWrapper.setBasePersistableModel(usecaseModel);    
	    return baseWrapper;
	  }
	
	
	public SearchBaseWrapper searchUsecaseLevels(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	  {
		CustomList<UsecaseLevelModel> list = this.usecaseLevelDAO.findByExample((UsecaseLevelModel) searchBaseWrapper.getBasePersistableModel(),null,searchBaseWrapper.getSortingOrderMap());  
 	    searchBaseWrapper.setCustomList(list);
 	    return searchBaseWrapper;
	  }
	public UsecaseLevelModel findUsecaseLevel(Long usecaseId,Long levelNo) throws FrameworkCheckedException
	  {
		UsecaseLevelModel usecaseLevelModel = new UsecaseLevelModel();
		usecaseLevelModel.setUsecaseId(usecaseId);
		usecaseLevelModel.setLevelNo(levelNo);
		CustomList<UsecaseLevelModel> list = this.usecaseLevelDAO.findByExample(usecaseLevelModel);
		if(list != null && !list.getResultsetList().isEmpty())
			usecaseLevelModel = list.getResultsetList().get(0);
	    return usecaseLevelModel;
	  }
	@Override
	public List<LevelCheckerModel> getLevelCheckerModelList(Long usecaseLevelId ) throws FrameworkCheckedException {
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		LinkedHashMap<String, SortingOrder> sortingOrderMap = new LinkedHashMap<String, SortingOrder>();
		LevelCheckerModel levelCheckerModel = new LevelCheckerModel();
		levelCheckerModel.setUsecaseLevelId(usecaseLevelId);	
		sortingOrderMap = new LinkedHashMap<String, SortingOrder>(); 
		sortingOrderMap.put("levelCheckerId", SortingOrder.ASC);
		searchBaseWrapper.setBasePersistableModel(levelCheckerModel);
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
				
		CustomList<LevelCheckerModel> list = this.levelCheckerDAO.findByExample((LevelCheckerModel) searchBaseWrapper.getBasePersistableModel(),null,searchBaseWrapper.getSortingOrderMap());  
 	   
 	    return list.getResultsetList();
	}
	@Override
	public List<LevelCheckerModel> getLevelCheckerModelListByCheckerAppUserId(Long checkerAppUserId ) throws FrameworkCheckedException {
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		LinkedHashMap<String, SortingOrder> sortingOrderMap = new LinkedHashMap<String, SortingOrder>();
		LevelCheckerModel levelCheckerModel = new LevelCheckerModel();
		levelCheckerModel.setCheckerId(checkerAppUserId);
		sortingOrderMap = new LinkedHashMap<String, SortingOrder>(); 
		sortingOrderMap.put("levelCheckerId", SortingOrder.ASC);
		searchBaseWrapper.setBasePersistableModel(levelCheckerModel);
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
				
		CustomList<LevelCheckerModel> list = this.levelCheckerDAO.findByExample((LevelCheckerModel) searchBaseWrapper.getBasePersistableModel(),null,searchBaseWrapper.getSortingOrderMap());  
 	   
 	    return list.getResultsetList();
	}
	public boolean isCheckerAtLevel(Long usecaseId, Long levelNo, Long appUserId)throws FrameworkCheckedException {
		boolean isChecker=false; 
		
		if(levelNo.intValue()<=0)//in case of Assigned back
			return false;
		
		UsecaseLevelModel usecaseLevelModel = findUsecaseLevel(usecaseId,levelNo);
		
		if(null!=usecaseLevelModel && null!=usecaseLevelModel.getUsecaseLevelId()){
			List<LevelCheckerModel> levelCheckerModelList = this.getLevelCheckerModelList(usecaseLevelModel.getUsecaseLevelId());
			
			for (LevelCheckerModel temLevelCheckerModel : levelCheckerModelList) {
				if(temLevelCheckerModel.getCheckerIdAppUserModel().getAppUserId().longValue()== appUserId.longValue()){
					isChecker=true;
					break;
				}	
			}
		}
		else
		{
			isChecker=false;
		}
		
 	    return isChecker;
	}		
	
	@Override
	public Long getNextAuthorizationLevel(Long usecaseId, Long currentLevel) throws FrameworkCheckedException {
		UsecaseModel usecaseModel = (UsecaseModel)this.usecaseDAO.findByPrimaryKey(usecaseId);
		for(long i = currentLevel+1; i<= usecaseModel.getEscalationLevels();i++){
			UsecaseLevelModel usecaseLevelModel= findUsecaseLevel(usecaseId,i);
			if(!(usecaseLevelModel.getIntimateOnly())){
				return i;
			}
		}	
		return new Long(-1);	
	}
	@Override
	public boolean getAffectedAuthorizationRequests(UsecaseModel usecaseModel) throws FrameworkCheckedException {
		boolean records = usecaseDAO.getAffectedAuthorizationRequests(usecaseModel);		
		return records;
	}
	@Override
	public List<AppUserModel> getAsociatedCheckers(List<UsecaseModel> usecaseModelList)throws FrameworkCheckedException {
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		UsecaseLevelModel usecaseLevelModel = new UsecaseLevelModel();	
		List<Long> usecaseLevels = new ArrayList<>();
		CustomList<UsecaseLevelModel> usecaseLevelModelCustomList = null;
		List<LevelCheckerModel> levelCheckerModelList = new ArrayList<LevelCheckerModel>();
		List<AppUserModel> appUserModelList = new ArrayList<AppUserModel>(); 
		
		for (UsecaseModel usecaseModel : usecaseModelList) {
			List<UsecaseLevelModel> usecaseLevelModelList=null;
			usecaseLevelModel.setUsecaseId(usecaseModel.getUsecaseId());
			searchBaseWrapper.setBasePersistableModel(usecaseLevelModel);
			
			usecaseLevelModelCustomList =this.searchUsecaseLevels(searchBaseWrapper).getCustomList();
			
			if(null!=usecaseLevelModelCustomList && null!=usecaseLevelModelCustomList.getResultsetList()){
				usecaseLevelModelList = usecaseLevelModelCustomList.getResultsetList();
				for (UsecaseLevelModel usecaseLevelModel2 :usecaseLevelModelList ) {
					usecaseLevels.add(usecaseLevelModel2.getUsecaseLevelId());
				}		
			}
			
		}
		if(usecaseLevels.size()<1)
			return null;
		else
		{				
				levelCheckerModelList = levelCheckerDAO.getAsociatedCheckers(usecaseLevels);
				if(null!=levelCheckerModelList && levelCheckerModelList.size()>0){
					for (LevelCheckerModel levelCheckerModel2 : levelCheckerModelList) {
			    		if(!appUserModelList.contains(levelCheckerModel2.getCheckerIdAppUserModel()))
			    			appUserModelList.add(levelCheckerModel2.getCheckerIdAppUserModel());
					}
				}	
				
				if(null!= appUserModelList && appUserModelList.size()>0)
					return appUserModelList;
				else 
					return null;
		}
			 
	}

	@Override
	public List<UsecaseModel> getAllUseCases() throws FrameworkCheckedException {
		return usecaseDAO.getAllUseCases();
	}

	public void setUsecaseDAO(UsecaseDAO usecaseDAO) {
		this.usecaseDAO = usecaseDAO;
	}	
	public void setUsecaseLevelDAO(UsecaseLevelDAO usecaseLevelDAO) {
		this.usecaseLevelDAO = usecaseLevelDAO;
	}
	public void setLevelCheckerDAO(LevelCheckerDAO levelCheckerDAO) {
		this.levelCheckerDAO = levelCheckerDAO;
	}

	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}
	
}
