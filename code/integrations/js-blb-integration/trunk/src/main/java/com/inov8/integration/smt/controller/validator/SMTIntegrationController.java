package com.inov8.integration.smt.controller.validator;

// @Created On 4/17/2024 : Wednesday
// @Created By muhammad.aqeel

import com.inov8.integration.smt.controller.smtController.SMTController;
import com.inov8.integration.webservice.controller.SMTSwitchController;
import com.inov8.integration.webservice.vo.WebServiceVO;
import org.springframework.stereotype.Controller;

@Controller("SMTIntegrationController")
public class SMTIntegrationController implements SMTSwitchController {
    @Override
    public WebServiceVO accountInfoSmt(WebServiceVO webServiceVO) {
        return null;
    }
}
