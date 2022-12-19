package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.BankSegmentsModel;
import com.inov8.microbank.common.model.GeoLocationModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.Map;

import static com.inov8.microbank.common.util.XMLConstants.*;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;

public class BOPCardIssuanceInfoCommand extends BaseCommand{

    private final Log logger = LogFactory.getLog(BOPCardIssuanceInfoCommand.class);

    private String cMsisdn, cNic;
    private String segmentId,cardNo,terminalId,latitude,longitude;

    private AppUserModel appUserModel;
    private I8SBSwitchControllerResponseVO responseVO;
    WorkFlowWrapper workFlowWrapper;

    @Override
    public void prepare(BaseWrapper baseWrapper) {
        this.cMsisdn = getCommandParameter(baseWrapper, "CMOB");
        this.cNic = getCommandParameter(baseWrapper, "CNIC");
        this.deviceTypeId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        segmentId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_THIRD_PARTY_CUST_SEGMENT_CODE);
        cardNo = getCommandParameter(baseWrapper,CommandFieldConstants.KEY_DEBIT_CARD_NO);
        terminalId = getCommandParameter(baseWrapper,CommandFieldConstants.KEY_TERMINAL_ID);
        latitude = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_LATITUDE);
        longitude = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_LONGITUDE);
        appUserModel = ThreadLocalAppUser.getAppUserModel();
    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        validationErrors = ValidatorWrapper.doRequired(cMsisdn,validationErrors,"Mobile No");
        validationErrors = ValidatorWrapper.doRequired(this.cNic, validationErrors, "CNIC");
        validationErrors = ValidatorWrapper.doRequired(segmentId, validationErrors, "Segment Id");
        validationErrors = ValidatorWrapper.doRequired(this.deviceTypeId, validationErrors, "Device Type");
        ValidatorWrapper.doRequired(this.latitude, validationErrors, "Latitude");
        ValidatorWrapper.doRequired(this.longitude, validationErrors, "Longitude");
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
        String classNMethodName = "[BOPCardIssuanceInfoCommand.execute] ";
        String exceptionMessage = "Exception occurred ";
        String inputParams = "AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Product ID: " +  ProductConstantsInterface.BOP_CARD_ISSUANCE_REISSUANCE
                + " Customer Mobile #:" + cMsisdn + " Customer Card #: " + cardNo ;
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

        if(appUserModel.getRetailerContactId() != null) {
            try {
                String agentLocation = longitude + "," + latitude;
                validationErrors = getCommonCommandManager().checkActiveAppUser(appUserModel);
                if (validationErrors.hasValidationErrors()) {
                    throw new Exception("Agent Is not in correct state.");
                }
                BankSegmentsModel bankSegmentsModel = new BankSegmentsModel();
                bankSegmentsModel.setDestinationSegmentId(Long.valueOf(segmentId));
                bankSegmentsModel = commonCommandManager.getBankSegmentsDao().findByPrimaryKey(Long.valueOf(segmentId));
                //Populating imd's
                List<Map<String, Object>> notAllowedSegments = commonCommandManager.getBankSegmentsDao().notAllowedSegments(bankSegmentsModel.getImd());

                String imd = bankSegmentsModel.getImd();

                if (imd != null && !cardNo.contains(imd)) {
                    throw new CommandException("Cross Segment Tagging in not allowed", ErrorCodes.VALIDATION_ERROR, ErrorLevel.HIGH);
                } else if (imd == null) {
                    for (int i = 0; i < notAllowedSegments.size(); i++) {
                        if (cardNo.contains((CharSequence) notAllowedSegments.get(i).get("imd"))) {
                            throw new CommandException("Cross Segment Tagging in not allowed", ErrorCodes.VALIDATION_ERROR, ErrorLevel.HIGH);
                        }
                    }
                }
                    ESBAdapter adapter = new ESBAdapter();
                    //Request type for card issuance
                    I8SBSwitchControllerRequestVO requestVO = ESBAdapter.prepareRequestVoForBOP(I8SBConstants.RequestType_BOP_CardIssuanceReIssuanceInquiry);
                    requestVO.setMobileNumber(cMsisdn);
                    requestVO.setSegmentId(segmentId);
                    requestVO.setCNIC(cNic);
                    requestVO.setCardId(cardNo);
                    requestVO.setTerminalID(terminalId);
                    requestVO.setAgentLocation(agentLocation);
                    //trx type for card issuance
                    requestVO.setTransactionType("03");
                    SwitchWrapper sWrapper = new SwitchWrapperImpl();
                    sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                    responseVO = new I8SBSwitchControllerResponseVO();
                    sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
                    sWrapper = adapter.makeI8SBCall(sWrapper);
                    ESBAdapter.processI8sbResponseCode(sWrapper.getI8SBSwitchControllerResponseVO(), false);
                    responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();
            }
            catch (Exception ex){
                logger.error(genericExceptionMessage,ex);
                throw new CommandException(ex.getMessage(), ErrorCodes.TERMINATE_EXECUTION_FLOW, ErrorLevel.MEDIUM, new Throwable());
            }
        }
    }

    @Override
    public String response() {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAMS)
                .append(TAG_SYMBOL_CLOSE);


        strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
                .append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
                .append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                .append(CommandFieldConstants.KEY_CNIC)
                .append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
                .append(this.cNic).append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
                .append(TAG_SYMBOL_CLOSE);

        strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
                .append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
                .append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                .append(CommandFieldConstants.KEY_CUSTOMER_MOBILE)
                .append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
                .append(this.cMsisdn).append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
                .append(TAG_SYMBOL_CLOSE);

        strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
                .append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
                .append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                .append(CommandFieldConstants.KEY_THIRD_PARTY_CUST_SEGMENT_CODE)
                .append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
                .append(this.segmentId)
                .append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH)
                .append(TAG_PARAM).append(TAG_SYMBOL_CLOSE);

        strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
                .append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
                .append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                .append(CommandFieldConstants.KEY_DEBIT_CARD_NO)
                .append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
                .append(this.cardNo)
                .append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH)
                .append(TAG_PARAM).append(TAG_SYMBOL_CLOSE);

        strBuilder.append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH)
                .append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);

        return strBuilder.toString();

    }
}
