/**
 * 
 */
package com.inov8.microbank.server.service.ajaxtags;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.apache.commons.beanutils.BeanUtilsBean;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.BulkDisbursementsModel;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;

/**
 * Project Name: 			Microbank	
 * @author Imran Sarwar
 * Creation Date: 			May 3, 2007
 * Creation Time: 			5:59:28 PM
 * Description:				
 */
public class AjaxTagsManagerImpl implements AjaxTagsManager
{
	protected GenericDao genericDao;
	private ActionLogManager actionLogManager;
	
	public BaseWrapper deleteRecord(BaseWrapper  baseWrapper)
			throws FrameworkCheckedException
	{
		ActionLogModel actionLogModel = null;
		BasePersistableModel model = (BasePersistableModel)baseWrapper.getBasePersistableModel();
		final String propertyName = (String) baseWrapper.getObject(KEY_PROPERTY_NAME);
		final String modelName = (String)baseWrapper.getObject(AjaxTagsManager.KEY_MODEL_NAME);
		final String hardDel = (String)baseWrapper.getObject(AjaxTagsManager.KEY_HARD_DEL);
		final String useCaseId = (String)baseWrapper.getObject(AjaxTagsManager.KEY_USECASE_ID);
		model = genericDao.getEntityByPrimaryKey(model);
		
		try
		{
			baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_DELETE);
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, Long.valueOf(useCaseId));
			actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
			ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

			BeanUtilsBean beanUtils = BeanUtilsBean.getInstance();
			Boolean isDelete  = Boolean.valueOf(beanUtils.getProperty(model, propertyName));
			if(hardDel.equalsIgnoreCase("false")){
				beanUtils.setProperty(model, "updatedOn", new Date());
				beanUtils.setProperty(model, "updatedBy", UserUtils.getCurrentUser().getAppUserId());
				beanUtils.setProperty(model, propertyName, !isDelete);
				genericDao.updateEntity(model);
				baseWrapper.setBasePersistableModel(model);
				baseWrapper.putObject(KEY_RESPONSE, !isDelete);	
			}else if(hardDel.equalsIgnoreCase("true")){
				genericDao.deleteEntity(model);
				baseWrapper.putObject(KEY_RESPONSE, !isDelete);	
				baseWrapper.putObject(KEY_HARD_DEL, hardDel);	
			}
			
			if("com.inov8.microbank.common.model.BulkDisbursementsModel".equals(modelName)){
				BulkDisbursementsModel bulkDisbursementsModel = (BulkDisbursementsModel) model;
				actionLogModel.setCustomField11(bulkDisbursementsModel.getCnic());
			}
			
			if(actionLogModel != null && model.getPrimaryKey() != null){
				actionLogModel.setCustomField1(""+model.getPrimaryKey());
				this.actionLogManager.completeActionLog(actionLogModel);
			}
			
			return baseWrapper;
		}
		catch(InvocationTargetException iex)
		{
			throw new FrameworkCheckedException("InvocationTargetException =>", iex);
		}
		catch(NoSuchMethodException nex)
		{
			throw new FrameworkCheckedException("NoSuchMethodException =>", nex);
		}
		catch(IllegalAccessException ilex)
		{
			throw new FrameworkCheckedException("IllegalAccessException =>", ilex);
		}
		
	}

	/**
	 * @return the genericDao
	 */
	public GenericDao getGenericDao()
	{
		return genericDao;
	}

	/**
	 * @param genericDao the genericDao to set
	 */
	public void setGenericDao(GenericDao genericDao)
	{
		this.genericDao = genericDao;
	}

	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}
}
