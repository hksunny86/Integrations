package com.inov8.microbank.common.util;

import java.util.ArrayList;
import java.util.List;

public class PasswordResultDTO  {
	
	private Boolean 					isValid			 = false;
	private List<String>			    errorMessages	 = new ArrayList<String>( );
	
	
	public Boolean getIsValid() {
		return isValid;
	}
	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}
	public List<String> getErrorMessages() {
		return errorMessages;
	}
	public void setErrorMessages(List<String> errorMessages) {
		this.errorMessages = errorMessages;
	}
	
	

}
