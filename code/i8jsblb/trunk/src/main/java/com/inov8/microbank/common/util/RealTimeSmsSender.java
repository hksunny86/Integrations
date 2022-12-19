package com.inov8.microbank.common.util;

import com.inov8.microbank.common.model.messagemodule.SmsMessage;

public interface RealTimeSmsSender
{

	public abstract void send(SmsMessage smsMessage);

}