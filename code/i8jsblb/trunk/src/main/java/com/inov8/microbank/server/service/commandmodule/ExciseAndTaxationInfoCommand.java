package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.common.CreateNewDateFormat;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

import static com.inov8.microbank.common.util.XMLConstants.*;
import static org.apache.poi.hwpf.model.FileInformationBlock.logger;

public class ExciseAndTaxationInfoCommand extends BaseCommand {
    private String vehicleRegistrationNo;
    private String vehicleChassisNo;
    private String customerMobileNo;
    private String deviceTypeID;
    private String prodcutID;
    private AppUserModel agentAppUserModel, customerAppUserModel;
    private ProductModel productModel;
    I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = null;
    I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = null;
    CommissionWrapper commissionWrapper;
    CommissionAmountsHolder commissionAmountsHolder;
    protected final Log logger = LogFactory.getLog(getClass());

    @Override
    public void prepare(BaseWrapper baseWrapper) {

        String classNMethodName = "[ExciseAndTaxationInfoCommand.prepare] ";
        if(logger.isDebugEnabled()){
            logger.debug("Start of " + classNMethodName);
        }
        deviceTypeID  = baseWrapper.getObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID).toString();
        prodcutID = baseWrapper.getObject(CommandFieldConstants.KEY_PROD_ID).toString();
        vehicleRegistrationNo = baseWrapper.getObject(CommandFieldConstants.KEY_VEHICLE_REG_NO).toString();
        vehicleChassisNo = baseWrapper.getObject(CommandFieldConstants.KEY_VEHICLE_CHASSIS_NO).toString();
        customerMobileNo = baseWrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE).toString();
        agentAppUserModel = ThreadLocalAppUser.getAppUserModel();

        // loading Customer App User Model:
        try {
            customerAppUserModel = this.getCommonCommandManager().loadAppUserByMobileAndType(customerMobileNo,new Long[]{UserTypeConstantsInterface.CUSTOMER});
            BaseWrapper productBaseWrapper = new BaseWrapperImpl();
            productModel = new ProductModel();
            productModel.setPrimaryKey(Long.parseLong(prodcutID));
            productBaseWrapper.setBasePersistableModel(productModel);
            productBaseWrapper = this.getCommonCommandManager().loadProduct(productBaseWrapper);

            productModel = (ProductModel) productBaseWrapper.getBasePersistableModel();
        } catch (FrameworkCheckedException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {


        String classNMethodName = "[ExciseAndTaxationInfoCommand]";
        validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
        validationErrors = ValidatorWrapper.doRequired(prodcutID, validationErrors, "Product Id");
        validationErrors = ValidatorWrapper.doRequired(vehicleRegistrationNo, validationErrors, "Vehicle Registration No");
        validationErrors = ValidatorWrapper.doRequired(vehicleChassisNo, validationErrors, "Vehicle Chasis No ");
        validationErrors = ValidatorWrapper.doRequired(customerMobileNo, validationErrors, "Customer Mobile No");


        if(productModel == null)
        {
            // throw exception here...
            logger.error( classNMethodName+ " Product model not found: ");
            validationErrors = ValidatorWrapper.addError(validationErrors, "Product not found.");
        }
       if(customerAppUserModel != null)
       {
           // User with mobile No Exists..So we will throw a exception
           logger.error( classNMethodName+ "Customer Already Exist with Provided Mobile No");
           validationErrors = ValidatorWrapper.addError(validationErrors, "Customer Already Exists.");
       }

        return validationErrors;
    }

    @Override
    public void execute() throws CommandException {
        if(customerAppUserModel == null)
        {
            try {

                i8SBSwitchControllerRequestVO =   ESBAdapter.prepareGetAssessmentDetailsRequest(vehicleRegistrationNo,vehicleChassisNo,customerMobileNo,productModel.getProductCode());
                SwitchWrapper i8sbSwitchWrapper = new SwitchWrapperImpl();
                i8sbSwitchWrapper.setI8SBSwitchControllerRequestVO(i8SBSwitchControllerRequestVO);
                i8sbSwitchWrapper = new ESBAdapter().makeI8SBCall(i8sbSwitchWrapper);
                i8SBSwitchControllerResponseVO = i8sbSwitchWrapper.getI8SBSwitchControllerResponseVO();
                ESBAdapter.processI8sbResponseCode(i8SBSwitchControllerResponseVO,Boolean.FALSE);

               // To calculate the commission

                SmartMoneyAccountModel agentSmartMoneyAccountModel = commonCommandManager.getSmartMoneyAccountByAppUserModelAndPaymentModId(agentAppUserModel,PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
                TransactionModel transactionModel = new TransactionModel();
                transactionModel.setTransactionAmount(Double.valueOf(i8SBSwitchControllerResponseVO.getTotalAmount().toString()));

                TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
                WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
                transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.EXCISE_AND_TAXATION_TX);
                workFlowWrapper.setProductModel(productModel);
                workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
                workFlowWrapper.setTransactionModel(transactionModel);
                workFlowWrapper.setSmartMoneyAccountModel(agentSmartMoneyAccountModel);
                workFlowWrapper.setFromRetailerContactAppUserModel(agentAppUserModel);
                SegmentModel segmentModel = new SegmentModel();
                segmentModel.setSegmentId(10001L);
                workFlowWrapper.setSegmentModel(segmentModel);

                RetailerContactModel agentRetailContactModel = new RetailerContactModel();
                agentRetailContactModel.setRetailerContactId(agentAppUserModel.getRetailerContactId());
                BaseWrapper retailContactBaseWrapper = new BaseWrapperImpl();
                retailContactBaseWrapper.setBasePersistableModel(agentRetailContactModel);
                agentRetailContactModel = (RetailerContactModel) commonCommandManager.loadRetailerContact(retailContactBaseWrapper).getBasePersistableModel();
                workFlowWrapper.setRetailerContactModel(agentRetailContactModel);
                workFlowWrapper.setTaxRegimeModel(agentRetailContactModel.getTaxRegimeIdTaxRegimeModel());
                workFlowWrapper.setDeviceTypeModel(new DeviceTypeModel());
                workFlowWrapper.getDeviceTypeModel().setDeviceTypeId(Long.valueOf(deviceTypeId));
                workFlowWrapper.setHandlerModel(agentAppUserModel.getHandlerIdHandlerModel());
                commonCommandManager.checkProductLimit(null, productModel.getProductId(), agentAppUserModel.getMobileNo(), Long.valueOf(deviceTypeId), transactionModel.getTransactionAmount(), productModel, null,workFlowWrapper.getHandlerModel());
                commissionWrapper = commonCommandManager.calculateCommission(workFlowWrapper);
                commissionAmountsHolder = (CommissionAmountsHolder)commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);

            } catch (Exception e) {
                logger.error(e.getMessage());
                throw new CommandException(e.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
            }
        }
    }

    @Override
    public String response() {
        StringBuilder responseXML = new StringBuilder();
        if(i8SBSwitchControllerResponseVO != null && customerAppUserModel == null) {
            responseXML.append(TAG_SYMBOL_OPEN).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_VEHICLE_REG_NO, i8SBSwitchControllerResponseVO.getRegistrationNumber()));
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_VEHICLE_CHASSIS_NO, i8SBSwitchControllerResponseVO.getChassisNo()));
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_VEHICLE_ASSESMENT_NO, Integer.toString(i8SBSwitchControllerResponseVO.getVehicleAssesmentNo())));
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_VEH_REG_DATE, i8SBSwitchControllerResponseVO.getRegistrationDate()));
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_MAKER_MAKE, i8SBSwitchControllerResponseVO.getMakerMake()));
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_VEHICLE_CATEGORY, i8SBSwitchControllerResponseVO.getCatagory()));
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_VEHICLE_BODY_TYPE, i8SBSwitchControllerResponseVO.getBodyType()));
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_VEHICLE_ENG_CAPACITY, Integer.toString(i8SBSwitchControllerResponseVO.getEngCapacity())));
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_VEHICLE_SEATS, Integer.toString(i8SBSwitchControllerResponseVO.getSeats())));
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_VEHICLE_CYLINDERS, Integer.toString(i8SBSwitchControllerResponseVO.getCyllinders())));
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_VEHICLE_OWNER_NAME, i8SBSwitchControllerResponseVO.getOwnerName()));
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_OWNER_CNIC, i8SBSwitchControllerResponseVO.getOwnerCnic()));
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FILEER_STATUS, i8SBSwitchControllerResponseVO.getFilerStatus()));
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TAX_PAID_FROM, i8SBSwitchControllerResponseVO.getTaxPaidFrom()));
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TAX_PAID_UPTO, i8SBSwitchControllerResponseVO.getTaxPaidUpto().split("T")[0]));
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_VEHICLE_TAX_PAID_LIFETIME, i8SBSwitchControllerResponseVO.getVehTaxPaidLifeTime()));
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_VEHICLE_STATUS, i8SBSwitchControllerResponseVO.getVehicleStatus()));
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FITNESS_DATE, i8SBSwitchControllerResponseVO.getFitnessDate()));
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TAX_AMOUNT, i8SBSwitchControllerResponseVO.getTotalAmount().toString()));
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_ASSESMENT_DATE, i8SBSwitchControllerResponseVO.getAssesmentDate()));
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_ASSESSMENT_NO, Integer.toString(i8SBSwitchControllerResponseVO.getVehicleAssesmentNo())));
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TX_AMOUNT, ""+replaceNullWithZero(commissionAmountsHolder.getTransactionAmount())));
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATTED_TX_AMOUNT, ""+Formatter.formatNumbers(commissionAmountsHolder.getTransactionAmount())));
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_COMM_AMOUNT, ""+replaceNullWithZero(commissionAmountsHolder.getTotalCommissionAmount())));
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_COMM_AMOUNT, Formatter.formatNumbers(commissionAmountsHolder.getTotalCommissionAmount())));
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TX_PROCESS_AMNT, ""+replaceNullWithZero(commissionAmountsHolder.getTransactionProcessingAmount())));
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_TX_PROCESS_AMNT, Formatter.formatNumbers(commissionAmountsHolder.getTransactionProcessingAmount())));
            Integer newT = (int) Math.round(commissionAmountsHolder.getTotalAmount());
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TOTAL_AMOUNT, newT.toString()));
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_TOTAL_AMOUNT, Formatter.formatNumbers(commissionAmountsHolder.getTotalAmount())));
            responseXML.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);
        }
        return responseXML.toString();
    }
}