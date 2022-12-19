package com.inov8;


import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.controller.I8SBSwitchController;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.util.EncryptionUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class VrgEChallanTest {

    @Autowired
    private I8SBSwitchController switchController;
    String key = new String("682ede816988e58fb6d057d9d85605e0");

    private String encrypt(String input) {
        return EncryptionUtil.encryptWithAES(key, input);
    }

    private String decrypt(String input) {
        return EncryptionUtil.decryptWithAES(key, input);
    }

    @Test
    public void eChallanInquiry() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_VRG_ECHALLAN);
        requestVO.setRequestType(I8SBConstants.RequestType_EChallanInquiry);
        requestVO.setUserId("itpayuser");
        requestVO.setPassword(encrypt("Elot14%12@024"));
        requestVO.setBankMnemonic("KCC");
        requestVO.setConsumerNumber("2019110510018");
        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        responseVO = requestVO.getI8SBSwitchControllerResponseVO();
        System.out.println(responseVO.getResponseCode());
        System.out.println(responseVO.getConsumerTitle());
        System.out.println(responseVO.getBillStatus());
        System.out.println(responseVO.getBillAmountAfterDueDate());
    }
    @Test
    public void eChallanPayment() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_MB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_VRG_ECHALLAN);
        requestVO.setRequestType(I8SBConstants.RequestType_EChallanPayment);
        requestVO.setUserId("itpayuser");
        requestVO.setPassword("Elot14%12@024");
        requestVO.setBankMnemonic("WMB");
        requestVO.setConsumerNumber("1000106");
        requestVO.setTransactionAmount("300");
        requestVO.setRRN("694300");
        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);

        System.out.println(requestVO.getI8SBSwitchControllerResponseVO().getResponseCode());
    }

}
