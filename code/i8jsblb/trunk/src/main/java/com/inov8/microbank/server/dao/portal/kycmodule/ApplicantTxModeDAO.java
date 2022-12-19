package com.inov8.microbank.server.dao.portal.kycmodule;

import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.ApplicantTxModeModel;

/**
 * @author basit.mehr
 *
 */
public interface ApplicantTxModeDAO  extends BaseDAO<ApplicantTxModeModel, Long>{

	public void deleteApplicantTransactionMode(SearchBaseWrapper searchWrapper);

}
