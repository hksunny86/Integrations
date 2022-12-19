package com.inov8.microbank.webapp.action.productmodule;

/**
 * <p>Title: Microbank</p>
 *
 * <p>Description: Catalog managemetn.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Abdul Qadeer
 * @version 1.0
 */

public class CatalogController
{
  //   extends AdvanceFormController implements Cell
//
//{
//  private ProductManager productManager;
//  private ProductCatalogueManager productCatalogueManager;
//  public CatalogController()
//  {
//
//    setCommandName("productCatalogueModel");
//    setCommandClass(ProductCatalogueModel.class);
//
//    if(log.isDebugEnabled()){
//      log.debug("Catalog controller is being constructed.");
//    }
//
//  }
//
//  protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws
//      Exception
//  {
//
//
//    if(log.isDebugEnabled()){
//      log.debug("CatalogController.loadReferenceData called...");
//    }
//
//
//    //Fetch list of all products
//    ProductListViewModel prodModel = new ProductListViewModel();
//    //Only active products need to be shown.  So I am setting active
//    //method of ProductListViewModel so that Hibernate could ultimately
//    //translate it to a "where is_active = 1" clause
//    prodModel.setActive(true);
//    SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
//    wrapper.setBasePersistableModel(prodModel);
//    LinkedHashMap<String, SortingOrder>
//        sortingOrderMap = new LinkedHashMap<String, SortingOrder> ();
//    sortingOrderMap.put("supplierName", SortingOrder.ASC);
//    sortingOrderMap.put("productName", SortingOrder.ASC);
//    wrapper.setSortingOrderMap(sortingOrderMap);
//
////    wrapper.setSortingOrderMap();
//    SearchBaseWrapper result = productManager.searchProduct(wrapper);
//    if(log.isDebugEnabled()){
//      log.debug("Prodcut list lengtth is : " +
//                       result.getCustomList().getResultsetList().size());
//    }
//
//    Map refData = new HashMap();
//    refData.put("prodList", result.getCustomList().getResultsetList());
//    return refData;
//
//  }
//
//  protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws
//      Exception
//  {
//
//    if(log.isDebugEnabled()){
//      log.debug("CatalogController.loadFormBackingObject called...");
//    }
//
//    ProductCatalogueModel productCatalogModel = new ProductCatalogueModel();
//
//    Long id = ServletRequestUtils.getLongParameter(httpServletRequest,
//        "productCatalogueId");
//    if (id != null)
//    {
//      // do update case handling
//    }
//    else
//    {
//      if (log.isDebugEnabled())
//      {
//        log.debug("id is null....creating new instance of productCatlogModel");
//      }
//      long theDate = new Date().getTime();
//      productCatalogModel = new ProductCatalogueModel();
//      productCatalogModel.setCreatedOn(new Date(theDate));
//      productCatalogModel.setUpdatedOn(new Date(theDate));
//
//    }
//    return productCatalogModel;
//
//  }
//
//  protected ModelAndView onCreate(HttpServletRequest httpServletRequest,
//                                  HttpServletResponse httpServletResponse,
//                                  Object object, BindException bindException) throws
//      Exception
//  {
//
//    if(log.isDebugEnabled()){
//      log.debug("CatalogController.onCreate called...");
//    }
//
//    //////////////////Step 1://////////////////////////////////////////////
//    // Prepare an array list of product IDs which were selected by
//    //the user on the product catalog form.
//    Enumeration parameterNames = httpServletRequest.getParameterNames();
//    ArrayList<Integer> productIdList = new ArrayList<Integer> ();
//    while (parameterNames.hasMoreElements())
//    {
//      String parameterName = (String) parameterNames.nextElement();
//      if (parameterName.startsWith("checkBox"))
//      {
//        String parameterValue = httpServletRequest.getParameter(parameterName);
//        if (parameterValue.equalsIgnoreCase("SELECTED"))
//        {
//          int prodId = Integer.parseInt(parameterName.replaceFirst("checkBox",
//              ""));
//          if (log.isDebugEnabled())
//          {
//            log.debug("prodId is: " + prodId);
//          }
//          productIdList.add(prodId);
//        }
//      }
//    } //end of while
//    //Step 1 ends here
//
//    //////////////////Step 2://////////////////////////////////////////////
//    //Put all necessary stuff in a wrapper object and pass this object
//    //to the service layer (product catalog manager in this case)
//    ProductCatalogueModel productCatalogModel = (ProductCatalogueModel) object;
//
//    productCatalogModel.setActive(true);
//    productCatalogModel.setCreatedBy(1L);
//    productCatalogModel.setUpdatedBy(1L);
//    productCatalogModel.setCreatedOn(new Date(System.currentTimeMillis()));
//    productCatalogModel.setUpdatedOn(new Date(System.currentTimeMillis()));
//
//    for (int i = 0; i < productIdList.size(); i++)
//    {
//      ProductCatalogueDetailModel catalogDetail = new
//          ProductCatalogueDetailModel();
//      catalogDetail.setProductId(productIdList.get(i).longValue());
//      productCatalogModel.addProductCatalogueIdProductCatalogueDetailModel(
//          catalogDetail);
//    }
//
//    BaseWrapper catalogProductInfo = new BaseWrapperImpl();
//    catalogProductInfo.setBasePersistableModel(productCatalogModel);
//    catalogProductInfo = this.catalogManager.createOrUpdateCatalogue(
//        catalogProductInfo);
//
//    //Sthis.saveMessage(httpServletRequest, "Catalog created successfully");
//    ModelAndView mav = new ModelAndView(this.getSuccessView());
//    //productCatalogModel = new ProductCatalogueModel();
//    //mav =  super.showForm(httpServletRequest, httpServletResponse, bindException);
//    //mav.addObject(productCatalogModel);
//    return mav;
//
//
//
//
//
///*
//
//    ModelAndView mav = new ModelAndView("productcatalogview");
//    productCatalogModel = new ProductCatalogueModel();
//    mav.addObject(productCatalogModel);
//    return mav;
//*/
//  }
//
//  protected ModelAndView onUpdate(HttpServletRequest httpServletRequest,
//                                  HttpServletResponse httpServletResponse,
//                                  Object object, BindException bindException) throws
//      Exception
//  {
//    //return null;
//
//    if(log.isDebugEnabled()){
//      log.debug("CatalogController.onUpdate called...");
//    }
//
//    return new ModelAndView("productcatalogview");
//  }
//
//  public ProductManager getProductManager()
//  {
//    return productManager;
//  }
//
//  public ProductCatalogueManager getcatalogManager()
//  {
//    return catalogManager;
//  }
//
//  public void setProductManager(ProductManager productManager)
//  {
//    this.productManager = productManager;
//  }
//
//  public void setProductCatalogueManager(ProductCatalogueManager
//                                         productCatalogueManager)
//  {
//    this.productCatalogueManager = productCatalogueManager;
//  }
//
//  public void setcatalogManager(ProductCatalogueManager catalogManager)
//  {
//    this.catalogManager = catalogManager;
//  }
//
//  public String getExportDisplay(TableModel tableModel, Column column)
//  {
//    return null;
//  }
//
//  public String getHtmlDisplay(TableModel tableModel, Column column)
//  {
//    //The purpose of this method is to generate dynamic html for combo box
//    //column of the table on productcatalogview page.
//
//    if(log.isDebugEnabled()){
//      log.debug("CatalogController.getHtmlDisplay called...");
//    }
//
//    ProductListViewModel prodListModel = (ProductListViewModel) tableModel.
//        getCurrentRowBean();
//
//    HtmlBuilder html = new HtmlBuilder();
//    ColumnBuilder columnBuilder = new ColumnBuilder(html, column);
//    columnBuilder.tdStart();
//    html.input("hidden").name("checkBox" +
//        prodListModel.getProductId()).value("SELECTED").xclose();
//    html.input("checkbox").name(prodListModel.getProductId().toString());
//    html.onclick("setProductSelectionState(this)");
//    html.checked();
//    html.xclose();
//    columnBuilder.tdEnd();
//    return html.toString();
//
//  }
}
