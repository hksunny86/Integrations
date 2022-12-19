package com.inov8.microbank.common.util;

import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.usergroupmodule.CustomUserPermissionViewModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

import static com.inov8.microbank.common.util.PortalConstants.*;


public class UserUtils
{

	private static Log logger	= LogFactory.getLog(UserUtils.class);

  public UserUtils()
  {
  }

  public static AppUserModel getCurrentUser()
  {
    SecurityContext ctx = SecurityContextHolder.getContext();
    Authentication authentication = ctx.getAuthentication();
    AppUserModel usr = null;

    if(authentication != null)
    {
    	//Start Changed by Basit Mehr as on 21092007 -- to avoid ClassCastException in jsp page produce when request come without user principal (e.g direct hit jsp pages)
//    	usr = (AppUserModel) authentication.getPrincipal();
    	Object principalObj = authentication.getPrincipal();
    	if(principalObj instanceof AppUserModel){
    		usr = (AppUserModel)principalObj;
    	}
    	//End Changed by Basit Mehr as on 21092007
    }
    else
    {
      usr = ThreadLocalAppUser.getAppUserModel();
        if( usr == null )
        {
            logger.info("UserUtils.getCurrentUser() - User not found. Setting AppUserId 1L");
            AppUserModel user = new AppUserModel();
            user.setPrimaryKey(1l);
            user.setAppUserTypeId(1l);
            return user ;
        }
      // --------------------------------------------------------------------------------------------
      /**
       * @todo Following code is added to run the TEST CASES
       */

      // --------------------------------------------------------------------------------------------
    }

    return usr;
  }
  
  public static String getImage()
  {
	  String img = null;
	  AppUserModel appUser = UserUtils.getCurrentUser();
		Long userType = appUser.getAppUserTypeId();
		if(UserTypeConstantsInterface.BANK.equals(userType))
		{
			img = appUser.getBankUserIdBankUserModel().getBankIdBankModel().getImagePath();
		}
		else if(UserTypeConstantsInterface.MNO.equals(userType))
		{
			img = appUser.getMnoUserIdMnoUserModel().getMnoIdMnoModel().getImagePath();
		}	
		else if(UserTypeConstantsInterface.SUPPLIER.equals(userType))
		{
			img = appUser.getSupplierUserIdSupplierUserModel().getSupplierIdSupplierModel().getImagePath();
		}	
		else if(UserTypeConstantsInterface.INOV8.equals(userType))
		{
			img = appUser.getOperatorUserIdOperatorUserModel().getOperatorIdOperatorModel().getImagePath();
		}	
	return img;
  }
  
  
  public static boolean isAccessAllowed(Long action, AppUserModel appUserModel)
  throws AccessDeniedException
  {
	  	if(null == appUserModel)
	  		appUserModel = getCurrentUser();

	  	//To provide backward compatibility we are using following code for othar than bank partners
	  	boolean isAccessLevelAllowedToAction = false;	  	
	  	Long appUserTypeId = appUserModel.getAppUserTypeId();
	  	if(appUserTypeId.compareTo(UserTypeConstantsInterface.BANK) != 0)
	  	{			  		
	    	  Collection<CustomUserPermissionViewModel> userPermissionList = appUserModel.getUserPermissionList();  	    	  			
	    	  	    	  
				if(isInPermissionList(userPermissionList,AccessLevelConstants.CSR_GP_SUPER_USER) 
						|| isInPermissionList(userPermissionList,AccessLevelConstants.ADMIN_GP_USER) 
						|| isInPermissionList(userPermissionList,AccessLevelConstants.MNO_GP_SUPER_USER)
						|| isInPermissionList(userPermissionList,AccessLevelConstants.PAS_GP_SUPER_USER)
						|| isInPermissionList(userPermissionList,AccessLevelConstants.RET_GP_SUPER_USER)
						|| isInPermissionList(userPermissionList,AccessLevelConstants.PRS_GP_SUPER_USER))
				{
					isAccessLevelAllowedToAction = true;
				}
				else
					if( (isInPermissionList(userPermissionList,AccessLevelConstants.CSR_GP_NORMAL_USER)
							|| isInPermissionList(userPermissionList,AccessLevelConstants.MNO_GP_NORMAL_USER)
							|| isInPermissionList(userPermissionList,AccessLevelConstants.PAS_GP_NORMAL_USER)
							|| isInPermissionList(userPermissionList,AccessLevelConstants.PRS_GP_NORMAL_USER)
							|| isInPermissionList(userPermissionList,AccessLevelConstants.RET_GP_NORMAL_USER)
							|| isInPermissionList(userPermissionList,AccessLevelConstants.DISTRIBUTOR_MGMT)
							) 
							&& (ACTION_DEFAULT.equals(action) || ACTION_UPDATE.equals(action)))// || ACTION_RETRIEVE.equals(action)))
					{
						isAccessLevelAllowedToAction = true;
					}
					else
						if( (isInPermissionList(userPermissionList,AccessLevelConstants.CSR_GP_VIEWONLY_USER)
								|| isInPermissionList(userPermissionList,AccessLevelConstants.MNO_GP_VIEWONLY_USER)
								|| isInPermissionList(userPermissionList,AccessLevelConstants.PAS_GP_VIEWONLY_USER)
								|| isInPermissionList(userPermissionList,AccessLevelConstants.PRS_GP_VIEWONLY_USER)
								|| isInPermissionList(userPermissionList,AccessLevelConstants.RET_GP_VIEWONLY_USER)
								) 
								&& (ACTION_RETRIEVE.equals(action)))
						{
							isAccessLevelAllowedToAction = true;
						}	    			
	  	}//if not bank check ends
	  	else
	  	{
	  		return true;
	  	}
		return isAccessLevelAllowedToAction;
  }
  
  public static boolean isInPermissionList(Collection<CustomUserPermissionViewModel> customUserPermissionList, String requestedPermission) {
	
	  for(CustomUserPermissionViewModel permission : customUserPermissionList)
	  {
		  if(permission.getAuthority().equalsIgnoreCase(requestedPermission))
		  {
			  return true;
		  }
	  }
	return false;
}


//public static AppUserAppRoleModel determineCurrentAppUserAppRoleModel (AppUserModel appUserModel)
//  {//TODO
//	  	if(null == appUserModel)
//	  		appUserModel = getCurrentUser();
//	  	AppRoleModel appRoleModel = null; 
//		if (null!=appUserModel.getAppUserAppRoleModels())
//		{
//			Iterator<AppUserAppRoleModel> iter = appUserModel.getAppUserAppRoleModels().iterator();
//			while(iter.hasNext())
//			{
//				AppUserAppRoleModel appUserAppRoleModel = iter.next();
//				appRoleModel = appUserAppRoleModel.getAppRoleModel();
//				Long roleId = appRoleModel.getAppRoleId();
//				Long userType = appUserModel.getAppUserTypeId();
//
//				if(UserTypeConstantsInterface.BANK.equals(userType)&& (PAYMENT_GATEWAY.equals(roleId)))
//				{
//					return appUserAppRoleModel;
//				}
//				else if(UserTypeConstantsInterface.MNO.equals(userType)&& (MNO.equals(roleId)))
//				{
//					return appUserAppRoleModel;
//				}	
//				else if(UserTypeConstantsInterface.SUPPLIER.equals(userType)
//						&& (PRODUCT_SUPPLIER.equals(roleId)||PAYMENT_SERVICE.equals(roleId)) )
//				{
//					return appUserAppRoleModel;
//				}	
//				else if(UserTypeConstantsInterface.INOV8.equals(userType) && (CSR.equals(roleId)))
//				{
//					return appUserAppRoleModel;
//				}
//				else if(UserTypeConstantsInterface.INOV8.equals(userType) && (ADMIN.equals(roleId)))
//				{
//					return appUserAppRoleModel;
//				}
//				else if(UserTypeConstantsInterface.CUSTOMER.equals(userType) && (CUSTOMER.equals(roleId)))
//				{
//					return appUserAppRoleModel;
//				}
//			}
//		}
//		return null;
//  }


  /*
   * Previously used by ActiavateDeactivateManagerImpl. not in use now 
	public static Converter getAppUserConverter()
	{
		return new AppUserConverter();
	}

  private static class AppUserConverter implements Converter
  {

	public AppUserConverter()
	{
		super();
	}

	public Object convert(Class clazz, Object obj)
	{
		System.out.println("-------- CLASS ------- "+ obj.getClass());
		if(obj instanceof AppUserModel)
			return obj;
		else
			throw new ConversionException("Unable to convert obj to AppUserModel [argument type mismatch]");
	}

  }
*/
 
  /* For testing  
  public static void main(String []args)
  {
	Long []actions = {ACTION_CREATE, ACTION_DEFAULT, ACTION_DELETE, ACTION_RETRIEVE, ACTION_UPDATE, ACTION_UPDATE_ADMIN};
	AppUserModel aUserModel = new AppUserModel();
	AppRoleModel roleModel = new AppRoleModel();
	AccessLevelModel aLevelModel = new AccessLevelModel();

	aUserModel.setAppUserTypeId(UserTypeConstantsInterface.BANK);
	roleModel.setAppRoleId(PAYMENT_GATEWAY);
	aLevelModel.setAccessLevelId(ACCESS_LEVEL_NORMAL_USER);
	
	AppUserAppRoleModel aUserAppRoleModel = new AppUserAppRoleModel(aLevelModel, roleModel, aUserModel);
	System.out.println("Run Time: " + PortalDateUtils.currentFormattedDate("HH:mm:ss"));
	for (Long action : actions)
	{
		System.out.println("\nAction: [" + getActionName(action)
				+ "]\nAllowed: [" + isAccessAllowed(action, aUserModel)
				+"]");
	}
  }
*/

  public static String getActionName(Long action)
  {
	  if(ACTION_CREATE.equals(action))
		  return "CREATE";
	  if(ACTION_DELETE.equals(action))
		  return "DELETE";
	  if(ACTION_UPDATE.equals(action))
		  return "UPDATE";
	  if(ACTION_RETRIEVE.equals(action))
		  return "VIEW";
	  if(ACTION_UPDATE_ADMIN.equals(action))
		  return "UPDATE ADMIN";
	  if(ACTION_DEFAULT.equals(action))
		  return "DEFAULT";
	  else
		  return "INVALID";
  }

	public static String getClientIpAddress(HttpServletRequest request){

		String remoteAddr = "";

		if (request != null) {
			remoteAddr = request.getHeader("X-FORWARDED-FOR");
			if (StringUtil.isNullOrEmpty(remoteAddr)) {
				remoteAddr = request.getRemoteAddr();
			}
		}
		return remoteAddr;
	}

	public static Cookie prepareCookieUserType(String loggedInUserType)
	{
		if(loggedInUserType == null || "".equals(loggedInUserType))
			loggedInUserType="6";

		Cookie cookie = new Cookie("loggedInUserType", loggedInUserType);
		cookie.setMaxAge(-1);
		//cookie.setPath("/");
		cookie.setComment(loggedInUserType == "3" ? "AgentWeb" : "Microbank");

		return cookie;
	}

	public static String getCookieValue(String name, HttpServletRequest httpServletRequest)
	{
		Cookie[] cookies = httpServletRequest.getCookies();
		String value="";
		for(Cookie cookie : cookies) {
			if(cookie.getName() != null && cookie.getName().equalsIgnoreCase(name)) {
				value = cookie.getValue();
				break;
			}
		}

		return value;
	}

}
