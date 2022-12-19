package com.inov8.microbank.webapp.action.portal.velocityrulemodule;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.microbank.server.dao.velocitymodule.VelocityRuleModelDAO;
import org.apache.commons.validator.GenericValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
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
import com.inov8.microbank.common.model.DeviceTypeModel;
import com.inov8.microbank.common.model.DistributorLevelModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.common.model.VelocityRuleModel;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.facade.portal.velocityrulemodule.VelocityRuleFacade;
import com.inov8.microbank.server.service.agenthierarchy.AgentHierarchyManager;
import com.inov8.microbank.server.service.devicemodule.DeviceTypeManager;

public class VelocityRuleFormController extends AdvanceFormController {
	
	private static final Logger LOGGER = Logger.getLogger( VelocityRuleFormController.class );
	private VelocityRuleFacade velocityRuleFacade;
	private ReferenceDataManager referenceDataManager;
	private DeviceTypeManager deviceTypeManager;
	private VelocityRuleModelDAO velocityRuleModelDAO;
	@Autowired
	private AgentHierarchyManager	agentHierarchyManager;
	
	public VelocityRuleFormController() {
		setCommandName("velocityRuleModel");
		setCommandClass(VelocityRuleModel.class);
	}

	@Override
	protected Map<String,Object> loadReferenceData(HttpServletRequest request) throws Exception
	{
		 Map referenceDataMap = new HashMap();
		 ReferenceDataWrapper referenceDataWrapper;
		 
		 String distributorId = ServletRequestUtils.getStringParameter(request, "distributorId");

		 List<ProductModel> productModelList = null;
		 ProductModel productModel = new ProductModel(); 
		 productModel.setActive(true);
		 
		 List<SegmentModel> segmentModelLsit = null;
		 SegmentModel segmentModel = new SegmentModel();
		 segmentModel.setIsActive(true);
		 
		 List<DeviceTypeModel> deviceTypeModelList = null;
		 DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
		 deviceTypeModel.setActive(true);
		 
		 List<DistributorModel> distributorModelList = null;
		 DistributorModel	distributorModel = new DistributorModel();
		 distributorModel.setActive(true);
		 
		 
		 List<DistributorLevelModel> distributorLevelModelList = null;
		 DistributorLevelModel	distributorLevelModel = new DistributorLevelModel();
		 distributorLevelModel.setActive(true);

		 List<OlaCustomerAccountTypeModel> olaCustomerAccountTypeModelList = null;
		 OlaCustomerAccountTypeModel olaCustomerAccountTypeModel = new OlaCustomerAccountTypeModel();
		 olaCustomerAccountTypeModel.setActive(true);

		 
		 referenceDataWrapper = new ReferenceDataWrapperImpl(productModel, "name", SortingOrder.DESC);
		 try
		    {
		      referenceDataManager.getReferenceData(referenceDataWrapper);
		    }
		    catch (FrameworkCheckedException ex1)
		    {
		    	ex1.printStackTrace();
		    }
		 if (null != referenceDataWrapper.getReferenceDataList() )
		  {
			 productModelList = referenceDataWrapper.getReferenceDataList();
		  }
		 
		 referenceDataMap.put("productList", productModelList);
		 
		 ////////////////////////////////////////////////////////////
		 referenceDataWrapper = new ReferenceDataWrapperImpl(segmentModel, "name", SortingOrder.DESC);
		 try
		    {
		      referenceDataManager.getReferenceData(referenceDataWrapper);
		    }
		    catch (FrameworkCheckedException ex1)
		    {
		    	ex1.printStackTrace();
		    }
		 if (null != referenceDataWrapper.getReferenceDataList())
		  {
			segmentModelLsit = referenceDataWrapper.getReferenceDataList();
		  }
		 referenceDataMap.put("segmentList", segmentModelLsit);
		
		 ////////////////////////////////////////////////////////////
		deviceTypeModelList = deviceTypeManager.searchDeviceTypes(DeviceTypeConstantsInterface.ALL_PAY, DeviceTypeConstantsInterface.BANKING_MIDDLEWARE,
				DeviceTypeConstantsInterface.ALLPAY_WEB, DeviceTypeConstantsInterface.WEB_SERVICE);
		referenceDataMap.put("deviceTypeList", deviceTypeModelList);
		 ////////////////////////////////////////////////////////////
		 referenceDataWrapper = new ReferenceDataWrapperImpl(distributorModel, "name", SortingOrder.DESC);
		 try
		    {
		      referenceDataManager.getReferenceData(referenceDataWrapper);
		    }
		    catch (FrameworkCheckedException ex1)
		    {
		    	ex1.printStackTrace();
		    }
		 if (null != referenceDataWrapper.getReferenceDataList())
		  {
			 distributorModelList = referenceDataWrapper.getReferenceDataList();
		  }
		 
		 referenceDataMap.put("distributorList", distributorModelList);
		////////////////////////////////////////////////////////////

		//ACccount Type Limit
		referenceDataWrapper = new ReferenceDataWrapperImpl(olaCustomerAccountTypeModel, "name", SortingOrder.DESC);

		try {
			referenceDataManager.getReferenceData(referenceDataWrapper);
		}
		catch (FrameworkCheckedException ex1) {
			ex1.printStackTrace();
		}

		if (null != referenceDataWrapper.getReferenceDataList()) {
			olaCustomerAccountTypeModelList = referenceDataWrapper.getReferenceDataList();
		}
		referenceDataMap.put("olaCustomerAccountTypeList", olaCustomerAccountTypeModelList);


		if( !GenericValidator.isBlankOrNull(distributorId) )
		 {
			SearchBaseWrapper searchBaseWrapper=new SearchBaseWrapperImpl();
			searchBaseWrapper.setBasePersistableModel(new DistributorModel(Long.parseLong(distributorId)));
			searchBaseWrapper	=	agentHierarchyManager.findDistributorLevelsByDistributorId(searchBaseWrapper);
			
			if(searchBaseWrapper.getCustomList()!=null && searchBaseWrapper.getCustomList().getResultsetList()!=null )
			{
				distributorLevelModelList	=	searchBaseWrapper.getCustomList().getResultsetList();
			}
		 }
		
		 referenceDataMap.put("distributorLevelList", distributorLevelModelList);	
		return referenceDataMap;
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest httpServletRequest)
			throws Exception
	{

		Long velocityRuleId = ServletRequestUtils.getLongParameter(httpServletRequest, "velocityRuleId");
		if (null != velocityRuleId)
		{
			if (log.isDebugEnabled())
			{
				log.debug("id is not null....retrieving object from DB");
			}

			VelocityRuleModel velocityRuleModel = velocityRuleFacade.loadVelocityRuleModel(velocityRuleId);			
			return velocityRuleModel;
		}
		else
		{
			if(log.isDebugEnabled())
			{
				log.debug("id is null....creating new instance of Model");
			}

			return new VelocityRuleModel();
		}
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object,
			BindException bindException) throws Exception
	{
		VelocityRuleModel velocityRuleModel = (VelocityRuleModel) object;
		return this.createOrUpdate(httpServletRequest, httpServletResponse,velocityRuleModel, bindException);
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object,
			BindException bindException) throws Exception
	{
		VelocityRuleModel velocityRuleModel = (VelocityRuleModel) object;
		return this.createOrUpdate(httpServletRequest, httpServletResponse, velocityRuleModel, bindException);
	}

	private ModelAndView createOrUpdate(HttpServletRequest request, HttpServletResponse response, VelocityRuleModel model,
			BindException errors) throws Exception
	{
		ModelAndView modelAndView = null;
		try
		{
			BaseWrapper baseWrapper = new BaseWrapperImpl();

			long theDate = new Date().getTime();	
			
			VelocityRuleModel velocityRuleModelTemp = new VelocityRuleModel();
			
			velocityRuleModelTemp.setProductId( model.getProductId());
			velocityRuleModelTemp.setDeviceTypeId( model.getDeviceTypeId());
			velocityRuleModelTemp.setSegmentId(model.getSegmentId());
			velocityRuleModelTemp.setDistributorId(model.getDistributorId());
			velocityRuleModelTemp.setDistributorLevelId(model.getDistributorLevelId());
			velocityRuleModelTemp.setLimitTypeId(model.getLimitTypeId());
			velocityRuleModelTemp.setIsActive(model.getIsActive());
			velocityRuleModelTemp.setVelocityRuleId(model.getVelocityRuleId());
			//Account Type Limit
			velocityRuleModelTemp.setCustomerAccountTypeId(model.getCustomerAccountTypeId());

			if(!model.getIsActive()){
				velocityRuleModelDAO.update(velocityRuleModelTemp);
			}
			else {
				List<VelocityRuleModel> recordList = velocityRuleFacade.findByCriteria(velocityRuleModelTemp);

				if (null != recordList && recordList.size() > 1) {
					throw new FrameworkCheckedException("Same record already exists");
				}
				if (null != recordList && recordList.size() > 0 && (null == model.getVelocityRuleId())) {
					throw new FrameworkCheckedException("Same record already exists");
				} else if (null != recordList && recordList.size() == 1 && (null != model.getVelocityRuleId())) {

					if (model.getVelocityRuleId().intValue() != recordList.get(0).getVelocityRuleId()) {
						throw new FrameworkCheckedException("Same record already exists");
					}
				}

			}
			if (null != model.getVelocityRuleId())
			{	
				model.setUpdatedOn(new Date());
				model.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
				baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
				baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.UPDATE_VELOCITY_RULE_USECASE_ID));
			}
			else
			{
				model.setCreatedOn(new Date(theDate));
				model.setUpdatedOn(new Date(theDate));
				model.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
				model.setCreatedByAppUserModel(UserUtils.getCurrentUser());
				baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
				baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.CREATE_VELOCITY_RULE_USECASE_ID));
			}

			model.setIsActive(model.getIsActive() == null ? false : model.getIsActive());
			baseWrapper.setBasePersistableModel(model);
			baseWrapper = this.velocityRuleFacade.saveOrUpdate(baseWrapper);
			this.saveMessage(request, "Record saved successfully");
			modelAndView = new ModelAndView(this.getSuccessView());
			
		}
		
		catch (FrameworkCheckedException ex)
		{
			LOGGER.error("Exception occured while resending sms : " +ex.getMessage());
			super.saveMessage(request, ex.getMessage());
			return super.showForm(request, response, errors);
			
		}
		catch (Exception ex)
		{
			LOGGER.error("Exception occured while resending sms : " +ex.getMessage());
			super.saveMessage(request, MessageUtil.getMessage("6075"));
			return super.showForm(request, response, errors);
			
		}
		
		return modelAndView;
	}
	
	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public void setVelocityRuleFacade(VelocityRuleFacade velocityRuleFacade) {
		this.velocityRuleFacade = velocityRuleFacade;
	}

	public void setDeviceTypeManager(DeviceTypeManager deviceTypeManager) {
		this.deviceTypeManager = deviceTypeManager;
	}

	public void setAgentHierarchyManager(AgentHierarchyManager agentHierarchyManager) {
		this.agentHierarchyManager = agentHierarchyManager;
	}

	public void setVelocityRuleModelDAO(VelocityRuleModelDAO velocityRuleModelDAO) {
		this.velocityRuleModelDAO = velocityRuleModelDAO;
	}
}

	


