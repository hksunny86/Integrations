package com.inov8.microbank.webapp.action.mnomodule;

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
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.MnoModel;
import com.inov8.microbank.common.model.PermissionGroupModel;
import com.inov8.microbank.common.model.SupplierModel;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.mnomodule.MnoManager;



/**
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backend Application for POS terminals.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Asad Hayat
 * @version 1.0
 */

public class MnoFormController extends AdvanceFormController
{
  private MnoManager mnoManager;
  private ReferenceDataManager referenceDataManager;


  private Long id;

  public MnoFormController()
  {
    setCommandName("mnoModel");
    setCommandClass(MnoModel.class);
  }

  protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws
      Exception
  {
	    /**
	     * code fragment to load reference data for SupplierModel
	     */
	    /*SupplierModel supplierModel = new SupplierModel();
	    supplierModel.setActive(true);
	    ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
	        supplierModel, "name", SortingOrder.ASC);

	    try
	    {
	      referenceDataManager.getReferenceData(referenceDataWrapper);
	    }
	    catch (FrameworkCheckedException ex1)
	    {
	    }*/
	    List<SupplierModel> supplierModelList = null;
	    /*if (referenceDataWrapper.getReferenceDataList() != null)
	    {
	      supplierModelList = referenceDataWrapper.getReferenceDataList();
	    }
	    */
	    
	    if (null==id)
	    {
	    supplierModelList=mnoManager.searchSupplierModels();
	    }
	    else
	    {
	    	
	    	SupplierModel supplierModel = new SupplierModel();
		    supplierModel.setActive(true);
		    ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
		        supplierModel, "name", SortingOrder.ASC);

		    try
		    {
		      referenceDataManager.getReferenceData(referenceDataWrapper);
		    }
		    catch (FrameworkCheckedException ex1)
		    {
		    }
		    
		    if (referenceDataWrapper.getReferenceDataList() != null)
		    {
		      supplierModelList = referenceDataWrapper.getReferenceDataList();
		    }
	    	
	    }

	    Map referenceDataMap = new HashMap();
	    referenceDataMap.put("SupplierModelList", supplierModelList);
	    
		
	    PermissionGroupModel permissionGroupModel = new PermissionGroupModel();
		permissionGroupModel.setAppUserTypeId(UserTypeConstantsInterface.MNO);
		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
				permissionGroupModel, "name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(permissionGroupModel);
		try {
		referenceDataManager.getReferenceData(referenceDataWrapper);
		} catch (FrameworkCheckedException ex1) {
			ex1.getStackTrace();
		}
		List<PermissionGroupModel> permissionGroupModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null) 
		{
			permissionGroupModelList = referenceDataWrapper.getReferenceDataList();
		}	
		referenceDataMap.put("permissionGroupModelList", permissionGroupModelList);	      
	    
	    return referenceDataMap;
  }

  protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws
      Exception
  {
    id = ServletRequestUtils.getLongParameter(httpServletRequest, "mnoId");
        if (null != id)
        {
            if (log.isDebugEnabled())
            {
                log.debug("id is not null....retrieving object from DB");
            }

            SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
            MnoModel mnoModel = new MnoModel();

            mnoModel.setMnoId(id);
            searchBaseWrapper.setBasePersistableModel(mnoModel);
            searchBaseWrapper = this.mnoManager.loadMno(
                    searchBaseWrapper);
            
            Long permissionGroupIdInReq = (Long)searchBaseWrapper.getObject("permissionGroupId");
            httpServletRequest.setAttribute("permissionGroupIdInReq", permissionGroupIdInReq.toString());
            
            return (MnoModel) searchBaseWrapper.
                    getBasePersistableModel();
        }
        else
        {
            if (log.isDebugEnabled())
            {
                log.debug("id is null....creating new instance of Model");
            }
            //setting the current date
            long theDate = new Date().getTime();
            MnoModel mnoModel  = new MnoModel();
            mnoModel.setCreatedOn(new Date(theDate));

            return mnoModel;
        }

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

  protected ModelAndView onCreate(HttpServletRequest httpServletRequest,
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
      MnoModel mnoModel=(MnoModel) command;
      	
      mnoModel.setUpdatedOn(new Date());
      mnoModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
      
      Long mnoId=null;
      String mnoIdString= ServletRequestUtils.getStringParameter(request, "mnoId");
      
      if(mnoIdString !=null && !mnoIdString.equals("")) 
      {
    	  mnoId=Long.parseLong(mnoIdString);
      }
      
      if (null == mnoId)
      {
   	    Long permissionGroupId = ServletRequestUtils.getLongParameter(request, "permissionGroupId");
   	    baseWrapper.putObject("permissionGroupId", permissionGroupId);
      	mnoModel.setCreatedOn(new Date());        
      	mnoModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
      }
      else
      {
    	  MnoModel tempMnoModel = new MnoModel();
    	  tempMnoModel.setMnoId(mnoId);
    	  baseWrapper.setBasePersistableModel(tempMnoModel);
    	  tempMnoModel = (MnoModel)this.mnoManager.loadMno(baseWrapper).getBasePersistableModel();
    	  mnoModel.setCreatedBy(tempMnoModel.getCreatedBy());
    	  mnoModel.setCreatedOn(tempMnoModel.getCreatedOn());    	  
      }
        
      mnoModel.setActive( mnoModel.getActive() == null ? false : mnoModel.getActive() );
      baseWrapper.setBasePersistableModel(mnoModel);

      baseWrapper = this.mnoManager.createOrUpdateMno(
          baseWrapper);

      //***Check if record already exists.
       if (null != baseWrapper.getBasePersistableModel())
       { //if not found
         this.saveMessage(request, "Record saved successfully");
         ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
         return modelAndView;
       }
       else
       {
         this.saveMessage(request,
             "Record not Saved successfully");
         return super.showForm(request, response, errors);
       }
    }
    catch (FrameworkCheckedException ex)
    {
      
    	if (ex.getMessage().equalsIgnoreCase("MnoNameUniqueException"))
		{
			super.saveMessage(request, "MNO name already exists.");
			return super.showForm(request, response, errors);
		}
		else if (ex.getMessage().equalsIgnoreCase("SupplierUniqueException"))
		{
			super.saveMessage(request, "Supplier already exists.");
			return super.showForm(request, response, errors);
		}
		else if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION ==
          ex.getErrorCode())
      {

        super.saveMessage(request, "Record not Saved successfully");
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


  public void setMnoManager(MnoManager mnoManager)
  {
    this.mnoManager = mnoManager;
  }

public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
{
	this.referenceDataManager = referenceDataManager;
}


}
