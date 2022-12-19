package com.inov8.microbank.webapp.action.smartmoneymodule;

import java.util.ArrayList;
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
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.CardTypeModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.customermodule.CustomerListViewModel;
import com.inov8.microbank.common.model.smartmoneymodule.SmartMoneyAccountVeriflyModel;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.financialintegrationmodule.veriflymodule.VeriflyManagerService;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl;
import com.inov8.verifly.server.service.mainmodule.VeriflyManager;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Asad Hayat
 * @version 1.0
 */

public class SmartMoneyAccountFormController
    extends AdvanceFormController
{

  private SmartMoneyAccountManager smartMoneyAccountManager;
  private ReferenceDataManager referenceDataManager;
  private Long id;
  private VeriflyManagerService veriflyController;


  public SmartMoneyAccountFormController()
  {
    setCommandName("smartMoneyAccountVeriflyModel");
    setCommandClass(SmartMoneyAccountVeriflyModel.class);
  }

  @Override
  protected Map loadReferenceData(HttpServletRequest httpServletRequest)
  {

    ReferenceDataWrapper referenceDataWrapper;
    if (log.isDebugEnabled())
    {
      log.debug("Inside reference data");
    }

    /**
     * code fragment to load reference data  for Customer
     *
     */

    CustomerListViewModel customerModel = new CustomerListViewModel();
    referenceDataWrapper = new ReferenceDataWrapperImpl(
        customerModel, "firstName", SortingOrder.ASC);
    referenceDataWrapper.setBasePersistableModel(customerModel);
    try
    {
      referenceDataManager.getReferenceData(referenceDataWrapper);
    }
    catch (Exception e)
    {

    }
    List<CustomerModel> customerModelList = null;
    if (referenceDataWrapper.getReferenceDataList() != null)
    {
      customerModelList = referenceDataWrapper.
          getReferenceDataList();
    }
    Map referenceDataMap = new HashMap();
    referenceDataMap.put("customerModelList", customerModelList);



    /**
     * code fragment to load reference data  for Card Type
     *
     */

    CardTypeModel cardTypeModel = new CardTypeModel();
//    referenceDataWrapper = new ReferenceDataWrapperImpl(
//        cardTypeModel, "name", SortingOrder.DESC);
//    referenceDataWrapper.setBasePersistableModel(cardTypeModel);
//    try
//    {
//      referenceDataManager.getReferenceData(referenceDataWrapper);
//    }
//    catch (Exception e)
//    {
//
//    }
    List<CardTypeModel> cardTypeModelList = new ArrayList<CardTypeModel>();
    CardTypeModel cardTypeModel1 = new CardTypeModel();
    CardTypeModel cardTypeModel2 = new CardTypeModel();
    CardTypeModel cardTypeModel3 = new CardTypeModel();

    cardTypeModel1.setPrimaryKey(1L);
    cardTypeModel1.setName("Visa");
    cardTypeModel2.setPrimaryKey(2L);
    cardTypeModel2.setName("Master");
    cardTypeModel3.setPrimaryKey(3L);
    cardTypeModel3.setName("America Express");
    cardTypeModelList.add(cardTypeModel1);
    cardTypeModelList.add(cardTypeModel2);
    cardTypeModelList.add(cardTypeModel3);


//    if (referenceDataWrapper.getReferenceDataList() != null)
//    {
//      cardTypeModelList = referenceDataWrapper.
//          getReferenceDataList();
//    }
  //  Map referenceDataMap = new HashMap();
    referenceDataMap.put("cardTypeModelList", cardTypeModelList);

    /**
   * code fragment to load reference data  for Card Type
   *
   */

  com.inov8.verifly.common.model.VfPaymentModeModel paymentModeModel = new com.inov8.verifly.common.model.VfPaymentModeModel();
//  referenceDataWrapper = new ReferenceDataWrapperImpl(
//      paymentModeModel, "name", SortingOrder.DESC);
//  referenceDataWrapper.setBasePersistableModel(paymentModeModel);
//  try
//  {
//    referenceDataManager.getReferenceData(referenceDataWrapper);
//  }
//  catch (Exception e)
//  {
//
//  }
  List<com.inov8.verifly.common.model.VfPaymentModeModel> paymentModeModelList = new ArrayList();
  com.inov8.verifly.common.model.VfPaymentModeModel paymentModeModel1 = new com.inov8.verifly.common.model.VfPaymentModeModel();
  com.inov8.verifly.common.model.VfPaymentModeModel paymentModeModel2 = new com.inov8.verifly.common.model.VfPaymentModeModel();

  paymentModeModel1.setPrimaryKey(1L);
  paymentModeModel1.setName("Credit");
  paymentModeModel2.setPrimaryKey(2L);
  paymentModeModel2.setName("Debit");
  paymentModeModelList.add(paymentModeModel1);
  paymentModeModelList.add(paymentModeModel2);

//  if (referenceDataWrapper.getReferenceDataList() != null)
//  {
//    paymentModeModelList = referenceDataWrapper.getReferenceDataList();
//  }
//  Map referenceDataMap = new HashMap();
  referenceDataMap.put("paymentModeModelList", paymentModeModelList);



    /**
     * code fragment to load reference data  for Bank
     *
     */

    BankModel bankModel = new BankModel();
    referenceDataWrapper = new ReferenceDataWrapperImpl(
        bankModel, "name", SortingOrder.DESC);
    referenceDataWrapper.setBasePersistableModel(bankModel);
    try
    {
      referenceDataManager.getReferenceData(referenceDataWrapper);
    }
    catch (Exception e)
    {

    }
    List<BankModel> bankModelList = null;
    if (referenceDataWrapper.getReferenceDataList() != null)
    {
      bankModelList = referenceDataWrapper.
          getReferenceDataList();
    }
    referenceDataMap.put("bankModelList", bankModelList);

    return referenceDataMap;

  }

  @Override
  protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws
      Exception
  {
    id = ServletRequestUtils.getLongParameter(httpServletRequest,
                                              "smartMoneyAccountId");
    if (null != id)
    {
      if (log.isDebugEnabled())
      {
        log.debug("id is not null....retrieving object from DB");
      }

      SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
      SmartMoneyAccountVeriflyModel smartMoneyAccountVeriflyModel = new
          SmartMoneyAccountVeriflyModel();
      smartMoneyAccountVeriflyModel.setPrimaryKey(id);

      searchBaseWrapper.setBasePersistableModel(smartMoneyAccountVeriflyModel);
      searchBaseWrapper = this.smartMoneyAccountManager.loadSmartMoneyAccount(
          searchBaseWrapper);
      return (SmartMoneyAccountVeriflyModel) searchBaseWrapper.getBasePersistableModel();
    }
    else
    {
      if (log.isDebugEnabled())
      {
        log.debug("id is null....creating new instance of Model");
      }
      return new SmartMoneyAccountVeriflyModel();
    }
  }

  @Override
  protected ModelAndView onCreate(HttpServletRequest httpServletRequest,
                                  HttpServletResponse httpServletResponse,
                                  Object object, BindException errors) throws
      Exception
  {
    BaseWrapper baseWrapper = new BaseWrapperImpl();
    VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();
    try
    {
      int defAccountFlag = 0;
      if (httpServletRequest.getParameter("isDefaultAccount") != null)
      {
        defAccountFlag = Integer.parseInt(httpServletRequest.getParameter(
            "isDefaultAccount"));
        System.out.println("isDefaultAccount : " + defAccountFlag);
      }
      baseWrapper.putObject("defAccountFlag", defAccountFlag);
      SmartMoneyAccountVeriflyModel smartMoneyAccountVeriflyModel = (SmartMoneyAccountVeriflyModel)
          object;
      Long id;
      BankModel bankModel = new BankModel();
      AccountInfoModel accountInfoModel = new AccountInfoModel();
      id = smartMoneyAccountVeriflyModel.getBankId();
      smartMoneyAccountVeriflyModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
      smartMoneyAccountVeriflyModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
      baseWrapper.setBasePersistableModel(smartMoneyAccountVeriflyModel);
      baseWrapper = this.smartMoneyAccountManager.createSmartMoneyAccount(
          baseWrapper);

      accountInfoModel.setAccountInfoId(smartMoneyAccountVeriflyModel.getAccountInfoId());
      accountInfoModel.setAccountNick(smartMoneyAccountVeriflyModel.getAccountNick());
     accountInfoModel.setActive(smartMoneyAccountVeriflyModel.getActive());
     //accountInfoModel.setCardExpiryDate(smartMoneyAccountVeriflyModel.getCardExpiryDate());
     accountInfoModel.setCardNo(smartMoneyAccountVeriflyModel.getCardNo());
     accountInfoModel.setCardTypeId(smartMoneyAccountVeriflyModel.getCardTypeId());
     accountInfoModel.setCreatedOn(new Date());//smartMoneyAccountVeriflyModel.getCreatedOn());
     accountInfoModel.setCustomerId(smartMoneyAccountVeriflyModel.getCustomerId());
     accountInfoModel.setCustomerMobileNo(smartMoneyAccountVeriflyModel.getCustomerMobileNo());
     accountInfoModel.setFirstName(smartMoneyAccountVeriflyModel.getFirstName());
     accountInfoModel.setLastName(smartMoneyAccountVeriflyModel.getLastName());
     accountInfoModel.setPaymentModeId(smartMoneyAccountVeriflyModel.getPaymentModeId());
     accountInfoModel.setVersionNo(smartMoneyAccountVeriflyModel.getVersionNo());

      accountInfoModel.setUpdatedOn(new Date()); //smartMoneyAccountVeriflyModel.getUpdatedOn());
//      accountInfoModel.setVersionNo(smartMoneyAccountVeriflyModel.getVersionNo());
      bankModel.setBankId(id);
      VeriflyManager veriflyMgr = veriflyController.getVeriflyMgrByBankId(bankModel);
       veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
       veriflyBaseWrapper = veriflyMgr.generatePIN(veriflyBaseWrapper);






    //  if (null != baseWrapper.getBasePersistableModel());

      if(veriflyBaseWrapper.isErrorStatus())
      {
        this.saveMessage(httpServletRequest,
                         "Smart Money Account Saved Successfully");
        ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
        return modelAndView;
      }

      else
      {
        this.saveMessage(httpServletRequest,
                         veriflyBaseWrapper.getErrorMessage());
        return super.showForm(httpServletRequest, httpServletResponse, errors);
      }
    }
    catch (FrameworkCheckedException ex)
    {
      if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION ==
          ex.getErrorCode())
      {
        super.saveMessage(httpServletRequest,
                          "Smart Money Account Could Not Be Saved");
        return super.showForm(httpServletRequest, httpServletResponse, errors);
      }
      throw ex;
    }
    catch (Exception ex)
    {
        super.saveMessage(httpServletRequest,MessageUtil.getMessage("6075"));
        return super.showForm(httpServletRequest, httpServletResponse, errors);
    }
  }

  @Override
  protected ModelAndView onUpdate(HttpServletRequest httpServletRequest,
                                  HttpServletResponse httpServletResponse,
                                  Object object, BindException errors) throws
      Exception
  {
    BaseWrapper baseWrapper = new BaseWrapperImpl();
    try
    {
      Date nowDate = new Date();
      SmartMoneyAccountVeriflyModel smartMoneyAccountVeriflyModel = (SmartMoneyAccountVeriflyModel)
          object;

      System.out.println("Smart Money Account ID : " +
                         smartMoneyAccountVeriflyModel.getSmartMoneyAccountId());
      smartMoneyAccountVeriflyModel.setUpdatedOn(nowDate);
      smartMoneyAccountVeriflyModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
      baseWrapper.setBasePersistableModel(smartMoneyAccountVeriflyModel);
      baseWrapper = this.smartMoneyAccountManager.updateSmartMoneyAccount(
          baseWrapper);

      if (null != baseWrapper.getBasePersistableModel())
      {
        this.saveMessage(httpServletRequest,
                         "Smart Money Account Updated Successfully");
        ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
        return modelAndView;
      }
      else
      {
        this.saveMessage(httpServletRequest,
                         "Smart Money Account Could Not Be Updated");
        return super.showForm(httpServletRequest, httpServletResponse, errors);
      }
    }
    catch (FrameworkCheckedException ex)
    {
      if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION ==
          ex.getErrorCode())
      {
        super.saveMessage(httpServletRequest,
                          "Smart Money Account Could Not Be Updated");
        return super.showForm(httpServletRequest, httpServletResponse, errors);
      }
      throw ex;
    }
    catch (Exception ex)
    {
        super.saveMessage(httpServletRequest,MessageUtil.getMessage("6075"));
        return super.showForm(httpServletRequest, httpServletResponse, errors);
    }
  }

  public void setSmartMoneyAccountManager(SmartMoneyAccountManager
                                          smartMoneyAccountManager)
  {
    this.smartMoneyAccountManager = smartMoneyAccountManager;
  }

  public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
  {
    this.referenceDataManager = referenceDataManager;
  }

  public void setVeriflyController(VeriflyManagerService veriflyController)
  {
    this.veriflyController = veriflyController;
  }

}
