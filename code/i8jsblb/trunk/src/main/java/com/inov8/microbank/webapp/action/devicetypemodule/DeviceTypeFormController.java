package com.inov8.microbank.webapp.action.devicetypemodule;

import java.util.Date;
import java.util.HashMap;
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
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.DeviceTypeModel;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.devicemodule.DeviceTypeManager;


public class DeviceTypeFormController extends AdvanceFormController
{
	private DeviceTypeManager deviceTypeManager;

	  private ReferenceDataManager referenceDataManager;

	  private Long id;

	  public DeviceTypeFormController()
	  {
	    setCommandName("deviceTypeModel");
	    setCommandClass(DeviceTypeModel.class);
	  }

	  @Override
	  protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws
	      Exception
	  {
	    ReferenceDataWrapper referenceDataWrapper ;
	    Map referenceDataMap = new HashMap();

	    return referenceDataMap;
	  }
	  
	  
	  @Override
	  protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws
	      Exception
	  {
	     id = ServletRequestUtils.getLongParameter(httpServletRequest, "deviceTypeId");
	        if (null != id)
	        {
	            if (log.isDebugEnabled())
	            {
	                log.debug("id is not null....retrieving object from DB");
	            }

	            BaseWrapper baseWrapper = new BaseWrapperImpl();
	            DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
	            deviceTypeModel.setDeviceTypeId(id);
	            baseWrapper.setBasePersistableModel(deviceTypeModel);
	            baseWrapper = this.deviceTypeManager.loadDeviceType(baseWrapper);
	            
	            return (DeviceTypeModel) baseWrapper.getBasePersistableModel();
	        }
	        else
	        {
	            if (log.isDebugEnabled())
	            {
	                log.debug("id is null....creating new instance of Model");
	            }

	            return new DeviceTypeModel();
	        }
	  }
	  
	  @Override
	  protected ModelAndView onCreate(HttpServletRequest httpServletRequest,
	                                  HttpServletResponse httpServletResponse,
	                                  Object object, BindException bindException) throws
	      Exception
	  {
	    return this.createOrUpdate(httpServletRequest, httpServletResponse, object,
	                               bindException);
	  }
	  @Override
	  protected ModelAndView onUpdate(HttpServletRequest httpServletRequest,
	                                  HttpServletResponse httpServletResponse,
	                                  Object object, BindException bindException) throws
	      Exception
	  {
	    return this.createOrUpdate(httpServletRequest, httpServletResponse, object,
	                               bindException);
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
	  private ModelAndView createOrUpdate(HttpServletRequest request,
	                                      HttpServletResponse response,
	                                      Object command,
	                                      BindException errors) throws Exception
	  {
	    try
	    {
	      BaseWrapper baseWrapper = new BaseWrapperImpl();
	      DeviceTypeModel deviceTypeModel = (DeviceTypeModel) command;

	      if(null != id)//To check edit/create case
	      {
	    	  DeviceTypeModel tempDeviceTypeModel ;
	    	  
	          baseWrapper.setBasePersistableModel(deviceTypeModel);
	          tempDeviceTypeModel = (DeviceTypeModel)this.deviceTypeManager.loadDeviceType(baseWrapper).getBasePersistableModel();
	    	  
	          deviceTypeModel.setUpdatedOn(new Date());
	          deviceTypeModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
	          deviceTypeModel.setCreatedBy(tempDeviceTypeModel.getCreatedBy()) ;
	          deviceTypeModel.setCreatedOn(tempDeviceTypeModel.getCreatedOn()) ;	    	  
	    	  
	      }
	      else
	      {	        
	    	  deviceTypeModel.setUpdatedOn(new Date());
	    	  deviceTypeModel.setCreatedOn(new Date());
	    	  deviceTypeModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
	    	  deviceTypeModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
	      }

	      baseWrapper.setBasePersistableModel(deviceTypeModel);
	      baseWrapper = this.deviceTypeManager.createOrUpdateDeviceType(baseWrapper);
	      //***Check if record already exists.

	     if(null!=baseWrapper.getBasePersistableModel())
	     {//if not found
	       this.saveMessage(request, "Record saved successfully");
	       ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
	       return modelAndView;
	     }
	     else
	     {
	       this.saveMessage(request, "Device Type with the same name already exists.");
	       return super.showForm(request, response,errors);
	     }
	    }
	    catch (FrameworkCheckedException ex)
	    {
	      if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION ==
	          ex.getErrorCode())
	      {

	        super.saveMessage(request, "Record could not be saved.");
	        return super.showForm(request, response, errors);
	      }

	      throw ex;
	    }
	    catch (Exception ex)
	    {
	        super.saveMessage(request, MessageUtil.getMessage("6075"));
	        return super.showForm(request, response, errors);
	    }
	  }  

	  public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
	  {
	    this.referenceDataManager = referenceDataManager;
	  }
	

	public void setDeviceTypeManager(DeviceTypeManager deviceTypeManager) {
		this.deviceTypeManager = deviceTypeManager;
	}

}
