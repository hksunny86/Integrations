package com.inov8.ivr.task.input;

import io.task.exception.BaseException;

import java.util.List;

public class PashtoNumberParserImpl extends NumberParserImpl
{
	private String afterHundred = "above";

	@Override
	public List<String> parseNumber(String number) throws BaseException
	{
		List<String> list = super.parseNumber(number);
		
		int idx = list.indexOf(super.getUnits()[2]);
		
		if(idx < (list.size() - 1)) {
			list.add(idx + 1, afterHundred);
		}
		
		return list;
	}
	
	public void setAfterHundred(String afterHundred) {
		this.afterHundred = afterHundred;
	}

}
