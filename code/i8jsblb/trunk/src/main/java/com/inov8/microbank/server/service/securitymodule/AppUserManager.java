package com.inov8.microbank.server.service.securitymodule;

import java.util.Date;
import java.util.List;

import com.inov8.microbank.common.model.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.integration.common.model.AccountModel;
import com.inov8.microbank.common.exception.UserExistsException;
import com.inov8.microbank.common.model.retailermodule.HandlerSearchViewModel;

public interface AppUserManager {
    /**
     * Retrieves a user by userId.  An exception is thrown if user not found
     *
     * @param userId
     * @return User
     */

    public AppUserModel fetchL3Agent(Long retailerContactId) throws FrameworkCheckedException;

    public AppUserModel getUser(String userId);

    public SearchBaseWrapper findAppUsersByAppUserTypeId(long appUserTypeId) throws FrameworkCheckedException;

    public AppUserModel getAppUserWithRegistrationState(String mobileNo, String cnic, Long registrationState) throws FrameworkCheckedException;

    public AppUserModel loadAppUserByCnicAndType(String cnic) throws FrameworkCheckedException;

    public AppUserModel loadAppUserByEmail(String emailAddress) throws FrameworkCheckedException;

    public AppUserModel loadAppUserByCnicAndMobileAndAppUserType(String cnic, String mobileNo, Long appUserTypeId) throws FrameworkCheckedException;

    public AppUserModel loadAppUserByMobileAndType(String mobileNo) throws FrameworkCheckedException;

    public AppUserModel loadAppUserByMobileAndTypeForCustomer(String mobileNo) throws FrameworkCheckedException;

    public AppUserModel loadAppUserByQuery(String mobileNo, long appUserTypeId) throws FrameworkCheckedException;

    public AppUserModel loadAppUserByMobileByQuery(final String mobileNo) throws FrameworkCheckedException;

    public AppUserModel loadWalkinCustomerAppUserByCnic(String cnic) throws FrameworkCheckedException;

    public BaseWrapper closeHandlerAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException;

    public AppUserModel loadAppUser(Long primaryKey);

    /**
     * Finds a user by their username.
     *
     * @param username
     * @return User a populated user object
     */
    public AppUserModel getUserByUsername(String username) throws
            UsernameNotFoundException;

    /**
     * Retrieves a list of users, filtering with parameters on a user object
     *
     * @param user parameters to filter on
     * @return List
     */
    public List getUsers(AppUserModel user);

    /**
     * Saves a user's information
     *
     * @param user the user's information
     * @throws UserExistsException
     */
    public void saveUser(AppUserModel user) throws UserExistsException;

    public BaseWrapper searchCustomerByUser(BaseWrapper wrapper) throws
            FrameworkCheckedException;

    /**
     * Removes a user from the database by their userId
     *
     * @param userId the user's id
     */
    public void removeUser(String userId);

    SearchBaseWrapper searchAppUserByMobile(SearchBaseWrapper searchBaseWrapper) throws
            FrameworkCheckedException;

    BaseWrapper loadAppUser(BaseWrapper searchBaseWrapper) throws
            FrameworkCheckedException;

    BaseWrapper searchRetailerByMobile(BaseWrapper wrapper) throws FrameworkCheckedException;

    public BaseWrapper saveOrUpdateAppUser(BaseWrapper baseWrapper) throws FrameworkCheckedException;

    public BaseWrapper searchAppUser(SearchBaseWrapper searchBaseWrapper) throws
            FrameworkCheckedException;

    public SearchBaseWrapper searchAppUserForm(SearchBaseWrapper searchBaseWrapper) throws
            FrameworkCheckedException;

    public boolean isMobileNumberCNICUnique(BaseWrapper baseWrapper) throws FrameworkCheckedException;

    public AppUserModel getAppUserModel(AppUserModel example);

    public AppUserModel loadAppUserByCNIC(String cnic) throws FrameworkCheckedException;

    public BaseWrapper closeAgentAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException;

    public SearchBaseWrapper loadMobileHistory(SearchBaseWrapper wrapper) throws FrameworkCheckedException;

    public BaseWrapper updateMobileNo(BaseWrapper wrapper) throws FrameworkCheckedException;

    public List<String> getAppUserPreviousThreePasswordsByAppUserId(
            String username) throws FrameworkCheckedException;


    public void saveAppUserPasswordHistory(AppUserPasswordHistoryModel appUserPasswordHistory)
            throws FrameworkCheckedException;


    public List<AppUserModel> getAllUsersForPasswordChangeRequired() throws FrameworkCheckedException;


    public Long markPasswordChangeRequired(List<AppUserModel> users) throws FrameworkCheckedException;


    public List<AppUserModel> getAllUsersForAccountInactive()
            throws FrameworkCheckedException;


    public Long markAppUserAccountInactive(List<AppUserModel> users) throws FrameworkCheckedException;

    public void updateCustomerFirstDebitCredit(CustomerModel customerModel, Long registerState, boolean isDebit, Boolean ivrResponse) throws FrameworkCheckedException;

    public List<CustomerModel> findCustomersByAppUserIds(List<Long> appUserIdsInClause)
            throws FrameworkCheckedException;

    public void updateCustomersWithAttribute(String attribue, String value,
                                             List<CustomerModel> customers) throws FrameworkCheckedException;

    public List<AppUserModel> findAppUsersByAppUserIds(List<Long> appUserIdsInClause)
            throws FrameworkCheckedException;

    public void updateAccountWithAccountTypeId(List<Long> appUserIds, Long accountTypeId)
            throws FrameworkCheckedException;

    public List<AppUserModel> getCNICExpiryAlertUsers()
            throws FrameworkCheckedException;

    public void markCnicExpiryReminderSent(List<AppUserModel> appUserModelList)
            throws FrameworkCheckedException;

    public Long getAppUserTypeId(Long appUserId) throws FrameworkCheckedException;

    public SearchBaseWrapper searchAppUserHistoryView(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

    public List<AppUserModel> getAppUserModelByRegionId(Long regionId);

    public AppUserModel checkAppUserTypeAsWalkinCustoemr(String mobileNo) throws FrameworkCheckedException;

    public void checkHandlerPendingTransactions(List<HandlerSearchViewModel> handlerSearchViewModelList)
            throws FrameworkCheckedException;

    public List<Object[]> getAppUserCNICsToMarkDormant()
            throws FrameworkCheckedException;

    public List<AppUserModel> markAppUserDormant(List<AccountModel> accountModelList) throws FrameworkCheckedException;

    public AppUserModel loadAppUserByMobileAndType(String mobileNo, Long... appUserTypes) throws FrameworkCheckedException;

    public List<AppUserModel> oneTimeHsmPinGenerator(List<AppUserModel> appUserModels, Long appUserTypeId)
            throws FrameworkCheckedException;

    public List<AppUserModel> getAppUserOneTimeByTypeForHsmCall(Long appUserTypeId, String strDate1, String strDate2, int limit) throws FrameworkCheckedException;

    public BaseWrapper updateCNIC(BaseWrapper wrapper) throws FrameworkCheckedException;

    public void checkIsCustomerPINGenerated(CustomerModel customerModel);

    public AppUserModel loadAppUserModelByUserId(String userId) throws FrameworkCheckedException;

    public String getStatusDetails(Long appUserId, Date updationDate, Boolean accLocked, Boolean accEnabled) throws FrameworkCheckedException;

    public AppUserModel findByRetailerContractId(long retailerContactId) throws FrameworkCheckedException;

    public CustomerModel getCustomerModelByPK(Long customerId);

    public AppUserModel loadRetailerAppUserModelByAppUserId(Long appUserId) throws FrameworkCheckedException;

    public boolean isAppUserFiler(Long appUserId);

    public void updateAppUserWithAuthorization(BaseWrapper baseWrapper) throws FrameworkCheckedException;

    public String getCustomerId(String cnic) throws FrameworkCheckedException;

    public void saveOrUpdateAppUserModels(List<AppUserModel> appUserModels) throws FrameworkCheckedException;

    RetailerContactModel getRetailerContactModelById(Long retailerContactId) throws FrameworkCheckedException;

    RetailerModel getRetailerModelByRetailerIdForTrx(Long retailerId) throws FrameworkCheckedException;

    DistributorModel findDistributorModelByIdForTrx(Long distributorId) throws FrameworkCheckedException;

    MnoUserModel findMnoUserModelByPrimaryKey(Long mnoUserId) throws FrameworkCheckedException;

    public abstract AppUserModel loadAppUserModelByCustomerId(Long customerId);

    public List<AppUserModel> loadAppUserMarkAmaAccountDebitBlock() throws FrameworkCheckedException;

    public List<AppUserModel> loadPendingAccountOpeningAppUserModel() throws FrameworkCheckedException;

    public AppUserModel getAppUserWithRegistrationStates(String mobileNo, String cnic, Long ...registrationStates) throws FrameworkCheckedException;

}
