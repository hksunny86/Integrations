package com.inov8.integration.channel.vrg.echallan.request;

import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.annotation.*;

@XmlType(name = "")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BillInquiry")
public class BillInquiryRequest extends Request {

    public BillInquiryRequest() {
    }

    public BillInquiryRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        super(i8SBSwitchControllerRequestVO);

    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {

        return validateCommonRequestAttributes();

    }

    @Override
    public String toString() {
        return null;
    }
}
