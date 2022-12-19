package com.inov8.integration.middleware.util;

import org.apache.commons.lang.StringUtils;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.DESEngine;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DESEncryption {
	private static Logger logger = LoggerFactory.getLogger(DESEncryption.class.getSimpleName());

	protected BlockCipher engine;
	protected CipherParameters param;

	protected BufferedBlockCipher cipher;

	public DESEncryption(String key) {
		this.engine = new DESEngine();
		this.param = new KeyParameter(Hex.decode(key));
		cipher = new BufferedBlockCipher(engine);
	}

	public String performEncryption(String inputStr) throws Exception {
		byte[] input = Hex.decode(inputStr);
		cipher.init(true, param);
		byte[] out = new byte[input.length];
		int length = cipher.processBytes(input, 0, input.length, out, 0);
		cipher.doFinal(out, length);
		return new String(Hex.encode(out));
	}

	public String performDecryption(String stringToDecrypt) throws Exception {
		byte[] input = Hex.decode(stringToDecrypt);
		cipher.init(false, param);
		byte[] out = new byte[input.length];
		int length = cipher.processBytes(input, 0, input.length, out, 0);
		cipher.doFinal(out, length);
		return new String(Hex.encode(out));
	}

	public String generatePassword(final String pwd) {
		String encPwd = "";
		try {
			if (!StringUtils.isAlphanumeric(pwd)) {
				throw new Exception("Password must be alpha numeric only...");
			}
			String pwdBuffer = StringUtils.rightPad(pwd, 16, 'F');
			encPwd = performEncryption(pwdBuffer).toUpperCase();
		} catch (Exception ex) {
			logger.error("Error while encrypting channel password...", ex);
		}
		return encPwd;
	}

	public static void main(String[] args) {
		String input = "123";
		DESEncryption des = new DESEncryption("1111111111111111");
		try {
			System.out.println("act: " + input);
			String enc = des.generatePassword(input);
			System.out.println("enc: " + enc);
			String dcr = des.performDecryption(enc);
			System.out.println("dcr: " + dcr);
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}

}
