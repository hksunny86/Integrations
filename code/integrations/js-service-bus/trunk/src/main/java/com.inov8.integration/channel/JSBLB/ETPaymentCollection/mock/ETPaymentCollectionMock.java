package com.inov8.integration.channel.JSBLB.ETPaymentCollection.mock;

import com.inov8.integration.channel.JSBLB.ETPaymentCollection.client.AssesmentDetails;
import com.inov8.integration.channel.JSBLB.ETPaymentCollection.client.ChallanDetail;
import org.joda.time.DateTime;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Inov8 on 10/23/2019.
 */
public class ETPaymentCollectionMock {

    public AssesmentDetails getassmentdetail(String registrationNumber,String chessisNo) throws  Exception{

        XMLGregorianCalendar xgc = DatatypeFactory.newInstance().newXMLGregorianCalendar(new DateTime().toGregorianCalendar());

        AssesmentDetails assesmentDetails=new AssesmentDetails();
        assesmentDetails.setVctAssesmentNO(505);
        assesmentDetails.setAssesmentDate(xgc);
        assesmentDetails.setRegistrationNo("123455");
        assesmentDetails.setRegistrationDate(xgc);
        assesmentDetails.setChassisNo("123333");
        assesmentDetails.setMarkerMake("honda");
        assesmentDetails.setCategory("4");
        assesmentDetails.setBodyType("");
        assesmentDetails.setEngCapacity(1300);
        assesmentDetails.setSeats(5);
        assesmentDetails.setCylinders(4);
        assesmentDetails.setOwnerCnic("3520243953533");
        assesmentDetails.setOwnerName("ahsan Raza");
        assesmentDetails.setFilerStatus("NO");
        assesmentDetails.setTaxPaidFrom(xgc);
        assesmentDetails.setTaxPaidUpTo(xgc);
        assesmentDetails.setVehTaxPaidLifeTime("y");
        assesmentDetails.setVehicalStatus("5");
        assesmentDetails.setFitnessDate(xgc);
        assesmentDetails.setTotalAmount(BigDecimal.valueOf(5000));
        assesmentDetails.setStatusCode("ORA-00000:Vehicle registration status (Vehicle Registration No.) is invalid for tax payment, , Please contact ETO office.");
        return assesmentDetails;
    }

    public ChallanDetail generateChallanDetail(String registrationnumber,String assesmentNumber,String assesmentTotalAmount,String challanStatus)throws Exception{
        XMLGregorianCalendar xgc = DatatypeFactory.newInstance().newXMLGregorianCalendar(new DateTime().toGregorianCalendar());
        ChallanDetail challanDetail=new ChallanDetail();

        challanDetail.setVCTChallanNO("505");
        challanDetail.setChallanDate("23102019");
        challanDetail.setRegistrationNo("123455");
        challanDetail.setRegistrationDate(xgc);
        challanDetail.setChassisNo("123333");
        challanDetail.setMarkerMake("honda");
        challanDetail.setCategory("4");
        challanDetail.setBodyType("8");
        challanDetail.setEngCapacity(1300);
        challanDetail.setSeats(5);
        challanDetail.setCylinders(4);
        challanDetail.setOwnerCnic("3520243953533");
        challanDetail.setOwnerName("ahsan Raza");
        challanDetail.setFilerStatus("NO");
        challanDetail.setTaxPaidFrom(xgc);
        challanDetail.setTaxPaidUpTo(xgc);
        challanDetail.setVehTaxPaidLifeTime("y");
        challanDetail.setVehicalStatus("5");
        challanDetail.setFitnessDate(xgc);
        challanDetail.setTotalAmount(BigDecimal.valueOf(5000));
        challanDetail.setStatusCode("ORA-00000:successFull");

        return challanDetail;
    }

    public ChallanDetail getChallanStatus(String challanNumber,String vehRegistrationNumber)throws Exception{
        XMLGregorianCalendar xgc = DatatypeFactory.newInstance().newXMLGregorianCalendar(new DateTime().toGregorianCalendar());
        ChallanDetail challanDetail=new ChallanDetail();

        challanDetail.setVCTChallanNO("505");
        challanDetail.setChallanDate("23102019");
        challanDetail.setRegistrationNo("123455");
        challanDetail.setRegistrationDate(xgc);
        challanDetail.setChassisNo("123333");
        challanDetail.setMarkerMake("honda");
//        challanDetail.setCategory("4");
//        challanDetail.setBodyType("8");
        challanDetail.setEngCapacity(1300);
        challanDetail.setSeats(5);
        challanDetail.setCylinders(4);
        challanDetail.setOwnerCnic("3520243953533");
        challanDetail.setOwnerName("ahsan Raza");
        challanDetail.setFilerStatus("NO");
        challanDetail.setTaxPaidFrom(xgc);
        challanDetail.setTaxPaidUpTo(xgc);
        challanDetail.setVehTaxPaidLifeTime("y");
//        challanDetail.setVehicalStatus("5");
        challanDetail.setFitnessDate(xgc);
        challanDetail.setTotalAmount(BigDecimal.valueOf(5000));
        challanDetail.setStatusCode("ORA-00000:Could not get user info, Contact system administrator.");

        return challanDetail;
    }
}
