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
import com.inov8.microbank.common.model.MnoDialingCodeModel;
import com.inov8.microbank.common.model.MnoModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.mnomodule.MnoDialingCodeManager;

/**
 *
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backened application for POS terminal</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Ltd</p>
 *
 * @author Ahmad Iqbal
 * @version 1.0
 *
 */

public class MnoDialingCodeFormController
    extends AdvanceFormController
{

  private MnoDialingCodeManager mnoDialingCodeManager;

  private ReferenceDataManager referenceDataManager;
  private Long id ;

  public MnoDialingCodeFormController()
  {
    setCommandName("mnoDialingCodeModel");
    setCommandClass(MnoDialingCodeModel.class);
  }

  @Override
  protected Map loadReferenceData(HttpServletRequest request) throws Exception
  {
    /**
     * code fragment to load reference data for BankModel
     */
	  MnoModel mnoModel = new MnoModel();
	  mnoModel.setActive(true);
    ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
    		mnoModel, "name", SortingOrder.ASC);

    referenceDataManager.getReferenceData(referenceDataWrapper);

    List<MnoModel> mnoModelList = null;
    if (referenceDataWrapper.getReferenceDataList() != null)
    {
    	mnoModelList = referenceDataWrapper.getReferenceDataList();
    }

    Map referenceDataMap = new HashMap();
    referenceDataMap.put("mnoModelList", mnoModelList);

    /**
     * code fragment to load reference data for StakeholderCommissionModel
     */

        return referenceDataMap;

  }

  @Override
  protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws
      Exception
  {
    
	  id = ServletRequestUtils.getLongParameter(httpServletRequest, "mnoDialingCodeId");
	    if (null != id)
	    {
	      if (log.isDebugEnabled())
	      {
	        log.debug("id is not null....retrieving object from DB");
	      }

	      SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
	      MnoDialingCodeModel mnoDialingCodeModel = new MnoDialingCodeModel();
	      mnoDialingCodeModel.setMnoDialingCodeId(id);

	      searchBaseWrapper.setBasePersistableModel(mnoDialingCodeModel);
	      searchBaseWrapper = this.mnoDialingCodeManager.loadMnoDialingCode(
	          searchBaseWrapper);
	      mnoDialingCodeModel = (MnoDialingCodeModel) searchBaseWrapper.getBasePersistableModel();


	      if (log.isDebugEnabled())
	      {
	        log.debug("ticker String  is : " + mnoDialingCodeModel.getCode());
	      }
	      return mnoDialingCodeModel;
	    }
	    else
	    {
	      if (log.isDebugEnabled())
	      {
	        log.debug("id is null....creating new instance of Model");
	      }
	      long theDate = new Date().getTime();
	      MnoDialingCodeModel mnoDialingCodeModel = new MnoDialingCodeModel();
	      mnoDialingCodeModel.setCreatedOn(new Date(theDate));
	      return mnoDialingCodeModel;
	    }
		

  }

  @Override
  protected ModelAndView onCreate(HttpServletRequest request,
                                  HttpServletResponse response, Object command,
                                  BindException errors) throws Exception
  {
    return this.createOrUpdate(request, response, command,
                               errors);
  }

  @Override
  protected ModelAndView onUpdate(HttpServletRequest request,
                                  HttpServletResponse response, Object command,
                                  BindException errors) throws Exception
  {
	  return this.createOrUpdate(request, response, command,
              errors);
  }

  private ModelAndView createOrUpdate(HttpServletRequest request,
                                      HttpServletResponse response,
                                      Object command,
                                      BindException errors) throws Exception
  {
    try
    {
      BaseWrapper baseWrapper = new BaseWrapperImpl();
      MnoDialingCodeModel mnoDialingCodeModel = (
          MnoDialingCodeModel) command;

      mnoDialingCodeModel.setCreatedOn(new Date());
      
      
      mnoDialingCodeModel.setCreatedByAppUserModel(UserUtils.
          getCurrentUser());
      

      baseWrapper.setBasePersistableModel(mnoDialingCodeModel);
      baseWrapper = this.mnoDialingCodeManager.
          createOrUpdateMnoDialingCode(
              baseWrapper);
      if (null != baseWrapper.getBasePersistableModel())
      { //if not found

        this.saveMessage(request, "Record saved successfully");
        ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
        return modelAndView;
      }
      else
      {
        this.saveMessage(request,
                         "Duplicate MNO dialing Code not allowed");
        return super.showForm(request, response, errors);
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

  public void setMnoDialingCodeManager(MnoDialingCodeManager
                                            mnoDialingCodeManager)
  {
    this.mnoDialingCodeManager = mnoDialingCodeManager;
  }
}
