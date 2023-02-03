package com.inov8.verifly.common.encryption;

import java.security.Key;
/**
 * 
 * @author irfan mirza
 * @version 1.0
 * @date 06-Sep-2006
 * 
 */

public class NullEncryption implements Encryption {
	public String encrypt(Key pubKey, String input) {
		return input;
	}

	public String decrypt(Key privKey, String cipherText) {
		return cipherText;
	}
}