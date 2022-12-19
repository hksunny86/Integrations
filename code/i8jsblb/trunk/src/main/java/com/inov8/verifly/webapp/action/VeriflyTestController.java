/**
 * 
 */
package com.inov8.verifly.webapp.action;

import java.security.KeyPair;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import sun.misc.BASE64Decoder;

import com.inov8.verifly.common.constants.ConfigurationConstants;
import com.inov8.verifly.common.constants.VeriflyConstants;
import com.inov8.verifly.common.encryption.AESEncryption;
import com.inov8.verifly.common.encryption.Encryption;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.model.LogModel;
import com.inov8.verifly.common.model.logmodule.LogListViewModel;
import com.inov8.verifly.common.util.ConfigurationContainer;
import com.inov8.verifly.common.util.TestVeriflyFacadeSingleton;
import com.inov8.verifly.common.util.VeriflyUtility;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl;
import com.inov8.verifly.common.wrapper.logmodule.LogWrapper;
import com.inov8.verifly.common.wrapper.logmodule.LogWrapperImpl;
import com.inov8.verifly.server.dao.mainmodule.VeriflyConfigurationDAO;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;

/**
 * @author asif.rahim
 *
 */
public class VeriflyTestController implements Controller {

	
	VeriflyConfigurationDAO veriflyConfigurationDao;
	ConfigurationContainer keysObj;
	
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		ModelAndView mv = null;
////////////////////////////////////////////////////////////////
		
		System.out.println("this is VeriflyTestController");
		
		   String veriflyPath = "http://"+request.getServerName() +":"+ request.getServerPort() + VeriflyConstants.VERIFLY_URL;

		   int requestId = request.getParameter("requestId")==null?0:Integer.parseInt(request.getParameter("requestId").toString());

		   VeriflyBaseWrapper baseWrapper = null;
		   AccountInfoModel accountInfoModel = null;
		   LogModel logModel = null;
		   XStream xstream = new XStream(new PureJavaReflectionProvider());
		   Encryption encryption = (Encryption)keysObj.getEncrptionClassObject();
		//   encryption.setKeysObj(keysObj);// set the keys object
		   /*String keyPairValue = keysObj.getValue (ConfigurationConstants.KEY_PAIR);
		   KeyPair keypair = (KeyPair) xstream.fromXML(keyPairValue);*/

	        /*********************************************************************************
			 * Updated by Soofia Faruq AES Encryption Support
			 */
			KeyPair keypair = null;
			SecretKey aesKey = null;
			if (encryption instanceof AESEncryption) {
				String strKey = keysObj.getValue(ConfigurationConstants.AES_KEY);
//				aesKey = new SecretKeySpec(strKey.getBytes(), 0,
//						strKey.getBytes().length, "AES");
				BASE64Decoder decoder = new BASE64Decoder();
				byte[] encodedKey = decoder.decodeBuffer(strKey);
		        aesKey = new SecretKeySpec(encodedKey,0,encodedKey.length, "AES"); 
			} else {
				String keyPairValue = keysObj
						.getValue(ConfigurationConstants.KEY_PAIR);
				keypair = (KeyPair) xstream.fromXML(keyPairValue);
			}
			/**********************************************************************************/
		   
		   switch(requestId){
		     // generate pin
		     case 1:
		     baseWrapper = new VeriflyBaseWrapperImpl();
		     accountInfoModel = new AccountInfoModel();
		     logModel = new LogModel();

		     // setting values into object from ui fields
		     if(request.getParameter("customerId") != null && request.getParameter("customerId").trim().length() > 0 ){
		       accountInfoModel.setCustomerId(new Long(VeriflyUtility.toDefaultNum(request.getParameter("customerId"))));
		     }

		     if(request.getParameter("accountNick") != null){
		       accountInfoModel.setAccountNick(VeriflyUtility.toDefault(request.getParameter("accountNick")));
		     }

		     if(request.getParameter("accountNo") != null){
		       accountInfoModel.setAccountNo(VeriflyUtility.toDefault(request.getParameter("accountNo")));
		     }
		     
		     if(request.getParameter("accountTypeId") != null){
		    	 accountInfoModel.setAccountTypeId(new Long(request.getParameter("accountTypeId")));
			     }
		     
		     if(request.getParameter("currencyCodeId") != null){
		    	 accountInfoModel.setCurrencyCodeId(new Long(request.getParameter("currencyCodeId")));			       
			     }

		     if(request.getParameter("cardNo") != null ){
		       accountInfoModel.setCardNo(VeriflyUtility.toDefault(request.getParameter("cardNo")));
		     }

		     if(request.getParameter("expiryDate") != null){
		       accountInfoModel.setCardExpiryDate(request.getParameter("expiryDate"));
		     }

		     if(request.getParameter("customerFirstName") != null){
		       accountInfoModel.setFirstName(VeriflyUtility.toDefault(request.getParameter("customerFirstName")));
		     }

		     if(request.getParameter("customerLastName") != null){
		       accountInfoModel.setLastName(VeriflyUtility.toDefault(request.getParameter("customerLastName")));
		     }

		     if(request.getParameter("mobileNo") != null){
		       accountInfoModel.setCustomerMobileNo(VeriflyUtility.toDefault(request.getParameter("mobileNo")));
		     }

		     if(request.getParameter("cardTypeId") != null && request.getParameter("cardTypeId").trim().length() > 0 ){
		       accountInfoModel.setCardTypeId(new Long(VeriflyUtility.toDefaultNum(request.getParameter("cardTypeId"))));
		     }

		     if(request.getParameter("paymentModeId") != null && request.getParameter("paymentModeId").trim().length() > 0 ){
		       accountInfoModel.setPaymentModeId(new Long(VeriflyUtility.toDefaultNum(request.getParameter("paymentModeId"))));
		     }

		     //Inserting non Mandatory fields.
		     if(request.getParameter("comments") != null){
		       accountInfoModel.setComments(VeriflyUtility.toDefault(request.getParameter("comments")));
		     }

		     if(request.getParameter("description") != null){
		       accountInfoModel.setDescription(VeriflyUtility.toDefault(request.getParameter("description")));
		     }

		     if(request.getParameter("createdBy") != null){
		       logModel.setCreatedBy(VeriflyUtility.toDefault(request.getParameter("createdBy")));
		     }

		     if(request.getParameter("createdByUserId") != null && request.getParameter("createdByUserId").trim().length() > 0 ){
		       logModel.setCreatdByUserId(new Long(VeriflyUtility.toDefaultNum(request.getParameter("createdByUserId"))));
		     }

		     //setting model into wrapper
		     baseWrapper.setAccountInfoModel(accountInfoModel);
		     baseWrapper.setLogModel(logModel);
		     try {
		            baseWrapper =
		                    TestVeriflyFacadeSingleton.getInstance(veriflyPath).generatePIN(baseWrapper);
		        } catch (Exception ex) {
		            ex.printStackTrace();
		        }


		     request.setAttribute("baseWrapper",baseWrapper);
		     request.setAttribute("requestId",request.getParameter("requestId"));
		     mv = new ModelAndView("Result");
		     break;

		     // Verify Pin
		     case 2:
		       logModel = new LogModel();
		       baseWrapper = new VeriflyBaseWrapperImpl();
		       accountInfoModel = new AccountInfoModel();

		        // populate accountInfo bean with values entered by user

		        if(request.getParameter("verifyPin_CustomerId") != null && request.getParameter("verifyPin_CustomerId").trim().length() > 0 ){
		          accountInfoModel.setCustomerId(new Long(VeriflyUtility.toDefaultNum(request.getParameter("verifyPin_CustomerId"))));
		        }

		        if(request.getParameter("verifyPin_AccountNick") != null){
		          accountInfoModel.setAccountNick(VeriflyUtility.toDefault(request.getParameter("verifyPin_AccountNick")));
		        }

		        if(request.getParameter("verifyPin_EncryptedPin") != null)
		        {
//		          accountInfoModel.setOldPin(rsaEncrypter.doEncrypt(VeriflyUtility.toDefault(request.getParameter("verifyPin_EncryptedPin"))));
		        	String encryptPin = null;
		    		/*********************************************************************************
		    		 * Updated by Soofia Faruq
		    		 */
		    		if (keypair != null) 
		    		{
		    			encryptPin = encryption.encrypt(keypair.getPrivate(), VeriflyUtility.toDefault(request.getParameter("verifyPin_EncryptedPin")));
		    		} 
		    		else 
		    		{
		    			encryptPin = encryption.encrypt(aesKey, VeriflyUtility.toDefault(request.getParameter("verifyPin_EncryptedPin")));
		    		}
		    	
		    		/**********************************************************************************/
		        	
		        	
		          accountInfoModel.setOldPin(encryptPin);
		        }

		        //populate logModel bean with transactionCodeId
		        if(request.getParameter("verifyPin_transactionCode") != null && request.getParameter("verifyPin_transactionCode").trim().length() > 0  ){
		          logModel.setTransactionCodeId(new Long(VeriflyUtility.toDefaultNum(request.getParameter("verifyPin_transactionCode"))));
		        }

		        if(request.getParameter("verifyPin_createdBy") != null){
		          logModel.setCreatedBy(VeriflyUtility.toDefault(request.getParameter("verifyPin_createdBy")));
		        }

		        if(request.getParameter("verifyPin_createdByUserId") != null && request.getParameter("verifyPin_createdByUserId").trim().length() > 0 ){
		          logModel.setCreatdByUserId(new Long(VeriflyUtility.toDefaultNum(request.getParameter("verifyPin_createdByUserId"))));
		        }

		        // creating wrapper object and populate models in
		        baseWrapper = new VeriflyBaseWrapperImpl();
		        baseWrapper.setAccountInfoModel(accountInfoModel);
		        baseWrapper.setLogModel(logModel);

		        try {
		            baseWrapper =
		                    TestVeriflyFacadeSingleton.getInstance(veriflyPath).verifyPIN(baseWrapper);

		        } catch (Exception ex) {
		            ex.printStackTrace();
		        }

		     request.setAttribute("baseWrapper",baseWrapper);
		     request.setAttribute("requestId",request.getParameter("requestId"));
		     mv = new ModelAndView("Result");
		     break;

		    //change pin
		     case 3:
		        // initialize necessary objects
		        accountInfoModel = new AccountInfoModel();
		        baseWrapper = new VeriflyBaseWrapperImpl();
		        logModel = new LogModel();

		        if(request.getParameter("changePin_CustomerId") != null && request.getParameter("changePin_CustomerId").trim().length() > 0 ){
		          accountInfoModel.setCustomerId(new Long(VeriflyUtility.toDefaultNum(request.getParameter("changePin_CustomerId"))));
		        }

		        if(request.getParameter("changePin_AccountNick") != null){
		          accountInfoModel.setAccountNick(VeriflyUtility.toDefault(request.getParameter("changePin_AccountNick")));
		        }

		        if(request.getParameter("oldPin") != null){
//		          accountInfoModel.setOldPin(rsaEncrypter.doEncrypt(VeriflyUtility.toDefault(request.getParameter("oldPin"))));
		        	String encryptPin = null;
		    		/*********************************************************************************
		    		 * Updated by Soofia Faruq
		    		 */
		    		if (keypair != null) 
		    		{
		    			encryptPin = encryption.encrypt(keypair.getPrivate(), VeriflyUtility.toDefault(request.getParameter("oldPin")));
		    		} 
		    		else 
		    		{
		    			encryptPin = encryption.encrypt(aesKey, VeriflyUtility.toDefault(request.getParameter("oldPin")));
		    		}
		    	
		    		/**********************************************************************************/
		          accountInfoModel.setOldPin(encryptPin);
		        }

		        if(request.getParameter("newPin") != null){
		         // accountInfoModel.setNewPin(rsaEncrypter.doEncrypt(VeriflyUtility.toDefault(request.getParameter("newPin"))));
		        	String encryptPin = null;
		    		/*********************************************************************************
		    		 * Updated by Soofia Faruq
		    		 */
		    		if (keypair != null) 
		    		{
		    			encryptPin = encryption.encrypt(keypair.getPrivate(), VeriflyUtility.toDefault(request.getParameter("newPin")));
		    		} 
		    		else 
		    		{
		    			encryptPin = encryption.encrypt(aesKey, VeriflyUtility.toDefault(request.getParameter("newPin")));
		    		}
		    	
		    		/**********************************************************************************/
		          accountInfoModel.setNewPin(encryptPin);
		        	
		        	
		          //accountInfoModel.setNewPin(encryption.encrypt(keypair.getPublic(),VeriflyUtility.toDefault(request.getParameter("newPin"))));
		        }

		        if(request.getParameter("confirmNewPin") != null){
		        	String encryptPin = null;
		    		/*********************************************************************************
		    		 * Updated by Soofia Faruq
		    		 */
		    		if (keypair != null) 
		    		{
		    			encryptPin = encryption.encrypt(keypair.getPrivate(), VeriflyUtility.toDefault(request.getParameter("confirmNewPin")));
		    		} 
		    		else 
		    		{
		    			encryptPin = encryption.encrypt(aesKey, VeriflyUtility.toDefault(request.getParameter("confirmNewPin")));
		    		}
		    	
		    		/**********************************************************************************/
		    		accountInfoModel.setConfirmNewPin(encryptPin);
		          //accountInfoModel.setConfirmNewPin(encryption.encrypt(keypair.getPublic(),VeriflyUtility.toDefault(request.getParameter("confirmNewPin"))));
		          
		        }

		        if(request.getParameter("changePin_createdBy") != null){
		          logModel.setCreatedBy(VeriflyUtility.toDefault(request.getParameter("changePin_createdBy")));
		        }

		        if(request.getParameter("changePin_createdByUserId") != null && request.getParameter("changePin_createdByUserId").trim().length() > 0 ){
		          logModel.setCreatdByUserId(new Long(VeriflyUtility.toDefaultNum(request.getParameter("changePin_createdByUserId"))));
		        }

		        //create wrapper object and populate with accountinfo & LogModel for send to server
		        baseWrapper.setAccountInfoModel(accountInfoModel);
		        baseWrapper.setLogModel(logModel);
		        try {
		            baseWrapper =
		                    TestVeriflyFacadeSingleton.getInstance(veriflyPath).changePIN(baseWrapper);
		        } catch (Exception ex) {
		            ex.printStackTrace();
		        }
		     request.setAttribute("baseWrapper",baseWrapper);
		     request.setAttribute("requestId",request.getParameter("requestId"));
		     mv = new ModelAndView("Result");
		     break;

		     //reset pin
		     case 4:
		     //initalize necessary objects
		     accountInfoModel = new AccountInfoModel();
		     baseWrapper = new VeriflyBaseWrapperImpl();
		     logModel = new LogModel();

		        if(request.getParameter("resetPin_CustomerId") != null && request.getParameter("resetPin_CustomerId").trim().length() > 0 ){
		          accountInfoModel.setCustomerId(new Long(VeriflyUtility.toDefaultNum(request.getParameter("resetPin_CustomerId"))));
		        }

		        if(request.getParameter("resetPin_AccountNick") != null){
		          accountInfoModel.setAccountNick(VeriflyUtility.toDefault(request.getParameter("resetPin_AccountNick")));
		        }

		        if(request.getParameter("resetPin_createdBy") != null){
		          logModel.setCreatedBy(VeriflyUtility.toDefault(request.getParameter("resetPin_createdBy")));
		        }

		        if(request.getParameter("resetPin_createdByUserId") != null && request.getParameter("resetPin_createdByUserId").trim().length() > 0 ){
		          logModel.setCreatdByUserId(new Long(VeriflyUtility.toDefaultNum(request.getParameter("resetPin_createdByUserId"))));
		        }

		        //create wrapper object and populate with AccountInfoModel
		        baseWrapper.setAccountInfoModel(accountInfoModel);
		        baseWrapper.setLogModel(logModel);
		        try {
		            baseWrapper =
		                    TestVeriflyFacadeSingleton.getInstance(veriflyPath).resetPIN(baseWrapper);
		        } catch (Exception ex) {
		            ex.printStackTrace();
		        }
		     request.setAttribute("baseWrapper",baseWrapper);
		     request.setAttribute("requestId",request.getParameter("requestId"));
		     mv = new ModelAndView("Result");
		     break;

		     //view Transaction Log
		     case 5:
		     //initlaize required objects
		     LogListViewModel logListViewModel = new LogListViewModel();
		     LogWrapper logWrapper = new LogWrapperImpl();
		     logModel = new LogModel();

		        // create LogListViewModel object and populate with data
		        if(request.getParameter("viewTransactionLog_TransactionCode")!= null && request.getParameter("viewTransactionLog_TransactionCode").trim().length() > 0 ){
		            logListViewModel.setTransactionCodeId(new Long(request.getParameter("viewTransactionLog_TransactionCode")));
		          }
		        if(request.getParameter("viewTransactionLog_Action") != null && request.getParameter("viewTransactionLog_Action").trim().length() > 0 ){
		            logListViewModel.setActionId(new Long(request.getParameter("viewTransactionLog_Action")));
		        }
		        if(request.getParameter("viewTransactionLog_AccountInfo") != null && request.getParameter("viewTransactionLog_AccountInfo").trim().length() > 0 ){
		          logListViewModel.setAccountInfoId(new Long(request.getParameter("viewTransactionLog_AccountInfo")));
		        }
		        if(request.getParameter("viewTransactionLog_FailureReason") != null && request.getParameter("viewTransactionLog_FailureReason").trim().length() > 0 ){
		            logListViewModel.setFailureReasonId(new Long(request.getParameter("viewTransactionLog_FailureReason")));
		        }
		        if(request.getParameter("viewTransactionLog_Status") != null && request.getParameter("viewTransactionLog_Status").trim().length() > 0 ){
		            logListViewModel.setStatusId(new Integer(VeriflyUtility.toDefaultNum(request.getParameter("viewTransactionLog_Status"))));
		        }

		        if(request.getParameter("viewTransactionLog_createdBy") != null){
		          logModel.setCreatedBy(VeriflyUtility.toDefault(request.getParameter("viewTransactionLog_createdBy")));
		        }

		        if(request.getParameter("viewTransactionLog_createdByUserId") != null && request.getParameter("viewTransactionLog_createdByUserId").trim().length() > 0 ){
		          logModel.setCreatdByUserId(new Long(VeriflyUtility.toDefaultNum(request.getParameter("viewTransactionLog_createdByUserId"))));
		        }

		        //create logwrapper object and populate with logListViewModel & logModel
		        logWrapper.setLogListViewModel(logListViewModel);
		        logWrapper.setLogModel(logModel);
		        try {
		            logWrapper =
		                    TestVeriflyFacadeSingleton.getInstance(veriflyPath).getLog(logWrapper);
		        } catch (Exception ex) {
		            ex.printStackTrace();
		        }
		     request.setAttribute("logWrapper",logWrapper);
		     request.setAttribute("requestId",request.getParameter("requestId"));
		     mv = new ModelAndView("Result");
		     break;

		     //Deactivate pin
		     case 6:
		     //initialize required Objects
		     accountInfoModel = new AccountInfoModel();
		     baseWrapper = new VeriflyBaseWrapperImpl() ;
		     logModel = new LogModel();

		    if(request.getParameter("deActivatePin_CustomerId") != null && request.getParameter("deActivatePin_CustomerId").trim().length() > 0 ){
		      accountInfoModel.setCustomerId(new Long(VeriflyUtility.toDefaultNum(request.getParameter("deActivatePin_CustomerId"))));
		    }

		    if(request.getParameter("deActivatePin_AccountNick") != null){
		      accountInfoModel.setAccountNick(VeriflyUtility.toDefault(request.getParameter("deActivatePin_AccountNick")));
		    }

		    if(request.getParameter("deActivatePin_createdBy") != null){
		      logModel.setCreatedBy(VeriflyUtility.toDefault(request.getParameter("deActivatePin_createdBy")));
		    }

		    if(request.getParameter("deActivatePin_createdByUserId") != null && request.getParameter("deActivatePin_createdByUserId").trim().length() > 0 ){
		      logModel.setCreatdByUserId(new Long(VeriflyUtility.toDefaultNum(request.getParameter("deActivatePin_createdByUserId"))));
		    }

		    baseWrapper.setAccountInfoModel(accountInfoModel);
		    baseWrapper.setLogModel(logModel);
		    try
		    {
		      baseWrapper = TestVeriflyFacadeSingleton.getInstance(veriflyPath).deactivatePIN(baseWrapper) ;
		    }
		    catch (Exception ex )
		    {
		      ex.printStackTrace() ;
		    }
		     request.setAttribute("baseWrapper",baseWrapper);
		     request.setAttribute("requestId",request.getParameter("requestId"));
		     mv = new ModelAndView("Result");
		     break;

		     //activate Pin
		     case 7:
		     //initialize required objects
		     accountInfoModel = new AccountInfoModel();
		     baseWrapper = new VeriflyBaseWrapperImpl() ;
		     logModel = new LogModel();

		     if(request.getParameter("activatePin_CustomerId") != null && request.getParameter("activatePin_CustomerId").trim().length() > 0 ){
		       accountInfoModel.setCustomerId(new Long(VeriflyUtility.toDefaultNum(request.getParameter("activatePin_CustomerId"))));
		     }

		     if(request.getParameter("activatePin_AccountNick") != null){
		       accountInfoModel.setAccountNick(VeriflyUtility.toDefault(request.getParameter("activatePin_AccountNick")));
		     }

		    if(request.getParameter("activatePin_createdBy") != null){
		      logModel.setCreatedBy(VeriflyUtility.toDefault(request.getParameter("activatePin_createdBy")));
		    }

		    if(request.getParameter("activatePin_createdByUserId") != null && request.getParameter("activatePin_createdByUserId").trim().length() > 0 ){
		      logModel.setCreatdByUserId(new Long(VeriflyUtility.toDefaultNum(request.getParameter("activatePin_createdByUserId"))));
		    }

		     // create wrapper object and populate with accountInfo & logmodel objects
		     baseWrapper.setAccountInfoModel(accountInfoModel);
		     baseWrapper.setLogModel(logModel);
		     try
		     {
		       baseWrapper = TestVeriflyFacadeSingleton.getInstance(veriflyPath).activatePIN(baseWrapper) ;
		     }
		     catch (Exception ex )
		     {
		       ex.printStackTrace() ;
		     }
		     request.setAttribute("baseWrapper",baseWrapper);
		     request.setAttribute("requestId",request.getParameter("requestId"));
		     mv = new ModelAndView("Result");
		     break;

		     //Modify Bank Account
		     case 8:
		     //initialize required objects
		     accountInfoModel = new AccountInfoModel();
		     baseWrapper = new VeriflyBaseWrapperImpl();
		     logModel = new LogModel();

		     if(request.getParameter("modifyBankAccount_CustomerId") != null && request.getParameter("modifyBankAccount_CustomerId").trim().length() > 0 ){
		       accountInfoModel.setCustomerId(new Long(VeriflyUtility.toDefaultNum(request.getParameter("modifyBankAccount_CustomerId"))));
		     }

		     if(request.getParameter("modifyBankAccount_AccountNick") != null){
		       accountInfoModel.setAccountNick(VeriflyUtility.toDefault(request.getParameter("modifyBankAccount_AccountNick")));
		     }

		     if(request.getParameter("modifyBankAccount_AccountNo") != null){
		       accountInfoModel.setAccountNo(VeriflyUtility.toDefault(request.getParameter("modifyBankAccount_AccountNo")));
		     }

		     if(request.getParameter("modifyBankAccount_CardNo") != null){
		       accountInfoModel.setCardNo(VeriflyUtility.toDefault(request.getParameter("modifyBankAccount_CardNo")));
		     }

		     if(request.getParameter("modifyBankAccount_expiryDate") != null){
		       accountInfoModel.setCardExpiryDate(request.getParameter("modifyBankAccount_expiryDate"));
		     }

		     if(request.getParameter("modifyBankAccount_customerFirstName") != null){
		       accountInfoModel.setFirstName(VeriflyUtility.toDefault(request.getParameter("modifyBankAccount_customerFirstName")));
		     }

		     if(request.getParameter("modifyBankAccount_customerLastName") != null){
		       accountInfoModel.setLastName(VeriflyUtility.toDefault(request.getParameter("modifyBankAccount_customerLastName")));
		     }

		     if(request.getParameter("modifyBankAccount_mobileNo") != null){
		       accountInfoModel.setCustomerMobileNo(VeriflyUtility.toDefault(request.getParameter("modifyBankAccount_mobileNo")));
		     }

		     if(request.getParameter("modifyBankAccount_cardTypeId") != null && request.getParameter("modifyBankAccount_cardTypeId").trim().length() > 0 ){
		       accountInfoModel.setCardTypeId(new Long(VeriflyUtility.toDefaultNum(request.getParameter("modifyBankAccount_cardTypeId"))));
		     }

		     if(request.getParameter("modifyBankAccount_paymentModeId") != null && request.getParameter("modifyBankAccount_paymentModeId").trim().length() > 0 ){
		       accountInfoModel.setPaymentModeId(new Long(VeriflyUtility.toDefaultNum(request.getParameter("modifyBankAccount_paymentModeId"))));
		     }

		     if(request.getParameter("modifyBankAccount_comments") != null){
		       accountInfoModel.setComments(VeriflyUtility.toDefault(request.getParameter("modifyBankAccount_comments")));
		     }

		     if(request.getParameter("modifyBankAccount_description") != null){
		       accountInfoModel.setDescription(VeriflyUtility.toDefault(request.getParameter("modifyBankAccount_description")));
		     }

		    if(request.getParameter("modifyBankAccount_createdBy") != null){
		      logModel.setCreatedBy(VeriflyUtility.toDefault(request.getParameter("modifyBankAccount_createdBy")));
		    }

		    if(request.getParameter("modifyBankAccount_createdByUserId") != null && request.getParameter("modifyBankAccount_createdByUserId").trim().length() > 0 ){
		      logModel.setCreatdByUserId(new Long(VeriflyUtility.toDefaultNum(request.getParameter("modifyBankAccount_createdByUserId"))));
		    }

		     //create wrapper object and populate with accountInfoModel
		     baseWrapper.setAccountInfoModel(accountInfoModel);
		     baseWrapper.setLogModel(logModel);
		     try {
		       baseWrapper =
		       TestVeriflyFacadeSingleton.getInstance(veriflyPath).modifyAccountInfo(baseWrapper);
		     } catch (Exception ex) {
		       ex.printStackTrace();
		     }
		     request.setAttribute("baseWrapper",baseWrapper);
		     request.setAttribute("requestId",request.getParameter("requestId"));
		     mv = new ModelAndView("Result");
		     break;

		     //delete Bank Account
		     case 9:
		     //initialize required objects
		     accountInfoModel = new AccountInfoModel();
		     baseWrapper = new VeriflyBaseWrapperImpl();

		     //create accountInfoModel and populate with ui values

		     if(request.getParameter("deleteBankAccount_CustomerId") != null && request.getParameter("deleteBankAccount_CustomerId").trim().length() > 0 ){
		       accountInfoModel.setCustomerId(new Long(VeriflyUtility.toDefaultNum(request.getParameter("deleteBankAccount_CustomerId"))));
		     }

		     if(request.getParameter("deleteBankAccount_AccountNick") != null){
		       accountInfoModel.setAccountNick(VeriflyUtility.toDefault(request.getParameter("deleteBankAccount_AccountNick")));
		     }

		     //create wrapper object and populate with accountInfoObject
		     baseWrapper.setAccountInfoModel(accountInfoModel);
		     try {
		       baseWrapper =
		       TestVeriflyFacadeSingleton.getInstance(veriflyPath).deleteAccount(baseWrapper);
		     } catch (Exception ex) {
		       ex.printStackTrace();
		     }
		     request.setAttribute("baseWrapper",baseWrapper);
		     request.setAttribute("requestId",request.getParameter("requestId"));
		     mv = new ModelAndView("Result");
		     break;

		     //encrypted pin
		     
		     case 10:
			     //initialize required objects
		    String encryp_pin = request.getParameter("encryp_pin");
			request.setAttribute("encryp_pin",encryp_pin);
			
			String encryptPin = null;
    		/*********************************************************************************
    		 * Updated by Soofia Faruq
    		 */
    		if (keypair != null) 
    		{
    			encryptPin = encryption.encrypt(keypair.getPrivate(), encryp_pin);
    		} 
    		else 
    		{
    			encryptPin = encryption.encrypt(aesKey, encryp_pin);
    		}
    	
    		/**********************************************************************************/
			
			request.setAttribute("encryp_pinWithEncryption",    encryptPin);
			request.setAttribute("requestId",request.getParameter("requestId"));
			mv = new ModelAndView("Result");

			break;

		     case 11:
			     accountInfoModel = new AccountInfoModel();
			     baseWrapper = new VeriflyBaseWrapperImpl();
			     logModel = new LogModel ();
			     //initialize required objects
			    	 if(request.getParameter("changeAccount_CustomerId") != null && request.getParameter("changeAccount_CustomerId").trim().length() > 0 ){
			    		 accountInfoModel.setCustomerId(new Long(VeriflyUtility.toDefaultNum(request.getParameter("changeAccount_CustomerId"))));
					  }
		 
			    	 if(request.getParameter("changeAccount_AccountNick") != null && request.getParameter("changeAccount_AccountNick").trim().length() > 0 ){
			    		 accountInfoModel.setAccountNick(VeriflyUtility.toDefault(request.getParameter("changeAccount_AccountNick")));
					  }

			    	 if(request.getParameter("changeAccount_NewAccountNick") != null && request.getParameter("changeAccount_NewAccountNick").trim().length() > 0 ){
			    		 accountInfoModel.setNewAccountNick(VeriflyUtility.toDefault(request.getParameter("changeAccount_NewAccountNick")));
					  }


			    	 if(request.getParameter("changeAccount_CreatedBy") != null && request.getParameter("changeAccount_CreatedBy").trim().length() > 0 ){
			    		 logModel.setCreatedBy((VeriflyUtility.toDefault(request.getParameter("changeAccount_CreatedBy"))));
					  }


			    	 if(request.getParameter("changeAccount_CreatedByUserId") != null && request.getParameter("changeAccount_CreatedByUserId").trim().length() > 0 ){
			    		 logModel.setCreatdByUserId(new Long(VeriflyUtility.toDefaultNum(request.getParameter("changeAccount_CreatedByUserId"))));
					  }
			     baseWrapper.setLogModel(logModel);	 
				 baseWrapper.setAccountInfoModel(accountInfoModel);
			     try {
				       baseWrapper =
				       TestVeriflyFacadeSingleton.getInstance(veriflyPath).changeAccountNick(baseWrapper);
				     } catch (Exception ex) {
				       ex.printStackTrace();
				     }
				     request.setAttribute("baseWrapper",baseWrapper);
				     request.setAttribute("requestId",request.getParameter("requestId"));
				     mv = new ModelAndView("Result");

			     break;

		     case 12:
			     accountInfoModel = new AccountInfoModel();
			     baseWrapper = new VeriflyBaseWrapperImpl();
			     logModel = new LogModel();
			     //initialize required objects
			    	 if(request.getParameter("markDel_CustomerId") != null && request.getParameter("markDel_CustomerId").trim().length() > 0 ){
			    		 accountInfoModel.setCustomerId(new Long(VeriflyUtility.toDefaultNum(request.getParameter("markDel_CustomerId"))));
					  }
		 
			    	 if(request.getParameter("markDel_AccountNick") != null && request.getParameter("markDel_AccountNick").trim().length() > 0 ){
			    		 accountInfoModel.setAccountNick(VeriflyUtility.toDefault(request.getParameter("markDel_AccountNick")));
					  }

			    	 if(request.getParameter("markDel_CreatedBy") != null && request.getParameter("markDel_CreatedBy").trim().length() > 0 ){
			    		 logModel.setCreatedBy((VeriflyUtility.toDefault(request.getParameter("markDel_CreatedBy"))));
					  }


			    	 if(request.getParameter("markDel_CreatedByUserId") != null && request.getParameter("markDel_CreatedByUserId").trim().length() > 0 ){
			    		 logModel.setCreatdByUserId(new Long(VeriflyUtility.toDefaultNum(request.getParameter("markDel_CreatedByUserId"))));
					  }


			    	 baseWrapper.setLogModel(logModel);
				 baseWrapper.setAccountInfoModel(accountInfoModel);
			     try {
				       baseWrapper =
				       TestVeriflyFacadeSingleton.getInstance(veriflyPath).markAsDeleted(baseWrapper);
				     } catch (Exception ex) {
				       ex.printStackTrace();
				     }
				     request.setAttribute("baseWrapper",baseWrapper);
				     request.setAttribute("requestId",request.getParameter("requestId"));
				     mv = new ModelAndView("Result");

			     break;


		     case 13:
			     accountInfoModel = new AccountInfoModel();
			     baseWrapper = new VeriflyBaseWrapperImpl();
			     logModel = new LogModel();
			     //initialize required objects
			    	 if(request.getParameter("OT_CustomerId") != null && request.getParameter("OT_CustomerId").trim().length() > 0 ){
			    		 accountInfoModel.setCustomerId(new Long(VeriflyUtility.toDefault(request.getParameter("OT_CustomerId"))));
					  }
		 
			    	 if(request.getParameter("OT_AccountNick") != null && request.getParameter("OT_AccountNick").trim().length() > 0 ){
			    		 accountInfoModel.setAccountNick(VeriflyUtility.toDefault(request.getParameter("OT_AccountNick")));
					  }

			    	 if(request.getParameter("OT_CreatedBy") != null && request.getParameter("OT_CreatedBy").trim().length() > 0 ){
			    		 logModel.setCreatedBy((VeriflyUtility.toDefault(request.getParameter("OT_CreatedBy"))));
					  }


			    	 if(request.getParameter("OT_CreatedByUserId") != null && request.getParameter("OT_CreatedByUserId").trim().length() > 0 ){
			    		 logModel.setCreatdByUserId(new Long(VeriflyUtility.toDefaultNum(request.getParameter("OT_CreatedByUserId"))));
					  }


			    	 baseWrapper.setLogModel(logModel);
				 baseWrapper.setAccountInfoModel(accountInfoModel);
			     try {
				       baseWrapper =
				       TestVeriflyFacadeSingleton.getInstance(veriflyPath).generateOneTimePin(baseWrapper);
				     } catch (Exception ex) {
				       ex.printStackTrace();
				     }
				     request.setAttribute("baseWrapper",baseWrapper);
				     request.setAttribute("requestId",request.getParameter("requestId"));
				     mv = new ModelAndView("Result");

			     break;

		     case 14:
			     accountInfoModel = new AccountInfoModel();
			     baseWrapper = new VeriflyBaseWrapperImpl();
			     logModel = new LogModel();
			     //initialize required objects
			    	 if(request.getParameter("OT_CustomerId") != null && request.getParameter("OT_CustomerId").trim().length() > 0 ){
			    		 accountInfoModel.setCustomerId(new Long(VeriflyUtility.toDefaultNum(request.getParameter("OT_CustomerId"))));
					  }
		 
			    	 if(request.getParameter("OT_AccountNick") != null && request.getParameter("OT_AccountNick").trim().length() > 0 ){
			    		 accountInfoModel.setAccountNick(VeriflyUtility.toDefault(request.getParameter("OT_AccountNick")));
					  }

			    	 if(request.getParameter("OT_CreatedBy") != null && request.getParameter("OT_CreatedBy").trim().length() > 0 ){
			    		 logModel.setCreatedBy((VeriflyUtility.toDefault(request.getParameter("OT_CreatedBy"))));
					  }


			    	 if(request.getParameter("OT_CreatedByUserId") != null && request.getParameter("OT_CreatedByUserId").trim().length() > 0 ){
			    		 logModel.setCreatdByUserId(new Long(VeriflyUtility.toDefaultNum(request.getParameter("OT_CreatedByUserId"))));
					  }
			    	 
			    	 if(request.getParameter("OT_PIN") != null && request.getParameter("OT_PIN").trim().length() > 0 ){
			    		 accountInfoModel.setOtPin(VeriflyUtility.toDefaultNum(request.getParameter("OT_PIN")));
					  }

			    	 


			    	 baseWrapper.setLogModel(logModel);
				 baseWrapper.setAccountInfoModel(accountInfoModel);
			     try {
				       baseWrapper =
				       TestVeriflyFacadeSingleton.getInstance(veriflyPath).verifyOneTimePin(baseWrapper);
				     } catch (Exception ex) {
				       ex.printStackTrace();
				     }
				     request.setAttribute("baseWrapper",baseWrapper);
				     request.setAttribute("requestId",request.getParameter("requestId"));
				     mv = new ModelAndView("Result");

			     break;


			     
		     default:
		    	 mv = new ModelAndView("TestVerifly");
		    	 break;
		   }// end switch

////////////////////////////////////////////////////////////////		
		return mv;
	}

	public void setKeysObj(ConfigurationContainer keysObj) {
		this.keysObj = keysObj;
	}

	public void setVeriflyConfigurationDao(
			VeriflyConfigurationDAO veriflyConfigurationDao) {
		this.veriflyConfigurationDao = veriflyConfigurationDao;
	}


}
