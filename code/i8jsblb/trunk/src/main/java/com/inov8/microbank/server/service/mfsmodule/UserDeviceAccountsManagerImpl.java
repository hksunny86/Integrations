package com.inov8.microbank.server.service.mfsmodule;

import java.util.List;

import org.hibernate.criterion.MatchMode;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.SmsSender;
import com.inov8.microbank.server.dao.mfsmodule.UserDeviceAccountsDAO;

public class UserDeviceAccountsManagerImpl implements UserDeviceAccountsManager {
	
	private UserDeviceAccountsDAO userDeviceAccountsDAO;
	private SmsSender smsSender;
	
	
	public BaseWrapper loadUserDeviceAccount(BaseWrapper baseWrapper)
	{
		UserDeviceAccountsModel userDeviceAccountsModel = (UserDeviceAccountsModel)baseWrapper.getBasePersistableModel();
	 	   ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
		   exampleConfigHolderModel.setMatchMode(MatchMode.EXACT);
		   exampleConfigHolderModel.setEnableLike(Boolean.FALSE);

		List<UserDeviceAccountsModel> list = this.userDeviceAccountsDAO.findByExample(userDeviceAccountsModel, null, null, exampleConfigHolderModel).getResultsetList();
		if(!list.isEmpty())
		{
			userDeviceAccountsModel = list.get(0);
			baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
		}
		return baseWrapper;
	}


	
	public BaseWrapper updateUserDeviceAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		UserDeviceAccountsModel userDeviceAccount = (UserDeviceAccountsModel)baseWrapper.getBasePersistableModel();
		userDeviceAccount = userDeviceAccountsDAO.saveOrUpdate(userDeviceAccount);
		baseWrapper.setBasePersistableModel(userDeviceAccount);
		return baseWrapper;
	}
	
	public void sendSMS( String mfsId, String randomPin, String mobileNo )
	{
		Object[] args = {mfsId,randomPin};
		
		String messageString = MessageUtil.getMessage("customer.mfsAccountCreated", args);
		
//		String messageString = "Dear Customer! Your New MWallet Account  is created Your MFSID is: " +
//		mfsId + " and Pin is: " + randomPin ;

		SmsMessage smsMessage = new SmsMessage(mobileNo, messageString);
		try
		{
			smsSender.send(smsMessage);
		}
		catch (FrameworkCheckedException e)
		{			
			e.printStackTrace();
		}		
	}

	@Override
	public UserDeviceAccountsModel loadUserDeviceAccountsModelByAppUserIdAndDeviceTypeId(Long deviceTypeId, Long appUserId) throws FrameworkCheckedException {
		return userDeviceAccountsDAO.loadUserDeviceAccountsModelByAppUserIdAndDeviceTypeId(deviceTypeId, appUserId);
	}
	public UserDeviceAccountsModel loadUserDeviceAccountByUserId(String userId) throws FrameworkCheckedException
	{
		return userDeviceAccountsDAO.loadUserDeviceAccountByUserId(userId);
	}



	public void setUserDeviceAccountsDAO(UserDeviceAccountsDAO userDeviceAccountsDAO) {
		this.userDeviceAccountsDAO = userDeviceAccountsDAO;
	}



	public void setSmsSender(SmsSender smsSender)
	{
		this.smsSender = smsSender;
	}

}
