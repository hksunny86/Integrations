package com.inov8.microbank.tax.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.tax.common.TaxConstantsInterface;
import com.inov8.microbank.tax.model.DailyWhtDeductionModel;
import com.inov8.microbank.tax.model.DateWiseUserWHTAmountViewModel;
import com.inov8.microbank.tax.model.WHTConfigModel;
import com.inov8.microbank.tax.model.WHTDeductionSchedularStatusModel;
import com.inov8.microbank.tax.service.DailyWhtDeductionManager;
import com.inov8.microbank.tax.service.TaxManager;

/**
 * Created by ADMIN on 7/12/2016.
 */
public class WHTDeductionScheduler 
{

    private TaxManager taxManager;
    private DailyWhtDeductionManager dailyWhtDeductionManager;
    private List<WorkFlowWrapper> dailyWHTEntriesList = null;
    protected final Log logger = LogFactory.getLog(getClass());

    private void process() throws FrameworkCheckedException{

        Date currentDateTime = GregorianCalendar.getInstance(TimeZone.getDefault()).getTime();
        logger.info("*********************************** Starting WHT Deduction Scheduler *********************************");
        try{
            execute();
        } catch (Exception e) {
            logger.error("\n\n*** WHT Deduction Scheduler FAILED DUE TO SYSTEM ERROR ***" + e.getLocalizedMessage());
            e.printStackTrace();
        }finally {
            ThreadLocalAppUser.remove();
        }

        logger.info("*********************************** Exiting WHT Deduction Scheduler *********************************");

    }

    private void execute() throws Exception{
        Date startTimestamp = new Date();
        Date toTimestamp = new Date();
        Date transactionDate = new Date();
        Boolean insert = false;
        int count=5;
        
        WHTConfigModel cashWithdrawalWHTConfigModel = taxManager.loadWHTConfigModel(TaxConstantsInterface.WHT_CONFIG_WITHDRAWAL_ID);
        WHTConfigModel transferWHTConfigModel = taxManager.loadWHTConfigModel(TaxConstantsInterface.WHT_CONFIG_TRANSFER_ID);
        
        Map<Long, WHTConfigModel> configModelMap = new HashMap<>(2);
        configModelMap.put(TaxConstantsInterface.WHT_CONFIG_WITHDRAWAL_ID, cashWithdrawalWHTConfigModel);
        configModelMap.put(TaxConstantsInterface.WHT_CONFIG_TRANSFER_ID, transferWHTConfigModel);
        
        List <WHTDeductionSchedularStatusModel> listWHTDeductionSchedularStatusModels = taxManager.findWHTDeductionSchedularStatusEntries();
        
        if(null!=listWHTDeductionSchedularStatusModels  && listWHTDeductionSchedularStatusModels.size() >=count){
        for(int i =1; i<=count; i++){
        	
        	WHTDeductionSchedularStatusModel whtDeductionSchdularStatusmodel = new WHTDeductionSchedularStatusModel();
			whtDeductionSchdularStatusmodel.setStart_date(PortalDateUtils.getDateWithoutTime(PortalDateUtils.subtractDays(startTimestamp, i)));
			List<WHTDeductionSchedularStatusModel> deductionSchdularStatuslist = taxManager.findWHTDeductionMissedEntries(whtDeductionSchdularStatusmodel);
			if(null!=deductionSchdularStatuslist){
				
			}else{
				insert = true;
				startTimestamp = new Date();
			}
	
        	if(insert == true){
                WHTDeductionSchedularStatusModel wHTDeductionSchedularStatusModel = new WHTDeductionSchedularStatusModel();
                wHTDeductionSchedularStatusModel.setStart_date(PortalDateUtils.getDateWithoutTime(PortalDateUtils.subtractDays(startTimestamp, i)));
                wHTDeductionSchedularStatusModel.setTransaction_date(PortalDateUtils.getDateWithoutTime(PortalDateUtils.subtractDays(transactionDate, i+1)));
                wHTDeductionSchedularStatusModel.setInitialization_status(true);
                wHTDeductionSchedularStatusModel.setCompletion_status(false);
                wHTDeductionSchedularStatusModel.setCreated_on(new Date());
                wHTDeductionSchedularStatusModel.setUpdated_on(new Date());
                
                taxManager.saveOrUpdateWHTDeductionSchedularStatus(wHTDeductionSchedularStatusModel);
                
                insert = false;

                 List<DateWiseUserWHTAmountViewModel> previousDayUsersList = dailyWhtDeductionManager.loadWithholdingUsersList(cashWithdrawalWHTConfigModel,transferWHTConfigModel);
                 
                 logger.info(previousDayUsersList.size() + " Records Found for Withdrawal Withholding deduction on " + transactionDate + " or before " + transactionDate);
                 try{
                 dailyWHTEntriesList = taxManager.prepareDailyWHTEntries(previousDayUsersList, configModelMap);
                 if(dailyWHTEntriesList !=null){
                	 taxManager.saveDailyWHTDeductionModels(dailyWHTEntriesList);
                 }
                 }catch(Exception e){
                	 logger.error("WHTDeductionScheduler. Exception on saveDailyWHTEntries(). Data of : "+ transactionDate + " or before " + transactionDate + " Message : " + e.getMessage());
                 }
                 startTimestamp = new Date();
                 transactionDate = new Date();
        	}else{
        		insert = false;
        		startTimestamp = new Date();
        		transactionDate = new Date();
        	}
        }
     }
        
        
        startTimestamp = new Date();
        transactionDate = new Date();
        
        	WHTDeductionSchedularStatusModel wHTDeductionSchedularStatusModel = new WHTDeductionSchedularStatusModel();
            wHTDeductionSchedularStatusModel.setStart_date(PortalDateUtils.getDateWithoutTime(startTimestamp));
            wHTDeductionSchedularStatusModel.setTransaction_date(PortalDateUtils.getDateWithoutTime(PortalDateUtils.subtractDays(transactionDate, 1)));
            wHTDeductionSchedularStatusModel.setInitialization_status(true);
            wHTDeductionSchedularStatusModel.setCompletion_status(false);
            wHTDeductionSchedularStatusModel.setCreated_on(new Date());
            wHTDeductionSchedularStatusModel.setUpdated_on(new Date());
            
            taxManager.saveOrUpdateWHTDeductionSchedularStatus(wHTDeductionSchedularStatusModel);
        
        
         Date toDate = PortalDateUtils.getDateWithoutTime(toTimestamp);
         dailyWHTEntriesList = null;

        /**
         * Step 1. Load all candidate customers/agents for withholding debit.
         */
        List<DateWiseUserWHTAmountViewModel> previousDayUsersList = dailyWhtDeductionManager.loadWithholdingUsersList(cashWithdrawalWHTConfigModel,transferWHTConfigModel);
        
        
        /**
        * Step 2. Save all candidate wht entries to DailyWhtDeduction table.
        */
        logger.info(previousDayUsersList.size() + " Records Found for Withdrawal Withholding deduction on " + transactionDate + " or before " + transactionDate);
        try{
        	dailyWHTEntriesList = taxManager.prepareDailyWHTEntries(previousDayUsersList, configModelMap);
            if(dailyWHTEntriesList !=null){
           	 taxManager.saveDailyWHTDeductionModels(dailyWHTEntriesList);
            }
        }catch(Exception e){
        	logger.error("WHTDeductionScheduler. Exception on saveDailyWHTEntries(). Data of : "+ transactionDate + " or before " + transactionDate +  " Message : " + e.getMessage());
        }
        

        /**
         * Step 2. load all previous unsettled wht entries from DailyWhtDeduction table.
         */
/*        List<DailyWhtDeductionModel> unsettledWhtList = taxManager.loadUnsettledWithholdingDeductionList(toDate);

        for (DailyWhtDeductionModel model : unsettledWhtList){

            try {
                Long appUserId = model.getAppUserId();
                Double amount = model.getAmount();
                logger.info("Going to debit AppUserId: " + appUserId + ". Sum Amount: " + amount + ". Threshold:" + model.getWhtConfigIdWhtConfigModel().getThresholdLimit());
                taxManager.makeDebitForWHT(model);
            }

            catch (Exception e) {
                logger.error("WHTDeductionScheduler. Exception on debitAccountForWHT(). App User Id : "+ model.getAppUserId() + " Message : " + e.getMessage());
            }
        }*/
    }

    public void setTaxManager(TaxManager taxManager) {
        this.taxManager = taxManager;
    }

    public void setDailyWhtDeductionManager(DailyWhtDeductionManager dailyWhtDeductionManager) {
        this.dailyWhtDeductionManager = dailyWhtDeductionManager;
    }

}
