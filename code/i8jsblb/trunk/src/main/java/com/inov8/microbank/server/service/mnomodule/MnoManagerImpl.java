package com.inov8.microbank.server.service.mnomodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.mnomodule.MnoListViewModel;
import com.inov8.microbank.common.util.ConcernPartnerTypeConstants;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.dao.mnomodule.MnoDAO;
import com.inov8.microbank.server.dao.mnomodule.MnoListViewDAO;
import com.inov8.microbank.server.dao.portal.concernmodule.ConcernPartnerDAO;
import com.inov8.microbank.server.dao.usergroupmodule.PartnerDAO;
import com.inov8.microbank.server.dao.usergroupmodule.PartnerPermissionGroupDAO;
import org.hibernate.criterion.MatchMode;

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

public class MnoManagerImpl
    implements MnoManager
{

  private MnoDAO mnoDAO;
  private MnoListViewDAO mnoListViewDAO;
  private GenericDao genericDAO;
  private ConcernPartnerDAO concernPartnerDAO;
  private PartnerDAO partnerDAO;
  private PartnerPermissionGroupDAO partnerPermissionGroupDAO;  

  public void setGenericDAO(GenericDao genericDAO) {
	this.genericDAO = genericDAO;
}

public SearchBaseWrapper loadMno(SearchBaseWrapper searchBaseWrapper)
  {
    MnoModel mnoModel = this.mnoDAO.findByPrimaryKey(searchBaseWrapper.getBasePersistableModel().
                         getPrimaryKey());
    searchBaseWrapper.setBasePersistableModel(mnoModel);
    
    //load MNO permission from partner_permission_group
    PartnerModel tmpPartnerModel = new PartnerModel();
    tmpPartnerModel.setMnoId(mnoModel.getMnoId());
    tmpPartnerModel.setAppUserTypeId(UserTypeConstantsInterface.MNO);
    
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

  /**
   * This method is meant for the deactivate case
   * @param baseWrapper BaseWrapper
   * @return BaseWrapper
   */
  public BaseWrapper loadMno(BaseWrapper baseWrapper)
  {
    MnoModel mnoModel = this.mnoDAO.findByPrimaryKey(baseWrapper.getBasePersistableModel().
                         getPrimaryKey());
    baseWrapper.setBasePersistableModel(mnoModel);
    return baseWrapper;
  }

  public SearchBaseWrapper searchMno(SearchBaseWrapper searchBaseWrapper)
  {
    CustomList<MnoListViewModel>
        list = this.mnoListViewDAO.findByExample( (MnoListViewModel)
        searchBaseWrapper.getBasePersistableModel(),
        searchBaseWrapper.getPagingHelperModel(),
        searchBaseWrapper.getSortingOrderMap());
    searchBaseWrapper.setCustomList(list);
    return searchBaseWrapper;
  }

  public BaseWrapper createOrUpdateMno(BaseWrapper baseWrapper) throws FrameworkCheckedException
    {
      MnoModel newMnoModel=new MnoModel();
      MnoModel mnoModelSupplier=new MnoModel();
      MnoModel mnoModel=(MnoModel)baseWrapper.getBasePersistableModel();
      /**
       * Saving New record Case
       */
      if( mnoModel.getMnoId() == null ){ // new record case
    	  newMnoModel.setName(mnoModel.getName());
    	  ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
  		exampleHolder.setMatchMode(MatchMode.EXACT);
    	  int recordCountMnoName = mnoDAO.countByExample(newMnoModel,exampleHolder);

    	  //MNO name unique and supplier not given
    	  if(recordCountMnoName==0 && mnoModel.getSupplierId()==null){
    		  mnoModel = this.mnoDAO.saveOrUpdate( (
	                    MnoModel) baseWrapper.getBasePersistableModel());
	          baseWrapper.setBasePersistableModel(mnoModel);
	          
	          Long currentUserId = UserUtils.getCurrentUser().getAppUserId();
	          //save mno in concern parntner table also
	          ConcernPartnerModel concernPartnerModel = new ConcernPartnerModel();    
	          concernPartnerModel.setActive(mnoModel.getActive());
	          concernPartnerModel.setMnoId(mnoModel.getMnoId());
	          concernPartnerModel.setComments(mnoModel.getComments());
	          concernPartnerModel.setDescription(mnoModel.getDescription());
	          concernPartnerModel.setConcernPartnerTypeId(ConcernPartnerTypeConstants.MNO);
	          concernPartnerModel.setCreatedBy(currentUserId);
	          concernPartnerModel.setCreatedOn(new Date());
	          concernPartnerModel.setName(mnoModel.getName());
	          concernPartnerModel.setUpdatedBy(currentUserId);
	          concernPartnerModel.setUpdatedOn(new Date());
	          concernPartnerDAO.saveOrUpdate(concernPartnerModel);
	          
	          //saved data in partner and partner_permission_group
	          PartnerModel partnerModel = new PartnerModel();
	          partnerModel.setName(mnoModel.getName());
	   	   	  partnerModel.setAppUserTypeId(UserTypeConstantsInterface.MNO);
	   	      partnerModel.setMnoId(mnoModel.getMnoId());
	   	      partnerModel.setComments(mnoModel.getComments());
	   	      partnerModel.setActive(mnoModel.getActive());
	   	      partnerModel.setDescription(mnoModel.getDescription());
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
          //MNO name is unique and supplier is given, have to check supplier is unique
    	  else if(recordCountMnoName == 0 && mnoModel.getSupplierId()!=null){
    		  mnoModelSupplier.setSupplierId(mnoModel.getSupplierId());
    		  int recordCountSupplier = mnoDAO.countByExample(mnoModelSupplier);
    		  if(recordCountSupplier==0){
    			  mnoModel = this.mnoDAO.saveOrUpdate( (
    	                    MnoModel) baseWrapper.getBasePersistableModel());
    	          baseWrapper.setBasePersistableModel(mnoModel);
    	          
    	          Long currentUserId = UserUtils.getCurrentUser().getAppUserId();
    	          //saved data in partner and partner_permission_group
    	          PartnerModel partnerModel = new PartnerModel();
    	          partnerModel.setName(mnoModel.getName());
    	   	   	  partnerModel.setAppUserTypeId(UserTypeConstantsInterface.MNO);
    	   	      partnerModel.setMnoId(mnoModel.getMnoId());
    	   	      partnerModel.setComments(mnoModel.getComments());
    	   	      partnerModel.setActive(mnoModel.getActive());
    	   	      partnerModel.setDescription(mnoModel.getDescription());
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
    		  else{
    			  throw new FrameworkCheckedException("SupplierUniqueException");
    		  }
    	  }
          //MNO name is not unique
    	  else{
    		  throw new FrameworkCheckedException("MnoNameUniqueException");
    	  }
      }
      /**
       * Updating a record case
       */
      else{ // update record case
    	  mnoModel = this.mnoDAO.saveOrUpdate( (
                  MnoModel) baseWrapper.getBasePersistableModel());
          baseWrapper.setBasePersistableModel(mnoModel);
          
          
          //update mno in concern parntner table also
          ConcernPartnerModel concernPartnerModel = new ConcernPartnerModel();
          concernPartnerModel.setConcernPartnerTypeId(ConcernPartnerTypeConstants.MNO);
          concernPartnerModel.setName(mnoModel.getName());
          
          ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
          exampleHolder.setMatchMode(MatchMode.EXACT);
          
          CustomList<ConcernPartnerModel>concernCList = concernPartnerDAO.findByExample(concernPartnerModel,null,null,exampleHolder);
          List <ConcernPartnerModel>list = concernCList.getResultsetList();

          if(!list.isEmpty()){
        	  concernPartnerModel = list.get(0);
        	  concernPartnerModel.setActive(mnoModel.getActive());
              concernPartnerModel.setComments(mnoModel.getComments());
              concernPartnerModel.setDescription(mnoModel.getDescription());
              concernPartnerModel.setName(mnoModel.getName());
              concernPartnerModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
              concernPartnerModel.setUpdatedOn(new Date());
              concernPartnerDAO.saveOrUpdate(concernPartnerModel);
          }
          
          return baseWrapper;
      }
      /*
      ////////////////////////
      if (mnoModel.getSupplierId()!=null)
        {
        	mnoModelSupplier.setSupplierId(mnoModel.getSupplierId());	
        }
        newMnoModel.setName(mnoModel.getName());

        int recordCount=mnoDAO.countByExample(newMnoModel);
        int recordCountSupplier=mnoDAO.countByExample(mnoModelSupplier);
        //***Check if name already exists
        if(recordCount == 0 && recordCountSupplier==0 )
        {
          mnoModel = this.mnoDAO.saveOrUpdate( (
              MnoModel) baseWrapper.getBasePersistableModel());
          baseWrapper.setBasePersistableModel(mnoModel);
          return baseWrapper;
        }
        else if(recordCount == 0 && mnoModel.getSupplierId()==null){
        	System.out.println("Inside the supplier id is null");
        	mnoModel = this.mnoDAO.saveOrUpdate( (
                    MnoModel) baseWrapper.getBasePersistableModel());
            baseWrapper.setBasePersistableModel(mnoModel);
            return baseWrapper;
        }
        else
        {
          //set baseWrapper to null if record exists
        	if (recordCount!=0)
        	{
        		throw new FrameworkCheckedException("MnoNameUniqueException");
        		
        	}
        	else if (recordCountSupplier!=0)
        	{
        		
        		throw new FrameworkCheckedException("SupplierUniqueException");
        	}
          baseWrapper.setBasePersistableModel(null);
          return baseWrapper;
        } */
    }
  
  public List<SupplierModel> searchSupplierModels() {

		List supplierModelList = null;
		List supplierModelListView = new ArrayList();
		
		String supplierHQL = " select sp.id,sp.name from  SupplierModel sp "
			+ "where sp.supplierId NOT IN (select distinct mno.relationSupplierIdSupplierModel.supplierId from MnoModel mno "
			+" where mno.relationSupplierIdSupplierModel.supplierId is not null)"
			+ "and sp.active= true "
			+ "order by sp.name";
		
		
		//SELECT s.* FROM supplier s WHERE s.supplier_id NOT IN (SELECT distinct supplier_id FROM mno where supplier_id IS NOT null)


		supplierModelList = genericDAO.findByHQL(supplierHQL);

		SupplierModel supplierModel = null;
		for (int count = 0; count < supplierModelList.size(); count++) {
			supplierModel = new SupplierModel();

			Object obj[] = (Object[]) supplierModelList.get(count);

			supplierModel.setPrimaryKey((Long) obj[0]);
			supplierModel.setName((String) obj[1]);

			supplierModelListView.add(supplierModel);

		}
		return supplierModelListView;
	}

	public SearchBaseWrapper searchMnoByMnoUser(SearchBaseWrapper searchBaseWrapper)throws
			FrameworkCheckedException
	{
		MnoUserModel mnoUserModel=(MnoUserModel)searchBaseWrapper.getBasePersistableModel();
		MnoModel mnoModel=new MnoModel();
		mnoModel.setMnoId(mnoUserModel.getMnoId());
		mnoModel=mnoDAO.findByPrimaryKey(mnoUserModel.getMnoId());
		searchBaseWrapper.setBasePersistableModel(mnoModel);
		return searchBaseWrapper;
	}


  public void setMnoDAO(MnoDAO mnoDAO)
  {
    this.mnoDAO = mnoDAO;
  }

  public void setMnoListViewDAO(MnoListViewDAO mnoListViewDAO)
  {
    this.mnoListViewDAO = mnoListViewDAO;
  }

public void setConcernPartnerDAO(ConcernPartnerDAO concernPartnerDAO) {
	this.concernPartnerDAO = concernPartnerDAO;
}

public void setPartnerDAO(PartnerDAO partnerDAO) {
	this.partnerDAO = partnerDAO;
}

public void setPartnerPermissionGroupDAO(
		PartnerPermissionGroupDAO partnerPermissionGroupDAO) {
	this.partnerPermissionGroupDAO = partnerPermissionGroupDAO;
}

}
