package com.inov8.integration.middleware.util;

import org.apache.commons.lang.StringUtils;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

public class PropertiesDecryptionUtil {
    private static Logger logger = LoggerFactory.getLogger(PropertiesDecryptionUtil.class.getSimpleName());

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String input = "";
        do {
            System.out.print(String.format("%-45s", "Please enter string to encrypt [q to quit]: "));
            input = scanner.nextLine();

            if (!input.equalsIgnoreCase("q")) {
                String encryptedText = getEncryptor().encrypt(input);
                System.out.print(String.format("%-45s", "Encrypted String is : "));
                System.out.println(encryptedText);
            }
            if (input.equalsIgnoreCase("d")) {
                System.out.println(getEncryptor().decrypt("QdI8/rkLnxsxRib/C33aoA=="));
            }
        }

        while (!input.equalsIgnoreCase("q"));
    }

    private static StandardPBEStringEncryptor encryptor;

    public static StandardPBEStringEncryptor getEncryptor() {
        if (encryptor == null) {
            encryptor = new StandardPBEStringEncryptor();
            encryptor.setAlgorithm("PBEWithMD5AndDES");//getProperty("algorithm"));
            encryptor.setPassword("682ede816988e58fb6d057d9d85605e0");//getProperty("password"));

/*			String algorithm = getProperty("algorithm");
            System.out.println(algorithm);

			encryptor.setAlgorithm(algorithm);

			String password = getProperty("password");
			System.out.println(password);

			encryptor.setPassword(password);*/
        }

        return encryptor;
    }

    private static Properties prop;

    static {
        try (InputStream is = PropertiesDecryptionUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            prop = new Properties();
            if (is != null)
                prop.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getProperty(String key) {

        return prop.getProperty(key);
    }

    public static String DecryptProperty(String value) {
        String decryptedValue = "";
        if (StringUtils.isNotEmpty(value)) {
            try {
                decryptedValue = getEncryptor().decrypt(StringUtils.substring(value, 4, value.length() - 1));
            } catch (Exception e) {
                logger.debug(e.toString());
            }
        }
        return decryptedValue;
    }
}
