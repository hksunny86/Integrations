package com.inov8.microbank.webapp.action.productmodule;
    
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.ExceptionErrorCodes;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.NotificationMessageModel;
import com.inov8.microbank.common.model.ProductIntgModuleInfoModel;
import com.inov8.microbank.common.model.ProductIntgVoModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.ServiceModel;
import com.inov8.microbank.common.model.SupplierModel;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.productmodule.ProductCatalogManager;
import com.inov8.microbank.server.service.productmodule.ProductManager;

/**
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backend Application for POS terminals.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Asad Hayat
 * @version 1.0
 */

public class ProductFormController extends AdvanceFormController
{
  private ProductManager productManager;
  private ReferenceDataManager referenceDataManager;
  private ProductCatalogManager catalogManager;
  private Long id;

  public ProductFormController()
  {
    setCommandName("productModel");
    setCommandClass(ProductModel.class);
  }

  @Override
  protected Map loadReferenceData(HttpServletRequest httpServletRequest)
  {
    /**
     * code fragment to load reference data for SupplierModel
     */
    SupplierModel supplierModel = new SupplierModel();
    supplierModel.setActive(true);
    ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
        supplierModel, "name", SortingOrder.ASC);

    try
    {
      referenceDataManager.getReferenceData(referenceDataWrapper);
    }
    catch (FrameworkCheckedException ex1)
    {
    }
    List<SupplierModel> supplierModelList = null;
    if (referenceDataWrapper.getReferenceDataList() != null)
    {
      supplierModelList = referenceDataWrapper.getReferenceDataList();
    }

    Map referenceDataMap = new HashMap();
    referenceDataMap.put("SupplierModelList", supplierModelList);

    /**
     * code fragment to load reference data for ServiceModel
     */

    ServiceModel serviceModel = new ServiceModel();
    serviceModel.setActive(true);
    referenceDataWrapper = new ReferenceDataWrapperImpl(
        serviceModel, "name", SortingOrder.ASC);
    referenceDataWrapper.setBasePersistableModel(serviceModel);

    try
    {
      referenceDataManager.getReferenceData(referenceDataWrapper);
    }
    catch (FrameworkCheckedException ex)
    {
    }
    List<ServiceModel> ServiceModelList = null;
    if (referenceDataWrapper.getReferenceDataList() != null)
    {
      ServiceModelList = referenceDataWrapper.getReferenceDataList();
    }

    referenceDataMap.put("ServiceModelList", ServiceModelList);

    /**
    * code fragment to load reference data for Notification Message (Failure Message) 1
    */
    NotificationMessageModel notificationMessageModel = new NotificationMessageModel();
    notificationMessageModel.setMessageTypeId(1L);
    referenceDataWrapper = new ReferenceDataWrapperImpl(
            notificationMessageModel, "smsMessageText", SortingOrder.ASC);
    referenceDataWrapper.setBasePersistableModel(notificationMessageModel);

    try
    {
      referenceDataManager.getReferenceData(referenceDataWrapper);
    }
    catch (FrameworkCheckedException ex)
    {}
    List<NotificationMessageModel> NotificationMessageModelList = null;
    if (referenceDataWrapper.getReferenceDataList() != null)
    {
      NotificationMessageModelList = referenceDataWrapper.getReferenceDataList();
    }

    referenceDataMap.put("failureNotificationMessageModelList", NotificationMessageModelList);
    
    
    /**
     * code fragment to load reference data for Notification Message (Success Message) 2
     */
     NotificationMessageModel successNotificationMessageModel = new NotificationMessageModel();
     successNotificationMessageModel.setMessageTypeId(2L);
     referenceDataWrapper = new ReferenceDataWrapperImpl(
    		 successNotificationMessageModel, "smsMessageText", SortingOrder.ASC);
     referenceDataWrapper.setBasePersistableModel(successNotificationMessageModel);

     try
     {
       referenceDataManager.getReferenceData(referenceDataWrapper);
     }
     catch (FrameworkCheckedException ex)
     {}
     List<NotificationMessageModel> successNotificationMessageModelList = null;
     if (referenceDataWrapper.getReferenceDataList() != null)
     {
    	 successNotificationMessageModelList = referenceDataWrapper.getReferenceDataList();
     }

     referenceDataMap.put("successNotificationMessageModelList", successNotificationMessageModelList);
     
     NotificationMessageModel helpLineNotificationMessageModel = new NotificationMessageModel();
     helpLineNotificationMessageModel.setMessageTypeId(4L);
     referenceDataWrapper = new ReferenceDataWrapperImpl(
    		 helpLineNotificationMessageModel, "smsMessageText", SortingOrder.ASC);
     referenceDataWrapper.setBasePersistableModel(helpLineNotificationMessageModel);

     try
     {
       referenceDataManager.getReferenceData(referenceDataWrapper);
     }
     catch (FrameworkCheckedException ex)
     {}
     List<NotificationMessageModel> helpLineNotificationMessageModelList = null;
     if (referenceDataWrapper.getReferenceDataList() != null)
     {
    	 helpLineNotificationMessageModelList = referenceDataWrapper.getReferenceDataList();
     }

     referenceDataMap.put("helpLineNotificationMessageModelList", helpLineNotificationMessageModelList);
     
     /**
      * code fragment to load reference data for Notification Message (Instructions Messages) 3
      */
      NotificationMessageModel instructionNotificationMessageModel = new NotificationMessageModel();
      instructionNotificationMessageModel.setMessageTypeId(3L);
      referenceDataWrapper = new ReferenceDataWrapperImpl(
     		 instructionNotificationMessageModel, "smsMessageText", SortingOrder.ASC);
      referenceDataWrapper.setBasePersistableModel(instructionNotificationMessageModel);

      try
      {
        referenceDataManager.getReferenceData(referenceDataWrapper);
      }
      catch (FrameworkCheckedException ex)
      {}
      List<NotificationMessageModel> instructionNotificationMessageModelList = null;
      if (referenceDataWrapper.getReferenceDataList() != null)
      {
     	 instructionNotificationMessageModelList = referenceDataWrapper.getReferenceDataList();
      }

      referenceDataMap.put("instructionNotificationMessageModelList", instructionNotificationMessageModelList);

    
   ProductIntgModuleInfoModel productIntgModuleInfoModel = new ProductIntgModuleInfoModel();
   productIntgModuleInfoModel.setActive(true);
   referenceDataWrapper = new ReferenceDataWrapperImpl(
       productIntgModuleInfoModel, "name", SortingOrder.ASC);
   referenceDataWrapper.setBasePersistableModel(productIntgModuleInfoModel);

   try
   {
     referenceDataManager.getReferenceData(referenceDataWrapper);
   }
   catch (FrameworkCheckedException ex)
   {
   }
   List<ProductIntgModuleInfoModel> ProductIntgModuleInfoModelList = null;
   if (referenceDataWrapper.getReferenceDataList() != null)
   {
     ProductIntgModuleInfoModelList = referenceDataWrapper.getReferenceDataList();
   }

   referenceDataMap.put("ProductIntgModuleInfoModelList", ProductIntgModuleInfoModelList);

   /**
    * code fragment to load reference data for NotificationMessageModel class name
    */
   
   
   /**
   * code fragment to load reference data for ProductIntgModuleInfoModel class name
   */

  ProductIntgVoModel productIntgVoModel = new ProductIntgVoModel();
  productIntgVoModel.setActive(true);
  referenceDataWrapper = new ReferenceDataWrapperImpl(
		  productIntgVoModel, "name", SortingOrder.ASC);
  referenceDataWrapper.setBasePersistableModel(productIntgVoModel);

  try
  {
    referenceDataManager.getReferenceData(referenceDataWrapper);
  }
  catch (FrameworkCheckedException ex)
  {
  }
  List<ProductIntgVoModel> ProductIntgVoModelList = null;
  if (referenceDataWrapper.getReferenceDataList() != null)
  {
	  ProductIntgVoModelList = referenceDataWrapper.getReferenceDataList();
  }

  referenceDataMap.put("ProductIntgVoModelList", ProductIntgVoModelList);
  
  
  return referenceDataMap;

  }

  @Override
  protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws
      Exception
  {
    id = ServletRequestUtils.getLongParameter(httpServletRequest, "productId");
    if (null != id)
    {
      if (log.isDebugEnabled())
      {
        log.debug("id is not null....retrieving object from DB");
      }

      SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
      ProductModel productModel = new ProductModel();
      productModel.setPrimaryKey(id);
      searchBaseWrapper.setBasePersistableModel(productModel);
      searchBaseWrapper = this.productManager.loadProduct(searchBaseWrapper);
      productModel=(ProductModel) searchBaseWrapper.
      getBasePersistableModel();
      if (productModel.getCostPrice()!=null)
      {
    	  Double balance =productModel.getCostPrice();
		DecimalFormat myFormatter = new DecimalFormat("###.##");
		String bal = myFormatter.format(balance);
		httpServletRequest.setAttribute("costPrice", bal);
    }
      
      if (productModel.getUnitPrice()!=null)
      {
		Double balanceUnitPrice =productModel.getUnitPrice();
		DecimalFormat myUnitFormatter = new DecimalFormat("###.##");
		String balUnit = myUnitFormatter.format(balanceUnitPrice);
		httpServletRequest.setAttribute("unitPrice", balUnit);
      }
      
      { // Setting format of fixedDiscount and percentDiscount
    	DecimalFormat myUnitFormatter = new DecimalFormat("###.##");
    	Double fixedDiscount =productModel.getFixedDiscount();		
		String balUnit = myUnitFormatter.format(fixedDiscount);
		httpServletRequest.setAttribute("fixedDiscount", balUnit);
		
		Double percentDiscount =productModel.getPercentDiscount();		
		balUnit = myUnitFormatter.format(percentDiscount);
		httpServletRequest.setAttribute("percentDiscount", balUnit);
      }     
      
      return productModel;
    }
    else
    {
      if (log.isDebugEnabled())
      {
        log.debug("id is null....creating new instance of Model");
      }

      return new ProductModel();
    }
  }

  @Override
  protected ModelAndView onCreate(HttpServletRequest httpServletRequest,
                                  HttpServletResponse httpServletResponse,
                                  Object object, BindException bindException) throws
      Exception
  {
    ProductModel productModel = (ProductModel)object;
   /* Date nowDate = new Date();
    productModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
    productModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
    productModel.setCreatedOn(nowDate);
    productModel.setUpdatedOn(nowDate);
    productModel.setActive( productModel.getActive() == null ? false : productModel.getActive() ) ;*/
    productModel.setUspProductidCheck(productModel.getUspProductidCheck() == null ? 0 : productModel.getUspProductidCheck() );
    productModel.setFixedDiscount(productModel.getFixedDiscount() == null ? 0 : productModel.getFixedDiscount());
    productModel.setPercentDiscount(productModel.getPercentDiscount() == null ? 0 : productModel.getPercentDiscount());
    return this.createOrUpdate(httpServletRequest, httpServletResponse, productModel,
                               bindException);
  }

  @Override
  protected ModelAndView onUpdate(HttpServletRequest httpServletRequest,
                                  HttpServletResponse httpServletResponse,
                                  Object object, BindException bindException) throws
      Exception
  {
	ProductModel productModel = (ProductModel)object;
	/*Date nowDate = new Date();
	productModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
	productModel.setUpdatedOn(nowDate);
	productModel.setActive( productModel.getActive() == null ? false : productModel.getActive() ) ;*/
	productModel.setUspProductidCheck(productModel.getUspProductidCheck() == null ? 0 : productModel.getUspProductidCheck() );
    productModel.setFixedDiscount(productModel.getFixedDiscount() == null ? 0 : productModel.getFixedDiscount());
    productModel.setPercentDiscount(productModel.getPercentDiscount() == null ? 0 : productModel.getPercentDiscount());	
    return this.createOrUpdate(httpServletRequest, httpServletResponse, productModel,bindException);
  }

  private ModelAndView createOrUpdate(HttpServletRequest request,
                                      HttpServletResponse response,
                                      ProductModel productModel,
                                      BindException errors) throws Exception
  {
    try
    {         	
      BaseWrapper baseWrapper = new BaseWrapperImpl();
      SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
      

      long theDate = new Date().getTime();
      
      if (null != id)
      {
    	ProductModel tempProductModel;    	
        searchBaseWrapper.setBasePersistableModel(productModel);
        searchBaseWrapper = this.productManager.loadProduct(searchBaseWrapper);        
        tempProductModel=(ProductModel) searchBaseWrapper.getBasePersistableModel();
        productModel.setCreatedOn( tempProductModel.getCreatedOn() );
        productModel.setCreatedBy( tempProductModel.getCreatedBy() );
        productModel.setUpdatedOn(new Date());
        productModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());       
        
      }
      else
      {
        productModel.setCreatedOn(new Date(theDate));
        productModel.setUpdatedOn(new Date(theDate));
        productModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
        productModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
      }
      
      productModel.setActive( productModel.getActive() == null ? false : productModel.getActive() );
      productModel.setBatchMode( productModel.getBatchMode() == null ? false : productModel.getBatchMode() );
      
      baseWrapper.setBasePersistableModel(productModel);
      baseWrapper = this.productManager.createOrUpdateProduct(baseWrapper);
 
    	/* 
    	BaseWrapper baseWrapper = new BaseWrapperImpl();

     
      //productModel.setBatchMode(false);

      baseWrapper.setBasePersistableModel(productModel);

      baseWrapper = productManager.createOrUpdateProduct(baseWrapper);*/

      if (null != baseWrapper.getBasePersistableModel())
      {
    	  
	     if (null != id){
	    	SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
	    	wrapper.setBasePersistableModel(productModel);
	    	productManager.updateProductCatalogVersion(searchBaseWrapper);
	     }

        //Every new product needs to be included in a catalog named 'ALL'
        //if (productModel.getActive())
//        {
//          this.catalogManager.includeProductInCatalogAll(baseWrapper);
//        }

        this.saveMessage(request, "Record saved successfully");

        ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
        return modelAndView;
      }
      else
      {
        this.saveMessage(request, "Product with the same name already exists.");
        return super.showForm(request, response, errors);
      }
    }
    catch (FrameworkCheckedException ex)
    {
      if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION ==
          ex.getErrorCode())
      {

        super.saveMessage(request, "Record could not be saved.");
        return super.showForm(request, response, errors);
      }

      throw ex;
    }
    catch (Exception ex)
    {
        super.saveMessage(request,MessageUtil.getMessage("6075"));
        return super.showForm(request, response, errors);
    }

  }

  public void setProductManager(ProductManager productManager)
  {
    this.productManager = productManager;
  }

  public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
  {
    this.referenceDataManager = referenceDataManager;
  }

  public void setCatalogManager(ProductCatalogManager catalogManager)
  {
    this.catalogManager = catalogManager;
  }

  public ProductCatalogManager getCatalogManager()
  {
    return catalogManager;
  }

}
