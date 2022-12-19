package com.inov8.microbank.debitcard.dao;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.debitcard.model.DebitCardViewModel;
import org.springframework.dao.DataAccessException;

import java.util.LinkedHashMap;

public interface DebitCardViewModelDAO extends BaseDAO<DebitCardViewModel,Long> {

    CustomList<DebitCardViewModel> findByExampleUnSorted(DebitCardViewModel exampleInstance, PagingHelperModel pagingHelperModel,
                                                         LinkedHashMap<String, SortingOrder> sortingOrderMap, DateRangeHolderModel dateRangeHolderModel,
                                                         ExampleConfigHolderModel exampleConfigHolderModel, String... excludeProperty) throws DataAccessException;
}
