package com.inov8.microbank.server.facade.portal.bookmemodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.portal.bookmemodule.BookMeTransactionDetailManager;

public class BookMeTransactionDetailFacadeImpl implements BookMeTransactionDetailFacade {

    private BookMeTransactionDetailManager bookMeTransactionDetailManager;

    private FrameworkExceptionTranslator frameworkExceptionTranslator;



    public void setBookMeTransactionDetailManager(BookMeTransactionDetailManager bookMeTransactionDetailManager) {
        this.bookMeTransactionDetailManager = bookMeTransactionDetailManager;
    }

    public void setFrameworkExceptionTranslator(
            FrameworkExceptionTranslator frameworkExceptionTranslator) {
        this.frameworkExceptionTranslator = frameworkExceptionTranslator;
    }

    @Override
    public SearchBaseWrapper searchBookMeTransactionDetail(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        return bookMeTransactionDetailManager.searchBookMeTransactionDetail(searchBaseWrapper);
    }
}
