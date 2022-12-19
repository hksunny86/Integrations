/**
 * 
 */
package com.inov8.microbank.webapp.action.allpayweb;

import static com.inov8.microbank.webapp.action.allpayweb.PayCashController.isExpired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.format.DateTimeFormat;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CommissionRateModel;
import com.inov8.microbank.common.model.CommissionTransactionModel;
import com.inov8.microbank.common.model.MiniTransactionModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.dao.commissionmodule.CommissionRateDAO;
import com.inov8.microbank.server.dao.commissionmodule.CommissionTransactionDAO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;

/**
 * @author kashefbasher
 *
 */
public class AgentWebManagerImpl implements AgentWebManager {

	private final static Log logger = LogFactory.getLog(AgentWebManagerImpl.class);
	
	public CommissionRateDAO commissionRateDAO;
	public CommonCommandManager commonCommandManager;
	private CommissionTransactionDAO commissionTransactionDAO;
	public AllPayWebResponseDataPopulator allPayWebResponseDataPopulator;
	

	/* (non-Javadoc)
	 * @see com.inov8.microbank.webapp.action.allpayweb.AgentWebManager#initTransactionParams(javax.servlet.http.HttpServletRequest, java.lang.Boolean, com.inov8.microbank.common.model.TransactionCodeModel)
	 */
	@Override	
	public Boolean initTransactionParams(HttpServletRequest request, Boolean isPrintRequest, TransactionCodeModel _transactionCodeModel) {

		Boolean requestPopulated = Boolean.TRUE; 
		
		String transactionId = request.getParameter(CommandFieldConstants.KEY_TX_ID);
		
		TransactionCodeModel transactionCodeModel = null;
		
		if(_transactionCodeModel == null) {	
			
			transactionCodeModel = loadTransactionCodeByCode(transactionId);
		} else {
			
			transactionCodeModel = _transactionCodeModel;
		}
		
		if(transactionCodeModel != null) {
			
			MiniTransactionModel miniTransactionModel = loadMiniTransactionModelByTrId(transactionCodeModel, request);
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			TransactionDetailMasterModel model = new TransactionDetailMasterModel();
			model.setTransactionCode(transactionId);
			searchBaseWrapper.setBasePersistableModel(model);
			try {
				model = (TransactionDetailMasterModel) commonCommandManager.loadTransactionDetailMaster(searchBaseWrapper).getBasePersistableModel();
			} catch (FrameworkCheckedException e) {
				e.printStackTrace();
			}

			if(!isExpired(miniTransactionModel,model) | isPrintRequest) {
			
				String transactionType = getTransactionType(miniTransactionModel,model);
			
				if(transactionType == null) {

					TransactionModel transactionModel = loadTransactionModel(transactionCodeModel);
					transactionType = getTransactionType(transactionModel);
				}
				
				request.setAttribute(TRANSACTION_TYPE, transactionType);

				try {
					
					setTransactionParameter(transactionId, request, miniTransactionModel,model);
					
				} catch (FrameworkCheckedException e) {
					requestPopulated = Boolean.FALSE; 
					request.setAttribute("errors", e.getMessage());	
					logger.error(e);
				}	
			
			} else {				
				requestPopulated = Boolean.FALSE;
				request.setAttribute("errors", "Transaction '"+transactionId+"' is not longer valid.");				
			}
			
		} else {			
			requestPopulated = Boolean.FALSE;
			request.setAttribute("errors", "Invalid Transaction ID "+transactionId);
		}		
		
		return requestPopulated;
	}
	
	/**
	 * @param transactionCodeModel
	 * @param request
	 * @return
	 */
	@Override
	public MiniTransactionModel loadMiniTransactionModelByTrId(TransactionCodeModel transactionCodeModel, HttpServletRequest request) {
		
		MiniTransactionModel miniTransactionModel = null;
		   
		try {			   
			   MiniTransactionModel miniTrModel = new MiniTransactionModel();
			   miniTrModel.setTransactionCodeId(transactionCodeModel.getTransactionCodeId());
				   
			   SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
			   wrapper.setBasePersistableModel(miniTrModel);
			   wrapper = commonCommandManager.loadMiniTransaction(wrapper);
				   
			   if(wrapper.getCustomList() != null && 
					   wrapper.getCustomList().getResultsetList() != null && 
					   	wrapper.getCustomList().getResultsetList().size() > 0) {
				
				miniTransactionModel = (MiniTransactionModel)wrapper.getCustomList().getResultsetList().get(0);			   
			   }
		   
		} catch (FrameworkCheckedException e) {
			logger.error(e);
			request.setAttribute("errors", e.getLocalizedMessage());
		}
		
		return miniTransactionModel;
	}
	
	/**
	 * @param transactionModel
	 * @return
	 */
	private String getTransactionType(TransactionModel transactionModel) {

	    String transactionType = null;
	    
	    if(transactionModel != null && transactionModel.getTransactionTypeId() != null) {
	    	
	    	if(transactionModel.getTransactionTypeId().longValue() == TransactionTypeConstantsInterface.TRANSFER_IN_TX.longValue()) {
	    		
	    		transactionType = TRANSFER_IN;
	    	}
	    }		
	    
	    return transactionType;
	}

	/**
	 * @param miniTransactionModel
	 * @return
	 */
	private String getTransactionType(MiniTransactionModel miniTransactionModel,TransactionDetailMasterModel tdm) {

		String transactionType = null;

		if (miniTransactionModel != null && miniTransactionModel.getCommandId() != null) {

			if (String.valueOf(miniTransactionModel.getCommandId()).equals(CommandFieldConstants.CMD_ACCOUNT_TO_CASH)) {

				transactionType = ACCOUNT_TO_CASH;
			}

			if (String.valueOf(miniTransactionModel.getCommandId()).equals(CommandFieldConstants.CMD_CASH_TO_CASH_INFO)) {

				transactionType = CASH_TO_CASH;
			}

			if (String.valueOf(miniTransactionModel.getCommandId()).equals(CommandFieldConstants.CMD_MINI_CASHOUT)) {

				transactionType = CASH_WITHDRAW;
			}

			if (String.valueOf(miniTransactionModel.getCommandId()).equals(CommandFieldConstants.CMD_BULK_PAYMENT)) {

				transactionType = BULK_PAYMENT_WITHDRAW;
			}
			if (String.valueOf(miniTransactionModel.getCommandId()).equals(CommandFieldConstants.CMD_CUSTOMER_CASH_WITHDRAWAL_REQUEST)) {

				transactionType = CASH_WITHDRAW;
			}
			if (tdm != null) {
				if (tdm.getProductId().equals(50011L))
					transactionType = CASH_TO_CASH;

				if (tdm.getProductId().equals(ProductConstantsInterface.HRA_CASH_WITHDRAWAL))
					transactionType = "HRA Cash WithDrawal";
			}
		}
		else if (tdm != null) {
			if (tdm.getProductId().equals(ProductConstantsInterface.HRA_CASH_WITHDRAWAL))
				transactionType = "HRA Cash WithDrawal";
		}
		return transactionType;
	}
	
	
	/**
	 * @param transactionId
	 * @param request
	 * @throws FrameworkCheckedException
	 */
	@Override
	public void setTransactionParameter(String transactionId, HttpServletRequest request, MiniTransactionModel _miniTransactionModel, TransactionDetailMasterModel tdm) throws FrameworkCheckedException {
		
 		MiniTransactionModel miniTransactionModel = null;
		
		if(_miniTransactionModel == null) {
			
			TransactionCodeModel transactionCodeModel = loadTransactionCodeByCode(transactionId);
			
			if(transactionCodeModel != null) {
				
				miniTransactionModel =	loadMiniTransactionModelByTrId(transactionCodeModel, request);	
			}
			
		} else {
			
			miniTransactionModel = _miniTransactionModel;
		}

		if(miniTransactionModel != null || tdm != null) {

			String transactionType = null;
			if(miniTransactionModel == null)
				transactionType = (String) request.getAttribute(TRANSACTION_TYPE);
			else
				transactionType = getTransactionType(miniTransactionModel,tdm);
			
			if(transactionType.equals(AgentWebManager.CASH_WITHDRAW)) {
				
				updateCWRequestParameter(miniTransactionModel, request);
				
			} else if(transactionType.equals(AgentWebManager.ACCOUNT_TO_CASH)) {
				
				updateA2CRequestParameter(miniTransactionModel, request);
				
			} else if(transactionType.equals(AgentWebManager.BULK_PAYMENT_WITHDRAW)) {
				
				updateBPRequestParameter(miniTransactionModel, request);
				
			} else if(transactionType.equals(AgentWebManager.CASH_TO_CASH)) {
				updateC2CRequestParameter(miniTransactionModel,tdm, request);
			}
			else if(transactionType != null && transactionType.equals("HRA Cash WithDrawal"))
			{
				updateHRACWRequestParameter(tdm, request);
			}
		}
	}



	/* (non-Javadoc)
	 * @see com.inov8.microbank.webapp.action.allpayweb.AgentWebManager#getCommissionRate(com.inov8.microbank.common.model.ProductModel)
	 */
	@Override
	@Deprecated
	public Double getCommissionRate(ProductModel productModel) {
		
		Double rate = 0.0D;
		
		CommissionRateModel example = new CommissionRateModel();
		example.setProductIdProductModel(productModel);
		
		CustomList<CommissionRateModel> commissionRateList = commissionRateDAO.findByExample(example, null);
		
		for(CommissionRateModel commissionRate : commissionRateList.getResultsetList()) {
			
			if(commissionRate.getActive()) {
				rate = commissionRate.getRate();
				break;
			}
		}		
		
		return rate;
	}

	/* (non-Javadoc)
	 * @see com.inov8.microbank.webapp.action.allpayweb.AgentWebManager#getCommissionRate(com.inov8.microbank.common.model.ProductModel)
	 */

	public Double getCommissionAmount(WorkFlowWrapper _workFlowWrapper) {
		
		SegmentModel segmentModel = new SegmentModel();
		segmentModel.setSegmentId(CommissionConstantsInterface.DEFAULT_SEGMENT_ID);
		_workFlowWrapper.setSegmentModel(segmentModel);
		
		Double _commissionAmount = 0.0D;
		
		try {
			
			CommissionWrapper commissionWrapper = commonCommandManager.calculateCommission(_workFlowWrapper);
			
			List<CommissionRateModel> commissionRateModelArray = (ArrayList<CommissionRateModel>) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_RATE_LIST);
			
			if(commissionRateModelArray != null && !commissionRateModelArray.isEmpty()) {
				
				_commissionAmount = commissionRateModelArray.get(0).getRate();
				
			} else {
				
				logger.error("No Commission Amount Defined or Check Transaction Amount vs Commission Rate's range");
			}
						
		} catch (FrameworkCheckedException e) {
			e.printStackTrace();
		}
		
		return _commissionAmount;
		
	}
	
	
	/**
	 * @param transactionCode
	 * @param request
	 * @throws FrameworkCheckedException
	 * @Deprecated use AgentWebManager.updateBillPaymentRequestParameter(..)
	 */
	@Override
	public void updateBillPaymentRequestParameter(String transactionCode, HttpServletRequest request) throws FrameworkCheckedException {
		
		TransactionCodeModel transactionCodeModel = loadTransactionCodeByCode(transactionCode.trim());
		
		if(transactionCodeModel != null) {
		
		TransactionDetailModel transactionDetailModel = loadTransactionDetailModel(transactionCodeModel, request);
		
		TransactionModel transactionModel = loadTransactionModel(transactionCodeModel);
		
		String transactionType = null;
		
			if(transactionDetailModel != null & transactionModel != null) {
				
				request.setAttribute(CommandFieldConstants.KEY_TX_ID, transactionCode);
				request.setAttribute("CONSUMER_NUMBER", transactionDetailModel.getConsumerNo());
				request.setAttribute(CommandFieldConstants.KEY_PROD_ID, transactionDetailModel.getProductId());
				
				ProductModel productModel = transactionDetailModel.getProductIdProductModel();
				
				if(productModel != null) {
				
					String trxType = productModel.getName();
							
					if(!StringUtil.isNullOrEmpty(productModel.getBillType())) {
						trxType += " "+ productModel.getBillType();
					}
					
					request.setAttribute("TRANSACTION_TYPE", trxType);
				}
				
				request.setAttribute("TRANSACTION_AMOUNT", Formatter.formatDouble(transactionModel.getTransactionAmount()));
				request.setAttribute("TRANSACTION_DATE", DateTimeFormat.forPattern("dd/MM/yyyy").print(transactionCodeModel.getCreatedOn().getTime()));
			    request.setAttribute("TRANSACTION_TIME", DateTimeFormat.forPattern("h:mm a").print(transactionCodeModel.getCreatedOn().getTime()));				
				request.setAttribute("TOTAL_AMOUNT", Formatter.formatDouble(transactionModel.getTotalAmount()));

				Map<String, String> customerInfoMap = getBBCustomerName(transactionDetailModel.getCustomField6());
				
				request.setAttribute("CUSTOMER_MOBILE", transactionDetailModel.getCustomField6());		
				request.setAttribute("CUSTOMER_NAME", customerInfoMap.get("MS_CUSTOMER_NAME"));
			    request.setAttribute("MS_ISDN_MOBILE", customerInfoMap.get("MS_ISDN_MOBILE"));
			    request.setAttribute("MS_CUSTOMER_CNIC", customerInfoMap.get("MS_CUSTOMER_CNIC"));				

			    if(isInclusiveTransaction(transactionDetailModel.getTransactionDetailId())) {

					request.setAttribute("DEDUCTION_AMOUNT", Formatter.formatDouble(0D));

			    } else {

					request.setAttribute("DEDUCTION_AMOUNT", Formatter.formatDouble(transactionModel.getTotalCommissionAmount()));
				}		
			    
				if(!transactionDetailModel.getSettled()) {
					transactionType = transactionType + " (Request)";
					request.setAttribute("LEG", "1");			
				} else {
					transactionType = transactionType + " (Receipt)";
					request.setAttribute("LEG", "2");							
				}			    
			}			
		}
	}	

	
	/**
	 * @param transactionCode
	 * @param request
	 * @throws FrameworkCheckedException
	 */
	public void updateCashDepositRequestParameter(String transactionCode, HttpServletRequest request) throws FrameworkCheckedException {
		
		TransactionCodeModel transactionCodeModel = loadTransactionCodeByCode(transactionCode.trim());
		
		if(transactionCodeModel != null) {
		
			TransactionDetailModel transactionDetailModel = loadTransactionDetailModel(transactionCodeModel, request);
		
			TransactionModel transactionModel = loadTransactionModel(transactionCodeModel);
			
			if(transactionDetailModel != null) {
				
				request.setAttribute(CommandFieldConstants.KEY_TX_ID, transactionCode);
				request.setAttribute("CONSUMER_NUMBER", transactionDetailModel.getConsumerNo());
				request.setAttribute(CommandFieldConstants.KEY_PROD_ID, transactionDetailModel.getProductId());
				
				if(transactionDetailModel.getProductId().longValue() == 50002L) {
				
					request.setAttribute("TRANSACTION_TYPE", "Cash Deposit");
				}

				request.setAttribute("TRANSACTION_DATE", DateTimeFormat.forPattern("dd/MM/yyyy").print(transactionCodeModel.getCreatedOn().getTime()));
			    request.setAttribute("TRANSACTION_TIME", DateTimeFormat.forPattern("h:mm a").print(transactionCodeModel.getCreatedOn().getTime()));
			    
				request.setAttribute("TRANSACTION_AMOUNT", Formatter.formatDouble(transactionModel.getTransactionAmount()));
				request.setAttribute("TOTAL_AMOUNT", Formatter.formatDouble(transactionModel.getTotalAmount()));
				
				Map<String, String> customerInfoMap = getBBCustomerName(transactionDetailModel.getConsumerNo());
				
				request.setAttribute("CUSTOMER_MOBILE", customerInfoMap.get("MS_ISDN_MOBILE"));		
				request.setAttribute("CUSTOMER_NAME", customerInfoMap.get("MS_CUSTOMER_NAME"));
			    request.setAttribute("MS_ISDN_MOBILE", customerInfoMap.get("MS_CUSTOMER_NAME"));
			    request.setAttribute("MS_CUSTOMER_CNIC", customerInfoMap.get("MS_CUSTOMER_CNIC"));			

			    if(isInclusiveTransaction(transactionDetailModel.getTransactionDetailId())) {

					request.setAttribute("DEDUCTION_AMOUNT", Formatter.formatDouble(0D));

			    } else {

					request.setAttribute("DEDUCTION_AMOUNT", Formatter.formatDouble(transactionModel.getTotalCommissionAmount()));
				}
 			}			
		}
	}	
	

	/* (non-Javadoc)
	 * @see com.inov8.microbank.webapp.action.allpayweb.AgentWebManager#isInclusiveTransaction(java.lang.Long)
	 */
	public Boolean isInclusiveTransaction(Long transactionDetailId) {		
	    
		Boolean isInclusiveTransaction = Boolean.TRUE;
	    CommissionTransactionModel commissionTransactionModel = new CommissionTransactionModel();
	    commissionTransactionModel.setTransactionDetailId(transactionDetailId);
	    
	    CustomList<CommissionTransactionModel> customList = commissionTransactionDAO.findByExample(commissionTransactionModel, null);
					    
	    if(!customList.getResultsetList().isEmpty()) {
	    	
	    	CommissionTransactionModel _commissionTransactionModel = customList.getResultsetList().get(0);
	    	CommissionRateModel commissionRateModel = _commissionTransactionModel.getRelationCommissionRateIdCommissionRateModel();
	    	
		    if(commissionRateModel.getCommissionReasonId() != null && commissionRateModel.getCommissionReasonId().longValue() == 5L) {
		    	
		    	isInclusiveTransaction = Boolean.FALSE;
		    }
	    }
	    
	    return isInclusiveTransaction;
	}
	
	/**
	 * @param transactionId
	 * @return
	 */
	@Override
	public TransactionCodeModel loadTransactionCodeByCode(String transactionId) {
		
		logger.info("[AgentWebManagerImpl.loadTransactionCodeByCode] Transaction ID: " + transactionId );
		
		TransactionCodeModel transactionCodeModel = new TransactionCodeModel();
		transactionCodeModel.setCode(transactionId);	
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(transactionCodeModel);
		
		try {
			
			baseWrapper = commonCommandManager.loadTransactionCodeByCode(baseWrapper);
			
			if(baseWrapper != null && baseWrapper.getBasePersistableModel() != null) {
				   
				return ((TransactionCodeModel)baseWrapper.getBasePersistableModel());	
			}
			
		} catch (FrameworkCheckedException e) {
			logger.error(e);
		}   
		
		return null;
	}	
	
	
	/**
	 * @param mobileNumber
	 * @return
	 */
	private Map<String, String> getBBCustomerName(String mobileNumber) {
		
		Map<String, String> customerInfoMap = new HashMap<String, String>(0);
		
		String _customerName = "";
		String _mobileNumber = "";
		String _cnic = "";
		
		AppUserModel customerModel = new AppUserModel();
		customerModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
		customerModel.setMobileNo(mobileNumber);
		SearchBaseWrapper sBaseWrapper = new SearchBaseWrapperImpl();
		sBaseWrapper.setBasePersistableModel(customerModel);
		try
		{
			sBaseWrapper = commonCommandManager.loadAppUserByMobileNumberAndType(sBaseWrapper);
		}
		catch(Exception e)
		{
			logger.error(e.getStackTrace());
			throw new WorkFlowException("Invalid Customer");
		}
		
		
		if (sBaseWrapper != null && sBaseWrapper.getBasePersistableModel()!= null) {			
		
			customerModel = (AppUserModel) sBaseWrapper.getBasePersistableModel();
			
			if(customerModel.getFirstName().equalsIgnoreCase(customerModel.getLastName())) {
				
				_customerName = customerModel.getFirstName();
						
			} else {
						
				_customerName = customerModel.getFirstName() + " " + customerModel.getLastName();
			}	
			
			_mobileNumber = customerModel.getMobileNo();
			_cnic = customerModel.getNic();
		}

		customerInfoMap.put("MS_ISDN_MOBILE", _mobileNumber);
		customerInfoMap.put("MS_CUSTOMER_CNIC", _cnic);
		customerInfoMap.put("MS_CUSTOMER_NAME", _customerName);
		
		return customerInfoMap;
	}	
	
	
	/**
	 * @param transactionCodeModel
	 * @param request
	 * @return
	 */
	@Override
	public TransactionDetailModel loadTransactionDetailModel(TransactionCodeModel transactionCodeModel, HttpServletRequest request) {
		
		TransactionDetailModel transactionDetailModel = null;
		   
		try {
				   
			SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
			wrapper.setBasePersistableModel(transactionCodeModel);
			wrapper = commonCommandManager.loadTransactionByTransactionCode(wrapper);
			
			TransactionModel transactionModel = (TransactionModel) wrapper.getBasePersistableModel();
			
			if(transactionModel != null) {
				
				wrapper.setBasePersistableModel(transactionModel);
				commonCommandManager.loadTransactionDetail(wrapper);
				transactionDetailModel = (TransactionDetailModel) wrapper.getBasePersistableModel();
			}
		   
		} catch (FrameworkCheckedException e) {
			logger.error(e);
			request.setAttribute("errors", e.getLocalizedMessage());
		}
		
		return transactionDetailModel;
	}	
	
	
	/**
	 * @param transactionCodeModel
	 * @return
	 */
	public TransactionModel loadTransactionModel(TransactionCodeModel transactionCodeModel) {
		
		TransactionModel transactionModel = null;
		   
		try {
				   
			SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
			wrapper.setBasePersistableModel(transactionCodeModel);
			wrapper = commonCommandManager.loadTransactionByTransactionCode(wrapper);
			
			transactionModel = (TransactionModel) wrapper.getBasePersistableModel();

		} catch (FrameworkCheckedException e) {
			logger.error(e);
		}
		
		return transactionModel;
	}		
	
	/**
	 * @param transactionId
	 * @param request
	 * @throws FrameworkCheckedException
	 */
	private void updateCWRequestParameter(MiniTransactionModel miniTransactionModel, HttpServletRequest request) throws FrameworkCheckedException {
		Double totalAmount = Double.parseDouble( Formatter.formatDouble(miniTransactionModel.getBAMT().doubleValue()));
		if(miniTransactionModel.getTPAM() != null){
			totalAmount += Double.parseDouble( Formatter.formatDouble( miniTransactionModel.getTPAM().doubleValue()));
		}
		request.setAttribute(CommandFieldConstants.KEY_TX_ID, miniTransactionModel.getTransactionCodeIdTransactionCodeModel().getCode());
		request.setAttribute("TRANSACTION_DATE", DateTimeFormat.forPattern("dd/MM/yyyy").print(miniTransactionModel.getCreatedOn().getTime()));
		request.setAttribute("TRANSACTION_TIME", DateTimeFormat.forPattern("h:mm a").print(miniTransactionModel.getCreatedOn().getTime()));
		request.setAttribute("TRANSACTION_AMOUNT", Formatter.formatDouble(miniTransactionModel.getBAMT().doubleValue()));
		request.setAttribute("DEDUCTION_AMOUNT", Formatter.formatDouble( miniTransactionModel.getTPAM().doubleValue()));
		request.setAttribute("TOTAL_AMOUNT", totalAmount);
//		request.setAttribute("MS_ISDN_MOBILE", miniTransactionModel.getMobileNo());
		request.setAttribute(CommandFieldConstants.KEY_PROD_ID, 50006);		
		request.setAttribute(CommandFieldConstants.KEY_PIN, request.getParameter(CommandFieldConstants.KEY_PIN));
		
		Map<String, String> customerInfoMap = getBBCustomerName(miniTransactionModel.getMobileNo());
		
		request.setAttribute("CUSTOMER_MOBILE", customerInfoMap.get("MS_ISDN_MOBILE"));		
		request.setAttribute("CUSTOMER_NAME", customerInfoMap.get("MS_CUSTOMER_NAME"));
	    request.setAttribute("MS_ISDN_MOBILE", customerInfoMap.get("MS_ISDN_MOBILE"));
	    request.setAttribute("MS_CUSTOMER_CNIC", customerInfoMap.get("MS_CUSTOMER_CNIC"));
	}

	private void updateHRACWRequestParameter(TransactionDetailMasterModel tdm, HttpServletRequest request) throws FrameworkCheckedException {
		Double totalAmount = Double.parseDouble( Formatter.formatDouble(tdm.getTotalAmount().doubleValue()));
		/*if(tdm.getTransactionAmount() != null){
			totalAmount += Double.parseDouble( Formatter.formatDouble( miniTransactionModel.getTPAM().doubleValue()));
		}*/
		request.setAttribute(CommandFieldConstants.KEY_TX_ID, tdm.getTransactionCode());
		request.setAttribute("TRANSACTION_DATE", DateTimeFormat.forPattern("dd/MM/yyyy").print(tdm.getCreatedOn().getTime()));
		request.setAttribute("TRANSACTION_TIME", DateTimeFormat.forPattern("h:mm a").print(tdm.getCreatedOn().getTime()));
		request.setAttribute("TRANSACTION_AMOUNT", totalAmount);
		//request.setAttribute("DEDUCTION_AMOUNT", Formatter.formatDouble( tdm.getC.doubleValue()));
		request.setAttribute("TOTAL_AMOUNT", totalAmount);
//		request.setAttribute("MS_ISDN_MOBILE", miniTransactionModel.getMobileNo());
		request.setAttribute(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.HRA_CASH_WITHDRAWAL);
		request.setAttribute(CommandFieldConstants.KEY_PIN, request.getParameter(CommandFieldConstants.KEY_PIN));

		Map<String, String> customerInfoMap = getBBCustomerName(tdm.getRecipientMobileNo());

		request.setAttribute("WALKIN_REC_MSISDN", customerInfoMap.get("MS_ISDN_MOBILE"));
		request.setAttribute("CUSTOMER_NAME", customerInfoMap.get("MS_CUSTOMER_NAME"));
		request.setAttribute("WALKIN_REC_MSISDN", customerInfoMap.get("MS_ISDN_MOBILE"));
		request.setAttribute("WALKIN_REC_CNIC", customerInfoMap.get("MS_CUSTOMER_CNIC"));
	}
	
	
	/**
	 * @param miniTransactionModel
	 * @param request
	 * @throws FrameworkCheckedException
	 */
	private void updateA2CRequestParameter(MiniTransactionModel miniTransactionModel, HttpServletRequest request) throws FrameworkCheckedException {
	    
	    request.setAttribute(CommandFieldConstants.KEY_TX_ID, miniTransactionModel.getTransactionCodeIdTransactionCodeModel().getCode());
	    request.setAttribute("TRANSACTION_DATE", DateTimeFormat.forPattern("dd/MM/yyyy").print(miniTransactionModel.getCreatedOn().getTime()));
	    request.setAttribute("TRANSACTION_TIME", DateTimeFormat.forPattern("h:mm a").print(miniTransactionModel.getCreatedOn().getTime()));
	    request.setAttribute("TRANSACTION_AMOUNT", Formatter.formatDouble(miniTransactionModel.getTAMT().doubleValue()));
	    request.setAttribute("DEDUCTION_AMOUNT", Formatter.formatDouble( miniTransactionModel.getTPAM().doubleValue()));
	    request.setAttribute("TOTAL_AMOUNT", Double.parseDouble( Formatter.formatDouble( miniTransactionModel.getBAMT().doubleValue())));
//	    request.setAttribute("MS_ISDN_MOBILE", miniTransactionModel.getMobileNo());
		request.setAttribute("CUSTOMER_NAME", "Walkin Customer");
		request.setAttribute(CommandFieldConstants.KEY_PIN, request.getParameter(CommandFieldConstants.KEY_PIN));
		
		TransactionDetailModel transactionDetailModel = 
								loadTransactionDetailModel(miniTransactionModel.getTransactionCodeIdTransactionCodeModel(), request);
		
		if(transactionDetailModel != null) {
			request.setAttribute(CommandFieldConstants.KEY_WALKIN_RECEIVER_MSISDN, transactionDetailModel.getCustomField5());
			request.setAttribute(CommandFieldConstants.KEY_WALKIN_SENDER_MSISDN, transactionDetailModel.getCustomField6());
			request.setAttribute(CommandFieldConstants.KEY_WALKIN_SENDER_CNIC, transactionDetailModel.getCustomField7());
			request.setAttribute(CommandFieldConstants.KEY_WALKIN_RECEIVER_CNIC, transactionDetailModel.getCustomField9());
			request.setAttribute(CommandFieldConstants.KEY_PROD_ID, transactionDetailModel.getProductId());
		    request.setAttribute("MS_ISDN_MOBILE", transactionDetailModel.getCustomField5());
		    request.setAttribute("MS_CUSTOMER_CNIC", transactionDetailModel.getCustomField9());
		}	
		
		logger.info("[AgentWebManagerImpl.updateA2CRequestParameter] Trx ID:" +  miniTransactionModel.getTransactionCodeIdTransactionCodeModel().getCode() + 
				" Agent 2 AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + 
				" Walkin Receiver CNIC: " + transactionDetailModel.getCustomField9());
	}
	
	
	/**
	 * @param miniTransactionModel
	 * @param request
	 * @throws FrameworkCheckedException
	 */
	private void updateBPRequestParameter(MiniTransactionModel miniTransactionModel, HttpServletRequest request) throws FrameworkCheckedException {
	    
	    request.setAttribute(CommandFieldConstants.KEY_TX_ID, miniTransactionModel.getTransactionCodeIdTransactionCodeModel().getCode());
	    request.setAttribute("TRANSACTION_DATE", DateTimeFormat.forPattern("dd/MM/yyyy").print(miniTransactionModel.getCreatedOn().getTime()));
	    request.setAttribute("TRANSACTION_TIME", DateTimeFormat.forPattern("h:mm a").print(miniTransactionModel.getCreatedOn().getTime()));
	    request.setAttribute("TRANSACTION_AMOUNT", Formatter.formatDouble(miniTransactionModel.getTAMT().doubleValue() - miniTransactionModel.getCAMT().doubleValue()));
	    request.setAttribute("DEDUCTION_AMOUNT", Formatter.formatDouble( miniTransactionModel.getCAMT().doubleValue()));
	    request.setAttribute("TOTAL_AMOUNT", Double.parseDouble( Formatter.formatDouble( miniTransactionModel.getTAMT().doubleValue()+miniTransactionModel.getTPAM().doubleValue())));
	    request.setAttribute("CW_TOTOAL_AMOUNT_FOR_CUSTOMER", Double.parseDouble( Formatter.formatDouble( miniTransactionModel.getTAMT().doubleValue())));
		request.setAttribute("CUSTOMER_NAME", "Walkin Customer");
		request.setAttribute(CommandFieldConstants.KEY_PIN, request.getParameter(CommandFieldConstants.KEY_PIN));
		
		TransactionDetailModel transactionDetailModel = 
								loadTransactionDetailModel(miniTransactionModel.getTransactionCodeIdTransactionCodeModel(), request);
		
		if(transactionDetailModel != null) {
			request.setAttribute(CommandFieldConstants.KEY_WALKIN_RECEIVER_MSISDN, transactionDetailModel.getCustomField5());
			request.setAttribute(CommandFieldConstants.KEY_WALKIN_SENDER_MSISDN, transactionDetailModel.getCustomField6());
			request.setAttribute(CommandFieldConstants.KEY_WALKIN_SENDER_CNIC, transactionDetailModel.getCustomField7());
			request.setAttribute(CommandFieldConstants.KEY_WALKIN_RECEIVER_CNIC, transactionDetailModel.getCustomField9());
			request.setAttribute(CommandFieldConstants.KEY_PROD_ID, transactionDetailModel.getProductId());				
		    request.setAttribute("MS_ISDN_MOBILE", transactionDetailModel.getCustomField5());
		    request.setAttribute("MS_CUSTOMER_CNIC", transactionDetailModel.getCustomField9());				

			String transactionType = (String) request.getAttribute(TRANSACTION_TYPE);
			
			if(!transactionDetailModel.getSettled()) {
				transactionType = transactionType + " (Request)";
				request.setAttribute("LEG", "1");			
			} else {
				transactionType = transactionType + " (Receipt)";
				request.setAttribute("LEG", "2");							
			}
			request.setAttribute(TRANSACTION_TYPE, transactionType);
			
			logger.info("[AgentWebManagerImpl.updateC2CRequestParameter] Trx ID:" +  miniTransactionModel.getTransactionCodeIdTransactionCodeModel().getCode() + 
							" Agent 2 AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + 
							" Walkin Sender CNIC: " + transactionDetailModel.getCustomField7() + 
							" Walkin Receiver CNIC: " + transactionDetailModel.getCustomField9());
		}			
	}
	
	
	/**
	 * @param miniTransactionModel
	 * @param request
	 * @throws FrameworkCheckedException
	 */
	private void updateC2CRequestParameter(MiniTransactionModel miniTransactionModel,TransactionDetailMasterModel tdm, HttpServletRequest request) throws FrameworkCheckedException {

		String trxCode = null;
		if(miniTransactionModel != null)
		{
			trxCode = miniTransactionModel.getTransactionCodeIdTransactionCodeModel().getCode();
			request.setAttribute(CommandFieldConstants.KEY_TX_ID, trxCode);
			request.setAttribute("TRANSACTION_DATE", DateTimeFormat.forPattern("dd/MM/yyyy").print(miniTransactionModel.getUpdatedOn().getTime()));
			request.setAttribute("TRANSACTION_TIME", DateTimeFormat.forPattern("h:mm a").print(miniTransactionModel.getUpdatedOn().getTime()));
			request.setAttribute("TRANSACTION_AMOUNT", Formatter.formatDouble(miniTransactionModel.getTAMT().doubleValue()));
			request.setAttribute("DEDUCTION_AMOUNT", Formatter.formatDouble( miniTransactionModel.getTPAM().doubleValue()));
			request.setAttribute("TOTAL_AMOUNT", Double.parseDouble( Formatter.formatDouble( miniTransactionModel.getTAMT().doubleValue()+miniTransactionModel.getTPAM().doubleValue())));
			request.setAttribute("CW_TOTOAL_AMOUNT_FOR_CUSTOMER", Double.parseDouble( Formatter.formatDouble( miniTransactionModel.getTAMT().doubleValue())));
			request.setAttribute("CUSTOMER_NAME", "Walkin Customer");
			request.setAttribute(CommandFieldConstants.KEY_PIN, request.getParameter(CommandFieldConstants.KEY_PIN));
		}
		else
		{
			trxCode = tdm.getTransactionCode();
			request.setAttribute(CommandFieldConstants.KEY_TX_ID, trxCode);
			request.setAttribute("TRANSACTION_DATE", DateTimeFormat.forPattern("dd/MM/yyyy").print(tdm.getCreatedOn().getTime()));
			request.setAttribute("TRANSACTION_TIME", DateTimeFormat.forPattern("h:mm a").print(tdm.getUpdatedOn().getTime()));
			request.setAttribute("TRANSACTION_AMOUNT", Formatter.formatDouble(tdm.getTransactionAmount().doubleValue()));
			request.setAttribute("DEDUCTION_AMOUNT", Formatter.formatDouble( tdm.getTotalAmount().doubleValue()));
			request.setAttribute("TOTAL_AMOUNT", Double.parseDouble( Formatter.formatDouble( tdm.getTotalAmount().doubleValue())));
			request.setAttribute("CW_TOTOAL_AMOUNT_FOR_CUSTOMER", Double.parseDouble( Formatter.formatDouble( tdm.getTotalAmount().doubleValue())));
			request.setAttribute("CUSTOMER_NAME", "Walkin Customer");
			request.setAttribute("OTP","0");
			request.setAttribute(CommandFieldConstants.KEY_PIN, request.getParameter(CommandFieldConstants.KEY_PIN));
		}
		
		TransactionDetailModel transactionDetailModel = null;
		if(miniTransactionModel != null)
			transactionDetailModel = loadTransactionDetailModel(miniTransactionModel.getTransactionCodeIdTransactionCodeModel(), request);
		else
		{
			TransactionCodeModel transactionCodeModel = new TransactionCodeModel();
			transactionCodeModel.setTransactionCodeId(tdm.getTransactionCodeId());
			transactionDetailModel = loadTransactionDetailModel(transactionCodeModel,request);
		}
		
		if(transactionDetailModel != null) {
			request.setAttribute(CommandFieldConstants.KEY_WALKIN_RECEIVER_MSISDN, transactionDetailModel.getCustomField5());
			request.setAttribute(CommandFieldConstants.KEY_WALKIN_SENDER_MSISDN, transactionDetailModel.getCustomField6());
			request.setAttribute(CommandFieldConstants.KEY_WALKIN_SENDER_CNIC, transactionDetailModel.getCustomField7());
			request.setAttribute(CommandFieldConstants.KEY_WALKIN_RECEIVER_CNIC, transactionDetailModel.getCustomField9());
			request.setAttribute(CommandFieldConstants.KEY_PROD_ID, transactionDetailModel.getProductId());				
		    request.setAttribute("MS_ISDN_MOBILE", transactionDetailModel.getCustomField5());
		    request.setAttribute("MS_CUSTOMER_CNIC", transactionDetailModel.getCustomField9());				

			String transactionType = (String) request.getAttribute(TRANSACTION_TYPE);
			
			if(!transactionDetailModel.getSettled()) {
				transactionType = transactionType + " (Request)";
				request.setAttribute("LEG", "1");			
			} else {
				transactionType = transactionType + " (Receipt)";
				request.setAttribute("LEG", "2");							
			}
			request.setAttribute(TRANSACTION_TYPE, transactionType);
			
			logger.info("[AgentWebManagerImpl.updateC2CRequestParameter] Trx ID:" +  trxCode +
							" Agent 2 AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + 
							" Walkin Sender CNIC: " + transactionDetailModel.getCustomField7() + 
							" Walkin Receiver CNIC: " + transactionDetailModel.getCustomField9());
		}			
	}
	
	/*
	public Boolean checkProductLimit(Long productId, Double amount, HttpServletRequest request) {
		
		logger.info("[AgentWebManagerImpl.checkProductLimit] productId:"+productId);
		
		Boolean isValidAmount = Boolean.TRUE;
		
		ProductModel productModel = allPayWebResponseDataPopulator.getProductModel(productId);
		
		try {
			
			if(productModel.getMinLimit() != null && (amount < productModel.getMinLimit())) {
	 
				throw new FrameworkCheckedException("The amount provided is less than the minimum transaction limit.");
				
			} else if(productModel.getMaxLimit() != null && (amount > productModel.getMaxLimit())) {
				
				throw new FrameworkCheckedException("The amount provided is greater than the maximum transaction limit.");
			}
			
		} catch (Exception e) {
			isValidAmount = Boolean.FALSE;
			request.setAttribute("errors", e.getMessage());
		}
		
		return isValidAmount;
	}
	*/
	/**
	 * @param productId
	 * @return
	 */
	public ProductModel getProductModel(Long productId) {
	
		return allPayWebResponseDataPopulator.getProductModel(productId);	
	}
	
	/**
	 * 		DI/IoC Setter Injections
	 * */
	
	public void setCommissionRateDAO(CommissionRateDAO commissionRateDAO) {
		this.commissionRateDAO = commissionRateDAO;
	}

	public void setCommonCommandManager(CommonCommandManager commonCommandManager) {
		this.commonCommandManager = commonCommandManager;
	}
	
	public void setAllPayWebResponseDataPopulator(AllPayWebResponseDataPopulator allPayWebResponseDataPopulator) {
		this.allPayWebResponseDataPopulator = allPayWebResponseDataPopulator;
	}
	
	public void setCommissionTransactionDAO(CommissionTransactionDAO commissionTransactionDAO) {
		this.commissionTransactionDAO = commissionTransactionDAO;
	}
}
