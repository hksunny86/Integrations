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

package com.inov8.ivr.task.call;

import io.task.exception.BaseException;
import io.task.exception.TaskPropertyException;
import io.task.tasks.Task;
import io.task.util.StringUtil;
import io.task.util.TaskConstant;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.LiveException;
import org.asteriskjava.live.OriginateCallback;
import org.asteriskjava.manager.action.OriginateAction;

import com.inov8.ivr.wrapper.AsteriskServerWrapper;
import com.inov8.util.IvrConstant;
import com.inov8.util.IvrUtil;
import com.inov8.util.StatusCodeConstant;

/**<pre>
 * Created By : Ahmed Mobasher Khan
 * Creation Date : Aug 11, 2014
 * 
 * Purpose : 
 * 
 * Updated By : 
 * Updated Date : 
 * Comments : 
 * </pre>
 */
public class CallOriginatorTask extends Task
{	
	private Map<String, String> nextTaskMap = new HashMap<String, String>();
	
//   private String channel;
   private String exten;
   private String callContext;
   private Integer priority = 1;
//   private String callerId;
   private Boolean isAsync;
   private long timeout = 30000L;
   private Map<String,String> channelMap = new HashMap<String,String>();
   private String agiUrl;
   private List<Long> retryAfter = new ArrayList<Long>();
   private Timer retryTimer = new Timer(true);

//   private ManagerConnection managerConnection;
//   private AsteriskServer asteriskServer;
   private List<AsteriskServerWrapper> asteriskServerWrappers;
      

   @SuppressWarnings({ "unchecked", "rawtypes" })
   @Override
	public void process(final Map<String, Object> dataMap)
	{
	   int count = getRetryCount(dataMap);
	   AsteriskServerWrapper asteriskServerWrapper = getAsteriskServerWrapper(dataMap);
	   
       String resp = IvrConstant.SUCCESS;

	   if(count < getTotalAttempts())
	   {
         OriginateAction originateAction = getOriginateAction(dataMap, asteriskServerWrapper);
         try {
//             managerConnection.sendAction(originateAction, getTimeout());
	    	 	asteriskServerWrapper.getAsteriskServer().originateAsync(originateAction, new OriginateCallback() {
					
					@Override
					public void onSuccess(AsteriskChannel channel) {
						dataMap.put(TaskConstant.TASK_STATUS, IvrConstant.SUCCESS);
						dataMap.put(TaskConstant.NEXT_TASK_ID, nextTaskMap.get(IvrConstant.SUCCESS));
						dataMap.put(IvrConstant.UNIQUE_ID, channel.getId());
						logger.info("{} call connected successfully. {}",IvrUtil.getWsParamForLogging(dataMap),channel);
						getContext().startFlow(dataMap);
					}
					
					@Override
					public void onNoAnswer(AsteriskChannel channel) {
						dataMap.put(IvrConstant.UNIQUE_ID, channel.getId());
						if(channel.getHangupCause() != null)
						{
							logger.info("{}, attempt to call failed due to no answer. {}",IvrUtil.getWsParamForLogging(dataMap),channel);
							dataMap.put(TaskConstant.TASK_STATUS, channel.getHangupCauseText());
							dataMap.put(TaskConstant.NEXT_TASK_ID, nextTaskMap.get(IvrConstant.NO_ANSWER));
							retry(dataMap);
						}
						else
						{
							onSuccess(channel);
						}
					}
					
					@Override
					public void onFailure(LiveException cause) {
						logger.info("{}, attempt to call failed... {}", IvrUtil.getWsParamForLogging(dataMap),cause);
						dataMap.put(TaskConstant.TASK_STATUS, cause.getMessage());
						dataMap.put(TaskConstant.NEXT_TASK_ID, nextTaskMap.get(IvrConstant.FAILURE));
						retryServer(dataMap);
					}
					
					@Override
					public void onDialing(AsteriskChannel channel) {
						dataMap.put(IvrConstant.UNIQUE_ID, channel.getId());
						logger.info("{}, in dialing state... {}", IvrUtil.getWsParamForLogging(dataMap),channel);
					}
					
					@Override
					public void onBusy(AsteriskChannel channel) {
						if(channel.getHangupCause() != null)
						{
							dataMap.put(TaskConstant.TASK_STATUS, channel.getHangupCauseText());
							dataMap.put(TaskConstant.NEXT_TASK_ID, nextTaskMap.get(IvrConstant.BUSY));
							dataMap.put(IvrConstant.UNIQUE_ID, channel.getId());
							logger.info("{}, attempt to call failed due to user busy... {}",IvrUtil.getWsParamForLogging(dataMap),channel);
							retry(dataMap);
						}
					}
				});	
			} catch (Exception e) {
				logger.error(IvrUtil.getWsParamForLogging(dataMap), e);
				dataMap.put(IvrConstant.RESPONSE_CODE, StatusCodeConstant.CODE_9001);
				dataMap.put(TaskConstant.NEXT_TASK_ID, nextTaskMap.get(IvrConstant.ORIGINATE_EXCEPTION));
				retryServer((Map<String, Object>) ((HashMap)dataMap).clone());
//				resp = "Failure: " + e.toString();
			}
         	dataMap.put(TaskConstant.TASK_STATUS, resp);
	   }
	   else
	   {
		   logger.info("{}, retry attempts ({}) exhausted: {}",IvrUtil.getWsParamForLogging(dataMap),count,getTotalAttempts());
		   dataMap.remove(IvrConstant.RETRY_COUNT);
		   dataMap.put(IvrConstant.RESPONSE_CODE, StatusCodeConstant.CODE_9001);				
		   resp = IvrConstant.RETRY_OVER;
	   }

   		dataMap.put(TaskConstant.NEXT_TASK_ID, nextTaskMap.get(resp));
	}


   private void retryServer(final Map<String, Object> dataMap)
{
	   Integer serverRetryCount = (Integer) dataMap.get(IvrConstant.SERVER_RETRY_COUNT);
	   
	   if(serverRetryCount == null) {
		   serverRetryCount = 1;
	   } else {
		   serverRetryCount += 1;
	   }
	   dataMap.put(IvrConstant.SERVER_RETRY_COUNT, serverRetryCount);
	   
	   Integer serverIndex = (Integer) dataMap.get(IvrConstant.SERVER_INDEX);
	   
	   if(serverRetryCount >=3) {
		   serverIndex += 1;
		   dataMap.put(IvrConstant.SERVER_INDEX, serverIndex);
		   if(serverIndex >= asteriskServerWrappers.size()) {
			   dataMap.put(IvrConstant.RESPONSE_CODE, StatusCodeConstant.CODE_9001);				
			   dataMap.put(TaskConstant.NEXT_TASK_ID, nextTaskMap.get(IvrConstant.RETRY_OVER));
		   } else {
			   dataMap.remove(IvrConstant.SERVER_RETRY_COUNT);
		   }
	   }
	   
	   new Thread(new Runnable()
	{
		
		@Override
		public void run()
		{
			getContext().startFlow(dataMap);
		}
	}).start();
}


private AsteriskServerWrapper getAsteriskServerWrapper(
		Map<String, Object> dataMap)
{
	   Integer serverIndex = (Integer) dataMap.get(IvrConstant.SERVER_INDEX);

	   if(serverIndex == null) {
		   serverIndex = 0;
		   dataMap.put(IvrConstant.SERVER_INDEX, serverIndex);
	   }

	   return asteriskServerWrappers.get(serverIndex);
}


private int getRetryCount(Map<String, Object> dataMap)
{
	   int count = 0;
	   if(dataMap.get(IvrConstant.RETRY_COUNT) == null)
	   {
		   dataMap.put(IvrConstant.RETRY_COUNT, count);
	   }
	   else
	   {
		   count = (Integer) dataMap.get(IvrConstant.RETRY_COUNT);		   
	   }
	   
	   return count;
}


private void retry(final Map<String, Object> dataMap)
   {
		int count = (Integer) dataMap.get(IvrConstant.RETRY_COUNT);

		dataMap.put(IvrConstant.RETRY_COUNT, count+1);
		
		if((count+1) < getTotalAttempts())
		{
			logger.info("{}, retry count: {}, retry delay: {}",IvrUtil.getWsParamForLogging(dataMap), count, getRetryAfter(count));

			retryTimer.schedule(new TimerTask() {
	
				@Override
				public void run() {
					new Thread() {
						public void run() {
							getContext().startFlow(dataMap);
						}
					}.start();
				}
				
			}, getRetryAfter(count));
		}
		else
		{
			getContext().startFlow(dataMap);
		}
   }
   
   /**<pre>
	 * 
	 * @author - Ahmed Mobasher Khan 
	 * @param dataMap 
 * @param asteriskServerWrapper 
	 * 
	 * @return - 
	 * </pre>
	 */
	@SuppressWarnings("unchecked")
	private OriginateAction getOriginateAction(Map<String, Object> dataMap, AsteriskServerWrapper asteriskServerWrapper)
	{
      OriginateAction originateAction = new OriginateAction();

  	Map<String, String> wsParamMap = (Map<String, String>) dataMap.get(IvrConstant.WS_PARAMS);

 
      if(channelMap != null && channelMap.isEmpty() == false)
      {
      	originateAction.setVariables(channelMap);
      }
//      if(dataMap.containsKey(IvrConstant.LANG_ID))
//      {
//      	originateAction.setVariable(IvrConstant.LANG_ID, (String) dataMap.get(IvrConstant.LANG_ID));
//      }
//
//      if(dataMap.containsKey(IvrConstant.WS_PARAMS))
//      {
//      	Map<String, String> wsParamMap = (Map<String, String>) dataMap.get(IvrConstant.WS_PARAMS);
//
//      	originateAction.setVariable(IvrConstant.WS_PARAM__MAP, IvrUtil.mapToTokenString(wsParamMap));
//      	
//      	if(wsParamMap.containsKey(IvrConstant.CUSTOMER_MOBILE))
//      	{
//      		originateAction.setVariable(IvrConstant.CALLEE, StringUtil.setNullToEmpty(wsParamMap.get(IvrConstant.CUSTOMER_MOBILE)));
//      	}
//      }

      String channel = asteriskServerWrapper.getChannel();

      channel = channel.substring(0, channel.indexOf('/') + 1) + StringUtil.setNullToEmpty(wsParamMap.get(IvrConstant.CUSTOMER_MOBILE)) + channel.substring(channel.indexOf('@'));
   	originateAction.setChannel(channel);
//      if(getContext() != null)
//      	originateAction.setContext(getContext());
//      if(getExten() != null)
//      	originateAction.setExten(getExten());
//      originateAction.setPriority(getPriority());
      originateAction.setTimeout(getTimeout());
      if(isAsync() != null)
      	originateAction.setAsync(isAsync());
      if(asteriskServerWrapper.getCallerId() != null)
      	originateAction.setCallerId(asteriskServerWrapper.getCallerId());

      originateAction.setApplication("AGI");
		try
		{
			originateAction.setData(agiUrl
					+ "?"
					+ TaskConstant.NEXT_TASK_ID
					+ "="
					+ channelMap.get(TaskConstant.NEXT_TASK_ID)
					+ "&"
					+ IvrConstant.WS_PARAM__MAP
					+ "="
					+ URLEncoder.encode(IvrUtil.mapToTokenString(wsParamMap),
							"UTF-8"));
		} catch (UnsupportedEncodingException e)
		{
			throw new BaseException(e);
		}
		originateAction.setAccount(StringUtil.setNullToEmpty(wsParamMap.get(IvrConstant.CUSTOMER_MOBILE)));

      return originateAction;
	}

	public String getExten()
	{
		return exten;
	}

	public void setExten(String exten)
	{
		this.exten = exten;
	}

	public String getCallContext()
	{
		return callContext;
	}

	public void setContext(String callContext)
	{
		this.callContext = callContext;
	}

	public Integer getPriority()
	{
		return priority;
	}

	public void setPriority(Integer priority)
	{
		this.priority = priority;
	}

/*	public String getCallerId()
	{
		return callerId;
	}

	public void setCallerId(String callerId)
	{
		this.callerId = callerId;
	}

	public String getChannel()
	{
		return channel;
	}
	
	public void setChannel(String channel)
	{
		this.channel = channel;
	}
*/
	/**<pre>
	 * 
	 * @author - Ahmed Mobasher Khan 
	 *
	 * @return - Returns the isAsync
	 * </pre>
	 */
	public Boolean isAsync()
	{
		return isAsync;
	}

	/**<pre>
	 * 
	 * @author - Ahmed Mobasher Khan 
	 *
	 * @param isAsync - the isAsync to set
	 * </pre>
	 */
	public void setIsAsync(boolean isAsync)
	{
		this.isAsync = isAsync;
	}

	/**<pre>
	 * 
	 * @author - Ahmed Mobasher Khan 
	 *
	 * @return - Returns the timeout
	 * </pre>
	 */
	public long getTimeout()
	{
		return timeout;
	}

	/**<pre>
	 * 
	 * @author - Ahmed Mobasher Khan 
	 *
	 * @param timeout - the timeout to set
	 * </pre>
	 */
	public void setTimeout(long timeout)
	{
		this.timeout = timeout;
	}
	
	public void setNextTaskMap(String status, String taskId)
	{
		this.nextTaskMap.put(status, taskId);
	}

	public Map<String, String> getChannelMap()
	{
		return channelMap;
	}

	public void setChannelMap(Map<String, String> channelMap)
	{
		this.channelMap = channelMap;
	}
	
	public void setChannelMap(String key, String value)
	{
		this.channelMap.put(key, value);
	}


	/**
	 * @return the agiUrl
	 */
	public String getAgiUrl() {
		return agiUrl;
	}


	/**
	 * @param agiUrl the agiUrl to set
	 */
	public void setAgiUrl(String agiUrl) {
		this.agiUrl = agiUrl;
	}


	public long getRetryAfter(int index) throws TaskPropertyException {
		if(this.retryAfter.size() == 0)
		{
			throw new TaskPropertyException("Retry timeout not set");
		}
		if(index < retryAfter.size())
		{
			return retryAfter.get(index);
		}
		else
		{
			return retryAfter.get(index - 1);
		}
	}


	public void setRetryAfter(long retryAfter) {
		this.retryAfter.add(retryAfter);
	}


	public List<AsteriskServerWrapper> getAsteriskServerWrappers()
	{
		return asteriskServerWrappers;
	}


	public void setAsteriskServerWrappers(List<AsteriskServerWrapper> asteriskServerWrappers)
	{
		this.asteriskServerWrappers = asteriskServerWrappers;
	}

}
