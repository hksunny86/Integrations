package com.inov8.microbank.webapp.action.teller;

import static com.inov8.microbank.common.util.CommandFieldConstants.KEY_CAMTF;
import static com.inov8.microbank.common.util.CommandFieldConstants.KEY_CNIC;
import static com.inov8.microbank.common.util.CommandFieldConstants.KEY_FORMATTED_TX_AMOUNT;
import static com.inov8.microbank.common.util.CommandFieldConstants.KEY_NAME;
import static com.inov8.microbank.common.util.CommandFieldConstants.KEY_TAMTF;
import static com.inov8.microbank.common.util.CommandFieldConstants.KEY_USER_TYPE;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.RequestWrapper;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.JSFContext;
import com.inov8.microbank.common.util.MiniXMLUtil;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.mfsweb.MfsWebManager;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.webapp.action.allpayweb.AllPayRequestWrapper;
import com.inov8.microbank.webapp.action.allpayweb.AllPayWebResponseDataPopulator;
import com.inov8.microbank.webapp.action.allpayweb.LoginController;


/**
 * @author Kashif Bashir
 *
 */

@ManagedBean(name="tellerTransactionsManagedBean", eager=true)
@ViewScoped
@SuppressWarnings("all")
public class TellerTransactionsManagedBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private final static Log logger = LogFactory.getLog(TellerTransactionsManagedBean.class);
	
	private TellerTransactionsBackingBean tellerTransactionsBackingBean;

	@ManagedProperty(value="#{appUserDAO}")
	private AppUserDAO appUserDAO;
	@ManagedProperty(value="#{commonCommandManager}")
	private CommonCommandManager commonCommandManager;
	@ManagedProperty(value="#{mfsWebManager}")
	private MfsWebManager mfsWebManager;


	/**
	 * @param responseXml
	 * @throws Exception
	 */
	public void populateBackingEntityInfo(String responseXml) throws Exception {

		String name = MiniXMLUtil.getTagTextValue(responseXml, "/msg/params/param[@name='"+KEY_NAME+"']/text()");
		String cnic = MiniXMLUtil.getTagTextValue(responseXml, "/msg/params/param[@name='"+KEY_CNIC+"']/text()");
		String userType = MiniXMLUtil.getTagTextValue(responseXml, "/msg/params/param[@name='"+KEY_USER_TYPE+"']/text()");
		String amount = MiniXMLUtil.getTagTextValue(responseXml, "/msg/params/param[@name='"+KEY_FORMATTED_TX_AMOUNT+"']/text()");
		String commissionAmount = MiniXMLUtil.getTagTextValue(responseXml, "/msg/params/param[@name='"+KEY_CAMTF+"']/text()");
		String totalAmount = MiniXMLUtil.getTagTextValue(responseXml, "/msg/params/param[@name='"+KEY_TAMTF+"']/text()");
		
		this.getBackingEntity().setAmountFormated(amount);
		this.getBackingEntity().setCnic(cnic);
		this.getBackingEntity().setCommisttionAmount(commissionAmount);
		this.getBackingEntity().setTotalAmount(totalAmount);
		this.getBackingEntity().setUserName(name);
		this.getBackingEntity().setUserType(userType);
	}
	
	
	/**
	 * @param responseXml
	 * @throws Exception
	 */
	public void populateBackingEntityResponse(String responseXml) throws Exception {

		String productId = MiniXMLUtil.getTagTextValue(responseXml, "/msg/trans/trn/@"+CommandFieldConstants.KEY_PRODUCT_ID);
		String transactionCode = MiniXMLUtil.getTagTextValue(responseXml, "/msg/trans/trn/@"+CommandFieldConstants.KEY_TX_ID);
		String amountFormated = MiniXMLUtil.getTagTextValue(responseXml, "/msg/trans/trn/@"+CommandFieldConstants.KEY_TXAMF);
		String transactionDate = MiniXMLUtil.getTagTextValue(responseXml, "/msg/trans/trn/@"+CommandFieldConstants.KEY_DATEF);
		String transactionTime = MiniXMLUtil.getTagTextValue(responseXml, "/msg/trans/trn/@"+CommandFieldConstants.KEY_TIMEF);
		String commisttionAmount = MiniXMLUtil.getTagTextValue(responseXml, "/msg/trans/trn/@"+CommandFieldConstants.KEY_CAMTF);
		String tpamf = MiniXMLUtil.getTagTextValue(responseXml, "/msg/trans/trn/@"+CommandFieldConstants.KEY_TPAMF);
		String senderMobileNumber = MiniXMLUtil.getTagTextValue(responseXml, "/msg/trans/trn/@"+CommandFieldConstants.KEY_WALKIN_SENDER_MSISDN);
		String senderCnic = MiniXMLUtil.getTagTextValue(responseXml, "/msg/trans/trn/@"+CommandFieldConstants.KEY_WALKIN_SENDER_CNIC);
		String productName = MiniXMLUtil.getTagTextValue(responseXml, "/msg/trans/trn/@"+CommandFieldConstants.KEY_PRODUCT);
		String userName = MiniXMLUtil.getTagTextValue(responseXml, "/msg/trans/trn/@"+CommandFieldConstants.KEY_NAME);
		String totalAmount = MiniXMLUtil.getTagTextValue(responseXml, "/msg/trans/trn/@"+CommandFieldConstants.KEY_TOTAL_AMOUNT);
		
		this.getBackingEntity().setProductId(Long.valueOf(productId));
		this.getBackingEntity().setTransactionCode(transactionCode);
		this.getBackingEntity().setAmountFormated(amountFormated);
		this.getBackingEntity().setTransactionDate(transactionDate);
		this.getBackingEntity().setTransactionTime(transactionTime);
		this.getBackingEntity().setCommisttionAmount(commisttionAmount);
		this.getBackingEntity().setSenderMobileNumber(senderMobileNumber);
		this.getBackingEntity().setProductName(productName);
		this.getBackingEntity().setCnic(senderCnic);
		this.getBackingEntity().setTotalAmount(totalAmount);

		if(ProductConstantsInterface.TELLER_ACCOUNT_HOLDER_CASH_IN.longValue() == this.getBackingEntity().getProductId().longValue()) {
			this.getBackingEntity().setUserType("Cash In: Account Holder");
		} else if(ProductConstantsInterface.TELLER_WALK_IN_CASH_IN.longValue() == this.getBackingEntity().getProductId().longValue()) {
			this.getBackingEntity().setUserType("Cash In: Walkin Customer");
		}
		
		this.getBackingEntity().setUserName(userName);
	}
	
	
	/**
	 * @param httpServletRequest
	 * @return
	 */
	private Boolean isValidRequest(HttpServletRequest httpServletRequest) {

		return AllPayWebResponseDataPopulator.isTokenValid(httpServletRequest);		
	}
	
	
	/**
	 * @return
	 */
	public String execute() {

		String transactionCode = null;
		
		HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();		
		
		AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(httpServletRequest);
		requestWrapper.addParameter(CommandFieldConstants.KEY_DEVICE_TYPE_ID.toString(), "5");
		requestWrapper.addParameter(CommandFieldConstants.KEY_TX_AMOUNT, this.tellerTransactionsBackingBean.getAmount().toString());
		requestWrapper.addParameter(CommandFieldConstants.KEY_PROD_ID, String.valueOf(this.tellerTransactionsBackingBean.getProductId()));

		requestWrapper.addParameter(CommandFieldConstants.KEY_MOB_NO, this.tellerTransactionsBackingBean.getAppUserModel().getMobileNo());
		requestWrapper.addParameter(CommandFieldConstants.KEY_WALKIN_SENDER_MSISDN, this.tellerTransactionsBackingBean.getSenderMobileNumber());
		requestWrapper.addParameter(CommandFieldConstants.KEY_WALKIN_SENDER_CNIC, this.tellerTransactionsBackingBean.getCnic());
		
		Boolean isError= Boolean.FALSE;
		String responseXml = null;
		
		try {
			
			if(!isValidRequest(httpServletRequest)) {

				throw new CommandException((String)httpServletRequest.getAttribute("errors"),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());	
			}
			
			AppUserModel appUserModel = UserUtils.getCurrentUser();
			
			if(appUserModel != null && appUserModel.getPrimaryKey().longValue() > 1) {

				ThreadLocalAppUser.setAppUserModel(appUserModel);
				
			} else {
					
				throw new CommandException("Login User Not Found, Please Retry.",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());				
			}
			
			responseXml = runCommand(requestWrapper);
			
			logger.info(responseXml);
			
			if(StringUtil.isNullOrEmpty(responseXml) && responseXml.contains("errors")) {

				throw new CommandException(getError(responseXml),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());	
			}			
			
			populateBackingEntityResponse(responseXml);
			
			if(this.tellerTransactionsBackingBean.getProductId().longValue() == ProductConstantsInterface.TELLER_CASH_OUT.longValue()) {
				
				JSFContext.setInfoMessage("An IVR call has been sent, Kindly response it to make Transaction completed successfully.");
				
			} else {
				
				JSFContext.setInfoMessage("Transaction has completed successfully.");
			}

			httpServletRequest.setAttribute(CommandFieldConstants.KEY_TX_CODE, responseXml);
			
			ThreadLocalAppUser.remove();				
			ThreadLocalAppUser.setAppUserModel(null);
			
		} catch (Exception e) {
			isError= Boolean.TRUE;
			e.printStackTrace();

			addErrorMessage(null, e.getLocalizedMessage());
			return null;
			
		} finally {
			
			ThreadLocalAppUser.remove();				
			ThreadLocalAppUser.setAppUserModel(null);
		}

		if(isError) {

			String productId = httpServletRequest.getParameter(CommandFieldConstants.KEY_PROD_ID);
			
			if(Long.parseLong(productId) == ProductConstantsInterface.TELLER_ACCOUNT_HOLDER_CASH_IN.longValue()) {

				return "p_cashInAccountHolder";
				
			} else if(Long.parseLong(productId) == ProductConstantsInterface.TELLER_WALK_IN_CASH_IN.longValue()) {

				return "p_cashInWalkinCustomer";
			}			
		}
		
		return "p_cashInTellerSummery";
	}

	
	/**
	 * @param requestWrapper
	 * @return
	 * @throws Exception
	 */
	private String runCommand(AllPayRequestWrapper requestWrapper) throws Exception {
		
		Long productId = this.tellerTransactionsBackingBean.getProductId();
		String commandId = null;
		
		if(productId.longValue() == ProductConstantsInterface.TELLER_WALK_IN_CASH_IN.longValue() ||
				productId.longValue() == ProductConstantsInterface.TELLER_ACCOUNT_HOLDER_CASH_IN.longValue()) {
			
			commandId = CommandFieldConstants.TELLER_CASH_IN_COMMAND;		
			
		} else if(productId.longValue() == ProductConstantsInterface.TELLER_CASH_OUT.longValue()) {

			commandId = CommandFieldConstants.TELLER_CASH_OUT_COMMAND;	
		}
		
		String responseXml = mfsWebManager.handleRequest(requestWrapper, commandId, Boolean.TRUE);
		
		logger.info(responseXml);
	
		return responseXml;
	}
	
	
	/**
	 * @return
	 */
	public Boolean validateInputs() {

		Boolean validInputs = Boolean.TRUE;
		String mobileNumber = this.tellerTransactionsBackingBean.getAppUserModel().getMobileNo();
		
		if(StringUtil.isNullOrEmpty(mobileNumber)) {

			addErrorMessage(null, "Kindly Enter 11 Digit Mobile Number");
			validInputs = Boolean.FALSE;

		} else if(!StringUtil.isNumeric(mobileNumber) || mobileNumber.length() !=11) {

			addErrorMessage(null, "Kindly Enter 11 Digit Mobile Number");
			validInputs = Boolean.FALSE;
		}
		
		if(this.tellerTransactionsBackingBean.getAmount() == null || this.tellerTransactionsBackingBean.getAmount() == 0.0 ) {

			addErrorMessage(null, "Kindly Enter valid Amount.");
			validInputs = Boolean.FALSE;
		}
		
		
		if(ProductConstantsInterface.TELLER_WALK_IN_CASH_IN.longValue() == this.tellerTransactionsBackingBean.getProductId().longValue()) {

			if(StringUtil.isNullOrEmpty(this.tellerTransactionsBackingBean.getSenderMobileNumber())) {

				addErrorMessage(null, "Kindly Enter 11 Digit Sender Mobile Number");
				validInputs = Boolean.FALSE;
				
			} else if(mobileNumber.equalsIgnoreCase(this.tellerTransactionsBackingBean.getSenderMobileNumber())) {

				addErrorMessage(null, "Sender and Recipient Mobile Numbers can't be same.");
				validInputs = Boolean.FALSE;
			}
			
			else if(this.tellerTransactionsBackingBean.getSenderMobileNumber().length() !=11) {

				addErrorMessage(null, "Kindly Enter 11 Digit Sender Mobile Number");
				validInputs = Boolean.FALSE;
				
			} else if(StringUtil.isNullOrEmpty(this.tellerTransactionsBackingBean.getCnic())) {

				addErrorMessage(null, "Kindly Enter 13 Digit Sender CNIC");
				validInputs = Boolean.FALSE;
				
			} else if(!StringUtil.isNullOrEmpty(this.tellerTransactionsBackingBean.getCnic()) && this.tellerTransactionsBackingBean.getCnic().length() != 13) {
	
					addErrorMessage(null, "Kindly Enter 13 Digit Sender CNIC");
					validInputs = Boolean.FALSE;
			}
		}
		
		return validInputs;
	}
	
	
	
	/**
	 * @return
	 * @throws FrameworkCheckedException
	 */
	public TellerTransactionsBackingBean verifyDetails() throws FrameworkCheckedException {

		AppUserModel currentUser = UserUtils.getCurrentUser();
				
		try {
		
			if(currentUser!= null && StringUtil.isNullOrEmpty(currentUser.getTellerId())) {
				
				throw new CommandException("Bank User ["+currentUser.getFirstName() +" "+ currentUser.getLastName()+"] is not associated with Teller ID",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());	
				
			} else if(currentUser != null && currentUser.getPrimaryKey().longValue() > 1) {
	
				ThreadLocalAppUser.setAppUserModel(currentUser);
			}
			
			if(!validateInputs()) {
				
				return this.getBackingEntity();
			}

			String responseXml = null;	
			
			HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			
			AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(httpServletRequest);
			requestWrapper.addParameter(CommandFieldConstants.KEY_DEVICE_TYPE_ID.toString(), "5");
			requestWrapper.addParameter(CommandFieldConstants.KEY_MSISDN, this.tellerTransactionsBackingBean.getAppUserModel().getMobileNo());
			requestWrapper.addParameter(CommandFieldConstants.KEY_SENDER_MOBILE, this.tellerTransactionsBackingBean.getSenderMobileNumber());
			requestWrapper.addParameter(CommandFieldConstants.KEY_TX_AMOUNT, this.tellerTransactionsBackingBean.getAmount().toString());
			requestWrapper.addParameter(CommandFieldConstants.KEY_CNIC, this.tellerTransactionsBackingBean.getCnic());
			requestWrapper.addParameter(CommandFieldConstants.KEY_PRODUCT_ID, String.valueOf(this.tellerTransactionsBackingBean.getProductId()));
			
			responseXml = mfsWebManager.handleRequest(requestWrapper, CommandFieldConstants.TELLER_CASH_IN_INFO_COMMAND, Boolean.TRUE);
			
			logger.info(responseXml);
			
			if(responseXml.contains("errors")) {

				throw new CommandException(getError(responseXml),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());		
			}

			populateBackingEntityInfo(responseXml);
		
			
		} catch (Exception e) {
		
			e.printStackTrace();
			addErrorMessage(null, e.getLocalizedMessage());
		
		} finally {
			
			ThreadLocalAppUser.remove();
			ThreadLocalAppUser.setAppUserModel(null);
		}					
	
		
		return this.getBackingEntity();
	}
	
	
	/**
	 * @throws Exception 
	 * 
	 */
	@PostConstruct
	public void init() throws Exception {
		
		
		HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		
		if(httpServletRequest.getParameter(CommandFieldConstants.KEY_PRODUCT_ID) != null) {

			this.getBackingEntity().setProductId(Long.valueOf(httpServletRequest.getParameter(CommandFieldConstants.KEY_PRODUCT_ID)));
		}				
		
		try {

			AppUserModel currentUser = UserUtils.getCurrentUser();	
			
			String responseXml = (String) httpServletRequest.getAttribute(CommandFieldConstants.KEY_TX_CODE);		
			
			if(!StringUtil.isNullOrEmpty(responseXml)) {

				this.populateBackingEntityResponse(responseXml);
			}
			
		} catch (Exception e) {
			this.addErrorMessage(null, e.getLocalizedMessage());
			e.printStackTrace();
		}
	}	
	
	
	/**
	 * @param responseXml
	 */
	private String getError(String responseXml) throws XPathExpressionException {

		logger.info("Error XML\n"+responseXml);
		
		String errors = MiniXMLUtil.getTagTextValue(responseXml, "/msg/errors/error/text()");
		
		return errors;
	}
	
	
	public static void addErrorMessage(String controlID, String summary) {

		Severity severity = FacesMessage.SEVERITY_ERROR;
		
		FacesMessage errorMessage = new FacesMessage();
		errorMessage.setDetail(summary);
		errorMessage.setSummary(summary);
		errorMessage.setSeverity(severity);

		FacesContext.getCurrentInstance().addMessage(null, errorMessage);
//		throw new ValidatorException(errorMessage);
	}	
	
	
	public TellerTransactionsBackingBean getBackingEntity() {
		
		if(this.getTellerTransactionsBackingBean() == null) {
			setTellerTransactionsBackingBean(new TellerTransactionsBackingBean());
		}
		
		return getTellerTransactionsBackingBean();
	} 
	
	
	public TellerTransactionsBackingBean getTellerTransactionsBackingBean() {
		return tellerTransactionsBackingBean;
	}

	public void setTellerTransactionsBackingBean(TellerTransactionsBackingBean tellerTransactionsBackingBean) {
		this.tellerTransactionsBackingBean = tellerTransactionsBackingBean;
	}	
	
	public void setAppUserDAO(AppUserDAO appUserDAO) {
		this.appUserDAO = appUserDAO;
	}	

	public void setCommonCommandManager(CommonCommandManager commonCommandManager) {
		this.commonCommandManager = commonCommandManager;
	}

	public TellerTransactionsManagedBean() {
		tellerTransactionsBackingBean = this.getBackingEntity();
	}
	public void setMfsWebManager(MfsWebManager mfsWebManager) {
		this.mfsWebManager = mfsWebManager;
	}
}