package com.inov8.jsblconsumer.util;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DecodingUtility {

    static {
        System.loadLibrary("keys");
    }

    public native String getScanner13();

    public native String getScanner14();

    public native String getScanner15();

    public native String getScanner16();

    private static String AES_ALGORITHM;
    private SecretKeySpec secretKeySpec;
    private IvParameterSpec ivSpec;

    static {
        System.loadLibrary("keys");
    }

    public DecodingUtility() {

        final byte[] iv = Base64.decode(getScanner13(), Base64.NO_WRAP);

        final byte[] keyValue = Base64.decode(getScanner14(), Base64.NO_WRAP);
        AES_ALGORITHM = getScanner15();

        ivSpec = new IvParameterSpec(iv);
        secretKeySpec = new SecretKeySpec(keyValue, getScanner16());
    }

    public String decode(String[] parts) {

        String var = "";

        try {

            int index = 0;

            String str1 = parts[0].substring(3, (parts[0].length() - 2));
            String str2 = parts[1].substring(2, (parts[1].length() - 1));
            String str3 = parts[2].substring(4, (parts[2].length() - 3));
            String str4 = parts[3].substring(1, (parts[3].length() - 4));

            String p1 = new StringBuilder(new String(Base64.decode(decrypt(str1), Base64.NO_WRAP), "UTF-8")).reverse().toString();
            String p2 = new StringBuilder(new String(Base64.decode(decrypt(str2), Base64.NO_WRAP), "UTF-8")).reverse().toString();
            String p3 = new StringBuilder(new String(Base64.decode(decrypt(str3), Base64.NO_WRAP), "UTF-8")).reverse().toString();
            String p4 = new StringBuilder(new String(Base64.decode(decrypt(str4), Base64.NO_WRAP), "UTF-8")).reverse().toString();
            int length = p1.length() + p2.length() + p3.length() + p4.length();

            for (int i = 0; i < length; i++) {

                switch (i % 4) {

                    case 0:

                        var = var + p1.charAt(index);
                        break;

                    case 1:
                        var = var + p2.charAt(index);
                        break;

                    case 2:
                        var = var + p3.charAt(index);
                        break;

                    case 3:
                        var = var + p4.charAt(index);
                        break;
                }

                if (var.length() % 4 == 0) {
                    index++;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return var;
    }

    private String decrypt(String text) {

        try {
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);
            byte[] decordedValue = Base64.decode(text, Base64.DEFAULT);
            return new String(cipher.doFinal(decordedValue), "UTF-8");
        } catch (Exception e) {
            return (new StringBuilder()).toString();
        }
    }

    private String encrypt(String text) {
        String encryptedValue;
        try {
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);
            byte[] encValue = cipher.doFinal(text.getBytes());
            encryptedValue = Base64.encodeToString(encValue, Base64.DEFAULT);
        } catch (Exception e) {
            encryptedValue = "";
        }
        return encryptedValue;
    }
}