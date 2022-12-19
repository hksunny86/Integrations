package com.inov8.microbank.server.service.portal.linkpaymentmodemodule;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.MatchMode;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.exception.ImplementationNotSupportedException;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.AllpayUserInfoListViewModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.model.portal.changeaccnickmodule.ChangeAccountNickListViewModel;
import com.inov8.microbank.common.model.portal.linkpaymentmodemodule.LinkPaymentModeModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.UserInfoListViewModel;
import com.inov8.microbank.common.util.AppRoleConstantsInterface;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.FinancialInstitutionConstants;
import com.inov8.microbank.common.util.MessageParsingUtils;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.SmsSender;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.server.dao.accounttypemodule.AccountTypeDAO;
import com.inov8.microbank.server.dao.allpaymodule.AllPayUserInfoListViewDAO;
import com.inov8.microbank.server.dao.currencycodemodule.CurrencyCodeDAO;
import com.inov8.microbank.server.dao.mfsmodule.UserDeviceAccountsDAO;
import com.inov8.microbank.server.dao.portal.changeaccnickmodule.ChangeAccountNickListViewDAO;
import com.inov8.microbank.server.dao.portal.linkpaymentmodemodule.LinkPaymentModeDAO;
import com.inov8.microbank.server.dao.portal.mfsaccountmodule.UserInfoListViewDAO;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;
import com.inov8.microbank.server.dao.smartmoneymodule.SmartMoneyAccountDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.model.LogModel;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl;

public class LinkPaymentModeManagerImpl implements LinkPaymentModeManager {
	
	private final static Logger logger = Logger.getLogger(LinkPaymentModeManagerImpl.class);

	private AllPayUserInfoListViewDAO allpayUserInfoListViewDAO;

	private LinkPaymentModeDAO linkPaymentModeDAO;

	private ActionLogManager actionLogManager;

	private UserDeviceAccountsManager userDeviceAccountsManager;

	private FinancialIntegrationManager financialIntegrationManager;

	private SmsSender smsSender;

	private ChangeAccountNickListViewDAO changeAccountNickListViewDAO;

	private SmartMoneyAccountDAO smartMoneyAccountDAO;

	private UserInfoListViewDAO userInfoListViewDAO;

	private CurrencyCodeDAO currencyCodeDAO;

	private AppUserDAO appUserDAO;

	private UserDeviceAccountsDAO userDeviceAccountsDAO;

	private AccountTypeDAO accountTypeDAO;

	/**
	 * @param smartMoneyAccountDAO
	 *            the smartMoneyAccountDAO to set
	 */
	public void setSmartMoneyAccountDAO(SmartMoneyAccountDAO smartMoneyAccountDAO) {
		this.smartMoneyAccountDAO = smartMoneyAccountDAO;
	}

	public void setSmsSender(SmsSender smsSender) {
		this.smsSender = smsSender;
	}

	public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager) {
		this.financialIntegrationManager = financialIntegrationManager;
	}

	public void setUserDeviceAccountsManager(UserDeviceAccountsManager userDeviceAccountsManager) {
		this.userDeviceAccountsManager = userDeviceAccountsManager;
	}

	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}

	public BaseWrapper loadUserInfoListViewByPrimaryKey(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		UserInfoListViewModel userInfoListViewModel = (UserInfoListViewModel) baseWrapper.getBasePersistableModel();
		userInfoListViewModel = this.userInfoListViewDAO.findByPrimaryKey(userInfoListViewModel.getAppUserId());
		baseWrapper.setBasePersistableModel(userInfoListViewModel);
		return baseWrapper;
	}

	public BaseWrapper loadUserInfoListViewByMfsId(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		UserInfoListViewModel userInfoListViewModel = (UserInfoListViewModel) baseWrapper.getBasePersistableModel();
		CustomList<UserInfoListViewModel> cList = this.userInfoListViewDAO.findByExample(userInfoListViewModel);
		List<UserInfoListViewModel> list = cList.getResultsetList();
		userInfoListViewModel = list.get(0);
		baseWrapper.setBasePersistableModel(userInfoListViewModel);
		return baseWrapper;
	}

	public BaseWrapper createLinkPaymentMode(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		long useCaseId = 0;
		long actionId = 0;
		long appUserId = 0;

		actionId = (Long) baseWrapper.getObject(PortalConstants.KEY_ACTION_ID);
		useCaseId = (Long) baseWrapper.getObject(PortalConstants.KEY_USECASE_ID);

		ActionLogModel actionLogModel = new ActionLogModel();
		actionLogModel = logBeforeAction(actionId, useCaseId/* ,appUserId */);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

		logger.info("after theread local");

		AbstractFinancialInstitution abstractFinancialInstitution = null;
		VeriflyBaseWrapper veriflyBaseWrapper = null;
		SmartMoneyAccountModel smartMoneyAccountModel = null;
		boolean phoenixSuccessful = false;
		boolean firstSmartMoneyAccount = false;
		try { // Start TRY block
			String errorMessage;
			String messageString;
			LinkPaymentModeModel linkPaymentModeModel = (LinkPaymentModeModel) baseWrapper.getObject("linkPaymentModeModel");
			UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
			userDeviceAccountsModel.setUserId(linkPaymentModeModel.getMfsId());
			BaseWrapper baseWrapperUserDevice = new BaseWrapperImpl();
			baseWrapperUserDevice.setBasePersistableModel(userDeviceAccountsModel);
			baseWrapperUserDevice = userDeviceAccountsManager.loadUserDeviceAccount(baseWrapperUserDevice);
			userDeviceAccountsModel = (UserDeviceAccountsModel) baseWrapperUserDevice.getBasePersistableModel();

			// check if customer exist or not
			if (userDeviceAccountsModel.getAppUserIdAppUserModel() == null) {
				baseWrapperUserDevice.putObject("ErrMessage", "Agent ID does not exist");
				throw new FrameworkCheckedException("Agent ID does not exist");
			}

			// customer account enable or disable
			if (!userDeviceAccountsModel.getAccountEnabled()) {
				baseWrapperUserDevice.putObject("ErrMessage", "Account is disabled.");
				throw new FrameworkCheckedException("Account is disabled.");
			}

			// customer account for expired
			if (userDeviceAccountsModel.getAccountExpired()) {
				baseWrapperUserDevice.putObject("ErrMessage", "Account is expired");
				throw new FrameworkCheckedException("Account is expired");
			}

			// customer account for locked
			if (userDeviceAccountsModel.getAccountLocked()) {
				baseWrapperUserDevice.putObject("ErrMessage", "Account is locked");
				throw new FrameworkCheckedException("Account is locked");
			}

			// customer account for credential expired
			if (userDeviceAccountsModel.getCredentialsExpired()) {
				baseWrapperUserDevice.putObject("ErrMessage", "Account credential is expired");
				throw new FrameworkCheckedException("Account credential is expired");
			}

			// Check if this is the first SMA of customer
			boolean isFirstSMA = true;
			if (userDeviceAccountsModel.getAppUserIdAppUserModel().getCustomerId() != null)
				isFirstSMA = isFirstSmartMoneyAccount(userDeviceAccountsModel.getAppUserIdAppUserModel().getCustomerId(),AppRoleConstantsInterface.CUSTOMER);
			else if (userDeviceAccountsModel.getAppUserIdAppUserModel().getRetailerContactId() != null)
				isFirstSMA = isFirstSmartMoneyAccount(userDeviceAccountsModel.getAppUserIdAppUserModel().getRetailerContactId(),AppRoleConstantsInterface.RETAILER);
			else if (userDeviceAccountsModel.getAppUserIdAppUserModel().getDistributorContactId() != null)
				isFirstSMA = isFirstSmartMoneyAccount(userDeviceAccountsModel.getAppUserIdAppUserModel().getDistributorContactId(),AppRoleConstantsInterface.DISTRIBUTOR);
			appUserId = userDeviceAccountsModel.getAppUserId();

			if (userDeviceAccountsModel.getAppUserIdAppUserModel().getNic() == null) {
				userDeviceAccountsModel.getAppUserIdAppUserModel().setNic(linkPaymentModeModel.getNic());
				baseWrapperUserDevice.setBasePersistableModel(userDeviceAccountsModel);
				baseWrapperUserDevice = this.userDeviceAccountsManager.updateUserDeviceAccount(baseWrapperUserDevice);
			}

			BankModel bankModel = new BankModel();
			bankModel.setBankId(UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankId());

			BaseWrapper baseWrapperBank = new BaseWrapperImpl();
			baseWrapperBank.setBasePersistableModel(bankModel);
			abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperBank);
			String mobileno = userDeviceAccountsModel.getAppUserIdAppUserModel().getMobileNo();

			/**
			 * Smart Money Account is created here
			 */
			// TODO: this smartmoney account creation has to go below verifly
			smartMoneyAccountModel = createSmartMoneyAccountModel(linkPaymentModeModel, userDeviceAccountsModel, abstractFinancialInstitution.isVeriflyRequired());

			/**
			 * Everything related the verifly is done in this IF block
			 */
			if (abstractFinancialInstitution.isVeriflyRequired()
					&& !(PaymentModeConstantsInterface.CASH.equals(linkPaymentModeModel.getPaymentMode()) || PaymentModeConstantsInterface.CREDIT_REGISTER
							.equals(linkPaymentModeModel.getPaymentMode()))) {

				AccountInfoModel accountInfoModel = crAccountModel(linkPaymentModeModel, userDeviceAccountsModel, smartMoneyAccountModel);

				veriflyBaseWrapper = new VeriflyBaseWrapperImpl();

				// VeriflyManager veriflyMgr =
				// veriflyManagerService.getVeriflyMgrByBankId(bankModel);
				veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
				LogModel logmodel = new LogModel();
				logmodel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
				logmodel.setCreatedBy(UserUtils.getCurrentUser().getFirstName());
				veriflyBaseWrapper.setLogModel(logmodel);
				veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
				veriflyBaseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB);

				// ..: Code to be changed here :..
				// veriflyBaseWrapper =
				// veriflyMgr.generatePIN(veriflyBaseWrapper);
				veriflyBaseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB);
				veriflyBaseWrapper.setSkipPanCheck(Boolean.TRUE);

				veriflyBaseWrapper = abstractFinancialInstitution.generatePin(veriflyBaseWrapper);

				if (veriflyBaseWrapper.isErrorStatus()) {

					Object[] args = { veriflyBaseWrapper.getAccountInfoModel().getAccountNick(), veriflyBaseWrapper.getAccountInfoModel().getAccountNick(),
							veriflyBaseWrapper.getAccountInfoModel().getGeneratedPin() };
					// messageString =
					// MessageUtil.getMessage("linkPaymentMode.successMessage",
					// args);
					/**
					 * Phoenix call if verifly Lite (bank bank) is required. And
					 * First Account is being created.
					 */
					Long finIntegrationId = UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankIdBankModel().getFinancialIntegrationId();
					if (abstractFinancialInstitution.isVeriflyLite() && FinancialInstitutionConstants.OLA_FINANCIAL_INSTITUTION.longValue() != finIntegrationId.longValue()) {
						if (isFirstSMA) {
							firstSmartMoneyAccount = true;
							// if(abstractFinancialInstitution.isIVRChannelActive(linkPaymentModeModel.getAccountNo(),
							// linkPaymentModeModel.getAccountType().toString(),
							// linkPaymentModeModel.getCurrencyCode().toString(),linkPaymentModeModel.getNic()))
							// {
							// String
							// title=abstractFinancialInstitution.getAccountTitle(linkPaymentModeModel.getAccountNo(),
							// linkPaymentModeModel.getAccountType().toString(),
							// linkPaymentModeModel.getCurrencyCode().toString(),
							// linkPaymentModeModel.getNic());

							if (abstractFinancialInstitution.activateDeliveryChannel(linkPaymentModeModel.getAccountType().toString(), linkPaymentModeModel.getNic())) {
								// messageString =
								// MessageUtil.getMessage("linkPaymentMode.bankfirstaccount.successMessage",
								// args);
								phoenixSuccessful = true;

							} else {
								phoenixSuccessful = false;
								throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.PHOENIX_ACT_CHANNEL_REQ_FAILED);

							}
							// }
							// else
							// {
							//								
							// throw new FrameworkCheckedException
							// ("linkPaymentMode.customerprofiledoesnotexist");
							// }

						}

						/*
						 * else { firstSmartMoneyAccount = false; String
						 * title=abstractFinancialInstitution.getAccountTitle(linkPaymentModeModel.getAccountNo(),
						 * linkPaymentModeModel.getAccountType().toString(),
						 * linkPaymentModeModel.getCurrencyCode().toString(),
						 * linkPaymentModeModel.getNic()); if(title!= null) {
						 * //messageString =
						 * MessageUtil.getMessage("linkPaymentMode.bankaccount.successMessage",
						 * args); phoenixSuccessful = true; } else {
						 * phoenixSuccessful = false; throw new
						 * FrameworkCheckedException
						 * ("linkPaymentMode.customerprofiledoesnotexist"); }
						 * 
						 *  }
						 */

						if (firstSmartMoneyAccount) {
							if (userDeviceAccountsModel.getDeviceTypeId().longValue() == DeviceTypeConstantsInterface.ALL_PAY.longValue()) {
								if (smartMoneyAccountModel.getPaymentModeId().longValue() == PaymentModeConstantsInterface.CREDIT_CARD.longValue())
									messageString = MessageUtil.getMessage("allpayLinkPaymentModeCC.bankfirstaccount.successMessage", args);
								else
									messageString = MessageUtil.getMessage("allpayLinkPaymentModeBA.bankfirstaccount.successMessage", args);
							} else {
								if (smartMoneyAccountModel.getPaymentModeId().longValue() == PaymentModeConstantsInterface.CREDIT_CARD.longValue())
									messageString = MessageUtil.getMessage("linkPaymentModeCC.bankfirstaccount.successMessage", args);
								else
									messageString = MessageUtil.getMessage("linkPaymentModeBA.bankfirstaccount.successMessage", args);
							}
						} else {
							if (userDeviceAccountsModel.getDeviceTypeId().longValue() == DeviceTypeConstantsInterface.ALL_PAY.longValue()) {
								if (smartMoneyAccountModel.getPaymentModeId().longValue() == PaymentModeConstantsInterface.CREDIT_CARD.longValue())
									messageString = MessageUtil.getMessage("allpayLinkPaymentModeCC.bankaccount.successMessage", args);
								else
									messageString = MessageUtil.getMessage("allPaylinkPaymentModeBA.bankaccount.successMessage", args);
							} else {
								if (smartMoneyAccountModel.getPaymentModeId().longValue() == PaymentModeConstantsInterface.CREDIT_CARD.longValue())
									messageString = MessageUtil.getMessage("linkPaymentModeCC.bankaccount.successMessage", args);
								else
									messageString = MessageUtil.getMessage("linkPaymentModeBA.bankaccount.successMessage", args);
							}

							// messageString =
							// MessageUtil.getMessage("linkPaymentMode.bankaccount.successMessage",
							// args);
						}

					} else {
						if (userDeviceAccountsModel.getDeviceTypeId().longValue() == DeviceTypeConstantsInterface.ALL_PAY.longValue()) {
							messageString = MessageUtil.getMessage("allpaylinkPaymentMode.successMessage", args);
						} else {
							messageString = MessageUtil.getMessage("linkPaymentMode.successMessage", args);
						}

					}

					SmsMessage smsMessage = new SmsMessage(mobileno, messageString);

					// For ALLPAY users
					if (null != userDeviceAccountsModel && userDeviceAccountsModel.getCommissioned()) {
						smsMessage.setMessageText(MessageParsingUtils.parseMessageForIpos(smsMessage.getMessageText()));
					}
					smsSender.send(smsMessage);

					actionLogModel.setCustomField1(String.valueOf(appUserId));
					actionLogModel.setCustomField11(linkPaymentModeModel.getMfsId());
					logAfterAction(actionLogModel);
					ThreadLocalActionLog.remove();

				} else {

					errorMessage = veriflyBaseWrapper.getErrorMessage();
					baseWrapper.putObject("ErrMessage", errorMessage);

					linkPaymentModeDAO.delete(smartMoneyAccountModel);

					logger.info(">>>>>>>>>>>>>>> Record not saved in verifly ");
					
					if(errorMessage == null || "".equals(errorMessage)) {
						errorMessage = "The Account Number is already Linked. Kindly Select Another Account Number and Try Again.";
					}
					if(errorMessage.contains("Account No already exists")) {
						errorMessage = "The Account Number is already Linked. Kindly Select Another Account Number and Try Again.";
					}
								
					throw new FrameworkCheckedException(errorMessage);
				}

			}

			else {
				// Object[] args =
				// {UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankIdBankModel().getName()};
				SmsMessage smsMessage=null;
				if (userDeviceAccountsModel.getDeviceTypeId().longValue() == DeviceTypeConstantsInterface.ALL_PAY.longValue()) {			
					smsMessage= new SmsMessage(mobileno, MessageUtil.getMessage("allpayLinkPaymentMode.nonverifly.successMessage"));
				}else{
					smsMessage= new SmsMessage(mobileno, MessageUtil.getMessage("linkPaymentMode.nonverifly.successMessage"));
				}
				if (null != userDeviceAccountsModel && userDeviceAccountsModel.getCommissioned()) {
					smsMessage.setMessageText(MessageParsingUtils.parseMessageForIpos(smsMessage.getMessageText()));
				}
				smsSender.send(smsMessage);
			}

		} // End TRY Block
		catch (ImplementationNotSupportedException inse) {
			throw new FrameworkCheckedException("implementationNotSupportedException");
		} catch (Exception ex) {
			ex.printStackTrace();
			String errMessage = ex.getMessage();
			baseWrapper.putObject("ErrMessage", errMessage);
			// if(ex instanceof FrameworkCheckedException){
			// if(ex.getMessage().equals("Error Message. Phoenix activation
			// failed")){
			// // Phoenix activation has failed
			// }
			// else{
			// // Phoenix activation has not failed, somehow other exception has
			// occured,
			// // any way have to deactivate Phoenix.
			// }
			// }

			// This is a tuch to handle the smartmoney account created in case
			// of verifly exception.
			// this is because the transaction is not getting rolled back
			if (smartMoneyAccountModel != null && smartMoneyAccountModel.getSmartMoneyAccountId() != null) {
				logger.info("<<<<<<<<<< Delete the smartmoeny account as exception has occured >>>>>>>>>");
				try{
				this.linkPaymentModeDAO.deleteByPrimaryKey(smartMoneyAccountModel.getSmartMoneyAccountId());
				}catch(Exception dataExp){
					logger.error(dataExp.getMessage(),dataExp);
				}
				logger.info("<<<<<<<<<< Smartmoeny account is Deleted >>>>>>>>>");
			}
			if (/* firstSmartMoneyAccount && */!phoenixSuccessful) {
				// phoenix had a problem, verifly was hit, so rollback verifly.
				// TODO: here code to delete the verifly account
				try {
					abstractFinancialInstitution.deleteAccount(veriflyBaseWrapper);
				}catch(Exception dataExp){
					logger.error(dataExp.getMessage(),dataExp);
				}
				logger.info("Phoenix had a problem");
			}

			throw new FrameworkCheckedException(ex.getMessage(), ex);
		}

		return baseWrapper;
	}

	@Override
	public BaseWrapper createLinkPaymentModeForL3(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		long useCaseId = 0;
		long actionId = 0;
		long appUserId = 0;

		AbstractFinancialInstitution abstractFinancialInstitution = null;
		VeriflyBaseWrapper veriflyBaseWrapper = null;
		SmartMoneyAccountModel smartMoneyAccountModel = null;
		boolean phoenixSuccessful = false;
		boolean firstSmartMoneyAccount = false;
		try { // Start TRY block
			String errorMessage;
			String messageString;
			LinkPaymentModeModel linkPaymentModeModel = (LinkPaymentModeModel) baseWrapper.getObject("linkPaymentModeModel");
			UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
			userDeviceAccountsModel.setUserId(linkPaymentModeModel.getMfsId());
			BaseWrapper baseWrapperUserDevice = new BaseWrapperImpl();
			baseWrapperUserDevice.setBasePersistableModel(userDeviceAccountsModel);
			baseWrapperUserDevice = userDeviceAccountsManager.loadUserDeviceAccount(baseWrapperUserDevice);
			userDeviceAccountsModel = (UserDeviceAccountsModel) baseWrapperUserDevice.getBasePersistableModel();

			// check if customer exist or not
			if (userDeviceAccountsModel.getAppUserIdAppUserModel() == null) {
				baseWrapperUserDevice.putObject("ErrMessage", "Agent ID does not exist");
				throw new FrameworkCheckedException("Agent ID does not exist");
			}

			// customer account enable or disable
			if (!userDeviceAccountsModel.getAccountEnabled()) {
				baseWrapperUserDevice.putObject("ErrMessage", "Account is disabled.");
				throw new FrameworkCheckedException("Account is disabled.");
			}

			// customer account for expired
			if (userDeviceAccountsModel.getAccountExpired()) {
				baseWrapperUserDevice.putObject("ErrMessage", "Account is expired");
				throw new FrameworkCheckedException("Account is expired");
			}

			// customer account for locked
			if (userDeviceAccountsModel.getAccountLocked()) {
				baseWrapperUserDevice.putObject("ErrMessage", "Account is locked");
				throw new FrameworkCheckedException("Account is locked");
			}

			// customer account for credential expired
			if (userDeviceAccountsModel.getCredentialsExpired()) {
				baseWrapperUserDevice.putObject("ErrMessage", "Account credential is expired");
				throw new FrameworkCheckedException("Account credential is expired");
			}

			// Check if this is the first SMA of customer
			boolean isFirstSMA = true;
			if (userDeviceAccountsModel.getAppUserIdAppUserModel().getCustomerId() != null)
				isFirstSMA = isFirstSmartMoneyAccount(userDeviceAccountsModel.getAppUserIdAppUserModel().getCustomerId(),AppRoleConstantsInterface.CUSTOMER);
			else if (userDeviceAccountsModel.getAppUserIdAppUserModel().getRetailerContactId() != null)
				isFirstSMA = isFirstSmartMoneyAccount(userDeviceAccountsModel.getAppUserIdAppUserModel().getRetailerContactId(),AppRoleConstantsInterface.RETAILER);
			else if (userDeviceAccountsModel.getAppUserIdAppUserModel().getDistributorContactId() != null)
				isFirstSMA = isFirstSmartMoneyAccount(userDeviceAccountsModel.getAppUserIdAppUserModel().getDistributorContactId(),AppRoleConstantsInterface.DISTRIBUTOR);
			appUserId = userDeviceAccountsModel.getAppUserId();

			if (userDeviceAccountsModel.getAppUserIdAppUserModel().getNic() == null) {
				userDeviceAccountsModel.getAppUserIdAppUserModel().setNic(linkPaymentModeModel.getNic());
				baseWrapperUserDevice.setBasePersistableModel(userDeviceAccountsModel);
				baseWrapperUserDevice = this.userDeviceAccountsManager.updateUserDeviceAccount(baseWrapperUserDevice);
			}

			BankModel bankModel = new BankModel();
			bankModel.setBankId(UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankId());

			BaseWrapper baseWrapperBank = new BaseWrapperImpl();
			baseWrapperBank.setBasePersistableModel(bankModel);
			abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperBank);
			String mobileno = userDeviceAccountsModel.getAppUserIdAppUserModel().getMobileNo();

			/**
			 * Smart Money Account is created here
			 */
			// TODO: this smartmoney account creation has to go below verifly
			smartMoneyAccountModel = createSmartMoneyAccountModel(linkPaymentModeModel, userDeviceAccountsModel, abstractFinancialInstitution.isVeriflyRequired());

			/**
			 * Everything related the verifly is done in this IF block
			 */
			if (abstractFinancialInstitution.isVeriflyRequired()
					&& !(PaymentModeConstantsInterface.CASH.equals(linkPaymentModeModel.getPaymentMode()) || PaymentModeConstantsInterface.CREDIT_REGISTER
							.equals(linkPaymentModeModel.getPaymentMode()))) {

				AccountInfoModel accountInfoModel = crAccountModel(linkPaymentModeModel, userDeviceAccountsModel, smartMoneyAccountModel);

				veriflyBaseWrapper = new VeriflyBaseWrapperImpl();

				// VeriflyManager veriflyMgr =
				// veriflyManagerService.getVeriflyMgrByBankId(bankModel);
				veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
				LogModel logmodel = new LogModel();
				logmodel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
				logmodel.setCreatedBy(UserUtils.getCurrentUser().getFirstName());
				veriflyBaseWrapper.setLogModel(logmodel);
				veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
				veriflyBaseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB);

				// ..: Code to be changed here :..
				// veriflyBaseWrapper =
				// veriflyMgr.generatePIN(veriflyBaseWrapper);
				veriflyBaseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB);
				veriflyBaseWrapper.setSkipPanCheck(Boolean.TRUE);

				veriflyBaseWrapper = abstractFinancialInstitution.generatePin(veriflyBaseWrapper);

				if (veriflyBaseWrapper.isErrorStatus()) {

					Object[] args = { veriflyBaseWrapper.getAccountInfoModel().getAccountNick(), veriflyBaseWrapper.getAccountInfoModel().getAccountNick(),
							veriflyBaseWrapper.getAccountInfoModel().getGeneratedPin() };
					// messageString =
					// MessageUtil.getMessage("linkPaymentMode.successMessage",
					// args);
					/**
					 * Phoenix call if verifly Lite (bank bank) is required. And
					 * First Account is being created.
					 */
					Long finIntegrationId = UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankIdBankModel().getFinancialIntegrationId();
					if (abstractFinancialInstitution.isVeriflyLite() && FinancialInstitutionConstants.OLA_FINANCIAL_INSTITUTION.longValue() != finIntegrationId.longValue()) {
						if (isFirstSMA) {
							firstSmartMoneyAccount = true;
							// if(abstractFinancialInstitution.isIVRChannelActive(linkPaymentModeModel.getAccountNo(),
							// linkPaymentModeModel.getAccountType().toString(),
							// linkPaymentModeModel.getCurrencyCode().toString(),linkPaymentModeModel.getNic()))
							// {
							// String
							// title=abstractFinancialInstitution.getAccountTitle(linkPaymentModeModel.getAccountNo(),
							// linkPaymentModeModel.getAccountType().toString(),
							// linkPaymentModeModel.getCurrencyCode().toString(),
							// linkPaymentModeModel.getNic());

							if (abstractFinancialInstitution.activateDeliveryChannel(linkPaymentModeModel.getAccountType().toString(), linkPaymentModeModel.getNic())) {
								// messageString =
								// MessageUtil.getMessage("linkPaymentMode.bankfirstaccount.successMessage",
								// args);
								phoenixSuccessful = true;

							} else {
								phoenixSuccessful = false;
								throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.PHOENIX_ACT_CHANNEL_REQ_FAILED);

							}
							// }
							// else
							// {
							//								
							// throw new FrameworkCheckedException
							// ("linkPaymentMode.customerprofiledoesnotexist");
							// }

						}

						/*
						 * else { firstSmartMoneyAccount = false; String
						 * title=abstractFinancialInstitution.getAccountTitle(linkPaymentModeModel.getAccountNo(),
						 * linkPaymentModeModel.getAccountType().toString(),
						 * linkPaymentModeModel.getCurrencyCode().toString(),
						 * linkPaymentModeModel.getNic()); if(title!= null) {
						 * //messageString =
						 * MessageUtil.getMessage("linkPaymentMode.bankaccount.successMessage",
						 * args); phoenixSuccessful = true; } else {
						 * phoenixSuccessful = false; throw new
						 * FrameworkCheckedException
						 * ("linkPaymentMode.customerprofiledoesnotexist"); }
						 * 
						 *  }
						 */

						if (firstSmartMoneyAccount) {
							if (userDeviceAccountsModel.getDeviceTypeId().longValue() == DeviceTypeConstantsInterface.ALL_PAY.longValue()) {
								if (smartMoneyAccountModel.getPaymentModeId().longValue() == PaymentModeConstantsInterface.CREDIT_CARD.longValue())
									messageString = MessageUtil.getMessage("allpayLinkPaymentModeCC.bankfirstaccount.successMessage", args);
								else
									messageString = MessageUtil.getMessage("allpayLinkPaymentModeBA.bankfirstaccount.successMessage", args);
							} else {
								if (smartMoneyAccountModel.getPaymentModeId().longValue() == PaymentModeConstantsInterface.CREDIT_CARD.longValue())
									messageString = MessageUtil.getMessage("linkPaymentModeCC.bankfirstaccount.successMessage", args);
								else
									messageString = MessageUtil.getMessage("linkPaymentModeBA.bankfirstaccount.successMessage", args);
							}
						} else {
							if (userDeviceAccountsModel.getDeviceTypeId().longValue() == DeviceTypeConstantsInterface.ALL_PAY.longValue()) {
								if (smartMoneyAccountModel.getPaymentModeId().longValue() == PaymentModeConstantsInterface.CREDIT_CARD.longValue())
									messageString = MessageUtil.getMessage("allpayLinkPaymentModeCC.bankaccount.successMessage", args);
								else
									messageString = MessageUtil.getMessage("allPaylinkPaymentModeBA.bankaccount.successMessage", args);
							} else {
								if (smartMoneyAccountModel.getPaymentModeId().longValue() == PaymentModeConstantsInterface.CREDIT_CARD.longValue())
									messageString = MessageUtil.getMessage("linkPaymentModeCC.bankaccount.successMessage", args);
								else
									messageString = MessageUtil.getMessage("linkPaymentModeBA.bankaccount.successMessage", args);
							}

							// messageString =
							// MessageUtil.getMessage("linkPaymentMode.bankaccount.successMessage",
							// args);
						}

					} else {
						if (userDeviceAccountsModel.getDeviceTypeId().longValue() == DeviceTypeConstantsInterface.ALL_PAY.longValue()) {
							messageString = MessageUtil.getMessage("allpaylinkPaymentMode.successMessage", args);
						} else {
							messageString = MessageUtil.getMessage("linkPaymentMode.successMessage", args);
						}

					}

					SmsMessage smsMessage = new SmsMessage(mobileno, messageString);

					// For ALLPAY users
					if (null != userDeviceAccountsModel && userDeviceAccountsModel.getCommissioned()) {
						smsMessage.setMessageText(MessageParsingUtils.parseMessageForIpos(smsMessage.getMessageText()));
					}
					smsSender.send(smsMessage);
				} else {

					errorMessage = veriflyBaseWrapper.getErrorMessage();
					baseWrapper.putObject("ErrMessage", errorMessage);

					linkPaymentModeDAO.delete(smartMoneyAccountModel);

					logger.info(">>>>>>>>>>>>>>> Record not saved in verifly ");
					
					if(errorMessage == null || "".equals(errorMessage)) {
						errorMessage = "The Account Number is already Linked. Kindly Select Another Account Number and Try Again.";
					}
					if(errorMessage.contains("Account No already exists")) {
						errorMessage = "The Account Number is already Linked. Kindly Select Another Account Number and Try Again.";
					}
								
					throw new FrameworkCheckedException(errorMessage);
				}

			}

			else {
				// Object[] args =
				// {UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankIdBankModel().getName()};
				SmsMessage smsMessage=null;
				if (userDeviceAccountsModel.getDeviceTypeId().longValue() == DeviceTypeConstantsInterface.ALL_PAY.longValue()) {			
					smsMessage= new SmsMessage(mobileno, MessageUtil.getMessage("allpayLinkPaymentMode.nonverifly.successMessage"));
				}else{
					smsMessage= new SmsMessage(mobileno, MessageUtil.getMessage("linkPaymentMode.nonverifly.successMessage"));
				}
				if (null != userDeviceAccountsModel && userDeviceAccountsModel.getCommissioned()) {
					smsMessage.setMessageText(MessageParsingUtils.parseMessageForIpos(smsMessage.getMessageText()));
				}
				smsSender.send(smsMessage);
			}

		} // End TRY Block
		catch (ImplementationNotSupportedException inse) {
			throw new FrameworkCheckedException("implementationNotSupportedException");
		} catch (Exception ex) {
			ex.printStackTrace();
			String errMessage = ex.getMessage();
			baseWrapper.putObject("ErrMessage", errMessage);
			// if(ex instanceof FrameworkCheckedException){
			// if(ex.getMessage().equals("Error Message. Phoenix activation
			// failed")){
			// // Phoenix activation has failed
			// }
			// else{
			// // Phoenix activation has not failed, somehow other exception has
			// occured,
			// // any way have to deactivate Phoenix.
			// }
			// }

			// This is a tuch to handle the smartmoney account created in case
			// of verifly exception.
			// this is because the transaction is not getting rolled back
			if (smartMoneyAccountModel != null && smartMoneyAccountModel.getSmartMoneyAccountId() != null) {
				logger.info("<<<<<<<<<< Delete the smartmoeny account as exception has occured >>>>>>>>>");
				try{
				this.linkPaymentModeDAO.deleteByPrimaryKey(smartMoneyAccountModel.getSmartMoneyAccountId());
				}catch(Exception dataExp){
					logger.error(dataExp.getMessage(),dataExp);
				}
				logger.info("<<<<<<<<<< Smartmoeny account is Deleted >>>>>>>>>");
			}
			if (/* firstSmartMoneyAccount && */!phoenixSuccessful) {
				// phoenix had a problem, verifly was hit, so rollback verifly.
				// TODO: here code to delete the verifly account
				try {
					abstractFinancialInstitution.deleteAccount(veriflyBaseWrapper);
				}catch(Exception dataExp){
					logger.error(dataExp.getMessage(),dataExp);
				}
				logger.info("Phoenix had a problem");
			}

			throw new FrameworkCheckedException(ex.getMessage(), ex);
		}

		return baseWrapper;
	}
	
	
	
	public BaseWrapper createLinkPaymentModeForBulk(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		long useCaseId = 0;
		long actionId = 0;
		long appUserId = 0;

		actionId = (Long) baseWrapper.getObject(PortalConstants.KEY_ACTION_ID);
		useCaseId = (Long) baseWrapper.getObject(PortalConstants.KEY_USECASE_ID);

		ActionLogModel actionLogModel = new ActionLogModel();
		actionLogModel = logBeforeAction(actionId, useCaseId/* ,appUserId */);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

		logger.info("after theread local");

		AbstractFinancialInstitution abstractFinancialInstitution = null;
		VeriflyBaseWrapper veriflyBaseWrapper = null;
		SmartMoneyAccountModel smartMoneyAccountModel = null;
		boolean phoenixSuccessful = false;
		boolean firstSmartMoneyAccount = false;
		try { // Start TRY block
			String errorMessage;
			String messageString;
			LinkPaymentModeModel linkPaymentModeModel = (LinkPaymentModeModel) baseWrapper.getObject("linkPaymentModeModel");
			UserDeviceAccountsModel userDeviceAccountsModel = (UserDeviceAccountsModel)baseWrapper.getObject("userDeviceAccountsModel");
			AppUserModel appUserModel = (AppUserModel)baseWrapper.getObject("appUserModel");
			BankModel bankModel = (BankModel)baseWrapper.getObject("bankModel");
			userDeviceAccountsModel.setAppUserIdAppUserModel(appUserModel);
			
			boolean isFirstSMA = true;
			appUserId = userDeviceAccountsModel.getAppUserId();

			
			BaseWrapper baseWrapperBank = new BaseWrapperImpl();
			baseWrapperBank.setBasePersistableModel(bankModel);
			abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperBank);
			String mobileno = appUserModel.getMobileNo();

			/**
			 * Smart Money Account is created here
			 */
			// TODO: this smartmoney account creation has to go below verifly
			smartMoneyAccountModel = createSmartMoneyAccountModelForBulkAgent(linkPaymentModeModel, userDeviceAccountsModel, abstractFinancialInstitution.isVeriflyRequired(), bankModel);

			/**
			 * Everything related the verifly is done in this IF block
			 */
			if (abstractFinancialInstitution.isVeriflyRequired()
					&& !(PaymentModeConstantsInterface.CASH.equals(linkPaymentModeModel.getPaymentMode()) || PaymentModeConstantsInterface.CREDIT_REGISTER
							.equals(linkPaymentModeModel.getPaymentMode()))) {

				AccountInfoModel accountInfoModel = crAccountModel(linkPaymentModeModel, userDeviceAccountsModel, smartMoneyAccountModel);

				veriflyBaseWrapper = new VeriflyBaseWrapperImpl();

				veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
				LogModel logmodel = new LogModel();
				logmodel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
				logmodel.setCreatedBy(UserUtils.getCurrentUser().getFirstName());
				veriflyBaseWrapper.setLogModel(logmodel);
				veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
				veriflyBaseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB);

				veriflyBaseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB);

				veriflyBaseWrapper = abstractFinancialInstitution.generatePin(veriflyBaseWrapper);

				if (veriflyBaseWrapper.isErrorStatus()) {

					Object[] args = { veriflyBaseWrapper.getAccountInfoModel().getAccountNick(), veriflyBaseWrapper.getAccountInfoModel().getAccountNick(),
							veriflyBaseWrapper.getAccountInfoModel().getGeneratedPin() };
					
					Long finIntegrationId = bankModel.getFinancialIntegrationId();
					if (abstractFinancialInstitution.isVeriflyLite() && FinancialInstitutionConstants.OLA_FINANCIAL_INSTITUTION.longValue() != finIntegrationId.longValue()) {
						if (isFirstSMA) {
							firstSmartMoneyAccount = true;
							
							if (abstractFinancialInstitution.activateDeliveryChannel(linkPaymentModeModel.getAccountType().toString(), linkPaymentModeModel.getNic())) {
								phoenixSuccessful = true;

							} else {
								phoenixSuccessful = false;
								throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.PHOENIX_ACT_CHANNEL_REQ_FAILED);

							}
						}

						if (firstSmartMoneyAccount) {
							if (userDeviceAccountsModel.getDeviceTypeId().longValue() == DeviceTypeConstantsInterface.ALL_PAY.longValue()) {
								if (smartMoneyAccountModel.getPaymentModeId().longValue() == PaymentModeConstantsInterface.CREDIT_CARD.longValue())
									messageString = MessageUtil.getMessage("allpayLinkPaymentModeCC.bankfirstaccount.successMessage", args);
								else
									messageString = MessageUtil.getMessage("allpayLinkPaymentModeBA.bankfirstaccount.successMessage", args);
							} else {
								if (smartMoneyAccountModel.getPaymentModeId().longValue() == PaymentModeConstantsInterface.CREDIT_CARD.longValue())
									messageString = MessageUtil.getMessage("linkPaymentModeCC.bankfirstaccount.successMessage", args);
								else
									messageString = MessageUtil.getMessage("linkPaymentModeBA.bankfirstaccount.successMessage", args);
							}
						} else {
							if (userDeviceAccountsModel.getDeviceTypeId().longValue() == DeviceTypeConstantsInterface.ALL_PAY.longValue()) {
								if (smartMoneyAccountModel.getPaymentModeId().longValue() == PaymentModeConstantsInterface.CREDIT_CARD.longValue())
									messageString = MessageUtil.getMessage("allpayLinkPaymentModeCC.bankaccount.successMessage", args);
								else
									messageString = MessageUtil.getMessage("allPaylinkPaymentModeBA.bankaccount.successMessage", args);
							} else {
								if (smartMoneyAccountModel.getPaymentModeId().longValue() == PaymentModeConstantsInterface.CREDIT_CARD.longValue())
									messageString = MessageUtil.getMessage("linkPaymentModeCC.bankaccount.successMessage", args);
								else
									messageString = MessageUtil.getMessage("linkPaymentModeBA.bankaccount.successMessage", args);
							}

						}

					} else {
						if (userDeviceAccountsModel.getDeviceTypeId().longValue() == DeviceTypeConstantsInterface.ALL_PAY.longValue()) {
							messageString = MessageUtil.getMessage("allpaylinkPaymentMode.successMessage", args);
						} else {
							messageString = MessageUtil.getMessage("linkPaymentMode.successMessage", args);
						}

					}

					SmsMessage smsMessage = new SmsMessage(mobileno, messageString);

					// For ALLPAY users
					if (null != userDeviceAccountsModel && userDeviceAccountsModel.getCommissioned()) {
						smsMessage.setMessageText(MessageParsingUtils.parseMessageForIpos(smsMessage.getMessageText()));
					}
					smsSender.send(smsMessage);

					actionLogModel.setCustomField1(String.valueOf(appUserId));
					actionLogModel.setCustomField11(linkPaymentModeModel.getMfsId());
					logAfterAction(actionLogModel);
					ThreadLocalActionLog.remove();

				} else {

					errorMessage = veriflyBaseWrapper.getErrorMessage();
					baseWrapper.putObject("ErrMessage", errorMessage);

					linkPaymentModeDAO.delete(smartMoneyAccountModel);

					logger.info(">>>>>>>>>>>>>>> Record not saved in verifly ");
					
					if(errorMessage == null || "".equals(errorMessage)) {
						errorMessage = "The Account Number is already Linked. Kindly Select Another Account Number and Try Again.";
					}
					
					throw new FrameworkCheckedException(errorMessage);
				}

			}

			else 
			{
				SmsMessage smsMessage=null;
				if (userDeviceAccountsModel.getDeviceTypeId().longValue() == DeviceTypeConstantsInterface.ALL_PAY.longValue()) {			
					smsMessage= new SmsMessage(mobileno, MessageUtil.getMessage("allpayLinkPaymentMode.nonverifly.successMessage"));
				}else{
					smsMessage= new SmsMessage(mobileno, MessageUtil.getMessage("linkPaymentMode.nonverifly.successMessage"));
				}
				if (null != userDeviceAccountsModel && userDeviceAccountsModel.getCommissioned()) {
					smsMessage.setMessageText(MessageParsingUtils.parseMessageForIpos(smsMessage.getMessageText()));
				}
				smsSender.send(smsMessage);
			}

		} // End TRY Block
		catch (ImplementationNotSupportedException inse) {
			throw new FrameworkCheckedException("implementationNotSupportedException");
		} catch (Exception ex) {
			ex.printStackTrace();
			String errMessage = ex.getMessage();
			baseWrapper.putObject("ErrMessage", errMessage);

			if (smartMoneyAccountModel != null && smartMoneyAccountModel.getSmartMoneyAccountId() != null) {
				logger.info("<<<<<<<<<< Delete the smartmoeny account as exception has occured >>>>>>>>>");
				this.linkPaymentModeDAO.deleteByPrimaryKey(smartMoneyAccountModel.getSmartMoneyAccountId());
				logger.info("<<<<<<<<<< Smartmoeny account is Deleted >>>>>>>>>");
			}
			if (!phoenixSuccessful) {
				// phoenix had a problem, verifly was hit, so rollback verifly.
				// TODO: here code to delete the verifly account
				try {
					abstractFinancialInstitution.deleteAccount(veriflyBaseWrapper);
				} catch (Exception exp) {
					ex = exp;
				}
				logger.info("Phoenix had a problem");
			}

			throw new FrameworkCheckedException(ex.getMessage(), ex);
		}

		return baseWrapper;
	}
	
	
	public BaseWrapper changeAccountNick(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		Long actionId = (Long) baseWrapper.getObject(PortalConstants.KEY_ACTION_ID);
		Long useCaseId = (Long) baseWrapper.getObject(PortalConstants.KEY_USECASE_ID);
		String newAccountNick = (String) baseWrapper.getObject("newAccountNick");

		ActionLogModel actionLogModel = new ActionLogModel();
		actionLogModel = logBeforeAction(actionId, useCaseId/* ,appUserId */);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

		ChangeAccountNickListViewModel changeAccountNickListViewModel = (ChangeAccountNickListViewModel) baseWrapper.getObject("changeAccountNickListViewModel");

		// checking if new nick is already exist or not
		SmartMoneyAccountModel tmpSmartMoneyAccountModel = new SmartMoneyAccountModel();
		tmpSmartMoneyAccountModel.setName(newAccountNick);
		tmpSmartMoneyAccountModel.setCustomerId(changeAccountNickListViewModel.getCustomerId());
		ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
		exampleConfigHolderModel.setEnableLike(false);
		CustomList cList = smartMoneyAccountDAO.findByExample(tmpSmartMoneyAccountModel, null, null, exampleConfigHolderModel);
		List list = cList.getResultsetList();
		if (!list.isEmpty()) {
			throw new FrameworkCheckedException("Record already exists.");
		}

		SmartMoneyAccountModel smartMoneyAccountModel = smartMoneyAccountDAO.findByPrimaryKey(changeAccountNickListViewModel.getSmartMoneyAccountId());

		VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();

		AccountInfoModel accountInfoModel = new AccountInfoModel();
		accountInfoModel.setAccountNick(changeAccountNickListViewModel.getAccountNick());
		accountInfoModel.setCustomerId(changeAccountNickListViewModel.getCustomerId());
		accountInfoModel.setNewAccountNick(newAccountNick);

		veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);

		LogModel logmodel = new LogModel();
		logmodel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
		logmodel.setCreatedBy(UserUtils.getCurrentUser().getFirstName());
		veriflyBaseWrapper.setLogModel(logmodel);
		BaseWrapper baseWrapperSmartMoney = new BaseWrapperImpl();
		baseWrapperSmartMoney.setBasePersistableModel(smartMoneyAccountModel);
		AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperSmartMoney);
		// VeriflyManager veriflyMgr =
		// veriflyManagerService.getVeriflyMgrByAccountId(smartMoneyAccountModel);
		veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
		try {
			// veriflyBaseWrapper =
			// veriflyMgr.changeAccountNick(veriflyBaseWrapper);
			veriflyBaseWrapper = abstractFinancialInstitution.changeAccountNick(veriflyBaseWrapper);
		} catch (ImplementationNotSupportedException inse) {
			throw new FrameworkCheckedException("implementationNotSupportedException");
		} catch (Exception fce) {
			fce.printStackTrace();
			throw new FrameworkCheckedException("Verifly Access Exception");
		}

		if (!veriflyBaseWrapper.isErrorStatus()) {
			throw new FrameworkCheckedException(veriflyBaseWrapper.getErrorMessage());
		}

		smartMoneyAccountModel.setName(newAccountNick);
		smartMoneyAccountModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
		smartMoneyAccountModel.setUpdatedOn(new Date());
		smartMoneyAccountDAO.saveOrUpdate(smartMoneyAccountModel);

		// actionLogModel.setCustomField1(String.valueOf(appUserId));
		logAfterAction(actionLogModel);
		ThreadLocalActionLog.remove();

		return baseWrapper;
	}

	public boolean isFirstSmartMoneyAccount(Long customerId,Long customerType) {		
		SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
		if (AppRoleConstantsInterface.CUSTOMER.longValue() == customerType.longValue()){
			smartMoneyAccountModel.setCustomerId(customerId);
		}else if (AppRoleConstantsInterface.RETAILER.longValue() == customerType.longValue()){
			smartMoneyAccountModel.setRetailerContactId(customerId);
		}else if (AppRoleConstantsInterface.DISTRIBUTOR.longValue() == customerType.longValue()){
			smartMoneyAccountModel.setDistributorContactId(customerId);
		}
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

	public boolean isFirstAccountOtherThanOla(String allpayId) {

		// get app user id from user device accounts
		UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
		uda.setUserId(allpayId);
		CustomList<UserDeviceAccountsModel> deviceAccountsList = this.userDeviceAccountsDAO.findByExample(uda, null, null, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
		if (deviceAccountsList.getResultsetList().size() > 0) {
			uda = deviceAccountsList.getResultsetList().get(0);
			// now look in the app user table to see if this user is retailer or
			// distributor
			AppUserModel au = new AppUserModel();
			au.setAppUserId(uda.getAppUserId());

			au = this.appUserDAO.findByPrimaryKey(au.getPrimaryKey());
			if (au != null) {
				SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
				// smartMoneyAccountModel.setBankId(UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankId());
				smartMoneyAccountModel.setActive(true);
				// app user type id 3 is for retailer and 4 is for distributor
				if (au.getAppUserTypeId() == 3) {
					smartMoneyAccountModel.setRetailerContactId(uda.getAppUserIdAppUserModel().getRetailerContactId());
				} else if (au.getAppUserTypeId() == 4) {
					smartMoneyAccountModel.setDistributorContactId(uda.getAppUserIdAppUserModel().getDistributorContactId());
				}
				CustomList list = this.smartMoneyAccountDAO.findByExample(smartMoneyAccountModel);
				if (list.getResultsetList().size() == 2) {
					return false;
				} else {
					return true;
				}
			} else {
				// throw exception
			}
		} else {
			// throw exception
		}
		// -------------------------------------------------
		return false;
	}

	private SmartMoneyAccountModel createSmartMoneyAccountModel(LinkPaymentModeModel linkPaymentModeModel, UserDeviceAccountsModel userDeviceAccountsModel, boolean veriflyRequired) throws Exception {
		int recordCount;
		int defRecordCount;

		BaseWrapper baseWrapperBank = new BaseWrapperImpl();
		BankModel bankModel = UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankIdBankModel();
		baseWrapperBank.setBasePersistableModel(bankModel);
		AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperBank);

		String bankName = bankModel.getName();

		BaseWrapper baseWrapper = new BaseWrapperImpl();

		SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();

		if (abstractFinancialInstitution.isVeriflyRequired()) {
			smartMoneyAccountModel.setName(linkPaymentModeModel.getName());
		} else {

			if (PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT.equals(linkPaymentModeModel.getPaymentMode())) {
				smartMoneyAccountModel.setName(bankName);
			} else {
				smartMoneyAccountModel.setName(linkPaymentModeModel.getName());
			}
		}

		smartMoneyAccountModel.setPaymentModeId(linkPaymentModeModel.getPaymentMode());
		smartMoneyAccountModel.setBankId(UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankId());
		if (userDeviceAccountsModel.getAppUserIdAppUserModel().getCustomerId() != null) {
			smartMoneyAccountModel.setCustomerId(userDeviceAccountsModel.getAppUserIdAppUserModel().getCustomerId());
		} else if (userDeviceAccountsModel.getAppUserIdAppUserModel().getDistributorContactId() != null) {
			smartMoneyAccountModel.setDistributorContactId(userDeviceAccountsModel.getAppUserIdAppUserModel().getDistributorContactId());
		} else if (userDeviceAccountsModel.getAppUserIdAppUserModel().getRetailerContactId() != null) {
			smartMoneyAccountModel.setRetailerContactId(userDeviceAccountsModel.getAppUserIdAppUserModel().getRetailerContactId());
		}

		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setEnableLike(Boolean.FALSE);

		CustomList cList = linkPaymentModeDAO.findByExample(smartMoneyAccountModel, null, null, exampleHolder);
		List list = cList.getResultsetList();
		if (!list.isEmpty()) {
			throw new FrameworkCheckedException("This Payment Mode is already linked.");
		}

		smartMoneyAccountModel.setCardTypeId(linkPaymentModeModel.getCardType());
		smartMoneyAccountModel.setCreatedOn(new Date());
		smartMoneyAccountModel.setUpdatedOn(new Date());
		smartMoneyAccountModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		smartMoneyAccountModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		smartMoneyAccountModel.setActive(true);
		smartMoneyAccountModel.setChangePinRequired(false);
//******Commented By Omar Butt 20-Jun-13: In case of Agent BB Accounts(OLA) - Existing PIN of OLA Account will be used 		

//		if (userDeviceAccountsModel.getDeviceTypeId().longValue() == DeviceTypeConstantsInterface.ALL_PAY.longValue()){
//			smartMoneyAccountModel.setChangePinRequired(false);
//		}else {
//		if (veriflyRequired) {
//			if (abstractFinancialInstitution.isVeriflyLite()) {
//				smartMoneyAccountModel.setChangePinRequired(false);
//			} else {
//				smartMoneyAccountModel.setChangePinRequired(true);
//			}
//		} else {
//			smartMoneyAccountModel.setChangePinRequired(false);
//		}
//	}
		if (linkPaymentModeModel.getCurrencyCode() != null && linkPaymentModeModel.getAccountType() != null) {
			smartMoneyAccountModel.setCurrencyCodeId(linkPaymentModeModel.getCurrencyCode());
			smartMoneyAccountModel.setAccountTypeId(linkPaymentModeModel.getAccountType());
		}
		smartMoneyAccountModel.setDefAccount(false);
		smartMoneyAccountModel.setDeleted(false);

//******Commented By Omar Butt 20-Jun-13: In case of Agent BB Accounts(OLA)- Default Account is Always OLA Account
//		SmartMoneyAccountModel defaultSmartMoneyAccountModel = new SmartMoneyAccountModel();
//		if (userDeviceAccountsModel.getAppUserIdAppUserModel().getCustomerId() != null) {
//			defaultSmartMoneyAccountModel.setCustomerId(userDeviceAccountsModel.getAppUserIdAppUserModel().getCustomerId());
//		} else if (userDeviceAccountsModel.getAppUserIdAppUserModel().getDistributorContactId() != null) {
//			defaultSmartMoneyAccountModel.setDistributorContactId(userDeviceAccountsModel.getAppUserIdAppUserModel().getDistributorContactId());
//		} else if (userDeviceAccountsModel.getAppUserIdAppUserModel().getRetailerContactId() != null) {
//			defaultSmartMoneyAccountModel.setRetailerContactId(userDeviceAccountsModel.getAppUserIdAppUserModel().getRetailerContactId());
//		}
//		defaultSmartMoneyAccountModel.setDefAccount(true);
//		defaultSmartMoneyAccountModel.setActive(true);
//
//		cList = linkPaymentModeDAO.findByExample(defaultSmartMoneyAccountModel, null, null, exampleHolder);
//		list = cList.getResultsetList();
//
//		if (!list.isEmpty()) {
//			smartMoneyAccountModel.setDefAccount(false);
//		}

		// first check that if account nick and customer account already exist
		// then throw exception
		SmartMoneyAccountModel testSmartMoneyAccountModel = new SmartMoneyAccountModel();

		// testSmartMoneyAccountModel.setCustomerId(userDeviceAccountsModel.getAppUserIdAppUserModel().getCustomerId());
		if (userDeviceAccountsModel.getAppUserIdAppUserModel().getCustomerId() != null) {
			testSmartMoneyAccountModel.setCustomerId(userDeviceAccountsModel.getAppUserIdAppUserModel().getCustomerId());
		} else if (userDeviceAccountsModel.getAppUserIdAppUserModel().getDistributorContactId() != null) {
			testSmartMoneyAccountModel.setDistributorContactId(userDeviceAccountsModel.getAppUserIdAppUserModel().getDistributorContactId());
		} else if (userDeviceAccountsModel.getAppUserIdAppUserModel().getRetailerContactId() != null) {
			testSmartMoneyAccountModel.setRetailerContactId(userDeviceAccountsModel.getAppUserIdAppUserModel().getRetailerContactId());
		}
		if (abstractFinancialInstitution.isVeriflyRequired()) {
			testSmartMoneyAccountModel.setName(linkPaymentModeModel.getName());
		} else {
			if (PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT.equals(linkPaymentModeModel.getPaymentMode())) {
				testSmartMoneyAccountModel.setName(bankName);
			} else {
				testSmartMoneyAccountModel.setName(linkPaymentModeModel.getName());
			}
		}

		CustomList<SmartMoneyAccountModel> smartMoneyAccountList = linkPaymentModeDAO.findByExample(testSmartMoneyAccountModel, null, null, exampleHolder);
		List smartMoneyAccountRList = smartMoneyAccountList.getResultsetList();
		if (smartMoneyAccountRList.size() > 0) {
			throw new FrameworkCheckedException("This Payment Mode is already linked.");
		}
		
		this.markOldSmartMoneyAccountsNonDefault(userDeviceAccountsModel.getAppUserIdAppUserModel());
		
		baseWrapper.setBasePersistableModel(this.linkPaymentModeDAO.saveOrUpdate(smartMoneyAccountModel));

		return (SmartMoneyAccountModel) baseWrapper.getBasePersistableModel();
	}
	
	
	private SmartMoneyAccountModel createSmartMoneyAccountModelForBulkAgent(LinkPaymentModeModel linkPaymentModeModel, UserDeviceAccountsModel userDeviceAccountsModel, boolean veriflyRequired, BankModel bankModel) throws Exception {
		int recordCount;
		int defRecordCount;

		BaseWrapper baseWrapperBank = new BaseWrapperImpl();
		baseWrapperBank.setBasePersistableModel(bankModel);
		AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperBank);

		String bankName = bankModel.getName();

		BaseWrapper baseWrapper = new BaseWrapperImpl();

		SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();

		if (abstractFinancialInstitution.isVeriflyRequired()) {
			smartMoneyAccountModel.setName(linkPaymentModeModel.getName());
		} else {

			if (PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT.equals(linkPaymentModeModel.getPaymentMode())) {
				smartMoneyAccountModel.setName(bankName);
			} else {
				smartMoneyAccountModel.setName(linkPaymentModeModel.getName());
			}

		}

		smartMoneyAccountModel.setPaymentModeId(linkPaymentModeModel.getPaymentMode());
		smartMoneyAccountModel.setBankId(bankModel.getBankId());
		if (userDeviceAccountsModel.getAppUserIdAppUserModel().getCustomerId() != null) {
			smartMoneyAccountModel.setCustomerId(userDeviceAccountsModel.getAppUserIdAppUserModel().getCustomerId());
		} else if (userDeviceAccountsModel.getAppUserIdAppUserModel().getDistributorContactId() != null) {
			smartMoneyAccountModel.setDistributorContactId(userDeviceAccountsModel.getAppUserIdAppUserModel().getDistributorContactId());
		} else if (userDeviceAccountsModel.getAppUserIdAppUserModel().getRetailerContactId() != null) {
			smartMoneyAccountModel.setRetailerContactId(userDeviceAccountsModel.getAppUserIdAppUserModel().getRetailerContactId());
		}

		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setEnableLike(Boolean.FALSE);

		CustomList cList = linkPaymentModeDAO.findByExample(smartMoneyAccountModel, null, null, exampleHolder);
		List list = cList.getResultsetList();
		if (!list.isEmpty()) {
			throw new FrameworkCheckedException("This Payment Mode is already linked.");
		}

		smartMoneyAccountModel.setCardTypeId(linkPaymentModeModel.getCardType());
		smartMoneyAccountModel.setCreatedOn(new Date());
		smartMoneyAccountModel.setUpdatedOn(new Date());
		smartMoneyAccountModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		smartMoneyAccountModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		smartMoneyAccountModel.setActive(true);
		if (userDeviceAccountsModel.getDeviceTypeId().longValue() == DeviceTypeConstantsInterface.ALL_PAY.longValue()){
			smartMoneyAccountModel.setChangePinRequired(false);
		}else {
		if (veriflyRequired) {
			if (abstractFinancialInstitution.isVeriflyLite()) {
				smartMoneyAccountModel.setChangePinRequired(false);
			} else {
				smartMoneyAccountModel.setChangePinRequired(true);
			}
		} else {
			smartMoneyAccountModel.setChangePinRequired(false);
		}
	}
		if (linkPaymentModeModel.getCurrencyCode() != null && linkPaymentModeModel.getAccountType() != null) {
			smartMoneyAccountModel.setCurrencyCodeId(linkPaymentModeModel.getCurrencyCode());
			smartMoneyAccountModel.setAccountTypeId(linkPaymentModeModel.getAccountType());
		}
		//For BB Agents accounts... default account is Branchless Banking
		smartMoneyAccountModel.setDefAccount(false);
		smartMoneyAccountModel.setDeleted(false);

		SmartMoneyAccountModel defaultSmartMoneyAccountModel = new SmartMoneyAccountModel();

		// defaultSmartMoneyAccountModel.setCustomerId(smartMoneyAccountModel.getCustomerId());
		if (userDeviceAccountsModel.getAppUserIdAppUserModel().getCustomerId() != null) {
			defaultSmartMoneyAccountModel.setCustomerId(userDeviceAccountsModel.getAppUserIdAppUserModel().getCustomerId());
		} else if (userDeviceAccountsModel.getAppUserIdAppUserModel().getDistributorContactId() != null) {
			defaultSmartMoneyAccountModel.setDistributorContactId(userDeviceAccountsModel.getAppUserIdAppUserModel().getDistributorContactId());
		} else if (userDeviceAccountsModel.getAppUserIdAppUserModel().getRetailerContactId() != null) {
			defaultSmartMoneyAccountModel.setRetailerContactId(userDeviceAccountsModel.getAppUserIdAppUserModel().getRetailerContactId());
		}
		defaultSmartMoneyAccountModel.setDefAccount(true);
		defaultSmartMoneyAccountModel.setActive(true);

		cList = linkPaymentModeDAO.findByExample(defaultSmartMoneyAccountModel, null, null, exampleHolder);
		list = cList.getResultsetList();

		if (!list.isEmpty()) {
			smartMoneyAccountModel.setDefAccount(false);
		}

		// first check that if account nick and customer account already exist
		// then throw exception
		SmartMoneyAccountModel testSmartMoneyAccountModel = new SmartMoneyAccountModel();

		// testSmartMoneyAccountModel.setCustomerId(userDeviceAccountsModel.getAppUserIdAppUserModel().getCustomerId());
		if (userDeviceAccountsModel.getAppUserIdAppUserModel().getCustomerId() != null) {
			testSmartMoneyAccountModel.setCustomerId(userDeviceAccountsModel.getAppUserIdAppUserModel().getCustomerId());
		} else if (userDeviceAccountsModel.getAppUserIdAppUserModel().getDistributorContactId() != null) {
			testSmartMoneyAccountModel.setDistributorContactId(userDeviceAccountsModel.getAppUserIdAppUserModel().getDistributorContactId());
		} else if (userDeviceAccountsModel.getAppUserIdAppUserModel().getRetailerContactId() != null) {
			testSmartMoneyAccountModel.setRetailerContactId(userDeviceAccountsModel.getAppUserIdAppUserModel().getRetailerContactId());
		}
		if (abstractFinancialInstitution.isVeriflyRequired()) {
			testSmartMoneyAccountModel.setName(linkPaymentModeModel.getName());
		} else {
			if (PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT.equals(linkPaymentModeModel.getPaymentMode())) {
				testSmartMoneyAccountModel.setName(bankName);
			} else {
				testSmartMoneyAccountModel.setName(linkPaymentModeModel.getName());
			}
		}

		CustomList<SmartMoneyAccountModel> smartMoneyAccountList = linkPaymentModeDAO.findByExample(testSmartMoneyAccountModel, null, null, exampleHolder);
		List smartMoneyAccountRList = smartMoneyAccountList.getResultsetList();
		if (smartMoneyAccountRList.size() > 0) {
			throw new FrameworkCheckedException("This Payment Mode is already linked.");
		}
		
		this.markOldSmartMoneyAccountsNonDefault(userDeviceAccountsModel.getAppUserIdAppUserModel());
		
		baseWrapper.setBasePersistableModel(this.linkPaymentModeDAO.saveOrUpdate(smartMoneyAccountModel));

		return (SmartMoneyAccountModel) baseWrapper.getBasePersistableModel();
	}
	
	
	/**@author kashif.bashir
	 * @param userDeviceAccountsModel
	 */
	private void markOldSmartMoneyAccountsNonDefault(AppUserModel appUserModel) {
		
		ExampleConfigHolderModel configHolderModel = new ExampleConfigHolderModel();
		configHolderModel.setMatchMode(MatchMode.EXACT);
		
		SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();

		smartMoneyAccountModel.setDeleted(Boolean.TRUE);
		
		smartMoneyAccountModel.setActive(Boolean.FALSE);
		
		if (appUserModel.getCustomerId() != null) {
			
			smartMoneyAccountModel.setCustomerId(appUserModel.getCustomerId());
			
		} else if (appUserModel.getDistributorContactId() != null) {
			
			smartMoneyAccountModel.setDistributorContactId(appUserModel.getDistributorContactId());
			
		} else if (appUserModel.getRetailerContactId() != null) {
			
			smartMoneyAccountModel.setRetailerContactId(appUserModel.getRetailerContactId());
		}
		
		CustomList<SmartMoneyAccountModel> customList = linkPaymentModeDAO.findByExample(smartMoneyAccountModel, null, null, configHolderModel);
		
		List<SmartMoneyAccountModel> existingSmartMoneyAccountList = customList.getResultsetList();

		if(!existingSmartMoneyAccountList.isEmpty()) {
		
			for(SmartMoneyAccountModel _smartMoneyAccountModel : existingSmartMoneyAccountList) {
				
				_smartMoneyAccountModel.setDefAccount(Boolean.FALSE);
			}		
			
			logger.info("MARKING_OLD_SMA_NOT_DEFAULT : "+existingSmartMoneyAccountList.size());
			
			linkPaymentModeDAO.saveOrUpdateCollection(existingSmartMoneyAccountList);
		}
	}
	

	private AccountInfoModel crAccountModel(LinkPaymentModeModel linkPaymentModeModel, UserDeviceAccountsModel userDeviceAccountsModel, SmartMoneyAccountModel smartMoneyAccountModel)
			throws FrameworkCheckedException {

		AccountInfoModel accountInfoModel = new AccountInfoModel();
		accountInfoModel.setAccountNo(linkPaymentModeModel.getAccountNo());
		accountInfoModel.setAccountNick(linkPaymentModeModel.getName());
		accountInfoModel.setActive(smartMoneyAccountModel.getActive());

		if (linkPaymentModeModel.getExpiryDate() != null) {
			accountInfoModel.setCardExpiryDate(PortalDateUtils.formatDate(linkPaymentModeModel.getExpiryDate(), "MM/yy"));
		}
		accountInfoModel.setCardNo(linkPaymentModeModel.getCardNo());
		accountInfoModel.setCardTypeId(linkPaymentModeModel.getCardType());

		accountInfoModel.setCreatedOn(smartMoneyAccountModel.getCreatedOn());
		accountInfoModel.setUpdatedOn(smartMoneyAccountModel.getUpdatedOn());
		if (smartMoneyAccountModel.getCustomerId() != null) {
			accountInfoModel.setCustomerId(smartMoneyAccountModel.getCustomerId());
		} else {
			accountInfoModel.setCustomerId(userDeviceAccountsModel.getAppUserId());
		}

		accountInfoModel.setCustomerMobileNo(userDeviceAccountsModel.getAppUserIdAppUserModel().getMobileNo());
		accountInfoModel.setFirstName(userDeviceAccountsModel.getAppUserIdAppUserModel().getFirstName());
		accountInfoModel.setLastName(userDeviceAccountsModel.getAppUserIdAppUserModel().getLastName());
		accountInfoModel.setPaymentModeId(smartMoneyAccountModel.getPaymentModeId());

		accountInfoModel.setAccountTypeId(linkPaymentModeModel.getAccountType());
		accountInfoModel.setCurrencyCodeId(linkPaymentModeModel.getCurrencyCode());

		return accountInfoModel;
	}

	public SearchBaseWrapper loadLinkPaymentMode(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		return null;
	}

	public BaseWrapper loadLinkPaymentMode(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		return null;
	}

	public SearchBaseWrapper searchLinkPaymentMode(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		return null;
	}

	public BaseWrapper updateLinkPaymentMode(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		return null;
	}

	public void setLinkPaymentModeDAO(LinkPaymentModeDAO linkPaymentModeDAO) {
		this.linkPaymentModeDAO = linkPaymentModeDAO;
	}

	private ActionLogModel logBeforeAction(Long actionId, Long useCaseId/*
																		 * ,Long
																		 * appUserId
																		 */) throws FrameworkCheckedException {
		ActionLogModel actionLogModel = new ActionLogModel();
		actionLogModel.setActionId(actionId);
		actionLogModel.setUsecaseId(useCaseId);
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_START); // the
		// process
		// is
		// starting
		actionLogModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
		actionLogModel.setUserName(UserUtils.getCurrentUser().getUsername());
		// actionLogModel.setCustomField1(appUserId.toString());
		actionLogModel.setStartTime(new Timestamp(new Date().getTime()));
		actionLogModel.setDeviceTypeId(PortalConstants.WEB_TYPE_DEVICE);
		BaseWrapper baseWrapperActionLog = new BaseWrapperImpl();
		baseWrapperActionLog.setBasePersistableModel(actionLogModel);
		baseWrapperActionLog = this.actionLogManager.createOrUpdateActionLogRequiresNewTransaction(baseWrapperActionLog);
		return actionLogModel;
	}

	private void logAfterAction(ActionLogModel actionLogModel) throws FrameworkCheckedException {
		BaseWrapper baseWrapperActionLog = new BaseWrapperImpl();
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END); // the
		// process
		// is
		// starting
		actionLogModel.setEndTime(new Timestamp(new Date().getTime()));
		baseWrapperActionLog.setBasePersistableModel(actionLogModel);
		baseWrapperActionLog = this.actionLogManager.createOrUpdateActionLog(baseWrapperActionLog);
	}

	public SearchBaseWrapper searchAccounts(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		CustomList<ChangeAccountNickListViewModel> customList = this.changeAccountNickListViewDAO.findByExample((ChangeAccountNickListViewModel) searchBaseWrapper.getBasePersistableModel(),
				searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap());
		searchBaseWrapper.setCustomList(customList);
		return searchBaseWrapper;
	}

	public BaseWrapper getSmartMoneyAccountInfo(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		Long smartMoneyAccountId = new Long(baseWrapper.getObject("smartMoneyAccountId").toString());
		Long appUserId = new Long(baseWrapper.getObject("appUserId").toString());

		ChangeAccountNickListViewModel changeAccountNickListViewModel = new ChangeAccountNickListViewModel();
		changeAccountNickListViewModel.setSmartMoneyAccountId(smartMoneyAccountId);
		changeAccountNickListViewModel.setAppUserId(appUserId);

		CustomList<ChangeAccountNickListViewModel> cList = changeAccountNickListViewDAO.findByExample(changeAccountNickListViewModel);
		List<ChangeAccountNickListViewModel> list = cList.getResultsetList();
		changeAccountNickListViewModel = list.get(0);

		baseWrapper.setBasePersistableModel(changeAccountNickListViewModel);
		return baseWrapper;
	}

	/**
	 * @param changeAccountNickListViewDAO
	 *            the changeAccountNickListViewDAO to set
	 */
	public void setChangeAccountNickListViewDAO(ChangeAccountNickListViewDAO changeAccountNickListViewDAO) {
		this.changeAccountNickListViewDAO = changeAccountNickListViewDAO;
	}

	public void setUserInfoListViewDAO(UserInfoListViewDAO userInfoListViewDAO) {
		this.userInfoListViewDAO = userInfoListViewDAO;
	}

	public void setAccountTypeDAO(AccountTypeDAO accountTypeDAO) {
		this.accountTypeDAO = accountTypeDAO;
	}

	public void setCurrencyCodeDAO(CurrencyCodeDAO currencyCodeDAO) {
		this.currencyCodeDAO = currencyCodeDAO;
	}

	public UserDeviceAccountsDAO getUserDeviceAccountsDAO() {
		return userDeviceAccountsDAO;
	}

	public void setUserDeviceAccountsDAO(UserDeviceAccountsDAO userDeviceAccountsDAO) {
		this.userDeviceAccountsDAO = userDeviceAccountsDAO;
	}

	public AppUserDAO getAppUserDAO() {
		return appUserDAO;
	}

	public void setAppUserDAO(AppUserDAO appUserDAO) {
		this.appUserDAO = appUserDAO;
	}

	public BaseWrapper loadUserInfoListViewByAllPayId(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		AllpayUserInfoListViewModel userInfoListViewModel = (AllpayUserInfoListViewModel) baseWrapper.getBasePersistableModel();
		CustomList<AllpayUserInfoListViewModel> cList = this.allpayUserInfoListViewDAO.findByExample(userInfoListViewModel);
		List<AllpayUserInfoListViewModel> list = cList.getResultsetList();
		userInfoListViewModel = list.get(0);
		baseWrapper.setBasePersistableModel(userInfoListViewModel);
		return baseWrapper;
	}

	public BaseWrapper loadAllPayUserInfoListViewByPrimaryKey(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		AllpayUserInfoListViewModel userInfoListViewModel = (AllpayUserInfoListViewModel) baseWrapper.getBasePersistableModel();
		userInfoListViewModel = this.allpayUserInfoListViewDAO.findByPrimaryKey(userInfoListViewModel.getAppUserId());
		baseWrapper.setBasePersistableModel(userInfoListViewModel);
		return baseWrapper;
	}

	public AllPayUserInfoListViewDAO getAllpayUserInfoListViewDAO() {
		return allpayUserInfoListViewDAO;
	}

	public void setAllpayUserInfoListViewDAO(AllPayUserInfoListViewDAO allpayUserInfoListViewDAO) {
		this.allpayUserInfoListViewDAO = allpayUserInfoListViewDAO;
	}

}
