package com.inov8.microbank.server.service.portal.collectionpaymentmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.portal.collectionpaymentsmodule.CollectionPaymentsViewModel;
import com.inov8.microbank.server.dao.portal.collectionpaymentmodule.CollectionPaymentsViewDao;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Jul 11, 2013 11:07:10 AM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class CollectionPaymentManagerImpl implements CollectionPaymentManager
{
    //Autowired
    private CollectionPaymentsViewDao collectionPaymentsViewDao;

    @Override
    public SearchBaseWrapper searchCollectionPaymentsView( SearchBaseWrapper wrapper ) throws FrameworkCheckedException
    {
        CollectionPaymentsViewModel model = (CollectionPaymentsViewModel) wrapper.getBasePersistableModel();
        CustomList<CollectionPaymentsViewModel> customList = collectionPaymentsViewDao.findByExample( model, wrapper.getPagingHelperModel(), wrapper.getSortingOrderMap(), wrapper.getDateRangeHolderModel() );
        wrapper.setCustomList( customList );
        return wrapper;
    }

    public void setCollectionPaymentsViewDao( CollectionPaymentsViewDao collectionPaymentsViewDao )
    {
        this.collectionPaymentsViewDao = collectionPaymentsViewDao;
    }

}
