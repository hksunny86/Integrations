package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.BankSegmentsModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.Map;

import static com.inov8.microbank.common.util.XMLConstants.*;

public class ThirdPartyAccountOpeningInfoCommand extends BaseCommand {

    private final Log logger = LogFactory.getLog(ThirdPartyAccountOpeningInfoCommand.class);

    private String cMsisdn, cNic;
    private String segmentId,cardNo,terminalId;
    private String fingerIndex;
    private String templateType;
    private String fingerTemplate;

    private AppUserModel appUserModel;
    private UserDeviceAccountsModel userDeviceAccountsModel;
    private I8SBSwitchControllerResponseVO responseVO;

    @Override
    public void prepare(BaseWrapper baseWrapper) {
        this.cMsisdn = getCommandParameter(baseWrapper, "CMOB");
        this.cNic = getCommandParameter(baseWrapper, "CNIC");
        this.deviceTypeId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        segmentId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_THIRD_PARTY_CUST_SEGMENT_CODE);
        cardNo = getCommandParameter(baseWrapper,CommandFieldConstants.KEY_DEBIT_CARD_NO);
        terminalId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TERMINAL_ID);
        appUserModel = ThreadLocalAppUser.getAppUserModel();
    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        validationErrors = ValidatorWrapper.doRequired(cMsisdn,validationErrors,"Mobile No");
        validationErrors = ValidatorWrapper.doRequired(this.cNic, validationErrors, "CNIC");
        validationErrors = ValidatorWrapper.doRequired(segmentId, validationErrors, "Segment Id");
        validationErrors = ValidatorWrapper.doRequired(this.deviceTypeId, validationErrors, "Device Type");
        if(validationErrors.hasValidationErrors())
            throw new CommandException(validationErrors.getErrors(), ErrorCodes.VALIDATION_ERROR, ErrorLevel.HIGH,new Throwable());
        return validationErrors;
    }

    @Override
    public void execute() throws CommandException {
        String classNMethodName = "[ThirdPartyAccountOpeningInfoCommand.execute] ";
        String exceptionMessage = "Exception occurred ";
        String inputParams = "AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Product ID: " +  ProductConstantsInterface.THIRD_PARTY_ACCOUNT_OPENING
                + " Customer Mobile #:" + cMsisdn + " Customer Card #: " + cardNo ;
        String genericExceptionMessage = classNMethodName + exceptionMessage + inputParams;
        logger.info(classNMethodName + "\n" + inputParams);
        ValidationErrors validationErrors;
        if(appUserModel.getRetailerContactId() != null) {
            try {
                validationErrors = getCommonCommandManager().checkActiveAppUser(appUserModel);
                if (validationErrors.hasValidationErrors()) {
                    throw new Exception("Agent Is not in correct state.");
                }

                BankSegmentsModel bankSegmentsModel = new BankSegmentsModel();
                bankSegmentsModel.setDestinationSegmentId(Long.valueOf(segmentId));
                bankSegmentsModel = commonCommandManager.getBankSegmentsDao().findByPrimaryKey(Long.valueOf(segmentId));
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
                //userDeviceAccountsModel = commonCommandManager.getUserDeviceAccountListViewManager().findUserDeviceByAppUserId(appUserModel.getAppUserId());
                //Third Party Call
                ESBAdapter adapter =new ESBAdapter();
                I8SBSwitchControllerRequestVO requestVO = ESBAdapter.prepareRequestVoForBOP(I8SBConstants.RequestType_BOP_AccountRegistrationInquiry);
                requestVO.setMobileNumber(cMsisdn);
                requestVO.setAccountNumber(EncryptionUtil.encryptWithAES("682ede816988e58fb6d057d9d85605e0",cMsisdn));
                requestVO.setSegmentId(segmentId);
                requestVO.setCNIC(cNic);
                requestVO.setCardId(cardNo);
                requestVO.setTerminalID(terminalId);
                requestVO.setTransactionType("01");
                SwitchWrapper sWrapper=new SwitchWrapperImpl();
                sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                responseVO = new I8SBSwitchControllerResponseVO();
                sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
                sWrapper = adapter.makeI8SBCall(sWrapper);
                ESBAdapter.processI8sbResponseCode(sWrapper.getI8SBSwitchControllerResponseVO(),false);
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
