package com.inov8.microbank.server.service.integration.dispenser;

import com.inov8.microbank.server.service.integration.vo.UtilityBillVO;
import com.inov8.microbank.server.service.switchmodule.ediVO.ISO8583VO;




public interface PaymentServicesIntgModule
{	
	UtilityBillVO getBillInfo(UtilityBillVO paymentServicesVO) ;
	ISO8583VO billPayment( ISO8583VO iSO8583VO ) throws Exception ;
	ISO8583VO rollbackBillPayment( ISO8583VO iSO8583VO ) throws Exception ;
}
