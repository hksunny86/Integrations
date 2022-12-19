package com.inov8.microbank.webapp.action.portal.limitmodule;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.microbank.common.model.LimitRuleModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.ola.server.facade.LimitFacade;

public class ExcludeLimitFormController extends AdvanceFormController {
	
	private static final Logger LOGGER = Logger.getLogger( ExcludeLimitFormController.class );
	private LimitFacade limitFacade;
	private ReferenceDataManager referenceDataManager;
	
	public ExcludeLimitFormController() {
		setCommandName("limitRuleModel");
		setCommandClass(LimitRuleModel.class);
	}

	@Override
	protected Map<String,Object> loadReferenceData(HttpServletRequest request) throws Exception
	{
		 Map referenceDataMap = new HashMap();
		 ReferenceDataWrapper referenceDataWrapper;
		 
		 List<ProductModel> productModelList = null;
		 ProductModel productModel = new ProductModel();
		 productModel.setActive(true);
		 
		 List<SegmentModel> segmentModelLsit = null;
		 SegmentModel segmentModel = new SegmentModel();
		 segmentModel.setIsActive(true);
		 
		 List<OlaCustomerAccountTypeModel> accountTypeModelList = null;
		 OlaCustomerAccountTypeModel alaCustomerAccountTypeModel = new OlaCustomerAccountTypeModel();
		 alaCustomerAccountTypeModel.setActive(true);
		
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
		 referenceDataWrapper = new ReferenceDataWrapperImpl(alaCustomerAccountTypeModel, "name", SortingOrder.DESC);
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
			 accountTypeModelList = referenceDataWrapper.getReferenceDataList();
		  }
		 
		 referenceDataMap.put("accountTypeList", accountTypeModelList);
		 
		 return referenceDataMap;
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest httpServletRequest)
			throws Exception
	{

		Long limitRuleId = ServletRequestUtils.getLongParameter(httpServletRequest, "limitRuleId");
		if (null != limitRuleId)
		{
			if (log.isDebugEnabled())
			{
				log.debug("id is not null....retrieving object from DB");
			}

			LimitRuleModel limitRuleModel = limitFacade.loadLimitRuleModel(limitRuleId);			
			return limitRuleModel;
		}
		else
		{
			if(log.isDebugEnabled())
			{
				log.debug("id is null....creating new instance of Model");
			}

			return new LimitRuleModel();
		}
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object,
			BindException bindException) throws Exception
	{
		LimitRuleModel limitRuleModel = (LimitRuleModel) object;
		return this.createOrUpdate(httpServletRequest, httpServletResponse,limitRuleModel, bindException);
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object,
			BindException bindException) throws Exception
	{
		LimitRuleModel limitRuleModel = (LimitRuleModel) object;
		return this.createOrUpdate(httpServletRequest, httpServletResponse, limitRuleModel, bindException);
	}

	private ModelAndView createOrUpdate(HttpServletRequest request, HttpServletResponse response, LimitRuleModel model,
			BindException errors) throws Exception
	{
		ModelAndView modelAndView = null;
		try
		{
			BaseWrapper baseWrapper = new BaseWrapperImpl();

			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();

			long theDate = new Date().getTime();
					
			if (null != model.getLimitRuleId())
			{	
				model.setUpdatedOn(new Date());
				model.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
				baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
				baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.UPDATE_EXCLUDE_LIMIT_USECASE_ID));
			}
			else
			{
				model.setCreatedOn(new Date(theDate));
				model.setUpdatedOn(new Date(theDate));
				model.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
				model.setCreatedByAppUserModel(UserUtils.getCurrentUser());
				baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
				baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.CREATE_EXCLUDE_LIMIT_USECASE_ID));
			}

			model.setIsActive(model.getIsActive() == null ? false : model.getIsActive());
			baseWrapper.setBasePersistableModel(model);
			baseWrapper = this.limitFacade.saveOrUpdateLimitRule(baseWrapper);
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
	public void setLimitFacade(LimitFacade limitFacade) {
		this.limitFacade = limitFacade;
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}		

}	

	

