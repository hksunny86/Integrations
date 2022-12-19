package com.inov8.microbank.server.service.portal.esctoinov8;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.portal.esctoinov8module.EscToInov8ViewModel;
import com.inov8.microbank.server.dao.portal.esctoinov8.EscToInov8ViewDAO;

public class EscToInov8ManagerImpl implements EscToInov8Manager {
    private EscToInov8ViewDAO escToInov8ViewDAO;


    public void setEscToInov8ViewDAO(EscToInov8ViewDAO
                                     escToInov8ViewDAO) {
        this.escToInov8ViewDAO = escToInov8ViewDAO;
    }

    public SearchBaseWrapper searchEscToInov8(SearchBaseWrapper
                                              searchBaseWrapper) throws
            FrameworkCheckedException {
        //populate list
        CustomList<EscToInov8ViewModel>
                list = this.escToInov8ViewDAO.findByExample((
                        EscToInov8ViewModel)
                searchBaseWrapper.
                getBasePersistableModel(),
                searchBaseWrapper.
                getPagingHelperModel(),
                searchBaseWrapper.
                getSortingOrderMap(),searchBaseWrapper.getDateRangeHolderModel());
        
        //setting list into wrapper
        searchBaseWrapper.setCustomList(list);

        return searchBaseWrapper;
    }
}
