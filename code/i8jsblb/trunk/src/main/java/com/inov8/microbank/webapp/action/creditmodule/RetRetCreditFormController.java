package com.inov8.microbank.webapp.action.creditmodule;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.ExceptionErrorCodes;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.DeviceTypeModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.TransactionTypeModel;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.creditmodule.RetRetCreditManager;
import com.inov8.microbank.server.service.workflow.controller.WorkFlowController;

/**
 *
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backened application for POS terminal</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8(PVT)Ltd</p>
 *
 * @author Ahmad Iqbal
 * @version 1.0
 * @todo Set From Retailer Contact Id according to currently logged in user.
 */

public class RetRetCreditFormController
    extends AdvanceFormController
{
  private ReferenceDataManager referenceDataManager;
  private RetRetCreditManager retRetCreditManager;
  private WorkFlowController workFlowController;

  public RetRetCreditFormController()
  {
    setCommandName("transactionModel");
    setCommandClass(TransactionModel.class);
  }

  protected Map loadReferenceData(HttpServletRequest request) throws Exception
  {
    if (log.isDebugEnabled())
    {
      log.debug("Inside reference data");
    }

    /**
     * code fragment to load reference data  for Distributor
     *
     */

//    RetailerContactModel retailerContactModel = new RetailerContactModel();
//    ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
//        retailerContactModel, "firstName", SortingOrder.DESC);
//    referenceDataWrapper.setBasePersistableModel(retailerContactModel);
//    referenceDataManager.getReferenceData(referenceDataWrapper);
//    List<RetailerContactModel> retailerContactModelList = null;
//    if (referenceDataWrapper.getReferenceDataList() != null)
//    {
//      retailerContactModelList = referenceDataWrapper.getReferenceDataList();
//    }
//
    Map referenceDataMap = new HashMap();
//    referenceDataMap.put("retailerContactModelList",
//                         retailerContactModelList);
    return referenceDataMap;
  }

  protected Object loadFormBackingObject(HttpServletRequest request) throws
      Exception
  {
    return new TransactionModel();
  }

  protected ModelAndView onCreate(HttpServletRequest request,
                                  HttpServletResponse response, Object command,
                                  BindException errors) throws Exception
  {
    try
    {

      TransactionModel transactionModel = (TransactionModel) command;
      WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();

      workFlowWrapper.setAppUserModel(new AppUserModel());
      workFlowWrapper.setToRetailerContactAppUserModel(new AppUserModel());
      workFlowWrapper.setTransactionTypeModel(new TransactionTypeModel());
      workFlowWrapper.setDeviceTypeModel(new DeviceTypeModel());

      workFlowWrapper.setAppUserModel( UserUtils.getCurrentUser() ) ;
      workFlowWrapper.getTransactionTypeModel().setTransactionTypeId( TransactionTypeConstantsInterface.RET_TO_RET_TX ) ;
      workFlowWrapper.getDeviceTypeModel().setDeviceTypeId( DeviceTypeConstantsInterface.MOBILE );

      workFlowWrapper.setTransactionAmount( 100D );

      workFlowWrapper.getToRetailerContactAppUserModel().setMobileNo( "0334455667788" );

      workFlowWrapper=this.workFlowController.workflowProcess(workFlowWrapper);

      transactionModel=workFlowWrapper.getTransactionModel();

      this.saveMessage(request, "The Transaction Completed Successfully. " + "Transaction Code: " + transactionModel.getTransactionCodeIdTransactionCodeModel().getCode());
      ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
      return modelAndView;

    }
    catch (WorkFlowException ex)
    {
      if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION ==
          new Long(ex.getMessage()).longValue())
      {
        super.saveMessage(request, "Record could not be saved.");
        return super.showForm(request, response, errors);
      }
      throw ex;
    }

}

  protected ModelAndView onUpdate(HttpServletRequest request,
                                  HttpServletResponse response, Object command,
                                  BindException errors) throws Exception
  {
    return null;
  }

  public void setRetRetCreditManager(RetRetCreditManager retRetCreditManager)
  {
    this.retRetCreditManager = retRetCreditManager;
  }

  public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
  {
    this.referenceDataManager = referenceDataManager;
  }

  public void setWorkFlowController(WorkFlowController workFlowController)
  {
    this.workFlowController = workFlowController;
  }
}
