package com.inov8.microbank.server.service.integration.dispenser;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.productmodule.paymentservice.BookMeLog;
import com.inov8.microbank.common.model.productmodule.paymentservice.LescoLogModel;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.facade.postedtransactionreportmodule.PostedTransactionReportFacadeImpl;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.failurelogmodule.FailureLogManager;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.financialintegrationmodule.PhoenixFinancialInstitutionImpl;
import com.inov8.microbank.server.service.integration.vo.BookMeTransactionVO;
import com.inov8.microbank.server.service.integration.vo.UtilityBillVO;
import com.inov8.microbank.server.service.inventorymodule.ProductUnitManager;
import com.inov8.microbank.server.service.inventorymodule.ShipmentManager;
import com.inov8.microbank.server.service.productmodule.ProductIntgModuleInfoManager;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.settlementmodule.SettlementManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;
import com.inov8.microbank.server.service.suppliermodule.SupplierBankInfoManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import java.util.Date;

public class BookMePaymentDispenser extends BillPaymentProductDispenser {

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
    private StakeholderBankInfoManager stakeholderBankInfoManager;

    private final Log logger = LogFactory.getLog(this.getClass());

    @Override
    public WorkFlowWrapper getBillInfo(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
        return null;
    }

    @Override
    public WorkFlowWrapper verify(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
        return workFlowWrapper;
    }

    @Override
    public WorkFlowWrapper doSale(WorkFlowWrapper workFlowWrapper) throws Exception {
        if(logger.isDebugEnabled()) {
            logger.debug("Start of doSale of BookMe Payment Dispenser");
        }

        workFlowWrapper = doBillPaymentWithoutQueue(workFlowWrapper);

        return workFlowWrapper;    }

    @Override
    public WorkFlowWrapper rollback(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
        return null;
    }

    public BookMePaymentDispenser(CommissionManager commissionManager,
                                  SmartMoneyAccountManager smartMoneyAccountManager, SettlementManager settlementManager,
                                  ProductManager productManager, AppUserManager appUserManager,
                                  ProductUnitManager productUnitManager, ShipmentManager shipmentManager, GenericDao genericDAO, ApplicationContext ctx)
    {
        this.commissionManager = commissionManager;
        this.smartMoneyAccountManager = smartMoneyAccountManager;
        this.settlementManager = settlementManager;
        this.productManager = productManager;
        this.appUserManager = appUserManager;
        this.productUnitManager = productUnitManager;
        this.shipmentManager = shipmentManager;
        this.genericDAO = genericDAO ;

        this.ctx=ctx;

        super.financialIntegrationManager = (FinancialIntegrationManager) ctx.getBean("financialIntegrationManager");

        this.setFailureLogManager((FailureLogManager) ctx.getBean("failureLogManager"));
        super.setAuditLogModule((FailureLogManager) ctx.getBean("failureLogManager"));
        this.setStakeholderBankInfoManager((StakeholderBankInfoManager)ctx.getBean("stakeholderBankInfoManager"));
        super.setSupplierBankInfoManager((SupplierBankInfoManager)ctx.getBean("supplierBankInfoManager"));

        super.setStakeholderBankInfoManager(stakeholderBankInfoManager);
        super.setPhoenixFinancialInstitution((PhoenixFinancialInstitutionImpl) ctx.getBean("com.inov8.microbank.server.service.financialintegrationmodule.PhoenixFinancialInstitutionImpl")) ;
        super.setPostedTransactionReportFacade((PostedTransactionReportFacadeImpl)ctx.getBean("postedTransactionReportFacade"));
    }

    public void setFailureLogManager(FailureLogManager failureLogManager) {
        this.failureLogManager = failureLogManager;
    }

    public void setCtx(ApplicationContext ctx) {
        this.ctx = ctx;
    }
}
