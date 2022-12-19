package com.inov8;

import com.inov8.integration.enums.I8SBResponseCodeEnum;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.controller.I8SBSwitchController;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class HealthAndNutritionTest {
    @Autowired
    private I8SBSwitchController switchController;

    @Test
    public void getBeneficiaryDetails(){
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_BOP);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_HealthAndNutrition);
        requestVO.setRequestType(I8SBConstants.RequestType_BenificiaryDetails);

        requestVO.setStartDate("20210201120000");
        requestVO.setEndDate("20210215235959");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

    @Test
    public void getBeneficiaryVisit(){
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_BOP);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_HealthAndNutrition);
        requestVO.setRequestType(I8SBConstants.RequestType_BenificiaryVisit);

        //for Beneficiary Vist List
//        requestVO.setStartDate("20210210120000");
//        requestVO.setEndDate("20210215235959");

        //for Beneficiary Vist for first payment
        requestVO.setCNIC("3120233494352");
        requestVO.setMobileNumber("03005696352");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        assertNotNull(requestVO);
        assertNotNull(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
        assertEquals(I8SBResponseCodeEnum.PROCESSED.getValue(), requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }
}
