package com.inov8.microbank.server.service.stakeholdermodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.StakeholderBankInfoModel;
import com.inov8.microbank.common.model.stakeholdermodule.StakehldAcctMapListViewModel;
import com.inov8.microbank.common.model.stakeholdermodule.StakehldBankInfoListViewModel;
import com.inov8.microbank.common.util.BankConstantsInterface;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.server.dao.stakeholdermodule.StakeHolderBankInfoListViewDAO;
import com.inov8.microbank.server.dao.stakeholdermodule.StakehldAcctMapListViewModelDAO;
import com.inov8.microbank.server.dao.stakeholdermodule.StakeholderBankInfoDAO;
import com.inov8.ola.integration.service.OLAServiceImpl;
import com.inov8.ola.integration.vo.OLAVO;
import org.hibernate.criterion.MatchMode;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backened application for POS terminal</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Ltd</p>
 *
 * @author Ahmad Iqbal
 * @version 1.0
 *
 */

public class StakeholderBankInfoManagerImpl
    implements StakeholderBankInfoManager
{
   private StakeholderBankInfoDAO stakeholderBankInfoDAO;
   private StakeHolderBankInfoListViewDAO stakeHolderBankInfoListViewDAO;
   private StakehldAcctMapListViewModelDAO stakehldAcctMapListViewModelDAO;
   private OLAServiceImpl olaServiceImpl;

  public void setStakeHolderBankInfoListViewDAO(
		StakeHolderBankInfoListViewDAO stakeHolderBankInfoListViewDAO) {
	this.stakeHolderBankInfoListViewDAO = stakeHolderBankInfoListViewDAO;
}

public BaseWrapper createOrUpdateStakeholderBankInfo(BaseWrapper baseWrapper) throws FrameworkCheckedException
  {
    /*StakeholderBankInfoModel newStakeholderBankInfoModel = new
        StakeholderBankInfoModel();*/
    StakeholderBankInfoModel stakeholderBankInfoModel = (StakeholderBankInfoModel)baseWrapper.getBasePersistableModel();
    if (stakeholderBankInfoModel.getStakeholderBankInfoId()!=null)
    {
    	/*
    	if (!stakeholderBankInfoModel.getName().equals(stakeholderBankInfoModel.getName()))
		{

			if (!this.isUserNameUnique(stakeholderBankInfoModel.getName()))
			{
					throw new FrameworkCheckedException(
							"NameUniqueException");
				
			}
		}
    	*/
    	stakeholderBankInfoModel = this.stakeholderBankInfoDAO.saveOrUpdate( (StakeholderBankInfoModel) baseWrapper.getBasePersistableModel());
    	       baseWrapper.setBasePersistableModel(stakeholderBankInfoModel);
    	       return baseWrapper;
    	
    	
    }
    
    else
    {
    	/*
    	if (!this.isUserNameUnique(stakeholderBankInfoModel.getName()))
			throw new FrameworkCheckedException("NameUniqueException");
    	*/
    	stakeholderBankInfoModel = this.stakeholderBankInfoDAO.saveOrUpdate( (
    	           StakeholderBankInfoModel) baseWrapper.getBasePersistableModel());
    	       baseWrapper.setBasePersistableModel(stakeholderBankInfoModel);
    	       return baseWrapper;
    }
    
    //newStakeholderBankInfoModel.setName(stakeholderBankInfoModel.getName());

    /*int recordCount = stakeholderBankInfoDAO.countByExample(
        newStakeholderBankInfoModel);
    //***Check if name already exists
     if (recordCount == 0 ||
         newStakeholderBankInfoModel.getStakeholderBankInfoId() != null) // create case
     {
       stakeholderBankInfoModel = this.stakeholderBankInfoDAO.saveOrUpdate( (
           StakeholderBankInfoModel) baseWrapper.getBasePersistableModel());
       baseWrapper.setBasePersistableModel(stakeholderBankInfoModel);
       return baseWrapper;
     }
     else
     {
       //set baseWrapper to null if record exists
       baseWrapper.setBasePersistableModel(null);
       return baseWrapper;
     }*/

  }

  public SearchBaseWrapper searchStakeHolderBankInfo(SearchBaseWrapper searchBaseWrapper)
  {
	  ExampleConfigHolderModel configHolderModel=new ExampleConfigHolderModel();
	  configHolderModel.setMatchMode(MatchMode.EXACT);
	  
    CustomList<StakehldBankInfoListViewModel>
        list = this.stakeHolderBankInfoListViewDAO.findByExample((StakehldBankInfoListViewModel)searchBaseWrapper.getBasePersistableModel(),null,null,configHolderModel);
    searchBaseWrapper.setCustomList(list);
    return searchBaseWrapper;
  }
  
  
  public SearchBaseWrapper loadStakeHolderBankInfo(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		
		
	  StakeholderBankInfoModel stakeholderBankInfoModel = this.stakeholderBankInfoDAO
		.findByPrimaryKey(searchBaseWrapper.getBasePersistableModel()
				.getPrimaryKey());
	searchBaseWrapper.setBasePersistableModel(stakeholderBankInfoModel);
	return searchBaseWrapper;

	}

	private boolean isUserNameUnique(String userName) {
		StakeholderBankInfoModel stakeholderBankInfoModel = new StakeholderBankInfoModel();
		stakeholderBankInfoModel.setName(userName);
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT);
		int count = this.stakeholderBankInfoDAO.countByExample(stakeholderBankInfoModel,exampleHolder);
		
		if (count == 0)
			return true;
		else
			return false;
	}
	  
	
	
	  public StakeholderBankInfoModel loadStakeholderBankInfoModel(StakeholderBankInfoModel example) throws FrameworkCheckedException {

			ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
			exampleHolder.setMatchMode(MatchMode.EXACT);
			exampleHolder.setEnableLike(Boolean.FALSE);
			
			CustomList<StakeholderBankInfoModel> customList = stakeholderBankInfoDAO.findByExample(example, null, null, exampleHolder);
			
			StakeholderBankInfoModel stakeholderBankInfoModel = null;
			
			if(customList.getResultsetList().size() > 0) {
			
				stakeholderBankInfoModel = (StakeholderBankInfoModel) customList.getResultsetList().get(0);
			}
			
			return stakeholderBankInfoModel;
		}  
	  
	  
	  /**
	   * 
	   */
	  public BaseWrapper createStakeHolderAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException{
		  StakeholderBankInfoModel model =  (StakeholderBankInfoModel)baseWrapper.getBasePersistableModel();
		  
		  OLAVO olaVO = new OLAVO();
		  olaVO.setFirstName(model.getName());
		  olaVO.setLastName(model.getName());

		  //Code Added by Attique Butt on 29/09/2017 to avoid Cnic Unique index constraint on account holder
		  Calendar calendar = Calendar.getInstance();
		  calendar.setTime(new Date());
		  olaVO.setCnic("-1"+calendar.getTimeInMillis()+"1-");
		  olaVO.setDob(new Date());
		  olaVO.setMobileNumber("-1");
		  olaVO.setAddress("-1");
		  olaVO.setMiddleName("-1");
		  olaVO.setFatherName("-1");
		  olaVO.setLandlineNumber("-1");
		  olaVO.setCustomerAccountTypeId(3L);
		  olaVO.setStatusId(1L);
		  try {
			 olaVO = olaServiceImpl.createAccount(olaVO);		
			 
			 if(!StringUtil.isNullOrEmpty(olaVO.getResponseCode()) && olaVO.getResponseCode().equalsIgnoreCase("00")){
				 model.setBankId(50050L);
				 StakeholderBankInfoModel stakeholderBankInfoModel = this.stakeholderBankInfoDAO.saveOrUpdate(model);
				 if(stakeholderBankInfoModel != null){
					 if(olaVO.getPayingAccNo() != null){						 
						 stakeholderBankInfoModel.setAccountNo(olaVO.getPayingAccNo());
					 }
					 baseWrapper.setBasePersistableModel(stakeholderBankInfoModel);
				 }
			 }
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
		  return baseWrapper;
	  }
	  	  
		
		@Override
		public boolean isAccountTypeUnique(String accountNumber, Long accountTypeId){
			return stakeholderBankInfoDAO.isAccountTypeUnique(accountNumber, accountTypeId);
		}

		public List<StakeholderBankInfoModel> getStakeholderBankInfoByCommShId(Long commissionStakeholderId) throws FrameworkCheckedException {
			List<StakeholderBankInfoModel> list = null;
			
			ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
			exampleHolder.setMatchMode(MatchMode.EXACT);
			exampleHolder.setEnableLike(Boolean.FALSE);
			
			StakeholderBankInfoModel bankInfoModel = new StakeholderBankInfoModel();
			bankInfoModel.setCommissionStakeholderId(commissionStakeholderId);
			bankInfoModel.setActive(Boolean.TRUE);
			
			CustomList<StakeholderBankInfoModel> customList = stakeholderBankInfoDAO.findByExample(bankInfoModel, null, null, exampleHolder);
			
			if(customList != null && customList.getResultsetList().size() > 0) {
				list = customList.getResultsetList();
			}
			
			return list;
		} 	

		
		 @Override
		    public StakeholderBankInfoModel loadBLBStakeholderBankInfoModel(String accountNo) throws FrameworkCheckedException {
		        StakeholderBankInfoModel stakeholderBankInfoModel = loadStakeholderBankInfoModel(new StakeholderBankInfoModel(accountNo));

		        if(stakeholderBankInfoModel == null || stakeholderBankInfoModel.getBankId() == null) {
//		            logger.warn("FetchAccountTitleByAccountNoAJAXController : No stakeholder bank info record found with given account no. or bank id is null" );

		            return null;
		        }

		        Long bankId = stakeholderBankInfoModel.getBankId();
		        if(BankConstantsInterface.OLA_BANK_ID.longValue() == bankId) {
		            return stakeholderBankInfoModel;
		        }

		        if(BankConstantsInterface.ASKARI_BANK_ID.longValue() == bankId) {
		            StakeholderBankInfoModel sbiModel = new StakeholderBankInfoModel();
		            sbiModel.setOfSettlementStakeholderBankInfoModelId(stakeholderBankInfoModel.getStakeholderBankInfoId());

		            //expected to have only one record having OF settlement reference
		            sbiModel = loadStakeholderBankInfoModel(sbiModel);
		            if(sbiModel == null || sbiModel.getBankId() == null) {

		                return null;
		            }

		            return sbiModel;
		        }

		        return null;
		    }

		
		public List<StakeholderBankInfoModel> getStakeholderBankInfoForInclCharges(Long productId) throws FrameworkCheckedException {
			List<StakeholderBankInfoModel> list = null;
			
			ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
			exampleHolder.setMatchMode(MatchMode.EXACT);
			exampleHolder.setEnableLike(Boolean.FALSE);
			
			StakeholderBankInfoModel bankInfoModel = new StakeholderBankInfoModel();
			bankInfoModel.setProductId(productId);
//			bankInfoModel.setActive(Boolean.TRUE); reference email from Shahbaz siddiqui on 26-Jan-2017
			
			CustomList<StakeholderBankInfoModel> customList = stakeholderBankInfoDAO.findByExample(bankInfoModel, null, null, exampleHolder);
			
			if(customList != null && customList.getResultsetList().size() > 0) {
				list = customList.getResultsetList();
			}
			
			return list;
		}

	@Override
	public List<StakeholderBankInfoModel> getStakeholderBankInfoForProductandAccountType(Long productId, String accountType) throws FrameworkCheckedException {
		List<StakeholderBankInfoModel> list = null;
		StakeholderBankInfoModel bankInfoModel = new StakeholderBankInfoModel();
		bankInfoModel.setProductId(productId);
		bankInfoModel.setAccountType(accountType);

		CustomList<StakeholderBankInfoModel> customList = stakeholderBankInfoDAO.findByExample(bankInfoModel, null, null, null);

		if(customList != null && customList.getResultsetList().size() > 0) {
			list = customList.getResultsetList();
		}

		return list;
	}

	public List<StakeholderBankInfoModel> getStakeholderBankInfoForProduct(Long productId) throws FrameworkCheckedException {
			List<StakeholderBankInfoModel> list = null;
			
			ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
			exampleHolder.setMatchMode(MatchMode.EXACT);
			exampleHolder.setEnableLike(Boolean.FALSE);
			
			StakeholderBankInfoModel bankInfoModel = new StakeholderBankInfoModel();
			bankInfoModel.setProductId(productId);
			
			CustomList<StakeholderBankInfoModel> customList = stakeholderBankInfoDAO.findByExample(bankInfoModel, null, null, exampleHolder);
			
			if(customList != null && customList.getResultsetList().size() > 0) {
				list = customList.getResultsetList();
			}
			
			return list;
		} 	
		
		@Override
		public StakeholderBankInfoModel loadStakeholderBankInfoModel(Long primaryKey)
				throws FrameworkCheckedException {
			return this.stakeholderBankInfoDAO.findByPrimaryKey(primaryKey) ;
		}
		
		 @Override
		  public SearchBaseWrapper getStakehldAcctMappingList(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
			ExampleConfigHolderModel configHolderModel=new ExampleConfigHolderModel();
			configHolderModel.setMatchMode(MatchMode.EXACT);
			  
		    CustomList<StakehldAcctMapListViewModel>
		    list = this.stakehldAcctMapListViewModelDAO.findByExample((StakehldAcctMapListViewModel)searchBaseWrapper.getBasePersistableModel(),searchBaseWrapper.getPagingHelperModel(),searchBaseWrapper.getSortingOrderMap(),configHolderModel);
		    searchBaseWrapper.setCustomList(list);
		    return searchBaseWrapper;
		  }
			
	  public List<StakeholderBankInfoModel> getStakeholderBankInfoModelList() throws FrameworkCheckedException {
			
		  return stakeholderBankInfoDAO.getStakeholderBankInfoModelList();
	  }	  
	

	  public StakeholderBankInfoModel loadDistributorStakeholderBankInfoModel(AppUserModel appUserModel) throws FrameworkCheckedException {
			
			return stakeholderBankInfoDAO.loadDistributorStakeholderBankInfoModel(appUserModel);
		}
	  
	  public StakeholderBankInfoModel loadDistributorStakeholderBankInfoModel(DistributorModel distributorModel) throws FrameworkCheckedException {
			
		  return stakeholderBankInfoDAO.loadDistributorStakeholderBankInfoModel(distributorModel);
	  }
	  
	  public StakeholderBankInfoModel getStakeholderAccountBankInfoModel(Long accTypeId) throws FrameworkCheckedException {

		  return stakeholderBankInfoDAO.getStakeholderAccountBankInfoModel(accTypeId);
	  }
	  
	  
	  @Override
		public List<Object[]> loadOfSettlementAccounts(Long accountType)
				throws FrameworkCheckedException {
			return stakeholderBankInfoDAO.loadOfSettlementAccounts(accountType);
		}
	
  public void setStakeholderBankInfoDAO(StakeholderBankInfoDAO
                                        stakeholderBankInfoDAO)
  {
    this.stakeholderBankInfoDAO = stakeholderBankInfoDAO;
  }
  
  /**
   * @param stakehldAcctMapListViewModelDAO the stakehldAcctMapListViewModelDAO to set
   */
  public void setStakehldAcctMapListViewModelDAO(
  		StakehldAcctMapListViewModelDAO stakehldAcctMapListViewModelDAO) {
  	this.stakehldAcctMapListViewModelDAO = stakehldAcctMapListViewModelDAO;
  }

public OLAServiceImpl getOlaServiceImpl() {
	return olaServiceImpl;
}

public void setOlaServiceImpl(OLAServiceImpl olaServiceImpl) {
	this.olaServiceImpl = olaServiceImpl;
}

}
