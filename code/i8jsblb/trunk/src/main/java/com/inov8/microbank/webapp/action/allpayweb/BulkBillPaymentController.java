package com.inov8.microbank.webapp.action.allpayweb;

import static com.inov8.microbank.webapp.action.allpayweb.AllPayWebResponseDataPopulator.isTokenValid;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.productmodule.ProdCatalogDetailListViewModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.mfsweb.MfsWebManager;
import com.inov8.microbank.mfsweb.MfsWebResponseDataPopulator;
import com.inov8.microbank.server.dao.productmodule.ProdCatalogDetailListViewDAO;
import com.inov8.microbank.server.dao.smartmoneymodule.SmartMoneyAccountDAO;
import com.inov8.microbank.server.service.financialintegrationmodule.OLAVeriflyFinancialInstitutionImpl;
import com.inov8.microbank.server.service.integration.vo.BulkBillPaymentVO;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.verifly.common.model.AccountInfoModel;

public class BulkBillPaymentController extends AdvanceFormController {
	
	private MfsWebManager mfsWebController;
	private MfsWebResponseDataPopulator mfsWebResponseDataPopulator; 
	private AllPayWebResponseDataPopulator allPayWebResponseDataPopulator;
	private ProdCatalogDetailListViewDAO prodCatalogDetailListViewDAO;
	private List<ProdCatalogDetailListViewModel> list = null;
	private SmartMoneyAccountDAO smartMoneyAccountDAO;
	private OLAVeriflyFinancialInstitutionImpl olaVeriflyFinancialInstitution;
	
	public BulkBillPaymentController() {
		setCommandName("object");
	    setCommandClass(Object.class);
	}
	@Override
	protected Object loadFormBackingObject(HttpServletRequest request) throws Exception {
		
		return new Object();
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest request) throws Exception {
		
		setReferenceData(request);
		
		return null;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse response, Object model, BindException exception) throws Exception {
		
		ModelAndView modelAndView = new ModelAndView(getSuccessView());
		
		if(!isTokenValid(request)) {			
			return new ModelAndView(AllPayWebConstant.GENERIC_PAGE.getValue());
		}
		
		AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(request);
		
		AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);
		
		String UID = requestWrapper.getParameter(CommandFieldConstants.KEY_U_ID);	
		
		AppUserModel appUserModel = allPayWebResponseDataPopulator.getAppUserModel(UID);
		
		ThreadLocalAppUser.setAppUserModel(appUserModel);

		String responseXml = mfsWebController.handleRequest(requestWrapper, CommandFieldConstants.CMD_BULK_BILL_PAYMENT);

		List<BulkBillPaymentVO> bulkBillPaymentVOList = getBulkBillPaymentVOList(responseXml);
		
		request.setAttribute("bulkBillPaymentVOList", bulkBillPaymentVOList);
		
		return modelAndView;
	}
	
	
	
	/**
	 * @param xmlResponse
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private List<BulkBillPaymentVO> getBulkBillPaymentVOList(String xmlResponse) throws ParserConfigurationException, SAXException, IOException {
		
		List<BulkBillPaymentVO> bulkBillPaymentVOList = new ArrayList<BulkBillPaymentVO>();
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputStream inputStream = new ByteArrayInputStream(xmlResponse.getBytes());
			
		org.w3c.dom.Document document = builder.parse(inputStream);
			
		NodeList billNodeList = document.getElementsByTagName("bill");
			
		for(int i = 0; i< billNodeList.getLength(); i++) {

			BulkBillPaymentVO billPaymentVO = new BulkBillPaymentVO();
			
			Node billNode = billNodeList.item(i);

			NodeList paramNodeList = billNode.getChildNodes();
				
			for(int x = 0; x<paramNodeList.getLength(); x++) {
					
				Node node = paramNodeList.item(x);

				if(node == null || node.getAttributes() == null) {
					
					continue;
				}
								
				String attributeName = node.getAttributes().getNamedItem("name").getNodeValue();
				String attributeValue = node.getTextContent();

				if(CommandFieldConstants.KEY_NAME.equalsIgnoreCase(attributeName)) {
					billPaymentVO.setProductName(attributeValue);
				}
				if(CommandFieldConstants.KEY_CUST_CODE.equalsIgnoreCase(attributeName)) {
					billPaymentVO.setConsumerNumber(attributeValue);
				}
				if(CommandFieldConstants.KEY_CW_CUSTOMER_MOBILE.equalsIgnoreCase(attributeName)) {
					billPaymentVO.setMobileNumber(attributeValue);
				}
				if(CommandFieldConstants.KEY_TX_AMOUNT.equalsIgnoreCase(attributeName)) {
					billPaymentVO.setBillAmount(Double.parseDouble(attributeValue));
				}
				if(CommandFieldConstants.KEY_STATUS.equalsIgnoreCase(attributeName)) {
					billPaymentVO.setSupProcessingStatus(attributeValue);
				}
				if(CommandFieldConstants.KEY_TX_CODE.equalsIgnoreCase(attributeName)) {
					billPaymentVO.setTransactionCode(attributeValue);
				}		
			}
			
			bulkBillPaymentVOList.add(billPaymentVO);
		}
	
		return bulkBillPaymentVOList;
	}
	
	
	
	
	
	/**
	 * @param request
	 * @throws Exception
	 */
	/**
	 * @param request
	 * @throws Exception
	 */
	/**
	 * @param request
	 * @throws Exception
	 */
	/**
	 * @param request
	 * @throws Exception
	 */
	/**
	 * @param request
	 * @throws Exception
	 */
	private void setReferenceData(HttpServletRequest request) throws Exception {
		
		AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(request);
		allPayWebResponseDataPopulator.setDefaultParams(requestWrapper);		
		
		if(list == null || list.isEmpty()) {
			
			list = this.prodCatalogDetailListViewDAO.loadCatalogProductForRetailerBulkBillPayment(5001L);
		}
		
		requestWrapper.setAttribute("ProdCatalogDetailListViewModelList", list);
				
		AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);
		
		String UID = requestWrapper.getParameter(CommandFieldConstants.KEY_U_ID);	
		
		AppUserModel appUserModel = allPayWebResponseDataPopulator.getAppUserModel(UID);
		
		ThreadLocalAppUser.setAppUserModel(appUserModel); 
				
        SmartMoneyAccountModel smartMoneyAccountModel = getDefaultOLAAccount(appUserModel);

		WorkFlowWrapper _workFlowWrapper = new WorkFlowWrapperImpl();
		_workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);

		OLAVO olavo = new OLAVO();
		olavo.setCnic(appUserModel.getNic());
		
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		switchWrapper.setOlavo(olavo);
		switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());
		switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
		switchWrapper.setWorkFlowWrapper(_workFlowWrapper);
		switchWrapper.setTransactionTransactionModel(new TransactionModel());
		switchWrapper.setBasePersistableModel(_workFlowWrapper.getSmartMoneyAccountModel() ) ;		
		switchWrapper.setBankId(Long.valueOf(requestWrapper.getParameter("BAID")));
  
        BaseWrapper baseWrapper = new BaseWrapperImpl();
	    baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.ALLPAY_WEB);     
	    baseWrapper.putObject(CommandFieldConstants.KEY_ACC_ID, smartMoneyAccountModel.getSmartMoneyAccountId()); 
			
	    AccountInfoModel accountInfoModel = new AccountInfoModel();
		accountInfoModel.setAccountNick(smartMoneyAccountModel.getName());	        
		accountInfoModel.setCustomerId(appUserModel.getAppUserId());
		accountInfoModel.setAccountInfoIdLogModelList(null);
			
		ThreadLocalActionLog.getActionLogId();

		ActionLogModel actionLogModel = new ActionLogModel();
		
		if(null == ThreadLocalActionLog.getActionLogId()){
			
			actionLogModel.setActionLogId(1L);
			ThreadLocalActionLog.setActionLogId(1L);
			
		} else {

			actionLogModel.setActionLogId(ThreadLocalActionLog.getActionLogId());
		}
		
		switchWrapper.setAccountInfoModel(accountInfoModel);
			
		olaVeriflyFinancialInstitution.checkBalanceWithoutPin(switchWrapper);
		Double balance = switchWrapper.getBalance();
			
		request.setAttribute("balance", Formatter.formatDouble(balance));
	}	
	

	private SmartMoneyAccountModel getDefaultOLAAccount(AppUserModel appUserModel) throws FrameworkCheckedException {
		
		SmartMoneyAccountModel sma = new SmartMoneyAccountModel() ;
		
		Long retailerContactId = appUserModel.getRetailerContactId() ;
		Long distributorContactId = appUserModel.getDistributorContactId() ;
		
		sma.setRetailerContactId(retailerContactId);
		sma.setDistributorContactId(distributorContactId);
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(sma) ;
		sma = allPayWebResponseDataPopulator.getSmartMoneyAccountModel(sma);
		
		return sma;
	}
	
	@Override
	protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object model, BindException exception) throws Exception {
		return onCreate(request, response, model, exception);
	}

	public MfsWebManager getMfsWebController() {
		return mfsWebController;
	}
	public void setMfsWebController(MfsWebManager mfsWebController) {
		this.mfsWebController = mfsWebController;
	}
	public MfsWebResponseDataPopulator getMfsWebResponseDataPopulator() {
		return mfsWebResponseDataPopulator;
	}
	public void setMfsWebResponseDataPopulator(MfsWebResponseDataPopulator mfsWebResponseDataPopulator) {
		this.mfsWebResponseDataPopulator = mfsWebResponseDataPopulator;
	}
	public void setAllPayWebResponseDataPopulator(AllPayWebResponseDataPopulator allPayWebResponseDataPopulator) {
		this.allPayWebResponseDataPopulator = allPayWebResponseDataPopulator;
	}
	public void setProdCatalogDetailListViewDAO(ProdCatalogDetailListViewDAO prodCatalogDetailListViewDAO) {
		this.prodCatalogDetailListViewDAO = prodCatalogDetailListViewDAO;
	}
	public void setSmartMoneyAccountDAO(SmartMoneyAccountDAO smartMoneyAccountDAO) {
		this.smartMoneyAccountDAO = smartMoneyAccountDAO;
	}
	public void setOlaVeriflyFinancialInstitution(OLAVeriflyFinancialInstitutionImpl olaVeriflyFinancialInstitution) {
		this.olaVeriflyFinancialInstitution = olaVeriflyFinancialInstitution;
	}
}