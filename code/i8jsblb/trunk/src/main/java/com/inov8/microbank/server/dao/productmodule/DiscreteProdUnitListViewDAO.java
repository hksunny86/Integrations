package com.inov8.microbank.server.dao.productmodule;

import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.productmodule.DiscreteProdUnitListViewModel;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: inov8 Limited</p>
 *
 * @author Jawwad Farooq
 * @version 1.0
 */
public interface DiscreteProdUnitListViewDAO
    extends BaseDAO<DiscreteProdUnitListViewModel, Long>
{
  public SearchBaseWrapper getUnsoldProductUnits(SearchBaseWrapper searchBaseWrapper);
}
