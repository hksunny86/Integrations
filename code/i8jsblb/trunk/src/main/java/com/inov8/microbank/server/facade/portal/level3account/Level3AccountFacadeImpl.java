/**
 * 
 */
package com.inov8.microbank.server.facade.portal.level3account;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.BusinessTypeModel;
import com.inov8.microbank.common.model.CityModel;
import com.inov8.microbank.common.model.OccupationModel;
import com.inov8.microbank.common.model.ProfessionModel;
import com.inov8.microbank.common.model.customermodule.CustomerPictureModel;
import com.inov8.microbank.server.service.portal.level3account.Level3AccountManager;

/**
 * @author NaseerUl
 *
 */ 
public class Level3AccountFacadeImpl implements Level3AccountFacade
{
	private Level3AccountManager level3AccountManager; 
	private FrameworkExceptionTranslator frameworkExceptionTranslator;

	public Level3AccountFacadeImpl()
	{
		
	}

	@Override
	public SearchBaseWrapper searchLevel3AccountsView(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
		try
		{
			return level3AccountManager.searchLevel3AccountsView(searchBaseWrapper);
		}
		catch (Exception e)
		{
			throw frameworkExceptionTranslator.translate(e, FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	@Override
	public BaseWrapper createLevel3Account(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		try
		{
			return level3AccountManager.createLevel3Account(baseWrapper);
		}
		catch (Exception e)
		{
			throw frameworkExceptionTranslator.translate(e, FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
		}
	}

	@Override
	public BaseWrapper updateLevel3Account(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		try
		{
			return level3AccountManager.updateLevel3Account(baseWrapper);
		}
		catch (Exception e)
		{
			throw frameworkExceptionTranslator.translate(e, FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
		}
	}

	public void setLevel3AccountManager(Level3AccountManager level3AccountManager)
	{
		this.level3AccountManager = level3AccountManager;
	}

	public void setFrameworkExceptionTranslator(FrameworkExceptionTranslator frameworkExceptionTranslator)
	{
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	@Override
	public List<CustomerPictureModel> getAllRetailerContactPictures(
			Long retailerContactId) throws FrameworkCheckedException {
		try{
			return this.level3AccountManager.getAllRetailerContactPictures(retailerContactId);
		}catch(Exception e){
			throw frameworkExceptionTranslator.translate(e, FrameworkExceptionTranslator.FIND_ACTION);
		}
	}
	
	@Override
	public SearchBaseWrapper searchLevel3Account(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
		try
        {
            return  this.level3AccountManager.searchLevel3Account(searchBaseWrapper);
        }
		catch(Exception exp)
		{
            throw this.frameworkExceptionTranslator.translate(exp,FrameworkExceptionTranslator.FIND_ACTION);
        }
	}
	
	@Override
	public boolean isIdDocumentNumberAlreadyExist(String initialAppFormNumber, Long idDocumentType, String idDocumentNumber)
			throws FrameworkCheckedException
	{
		try
        {
            return  this.level3AccountManager.isIdDocumentNumberAlreadyExist(initialAppFormNumber, idDocumentType, idDocumentNumber);
        }
		catch(Exception exp)
		{
            throw this.frameworkExceptionTranslator.translate(exp,FrameworkExceptionTranslator.FIND_ACTION);
        }	
	}
	
	@Override
	public String getLinkedCoreAccountNo(String appUserUserName) throws FrameworkCheckedException
	{
		try
        {
            return  this.level3AccountManager.getLinkedCoreAccountNo(appUserUserName);
        }
		catch(Exception exp)
		{
            throw this.frameworkExceptionTranslator.translate(exp,FrameworkExceptionTranslator.FIND_ACTION);
        }
	}
	
	@Override
	public boolean isCoreAccountLinkedToOtherAgent(String accountNumber, Long retailerContact) throws FrameworkCheckedException
	{
		try
        {
            return  this.level3AccountManager.isCoreAccountLinkedToOtherAgent(accountNumber, retailerContact);
        }
		catch(Exception exp)
		{
            throw this.frameworkExceptionTranslator.translate(exp,FrameworkExceptionTranslator.FIND_ACTION);
        }
	}

	@Override
	public CityModel loadCityModel(Long cityId)
			throws FrameworkCheckedException {
		try {
            return  this.level3AccountManager.loadCityModel(cityId);
        } catch(Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp,FrameworkExceptionTranslator.FIND_ACTION);
        }
	}

	@Override
	public BusinessTypeModel loadBusinessTypeModel(Long businessTypeId)
			throws FrameworkCheckedException {
		try {
            return  this.level3AccountManager.loadBusinessTypeModel(businessTypeId);
        } catch(Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp,FrameworkExceptionTranslator.FIND_ACTION);
        }
	}

	@Override
	public ProfessionModel loadProfessionModel(Long professionId)
			throws FrameworkCheckedException {
		try {
            return  this.level3AccountManager.loadProfessionModel(professionId);
        } catch(Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp,FrameworkExceptionTranslator.FIND_ACTION);
        }
	}

	@Override
	public OccupationModel loadOccupationModel(Long occupationId)
			throws FrameworkCheckedException {
		try {
            return  this.level3AccountManager.loadOccupationModel(occupationId);
        } catch(Exception exp) {
            throw this.frameworkExceptionTranslator.translate(exp,FrameworkExceptionTranslator.FIND_ACTION);
        }
	}
}
