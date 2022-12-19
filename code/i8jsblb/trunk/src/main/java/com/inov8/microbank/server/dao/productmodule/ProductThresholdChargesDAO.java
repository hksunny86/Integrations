package com.inov8.microbank.server.dao.productmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.ProductThresholdChargesModel;

import java.util.List;

public interface ProductThresholdChargesDAO extends BaseDAO<ProductThresholdChargesModel, Long> {

    List<ProductThresholdChargesModel> loadProductThresholdCharges(String hql, Object[] parameterList) throws FrameworkCheckedException;

    void updateAndSaveProductThresholdCharges(List<ProductThresholdChargesModel> existingList, List<ProductThresholdChargesModel> newList );

}
