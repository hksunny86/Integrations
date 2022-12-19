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
import io.task.tasks.ComparatorTask;
import io.task.tasks.PropertyMapperTask;
import io.task.tasks.Task;
import io.task.tasks.group.TaskGroup;
import io.task.util.StringUtil;
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
import com.inov8.ivr.task.input.NumberParser;
import com.inov8.ivr.task.input.PashtoNumberParserImpl;
import com.inov8.ivr.task.ws.PinVerifierTask;
import com.inov8.ivr.wrapper.AsteriskServerWrapper;
import com.inov8.microbank.ws.service.WsClient;
import com.inov8.spring.ApplicationContextImpl;
import com.inov8.util.IvrConstant;
import com.inov8.util.ProductConstant;
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
public class Flow
{
	private Map<String, Task>		taskContext;
//	private String channel ;//= "SIP/@wateen.net"; //"Local/@phones";//"SIP/6002";//
//	private String callerId;
	
	private	Context context;
	private NumberParser pashtoNumberParser = new PashtoNumberParserImpl();

	public Flow(Context context/*, String channel*/, Map<String, Task> taskContext)
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
				return Flow.this.taskContext.containsKey(taskId);
			}
			
			@Override
			public Task getTask(String taskId) {
				return Flow.this.taskContext.get(taskId);
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
		testFlow();
		addHangupFlow();
		flow50010();//BLB to CNIC
		flow50000();//BLB to BLB
		flow50006();//Cash Out
		flow50031();//Customer Retail Payment
		flow50026();//BLB to Core Account
		flow6();// check balance
		flow122();// mini statement
		flow123();// change pin
		flow125();//new pin
        flow134();//new pin - Customer
		flow126();//regenerate pin
		flow2510700();// bill payment consumer
		flow2510715();// mobile bill payment
		flow2510763(IvrConstant.BANK_NRSP+ProductConstant.PROD_ID_2510763, IvrConstant.BANK_NRSP + "/products/"+ProductConstant.PROD_ID_2510763+"/account_approved");
		flowNrspDb(IvrConstant.BANK_NRSP+ProductConstant.PROD_ID_50006);
		flowNrspCr(IvrConstant.BANK_NRSP+ProductConstant.PROD_ID_50002, IvrConstant.BANK_NRSP + "/products/common/1", "{#"+IvrConstant.AGENT_ID+"}", 
				IvrConstant.BANK_NRSP + "/products/common/4", "{$"+IvrConstant.TX_AMOUNT+"}", IvrConstant.BANK_NRSP + "/products/common/5");
		flowNrspCr(IvrConstant.BANK_NRSP+ProductConstant.PROD_ID_2510733, IvrConstant.BANK_NRSP + "/products/common/6", "{$"+IvrConstant.TX_AMOUNT+"}", IvrConstant.BANK_NRSP + "/products/common/5");
	}

	private void addHangupFlow()
	{
		PinVerifierTask pvt = new PinVerifierTask();
		
		pvt.setWsClient(ApplicationContextImpl.getBean("wsClient", WsClient.class));//TODO auto resolution required
		pvt.setContext(context);
		
		taskContext.put(IvrConstant.HANGUP_FLOW, pvt);

	}
	
	private void testFlow() {
		MultiCharInputTask mcit = new MultiCharInputTask();
		
		mcit.setTotalAttempts(3);
		mcit.setPromptSoundFiles(0, "*pleasehold");
		mcit.setPromptSoundFiles(1, "*asterisksuccess");
		
		mcit.setContext(context);
		
		taskContext.put("testFlow", mcit);
	}

	void addEnUrLanguageSelection(String taskPrefix, String nextTaskId)
	{
		MultiCharInputTask mcit = new MultiCharInputTask();
		
//		mcit.setCurrentAttempt(1);
		mcit.setTotalAttempts(3);
		mcit.setPromptSoundFiles(0, "*common/branchless_banking/lang_selection");
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
	
	void addEnUrPaLanguageSelection(String taskPrefix, String nextTaskId)
	{
		MultiCharInputTask mcit = new MultiCharInputTask();
		
//		mcit.setCurrentAttempt(1);
		mcit.setTotalAttempts(3);
		mcit.setPromptSoundFiles(0, "*common/branchless_banking/en_ur_pa_lang_selection");
//		mcit.setInterKeyTimeout(100);
//		mcit.setLastKeyTimeout(100);
		mcit.setExitOnFirst('1', taskPrefix + "14");
		mcit.setExitOnFirst('2', taskPrefix + "14");
		mcit.setExitOnFirst('3', taskPrefix + "14");
		mcit.setAcceptablePattern("123");
		mcit.setDefaultNextTask(taskPrefix + "24");
		
		mcit.setContext(context);

		taskContext.put(taskPrefix + "1", mcit);

		PropertyMapperTask pmt = new PropertyMapperTask();
		
		pmt.setPropertyMap(IvrConstant.USER_INPUT, IvrConstant.LANG_ID, "1", "ur");
		pmt.setPropertyMap(IvrConstant.USER_INPUT, IvrConstant.LANG_ID, "2", "en");
		pmt.setPropertyMap(IvrConstant.USER_INPUT, IvrConstant.LANG_ID, "3", IvrConstant.LANG_ID_PASHTO);
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
//		NumberParserImpl pashtoNumParser = new NumberParserImpl();
		
//		pashtoNumParser.getAfterUnits()[2] = "above";
		
		MultiCharInputTask mcit = new MultiCharInputTask();

		mcit = new MultiCharInputTask();
		
//		mcit.setCurrentAttempt(1);
//		mcit.setTotalAttempts(3);
		mcit.setInterKeyTimeout(2000);
		mcit.setNumberParser(IvrConstant.LANG_ID_PASHTO, pashtoNumberParser);
		for(int i=0;i<soundFiles.length;i++)
		{
			mcit.setPromptSoundFiles(i, soundFiles[i]);
		}
		mcit.setDefaultNextTask(taskPrefix + "24");
		mcit.setValidLengthNextTask(taskPrefix + "23");
		
		mcit.setContext(context);
		
		taskContext.put(taskPrefix + "3", mcit);

		PropertyMapperTask pmt = new PropertyMapperTask();
		
		pmt.setPropertyMap(IvrConstant.USER_INPUT, IvrConstant.PIN, "", "");
		pmt.addData(IvrConstant.RESPONSE_CODE, StatusCodeConstant.CODE_0000);
		pmt.setNextTaskId(taskPrefix + "5");
		
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

	void addCallOriginator(String taskPrefix, String hangUpFlow)
	{
		CallOriginatorTask cot = new CallOriginatorTask(/*getAsteriskServer()*/ /*getManagerConnection()*/);
		
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
		cot.setNextTaskMap(IvrConstant.ORIGINATE_EXCEPTION, taskPrefix);
		cot.setNextTaskMap(IvrConstant.SUCCESS, "");//TODO 
		cot.setNextTaskMap(IvrConstant.RETRY_OVER, hangUpFlow);//TODO send notification to server on failure
		if(cot.getTotalAttempts() > 1)
		{
			String time = ProjectPropertiesUtil.getProperty(taskPrefix+".call.timeouts", ProjectPropertiesUtil.getProperty("call.timeouts"));
			if(!StringUtil.isNullOrEmpty(time)) {
				String parsedTime[] = time.split("\\,");
				for(String t : parsedTime)
				{
					cot.setRetryAfter(Long.parseLong(t)*1000);
				}
			}
//			cot.setRetryAfter(5000);
//			cot.setRetryAfter(10000);
		}
		
		cot.setContext(context);
		cot.setAsteriskServerWrappers(getAsteriskServerWrappers());

		taskContext.put(taskPrefix, cot);
		
	}
	
	@SuppressWarnings("unchecked")
	private List<AsteriskServerWrapper> getAsteriskServerWrappers()
	{
		return (List<AsteriskServerWrapper>) ApplicationContextImpl.getBean("asteriskServers");
	}

	void addPinVerifier(String taskPrefix)
	{
		PinVerifierTask pvt = new PinVerifierTask();
		
		pvt.setNextTask(TaskConstant.DEFAULT_TASK, taskPrefix + "7");

		pvt.setNextTask(StatusCodeConstant.CODE_7022, taskPrefix + "6");
		pvt.setNextTask(StatusCodeConstant.CODE_8058, taskPrefix + "7");
		pvt.setNextTask(StatusCodeConstant.CODE_8059, taskPrefix + "9");
		pvt.setNextTask(StatusCodeConstant.CODE_8062, taskPrefix + "10");
		pvt.setNextTask(StatusCodeConstant.CODE_8064, taskPrefix + "11");
		pvt.setNextTask(StatusCodeConstant.CODE_8065, taskPrefix + "12");
		pvt.setNextTask(StatusCodeConstant.CODE_0000, taskPrefix + "13");
		pvt.setNextTask(StatusCodeConstant.CODE_9999, taskPrefix + "21");
		pvt.setNextTask(StatusCodeConstant.CODE_8061, taskPrefix + "28");
		pvt.setNextTask(StatusCodeConstant.CODE_8063, taskPrefix + "29");
		pvt.setNextTask(StatusCodeConstant.CODE_8066, taskPrefix + "30");
		pvt.setNextTask(StatusCodeConstant.CODE_8055, taskPrefix + "26");
		pvt.setNextTask(StatusCodeConstant.CODE_8067, taskPrefix + "27");
		pvt.setNextTask(StatusCodeConstant.CODE_8077, taskPrefix + "insufficient_bal");

		//TODO needs improvement for auto resolution
		pvt.setWsClient(ApplicationContextImpl.getBean("wsClient", WsClient.class));
		
		pvt.setContext(context);
		
		taskContext.put(taskPrefix + "5", pvt);

		MultiCharInputTask mcit = new MultiCharInputTask();

		mcit = new MultiCharInputTask();
		
		mcit.setPromptSoundFiles(0, "common/acc_block_contect_cs");
		mcit.setAcceptablePattern("");
		mcit.setDefaultNextTask(taskPrefix + "8");
		mcit.setInterKeyTimeout(100);
		mcit.setLastKeyTimeout(100);
		mcit.setTotalAttempts(1);
		
		mcit.setContext(context);
		
		taskContext.put(taskPrefix + "21", mcit);

//		NumberParserImpl pashtoNumParser = new NumberParserImpl();
		
//		pashtoNumParser.getAfterUnits()[2] = "above";

		mcit = new MultiCharInputTask();
		
		mcit.setPromptSoundFiles(0, "common/invalid_pin_try_again");
//		mcit.setAcceptablePattern("");
		mcit.setDefaultNextTask(taskPrefix + "3");
		mcit.setNoResponseNextTask(taskPrefix + "3");
		mcit.setValidLengthNextTask(taskPrefix + "25");
		mcit.setInterKeyTimeout(2000);
//		mcit.setLastKeyTimeout(100);
		mcit.setTotalAttempts(1);
		mcit.setNumberParser(IvrConstant.LANG_ID_PASHTO, pashtoNumberParser);
		
		mcit.setContext(context);
		
		taskContext.put(taskPrefix + "6", mcit);

		PropertyMapperTask pmt = new PropertyMapperTask();
		
		pmt.setPropertyMap(IvrConstant.USER_INPUT, IvrConstant.PIN, "", "");
		pmt.addData(IvrConstant.RESPONSE_CODE, StatusCodeConstant.CODE_0000);
		pmt.setNextTaskId(taskPrefix + "5");
		
		pmt.setContext(context);

		taskContext.put(taskPrefix + "25", pmt);

		mcit = new MultiCharInputTask();
		
		mcit.setPromptSoundFiles(0, "common/service_unavailable");
		mcit.setAcceptablePattern("");
		mcit.setDefaultNextTask(taskPrefix + "8");
		mcit.setInterKeyTimeout(100);
		mcit.setLastKeyTimeout(100);
		mcit.setTotalAttempts(1);
		
		mcit.setContext(context);
		
		taskContext.put(taskPrefix + "7", mcit);

		mcit = new MultiCharInputTask();
		
		mcit.setPromptSoundFiles(0, "common/insufficient_bal");
		mcit.setAcceptablePattern("");
		mcit.setDefaultNextTask(taskPrefix + "8");
		mcit.setInterKeyTimeout(100);
		mcit.setLastKeyTimeout(100);
		mcit.setTotalAttempts(1);
		
		mcit.setContext(context);
		
		taskContext.put(taskPrefix + "9", mcit);

		mcit = new MultiCharInputTask();
		
		mcit.setPromptSoundFiles(0, "common/daily_tx_limit_over");
		mcit.setAcceptablePattern("");
		mcit.setDefaultNextTask(taskPrefix + "8");
		mcit.setInterKeyTimeout(100);
		mcit.setLastKeyTimeout(100);
		mcit.setTotalAttempts(1);
		
		mcit.setContext(context);
		
		taskContext.put(taskPrefix + "10", mcit);

		mcit = new MultiCharInputTask();
		
		mcit.setPromptSoundFiles(0, "common/monthly_tx_limit_over");
		mcit.setAcceptablePattern("");
		mcit.setDefaultNextTask(taskPrefix + "8");
		mcit.setInterKeyTimeout(100);
		mcit.setLastKeyTimeout(100);
		mcit.setTotalAttempts(1);
		
		mcit.setContext(context);
		
		taskContext.put(taskPrefix + "11", mcit);

		mcit = new MultiCharInputTask();
		
		mcit.setPromptSoundFiles(0, "common/yearly_tx_limit_over");
		mcit.setAcceptablePattern("");
		mcit.setDefaultNextTask(taskPrefix + "8");
		mcit.setInterKeyTimeout(100);
		mcit.setLastKeyTimeout(100);
		mcit.setTotalAttempts(1);
		
		mcit.setContext(context);
		
		taskContext.put(taskPrefix + "12", mcit);

		mcit = new MultiCharInputTask();
		
		mcit.setPromptSoundFiles(0, "common/tx_success");
		mcit.setAcceptablePattern("");
		mcit.setDefaultNextTask(taskPrefix + "8");
		mcit.setInterKeyTimeout(100);
		mcit.setLastKeyTimeout(100);
		mcit.setTotalAttempts(1);
		
		mcit.setContext(context);
		
		taskContext.put(taskPrefix + "13", mcit);

		mcit = new MultiCharInputTask();
		
		mcit.setPromptSoundFiles(0, "common/inactive_account");
		mcit.setAcceptablePattern("");
		mcit.setDefaultNextTask(taskPrefix + "8");
		mcit.setInterKeyTimeout(100);
		mcit.setLastKeyTimeout(100);
		mcit.setTotalAttempts(1);
		
		mcit.setContext(context);
		
		taskContext.put(taskPrefix + "26", mcit);

		mcit = new MultiCharInputTask();
		
		mcit.setPromptSoundFiles(0, "common/max_cr_limit_over");
		mcit.setAcceptablePattern("");
		mcit.setDefaultNextTask(taskPrefix + "8");
		mcit.setInterKeyTimeout(100);
		mcit.setLastKeyTimeout(100);
		mcit.setTotalAttempts(1);
		
		mcit.setContext(context);
		
		taskContext.put(taskPrefix + "27", mcit);

		mcit = new MultiCharInputTask();

		mcit.setPromptSoundFiles(0, "common/daily_cr_limit_over");
		mcit.setAcceptablePattern("");
		mcit.setDefaultNextTask(taskPrefix + "8");
		mcit.setInterKeyTimeout(100);
		mcit.setLastKeyTimeout(100);
		mcit.setTotalAttempts(1);
		
		mcit.setContext(context);
		
		taskContext.put(taskPrefix + "28", mcit);

		mcit = new MultiCharInputTask();
		
		mcit.setPromptSoundFiles(0, "common/monthly_cr_limit_over");
		mcit.setAcceptablePattern("");
		mcit.setDefaultNextTask(taskPrefix + "8");
		mcit.setInterKeyTimeout(100);
		mcit.setLastKeyTimeout(100);
		mcit.setTotalAttempts(1);
		
		mcit.setContext(context);
		
		taskContext.put(taskPrefix + "29", mcit);

		mcit = new MultiCharInputTask();
		
		mcit.setPromptSoundFiles(0, "common/yearly_cr_limit_over");
		mcit.setAcceptablePattern("");
		mcit.setDefaultNextTask(taskPrefix + "8");
		mcit.setInterKeyTimeout(100);
		mcit.setLastKeyTimeout(100);
		mcit.setTotalAttempts(1);
		mcit.setContext(context);
		
		taskContext.put(taskPrefix + "30", mcit);

		mcit = new MultiCharInputTask();
		
		mcit.setPromptSoundFiles(0, "common/insufficient_bal");
		mcit.setAcceptablePattern("");
		mcit.setDefaultNextTask(taskPrefix + "8");
		mcit.setInterKeyTimeout(100);
		mcit.setLastKeyTimeout(100);
		mcit.setTotalAttempts(1);
		mcit.setContext(context);
		
		taskContext.put(taskPrefix + "insufficient_bal", mcit);
	}
	
	void addGoodbye(String taskPrefix)
	{
		MultiCharInputTask mcit = new MultiCharInputTask();
		
		mcit.setPromptSoundFiles(0, "common/good_bye");
		mcit.setAcceptablePattern("");
//		mcit.setDefaultNextTask("8");
		mcit.setInterKeyTimeout(100);
		mcit.setLastKeyTimeout(100);
		mcit.setTotalAttempts(1);
		mcit.setContext(context);
		
		taskContext.put(taskPrefix + "8", mcit);

		mcit = new MultiCharInputTask();
		
		mcit.setPromptSoundFiles(0, "common/thank_you_calling_bb");
		mcit.setAcceptablePattern("");
		mcit.setDefaultNextTask(taskPrefix + "8");
		mcit.setInterKeyTimeout(100);
		mcit.setLastKeyTimeout(100);
		mcit.setTotalAttempts(1);
		mcit.setContext(context);
		
		taskContext.put(taskPrefix + "31", mcit);
		
		PropertyMapperTask pmt = new PropertyMapperTask();
		
		pmt.addData(IvrConstant.RESPONSE_CODE, StatusCodeConstant.CODE_9001);
		pmt.setNextTaskId(taskPrefix+"32");
		pmt.setContext(context);
		
		taskContext.put(taskPrefix + "24", pmt);
		
		PinVerifierTask pvt = new PinVerifierTask();
		
		pvt.setNextTask(TaskConstant.DEFAULT_TASK, taskPrefix + "31");
		pvt.setWsClient(ApplicationContextImpl.getBean("wsClient", WsClient.class));//TODO auto resolution required
		pvt.setContext(context);
		
		taskContext.put(taskPrefix + "32", pvt);

	}

	void addNewPinFlow(String taskPrefix, String successSoundfile, String customerPostfix)
	{
		TaskGroup tg = (TaskGroup) taskContext.get(taskPrefix + "2");
		
		tg.addTask(taskPrefix + "15");
		tg.addTask(taskPrefix + "16");
		tg.addTask(taskPrefix + "17");
		tg.addTask(taskPrefix + "18");
		tg.addTask(taskPrefix + "19");
		tg.addTask(taskPrefix + "20");
		tg.addTask(taskPrefix + "22");
		tg.setContext(context);

		MultiCharInputTask mcit = new MultiCharInputTask();
		
		mcit.setPromptSoundFiles(0, "common/new_pin_prompt" + customerPostfix);
		mcit.setDefaultNextTask(taskPrefix + "24");
		mcit.setValidLengthNextTask(taskPrefix + "15");
		mcit.setContext(context);
		
		taskContext.put(taskPrefix + "13", mcit);
		
		PropertyMapperTask pmt = new PropertyMapperTask();
		
		pmt.setPropertyMap(IvrConstant.USER_INPUT, IvrConstant.NEW_PIN, "", "");
		pmt.addData(IvrConstant.RESPONSE_CODE, StatusCodeConstant.CODE_0000);
		pmt.setNextTaskId(taskPrefix + "16");
		pmt.setContext(context);
		
		taskContext.put(taskPrefix + "15", pmt);

		mcit = new MultiCharInputTask();
		
		mcit.setPromptSoundFiles(0, "common/verify_new_pin_prompt" + customerPostfix);
		mcit.setDefaultNextTask(taskPrefix + "24");
		mcit.setValidLengthNextTask(taskPrefix + "17");
		mcit.setContext(context);
		
		taskContext.put(taskPrefix + "16", mcit);		

		ComparatorTask ct = new ComparatorTask();
		
		ct.setCompareKeys(IvrConstant.USER_INPUT, IvrConstant.NEW_PIN, taskPrefix + "19", taskPrefix + "18");
		ct.setContext(context);
		
		taskContext.put(taskPrefix + "17", ct);
		
		mcit = new MultiCharInputTask();
		
		mcit.setPromptSoundFiles(0, "common/invalid_pin_try_again");
		mcit.setAcceptablePattern("");
		mcit.setDefaultNextTask(taskPrefix + "13");
		mcit.setInterKeyTimeout(100);
		mcit.setLastKeyTimeout(100);
		mcit.setTotalAttempts(1);
		mcit.setContext(context);
		
		taskContext.put(taskPrefix + "18", mcit);
		

		PinVerifierTask pvt = new PinVerifierTask();
		
		pvt.setNextTask(TaskConstant.DEFAULT_TASK, taskPrefix+"7");
		
		pvt.setNextTask(StatusCodeConstant.CODE_0000, taskPrefix + "20");
		pvt.setNextTask(StatusCodeConstant.CODE_7022, taskPrefix + "18");
		pvt.setNextTask(StatusCodeConstant.CODE_8058, taskPrefix + "7");

		//TODO needs improvement for auto resolution
		pvt.setWsClient(ApplicationContextImpl.getBean("wsClient", WsClient.class));
		pvt.setContext(context);

		taskContext.put(taskPrefix + "19", pvt);
		
		mcit = new MultiCharInputTask();
		
		mcit.setPromptSoundFiles(0, successSoundfile);
		mcit.setAcceptablePattern("");
		mcit.setDefaultNextTask(taskPrefix + "8");
		mcit.setInterKeyTimeout(100);
		mcit.setLastKeyTimeout(100);
		mcit.setTotalAttempts(1);
		mcit.setContext(context);
		
		taskContext.put(taskPrefix + "20", mcit);
	}

	void baseFlow(String taskPrefix, String ...soundFiles)
	{
		addEnUrLanguageSelection(taskPrefix, "3");
		
		addEnUrPaLanguageSelection(IvrConstant.BANK_BOP + taskPrefix, "3");

		addGroup(taskPrefix);
		
		addGroup(IvrConstant.BANK_BOP + taskPrefix);

		addPinPrompt(taskPrefix, soundFiles);
		
		addPinPrompt(IvrConstant.BANK_BOP + taskPrefix, soundFiles);

		addCallOriginator(taskPrefix, IvrConstant.HANGUP_FLOW);
		
		addCallOriginator(IvrConstant.BANK_BOP + taskPrefix, IvrConstant.HANGUP_FLOW);

		addPinVerifier(taskPrefix);
		
		addPinVerifier(IvrConstant.BANK_BOP + taskPrefix);

		addGoodbye(taskPrefix);

		addGoodbye(IvrConstant.BANK_BOP + taskPrefix);
	}
	
	void flow50010()
	{
		baseFlow(ProductConstant.PROD_ID_50010, "common/products/common/1","{#"+IvrConstant.AGENT_ID+"}",
				"common/products/common/2","{$"+IvrConstant.TX_AMOUNT+"}","common/products/50010/3",
				"{#" + IvrConstant.RECIPIENT_CNIC + "}", "common/products/common/4");		
	}
	
	void flow50000()
	{
		baseFlow(ProductConstant.PROD_ID_50000, "common/products/common/1", "{#"+IvrConstant.AGENT_ID+"}",
				"common/products/common/2", "{$"+IvrConstant.TX_AMOUNT+"}", "common/products/50000/3",
				"{#" + IvrConstant.RECIPIENT_MOBILE + "}", "common/products/common/4");
	}

	void flow50006()
	{
		baseFlow(ProductConstant.PROD_ID_50006, "common/products/common/1", "{#"+IvrConstant.AGENT_ID+"}", 
				"common/products/common/2", "{$"+IvrConstant.TX_AMOUNT+"}", "common/products/common/3");
	}

	void flow50026()
	{
		baseFlow(ProductConstant.PROD_ID_50026, "common/products/common/1", "{#"+IvrConstant.AGENT_ID+"}", 
				"common/products/common/2", "{$"+IvrConstant.TX_AMOUNT+"}", 
				"common/products/50026/3", "{#" + IvrConstant.BANK_ACC_NUM + "}"
				, "common/products/common/4");
	}

	void flow50031()
	{
		baseFlow(ProductConstant.PROD_ID_50031, "common/products/common/1", "{#"+IvrConstant.AGENT_ID+"}", 
				"common/products/common/2", "{$"+IvrConstant.TX_AMOUNT+"}", "common/products/common/3");
	}
	
	void flow6()
	{
		baseFlow(ProductConstant.PROD_ID_6, "common/bal_pin_prompt");
	}

	void flow122()
	{
		baseFlow(ProductConstant.PROD_ID_122, "common/mini_statement_pin_prompt");
	}
	
	void flow123()
	{
		baseFlow(ProductConstant.PROD_ID_123, "common/change_pin_prompt");

		addNewPinFlow(ProductConstant.PROD_ID_123, "common/pin_change_success" , "");

		addNewPinFlow(IvrConstant.BANK_BOP + ProductConstant.PROD_ID_123, "common/pin_change_success" , "");
	}
	
	void flow125()
	{
		addGroup(ProductConstant.PROD_ID_125);
		
		addGroup(IvrConstant.BANK_BOP + ProductConstant.PROD_ID_125);
		
		addPinVerifier(ProductConstant.PROD_ID_125);

		addPinVerifier(IvrConstant.BANK_BOP + ProductConstant.PROD_ID_125);

		addCallOriginator(ProductConstant.PROD_ID_125, IvrConstant.HANGUP_FLOW);
		
		addCallOriginator(IvrConstant.BANK_BOP + ProductConstant.PROD_ID_125, IvrConstant.HANGUP_FLOW);

		addEnUrLanguageSelection(ProductConstant.PROD_ID_125, "13");
		
		addEnUrPaLanguageSelection(IvrConstant.BANK_BOP + ProductConstant.PROD_ID_125, "13");

		addNewPinFlow(ProductConstant.PROD_ID_125, "common/new_pin_success" , "");

		addNewPinFlow(IvrConstant.BANK_BOP + ProductConstant.PROD_ID_125, "common/new_pin_success" , "");

		addGoodbye(ProductConstant.PROD_ID_125);

		addGoodbye(IvrConstant.BANK_BOP + ProductConstant.PROD_ID_125);
	}

    void flow134()
    {
        addGroup(ProductConstant.PROD_ID_134);

        addGroup(IvrConstant.BANK_BOP + ProductConstant.PROD_ID_134);

        addPinVerifier(ProductConstant.PROD_ID_134);

        addPinVerifier(IvrConstant.BANK_BOP + ProductConstant.PROD_ID_134);

        addCallOriginator(ProductConstant.PROD_ID_134, IvrConstant.HANGUP_FLOW);

        addCallOriginator(IvrConstant.BANK_BOP + ProductConstant.PROD_ID_134, IvrConstant.HANGUP_FLOW);

        addEnUrLanguageSelection(ProductConstant.PROD_ID_134, "13");

        addEnUrPaLanguageSelection(IvrConstant.BANK_BOP + ProductConstant.PROD_ID_134, "13");

        addNewPinFlow(ProductConstant.PROD_ID_134, "common/new_pin_success" , "_customer");

        addNewPinFlow(IvrConstant.BANK_BOP + ProductConstant.PROD_ID_134, "common/new_pin_success" , "_customer");

        addGoodbye(ProductConstant.PROD_ID_134);

        addGoodbye(IvrConstant.BANK_BOP + ProductConstant.PROD_ID_134);
    }


    void flow126()
	{
		taskContext.put(ProductConstant.PROD_ID_126, taskContext.get(ProductConstant.PROD_ID_125));
	}

	void flow2510700()
	{
		baseFlow(ProductConstant.PROD_ID_2510700, "common/products/common/1", "{#"+IvrConstant.AGENT_ID+"}", 
				"common/products/common/2", "{$"+IvrConstant.TX_AMOUNT+"}", "common/products/2510700/3", 
				"{#" + IvrConstant.CONSUMER_NUMBER + "}", "common/products/common/4");
		
		Task cot = taskContext.get(ProductConstant.PROD_ID_2510700);
		
		taskContext.put(ProductConstant.PROD_ID_2510600, cot);
		taskContext.put(ProductConstant.PROD_ID_2510704, cot);
		taskContext.put(ProductConstant.PROD_ID_2510705, cot);
		taskContext.put(ProductConstant.PROD_ID_2510708, cot);
		taskContext.put(ProductConstant.PROD_ID_2510710, cot);
		taskContext.put(ProductConstant.PROD_ID_2510711, cot);
		taskContext.put(ProductConstant.PROD_ID_2510720, cot);
		taskContext.put(ProductConstant.PROD_ID_2510740, cot);
		taskContext.put(ProductConstant.PROD_ID_2510741, cot);

		taskContext.put(ProductConstant.PROD_ID_2510747, cot);
		taskContext.put(ProductConstant.PROD_ID_2510748, cot);
		taskContext.put(ProductConstant.PROD_ID_2510751, cot);
		taskContext.put(ProductConstant.PROD_ID_2510756, cot);
		taskContext.put(ProductConstant.PROD_ID_2510758, cot);
		taskContext.put(ProductConstant.PROD_ID_2510760, cot);
		taskContext.put(ProductConstant.PROD_ID_2510762, cot);
		taskContext.put(ProductConstant.PROD_ID_2510764, cot);
		taskContext.put(ProductConstant.PROD_ID_2510766, cot);
		taskContext.put(ProductConstant.PROD_ID_2510767, cot);
		taskContext.put(ProductConstant.PROD_ID_2510768, cot);
		taskContext.put(ProductConstant.PROD_ID_2510770, cot);
		taskContext.put(ProductConstant.PROD_ID_2510772, cot);
		taskContext.put(ProductConstant.PROD_ID_2510773, cot);
		taskContext.put(ProductConstant.PROD_ID_2510774, cot);
		taskContext.put(ProductConstant.PROD_ID_2510776, cot);
		taskContext.put(ProductConstant.PROD_ID_2510778, cot);
		taskContext.put(ProductConstant.PROD_ID_2510780, cot);
		taskContext.put(ProductConstant.PROD_ID_2510782, cot);
		taskContext.put(ProductConstant.PROD_ID_2510783, cot);
		taskContext.put(ProductConstant.PROD_ID_2510784, cot);
		taskContext.put(ProductConstant.PROD_ID_2510789, cot);
	}

	void flow2510715()
	{
		baseFlow(ProductConstant.PROD_ID_2510715, "common/products/common/1", "{#"+IvrConstant.AGENT_ID+"}", 
				"common/products/common/2", "{$"+IvrConstant.TX_AMOUNT+"}", "common/products/50000/3", 
				"{#" + IvrConstant.CONSUMER_NUMBER + "}", "common/products/common/4");

		Task cot = taskContext.get(ProductConstant.PROD_ID_2510715);
		
		taskContext.put(ProductConstant.PROD_ID_2510719, cot);
		taskContext.put(ProductConstant.PROD_ID_2510738, cot);
		taskContext.put(ProductConstant.PROD_ID_2510742, cot);
		taskContext.put(ProductConstant.PROD_ID_2510743, cot);
		taskContext.put(ProductConstant.PROD_ID_2510744, cot);
		taskContext.put(ProductConstant.PROD_ID_2510745, cot);
		taskContext.put(ProductConstant.PROD_ID_2510749, cot);
		taskContext.put(ProductConstant.PROD_ID_2510753, cot);
		taskContext.put(ProductConstant.PROD_ID_2510765, cot);
	}

	private void flow2510763(String taskPrefix, String soundfile)
	{
//		addLanguage(taskPrefix);
		addCallOriginator(taskPrefix, "");
		addResponseCode(taskPrefix);
		addPlayMessage(taskPrefix, soundfile);
	}

	private void addPlayMessage(String taskPrefix, String... soundfile)
	{
		MultiCharInputTask mcit = new MultiCharInputTask();
		
		for(int i=0;i<soundfile.length;i++) {
			mcit.setPromptSoundFiles(i, soundfile[i]);
		}
		mcit.setAcceptablePattern("");
//		mcit.setDefaultNextTask(taskPrefix + "13");
		mcit.setInterKeyTimeout(100);
		mcit.setLastKeyTimeout(100);
		mcit.setTotalAttempts(1);
		mcit.setContext(context);
		
		taskContext.put(taskPrefix + "33", mcit);
		
	}

	private void addLanguage(String taskPrefix)
	{
		PropertyMapperTask pmt = new PropertyMapperTask();
		
		pmt.addData(IvrConstant.LANG_ID, IvrConstant.LANG_ID_URDU);
		pmt.setNextTaskId(taskPrefix + "34");
		
		taskContext.put(taskPrefix + "33", pmt);
	}
	
	private void addResponseCode(String taskPrefix) {
		PropertyMapperTask pmt = new PropertyMapperTask();
		
		pmt.addData(IvrConstant.RESPONSE_CODE, StatusCodeConstant.CODE_0000);
		pmt.setNextTaskId(taskPrefix + "33");
		
		taskContext.put(taskPrefix + "2", pmt);
	}

	private void flowNrspDb(String taskPrefix)
	{
		addCallOriginator(taskPrefix, "");
		addResponseCode(taskPrefix);
		addPlayMessage(taskPrefix, IvrConstant.BANK_NRSP + "/products/common/1", "{#"+IvrConstant.AGENT_ID+"}", 
				IvrConstant.BANK_NRSP + "/products/common/2", "{$"+IvrConstant.TX_AMOUNT+"}", IvrConstant.BANK_NRSP + "/products/common/3");
		
		Task task = taskContext.get(taskPrefix);
		
		taskContext.put(IvrConstant.BANK_NRSP+ProductConstant.PROD_ID_2510700, task);
		taskContext.put(IvrConstant.BANK_NRSP+ProductConstant.PROD_ID_2510705, task);
		taskContext.put(IvrConstant.BANK_NRSP+ProductConstant.PROD_ID_2510710, task);
	}

	private void flowNrspCr(String taskPrefix, String... soundFiles)
	{
		addCallOriginator(taskPrefix, "");
		addResponseCode(taskPrefix);
		addPlayMessage(taskPrefix, soundFiles);
	}
}
