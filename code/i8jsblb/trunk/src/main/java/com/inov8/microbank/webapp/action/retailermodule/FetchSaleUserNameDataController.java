package com.inov8.microbank.webapp.action.retailermodule;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.ServletRequestUtils;

import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.agenthierarchy.SalesHierarchyModel;
import com.inov8.microbank.server.dao.agenthierarchymodule.SalesHierarchyDAO;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.webapp.action.ajax.AjaxController;

public class FetchSaleUserNameDataController extends AjaxController {

	private static Log logger = LogFactory.getLog(FetchSaleUserNameDataController.class);
	private AppUserManager 		securityFacade;
	private SalesHierarchyDAO	salesHierarchyDAO;
	

	/* (non-Javadoc)
	 * @see com.inov8.microbank.webapp.action.ajax.AjaxController#getResponseContent(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String empId = ServletRequestUtils.getRequiredStringParameter(request, "empId");
		String errMsg = "";
		String saleUserName = "";
		String empAppUserId=null;
		AppUserModel appUserModel = new AppUserModel();
		appUserModel.setEmployeeId(Long.parseLong(empId));
		appUserModel = securityFacade.getAppUserModel(appUserModel);
		SalesHierarchyModel salesHierarchyModel = new SalesHierarchyModel();
		salesHierarchyModel = salesHierarchyDAO.findSaleUserByBankUserId(appUserModel.getAppUserId());
		if(null!=salesHierarchyModel){
			if(null != appUserModel){
				empAppUserId = appUserModel.getAppUserId().toString();
				saleUserName = appUserModel.getFirstName() +  " " + appUserModel.getLastName();
				errMsg = "null";
			}else{
				saleUserName = "null";
				errMsg = "User is not associated with any sale hierarchy";
			}
		}else{
			errMsg = "User is not associated with any sale hierarchy";
		}
		
		AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
		ajaxXmlBuilder.addItem("empName", saleUserName);
		ajaxXmlBuilder.addItem("empAppUserId", empAppUserId);
		ajaxXmlBuilder.addItem("errMsg", errMsg);
		return ajaxXmlBuilder.toString();
	}

	public void setSecurityFacade(AppUserManager securityFacade) {
		if (securityFacade != null) {
			this.securityFacade = securityFacade;
		}
	}

	public void setSalesHierarchyDAO(SalesHierarchyDAO salesHierarchyDAO) {
		if (salesHierarchyDAO != null) {
			this.salesHierarchyDAO = salesHierarchyDAO;
		}
	}
}
