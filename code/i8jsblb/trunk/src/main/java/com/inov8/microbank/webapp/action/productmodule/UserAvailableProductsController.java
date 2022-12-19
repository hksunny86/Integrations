package com.inov8.microbank.webapp.action.productmodule;

import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.model.ProductCatalogDetailModel;
import com.inov8.microbank.common.model.ProductCatalogModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.portal.authorizationreferencedata.ProductModelVO;
import com.inov8.microbank.common.model.productmodule.ProductListViewModel;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.server.service.productmodule.ProductCatalogManager;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
public class UserAvailableProductsController {

    private final static Log logger = LogFactory.getLog(UserAvailableProductsController.class);

    @Autowired
    private ProductManager productManager;

    @Autowired
    private ProductCatalogManager productCatalogManager;

    @RequestMapping(value = "/p_loadProductsByUserType", method = RequestMethod.GET)
    public @ResponseBody
    List<ProductModelVO> loadProductByUserType(@RequestParam Long appUserTypeId , @RequestParam Long productCatalogId) throws Exception{


        List<ProductListViewModel> productListViewModelList=new ArrayList<>();
        ProductListViewModel productListViewModel = new ProductListViewModel();

        if(appUserTypeId!=null) {
            if (UserTypeConstantsInterface.RETAILER == appUserTypeId || UserTypeConstantsInterface.HANDLER == appUserTypeId)  //for handler and agent , same productList will be loaded
                productListViewModel.setAppUserTypeId(UserTypeConstantsInterface.RETAILER);
            else
                productListViewModel.setAppUserTypeId(appUserTypeId);

            productListViewModel.setActive(Boolean.TRUE);
            SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
            wrapper.setBasePersistableModel(productListViewModel);
            LinkedHashMap<String, SortingOrder> sortingOrderMap = new LinkedHashMap<>();
            sortingOrderMap.put("productName", SortingOrder.ASC);
            wrapper.setSortingOrderMap(sortingOrderMap);
            SearchBaseWrapper result = productManager.searchProduct(wrapper);
            productListViewModelList = result.getCustomList().getResultsetList();
        }

        if(productCatalogId!=null) {
            ProductCatalogDetailModel catalogProdutDetailModel = new ProductCatalogDetailModel();
            catalogProdutDetailModel.setProductCatalogId(productCatalogId);

            BaseWrapper baseWrapper = new BaseWrapperImpl();
            baseWrapper.setBasePersistableModel(catalogProdutDetailModel);
            baseWrapper = this.productCatalogManager.loadCatalogProducts(baseWrapper);
            ProductCatalogModel catModel = (ProductCatalogModel) baseWrapper.getBasePersistableModel();

            Collection<ProductCatalogDetailModel> list = catModel.getProductCatalogIdProductCatalogDetailModelList();
            List<ProductListViewModel> prodList = new ArrayList<ProductListViewModel>();
            Iterator<ProductCatalogDetailModel> iter = list.iterator();
            while (iter.hasNext()) {

                ProductListViewModel prodListModel = new ProductListViewModel();
                ProductCatalogDetailModel productCatDetailModel = iter.next();

                ProductModel prodModel = productCatDetailModel
                        .getProductIdProductModel();
                prodListModel.setActive(prodModel.getActive());
                prodListModel.setProductId(prodModel.getPrimaryKey());
                prodListModel.setCategoryId(prodModel.getCategoryId());
                prodListModel.setProductName(prodModel.getName());
                prodListModel.setVersionNo(prodModel.getVersionNo());
                prodListModel.setSupplierName(prodModel
                        .getSupplierIdSupplierModel().getName());
                prodListModel.setSequenceNo(productCatDetailModel
                        .getSequenceNo());
                prodListModel.setChecked(true);
                prodList.add(prodListModel);
            }

            for (ProductListViewModel productListModel : productListViewModelList) {
                for (int i = 0; i < prodList.size(); i++) {
                    if (productListModel.getProductId().equals(prodList.get(i).getProductId()))
                        productListModel.setChecked(Boolean.TRUE);
                }
            }
        }
        List<ProductModelVO> productModelVOList= populateVO(productListViewModelList);

        return  productModelVOList;

    }


    public List<ProductModelVO> populateVO(List<ProductListViewModel> productListViewModelList){

        List<ProductModelVO> productModelVOList= new ArrayList<>();
        for(ProductListViewModel productListViewModel : productListViewModelList)
        {
            if(productListViewModel.getCategoryId()!=null){
                ProductModelVO productModelVO = new ProductModelVO();
                productModelVO.setActive(productListViewModel.getActive());
                productModelVO.setName(productListViewModel.getProductName());
                productModelVO.setSupplierName(productListViewModel.getSupplierName());
                productModelVO.setProductId(productListViewModel.getProductId());
                productModelVO.setChecked(productListViewModel.getChecked());
                productModelVO.setServiceName(productListViewModel.getServiceName());

                productModelVOList.add(productModelVO);
            }
        }

        return productModelVOList;
    }
}
