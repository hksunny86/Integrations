package com.inov8.microbank.webapp.action.stakeholdermodule;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.ExceptionErrorCodes;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.CommissionStakeholderModel;
import com.inov8.microbank.common.model.PaymentModeModel;
import com.inov8.microbank.common.model.StakeholderBankInfoModel;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;

/**
 *
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backened application for POS terminal</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Ltd</p>
 *
 * @author Ahmad Iqbal
 * @version 1.0
 *
 */

public class StakeholderBankInfoFormController extends AdvanceFormController {

	private StakeholderBankInfoManager stakeholderBankInfoManager;

	private ReferenceDataManager referenceDataManager;

	private Long id;

	public StakeholderBankInfoFormController() {
		setCommandName("stakeholderBankInfoModel");
		setCommandClass(StakeholderBankInfoModel.class);
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest request)
			throws Exception {
		/**
		 * code fragment to load reference data for BankModel
		 */
		BankModel bankModel = new BankModel();
		bankModel.setActive(true);
		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
				bankModel, "name", SortingOrder.ASC);

		referenceDataManager.getReferenceData(referenceDataWrapper);

		List<BankModel> bankModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null) {
			bankModelList = referenceDataWrapper.getReferenceDataList();
		}

		Map referenceDataMap = new HashMap();
		referenceDataMap.put("bankModelList", bankModelList);

		/**
		 * code fragment to load reference data for StakeholderCommissionModel
		 */

		CommissionStakeholderModel commissionStakeHolderModel = new CommissionStakeholderModel();
		referenceDataWrapper = new ReferenceDataWrapperImpl(
				commissionStakeHolderModel, "name", SortingOrder.ASC);
		referenceDataWrapper
				.setBasePersistableModel(commissionStakeHolderModel);
		referenceDataManager.getReferenceData(referenceDataWrapper);
		List<CommissionStakeholderModel> commissionStakeHolderModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null) {
			commissionStakeHolderModelList = referenceDataWrapper
					.getReferenceDataList();
		}

		referenceDataMap.put("commissionStakeHolderModelList",
				commissionStakeHolderModelList);

		PaymentModeModel paymentModeModel = new PaymentModeModel();
        
		//paymentModeModel.setActive(true);
		referenceDataWrapper = new ReferenceDataWrapperImpl(paymentModeModel,
				"name", SortingOrder.ASC);

		try {
			referenceDataManager.getReferenceData(referenceDataWrapper);
		} catch (FrameworkCheckedException ex1) {
			ex1.getStackTrace();
		}
		List<PaymentModeModel> paymentModeModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null) {
			paymentModeModelList = referenceDataWrapper.getReferenceDataList();
		}

		referenceDataMap.put("PaymentModeModelList", paymentModeModelList);

		return referenceDataMap;

	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest httpServletRequest)
			throws Exception {

		id = ServletRequestUtils.getLongParameter(httpServletRequest,
				"stakeholderBankInfoId");
		if (null != id) {
			if (log.isDebugEnabled()) {
				log.debug("id is not null....retrieving object from DB");
			}

			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			StakeholderBankInfoModel stakeholderBankInfoModel = new StakeholderBankInfoModel();
			stakeholderBankInfoModel.setStakeholderBankInfoId(id);

			searchBaseWrapper.setBasePersistableModel(stakeholderBankInfoModel);
			searchBaseWrapper = this.stakeholderBankInfoManager
					.loadStakeHolderBankInfo(searchBaseWrapper);
			stakeholderBankInfoModel = (StakeholderBankInfoModel) searchBaseWrapper
					.getBasePersistableModel();

			if (log.isDebugEnabled()) {
				log.debug("ticker String  is : "
						+ stakeholderBankInfoModel.getName());
			}
			return stakeholderBankInfoModel;
		} else {
			if (log.isDebugEnabled()) {
				log.debug("id is null....creating new instance of Model");
			}
			long theDate = new Date().getTime();
			StakeholderBankInfoModel stakeholderBankInfoModel = new StakeholderBankInfoModel();
			stakeholderBankInfoModel.setCreatedOn(new Date(theDate));
			return stakeholderBankInfoModel;
		}

	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		return this.createOrUpdate(request, response, command, errors);
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		return this.createOrUpdate(request, response, command, errors);
	}

	private ModelAndView createOrUpdate(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		try {
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			StakeholderBankInfoModel stakeholderBankInfoModel = (StakeholderBankInfoModel) command;
			stakeholderBankInfoModel.setActive( stakeholderBankInfoModel.getActive() == null ? false : stakeholderBankInfoModel.getActive() ) ;
			if (null != id) {

				StakeholderBankInfoModel tempStakeholderBankInfoModel;

				searchBaseWrapper
						.setBasePersistableModel(stakeholderBankInfoModel);
				searchBaseWrapper = this.stakeholderBankInfoManager
						.loadStakeHolderBankInfo(searchBaseWrapper);

				tempStakeholderBankInfoModel = (StakeholderBankInfoModel) searchBaseWrapper
						.getBasePersistableModel();
				stakeholderBankInfoModel
						.setCreatedOn(tempStakeholderBankInfoModel
								.getCreatedOn());
				stakeholderBankInfoModel
						.setCreatedBy(tempStakeholderBankInfoModel
								.getCreatedBy());
				stakeholderBankInfoModel.setUpdatedOn(new Date());
				stakeholderBankInfoModel.setUpdatedByAppUserModel(UserUtils
						.getCurrentUser());

			} else {
				stakeholderBankInfoModel.setCreatedOn(new Date());
				stakeholderBankInfoModel.setUpdatedByAppUserModel(UserUtils
						.getCurrentUser());
				stakeholderBankInfoModel.setCreatedByAppUserModel(UserUtils
						.getCurrentUser());
				stakeholderBankInfoModel.setUpdatedOn(new Date());

			}

			baseWrapper.setBasePersistableModel(stakeholderBankInfoModel);
			baseWrapper = this.stakeholderBankInfoManager
					.createOrUpdateStakeholderBankInfo(baseWrapper);
			if (null != baseWrapper.getBasePersistableModel()) { //if not found

				this.saveMessage(request, "Record saved successfully");
				ModelAndView modelAndView = new ModelAndView(this
						.getSuccessView());
				return modelAndView;
			} else {
				this
						.saveMessage(request,
								"Stakeholder Bank with the same name already exists.");
				return super.showForm(request, response, errors);
			}

		} catch (FrameworkCheckedException ex) {
			if (ex.getMessage().equalsIgnoreCase("NameUniqueException")) {
				super
						.saveMessage(request,
								"Stakeholder Bank with the same name already exists.");
				return super.showForm(request, response, errors);
			}

			else if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION == ex
					.getErrorCode()) {
				super.saveMessage(request, "Record could not be saved.");
				return super.showForm(request, response, errors);
			}
			throw ex;
		}
		catch (Exception ex)
	    {
	        super.saveMessage(request,MessageUtil.getMessage("6075"));
	        return super.showForm(request, response, errors);
	    }
	}

	public void setReferenceDataManager(
			ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public void setStakeholderBankInfoManager(
			StakeholderBankInfoManager stakeholderBankInfoManager) {
		this.stakeholderBankInfoManager = stakeholderBankInfoManager;
	}
}
