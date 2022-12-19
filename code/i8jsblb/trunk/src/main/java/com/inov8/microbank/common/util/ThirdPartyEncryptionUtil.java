package com.inov8.microbank.common.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Attique on 12/29/2017.
 */
public class ThirdPartyEncryptionUtil {

        private static final Logger log = LoggerFactory.getLogger(EncryptionUtil.class);


        public static String encryptWithAES(String key, String strToEncrypt) {
            Security.addProvider(new BouncyCastleProvider());
            byte[] keyBytes = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13,
                    0x14, 0x15, 0x16, 0x17};
            try {
                keyBytes = generateKey(key);//key.getBytes();//
                SecretKeySpec skey = new SecretKeySpec(keyBytes, "AES");
                byte[] input = strToEncrypt.trim().getBytes("UTF8");

                synchronized (Cipher.class) {
                    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
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
                keyBytes = generateKey(key);//key.getBytes("UTF8");//
                SecretKeySpec skey = new SecretKeySpec(keyBytes, "AES");
                byte[] input = org.bouncycastle.util.encoders.Base64.decode(strToDecrypt.trim().getBytes("UTF8"));

                synchronized (Cipher.class) {
                    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                    log.info("Cipher Block Size: " + cipher.getBlockSize());
                    cipher.init(Cipher.DECRYPT_MODE, skey);
                    log.info("After INIT()");
                    byte[] plainText = new byte[cipher.getOutputSize(input.length)];

                    int ptLength = cipher.update(input, 0, cipher.getOutputSize(input.length), plainText, 0);
                    log.info("PT Length: " + ptLength);
                    ptLength += cipher.doFinal(plainText, ptLength);
                    //byte[] tempArr = cipher.doFinal(plainText);
                    String decryptedString = new String(plainText);
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

        public static byte[] generateKey(String sKey) throws NoSuchAlgorithmException, UnsupportedEncodingException {
            final MessageDigest md = MessageDigest.getInstance("md5");
            final byte[] digestOfPassword = md.digest(sKey.getBytes("utf-8"));
            final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 32);
            for (int j = 0, k = 16; j < 16; ) {
                keyBytes[k++] = keyBytes[j++];
            }
            return keyBytes;
        }

        public static void main(String[] args) {

            HashMap<String, String> map = new HashMap<String, String>();
            map.put("Pxwbdz81ajYZfLVcjbq69Q==", "1111");
            map.put("vKh939Wdo5EPbRW92ehI5A==", "2460");
            map.put("0gZd10wj6yukJKsBluUnWQ==", "se6f");
            map.put("kfFUiMQZXlJb35p3LCu1YA==", "rest");
            map.put("545J0LPIpp0IQ9/Xw6jBwg==", "24569825");
            map.put("Ej6OEZRbTHjxdhzN+rDRIA==", "abc123yz");
            map.put("FDwnf5GKDDfEoQ9H/dONZA==", "meezanmb");
            map.put("79Pk9+Yz0/epfjMj3JNACQ==", "testing520");
            map.put("K/PJzXwNZexuL60W3cesiQ==", "0300425098");
            map.put("RX/UzFdCyxJL7rWLG5iuew==", "inov8testing");
            map.put("VAQBNhwvH0rEDy9Kg9bG1g==", "445632400876");
            map.put("yi3+3XslbqzB2fDMABFAFA==", "testingencrypti");
            map.put("S1DKJIl8/QREErZRXzlLRQ==", "234566669856444");
            map.put("r+F4wF51ChllNhVpYGGRHQ==", "abc1234refd9873");

            map.put("VX51hZmUHLVJy+XZqOtobg==", "abc1234refd9873");

            map.put("3tvn+sqzggh922Lca0Wivw==", "11");

            String key = new String("65412399991212FF65412399991212FF");

            try {
                Iterator<String> myIterator = map.keySet().iterator();
                String pin="15370";
                String enpin=encryptWithAES(key,pin);
                System.out.println(enpin);
                System.out.println(decryptWithAES(key,enpin));
                /*while (myIterator.hasNext()) {
                    String encryptedStr = myIterator.next();
                    System.out.println("Original key: " + encryptedStr + " | Original value: " + map.get(encryptedStr));

                    String encryption = encryptWithAES(key, map.get(encryptedStr));
                    System.out.println("Encryption: " + encryption);

                    String decrypted = decryptWithAES(key, encryption);
                    System.out.println("Decryption: " + decrypted);

                    encryption = encryptWithAES(key, decrypted);
                    System.out.println("Encryption: " + encryption);

                    System.out.println("-----------------------------------------");
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }


        }




}
