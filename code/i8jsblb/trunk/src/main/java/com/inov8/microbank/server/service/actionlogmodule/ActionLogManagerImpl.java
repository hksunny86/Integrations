package com.inov8.microbank.server.service.actionlogmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.actionlogmodule.ActionLogListViewModel;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.dao.actionlogmodule.ActionLogDAO;
import com.inov8.microbank.server.dao.actionlogmodule.ActionLogListViewDAO;
import com.inov8.microbank.server.dao.usecasemodule.UsecaseDAO;
import com.inov8.microbank.server.service.xml.XmlMarshaller;
import com.inov8.verifly.common.constants.ActionConstants;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Rizwan-ur-Rehman
 * @version 1.0
 */


public class ActionLogManagerImpl implements ActionLogManager
{
	private ActionLogDAO actionLogDAO;
	private ActionLogListViewDAO actionLogListViewDAO;
	private XmlMarshaller<Object> xmlMarshaller;
	private UsecaseDAO usecaseDAO;
	
			
	public SearchBaseWrapper searchActionLog(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		
		CustomList<ActionLogListViewModel> list = this.actionLogListViewDAO
		                                               
		.findByExample((ActionLogListViewModel)searchBaseWrapper
				.getBasePersistableModel(), searchBaseWrapper
				.getPagingHelperModel(), searchBaseWrapper
				.getSortingOrderMap());
					searchBaseWrapper.setCustomList(list);
				
		return searchBaseWrapper;
	}

	public BaseWrapper createOrUpdateActionLogRequiresNewTransaction(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		ActionLogModel actionLogModel = (ActionLogModel) baseWrapper.getBasePersistableModel();
		actionLogModel = this.actionLogDAO.saveOrUpdate( (ActionLogModel) baseWrapper.
				getBasePersistableModel());
		if(actionLogModel.getClientIpAddress() == null)
		{
			String clientIP = this.getClientIpAddress();
			actionLogModel.setClientIpAddress(clientIP);
		}
		baseWrapper.setBasePersistableModel(actionLogModel);
		return baseWrapper;
	}

	public BaseWrapper createOrUpdateActionLog(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		ActionLogModel actionLogModel = (ActionLogModel) baseWrapper.getBasePersistableModel();
		actionLogModel = this.actionLogDAO.saveOrUpdate( (ActionLogModel) baseWrapper.
				getBasePersistableModel());
		baseWrapper.setBasePersistableModel(actionLogModel);
		return baseWrapper;
	}
	


	public ActionLogModel createOrUpdateActionLog(ActionLogModel actionLogModel) throws FrameworkCheckedException
	{
		return this.actionLogDAO.saveOrUpdate(actionLogModel);
	}

	public BaseWrapper loadUserActionLog(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		ActionLogModel actionLogModel=this.actionLogDAO.findByPrimaryKey(baseWrapper.getBasePersistableModel().getPrimaryKey());
		baseWrapper.setBasePersistableModel(actionLogModel);
	   return baseWrapper;
	}

	
	/* (non-Javadoc)
	 * @see com.inov8.microbank.server.service.actionlogmodule.ActionLogManager#createActionLogModel(com.inov8.framework.common.wrapper.BaseWrapper)
	 */
	public ActionLogModel createActionLogRequiresNewTransaction(BaseWrapper baseWrapper) throws FrameworkCheckedException {

		ActionLogModel actionLogModel = new ActionLogModel();
		Long createdById = (Long) baseWrapper.getObject(PortalConstants.KEY_APP_USER_ID);
		Long actionAuthId = (Long) baseWrapper.getObject(PortalConstants.KEY_ACTION_AUTH_ID);
		String createdByUsername = (String) baseWrapper.getObject(PortalConstants.KEY_APPUSER_USERNAME);
		Date createdOn = (Date) baseWrapper.getObject(PortalConstants.KEY_CREATED_ON);

		actionLogModel.setActionAuthorizationId(actionAuthId);
		actionLogModel.setActionId((Long) baseWrapper.getObject(PortalConstants.KEY_ACTION_ID));
		actionLogModel.setUsecaseId((Long) baseWrapper.getObject(PortalConstants.KEY_USECASE_ID));
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_START);

		String clientIpAddress = this.getClientIpAddress();
		//Changed to accomodate maker-checker changes 
		if(null!=createdById)
			actionLogModel.setAppUserId(createdById);
		else
			actionLogModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
		if(null!=createdByUsername)
			actionLogModel.setUserName(createdByUsername);
		else
			actionLogModel.setUserName(UserUtils.getCurrentUser().getUsername());
		if(null!=createdOn)
			actionLogModel.setStartTime(new Timestamp(createdOn.getTime()));
		else
			actionLogModel.setStartTime(new Timestamp(new Date().getTime()));
		//End Changed to accomodate maker-checker changes 
		
		actionLogModel.setCustomField1((String)baseWrapper.getObject(PortalConstants.KEY_CUSTOM_FIELD_1));
		actionLogModel.setClientIpAddress(clientIpAddress);
		actionLogModel.setDeviceTypeId(PortalConstants.WEB_TYPE_DEVICE);
		
		return actionLogDAO.saveOrUpdate(actionLogModel);
	}	
	
	public ActionLogModel createCustomActionLogRequiresNewTransaction(BaseWrapper baseWrapper) throws FrameworkCheckedException {

		ActionLogModel actionLogModel = new ActionLogModel();

		actionLogModel.setActionId((Long) baseWrapper.getObject(PortalConstants.KEY_ACTION_ID));
		actionLogModel.setUsecaseId((Long) baseWrapper.getObject(PortalConstants.KEY_USECASE_ID));
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_START);

		actionLogModel.setAppUserId((Long) baseWrapper.getObject("appUser"));
		actionLogModel.setUserName((String) baseWrapper.getObject("userName"));
		actionLogModel.setStartTime(new Timestamp(new Date().getTime()));
		
		actionLogModel.setCustomField1((String)baseWrapper.getObject(PortalConstants.KEY_CUSTOM_FIELD_1));
		actionLogModel.setDeviceTypeId((Long) baseWrapper.getObject("deviceType"));
		
		return actionLogDAO.saveOrUpdate(actionLogModel);
	}

	
	/* (non-Javadoc)
	 * @see com.inov8.microbank.server.service.actionlogmodule.ActionLogManager#completeActionLog(com.inov8.microbank.common.model.ActionLogModel)
	 */
	public ActionLogModel completeActionLog(ActionLogModel actionLogModel) throws FrameworkCheckedException {
		
		actionLogModel.setEndTime( new Timestamp(new Date().getTime()) );
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END);
		actionLogModel = createOrUpdateActionLog(actionLogModel);
		
		return actionLogModel;
	}
	
	public ActionLogModel completeActionLogRequiresNewTransaction(ActionLogModel actionLogModel) throws FrameworkCheckedException{
		return this.completeActionLog(actionLogModel);
	}

	@Override
	public ActionLogModel prepareAndSaveActionLogDataRequiresNewTransaction(SearchBaseWrapper searchBaseWrapper, BaseWrapper baseWrapper, ActionLogModel actionLogModel) throws FrameworkCheckedException {
		Object objToXML = null;
		String xml = null;
		Long useCaseId = null;
		Long actionAuthId = null;
		if(actionLogModel == null || actionLogModel.getActionLogId() == null)
		{
			if(actionLogModel == null)
				actionLogModel = new ActionLogModel();
			if(searchBaseWrapper != null)
			{
				objToXML = searchBaseWrapper.getBasePersistableModel();
				useCaseId = (Long) searchBaseWrapper.getObject(PortalConstants.KEY_USECASE_ID);
				/*actionAuthId = (Long) searchBaseWrapper.getObject(PortalConstants.KEY_ACTION_AUTH_ID);*/
			}
			else if(baseWrapper != null)
			{
				if(baseWrapper.getObject("model") != null)
					objToXML = baseWrapper.getObject("model");
				else
					objToXML = baseWrapper.getBasePersistableModel();
				useCaseId = (Long) baseWrapper.getObject(PortalConstants.KEY_USECASE_ID);
				actionAuthId = (Long) baseWrapper.getObject(PortalConstants.KEY_ACTION_AUTH_ID);
			}

			if(objToXML != null)
				xml = this.getXML(objToXML);

			String clientIpAddress = this.getClientIpAddress();
			actionLogModel.setClientIpAddress(clientIpAddress);
			if(actionLogModel.getActionLogId() == null)
				actionLogModel.setActionId(2L);
			if(actionLogModel.getUsecaseId() == null)
				actionLogModel.setUsecaseId(useCaseId);
			if(actionLogModel.getActionAuthorizationId() == null)
				actionLogModel.setActionAuthorizationId(actionAuthId);
			/*if(actionLogModel.getCustomField11() == null)
				actionLogModel.setCustomField11(UserUtils.getCurrentUser().getAppUserId().toString());*/

			actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_START);
			actionLogModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
			actionLogModel.setUserName(UserUtils.getCurrentUser().getUsername());
			if(actionLogModel.getStartTime() == null)
				actionLogModel.setStartTime(new Timestamp(new Date().getTime()) );
			if(actionLogModel.getInputXml() == null)
				actionLogModel.setInputXml(xml);
			actionLogModel.setDeviceTypeId(PortalConstants.WEB_TYPE_DEVICE);
			if(actionLogModel.getTrxData() == null)
				actionLogModel.setTrxData("||||||||||||");
			//
			actionLogModel = actionLogDAO.saveOrUpdate(actionLogModel);
		}
		else
		{
			if(searchBaseWrapper != null)
			{
				//xml = this.convertListToXML(searchBaseWrapper);
			}
			else if(baseWrapper != null)
			{
				actionAuthId = (Long) baseWrapper.getObject(PortalConstants.KEY_ACTION_AUTH_ID);
				//objToXML = (Object) baseWrapper.getBasePersistableModel();
				//xml = this.getXML(objToXML);
			}

			/*if(actionLogModel.getOutputXml() == null)
				actionLogModel.setOutputXml(xml);*/
			if(actionLogModel.getActionAuthorizationId() == null && actionAuthId != null)
				actionLogModel.setActionAuthorizationId(actionAuthId);
			if(actionLogModel.getTrxData() == null)
				actionLogModel.setTrxData("|||||||||||||");//||||||||||||
			actionLogModel = completeActionLog(actionLogModel);
		}
		return actionLogModel;
	}

	@Override
	public ActionLogModel getActionLogModelByActionAuthId(Long actionAuthId) throws FrameworkCheckedException {

		return actionLogDAO.getActionLogModelByActionAuthId(actionAuthId);
	}

	private String getXML(Object obj)
	{
		String xml = null;
		try {
			xml = xmlMarshaller.marshal(obj);
		} catch (IOException e) {
			e.printStackTrace();
		}
		xml = this.formatXML(xml);
		return xml;
	}
	private String formatXML(String xml)
	{
		String strSplit = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?";
		String newXML = xml.substring(strSplit.length()+1,xml.length());
		return newXML;
	}
	private String convertListToXML(SearchBaseWrapper searchBaseWrapper)
	{
		String responseXML = null;
		String fullXML = "";
		Object objToXML = null;
		List<Object> objList = searchBaseWrapper.getCustomList().getResultsetList();
		for(int i=0;i<objList.size();i++)
		{
			objToXML = objList.get(i);
			responseXML = this.getXML(objToXML);
			fullXML += responseXML;
		}
		return fullXML;
	}

	private String getClientIpAddress()
	{
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest req = null;
		String clientIpAddress=null;
		if(null!=sra){
			req = sra.getRequest();
			if(null!=req)
			{
				clientIpAddress = UserUtils.getClientIpAddress(req) ;
			}
		}
		return clientIpAddress;
	}

	public void setActionLogListViewDAO(ActionLogListViewDAO actionLogListViewDAO) {
		this.actionLogListViewDAO = actionLogListViewDAO;
	}

	public void setActionLogDAO(ActionLogDAO actionLogDAO)
	{
		this.actionLogDAO = actionLogDAO;
	}

	public void setXmlMarshaller(XmlMarshaller<Object> xmlMarshaller) {
		this.xmlMarshaller = xmlMarshaller;
	}

	public void setUsecaseDAO(UsecaseDAO usecaseDAO) {
		this.usecaseDAO = usecaseDAO;
	}
}
