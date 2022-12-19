package com.inov8.microbank.server.service.productmodule;

import java.lang.reflect.Constructor;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.model.ProductIntgModuleInfoModel;
import com.inov8.microbank.common.model.ProductIntgVoModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.failurelogmodule.FailureLogManager;
import com.inov8.microbank.server.service.integration.dispenser.ProductDispenser;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.inventorymodule.ProductUnitManager;
import com.inov8.microbank.server.service.inventorymodule.ShipmentManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.settlementmodule.SettlementManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;




/**
 *
 * <p>Company: </p>
 *
 * @author Jawwad Farooq
 * @version 1.0
 */
public class ProductDispenseControllerImpl implements ProductDispenseController, ApplicationContextAware
{

private ProductIntgModuleInfoManager productIntgModuleInfoManager;
  private ProductIntgVOManager productIntgVOManager;
  private CommissionManager commissionManager;
  private SmartMoneyAccountManager smartMoneyAccountManager ;
  private CustTransManager customerManager;
  private SettlementManager settlementManager;
  private ProductManager productManager;
  private GenericDao genericDAO;
  private ProductUnitManager productUnitManager;
  private FailureLogManager failureLogManager;
  private AppUserManager appUserManager;
  private ShipmentManager shipmentManager;
  private ApplicationContext ctx;

  


public ProductDispenser loadProductDispenser( WorkFlowWrapper workFlowWrapper ) throws FrameworkCheckedException
  {
    ProductIntgModuleInfoModel productIntgModuleInfoModel = new ProductIntgModuleInfoModel() ;
    productIntgModuleInfoModel.setProductIntgModuleInfoId( workFlowWrapper.getProductModel().getProductIntgModuleInfoId() );

    BaseWrapper baseWrapper = new BaseWrapperImpl() ;
    baseWrapper.setBasePersistableModel( productIntgModuleInfoModel );
    baseWrapper = this.productIntgModuleInfoManager.loadProductIntgModuleInfo(baseWrapper) ;
    productIntgModuleInfoModel = (ProductIntgModuleInfoModel)baseWrapper.getBasePersistableModel() ;
    
    if(workFlowWrapper.getProductModel() != null) {
    
    	workFlowWrapper.getProductModel().setProductIntgModuleInfoIdProductIntgModuleInfoModel(productIntgModuleInfoModel);
    }
    
    try
    {
      Class productIntgClass = Class.forName(productIntgModuleInfoModel.getClassName());

      Class[] argsClass = new Class[] {CommissionManager.class, SmartMoneyAccountManager.class,
          SettlementManager.class, ProductManager.class, AppUserManager.class,
          ProductUnitManager.class, ShipmentManager.class, GenericDao.class, ApplicationContext.class};

      Object[] argsVals = new Object[] {this.commissionManager, this.smartMoneyAccountManager,
          this.settlementManager, this.productManager, this.appUserManager,
          this.productUnitManager, this.shipmentManager, this.genericDAO, this.ctx};

      Constructor constructor = productIntgClass.getConstructor( argsClass ) ;
      return  (ProductDispenser)constructor.newInstance(argsVals) ;

    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      throw new FrameworkCheckedException( ex.getMessage(), ex ) ;
    }
  }

  public ProductVO loadProductVO( BaseWrapper baseWrapper ) throws FrameworkCheckedException
  {
    ProductModel productModel = new ProductModel();
    productModel.setPrimaryKey( Long.parseLong( baseWrapper.getObject( CommandFieldConstants.KEY_PROD_ID).toString() ) );

    BaseWrapper tempBaseWrapper = new BaseWrapperImpl();
    tempBaseWrapper.setBasePersistableModel(productModel);
    tempBaseWrapper = this.productManager.loadProduct(tempBaseWrapper);
    productModel = (ProductModel)tempBaseWrapper.getBasePersistableModel();
    baseWrapper.setBasePersistableModel(productModel);

    if( productModel.getActive() != null && productModel.getActive() == Boolean.FALSE )
    {
      throw new FrameworkCheckedException( "Product is deactivated. Please contact your service provider." );
    }
    
    if( productModel.getProductIntgVoId() == null )
    {
      throw new FrameworkCheckedException( "Product Integration VO is not specified." );
    }

    ProductIntgVoModel productIntgVoModel = new ProductIntgVoModel() ;
    productIntgVoModel.setProductIntgVoId( productModel.getProductIntgVoId() );

    tempBaseWrapper.setBasePersistableModel( productIntgVoModel );
    tempBaseWrapper = this.productIntgVOManager.loadProductIntgVO(tempBaseWrapper) ;
    productIntgVoModel = (ProductIntgVoModel)tempBaseWrapper.getBasePersistableModel() ;

    try
    {
      Class productIntgClass = Class.forName(productIntgVoModel.getClassName());
      ProductVO productVO = (ProductVO)productIntgClass.newInstance() ;

      productVO = productVO.populateVO( productVO, baseWrapper ) ;
      productVO.validateVO( productVO ) ;

      return  productVO;
    }
    catch (Exception ex)
    {
    	ex.printStackTrace();
      throw new FrameworkCheckedException( ex.getMessage(), ex ) ;
    }
  }
  
  
  public ProductDispenser loadProductDispenserByProductId(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException
	{
		ProductModel productModel = new ProductModel();
		productModel.setProductId(workFlowWrapper.getProductModel().getProductId());

		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(productModel);
		
		baseWrapper = this.productManager.loadProduct(baseWrapper) ;
		workFlowWrapper.setProductModel( (ProductModel)baseWrapper.getBasePersistableModel() ) ;

		
		return this.loadProductDispenser(workFlowWrapper);
	}
  
  
  public ProductVO loadProductVOWithoutLoadingProd(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		ProductModel productModel = new ProductModel();

		BaseWrapper tempBaseWrapper = new BaseWrapperImpl();
		productModel = ((WorkFlowWrapper)baseWrapper).getProductModel();

		if (productModel.getProductIntgVoId() == null)
		{
			throw new FrameworkCheckedException("Product Integration VO is not specified.");
		}

		ProductIntgVoModel productIntgVoModel = new ProductIntgVoModel();
		productIntgVoModel.setProductIntgVoId(productModel.getProductIntgVoId());

		tempBaseWrapper.setBasePersistableModel(productIntgVoModel);
		tempBaseWrapper = this.productIntgVOManager.loadProductIntgVO(tempBaseWrapper);
		productIntgVoModel = (ProductIntgVoModel) tempBaseWrapper.getBasePersistableModel();

		try
		{
			Class productIntgClass = Class.forName(productIntgVoModel.getClassName());
			ProductVO productVO = (ProductVO) productIntgClass.newInstance();

			productVO = productVO.populateVO(productVO, baseWrapper);
			productVO.validateVO(productVO);

			return productVO;
		}
		catch (Exception ex)
		{
			throw new FrameworkCheckedException(ex.getMessage(), ex);
		}
	}

//  BillPaymentVO populateVO( BillPaymentVO billPaymentVO, BaseWrapper baseWrapper )
//  {
//    billPaymentVO.setConsumerNo( baseWrapper.getObject( CommandFieldConstants.KEY_CUST_CODE ).toString().trim() );
//    return billPaymentVO;
//  }
//
//  void validateVO( BillPaymentVO billPaymentVO )throws FrameworkCheckedException
//  {
//    if( billPaymentVO.getConsumerNo() != null )
//    {
//      if( billPaymentVO.getConsumerNo().equals("") )
//        throw new FrameworkCheckedException("Consumer Reference number is not provided.");
//    }
//    else
//      throw new FrameworkCheckedException("Consumer Reference number is not provided.");
//  }


  public void setProductIntgModuleInfoManager(ProductIntgModuleInfoManager
                                              productIntgModuleInfoManager)
  {
    this.productIntgModuleInfoManager = productIntgModuleInfoManager;
  }

  public void setAppUserManager(AppUserManager appUserManager)
  {
    this.appUserManager = appUserManager;
  }

  public void setCommissionManager(CommissionManager commissionManager)
  {
    this.commissionManager = commissionManager;
  }

  public void setCustomerManager(CustTransManager customerManager)
  {
    this.customerManager = customerManager;
  }
  

  public void setApplicationContext(ApplicationContext ctx) throws BeansException
  {
	this.ctx = ctx;
  }

  public void setFailureLogManager(FailureLogManager failureLogManager)
  {
    this.failureLogManager = failureLogManager;
  }

  public void setProductManager(ProductManager productManager)
  {
    this.productManager = productManager;
  }

  public void setProductUnitManager(ProductUnitManager productUnitManager)
  {
    this.productUnitManager = productUnitManager;
  }

  public void setSettlementManager(SettlementManager settlementManager)
  {
    this.settlementManager = settlementManager;
  }

  public void setSmartMoneyAccountManager(SmartMoneyAccountManager
                                          smartMoneyAccountManager)
  {
    this.smartMoneyAccountManager = smartMoneyAccountManager;
  }

  public void setShipmentManager(ShipmentManager shipmentManager)
  {
    this.shipmentManager = shipmentManager;
  }

public void setProductIntgVOManager(ProductIntgVOManager productIntgVOManager)
{
	this.productIntgVOManager = productIntgVOManager;
}


public void setGenericDAO(GenericDao genericDAO)
{
	this.genericDAO = genericDAO;
}
}
