package com.inov8.microbank.server.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.ProductDeviceFlowModel;
import com.inov8.microbank.server.service.productdeviceflowmodule.ProductDeviceFlowManager;

public class ProductDeviceFlowFacadeImpl implements ProductDeviceFlowFacade{
    private ProductDeviceFlowManager productDeviceFlowManager;
    private FrameworkExceptionTranslator frameworkExceptionTranslator;
    
    
    public SearchBaseWrapper searchProductDeviceFlowListView(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
    	try
        {
          this.productDeviceFlowManager.searchProductDeviceFlowListView(searchBaseWrapper);
        }
        catch (Exception ex)
        {
          throw this.frameworkExceptionTranslator.translate(ex,
              this.frameworkExceptionTranslator.FIND_ACTION);
        }
        return searchBaseWrapper;
	}

	public BaseWrapper loadProductDeviceFlowByPrimaryKey(BaseWrapper baseWrapper) throws FrameworkCheckedException {
    	 try
         {
           this.productDeviceFlowManager.loadProductDeviceFlowByPrimaryKey(baseWrapper);
         }
         catch (Exception ex)
         {
           throw this.frameworkExceptionTranslator.translate(ex,
               this.frameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
         }
         return baseWrapper;
	}

	public BaseWrapper createProductDeviceFlow(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        try
        {
          this.productDeviceFlowManager.createProductDeviceFlow(baseWrapper);
        }
        catch (Exception ex)
        {
          throw this.frameworkExceptionTranslator.translate(ex,
              this.frameworkExceptionTranslator.INSERT_ACTION);
        }
        return baseWrapper;
	}
 
	public BaseWrapper updateProductDeviceFlow(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        try
        {
          this.productDeviceFlowManager.updateProductDeviceFlow(baseWrapper);
        }
        catch (Exception ex)
        {
          throw this.frameworkExceptionTranslator.translate(ex,
              this.frameworkExceptionTranslator.UPDATE_ACTION);
        }
        return baseWrapper;
	}
	
	public int  isproductDeviceFlow(ProductDeviceFlowModel productDeviceFlowModel) throws FrameworkCheckedException
	{
		
		try
        {
          return this.productDeviceFlowManager.isproductDeviceFlow(productDeviceFlowModel);
        }
        catch (Exception ex)
        {
          throw this.frameworkExceptionTranslator.translate(ex,
              FrameworkExceptionTranslator.FIND_ACTION);
        }
        
		
		
		
	}

	public void setProductDeviceFlowManager(
			ProductDeviceFlowManager productDeviceFlowManager) {
		this.productDeviceFlowManager = productDeviceFlowManager;
	}

	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}
    
}
