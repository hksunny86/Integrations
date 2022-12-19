/**
 *
 */
package com.inov8.integration.util;

import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.i8sb.controller.I8SBSwitchController;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommonUtils {
    private static final String filePath = PropertyReader.getProperty("js.account.statement.path");
    private static final String CAP_DELIMITER = "^";
    private static final String NEW_LINE_SEPARATOR = "/n";

    public static String generateSTAN() {
        Random rnd = new Random();
        String stan = String.valueOf((100000 + rnd.nextInt(900000)));
        return stan;
    }

    public static String generateRandomNumber() {
        Random rnd = new Random();
        String stan = String.valueOf((10000 + rnd.nextInt(90000)));
        return stan;
    }

    public static String getFirstLengthHexByte(int length) {
        String firstByte = Integer.toHexString(length / 256);
        if (firstByte.length() == 1) {
            firstByte = "0" + firstByte;
        } else if (firstByte.length() < 1) {
            firstByte = "00";
        }
        return firstByte;
    }

    public static String get2ndLengthHexByte(int length) {
        String secondByte = Integer.toHexString(length % 256);
        if (secondByte.length() == 1) {
            secondByte = "0" + secondByte;
        } else if (secondByte.length() < 1) {
            secondByte = "00";
        }
        return secondByte;
    }

    public static byte[] hexStringToByteArray(String hex) {

        byte[] bts = new byte[hex.length() / 2];
        for (int i = 0; i < bts.length; i++) {
            bts[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bts;
    }

    public static String byteArrayToHexString(byte in[]) {
        byte ch = 0x00;
        int i = 0;
        if (in == null || in.length <= 0)
            return null;
        String pseudo[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
        StringBuffer out = new StringBuffer(in.length * 2);
        while (i < in.length) {
            ch = (byte) (in[i] & 0xF0); // Strip off high nibble
            ch = (byte) (ch >>> 4); // shift the bits down
            ch = (byte) (ch & 0x0F);// must do this is high order bit is on!
            out.append(pseudo[(int) ch]); // convert thenibble to a String
            // Character
            ch = (byte) (in[i] & 0x0F); // Strip off low nibble
            out.append(pseudo[(int) ch]); // convert the nibble to a String
            // Character
            i++;
        }
        String rslt = new String(out);
        return rslt;
    }

    public static String bytesToBinary(byte[] bytes) {
        StringBuffer buffer = new StringBuffer();
        for (byte b : bytes) {
            buffer.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }
        return buffer.toString();
    }

    public static String addPadding(String padCharacter, boolean leftPad, int requiredLength, String value) {
        String input = StringUtils.defaultString(value).trim();
        if (leftPad)
            return StringUtils.leftPad(input, requiredLength, padCharacter);
        else
            return StringUtils.rightPad(input, requiredLength, padCharacter);
    }


    public static List<String> readFile(String name) throws IOException {

        String path = filePath + name;
        return readFileByPath (path);
    }

    public static List<String> readFileByPath(String filePath) throws IOException {
        BufferedReader br = null;
        FileReader fr = null;
        List<String> arr = new ArrayList<>();
        try {
            fr = new FileReader(filePath);
            if (fr != null) {
                br = new BufferedReader(fr);
            }
            String currentLine;
            while ((currentLine = br.readLine()) != null)
                arr.add(currentLine);
        } catch (Exception e) {
            throw new IOException("File Not Found at : " + filePath);
        }

        return arr;
    }

    public static List<String> readFileOverNetwork(String url) throws IOException {
        List<String> arr = new ArrayList<>();
        if (new UrlValidator().isValid(url)) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(new URL(url).openStream(), StandardCharsets.UTF_8))) {
                String currentLine;
                while ((currentLine = in.readLine()) != null)
                    arr.add(currentLine);
            }

        } else {
            throw new IOException("Invalid Or Malformed URL: " + url);
        }

        return arr;
    }

    /**
     * A utility method that takes String input from eChallan BalanceInquiryResponse and
     * parses it according to conditions set in eChallan contract.
     *
     * @param input
     * @return Matcher with parsed string if conditions met, else returns null
     */
    public static List<String> eChallanBillInquiryResponseParser(String input) {

        Matcher m = Pattern.compile("^(\\d{2})([\\w\\s]{30})([P|U|B ])([\\d\\s]{8})([0-9-+ ]{14})([0-9-+ ]{14})([\\d\\s]{4})([\\d\\s]{8})([\\d\\s]{12})([\\d\\s]{6})(.*)").matcher(input);
        if (m.matches()) {
            List<String> parsedStrings = new ArrayList<>();
            for (int i = 1; i <= m.groupCount(); i++) parsedStrings.add(m.group(i));

            return parsedStrings;
        }
        return Collections.emptyList();
    }

    public static InputStream writeRecordsToFile(List<String> cardList,String fileName)
    {

        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        File file = new File(fileName);
        try {

            fileWriter = new FileWriter(file);
            bufferedWriter = new BufferedWriter(fileWriter);
            for (String cards : cardList) {

                bufferedWriter.write(cards);
                bufferedWriter.newLine();

            }


            bufferedWriter.flush();
            InputStream targetStream = new FileInputStream(file);
            fileWriter.close();
            bufferedWriter.close();

            return targetStream;


        }

        catch (Exception e) {
            System.out.println("Error in FileWriter !!!");
            e.printStackTrace();
        }

        return null;
    }

    public static String trimObjectToEmpty(Object obj)
    {
        if( obj instanceof Integer )
        {
          if(obj == null || ((Integer)obj) == 0)
              return "";
        }
        else if (obj instanceof Double)
        {
            if (obj == null || ((Double)obj) == 0.0)
                return "";

        }

        return obj.toString();
    }

    public static I8SBSwitchController getFromProxy(String serverUrl) throws Exception {
        HttpInvokerProxyFactoryBean httpInvokerProxyFactoryBean = new HttpInvokerProxyFactoryBean();
        httpInvokerProxyFactoryBean.setServiceInterface(I8SBSwitchController.class);
        httpInvokerProxyFactoryBean.setServiceUrl(serverUrl);
        httpInvokerProxyFactoryBean.afterPropertiesSet();

        I8SBSwitchController i8SBSwitchController = (I8SBSwitchController) httpInvokerProxyFactoryBean.getObject();
        return i8SBSwitchController;
    }

    public static String convertion(String message){
        String[] arrOfMsg = message.split("\\s+");
        for (int i=0; i<arrOfMsg.length; i++){
            if (arrOfMsg[i].equals("Password") && arrOfMsg[i+1].equals("is")){
                String password = arrOfMsg[i+2];
                StringBuilder newPassword = new StringBuilder();
                char[] letters = password.toCharArray();
                for (char ch : letters) {
                    if(ch >= '!' && ch <= '/' || ch >= ':' && ch <='@' || ch >= '[' && ch <= '`' || ch >= '{' && ch <= '~' ){
                        newPassword.append("%"+Integer.toHexString((int) ch));
                    }else {
                        newPassword.append(ch);
                    }
                }
                arrOfMsg[i+2] = newPassword.toString();
            }
        }
        message = StringUtils.join(arrOfMsg, " ");
        return message;
    }

}