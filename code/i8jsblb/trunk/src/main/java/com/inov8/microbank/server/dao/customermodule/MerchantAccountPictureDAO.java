package com.inov8.microbank.server.dao.customermodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.customermodule.BlinkCustomerPictureModel;
import com.inov8.microbank.common.model.customermodule.MerchantAccountPictureModel;

public interface MerchantAccountPictureDAO extends BaseDAO<MerchantAccountPictureModel, Long> {
    MerchantAccountPictureModel getMerchantAccountPictureByTypeId(Long pictureTypeId, Long customerId) throws FrameworkCheckedException;
}
