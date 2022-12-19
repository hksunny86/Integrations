package com.inov8.microbank.server.service.AllpayModule;

import java.util.Date;

import org.springframework.dao.DataAccessException;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.AllpayCommissionRateModel;
import com.inov8.microbank.common.model.CommissionRatesListViewModel;
import com.inov8.microbank.server.dao.allpaymodule.AllpayCommissionRatesDAO;
import com.inov8.microbank.server.dao.allpaymodule.AllpayCommissionRatesListViewDAO;

public class AllpayCommissionRatesManagerImpl implements
		AllpayCommissionRatesManager {
	private AllpayCommissionRatesDAO allpayCommissionRatesDAO;
	private AllpayCommissionRatesListViewDAO allpayCommissionRatesListViewDAO;
	public BaseWrapper createCommissionRates(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		Date nowDate = new Date();
	    AllpayCommissionRateModel newCommissionRateModel = new AllpayCommissionRateModel();
	    AllpayCommissionRateModel commissionRateModel = (AllpayCommissionRateModel) baseWrapper.
	        getBasePersistableModel();

	    newCommissionRateModel.setProductId(commissionRateModel.getProductId());
	    newCommissionRateModel.setAllpayCommissionReasonId(commissionRateModel.
	                                                 getAllpayCommissionReasonId());
	    
	    newCommissionRateModel.setFromDate(commissionRateModel.getFromDate());
	    newCommissionRateModel.setToDate(commissionRateModel.getToDate());

	    //***Check if Record already exists

	    boolean recordCount = allpayCommissionRatesDAO.getDuplicateCommissionRateRecords(commissionRateModel);
	    
	    commissionRateModel.setCreatedOn(nowDate);
	    commissionRateModel.setUpdatedOn(nowDate);
	    
	    if (recordCount)
	    {
	    	throw new FrameworkCheckedException("duplicateRecord");
	    }
	    
	    AllpayCommissionRateModel cRateModel = this.allpayCommissionRatesDAO.saveOrUpdate(
	            commissionRateModel);
	        baseWrapper.setBasePersistableModel(cRateModel);
	        return baseWrapper;
	}

	public SearchBaseWrapper loadCommissionRates(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		
		AllpayCommissionRateModel allpayCommissionRateModel;
		try {
			allpayCommissionRateModel = this.allpayCommissionRatesDAO.
			findByPrimaryKey(searchBaseWrapper.getBasePersistableModel().
			                 getPrimaryKey());
			 searchBaseWrapper.setBasePersistableModel(allpayCommissionRateModel);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   
    return searchBaseWrapper;
	}

	public BaseWrapper loadCommissionRates(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		AllpayCommissionRateModel commissionRateModel = this.allpayCommissionRatesDAO.
        findByPrimaryKey(baseWrapper.getBasePersistableModel().
                         getPrimaryKey());
    baseWrapper.setBasePersistableModel(commissionRateModel);
    return baseWrapper;
	}

	public SearchBaseWrapper searchCommissionRates(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
//		CommissionRatesListViewModel t = 	(CommissionRatesListViewModel)searchBaseWrapper.getBasePersistableModel();
//		t.setProductName("a");
	    CustomList<CommissionRatesListViewModel>
	        list = this.allpayCommissionRatesListViewDAO.findByExample( (
	            CommissionRatesListViewModel)
	        searchBaseWrapper.getBasePersistableModel(),
	        searchBaseWrapper.getPagingHelperModel(),
	        searchBaseWrapper.getSortingOrderMap());
	    searchBaseWrapper.setCustomList(list);
	    return searchBaseWrapper;
	  
	}

	public BaseWrapper updateCommissionRates(BaseWrapper baseWrapper) throws FrameworkCheckedException {

	    AllpayCommissionRateModel commissionRateModel = (AllpayCommissionRateModel) baseWrapper.
	        getBasePersistableModel();
	    AllpayCommissionRateModel newCommissionRateModel = new AllpayCommissionRateModel();
	    newCommissionRateModel.setAllpayCommissionRateId(commissionRateModel.
	                                               getAllpayCommissionRateId());

	    if( allpayCommissionRatesDAO.getDuplicateCommissionRateRecords(commissionRateModel) ){   
	    	baseWrapper.setBasePersistableModel(null);
	    	return baseWrapper;
	    }

	      commissionRateModel = this.allpayCommissionRatesDAO.saveOrUpdate( (
	    		  AllpayCommissionRateModel) baseWrapper.getBasePersistableModel());
	      baseWrapper.setBasePersistableModel(commissionRateModel);
	   
	      return baseWrapper;
	   
	  
	}
	
	public SearchBaseWrapper searchAllPayCommissionRate(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException 
	{
	    CustomList<AllpayCommissionRateModel> list = this.allpayCommissionRatesDAO.findByExample(
				(AllpayCommissionRateModel) searchBaseWrapper.getBasePersistableModel(), searchBaseWrapper
						.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap());
		
	    searchBaseWrapper.setCustomList(list);
	    
	    return searchBaseWrapper;	  
	}

	public AllpayCommissionRatesDAO getAllpayCommissionRatesDAO() {
		return allpayCommissionRatesDAO;
	}

	public void setAllpayCommissionRatesDAO(
			AllpayCommissionRatesDAO allpayCommissionRatesDAO) {
		this.allpayCommissionRatesDAO = allpayCommissionRatesDAO;
	}

	public AllpayCommissionRatesListViewDAO getAllpayCommissionRatesListViewDAO() {
		return allpayCommissionRatesListViewDAO;
	}

	public void setAllpayCommissionRatesListViewDAO(AllpayCommissionRatesListViewDAO allpayCommissionRatesListViewDAO) {
		this.allpayCommissionRatesListViewDAO = allpayCommissionRatesListViewDAO;
	}

	}
