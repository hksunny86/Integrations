package com.inov8.ivr.script;

import io.task.exception.BaseException;
import io.task.util.StringUtil;
import io.task.util.TaskConstant;

import java.util.HashMap;
import java.util.Map;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inov8.ivr.agi.service.AgiManager;
import com.inov8.util.IvrConstant;
import com.inov8.util.IvrUtil;

public class JavaAgiScript extends BaseAgiScript
{
	private static final Logger	log = LoggerFactory.getLogger(JavaAgiScript.class);
	private AgiManager agiManager;

	public JavaAgiScript(AgiManager agiManager)
	{
		this.agiManager = agiManager;
	}


	@Override
	public void service(AgiRequest request, AgiChannel channel)
	{
		if(log.isInfoEnabled() == true) {
			log.info(TaskConstant.START_STRING);
		}

		Map<String, Object> dataMap = new HashMap<String, Object>();

		dataMap.put(IvrConstant.DO_HANGUP_CALL, true);
		dataMap.put(IvrConstant.UNIQUE_ID, request.getUniqueId());
		dataMap.put(IvrConstant.CALLER, request.getCallerIdNumber());

		try {
			Map<String,String[]> requestMap = request.getParameterMap();

			for(String str : requestMap.keySet())
			{
				String arr[] = requestMap.get(str);
				if(arr != null && arr.length > 0)
				{
					if(str.endsWith("_map"))
					{
						IvrUtil.parseTokenToMap(dataMap, arr);
					}
					else
					{
						dataMap.put(str, arr.length == 1 ? arr[0] : arr);
					}
				}
			}

			dataMap.put(IvrConstant.JAVA_AGI_SCRIPT, this);
			
			String ivrId = request.getParameter(TaskConstant.NEXT_TASK_ID);

			if(log.isInfoEnabled() == true) {
				log.info(StringUtil.mergeStrings(TaskConstant.NEXT_TASK_ID, IvrConstant.EQUAL_SIGN_WITH_SPACE, ivrId));
			}

			super.answer();
			agiManager.process(dataMap);

		} catch (BaseException be) {
			if(be.getCause() instanceof AgiException)
				dataMap.put(IvrConstant.DO_HANGUP_CALL, false);
			log.error("",be);
		}
		catch (Exception ex) {
			log.error("",ex);
		} finally {
			try {
				if((Boolean)dataMap.get(IvrConstant.DO_HANGUP_CALL) == true) {
					hangup();
				}
			} catch(Exception e) {
			}
			if(log.isInfoEnabled() == true) {
				log.info(TaskConstant.END_STRING);
			}
		}

	}

}
