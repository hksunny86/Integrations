/**
 * 
 */
package com.inov8.microbank.server.dao.customermodule;

import java.util.List;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.customermodule.CustomerPictureModel;

/**
 * @author Soofiafa
 * 
 */
public interface CustomerPictureDAO extends BaseDAO<CustomerPictureModel, Long> {
	CustomerPictureModel getCustomerPictureByTypeId(Long pictureTypeId, Long customerId);
	CustomerPictureModel getCustomerPictureByTypeIdAndStatus(Long pictureTypeId, Long customerId);
	List<CustomerPictureModel> getAllCustomerPictures(Long customerId);
	List<CustomerPictureModel> getDiscrepantCustomerPictures(Long customerId);
	List<CustomerPictureModel> getAllRetailerContactPictures(Long retailerContactId);
	Boolean isCustomerIdExists(Long customerId);
	public void updateCustomerPictureModel(Long pictureTypeId, byte[] picture, Long customerId);

}
