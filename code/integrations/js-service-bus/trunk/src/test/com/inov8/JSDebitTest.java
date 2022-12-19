import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.controller.I8SBSwitchController;
import com.inov8.integration.i8sb.enums.I8SBKeysOfCollectionEnum;
import com.inov8.integration.i8sb.vo.*;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class JSDebitTest {

    @Autowired
    private I8SBSwitchController switchController;

    @Test
    public void exportDebitCardFiles() {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
        HashMap<String,List<?>> map = new HashMap<String, List<?>>();

        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_JSDEBITCARD);
        requestVO.setRequestType(I8SBConstants.RequestType_JSDEBITCARD_EXPORT);

        List<DebitCardVO> debitCardVOList = new ArrayList<>();
        List<AccountVO> accounVOList = new ArrayList<>();
        List<CustomerVO> customerVOList = new ArrayList<>();
        List<CustomerAccountVO> customerAccountList = new ArrayList<>();

        DebitCardVO debitCardVO = new DebitCardVO();
        debitCardVO.setRelationShipNo("09550201");
        debitCardVO.setAccountNo("095502010200001060");
        debitCardVO.setCardEmborsingName("Jawad Tariq");
        debitCardVO.setCardTypeCode("2");
        debitCardVO.setCardProdCode("4");
        debitCardVO.setCardBranchCode("0955");
        debitCardVO.setPrimaryCNIC("3420256381101");
        debitCardVO.setRequestType("Personalized");

        DebitCardVO debitCardVO1 = new DebitCardVO();
        debitCardVO1.setRelationShipNo("09550982");
        debitCardVO1.setAccountNo("0955201130022002");
        debitCardVO1.setCardEmborsingName("Muhammad Asif");
        debitCardVO1.setCardTypeCode("2");
        debitCardVO1.setCardProdCode("4");
        debitCardVO1.setCardBranchCode("0955");
        debitCardVO1.setPrimaryCNIC("3420256381104");
//        debitCardVO1.setIssuedDate(new DateTime());
        debitCardVO1.setRequestType("Personalized");

        DebitCardVO debitCardVO2 = new DebitCardVO();
        debitCardVO2.setRelationShipNo("09550410");
        debitCardVO2.setAccountNo("095502010200000098");
        debitCardVO2.setCardEmborsingName("Faisal Mahmood");
        debitCardVO2.setCardTypeCode("2");
        debitCardVO2.setCardProdCode("4");
        debitCardVO2.setCardBranchCode("0955");
        debitCardVO2.setPrimaryCNIC("3420256381104");
//        debitCardVO2.setIssuedDate(new DateTime());
        debitCardVO2.setRequestType("Personalized");

        DebitCardVO debitCardV03 = new DebitCardVO();
        debitCardV03.setRelationShipNo("09550295");
        debitCardV03.setAccountNo("095502010200000987");
        debitCardV03.setCardEmborsingName("Sheryar");
        debitCardV03.setCardTypeCode("2");
        debitCardV03.setCardProdCode("4");
        debitCardV03.setCardBranchCode("0955");
        debitCardV03.setPrimaryCNIC("3420256381108");
//        debitCardV03.setIssuedDate(new DateTime());
        debitCardV03.setRequestType("Personalized");

        debitCardVOList.add(debitCardVO);
        debitCardVOList.add(debitCardVO1);
        debitCardVOList.add(debitCardVO2);
        debitCardVOList.add(debitCardV03);
        map.put(I8SBKeysOfCollectionEnum.DebitCard.getValue(),debitCardVOList);


        // Populating Account List

        AccountVO accountVO = new AccountVO();
        accountVO.setAccountNo("095502010200001060");
        accountVO.setAccountTypeCode("08");
        accountVO.setAccountStatusCode("Active");
        accountVO.setBranchCode("0955");
        accountVO.setCurrencyCode("PKR");
        accountVO.setAccountTitle("Jawad Tariq");

        AccountVO accountVO2 = new AccountVO();
        accountVO2.setAccountNo("0955201130022002");
        accountVO2.setAccountTypeCode("08");
        accountVO2.setAccountStatusCode("Active");
        accountVO2.setBranchCode("0955");
        accountVO2.setCurrencyCode("PKR");
        accountVO2.setAccountTitle("Muhammad Asif");

        AccountVO accountVO3 = new AccountVO();
        accountVO3.setAccountNo("095502010200000098");
        accountVO3.setAccountTypeCode("08");
        accountVO3.setAccountStatusCode("Active");
        accountVO3.setBranchCode("0955");
        accountVO3.setCurrencyCode("PKR");
        accountVO3.setAccountTitle("Faisal Mahmood");

        AccountVO accountVO4 = new AccountVO();
        accountVO4.setAccountNo("095502010200000987");
        accountVO4.setAccountTypeCode("08");
        accountVO4.setAccountStatusCode("Active");
        accountVO4.setBranchCode("0955");
        accountVO4.setCurrencyCode("PKR");
        accountVO4.setAccountTitle("Sheryar");

        accounVOList.add(accountVO);
        accounVOList.add(accountVO2);
        accounVOList.add(accountVO3);
        accounVOList.add(accountVO4);

        map.put(I8SBKeysOfCollectionEnum.Account.getValue(),accounVOList);

        //Populating Customer List

        CustomerVO customerVO = new CustomerVO();
        customerVO.setRelationshipNo("09550201");
        customerVO.setCustomerTypeCode("02");
        customerVO.setBranchCode("0955");
        customerVO.setSegmantCode("10");
        customerVO.setCustomerStatusCode("1");
        customerVO.setFullName("Jawad Tariq");
        customerVO.setGenderCode("1");

        CustomerVO customerVO2 = new CustomerVO();
        customerVO2.setRelationshipNo("09550982");
        customerVO2.setCustomerTypeCode("02");
        customerVO2.setBranchCode("0955");
        customerVO2.setSegmantCode("10");
        customerVO2.setCustomerStatusCode("1");
        customerVO2.setFullName("Muhammad Asif");
        customerVO2.setGenderCode("1");

        CustomerVO customerVO3 = new CustomerVO();
        customerVO3.setRelationshipNo("09550410");
        customerVO3.setCustomerTypeCode("02");
        customerVO3.setBranchCode("0955");
        customerVO3.setSegmantCode("10");
        customerVO3.setCustomerStatusCode("1");
        customerVO3.setFullName("Faisal Mahmood");
        customerVO3.setGenderCode("1");

        CustomerVO customerVO4 = new CustomerVO();
        customerVO4.setRelationshipNo("09550295");
        customerVO4.setCustomerTypeCode("02");
        customerVO4.setBranchCode("0955");
        customerVO4.setSegmantCode("10");
        customerVO4.setCustomerStatusCode("1");
        customerVO4.setFullName("Amen Zafar");
        customerVO4.setGenderCode("1");

        customerVOList.add(customerVO);
        customerVOList.add(customerVO2);
        customerVOList.add(customerVO3);
        customerVOList.add(customerVO4);

        map.put(I8SBKeysOfCollectionEnum.Customer.getValue(),customerVOList);
        // Populating CustomerAccount List...

        CustomerAccountVO customerAccountVO = new CustomerAccountVO();
        customerAccountVO.setAccountNo("095502010200001060");
        customerAccountVO.setRelationshipNo("09550201");

        CustomerAccountVO customerAccountVO2 = new CustomerAccountVO();
        customerAccountVO2.setAccountNo("0955201130022002");
        customerAccountVO2.setRelationshipNo("09550982");

        CustomerAccountVO customerAccountVO3 = new CustomerAccountVO();
        customerAccountVO3.setAccountNo("095502010200000098");
        customerAccountVO3.setRelationshipNo("09550410");

        CustomerAccountVO customerAccountVO4 = new CustomerAccountVO();
        customerAccountVO4.setAccountNo("095502010200000987");
        customerAccountVO4.setRelationshipNo("09550295");

        customerAccountList.add(customerAccountVO);
        customerAccountList.add(customerAccountVO2);
        customerAccountList.add(customerAccountVO3);
        customerAccountList.add(customerAccountVO4);


        map.put(I8SBKeysOfCollectionEnum.CustomerAccount.getValue(),customerAccountList);

        requestVO.setCollectionOfList(map);
        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        responseVO = requestVO.getI8SBSwitchControllerResponseVO();
        System.out.println(responseVO.getResponseCode());

    }

    @Test
    public void importDebitCardFiles()
    {
        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
        HashMap<String,List<?>> map = new HashMap<String, List<?>>();
        requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
        requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_JSDEBITCARD);
        requestVO.setRequestType(I8SBConstants.RequestType_JSDEBITCARD_IMPORT);
        requestVO = (I8SBSwitchControllerRequestVO) switchController.invoke(requestVO);
        responseVO = requestVO.getI8SBSwitchControllerResponseVO();
        System.out.println(responseVO.getResponseCode());
    }
}
