package com.inov8.microbank.webapp.action.portal.mfsaccountmodule;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.ApplicantTxModeModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.FundSourceModel;
import com.inov8.microbank.common.model.KYCModel;
import com.inov8.microbank.common.model.TransactionModeModel;
import com.inov8.microbank.common.util.AuthenticationUtil;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import com.inov8.microbank.server.service.portal.usermanagementmodule.UserManagementManager;
import com.inov8.microbank.webapp.action.authorizationmodule.AdvanceAuthorizationFormController;
import com.inov8.ola.util.CustomerAccountTypeConstants;

public class Level2KYCController extends AdvanceAuthorizationFormController{

	private MfsAccountManager mfsAccountManager;
	private ReferenceDataManager referenceDataManager;
	

	public Level2KYCController() {
		setCommandName("level2KycModel");
	    setCommandClass(KYCModel.class);
	}
	@Override
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
	{
		super.initBinder(request, binder);
		CommonUtils.bindCustomDateEditor(binder);
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest req) throws Exception {
		
		String initialAppFormNo = ServletRequestUtils.getStringParameter(req, "initialApplicationFormNumber");
		String appUserId = ServletRequestUtils.getStringParameter(req, "appUserId");
		Long id = null;
		KYCModel kycModel;
		if (null != appUserId && appUserId.trim().length() > 0)
	    {
			id = new Long(EncryptionUtil.decryptWithDES(appUserId));
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			AppUserModel appUserModel = new AppUserModel();
			appUserModel.setAppUserId(id);
			baseWrapper.setBasePersistableModel(appUserModel);
			baseWrapper = this.mfsAccountManager.searchAppUserByPrimaryKey(baseWrapper);
			appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();
			
			baseWrapper = new BaseWrapperImpl();
			CustomerModel customer = new CustomerModel();
			customer.setCustomerId(appUserModel.getCustomerId());
			baseWrapper.setBasePersistableModel(customer);
			baseWrapper = mfsAccountManager.loadCustomer(baseWrapper);
			customer = (CustomerModel) baseWrapper.getBasePersistableModel();
			initialAppFormNo = customer.getInitialApplicationFormNumber();
	    }
		
		if(null != initialAppFormNo){
			kycModel = new KYCModel();
			kycModel.setInitialAppFormNo(initialAppFormNo);
			kycModel.setAcType(CustomerAccountTypeConstants.LEVEL_2);
			SearchBaseWrapper baseWrapper = new SearchBaseWrapperImpl();
			baseWrapper.setBasePersistableModel(kycModel);
			List<KYCModel> list = mfsAccountManager.findKycModel(baseWrapper);
			if(CollectionUtils.isNotEmpty(list)){
				kycModel = list.get(0);
				kycModel.setActionId(PortalConstants.ACTION_UPDATE);
				req.setAttribute("initialAppFormNo", initialAppFormNo);
			}
			//load mode of transactions
			List<ApplicantTxModeModel> modes = mfsAccountManager.loadApplicantTxModeModelByInitialApplicationFormNo(initialAppFormNo);
			List<Long> ids = new ArrayList<Long>();
			for(ApplicantTxModeModel mode : modes){
				ids.add(mode.getTxModeId());
			}
			kycModel.setExpectedModeOfTransaction(ids);
			kycModel.setUsecaseId(new Long(PortalConstants.LEVEL2_KYC_USECASE_ID));
			
			return kycModel;
		}else{
			kycModel = new KYCModel();
			kycModel.setActionId(PortalConstants.ACTION_CREATE);
			kycModel.setUsecaseId(new Long(PortalConstants.LEVEL2_KYC_USECASE_ID));
	    	return kycModel;
		}    	
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Map loadReferenceData(HttpServletRequest req) throws Exception {

		Map referenceDataMap = new HashMap();
	    FundSourceModel fundSourceModel = new FundSourceModel();
	    ReferenceDataWrapper fundSourceDataWrapper = new ReferenceDataWrapperImpl(fundSourceModel, "fundSourceId", SortingOrder.ASC);
	    fundSourceDataWrapper.setBasePersistableModel(fundSourceModel);
	    try
	    {
	      referenceDataManager.getReferenceData(fundSourceDataWrapper);
	    }
	    catch (Exception e)
	    {
	    	logger.error(e.getMessage(), e);
	    }
	    List<FundSourceModel> fundSourceList = null;
	    if (fundSourceDataWrapper.getReferenceDataList() != null)
	    {
	    	fundSourceList = fundSourceDataWrapper.getReferenceDataList();
	    }
	    referenceDataMap.put("fundSourceList", fundSourceList);
	    
	    
	    
	    TransactionModeModel transactionModeModel = new TransactionModeModel();
	    ReferenceDataWrapper transactionModeReferenceDataWrapper = new ReferenceDataWrapperImpl(transactionModeModel, "transactionModeId", SortingOrder.ASC);
	    transactionModeReferenceDataWrapper.setBasePersistableModel(transactionModeModel);
	    try
	    {
	      referenceDataManager.getReferenceData(transactionModeReferenceDataWrapper);
	    }
	    catch (Exception e)
	    {
	    	logger.error(e.getMessage(), e);
	    }
	    
	    List<TransactionModeModel> transactionModeList = null;
	    if (transactionModeReferenceDataWrapper.getReferenceDataList() != null)
	    {
	    	transactionModeList = transactionModeReferenceDataWrapper.getReferenceDataList();
	    }
	    referenceDataMap.put("transactionModeList", transactionModeList);

	    return referenceDataMap;
	}
    
	@Override
	protected ModelAndView onCreate(HttpServletRequest req, HttpServletResponse res, Object obj, BindException errors) throws Exception {
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		String initialApplicationFormNumber;
		Boolean securityCheck= Boolean.FALSE;
		try{
			KYCModel level2KycModel = (KYCModel) obj;
			
			level2KycModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
			level2KycModel.setCreatedOn(new Date());
			level2KycModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
			level2KycModel.setUpdatedOn(new Date());
			level2KycModel.setAcType(CustomerAccountTypeConstants.LEVEL_2);
			
			baseWrapper.putObject(level2KycModel.KYC_MODEL_KEY, level2KycModel);
			baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, level2KycModel.getUsecaseId());
		    baseWrapper = this.mfsAccountManager.createKYC(baseWrapper);
		    initialApplicationFormNumber = (String)baseWrapper.getObject(PortalConstants.KEY_KYC_IAFN);
		}catch(FrameworkCheckedException exception){
			
			String msg = exception.getMessage();
			String args = (String) baseWrapper.getObject(PortalConstants.KEY_KYC_IAFN);
			
			if("applicationNumberNotUniqueException".equals(msg)) {				
				this.saveMessage(req, super.getText("KYC.applicationNumberNotUnique", args, req.getLocale() ));
			} 
			
	        return super.showForm(req, res, errors);
		}
		catch (Exception e) {
			this.saveMessage(req, MessageUtil.getMessage("6075")); 
			return super.showForm(req, res, errors);
		}
		String restrictedPartnerGroup = MessageUtil.getMessage("PARTNER_GROUP.SUPER_ADMIN");
		
		//boolean access = this.userManagementManager.isAppUserInPartnerGroup(UserUtils.getCurrentUser().getAppUserId(), Long.parseLong(restrictedPartnerGroup));

		this.saveMessage(req, super.getText("KYC.recordSaveSuccessful",new Object[]{initialApplicationFormNumber} ,req.getLocale() ));
		
		String createL2Form=PortalConstants.MNG_L2_CUST_CREATE;
        
		securityCheck=AuthenticationUtil.checkRightsIfAny(createL2Form);
		
		if(securityCheck){
			//redirect to Level 2 form
			ModelAndView modelAndView = new ModelAndView("redirect:p_l2accountform.html?iaf="+initialApplicationFormNumber);
			return modelAndView;
		}

		ModelAndView modelAndView = new ModelAndView(this.getSuccessView()+"&initialApplicationFormNumber="+initialApplicationFormNumber);
		
		return modelAndView;
	}

	
	@Override
	protected ModelAndView onUpdate(HttpServletRequest req,
			HttpServletResponse res, Object obj, BindException errors)
					throws Exception {
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		String initialApplicationFormNumber;
		Boolean securityCheck=Boolean.FALSE;
		try{
			KYCModel level2KycModel = (KYCModel) obj;
			
			KYCModel queryObj =  this.mfsAccountManager.findKycByPrimaryKey(level2KycModel.getKycId());
			level2KycModel.setVersionNo(queryObj.getVersionNo());

			level2KycModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
			level2KycModel.setUpdatedOn(new Date());
			level2KycModel.setAcType(CustomerAccountTypeConstants.LEVEL_2);
			
			baseWrapper.putObject(level2KycModel.KYC_MODEL_KEY, level2KycModel);
			baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, level2KycModel.getUsecaseId());
		    baseWrapper = this.mfsAccountManager.updateKYC(baseWrapper);
		    initialApplicationFormNumber = (String)baseWrapper.getObject(PortalConstants.KEY_KYC_IAFN);
		}catch(FrameworkCheckedException exception){
			
			String msg = exception.getMessage();
			String args = (String) baseWrapper.getObject(PortalConstants.KEY_KYC_IAFN);
			
			if("applicationNumberNotUniqueException".equals(msg)) {				
				this.saveMessage(req, super.getText("KYC.applicationNumberNotUnique", args, req.getLocale() ));
			} 
			
	        return super.showForm(req, res, errors);
		}catch(Exception exception){
			this.saveMessage(req, MessageUtil.getMessage("6075"));
	        return super.showForm(req, res, errors);
		}    

		this.saveMessage(req, super.getText("KYC.recordUpdatedSuccessful",new Object[]{initialApplicationFormNumber} ,req.getLocale() ));
		
		String createL2Form=PortalConstants.MNG_L2_CUST_UPDATE+","+PortalConstants.MNG_L2_CUST_CREATE;
        
		securityCheck=AuthenticationUtil.checkRightsIfAny(createL2Form);
		
		if(securityCheck){
			//redirect to Level 2 form
			ModelAndView modelAndView = new ModelAndView("redirect:p_l2accountform.html?iaf="+initialApplicationFormNumber);
			return modelAndView;
		}

		ModelAndView modelAndView = new ModelAndView(this.getSuccessView()+"&initialApplicationFormNumber="+initialApplicationFormNumber);
		
		return modelAndView;
	}

    
	public void setMfsAccountManager(MfsAccountManager mfsAccountManager) {
		this.mfsAccountManager = mfsAccountManager;
	}
	
	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}
	
}
