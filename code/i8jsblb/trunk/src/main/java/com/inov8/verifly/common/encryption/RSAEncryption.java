package com.inov8.verifly.common.encryption;

/**
 *
 * @author M Hassan
 * @version 1.0
 * @date  06-Sep-2006
 *
 */


import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

public class RSAEncryption implements Encryption
{
	public String encrypt (Key key, String input){
    	return com.inov8.framework.common.util.RSAEncryption.doEncrypt((PublicKey)key, input);	
    }
    public String decrypt (Key key, String input){
  		return com.inov8.framework.common.util.RSAEncryption.doDecrypt((PrivateKey)key, input);	
    }
}
