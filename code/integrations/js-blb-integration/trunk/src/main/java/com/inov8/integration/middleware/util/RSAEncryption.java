package com.inov8.integration.middleware.util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


/**
 * Created by Inov8 on 12/18/2019.
 */
public class RSAEncryption {

    private static String publicKey = ConfigReader.getInstance().getProperty("publicKey","");
    private static String privateKey = ConfigReader.getInstance().getProperty("privateKey","");


    public static PublicKey getPublicKey(String base64PublicKey){
        PublicKey publicKey = null;
        try{
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(base64PublicKey.getBytes()));
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

//    private PrivateKey privateKey;
//    private PublicKey publicKey;

//    public RSAEncryption() throws NoSuchAlgorithmException {
//        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
//        keyGen.initialize(2048);
//        KeyPair pair = keyGen.generateKeyPair();
//        this.privateKey = pair.getPrivate();
//        this.publicKey = pair.getPublic();
//    }

    public void writeToFile(String path, byte[] key) throws IOException {
        File f = new File(path);
        f.getParentFile().mkdirs();

        FileOutputStream fos = new FileOutputStream(f);
        fos.write(key);
        fos.flush();
        fos.close();
    }

//    public PrivateKey getPrivateKey() {
//        return privateKey;
//    }
//
//    public PublicKey getPublicKey() {
//        return publicKey;
//    }


    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        RSAEncryption keyPairGenerator = new RSAEncryption();
//        keyPairGenerator.writeToFile("RSA/publicKey", keyPairGenerator.getPublicKey().getEncoded());
//        keyPairGenerator.writeToFile("RSA/privateKey", keyPairGenerator.getPrivateKey().getEncoded());
//        System.out.println(Base64.getEncoder().encodeToString(keyPairGenerator.getPublicKey().getEncoded()));
//        System.out.println(Base64.getEncoder().encodeToString(keyPairGenerator.getPrivateKey().getEncoded()));
        String text;
        String confirmLoginPin="NMcobg2Grjas1fhDdIbzjtTHy2iVtAgCHOzLdiAawMAtQ7YZl90kW/tEz+ITYF2Kw4eR6X08fDc95ZLbuHGFcpXcbh" +
                "4PupxVuyaWNtc9KL85KpnM0b/qlXGqHfsDXmrvip2Qivx6048E2Ki17cYMG8iJkdpmZy/VF+29rOoiUmVdwmgzW1QYfgBAuc9XU8UlqYTiNvGwKv8IiIFl2e3cuhQrj+Jlpl1h9SP3Z5YAAtc73VjW4Pt7fyIXJ84qkL+6ijITePYUXKGwbKtU1ikRY0LO/OCu1u4cHnPKimNCt/TCobMLOkyn9GkYPdNqSS0yYvjiwxi4wDlgnS+PbSgNIA==";
        text = confirmLoginPin;
        confirmLoginPin = text.replaceAll("\\r|\\n", "");
        String confirmPin = null;
        try {
            confirmPin = (RSAEncryption.decrypt(confirmLoginPin, "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCbtyBh7ihgU9JTSUa2Vda7/bTJWW4EJ221VFGS+TNZKZU4ErXExRglQvBSg3u3dkcXR4xm/gDCJEPy1WM13ASyAS9wUo5L/GR8KYVwl55ChtogfxxLt3DAWyHX1RkRqbyfp8F0dm8jRyHHpOoa4KlcV0e9VOvVyNfeub8yR1sJ3F85KkgYRKK90tsGdmcelSEqflN90W16Sig7WrRnqMPNmqP23R/TuSC5sYcnCYwj+3rkofkNbQb2czu39/dReuAuqB0RNfOZsxZTfjhFmv4hrVZqzsrv5JVpOtmpbyf/iOR1LL92oGfxqkgNJU6FNykM9NUH1q+Y3kvBj9DqU1VZAgMBAAECggEAZoHkUHOeouMrV2aqpUfW58S5qb80nIl0QGW0mcNxWSX1tIckn9d78LOjTz59SCYV4K5aMEbQKv4CuS5edEtVz/TVzOScBkutvsr6rEcziOoLWE81SmcFU6vqmJIRQb5yjMu7n7aDrT6bbzkSYAU7mK1gbbQk2ISyD05KHog410DxQjI8aGO1eUEePNHopuTLR9ZLOcr+VLjInfBFtp02oVGWrdeimVZiVg3buqTikBkvVoBW5+Hx7EH8blRlAweg4kjBcF1vJ6HR4RM6FF8ja/9zq3n14cXrKOHDpfngh3iHgO/6CbKezXXWWcL+VWb3GSSeRe4D4GJwA9tKG+digQKBgQDj+513StPIYzhKN9WZJXRJVKs9zZD4nKGlZVJwgmbydCq49K0td/R2OXfZ/kB5jguX8I8gW/lLXSlp7ERQ3UiATbROqP5v1h8Oa4neGQxISmSzHCS6YwTZFOtWyGdF97XJLAiknCSUz+YVlr7rqywm/gjEFrgKSbI3QbiqAXVS8QKBgQCu2fX+aVyGU4Pub6ziBkTXmBqpyd9YDBtkewnvRmGY9rK877Ii+VDCCx2U0/PsyatFnaZ/5i4pInu2XjaQi0BlgSLf3Q2hlKkCdaJXRNZL3oSKiMaZE2Gq4Rz1XEohXAY209JouuBpKRW428avOoq5JMcgRwJUvQWdd2ywUohY6QKBgQCkTU6zjULJ1O/ZVRnj8AE5ZAKxXLfQ5Bj++OGL4f7NVvj/KJ6x+RQ+K7Z7bWgL8lnam2ya1o34SWCyTKsDWCaLCPUcB2CvOrJREyFd5dFbO/oMtwilJv10cUYV2gHLg/UH0Ws9LqaSdK4N+wEHGOqOA2BP6UujZm9AsWWjZ7+lEQKBgBB7l4buI7y97rfxaxi7go3YVtsenqFMMuDcAGm/9r9Wsi7BYceOPSCtr5IyENKHiE+9ts5jwoI6L/NXGkmx9tYawFDeI8TRoMUMlcsoQBNS3Ke6kQ4pF5HmOraehpZyeFt+yFz7EOCY8OVGQoqOODmPz/2o8/1M/FSFx4cvoI7RAoGBAIU6rMIkmiqbyZTq/sLM8HIP9CRYLxgsvoXNHwxgp84gt9EC+6E+UvWZCYfKLNfb7Pkw126DSWeXlSUniSSFPfev7h9PfFuURXzADNMT0H7XYV/Z+NKjlqz32ValmzG11qmAjq0H+ojL9VJez0GmZvQSpuUzdJwWP3Xyoxklv93E"));
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        System.out.println(" Encrypyted String : "+confirmPin);

//        try {
//            String encryptedString = Base64.getEncoder().encodeToString(encrypt("1111", publicKey));
////            String encryptedString= "eJbDrZXdEhyUiXFs6hrL/236dnU1M1nJIct3whp+DIME/KfeBcyyK1LMNIrJYqR9P6mrlSfj/wDdec2ZXsuf919QsFILtGQzA69TRGr81rhABgtbZJsb8mJH93Y7VTqMkL6+ZVwmpxsOUsF5KEVlb7+o8hIAs+zVgNSpq1M3Wc8=";
//            System.out.println(" Encrypyted String : "+encryptedString);
//            String decryptedString = RSAEncryption.decrypt(encryptedString, privateKey);
//            System.out.print(" Decrypyted String : "+decryptedString);
//        } catch (NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException e) {
//            System.err.println(e.getMessage());
//        }

    }


    }
