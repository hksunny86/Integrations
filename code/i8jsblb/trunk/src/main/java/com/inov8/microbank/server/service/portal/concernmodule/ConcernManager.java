package com.inov8.microbank.server.service.portal.concernmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

public interface ConcernManager {
	
	SearchBaseWrapper searchConcernsList(SearchBaseWrapper searchBaseWrapper)
	throws FrameworkCheckedException;
	public BaseWrapper createConcern(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public BaseWrapper loadConcern(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public SearchBaseWrapper searchConcernPriority() throws FrameworkCheckedException;
	public SearchBaseWrapper searchConcernStatus() throws FrameworkCheckedException;
	public SearchBaseWrapper searchConcernType() throws FrameworkCheckedException;
	public SearchBaseWrapper searchConcernPartner() throws FrameworkCheckedException;
	public BaseWrapper searchConcernCategoryByName(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public SearchBaseWrapper searchConcernCategory() throws FrameworkCheckedException;
	public SearchBaseWrapper searchConcernCategory(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException; 
	public BaseWrapper searchConcernCategoryByPrimaryKey(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public BaseWrapper createConcernCategory(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public BaseWrapper updateConcernCategory(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public SearchBaseWrapper searchConcernsHistory(SearchBaseWrapper searchBaseWrapper)
	throws FrameworkCheckedException;
	public BaseWrapper searchAppUserConcernPartnerViewByPrimaryKey(BaseWrapper baseWrapper)
	throws FrameworkCheckedException;

	public BaseWrapper loadConcernModel(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public BaseWrapper updateConcernModel(BaseWrapper baseWrapper) throws FrameworkCheckedException;

	public SearchBaseWrapper searchOpPartners(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	public BaseWrapper getPartnerPartners(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public BaseWrapper updateConcernForResolver(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public BaseWrapper updateConcernForReply(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public BaseWrapper findIndirectActiveConcern(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public SearchBaseWrapper searchConcernsParentList(SearchBaseWrapper searchBaseWrapper)
	throws FrameworkCheckedException;
	public SearchBaseWrapper searchConcernPartners(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	public BaseWrapper createPartnerAssociationReferenceData(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public SearchBaseWrapper loadConcernPartnerRules(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	public BaseWrapper loadConcernPartnerByPrimaryKey(BaseWrapper baseWrapper)throws FrameworkCheckedException;
	public BaseWrapper updatePartnerAssociationsRole(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public SearchBaseWrapper searchAllOtherPartners(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	public SearchBaseWrapper searchOpConcernsHistory(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
}