package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
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

public class ProofOfLifeCommand extends BaseCommand {
    private final Log logger = LogFactory.getLog(ProofOfLifeCommand.class);

    private String cMsisdn, cNic;
    private String segmentId,productId,latitude,longitude,udid,macAddress,machineName, agentId, statusFlag;
    private String fingerIndex,templateType,fingerTemplate;

    private AppUserModel appUserModel;
    private ProductModel productModel;
    private RetailerContactModel retailerContactModel;
    private UserDeviceAccountsModel userDeviceAccountsModel;
    private TransactionModel transactionModel;

    @Override
    public void prepare(BaseWrapper baseWrapper) {
        fingerIndex = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_FINGER_INDEX);
        templateType = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TEMPLATE_TYPE);
        fingerTemplate = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_FINGER_TEMPLATE);
        productId = this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_PROD_ID);
        this.cMsisdn = getCommandParameter(baseWrapper, "CMOB");
        this.cNic = getCommandParameter(baseWrapper, "CNIC");
        this.segmentId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_THIRD_PARTY_CUST_SEGMENT_CODE);
        this.deviceTypeId = getCommandParameter(baseWrapper, "DTID");
        appUserModel = ThreadLocalAppUser.getAppUserModel();
        latitude = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_LATITUDE);
        agentId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AGENT_ID);
        longitude = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_LONGITUDE);
        udid = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_UDID);
        machineName = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MACHINE_NAME);
        macAddress = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MAC_ADDRESS);
        statusFlag = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_STATUS);
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
        double latitudeD = Double.parseDouble(latitude);
        double longitudeD = Double.parseDouble(longitude);
        double currentLatitude = 0;
        double currentLongitude= 0;
        String transactionAmount = "0.0";
        WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
        String classNMethodName = "[ProofOfLifeCommand.execute] ";
        String exceptionMessage = "Exception occurred ";
        String inputParams = "AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Product ID: " +  ProductConstantsInterface.PROOF_OF_LIFE
                + " Customer Mobile #:" + cMsisdn;
        String genericExceptionMessage = classNMethodName + exceptionMessage + inputParams;
        logger.info(classNMethodName + "\n" + inputParams);
        try{
            RetailerContactModel retailerContactModel = new RetailerContactModel();
            retailerContactModel.setRetailerContactId(appUserModel.getRetailerContactId());

            BaseWrapper bWrapper = new BaseWrapperImpl();
            GeoLocationModel geoLocationModel = new GeoLocationModel();
            bWrapper.setBasePersistableModel(retailerContactModel);
            try {
                bWrapper = commonCommandManager.loadRetailerContact(bWrapper);
            } catch (FrameworkCheckedException e) {
                e.printStackTrace();
                throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
            }
            retailerContactModel = (RetailerContactModel) bWrapper.getBasePersistableModel();

            if(retailerContactModel.getGeoLocationId()!=null){
                geoLocationModel = commonCommandManager.getGeoLocationDao().findByPrimaryKey(retailerContactModel.getGeoLocationId());
            }
            if(geoLocationModel == null || (geoLocationModel != null && (geoLocationModel.getLatitude() == null || geoLocationModel.getLongitude() == null)))
                throw new CommandException("Agent Location is not configured.Please contact your service provider.", ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,null);
            currentLatitude = geoLocationModel.getLatitude();
            currentLongitude= geoLocationModel.getLongitude();
            double currentDistance=LocatorUtil.BISPCalculateDistance(currentLatitude,currentLongitude,latitudeD,longitudeD);

            Long result = commonCommandManager.getBispCustNadraVerificationDAO().isBVSSuccessful(null,
                    null,cNic,-1L,UserTypeConstantsInterface.CUSTOMER);
            if(result >= 16)
                throw new CommandException(MessageUtil.getMessage("BISP.cust.daily.NADRA.retries"), ErrorCodes.INVALID_INPUT,ErrorLevel.MEDIUM,null);
            else{
                result = commonCommandManager.getBispCustNadraVerificationDAO().isBVSSuccessful(null,
                        userDeviceAccountsModel.getUserId(),cNic,-1L,UserTypeConstantsInterface.CUSTOMER);
                if(result >= 8)
                    throw new CommandException(MessageUtil.getMessage("BISP.cust.agent.NADRA.retries"), ErrorCodes.INVALID_INPUT,ErrorLevel.MEDIUM,null);
            }
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
            bWrapper.putObject(CommandConstants.VELOCITY_CUSTOMER_ID, userId);
//            bWrapper.putObject(CommandConstants.VELOCITY_RETAILER_ID, retailerId);
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

            RetailerModel retailerModel = new RetailerModel();
            retailerModel.setRetailerId(retailerContactModel.getRetailerId());
            retailerModel = commonCommandManager.getRetailerDao().findByPrimaryKey(retailerModel.getRetailerId());
            String agentCity = retailerModel.getCity();
            if(retailerModel.getCity() == null){
                agentCity = "";
            }

            ActionLogModel actionLogModel = new ActionLogModel();
            actionLogModel.setActionLogId(ThreadLocalActionLog.getActionLogId());
            actionLogModel = commonCommandManager.getActionLogDao().findByPrimaryKey(ThreadLocalActionLog.getActionLogId());
            String ipAddress = actionLogModel.getClientIpAddress();

            String agentLocation = longitude + "," + latitude;

            workFlowWrapper.setSmartMoneyAccountModel(agentSMAModel);
            AccountInfoModel accountInfoModel = commonCommandManager.getAccountInfoModel(appUserModel.getAppUserId(), agentSMAModel.getName());
            workFlowWrapper.setAccountInfoModel(accountInfoModel);
            TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
            transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.PROOF_OF_LIFE_TX);
            workFlowWrapper.setRetailerContactModel(retailerContactModel);
            workFlowWrapper.setFromRetailerContactModel(retailerContactModel);
            DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
            deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));
            workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
            workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_PRODUCT_ID, productId);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_THIRD_PARTY_CUST_SEGMENT_CODE,segmentId);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_MAC_ADDRESS, macAddress);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_MACHINE_NAME, machineName);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_LONGITUDE, longitude);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_LATITUDE, latitude);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_UDID, udid);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_IP_ADDRESS, ipAddress);

            if(cMsisdn != null && !cMsisdn.equals(""))
                workFlowWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE,cMsisdn);
            if(cNic != null && !cNic.equals(""))
                workFlowWrapper.putObject(CommandFieldConstants.KEY_CNIC,cNic);

            I8SBSwitchControllerRequestVO requestVO = ESBAdapter.prepareRequestVoForBOP(I8SBConstants.RequestType_BOP_ProofOfLifeVerification);
            requestVO.setMobileNumber(cMsisdn);
            requestVO.setSegmentId(segmentId);
            requestVO.setCNIC(cNic);
            requestVO.setAgentId(agentId);
            requestVO.setLongitude(longitude);
            requestVO.setLatitude(latitude);
            requestVO.setAgentLocation(agentLocation);
            requestVO.setAgentCity(agentCity);
            requestVO.setIpAddress(ipAddress);
            requestVO.setMacAddress(macAddress);
            requestVO.setMachineName(machineName);
            requestVO.setUdid(udid);
            requestVO.setStatusFlag(statusFlag);//will come from mobile side
            requestVO.setFingerIndex(fingerIndex);
            requestVO.setTempeleteType(templateType);
            requestVO.setFingerTemplete(fingerTemplate);
//Params To be Added
            SwitchWrapper sWrapper=new SwitchWrapperImpl();
            sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
            I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
            sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
            workFlowWrapper.putObject("SWITCH_WRAPPER",sWrapper);

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
        params.add(new LabelValueBean(XMLConstants.TAG_MSG.toUpperCase(), "Customer BVS is successful."));
        return MiniXMLUtil.createInfoResponseXMLByParams(params);
    }
}
