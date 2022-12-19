package com.inov8.microbank.webapp.action.switchmodule;

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
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.SwitchModel;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchModuleManager;

public class SwitchFormController extends AdvanceFormController
{
	
	private SwitchModuleManager switchManager;
	

	  private ReferenceDataManager referenceDataManager;

	  private Long id;

	  public SwitchFormController()
	  {
	    setCommandName("switchModel");
	    setCommandClass(SwitchModel.class);
	  }

	  @Override
	  protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws
	      Exception
	  {
	    return null;
	  }
	  
	  @Override
	  protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws
	      Exception
	  {
	     id = ServletRequestUtils.getLongParameter(httpServletRequest, "switchId");
	        if (null != id)
	        {
	            if (log.isDebugEnabled())
	            {
	                log.debug("id is not null....retrieving object from DB");
	            }

	            BaseWrapper baseWrapper = new BaseWrapperImpl();
	            SwitchModel switchModel = new SwitchModel();
	            switchModel.setSwitchId(id);
	            baseWrapper.setBasePersistableModel(switchModel);
	            baseWrapper = this.switchManager.loadSwitch(baseWrapper);
	            
	            return (SwitchModel) baseWrapper.getBasePersistableModel();
	        }
	        else
	        {
	            if (log.isDebugEnabled())
	            {
	                log.debug("id is null....creating new instance of Model");
	            }

	            return new SwitchModel();
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
	      SwitchModel switchModel = (SwitchModel) command;

	      if (null != id)
	      {
	    	    SwitchModel tempSwitchModel = new SwitchModel();
	    	    tempSwitchModel.setSwitchId(id);
	            baseWrapper.setBasePersistableModel(tempSwitchModel);
	            tempSwitchModel = (SwitchModel)this.switchManager.loadSwitch(baseWrapper).getBasePersistableModel() ;
	    	  
	            if( switchModel.getPassword() == null )
	            	switchModel.setPassword( tempSwitchModel.getPassword() ) ;
	      }
	      
	      switchModel.setActive( switchModel.getActive() == null ? false : switchModel.getActive() ) ;
	      switchModel.setCvvRequired(switchModel.getCvvRequired() == null ? false : switchModel.getCvvRequired() ) ;
	      switchModel.setMpinRequired(switchModel.getMpinRequired() == null ? false : switchModel.getMpinRequired() ) ;
	      switchModel.setTpinRequired(switchModel.getTpinRequired() == null ? false : switchModel.getTpinRequired() ) ;
	      baseWrapper.setBasePersistableModel(switchModel);
	      baseWrapper = this.switchManager.createOrUpdateSwitch(baseWrapper);
	      //***Check if record already exists.

	     if(null!=baseWrapper.getBasePersistableModel())
	     {//if not found
	       this.saveMessage(request, "Record saved successfully");
	       ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
	       return modelAndView;
	     }
	     else
	     {
	       this.saveMessage(request, "Switch with the same name already exists.");
	       baseWrapper.setBasePersistableModel(switchModel);
           //command = (SwitchModel)this.switchManager.loadSwitch(baseWrapper).getBasePersistableModel() ;
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
	  }  

	  public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
	  {
	    this.referenceDataManager = referenceDataManager;
	  }

	public void setSwitchManager(SwitchModuleManager switchManager)
	{
		this.switchManager = switchManager;
	}

}
