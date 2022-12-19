package com.inov8.microbank.webapp.action.portal.transactiondetail;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.common.util.RandomUtils;
import com.inov8.framework.common.exception.ExceptionErrorCodes;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.IssueModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.portal.transactiondetail.TransactionDetailManager;

public class TransactionDetailFormController extends AdvanceFormController
{
 // private SalesManager salesManager;
 private TransactionDetailManager transactionDetailManager;
 // private TransactionManager transactionManager;
  private Long id;


  public TransactionDetailFormController()
  {
    setCommandName("issueModel");
    setCommandClass(IssueModel.class);
  }

  protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws
      Exception
  {
    return null;
  }

  protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws
      Exception
  {
    id = ServletRequestUtils.getLongParameter(httpServletRequest, "issueId");
     if (null != id)
     {
       if (log.isDebugEnabled())
       {
         log.debug("id is not null....retrieving object from DB");
       }

       SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
       IssueModel issueModel = new IssueModel();
       issueModel.setTransactionId(id);
      // appUserModel.setPassword(RandomUtils.generateRandom(10, true, true));
        searchBaseWrapper.setBasePersistableModel(issueModel);
       searchBaseWrapper = this.transactionDetailManager.loadTransactionDetail(
           searchBaseWrapper);
       return (IssueModel) searchBaseWrapper.
           getBasePersistableModel();
     }
     else
     {
       if (log.isDebugEnabled())
       {
         log.debug("id is null....creating new instance of Model");
       }
       IssueModel issueModel = new IssueModel();
      issueModel.setTransactionCodeId(ServletRequestUtils.getLongParameter(httpServletRequest,"transactionCodeId"));
       issueModel.setTransactionId(ServletRequestUtils.getLongParameter(httpServletRequest,"transactionId"));
       return issueModel;
     }


  }

  protected ModelAndView onCreate(HttpServletRequest httpServletRequest,
                                  HttpServletResponse httpServletResponse,
                                  Object object, BindException bindException) throws
      Exception
  {
    return this.createOrUpdate(httpServletRequest, httpServletResponse, object,
                               bindException);
  }

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

     long theDate = new Date().getTime();
     IssueModel issueModel = (IssueModel) command;
     if (null != id)
     {

       issueModel.setUpdatedOn(new Date(theDate));
       issueModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
     }
     else
     {
       issueModel.setCreatedOn(new Date(theDate));
       issueModel.setUpdatedOn(new Date(theDate));
       issueModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
       issueModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
       issueModel.setIssueCode(RandomUtils.generateRandom(4, false, true));
       issueModel.setIssueTypeStatusId(1L);
     }
     baseWrapper.setBasePersistableModel(issueModel);
     baseWrapper = this.transactionDetailManager.createOrUpdateIssue(
         baseWrapper);



        issueModel = (IssueModel) baseWrapper.getBasePersistableModel();

        TransactionModel transactionModel = new TransactionModel();
        transactionModel.setTransactionId(issueModel.getTransactionId());
        baseWrapper.setBasePersistableModel(transactionModel);
        baseWrapper = this.transactionDetailManager.loadTransaction(baseWrapper);
        if (null != baseWrapper.getBasePersistableModel())
        {

          transactionModel = (TransactionModel) baseWrapper.getBasePersistableModel();
          transactionModel.setIssue(true);
          baseWrapper.setBasePersistableModel(transactionModel);
          baseWrapper = this.transactionDetailManager.createOrUpdateTransaction(baseWrapper);
        }

//     ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
//         return modelAndView;
     //***Check if record already exists.


//      { //if not found


//
        this.saveMessage(request, "Chargeback Created Successfully");
        ModelAndView modelAndView = new ModelAndView(this.getSuccessView()+"?isIssue="+transactionModel.getIssue()+"&transactionId="+transactionModel.getTransactionId());
        return modelAndView;
//      }
//      else
//      {
//        this.saveMessage(request,
//                         "Supplier with the same name already exists.");
//        return super.showForm(request, response, errors);
//      }
   }
   catch (FrameworkCheckedException ex)
   {
     if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION ==
         ex.getErrorCode())
     {
       super.saveMessage(request, "Chargback could not be created.");
       return super.showForm(request, response, errors);
     }

     throw ex;
   }
 }




  public void setTransactionDetailManager(TransactionDetailManager
                                          transactionDetailManager)
  {
    this.transactionDetailManager = transactionDetailManager;
  }

}
