package com.inov8.microbank.common.model;

import java.io.Serializable;

public class ChangePasswordListViewFormModel implements Serializable  {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4325286132584363810L;
	private String oldPassword;
	private String newPassword;
	private String confirmPassword;
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	
}
