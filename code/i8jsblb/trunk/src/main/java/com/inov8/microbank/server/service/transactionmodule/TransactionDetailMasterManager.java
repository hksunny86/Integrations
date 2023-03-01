package com.inov8.microbank.server.service.transactionmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.TransactionDetailMasterModel;

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
public interface TransactionDetailMasterManager{

	BaseWrapper saveTransactionDetailMaster(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	BaseWrapper saveTransactionDetailMasterRequiresNewTransaction(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	void saveTransactionDetailMaster(TransactionDetailMasterModel transactionDetailMasterModel) throws FrameworkCheckedException;
	BaseWrapper loadTransactionDetailMasterModel(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	TransactionDetailMasterModel loadTransactionDetailMasterModel(String transactionCode) throws FrameworkCheckedException;
	BaseWrapper loadAndLockTransactionDetailMasterModel(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	String loadSmartMoneyAccountNickBySmartMoneyAccountId(Long smartMoneyAccountId) throws FrameworkCheckedException;
	String getAgentMobileByRetailerContactId(Long retailerContactId) throws FrameworkCheckedException;
	AppUserModel getAppUserByMobile(String mobileNO) throws FrameworkCheckedException;
	String loadAgentId(Long retailerContactId) throws FrameworkCheckedException;
	ProductModel getProductModelByProductId(Long productId) throws FrameworkCheckedException;
	String getDeviceTypeNameById(Long deviceTypeId) throws FrameworkCheckedException;
	String loadUserIdByMobileNo(String mobileNo) throws FrameworkCheckedException;
	Integer updateTransactionProcessingStatus(List<Long> transactionId, Long transactionProcessingStatus, String processingStatusName);
	Integer updateTransactionDetailMasterForLeg2(TransactionDetailMasterModel model);
	public void updateTransactionDetailMaster(TransactionDetailMasterModel tdm) throws FrameworkCheckedException;

	public int  findAgentPendingTrxByCNIC(String customerCNIC);
	int getCustomerChallanCount(String mobileNo);
	public long getPaidChallan(String consumerNo,String productId);

	TransactionDetailMasterModel loadTransactionDetailMasterModelByRRN(String rrn) throws FrameworkCheckedException;
	TransactionDetailMasterModel loadTDMbyMobileNumber(String mobileNo, String productId) throws FrameworkCheckedException;
	TransactionDetailMasterModel loadTDMbyProductId(String mobileNo, String productId) throws FrameworkCheckedException;
//	List<TransactionDetailMasterModel> loadTDMbyMobileandDateRange(String mobileNo, String startDate, String endDate) throws FrameworkCheckedException;

}
