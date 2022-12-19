package com.inov8.microbank.webapp.action.distributormodule;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.ExceptionErrorCodes;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.AreaModel;
import com.inov8.microbank.common.model.DistributorContactModel;
import com.inov8.microbank.common.model.DistributorLevelModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.MobileTypeModel;
import com.inov8.microbank.common.model.PartnerGroupModel;
import com.inov8.microbank.common.model.PartnerModel;
import com.inov8.microbank.common.model.distributormodule.DistributorContactFormModel;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.server.service.distributormodule.DistributorContactManager;
import com.inov8.microbank.server.service.distributormodule.DistributorLevelManager;
import com.inov8.microbank.server.service.portal.partnergroupmodule.PartnerGroupManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;

/**
 * <p>
 * Title: Microbank
 * </p>
 * 
 * <p>
 * Description: Backend Application for POS terminals.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: Inov8 Limited
 * </p>
 * 
 * @author Asad Hayat
 * @version 1.0
 */

public class DistributorContactFormController extends AdvanceFormController {
	private DistributorContactManager distributorContactManager;
	
	private DistributorLevelManager distributorLevelManager;

	private ReferenceDataManager referenceDataManager;
	
	private PartnerGroupManager partnerGroupManager;

	private AppUserManager appUserManager;

	private Long id = null;

	public DistributorContactFormController() {
		setCommandName("distributorContactModel");
		setCommandClass(DistributorContactFormModel.class);
	}

	@Override
	public Map loadReferenceData(HttpServletRequest httpServletRequest)
			throws FrameworkCheckedException {
		if (log.isDebugEnabled()) {
			log.debug("Inside reference data");
		}

		ReferenceDataWrapper referenceDataWrapper;
		Map referenceDataMap = new HashMap();

		/**
		 * code fragment to load reference data for MobileTypeModel
		 */

		MobileTypeModel mobileTypeModel = new MobileTypeModel();

		referenceDataWrapper = new ReferenceDataWrapperImpl(mobileTypeModel,
				"name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(mobileTypeModel);

		try {
			referenceDataManager.getReferenceData(referenceDataWrapper);
		} catch (FrameworkCheckedException ex) {
		}

		List<MobileTypeModel> mobileTypeModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null) {
			mobileTypeModelList = referenceDataWrapper.getReferenceDataList();
		}
		referenceDataMap.put("mobileTypeModelList", mobileTypeModelList);

		AreaModel areaModel = new AreaModel();
		referenceDataWrapper = new ReferenceDataWrapperImpl(areaModel, "name",
				SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(areaModel);

		referenceDataManager.getReferenceData(referenceDataWrapper);
		List<AreaModel> areaModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null) {
			areaModelList = referenceDataWrapper.getReferenceDataList();
		}
		referenceDataMap.put("areaModelList", areaModelList);
		List<DistributorModel> distributorModelList = null;
		if (id == null) {
			DistributorModel distributorModel = new DistributorModel();
			if (httpServletRequest.getParameter("areaId") != null) {

				distributorModel.setAreaId(Long.getLong(httpServletRequest
						.getParameter("areaId")));
			} else {
				if (areaModelList.size()>0)
				{
				distributorModel.setAreaId(((AreaModel) areaModelList.get(0))
						.getAreaId());
				}
			}
			distributorModel.setActive(true);
			referenceDataWrapper = new ReferenceDataWrapperImpl(
					distributorModel, "name", SortingOrder.ASC);
			referenceDataWrapper.setBasePersistableModel(distributorModel);

			referenceDataManager.getReferenceData(referenceDataWrapper);
			
			if (referenceDataWrapper.getReferenceDataList() != null) {
				distributorModelList = referenceDataWrapper
						.getReferenceDataList();
			}
			if (distributorModelList!=null)
			{	
			referenceDataMap.put("distributorModelList", distributorModelList);
			}

			
			String distributorLevelHQL = " select distributorLevelId,distributorLevelName from DistributorLevelListViewModel dl "
				+ "where dl.distributorId = ? "
				+ " order by distributorLevelName " ;
			List<DistributorLevelModel> distributorLevelModelList = null;
			if (httpServletRequest.getParameter("distributorId") != null) {
			
				Long dist= 0L ;
				try {
					dist = ServletRequestUtils.getLongParameter(httpServletRequest,"distributorId");
				} catch (ServletRequestBindingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				distributorLevelModelList=distributorLevelManager.searchDistributorLevelModels(dist,distributorLevelHQL);
				referenceDataMap.put("distributorLevelModelList",
						distributorLevelModelList);
			} else {
				if (!distributorModelList.isEmpty()
						&& distributorModelList != null) {
					
			
					distributorLevelModelList=distributorLevelManager.searchDistributorLevelModels(((DistributorModel) distributorModelList.get(0)).getDistributorId(),distributorLevelHQL);
					referenceDataMap.put("distributorLevelModelList",
							distributorLevelModelList);
				}
			}
		
		}	

			PartnerModel partnerModel = new PartnerModel();
			partnerModel.setAppUserTypeId(UserTypeConstantsInterface.DISTRIBUTOR);
			if(null!=id)
			{
				DistributorContactModel distributorContactModel = new DistributorContactModel();
				SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
				distributorContactModel.setDistributorContactId(id);
				searchBaseWrapper.setBasePersistableModel(distributorContactModel);
				distributorContactModel = (DistributorContactModel) this.distributorContactManager
						.loadDistributorContact(searchBaseWrapper)
						.getBasePersistableModel();
				
				if(distributorContactModel!=null)
				{	
				partnerModel.setDistributerId(distributorContactModel.getDistributorId());
				}
			}
			else
			{
			if(distributorModelList!=null && distributorModelList.size()>0)
			{
				
				
				Long distributorId = (Long)httpServletRequest.getAttribute("distributorId");
				
				if(null!=distributorId)
				{
					
					partnerModel.setDistributerId(distributorId);
				}
				else
				{
					partnerModel.setDistributerId(distributorModelList.get(0).getDistributorId());
				}
				
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
			referenceDataMap.put("partnerGroupModelList", partnerGroupModelList);

		

		return referenceDataMap;
	}

	@Override
	public Object loadFormBackingObject(HttpServletRequest httpServletRequest)
			throws Exception {
		id = ServletRequestUtils.getLongParameter(httpServletRequest,
				"distributorContactId");
		if (null != id) {
			if (log.isDebugEnabled()) {
				log.debug("id is not null....retrieving object from DB");
			}

			DistributorContactFormModel distributorContactFormModel = new DistributorContactFormModel();
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			DistributorContactModel distributorContactModel = new DistributorContactModel();

			distributorContactModel.setDistributorContactId(id);
			searchBaseWrapper.setBasePersistableModel(distributorContactModel);
			distributorContactModel = (DistributorContactModel) this.distributorContactManager
					.loadDistributorContact(searchBaseWrapper)
					.getBasePersistableModel();
			Double balance = distributorContactModel.getBalance();
			if(balance ==0)
			{
				balance=0.0;
				
			}
			DecimalFormat myFormatter = new DecimalFormat("###.##");
			String bal = myFormatter.format(balance);
			httpServletRequest.setAttribute("balance", bal);
			distributorContactModel.setBalance(Double.valueOf(bal));
			AppUserModel appUserModel = new AppUserModel();
			appUserModel.setAppUserId(ServletRequestUtils.getLongParameter(
					httpServletRequest, "appUserId"));
			baseWrapper.setBasePersistableModel(appUserModel);
			appUserModel = (AppUserModel) this.appUserManager.loadAppUser(
					baseWrapper).getBasePersistableModel();

			BeanUtils.copyProperties(distributorContactFormModel, appUserModel);
			BeanUtils.copyProperties(distributorContactFormModel,
					distributorContactModel);

			distributorContactFormModel.setAreaName(distributorContactModel
					.getAreaIdAreaModel().getName());
			distributorContactFormModel
					.setDistributorName(distributorContactModel
							.getDistributorIdDistributorModel().getName());
			distributorContactFormModel.setLevelName(distributorContactModel
					.getDistributorLevelIdDistributorLevelModel().getName());

			if (distributorContactModel.getManagingContactId() != null) {
				AppUserModel tempAppUserModel = new AppUserModel();
				tempAppUserModel
						.setDistributorContactId(distributorContactModel
								.getManagingContactId());
				searchBaseWrapper.setBasePersistableModel(tempAppUserModel);
				tempAppUserModel = (AppUserModel) this.appUserManager
						.searchAppUser(searchBaseWrapper)
						.getBasePersistableModel();

				distributorContactFormModel
						.setManagingContactName(tempAppUserModel.getFirstName()
								+ " " + tempAppUserModel.getLastName());
			}

			distributorContactFormModel.setMobileTypeId(String
					.valueOf(appUserModel.getMobileTypeId()));
			distributorContactFormModel
					.setDistributorContactModelVersionNo(distributorContactModel
							.getVersionNo());
			distributorContactFormModel.setAppUserModelVersionNo(appUserModel
					.getVersionNo());

			// ******************** Load Reference data
			// ***********************************************
			ReferenceDataWrapper referenceDataWrapper;

			DistributorModel distributorModel = new DistributorModel();
			distributorModel.setAreaId(distributorContactModel.getAreaId());
			referenceDataWrapper = new ReferenceDataWrapperImpl(
					distributorModel, "name", SortingOrder.ASC);
			referenceDataWrapper.setBasePersistableModel(distributorModel);

			referenceDataManager.getReferenceData(referenceDataWrapper);
			List<DistributorModel> distributorModelList = null;
			if (referenceDataWrapper.getReferenceDataList() != null) {
				distributorModelList = referenceDataWrapper
						.getReferenceDataList();
			}
			httpServletRequest.setAttribute("distributorModelList",
					distributorModelList);

			DistributorLevelModel distributorLevelModel = new DistributorLevelModel();
			// commented by Rashid Starts
			/*distributorLevelModel.setDistributorId(distributorContactModel.getDistributorId());*/
			// commented by Rashid Ends
			referenceDataWrapper = new ReferenceDataWrapperImpl(
					distributorLevelModel, "name", SortingOrder.ASC);
			referenceDataWrapper.setBasePersistableModel(distributorLevelModel);

			referenceDataManager.getReferenceData(referenceDataWrapper);
			List<DistributorLevelModel> distributorLevelModelList = null;
			if (referenceDataWrapper.getReferenceDataList() != null) {
				distributorLevelModelList = referenceDataWrapper
						.getReferenceDataList();
			}
			httpServletRequest.setAttribute("distributorLevelModelList",
					distributorLevelModelList);
			httpServletRequest.setAttribute("distributor_Id",
					distributorContactModel.getDistributorId());
			// =========================================================================================
			distributorContactFormModel.setPartnerGroupId(this.distributorContactManager.getAppUserPartnerGroupId(appUserModel.getAppUserId()));
			return distributorContactFormModel;
		} else {
			if (log.isDebugEnabled()) {
				log.debug("id is null....creating new instance of Model");
			}
			return new DistributorContactFormModel();
		}
	}

	@Override
	public ModelAndView onCreate(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object object,
			BindException bindException) throws Exception {

		return this.createOrUpdate(httpServletRequest, httpServletResponse,
				object, bindException);
	}

	@Override
	public ModelAndView onUpdate(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object object,
			BindException bindException) throws Exception {
		return this.createOrUpdate(httpServletRequest, httpServletResponse,
				object, bindException);
	}

	public ModelAndView createOrUpdate(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		String distribId = (String)request.getParameter("distributorContactId");
		if ("".equals(distribId)){
			id = null;		
		}else{
			id = (Long.valueOf(distribId));
		}
		
		DistributorContactFormModel distributorContactFormModel = (DistributorContactFormModel) command;
		try {
			BaseWrapper baseWrapper = new BaseWrapperImpl();

		if (null==distributorContactFormModel.getDistributorLevelId())
		{
			distributorContactFormModel.setDistributorLevelId(request.getParameter("distributorLevel"));
		}
		
			
			
			if (null != id) {
				AppUserModel appUser = populateAppUserModel(distributorContactFormModel);
				
				DistributorContactModel distributorContactModel = populateDistributorContactModel(distributorContactFormModel);

				appUser.setVersionNo(distributorContactFormModel
						.getAppUserModelVersionNo());
				appUser.setAppUserId(Long.parseLong(distributorContactFormModel
						.getAppUserId()));
				distributorContactModel
						.setVersionNo(distributorContactFormModel
								.getDistributorContactModelVersionNo());
				distributorContactModel
						.setDistributorContactId(distributorContactFormModel
								.getDistributorContactId());

				baseWrapper.putObject("appUserModel", appUser);
				baseWrapper.putObject("distributorContactModel",
						distributorContactModel);
				baseWrapper.putObject("distributorContactFormModel",
						distributorContactFormModel);

				baseWrapper = this.distributorContactManager
						.updateDistributorContact(baseWrapper);
				distributorContactModel=(DistributorContactModel)baseWrapper.getBasePersistableModel();
				Double balance = distributorContactModel.getBalance();
				DecimalFormat myFormatter = new DecimalFormat("###.##");
				String bal = myFormatter.format(balance);
				request.setAttribute("balance", bal);
				
			} else {
				baseWrapper.putObject("appUserModel",
						populateAppUserModel(distributorContactFormModel));
				baseWrapper
						.putObject(
								"distributorContactModel",
								populateDistributorContactModel(distributorContactFormModel));
				
				baseWrapper.putObject("distributorContactFormModel",
						distributorContactFormModel);

				baseWrapper = this.distributorContactManager
						.createDistributorContact(baseWrapper);
				
			}

			this.saveMessage(request, "Record saved successfully");
			ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
			request.setAttribute("baseWrapper", baseWrapper);
			
			return modelAndView;

		} catch (FrameworkCheckedException ex) {
			
			int checkCode=1;			
			request.setAttribute("errorCode",checkCode);
			request.setAttribute("exceptionMessage", ex.getMessage());
			request.setAttribute("bankId", distributorContactFormModel.getDistributorId());
			
			Double balance = Double.valueOf(distributorContactFormModel
					.getBalance());
			DecimalFormat myFormatter = new DecimalFormat("###.##");
			String bal = myFormatter.format(balance);
			request.setAttribute("balance", bal);
			distributorContactFormModel.setBalance(bal);
			AreaModel areaModel = new AreaModel();
			ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
					areaModel, "name", SortingOrder.ASC);
			referenceDataWrapper.setBasePersistableModel(areaModel);

			referenceDataManager.getReferenceData(referenceDataWrapper);
			List<AreaModel> areaModelList = null;
			if (referenceDataWrapper.getReferenceDataList() != null) {
				areaModelList = referenceDataWrapper.getReferenceDataList();
			}
			request.setAttribute("areaModelList", areaModelList);

			if (request.getParameter("areaId") != null) {

				DistributorModel distributorModel = new DistributorModel();
				distributorModel.setAreaId(Long
						.valueOf(distributorContactFormModel.getAreaId()));
				distributorModel.setActive(true);
				referenceDataWrapper = new ReferenceDataWrapperImpl(
						distributorModel, "name", SortingOrder.ASC);
				referenceDataWrapper.setBasePersistableModel(distributorModel);

				referenceDataManager.getReferenceData(referenceDataWrapper);
				List<DistributorModel> distributorModelList = null;
				if (referenceDataWrapper.getReferenceDataList() != null) {
					distributorModelList = referenceDataWrapper
							.getReferenceDataList();
				}
				request.setAttribute("distributorModelList",
						distributorModelList);

			}
			if (request.getParameter("distributorId") != null) {

				DistributorLevelModel distributorLevelModel = new DistributorLevelModel();
				// commented by Rashid Starts
				/*distributorLevelModel.setDistributorId(Long.getLong(request.getParameter("distributorId")));*/
				// commented by Rashid Ends
				referenceDataWrapper = new ReferenceDataWrapperImpl(
						distributorLevelModel, "name", SortingOrder.ASC);
				referenceDataWrapper
						.setBasePersistableModel(distributorLevelModel);

				referenceDataManager.getReferenceData(referenceDataWrapper);
				List<DistributorLevelModel> distributorLevelModelList = null;
				if (referenceDataWrapper.getReferenceDataList() != null) {
					distributorLevelModelList = referenceDataWrapper
							.getReferenceDataList();
				}
				request.setAttribute("distributorLevelModelList",
						distributorLevelModelList);

			}

			if (ex.getMessage().equals("MobileNumUniqueException")) {
				super.saveMessage(request, "Mobile Number already exist.");
				return super.showForm(request, response, errors);
			} else if (ex.getMessage().equals("UsernameUniqueException")) {
				super.saveMessage(request, "Username already exist.");
				return super.showForm(request, response, errors);
			} else if (ex.getMessage().equalsIgnoreCase("HeadUniqueException")) {
				super.saveMessage(request,
						"Two distributor heads  are not allowed.");
				return super.showForm(request, response, errors);
			}
			
				else if (ex.getMessage().equalsIgnoreCase("HeadLevelUniqueException")) {
					super.saveMessage(request,
							"Distributor head can only be defined on the top level in heirarchy.");
					return super.showForm(request, response, errors);
					
					
					
			}
				else if("MobileNumUniqueException".equals(ex.getMessage()))
				{
					this.saveMessage(request, super.getText("newMfsAccount.mobileNumNotUnique", request.getLocale() ));
					return super.showForm(request, response, errors);
				}
				
				else if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION == ex
					.getErrorCode()) {
				super.saveMessage(request, "Record could not be saved.");
				return super.showForm(request, response, errors);
			}
			throw ex;
		}
		catch (Exception ex) {
			
			int checkCode=1;			
			request.setAttribute("errorCode",checkCode);
			request.setAttribute("exceptionMessage", MessageUtil.getMessage("6075"));
			request.setAttribute("bankId", distributorContactFormModel.getDistributorId());
			
			Double balance = Double.valueOf(distributorContactFormModel
					.getBalance());
			DecimalFormat myFormatter = new DecimalFormat("###.##");
			String bal = myFormatter.format(balance);
			request.setAttribute("balance", bal);
			distributorContactFormModel.setBalance(bal);
			AreaModel areaModel = new AreaModel();
			ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
					areaModel, "name", SortingOrder.ASC);
			referenceDataWrapper.setBasePersistableModel(areaModel);

			referenceDataManager.getReferenceData(referenceDataWrapper);
			List<AreaModel> areaModelList = null;
			if (referenceDataWrapper.getReferenceDataList() != null) {
				areaModelList = referenceDataWrapper.getReferenceDataList();
			}
			request.setAttribute("areaModelList", areaModelList);

			if (request.getParameter("areaId") != null) {

				DistributorModel distributorModel = new DistributorModel();
				distributorModel.setAreaId(Long
						.valueOf(distributorContactFormModel.getAreaId()));
				distributorModel.setActive(true);
				referenceDataWrapper = new ReferenceDataWrapperImpl(
						distributorModel, "name", SortingOrder.ASC);
				referenceDataWrapper.setBasePersistableModel(distributorModel);

				referenceDataManager.getReferenceData(referenceDataWrapper);
				List<DistributorModel> distributorModelList = null;
				if (referenceDataWrapper.getReferenceDataList() != null) {
					distributorModelList = referenceDataWrapper
							.getReferenceDataList();
				}
				request.setAttribute("distributorModelList",
						distributorModelList);

			}
			if (request.getParameter("distributorId") != null) {

				DistributorLevelModel distributorLevelModel = new DistributorLevelModel();
				// commented by Rashid Starts
				/*distributorLevelModel.setDistributorId(Long.getLong(request.getParameter("distributorId")));*/
				// commented by Rashid Ends
				referenceDataWrapper = new ReferenceDataWrapperImpl(
						distributorLevelModel, "name", SortingOrder.ASC);
				referenceDataWrapper
						.setBasePersistableModel(distributorLevelModel);

				referenceDataManager.getReferenceData(referenceDataWrapper);
				List<DistributorLevelModel> distributorLevelModelList = null;
				if (referenceDataWrapper.getReferenceDataList() != null) {
					distributorLevelModelList = referenceDataWrapper
							.getReferenceDataList();
				}
				request.setAttribute("distributorLevelModelList",
						distributorLevelModelList);

			}

			
			super.saveMessage(request, MessageUtil.getMessage("6075"));
			return super.showForm(request, response, errors);

		}
		
	}

	public AppUserModel populateAppUserModel(
			DistributorContactFormModel distributorContactFormModel) {
		AppUserModel appUserModel = new AppUserModel();

		appUserModel.setFirstName(distributorContactFormModel.getFirstName());
		appUserModel.setLastName(distributorContactFormModel.getLastName());
		appUserModel.setMobileNo(distributorContactFormModel.getMobileNo());

		appUserModel.setAddress1(distributorContactFormModel.getAddress1());
		appUserModel.setAddress2(distributorContactFormModel.getAddress2());

		appUserModel.setUsername(distributorContactFormModel.getUsername());
		if (distributorContactFormModel.getPassword() != null)
			appUserModel.setPassword(EncoderUtils
					.encodeToSha(distributorContactFormModel.getPassword()));
		appUserModel.setCity(distributorContactFormModel.getCity());
		appUserModel.setState(distributorContactFormModel.getState());
		appUserModel.setCountry(distributorContactFormModel.getCountry());
		appUserModel.setZip(distributorContactFormModel.getZip());
		appUserModel.setEmail(distributorContactFormModel.getEmail());
		appUserModel.setFax(distributorContactFormModel.getFax());
		appUserModel.setMobileTypeId(Long.parseLong(distributorContactFormModel
				.getMobileTypeId()));
		appUserModel.setNic(distributorContactFormModel.getNic());
		appUserModel.setPasswordHint(distributorContactFormModel
				.getPasswordHint());
		appUserModel.setMotherMaidenName(distributorContactFormModel
				.getMotherMaidenName());

		appUserModel.setAccountEnabled(new Boolean(distributorContactFormModel
				.getAccountEnabled()));
		appUserModel.setAccountExpired(new Boolean(distributorContactFormModel
				.getAccountExpired()));
		appUserModel.setAccountLocked(new Boolean(distributorContactFormModel
				.getAccountLocked()));
		appUserModel.setVerified(new Boolean(distributorContactFormModel
				.getVerified()));
		appUserModel.setCredentialsExpired(new Boolean(
				distributorContactFormModel.getCredentialsExpired()));

		appUserModel.setDob(distributorContactFormModel.getDob());

		return appUserModel;
	}

	public DistributorContactModel populateDistributorContactModel(
			DistributorContactFormModel distributorContactFormModel) {
		DistributorContactModel distributorContactModel = new DistributorContactModel();

		distributorContactModel.setDistributorId(Long
				.parseLong(distributorContactFormModel.getDistributorId()));

		if (distributorContactFormModel.getManagingContactId() != null)
			distributorContactModel.setManagingContactId(Long
					.parseLong(distributorContactFormModel
							.getManagingContactId()));

		
		distributorContactModel
				.setDistributorLevelId(Long
						.parseLong(distributorContactFormModel
								.getDistributorLevelId()));
		
		
		distributorContactModel.setAreaId(Long
				.parseLong(distributorContactFormModel.getAreaId()));

		distributorContactModel.setBalance(Double
				.parseDouble(distributorContactFormModel.getBalance()));
		distributorContactModel.setHead(new Boolean(distributorContactFormModel
				.getHead()));
		distributorContactModel.setActive(true);
		/*distributorContactModel.setActive(distributorContactFormModel
				.getAccountEnabled() == null ? false
				: new Boolean(distributorContactFormModel.getActive()));*/
		/*distributorContactModel.setActive(new Boolean(
				distributorContactFormModel.getActive()));*/

		distributorContactModel.setDescription(distributorContactFormModel
				.getDescription());
		distributorContactModel.setComments(distributorContactFormModel
				.getComments());

		return distributorContactModel;
	}

	public void setDistributorContactManager(
			DistributorContactManager distributorContactManager) {
		this.distributorContactManager = distributorContactManager;
	}

	public void setReferenceDataManager(
			ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}

	public void setDistributorLevelManager(
			DistributorLevelManager distributorLevelManager) {
		this.distributorLevelManager = distributorLevelManager;
	}

	public void setPartnerGroupManager(PartnerGroupManager partnerGroupManager) {
		this.partnerGroupManager = partnerGroupManager;
	}

}
