package com.inov8.microbank.debitcard.command;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.cardconfiguration.common.CardConstantsInterface;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.agentsegmentrestrictionmodule.AgentSegmentRestriction;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.debitcard.dao.DebitCardMailingAddressDAO;
import com.inov8.microbank.debitcard.model.DebitCardMailingAddressModel;
import com.inov8.microbank.debitcard.model.DebitCardModel;
import com.inov8.microbank.debitcard.util.DebitCardUtill;
import com.inov8.microbank.debitcard.vo.DebitCardVO;
import com.inov8.microbank.server.service.commandmodule.BaseCommand;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.portal.agentsegmentrestrictionmodule.AgentSegmentRestrictionManager;
import com.inov8.verifly.common.util.RandomNumberGenerator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.viewer.Command;
import org.codehaus.jackson.map.ObjectMapper;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import javax.xml.xpath.XPathConstants;
import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.List;

import static com.inov8.microbank.common.util.XMLConstants.*;

public class DebitCardIssuenceCommand extends BaseCommand {

    protected final Log logger = LogFactory.getLog(DebitCardIssuenceCommand.class);
    private String cnic;
    private String mobileNo;
    private String cardDescription;
    private String mailingAddress;
    private String appId;
    private String fee;
    private String transactionCode;
    private CommonCommandManager commonCommandManager;
    private AppUserModel customerAppUserModel;
    private AppUserModel agentAppUserModel;
    private AppUserModel agentAppUserModel1;
    private String cardTypeId;
    private SmartMoneyAccountModel sma;
    private String transactiontype;
    DocumentBuilderFactory domFactory = null;
    private String agentId;
    private String segmentId;
    private String agentMobileNo;
    private AgentSegmentRestrictionManager agentSegmentRestrictionManager;
    private AgentSegmentRestriction agentSegmentRestriction;
    private DebitCardMailingAddressDAO debitCardMailingAddressDAO;

    @Override
    public void prepare(BaseWrapper baseWrapper) {
        if (this.logger.isDebugEnabled())
            this.logger.debug("Start of DebitCardIssuanceCommand.prepare()");
        cnic = getCommandParameter(baseWrapper, "CNIC");
        mobileNo = getCommandParameter(baseWrapper, "CMOB");
        cardDescription = getCommandParameter(baseWrapper, "CARD_DESCRIPTION");
        mailingAddress = getCommandParameter(baseWrapper, "MAILING_ADDRESS");
        cardTypeId = getCommandParameter(baseWrapper, "CTYPE");
        agentId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AGENT_ID);
        segmentId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_SEGMENT_ID);
        transactiontype = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TRANSACTION_TYPE);
        agentMobileNo=getCommandParameter(baseWrapper,CommandFieldConstants.KEY_AGENT_MOB_NO);
        appId = getCommandParameter(baseWrapper, "APPID");
        if (appId.equals("2") && mobileNo.equals("") && cnic.equals("")) {
            mobileNo = ThreadLocalAppUser.getAppUserModel().getMobileNo();
            cnic = ThreadLocalAppUser.getAppUserModel().getNic();
        } else {

            agentAppUserModel = ThreadLocalAppUser.getAppUserModel();

        }
        fee = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AMOUNT);
        if (this.logger.isDebugEnabled())
            this.logger.debug("End of DebitCardIssuanceCommand.prepare()");
    }

    @Override
    public void doValidate() throws CommandException {
        if (this.logger.isDebugEnabled())
            this.logger.debug("Start of DebitCardIssuanceCommand.doValidate()");

        ValidationErrors validationErrors = new ValidationErrors();
        validationErrors = ValidatorWrapper.doRequired(mobileNo, validationErrors, "MSISDN");
        validationErrors = ValidatorWrapper.doRequired(cnic, validationErrors, "CNIC");
        boolean isValid = CommonUtils.isValidCnic(cnic);
        if (!validationErrors.hasValidationErrors()) {
            if (!isValid) {
                validationErrors.getStringBuilder().append("Invalid CNIC");
                throw new CommandException(validationErrors.getErrors(), ErrorCodes.TERMINATE_EXECUTION_FLOW, ErrorLevel.MEDIUM, new Throwable());
            }
        }
        if (!validationErrors.hasValidationErrors()) {
            if (getCommonCommandManager().isCnicBlacklisted(cnic)) {
                validationErrors.getStringBuilder().append(MessageUtil.getMessage("walkinAccountBlacklisted"));
                throw new CommandException(validationErrors.getErrors(), ErrorCodes.TERMINATE_EXECUTION_FLOW, ErrorLevel.MEDIUM, new Throwable());
            }
        }

        validationErrors = ValidatorWrapper.doRequired(cardDescription, validationErrors, "Card Description");
        if (!validationErrors.hasValidationErrors()) {
            if (!DebitCardUtill.isValidCardDescription(cardDescription)) {
                validationErrors.getStringBuilder().append("Invalid Card Embossing Name");
                throw new CommandException(validationErrors.getErrors(), ErrorCodes.TERMINATE_EXECUTION_FLOW, ErrorLevel.MEDIUM, new Throwable());
            }
        }
        validationErrors = ValidatorWrapper.doRequired(mailingAddress, validationErrors, "Mailing Address");
        if (validationErrors.hasValidationErrors())
            throw new CommandException(validationErrors.getErrors(), ErrorCodes.VALIDATION_ERROR, ErrorLevel.HIGH, new Throwable());

        validationErrors = ValidatorWrapper.doRequired(fee, validationErrors, "Debit Card Issuance Fee");
        /*if(validationErrors.hasValidationErrors())
            throw new CommandException(validationErrors.getErrors(), ErrorCodes.VALIDATION_ERROR, ErrorLevel.HIGH,new Throwable());*/

        if (this.logger.isDebugEnabled())
            this.logger.debug("End of DebitCardIssuanceCommand.doValidate()");
    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        return new ValidationErrors();
    }

    private void executeIssuanceFee(String productId, BaseWrapper baseWrapper) throws CommandException {
        logger.info("Start of executeIssuanceFee() in DebitCardIssuanceCommand.execute() for Customer Mobile # :: " + mobileNo);

//        Double cardFee = 0.0D;
        WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();

        try {
            if (transactiontype.equals("02") && deviceTypeId.equals(DeviceTypeConstantsInterface.WEB_SERVICE.toString())) {
                workFlowWrapper = getCommonCommandManager().calculateDebitCardFeeForAPI(mobileNo, cnic, null, null, null,
                        Long.valueOf(productId),
                        CardConstantsInterface.CARD_FEE_TYPE_RE_ISSUANCE, Long.valueOf(cardTypeId), Long.parseLong(deviceTypeId), null);
            } else if(deviceTypeId.equals(DeviceTypeConstantsInterface.WEB_SERVICE.toString())) {
                workFlowWrapper = getCommonCommandManager().calculateDebitCardFeeForAPI(mobileNo, cnic, null, null, null,
                        Long.valueOf(productId),
                        CardConstantsInterface.CARD_FEE_TYPE_ISSUANCE, Long.valueOf(cardTypeId), Long.parseLong(deviceTypeId), null);
            }
            else{
                workFlowWrapper = getCommonCommandManager().calculateDebitCardFeeForAPI(mobileNo, cnic, null, null, null,
                        Long.valueOf(productId),
                        CardConstantsInterface.CARD_FEE_TYPE_ISSUANCE,Long.valueOf(cardTypeId), Long.parseLong(deviceTypeId), null);
            }

            if(workFlowWrapper.getCardFeeRuleModel() != null && workFlowWrapper.getCardFeeRuleModel().getIsInstallments()) {
                baseWrapper.putObject("cardFeeRuleModel", workFlowWrapper.getCardFeeRuleModel());
                baseWrapper.putObject("noOfInstallments", workFlowWrapper.getCardFeeRuleModel().getNoOfInstallments());
                baseWrapper.putObject("isInstallments", workFlowWrapper.getCardFeeRuleModel().getIsInstallments());
            }
        }
        catch (CommandException ce) {
            throw new CommandException(ce.getMessage(), ce.getErrorCode(), ErrorLevel.MEDIUM, new Throwable());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        //validateCharges();
        fee = String.valueOf(workFlowWrapper.getCommissionAmountsHolder().getTransactionAmount());

        BaseWrapper dWrapper = new BaseWrapperImpl();
        dWrapper.putObject(CommandFieldConstants.KEY_MOB_NO, mobileNo);
        dWrapper.putObject(CommandFieldConstants.KEY_AMOUNT, fee);
        String response = null;
        dWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, productId);
        dWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, deviceTypeId);
        dWrapper.putObject(CommandFieldConstants.KEY_TXAM, fee);
        dWrapper.putObject(CommandFieldConstants.KEY_TPAM, "0");
        dWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, mobileNo);
        dWrapper.putObject(CommandFieldConstants.KEY_TAMT, fee);
        dWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, "MOBILE");
        dWrapper.putObject(CommandFieldConstants.KEY_STAN, "");
        dWrapper.putObject(CommandFieldConstants.KEY_PAYMENT_MODE, "");
        dWrapper.putObject(CommandFieldConstants.KEY_APP_ID, appId);
        dWrapper.putObject("IS_CHARGES_REQUEST", "1");
        if (fee != null && !fee.equals("0.0"))
            response = getCommandManager().executeCommand(dWrapper, CommandFieldConstants.CMD_DEBIT_CARD_CW);
        logger.info("End of executeIssuanceFee() in DebitCardIssuanceCommand.execute() \nResponse :: " + response);
        if(workFlowWrapper.getCommissionAmountsHolder() != null) {
            fee = String.valueOf(workFlowWrapper.getCommissionAmountsHolder().getTransactionAmount());
            if (workFlowWrapper.getCommissionAmountsHolder().getExclusivePercentAmount() > 0.0 || workFlowWrapper.getCommissionAmountsHolder().getExclusiveFixAmount() > 0.0) {
                fee = String.valueOf(Double.valueOf(fee) + workFlowWrapper.getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.FED_STAKE_HOLDER_ID));
            }
            else{
                fee = String.valueOf(workFlowWrapper.getCommissionAmountsHolder().getTransactionAmount());
            }
        }
        else{
            fee = "0.0";
        }
        if (response != null)
            this.populateXMLParams(response);
        if (!appId.equals("2") && !mobileNo.equals("") && !cnic.equals("")) {
            ThreadLocalAppUser.setAppUserModel(agentAppUserModel);
        }
    }

    private NodeList executeXPathQuery(String xml, String xpathExpression) {
        Object result = null;
        try {
            domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setNamespaceAware(true); // never forget this!
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xml)));
            XPathFactory factory = XPathFactory.newInstance();
            XPath xpath = factory.newXPath();
            XPathExpression expr = xpath.compile(xpathExpression);
            result = expr.evaluate(doc, XPathConstants.NODESET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (NodeList) result;
    }

    public void populateXMLParams(String xml) {
        logger.info("populateProductPurchase(...) in DebitCardIssuanceCommand " + xml);
        NodeList nodeList = this.executeXPathQuery(xml, "//trans/*");
        for (int i = 0; i < nodeList.getLength(); i++) {
            transactionCode = nodeList.item(0).getAttributes().getNamedItem(CommandFieldConstants.KEY_TX_ID).getNodeValue();
            /*NodeList childNodeList = nodeList.item(i).getChildNodes();
            for (int j = 0; j < childNodeList.getLength(); j++) {
                NodeList nList = childNodeList.item(j).getChildNodes();
                if (nList != null && nList.getLength() == 0) {
                    NamedNodeMap namedNodeMap = childNodeList.item(0).getAttributes();
                    if (namedNodeMap != null) {
                        if (namedNodeMap.getNamedItem("ID") != null && !namedNodeMap.getNamedItem("ID").getNodeValue().equals(""))
                            transactionCode = namedNodeMap.getNamedItem("ID").getNodeValue();
                    }
                }
            }*/
        }
    }

    @Override
    public void execute() throws CommandException {
        if (this.logger.isDebugEnabled())
            this.logger.debug("Start of DebitCardIssuanceCommand.execute()");
        commonCommandManager = this.getCommonCommandManager();
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        Boolean result = null;

        List<DebitCardModel> list = null;
        try {
            list = commonCommandManager.getDebitCardModelDao().getDebitCardModelByMobileAndNIC(mobileNo, cnic);
        } catch (FrameworkCheckedException e) {
            e.printStackTrace();
        }
        if (list != null && !list.isEmpty()) {
            DebitCardModel debitCardModel = list.get(0);

            if (debitCardModel.getCardStatusId() != null
                    && (debitCardModel.getCardStatusId().equals(CardConstantsInterface.CARD_STATUS_APPROVED) || debitCardModel.getCardStatusId().equals(CardConstantsInterface.CARD_STATUS_ACTIVE)) && deviceTypeId.equals(DeviceTypeConstantsInterface.WEB_SERVICE.toString()) && transactiontype.equals("02")) {
                if (debitCardModel.getReIssuanceStatus() != null
                        && (debitCardModel.getReIssuanceStatus().equals(CardConstantsInterface.CARD_STATUS_IN_PROCESS)
                        || debitCardModel.getReIssuanceStatus().equals(CardConstantsInterface.CARD_STATUS_APPROVED)
                        || debitCardModel.getReIssuanceStatus().equals(CardConstantsInterface.CARD_STATUS_PENDING)
                        || debitCardModel.getReIssuanceStatus().equals(CardConstantsInterface.CARD_STATUS_INTITATED))) {
                    throw new CommandException(MessageUtil.getMessage("debit.card.req.in.process"),
                            ErrorCodes.INVALID_INPUT, ErrorLevel.MEDIUM, new Throwable());
                } else {
                    logger.info("*** Customer Validation Successful for Debit Card Re-Issuance ***");
                }

            } else if (debitCardModel.getCardStatusId() != null && !debitCardModel.getCardStatusId().equals(CardConstantsInterface.CARD_STATUS_REJECTED)
                    && !debitCardModel.getCardStatusId().equals(CardConstantsInterface.CARD_STATUS_IN_PROCESS)
                    && !debitCardModel.getCardStatusId().equals(CardConstantsInterface.CARD_STATUS_APPROVED)
                    && !debitCardModel.getCardStatusId().equals(CardConstantsInterface.CARD_STATUS_PENDING)
                    && !debitCardModel.getCardStatusId().equals(CardConstantsInterface.CARD_STATUS_INTITATED)) {
                throw new CommandException(MessageUtil.getMessage("debit.card.already.linked"), ErrorCodes.INVALID_INPUT, ErrorLevel.MEDIUM, new Throwable());
            } else if (debitCardModel.getCardStatusId() != null
                    && (debitCardModel.getCardStatusId().equals(CardConstantsInterface.CARD_STATUS_IN_PROCESS)
                    || debitCardModel.getCardStatusId().equals(CardConstantsInterface.CARD_STATUS_APPROVED)
                    && !debitCardModel.getCardStatusId().equals(CardConstantsInterface.CARD_STATUS_PENDING)
                    || debitCardModel.getCardStatusId().equals(CardConstantsInterface.CARD_STATUS_INTITATED))) {
                throw new CommandException(MessageUtil.getMessage("debit.card.req.in.process"),
                        ErrorCodes.INVALID_INPUT, ErrorLevel.MEDIUM, new Throwable());
            }
        }
        if (!(appId.equals("2")) && !deviceTypeId.equals(DeviceTypeConstantsInterface.WEB_SERVICE.toString())) {
            this.agentSegmentRestrictionManager = commonCommandManager.getAgentSegmentRestriction();
            String productId1 = ProductConstantsInterface.DEBIT_CARD_ISSUANCE.toString();
            agentSegmentRestriction = new AgentSegmentRestriction();
            agentSegmentRestriction.setAgentID(agentId);
            agentSegmentRestriction.setSegmentId(Long.valueOf(segmentId));
            agentSegmentRestriction.setProductId(Long.valueOf(productId1));
            agentSegmentRestriction.setIsActive(true);
            BaseWrapperImpl baseWrapper1 = new BaseWrapperImpl();
            baseWrapper1.setBasePersistableModel(agentSegmentRestriction);
            try {
                result = agentSegmentRestrictionManager.checkAgentSegmentRestriction(baseWrapper1);
            } catch (FrameworkCheckedException e) {
                e.printStackTrace();
            }
            if (result.equals(false)) {

                throw new CommandException(MessageUtil.getMessage("debit.card.req.in.segment.restrict"), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());


            } else {
                Long[] appUserTypes = new Long[]{UserTypeConstantsInterface.CUSTOMER};
                try {
                    customerAppUserModel = this.commonCommandManager.loadAppUserByMobileAndType(mobileNo, appUserTypes);
                    baseWrapper.putObject(CommandFieldConstants.KEY_APP_USER_MODEL, customerAppUserModel);
//                    if(customerAppUserModel.getRegistrationStateId().equals(RegistrationStateConstants.CLSPENDING)) {
//                        AccountOpeningPendingSafRepoModel model = new AccountOpeningPendingSafRepoModel();
//                        model.setMobileNo(customerAppUserModel.getMobileNo());
//                        model = this.getCommonCommandManager().getAccountOpeningPendingSafRepoModel(model);
//
//
//                        DebitCardPendingSafRepo model1 = new DebitCardPendingSafRepo();
//                        model1.setMobileNo(mobileNo);
//                        model1.setCnic(cnic);
//                        model1.setProductId(Long.valueOf(productId1));
//                        if(model.getClsResponseCode().equals("True Match-Compliance") || model.getClsResponseCode().equals("Revert to Branch")) {
//                            model1.setIsCompleted("1");
//                        }
//                        else{
//                            model1.setIsCompleted("0");
//                        }
//                        model1 = this.getCommonCommandManager().loadExistingDebitCardSafRepo(model1);
//
//                        if(model1 != null){
//                            throw new CommandException("Request Already Exist With Mobile Number in Pending State" , ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
//                        }
//
//                        DebitCardPendingSafRepo debitCardPendingSafRepo = new DebitCardPendingSafRepo();
//                        debitCardPendingSafRepo.setMobileNo(customerAppUserModel.getMobileNo());
//                        debitCardPendingSafRepo.setCnic(customerAppUserModel.getNic());
//                        debitCardPendingSafRepo.setRegistrationStateId(customerAppUserModel.getRegistrationStateId());
//                        debitCardPendingSafRepo.setProductId(ProductConstantsInterface.DEBIT_CARD_ISSUANCE);
//                        debitCardPendingSafRepo.setAppId(appId);
//                        debitCardPendingSafRepo.setEmail(mailingAddress);
//                        debitCardPendingSafRepo.setCardDescription(cardDescription);
//                        debitCardPendingSafRepo.setDebitCardRegectionReason("Card Request is in Pending State");
//                        debitCardPendingSafRepo.setCreatedBy(ThreadLocalAppUser.getAppUserModel().getCreatedBy());
//                        debitCardPendingSafRepo.setUpdatedBy(2L);
//                        debitCardPendingSafRepo.setCreatedOn(new Date());
//                        debitCardPendingSafRepo.setUpdatedOn(new Date());
//                        if(model.getClsResponseCode().equals("True Match-Compliance") || model.getClsResponseCode().equals("Revert to Branch")) {
//                            debitCardPendingSafRepo.setIsCompleted(String.valueOf(1));
//                        }
//                        else {
//                            debitCardPendingSafRepo.setIsCompleted(String.valueOf(0));
//                        }
//                        debitCardPendingSafRepo.setCardTypeId(cardTypeId);
//                        debitCardPendingSafRepo.setAgentId(agentId);
//                        debitCardPendingSafRepo.setSegmentId(segmentId);
//                        debitCardPendingSafRepo.setDeviceTypeId(deviceTypeId);
//                        debitCardPendingSafRepo = commonCommandManager.debitCardPendingSafRepo(debitCardPendingSafRepo);
//                        if (debitCardPendingSafRepo == null) {
//                            logger.info("Exception occured on saving data in debitCardPendingSafRepo :");
//                        }
//
//                        throw new CommandException("Your Debit Card Request is in Pending State", ErrorCodes.DEBIT_CARD_CLS_PENDING_STATE, ErrorLevel.MEDIUM, new Throwable());
//                    }

                } catch (FrameworkCheckedException e) {
                    e.printStackTrace();
//                    if(e.getErrorCode() == 9046L){
//                        throw new CommandException("Your Debit Card Request is in Pending State", ErrorCodes.DEBIT_CARD_CLS_PENDING_STATE, ErrorLevel.MEDIUM, new Throwable());
//                    }
//                    else if(e.getMessage().equals("Request Already Exist With Mobile Number in Pending State")){
//                        throw new CommandException("Request Already Exist With Mobile Number in Pending State" , ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
//                    }
//                    else {
//                        throw new CommandException("Request Already Exist With Mobile Number in Pending State", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
//                    }
                }
                sma = commonCommandManager.getSmartMoneyAccountByAppUserModelAndPaymentModId(customerAppUserModel, PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
                if (sma == null)
                    throw new CommandException("Could not load Smart Money Account.", ErrorCodes.INVALID_USER_ACCOUNT, ErrorLevel.MEDIUM, new Throwable());
                else if (sma.getStatusId() != null && sma.getStatusId().equals(OlaStatusConstants.ACCOUNT_STATUS_BLOCKED))
                    throw new CommandException("Your Account is Blocked.", ErrorCodes.INVALID_USER_ACCOUNT, ErrorLevel.MEDIUM, new Throwable());
                else if (sma.getStatusId() != null && sma.getStatusId().equals(OlaStatusConstants.ACCOUNT_STATUS_IN_ACTIVE))
                    throw new CommandException("Your Account is In-Active.", ErrorCodes.INVALID_USER_ACCOUNT, ErrorLevel.MEDIUM, new Throwable());

                if (appId.isEmpty() || !(appId.equals("2"))) {
                    sma.setCardProdId(Long.valueOf(cardTypeId));

                    try {
                        commonCommandManager.updateSmartMoneyAccountCardType(sma);
                    } catch (FrameworkCheckedException e) {
                        e.printStackTrace();
                    }
                }


                String productId = null;
                if (!appId.equals("2") && !deviceTypeId.equals(DeviceTypeConstantsInterface.WEB_SERVICE.toString())) {
                    productId = ProductConstantsInterface.DEBIT_CARD_ISSUANCE.toString();
                    RetailerContactModel retailerContactModel = null;
//                    if(ThreadLocalAppUser.getAppUserModel() == null){
//                        try {
//                            agentAppUserModel1=  this.commonCommandManager.loadAppUserByMobileAndType(agentMobileNo,UserTypeConstantsInterface.RETAILER);
//                        } catch (FrameworkCheckedException e) {
//                            e.printStackTrace();
//                        }
//                        ThreadLocalAppUser.setAppUserModel(agentAppUserModel1);
//                    }
                    BaseWrapper retailerWrapper = new BaseWrapperImpl();
                    RetailerContactModel model = new RetailerContactModel();
                    model.setPrimaryKey(ThreadLocalAppUser.getAppUserModel().getRetailerContactId());
                    retailerWrapper.setBasePersistableModel(model);
                    try {
                        retailerWrapper = commonCommandManager.loadRetailerContact(retailerWrapper);
                        retailerContactModel = (RetailerContactModel) retailerWrapper.getBasePersistableModel();
                    } catch (FrameworkCheckedException e) {
                        e.printStackTrace();
                    }
                    if (retailerContactModel.getIsDebitCardFeeEnabled() != null && retailerContactModel.getIsDebitCardFeeEnabled()) {
                        executeIssuanceFee(productId, baseWrapper);
                    }
                } else {
                    if (transactiontype.equals("02") && deviceTypeId.equals(DeviceTypeConstantsInterface.WEB_SERVICE.toString())) {
                        productId = ProductConstantsInterface.DEBIT_CARD_RE_ISSUANCE.toString();
                        executeIssuanceFee(productId, baseWrapper);
                    } else {
                        productId = ProductConstantsInterface.CUSTOMER_DEBIT_CARD_ISSUANCE.toString();
                        executeIssuanceFee(productId, baseWrapper);
                    }
                }
                try {
                    CustomerModel customerModel = commonCommandManager.getCustomerModelById(customerAppUserModel.getCustomerId());
                    Long segmentId = customerModel.getSegmentId();
//                    baseWrapper = this.preparePopulateAuthenticationParamForDebitCardRequest(baseWrapper);
                    DebitCardVO debitCardVO = this.prepareDebitCardIssuanceVO();
                    baseWrapper.setBasePersistableModel(debitCardVO);
                    baseWrapper.putObject("segmentId", segmentId);
                    baseWrapper.putObject("productId", productId);
                    baseWrapper.putObject(CommandFieldConstants.KEY_TRANSACTION_TYPE, transactiontype);
                    baseWrapper = commonCommandManager.saveOrUpdateDebitCardIssuenceRequest(baseWrapper);
                } catch (FrameworkCheckedException e) {
                    e.printStackTrace();
                    throw new CommandException(MessageUtil.getMessage("debit.card.req.in.process"), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                }
                String msgToText = MessageUtil.getMessage("debit.card.req.submitted");
                BaseWrapper msgBaseWrapper = new BaseWrapperImpl();
                msgBaseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, new SmsMessage(mobileNo, msgToText));
                try {
                    commonCommandManager.sendSMSToUser(msgBaseWrapper);
                } catch (FrameworkCheckedException e) {
                }
            }

        } else {
            Long[] appUserTypes = new Long[]{UserTypeConstantsInterface.CUSTOMER};
            try {
                customerAppUserModel = this.commonCommandManager.loadAppUserByMobileAndType(mobileNo, appUserTypes);
                baseWrapper.putObject(CommandFieldConstants.KEY_APP_USER_MODEL, customerAppUserModel);
//                logger.info("Check for CLS Pending State");
//                if(customerAppUserModel.getRegistrationStateId().equals(RegistrationStateConstants.CLSPENDING)){
//                    DebitCardPendingSafRepo debitCardPendingSafRepo = new DebitCardPendingSafRepo();
//                    debitCardPendingSafRepo.setMobileNo(customerAppUserModel.getMobileNo());
//                    debitCardPendingSafRepo.setCnic(customerAppUserModel.getNic());
//                    debitCardPendingSafRepo.setRegistrationStateId(customerAppUserModel.getRegistrationStateId());
//                    debitCardPendingSafRepo.setProductId(ProductConstantsInterface.DEBIT_CARD_ISSUANCE);
//                    debitCardPendingSafRepo.setAppId(appId);
//                    debitCardPendingSafRepo.setEmail(mailingAddress);
//                    debitCardPendingSafRepo.setCardDescription(cardDescription);
//                    debitCardPendingSafRepo.setDebitCardRegectionReason("Card Request is in Pending State");
//                    debitCardPendingSafRepo.setCreatedBy(ThreadLocalAppUser.getAppUserModel().getCreatedBy());
//                    debitCardPendingSafRepo.setUpdatedBy(2L);
//                    debitCardPendingSafRepo.setCreatedOn(new Date());
//                    debitCardPendingSafRepo.setUpdatedOn(new Date());
//                    debitCardPendingSafRepo.setIsCompleted(String.valueOf(0));
//                    debitCardPendingSafRepo.setCardTypeId(cardTypeId);
//                    debitCardPendingSafRepo.setAgentId(agentId);
//                    debitCardPendingSafRepo.setSegmentId(segmentId);
//                    debitCardPendingSafRepo.setDeviceTypeId(deviceTypeId);
//                    debitCardPendingSafRepo = commonCommandManager.debitCardPendingSafRepo(debitCardPendingSafRepo);
//                    if(debitCardPendingSafRepo == null){
//                        logger.info("Exception occured on saving data in debitCardPendingSafRepo :");
//                    }
//                    throw new CommandException("Your Debit Card Request is in Pending State", ErrorCodes.DEBIT_CARD_CLS_PENDING_STATE, ErrorLevel.MEDIUM, new Throwable());
//
//                }
            } catch (FrameworkCheckedException e) {
//                if(e.getErrorCode() == 9046L){
//                    throw new CommandException("Your Debit Card Request is in Pending State", ErrorCodes.DEBIT_CARD_CLS_PENDING_STATE, ErrorLevel.MEDIUM, new Throwable());
//                }
                e.printStackTrace();
            }
            sma = commonCommandManager.getSmartMoneyAccountByAppUserModelAndPaymentModId(customerAppUserModel, PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
            if (sma == null)
                throw new CommandException("Could not load Smart Money Account.", ErrorCodes.INVALID_USER_ACCOUNT, ErrorLevel.MEDIUM, new Throwable());
            else if (sma.getStatusId() != null && sma.getStatusId().equals(OlaStatusConstants.ACCOUNT_STATUS_BLOCKED))
                throw new CommandException("Your Account is Blocked.", ErrorCodes.INVALID_USER_ACCOUNT, ErrorLevel.MEDIUM, new Throwable());
            else if (sma.getStatusId() != null && sma.getStatusId().equals(OlaStatusConstants.ACCOUNT_STATUS_IN_ACTIVE))
                throw new CommandException("Your Account is In-Active.", ErrorCodes.INVALID_USER_ACCOUNT, ErrorLevel.MEDIUM, new Throwable());

            if (appId.isEmpty() || !(appId.equals("2"))) {
                sma.setCardProdId(Long.valueOf(cardTypeId));

                try {
                    commonCommandManager.updateSmartMoneyAccountCardType(sma);
                } catch (FrameworkCheckedException e) {
                    e.printStackTrace();
                }
            }


            String productId = null;
            if (!appId.equals("2") && !deviceTypeId.equals(DeviceTypeConstantsInterface.WEB_SERVICE.toString())) {
                productId = ProductConstantsInterface.DEBIT_CARD_ISSUANCE.toString();
                RetailerContactModel retailerContactModel = null;
                BaseWrapper retailerWrapper = new BaseWrapperImpl();
                RetailerContactModel model = new RetailerContactModel();
                model.setPrimaryKey(ThreadLocalAppUser.getAppUserModel().getRetailerContactId());
                retailerWrapper.setBasePersistableModel(model);
                try {
                    retailerWrapper = commonCommandManager.loadRetailerContact(retailerWrapper);
                    retailerContactModel = (RetailerContactModel) retailerWrapper.getBasePersistableModel();
                } catch (FrameworkCheckedException e) {
                    e.printStackTrace();
                }
                if (retailerContactModel.getIsDebitCardFeeEnabled() != null && retailerContactModel.getIsDebitCardFeeEnabled()) {
                    executeIssuanceFee(productId, baseWrapper);
                }
            }
            else if(appId.equals("1")){
                productId = ProductConstantsInterface.CUSTOMER_DEBIT_CARD_ISSUANCE.toString();
                executeIssuanceFee(productId, baseWrapper);
            }
            else {
                if (transactiontype.equals("02") && deviceTypeId.equals(DeviceTypeConstantsInterface.WEB_SERVICE.toString())) {
                    productId = ProductConstantsInterface.DEBIT_CARD_RE_ISSUANCE.toString();
                    executeIssuanceFee(productId, baseWrapper);
                } else {
                    productId = ProductConstantsInterface.CUSTOMER_DEBIT_CARD_ISSUANCE.toString();
                    executeIssuanceFee(productId, baseWrapper);
                }
            }
            try {
                CustomerModel customerModel = commonCommandManager.getCustomerModelById(customerAppUserModel.getCustomerId());
                Long segmentId = customerModel.getSegmentId();
                DebitCardVO debitCardVO = this.prepareDebitCardIssuanceVO();
                baseWrapper.setBasePersistableModel(debitCardVO);
                baseWrapper.putObject("segmentId", segmentId);
                baseWrapper.putObject("productId", productId);
//                String cardNo = debitCardVO.getCardNo();
                baseWrapper.putObject("debitCardVo", debitCardVO);
                baseWrapper.putObject(CommandFieldConstants.KEY_TRANSACTION_TYPE, transactiontype);
                baseWrapper = commonCommandManager.saveOrUpdateDebitCardIssuenceRequest(baseWrapper);
            } catch (FrameworkCheckedException e) {
                e.printStackTrace();
                throw new CommandException(MessageUtil.getMessage("debit.card.req.in.process"), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
            }
            String msgToText = MessageUtil.getMessage("debit.card.req.submitted");
            BaseWrapper msgBaseWrapper = new BaseWrapperImpl();
            msgBaseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, new SmsMessage(mobileNo, msgToText));
            try {
                commonCommandManager.sendSMSToUser(msgBaseWrapper);
            } catch (FrameworkCheckedException e) {
            }
        }
    }

    private BaseWrapper preparePopulateAuthenticationParamForDebitCardRequest(BaseWrapper baseWrapper) {
        DebitCardVO debitCardVO = this.prepareDebitCardIssuanceVO();
        ObjectMapper mapper = new ObjectMapper();
        String modelJsonString = null;
        try {
            modelJsonString = mapper.writeValueAsString(debitCardVO);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Long actionAuthorizationId = null;
        String initialModelJsonString = null;
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_OLD_MODEL_STRING, initialModelJsonString);
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_METHODE_NAME, "saveOrUpdateDebitCardIssuenceRequestWithAuthorization");
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS, DebitCardVO.class.getSimpleName());
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS_QUALIFIED_NAME, DebitCardVO.class.getName());
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_STRING, modelJsonString);
        baseWrapper.putObject("managerName", "commonCommandManager");
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_REQ_REF_ID, customerAppUserModel.getAppUserId().toString());
        baseWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID, actionAuthorizationId);
        if (transactiontype.equals("02") && deviceTypeId.equals(DeviceTypeConstantsInterface.WEB_SERVICE.toString())) {
            baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.KEY_DEBIT_CARD_REISSUENCE_USECASE_ID);
        } else {
            baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.KEY_DEBIT_CARD_ISSUENCE_USECASE_ID);
        }
        baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
        baseWrapper.putObject(CommandFieldConstants.KEY_TRANSACTION_ID, transactionCode);
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_FORM_NAME, "debit-card/searchDebitCardRequests");

        return baseWrapper;
    }

    private DebitCardVO prepareDebitCardIssuanceVO() {
        DebitCardVO debitCardVO = new DebitCardVO();

        if (transactiontype.equals("02")){
            List<DebitCardModel> list = null;
            try {
                list = commonCommandManager.getDebitCardModelDao().getDebitCardModelByMobileAndNIC(mobileNo, cnic);
            } catch (FrameworkCheckedException e) {
                e.printStackTrace();
            }
            if (list != null && !list.isEmpty()) {
                DebitCardModel debitCardModel = list.get(0);

                if(debitCardModel.getReissuance() != null && debitCardModel.getReissuance().equals("1")){
                    if((debitCardModel.getIsReIssuanceApproved() != null && debitCardModel.getIsReIssuanceApproved().equals("1")) ||
                            (debitCardModel.getIsReIssuanceApprovedDenied() != null && debitCardModel.getIsReIssuanceApprovedDenied().equals("1"))){
                        debitCardModel.setIsReIssuanceApproved("0");
                        debitCardModel.setIsReIssuanceApprovedDenied("0");
                        commonCommandManager.getDebitCardModelDao().saveOrUpdate(debitCardModel);
                    }
                }

                debitCardVO.setMobileNo(mobileNo);
                debitCardVO.setcNic(cnic);
                debitCardVO.setCustomerAppUserId(customerAppUserModel.getAppUserId());
                debitCardVO.setCardNo(debitCardModel.getCardNo());
                debitCardVO.setDebitCardEmbosingName(cardDescription);
                debitCardVO.setCardStatusId(debitCardModel.getCardStatusId());
                debitCardVO.setMailingAddress(mailingAddress);
                debitCardVO.setCreatedByAppUserId(UserUtils.getCurrentUser().getAppUserId());
                debitCardVO.setCreatedOn(new Date());
                debitCardVO.setAppId(appId);
                debitCardVO.setSmartMoneyAccountId(sma.getSmartMoneyAccountId());
                debitCardVO.setCustomerNadraName(customerAppUserModel.getFirstName() + " " + customerAppUserModel.getLastName());
                debitCardVO.setTransactionCode(transactionCode);
                debitCardVO.setFee(fee);
            }

        }else {
            debitCardVO.setMobileNo(mobileNo);
            debitCardVO.setcNic(cnic);
            debitCardVO.setCustomerAppUserId(customerAppUserModel.getAppUserId());
            debitCardVO.setCardNo(RandomNumberGenerator.generatePin(16));
            debitCardVO.setDebitCardEmbosingName(cardDescription);
            debitCardVO.setCardStatusId(CardConstantsInterface.CARD_STATUS_PENDING);
            debitCardVO.setMailingAddress(mailingAddress);
            debitCardVO.setCreatedByAppUserId(UserUtils.getCurrentUser().getAppUserId());
            debitCardVO.setCreatedOn(new Date());
            debitCardVO.setAppId(appId);
            debitCardVO.setSmartMoneyAccountId(sma.getSmartMoneyAccountId());
            debitCardVO.setCustomerNadraName(customerAppUserModel.getFirstName() + " " + customerAppUserModel.getLastName());
            debitCardVO.setTransactionCode(transactionCode);
            debitCardVO.setFee(fee);
        }
        return debitCardVO;
    }

    @Override
    public String response() {
        if (this.logger.isDebugEnabled())
            this.logger.debug("Start of DebitCardIssuanceCommand.response()");
        StringBuilder strBuilder = new StringBuilder();

        strBuilder.append(XMLConstants.TAG_SYMBOL_OPEN)
                .append(XMLConstants.TAG_MESGS)
                .append(XMLConstants.TAG_SYMBOL_CLOSE);

        strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_MESG)
                .append(TAG_SYMBOL_SPACE).append(ATTR_LEVEL)
                .append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                .append(ATTR_LEVEL_ONE)
                .append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
                .append(MessageUtil.getMessage("debit.card.req.submitted")).append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH).append(TAG_MESG)
                .append(TAG_SYMBOL_CLOSE);

        strBuilder.append(XMLConstants.TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH)
                .append(XMLConstants.TAG_MESGS)
                .append(XMLConstants.TAG_SYMBOL_CLOSE);
        if (this.logger.isDebugEnabled())
            this.logger.debug("End of DebitCardIssuanceCommand.response()");

        return strBuilder.toString();
    }

    public AgentSegmentRestrictionManager getAgentSegmentRestrictionManager() {
        return agentSegmentRestrictionManager;
    }

    public void setAgentSegmentRestrictionManager(AgentSegmentRestrictionManager agentSegmentRestrictionManager) {
        this.agentSegmentRestrictionManager = agentSegmentRestrictionManager;
    }

}
