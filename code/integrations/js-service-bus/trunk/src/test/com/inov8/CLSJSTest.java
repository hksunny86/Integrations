package com.inov8;


import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.controller.I8SBSwitchController;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class CLSJSTest {
    private static Logger logger = LoggerFactory.getLogger(AMMATest.class.getSimpleName());

    @Autowired
    private I8SBSwitchController switchController;

    @Test
    public void sendImportScreening(){
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_CLSJS);
        requestVO.setRequestType(I8SBConstants.RequestType_CLSJS_ImportScreening);

        requestVO.setRequestId("20200630121312666666");
        requestVO.setCNIC("4220176541313");
        requestVO.setName("Osama bin");
        requestVO.setDateOfBirth("06-09-2021");
        requestVO.setNationality("PK");
        requestVO.setCity("Gujranwala");
        requestVO.setMobileNumber("03079278452");
        requestVO.setUserId("21");


        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        responseVO = requestVO.getI8SBSwitchControllerResponseVO();
        logger.info("Response Code : ",responseVO.getResponseCode());
    }
}
