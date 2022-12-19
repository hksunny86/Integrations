package com.inov8.microbank.server.service.portal.forgotpinmodule;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;

import com.inov8.common.util.RandomUtils;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.model.portal.forgotpinmodule.ForgotpinListViewModel;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.MessageParsingUtils;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.SMSConstants;
import com.inov8.microbank.common.util.SmsSender;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.dao.portal.forgotpinmodule.ForgotPinDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;

public class ForgotpinManagerImp implements ForgotpinManager
{

	private ForgotPinDAO forgotPinDao;

	private UserDeviceAccountsManager userDeviceAccountsManager;

	private SmsSender smsSender = null;

	private ActionLogManager actionLogManager;

	public SearchBaseWrapper searchForgotPinUser(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{

		CustomList<ForgotpinListViewModel> list = this.forgotPinDao.findByExample((ForgotpinListViewModel) searchBaseWrapper
				.getBasePersistableModel(), searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap());

		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;
	}

	public BaseWrapper updateForgotPin(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		ActionLogModel actionLogModel = new ActionLogModel();
		UserDeviceAccountsModel uModel = (UserDeviceAccountsModel) baseWrapper.getBasePersistableModel();
		// Get data from BaseWrapper of ActionLogMOdel
		actionLogModel.setActionId((Long) baseWrapper.getObject(PortalConstants.KEY_ACTION_ID));
		actionLogModel.setUsecaseId((Long) baseWrapper.getObject(PortalConstants.KEY_USECASE_ID));
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_START);
		actionLogModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
		actionLogModel.setUserName(UserUtils.getCurrentUser().getUsername());
		actionLogModel.setStartTime(new Timestamp(new Date().getTime()));
		actionLogModel.setDeviceTypeId(PortalConstants.WEB_TYPE_DEVICE);
		actionLogModel.setCustomField1((String.valueOf(uModel.getAppUserId())));
		// Save the ActionLogModel in ActionLog Table
		actionLogModel = this.logAction(actionLogModel);

		// Get data from BaseWrapper of userDeviceAccountsModel

		UserDeviceAccountsModel userDeviceAccountsModel = (UserDeviceAccountsModel) this.userDeviceAccountsManager
				.loadUserDeviceAccount(baseWrapper).getBasePersistableModel();

		String randomPin = RandomUtils.generateRandom(4, false, true);
		userDeviceAccountsModel.setPin(EncoderUtils.encodeToSha(randomPin));
		userDeviceAccountsModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		userDeviceAccountsModel.setUpdatedOn(new Date());
		userDeviceAccountsModel.setPinChangeRequired(true);

		// Update the Pin in the UserDeviceAccountModel
		baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
		this.userDeviceAccountsManager.updateUserDeviceAccount(baseWrapper);

		// Send SMS to User.
		String mobileno = userDeviceAccountsModel.getAppUserIdAppUserModel().getMobileNo();

		// Updated against CRF-28.

		Object[] args = { randomPin };

		String messageString = MessageUtil.getMessage("forgotpin.pinChangeSuccessMessage", args);

		SmsMessage smsMessage = null;
		if(userDeviceAccountsModel.getDeviceTypeId() == DeviceTypeConstantsInterface.ALL_PAY)
		{
			smsMessage = new SmsMessage(mobileno, messageString, SMSConstants.Sender_1611);
		
		}
		else
		{
			smsMessage = new SmsMessage(mobileno, messageString);
		}
		if (null != userDeviceAccountsModel && userDeviceAccountsModel.getCommissioned())
		{
			smsMessage.setMessageText(MessageParsingUtils.parseMessageForIpos(smsMessage.getMessageText()));
		}

		// SmsMessage smsMessage=new SmsMessage(mobileno,"Dear Customer, your
		// new MWallet PIN is "+ randomPin +". Please change this PIN immediately
		// after first use.");
		smsSender.send(smsMessage);

		actionLogModel.setEndTime(new Timestamp(new Date().getTime()));
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END);
		this.logAction(actionLogModel);
		return baseWrapper;
	}

	private ActionLogModel logAction(ActionLogModel actionLogModel) throws FrameworkCheckedException
	{

		BaseWrapper bW = new BaseWrapperImpl();
		bW.setBasePersistableModel(actionLogModel);
		if (actionLogModel.getActionLogId() == null)
			this.actionLogManager.createOrUpdateActionLogRequiresNewTransaction(bW);
		else
			this.actionLogManager.createOrUpdateActionLog(bW);
		return (ActionLogModel) bW.getBasePersistableModel();

	}

	public BaseWrapper updateForgotPassword(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		ActionLogModel actionLogModel = new ActionLogModel();
		UserDeviceAccountsModel uModel = (UserDeviceAccountsModel) baseWrapper.getBasePersistableModel();
		// Get data from BaseWrapper of ActionLogMOdel
		actionLogModel.setActionId((Long) baseWrapper.getObject(PortalConstants.KEY_ACTION_ID));
		actionLogModel.setUsecaseId((Long) baseWrapper.getObject(PortalConstants.KEY_USECASE_ID));
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_START);
		actionLogModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
		actionLogModel.setUserName(UserUtils.getCurrentUser().getUsername());
		actionLogModel.setStartTime(new Timestamp(new Date().getTime()));
		actionLogModel.setDeviceTypeId(PortalConstants.WEB_TYPE_DEVICE);
		actionLogModel.setCustomField1((String.valueOf(uModel.getAppUserId())));
		actionLogModel.setCustomField11(uModel.getUserId());
		// Save the ActionLogModel in ActionLog Table
		actionLogModel = this.logAction(actionLogModel);

		// Get data from BaseWrapper of userDeviceAccountsModel

		UserDeviceAccountsModel userDeviceAccountsModel = (UserDeviceAccountsModel) this.userDeviceAccountsManager
				.loadUserDeviceAccount(baseWrapper).getBasePersistableModel();

		// String randomPassword=RandomUtils.generateRandom(8,true, true);
		String randomPassword = this.generateRandom(8, false, true);
		userDeviceAccountsModel.setPassword(EncoderUtils.encodeToSha(randomPassword));
		userDeviceAccountsModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		userDeviceAccountsModel.setUpdatedOn(new Date());
		userDeviceAccountsModel.setPasswordChangeRequired(true);
		userDeviceAccountsModel.setAccountLocked(false);

		// Update the Pin in the UserDeviceAccountModel
		baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
		this.userDeviceAccountsManager.updateUserDeviceAccount(baseWrapper);

		// Send SMS to User.
		AppUserModel appUserModel = userDeviceAccountsModel.getAppUserIdAppUserModel();
		String mobileno = appUserModel.getMobileNo();

		// Updated against CRF-28.

		Object[] args = { randomPassword };

		String messageString = "";
		if(appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER.longValue())
		{
			messageString = MessageUtil.getMessage("forgotpin.allpaypasswordChangeSuccessMessage", args);
		}
		else if(appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue())
		{
			messageString = MessageUtil.getMessage("forgotpin.passwordChangeSuccessMessage", args);
		}
		else if(appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.HANDLER.longValue()) {

			messageString = MessageUtil.getMessage("forgotpin.allpaypasswordChangeSuccessMessage.handler", args);
		}
		
		SmsMessage smsMessage = new SmsMessage(mobileno, messageString);
		if (null != userDeviceAccountsModel && userDeviceAccountsModel.getCommissioned())
		{
			smsMessage.setMessageText(MessageParsingUtils.parseMessageForIpos(smsMessage.getMessageText()));
		}

		// SmsMessage smsMessage=new SmsMessage(mobileno,"Dear Customer, your
		// new MWallet PIN is "+ randomPin +". Please change this PIN immediately
		// after first use.");
		smsSender.send(smsMessage);

		actionLogModel.setEndTime(new Timestamp(new Date().getTime()));
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END);
		this.logAction(actionLogModel);
		return baseWrapper;
	}

	public void setForgotPinDao(ForgotPinDAO forgotPinDao)
	{
		this.forgotPinDao = forgotPinDao;
	}

	public void setUserDeviceAccountsManager(UserDeviceAccountsManager userDeviceAccountsManager)
	{
		this.userDeviceAccountsManager = userDeviceAccountsManager;
	}

	public void setSmsSender(SmsSender smsSender)
	{
		this.smsSender = smsSender;
	}

	public void setActionLogManager(ActionLogManager actionLogManager)
	{
		this.actionLogManager = actionLogManager;
	}

	public String generateRandom(int count, boolean letters, boolean numbers)
	{
		int tempCount = count;
		char[] chars = null;
		Random random = new Random();
		int start = 0, end = 0;
		if (count == 0)
		{
			return "";
		}
		else if (count < 0)
		{
			throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
		}
		if ((start == 0) && (end == 0))
		{
			end = 'z' + 1;
			start = ' ';
			if (!letters && !numbers)
			{
				start = 0;
				end = Integer.MAX_VALUE;
			}
		}

		StringBuffer buffer = new StringBuffer();
		int gap = end - start;

		while (count-- != 0)
		{
			char ch;
			if (chars == null)
			{
				ch = (char) (random.nextInt(gap) + start);
			}
			else
			{
				ch = chars[random.nextInt(gap) + start];
			}
			if ((letters && numbers && Character.isLetterOrDigit(ch)) || (letters && Character.isLetter(ch))
					|| (numbers && Character.isDigit(ch)) || (!letters && !numbers))
			{
				if (letters && numbers && buffer.length() == tempCount-1)
				{
					boolean letter = false;
					boolean digit = false;

					for (int i = 0; i < buffer.length(); i++)
					{
						char a = buffer.charAt(i);
						if (!letter && Character.isLetter(a))
						{
							letter = true;
						}
					}
					for (int i = 0; i < buffer.length(); i++)
					{
						char a = buffer.charAt(i);
						if (!digit && Character.isDigit(a))
						{
							digit = true;
						}
					}
					if (letter && digit)
					{
						buffer.append(ch);
					}
					else
					{
						count++;
					}
				}
				else
				{
					buffer.append(ch);
				}
			}
			else
			{
				count++;
			}
		}
		return buffer.toString();
	}

}
