package com.inov8.microbank.webapp.action.veriflymodule;

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
import com.inov8.microbank.common.model.VeriflyModeModel;
import com.inov8.microbank.common.model.VeriflyModel;
import com.inov8.microbank.server.service.financialintegrationmodule.veriflymodule.VeriflyModuleManager;

public class VeriflyFormController extends AdvanceFormController
{
	
	private VeriflyModuleManager veriflyManager;
	

	  private ReferenceDataManager referenceDataManager;

	  private Long id;

	  public VeriflyFormController()
	  {
	    setCommandName("veriflyModel");
	    setCommandClass(VeriflyModel.class);
	  }

	  @Override
	  protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws
	      Exception
	  {
		    VeriflyModeModel veriflyModeModel = new VeriflyModeModel();
			ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(veriflyModeModel, "modeName",
					SortingOrder.ASC);
			try
			{
				referenceDataManager.getReferenceData(referenceDataWrapper);
			}
			catch (FrameworkCheckedException ex1)
			{
				ex1.getStackTrace();
			}
			List<VeriflyModeModel> veriflyModeModelList = null;
			if (referenceDataWrapper.getReferenceDataList() != null)
			{
				veriflyModeModelList = referenceDataWrapper.getReferenceDataList();
			}

			Map referenceDataMap = new HashMap();
			referenceDataMap.put("veriflyModeModelList", veriflyModeModelList);

		  return referenceDataMap;
	  }
	  
	  @Override
	  protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws
	      Exception
	  {
	     id = ServletRequestUtils.getLongParameter(httpServletRequest, "veriflyId");
	        if (null != id)
	        {
	            if (log.isDebugEnabled())
	            {
	                log.debug("id is not null....retrieving object from DB");
	            }

	            BaseWrapper baseWrapper = new BaseWrapperImpl();
	            VeriflyModel veriflyModel = new VeriflyModel();
	            veriflyModel.setVeriflyId(id);
	            baseWrapper.setBasePersistableModel(veriflyModel);
	            baseWrapper = this.veriflyManager.loadVerifly(baseWrapper);
	            
	            return (VeriflyModel) baseWrapper.getBasePersistableModel();
	        }
	        else
	        {
	            if (log.isDebugEnabled())
	            {
	                log.debug("id is null....creating new instance of Model");
	            }

	            return new VeriflyModel();
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
		  VeriflyModel veriflyModel = (VeriflyModel) command;
	    try
	    {
	      BaseWrapper baseWrapper = new BaseWrapperImpl();
	     

	      if (null != id)
	      {
	    	  VeriflyModel tempVeriflyModel = new VeriflyModel();
	    	  tempVeriflyModel.setVeriflyId(id);
	            baseWrapper.setBasePersistableModel(tempVeriflyModel);
	            tempVeriflyModel = (VeriflyModel)this.veriflyManager.loadVerifly(baseWrapper).getBasePersistableModel() ;
	    	  
	            if( veriflyModel.getPassword().equalsIgnoreCase("*****") )
	            	veriflyModel.setPassword( tempVeriflyModel.getPassword() ) ;
	      }
	      
	      veriflyModel.setActive( veriflyModel.getActive() == null ? false : veriflyModel.getActive() );


	      baseWrapper.setBasePersistableModel(veriflyModel);
	      baseWrapper = this.veriflyManager.createOrUpdateVerifly(baseWrapper);
	      //***Check if record already exists.

	     if(null!=baseWrapper.getBasePersistableModel())
	     {//if not found
	       this.saveMessage(request, "Record saved successfully");
	       ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
	       return modelAndView;
	     }
	     else
	     {
	       this.saveMessage(request, "Verifly with the same Name already exists");
	       baseWrapper.setBasePersistableModel(veriflyModel);
           command = (VeriflyModel)this.veriflyManager.loadVerifly(baseWrapper).getBasePersistableModel() ;
	       return super.showForm(request, response,errors);
	     }
	    }
	    catch (FrameworkCheckedException ex)
	    {
	    	veriflyModel.setPassword(""); 
	    	if( ex.getMessage().equalsIgnoreCase("VeriflyNameUniqueException") )
			{
				super.saveMessage(request, "Verifly with the same Name already exists");
				return super.showForm(request, response, errors);				
			}		
	    	else if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION ==
	          ex.getErrorCode())
	      {

	        super.saveMessage(request, "Verifly with the same Name already exists");
	        return super.showForm(request, response, errors);
	      }

	      throw ex;
	    }
	  }  

	  public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
	  {
	    this.referenceDataManager = referenceDataManager;
	  }

	public void setVeriflyManager(VeriflyModuleManager veriflyManager)
	{
		this.veriflyManager = veriflyManager;
	}

}
