package com.inov8.microbank.webapp.action.changepasswordmodule;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.ExceptionErrorCodes;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.ChangePasswordListViewFormModel;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PasswordConfigUtil;
import com.inov8.microbank.common.util.PasswordInputDTO;
import com.inov8.microbank.common.util.PasswordResultDTO;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.changepasswordmodule.ChangePasswordManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;

public class AwChangePasswordFormController extends AdvanceFormController{
 
	private AppUserManager appUserManager ;
	private ChangePasswordManager changePasswordManager ;

	@Value("classpath:dictionary")
	private Resource resource;
	
	public void setChangePasswordManager(ChangePasswordManager changePasswordManager) {
		this.changePasswordManager = changePasswordManager;
	}
	
	public AwChangePasswordFormController() {
		setCommandName("changePasswordListViewFormModel");
		setCommandClass(ChangePasswordListViewFormModel.class);
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest arg0) throws Exception {
		// TODO Auto-generated method stub
		return new ChangePasswordListViewFormModel();
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest arg0) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object, BindException bindException) throws Exception {
		return this.createOrUpdate(httpServletRequest, httpServletResponse,
				object, bindException);
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object, BindException bindException) throws Exception {
		return this.createOrUpdate(httpServletRequest, httpServletResponse,
				object, bindException);
	}
	
	
	private ModelAndView createOrUpdate(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		
		try {
			BaseWrapper baseWrapper = new BaseWrapperImpl();

			ChangePasswordListViewFormModel changePasswordListViewFormModel = (ChangePasswordListViewFormModel) command;
			PasswordInputDTO passwordInputDTO = new PasswordInputDTO( );
			PasswordResultDTO passwordResultDTO = new PasswordResultDTO( );
			passwordInputDTO.setPassword(changePasswordListViewFormModel.getNewPassword());
			passwordInputDTO.setUserName(UserUtils.getCurrentUser().getUsername());
			passwordInputDTO.setFirstName(UserUtils.getCurrentUser().getFirstName());
			passwordInputDTO.setLastName(UserUtils.getCurrentUser().getLastName());
			//check whether password is of minimum 8 characters and combinations of digits, special characters and alphabets.
			passwordResultDTO = PasswordConfigUtil.isValidPasswordStrength(passwordInputDTO);
			if ( !passwordResultDTO.getIsValid() ){ 
				throw new FrameworkCheckedException("InvalidStrenghtOfPasswordException");
			}
			 //check whether supplied password is same as of user ID
			passwordResultDTO = PasswordConfigUtil.isPasswordContainsUserIDOrName(passwordInputDTO);
			if (!passwordResultDTO.getIsValid()){
				throw new FrameworkCheckedException("UserIdCantBePasswordException");
			}
			//check whether supplied password is common dictionary words
			/*passwordResultDTO = PasswordConfigUtil.isPasswordContainsDictionaryWord(passwordInputDTO, resource.getFile()); 
			if ( !passwordResultDTO.getIsValid() ){
				throw new FrameworkCheckedException ( "DictionaryWordAsPasswordException" );
			}
			//validate alpha sequence rule
			passwordResultDTO = PasswordConfigUtil.validateAlphaSequenceRule(passwordInputDTO);
			if ( !passwordResultDTO.getIsValid() ){
				throw new FrameworkCheckedException ( "SequencePasswordException" );
			}
			//validate numeric rule
			passwordResultDTO = PasswordConfigUtil.validateNumericSequenceRule(passwordInputDTO);
			if ( !passwordResultDTO.getIsValid() ){
				throw new FrameworkCheckedException ( "SequencePasswordException" );
			}*/
			
			//validate last three history password
			List<String> passwordList = appUserManager.getAppUserPreviousThreePasswordsByAppUserId( UserUtils.getCurrentUser().getUsername() );
			passwordInputDTO.setHistoryPasswords(passwordList);
			passwordResultDTO = PasswordConfigUtil.isPassowrdContainsHistoryPassword(passwordInputDTO);
					if ( !passwordResultDTO.getIsValid()){
						throw new FrameworkCheckedException ( "PasswordMatchLastThreeException" );
					}

			baseWrapper.putObject("changePasswordListViewFormModel", changePasswordListViewFormModel);

			//Unsetting the change password required flag.
			/*BaseWrapper appUserBaseWrapper = new BaseWrapperImpl();
			AppUserModel appUserModel = appUserManager.getUser(UserUtils.getCurrentUser().getAppUserId().toString());													
			appUserModel.setPasswordChangeRequired(false);
			appUserModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			appUserModel.setUpdatedOn(new Date());
			appUserBaseWrapper.setBasePersistableModel(appUserModel);
			appUserManager.saveOrUpdateAppUser(appUserBaseWrapper);*/
			
			baseWrapper = this.changePasswordManager.savePassword(
					baseWrapper);
			
			
			this.saveMessage(request, "Password changed successfully");
			ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
			return modelAndView;

		} catch (FrameworkCheckedException ex) {
			if( ex.getMessage().equalsIgnoreCase("IncorrectOldPasswordException") )
			{
				super.saveMessage(request, "Old password does not match with current password");
				return super.showForm(request, response, errors);		
				
			}			
			else if( ex.getMessage().equalsIgnoreCase("NewandConfirmPasswordException") )
			{
				super.saveMessage(request, "Password and Confirm Password does not match");
				return super.showForm(request, response, errors);				
			}
			else if ( ex.getMessage().equalsIgnoreCase("InvalidStrenghtOfPasswordException") ){
				super.saveMessage(request, "New password does not match the password policy i.e, minimum 8 characters including digits, alphabets and special characters");
				return super.showForm(request, response, errors);
			}
			else if ( ex.getMessage( ).equalsIgnoreCase("UserIdCantBePasswordException") ){
				super.saveMessage(request, "User ID/Name can not be used as password");
				return super.showForm(request, response, errors);
			}
			else if ( ex.getMessage().equalsIgnoreCase("DictionaryWordAsPasswordException") ){
				super.saveMessage(request, "Password should not contain common english dictionary words");
				return super.showForm(request, response, errors);
			}
			else if ( ex.getMessage().equalsIgnoreCase("SequencePasswordException") ){
				super.saveMessage(request, "Password should not be in a predictable sequence");
				return super.showForm(request, response, errors);
			} 
			else if ( ex.getMessage().equalsIgnoreCase("PasswordMatchLastThreeException") ){
				super.saveMessage(request, "New password can not be from last five passwords");
				return super.showForm(request, response, errors);
			}
			else if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION == ex.getErrorCode()) {
				super.saveMessage(request, "Password could not be saved.");
				return super.showForm(request, response, errors);
			}
			throw ex;
		} catch (Exception ex) {
			
			super.saveMessage(request, MessageUtil.getMessage("6075"));
			return super.showForm(request, response, errors);
		}
	}
	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}	

	
}
