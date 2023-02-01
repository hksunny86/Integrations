package com.inov8.microbank.common.util;

import java.util.ArrayList;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;

public interface SmsSender
{

	public abstract void send(SmsMessage smsMessage) throws FrameworkCheckedException;
	public abstract void send(ArrayList<SmsMessage> smsMessageList) throws FrameworkCheckedException;
	public abstract void sendDelayed(SmsMessage smsMessage) throws FrameworkCheckedException;
	public abstract void pushNotification(SmsMessage smsMessage) throws FrameworkCheckedException;

}