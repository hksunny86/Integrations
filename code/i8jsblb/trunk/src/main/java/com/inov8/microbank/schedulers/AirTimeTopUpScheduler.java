package com.inov8.microbank.schedulers;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.hra.airtimetopup.model.HRARemitanceInfoModel;
import com.inov8.microbank.hra.airtimetopup.vo.AirTimeTopUpVO;
import com.inov8.microbank.hra.service.HRAManager;
import com.inov8.microbank.server.service.commandmodule.CommandManager;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;
import java.util.List;

import static com.inov8.microbank.common.util.ProductConstantsInterface.*;

public class AirTimeTopUpScheduler {

    private static final Log LOGGER = LogFactory.getLog(AirTimeTopUpScheduler.class);

    private AppUserManager appUserManager;
    private HRAManager hraManager;
    private CommandManager commandManager;

    private Double convertAmountToDollars(Double amountCashedIn, Double dollarRate) {
        return amountCashedIn / dollarRate;
    }

    public void init() {
        List<HRARemitanceInfoModel> list = null;
        try {
            list = hraManager.getActiveRemitances();
        } catch (Exception ex) {
            LOGGER.error("Error Occurred in AirTimeTopUpScheduler while fetching Active Remitances :: " + ex.getMessage(), ex);
        }
        if (list != null && !list.isEmpty()) {
            AirTimeTopUpVO airTimeTopUpVO = null;
            AppUserModel appUserModel = null;
            String mobileNetworkName = null;
            for (HRARemitanceInfoModel model : list) {
                AppUserModel example = new AppUserModel();
                example.setMobileNo(model.getMobileNo());
                example.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
                appUserModel = appUserManager.getAppUserModel(example);
                try {
                    airTimeTopUpVO = hraManager.getRateForSpecificRemitance(model.getHraRemitanceInfoId());
                } catch (Exception ex) {
                    LOGGER.error("Error Occurred in AirTimeTopUpScheduler while Performing Top-Up Transaction :: " + ex.getMessage(), ex);
                    LOGGER.error("Error Occurred in AirTimeTopUpScheduler for Remitance_Info_Id :: " + model.getHraRemitanceInfoId());
                }
                if (appUserModel != null && airTimeTopUpVO != null && airTimeTopUpVO.getDollarRate() != null && airTimeTopUpVO.getTopUpRate() != null) {
                    Double amountInDollars = this.convertAmountToDollars(model.getAmountCashedIn(), airTimeTopUpVO.getDollarRate());
                    Double topUpAmount = amountInDollars * airTimeTopUpVO.getTopUpRate();
                    topUpAmount = CommonUtils.formatAmountTwoDecimal(topUpAmount);
                    BaseWrapper baseWrapper = new BaseWrapperImpl();
                    if(appUserModel.getCustomerMobileNetwork() == null)
                    {
                        LOGGER.info("No Mobile Network Defined for Mobile # :: " + appUserModel.getMobileNo());
                        continue;
                    }
                    Long productId = this.getProductIdForTopUp(appUserModel.getCustomerMobileNetwork());
                    if(productId == null)
                    {
                        LOGGER.info("No Mobile Network Defined for Mobile # :: " + appUserModel.getMobileNo());
                        continue;
                    }
                    baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE);
                    baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, productId.toString());
                    baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, appUserModel.getMobileNo());
                    baseWrapper.putObject(CommandFieldConstants.KEY_CSCD,appUserModel.getMobileNo());
                    baseWrapper.putObject(CommandFieldConstants.KEY_BILL_AMOUNT, Double.toString(topUpAmount));
                    baseWrapper.putObject(CommandFieldConstants.KEY_TX_PROCESS_AMNT, "0");
                    baseWrapper.putObject(CommandFieldConstants.KEY_TOTAL_AMOUNT, topUpAmount);
                    baseWrapper.putObject(CommandFieldConstants.KEY_COMM_AMOUNT, "0");
                    baseWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, "SCHEDULER");
                    baseWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, "AirTime TopUp");
                    baseWrapper.putObject("IS_AIR_TIME_TOP_UP","1");
                    try {
                        ThreadLocalAppUser.setAppUserModel(appUserModel);
                        String responseXML = commandManager.executeCommand(baseWrapper,CommandFieldConstants.CMD_CUSTOMER_BILL_PAYMENTS);
                        LOGGER.info("AirTime Top-Up Response :: " + responseXML);
                        model.setActive(Boolean.FALSE);
                        model.setUpdatedOn(new Date());
                    } catch (CommandException e) {
                        LOGGER.error("Error while performing AirTime Top-Up for Mobile # :: " + appUserModel.getMobileNo());
                    }
                }
                else
                {
                    LOGGER.info(">>>>>>>>>>>>>>>>>>>>>>>>Couldn't Perform Top-Up Transaction>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                }
            }
            try {
                hraManager.saveOrUpdateTopUpCollectionRequiresNewTransaction(list);
            } catch (FrameworkCheckedException e) {
                LOGGER.error("Error while Updating AirTime Top-Up Scheduler Collection :: " + e.getMessage(),e);
            }
        }
    }

    private Long getProductIdForTopUp(String mobileNetworkName)
    {
        Long productId = null;
        if(mobileNetworkName.equalsIgnoreCase(MobileNetworkTypeConstantsInterface.MOBILINK_NAME))
            productId = MOBILINK_PREPAID_AIRTIME;
        else if(mobileNetworkName.equalsIgnoreCase(MobileNetworkTypeConstantsInterface.WARID_NAME))
            productId = WARID_PREPAID_AIRTIME;
        else if(mobileNetworkName.equalsIgnoreCase(MobileNetworkTypeConstantsInterface.UFONE_NAME))
            productId = UFONE_PREPAID_AIRTIME;
        else if(mobileNetworkName.equalsIgnoreCase(MobileNetworkTypeConstantsInterface.TELENOR_NAME))
            productId = TELENOR_PREPAID_AIRTIME;
        else if(mobileNetworkName.equalsIgnoreCase(MobileNetworkTypeConstantsInterface.ZONG_NAME))
            productId = ZONG_PREPAID_AIRTIME;
        return productId;
    }

    public void setHraManager(HRAManager hraManager) {
        this.hraManager = hraManager;
    }

    public void setAppUserManager(AppUserManager appUserManager) {
        this.appUserManager = appUserManager;
    }

    public void setCommandManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }
}
