/*
 * Usage rights pending...
 * 
 * 
 * 
 * 
 * 
 * 
 * ****************************************************************************
 */

package com.inov8.ivr.task.ws;

import io.task.tasks.Task;
import io.task.util.StringUtil;
import io.task.util.TaskConstant;

import java.util.HashMap;
import java.util.Map;

import com.inov8.microbank.ws.service.WsClient;
import com.inov8.util.IvrConstant;


/**<pre>
 * Created By : Ahmed Mobasher Khan
 * Creation Date : Aug 14, 2014
 * 
 * Purpose : 
 * 
 * Updated By : 
 * Updated Date : 
 * Comments : 
 * </pre>
 */
public class KeyInTask extends Task
{
	
	private Map<String, String> nextTaskMap = new HashMap<String, String>();
	private WsClient wsClient;
	
	public void setWsClient(WsClient wsClient)
	{
		this.wsClient = wsClient;
	}

	/* (non-Javadoc)
	 * @see com.ivr.javaagi.task.Task#process(java.util.Map)
	 */
	@Override
	public void process(Map<String, Object> dataMap)
	{
		String nextTaskId = nextTaskMap.get(TaskConstant.DEFAULT_TASK);
		try
		{
			wsClient.processKeyIn(dataMap);
			String taskId = nextTaskMap.get(dataMap.get(IvrConstant.TX_RESPONSE));
			if(StringUtil.isNullOrEmptyTrimmed(taskId) == false)
			{
				nextTaskId = taskId;
			}
		}
		catch(Exception e)
		{
			logger.error("TX ID {}", dataMap.get(IvrConstant.TX_ID), e);
		}
		dataMap.put(TaskConstant.NEXT_TASK_ID, nextTaskId);
	}
	
	public void setNextTask(String response, String taskId){
		nextTaskMap.put(response, taskId);
	}

}
