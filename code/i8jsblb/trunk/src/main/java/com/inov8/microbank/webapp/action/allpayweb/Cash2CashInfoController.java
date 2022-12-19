package com.inov8.microbank.webapp.action.allpayweb;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.integration.middleware.controller.NadraIntegrationController;
import com.inov8.integration.vo.NadraIntegrationVO;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.util.*;
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
 *
 * @author Kashif Bashir
 * @since July, 2012
 *
 *
 * @author Yasir Shabbir
 * @since Oct, 2016
 *
 */

public class Cash2CashInfoController extends AgentWebFormController
{

	private AgentWebManager agentWebManager;

	private final static Log logger = LogFactory.getLog(PayCashWithDrawalController.class);
	private final String PAGE_FROM = "allpay-web/case2CashStepOne";

	public Cash2CashInfoController() {
		setCommandName("object");
		setCommandClass(Object.class);
	}


	@Override
	protected String runCommand(AllPayRequestWrapper requestWrapper, HttpServletResponse response, Object commandObject, BindException exception) throws Exception {


		String responseXml = AllPayWebConstant.BLANK_SPACE.getValue();

		final String manualOTPPIN = requestWrapper.getParameter(CommandFieldConstants.KEY_MAN_OT_PIN);
		if(!StringUtil.isNullOrEmpty(manualOTPPIN)){
			requestWrapper.addParameter(CommandFieldConstants.KEY_MAN_OT_PIN,  EncryptionUtil.encryptWithAES(XMLConstants.AES_ENCRYPTION_KEY, manualOTPPIN));

		}

		String isBVSAuhtorized = (String) requestWrapper.getSession().getAttribute(PortalConstants.IS_BVS_ENABLE);

		requestWrapper.addParameter(PortalConstants.IS_BVS_ENABLE, "1");
		requestWrapper.addParameter(CommandFieldConstants.KEY_RECEIVER_CITY,(String) requestWrapper.getParameter(CommandFieldConstants.KEY_RECEIVER_CITY + "_1"));

		responseXml = mfsWebController.handleRequest(requestWrapper, CommandFieldConstants.CMD_CASH_TO_CASH_INFO);

		if(responseXml != null && !(AllPayWebConstant.BLANK_SPACE.getValue().equals(responseXml))) {

			prepareResponse(requestWrapper, responseXml);
			requestWrapper.setAttribute(AllPayWebConstant.HEADING.getValue(), "Cash Transfer Notification");
		}

		if(PAGE_FROM.equals(getSuccessView())) {
			updateRequest(requestWrapper);
		}

		return responseXml;
	}



	private void updateRequest(AllPayRequestWrapper requestWrapper) {

		AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);

		requestWrapper.addParameter(CommandFieldConstants.KEY_PROD_ID, String.valueOf(50011L));
		requestWrapper.addParameter(CommandFieldConstants.KEY_DEVICE_TYPE_ID, String.valueOf(3));
		requestWrapper.addParameter(CommandFieldConstants.KEY_U_ID, requestWrapper.getParameter(CommandFieldConstants.KEY_U_ID));
		requestWrapper.addParameter(CommandFieldConstants.KEY_S_W_CNIC, requestWrapper.getParameter(CommandFieldConstants.KEY_S_W_CNIC));
		requestWrapper.addParameter(CommandFieldConstants.KEY_WALKIN_SENDER_MOBILE, requestWrapper.getParameter(CommandFieldConstants.KEY_WALKIN_SENDER_MOBILE));
		requestWrapper.addParameter(CommandFieldConstants.KEY_R_W_CNIC, requestWrapper.getParameter(CommandFieldConstants.KEY_R_W_CNIC));
		requestWrapper.addParameter(CommandFieldConstants.KEY_RECIPIENT_WALKIN_MOBILE, requestWrapper.getParameter(CommandFieldConstants.KEY_RECIPIENT_WALKIN_MOBILE));
		requestWrapper.addParameter(CommandFieldConstants.KEY_TX_AMOUNT, requestWrapper.getParameter(CommandFieldConstants.KEY_TX_AMOUNT));

//		requestWrapper.toString();
	}

	private void prepareResponse(HttpServletRequest request, String xmlResponse) {

		try {

			String xpath = "/msg/trans/trn/@";

			String trxCode = MiniXMLUtil.getTagTextValue(xmlResponse, xpath + XMLConstants.ATTR_TRXID);
			String senderCnic = MiniXMLUtil.getTagTextValue(xmlResponse, xpath + XMLConstants.ATTR_SWCNIC);
			String receiverCnic = MiniXMLUtil.getTagTextValue(xmlResponse, xpath + XMLConstants.ATTR_RWCNIC);
			String senderMobile = MiniXMLUtil.getTagTextValue(xmlResponse, xpath + XMLConstants.ATTR_SWMOB);
			String receiverMobile = MiniXMLUtil.getTagTextValue(xmlResponse, xpath + XMLConstants.ATTR_RWMOB);
			String date = MiniXMLUtil.getTagTextValue(xmlResponse, xpath + XMLConstants.ATTR_DATEF);
			String time = MiniXMLUtil.getTagTextValue(xmlResponse, xpath + XMLConstants.ATTR_TIMEF);
			String commission = MiniXMLUtil.getTagTextValue(xmlResponse, xpath + XMLConstants.ATTR_CAMTF);
			String charges = MiniXMLUtil.getTagTextValue(xmlResponse, xpath + XMLConstants.ATTR_TPAMF);
			String amount = MiniXMLUtil.getTagTextValue(xmlResponse, xpath + XMLConstants.ATTR_TXAMF);
			String trxDetailId = MiniXMLUtil.getTagTextValue(xmlResponse, xpath + XMLConstants.TRANSACTION_ID);
			String total = MiniXMLUtil.getTagTextValue(xmlResponse, xpath + XMLConstants.ATTR_TAMTF);

			request.setAttribute(XMLConstants.ATTR_TRXID, trxCode);
			request.setAttribute(XMLConstants.ATTR_SWCNIC, senderCnic);
			request.setAttribute(XMLConstants.ATTR_RWCNIC, receiverCnic);
			request.setAttribute(XMLConstants.ATTR_SWMOB, senderMobile);
			request.setAttribute(XMLConstants.ATTR_RWMOB, receiverMobile);
			request.setAttribute(XMLConstants.ATTR_DATEF, date);
			request.setAttribute(XMLConstants.ATTR_TIMEF, time);
			request.setAttribute(XMLConstants.ATTR_CAMTF, commission);
			request.setAttribute(XMLConstants.ATTR_TPAMF, charges);
			request.setAttribute(XMLConstants.ATTR_TXAMF, amount);
			request.setAttribute(XMLConstants.ATTR_TAMTF, total);

			if(!StringUtil.isNullOrEmpty(trxDetailId)) {

				request.setAttribute("isInclusiveTransaction", agentWebManager.isInclusiveTransaction(Long.valueOf(trxDetailId)));
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private Boolean logActionLogModel() {

		ActionLogModel actionLogModel = new ActionLogModel();
		actionLogModel.setAppUserIdAppUserModel(ThreadLocalAppUser.getAppUserModel());
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END);
		actionLogModel.setEndTime(new Timestamp(new Date().getTime()));

		ActionLogManager actionLogManager = (ActionLogManager) this.allPayWebResponseDataPopulator.getBean("actionLogManager");

		BaseWrapper baseWrapperActionLog = new BaseWrapperImpl();

		baseWrapperActionLog.setBasePersistableModel(actionLogModel);

		try {

			baseWrapperActionLog = actionLogManager.createOrUpdateActionLog(baseWrapperActionLog);

		} catch (FrameworkCheckedException e) {
			logger.error(e.getLocalizedMessage());
		}

		if(baseWrapperActionLog != null && baseWrapperActionLog.getBasePersistableModel() != null) {

			actionLogModel = (ActionLogModel) baseWrapperActionLog.getBasePersistableModel();

			ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

			return Boolean.TRUE;
		}

		return Boolean.FALSE;
	}


	public void setMfsWebController(MfsWebManager mfsWebController) {
		this.mfsWebController = mfsWebController;
	}
	public void setMfsWebResponseDataPopulator(MfsWebResponseDataPopulator mfsWebResponseDataPopulator) {
		this.mfsWebResponseDataPopulator = mfsWebResponseDataPopulator;
	}
	public void setAllPayWebResponseDataPopulator(AllPayWebResponseDataPopulator allPayWebResponseDataPopulator) {
		this.allPayWebResponseDataPopulator = allPayWebResponseDataPopulator;
	}
	public void setAgentWebManager(AgentWebManager agentWebManager) {
		this.agentWebManager = agentWebManager;
	}
}