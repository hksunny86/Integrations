package com.inov8.microbank.server.service.salariedaccountprofitcreditdebitmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.SalariedAccountProfitModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.server.service.commandmodule.CommandManager;
import com.inov8.microbank.server.service.salariedaccountprofitmodule.dao.SalariedAccountProfitDebitCreditDAO;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

public class SalariedAccountProfitcreditDebitManagerImpl implements SalariedAccountProfitcreditDebitManager {
    SalariedAccountProfitDebitCreditDAO salariedAccountProfitDebitCreditDAO;
    private CommandManager commandManager;
    private static final Logger LOGGER = Logger.getLogger(SalariedAccountProfitcreditDebitManagerImpl.class);


    @Override
    public List<SalariedAccountProfitModel> loadAllSalariedAccountProfitData() throws FrameworkCheckedException {
        return salariedAccountProfitDebitCreditDAO.loadAllSalariedAccountProfitDebitCredit();
    }

    @Override
    public String salariedAccountProfitDebitCreditDeductionCommand(SalariedAccountProfitModel salariedAccountProfitModel) throws FrameworkCheckedException {
        StringBuilder sb = new StringBuilder();
        AppUserModel appUserModel = new AppUserModel();
        String response = null;
        appUserModel = commandManager.getCommonCommandManager().loadAppUserByMobileAndType(salariedAccountProfitModel.getMobileNo(), UserTypeConstantsInterface.CUSTOMER.longValue());
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(appUserModel);
        ThreadLocalAppUser.setAppUserModel(appUserModel);
        baseWrapper = commandManager.getCommonCommandManager().loadUserDeviceAccountByMobileNumber(baseWrapper);
        UserDeviceAccountsModel uda = (UserDeviceAccountsModel) baseWrapper.getBasePersistableModel();
        ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
        baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, salariedAccountProfitModel.getMobileNo());
        baseWrapper.putObject(CommandFieldConstants.KEY_TX_AMOUNT, salariedAccountProfitModel.getProfitAmount());
        baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, MessageUtil.getMessage("salaried.Profit.product.id"));
        baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.MOBILE);
        baseWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID,"Backend Services");
        baseWrapper.putObject(CommandFieldConstants.KEY_STAN, "");
        baseWrapper.putObject(CommandFieldConstants.KEY_PAYMENT_MODE, "");
        baseWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID,"Backend Services");
        baseWrapper.putObject(CommandFieldConstants.KEY_APP_ID, "2");//Customer Initiated Transaction
        sb.append("Start of executeSalariedAccountProfit() in SalariedAccountProfit for Product :: ").append(MessageUtil.getMessage("salaried.Profit.product.id"));
        LOGGER.info(sb.toString());
        response = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CMD_CREDIT_PAYMENT_API);
        sb.append("\n Response :: " + response);
        LOGGER.info(sb.toString());
        if (response != null) {
//            String response1 = commandManager.executeCommand(baseWrapper, CommandFieldConstants.CMD_DEBIT_PAYMENT_API);
//            if (response1 != null) {
                salariedAccountProfitModel.setCreditUpdatedOn(new Date());
                salariedAccountProfitModel.setDebitUpdatedOn(new Date());
                salariedAccountProfitModel.setLastCreatedOn(new Date());
                salariedAccountProfitModel.setLastUpdatedOn(new Date());
                salariedAccountProfitModel.setProfitAmount("0");
                salariedAccountProfitDebitCreditDAO.saveOrUpdate(salariedAccountProfitModel);
//            }
        }
        return response;
    }


    public void setSalariedAccountProfitDebitCreditDAO(SalariedAccountProfitDebitCreditDAO salariedAccountProfitDebitCreditDAO) {
        this.salariedAccountProfitDebitCreditDAO = salariedAccountProfitDebitCreditDAO;
    }

    public void setCommandManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }
}
