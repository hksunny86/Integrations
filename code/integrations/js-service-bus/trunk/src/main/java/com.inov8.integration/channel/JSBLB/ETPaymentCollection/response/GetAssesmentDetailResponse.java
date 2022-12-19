package com.inov8.integration.channel.JSBLB.ETPaymentCollection.response;

import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.channel.JSBLB.ETPaymentCollection.client.*;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Inov8 on 10/15/2019.
 */
@XmlRootElement
public class GetAssesmentDetailResponse extends Response {

    //private Return aReturn = new Return();

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        return null;
    }

    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO(AssesmentDetails assesmentDetails) throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        if (assesmentDetails != null) {
            i8SBSwitchControllerResponseVO.setVehicleAssesmentNo(assesmentDetails.getVctAssesmentNO());
            i8SBSwitchControllerResponseVO.setAssesmentDate(String.valueOf(assesmentDetails.getAssesmentDate()));
            i8SBSwitchControllerResponseVO.setRegistrationDate(String.valueOf(assesmentDetails.getRegistrationDate()));
            i8SBSwitchControllerResponseVO.setRegistrationNumber(assesmentDetails.getRegistrationNo());
            i8SBSwitchControllerResponseVO.setChassisNo(assesmentDetails.getChassisNo());
            i8SBSwitchControllerResponseVO.setMakerMake(assesmentDetails.getMarkerMake());
            i8SBSwitchControllerResponseVO.setCatagory(assesmentDetails.getCategory());
            i8SBSwitchControllerResponseVO.setBodyType(assesmentDetails.getBodyType());
            i8SBSwitchControllerResponseVO.setEngCapacity(assesmentDetails.getEngCapacity());
            i8SBSwitchControllerResponseVO.setSeats(assesmentDetails.getSeats());
            i8SBSwitchControllerResponseVO.setCyllinders(assesmentDetails.getCylinders());
            i8SBSwitchControllerResponseVO.setOwnerCnic(assesmentDetails.getOwnerCnic());
            i8SBSwitchControllerResponseVO.setOwnerName(assesmentDetails.getOwnerName());
            i8SBSwitchControllerResponseVO.setFilerStatus(assesmentDetails.getFilerStatus());
            i8SBSwitchControllerResponseVO.setTaxPaidFrom(String.valueOf(assesmentDetails.getTaxPaidFrom()));
            i8SBSwitchControllerResponseVO.setTaxPaidUpto(String.valueOf(assesmentDetails.getTaxPaidUpTo()));
            i8SBSwitchControllerResponseVO.setVehTaxPaidLifeTime(assesmentDetails.getVehTaxPaidLifeTime());
            i8SBSwitchControllerResponseVO.setVehicleStatus(assesmentDetails.getVehicalStatus());
            i8SBSwitchControllerResponseVO.setFitnessDate(String.valueOf(assesmentDetails.getFitnessDate()));
            i8SBSwitchControllerResponseVO.setTotalAmount(assesmentDetails.getTotalAmount());
            String statusCode = assesmentDetails.getStatusCode();
            String[] parts = statusCode.split("-");
            String part1=parts[0];
            String part2 = parts[1];
            String [] parts2 =part2.split(":");
            String responsecode=parts2[0];
            String responsedescription=parts2[1];
            i8SBSwitchControllerResponseVO.setResponseCode(responsecode);
            i8SBSwitchControllerResponseVO.setDescription(responsedescription);


        }
        return i8SBSwitchControllerResponseVO;
    }

}


