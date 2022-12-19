package com.inov8.microbank.server.service.commandmodule;

import static com.inov8.microbank.common.util.XMLConstants.ATTR_DATE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_DATEF;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_PROD;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_RWCNIC;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TAMT;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TAMTF;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TIMEF;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TPAM;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TPAMF;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TRXID;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TXAM;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_TXAMF;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_EQUAL;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_OPEN;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_QUOTE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SLASH;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SPACE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_TRANS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_TRN;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_BALF;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_RCMOB;

import java.util.Date;
import java.util.List;

import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.fonepay.common.FonePayConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.commandmodule.minicommandmodule.MiniBaseCommand;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.verifly.common.model.AccountInfoModel;

public class CustomerInitiatedAccountToCashCommand extends MiniBaseCommand {

    private AppUserModel appUserModel;
    private String productId;
    private String txProcessingAmount;
    private String pin;
    private String deviceTypeId;
    private String commissionAmount;
    private String totalAmount;
    private String txAmount;
    //private String manualOTPin;

    protected double discountAmount = 0d;


    TransactionModel transactionModel;
    ProductModel productModel;
    String successMessage;
    BaseWrapper baseWrapper;
    SmartMoneyAccountModel smartMoneyAccountModel;
    //UserDeviceAccountsModel userDeviceAccountsModel;

    //private double customer1Balance;
    private double senderCustomerBalance = 0.0D;
    private String senderMobileNo;
    private long fromSegmentId;

    //	added by mudassir
    String recepientWalkinCNIC;
    String recepientWalkinMobile;
    private String transactionPurposeCode;
    private String terminalId;
    private String channelId;
    private String thirdPartyTransactionId;
    private String stan;

    protected final Log logger = LogFactory.getLog(CustomerInitiatedAccountToCashCommand.class);

    @Override
    public void execute() throws CommandException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CustomerInitiatedAccountToCashCommand.execute()");
        }

        SegmentModel segmentModel = new SegmentModel();
        segmentModel.setSegmentId(CommissionConstantsInterface.DEFAULT_SEGMENT_ID);

        CommonCommandManager commonCommandManager = this.getCommonCommandManager();
        WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();

        if (appUserModel.getCustomerId() != null) {
            try {
                CustomerModel customerModel = new CustomerModel();
                customerModel = commonCommandManager.getCustomerModelById(appUserModel.getCustomerId());
                ValidationErrors validationErrors = commonCommandManager.checkActiveAppUser(appUserModel);

                if (!validationErrors.hasValidationErrors()) {
                    AccountInfoModel accountInfoModel = new AccountInfoModel();
                    accountInfoModel.setOldPin(pin);

                    productModel = new ProductModel();
                    productModel.setProductId(Long.parseLong(productId));

                    //Work Flow to create walkin updated on 30/05/2017 By Atiq Butt
                    this.getCommonCommandManager().createOrUpdateWalkinCustomer(recepientWalkinCNIC, recepientWalkinMobile, null);
                    //List<AppUserModel> appUserModelList=new ArrayList<>();

                    ProductVO productVo = commonCommandManager.loadProductVO(baseWrapper);

                    if (productVo == null) {
                        throw new CommandException("CustomerInitiatedAccountToCashCommand.productVoLoadedError", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                    }

                    String userId = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId();
                    // Velocity validation - start
                    BaseWrapper bWrapper = new BaseWrapperImpl();
                    bWrapper.putObject(CommandConstants.VELOCITY_PRODUCT_ID, Long.parseLong(productId));
                    bWrapper.putObject(CommandConstants.VELOCITY_DEVICE_TYPE_ID, Long.parseLong(deviceTypeId));
                    bWrapper.putObject(CommandConstants.VELOCITY_DISTRIBUTOR_ID, -1L);
                    bWrapper.putObject(CommandConstants.VELOCITY_AGENT_TYPE, -1L);
                    bWrapper.putObject(CommandConstants.VELOCITY_TRANSACTION_AMOUNT, Double.parseDouble(txAmount));
                    bWrapper.putObject(CommandConstants.VELOCITY_ACCOUNT_TYPE, customerModel.getCustomerAccountTypeId());
                    bWrapper.putObject(CommandConstants.VELOCITY_CUSTOMER_ID, userId);
//					bWrapper.putObject(CommandConstants.VELOCITY_RETAILER_ID, retailerId);
                    commonCommandManager.checkVelocityCondition(bWrapper);
                    // Velocity validation - end

                    TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
                    transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.CUSTOMER_INITIATED_ACCOUT_TO_CASH_TX);

                    DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
                    deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));

                    String pin = null;
                    pin = CommonUtils.generateOneTimePin(5);
                    logger.info("**************************");
                    logger.info("Your otp is : " + pin);
                    logger.info("**************************");


                    workFlowWrapper.setOneTimePin(pin);
                    //Encode PIN
                    String encryptedPin = EncoderUtils.encodeToSha(pin);
                    workFlowWrapper.setMiniTransactionModel(populateMiniTransactionModel(encryptedPin));

                    workFlowWrapper.setTxProcessingAmount(Double.parseDouble(txProcessingAmount));
                    workFlowWrapper.setTotalAmount(Double.parseDouble(totalAmount));
                    workFlowWrapper.setTransactionAmount(Double.parseDouble(txAmount));
                    workFlowWrapper.setBillAmount(Double.parseDouble(txAmount));
                    workFlowWrapper.setTotalCommissionAmount(Double.parseDouble(commissionAmount));
                    workFlowWrapper.setProductVO(productVo);
                    workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
                    workFlowWrapper.setAccountInfoModel(accountInfoModel);
                    workFlowWrapper.setProductModel(productModel);
                    workFlowWrapper.setCustomerAppUserModel(appUserModel);
                    workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
                    workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
                    workFlowWrapper.setAppUserModel(appUserModel);
                    workFlowWrapper.setDiscountAmount(new Double(this.discountAmount).doubleValue());
                    workFlowWrapper.setSegmentModel(segmentModel);
                    workFlowWrapper.putObject(CommandFieldConstants.KEY_CNIC, appUserModel.getNic());
                    workFlowWrapper.setCommissionSettledOnLeg2(true);
                    workFlowWrapper.setIsCustomerInitiatedTransaction(true);
                    TransactionPurposeModel transactionPurposeModel = new TransactionPurposeModel();
                    transactionPurposeModel.setCode(transactionPurposeCode);
                    List<TransactionPurposeModel> list = null;
                    if (transactionPurposeCode != null && !transactionPurposeCode.equals(""))
                        list = commonCommandManager.getTransactionPurposeDao().findByExample(transactionPurposeModel).getResultsetList();
                    if (list != null && !list.isEmpty()) {
                        workFlowWrapper.putObject("TRANS_PURPOSE_MODEL", list.get(0));
                    }
//					put walkin customer details in wrapper to populate in AccountToCashTransaction.doPreStart() from DB.
                    workFlowWrapper.setRecipientWalkinCustomerModel(new WalkinCustomerModel(recepientWalkinCNIC));
                    workFlowWrapper.setRecipientWalkinSmartMoneyAccountModel(new SmartMoneyAccountModel());
                    workFlowWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, channelId);
                    workFlowWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, terminalId);
                    workFlowWrapper.putObject(CommandFieldConstants.KEY_STAN, stan);
                    workFlowWrapper.putObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE, thirdPartyTransactionId);

                    StringBuilder logString = new StringBuilder();
                    logString.append("[CustomerInitiatedAccountToCashCommand.execute] ")
                            .append(" Sender appUserId: " + appUserModel.getAppUserId());

                    logger.info(logString.toString());

                    workFlowWrapper = commonCommandManager.executeSaleCreditTransaction(workFlowWrapper);
                    senderCustomerBalance = CommonUtils.getDoubleOrDefaultValue(workFlowWrapper.getOLASwitchWrapper().getOlavo().getFromBalanceAfterTransaction()); // Sender balance
                    transactionModel = workFlowWrapper.getTransactionModel();
                    //smartMoneyAccountModel = workFlowWrapper.getOlaSmartMoneyAccountModel();

                    productModel = workFlowWrapper.getProductModel();
                    //userDeviceAccountsModel =  workFlowWrapper.getUserDeviceAccountModel();
                    //discountAmount = workFlowWrapper.getDiscountAmount();
                    //successMessage = workFlowWrapper.getSuccessMessage().getSmsMessageText();
                    txProcessingAmount = workFlowWrapper.getCommissionAmountsHolder().getTransactionProcessingAmount().toString();
					
				/*	if(((AccountToCashVO)workFlowWrapper.getProductVO()).getBalance() !=null && ((AccountToCashVO)workFlowWrapper.getProductVO()).getBalance().doubleValue() != 0.0d){
						customer1Balance= ((AccountToCashVO)workFlowWrapper.getProductVO()).getBalance();
					}*/

                    //Double charges = workFlowWrapper.getTransactionModel().getTotalCommissionAmount();

                    commonCommandManager.sendSMS(workFlowWrapper);

                } else {
                    logger.error(" Exception occured in [CustomerInitiatedAccountToCashCommand.execute()]");

                    throw new CommandException(validationErrors.getErrors(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                }
            } catch (FrameworkCheckedException ex) {
                ivrErrorCode = ErrorCodes.COMMAND_EXECUTION_ERROR.toString();

                if (logger.isErrorEnabled()) {
                    logger.error(" Exception occured in [CustomerInitiatedAccountToCashCommand.execute()]");
                }
                throw new CommandException(ex.getMessage(), Long.valueOf(ivrErrorCode), ErrorLevel.MEDIUM, ex);
            } catch (WorkFlowException wex) {
                ivrErrorCode = ErrorCodes.COMMAND_EXECUTION_ERROR.toString();

                if (logger.isErrorEnabled()) {
                    logger.error(" Exception occured in [CustomerInitiatedAccountToCashCommand.execute()]");
                }
                throw new CommandException(wex.getMessage(), Long.valueOf(ivrErrorCode), ErrorLevel.MEDIUM, wex);
            } catch (Exception ex) {
                ivrErrorCode = ErrorCodes.COMMAND_EXECUTION_ERROR.toString();

                if (logger.isErrorEnabled()) {
                    logger.error(" Exception occured in [CustomerInitiatedAccountToCashCommand.execute()]");
                }
                throw new CommandException(ex.getMessage(), Long.valueOf(ivrErrorCode), ErrorLevel.MEDIUM, ex);
            }
        } else {
            logger.error(" Exception occured in [CustomerInitiatedAccountToCashCommand.execute()]");
            throw new CommandException(this.getMessageSource().getMessage("Customer AccountToCashCommand.invalidAppUserType", null, null), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("End of CustomerInitiatedAccountToCashCommand.execute()");
        }
    }

    @Override
    public void prepare(BaseWrapper baseWrapper) {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CustomerInitiatedAccountToCashCommand.prepare()");
        }
        this.baseWrapper = baseWrapper;
        appUserModel = ThreadLocalAppUser.getAppUserModel();
        transactionPurposeCode = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TRANS_PURPOSE_CODE);
        productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
        recepientWalkinCNIC = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_R_W_CNIC);
        recepientWalkinMobile = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE);
        senderMobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);

        deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        txProcessingAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_PROCESS_AMNT);
        totalAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TOTAL_AMOUNT);
        commissionAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_COMM_AMOUNT);
        txAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_AMOUNT);
        channelId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CHANNEL_ID);
        terminalId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TERMINAL_ID);
        stan = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_STAN);
        thirdPartyTransactionId = getCommandParameter(baseWrapper, FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE);


        try {
            fromSegmentId = this.getCommonCommandManager().getCustomerSegmentIdByMobileNo(senderMobileNo);
        } catch (Exception e) {
            logger.error("[CustomerInitiatedAccountToCashCommand.prepare] Unable to load Sender Customer Segment info... ", e);
        }

        logger.info("[CustomerInitiatedAccountToCashCommand.prepare] Recipient CNIC:" + recepientWalkinCNIC + " RecepientMobileNo:" + recepientWalkinMobile + " Sender AppUserID:" + appUserModel.getAppUserId());


        if (logger.isDebugEnabled()) {
            logger.debug("End of CustomerInitiatedAccountToCashCommand.prepare()");
        }
    }

    @Override
    public String response() {
        return toXML();
    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CustomerInitiatedAccountToCashCommand.validate()");
        }
        //validationErrors = ValidatorWrapper.doRequired(transactionPurposeCode,validationErrors,"Transaction Purpose");
        validationErrors = ValidatorWrapper.doRequired(senderMobileNo, validationErrors, "Sender Mobile No");
        validationErrors = ValidatorWrapper.doRequired(recepientWalkinMobile, validationErrors, "Recipient Walkin Mobile No");
        validationErrors = ValidatorWrapper.doRequired(recepientWalkinCNIC, validationErrors, "Recipient Walkin CNIC No");

        validationErrors = ValidatorWrapper.doRequired(productId, validationErrors, "Product");
        validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
        validationErrors = ValidatorWrapper.doRequired(txAmount, validationErrors, "Tx Amount");
        validationErrors = ValidatorWrapper.doRequired(totalAmount, validationErrors, "Total Amount");
        validationErrors = ValidatorWrapper.doRequired(commissionAmount, validationErrors, "Commission Amount");
        validationErrors = ValidatorWrapper.doRequired(txProcessingAmount, validationErrors, "Tx Processing Amount");


        if (!validationErrors.hasValidationErrors()) {
            validationErrors = ValidatorWrapper.doNumeric(productId, validationErrors, "Product");
            validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
            validationErrors = ValidatorWrapper.doNumeric(txAmount, validationErrors, "Tx Amount");
            validationErrors = ValidatorWrapper.doNumeric(txProcessingAmount, validationErrors, "Tx Processing Amount");
        }


        if (logger.isDebugEnabled()) {
            logger.debug("End of CustomerInitiatedAccountToCashCommand.validate()");
        }
        return validationErrors;
    }

    private String toXML() {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CustomerInitiatedAccountToCashCommand.toXML()");
        }
        StringBuilder strBuilder = new StringBuilder();

        /*	if (isIvrResponse == false) {*/
        Double totalCommission = Double.valueOf(txProcessingAmount);
        strBuilder.append(TAG_SYMBOL_OPEN)
                .append(TAG_TRANS)
                .append(TAG_SYMBOL_CLOSE)
                .append(TAG_SYMBOL_OPEN)
                .append(TAG_TRN)
                .append(TAG_SYMBOL_SPACE)
                .append(ATTR_TRXID)
                .append(TAG_SYMBOL_EQUAL)
                .append(TAG_SYMBOL_QUOTE)
                .append(replaceNullWithEmpty(transactionModel.getTransactionCodeIdTransactionCodeModel().getCode()))
                .append(TAG_SYMBOL_QUOTE)
                .append(TAG_SYMBOL_SPACE)
                .append(ATTR_RWCNIC)
                .append(TAG_SYMBOL_EQUAL)
                .append(TAG_SYMBOL_QUOTE)
                .append(replaceNullWithEmpty(recepientWalkinCNIC))
                .append(TAG_SYMBOL_QUOTE)
                .append(TAG_SYMBOL_SPACE)
                .append(ATTR_RCMOB)
                .append(TAG_SYMBOL_EQUAL)
                .append(TAG_SYMBOL_QUOTE)
                .append(recepientWalkinMobile)
                .append(TAG_SYMBOL_QUOTE)
                .append(TAG_SYMBOL_SPACE)

/*					.append(ATTR_RWMOB)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(recepientWalkinMobile)
					.append(TAG_SYMBOL_QUOTE)
					.append(TAG_SYMBOL_SPACE)*/

                .append(ATTR_DATE)
                .append(TAG_SYMBOL_EQUAL)
                .append(TAG_SYMBOL_QUOTE)
                .append(transactionModel.getCreatedOn())
                .append(TAG_SYMBOL_QUOTE)
                .append(TAG_SYMBOL_SPACE)
                .append(ATTR_DATEF)
                .append(TAG_SYMBOL_EQUAL)
                .append(TAG_SYMBOL_QUOTE)
                .append(PortalDateUtils.formatDate(transactionModel.getCreatedOn(), PortalDateUtils.SHORT_DATE_TIME_FORMAT3))
                .append(TAG_SYMBOL_QUOTE)
                .append(TAG_SYMBOL_SPACE)
                .append(ATTR_TIMEF)
                .append(TAG_SYMBOL_EQUAL)
                .append(TAG_SYMBOL_QUOTE)
                .append(Formatter.formatTime(transactionModel.getCreatedOn()))
                .append(TAG_SYMBOL_QUOTE)
                .append(TAG_SYMBOL_SPACE)
                .append(ATTR_PROD)
                .append(TAG_SYMBOL_EQUAL)
                .append(TAG_SYMBOL_QUOTE)
                .append(replaceNullWithEmpty(productModel.getDescription()))
                .append(TAG_SYMBOL_QUOTE)

                .append(TAG_SYMBOL_SPACE)
                .append(ATTR_TPAM)
                .append(TAG_SYMBOL_EQUAL)
                .append(TAG_SYMBOL_QUOTE)
                .append(replaceNullWithZero(totalCommission))
                .append(TAG_SYMBOL_QUOTE)

                .append(TAG_SYMBOL_SPACE)
                .append(ATTR_TPAMF)
                .append(TAG_SYMBOL_EQUAL)
                .append(TAG_SYMBOL_QUOTE)
                .append(Formatter.formatNumbers(totalCommission))
                .append(TAG_SYMBOL_QUOTE)

                .append(TAG_SYMBOL_SPACE)
                .append(ATTR_TAMT)
                .append(TAG_SYMBOL_EQUAL)
                .append(TAG_SYMBOL_QUOTE)
                .append(replaceNullWithZero(transactionModel.getTotalAmount()))
                .append(TAG_SYMBOL_QUOTE)

                .append(TAG_SYMBOL_SPACE)
                .append(ATTR_TAMTF)
                .append(TAG_SYMBOL_EQUAL)
                .append(TAG_SYMBOL_QUOTE)
                .append(Formatter.formatNumbers(transactionModel.getTotalAmount()))
                .append(TAG_SYMBOL_QUOTE)

                .append(TAG_SYMBOL_SPACE)
                .append(ATTR_TXAM)
                .append(TAG_SYMBOL_EQUAL)
                .append(TAG_SYMBOL_QUOTE)
                .append(replaceNullWithZero(transactionModel.getTransactionAmount()))
                .append(TAG_SYMBOL_QUOTE)

                .append(TAG_SYMBOL_SPACE)
                .append(ATTR_BALF)
                .append(TAG_SYMBOL_EQUAL)
                .append(TAG_SYMBOL_QUOTE)
                .append(Formatter.formatNumbers(senderCustomerBalance))
                .append(TAG_SYMBOL_QUOTE)

                .append(TAG_SYMBOL_SPACE)
                .append(ATTR_TXAMF)
                .append(TAG_SYMBOL_EQUAL)
                .append(TAG_SYMBOL_QUOTE)
                .append(Formatter.formatNumbers(transactionModel.getTransactionAmount()))
                .append(TAG_SYMBOL_QUOTE)
        ;

        strBuilder.append(TAG_SYMBOL_CLOSE);

        strBuilder.append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH)
                .append(TAG_TRN)
                .append(TAG_SYMBOL_CLOSE)
                .append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH)
                .append(TAG_TRANS)
                .append(TAG_SYMBOL_CLOSE);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CustomerInitiatedAccountToCashCommand.toXML()");
        }
        //	}

        return strBuilder.toString();
    }

    /**
     * @param workFlowWrapper
     */
    private MiniTransactionModel populateMiniTransactionModel(String encryptedPin) throws CommandException {

        MiniTransactionModel miniTransactionModel = new MiniTransactionModel();

        try {
            miniTransactionModel.setCommandId(Long.valueOf(CommandFieldConstants.CMD_ACCOUNT_TO_CASH));
            miniTransactionModel.setAppUserId(ThreadLocalAppUser.getAppUserModel().getAppUserId());
            miniTransactionModel.setTimeDate(new Date());
            miniTransactionModel.setMobileNo(recepientWalkinMobile);
            miniTransactionModel.setSmsText(recepientWalkinMobile + " " + txAmount);
            miniTransactionModel.setMiniTransactionStateId(MiniTransactionStateConstant.PIN_SENT);
            miniTransactionModel.setActionLogId(ThreadLocalActionLog.getActionLogId());
            miniTransactionModel.setOneTimePin(encryptedPin);//save pin in miniTransaction.
            miniTransactionModel.setCAMT(Double.parseDouble(StringUtil.isNullOrEmpty(commissionAmount) ? "0" : commissionAmount));
            miniTransactionModel.setBAMT(Double.parseDouble(StringUtil.isNullOrEmpty(txAmount) ? "0" : txAmount));
            miniTransactionModel.setTAMT(Double.parseDouble(StringUtil.isNullOrEmpty(totalAmount) ? "0" : totalAmount));
            miniTransactionModel.setTPAM(Double.parseDouble(StringUtil.isNullOrEmpty(txProcessingAmount) ? "0" : txProcessingAmount));
			
/*			if(manualOTPin != null && ! "".equals(manualOTPin)){//if Pin is manually set by customer, use this.
				miniTransactionModel.setIsManualOTPin(Boolean.TRUE);
			}*/


        } catch (Exception e) {

            if (logger.isErrorEnabled()) {
                logger.error("[CustomerInitiatedAccountToCashCommand.populateMiniTransactionModel] Exception in populating MiniTransactionModel. Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Details\n" + ExceptionProcessorUtility.prepareExceptionStackTrace(e));
            }

            throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
        }

        return miniTransactionModel;

    }


}
