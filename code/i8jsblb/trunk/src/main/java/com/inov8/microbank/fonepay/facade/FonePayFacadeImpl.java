package com.inov8.microbank.fonepay.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.integration.vo.MiddlewareMessageVO;
import com.inov8.integration.webservice.vo.WebServiceVO;
import com.inov8.microbank.fonepay.common.FonePayLogModel;
import com.inov8.microbank.fonepay.common.FonePayMessageVO;
import com.inov8.microbank.fonepay.model.VirtualCardModel;
import com.inov8.microbank.fonepay.model.VirtualCardReportModel;
import com.inov8.microbank.fonepay.service.FonePayManager;
import com.inov8.microbank.server.dao.securitymodule.EcofinSubAgentDAO;
import com.inov8.microbank.server.facade.CreditAccountQueingPreProcessor;

public class FonePayFacadeImpl implements FonePayFacade {

	private FrameworkExceptionTranslator frameworkExceptionTranslator;
	private FonePayManager fonePayManager;
	private CreditAccountQueingPreProcessor creditAccountQueingPreProcessor;
	
	
	@Override
	public WebServiceVO createCustomer(WebServiceVO webServiceVO,boolean isConusmerApp) throws FrameworkCheckedException {
		return fonePayManager.createCustomer(webServiceVO,isConusmerApp);
	}

	@Override
	public WebServiceVO bulkCreateCustomer(WebServiceVO webServiceVO, long segmentId) throws FrameworkCheckedException {
		return fonePayManager.bulkCreateCustomer(webServiceVO, segmentId);
	}

	@Override
	public WebServiceVO createL2Customer(WebServiceVO webServiceVO, boolean isConsumerApp) throws FrameworkCheckedException {
		return fonePayManager.createL2Customer(webServiceVO,isConsumerApp);
	}

	@Override
	public WebServiceVO makeExistingCustomerVerification(WebServiceVO webServiceVO) {
		return fonePayManager.makeExistingCustomerVerification(webServiceVO);
	}

	@Override
	public WebServiceVO verifyNewCustomer(WebServiceVO webServiceVO) {
		return fonePayManager.verifyNewCustomer(webServiceVO);
	}

	@Override
	public WebServiceVO verifyLoginCustomer(WebServiceVO webServiceVO) {
		return fonePayManager.verifyLoginCustomer(webServiceVO);
	}

	@Override
	public WebServiceVO makeVirtualCardTagging(WebServiceVO integrationMessageVO) {
		return fonePayManager.makeVirtualCardTagging(integrationMessageVO);
	}

	@Override
	public CustomList<VirtualCardReportModel> searchCards(SearchBaseWrapper wrapper) {
		return fonePayManager.searchCards(wrapper);
	}

	@Override
	public VirtualCardModel searchCardById(VirtualCardModel virtualCardModel) {
		return fonePayManager.searchCardById(virtualCardModel);
	}

	@Override
	public void updateVirtualCardStatusWithAuthorization(BaseWrapper baseWrapper) {
		 fonePayManager.updateVirtualCardStatusWithAuthorization(baseWrapper);
	}

	@Override
	public WebServiceVO makeLinkDelinkAccount(WebServiceVO webServiceVO) throws FrameworkCheckedException {
		return fonePayManager.makeLinkDelinkAccount(webServiceVO);
	}

	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}



	public void setFonePayManager(FonePayManager fonePayManager) {
		this.fonePayManager = fonePayManager;
	}

	public void setCreditAccountQueingPreProcessor(
			CreditAccountQueingPreProcessor creditAccountQueingPreProcessor) {
		this.creditAccountQueingPreProcessor = creditAccountQueingPreProcessor;
	}

	@Override
	public FonePayLogModel saveFonePayIntegrationLogModel(WebServiceVO webServiceVO,String reqType)
			throws Exception{
		try{
			return fonePayManager.saveFonePayIntegrationLogModel(webServiceVO,reqType);
		}catch (Exception ex){
	    	ex.printStackTrace();
		    throw ex ;
		}
	}

	@Override
	public void updateFonePayIntegrationLogModelForDebitCard(FonePayLogModel model, MiddlewareMessageVO webServiceVO) {

	}

	@Override
	public Boolean validateApiGeeRRN(WebServiceVO webServiceVO) throws FrameworkCheckedException {
		return fonePayManager.validateApiGeeRRN(webServiceVO);
	}

	@Override
	public WebServiceVO simpleAccountOpening(WebServiceVO webServiceVO) throws Exception {
		return fonePayManager.simpleAccountOpening(webServiceVO);
	}

	@Override
	public void updateFonePayIntegrationLogModel(FonePayLogModel model , WebServiceVO webServiceVO) {
		try{
			fonePayManager.updateFonePayIntegrationLogModel(model,webServiceVO);
		}catch (Exception ex){
			ex.printStackTrace();
		}

	}

	@Override
	public SearchBaseWrapper searchFonePayLogModels(
			SearchBaseWrapper searchBaseWrapper) {
		// TODO Auto-generated method stub
		return fonePayManager.searchFonePayLogModels(searchBaseWrapper);
	}


	@Override
	public FonePayMessageVO makevalidateExistingCustomer(FonePayMessageVO fonePayMessageVO) throws Exception {
		return fonePayManager.makevalidateExistingCustomer(fonePayMessageVO);
	}

	@Override
	public FonePayMessageVO makevalidateAgent(FonePayMessageVO fonePayMessageVO) throws Exception {
		return fonePayManager.makevalidateAgent(fonePayMessageVO);
	}

	@Override
	public SearchBaseWrapper loadVirtualCard(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		return fonePayManager.loadVirtualCard(searchBaseWrapper);
	}


	@Override
	public WebServiceVO makeChecqueBookStatus(WebServiceVO webServiceVO) throws Exception {
		return fonePayManager.makeChecqueBookStatus(webServiceVO);
	}

	@Override
	public void updateCustomerModelWithAuthorization(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		fonePayManager.updateCustomerModelWithAuthorization(baseWrapper);
		
	}



	@Override
	public WebServiceVO makeTransactionReversal(
			WebServiceVO webServiceVO) throws Exception{
		Boolean isSuccess=false;

		try {
			String fonePayTransCode=webServiceVO.getMicrobankTransactionCode();
			webServiceVO= fonePayManager.makeTransactionReversal(webServiceVO);
				isSuccess=true;
			creditAccountQueingPreProcessor.loadAndForwardAccountToQueue(webServiceVO.getMicrobankTransactionCode());
			webServiceVO.setMicrobankTransactionCode(fonePayTransCode);
		} catch (Exception e) {

			e.printStackTrace();
			if(!isSuccess){
				throw e;
			}

		}
		return webServiceVO;
	}

	@Override
	public WebServiceVO makeDebitPaymentTransactionReversal(
			WebServiceVO webServiceVO) throws Exception{
		Boolean isSuccess=false;

		try {
			String fonePayTransCode=webServiceVO.getMicrobankTransactionCode();
			webServiceVO= fonePayManager.makeDebitPaymentTransactionReversal(webServiceVO);
			isSuccess=true;
			creditAccountQueingPreProcessor.loadAndForwardAccountToQueue(webServiceVO.getMicrobankTransactionCode());
			webServiceVO.setMicrobankTransactionCode(fonePayTransCode);
		} catch (Exception e) {

			e.printStackTrace();
			if(!isSuccess){
				throw e;
			}

		}
		return webServiceVO;
	}
	@Override
	public WebServiceVO updateCardInfo(
			WebServiceVO webServiceVO) throws Exception{

		try {
			webServiceVO= fonePayManager.updateCardInfo(webServiceVO);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return webServiceVO;
	}



/*	public WorkFlowWrapper phonePayTransaction(BaseWrapper baseWrapper) throws Exception{
		try{
			   WorkFlowWrapper workFlowWrapper = taxManager.phonePayTransaction(baseWrapper);
			   creditAccountQueingPreProcessor.startProcessing(workFlowWrapper);
			   return workFlowWrapper;
		}catch (Exception ex){
			throw frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.UPDATE_ACTION);
		}
	}
*/
	
	@Override
	public FonePayMessageVO validateMPINRetryCount(WebServiceVO webServiceVO,BaseWrapper pinWrapper)
			throws Exception {
		FonePayMessageVO fonePayMessageVO = null;
		try {
			fonePayMessageVO= fonePayManager.validateMPINRetryCount(webServiceVO,pinWrapper);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return fonePayMessageVO;
	}

	@Override
	public void createMiniTransactionModel(String encryptedPin, String mobileNo,String channelId,String commandId)
			throws Exception {
		
		try {
			fonePayManager.createMiniTransactionModel(encryptedPin, mobileNo,channelId,commandId);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public SearchBaseWrapper searchFonePayTransactionDetail(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try {
			return fonePayManager.searchFonePayTransactionDetail(searchBaseWrapper);
		} catch (FrameworkCheckedException e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void sendOtpSms(String otp,String messageType,String mobileNo)
			throws FrameworkCheckedException {
		try {
			fonePayManager.sendOtpSms(otp,messageType,mobileNo);
		} catch (FrameworkCheckedException e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public WebServiceVO getOlaBalance(WebServiceVO webServiceVO)
			throws FrameworkCheckedException {
		try {
			return fonePayManager.getOlaBalance(webServiceVO);
		} catch (FrameworkCheckedException e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public WebServiceVO makevalidateCustomer(WebServiceVO fonePayMessageVO) throws Exception {
		return fonePayManager.makevalidateCustomer(fonePayMessageVO);
	}

	@Override
	public WebServiceVO makevalidateCustomerForResetPinAPI(WebServiceVO fonePayMessageVO) throws Exception {
		return fonePayManager.makevalidateCustomerForResetPinAPI(fonePayMessageVO);

	}

	@Override
	public WebServiceVO getMiniStatment(WebServiceVO webServiceVO) throws Exception {
		return fonePayManager.getMiniStatment(webServiceVO);
	}

	@Override
	public WebServiceVO makeCashIninquiry(WebServiceVO webServiceVO) throws Exception {
		return fonePayManager.makeCashIninquiry(webServiceVO);
	}

	@Override
	public FonePayLogModel saveFonePayIntegrationLogModelForDebitCardReq(MiddlewareMessageVO webServiceVO, String reqType) throws Exception {
		return null;
	}

}
