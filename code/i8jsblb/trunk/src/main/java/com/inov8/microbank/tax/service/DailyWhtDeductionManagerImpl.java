package com.inov8.microbank.tax.service;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.server.service.transactionmodule.TransactionCodeGenerator;
import com.inov8.microbank.server.service.transactionmodule.TransactionCodeGeneratorImpl;
import com.inov8.microbank.server.service.transactionmodule.TransactionModuleManager;
import com.inov8.microbank.tax.dao.*;
import com.inov8.microbank.tax.model.*;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.TransactionManager;

public class DailyWhtDeductionManagerImpl implements DailyWhtDeductionManager {
	private DailyWhtDeductionDAO dailyWhtDeductionDAO;
	private WHTTrxViewModelDAO whtTrxViewModelDAO;
	private DateWiseUserWHTAmountViewDAO dateWiseUserWHTAmountViewDAO;
	private TransactionModuleManager transactionManager;
	private WHTDeductionSchedularStatusDAO whtDeductionSchedularStatusDAO;


	public void setTransactionManager(TransactionModuleManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	protected final Log logger = LogFactory.getLog(getClass());


	public BaseWrapper saveDailyWhtDeduction(BaseWrapper baseWrapper) throws Exception{
		DailyWhtDeductionModel model = (DailyWhtDeductionModel)baseWrapper.getBasePersistableModel();

		model = dailyWhtDeductionDAO.saveOrUpdate(model);
		baseWrapper.setBasePersistableModel(model);
		return baseWrapper;
	}

	public BaseWrapper saveDailyWhtDeductionRequiresNewTransaction(BaseWrapper baseWrapper) throws Exception{
		return saveDailyWhtDeduction(baseWrapper);
	}

	public List<DailyWhtDeductionModel> loadUnsettledWhtDeductionList(Date toDate) throws Exception{
		return dailyWhtDeductionDAO.loadUnsettledWhtDeductionList(toDate);
	}
	public CustomList<WHTTrxViewModel> loadWHTTrx(SearchBaseWrapper wrapper)throws Exception
	{
		WHTTrxViewModel whtTrxViewModel= (WHTTrxViewModel) wrapper.getBasePersistableModel();
		CustomList<WHTTrxViewModel> list = whtTrxViewModelDAO.findByExample(whtTrxViewModel,wrapper.getPagingHelperModel(),
				wrapper.getSortingOrderMap(), wrapper.getDateRangeHolderModel());
		return list;
	}

	public List<DateWiseUserWHTAmountViewModel> loadWithholdingUsersList(WHTConfigModel cashWithdrawalWHTConfigModel ,WHTConfigModel transferWHTConfigModel) throws Exception{
		List<DateWiseUserWHTAmountViewModel> resultList = dateWiseUserWHTAmountViewDAO.loadWithholdingUsersList(cashWithdrawalWHTConfigModel , transferWHTConfigModel);
		return resultList;
	}
	
	public void updateTransactionCode()
	{
		DailyWhtDeductionModel dailyWhtDeductionModel = new DailyWhtDeductionModel();
		dailyWhtDeductionModel.setStatus(0);
		//SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		CustomList<DailyWhtDeductionModel> dailyWhtDeductionModelsList = dailyWhtDeductionDAO.findByExample(dailyWhtDeductionModel,null);
	    if(dailyWhtDeductionModelsList.getResultsetList().size() > 0 && null != dailyWhtDeductionModelsList){
	    	for(DailyWhtDeductionModel WhtDeductionModel : dailyWhtDeductionModelsList.getResultsetList()){
	    		BaseWrapper baseWrapper = new BaseWrapperImpl();
	    		TransactionCodeModel transactionCodeModel = new TransactionCodeModel();
	    		transactionCodeModel.setTransactionCodeId(WhtDeductionModel.getTransactionCodeId());
	    		baseWrapper.setBasePersistableModel(transactionCodeModel);
	    		try {
	    			baseWrapper = transactionManager.loadTransactionCode(baseWrapper);
				} catch (FrameworkCheckedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		String transactionCode = "";
	    	    TransactionCodeGenerator transactionCodeGenerator = new TransactionCodeGeneratorImpl();
	    	    transactionCode = transactionCodeGenerator.getAllPayTransactionCode();
	    	   // WhtDeductionModel.setTransactionCodeId(Long.valueOf(transactionCode));
	    	    TransactionCodeModel tCodeModel=new TransactionCodeModel();
	    	    tCodeModel = (TransactionCodeModel) baseWrapper.getBasePersistableModel();
	    	    tCodeModel.setCode(transactionCode);
	    	    BaseWrapper bWrapper = new BaseWrapperImpl();
	    	    bWrapper.setBasePersistableModel(tCodeModel);
	    	    try {
					transactionManager.updateTransactionCode(bWrapper);
				} catch (FrameworkCheckedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
	    }
	}
	
	
	public void saveDailyWhtDeduction(List<DailyWhtDeductionModel> dailyWhtDeductionModelList) throws Exception{

		if(dailyWhtDeductionModelList !=null && dailyWhtDeductionModelList.size() > 0){
			dailyWhtDeductionDAO.saveOrUpdateCollection(dailyWhtDeductionModelList);
			
			logger.info("End of DailyWhtDeductionManagerImpl.saveDailyWhtDeduction()");
			
/*			WHTDeductionSchedularStatusModel whtSchdularStatusmodel = null;
			whtSchdularStatusmodel = whtDeductionSchedularStatusDAO.findByPrimaryKey(id);
			if(whtSchdularStatusmodel.getCompletion_status() == false){
				whtSchdularStatusmodel.setCompletion_status(true);
				whtSchdularStatusmodel.setUpdated_on(new Date());
				
				whtDeductionSchedularStatusDAO.saveOrUpdate(whtSchdularStatusmodel);
			}*/
			
			whtDeductionSchedularStatusDAO.updateWhtSchedulerStatus();
			
			
/*			Date trackDate = new Date();
			
			for(DailyWhtDeductionModel model : dailyWhtDeductionModelList){
				
				if(trackDate.equals(PortalDateUtils.getDateWithoutTime(model.getTransaction_date()))){
					continue;
				}
				
				WHTDeductionSchedularStatusModel whtDeductionSchdularStatusmodel = new WHTDeductionSchedularStatusModel();
				whtDeductionSchdularStatusmodel.setTransaction_date(PortalDateUtils.getDateWithoutTime(model.getTransaction_date()));
				CustomList<WHTDeductionSchedularStatusModel> deductionSchdularStatuslist = whtDeductionSchedularStatusDAO.findByExample(whtDeductionSchdularStatusmodel, null);
				if(deductionSchdularStatuslist !=null && deductionSchdularStatuslist.getResultsetList().size() >0){
					whtDeductionSchdularStatusmodel = deductionSchdularStatuslist.getResultsetList().get(0);
				}
				if(null !=whtDeductionSchdularStatusmodel && whtDeductionSchdularStatusmodel.getCompletion_status() == false ){
					
					whtDeductionSchdularStatusmodel.setCompletion_status(true);
					whtDeductionSchdularStatusmodel.setTransaction_date(model.getTransaction_date());
					whtDeductionSchdularStatusmodel.setUpdated_on(new Date());
					whtDeductionSchedularStatusDAO.saveOrUpdate(whtDeductionSchdularStatusmodel);
					trackDate = PortalDateUtils.getDateWithoutTime(model.getTransaction_date());
				}
				
			}*/

		}
		
		
}


public void saveDailyWhtDeductionRequiresNewTransaction(List<DailyWhtDeductionModel> dailyWhtDeductionModelList) throws Exception{
	 saveDailyWhtDeduction(dailyWhtDeductionModelList);
}


	public void setDailyWhtDeductionDAO(DailyWhtDeductionDAO dailyWhtDeductionDAO) {
		this.dailyWhtDeductionDAO = dailyWhtDeductionDAO;
	}
	public void setWhtTrxViewModelDAO(WHTTrxViewModelDAO whtTrxViewModelDAO)
	{
		this.whtTrxViewModelDAO = whtTrxViewModelDAO;
	}

	public void setDateWiseUserWHTAmountViewDAO(DateWiseUserWHTAmountViewDAO dateWiseUserWHTAmountViewDAO) {
		this.dateWiseUserWHTAmountViewDAO = dateWiseUserWHTAmountViewDAO;
	}

	public void setWhtDeductionSchedularStatusDAO(
			WHTDeductionSchedularStatusDAO whtDeductionSchedularStatusDAO) {
		this.whtDeductionSchedularStatusDAO = whtDeductionSchedularStatusDAO;
	}




}
