package com.inov8.microbank.server.dao.retailermodule.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.BulkAgentDataHolderModel;
import com.inov8.microbank.server.dao.retailermodule.BulkAgentCreationDAO;

import java.util.ArrayList;
import java.util.List;

public class BulkAgentCreationHibernateDAO extends BaseHibernateDAO<BulkAgentDataHolderModel,Long,BulkAgentCreationDAO> implements BulkAgentCreationDAO {
    @Override
    public List<BulkAgentDataHolderModel> getDataForAgentCreation(Long isProcessByScheduler) throws FrameworkCheckedException {
        List<BulkAgentDataHolderModel> resultList = new ArrayList<>(0);
        try{
            String hql = "FROM BulkAgentDataHolderModel badhm WHERE badhm.isProcessedByScheduler = ? ";

            resultList = (List<BulkAgentDataHolderModel>)this.getHibernateTemplate().find(hql, isProcessByScheduler);


        }catch(Exception ex){
            logger.error(ex.getMessage(), ex);
        }

        return resultList;
    }
}
