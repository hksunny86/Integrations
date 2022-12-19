package com.inov8.microbank.server.facade.portal.mfsaccountmodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.integration.common.model.AccountModel;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.customermodule.BlinkCustomerPictureModel;
import com.inov8.microbank.common.model.customermodule.CustomerPictureModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.ApplicantDetailModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.BusinessDetailModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.MfsAccountModel;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.vo.account.BulkCustomerAccountVo;
import com.inov8.microbank.server.facade.CreditAccountQueingPreProcessor;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import com.inov8.microbank.server.webserviceclient.ivr.IvrRequestDTO;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;

public class MfsAccountFacadeImpl implements MfsAccountFacade {

    private FrameworkExceptionTranslator frameworkExceptionTranslator;
    private MfsAccountManager mfsAccountManager;
    private CreditAccountQueingPreProcessor creditAccountQueingPreProcessor;

    public BaseWrapper activateDeactivateMfsAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        try {
            return mfsAccountManager.activateDeactivateMfsAccount(baseWrapper);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp,
                    FrameworkExceptionTranslator.UPDATE_ACTION);
        }
    }

    public BaseWrapper createMfsAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        // TODO Auto-generated method stub
        try {
            return this.mfsAccountManager.createMfsAccount(baseWrapper);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp,
                    FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
        }
    }

    public SearchBaseWrapper searchUserInfo(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        // TODO Auto-generated method stub
        try {
            return mfsAccountManager.searchUserInfo(searchBaseWrapper);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp,
                    FrameworkExceptionTranslator.FIND_ACTION);
        }
    }

    @Override
    public SearchBaseWrapper searchMinorUserInfo(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        // TODO Auto-generated method stub
        try {
            return mfsAccountManager.searchMinorUserInfo(searchBaseWrapper);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp,
                    FrameworkExceptionTranslator.FIND_ACTION);
        }
    }

    public BaseWrapper updateMfsAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        // TODO Auto-generated method stub
        try {
            baseWrapper = mfsAccountManager.updateMfsAccount(baseWrapper);
            String transactionId = (String) baseWrapper.getObject(PortalConstants.ACC_OPENING_COMM_TRANSACTION_ID);
            if (!StringUtil.isNullOrEmpty(transactionId)) {
                creditAccountQueingPreProcessor.loadAndForwardAccountToQueue(transactionId);

            }
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp,
                    FrameworkExceptionTranslator.UPDATE_ACTION);
        }
        return baseWrapper;
    }

    @Override
    public void saveClsPendignAccount(BaseWrapper baseWrapper, I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO) throws FrameworkCheckedException {
        mfsAccountManager.saveClsPendignAccount(baseWrapper, i8SBSwitchControllerResponseVO);
    }


    @Override
    public void accountBlock(BaseWrapper baseWrapper) {
        mfsAccountManager.accountBlock(baseWrapper);
    }

    @Override
    public BaseWrapper updateMfsDebitCard(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        // TODO Auto-generated method stub
        try {
            baseWrapper = mfsAccountManager.updateMfsDebitCard(baseWrapper);

        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp,
                    FrameworkExceptionTranslator.UPDATE_ACTION);
        }
        return baseWrapper;
    }

    public void setFrameworkExceptionTranslator(
            FrameworkExceptionTranslator frameworkExceptionTranslator) {
        this.frameworkExceptionTranslator = frameworkExceptionTranslator;
    }

    public void setMfsAccountManager(MfsAccountManager mfsAccountManager) {
        this.mfsAccountManager = mfsAccountManager;
    }

    public BaseWrapper searchAppUserByPrimaryKey(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        try {
            return mfsAccountManager.searchAppUserByPrimaryKey(baseWrapper);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp,
                    FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
        }
    }

    public void logPrintAction(Long customerId) throws FrameworkCheckedException {
        try {
            mfsAccountManager.logPrintAction(customerId);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp,
                    FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
        }
    }

    public BaseWrapper searchUserInfoByPrimaryKey(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        try {
            return mfsAccountManager.searchUserInfoByPrimaryKey(baseWrapper);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp,
                    FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
        }
    }

    public BaseWrapper deactivateMfsAccountThroughSMS(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        // TODO Auto-generated method stub
        return null;
    }

    public BaseWrapper reactivateMfsAccountThroughSMS(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        // TODO Auto-generated method stub
        return null;
    }

    public BaseWrapper createMfsAccountThroughSMS(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        // TODO Auto-generated method stub
        return null;
    }

    public OLAVO getAccountInfoFromOLA(String cnic, Long bankId) throws FrameworkCheckedException {
        try {
            return mfsAccountManager.getAccountInfoFromOLA(cnic, bankId);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp,
                    FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
        }
    }

    public SmartMoneyAccountModel getSmartMoneyAccountByCustomerId(Long customerId)
            throws FrameworkCheckedException {

        try {
            return mfsAccountManager.getSmartMoneyAccountByCustomerId(customerId);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp,
                    FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
        }
    }

    @Override
    public List<SmartMoneyAccountModel> getLastClosedSMAAccount(SmartMoneyAccountModel smartMoneyAccountModel) {
        return mfsAccountManager.getLastClosedSMAAccount(smartMoneyAccountModel);
    }

    @Override
    public AccountModel getLastClosedAccountModel(String cnic, Long customerAccountTypeId) {
        return mfsAccountManager.getLastClosedAccountModel(cnic, customerAccountTypeId);
    }

    @Override
    public AccountModel getAccountModelByCnicAndCustomerAccountTypeAndStatusId(String cnic, Long customerAccountTypeId, Long statusId) {
        return mfsAccountManager.getAccountModelByCnicAndCustomerAccountTypeAndStatusId(cnic, customerAccountTypeId, statusId);
    }

    @Override
    public SmartMoneyAccountModel getSmartMoneyAccountByExample(SmartMoneyAccountModel _smartMoneyAccountModel) throws FrameworkCheckedException {
        return mfsAccountManager.getSmartMoneyAccountByExample(_smartMoneyAccountModel);
    }

    public UserDeviceAccountsModel getDeviceAccountByAppUserId(Long appUserId, Long deviceTypeId)
            throws FrameworkCheckedException {
        try {
            return mfsAccountManager.getDeviceAccountByAppUserId(appUserId, deviceTypeId);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp,
                    FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
        }
    }

    /* (non-Javadoc)
     * @see com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager#getAppUserModel(com.inov8.microbank.common.model.AppUserModel)
     */
    public AppUserModel getAppUserModel(AppUserModel exampleInstance) throws FrameworkCheckedException {
        try {
            return mfsAccountManager.getAppUserModel(exampleInstance);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp,
                    FrameworkExceptionTranslator.FIND_ACTION);
        }
    }

    public Boolean updateWalkinCustomerIfExists(String walkInCNIC, String walkInMobileNumber, String senderMobileNumber) throws FrameworkCheckedException {

        return mfsAccountManager.updateWalkinCustomerIfExists(walkInCNIC, walkInMobileNumber, senderMobileNumber);
    }

    public BaseWrapper createWalkinCustomerAccount(String walkInCnic, String walkInMobileNumber, String senderMobileNumber) throws FrameworkCheckedException {
        return mfsAccountManager.createWalkinCustomerAccount(walkInCnic, walkInMobileNumber, senderMobileNumber);
    }

    @Override
    public BaseWrapper createWalkinCustomerAccount(String walkInCnic, String walkInMobileNumber, AppUserModel createdByAppUserModel) throws FrameworkCheckedException {
        return mfsAccountManager.createWalkinCustomerAccount(walkInCnic, walkInMobileNumber, createdByAppUserModel);
    }

    public WalkinCustomerModel getWalkinCustomerModel(WalkinCustomerModel _walkinCustomerModel) {

        return mfsAccountManager.getWalkinCustomerModel(_walkinCustomerModel);
    }

    public SmartMoneyAccountModel getSmartMoneyAccount(SmartMoneyAccountModel _smartMoneyAccountModel) throws FrameworkCheckedException {

        return mfsAccountManager.getSmartMoneyAccount(_smartMoneyAccountModel);
    }


    public List<BulkDisbursementsModel> validateMobileNos(List<BulkDisbursementsModel> bulkList)
            throws FrameworkCheckedException {
        try {
            return mfsAccountManager.validateMobileNos(bulkList);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp,
                    FrameworkExceptionTranslator.FIND_ACTION);
        }
    }

    @Override
    public List<BulkDisbursementsModel> isAlreadyRegistered(List<BulkDisbursementsModel> bulkList) throws FrameworkCheckedException {
        try {
            return mfsAccountManager.isAlreadyRegistered(bulkList);
        } catch (Exception ex) {
            throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_ACTION);
        }
    }

    @Override
    public List<BulkCustomerAccountVo> isCustomerOrAgent(List<BulkCustomerAccountVo> bulkCustomerAccountVoList) throws FrameworkCheckedException {
        try {
            return mfsAccountManager.isCustomerOrAgent(bulkCustomerAccountVoList);
        } catch (Exception ex) {
            throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_ACTION);
        }
    }

    @Override
    public void enqueueBulkCustomerAccountsCreation(List<BulkCustomerAccountVo> bulkCustomerAccountVoList) throws FrameworkCheckedException {
        try {
            mfsAccountManager.enqueueBulkCustomerAccountsCreation(bulkCustomerAccountVoList);
        } catch (Exception ex) {
            throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.INSERT_ACTION);
        }
    }

    public Boolean isAlreadyRegistered(String CNIC, String mobileNumber) throws FrameworkCheckedException {

        return mfsAccountManager.isAlreadyRegistered(CNIC, mobileNumber);
    }

    public SearchBaseWrapper loadAppUserByMobileNumberAndType(SearchBaseWrapper searchBaseWrapper) {

        return mfsAccountManager.loadAppUserByMobileNumberAndType(searchBaseWrapper);
    }

    @Override
    public SearchBaseWrapper loadAppUserByMobileNumberAndTypeHQL(SearchBaseWrapper searchBaseWrapper) {
        return mfsAccountManager.loadAppUserByMobileNumberAndTypeHQL(searchBaseWrapper);
    }

    @Override
    public AppUserModel getAppUserModelByPrimaryKey(Long appUserId) {
        return mfsAccountManager.getAppUserModelByPrimaryKey(appUserId);
    }

    @Override
    public AppUserModel getAppUserModelByMobileNumber(String mobileNo) {
        return mfsAccountManager.getAppUserModelByMobileNumber(mobileNo);
    }

    @Override
    public String getAreaByAppUserId(Long appUserId) {
        return mfsAccountManager.getAreaByAppUserId(appUserId);
    }

    @Override
    public BaseWrapper updateAppUserModel(BaseWrapper baseWrapper)
            throws FrameworkCheckedException {
        return mfsAccountManager.updateAppUserModel(baseWrapper);

    }

    @Override
    public double getMfsAccountBalance(Long appUserId, Long paymentModeId) throws Exception {
        return mfsAccountManager.getMfsAccountBalance(appUserId, paymentModeId);
    }

    /***************************************************************************************************************************
     * Added by Soofia
     */
    @Override
    public CustomerPictureModel getCustomerPictureByTypeId(Long pictureTypeId, Long customerId) throws FrameworkCheckedException {
        try {
            return mfsAccountManager.getCustomerPictureByTypeId(pictureTypeId, customerId);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp,
                    FrameworkExceptionTranslator.FIND_ACTION);
        }
    }

    @Override
    public CustomerPictureModel getCustomerPictureByTypeIdAndStatus(Long pictureTypeId, Long customerId) throws FrameworkCheckedException {
        try {
            return mfsAccountManager.getCustomerPictureByTypeIdAndStatus(pictureTypeId, customerId);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp,
                    FrameworkExceptionTranslator.FIND_ACTION);
        }
    }

    @Override
    public BlinkCustomerPictureModel getBlinkCustomerPictureByTypeId(Long pictureTypeId, Long customerId) throws FrameworkCheckedException {
        try {
            return mfsAccountManager.getBlinkCustomerPictureByTypeId(pictureTypeId, customerId);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp,
                    FrameworkExceptionTranslator.FIND_ACTION);
        }
    }

    @Override
    public SearchBaseWrapper searchCustomerPictures(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        try {
            return mfsAccountManager.searchCustomerPictures(searchBaseWrapper);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp, FrameworkExceptionTranslator.FIND_ACTION);
        }
    }

    @Override
    public List<CustomerPictureModel> getAllCustomerPictures(Long customerId)
            throws FrameworkCheckedException {
        try {
            return mfsAccountManager.getAllCustomerPictures(customerId);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp,
                    FrameworkExceptionTranslator.FIND_ACTION);
        }
    }

    /***************************************************************************************************************************/

    @Override
    public VeriflyBaseWrapper changePIN(VeriflyBaseWrapper wrapper)
            throws Exception {
        return mfsAccountManager.changePIN(wrapper);
    }

    @Override
    public BaseWrapper createLevel2Account(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        try {
            return mfsAccountManager.createLevel2Account(baseWrapper);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp,
                    FrameworkExceptionTranslator.INSERT_ACTION);
        }
    }

    @Override
    public BaseWrapper updateLevel2Account(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        try {
            return mfsAccountManager.updateLevel2Account(baseWrapper);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp,
                    FrameworkExceptionTranslator.UPDATE_ACTION);
        }
    }

    @Override
    public ApplicantDetailModel loadApplicantDetail(ApplicantDetailModel model) throws FrameworkCheckedException {
        try {
            return mfsAccountManager.loadApplicantDetail(model);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp,
                    FrameworkExceptionTranslator.FIND_ACTION);
        }
    }

    @Override
    public BusinessDetailModel loadBusinessDetail(BusinessDetailModel model) throws FrameworkCheckedException {
        try {
            return mfsAccountManager.loadBusinessDetail(model);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp,
                    FrameworkExceptionTranslator.FIND_ACTION);
        }
    }

    @Override
    public List<OlaCustomerAccountTypeModel> loadCustomerACTypes(Long[] typesId) throws FrameworkCheckedException {
        try {
            return mfsAccountManager.loadCustomerACTypes(typesId);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp,
                    FrameworkExceptionTranslator.FIND_ACTION);
        }
    }

    @Override
    public void makeInitialDeposit(MfsAccountModel mfsAccountModel) throws CommandException {
        try {
            mfsAccountManager.makeInitialDeposit(mfsAccountModel);
        } catch (Exception exp) {
            exp.printStackTrace();
        }

    }

    @Override
    public void verifyAgentBalanceForInitialDeposit(double initialDeposit) throws Exception {
        mfsAccountManager.verifyAgentBalanceForInitialDeposit(initialDeposit);
    }

    @Override
    public ApplicantDetailModel loadApplicantDetailById(Long applicantDetailId)
            throws FrameworkCheckedException {
        try {
            return this.mfsAccountManager.loadApplicantDetailById(applicantDetailId);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp,
                    FrameworkExceptionTranslator.FIND_ACTION);
        }
    }

    @Override
    public List<ApplicantDetailModel> loadApplicantDetails(
            ApplicantDetailModel model) throws FrameworkCheckedException {
        try {
            return this.mfsAccountManager.loadApplicantDetails(model);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp,
                    FrameworkExceptionTranslator.FIND_ACTION);
        }
    }

    @Override
    public void validateUniqueness(MfsAccountModel mfsAccountModel)
            throws FrameworkCheckedException {
        try {
            this.mfsAccountManager.validateUniqueness(mfsAccountModel);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp,
                    FrameworkExceptionTranslator.FIND_ACTION);
        }
    }

    @Override
    public String computeMfsId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BaseWrapper createHandlerAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException {

        return mfsAccountManager.createHandlerAccount(baseWrapper);
    }

    @Override
    public BaseWrapper closeHandlerAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException {

        return mfsAccountManager.closeHandlerAccount(baseWrapper);
    }


    @Override
    public String[] getFundSourceName(String[] fundSourceIds) throws FrameworkCheckedException {
        try {
            return this.mfsAccountManager.getFundSourceName(fundSourceIds);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp,
                    FrameworkExceptionTranslator.FIND_ACTION);
        }
    }


    @Override
    public void initiateUserGeneratedPinIvrCall(IvrRequestDTO ivrDTO)
            throws FrameworkCheckedException {
        try {
            this.mfsAccountManager.initiateUserGeneratedPinIvrCall(ivrDTO);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp,
                    FrameworkExceptionTranslator.FIND_ACTION);
        }

    }

    @Override
    public BaseWrapper createKYC(BaseWrapper baseWrapper)
            throws FrameworkCheckedException {
        return this.mfsAccountManager.createKYC(baseWrapper);
    }

    @Override
    public BaseWrapper createAgentMerchant(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        try {
            return this.mfsAccountManager.createAgentMerchant(baseWrapper);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp, FrameworkExceptionTranslator.INSERT_ACTION);
        }
    }

    @Override
    public Boolean isAlreadyExistInitAppFormNumber(String initialApplicationFormNo) throws FrameworkCheckedException {
        try {
            return this.mfsAccountManager.isAlreadyExistInitAppFormNumber(initialApplicationFormNo);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp, FrameworkExceptionTranslator.FIND_ACTION);
        }
    }

    @Override
    public Boolean isAlreadyExistReferenceNumber(String referenceNo, String appFormNo) throws FrameworkCheckedException {

        try {
            return this.mfsAccountManager.isAlreadyExistReferenceNumber(referenceNo, appFormNo);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp, FrameworkExceptionTranslator.FIND_ACTION);
        }
    }

    @Override
    public BaseWrapper findAgentMerchantByInitialAppFormNo(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        try {
            return this.mfsAccountManager.findAgentMerchantByInitialAppFormNo(baseWrapper);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp, FrameworkExceptionTranslator.FIND_ACTION);
        }
    }

    @Override
    public BaseWrapper updateAgentMerchant(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        try {
            return this.mfsAccountManager.updateAgentMerchant(baseWrapper);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp, FrameworkExceptionTranslator.UPDATE_ACTION);
        }
    }

    @Override
    public List<KYCModel> findKycModel(SearchBaseWrapper baseWrapper) throws FrameworkCheckedException {
        try {
            return this.mfsAccountManager.findKycModel(baseWrapper);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp,
                    FrameworkExceptionTranslator.FIND_ACTION);
        }
    }

    @Override
    public List<ACOwnershipDetailModel> loadAccountOwnerShipDetails(
            SearchBaseWrapper baseWrapper) throws FrameworkCheckedException {
        try {
            return this.mfsAccountManager.loadAccountOwnerShipDetails(baseWrapper);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp,
                    FrameworkExceptionTranslator.FIND_ACTION);
        }
    }

    @Override
    public List<ApplicantTxModeModel> loadApplicantTxModeModelByInitialApplicationFormNo(
            String formNumber) throws FrameworkCheckedException {
        try {
            return this.mfsAccountManager.loadApplicantTxModeModelByInitialApplicationFormNo(formNumber);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp,
                    FrameworkExceptionTranslator.FIND_ACTION);
        }
    }

    @Override
    public BaseWrapper updateKYC(BaseWrapper baseWrapper)
            throws FrameworkCheckedException {
        return this.mfsAccountManager.updateKYC(baseWrapper);
    }

    @Override
    public BaseWrapper searchCustomerByInitialAppFormNo(BaseWrapper wrapper)
            throws FrameworkCheckedException {
        return this.mfsAccountManager.searchCustomerByInitialAppFormNo(wrapper);
    }

    @Override
    public SearchBaseWrapper searchAgentMerchant(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        try {
            return this.mfsAccountManager.searchAgentMerchant(searchBaseWrapper);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp, FrameworkExceptionTranslator.FIND_ACTION);
        }
    }

    @Override
    public BaseWrapper loadCustomer(BaseWrapper wrapper)
            throws FrameworkCheckedException {
        try {
            return this.mfsAccountManager.loadCustomer(wrapper);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp, FrameworkExceptionTranslator.FIND_ACTION);
        }
    }

    @Override
    public SearchBaseWrapper searchKYC(SearchBaseWrapper baseWrapper)
            throws FrameworkCheckedException {
        try {
            return this.mfsAccountManager.searchKYC(baseWrapper);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp, FrameworkExceptionTranslator.FIND_ACTION);
        }
    }

    @Override
    public List<ACOwnershipDetailModel> loadL3AccountOwnerShipDetails(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        try {
            return this.mfsAccountManager.loadL3AccountOwnerShipDetails(searchBaseWrapper);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp, FrameworkExceptionTranslator.FIND_ACTION);
        }
    }

    @Override
    public void isUniqueCNICMobile(AppUserModel appUserModel, BaseWrapper baseWrapper) throws FrameworkCheckedException {

        mfsAccountManager.isUniqueCNICMobile(appUserModel, baseWrapper);
    }

    @Override
    public AgentMerchantDetailModel loadAgentMerchantDetailModel(
            Long agentMerchantDetailId) throws FrameworkCheckedException {
        return mfsAccountManager.loadAgentMerchantDetailModel(agentMerchantDetailId);
    }

    @Override
    public KYCModel findKycByPrimaryKey(Long kycId)
            throws FrameworkCheckedException {
        try {
            return this.mfsAccountManager.findKycByPrimaryKey(kycId);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp, FrameworkExceptionTranslator.FIND_ACTION);
        }
    }

    @Override
    public boolean isAppUserExist(String userName) throws FrameworkCheckedException {
        try {
            return this.mfsAccountManager.isAppUserExist(userName);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp, FrameworkExceptionTranslator.FIND_ACTION);
        }
    }

    @Override
    public boolean isAppUserUsernameUniqueForUpdate(String userName, String initAppFormNo) throws FrameworkCheckedException {
        try {
            return this.mfsAccountManager.isAppUserUsernameUniqueForUpdate(userName, initAppFormNo);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp, FrameworkExceptionTranslator.FIND_ACTION);
        }
    }

    @Override
    public AgentMerchantDetailModel loadAgentMerchantDetailModelByUserName(String userName) throws FrameworkCheckedException {
        try {
            return this.mfsAccountManager.loadAgentMerchantDetailModelByUserName(userName);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp, FrameworkExceptionTranslator.FIND_ACTION);
        }
    }

    @Override
    public CityModel loadCityModel(Long cityId)
            throws FrameworkCheckedException {
        try {
            return this.mfsAccountManager.loadCityModel(cityId);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp, FrameworkExceptionTranslator.FIND_ACTION);
        }
    }

    @Override
    public OccupationModel loadOccupationModel(Long occupationId)
            throws FrameworkCheckedException {
        try {
            return this.mfsAccountManager.loadOccupationModel(occupationId);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp, FrameworkExceptionTranslator.FIND_ACTION);
        }
    }

    @Override
    public ProfessionModel loadProfessionModel(Long professionId)
            throws FrameworkCheckedException {
        try {
            return this.mfsAccountManager.loadProfessionModel(professionId);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp, FrameworkExceptionTranslator.FIND_ACTION);
        }
    }

    @Override
    public BankModel getOlaBankMadal() {
        return this.mfsAccountManager.getOlaBankMadal();
    }

    @Override
    public Boolean isAlreadyRegistered(String CNIC) throws FrameworkCheckedException {
        try {
            return this.mfsAccountManager.isAlreadyRegistered(CNIC);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp, FrameworkExceptionTranslator.FIND_ACTION);
        }
    }

    @Override
    public void saveRootedMobileHistory(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        try {
            this.mfsAccountManager.saveRootedMobileHistory(baseWrapper);
        } catch (Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp, FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
        }
    }

    public void setCreditAccountQueingPreProcessor(CreditAccountQueingPreProcessor creditAccountQueingPreProcessor) {
        this.creditAccountQueingPreProcessor = creditAccountQueingPreProcessor;
    }

    @Override
    public int validateCNIC(String input, String mobile) throws Exception {
        return mfsAccountManager.validateCNIC(input, mobile);
    }

    @Override
    public int validateMobileNo(String mobileNo, Long appUserTypeId) throws Exception {
        return mfsAccountManager.validateMobileNo(mobileNo, appUserTypeId);
    }

    @Override
    public String settleAccountOpeningCommission(Long customerId)
            throws FrameworkCheckedException {

        return mfsAccountManager.settleAccountOpeningCommission(customerId);
    }

    @Override
    public void saveBulkAgentData(List<BulkAgentDataHolderModel> list) throws FrameworkCheckedException {
        mfsAccountManager.saveBulkAgentData(list);
    }

}
