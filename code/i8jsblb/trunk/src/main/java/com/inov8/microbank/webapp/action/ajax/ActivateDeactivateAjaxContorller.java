/**
 * 
 */
package com.inov8.microbank.webapp.action.ajax;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.springframework.web.bind.ServletRequestUtils;

import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.server.service.activatedeactivate.ActivateDeactivateManager;

/**
 * Project Name: 			Microbank	
 * @author Imran Sarwar
 * Creation Date: 			May 3, 2007
 * Creation Time: 			4:28:00 PM
 * Description:				
 */
public class ActivateDeactivateAjaxContorller extends AjaxController
{

	protected ActivateDeactivateManager activateDeactivateManager;
	
	@Override
	public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
		//Long id = ServletRequestUtils.getLongParameter(request, KEY_ID);
		String strId = ServletRequestUtils.getStringParameter(request, ActivateDeactivateManager.KEY_ID);
		String propertyName = ServletRequestUtils.getStringParameter(request, ActivateDeactivateManager.KEY_PROPERTY_NAME);
		String modelName = ServletRequestUtils.getStringParameter(request, ActivateDeactivateManager.KEY_MODEL_NAME);
		String activeMessageArg = ServletRequestUtils.getStringParameter(request, ActivateDeactivateManager.KEY_ACTIVE_MSG_ARG);
		String deactiveMessageArg = ServletRequestUtils.getStringParameter(request, ActivateDeactivateManager.KEY_DEACTIVE_MSG_ARG);
		String activeLabel = ServletRequestUtils.getStringParameter(request, ActivateDeactivateManager.KEY_ACTIVE_LABEL);
		String deactiveLabel = ServletRequestUtils.getStringParameter(request, ActivateDeactivateManager.KEY_DEACTIVE_LABEL);
		String isButton = ServletRequestUtils.getStringParameter(request, ActivateDeactivateManager.KEY_ISBUTTON);
		
		if(validateRequest(strId, propertyName, modelName))
		{
			Long id = Long.parseLong(EncryptionUtil.decryptWithDES(strId));
			BasePersistableModel model = loadModel(modelName);
			model.setPrimaryKey(id);
			
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(model);
			baseWrapper.putObject(ActivateDeactivateManager.KEY_PROPERTY_NAME, propertyName);
			baseWrapper.putObject(ActivateDeactivateManager.KEY_MODEL_NAME, modelName);
			baseWrapper = activateDeactivateManager.activateDeactivate(baseWrapper);
			
			Boolean operationResponse = (Boolean)baseWrapper.getObject(ActivateDeactivateManager.KEY_RESPONSE);
			ajaxXmlBuilder.addItemAsCData(ActivateDeactivateManager.KEY_ID, strId);
			//ajaxXmlBuilder.addItemAsCData(ActivateDeactivateManager.KEY_RESPONSE, operationResponse.toString());
			
			if(StringUtil.isNullOrEmpty(activeMessageArg))
			{
				activeMessageArg = ActivateDeactivateManager.DEFAULT_ACTIVE_MSG_ARG;
			}

			if(StringUtil.isNullOrEmpty(deactiveMessageArg))
			{
				deactiveMessageArg = ActivateDeactivateManager.DEFAULT_DEACTIVE_MSG_ARG;
			}

			if(StringUtil.isNullOrEmpty(activeLabel))
			{
				activeLabel = ActivateDeactivateManager.DEFAULT_ACTIVE_LABEL;
			}

			if(StringUtil.isNullOrEmpty(deactiveLabel))
			{
				deactiveLabel = ActivateDeactivateManager.DEFAULT_DEACTIVE_LABEL;
			}

			String status = activeMessageArg;
			
			if(!operationResponse)
				status = deactiveMessageArg;

			ajaxXmlBuilder.addItemAsCData(ActivateDeactivateManager.KEY_RESPONSE, operationResponse? deactiveLabel: activeLabel);
			ajaxXmlBuilder.addItemAsCData(ActivateDeactivateManager.KEY_MSG, getMessage(request, "activateDeactivate.success", new String[] {status}));
			ajaxXmlBuilder.addItemAsCData(ActivateDeactivateManager.KEY_ISBUTTON, StringUtil.isNullOrEmpty(isButton)
					? String.valueOf(ActivateDeactivateManager.DEFAULT_ISBUTTON.booleanValue())
							: isButton);			
		}	
		return ajaxXmlBuilder.toString();
	}

	/**
	 * Loads the given BasePersistableModel 
	 * @param fqdn
	 * @return new instance of BasePersistableModel
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	protected BasePersistableModel loadModel(String fqdn)
		throws InstantiationException, IllegalAccessException, ClassNotFoundException 
	{
		return (BasePersistableModel)Class.forName(fqdn).newInstance();
	}
	
	protected boolean validateRequest(String id, String propertyName, String fqdn)
	{
		if(StringUtil.isNullOrEmpty(id))
			throw new IllegalArgumentException("id must not be null or empty");

		if(StringUtil.isNullOrEmpty(propertyName))
			throw new IllegalArgumentException("propertyName must not be null or empty");

		if(StringUtil.isNullOrEmpty(fqdn))
			throw new IllegalArgumentException("fqdn must not be null or empty");

		return true;
	}
	
	/**
	 * @param activateDeactivateManager the activateDeactivateManager to set
	 */
	public void setActivateDeactivateManager(ActivateDeactivateManager activateDeactivateManager)
	{
		this.activateDeactivateManager = activateDeactivateManager;
	}
	
	
}
