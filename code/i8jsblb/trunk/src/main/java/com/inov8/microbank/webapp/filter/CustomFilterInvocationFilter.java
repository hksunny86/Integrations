package com.inov8.microbank.webapp.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import com.inov8.microbank.common.model.usergroupmodule.CustomUserPermissionViewModel;
import com.inov8.microbank.common.util.UserUtils;

public class CustomFilterInvocationFilter extends FilterSecurityInterceptor{
	
	CustomFilterInvocationFilter(){
		
	}
	/*
	 * This method is used to provide CRUD level security to URL requests of user.
	 * 
	 * @see org.acegisecurity.intercept.web.FilterSecurityInterceptor#invoke(org.acegisecurity.intercept.web.FilterInvocation)
	 */
	public void invoke(FilterInvocation fi) throws IOException, ServletException{

		SecurityMetadataSource objDefSource = super.obtainSecurityMetadataSource();						
		Collection<CustomUserPermissionViewModel> userPermissions = null;
		boolean isAllowed = false;
		
		if(UserUtils.getCurrentUser() != null) {
			
			userPermissions = UserUtils.getCurrentUser().getUserPermissionList();
			String isUpdate = ((HttpServletRequest)fi.getRequest()).getParameter("isUpdate");
			boolean currentURLhasCreateOrUpdatePermission = false;			
			Collection<ConfigAttribute> requestedURLObjDefinition = objDefSource.getAttributes(fi);			
			
			if(requestedURLObjDefinition != null) {						
				Iterator<ConfigAttribute> iterAttributes = requestedURLObjDefinition.iterator();				
				
				while(iterAttributes.hasNext()) {
					ConfigAttribute configAttr = iterAttributes.next();
	
					if(isUpdate != null && "true".equalsIgnoreCase(isUpdate)) {
						currentURLhasCreateOrUpdatePermission = true;
						//it shows that current request if of type Update Record
						if(configAttr.getAttribute().endsWith("_U")) {									
							//match with userPermissions
							if(isInPermissionList(userPermissions, configAttr.getAttribute())) {							
								//shows permission found so proceed.
								isAllowed = true;
								break;
							}
						}
					}//if isUpdate true ends
					else {
	//						it shows that current request if not of type Update Record
						//TODO: we need to change isUpdate hidden field value to C, R, U, D and alway check it for 
						//appropriate operation, if its not present then deny to user, also change framework accordingly.
						
						//Check if current URL contains Create Permissions then match it with userPermissionList 
						//and if not found then deny and if there is no Create permission associated with current URL then leave any checks.
						if(configAttr.getAttribute().endsWith("_C")) {	
							currentURLhasCreateOrUpdatePermission = true;
							//match with userPermissions
							if(isInPermissionList(userPermissions, configAttr.getAttribute())) {							
								//shows permission found so proceed.
								isAllowed = true;
								break;
							}
						}					
					}
				}
			}//null check ends
			if(!currentURLhasCreateOrUpdatePermission) {
				//it shows current URL has Read permission, so let Acegi decide its rights.
				isAllowed = true;
			}
		}
		else {
//			anonymous user, so proceed normally, let Acegi filter to work
			isAllowed = true;
		}
		
		if(isAllowed) {
			super.invoke(fi);
		}
		else {
			//forward to access denied page.
			throw new AccessDeniedException("Access to the required resource is not granted");
		}
	}
	
	private static boolean isInPermissionList(Collection<CustomUserPermissionViewModel> customUserPermissionList, String requestedPermission) {
		
		  for(CustomUserPermissionViewModel permission : customUserPermissionList)
		  {
			  if(permission.getAuthority().equalsIgnoreCase(requestedPermission))
			  {
				  return true;
			  }
		  }
		return false;
	}
}
