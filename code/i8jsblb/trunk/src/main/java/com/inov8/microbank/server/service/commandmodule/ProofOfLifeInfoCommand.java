package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.viewer.Command;

import static com.inov8.microbank.common.util.XMLConstants.*;

public class ProofOfLifeInfoCommand extends BaseCommand {

    private final Log logger = LogFactory.getLog(BOPCardIssuanceInfoCommand.class);
    private String cMsisdn, cNic , productId, agentId, segmentId,latitude,longitude,udid,macAddress,machineName;

    private AppUserModel appUserModel;
    private I8SBSwitchControllerResponseVO responseVO;
    WorkFlowWrapper workFlowWrapper;
    @Override
    public void prepare(BaseWrapper baseWrapper) {
        this.cMsisdn = getCommandParameter(baseWrapper, "CMOB");
        this.cNic = getCommandParameter(baseWrapper, "CNIC");
        this.deviceTypeId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        this.productId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
        agentId = getCommandParameter(baseWrapper,CommandFieldConstants.KEY_AGENT_ID);
        segmentId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_THIRD_PARTY_CUST_SEGMENT_CODE);
        latitude = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_LATITUDE);
        longitude = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_LONGITUDE);
        udid = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_UDID);
        machineName = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MACHINE_NAME);
        macAddress = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MAC_ADDRESS);
        appUserModel = ThreadLocalAppUser.getAppUserModel();

    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        validationErrors = ValidatorWrapper.doRequired(cMsisdn,validationErrors,"Mobile No");
        validationErrors = ValidatorWrapper.doRequired(this.cNic, validationErrors, "CNIC");
        validationErrors = ValidatorWrapper.doRequired(this.deviceTypeId, validationErrors, "Device Type");
        validationErrors = ValidatorWrapper.doRequired(this.agentId, validationErrors, "Agent Id");
        validationErrors = ValidatorWrapper.doRequired(segmentId, validationErrors, "Segment Id");
        if(validationErrors.hasValidationErrors())
            throw new CommandException(validationErrors.getErrors(), ErrorCodes.VALIDATION_ERROR, ErrorLevel.HIGH,new Throwable());

        return validationErrors;
    }

    @Override
    public void execute() throws CommandException {
        double latitudeD = Double.parseDouble(latitude);
        double longitudeD = Double.parseDouble(longitude);
        double currentLatitude = 0;
        double currentLongitude= 0;
        String classNMethodName = "[ProofOfLifeInfoCommand.execute] ";
        String exceptionMessage = "Exception occurred ";
        String inputParams = "AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Product ID: " + ProductConstantsInterface.PROOF_OF_LIFE
                + " Customer Mobile #:" + cMsisdn;
        String genericExceptionMessage = classNMethodName + exceptionMessage + inputParams;
        logger.info(classNMethodName + "\n" + inputParams);
        ValidationErrors validationErrors;
        workFlowWrapper = new WorkFlowWrapperImpl();
        workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);
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

        String agentLocation = longitude + "," + latitude;
        if (appUserModel.getRetailerContactId() != null) {
            try {
                validationErrors = getCommonCommandManager().checkActiveAppUser(appUserModel);
                if (validationErrors.hasValidationErrors()) {
                    throw new Exception("Agent is not in correct state.");
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

                ESBAdapter adapter = new ESBAdapter();
                //Request type for card issuance
                I8SBSwitchControllerRequestVO requestVO = ESBAdapter.prepareRequestVoForBOP(I8SBConstants.RequestType_BOP_ProofOFLifeVerficationInquiry);
                requestVO.setMobileNumber(cMsisdn);
                requestVO.setCNIC(cNic);
                requestVO.setSegmentId(segmentId);
                requestVO.setAgentId(agentId);
                requestVO.setLongitude(longitude);
                requestVO.setLatitude(latitude);
                requestVO.setAgentLocation(agentLocation);
                requestVO.setIpAddress(ipAddress);
                requestVO.setAgentCity(agentCity);
                requestVO.setUdid(udid);
                requestVO.setMacAddress(macAddress);
                requestVO.setMachineName(machineName);
                requestVO.setStatusFlag("");
                SwitchWrapper sWrapper = new SwitchWrapperImpl();
                sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                responseVO = new I8SBSwitchControllerResponseVO();
                sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
                sWrapper = adapter.makeI8SBCall(sWrapper);
                ESBAdapter.processI8sbResponseCode(sWrapper.getI8SBSwitchControllerResponseVO(), false);
                responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();


            } catch (Exception ex) {
                logger.error(genericExceptionMessage, ex);
                throw new CommandException(ex.getMessage(), ErrorCodes.TERMINATE_EXECUTION_FLOW, ErrorLevel.MEDIUM, new Throwable());
            }
        }
    }

    @Override
    public String response() {
        StringBuilder responseXML = new StringBuilder();
        responseXML.append(TAG_SYMBOL_OPEN)
                .append(TAG_PARAMS)
                .append(TAG_SYMBOL_CLOSE);
        responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_SMS_MESSAGE, responseVO.getSmsText()));

        responseXML.append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH)
                .append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);
        return responseXML.toString();

    }
}
