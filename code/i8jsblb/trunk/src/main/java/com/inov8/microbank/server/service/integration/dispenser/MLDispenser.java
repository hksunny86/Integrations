package com.inov8.microbank.server.service.integration.dispenser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.remoting.RemoteConnectFailureException;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.jdbc.OracleSequenceGeneratorJdbcDAO;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.integration.boss.controller.BOSSVO;
import com.inov8.integration.boss.controller.IBOSSSwitchController;
import com.inov8.microbank.common.exception.ConnectionFailureException;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.model.AuditLogModel;
import com.inov8.microbank.common.model.productdeviceflowmodule.ProductDeviceFlowListViewModel;
import com.inov8.microbank.common.util.DeviceFlowConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.HttpInvokerUtil;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.failurelogmodule.FailureLogManager;
import com.inov8.microbank.server.service.integration.vo.MLVO;
import com.inov8.microbank.server.service.inventorymodule.ProductUnitManager;
import com.inov8.microbank.server.service.inventorymodule.ShipmentManager;
import com.inov8.microbank.server.service.productmodule.ProductIntgModuleInfoManager;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.settlementmodule.SettlementManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.thoughtworks.xstream.XStream;

public class MLDispenser extends BillPaymentProductDispenser {
    private ProductIntgModuleInfoManager productIntgModuleInfoManager;
    private CommissionManager commissionManager;
    private SmartMoneyAccountManager smartMoneyAccountManager;
    private CustTransManager customerManager;
    private SettlementManager settlementManager;
    private ProductManager productManager;
    private GenericDao genericDAO;
    private ProductUnitManager productUnitManager;
    private FailureLogManager failureLogManager;
    private AppUserManager appUserManager;
    private ShipmentManager shipmentManager;
    private ApplicationContext ctx;
    private OracleSequenceGeneratorJdbcDAO oracleSequenceGeneratorJdbcDAO;

    private final Log logger = LogFactory.getLog(getClass());

    public MLDispenser(CommissionManager commissionManager,
            SmartMoneyAccountManager smartMoneyAccountManager,
            SettlementManager settlementManager, ProductManager productManager,
            AppUserManager appUserManager,
            ProductUnitManager productUnitManager,
            ShipmentManager shipmentManager, GenericDao genericDAO,
            ApplicationContext ctx) {
        this.commissionManager = commissionManager;
        this.smartMoneyAccountManager = smartMoneyAccountManager;
        this.settlementManager = settlementManager;
        this.productManager = productManager;
        this.appUserManager = appUserManager;
        this.productUnitManager = productUnitManager;
        this.shipmentManager = shipmentManager;
        this.genericDAO = genericDAO;
        this.ctx = ctx;
        this.oracleSequenceGeneratorJdbcDAO = null;

        super.setAuditLogModule((FailureLogManager) ctx
                .getBean("failureLogManager"));
    }

    public WorkFlowWrapper doSale(WorkFlowWrapper workFlowWrapper)
            throws FrameworkCheckedException {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Inside doSale of MLDispenser");
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        DateFormat timeFormat = new SimpleDateFormat("HHmmss");
        BOSSVO zongIntegrationVO = new BOSSVO();
//        ZongIntegrationMessageVO zongIntegrationVO = new ZongIntegrationMessageVO(false, "1003", workFlowWrapper.getTransactionCodeModel().getCode());
        MLVO zongTopupVO = (MLVO) workFlowWrapper.getProductVO();
        Date today = new Date();

        zongIntegrationVO.setSubNumber(zongTopupVO.getConsumerNo());

        zongIntegrationVO.setAmount(Integer.toString(zongTopupVO.getBillAmount().intValue()));

        zongIntegrationVO.setMicrobankTransactionId(workFlowWrapper.getTransactionCodeModel().getCode());

        XStream xStream = new XStream();
        AuditLogModel auditLogModel = auditLogBeforeCall(workFlowWrapper, "");
        try {
            IBOSSSwitchController switchController = 
                    
                    getRemoteServiceManager(workFlowWrapper
                    .getProductModel()
                    .getProductIntgModuleInfoIdProductIntgModuleInfoModel()
                    .getUrl());
            if (switchController == null) {
                this.logger
                        .error("[MLDispenser.doSale] Could not connect to Switch. Throwing Exception for Logged in AppuserID:"
                                + ThreadLocalAppUser.getAppUserModel()
                                        .getAppUserId()
                                + ". Exception:"
                                + "MLDispenser - Service cannot be invoked.");
                throw new ConnectionFailureException(
                        "MLDispenser - Service cannot be invoked.");
            }
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Going to hit Zong for Bill Payment");
            }
            this.logger
                    .info("[MLDispenser.doSale] Going to make Topup Transacin for Logged in AppuserID:"
                            + ThreadLocalAppUser.getAppUserModel()
                                    .getAppUserId());
            zongIntegrationVO = (BOSSVO) switchController
                    .dealerTopup(zongIntegrationVO);
            if (zongIntegrationVO != null) {
                auditLogModel.setIntegrationPartnerIdentifier(zongIntegrationVO.getTransactionRefNumber()
                        );
                zongTopupVO
                        .setResponseCode(zongIntegrationVO.getResponse().getResponseCode());
                zongTopupVO.setSerialNumber(zongIntegrationVO.getBusinessSerial());
            }
            if ((zongIntegrationVO.getResponse().getResponseCode() != null)
                    && (zongIntegrationVO.getResponse().getResponseCode()
                            .equalsIgnoreCase("0000"))) {
                this.logger
                        .info("[MLDispenser.doSale] Logged in AppUserID:"
                                + ThreadLocalAppUser.getAppUserModel()
                                        .getAppUserId()
                                + " Response of product provising from ZONG."
                                + zongIntegrationVO.getResponse().getResponseCode());

                auditLogModel.setCustomField1("S");
            } else {
                this.logger
                        .error("[MLDispenser.doSale] Logged in AppUserID:"
                                + ThreadLocalAppUser.getAppUserModel()
                                        .getAppUserId()
                                + " Response code is null or invalid response code received."
                                + zongIntegrationVO.getResponse().getResponseCode());

                auditLogModel.setCustomField1("F");
                auditLogModel
                        .setCustomField2("Error Occured while accessing ZONG--->"
                                + zongIntegrationVO.getResponse().getResponseCode());
                parseResponse(zongIntegrationVO.getResponse().getResponseCode(),
                        workFlowWrapper);
            }
        } catch (RemoteConnectFailureException e) {
            this.logger
                    .error("[MLDispenser.doSale] Exception occured while making Zont Topup Logged in AppUserID:"
                            + ThreadLocalAppUser.getAppUserModel()
                                    .getAppUserId()
                            + " Exception Msg:"
                            + e.getMessage());
            throw new FrameworkCheckedException(
                    "Your request cannot be processed at the moment. Please try again later.");
        } catch (FrameworkCheckedException ex) {
            this.logger
                    .error("[MLDispenser.doSale] Exception occured for Logged in AppUserID:"
                            + ThreadLocalAppUser.getAppUserModel()
                                    .getAppUserId()
                            + " Exception Msg:"
                            + ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            this.logger
                    .error("[MLDispenser.doSale] Exception occured for Logged in AppUserID:"
                            + ThreadLocalAppUser.getAppUserModel()
                                    .getAppUserId()
                            + " Exception Msg:"
                            + ex.getMessage());
            throw new FrameworkCheckedException(
                    "Your request cannot be processed at the moment. Please try again later.");
        } finally {
            if ((auditLogModel.getCustomField1() != null)
                    && (auditLogModel.getCustomField1().equals("F"))) {
                auditLogAfterCall(auditLogModel, "");
            } else {
                auditLogAfterCall(auditLogModel,
                        xStream.toXML(zongIntegrationVO));
            }
        }
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Ending doSale of ZongBillPaymentDispenser");
        }
        return workFlowWrapper;
    }

    public WorkFlowWrapper getBillInfo(WorkFlowWrapper workFlowWrapper)
            throws FrameworkCheckedException {
        return workFlowWrapper;
    }

    public WorkFlowWrapper rollback(WorkFlowWrapper workFlowWrapper)
            throws FrameworkCheckedException {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Inside Rollback of MLDispenser");
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        DateFormat timeFormat = new SimpleDateFormat("HHmmss");
        BOSSVO zongIntegrationVO = new BOSSVO();
        MLVO zongTopupVO = (MLVO) workFlowWrapper.getProductVO();
        Date today = new Date();

        zongIntegrationVO.setBusinessSerial(zongTopupVO.getSerialNumber());

//        zongIntegrationVO.setAmount(Integer.toString(zongTopupVO
//                .getBillAmount().intValue()));
//
//        zongIntegrationVO.setMicrobankTransactionId(workFlowWrapper
//                .getTransactionCodeModel().getCode());

        XStream xStream = new XStream();
        try {
            IBOSSSwitchController switchController = getRemoteServiceManager(workFlowWrapper
                    .getProductModel()
                    .getProductIntgModuleInfoIdProductIntgModuleInfoModel()
                    .getUrl());
            System.out.println();
            if (switchController == null) {
                throw new ConnectionFailureException(
                        "MLDispenser - Service cannot be invoked.");
            }
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Going to hit Zong for REVERSAL");
            }
            zongIntegrationVO = (BOSSVO) switchController
                    .dealerTopupRollback(zongIntegrationVO);
            if (zongIntegrationVO != null) {
                
                if ((zongIntegrationVO.getResponse().getResponseCode() != null)
                        && (zongIntegrationVO.getResponse().getResponseCode()
                                .equalsIgnoreCase("0000"))) {
                    if (this.logger.isDebugEnabled()) {
                        this.logger
                                .debug("***** Response of reversal from ZONG :"
                                        + zongIntegrationVO.getResponse().getResponseCode());
                    }
                } else {
                    if (this.logger.isDebugEnabled()) {
                        this.logger
                                .debug("Response code is null or invalid response code received."
                                        + zongIntegrationVO.getResponse().getResponseCode());
                    }
                    parseResponse(zongIntegrationVO.getResponse().getResponseCode(),
                            workFlowWrapper);
                }
            }
        } catch (RemoteConnectFailureException e) {
            this.logger.error("Exception occured while reversing zong topup"
                    + ExceptionProcessorUtility.prepareExceptionStackTrace(e));
            throw new FrameworkCheckedException(
                    "Your request cannot be processed at the moment. Please try again later.");
        } catch (FrameworkCheckedException ex) {
            this.logger.error("Exception occured "
                    + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
            throw ex;
        } catch (Exception ex) {
            this.logger.error("Exception occured "
                    + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
            throw new FrameworkCheckedException(
                    "Your request cannot be processed at the moment. Please try again later.");
        } finally {
        }
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Ending doSale of ZongBillPaymentDispenser");
        }
        return workFlowWrapper;
    }

    public WorkFlowWrapper verify(WorkFlowWrapper workFlowWrapper)
            throws FrameworkCheckedException {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Inside getBillInfo of ZongBillPaymentDispenser");
        }
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Ending getBillInfo of ZongBillPaymentDispenser");
        }
        return workFlowWrapper;
    }

    private IBOSSSwitchController getRemoteServiceManager(String url) {
		return HttpInvokerUtil.getHttpInvokerFactoryBean(IBOSSSwitchController.class, url);
    }

    private void parseResponse(String responseCode,
            WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Inside parseResponse of MLDispenser");
        }
        boolean isPrepaidProduct = false;

        ProductDeviceFlowListViewModel productDeviceFlowModel = new ProductDeviceFlowListViewModel();
        productDeviceFlowModel.setProductId(workFlowWrapper.getProductModel()
                .getPrimaryKey());
        productDeviceFlowModel
                .setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);

        List<ProductDeviceFlowListViewModel> list = this.genericDAO
                .findEntityByExample(productDeviceFlowModel, null);
        if ((list != null) && (list.size() > 0)) {
            productDeviceFlowModel = (ProductDeviceFlowListViewModel) list
                    .get(0);
            if (productDeviceFlowModel.getDeviceFlowId().longValue() == DeviceFlowConstants.ZONG_TOPUP_FLOW
                    .longValue()) {
                isPrepaidProduct = true;
            } else if (productDeviceFlowModel.getDeviceFlowId().longValue() == DeviceFlowConstants.ZONG_BILL_PAYMENT_FLOW
                    .longValue()) {
                isPrepaidProduct = false;
            }
        } else if (this.logger.isDebugEnabled()) {
            this.logger
                    .debug("MLDispenser... NO MAPPING FOR ZONG PRODUCT IN PRODUCT_DEVICE_FLOW");
        }
        if ((responseCode != null) && ("1003".equalsIgnoreCase(responseCode))
                && (isPrepaidProduct)) {
            throw new FrameworkCheckedException(
                    "Invalid number for Zong Top Up. Please enter a correct pre-paid number.");
        }
        if ((responseCode != null) && ("1003".equalsIgnoreCase(responseCode))
                && (!isPrepaidProduct)) {
            throw new FrameworkCheckedException(
                    "Invalid number for Zong Bill Payment. Please enter a correct post paid number.");
        }
        throw new FrameworkCheckedException(
                "Your request cannot be processed at the moment. Please try again later.");
    }
}