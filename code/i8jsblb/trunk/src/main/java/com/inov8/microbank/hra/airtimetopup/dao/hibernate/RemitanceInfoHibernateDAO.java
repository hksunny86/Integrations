package com.inov8.microbank.hra.airtimetopup.dao.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.hra.airtimetopup.dao.RemitanceInfoDAO;
import com.inov8.microbank.hra.airtimetopup.model.HRARemitanceInfoModel;

import java.util.List;

public class RemitanceInfoHibernateDAO extends BaseHibernateDAO<HRARemitanceInfoModel,Long,RemitanceInfoDAO>
    implements RemitanceInfoDAO{

    @Override
    public List<HRARemitanceInfoModel> getActiveRemitances() {
        HRARemitanceInfoModel remitanceInfoModel = new HRARemitanceInfoModel();
        remitanceInfoModel.setActive(Boolean.TRUE);
        List<HRARemitanceInfoModel> list = findByExample(remitanceInfoModel).getResultsetList();
        return list;
    }
}
