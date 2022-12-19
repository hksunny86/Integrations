package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.financialintegrationmodule.OLAVeriflyFinancialInstitutionImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import com.inov8.microbank.server.service.integration.vo.AccountToAccountVO;
import com.inov8.microbank.server.service.messagemodule.NotificationMessageManager;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;
import com.inov8.microbank.server.service.workflow.sales.SalesTransaction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class VCTransferTransaction extends SalesTransaction {
    protected final Log log = LogFactory.getLog(getClass());
    private ProductManager productManager;
    private NotificationMessageManager notificationMessageManager;
    private FinancialIntegrationManager financialIntegrationManager;
    private ESBAdapter esbAdapter;
    private StakeholderBankInfoManager stakeholderBankInfoManager;


    @Override
    protected WorkFlowWrapper doPreProcess(WorkFlowWrapper wrapper) throws Exception
    {
        logger.debug("Inside doPreProcess(WorkFlowWrapper wrapper) of CustomerInitiatedAccountToAccountTransaction....");

        wrapper = super.doPreProcess(wrapper);

	/*	if (wrapper.getIsIvrResponse())                                //comment by tayyab
			return wrapper;*/

        TransactionModel txModel = wrapper.getTransactionModel();

        txModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
        txModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
        txModel.setTransactionAmount(wrapper.getTransactionAmount());
        txModel.setTotalAmount(wrapper.getTransactionAmount());
		/*txModel.setRetailerId(wrapper.getFromRetailerContactModel().getRetailerId());                                           //comment by tayyab
		txModel.setDistributorId(wrapper.getFromRetailerContactModel().getRetailerIdRetailerModel().getDistributorId());
		txModel.setFromRetContactMobNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
		txModel.setFromRetContactName(ThreadLocalAppUser.getAppUserModel().getFirstName() + " " + ThreadLocalAppUser.getAppUserModel().getLastName() ) ;*/


        txModel.setTotalCommissionAmount(0d);
        txModel.setDiscountAmount(0d);

        // Transaction Type model of transaction is populated
        txModel.setTransactionTypeIdTransactionTypeModel(wrapper.getTransactionTypeModel());

        // Sets the Device type
//        txModel.setDeviceTypeId(wrapper.getDeviceTypeModel().getDeviceTypeId());
//        txModel.setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());

        // Sender Customer model of transaction is populated
//        txModel.setCustomerIdCustomerModel(new CustomerModel());
//        txModel.getCustomerIdCustomerModel().setCustomerId(wrapper.getCustomerModel().getCustomerId());

        //Sender Customer Smart Money Account Id is populated
//        txModel.setSmartMoneyAccountId(wrapper.getOlaSmartMoneyAccountModel().getSmartMoneyAccountId());

        // Payment mode model of transaction is populated
//        txModel.setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());

//        txModel.setCustomerMobileNo(((AccountToAccountVO)wrapper.getProductVO()).getSenderCustomerMobileNo());
//        txModel.setSaleMobileNo(((AccountToAccountVO)wrapper.getProductVO()).getSenderCustomerMobileNo());

        // Populate processing Bank Id
        txModel.setProcessingBankId(BankConstantsInterface.ASKARI_BANK_ID);
//        txModel.setMfsId(wrapper.getUserDeviceAccountModel().getUserId());

        wrapper.setTransactionModel(txModel);
        if (logger.isDebugEnabled())
        {
            logger.debug("Ending doPreProcess(WorkFlowWrapper wrapper) of CustomerInitiatedAccountToAccountTransaction....");
        }

        return wrapper;
    }

    @Override
    protected WorkFlowWrapper doPreStart(WorkFlowWrapper wrapper) throws Exception
    {

        BaseWrapper baseWrapper = new BaseWrapperImpl();

        wrapper = super.doPreStart(wrapper);

        baseWrapper.setBasePersistableModel(wrapper.getProductModel());
        baseWrapper = productManager.loadProduct(baseWrapper);
        wrapper.setProductModel((ProductModel) baseWrapper.getBasePersistableModel());

        // --Setting instruction and success Message
        NotificationMessageModel notificationMessage = new NotificationMessageModel();
        notificationMessage.setNotificationMessageId(wrapper.getProductModel().getInstructionId());
        baseWrapper.setBasePersistableModel(notificationMessage);
        baseWrapper = this.notificationMessageManager.loadNotificationMessage(baseWrapper);
        wrapper.setInstruction((NotificationMessageModel) baseWrapper.getBasePersistableModel());

        NotificationMessageModel successMessage = new NotificationMessageModel();
        successMessage.setNotificationMessageId(wrapper.getProductModel().getSuccessMessageId());
        baseWrapper.setBasePersistableModel(successMessage);
        baseWrapper = this.notificationMessageManager.loadNotificationMessage(baseWrapper);
        wrapper.setSuccessMessage((NotificationMessageModel) baseWrapper.getBasePersistableModel());

        if (logger.isDebugEnabled())
        {
            logger.debug("Ending doPreStart(WorkFlowWrapper wrapper) of CustomerInitiatedAccountToAccountTransaction....");
        }
        return wrapper;
    }

    @Override
    protected WorkFlowWrapper doValidate(WorkFlowWrapper wrapper) throws Exception {
        return wrapper;
    }

    @Override
    protected WorkFlowWrapper doSale(WorkFlowWrapper wrapper) throws Exception {
        TransactionDetailModel txDetailModel = new TransactionDetailModel();
        if (logger.isDebugEnabled())
        {
            logger.debug("Inside doSale(WorkFlowWrapper wrapper) of CustomerInitiatedAccountToAccountTransaction..");
        }

        AbstractFinancialInstitution olaVeriflyFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitutionByClassName(OLAVeriflyFinancialInstitutionImpl.class.getName());

        txDetailModel.setProductIdProductModel(wrapper.getProductModel());
        wrapper.setTransactionDetailModel(txDetailModel);
        wrapper.getTransactionModel().addTransactionIdTransactionDetailModel(txDetailModel);

        BaseWrapper baseWrapper = new BaseWrapperImpl();
        BankModel bankModel = new BankModel();
        bankModel.setBankId(BankConstantsInterface.OLA_BANK_ID);
        baseWrapper.setBasePersistableModel(bankModel);
//        AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);
        OLAVeriflyFinancialInstitutionImpl olaFinancialInstitution = (OLAVeriflyFinancialInstitutionImpl) this.financialIntegrationManager.
                loadFinancialInstitution(baseWrapper);

        SwitchWrapper switchWrapper = new SwitchWrapperImpl();
        switchWrapper.setWorkFlowWrapper(wrapper);
        switchWrapper.setBankId(BankConstantsInterface.ASKARI_BANK_ID);
        switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);

        StakeholderBankInfoModel stakeholderBankInfoModel = stakeholderBankInfoManager.loadStakeholderBankInfoModel
                (PoolAccountConstantsInterface.VIRTUAL_PREPAID_CARD_INTERNAL_T24);
        String bulkSundryCoreAccountNumber = stakeholderBankInfoModel.getAccountNo();

        StakeholderBankInfoModel stakeholderBankInfoModel1 = stakeholderBankInfoManager.loadStakeholderBankInfoModel
                (PoolAccountConstantsInterface.VIRTUAL_PREPAID_CARD_SETTLEMENT);
        String toAccountNo = stakeholderBankInfoModel1.getAccountNo();


        switchWrapper.setFromAccountNo(bulkSundryCoreAccountNumber);
        switchWrapper.setToAccountNo(toAccountNo);
        switchWrapper = olaFinancialInstitution.VCFundsTransfer(switchWrapper) ;

        wrapper.setOLASwitchWrapper(switchWrapper);

        wrapper.getTransactionModel().setProcessingSwitchId(switchWrapper.getSwitchSwitchModel().getSwitchId());
        wrapper.getTransactionModel().setBankAccountNo(switchWrapper.getFromAccountNo());
        wrapper.getTransactionModel().setNotificationMobileNo("");
        wrapper.getTransactionDetailModel().setCustomField2(switchWrapper.getToAccountNo());
        wrapper.getTransactionDetailModel().setCustomField3(""+switchWrapper.getSwitchSwitchModel().getSwitchId());
        wrapper.getTransactionModel().setConfirmationMessage(" _ ");
        wrapper.getTransactionModel().setDeviceTypeId(DeviceTypeConstantsInterface.BULK_DISBURSEMENT);
        wrapper.getTransactionModel().setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        wrapper.getTransactionDetailModel().setSettled(true);
        txManager.saveTransaction(wrapper);
        wrapper.setSwitchWrapper(switchWrapper);

        return wrapper;
    }

    @Override
    protected WorkFlowWrapper logTransaction(WorkFlowWrapper wrapper) throws Exception {
        return wrapper;
    }

    @Override
    protected WorkFlowWrapper doEnd(WorkFlowWrapper wrapper) throws Exception
    {
        return wrapper;
    }

    @Override
    protected WorkFlowWrapper doPostRollback(WorkFlowWrapper wrapper) throws Exception
    {
        // TODO Auto-generated method stub
        return wrapper;
    }

    @Override
    protected WorkFlowWrapper doPreRollback(WorkFlowWrapper wrapper) throws Exception
    {
        // TODO Auto-generated method stub
        return wrapper;
    }

    @Override
    protected WorkFlowWrapper doRollback(WorkFlowWrapper wrapper) throws Exception
    {
        return wrapper;
    }

    @Override
    public WorkFlowWrapper rollback(WorkFlowWrapper wrapper) throws Exception
    {
//		logger.info("[AccountToAccountTransaction.rollback] rollback called...");
//		try{
//			wrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.FAILED);
//			wrapper.getTransactionModel().setTransactionId(null);
//			wrapper.getTransactionDetailModel().setTransactionDetailId(null);
//			txManager.saveTransaction(wrapper);
//		}catch(Exception ex){
//			logger.error("Unable to save Transaction details in case of rollback: \n"+ ex.getStackTrace());
//		}
//
        logger.info("[VCTransferTransaction.rollback] rollback complete...");
        return wrapper;
    }

    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
    }

    public void setNotificationMessageManager(NotificationMessageManager notificationMessageManager) {
        this.notificationMessageManager = notificationMessageManager;
    }

    public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager) {
        this.financialIntegrationManager = financialIntegrationManager;
    }

    public void setEsbAdapter(ESBAdapter esbAdapter) {
        this.esbAdapter = esbAdapter;
    }

    public void setStakeholderBankInfoManager(StakeholderBankInfoManager stakeholderBankInfoManager) {
        this.stakeholderBankInfoManager = stakeholderBankInfoManager;
    }
}
