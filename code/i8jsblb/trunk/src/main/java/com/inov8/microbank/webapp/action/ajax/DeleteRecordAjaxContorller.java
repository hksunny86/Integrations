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
import com.inov8.microbank.server.service.ajaxtags.AjaxTagsManager;

/**
 * Project Name: 			Microbank	
 * @author Imran Sarwar
 * Creation Date: 			May 3, 2007
 * Creation Time: 			4:28:00 PM
 * Description:				
 */
public class DeleteRecordAjaxContorller extends AjaxController
{

	protected AjaxTagsManager ajaxTagsManager;
	
	@Override
	public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
		String strId = ServletRequestUtils.getStringParameter(request, AjaxTagsManager.KEY_ID);
		String propertyName = ServletRequestUtils.getStringParameter(request, AjaxTagsManager.KEY_PROPERTY_NAME);
		String modelName = ServletRequestUtils.getStringParameter(request, AjaxTagsManager.KEY_MODEL_NAME);
		String delLabel = ServletRequestUtils.getStringParameter(request, AjaxTagsManager.KEY_DEL_LABEL);
		String deletedLabel = ServletRequestUtils.getStringParameter(request, AjaxTagsManager.KEY_DELTD_LABEL);
		String hardDel = ServletRequestUtils.getStringParameter(request, AjaxTagsManager.KEY_HARD_DEL);
		String isButton = ServletRequestUtils.getStringParameter(request, AjaxTagsManager.KEY_ISBUTTON);
		String useCaseId = ServletRequestUtils.getStringParameter(request, AjaxTagsManager.KEY_USECASE_ID);
		
		if(validateRequest(strId, propertyName, modelName))
		{
			Long id = Long.parseLong(EncryptionUtil.decryptWithDES(strId));
			BasePersistableModel model = loadModel(modelName);
			model.setPrimaryKey(id);
			
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(model);
			baseWrapper.putObject(AjaxTagsManager.KEY_PROPERTY_NAME, propertyName);
			baseWrapper.putObject(AjaxTagsManager.KEY_MODEL_NAME, modelName);
			baseWrapper.putObject(AjaxTagsManager.KEY_HARD_DEL, hardDel);
			baseWrapper.putObject(AjaxTagsManager.KEY_USECASE_ID, useCaseId);
			
			baseWrapper = ajaxTagsManager.deleteRecord(baseWrapper);
			
			Boolean operationResponse = (Boolean)baseWrapper.getObject(AjaxTagsManager.KEY_RESPONSE);
			ajaxXmlBuilder.addItemAsCData(AjaxTagsManager.KEY_ID, strId);
			
			if(StringUtil.isNullOrEmpty(delLabel))
			{
				delLabel = AjaxTagsManager.DEFAULT_DEL_LABEL;
			}

			if(StringUtil.isNullOrEmpty(deletedLabel))
			{
				deletedLabel = AjaxTagsManager.DEFAULT_DELTD_LABEL;
			}
			
			ajaxXmlBuilder.addItemAsCData(AjaxTagsManager.KEY_RESPONSE, operationResponse? deletedLabel: delLabel);
			ajaxXmlBuilder.addItemAsCData(AjaxTagsManager.KEY_MSG, "The record has been deleted");
			ajaxXmlBuilder.addItemAsCData(AjaxTagsManager.KEY_ISBUTTON, StringUtil.isNullOrEmpty(isButton)
					? String.valueOf(AjaxTagsManager.DEFAULT_ISBUTTON.booleanValue()) : isButton);
			ajaxXmlBuilder.addItemAsCData(AjaxTagsManager.KEY_HARD_DEL, StringUtil.isNullOrEmpty(hardDel)
					? String.valueOf(AjaxTagsManager.DEFAULT_HARD_DEL.booleanValue()) : hardDel);
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
	protected BasePersistableModel loadModel(String modelName)
		throws InstantiationException, IllegalAccessException, ClassNotFoundException 
	{
		return (BasePersistableModel)Class.forName(modelName).newInstance();
	}
	
	protected boolean validateRequest(String id, String propertyName, String modelName)
	{
		if(StringUtil.isNullOrEmpty(id))
			throw new IllegalArgumentException("id must not be null or empty");

		if(StringUtil.isNullOrEmpty(propertyName))
			throw new IllegalArgumentException("propertyName must not be null or empty");

		if(StringUtil.isNullOrEmpty(modelName))
			throw new IllegalArgumentException("modelName must not be null or empty");

		return true;
	}

	public void setAjaxTagsManager(AjaxTagsManager ajaxTagsManager) {
		this.ajaxTagsManager = ajaxTagsManager;
	}
	
	
}
