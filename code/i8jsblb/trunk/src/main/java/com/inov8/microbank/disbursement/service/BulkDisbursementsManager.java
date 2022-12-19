package com.inov8.microbank.disbursement.service;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.BBAccountsViewModel;
import com.inov8.microbank.common.model.BulkDisbursementsModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.StakeholderBankInfoModel;
import com.inov8.microbank.common.model.portal.bulkdisbursements.BulkManualAdjustmentModel;
import com.inov8.microbank.common.util.LabelValueBean;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.disbursement.model.BulkDisbursementsFileInfoModel;
import com.inov8.microbank.disbursement.model.DisbursementFileInfoViewModel;
import com.inov8.microbank.disbursement.vo.BulkDisbursementsVOModel;
import com.inov8.microbank.disbursement.vo.DisbursementVO;
import com.inov8.microbank.disbursement.vo.DisbursementWrapper;
import com.inov8.verifly.common.model.AccountInfoModel;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public interface BulkDisbursementsManager {
    BulkDisbursementsModel saveOrUpdateBulkDisbursement(BulkDisbursementsModel bulkDisbursementsModel) throws FrameworkCheckedException;

//    void createOrUpdateBulkDisbursements(CopyOnWriteArrayList<String[]> recordList) throws FrameworkCheckedException;

//    void update(BulkDisbursementsModel model) throws FrameworkCheckedException;

    void makeAcHolderTransferFund(DisbursementVO disbursementVO, WorkFlowWrapper workFlowWrapper) throws Exception;

    SearchBaseWrapper searchBulkDisbursements(SearchBaseWrapper wrapper) throws FrameworkCheckedException;

    SearchBaseWrapper searchBulkPayments(SearchBaseWrapper wrapper) throws FrameworkCheckedException;

//    BulkDisbursementsModel findById(Long id) throws FrameworkCheckedException;

    SearchBaseWrapper loadBulkDisbursement(SearchBaseWrapper wrapper) throws FrameworkCheckedException;

//    BaseWrapper updateBulkDisbursement(BaseWrapper wrapper) throws FrameworkCheckedException;

    List<LabelValueBean> loadBankUsersList() throws FrameworkCheckedException;

//    void saveOrUpdateCollection(List<BulkDisbursementsModel> bulkDisbursementsModels) throws FrameworkCheckedException;

    List<BulkDisbursementsModel> searchBulkDisbursementsModelList(SearchBaseWrapper wrapper) throws FrameworkCheckedException;

    BulkDisbursementsFileInfoModel saveUpdateBulkDisbursementsFileInfoModel(BulkDisbursementsFileInfoModel bulkDisbursementsFileInfoModel);

//    List<BulkDisbursementsFileInfoModel> getBulkDisbursementsFileInfoModelList();

    void saveBulkDisbursementsModelList(BulkDisbursementsVOModel bulkDisbursementsVOModel, BulkDisbursementsFileInfoModel fileInfoModel) throws Exception;

    WorkFlowWrapper nonAcHolderFundTransfer(DisbursementVO bulkDisbursementsModel, WorkFlowWrapper workFlowWrapper) throws Exception;

//    void sendSMS(BaseWrapper baseWrapper) throws Exception;

//    void createBulkWalkInRecords(String batchNo) throws FrameworkCheckedException;

    CommissionWrapper calculateCommission(ProductModel productModel, Double amount, Long segmentId, Boolean isCustomerProduct,Long parm, Boolean calculateShares, Long taxRegimeModelId) throws FrameworkCheckedException;

//    List<String[]> validateMobileNos(CopyOnWriteArrayList<String[]> recordList, BulkDisbursementsFileInfoModel fileInfoModel) throws FrameworkCheckedException;

    void setProductAccounts(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException;

//    BulkDisbursementsFileInfoModel getBulkDisbursementsFileInfoModel(Long fileInfoId);

    BBAccountsViewModel getBBAccountsViewModel(BBAccountsViewModel model) throws FrameworkCheckedException;

//    CustomerModel getCustomerModel(Long customerId);

//    Map<String, Double> createSourceAccountsMap(List<BulkDisbursementsModel> bulkDisbursementsModelList, Map<String, List<BulkDisbursementsModel>> accountListMap, Map<String, Double> accountChargesMap, Boolean isPosted, Boolean isSettled);

    List<DisbursementFileInfoViewModel> findDFIVModelByExample(SearchBaseWrapper searchBaseWrapper) throws Exception;

    Object getDisbursementFileSettlementStatus(String batchNumber);

    int updateDisbursementFileStatus(Long fileInfoId, Integer status);

    void makeCoreFundTransfer(Long serviceId, Date currentDateTime) throws Exception;

    void makeBLBFundTransfer(Long serviceId, Date currentDateTime) throws Exception;

//    AccountInfoModel populateAccountInfo(BBAccountsViewModel bbAccountsViewModel);

    List<DisbursementWrapper> findDueDisbursement(Long serviceId, Date currentDateTime, Boolean isCoreSourceAccountNo, Boolean posted, Boolean settled) throws Exception;

    List<DisbursementWrapper> findDueDisbursementForT24(Long serviceId, Date currentDateTime, Boolean isCoreSourceAccountNo, Boolean posted, Boolean settled) throws Exception;

    void performDisbursementLeg1(DisbursementWrapper wrapper, WorkFlowWrapper workFlowWrapper, Date currentDateTime, Boolean isCoreSourceAccountNo) throws Exception;

    void updatePostedRecords(String batchNumber);
    void updatePostedRecordsForT24(String batchNumber);

    void saveTransactionData(SwitchWrapper switchWrapper);

//    void update(Long disbursementId, String txCode) throws Exception;

    SwitchWrapper performBLBSumFT(AccountInfoModel sourceAccountInfoModel, StakeholderBankInfoModel productBLBAccount,
                                         DisbursementWrapper disbursementWrapper, WorkFlowWrapper workFlowWrapper) throws Exception;

    void performDayEndSettlement(SwitchWrapper switchWrapper) throws Exception;
    
    public List<BulkDisbursementsModel> makeCoreSumFT(DisbursementWrapper disbursementWrapper,
    												StakeholderBankInfoModel stakeholderBankInfoModel,
    												WorkFlowWrapper workFlowWrapper) throws Exception;

    void saveBulkDisbursementWithAuthorization(BaseWrapper baseWrapper) throws FrameworkCheckedException;

    List<BulkDisbursementsModel> loadBulkDisbursementModelList(SearchBaseWrapper searchBaseWrapper) throws Exception;

    void updateIsApprovedForBatch(String batchNumber) throws Exception;

}