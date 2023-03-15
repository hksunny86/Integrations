package com.inov8.microbank.server.dao.transactionmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.TransactionDetailMasterModel;
import com.inov8.microbank.common.model.WalletSafRepoModel;

import java.util.Date;
import java.util.List;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public interface TransactionDetailMasterDAO
    extends BaseDAO<TransactionDetailMasterModel, Long>
{
	Integer updateTransactionProcessingStatus(List<Long> transactionId, Long transactionProcessingStatus, String processingStatusName);
    public List<TransactionDetailMasterModel>  findCustomerPendingTrxByCNIC(String customerCNIC);
    public int  findAgentPendingTrxByCNIC(String agentCNIC);

    public Integer updateTransactionDetailMasterForLeg2(TransactionDetailMasterModel model);

    TransactionDetailMasterModel  findCustomerTrxByTransactionCode(String transactionCode);
    TransactionDetailMasterModel  findTrxByTransactionCodeAndStatus(String transactionCode);
	public List<TransactionDetailMasterModel>  findCustomerPendingTrxByMobile(String recipientMobileNo);
	public List<TransactionDetailMasterModel> getTaggedAgentTransactionDetailList(SearchBaseWrapper searchBaseWrapper);
    List<TransactionDetailMasterModel> findWalInUserPendingTransactions(String mobileNo,String cnic);
    int getCustomerChallanCount(String mobileNo);
    public BaseWrapper loadAndLockTransactionDetailMasterModel(BaseWrapper baseWrapper) throws FrameworkCheckedException;
    public long getPaidChallan(String consumerNo,String productCode);

    TransactionDetailMasterModel  loadTDMbyRRN(String rrn);
    TransactionDetailMasterModel  loadTDMbyReserved2(String reserved2);
    TransactionDetailMasterModel  loadTDMbyMobileNumber(String mobileNo, String productId);
    TransactionDetailMasterModel  loadTDMbyProductId(String mobileNo, String productId);
//    TransactionDetailMasterModel  loadTDMbyMobileandDateRange(String mobileNo, String startDate, String endDate);
    List<TransactionDetailMasterModel> loadTDMbyMobileandDateRange(String mobileNo, Date startDate, Date endDate, String productId) throws FrameworkCheckedException;

}
