/**
 * 
 */
package com.inov8.microbank.server.webservice.bean;

/**
 * @author Wateen
 *
 */
public class USSDOutputDTO {
	private String menu;
	private boolean inputRequired;
	
	public String getMenu() {
		return menu;
	}
	public void setMenu(String menu) {
		this.menu = menu;
	}
	public boolean isInputRequired() {
		return inputRequired;
	}
	public void setInputRequired(boolean inputRequired) {
		this.inputRequired = inputRequired;
	}
}
