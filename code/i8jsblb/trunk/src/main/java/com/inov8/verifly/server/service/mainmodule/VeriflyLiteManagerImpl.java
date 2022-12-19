package com.inov8.verifly.server.service.mainmodule;

import java.security.KeyPair;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.misc.BASE64Decoder;

import com.inov8.verifly.common.constants.ConfigurationConstants;
import com.inov8.verifly.common.constants.FailureReasonConstants;
import com.inov8.verifly.common.constants.VeriflyLiteConstants;
import com.inov8.verifly.common.encryption.AESEncryption;
import com.inov8.verifly.common.encryption.Encryption;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;


public class VeriflyLiteManagerImpl extends VeriflyManagerImpl
{
	private final Log logger = LogFactory.getLog(this.getClass());

	@Override
	public VeriflyBaseWrapper changePIN(VeriflyBaseWrapper wrapper) throws Exception
	{
		wrapper.setExceptionStatus(true);
		wrapper.setErrorStatus(false);
        String errorMessage = this.getErrorMessages(new Long(
                FailureReasonConstants.FACILITY_NOT_AVAILABLE).toString());
		wrapper.setErrorMessage(errorMessage);
		return wrapper;
	}

	@Override
	public VeriflyBaseWrapper generatePIN(VeriflyBaseWrapper baseWrapper) throws Exception
	{
		baseWrapper = super.generatePIN(baseWrapper, false);		
    	return baseWrapper ;
	}
	
	@Override
	public VeriflyBaseWrapper activatePIN(VeriflyBaseWrapper baseWrapper) throws Exception
	{
		baseWrapper = super.activatePIN(baseWrapper, false);		
    	return baseWrapper ;
	}

	@Override
	public VeriflyBaseWrapper resetPIN(VeriflyBaseWrapper wrapper) throws Exception
	{
		wrapper.setExceptionStatus(true);
		wrapper.setErrorStatus(false);
        String errorMessage = this.getErrorMessages(new Long(
                FailureReasonConstants.FACILITY_NOT_AVAILABLE).toString());
		wrapper.setErrorMessage(errorMessage);
		return wrapper;
	}

	@Override
	public VeriflyBaseWrapper verifyPIN(VeriflyBaseWrapper wrapper) throws Exception
	{
    	XStream xstream = new XStream(new PureJavaReflectionProvider());
        Encryption encryption = (Encryption)keysObj.getEncrptionClassObject();
        
        
        /*String keyPairValue = keysObj.getValue (ConfigurationConstants.KEY_PAIR);
        KeyPair keypair = (KeyPair) xstream.fromXML(keyPairValue);
    	
        String str = encryption.encrypt(keypair.getPublic(), this.messageSource.getMessage(VeriflyLiteConstants.VERIFLY_LITE_PIN_KEY,null,null) );*/

        System.out.println("this is VeriflyLiteManagerImpl");
        
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
		
		String encryptPin = null;
		/*********************************************************************************
		 * Updated by Soofia Faruq
		 */
		if (keypair != null) 
		{
			encryptPin = encryption.encrypt(keypair.getPrivate(), wrapper.getAccountInfoModel().getNewPin());
		} 
		else 
		{
			encryptPin = encryption.encrypt(aesKey, wrapper.getAccountInfoModel().getNewPin());
		}
		/**********************************************************************************/
		
		
		
		
        wrapper.getAccountInfoModel().setOldPin(encryptPin);
		
		return super.verifyPIN(wrapper);
	}
	
}
