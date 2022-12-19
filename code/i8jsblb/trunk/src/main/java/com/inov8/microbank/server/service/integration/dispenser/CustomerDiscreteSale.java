package com.inov8.microbank.server.service.integration.dispenser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.ProductUnitModel;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.failurelogmodule.FailureLogManager;
import com.inov8.microbank.server.service.inventorymodule.ProductUnitManager;
import com.inov8.microbank.server.service.inventorymodule.ShipmentManager;
import com.inov8.microbank.server.service.productmodule.ProductIntgModuleInfoManager;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.settlementmodule.SettlementManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;


/**
 * <p>Company: </p>
 *
 * @author Jawwad Farooq
 * @version 1.0
 */
public class CustomerDiscreteSale
    implements ProductDispenser
{

  private ProductIntgModuleInfoManager productIntgModuleInfoManager;
  private CommissionManager commissionManager;
  private SmartMoneyAccountManager smartMoneyAccountManager ;
  private CustTransManager customerManager;
  private SettlementManager settlementManager;
  private ProductManager productManager;
  private ShipmentManager shipmentManager ;
  private ProductUnitManager productUnitManager;
  private FailureLogManager failureLogManager;
  private AppUserManager appUserManager;
  
  protected static Log  logger	= LogFactory.getLog(CustomerDiscreteSale.class);

  public CustomerDiscreteSale( CommissionManager commissionManager, SmartMoneyAccountManager smartMoneyAccountManager,
          SettlementManager settlementManager, ProductManager productManager, AppUserManager appUserManager,
          ProductUnitManager productUnitManager, ShipmentManager shipmentManager, GenericDao genericDAO,ApplicationContext ctx )
  {
    this.commissionManager = commissionManager ;
    this.smartMoneyAccountManager = smartMoneyAccountManager ;
    this.settlementManager = settlementManager ;
    this.productManager = productManager ;
    this.appUserManager = appUserManager ;
    this.productUnitManager = productUnitManager ;
    this.shipmentManager = shipmentManager ;
  }


  /**
   * rollback
   *
   * @param workFlowWrapper WorkFlowWrapper
   * @return WorkFlowWrapper
   * @throws FrameworkCheckedException
   */
  public WorkFlowWrapper rollback(WorkFlowWrapper workFlowWrapper) throws
      FrameworkCheckedException
  {
    return workFlowWrapper;
  }

  /**
   * updateSupplier
   *
   * @param workFlowWrapper WorkFlowWrapper
   * @return WorkFlowWrapper
   * @throws FrameworkCheckedException
   */
  public WorkFlowWrapper doSale(WorkFlowWrapper workFlowWrapper) throws
      Exception
  {
	  if(logger.isDebugEnabled())
		  logger.debug("Inside doSale of CustomerDiscreteSale");
    ProductUnitModel productUnitModel = new ProductUnitModel();

    productUnitModel.setProductIdProductModel(workFlowWrapper.getProductModel());
    workFlowWrapper.setBasePersistableModel(productUnitModel);
    workFlowWrapper = productManager.sellDiscreteProduct(workFlowWrapper);
    if(null == workFlowWrapper.getBasePersistableModel())
    {
    	throw new WorkFlowException(WorkFlowErrorCodeConstants.PRODUCT_STOCK_OUT);
    }
    productUnitModel = (ProductUnitModel) workFlowWrapper.getBasePersistableModel();
    if(logger.isDebugEnabled())
		  logger.debug("Ending doSale of CustomerDiscreteSale");
    return workFlowWrapper ;
  }

  /**
   * verify
   *
   * @param workFlowWrapper WorkFlowWrapper
   * @return WorkFlowWrapper
   * @throws FrameworkCheckedException
   */
  public WorkFlowWrapper verify(WorkFlowWrapper workFlowWrapper) throws
      FrameworkCheckedException
  {
    return null;
  }
}
