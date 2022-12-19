package com.inov8.integration.util;

/**
 * Created by Inov8 on 12/18/2019.
 */

import com.thoughtworks.xstream.XStream;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
public class RSEncryption {

    public static PublicKey getPublicKey(String base64PublicKey){
        PublicKey publicKey = null;
        try{
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(java.util.Base64.getDecoder().decode(base64PublicKey.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return publicKey;
    }
    public static PrivateKey getPrivateKey(String base64PrivateKey){
        PrivateKey privateKey = null;
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(base64PrivateKey.getBytes()));
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            privateKey = keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return privateKey;
    }

    public static byte[] encrypt(String data, String publicKey) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
        return cipher.doFinal(data.getBytes());
    }

    public static String decrypt(byte[] data, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(data));
    }

    public static String decrypt(String data, String base64PrivateKey) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        return decrypt(Base64.getDecoder().decode(data.getBytes()), getPrivateKey(base64PrivateKey));
    }
    public static void main(String[] args) throws IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, BadPaddingException {

        String publicKey="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqrNGIdcnqApyewqko8tGTrP45oDcFVTKxeTzROx758R3G9WQwxoOa/ueXvro5XAhbjLAsn/N4TR9Qo5YBgsYuWIfpg6/xUx78AAYywyGkAQ9O08hv2wIx2lpXi15bpGgIvBWZ//q0AIRQer/uXN7Lf4TLgiXrnGldZ3ol9lTdI3q/3fNGxPo8F4MHS4f+99xWaRqYIgacTghWYyMfmPA0eX4rbiCJQ9YSYRoFd5JGsFX0H5UM5/KEdUX9qJ29nhsiVg31YMfQT1TkxEej8XlnEbrcNZInewzxnjg5Qp2X405DlNNs2qRg8KX3g4JW0w0aomrIiyA/URQo5OYB+ogKQIDAQAB";
        String privateKey="MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCqs0Yh1yeoCnJ7CqSjy0ZOs/jmgNwVVMrF5PNE7HvnxHcb1ZDDGg5r+55e+ujlcCFuMsCyf83hNH1CjlgGCxi5Yh+mDr/FTHvwABjLDIaQBD07TyG/bAjHaWleLXlukaAi8FZn/+rQAhFB6v+5c3st/hMuCJeucaV1neiX2VN0jer/d80bE+jwXgwdLh/733FZpGpgiBpxOCFZjIx+Y8DR5fituIIlD1hJhGgV3kkawVfQflQzn8oR1Rf2onb2eGyJWDfVgx9BPVOTER6PxeWcRutw1kid7DPGeODlCnZfjTkOU02zapGDwpfeDglbTDRqiasiLID9RFCjk5gH6iApAgMBAAECggEAN4SVI755bGLxdukwQQGE/WX1Zuq0Kc/pH1HmeR0881Vns8rTCVF5LU+p45RRmQXOKawiFiwRBtTuhG4NXPHQ+cJbLsQPRRkqGhW9+pQ5U6+9RoPnIv39IPY8yd+aZgbBAXwPbkXC1iJGAJ1wzJ0ti/jdvhQXWSonOUjBdFm/leFluKKXXYKbuSu80eOuXwXxRS6iIcl769T0nWrkJ16GMNUSjrQwewpVs+eL+DvgR9sDw+2QZ3vedQIJ34JrdFVCWJ6VMVxOZTFAnM6Mdhz2U0Yg6OygqjdaJ7cpxObZQBnGV1wa2tXUcmAF9oh4pNfopFW0YKg1TaB1fTXQYJQoDQKBgQDoONHkqpUNdFPe0BJTYypN/CkNfniWD8hWq2tUWNriXlUJ/ew/mhiCnrG7Z2pazW5sj+5YafepYorqtCy4wE0LGQEinzK/sqLBi0g1ntAjX79yL7ZAo1UM/p9NxP2blemfd3NZYX4p9WbAQox6e3uvtFNzaSnjx8GEeChTv/3qawKBgQC8Lc0MsodSY61ipL3sZDHWeT0oWEhmyIv5ffAyxl5ky2shltHIT5+YQXD/ALm0ZDlagSZllgh5pYseN1KmN8tWgsnzqwMjSIQKme+8b2AkEdbMs5O4MjjXjDoSYsKXzoPTzUCQFSScA8QZiZTbR7ifwY6wbA2UesDXak0pa9CsuwKBgQCwG+8psCfrlS3JyxgqvwilLbdgcWo3u25eNA/oXp5Yj9uEAVq5/T3bSUhWbB4fEK5OjHTf/jUA8yeQyOUlk0iaQvM2c28ELA/8IYm4iGEmgoVorkBUEsn8WdksHFmMNYuAERGcVjBc16t94LoyZRUu8d1bqARJpwLeWFWU9gQ1UwKBgQCc6yyEHBOP9qLNEUd26o5MpMkE7aliXHWs1QIJtTVdo+twFQ5WlV8mXdvWKUqyTKku+WeFarkih5Mc3VFEXl5ng6TYVpDFSWEiXf92CQII0f9mr5DNFsQjqkF6t3RBluqDIjNmctsWZmWOThYdTQ992LAeGdyxJjPM7k3t7oQSpQKBgQDKeqiPRKCqxyXVTJCTlwFk8UNaooRAvJ0RH9UPuQmfs6LXg2kkirDfBwEw7mZXsVA+ispd0CvhHU75z10i8U6sJ0iIcgD4dy+kZreUwysslrD9GxhYqlBsNegL84kKIMrlzaRaWQ43bTf9xl1s6j2Zt/Zs8UrSG6r1YVAkGJB2tA==";
        try {
            String encryptedString = Base64.getEncoder().encodeToString(encrypt("1313", publicKey));
            System.out.println("Encrypted String: "+encryptedString);
            String decryptedString = RSEncryption.decrypt(encryptedString, privateKey);
            System.out.println("Decrypted String"+decryptedString);
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        }

    }
}
