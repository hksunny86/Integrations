package com.inov8.microbank.webapp.action.allpayweb;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.xml.xpath.XPathExpressionException;

import org.springframework.validation.BindException;

import com.inov8.microbank.common.util.AllPayWebUtil;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.MiniXMLUtil;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.mfsweb.MfsWebManager;
import com.inov8.microbank.mfsweb.MfsWebResponseDataPopulator;

/**
 * Created by Yasir Shabbir on 11/29/2016.
 */

/**
 *
 * @author Kashif Bashir
 * @since April, 2012
 *
 */

public class SenderReedemTransactionCodeVerificationController extends AgentWebFormController {

    public SenderReedemTransactionCodeVerificationController() {
        setCommandName("object");
        setCommandClass(Object.class);
    }

    @Override
    protected String runCommand(AllPayRequestWrapper httpServletRequest, HttpServletResponse httpServletResponse, Object model, BindException exception) throws Exception
    {

        Map<String, String> responseData = null;
        httpServletRequest.addParameter(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.CASH_TRANSFER.toString());

        String responseXML = null;
        responseXML = mfsWebController.handleRequest(httpServletRequest, CommandFieldConstants.CMD_SENDER_REEDEM);

        Boolean hasError = AllPayWebUtil.isErrorXML(responseXML);

        if (!hasError) {

            responseData = getResultData(responseXML);
            httpServletRequest.setAttribute("responseData", responseData);
        } else {

            String errors = MiniXMLUtil.getTagTextValue(responseXML, "/msg/errors/error/text()");

            httpServletRequest.setAttribute("errors", errors);
        }

        return responseXML;
    }
    private Map<String, String> getResultData(String responseXml) throws XPathExpressionException
    {

        Map<String, String> responseData = new LinkedHashMap<>();


        responseData.put("Sender Mobile", MiniXMLUtil.getTagTextValue(responseXml, "string(//trans/trn/@SWMOB)"));
        responseData.put("Sender CNIC", MiniXMLUtil.getTagTextValue(responseXml, "string(//trans/trn/@SWCNIC)"));
        responseData.put("Receiver Mobile", MiniXMLUtil.getTagTextValue(responseXml, "string(//trans/trn/@RWMOB)"));
        responseData.put("Receiver CNIC", MiniXMLUtil.getTagTextValue(responseXml, "string(//trans/trn/@RWCNIC)"));
        responseData.put("Transaction Id", MiniXMLUtil.getTagTextValue(responseXml, "string(//trans/trn/@TRXID)"));
        responseData.put("Date ",MiniXMLUtil.getTagTextValue(responseXml, "string(//trans/trn/@DATEF)"));
        responseData.put("Time", MiniXMLUtil.getTagTextValue(responseXml, "string(//trans/trn/@TIMEF)"));
        responseData.put("Amount", MiniXMLUtil.getTagTextValue(responseXml, "string(//trans/trn/@TAMTF)"));
        responseData.put("Balance", MiniXMLUtil.getTagTextValue(responseXml, "string(//trans/trn/@BALF)"));

        return responseData;
    }

    public void setMfsWebController(MfsWebManager mfsWebController) {
        this.mfsWebController = mfsWebController;
    }

    public void setMfsWebResponseDataPopulator(MfsWebResponseDataPopulator mfsWebResponseDataPopulator) {
        this.mfsWebResponseDataPopulator = mfsWebResponseDataPopulator;
    }

    public void setAllPayWebResponseDataPopulator(AllPayWebResponseDataPopulator allPayWebResponseDataPopulator) {
        this.allPayWebResponseDataPopulator = allPayWebResponseDataPopulator;
    }


}
