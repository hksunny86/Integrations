package com.inov8.microbank.hra.airtimetopup.dao;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.hra.airtimetopup.model.HRARemitanceInfoModel;

import java.util.List;

public interface RemitanceInfoDAO extends BaseDAO<HRARemitanceInfoModel,Long> {

    List<HRARemitanceInfoModel> getActiveRemitances();
}
