package com.inov8.integration.channel.microbank.request.scorequest;

import com.inov8.integration.channel.microbank.request.Request;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.joda.time.DateTime;

public class ScoUssdRequest extends Request {

   private String transactionID;
   private String transactionDateTime;
   private String mobileNo;
   private String uSSDServiceCode;
   private String uSSDRequestString;
   private String uSSDResponseCode;
   private String location;
   private String Vlr;

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.transactionID = i8SBSwitchControllerRequestVO.getTransactionId();
        this.transactionDateTime = i8SBSwitchControllerRequestVO.getTransactionDateTime();
        this.mobileNo = i8SBSwitchControllerRequestVO.getMobileNumber();
        this.uSSDServiceCode = i8SBSwitchControllerRequestVO.getuSSDServiceCode();
        this.uSSDRequestString = i8SBSwitchControllerRequestVO.getuSSDRequestString();
        this.uSSDResponseCode = i8SBSwitchControllerRequestVO.getuSSDResponseCode();
        this.location = i8SBSwitchControllerRequestVO.getLocation();
        this.Vlr = i8SBSwitchControllerRequestVO.getVlrNumber();
    }

    @Override
    public boolean validateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        if(this.transactionID == null || this.transactionID.equals(""))
            return false;
        else if(this.transactionDateTime == null)
            return false;
        else if(this.mobileNo == null || this.mobileNo.equals(""))
            return false;
        else if(this.uSSDServiceCode == null || this.uSSDServiceCode.equals(""))
            return false;
        else if(this.uSSDRequestString == null || this.uSSDRequestString.equals(""))
            return false;
        else if(this.uSSDResponseCode == null || this.uSSDResponseCode.equals(""))
            return false;
        else
            return true;
    }

    @Override
    public void buildRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws I8SBValidationException {

    }
}
