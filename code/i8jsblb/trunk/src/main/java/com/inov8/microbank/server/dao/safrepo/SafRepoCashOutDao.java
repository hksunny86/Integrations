package com.inov8.microbank.server.dao.safrepo;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.SafRepoCashOutModel;

/**
 * Created by Attique on 9/6/2018.
 */
public interface SafRepoCashOutDao extends BaseDAO<SafRepoCashOutModel,Long> {
    public void updateStatus(SafRepoCashOutModel model);
}
