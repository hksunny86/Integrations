/**
 * 
 */
package com.inov8.microbank.webapp.action.portal.opconcernsmodule;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.springframework.web.bind.ServletRequestUtils;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.model.ConcernCategoryModel;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.portal.concernmodule.ConcernManager;
import com.inov8.microbank.webapp.action.ajax.AjaxController;

/**
 * Project Name: 			Microbank	
 * @author Imran Sarwar
 * Creation Date: 			Jan 19, 2007
 * Creation Time: 			8:42:08 PM
 * Description:				
 */
public class OpConcernCategoryAjaxController extends AjaxController
{

	private ConcernManager concernManager;

	/* (non-Javadoc)
	 * @see com.inov8.microbank.webapp.action.ajax.AjaxController#getResponseContent(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
		
		Long concernCategoryId = new Long(ServletRequestUtils.getStringParameter(request,"btnDel"));

		//getting log information from the request
		Long actionId = ServletRequestUtils.getLongParameter(request, PortalConstants.KEY_ACTION_ID);
		

			ConcernCategoryModel concernCategoryModel = new ConcernCategoryModel();
			concernCategoryModel.setConcernCategoryId(concernCategoryId);
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(concernCategoryModel);
			try{
			   Date nowDate = new Date(); 	
			   baseWrapper = this.concernManager.searchConcernCategoryByPrimaryKey(baseWrapper);
			   concernCategoryModel = (ConcernCategoryModel)baseWrapper.getBasePersistableModel();
			   concernCategoryModel.setActive(false);
	           concernCategoryModel.setName(concernCategoryModel.getName()+"_"+nowDate.getTime());
	           concernCategoryModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
	           concernCategoryModel.setUpdatesOn(nowDate);
	           concernCategoryModel.setDescription("Deleted");
	           concernCategoryModel.setComments("Deleted");
			   baseWrapper.setBasePersistableModel(concernCategoryModel);
			   baseWrapper = this.concernManager.updateConcernCategory(baseWrapper);

			   ajaxXmlBuilder.addItem("message", getMessage(request, "concern.category.delete.success", request.getLocale()));	
			   ajaxXmlBuilder.addItem("divsToDisable", ""+concernCategoryId);
			   
			   
			}
			catch(FrameworkCheckedException fce){
			   fce.printStackTrace();
			}
			

		
		return ajaxXmlBuilder.toString();
	}


	public void setConcernManager(ConcernManager concernManager) {
		this.concernManager = concernManager;
	}

}
