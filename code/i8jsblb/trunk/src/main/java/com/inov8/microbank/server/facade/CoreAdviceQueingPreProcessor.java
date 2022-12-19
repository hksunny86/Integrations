package com.inov8.microbank.server.facade;

import java.util.Date;
import java.util.List;

import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.microbank.common.model.SafRepoCoreModel;
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.dao.safrepo.SafRepoCoreDao;
import com.inov8.microbank.server.service.integration.vo.MiddlewareAdviceVO;

public class CoreAdviceQueingPreProcessor {

	private SafRepoCoreDao safRepoCoreDao;
	private CoreAdviceSender coreAdviceSender;
	private static final Log logger = LogFactory.getLog(CoreAdviceQueingPreProcessor.class);
	
	
	public CoreAdviceQueingPreProcessor(){}
	
	public void loadAndForwardAdviceToQueue(String transactionCode) throws InterruptedException{
		if(StringUtil.isNullOrEmpty(transactionCode)){
			logger.error("[CoreAdviceQueingPreProcessor.loadAndForwardAdviceToQueue] TransactionCode supplied is Null...");
			return;
		}
		
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
		workFlowWrapper.setTransactionCodeModel(new TransactionCodeModel());
		workFlowWrapper.getTransactionCodeModel().setCode(transactionCode);
		this.startProcessing(workFlowWrapper);
	}

	public void startProcessing(WorkFlowWrapper wrapper){
		new CoreQueingThread(wrapper).start();
	}
	
	private MiddlewareAdviceVO pushCoreAdviceQueue(MiddlewareAdviceVO middlewareAdviceVO) throws FrameworkCheckedException {
		try {
			logger.info("[CoreAdviceQueingPreProcessor.pushCoreAdviceQueue] Pushing Core Advice to queue. trx ID:" + middlewareAdviceVO.getMicrobankTransactionCode());
			coreAdviceSender.send(middlewareAdviceVO);
			
		} catch (Exception e) {
			logger.error("Failed to push Core Advice to queue. trx ID:" + middlewareAdviceVO.getMicrobankTransactionCode(), e);
			throw new FrameworkCheckedException("Failed to queue Core Advice");
		}
		
		return middlewareAdviceVO;
	}

	private WorkFlowWrapper pushCoreAdviceQueueForAccOpening(MiddlewareAdviceVO middlewareAdviceVO, WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
		try {
			logger.info("[CoreAdviceQueingPreProcessor.pushCoreAdviceQueue] Pushing Core Advice to queue. trx ID:" + middlewareAdviceVO.getMicrobankTransactionCode());
			coreAdviceSender.sendForAccOpening(middlewareAdviceVO, workFlowWrapper);

		} catch (Exception e) {
			logger.error("Failed to push Core Advice to queue. trx ID:" + middlewareAdviceVO.getMicrobankTransactionCode(), e);
			throw new FrameworkCheckedException("Failed to queue Core Advice");
		}

		return workFlowWrapper;
	}
	class CoreQueingThread extends Thread{

		WorkFlowWrapper workFlowWrapper;
		
		CoreQueingThread(WorkFlowWrapper workFlowWrapper){
			this.workFlowWrapper = workFlowWrapper;
		}
		
		@Override
		public void run() {			
			MiddlewareAdviceVO middlewareAdviceVO = null;
			try {					
				if(workFlowWrapper.getProductId() != null && (workFlowWrapper.getProductId().equals(ProductConstantsInterface.CUST_ACCOUNT_OPENING)
						|| workFlowWrapper.getProductId().equals(ProductConstantsInterface.ACCOUNT_OPENING)
						|| workFlowWrapper.getProductId().equals(ProductConstantsInterface.PORTAL_ACCOUNT_OPENING))){


					SafRepoCoreModel safRepoCoreModel = new SafRepoCoreModel();
					safRepoCoreModel.setProductId(workFlowWrapper.getProductId());
					safRepoCoreModel.setStatus(PortalConstants.SAF_STATUS_INITIATED);
					safRepoCoreModel.setConsumerNo(String.valueOf(((AppUserModel) workFlowWrapper.getObject(CommandFieldConstants.KEY_APP_USER_MODEL)).getMobileNo()));

					logger.info("[CoreAdviceQueingPreProcessor.run] Loading transactions against productId: "+safRepoCoreModel.getProductId());
					CustomList<SafRepoCoreModel> customList = safRepoCoreDao.findByExample(safRepoCoreModel,null,null,PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
					List<SafRepoCoreModel> safRepoCoreModels = customList.getResultsetList();

					if(safRepoCoreModels != null && safRepoCoreModels.size() > 0) {

						for (SafRepoCoreModel safModel : safRepoCoreModels) {
							try{
								middlewareAdviceVO = CoreAdviceUtil.prepareMiddlewareAdviceVO(safModel);

								middlewareAdviceVO.setWorkFlowWrapper(workFlowWrapper);

								pushCoreAdviceQueueForAccOpening(middlewareAdviceVO, workFlowWrapper);

								updateSafRepoCoreStatus(PortalConstants.SAF_STATUS_SUCCESSFUL);

							}catch(Exception ee){
 								logger.error("Exception while pushing Core Advice to Queue for accountOpening:" + ee);
								updateSafRepoCoreStatus(PortalConstants.SAF_STATUS_FAILED);
							}
						}
					}
				}
				else {
					SafRepoCoreModel safRepoCoreModel = new SafRepoCoreModel();
					safRepoCoreModel.setTransactionCode(workFlowWrapper.getTransactionCodeModel().getCode());
					safRepoCoreModel.setStatus(PortalConstants.SAF_STATUS_INITIATED);
					logger.info("[CoreAdviceQueingPreProcessor.run] Loading transactions against trx_code: " + safRepoCoreModel.getTransactionCode());
					CustomList<SafRepoCoreModel> customList = safRepoCoreDao.findByExample(safRepoCoreModel, null, null, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
					List<SafRepoCoreModel> safRepoCoreModels = customList.getResultsetList();

					if (safRepoCoreModels != null && safRepoCoreModels.size() > 0) {

						for (SafRepoCoreModel safModel : safRepoCoreModels) {
							try {
								middlewareAdviceVO = CoreAdviceUtil.prepareMiddlewareAdviceVO(safModel);

								pushCoreAdviceQueue(middlewareAdviceVO);

								updateSafRepoCoreStatus(workFlowWrapper.getTransactionCodeModel().getCode(), PortalConstants.SAF_STATUS_SUCCESSFUL);

							} catch (Exception ee) {
								logger.error("Exception while pushing Core Advice to Queue for transactionCodeId:" + workFlowWrapper.getTransactionCodeModel().getTransactionCodeId(), ee);
								updateSafRepoCoreStatus(workFlowWrapper.getTransactionCodeModel().getCode(), PortalConstants.SAF_STATUS_FAILED);
							}
						}
					}
				}
			  
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Exception in CoreQueingThread.run()...",e);
			}
		}
		
	}

	private void updateSafRepoCoreStatus(String trxCode,String status) throws FrameworkCheckedException{
		try{
			SafRepoCoreModel safRepoCoreModel = new SafRepoCoreModel();
			safRepoCoreModel.setTransactionCode(trxCode);
			safRepoCoreModel.setStatus(status);
			safRepoCoreModel.setUpdatedOn(new Date());

			safRepoCoreDao.updateStatus(safRepoCoreModel);
		}catch(Exception ex){
			logger.error("Failed to updateSafRepoCoreStatus to '"+status+"' for  trxCode:" + trxCode, ex);
		}
	}

	private void updateSafRepoCoreStatus(String status) throws FrameworkCheckedException{
		try{
			SafRepoCoreModel safRepoCoreModel = new SafRepoCoreModel();
			safRepoCoreModel.setStatus(status);
			safRepoCoreModel.setUpdatedOn(new Date());

			safRepoCoreDao.updateStatus(safRepoCoreModel);
		}catch(Exception ex){
			logger.error("Failed to updateSafRepoCoreStatus to '"+status+"'" + ex);
		}
	}

	public void setSafRepoCoreDao(SafRepoCoreDao safRepoCoreDao) {
		this.safRepoCoreDao = safRepoCoreDao;
	}

	public void setCoreAdviceSender(CoreAdviceSender coreAdviceSender) {
		this.coreAdviceSender = coreAdviceSender;
	}

	
}
