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

package com.inov8.ivr.thread.config;

import io.task.context.Context;
import io.task.context.ContextImpl;
import io.task.exception.BaseException;
import io.task.factory.BeanFactory;
import io.task.tasks.PropertyMapperTask;
import io.task.tasks.Task;
import io.task.tasks.group.TaskGroup;
import io.task.util.TaskConstant;

import java.util.List;
import java.util.Map;

import org.asteriskjava.live.AsteriskServer;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.event.ManagerEvent;

import com.inov8.ivr.task.call.CallOriginatorTask;
import com.inov8.ivr.task.input.MultiCharInputTask;
import com.inov8.ivr.task.ws.KeyInOtpTask;
import com.inov8.ivr.task.ws.KeyInTask;
import com.inov8.ivr.task.ws.PinVerifierTask;
import com.inov8.ivr.wrapper.AsteriskServerWrapper;
import com.inov8.microbank.ws.service.WsClient;
import com.inov8.spring.ApplicationContextImpl;
import com.inov8.util.IvrConstant;
import com.inov8.util.ProjectPropertiesUtil;
import com.inov8.util.StatusCodeConstant;

/**<pre>
 * Created By : Ahmed Mobasher Khan
 * Creation Date : Aug 13, 2014
 * 
 * Purpose : 
 * 
 * Updated By : 
 * Updated Date : 
 * Comments : 
 * </pre>
 */
public class CardFlow
{
	private Map<String, Task>		taskContext;
//	private String channel ;//= "SIP/@wateen.net"; //"Local/@phones";//"SIP/6002";//
//	private String callerId;
	
	private	Context context;


	public CardFlow(Context context,/* String channel,*/ Map<String, Task> taskContext)
	{
		this.context = context;
//		this.channel = channel;
//		this.callerId = ProjectPropertiesUtil.getProperty(IvrConstant.PROP_CALLER_ID);

		this.taskContext = taskContext;
//		taskContext = new HashMap<String, Task>();		
		((ContextImpl)context).setBeanFactory(new BeanFactory() {
			
			@Override
			public Void load() throws BaseException {
				return null;
			}
			
			@Override
			public boolean taskExists(String taskId) {
				return CardFlow.this.taskContext.containsKey(taskId);
			}
			
			@Override
			public Task getTask(String taskId) {
				return CardFlow.this.taskContext.get(taskId);
			}
			
			@Override
			public Object getBean(String beanId) {
				return null;
			}
			
			@Override
			public boolean beanExists(String beanId) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		loadTask();

	}

	public void setChannel(String channel)
	{
//		this.channel = channel;
	}

	void loadTask()
	{
//		addHangupFlow();
		baseFlow("cardtx_");
	}

	private void addHangupFlow()
	{
		PinVerifierTask pvt = new PinVerifierTask();
		
		pvt.setWsClient(ApplicationContextImpl.getBean("wsClient", WsClient.class));//TODO auto resolution required
		pvt.setContext(context);
		
		taskContext.put(IvrConstant.HANGUP_FLOW, pvt);

	}
	
	void addLanguageSelection(String taskPrefix, String nextTaskId)
	{
		MultiCharInputTask mcit = new MultiCharInputTask();
		
//		mcit.setCurrentAttempt(1);
		mcit.setTotalAttempts(3);
		mcit.setPromptSoundFiles(0, "*common/mpos/hbl/lang_selection");
//		mcit.setInterKeyTimeout(100);
//		mcit.setLastKeyTimeout(100);
		mcit.setExitOnFirst('1', taskPrefix + "14");
		mcit.setExitOnFirst('2', taskPrefix + "14");
		mcit.setAcceptablePattern("12");
		mcit.setDefaultNextTask(taskPrefix + "24");
		
		mcit.setContext(context);

		taskContext.put(taskPrefix + "1", mcit);

		PropertyMapperTask pmt = new PropertyMapperTask();
		
		pmt.setPropertyMap(IvrConstant.USER_INPUT, IvrConstant.LANG_ID, "1", "ur");
		pmt.setPropertyMap(IvrConstant.USER_INPUT, IvrConstant.LANG_ID, "2", "en");
		pmt.setNextTaskId(taskPrefix + nextTaskId);
		
		pmt.setContext(context);
		
		taskContext.put(taskPrefix + "14", pmt);
	}
	
	void addGroup(String taskPrefix)
	{
		TaskGroup tg = new TaskGroup();
		
//		tg.setTotalAttempts(3);
		tg.addTask(taskPrefix + "1");
//		tg.addTask("2");
		tg.addTask(taskPrefix + "3");
		tg.addTask(taskPrefix + "5");
		tg.addTask(taskPrefix + "6");
		tg.addTask(taskPrefix + "7");
		tg.addTask(taskPrefix + "8");
		tg.addTask(taskPrefix + "9");
		tg.addTask(taskPrefix + "10");
		tg.addTask(taskPrefix + "11");
		tg.addTask(taskPrefix + "12");
		tg.addTask(taskPrefix + "13");
		tg.addTask(taskPrefix + "14");
		tg.addTask(taskPrefix + "21");
		tg.setDefaultTaskId(taskPrefix + "1");
		
		tg.setContext(context);
		
		taskContext.put(taskPrefix + "2", tg);
	}
	
	void addPinPrompt(String taskPrefix, String ...soundFiles)
	{
		MultiCharInputTask mcit = new MultiCharInputTask();

		mcit = new MultiCharInputTask();
		
//		mcit.setCurrentAttempt(1);
//		mcit.setTotalAttempts(3);
//		mcit.setInterKeyTimeout(100);
		for(int i=0;i<soundFiles.length;i++)
		{
			mcit.setPromptSoundFiles(i, soundFiles[i]);
		}
		mcit.setDefaultNextTask(taskPrefix + "8");
		mcit.setValidLengthNextTask(taskPrefix + "23");
		
		mcit.setContext(context);
		
		taskContext.put(taskPrefix + "44", mcit);

		PropertyMapperTask pmt = new PropertyMapperTask();
		
		pmt.setPropertyMap(IvrConstant.USER_INPUT, IvrConstant.PIN, "", "");
		pmt.addData(IvrConstant.RESPONSE_CODE, StatusCodeConstant.CODE_0000);
		pmt.setNextTaskId(taskPrefix + "38");
		
		pmt.setContext(context);

		taskContext.put(taskPrefix + "23", pmt);

	}
	
	private static AsteriskServer asteriskServer = null;
	
	private static AsteriskServer getAsteriskServer()
	{
		if(asteriskServer == null)
		{
			asteriskServer = (AsteriskServer) ApplicationContextImpl.getBean("defaultAsteriskServer"); 
					//new DefaultAsteriskServer("asteriskserverip", 5038, "manager", "1234");
//			asteriskServer = new DefaultAsteriskServer(getManagerConnection());
			asteriskServer.initialize();
		}
		return asteriskServer;
	}
	
	private static ManagerConnection managerConnection = null;
	
	private static ManagerConnection getManagerConnection()
	{
		if(managerConnection == null)
		{
			managerConnection = new ManagerConnectionFactory("asteriskserverip", 5038, "manager", "1234").createManagerConnection();
			managerConnection.addEventListener(new ManagerEventListener() {
				
				@Override
				public void onManagerEvent(ManagerEvent event) {
					System.out.println("first here");
					System.out.println(event);
				}
			});
			try {
				managerConnection.login("call");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return managerConnection;
	}

	void addCallOriginator(String taskPrefix)
	{
		CallOriginatorTask cot = new CallOriginatorTask(/*getAsteriskServer() *//*getManagerConnection()*/);
		
//		cot.setChannel(channel);
//		cot.setContext("phones");
//		cot.setExten("1300");
//		cot.setPriority(new Integer(1));
		cot.setChannelMap(TaskConstant.NEXT_TASK_ID, taskPrefix + "2");
//		cot.setCallerId(callerId);
		cot.setTimeout(30000L);
		cot.setIsAsync(true);
		cot.setAgiUrl(ProjectPropertiesUtil.getProperty(IvrConstant.PROP_AGI_URL));
		cot.setTotalAttempts(Integer.parseInt(ProjectPropertiesUtil.getProperty(taskPrefix+".call.totalattempts", ProjectPropertiesUtil.getProperty("call.totalattempts", "1"))));
		cot.setNextTaskMap(IvrConstant.NO_ANSWER, taskPrefix);
		cot.setNextTaskMap(IvrConstant.BUSY, taskPrefix);
		cot.setNextTaskMap(IvrConstant.FAILURE, taskPrefix);
		cot.setNextTaskMap(IvrConstant.SUCCESS, "");//TODO 
		cot.setNextTaskMap(IvrConstant.RETRY_OVER, "");//TODO send notification to server on failure
		if(cot.getTotalAttempts() > 1)
		{
			String time[] = ProjectPropertiesUtil.getProperty(taskPrefix+".call.timeouts", ProjectPropertiesUtil.getProperty("call.timeouts")).split("\\,");
			for(String t : time)
			{
				cot.setRetryAfter(Long.parseLong(t)*1000);
			}
//			cot.setRetryAfter(5000);
//			cot.setRetryAfter(10000);
		}
		
		cot.setContext(context);
		cot.setAsteriskServerWrappers(getAsteriskServerWrappers());

		taskContext.put(taskPrefix, cot);
		
	}
	
	void addGoodbye(String taskPrefix)
	{
		MultiCharInputTask mcit = new MultiCharInputTask();
		
		mcit.setPromptSoundFiles(0, "mpos/hbl/goodbye");
		mcit.setAcceptablePattern("");
//		mcit.setDefaultNextTask("8");
		mcit.setInterKeyTimeout(100);
		mcit.setLastKeyTimeout(100);
		mcit.setTotalAttempts(1);
		mcit.setContext(context);
		
		taskContext.put(taskPrefix + "8", mcit);
	}

	void baseFlow(String taskPrefix)
	{
		addLanguageSelection(taskPrefix, "33");

		addGroup(taskPrefix);
		
		addCardNumPrompt(taskPrefix, new String[]{"mpos/customer_data_input"});
		
		addAmountPrompt(taskPrefix);

		addVerifyForOtp(taskPrefix);
		
		addPinPrompt(taskPrefix, "mpos/otp_input");
		
		addVerifyForKeyIn(taskPrefix);
		
//		addCallOriginator(taskPrefix);

		addGoodbye(taskPrefix);
	}
	
	private void addVerifyForKeyIn(String taskPrefix)
	{

		KeyInTask kit = new KeyInTask();
		
		kit.setNextTask(TaskConstant.DEFAULT_TASK, taskPrefix + "42");

		kit.setNextTask(StatusCodeConstant.CODE_0000, taskPrefix + "43");
		kit.setNextTask(StatusCodeConstant.CODE_9019, taskPrefix + "39");
		kit.setNextTask(StatusCodeConstant.CODE_9021, taskPrefix + "40");
		kit.setNextTask(StatusCodeConstant.CODE_9022, taskPrefix + "41");
		kit.setNextTask(StatusCodeConstant.CODE_9010, taskPrefix + "45");
		kit.setNextTask(StatusCodeConstant.CODE_9015, taskPrefix + "46");

		//TODO needs improvement for auto resolution
		kit.setWsClient(ApplicationContextImpl.getBean("wsClient", WsClient.class));
		
		kit.setContext(context);
		
		taskContext.put(taskPrefix + "38", kit);

		MultiCharInputTask mcit = new MultiCharInputTask();

		mcit = new MultiCharInputTask();
		
		mcit.setPromptSoundFiles(0, "mpos/invalid_pin");
		mcit.setAcceptablePattern("");
		mcit.setDefaultNextTask(taskPrefix + "44");
		mcit.setInterKeyTimeout(100);
		mcit.setLastKeyTimeout(100);
		mcit.setTotalAttempts(1);
		
		mcit.setContext(context);
		
		taskContext.put(taskPrefix + "45", mcit);

		mcit = new MultiCharInputTask();
		
		mcit.setPromptSoundFiles(0, "mpos/otp_expired");
		mcit.setAcceptablePattern("");
		mcit.setDefaultNextTask(taskPrefix + "8");
//		mcit.setNoResponseNextTask(taskPrefix + "3");
//		mcit.setValidLengthNextTask(taskPrefix + "25");
		mcit.setInterKeyTimeout(100);
		mcit.setLastKeyTimeout(100);
		mcit.setTotalAttempts(1);
		
		mcit.setContext(context);

		taskContext.put(taskPrefix + "46", mcit);
	}

	private void addVerifyForOtp(String taskPrefix)
	{
		KeyInOtpTask kitt = new KeyInOtpTask();
		
		kitt.setNextTask(TaskConstant.DEFAULT_TASK, taskPrefix + "42");

		kitt.setNextTask(StatusCodeConstant.CODE_0000, taskPrefix + "44");
		kitt.setNextTask(StatusCodeConstant.CODE_9019, taskPrefix + "39");
		kitt.setNextTask(StatusCodeConstant.CODE_9021, taskPrefix + "40");
		kitt.setNextTask(StatusCodeConstant.CODE_9022, taskPrefix + "41");

		//TODO needs improvement for auto resolution
		kitt.setWsClient(ApplicationContextImpl.getBean("wsClient", WsClient.class));
		
		kitt.setContext(context);
		
		taskContext.put(taskPrefix + "37", kitt);

		MultiCharInputTask mcit = new MultiCharInputTask();

		mcit = new MultiCharInputTask();
		
		mcit.setPromptSoundFiles(0, "mpos/invalid_customer");
		mcit.setAcceptablePattern("");
		mcit.setDefaultNextTask(taskPrefix + "33");
//		mcit.setNoResponseNextTask(taskPrefix + "33");
//		mcit.setValidLengthNextTask(taskPrefix + "25");
		mcit.setInterKeyTimeout(100);
		mcit.setLastKeyTimeout(100);
		mcit.setTotalAttempts(1);
		
		mcit.setContext(context);
		
		taskContext.put(taskPrefix + "39", mcit);

		mcit = new MultiCharInputTask();
		
		mcit.setPromptSoundFiles(0, "mpos/tx_amount_limit_exceeded");
		mcit.setAcceptablePattern("");
		mcit.setDefaultNextTask(taskPrefix + "8");
		mcit.setInterKeyTimeout(100);
		mcit.setLastKeyTimeout(100);
		mcit.setTotalAttempts(1);
		
		mcit.setContext(context);
		
		taskContext.put(taskPrefix + "40", mcit);

		mcit = new MultiCharInputTask();
		
		mcit.setPromptSoundFiles(0, "mpos/tx_amount_limit_exceeds");
		mcit.setAcceptablePattern("");
		mcit.setDefaultNextTask(taskPrefix + "35");
		mcit.setInterKeyTimeout(100);
		mcit.setLastKeyTimeout(100);
		mcit.setTotalAttempts(1);
		
		mcit.setContext(context);
		
		taskContext.put(taskPrefix + "41", mcit);

		mcit = new MultiCharInputTask();
		
		mcit.setPromptSoundFiles(0, "mpos/tx_fail");
		mcit.setAcceptablePattern("");
//		mcit.setDefaultNextTask(taskPrefix + "8");
		mcit.setInterKeyTimeout(100);
		mcit.setLastKeyTimeout(100);
		mcit.setTotalAttempts(1);
		
		mcit.setContext(context);
		
		taskContext.put(taskPrefix + "42", mcit);

		mcit = new MultiCharInputTask();
		
		mcit.setPromptSoundFiles(0, "mpos/tx_success");
		mcit.setAcceptablePattern("");
//		mcit.setDefaultNextTask(taskPrefix + "8");
		mcit.setInterKeyTimeout(100);
		mcit.setLastKeyTimeout(100);
		mcit.setTotalAttempts(1);
		
		mcit.setContext(context);
		
		taskContext.put(taskPrefix + "43", mcit);
	}

	private void addAmountPrompt(String taskPrefix)
	{
		MultiCharInputTask mcit = new MultiCharInputTask();

		mcit = new MultiCharInputTask();
		
//		mcit.setCurrentAttempt(1);
//		mcit.setTotalAttempts(3);
//		mcit.setInterKeyTimeout(100);
		mcit.setPromptSoundFiles(0, "mpos/tx_amount_input");
		mcit.setDefaultNextTask(taskPrefix + "24");
		mcit.setValidLengthNextTask(taskPrefix + "36");
		
		mcit.setContext(context);
		
		taskContext.put(taskPrefix + "35", mcit);

		PropertyMapperTask pmt = new PropertyMapperTask();
		
		pmt.setPropertyMap(IvrConstant.USER_INPUT, IvrConstant.TX_AMOUNT, "", "");
		pmt.addData(IvrConstant.RESPONSE_CODE, StatusCodeConstant.CODE_0000);
		pmt.setNextTaskId(taskPrefix + "37");
		
		pmt.setContext(context);

		taskContext.put(taskPrefix + "36", pmt);
	}

	private void addCardNumPrompt(String taskPrefix, String[] soundFiles)
	{
		MultiCharInputTask mcit = new MultiCharInputTask();

		mcit = new MultiCharInputTask();
		
//		mcit.setCurrentAttempt(1);
//		mcit.setTotalAttempts(3);
//		mcit.setInterKeyTimeout(100);
		for(int i=0;i<soundFiles.length;i++)
		{
			mcit.setPromptSoundFiles(i, soundFiles[i]);
		}
		mcit.setDefaultNextTask(taskPrefix + "24");
		mcit.setValidLengthNextTask(taskPrefix + "34");
		
		mcit.setContext(context);
		
		taskContext.put(taskPrefix + "33", mcit);

		PropertyMapperTask pmt = new PropertyMapperTask();
		
		pmt.setPropertyMap(IvrConstant.USER_INPUT, IvrConstant.CUSTOMER_DATA, "", "");
		pmt.addData(IvrConstant.RESPONSE_CODE, StatusCodeConstant.CODE_0000);
		pmt.setNextTaskId(taskPrefix + "35");
		
		pmt.setContext(context);

		taskContext.put(taskPrefix + "34", pmt);
	}

	@SuppressWarnings("unchecked")
	private List<AsteriskServerWrapper> getAsteriskServerWrappers()
	{
		return (List<AsteriskServerWrapper>) ApplicationContextImpl.getBean("asteriskServers");
	}
}

