package com.inov8.microbank.webapp.action.allpayweb;

import static com.inov8.microbank.webapp.action.allpayweb.AllPayWebResponseDataPopulator.isTokenValid;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.CommissionRateModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.mfsweb.MfsWebManager;
import com.inov8.microbank.mfsweb.MfsWebResponseDataPopulator;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.webapp.action.allpayweb.formbean.AgentWebFormBean;
import com.inov8.microbank.webapp.action.allpayweb.utils.ParameterMapper;

public class DonationPaymentController extends AdvanceFormController {
	
	private MfsWebManager mfsWebController;
	private MfsWebResponseDataPopulator mfsWebResponseDataPopulator; 
	private AllPayWebResponseDataPopulator allPayWebResponseDataPopulator;
	private AgentWebManager agentWebManager;
	private CommissionManager commissionManager;
	private CommonCommandManager commonCommandManager;
	private final String PAGE_FROM = "allpay-web/donationPayment";
	
	public DonationPaymentController () {
		
		setCommandName(AgentWebFormBean.CLASS_NAME);
	    setCommandClass(AgentWebFormBean.class);
	}
	
	
	@Override
	protected Object loadFormBackingObject(HttpServletRequest request) throws Exception {
		
		AgentWebFormBean agentWebFormBean = new AgentWebFormBean();
		agentWebFormBean.setDeviceTypeId(Long.valueOf(8));
		setReferenceData(request, agentWebFormBean);
		
		return agentWebFormBean;
	}
	

	@Override
	protected Map loadReferenceData(HttpServletRequest request) throws Exception {

		return null;
	}


	@Override
	protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse response, Object formBean, BindException exception) throws Exception {	

		long start = System.currentTimeMillis();		

		AgentWebFormBean agentWebFormBean = (AgentWebFormBean)formBean;
		
		AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(request);						
		AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);		

		ModelAndView modelAndView = new ModelAndView();
		String nextView = getSuccessView();
		
		if(!isTokenValid(request)) {	
			return new ModelAndView(AllPayWebConstant.GENERIC_PAGE.getValue());
		}
		
		ParameterMapper.mapParameters(agentWebFormBean, requestWrapper);
		
		String UID = requestWrapper.getParameter(CommandFieldConstants.KEY_U_ID);
		
		requestWrapper.setAttribute("PNAME", requestWrapper.getParameter("PNAME"));

		request.setAttribute(AllPayWebConstant.PRODUCT_NAME.getValue(), agentWebFormBean.getProductName());
		request.setAttribute(AllPayWebConstant.PRODUCT_ID.getValue(), agentWebFormBean.getProductId());
		request.setAttribute("PRODUCT_ID", AllPayWebResponseDataPopulator.encrypt(String.valueOf(agentWebFormBean.getProductId())));
		request.setAttribute("SID", requestWrapper.getParameter("SID"));

		
		requestWrapper.setAttribute("reasonId", agentWebFormBean.getMiscInfo());
		
		modelAndView.setViewName(getSuccessView());
		
		Double commissionRate = getCommissionAmount(agentWebFormBean, new ProductModel(agentWebFormBean.getProductId()));
		
		if(commissionRate.doubleValue() >= Double.valueOf(agentWebFormBean.getAmount()).doubleValue()) {

			requestWrapper.setAttribute("errors", "Please Enter amount greater than Rs." + commissionRate);
			modelAndView.setViewName(PAGE_FROM);

			request.setAttribute("SID", requestWrapper.getParameter("SID"));			
		} 	
		agentWebFormBean.setCommissionAmountTotal(commissionRate.toString());
		
		modelAndView.addObject("agentWebFormBean", agentWebFormBean);
		
		logger.info("\n\nDonation Transfer Took :"+((System.currentTimeMillis() - start) / 1000)+ " Second(s)");
		
		return modelAndView;
	}	
	
	/**
	 * @param request
	 * @throws Exception
	 */
	private void setReferenceData(HttpServletRequest request, AgentWebFormBean agentWebFormBean) throws Exception {
		
		AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(request);
		allPayWebResponseDataPopulator.setDefaultParams(requestWrapper);		
		
		Long productId = null;
		
		String productIdParam = request.getParameter("PID");
		
		if(productIdParam.contains(" ")) {
			productIdParam = productIdParam.replace(" ", "+");
 		}		
		
		if(!StringUtils.isEmpty(productIdParam)) {
			
			if(StringUtils.isNumeric(productIdParam)) {
			
				productId = Long.valueOf(productIdParam);
			
			} else {
			
				productIdParam = allPayWebResponseDataPopulator.decrypt(productIdParam);
				productId = Long.valueOf(productIdParam);
			}
		}
		
		ProductModel productModel = allPayWebResponseDataPopulator.getProductModel(productId);
		
		Long reasonId = getReasonType(productModel);
		
		requestWrapper.addParameter("reasonId", reasonId.toString());
		requestWrapper.setAttribute("reasonId", reasonId.toString());
		agentWebFormBean.setMiscInfo(reasonId.toString());
		if(productModel != null) {

			if(agentWebFormBean != null) {
			
				agentWebFormBean.setProductId(productModel.getProductId());
				agentWebFormBean.setProductName(productModel.getName());
			}
			
		} else {
			requestWrapper.setAttribute("errors", "No Product Found.");
		}
	}	
	
	
	private Double getCommissionAmount(AgentWebFormBean agentWebFormBean, ProductModel productModel) {
		
		TransactionModel transactionModel = new TransactionModel();
		transactionModel.setTransactionAmount(Double.valueOf(agentWebFormBean.getAmount()));
		
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
		workFlowWrapper.setProductModel(productModel);
		workFlowWrapper.setTransactionModel(transactionModel);

		if(commonCommandManager == null) {
			commonCommandManager = (CommonCommandManager) allPayWebResponseDataPopulator.getBean("commonCommandManager");
		}
		
		Double commissionRate = commonCommandManager.getCommissionAmount(workFlowWrapper);	
		Long reasonId = (Long) workFlowWrapper.getObject("REASON_ID");
				
		agentWebFormBean.setCommissionAmountTotal(commissionRate.toString());
		agentWebFormBean.setMiscInfo(reasonId.toString());
		
		return commissionRate;
	}

/**
	 * @param request
	 * @param xmlResponse
	 */
	private Map<String, String> updateRequest(HttpServletRequest request, String xmlResponse) {

		Map<String, String> responseData = new java.util.HashMap<String, String>();

		xmlResponse = xmlResponse.replace(CommandFieldConstants.KEY_PROD_ID, "Payment for ");
		xmlResponse = xmlResponse.replace(CommandFieldConstants.KEY_TX_AMOUNT, "Transaction Amount ");
		xmlResponse = xmlResponse.replace(CommandFieldConstants.KEY_COMM_AMOUNT, "Charges ");
		xmlResponse = xmlResponse.replace(CommandFieldConstants.KEY_TX_CODE, "Transaction ID ");
		xmlResponse = xmlResponse.replace(CommandFieldConstants.KEY_TX_DATE, "Dated ");
		xmlResponse = xmlResponse.replace(CommandFieldConstants.KEY_FORMATED_BAL, "Current Balance ");	
		
		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputStream inputStream = new ByteArrayInputStream(xmlResponse.getBytes());
			
			org.w3c.dom.Document document = builder.parse(inputStream);
			
			NodeList list = document.getElementsByTagName("param");
			
			for(int i = 0; i< list.getLength(); i++) {
				
				Node node = list.item(i);

				String attributeName = node.getAttributes().item(0).getTextContent();
				String attributeValue = node.getTextContent();

				responseData.put(attributeName.trim(), attributeValue.trim());		
				
				if("Transaction ID".equals(attributeName.trim())) {

					request.setAttribute("TRXID", attributeValue);
					request.setAttribute("PID", request.getParameter("PID"));	
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return responseData;
	}	
	

	private Long getReasonType(ProductModel productModel) {
		
		Long reasonId = null;
		
		CommissionRateModel commissionRateModel = new CommissionRateModel();
		commissionRateModel.setProductId(productModel.getProductId());
		commissionRateModel.setActive(Boolean.TRUE);

		SearchBaseWrapper searchBaseWrapper = commissionManager.getCommissionRateData(commissionRateModel);

		List<CommissionRateModel> resultSetList = searchBaseWrapper.getCustomList().getResultsetList();
		
		for(CommissionRateModel _commissionRateModel : resultSetList) {
			
			reasonId = _commissionRateModel.getCommissionReasonId();
		
			if(reasonId.longValue() == 5) {
				break;
			}
		}
		
		return reasonId;
	}	
	
	@Override
	protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object model, BindException exception) throws Exception {
		return onCreate(request, response, model, exception);
	}
	

	public void setMfsWebController(MfsWebManager mfsWebController) {
		this.mfsWebController = mfsWebController;
	}
	public void setMfsWebResponseDataPopulator(MfsWebResponseDataPopulator mfsWebResponseDataPopulator) {
		this.mfsWebResponseDataPopulator = mfsWebResponseDataPopulator;
	}
	public void setAllPayWebResponseDataPopulator(AllPayWebResponseDataPopulator allPayWebResponseDataPopulator) {
		this.allPayWebResponseDataPopulator = allPayWebResponseDataPopulator;
	}	
	public void setAgentWebManager(AgentWebManager agentWebManager) {
		this.agentWebManager = agentWebManager;
	}
	public void setCommissionManager(CommissionManager commissionManager) {
		this.commissionManager = commissionManager;
	}
}