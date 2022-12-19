/**
 * 
 */
package com.inov8.microbank.server.dao.customermodule;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.ApplicantDetailModel;

/**
 * @author Soofiafa
 * 
 */ 
public interface ApplicantDetailDAO extends BaseDAO<ApplicantDetailModel, Long> {

	boolean isIdDocumentNumberAleardyExist(Long retailerContactId, Long idDocumentType, String idDocumentNumber);
	
}
