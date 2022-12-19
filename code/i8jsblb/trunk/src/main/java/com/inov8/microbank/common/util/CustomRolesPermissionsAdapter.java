package com.inov8.microbank.common.util;

import javax.servlet.http.HttpServletRequest;

import net.sf.navigator.menu.MenuComponent;
import net.sf.navigator.menu.PermissionsAdapter;

public class CustomRolesPermissionsAdapter implements PermissionsAdapter {
	
	private HttpServletRequest request;
	public CustomRolesPermissionsAdapter(HttpServletRequest request)
	{			
		this.request = request;
	}
	
	public boolean isAllowed(MenuComponent menuComponent)
	{
		boolean accessAllowed=false;
		if (menuComponent.getRoles() == null) {
            accessAllowed =  true; // no roles define, allow everyone
        }         
		if(isInPermissionList(menuComponent.getRoles()))
		{
			accessAllowed = true;			
		}
		else
		{
			//role not found
			accessAllowed=false;
		}

		return accessAllowed;
	}
	private boolean isInPermissionList(String requestedPermission) 
	{
		String[] allowedRoles = requestedPermission.split(",");
		  for(String role : allowedRoles)
		  {
			  if (request.isUserInRole(role))
			  {				  
				  return true;
			  }
			  
		  }
		return false;
	}	
	
}
