package com.inov8.integration.middleware.util;

import org.bouncycastle.util.encoders.Hex;
import org.jpos.iso.ISOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class PINGenerationUtil {
    private static final Logger log = LoggerFactory.getLogger(PINGenerationUtil.class);

    private static String TRIPLE_DES_TRANSFORMATION = "DESede/ECB/NoPadding";
    private static String TRIPLE_DES_TRANSFORMATION_ZERO = "DESede/ECB/NoPadding";
    private static String ALGORITHM = "DESede";

    private static String KEY = ConfigReader.getInstance().getProperty("crypto.pinblock.key", "27C6A677437B56F64B237224DCF1339F");
    private static final byte[] fPaddingBlock = ISOUtil.hex2byte("FFFFFFFFFFFFFFFF");

    public static String calculatePINBlock(String pin, String accountNumber){
        // Block 1
        byte[] block1 = ISOUtil.hex2byte(formatPINBlock(pin));
        System.out.println(ISOUtil.byte2hex(block1));

        // Block 2
        byte[] block2 = ISOUtil.hex2byte(formatAccount(accountNumber));

        System.out.println(ISOUtil.byte2hex(block2));

        // pinBlock
        byte[] pinBlock = ISOUtil.xor(block1, block2);

        System.out.println(ISOUtil.byte2hex(pinBlock));

        System.out.println(" pin block "+new String(pinBlock));

        String result = null;
        try {
            result = encrypt(pinBlock);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static void main(String[] args) throws Exception{
        System.out.println(calculatePINBlock("4466", "2205450050000666"));
//        String str="34502-5430877-1";
//        str=str.replace("-","");
//        System.out.println(str);
//        String str2=str.substring(0,5)+"-"+str.substring(5,12)+"-"+str.substring(12);
//        System.out.println(str2);

    }

    private static String formatPINBlock(String pin){
        char[] block = ISOUtil.hexString(fPaddingBlock).toCharArray();
        char[] pinLenHex = String.format("%02X", pin.length()).toCharArray();
        pinLenHex[0] = (char)('0' + 0x0);

        // pin length then pad with 'F'
        System.arraycopy(pinLenHex, 0, block, 0, pinLenHex.length);
        System.arraycopy(pin.toCharArray(), 0 ,block, pinLenHex.length, pin.length());
        return new String(block);
    }

    private static String formatAccount(String accountNumber){
        return "0000" + accountNumber.substring(3, accountNumber.length()-1);
    }

    private static String processKey(String key){
        return key + key.substring(0, 16);
    }

    private static String encrypt(byte[] input)
            throws IllegalBlockSizeException, BadPaddingException,
            NoSuchAlgorithmException, NoSuchProviderException,
            NoSuchPaddingException, InvalidKeyException {
        byte[] key = Hex.decode(processKey(KEY));

        SecretKey keySpec = new SecretKeySpec(key, ALGORITHM);
        Cipher encrypter = Cipher.getInstance(TRIPLE_DES_TRANSFORMATION);
        encrypter.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] output = encrypter.doFinal(input);
        return ISOUtil.byte2hex(output).toUpperCase();
        //return Hex.toHexString(output).toUpperCase();
    }

    private static String decrypt(String text)
            throws IllegalBlockSizeException, BadPaddingException,
            NoSuchAlgorithmException, NoSuchProviderException,
            NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException {
        byte[] key = Hex.decode(processKey(KEY));
        byte[] input = Hex.decode(text);

        SecretKey keySpec = new SecretKeySpec(key, ALGORITHM);
        Cipher decrypter = Cipher.getInstance(TRIPLE_DES_TRANSFORMATION);
        decrypter.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] output = decrypter.doFinal(input);

        return Hex.toHexString(output);
    }

}
