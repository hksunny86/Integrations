package com.inov8.microbank.common.wrapper.workflow;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.cardconfiguration.model.CardFeeRuleModel;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.bankmodule.MemberBankModel;
import com.inov8.microbank.common.model.productdeviceflowmodule.ProductDeviceFlowListViewModel;
import com.inov8.microbank.common.model.schedulemodule.ScheduleLoanPaymentModel;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.integration.dispenser.ProductDispenser;
import com.inov8.microbank.server.service.integration.vo.MiddlewareAdviceVO;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.switchmodule.IntegrationMessageVO;
import com.inov8.microbank.server.service.switchmodule.iris.model.CustomerAccount;
import com.inov8.verifly.common.model.AccountInfoModel;

import java.util.Date;

public interface WorkFlowWrapper extends BaseWrapper {
	public ServiceTypeModel getServiceTypeModel();

	public void setServiceTypeModel(ServiceTypeModel serviceTypeModel);

	public void setDistributorContactModel(
			DistributorContactModel distributorContactModel);

	public DistributorContactModel getDistributorContactModel();

	Boolean getInclChargesCheck();

	void setInclChargesCheck(Boolean inclChargesCheck);

	public void setTransactionModel(TransactionModel transactionModel);

	public TransactionModel getTransactionModel();

	public void setOperatorModel(OperatorModel operatorModel);

	public OperatorModel getOperatorModel();

	public void setRetailerContactModel(
			RetailerContactModel retailerContactModel);

	public void setFromRetailerContactModel(
			RetailerContactModel fromRetailerContactModel);

	public void setToRetailerContactModel(
			RetailerContactModel toRetailerContactModel);

	public RetailerContactModel getToRetailerContactModel();

	public RetailerContactModel getFromRetailerContactModel();

	public RetailerContactModel getRetailerContactModel();

	public void setSupplierModel(SupplierModel supplierModel);

	public SupplierModel getSupplierModel();

	public void setProductModel(ProductModel productModel);

	public ProductModel getProductModel();

	public void setMemberBankModel(MemberBankModel memberBankModel);

	public MemberBankModel getMemberBankModel();

	public void setCommissionAmountsHolder(
			CommissionAmountsHolder commissionAmountsHolder);

	public CommissionAmountsHolder getCommissionAmountsHolder();

	public void setBankModel(BankModel bankModel);

	public BankModel getBankModel();

	public void setFailureLogModel(AuditLogModel failureReasonModel);

	public AuditLogModel getFailureLogModel();

	public void setTransactionTypeModel(
			TransactionTypeModel transactionTypeModel);

	public TransactionTypeModel getTransactionTypeModel();

	public void setToDistributorContactModel(
			DistributorContactModel toDistributorContactModel);

	public DistributorContactModel getToDistributorContactModel();

	public void setFromDistributorContactModel(
			DistributorContactModel fromDistributorContactModel);

	public DistributorContactModel getFromDistributorContactModel();

	public void setTransactionAmount(Double transactionAmount);

	public void setCardAcceptorDetails(String cardAcceptorDetails);

	public Double getTransactionAmount();
	public String getCardAcceptorDetails();
	public void setTotalAmount(Double totalAmount);
	public Double getTotalAmount();

	public TransactionCodeModel getTransactionCodeModel();

	public void setTransactionCodeModel(
			TransactionCodeModel transactionCodeModel);

	public void setPaymentModeModel(PaymentModeModel paymentModeModel);

	public PaymentModeModel getPaymentModeModel();
	public void setCustomerModel(CustomerModel customerModel);

	public CustomerModel getCustomerModel();
	public DistributorModel getDistributorModel();

	public void setDistributorModel(DistributorModel distributorModel);
	public void setDistributorNmContactModel(DistributorContactModel distributorContactModel);
	public DistributorContactModel getDistributorNmContactModel();

	public DeviceTypeModel getDeviceTypeModel() ;
	public void setDeviceTypeModel(DeviceTypeModel deviceTypeModel) ;

	public SmartMoneyAccountModel getSmartMoneyAccountModel() ;
	public void setSmartMoneyAccountModel(SmartMoneyAccountModel smartMoneyAccountModel) ;

	public void setAppUserModel(AppUserModel appUserModel) ;
	public AppUserModel getAppUserModel() ;

	public void setCommissionThresholdRate(CommissionThresholdRateModel commissionThresholdRateModel) ;
	public CommissionThresholdRateModel getCommissionThresholdRate() ;

	public void setSupplierBankInfoModel(SupplierBankInfoModel supplierBankInfoModel);
	public SupplierBankInfoModel getSupplierBankInfoModel();

	public ProductVO getProductVO() ;
	public void setProductVO(ProductVO productVO) ;

	public void setCustomerAppUserModel(AppUserModel customerAppUserModel) ;
	public void setRetailerAppUserModel(AppUserModel retailerAppUserModel);
	public AppUserModel getRetailerAppUserModel();
//        public void setDistributorAppUserModel(AppUserModel distributorAppUserModel);

	public AppUserModel getCustomerAppUserModel();
	//        public AppUserModel getDistributorAppUserModel();
	public ProductUnitModel getProductUnitModel();
	public void setProductUnitModel(ProductUnitModel productUnitModel);
	public void setToDistributorContactAppUserModel(AppUserModel toDistributorContactAppUserModel) ;
	public AppUserModel getToDistributorContactAppUserModel() ;
	public void setFromDistributorContactAppUserModel(AppUserModel fromDistributorContactAppUserModel) ;
	public AppUserModel getFromDistributorContactAppUserModel() ;

	public AppUserModel getFromRetailerContactAppUserModel();
	public AppUserModel getToRetailerContactAppUserModel();
	public AppUserModel getToDistributorContactAppUserModel(AppUserModel toDistributorContactAppUserModel);
	public AppUserModel getFromDistributorContactAppUserModel(AppUserModel fromDistributorContactAppUserModel);
	public void setFromRetailerContactAppUserModel(AppUserModel fromRetailerContactAppUserModel) ;
	public void setToRetailerContactAppUserModel(AppUserModel toRetailerContactAppUserModel);

	public AccountInfoModel getAccountInfoModel();
	public void setAccountInfoModel(AccountInfoModel accountInfoModel);
	public UserDeviceAccountsModel getUserDeviceAccountModel();
	public void setUserDeviceAccountModel(UserDeviceAccountsModel userDeviceAccountsModel);
	public void setSuccessMessage(NotificationMessageModel successMessage);
	public NotificationMessageModel getSuccessMessage();
	public void setInstruction(NotificationMessageModel instruction);
	public NotificationMessageModel getInstruction();

	public void setTotalCommissionAmount(Double totalCommissionAmount) ;
	public Double getTotalCommissionAmount();
	public void setTransactionDetailModel(TransactionDetailModel transactionDetailModel);
	public TransactionDetailModel getTransactionDetailModel();
	public void setBillAmount(Double billAmount);
	public Double getBillAmount();
	public void setTxProcessingAmount(Double txProcessingAmount);
	public Double getTxProcessingAmount();

	public OperatorBankInfoModel getOperatorBankInfoModel();
	public void setOperatorBankInfoModel(OperatorBankInfoModel operatorBankInfoModel);

	public OperatorBankInfoModel getOperatorPayingBankInfoModel();
	public void setOperatorPayingBankInfoModel(OperatorBankInfoModel operatorPayingBankInfoModel);

	public String getCcCVV();
	public void setCcCVV(String ccCVV);

	public void setFinancialTransactionsMileStones(FinancialTransactionsMileStones financialTransactionsMileStones);
	public FinancialTransactionsMileStones getFinancialTransactionsMileStones();
	public void setCommissionWrapper(CommissionWrapper commissionWrapper);
	public CommissionWrapper getCommissionWrapper();
	public void setProductDispenser(ProductDispenser productDispenser);
	public ProductDispenser getProductDispenser();

	public void setMPin(String mPin);
	public String getMPin();

	public void setCustomerAccount(CustomerAccount customerAccount);
	public CustomerAccount getCustomerAccount();

	public SwitchWrapper getSwitchWrapper();
	public void setSwitchWrapper(SwitchWrapper switchWrapper);
	public void setDiscountAmount(Double discountAmount);
	public Double getDiscountAmount();

	public SmartMoneyAccountModel getReceivingSmartMoneyAccountModel() ;
	public void setReceivingSmartMoneyAccountModel(SmartMoneyAccountModel smartMoneyAccountModel) ;

	public SmartMoneyAccountModel getOlaSmartMoneyAccountModel();
	public void setOlaSmartMoneyAccountModel(SmartMoneyAccountModel olaSmartMoneyAccountModel);
	public void setDistOrRetAppUserModel(AppUserModel distOrRetAppUserModel);
	public AppUserModel getDistOrRetAppUserModel();

	public ProductDeviceFlowListViewModel getProductDeviceFlowModel();
	public void setProductDeviceFlowModel(ProductDeviceFlowListViewModel productDeviceFlowModel);

	public boolean isCallNextProductDispenser();
	public void setCallNextProductDispenser(boolean callNextProductDispenser);

	public SegmentModel getSegmentModel();
	public void setSegmentModel(SegmentModel segmentModel);

	public void setWalkInCustomerMob(String walkInCustomerMob);
	public String getWalkInCustomerMob();
	public void setWalkInCustomerCNIC(String walkInCustomerCNIC);
	public String getWalkInCustomerCNIC();
	public void setOneTimePin(String oneTimePin);
	public String getOneTimePin();
	public void setOLASwitchWrapper(SwitchWrapper switchWrapper);
	public SwitchWrapper getOLASwitchWrapper();
	public void setRecipientSwitchWrapper(SwitchWrapper recipientSwitchWrapper);
	public SwitchWrapper getRecipientSwitchWrapper();
	public void setRecipientSmartMoneyAccountModel(SmartMoneyAccountModel recipientSmartMoneyAccountModel);
	public SmartMoneyAccountModel getRecipientSmartMoneyAccountModel();

	//added by mudassar for a/c to cash
	public WalkinCustomerModel getRecipientWalkinCustomerModel();
	public void setRecipientWalkinCustomerModel(WalkinCustomerModel recepientWalkinCustomerModel);
	public SmartMoneyAccountModel getRecipientWalkinSmartMoneyAccountModel();
	public void setRecipientWalkinSmartMoneyAccountModel(SmartMoneyAccountModel recepientWalkinSmartMoneyAccountModel);

	public SmartMoneyAccountModel getSenderSmartMoneyAccountModel();
	public void setSenderSmartMoneyAccountModel(SmartMoneyAccountModel senderSmartMoneyAccountModel);

	public void setSenderAppUserModel(AppUserModel senderAppUserModel) ;
	public AppUserModel getSenderAppUserModel() ;

	public WalkinCustomerModel getSenderWalkinCustomerModel();
	public void setSenderWalkinCustomerModel(WalkinCustomerModel senderWalkinCustomerModel);

	/*********Defining Methods for implementing reverse FT in case a transaction fails after FT**********/

	public void setFirstFTIntegrationVO(IntegrationMessageVO integrationMessageVO);
	public IntegrationMessageVO getFirstFTIntegrationVO();

	public void setFirstFTSwitchWrapper(SwitchWrapper switchWrapper);
	public SwitchWrapper getFirstFTSwitchWrapper();
	public void setBillPaymentIntegrationVO(IntegrationMessageVO integrationMessageVO);
	public IntegrationMessageVO getBillPaymentIntegrationVO();
	public void setBillPaymentSwitchWrapper(SwitchWrapper switchWrapper);
	public SwitchWrapper getBillPaymentSwitchWrapper();

	/****************************************************************************************************/

	public void setOlaSwitchWrapper_2(SwitchWrapper switchWrapper_2);
	public SwitchWrapper getOlaSwitchWrapper_2();
	public SwitchWrapper getOlaSwitchWrapper_3();
	public void setOlaSwitchWrapper_3(SwitchWrapper olaSwitchWrapper_3);
	public SwitchWrapper getOlaSwitchWrapper_4();
	public void setOlaSwitchWrapper_4(SwitchWrapper olaSwitchWrapper_4);
	public RetailerModel getRetailerModel();
	public void setRetailerModel(RetailerModel retailerModel);

	public boolean isAccountToCashLeg1();
	public void setAccountToCashLeg1(boolean isAccountToCashLeg1);
	public boolean isCashToCashLeg1();
	public void setCashToCashLeg1(boolean isCashToCashLeg1);
	public boolean isCashDeposit();
	public void setCashDeposit(boolean isCashDeposit);
	public boolean isCashWithdrawl();
	public void setCashWithdrawl(boolean isCashWithdrawl);
	public void setP2PRecepient(boolean isP2PRecepient);
	public boolean isP2PRecepient();
	public boolean isCheckBalance();
	public void setCheckBalance(boolean isCheckBalance);

	public AppUserModel getReceiverAppUserModel();
	public void setReceiverAppUserModel(AppUserModel receiverAppUserModel);
	public boolean isCRetailPayment();
	public void setIsCRetailPayment(boolean iscrp);
	public RetailerContactModel getHeadRetailerContactModel();
	public void setHeadRetailerContactModel(RetailerContactModel headRetailerAppUserModel);
	public SmartMoneyAccountModel getHeadRetailerSmaModel();
	public void setHeadRetailerSmaModel(SmartMoneyAccountModel headRetailerSmaModel) ;
	public AppUserModel getHeadRetailerAppuserModel();
	public void setHeadRetailerAppuserModel(AppUserModel headRetailerAppuserModel);
	public TransactionDetailMasterModel getTransactionDetailMasterModel();
	public void setTransactionDetailMasterModel(TransactionDetailMasterModel transactionDetailMasterModel);
	public MiniTransactionModel getMiniTransactionModel();
	public void setMiniTransactionModel(MiniTransactionModel miniTransactionModel);
	public boolean isWalkinLimitApplicable();
	public void setWalkinLimitApplicable(boolean isWalkinLimitApplicable);
	public Long getBulkDisbursmentsId();
	public void setBulkDisbursmentsId(Long bulkDisbursmentsId);
	public void setFromSegmentId(Long segmentId);
	public Long getFromSegmentId();
	public void setToSegmentId(Long segmentId);
	public Long getToSegmentId();
	public Object getCustomField();
	public void setCustomField(Object customField);
	Boolean getIsIvrResponse() ;
	void setIsIvrResponse(Boolean isIvrResponse);
	String getErrorCode();
	void setErrorCode(String errorCode);
	public CustomerModel getRecipientCustomerModel();
	public void setRecipientCustomerModel(CustomerModel customerModel);
	public SwitchWrapper getMiddlewareSwitchWrapper();
	public void setMiddlewareSwitchWrapper(SwitchWrapper switchWrapper);
	HandlerModel getHandlerModel();
	void setHandlerModel(HandlerModel handlerModel);
	public void setHandlerAppUserModel(AppUserModel handlerAppUserModel) ;
	public AppUserModel getHandlerAppUserModel();
	public RetailerContactModel getHandlerRetContactModel();
	public void setHandlerRetContactModel(RetailerContactModel handlerRetContactModel);
	public SmartMoneyAccountModel getHandlerSMAModel() ;
	public void setHandlerSMAModel(SmartMoneyAccountModel handlerSMAModel);
	public void setHandlerUserDeviceAccountModel(UserDeviceAccountsModel handlerUserDeviceAccountsModel) ;
	public UserDeviceAccountsModel getHandlerUserDeviceAccountModel();
	public boolean isLeg2Transaction();
	public void setLeg2Transaction(boolean isLeg2Transaction);
	public Long getCurrentSupProcessingStatusId();
	public void setCurrentSupProcessingStatusId(Long currentSupProcessingStatusId);
	public Long getAccOpeningAppUserId();
	public void setAccOpeningAppUserId(Long accOpeningAppUserId);
	public AgentTransferRuleModel getAgentTransferRuleModel();
	public void setAgentTransferRuleModel(AgentTransferRuleModel agentTransferRuleModel);
	public void setCommissionSettledOnLeg2(boolean value);
	public boolean isCommissionSettledOnLeg2();


	public WorkFlowWrapper cloneForDisbursement();

	Date getBusinessDate();

	void setBusinessDate(Date businessDate);

	public abstract void setTaxRegimeModel(TaxRegimeModel taxRegimeModel);

	public abstract TaxRegimeModel getTaxRegimeModel();
	public boolean isSenderBvs();
	public void setSenderBvs(boolean senderBvs);
	public boolean isReceiverBvs();
	public void setReceiverBvs(boolean receiverBvs);

	public void setStartDate(Date start_date);
	Date getStartDate();

	public void setTransactionDate(Date transaction_date);
	Date getTransactionDate();

	public void setWHTDedTransactionDate(Date transaction_date);
	Date getWHTDedTransactionDate();
	public void setFilerRate(Double filerRate);
	public Double getFilerRate();
	public void setNonFilerRate(Double nonFilerRate);
	public Double getNonFilerRate();
	public void setIsFiler(Boolean isFiler);
	public Boolean getIsFiler();
	public void setSumAmount(Double sumAmount);
	public Double getSumAmount();
	public void setIsCustomerInitiatedTransaction(Boolean isCustomerInitiatedTransaction);
	public Boolean getIsCustomerInitiatedTransaction();
	public SafRepoCashOutModel getSafRepoCashOutModel();
	public void setSafRepoCashOutModel(SafRepoCashOutModel safRepoCashOutModel);
	public void setUSSDCashWithdrawal(Boolean USSDCashWithdrawal);
	public Boolean getUSSDCashWithdrawal();

	public void setThirdPartyAccountOpeningModel(ThirdPartyAccountOpeningModel thirdPartyAccountOpeningModel);
	public void setBOPCardIssuanceModel(BOPCardIssuanceModel bopCardIssuanceModel);
	public void setScheduleLoanPaymentModel(ScheduleLoanPaymentModel scheduleLoanPaymentModel);

	public ThirdPartyAccountOpeningModel getThirdPartyAccountOpeningModel();
	public BOPCardIssuanceModel getBOPCardIssuanceModel();
	public ScheduleLoanPaymentModel getScheduleLoanPaymentModel();

	public void setLoanAmount(Double loanAmount);
	public Double getLoanAmount();

	public void setChargedAmount(Double chargedAmount);
	public Double getChargedAmount();

	public void setProductId(Long productId);
	public Long getProductId();

	public void setMiddlewareAdviceVO(MiddlewareAdviceVO middlewareAdviceVO);
	public MiddlewareAdviceVO getMiddlewareAdviceVO();

	public void setRecipientAccountNo(String recipientAccountNo);
	public String getRecipientAccountNo();

	public void setConsumerNumber(String consumerNumber);
	public String getConsumerNumber();

	public void setCardFeeRuleModel(CardFeeRuleModel cardFeeRuleModel);
	public CardFeeRuleModel getCardFeeRuleModel();
}
