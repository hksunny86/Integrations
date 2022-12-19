package com.inov8.microbank.server.dao.productmodule.hibernate;

import java.util.List;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.productmodule.DiscreteProdUnitListViewModel;
import com.inov8.microbank.server.dao.productmodule.DiscreteProdUnitListViewDAO;

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
public class DiscreteProdUnitListViewHibernateDAO
    extends BaseHibernateDAO<DiscreteProdUnitListViewModel, Long, DiscreteProdUnitListViewDAO>
    implements DiscreteProdUnitListViewDAO
{

  public SearchBaseWrapper getUnsoldProductUnits(SearchBaseWrapper searchBaseWrapper)
  {
    CustomList<DiscreteProdUnitListViewModel>
        customList = super.findByExample( (DiscreteProdUnitListViewModel) searchBaseWrapper.
                                         getBasePersistableModel());
    if (null != customList && null != customList.getResultsetList() && customList.getResultsetList().size() > 0)
    {
      List<DiscreteProdUnitListViewModel> list = customList.getResultsetList();
      searchBaseWrapper.setBasePersistableModel(list.get(0));

    }
    else
    {
      /*If no unsold product unit found then set null*/
      searchBaseWrapper.setBasePersistableModel(null);
    }

    return searchBaseWrapper;

}



}
