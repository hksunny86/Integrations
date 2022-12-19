package com.inov8.integration.channel.JSBLB.ETPaymentCollection.service;

import com.inov8.integration.channel.APIGEE.response.ThirdPartyCashOut.BalanceInquiryResponse;
import com.inov8.integration.channel.AppInSnap.request.Request;
import com.inov8.integration.channel.JSBLB.ETPaymentCollection.client.*;


import com.inov8.integration.channel.JSBLB.ETPaymentCollection.mock.ETPaymentCollectionMock;
import com.inov8.integration.channel.JSBLB.ETPaymentCollection.request.GenerateChallanRequest;
import com.inov8.integration.channel.JSBLB.ETPaymentCollection.request.GetAssesmentDetailRequest;
import com.inov8.integration.channel.JSBLB.ETPaymentCollection.request.GetchallanStatusRequest;
//import com.inov8.integration.channel.rdv.ws.service.RDVWebService;
import com.inov8.integration.exception.I8SBServiceNotAvailableException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.util.JSONUtil;
import com.inov8.integration.util.XMLUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.ws.Holder;
import java.util.Date;

/**
 * Created by Inov8 on 10/15/2019.
 */
@Service
public class ETPaymentCollectionService {

    @Qualifier("ETPaymentCollection")
    @Autowired(required = false)
    private TaxInquirySoap taxInquirySoap;

    @Value("${i8sb.target.environment:#{null}}")
    private String i8sb_target_environment;

    private ETPaymentCollectionMock etPaymentCollectionMock = new ETPaymentCollectionMock();
    private static Logger logger = LoggerFactory.getLogger(ETPaymentCollectionService.class.getSimpleName());

    public AssesmentDetails getAssesmentDetailResponse(GetAssesmentDetailRequest message, I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws Exception {

        AssesmentDetails getAssesmentDetailResult = new AssesmentDetails();
        if (i8sb_target_environment.equals("mock")) {
            getAssesmentDetailResult = etPaymentCollectionMock.getassmentdetail(message.getRegisterationNumber(), message.getChesisNumber());
        } else {
            getAssesmentDetailResult = taxInquirySoap.getAssesmentDetail(message.getRegisterationNumber(), message.getChesisNumber(),message.getBankMnemonic());
        }
        logger.info(i8SBSwitchControllerRequestVO.getRequestType() + " response Received from AppInSnap at : " + new Date());
        return getAssesmentDetailResult;

    }

    public ChallanDetail generateChallanResponse(GenerateChallanRequest message, I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws Exception {
        ChallanDetail generateChallanResponse = new ChallanDetail();
        if (i8sb_target_environment.equals("mock")) {
            generateChallanResponse = etPaymentCollectionMock.generateChallanDetail(message.getAssesmentNumber(), message.getVehicleRegistrationNumber(), message.getAssesmentTotalAmount(), message.getChallanStatus());
        } else {
            generateChallanResponse = taxInquirySoap.generateChallan(message.getAssesmentNumber(),message.getVehicleRegistrationNumber(),message.getCustomerMobileNumeber(),message.getAssesmentTotalAmount(),message.getChallanStatus(),message.getBankMnemonic(),message.getAuthId(),message.getTranDate(),message.getTranTime());

        }
        logger.info(i8SBSwitchControllerRequestVO.getRequestType() + " response Received from AppInSnap at : " + new Date());
        return generateChallanResponse;
    }

    public ChallanDetail getChallanStatus(GetchallanStatusRequest message, I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws Exception {
        ChallanDetail getchallanstatus = new ChallanDetail();
        if (i8sb_target_environment.equals("mock")) {
            getchallanstatus = etPaymentCollectionMock.getChallanStatus(message.getVehicleRegistrationNumber(),message.getChallanNumber());
        } else {
            getchallanstatus = taxInquirySoap.getChallanStatus(message.getChallanNumber(), message.getVehicleRegistrationNumber(),message.getBankMnemonic());
        }
        logger.info(i8SBSwitchControllerRequestVO.getRequestType() + " response Received from AppInSnap at : " + new Date());
        return getchallanstatus;
    }

}
