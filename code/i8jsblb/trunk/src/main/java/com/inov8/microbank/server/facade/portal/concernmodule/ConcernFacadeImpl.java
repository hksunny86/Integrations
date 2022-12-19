package com.inov8.microbank.server.facade.portal.concernmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.portal.concernmodule.ConcernManager;

public class ConcernFacadeImpl implements ConcernFacade {
	private ConcernManager concernManager;
	private FrameworkExceptionTranslator frameworkExceptionTranslator;

	public SearchBaseWrapper searchConcernsList(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try {
			return concernManager.searchConcernsList(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public SearchBaseWrapper searchConcernsHistory(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try {
			return concernManager.searchConcernsHistory(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public BaseWrapper searchConcernCategoryByName(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		try {
			return concernManager.searchConcernCategoryByName(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public SearchBaseWrapper searchConcernCategory(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try {
			return concernManager.searchConcernCategory(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public BaseWrapper createConcernCategory(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try {
			return concernManager.createConcernCategory(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
		}
	}

	public BaseWrapper searchConcernCategoryByPrimaryKey(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try {
			return concernManager
					.searchConcernCategoryByPrimaryKey(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public BaseWrapper updateConcernCategory(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try {
			return concernManager.updateConcernCategory(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
		}
	}

	public BaseWrapper createConcern(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try {
			return concernManager.createConcern(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public BaseWrapper getPartnerPartners(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try {
			return concernManager.getPartnerPartners(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public BaseWrapper loadConcern(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try {
			return concernManager.loadConcern(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public BaseWrapper loadConcernModel(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try {
			return concernManager.loadConcernModel(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public BaseWrapper updateConcernModel(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try {
			return concernManager.updateConcernModel(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
		}
	}

	public SearchBaseWrapper searchOpPartners(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {

		try {
			return concernManager.searchOpPartners(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public BaseWrapper updateConcernForResolver(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {

		try {
			return concernManager.updateConcernForResolver(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public BaseWrapper updateConcernForReply(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {

		try {
			return concernManager.updateConcernForReply(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public BaseWrapper findIndirectActiveConcern(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {

		try {
			return concernManager.findIndirectActiveConcern(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public SearchBaseWrapper searchConcernPriority()
			throws FrameworkCheckedException {
		try {
			return concernManager.searchConcernPriority();
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public SearchBaseWrapper searchOpConcernsHistory(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try {
			return concernManager.searchOpConcernsHistory(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public SearchBaseWrapper searchConcernsParentList(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try {
			return concernManager.searchConcernsParentList(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public SearchBaseWrapper searchConcernStatus()
			throws FrameworkCheckedException {
		try {
			return concernManager.searchConcernStatus();
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public SearchBaseWrapper searchConcernType()
			throws FrameworkCheckedException {
		try {
			return concernManager.searchConcernType();
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public SearchBaseWrapper searchConcernPartner()
			throws FrameworkCheckedException {
		try {
			return concernManager.searchConcernPartner();
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public SearchBaseWrapper searchConcernCategory()
			throws FrameworkCheckedException {
		try {
			return concernManager.searchConcernCategory();
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public BaseWrapper searchAppUserConcernPartnerViewByPrimaryKey(
			BaseWrapper baseWrapper) throws FrameworkCheckedException {
		try {
			return concernManager
					.searchAppUserConcernPartnerViewByPrimaryKey(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public SearchBaseWrapper searchConcernPartners(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try {
			return concernManager.searchConcernPartners(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public BaseWrapper createPartnerAssociationReferenceData(
			BaseWrapper baseWrapper) throws FrameworkCheckedException {
		try {
			return concernManager
					.createPartnerAssociationReferenceData(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public SearchBaseWrapper loadConcernPartnerRules(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try {
			return concernManager.loadConcernPartnerRules(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public BaseWrapper loadConcernPartnerByPrimaryKey(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try {
			return concernManager.loadConcernPartnerByPrimaryKey(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public BaseWrapper updatePartnerAssociationsRole(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try {
			return concernManager.updatePartnerAssociationsRole(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public SearchBaseWrapper searchAllOtherPartners(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try {
			return concernManager.searchAllOtherPartners(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	public void setConcernManager(ConcernManager concernManager) {
		this.concernManager = concernManager;
	}
}
