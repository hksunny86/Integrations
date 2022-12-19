package com.inov8.microbank.webapp.action.commissionmodule;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.CustomNumberEditor;
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
import com.inov8.microbank.common.model.CommissionRateModel;
import com.inov8.microbank.common.model.CommissionReasonModel;
import com.inov8.microbank.common.model.CommissionStakeholderModel;
import com.inov8.microbank.common.model.CommissionTypeModel;
import com.inov8.microbank.common.model.DispenseTypeModel;
import com.inov8.microbank.common.model.PaymentModeModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.common.model.StakeholderBankInfoModel;
import com.inov8.microbank.common.model.StakeholderTypeModel;
import com.inov8.microbank.common.model.TransactionTypeModel;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.commissionmodule.CommissionRateManager;
import com.inov8.microbank.server.service.commissionstakeholdermodule.CommissionStakeholderManager;

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

public class CommissionRateFormController extends AdvanceFormController
{
	private CommissionRateManager commissionRateManager;

	private CommissionStakeholderManager commissionStakeholderManager;
	
	private ReferenceDataManager referenceDataManager;

	private Long id;

	int insertFlag = 0;

	public CommissionRateFormController()
	{
		setCommandName("commissionRateModel");
		setCommandClass(CommissionRateModel.class);
	}

	@Override
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
	{
		super.initBinder(request, binder);
		DecimalFormat decimalFormat = new DecimalFormat("###.##");
		binder.registerCustomEditor(Double.class, new CustomNumberEditor(Double.class,decimalFormat,true));

	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected Map<String,Object> loadReferenceData(HttpServletRequest httpServletRequest)
	{			 
		if (log.isDebugEnabled())
		{
			log.debug("Inside reference data");
		}

		/**
		 * code fragment to load reference data  for Commission Rate
		 *
		 */

		TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(transactionTypeModel,
				"name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(transactionTypeModel);
		try
		{
			referenceDataManager.getReferenceData(referenceDataWrapper);
		}
		catch (Exception e)
		{

		}
		List<TransactionTypeModel> transactionTypeModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null)
		{
			transactionTypeModelList = referenceDataWrapper.getReferenceDataList();
		}
		Map<String,Object> referenceDataMap = new HashMap<>();
		referenceDataMap.put("transactionTypeModelList", transactionTypeModelList);
		
		// Load Reference Data For Segment
		SegmentModel segmentModel = new SegmentModel();
		referenceDataWrapper = new ReferenceDataWrapperImpl(segmentModel, "name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(segmentModel);
		try
		{
			referenceDataManager.getReferenceData(referenceDataWrapper);
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}
		List<SegmentModel> segmentModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null)
		{
			segmentModelList = referenceDataWrapper.getReferenceDataList();
		}
		referenceDataMap.put("segmentModelList", segmentModelList);

		// Load Reference Data For Dispense Type

		DispenseTypeModel dispenseTypeModel = new DispenseTypeModel();
		referenceDataWrapper = new ReferenceDataWrapperImpl(dispenseTypeModel, "name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(dispenseTypeModel);
		try
		{
			referenceDataManager.getReferenceData(referenceDataWrapper);
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}
		List<DispenseTypeModel> dispenseTypeModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null)
		{
			dispenseTypeModelList = referenceDataWrapper.getReferenceDataList();
		}
		referenceDataMap.put("dispenseTypeModelList", dispenseTypeModelList);

		//Load Reference Data For Commission Type

		CommissionTypeModel commissionTypeModel = new CommissionTypeModel();
		referenceDataWrapper = new ReferenceDataWrapperImpl(commissionTypeModel, "name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(commissionTypeModel);
		try
		{
			referenceDataManager.getReferenceData(referenceDataWrapper);
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}
		List<CommissionTypeModel> commissionTypeModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null)
		{
			commissionTypeModelList = referenceDataWrapper.getReferenceDataList();

			//Remove Percentage because it is currently not supported
			if( commissionTypeModelList != null && commissionTypeModelList.size() > 1 )
            {
			    commissionTypeModelList.remove( 1 );
            }
		}
		referenceDataMap.put("commissionTypeModelList", commissionTypeModelList);

		// Load Reference Data For Product

		ProductModel productModel = new ProductModel();
		productModel.setActive(true);
		referenceDataWrapper = new ReferenceDataWrapperImpl(productModel, "name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(productModel);
		try
		{
			referenceDataManager.getReferenceData(referenceDataWrapper);
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}
		List<ProductModel> productModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null)
		{
			productModelList = referenceDataWrapper.getReferenceDataList();
			removeChargesLessProducts( productModelList );
		}
		referenceDataMap.put("productModelList", productModelList);

		// Load Reference Data For Payment Type

		PaymentModeModel paymentModeModel = new PaymentModeModel();
		referenceDataWrapper = new ReferenceDataWrapperImpl(paymentModeModel, "name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(paymentModeModel);
		try
		{
			referenceDataManager.getReferenceData(referenceDataWrapper);
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}
		List<PaymentModeModel> paymentModeModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null)
		{
			paymentModeModelList = referenceDataWrapper.getReferenceDataList();
		}
		referenceDataMap.put("paymentModeModelList", paymentModeModelList);

		// Load Reference Data For Stakeholder Type

		StakeholderTypeModel stakeHolderTypeModel = new StakeholderTypeModel();
		referenceDataWrapper = new ReferenceDataWrapperImpl(stakeHolderTypeModel, "name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(stakeHolderTypeModel);
		try
		{
			referenceDataManager.getReferenceData(referenceDataWrapper);
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}
		List<StakeholderTypeModel> stakeHolderTypeModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null)
		{
			stakeHolderTypeModelList = referenceDataWrapper.getReferenceDataList();
		}
		referenceDataMap.put("stakeHolderTypeModelList", stakeHolderTypeModelList);

		// Load Reference Data For Commission Stake Holder

//		CommissionStakeholderModel commissionStakeHolderModel = new CommissionStakeholderModel();
//		referenceDataWrapper = new ReferenceDataWrapperImpl(commissionStakeHolderModel, "name",
//				SortingOrder.ASC);
//		referenceDataWrapper.setBasePersistableModel(commissionStakeHolderModel);
//		try
//		{
//			referenceDataManager.getReferenceData(referenceDataWrapper);
//		}
//		catch (Exception e)
//		{
//
//		}
//		List<CommissionStakeholderModel> commissionStakeHolderModelList = null;
//		if (referenceDataWrapper.getReferenceDataList() != null)
//		{
//			commissionStakeHolderModelList = referenceDataWrapper.getReferenceDataList();
//		}
//		referenceDataMap.put("commissionStakeHolderModelList", commissionStakeHolderModelList);

		
		// Load Reference Data For Commission Reason

		CommissionReasonModel commissionReasonModel = new CommissionReasonModel();
		commissionReasonModel.setActive(true);
		referenceDataWrapper = new ReferenceDataWrapperImpl(commissionReasonModel, "name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(commissionReasonModel);
		try
		{
			referenceDataManager.getReferenceData(referenceDataWrapper);
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}
		List<CommissionReasonModel> commissionReasonModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null)
		{
			commissionReasonModelList = referenceDataWrapper.getReferenceDataList();
		}		
//		if(commissionStakeHolderModelList.size()!=0){
			
			List<CommissionReasonModel> tempCommissionReasonModelList = new ArrayList<CommissionReasonModel>();
			for (CommissionReasonModel model : commissionReasonModelList) {
				tempCommissionReasonModelList.add(model);
			}			

			if(id!=null){
				SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
				CommissionRateModel commissionRateModel = new CommissionRateModel();
				commissionRateModel.setPrimaryKey(id);
				searchBaseWrapper.setBasePersistableModel(commissionRateModel);
				try {
					searchBaseWrapper = this.commissionRateManager.loadCommissionRate(searchBaseWrapper);
				} catch (FrameworkCheckedException e) {					
					e.printStackTrace();
				}
				commissionRateModel = (CommissionRateModel) searchBaseWrapper.getBasePersistableModel();				
				if(commissionRateModel.getCommissionStakeholderIdCommissionStakeholderModel().getStakeholderTypeId()==5){
					for (CommissionReasonModel model : commissionReasonModelList) {
							if(model.getCommissionReasonId()!=4 && model.getCommissionReasonId()!=8 && model.getCommissionReasonId()!=9)
								tempCommissionReasonModelList.remove(model);
					}						
				}
				else{
					for (CommissionReasonModel model : commissionReasonModelList) {
						if(model.getCommissionReasonId()==8 || model.getCommissionReasonId()==9)
							tempCommissionReasonModelList.remove(model);
					}										
				}									
			}
			else{
//				Object stakeHolderId = httpServletRequest.getAttribute("stakeHolderId");
				Long stakeholderTypeId = 1l;				
//				if(stakeHolderId!=null && !"".equals(stakeHolderId.toString())){					
//					for(CommissionStakeholderModel model : commissionStakeHolderModelList){
//						if(model.getCommissionStakeholderId()== Long.parseLong(stakeHolderId.toString())){
//							stakeholderTypeId = model.getStakeholderTypeId();
//							break;
//						}
//					}
//				}
//				else{
//					stakeholderTypeId = commissionStakeHolderModelList.get(0).getStakeholderTypeId();
//				}
				if(stakeholderTypeId==5){															
					for (CommissionReasonModel model : commissionReasonModelList) {
							if(model.getCommissionReasonId()!=4 && model.getCommissionReasonId()!=8 && model.getCommissionReasonId()!=9)
								tempCommissionReasonModelList.remove(model);
					}						
				}
				else{
					for (CommissionReasonModel model : commissionReasonModelList) {
						if(model.getCommissionReasonId()==8 || model.getCommissionReasonId()==9)
							tempCommissionReasonModelList.remove(model);
					}										
				}	
			}	
			commissionReasonModelList = tempCommissionReasonModelList;
//		}				
		referenceDataMap.put("commissionReasonModelList", commissionReasonModelList);		
		
		// Load Reference Data For Stake Holder Bank Info

		StakeholderBankInfoModel stakeholderBankInfoModel = new StakeholderBankInfoModel();
		stakeholderBankInfoModel.setActive(true);
		if (null==id)
		{
			
//			if(commissionStakeHolderModelList.size()>0)
//			{
//			stakeholderBankInfoModel.setCommissionStakeholderId((((CommissionStakeholderModel) commissionStakeHolderModelList.get(0)).getCommissionStakeholderId()));
//			}
			stakeholderBankInfoModel.setCommissionStakeholderId(50020l);
					
		}
		else 
		{
			
			CommissionRateModel commissionRateModel = new CommissionRateModel();
			commissionRateModel.setPrimaryKey(id);
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();  
			searchBaseWrapper.setBasePersistableModel(commissionRateModel);
			try {
				searchBaseWrapper = this.commissionRateManager.loadCommissionRate(searchBaseWrapper);
			} catch (FrameworkCheckedException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage(), e);
			}
			CommissionRateModel commRateModel = (CommissionRateModel) searchBaseWrapper.getBasePersistableModel();
			stakeholderBankInfoModel.setCommissionStakeholderId(commRateModel.getCommissionStakeholderId());
			
			
		}
		referenceDataWrapper = new ReferenceDataWrapperImpl(stakeholderBankInfoModel, "name",
				SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(stakeholderBankInfoModel);
		try
		{
			referenceDataManager.getReferenceData(referenceDataWrapper);
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}
		List<StakeholderBankInfoModel> stakeholderBankInfoModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null)
		{
			stakeholderBankInfoModelList = referenceDataWrapper.getReferenceDataList();
		}
		
		referenceDataMap.put("stakeholderBankInfoModelList", stakeholderBankInfoModelList);
		return referenceDataMap;

	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws Exception
	{
		id = ServletRequestUtils.getLongParameter(httpServletRequest, "commissionRateId");
		if (null != id)
		{
			if (log.isDebugEnabled())
			{
				log.debug("id is not null....retrieving object from DB");
			}

			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			CommissionRateModel commissionRateModel = new CommissionRateModel();
			commissionRateModel.setPrimaryKey(id);
			searchBaseWrapper.setBasePersistableModel(commissionRateModel);
			searchBaseWrapper = this.commissionRateManager.loadCommissionRate(searchBaseWrapper);
			CommissionRateModel commRateModel = (CommissionRateModel) searchBaseWrapper.getBasePersistableModel();
			DecimalFormat dFormat = new DecimalFormat("###.##");
			String rate = dFormat.format(commRateModel.getRate());
			httpServletRequest.setAttribute("_rate", rate);
			System.out.println("fdfdsf"+rate);
			
			return (CommissionRateModel) searchBaseWrapper.getBasePersistableModel();
		}
		else
		{
			if (log.isDebugEnabled())
			{
				log.debug("id is null....creating new instance of Model");
			}
			return new CommissionRateModel();
		}
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object object, BindException errors) throws Exception
	{
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
		baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.TX_CHARGES_USECASE_ID));
		CommissionRateModel commissionRateModel = (CommissionRateModel) object;
		try
		{
			
			//httpServletRequest.setAttribute("stakeHolderId", commissionRateModel.getCommissionStakeholderId());
			
			if (httpServletRequest.getParameter("insertPageFlag") != null)
			{
				insertFlag = Integer.parseInt(httpServletRequest.getParameter("insertPageFlag"));
			}
			baseWrapper.putObject("insertFlag", insertFlag);
			
			commissionRateModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			commissionRateModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
			commissionRateModel.setActive(commissionRateModel.getActive() == null ? false
					: commissionRateModel.getActive());
			commissionRateModel.setCommissionStakeholderId(50020l);
			baseWrapper.setBasePersistableModel(commissionRateModel);
			
			//check for commission rate only for operator, if its already exist donot proceed.
			//first get the commissionStakeholder data to check whether it is operator
	
			long commStakeholderId = 50020l;//commissionRateModel.getCommissionStakeholderId();
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			CommissionStakeholderModel commStakeModel = new CommissionStakeholderModel();
			commStakeModel.setCommissionStakeholderId(commStakeholderId);
			searchBaseWrapper.setBasePersistableModel(commStakeModel);
			searchBaseWrapper = commissionStakeholderManager.loadCommissionStakeholder(searchBaseWrapper);
			commStakeModel = (CommissionStakeholderModel)searchBaseWrapper.getBasePersistableModel();
			
			
			// reason is not contrained to only one... same reason can be used multiple times
			 
//			if(commStakeModel.getOperatorId() != null)
//			{
//				//it means this is operator stakeholder, now check its commission rate entry against given product, if already exist then do not proceed.
//				CommissionRateListViewModel commissionRatelistViewModel = new CommissionRateListViewModel();
//				commissionRatelistViewModel.setCommissionStakeholderId(commStakeholderId);
//				commissionRatelistViewModel.setProductId(commissionRateModel.getProductId());
//				commissionRatelistViewModel.setActive(true);
//				commissionRatelistViewModel.setCommissionTypeId(commissionRateModel.getCommissionTypeId());
//				
//				searchBaseWrapper.setBasePersistableModel(commissionRatelistViewModel);
//				searchBaseWrapper = this.commissionRateManager.searchCommissionRate(searchBaseWrapper);
//				
//				if(searchBaseWrapper.getCustomList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() >0)
//				{
//					//entry exist so return an exception;
//					super.saveMessage(httpServletRequest, "Commission Rate already defined against this product.");
//					return super.showForm(httpServletRequest, httpServletResponse, errors);
//				}
//			}			
			
			baseWrapper = this.commissionRateManager.createCommissionRate(baseWrapper);

			if (null != baseWrapper.getBasePersistableModel())
			{
				this.saveMessage(httpServletRequest, "Record saved successfully");
				ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
				return modelAndView;
			}
			else
			{
				insertFlag = 1;
				httpServletRequest.setAttribute("insertFlag", insertFlag);
				this.saveMessage(httpServletRequest,
						"Record already exists");
				return super.showForm(httpServletRequest, httpServletResponse, errors);
			}
		}
		catch (FrameworkCheckedException ex)
		{
			DecimalFormat dFormat = new DecimalFormat("###.##");
			String rate = dFormat.format(commissionRateModel.getRate());							
			httpServletRequest.setAttribute("_rate", rate);
			//commissionRateModel.setRate(Double.valueOf(rate));
			
			if (ex.getMessage().equalsIgnoreCase("UniqueKeyViolated"))
			{
				super.saveMessage(httpServletRequest, "Commission Rate with same segment, product, commission reason, range starts,range ends and commission type already exists.");
				//return super.showForm(httpServletRequest, httpServletResponse, errors);
			}
			else if (ex.getMessage().equalsIgnoreCase("SlabRateExists"))
			{
				super.saveMessage(httpServletRequest, "Same Rate already exists for another Slab Rage against same segment, product, commission reason and commission type.");
				//return super.showForm(httpServletRequest, httpServletResponse, errors);
			}
			
			else if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION == ex.getErrorCode())
			{
				super.saveMessage(httpServletRequest, "Record could not be saved");
				//return super.showForm(httpServletRequest, httpServletResponse, errors);
			}
			else{
				super.saveMessage(httpServletRequest, ex.getMessage());
			}
			
			return super.showForm(httpServletRequest, httpServletResponse, errors);
		}
		catch (Exception ex)
		{
			DecimalFormat dFormat = new DecimalFormat("###.##");
			String rate = dFormat.format(commissionRateModel.getRate());							
			httpServletRequest.setAttribute("_rate", rate);
			
			super.saveMessage(httpServletRequest,MessageUtil.getMessage("6075"));
			return super.showForm(httpServletRequest, httpServletResponse, errors);
		}
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object object, BindException errors) throws Exception
	{
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
		baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.TX_CHARGES_USECASE_ID));
		CommissionRateModel commissionRateModel = (CommissionRateModel) object;
		
		/*String commissionReason = httpServletRequest.getParameter("commissionReason");				
		
		if(commissionReason!=null && !commissionReason.equals(""))
			commissionRateModel.setCommissionReasonId(Long.parseLong(commissionReason));*/
		
		try
		{
			Date nowDate = new Date();
			
			commissionRateModel.setUpdatedOn(nowDate);
			commissionRateModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			commissionRateModel.setActive(commissionRateModel.getActive() == null ? false
					: commissionRateModel.getActive());
			
			
			// *********** Load the commission rate model for the createdOn problem
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			CommissionRateModel tempCommissionRateModel = new CommissionRateModel();
			commissionRateModel.setPrimaryKey(id);
			searchBaseWrapper.setBasePersistableModel(commissionRateModel);
			tempCommissionRateModel = (CommissionRateModel)this.commissionRateManager.loadCommissionRate(searchBaseWrapper).getBasePersistableModel() ;
			// ********************************************************************
			
			commissionRateModel.setCreatedOn( tempCommissionRateModel.getCreatedOn() ) ;
			baseWrapper.setBasePersistableModel(commissionRateModel);
			baseWrapper = this.commissionRateManager.updateCommissionRate(baseWrapper);

			if (null != baseWrapper.getBasePersistableModel())
			{
				this.saveMessage(httpServletRequest, "Record saved successfully");
				ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
				return modelAndView;
			}
			else
			{
				this.saveMessage(httpServletRequest, "Record already exists.");
				return super.showForm(httpServletRequest, httpServletResponse, errors);
			}
		}
		catch (FrameworkCheckedException ex)
		{
			DecimalFormat dFormat = new DecimalFormat("###.##");
			String rate = dFormat.format(commissionRateModel.getRate());
			httpServletRequest.setAttribute("_rate", rate);
			commissionRateModel.setRate(Double.valueOf(rate));
	
			if (ex.getMessage().equalsIgnoreCase("UniqueKeyViolated"))
			{
				super.saveMessage(httpServletRequest, "Commission Rate with same segment, product, commission reason, range starts,range ends and commission type already exists.");
				//return super.showForm(httpServletRequest, httpServletResponse, errors);
			}
			else if (ex.getMessage().equalsIgnoreCase("SlabRateExists"))
			{
				super.saveMessage(httpServletRequest, "Same Rate already exists for another Slab Rage against same segment, product, commission reason and commission type.");
				//return super.showForm(httpServletRequest, httpServletResponse, errors);
			}
			
			else if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION == ex.getErrorCode())
			{
				super.saveMessage(httpServletRequest, "Record could not be saved");
				//return super.showForm(httpServletRequest, httpServletResponse, errors);
			}
			else{
				super.saveMessage(httpServletRequest, ex.getMessage());
			}
			
			return super.showForm(httpServletRequest, httpServletResponse, errors);		
		}
		catch (Exception ex)
		{
			DecimalFormat dFormat = new DecimalFormat("###.##");
			String rate = dFormat.format(commissionRateModel.getRate());
			httpServletRequest.setAttribute("_rate", rate);
			commissionRateModel.setRate(Double.valueOf(rate));
			super.saveMessage(httpServletRequest, MessageUtil.getMessage("6075"));
			return super.showForm(httpServletRequest, httpServletResponse, errors);		
		}
	}

	private void removeChargesLessProducts(List<ProductModel> productModelList)
	{
	    if( productModelList != null && !productModelList.isEmpty() )
        {
	        Iterator<ProductModel> itrProductModel = productModelList.iterator();
	        while( itrProductModel.hasNext() )
            {
                ProductModel productModel = itrProductModel.next();
                long productId = productModel.getProductId().longValue();
                if( productId == ProductConstantsInterface.AGENT_TO_AGENT_TRANSFER || productId == ProductConstantsInterface.RSO_TO_AGENT_TRANSFER )
                {
                    itrProductModel.remove();
                }
            }
        }
	}

	public void setCommissionRateManager(CommissionRateManager commissionRateManager)
	{
		this.commissionRateManager = commissionRateManager;
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
	{
		this.referenceDataManager = referenceDataManager;
	}

	public CommissionStakeholderManager getCommissionStakeholderManager() {
		return commissionStakeholderManager;
	}

	public void setCommissionStakeholderManager(
			CommissionStakeholderManager commissionStakeholderManager) {
		this.commissionStakeholderManager = commissionStakeholderManager;
	}
}
