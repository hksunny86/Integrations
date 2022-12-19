package com.inov8.microbank.server.service.retailermodule;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.MatchMode;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.ConcernPartnerModel;
import com.inov8.microbank.common.model.PartnerModel;
import com.inov8.microbank.common.model.PartnerPermissionGroupModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.RetailerModel;
import com.inov8.microbank.common.model.retailermodule.RetailerListViewModel;
import com.inov8.microbank.common.util.ConcernPartnerTypeConstants;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.dao.portal.concernmodule.ConcernPartnerDAO;
import com.inov8.microbank.server.dao.retailermodule.RetailerDAO;
import com.inov8.microbank.server.dao.retailermodule.RetailerListViewDAO;
import com.inov8.microbank.server.dao.usergroupmodule.PartnerDAO;
import com.inov8.microbank.server.dao.usergroupmodule.PartnerPermissionGroupDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;

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
public class RetailerManagerImpl
    implements RetailerManager
{

  private RetailerDAO retailerDAO;
  private RetailerListViewDAO retailerListViewDAO;
  private PartnerDAO partnerDAO;
  private PartnerPermissionGroupDAO partnerPermissionGroupDAO; 
  private ConcernPartnerDAO concernPartnerDAO;
  private ActionLogManager actionLogManager;

  public SearchBaseWrapper loadRetailer(SearchBaseWrapper searchBaseWrapper)
  {
    RetailerModel retailerModel = this.retailerDAO.findByPrimaryKey(
        searchBaseWrapper.getBasePersistableModel().
        getPrimaryKey());
    searchBaseWrapper.setBasePersistableModel(retailerModel);
    
    //load Retailor permission from partner_permission_group
    PartnerModel tmpPartnerModel = new PartnerModel();
    tmpPartnerModel.setRetailerId(retailerModel.getRetailerId());
    tmpPartnerModel.setAppUserTypeId(UserTypeConstantsInterface.RETAILER);
    
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
   * This is meant specifically for the deactivate case
   * @param baseWrapper BaseWrapper
   * @return BaseWrapper
   */
  public BaseWrapper loadRetailer(BaseWrapper baseWrapper)
  {
    RetailerModel retailerModel = this.retailerDAO.findByPrimaryKey(baseWrapper.
        getBasePersistableModel().
        getPrimaryKey());
    baseWrapper.setBasePersistableModel(retailerModel);
    return baseWrapper;
  }

  public SearchBaseWrapper searchRetailer(SearchBaseWrapper searchBaseWrapper)
  {
    CustomList<RetailerListViewModel>
        list = this.retailerListViewDAO.findByExample( (RetailerListViewModel)
        searchBaseWrapper.getBasePersistableModel(),
        searchBaseWrapper.getPagingHelperModel(),
        searchBaseWrapper.getSortingOrderMap());
    searchBaseWrapper.setCustomList(list);
    return searchBaseWrapper;
  }

  public BaseWrapper createOrUpdateRetailer(BaseWrapper baseWrapper) throws FrameworkCheckedException
  {
    RetailerModel newRetailerModel = new RetailerModel();
    RetailerModel retailerModel = (RetailerModel) baseWrapper.
        getBasePersistableModel();
    newRetailerModel.setName(retailerModel.getName());
    
    ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
	exampleHolder.setMatchMode(MatchMode.EXACT);
    int recordCount = retailerDAO.countByExample(newRetailerModel, exampleHolder);
 
	  Long currentUserId = UserUtils.getCurrentUser().getAppUserId();
    
    //***Check if name already exists
     if (recordCount == 0 || retailerModel.getRetailerId() == null)
     {
    	baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.RETAILER_FORM_USECASE_ID);
     	baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
		ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

    	 retailerModel = this.retailerDAO.saveOrUpdate( (
           RetailerModel) baseWrapper.getBasePersistableModel());
       baseWrapper.setBasePersistableModel(retailerModel);

	      //save retailer in concern parntner table also
	      ConcernPartnerModel concernPartnerModel = new ConcernPartnerModel(); 
	      concernPartnerModel.setActive(retailerModel.getActive());
	      concernPartnerModel.setRetailerId(retailerModel.getRetailerId());
	      //concernPartnerModel.setComments(bankModel.getComments());
	      concernPartnerModel.setDescription(retailerModel.getDescription());
	      concernPartnerModel.setConcernPartnerTypeId(ConcernPartnerTypeConstants.RETAILER);
	      concernPartnerModel.setCreatedBy(currentUserId);
	      concernPartnerModel.setCreatedOn(new Date());
	      concernPartnerModel.setName(retailerModel.getName());
	      concernPartnerModel.setUpdatedBy(currentUserId);
	      concernPartnerModel.setUpdatedOn(new Date());
	      concernPartnerDAO.saveOrUpdate(concernPartnerModel);

       
          //saved data in partner and partner_permission_group
          PartnerModel partnerModel = new PartnerModel();
          partnerModel.setName(retailerModel.getName());
       	  partnerModel.setAppUserTypeId(UserTypeConstantsInterface.RETAILER);
          partnerModel.setRetailerId(retailerModel.getRetailerId());
          partnerModel.setActive(retailerModel.getActive());
          partnerModel.setComments(retailerModel.getComments());
          partnerModel.setDescription(retailerModel.getDescription());
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
       
        actionLogModel.setCustomField1(retailerModel.getRetailerId().toString());
        actionLogModel.setCustomField1(retailerModel.getName());
		this.actionLogManager.completeActionLog(actionLogModel);
       
       return baseWrapper;

     }
     else if (recordCount == 0 || retailerModel.getRetailerId() != null)
     {
    	baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.RETAILER_FORM_USECASE_ID);
    	baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
		ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
      
    	retailerModel = this.retailerDAO.saveOrUpdate( (RetailerModel) baseWrapper.getBasePersistableModel());
        baseWrapper.setBasePersistableModel(retailerModel);
 		
        actionLogModel.setCustomField1(retailerModel.getRetailerId().toString());
		this.actionLogManager.completeActionLog(actionLogModel);
     }else{
         //set baseWrapper to null if record exists
         baseWrapper.setBasePersistableModel(null); 
     }
     return baseWrapper;
  }
  
  public void setRetailerDAO(RetailerDAO retailerDAO)
  {
    this.retailerDAO = retailerDAO;
  }

  public void setRetailerListViewDAO(RetailerListViewDAO
                                     retailerListViewDAO)
  {
    this.retailerListViewDAO = retailerListViewDAO;
  }

public void setPartnerDAO(PartnerDAO partnerDAO) {
	this.partnerDAO = partnerDAO;
}

public void setPartnerPermissionGroupDAO(
		PartnerPermissionGroupDAO partnerPermissionGroupDAO) {
	this.partnerPermissionGroupDAO = partnerPermissionGroupDAO;
}

public void setConcernPartnerDAO(ConcernPartnerDAO concernPartnerDAO) {
	this.concernPartnerDAO = concernPartnerDAO;
}

public void setActionLogManager(ActionLogManager actionLogManager)
{
	this.actionLogManager = actionLogManager;
}

}
