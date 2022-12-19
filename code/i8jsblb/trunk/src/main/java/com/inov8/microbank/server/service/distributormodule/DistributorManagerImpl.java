package com.inov8.microbank.server.service.distributormodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.DistributorLevelModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.PartnerModel;
import com.inov8.microbank.common.model.PartnerPermissionGroupModel;
import com.inov8.microbank.common.model.distributormodule.DistributorListViewModel;
import com.inov8.microbank.common.util.DistributorConstants;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.vo.agenthierarchy.DistributorVO;
import com.inov8.microbank.server.dao.distributormodule.DistributorDAO;
import com.inov8.microbank.server.dao.distributormodule.DistributorLevelDAO;
import com.inov8.microbank.server.dao.distributormodule.DistributorListViewDAO;
import com.inov8.microbank.server.dao.usergroupmodule.PartnerDAO;
import com.inov8.microbank.server.dao.usergroupmodule.PartnerPermissionGroupDAO;
import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.criterion.MatchMode;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
public class DistributorManagerImpl
    implements DistributorManager
{

  private DistributorListViewDAO distributorListViewDAO;
  private DistributorDAO distributorDAO;
  private DistributorLevelDAO distributorLevelDAO;
  private PartnerDAO partnerDAO;
  private PartnerPermissionGroupDAO partnerPermissionGroupDAO;    

  public SearchBaseWrapper searchDistributorsByCriteria(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
  {
	  try
	  {
		  ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
          exampleHolder.setMatchMode(MatchMode.EXACT); 
          exampleHolder.setIgnoreCase(false);
		  DistributorModel distributorModel = (DistributorModel)searchBaseWrapper.getBasePersistableModel(); 
		  CustomList<DistributorModel> distributorModelList = this.distributorDAO.findByExample(distributorModel, null, null, exampleHolder);
		  
		  if (distributorModelList != null && distributorModelList.getResultsetList().size() > 0)
		  {
			  searchBaseWrapper.setCustomList(distributorModelList);
		  }
		  
	  }
	  catch(Exception ex)
	  {
		  ex.printStackTrace();
	  }
	  return searchBaseWrapper;
  }

	public List<DistributorModel> searchDistributorsByExample(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
		List<DistributorModel> distributorModelList=new ArrayList<>();
		try
		{

			DistributorModel distributorModel = (DistributorModel)searchBaseWrapper.getBasePersistableModel();
			 distributorModelList = this.distributorDAO.findAllDistributorForMno(distributorModel);

		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return distributorModelList;
	}

	@Override
	public List<DistributorModel> findAllDistributor() {
		return distributorDAO.findAllDistributor();
	}

	public SearchBaseWrapper loadDistributor(SearchBaseWrapper searchBaseWrapper)
  {
    DistributorModel distributorModel = this.distributorDAO.
        findByPrimaryKey(searchBaseWrapper.getBasePersistableModel().
                         getPrimaryKey());
    searchBaseWrapper.setBasePersistableModel(distributorModel);
    
    
    //load Distributor permission from partner_permission_group
    PartnerModel tmpPartnerModel = new PartnerModel();
    tmpPartnerModel.setDistributerId(distributorModel.getDistributorId());
    tmpPartnerModel.setAppUserTypeId(UserTypeConstantsInterface.DISTRIBUTOR);
    
	 ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setEnableLike(Boolean.FALSE);		    
    
    CustomList<PartnerModel> partnerModelList = partnerDAO.findByExample(tmpPartnerModel,null,null,exampleHolder);
    tmpPartnerModel = partnerModelList.getResultsetList().get(0);
    List<PartnerPermissionGroupModel> partnerPermissionGroupModelList = null;
    try {
		 partnerPermissionGroupModelList = (List<PartnerPermissionGroupModel>) tmpPartnerModel.getPartnerIdPartnerPermissionGroupModelList();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	PartnerPermissionGroupModel tmppartnerPermissionGroupModel = partnerPermissionGroupModelList.get(0);
    searchBaseWrapper.putObject("permissionGroupId", tmppartnerPermissionGroupModel.getPermissionGroupId());      
    
    return searchBaseWrapper;
  }

  public BaseWrapper loadDistributor(BaseWrapper
                                     baseWrapper)
  {
    DistributorModel distributorModel = this.distributorDAO.
        findByPrimaryKey(baseWrapper.getBasePersistableModel().
                         getPrimaryKey());
    baseWrapper.setBasePersistableModel(distributorModel);
    return baseWrapper;
  }

  public SearchBaseWrapper searchDistributor(SearchBaseWrapper
                                             searchBaseWrapper)
  {
    CustomList<DistributorListViewModel>
        list = this.distributorListViewDAO.findByExample( (
            DistributorListViewModel)
        searchBaseWrapper.
        getBasePersistableModel(),
        searchBaseWrapper.
        getPagingHelperModel(),
        searchBaseWrapper.
        getSortingOrderMap());
    searchBaseWrapper.setCustomList(list);
    return searchBaseWrapper;

  }

  public void setDistributorListViewDAO(DistributorListViewDAO
                                        distributorListViewDAO)
  {
    this.distributorListViewDAO = distributorListViewDAO;
  }

  public void setDistributorDAO(DistributorDAO distributorDAO)
  {
    this.distributorDAO = distributorDAO;
  }

  public void setDistributorLevelDAO(DistributorLevelDAO distributorLevelDAO)
  {
    this.distributorLevelDAO = distributorLevelDAO;
  }
  
  
  public BaseWrapper saveOrUpdateDistributor(BaseWrapper baseWrapper) throws FrameworkCheckedException
  {
	  DistributorModel newDistributorModel = new DistributorModel();
	  DistributorVO distributorVO = (DistributorVO) baseWrapper.getBasePersistableModel();
	  newDistributorModel.setActive(distributorVO.getActive());
	  newDistributorModel.setAddress1(distributorVO.getAddress1());
	  newDistributorModel.setAddress2(distributorVO.getAddress2());
	  newDistributorModel.setCity(distributorVO.getCity());
	  newDistributorModel.setComments(distributorVO.getComments());
	  newDistributorModel.setContactName(distributorVO.getContactName());
	  newDistributorModel.setCountry(distributorVO.getCountry());
	  newDistributorModel.setCreatedBy(distributorVO.getCreatedBy());
	  newDistributorModel.setCreatedOn(distributorVO.getCreatedOn());
	  newDistributorModel.setDescription(distributorVO.getDescription());
	  newDistributorModel.setEmail(distributorVO.getEmail());
	  newDistributorModel.setFax(distributorVO.getFax());
	  newDistributorModel.setName(distributorVO.getName());
	  newDistributorModel.setPhoneNo(distributorVO.getPhoneNo());
	  newDistributorModel.setState(distributorVO.getState());
	  newDistributorModel.setVersionNo(1);
	  newDistributorModel.setNational(Boolean.FALSE);
	  newDistributorModel.setNationalString("abc");
	  newDistributorModel.setZip(distributorVO.getZip());
	  newDistributorModel.setAreaId(1228l);
	  newDistributorModel.setUpdatedOn(new Date());
	  newDistributorModel.setUpdatedBy(distributorVO.getCreatedBy());  
	  
	  
	  distributorDAO.saveOrUpdate(newDistributorModel);
	  
	  return baseWrapper;
  }

  public BaseWrapper createOrUpdateDistributor(BaseWrapper baseWrapper) throws FrameworkCheckedException
  {
	
    DistributorModel newDistributorModel = new DistributorModel();
    DistributorModel distributorModel = (DistributorModel) baseWrapper.
        getBasePersistableModel();
    newDistributorModel.setName(distributorModel.getName());
    ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
	exampleHolder.setMatchMode(MatchMode.EXACT);
    int recordCount = distributorDAO.countByExample(newDistributorModel,exampleHolder);
    Long currentUserId = UserUtils.getCurrentUser().getAppUserId();
    
//  ***check if national distributor already exists
    boolean nationalDistributorInSystem = nationalDistributorExist();

    //---national Distributor must be active
    if (distributorModel.getNational() == true && distributorModel.getActive() == false){
    	FrameworkCheckedException fce = new FrameworkCheckedException (DistributorConstants.NATIONAL_DISTRIBUTOR_ACTIVE_CHK);				
		throw fce;
    }
    //***Check if name already exists
     if (recordCount == 0 || distributorModel.getDistributorId() == null)
     {
    	 if (distributorModel.getNational() == true){			
 					if (nationalDistributorInSystem){
 					FrameworkCheckedException fce = new FrameworkCheckedException (DistributorConstants.NATIONAL_DISTRIBUTOR_VIOLATION);
 					distributorModel.setNational(false);
 					throw fce;		
 					}
 				
 			}else if (! nationalDistributorInSystem && distributorModel.getNational() != true){
 				FrameworkCheckedException fce = new FrameworkCheckedException (DistributorConstants.NATIONAL_DISTRIBUTOR_CHK);				
 				throw fce;
 		    } 
	    distributorModel = this.distributorDAO.saveOrUpdate( (
	            DistributorModel) baseWrapper.
	            getBasePersistableModel());
	        	  baseWrapper.setBasePersistableModel(distributorModel);
	 
    	 //saved data in partner and partner_permission_group
          PartnerModel partnerModel = new PartnerModel();
          partnerModel.setName(distributorModel.getName());
	   	  partnerModel.setAppUserTypeId(UserTypeConstantsInterface.DISTRIBUTOR);
	      partnerModel.setDistributerId(distributorModel.getDistributorId());
	      partnerModel.setComments(distributorModel.getComments());
	      partnerModel.setActive(distributorModel.getActive());
	      partnerModel.setDescription(distributorModel.getDescription());
	      partnerModel.setCreatedBy(currentUserId);
	      partnerModel.setUpdatedBy(currentUserId);
	      partnerModel.setUpdatedOn(new Date());
	      partnerModel.setCreatedOn(new Date());
	   
	      Long permissionGroupId = (Long)baseWrapper.getObject("permissionGroupId");
	   
	      PartnerPermissionGroupModel partnerPermissionGroupModel = new PartnerPermissionGroupModel();
	      partnerPermissionGroupModel.setPermissionGroupId(permissionGroupId);		       
	      partnerPermissionGroupModel.setCreatedBy(currentUserId);
	      partnerPermissionGroupModel.setUpdatedBy(currentUserId);
	      partnerPermissionGroupModel.setUpdatedOn(new Date());
	      partnerPermissionGroupModel.setCreatedOn(new Date());

	      partnerModel.addPartnerIdPartnerPermissionGroupModel(partnerPermissionGroupModel);
	      partnerDAO.saveOrUpdate(partnerModel);	          
       
	      return baseWrapper;
     }
     else if (recordCount == 0 || distributorModel.getDistributorId() != null)
     {
    	 if (! nationalDistributorInSystem && distributorModel.getNational() != true){
				FrameworkCheckedException fce = new FrameworkCheckedException (DistributorConstants.NATIONAL_DISTRIBUTOR_CHK);				
				throw fce;
		    } 
    	 if (distributorModel.getNational() == true){
  			DistributorModel tempDistributorModel = new DistributorModel ();
  			tempDistributorModel.setNational(true);
  			CustomList tempList = this.distributorDAO.findByExample(tempDistributorModel);  			
  			if (tempList.getResultsetList().size()!= 0){
  				tempDistributorModel = (DistributorModel)tempList.getResultsetList().get(0);
  				if (tempDistributorModel.getDistributorId().longValue() != distributorModel.getDistributorId().longValue()){
  					//System.out.println("National distributor already exists in the system");
					FrameworkCheckedException fce = new FrameworkCheckedException (DistributorConstants.NATIONAL_DISTRIBUTOR_VIOLATION);
					distributorModel.setNational(false);
					throw fce;		
  				}  	
  				try {
					BeanUtils.copyProperties(tempDistributorModel, distributorModel);
					baseWrapper.setBasePersistableModel(tempDistributorModel);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
  			}
    	 }
 	    distributorModel = this.distributorDAO.saveOrUpdate( (
	            DistributorModel) baseWrapper.
	            getBasePersistableModel());
	        	  baseWrapper.setBasePersistableModel(distributorModel);    	 
     }else{
       //set baseWrapper to null if record exists
       baseWrapper.setBasePersistableModel(null);
   }

     return baseWrapper;
  }

  private boolean nationalDistributorExist() {
	  
			DistributorModel tempDistributorModel = new DistributorModel ();
			tempDistributorModel.setNational(true);
			CustomList tempList = this.distributorDAO.findByExample(tempDistributorModel);  			
			if (tempList.getResultsetList().size()== 0){
				return false;
//				FrameworkCheckedException fce = new FrameworkCheckedException (DistributorConstants.NATIONAL_DISTRIBUTOR_CHK);				
//				throw fce;		
				}
			else{
				return true;
			}
  }

/**
   * This method returns the boolean to check whether there is any Distributor Contact defined against Distributor Level
   * @param baseWrapper BaseWrapper
   * @return boolean
   */
  public boolean findDistributorLevelByDistributorId(BaseWrapper baseWrapper)
  {
    DistributorModel distributorModel = (DistributorModel) baseWrapper.
        getBasePersistableModel();

    DistributorLevelModel distributorLevelModel = new DistributorLevelModel();
	// commented by Rashid Starts
    /*distributorLevelModel.setDistributorId(distributorModel.getDistributorId());*/
	// commented by Rashid Ends
    //int recordCount=distributorLevelDAO.findDistributorLevelByDistributorId(distributorModel.getDistributorId());
    ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
	exampleHolder.setMatchMode(MatchMode.EXACT);

    int recordCount = distributorLevelDAO.countByExample(distributorLevelModel,exampleHolder);

    if (recordCount > 0)
    {
      return true;
    }
    else
    {
      return false;
    }
  }
  
  
  public List<DistributorModel> findActiveDistributor() {
	  
	  return distributorDAO.findActiveDistributor();
  }
  
  

public void setPartnerDAO(PartnerDAO partnerDAO) {
	this.partnerDAO = partnerDAO;
}

public void setPartnerPermissionGroupDAO(
		PartnerPermissionGroupDAO partnerPermissionGroupDAO) {
	this.partnerPermissionGroupDAO = partnerPermissionGroupDAO;
}

}
