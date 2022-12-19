package com.inov8.microbank.server.service.bankmodule;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.MatchMode;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.ConcernPartnerModel;
import com.inov8.microbank.common.model.FinancialIntegrationModel;
import com.inov8.microbank.common.model.PartnerModel;
import com.inov8.microbank.common.model.PartnerPermissionGroupModel;
import com.inov8.microbank.common.model.bankmodule.BankListViewModel;
import com.inov8.microbank.common.util.ConcernPartnerTypeConstants;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.dao.bankmodule.BankDAO;
import com.inov8.microbank.server.dao.bankmodule.BankListViewDAO;
import com.inov8.microbank.server.dao.portal.concernmodule.ConcernPartnerDAO;
import com.inov8.microbank.server.dao.usergroupmodule.PartnerDAO;
import com.inov8.microbank.server.dao.usergroupmodule.PartnerPermissionGroupDAO;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Rizwan-ur-Rehman
 * @version 1.0
 */


public class BankManagerImpl
    implements BankManager
{

  private BankDAO bankDAO;
  private BankListViewDAO bankListViewDAO;
  private ConcernPartnerDAO concernPartnerDAO;
  private GenericDao genericDao;
  private PartnerDAO partnerDAO;
  private PartnerPermissionGroupDAO partnerPermissionGroupDAO;

  public BankManagerImpl()
  {
  }

  public SearchBaseWrapper loadBank(SearchBaseWrapper searchBaseWrapper)
  {
    BankModel bankModel = this.bankDAO.findByPrimaryKey(searchBaseWrapper.
        getBasePersistableModel().
        getPrimaryKey());
    searchBaseWrapper.setBasePersistableModel(bankModel);
    
    //load Bank permission from partner_permission_group
    PartnerModel tmpPartnerModel = new PartnerModel();
    tmpPartnerModel.setBankId(bankModel.getBankId());
    tmpPartnerModel.setAppUserTypeId(UserTypeConstantsInterface.BANK);
    
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

  public BaseWrapper loadBank(BaseWrapper baseWrapper)
  {
    BankModel bankModel = this.bankDAO.findByPrimaryKey(baseWrapper.
        getBasePersistableModel().
        getPrimaryKey());
    baseWrapper.setBasePersistableModel(bankModel);
    return baseWrapper;
  }

  public SearchBaseWrapper searchBank(SearchBaseWrapper searchBaseWrapper)
  {
    CustomList<BankListViewModel>
        list = this.bankListViewDAO.findByExample( (BankListViewModel)
                                                  searchBaseWrapper.
                                                  getBasePersistableModel(),
                                                  searchBaseWrapper.
                                                  getPagingHelperModel(),
                                                  searchBaseWrapper.
                                                  getSortingOrderMap());
    searchBaseWrapper.setCustomList(list);
    return searchBaseWrapper;
  }

  public SearchBaseWrapper searchBankByExample(SearchBaseWrapper searchBaseWrapper)
  {
    CustomList<BankModel>
        list = this.bankDAO.findByExample( (BankModel)
                                                  searchBaseWrapper.
                                                  getBasePersistableModel(),
                                                  searchBaseWrapper.
                                                  getPagingHelperModel(),
                                                  searchBaseWrapper.
                                                  getSortingOrderMap());
    searchBaseWrapper.setCustomList(list);
    return searchBaseWrapper;
  }
  
  public BaseWrapper updateBank(BaseWrapper baseWrapper) throws FrameworkCheckedException
  {
    BankModel bankModel = (BankModel) baseWrapper.getBasePersistableModel();
    BankModel newBankModel = new BankModel();
    newBankModel.setBankId(bankModel.getBankId());

    /**
     * code for checking the verifly using bank and financial instituiton matches and vice versa
     */
    FinancialIntegrationModel financialIntegrationModel = new FinancialIntegrationModel();
    financialIntegrationModel.setFinancialIntegrationId(bankModel.getFinancialIntegrationId());
    
    financialIntegrationModel = this.genericDao.getEntityByPrimaryKey(financialIntegrationModel);
    
    
    if(financialIntegrationModel != null)
    {

    	 if(financialIntegrationModel.getIsUsingVerifly() && bankModel.getVeriflyId() == null)
    	 {
    		 throw new FrameworkCheckedException("VeriflyAndFinancialInstitutionDoNotMatch");	 
    	 }
    	 else if(!financialIntegrationModel.getIsUsingVerifly() && bankModel.getVeriflyId() != null)
    	 {
    		 throw new FrameworkCheckedException("VeriflyAndFinancialInstitutionDoNotMatch");
    	 }
    	
    	
    }
    
    int recordCount = bankDAO.countByExample(newBankModel);

    if (recordCount != 0 && bankModel.getPrimaryKey() != null)
    {
      bankModel = this.bankDAO.saveOrUpdate( (BankModel) baseWrapper.
                                            getBasePersistableModel());
      baseWrapper.setBasePersistableModel(bankModel);
      
      Long currentUserId = UserUtils.getCurrentUser().getAppUserId();

      //update bank in concern parntner table also
      ConcernPartnerModel concernPartnerModel = new ConcernPartnerModel();      
      concernPartnerModel.setConcernPartnerTypeId(ConcernPartnerTypeConstants.BANK);      
      concernPartnerModel.setName(bankModel.getName());
      
      ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
      exampleHolder.setMatchMode(MatchMode.EXACT);
      CustomList<ConcernPartnerModel>concernCList = concernPartnerDAO.findByExample(concernPartnerModel,null,null,exampleHolder);
      List <ConcernPartnerModel>list = concernCList.getResultsetList();

      if(!list.isEmpty()){
    	  concernPartnerModel = list.get(0);
    	  concernPartnerModel.setActive(bankModel.getActive());
          concernPartnerModel.setComments(bankModel.getComments());
          concernPartnerModel.setDescription(bankModel.getDescription());
          concernPartnerModel.setName(bankModel.getName());
          concernPartnerModel.setUpdatedBy(currentUserId);
          concernPartnerModel.setUpdatedOn(new Date());
          concernPartnerDAO.saveOrUpdate(concernPartnerModel);
      }
      
      return baseWrapper;
    }
    else
    {
      baseWrapper.setBasePersistableModel(null);
      return baseWrapper;
    }
  }

  public BaseWrapper createBank(BaseWrapper baseWrapper) throws  FrameworkCheckedException
  {
    int recordCount;
    Date nowDate = new Date();

    BankModel newBankModel = new BankModel();
    BankModel bankModel = (BankModel) baseWrapper.getBasePersistableModel();
    /**
     * code for checking the verifly using bank and financial instituiton matches and vice versa
     */
    FinancialIntegrationModel financialIntegrationModel = new FinancialIntegrationModel();
    financialIntegrationModel.setFinancialIntegrationId(bankModel.getFinancialIntegrationId());
    
    financialIntegrationModel = this.genericDao.getEntityByPrimaryKey(financialIntegrationModel);
    
    
    if(financialIntegrationModel != null)
    {

    	 if(financialIntegrationModel.getIsUsingVerifly() && bankModel.getVeriflyId() == null)
    	 {
    		 throw new FrameworkCheckedException("VeriflyAndFinancialInstitutionDoNotMatch");	 
    	 }
    	 else if(!financialIntegrationModel.getIsUsingVerifly() && bankModel.getVeriflyId() != null)
    	 {
    		 throw new FrameworkCheckedException("VeriflyAndFinancialInstitutionDoNotMatch");
    	 }
    	
    	
    }

    
    ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
	exampleHolder.setMatchMode(MatchMode.EXACT);

    

    newBankModel.setName(bankModel.getName());
    recordCount = bankDAO.countByExample(newBankModel,exampleHolder);

    //***Check if Record already exists

     bankModel.setCreatedOn(nowDate);
    bankModel.setUpdatedOn(nowDate);

    if (recordCount == 0)
    {
    	
      bankModel = this.bankDAO.saveOrUpdate(bankModel);	
      baseWrapper.setBasePersistableModel(bankModel);
      
      Long currentUserId = UserUtils.getCurrentUser().getAppUserId();
      
      //save bank in concern parntner table also
      ConcernPartnerModel concernPartnerModel = new ConcernPartnerModel(); 
      concernPartnerModel.setActive(bankModel.getActive());
      concernPartnerModel.setBankId(bankModel.getBankId());
      concernPartnerModel.setComments(bankModel.getComments());
      concernPartnerModel.setDescription(bankModel.getDescription());
      concernPartnerModel.setConcernPartnerTypeId(ConcernPartnerTypeConstants.BANK);
      concernPartnerModel.setCreatedBy(currentUserId);
      concernPartnerModel.setCreatedOn(new Date());
      concernPartnerModel.setName(bankModel.getName());
      concernPartnerModel.setUpdatedBy(currentUserId);
      concernPartnerModel.setUpdatedOn(new Date());
      concernPartnerDAO.saveOrUpdate(concernPartnerModel);
      
       //saved data in partner and partner_permission_group
       PartnerModel partnerModel = new PartnerModel();
       partnerModel.setName(bankModel.getName());
	   partnerModel.setAppUserTypeId(UserTypeConstantsInterface.BANK);
	   partnerModel.setBankId(bankModel.getBankId());
	   partnerModel.setActive(bankModel.getActive());
	   partnerModel.setComments(bankModel.getComments());
	   partnerModel.setDescription(bankModel.getDescription());
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
    else
    {
      baseWrapper.setBasePersistableModel(null);
      return baseWrapper;
    }
  }
  
 

  public void setBankListViewDAO(BankListViewDAO bankListViewDAO)
  {
    this.bankListViewDAO = bankListViewDAO;
  }

  public void setBankDAO(BankDAO bankDAO)
  {
    this.bankDAO = bankDAO;
  }

public void setConcernPartnerDAO(ConcernPartnerDAO concernPartnerDAO) {
	this.concernPartnerDAO = concernPartnerDAO;
}

public void setGenericDao(GenericDao genericDao) {
	this.genericDao = genericDao;
}

public void setPartnerDAO(PartnerDAO partnerDAO) {
	this.partnerDAO = partnerDAO;
}

public void setPartnerPermissionGroupDAO(
		PartnerPermissionGroupDAO partnerPermissionGroupDAO) {
	this.partnerPermissionGroupDAO = partnerPermissionGroupDAO;
}

}
