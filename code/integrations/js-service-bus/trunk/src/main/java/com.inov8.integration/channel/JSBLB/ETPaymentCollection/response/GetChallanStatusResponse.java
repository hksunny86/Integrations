package com.inov8.integration.channel.JSBLB.ETPaymentCollection.response;

import com.inov8.integration.channel.JSBLB.ETPaymentCollection.client.*;
import com.inov8.integration.channel.JSBLB.ETPaymentCollection.client.GetAssesmentDetailResponse;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

/**
 * Created by Inov8 on 10/18/2019.
 */
public class GetChallanStatusResponse extends Response {


    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        return null;
    }

    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO(ChallanDetail challanDetail) throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        if (challanDetail != null) {
            i8SBSwitchControllerResponseVO.setVctChallanNumber(String.valueOf(challanDetail.getVCTChallanNO()));
            i8SBSwitchControllerResponseVO.setChallanDate(String.valueOf(challanDetail.getChallanDate()));
            i8SBSwitchControllerResponseVO.setChallanStatus(challanDetail.getChallanStatus());
            i8SBSwitchControllerResponseVO.setPaymentDate(challanDetail.getPaymentDate());
            i8SBSwitchControllerResponseVO.setPaymentRecievedBy(challanDetail.getPaymentReceivedBY());
            i8SBSwitchControllerResponseVO.setRegistrationDate(String.valueOf(challanDetail.getRegistrationDate()));
            i8SBSwitchControllerResponseVO.setRegistrationNumber(challanDetail.getRegistrationNo());
            i8SBSwitchControllerResponseVO.setChassisNo(challanDetail.getChassisNo());
            i8SBSwitchControllerResponseVO.setMakerMake(challanDetail.getMarkerMake());
            i8SBSwitchControllerResponseVO.setCatagory(String.valueOf(challanDetail.getCategory()));
            i8SBSwitchControllerResponseVO.setBodyType(String.valueOf(challanDetail.getBodyType()));
            i8SBSwitchControllerResponseVO.setEngCapacity(challanDetail.getEngCapacity());
            i8SBSwitchControllerResponseVO.setSeats(challanDetail.getSeats());
            i8SBSwitchControllerResponseVO.setCyllinders(challanDetail.getCylinders());
            i8SBSwitchControllerResponseVO.setOwnerCnic(challanDetail.getOwnerCnic());
            i8SBSwitchControllerResponseVO.setOwnerName(challanDetail.getOwnerName());
            i8SBSwitchControllerResponseVO.setFilerStatus(challanDetail.getFilerStatus());
            i8SBSwitchControllerResponseVO.setTaxPaidFrom(String.valueOf(challanDetail.getTaxPaidFrom()));
            i8SBSwitchControllerResponseVO.setTaxPaidUpto(String.valueOf(challanDetail.getTaxPaidUpTo()));
            i8SBSwitchControllerResponseVO.setVehTaxPaidLifeTime(challanDetail.getVehTaxPaidLifeTime());
            i8SBSwitchControllerResponseVO.setVehicleStatus(String.valueOf(challanDetail.getVehicalStatus()));
            i8SBSwitchControllerResponseVO.setFitnessDate(String.valueOf(challanDetail.getFitnessDate()));
            i8SBSwitchControllerResponseVO.setAmount(String.valueOf(challanDetail.getTotalAmount()));
            String statusCode = challanDetail.getStatusCode();
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
