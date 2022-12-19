package com.inov8.microbank.webapp.action.portal.manualadjustmentmodule;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inov8.microbank.common.util.*;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.ManualAdjustmentModel;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.model.portal.authorizationreferencedata.ManualAdjustmentRefDataModel;
import com.inov8.microbank.common.model.portal.authorizationreferencedata.UserGroupReferenceDataModel;
import com.inov8.microbank.common.model.portal.partnergroupmodule.PartnerGroupPermissionListViewModel;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.manualadjustmentmodule.ManualAdjustmentManager;
import com.inov8.microbank.webapp.action.authorizationmodule.AdvanceAuthorizationFormController;
import com.thoughtworks.xstream.XStream;

public class ManualAdjustmentFormController extends AdvanceAuthorizationFormController {
    
	private ManualAdjustmentManager manualAdjustmentManager;
    private ReferenceDataManager referenceDataManager;
	private String acType1;
	private String acType2;
    
	public ManualAdjustmentFormController() {
		setCommandName("manualAdjustmentModel");
		setCommandClass(ManualAdjustmentModel.class);
	}
	
	@Override
	protected Map<String, Object> loadReferenceData(HttpServletRequest req) throws Exception {
		Map<String, Object> referenceDataMap = new HashMap<String, Object>(1);
		List<LabelValueBean> adjustmentTypeList = new ArrayList<LabelValueBean>();
	    LabelValueBean adjustmentType = new LabelValueBean("BB to BB", "1");
	    adjustmentTypeList.add(adjustmentType);
	    adjustmentType = new LabelValueBean("BB to Core", "2");
	    adjustmentTypeList.add(adjustmentType);
	    adjustmentType = new LabelValueBean("Core to BB", "3");
	    adjustmentTypeList.add(adjustmentType);
	    referenceDataMap.put("adjustmentTypeList", adjustmentTypeList);
	    return referenceDataMap;
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest req) throws Exception {
		
		boolean isReSubmit = ServletRequestUtils.getBooleanParameter(req, "isReSubmit",false);
		boolean isReadOnly = ServletRequestUtils.getBooleanParameter(req, "isReadOnly",false);
		ManualAdjustmentModel manualAdjustmentModel = new ManualAdjustmentModel();
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		ManualAdjustmentRefDataModel manualAdjustmentRefDataModel = null;
				
		/// Added for Resubmit Authorization Request 
		if(isReSubmit || isReadOnly){
			Long actionAuthorizationId = ServletRequestUtils.getLongParameter(req,"authId");
			ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(actionAuthorizationId);
			
			if((actionAuthorizationModel.getCreatedById().longValue()!=UserUtils.getCurrentUser().getAppUserId().longValue()) && isReSubmit){
				throw new FrameworkCheckedException("illegal operation performed");
			}
	 	
			XStream xstream = new XStream();
			manualAdjustmentRefDataModel = (ManualAdjustmentRefDataModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
			
			///Populate from reference data Model
			manualAdjustmentModel.setTransactionCodeId(manualAdjustmentRefDataModel.getTransactionCodeId());
			manualAdjustmentModel.setFromACNo(manualAdjustmentRefDataModel.getFromACNo());
			manualAdjustmentModel.setFromACNick(manualAdjustmentRefDataModel.getFromACNick());
			manualAdjustmentModel.setAdjustmentType(manualAdjustmentRefDataModel.getAdjustmentType());
			manualAdjustmentModel.setToACNo(manualAdjustmentRefDataModel.getToACNo());
			manualAdjustmentModel.setToACNick(manualAdjustmentRefDataModel.getToACNick());
			manualAdjustmentModel.setAmount(manualAdjustmentRefDataModel.getAmount());
			manualAdjustmentModel.setComments(manualAdjustmentRefDataModel.getComments());
		}
		return manualAdjustmentModel;
	}
	
	@Override
	protected ModelAndView onCreate(HttpServletRequest req, HttpServletResponse res, Object model, BindException errors) throws Exception {
		Date nowDate = new Date();
		ManualAdjustmentModel manualAdjustmentModel = (ManualAdjustmentModel) model;
		boolean emptyTransactionCode = StringUtil.isNullOrEmpty(manualAdjustmentModel.getTransactionCodeId());
		
		manualAdjustmentModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		manualAdjustmentModel.setCreatedOn(nowDate);
		manualAdjustmentModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		manualAdjustmentModel.setUpdatedOn(nowDate);
		
		try{
			this.manualAdjustmentManager.validateTransactionCode(manualAdjustmentModel.getTransactionCodeId());
			
			Double fmtAmount = formatAmount(manualAdjustmentModel.getAmount());
			manualAdjustmentModel.setAmount(fmtAmount);
			if(fmtAmount < 0.01D){
				logger.error("Manual Adjustment: Amount cannot be less than 0.01");
				throw new FrameworkCheckedException("INVALID_AMOUNT_LOWER");
			}
			if(fmtAmount > 999999999999.99D){
				logger.error("Manual Adjustment: Amount cannot be greater than 999999999999.99");
				throw new FrameworkCheckedException("INVALID_AMOUNT_UPPER");
			}
			
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			if(manualAdjustmentModel.getAdjustmentType().equals(ManualAdjustmentTypeConstants.BB_TO_BB)) {
				this.manualAdjustmentManager.makeOlaToOlaTransfer(manualAdjustmentModel, null);
			}else if(manualAdjustmentModel.getAdjustmentType().equals(ManualAdjustmentTypeConstants.BB_TO_CORE)){
				this.manualAdjustmentManager.makeBBToCoreTransfer(manualAdjustmentModel, null);
			}else if(manualAdjustmentModel.getAdjustmentType().equals(ManualAdjustmentTypeConstants.CORE_TO_BB)){
				this.manualAdjustmentManager.makeCoreToBBTransfer(manualAdjustmentModel, null);
//			}else if(manualAdjustmentModel.getAdjustmentType().equals(ManualAdjustmentTypeConstants.ORACLE_FINANCIAL_SETTLEMENT)){
//				this.manualAdjustmentManager.makeOracleFinancialSettlement(manualAdjustmentModel, null);
			}

			String msg = super.getText("manualadjustment.add.success", req.getLocale());
			this.saveMessage(req, msg);
		}catch(WorkFlowException wfe){
				wfe.printStackTrace();
				super.saveMessage(req, super.getText("manualadjustment.add.failure.lowbalance", req.getLocale()));
				return super.showForm(req, res, errors);
		}catch(FrameworkCheckedException fce){
			fce.printStackTrace();
			if(null!=fce.getMessage() && fce.getMessage().contains("Same BB Accounts Type"))
				super.saveMessage(req,"Manual Correction/Adjustment for Customer to Customer BLB A/c or Agent to Agent BLB A/c is not Allowed");
			else if(null!=fce.getMessage() && fce.getMessage().equals("INVALID_TRX_ID"))
				super.saveMessage(req,"Invalid Transaction ID");
			else if(null!=fce.getMessage() && fce.getMessage().equals("INVALID_AMOUNT_LOWER"))
				super.saveMessage(req,"Amount cannot be less than 0.01");
			else if(null!=fce.getMessage() && fce.getMessage().equals("INVALID_AMOUNT_UPPER"))
				super.saveMessage(req,"Amount cannot be greater than 999999999999.99");
//			else if(fce.getMessage().equals("8062"))
//			{
//				super.saveMessage(req,"Per Day limit of Sender exceeded.");
//
//			}
//			else if(fce.getMessage().equals("8064"))
//			{
//				super.saveMessage(req,"Per Month limit of Sender exceeded.");
//
//			}
//			else if(fce.getMessage().equals("8061"))
//			{
//				super.saveMessage(req,"Per Day limit of Recipient exceeded.");
//
//			}
//			else if(fce.getMessage().equals("8063"))
//			{
//				super.saveMessage(req,"Per Month limit of Recipient exceeded.");
//
//			}
			else
				super.saveMessage(req, super.getText("manualadjustment.add.failure", req.getLocale()));
			return super.showForm(req, res, errors);
		}catch(Exception ex){
			logger.error(ex);
			super.saveMessage(req, super.getText("manualadjustment.add.failure", req.getLocale()));
			return super.showForm(req, res, errors);
		}finally{
			if(emptyTransactionCode){
				manualAdjustmentModel.setTransactionCodeId("");
			}
		}
		
		ModelAndView modelAndView = new ModelAndView( new RedirectView("p_manualadjustment.html?actionId=2"));
		return modelAndView;
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest req, HttpServletResponse res, Object obj, BindException errors) throws Exception {
		return null;
	}
	
	@Override
	protected ModelAndView onAuthorization(HttpServletRequest req, HttpServletResponse res,Object command, BindException errors) throws Exception {
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		ManualAdjustmentModel model = (ManualAdjustmentModel)command;
		String fromAccountNick = CommonUtils.escapeUnicode(model.getFromACNick());
		model.setFromACNick(fromAccountNick);
		boolean emptyTransactionCode = StringUtil.isNullOrEmpty(model.getTransactionCodeId());
		boolean resubmitRequest = ServletRequestUtils.getBooleanParameter(req, "resubmitRequest",false);
		Long usecaseId= ServletRequestUtils.getLongParameter(req, "usecaseId");
		
		Long actionAuthorizationId = null;
		if(resubmitRequest)
			actionAuthorizationId=ServletRequestUtils.getLongParameter(req, "actionAuthorizationId");
		///Populate reference data Model
		ManualAdjustmentRefDataModel manualAdjustmentRefDataModel = new ManualAdjustmentRefDataModel();
		manualAdjustmentRefDataModel.setManualAdjustmentID(model.getManualAdjustmentID());
		manualAdjustmentRefDataModel.setTransactionCodeId(model.getTransactionCodeId());
		manualAdjustmentRefDataModel.setAdjustmentType(model.getAdjustmentType());
		manualAdjustmentRefDataModel.setFromACNo(model.getFromACNo());
		manualAdjustmentRefDataModel.setFromACNick(model.getFromACNick());
		manualAdjustmentRefDataModel.setToACNo(model.getToACNo());
		manualAdjustmentRefDataModel.setToACNick(model.getToACNick());
		manualAdjustmentRefDataModel.setAmount(model.getAmount());
		manualAdjustmentRefDataModel.setComments(model.getComments());
		///End Populate reference data Model
		try
		{				
			Date nowDate = new Date();
			model.setCreatedByAppUserModel(UserUtils.getCurrentUser());
			model.setCreatedOn(nowDate);
			model.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			model.setUpdatedOn(nowDate);

			this.manualAdjustmentManager.validateTransactionCode(model.getTransactionCodeId());

			Double fmtAmount = formatAmount(model.getAmount());
			model.setAmount(fmtAmount);
			manualAdjustmentRefDataModel.setAmount(fmtAmount);
			if(fmtAmount < 0.01D){
				logger.error("Manual Adjustment: Amount cannot be less than 0.01");
				throw new FrameworkCheckedException("INVALID_AMOUNT_LOWER");
			}
			if(fmtAmount > 999999999999.99D){
				logger.error("Manual Adjustment: Amount cannot be greater than 999999999999.99");
				throw new FrameworkCheckedException("INVALID_AMOUNT_UPPER");
			}
			
			XStream xstream = new XStream();
			String refDataModelString= xstream.toXML(manualAdjustmentRefDataModel);
			
			UsecaseModel usecaseModel = usecaseFacade.loadUsecase(usecaseId);
			Long nextAuthorizationLevel = usecaseFacade.getNextAuthorizationLevel(usecaseId,new Long(0));
				
			if(nextAuthorizationLevel.intValue()<1){
					
				baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
				baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.MANUAL_ADJUSTMENT_USECASE_ID));

				if(model.getAdjustmentType().equals(ManualAdjustmentTypeConstants.BB_TO_BB)) {
					this.manualAdjustmentManager.makeOlaToOlaTransfer(model, actionAuthorizationId);
				}else if(model.getAdjustmentType().equals(ManualAdjustmentTypeConstants.BB_TO_CORE)){
					this.manualAdjustmentManager.makeBBToCoreTransfer(model, actionAuthorizationId);
				}else if(model.getAdjustmentType().equals(ManualAdjustmentTypeConstants.CORE_TO_BB)){
					this.manualAdjustmentManager.makeCoreToBBTransfer(model, actionAuthorizationId);
//				}else if(model.getAdjustmentType().equals(ManualAdjustmentTypeConstants.ORACLE_FINANCIAL_SETTLEMENT)){
//					this.manualAdjustmentManager.makeOracleFinancialSettlement(model, actionAuthorizationId);
				}

				String msg = super.getText("manualadjustment.add.success", req.getLocale());
				this.saveMessage(req, msg);
					
				actionAuthorizationId = performActionWithAllIntimationLevels(nextAuthorizationLevel,"", refDataModelString,null,usecaseModel,actionAuthorizationId,req);
				this.saveMessage(req,"Action is authorized successfully. Changes are saved against refernce Action ID : "+actionAuthorizationId);
			}else{									
				actionAuthorizationId = createAuthorizationRequest(nextAuthorizationLevel,"", refDataModelString,null,usecaseModel.getUsecaseId(),"",resubmitRequest,actionAuthorizationId,req);
				this.saveMessage(req,"Action is pending for approval against reference Action ID : "+actionAuthorizationId);
			}
		}catch(WorkFlowException wfe){
			wfe.printStackTrace();
			super.saveMessage(req, super.getText("manualadjustment.add.failure.lowbalance", req.getLocale()));
			return super.showForm(req, res, errors);
		}catch(FrameworkCheckedException fce){
			fce.printStackTrace();
			if(null!=fce.getMessage() && fce.getMessage().contains("Same BB Accounts Type"))
				super.saveMessage(req,"Manual Correction/Adjustment for Customer to Customer BLB A/c or Agent to Agent BLB A/c is not Allowed");
			else if(null!=fce.getMessage() && fce.getMessage().equals("INVALID_TRX_ID"))
				super.saveMessage(req,"Invalid Transaction ID");
			else if(null!=fce.getMessage() && fce.getMessage().equals("INVALID_AMOUNT_LOWER"))
				super.saveMessage(req,"Amount cannot be less than 0.01");
			else if(null!=fce.getMessage() && fce.getMessage().equals("INVALID_AMOUNT_UPPER"))
				super.saveMessage(req,"Amount cannot be greater than 999999999999.99");
//			else if(fce.getMessage().equals("8062"))
//			{
//				super.saveMessage(req,"Per Day limit of Sender exceeded.");
//
//			}
//			else if(fce.getMessage().equals("8064"))
//			{
//				super.saveMessage(req,"Per Month limit of Sender exceeded.");
//
//			}
//			else if(fce.getMessage().equals("8061"))
//			{
//				super.saveMessage(req,"Per Day limit of Recipient exceeded.");
//
//			}
//			else if(fce.getMessage().equals("8063"))
//			{
//				super.saveMessage(req,"Per Month limit of Recipient exceeded.");
//
//			}
			else
				super.saveMessage(req, super.getText("manualadjustment.add.failure", req.getLocale()));
			return super.showForm(req, res, errors);
		}catch(Exception ex){
			logger.error(ex);
			super.saveMessage(req, super.getText("manualadjustment.add.failure", req.getLocale()));
			return super.showForm(req, res, errors);
		}finally{
			if(emptyTransactionCode){
				model.setTransactionCodeId("");
			}
		}
		
		ModelAndView modelAndView = new ModelAndView( new RedirectView("p_manualadjustment.html?actionId=2"));
		return modelAndView;
	}
	
	private ActionLogModel insertActionLogRequiresNewTransaction(ActionLogModel actionLogModel) {
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(actionLogModel);
		try {
			//baseWrapper = this.actionLogManager.createOrUpdateActionLogRequiresNewTransaction(baseWrapper);
			actionLogModel = (ActionLogModel) baseWrapper.getBasePersistableModel();
		} catch (Exception ex) {
			logger.error("Exception occurred while processing", ex);

		}
		return actionLogModel;
	}
	
	private Double formatAmount(Double amount){
		if(amount == null){
			return 0.0D;
		}else{
			String amt = Formatter.formatDouble(amount);
			return Double.parseDouble(amt);
		}
	}
	
	public ReferenceDataManager getReferenceDataManager() {
		return referenceDataManager;
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public ManualAdjustmentManager getManualAdjustmentManager() {
		return manualAdjustmentManager;
	}

	public void setManualAdjustmentManager(ManualAdjustmentManager manualAdjustmentManager) {
		this.manualAdjustmentManager = manualAdjustmentManager;
	}

	public String getAcType1() {
		return acType1;
	}

	public void setAcType1(String acType1) {
		this.acType1 = acType1;
	}

	public String getAcType2() {
		return acType2;
	}

	public void setAcType2(String acType2) {
		this.acType2 = acType2;
	}

}