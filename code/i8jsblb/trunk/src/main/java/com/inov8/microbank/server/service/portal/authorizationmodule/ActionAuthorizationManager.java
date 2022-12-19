package com.inov8.microbank.server.service.portal.authorizationmodule;

import javax.servlet.http.HttpServletRequest;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthPictureModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;

public interface ActionAuthorizationManager {
	SearchBaseWrapper search( SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	SearchBaseWrapper searchConflictedAuthorizationRequests(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	BaseWrapper saveOrUpdate(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	ActionAuthorizationModel load(Long actionAuthorizationId) throws FrameworkCheckedException;
	CustomList<ActionAuthorizationModel> checkExistingRequest(ActionAuthorizationModel actionAuthorizationModel) throws FrameworkCheckedException;
	SearchBaseWrapper searchMyRequests(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	ActionAuthPictureModel getActionAuthPictureModelByTypeId(Long actionAuthorizationId,Long pictureTypeId) throws FrameworkCheckedException ;
	ActionAuthPictureModel saveOrUpdate(ActionAuthPictureModel actionAuthPictureModel) throws FrameworkCheckedException;
	String loadAuthorizationVOJson(HttpServletRequest req) throws Exception;
	
	void requestApproved(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	void actionDeniedOrCancelled(ActionAuthorizationModel actionAuthorizationModel,ActionAuthorizationModel commandModel) throws FrameworkCheckedException;
	void requestAssignedBack(ActionAuthorizationModel actionAuthorizationModel,ActionAuthorizationModel commandModel) throws FrameworkCheckedException;
	Boolean performAuthorization(BaseWrapper baseWrapper) throws FrameworkCheckedException;
}
