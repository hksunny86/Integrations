package com.inov8.microbank.server.service.distributormodule;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.MatchMode;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.model.DistributorContactModel;
import com.inov8.microbank.common.model.DistributorLevelModel;
import com.inov8.microbank.common.model.DistributorLvlRetContactViewModel;
import com.inov8.microbank.common.model.distributormodule.DistributorLevelListViewModel;
import com.inov8.microbank.server.dao.distributormodule.DistributorContactDAO;
import com.inov8.microbank.server.dao.distributormodule.DistributorLevelDAO;
import com.inov8.microbank.server.dao.distributormodule.DistributorLevelListViewDAO;
import com.inov8.microbank.server.dao.distributormodule.DistributorLvlRetContactViewDAO;

/**
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backend Application for POS terminals.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Asad Hayat
 * @version 1.0
 */

public class DistributorLevelManagerImpl
    implements DistributorLevelManager
{

  private DistributorLevelDAO distributorLevelDAO;
  private DistributorContactDAO distributorContactDAO;
  private DistributorLevelListViewDAO distributorLevelListViewDAO;
  private DistributorLvlRetContactViewDAO distributorLvlRetContactViewDAO;
  private GenericDao genericDAO;

  public void setGenericDAO(GenericDao genericDAO) {
	this.genericDAO = genericDAO;
}

public SearchBaseWrapper loadDistributorLevel(SearchBaseWrapper
                                                searchBaseWrapper)
  {
    DistributorLevelModel distributorLevelModel = this.
        distributorLevelDAO.
        findByPrimaryKey(searchBaseWrapper.getBasePersistableModel().
                         getPrimaryKey());
    searchBaseWrapper.setBasePersistableModel(distributorLevelModel);
    return searchBaseWrapper;
  }

  public BaseWrapper loadDistributorLevel(BaseWrapper
                                          baseWrapper)
  {
    DistributorLevelModel distributorLevelModel = this.
        distributorLevelDAO.
        findByPrimaryKey(baseWrapper.getBasePersistableModel().
                         getPrimaryKey());
    baseWrapper.setBasePersistableModel(distributorLevelModel);
    return baseWrapper;
  }

  public SearchBaseWrapper searchDistributorLevel(SearchBaseWrapper
                                                  searchBaseWrapper)
  {

    CustomList<DistributorLevelListViewModel>
        list = this.distributorLevelListViewDAO.findByExample( (
            DistributorLevelListViewModel)
        searchBaseWrapper.
        getBasePersistableModel(),
        searchBaseWrapper.
        getPagingHelperModel(),
        searchBaseWrapper.
        getSortingOrderMap());
    searchBaseWrapper.setCustomList(list);
    return searchBaseWrapper;

  }

  public BaseWrapper createOrUpdateDistributorLevel(BaseWrapper baseWrapper)
  {
    DistributorLevelModel newDistributorLevelModel = new DistributorLevelModel();
    DistributorLevelModel distributorLevelModel = (DistributorLevelModel)
        baseWrapper.getBasePersistableModel();

    newDistributorLevelModel.setName(distributorLevelModel.getName());
//    newDistributorLevelModel.setDistributorId(distributorLevelModel.getDistributorId());
    ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
	exampleHolder.setMatchMode(MatchMode.EXACT);
    int recordCount = distributorLevelDAO.countByExample(
        newDistributorLevelModel,exampleHolder);
    //***Check if name already exists
     if (recordCount == 0 || distributorLevelModel.getDistributorLevelId() != null)
     {
       distributorLevelModel = this.
           distributorLevelDAO.saveOrUpdate( (
               DistributorLevelModel) baseWrapper.
                                            getBasePersistableModel());
       baseWrapper.setBasePersistableModel(distributorLevelModel);
       return baseWrapper;
     }
     else
     {
       //set baseWrapper to null if record exists
       baseWrapper.setBasePersistableModel(null);
       return baseWrapper;
     }
  }

  /**
   * This method returns the boolean to check whether there is any Distributor Contact defined against Distributor Level
   * @param baseWrapper BaseWrapper
   * @return boolean
   */
  public boolean findDistributorContactByDistributorLevelId(BaseWrapper
      baseWrapper)
  {
    DistributorLevelModel distributorLevelModel = (DistributorLevelModel)
        baseWrapper.getBasePersistableModel();
    DistributorContactModel dcm = new DistributorContactModel();
    dcm.setDistributorLevelId(distributorLevelModel.getDistributorLevelId());
    //int recordCount=distributorContactDAO.findDistributorContactByDistributorLevelId(distributorLevelModel.getDistributorLevelId());
    int recordCount = distributorContactDAO.countByExample(dcm);

    if (recordCount > 0)
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  public List<DistributorLevelModel> searchDistributorLevelModels(Long distributorId,String distributorLevelHQL) {

		List distributorLevelModelList = null;
		List distributorLevelModelListView = new ArrayList();
		/*String distributorLevelHQL = " select distributorLevelId,distributorLevelName from DistributorLevelListViewModel dl "
				+ "where dl.distributorId = ? "
				+ " and dl.managingLevelId is null and dl.ultimateManagingLevelId is null " ;
				*/

		distributorLevelModelList = genericDAO.findByHQL(distributorLevelHQL,
				new Object[] { distributorId });

		DistributorLevelListViewModel distributorLevelModel = null;
		for (int count = 0; count < distributorLevelModelList.size(); count++) {
			distributorLevelModel = new DistributorLevelListViewModel();

			Object obj[] = (Object[]) distributorLevelModelList.get(count);
			if (obj.length>0)
			{
			distributorLevelModel.setDistributorLevelId((Long) obj[0]);
			distributorLevelModel.setDistributorLevelName((String) obj[1]);

			distributorLevelModelListView.add(distributorLevelModel);
			}

		}
		return distributorLevelModelListView;
	}
  
  public void setDistributorLevelDAO(DistributorLevelDAO
                                     distributorLevelDAO)
  {
    this.distributorLevelDAO = distributorLevelDAO;

  }

  public void setDistributorLevelListViewDAO(DistributorLevelListViewDAO
                                             distributorLevelListViewDAO)
  {
    this.distributorLevelListViewDAO = distributorLevelListViewDAO;
  }

  public void setDistributorContactDAO(DistributorContactDAO
                                       distributorContactDAO)
  {
    this.distributorContactDAO = distributorContactDAO;
  }
  public void setDistributorLvlRetContactViewDAO(DistributorLvlRetContactViewDAO distributorLvlRetContactViewDAO) 
  {
		this.distributorLvlRetContactViewDAO = distributorLvlRetContactViewDAO;
  }

public BaseWrapper updateDistributorLevel(BaseWrapper baseWrapper) throws FrameworkCheckedException {
	
	DistributorLevelModel newDistributorLevelModel = new DistributorLevelModel();
    DistributorLevelModel distributorLevelModel = (DistributorLevelModel)
        baseWrapper.getBasePersistableModel();

    newDistributorLevelModel.setName(distributorLevelModel.getName());
//    newDistributorLevelModel.setDistributorId(distributorLevelModel.getDistributorId());
    ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
	exampleHolder.setMatchMode(MatchMode.EXACT);
    int recordCount = distributorLevelDAO.countByExample(
        newDistributorLevelModel,exampleHolder);
    //***Check if name already exists
     if (recordCount == 1 || distributorLevelModel.getDistributorLevelId() != null)
     {
       distributorLevelModel = this.
           distributorLevelDAO.saveOrUpdate( (
               DistributorLevelModel) baseWrapper.
                                            getBasePersistableModel());
       baseWrapper.setBasePersistableModel(distributorLevelModel);
       return baseWrapper;
     }
     else
     {
       //set baseWrapper to null if record exists
       baseWrapper.setBasePersistableModel(null);
       return baseWrapper;
     }

}

	@Override
	public SearchBaseWrapper getParentAgentsBydistributorLevelId(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		return this.distributorLvlRetContactViewDAO.getParentAgentsBydistributorLevelId(searchBaseWrapper);	
	}

    @Override
    public List<DistributorLvlRetContactViewModel> getParentAgentsByDistributorLevelId(Long retailerId, Long distributorLevelId, Long distributorId) throws FrameworkCheckedException {
        return distributorLvlRetContactViewDAO.getParentAgentsByDistributorLevelId(retailerId,distributorLevelId,distributorId);
    }

}
