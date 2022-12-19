package com.inov8.microbank.webapp.action.commissionmodule;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.ExceptionErrorCodes;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.CommissionReasonModel;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.commissionmodule.CommissionReasonManager;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Rizwan-ur-Rehman
 * @version 1.0
 */

public class CommissionReasonFormController extends AdvanceFormController {
	private CommissionReasonManager commissionReasonManager;

	private Long id;

	public CommissionReasonFormController() {
		setCommandName("commissionReasonModel");
		setCommandClass(CommissionReasonModel.class);
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest httpServletRequest) {
		return null;
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest httpServletRequest)
			throws Exception {
		id = ServletRequestUtils.getLongParameter(httpServletRequest,
				"commissionReasonId");
		if (null != id) {
			if (log.isDebugEnabled()) {
				log.debug("id is not null....retrieving object from DB");
			}

			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			CommissionReasonModel commissionReasonModel = new CommissionReasonModel();
			commissionReasonModel.setPrimaryKey(id);
			searchBaseWrapper.setBasePersistableModel(commissionReasonModel);
			searchBaseWrapper = this.commissionReasonManager
					.loadCommissionReason(searchBaseWrapper);
			return (CommissionReasonModel) searchBaseWrapper
					.getBasePersistableModel();
		} else {
			if (log.isDebugEnabled()) {
				log.debug("id is null....creating new instance of Model");
			}
			return new CommissionReasonModel();
		}
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object object,
			BindException errors) throws Exception {
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		try {
			CommissionReasonModel commissionReasonModel = (CommissionReasonModel) object;
			commissionReasonModel.setActive( commissionReasonModel.getActive() == null ? false : commissionReasonModel.getActive() );
			commissionReasonModel.setUpdatedByAppUserModel(UserUtils
					.getCurrentUser());
			commissionReasonModel.setCreatedByAppUserModel(UserUtils
					.getCurrentUser());
			baseWrapper.setBasePersistableModel(commissionReasonModel);
			baseWrapper = this.commissionReasonManager
					.createCommissionReason(baseWrapper);

			if (null != baseWrapper.getBasePersistableModel()) {
				this.saveMessage(httpServletRequest,
						"Record Saved Successfully");
				ModelAndView modelAndView = new ModelAndView(this
						.getSuccessView());
				return modelAndView;
			} else {

				this.saveMessage(httpServletRequest,
						"Commission Reason with the same name already exists.");
				return super.showForm(httpServletRequest, httpServletResponse,
						errors);
			}
		} catch (FrameworkCheckedException ex) {
			if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION == ex
					.getErrorCode()) {
				super.saveMessage(httpServletRequest,
						"Record Could Not Be Saved");
				return super.showForm(httpServletRequest, httpServletResponse,
						errors);
			}

			throw ex;
		}
		catch (Exception ex) {
			
				super.saveMessage(httpServletRequest,
						MessageUtil.getMessage("6075"));
				return super.showForm(httpServletRequest, httpServletResponse,
						errors);
		}
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object object,
			BindException errors) throws Exception {
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		try {
			Date nowDate = new Date();
			CommissionReasonModel commissionReasonModel = (CommissionReasonModel) object;

			commissionReasonModel.setActive( commissionReasonModel.getActive() == null ? false : commissionReasonModel.getActive() );
			
			CommissionReasonModel tempCommissionReasonModel;

			searchBaseWrapper.setBasePersistableModel(commissionReasonModel);
			searchBaseWrapper = this.commissionReasonManager
					.loadCommissionReason(searchBaseWrapper);

			tempCommissionReasonModel = (CommissionReasonModel) searchBaseWrapper
					.getBasePersistableModel();
			commissionReasonModel.setCreatedOn(tempCommissionReasonModel
					.getCreatedOn());
			commissionReasonModel.setCreatedBy(tempCommissionReasonModel
					.getCreatedBy());
			commissionReasonModel.setUpdatedOn(nowDate);
			commissionReasonModel.setUpdatedByAppUserModel(UserUtils
					.getCurrentUser());

			baseWrapper.setBasePersistableModel(commissionReasonModel);
			baseWrapper = this.commissionReasonManager
					.updateCommissionReason(baseWrapper);

			if (null != baseWrapper.getBasePersistableModel()) {
				this.saveMessage(httpServletRequest,
						"Record saved Successfully");
				ModelAndView modelAndView = new ModelAndView(this
						.getSuccessView());
				return modelAndView;
			} else {
				this.saveMessage(httpServletRequest,
						"Record could not be Saved");
				return super.showForm(httpServletRequest, httpServletResponse,
						errors);
			}
		} catch (FrameworkCheckedException ex) {
			if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION == ex
					.getErrorCode()) {
				super.saveMessage(httpServletRequest,
						"Commission reason could not be saved");
				return super.showForm(httpServletRequest, httpServletResponse,
						errors);
			}
			throw ex;
		}catch (Exception ex) {
				super.saveMessage(httpServletRequest,
						MessageUtil.getMessage("6075"));
				return super.showForm(httpServletRequest, httpServletResponse,
						errors);
		}
	}

	public void setCommissionReasonManager(
			CommissionReasonManager commissionReasonManager) {
		this.commissionReasonManager = commissionReasonManager;
	}

}
