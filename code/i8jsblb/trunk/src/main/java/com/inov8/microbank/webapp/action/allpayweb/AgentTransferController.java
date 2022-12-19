package com.inov8.microbank.webapp.action.allpayweb;

import static com.inov8.microbank.webapp.action.allpayweb.AllPayWebResponseDataPopulator.isTokenValid;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.MfsWebUtil;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.mfsweb.MfsWebManager;
import com.inov8.microbank.mfsweb.MfsWebResponseDataPopulator;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;

/**
 * 
 * @author Kashif Bashir
 * @since November, 2012
 * 
 */

public class AgentTransferController extends AdvanceFormController {

	private MfsWebManager mfsWebController;
	private MfsWebResponseDataPopulator mfsWebResponseDataPopulator;
	private AllPayWebResponseDataPopulator allPayWebResponseDataPopulator;

	private final static Log logger = LogFactory.getLog(AgentTransferController.class);
	private final String PAGE_FROM = getSuccessView();

	
	private PayCashWithDrawalController payCashWithDrawalController = null;
	
	public AgentTransferController() {
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


	@Override
	protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse response, Object commandObject, BindException exception) throws Exception {
		
		logger.info("[AgentTransferController.onCreate] Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
		
		if(!isTokenValid(request)) {		
			return new ModelAndView(AllPayWebConstant.GENERIC_PAGE.getValue());
		}
		
		long start = System.currentTimeMillis();
		
		AllPayRequestWrapper requestWrapper = initializeRequest(request);
		
		AppUserModel appUserModel = allPayWebResponseDataPopulator.getAppUserModel(requestWrapper.getParameter(CommandFieldConstants.KEY_U_ID));
		ThreadLocalAppUser.setAppUserModel(appUserModel);	
		
		if(!isValidReceiverAgent(request, appUserModel.getMobileNo())) {
			return new ModelAndView(getFormView());
		}	
		/*
		if(!isValidProductLimit(requestWrapper)) {
			
			return new ModelAndView(getFormView());
		}
		*/
		String nextView = getSuccessView();		
		
		String responseXml = mfsWebController.handleRequest(requestWrapper, CommandFieldConstants.AGENT_TO_AGENT_INFO);
		
		
		if(MfsWebUtil.isErrorXML(responseXml)) {
			
			mfsWebResponseDataPopulator.populateErrorMessages(requestWrapper, responseXml);	
			
			String errorType = allPayWebResponseDataPopulator.getErrorType(requestWrapper);
			
			if(AllPayWebConstant.LIMIT_EXCEED.getValue().equals(errorType)) {	
				
				nextView = AllPayWebConstant.GENERIC_PAGE.getValue();		
				
			} else if(AllPayWebConstant.BLANK_SPACE.getValue().equals(errorType)) {	
				
				nextView = AllPayWebConstant.GENERIC_PAGE.getValue();
				
			} else if(AllPayWebConstant.SERVICE_UNAVAILABLE.getValue().equals(errorType)) {	
				
				nextView = AllPayWebConstant.GENERIC_PAGE.getValue();					
			}

		} else if(responseXml != null && !(AllPayWebConstant.BLANK_SPACE.getValue().equals(responseXml))) {
			
			updateRequest(requestWrapper, responseXml);	
			nextView = getSuccessView();		
		}	

		return new ModelAndView(nextView);
	}
	
	
	/**
	 * @param request
	 * @return
	 */
	protected Boolean isValidReceiverAgent(HttpServletRequest request, String senderMobileNo) {

		Boolean isValidAgent = Boolean.TRUE;
		
		String errors = "";
		
		String recevierMobileNo =  request.getParameter(CommandFieldConstants.KEY_MOB_NO);
		AppUserModel recevierAppUserModel = new AppUserModel();
		recevierAppUserModel.setMobileNo(recevierMobileNo);
		recevierAppUserModel.setAppUserTypeId(UserTypeConstantsInterface.RETAILER);
		
		recevierAppUserModel = allPayWebResponseDataPopulator.getAppUserModelByQuery(recevierAppUserModel);
		
		logger.info("[AgentTransferController.onCreate] Validating Reciver Agent Mobile No:" + recevierMobileNo + ". Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());

		if(recevierAppUserModel != null) {
			
			if(senderMobileNo.equals(recevierMobileNo)) {
				errors = "Sender and Receiver Agent cannot be same.";
				isValidAgent = Boolean.FALSE;
			}

			if(isValidAgent && (recevierAppUserModel.getAccountClosedUnsettled() || recevierAppUserModel.getAccountClosedSettled())) {
				errors = "Receiver agent account has been closed";
				isValidAgent = Boolean.FALSE;
			}
			if(isValidAgent && !recevierAppUserModel.getAccountEnabled()) {
				errors = "Receiver Agent is Not Enabled";
				isValidAgent = Boolean.FALSE;
			}
			if(isValidAgent && recevierAppUserModel.getAccountLocked()) {
				errors = "Receiver Agent Account is Locked";
				isValidAgent = Boolean.FALSE;
			}
			if(isValidAgent && !recevierAppUserModel.isEnabled()) {
				errors = "Receiver Agent is Not Enabled";
				isValidAgent = Boolean.FALSE;
			}
			
		} else {
			errors = "Receiver Agent Not Found Against Mobile # "+recevierMobileNo;
			isValidAgent = Boolean.FALSE;
		}		
		
		logger.info("Error before Title Ftech = '"+errors+"'");
		
		if(!isValidAgent) {			
			request.setAttribute("errors", errors);
			logger.error("[AgentTransferController.onCreate] Error in Validating Reciver Agent Mobile No:" + recevierMobileNo + ". Errors:" + errors + ". Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());

		}
		
		if(isValidAgent) {	
			
//			isValidAgent = this.fetchTitle(recevierAppUserModel, request);
			isValidAgent = isValidReceiverAgent(request, recevierAppUserModel);
		}
		
		return isValidAgent;
	}
	
	
	/**
	 * @param requestWrapper
	 * @param recevierAppUserModel
	 * @return
	 */
	protected Boolean isValidReceiverAgent(HttpServletRequest requestWrapper, AppUserModel recevierAppUserModel) {

		Boolean isValidAgent = Boolean.TRUE;

		UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
		userDeviceAccountsModel.setAppUserId(recevierAppUserModel.getAppUserId());
		userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.USSD);
				
		userDeviceAccountsModel = this.allPayWebResponseDataPopulator.getUserDeviceAccountsModel(userDeviceAccountsModel);
		
		if(userDeviceAccountsModel != null) {

			if(!userDeviceAccountsModel.getAccountEnabled()) {
				requestWrapper.setAttribute("errors", "Receiver's Account is not Active.");
				isValidAgent = Boolean.FALSE;
			}
			if(isValidAgent && userDeviceAccountsModel.getAccountLocked()) {
				requestWrapper.setAttribute("errors", "Receiver's Account is Locked.");
				isValidAgent = Boolean.FALSE;
			}
			if(isValidAgent && userDeviceAccountsModel.getAccountExpired()) {
				requestWrapper.setAttribute("errors", "Receiver's Account has been Expired.");
				isValidAgent = Boolean.FALSE;
			}
			if(isValidAgent && userDeviceAccountsModel.getCredentialsExpired()) {
				requestWrapper.setAttribute("errors", "Receiver's Account Credentials has been Expired.");
				isValidAgent = Boolean.FALSE;
			}
			
			AppUserModel appUserIdAppUserModel = userDeviceAccountsModel.getAppUserIdAppUserModel();
			
			if(appUserIdAppUserModel != null && isValidAgent) {
				
				RetailerContactModel retailerContactModel = appUserIdAppUserModel.getRetailerContactIdRetailerContactModel();
				Long retailerContactId = retailerContactModel.getRetailerContactId();
				
				SmartMoneyAccountModel exampleInstance = new SmartMoneyAccountModel();
				exampleInstance.setRetailerContactId(retailerContactId);
				exampleInstance.setDeleted(Boolean.FALSE);
				exampleInstance.setDefAccount(Boolean.TRUE);
				
				exampleInstance = allPayWebResponseDataPopulator.getSmartMoneyAccountModel(exampleInstance);
				
				if(exampleInstance != null) {
					
					if(!exampleInstance.getActive()) {
						requestWrapper.setAttribute("errors", "Receiver's Account is not active.");
						isValidAgent = Boolean.FALSE;
					}
				} else {
					requestWrapper.setAttribute("errors", "Receiver's Account has been Deleted.");
					isValidAgent = Boolean.FALSE;
				}
			}
		}
				
		return isValidAgent;
	}	
	
	
	private Boolean fetchTitle(AppUserModel recevierAppUserModel, HttpServletRequest request) {

		logger.info("[AgentTransferController.fetchTitle] Fetching Title for Receiver AppUserID:" + (recevierAppUserModel == null ? " is NULL. " : recevierAppUserModel.getAppUserId()) + ". Receiver MobileNo:" + recevierAppUserModel.getMobileNo() + ". Logged in AppUserID:" + " Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());		
		
		Boolean isValidAgent = Boolean.TRUE;
		
		try {
			
			this.allPayWebResponseDataPopulator.fetchTitle(recevierAppUserModel);
			
		} catch (Exception e) {
			isValidAgent = Boolean.FALSE;
			request.setAttribute("errors", e.getMessage());
			logger.error("[AgentTransferController.onCreate] Error in Fetching Title of Receiver AppUserID:" + (recevierAppUserModel == null ? " is NULL. " : recevierAppUserModel.getAppUserId()) + ". Receiver MobileNo:" + recevierAppUserModel.getMobileNo() + ". Error:" + e.getMessage() + ". Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
		}		

		return isValidAgent;
	} 
	
	
	/**
	 * @param requestWrapper
	 * @return
	 */
	/*
	private Boolean isValidProductLimit(AllPayRequestWrapper requestWrapper) {
	
		if(commonCommandManager == null) {
			commonCommandManager = (CommonCommandManager) allPayWebResponseDataPopulator.getBean("commonCommandManager");
		}
		
		Boolean isValidProductLimit = Boolean.TRUE;
		ProductModel productModel = null;
		
		String trfAmount = requestWrapper.getParameter(CommandFieldConstants.KEY_TX_AMOUNT);
		Double amount = Double.valueOf(trfAmount);
		
		try {
			
			ProductModel example = new ProductModel();
			example.setPrimaryKey(50013L);		
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(example);			
			
			baseWrapper = commonCommandManager.loadProduct(baseWrapper);
			productModel = (ProductModel) baseWrapper.getBasePersistableModel();
			
		} catch(Exception e) {
			isValidProductLimit = Boolean.FALSE;
			logger.info(e);
		}		
		
		if(productModel != null) {
	
			if(productModel.getMinLimit() != null && amount.doubleValue() < productModel.getMinLimit().doubleValue()) {	
	
				isValidProductLimit = Boolean.FALSE;
				requestWrapper.setAttribute("errors", "Amount entered is less than the minimum transactional limit.");
				
			} else if(productModel.getMaxLimit() != null && amount.doubleValue() > productModel.getMaxLimit().doubleValue()) {
	
				isValidProductLimit = Boolean.FALSE;
				requestWrapper.setAttribute("errors", "Amount entered exceeds the maximum transactional limit.");
			}
		}
		
		return isValidProductLimit;
	}
	
	*/
	/**
	 * @param request
	 * @return
	 */
	private AllPayRequestWrapper initializeRequest(HttpServletRequest request) {
		
		AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(request);						
		AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);
		
		requestWrapper.addParameter(CommandFieldConstants.KEY_DEVICE_TYPE_ID, String.valueOf(DeviceTypeConstantsInterface.ALLPAY_WEB));
		requestWrapper.addParameter(CommandFieldConstants.KEY_PROD_ID, "50013");
		
		return requestWrapper;
	}
	
	
	CommonCommandManager commonCommandManager = null;
	
	void updateRequest(HttpServletRequest request, String xmlResponse) {

		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputStream inputStream = new ByteArrayInputStream(xmlResponse.getBytes());
			
			org.w3c.dom.Document document = builder.parse(inputStream);
			
			NodeList list = document.getElementsByTagName("param");
			
			for(int i = 0; i< list.getLength(); i++) {
				
				Node node = list.item(i);

				String attributeName = node.getAttributes().item(0).getTextContent();
				String attributeValue = node.getTextContent();
				
				request.setAttribute(attributeName.trim(), attributeValue.trim());
			}
			
			OTHER_ATTRIBUTES : {
				
				if(commonCommandManager == null) {
					commonCommandManager = (CommonCommandManager) allPayWebResponseDataPopulator.getBean("commonCommandManager");
				}
				
				AppUserModel agentReceiver = new AppUserModel();
				agentReceiver.setMobileNo(request.getParameter(CommandFieldConstants.KEY_MOB_NO));
				agentReceiver.setAppUserTypeId(UserTypeConstantsInterface.RETAILER);
				
				SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
				searchBaseWrapper.setBasePersistableModel(agentReceiver);
				commonCommandManager.searchAppUserByMobile(searchBaseWrapper);
				
				CustomList<AppUserModel> customList = searchBaseWrapper.getCustomList();
				
				if(customList != null && customList.getResultsetList() != null && customList.getResultsetList().size() == 1) {
				
					agentReceiver = customList.getResultsetList().get(0);
					request.setAttribute("CDCUSTMOB", agentReceiver.getMobileNo());
					request.setAttribute("RECEIVER_NAME", agentReceiver.getFirstName() + " " + agentReceiver.getLastName() );
					request.setAttribute("RECEIVER_CNIC", agentReceiver.getNic());
				}

				String UID = request.getParameter(CommandFieldConstants.KEY_U_ID);
				AppUserModel agentSender = allPayWebResponseDataPopulator.getAppUserModel(UID);
				
				request.setAttribute(CommandFieldConstants.KEY_SENDER_MOBILE, agentSender.getMobileNo());
				request.setAttribute(CommandFieldConstants.KEY_NAME, agentSender.getFirstName() + " " + agentSender.getLastName() );
				request.setAttribute(CommandFieldConstants.KEY_TXAM, request.getParameter(CommandFieldConstants.KEY_TXAM));
				
			}
			
		} catch (Exception e) {
			request.setAttribute("errors", e.getMessage());
			logger.error(e);
		}
		
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
}
