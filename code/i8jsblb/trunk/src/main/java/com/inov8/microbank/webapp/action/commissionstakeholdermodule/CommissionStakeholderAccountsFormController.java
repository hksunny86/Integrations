package com.inov8.microbank.webapp.action.commissionstakeholdermodule;

import com.inov8.framework.common.exception.ExceptionErrorCodes;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.*;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.CommissionShAcctsTypeModel;
import com.inov8.microbank.common.model.CommissionStakeholderModel;
import com.inov8.microbank.common.model.OperatorModel;
import com.inov8.microbank.common.model.StakeholderBankInfoModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.server.service.commissionstakeholdermodule.CommissionStakeholderManager;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Fahad Tariq
 * @version 1.0
 */

public class CommissionStakeholderAccountsFormController
	extends AdvanceFormController
{
	private StakeholderBankInfoManager stakeholderBankInfoManager;
	private CommissionStakeholderManager commissionStakeholderManager;
	private ReferenceDataManager referenceDataManager;


	private Long id;

	public CommissionStakeholderAccountsFormController()
	{
		setCommandName("stakeholderBankInfoModel");
		setCommandClass(StakeholderBankInfoModel.class);
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest httpServletRequest)
	{
		Map referenceDataMap = new HashMap();

		/*if (log.isDebugEnabled())
		{
			log.debug("Inside reference data");
		}
		
		CommissionShAcctsTypeModel commissionShAcctsTypeModel = new CommissionShAcctsTypeModel();
		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(commissionShAcctsTypeModel, "name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(commissionShAcctsTypeModel);
		try
		{
			referenceDataManager.getReferenceData(referenceDataWrapper);
		}
		catch (Exception e)
		{

		}
		
		List<CommissionShAcctsTypeModel> commissionShAcctsTypeModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null)
		{
			commissionShAcctsTypeModelList = referenceDataWrapper.getReferenceDataList();
		}
		
		referenceDataMap.put("commissionShAcctsTypeModelList", commissionShAcctsTypeModelList);
		
		///////////////////////////////////////////
		
		CommissionStakeholderModel commissionStakeholderModel = new CommissionStakeholderModel();
		referenceDataWrapper = new ReferenceDataWrapperImpl(commissionStakeholderModel, "name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(commissionStakeholderModel);
		try
		{
			referenceDataManager.getReferenceData(referenceDataWrapper);
		}
		catch (Exception e)
		{

		}
		
		List<CommissionStakeholderModel> commissionStakeholderModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null)
		{
			commissionStakeholderModelList = referenceDataWrapper.getReferenceDataList();
			
			ArrayList<CommissionStakeholderModel> toBeRemovedList = new ArrayList<CommissionStakeholderModel>();
			
			CommissionStakeholderModel cshm = null;
			// To remove Agent Entry
			for(int i=0; i<commissionStakeholderModelList.size() ; i++){
				cshm = commissionStakeholderModelList.get(i);
				if(cshm.getName().contains("Agent") == true){
					toBeRemovedList.add(commissionStakeholderModelList.get(i));
				}
			}
			
			for(int j=0; j<toBeRemovedList.size(); j++){
				commissionStakeholderModelList.remove(toBeRemovedList.get(j));
			}
			
		}
		
		referenceDataMap.put("commissionStakeholderModelList", commissionStakeholderModelList);*/

		List<CommissionShAcctsTypeModel> commissionShAcctsTypeModelList = new ArrayList<CommissionShAcctsTypeModel>();
		CommissionShAcctsTypeModel commissionShAcctsTypeModel = new CommissionShAcctsTypeModel();
		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
				commissionShAcctsTypeModel, "name", SortingOrder.ASC);

		try {
			referenceDataManager.getReferenceData(referenceDataWrapper);
			if (referenceDataWrapper.getReferenceDataList() != null) {
				commissionShAcctsTypeModelList = (List<CommissionShAcctsTypeModel>) referenceDataWrapper.getReferenceDataList();
			}
		} catch (FrameworkCheckedException ex) {
			logger.error(ex.getMessage(), ex);
		}
		referenceDataMap.put("commissionShAcctsTypeModelList",commissionShAcctsTypeModelList);

		List<GLTypeEnum> glTypeEnumList=new ArrayList<>();
		glTypeEnumList.add(GLTypeEnum.ASSETS);
		glTypeEnumList.add(GLTypeEnum.EXPENSE);
		glTypeEnumList.add(GLTypeEnum.LIABILITY);
		glTypeEnumList.add(GLTypeEnum.INCOME);
		referenceDataMap.put("glTypeModelList",glTypeEnumList );
		List<ParentGLEnum> parentGLEnumList=new ArrayList<>();
		parentGLEnumList.add(ParentGLEnum.SUNDRY);
		parentGLEnumList.add(ParentGLEnum.CONTROL_AC);
		parentGLEnumList.add(ParentGLEnum.EXPENSE);
		parentGLEnumList.add(ParentGLEnum.INCOME);
		parentGLEnumList.add(ParentGLEnum.LIABILITY);
		parentGLEnumList.add(ParentGLEnum.PAYABLE);
		parentGLEnumList.add(ParentGLEnum.RECEIVABLES);
		parentGLEnumList.add(ParentGLEnum.SETTLEMENT);
		parentGLEnumList.add(ParentGLEnum.WHT_PAYABLES);
		parentGLEnumList.add(ParentGLEnum.BLB_DEPOSITS);

		referenceDataMap.put("parentGLModelList", parentGLEnumList);

		List<LabelValueBean> filerList = new ArrayList<>(2);
		LabelValueBean filer = new LabelValueBean("Filer", "1");
		filerList.add(filer);
		filer = new LabelValueBean("Non Filer", "0");
		filerList.add(filer);
		referenceDataMap.put("filerList", filerList);




		return referenceDataMap;
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws Exception
	{
		id = ServletRequestUtils.getLongParameter(httpServletRequest, "stakeholderBankInfoId");
		if (null != id)
		{
			if (log.isDebugEnabled())
			{
				log.debug("id is not null....retrieving object from DB");
			}

			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			StakeholderBankInfoModel stakeholderBankInfoModel = new StakeholderBankInfoModel();
			stakeholderBankInfoModel.setStakeholderBankInfoId(id);
			searchBaseWrapper.setBasePersistableModel(stakeholderBankInfoModel);
			searchBaseWrapper = this.stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper);
//			searchBaseWrapper = this.commissionStakeholderManager.loadCommissionStakeholder(searchBaseWrapper);
			return (StakeholderBankInfoModel) searchBaseWrapper.getBasePersistableModel();
		}
		else
		{
			if (log.isDebugEnabled())
			{
				log.debug("id is null....creating new instance of Model");
			}
			return new StakeholderBankInfoModel();
		}
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,
			Object object, BindException errors) throws	Exception
	{
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		try
		{
			
			StakeholderBankInfoModel stakeholderBankInfoModel = (StakeholderBankInfoModel) object;			

			// Check already commission stake holder exist or not
			CommissionStakeholderModel stakeholderModel = new CommissionStakeholderModel();
			if(stakeholderBankInfoModel.getCmshaccttypeId()==3){
				stakeholderModel.setDisplayOnProductScreen(true);
			}
			else
			{
				stakeholderModel.setDisplayOnProductScreen(false);
			}
			ReferenceDataWrapper  referenceDataWrapper = new ReferenceDataWrapperImpl(stakeholderModel, "name", SortingOrder.ASC);
			referenceDataWrapper.setBasePersistableModel(stakeholderModel);
			referenceDataManager.getReferenceData(referenceDataWrapper);
			List<CommissionStakeholderModel> commissionStakeholderModelList = null;
			if (referenceDataWrapper.getReferenceDataList() != null)
		    {
				commissionStakeholderModelList = referenceDataWrapper. getReferenceDataList();
		    }
			
			for (CommissionStakeholderModel csModel : commissionStakeholderModelList) {
				if(csModel.getName().equalsIgnoreCase(stakeholderBankInfoModel.getCommissionStakeholderIdCommissionStakeholderModel().getName())){
					this.saveMessage(httpServletRequest,"Commission stake holder with "+stakeholderBankInfoModel.getCommissionStakeholderIdCommissionStakeholderModel().getName()
							+" name already exists. Please choose anotherone.");
					ModelAndView modelAndView = new ModelAndView("redirect:commissionstakeholderaccountsform.html");
					return modelAndView;

				}
			}
			
			//added by atif hussain
			//stakeholderBankInfoModel.setCmshaccttypeId(3L);
			stakeholderBankInfoModel.setAccountType("OF_SET");
			//end of added by atif hussain
			stakeholderBankInfoModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			stakeholderBankInfoModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
			stakeholderBankInfoModel.setUpdatedOn(new Date());
			stakeholderBankInfoModel.setCreatedOn(new Date());
			stakeholderBankInfoModel.setActive(true);
			stakeholderBankInfoModel.setBankId(CommissionConstantsInterface.BANK_ID);
			
			
			// Create Commission Stake Holder
			CommissionStakeholderModel commissionStakeholderModel = new CommissionStakeholderModel();
			commissionStakeholderModel.setCmshaccttypeId(stakeholderBankInfoModel.getCmshaccttypeIdCommissionShAcctsTypeModel().getCmshacctstypeId());
			// Temp set , need to discussed with mudassir later.
			commissionStakeholderModel.setStakeholderTypeId(5L);
			commissionStakeholderModel.setOperatorId(1L);
			OperatorModel operatorModel = new OperatorModel();
			operatorModel.setOperatorId(1L);
			commissionStakeholderModel.setOperatorIdOperatorModel(operatorModel);
			commissionStakeholderModel.setName(stakeholderBankInfoModel.getCommissionStakeholderIdCommissionStakeholderModel().getName());
			commissionStakeholderModel.setContactName(stakeholderBankInfoModel.getName());
			commissionStakeholderModel.setUpdatedOn(new Date());
			commissionStakeholderModel.setCreatedOn(new Date());
			commissionStakeholderModel.setDisplayOnProductScreen(stakeholderModel.getDisplayOnProductScreen());
			commissionStakeholderModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			commissionStakeholderModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
			commissionStakeholderModel.setFiler(stakeholderBankInfoModel.getFiler());
			baseWrapper.setBasePersistableModel(commissionStakeholderModel);
			BaseWrapper wrapper = this.commissionStakeholderManager.saveCommissionStakeholderAccount(baseWrapper);
			commissionStakeholderModel = (CommissionStakeholderModel)wrapper.getBasePersistableModel();
			stakeholderBankInfoModel.setCommissionStakeholderIdCommissionStakeholderModel(commissionStakeholderModel);
			//added by atif hussain
//			stakeholderBankInfoModel.setCmshaccttypeId(3L);
			stakeholderBankInfoModel.setAccountType("OF_SET");
			//end of added by atif hussain
			
			// Create Stake Holder Bank Info for Core Account
			commissionStakeholderModel.setBankId(CommissionConstantsInterface.BANK_ID);
			baseWrapper.setBasePersistableModel(stakeholderBankInfoModel);
			this.stakeholderBankInfoManager.createOrUpdateStakeholderBankInfo(baseWrapper);
			  
			// Create Stake Holder Bank Info for OLA Account
			StakeholderBankInfoModel bankInfoModel = new StakeholderBankInfoModel();

			bankInfoModel.setAccountNo(stakeholderBankInfoModel.getAccountNo());
			bankInfoModel.setActive(stakeholderBankInfoModel.getActive());
			bankInfoModel.setCmshaccttypeIdCommissionShAcctsTypeModel(stakeholderBankInfoModel.getCmshaccttypeIdCommissionShAcctsTypeModel());
			bankInfoModel.setUpdatedOn(new Date());
			bankInfoModel.setCreatedOn(new Date());
			bankInfoModel.setActive(Boolean.TRUE);
			bankInfoModel.setName(stakeholderBankInfoModel.getName()+" -BLB");
			bankInfoModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			bankInfoModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
			bankInfoModel.setCommissionStakeholderIdCommissionStakeholderModel(commissionStakeholderModel);
			bankInfoModel.setOfSettlementStakeholderBankInfoModel(stakeholderBankInfoModel);
			//added by atif hussain
			bankInfoModel.setBankId(BankConstantsInterface.OLA_BANK_ID);
			bankInfoModel.setAccountType("BLB");
			bankInfoModel.setFiler(stakeholderBankInfoModel.getFiler());
			//end of added by atif hussain
			
			baseWrapper.setBasePersistableModel(bankInfoModel);
			baseWrapper = this.stakeholderBankInfoManager.createStakeHolderAccount(baseWrapper);
			
			this.saveMessage(httpServletRequest,"Record saved successfully");
			ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
			return modelAndView;

		}
		catch (FrameworkCheckedException ex)
		{
			if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION ==
				ex.getErrorCode())
			{
				super.saveMessage(httpServletRequest,
						"Record could not be saved.");
				return super.showForm(httpServletRequest, httpServletResponse, errors);
			}
			throw ex;
		}
		catch (Exception ex)
		{
				super.saveMessage(httpServletRequest,
						MessageUtil.getMessage("6075"));
				return super.showForm(httpServletRequest, httpServletResponse, errors);
		}
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,
			Object object, BindException errors) throws Exception
	{
		String stakeholderBankInfoId = ServletRequestUtils.getStringParameter(httpServletRequest, "stakeholderBankInfoId");		
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		try
		{
			StakeholderBankInfoModel stakeholderBankInfoModel = (StakeholderBankInfoModel) object;
			if(!StringUtil.isNullOrEmpty(stakeholderBankInfoId)){				
				stakeholderBankInfoModel.setStakeholderBankInfoId(Long.valueOf(stakeholderBankInfoId));
			}
		
			SearchBaseWrapper sBaseWrapper = new SearchBaseWrapperImpl();
			sBaseWrapper.setBasePersistableModel(stakeholderBankInfoModel);
			SearchBaseWrapper wrapper = this.stakeholderBankInfoManager.loadStakeHolderBankInfo(sBaseWrapper);
			StakeholderBankInfoModel model = (StakeholderBankInfoModel)wrapper.getBasePersistableModel();
			CommissionStakeholderModel commissionStakeholderModel =new CommissionStakeholderModel();
			commissionStakeholderModel=model.getCommissionStakeholderIdCommissionStakeholderModel();

			model.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			model.setCreatedByAppUserModel(UserUtils.getCurrentUser());
			model.setUpdatedOn(new Date());
			model.setCreatedOn(new Date());
			model.setAccountNo(stakeholderBankInfoModel.getAccountNo());
			model.setName(stakeholderBankInfoModel.getName());
			model.setFiler(stakeholderBankInfoModel.getFiler());
			model.setCmshaccttypeIdCommissionShAcctsTypeModel(stakeholderBankInfoModel.getCmshaccttypeIdCommissionShAcctsTypeModel());
			commissionStakeholderModel.setCmshaccttypeIdCommissionShAcctsTypeModel(stakeholderBankInfoModel.getCmshaccttypeIdCommissionShAcctsTypeModel());
			baseWrapper.setBasePersistableModel(model);
			if(commissionStakeholderModel.getCmshaccttypeId()==3){
				commissionStakeholderModel.setDisplayOnProductScreen(true);
			}
			else
			{
				commissionStakeholderModel.setDisplayOnProductScreen(false);
			}
			commissionStakeholderModel.setFiler(stakeholderBankInfoModel.getFiler());
			baseWrapper = this.stakeholderBankInfoManager.createOrUpdateStakeholderBankInfo(baseWrapper);

			if (null != baseWrapper.getBasePersistableModel())
			{
				baseWrapper.setBasePersistableModel(commissionStakeholderModel);
				baseWrapper = this.commissionStakeholderManager.updateCommissionStakeholder(baseWrapper);
				if (null != baseWrapper.getBasePersistableModel())
				{
					this.saveMessage(httpServletRequest,"Record updated successfully");
					ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
					return modelAndView;
				}
				else
				{
					this.saveMessage(httpServletRequest,"Record could not be saved");
					return super.showForm(httpServletRequest, httpServletResponse, errors);
				}
			}
			else
			{
				this.saveMessage(httpServletRequest,"Record could not be saved");
				return super.showForm(httpServletRequest, httpServletResponse, errors);
			}
		}
		catch (FrameworkCheckedException ex)
		{
			if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION == ex.getErrorCode())
			{
				super.saveMessage(httpServletRequest,"Record could not be saved");
				return super.showForm(httpServletRequest, httpServletResponse, errors);
			}
			throw ex;
		}
		catch (Exception ex)
		{
				super.saveMessage(httpServletRequest,
						MessageUtil.getMessage("6075"));
				return super.showForm(httpServletRequest, httpServletResponse, errors);
		}
	}

	public void setCommissionStakeholderManager(CommissionStakeholderManager commissionStakeholderManager)
	{
		this.commissionStakeholderManager = commissionStakeholderManager;
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
	{
		this.referenceDataManager = referenceDataManager;
	}

	public StakeholderBankInfoManager getStakeholderBankInfoManager() {
		return stakeholderBankInfoManager;
	}

	public void setStakeholderBankInfoManager(
			StakeholderBankInfoManager stakeholderBankInfoManager) {
		this.stakeholderBankInfoManager = stakeholderBankInfoManager;
	}


}
