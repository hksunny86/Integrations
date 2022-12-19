package com.inov8.microbank.disbursement.controller;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.disbursement.util.DisbursementStatusConstants;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.disbursement.model.DisbursementFileInfoViewModel;
import com.inov8.microbank.disbursement.service.DisbursementFileFacade;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.thoughtworks.xstream.XStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.util.Date;

@Controller
public class DisbursementFileSummaryController {

	@Autowired
	private DisbursementFileFacade disbursementFacade;

	@Autowired
	private ActionLogManager actionLogManager;

	public DisbursementFileSummaryController() {
	}

	@RequestMapping(value= "/p_bulkfileinfosummaryviewpopup", method= RequestMethod.GET)
	public String showSummary(@RequestParam String id, Model model) throws FrameworkCheckedException {
		loadRefData(id, model);

		return "/p_bulkfileinfosummaryviewpopup";
	}

	@RequestMapping(value= "/processBatch")
	public String processBatch(@RequestParam String id, Model model) throws FrameworkCheckedException {

		DisbursementFileInfoViewModel infoViewModel = null;
		try {
			if (StringUtil.isNullOrEmpty(id)) {
				model.addAttribute("message", "In valid parameters provided.");
			}

			else {
				Long key = Long.parseLong(id);
				disbursementFacade.processBatch(key);
				model.addAttribute("messages", "Selected action performed.");

				infoViewModel = loadRefData(id, model);
			}
		}
		catch (Exception e) {
			model.addAttribute("messages", "Unable perform selected operation.");

			e.printStackTrace();
		}

		finally {
			if(infoViewModel != null) {
				infoViewModel.setAction("Process Batch");
				createActionLog(infoViewModel, PortalConstants.MANAGE_DISBURSEMENT_BATCH_USECASE_ID);
			}
		}

		return "/p_bulkfileinfosummaryviewpopup";
	}

	@RequestMapping(value= "/cancelBatch")
	public String cancelBatch(@RequestParam String id, Model model) throws FrameworkCheckedException {
		DisbursementFileInfoViewModel infoViewModel = null;

		try {
			if (StringUtil.isNullOrEmpty(id)) {
				model.addAttribute("message", "In valid parameters provided.");
			}

			else {
				Long key = Long.parseLong(id);
				int result = disbursementFacade.cancelBatch(key);
				if (result > 0) {
					model.addAttribute("messages", "Selected batch is cancelled.");
				}

				else {
					model.addAttribute("messages", "Unable perform selected operation.");
				}

				infoViewModel = loadRefData(id, model);
			}
		}

		catch (Exception e) {
			model.addAttribute("messages", "Unable perform selected operation.");

			e.printStackTrace();
		}

		finally {

			if(infoViewModel != null) {
				infoViewModel.setAction("Cancel Batch");
				createActionLog(infoViewModel, PortalConstants.CANCEL_DISBURSEMENT_BATCH_USECASE_ID);
			}
		}

		return "/p_bulkfileinfosummaryviewpopup";
	}

	@RequestMapping(value= "/putOnHolder")
	public String putOnHold(@RequestParam String id, Model model) throws FrameworkCheckedException {
		DisbursementFileInfoViewModel infoViewModel = null;
		try {
			if (StringUtil.isNullOrEmpty(id)) {
				model.addAttribute("message", "In valid parameters provided.");
			}

			else {
				Long key = Long.parseLong(id);
				int result = disbursementFacade.updateDisbursementFileStatus(key, DisbursementStatusConstants.STATUS_ON_HOLD);
				if (result > 0) {
					model.addAttribute("messages", "Selected batch is marked as On Hold.");
				}

				else {
					model.addAttribute("messages", "Unable to perform selected operation.");
				}

				infoViewModel = loadRefData(id, model);
			}
		}

		catch (Exception e) {
			model.addAttribute("messages", "Unable perform selected operation.");

			e.printStackTrace();
		}

		finally {

			if(infoViewModel != null) {
				infoViewModel.setAction("Put on Hold");
				createActionLog(infoViewModel, PortalConstants.MANAGE_DISBURSEMENT_BATCH_USECASE_ID);
			}
		}

		return "/p_bulkfileinfosummaryviewpopup";
	}

	private DisbursementFileInfoViewModel loadRefData(String id, Model model) {

		if (StringUtil.isNullOrEmpty(id)) {
			model.addAttribute("message", "In valid parameters provided.");

			return new DisbursementFileInfoViewModel();
		}

		DisbursementFileInfoViewModel infoViewModel = disbursementFacade.findDisbursementInfoViewModel(Long.parseLong(id));
		model.addAttribute("disbursementFileInfoViewModel", infoViewModel);

		if(DisbursementStatusConstants.STATUS_CANCELED.intValue() != infoViewModel.getStatus()) {
			Object obj = disbursementFacade.getDisbursementFileSettlementStatus(infoViewModel.getBatchNumber());
			if (obj != null) {
				Object[] results = (Object[]) obj;

				model.addAttribute("firstSettledOn", results[0]);
				model.addAttribute("lastSettledOn", results[1]);
				model.addAttribute("totalSettled", results[2]);
				model.addAttribute("totalCreated", results[3]);
			}
		}

		return infoViewModel;
	}

	private ActionLogModel createActionLog(DisbursementFileInfoViewModel vo, long useCaseId) throws FrameworkCheckedException{

		Timestamp now = new Timestamp(new Date().getTime());
		XStream xstream = new XStream();
		ActionLogModel actionLogModel = new ActionLogModel();
		actionLogModel.setInputXml(xstream.toXML(vo));
		actionLogModel.setActionId(PortalConstants.ACTION_UPDATE);
		actionLogModel.setUsecaseId(useCaseId);
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END);
		actionLogModel.setDeviceTypeId(PortalConstants.WEB_TYPE_DEVICE);
		actionLogModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
		actionLogModel.setUserName(UserUtils.getCurrentUser().getUsername());
		actionLogModel.setStartTime(now);
		actionLogModel.setEndTime(now);
		
		BaseWrapper bWrapper = new BaseWrapperImpl();
		bWrapper.setBasePersistableModel(actionLogModel);
		bWrapper = actionLogManager.createOrUpdateActionLog(bWrapper);
		return (ActionLogModel) bWrapper.getBasePersistableModel();
	}

	public void setDisbursementFacade(DisbursementFileFacade disbursementFacade) {
		this.disbursementFacade = disbursementFacade;
	}

	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}
}