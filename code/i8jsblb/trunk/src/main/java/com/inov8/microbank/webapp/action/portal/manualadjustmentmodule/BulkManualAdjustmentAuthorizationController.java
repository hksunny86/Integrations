package com.inov8.microbank.webapp.action.portal.manualadjustmentmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.*;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.ActionStatusModel;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationHistoryModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.model.portal.authorizationreferencedata.BulkManualAdjustmentRefDataModel;
import com.inov8.microbank.common.model.portal.bulkdisbursements.BulkManualAdjustmentModel;
import com.inov8.microbank.common.util.ActionAuthorizationConstantsInterface;
import com.inov8.microbank.common.util.IssueTypeStatusConstantsInterface;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.manualadjustmentmodule.ManualAdjustmentManager;
import com.inov8.microbank.webapp.action.authorizationmodule.AdvanceAuthorizationFormController;
import com.thoughtworks.xstream.XStream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BulkManualAdjustmentAuthorizationController extends AdvanceAuthorizationFormController {

	private static final Logger LOGGER = Logger.getLogger( ManualAdjustmentAuthorizationController.class );
	private ManualAdjustmentManager manualAdjustmentManager;
	private ReferenceDataManager referenceDataManager;

	public BulkManualAdjustmentAuthorizationController() {
		setCommandName("actionAuthorizationModel");
		setCommandClass(ActionAuthorizationModel.class);
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest request)
			throws Exception {
		Map<String, List<?>> referenceDataMap = new HashMap<String, List<?>>();
		boolean escalateRequest = ServletRequestUtils.getBooleanParameter(request, "escalateRequest", false);
		boolean resolveRequest = ServletRequestUtils.getBooleanParameter(request, "resolveRequest", false);

		if (escalateRequest || resolveRequest){
			ActionStatusModel actionStatusModel = new ActionStatusModel();
			ReferenceDataWrapper refDataWrapper = new ReferenceDataWrapperImpl( actionStatusModel, "name", SortingOrder.ASC );
			referenceDataManager.getReferenceData( refDataWrapper );
			List<ActionStatusModel> actionStatusModelList;
			actionStatusModelList=refDataWrapper.getReferenceDataList();
			List<ActionStatusModel> tempActionStatusModelList = new ArrayList<>();

			for (ActionStatusModel actionStatusModel2 :  actionStatusModelList) {
				if(((actionStatusModel2.getActionStatusId().intValue()== ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED.intValue())
						||(actionStatusModel2.getActionStatusId().intValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL.intValue())
						|| (actionStatusModel2.getActionStatusId().intValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_CANCELLED.intValue())
						|| (actionStatusModel2.getActionStatusId().intValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVAL_DENIED.intValue()))
						&& escalateRequest )
					tempActionStatusModelList.add(actionStatusModel2);
				else if((actionStatusModel2.getActionStatusId().intValue()== ActionAuthorizationConstantsInterface.ACTION_STATUS_RESOLVED.intValue()) && resolveRequest)
					tempActionStatusModelList.add(actionStatusModel2);
			}
			referenceDataMap.put( "actionStatusModel", tempActionStatusModelList);

			////// Action Authorization history////
			Long actionAuthorizationId = ServletRequestUtils.getLongParameter(request, "actionAuthorizationId");
			ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(actionAuthorizationId);

			ActionAuthorizationHistoryModel actionAuthorizationHistoryModel = new ActionAuthorizationHistoryModel();
			actionAuthorizationHistoryModel.setActionAthorizationIdActionAuthorizationModel(actionAuthorizationModel);

			List<ActionAuthorizationHistoryModel> actionAuthorizationHistoryModelList;

			refDataWrapper = new ReferenceDataWrapperImpl( actionAuthorizationHistoryModel, "escalationLevel", SortingOrder.ASC );
			referenceDataManager.getReferenceData( refDataWrapper );

			actionAuthorizationHistoryModelList=refDataWrapper.getReferenceDataList();

			referenceDataMap.put( "actionAuthorizationHistoryModelList",actionAuthorizationHistoryModelList );

			if(actionAuthorizationModel.getActionStatusId().longValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK.longValue()
					&& actionAuthorizationModel.getCreatedById().longValue()== UserUtils.getCurrentUser().getAppUserId()){
				boolean isAssignedBack=false;
				isAssignedBack=true;
				request.setAttribute( "isAssignedBack",isAssignedBack );
			}
		}
		return referenceDataMap;
	}


	@Override
	protected Object loadFormBackingObject(HttpServletRequest request) throws Exception {

		boolean escalateRequest = ServletRequestUtils.getBooleanParameter(request, "escalateRequest", false);
		boolean resolveRequest = ServletRequestUtils.getBooleanParameter(request, "resolveRequest", false);

		if (escalateRequest || resolveRequest){
			Long actionAuthorizationId = ServletRequestUtils.getLongParameter(request, "actionAuthorizationId");
			ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(actionAuthorizationId);

			XStream xstream = new XStream();
//			BulkManualAdjustmentRefDataModel manualAdjustmentRefDataModel = (BulkManualAdjustmentRefDataModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
			BulkManualAdjustmentVOModel bulkManualAdjustmentVOModel = (BulkManualAdjustmentVOModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
			Long batchId = bulkManualAdjustmentVOModel.getBatchId();
			BulkManualAdjustmentModel bulkManualAdjustmentModel = new BulkManualAdjustmentModel();
			bulkManualAdjustmentModel.setBatchId(batchId);
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			searchBaseWrapper.setBasePersistableModel(bulkManualAdjustmentModel);

			List<BulkManualAdjustmentModel> resultList = manualAdjustmentManager.loadBulkManualAdjustmentModelList(searchBaseWrapper);

			Double totalAmount = 0d;
			for (BulkManualAdjustmentModel model : resultList) {
				totalAmount += model.getAmount();
			}
			request.setAttribute("bulkManualAdjustmentsModelList",resultList);
			request.setAttribute("totalAmount",totalAmount);
			request.setAttribute("trxCount",resultList.size());
			/*request.setAttribute("transactionCodeId",manualAdjustmentRefDataModel.getTransactionCodeId());
			request.setAttribute("adjustmentType",manualAdjustmentRefDataModel.getAdjustmentType());
			request.setAttribute("fromACNo",manualAdjustmentRefDataModel.getFromACNo());
			request.setAttribute("fromACNick",manualAdjustmentRefDataModel.getFromACNick());
			request.setAttribute("toACNo",manualAdjustmentRefDataModel.getToACNo());
			request.setAttribute("toACNick",manualAdjustmentRefDataModel.getToACNick());
			request.setAttribute("amount",manualAdjustmentRefDataModel.getAmount());
			request.setAttribute("comments",manualAdjustmentRefDataModel.getComments());*/

			return actionAuthorizationModel;
		}
		else
			return new ActionAuthorizationModel();
	}



	@Override
	protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse response, Object object,
									BindException errors) throws Exception
	{
		return null;
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object object,
									BindException errors) throws Exception
	{
		return null;
	}


	@Override
	protected ModelAndView onAuthorization(HttpServletRequest request, HttpServletResponse response,Object command, BindException errors) throws Exception {
		return null;
	}

	@Override
	protected ModelAndView onEscalate(HttpServletRequest req,HttpServletResponse resp, Object command, BindException errors) throws Exception {
		ModelAndView modelAndView = null;
		ActionAuthorizationModel model = (ActionAuthorizationModel) command; 
		String partnersAssociation[] = req.getParameterValues("checkedList"); 
		try{
			XStream xstream = new XStream();

			ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(model.getActionAuthorizationId());
			boolean isValidChecker  = usecaseFacade.isCheckerAtLevel(actionAuthorizationModel.getUsecaseId(), actionAuthorizationModel.getEscalationLevel(),UserUtils.getCurrentUser().getAppUserId());
			long currentUserId= UserUtils.getCurrentUser().getAppUserId();
			
			BulkManualAdjustmentVOModel bulkManualAdjustmentVOModel = (BulkManualAdjustmentVOModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
			Long batchId = bulkManualAdjustmentVOModel.getBatchId();

			UsecaseModel usecaseModel= usecaseFacade.loadUsecase(actionAuthorizationModel.getUsecaseId());
			if(model.getActionStatusId().longValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED.longValue()
					&& actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)){
				if((!isValidChecker) || (actionAuthorizationModel.getCreatedById().longValue()==currentUserId)){
					throw new FrameworkCheckedException("You are not authorized to update action status.");
				}

				long nextAuthorizationLevel = usecaseFacade.getNextAuthorizationLevel(PortalConstants.BULK_MANUAL_ADJUSTMENT_USECASE_ID,actionAuthorizationModel.getEscalationLevel());
				if(nextAuthorizationLevel<1){
					manualAdjustmentManager.updateIsApprovedForBatch(batchId , partnersAssociation );
					List<BulkManualAdjustmentModel> bulkManualAdjustmentModelList = loadBulkManualAdjustmentModelList(batchId);
					if (CollectionUtils.isNotEmpty(bulkManualAdjustmentModelList)) {
						for (BulkManualAdjustmentModel bulkModel : bulkManualAdjustmentModelList) {
							try{
								BulkManualAdjustmentRefDataModel bulkManualAdjustmentRefDataModel = convertBulkManualAdjustmentToRefDataModel(bulkModel, actionAuthorizationModel);
								manualAdjustmentManager.pushBulkManualAdjustmentToQueue(bulkManualAdjustmentRefDataModel);
							}catch(Exception ex){
								logger.error("Failed to pushBulkManualAdjustmentToQueue for BulkAdjustmentId:" + bulkModel.getBulkAdjustmentId() + " Exception:" + ex.getMessage(), ex);
							}

						}
					}

					String msg = this.getText("manualadjustment.add.success", req.getLocale());
					String newMsg = msg + " against Batch ID : "+batchId;
					this.saveMessage(req, newMsg);

					if(actionAuthorizationModel.getEscalationLevel().intValue()< usecaseModel.getEscalationLevels().intValue()){
						approvedWithIntimationLevelsNext(actionAuthorizationModel,model, usecaseModel,req);
					}
					else
					{
						approvedAtMaxLevel(actionAuthorizationModel, model);
					}
				}else{
					escalateToNextLevel(actionAuthorizationModel,model, nextAuthorizationLevel, usecaseModel.getUsecaseId(),req);
				}

			}else if(model.getActionStatusId().longValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVAL_DENIED.longValue() && actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)){
				isValidChecker  = usecaseFacade.isCheckerAtLevel(actionAuthorizationModel.getUsecaseId(), actionAuthorizationModel.getEscalationLevel(),UserUtils.getCurrentUser().getAppUserId());

				if((!isValidChecker) || (actionAuthorizationModel.getCreatedById().equals(currentUserId))){
					throw new FrameworkCheckedException("You are not authorized to update action status.");
				}
				actionDeniedOrCancelled(actionAuthorizationModel, model,req);
			}else if(model.getActionStatusId().longValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_CANCELLED.longValue()
					&& (actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)
					|| actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK))){

				if(!(actionAuthorizationModel.getCreatedById().equals(currentUserId))){
					throw new FrameworkCheckedException("You are not authorized to update action status.");
				}
				actionDeniedOrCancelled(actionAuthorizationModel,model,req);
			}else if(model.getActionStatusId().longValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK.longValue()
					&& actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)){
				isValidChecker  = usecaseFacade.isCheckerAtLevel(actionAuthorizationModel.getUsecaseId(), actionAuthorizationModel.getEscalationLevel(),UserUtils.getCurrentUser().getAppUserId());

				if((!isValidChecker) || (actionAuthorizationModel.getCreatedById().equals(currentUserId))){
					throw new FrameworkCheckedException("You are not authorized to update action status.");
				}
				requestAssignedBack(actionAuthorizationModel,model,req);
			}else if(model.getActionStatusId().longValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_RE_SUBMIT.longValue()
					&& actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK)){

				if(!(actionAuthorizationModel.getCreatedById().equals(currentUserId))){
					throw new FrameworkCheckedException("You are not authorized to update action status.");
				}
			}else{
				throw new FrameworkCheckedException("Invalid status marked");
			}
		}catch(WorkFlowException wfe){
			wfe.printStackTrace();
			this.showForm(req, resp, errors);
			req.setAttribute("message", super.getText("manualadjustment.add.failure.lowbalance", req.getLocale()));
			req.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
			return super.showForm(req, resp, errors);
		}catch(FrameworkCheckedException fce){
			fce.printStackTrace();
			String msg = null;
			if(null!=fce.getMessage() && fce.getMessage().contains("Same BB Accounts Type"))
				msg = "Manual Correction/Adjustment for Customer to Customer BLB A/c or Agent to Agent BLB A/c is not Allowed";
			else if(fce.getMessage().contains("You are not authorized to update action status."))
				msg = "You are not authorized to update action status.";
			else if(fce.getMessage().contains("Invalid status marked"))
				msg = "Invalid status marked";
			else
				msg = super.getText("manualadjustment.add.failure", req.getLocale());

			req.setAttribute("message", msg);
			req.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
			return super.showForm(req, resp, errors);

		}catch(Exception ex){
			ex.printStackTrace();
			String msg = super.getText("manualadjustment.add.failure", req.getLocale());
			req.setAttribute("message", msg);
			req.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
			return super.showForm(req, resp, errors);
		}

		req.setAttribute("status",IssueTypeStatusConstantsInterface.SUCCESS);
		modelAndView = super.showForm(req, resp, errors);
		return modelAndView;
	}

	@Override
	protected ModelAndView onResolve(HttpServletRequest req, HttpServletResponse resp, Object command, BindException errors) throws Exception {

		ModelAndView modelAndView = null;
		ActionAuthorizationModel model = (ActionAuthorizationModel) command;
		String partnersAssociation[] = req.getParameterValues("checkedList"); 
		try{
			XStream xstream = new XStream();

			ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(model.getActionAuthorizationId());
			UsecaseModel usecaseModel= usecaseFacade.loadUsecase(actionAuthorizationModel.getUsecaseId());
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			
			BulkManualAdjustmentVOModel bulkManualAdjustmentVOModel = (BulkManualAdjustmentVOModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
			Long batchId = bulkManualAdjustmentVOModel.getBatchId();

			manualAdjustmentManager.updateIsApprovedForBatch(batchId , partnersAssociation);
			List<BulkManualAdjustmentModel> bulkManualAdjustmentModelList = loadBulkManualAdjustmentModelList(batchId);
			if (CollectionUtils.isNotEmpty(bulkManualAdjustmentModelList)) {
				
			//	manualAdjustmentManager.updateIsApprovedForBatch(batchId , partnersAssociation);
				
				for (BulkManualAdjustmentModel bulkModel : bulkManualAdjustmentModelList) {
					try{
						
						BulkManualAdjustmentRefDataModel bulkManualAdjustmentRefDataModel = convertBulkManualAdjustmentToRefDataModel(bulkModel, actionAuthorizationModel);
						manualAdjustmentManager.pushBulkManualAdjustmentToQueue(bulkManualAdjustmentRefDataModel);
					}catch(Exception ex){
						logger.error("Failed to pushBulkManualAdjustmentToQueue for BulkAdjustmentId:" + bulkModel.getBulkAdjustmentId() + " Exception:" + ex.getMessage(), ex);
					}
				}
			}

			String msg = this.getText("manualadjustment.add.success", req.getLocale());
			String newMsg=msg+" against Batch ID : "+batchId;
			this.saveMessage(req, newMsg);

			resolveWithIntimation(actionAuthorizationModel,model, usecaseModel, req);

		}
		catch (Exception ex)
		{
			LOGGER.error("Exception occured while Action Authorization on Action Authorization ID : "+model.getActionAuthorizationId(), ex);
			req.setAttribute("message", ex.getMessage());
			req.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
			return super.showForm(req, resp, errors);
		}
		req.setAttribute("status",IssueTypeStatusConstantsInterface.SUCCESS);
		modelAndView = super.showForm(req, resp, errors);
		return modelAndView;
	}

	private List<BulkManualAdjustmentModel> loadBulkManualAdjustmentModelList(Long batchID) throws Exception{
		BulkManualAdjustmentModel bulkManualAdjustmentModel = new BulkManualAdjustmentModel();
		bulkManualAdjustmentModel.setBatchId(batchID);
		bulkManualAdjustmentModel.setIsSkipped(false);
		bulkManualAdjustmentModel.setIsProcessed(Boolean.FALSE);
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel(bulkManualAdjustmentModel);

		return manualAdjustmentManager.loadBulkManualAdjustmentModelList(searchBaseWrapper);
	}

	private BulkManualAdjustmentRefDataModel convertBulkManualAdjustmentToRefDataModel(BulkManualAdjustmentModel bulkManualAdjModel, ActionAuthorizationModel actionAuthorizationModel){

		BulkManualAdjustmentRefDataModel bulkManualAdjustmentRefDataModel = new BulkManualAdjustmentRefDataModel();
		bulkManualAdjustmentRefDataModel.setBulkAdjustmentId(bulkManualAdjModel.getBulkAdjustmentId());
		bulkManualAdjustmentRefDataModel.setTrxnId(bulkManualAdjModel.getTrxnId());
		bulkManualAdjustmentRefDataModel.setAdjustmentType(bulkManualAdjModel.getAdjustmentType());
		bulkManualAdjustmentRefDataModel.setFromAccount(bulkManualAdjModel.getFromAccount());
		bulkManualAdjustmentRefDataModel.setFromAccountTitle(bulkManualAdjModel.getFromAccountTitle());
		bulkManualAdjustmentRefDataModel.setToAccount(bulkManualAdjModel.getToAccount());
		bulkManualAdjustmentRefDataModel.setToAccountTitle(bulkManualAdjModel.getToAccountTitle());
		bulkManualAdjustmentRefDataModel.setAmount(bulkManualAdjModel.getAmount());
		bulkManualAdjustmentRefDataModel.setComments(bulkManualAdjModel.getDescription());
		bulkManualAdjustmentRefDataModel.setCreatedBy(actionAuthorizationModel.getCreatedById());
		bulkManualAdjustmentRefDataModel.setCreatedOn(actionAuthorizationModel.getCreatedOn());
		bulkManualAdjustmentRefDataModel.setAuthorizerId(UserUtils.getCurrentUser().getUsername());
		bulkManualAdjustmentRefDataModel.setBatchID(bulkManualAdjModel.getBatchId());

		return bulkManualAdjustmentRefDataModel;
	}

	/*private void pushBulkManualAdjustmentBatchToQueue(BulkManualAdjustmentRefDataModel refDataModel) throws Exception{
		Long batchId = refDataModel.getBatchID();
		BulkManualAdjustmentModel bulkManualAdjustmentModel = new BulkManualAdjustmentModel();
		bulkManualAdjustmentModel.setBatchId(batchId);
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel(bulkManualAdjustmentModel);

		List<BulkManualAdjustmentModel> resultList = manualAdjustmentManager.loadBulkManualAdjustmentModelList(searchBaseWrapper);
		if (CollectionUtils.isNotEmpty(resultList)){
			for (BulkManualAdjustmentModel model : resultList) {

				ManualAdjustmentModel manualAdjustmentModel = new ManualAdjustmentModel();
				manualAdjustmentModel.setTransactionCodeId(model.getTrxnId());
				manualAdjustmentModel.setAdjustmentType(model.getAdjustmentType());
				manualAdjustmentModel.setFromACNo(model.getFromAccount());
//				manualAdjustmentModel.setFromACNick(model.getFromAccount);//TODO
				manualAdjustmentModel.setToACNo(model.getToAccount());
//				manualAdjustmentModel.setToACNick(model.getToACNick());
				manualAdjustmentModel.setAmount(model.getAmount());//TODO
				manualAdjustmentModel.setComments(model.getDescription());
				manualAdjustmentModel.setCreatedBy(model.getCreatedBy());
				manualAdjustmentModel.setCreatedOn(model.getCreatedOn());
				manualAdjustmentModel.setAuthorizerId(UserUtils.getCurrentUser().getUsername());

				manualAdjustmentManager.pushBulkManualAdjustmentToQueue(model);

			}
		}
	}*/

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public void setManualAdjustmentManager(ManualAdjustmentManager manualAdjustmentManager) {
		this.manualAdjustmentManager = manualAdjustmentManager;
	}

}
