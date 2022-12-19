package com.inov8.microbank.faq.service;

import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.microbank.faq.dao.FaqCatalogDAO;
import com.inov8.microbank.faq.dao.FaqCatalogDetailViewDAO;
import com.inov8.microbank.faq.model.FaqCatalogDetailViewModel;
import com.inov8.microbank.faq.model.FaqCatalogModel;
import org.apache.commons.collections.CollectionUtils;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by AtieqRe on 9/3/2016.
 */
public class FaqManagerImpl implements FaqManager {

    private FaqCatalogDAO faqCatalogDAO;
    private FaqCatalogDetailViewDAO faqCatalogDetailViewDAO;

    @Override
    public FaqCatalogModel findFaqCatalog() {
        CustomList<FaqCatalogModel> list = faqCatalogDAO.findAll();
        if (!CollectionUtils.isEmpty(list.getResultsetList()))
            return list.getResultsetList().get(0);

        return null;
    }

    @Override
    public List<FaqCatalogDetailViewModel> loadFaqCatalogDetail(Long faqCatalogId) {
        FaqCatalogDetailViewModel faqCatalogDetailViewModel = new FaqCatalogDetailViewModel();
        LinkedHashMap<String, SortingOrder> sortingOrderMap = new LinkedHashMap<String, SortingOrder>();
        sortingOrderMap.put("questionNo", SortingOrder.ASC);
        faqCatalogDetailViewModel.setFaqCatalogId(faqCatalogId);
        faqCatalogDetailViewModel.setFaqCategoryId(1L);

        CustomList<FaqCatalogDetailViewModel> customList = faqCatalogDetailViewDAO.findByExample(faqCatalogDetailViewModel, null, sortingOrderMap);
        if (!CollectionUtils.isEmpty(customList.getResultsetList())) {
            return customList.getResultsetList();
        }

        return null;
    }

    public void setFaqCatalogDAO(FaqCatalogDAO faqCatalogDAO) {
        this.faqCatalogDAO = faqCatalogDAO;
    }

    public void setFaqCatalogDetailViewDAO(FaqCatalogDetailViewDAO faqCatalogDetailViewDAO) {
        this.faqCatalogDetailViewDAO = faqCatalogDetailViewDAO;
    }
}
