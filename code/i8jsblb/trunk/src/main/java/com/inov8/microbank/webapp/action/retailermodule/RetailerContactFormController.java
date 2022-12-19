package com.inov8.microbank.webapp.action.retailermodule;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
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
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.AreaModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.MobileTypeModel;
import com.inov8.microbank.common.model.PartnerGroupModel;
import com.inov8.microbank.common.model.PartnerModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.RetailerModel;
import com.inov8.microbank.common.model.retailermodule.RetailerContactListViewFormModel;
import com.inov8.microbank.common.model.retailermodule.RetailerContactListViewModel;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.server.service.portal.partnergroupmodule.PartnerGroupManager;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;

/**
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backend Application for POS terminals.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Asad Hayat
 * @version 1.0
 */

public class RetailerContactFormController extends AdvanceFormController
{
	private RetailerContactManager retailerContactManager;

	private ReferenceDataManager referenceDataManager;
	
	private PartnerGroupManager partnerGroupManager;

	private AppUserManager appUserManager;

	private Long id;

	public RetailerContactFormController()
	{
		setCommandName("retailerContactListViewFormModel");
		setCommandClass(RetailerContactListViewFormModel.class);
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws FrameworkCheckedException
	{
		if (log.isDebugEnabled())
		{
			log.debug("Inside reference data");
		}

		/**
		 * code fragment to load reference data  for Distributor
		 *
		 */

		DistributorModel distributorModel = new DistributorModel();
		List<RetailerModel> retailerModelList = null;
		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(distributorModel, "name",
				SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(distributorModel);

		referenceDataManager.getReferenceData(referenceDataWrapper);

		List<DistributorModel> distributorModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null)
		{
			distributorModelList = referenceDataWrapper.getReferenceDataList();
		}

		Map referenceDataMap = new HashMap();
		referenceDataMap.put("distributorModelList", distributorModelList);


		/**
		 * code fragment to load reference data for Area
		 */

		AreaModel areaModel = new AreaModel();
		referenceDataWrapper = new ReferenceDataWrapperImpl(areaModel, "name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(areaModel);

		referenceDataManager.getReferenceData(referenceDataWrapper);
		List<AreaModel> areaModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null)
		{
			areaModelList = referenceDataWrapper.getReferenceDataList();
		}

		referenceDataMap.put("areaModelList", areaModelList);

		MobileTypeModel mobileTypeModel = new MobileTypeModel();

		//paymentModeModel.setActive(true);
		referenceDataWrapper = new ReferenceDataWrapperImpl(mobileTypeModel, "name", SortingOrder.ASC);

		try
		{
			referenceDataManager.getReferenceData(referenceDataWrapper);
		}
		catch (FrameworkCheckedException ex1)
		{
			ex1.getStackTrace();
		}
		List<MobileTypeModel> mobileTypeModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null)
		{
			mobileTypeModelList = referenceDataWrapper.getReferenceDataList();
		}

		referenceDataMap.put("mobileTypeModelList", mobileTypeModelList);

		
		/**
		 * code fragment to load reference data for Retailer
		 */
		
		if (null == id)
		{
		
		RetailerModel retailerModel = new RetailerModel();
/*
		try
		{
			Long areaIdRef = ServletRequestUtils.getLongParameter(httpServletRequest, "areaId");

			if (areaIdRef == null)
			{
				areaIdRef = (Long) httpServletRequest.getAttribute("areaIdRef");
				retailerModel.setAreaId(areaIdRef);
			}
			else
			{
				retailerModel.setAreaId(areaIdRef);
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
*/
		
		if (httpServletRequest.getParameter("areaId")!=null)
		{
			
			
			retailerModel.setAreaId(Long.getLong(httpServletRequest.getParameter("areaId")));
		}
		else
		{
			if (areaModelList.size()>0)
			{
		retailerModel.setAreaId( ((AreaModel)areaModelList.get(0)).getAreaId() ) ;
			}
		}
		retailerModel.setActive(true);
		referenceDataWrapper = new ReferenceDataWrapperImpl(retailerModel, "name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(retailerModel);

		referenceDataManager.getReferenceData(referenceDataWrapper);
		
		if (referenceDataWrapper.getReferenceDataList() != null)
		{
			retailerModelList = referenceDataWrapper.getReferenceDataList();
		}
		referenceDataMap.put("retailerModelList", retailerModelList);
		}
		
		
		
		
		PartnerModel partnerModel = new PartnerModel();
		partnerModel.setAppUserTypeId(UserTypeConstantsInterface.RETAILER);
		if(retailerModelList!=null && retailerModelList.size()>0)
		{
			
			
			Long retailerId = (Long)httpServletRequest.getAttribute("retailerId");
			
			if(null!=retailerId)
			{
				
				partnerModel.setRetailerId(retailerId);
			}
			else
			{
				partnerModel.setRetailerId(retailerModelList.get(0).getRetailerId());	
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
	protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws Exception
	{
		id = ServletRequestUtils.getLongParameter(httpServletRequest, "retailerContactId");
		if (null != id)
		{
			if (log.isDebugEnabled())
			{
				log.debug("id is not null....retrieving object from DB");
			}

			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			RetailerContactListViewFormModel retailerContactListViewFormModel = new RetailerContactListViewFormModel();
			RetailerContactListViewModel retailerContactListViewModel = new RetailerContactListViewModel();
			RetailerContactModel retailerContactModel = new RetailerContactModel();
			retailerContactListViewModel.setRetailerContactId(id);
			retailerContactModel.setRetailerContactId(id);
			searchBaseWrapper.setBasePersistableModel(retailerContactListViewModel);
			searchBaseWrapper = this.retailerContactManager.loadRetailerContactListView(searchBaseWrapper);
			retailerContactListViewModel = (RetailerContactListViewModel) searchBaseWrapper
					.getBasePersistableModel();
			// setting the area id //
			Double balance =retailerContactListViewModel.getBalance();
			DecimalFormat myFormatter = new DecimalFormat("###.##");
			String bal =myFormatter.format(balance);
			httpServletRequest.setAttribute("balance", bal);
			retailerContactListViewModel.setBalance(Double.valueOf(bal));
			httpServletRequest.setAttribute("areaIdRef", retailerContactListViewModel.getAreaId());
			searchBaseWrapper = this.retailerContactManager.loadRetailerContact(searchBaseWrapper);

			retailerContactModel = (RetailerContactModel) searchBaseWrapper.getBasePersistableModel();
			AppUserModel appUserModel = new AppUserModel();
			appUserModel = appUserManager
					.getUser(String.valueOf(retailerContactListViewModel.getAppUserId()));
			
			
			
			RetailerModel retailerModel = new RetailerModel();
			retailerModel.setAreaId( retailerContactListViewModel.getAreaId() ) ;			 
			 ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(retailerModel, "name", SortingOrder.ASC);
			referenceDataWrapper.setBasePersistableModel(retailerModel);
			
			referenceDataManager.getReferenceData(referenceDataWrapper);
			List<RetailerModel> retailerModelList = null;
			if (referenceDataWrapper.getReferenceDataList() != null)
			{
				retailerModelList = referenceDataWrapper.getReferenceDataList();
			}
			httpServletRequest.setAttribute("retailerModelList", retailerModelList);
			
			BeanUtils.copyProperties(retailerContactListViewFormModel, retailerContactListViewModel);
			BeanUtils.copyProperties(retailerContactListViewFormModel, appUserModel);

			retailerContactListViewFormModel.setHead(retailerContactModel.getHead());
			retailerContactListViewFormModel.setComments( retailerContactModel.getComments() ) ;
			retailerContactListViewFormModel.setDob( appUserModel.getDob() ) ;			

			retailerContactListViewFormModel.setBalance(retailerContactModel.getBalance());
			retailerContactListViewFormModel.setPartnerGroupId(this.retailerContactManager.getAppUserPartnerGroupId(appUserModel.getAppUserId()));
			return retailerContactListViewFormModel;
		}
		else
		{
			if (log.isDebugEnabled())
			{
				log.debug("id is null....creating new instance of Model");
			}

			return new RetailerContactListViewFormModel();
		}

	}

	@Override
	public ModelAndView onCreate(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object object, BindException bindException)
			throws Exception
	{
		return this.createOrUpdate(httpServletRequest, httpServletResponse, object, bindException);
	}

	@Override
	public ModelAndView onUpdate(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object object, BindException bindException)
			throws Exception
	{
		return this.createOrUpdate(httpServletRequest, httpServletResponse, object, bindException);

	}

	/**
	 *
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param command Object
	 * @param errors BindException
	 * @return ModelAndView
	 * @throws Exception
	 */

	private ModelAndView createOrUpdate(HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception
	{
		String retailerId = (String)request.getParameter("retailerContactId");
		if ("".equals(retailerId)){
			id = null;		
		}else{
			id = (Long.valueOf(retailerId));
		}
		RetailerContactListViewFormModel retailerContactListViewFormModel = (RetailerContactListViewFormModel) command;
		try
		{
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			long useCaseId = 0;
			long actionId = 0;
			
			if (request.getParameter(PortalConstants.KEY_USECASE_ID) != null)
			{
				useCaseId = Long.parseLong(request.getParameter(PortalConstants.KEY_USECASE_ID));
				System.out.println("usecase id " + useCaseId);
				baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, useCaseId);

			}
			if (request.getParameter(PortalConstants.KEY_ACTION_ID) != null)
			{
				actionId = Long.parseLong(request.getParameter(PortalConstants.KEY_ACTION_ID));
				System.out.println("action id " + actionId);
				baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, actionId);
			}


			if (null != id)
			{
				baseWrapper.putObject("retailerContactListViewFormModel", retailerContactListViewFormModel);

				baseWrapper = this.retailerContactManager.updateRetailerContact(baseWrapper);
			}
			else
			{
				baseWrapper.putObject("retailerContactListViewFormModel", retailerContactListViewFormModel);
				baseWrapper = this.retailerContactManager.createRetailerContact(baseWrapper);
			}
			int checkCode=0;			
			request.setAttribute("errorCode",checkCode);
			
			this.saveMessage(request, "Record saved successfully");
			ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
			request.setAttribute("baseWrapper", baseWrapper);
			return modelAndView;

		}
		catch (FrameworkCheckedException ex)
		{
			int checkCode=1;	
			request.setAttribute("exceptionMessage", ex.getMessage());
			request.setAttribute("errorCode",checkCode);
			
			Double balance =retailerContactListViewFormModel.getBalance();
			DecimalFormat myFormatter = new DecimalFormat("###.##");
			String bal =myFormatter.format(balance==null?new Double(0):balance);
			request.setAttribute("balance", bal);
			//request.setAttribute("retailerId", retailerContactListViewFormModel.getRetailerId());
			//request.setAttribute("areaId", retailerContactListViewFormModel.getRetailerId());
			//request.setAttribute("retailerContactListViewFormModel", retailerContactListViewFormModel);
			
			
			AreaModel areaModel = new AreaModel();
			ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(areaModel, "name", SortingOrder.ASC);
			referenceDataWrapper.setBasePersistableModel(areaModel);
			
			try{
				
			referenceDataManager.getReferenceData(referenceDataWrapper);
			List<AreaModel> areaModelList = null;
			if (referenceDataWrapper.getReferenceDataList() != null)
			{
				areaModelList = referenceDataWrapper.getReferenceDataList();
			}

			request.setAttribute("areaModelList", areaModelList);

			if (request.getParameter("areaId")!=null)
			{
				
				RetailerModel retailerModel = new RetailerModel();		
				retailerModel.setAreaId(Long.getLong(request.getParameter("areaId")));
			
		
			retailerModel.setActive(true);
			 referenceDataWrapper = new ReferenceDataWrapperImpl(retailerModel, "name", SortingOrder.ASC);
			referenceDataWrapper.setBasePersistableModel(retailerModel);

			referenceDataManager.getReferenceData(referenceDataWrapper);
			List<RetailerModel> retailerModelList = null;
			if (referenceDataWrapper.getReferenceDataList() != null)
			{
				retailerModelList = referenceDataWrapper.getReferenceDataList();
			}
			request.setAttribute("retailerModelList", retailerModelList);
			}
			
			}catch(Exception exp){
				exp.printStackTrace();
			}
			
			request.setAttribute("retailerId", retailerContactListViewFormModel.getRetailerId());
			if (ex.getMessage().equalsIgnoreCase("MobileNumUniqueException"))
			{
				super.saveMessage(request, "Mobile number already exists.");
				return super.showForm(request, response, errors);
			}
			else if (ex.getMessage().equalsIgnoreCase("UsernameUniqueException"))
			{
				super.saveMessage(request, "Username already exists.");
				return super.showForm(request, response, errors);
			}
			else if (ex.getMessage().equalsIgnoreCase("HeadUniqueException"))
			{
				super.saveMessage(request, "Two retailer heads  are not allowed.");
				return super.showForm(request, response, errors);
			}
			
			else if("MobileNumUniqueException".equals(ex.getMessage()))
			{
				this.saveMessage(request, super.getText("newMfsAccount.mobileNumNotUnique", request.getLocale() ));
				return super.showForm(request, response, errors);
			}
			
			else if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION == ex.getErrorCode())
			{
				super.saveMessage(request, "Record could not be saved.");
				ex.printStackTrace();
				return super.showForm(request, response, errors);
			}
			throw ex;
		}
		catch (Exception ex)
		{
			int checkCode=1;	
			request.setAttribute("exceptionMessage", MessageUtil.getMessage("6075"));
			request.setAttribute("errorCode",checkCode);
			
			Double balance =retailerContactListViewFormModel.getBalance();
			DecimalFormat myFormatter = new DecimalFormat("###.##");
			String bal =myFormatter.format(balance==null?new Double(0):balance);
			request.setAttribute("balance", bal);

			AreaModel areaModel = new AreaModel();
			ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(areaModel, "name", SortingOrder.ASC);
			referenceDataWrapper.setBasePersistableModel(areaModel);
			
			try{
				
			referenceDataManager.getReferenceData(referenceDataWrapper);
			List<AreaModel> areaModelList = null;
			if (referenceDataWrapper.getReferenceDataList() != null)
			{
				areaModelList = referenceDataWrapper.getReferenceDataList();
			}

			request.setAttribute("areaModelList", areaModelList);

			if (request.getParameter("areaId")!=null)
			{
				
				RetailerModel retailerModel = new RetailerModel();		
				retailerModel.setAreaId(Long.getLong(request.getParameter("areaId")));
			
		
			retailerModel.setActive(true);
			 referenceDataWrapper = new ReferenceDataWrapperImpl(retailerModel, "name", SortingOrder.ASC);
			referenceDataWrapper.setBasePersistableModel(retailerModel);

			referenceDataManager.getReferenceData(referenceDataWrapper);
			List<RetailerModel> retailerModelList = null;
			if (referenceDataWrapper.getReferenceDataList() != null)
			{
				retailerModelList = referenceDataWrapper.getReferenceDataList();
			}
			request.setAttribute("retailerModelList", retailerModelList);
			}
			
			}catch(Exception exp){
				exp.printStackTrace();
			}
			
			request.setAttribute("retailerId", retailerContactListViewFormModel.getRetailerId());
			super.saveMessage(request, MessageUtil.getMessage("6075"));
			ex.printStackTrace();
			return super.showForm(request, response, errors);	
		}
		

	}

	public void setRetailerContactManager(RetailerContactManager retailerContactManager)
	{
		this.retailerContactManager = retailerContactManager;
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
	{
		this.referenceDataManager = referenceDataManager;
	}

	public void setAppUserManager(AppUserManager appUserManager)
	{
		this.appUserManager = appUserManager;
	}

	public void setPartnerGroupManager(PartnerGroupManager partnerGroupManager) {
		this.partnerGroupManager = partnerGroupManager;
	}
}
