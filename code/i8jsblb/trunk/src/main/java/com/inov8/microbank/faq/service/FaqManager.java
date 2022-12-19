package com.inov8.microbank.faq.service;

import com.inov8.microbank.faq.model.FaqCatalogDetailViewModel;
import com.inov8.microbank.faq.model.FaqCatalogModel;

import java.util.List;

/**
 * Created by AtieqRe on 9/3/2016.
 */
public interface FaqManager {
    FaqCatalogModel findFaqCatalog();

    List<FaqCatalogDetailViewModel> loadFaqCatalogDetail(Long faqCatalogId);
}
