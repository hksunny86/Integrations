package com.inov8.microbank.webapp.action.common;

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
import com.inov8.microbank.common.model.AreaModel;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.common.AreaManager;


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

public class AreaFormController extends AdvanceFormController
{

  private AreaManager areaManager;

  private ReferenceDataManager referenceDataManager;

  private Long id;

  public AreaFormController()
  {
    setCommandName("areaModel");
    setCommandClass(AreaModel.class);
  }

  @Override
  protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws
      Exception
  {
    /**
     * code fragment to load reference data for AreaModel
     */

    AreaModel areaModel = new AreaModel();
    ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
        areaModel, "name", SortingOrder.ASC);

    referenceDataManager.getReferenceData(referenceDataWrapper);

    List<AreaModel> areaModelList = null;

    if (referenceDataWrapper.getReferenceDataList() != null)
    {
      areaModelList = referenceDataWrapper.getReferenceDataList();
    }

    Map referenceDataMap = new HashMap();
    referenceDataMap.put("areaModelList", areaModelList);

    return referenceDataMap;

  }
  @Override
  protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws
      Exception
  {
     id = ServletRequestUtils.getLongParameter(httpServletRequest, "areaId");
        if (null != id)
        {
            if (log.isDebugEnabled())
            {
                log.debug("id is not null....retrieving object from DB");
            }

            SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
            AreaModel areaModel = new AreaModel();
            areaModel.setAreaId(id);
            searchBaseWrapper.setBasePersistableModel(areaModel);
            searchBaseWrapper = this.areaManager.loadArea(
                    searchBaseWrapper);
            return (AreaModel) searchBaseWrapper.
                    getBasePersistableModel();
        }
        else
        {
            if (log.isDebugEnabled())
            {
                log.debug("id is null....creating new instance of Model");
            }

            return new AreaModel();
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
      BaseWrapper tempBaseWrapper = new BaseWrapperImpl();
      AreaModel areaModel = (AreaModel) command;
      AreaModel tempAreaModel =new AreaModel(); 

      if(null != id)//To check edit/create case
      {
    	  tempAreaModel.setAreaId(id);
    	  tempBaseWrapper.setBasePersistableModel(tempAreaModel);
    	  tempBaseWrapper=areaManager.loadArea(tempBaseWrapper);
    	  tempAreaModel=(AreaModel)tempBaseWrapper.getBasePersistableModel();
    	  areaModel.setCreatedOn(tempAreaModel.getCreatedOn());
    	  areaModel.setUpdatedOn(new Date());//set current date
        areaModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
      }
      else
      {
        //areaModel.setAreaId(null);
        areaModel.setUpdatedOn(new Date());
        areaModel.setCreatedOn(new Date());
        areaModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
        areaModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
      }

      baseWrapper.setBasePersistableModel(areaModel);
      baseWrapper = this.areaManager.createOrUpdateArea(
          baseWrapper);
      //***Check if record already exists.

     if(null!=baseWrapper.getBasePersistableModel())
     {//if not found
       this.saveMessage(request, "Record saved successfully");
       ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
       return modelAndView;
     }
     else
     {
       this.saveMessage(request, "Area with the same name already exists.");
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
    }catch (Exception ex)
	{
		super.saveMessage(request,
				MessageUtil.getMessage("6075"));
		return super.showForm(request, response, errors);
	}

  }

  public void setAreaManager(AreaManager areaManager)
  {
    this.areaManager = areaManager;
  }

  public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
  {
    this.referenceDataManager = referenceDataManager;
  }
}
