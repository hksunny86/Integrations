package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BISPAgentBVSCommand extends BaseCommand {

    private String fingerTemplate;
    private String fingerIndex;
    private String templateType;
    private String nFiq;
    private String minutiaeCount;
    private String iMEINumber;
    private String customerMobileNumber;
    private String nadraSessionId;
    private String responseCode = "00";
    private String cNic;

    private UserDeviceAccountsModel userDeviceAccountsModel;
    private AppUserModel appUserModel;
    private ESBAdapter esbAdapter;

    @Override
    public void prepare(BaseWrapper baseWrapper) {

        nadraSessionId = getCommandParameter(baseWrapper,CommandFieldConstants.KEY_NADRA_SESSION_ID);
        minutiaeCount = getCommandParameter(baseWrapper,CommandFieldConstants.KEY_NADRA_MINUTIAE_COUNT);
        nFiq = getCommandParameter(baseWrapper,CommandFieldConstants.KEY_NADRA_NIFQ);
        fingerIndex = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_FINGER_INDEX);
        templateType = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TEMPLATE_TYPE);
        fingerTemplate = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_FINGER_TEMPLATE);
        customerMobileNumber = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
        iMEINumber = this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_MOBILE_IMEI_NUMBER);
        cNic = this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_CNIC);

        userDeviceAccountsModel = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
        appUserModel = ThreadLocalAppUser.getAppUserModel();
    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
        validationErrors = ValidatorWrapper.doRequired(minutiaeCount, validationErrors, "Minutiae Count");
        validationErrors = ValidatorWrapper.doRequired(nFiq, validationErrors, "NFIQ");
        validationErrors = ValidatorWrapper.doRequired(fingerIndex, validationErrors, "Finger Index");
        validationErrors = ValidatorWrapper.doRequired(templateType, validationErrors, "Template Type");
        validationErrors = ValidatorWrapper.doRequired(fingerTemplate, validationErrors, "Finger Template");
        //validationErrors = ValidatorWrapper.doRequired(customerMobileNumber, validationErrors, "Customer Mobile Number");
        validationErrors = ValidatorWrapper.doRequired(iMEINumber, validationErrors, "IMEI Number");
        validationErrors = ValidatorWrapper.doRequired(cNic, validationErrors, "CNIC ");
        if(validationErrors.hasValidationErrors()) {
            validationErrors = ValidatorWrapper.addError(validationErrors, validationErrors.getErrors());
        }
        return validationErrors;
    }

    @Override
    public void execute() throws CommandException {
//        Long result = 0L;
//        try {
//            result = commonCommandManager.getBispCustNadraVerificationDAO().isNicUsedInCurrentDate(null,
//                    cNic,1L,UserTypeConstantsInterface.RETAILER);
//        } catch (FrameworkCheckedException e) {
//            e.printStackTrace();
//            result = 0L;
//        }
//        if(result >=1)
//            throw new CommandException("Agent BVS is already performed for this NIC for Current Date.", ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,null);
        try{
            this.esbAdapter = new ESBAdapter();
            I8SBSwitchControllerRequestVO requestVO = ESBAdapter.prepareRequestVoForApiGee(I8SBConstants.RequestType_AgentVerification);
            requestVO.setTransactionId(requestVO.getRRN());
            requestVO.setMobileNumber(appUserModel.getMobileNo());
            requestVO.setAgentId(userDeviceAccountsModel.getUserId());
            requestVO.setCNIC(cNic);//NIC to be verified
            requestVO.setSenderMobile(cNic);//Agent Mobile Number
            requestVO.setSessionIdNadra(nadraSessionId);
            requestVO.setAreaName("SINDH");
            requestVO.setFingerIndex(fingerIndex);
            requestVO.setTempeleteType(templateType);
            requestVO.setFingerTemplete(fingerTemplate);
            requestVO.setMinutiaeCount(minutiaeCount);
            requestVO.setNfiq(nFiq);
            requestVO.setImeiNumber(iMEINumber);
            requestVO.setReserved1("");
            I8SBSwitchControllerResponseVO responseVO=new I8SBSwitchControllerResponseVO();
            SwitchWrapper sWrapper=new SwitchWrapperImpl();
            sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
            sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
            sWrapper=esbAdapter.makeI8SBCall(sWrapper);
            responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();
            //
            sWrapper.putObject("APP_USER_TYPE_ID",UserTypeConstantsInterface.RETAILER);
            responseCode = responseVO.getResponseCode();
            String errorMessage = commonCommandManager.saveOrUpdateBVSEntryRequiresNewTransaction(userDeviceAccountsModel,appUserModel,
                    cNic,sWrapper,null);
            if(responseCode != null && responseCode.equals("I8SB-500"))
                responseCode = "500";
            if(errorMessage != null)
                throw new CommandException(errorMessage, Long.valueOf(responseCode),ErrorLevel.MEDIUM,null);
            ESBAdapter.processI8sbResponseCode(responseVO,true);
        }
        catch (Exception ex){
            ex.printStackTrace();
            throw new CommandException(ex.getMessage(), Long.valueOf(responseCode),ErrorLevel.MEDIUM,null);
        }
    }

    @Override
    public String response() {
        return MiniXMLUtil.createMessageXML("Dear Agent, your BVS is successful.");
    }

    public void setEsbAdapter(ESBAdapter esbAdapter) {
        this.esbAdapter = esbAdapter;
    }
    public ESBAdapter getEsbAdapter() {
        return this.esbAdapter ;
    }
}
