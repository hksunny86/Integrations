package com.inov8.microbank.server.service.portal.mfsaccountmodule;

import java.util.List;

import com.inov8.integration.common.model.AccountModel;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.customermodule.BlinkCustomerPictureModel;
import com.inov8.microbank.common.model.customermodule.MerchantAccountPictureModel;
import org.apache.commons.collections.CollectionUtils;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.customermodule.CustomerPictureModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.ApplicantDetailModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.BusinessDetailModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.MfsAccountModel;
import com.inov8.microbank.common.util.RegistrationStateConstants;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.vo.account.BulkCustomerAccountVo;
import com.inov8.microbank.server.webserviceclient.ivr.IvrRequestDTO;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;

public interface MfsAccountManager
{
	public CustomerPictureModel getCustomerPictureByTypeId(Long pictureTypeId, Long customerId) throws FrameworkCheckedException;
	public CustomerPictureModel getCustomerPictureByTypeIdAndStatus(Long pictureTypeId, Long customerId) throws FrameworkCheckedException;
	public BlinkCustomerPictureModel getBlinkCustomerPictureByTypeId(Long pictureTypeId, Long customerId) throws FrameworkCheckedException;
	public MerchantAccountPictureModel getMerchantCustomerPictureByTypeId(Long pictureTypeId, Long customerId) throws FrameworkCheckedException;

	public List<CustomerPictureModel> getAllCustomerPictures(Long customerId) throws FrameworkCheckedException;
	public SearchBaseWrapper searchCustomerPictures(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	public static final String	KEY_IS_ACTIVE = "isActive";
	//	public BaseWrapper createMfsWalkinAccount(String senderMobileNumber, String walkInMobileNumber, String walkInCnic) throws FrameworkCheckedException;
	public WalkinCustomerModel getWalkinCustomerModel(WalkinCustomerModel _walkinCustomerModel);
	BaseWrapper createMfsAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	BaseWrapper createLevel2Account(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	Boolean updateWalkinCustomerIfExists(String walkInCNIC, String walkInMobileNumber, String senderMobileNumber) throws FrameworkCheckedException;
	BaseWrapper updateMfsAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException;

	BaseWrapper updateMfsMerchantAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException;

	void saveClsPendignAccount(BaseWrapper baseWrapper, I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO) throws FrameworkCheckedException;

	BaseWrapper updateMfsDebitCard(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	BaseWrapper updateLevel2Account(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	SearchBaseWrapper searchUserInfo(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	SearchBaseWrapper searchMinorUserInfo(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	BaseWrapper searchAppUserByPrimaryKey(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	void logPrintAction(Long customerId) throws FrameworkCheckedException;
	BaseWrapper activateDeactivateMfsAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	BaseWrapper createMfsAccountThroughSMS(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	BaseWrapper searchUserInfoByPrimaryKey(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	OLAVO getAccountInfoFromOLA(String cnic,Long bankId) throws FrameworkCheckedException;
	SmartMoneyAccountModel getSmartMoneyAccountByCustomerId(Long customerId)throws FrameworkCheckedException;
	void accountBlock(BaseWrapper baseWrapper);

	//Added by Sheheryaar
	List<SmartMoneyAccountModel> getLastClosedSMAAccount(SmartMoneyAccountModel smartMoneyAccountModel);
	AccountModel getLastClosedAccountModel(String cnic, Long customerAccountTypeId);

	AccountModel getAccountModelByCnicAndCustomerAccountTypeAndStatusId(String cnic, Long customerAccountTypeId, Long statusId);

	//Added By Sheheryaar
	SmartMoneyAccountModel getSmartMoneyAccountByExample(SmartMoneyAccountModel _smartMoneyAccountModel) throws FrameworkCheckedException;
	//
	SmartMoneyAccountModel getSmartMoneyAccount(SmartMoneyAccountModel _smartMoneyAccountModel) throws FrameworkCheckedException;
	UserDeviceAccountsModel getDeviceAccountByAppUserId(Long appUserId, Long deviceTypeId) throws FrameworkCheckedException;
	AppUserModel getAppUserModel(AppUserModel exampleInstance) throws FrameworkCheckedException;
	List<BulkDisbursementsModel> validateMobileNos(List<BulkDisbursementsModel> bulkList) throws FrameworkCheckedException;
	Boolean isAlreadyRegistered(String CNIC, String mobileNumber) throws FrameworkCheckedException;
	Boolean isAlreadyRegistered(String CNIC) throws FrameworkCheckedException;
	SearchBaseWrapper loadAppUserByMobileNumberAndType(SearchBaseWrapper searchBaseWrapper);
	public SearchBaseWrapper loadAppUserByMobileNumberAndTypeHQL(SearchBaseWrapper searchBaseWrapper);
	public AppUserModel getAppUserModelByPrimaryKey(Long appUserId);
	public AppUserModel getAppUserModelByMobileNumber(String mobileNo);
	String getAreaByAppUserId(Long appUserId);
	public BaseWrapper updateAppUserModel(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public double getMfsAccountBalance(Long appUserId,Long paymentModeId) throws Exception;
	public BaseWrapper createWalkinCustomerAccount(String walkInCnic, String walkInMobileNumber, String senderMobileNumber) throws FrameworkCheckedException;
	public BaseWrapper createWalkinCustomerAccount(String walkInCnic, String walkInMobileNumber, AppUserModel createdByAppUserModel) throws FrameworkCheckedException;
	List<BulkDisbursementsModel> isAlreadyRegistered(List<BulkDisbursementsModel> bulkList) throws FrameworkCheckedException;
	List<BulkCustomerAccountVo> isCustomerOrAgent(List<BulkCustomerAccountVo> bulkCustomerAccountVoList) throws FrameworkCheckedException;
	public VeriflyBaseWrapper changePIN(VeriflyBaseWrapper wrapper) throws Exception;
	void enqueueBulkCustomerAccountsCreation(List<BulkCustomerAccountVo> bulkCustomerAccountVoList) throws FrameworkCheckedException;
	ApplicantDetailModel loadApplicantDetail(ApplicantDetailModel model)throws FrameworkCheckedException;
	BusinessDetailModel loadBusinessDetail(BusinessDetailModel model)throws FrameworkCheckedException;
	List<OlaCustomerAccountTypeModel> loadCustomerACTypes(Long[] typesId) throws FrameworkCheckedException;
	public void makeInitialDeposit(MfsAccountModel mfsAccountModel) throws CommandException;
	public void verifyAgentBalanceForInitialDeposit(double initialDeposit) throws Exception;
	public ApplicantDetailModel loadApplicantDetailById(Long applicantDetailId) throws FrameworkCheckedException;
	public List<ApplicantDetailModel> loadApplicantDetails(ApplicantDetailModel model) throws FrameworkCheckedException;
	public String computeMfsId();
	public BaseWrapper createHandlerAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public BaseWrapper closeHandlerAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	void validateUniqueness(MfsAccountModel mfsAccountModel) throws FrameworkCheckedException;
	public void initiateUserGeneratedPinIvrCall(IvrRequestDTO ivrDTO)
			throws FrameworkCheckedException;
	public String[] getFundSourceName(String[] fundSourceIds) throws FrameworkCheckedException;
	public BaseWrapper createKYC(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public List<KYCModel> findKycModel(SearchBaseWrapper baseWrapper)throws FrameworkCheckedException;
	public List<ACOwnershipDetailModel> loadAccountOwnerShipDetails(SearchBaseWrapper baseWrapper)
			throws FrameworkCheckedException;
	public List<ApplicantTxModeModel> loadApplicantTxModeModelByInitialApplicationFormNo(
			String formNumber) throws FrameworkCheckedException;
	public BaseWrapper updateKYC(BaseWrapper baseWrapper)
			throws FrameworkCheckedException;
	/**
	 * @author atif hussain
	 * @param baseWrapper
	 * @return
	 * @throws FrameworkCheckedException
	 */
	BaseWrapper createAgentMerchant(BaseWrapper baseWrapper)throws FrameworkCheckedException;
	BaseWrapper updateAgentMerchant(BaseWrapper baseWrapper)throws FrameworkCheckedException;
	SearchBaseWrapper searchAgentMerchant(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	Boolean isAlreadyExistInitAppFormNumber(String initialApplicationFormNo) throws FrameworkCheckedException;
	Boolean isAlreadyExistReferenceNumber(String referenceNo, String initialAppFormNo) throws FrameworkCheckedException;
	BaseWrapper findAgentMerchantByInitialAppFormNo(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public BaseWrapper searchCustomerByInitialAppFormNo(BaseWrapper wrapper)
			throws FrameworkCheckedException;
	public BaseWrapper loadCustomer(BaseWrapper wrapper) throws FrameworkCheckedException;
	public AgentMerchantDetailModel loadAgentMerchantDetailModel(Long agentMerchantDetailId) throws FrameworkCheckedException;
	public SearchBaseWrapper searchKYC(SearchBaseWrapper baseWrapper)
			throws FrameworkCheckedException;
	List<ACOwnershipDetailModel> loadL3AccountOwnerShipDetails(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	public void isUniqueCNICMobile(AppUserModel appUserModel, BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public KYCModel findKycByPrimaryKey(Long kycId) throws FrameworkCheckedException;
	boolean isAppUserExist(String userName) throws FrameworkCheckedException;
	boolean isAppUserUsernameUniqueForUpdate(String userName, String initAppFormNo) throws FrameworkCheckedException;
	AgentMerchantDetailModel loadAgentMerchantDetailModelByUserName(String userName) throws FrameworkCheckedException;
	CityModel loadCityModel(Long cityId) throws FrameworkCheckedException;
	OccupationModel loadOccupationModel(Long occupationId) throws FrameworkCheckedException;
	ProfessionModel loadProfessionModel(Long professionId) throws FrameworkCheckedException;
	BankModel getOlaBankMadal();
	public void saveRootedMobileHistory(BaseWrapper baseWrapper) throws FrameworkCheckedException;

	int validateCNIC(String cnic, String mobile) throws Exception;
	int validateMobileNo(String mobileNo, Long appUserTypeId) throws Exception;

	public String settleAccountOpeningCommission(Long customerId) throws FrameworkCheckedException;

	void saveBulkAgentData(List<BulkAgentDataHolderModel> list) throws FrameworkCheckedException;
}