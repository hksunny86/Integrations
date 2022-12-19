package com.inov8.microbank.webapp.action.allpayweb;

import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.mfsweb.MfsWebManager;
import com.inov8.microbank.mfsweb.MfsWebResponseDataPopulator;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static com.inov8.microbank.webapp.action.allpayweb.AllPayWebResponseDataPopulator.isTokenValid;

public class ProductPurchaseFormController extends AdvanceFormController {
	
	private static final Logger logger = Logger.getLogger(ProductPurchaseFormController.class);
	private static final String PRODUCT_NAME = "Product";
	private static final String DEVICE_F_ID = "dfid";
	private static final String PRODUCT_ID = "PID";
	
	private MfsWebManager mfsWebController;
	private MfsWebResponseDataPopulator mfsWebResponseDataPopulator;
	private MfsAccountManager mfsAccountManager;
	private AllPayWebResponseDataPopulator allPayWebResponseDataPopulator;
	
	
	public ProductPurchaseFormController (){
		setCommandName("object");
	    setCommandClass(Object.class);
	}
	@Override
	protected Object loadFormBackingObject(HttpServletRequest request) throws Exception {
		AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(request);
		AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);
		
		return new Object();
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest request) throws Exception {
		AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(request);
		requestWrapper.addParameter(PRODUCT_NAME,"Cash In");
		AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);
		return null;
	}
	
	@Override
	protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse arg1, Object model, BindException arg3) throws Exception
	{

		AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(request);
		AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);
		
		if(!isTokenValid(request)) {			
			return new ModelAndView(AllPayWebConstant.GENERIC_PAGE.getValue());
		}
		

		String mobileNumber = requestWrapper.getParameter(CommandFieldConstants.KEY_CUSTOMER_MOBILE).trim();
		String PNAME = ProductConstantsInterface.CASH_DEPOSIT_NAME;//Encryption.decrypt(requestWrapper.getParameter(PRODUCT_NAME));
		String GFID = DeviceFlowConstants.AGENT_WEB_CASH_IN.toString();//Encryption.decrypt(requestWrapper.getParameter(DEVICE_F_ID));
		String PID = ProductConstantsInterface.CASH_DEPOSIT.toString(); //Encryption.decrypt(requestWrapper.getParameter(PRODUCT_ID));

		requestWrapper.addParameter(PRODUCT_NAME, PNAME);
		requestWrapper.addParameter(DEVICE_F_ID, GFID);
		requestWrapper.addParameter(PRODUCT_ID, PID);

		logger.info("[ProductPurchaseFormController.onCreate] Validating Customer. Mobile No:" + mobileNumber + " Product ID:" + PID);
		
		Boolean isValidCustomer = allPayWebResponseDataPopulator.isValidCustomer(requestWrapper, mobileNumber);
		
		if(!isValidCustomer) {
									
			return new ModelAndView( getFormView() );
		}
		
		String UID = requestWrapper.getParameter(CommandFieldConstants.KEY_U_ID);
		AppUserModel appUserModel = allPayWebResponseDataPopulator.getAppUserModel(UID);
		ThreadLocalAppUser.setAppUserModel(appUserModel);
		
		String responseXML = mfsWebController.handleRequest(requestWrapper, CommandFieldConstants.CASH_DEPOSIT_INFO_COMMAND);

		this.mfsWebResponseDataPopulator.populateTransactionSummary(requestWrapper, responseXML);
		
		if(MfsWebUtil.isErrorXML(responseXML)) {
			
			this.mfsWebResponseDataPopulator.populateErrorMessages(requestWrapper, responseXML);
			
			requestWrapper.addParameter(PRODUCT_NAME, Encryption.encrypt(PNAME));
			requestWrapper.addParameter(DEVICE_F_ID, Encryption.encrypt(GFID));
			requestWrapper.addParameter(PRODUCT_ID, Encryption.encrypt(PID));
			
			return new ModelAndView(getFormView());
		}
		
		this.mfsWebResponseDataPopulator.populateProductPurchase(requestWrapper, responseXML);
		
		ValidationErrors vErrors = new ValidationErrors();
		String txAmount = requestWrapper.getParameter(CommandFieldConstants.KEY_TXAM);
		
//		String mobNum = requestWrapper.getParameter("MOBN");		
//		String dfid = requestWrapper.getParameter("dfid");
		
		if(null != txAmount && !"".equalsIgnoreCase(txAmount))
		{
			vErrors = ValidatorWrapper.doInteger(txAmount, vErrors, "Amount");
			
				try
				{
					Long amount = new Long(txAmount).longValue();
					
					if( GFID.equalsIgnoreCase("3") ) // Case of INDIGO
					{
						if( amount < 10 || amount > 999999 )
						{						
							request.setAttribute("errors", "Amount should be between 10 to 999999");
							throw new NumberFormatException();
						}
					}	
					else if( GFID.equalsIgnoreCase("5") ) // Case of JAZZ
					{
						if( amount < 20 || amount > 1000 )
						{	
							requestWrapper.setAttribute("errors", "Amount should be between 20 to 1000");
							throw new NumberFormatException();
						}
					}
					else if( GFID.equalsIgnoreCase("13") ) // Case of Zong Postpaid
					{
						if( amount < 10 || amount > 3000 )
						{	
							requestWrapper.setAttribute("errors", "Amount should be between 10 to 3000");
							throw new NumberFormatException();
						}
					}
					
					else if( GFID.equalsIgnoreCase("7") || GFID.equalsIgnoreCase("12") ) // Case of UFONE and ZONG
					{
						if( amount < 10 || amount > 1000 )
						{	
							requestWrapper.setAttribute("errors", "Amount should be between 10 to 1000");
							throw new NumberFormatException();
						}
					}
					
				}
				catch(NumberFormatException nfe)
				{
					
					return super.showForm(requestWrapper, arg1, arg3);					
				}
		}					
		if( mobileNumber == null || mobileNumber.equals("") )
		{						
				requestWrapper.setAttribute("errors", "Mobile number is not provided.");				
				return super.showForm(requestWrapper, arg1, arg3);
			}
			
		vErrors = ValidatorWrapper.doRequired(txAmount, vErrors, "Amount");
		vErrors = ValidatorWrapper.doInteger(txAmount, vErrors, "Amount");
		vErrors = ValidatorWrapper.doValidateMobileNo(mobileNumber, vErrors, "Mobile Number");
//		vErrors = ValidatorWrapper.doRange("10", "1000", txAmount, "Amount", vErrors);
		vErrors = ValidatorWrapper.checkLength(mobileNumber, 11, vErrors, "Mobile Number");
		
		if(vErrors.hasValidationErrors())
		{
			requestWrapper.setAttribute("errors", vErrors.getErrors());			
			return super.showForm(requestWrapper, arg1, arg3);
		}
		requestWrapper.setAttribute(CommandFieldConstants.KEY_TXAM, txAmount);
		requestWrapper.setAttribute(CommandFieldConstants.KEY_MOB_NO, mobileNumber);
		requestWrapper.setAttribute("isValid","0");
		
		return new ModelAndView(this.getSuccessView());
		
	}	
	@Override
	protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object model, BindException exception) throws Exception {
		return onCreate(request, response, model, exception);
	}

	public void setMfsWebController(MfsWebManager mfsWebController) {
		this.mfsWebController = mfsWebController;
	}
	public void setMfsWebResponseDataPopulator(MfsWebResponseDataPopulator mfsWebResponseDataPopulator) {
		this.mfsWebResponseDataPopulator = mfsWebResponseDataPopulator;
	}
	public void setMfsAccountManager(MfsAccountManager mfsAccountManager) {
		this.mfsAccountManager = mfsAccountManager;
	}
	public void setAllPayWebResponseDataPopulator(AllPayWebResponseDataPopulator allPayWebResponseDataPopulator) {
		this.allPayWebResponseDataPopulator = allPayWebResponseDataPopulator;
	}

}
