package com.inov8.microbank.server.service.commissionmodule;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import com.inov8.integration.common.model.AccountModel;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.vo.product.CommissionShSharesDefaultVO;
import com.inov8.microbank.common.vo.product.CommissionShSharesVO;
import com.inov8.microbank.common.vo.product.CommissionStakeholderVO;
import com.inov8.microbank.server.dao.productmodule.CommissionThresholdRateDAO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import com.inov8.ola.util.LimitTypeConstants;
import com.inov8.ola.util.TransactionTypeConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.MatchMode;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.model.agenthierarchy.DistributorCommShareViewModel;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.dao.commissionmodule.AgentCommissionSummaryDAO;
import com.inov8.microbank.server.dao.commissionmodule.CommissionRateDAO;
import com.inov8.microbank.server.dao.commissionmodule.CommissionShSharesDAO;
import com.inov8.microbank.server.dao.commissionmodule.CommissionStakeholderDAO;
import com.inov8.microbank.server.dao.commissionmodule.CommissionTransactionDAO;
import com.inov8.microbank.server.dao.commissionmodule.UnsettledAgentCommDAO;
import com.inov8.microbank.server.dao.commissionmodule.hibernate.CommissionRateDefaultHibernateDAO;
import com.inov8.microbank.server.dao.commissionmodule.hibernate.CommissionTransactionHibernateDAO;
import com.inov8.microbank.server.dao.mfsmodule.UserDeviceAccountsDAO;
import com.inov8.microbank.server.service.agenthierarchy.AgentNetworkCommShareManager;
import com.inov8.microbank.server.service.commissionstakeholdermodule.CommissionStakeholderManager;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.settlementmodule.SettlementManager;
import com.inov8.microbank.tax.common.TaxConstantsInterface;
import com.inov8.microbank.tax.model.FEDRuleViewModel;
import com.inov8.microbank.tax.model.WHTConfigModel;
import com.inov8.microbank.tax.service.TaxManager;
import org.jpos.iso.IF_CHAR;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

/**
 * <p>Title: </p>
 *
 * <p>Description: This is the main class for Commission Module. The pupose of the
 * class is to return the calculated commissions i.e. Total Commission Amount, Transaction Amount
 * Billing Organization Amount and Total Amount along with the details</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Syed Ahmad Bilal
 * @version 1.0
 */

/**
 * Re-structured by
 *
 * @author Jawwad Farooq
 */

public class CommissionManagerImpl
        implements CommissionManager {
    private CommissionRateDAO commissionRateDAO;
    private CommissionShSharesDAO commissionShSharesDAO;
    private CommissionTransactionDAO commissionTransactionDAO;
    private CommissionTransactionHibernateDAO commissionTransactionHibernateDAO;
    private AgentCommissionSummaryDAO agentCommissionSummaryDAO;
    private CommissionStakeholderManager commissionStakeholderManager;
    private CommissionRateDefaultHibernateDAO commissionRateDefaultDAO;
    private AgentNetworkCommShareManager agentNetworkCommShareManager;
    protected SettlementManager settlementManager;
    private ProductManager productManager;
    private UnsettledAgentCommDAO unsettledAgentCommDAO;
    private UserDeviceAccountsDAO userDeviceAccountsDAO;
    private TaxManager taxManager;
    private CommissionStakeholderDAO commissionStakeholderDAO;
    private CommissionThresholdRateDAO commissionThresholdRateDAO;

    protected final transient Log logger = LogFactory.getLog(CommissionManagerImpl.class);

    public CommissionManagerImpl() {
    }

    public CommissionRateDAO getCommissionRateDAO() {
        return commissionRateDAO;
    }

    public void setCommissionRateDAO(CommissionRateDAO commissionRateDAO) {
        this.commissionRateDAO = commissionRateDAO;
    }

    /**
     * This method is used for calculating the commission. Its a caller method who use
     * other utility methods in teh class for returning the results.
     *
     * @param commissionWrapper CommissionWrapper
     * @return CommissionWrapper which contains the HashMap. This HashMap includes the desired
     * results in form of key value payers. i.e. commissionRateDetailsList -> Detail Records
     * commissionAmountsHolder -> An object of bean class from which we can get the desired
     * commission amounts.i.e. Total Amount, Total Commission Amount, Transaction Amount and Total Biling Organization Amount
     * @throws FrameworkCheckedException
     */

    @SuppressWarnings("unchecked")
    public CommissionWrapper calculateCommission(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Inside calculateCommission method of CommissionManagerImpl");
        }
        if (ThreadLocalAppUser.getAppUserModel() != null && ThreadLocalAppUser.getAppUserModel().getAppUserTypeId() != null
                && ThreadLocalAppUser.getAppUserModel().getAppUserTypeId().equals(UserTypeConstantsInterface.RETAILER))
            logger.info("Going to Calculate Commission in CommissionManagerImpl.calculateCommission() for Product-ID: " + workFlowWrapper.getProductModel().getProductId()
                    + " Mobile #: " + ThreadLocalAppUser.getAppUserModel().getMobileNo() + " at Time: " + new Date());
        else
            logger.info("Going to Calculate Commission in CommissionManagerImpl.calculateCommission() for Product-ID: " + workFlowWrapper.getProductModel().getProductId()
                    + " Mobile #: " + ThreadLocalAppUser.getAppUserModel().getMobileNo() + " at Time: " + new Date());
        CommissionAmountsHolder commissionAmountsHolder = new CommissionAmountsHolder();
        CommissionWrapper commissionWrapper = new CommissionWrapperImpl();
        Double transactionAmount = workFlowWrapper.getTransactionModel().getTransactionAmount();
        commissionAmountsHolder.setTransactionAmount(transactionAmount);
        commissionAmountsHolder.setTotalAmount(transactionAmount);
        commissionAmountsHolder.setBillingOrganizationAmount(transactionAmount);
        commissionAmountsHolder.setProductId(workFlowWrapper.getProductModel().getProductId());
        commissionAmountsHolder.setTotalCommissionAmount(new Double(0.0));
        commissionAmountsHolder.setTransactionProcessingAmount(new Double(0.0));
        commissionAmountsHolder.setServiceCharges(new Double(0.0));
        commissionAmountsHolder.setSupplierCharges(new Double(0.0));
        commissionAmountsHolder.setAgent1CommissionAmount(new Double(0.0));
        commissionAmountsHolder.setAgent2CommissionAmount(new Double(0.0));
        commissionAmountsHolder.setAskariCommissionAmount(new Double(0.0));
        commissionAmountsHolder.setFedCommissionAmount(new Double(0.0));
        commissionAmountsHolder.setI8CommissionAmount(new Double(0.0));
        commissionAmountsHolder.setWhtCommissionAmount(new Double(0.0));
        commissionAmountsHolder.setZongCommissionAmount(new Double(0.0));
        commissionAmountsHolder.setFranchise1CommissionAmount(new Double(0.0));
        commissionAmountsHolder.setFranchise2CommissionAmount(new Double(0.0));
        commissionAmountsHolder.setInclusiveFixAmount(new Double(0.0));
        commissionAmountsHolder.setInclusivePercentAmount(new Double(0.0));
        commissionAmountsHolder.setExclusiveFixAmount(new Double(0.0));
        commissionAmountsHolder.setExclusivePercentAmount(new Double(0.0));
        HashMap commissionModuleHashMap = new HashMap();
        List<CommissionRateModel> commissionRateList = null;
        if (workFlowWrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.AGENT_TO_AGENT_TRANSFER) {
            AgentTransferRuleModel ruleModel = workFlowWrapper.getAgentTransferRuleModel();
            CommissionRateModel rateModel = this.setCommissionRateModel(workFlowWrapper);
            rateModel.setProductId(workFlowWrapper.getProductModel().getProductId());
            rateModel.setInclusiveFixAmount(ruleModel.getInclusiveFixAmount());
            rateModel.setInclusivePercentAmount(ruleModel.getInclusivePercentAmount());
            rateModel.setExclusiveFixAmount(ruleModel.getExclusiveFixAmount());
            rateModel.setExclusivePercentAmount(ruleModel.getExclusivePercentAmount());
            rateModel.setActive(Boolean.TRUE);
            rateModel.setRangeStarts(ruleModel.getRangeStarts());
            rateModel.setRangeEnds(ruleModel.getRangeEnds());
            commissionRateList = new ArrayList<CommissionRateModel>();
            commissionRateList.add(rateModel);
        } else {

            CommissionRateModel vo = this.setCommissionRateModel(workFlowWrapper);
            commissionRateList = this.getCommissionRateList(vo);

            boolean iDefauleRateApplied = checkCommissionSlabsForDefaultRates(commissionRateList, transactionAmount);
            if (iDefauleRateApplied) {//If rates are not defined for this segment/Agent Network/Channel, then load default rates
                CommissionRateDefaultModel defaultRateModel = new CommissionRateDefaultModel();
                defaultRateModel.setProductId(workFlowWrapper.getProductModel().getProductId());
                List<CommissionRateDefaultModel> defaultRateList = commissionRateDefaultDAO.loadDefaultCommissionRateList(defaultRateModel);
                if (!CollectionUtils.isEmpty(defaultRateList)) {
                    commissionRateList = new ArrayList<CommissionRateModel>();
                    for (CommissionRateDefaultModel defaultModel : defaultRateList) {
                        CommissionRateModel rateModel = this.setCommissionRateModel(workFlowWrapper);
                        rateModel.setProductId(defaultModel.getProductId());
                        rateModel.setInclusiveFixAmount(defaultModel.getInclusiveFixAmount());
                        rateModel.setInclusivePercentAmount(defaultModel.getInclusivePercentAmount());
                        rateModel.setExclusiveFixAmount(defaultModel.getExclusiveFixAmount());
                        rateModel.setExclusivePercentAmount(defaultModel.getExclusivePercentAmount());
                        rateModel.setActive(Boolean.TRUE);
                        rateModel.setRangeStarts(0d);//TODO: remove hardcoded value and use another way to provode ranges
                        rateModel.setRangeEnds(9999999d);//TODO: remove hardcoded value and use another way to provode ranges
                        commissionRateList.add(rateModel);
                    }
                }
            }

        }//end else of AGENT_TO_AGENT_TRANSFER

        //Start New Module Charges Implementation Added by Abubakar
        workFlowWrapper.putObject("GIVEN_AMOUNT", commissionAmountsHolder.getTotalAmount());
        Double chargedAmount = setCommissionThresholdRateModel(workFlowWrapper);

        if(chargedAmount != 0.0 && chargedAmount > 0.0D) {
            if(workFlowWrapper.getCommissionThresholdRate() != null && chargedAmount.equals(workFlowWrapper.getCommissionThresholdRate().getMaxThresholdAmount())) {
                commissionAmountsHolder.setTotalAmount(commissionAmountsHolder.getTotalAmount() + chargedAmount);
                commissionAmountsHolder.setTransactionProcessingAmount(chargedAmount);
                commissionAmountsHolder.setTotalCommissionAmount(chargedAmount);
            }
            else{
                commissionAmountsHolder.setTotalAmount(commissionAmountsHolder.getTotalAmount() + chargedAmount);
                commissionAmountsHolder.setTransactionProcessingAmount(commissionAmountsHolder.getTransactionProcessingAmount() + chargedAmount);
                commissionAmountsHolder.setTotalCommissionAmount(commissionAmountsHolder.getTotalCommissionAmount() + chargedAmount);
            }
            workFlowWrapper.setChargedAmount(chargedAmount);
//            this.processCommissionThresholdRate(workFlowWrapper.getCommissionThresholdRate(), commissionAmountsHolder, workFlowWrapper);
        }
        //End New Module Charges Implementation Added by Abubakar

        Iterator<CommissionRateModel> listIterator = commissionRateList.iterator();
        CommissionRateModel commissionRate;

        boolean rateApplied = false;

        while (listIterator.hasNext()) {
            commissionRate = (CommissionRateModel) listIterator.next();

            if ((commissionRate.getRangeEnds() != null && transactionAmount > commissionRate.getRangeEnds()) || !commissionRate.getActive()) {
                listIterator.remove();
            } else if ((commissionRate.getRangeStarts() != null && transactionAmount < commissionRate.getRangeStarts())) {
                listIterator.remove();
            } else if (commissionRate.getRangeEnds() != null
                    && transactionAmount <= commissionRate.getRangeEnds()
                    && commissionRate.getRangeStarts() != null
                    && transactionAmount >= commissionRate.getRangeStarts()) {

                rateApplied = true;
                this.processCommissionRate(commissionRate, commissionAmountsHolder, workFlowWrapper);

                StringBuilder sb = new StringBuilder();
                sb.append("[CommissionManagerImpl.CalculateCommission] Calculating Commissions for Product ID: " + workFlowWrapper.getProductModel().getProductId() +
                        " Transaction Amount: " + transactionAmount +
                        " LoggedIn AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() +
                        " Commission Rate ID: " + commissionRate.getCommissionRateId() + ".Stakeholder Commissions:");


                Map<Long, TaxValueBean> taxMap = commissionAmountsHolder.getStakeholderTaxMap();
                Map<Long, TaxValueBean> hierarchyTaxMap = commissionAmountsHolder.getHierarchyStakeholderTaxMap();
                TaxValueBean taxBean = null;

                for (Map.Entry<Long, Double> stakeholderShareEntry : commissionAmountsHolder.getStakeholderCommissionsMap().entrySet()) {
                    taxBean = taxMap.get(stakeholderShareEntry.getKey());
                    if (taxBean != null) {
                        sb.append("\nStakeholderID ").append(stakeholderShareEntry.getKey()).append(" --> ").append(stakeholderShareEntry.getValue())
                                .append(" -- FED ").append(taxBean.getFedAmount())
                                .append(" -- WHT ").append(taxBean.getWhtAmount());
                    }
                }

                for (Map.Entry<Long, Double> hierarchyStakeholderEntry : commissionAmountsHolder.getHierarchyStakeholderCommissionMap().entrySet()) {
                    taxBean = hierarchyTaxMap.get(hierarchyStakeholderEntry.getKey());
                    if (taxBean != null) {
                        sb.append("\nHierarchy AppUserID ").append(hierarchyStakeholderEntry.getKey()).append(" --> ").append(hierarchyStakeholderEntry.getValue())
                                .append(" -- FED ").append(taxBean.getFedAmount())
                                .append(" -- WHT ").append(taxBean.getWhtAmount());
                    }
                }

                logger.info(sb.toString());
            }

        }

        if (rateApplied == false) {//If rates are not defined for this segment/Agent Network/Channel, then load default rates
            CommissionRateDefaultModel defaultRateModel = new CommissionRateDefaultModel();
            defaultRateModel.setProductId(workFlowWrapper.getProductModel().getProductId());
            List<CommissionRateDefaultModel> defaultRateList = commissionRateDefaultDAO.loadDefaultCommissionRateList(defaultRateModel);

            if (!CollectionUtils.isEmpty(defaultRateList)) {
                commissionRateList = new ArrayList<CommissionRateModel>();
                for (CommissionRateDefaultModel defaultModel : defaultRateList) {
                    CommissionRateModel rateModel = new CommissionRateModel();
                    rateModel.setProductId(defaultModel.getProductId());
                    rateModel.setInclusiveFixAmount(defaultModel.getInclusiveFixAmount());
                    rateModel.setInclusivePercentAmount(defaultModel.getInclusivePercentAmount());
                    rateModel.setExclusiveFixAmount(defaultModel.getExclusiveFixAmount());
                    rateModel.setExclusivePercentAmount(defaultModel.getExclusivePercentAmount());
                    rateModel.setActive(Boolean.TRUE);//TRUE
                    rateModel.setRangeStarts(1d);//TODO: remove hardcoded value and use another way to provode ranges
                    rateModel.setRangeEnds(9999999d);//TODO: remove hardcoded value and use another way to provode ranges
                    commissionRateList.add(rateModel);
                }
            }
        }

        commissionModuleHashMap.put(CommissionConstantsInterface.COMMISSION_RATE_LIST, commissionRateList);
        commissionModuleHashMap.put(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER, commissionAmountsHolder);
        commissionWrapper.setCommissionWrapperHashMap(commissionModuleHashMap);
        if (logger.isDebugEnabled()) {
            logger.debug("Ending calculateCommission method of CommissionManagerImpl");
        }

        if (ThreadLocalAppUser.getAppUserModel() != null && ThreadLocalAppUser.getAppUserModel().getAppUserTypeId() != null
                && ThreadLocalAppUser.getAppUserModel().getAppUserTypeId().equals(UserTypeConstantsInterface.RETAILER)) {
            logger.info("Commission Calculated in CommissionManagerImpl.calculateCommission() for Product-ID: " + workFlowWrapper.getProductModel().getProductId()
                    + " Agent_ID: " + ThreadLocalAppUser.getAppUserModel().getMobileNo() + " at Time: " + new Date());
        } else {
            logger.info("Commission Calculated in CommissionManagerImpl.calculateCommission() for Product-ID: " + workFlowWrapper.getProductModel().getProductId()
                    + " Mobile #: " + ThreadLocalAppUser.getAppUserModel().getMobileNo() + " at Time: " + new Date());
        }

        if(workFlowWrapper.getProductModel().getProductId().equals(10245302L) ||
                workFlowWrapper.getProductModel().getProductId().equals(ProductConstantsInterface.International_POS_DEBIT_CARD_CASH_WITHDRAWAL)) {
            Double filerRate = 0.0;
            Double nonFilerRate = 0.0;
            if (workFlowWrapper.getAppUserModel() != null && workFlowWrapper.getAppUserModel().getFiler() != null) {
                if (workFlowWrapper.getAppUserModel().getFiler().equals(true)) {
                    FilerRateConfigModel filerRateConfigModel = taxManager.loadFilerRateConfigModelByFiler(1L);
                    if (filerRateConfigModel != null) {
                        filerRate = roundTwoDecimals((commissionAmountsHolder.getTotalAmount() * filerRateConfigModel.getRate())/100);
                        workFlowWrapper.setFilerRate(filerRate);
                        commissionAmountsHolder.setTotalAmount(commissionAmountsHolder.getTotalAmount() + filerRate);
                    }
                }
                else{
                    FilerRateConfigModel filerRateConfigModel = taxManager.loadFilerRateConfigModelByFiler(0L);
                    if (filerRateConfigModel != null) {
                        nonFilerRate = roundTwoDecimals((commissionAmountsHolder.getTotalAmount() * filerRateConfigModel.getRate())/100);
                        workFlowWrapper.setNonFilerRate(nonFilerRate);
                        commissionAmountsHolder.setTotalAmount(commissionAmountsHolder.getTotalAmount() + nonFilerRate);
                    }
                }
            }
        }
        return commissionWrapper;
    }

    /**
     * The purpose of this method is to set the various values in CommissionRate Model
     * So that it can be used for retrieving the data from the Commission Rate Table.
     *
     * @param commissionWrapper CommissionWrapper
     * @return CommissionRateModel
     */
    private CommissionRateModel setCommissionRateModel(WorkFlowWrapper
                                                               workFlowWrapper) {
        CommissionRateModel vo = new CommissionRateModel();
        vo.setProductId(workFlowWrapper.getProductModel().getProductId());
        vo.setSegmentId(workFlowWrapper.getSegmentModel().getSegmentId());
        if (workFlowWrapper.getSegmentModel().getSegmentId() == null) {
            vo.setSegmentId(CommissionConstantsInterface.DEFAULT_SEGMENT_ID);
        }
        //FIXME need to revisit all info commands after uncommenting this.
        if (workFlowWrapper.getDeviceTypeModel() != null && workFlowWrapper.getDeviceTypeModel().getDeviceTypeId() != null) {
            vo.setDeviceTypeId(workFlowWrapper.getDeviceTypeModel().getDeviceTypeId());
        } else {
            logger.error("[CommissionManagerImpl.setCommissionRateModel] ERROR! Device Type ID Not set for product ID: " + workFlowWrapper.getProductModel().getProductId());
        }

        //No Retailer involved in Bulk Payments/ Bulk Disbursments
        Long serviceId = workFlowWrapper.getProductModel().getServiceId();
        boolean isRetailerInvolved = true;
        if (workFlowWrapper.getIsCustomerInitiatedTransaction() ||
                workFlowWrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.IBFT ||
                workFlowWrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.WEB_SERVICE_PAYMENT ||
                workFlowWrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.VIRTUAL_CARD_PAYMENT ||
                (serviceId != null && (serviceId == ServiceConstantsInterface.BULK_DISB_NON_ACC_HOLDER.longValue() || workFlowWrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.AGENT_CASH_DEPOSIT ||
                        serviceId == ServiceConstantsInterface.BULK_DISB_ACC_HOLDER.longValue())
                )) {

            isRetailerInvolved = false;
        }


        if (isRetailerInvolved
                && workFlowWrapper.getRetailerContactModel() != null
                && workFlowWrapper.getRetailerContactModel().getRetailerIdRetailerModel() != null
                && workFlowWrapper.getRetailerContactModel().getRetailerIdRetailerModel().getDistributorId() != null) {

            vo.setDistributorId(workFlowWrapper.getRetailerContactModel().getRetailerIdRetailerModel().getDistributorId());
        } else {
            // For customer initiated transactions/Web Service Transactions
            vo.setDistributorId(DistributorConstants.DEFAULT_DISTRIBUTOR_ID);
        }
        //commissionRateModel.setPaymentModeId(workFlowWrapper.getTransactionModel().getPaymentModeId());
        //commissionRateModel.setTransactionTypeId(workFlowWrapper.getTransactionModel().getTransactionTypeId());
        vo.setActive(Boolean.TRUE);//TRUE
        vo.setIsDeleted(Boolean.FALSE);//FALSE
        //
        if (UserUtils.getCurrentUser().getMnoId() != null && UserUtils.getCurrentUser().getMnoId().equals(50028L))
            vo.setMnoId(50028L);
        else
            vo.setMnoId(50027L);
        return vo;

    }


    private Double setCommissionThresholdRateModel(WorkFlowWrapper
                                                               workFlowWrapper) {
        CommissionThresholdRateModel vo = new CommissionThresholdRateModel();
        vo.setProductId(workFlowWrapper.getProductModel().getProductId());
        vo.setSegmentId(workFlowWrapper.getSegmentModel().getSegmentId());
        if (workFlowWrapper.getSegmentModel().getSegmentId() == null) {
            vo.setSegmentId(CommissionConstantsInterface.DEFAULT_SEGMENT_ID);
        }
        //FIXME need to revisit all info commands after uncommenting this.
        if (workFlowWrapper.getDeviceTypeModel() != null && workFlowWrapper.getDeviceTypeModel().getDeviceTypeId() != null) {
            vo.setDeviceTypeId(workFlowWrapper.getDeviceTypeModel().getDeviceTypeId());
        } else {
            logger.error("[CommissionManagerImpl.setCommissionRateModel] ERROR! Device Type ID Not set for product ID: " + workFlowWrapper.getProductModel().getProductId());
        }

        //No Retailer involved in Bulk Payments/ Bulk Disbursments
        Long serviceId = workFlowWrapper.getProductModel().getServiceId();
        boolean isRetailerInvolved = true;

        if (isRetailerInvolved
                && workFlowWrapper.getRetailerContactModel() != null
                && workFlowWrapper.getRetailerContactModel().getRetailerIdRetailerModel() != null
                && workFlowWrapper.getRetailerContactModel().getRetailerIdRetailerModel().getDistributorId() != null) {

            vo.setDistributorId(workFlowWrapper.getRetailerContactModel().getRetailerIdRetailerModel().getDistributorId());
        } else {
            // For customer initiated transactions/Web Service Transactions
            vo.setDistributorId(DistributorConstants.DEFAULT_DISTRIBUTOR_ID);
        }

        vo.setActive(Boolean.TRUE);//TRUE
        vo.setIsDeleted(Boolean.FALSE);//FALSE
        //
        List<CommissionThresholdRateModel> rulesList = new ArrayList<CommissionThresholdRateModel>();
        rulesList = this.commissionThresholdRateDAO.loadCommissionThresholdRateList(vo);
        Double dailyDebitConsumed = 0.0d;
        Double monthlyDebitConsumed = 0.0d;
        Double chargedAmount = 0.0d;

        if(rulesList.size() != 0) {
            workFlowWrapper.setCommissionThresholdRate(rulesList.get(0));

            Double percentage = rulesList.get(0).getPercentageCharges();
            percentage = (percentage / 100);

            Long statusId = OlaStatusConstants.ACCOUNT_STATUS_ACTIVE;
            Long customerAccountTypeId;
            AccountModel accountModel = null;
            if(workFlowWrapper.getCustomerModel() != null){
                customerAccountTypeId = workFlowWrapper.getCustomerModel().getCustomerAccountTypeId();
            }
            else{
                customerAccountTypeId = CustomerAccountTypeConstants.RETAILER;
            }
            accountModel = getCommonCommandManager().getAccountModelByCnicAndCustomerAccountTypeAndStatusId
                    (ThreadLocalAppUser.getAppUserModel().getNic(), customerAccountTypeId, statusId);
            if (accountModel != null) {
                Long accountId = accountModel.getAccountId();
                Date currentDate = new Date();

                if (rulesList.get(0).getProductId().equals(ProductConstantsInterface.CUSTOMER_BB_TO_IBFT)) {
                    if (rulesList.get(0).getLimitTypeId().equals(LimitTypeConstants.DAILY)) {
                        dailyDebitConsumed = getCommonCommandManager().getDailyConsumedBalanceForIBFT(accountId, TransactionTypeConstants.DEBIT, currentDate, null);

                        Double amountGiven = (Double) workFlowWrapper.getObject("GIVEN_AMOUNT");

                        chargedAmount = dailyDebitConsumed + amountGiven - rulesList.get(0).getThresholdAmount();

                        if (dailyDebitConsumed > rulesList.get(0).getThresholdAmount()) {
                            chargedAmount = amountGiven * percentage;
                            chargedAmount = roundTwoDecimals(chargedAmount);
                        } else if (chargedAmount > 0.0) {
                            chargedAmount = chargedAmount * percentage;
                            chargedAmount = roundTwoDecimals(chargedAmount);
                        }
                    }
                    if (rulesList.get(0).getLimitTypeId().equals(LimitTypeConstants.MONTHLY)) {
                        Date startDate;
                        Calendar cal = GregorianCalendar.getInstance();
                        cal.setTime(new Date());
                        cal.set(Calendar.DAY_OF_MONTH, 1);
                        startDate = cal.getTime();
                        monthlyDebitConsumed = getCommonCommandManager().getConsumedBalanceByDateRangeForIBFT(accountId, TransactionTypeConstants.DEBIT, startDate, currentDate);

                        Double amountGiven = (Double) workFlowWrapper.getObject("GIVEN_AMOUNT");
                        chargedAmount = monthlyDebitConsumed + amountGiven - rulesList.get(0).getThresholdAmount();

                        if (monthlyDebitConsumed > rulesList.get(0).getThresholdAmount()) {
                            chargedAmount = amountGiven * percentage;
                            chargedAmount = roundTwoDecimals(chargedAmount);
                        } else if (chargedAmount > 0.0) {
                            chargedAmount = chargedAmount * percentage;
                            chargedAmount = roundTwoDecimals(chargedAmount);
                        }
                    }
                }

                if(rulesList.get(0).getProductId().equals(ProductConstantsInterface.AGENT_BB_TO_IBFT)){
                    if (rulesList.get(0).getLimitTypeId().equals(LimitTypeConstants.DAILY)) {
                        dailyDebitConsumed = getCommonCommandManager().getDailyConsumedBalanceForAgentIBFT
                                (accountId, TransactionTypeConstants.DEBIT, currentDate, null);

                        Double amountGiven = (Double) workFlowWrapper.getObject("GIVEN_AMOUNT");

                        chargedAmount = dailyDebitConsumed + amountGiven - rulesList.get(0).getThresholdAmount();

                        if (dailyDebitConsumed > rulesList.get(0).getThresholdAmount()) {
                            chargedAmount = amountGiven * percentage;
                            chargedAmount = roundTwoDecimals(chargedAmount);
                        } else if (chargedAmount > 0.0) {
                            chargedAmount = chargedAmount * percentage;
                            chargedAmount = roundTwoDecimals(chargedAmount);
                        }
                    }
                    if (rulesList.get(0).getLimitTypeId().equals(LimitTypeConstants.MONTHLY)) {
                        Date startDate;
                        Calendar cal = GregorianCalendar.getInstance();
                        cal.setTime(new Date());
                        cal.set(Calendar.DAY_OF_MONTH, 1);
                        startDate = cal.getTime();
                        monthlyDebitConsumed = getCommonCommandManager().getConsumedBalanceByDateRangeForAgentIBFT
                                (accountId, TransactionTypeConstants.DEBIT, startDate, currentDate);

                        Double amountGiven = (Double) workFlowWrapper.getObject("GIVEN_AMOUNT");
                        chargedAmount = monthlyDebitConsumed + amountGiven - rulesList.get(0).getThresholdAmount();

                        if (monthlyDebitConsumed > rulesList.get(0).getThresholdAmount()) {
                            chargedAmount = amountGiven * percentage;
                            chargedAmount = roundTwoDecimals(chargedAmount);
                        } else if (chargedAmount > 0.0) {
                            chargedAmount = chargedAmount * percentage;
                            chargedAmount = roundTwoDecimals(chargedAmount);
                        }
                    }
                }

                if(rulesList.get(0).getMaxThresholdAmount() > 0.0) {
                    if (chargedAmount > rulesList.get(0).getMaxThresholdAmount()) {
                        chargedAmount = rulesList.get(0).getMaxThresholdAmount();
                    }
                }
            }
        }
        return chargedAmount;
    }
    /**
     * This method is used for retrieving the data from the Transaction Rate table
     * depending upon the criteria set in the Commission Rate Model.
     *
     * @param commissionRateModel CommissionRateModel
     * @return SearchBaseWrapper
     */
    public SearchBaseWrapper getCommissionRateData(CommissionRateModel
                                                           commissionRateModel) {
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        CustomList<CommissionRateModel>
                list = this.commissionRateDAO.findByExample((CommissionRateModel) commissionRateModel);
        searchBaseWrapper.setCustomList(list);

        return searchBaseWrapper;
    }

    public List<CommissionRateModel> getCommissionRateList(CommissionRateModel vo) {
        return this.commissionRateDAO.loadCommissionRateList(vo);
    }

    /**
     * @param resultList
     * @return
     * @author Shouab
     */
    private boolean isFedApplicable(Collection<CommissionShSharesModel> resultList) {
        if (resultList != null && resultList.size() > 0) {
            for (CommissionShSharesModel model : resultList) {
                if (model.getCommissionStakeholderId().longValue() == CommissionConstantsInterface.FED_STAKE_HOLDER_ID.longValue()) {
                    return true;
                }
            }
        }
        return false;
    }

    private CommissionShSharesModel getStakeholderShareModelByStakeholderId(Collection<CommissionShSharesModel> resultList, Long stakeholderId) {
        if (resultList != null && resultList.size() > 0) {
            for (CommissionShSharesModel model : resultList) {
                if (model.getCommissionStakeholderId().longValue() == stakeholderId.longValue()) {
                    return model;
                }
            }
        }

        return null;
    }


    /**
     * ---NEW---
     * Process commission rates for each staekholder
     * <p>
     * This method is used for the calculation of the various commission amounts. This method is
     * called recursively against the each record retrieved from Commission Rate table.
     *
     * @param commissionRateModel     Model which represents a single record retrieved from the Commsion Rate table.
     * @param commissionAmountsHolder Data Holder class for the calculation of various commission amounts.
     * @throws FrameworkCheckedException
     */
    private void processCommissionRate(CommissionRateModel commissionRateModel, CommissionAmountsHolder commissionAmountsHolder, WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Inside processCommissionRate method of CommissionManagerImpl....");
        }

        Double totalCommissionAmount = commissionAmountsHolder.getTotalCommissionAmount();             // equals to: Incl Fix + Incl Percent + Excl Fix + Excl Percent
        Double totalAmount = commissionAmountsHolder.getTotalAmount();                                 // Equals to: Trx amount + Excl Fix + Excl Percent
        Double billingOrganizationAmount = commissionAmountsHolder.getBillingOrganizationAmount();     // Equals to: Trx amount - Incl Fix - Incl Percent
        Double transactionAmount = commissionAmountsHolder.getTransactionAmount();                     // Equals to: trx amount
        Double transactionProcessingAmount = commissionAmountsHolder.getTransactionProcessingAmount(); // Equals to: Excl Fix + Excl Percent

        if (commissionRateModel.getInclusiveFixAmount() != null) {
            double inclFixAmount = commissionRateModel.getInclusiveFixAmount();
            inclFixAmount = roundTwoDecimals(inclFixAmount);
            totalCommissionAmount += inclFixAmount;
            billingOrganizationAmount -= inclFixAmount;
            commissionAmountsHolder.setInclusiveFixAmount(inclFixAmount);
        }

        //Check for %age rate and add
        if (commissionRateModel.getInclusivePercentAmount() != null) {
            double inclusivePercentAmount = calculateVariableCommissionAmount(commissionRateModel.getInclusivePercentAmount(), transactionAmount);
            totalCommissionAmount += inclusivePercentAmount;
            billingOrganizationAmount -= inclusivePercentAmount;
            commissionAmountsHolder.setInclusivePercentAmount(inclusivePercentAmount);
        }


        // Case of Service charges are Exclusive and Fix
        if (commissionRateModel.getExclusiveFixAmount() != null) {
            double exclusiveFixAmount = commissionRateModel.getExclusiveFixAmount();
            exclusiveFixAmount = roundTwoDecimals(exclusiveFixAmount);
            totalCommissionAmount += exclusiveFixAmount;
            totalAmount += exclusiveFixAmount;
            transactionProcessingAmount += exclusiveFixAmount;
            commissionAmountsHolder.setExclusiveFixAmount(exclusiveFixAmount);
        }

        // Case of Service charges are Exclusive and Percent
        if (commissionRateModel.getExclusivePercentAmount() != null) {
            double exclusivePercentAmount = calculateVariableCommissionAmount(commissionRateModel.getExclusivePercentAmount(), transactionAmount);
            totalCommissionAmount += exclusivePercentAmount;
            if (workFlowWrapper.getProductModel().getProductId().equals(ProductConstantsInterface.CUSTOMER_DEBIT_CARD_ISSUANCE) ||
                    workFlowWrapper.getProductModel().getProductId().equals(ProductConstantsInterface.DEBIT_CARD_RE_ISSUANCE) ||
                    workFlowWrapper.getProductModel().getProductId().equals(ProductConstantsInterface.DEBIT_CARD_ANNUAL_FEE)
                    || workFlowWrapper.getProductModel().getProductId().equals(ProductConstantsInterface.ATM_BALANCE_INQUIRY_OFF_US) ||
                    workFlowWrapper.getProductModel().getProductId().equals((ProductConstantsInterface.INTERNATIONAL_BALANCE_INQUIRY_OFF_US))) {
                totalAmount += exclusivePercentAmount;
            }
            totalAmount += exclusivePercentAmount;
            transactionProcessingAmount += exclusivePercentAmount;
            commissionAmountsHolder.setExclusivePercentAmount(exclusivePercentAmount);
        }
        if (billingOrganizationAmount < 0) {
            ProductModel productModel = workFlowWrapper.getProductModel();
            Boolean thirdPartyChargesIncluded = productModel.getInclChargesCheck();
            if (!thirdPartyChargesIncluded) {
                throw new FrameworkCheckedException("Charges Exceed Transaction Amount.");
            }
        }

        logger.info("Charged amount is: " + workFlowWrapper.getChargedAmount());
        if((workFlowWrapper.getCommissionThresholdRate() != null && workFlowWrapper.getChargedAmount() != null) && workFlowWrapper.getChargedAmount().equals
                (workFlowWrapper.getCommissionThresholdRate().getMaxThresholdAmount())){
            totalCommissionAmount = workFlowWrapper.getChargedAmount();
            transactionProcessingAmount = workFlowWrapper.getChargedAmount();
            totalAmount = commissionAmountsHolder.getTransactionAmount() + workFlowWrapper.getChargedAmount();
        }

        if(workFlowWrapper.getCommissionThresholdRate() != null && workFlowWrapper.getCommissionThresholdRate().getMaxThresholdAmount() > 0.0) {
            if (workFlowWrapper.getCommissionThresholdRate() != null && totalCommissionAmount > workFlowWrapper.getCommissionThresholdRate().getMaxThresholdAmount()) {
                totalCommissionAmount = workFlowWrapper.getCommissionThresholdRate().getMaxThresholdAmount();
                transactionProcessingAmount = workFlowWrapper.getCommissionThresholdRate().getMaxThresholdAmount();
                totalAmount = commissionAmountsHolder.getTransactionAmount() + workFlowWrapper.getCommissionThresholdRate().getMaxThresholdAmount();
            }
        }
        //Load all Commission Stakeholders
	  /*SearchBaseWrapper sBaseWrapper = new SearchBaseWrapperImpl();
	  sBaseWrapper.setBasePersistableModel(new CommissionStakeholderModel());*/
        List<CommissionStakeholderModel> commissionStakeholdersList = commissionStakeholderManager.loadCommissionStakeholdersList(null);
        populateStakeholderFilerMap(commissionAmountsHolder, commissionStakeholdersList);
        double fedAmount = 0d;
        Double fedRateForBank = 0d;
        boolean bankShareExists = false;
        if (commissionRateModel.getProductId() != null) {
//		  CommissionShSharesModel sharesModel = new CommissionShSharesModel();
            CommissionShSharesModel SharesVO = new CommissionShSharesModel();
            SharesVO.setIsDeleted(Boolean.FALSE);
            SharesVO.setProductId(commissionRateModel.getProductId());
            SharesVO.setSegmentId(commissionRateModel.getSegmentId());
            SharesVO.setDeviceTypeId(commissionRateModel.getDeviceTypeId());
            SharesVO.setDistributorId(commissionRateModel.getDistributorId());
            if (UserUtils.getCurrentUser().getMnoId() != null && UserUtils.getCurrentUser().getMnoId().equals(50028L))
                SharesVO.setMnoId(50028L);
            else
                SharesVO.setMnoId(50027L);
            //    	CustomList<CommissionShSharesModel> resultList1 = (CustomList<CommissionShSharesModel>)commissionShSharesDAO.loadCommissionShSharesList(sharesModel);
            List<CommissionShSharesModel> stakeholderSharesList = commissionStakeholderManager.loadCommissionShSharesList(SharesVO);
            if (null == stakeholderSharesList || stakeholderSharesList.isEmpty())
                stakeholderSharesList = new CopyOnWriteArrayList<CommissionShSharesModel>();
            CopyOnWriteArrayList<CommissionShSharesModel> stakeCopyOnWriteList = new CopyOnWriteArrayList<>();
            stakeCopyOnWriteList.addAll(stakeholderSharesList);
            if (CollectionUtils.isEmpty(stakeCopyOnWriteList)) {  //Default shares will be applied in this case.
                List<CommissionShSharesDefaultModel> defaultSSharesList = commissionStakeholderManager.loadDefaultCommissionShSharesList(commissionRateModel.getProductId());
                // map values to CommissionShSharsModel.
                SharesVO = new CommissionShSharesModel();
                for (CommissionShSharesDefaultModel defaultModel : defaultSSharesList) {
                    defaultModel.setCommissionShare(CommonUtils.getDoubleOrDefaultValue(defaultModel.getCommissionShare()));
                    SharesVO.setCommissionStakeholderId(defaultModel.getCommissionStakeholderId());
                    SharesVO.setCommissionShare(defaultModel.getCommissionShare());
                    SharesVO.setIsWhtApplicable(defaultModel.getIsWhtApplicable());
                    SharesVO.setProductId(defaultModel.getProductId());
                    if (UserUtils.getCurrentUser().getMnoId() != null && UserUtils.getCurrentUser().getMnoId().equals(50028L))
                        SharesVO.setMnoId(50028L);
                    else
                        SharesVO.setMnoId(50027L);
                    stakeCopyOnWriteList.add(SharesVO);
                    SharesVO = new CommissionShSharesModel();
                }
            }
            boolean isFedCalculated = false;//what is usage of this flag??
            boolean zeroFEDRate = false;
            if (stakeCopyOnWriteList != null && stakeCopyOnWriteList.size() > 0) {
                CommissionShSharesModel fedShareModel = null;
                //		  boolean isFedApplicable = isFedApplicable(stakeCopyOnWriteList);
                CommissionStakeholderModel fedModel = commissionStakeholderDAO.findStakeHolderById(CommissionConstantsInterface.FED_STAKE_HOLDER_ID);   //load model for FED
                CommissionStakeholderModel whtModel = commissionStakeholderDAO.findStakeHolderById(CommissionConstantsInterface.WHT_STAKE_HOLDER_ID);   //load model for WHT
                int index = 0;
                for (CommissionStakeholderModel stakeholderModel : commissionStakeholdersList) {
                    //initialize one taxBean for each stakehodlers
                    commissionAmountsHolder.getStakeholderTaxMap().put(stakeholderModel.getCommissionStakeholderId(), new TaxValueBean());
                    for (CommissionShSharesModel shSharemodel : stakeCopyOnWriteList) {
                        if (shSharemodel.getCommissionStakeholderId() == CommissionConstantsInterface.FED_STAKE_HOLDER_ID.longValue()
                                || shSharemodel.getCommissionStakeholderId() == CommissionConstantsInterface.WHT_STAKE_HOLDER_ID.longValue()) {
                            continue; //FED is already calculated above. skipping fed stakeholder iteration from here.
                        }
                        //*************************************************************
                        CommissionShSharesModel fedmodel = new CommissionShSharesModel();
                        CommissionShSharesModel whtmodel = new CommissionShSharesModel();
                        fedmodel.setCommissionStakeholderIdCommissionStakeholderModel(fedModel);
                        whtmodel.setCommissionStakeholderIdCommissionStakeholderModel(whtModel);
                        stakeCopyOnWriteList.add(fedmodel);
                        stakeCopyOnWriteList.add(whtmodel);
                        //******************************************************
                        if (!isFedCalculated) {
                            double commissionWithoutFEDAmount = 0d;
                            double fedRate = 0d; //yahan krna ha model load
                            FEDRuleViewModel fedRuleViewModel = new FEDRuleViewModel();
                            fedRuleViewModel = taxManager.loadFEDRuleViewModel(workFlowWrapper);
                            if (fedRuleViewModel.getRate() != null)
                                fedRate = fedRuleViewModel.getRate();
                            if (fedRate <= 0) {
                                defaultFEDSetup(commissionAmountsHolder);
                                isFedCalculated = true;
                                zeroFEDRate = true;
                                //isFedApplicable = false;
                                break;
                            }
                            double fedPercentage = 1 + fedRate / 100;
                            fedShareModel = getStakeholderShareModelByStakeholderId(stakeCopyOnWriteList, CommissionConstantsInterface.FED_STAKE_HOLDER_ID);
                            if (fedShareModel != null) {
                                fedShareModel.setCommissionShare(fedRate);
                                //handle zero and infinity cases
                                if (fedRate > 0d) {
                                  if (workFlowWrapper.getProductModel().getProductId().equals(ProductConstantsInterface.INTERNATIONAL_BALANCE_INQUIRY_OFF_US)){
                                      commissionWithoutFEDAmount = totalCommissionAmount * fedPercentage;

                                  }else {
                                      commissionWithoutFEDAmount = totalCommissionAmount / fedPercentage;
                                  }
                                }

                            } else {
                                throw new FrameworkCheckedException("FED stakeholder Not defined.");
                            }
                            if (fedShareModel.getCommissionShare() > 0d) {
                                if (workFlowWrapper.getProductModel().getProductId().equals(ProductConstantsInterface.INTERNATIONAL_BALANCE_INQUIRY_OFF_US)){
                                    fedAmount = commissionWithoutFEDAmount - totalCommissionAmount;
                                }else {
                                    fedAmount = totalCommissionAmount - commissionWithoutFEDAmount;
                                }
                                fedAmount = roundTwoDecimals(fedAmount);
                                if (commissionRateModel.getInclusivePercentAmount() == 0.0 && (workFlowWrapper.getProductModel().getProductId().equals(ProductConstantsInterface.CUSTOMER_DEBIT_CARD_ISSUANCE) ||
                                        workFlowWrapper.getProductModel().getProductId().equals(ProductConstantsInterface.DEBIT_CARD_ISSUANCE) ||
                                        workFlowWrapper.getProductModel().getProductId().equals(ProductConstantsInterface.DEBIT_CARD_RE_ISSUANCE) ||
                                        workFlowWrapper.getProductModel().getProductId().equals(ProductConstantsInterface.DEBIT_CARD_ANNUAL_FEE) ||
                                        workFlowWrapper.getProductModel().getProductId().equals(ProductConstantsInterface.ATM_BALANCE_INQUIRY_OFF_US)
                                        ||workFlowWrapper.getProductModel().getProductId().equals(ProductConstantsInterface.INTERNATIONAL_BALANCE_INQUIRY_OFF_US))) {
                                    totalCommissionAmount = totalCommissionAmount;
                                } else {
                                    totalCommissionAmount = totalCommissionAmount - fedAmount;
                                }

                                isFedCalculated = true;
                                commissionAmountsHolder.getStakeholderCommissionsMap().put(CommissionConstantsInterface.FED_STAKE_HOLDER_ID, fedAmount);
                            }
                        }

                        if (shSharemodel.getCommissionStakeholderId().longValue() != CommissionConstantsInterface.BANK_STAKE_HOLDER_ID.longValue()) {

                            if (shSharemodel.getCommissionStakeholderId().longValue() == stakeholderModel.getCommissionStakeholderId().longValue()) {
                                Double amount = calculateVariableCommissionAmount(shSharemodel.getCommissionShare(), totalCommissionAmount);
                                commissionAmountsHolder.getStakeholderCommissionsMap().put(stakeholderModel.getCommissionStakeholderId(), amount);

                                if (shSharemodel.getCommissionStakeholderId().longValue() == CommissionConstantsInterface.AGENT2_STAKE_HOLDER_ID.longValue()) {
                                    logger.info("Setting Agent2 wht applicable ---> " + shSharemodel.getIsWhtApplicable());
                                    boolean isWhtApplicable = false;
                                    if (shSharemodel.getIsWhtApplicable())
                                        isWhtApplicable = true;
                                    commissionAmountsHolder.getStakeholderTaxMap().get(stakeholderModel.getCommissionStakeholderId()).setAgent2WhtApplicable(isWhtApplicable);
                                }

                                if (isFedCalculated/* && isFedApplicable == true*/) { // skip FED Calculation if FED value is not defined
                                    Double stakeholderFedAmount = calculateVariableCommissionAmount(shSharemodel.getCommissionShare(), commissionAmountsHolder.getStakeholderCommissionsMap().get(CommissionConstantsInterface.FED_STAKE_HOLDER_ID));
                                    commissionAmountsHolder.getStakeholderTaxMap().get(stakeholderModel.getCommissionStakeholderId()).setFedAmount(stakeholderFedAmount);
                                    if (zeroFEDRate == false) {
                                        commissionAmountsHolder.getStakeholderTaxMap().get(stakeholderModel.getCommissionStakeholderId()).setFedRate(fedShareModel.getCommissionShare());
                                    }
                                }
                                break;
                            }

                        } else {
                            bankShareExists = true;
                            // if(isFedApplicable){
                            //required for bank fed calculation after bank commission is calculated.
                            if (zeroFEDRate) {

                            } else {
                                fedRateForBank = fedShareModel.getCommissionShare();
                            }

                            // }
                        }

                    }
                }
                double totalWhtAmount = 0.0;
                double whtSharePercentage = 0d;
                for (Map.Entry<Long, Double> stakeholderShareEntry : commissionAmountsHolder.getStakeholderCommissionsMap().entrySet()) {
                    Long stakeholderId = stakeholderShareEntry.getKey();
                    Double stakeholderShare = stakeholderShareEntry.getValue();
                    CommissionShSharesModel stakeholderShareModel = getStakeholderShareModelByStakeholderId(stakeCopyOnWriteList, stakeholderId);
                    if (null != stakeholderShareModel.getIsWhtApplicable() && stakeholderShareModel.getIsWhtApplicable()
                            && stakeholderId.longValue() != CommissionConstantsInterface.BANK_STAKE_HOLDER_ID
                            && stakeholderId.longValue() != CommissionConstantsInterface.WHT_STAKE_HOLDER_ID.longValue()
                            && stakeholderId.longValue() != CommissionConstantsInterface.FED_STAKE_HOLDER_ID.longValue()) {
                        if (stakeholderId.longValue() == CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID) {
                            whtSharePercentage = calculateFilerNonFilerRate();
                        } else {
                            WHTConfigModel whtConfigModel = taxManager.loadWHTConfigModel(TaxConstantsInterface.WHT_CONFIG_COMMISSION_ID);
                            whtSharePercentage = whtConfigModel.getNonFilerRate();
                            Boolean filer = commissionAmountsHolder.getStakeholderFilerMap().get(stakeholderId);
                            if (CommonUtils.getDefaultIfNull(filer, false)) {
                                //apply filer rate
                                whtSharePercentage = whtConfigModel.getFilerRate();
                            }
                        }
                        double whtAmount = calculateVariableCommissionAmount(whtSharePercentage, stakeholderShare);
                        totalWhtAmount = whtAmount + totalWhtAmount;
                        //double share = stakeholderShare - whtAmount;
                        if (stakeholderId.longValue() == CommissionConstantsInterface.AGENT2_STAKE_HOLDER_ID) {
                            whtSharePercentage = 0;
                            //share = stakeholderShare;
                            totalWhtAmount -= whtAmount;
                            whtAmount = 0.0;
                        }
                        commissionAmountsHolder.getStakeholderCommissionsMap().put(stakeholderId, stakeholderShare - whtAmount);
                        commissionAmountsHolder.getStakeholderTaxMap().get(stakeholderId).setWhtAmount(whtAmount);
                        commissionAmountsHolder.getStakeholderTaxMap().get(stakeholderId).setWhtRate(whtSharePercentage);
                    }
                }
                commissionAmountsHolder.getStakeholderTaxMap().put(CommissionConstantsInterface.WHT_STAKE_HOLDER_ID, new TaxValueBean());
                commissionAmountsHolder.getStakeholderCommissionsMap().put(CommissionConstantsInterface.WHT_STAKE_HOLDER_ID, totalWhtAmount);
            }
            //calculate agent hierarchy commissions from agent commission
            prepareAgentHierarchyCommission(commissionAmountsHolder, workFlowWrapper, CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID);

        }


        //rounding commissionn to TWO decimals
        this.roundCommissonRates(commissionAmountsHolder);

        double askariCommissionAmount = totalCommissionAmount;
        for (Map.Entry<Long, Double> stakeholderShareEntry : commissionAmountsHolder.getStakeholderCommissionsMap().entrySet()) {

            if (stakeholderShareEntry.getKey().doubleValue() != CommissionConstantsInterface.FED_STAKE_HOLDER_ID) {

                askariCommissionAmount = askariCommissionAmount - stakeholderShareEntry.getValue();
            }

        }

        for (Map.Entry<Long, Double> stakeholderShareEntry : commissionAmountsHolder.getHierarchyStakeholderCommissionMap().entrySet()) {

            askariCommissionAmount = askariCommissionAmount - stakeholderShareEntry.getValue();

        }

        this.roundTaxAmount(commissionAmountsHolder);

        Double bankFedAmount = roundTwoDecimals(fedAmount);
        for (Map.Entry<Long, TaxValueBean> entry : commissionAmountsHolder.getStakeholderTaxMap().entrySet()) {
            bankFedAmount = bankFedAmount - entry.getValue().getFedAmount();
        }
        for (Map.Entry<Long, TaxValueBean> entry : commissionAmountsHolder.getHierarchyStakeholderTaxMap().entrySet()) {
            bankFedAmount = bankFedAmount - entry.getValue().getFedAmount();
        }


//    Double bankFedAmount = calculateVariableCommissionAmount(bankCommissionShare, commissionAmountsHolder.getStakeholderCommissionsMap().get(CommissionConstantsInterface.FED_STAKE_HOLDER_ID));
        if (commissionAmountsHolder.getStakeholderTaxMap().get(CommissionConstantsInterface.BANK_STAKE_HOLDER_ID) != null) {
            commissionAmountsHolder.getStakeholderTaxMap().get(CommissionConstantsInterface.BANK_STAKE_HOLDER_ID).setFedAmount(roundTwoDecimals(bankFedAmount));
            commissionAmountsHolder.getStakeholderTaxMap().get(CommissionConstantsInterface.BANK_STAKE_HOLDER_ID).setFedRate(fedRateForBank);
        } else {
            commissionAmountsHolder.getStakeholderTaxMap().put(CommissionConstantsInterface.BANK_STAKE_HOLDER_ID, new TaxValueBean());
        }

        this.roundTaxAmount(commissionAmountsHolder);
	  
    /*askariCommissionAmount = totalCommissionAmount - 
			(commissionAmountsHolder.getZongCommissionAmount() + commissionAmountsHolder.getI8CommissionAmount()
					+ commissionAmountsHolder.getAgent1CommissionAmount() + commissionAmountsHolder.getAgent2CommissionAmount()
					+ commissionAmountsHolder.getWhtCommissionAmount() + commissionAmountsHolder.getFranchise1CommissionAmount()
					+ commissionAmountsHolder.getFranchise2CommissionAmount());*/
        /*after rounding, calculate askari commission amount by subtracting all stakeholders commission amount from total commmission amount.
         * this is fix applied to bug id 457. */

        this.adjustStakeholderWHT(commissionAmountsHolder);

        if (bankShareExists) {
            commissionAmountsHolder.getStakeholderCommissionsMap().put(CommissionConstantsInterface.BANK_STAKE_HOLDER_ID, this.roundTwoDecimals(askariCommissionAmount));
        } else {
            this.adjustDiffInTotalCommission(commissionAmountsHolder, askariCommissionAmount);
        }

        commissionAmountsHolder.setTotalCommissionAmount(this.roundTwoDecimals(totalCommissionAmount));
        commissionAmountsHolder.setTotalAmount(totalAmount);
        commissionAmountsHolder.setBillingOrganizationAmount(billingOrganizationAmount);
        commissionAmountsHolder.setTransactionProcessingAmount(transactionProcessingAmount);
        if (logger.isDebugEnabled()) {
            logger.debug("Ending processCommissionRate method of CommissionManagerImpl....");
        }
    }

    private void adjustDiffInTotalCommission(CommissionAmountsHolder commissionAmountsHolder, double diff) {
        diff = this.roundTwoDecimals(diff);
        if (diff >= 0.01 || diff <= -0.01) {
            logger.error("\n************ Going to adjust Rounding Difference in stakeholder other than bank --> Difference = " + diff);
            Double agent2CommAmount = commissionAmountsHolder.getStakeholderCommissionsMap().get(CommissionConstantsInterface.AGENT2_STAKE_HOLDER_ID);
            Double agent1CommAmount = commissionAmountsHolder.getStakeholderCommissionsMap().get(CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID);
            if (agent2CommAmount != null && agent2CommAmount > 1) {
                double adjustedAgent2CommAmount = this.roundTwoDecimals(agent2CommAmount + diff);
                commissionAmountsHolder.getStakeholderCommissionsMap().put(CommissionConstantsInterface.AGENT2_STAKE_HOLDER_ID, adjustedAgent2CommAmount);
                logger.info("\n************\nAdjusting diff in Agent2 Commission OldValue=" + agent2CommAmount + " NewValue=" + adjustedAgent2CommAmount + "\n************");
            } else if (agent1CommAmount != null && agent1CommAmount > 1) {
                double adjustedAgent1CommAmount = this.roundTwoDecimals(agent1CommAmount + diff);
                commissionAmountsHolder.getStakeholderCommissionsMap().put(CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID, adjustedAgent1CommAmount);
                logger.info("\n************\nAdjusting diff in Agent1 Commission OldValue=" + agent1CommAmount + " NewValue=" + adjustedAgent1CommAmount + "\n************");
            } else {
                logger.error("\n************\n************\nUnable to adjust the difference as agent1,agent2,bank stakeholders are not defined--> Difference = " + diff + "\n************\n************");
            }
        }
    }

    /**
     * This method is used for the calculation of the various commission amounts. This method is
     * called recursively against the each record retrieved from Commission Rate table.
     * @param commissionRateModel Model which represents a single record retrieved from the Commsion Rate table.
     * @param commissionAmountsHolder Data Holder class for the calculation of various commission amounts.
     */
/*  private void processCommissionRate(CommissionRateModel commissionRateModel,
                                     CommissionAmountsHolder
                                     commissionAmountsHolder)
  {
	  if(logger.isDebugEnabled())
		{
		  logger.debug("Inside processCommissionRate method of CommissionManagerImpl....");
		}
    Double totalCommissionAmount = commissionAmountsHolder.getTotalCommissionAmount();
    Double totalAmount = commissionAmountsHolder.getTotalAmount();
    Double billingOrganizationAmount = commissionAmountsHolder.getBillingOrganizationAmount();
    Double transactionAmount = commissionAmountsHolder.getTransactionAmount();
    Double transactionProcessingAmount = commissionAmountsHolder.getTransactionProcessingAmount();
    
    double commissionAmount = 0 ;
    
    // Case of Supplier charges - All charges are Inclusive
    if( commissionRateModel.getCommissionReasonId().longValue() == CommissionReasonConstants.SUPPLIER_COMMISSION.longValue())
    {
    	commissionAmountsHolder.setIsInclusiveCharges(true);
    	//If Rate is Fixed
    	if( commissionRateModel.getCommissionTypeId().longValue() == CommissionConstantsInterface.FIXED_COMMISSION.longValue() )
    	{
    		commissionAmount = commissionRateModel.getRate();
    		totalCommissionAmount += commissionRateModel.getRate();    		
    	    billingOrganizationAmount -= commissionRateModel.getRate();
    		
    	}
    }
    // Case of Service charges - All charges are Exclusive
    else if( commissionRateModel.getCommissionReasonId().longValue() == CommissionReasonConstants.SERVICE_CHARGE.longValue() )
    {
    	// If Rate is Fixed
    	if( commissionRateModel.getCommissionTypeId().longValue() == CommissionConstantsInterface.FIXED_COMMISSION.longValue() )
    	{
    		totalCommissionAmount += commissionRateModel.getRate();
    		totalAmount += commissionRateModel.getRate();
    	    commissionAmount = commissionRateModel.getRate();
    	    transactionProcessingAmount += commissionRateModel.getRate();    		
    	}
    }
    
    if(commissionRateModel != null && commissionRateModel.getProductId() != null){
    	CommissionShSharesModel sharesModel = new CommissionShSharesModel();
    	sharesModel.setProductId(commissionRateModel.getProductId());
    	ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
    	exampleHolder.setMatchMode(MatchMode.EXACT);
    	
    	CustomList<CommissionShSharesModel> resultList1 = (CustomList<CommissionShSharesModel>)commissionShSharesDAO.findByExample(sharesModel);
    	List<CommissionShSharesModel> resultList = resultList1.getResultsetList();
    	BaseWrapper searchBaseWrapper = new BaseWrapperImpl();
    	searchBaseWrapper.setBasePersistableModel(sharesModel);
    	
		boolean isFedCalculated = false;
    	if(resultList != null && resultList.size()> 0){
    		for(CommissionShSharesModel model : resultList){
    			if(isFedApplicable(resultList) && !isFedCalculated)
    			{
    				double commissionWithoutFEDAmount = 0d;
    				CommissionShSharesModel fedShareModel = getStakeholderShareModelByStakeholderId(resultList, CommissionConstantsInterface.FED_STAKE_HOLDER_ID);
    				if(fedShareModel != null){
    					commissionWithoutFEDAmount = totalCommissionAmount/fedShareModel.getCommissionShare();
    				}else{
    					logger.warn("[CommissionManagerImpl.processCommissionRate] FED stakeholder Not defined. Now using default FED value: " + DEFAULT_FED_VALUE + 
        						" LoggedIn AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
    					commissionWithoutFEDAmount = totalCommissionAmount/DEFAULT_FED_VALUE;
    				}
    				double fedAmount = totalCommissionAmount - commissionWithoutFEDAmount;
    				totalCommissionAmount = totalCommissionAmount - fedAmount;
    				isFedCalculated = true;
    				commissionAmountsHolder.setFedCommissionAmount(fedAmountcalculateVariableCommissionAmount(model.getCommissionShare(),totalCommissionAmount));
    			}
    			
    			fix applied by Mudassir. bug Id: 457 -  Commission Settlement - Difference in Recon (due to rounding off) should be settled in Askari's Account.
    			now askari commission will be calculated after values are rounded off and the difference of total commission from all stakeholder commission is charged to askari.
    			if(model.getCommissionStakeholderId().longValue()==CommissionConstantsInterface.ASKARI_STAKE_HOLDER_ID.longValue()){
    				commissionAmountsHolder.setAskariCommissionAmount(calculateVariableCommissionAmount(model.getCommissionShare(),totalCommissionAmount));
    			}else 
    			if(model.getCommissionStakeholderId().longValue()==CommissionConstantsInterface.ZONG_STAKE_HOLDER_ID.longValue()){
    				commissionAmountsHolder.setZongCommissionAmount(calculateVariableCommissionAmount(model.getCommissionShare(),totalCommissionAmount));
    			}else if(model.getCommissionStakeholderId().longValue()==CommissionConstantsInterface.I8_STAKE_HOLDER_ID.longValue()){
    				commissionAmountsHolder.setI8CommissionAmount(calculateVariableCommissionAmount(model.getCommissionShare(),totalCommissionAmount));
    			}else if(model.getCommissionStakeholderId().longValue()==CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID.longValue()){
    				commissionAmountsHolder.setAgent1CommissionAmount(calculateVariableCommissionAmount(model.getCommissionShare(),totalCommissionAmount));
    			}else if(model.getCommissionStakeholderId().longValue()==CommissionConstantsInterface.AGENT2_STAKE_HOLDER_ID.longValue()){ 
    				commissionAmountsHolder.setAgent2CommissionAmount(calculateVariableCommissionAmount(model.getCommissionShare(),totalCommissionAmount));
//    			}else if(model.getCommissionStakeholderId().equals(CommissionConstantsInterface.WHT_STAKE_HOLDER_ID)){ 
//    				commissionAmountsHolder.setWhtCommissionAmount(calculateVariableCommissionAmount(model.getShare(),totalCommissionAmount));
//    			}
	    		}
	    	}
    		
    		//once agent 1 AND agent 2 commission is calculated, deduct franchises commission accordingly
    		for(CommissionShSharesModel model : resultList){
    			
    			if(model.getCommissionStakeholderId().longValue() == CommissionConstantsInterface.FRANCHISE1_STAKE_HOLDER_ID.longValue()){
    				
    				if(commissionAmountsHolder.getAgent1CommissionAmount() > 0){
	    				
    					Double franchise1CommissionAmount = calculateVariableCommissionAmount(model.getCommissionShare(), commissionAmountsHolder.getAgent1CommissionAmount());
	    				Double agentCommissionAmount = commissionAmountsHolder.getAgent1CommissionAmount() - franchise1CommissionAmount;
	    				commissionAmountsHolder.setAgent1CommissionAmount(agentCommissionAmount);
	       				commissionAmountsHolder.setFranchise1CommissionAmount(franchise1CommissionAmount);
       			    			
    				}
    				
    			}else if(model.getCommissionStakeholderId().longValue() == CommissionConstantsInterface.FRANCHISE2_STAKE_HOLDER_ID.longValue()){
    				
    				if(commissionAmountsHolder.getAgent2CommissionAmount() > 0){
	    				Double franchise2CommissionAmount = calculateVariableCommissionAmount(model.getCommissionShare(), commissionAmountsHolder.getAgent2CommissionAmount());
	    				Double agentCommissionAmount = commissionAmountsHolder.getAgent2CommissionAmount() - franchise2CommissionAmount;
	    				commissionAmountsHolder.setAgent2CommissionAmount(agentCommissionAmount);
	    				commissionAmountsHolder.setFranchise2CommissionAmount(franchise2CommissionAmount);
    				
    				}
    				
    			}
    		}

    		if(isWHTApplicable(resultList))
    		{
    			double whtAmount = 0.0;
    			double whtSharePercentage = 0d;
    			CommissionShSharesModel whtShareModel = getStakeholderShareModelByStakeholderId(resultList, CommissionConstantsInterface.WHT_STAKE_HOLDER_ID);
				if(whtShareModel != null){
					whtSharePercentage = whtShareModel.getCommissionShare();
				}else{
					logger.warn("[CommissionManagerImpl.processCommissionRate] WHT stakeholder Not defined. Now using default WHT value: " + DEFAULT_WHT_VALUE + 
    						" LoggedIn AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
					whtSharePercentage = DEFAULT_WHT_VALUE.doubleValue();
				}
				
    			if(commissionAmountsHolder.getZongCommissionAmount()!=null && commissionAmountsHolder.getZongCommissionAmount().doubleValue()!=0.0)
    			{
    				double zongWHTAmount = calculateVariableCommissionAmount(whtSharePercentage, commissionAmountsHolder.getZongCommissionAmount());
    				whtAmount = whtAmount + zongWHTAmount;
    				commissionAmountsHolder.setZongCommissionAmount(commissionAmountsHolder.getZongCommissionAmount() - zongWHTAmount);
    			}
    			if(commissionAmountsHolder.getI8CommissionAmount()!=null && commissionAmountsHolder.getI8CommissionAmount().doubleValue()!=0.0)
    			{
    				double i8WHTAmount = calculateVariableCommissionAmount(whtSharePercentage, commissionAmountsHolder.getI8CommissionAmount());
    				whtAmount = whtAmount + i8WHTAmount;
    				commissionAmountsHolder.setI8CommissionAmount(commissionAmountsHolder.getI8CommissionAmount() - i8WHTAmount);
    			}
    			if(commissionAmountsHolder.getAgent1CommissionAmount()!=null && commissionAmountsHolder.getAgent1CommissionAmount().doubleValue()!=0.0)
    			{
    				double agent1WHTAmount = calculateVariableCommissionAmount(whtSharePercentage, commissionAmountsHolder.getAgent1CommissionAmount());
    				whtAmount = whtAmount + agent1WHTAmount;
    				commissionAmountsHolder.setAgent1CommissionAmount(commissionAmountsHolder.getAgent1CommissionAmount() - agent1WHTAmount);
    			}
    			if(commissionAmountsHolder.getAgent2CommissionAmount()!=null && commissionAmountsHolder.getAgent2CommissionAmount().doubleValue()!=0.0)
    			{
    				double agent2WHTAmount = calculateVariableCommissionAmount(whtSharePercentage, commissionAmountsHolder.getAgent2CommissionAmount());
    				whtAmount = whtAmount + agent2WHTAmount;
    				commissionAmountsHolder.setAgent2CommissionAmount(commissionAmountsHolder.getAgent2CommissionAmount() - agent2WHTAmount);
    			}
    			//added by mudassir -  franchise commission change 
    			if(commissionAmountsHolder.getFranchise1CommissionAmount()!=null && commissionAmountsHolder.getFranchise1CommissionAmount().doubleValue()!=0.0)
    			{
    				double fr1WHTAmount = calculateVariableCommissionAmount(whtSharePercentage, commissionAmountsHolder.getFranchise1CommissionAmount());
    				whtAmount = whtAmount + fr1WHTAmount;
    				commissionAmountsHolder.setFranchise1CommissionAmount(commissionAmountsHolder.getFranchise1CommissionAmount() - fr1WHTAmount);
    			}
    			if(commissionAmountsHolder.getFranchise2CommissionAmount()!=null && commissionAmountsHolder.getFranchise2CommissionAmount().doubleValue()!=0.0)
    			{
    				double fr2WHTAmount = calculateVariableCommissionAmount(whtSharePercentage, commissionAmountsHolder.getFranchise2CommissionAmount());
    				whtAmount = whtAmount + fr2WHTAmount;
    				commissionAmountsHolder.setFranchise2CommissionAmount(commissionAmountsHolder.getFranchise2CommissionAmount() - fr2WHTAmount);
    			}
    			commissionAmountsHolder.setWhtCommissionAmount(whtAmount);
    		}
    	}
    }

    
    //int fixedCommission = 1; // This vasriable is declared for the purpose of the code clarity.
    if (commissionRateModel.getCommissionTypeId().longValue() == CommissionConstantsInterface.FIXED_COMMISSION.longValue() &&
        commissionRateModel.getExtra())
    {
    	if(logger.isDebugEnabled())
		{
		  logger.debug("Commission is FIXED and EXTRA....");
		}
      totalCommissionAmount += commissionRateModel.getRate();
      totalAmount += commissionRateModel.getRate();
      commissionAmount = commissionRateModel.getRate();
      transactionProcessingAmount += commissionRateModel.getRate();
    }
    else if (commissionRateModel.getCommissionTypeId().longValue() == CommissionConstantsInterface.FIXED_COMMISSION.longValue()
             && commissionRateModel.getExtra() == false)
    {
    	if(logger.isDebugEnabled())
		{
		  logger.debug("Commission is FIXED and NOT EXTRA....");
		}
      totalCommissionAmount += commissionRateModel.getRate();
      commissionAmount = commissionRateModel.getRate();
      billingOrganizationAmount -= commissionRateModel.getRate();
    }
    else if (commissionRateModel.getCommissionTypeId().longValue() !=
             CommissionConstantsInterface.FIXED_COMMISSION.longValue() && commissionRateModel.getExtra())
    {
    	if(logger.isDebugEnabled())
		{
		  logger.debug("Commission is VARIABLE and EXTRA....");
		}
      commissionAmount = this.calculateVariableCommissionAmount(
          commissionRateModel.getRate(), transactionAmount);
      totalCommissionAmount += commissionAmount;
      totalAmount += commissionAmount;
      transactionProcessingAmount += commissionAmount;
    }
    else if (commissionRateModel.getCommissionTypeId().longValue() !=
             CommissionConstantsInterface.FIXED_COMMISSION.longValue() && commissionRateModel.getExtra() == false)
    {
    	if(logger.isDebugEnabled())
		{
		  logger.debug("Commission is VARIABLE and NOT EXTRA....");
		}
      commissionAmount = this.calculateVariableCommissionAmount(
          commissionRateModel.getRate(), transactionAmount);
      totalCommissionAmount += commissionAmount;
      billingOrganizationAmount -= commissionAmount;
    }
    

//    if( commissionRateModel.getCommissionStakeholderIdCommissionStakeholderModel().getStakeholderTypeId().longValue() != StakeholderTypeConstants.BILLING_ORGANIZATION.longValue() )
//      billingOrganizationAmount -= commissionAmount ;

    
  //added by mudassir.. rounding commissionn to TWO decimals
    this.roundCommissonRates(commissionAmountsHolder);
    
    after rounding, calculate askari commission amount by subtracting all stakeholders commission amount from total commmission amount.
     * this is fix applied to bug id 457. 
    
    
    double askariCommissionAmount = totalCommissionAmount - 
    									(commissionAmountsHolder.getZongCommissionAmount() + commissionAmountsHolder.getI8CommissionAmount()
    										+ commissionAmountsHolder.getAgent1CommissionAmount() + commissionAmountsHolder.getAgent2CommissionAmount()
    										+ commissionAmountsHolder.getWhtCommissionAmount() + commissionAmountsHolder.getFranchise1CommissionAmount()
    										+ commissionAmountsHolder.getFranchise2CommissionAmount());
    
    commissionAmountsHolder.setAskariCommissionAmount(this.roundTwoDecimals(askariCommissionAmount));
    
    commissionAmountsHolder.setTotalCommissionAmount(this.roundTwoDecimals(totalCommissionAmount));
    commissionAmountsHolder.setTotalAmount(totalAmount);
    commissionAmountsHolder.setBillingOrganizationAmount(billingOrganizationAmount);
    commissionAmountsHolder.setTransactionProcessingAmount(transactionProcessingAmount);
    if(logger.isDebugEnabled())
	{
	  logger.debug("Ending processCommissionRate method of CommissionManagerImpl....");
	}
  }
*/

    /**
     * <p>
     * 1. Determine Agent Level
     * 2. Find Agents up in the hierarchy up to predefined level. (List of AppUserModels)
     * 3. Calculate agents commissions in loop from agent commission.
     * </p>
     **/
    private void prepareAgentHierarchyCommission(CommissionAmountsHolder commissionAmountsHolder, WorkFlowWrapper workFlowWrapper, Long agentStakeHolderId) throws FrameworkCheckedException {

        Double agentOrignalCommission = commissionAmountsHolder.getStakeholderCommissionsMap().get(agentStakeHolderId);
        Long appUserId = ThreadLocalAppUser.getAppUserModel().getAppUserId();
        if (null == agentOrignalCommission || agentOrignalCommission.doubleValue() == 0d)
            return;
        List<DistributorCommShareViewModel> hierarchySharesList = agentNetworkCommShareManager.findHierarchyCommissoinSharesByAppUserIdAndProductId(appUserId, workFlowWrapper.getProductModel().getProductId());
        if (CollectionUtils.isEmpty(hierarchySharesList)) {
            logger.info("[CommissionManagerImpl.processCommissionRate] Customized Hierarchy Shares not defined. Checking if Default Hierarchy Stakeholders exist.");
            hierarchySharesList = agentNetworkCommShareManager.findHierarchyCommissoinSharesByAppUserIdAndProductId(appUserId, null);
        }
        if (CollectionUtils.isEmpty(hierarchySharesList))
            return;
        logger.info("[CommissionManagerImpl.processCommissionRate] Calculating Hierarchy Stakeholders Commissions. Count: " + hierarchySharesList.size());

//  		double totalHirarchyCommission = 0d;
        Double agentAffectedCommission = agentOrignalCommission;
        TaxValueBean agentTaxBean = commissionAmountsHolder.getStakeholderTaxMap().get(agentStakeHolderId);
        Double agentWHT = agentTaxBean.getWhtAmount();
        Double agentAffectedWHT = agentTaxBean.getWhtAmount();
        Double agentFED = agentTaxBean.getFedAmount();
        Double agentAffectedFED = agentTaxBean.getFedAmount();
        Boolean agent2WhtApplicable = agentTaxBean.getAgent2WhtApplicable();
        for (DistributorCommShareViewModel distCommShareViewModel : hierarchySharesList) {

            //check if this agent is head agent. if yes then skip this iteration to avoid miscalculation. and of course avoid NPE
            if (distCommShareViewModel.getParentAppUserId() == null || distCommShareViewModel.getParentCommissionShare() == null)
                continue;

            //avoid calculation if share is set as zero
            Double parentShare = distCommShareViewModel.getParentCommissionShare();
            if (parentShare == null || parentShare < 0)
                continue;

            double hirarchyShare = calculateVariableCommissionAmount(distCommShareViewModel.getParentCommissionShare(), agentOrignalCommission);
            agentAffectedCommission -= hirarchyShare;
//  			totalHirarchyCommission += hirarchyShare;

            logger.info("[CommissionManagerImpl.processCommissionRate] Hierarchy AppUserID:" + distCommShareViewModel.getParentAppUserId() + " is getting " + distCommShareViewModel.getParentCommissionShare() + " % share " + ". Comission: " + hirarchyShare + ". Agent Commission:" + agentOrignalCommission);
            if (hirarchyShare < 0.0D)
                continue;

            commissionAmountsHolder.getHierarchyStakeholderCommissionMap().put(distCommShareViewModel.getParentAppUserId(), hirarchyShare);


            Double hirarchyWHTShare = calculateVariableCommissionAmount(distCommShareViewModel.getParentCommissionShare(), agentWHT);
            agentAffectedWHT -= hirarchyWHTShare;

            Double hirarchyFEDShare = calculateVariableCommissionAmount(distCommShareViewModel.getParentCommissionShare(), agentFED);
            agentAffectedFED -= hirarchyFEDShare;

            TaxValueBean taxBean = new TaxValueBean(agentTaxBean.getFedRate(), hirarchyFEDShare, agentTaxBean.getWhtRate(), hirarchyWHTShare, agent2WhtApplicable);
            commissionAmountsHolder.getHierarchyStakeholderTaxMap().put(distCommShareViewModel.getParentAppUserId(), taxBean);

        }

//  		if(agentStakeHolderId == CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID.longValue()) 
//  			commissionAmountsHolder.setAgent1HierarchyCommission(totalHirarchyCommission);
//  		else if(agentStakeHolderId == CommissionConstantsInterface.AGENT2_STAKE_HOLDER_ID.longValue())
//  			commissionAmountsHolder.setAgent2HierarchyCommission(totalHirarchyCommission);

        commissionAmountsHolder.getStakeholderCommissionsMap().put(agentStakeHolderId, agentAffectedCommission);
        agentTaxBean.setFedAmount(agentAffectedFED);
        agentTaxBean.setWhtAmount(agentAffectedWHT);
    }

    private void defaultFEDSetup(CommissionAmountsHolder commissionAmountsHolder) {
        commissionAmountsHolder.getStakeholderCommissionsMap().put(CommissionConstantsInterface.FED_STAKE_HOLDER_ID, 0.0);
        commissionAmountsHolder.getStakeholderTaxMap().put(CommissionConstantsInterface.FED_STAKE_HOLDER_ID, new TaxValueBean());
    }

    @Override
    public void calculateHierarchyCommission(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
        CommissionAmountsHolder amountsHolder = workFlowWrapper.getCommissionAmountsHolder();
        Long agentStakeholderId = (Long) workFlowWrapper.getObject("AGENT_STAKEHOLDER_ID");

        prepareAgentHierarchyCommission(amountsHolder, workFlowWrapper, agentStakeholderId);
    }

    private void roundCommissonRates(CommissionAmountsHolder commissionAmountsHolder) {

        commissionAmountsHolder.setTotalAmount(roundTwoDecimals(commissionAmountsHolder.getTotalAmount()));
        commissionAmountsHolder.setTotalCommissionAmount(roundTwoDecimals(commissionAmountsHolder.getTotalCommissionAmount()));
        commissionAmountsHolder.setTransactionAmount(roundTwoDecimals(commissionAmountsHolder.getTransactionAmount()));
        commissionAmountsHolder.setBillingOrganizationAmount(roundTwoDecimals(commissionAmountsHolder.getBillingOrganizationAmount()));
        commissionAmountsHolder.setTransactionProcessingAmount(roundTwoDecimals(commissionAmountsHolder.getTransactionProcessingAmount()));
        commissionAmountsHolder.setSupplierCharges(roundTwoDecimals(commissionAmountsHolder.getSupplierCharges()));
        commissionAmountsHolder.setServiceCharges(roundTwoDecimals(commissionAmountsHolder.getServiceCharges()));
        commissionAmountsHolder.setI8SupplierCharges(roundTwoDecimals(commissionAmountsHolder.getI8SupplierCharges()));
        commissionAmountsHolder.setI8ServiceCharges(roundTwoDecimals(commissionAmountsHolder.getI8ServiceCharges()));

        for (Map.Entry<Long, Double> stakeholderShareEntry : commissionAmountsHolder.getStakeholderCommissionsMap().entrySet()) {
            Long stakeholderId = stakeholderShareEntry.getKey();
            Double stakeholderShare = stakeholderShareEntry.getValue();
            commissionAmountsHolder.getStakeholderCommissionsMap().put(stakeholderId, roundTwoDecimals(stakeholderShare));
        }

        for (Map.Entry<Long, Double> hierarchyShareEntry : commissionAmountsHolder.getHierarchyStakeholderCommissionMap().entrySet()) {
            Long appUserId = hierarchyShareEntry.getKey();
            Double stakeholderShare = hierarchyShareEntry.getValue();
            commissionAmountsHolder.getHierarchyStakeholderCommissionMap().put(appUserId, roundTwoDecimals(stakeholderShare));
        }

    }


    private void adjustStakeholderWHT(CommissionAmountsHolder commissionAmountsHolder) {
        Double totalWhtAmount = CommonUtils.getDoubleOrDefaultValue(commissionAmountsHolder.getStakeholderCommissionsMap().get(CommissionConstantsInterface.WHT_STAKE_HOLDER_ID));
        Double sumWhtAmount = 0D;
        TaxValueBean lastWHTEntry = null;
        Long lastWHTKey = 0L;

        if (totalWhtAmount > 0) {
            for (Map.Entry<Long, TaxValueBean> entry : commissionAmountsHolder.getStakeholderTaxMap().entrySet()) {
                if (entry.getValue().getWhtAmount() > 0) {
                    sumWhtAmount += entry.getValue().getWhtAmount();
                    lastWHTEntry = entry.getValue();
                    lastWHTKey = entry.getKey();
                }
            }
            for (Map.Entry<Long, TaxValueBean> entry : commissionAmountsHolder.getHierarchyStakeholderTaxMap().entrySet()) {
                if (entry.getValue().getWhtAmount() > 0) {
                    sumWhtAmount += entry.getValue().getWhtAmount();
                    lastWHTEntry = entry.getValue();
                    lastWHTKey = entry.getKey();
                }
            }

            sumWhtAmount = roundTwoDecimals(sumWhtAmount);

            if (lastWHTEntry != null && sumWhtAmount > 0 && sumWhtAmount.doubleValue() != totalWhtAmount.doubleValue()) {
                if (sumWhtAmount > totalWhtAmount) {
                    Double diff = sumWhtAmount - totalWhtAmount;
                    lastWHTEntry.setWhtAmount(lastWHTEntry.getWhtAmount() - diff);
                    logger.info("$$$$$$$$ adjustStakeholderWHT() totalWhtAmount:" + totalWhtAmount + ", Sum of individual WHTs:" + sumWhtAmount);
                    logger.info("$$$$$$$$ subtracting (" + diff + ") in WHT of StakeholderId:" + lastWHTKey);
                } else if (sumWhtAmount < totalWhtAmount) {
                    Double diff = totalWhtAmount - sumWhtAmount;
                    lastWHTEntry.setWhtAmount(lastWHTEntry.getWhtAmount() + diff);
                    logger.info("$$$$$$$$ adjustStakeholderWHT() totalWhtAmount:" + totalWhtAmount + ", Sum of individual WHTs:" + sumWhtAmount);
                    logger.info("$$$$$$$$ adding (" + diff + ") in WHT of StakeholderId:" + lastWHTKey);
                }
            }
        }

    }

    private void roundTaxAmount(CommissionAmountsHolder commissionAmountsHolder) {

        for (Map.Entry<Long, TaxValueBean> taxEntry : commissionAmountsHolder.getStakeholderTaxMap().entrySet()) {
            taxEntry.getValue().roundTwoDecimals();
        }

        for (Map.Entry<Long, TaxValueBean> taxEntry : commissionAmountsHolder.getHierarchyStakeholderTaxMap().entrySet()) {
            taxEntry.getValue().roundTwoDecimals();
        }

    }

    private Double roundTwoDecimals(Double value) {
        Double roundedValue = new Double(0.0);
        if (value != null) {
            DecimalFormat twoDForm = new DecimalFormat("#.##");
            roundedValue = Double.valueOf(twoDForm.format(value));
        }

        return roundedValue;
    }

    /**
     * Method used for the purpose of the calculation of amount against the given percentage.
     *
     * @param commissionPercentage double
     * @param transactionAmount    double
     * @return double
     */
    private double calculateVariableCommissionAmount(double commissionPercentage, double transactionAmount) {
        return roundTwoDecimals((transactionAmount * commissionPercentage) / 100);
    }

    public void setCommissionShSharesDAO(CommissionShSharesDAO commissionShSharesDAO) {
        this.commissionShSharesDAO = commissionShSharesDAO;
    }

    public SearchBaseWrapper getCommissionTransactionModel(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {

        ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
        exampleHolder.setMatchMode(MatchMode.EXACT);

        CustomList<CommissionTransactionModel> list = this.commissionTransactionDAO.findByExample((CommissionTransactionModel) searchBaseWrapper.getBasePersistableModel(), searchBaseWrapper
                .getPagingHelperModel(), null, exampleHolder);

        if (null != list && null != list.getResultsetList() && list.getResultsetList().size() > 0) {
            CommissionTransactionModel commissionTxModel = (CommissionTransactionModel) list.getResultsetList().get(0);
            searchBaseWrapper.setBasePersistableModel(commissionTxModel);

        } else {

            searchBaseWrapper.setBasePersistableModel(null);
        }

        return searchBaseWrapper;
    }

    @Override
    public BaseWrapper loadAgentCommission(BaseWrapper baseWrapper)
            throws FrameworkCheckedException {
        ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
        exampleHolder.setMatchMode(MatchMode.EXACT);
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setBasePersistableModel(baseWrapper.getBasePersistableModel());
        CustomList<AgentEarnedComSummaryModel> list = this.agentCommissionSummaryDAO.findByExample((AgentEarnedComSummaryModel) searchBaseWrapper.getBasePersistableModel(), searchBaseWrapper
                .getPagingHelperModel(), null, exampleHolder);


        if (null != list && null != list.getResultsetList() && list.getResultsetList().size() > 0) {
            AgentEarnedComSummaryModel agentEarnedComSummaryModel = (AgentEarnedComSummaryModel) list.getResultsetList().get(0);
            baseWrapper.setBasePersistableModel(agentEarnedComSummaryModel);

        } else {

            baseWrapper.setBasePersistableModel(null);
        }

        return baseWrapper;
    }

    private boolean checkCommissionSlabsForDefaultRates(List<CommissionRateModel> commissionRateList, Double transactionAmount) {
        boolean applyDefRate = true;

        Iterator<CommissionRateModel> listIterator = commissionRateList.iterator();
        CommissionRateModel commissionRate;

        while (listIterator.hasNext()) {
            commissionRate = (CommissionRateModel) listIterator.next();

            if ((commissionRate.getRangeEnds() != null && transactionAmount > commissionRate.getRangeEnds()) || !commissionRate.getActive()) {
                listIterator.remove();
            } else if ((commissionRate.getRangeStarts() != null && transactionAmount < commissionRate.getRangeStarts())) {
                listIterator.remove();
            } else if (commissionRate.getRangeEnds() != null
                    && transactionAmount <= commissionRate.getRangeEnds()
                    && commissionRate.getRangeStarts() != null
                    && transactionAmount >= commissionRate.getRangeStarts()) {

                logger.info("[CommissionManagerImpl.checkCommissionSlabsForDefaultRates] Slab found for trx amount: " + transactionAmount);
                applyDefRate = false;
                break;
            }

        }

        logger.info("[CommissionManagerImpl.CalculateCommission] Default Rate Applied: " + applyDefRate + " LoggedIn AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());

        return applyDefRate;
    }


    public Boolean updateCommissionTransaction(Object[] params) {

        return commissionTransactionDAO.updateCommissionTransaction(null, null, params, null, null);
    }

    public CommissionAmountsHolder loadCommissionDetails(Long transactionDetailId) throws FrameworkCheckedException {
        CommissionAmountsHolder commissionAmountsHolder = new CommissionAmountsHolder(true);

        List<CommissionTransactionModel> commTrxList = getCommissionTransactionModelList(transactionDetailId, false);

        for (CommissionTransactionModel commTrxModel : commTrxList) {
            if (commTrxModel.getHierarchyAppUserId() != null) {

                commissionAmountsHolder.getHierarchyStakeholderCommissionMapLeg1().put(commTrxModel.getHierarchyAppUserId(), commTrxModel.getCommissionAmount());

                commissionAmountsHolder.getHierarchyStakeholderTaxMap().put(commTrxModel.getHierarchyAppUserId(),
                        new TaxValueBean(commTrxModel.getFedRate(), commTrxModel.getFedAmount(), commTrxModel.getWhtRate(), commTrxModel.getWhtAmount(), commTrxModel.getWhtApplicable()));

            } else {

                commissionAmountsHolder.getStakeholderCommissionsMap().put(commTrxModel.getCommissionStakeholderId(), commTrxModel.getCommissionAmount());

                commissionAmountsHolder.getStakeholderTaxMap().put(commTrxModel.getCommissionStakeholderId(),
                        new TaxValueBean(commTrxModel.getFedRate(), commTrxModel.getFedAmount(), commTrxModel.getWhtRate(), commTrxModel.getWhtAmount(), commTrxModel.getWhtApplicable()));
            }
            commissionAmountsHolder.setTotalCommissionAmount(commissionAmountsHolder.getTotalCommissionAmount() + commTrxModel.getCommissionAmount());
        }

        return commissionAmountsHolder;

    }

    public CommissionAmountsHolder loadCommissionDetailsUnsettled(Long transactionDetailId) throws FrameworkCheckedException {
        CommissionAmountsHolder commissionAmountsHolder = new CommissionAmountsHolder(true);

        List<CommissionTransactionModel> commTrxList = getCommissionTransactionModelList(transactionDetailId, true);
        double unsettledWHTAmount = 0.0;
        boolean isWHTAlreadySettled = true;
        for (CommissionTransactionModel commTrxModel : commTrxList) {
            if (commTrxModel.getHierarchyAppUserId() != null) {

                commissionAmountsHolder.getHierarchyStakeholderCommissionMapLeg1().put(commTrxModel.getHierarchyAppUserId(), commTrxModel.getCommissionAmount());

                commissionAmountsHolder.getHierarchyStakeholderTaxMap().put(commTrxModel.getHierarchyAppUserId(),
                        new TaxValueBean(commTrxModel.getFedRate(), commTrxModel.getFedAmount(), commTrxModel.getWhtRate(), commTrxModel.getWhtAmount(), commTrxModel.getWhtApplicable()));

            } else {

                commissionAmountsHolder.getStakeholderCommissionsMap().put(commTrxModel.getCommissionStakeholderId(), commTrxModel.getCommissionAmount());

                commissionAmountsHolder.getStakeholderTaxMap().put(commTrxModel.getCommissionStakeholderId(),
                        new TaxValueBean(commTrxModel.getFedRate(), commTrxModel.getFedAmount(), commTrxModel.getWhtRate(), commTrxModel.getWhtAmount(), commTrxModel.getWhtApplicable()));
            }

            if (commTrxModel.getCommissionStakeholderId().longValue() == CommissionConstantsInterface.WHT_STAKE_HOLDER_ID.longValue()
                    && commTrxModel.getCommissionAmount() > 0D) {

                isWHTAlreadySettled = false;
            }

            if (commTrxModel.getWhtAmount() != null && commTrxModel.getWhtAmount() > 0) {
                unsettledWHTAmount += commTrxModel.getWhtAmount();
            }

            commissionAmountsHolder.setTotalCommissionAmountUnsettled(commissionAmountsHolder.getTotalCommissionAmountUnsettled() + commTrxModel.getCommissionAmount());
        }

        if (unsettledWHTAmount > 0.0 && isWHTAlreadySettled) {
            commissionAmountsHolder.getStakeholderCommissionsMap().put(CommissionConstantsInterface.WHT_STAKE_HOLDER_ID, unsettledWHTAmount);
            commissionAmountsHolder.getStakeholderTaxMap().put(CommissionConstantsInterface.WHT_STAKE_HOLDER_ID, new TaxValueBean(0D, 0D, 0D, 0D, false));
            commissionAmountsHolder.setTotalCommissionAmountUnsettled(commissionAmountsHolder.getTotalCommissionAmountUnsettled() + unsettledWHTAmount);
        }

        return commissionAmountsHolder;

    }

    private List<CommissionTransactionModel> getCommissionTransactionModelList(Long transactionDetailId, boolean loadPendingCommissionOnly) throws FrameworkCheckedException {
        List<CommissionTransactionModel> commTrxlist = new ArrayList<CommissionTransactionModel>();
        CommissionTransactionModel commissionTxModel = new CommissionTransactionModel();
        commissionTxModel.setTransactionDetailId(transactionDetailId);
        if (loadPendingCommissionOnly) {
            commissionTxModel.setSettled(false);
        }

        ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
        exampleHolder.setMatchMode(MatchMode.EXACT);

        CustomList<CommissionTransactionModel> list = this.commissionTransactionDAO.findByExample(commissionTxModel, null, null, exampleHolder);

        if (null != list && null != list.getResultsetList() && list.getResultsetList().size() > 0) {
            commTrxlist = list.getResultsetList();
        }

        return commTrxlist;
    }


    private Double calculateFilerNonFilerRate() throws FrameworkCheckedException {

        double totalWhtAmount = 0.0;
        double whtSharePercentage = 0d;
        Long appUserId = ThreadLocalAppUser.getAppUserModel().getAppUserId();
        Boolean filer = ThreadLocalAppUser.getAppUserModel().getFiler();
        filer = CommonUtils.getDefaultIfNull(filer, false);

        WHTConfigModel wConfigModel = taxManager.loadCommissionWHTConfigModel(appUserId);
        if (wConfigModel == null) {
            return 0.0;
        } else {
            WHTConfigModel whtConfigModel = taxManager.loadWHTConfigModel(TaxConstantsInterface.WHT_CONFIG_COMMISSION_ID);
            if (whtConfigModel == null) {
                throw new FrameworkCheckedException("Wht Configuration Rate not defined!");

            } else if (filer) {
                whtSharePercentage = whtConfigModel.getFilerRate();
            } else {
                whtSharePercentage = whtConfigModel.getNonFilerRate();
            }
        }

        return whtSharePercentage;
    }

    public void makeAgent2CommissionSettlement(WorkFlowWrapper wrapper) throws Exception {

        CommissionAmountsHolder amountsHolder = wrapper.getCommissionAmountsHolder();
        Double agent2CommAmount = amountsHolder.getStakeholderCommissionsMap().get(CommissionConstantsInterface.AGENT2_STAKE_HOLDER_ID);
        if (agent2CommAmount == null || agent2CommAmount.doubleValue() == 0d) {
            logger.info("Agent 2 Commission not applied.");
            return;
        }

        logger.info("[CommissionManagerImpl.makeAgent2CommissionSettlement] Loading CommissionTransaction of Agent 2. Trx ID:" + wrapper.getTransactionCodeModel().getCode() + " Agent SmartMoneyAccountId: " + wrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());

        CommissionTransactionModel agent2CommissoinTxModel = new CommissionTransactionModel();
        agent2CommissoinTxModel.setCommissionStakeholderId(CommissionConstantsInterface.AGENT2_STAKE_HOLDER_ID);
        agent2CommissoinTxModel.setTransactionDetailId(wrapper.getTransactionDetailModel().getTransactionDetailId());

        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setBasePersistableModel(agent2CommissoinTxModel);

        searchBaseWrapper = this.getCommissionTransactionModel(searchBaseWrapper);

        agent2CommissoinTxModel = (CommissionTransactionModel) searchBaseWrapper.getBasePersistableModel();

        boolean inclChargesPaidBy3rdParty = false;
        if (wrapper.getTransactionDetailMasterModel().getThirdPartyCheck() != null && wrapper.getTransactionDetailMasterModel().getThirdPartyCheck()) {
            inclChargesPaidBy3rdParty = true;
        }

        if (agent2CommissoinTxModel != null && agent2CommissoinTxModel.getCommissionAmount() != null && agent2CommissoinTxModel.getCommissionAmount().doubleValue() > 0d) {


            //********************************** WHT on Leg II - Start
            Double whtSharePercentage = calculateFilerNonFilerRate();
            double agent2WhtAmountCalculated = 0;

            Boolean agent2WhtApplicable = agent2CommissoinTxModel.getWhtApplicable();
            agent2WhtApplicable = agent2WhtApplicable == null ? false : agent2WhtApplicable;

            if (agent2WhtApplicable) {
                double oldAgent2Comm = CommonUtils.getDoubleOrDefaultValue(agent2CommissoinTxModel.getCommissionAmount());
                double oldAgent2Wht = CommonUtils.getDoubleOrDefaultValue(agent2CommissoinTxModel.getWhtAmount());
                double agent2WhtRateCalculated = whtSharePercentage;
                agent2WhtAmountCalculated = calculateVariableCommissionAmount(agent2WhtRateCalculated, oldAgent2Comm);

                double agent2CommCalculated = roundTwoDecimals(oldAgent2Comm + oldAgent2Wht - agent2WhtAmountCalculated);

                agent2CommissoinTxModel.setWhtRate(agent2WhtRateCalculated);
                agent2CommissoinTxModel.setWhtAmount(agent2WhtAmountCalculated);
                agent2CommissoinTxModel.setCommissionAmount(agent2CommCalculated);


                CommissionTransactionModel whtTxModel = new CommissionTransactionModel();
                whtTxModel.setCommissionStakeholderId(CommissionConstantsInterface.WHT_STAKE_HOLDER_ID);
                whtTxModel.setTransactionDetailId(wrapper.getTransactionDetailModel().getTransactionDetailId());

                SearchBaseWrapper sbWrapper = new SearchBaseWrapperImpl();
                sbWrapper.setBasePersistableModel(whtTxModel);
                sbWrapper = this.getCommissionTransactionModel(sbWrapper);
                whtTxModel = (CommissionTransactionModel) sbWrapper.getBasePersistableModel();
                if (whtTxModel != null && whtTxModel.getCommissionTransactionId() != null) {
                    double oldTotalWht = CommonUtils.getDoubleOrDefaultValue(whtTxModel.getCommissionAmount());
                    double totalWhtCalculated = roundTwoDecimals(oldTotalWht + agent2WhtAmountCalculated);
                    whtTxModel.setCommissionAmount(totalWhtCalculated);
                } else {
                    whtTxModel = prepareCommTransactionModelForWHT(wrapper, agent2WhtAmountCalculated);
                }

                settlementManager.saveCommissionTransactionMoel(whtTxModel);
                wrapper.getTransactionDetailMasterModel().setWht(whtTxModel.getCommissionAmount());

                Double unsettledWhtAmount = CommonUtils.getDoubleOrDefaultValue(amountsHolder.getStakeholderCommissionsMap().get(CommissionConstantsInterface.WHT_STAKE_HOLDER_ID));

                amountsHolder.getStakeholderCommissionsMap().put(CommissionConstantsInterface.WHT_STAKE_HOLDER_ID, roundTwoDecimals(unsettledWhtAmount + agent2WhtAmountCalculated));
                amountsHolder.getStakeholderCommissionsMap().put(CommissionConstantsInterface.AGENT2_STAKE_HOLDER_ID, agent2CommissoinTxModel.getCommissionAmount());
            }

            //********************************** WHT on Leg II - END

            TaxValueBean agent2TaxBean = new TaxValueBean();
            agent2TaxBean.setWhtAmount(agent2CommissoinTxModel.getWhtAmount());
            agent2TaxBean.setWhtRate(agent2CommissoinTxModel.getWhtRate());
            agent2TaxBean.setFedAmount(agent2CommissoinTxModel.getFedAmount());
            agent2TaxBean.setFedRate(agent2CommissoinTxModel.getFedRate());
            double agent2CommBeforeHierarchy = CommonUtils.getDoubleOrDefaultValue(agent2CommissoinTxModel.getCommissionAmount());
            double agent2WHTBeforeHierarchy = CommonUtils.getDoubleOrDefaultValue(agent2CommissoinTxModel.getWhtAmount());

            wrapper.putObject("AGENT2_TAX_BEAN", agent2TaxBean);
            amountsHolder.getStakeholderTaxMap().put(CommissionConstantsInterface.AGENT2_STAKE_HOLDER_ID, agent2TaxBean);

            CommissionWrapper commissionWrapper = this.calculateAgent2HierarchyCommission(wrapper, agent2CommissoinTxModel.getCommissionAmount());
            settlementManager.settleHierarchyCommission(commissionWrapper, wrapper);

            amountsHolder.setTotalCommissionAmount(wrapper.getTransactionModel().getTotalCommissionAmount());

            logger.info("[CommissionManagerImpl.makeAgent2CommissionSettlement] Saving CommissionTransaction of Agent 2. Trx ID:" + wrapper.getTransactionCodeModel().getCode() + " Agent CommissionTransactionId: " + agent2CommissoinTxModel.getCommissionTransactionId());
            TaxValueBean agent2TaxAmount = amountsHolder.getStakeholderTaxMap().get(CommissionConstantsInterface.AGENT2_STAKE_HOLDER_ID);
            agent2CommissoinTxModel.setCommissionAmount(amountsHolder.getStakeholderCommissionsMap().get(CommissionConstantsInterface.AGENT2_STAKE_HOLDER_ID));
            agent2CommissoinTxModel.setWhtAmount(agent2TaxAmount.getWhtAmount());
            agent2CommissoinTxModel.setFedAmount(agent2TaxAmount.getFedAmount());
            agent2CommissoinTxModel.setSettled(true);
            agent2CommissoinTxModel.setUpdatedOn(new java.util.Date());
            wrapper.getTransactionDetailMasterModel().setAgent2Commission(agent2CommissoinTxModel.getCommissionAmount());

            settlementManager.saveCommissionTransactionMoel(agent2CommissoinTxModel);

            amountsHolder.setAgent2CommissionAmount(agent2CommissoinTxModel.getCommissionAmount());
            amountsHolder.setAgent2CommAmountBeforeHierarchy(agent2CommBeforeHierarchy);
            amountsHolder.setAgent2WHTAmount(agent2WHTBeforeHierarchy);
            amountsHolder.setTransactionAmount(wrapper.getTransactionDetailMasterModel().getTransactionAmount());
            amountsHolder.setTotalInclusiveAmount(inclChargesPaidBy3rdParty ? 0.00D : wrapper.getTransactionDetailMasterModel().getInclusiveCharges());

        }
    }

    /**
     * This method calculates the commission on this product and transaction.The wrapper should have product,payment mode
     * and principal amount that is passed onto the commission module
     *
     * @param wrapper WorkFlowWrapper
     * @return CommissionWrapper
     */
    @SuppressWarnings("unchecked")
    private CommissionWrapper calculateAgent2HierarchyCommission(WorkFlowWrapper wrapper, Double agent2Commission) throws Exception {
        logger.info("[CommissionManagerImpl.calculateAgent2HierarchyCommission] calculating Hierarchy Commission...");
        CommissionAmountsHolder amountsHolder = wrapper.getCommissionAmountsHolder();

        wrapper.putObject("AGENT_STAKEHOLDER_ID", CommissionConstantsInterface.AGENT2_STAKE_HOLDER_ID);
        CommissionWrapper commissionWrapper = new CommissionWrapperImpl();

        @SuppressWarnings("rawtypes")
        HashMap commissionModuleHashMap = new HashMap();
        commissionModuleHashMap.put(CommissionConstantsInterface.COMMISSION_RATE_LIST, new ArrayList<CommissionRateModel>());
        commissionModuleHashMap.put(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER, amountsHolder);
        commissionWrapper.setCommissionWrapperHashMap(commissionModuleHashMap);

        commissionWrapper.setPaymentModeModel(wrapper.getTransactionModel().getPaymentModeIdPaymentModeModel());
        commissionWrapper.setTransactionModel(wrapper.getTransactionModel());
        commissionWrapper.setTransactionTypeModel(wrapper.getTransactionTypeModel());
        commissionWrapper.setProductModel(wrapper.getProductModel());

        this.calculateHierarchyCommission(wrapper);

        return commissionWrapper;
    }

    public void saveUnsettledCommission(UnsettledAgentCommModel model, Long agentAppUserId) throws FrameworkCheckedException {
        String agentID = this.getUserIdFromAppUserId(agentAppUserId);
        model.setAgentId(agentID);
        model.setCreatedOn(new Date());
        this.unsettledAgentCommDAO.saveOrUpdate(model);
    }

    private String getUserIdFromAppUserId(Long agentAppUserId) {
        String agentID = "";
        try {
            if (agentAppUserId != null && agentAppUserId > 0) {
                UserDeviceAccountsModel userDeviceAccountsModel = this.userDeviceAccountsDAO.findUserDeviceAccountByAppUserId(agentAppUserId);
                if (userDeviceAccountsModel != null) {
                    agentID = userDeviceAccountsModel.getUserId();
                }
            }
        } catch (Exception ex) {
            logger.error("Unable to load agent ID for UnsettledAgentComm. Criteria [agentAppUserId=" + agentAppUserId + "]");
        }
        return agentID;
    }

    public SearchBaseWrapper searchUnsettledAgentCommission(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        CustomList<UnsettledAgentCommModel> list = this.unsettledAgentCommDAO.findByExample(
                (UnsettledAgentCommModel) searchBaseWrapper.getBasePersistableModel(),
                searchBaseWrapper.getPagingHelperModel(),
                searchBaseWrapper.getSortingOrderMap(),
                searchBaseWrapper.getDateRangeHolderModel());

        if (list != null) {
            searchBaseWrapper.setCustomList(list);
        }
        return searchBaseWrapper;
    }

    private void populateStakeholderFilerMap(CommissionAmountsHolder amountsHolder, List<CommissionStakeholderModel> commissionStakeholderModelList) {
        for (CommissionStakeholderModel model : commissionStakeholderModelList) {
            amountsHolder.getStakeholderFilerMap().put(model.getCommissionStakeholderId(), model.getFiler());
        }
    }

    private CommissionTransactionModel prepareCommTransactionModelForWHT(WorkFlowWrapper workFlowWrapper, double whtAmount) throws FrameworkCheckedException {
        try {
            CommissionTransactionModel commissionTransactionModel = new CommissionTransactionModel();

            @SuppressWarnings("rawtypes")
            TransactionDetailModel transactionDetailModel = (TransactionDetailModel) ((List) workFlowWrapper.getTransactionModel()
                    .getTransactionIdTransactionDetailModelList()).get(0);

            commissionTransactionModel.setCommissionAmount(whtAmount);

            commissionTransactionModel.setProductId(workFlowWrapper.getProductModel().getProductId());
            commissionTransactionModel.setCommissionTypeId(CommissionConstantsInterface.FIXED_COMMISSION);
            commissionTransactionModel.setCommissionStakeholderId(CommissionConstantsInterface.WHT_STAKE_HOLDER_ID);
            commissionTransactionModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
            commissionTransactionModel.setStakeholderBankId(PoolAccountConstantsInterface.HIERARCHY_INCOME_ACCOUNT_ID);
            commissionTransactionModel.setPosted(false);
            commissionTransactionModel.setSettled(true);

            commissionTransactionModel.setTransactionDetailId(transactionDetailModel.getTransactionDetailId());

            commissionTransactionModel.setCreatedOn(new Date());
            commissionTransactionModel.setUpdatedOn(new Date());
            commissionTransactionModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
            commissionTransactionModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());

            commissionTransactionModel.setWhtRate(0D);
            commissionTransactionModel.setWhtAmount(0D);
            commissionTransactionModel.setFedRate(0D);
            commissionTransactionModel.setFedAmount(0D);

            logger.info("\n*******SAVING COMMISSION TRANSACTION (WHT on Leg II) **********\n" +
                    "Product ID : " + commissionTransactionModel.getProductId() + "\n" +
                    "Commission Stakeholder ID : " + CommissionConstantsInterface.WHT_STAKE_HOLDER_ID + "\n" +
                    "Transaction ID : " + (workFlowWrapper.getTransactionCodeModel() == null ? (" NULL") : workFlowWrapper.getTransactionCodeModel().getCode()) + "\n" +
                    "Transaction Detail ID : " + transactionDetailModel.getTransactionDetailId() + "\n******************************************");

            return commissionTransactionModel;

        } catch (Exception ex) {
            logger.error("[CommissionManagerImpl.prepareCommissionTransactionModel] Exception occured for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Exception Msg: " + ex.getMessage());
            throw new FrameworkCheckedException(ex.getMessage(), ex);
        }
    }


    public Boolean removeCommissionTransactionModel(CommissionTransactionModel commissionTxModel) {
        return commissionTransactionHibernateDAO.removeCommissionTransactionModel(commissionTxModel);
    }

    public void setCommissionTransactionDAO(CommissionTransactionDAO commissionTransactionDAO) {
        this.commissionTransactionDAO = commissionTransactionDAO;
    }

    public CommissionTransactionHibernateDAO getCommissionTransactionHibernateDAO() {
        return commissionTransactionHibernateDAO;
    }

    public void setCommissionTransactionHibernateDAO(CommissionTransactionHibernateDAO commissionTransactionHibernateDAO) {
        this.commissionTransactionHibernateDAO = commissionTransactionHibernateDAO;
    }

    public void setAgentCommissionSummaryDAO(
            AgentCommissionSummaryDAO agentCommissionSummaryDAO) {
        this.agentCommissionSummaryDAO = agentCommissionSummaryDAO;
    }

    @Override
    public Boolean updateCommissionTransaction(Boolean isSettled, Boolean isPosted, Object[] params, TransactionProductEnum productEnum, Integer legNumber) {

        return commissionTransactionDAO.updateCommissionTransaction(isSettled, isPosted, params, productEnum, legNumber);
    }

    public void setCommissionStakeholderManager(CommissionStakeholderManager commissionStakeholderManager) {
        this.commissionStakeholderManager = commissionStakeholderManager;
    }

    public void setCommissionRateDefaultDAO(
            CommissionRateDefaultHibernateDAO commissionRateDefaultDAO) {
        this.commissionRateDefaultDAO = commissionRateDefaultDAO;
    }

    public void setAgentNetworkCommShareManager(
            AgentNetworkCommShareManager agentNetworkCommShareManager) {
        this.agentNetworkCommShareManager = agentNetworkCommShareManager;
    }

    public void setSettlementManager(SettlementManager settlementManager) {
        this.settlementManager = settlementManager;
    }

    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
    }

    public void setUnsettledAgentCommDAO(UnsettledAgentCommDAO unsettledAgentCommDAO) {
        this.unsettledAgentCommDAO = unsettledAgentCommDAO;
    }

    public void setUserDeviceAccountsDAO(UserDeviceAccountsDAO userDeviceAccountsDAO) {
        this.userDeviceAccountsDAO = userDeviceAccountsDAO;
    }

    public void setTaxManager(TaxManager taxManager) {
        this.taxManager = taxManager;
    }

    public void setCommissionStakeholderDAO(
            CommissionStakeholderDAO commissionStakeholderDAO) {
        this.commissionStakeholderDAO = commissionStakeholderDAO;
    }

    public void setCommissionThresholdRateDAO(CommissionThresholdRateDAO commissionThresholdRateDAO) {
        this.commissionThresholdRateDAO = commissionThresholdRateDAO;
    }
    public CommonCommandManager getCommonCommandManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (CommonCommandManager) applicationContext.getBean("commonCommandManager");
    }
}
