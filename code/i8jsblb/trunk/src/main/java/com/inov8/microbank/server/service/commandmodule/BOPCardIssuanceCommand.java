package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.viewer.Command;

import java.util.ArrayList;
import java.util.List;

public class BOPCardIssuanceCommand extends BaseCommand {

    private final Log logger = LogFactory.getLog(BOPCardIssuanceCommand.class);

    private String cMsisdn, cNic,cardNo;
    private String segmentId,productId;
    private String fingerIndex,templateType,fingerTemplate,oneTimePin,latitude,longitude,terminalId;

    private AppUserModel appUserModel;
    private UserDeviceAccountsModel userDeviceAccountsModel;
    private ProductModel productModel;
    private RetailerContactModel retailerContactModel;
    private TransactionModel transactionModel;

    @Override
    public void prepare(BaseWrapper baseWrapper) {
        fingerIndex = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_FINGER_INDEX);
        templateType = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TEMPLATE_TYPE);
        fingerTemplate = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_FINGER_TEMPLATE);
        cardNo = getCommandParameter(baseWrapper,CommandFieldConstants.KEY_DEBIT_CARD_NO);
        productId = this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_PROD_ID);
        latitude = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_LATITUDE);
        longitude = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_LONGITUDE);
        terminalId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TERMINAL_ID);
        this.cMsisdn = getCommandParameter(baseWrapper, "CMOB");
        this.cNic = getCommandParameter(baseWrapper, "CNIC");
        this.segmentId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_THIRD_PARTY_CUST_SEGMENT_CODE);
        this.deviceTypeId = getCommandParameter(baseWrapper, "DTID");
        appUserModel = ThreadLocalAppUser.getAppUserModel();
        userDeviceAccountsModel = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();

        try{
            BaseWrapper bWrapper = new BaseWrapperImpl();
            //ProductModel
            productModel = new ProductModel();
            productModel.setProductId(Long.parseLong(productId));
            bWrapper.setBasePersistableModel(productModel);
            bWrapper = getCommonCommandManager().loadProduct(bWrapper);
            productModel = (ProductModel) bWrapper.getBasePersistableModel();

            RetailerContactModel retContactModel = new RetailerContactModel();
            retContactModel.setRetailerContactId(appUserModel.getRetailerContactId());

            bWrapper = new BaseWrapperImpl();
            bWrapper.setBasePersistableModel(retContactModel);
            CommonCommandManager commonCommandManager = this.getCommonCommandManager();
            bWrapper = commonCommandManager.loadRetailerContact(bWrapper);

            retailerContactModel = (RetailerContactModel) bWrapper.getBasePersistableModel();
        }

        catch(Exception ex){
            ex.printStackTrace();
            logger.error(" Product model not found: ");
        }
    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        validationErrors = ValidatorWrapper.doRequired(this.cMsisdn,validationErrors, "Customer Mobile No");
        validationErrors = ValidatorWrapper.doRequired(this.cNic,validationErrors, "CNIC");
        validationErrors = ValidatorWrapper.doRequired(this.deviceTypeId,validationErrors, "Device Type");
        ValidatorWrapper.doRequired(this.segmentId, validationErrors, CommandFieldConstants.KEY_THIRD_PARTY_CUST_SEGMENT_CODE);
        if(validationErrors.hasValidationErrors())
        {
            throw new CommandException(validationErrors.getErrors(),ErrorCodes.VALIDATION_ERROR,ErrorLevel.HIGH,new Throwable());
        }
        return validationErrors;
    }

    @Override
    public void execute() throws CommandException {
        String transactionAmount = "0.0";
        WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
        String classNMethodName = "[BOPCardIssuanceCommand.execute] ";
        String exceptionMessage = "Exception occurred ";
        String inputParams = "AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Product ID: " +  ProductConstantsInterface.BOP_CARD_ISSUANCE_REISSUANCE
                + " Customer Mobile #:" + cMsisdn + " Customer Card #: " + cardNo ;
        String genericExceptionMessage = classNMethodName + exceptionMessage + inputParams;
        logger.info(classNMethodName + "\n" + inputParams);
        try {
            workFlowWrapper.setTransactionAmount(Double.parseDouble(transactionAmount));
            workFlowWrapper.setHandlerModel(handlerModel);
            workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);
            // check product limit
            getCommonCommandManager().checkProductLimit(null, productModel.getProductId(), appUserModel.getMobileNo(),
                    Long.valueOf(deviceTypeId), Double.parseDouble(transactionAmount), productModel, null, workFlowWrapper.getHandlerModel());

            String userId = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId();

            // Velocity validation
            BaseWrapper vWrapper = new BaseWrapperImpl();
            vWrapper.putObject(CommandConstants.VELOCITY_PRODUCT_ID, Long.parseLong(productId));
            vWrapper.putObject(CommandConstants.VELOCITY_DEVICE_TYPE_ID, Long.parseLong(deviceTypeId));
            vWrapper.putObject(CommandConstants.VELOCITY_DISTRIBUTOR_ID, retailerContactModel.getRetailerIdRetailerModel().getDistributorId());
            vWrapper.putObject(CommandConstants.VELOCITY_AGENT_TYPE, retailerContactModel.getDistributorLevelId());
            vWrapper.putObject(CommandConstants.VELOCITY_TRANSACTION_AMOUNT, Double.valueOf(transactionAmount));
            vWrapper.putObject(CommandConstants.VELOCITY_SEGMENT_ID, CommissionConstantsInterface.DEFAULT_SEGMENT_ID);
            vWrapper.putObject(CommandConstants.VELOCITY_ACCOUNT_TYPE, retailerContactModel.getOlaCustomerAccountTypeModel().getCustomerAccountTypeId());
            vWrapper.putObject(CommandConstants.VELOCITY_CUSTOMER_ID, userId);
//            vWrapper.putObject(CommandConstants.VELOCITY_RETAILER_ID, retailerId);
            getCommonCommandManager().checkVelocityCondition(vWrapper);

            workFlowWrapper.setProductModel(productModel);
            workFlowWrapper.setAppUserModel(appUserModel);
            SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
            SmartMoneyAccountModel agentSMAModel = new SmartMoneyAccountModel();
            agentSMAModel.setRetailerContactId(appUserModel.getRetailerContactId());
            agentSMAModel.setActive(Boolean.TRUE);
            searchBaseWrapper.setBasePersistableModel(agentSMAModel);
            searchBaseWrapper = getCommonCommandManager().loadSmartMoneyAccount(searchBaseWrapper);
            CustomList smaList = searchBaseWrapper.getCustomList();
            if (smaList != null && smaList.getResultsetList() != null && smaList.getResultsetList().size() > 0) {
                agentSMAModel = (SmartMoneyAccountModel) smaList.getResultsetList().get(0);
            }
            workFlowWrapper.setSmartMoneyAccountModel(agentSMAModel);
            AccountInfoModel accountInfoModel = commonCommandManager.getAccountInfoModel(appUserModel.getAppUserId(), agentSMAModel.getName());
            workFlowWrapper.setAccountInfoModel(accountInfoModel);
            TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
            transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.BOP_CARD_ISSUANCE_REISSUANCE_TX);
            workFlowWrapper.setRetailerContactModel(retailerContactModel);
            workFlowWrapper.setFromRetailerContactModel(retailerContactModel);
            DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
            deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));
            workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
            workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_PRODUCT_ID, productId);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_THIRD_PARTY_CUST_SEGMENT_CODE,segmentId);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_LONGITUDE,longitude);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_LATITUDE,latitude);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_DEBIT_CARD_NO,cardNo);
            //
            if(cMsisdn != null && !cMsisdn.equals(""))
                workFlowWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE,cMsisdn);
            if(cNic != null && !cNic.equals(""))
                workFlowWrapper.putObject(CommandFieldConstants.KEY_CNIC,cNic);

            I8SBSwitchControllerRequestVO requestVO = ESBAdapter.prepareRequestVoForBOP(I8SBConstants.RequestType_BOP_CardIssuanceReIssuance);
            requestVO.setMobileNumber(cMsisdn);
            requestVO.setSegmentId(segmentId);
            requestVO.setCNIC(cNic);
            requestVO.setCardId(cardNo);
            requestVO.setFingerIndex(fingerIndex);
            requestVO.setFingerTemplete(fingerTemplate);
            requestVO.setTempeleteType(templateType);
            requestVO.setTerminalID(terminalId);
            requestVO.setTransactionType("03");
            SwitchWrapper sWrapper=new SwitchWrapperImpl();
            sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
            I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
            sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
            workFlowWrapper.putObject("SWITCH_WRAPPER",sWrapper);
            //
            logger.info(classNMethodName + " Going to execute Transaction flow. " + inputParams);
            workFlowWrapper = getCommonCommandManager().executeSaleCreditTransaction(workFlowWrapper);
            transactionModel = workFlowWrapper.getTransactionModel();
            productModel = workFlowWrapper.getProductModel();

        }
        catch (CommandException e) {
            if (e.getErrorCode() == 122)
                ivrErrorCode = "122";
            else if (e.getErrorCode() == 121)
                ivrErrorCode = "121";
            else
                ivrErrorCode = ErrorCodes.COMMAND_EXECUTION_ERROR.toString();

            logger.error(genericExceptionMessage + e.getMessage());
            String nadraSessionId = null;
            if (workFlowWrapper.getObject(CommandFieldConstants.KEY_NADRA_SESSION_ID) != null)
                nadraSessionId = (String) workFlowWrapper.getObject(CommandFieldConstants.KEY_NADRA_SESSION_ID);
            String thirdPartyTransactionId = null;
            throw new CommandException(e.getMessage(), Long.valueOf(ivrErrorCode), ErrorLevel.MEDIUM, e, nadraSessionId, thirdPartyTransactionId);
        }
        catch (Exception ex){
            ex.printStackTrace();
            logger.error(genericExceptionMessage);
            throw new CommandException(ex.getMessage(), ErrorCodes.TERMINATE_EXECUTION_FLOW, ErrorLevel.MEDIUM, new Throwable());
        }
    }

    @Override
    public String response() {
        List<LabelValueBean> params = new ArrayList<>();
        params.add(new LabelValueBean(XMLConstants.TAG_MSG.toUpperCase(), XMLConstants.MESSAGE_SENT_SUCCESS));
        return MiniXMLUtil.createInfoResponseXMLByParams(params);
    }
}
