package com.inov8.verifly.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.util.CustomList;
import com.inov8.verifly.common.constants.AccountInfoConstants;
import com.inov8.verifly.common.constants.FailureReasonConstants;
import com.inov8.verifly.common.constants.LogConstants;
import com.inov8.verifly.common.constants.PaymentModeConstants;
import com.inov8.verifly.common.des.EncryptionHandler;
import com.inov8.verifly.common.exceptions.InvalidDataException;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.model.LogModel;
import com.inov8.verifly.common.model.VfCardTypeModel;
import com.inov8.verifly.common.model.VfPaymentModeModel;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;
import com.inov8.verifly.server.dao.mainmodule.AccountInfoDAO;
import com.inov8.verifly.server.dao.mainmodule.CardTypeDAO;
import com.inov8.verifly.server.dao.mainmodule.PaymentModeDAO;


/**
 * INOV8 INC
 * <p>
 * VariflyValidator validate accountInfoModel for sufficient fields.
 *
 *
 * @author Basit Mehr
 * @version 1.0
 */

public class VariflyValidator {

    //create required message objects
    MessageObject messageObject = new MessageObject();
    private AccountInfoDAO accountInfoDao;
    private PaymentModeDAO paymentModeDAO;
    private CardTypeDAO cardTypeDAO;
    private EncryptionHandler encryptionHandler;
    private final Log logger = LogFactory.getLog(this.getClass());

    public EncryptionHandler getEncryptionHandler() {
		return encryptionHandler;
	}


	public void setEncryptionHandler(EncryptionHandler encryptionHandler) {
		this.encryptionHandler = encryptionHandler;
	}


	public CardTypeDAO getCardTypeDAO() {
		return cardTypeDAO;
	}


	public void setCardTypeDAO(CardTypeDAO cardTypeDAO) {
		this.cardTypeDAO = cardTypeDAO;
	}


	public AccountInfoDAO getAccountInfoDao() {
		return accountInfoDao;
	}


	public void setAccountInfoDao(AccountInfoDAO accountInfoDao) {
		this.accountInfoDao = accountInfoDao;
	}


	
	
	public boolean isValidDataForChangeAccountNick (VeriflyBaseWrapper wrapper) {
		AccountInfoModel accountInfoModel = wrapper.getAccountInfoModel();
		boolean isValid = true ;
		if (accountInfoModel.getCustomerId() == null || accountInfoModel.getCustomerId() > AccountInfoConstants.CUSTOMER_ID_MAX_RANGE)
			isValid = false;
		else if (accountInfoModel.getAccountNick() == null || accountInfoModel.getAccountNick().trim().equals("")
				|| accountInfoModel.getAccountNick().length() > AccountInfoConstants.ACCOUNT_NICK_LENGTH){
			isValid = false ;
		}else if (accountInfoModel.getNewAccountNick() == null || accountInfoModel.getNewAccountNick().trim().equals("")){
			isValid = false ;
		}
		
		return isValid ;
	}
	/**
     *  Validate accountInfoModel for sufficient data
     *
     * @param accountInfoModel  validate their fields
     * @return boolean if all valid return true else false
     */
    public MessageObject validateForGenerateModify(VeriflyBaseWrapper baseWrapper) throws Exception{
        //getting accountInfoModel
    	logger.info("<--------Start validateForGenerateModify()------------------------>");
        AccountInfoModel accountInfoModel = baseWrapper.getAccountInfoModel();
        LogModel logModel = baseWrapper.getLogModel();


        //first need check payment mode
        //Long paymentModeId = accountInfoModel.getPaymentModeId()==null?0:accountInfoModel.getPaymentModeId();

        // if the payment mode is null or empty throw exception from here [query db to get payment modes]
        if (accountInfoModel.getPaymentModeId() == null){
        	logger.info("Payment Mode------------------>" + accountInfoModel.getPaymentModeId());
        	throw new InvalidDataException (String.valueOf(FailureReasonConstants.INCORRECT_PAYMENT_MODE));
        }	
        
        if (!isPaymentModeExistInDB(accountInfoModel.getPaymentModeId())){
        	logger.info("Invalid Payment Mode------------------>" + accountInfoModel.getPaymentModeId());
        	throw new InvalidDataException (String.valueOf(FailureReasonConstants.INCORRECT_PAYMENT_MODE));
        }	
        //identify mode of request mean Credit, Debit or Bank account
        if (accountInfoModel.getPaymentModeId()== PaymentModeConstants.CREDIT ||
        		accountInfoModel.getPaymentModeId() == PaymentModeConstants.DEBIT) {
        	logger.info("<------Payment Mode is Card------------------>");
        	logger.info("Card Type Id------------------>" + accountInfoModel.getCardTypeId());
            //incase of mode card these fields cannot be null in any case
            if ((accountInfoModel.getCardNo() == null || accountInfoModel.getCardNo().trim().length() < 1) ||
                (accountInfoModel.getCardExpiryDate() == null || accountInfoModel.getCardExpiryDate().trim().length() < 1) ||
                accountInfoModel.getCardTypeId() == null) {
            	throw new InvalidDataException (String.valueOf(FailureReasonConstants.INCORRECT_DATA_FOR_CARD_NO));
               
            }else{
            	if (!isCardTypeExistInDB(accountInfoModel.getCardTypeId())){
            		logger.info("Invalid Card Type Id------------------>" + accountInfoModel.getCardTypeId());
            		throw new InvalidDataException (String.valueOf(FailureReasonConstants.INCORRECT_DATA_FOR_CARD_NO));
            	}	
                

            }
            //check maximum length for card number
            if (accountInfoModel.getCardNo().length() > AccountInfoConstants.CARD_NO_LENGTH) {
            	logger.info("Invalid Card Length------------------>" + accountInfoModel.getCardNo().length());
            	throw new InvalidDataException (String.valueOf(FailureReasonConstants.INCORRECT_FIELD_LENGTH));
            }

            //check max range value of long field
            
            //check validity of date
            //ensure that date must be valid and must be current or greater date from current date
            if (!VeriflyUtility.isValidDate(accountInfoModel.getCardExpiryDate()) ||
                VeriflyUtility.isBeforeDateFromCurrentDate(
                		accountInfoModel.getCardExpiryDate())) {
            	logger.info("Invalid Card Expiry------------------>" + accountInfoModel.getCardExpiryDate());
            	throw new InvalidDataException (String.valueOf(FailureReasonConstants.INCORRECT_CARD_EXPIRED_DATE));
            }
            
        	AccountInfoModel accountInfoModelCardNo = new AccountInfoModel();
            accountInfoModelCardNo.setCardNo(accountInfoModel.getCardNo());

            accountInfoModelCardNo.setCardNo(encryptionHandler.encrypt(accountInfoModelCardNo.getCardNo()));
            
            if (isExist(accountInfoModelCardNo)){
            	logger.info("Card No already exists------------------>");
        		throw new InvalidDataException (String.valueOf(FailureReasonConstants.CARD_NO_ALREADY_EXIST));
            }	


        } else if (accountInfoModel.getPaymentModeId() == PaymentModeConstants.BANK_ACCOUNT) {
            //incase if the mode is account then account no cannot be null
        	logger.info("Payment Mode is Bank A/C------------------>" );
            if (accountInfoModel.getAccountNo() == null || accountInfoModel.getAccountNo().trim().length() < 1) {
            	logger.info("Invalid Bank A/C------------------>" + accountInfoModel.getAccountNo());
            	throw new InvalidDataException (String.valueOf(FailureReasonConstants.INCORRECT_DATA_FOR_ACCOUNT_NO));
            }

            //checking account no maximum length should not exceed from db
            if (accountInfoModel.getAccountNo().length() > AccountInfoConstants.ACCOUNT_NO_LENGTH) {
            	logger.info("Invalid Account Length------------------>" + accountInfoModel.getAccountNo().length());
            	throw new InvalidDataException (String.valueOf(FailureReasonConstants.INCORRECT_FIELD_LENGTH));
            }

            AccountInfoModel accountInfoModelAccountNo = new AccountInfoModel();
            accountInfoModelAccountNo.setAccountNo(accountInfoModel.getAccountNo());

            accountInfoModelAccountNo.setAccountNo(encryptionHandler.encrypt(accountInfoModelAccountNo.getAccountNo()));
            
            if (isExist(accountInfoModelAccountNo)){
            	logger.info("<--------Account No already exists------------------>") ;
            	throw new InvalidDataException (String.valueOf(FailureReasonConstants.ACCOUNT_NO_ALREADY_EXIST));
            }	

        } 

        //checking necessary fields for null values these field requires in both card and account mode
        if (accountInfoModel.getCustomerId() == null ||
            (accountInfoModel.getAccountNick()== null || accountInfoModel.getAccountNick().trim().length() < 1) ||
            (accountInfoModel.getFirstName() == null || accountInfoModel.getFirstName().trim().length() < 1) ||
            (accountInfoModel.getLastName() == null || accountInfoModel.getLastName().trim().length() < 1) ||
            (accountInfoModel.getCustomerMobileNo() == null || accountInfoModel.getCustomerMobileNo().trim().length() < 1)) {

        	throw new InvalidDataException (String.valueOf(FailureReasonConstants.INSUFFICIENT_DATA));
        }

        //if these all fields have required data now need to check that coming data cannot exceeD FROM db fields lengths
        //checking string lengths
        if (accountInfoModel.getAccountNick().length() > AccountInfoConstants.ACCOUNT_NICK_LENGTH ||
        		accountInfoModel.getFirstName().length() > AccountInfoConstants.FIRST_NAME_LENGTH ||
        		accountInfoModel.getLastName().length() > AccountInfoConstants.LAST_NAME_LENGTH ||
        		accountInfoModel.getCustomerMobileNo().length() >
            AccountInfoConstants.CUSTOMER_MOBILE_NO_LENGTH ||
            (accountInfoModel.getComments() != null &&
            		accountInfoModel.getComments().length() > AccountInfoConstants.COMMENTS_LENGTH) ||
            (accountInfoModel.getDescription() != null &&
            		accountInfoModel.getDescription().length() > AccountInfoConstants.DESCRIPTION_LENGTH) ||
            (logModel.getCreatedBy() != null &&
            		logModel.getCreatedBy().length() > LogConstants.CREATED_BY_LENGTH)) {

        	throw new InvalidDataException (String.valueOf(FailureReasonConstants.INCORRECT_FIELD_LENGTH));        
        }

        //checking maximum limits of customer id field for long values
        long customerIdTemp = accountInfoModel.getCustomerId().longValue();
        
        if(customerIdTemp > AccountInfoConstants.CUSTOMER_ID_MAX_RANGE ){
        	logger.info("<--------INvalid Customer ID------------------>") ;
        	throw new InvalidDataException (String.valueOf(FailureReasonConstants.INCORRECT_FIELD_LENGTH));
        }

        //checking max range of createdByUserId
        if (logModel.getCreatdByUserId() != null){
        	long createdByUserIdTmp = logModel.getCreatdByUserId().longValue();
        	if(createdByUserIdTmp > LogConstants.CREATED_BY_USER_ID_MAX_RANGE ){
        		throw new InvalidDataException (String.valueOf(FailureReasonConstants.INCORRECT_FIELD_LENGTH));    
        	}
        }

        //mobile number also be number so parse this and check validity of this
        if (!VeriflyUtility.isNumber(accountInfoModel.getCustomerMobileNo())) {
        	logger.info("<--------Mobile No is non numeric------------------>") ;
        	throw new InvalidDataException (String.valueOf(FailureReasonConstants.INCORRECT_CUSTOMER_MOBILE_NO));
        }

        // here check the record already exists
        messageObject.setError(false);
    	logger.info("<--------End validate for generateModify ()------------------>") ;
        return messageObject;
    }


    /**
     *  Validate accountInfoModel for sufficient data
     *
     * @param accountInfoModel  validate their fields
     * @return boolean if all valid return true else false
     */
    public boolean isValidDataForVerifyPin(VeriflyBaseWrapper baseWrapper) {
        if( baseWrapper == null || baseWrapper.getAccountInfoModel() == null ){
            return false;
        }
        AccountInfoModel accountInfoModel = baseWrapper.getAccountInfoModel();

        //initialize required data
        Long customerId = accountInfoModel.getCustomerId();
        String accountNick = accountInfoModel.getAccountNick();
        String oldPin = accountInfoModel.getOldPin();

        //checking required fields for null values
        if (customerId == null ||
            (accountNick == null || accountNick.trim().length() < 1) ||
            (oldPin == null || oldPin.trim().length() < 1)) {
            return false;
        }

        //checking validity of the log created by and created by user id fields
        if(!isLogFieldsValid(baseWrapper.getLogModel())){
            return false;
        }

        //fields successfully passed all test apply to them now returning true mean all requires things are valid
        return true;
    }
    
    /**
     *  Validate accountInfoModel for sufficient data
     *
     * @param accountInfoModel  validate their fields
     * @return boolean if all valid return true else false
     */
    public boolean isValidDataForVerifyCredentials(VeriflyBaseWrapper baseWrapper) {
        if( baseWrapper == null || baseWrapper.getAccountInfoModel() == null ){
            return false;
        }
        AccountInfoModel accountInfoModel = baseWrapper.getAccountInfoModel();

        //initialize required data
        Long customerId = accountInfoModel.getCustomerId();
        String accountNick = accountInfoModel.getAccountNick();

        //checking required fields for null values
        if (customerId == null ||
            (accountNick == null || accountNick.trim().length() < 1)) {
            return false;
        }

        //checking validity of the log created by and created by user id fields
        if(!isLogFieldsValid(baseWrapper.getLogModel())){
            return false;
        }

        //fields successfully passed all test apply to them now returning true mean all requires things are valid
        return true;
    }


    /**
     *  Validate accountInfoModel for sufficient data
     *
     * @param accountInfoModel  validate their fields
     * @return boolean if all valid return true else false
     */
    public boolean isValidDataForChangePin(VeriflyBaseWrapper baseWrapper) {
        if(baseWrapper == null || baseWrapper.getAccountInfoModel() == null ){
            return false;
        }
        AccountInfoModel accountInfoModel = baseWrapper.getAccountInfoModel();

        //initialize reuired data
        Long customerId = accountInfoModel.getCustomerId();
        String accountNick = accountInfoModel.getAccountNick();
        String oldPin = accountInfoModel.getOldPin();
        String newPin = accountInfoModel.getNewPin();
        String confirmNewPin = accountInfoModel.getConfirmNewPin();

        //checking required fields for null values
        if(baseWrapper.getObject("IS_MIGRATED") == null)
        {
            if (customerId == null ||
                    (accountNick == null || accountNick.trim().length() < 1) ||
                    (oldPin == null || oldPin.trim().length() < 1) ||
                    (newPin == null || newPin.trim().length() < 1) ||
                    (confirmNewPin == null || confirmNewPin.trim().length() < 1)
                    ) {
                return false;
            }
        }
        else
        {
            if (customerId == null ||
                    (accountNick == null || accountNick.trim().length() < 1)||
                    (newPin == null || newPin.trim().length() < 1) ||
                    (confirmNewPin == null || confirmNewPin.trim().length() < 1)
                    ) {
                return false;
            }
        }

        //checking validity of the log created by and created by user id fields
        if(!isLogFieldsValid(baseWrapper.getLogModel())){
            return false;
        }

        //fields successfully passed all test apply to them now returning true mean all requires things are valid
        return true;
    }

    /**
     *  Validate accountInfoModel for sufficient data
     *
     * @param accountInfoModel  validate their fields
     * @return boolean if all valid return true else false
     */
    public boolean isValidDataForResetPin(VeriflyBaseWrapper baseWrapper) {
        if(baseWrapper == null || baseWrapper.getAccountInfoModel() == null){
            return false;
        }
        AccountInfoModel accountInfoModel = baseWrapper.getAccountInfoModel();

        //checking validity of the log created by and created by user id fields
        if(!isLogFieldsValid(baseWrapper.getLogModel())){
            return false;
        }

        //if data is valid return true else return false
        return isPrimaryFieldsValid(accountInfoModel.getCustomerId(),
                                    accountInfoModel.getAccountNick());
    }

    /**
     *  Validate accountInfoModel for sufficient data
     *
     * @param accountInfoModel  validate their fields
     * @return boolean if all valid return true else false
     */
    public boolean isValidDataForDeactivatePin(VeriflyBaseWrapper baseWrapper) {
        if(baseWrapper == null || baseWrapper.getAccountInfoModel() == null){
            return false;
        }
        AccountInfoModel accountInfoModel = baseWrapper.getAccountInfoModel();

        //checking validity of the log created by and created by user id fields
        if(!isLogFieldsValid(baseWrapper.getLogModel())){
            return false;
        }

        return isPrimaryFieldsValid(accountInfoModel.getCustomerId(),
                                    accountInfoModel.getAccountNick());
    }

    /**
     *  Validate accountInfoModel for sufficient data
     *
     * @param accountInfoModel  validate their fields
     * @return boolean if all valid return true else false
     */
    public boolean isValidDataForActivatePin(VeriflyBaseWrapper baseWrapper) {
        if (baseWrapper == null || baseWrapper.getAccountInfoModel() == null) {
            return false;
        }
        AccountInfoModel accountInfoModel = baseWrapper.getAccountInfoModel();

        //checking validity of the log created by and created by user id fields
        if (!isLogFieldsValid(baseWrapper.getLogModel())) {
            return false;
        }

        return isPrimaryFieldsValid(accountInfoModel.getCustomerId(),
                                    accountInfoModel.getAccountNick());
    }

    /**
     *  Validate accountInfoModel for sufficient data
     *
     * @param accountInfoModel  validate their fields
     * @return boolean if all valid return true else false
     */
    public boolean isValidDataForDeleteAccount(AccountInfoModel
            accountInfoModel) {
        return isPrimaryFieldsValid(accountInfoModel.getCustomerId(),
                                    accountInfoModel.getAccountNick());
    }

    private boolean isPrimaryFieldsValid(Long customerId,
                                                String accountNick) {
        //checking required fields for null values
        if (customerId == null ||
            (accountNick == null || accountNick.trim().length() < 1)) {
            return false;
        }
        return true;
    }

    private boolean isLogFieldsValid(LogModel logModel){
        String createdBy = logModel != null?logModel.getCreatedBy():"";
        Long   createdByUserId = logModel != null && logModel.getCreatdByUserId() != null ?logModel.getCreatdByUserId():0;

        //checking max range of createdByUserId
        long createdByUserIdTmp = createdByUserId.longValue();
        if (createdByUserIdTmp > LogConstants.CREATED_BY_USER_ID_MAX_RANGE ||
            (createdBy != null &&
             createdBy.length() > LogConstants.CREATED_BY_LENGTH)) {
            return false;
        }

        //correct log values return true mean all things are valid
        return true;
    }

    private boolean isExist(AccountInfoModel accInfoModel) {
    		

        CustomList<AccountInfoModel>
                cusList = this.accountInfoDao.findByExample(accInfoModel);
        if (cusList.getResultsetList().isEmpty())
            return false;
        else
            return true;
    }
    
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


	public PaymentModeDAO getPaymentModeDAO() {
		return paymentModeDAO;
	}


	public void setPaymentModeDAO(PaymentModeDAO paymentModeDAO) {
		this.paymentModeDAO = paymentModeDAO;
	}

}
