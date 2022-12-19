package com.inov8.microbank.server.service.inventorymodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.model.ProductUnitModel;
import com.inov8.microbank.server.service.activatedeactivate.ActivateDeactivateManagerImpl;

public class ActivateDeactivateProductUnitManagerImpl extends ActivateDeactivateManagerImpl
{

	private ProductUnitManager productUnitManager;
	@Override
	public BaseWrapper activateDeactivate(BaseWrapper baseWrapper) throws FrameworkCheckedException {

	    	baseWrapper=super.activateDeactivate(baseWrapper);

	      BaseWrapper baseWrapperProductUnit = new BaseWrapperImpl();
	      ProductUnitModel productUnitModel = (ProductUnitModel)baseWrapper.getBasePersistableModel();
	      baseWrapperProductUnit.setBasePersistableModel(productUnitModel);
	      baseWrapperProductUnit = this.productUnitManager.loadProductUnit(baseWrapperProductUnit);
	      productUnitModel = (ProductUnitModel) baseWrapperProductUnit.getBasePersistableModel();
	     if (productUnitModel.getActive()==true)
			{
	    	  this.productUnitManager.updateProductShipment(productUnitModel,0);
			}
		 else if (productUnitModel.getActive()==false)
			{
				this.productUnitManager.updateProductShipment(productUnitModel,1);
				
			}
	      
		return baseWrapper;
	}
	public void setProductUnitManager(ProductUnitManager productUnitManager) {
		this.productUnitManager = productUnitManager;
	}

	
}
