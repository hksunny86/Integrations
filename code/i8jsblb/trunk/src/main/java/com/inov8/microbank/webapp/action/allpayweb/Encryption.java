package com.inov8.microbank.webapp.action.allpayweb;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Kashif Bashir.
 */

public class Encryption {

	private final static Log log = LogFactory.getLog(Encryption.class);
	
    private static final byte[] _3desData = {

    	(byte)0xF2, (byte)0x6B, (byte)0x1A, (byte)0xC7, (byte)0xD0, (byte)0x2F,
        (byte)0x4E, (byte)0x1D, (byte)0xFF, (byte)0xEA, (byte)0x3C, (byte)0x2B,
        (byte)0x1A, (byte)0x4D, (byte)0x4E, (byte)0x6F, (byte)0xAA, (byte)0xCB,
        (byte)0x2E, (byte)0x3B, (byte)0xAC, (byte)0xBB, (byte)0xF1, (byte)0xDE
    };
    
    private static SecretKeySpec _key = new SecretKeySpec(_3desData, "DESede");
    

    
    
    /**
     * @param plainText
     * @return
     */
    public static String encrypt(String plainText){

        try {
        	
        	if(isValidString(plainText)) {
        		
	        	byte[] plain = plainText.getBytes("UTF8");        
	            
	            Cipher cipher = Cipher.getInstance("DESede");
	            
	            cipher.init(Cipher.ENCRYPT_MODE, _key);
	            
	            byte[] cipherText = cipher.doFinal(plain);
	            
	            Base64 b64 = new Base64();
	            
	            cipherText = b64.encodeBase64(cipherText);
	            
	            return new String(cipherText);
        	}
        	
        } catch(Exception e) {
        	
        	log.error("Encryption.encrypt("+plainText+")"+e.getLocalizedMessage());
        }
        
        return null;
    }
    
    
    /**
     * @param cipherText
     * @return
     */
    public static String decrypt(String cipherText) {    	
                
    	try {
        	
        	if(isValidString(cipherText)) {
	        	
	            Base64 b64 = new Base64();
	            
	            byte[] cipherByte = b64.decodeBase64(cipherText.getBytes());          
	            
	            Cipher cipher = Cipher.getInstance("DESede");  // Triple-DES encryption
	            
	            cipher.init(Cipher.DECRYPT_MODE, _key);
	                        
	            return new String(cipher.doFinal(cipherByte),"UTF8");
        	}
        	
        } catch(Exception e){
        	
        	log.error("Encryption.decrypt("+cipherText+")"+e.getLocalizedMessage());
        }        
        
        return null;
    }	
	
    public String doEnc(String plainText) {
    	
    	return new Encryption().encrypt(plainText);
    }
	
    public String doDyc(String cipherText) {
    	
    	return new Encryption().decrypt(cipherText);
    }
    
    /**
     * @param cipherText
     * @return
     */
    private static Boolean isValidString(String cipherText) {
    	
        if(cipherText == null || cipherText == "") {
        	
            return Boolean.FALSE;
        }    	
        
        return Boolean.TRUE;
    }
}
