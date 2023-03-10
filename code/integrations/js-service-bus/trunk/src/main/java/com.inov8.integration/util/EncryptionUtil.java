package com.inov8.integration.util;

import com.inov8.integration.config.PropertyReader;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.HashMap;
import java.util.Iterator;

public class EncryptionUtil {
    private static final Logger log = LoggerFactory.getLogger(EncryptionUtil.class);

    private static final String KEY ="682ede816988e58fb6d057d9d85605e0";

//            PropertyReader.getProperty("i8sb.gateway.crypto.key");

    public static String encrypt(String input) {
        return encryptWithAES(KEY, input);
    }

    public static String decrypt(String input) {
        return decryptWithAES(KEY, input);
    }

    public static String encryptWithAES(String key, String strToEncrypt) {
        Security.addProvider(new BouncyCastleProvider());
        byte[] keyBytes = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13,
                0x14, 0x15, 0x16, 0x17};
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
        byte[] keyBytes = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13,
                0x14, 0x15, 0x16, 0x17};
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



    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException {

//        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
//        keyGen.initialize(2048);
//        byte[] publicKey = keyGen.genKeyPair().getPublic().getEncoded();
//        StringBuffer retString = new StringBuffer();
//        for (int i = 0; i < publicKey.length; ++i) {
//            retString.append(Integer.toHexString(0x0100 + (publicKey[i] & 0x00FF)).substring(1));
//        }
//        System.out.println(retString);

        String key = new String("682ede816988e58fb6d057d9d85605e0");
        String decrypted = null;
        try {
            decrypted = encryptWithAES(key,"ub3r_khi");

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Decryption: " + decrypted);
    }


}
