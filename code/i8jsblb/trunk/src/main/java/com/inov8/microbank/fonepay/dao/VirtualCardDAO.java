package com.inov8.microbank.fonepay.dao;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.fonepay.model.VirtualCardModel;
import com.inov8.microbank.fonepay.vo.NADRADataVO;

import java.sql.SQLException;

/**
 * Created by Attique on 7/14/2017.
 */
public interface VirtualCardDAO extends BaseDAO<VirtualCardModel,Long> {
    public void updateRegData(NADRADataVO vo) throws SQLException;
}
