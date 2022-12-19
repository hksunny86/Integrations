package com.inov8.microbank.webapp.action.portal.changeaccnickmodule;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.portal.changeaccnickmodule.ChangeAccountNickListViewModel;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.server.service.portal.linkpaymentmodemodule.LinkPaymentModeManager;

public class PgUpdateAccountNickController extends AdvanceFormController{
	private LinkPaymentModeManager	linkPaymentModeManager;

	  /**
	 * @param linkPaymentModeManager the linkPaymentModeManager to set
	 */
	public void setLinkPaymentModeManager(
			LinkPaymentModeManager linkPaymentModeManager) {
		this.linkPaymentModeManager = linkPaymentModeManager;
	}

	public PgUpdateAccountNickController()
	  {
	    setCommandName("changeAccountNickListViewModel");
	    setCommandClass(ChangeAccountNickListViewModel.class);
	  }	
	
	@Override
	protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws Exception {
		// TODO Auto-generated method stub
		
		Long smartMoneyAccountId = new Long(EncryptionUtil.decryptWithDES(ServletRequestUtils.getStringParameter(httpServletRequest,"smartMoneyAccountId")));
		Long appUserId = new Long(EncryptionUtil.decryptWithDES(ServletRequestUtils.getStringParameter(httpServletRequest,"appUserId"))); 
		SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
				
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.putObject("smartMoneyAccountId", smartMoneyAccountId);
		baseWrapper.putObject("appUserId", appUserId);
		baseWrapper = linkPaymentModeManager.getSmartMoneyAccountInfo(baseWrapper);
				
		return baseWrapper.getBasePersistableModel();
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object, BindException bindException) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object, BindException bindException) throws Exception {
		// TODO Auto-generated method stub
		
		ChangeAccountNickListViewModel changeAccountNickListViewModel = (ChangeAccountNickListViewModel)object;
		String newAccountNick = ServletRequestUtils.getStringParameter(httpServletRequest, "newAccountNick");
		String enAppUserId = ServletRequestUtils.getStringParameter(httpServletRequest, "appUserId");

		try{
			
			changeAccountNickListViewModel.setSmartMoneyAccountId(new Long(EncryptionUtil.decryptWithDES(ServletRequestUtils.getStringParameter(httpServletRequest,"smartMoneyAccountId"))));
			changeAccountNickListViewModel.setAppUserId(new Long(EncryptionUtil.decryptWithDES(enAppUserId)));
			
			BaseWrapper baseWrapper = new BaseWrapperImpl();	
			baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.CHANGE_ACCOUNT_NICK_USECASE_ID);
			baseWrapper.putObject("newAccountNick", newAccountNick);
			
			baseWrapper.putObject("changeAccountNickListViewModel", changeAccountNickListViewModel);
			
			baseWrapper = linkPaymentModeManager.changeAccountNick(baseWrapper);
		
		}catch(FrameworkCheckedException exception){
					String msg = exception.getMessage();
					String errorMsg = "";
					if(msg.indexOf("already exists") != -1){
						errorMsg = msg;
					}
					else if(msg == "implementationNotSupportedException"){
						errorMsg = getText("linkpaymentmodemodule.featureNotSupported", httpServletRequest.getLocale());
					}
					else{
						errorMsg = "Problem changing account nick.";
					}
			
					this.saveMessage(httpServletRequest, errorMsg);
					return super.showForm(httpServletRequest, httpServletResponse, bindException);
		}
		catch (Exception fce)
		{	
			fce.printStackTrace();
			this.saveMessage(httpServletRequest, MessageUtil.getMessage("6075"));
			return super.showForm(httpServletRequest, httpServletResponse, bindException);
		}
			
		this.saveMessage(httpServletRequest, "Account Nick changed.");
		return new ModelAndView(this.getSuccessView()+"&appUserId="+enAppUserId);
	}

}
