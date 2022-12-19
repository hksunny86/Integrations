package com.inov8.microbank.server.dao.postedtransactionreportmodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.integration.vo.MiddlewareMessageVO;
import org.springframework.dao.DataAccessException;

import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.postedrransactionreportmodule.IntgTransactionTypeModel;
import com.inov8.microbank.common.model.postedrransactionreportmodule.PostedTransactionReportModel;

public interface PostedTransactionReportDAO extends BaseDAO<PostedTransactionReportModel, Long>{

    List<IntgTransactionTypeModel> fetchIntgTransactionTypes( java.util.List<IntgTransactionTypeModel> intgTransactionTypeModel, String propertyToSortBy, SortingOrder sortingOrder, Long... intgTransactionTypeIds ) throws DataAccessException;
    PostedTransactionReportModel getTransactionCodeIdForReversalByStanAndUserId(MiddlewareMessageVO middlewareMessageVO, Long[] productIds, String recipientMfsId) throws FrameworkCheckedException;

    Boolean validateDuplicateStan(Long[] productIds,String stan,String recipientMfsId) throws FrameworkCheckedException;
}
