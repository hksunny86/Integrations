package com.inov8.microbank.webapp.action.suppliermodule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.ExceptionErrorCodes;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.MobileTypeModel;
import com.inov8.microbank.common.model.PartnerGroupModel;
import com.inov8.microbank.common.model.PartnerModel;
import com.inov8.microbank.common.model.SupplierModel;
import com.inov8.microbank.common.model.SupplierUserModel;
import com.inov8.microbank.common.model.suppliermodule.SupplierUserFormListViewModel;
import com.inov8.microbank.common.model.suppliermodule.SupplierUserListViewModel;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.server.service.portal.partnergroupmodule.PartnerGroupManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.suppliermodule.SupplierUserManager;

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


public class SupplierUserFormController
    extends AdvanceFormController
{
  private SupplierUserManager supplierUserManager;
  private ReferenceDataManager referenceDataManager;
  private PartnerGroupManager partnerGroupManager;
  private AppUserManager appUserManager;
  private Long id = null;

  public SupplierUserFormController()
  {
    setCommandName("supplierUserFormListViewModel");
    setCommandClass(SupplierUserFormListViewModel.class);
  }
  @Override
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
	{
	  super.initBinder(request, binder);
	  CommonUtils.bindCustomDateEditor(binder);
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
     * code fragment to load reference data  for Supplier
     *
     */

    SupplierModel supplierModel = new SupplierModel();
    supplierModel.setActive(true);
    ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
        supplierModel, "name", SortingOrder.ASC);
    referenceDataWrapper.setBasePersistableModel(supplierModel);
    referenceDataManager.getReferenceData(referenceDataWrapper);
    List<SupplierModel> supplierModelList = null;
    if (referenceDataWrapper.getReferenceDataList() != null)
    {
      supplierModelList = referenceDataWrapper.getReferenceDataList();
    }

    Map referenceDataMap = new HashMap();
    referenceDataMap.put("supplierModelList",
                         supplierModelList);
    
    MobileTypeModel mobileTypeModel = new MobileTypeModel();

	//paymentModeModel.setActive(true);
	referenceDataWrapper = new ReferenceDataWrapperImpl(mobileTypeModel,
			"name", SortingOrder.ASC);

	try {
		referenceDataManager.getReferenceData(referenceDataWrapper);
	} catch (FrameworkCheckedException ex1) {
		ex1.getStackTrace();
	}
	List<MobileTypeModel> mobileTypeModelList = null;
	if (referenceDataWrapper.getReferenceDataList() != null) {
		mobileTypeModelList = referenceDataWrapper.getReferenceDataList();
	}

	referenceDataMap.put("mobileTypeModelList", mobileTypeModelList);


	
	PartnerModel partnerModel = new PartnerModel();
	partnerModel.setAppUserTypeId(UserTypeConstantsInterface.SUPPLIER);
	if(supplierModelList!=null && supplierModelList.size()>0)
	{
		
		
		
		
		Long supplierId = (Long)httpServletRequest.getAttribute("supplierId");
		
		if(null!=supplierId)
		{
			
			partnerModel.setSupplierId(supplierId);
		}
		else
		{
			partnerModel.setSupplierId(supplierModelList.get(0).getSupplierId());	
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
	}*/

	
	referenceDataMap.put("partnerGroupModelList", partnerGroupModelList);
	
	/*AppRoleModel appRoleModelPaymentSerives = new AppRoleModel();
	AppRoleModel appRoleModelProductSupplier = new AppRoleModel();
	appRoleModelPaymentSerives.setAppRoleId(AppRoleConstantsInterface.PAYMENT_SERVICE);
	appRoleModelPaymentSerives.setName("PaymentServies");
	
	appRoleModelProductSupplier.setAppRoleId(AppRoleConstantsInterface.PRODUCT_SUPPLIER);
	appRoleModelProductSupplier.setName("ProductSupplier");
	
	List<AppRoleModel> appRoleModelList = new ArrayList<AppRoleModel> ();
	appRoleModelList.add(appRoleModelPaymentSerives);
	appRoleModelList.add(appRoleModelProductSupplier);

	referenceDataMap.put("appRoleModelList", appRoleModelList);
*/
	
	//load accesslevels		
	/*AccessLevelModel accessLevelModel = new AccessLevelModel();
	
	List<AccessLevelModel> accessLevelModelList = null;
	referenceDataWrapper = new ReferenceDataWrapperImpl(accessLevelModel, "accessLevelName", SortingOrder.ASC);

	referenceDataWrapper.setBasePersistableModel(accessLevelModel);
	try
	{
		referenceDataManager.getReferenceData(referenceDataWrapper);
	}
	catch (Exception e)
	{
		e.printStackTrace();
	}
	
	if (referenceDataWrapper.getReferenceDataList() != null)
	{
		accessLevelModelList = referenceDataWrapper.getReferenceDataList();
		List<AccessLevelModel> accessLevelRefDataList = new ArrayList();
		Iterator<AccessLevelModel> accessItrator = accessLevelModelList.iterator();
		while (accessItrator.hasNext())
		{
			accessLevelModel = accessItrator.next();
			accessLevelRefDataList.add(accessLevelModel);
		}
		
		referenceDataMap.put("accessLevelRefDataList", accessLevelRefDataList);
	}		
*/	
	
    return referenceDataMap;
  }

  @Override
  protected Object loadFormBackingObject(HttpServletRequest
                                         httpServletRequest) throws
      Exception
  {
    id = ServletRequestUtils.getLongParameter(httpServletRequest,
                                              "supplierUserId");
    if (null != id)
    {
      if (log.isDebugEnabled())
      {
        log.debug("id is not null....retrieving object from DB");
      }

      SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
      BaseWrapper baseWrapper = new BaseWrapperImpl();
      SupplierUserListViewModel supplierUserListViewModel = new
      SupplierUserListViewModel();
      SupplierUserFormListViewModel supplierUserFormListViewModel = new SupplierUserFormListViewModel();
      SupplierUserModel supplierUserModel =new SupplierUserModel();
      supplierUserModel.setSupplierUserId(id);
      supplierUserListViewModel.setSupplierUserId(id);
      baseWrapper.setBasePersistableModel(supplierUserModel);
      searchBaseWrapper.setBasePersistableModel(supplierUserListViewModel);
      searchBaseWrapper = this.supplierUserManager.loadSupplierUser(
          searchBaseWrapper);
      baseWrapper=this.supplierUserManager.loadSupplierUser(baseWrapper);
      supplierUserListViewModel= (SupplierUserListViewModel) searchBaseWrapper.
      getBasePersistableModel();
      
      
      supplierUserModel=(SupplierUserModel)baseWrapper.getBasePersistableModel();
      
       AppUserModel appUserModel = new AppUserModel();
			appUserModel = appUserManager.getUser(String
					.valueOf(supplierUserListViewModel.getAppUserId()));
			supplierUserFormListViewModel.setComments(supplierUserModel.getComments());
			
			/*Iterator<AppRoleModel> rolesIter = appUserModel.getRoles().iterator();
			if(rolesIter.hasNext())
				supplierUserFormListViewModel.setAppRoleId( ((AppRoleModel)rolesIter.next()).getAppRoleId());
			*/
			
			supplierUserFormListViewModel.setDescription(supplierUserModel.getDescription());
			BeanUtils.copyProperties(supplierUserFormListViewModel,
					supplierUserListViewModel);
			BeanUtils.copyProperties(supplierUserFormListViewModel, appUserModel);
			try{
			supplierUserFormListViewModel.setPartnerGroupId(this.supplierUserManager.getAppUserPartnerGroupId(appUserModel.getAppUserId()));
			}
			catch (FrameworkCheckedException ex)
			{
				
				 if( ex.getMessage().equalsIgnoreCase("Userdoestnotbelongtoanypartnergroup") )
				{
					super.saveMessage(httpServletRequest, "User does not belong to any partner group");
					
				}
			}
			
	/*		supplierUserFormListViewModel.setAccessLevelId(
					UserUtils.determineCurrentAppUserAppRoleModel(appUserModel).getAccessLevelModel().getAccessLevelId()
			);*/
       
      return supplierUserFormListViewModel;
    }
    else
    {
      if (log.isDebugEnabled())
      {
        log.debug("id is null....creating new instance of Model");
      }

      return new SupplierUserFormListViewModel();
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

	  SupplierUserFormListViewModel supplierUserFormListViewModel = (SupplierUserFormListViewModel) command;
		try {
			BaseWrapper baseWrapper = new BaseWrapperImpl();

			
			

			if (null != id) {

				baseWrapper.putObject("SupplierUserFormListViewModel",
						supplierUserFormListViewModel);

				baseWrapper = this.supplierUserManager.updateSupplierUser(

				baseWrapper);

			} else {

			
				baseWrapper.putObject("SupplierUserFormListViewModel",
						supplierUserFormListViewModel);
				baseWrapper = this.supplierUserManager.createSupplierUser(baseWrapper);

			
			

			}


	  
	  
	        this.saveMessage(request, "Record saved successfully");
      ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
      return modelAndView;

    }
    catch (FrameworkCheckedException ex)
    {
    	System.out.println("the error mesaage is "+ex.getMessage());
    	request.setAttribute("supplierId", supplierUserFormListViewModel.getSupplierId());
    	if( ex.getMessage().equalsIgnoreCase("MobileNumUniqueException") )
		{
			super.saveMessage(request, "Mobile number already exists.");
			
			
			return super.showForm(request, response, errors);				
		}			
		else if( ex.getMessage().equalsIgnoreCase("UserNameUniqueException") )
		{
			super.saveMessage(request, "Username already exists.");
			
			return super.showForm(request, response, errors);				
		}
		else if( ex.getMessage().equalsIgnoreCase("Userdoestnotbelongtoanypartnergroup") )
		{
			super.saveMessage(request, "User does not belong to any partner group");
			
			return super.showForm(request, response, errors);				
		}
		else if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION == ex
				.getErrorCode()) {
			super.saveMessage(request, "Record could not be saved.");
			
			return super.showForm(request, response, errors);
		}
      throw ex;
    }

  }

  public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
  {
    this.referenceDataManager = referenceDataManager;
  }

  public void setAppUserManager(AppUserManager appUserManager)
  {
    this.appUserManager = appUserManager;
  }

  public void setSupplierUserManager(SupplierUserManager supplierUserManager)
  {
    this.supplierUserManager = supplierUserManager;
  }

public void setPartnerGroupManager(PartnerGroupManager partnerGroupManager) {
	this.partnerGroupManager = partnerGroupManager;
}

}
