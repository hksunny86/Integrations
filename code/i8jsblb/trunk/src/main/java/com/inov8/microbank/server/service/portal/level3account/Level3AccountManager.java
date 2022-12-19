package com.inov8.microbank.server.service.portal.level3account;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.BusinessTypeModel;
import com.inov8.microbank.common.model.CityModel;
import com.inov8.microbank.common.model.OccupationModel;
import com.inov8.microbank.common.model.ProfessionModel;
import com.inov8.microbank.common.model.customermodule.CustomerPictureModel;

/**
 * @author NaseerUl
 *
 */ 
public interface Level3AccountManager 
{
	BaseWrapper createLevel3Account(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	BaseWrapper updateLevel3Account(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	SearchBaseWrapper searchLevel3AccountsView(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	List<CustomerPictureModel> getAllRetailerContactPictures(Long retailerContactId) throws FrameworkCheckedException;
	SearchBaseWrapper searchLevel3Account(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	boolean isIdDocumentNumberAlreadyExist(String initialAppFormNumber, Long idDocumentType, String idDocumentNumber) throws FrameworkCheckedException;
	String getLinkedCoreAccountNo(String appUserUserName) throws FrameworkCheckedException;
	boolean isCoreAccountLinkedToOtherAgent(String accountNumber, Long retailerContact)throws FrameworkCheckedException;
	CityModel loadCityModel(Long cityId) throws FrameworkCheckedException;
	BusinessTypeModel loadBusinessTypeModel(Long businessTypeId) throws FrameworkCheckedException;
	ProfessionModel loadProfessionModel(Long professionId) throws FrameworkCheckedException;
	OccupationModel loadOccupationModel(Long occupationId) throws FrameworkCheckedException;
}
