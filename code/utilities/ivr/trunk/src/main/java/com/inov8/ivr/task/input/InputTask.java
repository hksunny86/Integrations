package com.inov8.ivr.task.input;

import io.task.tasks.Task;

import java.util.Map;


/**
 * <pre>
 * Created By : Ahmed Mobasher Khan
 * 
 * Purpose : 
 * 
 * Updated By : 
 * Updated Date : 
 * Comments :
 * </pre>
 */
public abstract class InputTask extends Task
{
	@Override
	public abstract void process(Map<String, Object> CallDataMap);

}
