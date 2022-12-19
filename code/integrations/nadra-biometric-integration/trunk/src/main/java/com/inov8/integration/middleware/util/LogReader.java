package com.inov8.integration.middleware.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogReader {
    public static void main(String args[])throws Exception{
        String string = "\"code\" : \"100\",";
        string = string.replaceAll("\""," ");
        string = string.split(":")[1];//.replaceAll(".","").replaceAll(",","");
        System.out.println(string.trim().replaceAll(",",""));
        FileReader fr=new FileReader("C:\\Users\\inov8\\Desktop\\Logs\\js-nadra\\js-nadra-integration.log.2018-06-02.1");
        BufferedReader br=new BufferedReader(fr);
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD hh:mm:ss");

//        String date  = "2017-12-15";
//        Date startTime = simpleDateFormat.parse("2017-12-15 14:03:48");
//        Date endTime = simpleDateFormat.parse("2017-12-15 14:03:52");
//        String reportDate = simpleDateFormat.format(startTime);

        String line;
        while ((line = br.readLine()) != null) {
            Date logTime =  null;
            if (line.indexOf("\"status\" : {")!=-1){
                //logTime = simpleDateFormat.parse(date+" "+line.substring(0,line.indexOf(",")));
                insertDate(br);
            }
        }
        br.close();
        fr.close();
    }
    public static String getValue(String string){
        try {
            string = string.replaceAll("\""," ");
            string = string.split(":")[1];
            string = string.replaceAll(",","").trim();
        }catch (Exception e){
            System.out.println("Sting: "+ string);
            System.out.println(e.getMessage());
        }
        return string;
    }

    public static void insertDate(BufferedReader br) throws Exception{
        String code = getValue(br.readLine());
        if(code.equals("100")) {
            String sessionId = getValue(br.readLine());
            String cnic = getValue(br.readLine());
            br.readLine();
            String expiryDate = getValue(br.readLine());
            String cardExpired = getValue(br.readLine());
            String gender = getValue(br.readLine());
            String birthPlace = getValue(br.readLine());
            String photograph = getValue(br.readLine());
            String presentAddress = getValue(br.readLine());
            String cardType = getValue(br.readLine());
            String name = getValue(br.readLine());
            String fatherHusbandName = getValue(br.readLine());
            String motherName = getValue(br.readLine());
            String dateOfBirth = getValue(br.readLine());
            String permanentAddress = getValue(br.readLine());
            String fingerIndexes = getValue(br.readLine());
            System.out.println("Session ID: " + sessionId);
            System.out.println("cnic: " + cnic);
            System.out.println("presentAddress: " + presentAddress);
            System.out.println("birthPlace: " + birthPlace);
            System.out.println("fatherHusbandName: " + fatherHusbandName);
            System.out.println("motherName: " + motherName);
            System.out.println("permanentAddress: " + permanentAddress);
        }
    }
}
