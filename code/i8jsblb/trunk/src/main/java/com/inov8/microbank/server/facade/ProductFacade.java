package com.inov8.microbank.server.facade;

import com.inov8.microbank.server.service.productmodule.ProductCatalogManager;
import com.inov8.microbank.server.service.productmodule.ProductIntgModuleInfoManager;
import com.inov8.microbank.server.service.productmodule.ProductIntgVOManager;
import com.inov8.microbank.server.service.productmodule.ProductManager;

public interface ProductFacade
    extends ProductManager, ProductCatalogManager, ProductIntgModuleInfoManager, ProductIntgVOManager
{
}
