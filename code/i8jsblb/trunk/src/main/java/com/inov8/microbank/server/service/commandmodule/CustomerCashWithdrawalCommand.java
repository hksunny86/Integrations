package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.verifly.common.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

public class CustomerCashWithdrawalCommand extends BaseCommand {

    private String manualOTPin;
    private WorkFlowWrapper workFlowWrapper;
    private Date expiryDate = null;
    private final Log logger = LogFactory.getLog(getClass());

    private AppUserModel appUserModel = null;
    String curCommandId;

    @Override
    public void execute() throws CommandException {

        logger.info( "[CustomerCashWithdrawalCommand.execute] Logged in AppUserId : " + appUserModel.getPrimaryKey() + " Mobile No:" + appUserModel.getMobileNo()) ;

        prepareWorkflowWrapper();
        try {
            if(!manualOTPin.isEmpty() && !CommonUtils.validateOneTimePin(manualOTPin)) {
                throw new CommandException(MessageUtil.getMessage("INVALID.SEQUENCE.OTP"), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
            }

            ProductModel prodModel = new ProductModel();
            prodModel.setPrimaryKey(ProductConstantsInterface.CUSTOMER_CASH_WITHDRAWAL);
            BaseWrapper bw = new BaseWrapperImpl();
            bw.setBasePersistableModel(prodModel);
            bw = commonCommandManager.loadProduct(bw);
            prodModel = (ProductModel) bw.getBasePersistableModel();
            workFlowWrapper.setProductModel(prodModel);
            validateRequest();

            workFlowWrapper.putObject("SEND_SMS", "No");
            commonCommandManager.makeOTPGeneration(workFlowWrapper);

            TransactionCodeModel txCodeModel = workFlowWrapper.getTransactionCodeModel();
            String txCode = null, pin = null;
            if(txCodeModel != null) {
                txCode = txCodeModel.getCode();
                pin = workFlowWrapper.getMiniTransactionModel().getPlainOTP();
                long minutes = MessageUtil.getCWOTPValidityInMin();
                String expiryString = "";

                if (minutes != 0.0) {
                    expiryDate = DateUtil.addMinutes(new Date(System.currentTimeMillis()), (int) minutes);
                    expiryString = "This transaction code is valid till " + PortalDateUtils.formatDateTimeDefault(expiryDate);
                }

                BaseWrapper smsBaseWrapper = new BaseWrapperImpl();
                SmsMessage smsMessage = new SmsMessage(appUserModel.getMobileNo(),
                        MessageUtil.getMessage ("otpSmsCW",new Object[]{MessageUtil.getBrandName(), txCode, pin, expiryString}));

                smsBaseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, smsMessage);

                logger.info("**************************");
                logger.info("Your sms is : "+ smsMessage.getMessageText());
                logger.info("**************************");

                commonCommandManager.sendSMSToUser(smsBaseWrapper);

                //commonCommandManager.sendSMSToUser(appUserModel.getMobileNo(), MessageUtil.getMessage ("otpSmsCW",new Object[]{MessageUtil.getBrandName(), txCode, pin, expiryString}));
            }
        }
        catch(Exception ex){
            handleException(ex);
        }
    }

    @Override
    public void prepare(BaseWrapper baseWrapper) {
        appUserModel = ThreadLocalAppUser.getAppUserModel();
        curCommandId = (String) baseWrapper.getObject(CommandFieldConstants.KEY_CURR_COMMAND);
        deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        manualOTPin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MAN_OT_PIN);
        logger.info("[CustomerCashWithdrawalCommand.prepare] Logged in AppUserID:" + appUserModel.getAppUserId() + ". Mobile No:" + appUserModel.getMobileNo());
    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
        validationErrors = ValidatorWrapper.doRequired(curCommandId,validationErrors,"Command Id");
        return validationErrors;
    }

    @Override
    public String response() {
        TransactionCodeModel transactionCodeModel = workFlowWrapper.getTransactionCodeModel();
        Map<String, Object> lvbs = new HashMap<>();

        Date createdOn = transactionCodeModel.getCreatedOn();

        String date = PortalDateUtils.formatDate(createdOn, PortalDateUtils.SHORT_DATE_FORMAT);
        String time = PortalDateUtils.formatDate(createdOn, PortalDateUtils.SHORT_TIME_FORMAT);
        String exDate = PortalDateUtils.formatDateTimeDefault(expiryDate);

        /*if(deviceTypeKey.longValue() == DeviceTypeConstantsInterface.USSD){
            return MessageUtil.getMessage("CUSTOMER_WITHDRAWAL_REQ", transactionCodeModel.getCode(), date ,time , exDate);
        }
        else*/ {
            lvbs.put(CommandFieldConstants.KEY_TX_ID, transactionCodeModel.getCode());
            lvbs.put(CommandFieldConstants.KEY_DATEF, PortalDateUtils.formatDate(transactionCodeModel.getCreatedOn(), PortalDateUtils.SHORT_DATE_TIME_FORMAT));
            lvbs.put(CommandFieldConstants.KEY_DATE, transactionCodeModel.getCreatedOn().toString());
            lvbs.put(CommandFieldConstants.KEY_TIMEF, Formatter.formatTime(transactionCodeModel.getCreatedOn()));
            lvbs.put(CommandFieldConstants.KEY_EXPIRY, exDate);
            lvbs.put("EXPIRY", exDate);

            return MiniXMLUtil.createInfoResponseXMLByParams(lvbs);
        }
    }
    
    //---------------------------  Private Methods  ---------------------------------------
    private void prepareWorkflowWrapper(){
        workFlowWrapper = new WorkFlowWrapperImpl();

        if(StringUtils.isNotEmpty(manualOTPin)){//if Pin is manually set by customer, use this.
            workFlowWrapper.putObject(CommandFieldConstants.KEY_MAN_OT_PIN, Boolean.TRUE);
            manualOTPin = CommonUtils.decryptPin(manualOTPin, EncryptionUtil.ENCRYPTION_TYPE_AES);
            workFlowWrapper.setOneTimePin(manualOTPin);
        }
        workFlowWrapper.putObject(CommandFieldConstants.KEY_CURR_COMMAND_ID, curCommandId );
        workFlowWrapper.putObject(CommandFieldConstants.KEY_MOB_NO, appUserModel.getMobileNo());

//        TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
//        transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.CUSTOMER_INITIATED_CW_TX);
    }

    private void validateRequest() throws FrameworkCheckedException {

        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        MiniTransactionModel miniTransactionModel = new MiniTransactionModel();

        miniTransactionModel.setCommandId(Long.parseLong(curCommandId));
        miniTransactionModel.setMiniTransactionStateId(MiniTransactionStateConstant.PIN_SENT);
        miniTransactionModel.setMobileNo(appUserModel.getMobileNo());
        searchBaseWrapper.setBasePersistableModel(miniTransactionModel);
        searchBaseWrapper = commonCommandManager.loadMiniTransaction(searchBaseWrapper);
        if(searchBaseWrapper.getCustomList() != null && (searchBaseWrapper.getCustomList().getResultsetList() != null
                && searchBaseWrapper.getCustomList().getResultsetList().size() > 0)){

            miniTransactionModel = (MiniTransactionModel) searchBaseWrapper.getCustomList().getResultsetList().get(0);
            if(!commonCommandManager.getMiniTransactionManager().updateIfExpired(miniTransactionModel, workFlowWrapper.getProductModel().getProductId())) {
                //Expiring old mini transaction rows
                modifyPINSentMiniTransToExpired(miniTransactionModel.getTransactionCodeId());
            }
        }
    }

    private void modifyPINSentMiniTransToExpired(Long trxCodeId) throws FrameworkCheckedException {
        List<MiniTransactionModel> miniTransactionModelList = commonCommandManager.getMiniTransactionDAO().LoadMiniTransactionModelByPK(trxCodeId);
        Iterator<MiniTransactionModel> ite = miniTransactionModelList.iterator() ;
        while( ite.hasNext() ){
            MiniTransactionModel miniTransactionModel  = ite.next() ;
            miniTransactionModel.setMiniTransactionStateId( MiniTransactionStateConstant.EXPIRED ) ;
            miniTransactionModel.setUpdatedOn(new Date());
            miniTransactionModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
            commonCommandManager.getMiniTransactionDAO().saveOrUpdate(miniTransactionModel);
        }
    }
}
