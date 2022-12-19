package com.inov8.microbank.webapp.action.mnomodule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.ExceptionErrorCodes;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.MnoModel;
import com.inov8.microbank.common.model.MnoUserModel;
import com.inov8.microbank.common.model.MobileTypeModel;
import com.inov8.microbank.common.model.PartnerGroupModel;
import com.inov8.microbank.common.model.PartnerModel;
import com.inov8.microbank.common.model.mnomodule.MnoUserFormModel;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.server.service.mnomodule.MnoUserManager;
import com.inov8.microbank.server.service.portal.partnergroupmodule.PartnerGroupManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;

/**
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backend Application for POS terminals.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Asad Hayat
 * @version 1.0
 */


public class MnoUserFormController
    extends AdvanceFormController
{
  private MnoUserManager mnoUserManager;
  private ReferenceDataManager referenceDataManager;
  private AppUserManager appUserManager;
  private PartnerGroupManager partnerGroupManager;
  private Long id = null;

  public MnoUserFormController()
  {
    setCommandName("mnoUserModel");
    setCommandClass(MnoUserFormModel.class);
  }

  @Override
  protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws
      FrameworkCheckedException
  {
    if (log.isDebugEnabled())
    {
      log.debug("Inside reference data");
    }

    /**
     * code fragment to load reference data  for Mno
     *
     */

	
	MnoUserModel mnoUserModel = new MnoUserModel();
	if(null!=id)
	{
		
		SearchBaseWrapper searchBaseWrapper =new SearchBaseWrapperImpl();
		
	       mnoUserModel.setMnoUserId(id);
	      searchBaseWrapper.setBasePersistableModel(mnoUserModel);
	      mnoUserModel = (MnoUserModel)this.mnoUserManager.loadMnoUser(searchBaseWrapper).getBasePersistableModel() ;

	

	}

    
    MnoModel mnoModel = new MnoModel();
    List<MnoModel> mnoModelList = null;
    mnoModel.setActive(true);
    ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
        mnoModel, "name", SortingOrder.ASC);
    referenceDataWrapper.setBasePersistableModel(mnoModel);
    referenceDataManager.getReferenceData(referenceDataWrapper);
   
    if (referenceDataWrapper.getReferenceDataList() != null)
    {
      mnoModelList = referenceDataWrapper.getReferenceDataList();
    }

    Map referenceDataMap = new HashMap();
    referenceDataMap.put("mnoModelList", mnoModelList);
    
    /**
     * code fragment to load reference data for ServiceModel
     */

    MobileTypeModel mobileTypeModel = new MobileTypeModel();
    
    referenceDataWrapper = new ReferenceDataWrapperImpl(
    		mobileTypeModel, "name", SortingOrder.ASC);
    referenceDataWrapper.setBasePersistableModel(mobileTypeModel);

    try
    {
      referenceDataManager.getReferenceData(referenceDataWrapper);
    }
    catch (FrameworkCheckedException ex)
    {
    }
    List<MobileTypeModel> mobileTypeModelList = null;
    if (referenceDataWrapper.getReferenceDataList() != null)
    {
    	mobileTypeModelList = referenceDataWrapper.getReferenceDataList();
    }
    referenceDataMap.put("mobileTypeModelList", mobileTypeModelList);
    
    
    
    PartnerModel partnerModel = new PartnerModel();
	partnerModel.setAppUserTypeId(UserTypeConstantsInterface.MNO);
	
	if(null!=id)
	{
		if(mnoUserModel!=null)
		{	
		partnerModel.setMnoId(mnoUserModel.getMnoId());
		}
	}
	
	else
	{	
		if(mnoModelList!=null && mnoModelList.size()>0)
		{
			
			Long mnoId=null;
			try{
			mnoId = ServletRequestUtils.getLongParameter(httpServletRequest,"mnoId");
			}
			catch(Exception e)
			{
			e.printStackTrace();	
			}
			
			 
			
			
			if(null!=mnoId)
			{
				
				partnerModel.setMnoId(mnoId);
			}
			else
			{
				partnerModel.setMnoId(mnoModelList.get(0).getMnoId());	
			}
			
			
		}
	}
	
	referenceDataWrapper = new ReferenceDataWrapperImpl(
			partnerModel, "name", SortingOrder.ASC);
	referenceDataWrapper.setBasePersistableModel(partnerModel);
	try {
	referenceDataManager.getReferenceData(referenceDataWrapper);
	} catch (FrameworkCheckedException ex1) {
		ex1.getStackTrace();
	}
	List<PartnerModel> partnerModelList = null;
	if (referenceDataWrapper.getReferenceDataList() != null) 
	{
		partnerModelList = referenceDataWrapper.getReferenceDataList();
	}
	
	
	PartnerGroupModel partnerGroupModel = new PartnerGroupModel();
	List<PartnerGroupModel> partnerGroupModelList = null;
	if(partnerModelList!=null && partnerModelList.size()>0)
	{
		partnerGroupModel.setActive(true);
		partnerGroupModel.setPartnerId(partnerModelList.get(0).getPartnerId());
		partnerGroupModelList =partnerGroupManager.getPartnerGroups(partnerGroupModel,true);
	}
    /*PartnerGroupModel partnerGroupModel = new PartnerGroupModel();
	partnerGroupModel.setActive(true);
	if(partnerModelList!=null && partnerModelList.size()>0)
	{
	partnerGroupModel.setPartnerId(partnerModelList.get(0).getPartnerId());
	}
	referenceDataWrapper = new ReferenceDataWrapperImpl(
			partnerGroupModel, "name", SortingOrder.ASC);
	referenceDataWrapper.setBasePersistableModel(partnerGroupModel);
	try {
	referenceDataManager.getReferenceData(referenceDataWrapper);
	} catch (FrameworkCheckedException ex1) {
		ex1.getStackTrace();
	}
	List<PartnerGroupModel> partnerGroupModelList = null;
	if (referenceDataWrapper.getReferenceDataList() != null) 
	{
		partnerGroupModelList = referenceDataWrapper.getReferenceDataList();
	}
*/
	
	referenceDataMap.put("partnerGroupModelList", partnerGroupModelList);

  
    
    return referenceDataMap;
  }

  @Override
  protected Object loadFormBackingObject(HttpServletRequest
                                         httpServletRequest) throws
      Exception
  {
    id = ServletRequestUtils.getLongParameter(httpServletRequest,
                                              "mnoUserId");
    if (null != id)
    {
      if (log.isDebugEnabled())
      {
        log.debug("id is not null....retrieving object from DB");
      }
      
      MnoUserFormModel mnoUserForm = new MnoUserFormModel();
      SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
      BaseWrapper baseWrapper = new BaseWrapperImpl();  
      MnoUserModel mnoUserModel = new MnoUserModel();
      
      mnoUserModel.setMnoUserId(id);
      searchBaseWrapper.setBasePersistableModel(mnoUserModel);
      mnoUserModel = (MnoUserModel)this.mnoUserManager.loadMnoUser(searchBaseWrapper).getBasePersistableModel() ;

      AppUserModel appUserModel = new AppUserModel() ;
      appUserModel.setAppUserId( ServletRequestUtils.getLongParameter(httpServletRequest,"appUserId") );      
      baseWrapper.setBasePersistableModel(appUserModel);
      appUserModel = (AppUserModel)this.appUserManager.loadAppUser( baseWrapper ).getBasePersistableModel() ;
      
      BeanUtils.copyProperties(mnoUserForm, appUserModel);
      BeanUtils.copyProperties(mnoUserForm, mnoUserModel);
      
      mnoUserForm.setMnoName( mnoUserModel.getMnoIdMnoModel().getName() ) ;
      mnoUserForm.setMnoId( String.valueOf(mnoUserModel.getMnoId())) ;
      mnoUserForm.setMobileTypeId( String.valueOf(appUserModel.getMobileTypeId())) ;
      mnoUserForm.setPartnerGroupId(this.mnoUserManager.getAppUserPartnerGroupId(appUserModel.getAppUserId()));
      /*mnoUserForm.setAccessLevelId(
    		  UserUtils.determineCurrentAppUserAppRoleModel(appUserModel).getAccessLevelModel().getAccessLevelId()	  
      );*/

      return mnoUserForm ;
    }
    else
    {
      if (log.isDebugEnabled())
      {
        log.debug("id is null....creating new instance of Model");
      }

      return new MnoUserFormModel();
    }
  }

  @Override
  protected ModelAndView onCreate(HttpServletRequest httpServletRequest,
                                  HttpServletResponse httpServletResponse,
                                  Object object, BindException bindException) throws
      Exception
  {

    return this.createOrUpdate(httpServletRequest, httpServletResponse, object,
                               bindException);
  }

  @Override
  protected ModelAndView onUpdate(HttpServletRequest httpServletRequest,
                                  HttpServletResponse httpServletResponse,
                                  Object object, BindException bindException) throws
      Exception
  {
    return this.createOrUpdate(httpServletRequest, httpServletResponse, object,
                               bindException);
  }

  private ModelAndView createOrUpdate(HttpServletRequest request,
                                      HttpServletResponse response,
                                      Object command,
                                      BindException errors) throws Exception
  {
	  MnoUserFormModel mnoUserFormModel = (MnoUserFormModel)command;
    try
    {
      BaseWrapper baseWrapper = new BaseWrapperImpl();

     

      if (null != id)
      {
    	AppUserModel appUser = populateAppUserModel(mnoUserFormModel) ;
    	MnoUserModel mnoUser = populateMnoUserModel(mnoUserFormModel) ;
    	
    	appUser.setVersionNo( mnoUserFormModel.getAppUserModelVersionNo() ) ;
    	appUser.setAppUserId( Long.parseLong(mnoUserFormModel.getAppUserId()) ) ;
    	mnoUser.setVersionNo( mnoUserFormModel.getMnoModelVersionNo() ) ;
    	mnoUser.setMnoUserId( Long.parseLong(mnoUserFormModel.getMnoUserId() )) ;
    	  
    	baseWrapper.putObject("appUserModel", appUser );
        baseWrapper.putObject("mnoUserModel", mnoUser );
        baseWrapper.putObject("mnoUserFormModel", mnoUserFormModel );
                
        baseWrapper = this.mnoUserManager.updateMnoUser(baseWrapper);
      }
      else
      {
        baseWrapper.putObject("appUserModel", populateAppUserModel(mnoUserFormModel) );
        baseWrapper.putObject("mnoUserModel", populateMnoUserModel(mnoUserFormModel) );
        baseWrapper.putObject("mnoUserFormModel",mnoUserFormModel);
        baseWrapper = this.mnoUserManager.createMnoUser(baseWrapper);
      }

      this.saveMessage(request, "Record saved successfully");
      ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
      return modelAndView;

    }
    catch (FrameworkCheckedException ex)
    {
    	request.setAttribute("mnoId", mnoUserFormModel.getMnoId());
    	if( ex.getMessage().equalsIgnoreCase("MobileNumUniqueException") )
		{
			super.saveMessage(request, "Mobile number already exists.");
			
			return super.showForm(request, response, errors);				
		}			
		else if( ex.getMessage().equalsIgnoreCase("UsernameUniqueException") )
		{
			super.saveMessage(request, "Username already exists.");
			
			return super.showForm(request, response, errors);				
		}
		else if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION ==
          ex.getErrorCode())
      {
        super.saveMessage(request, "Record could not be saved.");
        
        return super.showForm(request, response, errors);
      }
      throw ex;
    }
    catch (Exception ex)
    {
    	request.setAttribute("mnoId", mnoUserFormModel.getMnoId()); 	
        super.saveMessage(request, "Record could not be saved.");    
        return super.showForm(request, response, errors);
    }

  }

  private AppUserModel populateAppUserModel( MnoUserFormModel mnoUserFormModel )
  {
    AppUserModel appUserModel = new AppUserModel();

    appUserModel.setFirstName( mnoUserFormModel.getFirstName() );
    appUserModel.setLastName( mnoUserFormModel.getLastName() );
    appUserModel.setMobileNo( mnoUserFormModel.getMobileNo() );
    
    appUserModel.setAddress1( mnoUserFormModel.getAddress1() );
    appUserModel.setAddress2( mnoUserFormModel.getAddress2() );

    appUserModel.setUsername( mnoUserFormModel.getUsername() );
    if( mnoUserFormModel.getPassword() != null )
    	appUserModel.setPassword( EncoderUtils.encodeToSha(mnoUserFormModel.getPassword()) );
    appUserModel.setCity( mnoUserFormModel.getCity() );
    appUserModel.setState( mnoUserFormModel.getState() );
    appUserModel.setCountry( mnoUserFormModel.getCountry() );
    appUserModel.setZip( mnoUserFormModel.getZip() );
    appUserModel.setEmail( mnoUserFormModel.getEmail() );
    appUserModel.setFax( mnoUserFormModel.getFax() );
    
    appUserModel.setMotherMaidenName(mnoUserFormModel.getMotherMaidenName());
    appUserModel.setNic(mnoUserFormModel.getNic());
    appUserModel.setDob(mnoUserFormModel.getDob());
    appUserModel.setPasswordHint(mnoUserFormModel.getPasswordHint());
    
    appUserModel.setAccountEnabled( mnoUserFormModel.getAccountEnabled() == null ? false : mnoUserFormModel.getAccountEnabled() );
    appUserModel.setAccountExpired( mnoUserFormModel.getAccountExpired() == null ? false : mnoUserFormModel.getAccountExpired());
    appUserModel.setAccountLocked( mnoUserFormModel.getAccountLocked() == null ? false : mnoUserFormModel.getAccountLocked());
    appUserModel.setCredentialsExpired( mnoUserFormModel.getCredentialsExpired() == null ? false : mnoUserFormModel.getCredentialsExpired());
    appUserModel.setVerified( mnoUserFormModel.getVerified() == null ? false : mnoUserFormModel.getVerified());    
    appUserModel.setMobileTypeId( Long.parseLong(mnoUserFormModel.getMobileTypeId()) );
    
    return appUserModel;
  }

  private MnoUserModel populateMnoUserModel( MnoUserFormModel mnoUserFormModel )
  {
    MnoUserModel mnoUserModel = new MnoUserModel();

    mnoUserModel.setMnoId( Long.parseLong(mnoUserFormModel.getMnoId()) );
    mnoUserModel.setComments( mnoUserFormModel.getComments() ) ;
    mnoUserModel.setDescription( mnoUserFormModel.getDescription() ) ;

    return mnoUserModel;
  }

  public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
  {
    this.referenceDataManager = referenceDataManager;
  }

  public void setAppUserManager(AppUserManager appUserManager)
  {
    this.appUserManager = appUserManager;
  }

  public void setMnoUserManager(MnoUserManager mnoUserManager)
  {
    this.mnoUserManager = mnoUserManager;
  }

public void setPartnerGroupManager(PartnerGroupManager partnerGroupManager) {
	this.partnerGroupManager = partnerGroupManager;
}
}
