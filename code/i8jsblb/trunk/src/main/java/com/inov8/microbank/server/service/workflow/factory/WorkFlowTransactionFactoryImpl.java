package com.inov8.microbank.server.service.workflow.factory;

import com.inov8.microbank.common.model.ServiceTypeModel;
import com.inov8.microbank.common.model.TransactionTypeModel;
import com.inov8.microbank.common.util.ServiceTypeConstantsInterface;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.workflow.controller.TransactionController;
import com.inov8.microbank.server.service.workflow.sales.AgentWalletToCoreTransaction;
import com.inov8.microbank.server.service.workflow.sales.ThirdPartyAccountOpeningTransaction;

/**
 *
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Maqsood Shahzad
 * @version 1.0
 */

public class WorkFlowTransactionFactoryImpl implements WorkFlowTransactionFactory
{
    private TransactionController operatorToDistributorTransaction;
    private TransactionController distributorToDistributorTransaction;
    private TransactionController distributorToRetailerTransaction;
    private TransactionController retailerToRetailerTransaction;
    private TransactionController billSaleTransaction;
    private TransactionController customerDiscreteProductSale;
    private TransactionController customerVariableProductSale;
    private TransactionController retailerDiscreteProductSale;
    private TransactionController retailerVariableProductSale;
    private TransactionController creditCardBillSaleTransaction;
    private TransactionController distibutorCreditRechargeTx;
    private TransactionController retailerCreditRechargeTx;
    private TransactionController allPayBillSaleTransaction;
    private TransactionController accountToCashTransaction;
    private TransactionController accountToCashLeg2Transaction;
    private TransactionController cashToCashTransaction;
    private TransactionController cashToCashLeg2Transaction;
    private TransactionController agentInternetBillPaymentTransaction;
    private TransactionController customerInternetBillPaymentTransaction;
    private TransactionController customerRetailPaymentTransaction;
    private TransactionController cashDepositTransaction;
    private TransactionController agentCashDepositTransaction;
    private TransactionController accountToAccountTransaction;
    private TransactionController utilityBillSaleTransaction;
    private TransactionController cashWithdrawalTransaction;
    private TransactionController hraCashWithdrawalTransaction;
    private TransactionController customerCashWithdrawalLeg2Transaction;
    private TransactionController transferInTransaction;
    private TransactionController transferOutTransaction;
    private TransactionController donationPaymentTransaction;
    private TransactionController rsoToRetailerTransaction;
    private TransactionController bulkBillPaymentTransaction;
    private TransactionController agentCreditCardPaymentTransaction;
    private TransactionController bulkPaymentLeg2Transaction;
    private TransactionController bulkPaymentTransaction;
    private TransactionController bbToCoreAccountTransaction;
    private TransactionController customerBBToCoreTransaction;
    private TransactionController cnicToCoreAccountTransaction;
    private TransactionController cnicToBBAccountTransaction;
    private TransactionController agentRetailPaymentTransaction;
    private TransactionController initialDepositTransaction;
    private TransactionController ibftTransaction;
    private TransactionController tellerCashInTransaction;
    private TransactionController tellerCashOutTransaction;
    private TransactionController senderRedeemTransaction;
    private TransactionController receiveCashTransaction;
    private TransactionController fonepayTransaction;
    private TransactionController customerAccToAccTransaction;
    private TransactionController customerAccToCashTransaction;
    private TransactionController customerBillPaymentTransaction;
    private TransactionController fonepaySettlementTransaction;
    private TransactionController collectionPaymentTransaction;
    private TransactionController customerCollectionPaymentTransaction;
    private TransactionController webServiceCashInTransaction;
    private TransactionController thirdPartyCashOutTransaction;
    private TransactionController debitCardCashWithDrawlTransaction;
    private TransactionController posRefundTransaction;
    private TransactionController hraToWalletTransaction;

    private TransactionController exciseAndTaxationTransaction;

    private TransactionController bopCashOutTransaction;
    private TransactionController thirdPartyAccountOpeningTransaction;
    private TransactionController proofOfLifeTransaction;
    private TransactionController bopCardIssuanceTransaction;
    private TransactionController advanceSalaryLoanTransaction;
    private TransactionController agentWalletToCoreTransaction;

    private TransactionController debitPaymentApiTransaction;
    private TransactionController feePaymentApiTransaction;

    private TransactionController advanceLoanPaymentTransaction;

    private TransactionController creditPaymentApiTransaction;
    private TransactionController coreToWalletTransaction;
    private TransactionController vcTransferTransaction;
    //added
    private  TransactionController bookMeTransaction;

    public TransactionController getCashToCashLeg2Transaction() {
        return cashToCashLeg2Transaction;
    }

    public void setCashToCashLeg2Transaction(
            TransactionController cashToCashLeg2Transaction) {
        this.cashToCashLeg2Transaction = cashToCashLeg2Transaction;
    }

    public WorkFlowTransactionFactoryImpl()
    {
    }

    /**
     * Factory method that returns a specific transaction object based on parameters wrapped in the wrapper
     * @param workFlowWrapper WorkFlowWrapper
     * @return TransactionControllerImpl
     */


    public TransactionController getTransactionProcessor(WorkFlowWrapper
                                                                 workFlowWrapper)throws Exception
    {
        ServiceTypeModel serviceTypeModel = workFlowWrapper.getServiceTypeModel();
        TransactionTypeModel txTypeModel = workFlowWrapper.getTransactionTypeModel();
        if (null != txTypeModel)
        {
            if (null != serviceTypeModel &&
                    ServiceTypeConstantsInterface.SERVICE_TYPE_DISCRETE.equals(serviceTypeModel.getServiceTypeId()) &&
                    TransactionTypeConstantsInterface.OPERATOR_TO_DISTR_TX.equals(txTypeModel.getTransactionTypeId()))
            {
                return operatorToDistributorTransaction;

            }
            else if (null != serviceTypeModel &&
                    ServiceTypeConstantsInterface.SERVICE_TYPE_DISCRETE.equals(serviceTypeModel.getServiceTypeId()) &&
                    TransactionTypeConstantsInterface.RET_TO_CUSTOMER_TX.equals(txTypeModel.getTransactionTypeId()))
            {
                return (TransactionController)retailerDiscreteProductSale;

            }
            else if(TransactionTypeConstantsInterface.BOP_CASH_OUT_TX.equals(txTypeModel.getTransactionTypeId()))
            {
                return (TransactionController)bopCashOutTransaction;
            }
            else if(TransactionTypeConstantsInterface.AGENT_IBFT_TX.equals(txTypeModel.getTransactionTypeId()))
            {
                return agentWalletToCoreTransaction;
            }
            else if(TransactionTypeConstantsInterface.DIST_TO_DIST_TX.equals(txTypeModel.getTransactionTypeId()))
            {
                return (TransactionController)distributorToDistributorTransaction;
            }
            else if(TransactionTypeConstantsInterface.DIST_TO_RET_TX.equals(txTypeModel.getTransactionTypeId()))
            {
                return (TransactionController)distributorToRetailerTransaction;
            }
            else if(TransactionTypeConstantsInterface.RET_TO_RET_TX.equals(txTypeModel.getTransactionTypeId()))
            {
                return (TransactionController)retailerToRetailerTransaction;
            }
            else if(TransactionTypeConstantsInterface.CUST_DISC_PRODUCT_SALE_TX.equals(txTypeModel.getTransactionTypeId()))
            {
                return (TransactionController)customerDiscreteProductSale;
            }
            else if(TransactionTypeConstantsInterface.OPERATOR_TO_DISTR_TX.equals(txTypeModel.getTransactionTypeId()))
            {
                return (TransactionController)operatorToDistributorTransaction;
            }
            else if(TransactionTypeConstantsInterface.CUST_VAR_PRODUCT_SALE_TX.equals(txTypeModel.getTransactionTypeId()))
            {
                return (TransactionController)customerVariableProductSale;
            }
            else if(TransactionTypeConstantsInterface.RET_VAR_PRODUCT_SALE_TX.equals(txTypeModel.getTransactionTypeId()))
            {
                return (TransactionController)retailerVariableProductSale;
            }
            else if(TransactionTypeConstantsInterface.RET_DISC_PRODUCT_SALE_TX.equals(txTypeModel.getTransactionTypeId()))
            {
                return (TransactionController)retailerDiscreteProductSale;
            }

            else if(TransactionTypeConstantsInterface.BILL_SALE_TX.equals(txTypeModel.getTransactionTypeId())  ||
                    TransactionTypeConstantsInterface.CUSTOMER_INTERNET_BILL_SALE_TX.equals(txTypeModel.getTransactionTypeId()) ||
                    TransactionTypeConstantsInterface.CUSTOMER_NADRA_BILL_SALE_TX.longValue() == txTypeModel.getTransactionTypeId().longValue() ||
                    TransactionTypeConstantsInterface.P2P_TRANSACTION.longValue() == txTypeModel.getTransactionTypeId().longValue()) {

                return (TransactionController)billSaleTransaction;
            }
            else if(TransactionTypeConstantsInterface.CREDIT_CARD_BILL_SALE_TX.equals(txTypeModel.getTransactionTypeId()))
            {
                return (TransactionController)creditCardBillSaleTransaction;
            }
            // Added by Jawwad ... For Distributor OLA credit recharge
            else if(TransactionTypeConstantsInterface.DIST_CREDIT_RECHARGE_TX.equals(txTypeModel.getTransactionTypeId()))
            {
                return (TransactionController)distibutorCreditRechargeTx;
            }
            // Added by Jawwad ... For Retailer OLA credit recharge
            else if(TransactionTypeConstantsInterface.RET_CREDIT_RECHARGE_TX.equals(txTypeModel.getTransactionTypeId()))
            {
                return (TransactionController)retailerCreditRechargeTx;
            }
            // Added by Jawwad ... For AllPay Retailer bill payment
            else if(  !workFlowWrapper.getIsCustomerInitiatedTransaction() && (TransactionTypeConstantsInterface.UTILITY_BILL_SALE.equals(txTypeModel.getTransactionTypeId()) ||
                    TransactionTypeConstantsInterface.AGENT_INTERNET_BILL_SALE_TX.equals(txTypeModel.getTransactionTypeId()) ||
                    TransactionTypeConstantsInterface.AGENT_NADRA_BILL_SALE_TX.equals(txTypeModel.getTransactionTypeId())) )
            {
                return (TransactionController) utilityBillSaleTransaction;
            }
            /* Added by Mudassir --  */
            else if(TransactionTypeConstantsInterface.ACCOUNT_TO_CASH_TX.equals(txTypeModel.getTransactionTypeId()))
            {
                return (TransactionController)accountToCashTransaction;
            }
            else if(TransactionTypeConstantsInterface.ACCOUNT_TO_CASH_LEG_2_TX.equals(txTypeModel.getTransactionTypeId()))
            {
                return (TransactionController)accountToCashLeg2Transaction;
            }
            else if(TransactionTypeConstantsInterface.CASH_TO_CASH.equals(txTypeModel.getTransactionTypeId()))
            {
                return this.cashToCashTransaction;
            }
            else if(TransactionTypeConstantsInterface.CASH_TO_CASH_LEG_2_TX.equals(txTypeModel.getTransactionTypeId()))
            {
                return this.cashToCashLeg2Transaction;
            }
            else if(!workFlowWrapper.getIsCustomerInitiatedTransaction() && TransactionTypeConstantsInterface.AGENT_INTERNET_BILL_SALE_TX.equals(txTypeModel.getTransactionTypeId()))
            {
                return this.agentInternetBillPaymentTransaction;
            }
            else if(TransactionTypeConstantsInterface.CUSTOMER_INTERNET_BILL_SALE_TX.equals(txTypeModel.getTransactionTypeId()))
            {
                return this.customerInternetBillPaymentTransaction;
            }else if(TransactionTypeConstantsInterface.CUSTOMER_RET_PAYMENT_TX.equals(txTypeModel.getTransactionTypeId()))
            {
                return this.customerRetailPaymentTransaction;

            } else if(TransactionTypeConstantsInterface.CASH_DEPOSIT_TX.equals(txTypeModel.getTransactionTypeId()))
            {
                return this.cashDepositTransaction;

            }
            //Agent Cash Deposit Transaction
            else if(TransactionTypeConstantsInterface.AGENT_CASH_DEPOSIT_TX.equals(txTypeModel.getTransactionTypeId()))
            {
                return this.agentCashDepositTransaction;

            }

            else if(TransactionTypeConstantsInterface.ACCOUT_TO_ACCOUNT_TX.equals(txTypeModel.getTransactionTypeId())){
                return this.accountToAccountTransaction;

            }else if(TransactionTypeConstantsInterface.CASH_WITHDRAWAL_TX.equals(txTypeModel.getTransactionTypeId())){
                return this.cashWithdrawalTransaction;

            }else if(TransactionTypeConstantsInterface.HRA_CASH_WITHDRAWAL_TX.equals(txTypeModel.getTransactionTypeId())){
                return this.hraCashWithdrawalTransaction;

            }else if(TransactionTypeConstantsInterface.CUSTOMER_INITIATED_CW_TX.equals(txTypeModel.getTransactionTypeId())){
                return this.customerCashWithdrawalLeg2Transaction;

            } else if(TransactionTypeConstantsInterface.TRANSFER_IN_TX.equals(txTypeModel.getTransactionTypeId())){

                return this.transferInTransaction;

            } else if(TransactionTypeConstantsInterface.TRANSFER_OUT_TX.equals(txTypeModel.getTransactionTypeId())){

                return this.transferOutTransaction;
            }
            else if(TransactionTypeConstantsInterface.DONATION_PAYMENT_TX.equals(txTypeModel.getTransactionTypeId())) {

                return this.donationPaymentTransaction;

            } else if(TransactionTypeConstantsInterface.RSO_TO_RET_TX.equals(txTypeModel.getTransactionTypeId())) {

                return (TransactionController)rsoToRetailerTransaction;
            }
            else if(TransactionTypeConstantsInterface.AGENT_BULK_BILL_SALE_TX.equals(txTypeModel.getTransactionTypeId())) {

                return this.bulkBillPaymentTransaction;

            }else if(TransactionTypeConstantsInterface.AGENT_CREDIT_CARD_PAYMENT_TX.equals(txTypeModel.getTransactionTypeId())) {
                return this.agentCreditCardPaymentTransaction;

            }else if(TransactionTypeConstantsInterface.BULK_PAYMENT_LEG_2_TX.equals(txTypeModel.getTransactionTypeId())) {

                return (TransactionController)bulkPaymentLeg2Transaction;

            } else if(TransactionTypeConstantsInterface.BULK_PAYMENT_TX.equals(txTypeModel.getTransactionTypeId())) {

                return (TransactionController)bulkPaymentTransaction;

            }  else if (TransactionTypeConstantsInterface.BB_TO_CORE_ACCOUNT_TX.equals(txTypeModel.getTransactionTypeId())) {

                return (TransactionController)bbToCoreAccountTransaction;

            }  else if (TransactionTypeConstantsInterface.CUSTOMER_INITIATED_BB_TO_CORE_ACCOUNT_TX.equals(txTypeModel.getTransactionTypeId())) {

                return (TransactionController)customerBBToCoreTransaction;

            }else if (TransactionTypeConstantsInterface.CNIC_TO_CORE_ACCOUNT_TX.equals(txTypeModel.getTransactionTypeId())) {

                return (TransactionController)cnicToCoreAccountTransaction;

            }else if (TransactionTypeConstantsInterface.CNIC_TO_BB_ACCOUNT_TX.equals(txTypeModel.getTransactionTypeId())) {

                return (TransactionController)cnicToBBAccountTransaction;

            }else if (TransactionTypeConstantsInterface.AGENT_RETAIL_PAYMENT_TX.equals(txTypeModel.getTransactionTypeId())) {

                return (TransactionController)agentRetailPaymentTransaction;
            }
            else if (TransactionTypeConstantsInterface.INITIAL_DEPOSIT_TX.equals(txTypeModel.getTransactionTypeId())) {
                return (TransactionController) initialDepositTransaction;
            }else if (TransactionTypeConstantsInterface.IBFT_TX.equals(txTypeModel.getTransactionTypeId())) {
                return (TransactionController) ibftTransaction;

            } else if (TransactionTypeConstantsInterface.SENDER_REDEEM_TX.equals(txTypeModel.getTransactionTypeId())) {
                return (TransactionController) senderRedeemTransaction;
            } else if (TransactionTypeConstantsInterface.Teller_CASH_IN_TX.equals(txTypeModel.getTransactionTypeId())) {
                return (TransactionController) tellerCashInTransaction;
            } else if (TransactionTypeConstantsInterface.Teller_CASH_OUT_TX.equals(txTypeModel.getTransactionTypeId())) {
                return (TransactionController) tellerCashOutTransaction;

            } else if (TransactionTypeConstantsInterface.RECEIVE_MONEY_TX.equals(txTypeModel.getTransactionTypeId())) {
                return (TransactionController) receiveCashTransaction;
            }
            else if (TransactionTypeConstantsInterface.WEB_SERVICE_PAYMENT_TX.equals(txTypeModel.getTransactionTypeId())) {
                return (TransactionController) fonepayTransaction;
            }
            else if (TransactionTypeConstantsInterface.VIRTUAL_CARD_PAYMENT_TX.equals(txTypeModel.getTransactionTypeId())) {
                return (TransactionController) fonepayTransaction;
            }
            else if (TransactionTypeConstantsInterface.AGENT_SATTELMENT_PAYMENT_TX.equals(txTypeModel.getTransactionTypeId())) {
                return (TransactionController) fonepaySettlementTransaction;
            }
            else if (TransactionTypeConstantsInterface.CUSTOMER_COLLECTION_PAYMENT_TX.equals(txTypeModel.getTransactionTypeId())) {
                return (TransactionController) customerCollectionPaymentTransaction;
            }
            else if (TransactionTypeConstantsInterface.CUSTOMER_INITIATED_ACCOUT_TO_ACCOUNT_TX.equals(txTypeModel.getTransactionTypeId())) {
                return (TransactionController) customerAccToAccTransaction;
            }
            else if (TransactionTypeConstantsInterface.CUSTOMER_INITIATED_ACCOUT_TO_CASH_TX.equals(txTypeModel.getTransactionTypeId())) {
                return (TransactionController) customerAccToCashTransaction;
            }
            else if( (TransactionTypeConstantsInterface.CUSTOMER_BILL_PAYMENT_TX.equals(txTypeModel.getTransactionTypeId())||
                    TransactionTypeConstantsInterface.AGENT_INTERNET_BILL_SALE_TX.equals(txTypeModel.getTransactionTypeId()) ||
                    TransactionTypeConstantsInterface.OFFLINE_BILL_PAYMENT_TX.equals(txTypeModel.getTransactionTypeId()) ||
                    TransactionTypeConstantsInterface.AGENT_NADRA_BILL_SALE_TX.equals(txTypeModel.getTransactionTypeId())
            ) && (workFlowWrapper.getIsCustomerInitiatedTransaction())  ){
                return (TransactionController) customerBillPaymentTransaction;
            }
            else if (TransactionTypeConstantsInterface.COLLECTION_PAYMENT_TX.equals(txTypeModel.getTransactionTypeId())) {
                return (TransactionController) collectionPaymentTransaction;
            } else if(TransactionTypeConstantsInterface.WEB_SERVICE_CASH_IN_TX.equals(txTypeModel.getTransactionTypeId())) {
                return (TransactionController)this.webServiceCashInTransaction;

            }else if(TransactionTypeConstantsInterface.THIRD_PARTY_CASH_OUT_TX.equals(txTypeModel.getTransactionTypeId())) {
                return (TransactionController)this.thirdPartyCashOutTransaction;

            }
            else if(TransactionTypeConstantsInterface.THIRD_PARTY_ACCOUNT_OPENING_TX.equals(txTypeModel.getTransactionTypeId())) {
                return (TransactionController)this.thirdPartyAccountOpeningTransaction;

            }
            else if(TransactionTypeConstantsInterface.PROOF_OF_LIFE_TX.equals(txTypeModel.getTransactionTypeId())) {
                return (TransactionController)this.proofOfLifeTransaction;

            }
            else if (TransactionTypeConstantsInterface.BOOK_ME_TX.equals(txTypeModel.getTransactionTypeId())){
                return (TransactionController) this.bookMeTransaction;
            }
            else if(TransactionTypeConstantsInterface.BOP_CARD_ISSUANCE_REISSUANCE_TX.equals(txTypeModel.getTransactionTypeId())) {
                return (TransactionController)this.bopCardIssuanceTransaction;

            }
            else if(TransactionTypeConstantsInterface.ADVANCE_SALARY_LOAN_TX.equals(txTypeModel.getTransactionTypeId())) {
                return (TransactionController)this.advanceSalaryLoanTransaction;

            }
            else if(TransactionTypeConstantsInterface.DEBIT_API_TX.equals(txTypeModel.getTransactionTypeId())) {
                return (TransactionController) this.debitPaymentApiTransaction;
            }
            else if(TransactionTypeConstantsInterface.FEE_PAYMENT_TX.equals(txTypeModel.getTransactionTypeId())) {
                return (TransactionController) this.feePaymentApiTransaction;
            }

            else if(TransactionTypeConstantsInterface.ADVANCE_LOAN_PAYMENT_TX.equals(txTypeModel.getTransactionTypeId())) {
                return (TransactionController) this.advanceLoanPaymentTransaction;
            }
            else if(TransactionTypeConstantsInterface.CREDIT_API_TX.equals(txTypeModel.getTransactionTypeId())) {
                return (TransactionController) this.creditPaymentApiTransaction;
            }
            else if(TransactionTypeConstantsInterface.DEBIT_CARD_CW_TX.equals(txTypeModel.getTransactionTypeId()))
                return (TransactionController) this.debitCardCashWithDrawlTransaction;
            else if(TransactionTypeConstantsInterface.POS_CUSTOMER_REFUND_TX.equals(txTypeModel.getTransactionTypeId()))
                return (TransactionController) this.posRefundTransaction;
            else if (TransactionTypeConstantsInterface.HRA_TO_WALLET_TX.equals(txTypeModel.getTransactionTypeId()))
                return (TransactionController) this.hraToWalletTransaction;
            else if(TransactionTypeConstantsInterface.EXCISE_AND_TAXATION_TX.equals(txTypeModel.getTransactionTypeId()))
                return (TransactionController) this.exciseAndTaxationTransaction;
            else if(TransactionTypeConstantsInterface.CORE_TO_WALLET_TX.equals(txTypeModel.getTransactionTypeId())) {
                return (TransactionController) this.coreToWalletTransaction;
            }
            else if(TransactionTypeConstantsInterface.VC_TRANSFER_TX.equals(txTypeModel.getTransactionTypeId())) {
                return (TransactionController) this.vcTransferTransaction;
            }

        }


        return null;
    }

    public void setAgentInternetBillPaymentTransaction(
            TransactionController agentInternetBillPaymentTransaction) {
        this.agentInternetBillPaymentTransaction = agentInternetBillPaymentTransaction;
    }

    public void setCustomerInternetBillPaymentTransaction(
            TransactionController customerInternetBillPaymentTransaction) {
        this.customerInternetBillPaymentTransaction = customerInternetBillPaymentTransaction;
    }

    public void setBillSaleTransaction(TransactionController billSaleTransaction)
    {
        this.billSaleTransaction = billSaleTransaction;
    }

    public void setCustomerDiscreteProductSale(TransactionController
                                                       customerDiscreteProductSale)
    {
        this.customerDiscreteProductSale = customerDiscreteProductSale;
    }

    public void setCustomerVariableProductSale(TransactionController
                                                       customerVariableProductSale)
    {
        this.customerVariableProductSale = customerVariableProductSale;
    }

    public void setDistributorToDistributorTransaction(TransactionController
                                                               distributorToDistributorTransaction)
    {
        this.distributorToDistributorTransaction =
                distributorToDistributorTransaction;
    }

    public void setDistributorToRetailerTransaction(TransactionController
                                                            distributorToRetailerTransaction)
    {
        this.distributorToRetailerTransaction = distributorToRetailerTransaction;
    }

    public void setOperatorToDistributorTransaction(TransactionController
                                                            operatorToDistributorTransaction)
    {
        this.operatorToDistributorTransaction = operatorToDistributorTransaction;
    }

    public void setRetailerDiscreteProductSale(TransactionController
                                                       retailerDiscreteProductSale)
    {
        this.retailerDiscreteProductSale = retailerDiscreteProductSale;
    }

    public void setRetailerToRetailerTransaction(TransactionController
                                                         retailerToRetailerTransaction)
    {
        this.retailerToRetailerTransaction = retailerToRetailerTransaction;
    }

    public void setRetailerVariableProductSale(TransactionController
                                                       retailerVariableProductSale)
    {
        this.retailerVariableProductSale = retailerVariableProductSale;
    }


    public void setCreditCardBillSaleTransaction(TransactionController creditCardBillSaleTransaction)
    {
        this.creditCardBillSaleTransaction = creditCardBillSaleTransaction;
    }


    public void setDistibutorCreditRechargeTx(TransactionController distibutorCreditRechargeTx)
    {
        this.distibutorCreditRechargeTx = distibutorCreditRechargeTx;
    }


    public void setRetailerCreditRechargeTx(TransactionController retailerCreditRechargeTx)
    {
        this.retailerCreditRechargeTx = retailerCreditRechargeTx;
    }

    public void setAllPayBillSaleTransaction(TransactionController allPayBillSaleTransaction)
    {
        this.allPayBillSaleTransaction = allPayBillSaleTransaction;
    }

    public void setAccountToCashTransaction(TransactionController accountToCashTransaction) {
        this.accountToCashTransaction = accountToCashTransaction;
    }

    public void setAccountToCashLeg2Transaction(
            TransactionController accountToCashLeg2Transaction) {
        this.accountToCashLeg2Transaction = accountToCashLeg2Transaction;
    }

    public void setCashToCashTransaction(TransactionController cashToCashTransaction) {
        this.cashToCashTransaction = cashToCashTransaction;
    }

    public void setCustomerRetailPaymentTransaction(
            TransactionController customerRetailPaymentTransaction) {
        this.customerRetailPaymentTransaction = customerRetailPaymentTransaction;
    }

    public void setDonationPaymentTransaction(TransactionController donationPaymentTransaction) {
        this.donationPaymentTransaction = donationPaymentTransaction;
    }

    public void setRsoToRetailerTransaction(
            TransactionController rsoToRetailerTransaction) {
        this.rsoToRetailerTransaction = rsoToRetailerTransaction;
    }
    public void setCashDepositTransaction(TransactionController cashDepositTransaction) {
        this.cashDepositTransaction = cashDepositTransaction;
    }

    public void setAccountToAccountTransaction(
            TransactionController accountToAccountTransaction) {
        this.accountToAccountTransaction = accountToAccountTransaction;
    }

    public void setUtilityBillSaleTransaction(TransactionController utilityBillSaleTransaction) {
        this.utilityBillSaleTransaction = utilityBillSaleTransaction;
    }

    public TransactionController getCashWithdrawalTransaction() {
        return cashWithdrawalTransaction;
    }

    public void setCashWithdrawalTransaction(
            TransactionController cashWithdrawalTransaction) {
        this.cashWithdrawalTransaction = cashWithdrawalTransaction;
    }

    public TransactionController getHraCashWithdrawalTransaction() {
        return hraCashWithdrawalTransaction;
    }

    public void setHraCashWithdrawalTransaction(
            TransactionController hraCashWithdrawalTransaction) {
        this.hraCashWithdrawalTransaction = hraCashWithdrawalTransaction;
    }

    public TransactionController getCustomerCashWithdrawalLeg2Transaction() {
        return customerCashWithdrawalLeg2Transaction;
    }

    public void setCustomerCashWithdrawalLeg2Transaction(TransactionController customerCashWithdrawalLeg2Transaction) {
        this.customerCashWithdrawalLeg2Transaction = customerCashWithdrawalLeg2Transaction;
    }

    public void setTransferInTransaction(TransactionController transferInTransaction) {
        this.transferInTransaction = transferInTransaction;
    }

    public TransactionController getTransferOutTransaction() {
        return transferOutTransaction;
    }

    public void setTransferOutTransaction(TransactionController transferOutTransaction) {
        this.transferOutTransaction = transferOutTransaction;
    }

    public void setBulkBillPaymentTransaction(TransactionController bulkBillPaymentTransaction) {
        this.bulkBillPaymentTransaction = bulkBillPaymentTransaction;
    }

    public void setAgentCreditCardPaymentTransaction(TransactionController agentCreditCardPaymentTransaction) {
        this.agentCreditCardPaymentTransaction = agentCreditCardPaymentTransaction;
    }

    public void setBulkPaymentLeg2Transaction(
            TransactionController bulkPaymentLeg2Transaction) {
        this.bulkPaymentLeg2Transaction = bulkPaymentLeg2Transaction;
    }

    public void setBulkPaymentTransaction(TransactionController bulkPaymentTransaction) {
        this.bulkPaymentTransaction = bulkPaymentTransaction;
    }

    public void setBbToCoreAccountTransaction(
            TransactionController bbToCoreAccountTransaction) {
        this.bbToCoreAccountTransaction = bbToCoreAccountTransaction;
    }

    public void setCustomerBBToCoreTransaction(TransactionController customerBBToCoreTransaction) {
        this.customerBBToCoreTransaction = customerBBToCoreTransaction;
    }

    public void setCnicToCoreAccountTransaction(
            TransactionController cnicToCoreAccountTransaction) {
        this.cnicToCoreAccountTransaction = cnicToCoreAccountTransaction;
    }

    public void setCnicToBBAccountTransaction(
            TransactionController cnicToBBAccountTransaction) {
        this.cnicToBBAccountTransaction = cnicToBBAccountTransaction;
    }

    public void setAgentRetailPaymentTransaction(
            TransactionController agentRetailPaymentTransaction) {
        this.agentRetailPaymentTransaction = agentRetailPaymentTransaction;
    }

    public TransactionController getInitialDepositTransaction() {
        return initialDepositTransaction;
    }

    public void setInitialDepositTransaction(TransactionController initialDepositTransaction) {
        this.initialDepositTransaction = initialDepositTransaction;
    }

    public TransactionController getIbftTransaction() {
        return ibftTransaction;
    }

    public void setIbftTransaction(TransactionController ibftTransaction) {
        this.ibftTransaction = ibftTransaction;
    }

    public TransactionController getSenderRedeemTransaction() {
        return senderRedeemTransaction;
    }

    public void setSenderRedeemTransaction(
            TransactionController senderRedeemTransaction) {
        this.senderRedeemTransaction = senderRedeemTransaction;
    }

    public TransactionController getTellerCashInTransaction() {
        return tellerCashInTransaction;
    }

    public void setTellerCashInTransaction(TransactionController tellerCashInTransaction) {
        this.tellerCashInTransaction = tellerCashInTransaction;
    }

    public void setTellerCashOutTransaction(TransactionController tellerCashOutTransaction) {
        this.tellerCashOutTransaction = tellerCashOutTransaction;
    }

    public void setReceiveCashTransaction(TransactionController receiveCashTransaction) {
        this.receiveCashTransaction = receiveCashTransaction;
    }

    public TransactionController getFonepayTransaction() {
        return fonepayTransaction;
    }

    public void setFonepayTransaction(TransactionController fonepayTransaction) {
        this.fonepayTransaction = fonepayTransaction;
    }

    public TransactionController getCustomerAccToAccTransaction() {
        return customerAccToAccTransaction;
    }

    public void setCustomerAccToAccTransaction(
            TransactionController customerAccToAccTransaction) {
        this.customerAccToAccTransaction = customerAccToAccTransaction;
    }

    public TransactionController getCustomerAccToCashTransaction() {
        return customerAccToCashTransaction;
    }

    public void setCustomerAccToCashTransaction(
            TransactionController customerAccToCashTransaction) {
        this.customerAccToCashTransaction = customerAccToCashTransaction;
    }

    public TransactionController getCustomerBillPaymentTransaction() {
        return customerBillPaymentTransaction;
    }

    public void setCustomerBillPaymentTransaction(
            TransactionController customerBillPaymentTransaction) {
        this.customerBillPaymentTransaction = customerBillPaymentTransaction;
    }


    public TransactionController getFonepaySettlementTransaction() {
        return fonepaySettlementTransaction;
    }

    public void setFonepaySettlementTransaction(TransactionController fonepaySettlementTransaction) {
        this.fonepaySettlementTransaction = fonepaySettlementTransaction;
    }


    public TransactionController getCollectionPaymentTransaction() {
        return collectionPaymentTransaction;
    }

    public void setCollectionPaymentTransaction(
            TransactionController collectionPaymentTransaction) {
        this.collectionPaymentTransaction = collectionPaymentTransaction;
    }


    public TransactionController getCustomerCollectionPaymentTransaction() {
        return customerCollectionPaymentTransaction;
    }

    public void setCustomerCollectionPaymentTransaction(TransactionController customerCollectionPaymentTransaction) {
        this.customerCollectionPaymentTransaction = customerCollectionPaymentTransaction;
    }
    public void setWebServiceCashInTransaction(TransactionController webServiceCashInTransaction) {
        this.webServiceCashInTransaction = webServiceCashInTransaction;
    }
    public void setThirdPartyCashOutTransaction(TransactionController thirdPartyCashOutTransaction) {
        this.thirdPartyCashOutTransaction = thirdPartyCashOutTransaction;
    }

    public void setDebitCardCashWithDrawlTransaction(TransactionController debitCardCashWithDrawlTransaction) {
        this.debitCardCashWithDrawlTransaction = debitCardCashWithDrawlTransaction;
    }

    public void setPosRefundTransaction(TransactionController POSRefundTransaction) {
        this.posRefundTransaction = POSRefundTransaction;
    }
    public void setAgentCashDepositTransaction(TransactionController agentCashDepositTransaction) {
        this.agentCashDepositTransaction = agentCashDepositTransaction;
    }


    public void setExciseAndTaxationTransaction(TransactionController exciseAndTaxationTransaction) {
        this.exciseAndTaxationTransaction = exciseAndTaxationTransaction;
    }

    public void setHraToWalletTransaction(TransactionController hraToWalletTransaction) {
        this.hraToWalletTransaction = hraToWalletTransaction;
    }

    public void setBopCashOutTransaction(TransactionController bopCashOutTransaction) {
        this.bopCashOutTransaction = bopCashOutTransaction;
    }

    public void setThirdPartyAccountOpeningTransaction(TransactionController thirdPartyAccountOpeningTransaction) {
        this.thirdPartyAccountOpeningTransaction = thirdPartyAccountOpeningTransaction;
    }

    public void setAgentWalletToCoreTransaction(TransactionController agentWalletToCoreTransaction) {
        this.agentWalletToCoreTransaction = agentWalletToCoreTransaction;
    }

    public void setBopCardIssuanceTransaction(TransactionController bopCardIssuanceTransaction) {
        this.bopCardIssuanceTransaction = bopCardIssuanceTransaction;
    }

    public void setAdvanceSalaryLoanTransaction(TransactionController advanceSalaryLoanTransaction) {
        this.advanceSalaryLoanTransaction = advanceSalaryLoanTransaction;
    }
    public void setProofOfLifeTransaction(TransactionController proofOfLifeTransaction) {
        this.proofOfLifeTransaction = proofOfLifeTransaction;
    }

    public void setBookMeTransaction(TransactionController bookMeTransaction) {
        this.bookMeTransaction = bookMeTransaction;
    }

    public void setDebitPaymentApiTransaction(TransactionController debitPaymentApiTransaction) {
        this.debitPaymentApiTransaction = debitPaymentApiTransaction;
    }

    public void setFeePaymentApiTransaction(TransactionController feePaymentApiTransaction) {
        this.feePaymentApiTransaction = feePaymentApiTransaction;
    }

    public void setAdvanceLoanPaymentTransaction(TransactionController advanceLoanPaymentTransaction) {
        this.advanceLoanPaymentTransaction = advanceLoanPaymentTransaction;
    }

    public void setCreditPaymentApiTransaction(TransactionController creditPaymentApiTransaction) {
        this.creditPaymentApiTransaction = creditPaymentApiTransaction;
    }

    public void setCoreToWalletTransaction(TransactionController coreToWalletTransaction) {
        this.coreToWalletTransaction = coreToWalletTransaction;
    }

    public void setVcTransferTransaction(TransactionController vcTransferTransaction) {
        this.vcTransferTransaction = vcTransferTransaction;
    }
}