/**
 * 
 */
package com.inov8.verifly.common.des;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.util.DESEncryption;
import com.inov8.verifly.common.constants.ConfigurationConstants;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.model.VeriflyConfigurationModel;
import com.inov8.verifly.common.util.StringUtil;
import com.inov8.verifly.server.dao.mainmodule.VeriflyConfigurationDAO;


public class EncryptionHandler {
	private static final String KEY = "0102030405060708090A0B0C0D0E0F100102030405060708";

	private VeriflyConfigurationDAO veriflyConfigurationDao;
	private final Log logger = LogFactory.getLog(this.getClass());
    private List <VeriflyConfigurationModel> veriflyConfigurationList;
	public VeriflyConfigurationDAO getVeriflyConfigurationDao() {
		return veriflyConfigurationDao;
	}

	public void setVeriflyConfigurationDao(
			VeriflyConfigurationDAO veriflyConfigurationDao) {
		this.veriflyConfigurationDao = veriflyConfigurationDao;
	}
	
	public EncryptionHandler (VeriflyConfigurationDAO veriflyConfigurationDao) throws Exception{
		this.veriflyConfigurationDao = veriflyConfigurationDao ;
		VeriflyConfigurationModel veriflyConfigurationModel = new VeriflyConfigurationModel();
		veriflyConfigurationModel.setName(ConfigurationConstants.ENCRYPT_KEY_NAME);
        CustomList<VeriflyConfigurationModel> customList1 = veriflyConfigurationDao.findByExample(veriflyConfigurationModel);
        veriflyConfigurationList = customList1.getResultsetList();
        
        logger.info("Size of Configuration List------------------------>" + veriflyConfigurationList.size());
        
        if (veriflyConfigurationList.size() == 0){
        		throw new Exception ("Encryption Key not found in database");
        }		

	}

	/**
	 * This method encrypts the CardNo, Expiry Date and Account No
	 * @param _accountInfoModel
	 * @return
	 * @throws Exception
	 */
	public AccountInfoModel encrypt (AccountInfoModel _accountInfoModel) throws Exception {
		
        
        VeriflyConfigurationModel veriflyConfigurationModel = veriflyConfigurationList.get(ConfigurationConstants.FIRST_INDEX);
        
        String encryptKey = veriflyConfigurationModel.getValue();
       
        
        byte [] keyBytes = StringUtil.toBinArray(encryptKey);
        DESEncryption desEncryption = DESEncryption.getInstance();
        
        if (_accountInfoModel.getCardNo() != null && !_accountInfoModel.getCardNo().trim().equals("")){
        	String cardNo = desEncryption.encrypt(_accountInfoModel.getCardNo(),keyBytes, true);
        	logger.info("Encrypted Card No---------->" + cardNo);
        	_accountInfoModel.setCardNo(cardNo);
        }
        
        if (_accountInfoModel.getCardExpiryDate() != null && !_accountInfoModel.getCardExpiryDate().trim().equals("")){
        	String cardExpiryDate = desEncryption.encrypt(_accountInfoModel.getCardExpiryDate(),keyBytes, true);
        	logger.info("Encrypted Card Expiry--->" + cardExpiryDate);
        	_accountInfoModel.setCardExpiryDate(cardExpiryDate);
        }
        
        if (_accountInfoModel.getAccountNo() != null && !_accountInfoModel.getAccountNo().trim().equals("")){
        	String accountNo = desEncryption.encrypt(_accountInfoModel.getAccountNo(),keyBytes, true);
        	logger.info("Encrypted Account No--->" + accountNo);
        	_accountInfoModel.setAccountNo(accountNo);
        }
        logger.debug("<-----------------------------Encryption Completed----------------------------->");
		return _accountInfoModel ;
	}

	/**
	 * This method decrypts the Card No, Card Expiry date and Account No
	 * @param _accountInfoModel
	 * @return
	 * @throws Exception
	 */
	public AccountInfoModel decrypt (AccountInfoModel _accountInfoModel) throws Exception {
		
        VeriflyConfigurationModel veriflyConfigurationModel = veriflyConfigurationList.get(ConfigurationConstants.FIRST_INDEX);        
        String encryptKey = veriflyConfigurationModel.getValue();
      
        byte [] keyBytes = StringUtil.toBinArray(encryptKey);
        
        DESEncryption desEncryption = DESEncryption.getInstance();
        
        if (_accountInfoModel.getCardNo() != null && !_accountInfoModel.getCardNo().trim().equals("")){
        	
        	String cardNo = desEncryption.decrypt(_accountInfoModel.getCardNo(),keyBytes,true);
        //	logger.info("Decrypted Card No---------->" + cardNo);
        	_accountInfoModel.setCardNo(cardNo);
        }
        
        if (_accountInfoModel.getCardExpiryDate() != null && !_accountInfoModel.getCardExpiryDate().trim().equals("")){
        	String cardExpiryDate = desEncryption.decrypt(_accountInfoModel.getCardExpiryDate(),keyBytes, true);
        //	logger.info("Decrypted Card Expiry--->" + cardExpiryDate);
        	_accountInfoModel.setCardExpiryDate(cardExpiryDate);
        }
        
        if (_accountInfoModel.getAccountNo() != null && !_accountInfoModel.getAccountNo().trim().equals("")){
        	String accountNo = desEncryption.decrypt(_accountInfoModel.getAccountNo(),keyBytes, true);
        //	logger.info("Decrypted Account No--->" + accountNo);
        	_accountInfoModel.setAccountNo(accountNo);
        }
        logger.debug("<-----------------------------Decryption Completed----------------------------->");
		return _accountInfoModel ;
	}
	
	
	public String encrypt (String input)throws Exception {
        
		VeriflyConfigurationModel veriflyConfigurationModel = veriflyConfigurationList.get(ConfigurationConstants.FIRST_INDEX);        
        String encryptKey = veriflyConfigurationModel.getValue();
    
        
        byte [] keyBytes = StringUtil.toBinArray(encryptKey);
        DESEncryption desEncryption = DESEncryption.getInstance();
        
        return desEncryption.encrypt(input,keyBytes, true);

	}

	/**
	 * @author naseer.ullah 
	 * @param input The input string to decrypt
	 * @return
	 * @throws Exception
	 */
	public String decrypt( String input )throws Exception
	{
		VeriflyConfigurationModel veriflyConfigurationModel = veriflyConfigurationList.get(ConfigurationConstants.FIRST_INDEX);        
        String encryptKey = veriflyConfigurationModel.getValue();

		DESEncryption desUtil = DESEncryption.getInstance();
		byte[] keyBytes = StringUtil.toBinArray( encryptKey );
        return desUtil.decrypt( input, keyBytes, true );
	}

	public static void main (String [] args) throws Exception {
		
//		DESEncryption desEncryption = DESEncryption.getInstance();
//		desEncryption.encrypt(_accountInfoModel.getAccountNo(),keyBytes);
		String key = "0102030405060708090A0B0C0D0E0F100102030405060708" ;
		DESEncryption desUtil = DESEncryption.getInstance();
		byte [] keyBytes = StringUtil.toBinArray(key);
		String s =desUtil.encrypt("10000000001", keyBytes, true);
		System.out.println("Encrypted---->" + s);
		String s1 = desUtil.decrypt("kYlwk+N7em5W2haclpmPiA==", keyBytes, true);
		System.out.println("decrypted---->" + s1);
	}

}
