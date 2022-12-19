package com.inov8.microbank.webapp.action.portal;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.webapp.action.ajax.AjaxController;
import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class checkPendingWalkinTransactionsAjaxController extends AjaxController {
	
	private AppUserManager appUserManager ;


	@Override
	public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Boolean  retagMobile = ServletRequestUtils.getRequiredBooleanParameter(request,"retagMobile");
		String 	newMobileNo= ServletRequestUtils.getStringParameter(request, "newMobileNo");
		String	newCnic= ServletRequestUtils.getStringParameter(request, "newCnic");
		Long 	appUserId = ServletRequestUtils.getLongParameter(request,"appUserId");
		AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.putObject("mobileNo", newMobileNo);
		baseWrapper.putObject("cnic", newCnic);
		baseWrapper.putObject("appUserId", appUserId);
		
		Boolean customerExits = !appUserManager.isMobileNumberCNICUnique(baseWrapper);
		Boolean pendingTrxExists = false;
		Boolean walkinExists = false;
		Boolean checkCnic = false;
		
	/*	if(!customerExits){
			List<TransactionDetailMasterModel> pendingTransList= appUserManager.findWalInUserPendingTransactions(newMobileNo, newCnic);
			if(null!=pendingTransList && pendingTransList.size()>0){
				pendingTrxExists = true;
				walkinExists =true;
			}
			else
			{
				walkinExists = !appUserManager.isWalkinMobileNumberCNICUnique(baseWrapper);
				if(walkinExists){
					if(newCnic!=null && !"".equals(newCnic)){
						AppUserModel appUserModel=new AppUserModel();
						appUserModel.setNic(newCnic);
					AppUserModel cnicExists=	appUserManager.getAppUserModel(appUserModel);
								if(cnicExists!=null){
									checkCnic=true;

								}
					}
				}
			} 
				
		}*/
		

		ajaxXmlBuilder.addItem("customerExits", customerExits.toString());
		ajaxXmlBuilder.addItem("pendingTrxExists", pendingTrxExists.toString());
		ajaxXmlBuilder.addItem("walkinExists", walkinExists.toString());
		ajaxXmlBuilder.addItem("isCnicUpdate", checkCnic.toString());
		ajaxXmlBuilder.addItem("walkinExists", walkinExists.toString());


		return ajaxXmlBuilder.toString();
		
	}


	public AppUserManager getAppUserManager() {
		return appUserManager;
	}


	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}

}
