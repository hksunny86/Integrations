package com.inov8.integration.util;

import java.security.Key;

/**
 *
 * @author irfan mirza
 * @version 1.0
 * @date  06-Sep-2006
 *
 */

public interface Encryption {
//	public static String encrypt(Key pubKey, String input);
	  public String decrypt(Key privKey, String cipherText);
}
