
package com.inov8.microbank.server.service.mnomodule;

import java.util.Date;

import org.hibernate.criterion.MatchMode;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.AppUserPartnerGroupModel;
import com.inov8.microbank.common.model.MnoUserModel;
import com.inov8.microbank.common.model.mnomodule.MnoUserFormModel;
import com.inov8.microbank.common.model.mnomodule.MnoUserListViewModel;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.dao.appuserpartnergroupmodule.AppUserPartnerGroupDAO;
import com.inov8.microbank.server.dao.mnomodule.MnoUserDAO;
import com.inov8.microbank.server.dao.mnomodule.MnoUserListViewDAO;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;

public class MnoUserManagerImpl implements MnoUserManager
{

  private AppUserDAO appUserDAO;
  private MnoUserDAO mnoUserDAO;
  private MnoUserListViewDAO mnoUserListViewDAO;

  private AppUserPartnerGroupDAO appUserPartnerGroupDAO;


public SearchBaseWrapper loadMnoUser(SearchBaseWrapper
                                                   searchBaseWrapper)
   {
     MnoUserModel mnoUserModel = this.
         mnoUserDAO.findByPrimaryKey(searchBaseWrapper.getBasePersistableModel().
                          getPrimaryKey());
     searchBaseWrapper.setBasePersistableModel(mnoUserModel);
     return searchBaseWrapper;
   }

   public BaseWrapper loadMnoUser(BaseWrapper
                                             baseWrapper)
   {
     MnoUserModel mnoUserModel = this.
         mnoUserDAO.
         findByPrimaryKey(baseWrapper.getBasePersistableModel().
                          getPrimaryKey());
     baseWrapper.setBasePersistableModel(mnoUserModel);
     return baseWrapper;
   }

   public SearchBaseWrapper searchMnoUser(SearchBaseWrapper
       searchBaseWrapper)
   {

     CustomList<MnoUserListViewModel>
         list = this.mnoUserListViewDAO.findByExample( (
             MnoUserListViewModel)
         searchBaseWrapper.
         getBasePersistableModel(),
         searchBaseWrapper.
         getPagingHelperModel(),
         searchBaseWrapper.
         getSortingOrderMap());
     searchBaseWrapper.setCustomList(list);
     return searchBaseWrapper;
   }
   
   public SearchBaseWrapper searchAppUserModel(SearchBaseWrapper
	       searchBaseWrapper)
	   {
	   
	     CustomList<AppUserModel>
	         list = this.appUserDAO.findByExample((AppUserModel)searchBaseWrapper.getBasePersistableModel(),
	         searchBaseWrapper.getPagingHelperModel(),searchBaseWrapper.getSortingOrderMap());
	     searchBaseWrapper.setCustomList(list);
	     return searchBaseWrapper;
	   }


   public BaseWrapper createMnoUser(BaseWrapper baseWrapper)throws FrameworkCheckedException
   {

     MnoUserModel mnoUserModel = (MnoUserModel)baseWrapper.getObject("mnoUserModel");
     AppUserModel appUserModel = (AppUserModel)baseWrapper.getObject("appUserModel");
     
     mnoUserModel.setCreatedBy( UserUtils.getCurrentUser().getAppUserId() );
     mnoUserModel.setUpdatedBy( UserUtils.getCurrentUser().getAppUserId() );
     mnoUserModel.setUpdatedOn( new Date() );
     mnoUserModel.setCreatedOn( new Date() );
     
    /* if (!this
				.isMobileNumUnique(appUserModel.getMobileNo()))
			throw new FrameworkCheckedException("MobileNumUniqueException");
			*/

		if (!this
				.isUserNameUnique(appUserModel.getUsername()))
			throw new FrameworkCheckedException("UserNameUniqueException");
     
     mnoUserModel = this.mnoUserDAO.saveOrUpdate(mnoUserModel);
     
     appUserModel.setMnoUserId( mnoUserModel.getMnoUserId() );
     appUserModel.setPasswordChangeRequired(false);
     baseWrapper.putObject( "appUserModel" , appUserModel );

     createAppUserForMno( baseWrapper ) ;

     baseWrapper.setBasePersistableModel(mnoUserModel);
     return baseWrapper;
   }

   public BaseWrapper updateMnoUser(BaseWrapper baseWrapper) throws FrameworkCheckedException
   {
	 MnoUserModel mnoUserModel = (MnoUserModel)baseWrapper.getObject("mnoUserModel");
	 AppUserModel appUserModel = (AppUserModel)baseWrapper.getObject("appUserModel");
	 MnoUserFormModel mnoUserFormModel = (MnoUserFormModel)baseWrapper.getObject("mnoUserFormModel"); 
	 
	 	 
	 AppUserModel tempAppUserModel = new AppUserModel();
	 MnoUserModel tempMnoUserModel = new MnoUserModel();
	 
	 tempAppUserModel = this.appUserDAO.findByPrimaryKey( appUserModel.getAppUserId() ) ;
	
	 if( !tempAppUserModel.getMobileNo().equalsIgnoreCase( appUserModel.getMobileNo() ) )
	 {
		/* if (!this
					.isMobileNumUnique(appUserModel.getMobileNo()))
				throw new FrameworkCheckedException("MobileNumUniqueException");
				*/		 
	 }
	 
	 
	 if (!tempAppUserModel.getUsername().equals(
			 appUserModel.getUsername())) {
		    if (!this.isUserNameUnique(appUserModel.getUsername()))
			    throw new FrameworkCheckedException("UsernameUniqueException");
		}
	 
	 
	 tempAppUserModel.setFirstName( appUserModel.getFirstName() ) ;
	 tempAppUserModel.setLastName(appUserModel.getLastName()) ;
	 tempAppUserModel.setAddress1(appUserModel.getAddress1()) ;
	 tempAppUserModel.setAddress2(appUserModel.getAddress2()) ;
	 tempAppUserModel.setCity(appUserModel.getCity());
	 tempAppUserModel.setState(appUserModel.getState());
	 tempAppUserModel.setCountry(appUserModel.getCountry()) ;
	 tempAppUserModel.setZip(appUserModel.getZip()) ;
	 tempAppUserModel.setFax(appUserModel.getFax());
	 tempAppUserModel.setEmail(appUserModel.getEmail());
	 tempAppUserModel.setMobileTypeId(appUserModel.getMobileTypeId());
	 tempAppUserModel.setMobileNo(appUserModel.getMobileNo()) ;
	 tempAppUserModel.setAccountEnabled(appUserModel.getAccountEnabled());
	 tempAppUserModel.setAccountExpired(appUserModel.getAccountExpired());
	 tempAppUserModel.setAccountLocked(appUserModel.getAccountLocked());
	 tempAppUserModel.setCredentialsExpired(appUserModel.getCredentialsExpired());
	 tempAppUserModel.setVerified(appUserModel.getVerified());
	 tempAppUserModel.setMotherMaidenName(appUserModel.getMotherMaidenName());
	 tempAppUserModel.setNic(appUserModel.getNic());
	 tempAppUserModel.setDob(appUserModel.getDob());
	 tempAppUserModel.setPasswordHint(appUserModel.getPasswordHint());
	 if( appUserModel.getPassword() != null )
		 tempAppUserModel.setPassword(appUserModel.getPassword());
	 
	 
	 
	 
	 tempAppUserModel.setUpdatedBy( UserUtils.getCurrentUser().getAppUserId() );
	 tempAppUserModel.setUpdatedOn( new Date() );
	 
	 tempAppUserModel.setVersionNo( appUserModel.getVersionNo() ) ;
	 
	 try
	 {
		 this.appUserDAO.saveOrUpdate( tempAppUserModel ) ;
	 }
	 catch (Exception e) {
			// TODO Auto-generated catch block
			if(e.getMessage().contains("i8-20001"))
			{
				throw new FrameworkCheckedException("MobileNumUniqueException");
			}
   }
	 
	 tempMnoUserModel.setMnoUserId( mnoUserModel.getMnoUserId() ) ;
	 tempMnoUserModel = this.mnoUserDAO.findByPrimaryKey( mnoUserModel.getMnoUserId() ) ;
	 tempMnoUserModel.setDescription( mnoUserModel.getDescription() ) ;
	 tempMnoUserModel.setComments( mnoUserModel.getComments() ) ;
	 tempMnoUserModel.setMnoId( mnoUserModel.getMnoId() ) ;
	 tempMnoUserModel.setVersionNo( mnoUserModel.getVersionNo() ) ;
	 tempMnoUserModel.setUpdatedBy( UserUtils.getCurrentUser().getAppUserId() );
	 tempMnoUserModel.setUpdatedOn( new Date() );

     mnoUserModel = this.mnoUserDAO.saveOrUpdate( tempMnoUserModel );
     
    
     	AppUserPartnerGroupModel appUserPartnerGroupModel =new AppUserPartnerGroupModel();
	    appUserPartnerGroupModel.setAppUserId(appUserModel.getAppUserId());
	    //appUserPartnerGroupModel.setPartnerGroupId(mnoUserFormModel.getPartnerGroupId());
	    CustomList list=appUserPartnerGroupDAO.findByExample(appUserPartnerGroupModel);
	    if(list.getResultsetList().size()>0)
	    {
	    	
	    	appUserPartnerGroupModel=(AppUserPartnerGroupModel)list.getResultsetList().get(0);
	    	appUserPartnerGroupModel.setPartnerGroupId(mnoUserFormModel.getPartnerGroupId());
	    	appUserPartnerGroupModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
		    appUserPartnerGroupModel.setUpdatedOn(new Date());
		    appUserPartnerGroupDAO.saveOrUpdate(appUserPartnerGroupModel);
	    }
	    
	    
	    
     
     
 	 /*AppUserAppRoleModel appUserAppRoleModel = UserUtils.determineCurrentAppUserAppRoleModel(tempAppUserModel);
	 AccessLevelModel aLevelModel = new AccessLevelModel();
	 aLevelModel.setAccessLevelId(mnoUserFormModel.getAccessLevelId());

	 appUserAppRoleModel.setAccessLevelModel(aLevelModel);
	 this.appUserAppRoleDAO.saveOrUpdate(appUserAppRoleModel);		
*/
     
     baseWrapper.setBasePersistableModel(mnoUserModel);
     return baseWrapper;
   }

   public BaseWrapper createAppUserForMno(BaseWrapper baseWrapper) throws
        FrameworkCheckedException
    {
      AppUserModel appUserModel = (AppUserModel)baseWrapper.getObject("appUserModel");
      MnoUserFormModel mnoUserFormModel = (MnoUserFormModel)baseWrapper.getObject("mnoUserFormModel");

      appUserModel.setCreatedOn(new Date());
      appUserModel.setUpdatedOn(new Date());
      appUserModel.setCreatedBy( UserUtils.getCurrentUser().getAppUserId() );
      appUserModel.setUpdatedBy( UserUtils.getCurrentUser().getAppUserId() );
      appUserModel.setAppUserTypeId(UserTypeConstantsInterface.MNO);
      try
      {
    	  this.appUserDAO.saveUser(appUserModel);
      }
      catch (Exception e) {
			// TODO Auto-generated catch block
			if(e.getMessage().contains("i8-20001"))
			{
				throw new FrameworkCheckedException("MobileNumUniqueException");
			}
      }
      
      
      AppUserPartnerGroupModel appUserPartnerGroupModel =new AppUserPartnerGroupModel();
	    appUserPartnerGroupModel.setAppUserId(appUserModel.getAppUserId());
	    appUserPartnerGroupModel.setPartnerGroupId(mnoUserFormModel.getPartnerGroupId());
	    appUserPartnerGroupModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
	    appUserPartnerGroupModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
	    appUserPartnerGroupModel.setCreatedOn(new Date());
	    appUserPartnerGroupModel.setUpdatedOn(new Date());
	    appUserPartnerGroupDAO.saveOrUpdate(appUserPartnerGroupModel);
      //appUserModel.setVerified(false);
      
      /*AppRoleModel appRoleModel = new AppRoleModel() ;
      appRoleModel.setAppRoleId( AppRoleConstantsInterface.MNO ) ;
      //appUserModel.getRoles().add( appRoleModel ) ;
      
      
 	 AccessLevelModel aLevelModel = new AccessLevelModel();
	 aLevelModel.setAccessLevelId(mnoUserFormModel.getAccessLevelId());
	 AppUserAppRoleModel aUserAppRoleModel = new AppUserAppRoleModel(aLevelModel, appRoleModel, appUserModel);
	 this.appUserAppRoleDAO.saveOrUpdate(aUserAppRoleModel);
      
      
      baseWrapper.setBasePersistableModel(appUserModel);*/

      return baseWrapper;
   }
   
   
   
   public BaseWrapper activateDeactivateMnoUser(BaseWrapper baseWrapper) throws FrameworkCheckedException {
	    // TODO Auto-generated method stub
	   Long mnoUserId = (Long)baseWrapper.getObject("mnoUserId");
	   Boolean active = (Boolean)baseWrapper.getObject("active");
	   Long appUserId = (Long)baseWrapper.getObject("appUserId");
	   AppUserModel appUserModel = appUserModel = this.appUserDAO.findByPrimaryKey(appUserId);
	   appUserModel.setAccountEnabled(active);
	   this.appUserDAO.saveOrUpdate(appUserModel);
	   baseWrapper.setBasePersistableModel(appUserModel);
	   return baseWrapper;
   }

private boolean isMobileNumUnique(String mobileNo) {
		AppUserModel appUserModel = new AppUserModel();
		appUserModel.setMobileNo(mobileNo);
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT);
		int count = this.appUserDAO.countByExample(appUserModel,exampleHolder);
		if (count == 0)
			return true;
		else
			return false;
	}
   
	private boolean isUserNameUnique(String userName) {
		AppUserModel appUserModel = new AppUserModel();
		appUserModel.setUsername(userName);
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT);
		int count = this.appUserDAO.countByExample(appUserModel,exampleHolder);
		
		if (count == 0)
			return true;
		else
			return false;
	}
	
	public  Long getAppUserPartnerGroupId(Long appUserId)throws FrameworkCheckedException 
	{
		AppUserPartnerGroupModel appUserPartnerGroupModel = new	AppUserPartnerGroupModel();
		appUserPartnerGroupModel.setAppUserId(appUserId);
		
		

		CustomList list = this.appUserPartnerGroupDAO.findByExample(appUserPartnerGroupModel);
		if(list!=null && list.getResultsetList().size()>0)
		{
		appUserPartnerGroupModel =(AppUserPartnerGroupModel)list.getResultsetList().get(0);
		return appUserPartnerGroupModel.getPartnerGroupId();
		}
		else
			throw new FrameworkCheckedException("User doest not belong to any partner group");
		
	
		
		
	}


  public void setAppUserDAO(AppUserDAO appUserDAO)
  {
    this.appUserDAO = appUserDAO;
  }

  public void setMnoUserDAO(MnoUserDAO mnoUserDAO)
  {
    this.mnoUserDAO = mnoUserDAO;
  }

  public void setMnoUserListViewDAO(MnoUserListViewDAO mnoUserListViewDAO)
  {
    this.mnoUserListViewDAO = mnoUserListViewDAO;
  }

public void setAppUserPartnerGroupDAO(
		AppUserPartnerGroupDAO appUserPartnerGroupDAO) {
	this.appUserPartnerGroupDAO = appUserPartnerGroupDAO;
}

}
