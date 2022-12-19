package com.inov8.microbank.webapp.action.switchmodule;

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
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.PaymentModeModel;
import com.inov8.microbank.common.model.SwitchFinderModel;
import com.inov8.microbank.common.model.SwitchModel;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchFinderManager;

public class SwitchFinderFormController extends AdvanceFormController
{
	
	private SwitchFinderManager switchFinderManager;
	

	  private ReferenceDataManager referenceDataManager;

	  private Long id;

	  public SwitchFinderFormController()
	  {
	    setCommandName("switchFinderModel");
	    setCommandClass(SwitchFinderModel.class);
	  }

	  @Override
	  protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws
	      Exception
	  {
	    ReferenceDataWrapper referenceDataWrapper ;
	    Map referenceDataMap = new HashMap();

	    // ************* Load Payment Mode data *******************************************
	    PaymentModeModel paymentModeModel = new PaymentModeModel();
	    referenceDataWrapper = new ReferenceDataWrapperImpl(paymentModeModel, "name", SortingOrder.ASC);
	    referenceDataManager.getReferenceData(referenceDataWrapper);

	    List<PaymentModeModel> paymentModeModelList = null;
	    if (referenceDataWrapper.getReferenceDataList() != null)
	    {
	    	paymentModeModelList = referenceDataWrapper.getReferenceDataList();
	    }	    
	    referenceDataMap.put("paymentModeModelList", paymentModeModelList);
	    // ********************************************************************************

	    // ************* Load Switch data *******************************************
	    SwitchModel switchModel = new SwitchModel();
	    switchModel.setActive(true);
	    referenceDataWrapper = new ReferenceDataWrapperImpl(switchModel, "name", SortingOrder.ASC);
	    referenceDataManager.getReferenceData(referenceDataWrapper);

	    List<SwitchModel> switchModelList = null;
	    if (referenceDataWrapper.getReferenceDataList() != null)
	    {
	    	switchModelList = referenceDataWrapper.getReferenceDataList();
	    }	    
	    referenceDataMap.put("switchModelList", switchModelList);
	    // ********************************************************************************
	    
	    //	************* Load Bank data *******************************************
	    BankModel bankModel = new BankModel();
	    bankModel.setActive(true);
	    referenceDataWrapper = new ReferenceDataWrapperImpl(bankModel, "name", SortingOrder.ASC);
	    referenceDataManager.getReferenceData(referenceDataWrapper);

	    List<BankModel> bankModelList = null;
	    if (referenceDataWrapper.getReferenceDataList() != null)
	    {
	    	bankModelList = referenceDataWrapper.getReferenceDataList();
	    }	    
	    referenceDataMap.put("bankModelList", bankModelList);
	    // ********************************************************************************


	    
	    return referenceDataMap;
	  }
	  
	  @Override
	  protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws
	      Exception
	  {
	     id = ServletRequestUtils.getLongParameter(httpServletRequest, "switchFinderId");
	        if (null != id)
	        {
	            if (log.isDebugEnabled())
	            {
	                log.debug("id is not null....retrieving object from DB");
	            }

	            BaseWrapper baseWrapper = new BaseWrapperImpl();
	            SwitchFinderModel switchFinderModel = new SwitchFinderModel();
	            switchFinderModel.setSwitchFinderId(id);
	            baseWrapper.setBasePersistableModel(switchFinderModel);
	            baseWrapper = this.switchFinderManager.loadSwitchFinder(baseWrapper);
	            
	            return (SwitchFinderModel) baseWrapper.getBasePersistableModel();
	        }
	        else
	        {
	            if (log.isDebugEnabled())
	            {
	                log.debug("id is null....creating new instance of Model");
	            }

	            return new SwitchFinderModel();
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
	      SwitchFinderModel switchFinderModel = (SwitchFinderModel) command;


	      baseWrapper.setBasePersistableModel(switchFinderModel);
	      baseWrapper = this.switchFinderManager.createOrUpdateSwitchFinder(baseWrapper);
	      //***Check if record already exists.

	     if(null!=baseWrapper.getBasePersistableModel())
	     {//if not found
	       this.saveMessage(request, "Record saved successfully");
	       ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
	       return modelAndView;
	     }
	     else
	     {
	       this.saveMessage(request, "Switch Finder with the same Bank and Payment Mode already exists.");
	       return super.showForm(request, response,errors);
	     }
	    }
	    catch (FrameworkCheckedException ex)
	    {
	      
	    	if( ex.getMessage().equalsIgnoreCase("SwitchFinderUniqueException") )
			{
				super.saveMessage(request, "Switch Finder with the same Bank and Payment Mode already exists.");
				return super.showForm(request, response, errors);				
			}	
	    	
	    	else if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION ==
	          ex.getErrorCode())
	      {

	        super.saveMessage(request, "Switch Finder with the same Bank and Payment Mode already exists");
	        return super.showForm(request, response, errors);
	      }

	      throw ex;
	    }
	  }  

	  public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
	  {
	    this.referenceDataManager = referenceDataManager;
	  }


	public void setSwitchFinderManager(SwitchFinderManager switchFinderManager) {
		this.switchFinderManager = switchFinderManager;
	}

}
