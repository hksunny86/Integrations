package com.inov8.microbank.webapp.action.portal.changemobilemodule;

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
import com.inov8.microbank.common.model.MobileTypeModel;
import com.inov8.microbank.common.model.portal.changemobilemodule.ChangemobileListViewModel;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.server.service.portal.changemobilemodule.ChangeMobileManager;

public class ChangeMobileFormController extends AdvanceFormController {
	private ReferenceDataManager referenceDataManager;

	private ChangeMobileManager changeMobileManager;

	public ChangeMobileFormController() {
		setCommandName("changemobileListViewModel");
		setCommandClass(ChangemobileListViewModel.class);
	}

	protected Map loadReferenceData(HttpServletRequest httpServletRequest) {
		System.out.println("inside the loadReferenceData");

		MobileTypeModel mobileTypeModel = new MobileTypeModel();

		//paymentModeModel.setActive(true);
		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
				mobileTypeModel, "name", SortingOrder.ASC);

		try {
			referenceDataManager.getReferenceData(referenceDataWrapper);
		} catch (FrameworkCheckedException ex1) {
			ex1.getStackTrace();
		}
		List<MobileTypeModel> mobileTypeModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null) {
			mobileTypeModelList = referenceDataWrapper.getReferenceDataList();
		}

		Map referenceDataMap = new HashMap();
		referenceDataMap.put("MobileTypeModelList", mobileTypeModelList);
		return referenceDataMap;
	}

	protected ModelAndView onCreate(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object object,
			BindException bindException) throws Exception {
		System.out.println("inside the onCreate method");
		return null;
	}

	protected ModelAndView onUpdate(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object object,
			BindException bindException) throws Exception {

		try {
			long useCaseId = 0;
			long actionId = 0;
			System.out.println("inside the onUpdate method");
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			ChangemobileListViewModel changemobileListViewModel = (ChangemobileListViewModel) object;
			
			changemobileListViewModel.setAppUserId(new Long(EncryptionUtil.decryptWithDES(ServletRequestUtils.getStringParameter(httpServletRequest, "appUserId"))));

			if (httpServletRequest.getParameter(PortalConstants.KEY_USECASE_ID) != null) {
				useCaseId = Long.parseLong(httpServletRequest
						.getParameter(PortalConstants.KEY_USECASE_ID));
				System.out.println("usecase id " + useCaseId);
				baseWrapper
						.putObject(PortalConstants.KEY_USECASE_ID, useCaseId);

			}
			if (httpServletRequest.getParameter(PortalConstants.KEY_ACTION_ID) != null) {
				actionId = Long.parseLong(httpServletRequest
						.getParameter(PortalConstants.KEY_ACTION_ID));
				System.out.println("action id " + actionId);
				baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, actionId);
			}

			baseWrapper.putObject("changemobileListViewModel",
					changemobileListViewModel);
			baseWrapper = changeMobileManager.updateChangeMobile(baseWrapper);

			if (null != baseWrapper.getBasePersistableModel()) {

				this.saveMessage(httpServletRequest,
						"Branchless Banking user information is updated.");
				ModelAndView modelAndView = new ModelAndView(this
						.getSuccessView());
				return modelAndView;

			}

			else {
				this.saveMessage(httpServletRequest,
						"Record not saved.");
				return super.showForm(httpServletRequest, httpServletResponse,
						bindException);
			}

		} catch (FrameworkCheckedException ex) {

			if("MobileNumberUniqueException".equals(ex.getMessage())){
				super.saveMessage(httpServletRequest, super.getText("newMfsAccount.mobileNumNotUnique", httpServletRequest.getLocale() ));
				return super.showForm(httpServletRequest, httpServletResponse,
						bindException);
			}
		 else if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION == ex
					.getErrorCode()) {
				ex.printStackTrace();
				super.saveMessage(httpServletRequest, ex.getMessage());
				return super.showForm(httpServletRequest, httpServletResponse,
						bindException);
			}

			throw ex;
		}
		catch (Exception e) {
			this.saveMessage(httpServletRequest, MessageUtil.getMessage("6075")); 
			return super.showForm(httpServletRequest, httpServletResponse, bindException);
		}

	}

	protected Object loadFormBackingObject(HttpServletRequest httpServletRequest)
			throws Exception {
		System.out.println("inside the loadFormBackingObject method");
		String enAppUserId = ServletRequestUtils.getStringParameter(httpServletRequest, "appUserId");
		Long id = null;

		if (enAppUserId != null && enAppUserId.trim().length() > 0 ) {
			if (log.isDebugEnabled()) {
				log.debug("id is not null....retrieving object from DB");
			}
            httpServletRequest.setAttribute("searchMfsId", ServletRequestUtils.getStringParameter(httpServletRequest,"searchMfsId"));
            httpServletRequest.setAttribute("searchMobileNo", ServletRequestUtils.getStringParameter(httpServletRequest,"searchMobileNo"));
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			ChangemobileListViewModel changemobileListViewModel = new ChangemobileListViewModel();
			changemobileListViewModel.setPrimaryKey(new Long(EncryptionUtil.decryptWithDES(enAppUserId)));
			searchBaseWrapper
					.setBasePersistableModel(changemobileListViewModel);
			searchBaseWrapper = this.changeMobileManager
					.loadChangeMobile(searchBaseWrapper);
			return (ChangemobileListViewModel) searchBaseWrapper
					.getBasePersistableModel();
		} else {
			if (log.isDebugEnabled()) {
				log.debug("id is null....creating new instance of Model");
			}

			return new ChangemobileListViewModel();
		}
	}

	public void setChangeMobileManager(ChangeMobileManager changeMobileManager) {
		this.changeMobileManager = changeMobileManager;
	}

	public void setReferenceDataManager(
			ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

}
