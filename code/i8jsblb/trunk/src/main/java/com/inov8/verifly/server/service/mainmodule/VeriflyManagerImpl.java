package com.inov8.verifly.server.service.mainmodule;

import java.security.KeyPair;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.MatchMode;
import org.springframework.context.MessageSource;

import sun.misc.BASE64Decoder;

import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.util.XMLUtil;
import com.inov8.hsm.controller.IPayShieldSwitchController;
import com.inov8.hsm.dto.PayShieldDTO;
import com.inov8.microbank.common.util.HttpInvokerUtil;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.verifly.common.constants.ActionConstants;
import com.inov8.verifly.common.constants.ConfigurationConstants;
import com.inov8.verifly.common.constants.FailureReasonConstants;
import com.inov8.verifly.common.constants.StatusConstants;
import com.inov8.verifly.common.constants.VeriflyConstants;
import com.inov8.verifly.common.des.EncryptionHandler;
import com.inov8.verifly.common.encoder.Encoder;
import com.inov8.verifly.common.encryption.AESEncryption;
import com.inov8.verifly.common.encryption.Encryption;
import com.inov8.verifly.common.exceptions.InvalidDataException;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.model.LogDetailModel;
import com.inov8.verifly.common.model.LogModel;
import com.inov8.verifly.common.model.VeriflyConfigurationModel;
import com.inov8.verifly.common.model.VfCardTypeModel;
import com.inov8.verifly.common.model.VfFailureReasonModel;
import com.inov8.verifly.common.model.VfPaymentModeModel;
import com.inov8.verifly.common.model.logmodule.LogListViewModel;
import com.inov8.verifly.common.util.ConfigurationContainer;
import com.inov8.verifly.common.util.DateUtil;
import com.inov8.verifly.common.util.RandomNumberGenerator;
import com.inov8.verifly.common.util.VariflyValidator;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl;
import com.inov8.verifly.common.wrapper.logmodule.LogWrapper;
import com.inov8.verifly.common.wrapper.logmodule.LogWrapperImpl;
import com.inov8.verifly.server.dao.logmodule.LogDAO;
import com.inov8.verifly.server.dao.logmodule.LogDetailDAO;
import com.inov8.verifly.server.dao.mainmodule.AccountInfoDAO;
import com.inov8.verifly.server.dao.mainmodule.CardTypeDAO;
import com.inov8.verifly.server.dao.mainmodule.FailureReasonDAO;
import com.inov8.verifly.server.dao.mainmodule.PaymentModeDAO;
import com.inov8.verifly.server.dao.mainmodule.VeriflyConfigurationDAO;
import com.inov8.verifly.server.service.logmodule.LogManager;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;

/**
 * INOV8 INC
 * <p>
 * interact with dao classes for database operations
 *
 *
 
 */
public class VeriflyManagerImpl implements VeriflyManager {
	protected AccountInfoDAO accountInfoDao;
    private FailureReasonDAO failureReasonDao;
    private HashMap configurationHashMap = new HashMap();
    private HashMap failureReasonHashMap = new HashMap();
    protected LogManager logManager;
    private VeriflyConfigurationDAO veriflyConfigurationDao;
    private final Log logger = LogFactory.getLog(this.getClass());
    private LogDAO logDao;
    private LogDetailDAO logDetailDao;
    private PaymentModeDAO paymentModeDAO;
    private CardTypeDAO cardTypeDAO;
    protected EncryptionHandler encryptionHandler;
    protected ConfigurationContainer keysObj ;
    protected VariflyValidator variflyValidator;
    protected MessageSource messageSource;
    
 	public VariflyValidator getVariflyValidator() {
		return variflyValidator;
	}
	public void setVariflyValidator(VariflyValidator variflyValidator) {
		this.variflyValidator = variflyValidator;
	}
	
	/**
	 * This method verifies the given pin
	 */
	public VeriflyBaseWrapper verifyOneTimePin(VeriflyBaseWrapper wrapper)throws Exception{
		
		if (logger.isDebugEnabled())
			logger.debug("<----------Start verifyOneTimePin()------->");
    	String pin = null ;
    	LogWrapper logWrapper = new LogWrapperImpl();
        LogModel logModel = new LogModel();
        LogDetailModel logDetailModel = new LogDetailModel ();
        AccountInfoModel receivedAccountInfoModel = wrapper.getAccountInfoModel();
      
        XStream xstream = new XStream(new PureJavaReflectionProvider());
        String xml = xstream.toXML(wrapper) ;
        xml = XMLUtil.replaceElementsUsingXPath(xml, ConfigurationConstants.REPLACE_COLUMNS_LIST);
        logModel.setInputParam(xml);
        logModel.setStartTime(new Date ());
        logModel.setActionId(ActionConstants.VERIFY_ONETIME_PIN);
        ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setEnableLike(Boolean.FALSE);
		if (logger.isDebugEnabled())
			logger.debug("<---------Objects Created Successfully-------->");

		try{
	          if (wrapper.getLogModel().getCreatedBy() != null && !wrapper.getLogModel().getCreatedBy().trim().equals("")){
	            	logModel.setCreatedBy(wrapper.getLogModel().getCreatedBy());
	            }
	            if (wrapper.getLogModel().getCreatdByUserId() != null ){
	            	logModel.setCreatdByUserId(wrapper.getLogModel().getCreatdByUserId());
	            }
			logDetailModel.setActionId(ActionConstants.VALIDATING_USER);
			AccountInfoModel accountInfoModelTemp = new AccountInfoModel ();
			accountInfoModelTemp.setCustomerId(receivedAccountInfoModel.getCustomerId());
			if (logger.isDebugEnabled())
				logger.debug("Customer Id----------->" + receivedAccountInfoModel.getCustomerId());
			accountInfoModelTemp.setAccountNick(receivedAccountInfoModel.getAccountNick());
			if (logger.isDebugEnabled())
				logger.debug("Nick---------->" + receivedAccountInfoModel.getAccountNick());			
            CustomList<AccountInfoModel> customList = this.accountInfoDao.findByExample(accountInfoModelTemp,null,null,exampleHolder);

            List list = customList.getResultsetList();

            if (list.size() == 0){
            	
            	logger.error("<----Record not found in DB------->");            	
            	throw new InvalidDataException (String.valueOf(FailureReasonConstants.RECORD_NOT_FOUND));            	
            }
            accountInfoModelTemp = (AccountInfoModel)list.get(ConfigurationConstants.FIRST_INDEX);
            if (!accountInfoModelTemp.getActive()){
            	logger.error("<----Record is InActive->");
            	throw new InvalidDataException (String.valueOf(FailureReasonConstants.RECORD_IS_INACTIVE));
            }	
            if (accountInfoModelTemp.getDeleted()){
            	logger.error("<----Record is Marked as deleted->");
            	throw new InvalidDataException (String.valueOf(FailureReasonConstants.RECORD_MARK_AS_DELETED));
            }	
            if (logger.isDebugEnabled())
            	logger.debug("<----Going to generate PIN->");
//            pin = encryptionHandler.encrypt(receivedAccountInfoModel.getOtPin());
            Encoder encoder =(Encoder)this.getEncoderClassObject();
            logDetailModel.setActionId(ActionConstants.APPLYING_HASH_ALGORITHM);
            pin = encoder.doHash(receivedAccountInfoModel.getOtPin());// apply hashing alogrithm
            if (pin == null){
            	logger.error("<--Error applying hash algorithm->");
            	throw new InvalidDataException (String.valueOf(FailureReasonConstants.ERROR_APPLYING_HASHING_ALGORITHM));
            }	
            long retryCount = Long.parseLong(keysObj.getValue(ConfigurationConstants.PIN_RETRY_COUNT));
            if (logger.isDebugEnabled())
            	logger.debug("Retry Count----->" + retryCount);
            if (accountInfoModelTemp.getOtRetryCount() >= retryCount){
            	logger.error("<----Retry count exceeded->");
            	throw new InvalidDataException (String.valueOf(FailureReasonConstants.OT_RETRY_COUNT_EXCEEDED));            	
            }
            try{
            	
            	logDetailModel.setActionId(ActionConstants.VERIFY_ONETIME_PIN);
            	if (accountInfoModelTemp.getOtPin() == null || accountInfoModelTemp.getOtPin().length() < 1){
            		logger.error("<----PIN does not exist->");
            		throw new InvalidDataException (String.valueOf(FailureReasonConstants.OT_PIN_DOES_NOT_EXISTS));
            	}	
            	if (!accountInfoModelTemp.getOtPin().equals(pin)){
            		accountInfoModelTemp.setOtRetryCount(accountInfoModelTemp.getOtRetryCount() + 1);
            		logger.error("<----One time pin and pin are not same->");
            		throw new InvalidDataException (String.valueOf(FailureReasonConstants.ONETIMEPIN_AND_PIN_ARE_NOT_MATCHED));
            	}
            	Date date = new Date ();
            	Date oneTimedate = accountInfoModelTemp.getOtPinIssuedOn();
            	Date tempOneTimeDate = (Date)oneTimedate.clone();
            	int expiryTime = Integer.parseInt(keysObj.getValue(ConfigurationConstants.PIN_EXPIRY_TIME));
            	if (logger.isDebugEnabled())
            		logger.debug("Expiry Time->" + expiryTime);
            	tempOneTimeDate = DateUtil.addMinutes(tempOneTimeDate, expiryTime);
            	       	
            	
            	if (date.compareTo(tempOneTimeDate) == 1 ){
            		accountInfoModelTemp.setOtPin(null);
            		accountInfoModelTemp.setOtPinIssuedOn(null);
            		accountInfoModelTemp.setOtRetryCount(new Long(0));
            		logger.error("<----One time pin expired->");
            		throw new InvalidDataException (String.valueOf(FailureReasonConstants.OT_PIN_EXPIRED));            		
            	}
            
            	accountInfoModelTemp.setOtPin(null);
        		accountInfoModelTemp.setOtPinIssuedOn(null);
        		accountInfoModelTemp.setOtRetryCount(new Long(0));
            	
            	
            }catch (InvalidDataException exp){
            	// It is done so that in case of any exception model is saved properly
            	logger.error("Exception in verify one time pin[Failure Reason Code]---->" + exp.getMessage());
            	accountInfoDao.saveOrUpdate(accountInfoModelTemp);
            	logger.error("<---Rethrowing exception--->");
            	throw exp;
            }
            
            accountInfoDao.saveOrUpdate(accountInfoModelTemp);
           
            
            String outputParams = xstream.toXML(wrapper);
            outputParams = XMLUtil.replaceElementsUsingXPath(outputParams, ConfigurationConstants.REPLACE_COLUMNS_LIST);
            logModel.setOutputParam(outputParams);

            logDetailModel.setStatusId(StatusConstants.SUCCESS);
            logModel.addLogIdLogDetailModel(logDetailModel);
            logModel.setStatusId(StatusConstants.SUCCESS);
            logModel.setEndTime(new Date());

            logWrapper = new LogWrapperImpl();
            logWrapper.setLogModel(logModel);
            this.logManager.insertLogRequiresNewTransaction(logWrapper);
            wrapper.setErrorStatus(true);

            AccountInfoModel accountInfoModel1 = (AccountInfoModel) accountInfoModelTemp.clone();
            accountInfoModel1 = encryptionHandler.decrypt(accountInfoModel1);
            //--------------------------------------------------------------------------------
            wrapper.setAccountInfoModel(accountInfoModel1);

            
		}catch (InvalidDataException exp) {
			
        	Long errorCode = Long.parseLong(exp.getMessage());
        	
        	if (logger.isDebugEnabled())
        		logger.debug("Error Code --->" + errorCode);

            logDetailModel.setStatusId(StatusConstants.FAILURE);
            logDetailModel.setFailureReasonId(errorCode);

            logModel.addLogIdLogDetailModel(logDetailModel);
            logModel.setEndTime(new Date());
            logModel.setStatusId(StatusConstants.FAILURE);
            logModel.setFailureReasonId(errorCode);
        	
            wrapper = new VeriflyBaseWrapperImpl();
            wrapper.setErrorStatus(false);
            String errorMessage = this.getErrorMessages(new Long(errorCode).toString());
            wrapper.setErrorMessage(errorMessage);
            wrapper.setErrorCode(String.valueOf(errorCode));
                        
            logModel.setOutputParam(xstream.toXML(wrapper));
            logWrapper = new LogWrapperImpl();
            logWrapper.setLogModel(logModel);
            this.logManager.insertLogRequiresNewTransaction(logWrapper);
            if (logger.isDebugEnabled())
            	logger.debug("<----------Record Updated--------->");

		}
		
		if (logger.isDebugEnabled())
			logger.debug("<-------END verifyOneTimePin()----------->");
		return wrapper ;
	}
	/**
	 * This method generates the PIN at one time
	 */
    public VeriflyBaseWrapper generateOneTimePin(VeriflyBaseWrapper wrapper)throws Exception{
    	if (logger.isDebugEnabled())
    		logger.debug("<-----Start generateOneTimePin()----->");
    	String pin = null ;
    	String otPin = null ;
    	LogWrapper logWrapper = new LogWrapperImpl();
        LogModel logModel = new LogModel();
        LogDetailModel logDetailModel = new LogDetailModel ();
        AccountInfoModel receivedAccountInfoModel = wrapper.getAccountInfoModel();
        
        XStream xstream = new XStream(new PureJavaReflectionProvider());
        String xml = xstream.toXML(wrapper) ;
        xml = XMLUtil.replaceElementsUsingXPath(xml, ConfigurationConstants.REPLACE_COLUMNS_LIST);
        logModel.setInputParam(xml);
        logModel.setStartTime(new Date ());
        logModel.setActionId(ActionConstants.GENERATE_ONETIME_PIN);
        ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setEnableLike(Boolean.FALSE);
		if (logger.isDebugEnabled())
			logger.debug("<---------Objects Created Successfully--------->");
		try{
            if (wrapper.getLogModel().getCreatedBy() != null && !wrapper.getLogModel().getCreatedBy().trim().equals("")){
            	logModel.setCreatedBy(wrapper.getLogModel().getCreatedBy());
            }
            if (wrapper.getLogModel().getCreatdByUserId() != null ){
            	logModel.setCreatdByUserId(wrapper.getLogModel().getCreatdByUserId());
            }
			logDetailModel.setActionId(ActionConstants.VALIDATING_USER);
			AccountInfoModel accountInfoModelTemp = new AccountInfoModel ();
			accountInfoModelTemp.setCustomerId(receivedAccountInfoModel.getCustomerId());
			if (logger.isDebugEnabled())
				logger.debug("Customer Id------->" + receivedAccountInfoModel.getCustomerId());
			accountInfoModelTemp.setAccountNick(receivedAccountInfoModel.getAccountNick());
			if (logger.isDebugEnabled())
				logger.debug("Nick---------->" + receivedAccountInfoModel.getAccountNick());			
            CustomList<AccountInfoModel> customList = this.accountInfoDao.findByExample(accountInfoModelTemp,null,null,exampleHolder);

            List list = customList.getResultsetList();

            if (list.size() == 0){
    			logger.error("<------Record not found in DB-------->");            	
            	throw new InvalidDataException (String.valueOf(FailureReasonConstants.RECORD_NOT_FOUND));            	
            }
            accountInfoModelTemp = (AccountInfoModel)list.get(ConfigurationConstants.FIRST_INDEX);
            
            if (!accountInfoModelTemp.getActive())
            	throw new InvalidDataException (String.valueOf(FailureReasonConstants.RECORD_IS_INACTIVE));
            if (accountInfoModelTemp.getDeleted())
            	throw new InvalidDataException (String.valueOf(FailureReasonConstants.RECORD_MARK_AS_DELETED));
            // User validated and now going to generate PIN
            Encoder encoder =(Encoder)this.getEncoderClassObject();
            // loop until the PIN and generated One time pin are different
            do{
            	
        	pin = RandomNumberGenerator.generatePin(getPinSize());
        	otPin = pin ;
        	logDetailModel.setActionId(ActionConstants.GENERATE_RANDOM_NO);
        	if (pin == null){
            	logger.error("<--------Error in generating random no----->") ;
            	throw new  InvalidDataException (String.valueOf(FailureReasonConstants.ERROR_GENERATING_RANDOM_NUMBER));
            }
        	logDetailModel.setActionId(ActionConstants.APPLYING_HASH_ALGORITHM);
        	pin = encoder.doHash(pin);
        	if (pin == null){
            	logger.error("<--------Error in generating random no------->") ;
            	throw new  InvalidDataException (String.valueOf(FailureReasonConstants.ERROR_GENERATING_RANDOM_NUMBER));
            }

            }while (pin == accountInfoModelTemp.getPin());
            logDetailModel.setActionId(ActionConstants.GENERATE_ONETIME_PIN);
            accountInfoModelTemp.setOtPin(pin);
            accountInfoModelTemp.setOtPinIssuedOn(new Date());
            accountInfoModelTemp.setOtRetryCount(new Long (0));
            
            accountInfoDao.saveOrUpdate(accountInfoModelTemp); // update the record after setting values


           
            String outputParams = xstream.toXML(wrapper);
            outputParams = XMLUtil.replaceElementsUsingXPath(outputParams, ConfigurationConstants.REPLACE_COLUMNS_LIST);
            logModel.setOutputParam(outputParams);

            receivedAccountInfoModel.setOtPin(otPin);
            wrapper.setAccountInfoModel(receivedAccountInfoModel);
            logDetailModel.setStatusId(StatusConstants.SUCCESS);
            logModel.addLogIdLogDetailModel(logDetailModel);
            logModel.setStatusId(StatusConstants.SUCCESS);
            logModel.setEndTime(new Date());

            logWrapper = new LogWrapperImpl();
            logWrapper.setLogModel(logModel);
            this.logManager.insertLogRequiresNewTransaction(logWrapper);
            wrapper.setErrorStatus(true);

            
		}catch (InvalidDataException exp){
        	Long errorCode = Long.parseLong(exp.getMessage());
        	//logger.error( StringUtils.prepareExceptionStackTrace(exp));
        	if (logger.isDebugEnabled())
        		logger.debug("Error Code --->" + errorCode);

            logDetailModel.setStatusId(StatusConstants.FAILURE);
            logDetailModel.setFailureReasonId(errorCode);

            logModel.addLogIdLogDetailModel(logDetailModel);
            logModel.setEndTime(new Date());
            logModel.setStatusId(StatusConstants.FAILURE);
            logModel.setFailureReasonId(errorCode);
        	
            wrapper = new VeriflyBaseWrapperImpl();
            wrapper.setErrorStatus(false);
            String errorMessage = this.getErrorMessages(new Long(errorCode).toString());
            wrapper.setErrorMessage(errorMessage);
                        
            logModel.setOutputParam(xstream.toXML(wrapper));
            logWrapper = new LogWrapperImpl();
            logWrapper.setLogModel(logModel);
            this.logManager.insertLogRequiresNewTransaction(logWrapper);
            if (logger.isDebugEnabled())
            	logger.debug("<----------Record Updated------------->");

		}
		
		if (logger.isDebugEnabled())
			logger.debug("<-----END generateOneTimePin()------->");
    	
    	return wrapper ;
    }
	/**
	 * This method sets the existing record as deleted
	 * @param wrapper
	 * @return
	 * @throws Exception
	 */
	public VeriflyBaseWrapper markAsDeleted(VeriflyBaseWrapper wrapper)throws Exception{
		if (logger.isDebugEnabled())
			logger.debug("<------Start markAsDeleted()--------->");
    	LogWrapper logWrapper = new LogWrapperImpl();
        LogModel logModel = new LogModel();
        LogDetailModel logDetailModel = new LogDetailModel ();
        
        XStream xstream = new XStream(new PureJavaReflectionProvider());
        String xml = xstream.toXML(wrapper) ;
        xml = XMLUtil.replaceElementsUsingXPath(xml, ConfigurationConstants.REPLACE_COLUMNS_LIST);
        logModel.setInputParam(xml);

        logModel.setStartTime(new Date());
        logModel.setActionId(ActionConstants.MARK_AS_DELETED);
        ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setEnableLike(Boolean.FALSE);
		if (logger.isDebugEnabled())
			logger.debug("<-----Objects Created Successfully--->");

		try{
            if (wrapper.getLogModel().getCreatedBy() != null && !wrapper.getLogModel().getCreatedBy().trim().equals("")){
            	logModel.setCreatedBy(wrapper.getLogModel().getCreatedBy());
            }
            
            if (wrapper.getLogModel().getCreatdByUserId() != null ){
            	logModel.setCreatdByUserId(wrapper.getLogModel().getCreatdByUserId());
            }
			logDetailModel.setActionId(ActionConstants.VALIDATING_USER);
			AccountInfoModel accountInfoModelTemp = new AccountInfoModel ();
			accountInfoModelTemp.setCustomerId(wrapper.getAccountInfoModel().getCustomerId());
			if (logger.isDebugEnabled())
				logger.debug("Customer Id------>" + wrapper.getAccountInfoModel().getCustomerId());
			accountInfoModelTemp.setAccountNick(wrapper.getAccountInfoModel().getAccountNick());
			if (logger.isDebugEnabled())
				logger.debug("Nick------->" + wrapper.getAccountInfoModel().getAccountNick());			
            CustomList<AccountInfoModel> customList = this.accountInfoDao.findByExample(accountInfoModelTemp,null,null,exampleHolder);

            List list = customList.getResultsetList();

            if (list.size() == 0){
    			logger.error("<------Record not found in DB------>");            	
            	throw new InvalidDataException (String.valueOf(FailureReasonConstants.RECORD_NOT_FOUND));            	
            }
            logDetailModel.setActionId(ActionConstants.MARK_AS_DELETED);            
            // now to mark the record as deleted
            AccountInfoModel accountInfoModel = (AccountInfoModel)list.get(ConfigurationConstants.FIRST_INDEX);
            accountInfoModel.setDeleted(true);
            accountInfoModel.setCardNo(null);
            accountInfoModel.setCardExpiryDate(null);
            accountInfoModel.setAccountNo(null);
            accountInfoModel.setActive(false);
            accountInfoModel.setOtPin(null);
            accountInfoModel.setOtPinIssuedOn(null);
            accountInfoModel.setOtRetryCount(new Long(0));
            
            accountInfoModel.setPin(ConfigurationConstants.DELETED_STRING);
            accountInfoModel.setFirstName(ConfigurationConstants.DELETED_STRING);
            accountInfoModel.setLastName(ConfigurationConstants.DELETED_STRING);
            accountInfoModel.setCustomerMobileNo(ConfigurationConstants.DELETED_STRING);
            accountInfoModel.setComments(ConfigurationConstants.DELETED_STRING);
            accountInfoModel.setDescription(ConfigurationConstants.DELETED_STRING);
            if (logger.isDebugEnabled())
            	logger.debug("Time Stamp----->" + wrapper.getAccountInfoModel().getNewAccountNick());
            
            
            // set the account nick as new acount nick + account nick
            accountInfoModel.setAccountNick(wrapper.getAccountInfoModel().getAccountNick() + "_" + wrapper.getAccountInfoModel().getNewAccountNick());
            this.accountInfoDao.saveOrUpdate(accountInfoModel);
            wrapper.setAccountInfoModel(accountInfoModel);
            String outputParams = xstream.toXML(wrapper);
            outputParams = XMLUtil.replaceElementsUsingXPath(outputParams, ConfigurationConstants.REPLACE_COLUMNS_LIST);
            logModel.setOutputParam(outputParams);

 

            logDetailModel.setStatusId(StatusConstants.SUCCESS);
            logModel.addLogIdLogDetailModel(logDetailModel);
            logModel.setStatusId(StatusConstants.SUCCESS);
            logModel.setEndTime(new Date());

            logWrapper = new LogWrapperImpl();
            logWrapper.setLogModel(logModel);
            this.logManager.insertLogRequiresNewTransaction(logWrapper);
            wrapper.setErrorStatus(true);
            
            

		}catch (InvalidDataException exp){

        	Long errorCode = Long.parseLong(exp.getMessage());
        	//logger.error( StringUtils.prepareExceptionStackTrace(exp));
        	if (logger.isDebugEnabled())
        		logger.debug("Error Code --->" + errorCode);

            logDetailModel.setStatusId(StatusConstants.FAILURE);
            logDetailModel.setFailureReasonId(errorCode);

            logModel.addLogIdLogDetailModel(logDetailModel);
            logModel.setEndTime(new Date());
            logModel.setStatusId(StatusConstants.FAILURE);
            logModel.setFailureReasonId(errorCode);
        	
            wrapper = new VeriflyBaseWrapperImpl();
            wrapper.setErrorStatus(false);
            String errorMessage = this.getErrorMessages(new Long(errorCode).toString());
            wrapper.setErrorMessage(errorMessage);
                        
            logModel.setOutputParam(xstream.toXML(wrapper));
            logWrapper = new LogWrapperImpl();
            logWrapper.setLogModel(logModel);
            this.logManager.insertLogRequiresNewTransaction(logWrapper);
            if (logger.isDebugEnabled())
            	logger.debug("<----------Record Updated-------->");

		}
		if (logger.isDebugEnabled())
			logger.debug("<-----END markAsDeleted()------>");
		return wrapper ;
	}
	
    public VeriflyBaseWrapper changeAccountNick(VeriflyBaseWrapper wrapper)throws Exception{
    	if (logger.isDebugEnabled())
    		logger.debug("<----Start changeAccountNick()--->");
    	LogWrapper logWrapper = new LogWrapperImpl();
        LogModel logModel = new LogModel();
        LogDetailModel logDetailModel = new LogDetailModel ();
        
        XStream xstream = new XStream(new PureJavaReflectionProvider());
        String xml = xstream.toXML(wrapper) ;
        xml = XMLUtil.replaceElementsUsingXPath(xml, ConfigurationConstants.REPLACE_COLUMNS_LIST);
        logModel.setInputParam(xml);

        logModel.setStartTime(new Date());
        logModel.setActionId(ActionConstants.CHANGE_ACCOUNT_NICK);
        ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setEnableLike(Boolean.FALSE);
		if (logger.isDebugEnabled())
			logger.debug("<-----Objects Created Successfully----->");
		
		try{
			if (logger.isDebugEnabled())
				logger.debug("<---Going to validate financial info--->");			
			logDetailModel.setActionId(ActionConstants.VALIDATING_FINANCIAL_INFO);
			if (!variflyValidator.isValidDataForChangeAccountNick(wrapper)){
				logger.error("<-----Insufficient data---->");
				throw new InvalidDataException (String.valueOf(FailureReasonConstants.INSUFFICIENT_DATA));
			}
			if (logger.isDebugEnabled())
				logger.debug("<---Going to validate user-->");
			logDetailModel.setActionId(ActionConstants.VALIDATING_USER);

			AccountInfoModel accountInfoModelTemp = new AccountInfoModel ();
			accountInfoModelTemp.setCustomerId(wrapper.getAccountInfoModel().getCustomerId());
			accountInfoModelTemp.setAccountNick(wrapper.getAccountInfoModel().getAccountNick());
            CustomList<AccountInfoModel> customList = this.accountInfoDao.findByExample(accountInfoModelTemp,null,null,exampleHolder);

            List list = customList.getResultsetList();

            if (list.size() == 0){
    			logger.error("<--Record not found in DB--->");            	
            	throw new InvalidDataException (String.valueOf(FailureReasonConstants.RECORD_NOT_FOUND));            	
            }
            accountInfoModelTemp = (AccountInfoModel) list.get(ConfigurationConstants.FIRST_INDEX);            
            if (!accountInfoModelTemp.getActive())
            	throw new InvalidDataException (String.valueOf(FailureReasonConstants.RECORD_IS_INACTIVE));
           
            // Again Query with new nick
             AccountInfoModel accountInfoModelTemp1 = new AccountInfoModel ();
             accountInfoModelTemp1.setCustomerId(wrapper.getAccountInfoModel().getCustomerId());
             accountInfoModelTemp1.setAccountNick(wrapper.getAccountInfoModel().getNewAccountNick());
            customList = this.accountInfoDao.findByExample(accountInfoModelTemp1,null,null,exampleHolder);
            list = customList.getResultsetList(); 

            if (list.size() > 0){
    			logger.error("<---Record already found in db-->");            	
            	throw new InvalidDataException (String.valueOf(FailureReasonConstants.RECORD_ALREADY_EXISTS));            	
            }
            if (logger.isDebugEnabled()){
            	logger.debug("<--Going to save in db-->");
            	logger.debug("<-New Nick--->" + wrapper.getAccountInfoModel().getNewAccountNick());
            }	
            accountInfoModelTemp.setAccountNick(wrapper.getAccountInfoModel().getNewAccountNick());
            this.accountInfoDao.saveOrUpdate(accountInfoModelTemp);
            AccountInfoModel accountInfoModel = (AccountInfoModel)accountInfoModelTemp.clone();
			logger.error("<---Going to decryprt--->");
            accountInfoModel = encryptionHandler.decrypt(accountInfoModel);
            
            wrapper.setAccountInfoModel(accountInfoModel);
            String outputParams = xstream.toXML(wrapper);
            outputParams = XMLUtil.replaceElementsUsingXPath(outputParams, ConfigurationConstants.REPLACE_COLUMNS_LIST);
            logModel.setOutputParam(outputParams);
            if (wrapper.getLogModel().getCreatedBy() != null && !wrapper.getLogModel().getCreatedBy().trim().equals("")){
            	logModel.setCreatedBy(wrapper.getLogModel().getCreatedBy());
            }
            
            if (wrapper.getLogModel().getCreatdByUserId() != null ){
            	logModel.setCreatdByUserId(wrapper.getLogModel().getCreatdByUserId());
            }

            logDetailModel.setStatusId(StatusConstants.SUCCESS);
            logModel.addLogIdLogDetailModel(logDetailModel);
            logModel.setStatusId(StatusConstants.SUCCESS);
            logModel.setEndTime(new Date());

            logWrapper = new LogWrapperImpl();
            logWrapper.setLogModel(logModel);
            this.logManager.insertLogRequiresNewTransaction(logWrapper);
            wrapper.setErrorStatus(true);

			
		}catch (InvalidDataException exp){
        	Long errorCode = Long.parseLong(exp.getMessage());
        	//logger.error(StringUtils.prepareExceptionStackTrace(exp));
        	if (logger.isDebugEnabled())		
        		logger.debug("Error Code --->" + errorCode);

            logDetailModel.setStatusId(StatusConstants.FAILURE);
            logDetailModel.setFailureReasonId(errorCode);

            logModel.addLogIdLogDetailModel(logDetailModel);
            logModel.setEndTime(new Date());
            logModel.setStatusId(StatusConstants.FAILURE);
            logModel.setFailureReasonId(errorCode);
        	
            wrapper = new VeriflyBaseWrapperImpl();
            wrapper.setErrorStatus(false);
            String errorMessage = this.getErrorMessages(new Long(errorCode).toString());
            wrapper.setErrorMessage(errorMessage);
                        
            logModel.setOutputParam(xstream.toXML(wrapper));
            logWrapper = new LogWrapperImpl();
            logWrapper.setLogModel(logModel);
            this.logManager.insertLogRequiresNewTransaction(logWrapper);
            if (logger.isDebugEnabled())
            	logger.debug("<---Record Updated-->");

		}
		if (logger.isDebugEnabled())
			logger.debug("<----END changeAccountNick()-->");
    	return wrapper ;
    }
    
    public VeriflyBaseWrapper activatePIN(VeriflyBaseWrapper wrapper) throws Exception 
    {
    	return this.activatePIN(wrapper,true);
    }
    
	/**TODO: Done but needs unit testing.
	 * new pin is generated upon activation. need to call integration for pin generation and set its pvv in accountInfo
	 * This method is used to acivate already created PIN
	 */
	public VeriflyBaseWrapper activatePIN(VeriflyBaseWrapper wrapper, boolean isNormalMode) throws
    Exception {
		if (logger.isDebugEnabled())
			logger.debug("<----Start activatePin()---->");
    	LogWrapper logWrapper = new LogWrapperImpl();
        LogModel logModel = new LogModel();
        AccountInfoModel accountInfoModel = new AccountInfoModel();
        LogDetailModel logDetailModel = new LogDetailModel ();
        
        
        logModel.setStartTime(new Date());
        logModel.setActionId(ActionConstants.ACTIVATE_PIN);

        // loging input params.
        XStream xstream = new XStream(new PureJavaReflectionProvider());
        String xml = xstream.toXML(wrapper) ;
        xml = XMLUtil.replaceElementsUsingXPath(xml, ConfigurationConstants.REPLACE_COLUMNS_LIST);
        logModel.setInputParam(xml);
        ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setEnableLike(Boolean.FALSE);
		if (logger.isDebugEnabled())
			logger.debug("<--Objects created successfully-->");

        try{
        	if (logger.isDebugEnabled())
        		logger.debug("<----Going to validate financial information----->");
            logDetailModel.setActionId(ActionConstants.VALIDATING_FINANCIAL_INFO);
            if(!variflyValidator.isValidDataForActivatePin(wrapper)){
            	logger.error("<---Validation failed-->");
            	throw new InvalidDataException (String.valueOf(FailureReasonConstants.INSUFFICIENT_DATA));
            }
            // get the values from received log model and set in the log model
            if (wrapper.getLogModel() !=  null){
            	if (wrapper.getLogModel().getCreatdByUserId() != null){
            		logModel.setCreatdByUserId(wrapper.getLogModel().getCreatdByUserId());
            	}
            	if (wrapper.getLogModel().getCreatedBy() != null){
            		logModel.setCreatedBy(wrapper.getLogModel().getCreatedBy());
            	}
            	
            }
            /// going to validate user
            if (logger.isDebugEnabled())
            	logger.debug("<--Going to validate user---->");
            logDetailModel.setActionId(ActionConstants.VALIDATING_USER);
            accountInfoModel.setCustomerId(wrapper.getAccountInfoModel().getCustomerId());
            accountInfoModel.setAccountNick(wrapper.getAccountInfoModel().getAccountNick());
            
            CustomList<AccountInfoModel>
            customList = this.accountInfoDao.findByExample(accountInfoModel,null,null,exampleHolder);

            List list = customList.getResultsetList();
            if (list.size() == 0){
            	logger.error("<---Record not found in DB--->");
            	throw new InvalidDataException (String.valueOf(FailureReasonConstants.RECORD_NOT_FOUND));
            }
            accountInfoModel = (AccountInfoModel) list.get(ConfigurationConstants.FIRST_INDEX); // get account model from list
            if (accountInfoModel.getActive()){
            	logger.error("<--Record already active-->");
            	throw new InvalidDataException (String.valueOf(FailureReasonConstants.RECORD_ALREADY_ACTIVATED));            	
            }
            // going to generate pin
            logDetailModel.setActionId(ActionConstants.GENERATE_RANDOM_NO);
            
            String pin = null ;
            
            PayShieldDTO dto = new PayShieldDTO();
            dto.setPAN(accountInfoModel.getPan());
            
            if(wrapper != null && (wrapper.getSkipPanCheck() == null || wrapper.getSkipPanCheck() == Boolean.FALSE)) { // ReLink Account Case Only
                IPayShieldSwitchController iPayShieldSwitchController = getIntegrationSwitch();
            	dto = iPayShieldSwitchController.generateSystemPIN(dto);
            } else if(wrapper != null && (wrapper.getSkipPanCheck() != null && wrapper.getSkipPanCheck() == Boolean.TRUE)) {
            	dto.setResponseCode("00");
            }
            
            if ((dto != null) && (dto.getResponseCode() != null && dto.getResponseCode().equals("00"))) {
                accountInfoModel.setPvv(dto.getPVV());
            }else{
            	logger.error("<--Error generating Random No-->");
            	accountInfoModel = null;
            	throw new InvalidDataException (String.valueOf(FailureReasonConstants.ERROR_GENERATING_RANDOM_NUMBER));
    	
            }
            
            /*if( isNormalMode )            	           	
            	pin = RandomNumberGenerator.generatePin(getPinSize());
            else
            	pin = this.messageSource.getMessage(VeriflyLiteConstants.VERIFLY_LITE_PIN_KEY, null, null);
           
                        
            if (pin == null) {
            	logger.error("<--Error generating Random No-->");
            	throw new InvalidDataException (String.valueOf(FailureReasonConstants.ERROR_GENERATING_RANDOM_NUMBER));
            } 
            if (logger.isDebugEnabled())
            	logger.debug("<--Applying Hash Alogorithm-->");
            // Apply the Hash algorithm oner pin
            logDetailModel.setActionId(ActionConstants.APPLYING_HASH_ALGORITHM);           
            Encoder encoder = (Encoder)this.getEncoderClassObject();
            String encodedpin = encoder.doHash(pin);
            
            if (encodedpin == null){
            	logger.error("<-----Error Applying Hash Algorithm---->");
            	throw new InvalidDataException (String.valueOf(FailureReasonConstants.ERROR_APPLYING_HASHING_ALGORITHM));            	
            }

            accountInfoModel.setPin(encodedpin);
            */
            
            logDetailModel.setActionId(ActionConstants.CHANGE_PIN);           
            
            accountInfoModel.setActive(true);
            accountInfoModel.setUpdatedOn(new Date());
            if (logger.isDebugEnabled())
            	logger.debug("<----Going to update records------>");
            // Encrypr the data before saving in db

            accountInfoModel = this.accountInfoDao.saveOrUpdate(accountInfoModel);
            wrapper = new VeriflyBaseWrapperImpl();
            AccountInfoModel accountInfoModelTemp = (AccountInfoModel)accountInfoModel.clone();
            //-----------------------------------------------Decrypt-------------------------------------------------
             accountInfoModelTemp = encryptionHandler.decrypt(accountInfoModelTemp);
            	//-----------------------------------------------Decrypt-------------------------------------------------               
            //setting readable generated pin into accountIndo Model
            accountInfoModel.setGeneratedPin(pin);
            accountInfoModelTemp.setGeneratedPin(pin);
            wrapper.setAccountInfoModel(accountInfoModelTemp);
            
            //ensure that readable pin not saved in log
            String savePin = wrapper.getAccountInfoModel().getPin();
            String saveGeneratedPin = wrapper.getAccountInfoModel().getGeneratedPin();
            wrapper.getAccountInfoModel().setPin(null);
            wrapper.getAccountInfoModel().setGeneratedPin(null);
            
            // logging output Params
            String outputParams = xstream.toXML(wrapper);
            outputParams = XMLUtil.replaceElementsUsingXPath(outputParams, ConfigurationConstants.REPLACE_COLUMNS_LIST);
            logModel.setOutputParam(outputParams);

            wrapper.getAccountInfoModel().setPin(savePin);
            wrapper.getAccountInfoModel().setGeneratedPin(saveGeneratedPin);
            savePin = null;
            saveGeneratedPin = null;

            logDetailModel.setStatusId(StatusConstants.SUCCESS);
            logModel.addLogIdLogDetailModel(logDetailModel);
            logModel.setStatusId(StatusConstants.SUCCESS);
            logModel.setEndTime(new Date());

            logWrapper = new LogWrapperImpl();
            logWrapper.setLogModel(logModel);
            this.logManager.insertLogRequiresNewTransaction(logWrapper);
            wrapper.setErrorStatus(true);
            if (logger.isDebugEnabled())
            	logger.debug("<---PIN activated successfully-->");
            
        }catch (InvalidDataException exp){
        	Long errorCode = Long.parseLong(exp.getMessage());
        	//logger.error(StringUtils.prepareExceptionStackTrace(exp));
        	if (logger.isDebugEnabled())
        		logger.debug("Error Code --->" + errorCode);

            logDetailModel.setStatusId(StatusConstants.FAILURE);
            logDetailModel.setFailureReasonId(errorCode);

            logModel.addLogIdLogDetailModel(logDetailModel);
            logModel.setEndTime(new Date());
            logModel.setStatusId(StatusConstants.FAILURE);
            logModel.setFailureReasonId(errorCode);
        	
            wrapper = new VeriflyBaseWrapperImpl();
            wrapper.setErrorStatus(false);
            String errorMessage = this.getErrorMessages(new Long(errorCode).toString());
            wrapper.setErrorMessage(errorMessage);
                        
            logModel.setOutputParam(xstream.toXML(wrapper));
            logWrapper = new LogWrapperImpl();
            logWrapper.setLogModel(logModel);
            this.logManager.insertLogRequiresNewTransaction(logWrapper);
            if (logger.isDebugEnabled())
            	logger.debug("<----------Record Updated----------------------------->");
        }
        if (logger.isDebugEnabled())
        	logger.debug("<----------END activatePin()----------------------------->");
        
        return wrapper;
    }
    
    /**TODO: Done. update PVV based on new pin provided by calling integration.
     * THis method changes the PIN of the user
     */
    public VeriflyBaseWrapper changePIN(VeriflyBaseWrapper wrapper) throws
    Exception {
    	if (logger.isDebugEnabled())
    		logger.debug("<--Start changePin()---->");
        LogWrapper logWrapper = new LogWrapperImpl();
        LogModel logModel = new LogModel();
        LogDetailModel logDetailModel = new LogDetailModel ();
        AccountInfoModel accountInfoModel = new AccountInfoModel();

        XStream xstream = new XStream(new PureJavaReflectionProvider());
        Encryption encryption = (Encryption)keysObj.getEncrptionClassObject();
        
        /*********************************************************************************
		 * Updated by Soofia Faruq AES Encryption Support
		 */
		KeyPair keypair = null;
		SecretKey aesKey = null;
		if (encryption instanceof AESEncryption) {
			String strKey = keysObj.getValue(ConfigurationConstants.AES_KEY);
//			aesKey = new SecretKeySpec(strKey.getBytes(), 0,
//					strKey.getBytes().length, "AES");
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] encodedKey = decoder.decodeBuffer(strKey);
	        aesKey = new SecretKeySpec(encodedKey,0,encodedKey.length, "AES"); 
		} else {
			String keyPairValue = keysObj
					.getValue(ConfigurationConstants.KEY_PAIR);
			keypair = (KeyPair) xstream.fromXML(keyPairValue);
		}
		/**********************************************************************************/

        logModel.setStartTime(new Date());
        logModel.setActionId(ActionConstants.CHANGE_PIN);
        String xml = xstream.toXML(wrapper) ;
        xml = XMLUtil.replaceElementsUsingXPath(xml, ConfigurationConstants.REPLACE_COLUMNS_LIST);
        logModel.setInputParam(xml);
        if (logger.isDebugEnabled())
        	logger.debug("<--Objects created successfully---->");        
        
        try{
        logDetailModel.setActionId(ActionConstants.VALIDATING_FINANCIAL_INFO);
        if (logger.isDebugEnabled())
        	logger.debug("<--Going to validate financial info---->");
        if (!variflyValidator.isValidDataForChangePin(wrapper)){
        	logger.error("<--Insufficient Finnancial data---->");
        	throw new InvalidDataException (String.valueOf(FailureReasonConstants.INSUFFICIENT_DATA));
        	}
 
        if (wrapper.getLogModel() != null){
        	if (wrapper.getLogModel().getCreatedBy() != null)
        		logModel.setCreatedBy(wrapper.getLogModel().getCreatedBy());
        	if (wrapper.getLogModel().getCreatdByUserId() != null)
        		logModel.setCreatdByUserId(wrapper.getLogModel().getCreatdByUserId());
        }
        
        if (!wrapper.getAccountInfoModel().getNewPin().equals(wrapper.getAccountInfoModel().getConfirmNewPin())){
        	logger.error("<--New and Confirm new pins dont match---->");            
        	throw new InvalidDataException (String.valueOf(FailureReasonConstants.NEW_AND_CONFIRM_NEW_PINS_ARE_NOT_MATCHED));
        }
        String decryptedOldPin = null;
        String decryptedPin = null;
		/*********************************************************************************
		 * Updated by Soofia Faruq
		 */
		if (keypair != null) {
			decryptedPin = encryption.decrypt(keypair.getPrivate(), wrapper.getAccountInfoModel().getNewPin());
			decryptedOldPin = encryption.decrypt(keypair.getPrivate(), wrapper.getAccountInfoModel().getOldPin());
			
		} else {
			decryptedPin = encryption.decrypt(aesKey, wrapper.getAccountInfoModel().getNewPin());
            if(wrapper.getObject("IS_MIGRATED") == null)
                decryptedOldPin = encryption.decrypt(aesKey, wrapper.getAccountInfoModel().getOldPin());
		}
		/**********************************************************************************/

        String minLength = this.getConfiguration(new Long(ConfigurationConstants.MIN_PIN_LENGTH).toString());
        int minPinLength = Integer.parseInt(minLength);

        String maxLength = this.getConfiguration(new Long(ConfigurationConstants.MAX_PIN_LENGTH).toString());
        int maxPinLength = Integer.parseInt(maxLength);

      // logging of PIN size
        logDetailModel.setActionId(ActionConstants.VERIFY_PIN_LENGTH);
        
        if (decryptedPin.length() < minPinLength){
        	logger.error("<--Pin length is shorter than minimum---->");
        	throw new InvalidDataException (String.valueOf(FailureReasonConstants.PIN_LENGTH_IS_SHORTER));
        }
        if (decryptedPin.length() > maxPinLength){
        	logger.error("<--Pin length is greated than maximum---->");
        	throw new InvalidDataException (String.valueOf(FailureReasonConstants.PIN_LENGTH_IS_GREATER));
        }	
 
        wrapper.setErrorStatus(false);
        logDetailModel.setStatusId(StatusConstants.SUCCESS);
        logModel.addLogIdLogDetailModel(logDetailModel);

        logDetailModel.setActionId(ActionConstants.VALIDATING_USER);
        accountInfoModel = validatePIN(wrapper,encryption,keypair);
        
        

        if (accountInfoModel == null){
            logger.error("<--Incorrect Pin given-->");
        	throw new InvalidDataException (String.valueOf(FailureReasonConstants.INVALID_BANK_PIN_PLEASE_ENTER_VALID_BANK_PIN));
        } 	
        if (!accountInfoModel.getActive()){
        	throw new InvalidDataException (String.valueOf(FailureReasonConstants.RECORD_IS_INACTIVE));
        }
        if (accountInfoModel.getAccountInfoId() == null){
        	throw new InvalidDataException (String.valueOf(FailureReasonConstants.INVALID_BANK_PIN_PLEASE_ENTER_VALID_BANK_PIN));
        }
       
        /*Encoder encoder = (Encoder)this.getEncoderClassObject();
        String encodedPin = encoder.doHash(decryptedPin);
        // logging encoding process
        logDetailModel = new LogDetailModel();
        logDetailModel.setActionId(ActionConstants.APPLYING_HASH_ALGORITHM);

        if (encodedPin == null){
        	if (logger.isDebugEnabled())
        		logger.debug("<--Error applying hash algorithm---->");
        	throw new InvalidDataException (String.valueOf(FailureReasonConstants.ERROR_APPLYING_HASHING_ALGORITHM));
        }
        logDetailModel.setStatusId(StatusConstants.SUCCESS);
        logModel.addLogIdLogDetailModel(logDetailModel);

        accountInfoModel.setPin(encodedPin);*/
        
        // logging process
        logDetailModel = new LogDetailModel();
        logDetailModel.setActionId(ActionConstants.APPLYING_HASH_ALGORITHM);
        logDetailModel.setStatusId(StatusConstants.SUCCESS);
        logModel.addLogIdLogDetailModel(logDetailModel);

        PayShieldDTO dto = new PayShieldDTO();
        if(wrapper != null && wrapper.getObject("IS_MIGRATED") == null)
        {
            dto.setOldPIN(decryptedOldPin);
            dto.setOldPVV(accountInfoModel.getPvv());
        }
        dto.setPAN(accountInfoModel.getPan());
        dto.setPIN(decryptedPin);
        
        IPayShieldSwitchController iPayShieldSwitchController = getIntegrationSwitch();
        if(wrapper != null && wrapper.getObject("IS_MIGRATED") == null)
            dto = iPayShieldSwitchController.changeUserPIN(dto);
        else
            dto = iPayShieldSwitchController.generateUserPIN(dto);
        
        if (dto != null && dto.getResponseCode() != null && dto.getResponseCode().equals("00")) {

        	logger.info("<--New PVV has been generated against given PIN. Going to update Account Info---->");
        	accountInfoModel.setPvv(dto.getPVV());
            accountInfoModel.setUpdatedOn(new Date());
            accountInfoModel.setIsMigrated(0L);
            accountInfoModel = this.accountInfoDao.saveOrUpdate(accountInfoModel);

        }else{
        	logger.error("Error while generating new PVV against given PIN-. Response Code:" + (dto == null ? " NIL. " : dto.getResponseCode()));
        	throw new InvalidDataException (String.valueOf(FailureReasonConstants.ERROR_SAVING_OR_UPDATING_RECORD));
	
        }
        
        //wrapper = new VeriflyBaseWrapperImpl();
        wrapper.getAccountInfoModel().setGeneratedPin(wrapper.getAccountInfoModel().getNewPin());
        
        //ensure that readable pin not saved in log
 
        String savePin = wrapper.getAccountInfoModel().getNewPin();
 
        
        String saveGeneratedPin = wrapper.getAccountInfoModel().getGeneratedPin();
 
        wrapper.getAccountInfoModel().setPin(null);
        wrapper.getAccountInfoModel().setGeneratedPin(null);
        wrapper.setErrorStatus(true);
        // logging output Params
        String outputParams = xstream.toXML(wrapper);
        outputParams = XMLUtil.replaceElementsUsingXPath(outputParams, ConfigurationConstants.REPLACE_COLUMNS_LIST);
        logModel.setOutputParam(outputParams);

        
        //save original values in wrapper of pin and generated pin for send to client
        wrapper.getAccountInfoModel().setPin(savePin);
        wrapper.getAccountInfoModel().setGeneratedPin(saveGeneratedPin);
        savePin = null;
        saveGeneratedPin = null;

        logModel.setEndTime(new Date());
        logModel.setStatusId(StatusConstants.SUCCESS);
        logModel.setAccountInfoId(accountInfoModel.getAccountInfoId());

        logWrapper = new LogWrapperImpl();
        logWrapper.setLogModel(logModel);

        this.logManager.insertLogRequiresNewTransaction(logWrapper);
        if (logger.isDebugEnabled())
        	logger.debug("<--Pin changed successfully---->");
        }catch (InvalidDataException exp) {
        	Long errorCode = Long.parseLong(exp.getMessage());
        	//logger.error(StringUtils.prepareExceptionStackTrace(exp));
            logDetailModel.setStatusId(StatusConstants.FAILURE);
            logDetailModel.setFailureReasonId(errorCode);

            logModel.addLogIdLogDetailModel(logDetailModel);
            logModel.setEndTime(new Date());
            logModel.setStatusId(StatusConstants.FAILURE);
            logModel.setFailureReasonId(errorCode);
        	
            wrapper = new VeriflyBaseWrapperImpl();
            wrapper.setErrorStatus(false);
            String errorMessage = this.getErrorMessages(new Long(errorCode).toString());
            wrapper.setErrorMessage(errorMessage);
                        
            logModel.setOutputParam(xstream.toXML(wrapper));
            logWrapper = new LogWrapperImpl();
            logWrapper.setLogModel(logModel);
            this.logManager.insertLogRequiresNewTransaction(logWrapper);
            if (logger.isDebugEnabled())
            	logger.debug("<--Failure records updated---->");
        }
        if (logger.isDebugEnabled())
        	logger.debug("<--End pinChange ()---->");
    	return wrapper ;
    }


    /** Done: verify pin on each transction call
     * This method validates if the pin recieved is correct pin or not.
     * The recieved pin is first deincrypted using the private key and then
     * hashed using the SHA algo and finally it is matched with the pin stored in DB.
     *
     * @param wrapper VeriflyBaseWrapper
     * @return AccountInfoModel
     */
    protected AccountInfoModel validatePIN(VeriflyBaseWrapper wrapper, Encryption encryption,KeyPair keypair) throws Exception{
        //log activities
    	if (logger.isDebugEnabled())
    		logger.debug("validatePIN start");

        AccountInfoModel accountInfoModel = new AccountInfoModel();
        ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setEnableLike(Boolean.FALSE);
        
//        XStream xstream = new XStream(new PureJavaReflectionProvider());
//        encryption = (Encryption)keysObj.getEncrptionClassObject();
//        String keyPairValue = keysObj.getValue (ConfigurationConstants.KEY_PAIR);
//        keypair = (KeyPair) xstream.fromXML(keyPairValue);


        //log activities
        if (logger.isDebugEnabled())
        	logger.debug("validating pin");

        String encryptedPin = null;
        if(wrapper.getObject("IS_MIGRATED") == null)
            encryptedPin = wrapper.getAccountInfoModel().getOldPin();
        String decryptedPin = null;

//        String keyPairValue = this.getConfiguration(new Long(
//                ConfigurationConstants.KEY_PAIR).toString());
        // here use RSA Decrypter classes
        //decryptedPin = RSAEncryption.doDecrypt(keypair.getPrivate(),encryptedPin);
        
        
        //decryptedPin = encryption.decrypt(keypair.getPrivate(), encryptedPin);
        /*********************************************************************************
		 * Updated by Soofia Faruq 
		 * AES Encryption Support
		 */		
		SecretKey aesKey = null;
		if (encryption instanceof AESEncryption) {
			String strKey = keysObj.getValue(ConfigurationConstants.AES_KEY);
//			aesKey = new SecretKeySpec(strKey.getBytes(), 0,
//					strKey.getBytes().length, "AES");
			
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] encodedKey = decoder.decodeBuffer(strKey);
	        aesKey = new SecretKeySpec(encodedKey,0,encodedKey.length, "AES"); 
		}
		
		if (keypair != null) {
			decryptedPin = encryption.decrypt(keypair.getPrivate(), encryptedPin);
		} else {
		    if(encryptedPin != null)
		        decryptedPin = encryption.decrypt(aesKey, encryptedPin);
		}
		/**********************************************************************************/
        

        Encoder encoder = (Encoder)this.getEncoderClassObject();
        String encodedPin = null;
        if(decryptedPin != null)
            encodedPin = encoder.doHash(decryptedPin);

        accountInfoModel = wrapper.getAccountInfoModel();
        String accountNick = accountInfoModel.getAccountNick();
        Long customerId = accountInfoModel.getCustomerId();
        accountInfoModel.setCustomerId(new Long(customerId));
        accountInfoModel.setAccountNick(accountNick);
        if(wrapper.getObject("IS_MIGRATED") != null && "1".equals((String) wrapper.getObject("IS_MIGRATED"))
                && wrapper.getObject("IS_FORCEFUL") != null && wrapper.getObject("IS_FORCEFUL").equals(""))
            accountInfoModel.setIsMigrated(1L);
        CustomList<AccountInfoModel>
                list = this.accountInfoDao.findByExample(accountInfoModel,null,null,exampleHolder);
        List resultSetList = list.getResultsetList();

        if (resultSetList.size() > 0) {
        	
            //log activities
        	if (logger.isDebugEnabled())
        		logger.debug("record exist in db for verification");

            AccountInfoModel localAccountInfoModel = new AccountInfoModel();
            localAccountInfoModel = (AccountInfoModel) resultSetList.get(0);
            /*String pinFromDB = localAccountInfoModel.getPin();
            
            if (logger.isDebugEnabled()){
            	logger.debug("DB PIN "+pinFromDB+" \n\nEncoded PIN "+encodedPin);
            }*/
            
            //setting record active status
            accountInfoModel.setActive(localAccountInfoModel.getActive());
            PayShieldDTO dto = new PayShieldDTO();
            dto.setPVV(localAccountInfoModel.getPvv());
            dto.setPAN(localAccountInfoModel.getPan());
            dto.setPIN(decryptedPin);
            logger.debug("PAN: " + dto.getPAN() +"  PVV: "+dto.getPVV());
            
            if(wrapper != null && wrapper.getObject("IS_MIGRATED") == null && (wrapper.getSkipPanCheck() == null || wrapper.getSkipPanCheck() == Boolean.FALSE)) { // TIN/OUT check ola balance Only

	            IPayShieldSwitchController iPayShieldSwitchController = getIntegrationSwitch();
	            dto = iPayShieldSwitchController.verifyPIN(dto);
	            
            } else if(wrapper != null && (wrapper.getSkipPanCheck() != null && wrapper.getSkipPanCheck() == Boolean.TRUE)) {
            	logger.info("Skipping Validate Pin -> 'Skip Pan Check' = "+wrapper.getSkipPanCheck());
            	dto.setResponseCode("00");
            }
            else if(wrapper.getObject("IS_MIGRATED") != null && "1".equals((String) wrapper.getObject("IS_MIGRATED")))
            {
                logger.info("Skipping Validate Pin -> Migrated Account");
                dto.setResponseCode("00");
            }
                        
            if (dto != null && dto.getResponseCode() != null && dto.getResponseCode().equals("00")) {
                //log activities
            	if (logger.isDebugEnabled())
            		logger.debug("both pins match correctly");

                accountInfoModel = localAccountInfoModel;
            }else{
            	logger.error("pins are not matched. Response Code:" + (dto == null ? " NIL. " : dto.getResponseCode()));
            	accountInfoModel = null;
    	
            }
            
           /* if ((pinFromDB != null) && (pinFromDB.equals(encodedPin))) {
                //log activities
            	if (logger.isDebugEnabled())
            		logger.debug("b oth pins match correctly");

                accountInfoModel = localAccountInfoModel;
            }else{
            	if (logger.isDebugEnabled())
            		logger.debug("pins are not matched");
            	accountInfoModel = null;
    	
            }*/
        } else {
            //log activities
        	if (logger.isDebugEnabled())
        		logger.debug("pins are not matched");

            accountInfoModel = null;
        }
        //log activities
        if (logger.isDebugEnabled())
        	logger.debug("validatePIN end ");

        return accountInfoModel;
    }

    
    public VeriflyBaseWrapper deactivatePIN(VeriflyBaseWrapper wrapper) throws
    Exception {
    	if (logger.isDebugEnabled())
    		logger.debug("<----START deactivatePIN()-->");
    	LogWrapper logWrapper = new LogWrapperImpl();
        LogModel logModel = new LogModel();
        AccountInfoModel accountInfoModel = new AccountInfoModel();
        LogDetailModel logDetailModel = new LogDetailModel ();
        VariflyValidator variflyValidator = new VariflyValidator ();
        
        logModel.setStartTime(new Date());
        logModel.setActionId(ActionConstants.DEACTIVATE_PIN);

        // loging input params.
        XStream xstream = new XStream(new PureJavaReflectionProvider());
        String xml = xstream.toXML(wrapper);
        xml = XMLUtil.replaceElementsUsingXPath(xml, ConfigurationConstants.REPLACE_COLUMNS_LIST);
        logModel.setInputParam(xml);
        ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setEnableLike(Boolean.FALSE);
		if (logger.isDebugEnabled())
			logger.debug("<---Decryption Completed-->");
        try{
        	if (logger.isDebugEnabled())
        		logger.debug("<--Going to validate Financial Info-->");
            logDetailModel.setActionId(ActionConstants.VALIDATING_FINANCIAL_INFO);
            if(!variflyValidator.isValidDataForDeactivatePin(wrapper)){
                logger.error("<--Invalid financial info-->");
            	throw new InvalidDataException (String.valueOf(FailureReasonConstants.INSUFFICIENT_DATA));
            }
            // get the values from received log model and set in the log model
            if (wrapper.getLogModel() !=  null){
            	if (wrapper.getLogModel().getCreatdByUserId() != null){
            		logModel.setCreatdByUserId(wrapper.getLogModel().getCreatdByUserId());
            	}
            	if (wrapper.getLogModel().getCreatedBy() != null){
            		logModel.setCreatedBy(wrapper.getLogModel().getCreatedBy());
            	}
            	
            }
            /// going to validate user
            if (logger.isDebugEnabled())
            	logger.debug("<--Going to validate User-->");
            logDetailModel.setActionId(ActionConstants.VALIDATING_USER);
            accountInfoModel.setCustomerId(wrapper.getAccountInfoModel().getCustomerId());
            accountInfoModel.setAccountNick(wrapper.getAccountInfoModel().getAccountNick());
            
            CustomList<AccountInfoModel>
            customList = this.accountInfoDao.findByExample(accountInfoModel,null,null,exampleHolder);

            List list = customList.getResultsetList();
            if (list.size() == 0){
            	if (logger.isDebugEnabled())
            		logger.debug("<--Record not found in DB-->");
            	throw new InvalidDataException (String.valueOf(FailureReasonConstants.RECORD_NOT_FOUND));
            }
            accountInfoModel = (AccountInfoModel) list.get(ConfigurationConstants.FIRST_INDEX); // get account model from list
            if (!accountInfoModel.getActive()){
            	logger.error("<--Record is already deactivated--->");
            	throw new InvalidDataException (String.valueOf(FailureReasonConstants.RECORD_ALREADY_DEACTIVATED));            	
            }
            // going to generate pin

            accountInfoModel.setActive(false);
            accountInfoModel.setUpdatedOn(new Date());
            if (logger.isDebugEnabled())
            	logger.debug("<----Going to Save account info-->");
            // Encrypr the data before saving in db
            accountInfoModel = this.accountInfoDao.saveOrUpdate(accountInfoModel);
            if (logger.isDebugEnabled())
            	logger.debug("<--Record updated successfully-->");
            wrapper = new VeriflyBaseWrapperImpl();
            AccountInfoModel accountInfoModelTemp = (AccountInfoModel)accountInfoModel.clone();
            //--------------------------------------decrypt here-----------------------------------
            accountInfoModelTemp = encryptionHandler.decrypt(accountInfoModelTemp);
            //-------------------------------------------------------------------------------------
             // logging output Params
            String outputParams = xstream.toXML(wrapper);
            outputParams = XMLUtil.replaceElementsUsingXPath(outputParams, ConfigurationConstants.REPLACE_COLUMNS_LIST);
            logModel.setOutputParam(outputParams);

            logDetailModel.setStatusId(StatusConstants.SUCCESS);
            logModel.addLogIdLogDetailModel(logDetailModel);
            logModel.setStatusId(StatusConstants.SUCCESS);
            logModel.setEndTime(new Date());

            logWrapper = new LogWrapperImpl();
            logWrapper.setLogModel(logModel);
            this.logManager.insertLogRequiresNewTransaction(logWrapper);
            wrapper.setErrorStatus(true);
            wrapper.setAccountInfoModel(accountInfoModelTemp);
            if (logger.isDebugEnabled())
            	logger.debug("<--Record deactivated successfully-->");

            
        }catch (InvalidDataException exp){
       // 	logger.error(StringUtils.prepareExceptionStackTrace(exp));
        	Long errorCode = Long.parseLong(exp.getMessage());
        	if (logger.isDebugEnabled())
        		logger.debug("Error Code-->" + errorCode);

            logDetailModel.setStatusId(StatusConstants.FAILURE);
            logDetailModel.setFailureReasonId(errorCode);

            logModel.addLogIdLogDetailModel(logDetailModel);
            logModel.setEndTime(new Date());
            logModel.setStatusId(StatusConstants.FAILURE);
            logModel.setFailureReasonId(errorCode);
        	
            wrapper = new VeriflyBaseWrapperImpl();
            wrapper.setErrorStatus(false);
            String errorMessage = this.getErrorMessages(new Long(errorCode).toString());
            wrapper.setErrorMessage(errorMessage);
                        
            logModel.setOutputParam(xstream.toXML(wrapper));
            logWrapper = new LogWrapperImpl();
            logWrapper.setLogModel(logModel);
            this.logManager.insertLogRequiresNewTransaction(logWrapper);
            if (logger.isDebugEnabled())
            	logger.debug("<-Record updated successfully-->");
        }
        
        if (logger.isDebugEnabled())
        	logger.debug("<--END deactivatePin()-->");        
        return wrapper;
    }

    private String generatePIN (Encoder encoder,LogDetailModel logDetailModel) throws Exception {
    	
    	String pin = RandomNumberGenerator.generatePin(getPinSize());
    	logDetailModel.setActionId(ActionConstants.GENERATE_RANDOM_NO);
    	if (pin == null){
        	logger.error("<-Error in generating random no--->") ;
        	throw new  InvalidDataException (String.valueOf(FailureReasonConstants.ERROR_GENERATING_RANDOM_NUMBER));
        }
    	logDetailModel.setActionId(ActionConstants.APPLYING_HASH_ALGORITHM);
    	pin = encoder.doHash(pin);
    	if (pin == null){
        	logger.error("<--------Error in generating random no------------------>") ;
        	throw new  InvalidDataException (String.valueOf(FailureReasonConstants.ERROR_GENERATING_RANDOM_NUMBER));
        }


    	return pin;
    }
    
    /**
     * This method generated a new PIN and Account in the System
     */
    public VeriflyBaseWrapper generatePIN(VeriflyBaseWrapper baseWrapper) throws  Exception 
    {
    	return this.generatePIN(baseWrapper,true);
    }
    
    /**TODO: Done..
     *  generate pin using integration and update pvv in accountInfo
     * This method generated a new PIN and Account in the System
     */
    public VeriflyBaseWrapper generatePIN(VeriflyBaseWrapper baseWrapper, boolean normalMode) throws  Exception {
    	if (logger.isDebugEnabled())
    		logger.debug("<--Start generatePIN--->" );
    	LogWrapper logWrapper = new LogWrapperImpl();
        LogModel logModel = new LogModel();
        LogDetailModel logDetailModel = new LogDetailModel ();
        String pin = null ;
        
        logModel.setStartTime(new Date());
        logModel.setActionId(ActionConstants.GENERATE_PIN);

        XStream xstream = new XStream(new PureJavaReflectionProvider());
        String xml = xstream.toXML(baseWrapper);
        xml = XMLUtil.replaceElementsUsingXPath(xml, ConfigurationConstants.REPLACE_COLUMNS_LIST);
        logModel.setInputParam(xml);

        Encoder encoder = (Encoder)this.getEncoderClassObject();
        Date nowDate = new Date();
 
        if (logger.isDebugEnabled())
        	logger.debug("<-Required Objects created successfully-->");  
        try{
        	if (logger.isDebugEnabled())
        		logger.debug("<--Going to peform validation-->");
        	logDetailModel.setActionId(ActionConstants.VALIDATING_FINANCIAL_INFO);
            variflyValidator.validateForGenerateModify(baseWrapper);
            
            logDetailModel.setActionId(ActionConstants.VALIDATING_USER);
            
            AccountInfoModel accountInfoModelTemp = new AccountInfoModel ();
            accountInfoModelTemp.setCustomerId(baseWrapper.getAccountInfoModel().getCustomerId());
            accountInfoModelTemp.setAccountNick(baseWrapper.getAccountInfoModel().getAccountNick());
            
            if (isExist(accountInfoModelTemp)){
            	if (logger.isDebugEnabled())
            		logger.debug("<-Record already exists->") ;
        		throw new InvalidDataException (String.valueOf(FailureReasonConstants.RECORD_ALREADY_EXISTS));
            }
            if (isExistByAccountNo(baseWrapper)){
            	if (logger.isDebugEnabled())
            		logger.debug("<-Record/AccountNo already exists-> A/C #"+baseWrapper.getAccountInfoModel().getAccountNo()) ;
        		throw new InvalidDataException(String.valueOf(FailureReasonConstants.ACCOUNT_NO_ALREADY_EXIST));
            }
            if(baseWrapper != null && (baseWrapper.getSkipPanCheck() == null || baseWrapper.getSkipPanCheck() == Boolean.FALSE)) { // Other than Link Account Case Only

                if (isExistByPan(baseWrapper)) {
                	logger.error("<-Record/PAN already exists-> A/C #"+baseWrapper.getAccountInfoModel().getPan()) ;
            		throw new InvalidDataException(String.valueOf(FailureReasonConstants.ACCOUNT_NO_ALREADY_EXIST));
                }
            }

            if (logger.isDebugEnabled())
            	logger.debug("<-Going to generate random number-->");
            logDetailModel.setActionId(ActionConstants.GENERATE_RANDOM_NO);
            
            /*if( normalMode )            	           	
            	pin = RandomNumberGenerator.generatePin(getPinSize());
            else
            	pin = this.messageSource.getMessage(VeriflyLiteConstants.VERIFLY_LITE_PIN_KEY, null, null);*/
            
            PayShieldDTO dto = new PayShieldDTO();
            dto.setPAN(baseWrapper.getAccountInfoModel().getPan());
            logger.debug("PAN : " + dto.getPAN());
            
            if(baseWrapper != null && (baseWrapper.getSkipPanCheck() == null || baseWrapper.getSkipPanCheck() == Boolean.FALSE)) { // Other than Link Account Case Only

                IPayShieldSwitchController iPayShieldSwitchController = getIntegrationSwitch();
                dto = iPayShieldSwitchController.generateSystemPIN(dto);
                
            } else if(baseWrapper != null && (baseWrapper.getSkipPanCheck() != null && baseWrapper.getSkipPanCheck() == Boolean.TRUE)) { // Link Account Case Only
            	dto.setResponseCode("00");
            }     

            if ((dto != null) && (dto.getResponseCode() != null && dto.getResponseCode().equals("00"))) {
            	pin = dto.getPIN();
            	baseWrapper.getAccountInfoModel().setPvv(dto.getPVV());
            	baseWrapper.getAccountInfoModel().setCreatedOn(nowDate);
            	baseWrapper.getAccountInfoModel().setUpdatedOn(new Date());
            	baseWrapper.getAccountInfoModel().setGeneratedPin(pin);
            	baseWrapper.getAccountInfoModel().setPin("-1");
                logDetailModel.setStatusId(StatusConstants.SUCCESS);
            }else{
            	logger.error("Could not Generate Pin. Response Code: " + (dto == null ? " NIL. " : dto.getResponseCode()) );
            	logDetailModel.setStatusId(StatusConstants.FAILURE);
                logDetailModel.setFailureReasonId(FailureReasonConstants.
                                                  ERROR_GENERATING_RANDOM_NUMBER);
                throw new  InvalidDataException (String.valueOf(FailureReasonConstants.ERROR_GENERATING_RANDOM_NUMBER));
    	
            }
            
            /*if (pin == null){
            	if (logger.isDebugEnabled())
            		logger.debug("<--Error in generating random no-->") ;
            	throw new  InvalidDataException (String.valueOf(FailureReasonConstants.ERROR_GENERATING_RANDOM_NUMBER));
            }

			baseWrapper.getAccountInfoModel().setGeneratedPin(pin);
            if (logger.isDebugEnabled())
            	logger.debug("<--Going to apply Hashing algorithm-->") ;
            logDetailModel.setActionId(ActionConstants.APPLYING_HASH_ALGORITHM);
            String encodedpin = encoder.doHash(pin);
            
            if (encodedpin == null){
            	throw new InvalidDataException (String.valueOf(FailureReasonConstants.ERROR_APPLYING_HASHING_ALGORITHM));
            }
            
            baseWrapper.getAccountInfoModel().setPin(encodedpin);
            baseWrapper.getAccountInfoModel().setCreatedOn(nowDate);
            baseWrapper.getAccountInfoModel().setUpdatedOn(nowDate);*/
            
            if (logger.isDebugEnabled())
            	logger.debug("<--Going to save record--->") ;
            logDetailModel.setActionId(ActionConstants.SAVING_OR_UPDATING_RECORD);
            accountInfoModelTemp = (AccountInfoModel)baseWrapper.getAccountInfoModel().clone();
            accountInfoModelTemp = encryptionHandler.encrypt(accountInfoModelTemp);
            
            accountInfoModelTemp.setActive(true);
            accountInfoModelTemp.setDeleted(false);
            this.accountInfoDao.saveOrUpdate(accountInfoModelTemp);


            logDetailModel.setStatusId(StatusConstants.SUCCESS);
            logModel.addLogIdLogDetailModel(logDetailModel);

            String savePin = baseWrapper.getAccountInfoModel().getPin();
            String saveGeneratedPin = baseWrapper.getAccountInfoModel().getGeneratedPin();
            baseWrapper.getAccountInfoModel().setPin(null);
            baseWrapper.getAccountInfoModel().setGeneratedPin(null);
            //creating xml of wrapper object to save in out parameters of log table
            baseWrapper.setErrorStatus(true);
            String output = xstream.toXML(baseWrapper);
            output = XMLUtil.replaceElementsUsingXPath(output, ConfigurationConstants.REPLACE_COLUMNS_LIST);
            logModel.setOutputParam(output);
            if (baseWrapper.getLogModel().getCreatedBy() != null && !baseWrapper.getLogModel().getCreatedBy().trim().equals("")){
            	logModel.setCreatedBy(baseWrapper.getLogModel().getCreatedBy());
            }
            
            if (baseWrapper.getLogModel().getCreatdByUserId() != null ){
            	logModel.setCreatdByUserId(baseWrapper.getLogModel().getCreatdByUserId());
            }

           

            //save original values in wrapper of pin and generated pin for send to client
            baseWrapper.getAccountInfoModel().setPin(savePin);
            baseWrapper.getAccountInfoModel().setGeneratedPin(saveGeneratedPin);
            
            
  
            logModel.setEndTime(new Date());
            logModel.setStatusId(StatusConstants.SUCCESS);
            logModel.setAccountInfoId(baseWrapper.getAccountInfoModel().getAccountInfoId());

            logWrapper.setLogModel(logModel);
            this.logManager.insertLogRequiresNewTransaction(logWrapper);
            baseWrapper.getAccountInfoModel().setAccountInfoId(accountInfoModelTemp.getAccountInfoId());
            if (logger.isDebugEnabled())
            	logger.debug("<--Records inserted successfully--->") ;

        }catch (InvalidDataException exp){
        	logger.error(exp.getStackTrace()) ;
        	// Write the code here to catch the Exception
        	Long errorCode = Long.parseLong(exp.getMessage());
        	if (logger.isDebugEnabled())
        		logger.debug("Error Code->" + errorCode) ;

            logDetailModel.setStatusId(StatusConstants.FAILURE);
            logDetailModel.setFailureReasonId(errorCode);

            logModel.addLogIdLogDetailModel(logDetailModel);
            logModel.setEndTime(new Date());
            logModel.setStatusId(StatusConstants.FAILURE);
            logModel.setFailureReasonId(errorCode);
        	
            baseWrapper = new VeriflyBaseWrapperImpl();
            baseWrapper.setErrorStatus(false);
            String errorMessage = this.getErrorMessages(new Long(errorCode).toString());
            baseWrapper.setErrorMessage(errorMessage);
                        
            logModel.setOutputParam(xstream.toXML(baseWrapper));
            logWrapper = new LogWrapperImpl();
            logWrapper.setLogModel(logModel);
            this.logManager.insertLogRequiresNewTransaction(logWrapper);
            if (logger.isDebugEnabled())
            	logger.debug("<----LOG and LOG Details records inserted successfully-->") ;
        }
        if (logger.isDebugEnabled())
        	logger.debug("<--End generatePin()-->") ;
    	return baseWrapper ;
    }

    
    
    /**
     * getLog
     *This method is used for viewing all the4 log details
     * depending on the serch criteria provided.
     * @param wrapper VeriflyBaseWrapper
     * @return VeriflyBaseWrapper
     * @todo Implement this com.inov8.verifly.manager.VeriflyManager method
     */
    public VeriflyBaseWrapper getLog(VeriflyBaseWrapper wrapper) throws
            Exception {
        wrapper = this.populateLogModel(wrapper);
        return wrapper;
    }

    /**
     * when "max" is set then this function returns maximum pin size.
     * when "min" is set then this function returns minimus pin size.
     * @return int
     */
    protected int getPinSize() {
        int pinLength = 0;
        String maxPinLength = this.getConfiguration(new Long(
                ConfigurationConstants.MAX_PIN_LENGTH).toString());
        pinLength = Integer.parseInt(maxPinLength);
        return pinLength;
    }

    /* Done: Unit tested from customer details screen (Regenerate Pin)
     * TODO: reset pin using integration and update pvv(non-Javadoc)
     * @see com.inov8.verifly.server.service.mainmodule.VeriflyManager#resetPIN(com.inov8.verifly.common.wrapper.VeriflyBaseWrapper)
     */
    public VeriflyBaseWrapper resetPIN(VeriflyBaseWrapper wrapper) throws
    Exception {
    	if (logger.isDebugEnabled())
    		logger.debug("<--start resetPIn()---> ");
    	Boolean isUserGeneratedPin = false;
    	String userGeneratedPin="";
    	if(wrapper.getObject(VeriflyConstants.IVR_USER_PIN) != null){
    		userGeneratedPin = (String) wrapper.getObject(VeriflyConstants.IVR_USER_PIN);
    		isUserGeneratedPin = true;
    	}
        LogWrapper logWrapper = new LogWrapperImpl();
        LogModel logModel = new LogModel();
        LogDetailModel logDetailModel = new LogDetailModel ();
        logModel.setStartTime(new Date());
        logModel.setActionId(ActionConstants.RESET_PIN);
        logModel.setStatusId(StatusConstants.IN_PROGRESS);

        XStream xstream = new XStream(new PureJavaReflectionProvider());
        String xml = xstream.toXML(wrapper) ;
        xml = XMLUtil.replaceElementsUsingXPath(xml,ConfigurationConstants.REPLACE_COLUMNS_LIST);
        logModel.setInputParam(xml);
        ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setEnableLike(Boolean.FALSE);
        try{
        logDetailModel.setActionId(ActionConstants.
                VALIDATING_FINANCIAL_INFO);
        
        	if (!variflyValidator.isValidDataForResetPin(wrapper)){
        		logger.error("<--Error insufficent data--> ");
        		throw new InvalidDataException (String.valueOf(FailureReasonConstants.INSUFFICIENT_DATA));
        	}
        	
            if(wrapper.getLogModel() != null ){
                if(wrapper.getLogModel().getCreatedBy() != null ){
                    logModel.setCreatedBy(wrapper.getLogModel().getCreatedBy());
                }
                if(wrapper.getLogModel().getCreatdByUserId() != null ){
                    logModel.setCreatdByUserId(wrapper.getLogModel().getCreatdByUserId());
                }
            }

            // validating if record exists or not.
            if (logger.isDebugEnabled())
            	logger.debug("<---Going to validate users--> ");
            logDetailModel.setActionId(ActionConstants.VALIDATING_USER);
            AccountInfoModel accountInfoModel = new AccountInfoModel();
            accountInfoModel.setCustomerId(wrapper.getAccountInfoModel().getCustomerId());
            accountInfoModel.setAccountNick(wrapper.getAccountInfoModel().getAccountNick());

            CustomList<AccountInfoModel>
                    customList = this.accountInfoDao.findByExample(accountInfoModel,null,null,exampleHolder);
            List list = customList.getResultsetList();

            if (list.size() == 0){
            	logger.error("<--Record not found--> ");
            	throw new InvalidDataException (String.valueOf(FailureReasonConstants.RECORD_NOT_FOUND));
            }
            
            
            accountInfoModel = (AccountInfoModel) list.get(0);
            if (!accountInfoModel.getActive()){
            	logger.error("<-INactive record--> ");
            	throw new InvalidDataException (String.valueOf(FailureReasonConstants.RECORD_IS_INACTIVE));            	
            }
            
            String newPin = null;
            PayShieldDTO dto = new PayShieldDTO();
            dto.setPAN(accountInfoModel.getPan());
            logger.debug("PAN: " + dto.getPAN());
            
            IPayShieldSwitchController iPayShieldSwitchController = getIntegrationSwitch();
            if(!isUserGeneratedPin){
            	dto = iPayShieldSwitchController.generateSystemPIN(dto);
            }else{
            	dto.setPIN(userGeneratedPin);
            	dto = iPayShieldSwitchController.generateUserPIN(dto);
            }
            
            if ((dto != null) && (dto.getResponseCode() != null && dto.getResponseCode().equals("00"))) {
            	newPin = dto.getPIN();
                accountInfoModel.setPvv(dto.getPVV());
                accountInfoModel.setUpdatedOn(new Date());
                wrapper.getAccountInfoModel().setGeneratedPin(newPin);
                logDetailModel.setStatusId(StatusConstants.SUCCESS);
            }else{
            		logger.error("[resetPin] Could not rest Pin. Response Code: " + (dto == null ? " NIL" : dto.getResponseCode()));
            	logDetailModel.setStatusId(StatusConstants.FAILURE);
                logDetailModel.setFailureReasonId(FailureReasonConstants.
                                                  ERROR_GENERATING_RANDOM_NUMBER);
                throw new  InvalidDataException (String.valueOf(FailureReasonConstants.ERROR_GENERATING_RANDOM_NUMBER));
            }
            
  
            /*String newPin = RandomNumberGenerator.generatePin(getPinSize());
            wrapper.getAccountInfoModel().setGeneratedPin(newPin);

            // log for generating random number pin.
            if (logger.isDebugEnabled())
            	logger.debug("<---Going to generate random no--> ");
            logDetailModel.setActionId(ActionConstants.GENERATE_RANDOM_NO);

            if (newPin != null) { // successful random number pin generation.
                logDetailModel.setStatusId(StatusConstants.SUCCESS);
            } else {
                // unsuccessfull random number pin generation.
                logDetailModel.setStatusId(StatusConstants.FAILURE);
                logDetailModel.setFailureReasonId(FailureReasonConstants.
                                                  ERROR_GENERATING_RANDOM_NUMBER);
            }
            
            
            // applying hash algorithm
            Encoder encoder = (Encoder)this.getEncoderClassObject();
            String encodedpin = encoder.doHash(newPin);

            // log for hashing algorithm.
            if (logger.isDebugEnabled())
            	logger.debug("<--Going to apply hash algorithm--> ");
            logDetailModel.setActionId(ActionConstants.APPLYING_HASH_ALGORITHM);
            if (encodedpin != null) { // successful hashing
                logDetailModel.setStatusId(StatusConstants.SUCCESS);
            } else {
                // unsuccessful hashing.
                logDetailModel.setStatusId(StatusConstants.FAILURE);
                logDetailModel.setFailureReasonId(FailureReasonConstants.
                                                  ERROR_APPLYING_HASHING_ALGORITHM);
            }*/



            // saving AccountInfoModel into DB.
            /*accountInfoModel.setUpdatedOn(new Date());
            accountInfoModel.setPin(encodedpin);*/
            if (logger.isDebugEnabled())
            	logger.debug("<--Going to save or update record--> ");

            accountInfoModel = this.accountInfoDao.saveOrUpdate(accountInfoModel);

            AccountInfoModel acountInfoModelTemp =(AccountInfoModel) accountInfoModel.clone();
//----------------------------------------------------Decrypt here--------------------------------------------
            acountInfoModelTemp = encryptionHandler.decrypt(acountInfoModelTemp);
//------------------------------------------------------------------------------------------------------------            
            //log activities
            if (logger.isDebugEnabled())
            	logger.debug("successfully saveUpdate record into db");

            // log for saving AccountInfoModel into DB.
            logDetailModel = new LogDetailModel();
            logDetailModel.setActionId(ActionConstants.
                                       SAVING_OR_UPDATING_RECORD);
            logDetailModel.setStatusId(StatusConstants.SUCCESS);

            logModel.addLogIdLogDetailModel(logDetailModel);

            wrapper.setErrorStatus(true);

            //ensure that readable pin not saved in log
            String savePin = wrapper.getAccountInfoModel().getPin();
            String saveGeneratedPin = wrapper.getAccountInfoModel().getGeneratedPin();
            wrapper.getAccountInfoModel().setPin(null);
            wrapper.getAccountInfoModel().setGeneratedPin(null);

            // logging output param
            String outputParams = xstream.toXML(wrapper);

            wrapper.setAccountInfoModel(acountInfoModelTemp);
            //save original values in wrapper of pin and generated pin for send to client
            wrapper.getAccountInfoModel().setPin(savePin);
            wrapper.getAccountInfoModel().setGeneratedPin(saveGeneratedPin);
            savePin = null;
            saveGeneratedPin = null;


            logModel.setEndTime(new Date());
            outputParams = XMLUtil.replaceElementsUsingXPath(outputParams, ConfigurationConstants.REPLACE_COLUMNS_LIST);
            logModel.setOutputParam(outputParams);
            logModel.setAccountInfoId(accountInfoModel.getAccountInfoId());
            logModel.setStatusId(StatusConstants.SUCCESS);

            logWrapper.setLogModel(logModel);
            this.logManager.insertLogRequiresNewTransaction(logWrapper);
            if (logger.isDebugEnabled())
            	logger.debug("<---Pin reset successful--> ");
	
        }catch (InvalidDataException exp) {
      //      logger.error(StringUtils.prepareExceptionStackTrace(exp));
        	Long errorCode = Long.parseLong(exp.getMessage());
            

            logDetailModel.setStatusId(StatusConstants.FAILURE);
            logDetailModel.setFailureReasonId(errorCode);

            logModel.addLogIdLogDetailModel(logDetailModel);
            logModel.setEndTime(new Date());
            logModel.setStatusId(StatusConstants.FAILURE);
            logModel.setFailureReasonId(errorCode);
        	
            wrapper = new VeriflyBaseWrapperImpl();
            wrapper.setErrorStatus(false);
            String errorMessage = this.getErrorMessages(new Long(errorCode).toString());
            wrapper.setErrorMessage(errorMessage);
                        
            logModel.setOutputParam(xstream.toXML(wrapper));
            logWrapper = new LogWrapperImpl();
            logWrapper.setLogModel(logModel);
            this.logManager.insertLogRequiresNewTransaction(logWrapper);
            if (logger.isDebugEnabled())
            	logger.debug("<--Failure record inserted sucessfully-->");

        }

        
    	return wrapper ;
    }
    
    
    /*
     * Done: this method calls validatePin() which is already integrated with hsm
     * (non-Javadoc)
     * @see com.inov8.verifly.server.service.mainmodule.VeriflyManager#verifyPIN(com.inov8.verifly.common.wrapper.VeriflyBaseWrapper)
     */
    public VeriflyBaseWrapper verifyPIN(VeriflyBaseWrapper wrapper) throws
    Exception {
    	
    	
    	
    	XStream xstream = new XStream(new PureJavaReflectionProvider());
        Encryption encryption = (Encryption)keysObj.getEncrptionClassObject();
        
		/*********************************************************************************
		 * Updated by Soofia Faruq AES Encryption Support
		 */
		KeyPair keypair = null;
		if (!(encryption instanceof AESEncryption)) {
			String keyPairValue = keysObj
					.getValue(ConfigurationConstants.KEY_PAIR);
			keypair = (KeyPair) xstream.fromXML(keyPairValue);
		} 
		/**********************************************************************************/

        
        /*String keyPairValue1 = keysObj.getValue (ConfigurationConstants.KEY_PAIR);
        KeyPair keypair1 = (KeyPair) xstream.fromXML(keyPairValue1);
    	
    	
        String str = encryption.encrypt(keypair1.getPublic(), "1111");*/
		
		
//    	System.out.println(str);
    	
    	if (logger.isDebugEnabled())
    		logger.debug("<--Start verifyPin()-->");

        LogWrapper logWrapper = new LogWrapperImpl();
        LogModel logModel = new LogModel();
        LogDetailModel logDetailModel = new LogDetailModel ();
        VariflyValidator variflyValidator = new VariflyValidator();
        
        logModel.setStartTime(new Date());
        logModel.setActionId(ActionConstants.VERIFY_PIN);
        logModel.setStatusId(StatusConstants.IN_PROGRESS);
        logModel.setTransactionCodeId(wrapper.getLogModel().getTransactionCodeId());
        
       
        
        if (logger.isDebugEnabled())
        logger.debug("<--Objects created successfully-->");        
       
        String xml = xstream.toXML(wrapper);
        xml = XMLUtil.replaceElementsUsingXPath(xml, ConfigurationConstants.REPLACE_COLUMNS_LIST);
        logModel.setInputParam(xml);
        ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setEnableLike(Boolean.FALSE);
        try{
        // validate the information
        	if (logger.isDebugEnabled())
        		logger.debug("<--Going to validate financial Info-->");
        	logDetailModel.setActionId(ActionConstants.VALIDATING_FINANCIAL_INFO);
        	if (!variflyValidator.isValidDataForVerifyPin(wrapper)){
        		if (logger.isDebugEnabled())
        			logger.debug("<--Invalid financial data-->");
        		throw new InvalidDataException (String.valueOf(FailureReasonConstants.INSUFFICIENT_DATA)); 
        	}
      
        	if (wrapper.getLogModel().getCreatedBy() != null )
        		logModel.setCreatedBy(wrapper.getLogModel().getCreatedBy());
        	
        	if (wrapper.getLogModel().getCreatdByUserId() != null )
        		logModel.setCreatdByUserId(wrapper.getLogModel().getCreatdByUserId());

        	if (logger.isDebugEnabled())
        		logger.debug("<--Going to validate user-->");
        	logDetailModel.setActionId(ActionConstants.VALIDATING_USER);        	
            AccountInfoModel accountInfoModel = new AccountInfoModel();
            accountInfoModel.setCustomerId(wrapper.getAccountInfoModel().getCustomerId());
            accountInfoModel.setAccountNick(wrapper.getAccountInfoModel().getAccountNick());
            CustomList<AccountInfoModel> customList = this.accountInfoDao.findByExample(accountInfoModel,null,null,exampleHolder);
            List <AccountInfoModel> list = customList.getResultsetList();
        	
            if (list.size() == 0){
//            	if (logger.isDebugEnabled())
            	logger.error("[VeriflyManagerImpl.verifyPin] Record not found in db for Verifly AccountInfo.customerID:" + wrapper.getAccountInfoModel().getCustomerId() + ". Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
            	throw new InvalidDataException (String.valueOf(FailureReasonConstants.RECORD_NOT_FOUND));
            }
            accountInfoModel = list.get(ConfigurationConstants.FIRST_INDEX);
            if (!accountInfoModel.getActive()){
            	logger.error("[VeriflyManagerImpl.verifyPin] Record is Inactive for Verifly AccountInfo.customerID:" + wrapper.getAccountInfoModel().getCustomerId() + ". Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
            	throw new InvalidDataException (String.valueOf(FailureReasonConstants.RECORD_IS_INACTIVE));
            }	

            logDetailModel.setStatusId(StatusConstants.SUCCESS);
            logModel.setAccountInfoId(accountInfoModel.getAccountInfoId());
            
            logDetailModel.setActionId(ActionConstants.VERIFY_PIN);
            if (logger.isDebugEnabled())
            	logger.debug("<--Going to validate pin-->");            
           accountInfoModel = validatePIN(wrapper,encryption,keypair);
            
            if (accountInfoModel == null){
                logger.error("[VeriflyManagerImpl.verifyPin] Incorrect Pin given for Verifly AccountInfo.customerID:" + wrapper.getAccountInfoModel().getCustomerId() + ". Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
            	throw new InvalidDataException (String.valueOf(FailureReasonConstants.INVALID_BANK_PIN_PLEASE_ENTER_VALID_BANK_PIN));
            } 	
            AccountInfoModel accountInfoModel1 = (AccountInfoModel) accountInfoModel.clone();
            accountInfoModel1 = encryptionHandler.decrypt(accountInfoModel1);
            //--------------------------------------------------------------------------------
            wrapper.setAccountInfoModel(accountInfoModel1);

            // logging output params.
            wrapper.setErrorStatus(true);
            String outputParams = xstream.toXML(wrapper);
            outputParams = XMLUtil.replaceElementsUsingXPath(outputParams,ConfigurationConstants.REPLACE_COLUMNS_LIST);
            logModel.setOutputParam(outputParams);

            // log activities
//            logger.debug("output parameters.. " + outputParams);

            logModel.addLogIdLogDetailModel(logDetailModel);
            logModel.setEndTime(new Date());
            logModel.setStatusId(StatusConstants.SUCCESS);

            logWrapper = new LogWrapperImpl();
            logWrapper.setLogModel(logModel);
            this.logManager.insertLogRequiresNewTransaction(logWrapper);
            if (logger.isDebugEnabled())
            	logger.debug("<--Records inserted successfully-->");
        
        }catch (InvalidDataException exp){
         //   logger.error(StringUtils.prepareExceptionStackTrace(exp));
        	Long errorCode = Long.parseLong(exp.getMessage());
            

            logDetailModel.setStatusId(StatusConstants.FAILURE);
            logDetailModel.setFailureReasonId(errorCode);

            logModel.addLogIdLogDetailModel(logDetailModel);
            logModel.setEndTime(new Date());
            logModel.setStatusId(StatusConstants.FAILURE);
            logModel.setFailureReasonId(errorCode);
        	
            wrapper = new VeriflyBaseWrapperImpl();
            wrapper.setErrorStatus(false);
            String errorMessage = this.getErrorMessages(new Long(errorCode).toString());
            wrapper.setErrorMessage(errorMessage);
            wrapper.setErrorCode(exp.getMessage());
                        
            logModel.setOutputParam(xstream.toXML(wrapper));
            logWrapper = new LogWrapperImpl();
            logWrapper.setLogModel(logModel);
            this.logManager.insertLogRequiresNewTransaction(logWrapper);
            if (logger.isDebugEnabled())
            	logger.debug("<--Failure record inserted sucessfully-->");
        }
        if (logger.isDebugEnabled())
        	logger.debug("<--END verifyPin()-->");	
    	return wrapper ;
    }
    

    public void setAccountInfoDao(AccountInfoDAO accountInfoDao) {
        this.accountInfoDao = accountInfoDao;
    }

    
    /**
     * This method modifys the Account Information
     * 
     */
    public VeriflyBaseWrapper modifyAccountInfo(VeriflyBaseWrapper wrapper) throws Exception {
    	if (logger.isDebugEnabled())
    		logger.debug("<---START modifyAccountInfo ()--->");
        LogWrapper logWrapper = new LogWrapperImpl();
        LogModel logModel = new LogModel();
        LogDetailModel logDetailModel = new LogDetailModel ();
        
        AccountInfoModel accountModel = new AccountInfoModel ();
        logModel.setStartTime(new Date());

        logModel.setActionId(ActionConstants.MODIFY_ACCOUNT_INFO);
        logModel.setStatusId(StatusConstants.IN_PROGRESS);

        XStream xstream = new XStream(new PureJavaReflectionProvider());
        String xml = xstream.toXML(wrapper) ;
        xml = XMLUtil.replaceElementsUsingXPath(xml, ConfigurationConstants.REPLACE_COLUMNS_LIST);
        logModel.setInputParam(xml);
        ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setEnableLike(Boolean.FALSE);
		if (logger.isDebugEnabled())
			logger.debug("<--Objects created successfully-->");

        try{
            if (wrapper.getLogModel().getCreatedBy() != null && !wrapper.getLogModel().getCreatedBy().trim().equals("")){
            	logModel.setCreatedBy(wrapper.getLogModel().getCreatedBy());
            }
            
            if (wrapper.getLogModel().getCreatdByUserId() != null ){
            	logModel.setCreatdByUserId(wrapper.getLogModel().getCreatdByUserId());
            }
            if (logger.isDebugEnabled())
            	logger.debug("<--Going to validate users-->");
        	logDetailModel.setActionId(ActionConstants.VALIDATING_FINANCIAL_INFO);
        	variflyValidator.validateForGenerateModify(wrapper);
        	if (logger.isDebugEnabled())
        		logger.debug("<--Validation successful-->");
        	logDetailModel.setActionId(ActionConstants.VALIDATING_USER);
        	AccountInfoModel accountInfoModelTemp = new AccountInfoModel ();
        	accountInfoModelTemp.setCustomerId(wrapper.getAccountInfoModel().getCustomerId());
        	accountInfoModelTemp.setAccountNick(wrapper.getAccountInfoModel().getAccountNick());
        	
            CustomList<AccountInfoModel>
            customList = this.accountInfoDao.findByExample(accountInfoModelTemp,null,null,exampleHolder);
            List list = customList.getResultsetList();
            
            if (list.size() == 0){
            	logger.error("<--Record not found in db for updation-->");
            	throw new InvalidDataException (String.valueOf(FailureReasonConstants.RECORD_NOT_FOUND));
            } 	
        	
            accountModel = (AccountInfoModel) list.get(ConfigurationConstants.FIRST_INDEX);
            if (logger.isDebugEnabled())
            	logger.debug("<--Going to decrypt the records-->");
            accountModel = encryptionHandler.decrypt(accountModel);

            
            if (!accountModel.getActive()){
            	logger.error("<--Record is in active-->");
            	throw new InvalidDataException (String.valueOf(FailureReasonConstants.RECORD_IS_INACTIVE));
            }	

            accountModel.setUpdatedOn(new Date());
            
            
            // set the values here somewhere
            accountModel = setValue(wrapper.getAccountInfoModel(), accountModel);
            // Here call the method to encrypt the information
            //----------------------------Added by Asif--------------------------------------------------
            accountModel = encryptionHandler.encrypt(accountModel);
//          ----------------------------Added by Asif--------------------------------------------------
            
            this.accountInfoDao.saveOrUpdate(accountModel);

            wrapper.setErrorStatus(true);

            logDetailModel.setStatusId(StatusConstants.SUCCESS);

            logModel.addLogIdLogDetailModel(logDetailModel);
            logModel.setEndTime(new Date());
            logModel.setStatusId(StatusConstants.SUCCESS);

            // log for output params.
            String outputParams = xstream.toXML(wrapper);
            outputParams = XMLUtil.replaceElementsUsingXPath(outputParams, ConfigurationConstants.REPLACE_COLUMNS_LIST);
            logModel.setOutputParam(outputParams);

            logWrapper = new LogWrapperImpl();
            logWrapper.setLogModel(logModel);
            this.logManager.insertLogRequiresNewTransaction(logWrapper);
            if (logger.isDebugEnabled())
            	logger.debug("<--Record inserted successfully--->");

            
            
        }catch (InvalidDataException exp){
        	
        //	logger.error(StringUtils.prepareExceptionStackTrace(exp));
        	Long errorCode = Long.parseLong(exp.getMessage());
        	if (logger.isDebugEnabled())
        		logger.debug("Error Code----->" + errorCode);

            logDetailModel.setStatusId(StatusConstants.FAILURE);
            logDetailModel.setFailureReasonId(errorCode);

            logModel.addLogIdLogDetailModel(logDetailModel);
            logModel.setEndTime(new Date());
            logModel.setStatusId(StatusConstants.FAILURE);
            logModel.setFailureReasonId(errorCode);
        	
            wrapper = new VeriflyBaseWrapperImpl();
            wrapper.setErrorStatus(false);
            String errorMessage = this.getErrorMessages(new Long(errorCode).toString());
            wrapper.setErrorMessage(errorMessage);
                        
            logModel.setOutputParam(xstream.toXML(wrapper));
            logWrapper = new LogWrapperImpl();
            logWrapper.setLogModel(logModel);
            this.logManager.insertLogRequiresNewTransaction(logWrapper);
            if (logger.isDebugEnabled())
            	logger.debug("<------Data for LOG amd LOG Details inserted [Failure record]--->");

        }
        if (logger.isDebugEnabled())
        	logger.debug("<--END modifyAccountInfo ()-->");
    	return wrapper ;
    }

    /**
     * This method modifies the Account Information of Agents (both core and BB)
     * First Name and Last Name only
     * 
     */
    public VeriflyBaseWrapper modifyAccountInfoForBBAgents(VeriflyBaseWrapper wrapper) throws Exception {
        LogWrapper logWrapper = new LogWrapperImpl();
        LogModel logModel = new LogModel();
        LogDetailModel logDetailModel = new LogDetailModel ();
        
        AccountInfoModel updatedAccountInfoModel = wrapper.getAccountInfoModel();
        logModel.setStartTime(new Date());
        logModel.setActionId(ActionConstants.MODIFY_ACCOUNT_INFO);
        logModel.setStatusId(StatusConstants.IN_PROGRESS);

        XStream xstream = new XStream(new PureJavaReflectionProvider());
        String xml = xstream.toXML(wrapper) ;
        xml = XMLUtil.replaceElementsUsingXPath(xml, ConfigurationConstants.REPLACE_COLUMNS_LIST);
        logModel.setInputParam(xml);
        ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setEnableLike(Boolean.FALSE);
		exampleHolder.setMatchMode(MatchMode.EXACT);
        try{
            if (wrapper.getLogModel().getCreatedBy() != null && !wrapper.getLogModel().getCreatedBy().trim().equals("")){
            	logModel.setCreatedBy(wrapper.getLogModel().getCreatedBy());
            }
            
            if (wrapper.getLogModel().getCreatdByUserId() != null ){
            	logModel.setCreatdByUserId(wrapper.getLogModel().getCreatdByUserId());
            }
        	logDetailModel.setActionId(ActionConstants.VALIDATING_FINANCIAL_INFO);

        	logDetailModel.setActionId(ActionConstants.VALIDATING_USER);
        	AccountInfoModel accountInfoModelTemp = new AccountInfoModel ();
        	accountInfoModelTemp.setCustomerId(updatedAccountInfoModel.getCustomerId());
        	accountInfoModelTemp.setCustomerMobileNo(updatedAccountInfoModel.getCustomerMobileNo());
        	accountInfoModelTemp.setDeleted(false);
        	accountInfoModelTemp.setActive(true);
//        	accountInfoModelTemp.setAccountNick(wrapper.getAccountInfoModel().getAccountNick());
        	
            CustomList<AccountInfoModel> customList = this.accountInfoDao.findByExample(accountInfoModelTemp,null,null,exampleHolder);
            List<AccountInfoModel> list = customList.getResultsetList();
            
            if (list.size() == 0){
            	logger.error("<--Record not found in db for updation-->");
            	throw new InvalidDataException (String.valueOf(FailureReasonConstants.RECORD_NOT_FOUND));
            } 	
        	
	        // For BB Agent Update All active Accounts against this CustomerID, Mobile Number
            for( AccountInfoModel accInfoModel : list ){

	        	if (updatedAccountInfoModel.getFirstName() != null && !updatedAccountInfoModel.getFirstName().trim().equals("")){
	        		accInfoModel.setFirstName(updatedAccountInfoModel.getFirstName());
	        	}

	        	if (updatedAccountInfoModel.getLastName() != null && !updatedAccountInfoModel.getLastName().trim().equals("")){
	        		accInfoModel.setLastName(updatedAccountInfoModel.getLastName());
	        	}

	        	accInfoModel.setUpdatedOn(new Date());
	            
	            this.accountInfoDao.saveOrUpdate(accInfoModel);
	        }
         
            wrapper.setErrorStatus(true);

            logDetailModel.setStatusId(StatusConstants.SUCCESS);

            logModel.addLogIdLogDetailModel(logDetailModel);
            logModel.setEndTime(new Date());
            logModel.setStatusId(StatusConstants.SUCCESS);

            // log for output params.
            String outputParams = xstream.toXML(wrapper);
            outputParams = XMLUtil.replaceElementsUsingXPath(outputParams, ConfigurationConstants.REPLACE_COLUMNS_LIST);
            logModel.setOutputParam(outputParams);

            logWrapper = new LogWrapperImpl();
            logWrapper.setLogModel(logModel);
            this.logManager.insertLogRequiresNewTransaction(logWrapper);
            
        }catch (InvalidDataException exp){
        	
        	Long errorCode = Long.parseLong(exp.getMessage());

            logDetailModel.setStatusId(StatusConstants.FAILURE);
            logDetailModel.setFailureReasonId(errorCode);

            logModel.addLogIdLogDetailModel(logDetailModel);
            logModel.setEndTime(new Date());
            logModel.setStatusId(StatusConstants.FAILURE);
            logModel.setFailureReasonId(errorCode);
        	
            wrapper = new VeriflyBaseWrapperImpl();
            wrapper.setErrorStatus(false);
            String errorMessage = this.getErrorMessages(new Long(errorCode).toString());
            wrapper.setErrorMessage(errorMessage);
                        
            logModel.setOutputParam(xstream.toXML(wrapper));
            logWrapper = new LogWrapperImpl();
            logWrapper.setLogModel(logModel);
            this.logManager.insertLogRequiresNewTransaction(logWrapper);
        }
    	
        return wrapper ;
    }

    
    private AccountInfoModel setValue (AccountInfoModel _fromAccountModel, AccountInfoModel _toAccountModel) {
 
    	if (_fromAccountModel.getCardNo() != null && !_fromAccountModel.getCardNo().trim().equals("")){
    		_toAccountModel.setCardNo(_fromAccountModel.getCardNo());
    	}
    	
    	if (_fromAccountModel.getCardExpiryDate() != null && !_fromAccountModel.getCardExpiryDate().trim().equals("")){
    		_toAccountModel.setCardExpiryDate(_fromAccountModel.getCardExpiryDate());
    	}

    	if (_fromAccountModel.getFirstName() != null && !_fromAccountModel.getFirstName().trim().equals("")){
    		_toAccountModel.setFirstName(_fromAccountModel.getFirstName());
    	}

    	if (_fromAccountModel.getLastName() != null && !_fromAccountModel.getLastName().trim().equals("")){
    		_toAccountModel.setLastName(_fromAccountModel.getLastName());
    	}

    	if (_fromAccountModel.getCustomerMobileNo() != null && !_fromAccountModel.getCustomerMobileNo().trim().equals("")){
    		_toAccountModel.setCustomerMobileNo(_fromAccountModel.getCustomerMobileNo());
    	}

    	if (_fromAccountModel.getComments() != null && !_fromAccountModel.getComments().trim().equals("")){
    		_toAccountModel.setComments(_fromAccountModel.getComments());
    	}
    	
    	if (_fromAccountModel.getAccountNo() != null && !_fromAccountModel.getAccountNo().trim().equals("")){
    		_toAccountModel.setAccountNo(_fromAccountModel.getAccountNo());
    	}

    	
    	if (_fromAccountModel.getDescription() != null && !_fromAccountModel.getDescription().trim().equals("")){
    		_toAccountModel.setDescription(_fromAccountModel.getDescription());
    	}

    	return _toAccountModel; 
    }

    public void setVeriflyConfigurationDao(VeriflyConfigurationDAO
                                           veriflyConfigurationDao) {
        this.veriflyConfigurationDao = veriflyConfigurationDao;
    }

    public void setLogManager(LogManager logManager) {
        this.logManager = logManager;
    }

    public void setFailureReasonDao(FailureReasonDAO failureReasonDao) {
        this.failureReasonDao = failureReasonDao;
    }

    public void setLogDao(LogDAO logDao) {
        this.logDao = logDao;
    }

    public void setLogDetailDao(LogDetailDAO logDetailDao) {
        this.logDetailDao = logDetailDao;
    }

    public void setPaymentModeDAO(PaymentModeDAO paymentModeDAO) {
        this.paymentModeDAO = paymentModeDAO;
    }

    public void setCardTypeDAO(CardTypeDAO cardTypeDAO) {
        this.cardTypeDAO = cardTypeDAO;
    }

    /**
     * This method is used for deleting all the details of a perticular account.
     * Depending upon the Account Nick and Customer Id passed. This method delets
     * all the details related to that account.
     * @param wrapper VeriflyBaseWrapper
     * @return VeriflyBaseWrapper
     * @throws Exception
     */
    public VeriflyBaseWrapper deleteAccount(VeriflyBaseWrapper wrapper) throws
            Exception {
        //log Activities
    	if (logger.isDebugEnabled())
    		logger.debug(" deleteAccount start  ");

        //LogWrapper logWrapper = new LogWrapperImpl();
        AccountInfoModel recievedAccountInfoModel = new AccountInfoModel();
        recievedAccountInfoModel = wrapper.getAccountInfoModel();

        // loging input params.
        //XStream xstream = new XStream(new PureJavaReflectionProvider());
        //String baseWrapperString = xstream.toXML(wrapper);
        ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setEnableLike(Boolean.FALSE);

        //log Activities
		//logger.debug(" input parameters..  " + baseWrapperString);

        //first need to validate required data coming from the client application is data is sufficient to proceed
        VariflyValidator variflyValidator = new VariflyValidator();
        if(!variflyValidator.isValidDataForDeleteAccount(recievedAccountInfoModel)){
            //message wrapper to be returned.
            wrapper = new VeriflyBaseWrapperImpl();
            wrapper.setErrorStatus(false);
            String errorMessage = this.getErrorMessages(new Long(
                    FailureReasonConstants.INSUFFICIENT_DATA).toString());
            wrapper.setErrorMessage(errorMessage);

            // return bcz there are error in validation
            return wrapper;
        }

        // validating if record exists or not.
        AccountInfoModel accountInfoModel = new AccountInfoModel();
        accountInfoModel.setAccountNick(recievedAccountInfoModel.getAccountNick());
        accountInfoModel.setCustomerId(recievedAccountInfoModel.getCustomerId());

        CustomList<AccountInfoModel>
                customList = this.accountInfoDao.findByExample(accountInfoModel,null,null,exampleHolder);

        List<AccountInfoModel> list = customList.getResultsetList();

        wrapper = new VeriflyBaseWrapperImpl();
        //String outputParams;

        if (list.size() > 0) {
            //log Activities
        	if (logger.isDebugEnabled())
        		logger.debug("record is exist in db");

            accountInfoModel = (AccountInfoModel) list.get(0);

            //first need to delete logDetail and Log table records of this accountInfo respectively

            LogDetailModel logDetailModelDel = null;
            LogModel logModelDel = new LogModel();
            //we need accountInfoId for delete accountInfo records in Log table
            logModelDel.setAccountInfoId(accountInfoModel.getAccountInfoId());

            CustomList<LogModel>
                    logModelDelList = this.logDao.findByExample(logModelDel);
            List resultLogModelList = logModelDelList.getResultsetList();
            LogModel logModelFromdb = null;

            //log activities
            if (logger.isDebugEnabled())
            	logger.debug("going to delete logDetail and log records");

            for (Iterator itr = resultLogModelList.iterator(); itr.hasNext(); ) {
                logModelFromdb = (LogModel) itr.next();
                logDetailModelDel = new LogDetailModel();
                //for delete from logDetail we need Log Table logId for respective accountInfo
                logDetailModelDel.setLogId(logModelFromdb.getLogId());
                this.logDetailDao.delete(logDetailModelDel);
                this.logDao.delete(logModelFromdb);
            }

            //log activities
            if (logger.isDebugEnabled())
            	logger.debug("LogDetail and Log records deleted successfully");

            this.accountInfoDao.delete(accountInfoModel);

            //log Activities
            if (logger.isDebugEnabled())
            	logger.debug("successfullt delete accountInfoModel");

            wrapper.setErrorStatus(true);
        } else {
            //log Activities
        	if (logger.isDebugEnabled())
        		logger.debug("record not found in database");

            wrapper.setErrorStatus(false);
            String errorMessage = this.getErrorMessages(new Long(
                    FailureReasonConstants.RECORD_NOT_FOUND).toString());
            wrapper.setErrorMessage(errorMessage);

        }

        //log Activities
        if (logger.isDebugEnabled())
        	logger.debug("deleteAcccount End ");

        return wrapper;
    }

    /**
     * This method called the logManager viewTransactionLod method that used for getting log information from db
     *
     * @param wrapper LogWrapper
     * @return LogWrapper
     * @throws Exception
     */
    public LogWrapper getLog(LogWrapper logWrapper) throws Exception {
        return this.logManager.viewTransactionLog(logWrapper);
    }

    /**
     * This methid is used for getting the encoded class object depending on the
     * information provided in DB.
     * @return Object
     */
    protected Object getEncoderClassObject() {
        Object object = null;
        String encoderClassName = this.getConfiguration(new Long(
                ConfigurationConstants.ENCODER_CLASS).toString());
        if (encoderClassName == null)
            encoderClassName = "com.inov8.verifly.common.encoder.NullEncoder";

        try {
            Class classDefinition = Class.forName(encoderClassName);
            object = classDefinition.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return object;

    }

    /**
     * This methid is used for getting the encrypted class object depending on the
     * information provided in DB.
     * @return Object
     */
    private Object getEncrptionClassObject() {
        Object object = null;
        String encrptorClassName = this.getConfiguration(new Long(
                ConfigurationConstants.ENCRYPTOR_CLASS).toString());
        if (encrptorClassName == null)
            encrptorClassName =
                    "com.inov8.verifly.common.encoder.NullEncryption";

        try {
            Class classDefinition = Class.forName(encrptorClassName);
            object = classDefinition.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return object;
    }

    /**
     * This method is used for populating the log model from extracting the values
     * from the wrapper which is passed from Microbank.
     * @param wrapper VeriflyBaseWrapper
     * @return VeriflyBaseWrapper
     */
    private VeriflyBaseWrapper populateLogModel(VeriflyBaseWrapper wrapper) throws
            Exception {
        LogListViewModel logListViewModel = new LogListViewModel();
        LogWrapper logWrapper = new LogWrapperImpl();
        if (wrapper.getObject("transactionId") != null &&
            (wrapper.getObject("transactionId").toString()).length() > 0) {

            logListViewModel.setTransactionCodeId(new Long(wrapper.getObject(
                    "transactionId").toString()));
        }
        if (wrapper.getObject("actionId") != null &&
            (wrapper.getObject("actionId").toString()).length() > 0) {
            logListViewModel.setActionId(new Long(wrapper.getObject("actionId").
                                                  toString()));
        }

        if (wrapper.getObject("actionInfoId") != null &&
            (wrapper.getObject("actionInfoId").toString()).length() > 0) {
            logListViewModel.setAccountInfoId(new Long(wrapper.getObject(
                    "actionInfoId").toString()));
        }
        if (wrapper.getObject("failureReasonId") != null &&
            (wrapper.getObject("failureReasonId").toString()).length() > 0) {
            logListViewModel.setFailureReasonId(new Long(wrapper.getObject(
                    "failureReasonId").toString()));
        }
        if (wrapper.getObject("statusId") != null &&
            (wrapper.getObject("statusId").toString()).length() > 0) {
            logListViewModel.setStatusId(new Integer(wrapper.getObject(
                    "statusId").toString()).intValue());
        }

        logWrapper.setLogListViewModel(logListViewModel);
        logWrapper = this.getLog(logWrapper);
        if ((logWrapper.getObject("Status").toString()).equals("Success")) {
            wrapper = new VeriflyBaseWrapperImpl();
            wrapper.setErrorStatus(true);
            wrapper.putObject("logWrapper", logWrapper);
        } else {
            wrapper = new VeriflyBaseWrapperImpl();
            wrapper.setErrorStatus(false);
        }
        return wrapper;
    }

    /**
     * This method is used for populating a Hash Map with all the Configuration
     * values present in data base.
     */
    private void prepareConfiguration() {
        CustomList<VeriflyConfigurationModel>
                customlist = this.veriflyConfigurationDao.findAll();
        List<VeriflyConfigurationModel> list = customlist.getResultsetList();
        for (VeriflyConfigurationModel veriflyConfigurationModel : list) {
            configurationHashMap.put(veriflyConfigurationModel.getPrimaryKey(),
                                     veriflyConfigurationModel.getValue());
        }
    }

    /**
     * This method is used for returning the required configuration from the populated
     * hash map depending upon the key passed
     * @param key  The key of the required configuration.
     * @return String
     */
    private String getConfiguration(String key) {
        this.prepareConfiguration();
        return configurationHashMap.get(Long.valueOf(key)).toString();
    }

    /**
     * This method is used for populating a Hash Map with all the Error Messages
     * present in data base.
     */
    private void prepareErrorMessages() {
        CustomList<VfFailureReasonModel>
                customlist = this.failureReasonDao.findAll();
        List<VfFailureReasonModel> list = customlist.getResultsetList();
        for (VfFailureReasonModel failureReasonModel : list) {
            failureReasonHashMap.put(failureReasonModel.getPrimaryKey(),
                                     failureReasonModel.getName());
        }
    }

    /**
     * This method is used for returning the required failure reason from the populated
     * hash map depending upon the key passed
     * @param key  The key of the required configuration.
     * @return Required failure reason.
     */
    protected String getErrorMessages(String key) {
        if (this.failureReasonHashMap == null ||
            this.failureReasonHashMap.size() == 0) {
            this.prepareErrorMessages();
        }
        return failureReasonHashMap.get(Long.valueOf(key)).toString();
    }

    /**
     * This method is check for existance of AccountInfo Models
     *
     * @accInfoModel  model that need to check for existance
     * @return  true if record exist else false return.
     */
    protected boolean isExist(AccountInfoModel accInfoModel) {
    	
    	ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setEnableLike(Boolean.FALSE);
        CustomList<AccountInfoModel>
                cusList = this.accountInfoDao.findByExample(accInfoModel,null,null,exampleHolder);
        if (cusList.getResultsetList().isEmpty())
            return false;
        else
            return true;
    }
    
    
    /**
     * @param baseWrapper
     * @return
     * @throws Exception
     */
    protected Boolean isExistByAccountNo(VeriflyBaseWrapper baseWrapper) throws Exception {

        AccountInfoModel inputAccountInfoModel = baseWrapper.getAccountInfoModel();
    	AccountInfoModel accountInfoModel = new AccountInfoModel();
    	accountInfoModel.setAccountNo(inputAccountInfoModel.getAccountNo());
    	if(inputAccountInfoModel.getPaymentModeId() != null)
    	    accountInfoModel.setPaymentModeId(inputAccountInfoModel.getPaymentModeId());
        encryptionHandler.encrypt(accountInfoModel);
        
    	ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
    	exampleHolder.setMatchMode(MatchMode.EXACT);
    	
        CustomList<AccountInfoModel> customList = accountInfoDao.findByExample(accountInfoModel, null, null, exampleHolder);

        return !(customList.getResultsetList().isEmpty());
    }
    
    /**
     * @param baseWrapper
     * @return
     * @throws Exception
     */
    protected Boolean isExistByPan(VeriflyBaseWrapper baseWrapper) throws Exception {
    	
    	AccountInfoModel accountInfoModel = new AccountInfoModel();
    	accountInfoModel.setPan(baseWrapper.getAccountInfoModel().getPan());
        
    	ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
    	exampleHolder.setMatchMode(MatchMode.EXACT);
    	
        CustomList<AccountInfoModel> customList = accountInfoDao.findByExample(accountInfoModel, null, null, exampleHolder);

        return !(customList.getResultsetList().isEmpty());
    }

    /**
     * This method is check for existance of paymentMode Models
     *
     * @paymentModeModel  model that need to check for existance
     * @return  true if record exist else false return.
     */
    private boolean isPaymentModeExistInDB(long paymentModep) {
        try {
            VfPaymentModeModel paymentModeModel = this.paymentModeDAO.
                                                findByPrimaryKey(paymentModep);
            if (paymentModeModel == null) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            e = null;
            return false;
        }
    }


    /**
     * This method is check for existance of cardTypeModel Models
     *
     * @cardTypeModel  model that need to check for existance
     * @return  true if record exist else false return.
     */
    private boolean isCardTypeExistInDB(long cardTypep) {
        try {
            VfCardTypeModel cardTypeModel = this.cardTypeDAO.findByPrimaryKey(
                    cardTypep);
            if (cardTypeModel == null) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            e = null;
            return false;
        }
    }

	public EncryptionHandler getEncryptionHandler() {
		return encryptionHandler;
	}

	public void setEncryptionHandler(EncryptionHandler encryptionHandler) {
		this.encryptionHandler = encryptionHandler;
	}
	public ConfigurationContainer getKeysObj() {
		return keysObj;
	}
	public void setKeysObj(ConfigurationContainer keysObj) {
		this.keysObj = keysObj;
	}
	
	public void setMessageSource(MessageSource messageSource)
	{
		this.messageSource = messageSource;
	}
	
	/*
	 * Done: No need to cahnge this method. This does not even verify pin.
	 * (non-Javadoc)
	 * @see com.inov8.verifly.server.service.mainmodule.VeriflyManager#verifyCredentials(com.inov8.verifly.common.wrapper.VeriflyBaseWrapper)
	 */
	public VeriflyBaseWrapper verifyCredentials(VeriflyBaseWrapper wrapper) throws
    Exception {
    	
    	
    	
    	XStream xstream = new XStream(new PureJavaReflectionProvider());
        Encryption encryption = (Encryption)keysObj.getEncrptionClassObject();
        
//        String keyPairValue = keysObj.getValue (ConfigurationConstants.KEY_PAIR);
//        KeyPair keypair = (KeyPair) xstream.fromXML(keyPairValue);
//        
//        String keyPairValue1 = keysObj.getValue (ConfigurationConstants.KEY_PAIR);
//        KeyPair keypair1 = (KeyPair) xstream.fromXML(keyPairValue1);
//    	
//    	
//        String str = encryption.encrypt(keypair1.getPublic(), "1111");
//    	System.out.println(str);
    	
    	if (logger.isDebugEnabled())
    		logger.debug("<--Start verifyCredentials(VeriflyBaseWrapper wrapper)-->");

        LogWrapper logWrapper = new LogWrapperImpl();
        LogModel logModel = new LogModel();
        LogDetailModel logDetailModel = new LogDetailModel ();
        VariflyValidator variflyValidator = new VariflyValidator();
        
        logModel.setStartTime(new Date());
        logModel.setActionId(ActionConstants.VERIFY_CREDENTIALS_WITHOUT_PIN);
        logModel.setStatusId(StatusConstants.IN_PROGRESS);
        logModel.setTransactionCodeId(wrapper.getLogModel().getTransactionCodeId());
       
        
        if (logger.isDebugEnabled())
        logger.debug("<--Objects created successfully-->");        
       
        String xml = xstream.toXML(wrapper);
        xml = XMLUtil.replaceElementsUsingXPath(xml, ConfigurationConstants.REPLACE_COLUMNS_LIST);
        logModel.setInputParam(xml);
        ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setEnableLike(Boolean.FALSE);
        try{
        // validate the information
        	if (logger.isDebugEnabled())
        		logger.debug("<--Going to validate financial Info-->");
        	logDetailModel.setActionId(ActionConstants.VALIDATING_FINANCIAL_INFO);
        	if (!variflyValidator.isValidDataForVerifyCredentials(wrapper)){
        		if (logger.isDebugEnabled())
        			logger.debug("<--Invalid financial data-->");
        		throw new InvalidDataException (String.valueOf(FailureReasonConstants.INSUFFICIENT_DATA)); 
        	}
      
        	if (wrapper.getLogModel().getCreatedBy() != null )
        		logModel.setCreatedBy(wrapper.getLogModel().getCreatedBy());
        	
        	if (wrapper.getLogModel().getCreatdByUserId() != null )
        		logModel.setCreatdByUserId(wrapper.getLogModel().getCreatdByUserId());

        	if (logger.isDebugEnabled())
        		logger.debug("<--Going to validate user-->");
        	logDetailModel.setActionId(ActionConstants.VALIDATING_USER);        	
            AccountInfoModel accountInfoModel = new AccountInfoModel();
            accountInfoModel.setCustomerId(wrapper.getAccountInfoModel().getCustomerId());
            accountInfoModel.setAccountNick(wrapper.getAccountInfoModel().getAccountNick());
            CustomList<AccountInfoModel> customList = this.accountInfoDao.findByExample(accountInfoModel,null,null,exampleHolder);
            List <AccountInfoModel> list = customList.getResultsetList();
        	
            if (list.size() == 0){
            	if (logger.isDebugEnabled())
            		logger.debug("<--Record not found in db-->");
            	throw new InvalidDataException (String.valueOf(FailureReasonConstants.RECORD_NOT_FOUND));
            }
            accountInfoModel = list.get(ConfigurationConstants.FIRST_INDEX);
            if (!accountInfoModel.getActive()){
                logger.error("<--Record is inactive-->");
            	throw new InvalidDataException (String.valueOf(FailureReasonConstants.RECORD_IS_INACTIVE));
            }	

            logDetailModel.setStatusId(StatusConstants.SUCCESS);
            logModel.setAccountInfoId(accountInfoModel.getAccountInfoId());
            //-------------------------------------Remove starts here---------------------------
            
            logDetailModel.setActionId(ActionConstants.VERIFY_CREDENTIALS_WITHOUT_PIN);
//            if (logger.isDebugEnabled())
//            	logger.debug("<--Going to validate pin-->");            
//           accountInfoModel = validatePIN(wrapper,encryption,keypair);
//            
//            if (accountInfoModel == null){
//                logger.error("<--Incorrect Pin given-->");
//            	throw new InvalidDataException (String.valueOf(FailureReasonConstants.INVALID_BANK_PIN_PLEASE_ENTER_VALID_BANK_PIN));
//            } 	
            AccountInfoModel accountInfoModel1 = (AccountInfoModel) accountInfoModel.clone();
            accountInfoModel1 = encryptionHandler.decrypt(accountInfoModel1);
            //--------------------------------------------------------------------------------
            wrapper.setAccountInfoModel(accountInfoModel1);

            // logging output params.
            wrapper.setErrorStatus(true);
            String outputParams = xstream.toXML(wrapper);
            outputParams = XMLUtil.replaceElementsUsingXPath(outputParams,ConfigurationConstants.REPLACE_COLUMNS_LIST);
            logModel.setOutputParam(outputParams);

            // log activities
//            logger.debug("output parameters.. " + outputParams);

            logModel.addLogIdLogDetailModel(logDetailModel);
            logModel.setEndTime(new Date());
            logModel.setStatusId(StatusConstants.SUCCESS);

            logWrapper = new LogWrapperImpl();
            logWrapper.setLogModel(logModel);
            this.logManager.insertLogRequiresNewTransaction(logWrapper);
            if (logger.isDebugEnabled())
            	logger.debug("<--Records inserted successfully-->");
        
        }catch (InvalidDataException exp){
         //   logger.error(StringUtils.prepareExceptionStackTrace(exp));
        	Long errorCode = Long.parseLong(exp.getMessage());
        	exp.printStackTrace();

            logDetailModel.setStatusId(StatusConstants.FAILURE);
            logDetailModel.setFailureReasonId(errorCode);

            logModel.addLogIdLogDetailModel(logDetailModel);
            logModel.setEndTime(new Date());
            logModel.setStatusId(StatusConstants.FAILURE);
            logModel.setFailureReasonId(errorCode);
        	
            wrapper = new VeriflyBaseWrapperImpl();
            wrapper.setErrorStatus(false);
            String errorMessage = this.getErrorMessages(new Long(errorCode).toString());
            wrapper.setErrorMessage(errorMessage);
                        
            logModel.setOutputParam(xstream.toXML(wrapper));
            logWrapper = new LogWrapperImpl();
            logWrapper.setLogModel(logModel);
            this.logManager.insertLogRequiresNewTransaction(logWrapper);
            if (logger.isDebugEnabled())
            	logger.debug("<--Failure record inserted sucessfully-->");
        }
        if (logger.isDebugEnabled())
        	logger.debug("<--END verifyPin()-->");	
    	return wrapper ;
    }


	/* (non-Javadoc)
	 * @see com.inov8.verifly.server.service.mainmodule.VeriflyManager#getAccountNumber(java.lang.Long)
	 */
	public String getAccountNumber(Long customerId) throws Exception  {
				
        ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setEnableLike(Boolean.FALSE);		
		
        AccountInfoModel accountInfoModel = this.getAccountInfoModel(customerId, 0L);
        
        return accountInfoModel.getAccountNo();
	}
	
	protected IPayShieldSwitchController getIntegrationSwitch()  throws InvalidDataException{
		String urlStr = this.getConfiguration(new Long(ConfigurationConstants.AUTHENTICATION_URL).toString());
		return HttpInvokerUtil.getHttpInvokerFactoryBean(IPayShieldSwitchController.class, urlStr);
	}
	
	
public AccountInfoModel getAccountInfoModel(Long customerId, Long paymentModeId) throws Exception  {
	    AccountInfoModel accountInfoModel = accountInfoDao.getAccountInfoModel(customerId,paymentModeId);
        if(accountInfoModel != null) {
            if (accountInfoModel.getActive()) {
            	accountInfoModel = encryptionHandler.decrypt((AccountInfoModel) accountInfoModel.clone());
            } else {
            	throw new InvalidDataException (String.valueOf(FailureReasonConstants.RECORD_IS_INACTIVE));        	
            }
        } else {
        	throw new InvalidDataException (String.valueOf(FailureReasonConstants.RECORD_NOT_FOUND));        	
        }
        return accountInfoModel;
	}

	
	
	public AccountInfoModel getAccountInfoModel(Long customerId, String accountNick) throws Exception  {
		
        AccountInfoModel accountInfoModel = new AccountInfoModel();
        accountInfoModel.setCustomerId(customerId);
        accountInfoModel.setAccountNick(accountNick);
        accountInfoModel.setActive(Boolean.TRUE);
		
        ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setEnableLike(Boolean.FALSE);
		exampleHolder.setMatchMode(MatchMode.EXACT);
        
        CustomList<AccountInfoModel> customList = accountInfoDao.findByExample(accountInfoModel, null, null, exampleHolder);
        List <AccountInfoModel> list = customList.getResultsetList();
        AccountInfoModel accountInfoModel1 = null;
        if(list.size() > 0) {
        	
            accountInfoModel = list.get(ConfigurationConstants.FIRST_INDEX);
            accountInfoModel1 = (AccountInfoModel) accountInfoModel.clone();
            if (accountInfoModel.getActive()) {

                accountInfoModel1 = encryptionHandler.decrypt(accountInfoModel1);
            	
            } else {
            
            	throw new InvalidDataException (String.valueOf(FailureReasonConstants.RECORD_IS_INACTIVE));        	
            }
            
        } else {
        	
        	throw new InvalidDataException (String.valueOf(FailureReasonConstants.RECORD_NOT_FOUND));        	
        }
        return accountInfoModel1;
	}


}