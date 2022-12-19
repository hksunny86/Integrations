package com.inov8.microbank.server.service.integration.dispenser;

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

import org.springframework.context.ApplicationContext;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.ShipmentModel;
import com.inov8.microbank.common.util.ServiceTypeConstantsInterface;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.failurelogmodule.FailureLogManager;
import com.inov8.microbank.server.service.integration.vo.VariableProductVO;
import com.inov8.microbank.server.service.inventorymodule.ProductUnitManager;
import com.inov8.microbank.server.service.inventorymodule.ShipmentManager;
import com.inov8.microbank.server.service.productmodule.ProductIntgModuleInfoManager;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.settlementmodule.SettlementManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;

public class CustomerVariableSale
    implements ProductDispenser
{

  private ProductIntgModuleInfoManager productIntgModuleInfoManager;
  private CommissionManager commissionManager;
  private SmartMoneyAccountManager smartMoneyAccountManager ;
  private CustTransManager customerManager;
  private SettlementManager settlementManager;
  private ProductManager productManager;

  private ProductUnitManager productUnitManager;
  private FailureLogManager failureLogManager;
  private AppUserManager appUserManager;
  private ShipmentManager shipmentManager;

  public CustomerVariableSale( CommissionManager commissionManager, SmartMoneyAccountManager smartMoneyAccountManager,
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
   * doSale
   *
   * @param workFlowWrapper WorkFlowWrapper
   * @return WorkFlowWrapper
   * @throws FrameworkCheckedException
   */
  public WorkFlowWrapper doSale(WorkFlowWrapper workFlowWrapper) throws
      FrameworkCheckedException
  {

	  if(workFlowWrapper.getServiceTypeModel().getServiceTypeId().longValue() == ServiceTypeConstantsInterface.SERVICE_TYPE_DISCRETE.longValue() )
	  {
		  throw new WorkFlowException(WorkFlowErrorCodeConstants.GENERAL_ERROR);
	  }
    ShipmentModel shipmentModel = new ShipmentModel();

    workFlowWrapper = this.productManager.sellVariableProduct( workFlowWrapper ) ;
    shipmentModel = (ShipmentModel)workFlowWrapper.getBasePersistableModel();
    if( shipmentModel == null )
    {
      throw new WorkFlowException(WorkFlowErrorCodeConstants.PRODUCT_STOCK_OUT);
    }

    // @todo CALL THE EXTERNAL SYSTEM TO GET THE VARAIBLE PRODUCT
    VariableProductVO variableProductVO = new VariableProductVO();
    variableProductVO.setResponseCode("00");
    variableProductVO.setPin("8585856896");
    workFlowWrapper.setProductVO( variableProductVO );
    // -----------------------------------------------------------------------------------------

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
    return workFlowWrapper;
  }

  public void setShipmentManager(ShipmentManager shipmentManager)
  {
    this.shipmentManager = shipmentManager;
  }
}
