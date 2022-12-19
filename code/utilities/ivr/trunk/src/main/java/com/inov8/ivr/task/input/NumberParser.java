package com.inov8.ivr.task.input;

import io.task.exception.BaseException;

import java.util.List;

public interface NumberParser
{
	List<String> parseNumber(String number) throws BaseException;
}
