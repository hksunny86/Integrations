package com.inov8.microbank.server.service.smartmoneymodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.account.vo.DebitBlockVo;
import com.inov8.microbank.common.model.AdvanceSalaryLoanModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.smartmoneymodule.SmartMoneyAccountListViewModel;
import com.inov8.microbank.common.model.smartmoneymodule.SmartMoneyAccountVeriflyModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.vo.account.SmartMoneyAccountVO;
import com.inov8.microbank.server.dao.smartmoneymodule.SmartMoneyAccountDAO;
import com.inov8.microbank.server.dao.smartmoneymodule.SmartMoneyAccountListViewDAO;
import com.inov8.microbank.server.service.advancesalaryloan.dao.AdvanceSalaryLoanDAO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import org.hibernate.criterion.MatchMode;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import java.util.Date;
import java.util.List;

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

public class SmartMoneyAccountManagerImpl
        implements SmartMoneyAccountManager
{

  private SmartMoneyAccountDAO smartMoneyAccountDAO;
  private SmartMoneyAccountListViewDAO smartMoneyAccountListViewDAO;
  private AppUserManager appUserManager;
  private AdvanceSalaryLoanDAO advanceSalaryLoanDAO;

  public SmartMoneyAccountManagerImpl()
  {
  }

  public SearchBaseWrapper loadSmartMoneyAccount(SearchBaseWrapper
                                                         searchBaseWrapper)
  {
    SmartMoneyAccountModel smartMoneyAccountVeriflyModel = (SmartMoneyAccountVeriflyModel)
            searchBaseWrapper.getBasePersistableModel();
    SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
    smartMoneyAccountModel.setPrimaryKey(smartMoneyAccountVeriflyModel.getPrimaryKey());
    smartMoneyAccountModel = this.smartMoneyAccountDAO.
            findByPrimaryKey(searchBaseWrapper.getBasePersistableModel().
                    getPrimaryKey());

    smartMoneyAccountVeriflyModel.setPrimaryKey(smartMoneyAccountModel.getPrimaryKey());
    smartMoneyAccountVeriflyModel.setCustomerId(smartMoneyAccountModel.getCustomerId());
    smartMoneyAccountVeriflyModel.setBankId(smartMoneyAccountVeriflyModel.getBankId());
    smartMoneyAccountVeriflyModel.setName(smartMoneyAccountModel.getName());
    smartMoneyAccountVeriflyModel.setPaymentModeId(smartMoneyAccountModel.getPaymentModeId());
//    smartMoneyAccountVeriflyModel.setAccountNo(smartMoneyAccountModel.getAccountNo());
//    smartMoneyAccountVeriflyModel.setExpiryDate(smartMoneyAccountModel.getExpiryDate());
    smartMoneyAccountVeriflyModel.setDescription(smartMoneyAccountModel.getDescription());
    smartMoneyAccountVeriflyModel.setActive(smartMoneyAccountModel.getActive());




    searchBaseWrapper.setBasePersistableModel(smartMoneyAccountVeriflyModel);
    return searchBaseWrapper;
  }

  public BaseWrapper loadSmartMoneyAccount(BaseWrapper baseWrapper)
  {
    SmartMoneyAccountModel smartMoneyAccountModel = this.smartMoneyAccountDAO.
            findByPrimaryKey(baseWrapper.getBasePersistableModel().
                    getPrimaryKey());
    baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
    return baseWrapper;
  }

  public BaseWrapper searchSmartMoneyAccount(BaseWrapper
                                                     baseWrapper)
  {
    ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
    exampleHolder.setMatchMode(MatchMode.EXACT);
    exampleHolder.setEnableLike(false);
    exampleHolder.setIgnoreCase(false);
    CustomList<SmartMoneyAccountModel>
            list = this.smartMoneyAccountDAO.findByExample((SmartMoneyAccountModel)baseWrapper.getBasePersistableModel());
    if(null != list.getResultsetList() && !list.getResultsetList().isEmpty())
      baseWrapper.setBasePersistableModel(list.getResultsetList().get(0));
    else
      baseWrapper.setBasePersistableModel(null);
    return baseWrapper;
  }


  public BaseWrapper loadOLASmartMoneyAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException
  {

    SmartMoneyAccountModel sma = (SmartMoneyAccountModel) baseWrapper.getBasePersistableModel();
    Long retailerContactId = sma.getRetailerContactId();
    Long distributorContactId = sma.getDistributorContactId();
    Long handlerId = sma.getHandlerId();

    List<SmartMoneyAccountModel> list = this.smartMoneyAccountDAO.loadOLASmartMoneyAccount(retailerContactId, distributorContactId,handlerId);
		/*
		Iterator<SmartMoneyAccountModel> smaIte = list.iterator() ;
		
		while(smaIte.hasNext())
		{
			sma = smaIte.next() ;
			if( sma.getBankIdBankModel().getFinancialIntegrationId() != FinancialInstitutionConstants.OLA_FINANCIAL_INSTITUTION )
				smaIte.remove();			
		}
		*/
    if (list != null && list.size() > 0)
    {
      sma = list.get(0);
      baseWrapper.setBasePersistableModel(sma);
    }
    else
      baseWrapper.setBasePersistableModel(null);

    return baseWrapper;
  }

  public BaseWrapper loadOLASMAForRetOrDistHead(BaseWrapper baseWrapper) throws FrameworkCheckedException
  {
    Long retailerId = (Long)baseWrapper.getObject(WorkFlowErrorCodeConstants.RETAILER_ID);
    Long distributorId = (Long)baseWrapper.getObject(WorkFlowErrorCodeConstants.DISTRIBUTOR_ID);

    List<SmartMoneyAccountModel> list = this.smartMoneyAccountDAO.loadOLASMAForRetOrDistHead(retailerId, distributorId);

    if (list != null && list.size() > 0)
    {
      baseWrapper.setBasePersistableModel(list.get(0));
    }
    else
      baseWrapper.setBasePersistableModel(null);

    return baseWrapper;
  }

  public CustomList<SmartMoneyAccountModel> loadCustomerSmartMoneyAccountByHQL( SmartMoneyAccountModel smartMoneyAccountModel )
  {
    return smartMoneyAccountDAO.loadCustomerSmartMoneyAccountByHQL(smartMoneyAccountModel);
  }

  public SearchBaseWrapper searchSmartMoneyAccount(SearchBaseWrapper
                                                           searchBaseWrapper)
  {
    CustomList<SmartMoneyAccountListViewModel>
            list = this.smartMoneyAccountListViewDAO.findByExample( (
                    SmartMoneyAccountListViewModel)
                    searchBaseWrapper.getBasePersistableModel(),
            searchBaseWrapper.getPagingHelperModel(),
            searchBaseWrapper.getSortingOrderMap());
    searchBaseWrapper.setCustomList(list);
    return searchBaseWrapper;
  }

  public BaseWrapper updateSmartMoneyAccount(BaseWrapper baseWrapper)
  {
    SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel)
            baseWrapper.getBasePersistableModel();
    SmartMoneyAccountModel newSmartMoneyAccountModel = new
            SmartMoneyAccountModel();
    newSmartMoneyAccountModel.setSmartMoneyAccountId(smartMoneyAccountModel.
            getSmartMoneyAccountId());

    int recordCount = smartMoneyAccountDAO.countByExample(
            newSmartMoneyAccountModel);

    if (recordCount != 0 && smartMoneyAccountModel.getPrimaryKey() != null)
    {
      smartMoneyAccountModel = this.smartMoneyAccountDAO.saveOrUpdate( (
              SmartMoneyAccountModel) baseWrapper.getBasePersistableModel());
      baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
      return baseWrapper;
    }
    else
    {
      baseWrapper.setBasePersistableModel(null);
      return baseWrapper;
    }
  }

  public BaseWrapper createSmartMoneyAccount(BaseWrapper baseWrapper)
  {
    int recordCount;
    int defRecordCount;
    int defAccountFlag;
    Date nowDate = new Date();

    SmartMoneyAccountModel newSmartMoneyAccountModel = new
            SmartMoneyAccountModel();
    SmartMoneyAccountModel smartMoneyAccountVeriflyModel = (SmartMoneyAccountVeriflyModel)
            baseWrapper.getBasePersistableModel();
    SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
//    smartMoneyAccountModel.setAccountNo(smartMoneyAccountVeriflyModel.getAccountNo());
    smartMoneyAccountModel.setActive(smartMoneyAccountVeriflyModel.getActive());
    smartMoneyAccountModel.setCustomerId(smartMoneyAccountVeriflyModel.getCustomerId());
//    smartMoneyAccountModel.setExpiryDate(smartMoneyAccountModel.getExpiryDate());
    smartMoneyAccountModel.setName(smartMoneyAccountVeriflyModel.getName());
    smartMoneyAccountModel.setBankId(smartMoneyAccountVeriflyModel.getBankId());
    smartMoneyAccountModel.setDefAccount(smartMoneyAccountVeriflyModel.getDefAccount());
    smartMoneyAccountModel.setVersionNo(smartMoneyAccountVeriflyModel.getVersionNo());
    smartMoneyAccountModel.setCreatedBy(smartMoneyAccountVeriflyModel.getCreatedBy());
    smartMoneyAccountModel.setUpdatedBy(smartMoneyAccountVeriflyModel.getCreatedBy());
    smartMoneyAccountModel.setCreatedOn(new Date() );
    smartMoneyAccountModel.setUpdatedOn(new Date());
    smartMoneyAccountModel.setPaymentModeId(smartMoneyAccountVeriflyModel.getPaymentModeId());
    smartMoneyAccountModel.setChangePinRequired(smartMoneyAccountVeriflyModel.getChangePinRequired());
    smartMoneyAccountModel.setSmartMoneyAccountId(smartMoneyAccountVeriflyModel.getSmartMoneyAccountId());
    smartMoneyAccountModel.setDescription(smartMoneyAccountVeriflyModel.getDescription());

//    smartMoneyAccountModel.setUpdatedByAppUserModel(smartMoneyAccountVeriflyModel.gUserUtils.getCurrentUser());
//     smartMoneyAccountModel.setCreatedByAppUserModel(smartMoneyAccountVeriflyModel.UserUtils.getCurrentUser());

    SmartMoneyAccountModel defaultSmartMoneyAccountModel = new
            SmartMoneyAccountModel();

    newSmartMoneyAccountModel.setName(smartMoneyAccountModel.getName());

    defaultSmartMoneyAccountModel.setCustomerId(smartMoneyAccountModel.
            getCustomerId());
    defaultSmartMoneyAccountModel.setDefAccount(Boolean.TRUE);
    defaultSmartMoneyAccountModel.setActive(Boolean.TRUE);

    defRecordCount = smartMoneyAccountDAO.countByExample(
            defaultSmartMoneyAccountModel);

    recordCount = smartMoneyAccountDAO.countByExample(smartMoneyAccountModel);

    defAccountFlag = Integer.parseInt(baseWrapper.getObject("defAccountFlag").
            toString());

    //***Check if Record already exists

    smartMoneyAccountModel.setCreatedOn(nowDate);
    smartMoneyAccountModel.setUpdatedOn(nowDate);
    if (defAccountFlag == 0)
    {
      smartMoneyAccountModel.setDefAccount(Boolean.FALSE);
    }
    else
    {
      smartMoneyAccountModel.setDefAccount(Boolean.TRUE);
    }

    if (recordCount == 0 && defRecordCount == 0)
    {
      baseWrapper.setBasePersistableModel(this.smartMoneyAccountDAO.
              saveOrUpdate(smartMoneyAccountModel));
      return baseWrapper;
    }
    else if (recordCount == 0 && defAccountFlag == 1 && defRecordCount > 0)
    {
      CustomList<SmartMoneyAccountModel>
              customList = (CustomList<SmartMoneyAccountModel>)
              smartMoneyAccountDAO.findByExample(defaultSmartMoneyAccountModel);
      List<SmartMoneyAccountModel> list = customList.getResultsetList();

      for (SmartMoneyAccountModel localSmartMoneyAccountModel : list)
      {
        localSmartMoneyAccountModel.setDefAccount(Boolean.FALSE);
      }

      baseWrapper.setBasePersistableModel(this.smartMoneyAccountDAO.
              saveOrUpdate(smartMoneyAccountModel));
      defAccountFlag = 0;
      return baseWrapper;
    }
    else if (recordCount == 0 && defAccountFlag == 0 && defRecordCount > 0)
    {
      baseWrapper.setBasePersistableModel(this.smartMoneyAccountDAO.
              saveOrUpdate(smartMoneyAccountModel));
      return baseWrapper;
    }
    else
    {
      baseWrapper.setBasePersistableModel(null);
      return baseWrapper;
    }
  }


  public SmartMoneyAccountModel getSMAccountByRetailerContactId(Long retailerContactId)
          throws FrameworkCheckedException {
    SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
    smartMoneyAccountModel.setRetailerContactId(retailerContactId);
    smartMoneyAccountModel.setActive(true);
    smartMoneyAccountModel.setDeleted(false);
    CustomList<SmartMoneyAccountModel> results = smartMoneyAccountDAO.findByExample(smartMoneyAccountModel);
    if(results != null && results.getResultsetList().size() > 0){
      return results.getResultsetList().get(0);
    }
    return null;
  }

  public SmartMoneyAccountModel getSMALinkedWithCoreAccount(Long retailerContactId) throws FrameworkCheckedException
  {
    SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
    smartMoneyAccountModel.setBankId(BankConstantsInterface.ASKARI_BANK_ID);
    smartMoneyAccountModel.setRetailerContactId(retailerContactId);
    smartMoneyAccountModel.setActive(true);
    smartMoneyAccountModel.setDeleted(false);
    smartMoneyAccountModel.setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);
    CustomList<SmartMoneyAccountModel> results = smartMoneyAccountDAO.findByExample(smartMoneyAccountModel);
    if(results != null && results.getResultsetList().size() > 0){
      return results.getResultsetList().get(0);
    }
    return null;
  }

  public SmartMoneyAccountModel getSMAccountByRetailer(RetailerContactModel retailerContactModel) throws FrameworkCheckedException {

    SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
    smartMoneyAccountModel.setRetailerContactIdRetailerContactModel(retailerContactModel);

    CustomList<SmartMoneyAccountModel> results = smartMoneyAccountDAO.findByExample(smartMoneyAccountModel);

    if(results != null && results.getResultsetList().size() > 0) {

      return results.getResultsetList().get(0);
    }

    return null;
  }

  /**
   * @param appUserModel
   * @return
   */
  public AppUserModel getAppUserModel(AppUserModel appUserModel) {
    return appUserManager.getAppUserModel(appUserModel);
  }

  @Override
  public void blockSmartMoneyAccount(AppUserModel appUserModel) throws Exception {

    SmartMoneyAccountModel smaModel = loadSmartMoneyAccountModel(appUserModel);
    if(smaModel == null)
      return;

    //set MPIN change required to true
    smaModel.setChangePinRequired(Boolean.TRUE);
    smartMoneyAccountDAO.saveOrUpdate(smaModel);
  }


  public void setSmartMoneyAccountDAO(SmartMoneyAccountDAO smartMoneyAccountDAO)
  {
    this.smartMoneyAccountDAO = smartMoneyAccountDAO;
  }

  public void setSmartMoneyAccountListViewDAO(SmartMoneyAccountListViewDAO
                                                      smartMoneyAccountListViewDAO)
  {
    this.smartMoneyAccountListViewDAO = smartMoneyAccountListViewDAO;
  }

  public void setAppUserManager(AppUserManager appUserManager)
  {
    this.appUserManager = appUserManager;
  }

  public int countSmartMoneyAccountModel(BaseWrapper baseWrapper)
          throws FrameworkCheckedException {
    SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel)
            baseWrapper.getBasePersistableModel();

    CustomList<SmartMoneyAccountModel> results = smartMoneyAccountDAO.findByExample(smartMoneyAccountModel);
    if(null != results && null != results.getResultsetList())
    {
      return results.getResultsetList().size();
    }

    return 0;
  }

  public SmartMoneyAccountModel getSMAccountByHandlerId(Long handlerId) throws FrameworkCheckedException {

    SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
    smartMoneyAccountModel.setHandlerId(handlerId);
    smartMoneyAccountModel.setActive(true);
    smartMoneyAccountModel.setDeleted(false);
    CustomList<SmartMoneyAccountModel> results = smartMoneyAccountDAO.findByExample(smartMoneyAccountModel);
    if(results != null && results.getResultsetList().size() > 0){
      return results.getResultsetList().get(0);
    }
    return null;
  }

  @Override
  public SmartMoneyAccountModel loadSmartMoneyAccountModel(AppUserModel appUserModel, Long paymentModeId) {
    if(null == appUserModel)
      return null;

    SmartMoneyAccountModel smaModel = new SmartMoneyAccountModel();
    long appUserTypeId = appUserModel.getAppUserTypeId();
    if(UserTypeConstantsInterface.RETAILER == appUserTypeId)
      smaModel.setRetailerContactId(appUserModel.getRetailerContactId());

    else if(UserTypeConstantsInterface.HANDLER == appUserTypeId)
      smaModel.setHandlerId(appUserModel.getHandlerId());

    else if(UserTypeConstantsInterface.CUSTOMER == appUserTypeId)
      smaModel.setCustomerId(appUserModel.getCustomerId());

    //TODO handle walkin case as well
    //else if(UserTypeConstantsInterface.WALKIN_CUSTOMER == appUserTypeId)
    //	smaModel.setWalkinCustomerId(appUserModel.getWalkinCustomerId());

    smaModel.setPaymentModeId(paymentModeId);
    smaModel.setActive(Boolean.TRUE);
    smaModel.setDeleted(Boolean.FALSE);

    BaseWrapper baseWrapper = new BaseWrapperImpl();
    baseWrapper.setBasePersistableModel(smaModel);
    searchSmartMoneyAccount(baseWrapper);

    smaModel = (SmartMoneyAccountModel)baseWrapper.getBasePersistableModel();

    return smaModel;
  }

  private SmartMoneyAccountModel converVOToModel(SmartMoneyAccountVO smartMoneyAccountVO)
  {
    SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
    //
    smartMoneyAccountModel.setSmartMoneyAccountId(smartMoneyAccountVO.getSmartMoneyAccountId());
    smartMoneyAccountModel.setBankId(smartMoneyAccountVO.getBankId());
    smartMoneyAccountModel.setPaymentModeId(smartMoneyAccountVO.getPaymentModeId());
    smartMoneyAccountModel.setCustomerId(smartMoneyAccountVO.getCustomerId());
    smartMoneyAccountModel.setCreatedBy(smartMoneyAccountVO.getCreatedByAppUserId());
    smartMoneyAccountModel.setUpdatedBy(smartMoneyAccountVO.getUpdatedByAppUserId());

    smartMoneyAccountModel.setName(smartMoneyAccountVO.getName());
    smartMoneyAccountModel.setStatusId(smartMoneyAccountVO.getStatusId());
    smartMoneyAccountModel.setActive(smartMoneyAccountVO.getActive());
    smartMoneyAccountModel.setDefAccount(smartMoneyAccountVO.getDefAccount());
    smartMoneyAccountModel.setChangePinRequired(smartMoneyAccountVO.getChangePinRequired());
    smartMoneyAccountModel.setDormancyRemovedOn(smartMoneyAccountVO.getDormancyUpdatedOn());
    smartMoneyAccountModel.setCreatedOn(smartMoneyAccountVO.getCREATED_ON());
    smartMoneyAccountModel.setUpdatedOn(smartMoneyAccountVO.getUpdatedOn());
    smartMoneyAccountModel.setVersionNo(smartMoneyAccountVO.getVersionNo());
    smartMoneyAccountModel.setDeleted(smartMoneyAccountVO.getDeleted());
    smartMoneyAccountModel.setRegistrationStateId(smartMoneyAccountVO.getRegistrationStateId());
    smartMoneyAccountModel.setPreviousRegStateId(RegistrationStateConstantsInterface.DORMANT);
    smartMoneyAccountModel.setAccountStateId(smartMoneyAccountVO.getAccountStateId());
    smartMoneyAccountModel.setComments(smartMoneyAccountVO.getComments());
    //
    return smartMoneyAccountModel;
  }

  @Override
  public BaseWrapper updateSmartMoneyAccountDormancyWithAuthorization(BaseWrapper baseWrapper) throws FrameworkCheckedException {
    SmartMoneyAccountVO smartMoneyAccountVO = (SmartMoneyAccountVO)baseWrapper.getBasePersistableModel();

    SmartMoneyAccountModel smartMoneyAccountModel = this.converVOToModel(smartMoneyAccountVO);
    smartMoneyAccountModel.setDormancyRemovedOn(new Date());

    this.smartMoneyAccountDAO.saveOrUpdate(smartMoneyAccountModel);

    if(smartMoneyAccountModel.getPaymentModeId().equals(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT))
    {
      AppUserModel appUserModel = (AppUserModel)baseWrapper.getObject("appUserModel");
      if(appUserModel == null)
      {
          appUserModel = this.appUserManager.loadAppUser(smartMoneyAccountVO.getAppUserId());
          appUserModel.setAccountStateId(AccountStateConstants.ACCOUNT_STATE_COLD);
      }
      appUserModel.setRegistrationStateId(smartMoneyAccountModel.getRegistrationStateId());
      appUserModel.setPrevRegistrationStateId(RegistrationStateConstantsInterface.DORMANT);
      appUserModel.setClosingComments(smartMoneyAccountVO.getComments());
      appUserModel.setDormancyRemovedBy(UserUtils.getCurrentUser().getAppUserId());
      appUserModel.setDormancyRemovedOn(new Date());
      BaseWrapper appUserWrapper = new BaseWrapperImpl();
      appUserWrapper.setBasePersistableModel(appUserModel);
      appUserManager.saveOrUpdateAppUser(appUserWrapper);
    }
    return baseWrapper;
  }

  @Override
  public SmartMoneyAccountModel loadSmartMoneyAccountModel(AppUserModel appUserModel) {
    return loadSmartMoneyAccountModel(appUserModel, PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
  }

  @Override
  public SmartMoneyAccountModel getSmartMoneyAccountByCustomerIdAndPaymentModeId(SmartMoneyAccountModel smartMoneyAccountModel) throws FrameworkCheckedException {
    CustomList<SmartMoneyAccountModel> customList = this.smartMoneyAccountDAO.findByExample(smartMoneyAccountModel);
    SmartMoneyAccountModel sma = null;
    if(customList.getResultsetList().size() > 0 )
    {
      sma=customList.getResultsetList().get(0);
    }
    return sma;
  }
  @Override
  public SmartMoneyAccountModel getInActiveSMA(AppUserModel appUserModel, Long paymentModeId,Long statusId)
  {
    if(null == appUserModel)
      return null;

    SmartMoneyAccountModel smaModel = new SmartMoneyAccountModel();
    long appUserTypeId = appUserModel.getAppUserTypeId();
    if(UserTypeConstantsInterface.RETAILER == appUserTypeId)
      smaModel.setRetailerContactId(appUserModel.getRetailerContactId());

    else if(UserTypeConstantsInterface.HANDLER == appUserTypeId)
      smaModel.setHandlerId(appUserModel.getHandlerId());

    else if(UserTypeConstantsInterface.CUSTOMER == appUserTypeId)
      smaModel.setCustomerId(appUserModel.getCustomerId());

    //
    //else if(UserTypeConstantsInterface.WALKIN_CUSTOMER == appUserTypeId)
    //	smaModel.setWalkinCustomerId(appUserModel.getWalkinCustomerId());

    smaModel.setPaymentModeId(paymentModeId);
    smaModel.setDeleted(Boolean.FALSE);
    smaModel.setStatusId(statusId);

    BaseWrapper baseWrapper = new BaseWrapperImpl();
    baseWrapper.setBasePersistableModel(smaModel);
    searchSmartMoneyAccount(baseWrapper);

    smaModel = (SmartMoneyAccountModel)baseWrapper.getBasePersistableModel();

    return smaModel;
  }

  @Override
  public BaseWrapper updateDebitBlockWithAuthorization(BaseWrapper baseWrapper) throws FrameworkCheckedException {
    DebitBlockVo debitBlockVo = (DebitBlockVo) baseWrapper.getBasePersistableModel();

    AppUserModel appUserModel= null;
    try {
      appUserModel = appUserManager.loadAppUser(debitBlockVo.getAppUserId());
    } catch (Exception e) {
      e.printStackTrace();
    }

    Double blockedAmount;
    SmartMoneyAccountModel smartMoneyAccountModel;
    AdvanceSalaryLoanModel advanceSalaryLoanModel = new AdvanceSalaryLoanModel();
    advanceSalaryLoanModel.setMobileNo(appUserModel.getMobileNo());

      advanceSalaryLoanModel = getCommonCommandManager().getAdvanceSalaryLoanDAO().loadAdvanceSalaryLoanByMobileNumber(appUserModel.getMobileNo());

      if (appUserModel.getAppUserTypeId().equals(UserTypeConstantsInterface.CUSTOMER))
      smartMoneyAccountModel = getSMAccountByCustomerId(appUserModel.getCustomerId());
    else if (appUserModel.getAppUserTypeId().equals(UserTypeConstantsInterface.RETAILER))
      smartMoneyAccountModel = getSMAccountByRetailerContactId(appUserModel.getRetailerContactId());
    else
      smartMoneyAccountModel = getSMAccountByHandlerId(appUserModel.getHandlerId());

    baseWrapper = new BaseWrapperImpl();
    baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
    baseWrapper = loadSmartMoneyAccount(baseWrapper);
    smartMoneyAccountModel = (SmartMoneyAccountModel) baseWrapper.getBasePersistableModel();

    if (debitBlockVo.getIsDebitBlocked()) {
      if (debitBlockVo.getAmount() == null || debitBlockVo.getAmount() == 0) {
        smartMoneyAccountModel.setDebitBlockAmount(null);
      } else {
        blockedAmount = (Double) CommonUtils.getDefaultIfNull(smartMoneyAccountModel.getDebitBlockAmount(), 0.0);
        smartMoneyAccountModel.setDebitBlockAmount(debitBlockVo.getAmount() + blockedAmount);
      }
      smartMoneyAccountModel.setIsDebitBlocked(true);
      if(advanceSalaryLoanModel != null) {
        advanceSalaryLoanModel.setIsDebitBlock(true);
      }
    } else {
      blockedAmount = (Double) CommonUtils.getDefaultIfNull(smartMoneyAccountModel.getDebitBlockAmount(), 0.0);
      Double amount = blockedAmount - debitBlockVo.getAmount();
      if (amount < 0) {
        throw new FrameworkCheckedException(MessageUtil.getMessage("account.debit.unblocked"), "", ErrorCodes.DEBIT_BLOCKED, null);

      }
      smartMoneyAccountModel.setDebitBlockAmount(amount);
      if (amount > 0) {
        smartMoneyAccountModel.setIsDebitBlocked(true);
        if(advanceSalaryLoanModel != null) {
          advanceSalaryLoanModel.setIsDebitBlock(true);
        }
      } else {
        smartMoneyAccountModel.setIsDebitBlocked(false);
        if(advanceSalaryLoanModel != null) {
          advanceSalaryLoanModel.setIsDebitBlock(false);

            Date date = new Date();
            int noOfMonths = (date.getMonth() + 1) - (advanceSalaryLoanModel.getLastPaymentDate().getMonth() + 1);

            advanceSalaryLoanModel.setNoOfInstallmentPaid(advanceSalaryLoanModel.getNoOfInstallmentPaid() + noOfMonths);
            advanceSalaryLoanModel.setLastPaymentDate(new Date());

            debitBlockVo.setNoOfMonths(Long.valueOf(noOfMonths));

          if (advanceSalaryLoanModel.getNoOfInstallment().equals(advanceSalaryLoanModel.getNoOfInstallmentPaid())) {
            advanceSalaryLoanModel.setIsCompleted(true);
          }
        }
      }
    }

    smartMoneyAccountModel.setUpdatedOn(new Date());
    smartMoneyAccountModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());

    smartMoneyAccountModel = this.smartMoneyAccountDAO.saveOrUpdate(smartMoneyAccountModel);
    if(advanceSalaryLoanModel != null) {
      advanceSalaryLoanModel = this.advanceSalaryLoanDAO.saveOrUpdate(advanceSalaryLoanModel);
    }
    baseWrapper.setBasePersistableModel(smartMoneyAccountModel);

    return baseWrapper;
  }

  @Override
  public BaseWrapper updateDebitBlock(Long appUserId, Double amount, Boolean isDebitBlocked) throws FrameworkCheckedException {
    BaseWrapper baseWrapper = new BaseWrapperImpl();

    AppUserModel appUserModel= null;
    try {
      appUserModel = appUserManager.loadAppUser(appUserId);
    } catch (Exception e) {
      e.printStackTrace();
    }

    SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();

    if (appUserModel.getAppUserTypeId().equals(UserTypeConstantsInterface.CUSTOMER))
      smartMoneyAccountModel = getSMAccountByCustomerId(appUserModel.getCustomerId());
    else if (appUserModel.getAppUserTypeId().equals(UserTypeConstantsInterface.RETAILER))
      smartMoneyAccountModel = getSMAccountByRetailerContactId(appUserModel.getRetailerContactId());
    else
      smartMoneyAccountModel = getSMAccountByHandlerId(appUserModel.getHandlerId());

    baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
    baseWrapper = loadSmartMoneyAccount(baseWrapper);
    smartMoneyAccountModel = (SmartMoneyAccountModel) baseWrapper.getBasePersistableModel();

    if (isDebitBlocked) {
      if (amount == null || amount == 0) {
        smartMoneyAccountModel.setDebitBlockAmount(null);
      } else {
        smartMoneyAccountModel.setDebitBlockAmount(amount);
      }
      smartMoneyAccountModel.setIsDebitBlocked(true);
    } else {
      smartMoneyAccountModel.setDebitBlockAmount(null);
      smartMoneyAccountModel.setIsDebitBlocked(false);
    }


    smartMoneyAccountModel.setUpdatedOn(new Date());
    smartMoneyAccountModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());

    smartMoneyAccountModel = this.smartMoneyAccountDAO.saveOrUpdate(smartMoneyAccountModel);

    baseWrapper.setBasePersistableModel(smartMoneyAccountModel);

    return baseWrapper;
  }

  @Override
  public void validateDebitBlock(SmartMoneyAccountModel smaModel, Double txAmount, Double balance) throws FrameworkCheckedException {
    if (smaModel == null) {
      throw new FrameworkCheckedException("Invalid Account");
    }

    String messageKey = "account.debit.blocked";
    Boolean debitBlock = smaModel.getIsDebitBlocked();
    if (debitBlock == null || !debitBlock) {

      // account is not debit blocked
      return;
    }

    txAmount = CommonUtils.getDoubleOrDefaultValue(txAmount);

    Double blockAmount = CommonUtils.getDoubleOrDefaultValue(smaModel.getDebitBlockAmount());
    balance = CommonUtils.getDoubleOrDefaultValue(balance);
    double diff = balance - blockAmount;
    if (diff > 0.0) {
      messageKey = "account.debit.blocked.with.amount";
    }

    if (txAmount > diff) {
      throw new FrameworkCheckedException(MessageUtil.getMessage(messageKey, Formatter.formatNumbers(diff)), "", ErrorCodes.DEBIT_BLOCKED, null);
    }
  }

  public SmartMoneyAccountModel getSMAccountByCustomerId(Long customerId)
          throws FrameworkCheckedException {
    SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
    smartMoneyAccountModel.setCustomerId(customerId);
    smartMoneyAccountModel.setActive(true);
    smartMoneyAccountModel.setDeleted(false);
    CustomList<SmartMoneyAccountModel> results = smartMoneyAccountDAO.findByExample(smartMoneyAccountModel);
    if (results != null && results.getResultsetList().size() > 0) {
      return results.getResultsetList().get(0);
    }
    return null;
  }

    public CommonCommandManager getCommonCommandManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (CommonCommandManager) applicationContext.getBean("commonCommandManager");
    }

    public void setAdvanceSalaryLoanDAO(AdvanceSalaryLoanDAO advanceSalaryLoanDAO) {
        this.advanceSalaryLoanDAO = advanceSalaryLoanDAO;
    }
}
