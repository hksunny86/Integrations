package com.inov8.microbank.server.service.productmodule;

import java.text.DateFormat;
import java.text.ParseException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.failurelogmodule.FailureLogManager;
import com.inov8.microbank.server.service.integration.dispenser.BillPaymentProductDispenser;
import com.inov8.microbank.server.service.integration.vo.LescoBillSaleStubVO;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.inventorymodule.ProductUnitManager;
import com.inov8.microbank.server.service.inventorymodule.ShipmentManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.settlementmodule.SettlementManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;

/**
 *
 * @author Jawwad Farooq
 * @version 1.0
 */

public class LescoBillSaleStub
extends BillPaymentProductDispenser
{

  private ProductIntgModuleInfoManager productIntgModuleInfoManager;
  private CommissionManager commissionManager;
  private SmartMoneyAccountManager smartMoneyAccountManager ;
  private CustTransManager customerManager;
  private SettlementManager settlementManager;
  private ProductManager productManager;
  private GenericDao genericDAO;
  private ProductUnitManager productUnitManager;
  private FailureLogManager failureLogManager;
  private AppUserManager appUserManager;
  private ShipmentManager shipmentManager ;
  protected final transient Log logger = LogFactory.getLog(LescoBillSaleStub.class);

  public LescoBillSaleStub( CommissionManager commissionManager, SmartMoneyAccountManager smartMoneyAccountManager,
          SettlementManager settlementManager, ProductManager productManager, AppUserManager appUserManager,
          ProductUnitManager productUnitManager, ShipmentManager shipmentManager, GenericDao genericDao )
  {
    this.commissionManager = commissionManager ;
    this.smartMoneyAccountManager = smartMoneyAccountManager ;
    this.settlementManager = settlementManager ;
    this.productManager = productManager ;
    this.appUserManager = appUserManager ;
    this.productUnitManager = productUnitManager ;
    this.shipmentManager = shipmentManager;
    this.genericDAO = genericDao ;
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



    return workFlowWrapper;
  }

  /**
   * getBillInfo
   *
   * @param workFlowWrapper WorkFlowWrapper
   * @return WorkFlowWrapper
   * @throws FrameworkCheckedException
   */
  public WorkFlowWrapper getBillInfo(WorkFlowWrapper workFlowWrapper) throws
      FrameworkCheckedException
  {
	  if(logger.isDebugEnabled())
	  {
		  logger.debug("Inside getBillInfo of LescoBillSaleStub");
	  }
    LescoBillSaleStubVO lescoVO = (LescoBillSaleStubVO)workFlowWrapper.getProductVO();

    try
    {
      lescoVO.setAddress("RASOOL PARK M/ROAD LHR");
//      lescoVO.setConsumerNo("1123200084032");
      lescoVO.setBillingMonth("8/1/2006");
      lescoVO.setDueDate(DateFormat.getDateInstance().parse("Aug 22, 2006"));
      lescoVO.setName("SYED SHUJAH AHMED");
      lescoVO.setBillAmount(2D);
      lescoVO.setBillAmountAfterDate(3D);


    }
    catch (ParseException ex)
    {
      throw new FrameworkCheckedException( ex.getMessage(), ex ) ;
    }
    workFlowWrapper.setProductVO( (ProductVO)lescoVO );
    if(logger.isDebugEnabled())
	  {
		  logger.debug("Ending getBillInfo of LescoBillSaleStub");
	  }
    return workFlowWrapper ;
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
    return null;
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
	  if(logger.isDebugEnabled())
	  {
		  logger.debug("Inside verify of LescoBillSaleStub");
	  }
    LescoBillSaleStubVO lescoVO = (LescoBillSaleStubVO)workFlowWrapper.getProductVO() ;

    if( !lescoVO.getAddress().equalsIgnoreCase( workFlowWrapper.getObject("address").toString() )  )
      throw new WorkFlowException( WorkFlowErrorCodeConstants.NO_CUSTOMER_AGAINST_MOBILENO ) ;

    if( lescoVO.getBillAmount().doubleValue() != Double.valueOf(workFlowWrapper.getObject("billAmount").toString()).doubleValue() )
      throw new WorkFlowException( WorkFlowErrorCodeConstants.NO_CUSTOMER_AGAINST_MOBILENO ) ;

    if( lescoVO.getBillAmountAfterDate().doubleValue() != Double.valueOf(workFlowWrapper.getObject("billAmountAfterDate").toString()).doubleValue() )
      throw new WorkFlowException( WorkFlowErrorCodeConstants.NO_CUSTOMER_AGAINST_MOBILENO ) ;

    if( !lescoVO.getBillingMonth().equalsIgnoreCase( workFlowWrapper.getObject("billingMonth").toString() )  )
      throw new WorkFlowException( WorkFlowErrorCodeConstants.NO_CUSTOMER_AGAINST_MOBILENO ) ;

    if( !lescoVO.getDueDate().toString().equalsIgnoreCase( workFlowWrapper.getObject("dueDate").toString() )  )
      throw new WorkFlowException( WorkFlowErrorCodeConstants.NO_CUSTOMER_AGAINST_MOBILENO ) ;

    if( !lescoVO.getConsumerNo().equalsIgnoreCase( workFlowWrapper.getObject("consumerNo").toString() )  )
      throw new WorkFlowException( WorkFlowErrorCodeConstants.NO_CUSTOMER_AGAINST_MOBILENO ) ;

    if( !lescoVO.getName().equalsIgnoreCase( workFlowWrapper.getObject("name").toString() )  )
      throw new WorkFlowException( WorkFlowErrorCodeConstants.NO_CUSTOMER_AGAINST_MOBILENO ) ;
    
    if(logger.isDebugEnabled())
	  {
		  logger.debug("Ending verify of LescoBillSaleStub");
	  }

    return workFlowWrapper ;
  }
}
