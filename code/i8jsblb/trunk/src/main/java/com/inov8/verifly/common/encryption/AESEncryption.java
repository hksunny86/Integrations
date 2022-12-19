package com.inov8.verifly.common.encryption;

import java.security.Key;

import javax.crypto.SecretKey;

import org.apache.commons.codec.binary.Hex;

import sun.misc.BASE64Encoder;

import com.inov8.microbank.common.util.EncryptionUtil;

/**
 * 
 * @author Soofia Faruq
 * @date 29-Aug-2013
 * 
 */

public class AESEncryption implements Encryption {

	@Override
	public String encrypt(Key sharedKey, String input) {
		String key = new String(Hex.encodeHex(((SecretKey)sharedKey).getEncoded()));
		return EncryptionUtil.encryptWithAES(key, input);
	}

	@Override
	public String decrypt(Key sharedKey, String cipherText) {
		BASE64Encoder encoder = new BASE64Encoder();
		byte[] encodedKey = sharedKey.getEncoded();
        
//		String key = new String(Hex.encodeHex(((SecretKeySpec)sharedKey).getEncoded())); 
		String key = new String(encoder.encode(encodedKey));
		return EncryptionUtil.decryptWithAES(key, cipherText);
	}
}
