package com.inov8.microbank.webapp.action.portal.mfsaccountmodule;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.ApplicantTxModeModel;
import com.inov8.microbank.common.model.FundSourceModel;
import com.inov8.microbank.common.model.KYCModel;
import com.inov8.microbank.common.model.TransactionModeModel;
import com.inov8.microbank.common.util.AuthenticationUtil;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.IssueTypeStatusConstantsInterface;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.facade.portal.mfsaccountmodule.MfsAccountFacade;
import com.inov8.ola.util.CustomerAccountTypeConstants;

public class Level3KYCController extends AdvanceFormController
{

	private MfsAccountFacade mfsAccountFacade;  
	private ReferenceDataManager referenceDataManager;

	public Level3KYCController()
	{
		setCommandName("level3KycModel");
		setCommandClass(KYCModel.class);
	}

	@Override
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
	{
		super.initBinder(request, binder);
		CommonUtils.bindCustomDateEditor(binder);
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest req) throws Exception
	{
		KYCModel kycModel = new KYCModel();
		String 	initialApplicationFormNumber	=	ServletRequestUtils.getStringParameter(req,"initAppFormNumber");
		kycModel.setInitialAppFormNo(initialApplicationFormNumber);
		kycModel.setAcType(CustomerAccountTypeConstants.LEVEL_3);
		
		if(initialApplicationFormNumber!=null)
		{
			SearchBaseWrapper searchBaseWrapper=new SearchBaseWrapperImpl();
			searchBaseWrapper.setBasePersistableModel(kycModel);
			List<KYCModel>	kycModelList	=	mfsAccountFacade.findKycModel(searchBaseWrapper);
			
			if(kycModelList!=null && kycModelList.size()>0){
				kycModel=kycModelList.get(0);
			}
		}
		
		List<ApplicantTxModeModel> list=mfsAccountFacade.loadApplicantTxModeModelByInitialApplicationFormNo(initialApplicationFormNumber);

		if(list!=null){
			kycModel.setExpectedModeOfTransaction(new ArrayList<Long>());
			for (ApplicantTxModeModel applicantTxModeModel : list)
			{
				kycModel.getExpectedModeOfTransaction().add(applicantTxModeModel.getTxModeId());
			}
		}

		kycModel.setUsecaseId(new Long(PortalConstants.LEVEL3_KYC_USECASE_ID));
		kycModel.setActionId(PortalConstants.ACTION_CREATE);
		return kycModel;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Map loadReferenceData(HttpServletRequest req) throws Exception
	{

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
		ReferenceDataWrapper transactionModeReferenceDataWrapper = new ReferenceDataWrapperImpl(transactionModeModel, "transactionModeId",
				SortingOrder.ASC);
		transactionModeReferenceDataWrapper.setBasePersistableModel(transactionModeModel);
		try
		{
			referenceDataManager.getReferenceData(transactionModeReferenceDataWrapper);
		} catch (Exception e)
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
	protected ModelAndView onCreate(HttpServletRequest req, HttpServletResponse res, Object obj, BindException errors) throws Exception
	{
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		String initialApplicationFormNumber;
		try
		{
			KYCModel level3KycModel = (KYCModel) obj;

			level3KycModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
			level3KycModel.setCreatedOn(new Date());
			level3KycModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
			level3KycModel.setUpdatedOn(new Date());
			level3KycModel.setAcType(CustomerAccountTypeConstants.LEVEL_3);

			baseWrapper.putObject(KYCModel.KYC_MODEL_KEY, level3KycModel);
			baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, level3KycModel.getUsecaseId());
			baseWrapper = this.mfsAccountFacade.createKYC(baseWrapper);
			initialApplicationFormNumber = (String) baseWrapper.getObject(PortalConstants.KEY_KYC_IAFN);
		} catch (FrameworkCheckedException exception)
		{

			String msg = exception.getMessage();
			String args = (String) baseWrapper.getObject(PortalConstants.KEY_KYC_IAFN);

			if ("applicationNumberNotUniqueException".equals(msg))
			{
				this.saveMessage(req, super.getText("KYC.applicationNumberNotUnique", args, req.getLocale()));
			}
			else
			{
				this.saveMessage(req, super.getText("6075", req.getLocale()));
			}

			return super.showForm(req, res, errors);
		}catch (Exception ex) {		
			this.saveMessage(req, MessageUtil.getMessage("6075"));
			return super.showForm(req, res, errors);
		}

		this.saveMessage(req, super.getText("KYC.recordSaveSuccessful", new Object[] { initialApplicationFormNumber }, req.getLocale()));

		ModelAndView modelAndView = new ModelAndView(getSuccessView()+"&initAppFormNumber="+ initialApplicationFormNumber);
		
		String MNG_L3_ACT_CREATE=PortalConstants.MNG_L3_ACT_CREATE;
		boolean securityCheck=AuthenticationUtil.checkRightsIfAny(MNG_L3_ACT_CREATE);
		
		if(securityCheck){
			modelAndView = new ModelAndView("redirect:p_l3accountopeningform.html?initAppFormNumber="+ initialApplicationFormNumber);
		}

		return modelAndView;
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest req, HttpServletResponse res, Object obj, BindException errors) throws Exception
	{
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		String initialApplicationFormNumber;
		try
		{
			KYCModel level3KycModel = (KYCModel) obj;

			level3KycModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
			level3KycModel.setUpdatedOn(new Date());
			level3KycModel.setAcType(CustomerAccountTypeConstants.LEVEL_3);

			baseWrapper.putObject(KYCModel.KYC_MODEL_KEY, level3KycModel);
			baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, level3KycModel.getUsecaseId());
			baseWrapper = this.mfsAccountFacade.updateKYC(baseWrapper);
			initialApplicationFormNumber = (String) baseWrapper.getObject(PortalConstants.KEY_KYC_IAFN);
		} catch (FrameworkCheckedException exception)
		{

			String msg = exception.getMessage();
			String args = (String) baseWrapper.getObject(PortalConstants.KEY_KYC_IAFN);

			if ("applicationNumberNotUniqueException".equals(msg))
			{
				this.saveMessage(req, super.getText("KYC.applicationNumberNotUnique", args, req.getLocale()));
			}
			else
			{
				this.saveMessage(req, super.getText("6075", req.getLocale()));
			}
			return super.showForm(req, res, errors);
		}
		catch (Exception ex) {		
			this.saveMessage(req, MessageUtil.getMessage("6075"));
			return super.showForm(req, res, errors);
		}

		this.saveMessage(req, super.getText("KYC.recordUpdatedSuccessful", new Object[] { initialApplicationFormNumber }, req.getLocale()));


		ModelAndView modelAndView = new ModelAndView(getSuccessView()+"&initAppFormNumber="+ initialApplicationFormNumber);
		
		String l3Permission=PortalConstants.MNG_L3_ACT_UPDATE+","+PortalConstants.MNG_L3_ACT_CREATE+","+PortalConstants.MNG_L3_ACT_READ;
		boolean securityCheck=AuthenticationUtil.checkRightsIfAny(l3Permission);
		
		if(securityCheck){
			modelAndView = new ModelAndView("redirect:p_l3accountopeningform.html?initAppFormNumber="+ initialApplicationFormNumber);
		}

		return modelAndView;
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
	{
		this.referenceDataManager = referenceDataManager;
	}

	public void setMfsAccountFacade(MfsAccountFacade mfsAccountFacade)
	{
		this.mfsAccountFacade = mfsAccountFacade;
	}
}
