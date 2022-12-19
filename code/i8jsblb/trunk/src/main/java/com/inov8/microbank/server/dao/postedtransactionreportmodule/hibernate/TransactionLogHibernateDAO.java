package com.inov8.microbank.server.dao.postedtransactionreportmodule.hibernate;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.integration.enums.TransactionCodeEnum;
import com.inov8.microbank.common.model.TransactionLogModel;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.server.dao.postedtransactionreportmodule.TransactionLogDAO;

public class TransactionLogHibernateDAO extends BaseHibernateDAO<TransactionLogModel, Long, TransactionLogDAO> implements TransactionLogDAO {
	
	public CustomList<TransactionLogModel> searchPostedTransactions(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException{
		TransactionLogModel model = (TransactionLogModel) searchBaseWrapper.getBasePersistableModel();
		Criterion trxTypeCriteria = null;
		if(!StringUtil.isNullOrEmpty(model.getTransactionType())){
			trxTypeCriteria = Restrictions.eq("transactionType",model.getTransactionType());
		}else{
			String[] params = {TransactionCodeEnum.BILL_PAYMENT_ADVICE.getValue(),TransactionCodeEnum.ACCOUNT_OPEN_FUND_TRANSFER.getValue(),TransactionCodeEnum.OPEN_ACCOUNT_BALANCE_INQUIRY.getValue()};
			trxTypeCriteria = Restrictions.in("transactionType",params);
		}
		trxTypeCriteria = Restrictions.and(trxTypeCriteria, Restrictions.isNull("parentTransactionId"));

	    CustomList<TransactionLogModel> customList = findByCriteria(trxTypeCriteria,
	    		new TransactionLogModel(),
	    		searchBaseWrapper.getPagingHelperModel(),
	    		searchBaseWrapper.getSortingOrderMap(),
	    		searchBaseWrapper.getDateRangeHolderModel());
	    
		return customList;
	}
}
