package com.inov8.microbank.server.service.dailyjob;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.util.StopWatch;

import com.inov8.edi.common.util.DateUtil;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.integration.common.model.AccountModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.ola.server.service.account.AccountManager;
import com.inov8.ola.server.service.ledger.LedgerManager;
import com.inov8.ola.util.TransactionTypeConstants;

/** 
 * Created By    : Abu Turab <br>
 * Creation Date : Aug 05, 2014 11:13 AM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class AppUserAccountCheckScheduler {

	private static final Logger LOGGER = Logger.getLogger( AppUserAccountCheckScheduler.class );
	private AppUserManager	appUserManager;
	private LedgerManager	ledgerManager;
	private AccountManager	accountManager;
	//no argument default constructor
	public AppUserAccountCheckScheduler(){
	}
	
	public void init()
	    {
			StopWatch stopWatch = new StopWatch();
			stopWatch.start("AppUserAccountCheck Scheduler Process");
			
	        LOGGER.info("*********** Executing AppUserAccountCheckScheduler ***********");
	        Long passwordChangeSize = 0L;
	        Long accountInactiveSize = 0L;
	        int cnicExpirySize = 0;
	   //     int dormantMarkedSize = 0;
	        try
	        {
	            List<AppUserModel> toBePasswordChangeUsers = appUserManager.getAllUsersForPasswordChangeRequired();
	            passwordChangeSize = appUserManager.markPasswordChangeRequired(toBePasswordChangeUsers);
	        }
	        catch( Exception e ) 
	        {
	        	LOGGER.error( e.getMessage(), e );
	        }
	        try
	        {
	            List<AppUserModel> toBeInactiveUsers = appUserManager.getAllUsersForAccountInactive();
	            accountInactiveSize = appUserManager.markAppUserAccountInactive(toBeInactiveUsers);
	        }
	        catch( Exception e ) 
		    {
		        	LOGGER.error( e.getMessage(), e );
		    }
	        try
	        {
	            List<AppUserModel> cnicExpiryUsers = appUserManager.getCNICExpiryAlertUsers();
	            cnicExpirySize =  cnicExpiryUsers.size();
	            if(cnicExpiryUsers != null && CollectionUtils.isNotEmpty(cnicExpiryUsers)){
            		appUserManager.markCnicExpiryReminderSent(cnicExpiryUsers);
	            }
	        }
	        catch( Exception e ) 
		    {
		        	LOGGER.error( e.getMessage(), e );
		    }
	        
/*	        try
	        {
	        	
	        	Calendar cal = GregorianCalendar.getInstance();
	    		Date currentDate = new Date();
	    		List<AccountModel> dormantAccountModelList = new ArrayList<AccountModel>();
	    		List<AccountModel> accountModelList = new ArrayList<AccountModel>();
	    		List<String> cnicList = appUserManager.getAppUserCNICsToMarkDormant();
	    		
	    		if(cnicList.size() <= 1000){
	    			accountModelList = accountManager.getAccountModelListByCNICs(cnicList);	
	    		}else{//handle oracle limit of 1000 IN CLAUSE
	    			List<String>[] chunks = this.chunks(cnicList, 1000);
	    			for (List<String> inClauseByThousands : chunks) {
	    				accountModelList.addAll(accountManager.getAccountModelListByCNICs(inClauseByThousands));
	    				
	    			}
	    		}
	    		
	        	for(AccountModel accountModel : accountModelList){
	        		cal.setTime(currentDate);
	        		cal.add(Calendar.MONTH, -48);
	        		try{
	        			Double monthlyDebitConsumed = ledgerManager.getConsumedBalanceByDateRange(accountModel.getAccountId(), TransactionTypeConstants.DEBIT, cal.getTime(), currentDate);
	        			monthlyDebitConsumed = (monthlyDebitConsumed < 0 ? 0.0 : monthlyDebitConsumed);
	        			Double monthlyCreditConsumed = ledgerManager.getConsumedBalanceByDateRange(accountModel.getAccountId(), TransactionTypeConstants.CREDIT, cal.getTime(), currentDate);
	        			monthlyCreditConsumed = (monthlyCreditConsumed < 0 ? 0.0 : monthlyCreditConsumed);
	        			if( (monthlyDebitConsumed != null && monthlyDebitConsumed > 0.0) || (monthlyCreditConsumed != null && monthlyCreditConsumed > 0.0) ){
	        				continue;
	        			}else{
	        				if(accountModel.getCreatedOn().before(cal.getTime())){
	        					dormantAccountModelList.add(accountModel);
	        				}
	        			}
	        		}catch(Exception ex){
	        			ex.printStackTrace();
	        			throw new FrameworkCheckedException(ex.getLocalizedMessage());
	        		}
	        	}
	        	if(dormantAccountModelList.size() > 0){
	        		List<AppUserModel> dormantMarkedList = appUserManager.markAppUserDormant(dormantAccountModelList);
	        		dormantMarkedSize = dormantMarkedList.size();
	        	}
	        }
	        catch(Exception e)
	        {
	        	LOGGER.error( e.getMessage(), e );
	        }*/
	    	
	        LOGGER.info("Total " + passwordChangeSize + " accounts were marked as password change required");
	        LOGGER.info("Total " + accountInactiveSize + " accounts were marked as inactive, for not being used over 30 days");
	        LOGGER.info("Total " + cnicExpirySize + " accounts were selected to send SMS for CNIC expiry as ALERT");
	       // LOGGER.info("Total " + dormantMarkedSize + " accounts were marked as Dormant");
	        LOGGER.info("*********** Finished Executing AppUserAccountCheckScheduler ***********");
	        
	        stopWatch.stop();
	        LOGGER.info(stopWatch.prettyPrint());
	    }
	
	private List[] chunks(final List<String> pList, final int pSize)  
    {  
        if(pList == null || pList.size() == 0 || pSize == 0) return new List[] {};  
        if(pSize < 0) return new List[] { pList };  
   
        // Calculate the number of batches  
        int numBatches = (pList.size() / pSize) + 1;  
   
        // Create a new array of Lists to hold the return value  
        List[] batches = new List[numBatches];  
   
        for(int index = 0; index < numBatches; index++)  
        {  
            int count = index + 1;  
            int fromIndex = Math.max(((count - 1) * pSize), 0);  
            int toIndex = Math.min((count * pSize), pList.size());  
            batches[index] = pList.subList(fromIndex, toIndex);  
        }  
   
        return batches;  
    }

	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}


	public void setLedgerManager(LedgerManager ledgerManager) {
		this.ledgerManager = ledgerManager;
	}


	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}

}
