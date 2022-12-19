package com.inov8.microbank.server.service.settlementmodule;

/**
 * <p>Title: Settlement Manager</p>
 *
 * <p>Description: It is a main cladd of Settlement module whose purpose is to
 * settle the calculated commission with the bank and update the respective credit registers
 * of the various Stake Holders on the basis of the Transaction Type</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Syed Ahmad Bilal
 * @v
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AllpayCommissionTransactionModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.CommissionRateModel;
import com.inov8.microbank.common.model.CommissionTransactionModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.SettlementTransactionModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.StakeholderBankInfoModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.CommissionReasonConstants;
import com.inov8.microbank.common.util.FinancialInstitutionConstants;
import com.inov8.microbank.common.util.InternetCompanyEnum;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.PoolAccountConstantsInterface;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.TaxValueBean;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.util.UtilityCompanyEnum;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.dao.allpaymodule.AllPayCommissionTransactionDAO;
import com.inov8.microbank.server.dao.commissionmodule.CommissionStakeholderDAO;
import com.inov8.microbank.server.dao.commissionmodule.CommissionTransactionDAO;
import com.inov8.microbank.server.dao.dailyjob.SettlementTransactionDAO;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;
import com.inov8.microbank.server.dao.settlementmodule.VeriflyDAO;
import com.inov8.microbank.server.dao.stakeholdermodule.StakeholderBankInfoDAO;
import com.inov8.microbank.server.service.bankmodule.BankManager;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.creditmodule.DistDistCreditManager;
import com.inov8.microbank.server.service.creditmodule.DistRetCreditManager;
import com.inov8.microbank.server.service.creditmodule.Inov8CustomerCreditManager;
import com.inov8.microbank.server.service.creditmodule.Inov8DistributorCreditManager;
import com.inov8.microbank.server.service.creditmodule.RetCustomerCreditManager;
import com.inov8.microbank.server.service.creditmodule.RetHeadRetCreditManager;
import com.inov8.microbank.server.service.creditmodule.RetRetCreditManager;
import com.inov8.microbank.server.service.distributormodule.DistributorContactManager;
import com.inov8.microbank.server.service.distributormodule.DistributorManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchController;
import com.inov8.microbank.server.service.financialintegrationmodule.veriflymodule.VeriflyManagerService;
import com.inov8.microbank.server.service.operatormodule.OperatorManager;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.retailermodule.RetailerManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;
import com.inov8.ola.integration.vo.OLAInfo;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.model.LogModel;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl;
import com.inov8.verifly.server.service.mainmodule.VeriflyManager;

public class SettlementManagerImpl
    implements SettlementManager
{
  private VeriflyDAO veriflyDAO;

  private Inov8DistributorCreditManager inov8DistributorCreditManager;
  private RetHeadRetCreditManager retHeadRetCreditManager;
  private RetRetCreditManager retRetCreditManager;
  private DistDistCreditManager distDistCreditManager;
  private DistRetCreditManager distRetCreditManager;
  private Inov8CustomerCreditManager inov8CustomerCreditManager;
  private RetCustomerCreditManager retCustomerCreditManager;
  private StakeholderBankInfoDAO stakeholderBankInfoDAO;
  private CommissionStakeholderDAO commissionStakeholderDAO;
  private RetailerManager retailerManager;
  private DistributorManager distributorManager;
  private OperatorManager operatorManager;
  private RetailerContactManager retailerContactManager;
  private DistributorContactManager distributorContactManager;
  private CommissionTransactionDAO commissionTransactionDAO;
  private SwitchController switchController;
  private VeriflyManagerService veriflyController;
  private AllPayCommissionTransactionDAO allpayCommissionTransactionDAO;
  private SmartMoneyAccountManager smartMoneyAccountManager;
  private AppUserDAO appUserDAO;
  private FinancialIntegrationManager financialIntegrationManager;
  private BankManager bankManager;
  private SettlementTransactionDAO settlementTransactionDAO;
  private StakeholderBankInfoManager stakeholderBankInfoManager;
  
  protected final transient Log logger = LogFactory.getLog(SettlementManagerImpl.class);

  
  public void settleAllPayCommission(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException
  {
	  System.out.println("-----------> settle AllPay Commission method called ");
	  BaseWrapper baseWrapper = new BaseWrapperImpl();
	  
	  this.settleCommission(commissionWrapper, workFlowWrapper);
	  	  
	  List<AllpayCommissionTransactionModel> commTransactionList = (List<AllpayCommissionTransactionModel>)commissionWrapper.getObject(CommissionConstantsInterface.ALLPAY_COMMISSION_TRANSACTION_LIST);
	  
	  if( commTransactionList != null )
	  for( AllpayCommissionTransactionModel allpayCommTrans : commTransactionList )
	  {
		  
		  
		  try
			{
			  allpayCommTrans.setTransactionDetailId( ((TransactionDetailModel) ((List) workFlowWrapper.getTransactionModel()
						.getTransactionIdTransactionDetailModelList()).get(0)).getTransactionDetailId() );
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		  
		  // First saving the commission in AllPay_Commission_Transaction table
		  allpayCommTrans = this.allpayCommissionTransactionDAO.saveOrUpdate(allpayCommTrans) ;
		  
		  // Now retrieving the OLA SmartMoneyAccount for National Distributor's Head
		  baseWrapper = new BaseWrapperImpl();
		  baseWrapper.putObject(WorkFlowErrorCodeConstants.DISTRIBUTOR_ID, allpayCommTrans.getNationalDistributorId()) ;
		  baseWrapper = this.smartMoneyAccountManager.loadOLASMAForRetOrDistHead(baseWrapper);
		  baseWrapper.putObject(WorkFlowErrorCodeConstants.DISTRIBUTOR_ID, null) ;
		  SmartMoneyAccountModel sma = (SmartMoneyAccountModel)baseWrapper.getBasePersistableModel() ;
		  
		  if( sma != null )
		  {
			  System.out.println("-----> Smart Money of National Distributor : " + sma.getName());
			  
			  AppUserModel appUserModel = new AppUserModel() ;
			  appUserModel.setDistributorContactId(sma.getDistributorContactId());
			  appUserModel = this.getAppUserModel(appUserModel) ;
			  
			  // Prepare SwtichWrapper and pass that to Swtich Controller
			  SwitchWrapper switchWrapper = this.prepareSwitchWrapper(sma, workFlowWrapper) ;
			  switchWrapper.setAmountDue(allpayCommTrans.getNationalDistCalculatedComm());
			  switchWrapper.setCommissionAppUserModel(appUserModel);
			  switchWrapper.putObject(WorkFlowErrorCodeConstants.ALLPAY_COMM_TRANS_ID, allpayCommTrans.getPrimaryKey()) ;
			  switchWrapper.setCommissionStakeHolderType(CommissionConstantsInterface.NATIONAL_DISTRIBUTOR_STAKEHOLDER);
			  baseWrapper.setBasePersistableModel(sma);
			  
			  AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);
			  try
			  {
				  abstractFinancialInstitution.settleCommission(switchWrapper);
			  }
			  catch (Exception e)
			  {
				  e.printStackTrace();
			  }
		  }
		  
		  
		  // Now retrieving the OLA SmartMoneyAccount for Distributor's Head
		  baseWrapper.putObject(WorkFlowErrorCodeConstants.DISTRIBUTOR_ID, allpayCommTrans.getDistributorId()) ;
		  baseWrapper = this.smartMoneyAccountManager.loadOLASMAForRetOrDistHead(baseWrapper);
		  baseWrapper.putObject(WorkFlowErrorCodeConstants.DISTRIBUTOR_ID, null) ;
		  sma = (SmartMoneyAccountModel)baseWrapper.getBasePersistableModel() ;
		  		  
		  if( sma != null )
		  {
			  System.out.println("-----> Smart Money of Distributor : " + sma.getName());
			  
			  AppUserModel appUserModel = new AppUserModel() ;
			  appUserModel.setDistributorContactId(sma.getDistributorContactId());
			  appUserModel = this.getAppUserModel(appUserModel) ;
			  
			  // Prepare SwtichWrapper and pass that to Swtich Controller
			  SwitchWrapper switchWrapper = this.prepareSwitchWrapper(sma, workFlowWrapper) ;
			  switchWrapper.setAmountDue(allpayCommTrans.getDistributorCalculatedComm());
			  switchWrapper.setCommissionAppUserModel(appUserModel);
			  switchWrapper.putObject(WorkFlowErrorCodeConstants.ALLPAY_COMM_TRANS_ID, allpayCommTrans.getPrimaryKey()) ;
			  switchWrapper.setCommissionStakeHolderType(CommissionConstantsInterface.DISTRIBUTOR_STAKEHOLDER);
			  
			  baseWrapper.setBasePersistableModel(sma);
			  
			  AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);
			  try
			  {
				  abstractFinancialInstitution.settleCommission(switchWrapper);
			  }
			  catch (Exception e)
			  {
				  e.printStackTrace();
			  }
		  }
		  
		  // Now retrieving the OLA SmartMoneyAccount for Retailer's Head
		  baseWrapper.putObject(WorkFlowErrorCodeConstants.RETAILER_ID, allpayCommTrans.getRetailerId()) ;
		  baseWrapper = this.smartMoneyAccountManager.loadOLASMAForRetOrDistHead(baseWrapper);
		  sma = (SmartMoneyAccountModel)baseWrapper.getBasePersistableModel() ;
		  
		  
		  if( sma != null )
		  {
			  System.out.println("-----> Smart Money of Retailer : " + sma.getName());
			  
			  AppUserModel appUserModel = new AppUserModel() ;
			  appUserModel.setRetailerContactId(sma.getRetailerContactId());
			  appUserModel = this.getAppUserModel(appUserModel) ;
			  
			  // Prepare SwtichWrapper and pass that to Swtich Controller
			  SwitchWrapper switchWrapper = this.prepareSwitchWrapper(sma, workFlowWrapper) ;
			  switchWrapper.setAmountDue(allpayCommTrans.getRetailerCalculatedComm());
			  switchWrapper.setCommissionAppUserModel(appUserModel);
			  switchWrapper.putObject(WorkFlowErrorCodeConstants.ALLPAY_COMM_TRANS_ID, allpayCommTrans.getPrimaryKey()) ;
			  switchWrapper.setCommissionStakeHolderType(CommissionConstantsInterface.RETAILER_STAKEHOLDER);
			  
			  baseWrapper.setBasePersistableModel(sma);
			  
			  AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);
			  try
			  {
				  abstractFinancialInstitution.settleCommission(switchWrapper);
			  }
			  catch (Exception e)
			  {
				  e.printStackTrace();
			  }
		  }
		  
		  		
		  
		  
	  }	  
	  
	// Now settling Inov8 commission
	  // Prepare SwtichWrapper and pass that to Swtich Controller
	  
	  Long olaBankId = this.getOLABankId();
	  SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
	  
	  if( olaBankId != null )
	  {
		  SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		  switchWrapper.setBankId(olaBankId);
		  switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
		  CommissionAmountsHolder commissionAmounts = (CommissionAmountsHolder)commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
		  
		  if( commissionAmounts.getI8ServiceCharges() + commissionAmounts.getI8SupplierCharges() > 0 )
		  {
			  switchWrapper.setWorkFlowWrapper(workFlowWrapper);
			  
			  BankModel bankModel = new BankModel();
			  bankModel.setBankId(olaBankId);
			  
			  sma = new SmartMoneyAccountModel();
			  sma.setBankId(olaBankId);
			  sma.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
			  
			  switchWrapper.setAmountDue(commissionAmounts.getI8ServiceCharges()+commissionAmounts.getI8SupplierCharges());
			  switchWrapper.setOlaCommissionSMA(sma);
			  
			  baseWrapper.setBasePersistableModel(bankModel);
			  
			  AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);
			  try
			  {
				  abstractFinancialInstitution.settleInov8Commission(switchWrapper);
			  }
			  catch (Exception e)
			  {
				  e.printStackTrace();
			  }
		  }
	  }

	  
  }
  
  
  
  private SwitchWrapper prepareSwitchWrapper( SmartMoneyAccountModel sma, WorkFlowWrapper workFlowWrapper )
  {
	  SwitchWrapper switchWrapper = new SwitchWrapperImpl();
	  
	  switchWrapper.setOlaCommissionSMA(sma);
	  switchWrapper.setPaymentModeId(sma.getPaymentModeId());
	  switchWrapper.setBankId(sma.getBankId());
	  switchWrapper.setWorkFlowWrapper(workFlowWrapper);
	  
	  return switchWrapper;	  
  }
  
  private AppUserModel getAppUserModel( AppUserModel appUserModel )
  {
	  List<AppUserModel> list = this.appUserDAO.findByExample(appUserModel, null).getResultsetList() ;
	  
	  if( list != null && list.size() > 0 )
	  {
		  return list.get(0);		  
	  }
	  
	  return null;
  }
  
  
  
  
  
  
  /**
   * This method is used for settlement of the payment with Bank.
   * @param commissionWrapper CommissionWrapper which contains the calculation of commission.
   * @param settlementWrapper SettlementWrapper
   * @return SwitchWrapper
   */
  public SwitchWrapper settleBankPayment(CommissionWrapper commissionWrapper,
                                         WorkFlowWrapper workFlowWrapper) throws Exception
                                        
  {
//	  if(logger.isDebugEnabled())
//	  {
//		  logger.debug("Inside settleBankPayment method of SettlementManagerImpl...");
//	  }
////    BankModel bankModel = workFlowWrapper.getBankModel();
//	VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();
//	AccountInfoModel accountInfoModel = workFlowWrapper.getAccountInfoModel();
//	LogModel logModel = new LogModel();
//	logModel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
//	logModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());
//	
//    VeriflyManager veriflyManager = this.veriflyController.getVeriflyMgrByAccountId(workFlowWrapper
//			.getSmartMoneyAccountModel());
//	accountInfoModel.setCustomerId(workFlowWrapper.getCustomerModel().getCustomerId());
//	accountInfoModel.setAccountNick(workFlowWrapper.getSmartMoneyAccountModel().getName());
//	logModel.setTransactionCodeId(workFlowWrapper.getTransactionModel().getTransactionCodeId());
//	
//	veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
//	veriflyBaseWrapper.setLogModel(logModel);
//	
//	if(logger.isDebugEnabled())
//	{
//		logger.debug("Going to hit Verifly to verify the pin...");
//	}
//	
//	try
//	{
//	
//		veriflyBaseWrapper = veriflyManager.verifyPIN(veriflyBaseWrapper);
//	}
//	catch(Exception ex)
//	{
//		
//		if(ex instanceof RemoteAccessException)
//		{
//			throw new WorkFlowException(WorkFlowErrorCodeConstants.SWITCH_INACTIVE);
//		}
//		else if(ex instanceof FrameworkCheckedException)
//		{
//			throw ex;
//		}
//		
//	}
//	if (null != veriflyBaseWrapper.getErrorMessage()
//			&& !veriflyBaseWrapper.getErrorMessage().equalsIgnoreCase(""))
//	{
//		throw new WorkFlowException(WorkFlowErrorCodeConstants.PIN_NOT_VERIFIED);
//	}
//
//    //=========> Call to Verifly Module
//    // This modlule will return the CreditCrd#,Expiry Date, PayModeId in case of Credit card
//    // and in case of Account# it will return the Account# and Pay Mode Id.
//    //VeriFlyModule.veriFyPin(settlementWrapper.getPin(),
//    //                        settlementWrapper.getaccountNick,bankModel.getVeriflyId)
//    
//	SwitchWrapper switchWrapper = new SwitchWrapperImpl();
//	switchWrapper.setAccountInfoModel(veriflyBaseWrapper.getAccountInfoModel());
//	
//	System.out.println("Primary Account Number : "+veriflyBaseWrapper.getAccountInfoModel().getAccountNo());
//	
//	
//	switchWrapper.setTransactionTransactionModel(workFlowWrapper.getTransactionModel());
//	switchWrapper.setBankId(workFlowWrapper.getSmartMoneyAccountModel().getBankId());
//	switchWrapper.setPaymentModeId(workFlowWrapper.getSmartMoneyAccountModel().getPaymentModeId());
//	switchWrapper.setVeriflyBaseWrapper(veriflyBaseWrapper);
//	switchWrapper.setWorkFlowWrapper(workFlowWrapper);
//
//    //=========> Here the switch module method Transaction will be called.
////    SwitchWrapper switchWrapper = new SwitchWrapperImpl();
////    switchWrapper.setTransactionTransactionModel( workFlowWrapper.getTransactionModel() );
//
////    switchWrapper = this.prepairSwitchWrapper(switchWrapper, workFlowWrapper); //=====> Here we will prepare
//  //  try
//   // {
//	if(logger.isDebugEnabled())
//	{
//		logger.debug("Going to hit Switch Module to make Bank Payment Transaction...");
//	}
//      switchWrapper = switchController.transaction(switchWrapper,
//          commissionWrapper);
//      
//      
//      
//   // }
////    catch (Exception ex)
////    {
////      ex.printStackTrace();
////    }
//
//    //=========> Here u will give the call to the Credit Management Module
//    workFlowWrapper.getTransactionModel().setProcessingSwitchId(
//			switchWrapper.getSwitchSwitchModel().getSwitchId());
//    
//    if(logger.isDebugEnabled())
//	{
//    	logger.debug("Ending settleBankPayment method of SettlementManagerImpl...");
//	}
//    
//
    return null;
  }

  public void setVeriflyDAO(VeriflyDAO veriflyDAO)
  {
    this.veriflyDAO = veriflyDAO;
  }

  public void setRetRetCreditManager(RetRetCreditManager retRetCreditManager)
  {
    this.retRetCreditManager = retRetCreditManager;
  }

  public void setRetHeadRetCreditManager(RetHeadRetCreditManager
                                         retHeadRetCreditManager)
  {
    this.retHeadRetCreditManager = retHeadRetCreditManager;
  }

  public void setInov8DistributorCreditManager(Inov8DistributorCreditManager
                                               inov8DistributorCreditManager)
  {
    this.inov8DistributorCreditManager = inov8DistributorCreditManager;
  }

  public void setDistDistCreditManager(DistDistCreditManager
                                       distDistCreditManager)
  {
    this.distDistCreditManager = distDistCreditManager;
  }

  public void setDistRetCreditManager(DistRetCreditManager distRetCreditManager)
  {
    this.distRetCreditManager = distRetCreditManager;
  }

  public void setInov8CustomerCreditManager(Inov8CustomerCreditManager
                                            inov8CustomerCreditManager)
  {
    this.inov8CustomerCreditManager = inov8CustomerCreditManager;
  }

  public void setRetCustomerCreditManager(RetCustomerCreditManager
                                          retCustomerCreditManager)
  {
    this.retCustomerCreditManager = retCustomerCreditManager;
  }

  public void setStakeholderBankInfoDAO(StakeholderBankInfoDAO
                                        stakeholderBankInfoDAO)
  {
    this.stakeholderBankInfoDAO = stakeholderBankInfoDAO;
  }

  public void setCommissionStakeholderDAO(CommissionStakeholderDAO
                                          commissionStakeholderDAO)
  {
    this.commissionStakeholderDAO = commissionStakeholderDAO;
  }

  public void setRetailerManager(RetailerManager retailerManager)
  {
    this.retailerManager = retailerManager;
  }

  public void setDistributorManager(DistributorManager distributorManager)
  {
    this.distributorManager = distributorManager;
  }

  public void setOperatorManager(OperatorManager operatorManager)
  {
    this.operatorManager = operatorManager;
  }

  public void setRetailerContactManager(RetailerContactManager
                                        retailerContactManager)
  {
    this.retailerContactManager = retailerContactManager;
  }

  public void setDistributorContactManager(DistributorContactManager
                                           distributorContactManager)
  {
    this.distributorContactManager = distributorContactManager;
  }

  public void setCommissionTransactionDAO(CommissionTransactionDAO
                                          commissionTransactionDAO)
  {
    this.commissionTransactionDAO = commissionTransactionDAO;
  }

  public void setSwitchController(SwitchController switchController)
  {
    this.switchController = switchController;
  }

  /**
   * This method is used for the pupose of the Settlement of the Credit Payments.
   * @param commissionWrapper CommissionWrapper
   */
  public void settleCreditPayment(CommissionWrapper commissionWrapper,
                                  WorkFlowWrapper workFlowWrapper)
  {
    //=========> Here u will give the call to the Credit Management Module

    this.updateLocalRegisters(commissionWrapper, workFlowWrapper);
  }

  /**
   * This method is used for setting the various models in SwitchWrapper according to the
   * requirements of the switch module.
   *
   * @param switchWrapper SwitchWrapper
   * @param settlementWrapper SettlementWrapper
   * @return SwitchWrapper
   */
  private SwitchWrapper prepairSwitchWrapper(SwitchWrapper switchWrapper,
                                             WorkFlowWrapper workFlowWrapper, CommissionRateModel commissionRateModel,
                                             StakeholderBankInfoModel stakeholderBankInfoModel)
  {
	  if(logger.isDebugEnabled())
		{
		  logger.debug("Inside prepairSwitchWrapper of SettlementManagerImpl...");
		}
    switchWrapper.setBankBankModel(workFlowWrapper.getBankModel());
    switchWrapper.setPaymentModePaymentModeModel(workFlowWrapper.getTransactionModel().
                                                 getPaymentModeIdPaymentModeModel());
    switchWrapper.setCommissionRateModel( commissionRateModel );
    switchWrapper.setStakeholderBankInfoModel( stakeholderBankInfoModel );
    switchWrapper.setTransactionTransactionModel( workFlowWrapper.getTransactionModel() );
    
    
    switchWrapper.setBankId( stakeholderBankInfoModel.getBankId() ) ;
    switchWrapper.setPaymentModeId( workFlowWrapper.getSmartMoneyAccountModel().getPaymentModeId() ) ;
    if(logger.isDebugEnabled())
	{
    	logger.debug("Ending prepairSwitchWrapper of SettlementManagerImpl...");
	}
    return switchWrapper;
  }

  private void updateLocalRegisters(CommissionWrapper commissionWrapper,
                                    WorkFlowWrapper workFlowWrapper)
  {
	  if(logger.isDebugEnabled())
		{
		  logger.debug("Inside updateLocalRegisters of SettlementManagerImpl...");
		}
    Long transactionTypeId = workFlowWrapper.getTransactionModel().
                                            getRelationTransactionTypeIdTransactionTypeModel().
                                            getTransactionTypeId();
    try
    {
//      if (transactionTypeId == TransactionTypeConstantsInterface.
//                                           OPERATOR_TO_CUSTOMER_TX)
//      {
//
//        this.inov8CustomerCreditManager.updateInov8ToCustomerContactBalance(
//            workFlowWrapper);
//      }
      if (TransactionTypeConstantsInterface.OPERATOR_TO_DISTR_TX.equals(transactionTypeId))
      {
        inov8DistributorCreditManager.updateInov8DistributorContactBalance(workFlowWrapper);
      }
      else if(TransactionTypeConstantsInterface.DIST_TO_DIST_TX.equals(transactionTypeId))
      {
        distDistCreditManager.updateDistToDistContactBalance(workFlowWrapper);
      }
      else if (TransactionTypeConstantsInterface.DIST_TO_RET_TX.equals(transactionTypeId))
      {
        distRetCreditManager.updateDistributorRetailerContactBalance(
            workFlowWrapper);
      }
//      else if (transactionTypeId == TransactionTypeConstantsInterface.
//                                                RET_HEAD_TO_RET_TX)
//      {
//        retHeadRetCreditManager.updateRetailerContactBalanceForRetailer(
//            workFlowWrapper);
//      }
      else if (TransactionTypeConstantsInterface.RET_TO_RET_TX.equals(transactionTypeId))
      {
        retRetCreditManager.updateRetailerContactBalanceforRetailers(
            workFlowWrapper);
      }
      else if (TransactionTypeConstantsInterface.RET_TO_CUSTOMER_TX.equals(transactionTypeId))
      {
        retCustomerCreditManager.updateRetToCustContactBalance(workFlowWrapper);
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    if(logger.isDebugEnabled())
	{
    	logger.debug("Ending updateLocalRegisters of SettlementManagerImpl...");
	}
  }

  public void settleCommission(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Inside settleCommission of SettlementManagerImpl...");
		}

		List<CommissionRateModel> resultSetList = (List) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_RATE_LIST);
		
		if(null != resultSetList && resultSetList.size() > 0 )
		{
			CommissionRateModel commissionRateModel = resultSetList.get(0);
			CommissionAmountsHolder commissionHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
	    	CommissionRateModel commRateModel = new CommissionRateModel();
    		
    		//set inclusive and exclusive charges in TransactionDetailMasterModel
    		workFlowWrapper.getTransactionDetailMasterModel().setInclusiveCharges(commissionHolder.getInclusiveFixAmount() + commissionHolder.getInclusivePercentAmount());
    		workFlowWrapper.getTransactionDetailMasterModel().setExclusiveCharges(commissionHolder.getExclusiveFixAmount() + commissionHolder.getExclusivePercentAmount());
    		
    		List<CommissionTransactionModel> commissionTransactions = new ArrayList<>();
    		
    		if(MapUtils.isNotEmpty(commissionHolder.getStakeholderCommissionsMap())){
    			for (Map.Entry<Long, Double> entry : commissionHolder.getStakeholderCommissionsMap().entrySet()) {
    			    logger.info("Key = " + entry.getKey() + ", Value = " + entry.getValue());
    			   
    			    if (entry.getValue() != null && entry.getValue() <= 0d) {
    			    	logger.info("Skipping Entry for Key = " + entry.getKey() );
    			    	continue;
					}
    			    
    			    commRateModel = new CommissionRateModel();
    			    commRateModel.setRate(entry.getValue());
    			    commRateModel.setCommissionRateId(commissionRateModel.getCommissionRateId());
    			    commRateModel.setProductId(workFlowWrapper.getProductModel().getProductId());
    			    commRateModel.setCommissionTypeId(CommissionConstantsInterface.FIXED_COMMISSION);
    			    commRateModel.setCommissionStakeholderId(entry.getKey());
    			    
    			    commRateModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
    			    commRateModel.setStakeholderBankInfoId(PoolAccountConstantsInterface.HIERARCHY_INCOME_ACCOUNT_ID);
    			    TaxValueBean taxBean = commissionHolder.getStakeholderTaxMap().get(entry.getKey());
    			    
    			    commissionTransactions.add(populateCommissionTransactionModel(commRateModel, workFlowWrapper, taxBean, 0));

    			    updateTransactionDetailMasterForStakeholderCommissions(commRateModel, workFlowWrapper);
    			}
    			
    		}
    		
    		List<CommissionTransactionModel> hierarchyCommissionTransactions = populateHierarchyCommissionTransaction(commissionHolder, workFlowWrapper, commissionRateModel);
    		if(CollectionUtils.isNotEmpty(hierarchyCommissionTransactions))
    			commissionTransactions.addAll(hierarchyCommissionTransactions);
    		
    		if(!commissionTransactions.isEmpty())
    			commissionTransactionDAO.saveOrUpdateCollection(commissionTransactions);

    		/*
    		if(MapUtils.isNotEmpty(commissionHolder.getHierarchyStakeholderCommissionMap())){
    			Double otherCharges = 0d;
    			for (Map.Entry<Long, Double> entry : commissionHolder.getHierarchyStakeholderCommissionMap().entrySet()) {
    				logger.info("\nHierarchy Stakeholders Commission Key = " + entry.getKey() + ", Value = " + entry.getValue());
    				if (entry.getValue() != null && entry.getValue() <= 0d) {
    			    	logger.info("Skipping Entry for Key = " + entry.getKey() );
    			    	continue;
					}
    				
    				otherCharges += entry.getValue();
    				
    			    commRateModel = new CommissionRateModel();
    			    commRateModel.setCreatedBy(entry.getKey());
    			    
    			    commRateModel.setRate(entry.getValue());
    			    commRateModel.setCommissionRateId(commissionRateModel.getCommissionRateId());
    			    commRateModel.setProductId(workFlowWrapper.getProductModel().getProductId());
    			    commRateModel.setCommissionTypeId(CommissionConstantsInterface.FIXED_COMMISSION);
    			    commRateModel.setCommissionStakeholderId(CommissionConstantsInterface.HIERARCHY_STAKE_HOLDER_ID);
    			    
    			    commRateModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
    			    commRateModel.setStakeholderBankInfoId(PoolAccountConstantsInterface.HIERARCHY_INCOME_ACCOUNT_ID);
    			    
    			    TaxValueBean taxBean = commissionHolder.getHierarchyStakeholderTaxMap().get(entry.getKey());
    			    
    			    //save for Hierarchy Stakeholders Commission
    			    saveCommissionTransactionModel(commRateModel, workFlowWrapper, taxBean);
    			}
    			workFlowWrapper.getTransactionDetailMasterModel().setOthersCommission(otherCharges);
    		}*/
    		
	    }    	
  }
  
  	@Override
	public void settleHierarchyCommission(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
  		CommissionAmountsHolder commissionHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
//  		CommissionRateModel commRateModel = new CommissionRateModel();
  		CommissionRateModel commissionRateModel = new CommissionRateModel();
  	  
  		@SuppressWarnings({ "rawtypes", "unchecked" })
  		List<CommissionRateModel> resultSetList = (List) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_RATE_LIST);

  		if(null != resultSetList && resultSetList.size() > 0 ) {
  		  commissionRateModel = resultSetList.get(0);
  		}
		
//  		CommissionRateModel commissionRateModel = resultSetList.get(0);
		List<CommissionTransactionModel> hierarchyCommissionTransactions = populateHierarchyCommissionTransaction(commissionHolder, workFlowWrapper, commissionRateModel);
  		if(CollectionUtils.isNotEmpty(hierarchyCommissionTransactions))
  			commissionTransactionDAO.saveOrUpdateCollection(hierarchyCommissionTransactions);
  	}

  	private List<CommissionTransactionModel> populateHierarchyCommissionTransaction(CommissionAmountsHolder commissionHolder, WorkFlowWrapper workFlowWrapper, 
  			CommissionRateModel commissionRateModel) throws FrameworkCheckedException{
  		int orderNo = 1;
  		List<CommissionTransactionModel> commissionTransactions = new ArrayList<CommissionTransactionModel>();
  		if(MapUtils.isNotEmpty(commissionHolder.getHierarchyStakeholderCommissionMap())) {
  			Double otherCharges = workFlowWrapper.getTransactionDetailMasterModel().getOthersCommission() != null ? workFlowWrapper.getTransactionDetailMasterModel().getOthersCommission() : 0d;
			for (Map.Entry<Long, Double> entry : commissionHolder.getHierarchyStakeholderCommissionMap().entrySet()) {
				logger.info("\nHierarchy Stakeholders Commission Key = " + entry.getKey() + ", Value = " + entry.getValue());
				if (entry.getValue() != null && entry.getValue() <= 0d) {
			    	logger.info("Skipping Entry for Key = " + entry.getKey() );
			    	continue;
				}
				
				otherCharges += entry.getValue();
				
				Long hierarchyStakeholderId = CommissionConstantsInterface.HIERARCHY_STAKE_HOLDER_ID;
				if(workFlowWrapper.isLeg2Transaction()){
					hierarchyStakeholderId = CommissionConstantsInterface.HIERARCHY2_STAKE_HOLDER_ID;
				}
				
			    CommissionRateModel commRateModel = new CommissionRateModel(true, hierarchyStakeholderId);
			    commRateModel.setCreatedBy(entry.getKey());
			    commRateModel.setRate(entry.getValue());
			    commRateModel.setCommissionRateId(commissionRateModel.getCommissionRateId());
			    commRateModel.setProductId(workFlowWrapper.getProductModel().getProductId());
			    
			    TaxValueBean taxBean = commissionHolder.getHierarchyStakeholderTaxMap().get(entry.getKey());

			    commissionTransactions.add(populateCommissionTransactionModel(commRateModel, workFlowWrapper,taxBean, orderNo));
			    orderNo++;
			}
			
			workFlowWrapper.getTransactionDetailMasterModel().setOthersCommission(otherCharges);
		}
  		
		return commissionTransactions;
  	}
    
  
  private void updateTransactionDetailMasterForStakeholderCommissions(CommissionRateModel commissionRateModel, WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
	  
	  if (CommissionConstantsInterface.BANK_STAKE_HOLDER_ID.longValue() == commissionRateModel.getCommissionStakeholderId().longValue()) {
		  workFlowWrapper.getTransactionDetailMasterModel().setAkblCommission(commissionRateModel.getRate());
	  }else if (CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID.longValue() == commissionRateModel.getCommissionStakeholderId().longValue()) {
		  workFlowWrapper.getTransactionDetailMasterModel().setAgentCommission(commissionRateModel.getRate());
	  }else if (CommissionConstantsInterface.AGENT2_STAKE_HOLDER_ID.longValue() == commissionRateModel.getCommissionStakeholderId().longValue()) {
		  workFlowWrapper.getTransactionDetailMasterModel().setAgent2Commission(commissionRateModel.getRate());
	  }else if (CommissionConstantsInterface.FED_STAKE_HOLDER_ID.longValue() == commissionRateModel.getCommissionStakeholderId().longValue()) {
		  workFlowWrapper.getTransactionDetailMasterModel().setFed(commissionRateModel.getRate());
	  }else if (CommissionConstantsInterface.WHT_STAKE_HOLDER_ID.longValue() == commissionRateModel.getCommissionStakeholderId().longValue()) {
		  workFlowWrapper.getTransactionDetailMasterModel().setWht(commissionRateModel.getRate());
	  }else if (CommissionConstantsInterface.SALES_TEAM_STAKE_HOLDER_ID.longValue() == commissionRateModel.getCommissionStakeholderId().longValue()) {
		  workFlowWrapper.getTransactionDetailMasterModel().setSalesTeamCommission(commissionRateModel.getRate());
	  }else if (CommissionConstantsInterface.BLB_SETTLEMENT_STAKE_HOLDER_ID.longValue() == commissionRateModel.getCommissionStakeholderId().longValue()) {
		  workFlowWrapper.getTransactionDetailMasterModel().setBlbSettlementCommission(commissionRateModel.getRate());
	  }else if (CommissionConstantsInterface.SCO_STAKE_HOLDER_ID.longValue() == commissionRateModel.getCommissionStakeholderId().longValue()) {
		  System.out.println("Donnnnnnnnnnnnnnnnnnneeeeeeeeeeeeeeeeeeee!!!!!!!!!!!!!!");
		  workFlowWrapper.getTransactionDetailMasterModel().setScoCommission(commissionRateModel.getRate());
	  }

  }
  
  
  /**
   * saveCommissionTransactionModel
   *
   * @param commissionRateModel CommissionRateModel
   * @param workFlowWrapper WorkFlowWrapper
   */
  	private CommissionTransactionModel populateCommissionTransactionModel(CommissionRateModel commissionRateModel, WorkFlowWrapper workFlowWrapper, TaxValueBean taxBean, int hierarchyOrderNo) throws FrameworkCheckedException {
		try {
			CommissionTransactionModel commissionTransactionModel = new CommissionTransactionModel();
			Boolean isBvsAccountObj =  (Boolean) workFlowWrapper.getObject("isBvsAccount");
			boolean isBvsAccount = (isBvsAccountObj == null) ? false : isBvsAccountObj;

			@SuppressWarnings("rawtypes")
			TransactionDetailModel transactionDetailModel = (TransactionDetailModel) ((List) workFlowWrapper.getTransactionModel()
						.getTransactionIdTransactionDetailModelList()).get(0);

			commissionTransactionModel.setCommissionAmount(commissionRateModel.getRate());

			commissionTransactionModel.setCommissionRateId(commissionRateModel.getCommissionRateId());
			commissionTransactionModel.setProductId(commissionRateModel.getProductId());
			commissionTransactionModel.setCommissionTypeId(commissionRateModel.getCommissionTypeId());
			commissionTransactionModel.setCommissionStakeholderId(commissionRateModel.getCommissionStakeholderId());
			commissionTransactionModel.setPaymentModeId(commissionRateModel.getPaymentModeId());
			commissionTransactionModel.setStakeholderBankId(commissionRateModel.getStakeholderBankInfoId());
			commissionTransactionModel.setPosted(false);
			
			if(CommissionConstantsInterface.AGENT2_STAKE_HOLDER_ID.longValue() == commissionRateModel.getCommissionStakeholderId().longValue()
					|| (!isBvsAccount && ProductConstantsInterface.ACCOUNT_OPENING.longValue() == workFlowWrapper.getProductModel().getProductId())){
				commissionTransactionModel.setSettled(false);
			}else{
				commissionTransactionModel.setSettled(true);
				commissionTransactionModel.setUpdatedOn(new java.util.Date());
			}
			
			if(workFlowWrapper.isCommissionSettledOnLeg2()){
				commissionTransactionModel.setSettled(false);
			
			}			

				commissionTransactionModel.setProductCostPrice(transactionDetailModel.getProductCostPrice());
				commissionTransactionModel.setProductUnitPrice(transactionDetailModel.getProductUnitPrice());
				commissionTransactionModel.setProductUnitId(transactionDetailModel.getProductUnitId());
				commissionTransactionModel.setProductTopupAmount(transactionDetailModel.getProductTopupAmount());
				commissionTransactionModel.setTransactionDetailId(transactionDetailModel.getTransactionDetailId());

			commissionTransactionModel.setCreatedOn(new Date());
			commissionTransactionModel.setUpdatedOn(new Date());
			commissionTransactionModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
			commissionTransactionModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());

			//Hierarchy AppuserID set in RateModel createdBy
			commissionTransactionModel.setHierarchyAppUserId(commissionRateModel.getCreatedBy());
			
			if(commissionRateModel.getCommissionStakeholderId().longValue() == CommissionConstantsInterface.ACC_OPENING_AGENT_STAKE_HOLDER_ID){
				Long accOpeningAgentAppUserId = workFlowWrapper.getAccOpeningAppUserId();
				if(accOpeningAgentAppUserId != null){
					commissionTransactionModel.setRetentionAppUserId(workFlowWrapper.getAccOpeningAppUserId());
				}else{
					logger.error("[SettlementManagerImpl.populateCommissionTransactionModel] accOpeningAgentAppUserId is null... trxID: " + workFlowWrapper.getTransactionCodeModel() == null ? "NULL" : workFlowWrapper.getTransactionCodeModel().getCode() );
				}
			}
			commissionTransactionModel.setWhtRate(taxBean.getWhtRate());
			commissionTransactionModel.setWhtAmount(taxBean.getWhtAmount());
			commissionTransactionModel.setFedRate(taxBean.getFedRate());
			commissionTransactionModel.setFedAmount(taxBean.getFedAmount());
			
			if(CommissionConstantsInterface.AGENT2_STAKE_HOLDER_ID.longValue() == commissionRateModel.getCommissionStakeholderId().longValue()){
				commissionTransactionModel.setWhtApplicable(taxBean.getAgent2WhtApplicable());
			}
			
			if(hierarchyOrderNo > 0){
				commissionTransactionModel.setHierarchyOrderNo(hierarchyOrderNo);
			}
			logger.info("\n*******SAVING COMMISSION TRANSACTION**********\n" + 
					"Commission Rate ID : "+commissionTransactionModel.getCommissionRateId()+"\n" + 
					"Product ID : "+commissionTransactionModel.getProductId()+"\n" + 
					"Commission Type ID : "+commissionRateModel.getCommissionTypeId()+"\n" + 
					"Commission Stakeholder ID : "+commissionRateModel.getCommissionStakeholderId()+"\n" + 
					"Payment Mode ID : "+commissionRateModel.getPaymentModeId()+"\n" + 
					"Transaction ID : "+ (workFlowWrapper.getTransactionCodeModel() == null ? (" NULL") : workFlowWrapper.getTransactionCodeModel().getCode()) + "\n" + 
					"Transaction Detail ID : "+transactionDetailModel.getTransactionDetailId()+"\n******************************************");
			
			return commissionTransactionModel;
		}
		
		catch (Exception ex) {
			logger.error("[SettlementManagerImpl.populateCommissionTransactionModel] Exception occured for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Exception Msg: " + ex.getMessage());
			throw new FrameworkCheckedException(ex.getMessage(), ex);
		}
	}
  
  public CommissionTransactionModel saveCommissionTransactionMoel( CommissionTransactionModel commissionTxModel) throws FrameworkCheckedException, Exception{
	  commissionTxModel = this.commissionTransactionDAO.saveOrUpdate(commissionTxModel);
	  
	  return commissionTxModel;
  }

public void setVeriflyController(VeriflyManagerService veriflyController)
{
	this.veriflyController = veriflyController;
}

public WorkFlowWrapper rollbackBankPayment(WorkFlowWrapper workFlowWrapper) throws Exception
{
	CommissionWrapper commissionWrapper = workFlowWrapper.getCommissionWrapper();
	VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();
	AccountInfoModel accountInfoModel = workFlowWrapper.getAccountInfoModel();
	LogModel logModel = new LogModel();
	logModel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
	logModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());

    
    VeriflyManager veriflyManager = this.veriflyController.getVeriflyMgrByAccountId(workFlowWrapper
			.getSmartMoneyAccountModel());
	accountInfoModel.setCustomerId(workFlowWrapper.getCustomerModel().getCustomerId());
	accountInfoModel.setAccountNick(workFlowWrapper.getSmartMoneyAccountModel().getName());
	logModel.setTransactionCodeId(workFlowWrapper.getTransactionModel().getTransactionCodeId());
	
	veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
	veriflyBaseWrapper.setLogModel(logModel);
	veriflyBaseWrapper = veriflyManager.verifyPIN(veriflyBaseWrapper);
	if (null != veriflyBaseWrapper.getErrorMessage()
			&& !veriflyBaseWrapper.getErrorMessage().equalsIgnoreCase(""))
		throw new WorkFlowException(WorkFlowErrorCodeConstants.PIN_NOT_VERIFIED);

    //=========> Call to Verifly Module
    // This modlule will return the CreditCrd#,Expiry Date, PayModeId in case of Credit card
    // and in case of Account# it will return the Account# and Pay Mode Id.
    //VeriFlyModule.veriFyPin(settlementWrapper.getPin(),
    //                        settlementWrapper.getaccountNick,bankModel.getVeriflyId)
	SwitchWrapper switchWrapper = new SwitchWrapperImpl();
	switchWrapper.setAccountInfoModel(veriflyBaseWrapper.getAccountInfoModel());
	switchWrapper.setTransactionTransactionModel(workFlowWrapper.getTransactionModel());
	switchWrapper.setBankId(workFlowWrapper.getSmartMoneyAccountModel().getBankId());
	switchWrapper.setPaymentModeId(workFlowWrapper.getSmartMoneyAccountModel().getPaymentModeId());
	switchWrapper.setVeriflyBaseWrapper(veriflyBaseWrapper);
	switchWrapper.setWorkFlowWrapper(workFlowWrapper);

    //=========> Here the switch module method Transaction will be called.
//    SwitchWrapper switchWrapper = new SwitchWrapperImpl();
//    switchWrapper.setTransactionTransactionModel( workFlowWrapper.getTransactionModel() );

//    switchWrapper = this.prepairSwitchWrapper(switchWrapper, workFlowWrapper); //=====> Here we will prepare
  //  try
   // {
      switchWrapper = switchController.rollback(switchWrapper);
          
      
      
   // }
//    catch (Exception ex)
//    {
//      ex.printStackTrace();
//    }

    //=========> Here u will give the call to the Credit Management Module
    workFlowWrapper.getTransactionModel().setProcessingSwitchId(
			switchWrapper.getSwitchSwitchModel().getSwitchId());
	
	return workFlowWrapper;
}

public WorkFlowWrapper rollbackCommissionSettlement(WorkFlowWrapper workFlowWrapper) throws Exception
{
	/*
	CommissionWrapper commissionWrapper = workFlowWrapper.getCommissionWrapper();
	 List<CommissionRateModel> resultSetList = (List)commissionWrapper
     .getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_RATE_LIST);
	 HashMap stakeholderBankInfos = workFlowWrapper.getFinancialTransactionsMileStones().getIsCommissionsSettled();
	 
	 for (CommissionRateModel commissionRateModel : resultSetList)
	    {
		 
		
		 
	      if( commissionRateModel.getDispenseTypeId().longValue() == DispenseTypeConstants.BANK_ACCOUNT.longValue() )
	      {
	        StakeholderBankInfoModel stakeholderBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfos.get(commissionRateModel.getPrimaryKey());
	     
	          if(null != stakeholderBankInfoModel)
	          {
	          SwitchWrapper switchWrapper = new SwitchWrapperImpl();
	          switchWrapper = this.prepairSwitchWrapper(switchWrapper, workFlowWrapper,
	                                                    commissionRateModel, stakeholderBankInfoModel);
	          switchWrapper.setWorkFlowWrapper(workFlowWrapper);
	          switchWrapper.setInvoiceType(SwitchConstants.DEBIT_INVOICE);

	         
	            switchWrapper = switchController.rollback(switchWrapper);
	         
	         
	          }
	        }
	    
	    }
	 */
	return workFlowWrapper;
}

private double calculateVariableCommissionAmount(double commissionPercentage,
	      double transactionAmount)
	  {
	    return (transactionAmount * commissionPercentage) / 100;
	  }










public void setAllpayCommissionTransactionDAO(AllPayCommissionTransactionDAO allpayCommissionTransactionDAO)
{
	this.allpayCommissionTransactionDAO = allpayCommissionTransactionDAO;
}


private Long getOLABankId() throws FrameworkCheckedException
{
	BankModel bankModel = new BankModel();
	SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl(); 
	
	bankModel.setFinancialIntegrationId(FinancialInstitutionConstants.OLA_FINANCIAL_INSTITUTION);
	searchBaseWrapper.setBasePersistableModel(bankModel);
	
	searchBaseWrapper = this.bankManager.searchBankByExample(searchBaseWrapper);
	List<BankModel> list = searchBaseWrapper.getCustomList().getResultsetList();
	
	if( list != null && list.size() > 0 )
	{
		bankModel = list.get(0);
		return bankModel.getPrimaryKey() ;
	}
	else
		return null;
	
}







public void setSmartMoneyAccountManager(SmartMoneyAccountManager smartMoneyAccountManager)
{
	this.smartMoneyAccountManager = smartMoneyAccountManager;
}



public void setAppUserDAO(AppUserDAO appUserDAO)
{
	this.appUserDAO = appUserDAO;
}



public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager)
{
	this.financialIntegrationManager = financialIntegrationManager;
}

public static void main (String [] a)
{
	System.out.println(new Date());
}



public void setBankManager(BankManager bankManager)
{
	this.bankManager = bankManager;
}



@Override
public SettlementTransactionModel saveSettlementTransactionModel(
		SettlementTransactionModel settlementTransactionModel) throws Exception {
	
	return (SettlementTransactionModel)settlementTransactionDAO.saveOrUpdate(settlementTransactionModel);
}

	@SuppressWarnings("unchecked")
	public List<SettlementTransactionModel> getSettlementTransactionModelList(Long stakeholderBankInfoId, Boolean isCreditEntry, Date createdOn) { 
	
		List<SettlementTransactionModel> list = new ArrayList<SettlementTransactionModel>(0);
		
		try {
			
			SettlementTransactionModel settlementTransactionModel = new SettlementTransactionModel();
			settlementTransactionModel.setCreatedOn(createdOn);
			
			if(isCreditEntry) {
				
				settlementTransactionModel.setToBankInfoID(stakeholderBankInfoId);
			
			} else {
				
				settlementTransactionModel.setFromBankInfoID(stakeholderBankInfoId);
			}
			
			list  = settlementTransactionDAO.getSettlementTransactionModelList(settlementTransactionModel);
	
		} catch (Exception e) {
			logger.error(e);
		}
		
		return list;
	}
	
	
	public void prepareDataForDayEndSettlement(WorkFlowWrapper wrapper) throws Exception{
		  
		List<OLAInfo> debitList = wrapper.getOLASwitchWrapper().getOlavo().getDebitAccountList();
		List<OLAInfo> creditList = wrapper.getOLASwitchWrapper().getOlavo().getCreditAccountList();
		Boolean isAgent = null;
		
		for(OLAInfo debitOLAInfo: debitList){
			Long bankInfoId = null;
			isAgent = null;
			
			if(debitOLAInfo.getCustomerAccountTypeId().longValue() == CustomerAccountTypeConstants.SETTLEMENT){
				if(debitOLAInfo.getCoreStakeholderBankInfoId() != null){
					bankInfoId = debitOLAInfo.getCoreStakeholderBankInfoId();
				}
			}else{
				// Customer or Agent specific (AccountType based) Pool A/C
				bankInfoId = this.getStakeholderBankInfoId(debitOLAInfo.getCustomerAccountTypeId());
				if(bankInfoId == null){
					throw new WorkFlowException("Core Banking Pool account not defined for account type:"+debitOLAInfo.getCustomerAccountTypeId());
				}
				
				if(debitOLAInfo.getIsAgent() == null){
					throw new WorkFlowException("Unable to load Pool Account Info for debit.");
				}else{
					isAgent = debitOLAInfo.getIsAgent();
				}
			}
			
			if(bankInfoId != null){
				prepareAndSaveSettlementTransaction(wrapper.getTransactionModel().getTransactionId(),
						wrapper.getProductModel().getProductId(),
						debitOLAInfo.getBalance(),
						bankInfoId,
						PoolAccountConstantsInterface.JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID,
						isAgent,
						true);
			}
		}
		
		for(OLAInfo creditOLAInfo: creditList){
			Long bankInfoId = null;
			isAgent = null;

			if(creditOLAInfo.getCustomerAccountTypeId() != null) {
				if (creditOLAInfo.getCustomerAccountTypeId().longValue() == CustomerAccountTypeConstants.SETTLEMENT) {
					if (creditOLAInfo.getCoreStakeholderBankInfoId() != null) {
						bankInfoId = creditOLAInfo.getCoreStakeholderBankInfoId();
					}
				} else {
					// Customer or Agent specific (AccountType based) Pool A/C
					bankInfoId = this.getStakeholderBankInfoId(creditOLAInfo.getCustomerAccountTypeId());
					if (bankInfoId == null) {
						throw new WorkFlowException("Core Banking Pool account not defined for account type:" + creditOLAInfo.getCustomerAccountTypeId());
					}

					if (creditOLAInfo.getIsAgent() == null) {
						throw new WorkFlowException("Unable to load Pool Account Info for credit.");
					} else {
						isAgent = creditOLAInfo.getIsAgent();
					}

				}
			}
			if(bankInfoId != null){
				prepareAndSaveSettlementTransaction(wrapper.getTransactionModel().getTransactionId(),
						wrapper.getProductModel().getProductId(),
						creditOLAInfo.getBalance(),
						PoolAccountConstantsInterface.JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID,
						bankInfoId,
						isAgent,
						false);
			}
		}
	}

	private void prepareAndSaveSettlementTransaction(Long transactionId,Long productId,Double amount,Long fromAccountInfoId, Long toAccountInfoId, Boolean isAgent, boolean debitPoolAcc) throws Exception{
		
		if(amount == 0 ) {	//	to avoid 0 agent commission case.
			
			return;
		}
		
		AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();
		SettlementTransactionModel settlementModel = new SettlementTransactionModel();
		settlementModel.setTransactionID(transactionId);
		settlementModel.setProductID(productId);
		settlementModel.setCreatedBy(appUserModel.getAppUserId());
		settlementModel.setUpdatedBy(appUserModel.getAppUserId());
		settlementModel.setCreatedOn(new Date());
		settlementModel.setUpdatedOn(new Date());
		settlementModel.setStatus(0L);	  
		settlementModel.setFromBankInfoID(fromAccountInfoId);
		settlementModel.setToBankInfoID(toAccountInfoId);
		settlementModel.setAmount(amount);
		
		this.saveSettlementTransactionModel(settlementModel);
		
		if(isAgent != null){
			Long poolBankInfoId = null;
			
			settlementModel = new SettlementTransactionModel();
			if(isAgent){
				poolBankInfoId = PoolAccountConstantsInterface.AGENT_POOL_ACCOUNT_ID;
			}else{
				poolBankInfoId = PoolAccountConstantsInterface.CUSTOMER_POOL_ACCOUNT_ID;
			}
			
			settlementModel.setTransactionID(transactionId);
			settlementModel.setProductID(productId);
			settlementModel.setCreatedBy(appUserModel.getAppUserId());
			settlementModel.setUpdatedBy(appUserModel.getAppUserId());
			settlementModel.setCreatedOn(new Date());
			settlementModel.setUpdatedOn(new Date());
			settlementModel.setStatus(0L);	  
			settlementModel.setFromBankInfoID((debitPoolAcc)?poolBankInfoId:fromAccountInfoId);
			settlementModel.setToBankInfoID((!debitPoolAcc)?poolBankInfoId:toAccountInfoId);
			settlementModel.setAmount(amount);
			this.saveSettlementTransactionModel(settlementModel);
		}
	}

	

@Override
public Long getDistributorBankInfoId(Long distributerId) throws FrameworkCheckedException {
	if(distributerId == null){
		throw new WorkFlowException(WorkFlowErrorCodeConstants.DISTRIBUTOR_INFO_MISSING_TRX);	
	}
	DistributorModel distributorModel = new DistributorModel();
	distributorModel.setDistributorId(distributerId);
	StakeholderBankInfoModel bankInfoModel = stakeholderBankInfoManager.loadDistributorStakeholderBankInfoModel(distributorModel);
	if(bankInfoModel == null){
		throw new WorkFlowException(WorkFlowErrorCodeConstants.POOL_ACCOUNT_MISSING);	
	}
	return bankInfoModel.getStakeholderBankInfoId();
}

@Override
public Long getStakeholderBankInfoId(Long accountTypeId) throws FrameworkCheckedException {
	
	if(accountTypeId == null){
		logger.error("########################## accountTypeId is null in [SettlementManagerImpl.getStakeholderBankInfoId]");
		throw new WorkFlowException(WorkFlowErrorCodeConstants.ACC_TYPE_INFO_MISSING);	
	}
	
	StakeholderBankInfoModel bankInfoModel = stakeholderBankInfoManager.getStakeholderAccountBankInfoModel(accountTypeId);
	
	if(bankInfoModel == null){
		logger.error("########################## Unable to load StakeholderBankInfoModel by accountTypeId:" +accountTypeId);
		throw new WorkFlowException(WorkFlowErrorCodeConstants.POOL_ACCOUNT_MISSING);	
	}
	
	return bankInfoModel.getStakeholderBankInfoId();
}

public void updateCommissionTransactionSettled(Long transactionDetailId) throws FrameworkCheckedException {
	try{
		commissionTransactionDAO.updateCommissionTransactionSettled(transactionDetailId);
	}catch (Exception ex) {
		logger.error("[SettlementManagerImpl.updateCommissionTransactionSettled] Exception occured. Exception Msg: " + ex.getMessage());
		throw new FrameworkCheckedException(ex.getMessage(), ex);
	}

}


public void setSettlementTransactionDAO(
		SettlementTransactionDAO settlementTransactionDAO) {
	this.settlementTransactionDAO = settlementTransactionDAO;
}
public void setStakeholderBankInfoManager(StakeholderBankInfoManager stakeholderBankInfoManager) {
	this.stakeholderBankInfoManager = stakeholderBankInfoManager;
}

}
