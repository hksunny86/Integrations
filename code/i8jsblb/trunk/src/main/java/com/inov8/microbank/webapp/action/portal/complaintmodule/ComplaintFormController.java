package com.inov8.microbank.webapp.action.portal.complaintmodule;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.validator.GenericValidator;
import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.AddressModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.ComplaintCategoryModel;
import com.inov8.microbank.common.model.ComplaintModel;
import com.inov8.microbank.common.model.ComplaintParamValueModel;
import com.inov8.microbank.common.model.ComplaintReportModel;
import com.inov8.microbank.common.model.ComplaintSubcategoryModel;
import com.inov8.microbank.common.model.CustomerAddressesModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.HandlerModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.RetailerContactAddressesModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.portal.complaint.ComplaintModelVO;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.dao.complaintsmodule.ComplaintsModuleConstants;
import com.inov8.microbank.server.service.complaintmodule.ComplaintManager;
import com.inov8.microbank.server.service.handlermodule.HandlerManager;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;

public class ComplaintFormController extends AdvanceFormController{
    private static final Long SUPPLIER_BILL_PAYMENT_ID = 50063L;
    private final static Logger logger = Logger.getLogger(ComplaintDetailFormController.class);
    
	private ComplaintManager complaintManager;
	private ReferenceDataManager referenceDataManager;
	private MfsAccountManager mfsAccountManager;
	private RetailerContactManager retailerContactManager;
	private HandlerManager handlerManager;
	private AppUserManager appUserManager;
	
	public ComplaintFormController() {
		setCommandName("complaintModelVO");
		setCommandClass(ComplaintModelVO.class);
	}
	
	@Override
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
	{
		super.initBinder(request, binder);
		CommonUtils.bindCustomDateEditor(binder);
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest req) throws Exception {
		ComplaintModelVO complaintModelVO = new ComplaintModelVO();
//		ComplaintModel complaintModel = new ComplaintModel();
		String appUserId = ServletRequestUtils.getStringParameter(req, "appUserId");
		String usrType = ServletRequestUtils.getStringParameter(req, "usrType");
		String cnic = ServletRequestUtils.getStringParameter(req, "cnic");
		Long id = null;
	    if (null != appUserId && appUserId.trim().length() > 0){
	    	id = new Long(EncryptionUtil.decryptWithDES(appUserId));
			
	    	if(!StringUtil.isNullOrEmpty(usrType) && usrType.trim().equals(String.valueOf(UserTypeConstantsInterface.CUSTOMER))){
				this.populateCustomerDetails(complaintModelVO, id);
			}else if(!StringUtil.isNullOrEmpty(usrType) && usrType.trim().equals(String.valueOf(UserTypeConstantsInterface.RETAILER))){
				this.populateAgentDetails(complaintModelVO, id, null);
			}else if(!StringUtil.isNullOrEmpty(usrType) && usrType.trim().equals(String.valueOf(UserTypeConstantsInterface.HANDLER))){
				this.populateHandlerDetails(complaintModelVO, id, null);
			}
	    }else{
	    	if(null != cnic && cnic.length() == 13){
				AppUserModel appUserModel = appUserManager.loadAppUserByCNIC(cnic);
				if(appUserModel != null){
					if(appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()){
						this.populateCustomerDetails(complaintModelVO, appUserModel.getAppUserId());
					}else if(appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER.longValue()){
						this.populateAgentDetails(complaintModelVO, 0L, appUserModel);
					}else if(appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.HANDLER.longValue()){
						this.populateHandlerDetails(complaintModelVO, 0L, appUserModel);
					}else if(appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.WALKIN_CUSTOMER.longValue()){
				    	complaintModelVO.setIsWalkin(true);
						complaintModelVO.setInitiatorMobileNo(appUserModel.getMobileNo());
				    	complaintModelVO.setAppUserId(EncryptionUtil.encryptWithDES(appUserModel.getAppUserId().toString()));
				    	complaintModelVO.setInitiatorNIC(cnic);
						List<ComplaintReportModel> list = this.complaintManager.searchUserComplaintHistory(appUserModel.getAppUserId());
						complaintModelVO.setOldComplaints(list);
					}else{
				    	complaintModelVO.setInitiatorNIC(cnic);
				    	complaintModelVO.setIsWalkin(true);
					}
				}else{
			    	complaintModelVO.setInitiatorNIC(cnic);
			    	complaintModelVO.setIsWalkin(true);
				} 
	    	}
		}
		return complaintModelVO;
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest req) throws Exception {

		ComplaintCategoryModel complaintCategoryModel = new ComplaintCategoryModel();
	    complaintCategoryModel.setIsActive(true);
	    complaintCategoryModel.setIsAuto(false);
	    ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
	    		complaintCategoryModel, "name", SortingOrder.ASC);
	    referenceDataManager.getReferenceData(referenceDataWrapper);
	    List<ComplaintCategoryModel> complaintCategoryModelList = null;
	    if (referenceDataWrapper.getReferenceDataList() != null){
	    	complaintCategoryModelList = referenceDataWrapper.getReferenceDataList();
	    } 
	    Map referenceDataMap = new HashMap();
	    referenceDataMap.put("complaintCategoryModelList", complaintCategoryModelList);

	    
		Long complaintCatId = ServletRequestUtils.getLongParameter(req, "complaintCategoryId");
		if (complaintCatId != null){
			ComplaintSubcategoryModel complaintSubcategoryModel = new ComplaintSubcategoryModel();
			complaintSubcategoryModel.setComplaintCategoryId(complaintCatId);
		    complaintSubcategoryModel.setIsActive(true);
		    referenceDataWrapper = new ReferenceDataWrapperImpl(complaintSubcategoryModel, "name", SortingOrder.ASC);
		    referenceDataManager.getReferenceData(referenceDataWrapper);
		    List<ComplaintSubcategoryModel> complaintSubcategoryModelList = null;
		    if (referenceDataWrapper.getReferenceDataList() != null){
		    	complaintSubcategoryModelList = referenceDataWrapper.getReferenceDataList();
			    referenceDataMap.put("complaintSubcategoryList", complaintSubcategoryModelList);
		    } 
		}
		
// loading bill payment reference data
		ProductModel productModel = null;
		List<ProductModel> productModelList = null;

        productModel = new ProductModel();
        productModel.setSupplierId( SUPPLIER_BILL_PAYMENT_ID );
        productModel.setActive( true );
        referenceDataWrapper = new ReferenceDataWrapperImpl( productModel, "name", SortingOrder.ASC );
        referenceDataManager.getReferenceData( referenceDataWrapper );
        if( referenceDataWrapper.getReferenceDataList() != null )
        {
            productModelList = referenceDataWrapper.getReferenceDataList();

            SortedSet<String> billTypeSet = new TreeSet<String>();
            SortedSet<String> billCompanySet = new TreeSet<String>();
            SortedMap<String, SortedSet<String>> billTypeAndCompaniesMap = new TreeMap<String, SortedSet<String>>();

            for( ProductModel product: productModelList )
            {
                String billType = product.getBillType();
                if( !GenericValidator.isBlankOrNull( billType ) )
                {
                    billTypeSet.add( billType );

                    if( !billTypeAndCompaniesMap.containsKey( billType ) )
                    {
                        billTypeAndCompaniesMap.put( billType, new TreeSet<String>() );
                    }

                    String billCompany = product.getProductCode();
                    if( !GenericValidator.isBlankOrNull( billCompany ) )
                    {
                        billCompany = billCompany.toUpperCase();
                        billCompanySet.add( billCompany );

                        SortedSet<String> billCompanies = billTypeAndCompaniesMap.get( billType );
                        if( null != billCompanies )
                        {
                            billCompanies.add( billCompany );
                        }
                    }
                }
            }

            StringBuilder billTypeAndCompanyCsvBuilder = new StringBuilder(); //just like query string seperated by comma(,) instead of ampersand(&)  
            for( String billType : billTypeAndCompaniesMap.keySet() )
            {
                for( String company : billTypeAndCompaniesMap.get( billType ) )
                {
                    billTypeAndCompanyCsvBuilder.append( billType ).append( '=' ).append( company ).append( ',' );
                }
            }
            int lastCharIdx = billTypeAndCompanyCsvBuilder.length() -1;
            if(!StringUtil.isNullOrEmpty(billTypeAndCompanyCsvBuilder.toString()) && billTypeAndCompanyCsvBuilder.charAt( lastCharIdx ) == ',' )
            {
                billTypeAndCompanyCsvBuilder.deleteCharAt( lastCharIdx ); //delete trailing/extra comma
            }

            referenceDataMap.put( "billTypeAndCompanyCsv", billTypeAndCompanyCsvBuilder.toString() );
            referenceDataMap.put( "billCompanySet", billCompanySet );
            referenceDataMap.put( "billTypeSet", billTypeSet );
        }
		referenceDataMap.put("productModelList", productModelList);
		
	    return referenceDataMap;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest req, HttpServletResponse res, Object model, BindException errors) throws Exception {
		Date nowDate = new Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		ComplaintModelVO modelVO = (ComplaintModelVO) model;
		ComplaintModel complaintModel = new ComplaintModel();
		complaintModel.setComplaintCategoryId(modelVO.getComplaintCategoryId());
		complaintModel.setComplaintSubcategoryId(modelVO.getComplaintSubcategoryId());
		
		String appUserId = modelVO.getAppUserId();
		Long initAppUserId = null;
	    if (null != appUserId && appUserId.trim().length() > 0){
	    	initAppUserId = new Long(EncryptionUtil.decryptWithDES(appUserId));
			complaintModel.setInitAppUserId(initAppUserId);
	    }
	    if (modelVO.getInitiatorId() != null && modelVO.getInitiatorId().trim().length() > 0){
	    	complaintModel.setInitiatorId(modelVO.getInitiatorId());
	    }

		if(modelVO.getIsCustomer() != null && modelVO.getIsCustomer()){
			complaintModel.setInitiatorType("Customer");
		}else if(modelVO.getIsAgent() != null && modelVO.getIsAgent()){
			complaintModel.setInitiatorType("Agent");
		}else if(modelVO.getIsHandler() != null && modelVO.getIsHandler()){
			complaintModel.setInitiatorType("Handler");
		}else if(modelVO.getIsWalkin() != null && modelVO.getIsWalkin()){
			complaintModel.setInitiatorType("Walk-in Customer");
		}
		complaintModel.setInitiatorCity(modelVO.getInitiatorCity());
		complaintModel.setInitiatorName(modelVO.getInitiatorFirstName());
		complaintModel.setInitiatorCNIC(modelVO.getInitiatorNIC());
		complaintModel.setOtherContactNo(modelVO.getOtherContactNo());
		complaintModel.setMobileNo(modelVO.getInitiatorMobileNo());
		complaintModel.setComplaintDescription(modelVO.getComplaintDescription());
		complaintModel.setStatus(ComplaintsModuleConstants.STATUS_ASSIGNED);
		complaintModel.setEscalationStatus(ComplaintsModuleConstants.ESC_STATUS_DEFAULT);
		complaintModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		complaintModel.setCreatedOn(nowDate);
		
		List<ComplaintParamValueModel> complaintsParamValues = new ArrayList<ComplaintParamValueModel>();
		ComplaintParamValueModel paramValueModel = new ComplaintParamValueModel();
		
		if(modelVO.getComplaintCategoryId().longValue() == ComplaintsModuleConstants.CATEGORY_UTILITY.longValue()){
			if(!StringUtil.isNullOrEmpty(modelVO.getServiceProviderType())){
				paramValueModel.setRelationComplaintIdComplaintModel(complaintModel);
				paramValueModel.setComplaintParameterId(ComplaintsModuleConstants.UTILITY_SP_TYPE);
				paramValueModel.setValue(modelVO.getServiceProviderType());
				complaintsParamValues.add(paramValueModel);
			}
			if(!StringUtil.isNullOrEmpty(modelVO.getServiceProviderName())){
				paramValueModel = new ComplaintParamValueModel();
				paramValueModel.setRelationComplaintIdComplaintModel(complaintModel);
				paramValueModel.setComplaintParameterId(ComplaintsModuleConstants.UTILITY_SP_NAME);
				paramValueModel.setValue(modelVO.getServiceProviderName());
				complaintsParamValues.add(paramValueModel);
			}
			if(!StringUtil.isNullOrEmpty(modelVO.getUtilityTrxId())){
				paramValueModel = new ComplaintParamValueModel();
				paramValueModel.setRelationComplaintIdComplaintModel(complaintModel);
				paramValueModel.setComplaintParameterId(ComplaintsModuleConstants.UTILITY_TRX_ID);
				paramValueModel.setValue(modelVO.getUtilityTrxId());
				complaintsParamValues.add(paramValueModel);
				complaintModel.setTransactionId(paramValueModel.getValue());
			}
			if(modelVO.getPaymentDate() != null){
				paramValueModel = new ComplaintParamValueModel();
				paramValueModel.setRelationComplaintIdComplaintModel(complaintModel);
				paramValueModel.setComplaintParameterId(ComplaintsModuleConstants.UTILITY_PAYMENT_DATE);
			    paramValueModel.setValue(df.format(modelVO.getPaymentDate()));
				complaintsParamValues.add(paramValueModel);
			}
			if(modelVO.getPaymentAmount() != null && modelVO.getPaymentAmount() > 0){
				paramValueModel = new ComplaintParamValueModel();
				paramValueModel.setRelationComplaintIdComplaintModel(complaintModel);
				paramValueModel.setComplaintParameterId(ComplaintsModuleConstants.UTILITY_AMOUNT);
				paramValueModel.setValue( String.valueOf(modelVO.getPaymentAmount()));
				complaintsParamValues.add(paramValueModel);
			}
			if(!StringUtil.isNullOrEmpty(modelVO.getConsumerNumber())){
				paramValueModel = new ComplaintParamValueModel();
				paramValueModel.setRelationComplaintIdComplaintModel(complaintModel);
				paramValueModel.setComplaintParameterId(ComplaintsModuleConstants.UTILITY_CONSUMER_NO);
				paramValueModel.setValue(modelVO.getConsumerNumber());
				complaintsParamValues.add(paramValueModel);
			}
		}else if(modelVO.getComplaintCategoryId().longValue() == ComplaintsModuleConstants.CATEGORY_FUND_TRANSFER.longValue()){
			if(!StringUtil.isNullOrEmpty(modelVO.getTransactionId())){
				paramValueModel = new ComplaintParamValueModel();
				paramValueModel.setRelationComplaintIdComplaintModel(complaintModel);
				paramValueModel.setComplaintParameterId(ComplaintsModuleConstants.FT_TRANSACTION_ID);
				paramValueModel.setValue(modelVO.getTransactionId());
				complaintsParamValues.add(paramValueModel);
				complaintModel.setTransactionId(paramValueModel.getValue());
			}
			if(modelVO.getTransactionDate() != null){
				paramValueModel = new ComplaintParamValueModel();
				paramValueModel.setRelationComplaintIdComplaintModel(complaintModel);
				paramValueModel.setComplaintParameterId(ComplaintsModuleConstants.FT_TRX_DATE);
			    paramValueModel.setValue(df.format(modelVO.getTransactionDate()));
				complaintsParamValues.add(paramValueModel);
			}
			if(modelVO.getAmountTransferred() != null && modelVO.getAmountTransferred() > 0){
				paramValueModel = new ComplaintParamValueModel();
				paramValueModel.setRelationComplaintIdComplaintModel(complaintModel);
				paramValueModel.setComplaintParameterId(ComplaintsModuleConstants.FT_AMOUNT);
				paramValueModel.setValue( String.valueOf(modelVO.getAmountTransferred()));
				complaintsParamValues.add(paramValueModel);
			}
			if(!StringUtil.isNullOrEmpty(modelVO.getSenderMSISDN())){
				paramValueModel = new ComplaintParamValueModel();
				paramValueModel.setRelationComplaintIdComplaintModel(complaintModel);
				paramValueModel.setComplaintParameterId(ComplaintsModuleConstants.FT_SENDER_MSISDN);
				paramValueModel.setValue(modelVO.getSenderMSISDN());
				complaintsParamValues.add(paramValueModel);
			}
			if(!StringUtil.isNullOrEmpty(modelVO.getSenderCNIC())){
				paramValueModel = new ComplaintParamValueModel();
				paramValueModel.setRelationComplaintIdComplaintModel(complaintModel);
				paramValueModel.setComplaintParameterId(ComplaintsModuleConstants.FT_SENDER_CNIC);
				paramValueModel.setValue(modelVO.getSenderCNIC());
				complaintsParamValues.add(paramValueModel);
			}
			if(!StringUtil.isNullOrEmpty(modelVO.getSenderAccountNo())){
				paramValueModel = new ComplaintParamValueModel();
				paramValueModel.setRelationComplaintIdComplaintModel(complaintModel);
				paramValueModel.setComplaintParameterId(ComplaintsModuleConstants.FT_SENDER_ACC_NO);
				paramValueModel.setValue(modelVO.getSenderAccountNo());
				complaintsParamValues.add(paramValueModel);
			}
			if(!StringUtil.isNullOrEmpty(modelVO.getRecipientMSISDN())){
				paramValueModel = new ComplaintParamValueModel();
				paramValueModel.setRelationComplaintIdComplaintModel(complaintModel);
				paramValueModel.setComplaintParameterId(ComplaintsModuleConstants.FT_RECIPIENT_MSISDN);
				paramValueModel.setValue(modelVO.getRecipientMSISDN());
				complaintsParamValues.add(paramValueModel);
			}
			if(!StringUtil.isNullOrEmpty(modelVO.getRecipientCNIC())){
				paramValueModel = new ComplaintParamValueModel();
				paramValueModel.setRelationComplaintIdComplaintModel(complaintModel);
				paramValueModel.setComplaintParameterId(ComplaintsModuleConstants.FT_RECIPIENT_CNIC);
				paramValueModel.setValue(modelVO.getRecipientCNIC());
				complaintsParamValues.add(paramValueModel);
			}
			if(!StringUtil.isNullOrEmpty(modelVO.getRecipientAccountNo())){
				paramValueModel = new ComplaintParamValueModel();
				paramValueModel.setRelationComplaintIdComplaintModel(complaintModel);
				paramValueModel.setComplaintParameterId(ComplaintsModuleConstants.FT_RECIPIENT_ACC_NO);
				paramValueModel.setValue(modelVO.getRecipientAccountNo());
				complaintsParamValues.add(paramValueModel);
			}
		}else if(modelVO.getComplaintCategoryId().longValue() == ComplaintsModuleConstants.CATEGORY_AGENT_COMPLAINT.longValue()){
			if(!StringUtil.isNullOrEmpty(modelVO.getAgentLocation())){
				paramValueModel = new ComplaintParamValueModel();
				paramValueModel.setRelationComplaintIdComplaintModel(complaintModel);
				paramValueModel.setComplaintParameterId(ComplaintsModuleConstants.AGENT_LOCATION);
				paramValueModel.setValue(modelVO.getAgentLocation());
				complaintsParamValues.add(paramValueModel);
			}
			if(!StringUtil.isNullOrEmpty(modelVO.getAgentId())){
				paramValueModel = new ComplaintParamValueModel();
				paramValueModel.setRelationComplaintIdComplaintModel(complaintModel);
				paramValueModel.setComplaintParameterId(ComplaintsModuleConstants.AGENT_ID);
				paramValueModel.setValue( modelVO.getAgentId());
				complaintsParamValues.add(paramValueModel);
			}
			if(!StringUtil.isNullOrEmpty(modelVO.getShopName())){
				paramValueModel = new ComplaintParamValueModel();
				paramValueModel.setRelationComplaintIdComplaintModel(complaintModel);
				paramValueModel.setComplaintParameterId(ComplaintsModuleConstants.AGENT_SHOP_NAME);
				paramValueModel.setValue( modelVO.getShopName());
				complaintsParamValues.add(paramValueModel);
			}
			if(!StringUtil.isNullOrEmpty(modelVO.getShopAddress())){
				paramValueModel = new ComplaintParamValueModel();
				paramValueModel.setRelationComplaintIdComplaintModel(complaintModel);
				paramValueModel.setComplaintParameterId(ComplaintsModuleConstants.AGENT_SHOP_ADDRESS);
				paramValueModel.setValue( modelVO.getShopAddress());
				complaintsParamValues.add(paramValueModel);
			}
			if(!StringUtil.isNullOrEmpty(modelVO.getAgentTransactionId())){
				paramValueModel = new ComplaintParamValueModel();
				paramValueModel.setRelationComplaintIdComplaintModel(complaintModel);
				paramValueModel.setComplaintParameterId(ComplaintsModuleConstants.AGENT_TRANSACTION_ID);
				paramValueModel.setValue( modelVO.getAgentTransactionId());
				complaintsParamValues.add(paramValueModel);
				complaintModel.setTransactionId(paramValueModel.getValue());
			}
			if(modelVO.getAgentPaymentAmount() != null && modelVO.getAgentPaymentAmount() > 0){
				paramValueModel = new ComplaintParamValueModel();
				paramValueModel.setRelationComplaintIdComplaintModel(complaintModel);
				paramValueModel.setComplaintParameterId(ComplaintsModuleConstants.AGENT_AMOUNT);
				paramValueModel.setValue( String.valueOf(modelVO.getAgentPaymentAmount()));
				complaintsParamValues.add(paramValueModel);
			}
			if(!StringUtil.isNullOrEmpty(modelVO.getSenderAgentMSISDN())){
				paramValueModel = new ComplaintParamValueModel();
				paramValueModel.setRelationComplaintIdComplaintModel(complaintModel);
				paramValueModel.setComplaintParameterId(ComplaintsModuleConstants.SENDER_AGENT_MSISDN);
				paramValueModel.setValue( modelVO.getSenderAgentMSISDN());
				complaintsParamValues.add(paramValueModel);
			}
			if(!StringUtil.isNullOrEmpty(modelVO.getReceiverAgentMSISDN())){
				paramValueModel = new ComplaintParamValueModel();
				paramValueModel.setRelationComplaintIdComplaintModel(complaintModel);
				paramValueModel.setComplaintParameterId(ComplaintsModuleConstants.RECEIVER_AGENT_MSISDN);
				paramValueModel.setValue( modelVO.getReceiverAgentMSISDN());
				complaintsParamValues.add(paramValueModel);
			}
			if(modelVO.getAgentTransactionDate() != null){
				paramValueModel = new ComplaintParamValueModel();
				paramValueModel.setRelationComplaintIdComplaintModel(complaintModel);
				paramValueModel.setComplaintParameterId(ComplaintsModuleConstants.AGENT_TRANSACTION_DATE);
				paramValueModel.setValue(df.format(modelVO.getAgentTransactionDate()));
				complaintsParamValues.add(paramValueModel);
			}
		}else if(modelVO.getComplaintCategoryId().longValue() == ComplaintsModuleConstants.CATEGORY_TOP_UP.longValue()){
			if(!StringUtil.isNullOrEmpty(modelVO.getTopUpTrxId())){
				paramValueModel = new ComplaintParamValueModel();
				paramValueModel.setRelationComplaintIdComplaintModel(complaintModel);
				paramValueModel.setComplaintParameterId(ComplaintsModuleConstants.TOPUP_TRX_ID);
				paramValueModel.setValue(modelVO.getTopUpTrxId());
				complaintsParamValues.add(paramValueModel);
				complaintModel.setTransactionId(paramValueModel.getValue());
			}
			if(modelVO.getTopUpDate() != null){
				paramValueModel = new ComplaintParamValueModel();
				paramValueModel.setRelationComplaintIdComplaintModel(complaintModel);
				paramValueModel.setComplaintParameterId(ComplaintsModuleConstants.TOPUP_DATE);
			    paramValueModel.setValue(df.format(modelVO.getTopUpDate()));
				complaintsParamValues.add(paramValueModel);
			}
			if(!StringUtil.isNullOrEmpty(modelVO.getTopUpMobileNo())){
				paramValueModel = new ComplaintParamValueModel();
				paramValueModel.setRelationComplaintIdComplaintModel(complaintModel);
				paramValueModel.setComplaintParameterId(ComplaintsModuleConstants.TOPUP_MOBILE_NO);
				paramValueModel.setValue(modelVO.getTopUpMobileNo());
				complaintsParamValues.add(paramValueModel);
			}
			if(modelVO.getTopUpAmount() != null && modelVO.getTopUpAmount() > 0){
				paramValueModel = new ComplaintParamValueModel();
				paramValueModel.setRelationComplaintIdComplaintModel(complaintModel);
				paramValueModel.setComplaintParameterId(ComplaintsModuleConstants.TOPUP_AMOUNT);
				paramValueModel.setValue(String.valueOf(modelVO.getTopUpAmount()));
				complaintsParamValues.add(paramValueModel);
			}
		}else if(modelVO.getComplaintCategoryId().longValue() == ComplaintsModuleConstants.CATEGORY_CHARGEBACK.longValue()){
			if(!StringUtil.isNullOrEmpty(modelVO.getCbTrxId())){
				paramValueModel = new ComplaintParamValueModel();
				paramValueModel.setRelationComplaintIdComplaintModel(complaintModel);
				paramValueModel.setComplaintParameterId(ComplaintsModuleConstants.CB_TRANSACTION_ID);
				paramValueModel.setValue(modelVO.getCbTrxId());
				complaintsParamValues.add(paramValueModel);
				complaintModel.setTransactionId(paramValueModel.getValue());
			}
			if(modelVO.getCbTrxDate() != null){
				paramValueModel = new ComplaintParamValueModel();
				paramValueModel.setRelationComplaintIdComplaintModel(complaintModel);
				paramValueModel.setComplaintParameterId(ComplaintsModuleConstants.CB_TRX_DATE);
			    paramValueModel.setValue(df.format(modelVO.getCbTrxDate()));
				complaintsParamValues.add(paramValueModel);
			}
			if(modelVO.getCbAmount() != null && modelVO.getCbAmount() > 0){
				paramValueModel = new ComplaintParamValueModel();
				paramValueModel.setRelationComplaintIdComplaintModel(complaintModel);
				paramValueModel.setComplaintParameterId(ComplaintsModuleConstants.CB_AMOUNT);
				paramValueModel.setValue( String.valueOf(modelVO.getCbAmount()));
				complaintsParamValues.add(paramValueModel);
			}
			if(!StringUtil.isNullOrEmpty(modelVO.getCbSenderMSISDN())){
				paramValueModel = new ComplaintParamValueModel();
				paramValueModel.setRelationComplaintIdComplaintModel(complaintModel);
				paramValueModel.setComplaintParameterId(ComplaintsModuleConstants.CB_SENDER_MSISDN);
				paramValueModel.setValue(modelVO.getCbSenderMSISDN());
				complaintsParamValues.add(paramValueModel);
			}
			if(!StringUtil.isNullOrEmpty(modelVO.getCbSenderCNIC())){
				paramValueModel = new ComplaintParamValueModel();
				paramValueModel.setRelationComplaintIdComplaintModel(complaintModel);
				paramValueModel.setComplaintParameterId(ComplaintsModuleConstants.CB_SENDER_CNIC);
				paramValueModel.setValue(modelVO.getCbSenderCNIC());
				complaintsParamValues.add(paramValueModel);
			}
			if(!StringUtil.isNullOrEmpty(modelVO.getCbRecipientMSISDN())){
				paramValueModel = new ComplaintParamValueModel();
				paramValueModel.setRelationComplaintIdComplaintModel(complaintModel);
				paramValueModel.setComplaintParameterId(ComplaintsModuleConstants.CB_RECIPIENT_MSISDN);
				paramValueModel.setValue(modelVO.getCbRecipientMSISDN());
				complaintsParamValues.add(paramValueModel);
			}
			if(!StringUtil.isNullOrEmpty(modelVO.getCbRecipientCNIC())){
				paramValueModel = new ComplaintParamValueModel();
				paramValueModel.setRelationComplaintIdComplaintModel(complaintModel);
				paramValueModel.setComplaintParameterId(ComplaintsModuleConstants.CB_RECIPIENT_CNIC);
				paramValueModel.setValue(modelVO.getCbRecipientCNIC());
				complaintsParamValues.add(paramValueModel);
			}
		}else if(modelVO.getComplaintCategoryId().longValue() == ComplaintsModuleConstants.CATEGORY_BB_ACCOUNT.longValue()){
			if(!StringUtil.isNullOrEmpty(modelVO.getBbCustomerMSISDN())){
				paramValueModel = new ComplaintParamValueModel();
				paramValueModel.setRelationComplaintIdComplaintModel(complaintModel);
				paramValueModel.setComplaintParameterId(ComplaintsModuleConstants.BB_CUSTOMER_MSISDN);
			    paramValueModel.setValue(modelVO.getBbCustomerMSISDN());
				complaintsParamValues.add(paramValueModel);
			}
			if(modelVO.getBbAccountDate() != null){
				paramValueModel = new ComplaintParamValueModel();
				paramValueModel.setRelationComplaintIdComplaintModel(complaintModel);
				paramValueModel.setComplaintParameterId(ComplaintsModuleConstants.BB_ACCOUNT_DATE);
			    paramValueModel.setValue(df.format(modelVO.getBbAccountDate()));
				complaintsParamValues.add(paramValueModel);
			}
			if(!StringUtil.isNullOrEmpty(modelVO.getBbTransactionId())){
				paramValueModel = new ComplaintParamValueModel();
				paramValueModel.setRelationComplaintIdComplaintModel(complaintModel);
				paramValueModel.setComplaintParameterId(ComplaintsModuleConstants.BB_TRANSACTION_ID);
			    paramValueModel.setValue(modelVO.getBbTransactionId());
				complaintsParamValues.add(paramValueModel);
			}
			if(modelVO.getBbAmountTransferred() != null && modelVO.getBbAmountTransferred() > 0){
				paramValueModel = new ComplaintParamValueModel();
				paramValueModel.setRelationComplaintIdComplaintModel(complaintModel);
				paramValueModel.setComplaintParameterId(ComplaintsModuleConstants.BB_AMOUNT_TRANSFERRED);
			    paramValueModel.setValue(String.valueOf(modelVO.getBbAmountTransferred()));
				complaintsParamValues.add(paramValueModel);
			}
		}
		
		complaintModel.setComplaintParamValues(complaintsParamValues);
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(complaintModel);
		try{
			baseWrapper = this.complaintManager.createComplaint(baseWrapper);
			complaintModel = (ComplaintModel)baseWrapper.getBasePersistableModel();
			
			SimpleDateFormat dateFormat =  new SimpleDateFormat("dd/MM/yyyy hh:mm aaa");
			String expectedTat = dateFormat.format(complaintModel.getExpectedTat());
			String msg = super.getText("complaint.add.success", new Object[]{complaintModel.getComplaintCode(), expectedTat}, req.getLocale());
			this.saveMessage(req, msg);
			
		}catch(FrameworkCheckedException fce){
			fce.printStackTrace();
			logger.error(fce.getMessage());
			super.saveMessage(req, super.getText("complaint.add.failure", req.getLocale()));
			return super.showForm(req, res, errors);
		}catch(Exception fce){
			fce.printStackTrace();
			logger.error(fce.getMessage());
			super.saveMessage(req, MessageUtil.getMessage("6075"));
			return super.showForm(req, res, errors);
		}
		
		ModelAndView modelAndView = new ModelAndView(new RedirectView("p_listcomplaint.html?actionId=2"));
		return modelAndView;
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest req, HttpServletResponse res, Object model, BindException errors) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void setComplaintManager(ComplaintManager complaintManager) {
		this.complaintManager = complaintManager;
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public void setMfsAccountManager(MfsAccountManager mfsAccountManager) {
		this.mfsAccountManager = mfsAccountManager;
	}

	public void setRetailerContactManager(RetailerContactManager retailerContactManager) {
		this.retailerContactManager = retailerContactManager;
	}
	
	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}
	
	private void populateCustomerDetails(ComplaintModelVO complaintModelVO, Long id) throws Exception{
		AppUserModel appUserModel = new AppUserModel();

		complaintModelVO.setIsCustomer(true);

	    BaseWrapper baseWrapper = new BaseWrapperImpl();
		appUserModel.setAppUserId(id);
		baseWrapper.setBasePersistableModel(appUserModel);
		baseWrapper = this.mfsAccountManager.searchAppUserByPrimaryKey(baseWrapper);
		appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();
		
		complaintModelVO.setAppUserId(EncryptionUtil.encryptWithDES(id.toString()));
//		complaintModelVO.setInitiatorFirstName(appUserModel.getFirstName() + ((appUserModel.getLastName() != null)?" "+appUserModel.getLastName():"") );
		complaintModelVO.setInitiatorMobileNo(appUserModel.getMobileNo());
		complaintModelVO.setInitiatorNIC(appUserModel.getNic());

		UserDeviceAccountsModel deviceAccountModel = this.mfsAccountManager.getDeviceAccountByAppUserId(id,DeviceTypeConstantsInterface.MOBILE);
		complaintModelVO.setInitiatorId(deviceAccountModel.getUserId());
		
		CustomerModel customerModel = appUserModel.getCustomerIdCustomerModel();
		complaintModelVO.setInitiatorFirstName(customerModel.getName());
		
    	  // Populating City
    	  Collection<CustomerAddressesModel> customerAddresses = customerModel.getCustomerIdCustomerAddressesModelList();
    	  if(customerAddresses != null && customerAddresses.size() > 0){
    		  for(CustomerAddressesModel custAdd : customerAddresses){
    			  AddressModel addressModel = custAdd.getAddressIdAddressModel();
    			  if(custAdd.getAddressTypeId() == 1){
    				  if(addressModel.getCityId() != null){
    					  complaintModelVO.setInitiatorCity(addressModel.getCityIdCityModel().getName());
    				  }else if(addressModel.getOtherCity() != null && !addressModel.getOtherCity().isEmpty()){
    					  complaintModelVO.setInitiatorCity(addressModel.getOtherCity());  
    				  }
    			  }
    		  }
    	  }
    	  
		List<ComplaintReportModel> list = this.complaintManager.searchUserComplaintHistory(id);
		complaintModelVO.setOldComplaints(list);
		
	}
	
	private void populateAgentDetails(ComplaintModelVO complaintModelVO, Long id,  AppUserModel appUserModel) throws Exception{
		
		complaintModelVO.setIsAgent(true);

		if(appUserModel==null){
	    	appUserModel = appUserManager.getUser(String.valueOf(id));
		}

		complaintModelVO.setAppUserId(EncryptionUtil.encryptWithDES(appUserModel.getAppUserId().toString()));
		complaintModelVO.setInitiatorFirstName(appUserModel.getFirstName() + ((appUserModel.getLastName() != null)?" "+appUserModel.getLastName():"") );
		complaintModelVO.setInitiatorMobileNo(appUserModel.getMobileNo());
		complaintModelVO.setInitiatorNIC(appUserModel.getNic());

		UserDeviceAccountsModel deviceAccountModel = this.mfsAccountManager.getDeviceAccountByAppUserId(appUserModel.getAppUserId(),DeviceTypeConstantsInterface.ALL_PAY);
		complaintModelVO.setInitiatorId(deviceAccountModel.getUserId());

		RetailerContactModel retailerContactModel = new RetailerContactModel();
		retailerContactModel.setRetailerContactId(appUserModel.getRetailerContactId());
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel(retailerContactModel);
		searchBaseWrapper = this.retailerContactManager.loadRetailerContact(searchBaseWrapper);
		retailerContactModel = (RetailerContactModel) searchBaseWrapper.getBasePersistableModel();
		
		Collection<RetailerContactAddressesModel> retailerContactAddresses = retailerContactModel.getRetailerContactIdRetailerContactAddressesModelList();
		if(retailerContactAddresses != null && retailerContactAddresses.size() > 0){
			for(RetailerContactAddressesModel retAdd : retailerContactAddresses){
				AddressModel addressModel = retAdd.getAddressIdAddressModel();
				if(retAdd.getAddressTypeId() == 3){
					if(addressModel.getCityId() != null){
						complaintModelVO.setInitiatorCity(addressModel.getCityIdCityModel().getName());
					}
				}
			}
		}
		
		List<ComplaintReportModel> list = this.complaintManager.searchUserComplaintHistory(appUserModel.getAppUserId());
		complaintModelVO.setOldComplaints(list);
	}
	
	private void populateHandlerDetails(ComplaintModelVO complaintModelVO, Long id,  AppUserModel appUserModel) throws Exception{
		
		complaintModelVO.setIsHandler(true);

		if(appUserModel==null){
	    	appUserModel = appUserManager.getUser(String.valueOf(id));
		}

		complaintModelVO.setAppUserId(EncryptionUtil.encryptWithDES(appUserModel.getAppUserId().toString()));
		complaintModelVO.setInitiatorFirstName(appUserModel.getFirstName() + ((appUserModel.getLastName() != null)?" "+appUserModel.getLastName():"") );
		complaintModelVO.setInitiatorMobileNo(appUserModel.getMobileNo());
		complaintModelVO.setInitiatorNIC(appUserModel.getNic());

		UserDeviceAccountsModel deviceAccountModel = this.mfsAccountManager.getDeviceAccountByAppUserId(appUserModel.getAppUserId(),DeviceTypeConstantsInterface.ALL_PAY);
		complaintModelVO.setInitiatorId(deviceAccountModel.getUserId());

		HandlerModel handlerModel = new HandlerModel();
		handlerModel.setHandlerId(appUserModel.getHandlerId());
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel(handlerModel);
		searchBaseWrapper = this.handlerManager.loadHandler(searchBaseWrapper);
		handlerModel = (HandlerModel) searchBaseWrapper.getBasePersistableModel();
		
		List<ComplaintReportModel> list = this.complaintManager.searchUserComplaintHistory(appUserModel.getAppUserId());
		complaintModelVO.setOldComplaints(list);
	}

	public void setHandlerManager(HandlerManager handlerManager) {
		this.handlerManager = handlerManager;
	}
}
