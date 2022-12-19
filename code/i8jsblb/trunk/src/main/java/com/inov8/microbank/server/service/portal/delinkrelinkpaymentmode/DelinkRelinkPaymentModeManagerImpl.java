package com.inov8.microbank.server.service.portal.delinkrelinkpaymentmode;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.exception.ImplementationNotSupportedException;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.AllpayDeReLinkListViewModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.model.portal.delinkrelinkpaymentmodemodule.DelinkRelinkPaymentModeVieModel;
import com.inov8.microbank.common.util.AppRoleConstantsInterface;
import com.inov8.microbank.common.util.MessageParsingUtils;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.SmsSender;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.dao.portal.allpaymodule.AllpayDelinkRelinkPaymentModeViewDAO;
import com.inov8.microbank.server.dao.portal.delinkrelinkpaymentmodemodule.DelinkRelinkPaymentModeViewDAO;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;
import com.inov8.microbank.server.dao.smartmoneymodule.SmartMoneyAccountDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.financialintegrationmodule.veriflymodule.VeriflyManagerService;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.model.LogModel;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl;
import com.inov8.verifly.server.service.mainmodule.VeriflyManager;

public class DelinkRelinkPaymentModeManagerImpl implements DelinkRelinkPaymentModeManager {
	
	protected static Log logger = LogFactory.getLog(DelinkRelinkPaymentModeManagerImpl.class);

	private AllpayDelinkRelinkPaymentModeViewDAO allpayDelinkRelinkPaymentModeViewDAO;

	private DelinkRelinkPaymentModeViewDAO delinkRelinkPaymentModeViewDAO;

	private AppUserDAO appUserDAO;

	private VeriflyManagerService veriflyManagerService;

	private SmartMoneyAccountDAO smartMoneyAccountDAO;

	private ActionLogManager actionLogManager;

	private SmsSender smsSender;

	private FinancialIntegrationManager financialIntegrationManager;

	private UserDeviceAccountsManager userDeviceAccountsManager;

	public boolean isRetailerOrDistributor(Long appUserId) {
		AppUserModel appUser = this.appUserDAO.findByPrimaryKey(Long.valueOf(appUserId));
		if (appUser.getDistributorContactId() != null || appUser.getRetailerContactId() != null) {
			return true;
		} else {
			return false;
		}
	}

	public SearchBaseWrapper searchDelinkRelinkPaymentMode(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		// populate list
		CustomList<DelinkRelinkPaymentModeVieModel> list = this.delinkRelinkPaymentModeViewDAO.findByExample((DelinkRelinkPaymentModeVieModel) searchBaseWrapper.getBasePersistableModel(),
				searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap());

		// setting list into wrapper
		searchBaseWrapper.setCustomList(list);

		return searchBaseWrapper;
	}

	public BaseWrapper updateDelinkRelinkVeriflyPin(BaseWrapper baseWrapper) throws FrameworkCheckedException, Exception {

		// getting appUserId from wrapper
		Long appUserId = new Long(baseWrapper.getObject(KEY_APP_USER_ID).toString());
		Long smartMoneyAccountId = new Long(baseWrapper.getObject(KEY_SMART_MONEY_ACC_ID).toString());

		/**
		 * Loging the data
		 */
		AppUserModel appUserModel = UserUtils.getCurrentUser();

		ActionLogModel actionLogModel = new ActionLogModel();
		actionLogModel.setActionId((Long) baseWrapper.getObject(PortalConstants.KEY_ACTION_ID));
		actionLogModel.setUsecaseId((Long) baseWrapper.getObject(PortalConstants.KEY_USECASE_ID));
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_START);
		// the process is starting
		actionLogModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
		actionLogModel.setUserName(UserUtils.getCurrentUser().getUsername());
		actionLogModel.setStartTime(new Timestamp(new Date().getTime()));
		actionLogModel.setCustomField1(appUserId.toString());
		actionLogModel.setDeviceTypeId(PortalConstants.WEB_TYPE_DEVICE);
		actionLogModel = logAction(actionLogModel, true);

		// setting actionLogId into thread local
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

		// populating AccountInfoModel and LogModel into VeriflyBaseWrapper

		// initialize VeriflyBaseWrapper
		VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();

		// getting required data from wrapper for getting latest data for model

		// getting latest data from db
		DelinkRelinkPaymentModeVieModel delinkRelinkPaymentModeVieModel = new DelinkRelinkPaymentModeVieModel();
		delinkRelinkPaymentModeVieModel.setAppUserId(appUserId);
		delinkRelinkPaymentModeVieModel.setSmartMoneyAccountId(smartMoneyAccountId);

		CustomList customList = delinkRelinkPaymentModeViewDAO.findByExample(delinkRelinkPaymentModeVieModel);
		delinkRelinkPaymentModeVieModel = (DelinkRelinkPaymentModeVieModel) customList.getResultsetList().get(0);

		BaseWrapper userDeviceAccountBaseWrapper = new BaseWrapperImpl();
		UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
		userDeviceAccountsModel.setAppUserId(appUserId);
		userDeviceAccountBaseWrapper.setBasePersistableModel(userDeviceAccountsModel);
		userDeviceAccountBaseWrapper = userDeviceAccountsManager.loadUserDeviceAccount(userDeviceAccountBaseWrapper);
		userDeviceAccountsModel = (UserDeviceAccountsModel) userDeviceAccountBaseWrapper.getBasePersistableModel();

		SmartMoneyAccountModel smartMoneyAccountModel = smartMoneyAccountDAO.findByPrimaryKey(delinkRelinkPaymentModeVieModel.getSmartMoneyAccountId());

		BaseWrapper baseWrapperSmartMoney = new BaseWrapperImpl();
		baseWrapperSmartMoney.setBasePersistableModel(smartMoneyAccountModel);
		AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperSmartMoney);

		boolean activateAction = smartMoneyAccountModel.getActive();
		boolean phoenixSuccessful = false;
		boolean veriflySuccessful = false;
		boolean isFirstOrLastAccount = false;
		try {

			if (activateAction) { // De-Activate/De-Link Scenario

				smartMoneyAccountModel.setActive(false);
				smartMoneyAccountModel.setUpdatedBy(appUserModel.getAppUserId());
				smartMoneyAccountModel.setUpdatedOn(new Date());

				baseWrapper.putObject(KEY_IS_RELINK, Boolean.FALSE);
			} else { // Re-Activate/Re-Link Scenario

				smartMoneyAccountModel.setActive(true);
				smartMoneyAccountModel.setUpdatedBy(appUserModel.getAppUserId());
				smartMoneyAccountModel.setUpdatedOn(new Date());

				baseWrapper.putObject(KEY_IS_RELINK, Boolean.TRUE);
			}
			smartMoneyAccountDAO.saveOrUpdate(smartMoneyAccountModel);

			if (abstractFinancialInstitution.isVeriflyRequired()) {

				// create accountInfoModel and initialize with data
				AccountInfoModel accountInfoModel = new AccountInfoModel();
				accountInfoModel.setAccountNick(delinkRelinkPaymentModeVieModel.getAccountNick());
				accountInfoModel.setCustomerId(delinkRelinkPaymentModeVieModel.getCustomerId());
				veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);

				// create logModel and initialize with values
				LogModel logModel = new LogModel();

				logModel.setCreatdByUserId(appUserModel.getAppUserId());
				logModel.setCreatedBy(appUserModel.getFirstName() + " " + appUserModel.getLastName());
				veriflyBaseWrapper.setLogModel(logModel);

				VeriflyManager veriflyManager = null;

				if (logger.isDebugEnabled())
					logger.debug("Processing smart money account [" + smartMoneyAccountModel.getName() + ", [" + smartMoneyAccountModel.getActive() + "]");
				// veriflyManager =
				// veriflyManagerService.getVeriflyMgrByAccountId(smartMoneyAccountModel);

				veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
				String title = "";
				boolean firstSmartMoneyAccount = false;
				if (!activateAction) {
					veriflyBaseWrapper = abstractFinancialInstitution.activatePin(veriflyBaseWrapper);

					if (veriflyBaseWrapper.isErrorStatus()) {
						// //Updated against CRF-28.
						/**
						 * If verifly Lite is required (i.e bank) activate
						 * Mobile channel request to Phoenix if it is the first
						 * account/smartmoney account to be relinked.
						 */

						if (abstractFinancialInstitution.isVeriflyLite() && isFirstSmartMoneyAccount(delinkRelinkPaymentModeVieModel.getCustomerId(), AppRoleConstantsInterface.CUSTOMER)) {
							firstSmartMoneyAccount = true;
							if (abstractFinancialInstitution.isIVRChannelActive(veriflyBaseWrapper.getAccountInfoModel().getAccountNo(), smartMoneyAccountModel.getAccountTypeId().toString(),
									smartMoneyAccountModel.getCurrencyCodeId().toString(), delinkRelinkPaymentModeVieModel.getNic())) {

								if (abstractFinancialInstitution.activateDeliveryChannel(smartMoneyAccountModel.getAccountTypeId().toString(), delinkRelinkPaymentModeVieModel.getNic())) {
									// messageString =
									// MessageUtil.getMessage("linkPaymentMode.bankfirstaccount.successMessage",
									// args);
									phoenixSuccessful = true;

								} else {
									phoenixSuccessful = false;
									throw new FrameworkCheckedException("linkPaymentMode.customerprofiledoesnotexist");

								}
							}
						}

						Object[] args = { delinkRelinkPaymentModeVieModel.getAccountNick(), veriflyBaseWrapper.getAccountInfoModel().getGeneratedPin() };

						String messageString = "";

						if (abstractFinancialInstitution.isVeriflyLite()) {
							if (smartMoneyAccountModel.getPaymentModeId().longValue() == PaymentModeConstantsInterface.CREDIT_CARD.longValue())
								messageString = MessageUtil.getMessage("delinkRelinkPaymentModeCC.veriflylite.relinkPaymentMode", args);
							else
								messageString = MessageUtil.getMessage("delinkRelinkPaymentModeBA.veriflylite.relinkPaymentMode", args);
						} else {
							messageString = MessageUtil.getMessage("delinkRelinkPaymentMode.relinkPaymentMode", args);

						}

						if (null != userDeviceAccountsModel && userDeviceAccountsModel.getCommissioned()) {
							smsSender.send(new SmsMessage(delinkRelinkPaymentModeVieModel.getMobileNo(), MessageParsingUtils.parseMessageForIpos(messageString)));
						} else {
							smsSender.send(new SmsMessage(delinkRelinkPaymentModeVieModel.getMobileNo(), messageString));
						}
						baseWrapper.putObject(KEY_IS_RELINK, Boolean.TRUE);
						if (logger.isDebugEnabled())
							logger.debug("Smart money account Re-Linked");
						veriflySuccessful = true;
					} else {
						veriflySuccessful = false;
						throw new FrameworkCheckedException(veriflyBaseWrapper.getErrorMessage());
					}
				} else { // De-Link Scenario
					// using banks).
					veriflyBaseWrapper = abstractFinancialInstitution.deactivatePin(veriflyBaseWrapper);
					// if verifly have no error then changed in smartmoney
					// account
					if (veriflyBaseWrapper.isErrorStatus()) {
						/**
						 * If verifly Lite is required (i.e bank) deactivate
						 * Mobile channel request to Phoenix if it is the last
						 * account/smartmoney account to be relinked.
						 */

						if (abstractFinancialInstitution.isVeriflyLite() && isLastSmartMoneyAccount(delinkRelinkPaymentModeVieModel.getCustomerId(),AppRoleConstantsInterface.CUSTOMER)) {
							isFirstOrLastAccount = true;
							{

								if (abstractFinancialInstitution.deActivateDeliveryChannel(smartMoneyAccountModel.getAccountTypeId().toString(), delinkRelinkPaymentModeVieModel.getNic())) {
									// messageString =
									// MessageUtil.getMessage("linkPaymentMode.bankfirstaccount.successMessage",
									// args);
									phoenixSuccessful = true;

								} else {
									phoenixSuccessful = false;
									throw new FrameworkCheckedException("linkPaymentMode.customerprofiledoesnotexist");

								}
							}

						}

						baseWrapper.putObject(KEY_IS_RELINK, Boolean.FALSE);
						if (logger.isDebugEnabled())
							logger.debug("Smart money account De-Linked");
						veriflySuccessful = true;
					} else {
						veriflySuccessful = false;
						throw new FrameworkCheckedException(veriflyBaseWrapper.getErrorMessage());
					}

				} // End De-Link Scenario

			} else {
				// send re-link sms if verifly not required, this is for
				// non-verifly banks.
				if (!activateAction) {

					String messageString = MessageUtil.getMessage("delinkRelinkPaymentMode.nonverifly.relinkPaymentMode");
					if (null != userDeviceAccountsModel && userDeviceAccountsModel.getCommissioned()) {
						messageString = MessageParsingUtils.parseMessageForIpos(messageString);
					}

					smsSender.send(new SmsMessage(delinkRelinkPaymentModeVieModel.getMobileNo(), messageString));
				}
			}

			/**
			 * Logging Information, Ending Status
			 */
			AppUserModel appUser = appUserDAO.getUser(appUserId);
			actionLogModel.setEndTime(new Timestamp(new Date().getTime()));
			actionLogModel.setCustomField11(delinkRelinkPaymentModeVieModel.getMfsId());
			actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END); // for
			// process ends
			actionLogModel = logAction(actionLogModel, false);
		}
		// catch (ImplementationNotSupportedException inse) {
		// throw new FrameworkCheckedException(
		// "implementationNotSupportedException");
		// }
		catch (Exception ex) {
			if (abstractFinancialInstitution.isVeriflyLite() && !phoenixSuccessful) {
				// Error/Exception occured at phoenix call..
				// Means that Verifly was definetly successful.. have to roll
				// back the verifly
				if (activateAction) {
					// verifly call to activate the mobile channel
					LogModel logModel = new LogModel();

					logModel.setCreatdByUserId(appUserModel.getAppUserId());
					logModel.setCreatedBy(appUserModel.getFirstName() + " " + appUserModel.getLastName());
					veriflyBaseWrapper.setLogModel(logModel);
					veriflyBaseWrapper = abstractFinancialInstitution.activatePin(veriflyBaseWrapper);
				} else {
					// verifly call to deactivate the mobile channel
					veriflyBaseWrapper = abstractFinancialInstitution.deactivatePin(veriflyBaseWrapper);
				}
			}
			// if( abstractFinancialInstitution.isVeriflyRequired() &&
			// !veriflySuccessful ){
			// if(abstractFinancialInstitution.isVeriflyLite() &&
			// phoenixSuccessful){
			// // Roll back Phoenix call here
			// }
			// }
			if (ex instanceof ImplementationNotSupportedException) {
				throw new FrameworkCheckedException("implementationNotSupportedException");
			}
			throw new FrameworkCheckedException(ex.getMessage());
		} finally {
			ThreadLocalActionLog.remove();
		}
		return baseWrapper;
	}

	private boolean isFirstSmartMoneyAccount(Long customerId,Long customerType) {		
		SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
		if (customerType.longValue()== AppRoleConstantsInterface.CUSTOMER.longValue())
			smartMoneyAccountModel.setCustomerId(customerId);
		else if (customerType.longValue()== AppRoleConstantsInterface.RETAILER.longValue())
			smartMoneyAccountModel.setRetailerContactId(customerId);
		else if (customerType.longValue()== AppRoleConstantsInterface.DISTRIBUTOR.longValue())
			smartMoneyAccountModel.setDistributorContactId(customerId);
		smartMoneyAccountModel.setCustomerId(customerId);
		smartMoneyAccountModel.setBankId(UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankId());
		smartMoneyAccountModel.setActive(true);
		CustomList list = this.smartMoneyAccountDAO.findByExample(smartMoneyAccountModel);
		if (list.getResultsetList().size() > 0) {
			return false;
		} else {
			return true;
		}

	}

	private boolean isLastSmartMoneyAccount(Long customerId,Long customerType) {
		
		SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
		if (customerType.longValue()== AppRoleConstantsInterface.CUSTOMER.longValue())
			smartMoneyAccountModel.setCustomerId(customerId);
		else if (customerType.longValue()== AppRoleConstantsInterface.RETAILER.longValue())
			smartMoneyAccountModel.setRetailerContactId(customerId);
		else if (customerType.longValue()== AppRoleConstantsInterface.DISTRIBUTOR.longValue())
			smartMoneyAccountModel.setDistributorContactId(customerId);
		smartMoneyAccountModel.setBankId(UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankId());
		smartMoneyAccountModel.setActive(true);
		CustomList list = this.smartMoneyAccountDAO.findByExample(smartMoneyAccountModel);
		if (list.getResultsetList().size() == 1) {
			return true;
		} else {
			return false;
		}

	}
	public BaseWrapper allpayDeleteAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException, Exception {
		try {

			Long smartMoneyAccountId = new Long(baseWrapper.getObject(KEY_SMART_MONEY_ACC_ID).toString());
			Long appUserId = new Long(baseWrapper.getObject(KEY_APP_USER_ID).toString());

			/**
			 * Loging the data
			 */
			AppUserModel appUserModel = UserUtils.getCurrentUser();

			ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);

			// setting actionLogId into thread local
			ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

			String timestamp = new java.util.Date().getTime() + "";

			SmartMoneyAccountModel smartMoneyAccountModel = smartMoneyAccountDAO.findByPrimaryKey(smartMoneyAccountId);

			BaseWrapper baseWrapperSmartMoney = new BaseWrapperImpl();
			baseWrapperSmartMoney.setBasePersistableModel(smartMoneyAccountModel);
			AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperSmartMoney);
			if (smartMoneyAccountModel.getActive().booleanValue() == Boolean.FALSE) {

				String accountNick = smartMoneyAccountModel.getName();
				smartMoneyAccountModel.setActive(false);
				smartMoneyAccountModel.setUpdatedBy(appUserModel.getAppUserId());
				smartMoneyAccountModel.setUpdatedOn(new Date());
				smartMoneyAccountModel.setName(smartMoneyAccountModel.getName() + "_" + timestamp);
				smartMoneyAccountModel.setDeleted(true);
				

				// this delink code is done for getting the mobile no.
				AllpayDeReLinkListViewModel delinkRelinkPaymentModeVieModel = new AllpayDeReLinkListViewModel();
				delinkRelinkPaymentModeVieModel.setAppUserId(appUserId);
				delinkRelinkPaymentModeVieModel.setSmartMoneyAccountId(smartMoneyAccountId);

				CustomList customList = this.allpayDelinkRelinkPaymentModeViewDAO.findByExample(delinkRelinkPaymentModeVieModel);
				delinkRelinkPaymentModeVieModel = (AllpayDeReLinkListViewModel) customList.getResultsetList().get(0);
				boolean firstSmartMoneyAccount = false;
				boolean phoenixSuccessful = false;
				boolean isFirstOrLastAccount = false;
				smartMoneyAccountDAO.saveOrUpdate(smartMoneyAccountModel);
				if (abstractFinancialInstitution.isVeriflyRequired()) {

					// initialize VeriflyBaseWrapper
					VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();

					// create accountInfoModel and initialize with data
					AccountInfoModel accountInfoModel = new AccountInfoModel();
					accountInfoModel.setAccountNick(accountNick);
					accountInfoModel.setNewAccountNick(timestamp);
					accountInfoModel.setCustomerId(appUserId);//For agent, ACCOUNT_INFO.CUSTOMER_ID refers APP_USER.APP_USER_ID
					veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);

					// create logModel and initialize with values
					LogModel logModel = new LogModel();

					logModel.setCreatdByUserId(appUserModel.getAppUserId());
					logModel.setCreatedBy(appUserModel.getFirstName() + " " + appUserModel.getLastName());
					veriflyBaseWrapper.setLogModel(logModel);

					
					veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
					veriflyBaseWrapper = abstractFinancialInstitution.markAsDeleted(veriflyBaseWrapper);
					// if verifly successfull then changed into smart money
					if (veriflyBaseWrapper.isErrorStatus()) {
						// changing active status of smartmoney account table

						
						smsSender.send(new SmsMessage(delinkRelinkPaymentModeVieModel.getMobileNo(), MessageUtil.getMessage("delinkRelinkPaymentMode.accountDeleted", new Object[] { accountInfoModel
								.getAccountNick() })));
					} else {
						throw new FrameworkCheckedException(veriflyBaseWrapper.getErrorMessage());
					}
				} else {
					smsSender.send(new SmsMessage(delinkRelinkPaymentModeVieModel.getMobileNo(), MessageUtil.getMessage("delinkRelinkPaymentMode.nonverifly.accountDeleted")));
				}

				/**
				 * Logging Information, Ending Status
				 */
				AppUserModel userModel = appUserDAO.getUser(appUserId);
				actionLogModel.setCustomField1(smartMoneyAccountId.toString().toString());
				actionLogModel.setCustomField11(delinkRelinkPaymentModeVieModel.getMfsId());
		    	this.actionLogManager.completeActionLog(actionLogModel);
			} else {
				throw new FrameworkCheckedException("Account should be de-linked before deletion");

			}
		} catch (ImplementationNotSupportedException inse) {
			throw new FrameworkCheckedException("implementationNotSupportedException");
		}

		catch (Exception ex) {
			throw new FrameworkCheckedException(ex.getMessage());

		} finally {
			ThreadLocalActionLog.remove();
		}

		return baseWrapper;
	}
	public BaseWrapper deleteAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException, Exception {
		try {

			Long smartMoneyAccountId = new Long(baseWrapper.getObject(KEY_SMART_MONEY_ACC_ID).toString());
			Long appUserId = new Long(baseWrapper.getObject(KEY_APP_USER_ID).toString());

			/**
			 * Loging the data
			 */
			AppUserModel appUserModel = UserUtils.getCurrentUser();

			ActionLogModel actionLogModel = new ActionLogModel();
			actionLogModel.setActionId((Long) baseWrapper.getObject(PortalConstants.KEY_ACTION_ID));
			actionLogModel.setUsecaseId((Long) baseWrapper.getObject(PortalConstants.KEY_USECASE_ID));
			actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_START);
			// the process is starting
			actionLogModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
			actionLogModel.setUserName(UserUtils.getCurrentUser().getUsername());
			actionLogModel.setStartTime(new Timestamp(new Date().getTime()));
			actionLogModel.setCustomField1(smartMoneyAccountId.toString());
			actionLogModel.setDeviceTypeId(PortalConstants.WEB_TYPE_DEVICE);
			actionLogModel = logAction(actionLogModel, true);

			// setting actionLogId into thread local
			ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

			String timestamp = new java.util.Date().getTime() + "";

			SmartMoneyAccountModel smartMoneyAccountModel = smartMoneyAccountDAO.findByPrimaryKey(smartMoneyAccountId);

			BaseWrapper baseWrapperSmartMoney = new BaseWrapperImpl();
			baseWrapperSmartMoney.setBasePersistableModel(smartMoneyAccountModel);
			AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperSmartMoney);
			if (smartMoneyAccountModel.getActive().booleanValue() == Boolean.FALSE) {

				String accountNick = smartMoneyAccountModel.getName();
				smartMoneyAccountModel.setActive(false);
				smartMoneyAccountModel.setUpdatedBy(appUserModel.getAppUserId());
				smartMoneyAccountModel.setUpdatedOn(new Date());
				smartMoneyAccountModel.setName(smartMoneyAccountModel.getName() + "_" + timestamp);
				smartMoneyAccountModel.setDeleted(true);
				smartMoneyAccountDAO.saveOrUpdate(smartMoneyAccountModel);

				// this delink code is done for getting the mobile no.
				DelinkRelinkPaymentModeVieModel delinkRelinkPaymentModeVieModel = new DelinkRelinkPaymentModeVieModel();
				delinkRelinkPaymentModeVieModel.setAppUserId(appUserId);
				delinkRelinkPaymentModeVieModel.setSmartMoneyAccountId(smartMoneyAccountId);

				CustomList customList = delinkRelinkPaymentModeViewDAO.findByExample(delinkRelinkPaymentModeVieModel);
				delinkRelinkPaymentModeVieModel = (DelinkRelinkPaymentModeVieModel) customList.getResultsetList().get(0);
				boolean firstSmartMoneyAccount = false;
				boolean phoenixSuccessful = false;
				boolean isFirstOrLastAccount = false;

				if (abstractFinancialInstitution.isVeriflyRequired()) {

					// initialize VeriflyBaseWrapper
					VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();

					// create accountInfoModel and initialize with data
					AccountInfoModel accountInfoModel = new AccountInfoModel();
					accountInfoModel.setAccountNick(accountNick);
					accountInfoModel.setNewAccountNick(timestamp);
					accountInfoModel.setCustomerId(smartMoneyAccountModel.getCustomerId());
					veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);

					// create logModel and initialize with values
					LogModel logModel = new LogModel();

					logModel.setCreatdByUserId(appUserModel.getAppUserId());
					logModel.setCreatedBy(appUserModel.getFirstName() + " " + appUserModel.getLastName());
					veriflyBaseWrapper.setLogModel(logModel);

					// VeriflyManager veriflyManager = null;

					// veriflyManager =
					// veriflyManagerService.getVeriflyMgrByAccountId(smartMoneyAccountModel);

					// veriflyBaseWrapper =
					// veriflyManager.markAsDeleted(veriflyBaseWrapper);
					veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
					veriflyBaseWrapper = abstractFinancialInstitution.markAsDeleted(veriflyBaseWrapper);
					// if verifly successfull then changed into smart money
					if (veriflyBaseWrapper.isErrorStatus()) {
						// changing active status of smartmoney account table

						// smartMoneyAccountModel.setActive(false);
						// smartMoneyAccountModel.setUpdatedBy(appUserModel.getAppUserId());
						// smartMoneyAccountModel.setUpdatedOn(new Date());
						// smartMoneyAccountModel.setName(smartMoneyAccountModel.getName()+"_"+timestamp);
						// smartMoneyAccountModel.setDeleted(true);
						//
						// smartMoneyAccountDAO.saveOrUpdate(smartMoneyAccountModel);

						// getting latest data from db
						// DelinkRelinkPaymentModeVieModel
						// delinkRelinkPaymentModeVieModel = new
						// DelinkRelinkPaymentModeVieModel();
						// delinkRelinkPaymentModeVieModel.setAppUserId(appUserId);
						// delinkRelinkPaymentModeVieModel.setSmartMoneyAccountId(smartMoneyAccountId);
						//	
						// CustomList customList =
						// delinkRelinkPaymentModeViewDAO.findByExample(delinkRelinkPaymentModeVieModel);
						// delinkRelinkPaymentModeVieModel =
						// (DelinkRelinkPaymentModeVieModel)
						// customList.getResultsetList().get(0);

						// Updated against CRF-28.

						// /Bank accounts

						// / first account deleted

						/*
						 * if(abstractFinancialInstitution.isVeriflyLite() &&
						 * isFirstSmartMoneyAccount(delinkRelinkPaymentModeVieModel.getCustomerId())) {
						 * firstSmartMoneyAccount = true;
						 * if(abstractFinancialInstitution.isIVRChannelActive(veriflyBaseWrapper.getAccountInfoModel().getAccountNo(),
						 * smartMoneyAccountModel.getAccountTypeId().toString(),
						 * smartMoneyAccountModel.getCurrencyCodeId().toString(),delinkRelinkPaymentModeVieModel.getNic())) {
						 * 
						 * if(abstractFinancialInstitution.activateDeliveryChannel(smartMoneyAccountModel.getAccountTypeId().toString(),
						 * delinkRelinkPaymentModeVieModel.getNic())) {
						 * //messageString =
						 * MessageUtil.getMessage("linkPaymentMode.bankfirstaccount.successMessage",
						 * args); phoenixSuccessful = true; } else {
						 * phoenixSuccessful = false; throw new
						 * FrameworkCheckedException
						 * ("linkPaymentMode.customerprofiledoesnotexist"); } } }
						 */

						// end first account deleted
						// // last account deleted
						// no phoneix hit as account is already has been
						// deactivated
						/*
						 * if (abstractFinancialInstitution.isVeriflyLite()) {
						 * if(
						 * isLastSmartMoneyAccount(delinkRelinkPaymentModeVieModel.getCustomerId())){
						 * isFirstOrLastAccount = true; { if(
						 * abstractFinancialInstitution.deActivateDeliveryChannel(smartMoneyAccountModel.getAccountTypeId().toString(),
						 * delinkRelinkPaymentModeVieModel.getNic()) ) {
						 * 
						 * phoenixSuccessful = true; } else { phoenixSuccessful =
						 * false; throw new FrameworkCheckedException
						 * ("linkPaymentMode.customerprofiledoesnotexist"); } } } }
						 */
						// no phoneix hit as account is already has been
						// deactivated
						// / end last account
						// ////////////end bank accounts
						smsSender.send(new SmsMessage(delinkRelinkPaymentModeVieModel.getMobileNo(), MessageUtil.getMessage("delinkRelinkPaymentMode.accountDeleted", new Object[] { accountInfoModel
								.getAccountNick() })));

						// smsSender.send(new
						// SmsMessage(delinkRelinkPaymentModeVieModel.getMobileNo(),
						// "Dear Customer, Your account has been deleted."
						// ));

					} else {
						throw new FrameworkCheckedException(veriflyBaseWrapper.getErrorMessage());
					}
				} else {
					smsSender.send(new SmsMessage(delinkRelinkPaymentModeVieModel.getMobileNo(), MessageUtil.getMessage("delinkRelinkPaymentMode.nonverifly.accountDeleted")));
				}

				/**
				 * Logging Information, Ending Status
				 */
				AppUserModel appUser = appUserDAO.getUser(appUserId);
				actionLogModel.setEndTime(new Timestamp(new Date().getTime()));
				actionLogModel.setCustomField11(delinkRelinkPaymentModeVieModel.getMfsId());
				actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END); // for
				// process ends
				actionLogModel = logAction(actionLogModel, false);
			} else {
				throw new FrameworkCheckedException("Account should be de-linked before deletion");

			}
		} catch (ImplementationNotSupportedException inse) {
			throw new FrameworkCheckedException("implementationNotSupportedException");
		}

		catch (Exception ex) {
			throw new FrameworkCheckedException(ex.getMessage());

		} finally {
			ThreadLocalActionLog.remove();
		}

		return baseWrapper;
	}

	private ActionLogModel logAction(ActionLogModel actionLogModel, boolean isNewTrans) throws FrameworkCheckedException {
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(actionLogModel);
		if (isNewTrans)
			baseWrapper = this.actionLogManager.createOrUpdateActionLogRequiresNewTransaction(baseWrapper);
		else
			baseWrapper = this.actionLogManager.createOrUpdateActionLog(baseWrapper);
		return (ActionLogModel) baseWrapper.getBasePersistableModel();
	}

	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}

	public DelinkRelinkPaymentModeViewDAO getDelinkRelinkPaymentModeViewDAO() {
		return delinkRelinkPaymentModeViewDAO;
	}

	public void setDelinkRelinkPaymentModeViewDAO(DelinkRelinkPaymentModeViewDAO delinkRelinkPaymentModeViewDAO) {
		this.delinkRelinkPaymentModeViewDAO = delinkRelinkPaymentModeViewDAO;
	}

	public void setSmartMoneyAccountDAO(SmartMoneyAccountDAO smartMoneyAccountDAO) {
		this.smartMoneyAccountDAO = smartMoneyAccountDAO;
	}

	public void setSmsSender(SmsSender smsSender) {
		this.smsSender = smsSender;
	}

	public void setVeriflyManagerService(VeriflyManagerService veriflyManagerService) {
		this.veriflyManagerService = veriflyManagerService;
	}

	public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager) {
		this.financialIntegrationManager = financialIntegrationManager;
	}

	public void setUserDeviceAccountsManager(UserDeviceAccountsManager userDeviceAccountsManager) {
		this.userDeviceAccountsManager = userDeviceAccountsManager;
	}

	public BaseWrapper updateAllPayDelinkRelinkVeriflyPin(BaseWrapper baseWrapper) throws FrameworkCheckedException, Exception {

		// getting appUserId from wrapper
		Long appUserId = new Long(baseWrapper.getObject(KEY_APP_USER_ID).toString());
		Long smartMoneyAccountId = new Long(baseWrapper.getObject(KEY_SMART_MONEY_ACC_ID).toString());

		/**
		 * Loging the data
		 */
		AppUserModel appUserModel = UserUtils.getCurrentUser();

		ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);

		// setting actionLogId into thread local
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

		// populating AccountInfoModel and LogModel into VeriflyBaseWrapper

		// initialize VeriflyBaseWrapper
		VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();

		// getting required data from wrapper for getting latest data for model

		// getting latest data from db
		AllpayDeReLinkListViewModel delinkRelinkPaymentModeVieModel = new AllpayDeReLinkListViewModel();
		delinkRelinkPaymentModeVieModel.setAppUserId(appUserId);
		delinkRelinkPaymentModeVieModel.setSmartMoneyAccountId(smartMoneyAccountId);

		CustomList customList = allpayDelinkRelinkPaymentModeViewDAO.findByExample(delinkRelinkPaymentModeVieModel);
		delinkRelinkPaymentModeVieModel = (AllpayDeReLinkListViewModel) customList.getResultsetList().get(0);

		BaseWrapper userDeviceAccountBaseWrapper = new BaseWrapperImpl();
		UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
		userDeviceAccountsModel.setAppUserId(appUserId);
		userDeviceAccountBaseWrapper.setBasePersistableModel(userDeviceAccountsModel);
		userDeviceAccountBaseWrapper = userDeviceAccountsManager.loadUserDeviceAccount(userDeviceAccountBaseWrapper);
		userDeviceAccountsModel = (UserDeviceAccountsModel) userDeviceAccountBaseWrapper.getBasePersistableModel();

		SmartMoneyAccountModel smartMoneyAccountModel = smartMoneyAccountDAO.findByPrimaryKey(delinkRelinkPaymentModeVieModel.getSmartMoneyAccountId());

		BaseWrapper baseWrapperSmartMoney = new BaseWrapperImpl();
		baseWrapperSmartMoney.setBasePersistableModel(smartMoneyAccountModel);
		AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperSmartMoney);

		boolean activateAction = smartMoneyAccountModel.getActive();
		boolean phoenixSuccessful = false;
		boolean veriflySuccessful = false;
		boolean isFirstOrLastAccount = false;
		try {

			if (activateAction) { // De-Activate/De-Link Scenario

				smartMoneyAccountModel.setActive(false);
				smartMoneyAccountModel.setUpdatedBy(appUserModel.getAppUserId());
				smartMoneyAccountModel.setUpdatedOn(new Date());

				baseWrapper.putObject(KEY_IS_RELINK, Boolean.FALSE);
			} else { // Re-Activate/Re-Link Scenario

				// check if an account other than OLA is already linked or not
				// linkPaymentModeManager
				baseWrapper.putObject(KEY_IS_RELINK, Boolean.TRUE);
				if (isAccountOtherThanOLAExists(smartMoneyAccountModel)) {
					baseWrapper.putObject(KEY_IS_RELINK, Boolean.FALSE);
					baseWrapper.putObject("moreThanTwoAccounts", "y");
					return baseWrapper;
				}
				// ---------------------------------------------------------
				smartMoneyAccountModel.setActive(true);
				smartMoneyAccountModel.setUpdatedBy(appUserModel.getAppUserId());
				smartMoneyAccountModel.setUpdatedOn(new Date());
			}
			smartMoneyAccountDAO.saveOrUpdate(smartMoneyAccountModel);

			if (abstractFinancialInstitution.isVeriflyRequired()) {

				// create accountInfoModel and initialize with data
				AccountInfoModel accountInfoModel = new AccountInfoModel();
				accountInfoModel.setAccountNick(delinkRelinkPaymentModeVieModel.getAccountNick());
				if (delinkRelinkPaymentModeVieModel.getCustomerId() != null) {
					accountInfoModel.setCustomerId(delinkRelinkPaymentModeVieModel.getCustomerId());
				} else {
					accountInfoModel.setCustomerId(appUserId);
				}

				veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
				veriflyBaseWrapper.setSkipPanCheck(Boolean.TRUE);

				// create logModel and initialize with values
				LogModel logModel = new LogModel();

				logModel.setCreatdByUserId(appUserModel.getAppUserId());
				logModel.setCreatedBy(appUserModel.getFirstName() + " " + appUserModel.getLastName());
				veriflyBaseWrapper.setLogModel(logModel);

				VeriflyManager veriflyManager = null;

				if (logger.isDebugEnabled())
					logger.debug("Processing smart money account [" + smartMoneyAccountModel.getName() + ", [" + smartMoneyAccountModel.getActive() + "]");
				// veriflyManager =
				// veriflyManagerService.getVeriflyMgrByAccountId(smartMoneyAccountModel);

				veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
				String title = "";
				boolean firstSmartMoneyAccount = false;
				if (!activateAction) {
					veriflyBaseWrapper = abstractFinancialInstitution.activatePin(veriflyBaseWrapper);

					if (veriflyBaseWrapper.isErrorStatus()) {
						// //Updated against CRF-28.
						/**
						 * If verifly Lite is required (i.e bank) activate
						 * Mobile channel request to Phoenix if it is the first
						 * account/smartmoney account to be relinked.
						 */
						Long custId = null;
						Long custType=null;
						if (delinkRelinkPaymentModeVieModel.getRetailerContactId()!= null){
							custId = delinkRelinkPaymentModeVieModel.getRetailerContactId();
							custType=AppRoleConstantsInterface.RETAILER;
						}else if (delinkRelinkPaymentModeVieModel.getDistributorContactId()!= null){
							custId = delinkRelinkPaymentModeVieModel.getDistributorContactId();
							custType=AppRoleConstantsInterface.DISTRIBUTOR;
						}else if (delinkRelinkPaymentModeVieModel.getCustomerId()!= null){
							custId = delinkRelinkPaymentModeVieModel.getCustomerId();
							custType=AppRoleConstantsInterface.CUSTOMER;
						}
						if (abstractFinancialInstitution.isVeriflyLite() && isFirstSmartMoneyAccount(custId,custType)) {
							firstSmartMoneyAccount = true;
							if (abstractFinancialInstitution.isIVRChannelActive(veriflyBaseWrapper.getAccountInfoModel().getAccountNo(), smartMoneyAccountModel.getAccountTypeId().toString(),
									smartMoneyAccountModel.getCurrencyCodeId().toString(), delinkRelinkPaymentModeVieModel.getNic())) {

								if (abstractFinancialInstitution.activateDeliveryChannel(smartMoneyAccountModel.getAccountTypeId().toString(), delinkRelinkPaymentModeVieModel.getNic())) {
									// messageString =
									// MessageUtil.getMessage("linkPaymentMode.bankfirstaccount.successMessage",
									// args);
									phoenixSuccessful = true;

								} else {
									phoenixSuccessful = false;
									throw new FrameworkCheckedException("linkPaymentMode.customerprofiledoesnotexist");

								}
							}
						}

						Object[] args = { delinkRelinkPaymentModeVieModel.getAccountNick(), veriflyBaseWrapper.getAccountInfoModel().getGeneratedPin() };

						String messageString = "";

						if (abstractFinancialInstitution.isVeriflyLite()) {							
								if (smartMoneyAccountModel.getPaymentModeId().longValue() == PaymentModeConstantsInterface.CREDIT_CARD.longValue())
									messageString = MessageUtil.getMessage("AllPayBA.veriflylite.relinkPaymentMode", args);
								else
									messageString = MessageUtil.getMessage("AllPayCC.veriflylite.relinkPaymentMode", args);
							 
						} else {
							messageString = MessageUtil.getMessage("allpaydelinkRelinkPaymentMode.relinkPaymentMode", args);

						}

						if (null != userDeviceAccountsModel && userDeviceAccountsModel.getCommissioned()) {
							smsSender.send(new SmsMessage(delinkRelinkPaymentModeVieModel.getMobileNo(), MessageParsingUtils.parseMessageForIpos(messageString)));
						} else {
							smsSender.send(new SmsMessage(delinkRelinkPaymentModeVieModel.getMobileNo(), messageString));
						}
						baseWrapper.putObject(KEY_IS_RELINK, Boolean.TRUE);
						if (logger.isDebugEnabled())
							logger.debug("Smart money account Re-Linked");
						veriflySuccessful = true;
					} else {
						veriflySuccessful = false;
						throw new FrameworkCheckedException(veriflyBaseWrapper.getErrorMessage());
					}
				} else { // De-Link Scenario
					// using banks).
					veriflyBaseWrapper = abstractFinancialInstitution.deactivatePin(veriflyBaseWrapper);
					// if verifly have no error then changed in smartmoney
					// account
					if (veriflyBaseWrapper.isErrorStatus()) {
						/**
						 * If verifly Lite is required (i.e bank) deactivate
						 * Mobile channel request to Phoenix if it is the last
						 * account/smartmoney account to be relinked.
						 */
						Long custId = null;
						Long custType=null;
						if (delinkRelinkPaymentModeVieModel.getRetailerContactId()!= null){
							custId = delinkRelinkPaymentModeVieModel.getRetailerContactId();
							custType=AppRoleConstantsInterface.RETAILER;
						}else if (delinkRelinkPaymentModeVieModel.getDistributorContactId()!= null){
							custId = delinkRelinkPaymentModeVieModel.getDistributorContactId();
							custType=AppRoleConstantsInterface.DISTRIBUTOR;
						}else if (delinkRelinkPaymentModeVieModel.getCustomerId()!= null){
							custId = delinkRelinkPaymentModeVieModel.getCustomerId();
							custType=AppRoleConstantsInterface.CUSTOMER;
						}
						if (abstractFinancialInstitution.isVeriflyLite() && isLastSmartMoneyAccount(custId,custType)) {
							isFirstOrLastAccount = true;
							{

								if (abstractFinancialInstitution.deActivateDeliveryChannel(smartMoneyAccountModel.getAccountTypeId().toString(), delinkRelinkPaymentModeVieModel.getNic())) {
									// messageString =
									// MessageUtil.getMessage("linkPaymentMode.bankfirstaccount.successMessage",
									// args);
									phoenixSuccessful = true;

								} else {
									phoenixSuccessful = false;
									throw new FrameworkCheckedException("linkPaymentMode.customerprofiledoesnotexist");

								}
							}

						}

						baseWrapper.putObject(KEY_IS_RELINK, Boolean.FALSE);
						if (logger.isDebugEnabled())
							logger.debug("Smart money account De-Linked");
						veriflySuccessful = true;
					} else {
						veriflySuccessful = false;
						throw new FrameworkCheckedException(veriflyBaseWrapper.getErrorMessage());
					}

				} // End De-Link Scenario

			} else {
				// send re-link sms if verifly not required, this is for
				// non-verifly banks.
				if (!activateAction) {
					String messageString;
					
						messageString = MessageUtil.getMessage("allpayDelinkRelinkPaymentMode.nonverifly.relinkPaymentMode");
					
					if (null != userDeviceAccountsModel && userDeviceAccountsModel.getCommissioned()) {
						messageString = MessageParsingUtils.parseMessageForIpos(messageString);
					}

					smsSender.send(new SmsMessage(delinkRelinkPaymentModeVieModel.getMobileNo(), messageString));
				}
			}

			/**
			 * Logging Information, Ending Status
			 */
			AppUserModel userModel = appUserDAO.getUser(appUserId);
			actionLogModel.setCustomField11(delinkRelinkPaymentModeVieModel.getMfsId());
			actionLogModel.setCustomField1(appUserId.toString());
	    	this.actionLogManager.completeActionLog(actionLogModel);
		}
		// catch (ImplementationNotSupportedException inse) {
		// throw new FrameworkCheckedException(
		// "implementationNotSupportedException");
		// }
		catch (Exception ex) {
			if (abstractFinancialInstitution.isVeriflyLite() && !phoenixSuccessful) {
				// Error/Exception occured at phoenix call..
				// Means that Verifly was definetly successful.. have to roll
				// back the verifly
				if (activateAction) {
					// verifly call to activate the mobile channel
					LogModel logModel = new LogModel();

					logModel.setCreatdByUserId(appUserModel.getAppUserId());
					logModel.setCreatedBy(appUserModel.getFirstName() + " " + appUserModel.getLastName());
					veriflyBaseWrapper.setLogModel(logModel);
					veriflyBaseWrapper = abstractFinancialInstitution.activatePin(veriflyBaseWrapper);
				} else {
					// verifly call to deactivate the mobile channel
					veriflyBaseWrapper = abstractFinancialInstitution.deactivatePin(veriflyBaseWrapper);
				}
			}
			// if( abstractFinancialInstitution.isVeriflyRequired() &&
			// !veriflySuccessful ){
			// if(abstractFinancialInstitution.isVeriflyLite() &&
			// phoenixSuccessful){
			// // Roll back Phoenix call here
			// }
			// }
			if (ex instanceof ImplementationNotSupportedException) {
				throw new FrameworkCheckedException("implementationNotSupportedException");
			}
			throw new FrameworkCheckedException(ex.getMessage());
		} finally {
			ThreadLocalActionLog.remove();
		}
		return baseWrapper;

	}

	private boolean isAccountOtherThanOLAExists(SmartMoneyAccountModel smartMoneyAccountModel) {
		SmartMoneyAccountModel tempSMAM = new SmartMoneyAccountModel();
		tempSMAM.setActive(true);
		tempSMAM.setDistributorContactId(smartMoneyAccountModel.getDistributorContactId());
		tempSMAM.setRetailerContactId(smartMoneyAccountModel.getRetailerContactId());
		CustomList<SmartMoneyAccountModel> list = this.smartMoneyAccountDAO.findByExample(tempSMAM);
		if (list.getResultsetList().size() == 2) {
			return true;
		} else {
			return false;
		}

	}

	public AllpayDelinkRelinkPaymentModeViewDAO getAllpayDelinkRelinkPaymentModeViewDAO() {
		return allpayDelinkRelinkPaymentModeViewDAO;
	}

	public void setAllpayDelinkRelinkPaymentModeViewDAO(AllpayDelinkRelinkPaymentModeViewDAO allapyDelinkRelinkPaymentModeViewDAO) {
		this.allpayDelinkRelinkPaymentModeViewDAO = allapyDelinkRelinkPaymentModeViewDAO;
	}

	public AppUserDAO getAppUserDAO() {
		return appUserDAO;
	}

	public void setAppUserDAO(AppUserDAO appUserDAO) {
		this.appUserDAO = appUserDAO;
	}

}
