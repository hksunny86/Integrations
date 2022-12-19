package com.inov8.microbank.webapp.action.notificationmessagemodule;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.framework.webapp.action.ControllerUtils;
import com.inov8.microbank.common.model.MessageTypeModel;
import com.inov8.microbank.common.model.NotificationMessageModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.messagemodule.NotificationMessageManager;

public class NotificationMessageFormController extends AdvanceFormController 
{

	private  NotificationMessageManager notificationMessageManager;
	private  ReferenceDataManager referenceDataManager;
	private Long id;
	
	public NotificationMessageFormController() {
		setCommandName("notificationMessageModel");
		setCommandClass(NotificationMessageModel.class);
	}
	@Override
	protected Object loadFormBackingObject(HttpServletRequest request) throws Exception {
		id = ServletRequestUtils.getLongParameter(request,"mesId");
		if (null != id) 
		{
			if (log.isDebugEnabled()) 
			{
				log.debug("id is not null....retrieving object from DB");
			}

			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			NotificationMessageModel notificationMessageModel = new NotificationMessageModel();
			notificationMessageModel.setPrimaryKey(id);
			searchBaseWrapper.setBasePersistableModel(notificationMessageModel);
			searchBaseWrapper=this.notificationMessageManager.loadNotificationMessage(searchBaseWrapper);
			
			return (NotificationMessageModel) searchBaseWrapper.getBasePersistableModel();
			} 
		else 
		{
			if (log.isDebugEnabled()) 
			{
				log.debug("id is null....creating new instance of Model");
			}

				return new NotificationMessageModel();
		}
		
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest arg0) throws Exception {
		
		MessageTypeModel messageTypeModel=new MessageTypeModel();
		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(messageTypeModel,"name", SortingOrder.ASC);
				
		try{
		    referenceDataManager.getReferenceData(referenceDataWrapper);	
		}
		catch(FrameworkCheckedException exp){
			exp.printStackTrace();
		}
		List<MessageTypeModel> messageTypeList = null;
	    if (referenceDataWrapper.getReferenceDataList() != null)
	    {
	    	messageTypeList = referenceDataWrapper.getReferenceDataList();
	    }

	    Map referenceDataMap = new HashMap();
	    referenceDataMap.put("messageTypeList", messageTypeList);
	    
		return referenceDataMap;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse arg1, Object object, BindException arg3) throws Exception {
				
		BaseWrapper baseWrapper = new BaseWrapperImpl();	    
	       	
	    NotificationMessageModel notificationMessageModel=(NotificationMessageModel)object;
	    notificationMessageModel.setVersionNo(new Integer(1));
	    notificationMessageModel.setCreatedOn(new Date());
	    notificationMessageModel.setUpdatedOn(new Date());
	    notificationMessageModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
	    notificationMessageModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
	   
	    baseWrapper.setBasePersistableModel(notificationMessageModel);
	    
	    baseWrapper=notificationMessageManager.updateNotificationMessage(baseWrapper);		
	    ControllerUtils.saveMessage(request, "Record saved successfully");
	    	   
	   
	    if (null != baseWrapper.getBasePersistableModel())
	      {

	       		
	       		ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
	       		return modelAndView;
	        }
	        
	      
		
		return null;
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object object, BindException arg3) throws Exception 
	{
		
			
		Long messageTypeId=(Long.parseLong(request.getParameter("messageTypeId")));
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		NotificationMessageModel notificationMessageModel = new NotificationMessageModel();
		notificationMessageModel.setPrimaryKey(id);
		searchBaseWrapper.setBasePersistableModel(notificationMessageModel);
		searchBaseWrapper=this.notificationMessageManager.loadNotificationMessage(searchBaseWrapper);
	    
		NotificationMessageModel notificationMessageModelNew=new NotificationMessageModel();
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		notificationMessageModelNew=(NotificationMessageModel)searchBaseWrapper.getBasePersistableModel();
		notificationMessageModelNew.setSmsMessageText(((NotificationMessageModel)object).getSmsMessageText());
		notificationMessageModelNew.setEmailMessageText(((NotificationMessageModel)object).getEmailMessageText());	    
		notificationMessageModelNew.setMessageTypeId(messageTypeId)	;    	
		notificationMessageModelNew.setUpdatedOn(new Date());
		notificationMessageModelNew.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
		
		baseWrapper.setBasePersistableModel(notificationMessageModelNew);
	    
	    baseWrapper=notificationMessageManager.updateNotificationMessage(baseWrapper);		
	    ControllerUtils.saveMessage(request, "Record saved successfully");
	    	   
	   
	    if (null != baseWrapper.getBasePersistableModel())
	      {

	       		
	       		ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
	       		return modelAndView;
	        }
	        
	      
		
		return null;
	}
	public void setNotificationMessageManager(
			NotificationMessageManager notificationMessageManager) {
		this.notificationMessageManager = notificationMessageManager;
	}
	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}



}
