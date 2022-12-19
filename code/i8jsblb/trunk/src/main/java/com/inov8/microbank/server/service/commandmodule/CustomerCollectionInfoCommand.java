package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.inov8.microbank.common.util.XMLConstants.*;

public class CustomerCollectionInfoCommand extends BaseCommand {

	protected final Log logger = LogFactory.getLog(CustomerCollectionInfoCommand.class);

	private ProductModel productModel;
    private AppUserModel appUserModel;
	private DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd/MM/yyyy");
	private I8SBSwitchControllerResponseVO responseVO;

    protected String deviceTypeId;
	protected String productId;
	protected String customerMobileNo;
	protected String consumerNumber;

    CommissionAmountsHolder commissionAmountsHolder;

    @Override
	public void prepare(BaseWrapper baseWrapper) {
        appUserModel = ThreadLocalAppUser.getAppUserModel();

        deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
		productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
		customerMobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
		consumerNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CSCD);

		logger.info("[CustomerCollectionInfoCommand.prepare]" +
                "\nLogged In AppUserId: " + (appUserModel != null ? appUserModel.getAppUserId() : "null") +
                "\n deviceTypeId: " + deviceTypeId +
     			"\n Product ID:" + productId +
                "\n consumerNumber: " + consumerNumber +
     			"\n customerMobileNumber: " + customerMobileNo );
	}

	@Override
	public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
		validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
        validationErrors = ValidatorWrapper.doRequired(productId,validationErrors,"Product");
        validationErrors = ValidatorWrapper.doRequired(customerMobileNo, validationErrors, "Customer Mobile No");
        validationErrors = ValidatorWrapper.doRequired(consumerNumber, validationErrors, "Consumer Number");

		if(!validationErrors.hasValidationErrors()){
            validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
            validationErrors = ValidatorWrapper.doNumeric(productId,validationErrors,"Product");
			validationErrors = ValidatorWrapper.doNumeric(customerMobileNo,validationErrors,"Customer Mobile No");
            if(!productId.equals(ProductConstantsInterface.CUSTOMER_ET_COLLECTION.toString()))
                validationErrors = ValidatorWrapper.doNumeric(consumerNumber, validationErrors, "Consumer Code");
		}

		return validationErrors;
	}

	@Override
	public void execute() throws CommandException {

		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
        BaseWrapper baseWrapper = new BaseWrapperImpl();

        try {
            if(appUserModel == null || appUserModel.getCustomerId() != null){

                ValidationErrors validationErrors = getCommonCommandManager().checkActiveAppUser(appUserModel);

                if(!validationErrors.hasValidationErrors()) {

                    SmartMoneyAccountModel smartMoneyAccountModel = getSmartMoneyAccountModel(appUserModel.getCustomerId());

                    if(smartMoneyAccountModel.getSmartMoneyAccountId() != null) {
                        productModel = new ProductModel();
                        productModel.setProductId(Long.parseLong(productId));
                        baseWrapper.setBasePersistableModel(productModel);
                        baseWrapper = commonCommandManager.loadProduct(baseWrapper);
                        productModel = (ProductModel) baseWrapper.getBasePersistableModel();
                        if (productModel == null) {
                            throw new CommandException("Product not loaded", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                        }

                        //Please uncomment the following code in order to restrict customer daily challan payment
                        //Commented by Attique Butt
                        int count = this.getCommonCommandManager().getCustomerChallanCount(appUserModel.getMobileNo());
                        int config=Integer.parseInt(MessageUtil.getMessage("challan.collection.limit"));

                        if (config>0 && count >=config ){
                            throw new CommandException("Dear Customer, You have already processed your available quota of free challan payments for the day. Please try again tomorrow.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                        }
                        String productCode = productModel.getProductCode();
                        if(consumerNumber!=null && productId != null && productCode!=null)
                        {
                            logger.info("Validating Challan for Consumer No : " +consumerNumber+" product Code: "+productCode);
                            boolean flag =this.getCommonCommandManager().validateChallanParams(consumerNumber,productCode);
                            //Params are valid check for challan status
                            if (flag){
                                boolean billInQueue = this.getCommonCommandManager().getChallanStatus(consumerNumber,productCode);
                                if (billInQueue)
                                    throw new CommandException("Transaction is already Processing.",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);
                                //Check challan payment in TransactionDetailMaster and throws exception if exists
                               /* long paidCount = this.getCommonCommandManager().getPaidChallan(consumerNumber,productId);
                                // throws exception according to the count of paidCount, if paidCount >0
                                if(paidCount>0)
                                    this.getCommonCommandManager().throwsChallanException(paidCount);*/

                            }

                        }
                        checkCustomerCredentials(appUserModel);

                        workFlowWrapper.setProductModel(productModel);

                        CustomerModel customerModel = new CustomerModel();
                        customerModel.setCustomerId(appUserModel.getCustomerId());
                        baseWrapper = new BaseWrapperImpl();
                        baseWrapper.setBasePersistableModel(customerModel);

                        baseWrapper = this.getCommonCommandManager().loadCustomer(baseWrapper);

                        SegmentModel segmentModel = new SegmentModel();

                        if (baseWrapper.getBasePersistableModel() != null) {
                            customerModel = (CustomerModel) baseWrapper.getBasePersistableModel();
                            segmentModel.setSegmentId(customerModel.getSegmentId());

                            workFlowWrapper.setSegmentModel(segmentModel);
                            workFlowWrapper.setTaxRegimeModel((TaxRegimeModel) customerModel.getTaxRegimeIdTaxRegimeModel().clone());

                        } else {
                            throw new CommandException("Customer does not exist against mobile number " + customerMobileNo, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
                        }

                        //********************************************* Fetch Challan Details - Start
                        I8SBSwitchControllerRequestVO requestVO = ESBAdapter.prepareCollectionInquiryRequest(consumerNumber,productModel);
                        SwitchWrapper switchWrapper = new SwitchWrapperImpl();
                        switchWrapper.setI8SBSwitchControllerRequestVO(requestVO);

                        switchWrapper = commonCommandManager.makeI8SBCall(switchWrapper);

                        responseVO = switchWrapper.getI8SBSwitchControllerResponseVO();

                        ESBAdapter.processI8sbResponseCode(responseVO, true); //throws WorkFlowException incase of error response code

                        String billAmount = responseVO.getBillAmount() == null ? "0.0" : responseVO.getBillAmount();
                        //********************************************* Fetch Challan Details - End

                        //********************************************* Product Limit after bill inquiry- Start
                        DistributorModel distributorModel = new DistributorModel();
                        distributorModel.setDistributorId(DistributorConstants.DEFAULT_DISTRIBUTOR_ID);

                        commonCommandManager.checkProductLimit(segmentModel.getSegmentId(), productModel.getProductId(), "abc",
                                Long.valueOf(deviceTypeId), Double.parseDouble(billAmount), productModel, distributorModel, workFlowWrapper.getHandlerModel());
                        //********************************************* Product Limit after bill inquiry- End

                        TransactionModel transactionModel = new TransactionModel();
                        transactionModel.setTransactionAmount(Double.valueOf(billAmount));

                        workFlowWrapper.setProductModel(productModel);
                        workFlowWrapper.setAppUserModel(appUserModel);
                        workFlowWrapper.setTransactionAmount(Double.valueOf(billAmount));
                        workFlowWrapper.setDeviceTypeModel(new DeviceTypeModel(Long.parseLong(deviceTypeId)));

                        workFlowWrapper.setTransactionModel(transactionModel);

                        try {
                            workFlowWrapper.setIsCustomerInitiatedTransaction(true);
                            CommissionWrapper commissionWrapper = commonCommandManager.calculateCommission(workFlowWrapper);
                            commissionAmountsHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
//
//                            commonCommandManager.validateBalance(appUserModel, smartMoneyAccountModel, commissionAmountsHolder.getTotalAmount(), true);

                        } catch (FrameworkCheckedException e) {
                            logger.error("Exception while calculating commission...", e);
                            throw new CommandException(MessageUtil.getMessage("command.unexpectedError"), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
                        }



                    } else {
                        logger.error("[CustomerCollectionInfoCommand.execute] SmartMoney not loaded... Product ID: " +  productId + " - Logged In AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
                        throw new CommandException(MessageUtil.getMessage("checkAccountBalanceCommand.invalidAccountDetail"),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
                    }
                } else {
                    logger.error("[CustomerCollectionInfoCommand.execute] Throwing Exception in Product ID: " +  productId + " - Customer AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
                    throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
                }
            } else {
                logger.error("[CustomerCollectionInfoCommand.execute] Logged In AppUserType is not customer. AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
                throw new CommandException(MessageUtil.getMessage("getSupplierInfoCommand.invalidAppUserType"),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
            }

        } catch(CommandException ce) {
            throw ce;
        } catch (ClassCastException e) {
            logger.error("[CustomerCollectionInfoCommand.execute] Throwing Exception in Product ID: " +  productId + " - Customer AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
            throw new CommandException(MessageUtil.getMessage("command.unexpectedError"),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,e);
        } catch(Exception ex) {
            ex.printStackTrace();
            if(ex.getMessage() != null && !"".equals(ex.getMessage()) && ex.getMessage().indexOf("JTA") != -1) {
                logger.error("[CustomerCollectionInfoCommand.execute] Exception in Product ID: " +  productId + " - Customer AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
                throw new CommandException(MessageUtil.getMessage("command.unexpectedError"),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
            } else {
                logger.error("[CustomerCollectionInfoCommand.execute] Throwing Exception in Product ID: " +  productId + " - Customer AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
                throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
            }
        }

	}

	private SmartMoneyAccountModel getSmartMoneyAccountModel(Long customerId) throws FrameworkCheckedException {
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        SmartMoneyAccountModel customerSMAModel =  new SmartMoneyAccountModel();
        customerSMAModel.setCustomerId(appUserModel.getCustomerId());
        customerSMAModel.setActive(Boolean.TRUE);
        customerSMAModel.setDeleted(Boolean.FALSE);
        customerSMAModel.setDefAccount(Boolean.TRUE);
        searchBaseWrapper.setBasePersistableModel(customerSMAModel);

        searchBaseWrapper = getCommonCommandManager().loadSMAExactMatch(searchBaseWrapper);

        @SuppressWarnings("rawtypes")
        CustomList smaList = searchBaseWrapper.getCustomList();
        if(smaList != null && smaList.getResultsetList() != null && smaList.getResultsetList().size() > 0){
            customerSMAModel = (SmartMoneyAccountModel)smaList.getResultsetList().get(0);
        }

		return customerSMAModel;
	}

	private void checkCustomerCredentials(AppUserModel senderAppUserModel) throws FrameworkCheckedException {
		BaseWrapper baseWrapper = new BaseWrapperImpl();
	    baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.MOBILE);
	    baseWrapper.setBasePersistableModel(senderAppUserModel);

	    validationErrors = getCommonCommandManager().checkCustomerCredentials(baseWrapper);

	    if(validationErrors.hasValidationErrors()) {
	    	throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
	    }
	}

	@Override
	public String response() {
		return toXMLResponse();
	}

	private String toXMLResponse() {

        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMMMM yyyy hh:mm aaa");

        String dueDateStr = responseVO.getDueDate();
        String billStatusStr = responseVO.getBillStatus();
        String billAmountStr = responseVO.getBillAmount();
        String lateBillAmountStr = responseVO.getBillAmountAfterDueDate();

        Date dueDate = null;
        try {
            dueDate = PortalDateUtils.parseStringAsDate(dueDateStr, "yyyyMMdd");
        } catch (ParseException e) {
            logger.error("Unparseable date in response:" + dueDateStr);
        }

        double billAmount = StringUtil.isNullOrEmpty(billAmountStr) ? 0.0D : Double.parseDouble(billAmountStr);
        double lateBillAmount = StringUtil.isNullOrEmpty(lateBillAmountStr) ? 0.0D : Double.parseDouble(lateBillAmountStr);

        String billPaid = (billStatusStr.equalsIgnoreCase("U") ? "0" : "1");

		StringBuilder responseBuilder = new StringBuilder();

		responseBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);

		responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CUSTOMER_MOBILE, customerMobileNo));
		responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_PRODUCT_NAME, productModel.getName()));
		responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.CMD_AGNETMATE_CONSUMER_NUMBER, consumerNumber));

        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_BILL_AMOUNT, String.valueOf(replaceNullWithZero(billAmount))));
		responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_BILL_AMOUNT, Formatter.formatDoubleByPattern(billAmount, "#,###.00")));
		responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_LATE_BILL_AMT, String.valueOf(replaceNullWithZero(lateBillAmount))));
		responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_LATE_BILL_AMT, Formatter.formatDoubleByPattern(lateBillAmount, "#,###.00")));
		responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_BILL_PAID, billPaid));

		if(dueDate != null) {
			DateTime nowDate = new DateTime();
			DateTime _dueDate = new DateTime(dueDate).withTime(23, 59, 59, 0);
			responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.CMD_AGNETMATE_DUEDATER, dateTimeFormatter.print(_dueDate)));
			responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.CMD_AGNETMATE_DUEDATEF, sdf.format(_dueDate.toDate())));
			responseBuilder.append(MiniXMLUtil.createXMLParameterTag("ISOVERDUE", nowDate.isAfter(_dueDate) ? "1" : "0"));
		} else {
			responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.CMD_AGNETMATE_DUEDATER, "N/A"));
			responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.CMD_AGNETMATE_DUEDATEF, "N/A"));
			responseBuilder.append(MiniXMLUtil.createXMLParameterTag("ISOVERDUE", "1"));
		}

        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TOTAL_AMOUNT,replaceNullWithZero(commissionAmountsHolder.getTotalAmount())+""));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_TOTAL_AMOUNT, Formatter.formatNumbers(commissionAmountsHolder.getTotalAmount())));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_COMM_AMOUNT, replaceNullWithZero(commissionAmountsHolder.getTotalCommissionAmount())+""));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_COMM_AMOUNT, Formatter.formatNumbers(commissionAmountsHolder.getTotalCommissionAmount())));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TX_PROCESS_AMNT, replaceNullWithZero(commissionAmountsHolder.getTransactionProcessingAmount())+""));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_TX_PROCESS_AMNT, Formatter.formatNumbers(commissionAmountsHolder.getTransactionProcessingAmount())));

        responseBuilder.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);

		return responseBuilder.toString();
	}
}
