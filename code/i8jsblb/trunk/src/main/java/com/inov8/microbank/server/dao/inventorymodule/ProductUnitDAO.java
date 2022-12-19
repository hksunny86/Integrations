package com.inov8.microbank.server.dao.inventorymodule;

import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.ProductUnitModel;

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

public interface ProductUnitDAO
    extends BaseDAO<ProductUnitModel, Long>
{
  SearchBaseWrapper getUnsoldProductUnits(SearchBaseWrapper searchBaseWrapper);
  SearchBaseWrapper sellDiscreteProduct(SearchBaseWrapper searchBaseWrapper);

}
