/**
 * 
 */
package com.inov8.microbank.server.service.dailyjob;

import static com.inov8.microbank.common.util.CommandFieldConstants.KEY_BULK_DISBURSMENTS_ID;
import static com.inov8.microbank.common.util.CommandFieldConstants.KEY_DEVICE_TYPE_ID;
import static com.inov8.microbank.common.util.CommandFieldConstants.KEY_PAYMENT_TYPE;
import static com.inov8.microbank.common.util.CommandFieldConstants.KEY_PROD_ID;
import static com.inov8.microbank.common.util.CommandFieldConstants.KEY_TX_AMOUNT;
import static com.inov8.microbank.common.util.CommandFieldConstants.KEY_WALKIN_RECEIVER_CNIC;
import static com.inov8.microbank.common.util.CommandFieldConstants.KEY_WALKIN_RECEIVER_MSISDN;
import static com.inov8.microbank.common.util.CommandFieldConstants.WALK_IN_LIMIT_APPLIED;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.BulkDisbursementsModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.server.dao.disbursementmodule.BulkDisbursementDAO;
import com.inov8.microbank.server.service.commandmodule.CommandManager;

/**
 * @author Kashif
 *
 */

public class BulkDisbursementIDPSettlementThread implements Runnable {

	private static Logger logger = Logger.getLogger(BulkDisbursementIDPSettlementThread.class);
	private Integer threadNumber = null;
	private CommandManager commandManager = null;
	private List<BulkDisbursementsModel> bulkDisbursementsModelList = null;
	private BulkDisbursementDAO bulkDisbursementDao = null;
	private Map<Integer, String> paymentTypeMap;
	private CountDownLatch startSignal;
	private CountDownLatch doneSignal;
	
	public BulkDisbursementIDPSettlementThread(Integer threadNumber, CommandManager commandManager, List<BulkDisbursementsModel> bulkDisbursementsModelList, BulkDisbursementDAO bulkDisbursementDao, Map<Integer, String> paymentTypeMap,CountDownLatch startSignal,CountDownLatch doneSignal) {
		
		this.threadNumber = threadNumber;
		this.commandManager = commandManager;
		this.bulkDisbursementsModelList = bulkDisbursementsModelList;
		this.bulkDisbursementDao = bulkDisbursementDao;
		this.paymentTypeMap = paymentTypeMap;
		this.startSignal = startSignal;
	    this.doneSignal = doneSignal;
	}
 
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try
		{
			startSignal.await();
			logger.debug("\n\nThread Started # " + threadNumber + " Records to Perform : "+bulkDisbursementsModelList.size());
			
			for(BulkDisbursementsModel bulkDisbursementsModel : bulkDisbursementsModelList) {
				
				try {
					
					logger.debug("\n\nThread ID # " + threadNumber + " Processing Bulk Payment ID # "+bulkDisbursementsModel.getBulkDisbursementsId());

					if(bulkDisbursementsModel.getAmount() != null && bulkDisbursementsModel.getAmount() > 0.0D) {
					
						BaseWrapper baseWrapper = getBaseWrapper(bulkDisbursementsModel);
						
						String response = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CMD_BULK_PAYMENT);
					
					}
					
				} catch (Exception e) {
					
					logger.error("[BulkDisbursementIDPSettlementThread] Exception occurred:",e);
				
				}
			}
			doneSignal.countDown();
		}
		catch (Exception e)
		{
			logger.error(e);
		}
		
//		bulkDisbursementDao.saveOrUpdateList(bulkDisbursementsModelList);
	}

	
	/**
	 * @param bulkDisbursementsModel
	 * @return
	 * @throws FrameworkCheckedException
	 */
	private BaseWrapper getBaseWrapper(BulkDisbursementsModel bulkDisbursementsModel) throws FrameworkCheckedException { 
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.putObject(KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.USSD);
		baseWrapper.putObject(KEY_PROD_ID, bulkDisbursementsModel.getProductId());
		baseWrapper.putObject(KEY_WALKIN_RECEIVER_MSISDN, bulkDisbursementsModel.getMobileNo());
		baseWrapper.putObject(KEY_TX_AMOUNT, bulkDisbursementsModel.getAmount());
		baseWrapper.putObject(KEY_WALKIN_RECEIVER_CNIC, bulkDisbursementsModel.getAppUserIdAppUserModel().getNic());
		baseWrapper.putObject(WALK_IN_LIMIT_APPLIED, bulkDisbursementsModel.getLimitApplicable());
		baseWrapper.putObject(KEY_PAYMENT_TYPE, paymentTypeMap.get(bulkDisbursementsModel.getServiceId()));
		baseWrapper.putObject(KEY_BULK_DISBURSMENTS_ID, bulkDisbursementsModel.getBulkDisbursementsId());
		
		AppUserModel appUserModel = new AppUserModel();
		appUserModel.setAppUserId(PortalConstants.SCHEDULER_APP_USER_ID);
		baseWrapper.setBasePersistableModel(appUserModel);
		
		return baseWrapper;
	}
}