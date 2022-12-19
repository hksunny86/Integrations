package com.inov8.microbank.server.dao.customermodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.customermodule.BlinkCustomerPictureModel;

public interface BlinkCustomerPictureDAO extends BaseDAO<BlinkCustomerPictureModel, Long> {
    BlinkCustomerPictureModel getBlinkCustomerPictureByTypeId(Long pictureTypeId,Long customerId) throws FrameworkCheckedException;
}
