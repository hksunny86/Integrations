package com.inov8.microbank.server.dao.agentgroup;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.veriflymodule.TaggedAgentTransactionModel;

public interface TaggedAgentTransactionDetailDAO extends BaseDAO<TaggedAgentTransactionModel, Long>{

	CustomList<TaggedAgentTransactionModel> getTaggedAgentTransactions(
			TaggedAgentTransactionModel model,
			PagingHelperModel pagingHelperModel,
			DateRangeHolderModel dateRangeHolderModel);
}
