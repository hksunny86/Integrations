package com.inov8.microbank.webapp.action.commissionstakeholdermodule;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.OperatorModel;
import com.inov8.microbank.common.model.RetailerModel;
import com.inov8.microbank.common.model.StakeholderTypeModel;
import com.inov8.microbank.common.util.BankConstantsInterface;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.commissionstakeholdermodule.CommissionStakeholderManager;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Rizwan-ur-Rehman
 * @version 1.0
 */

public class CommissionStakeholderFormController
	extends AdvanceFormController
{
	private CommissionStakeholderManager commissionStakeholderManager;
	private ReferenceDataManager referenceDataManager;

	private Long id;

	public CommissionStakeholderFormController()
	{
		setCommandName("commissionStakeholderModel");
		setCommandClass(CommissionStakeholderModel.class);
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest httpServletRequest)
	{
		Map referenceDataMap = new HashMap();

		if (log.isDebugEnabled())
		{
			log.debug("Inside reference data");
		}

		//Load reference data for Stakeholder Type

		StakeholderTypeModel stakeholderTypeModel = new StakeholderTypeModel();
		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(stakeholderTypeModel, "name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(stakeholderTypeModel);
		try
		{
			referenceDataManager.getReferenceData(referenceDataWrapper);
		}
		catch (Exception e)
		{

		}
		List<StakeholderTypeModel> stakeholderTypeModelList = null;
		List<StakeholderTypeModel> bankOperatorOnlystakeholderTypeModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null)
		{
			stakeholderTypeModelList = referenceDataWrapper.getReferenceDataList();
			bankOperatorOnlystakeholderTypeModelList = new ArrayList<StakeholderTypeModel>();
			
			for(StakeholderTypeModel model: stakeholderTypeModelList)
			{
				if("bank".equalsIgnoreCase(model.getName()) || "operator".equalsIgnoreCase(model.getName()))
				{
					bankOperatorOnlystakeholderTypeModelList.add(model);
				}
			}
			
			
		}
		referenceDataMap.put("stakeholderTypeModelList", bankOperatorOnlystakeholderTypeModelList);

		//load reference data for bank

		BankModel bankModel = new BankModel();
		bankModel.setActive(true);
		referenceDataWrapper = new ReferenceDataWrapperImpl(bankModel, "name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(bankModel);
		try
		{
			referenceDataManager.getReferenceData(referenceDataWrapper);
		}
		catch (Exception e)
		{

		}
		List<BankModel> bankModelList = new ArrayList<BankModel>(0);
		if (referenceDataWrapper.getReferenceDataList() != null) {
			List<BankModel> banks = referenceDataWrapper.getReferenceDataList(); 
			
			// only core bank needs to be listed
			if(banks!= null) {
				Iterator<BankModel> bankIterator = banks.iterator();
				while(bankIterator.hasNext()) {
					BankModel bModel = bankIterator.next();
					if(BankConstantsInterface.ASKARI_BANK_ID.longValue() == bModel.getBankId().longValue()){
						bankModelList.add(bModel);
						break;
					}
				}
			}
		}
		referenceDataMap.put("bankModelList", bankModelList);


		//load reference data for Distributor

		DistributorModel distributorModel = new DistributorModel();
		distributorModel.setActive(true);
		referenceDataWrapper = new ReferenceDataWrapperImpl(distributorModel, "name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(distributorModel);
		try
		{
			referenceDataManager.getReferenceData(referenceDataWrapper);
		}
		catch (Exception e)
		{

		}
		List<DistributorModel> distributorModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null)
		{
			distributorModelList = referenceDataWrapper.getReferenceDataList();
		}
		referenceDataMap.put("distributorModelList", distributorModelList);

		//load reference data for Operator

		OperatorModel operatorModel = new OperatorModel();
		referenceDataWrapper = new ReferenceDataWrapperImpl(operatorModel, "name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(operatorModel);
		try
		{
			referenceDataManager.getReferenceData(referenceDataWrapper);
		}
		catch (Exception e)
		{

		}
		List<OperatorModel> operatorModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null)
		{
			operatorModelList = referenceDataWrapper.getReferenceDataList();
		}
		referenceDataMap.put("operatorModelList", operatorModelList);

		//load reference data for Retailer

		RetailerModel retailerModel = new RetailerModel();
		retailerModel.setActive(true);
		referenceDataWrapper = new ReferenceDataWrapperImpl(retailerModel, "name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(retailerModel);
		try
		{
			referenceDataManager.getReferenceData(referenceDataWrapper);
		}
		catch (Exception e)
		{

		}
		List<RetailerModel> retailerModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null)
		{
			retailerModelList = referenceDataWrapper.getReferenceDataList();
		}
		referenceDataMap.put("retailerModelList", retailerModelList);

		return referenceDataMap;
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws Exception
	{
		id = ServletRequestUtils.getLongParameter(httpServletRequest, "commissionStakeholderId");
		if (null != id)
		{
			if (log.isDebugEnabled())
			{
				log.debug("id is not null....retrieving object from DB");
			}

			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			CommissionStakeholderModel commissionStakeholderModel = new CommissionStakeholderModel();
			commissionStakeholderModel.setCommissionStakeholderId(id);
			searchBaseWrapper.setBasePersistableModel(commissionStakeholderModel);
			searchBaseWrapper = this.commissionStakeholderManager.loadCommissionStakeholder(searchBaseWrapper);
			return (CommissionStakeholderModel) searchBaseWrapper.getBasePersistableModel();
		}
		else
		{
			if (log.isDebugEnabled())
			{
				log.debug("id is null....creating new instance of Model");
			}
			return new CommissionStakeholderModel();
		}
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,
			Object object, BindException errors) throws	Exception
	{
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		try
		{
			CommissionStakeholderModel commissionStakeholderModel = (CommissionStakeholderModel) object;
			commissionStakeholderModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			commissionStakeholderModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
			baseWrapper.setBasePersistableModel(commissionStakeholderModel);
			baseWrapper = this.commissionStakeholderManager.createCommissionStakeholder(baseWrapper);

			if (null != baseWrapper.getBasePersistableModel() && ((CommissionStakeholderModel)baseWrapper.getBasePersistableModel()).getName()!=null)
			{ //It means model is not an empty model so show save msg 
				this.saveMessage(httpServletRequest,"Record saved successfully");
				ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
				return modelAndView;
			}						
			else if (null != baseWrapper.getBasePersistableModel() && ((CommissionStakeholderModel)baseWrapper.getBasePersistableModel()).getName()==null)
			{
				this.saveMessage(httpServletRequest,"Stakeholder of selected partner already exists.");
				return super.showForm(httpServletRequest, httpServletResponse, errors);			}
			else
			{
				this.saveMessage(httpServletRequest, "Commission Stakeholder with the same name already exists.");
				return super.showForm(httpServletRequest, httpServletResponse, errors);
			}
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
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		try
		{
			Date nowDate = new Date();
			CommissionStakeholderModel commissionStakeholderModel = (CommissionStakeholderModel) object;
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			CommissionStakeholderModel commStakeholderModel = new CommissionStakeholderModel();
			commStakeholderModel.setPrimaryKey(commissionStakeholderModel.getCommissionStakeholderId());
			searchBaseWrapper.setBasePersistableModel(commStakeholderModel);
			searchBaseWrapper = this.commissionStakeholderManager.loadCommissionStakeholder(searchBaseWrapper);
			commStakeholderModel = (CommissionStakeholderModel)searchBaseWrapper.getBasePersistableModel();
			commissionStakeholderModel.setCreatedOn(commStakeholderModel.getCreatedOn());
			
			commissionStakeholderModel.setUpdatedOn(nowDate);
			commissionStakeholderModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			baseWrapper.setBasePersistableModel(commissionStakeholderModel);
			baseWrapper = this.commissionStakeholderManager.updateCommissionStakeholder(baseWrapper);

			if (null != baseWrapper.getBasePersistableModel())
			{
				this.saveMessage(httpServletRequest,"Record saved successfully");
				ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
				return modelAndView;
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
		}catch (Exception ex)
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
}
