package com.inov8.microbank.server.service.operatormodule;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.MatchMode;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.OperatorBankInfoModel;
import com.inov8.microbank.common.model.operatorbankmodule.OperatorBankInfoListViewModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.dao.operatormodule.OperatorBankInfoDAO;
import com.inov8.microbank.server.dao.operatormodule.OperatorBankInfoListViewDAO;

public class OperatorBankInfoManagerImpl implements OperatorBankInfoManager{
	private OperatorBankInfoDAO operatorBankInfoDAO;
	  private OperatorBankInfoListViewDAO operatorBankInfoListViewDAO ;

	  public void setOperatorBankInfoListViewDAO(
			OperatorBankInfoListViewDAO operatorBankInfoListViewDAO) {
		this.operatorBankInfoListViewDAO = operatorBankInfoListViewDAO;
	}

	public BaseWrapper loadOperatorBankInfo(BaseWrapper baseWrapper)
	  {
	    List operatorBankInfoModelList = this.operatorBankInfoDAO.findByExample((OperatorBankInfoModel)baseWrapper.
	        getBasePersistableModel()).getResultsetList() ;
	    if( operatorBankInfoModelList.size() > 0 )
	      baseWrapper.setBasePersistableModel((OperatorBankInfoModel)operatorBankInfoModelList.get(0));
	    else
	      baseWrapper.setBasePersistableModel(null) ;

	    return baseWrapper;
	  }

	  public void setOperatorBankInfoDAO(OperatorBankInfoDAO operatorBankInfoDAO)
	  {
	    this.operatorBankInfoDAO = operatorBankInfoDAO;
	  }

	public BaseWrapper createOperatorBankInfo(BaseWrapper baseWrapper) throws FrameworkCheckedException {

		OperatorBankInfoModel operatorBankInfoModel = new OperatorBankInfoModel();
		OperatorBankInfoModel theNewOperatorBankInfoModel = new OperatorBankInfoModel();
		OperatorBankInfoModel operatorBankInfoModelUnique = new OperatorBankInfoModel();
		OperatorBankInfoListViewModel operatorBankInfoListViewModel = (OperatorBankInfoListViewModel) baseWrapper.
		        getBasePersistableModel();
		
		operatorBankInfoModelUnique.setOperatorId(operatorBankInfoListViewModel.getOperatorId());
		operatorBankInfoModelUnique.setPaymentModeId(operatorBankInfoListViewModel.getPaymentModeId());
		operatorBankInfoModelUnique.setBankId(operatorBankInfoListViewModel.getBankId());
		
		
		operatorBankInfoModel.setName(operatorBankInfoListViewModel.getName());
		 ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
			exampleHolder.setMatchMode(MatchMode.EXACT);
		    int recordCount = operatorBankInfoDAO.countByExample(operatorBankInfoModel, exampleHolder);
		    int recordCountUnique = operatorBankInfoDAO.countByExample(operatorBankInfoModelUnique, exampleHolder);
		    //***Check if name already exists
		     if (recordCount == 0 && recordCountUnique==0)
		     {
		    
		    	 
		    	 	
		    	 theNewOperatorBankInfoModel.setMerchantCategory(operatorBankInfoListViewModel.getMerchantCategory());
		    	 theNewOperatorBankInfoModel.setReceivingAccountNo(operatorBankInfoListViewModel.getReceivingAccountNo());
		    	 theNewOperatorBankInfoModel.setPayingAccountNo(operatorBankInfoListViewModel.getPayingAccountNo());
		    	 
		    	 theNewOperatorBankInfoModel.setBankId(operatorBankInfoListViewModel.getBankId());
		    	 theNewOperatorBankInfoModel.setName(operatorBankInfoListViewModel.getName());
		    	 theNewOperatorBankInfoModel.setPaymentModeId(operatorBankInfoListViewModel.getPaymentModeId());
		    	 theNewOperatorBankInfoModel.setOperatorId(operatorBankInfoListViewModel.getOperatorId());
		    	 //theNewOperatorBankInfoModel.setActive(operatorBankInfoListViewModel.getActive()==null?false:operatorBankInfoListViewModel.getActive());
			   	theNewOperatorBankInfoModel.setCreatedBy(UserUtils.getCurrentUser()
						.getAppUserId());
			   	theNewOperatorBankInfoModel.setCreatedOn(new Date());
			   	theNewOperatorBankInfoModel.setUpdatedBy(UserUtils.getCurrentUser()
						.getAppUserId());
			   	theNewOperatorBankInfoModel.setUpdatedOn(new Date());
				
				baseWrapper.setBasePersistableModel(theNewOperatorBankInfoModel);
				theNewOperatorBankInfoModel = this.operatorBankInfoDAO.saveOrUpdate( (OperatorBankInfoModel) baseWrapper.getBasePersistableModel());
		       baseWrapper.setBasePersistableModel(theNewOperatorBankInfoModel);
		       
		     }
		     else
		     {
		       //set baseWrapper to null if record exists
		 		if (recordCount!=0)	
		    	 {
		 			throw new FrameworkCheckedException("NameUniqueException");
		 		 }
		 		else if (recordCountUnique!=0)
		 		{
		 			throw new FrameworkCheckedException("OperatorPaymentBankUniqueException");
		 			
		 		}
		 		
		     }
		     return baseWrapper;


	}

	public BaseWrapper updateOperatorBankInfo(BaseWrapper baseWrapper) throws FrameworkCheckedException {

		OperatorBankInfoModel operatorBankInfoModel = new OperatorBankInfoModel();
		OperatorBankInfoListViewModel operatorBankInfoListViewModel = (OperatorBankInfoListViewModel) baseWrapper.
		        getBasePersistableModel();
		
		operatorBankInfoModel.setName(operatorBankInfoListViewModel.getName());
		    int recordCount = operatorBankInfoDAO.countByExample(operatorBankInfoModel);
		    //***Check if name already exists
		     if (recordCount != 0 || operatorBankInfoListViewModel.getOperatorBankInfoId() != null)
		     {
		     	 operatorBankInfoModel=this.operatorBankInfoDAO.findByPrimaryKey(operatorBankInfoListViewModel.getOperatorBankInfoId());
				 operatorBankInfoModel.setMerchantCategory(operatorBankInfoListViewModel.getMerchantCategory());
		    	 operatorBankInfoModel.setReceivingAccountNo(operatorBankInfoListViewModel.getReceivingAccountNo());
		    	 operatorBankInfoModel.setPayingAccountNo(operatorBankInfoListViewModel.getPayingAccountNo());
		    	 operatorBankInfoModel.setBankId(operatorBankInfoListViewModel.getBankId());
		    	 operatorBankInfoModel.setName(operatorBankInfoListViewModel.getName());
		    	 operatorBankInfoModel.setPaymentModeId(operatorBankInfoListViewModel.getPaymentModeId());
		    	 operatorBankInfoModel.setOperatorId(operatorBankInfoListViewModel.getOperatorId());
		    	 //operatorBankInfoModel.setActive(operatorBankInfoListViewModel.getActive()==null?false:operatorBankInfoListViewModel.getActive());
		    	 operatorBankInfoModel.setUpdatedOn(new Date());
		    	 operatorBankInfoModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		   		
			
			baseWrapper.setBasePersistableModel(operatorBankInfoModel);
		    	 operatorBankInfoModel = this.operatorBankInfoDAO.saveOrUpdate( (OperatorBankInfoModel) baseWrapper.getBasePersistableModel());
		       baseWrapper.setBasePersistableModel(operatorBankInfoModel);
		       
		     
		       return baseWrapper;

		     }
		     throw new FrameworkCheckedException("NameUniqueException");
	}


	public SearchBaseWrapper searchOperatorBankInfo(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		CustomList<OperatorBankInfoListViewModel>
	    list = this.operatorBankInfoListViewDAO.findByExample( (OperatorBankInfoListViewModel)
	    searchBaseWrapper.getBasePersistableModel(),
	    searchBaseWrapper.getPagingHelperModel(),
	    searchBaseWrapper.getSortingOrderMap());
	searchBaseWrapper.setCustomList(list);
	return searchBaseWrapper;
	}




	public SearchBaseWrapper loadOperatorBankInfo(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		
		
		OperatorBankInfoListViewModel operatorBankInfoListViewModel = this.operatorBankInfoListViewDAO
		.findByPrimaryKey(searchBaseWrapper.getBasePersistableModel()
				.getPrimaryKey());
	searchBaseWrapper.setBasePersistableModel(operatorBankInfoListViewModel);
	return searchBaseWrapper;

	}

}
