package com.inov8.microbank.webapp.action.allpayweb;

import static com.inov8.microbank.webapp.action.allpayweb.AllPayWebResponseDataPopulator.isTokenValid;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.xpath.XPathExpressionException;

import com.inov8.microbank.common.model.RetailerContactModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.integration.middleware.controller.NadraIntegrationController;
import com.inov8.integration.vo.NadraIntegrationVO;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.MiniTransactionModel;
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.HttpInvokerUtil;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.MfsWebUtil;
import com.inov8.microbank.common.util.MiniTransactionStateConstant;
import com.inov8.microbank.common.util.MiniXMLUtil;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.USSDXMLUtils;
import com.inov8.microbank.mfsweb.MfsWebManager;
import com.inov8.microbank.mfsweb.MfsWebResponseDataPopulator;
import com.inov8.microbank.server.dao.transactionmodule.MiniTransactionDAO;

/**
 * 
 * @author Kashif Bashir
 * @since April, 2012
 * 
 */

public class PayCashTransactionCodeController extends AdvanceFormController {

	private MfsWebManager mfsWebController;
	private MfsWebResponseDataPopulator mfsWebResponseDataPopulator; 
	private AllPayWebResponseDataPopulator allPayWebResponseDataPopulator = null;
	
	private final static Log logger = LogFactory.getLog(PayCashController.class);
	private final String PAGE_FROM = "allpay-web/payCashTransactionCodeVerification";
	public final String CASH_WITHDRAW = "Cash Withdraw";
	public final String ACCOUNT_TO_CASH = "Account To Cash";
	public final String CASH_TO_CASH = "Cash Transfer";
	public final String TRANSACTION_TYPE = "TRANSACTION_TYPE";
	NadraIntegrationVO iVo = new NadraIntegrationVO();
	


	public PayCashTransactionCodeController() {
		setCommandName("object");
	    setCommandClass(Object.class);
	}
	
	
	@Override
	protected Object loadFormBackingObject(HttpServletRequest arg0) throws Exception {
		
		return new Object();
	}

	
	@Override
	protected Map loadReferenceData(HttpServletRequest request) throws Exception {	
		
		AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(request);						
		AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);	
		
		return null;
	}	
	
	
	@Override
	protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object model, BindException exception) throws Exception {
	
		return onCreate(request, response, model, exception);
	}


	/* (non-Javadoc)
	 * @see com.inov8.framework.webapp.action.AdvanceFormController#onCreate(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@Override
	protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse response, Object commandObject, BindException exception) throws Exception {
		
		logger.info("onCreate(...)");
		
		
		if(!isTokenValid(request)) {			
			return new ModelAndView(AllPayWebConstant.GENERIC_PAGE.getValue());
		}		
		
		long start = System.currentTimeMillis();
		
		
		String nextView = getSuccessView();

		AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(request);						
		AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);

		String UID = requestWrapper.getParameter(CommandFieldConstants.KEY_U_ID);
		String productId = requestWrapper.getParameter(CommandFieldConstants.KEY_PROD_ID);	
		String pin = requestWrapper.getParameter(CommandFieldConstants.KEY_PIN);		
		String transactionCode = requestWrapper.getParameter(CommandFieldConstants.KEY_TX_CODE);	
		String customerMobile = requestWrapper.getParameter("MS_ISDN_MOBILE");	
		String transactionId = request.getParameter(CommandFieldConstants.KEY_TX_ID);

		AppUserModel appUserModel = allPayWebResponseDataPopulator.getAppUserModel(UID);
		ThreadLocalAppUser.setAppUserModel(appUserModel);
		
		
		//******************************************
		

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("allpay-web/genericblanknotificationscreen");
        String areaName = allPayWebResponseDataPopulator.getAreaName(appUserModel);
		iVo.setAreaName(areaName);
        iVo.setCitizenNumber(request.getParameter("WALKIN_REC_CNIC"));
        iVo.setContactNo(request.getParameter("MS_ISDN_MOBILE"));
        iVo.setFingerIndex(request.getParameter("FINGER_INDEX"));
        iVo.setFingerTemplate(request.getParameter("FINGER_TEMPLATE"));
		iVo.setSecondaryCitizenNumber(request.getParameter("SWCNIC"));
		iVo.setSecondaryContactNo(request.getParameter("SWMOB"));
		iVo.setTemplateType("ISO_19794_2");
        try {
            iVo = this.getNadraIntegrationController().fingerPrintVerification(iVo);
            if(null == iVo.getResponseCode() || !iVo.getResponseCode().equals("100")) {
               
                request.setAttribute("errors", iVo.getResponseDescription());
                return modelAndView;
            }
        }catch(Exception e){
           
            
            request.setAttribute("errors", "Error while connecting NADRA integeration!");
            return modelAndView;
        }

       //***************************************************************************
    
		
		
		
		
		
	
		
		Map<String, String> responseData = null;
		String commandId = null;

		String CommandTypelog = null;
		if(Integer.parseInt(productId) == AllPayWebConstant.CASH_WITHDRAWAL.getIntValue()) {
			String commandXML = getCWCommandXML(appUserModel.getMobileNo(), pin, transactionCode, customerMobile);			
			requestWrapper.setAttribute(AllPayWebConstant.EXTERNAL_XML.getValue(), commandXML);
			requestWrapper.setAttribute(CommandFieldConstants.KEY_AW_DTID, DeviceTypeConstantsInterface.ALLPAY_WEB);
			commandId = CommandFieldConstants.CMD_MINI_CMD_MNG;
			CommandTypelog = "[PayCashTransactionController.onCreate] Cash Withdrawl - ";
		} else if(Integer.parseInt(productId) == AllPayWebConstant.ACCOUNT_TO_CASH.getIntValue()) {			
			commandId = CommandFieldConstants.CMD_ACCOUNT_TO_CASH_LEG_2;
			CommandTypelog = "[PayCashTransactionController.onCreate] Account To Cash - ";
			
		} else if(Integer.parseInt(productId) == AllPayWebConstant.CASH_TO_CASH.getIntValue()) {
			//commandId = CommandFieldConstants.CMD_CASH_TO_CASH_LEG2;
			commandId ="143";
			CommandTypelog = "[PayCashTransactionController.onCreate] Cash To Cash - ";
		} else if(Integer.parseInt(productId) == AllPayWebConstant.BULK_PAYMENT.getIntValue()) {
			commandId = CommandFieldConstants.CMD_BULK_PAYMENT_LEG2;			
			CommandTypelog = "[PayCashTransactionController.onCreate] Cash To Cash - ";
		}
		
		
		String responseXml = mfsWebController.handleRequest(requestWrapper, commandId);

		if(MfsWebUtil.isErrorXML(responseXml)) {
			
			mfsWebResponseDataPopulator.populateErrorMessages(requestWrapper, responseXml);
			nextView = AllPayWebConstant.GENERIC_PAGE.getValue();
			requestWrapper.setAttribute(AllPayWebConstant.HEADING.getValue(), "Error(s)");
			
			
		} else if(responseXml != null && !(AllPayWebConstant.BLANK_SPACE.getValue().equals(responseXml))) {
			
			responseData = getResponseData(responseXml, requestWrapper, commandId);
			
			if(commandId != CommandFieldConstants.CMD_CASH_TO_CASH_LEG2 && commandId != CommandFieldConstants.CMD_ACCOUNT_TO_CASH_LEG_2) {
				markTransactionAsExpired(transactionId, requestWrapper);
			}
		}

		ThreadLocalAppUser.remove();
		
		logger.info("\n\nTransaction Took :"+((System.currentTimeMillis() - start)/1000)+ " Seconds");
		
		return new ModelAndView(nextView, "responseData", responseData);
	}
	
	/**
	 * @param xml
	 * @param requestWrapper
	 * @param commandId
	 * @return
	 * @throws Exception
	 */
	protected Map<String, String> getResponseData(String xml, AllPayRequestWrapper requestWrapper, String commandId) throws Exception {

		logger.info("getResponseData(String commandId "+commandId);
			
		Map<String, String> responseData = null;
		
		String responseXml = xml;//MiniXMLUtil.getTagTextValue(xml, "string(/msg)");

		if(CommandFieldConstants.CMD_MINI_CMD_MNG.equals(commandId)) {

			responseData = getCWResponseXML(responseXml, requestWrapper);
			
		} else if(CommandFieldConstants.CMD_ACCOUNT_TO_CASH_LEG_2.equals(commandId)) {

			responseData = getA2CResponseXML(responseXml, requestWrapper);
			
		} else if(CommandFieldConstants.RECEIVE_CASH_COMMAND_COMMAND.equals(commandId)) {

            responseData = getResponseXML(responseXml, requestWrapper);

        } else if(CommandFieldConstants.CMD_BULK_PAYMENT_LEG2.equals(commandId)) {

			responseData = getBPResponseXML(responseXml, requestWrapper);		
		}		

		return responseData;
	}	

	
	/**
	 * @param responseXml
	 * @param requestWrapper
	 * @return
	 */
	protected Map<String, String> getBPResponseXML(String responseXml, AllPayRequestWrapper requestWrapper) {

		Map<String, String> responseData = new HashMap<String, String>();
		
		StringBuilder xmlResponseBuilder = new StringBuilder(responseXml.replaceAll("\n", ";"));
		
		String[] lines = xmlResponseBuilder.toString().split(";");
		
		int i = 0;
		
		for(String line : lines) {
			
			String[] KV = line.split(":");

			if(i == 0) {
				requestWrapper.setAttribute("Heading", KV[0]);
				requestWrapper.setAttribute("Name", "WalkIn Customer");
			} else if(i == 1) {
				responseData.put("CNIC", KV[1].trim());
			} else if(i == 2) {
				responseData.put("Charges", KV[1].trim());
			} else if(i == 3) {
				responseData.put("Amount", KV[1].trim());
			} else if(i == 4) {
				responseData.put("Transaction ID", KV[1].trim());
			} else if(i == 5) {
				responseData.put("Dated", KV[0].trim()+":"+KV[1].trim());
			} else if(i == 6) {
				responseData.put("Current Balance", (KV[1]).trim());
			}

			i++;
		}	

		return responseData;
	}	
	
	
	
	public static void main(String f[]) {

		String xml = "Fund Transferred to \nCNIC: 3410271565424 \nAmount: Rs. 4950.00 \nTx ID: 594083712079 \n30/06/14 05:45 PM \n A/C balance: Rs. 208619.64";

		String s = "Fund Transferred\nCNIC:1111111\nCharges: 10\nPayable Amount: 200\nTx ID: 44444\n21/06/89 5:10\nA/C balance: Rs. 100";
		
		System.out.println(new PayCashTransactionCodeController().getBPResponseXML(s, null));
		
	}
	
	/**
	 * @param responseXml
	 * @param requestWrapper
	 * @return
	 */
	protected Map<String, String> getC2CResponseXML(String responseXml, AllPayRequestWrapper requestWrapper) {

		Map<String, String> responseData = new HashMap<String, String>();

		StringBuilder xmlResponseBuilder = new StringBuilder(responseXml.replaceAll("\n", ";"));
		
		String[] lines = xmlResponseBuilder.toString().split(";");
		
		int i = 0;
		
		for(String line : lines) {

			String[] KV = line.split(":");

			if(i == 0) {
				requestWrapper.setAttribute("Heading", KV[0]);
			} else if(i == 1) {
				requestWrapper.setAttribute("Name", "WalkIn Customer");
			} else if(i == 2) {
				responseData.put("Amount", KV[1]);
			} else if(i == 3) {
//				responseData.put("Charges/Tax", (KV[1]));
			} else if(i == 4) {
				responseData.put("Transaction ID", KV[1]);
			} else if(i == 5) {
				responseData.put("Dated", KV[0].trim()+":"+KV[1]);
			} else if(i == 6) {
				responseData.put("Current Balance", (KV[1]));
			}
			
			i++;
		}

		return responseData;
	}	
	

	/**
	 * @param responseXml
	 * @param request
	 * @return
	 */
	protected static Map<String, String> getCWResponseXML(String responseXml, HttpServletRequest request) {

		Map<String, String> responseData = new HashMap<String, String>();
		
		StringBuilder xmlResponseBuilder = new StringBuilder(responseXml.replaceAll("\n", ";"));
		
		String[] lines = xmlResponseBuilder.toString().split(";");
		
		int i = 0;
		
		for(String line : lines) {
			
			String[] KV = line.split(":");

			if(i == 0) {
				request.setAttribute("Heading", KV[0]);
			} else if(i == 1) {
				request.setAttribute("Name", KV[1]);
			} else if(i == 2) {
				responseData.put("Amount", KV[1]);
			} else if(i == 3) {
				responseData.put("Transaction ID", KV[1]);
			} else if(i == 4) {
				responseData.put("Dated", KV[0].trim()+":"+KV[1]);
			} else if(i == 5) {
				responseData.put("Current Balance", (KV[0]));
			}
			
			i++;
		}	

		return responseData;
	}
	
	
	/**
	 * @param responseXml
	 * @param request
	 * @return
	 */
	private Map<String, String> getA2CResponseXML(String responseXml, HttpServletRequest request) {

		Map<String, String> responseData = new HashMap<String, String>();
		
		StringBuilder xmlResponseBuilder = new StringBuilder(responseXml.replaceAll("\n", ";"));
		
		String[] lines = xmlResponseBuilder.toString().split(";");

		request.setAttribute("Name", "Walkin Customer");
		responseData.put("Name", "Walkin Customer");
		
		int i = 0;
		
		for(String line : lines) {
			
			String[] KV = line.split(":");
			
			if(i == 0) {
				request.setAttribute("Heading", KV[0]);
			} else if(i == 1) {
//				responseData.put("", ""); blank
			} else if(i == 2) {
				responseData.put("Amount", KV[1]);
			} else if(i == 3) {
//				responseData.put("Charges & Tax", (KV[1]));
			} else if(i == 4) {
				responseData.put("Transaction ID", KV[1]);
			} else if(i == 5) {
				responseData.put("Dated", KV[0].trim()+":"+KV[1]);
			} else if(i == 6) {
				responseData.put("Current Balance", (KV[1]));
			}
			
			i++;
		}	
		
		return responseData;
	}

    protected Map<String, String> getResponseXML(String responseXml, AllPayRequestWrapper requestWrapper) {

        Map<String, String> responseData = new LinkedHashMap<>();

        try {
            String productName = MiniXMLUtil.getTagTextValue(responseXml, "/msg/trans/trn/@PROD");
            String trxCode = MiniXMLUtil.getTagTextValue(responseXml, "/msg/trans/trn/@TRXID");
            String recipientMobile = MiniXMLUtil.getTagTextValue(responseXml, "/msg/trans/trn/@RWMOB");
            String recipientCNIC = MiniXMLUtil.getTagTextValue(responseXml, "/msg/trans/trn/@RWCNIC");
            String date = MiniXMLUtil.getTagTextValue(responseXml, "/msg/trans/trn/@DATEF");
            String time = MiniXMLUtil.getTagTextValue(responseXml, "/msg/trans/trn/@TIMEF");
            String TAMTF = MiniXMLUtil.getTagTextValue(responseXml, "/msg/trans/trn/@TAMTF");
            String BALF = MiniXMLUtil.getTagTextValue(responseXml, "/msg/trans/trn/@BALF");

            responseData.put("Product", productName);
            responseData.put("Tx ID", trxCode);
            responseData.put("Recipient CNIC", recipientCNIC);
            responseData.put("Recipient Mobile", recipientMobile);
            responseData.put("Date", date);
            responseData.put("Time", time);
            responseData.put("Amount To Be Paid", TAMTF);
            responseData.put("Balance", BALF);

        } catch (Exception e) {
            logger.error("[PayCashTransactionCodeController.getResponseXML] Exception while parsing XML response...", e);
        }

        return responseData;
    }

    /**
	 * @param agentMobile
	 * @param pin
	 * @param transactionCode
	 * @param customerMobile
	 * @return
	 */
	private String getCWCommandXML(String agentMobile, String pin, String transactionCode, String customerMobile) {
		
		String commandXML = USSDXMLUtils.prepareXmlMessage("44", "UssdApp", "kanne!", "4", agentMobile, 
														"PC "+ MfsWebUtil.encryptPin(pin) +" "+ transactionCode +" "+ customerMobile, "50000");

		String reqTimeNode = "<param name="+'"'+"reqTime"+'"'+">"+System.currentTimeMillis()+"</param>";
		String AW_DTID = "<param name="+'"' + CommandFieldConstants.KEY_AW_DTID + '"'+">8</param>";
		
		StringBuilder xmlResponse = new StringBuilder(commandXML);

		xmlResponse.insert(xmlResponse.indexOf("</params>"), reqTimeNode);	
		xmlResponse.insert(xmlResponse.indexOf("</params>"), AW_DTID);	
		
		return xmlResponse.toString();
	}

	
	/**
	 * @param transactionId
	 * @param request
	 */
	private void markTransactionAsExpired(String transactionId, HttpServletRequest request) {		
		
		MiniTransactionModel miniTransactionModel = null;
		
		PayCashController payCashController = ((PayCashController) this.getWebApplicationContext().getBean("payCashController"));
		
		TransactionCodeModel transactionCodeModel = payCashController.loadTransactionCodeByCode(transactionId);
		
		if(transactionCodeModel != null) {
			miniTransactionModel = payCashController.loadMiniTransactionModelByTrId(transactionCodeModel, request);
		}

		if(miniTransactionModel != null)
		{
			MiniTransactionDAO miniTransactionDAO = (MiniTransactionDAO) allPayWebResponseDataPopulator.getBean("miniTransactionDAO");

			miniTransactionModel.setMiniTransactionStateId(MiniTransactionStateConstant.EXPIRED);

			if(allPayWebResponseDataPopulator.logActionLogModel()) {

				logger.info("PayCashWithDrawalController : Marking MiniTransactionModel as EXPIRED"+miniTransactionModel);

				Long actionLogId = ThreadLocalActionLog.getActionLogId();
				miniTransactionModel.setActionLogId(actionLogId);

				miniTransactionDAO.saveOrUpdate(miniTransactionModel);
			}
		}
	}	
	
	  private NadraIntegrationController getNadraIntegrationController() {
	        return HttpInvokerUtil.getHttpInvokerFactoryBean(NadraIntegrationController.class,
	                MessageUtil.getMessage("NadraIntegrationURL"));
	    }
	
	public void setAllPayWebResponseDataPopulator(AllPayWebResponseDataPopulator allPayWebResponseDataPopulator) {
		this.allPayWebResponseDataPopulator = allPayWebResponseDataPopulator;
	}		
	public void setMfsWebController(MfsWebManager mfsWebController) {
		this.mfsWebController = mfsWebController;
	}	
	public void setMfsWebResponseDataPopulator(MfsWebResponseDataPopulator mfsWebResponseDataPopulator) {
		this.mfsWebResponseDataPopulator = mfsWebResponseDataPopulator;
	}
}