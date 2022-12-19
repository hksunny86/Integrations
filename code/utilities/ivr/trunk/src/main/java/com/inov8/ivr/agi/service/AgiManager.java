package com.inov8.ivr.agi.service;

import io.task.context.Context;
import io.task.util.TaskConstant;

import java.util.Map;

import com.inov8.util.IvrConstant;
import com.inov8.util.StatusCodeConstant;

public class AgiManager
{
	private Context context;

	public void setServiceStartMust(Object obj)
	{
	}

	public void process(Map<String, Object> dataMap)
	{
		try
		{
			dataMap.put(IvrConstant.AGIMANAGER, this);
			context.startFlow(dataMap);
		}
		finally
		{
			if(dataMap.containsKey(IvrConstant.RESPONSE_CODE) == false)
			{
				dataMap.put(IvrConstant.RESPONSE_CODE, StatusCodeConstant.CODE_9001);
				dataMap.put(TaskConstant.NEXT_TASK_ID, IvrConstant.HANGUP_FLOW);
				context.startFlow(dataMap);
			}
		}
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

}
