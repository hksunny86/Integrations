package com.inov8.microbank.fonepay.service;


import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.integration.vo.MiddlewareMessageVO;
import com.inov8.integration.webservice.vo.WebServiceVO;
import com.inov8.microbank.fonepay.common.FonePayLogModel;
import com.inov8.microbank.fonepay.common.FonePayMessageVO;
import com.inov8.microbank.fonepay.model.EcofinSubAgentModel;
import com.inov8.microbank.fonepay.model.VirtualCardModel;
import com.inov8.microbank.fonepay.model.VirtualCardReportModel;

public interface FonePayManager {

	public WebServiceVO createCustomer(WebServiceVO webServiceVO,boolean isConsumerApp) throws FrameworkCheckedException;
	public WebServiceVO bulkCreateCustomer(WebServiceVO webServiceVO, long segmentId) throws FrameworkCheckedException;

	public WebServiceVO createL2Customer(WebServiceVO webServiceVO,boolean isConsumerApp) throws FrameworkCheckedException;

	public WebServiceVO makeExistingCustomerVerification(WebServiceVO webServiceVO);
	public WebServiceVO verifyNewCustomer(WebServiceVO webServiceVO);
	public WebServiceVO verifyLoginCustomer(WebServiceVO webServiceVO);

	public WebServiceVO makeLinkDelinkAccount(WebServiceVO webServiceVO) throws FrameworkCheckedException;
	public WebServiceVO makeVirtualCardTagging(WebServiceVO integrationMessageVO);
	public CustomList<VirtualCardReportModel> searchCards(SearchBaseWrapper wrapper);
	public VirtualCardModel searchCardById(VirtualCardModel virtualCardModel);
	public void updateVirtualCardStatusWithAuthorization(BaseWrapper baseWrapper);
	public FonePayLogModel saveFonePayIntegrationLogModel(WebServiceVO webServiceVO,String reqType) throws Exception;
	public void updateFonePayIntegrationLogModel(FonePayLogModel model , WebServiceVO webServiceVO);
	public SearchBaseWrapper searchFonePayLogModels(SearchBaseWrapper searchBaseWrapper);
	public FonePayMessageVO makevalidateExistingCustomer(FonePayMessageVO fonePayMessageVO) throws Exception;
	public FonePayMessageVO makevalidateAgent(FonePayMessageVO fonePayMessageVO) throws Exception;
	SearchBaseWrapper loadVirtualCard( SearchBaseWrapper searchBaseWrapper ) throws FrameworkCheckedException;
	public void updateCustomerModelWithAuthorization(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public WebServiceVO makeTransactionReversal(WebServiceVO webServiceVO) throws Exception;
	public WebServiceVO makeChecqueBookStatus(WebServiceVO webServiceVO) throws Exception;

	public WebServiceVO makeDebitPaymentTransactionReversal(WebServiceVO webServiceVO) throws Exception;

	public WebServiceVO updateCardInfo(WebServiceVO webServiceVO) throws Exception;
	public FonePayMessageVO validateMPINRetryCount(WebServiceVO webServiceVO,BaseWrapper pinWrapper) throws Exception;
	public void createMiniTransactionModel(String encryptedPin, String mobileNo,String channelId,String commandId) throws Exception;
	
	public SearchBaseWrapper searchFonePayTransactionDetail(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	public void sendOtpSms(String otp,String messageType,String mobileNo) throws FrameworkCheckedException;
	public WebServiceVO getOlaBalance(WebServiceVO webServiceVO) throws FrameworkCheckedException;
	public WebServiceVO makevalidateCustomer(WebServiceVO fonePayMessageVO) throws Exception;
	public WebServiceVO makevalidateCustomerForResetPinAPI(WebServiceVO fonePayMessageVO) throws Exception;
	public WebServiceVO getMiniStatment(WebServiceVO webServiceVO) throws Exception;



	WebServiceVO makeCashIninquiry(WebServiceVO webServiceVO)throws Exception;

	public FonePayLogModel saveFonePayIntegrationLogModelForDebitCardReq(MiddlewareMessageVO webServiceVO,String reqType) throws Exception;
	public void updateFonePayIntegrationLogModelForDebitCard(FonePayLogModel model , MiddlewareMessageVO webServiceVO);

	Boolean validateApiGeeRRN(WebServiceVO webServiceVO) throws FrameworkCheckedException;
}
