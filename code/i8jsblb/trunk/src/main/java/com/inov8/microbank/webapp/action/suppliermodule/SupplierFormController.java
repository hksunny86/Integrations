package com.inov8.microbank.webapp.action.suppliermodule;

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
import com.inov8.microbank.common.model.PermissionGroupModel;
import com.inov8.microbank.common.model.SupplierModel;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.suppliermodule.SupplierManager;

/**
 * <p>Title: Microbank</p>
 *
 *
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Asad Hayat
 * @version 1.0
 */


public class SupplierFormController
    extends AdvanceFormController
{
  private SupplierManager supplierManager;
  private ReferenceDataManager referenceDataManager;

  private Long id;

  public SupplierFormController()
  {
    setCommandName("supplierModel");
    setCommandClass(SupplierModel.class);

  }

  @Override
  protected Map loadReferenceData(HttpServletRequest httpServletRequest)
  {
	  Map referenceDataMap = new HashMap();
	  
		PermissionGroupModel permissionGroupModel = new PermissionGroupModel();
		permissionGroupModel.setAppUserTypeId(UserTypeConstantsInterface.SUPPLIER);
		
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

  @Override
  protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws
      Exception
  {
	  
	  
    id = ServletRequestUtils.getLongParameter(httpServletRequest, "supplierId");
    if (null != id)
    {
      if (log.isDebugEnabled())
      {
        log.debug("id is not null....retrieving object from DB");
      }

      SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
      SupplierModel supplierModel = new SupplierModel();
      supplierModel.setSupplierId(id);
      searchBaseWrapper.setBasePersistableModel(supplierModel);
      searchBaseWrapper = this.supplierManager.loadSupplier(
          searchBaseWrapper);
      
      Long permissionGroupIdInReq = (Long)searchBaseWrapper.getObject("permissionGroupId");
      httpServletRequest.setAttribute("permissionGroupIdInReq", permissionGroupIdInReq.toString());
      
      return (SupplierModel) searchBaseWrapper.
          getBasePersistableModel();
    }
    else
    {
      if (log.isDebugEnabled())
      {
        log.debug("id is null....creating new instance of Model");
      }

      return new SupplierModel();
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
	  
	 Long permissionGroupId = ServletRequestUtils.getLongParameter(request, "permissionGroupId");
    try
    {
      BaseWrapper baseWrapper = new BaseWrapperImpl();
      SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
      

      long theDate = new Date().getTime();
      SupplierModel supplierModel = (SupplierModel) command;
      Long supId=null;
      String supIdString= ServletRequestUtils.getStringParameter(request, "supplierId");
      
      if(supIdString !=null && !supIdString.equals("")) 
      {
    	  supId=Long.parseLong(supIdString);
      }

      
      if (null != supId)
      {
    	SupplierModel tempSupplierModel;
    	
        searchBaseWrapper.setBasePersistableModel(supplierModel);
        searchBaseWrapper = this.supplierManager.loadSupplier(
                searchBaseWrapper);
        
        tempSupplierModel=(SupplierModel) searchBaseWrapper.getBasePersistableModel();
        supplierModel.setCreatedOn( tempSupplierModel.getCreatedOn() );
        supplierModel.setCreatedBy( tempSupplierModel.getCreatedBy() );
        supplierModel.setUpdatedOn(new Date());
        supplierModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());       
        
      }
      else
      {
        supplierModel.setCreatedOn(new Date(theDate));
        supplierModel.setUpdatedOn(new Date(theDate));
        supplierModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
        supplierModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
      }
      
      supplierModel.setActive( supplierModel.getActive() == null ? false : supplierModel.getActive() );
      supplierModel.setVendor( supplierModel.getVendor() == null ? false : supplierModel.getVendor() );
      
      baseWrapper.setBasePersistableModel(supplierModel);
      if(permissionGroupId != null){
    	  baseWrapper.putObject("permissionGroupId", permissionGroupId.toString());
      }
      
      baseWrapper = this.supplierManager.createOrUpdateSupplier(
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
                          "Supplier with the same name already exists.");
         return super.showForm(request, response, errors);
       }
    }
    catch (FrameworkCheckedException ex)
    {
      if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION ==
          ex.getErrorCode())
      {
        super.saveMessage(request, "Record Could Not Be Saved.");
        return super.showForm(request, response, errors);
      }

      throw ex;
    }
  }

  public void setSupplierManager(SupplierManager supplierManager)
  {
    this.supplierManager = supplierManager;
  }

  public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
  {
    this.referenceDataManager = referenceDataManager;
  }

}
