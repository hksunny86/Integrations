
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.controller.I8SBSwitchController;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class JSDebitCardApi {
    @Autowired
    I8SBSwitchController switchController;

    @Test
    public void cardReissuance() {

        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_JSDEBITCARDAPI);
        requestVO.setRequestType(I8SBConstants.RequestType_JSDEBITCARD_CardReissuance);

        requestVO.setProcessingCode("CardReIssuance");
        requestVO.setTraceNo("785888");
        requestVO.setDateTime("20210105201527");
        requestVO.setMerchantType("0088");
        requestVO.setMessageType("0200");
        requestVO.setTransactionCode("421");
        requestVO.setTransmissionDateAndTime("20170427155000");
        requestVO.setSystemTraceAuditNumber("785888");
        requestVO.setTransactionDateTime("20170427155000");
//        requestVO.setUserId("user");
//        requestVO.setPassword("password");
        requestVO.setChannelID("8");
        requestVO.setCardNumber("2205770000862430");
        requestVO.setOperation("replacement");
        requestVO.setExpiryDate("02022026");

        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        responseVO = requestVO.getI8SBSwitchControllerResponseVO();
        System.out.println(responseVO.getResponseCode());
    }
}
