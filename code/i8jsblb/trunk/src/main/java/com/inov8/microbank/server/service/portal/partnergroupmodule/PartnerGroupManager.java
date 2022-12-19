package com.inov8.microbank.server.service.portal.partnergroupmodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.PartnerGroupModel;
import com.inov8.microbank.common.model.portal.partnergroupmodule.PartnerGroupPermissionListViewModel;
import com.inov8.microbank.common.model.portal.partnergroupmodule.UserPermissionWrapper;

public interface PartnerGroupManager {
	
	public static final String	KEY_IS_ACTIVE = "isActive";
	
	SearchBaseWrapper loadPartnerGroup(SearchBaseWrapper searchBaseWrapper) throws
	FrameworkCheckedException;

	BaseWrapper loadPartnerGroupView(BaseWrapper baseWrapper) throws
	FrameworkCheckedException;
	
	BaseWrapper loadPartnerGroup(BaseWrapper baseWrapper) throws
	FrameworkCheckedException;

	SearchBaseWrapper searchPartnerGroup(SearchBaseWrapper searchBaseWrapper) throws
	FrameworkCheckedException;

	BaseWrapper updatePartnerGroup(BaseWrapper baseWrapper) throws
	FrameworkCheckedException;

	BaseWrapper createPartnerGroup(BaseWrapper baseWrapper) throws
	FrameworkCheckedException;	
	
	List<UserPermissionWrapper> loadPartnerPermission(Long partnerId) throws
	FrameworkCheckedException;
	
	SearchBaseWrapper searchDefaultPartnerPermission(SearchBaseWrapper searchBaseWrapper) throws
	FrameworkCheckedException;
	
	BaseWrapper loadAppUserPartner(BaseWrapper baseWrapper) throws
	FrameworkCheckedException;
	public List<PartnerGroupModel> getPartnerGroups(PartnerGroupModel partnerGroupModel,Boolean admin)throws FrameworkCheckedException;
	public BaseWrapper activateDeactivatePartnerGroup(BaseWrapper baseWrapper) throws FrameworkCheckedException ;

	SearchBaseWrapper getAllPartnerGroups() throws FrameworkCheckedException;
	void validatePartnerGroup(PartnerGroupPermissionListViewModel partnerGroupPermissionListViewModel) throws FrameworkCheckedException;

}
