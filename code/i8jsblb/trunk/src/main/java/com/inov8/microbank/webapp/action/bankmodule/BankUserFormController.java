package com.inov8.microbank.webapp.action.bankmodule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.BankUserModel;
import com.inov8.microbank.common.model.MobileTypeModel;
import com.inov8.microbank.common.model.PartnerGroupModel;
import com.inov8.microbank.common.model.PartnerModel;
import com.inov8.microbank.common.model.bankmodule.BankUserListViewFormModel;
import com.inov8.microbank.common.model.bankmodule.BankUserListViewModel;
import com.inov8.microbank.common.util.BankConstantsInterface;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.server.service.bankmodule.BankUserManager;
import com.inov8.microbank.server.service.portal.partnergroupmodule.PartnerGroupManager;
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

public class BankUserFormController extends AdvanceFormController {
	private BankUserManager bankUserManager;

	private ReferenceDataManager referenceDataManager;
	
	private PartnerGroupManager partnerGroupManager;
	private AppUserManager appUserManager;

	private Long id = null;

	public BankUserFormController() {
		setCommandName("bankUserListViewFormModel");
		setCommandClass(BankUserListViewFormModel.class);
	}
	@Override
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
	{
		super.initBinder(request, binder);
		CommonUtils.bindCustomDateEditor(binder);
	}
	@Override
	protected Map loadReferenceData(HttpServletRequest httpServletRequest)
			throws FrameworkCheckedException {

		if (log.isDebugEnabled()) {
			log.debug("Inside reference data");
		}
	
		BankUserListViewModel bankUserListViewModel = new BankUserListViewModel();
		if(null!=id)
		{
			
			SearchBaseWrapper searchBaseWrapper =new SearchBaseWrapperImpl();
			bankUserListViewModel.setBankUserId(id);
			searchBaseWrapper.setBasePersistableModel(bankUserListViewModel);
			searchBaseWrapper = this.bankUserManager
					.loadBankUser(searchBaseWrapper);
			
			bankUserListViewModel = (BankUserListViewModel) searchBaseWrapper
					.getBasePersistableModel();
		

		}
		BankModel bankModel = new BankModel();
		bankModel.setActive(true);
		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
				bankModel, "name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(bankModel);
		referenceDataManager.getReferenceData(referenceDataWrapper);
		List<BankModel> bankModelList = new ArrayList<BankModel>(0);
		if (referenceDataWrapper.getReferenceDataList() != null) {
			List<BankModel> banks = referenceDataWrapper.getReferenceDataList(); 
			
			// only core bank needs to be listed
			if(banks!= null) {
				Iterator<BankModel> bankIterator = banks.iterator();
				while(bankIterator.hasNext()){
					BankModel bModel = bankIterator.next();
					if(BankConstantsInterface.ASKARI_BANK_ID.longValue() == bModel.getBankId().longValue()){
						bankModelList.add(bModel);
						break;
					}
				}
			}
		}

		Map referenceDataMap = new HashMap();
		referenceDataMap.put("bankModelList", bankModelList);

		MobileTypeModel mobileTypeModel = new MobileTypeModel();

		//paymentModeModel.setActive(true);
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
		partnerModel.setAppUserTypeId(UserTypeConstantsInterface.BANK);
		if(null!=id)
		{
			if(bankUserListViewModel!=null)
			{	
			partnerModel.setBankId(bankUserListViewModel.getBankId());
			}
		}
		else
		{
			if(bankModelList!=null && bankModelList.size()>0)
			{	
			
				
				Long bankId = (Long)httpServletRequest.getAttribute("bankId");
				
				if(null!=bankId)
				{
					
					partnerModel.setBankId(bankId);
				}
				else
				{
					partnerModel.setBankId(bankModelList.get(0).getBankId());	
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
		if (referenceDataWrapper.getReferenceDataList() != null) {
			partnerGroupModelList = referenceDataWrapper.getReferenceDataList();
		}
*/
		
		referenceDataMap.put("partnerGroupModelList", partnerGroupModelList);
		//load accesslevels		
		/*AccessLevelModel accessLevelModel = new AccessLevelModel();
		
		List<AccessLevelModel> accessLevelModelList = null;
		referenceDataWrapper = new ReferenceDataWrapperImpl(accessLevelModel, "accessLevelName", SortingOrder.ASC);

		referenceDataWrapper.setBasePersistableModel(accessLevelModel);
		try
		{
			referenceDataManager.getReferenceData(referenceDataWrapper);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		if (referenceDataWrapper.getReferenceDataList() != null)
		{
			accessLevelModelList = referenceDataWrapper.getReferenceDataList();
			List<AccessLevelModel> accessLevelRefDataList = new ArrayList();
			Iterator<AccessLevelModel> accessItrator = accessLevelModelList.iterator();
			while (accessItrator.hasNext())
			{
				accessLevelModel = accessItrator.next();
				accessLevelRefDataList.add(accessLevelModel);
			}
			
			referenceDataMap.put("accessLevelRefDataList", accessLevelRefDataList);
		}		
*/
		return referenceDataMap;

		//return null;

	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest httpServletRequest)
			throws Exception {
		id = ServletRequestUtils.getLongParameter(httpServletRequest,
				"bankUserId");
		if (null != id) {
			if (log.isDebugEnabled()) {
				log.debug("id is not null....retrieving object from DB");
			}
			
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			BankUserListViewModel bankUserListViewModel = new BankUserListViewModel();
			BankUserListViewFormModel bankUserListViewFormModel = new BankUserListViewFormModel();
			BankUserModel bankUserModel =new BankUserModel();
			bankUserModel.setBankUserId(id);
			baseWrapper.setBasePersistableModel(bankUserModel);
			bankUserListViewModel.setBankUserId(id);
			searchBaseWrapper.setBasePersistableModel(bankUserListViewModel);
			searchBaseWrapper = this.bankUserManager
					.loadBankUser(searchBaseWrapper);
			baseWrapper=this.bankUserManager.loadBankUser(baseWrapper);
			bankUserListViewModel = (BankUserListViewModel) searchBaseWrapper
					.getBasePersistableModel();
			bankUserModel=(BankUserModel)baseWrapper.getBasePersistableModel();
			AppUserModel appUserModel = new AppUserModel();
			appUserModel = appUserManager.getUser(String
					.valueOf(bankUserListViewModel.getAppUserId()));

			bankUserListViewFormModel.setComments(bankUserModel.getComments());
			
			bankUserListViewFormModel.setDescription(bankUserModel.getDescription());
			bankUserListViewFormModel.setPartnerGroupId(this.bankUserManager.getAppUserPartnerGroupId(appUserModel.getAppUserId()));
			
			BeanUtils.copyProperties(bankUserListViewFormModel,
					bankUserListViewModel);
			BeanUtils.copyProperties(bankUserListViewFormModel, appUserModel);

			System.out.println(bankUserListViewModel.getAppUserId()
					+ "appuserID");
			return bankUserListViewFormModel;
		} else {
			if (log.isDebugEnabled()) {
				log.debug("id is null....creating new instance of Model");
			}

			return new BankUserListViewFormModel();
		}
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object object,
			BindException bindException) throws Exception {

		return this.createOrUpdate(httpServletRequest, httpServletResponse,
				object, bindException);
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object object,
			BindException bindException) throws Exception {
		return this.createOrUpdate(httpServletRequest, httpServletResponse,
				object, bindException);
	}

	private ModelAndView createOrUpdate(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		BankUserListViewFormModel bankUserListViewFormModel = (BankUserListViewFormModel) command;
		try {
			BaseWrapper baseWrapper = new BaseWrapperImpl();

			
			//   DistributorCreditSummaryModel distributorCreditSummaryModel=new DistributorCreditSummaryModel();

			if (null != id) {

				baseWrapper.putObject("BankUserListViewFormModel",
						bankUserListViewFormModel);

				baseWrapper = this.bankUserManager.updateBankUser(

				baseWrapper);

			} else {

				//        BankUserModel.setBankUserIdAppUserModelList(appUserModel.getAppUserId);
				baseWrapper.putObject("BankUserListViewFormModel",
						bankUserListViewFormModel);
				baseWrapper = this.bankUserManager.createBankUser(baseWrapper);

				//baseWrapper.setBasePersistableModel(bankUserListViewModel);
				//baseWrapper = this.bankUserManager.createAppUserForBank(baseWrapper);

			}

			this.saveMessage(request, "Record saved successfully");
			ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
			return modelAndView;

		} catch (FrameworkCheckedException ex) {
			request.setAttribute("bankId", bankUserListViewFormModel.getBankId());
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
		}
		catch (Exception ex) {
			request.setAttribute("bankId", bankUserListViewFormModel.getBankId());
			super.saveMessage(request, MessageUtil.getMessage("6075"));
			return super.showForm(request, response, errors);
		}
	}

	public void setReferenceDataManager(
			ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}

	public void setBankUserManager(BankUserManager bankUserManager) {
		this.bankUserManager = bankUserManager;
	}

	public void setPartnerGroupManager(PartnerGroupManager partnerGroupManager) {
		this.partnerGroupManager = partnerGroupManager;
	}

}
