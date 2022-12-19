package com.inov8.microbank.server.service.securitymodule;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;

import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.usergroupmodule.CustomUserPermissionViewModel;
import com.inov8.microbank.common.util.AccessLevelConstants;
import com.inov8.microbank.common.util.PortalConstants;

public class UserSecurityAdvice
    implements MethodBeforeAdvice, AfterReturningAdvice
{
  public final static String ACCESS_DENIED =
      "Access Denied: Only administrators are allowed to modify other users.";
  protected final Log log = LogFactory.getLog(UserSecurityAdvice.class);
  private UserCache userCache;

  public void setUserCache(UserCache userCache)
  {
    this.userCache = userCache;
  }

  /**
   * Method to enforce security and only allow administrators to modify users. Regular
   * users are allowed to modify themselves.
   */
  public void before(Method method, Object[] args, Object target) throws
      Throwable
  {
    SecurityContext ctx = SecurityContextHolder.getContext();

    if (ctx.getAuthentication() != null)
    {
      Authentication auth = ctx.getAuthentication();
      boolean administrator = false;
      for (GrantedAuthority role :auth.getAuthorities())
      {
        if (AccessLevelConstants.ADMIN_GP_USER.equals(role.getAuthority()) ||
        		PortalConstants.ADM_USR_MGMT_CREATE.equals(role.getAuthority()) ||
        		PortalConstants.ADM_USR_MGMT_UPDATE.equals(role.getAuthority()) ||
        		AccessLevelConstants.MNO_GP_SUPER_USER.equals(role.getAuthority())
        )
        {
          administrator = true;
          break;
        }
      }

      AppUserModel user = (AppUserModel) args[0];
      String username = user.getUsername();

      String currentUser;
      if (auth.getPrincipal() instanceof UserDetails)
      {
        currentUser = ( (UserDetails) auth.getPrincipal()).getUsername();
      }
      else
      {
        currentUser = String.valueOf(auth.getPrincipal());
      }

      if (username != null && !username.equals(currentUser))
      {
        AuthenticationTrustResolver resolver = new
            AuthenticationTrustResolverImpl();
        // allow new users to signup - this is OK b/c Signup doesn't allow setting of roles
        boolean signupUser = resolver.isAnonymous(auth);
        if (!signupUser)
        {
          if (log.isDebugEnabled())
          {
            log.debug("Verifying that '" + currentUser + "' can modify '" +
                      username + "'");
          }
          if (!administrator)
          {
            log.warn("Access Denied: '" + currentUser + "' tried to modify '" +
                     username + "'!");
            throw new AccessDeniedException(ACCESS_DENIED);
          }
        }
        else
        {
          if (log.isDebugEnabled())
          {
            log.debug("Registering new user '" + username + "'");
          }
        }
      }

      // fix for http://issues.appfuse.org/browse/APF-96
      // don't allow users with "user" role to upgrade to "admin" role
      else if (username != null && username.equalsIgnoreCase(currentUser) &&
               !administrator)
      {

        // get the list of roles the user is trying add
        Set<String> userRoles = new HashSet<>();

        if(user.getUserPermissionList() != null)
        {
        	for(CustomUserPermissionViewModel permission : user.getUserPermissionList())
	      	  {
        		userRoles.add(permission.getAuthority());
	      	  }
        }

        // get the list of roles the user currently has
        Set<String> authorizedRoles = new HashSet<>();
        for ( GrantedAuthority grantedAuthority : auth.getAuthorities() )
        {
          authorizedRoles.add(grantedAuthority.getAuthority());
        }

        // if they don't match - access denied
        // users aren't allowed to change their roles
        if (!CollectionUtils.isEqualCollection(userRoles, authorizedRoles))
        {
          log.warn("Access Denied: '" + currentUser +
                   "' tried to change their role(s)!");
          throw new AccessDeniedException(ACCESS_DENIED);
        }
      }
    }
  }

  public void afterReturning(Object returnValue, Method method, Object[] args,
                             Object target) throws Throwable
  {
    AppUserModel user = (AppUserModel) args[0];

    if (userCache != null && user.getVersionNo() != null)
    {
      if (log.isDebugEnabled())
      {
        log.debug("Removing '" + user.getUsername() + "' from userCache");
      }

      userCache.removeUserFromCache(user.getUsername());

      // reset the authentication object if current user
      Authentication auth = SecurityContextHolder.getContext().
          getAuthentication();
      if (auth != null && auth.getPrincipal() instanceof UserDetails)
      {
        AppUserModel currentUser = (AppUserModel) auth.getPrincipal();
        if (currentUser.getAppUserId().equals(user.getAppUserId()))
        {
          auth = new UsernamePasswordAuthenticationToken(user, user.getPassword(),
              user.getAuthorities());
          SecurityContextHolder.getContext().setAuthentication(auth);
        }
      }
    }
  }
}
