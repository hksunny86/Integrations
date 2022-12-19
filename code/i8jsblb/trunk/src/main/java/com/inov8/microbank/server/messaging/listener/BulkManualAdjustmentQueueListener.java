package com.inov8.microbank.server.messaging.listener;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.exception.WorkFlowExceptionTranslator;
import com.inov8.microbank.common.model.ManualAdjustmentModel;
import com.inov8.microbank.common.model.portal.authorizationreferencedata.BulkManualAdjustmentRefDataModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.server.service.manualadjustmentmodule.ManualAdjustmentManager;
import com.inov8.microbank.server.service.xml.XmlMarshaller;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class BulkManualAdjustmentQueueListener implements MessageListener {
	
	private static Log logger = LogFactory.getLog(BulkManualAdjustmentQueueListener.class);
	
	private String delay;
	private ManualAdjustmentManager manualAdjustmentManager;
	private XmlMarshaller<BulkManualAdjustmentRefDataModel> xmlMarshaller;
	private WorkFlowExceptionTranslator workflowExceptionTranslator;

	@Override
	public void onMessage(Message message) {

		BulkManualAdjustmentRefDataModel model = null;
		try {
			if(message instanceof TextMessage) {
				TextMessage textMessage = (TextMessage) message;
				String xml = textMessage.getText();
				if(!GenericValidator.isBlankOrNull(xml))
				{
					model = xmlMarshaller.unmarshal(xml);

					logger.info("Started processing Batch ID: " + (model == null ? ("null") : model.getBatchID()));
					logger.info("Started processing Manual Adjustment against the Transaction Code: " + (model == null ? ("Null TRX_CODE") : model.getTrxnId()));
					if(model != null && (model.getProcessed() == null ||(model.getProcessed() != null && !model.getProcessed())))
						this.processBulkManualAdjustments(model);
				}
			}
			
		}catch(Exception ex){
			logger.error("[BulkManualAdjustmentQueueListener.onMessage] Exception Occurred..."+ex.getMessage(), ex);
			if (model != null){
				logger.error("Going to save error desc. for BulkAdjustmentID:" + model.getBulkAdjustmentId() + " Adjustment Type:" + model.getAdjustmentType() + " Amount: " + model.getAmount());
				
				String reason = MessageUtil.getMessage(WorkFlowErrorCodeConstants.GENERAL_ERROR);
				if(!StringUtil.isNullOrEmpty(ex.getMessage())){
					reason = ex.getMessage();
				}
				if(StringUtil.isFailureReasonId(ex.getMessage())){
					Long failureReasonId = Long.parseLong(ex.getMessage());
					WorkFlowException wfEx = this.workflowExceptionTranslator.translateWorkFlowException(new WorkFlowException(ex.getMessage()),FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
					reason = wfEx.getMessage();
				}
				model.setErrorDescription(reason);
				updateErrorMessage(model.getBulkAdjustmentId(), model.getErrorDescription());
			}
		}finally{
			removeThreadLocals();
		}
	}

	public void processBulkManualAdjustments(BulkManualAdjustmentRefDataModel model) throws Exception{

		String fromCoreAccTitle = "";
		String toCoreAccTitle = "";

		ManualAdjustmentModel manualAdjustmentModel = new ManualAdjustmentModel();
		manualAdjustmentModel.setTransactionCodeId(model.getTrxnId());
		manualAdjustmentModel.setAdjustmentType(model.getAdjustmentType());
		manualAdjustmentModel.setFromACNo(model.getFromAccount());
		manualAdjustmentModel.setFromACNick(model.getFromAccountTitle());
		manualAdjustmentModel.setToACNo(model.getToAccount());
		manualAdjustmentModel.setToACNick(model.getToAccountTitle());
		manualAdjustmentModel.setAmount(model.getAmount());//TODO
		manualAdjustmentModel.setComments(model.getComments());
		manualAdjustmentModel.setCreatedBy(model.getCreatedBy());
		manualAdjustmentModel.setCreatedOn(model.getCreatedOn());
		manualAdjustmentModel.setAuthorizerId(UserUtils.getCurrentUser().getUsername());
		manualAdjustmentModel.setAdjustmentCategory(2L);
		manualAdjustmentModel.setAuthorizerId(model.getAuthorizerId());
		manualAdjustmentModel.setBulkAdjustmentId(model.getBulkAdjustmentId());
		
		if(manualAdjustmentModel.getAdjustmentType().equals(ManualAdjustmentTypeConstants.BB_TO_BB)) {
			manualAdjustmentManager.makeOlaToOlaTransfer(manualAdjustmentModel, model.getActionAuthorizationId());
		}else if(manualAdjustmentModel.getAdjustmentType().equals(ManualAdjustmentTypeConstants.BB_TO_CORE)){
			toCoreAccTitle = manualAdjustmentManager.fetchCoreAccountTitle(manualAdjustmentModel.getToACNo());
			if(toCoreAccTitle.equals("null")){
				throw new FrameworkCheckedException("The Core Account does not exist.");
			}else{
				manualAdjustmentModel.setToACNick(toCoreAccTitle);
				manualAdjustmentManager.makeBBToCoreTransfer(manualAdjustmentModel, model.getActionAuthorizationId());
			}
		}else if(manualAdjustmentModel.getAdjustmentType().equals(ManualAdjustmentTypeConstants.CORE_TO_BB)){
			fromCoreAccTitle = manualAdjustmentManager.fetchCoreAccountTitle(manualAdjustmentModel.getFromACNo());
			if(fromCoreAccTitle.equals("null")){
				throw new FrameworkCheckedException("The Core Account does not exist.");
			}else{
				manualAdjustmentModel.setFromACNick(fromCoreAccTitle);
				manualAdjustmentManager.makeCoreToBBTransfer(manualAdjustmentModel, model.getActionAuthorizationId());
			}

		}

		model.setProcessed(true);
	}

	public void updateErrorMessage(Long bulkManualAdjustmentId, String errorMessage) {
		try{
			manualAdjustmentManager.updateBulkAdjustmentErrorMessage(bulkManualAdjustmentId, errorMessage);
		}catch (Exception ex){
			ex.printStackTrace();
			logger.error("Error in saving Bulk Model. ID: " + bulkManualAdjustmentId + ". Error Message: " + errorMessage);
		}
	}

	private void removeThreadLocals(){
		ThreadLocalAppUser.remove();
		ThreadLocalUserDeviceAccounts.remove();
		ThreadLocalActionLog.remove();
	}

	public void setManualAdjustmentManager(ManualAdjustmentManager manualAdjustmentManager) {
		this.manualAdjustmentManager = manualAdjustmentManager;
	}

	public void setXmlMarshaller(XmlMarshaller<BulkManualAdjustmentRefDataModel> xmlMarshaller)
	{
		this.xmlMarshaller = xmlMarshaller;
	}

	public void setWorkflowExceptionTranslator(
			WorkFlowExceptionTranslator workflowExceptionTranslator) {
		this.workflowExceptionTranslator = workflowExceptionTranslator;
	}

}