// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 10/29/2009 7:37:49 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   DESEncryption.java

package com.inov8.microbank.common.des;


import java.security.Security;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.sun.crypto.provider.SunJCE;

public class DESEncryption
{
    private static class DESKeySupport
    {
    	
    	   public Cipher getDecryptCipher()
    	    {
    	        return decryptCipher;
    	    }

    	    public Cipher getEncryptCipher()
    	    {
    	        return encryptCipher;
    	    }

    	    public SecretKey getSecretKey()
    	    {
    	        return secretKey;
    	    }

    	    private SecretKey secretKey;
    	    private Cipher encryptCipher;
    	    private Cipher decryptCipher;

    	    public DESKeySupport(byte keyBytes[])
    	        throws Exception
    	    {
    	        encryptCipher = Cipher.getInstance("DESede/CBC/PKCS5Padding", "SunJCE");
    	        decryptCipher = Cipher.getInstance("DESede/CBC/PKCS5Padding", "SunJCE");
    	        secretKey = new SecretKeySpec(keyBytes, "DESede");
    	        encryptCipher.init(1, secretKey, DESEncryption.iv);
    	        decryptCipher.init(2, secretKey, DESEncryption.iv);
    	    }
        
    }


    public static synchronized DESEncryption getInstance()
    {
        if(object == null)
            object = new DESEncryption();
        return object;
    }

    private DESEncryption()
    {
        cache = Collections.synchronizedMap(new HashMap(5));
        base64Encoder = new BASE64Encoder();
        base64Decoder = new BASE64Decoder();
        initDefault();
    }

    private synchronized void initDefault()
    {
        try
        {
            Security.addProvider(new SunJCE());
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException("Error initializing Cipher for DES Encryption/Decryption");
        }
    }

    public synchronized String encrypt(String string, byte keyBytes[])
        throws Exception
    {
        DESKeySupport keySupport = getKeySupport(keyBytes);
        byte passwordBytes[] = string.getBytes("UTF8");
        byte encryptedPasswordBytes[] = keySupport.getEncryptCipher().doFinal(passwordBytes);
        String encodedEncryptedPassword = base64Encoder.encode(encryptedPasswordBytes);
        return encodedEncryptedPassword;
    }

    public synchronized String decrypt(String string)
        throws Exception
    {
    	String encryptKey = "0102030405060708090A0B0C0D0E0F100102030405060708" ;       
        byte [] keyBytes = StringUtil.toBinArray(encryptKey) ;
    	
        DESKeySupport keySupport = getKeySupport(keyBytes);
        byte encryptedBytes[] = base64Decoder.decodeBuffer(string);
        byte passwordBytes[] = keySupport.getDecryptCipher().doFinal(encryptedBytes);
        String recovered = new String(passwordBytes, "UTF8");
        return recovered;
    }

    private DESKeySupport getKeySupport(byte keyBytes[])
        throws Exception
    {
        keyBytes = keyBytes == null ? DESedeKeyBytes : keyBytes;
        DESKeySupport keySupport = null;
        String keyStr = new String(keyBytes, "UTF8");
        if(cache.containsKey(keyStr))
        {
            keySupport = (DESKeySupport)cache.get(keyStr);
        } else
        {
            keySupport = new DESKeySupport(keyBytes);
            cache.put(keyStr, keySupport);
        }
        return keySupport;
    }

    public static void main(String args[])
    {
        try
        {
            String key = "0102030405060asdasddsa708090A0B0C0D0E0F100102030405060708";
            DESEncryption encryptAgent = new DESEncryption();
            String string = key;
            System.out.println((new StringBuilder()).append("Password ....................[").append(string).append("]").toString());
            String encodedEncryptedStr = encryptAgent.encrypt(key, null);
            System.out.println((new StringBuilder()).append("Encoded encrypted password ..[").append(encodedEncryptedStr).append("]").toString());
            String recoveredStr = encryptAgent.decrypt(encodedEncryptedStr);
            System.out.println((new StringBuilder()).append("Recovered password ..........[").append(recoveredStr).append("]").toString());
            System.out.println((new StringBuilder()).append("String matched: ").append(key.equals(recoveredStr)).toString());
        }
        catch(Exception e)
        {
            e.printStackTrace(System.out);
        }
    }

//    protected static Log logger = LogFactory.getLog(com/inov8/framework/common/util/DESEncryption);
    protected Map cache;
    private static final byte DESedeKeyBytes[] = {
        1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 
        11, 12, 13, 14, 15, 16, 1, 2, 3, 4, 
        5, 6, 7, 8
    };
    private static final byte ivBytes[] = {
        0, 1, 2, 3, 4, 5, 6, 7
    };
    private static IvParameterSpec iv = new IvParameterSpec(ivBytes);
    private BASE64Encoder base64Encoder;
    private BASE64Decoder base64Decoder;
    private static final String characterEncoding = "UTF8";
    private static DESEncryption object = null;


}