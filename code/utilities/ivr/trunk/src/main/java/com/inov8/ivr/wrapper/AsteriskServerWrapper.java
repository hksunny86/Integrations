package com.inov8.ivr.wrapper;

import org.asteriskjava.live.AsteriskServer;

public class AsteriskServerWrapper
{
	private AsteriskServer asteriskServer;
	private String channel;
	private String callerId;

	public AsteriskServer getAsteriskServer()
	{
		return asteriskServer;
	}
	public void setAsteriskServer(AsteriskServer asteriskServer)
	{
		this.asteriskServer = asteriskServer;
	}
	public String getChannel()
	{
		return channel;
	}
	public void setChannel(String channel)
	{
		this.channel = channel;
	}
	public String getCallerId()
	{
		return callerId;
	}
	public void setCallerId(String callerId)
	{
		this.callerId = callerId;
	}
}
