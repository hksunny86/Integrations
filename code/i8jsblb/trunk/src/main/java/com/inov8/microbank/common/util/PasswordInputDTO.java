package com.inov8.microbank.common.util;

import java.util.ArrayList;
import java.util.List;


public class PasswordInputDTO  {
	
	public 		String 		password 						= null;
	public		String		userName						= null;
	public		String		firstName						= null;
	public		String		lastName						= null;
	public		List<String>	historyPasswords			= new ArrayList<String>( );

	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public List<String> getHistoryPasswords() {
		return historyPasswords;
	}

	public void setHistoryPasswords(List<String> historyPasswords) {
		this.historyPasswords = historyPasswords;
	}
		
}
