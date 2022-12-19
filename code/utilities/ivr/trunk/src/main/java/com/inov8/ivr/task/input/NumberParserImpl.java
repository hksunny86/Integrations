package com.inov8.ivr.task.input;

import io.task.exception.BaseException;
import io.task.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NumberParserImpl implements NumberParser
{
	private static final Logger logger = LoggerFactory.getLogger(NumberParserImpl.class);

	private String units[] = {"", "", "100", "1000", "1000", "100", "million", "million", "100"};
	private String afterUnits[] = {"", "", "", "", "", "", "", "", ""};
	
	public String[] getUnits() {
		return units;
	}

	public String[] getAfterUnits()
	{
		return afterUnits;
	}

	@Override
	public List<String> parseNumber(String number) throws BaseException
	{
		List<String> list = new ArrayList<String>();

		try {
			int len = number.length();
			if(len > 0) {
				int pLen = len, idx = 0, ctrl = 0, sub = 0;
				for(; idx < len; pLen -= sub, idx += sub) {
					sub = pLen % 3;
					if(sub == 0) {
						sub = 1;
						if(number.charAt(idx) != '0') {
							ctrl = 1;
						}
					}
					if(number.charAt(idx) != '0' || number.charAt(idx + sub - 1) != '0') {
						list.add(StringUtil.trimFromStart(number.substring(idx, idx + sub), '0'));
						if(units[pLen - 1].length() > 0) {
							list.add(units[pLen - 1]);
						}
						if(afterUnits[pLen - 1].length() > 0) {
							list.add(afterUnits[pLen - 1]);
						}
					} else if(ctrl == 1 && pLen > 3) {
						ctrl = 0;
						list.add(units[pLen - 1]);
					}
				}
			}
			
		} catch (Exception ex) {
			logger.error("", ex);
			throw new BaseException(ex);
		}
		return list;
	}

}
