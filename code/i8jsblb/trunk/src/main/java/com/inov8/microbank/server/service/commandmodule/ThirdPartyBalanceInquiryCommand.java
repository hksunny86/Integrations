package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import com.inov8.microbank.server.service.integration.vo.BillPaymentVO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static com.inov8.microbank.common.util.XMLConstants.*;

/**
 * Created by Attique on 9/3/2018.
 */
public class ThirdPartyBalanceInquiryCommand extends BaseCommand {

    private final Log logger = LogFactory.getLog(ThirdPartyBalanceInquiryCommand.class);

    private String paymentType = "0";
    private String productId;
    private String retailerMobileNumber;
    private String terminalId;
    private String consumerNumber;
    private String lastFingerIndex;
    private BillPaymentVO billPaymentVO;
    private String customerCNIC, customerMobileNo, customerAccountNo,customerAccountTitle,agentMobileNo, withdrawalAmount;
    private AppUserModel appUserModel;
    private ProductModel productModel;
    private UserDeviceAccountsModel userDeviceAccountsModel;
    private String companyCode,sessionId;
    private String agentId;

    private BaseWrapper baseWrapper;
    private BaseWrapper preparedBaseWrapper;
    private CommissionAmountsHolder commissionAmountsHolder;
    private ESBAdapter esbAdapter;

    private String isWalletExists = "N";
    private String walletAccountIdBAFL = "";
    private String walletAccountBalanceBAFL = "0";
    private String latitude,longitude, macAddress, imeiNumber;


    private Long reasonId = -1L;

    @Override
    public void prepare(BaseWrapper baseWrapper) {
        String classNMethodName = "[ThirdPartyBalanceInquiryCommand.prepare] ";
        customerMobileNo = this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_CUSTOMER_MOBILE);
        deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        paymentType = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PAYMENT_TYPE);
        productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
        latitude = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_LATITUDE);
        longitude = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_LONGITUDE);
        macAddress = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MAC_ADDRESS);
        imeiNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOBILE_IMEI_NUMBER);
        if(!productId.equals(ProductConstantsInterface.EOBI_CASH_OUT_ID))
            customerCNIC = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CNIC);
        else
            customerMobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
        retailerMobileNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.CMD_AGNETMATE_AGENT_MOBILE_NUMBER);

        if(null!=this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_WITHDRAWAL_AMOUNT) && !"".equals(this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_WITHDRAWAL_AMOUNT)))
            withdrawalAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_WITHDRAWAL_AMOUNT);
        else
            withdrawalAmount = "1";
        agentId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AGENT_ID);
        terminalId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TERMINAL_ID);

        baseWrapper.putObject(CommandFieldConstants.KEY_CSCD, consumerNumber);

        appUserModel = ThreadLocalAppUser.getAppUserModel();

        if(appUserModel == null) {
            appUserModel = getAppUserModel(retailerMobileNumber);
        }
        try{
            BaseWrapper bWrapper = new BaseWrapperImpl();
            productModel = new ProductModel();
            productModel.setProductId(Long.parseLong(productId));
            bWrapper.setBasePersistableModel(productModel);
            bWrapper = getCommonCommandManager().loadProduct(bWrapper);
            productModel = (ProductModel)bWrapper.getBasePersistableModel();
            this.baseWrapper=baseWrapper;
        }

        catch(Exception ex){
            logger.error(classNMethodName +" Product model not found: " + ex.getStackTrace());
        }

    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        validationErrors = ValidatorWrapper.doRequired(productId,validationErrors,"Product");
        validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
//		validationErrors = ValidatorWrapper.doRequired(accountId,validationErrors,"Account Id");
//		validationErrors = ValidatorWrapper.doRequired(customerCode,validationErrors,"Customer Code");

        if(!productId.equals(ProductConstantsInterface.EOBI_CASH_OUT_ID))
            validationErrors = ValidatorWrapper.doRequired(customerCNIC,validationErrors,"Customer CNIC");
        else
            validationErrors = ValidatorWrapper.doRequired(customerMobileNo,validationErrors,"Customer Mobile");


        if(!validationErrors.hasValidationErrors()) {
            validationErrors = ValidatorWrapper.doNumeric(productId,validationErrors,"Product");
            validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
        }


        return validationErrors;
    }

    @Override
    public void execute() throws CommandException {

        String integTargetEnv = MessageUtil.getMessage("I8MICROBANK.TARGET.ENVIRONMENT");
        Boolean skipForLocalTesting = Boolean.FALSE;
        if(integTargetEnv != null && !integTargetEnv.equals("") && integTargetEnv.equals("MOCK"))
            skipForLocalTesting = Boolean.TRUE;
        WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
        workFlowWrapper.setHandlerModel(handlerModel);
        workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);
        this.esbAdapter=getEsbAdapter();

        CommissionWrapper commissionWrapper;

        String classNMethodName = "[ThirdPartyBalanceInquiryCommand.execute] ";
        String exceptionMessage = "Exception occurred ";
        String inputParams = "AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Product ID: " +  productId + " Customer CNIC:" + customerCNIC ;
        String genericExceptionMessage = classNMethodName + exceptionMessage + inputParams;
        ValidationErrors validationErrors;
        if(appUserModel.getRetailerContactId() != null) {
            try {
                validationErrors=getCommonCommandManager().checkActiveAppUser(appUserModel);
                if(validationErrors.hasValidationErrors()){
                    throw new Exception("Agent Is not in correct state.");
                }
                Boolean isBlackListed = getCommonCommandManager().isCnicBlacklisted(customerCNIC);
                if(isBlackListed)
                    throw new CommandException("Customer CNIC is BlackListed.",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
                userDeviceAccountsModel = commonCommandManager.getUserDeviceAccountListViewManager().findUserDeviceByAppUserId(appUserModel.getAppUserId());
                Long result = commonCommandManager.getBispCustNadraVerificationDAO().isBVSSuccessful(null,
                        null,customerCNIC,-1L,UserTypeConstantsInterface.CUSTOMER);
                if(result >= 16)
                    throw new CommandException(MessageUtil.getMessage("BISP.cust.daily.NADRA.retries"), ErrorCodes.INVALID_INPUT,ErrorLevel.MEDIUM,null);
                else{
                    result = commonCommandManager.getBispCustNadraVerificationDAO().isBVSSuccessful(null,
                            userDeviceAccountsModel.getUserId(),customerCNIC,-1L,UserTypeConstantsInterface.CUSTOMER);
                    if(result >= 8)
                        throw new CommandException(MessageUtil.getMessage("BISP.cust.agent.NADRA.retries"), ErrorCodes.INVALID_INPUT,ErrorLevel.MEDIUM,null);
                }
                TransactionModel transactionModel = new TransactionModel();

                Long pimInfoId = productModel.getProductIntgModuleInfoId();
                Long piVOId = productModel.getProductIntgVoId();

                if (pimInfoId == null || "".equals(pimInfoId) || piVOId == null || "".equals(piVOId)) {
                    logger.error(classNMethodName + " Unable to load Product VO. " + inputParams + exceptionMessage);
                    throw new CommandException(this.getMessageSource().getMessage("billPaymentCommand.productVoLoadedError", null, null), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                }

                /*ProductVO productVO = getCommonCommandManager().loadProductVO(baseWrapperTemp);
                if (productVO == null) {
                    throw new CommandException("ProductVo is not loaded", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                }*/

                workFlowWrapper.setProductModel(productModel);
                // workFlowWrapper.setProductVO(productVO);

                logger.info(classNMethodName + inputParams);

                TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
                transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.CASH_WITHDRAWAL_TX);
                workFlowWrapper.setProductModel(productModel);
                workFlowWrapper.setTransactionTypeModel(transactionTypeModel);

                workFlowWrapper.setDeviceTypeModel(new DeviceTypeModel());

                workFlowWrapper.getDeviceTypeModel().setDeviceTypeId(Long.valueOf(deviceTypeId));
                SegmentModel segmentModel = new SegmentModel();
                segmentModel.setSegmentId(CommissionConstantsInterface.DEFAULT_SEGMENT_ID);
                workFlowWrapper.setSegmentModel(segmentModel);

                workFlowWrapper.setTransactionModel(transactionModel);



                if(null!=this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_WITHDRAWAL_AMOUNT)
                        && !"".equals(this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_WITHDRAWAL_AMOUNT))){
                    getCommonCommandManager().checkProductLimit(null, productModel.getProductId(), appUserModel.getMobileNo(),
                            Long.valueOf(deviceTypeId), Double.parseDouble(withdrawalAmount), productModel, null, workFlowWrapper.getHandlerModel());


                    transactionModel.setTransactionAmount(Double.valueOf(withdrawalAmount));
                    commissionWrapper = getCommonCommandManager().calculateCommission(workFlowWrapper);
                    commissionAmountsHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);

                }

                baseWrapper=new BaseWrapperImpl();
                RetailerContactModel retailerContactModel = new RetailerContactModel();
                retailerContactModel.setRetailerContactId(appUserModel.getRetailerContactId());
                baseWrapper.setBasePersistableModel(retailerContactModel);
                baseWrapper = getCommonCommandManager().loadRetailerContact(baseWrapper);
                retailerContactModel = (RetailerContactModel) baseWrapper.getBasePersistableModel();

//                GeoLocationModel geoLocationModel  = new GeoLocationModel();
//                if(retailerContactModel.getGeoLocationId()!=null){
//                    geoLocationModel = commonCommandManager.getGeoLocationDao().findByPrimaryKey(retailerContactModel.getGeoLocationId());
//                }
//                String existingLatitude = String.valueOf(geoLocationModel.getLatitude());
//                String existingLongitude = String.valueOf(geoLocationModel.getLongitude());
//
//                if(latitude.contains(existingLatitude.substring(0,7))) {
//                    latitude = existingLatitude;
//                }
//                else{
//                    throw new CommandException("You are not currently at the authorized location to conduct this transaction.",ErrorCodes.COMMAND_EXECUTION_ERROR,
//                            ErrorLevel.HIGH);
//                }
//                if(longitude.contains(existingLongitude.substring(0,7))) {
//                    longitude = existingLongitude;
//                }
//                else{
//                    throw new CommandException("You are not currently at the authorized location to conduct this transaction.",ErrorCodes.COMMAND_EXECUTION_ERROR,
//                            ErrorLevel.HIGH);
//                }
                workFlowWrapper.setRetailerContactModel(retailerContactModel);
                workFlowWrapper.setTaxRegimeModel(retailerContactModel.getTaxRegimeIdTaxRegimeModel());
                ESBAdapter adapter =new ESBAdapter();

                I8SBSwitchControllerRequestVO requestVO=new I8SBSwitchControllerRequestVO();
                I8SBSwitchControllerResponseVO responseVO=new I8SBSwitchControllerResponseVO();

                if(productId.equals(ProductConstantsInterface.EOBI_CASH_OUT_ID))
                    requestVO = adapter.prepareRequestVoForApiGee(I8SBConstants.RequestType_EOBI_TitleFetch);
                else
                {
                    requestVO = adapter.prepareRequestVoForApiGee(I8SBConstants.RequestType_AccountBalanceInquiry);
                    if(customerMobileNo != null && CommonUtils.isValidMobileNo(customerMobileNo)){
                        requestVO.setWalletRequired("Y");
                        requestVO.setConsumerNumber(customerMobileNo);
                    }
                    else
                    {
                        requestVO.setWalletRequired("N");
                    }
                }
                if(!productId.equals(ProductConstantsInterface.EOBI_CASH_OUT_ID))
                    requestVO.setCNIC(customerCNIC);
                else {
                    requestVO.setConsumerNumber(customerMobileNo);
                    requestVO.setAmount(withdrawalAmount);
                }
                HttpServletRequest req = null;
                ActionLogModel actionLogModel = new ActionLogModel();
//                actionLogModel.setAppUserId(appUserModel.getAppUserId());
                actionLogModel.setActionLogId(ThreadLocalActionLog.getActionLogId());
//                actionLogModel.setCommandId(Long.valueOf(CommandFieldConstants.KEY_CMD_THIRD_PARTY_BALANCE_INQUIRY));
                actionLogModel = commonCommandManager.getActionLogDao().findByPrimaryKey(ThreadLocalActionLog.getActionLogId());
                String ipAddress = actionLogModel.getClientIpAddress();
                requestVO.setTransactionId(requestVO.getSTAN() + requestVO.getTransmissionDateAndTime());
                requestVO.setTransactionDateTime(String.valueOf( new Date()));
                requestVO.setSenderMobile(retailerMobileNumber);
                requestVO.setProductCode(productModel.getProductCode());
                requestVO.setAgentId(agentId);
                requestVO.setTerminalID(terminalId);
                requestVO.setReserved1("");
                requestVO.setLongitude(longitude);
                requestVO.setLatitude(latitude);
                requestVO.setMacAddress(macAddress);
                requestVO.setIpAddress(ipAddress);
                requestVO.setImeiNumber(imeiNumber);
                SwitchWrapper sWrapper=new SwitchWrapperImpl();
                sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
                if(!skipForLocalTesting)
                    sWrapper=adapter.makeI8SBCall(sWrapper);
                else
                    Thread.sleep(2000);
                if(!skipForLocalTesting && !productId.equals(ProductConstantsInterface.EOBI_CASH_OUT_ID))
                   ESBAdapter.processI8sbResponseCode(sWrapper.getI8SBSwitchControllerResponseVO(),false);
                else if(!skipForLocalTesting)
                    ESBAdapter.processI8sbResponseCodeForEOBI(sWrapper.getI8SBSwitchControllerResponseVO(),true);
                if(!skipForLocalTesting)
                    responseVO=sWrapper.getI8SBSwitchControllerResponseVO().getI8SBSwitchControllerResponseVOList().get(1);
                else
                {
                    responseVO.setAccountBalance("1000");//MessageUtil.getMessage("bisp.ac.bal")
                    responseVO.setMobilePhone("03901234768");
                    responseVO.setConsumerNumber("1000567467");
                    responseVO.setSessionId("1005467891");
                    responseVO.setAccountTitle("BISP Test Account");
                    if(requestVO.getWalletRequired() != null && requestVO.getWalletRequired().equals("Y"))
                        responseVO.setWalletAccountId("188374837");
                    responseVO.setWalletBalance("120000");
                    walletAccountIdBAFL = responseVO.getWalletAccountId();
                    if(walletAccountIdBAFL != null && walletAccountIdBAFL.length() > 1)
                        isWalletExists = "Y";
                    else
                        isWalletExists = "N";
                    walletAccountBalanceBAFL = responseVO.getWalletBalance();
                }
                lastFingerIndex = responseVO.getLastFingerIndex();
                baseWrapper.putObject(CommandFieldConstants.KEY_LAST_FINGER_INDEX, lastFingerIndex);

                if(responseVO.getAccountBalance()!=null && responseVO.getAvailableLimit()!=null) {
                    if (Double.valueOf(responseVO.getAccountBalance()) < Double.valueOf(responseVO.getAvailableLimit()))
                        withdrawalAmount = responseVO.getAccountBalance();
                    else
                        withdrawalAmount = responseVO.getAvailableLimit();
                    if(responseVO.getAccountBalance().equals("0.0")){
                        throw new CommandException("Insufficient Balance", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Exception());
                    }else if(responseVO.getAvailableLimit().equals("0.0")){
                        throw new CommandException("Limit exhausted ", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Exception());
                    }else {

                        if(null==this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_WITHDRAWAL_AMOUNT)
                                || "".equals(this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_WITHDRAWAL_AMOUNT))){
                            getCommonCommandManager().checkProductLimit(null, productModel.getProductId(), appUserModel.getMobileNo(),
                                    Long.valueOf(deviceTypeId), Double.parseDouble(withdrawalAmount), productModel, null, workFlowWrapper.getHandlerModel());

                            transactionModel.setTransactionAmount(Double.valueOf(withdrawalAmount));
                            commissionWrapper = getCommonCommandManager().calculateCommission(workFlowWrapper);
                            commissionAmountsHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
                            walletAccountIdBAFL = responseVO.getWalletAccountId();
                            if(walletAccountIdBAFL != null && walletAccountIdBAFL.length() > 1)
                            {
                                isWalletExists = "Y";
                                walletAccountBalanceBAFL = responseVO.getWalletBalance();
                            }
                            else
                                isWalletExists = "N";
                        }
                    }
                }
                if(!productId.equals(ProductConstantsInterface.EOBI_CASH_OUT_ID))
                    customerMobileNo=responseVO.getMobilePhone();
                customerAccountTitle=responseVO.getAccountTitle();
                customerAccountNo=responseVO.getConsumerNumber();
                sessionId=responseVO.getSessionId();
                if(skipForLocalTesting)
                {
                    commissionAmountsHolder = new CommissionAmountsHolder();
                    commissionAmountsHolder.setTransactionAmount(Double.parseDouble("1000"));//MessageUtil.getMessage("bisp.cw.trx.amount")
                    commissionAmountsHolder.setTotalCommissionAmount(10.0D);
                    commissionAmountsHolder.setTransactionProcessingAmount(0.0D);
                    commissionAmountsHolder.setTotalAmount(Double.parseDouble("1000") + 10.0D );//MessageUtil.getMessage("bisp.cw.trx.amount")
                }
            } catch (CommandException e) {
                logger.error(genericExceptionMessage + e.getMessage());
                throw e;
            } catch (WorkFlowException we) {
                logger.error(genericExceptionMessage + we.getMessage());
                    if(we.getMessage().equals("Authorization Failed")) {
                        throw new CommandException("Agent BVS is Required", ErrorCodes.AGENT_BVS_REQUIRED, ErrorLevel.MEDIUM);
                    }
                throw new CommandException(we.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, we);
            } catch (Exception ex) {
                logger.error(genericExceptionMessage + ex.getMessage());
                if (ex.getMessage() != null && ex.getMessage().indexOf("JTA") != -1) {
                    throw new CommandException(this.getMessageSource().getMessage("command.unexpectedError", null, null), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
                } else {
                    throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
                }
            }
        }else {
            logger.error("[ThirdPartyBalanceInquiryCommand.execute] Throwing Exception in Product ID: " +  productId + " - Agent AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
            throw new CommandException(this.getMessageSource().getMessage("getSupplierInfoCommand.invalidAppUserType", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
        }


    }

    @Override
    public String response() {

        StringBuilder responseXML = new StringBuilder();
        responseXML.append(TAG_SYMBOL_OPEN)
                .append(TAG_PARAMS)
                .append(TAG_SYMBOL_CLOSE);
        //BISP Phase-2 changes
        responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_BAFL_WALLET_ACCOUNT_ID, walletAccountIdBAFL));
        if(isWalletExists.equalsIgnoreCase("Y"))
            isWalletExists = "1";
        else
            isWalletExists = "0";
        responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_BAFL_WALLET, isWalletExists));
        responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_BAFL_WALLET_BALANCE, walletAccountBalanceBAFL));
        //
        responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_DEVICE_TYPE_ID, "5"));
        responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CUSTOMER_MOBILE, customerMobileNo));
        responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_ACCOUNT_NUMBER, customerAccountNo));
        responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_ACCOUNT_TITLE_AGNETMATE, customerAccountTitle));
        responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_AGENT_MOBILE, retailerMobileNumber));
        responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_SESSION_ID, sessionId));
        responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FINGER_INDEX, lastFingerIndex));

        if(productId.equals(ProductConstantsInterface.EOBI_CASH_OUT_ID))
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_IS_OTP_REQUIRED, "1"));
        if(productModel.getBvsConfigId()!=null)
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_IS_BVS_REQUIRED, String.valueOf(productModel.getBvsConfigId())));
        else
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_IS_BVS_REQUIRED, "0"));

        if(commissionAmountsHolder != null) {
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TXAM, replaceNullWithZero(commissionAmountsHolder.getTransactionAmount()).toString()));
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TXAMF, Formatter.formatNumbers(replaceNullWithZero(commissionAmountsHolder.getTransactionAmount()))));
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TAMT, replaceNullWithZero(commissionAmountsHolder.getTotalAmount()).toString()));
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TAMTF, Formatter.formatNumbers(replaceNullWithZero(commissionAmountsHolder.getTotalAmount()))));

            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CAMT, replaceNullWithZero(commissionAmountsHolder.getTotalCommissionAmount()).toString()));
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CAMTF, Formatter.formatNumbers(replaceNullWithZero(commissionAmountsHolder.getTotalCommissionAmount()))));

            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TPAM, replaceNullWithZero(commissionAmountsHolder.getTransactionProcessingAmount()).toString()));
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TPAMF, Formatter.formatNumbers(replaceNullWithZero(commissionAmountsHolder.getTransactionProcessingAmount()))));
        }

        responseXML.append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH)
                .append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);

        return responseXML.toString();
    }

    private AppUserModel getAppUserModel(String _retailerMobileNumber) {

        AppUserModel _appUserModel = new AppUserModel();
        _appUserModel.setMobileNo(_retailerMobileNumber);
        _appUserModel.setAppUserTypeId(UserTypeConstantsInterface.RETAILER);

        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setBasePersistableModel(_appUserModel);

        try {

            searchBaseWrapper = getCommonCommandManager().loadAppUserByMobileNumberAndType(searchBaseWrapper);

        } catch (FrameworkCheckedException e) {

            logger.error("Could not load AppUser in ThirdPartyBalanceInquiryCommand");
        }

        return (AppUserModel) searchBaseWrapper.getBasePersistableModel();
    }

    private SmartMoneyAccountModel getSmartMoneyAccountModel(Long retailerContactId) throws FrameworkCheckedException {

        SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
        smartMoneyAccountModel.setRetailerContactId(appUserModel.getRetailerContactId());
        smartMoneyAccountModel.setChangePinRequired(Boolean.TRUE);
        smartMoneyAccountModel.setDeleted(Boolean.FALSE);
        smartMoneyAccountModel.setActive(Boolean.TRUE);

        baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
        baseWrapper = getCommonCommandManager().loadOLASmartMoneyAccount(baseWrapper);
        return (SmartMoneyAccountModel)baseWrapper.getBasePersistableModel();
    }

    private AppUserModel getCustomerAppUserModel(String mobileNumber) throws FrameworkCheckedException {

        AppUserModel customerAppUserModel = new AppUserModel();
        customerAppUserModel.setMobileNo(mobileNumber);
        customerAppUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);

        SearchBaseWrapper sBaseWrapper = new SearchBaseWrapperImpl();
        sBaseWrapper.setBasePersistableModel(customerAppUserModel);
        sBaseWrapper = getCommonCommandManager().loadAppUserByMobileNumberAndType(sBaseWrapper);

        customerAppUserModel = (AppUserModel) sBaseWrapper.getBasePersistableModel();

        if(customerAppUserModel != null && customerAppUserModel.getAccountEnabled() != null) {

            if(!customerAppUserModel.getAccountEnabled() || customerAppUserModel.getAccountLocked() || customerAppUserModel.getAccountExpired() ) {
                throw new CommandException( "Transaction cannot be processed. The customer account is locked / deactivated.",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
            }
        }

        return customerAppUserModel;
    }

    public void setEsbAdapter(ESBAdapter esbAdapter) {
        this.esbAdapter = esbAdapter;
    }
    public ESBAdapter getEsbAdapter() {
        return this.esbAdapter ;
    }

}
