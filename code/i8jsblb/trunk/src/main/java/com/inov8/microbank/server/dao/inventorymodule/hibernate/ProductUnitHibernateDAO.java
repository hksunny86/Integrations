package com.inov8.microbank.server.dao.inventorymodule.hibernate;

import java.util.List;

import org.hibernate.LockMode;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.ProductUnitModel;
import com.inov8.microbank.server.dao.inventorymodule.ProductUnitDAO;

/**
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backend Application for POS terminals.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Maqsood Shahzad
 * @version 1.0
 */

public class ProductUnitHibernateDAO
    extends BaseHibernateDAO<ProductUnitModel, Long, ProductUnitDAO>
    implements ProductUnitDAO
{

  public SearchBaseWrapper getUnsoldProductUnits(SearchBaseWrapper
                                                 searchBaseWrapper)
  {
    CustomList<ProductUnitModel>
        customList = super.findByExample( (ProductUnitModel) searchBaseWrapper.
                                         getBasePersistableModel());
    if (null != customList && null != customList.getResultsetList() && customList.getResultsetList().size() > 0 )
    {
      List<ProductUnitModel> list = customList.getResultsetList();
      Long PK = list.get(0).getPrimaryKey();
      /* Lock the row pessimistically*/
      this.getHibernateTemplate().get(this.getPersistentClass(), PK,
                                      LockMode.UPGRADE_NOWAIT);
      searchBaseWrapper.setBasePersistableModel(list.get(0));
      

    }
    else
    {
      /*If no unsold product unit found then set null*/
      searchBaseWrapper.setBasePersistableModel(null);
    }

    return searchBaseWrapper;

  }
  public SearchBaseWrapper sellDiscreteProduct(SearchBaseWrapper
          searchBaseWrapper)
	{
	CustomList<ProductUnitModel>
	customList = super.findByExample( (ProductUnitModel) searchBaseWrapper.
	  getBasePersistableModel());
	if (null != customList && null != customList.getResultsetList() && customList.getResultsetList().size() > 0 )
	{
	List<ProductUnitModel> list = customList.getResultsetList();
	Long PK = list.get(0).getPrimaryKey();
	/* Lock the row pessimistically*/
	this.getHibernateTemplate().get(this.getPersistentClass(), PK,
	LockMode.UPGRADE_NOWAIT);
	ProductUnitModel productUnitModel = (ProductUnitModel)list.get(0);
	productUnitModel.setSold(true);
	super.saveOrUpdate(productUnitModel);
	searchBaseWrapper.setBasePersistableModel(productUnitModel);
	
	
	
	}
	else
	{
	/*If no unsold product unit found then set null*/
	searchBaseWrapper.setBasePersistableModel(null);
	}
	
	return searchBaseWrapper;

}


}
