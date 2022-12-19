package com.inov8.microbank.webapp.action.servicetypemodule;

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
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.ServiceTypeModel;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.servicetypemodule.ServiceTypeManager;

public class ServiceTypeFormController extends  AdvanceFormController {

	
	private ServiceTypeManager serviceTypeManager;
	private Long id;
	
	
	public ServiceTypeFormController() {
		setCommandName("serviceTypeModel");
		setCommandClass(ServiceTypeModel.class);
	}
	
	
	@Override
	protected Object loadFormBackingObject(HttpServletRequest request) throws Exception {
		
		id = ServletRequestUtils.getLongParameter(request,"serviceTypeId");
		if (null != id) 
		{
			if (log.isDebugEnabled()) 
			{
				log.debug("id is not null....retrieving object from DB");
			}

			BaseWrapper baseWrapper = new BaseWrapperImpl();
			ServiceTypeModel serviceTypeModel = new ServiceTypeModel();
			serviceTypeModel.setPrimaryKey(id);
			baseWrapper.setBasePersistableModel(serviceTypeModel);
			baseWrapper=this.serviceTypeManager.loadServiceType(baseWrapper);
					
			return (ServiceTypeModel) baseWrapper.getBasePersistableModel();
			} 
		else 
		{
			if (log.isDebugEnabled()) 
			{
				log.debug("id is null....creating new instance of Model");
			}

				return new ServiceTypeModel();
		}
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest arg0) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse response, Object object, BindException errors) throws Exception {
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();
      	ServiceTypeModel serviceTypeModel = new ServiceTypeModel();
      	
      	serviceTypeModel.setName(((ServiceTypeModel)object).getName());
      	serviceTypeModel.setComments(((ServiceTypeModel)object).getComments());
      	serviceTypeModel.setDescription(((ServiceTypeModel)object).getDescription());      	
      	serviceTypeModel.setCreatedOn(new Date());
      	serviceTypeModel.setUpdatedOn(new Date());
      	serviceTypeModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
      	serviceTypeModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
		baseWrapper.setBasePersistableModel(serviceTypeModel);
      
       
       try{
  		 baseWrapper = this.serviceTypeManager.updateServiceType(baseWrapper);
  	        
  	        if (null != baseWrapper.getBasePersistableModel())
  	        { //if not found
  	          this.saveMessage(request, "Record saved successfully");
  	          ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
  	          return modelAndView;
  	        }
  	        else
  	        {
  	          this.saveMessage(request,
  	                           "Service Type with the same name already exists.");
  	          return super.showForm(request, response, errors);
  	        }
  		 }
  		    catch (FrameworkCheckedException ex)
  		    {
  		      if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION ==
  		          ex.getErrorCode())
  		      {

  		        super.saveMessage(request,
  		                          "Service Type with the same name already exists.");
  		        return super.showForm(request, response, errors);
  		      }
  		      else
  		      {

  		        super.saveMessage(request,
  		                          "Record could not be saved.");
  		        return super.showForm(request, response, errors);
  		      }
  		    }
       catch (Exception e) {
			e.printStackTrace();
			super.saveMessage(request, MessageUtil.getMessage("6075"));
			return super.showForm(request, response, errors);
		}

	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object object, BindException errors) throws Exception {
	
	
		if (log.isDebugEnabled())
	    {
	      log.debug("Execution of onToggleActivate started...");
	    }

	    Long id = ServletRequestUtils.getLongParameter(request,"serviceTypeId");
	    
	    if (null != id)
	    {
	      if (log.isDebugEnabled())
	      {
	        log.debug(
	            "id is not null....retrieving object from DB and then updating it");
	      }

	      	BaseWrapper baseWrapper = new BaseWrapperImpl();
	      	ServiceTypeModel serviceTypeModel = new ServiceTypeModel();
	      	serviceTypeModel.setPrimaryKey(id);
	      	baseWrapper.setBasePersistableModel(serviceTypeModel);
	      	baseWrapper=this.serviceTypeManager.loadServiceType(baseWrapper);
			
	      	ServiceTypeModel serviceTypeModelNew=new ServiceTypeModel();
	      	serviceTypeModelNew=(ServiceTypeModel)baseWrapper.getBasePersistableModel();

			
	      	serviceTypeModelNew.setName(((ServiceTypeModel)object).getName());
	      	serviceTypeModelNew.setComments(((ServiceTypeModel)object).getComments());
	      	serviceTypeModelNew.setDescription(((ServiceTypeModel)object).getDescription());
	      	
	      	serviceTypeModelNew.setUpdatedOn(new Date());
	      	serviceTypeModelNew.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
	      	
			baseWrapper.setBasePersistableModel(serviceTypeModelNew);
	      
	       
	       try{
	  		 baseWrapper = this.serviceTypeManager.updateServiceType(baseWrapper);
	  	        
	  	        if (null != baseWrapper.getBasePersistableModel())
	  	        { //if not found
	  	          this.saveMessage(request, "Record saved successfully");
	  	          ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
	  	          return modelAndView;
	  	        }
	  	        else
	  	        {
	  	          this.saveMessage(request,
	  	                           "Service Type name already exists. Please choose a different name.");
	  	          return super.showForm(request, response, errors);
	  	        }
	  		 }
	  		    catch (FrameworkCheckedException ex)
	  		    {
	  		      if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION ==
	  		          ex.getErrorCode())
	  		      {

	  		        super.saveMessage(request,
	  		                          "Service Type name already exists. Please choose a different name.");
	  		        return super.showForm(request, response, errors);
	  		      }
	  		      else
	  		      {

	  		        super.saveMessage(request,
	  		                          "Record could not be saved.");
	  		        return super.showForm(request, response, errors);
	  		      }
	  		    }
	       catch (Exception e) {
				e.printStackTrace();
				super.saveMessage(request, MessageUtil.getMessage("6075"));
				return super.showForm(request, response, errors);
			}

	    }
		
		return null;
		
			}

	public void setServiceTypeManager(ServiceTypeManager serviceTypeManager) {
		this.serviceTypeManager = serviceTypeManager;
	}


	
	
}
