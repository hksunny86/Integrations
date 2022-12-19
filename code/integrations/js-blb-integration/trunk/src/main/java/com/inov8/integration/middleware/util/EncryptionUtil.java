package com.inov8.integration.middleware.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.HashMap;
import java.util.Iterator;

public class EncryptionUtil {
    private static final Logger log = LoggerFactory.getLogger(EncryptionUtil.class);

    private static final String KEY = ConfigReader.getInstance().getProperty("crypto.channel.key", "682ede816988e58fb6d057d9d85605e0");

    public static String encrypt(String input) {
        return encryptWithAES(KEY, input);
    }

    public static String decrypt(String input) {
        return decryptWithAES(KEY, input);
    }

	public static String encryptWithAES(String key, String strToEncrypt) {
		Security.addProvider(new BouncyCastleProvider());
		byte[] keyBytes = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13,
				0x14, 0x15, 0x16, 0x17 };
		try {
			keyBytes = key.getBytes("UTF8");
			SecretKeySpec skey = new SecretKeySpec(keyBytes, "AES");
			byte[] input = strToEncrypt.trim().getBytes("UTF8");

			synchronized (Cipher.class) {
				Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
				cipher.init(Cipher.ENCRYPT_MODE, skey);

				byte[] cipherText = new byte[cipher.getOutputSize(input.length)];

				int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
				ctLength += cipher.doFinal(cipherText, ctLength);

				String encryptedString = new String(org.bouncycastle.util.encoders.Base64.encode(cipherText), "UTF8");
				return encryptedString.trim();
			}
		} catch (UnsupportedEncodingException uee) {
            log.error("Crypto Exception:", uee);
		} catch (IllegalBlockSizeException ibse) {
            log.error("Crypto Exception:", ibse);
		} catch (BadPaddingException bpe) {
            log.error("Crypto Exception:", bpe);
		} catch (InvalidKeyException ike) {
            log.error("Crypto Exception:", ike);
		} catch (NoSuchPaddingException nspe) {
            log.error("Crypto Exception:", nspe);
		} catch (NoSuchAlgorithmException nsae) {
            log.error("Crypto Exception:", nsae);
		} catch (ShortBufferException e) {
            log.error("Crypto Exception:", e);
		}
		return null;
	}

	public static String decryptWithAES(String key, String strToDecrypt) {
		Security.addProvider(new BouncyCastleProvider());
		byte[] keyBytes = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13,
				0x14, 0x15, 0x16, 0x17 };
		try {
			keyBytes = key.getBytes("UTF8");
			SecretKeySpec skey = new SecretKeySpec(keyBytes, "AES");
			byte[] input = org.bouncycastle.util.encoders.Base64.decode(strToDecrypt.trim().getBytes("UTF8"));

			synchronized (Cipher.class) {
				Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
				cipher.init(Cipher.DECRYPT_MODE, skey);

				byte[] plainText = new byte[cipher.getOutputSize(input.length)];
				
				int ptLength = cipher.update(input, 0, input.length, plainText, 0);
				ptLength += cipher.doFinal(plainText, ptLength);
				String decryptedString = new String(plainText, "UTF8");
				return decryptedString.trim();
			}
        } catch (UnsupportedEncodingException uee) {
            log.error("Crypto Exception:", uee);
        } catch (IllegalBlockSizeException ibse) {
            log.error("Crypto Exception:", ibse);
        } catch (BadPaddingException bpe) {
            log.error("Crypto Exception:", bpe);
        } catch (InvalidKeyException ike) {
            log.error("Crypto Exception:", ike);
        } catch (NoSuchPaddingException nspe) {
            log.error("Crypto Exception:", nspe);
        } catch (NoSuchAlgorithmException nsae) {
            log.error("Crypto Exception:", nsae);
        } catch (ShortBufferException e) {
            log.error("Crypto Exception:", e);
        }
		return null;
	}

	public static void main(String[] args) {
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("6tBH5Et3C3b9p7Xzr1YVIQ==", "1111");
		map.put("ndFIlSe9hvdJvk+tH0a8MA==", "2460");
		map.put("XJerXP0BAfwFw6f/l11XdQ==", "se6f");
		map.put("knkMAySjuEnkoMCQ/5M4yQ==", "rest");
		map.put("R+5cOIQmc8KUIUS8NdZuFA==", "24569825");
		map.put("bDPvW6pqkjJpG7wq6enodA==", "abc123yz");
		map.put("jMnEtFkJEUIoCSmYKDRQrw==", "meezanmb");
		map.put("EnDaiCPN2UYEkG0SzouELw==", "testing520");
		map.put("KoM51/x29tNaFW6iZBm8vQ==", "0300425098");
		map.put("vJ5736fUdbwbiKwQUbPHAw==", "inov8testing");
		map.put("UN+5wuQ7svfe/YYV+BiTlQ==", "445632400876");
		map.put("RNvYovEWkTq4TQmrC9KD4A==", "testingencrypti");
		map.put("NjCpGQhvQyfnzyfdOfsu+w==", "234566669856444");
		map.put("VX51hZmUHLVJy+XZqOtobg==", "abc1234refd9873");
		
		map.put("VX51hZmUHLVJy+XZqOtobg==", "abc1234refd9873");
		
		map.put("6MZPgyfjlKZAz4cOKxvknswszzJ32doOv+MDyr0pTvU=", "11");

		String key = new String(ConfigReader.getInstance().getProperty("encryption.key",""));

		try {
			Iterator<String> myIterator = map.keySet().iterator();

			while (myIterator.hasNext()) {
				String encryptedStr = myIterator.next();
				System.out.println("Original key: " + encryptedStr + " | Original value: " + map.get(encryptedStr));

				String encryption = encryptWithAES(key, map.get(encryptedStr));
				System.out.println("Encryption: " + encryption);

				String decrypted = decryptWithAES(key, encryption);
				System.out.println("Decryption: " + decrypted);

				encryption = encryptWithAES(key, decrypted);
				System.out.println("Encryption: " + encryption);

				System.out.println("-----------------------------------------");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	
	
}
