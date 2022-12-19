package com.inov8.agentmate.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class AesEncryptor {

    public static String encrypt(String strToEncrypt) {
        Security.addProvider(new BouncyCastleProvider());
        byte[] keyBytes = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05,
                0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f,
                0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17 };

        try {
            keyBytes = Constants.SECRET_KEY.getBytes("UTF8");
            SecretKeySpec skey = new SecretKeySpec(keyBytes, "AES");
            byte[] input = strToEncrypt.getBytes("UTF8");

            synchronized (Cipher.class) {
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
                cipher.init(Cipher.ENCRYPT_MODE, skey);

                byte[] cipherText = new byte[cipher.getBlockSize()];
                int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
                ctLength += cipher.doFinal(cipherText, ctLength);
                String encryptedString = new String(
                        org.bouncycastle.util.encoders.Base64
                                .encode(cipherText));
                return encryptedString;
            }
        } catch (UnsupportedEncodingException uee) {
            uee.printStackTrace();
        } catch (IllegalBlockSizeException ibse) {
            ibse.printStackTrace();
        } catch (BadPaddingException bpe) {
            bpe.printStackTrace();
        } catch (InvalidKeyException ike) {
            ike.printStackTrace();
        } catch (NoSuchPaddingException nspe) {
            nspe.printStackTrace();
        } catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
        } catch (ShortBufferException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encryptWithAES(String key, String strToEncrypt) {
        Security.addProvider(new BouncyCastleProvider());
        byte[] keyBytes = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13,
                0x14, 0x15, 0x16, 0x17};
        try {
            keyBytes = generateKey(key)
            ;
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
            System.err.println("Crypto Exception:"+ uee);
        } catch (IllegalBlockSizeException ibse) {
            System.err.println("Crypto Exception:"+ ibse);
        } catch (BadPaddingException bpe) {
            System.err.println("Crypto Exception:"+ bpe);
        } catch (InvalidKeyException ike) {
            System.err.println("Crypto Exception:"+ ike);
        } catch (NoSuchPaddingException nspe) {
            System.err.println("Crypto Exception:"+ nspe);
        } catch (NoSuchAlgorithmException nsae) {
            System.err.println("Crypto Exception:"+ nsae);
        } catch (ShortBufferException e) {
            System.err.println("Crypto Exception:"+ e);
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

    public static String encrypt(String key, String strToEncrypt) {
        Security.addProvider(new BouncyCastleProvider());
        byte[] keyBytes = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05,
                0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f,
                0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17};

        try {
            keyBytes = key.getBytes("UTF8");
            SecretKeySpec skey = new SecretKeySpec(keyBytes, "AES");
            byte[] input = strToEncrypt.getBytes("UTF8");

            synchronized (Cipher.class) {
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
                cipher.init(Cipher.ENCRYPT_MODE, skey);

//    byte[] cipherText = new byte[cipher.getBlockSize()];
                byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
                int ctLength = cipher.update(input, 0, input.length,
                        cipherText, 0);
                ctLength += cipher.doFinal(cipherText, ctLength);
                String encryptedString = new String(
                        org.bouncycastle.util.encoders.Base64
                                .encode(cipherText));
                return encryptedString;
            }
        } catch (UnsupportedEncodingException uee) {
            uee.printStackTrace();
        } catch (IllegalBlockSizeException ibse) {
            ibse.printStackTrace();
        } catch (BadPaddingException bpe) {
            bpe.printStackTrace();
        } catch (InvalidKeyException ike) {
            ike.printStackTrace();
        } catch (NoSuchPaddingException nspe) {
            nspe.printStackTrace();
        } catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
        } catch (ShortBufferException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decryptWithAES(String key, String strToDecrypt) {
        Security.addProvider(new BouncyCastleProvider());
        byte[] keyBytes = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05,
                0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f,
                0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17};

        try {
            keyBytes = key.getBytes("UTF8");
            SecretKeySpec skey = new SecretKeySpec(keyBytes, "AES");
            byte[] input = org.bouncycastle.util.encoders.Base64
                    .decode(strToDecrypt.trim().getBytes("UTF8"));

            synchronized (Cipher.class) {
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
                cipher.init(Cipher.DECRYPT_MODE, skey);

                byte[] plainText = new byte[cipher.getOutputSize(input.length)];
                int ptLength = cipher.update(input, 0, input.length, plainText, 0);
                ptLength += cipher.doFinal(plainText, ptLength);
                String decryptedString = new String(plainText);
                return decryptedString.trim();
            }
        } catch (UnsupportedEncodingException uee) {
            uee.printStackTrace(); }
        catch (IllegalBlockSizeException ibse) {
            ibse.printStackTrace();
        } catch (BadPaddingException bpe) {
            bpe.printStackTrace();
        } catch (InvalidKeyException ike) {
            ike.printStackTrace();
        } catch (NoSuchPaddingException nspe) {
            nspe.printStackTrace();
        } catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
        } catch (ShortBufferException e) {
            e.printStackTrace();
        }
        return null;
    }
}