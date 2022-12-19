package com.inov8.microbank.webapp.action.goldennosmodule;

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
import com.inov8.microbank.common.model.DeviceTypeModel;
import com.inov8.microbank.common.model.GoldenNosModel;
import com.inov8.microbank.server.service.goldennosmodule.GoldenNosManager;


public class GoldenNosFormController extends AdvanceFormController
{
	private GoldenNosManager goldenNosManager;

	  private ReferenceDataManager referenceDataManager;

	  private Long id;

	  public GoldenNosFormController()
	  {
	    setCommandName("goldenNosModel");
	    setCommandClass(GoldenNosModel.class);
	  }

	  @Override
	  protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws
	      Exception
	  {
		    if (log.isDebugEnabled())
		    {
		      log.debug("Inside reference data");
		    }

		    /**
		     * code fragment to load reference data  for Mno
		     *
		     */

		    DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
		    ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
		    		deviceTypeModel, "name", SortingOrder.ASC);
		    referenceDataWrapper.setBasePersistableModel(deviceTypeModel);
		    referenceDataManager.getReferenceData(referenceDataWrapper);
		    List<DeviceTypeModel> deviceTypeModelList = null;
		    if (referenceDataWrapper.getReferenceDataList() != null)
		    {
		    	deviceTypeModelList = referenceDataWrapper.getReferenceDataList();
		    }

		    Map referenceDataMap = new HashMap();
		    referenceDataMap.put("deviceTypeModelList", deviceTypeModelList);
		    
		    return referenceDataMap;
	  }
	  
	  
	  @Override
	  protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws
	      Exception
	  {
	     id = ServletRequestUtils.getLongParameter(httpServletRequest, "goldenNoId");
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
//	            baseWrapper = this.goldenNosManager.loadGoldenNo(baseWrapper);
	            
	            return (DeviceTypeModel) baseWrapper.getBasePersistableModel();
	        }
	        else
	        {
	            if (log.isDebugEnabled())
	            {
	                log.debug("id is null....creating new instance of Model");
	            }

	            return new GoldenNosModel();
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
	      GoldenNosModel goldenNosModel = (GoldenNosModel) command;

	      if(null != id)//To check edit/create case
	      {
//	    	  GoldenNosModel tempGoldenNosModel ;
	    	  
	          baseWrapper.setBasePersistableModel(goldenNosModel);
//	          tempDeviceTypeModel = (DeviceTypeModel)this.goldenNosManager.loadGoldenNo(baseWrapper).getBasePersistableModel();
	    	  
	    	  
	      }

	      baseWrapper.setBasePersistableModel(goldenNosModel);
	      baseWrapper = this.goldenNosManager.createOrUpdateGoldenNos(baseWrapper);
	      //***Check if record already exists.

	     if(null!=baseWrapper.getBasePersistableModel())
	     {//if not found
	       this.saveMessage(request, "Record saved successfully");
	       ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
	       return modelAndView;
	     }
	     else
	     {
	       this.saveMessage(request, "Golden number already exists.");
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

	public void setGoldenNosManager(GoldenNosManager goldenNosManager) {
		this.goldenNosManager = goldenNosManager;
	}
	


}
