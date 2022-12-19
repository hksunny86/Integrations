package com.inov8.microbank.common.model.usergroupmodule;

import org.springframework.security.core.GrantedAuthority;


public class CustomUserPermissionViewModel implements GrantedAuthority {

	UserPermissionViewModel userPermissionViewModel;
	
	public CustomUserPermissionViewModel()
	{		
	}
	public CustomUserPermissionViewModel(UserPermissionViewModel userPermissionViewModel)
	{
		this.userPermissionViewModel = userPermissionViewModel;
	}
//    @javax.persistence.Transient
    public String getAuthority()
	{
    	String authority = "";
    	if(this.userPermissionViewModel != null)
    	{
    		authority = this.userPermissionViewModel.getActionPermissionShortName();
    	}
	    return authority;
	}
}
