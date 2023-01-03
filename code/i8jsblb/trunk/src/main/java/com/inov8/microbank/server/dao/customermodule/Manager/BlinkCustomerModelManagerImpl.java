package com.inov8.microbank.server.dao.customermodule.Manager;
/* 
Created by IntelliJ IDEA 
@Project Name: trunk.
  @Copyright: 3/17/2022 On: 12:03 PM
  @author(Muhammad Aqeel)
*/

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.BlinkCustomerModel;
import com.inov8.microbank.server.dao.customermodule.BlinkCustomerModelDAO;

import java.util.List;

public class BlinkCustomerModelManagerImpl implements BlinkCustomerModelManager{
private BlinkCustomerModelDAO blinkCustomerModelDAO;

    @Override
    public SearchBaseWrapper searchAllData(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        CustomList<BlinkCustomerModel> list=blinkCustomerModelDAO.findByExample((BlinkCustomerModel) searchBaseWrapper.getBasePersistableModel(),
                searchBaseWrapper.getPagingHelperModel(),null,searchBaseWrapper.getDateRangeHolderModel());
        if(list!=null || list.getResultsetList().size()!=0){
            searchBaseWrapper.setCustomList(list);
        }
        return searchBaseWrapper;
    }

    @Override
    public List<BlinkCustomerModel> loadAllClsPendingBlinkCustomer() throws FrameworkCheckedException {
        return null;
    }


    public void setBlinkCustomerModelDAO(BlinkCustomerModelDAO blinkCustomerModelDAO) {
        this.blinkCustomerModelDAO = blinkCustomerModelDAO;
    }
}
