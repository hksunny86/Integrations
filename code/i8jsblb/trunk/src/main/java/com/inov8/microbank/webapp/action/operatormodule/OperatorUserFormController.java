package com.inov8.microbank.webapp.action.operatormodule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
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
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.MobileTypeModel;
import com.inov8.microbank.common.model.OperatorModel;
import com.inov8.microbank.common.model.PartnerGroupModel;
import com.inov8.microbank.common.model.PartnerModel;
import com.inov8.microbank.common.model.operatormodule.OperatorUserListViewModel;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.server.service.operatormodule.OperatorUserManager;
import com.inov8.microbank.server.service.portal.partnergroupmodule.PartnerGroupManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;

public class OperatorUserFormController extends AdvanceFormController {

	private OperatorUserManager operatorUserManager;

	private ReferenceDataManager referenceDataManager;
	
	private PartnerGroupManager partnerGroupManager;
	
	private AppUserManager appUserManager;
	
	private Long id = null;

	public OperatorUserFormController() {
		setCommandName("operatorUserListViewModel");
		setCommandClass(OperatorUserListViewModel.class);
	}

	public void setOperatorUserManager(OperatorUserManager operatorUserManager) {
		this.operatorUserManager = operatorUserManager;
	}

	@Override
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
	{
		super.initBinder(request, binder);
		CommonUtils.bindCustomDateEditor(binder);
	}
	@Override
	protected Object loadFormBackingObject(HttpServletRequest httpServletRequest)
			throws Exception {
		id = ServletRequestUtils.getLongParameter(httpServletRequest,
				"operatorUserId");
		if (null != id) {
			if (log.isDebugEnabled()) {
				log.debug("id is not null....retrieving object from DB");
			}

			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			OperatorUserListViewModel operatorUserListViewModel = new OperatorUserListViewModel();
			

			operatorUserListViewModel.setOperatorUserId(id);
			searchBaseWrapper.setBasePersistableModel(operatorUserListViewModel);
			searchBaseWrapper = this.operatorUserManager
					.loadOperatorUser(searchBaseWrapper);
			operatorUserListViewModel = (OperatorUserListViewModel) searchBaseWrapper
					.getBasePersistableModel();

			AppUserModel appUserModel = new AppUserModel();
			appUserModel = appUserManager.getUser(String
					.valueOf(operatorUserListViewModel.getAppUserId()));

			
			BeanUtils.copyProperties(operatorUserListViewModel, appUserModel);
			operatorUserListViewModel.setOperatorUserId(appUserModel.getOperatorUserId());	

			System.out.println(operatorUserListViewModel.getAppUserId()
					+ "appuserID");

			operatorUserListViewModel.setPartnerGroupId(this.operatorUserManager.getAppUserPartnerGroupId(appUserModel.getAppUserId()));		
			
			
			return operatorUserListViewModel;
		} else {
			if (log.isDebugEnabled()) {
				log.debug("id is null....creating new instance of Model");
			}
			
			return new OperatorUserListViewModel();
		}

	}

	@Override
	protected Map loadReferenceData(HttpServletRequest request) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Inside reference data");
		}

		OperatorModel operatorModel = new OperatorModel();
		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
				operatorModel, "name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(operatorModel);
		referenceDataManager.getReferenceData(referenceDataWrapper);
		List<OperatorModel> operatorModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null) {
			operatorModelList = referenceDataWrapper.getReferenceDataList();
		}

		Map referenceDataMap = new HashMap();
		referenceDataMap.put("operatorModelList", operatorModelList);

		MobileTypeModel mobileTypeModel = new MobileTypeModel();

		// paymentModeModel.setActive(true);
		referenceDataWrapper = new ReferenceDataWrapperImpl(mobileTypeModel,
				"name", SortingOrder.ASC);

		try {
			referenceDataManager.getReferenceData(referenceDataWrapper);
		} catch (FrameworkCheckedException ex1) {
			ex1.getStackTrace();
		}
		List<MobileTypeModel> mobileTypeModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null) {
			mobileTypeModelList = referenceDataWrapper.getReferenceDataList();
		}

		referenceDataMap.put("mobileTypeModelList", mobileTypeModelList);
		
		
		
		PartnerModel partnerModel = new PartnerModel();
		partnerModel.setAppUserTypeId(UserTypeConstantsInterface.INOV8);
		if(operatorModelList!=null && operatorModelList.size()>0)
		{
		
			
			String opId = request.getParameter("operatorId");
			Long operatorId = null;
			if (opId !=null){
				operatorId = Long.parseLong(opId);
			}
			
			if(null!=operatorId)
			{
				
				partnerModel.setOperatorId(operatorId);
			}
			else
			{
				partnerModel.setOperatorId(operatorModelList.get(0).getOperatorId());	
			}

			
		}
		
		referenceDataWrapper = new ReferenceDataWrapperImpl(
				partnerModel, "name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(partnerModel);
		try {
		referenceDataManager.getReferenceData(referenceDataWrapper);
		} catch (FrameworkCheckedException ex1) {
			ex1.getStackTrace();
		}
		List<PartnerModel> partnerModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null) 
		{
			partnerModelList = referenceDataWrapper.getReferenceDataList();
		}

		
		
		
		
		PartnerGroupModel partnerGroupModel = new PartnerGroupModel();
		List<PartnerGroupModel> partnerGroupModelList = null;
		if(partnerModelList!=null && partnerModelList.size()>0)
		{
			partnerGroupModel.setActive(true);
			partnerGroupModel.setPartnerId(partnerModelList.get(0).getPartnerId());
			partnerGroupModelList =partnerGroupManager.getPartnerGroups(partnerGroupModel,true);
		}
		/*PartnerGroupModel partnerGroupModel = new PartnerGroupModel();
		partnerGroupModel.setActive(true);
		if(partnerModelList!=null && partnerModelList.size()>0)
		{
		partnerGroupModel.setPartnerId(partnerModelList.get(0).getPartnerId());
		}
		
		referenceDataWrapper = new ReferenceDataWrapperImpl(
				partnerGroupModel, "name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(partnerGroupModel);
		try {
		referenceDataManager.getReferenceData(referenceDataWrapper);
		} catch (FrameworkCheckedException ex1) {
			ex1.getStackTrace();
		}
		List<PartnerGroupModel> partnerGroupModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null) 
		{
			partnerGroupModelList = referenceDataWrapper.getReferenceDataList();
		}*/

		
		referenceDataMap.put("partnerGroupModelList", partnerGroupModelList);
		
				
		
		return referenceDataMap;

	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object object, BindException bindException)
			throws Exception {
		return this.createOrUpdate(httpServletRequest, httpServletResponse,
				object, bindException);
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object object, BindException bindException)
			throws Exception {
		return this.createOrUpdate(httpServletRequest, httpServletResponse,
				object, bindException);
	}
	
	private ModelAndView createOrUpdate(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		OperatorUserListViewModel operatorUserListViewModel = (OperatorUserListViewModel) command;
		try {
			BaseWrapper baseWrapper = new BaseWrapperImpl();

		
			

			if (null != id) {

				baseWrapper.setBasePersistableModel(operatorUserListViewModel);

				baseWrapper = this.operatorUserManager.updateOperatorUser(

				baseWrapper);

			} else {

				
				baseWrapper.setBasePersistableModel(operatorUserListViewModel);
				baseWrapper = this.operatorUserManager.createOperatorUser(baseWrapper);

				

			}

			this.saveMessage(request, "Record saved successfully");
			ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
			return modelAndView;

		} catch (FrameworkCheckedException ex) {
			request.setAttribute("operatorId", operatorUserListViewModel.getOperatorId());
			if( ex.getMessage().equalsIgnoreCase("MobileNumUniqueException") )
			{
				super.saveMessage(request, "Mobile number already exists.");
				
				return super.showForm(request, response, errors);				
			}			
			else if( ex.getMessage().equalsIgnoreCase("UsernameUniqueException") )
			{
				super.saveMessage(request, "Username already exists.");
				
				return super.showForm(request, response, errors);				
			}
			else if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION == ex
					.getErrorCode()) {
				super.saveMessage(request, "Record could not be saved.");
				
				return super.showForm(request, response, errors);
			}
			throw ex;
		}catch (Exception ex) {
			request.setAttribute("operatorId", operatorUserListViewModel.getOperatorId());
			super.saveMessage(request,MessageUtil.getMessage("6075"));
			return super.showForm(request, response, errors);
		}

	}

	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public void setPartnerGroupManager(PartnerGroupManager partnerGroupManager) {
		this.partnerGroupManager = partnerGroupManager;
	}


}
