package com.inov8.microbank.webapp.action.allpayweb;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.MiniXMLUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.XMLConstants;
import com.inov8.microbank.mfsweb.MfsWebManager;
import com.inov8.microbank.mfsweb.MfsWebResponseDataPopulator;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author Kashif Bashir
 * @author Yasir Shabbir
 * @since Sep, 2016
 */

public class Cash2CashController extends AgentWebFormController
{


	private AgentWebManager agentWebManager;

	private final static Log logger = LogFactory.getLog(PayCashWithDrawalController.class);
	private final String PAGE_FROM = "allpay-web/case2CashStepOne";



	public Cash2CashController()
	{
		setCommandName("object");
		setCommandClass(Object.class);
	}




	@Override
	protected String runCommand(AllPayRequestWrapper requestWrapper, HttpServletResponse response, Object commandObject, BindException exception) throws Exception
	{

		String manualOTPin = requestWrapper.getParameter(CommandFieldConstants.KEY_MAN_OT_PIN);

		String responseXml = AllPayWebConstant.BLANK_SPACE.getValue();


		if(!StringUtil.isNullOrEmpty(manualOTPin) & !CommonUtils.validateOneTimePin(manualOTPin)) {

			String error = "Transaction Code in this sequence is not allowed.";

			requestWrapper.setAttribute("errors", error);

			return "<msg id='-1'><errors><error code='9001' level='2'>" + error + "</error></errors></msg>";
		}


		responseXml = mfsWebController.handleRequest(requestWrapper, CommandFieldConstants.CMD_CNIC_TO_CNIC_INFO); //CMD_CASH_TO_CASH_INFO

		if(responseXml != null && !(AllPayWebConstant.BLANK_SPACE.getValue().equals(responseXml))) {

			requestWrapper.setAttribute(AllPayWebConstant.HEADING.getValue(), "Cash Transfer Notification");
			requestWrapper.addParameter(XMLConstants.ATTR_CAMT, MiniXMLUtil.getTagTextValue(responseXml,"/msg/params/param[@name='CAMT']"));

		}
		requestWrapper.addParameter(CommandFieldConstants.KEY_IS_BVS_REQUIRED,"1");
		mfsWebResponseDataPopulator.populateCitiesData(requestWrapper,responseXml);
		populatePreviousScreen(requestWrapper);

		return responseXml;
	}


	private void updateRequest(AllPayRequestWrapper requestWrapper)
	{

		AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);

		requestWrapper.addParameter(CommandFieldConstants.KEY_PROD_ID, String.valueOf(50011L));
		requestWrapper.addParameter(CommandFieldConstants.KEY_DEVICE_TYPE_ID, String.valueOf(8));
		requestWrapper.addParameter(CommandFieldConstants.KEY_U_ID, requestWrapper.getParameter(CommandFieldConstants.KEY_U_ID));
		requestWrapper.addParameter(CommandFieldConstants.KEY_S_W_CNIC, requestWrapper.getParameter(CommandFieldConstants.KEY_S_W_CNIC));
		requestWrapper.addParameter(CommandFieldConstants.KEY_WALKIN_SENDER_MOBILE, requestWrapper.getParameter(CommandFieldConstants.KEY_WALKIN_SENDER_MOBILE));
		requestWrapper.addParameter(CommandFieldConstants.KEY_R_W_CNIC, requestWrapper.getParameter(CommandFieldConstants.KEY_R_W_CNIC));
		requestWrapper.addParameter(CommandFieldConstants.KEY_RECIPIENT_WALKIN_MOBILE, requestWrapper.getParameter(CommandFieldConstants.KEY_RECIPIENT_WALKIN_MOBILE));
		requestWrapper.addParameter(CommandFieldConstants.KEY_TX_AMOUNT, requestWrapper.getParameter(CommandFieldConstants.KEY_TX_AMOUNT));

//		requestWrapper.toString();
	}

	private void prepareResponse(HttpServletRequest request, String xmlResponse)
	{

		try {

			;


		} catch(Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private Boolean logActionLogModel()
	{

		ActionLogModel actionLogModel = new ActionLogModel();
		actionLogModel.setAppUserIdAppUserModel(ThreadLocalAppUser.getAppUserModel());
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END);
		actionLogModel.setEndTime(new Timestamp(new Date().getTime()));

		ActionLogManager actionLogManager = (ActionLogManager) this.allPayWebResponseDataPopulator.getBean("actionLogManager");

		BaseWrapper baseWrapperActionLog = new BaseWrapperImpl();

		baseWrapperActionLog.setBasePersistableModel(actionLogModel);

		try {

			baseWrapperActionLog = actionLogManager.createOrUpdateActionLog(baseWrapperActionLog);

		} catch(FrameworkCheckedException e) {
			logger.error(e.getLocalizedMessage());
		}

		if(baseWrapperActionLog != null && baseWrapperActionLog.getBasePersistableModel() != null) {

			actionLogModel = (ActionLogModel) baseWrapperActionLog.getBasePersistableModel();

			ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

			return Boolean.TRUE;
		}

		return Boolean.FALSE;
	}


	public void setMfsWebController(MfsWebManager mfsWebController)
	{
		this.mfsWebController = mfsWebController;
	}

	public void setMfsWebResponseDataPopulator(MfsWebResponseDataPopulator mfsWebResponseDataPopulator)
	{
		this.mfsWebResponseDataPopulator = mfsWebResponseDataPopulator;
	}

	public void setAllPayWebResponseDataPopulator(AllPayWebResponseDataPopulator allPayWebResponseDataPopulator)
	{
		this.allPayWebResponseDataPopulator = allPayWebResponseDataPopulator;
	}

	public void setAgentWebManager(AgentWebManager agentWebManager)
	{
		this.agentWebManager = agentWebManager;
	}
}