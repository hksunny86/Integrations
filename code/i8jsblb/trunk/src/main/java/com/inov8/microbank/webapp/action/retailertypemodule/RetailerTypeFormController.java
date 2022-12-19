package com.inov8.microbank.webapp.action.retailertypemodule;

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
import com.inov8.microbank.common.model.RetailerTypeModel;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.retailertypemodule.RetailerTypeManager;

public class RetailerTypeFormController extends AdvanceFormController {

	private RetailerTypeManager retailerTypeManager;
	private Long id;
	
	
	public RetailerTypeFormController() {
		setCommandName("retailerTypeModel");
		setCommandClass(RetailerTypeModel.class);
	}
	
	
	@Override
	protected Object loadFormBackingObject(HttpServletRequest request) throws Exception {
		id = ServletRequestUtils.getLongParameter(request,"retailerTypeId");
		if (null != id) 
		{
			if (log.isDebugEnabled()) 
			{
				log.debug("id is not null....retrieving object from DB");
			}

			BaseWrapper baseWrapper = new BaseWrapperImpl();
			RetailerTypeModel retailerTypeModel = new RetailerTypeModel();
			retailerTypeModel.setPrimaryKey(id);
			baseWrapper.setBasePersistableModel(retailerTypeModel);
			baseWrapper=this.retailerTypeManager.loadRetailerType(baseWrapper);
			
					
			return (RetailerTypeModel) baseWrapper.getBasePersistableModel();
			} 
		else 
		{
			if (log.isDebugEnabled()) 
			{
				log.debug("id is null....creating new instance of Model");
			}

				return new RetailerTypeModel();
		}
	}

	
	
	@Override
	protected Map loadReferenceData(HttpServletRequest arg0) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse response, Object object, BindException errors) throws Exception {
		
		RetailerTypeModel retailerTypeModelnew=new RetailerTypeModel();
        BaseWrapper  baseWrapper=new BaseWrapperImpl();
		retailerTypeModelnew.setName(((RetailerTypeModel)object).getName());
      	 retailerTypeModelnew.setComments(((RetailerTypeModel)object).getComments());
      	 retailerTypeModelnew.setDescription(((RetailerTypeModel)object).getDescription());
      	 retailerTypeModelnew.setCreatedOn(new Date());
      	 retailerTypeModelnew.setUpdatedOn(new Date());
      	 retailerTypeModelnew.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
      	 retailerTypeModelnew.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
		 baseWrapper.setBasePersistableModel(retailerTypeModelnew);
		 
		 try{
		 baseWrapper = this.retailerTypeManager.updateRetailerType(baseWrapper);
	        
	        if (null != baseWrapper.getBasePersistableModel())
	        { //if not found
	          this.saveMessage(request, "Record saved successfully");
	          ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
	          return modelAndView;
	        }
	        else
	        {
	          this.saveMessage(request,
	                           "Retailer Type name already exists. Please choose a different name.");
	          return super.showForm(request, response, errors);
	        }
		 }
		    catch (FrameworkCheckedException ex)
		    {
		      if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION ==
		          ex.getErrorCode())
		      {

		        super.saveMessage(request,
		                          "Retailer Type name already exists. Please choose a different name.");
		        return super.showForm(request, response, errors);
		      }
		      else
		      {

		        super.saveMessage(request,
		                          "Record could not be saved.");
		        return super.showForm(request, response, errors);
		      }
		    }
		 	catch (Exception ex)
		    {
		        super.saveMessage(request,MessageUtil.getMessage("6075"));
		        return super.showForm(request, response, errors);
		    }
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object object, BindException errors) throws Exception {
		if (log.isDebugEnabled())
	    {
	      log.debug("Execution of onToggleActivate started...");
	    }

	    Long id = ServletRequestUtils.getLongParameter(request,"retailerTypeId");
	    
	    if (null != id)
	    {
	      if (log.isDebugEnabled())
	      {
	        log.debug(
	            "id is not null....retrieving object from DB and then updating it");
	      }

	      	BaseWrapper baseWrapper = new BaseWrapperImpl();
	      	RetailerTypeModel retailerTypeModel = new RetailerTypeModel();
	      	retailerTypeModel.setPrimaryKey(id);
	      	baseWrapper.setBasePersistableModel(retailerTypeModel);
	      	baseWrapper=this.retailerTypeManager.loadRetailerType(baseWrapper);
			
	      	RetailerTypeModel retailerTypeModelnew=new RetailerTypeModel();
	      	retailerTypeModelnew=(RetailerTypeModel)baseWrapper.getBasePersistableModel();

	      	retailerTypeModelnew.setName(((RetailerTypeModel)object).getName());
	      	
	      	retailerTypeModelnew.setComments(((RetailerTypeModel)object).getComments());
	      	retailerTypeModelnew.setDescription(((RetailerTypeModel)object).getDescription());
	      	
	      	retailerTypeModelnew.setUpdatedOn(new Date());
	      	retailerTypeModelnew.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
	      	
			baseWrapper.setBasePersistableModel(retailerTypeModelnew);
			
			try{
			baseWrapper = this.retailerTypeManager.updateRetailerType(baseWrapper);
	        
	        if (null != baseWrapper.getBasePersistableModel())
	        { //if not found
	          this.saveMessage(request, "Record saved successfully");
	          ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
	          return modelAndView;
	        }
	        else
	        {
	          this.saveMessage(request,
	                           "Retailer Type name already exists. Please choose a different name.");
	          return super.showForm(request, response, errors);
	        }
			}
		    catch (FrameworkCheckedException ex)
		    {
		      if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION ==
		          ex.getErrorCode())
		      {

		        super.saveMessage(request,
		                          "Retailer Type name already exists. Please choose a different name.");
		        return super.showForm(request, response, errors);
		      }
		      else
		      {

		        super.saveMessage(request,
		                          "Record could not be saved.");
		        return super.showForm(request, response, errors);
		      }
		    }
			catch (Exception ex)
		    {
		        super.saveMessage(request,MessageUtil.getMessage("6075"));
		        return super.showForm(request, response, errors);
		    }

	    }
		
		
		
		return null;
	}

	public void setRetailerTypeManager(RetailerTypeManager retailerTypeManager) {
		this.retailerTypeManager = retailerTypeManager;
	}



}
