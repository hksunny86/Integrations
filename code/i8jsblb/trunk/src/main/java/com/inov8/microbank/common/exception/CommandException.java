package com.inov8.microbank.common.exception;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.util.ErrorLevel;


@SuppressWarnings("serial")
public class CommandException extends FrameworkCheckedException 
{
	protected ErrorLevel errorLevel;
	private String indexLabel;
	private String secondIndexLabel;
	
	public CommandException(String msg,long errorCode, ErrorLevel level,Throwable ex)
	{
		super(msg, null, errorCode, ex);
		setErrorLevel(level);
	}
	
	public CommandException(String msg,long errorCode,ErrorLevel level)
	{
		super(msg,null,errorCode,null);
		setErrorLevel(level);
	}

	public CommandException(String msg, long errorCode, ErrorLevel level, Throwable ex, String indexLabel) {
		super(msg, null, errorCode, ex);
		setErrorLevel(level);
		this.indexLabel = indexLabel;
	}

	public CommandException(String msg, long errorCode, ErrorLevel level, Throwable ex, String indexLabel,String secondIndexLabel) {
		super(msg, null, errorCode, ex);
		setErrorLevel(level);
		this.indexLabel = indexLabel;
		this.secondIndexLabel = secondIndexLabel;
	}
		
	public void setErrorLevel(ErrorLevel level)
	{
		this.errorLevel = level;
	}
	
	public ErrorLevel getLevel()
	{
		return errorLevel;
	}

	public String getIndexLabel() {
		return indexLabel;
	}

	public String getSecondIndexLabel() {
		return secondIndexLabel;
	}
}
