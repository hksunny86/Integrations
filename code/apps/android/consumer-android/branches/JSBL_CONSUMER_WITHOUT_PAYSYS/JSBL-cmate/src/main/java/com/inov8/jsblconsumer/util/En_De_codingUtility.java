package com.inov8.jsblconsumer.util;

import android.util.Base64;

import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Zaigham on 1/22/2018.
 */

public class En_De_codingUtility {
    private static String AES_ALGORITHM;
    private SecretKeySpec secretKeySpec;
    private IvParameterSpec ivSpec;

    public En_De_codingUtility() {

        final byte[] iv = {'0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0'};
        final byte[] keyValue = new byte[]{'J', 'S', 'B', 'L', 'C', 'o', 'n', 's', 'u', 'm', 'e', 'r','W', '@', 'i', '8'};
        AES_ALGORITHM = "AES/CBC/PKCS5Padding";

        ivSpec = new IvParameterSpec(iv);
        secretKeySpec = new SecretKeySpec(keyValue, "AES");
    }

    public String[] getParts() {
        String key = "682ede816988e58fb6d057d9d85605e0";
//        String key = "9aB64Be3b2OKcTq6UCTMOLGsF5J4CpVacHk1ErrPIEk="; SSL Pin
//        String key = "xnNgITFF5kesvYqgUFhO3rIxQtra18wS2XIR0NekNXU=";
        String[] parts = new String[4];



        String p1 = "", p2 = "", p3 = "", p4 = "";

        char[] arr = key.toCharArray();

        for (int i = 0; i < arr.length; i++) {

            switch (i % 4) {

                case 0:
                    p1 = p1 + arr[i];
                    break;

                case 1:
                    p2 = p2 + arr[i];
                    break;

                case 2:
                    p3 = p3 + arr[i];
                    break;

                case 3:
                    p4 = p4 + arr[i];
                    break;
            }
        }

        p1 = new StringBuilder(p1).reverse().toString();
        p2 = new StringBuilder(p2).reverse().toString();
        p3 = new StringBuilder(p3).reverse().toString();
        p4 = new StringBuilder(p4).reverse().toString();

        parts[0] = encrypt(Base64.encodeToString(p1.getBytes(), Base64.NO_WRAP));
        parts[1] = encrypt(Base64.encodeToString(p2.getBytes(), Base64.NO_WRAP));
        parts[2] = encrypt(Base64.encodeToString(p3.getBytes(), Base64.NO_WRAP));
        parts[3] = encrypt(Base64.encodeToString(p4.getBytes(), Base64.NO_WRAP));

        Random random = new Random();

        parts[0] = random.nextInt(10 - 0) + "" + random.nextInt(10 - 0) + "" + random.nextInt(10 - 0) + "" + parts[0] + random.nextInt(10 - 0) + "" + random.nextInt(10 - 0) + "";

        parts[1] = random.nextInt(10 - 0) + "" + random.nextInt(10 - 0) + "" + parts[1] + random.nextInt(10 - 0) + "";

        parts[2] = random.nextInt(10 - 0) + "" + random.nextInt(10 - 0) + "" + random.nextInt(10 - 0) + "" + random.nextInt(10 - 0) + "" + parts[2] + random.nextInt(10 - 0) + "" + random.nextInt(10 - 0) + "" + random.nextInt(10 - 0) + "";

        parts[3] = random.nextInt(10 - 0) + "" + parts[3] + random.nextInt(10 - 0) + "" + random.nextInt(10 - 0) + "" + random.nextInt(10 - 0) + "" + random.nextInt(10 - 0) + "";

        String var = decode(parts);

        return parts;
    }

    public String decode(String[] parts) {

        String var = "";

        try {

            int index = 0;

            String p1 = new StringBuilder(new String(Base64.decode(decrypt(parts[0]), Base64.NO_WRAP), "UTF-8")).reverse().toString();
            String p2 = new StringBuilder(new String(Base64.decode(decrypt(parts[1]), Base64.NO_WRAP), "UTF-8")).reverse().toString();
            String p3 = new StringBuilder(new String(Base64.decode(decrypt(parts[2]), Base64.NO_WRAP), "UTF-8")).reverse().toString();
            String p4 = new StringBuilder(new String(Base64.decode(decrypt(parts[3]), Base64.NO_WRAP), "UTF-8")).reverse().toString();
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
