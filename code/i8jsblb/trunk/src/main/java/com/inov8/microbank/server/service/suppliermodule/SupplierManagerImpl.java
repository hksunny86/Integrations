package com.inov8.microbank.server.service.suppliermodule;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.MatchMode;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.ConcernPartnerModel;
import com.inov8.microbank.common.model.PartnerModel;
import com.inov8.microbank.common.model.PartnerPermissionGroupModel;
import com.inov8.microbank.common.model.SupplierModel;
import com.inov8.microbank.common.model.suppliermodule.SupplierListViewModel;
import com.inov8.microbank.common.util.ConcernPartnerTypeConstants;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.wrapper.suppliermodule.SupplierWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.dao.portal.concernmodule.ConcernPartnerDAO;
import com.inov8.microbank.server.dao.suppliermodule.SupplierDAO;
import com.inov8.microbank.server.dao.suppliermodule.SupplierListViewDAO;
import com.inov8.microbank.server.dao.usergroupmodule.PartnerDAO;
import com.inov8.microbank.server.dao.usergroupmodule.PartnerPermissionGroupDAO;

/**
 * @author Jawwad Farooq
 * @version 1.0
 */

public class SupplierManagerImpl
    implements SupplierManager
{
  private SupplierDAO supplierDAO;
  private SupplierListViewDAO supplierListViewDAO;
  private ConcernPartnerDAO concernPartnerDAO;
  private PartnerDAO partnerDAO;
  private PartnerPermissionGroupDAO partnerPermissionGroupDAO;
  
  public SupplierManagerImpl()
  {
  }

  /**
   * This method retrieves the fully-qualified name of Supplier's implementation class from DB. Query is performed
   * on the supplier's ID.
   * @param supplierInfo SupplierWrapper
   * @return SupplierWrapper
   * @throws FrameworkCheckedException
   */
  public SupplierWrapper getSupplierClassPath(SupplierWrapper supplierInfo) throws
      FrameworkCheckedException
  {
    supplierInfo.getProductTypeProductModel().setSupplierIdSupplierModel(this.
        supplierDAO.
        findByPrimaryKey(supplierInfo.getProductTypeProductModel().
                         getSupplierIdSupplierModel().
                         getSupplierId()));

    System.out.println(" Contact : " +
                       supplierInfo.getProductTypeProductModel().
                       getSupplierIdSupplierModel().
                       getContactName());
    System.out.println(" Created On : " +
                       supplierInfo.getProductTypeProductModel().
                       getSupplierIdSupplierModel().getCreatedOn());
    System.out.println(" Updated On : " +
                       supplierInfo.getProductTypeProductModel().
                       getSupplierIdSupplierModel().getUpdatedOn());

    return supplierInfo;
  }

  public SearchBaseWrapper loadSupplier(SearchBaseWrapper searchBaseWrapper)
  {
    SupplierModel supplierModel = this.supplierDAO.findByPrimaryKey(
        searchBaseWrapper.getBasePersistableModel().
        getPrimaryKey());
    searchBaseWrapper.setBasePersistableModel(supplierModel);
    
    //load supplier permission from partner_permission_group
    PartnerModel tmpPartnerModel = new PartnerModel();
    tmpPartnerModel.setSupplierId(supplierModel.getSupplierId());
    tmpPartnerModel.setAppUserTypeId(UserTypeConstantsInterface.SUPPLIER);
    
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
  public BaseWrapper loadSupplier(BaseWrapper baseWrapper)
  {
    SupplierModel supplierModel = this.supplierDAO.findByPrimaryKey(baseWrapper.
        getBasePersistableModel().
        getPrimaryKey());
    baseWrapper.setBasePersistableModel(supplierModel);
    return baseWrapper;
  }

  public SearchBaseWrapper searchSupplier(SearchBaseWrapper searchBaseWrapper)
  {
    CustomList<SupplierListViewModel>
        list = this.supplierListViewDAO.findByExample( (SupplierListViewModel)
        searchBaseWrapper.getBasePersistableModel(),
        searchBaseWrapper.getPagingHelperModel(),
        searchBaseWrapper.getSortingOrderMap());
    searchBaseWrapper.setCustomList(list);
    return searchBaseWrapper;
  }

  public BaseWrapper createOrUpdateSupplier(BaseWrapper baseWrapper) throws FrameworkCheckedException
  {
	
    SupplierModel newSupplierModel = new SupplierModel();
    SupplierModel supplierModel = (SupplierModel) baseWrapper.
        getBasePersistableModel();
    Long supplierId = supplierModel.getSupplierId(); 
    
    newSupplierModel.setName(supplierModel.getName());
    ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
	exampleHolder.setMatchMode(MatchMode.EXACT);
    int recordCount = supplierDAO.countByExample(newSupplierModel, exampleHolder);
    //***Check if name already exists
     if (recordCount == 0 || supplierModel.getSupplierId() != null)
     {
       supplierModel = this.supplierDAO.saveOrUpdate( (
           SupplierModel) baseWrapper.getBasePersistableModel());
       baseWrapper.setBasePersistableModel(supplierModel);
       
       Long currentUserId = UserUtils.getCurrentUser().getAppUserId();

       ConcernPartnerModel concernPartnerModel = new ConcernPartnerModel();
       PartnerModel partnerModel = new PartnerModel();
	    if(supplierId == null){   
       		//save supplier in concern parntner table also
	       concernPartnerModel.setActive(supplierModel.getActive());
	       concernPartnerModel.setSupplierId(supplierModel.getSupplierId());
	       concernPartnerModel.setComments(supplierModel.getComments());
	       concernPartnerModel.setDescription(supplierModel.getDescription());
	       concernPartnerModel.setConcernPartnerTypeId(ConcernPartnerTypeConstants.SUPPLIER);
	       concernPartnerModel.setCreatedBy(currentUserId);
	       concernPartnerModel.setCreatedOn(new Date());
	       concernPartnerModel.setName(supplierModel.getName());
	       concernPartnerModel.setUpdatedBy(currentUserId);
	       concernPartnerModel.setUpdatedOn(new Date());
	       
	       
 	       //saved data in partner and partner_permission_group    
	       partnerModel.setName(supplierModel.getName());
		   partnerModel.setAppUserTypeId(UserTypeConstantsInterface.SUPPLIER);
		   partnerModel.setSupplierId(supplierModel.getSupplierId());
		   partnerModel.setComments(supplierModel.getComments());
		   partnerModel.setDescription(supplierModel.getDescription());
		   partnerModel.setActive(supplierModel.getActive());
		   partnerModel.setCreatedBy(currentUserId);
		   partnerModel.setUpdatedBy(currentUserId);
		   partnerModel.setUpdatedOn(new Date());
		   partnerModel.setCreatedOn(new Date());
		   
		   Long permissionGroupId = new Long((String)baseWrapper.getObject("permissionGroupId"));
		   
		   PartnerPermissionGroupModel partnerPermissionGroupModel = new PartnerPermissionGroupModel();
//		   partnerPermissionGroupModel.setPartnerId(partnerModel.getPartnerId());
		   partnerPermissionGroupModel.setPermissionGroupId(permissionGroupId);		       
		   partnerPermissionGroupModel.setCreatedBy(currentUserId);
		   partnerPermissionGroupModel.setUpdatedBy(currentUserId);
		   partnerPermissionGroupModel.setUpdatedOn(new Date());
		   partnerPermissionGroupModel.setCreatedOn(new Date());

		   partnerModel.addPartnerIdPartnerPermissionGroupModel(partnerPermissionGroupModel);
			
		   //partnerPermissionGroupDAO.saveOrUpdate(partnerPermissionGroupModel);
		   
		   partnerDAO.saveOrUpdate(partnerModel);
	       
	    }else{
	    	  concernPartnerModel.setSupplierId(supplierId);
	    	  concernPartnerModel.setConcernPartnerTypeId(ConcernPartnerTypeConstants.SUPPLIER);
	    	  CustomList<ConcernPartnerModel>concernCList = concernPartnerDAO.findByExample(concernPartnerModel,null,null,exampleHolder);
	          List <ConcernPartnerModel>list = concernCList.getResultsetList();

	          if(!list.isEmpty()){
	        	  concernPartnerModel = list.get(0);
	        	  concernPartnerModel.setActive(supplierModel.getActive());
	              concernPartnerModel.setComments(supplierModel.getComments());
	              concernPartnerModel.setDescription(supplierModel.getDescription());
	              concernPartnerModel.setName(supplierModel.getName());
	              concernPartnerModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
	              concernPartnerModel.setUpdatedOn(new Date());
	          }	    	
	    }
	       concernPartnerDAO.saveOrUpdate(concernPartnerModel);
 	       
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
   * This method delegates the requests to the Supplier's implementation class for some task and retrieves
   * information from the supplier.
   * @param supplierInfo SupplierWrapper
   * @throws Exception
   */
  public SupplierWrapper verify(SupplierWrapper supplierInfo,
                                WorkFlowWrapper workFlowWrapper) throws
      Exception
  {
    Supplier supplier = getSupplier(supplierInfo);
    return supplier.verify(supplierInfo, workFlowWrapper);
  }

  /**
   * This method delegates the requests to the Supplier's implementation class and updates
   * the supplier.
   * @param supplierInfo SupplierWrapper
   * @throws Exception
   */
  public SupplierWrapper updateSupplier(SupplierWrapper supplierInfo) throws
      Exception
  {
    Supplier supplier = getSupplier(supplierInfo);
    return supplier.updateSupplier(supplierInfo);
  }

  /**
   *
   * @throws Exception
   */
  public SupplierWrapper rollback(SupplierWrapper supplierWrapper) throws
      Exception
  {
    Supplier supplier = getSupplier(supplierWrapper);
    return supplier.rollback(supplierWrapper);
  }

  /**
   * This method calls the SupplierFactory to get the required Supplier's implementation class
   * @param supplierWrapper SupplierWrapper
   * @return Supplier
   * @throws Exception
   */
  private Supplier getSupplier(SupplierWrapper supplierWrapper) throws
      Exception
  {
    return new SupplierStub();
    /**
     * @todo ****** UN-COMMENT THE FOLLOWING LINE OF CODE ********
     */
//    return SupplierFactory.getSupplier(supplierWrapper);
  }

  public void setSupplierListViewDAO(SupplierListViewDAO supplierListViewDAO)
  {
    this.supplierListViewDAO = supplierListViewDAO;
  }

  public void setSupplierDAO(SupplierDAO supplierDAO)
  {
    this.supplierDAO = supplierDAO;

  }

public List getServicesAgainstSupplier(Long supplierId) throws FrameworkCheckedException {
	// TODO Auto-generated method stub
	return this.supplierDAO.getServicesAgainstSupplier(supplierId);
	
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
