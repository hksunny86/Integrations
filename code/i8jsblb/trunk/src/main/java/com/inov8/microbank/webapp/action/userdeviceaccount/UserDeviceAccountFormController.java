package com.inov8.microbank.webapp.action.userdeviceaccount;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.ExceptionErrorCodes;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.framework.webapp.action.ControllerUtils;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.DeviceTypeModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.userdeviceaccount.UserDeviceAccountListViewManager;


public class UserDeviceAccountFormController extends AdvanceFormController{

	
	private  UserDeviceAccountListViewManager userDeviceAccountListViewManager;
	private ReferenceDataManager referenceDataManager;
	private AppUserManager appUserManager;
	private Long id;
	
	public UserDeviceAccountFormController() {
		setCommandName("userDeviceAccountsModel");
		setCommandClass(UserDeviceAccountsModel.class);
	}
	
	@Override
	protected Object loadFormBackingObject(HttpServletRequest request) throws Exception {
		id = ServletRequestUtils.getLongParameter(request,"userDeviceAccountsId");
		
		if (null != id) 
		{
			if (log.isDebugEnabled()) 
			{
				log.debug("id is not null....retrieving object from DB");
			}

			BaseWrapper baseWrapper = new BaseWrapperImpl();
			UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
			userDeviceAccountsModel.setPrimaryKey(id);
			baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
			baseWrapper=this.userDeviceAccountListViewManager.loadUserDeviceAccount(baseWrapper);
						
			
				
			
			return (UserDeviceAccountsModel) baseWrapper.getBasePersistableModel();
			} 
		else 
		{
			if (log.isDebugEnabled()) 
			{
				log.debug("id is null....creating new instance of Model");
			}

				return new UserDeviceAccountsModel();
		}
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest arg0) throws Exception {
		
	    DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
	    ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
	    		deviceTypeModel, "name", SortingOrder.ASC);
	    referenceDataWrapper.setBasePersistableModel(deviceTypeModel);
	    referenceDataManager.getReferenceData(referenceDataWrapper);
	    List<DeviceTypeModel> deviceTypeModelList = null;
	    if (referenceDataWrapper.getReferenceDataList() != null)
	    {
	    	deviceTypeModelList = referenceDataWrapper.getReferenceDataList();
	    }

	    Map referenceDataMap = new HashMap();
	    referenceDataMap.put("deviceTypeModelList", deviceTypeModelList);
	    
	    /**
	     * code fragment to load reference data for ServiceModel
	     */
        
	    AppUserModel appUserModel = new AppUserModel();
	    
	    
	    referenceDataWrapper = new ReferenceDataWrapperImpl(
	    		appUserModel, "firstName", SortingOrder.ASC);
	    referenceDataWrapper.setBasePersistableModel(appUserModel);

	    try
	    {
	      referenceDataManager.getReferenceData(referenceDataWrapper);
	    }
	    catch (FrameworkCheckedException ex)
	    {
	    }
	    List<AppUserModel> appUserModelList = null;
	    if (referenceDataWrapper.getReferenceDataList() != null)
	    {
	    	appUserModelList = referenceDataWrapper.getReferenceDataList();
	    }
	    referenceDataMap.put("appUserModelList", appUserModelList);
        
	    
	    return referenceDataMap;

	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse response, Object object, BindException errors) throws Exception {
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
		baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.MFS_DEVICE_ACCOUNT_USECASE_ID);
		
		UserDeviceAccountsModel userDeviceAccountsModel = (UserDeviceAccountsModel)object;
				/*
		String randomPin=RandomUtils.generateRandom(4,false, true);
		userDeviceAccountsModel.setPin(EncoderUtils.encodeToSha(randomPin));
		*/

		// Commissioned Variable will remain false for new geinie account
		userDeviceAccountsModel.setCommissioned(false);
		userDeviceAccountsModel.setAccountExpired( userDeviceAccountsModel.getAccountExpired() == null ? false : userDeviceAccountsModel.getAccountExpired() );
		userDeviceAccountsModel.setAccountEnabled( userDeviceAccountsModel.getAccountEnabled() == null ? false : userDeviceAccountsModel.getAccountEnabled() );
		userDeviceAccountsModel.setAccountLocked( userDeviceAccountsModel.getAccountLocked() == null ? false : userDeviceAccountsModel.getAccountLocked() );
		userDeviceAccountsModel.setPinChangeRequired( userDeviceAccountsModel.getPinChangeRequired() == null ? false : userDeviceAccountsModel.getPinChangeRequired() );
		userDeviceAccountsModel.setCredentialsExpired( userDeviceAccountsModel.getCredentialsExpired() == null ? false : userDeviceAccountsModel.getCredentialsExpired() );		
		
		userDeviceAccountsModel.setCreatedOn(new Date());
		userDeviceAccountsModel.setUpdatedOn(new Date());
		userDeviceAccountsModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		userDeviceAccountsModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		
		
		baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
			
		try
		{
			this.userDeviceAccountListViewManager.createUserDeviceAccount(baseWrapper);
			ControllerUtils.saveMessage(request, "Record saved successfully");
		}
		catch (FrameworkCheckedException e)
		{
			if( e.getMessage().equalsIgnoreCase("UserIdUniqueException") )
			{
				super.saveMessage(request, "Device account with the same Customer ID already exists.");
				return super.showForm(request, response, errors);				
			}	
			else if( e.getMessage().equalsIgnoreCase("AppUserIdDeviceTypeIdCombinationRepeatException"))
			{
				super.saveMessage(request, "Device Account with same device type and app user already exists.");
				return super.showForm(request, response, errors);
			}	
					
			
			else if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION ==
		          e.getErrorCode())
		    {
		        super.saveMessage(request, "Record could not be saved.");
		        return super.showForm(request, response, errors);
		    }
			else{
				super.saveMessage(request, "Record could not be saved.");
				return super.showForm(request, response, errors);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			super.saveMessage(request, MessageUtil.getMessage("6075"));
			return super.showForm(request, response, errors);
		}
		
		/*AppUserModel appUser = new AppUserModel();
		appUser.setAppUserId( userDeviceAccountsModel.getAppUserId() ) ;
		baseWrapper.setBasePersistableModel( appUser ) ;		
		appUser = (AppUserModel)this.appUserManager.loadAppUser(baseWrapper).getBasePersistableModel() ;
		
		userDeviceAccountListViewManager.sendSMS(userDeviceAccountsModel.getUserId(), randomPin, appUser.getMobileNo()) ;
		*/
     
      ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
 		return modelAndView;
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object object, BindException errors) throws Exception 
	{
		if (log.isDebugEnabled())
	    {
	      log.debug("Execution of onToggleActivate started...");
	    }

	    Long id = Long.parseLong(request.getParameter("userDeviceAccountsId")); 
	    Boolean changepin = ServletRequestUtils.getBooleanParameter(request, "pinChangeRequired");
	    if (null != id)
	    {
	      if (log.isDebugEnabled())
	      {
	        log.debug(
	            "id is not null....retrieving object from DB and then updating it");
	      }
	        UserDeviceAccountsModel userDeviceAccountsModel = (UserDeviceAccountsModel)object;
	        
	        if( userDeviceAccountsModel.getDeviceTypeId() == DeviceTypeConstantsInterface.ALL_PAY && userDeviceAccountsModel.getUserId().length() > 12 )
	        {
	        	super.saveMessage(request, "Agent ID can not be greater than 12 characters.");
			    return super.showForm(request, response, errors);
	        }
	        
	        
	      	BaseWrapper baseWrapper = new BaseWrapperImpl();
	      	UserDeviceAccountsModel tempUserDeviceAccountsModel = new UserDeviceAccountsModel();
	      	tempUserDeviceAccountsModel.setPrimaryKey(id);
	      	baseWrapper.setBasePersistableModel(tempUserDeviceAccountsModel);
	      	baseWrapper=this.userDeviceAccountListViewManager.loadUserDeviceAccount(baseWrapper);
				      	
	      	tempUserDeviceAccountsModel=(UserDeviceAccountsModel)baseWrapper.getBasePersistableModel();
            if(!userDeviceAccountsModel.getDeviceTypeId().equals(tempUserDeviceAccountsModel.getDeviceTypeId()) ||
            		!userDeviceAccountsModel.getAppUserId().equals(tempUserDeviceAccountsModel.getAppUserId()) )
            	baseWrapper.putObject("isDeviceTypeChanged", true);
            else
            	baseWrapper.putObject("isDeviceTypeChanged", false);
            
            tempUserDeviceAccountsModel.setCommissioned( userDeviceAccountsModel.getCommissioned() == null ? false : userDeviceAccountsModel.getCommissioned() );            
            tempUserDeviceAccountsModel.setAccountExpired( userDeviceAccountsModel.getAccountExpired() == null ? false : userDeviceAccountsModel.getAccountExpired() );
	      	tempUserDeviceAccountsModel.setAccountEnabled( userDeviceAccountsModel.getAccountEnabled() == null ? false : userDeviceAccountsModel.getAccountEnabled() );
	      	tempUserDeviceAccountsModel.setAccountLocked( userDeviceAccountsModel.getAccountLocked() == null ? false : userDeviceAccountsModel.getAccountLocked() );
	      	tempUserDeviceAccountsModel.setPinChangeRequired( userDeviceAccountsModel.getPinChangeRequired() == null ? false : userDeviceAccountsModel.getPinChangeRequired() );
	      	tempUserDeviceAccountsModel.setCredentialsExpired( userDeviceAccountsModel.getCredentialsExpired() == null ? false : userDeviceAccountsModel.getCredentialsExpired() );
			
	      	tempUserDeviceAccountsModel.setDeviceTypeId(((UserDeviceAccountsModel)object).getDeviceTypeId());
	      	tempUserDeviceAccountsModel.setAppUserId(((UserDeviceAccountsModel)object).getAppUserId());
	      	tempUserDeviceAccountsModel.setUserId( userDeviceAccountsModel.getUserId() ) ;
	      	
	      	tempUserDeviceAccountsModel.setUpdatedOn(new Date());
	      	tempUserDeviceAccountsModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			
			
			/*if(tempUserDeviceAccountsModel.getPinChangeRequired())
			{	
				tempUserDeviceAccountsModel.setPin(EncoderUtils.encodeToSha(RandomUtils.generateRandom(4,false, true)));
			}*/
			
			baseWrapper.setBasePersistableModel(tempUserDeviceAccountsModel);
			
			baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE );
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.MFS_DEVICE_ACCOUNT_USECASE_ID);
			
			try{
			    this.userDeviceAccountListViewManager.updateUserDeviceAccount(baseWrapper);
			    ControllerUtils.saveMessage(request, "Record saved successfully");
			}
			catch(FrameworkCheckedException fce){
				String msg = fce.getMessage();
				if("AppUserIdDeviceTypeIdCombinationRepeatException".equals(msg)){
					super.saveMessage(request, "For this app user, device type already exist");
					System.out.println("Is update is: "+request.getParameter("isUpdate"));
					System.out.println("userDeviceId: "+request.getParameter("userDeviceId"));
					System.out.println("User Device Accounts Id is: "+request.getParameter("userDeviceAccountsId"));
					return super.showForm(request, response, errors);
				}else{
					super.saveMessage(request, "Record could not be saved.");
				    return super.showForm(request, response, errors);
				}				
			}
			catch (Exception e) {
				e.printStackTrace();
				super.saveMessage(request, MessageUtil.getMessage("6075"));
				return super.showForm(request, response, errors);
			}
	       
	      ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
     		return modelAndView;
	    }
		
	    super.saveMessage(request, "Record could not be saved.");
        return super.showForm(request, response, errors);
	
	}

	public void setUserDeviceAccountListViewManager(
			UserDeviceAccountListViewManager userDeviceAccountListViewManager) {
		this.userDeviceAccountListViewManager = userDeviceAccountListViewManager;
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
	{
		this.referenceDataManager = referenceDataManager;
	}

	public void setAppUserManager(AppUserManager appUserManager)
	{
		this.appUserManager = appUserManager;
	}	


}
