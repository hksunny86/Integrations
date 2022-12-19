package com.inov8.microbank.server.facade.portal.partnergroupmodule;

import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.PartnerGroupModel;
import com.inov8.microbank.common.model.portal.partnergroupmodule.PartnerGroupPermissionListViewModel;
import com.inov8.microbank.common.model.portal.partnergroupmodule.UserPermissionWrapper;
import com.inov8.microbank.server.service.portal.partnergroupmodule.PartnerGroupManager;

public class PartnerGroupFacadeImpl implements PartnerGroupFacade{
	
	private PartnerGroupManager partnerGroupManager;
	private FrameworkExceptionTranslator frameworkExceptionTranslator;

	public BaseWrapper createPartnerGroup(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		try {
			return this.partnerGroupManager.createPartnerGroup(baseWrapper);
		} catch (Exception ex) {
			if (ex instanceof ConstraintViolationException)
			{
				throw new FrameworkCheckedException("ConstraintViolationException", ex);
			}
			if (ex instanceof DataIntegrityViolationException)
			{
				throw new FrameworkCheckedException("DataIntegrityViolationException", ex);
			}
			else
			{
				throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
			}
		}
	}

	public SearchBaseWrapper loadPartnerGroup(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		try {
			return this.partnerGroupManager.loadPartnerGroup(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
							FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
		}
	}

	public BaseWrapper loadPartnerGroupView(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		 
		try {
			return this.partnerGroupManager.loadPartnerGroupView(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator
					.translate(
							ex,
							FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
		}
	}
	
	public BaseWrapper loadPartnerGroup(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		 
		try {
			return this.partnerGroupManager.loadPartnerGroup(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator
					.translate(
							ex,
							FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
		}
	}

	public SearchBaseWrapper searchPartnerGroup(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		try {
			return this.partnerGroupManager.searchPartnerGroup(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public BaseWrapper updatePartnerGroup(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		try {
			return this.partnerGroupManager.updatePartnerGroup(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	public void setPartnerGroupManager(PartnerGroupManager partnerGroupManager) {
		this.partnerGroupManager = partnerGroupManager;
	}

	public List<UserPermissionWrapper> loadPartnerPermission(Long partnerId)
			throws FrameworkCheckedException {
		try {
			return this.partnerGroupManager.loadPartnerPermission(partnerId);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator
					.translate(
							ex,
							FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
		}
	}

	public BaseWrapper loadAppUserPartner(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try {
			return this.partnerGroupManager.loadAppUserPartner(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator
					.translate(
							ex,
							FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
		}

	}

	public List<PartnerGroupModel> getPartnerGroups(PartnerGroupModel partnerGroupModel,Boolean admin) throws FrameworkCheckedException {
		
		
		try {
			return this.partnerGroupManager.getPartnerGroups(partnerGroupModel,admin);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator
					.translate(
							ex,
							FrameworkExceptionTranslator.FIND_ACTION);
		}

	}

	public SearchBaseWrapper searchDefaultPartnerPermission(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try {
			return this.partnerGroupManager.searchDefaultPartnerPermission(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator
					.translate(
							ex,
							FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
		}

	}

	public BaseWrapper activateDeactivatePartnerGroup(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		
		try {
			
			baseWrapper=this.partnerGroupManager.activateDeactivatePartnerGroup(baseWrapper);
			return baseWrapper;
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator
					.translate(
							ex,
							FrameworkExceptionTranslator.UPDATE_ACTION);
		}

		
	}	
	
	@Override
	public SearchBaseWrapper getAllPartnerGroups() throws FrameworkCheckedException
	{
		try
		{
			return	this.partnerGroupManager.getAllPartnerGroups();
			 
		} catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	@Override
	public void validatePartnerGroup(PartnerGroupPermissionListViewModel partnerGroupPermissionListViewModel)
			throws FrameworkCheckedException {
		try
		{
			this.partnerGroupManager.validatePartnerGroup(partnerGroupPermissionListViewModel);
			 
		} catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_ACTION);
		}
		
	}
}
