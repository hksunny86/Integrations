package com.inov8.microbank.common.wrapper.workflow;

import com.inov8.framework.common.wrapper.BaseWrapperImpl;
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
import java.util.HashMap;

/**
 *
 * <p>
 * Title:
 * </p>
 *
 * <p>
 * Description:
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 *
 * <p>
 * Company:
 * </p>
 *
 * @author Maqsood Shahzad
 * @version 1.0
 */

public class WorkFlowWrapperImpl extends BaseWrapperImpl implements WorkFlowWrapper
{

	private ThirdPartyAccountOpeningModel thirdPartyAccountOpeningModel;
	private ScheduleLoanPaymentModel scheduleLoanPaymentModel;
	private BOPCardIssuanceModel bopCardIssuanceModel;
	private TransactionTypeModel transactionTypeModel;
	private ServiceTypeModel serviceTypeModel;
	private DistributorContactModel distributorContactModel;
	private DeviceTypeModel deviceTypeModel;
	private TransactionModel transactionModel;
	private TransactionCodeModel transactionCodeModel;
	private HashMap hashMap = new HashMap();
	private OperatorModel operatorModel;
	private TransactionDetailModel transactionDetailModel;
	private DistributorContactModel fromDistributorContactModel;
	private DistributorContactModel toDistributorContactModel;
	private RetailerContactModel retailerContactModel;
	private RetailerContactModel fromRetailerContactModel;
	private RetailerContactModel toRetailerContactModel;
	private SupplierModel supplierModel;
	private ProductModel productModel;
	private PaymentModeModel paymentModeModel;
	private CommissionAmountsHolder commissionAmountsHolder;
	private CustomerModel customerModel;
	private BankModel bankModel;
	private MemberBankModel memberBankModel;
	private AuditLogModel failureLogModel;
	private Double transactionAmount;
	private String cardAcceptorDetails;
	private Double totalAmount;
	private Double totalCommissionAmount;
	private Double billAmount;
	private Double txProcessingAmount;
	private Double discountAmount;
	private DistributorModel distributorModel;
	private DistributorContactModel distributorNmContactModel;
	private SmartMoneyAccountModel smartMoneyAccountModel;
	private SmartMoneyAccountModel receivingSmartMoneyAccountModel;
	private SmartMoneyAccountModel olaSmartMoneyAccountModel;
	private AppUserModel appUserModel;
	private CommissionThresholdRateModel commissionThresholdRateModel;
	private ProductVO productVO;
	private AppUserModel customerAppUserModel;
	private AppUserModel retailerAppUserModel;
	private AppUserModel distributorAppUserModel;
	private ProductUnitModel productUnitModel;
	private AppUserModel toDistributorContactAppUserModel;
	private AppUserModel fromDistributorContactAppUserModel;
	private AppUserModel toRetailerContactAppUserModel;
	private AppUserModel fromRetailerContactAppUserModel;
	private AccountInfoModel accountInfoModel;
	private UserDeviceAccountsModel userDeviceAccountsModel;
	private NotificationMessageModel successMessage;
	private NotificationMessageModel instruction;
	private SupplierBankInfoModel supplierBankInfoModel;
	private OperatorBankInfoModel operatorBankInfoModel;
	private OperatorBankInfoModel operatorPayingBankInfoModel;
	private FinancialTransactionsMileStones financialTransactionsMileStones;
	private CommissionWrapper commissionWrapper;
	private ProductDispenser productDispenser;
	private SwitchWrapper switchWrapper;
	private AppUserModel distOrRetAppUserModel;
	private ProductDeviceFlowListViewModel productDeviceFlowModel;
	private SwitchWrapper olaSwitchWrapper;
	private SwitchWrapper recipientSwitchWrapper;
	private SmartMoneyAccountModel recipientSmartMoneyAccountModel;
	private SmartMoneyAccountModel senderSmartMoneyAccountModel;
	private AppUserModel senderAppUserModel;
	private SwitchWrapper olaSwitchWrapper_2;
	private SwitchWrapper olaSwitchWrapper_3;
	private SwitchWrapper olaSwitchWrapper_4;
	private SwitchWrapper middlewareSwitchWrapper;
	private RetailerModel retailerModel;
	private MiniTransactionModel miniTransactionModel;
	private AgentTransferRuleModel agentTransferRuleModel;
	private SafRepoCashOutModel safRepoCashOutModel;

	private String ccCVV;
	private String mPin;

	private String walkInCustomerCNIC;
	private String walkInCustomerMob;

	private String oneTimePin;

	private WalkinCustomerModel recipientWalkinCustomerModel;
	private WalkinCustomerModel senderWalkinCustomerModel;
	private SmartMoneyAccountModel recipientWalkinSmartMoneyAccountModel;
	private boolean isAccountToCashLeg1;
	private boolean isCashToCashLeg1;
	private boolean isCashDeposit;
	private boolean isCashWithdrawl;
	private boolean isP2PRecepient;
	private boolean isCheckBalance;
	private IntegrationMessageVO firstFTIntegrationVO;
	private SwitchWrapper firstFTSwitchWrapper;
	private boolean isCRetailPayment;
	private boolean isWalkinLimitApplicable;
	private Long bulkDisbursmentsId;
	private Long fromSegmentId;
	private Long toSegmentId;
	private Object customField;
	private HandlerModel handlerModel;
	private AppUserModel handlerAppUserModel;
	private RetailerContactModel handlerRetContactModel;
	private SmartMoneyAccountModel handlerSMAModel;
	private AppUserModel receiverAppUserModel;
	private Boolean inclChargesCheck = false;

	private boolean callNextProductDispenser;


	private CustomerAccount customerAccount;

	private SegmentModel segmentModel;

	private RetailerContactModel headRetailerContactModel;
	private SmartMoneyAccountModel headRetailerSmaModel;
	private AppUserModel headRetailerAppuserModel;


	private IntegrationMessageVO billPaymentIntegrationVO;
	private SwitchWrapper billPaymentSwitchWrapper;
	private TransactionDetailMasterModel transactionDetailMasterModel;
	private Boolean isIvrResponse;
	private String errorCode;

	private CustomerModel recipientCustomerModel;
	private UserDeviceAccountsModel handlerUserDeviceAccountModel;
	private boolean isLeg2Transaction;
	private Long currentSupProcessingStatusId;
	private Long accOpeningAppUserId;
	private boolean commissionSettledOnLeg2;
	private boolean isSenderBvs;
	private boolean isReceiverBvs;

	private Date start_date;
	public Date businessDate;

	private Date transaction_date;

	private TaxRegimeModel taxRegimeModel;

	private Date WHTDedTransactionDate;
	private Double filerRate;
	private Double nonFilerRate;
	private Boolean isFiler;
	private Double sumAmount;
	private Boolean isCustomerInitiatedTransaction = false;
	private Boolean isUSSDCashWithdrawal = false;
	private Double loanAmount;
	private Double chargedAmount;
	private Long productId;
	private String recipientAccountNo;
	private String consumerNumber;
	private MiddlewareAdviceVO middlewareAdviceVO;

	public ProductDeviceFlowListViewModel getProductDeviceFlowModel()
	{
		return productDeviceFlowModel;
	}

	public void setProductDeviceFlowModel(ProductDeviceFlowListViewModel productDeviceFlowModel)
	{
		this.productDeviceFlowModel = productDeviceFlowModel;
	}

	public ProductDispenser getProductDispenser()
	{
		return productDispenser;
	}

	public void setProductDispenser(ProductDispenser productDispenser)
	{
		this.productDispenser = productDispenser;
	}

	public CommissionWrapper getCommissionWrapper()
	{
		return commissionWrapper;
	}

	public void setCommissionWrapper(CommissionWrapper commissionWrapper)
	{
		this.commissionWrapper = commissionWrapper;
	}

	public void setInstruction(NotificationMessageModel instruction)
	{
		this.instruction = instruction;
	}

	public void setSuccessMessage(NotificationMessageModel successMessage)
	{
		this.successMessage = successMessage;
	}

	public void setDistributorNmContactModel(DistributorContactModel distributorNmContactModel)
	{
		this.distributorNmContactModel = distributorNmContactModel;
	}

	public WorkFlowWrapperImpl()
	{
	}

	@Override
	public Boolean getInclChargesCheck() {
		return inclChargesCheck;
	}

	@Override
	public void setInclChargesCheck(Boolean inclChargesCheck) {
		this.inclChargesCheck = inclChargesCheck;
	}

	public RetailerContactModel getFromRetailerContactModel()
	{
		return this.fromRetailerContactModel;
	}

	public void setFromRetailerContactModel(RetailerContactModel fromRetailerContactModel)
	{
		this.fromRetailerContactModel = fromRetailerContactModel;
	}

	public RetailerContactModel getToRetailerContactModel()
	{
		return this.toRetailerContactModel;
	}

	public void setToRetailerContactModel(RetailerContactModel toRetailerContactModel)
	{
		this.toRetailerContactModel = toRetailerContactModel;
	}

	public ServiceTypeModel getServiceTypeModel()
	{
		return serviceTypeModel;
	}

	public HashMap getHashMap()
	{
		return hashMap;
	}

	public void setServiceTypeModel(ServiceTypeModel serviceTypeModel)
	{
		this.serviceTypeModel = serviceTypeModel;
	}

	public void setHashMap(HashMap hashMap)
	{
		this.hashMap = hashMap;
	}

	public void setDistributorContactModel(DistributorContactModel distributorContactModel)
	{
		this.distributorContactModel = distributorContactModel;
	}

	public void setDeviceTypeModel(DeviceTypeModel deviceTypeModel)
	{
		this.deviceTypeModel = deviceTypeModel;
	}

	public void setTransactionModel(TransactionModel transactionModel)
	{
		this.transactionModel = transactionModel;

	}

	public void setOperatorModel(OperatorModel operatorModel)
	{
		this.operatorModel = operatorModel;
	}

	public void setSupplierModel(SupplierModel supplierModel)
	{
		this.supplierModel = supplierModel;
	}

	public void setProductModel(ProductModel productModel)
	{
		this.productModel = productModel;
	}

	public void setCommissionAmountsHolder(CommissionAmountsHolder commissionAmountsHolder)
	{
		this.commissionAmountsHolder = commissionAmountsHolder;
	}

	public void setBankModel(BankModel bankModel)
	{
		this.bankModel = bankModel;
	}

	public void setMemberBankModel(MemberBankModel memberBankModel) { this.memberBankModel = memberBankModel; }

	public DistributorContactModel getDistributorContactModel()
	{
		return distributorContactModel;
	}

	public DeviceTypeModel getDeviceTypeModel()
	{
		return deviceTypeModel;
	}

	public TransactionModel getTransactionModel()
	{
		return transactionModel;
	}

	public OperatorModel getOperatorModel()
	{
		return this.operatorModel;
	}

	public SupplierModel getSupplierModel()
	{
		return supplierModel;
	}

	public ProductModel getProductModel()
	{
		return productModel;
	}

	@Override
	public MemberBankModel getMemberBankModel() {
		return memberBankModel;
	}

	public CommissionAmountsHolder getCommissionAmountsHolder()
	{
		return commissionAmountsHolder;
	}

	public BankModel getBankModel()
	{
		return bankModel;
	}

	public void setFailureLogModel(AuditLogModel failureLogModel)
	{
		this.failureLogModel = failureLogModel;
	}

	public void setTransactionTypeModel(TransactionTypeModel transactionTypeModel)
	{
		this.transactionTypeModel = transactionTypeModel;
	}

	public void setFromDistributorContactModel(DistributorContactModel fromDistributorContactModel)
	{
		this.fromDistributorContactModel = fromDistributorContactModel;
	}

	public void setToDistributorContactModel(DistributorContactModel toDistributorContactModel)
	{
		this.toDistributorContactModel = toDistributorContactModel;
	}

	public void setTransactionDetailModel(TransactionDetailModel transactionDetailModel)
	{
		this.transactionDetailModel = transactionDetailModel;
	}

	public AuditLogModel getFailureLogModel()
	{
		return failureLogModel;
	}

	public DistributorContactModel getFromDistributorContactModel()
	{
		return fromDistributorContactModel;
	}

	public TransactionDetailModel getTransactionDetailModel()
	{
		return transactionDetailModel;
	}

	public TransactionTypeModel getTransactionTypeModel()
	{
		return transactionTypeModel;
	}

	public DistributorContactModel getToDistributorContactModel()
	{
		return this.toDistributorContactModel;
	}

	public void setTransactionAmount(Double transactionAmount)
	{
		this.transactionAmount = transactionAmount;
	}

	@Override
	public void setCardAcceptorDetails(String cardAcceptorDetails) {
		this.cardAcceptorDetails = cardAcceptorDetails;
	}

	public void setTransactionCodeModel(TransactionCodeModel transactionCodeModel)
	{
		this.transactionCodeModel = transactionCodeModel;
	}

	public void setPaymentModeModel(PaymentModeModel paymentModeModel)
	{
		this.paymentModeModel = paymentModeModel;
	}

	public Double getTransactionAmount()
	{
		return this.transactionAmount;
	}

	@Override
	public String getCardAcceptorDetails() {
		return this.cardAcceptorDetails;
	}

	public TransactionCodeModel getTransactionCodeModel()
	{
		return transactionCodeModel;
	}

	public PaymentModeModel getPaymentModeModel()
	{
		return paymentModeModel;
	}

	public void setTotalAmount(Double totalAmount)
	{
		this.totalAmount = totalAmount;
	}

	public Double getTotalAmount()
	{
		return this.totalAmount;
	}

	public void setRetailerContactModel(RetailerContactModel retailerContactModel)
	{
		this.retailerContactModel = retailerContactModel;
	}

	public RetailerContactModel getRetailerContactModel()
	{
		return this.retailerContactModel;
	}

	public CustomerModel getCustomerModel()
	{
		return customerModel;
	}

	public void setCustomerModel(CustomerModel customerModel)
	{
		this.customerModel = customerModel;
	}

	public DistributorModel getDistributorModel()
	{

		return this.distributorModel;
	}

	public void setDistributorModel(DistributorModel distributorModel)
	{

		this.distributorModel = distributorModel;
	}

	public DistributorContactModel getDistributorNmContactModel()
	{

		return this.distributorNmContactModel;
	}

	public SmartMoneyAccountModel getSmartMoneyAccountModel()
	{
		return smartMoneyAccountModel;
	}

	public AppUserModel getAppUserModel()
	{
		return appUserModel;
	}

	@Override
	public void setCommissionThresholdRate(CommissionThresholdRateModel commissionThresholdRateModel) {
		this.commissionThresholdRateModel = commissionThresholdRateModel;
	}

	@Override
	public CommissionThresholdRateModel getCommissionThresholdRate() {
		return commissionThresholdRateModel;
	}

	public ProductVO getProductVO()
	{
		return productVO;
	}

	public AppUserModel getCustomerAppUserModel()
	{
		return customerAppUserModel;
	}

	public AppUserModel getRetailerAppUserModel()
	{
		return retailerAppUserModel;
	}

	public AppUserModel getDistributorAppUserModel()
	{
		return distributorAppUserModel;
	}

	public void setSmartMoneyAccountModel(SmartMoneyAccountModel smartMoneyAccountModel)
	{
		this.smartMoneyAccountModel = smartMoneyAccountModel;
	}

	public void setAppUserModel(AppUserModel appUserModel)
	{
		this.appUserModel = appUserModel;
	}

	public void setProductVO(ProductVO productVO)
	{
		this.productVO = productVO;
	}

	public void setCustomerAppUserModel(AppUserModel customerAppUserModel)
	{
		this.customerAppUserModel = customerAppUserModel;
	}

	public void setRetailerAppUserModel(AppUserModel retailerAppUserModel)
	{
		this.retailerAppUserModel = retailerAppUserModel;
	}

	public void setDistributorAppUserModel(AppUserModel distributorAppUserModel)
	{
		this.distributorAppUserModel = distributorAppUserModel;
	}

	public ProductUnitModel getProductUnitModel()
	{

		return this.productUnitModel;
	}

	public AppUserModel getFromDistributorContactAppUserModel()
	{
		return fromDistributorContactAppUserModel;
	}

	public AppUserModel getFromRetailerContactAppUserModel()
	{
		return fromRetailerContactAppUserModel;
	}

	public AppUserModel getToDistributorContactAppUserModel()
	{
		return toDistributorContactAppUserModel;
	}

	public AppUserModel getToRetailerContactAppUserModel()
	{
		return toRetailerContactAppUserModel;
	}

	public AccountInfoModel getAccountInfoModel()
	{
		return accountInfoModel;
	}

	public void setProductUnitModel(ProductUnitModel productUnitModel)
	{
		this.productUnitModel = productUnitModel;

	}

	public AppUserModel getToDistributorContactAppUserModel(AppUserModel toDistributorContactAppUserModel)
	{
		// TODO Auto-generated method stub
		return this.toDistributorContactAppUserModel;
	}

	public void setToDistributorContactAppUserModel(AppUserModel toDistributorContactAppUserModel)
	{
		this.toDistributorContactAppUserModel = toDistributorContactAppUserModel;

	}

	public AppUserModel getFromDistributorContactAppUserModel(AppUserModel fromDistributorContactAppUserModel)
	{

		return this.fromDistributorContactAppUserModel;
	}

	public void setFromDistributorContactAppUserModel(AppUserModel fromDistributorContactAppUserModel)
	{
		this.fromDistributorContactAppUserModel = fromDistributorContactAppUserModel;

	}

	public void setFromRetailerContactAppUserModel(AppUserModel fromRetailerContactAppUserModel)
	{
		this.fromRetailerContactAppUserModel = fromRetailerContactAppUserModel;
	}

	public void setToRetailerContactAppUserModel(AppUserModel toRetailerContactAppUserModel)
	{
		this.toRetailerContactAppUserModel = toRetailerContactAppUserModel;
	}

	public void setAccountInfoModel(AccountInfoModel accountInfoModel)
	{
		this.accountInfoModel = accountInfoModel;
	}

	public void setTotalCommissionAmount(Double totalCommissionAmount)
	{
		this.totalCommissionAmount = totalCommissionAmount;
	}

	public void setBillAmount(Double billAmount)
	{
		this.billAmount = billAmount;
	}

	public void setTxProcessingAmount(Double txProcessingAmount)
	{
		this.txProcessingAmount = txProcessingAmount;
	}

	public void setSupplierBankInfoModel(SupplierBankInfoModel supplierBankInfoModel)
	{
		this.supplierBankInfoModel = supplierBankInfoModel;
	}

	public UserDeviceAccountsModel getUserDeviceAccountModel()
	{

		return this.userDeviceAccountsModel;
	}

	public void setUserDeviceAccountModel(UserDeviceAccountsModel userDeviceAccountsModel)
	{

		this.userDeviceAccountsModel = userDeviceAccountsModel;
	}

	public NotificationMessageModel getInstruction()
	{
		return instruction;
	}

	public NotificationMessageModel getSuccessMessage()
	{
		return successMessage;
	}

	public Double getTotalCommissionAmount()
	{
		return totalCommissionAmount;
	}

	public Double getBillAmount()
	{
		return billAmount;
	}

	public Double getTxProcessingAmount()
	{
		return txProcessingAmount;
	}

	public SupplierBankInfoModel getSupplierBankInfoModel()
	{
		return supplierBankInfoModel;
	}

	public String getCcCVV()
	{
		return ccCVV;
	}

	public void setCcCVV(String ccCVV)
	{
		this.ccCVV = ccCVV;
	}

	public OperatorBankInfoModel getOperatorBankInfoModel()
	{
		return operatorBankInfoModel;
	}

	public void setOperatorBankInfoModel(OperatorBankInfoModel operatorBankInfoModel)
	{
		this.operatorBankInfoModel = operatorBankInfoModel;
	}


	public OperatorBankInfoModel getOperatorPayingBankInfoModel()
	{
		return operatorPayingBankInfoModel;
	}


	public void setOperatorPayingBankInfoModel(OperatorBankInfoModel operatorPayingBankInfoModel)
	{
		this.operatorPayingBankInfoModel = operatorPayingBankInfoModel;
	}

	public FinancialTransactionsMileStones getFinancialTransactionsMileStones()
	{
		return financialTransactionsMileStones;
	}

	public void setFinancialTransactionsMileStones(FinancialTransactionsMileStones financialTransactionsMileStones)
	{
		this.financialTransactionsMileStones = financialTransactionsMileStones;
	}

	public String getMPin()
	{
		return this.mPin;
	}

	public void setMPin(String mPin)
	{
		this.mPin = mPin;
	}

	public CustomerAccount getCustomerAccount()
	{
		return this.customerAccount;
	}

	public void setCustomerAccount(CustomerAccount customerAccount)
	{
		this.customerAccount = customerAccount;
	}


	public SwitchWrapper getSwitchWrapper()
	{
		return switchWrapper;
	}


	public void setSwitchWrapper(SwitchWrapper switchWrapper)
	{
		this.switchWrapper = switchWrapper;
	}

	public Double getDiscountAmount()
	{
		// TODO Auto-generated method stub
		return discountAmount;
	}

	public void setDiscountAmount(Double discountAmount)
	{
		this.discountAmount = discountAmount;

	}

	public SmartMoneyAccountModel getReceivingSmartMoneyAccountModel()
	{
		return receivingSmartMoneyAccountModel;
	}

	public void setReceivingSmartMoneyAccountModel(SmartMoneyAccountModel receivingSmartMoneyAccountModel)
	{
		this.receivingSmartMoneyAccountModel = receivingSmartMoneyAccountModel;
	}


	public SmartMoneyAccountModel getOlaSmartMoneyAccountModel()
	{
		return olaSmartMoneyAccountModel;
	}


	public void setOlaSmartMoneyAccountModel(SmartMoneyAccountModel olaSmartMoneyAccountModel)
	{
		this.olaSmartMoneyAccountModel = olaSmartMoneyAccountModel;
	}

	public AppUserModel getDistOrRetAppUserModel()
	{
		// TODO Auto-generated method stub
		return distOrRetAppUserModel;
	}

	public void setDistOrRetAppUserModel(AppUserModel distOrRetAppUserModel)
	{
		this.distOrRetAppUserModel = distOrRetAppUserModel;

	}

	public boolean isCallNextProductDispenser() {
		return callNextProductDispenser;
	}

	public void setCallNextProductDispenser(boolean callNextProductDispenser) {
		this.callNextProductDispenser = callNextProductDispenser;
	}

	public SegmentModel getSegmentModel() {
		return segmentModel;
	}

	public void setSegmentModel(SegmentModel segmentModel) {
		this.segmentModel = segmentModel;
	}

	public String getWalkInCustomerCNIC() {
		return walkInCustomerCNIC;
	}

	public void setWalkInCustomerCNIC(String walkInCustomerCNIC) {
		this.walkInCustomerCNIC = walkInCustomerCNIC;
	}

	public String getWalkInCustomerMob() {
		return walkInCustomerMob;
	}

	public void setWalkInCustomerMob(String walkInCustomerMob) {
		this.walkInCustomerMob = walkInCustomerMob;
	}

	public void setOneTimePin(String oneTimePin) {
		// TODO Auto-generated method stub
		this.oneTimePin = oneTimePin;

	}

	public String getOneTimePin() {
		// TODO Auto-generated method stub
		return oneTimePin;
	}

	public void setOLASwitchWrapper(SwitchWrapper switchWrapper) {
		this.olaSwitchWrapper = switchWrapper;

	}

	public SwitchWrapper getOLASwitchWrapper() {
		// TODO Auto-generated method stub
		return this.olaSwitchWrapper;
	}

	public void setRecipientSwitchWrapper(SwitchWrapper recipientSwitchWrapper) {
		this.recipientSwitchWrapper = recipientSwitchWrapper;

	}

	public SwitchWrapper getRecipientSwitchWrapper() {

		return this.recipientSwitchWrapper;
	}

	public void setRecipientSmartMoneyAccountModel(
			SmartMoneyAccountModel recipientSmartMoneyAccountModel) {

		this.recipientSmartMoneyAccountModel = recipientSmartMoneyAccountModel;
	}

	public SmartMoneyAccountModel getRecipientSmartMoneyAccountModel() {
		// TODO Auto-generated method stub
		return this.recipientSmartMoneyAccountModel;
	}

	public WalkinCustomerModel getRecipientWalkinCustomerModel() {
		return recipientWalkinCustomerModel;
	}

	public void setRecipientWalkinCustomerModel(WalkinCustomerModel recepientWalkinCustomerModel) {
		this.recipientWalkinCustomerModel = recepientWalkinCustomerModel;
	}

	public SmartMoneyAccountModel getRecipientWalkinSmartMoneyAccountModel() {
		return recipientWalkinSmartMoneyAccountModel;
	}

	public void setRecipientWalkinSmartMoneyAccountModel(SmartMoneyAccountModel recepientWalkinSmartMoneyAccountModel) {
		this.recipientWalkinSmartMoneyAccountModel = recepientWalkinSmartMoneyAccountModel;
	}

	public SmartMoneyAccountModel getSenderSmartMoneyAccountModel() {
		return senderSmartMoneyAccountModel;
	}

	public void setSenderSmartMoneyAccountModel(SmartMoneyAccountModel senderSmartMoneyAccountModel) {
		this.senderSmartMoneyAccountModel = senderSmartMoneyAccountModel;
	}

	public AppUserModel getSenderAppUserModel() {
		return senderAppUserModel;
	}

	public void setSenderAppUserModel(AppUserModel senderAppUserModel) {
		this.senderAppUserModel = senderAppUserModel;
	}

	public WalkinCustomerModel getSenderWalkinCustomerModel() {
		// TODO Auto-generated method stub
		return senderWalkinCustomerModel;
	}

	public void setSenderWalkinCustomerModel(
			WalkinCustomerModel senderWalkinCustomerModel) {
		this.senderWalkinCustomerModel=senderWalkinCustomerModel;
	}

	public SwitchWrapper getOlaSwitchWrapper_2() {
		return olaSwitchWrapper_2;
	}

	public void setOlaSwitchWrapper_2(SwitchWrapper olaSwitchWrapper_2) {
		this.olaSwitchWrapper_2 = olaSwitchWrapper_2;
	}

	public SwitchWrapper getOlaSwitchWrapper_3() {
		return olaSwitchWrapper_3;
	}

	public void setOlaSwitchWrapper_3(SwitchWrapper olaSwitchWrapper_3) {
		this.olaSwitchWrapper_3 = olaSwitchWrapper_3;
	}

	public SwitchWrapper getOlaSwitchWrapper_4() {
		return olaSwitchWrapper_4;
	}

	public void setOlaSwitchWrapper_4(SwitchWrapper olaSwitchWrapper_4) {
		this.olaSwitchWrapper_4 = olaSwitchWrapper_4;
	}

	public RetailerModel getRetailerModel() {
		return retailerModel;
	}

	public void setRetailerModel(RetailerModel retailerModel) {
		this.retailerModel = retailerModel;
	}

	public boolean isAccountToCashLeg1() {
		return isAccountToCashLeg1;
	}

	public void setAccountToCashLeg1(boolean isAccountToCashLeg1) {
		this.isAccountToCashLeg1 = isAccountToCashLeg1;
	}

	public boolean isCashToCashLeg1() {
		return isCashToCashLeg1;
	}

	public void setCashToCashLeg1(boolean isCashToCashLeg1) {
		this.isCashToCashLeg1 = isCashToCashLeg1;
	}

	public boolean isCashDeposit() {
		return isCashDeposit;
	}

	public void setCashDeposit(boolean isCashDeposit) {
		this.isCashDeposit = isCashDeposit;
	}

	public boolean isCashWithdrawl() {
		return isCashWithdrawl;
	}

	public void setCashWithdrawl(boolean isCashWithdrawl) {
		this.isCashWithdrawl = isCashWithdrawl;
	}

	public boolean isP2PRecepient() {
		return isP2PRecepient;
	}

	public void setP2PRecepient(boolean isP2PRecepient) {
		this.isP2PRecepient = isP2PRecepient;
	}

	public Boolean getUSSDCashWithdrawal() {
		return isUSSDCashWithdrawal;
	}

	@Override
	public ThirdPartyAccountOpeningModel getThirdPartyAccountOpeningModel() {
		return thirdPartyAccountOpeningModel;
	}

	@Override
	public BOPCardIssuanceModel getBOPCardIssuanceModel() {
		return bopCardIssuanceModel;
	}

	@Override
	public ScheduleLoanPaymentModel getScheduleLoanPaymentModel() {
		return scheduleLoanPaymentModel;
	}

	public void setUSSDCashWithdrawal(Boolean USSDCashWithdrawal) {
		isUSSDCashWithdrawal = USSDCashWithdrawal;
	}

	@Override
	public boolean isCheckBalance() {
		return this.isCheckBalance;
	}

	@Override
	public void setCheckBalance(boolean isCheckBalance) {
		this.isCheckBalance = isCheckBalance;
	}

	public AppUserModel getReceiverAppUserModel() {
		return receiverAppUserModel;
	}

	public void setReceiverAppUserModel(AppUserModel receiverAppUserModel) {
		this.receiverAppUserModel = receiverAppUserModel;
	}

	@Override
	public void setFirstFTIntegrationVO(
			IntegrationMessageVO integrationMessageVO) {
		// TODO Auto-generated method stub
		this.firstFTIntegrationVO = integrationMessageVO;
	}

	@Override
	public IntegrationMessageVO getFirstFTIntegrationVO() {
		// TODO Auto-generated method stub
		return firstFTIntegrationVO;
	}

	@Override
	public void setFirstFTSwitchWrapper(SwitchWrapper switchWrapper) {
		this.firstFTSwitchWrapper = switchWrapper;

	}

	@Override
	public SwitchWrapper getFirstFTSwitchWrapper() {
		return firstFTSwitchWrapper;
	}

	@Override
	public boolean isCRetailPayment() {
		return isCRetailPayment;
	}

	@Override
	public void setIsCRetailPayment(boolean isCRP) {
		this.isCRetailPayment = isCRP;
	}

	@Override
	public void setBillPaymentIntegrationVO(IntegrationMessageVO integrationMessageVO) {
		this.billPaymentIntegrationVO = integrationMessageVO;

	}

	@Override
	public IntegrationMessageVO getBillPaymentIntegrationVO() {
		return this.billPaymentIntegrationVO;
	}

	@Override
	public void setBillPaymentSwitchWrapper(SwitchWrapper switchWrapper) {
		this.billPaymentSwitchWrapper = switchWrapper;

	}

	@Override
	public SwitchWrapper getBillPaymentSwitchWrapper() {
		return this.billPaymentSwitchWrapper;
	}

	public RetailerContactModel getHeadRetailerContactModel() {
		return headRetailerContactModel;
	}

	public void setHeadRetailerContactModel(RetailerContactModel headRetailerContactModel) {
		this.headRetailerContactModel = headRetailerContactModel;
	}

	public SmartMoneyAccountModel getHeadRetailerSmaModel() {
		return headRetailerSmaModel;
	}

	public void setHeadRetailerSmaModel(SmartMoneyAccountModel headRetailerSmaModel) {
		this.headRetailerSmaModel = headRetailerSmaModel;
	}

	public AppUserModel getHeadRetailerAppuserModel() {
		return headRetailerAppuserModel;
	}

	public void setHeadRetailerAppuserModel(AppUserModel headRetailerAppuserModel) {
		this.headRetailerAppuserModel = headRetailerAppuserModel;
	}

	public TransactionDetailMasterModel getTransactionDetailMasterModel() {
		return transactionDetailMasterModel;
	}

	public void setTransactionDetailMasterModel(
			TransactionDetailMasterModel transactionDetailMasterModel) {
		this.transactionDetailMasterModel = transactionDetailMasterModel;
	}

	public MiniTransactionModel getMiniTransactionModel() {
		return miniTransactionModel;
	}

	public void setMiniTransactionModel(MiniTransactionModel miniTransactionModel) {
		this.miniTransactionModel = miniTransactionModel;
	}

	public boolean isWalkinLimitApplicable() {
		return isWalkinLimitApplicable;
	}

	public void setWalkinLimitApplicable(boolean isWalkinLimitApplicable) {
		this.isWalkinLimitApplicable = isWalkinLimitApplicable;
	}

	public Long getBulkDisbursmentsId() {
		return bulkDisbursmentsId;
	}

	public void setBulkDisbursmentsId(Long bulkDisbursmentsId) {
		this.bulkDisbursmentsId = bulkDisbursmentsId;
	}

	public Long getFromSegmentId() {
		return fromSegmentId;
	}

	public void setFromSegmentId(Long fromSegmentId) {
		this.fromSegmentId = fromSegmentId;
	}

	public Long getToSegmentId() {
		return toSegmentId;
	}

	public void setToSegmentId(Long toSegmentId) {
		this.toSegmentId = toSegmentId;
	}

	public Object getCustomField() {
		return customField;
	}

	public void setCustomField(Object customField) {
		this.customField = customField;
	}

	public Boolean getIsIvrResponse() {
		return isIvrResponse;
	}

	public void setIsIvrResponse(Boolean isIvrResponse) {
		this.isIvrResponse = isIvrResponse;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public CustomerModel getRecipientCustomerModel() {
		return recipientCustomerModel;
	}

	public void setRecipientCustomerModel(CustomerModel recipientCustomerModel) {
		this.recipientCustomerModel = recipientCustomerModel;
	}

	public SwitchWrapper getMiddlewareSwitchWrapper() {
		return middlewareSwitchWrapper;
	}

	public void setMiddlewareSwitchWrapper(SwitchWrapper middlewareSwitchWrapper) {
		this.middlewareSwitchWrapper = middlewareSwitchWrapper;
	}

	public HandlerModel getHandlerModel() {
		return handlerModel;
	}

	public void setHandlerModel(HandlerModel handlerModel) {
		this.handlerModel = handlerModel;
	}

	public void setHandlerAppUserModel(AppUserModel handlerAppUserModel) {
		this.handlerAppUserModel = handlerAppUserModel;

	}

	public AppUserModel getHandlerAppUserModel() {
		return handlerAppUserModel;
	}

	public RetailerContactModel getHandlerRetContactModel() {
		return handlerRetContactModel;
	}

	public void setHandlerRetContactModel(
			RetailerContactModel handlerRetContactModel) {
		this.handlerRetContactModel = handlerRetContactModel;
	}

	public SmartMoneyAccountModel getHandlerSMAModel() {
		return handlerSMAModel;
	}

	public void setHandlerSMAModel(SmartMoneyAccountModel handlerSMAModel) {
		this.handlerSMAModel = handlerSMAModel;
	}

	@Override
	public void setHandlerUserDeviceAccountModel(UserDeviceAccountsModel handlerUserDeviceAccountModel) {
		this.handlerUserDeviceAccountModel = handlerUserDeviceAccountModel;
	}

	@Override
	public TaxRegimeModel getTaxRegimeModel() {
		return taxRegimeModel;
	}

	@Override
	public void setTaxRegimeModel(TaxRegimeModel taxRegimeModel) {
		this.taxRegimeModel = taxRegimeModel;
	}

	@Override
	public UserDeviceAccountsModel getHandlerUserDeviceAccountModel() {
		return handlerUserDeviceAccountModel;
	}

	@Override
	public boolean isLeg2Transaction() {
		return isLeg2Transaction;
	}

	@Override
	public void setLeg2Transaction(boolean isLeg2Transaction) {
		this.isLeg2Transaction = isLeg2Transaction;
	}

	@Override
	public Long getCurrentSupProcessingStatusId() {
		return currentSupProcessingStatusId;
	}

	@Override
	public void setCurrentSupProcessingStatusId(Long currentSupProcessingStatusId) {
		this.currentSupProcessingStatusId = currentSupProcessingStatusId;
	}

	@Override
	public Long getAccOpeningAppUserId() {
		return accOpeningAppUserId;
	}

	@Override
	public void setAccOpeningAppUserId(Long accOpeningAppUserId) {
		this.accOpeningAppUserId = accOpeningAppUserId;
	}

	@Override
	public AgentTransferRuleModel getAgentTransferRuleModel() {
		return agentTransferRuleModel;
	}

	@Override
	public void setAgentTransferRuleModel(AgentTransferRuleModel agentTransferRuleModel) {
		this.agentTransferRuleModel = agentTransferRuleModel;
	}

	@Override
	public void setCommissionSettledOnLeg2(boolean value) {
		this.commissionSettledOnLeg2 = value;
	}

	@Override
	public boolean isCommissionSettledOnLeg2() {
		return commissionSettledOnLeg2;
	}

	@Override
	public Date getBusinessDate() {
		return businessDate;
	}
	@Override
	public void setBusinessDate(Date businessDate) {
		this.businessDate = businessDate;
	}

	@Override
	public boolean isSenderBvs() {
		return isSenderBvs;
	}

	@Override
	public void setSenderBvs(boolean senderBvs) {
		isSenderBvs = senderBvs;
	}

	@Override
	public boolean isReceiverBvs() {
		return isReceiverBvs;
	}

	@Override
	public void setReceiverBvs(boolean receiverBvs) {
		isReceiverBvs = receiverBvs;
	}

	public WorkFlowWrapper cloneForDisbursement() {
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();

//		workFlowWrapper.setBusinessDate(this.getBusinessDate());
    	workFlowWrapper.setTaxRegimeModel(this.getTaxRegimeModel());
		workFlowWrapper.setSegmentModel(this.getSegmentModel());
		workFlowWrapper.setTransactionTypeModel(this.getTransactionTypeModel());
		workFlowWrapper.setDeviceTypeModel(this.getDeviceTypeModel());
		workFlowWrapper.setAppUserModel(this.getAppUserModel());

		workFlowWrapper.setProductModel(this.getProductModel());

		workFlowWrapper.putObject(StakeholderBankInfoModel.ACCOUNT_TYPE_BLB, this.getObject(StakeholderBankInfoModel.ACCOUNT_TYPE_BLB));
		workFlowWrapper.putObject(StakeholderBankInfoModel.ACCOUNT_TYPE_OF_SET, this.getObject(StakeholderBankInfoModel.ACCOUNT_TYPE_OF_SET));
		workFlowWrapper.putObject(StakeholderBankInfoModel.ACCOUNT_TYPE_CORE, this.getObject(StakeholderBankInfoModel.ACCOUNT_TYPE_CORE));
		workFlowWrapper.putObject("SUNDRY_ACCOUNT", this.getObject("SUNDRY_ACCOUNT"));

		return workFlowWrapper;
	}

	@Override
	public void setStartDate(Date start_date) {
		this.start_date = start_date;

	}

	@Override
	public Date getStartDate() {
		// TODO Auto-generated method stub
		return start_date;
	}

	@Override
	public void setTransactionDate(Date transaction_date) {
		// TODO Auto-generated method stub
		this.transaction_date = transaction_date;
	}

	@Override
	public Date getTransactionDate() {
		// TODO Auto-generated method stub
		return transaction_date;
	}

	@Override
	public void setWHTDedTransactionDate(Date transaction_date) {
      		this.WHTDedTransactionDate = transaction_date;
	}

	@Override
	public Date getWHTDedTransactionDate() {
		// TODO Auto-generated method stub
		return WHTDedTransactionDate;
	}

	@Override
	public Double getFilerRate() {
		return filerRate;
	}

	@Override
	public void setFilerRate(Double filerRate) {
		this.filerRate = filerRate;
	}

	@Override
	public Double getNonFilerRate() {
		return nonFilerRate;
	}

	@Override
	public void setNonFilerRate(Double nonFilerRate) {
		this.nonFilerRate = nonFilerRate;
	}

	@Override
	public Boolean getIsFiler() {
		return isFiler;
	}

	@Override
	public void setIsFiler(Boolean isFiler) {
		this.isFiler = isFiler;
	}

	@Override
	public void setSumAmount(Double sumAmount) {
		this.sumAmount = sumAmount;
	}

	@Override
	public Double getSumAmount() {
		return sumAmount;
	}

	@Override
	public void setIsCustomerInitiatedTransaction(Boolean isCustomerInitiatedTransaction) {
		this.isCustomerInitiatedTransaction = isCustomerInitiatedTransaction;
	}

	@Override
	public Boolean getIsCustomerInitiatedTransaction() {
		return isCustomerInitiatedTransaction;
	}

	@Override
	public SafRepoCashOutModel getSafRepoCashOutModel() {
		return safRepoCashOutModel;
	}

	@Override
	public void setSafRepoCashOutModel(SafRepoCashOutModel safRepoCashOutModel) {
		this.safRepoCashOutModel = safRepoCashOutModel;
	}

	public void setThirdPartyAccountOpeningModel(ThirdPartyAccountOpeningModel thirdPartyAccountOpeningModel) {
		this.thirdPartyAccountOpeningModel = thirdPartyAccountOpeningModel;
	}

	@Override
	public void setBOPCardIssuanceModel(BOPCardIssuanceModel bopCardIssuanceModel) {
		this.bopCardIssuanceModel = bopCardIssuanceModel;
	}

	public void setScheduleLoanPaymentModel(ScheduleLoanPaymentModel scheduleLoanPaymentModel) {
		this.scheduleLoanPaymentModel = scheduleLoanPaymentModel;
	}

	public Double getLoanAmount() { return loanAmount; }

	@Override
	public void setChargedAmount(Double chargedAmount) {
		this.chargedAmount = chargedAmount;
	}

	@Override
	public Double getChargedAmount() {
		return chargedAmount;
	}

	public void setLoanAmount(Double loanAmount) { this.loanAmount = loanAmount; }

	@Override
	public Long getProductId() {
		return productId;
	}

	@Override
	public void setMiddlewareAdviceVO(MiddlewareAdviceVO middlewareAdviceVO) {
		this.middlewareAdviceVO = middlewareAdviceVO;
	}

	@Override
	public MiddlewareAdviceVO getMiddlewareAdviceVO() {
		return middlewareAdviceVO;
	}

	@Override
	public void setRecipientAccountNo(String recipientAccountNo) {
		this.recipientAccountNo = recipientAccountNo;
	}

	@Override
	public String getRecipientAccountNo() {
		return recipientAccountNo;
	}

	@Override
	public void setConsumerNumber(String consumerNumber) {
		this.consumerNumber = consumerNumber;
	}

	@Override
	public String getConsumerNumber() {
		return consumerNumber;
	}

	@Override
	public void setProductId(Long productId) {
		this.productId = productId;
	}
}