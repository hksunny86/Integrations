package com.inov8.integration.channel.CLSJS.mock;

import com.inov8.integration.channel.CLSJS.response.ScreeningResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CLSJSMock {

    private static Logger logger = LoggerFactory.getLogger(CLSJSMock.class.getSimpleName());

    public ScreeningResponse importScreeningResponse(com.inov8.integration.channel.CLSJS.client.ImportScreening request) {

        ScreeningResponse response = new ScreeningResponse();

        if (request.getCNIC().equalsIgnoreCase("3503998765627") ||
                request.getCNIC().equalsIgnoreCase("3503668754631") ||
                request.getCNIC().equalsIgnoreCase("3503998765635") ||
                request.getCNIC().equalsIgnoreCase("3503668754633") ||
                request.getCNIC().equalsIgnoreCase("3503668754646") ||
                request.getCNIC().equalsIgnoreCase("3503668754644")||
                request.getCNIC().equalsIgnoreCase("3503668754645")) {

            response.setRequestID("20200630121312666666");
            response.setCaseId("1189");
            response.setCaseStatus("No Matches");
            response.setImportStatus("Add");
            response.setIsHit("Y");
            response.setScreeningStatus("Success");
            response.setTotalCWL("0");
            response.setTotalGWL("0");
            response.setTotalPEPEDD("1");
            response.setTotalPrivate("0");
        } else if (request.getCNIC().equalsIgnoreCase("3503998765634") ||
                request.getCNIC().equalsIgnoreCase("3503998765626") ||
                request.getCNIC().equalsIgnoreCase("3503668754632") ||
                request.getCNIC().equalsIgnoreCase("3503668754640") ||
                request.getCNIC().equalsIgnoreCase("3503998765633")) {

            response.setRequestID("20200630121312666666");
            response.setCaseId("1189");
            response.setCaseStatus("Passed By Rule");
            response.setImportStatus("Add");
            response.setIsHit("Y");
            response.setScreeningStatus("Success");
            response.setTotalCWL("0");
            response.setTotalGWL("0");
            response.setTotalPEPEDD("1");
            response.setTotalPrivate("0");
        } else if (
                request.getCNIC().equalsIgnoreCase("3503998765625") ) {

            response.setRequestID("20200630121312666666");
            response.setCaseId("1189");
            response.setCaseStatus("GWL-Open");
            response.setImportStatus("Add");
            response.setIsHit("Y");
            response.setScreeningStatus("Success");
            response.setTotalCWL("0");
            response.setTotalGWL("0");
            response.setTotalPEPEDD("1");
            response.setTotalPrivate("0");
        } else if (request.getCNIC().equalsIgnoreCase("3503998765648") ||
                request.getCNIC().equalsIgnoreCase("3503998765640") ||
                request.getCNIC().equalsIgnoreCase("3503668754634") ||
                request.getCNIC().equalsIgnoreCase("3503668754639") ) {

            response.setRequestID("20200630121312666666");
            response.setCaseId("1189");
            response.setCaseStatus("Private-Open");
            response.setImportStatus("Add");
            response.setIsHit("Y");
            response.setScreeningStatus("Success");
            response.setTotalCWL("0");
            response.setTotalGWL("0");
            response.setTotalPEPEDD("1");
            response.setTotalPrivate("0");
        } else if (request.getCNIC().equalsIgnoreCase("3503998765631") ||
                request.getCNIC().equalsIgnoreCase("3503998765639") ||
                request.getCNIC().equalsIgnoreCase("3503998765642")  ||
                request.getCNIC().equalsIgnoreCase("3503668754635") ||
                request.getCNIC().equalsIgnoreCase("3503668754643")) {

            response.setRequestID("20200630121312666666");
            response.setCaseId("1189");
            response.setCaseStatus("PEP/EDD-Open");
            response.setImportStatus("Add");
            response.setIsHit("Y");
            response.setScreeningStatus("Success");
            response.setTotalCWL("0");
            response.setTotalGWL("0");
            response.setTotalPEPEDD("1");
            response.setTotalPrivate("0");
        } else if (request.getCNIC().equalsIgnoreCase("3503998765630") ||
                request.getCNIC().equalsIgnoreCase("3503668754636") ||
                request.getCNIC().equalsIgnoreCase("3503998765638") ||
                request.getCNIC().equalsIgnoreCase("3503998765650") ||
                request.getCNIC().equalsIgnoreCase("3503998765651") ||
                request.getCNIC().equalsIgnoreCase("3503998765632")) {

            response.setRequestID("20200630121312666666");
            response.setCaseId("1189");
            response.setCaseStatus("False Match");
            response.setImportStatus("Add");
            response.setIsHit("Y");
            response.setScreeningStatus("Success");
            response.setTotalCWL("0");
            response.setTotalGWL("0");
            response.setTotalPEPEDD("1");
            response.setTotalPrivate("0");
        } else if (request.getCNIC().equalsIgnoreCase("3503998765629") ||
                request.getCNIC().equalsIgnoreCase("3503998765637") ||
                request.getCNIC().equalsIgnoreCase("3503668754637")) {

            response.setRequestID("20200630121312666666");
            response.setCaseId("1189");
            response.setCaseStatus("True Match-Compliance");
            response.setImportStatus("Add");
            response.setIsHit("Y");
            response.setScreeningStatus("Success");
            response.setTotalCWL("0");
            response.setTotalGWL("0");
            response.setTotalPEPEDD("1");
            response.setTotalPrivate("0");
        } else if (request.getCNIC().equalsIgnoreCase("3503998765628") ||
                request.getCNIC().equalsIgnoreCase("3503998765636")  ||
                request.getCNIC().equalsIgnoreCase("3503668754638")  ||
                request.getCNIC().equalsIgnoreCase("3503998765649")  ||
                request.getCNIC().equalsIgnoreCase("3503998765647")) {

            response.setRequestID("20200630121312666666");
            response.setCaseId("1189");
            response.setCaseStatus("Revert to Branch");
            response.setImportStatus("Add");
            response.setIsHit("Y");
            response.setScreeningStatus("Success");
            response.setTotalCWL("0");
            response.setTotalGWL("0");
            response.setTotalPEPEDD("1");
            response.setTotalPrivate("0");
        } else if (request.getCNIC().equalsIgnoreCase("3503998765645") ||
                request.getCNIC().equalsIgnoreCase("3503998765641") ||
                request.getCNIC().equalsIgnoreCase("3503668754641") ) {

            response.setRequestID("20200630121312666666");
            response.setCaseId("1189");
            response.setCaseStatus("PEP/EDD-Open|Private-Open");
            response.setImportStatus("Add");
            response.setIsHit("Y");
            response.setScreeningStatus("Success");
            response.setTotalCWL("0");
            response.setTotalGWL("0");
            response.setTotalPEPEDD("1");
            response.setTotalPrivate("0");
        } else if (request.getCNIC().equalsIgnoreCase("3503998765646") ||
                request.getCNIC().equalsIgnoreCase("3503998765643") ||
                request.getCNIC().equalsIgnoreCase("3503668754642")
        ) {

            response.setRequestID("20200630121312666666");
            response.setCaseId("1189");
            response.setCaseStatus("GWL-Open|PEP/EDD-Open|Private-Open");
            response.setImportStatus("Add");
            response.setIsHit("Y");
            response.setScreeningStatus("Success");
            response.setTotalCWL("0");
            response.setTotalGWL("0");
            response.setTotalPEPEDD("1");
            response.setTotalPrivate("0");
        } else {

            response.setRequestID("20200630121312666666");
            response.setCaseId("1189");
            response.setCaseStatus("No Matches");
            response.setImportStatus("Add");
            response.setIsHit("Y");
            response.setScreeningStatus("Success");
            response.setTotalCWL("0");
            response.setTotalGWL("0");
            response.setTotalPEPEDD("1");
            response.setTotalPrivate("0");

        }


        return response;
    }
}
