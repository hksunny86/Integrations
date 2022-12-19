package com.inov8.microbank.server.service.commandmodule;

import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAMS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_OPEN;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.DeviceTypeModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.RetailerModel;
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.TransactionTypeModel;
import com.inov8.microbank.common.util.CommandConstants;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.MiniXMLUtil;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;

/**
 * @author KashifBa
 */

public class TellerCashInInfoCommand extends BaseCommand {
	
	protected final Log logger = LogFactory.getLog(TellerCashInInfoCommand.class);

	public TellerCashInInfoCommand() {}

	private Double amount = null; 
	private String mobileNumber = null; 
	private String senderMobileNumber = null; 
	private CommissionWrapper commissionWrapper = null;
	private CommissionAmountsHolder commissionAmountsHolder = null;
	private CommonCommandManager commonCommandManager = null;
	private AppUserModel userAppUserModel = null;
	private ProductModel productModel = null;
	private Long productId = null;
	private Double commissionAmount = null;
	private String appUserType = null;
	private Long deviceTypeId = null;
	private Long segmentId = null;
	
	
	@Override
	public void prepare(BaseWrapper baseWrapper) {

		commonCommandManager = this.getCommonCommandManager();	
		
		try {

			senderMobileNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_SENDER_MOBILE);
			mobileNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MSISDN);
			amount = Double.valueOf(this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_AMOUNT));
			productId = Long.valueOf(this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PRODUCT_ID));
			deviceTypeId = Long.valueOf(this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID));
			
			Long[] appUserTypeIds = {UserTypeConstantsInterface.CUSTOMER, UserTypeConstantsInterface.RETAILER};
			userAppUserModel = commonCommandManager.loadAppUserByMobileAndType(mobileNumber, appUserTypeIds);
			
			productModel = new ProductModel(productId);
			
		} catch (FrameworkCheckedException e) {
			e.printStackTrace();
		}		
	}


	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
		
		return validationErrors;
	}


	@Override
	public void execute() throws CommandException {

		segmentId = CommissionConstantsInterface.DEFAULT_SEGMENT_ID;
		
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();

		BaseWrapper baseWrapper = new BaseWrapperImpl();
		
		try {
					
		if(userAppUserModel != null) {
				
			if(userAppUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()) {				
					
				baseWrapper.setBasePersistableModel(new CustomerModel(userAppUserModel.getCustomerId()));
					
				this.commonCommandManager.loadCustomer(baseWrapper);

				CustomerModel customerModel = (CustomerModel) baseWrapper.getBasePersistableModel();
					
				if(customerModel != null) {
					
					if(!customerModel.getRegister()) {
						throw new CommandException(mobileNumber + " " +this.getMessageSource().getMessage("LoginCommand.customerInactive", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());				
					}
					
					segmentId = customerModel.getSegmentId();	
					workFlowWrapper.setCustomerModel(customerModel);
					appUserType = "Customer";
				}
					
			} else if(userAppUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER.longValue()) {
					
				baseWrapper.setBasePersistableModel(new RetailerContactModel(userAppUserModel.getRetailerContactId()));
				this.commonCommandManager.loadRetailerContact(baseWrapper);
				RetailerContactModel retailerContactModel = (RetailerContactModel) baseWrapper.getBasePersistableModel();	
					
				if(retailerContactModel != null) {
					
					if(!retailerContactModel.getActive()) {
						throw new CommandException(mobileNumber + " " +this.getMessageSource().getMessage("LoginCommand.retailerInactive", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());				
					}
					
					workFlowWrapper.setRetailerContactModel(retailerContactModel);
					appUserType = "Agent";
				}

//				DistributorModel distributorModel = getCommonCommandManager().loadDistributor(userAppUserModel);
//				checkVelocityCondition(distributorModel, retailerContactModel, null);						
			}	
				
		} else {

			throw new CommandException(mobileNumber + " " +this.getMessageSource().getMessage("invalid.mobile", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());				
		}
					
		TransactionModel transactionModel = new TransactionModel();
		transactionModel.setTransactionAmount(amount);

		workFlowWrapper.setCustomerAppUserModel(userAppUserModel);
		workFlowWrapper.setSegmentModel(new SegmentModel(segmentId));
		workFlowWrapper.setTransactionModel(transactionModel);
		workFlowWrapper.setTransactionTypeModel(new TransactionTypeModel(TransactionTypeConstantsInterface.Teller_CASH_IN_TX));
		workFlowWrapper.setProductModel(productModel);
		workFlowWrapper.setDeviceTypeModel(new DeviceTypeModel(deviceTypeId));
		
		if(ProductConstantsInterface.TELLER_WALK_IN_CASH_IN.longValue() == productId.longValue() || 
				userAppUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()) {
			
			RetailerContactModel retailerContactModel = new RetailerContactModel();
			retailerContactModel.setRetailerIdRetailerModel(new RetailerModel());		
			workFlowWrapper.setRetailerContactModel(retailerContactModel);
		}
			
			RetailerContactModel retailerContactmodel = ThreadLocalAppUser.getAppUserModel().getRetailerContactIdRetailerContactModel();
			workFlowWrapper.setTaxRegimeModel(retailerContactmodel.getTaxRegimeIdTaxRegimeModel());
		
			this.commissionWrapper = commonCommandManager.calculateCommission(workFlowWrapper);
			this.commissionAmountsHolder = (CommissionAmountsHolder)commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
			commissionAmount = commissionAmountsHolder.getTotalCommissionAmount();
			
			workFlowWrapper.setCommissionWrapper(commissionWrapper);
			workFlowWrapper.setCommissionAmountsHolder(commissionAmountsHolder);
			
			commonCommandManager.checkProductLimit(segmentId, workFlowWrapper.getProductModel().getProductId(), userAppUserModel.getMobileNo(), workFlowWrapper.getDeviceTypeModel().getDeviceTypeId(), amount, null, workFlowWrapper.getDistributorModel(), handlerModel);	

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			throw new CommandException(e.getLocalizedMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
		}		
	}

	
	void checkVelocityCondition(DistributorModel distributorModel, RetailerContactModel retailerContactModel, Long segmentId) throws FrameworkCheckedException {
		
	    BaseWrapper bWrapper = new BaseWrapperImpl();
	    bWrapper.putObject(CommandConstants.VELOCITY_PRODUCT_ID, productId);
	    bWrapper.putObject(CommandConstants.VELOCITY_DEVICE_TYPE_ID,deviceTypeId);
	    bWrapper.putObject(CommandConstants.VELOCITY_DISTRIBUTOR_ID, distributorModel.getDistributorId());
	    bWrapper.putObject(CommandConstants.VELOCITY_AGENT_TYPE, retailerContactModel.getDistributorLevelId());
	    bWrapper.putObject(CommandConstants.VELOCITY_TRANSACTION_AMOUNT, amount);
		bWrapper.putObject(CommandConstants.VELOCITY_ACCOUNT_TYPE, retailerContactModel.getOlaCustomerAccountTypeModel().getCustomerAccountTypeId());

	    if(segmentId != null && CommissionConstantsInterface.DEFAULT_SEGMENT_ID.longValue() != segmentId.longValue()) {
	    	bWrapper.putObject(CommandConstants.VELOCITY_SEGMENT_ID, segmentId);
	    }
	    
	    getCommonCommandManager().checkVelocityCondition(bWrapper);
	}	
	
	
	@Override
	public String response() {
				
		StringBuilder responseBuilder = new StringBuilder();
			
		responseBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);

		responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_NAME, this.userAppUserModel.getFirstName()+" "+this.userAppUserModel.getLastName()));
		responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CNIC, this.userAppUserModel.getNic()));
		responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_USER_TYPE, this.appUserType));
		responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TX_AMOUNT, this.amount.toString()));
		responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATTED_TX_AMOUNT, Formatter.formatDoubleByPattern(amount, "#,###.00")));
		responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CAMT, this.commissionAmount.toString()));
		responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CAMTF, Formatter.formatDoubleByPattern(commissionAmount, "#,###.00")));
		responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TAMT, String.valueOf(amount + commissionAmount)));
		responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TAMTF, Formatter.formatDoubleByPattern((amount + commissionAmount), "#,###.00")));
		
		responseBuilder.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);	
		
		logger.info(responseBuilder.toString());
		return responseBuilder.toString();
	}
}