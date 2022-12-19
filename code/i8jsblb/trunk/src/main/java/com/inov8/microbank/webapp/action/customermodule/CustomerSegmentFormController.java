package com.inov8.microbank.webapp.action.customermodule;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.ExceptionErrorCodes;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.common.util.MessageConstantsInterface;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.customermodule.CustomerManager;

public class CustomerSegmentFormController extends AdvanceFormController {
	
	private CustomerManager customerManager;
	private Long id;

	public CustomerSegmentFormController() {
		setCommandName("segmentModel");
		setCommandClass(SegmentModel.class);
	}

	@Override
	protected Map<String,Object> loadReferenceData(HttpServletRequest request) throws Exception
	{
		return null;
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest httpServletRequest)
			throws Exception
	{
		id = ServletRequestUtils.getLongParameter(httpServletRequest, "segmentId");
		if (null != id)
		{
			if (log.isDebugEnabled())
			{
				log.debug("id is not null....retrieving object from DB");
			}

			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			SegmentModel segmentModel = new SegmentModel();
			segmentModel.setPrimaryKey(id);
			searchBaseWrapper.setBasePersistableModel(segmentModel);
			searchBaseWrapper = this.customerManager.loadCustomerSegment(searchBaseWrapper);
			segmentModel = (SegmentModel) searchBaseWrapper.getBasePersistableModel();
			return segmentModel;
		}
		else
		{
			if(log.isDebugEnabled())
			{
				log.debug("id is null....creating new instance of Model");
			}

			return new SegmentModel();
		}
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object,
			BindException bindException) throws Exception
	{
		SegmentModel segmentModel = (SegmentModel) object;
		return this.createOrUpdate(httpServletRequest, httpServletResponse,segmentModel, bindException);
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object,
			BindException bindException) throws Exception
	{
		SegmentModel segmentModel = (SegmentModel) object;
		return this.createOrUpdate(httpServletRequest, httpServletResponse, segmentModel, bindException);
	}

	private ModelAndView createOrUpdate(HttpServletRequest request, HttpServletResponse response, SegmentModel segmentModel,
			BindException errors) throws Exception
	{
		ModelAndView modelAndView = null;
		try
		{
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.CUSTOMER_SEGMENT_USECASE_ID));

			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();

			long theDate = new Date().getTime();

			if (null != id)
			{
				SegmentModel tempSegmentModel;
				searchBaseWrapper.setBasePersistableModel(segmentModel);
				searchBaseWrapper = this.customerManager.loadCustomerSegment(searchBaseWrapper);
				tempSegmentModel = (SegmentModel) searchBaseWrapper.getBasePersistableModel();
				segmentModel.setCreatedOn(tempSegmentModel.getCreatedOn());
				segmentModel.setCreatedByAppUserModel(tempSegmentModel.getCreatedByAppUserModel());
				segmentModel.setUpdatedOn(new Date());
				segmentModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			}
			else
			{
				segmentModel.setCreatedOn(new Date(theDate));
				segmentModel.setUpdatedOn(new Date(theDate));
				segmentModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
				segmentModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
			}

			segmentModel.setIsActive(segmentModel.getIsActive() == null ? false : segmentModel.getIsActive());
			baseWrapper.setBasePersistableModel(segmentModel);
			baseWrapper = this.customerManager.createOrUpdateCustomerSegment(baseWrapper);
			this.saveMessage(request, "Record saved successfully");
			modelAndView = new ModelAndView(this.getSuccessView());
		}
		catch (FrameworkCheckedException ex)
		{
			if( MessageConstantsInterface.ERROR_MSG_SEG_CUST_EXISTS.equals(ex.getMessage())
					|| MessageConstantsInterface.ERROR_MSG_DUPLICATE_SEGMENT.equals(ex.getMessage()))
			{
				super.saveMessage(request, ex.getMessage());
				modelAndView = super.showForm(request, response, errors);
			}
			else if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION == ex.getErrorCode())
			{
				super.saveMessage(request, "Record could not be saved.");
				modelAndView = super.showForm(request, response, errors);
			}
			else
			{
				throw ex;
			}
		}
		catch (Exception ex)
		{
			super.saveMessage(request, MessageUtil.getMessage("6075"));
			modelAndView = super.showForm(request, response, errors);	
		}
		return modelAndView;
	}

	public void setCustomerManager(CustomerManager customerManager)
	{
		this.customerManager = customerManager;
	}


}
