package com.inov8.microbank.server.dao.securitymodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.account.vo.BlacklistMarkingBulkUploadVo;
import com.inov8.microbank.account.vo.CustomerACNatureMarkingUploadVo;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.AppUserPasswordHistoryModel;
import com.inov8.microbank.common.model.BulkDisbursementsModel;
import com.inov8.microbank.common.model.retailermodule.HandlerSearchViewModel;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface AppUserDAO extends BaseDAO<AppUserModel, Long> {
	public AppUserModel verifyL3Agent(Long retailerContactId);
	public List<AppUserModel> findAppUsersByType(Long appUserType);
	public AppUserModel getAppUserWithRegistrationState(String mobileNo, String cnic, Long registrationState);
	public AppUserModel findByRetailerContractId(long retailerContactId) throws FrameworkCheckedException;
	public AppUserModel loadAppUserByCnicAndType(String cnic);
	public AppUserModel loadAppUserByEmailAddress(String emailAddress);
	public AppUserModel loadAppUserByCnicAndMobileAndAppUserType(String mobileNo, String cnic, Long appUserTypeId);
	public AppUserModel loadAppUserByMobileAndType(String mobileNo);
	public AppUserModel loadAppUserByMobileAndTypeForCustomer(String mobileNo);
	public AppUserModel loadWalkinCustomerAppUserByCnic(String cnic);
	public AppUserModel closeHandlerAccount(final AppUserModel handlerAppUserModel) throws FrameworkCheckedException;
	public void checkHandlerPendingTransactions(List<HandlerSearchViewModel> handlerSearchViewModelList) throws FrameworkCheckedException;
	public abstract AppUserModel loadAppUserModelByCustomerId(final long customerId);
	public List<AppUserModel> loadAppUserMarkAmaAccountDebitBlock() throws FrameworkCheckedException;
	public List<AppUserModel> loadPendingAccountOpeningAppUser() throws FrameworkCheckedException;

	List<BlacklistMarkingBulkUploadVo> findAppUsersByNICs(List<String> nics) throws FrameworkCheckedException;
	List<CustomerACNatureMarkingUploadVo> findAppUsersByCnics(List<String> cnics) throws FrameworkCheckedException;

	/**
	 * Gets users information based on user id.
	 * 
	 * @param userId
	 *            the user's id
	 * @return user populated user object
	 */
	public AppUserModel getUser(Long userId);

	/**
	 * Gets users information based on login name.
	 * 
	 * @param username
	 *            the user's username
	 * @return userDetails populated userDetails object
	 */
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

	/**
	 * Gets a list of users based on parameters passed in.
	 * 
	 * @return List populated list of users
	 */
	public List getUsers(AppUserModel user);

	/**
	 * Saves a user's information
	 * 
	 * @param user
	 *            the object to be saved
	 */
	public void saveUser(AppUserModel user);

	/**
	 * Removes a user from the database by id
	 * 
	 * @param userId
	 *            the user's id
	 */
	public void removeUser(Long userId);

	Boolean isAlreadyRegistered(AppUserModel appUserModel);

	/**
	 * 
	 * @param mobileNo
	 * @param appUserTypeId
	 * @return AppUserModel
	 */
	public AppUserModel loadAppUserByQuery(final String mobileNo, final long appUserTypeId);
	public AppUserModel loadAppUserByMobileByQuery(final String mobileNo);
	public AppUserModel loadAppUserByCNIC(String cnic);
	public AppUserModel loadHeadRetailerContactAppUser(Long retailerId);
	public AppUserModel closeAgnetAccount(final AppUserModel user) throws FrameworkCheckedException;
	public AppUserModel closeCustomerAccount(final AppUserModel user) throws FrameworkCheckedException;
    public Long loadSegmentIdByMobileNo(String mobileNo);
    public Long loadCustomerAccountTypeByMobileNo(String mobileNo);
	List<BulkDisbursementsModel> isAlreadyRegistered(List<BulkDisbursementsModel> bulkDisbursementsModelList, Long... appUserTypeIds);
	boolean isAgentOrCustomer(String mobileNo) throws FrameworkCheckedException;



	public List <String> getPreviousThreePasswordsByAppUserId(String username);
	public void saveAppUserPasswordHistory( AppUserPasswordHistoryModel appUserPasswordHistory );

	
	
	public List <AppUserModel> getAllAppUsersForPasswordChangeRequired( );
	public List <AppUserModel> getAllAppUsersForAccountInactive ( );
	public List<AppUserModel> findAppUsersByAppUserIds(List<Long> inClauseOfAppUserIds)
			throws FrameworkCheckedException;
	public void updateAccountWithAccountTypeId(List<String> cnicList,
			Long accountTypeId) throws FrameworkCheckedException;
	public List<String> findCNICsByAppUserIds(List<Long> appUserIds) throws FrameworkCheckedException;
	public List<Object[]> getCNICExpiryAlertUsers() throws FrameworkCheckedException;
	public Long getAppUserRetailerContactId(Long appUserId) throws FrameworkCheckedException;
	public Long getAppUserTypeId(Long appUserId) throws FrameworkCheckedException;
	public boolean isEmployeeIdUnique(AppUserModel appUserModel);
	public AppUserModel checkAppUserTypeAsWalkinCustoemr(String mobileNo);
	public List<AppUserModel> getAppUserModelByRegionId(Long regionId);
	public AppUserModel loadAppUserByHandlerByQuery(final Long handlerId);
	public AppUserModel loadAppUserByUserName(String userName);
	public boolean isMobileNumUnique(AppUserModel appUserModel);
	public boolean isUserIdUnique(AppUserModel appUserModel);
	boolean isCnicUnique(AppUserModel appUserModel);
	List<AppUserModel> findAppUserByCnicAndMobile(String mobileNo, String cnic);
	public List< Object [] > getCNICsToMarkAccountDormant() throws FrameworkCheckedException;
	public List<AppUserModel> getAppUserModelListByCNICs(List<String> cnicList) throws FrameworkCheckedException;
	public AppUserModel loadAppUserByMobileAndType(String mobileNo, Long ...apUserTypes) throws FrameworkCheckedException;
	public List<AppUserModel> getAppUsersByTypeForHsmCall(Long appUserTypeId, String strDate1, String strDate2, int limit) throws FrameworkCheckedException;
	AppUserModel loadAppUserModelByUserId(String userId);
	List<Object[]> loadUserByCNIC(String cnic, String mobile);
	List<Object[]> loadUserByMobileNo(String mobile, Long appUserTypeId);
	public AppUserModel loadRetailerAppUserModelByAppUserId(Long appUserId) throws FrameworkCheckedException;
	AppUserModel loadAppUserByCNICAndType(String cnic, Long... appUserTypes) throws FrameworkCheckedException;
	public boolean isUserFiler(Long appUserId);
	public String getCustomerId(String cnic) throws FrameworkCheckedException;

	public AppUserModel getAppUserWithRegistrationStates(String mobileNo, String cnic, Long ...registrationStates);
}
