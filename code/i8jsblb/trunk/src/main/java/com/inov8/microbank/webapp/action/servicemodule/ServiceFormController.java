package com.inov8.microbank.webapp.action.servicemodule;

import java.util.Date;
import java.util.HashMap;
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
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.ServiceModel;
import com.inov8.microbank.common.model.ServiceTypeModel;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.servicemodule.ServiceManager;

public class ServiceFormController extends AdvanceFormController
{
	
	private ServiceManager serviceManager;

	  private ReferenceDataManager referenceDataManager;

	  private Long id;

	  public ServiceFormController()
	  {
	    setCommandName("serviceModel");
	    setCommandClass(ServiceModel.class);
	  }

	  @Override
	  protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws
	      Exception
	  {
	    ReferenceDataWrapper referenceDataWrapper ;
	    Map referenceDataMap = new HashMap();

	    // ************* Load Service Type data *******************************************
	    ServiceTypeModel serviceTypeModel = new ServiceTypeModel();
	    referenceDataWrapper = new ReferenceDataWrapperImpl(serviceTypeModel, "name", SortingOrder.ASC);
	    referenceDataManager.getReferenceData(referenceDataWrapper);

	    List<ServiceTypeModel> serviceTypeModelList = null;
	    if (referenceDataWrapper.getReferenceDataList() != null)
	    {
	    	serviceTypeModelList = referenceDataWrapper.getReferenceDataList();
	    }	    
	    referenceDataMap.put("serviceTypeModelList", serviceTypeModelList);
	    // ********************************************************************************

	    return referenceDataMap;
	  }
	  
	  @Override
	  protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws
	      Exception
	  {
	     id = ServletRequestUtils.getLongParameter(httpServletRequest, "serviceId");
	        if (null != id)
	        {
	            if (log.isDebugEnabled())
	            {
	                log.debug("id is not null....retrieving object from DB");
	            }

	            BaseWrapper baseWrapper = new BaseWrapperImpl();
	            ServiceModel serviceModel = new ServiceModel();
	            serviceModel.setServiceId(id);
	            baseWrapper.setBasePersistableModel(serviceModel);
	            baseWrapper = this.serviceManager.loadService(baseWrapper);
	            
	            return (ServiceModel) baseWrapper.getBasePersistableModel();
	        }
	        else
	        {
	            if (log.isDebugEnabled())
	            {
	                log.debug("id is null....creating new instance of Model");
	            }

	            return new ServiceModel();
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
	      ServiceModel serviceModel = (ServiceModel) command;

	      if(null != id)//To check edit/create case
	      {
	    	  ServiceModel tempServiceModel ;
	    	  
	          baseWrapper.setBasePersistableModel(serviceModel);
	          tempServiceModel = (ServiceModel)this.serviceManager.loadService(baseWrapper).getBasePersistableModel();
	    	  
	    	  serviceModel.setUpdatedOn(new Date());
	    	  serviceModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
	    	  serviceModel.setCreatedBy(tempServiceModel.getCreatedBy()) ;
	    	  serviceModel.setCreatedOn(tempServiceModel.getCreatedOn()) ;	    	  
	    	  
	      }
	      else
	      {	        
	    	  serviceModel.setUpdatedOn(new Date());
	    	  serviceModel.setCreatedOn(new Date());
	    	  serviceModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
	    	  serviceModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
	      }
	      
	      serviceModel.setActive( serviceModel.getActive() == null ? false : serviceModel.getActive() ) ;

	      baseWrapper.setBasePersistableModel(serviceModel);
	      baseWrapper = this.serviceManager.createOrUpdateService(baseWrapper);
	      //***Check if record already exists.

	     if(null!=baseWrapper.getBasePersistableModel())
	     {//if not found
	       this.saveMessage(request, "Record saved successfully");
	       ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
	       return modelAndView;
	     }
	     else
	     {
	       this.saveMessage(request, "Service with the same name already exists.");
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
	    catch (Exception e) {
			e.printStackTrace();
			super.saveMessage(request, MessageUtil.getMessage("6075"));
			return super.showForm(request, response, errors);
		}
	  }  

	  public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
	  {
	    this.referenceDataManager = referenceDataManager;
	  }

	public void setServiceManager(ServiceManager serviceManager) {
		this.serviceManager = serviceManager;
	}

}
