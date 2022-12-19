/**
 * 
 */
package com.inov8.microbank.webapp.action.portal.mfsaccountmodule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.common.vo.account.BulkCustomerUpdateVo;
import com.inov8.microbank.server.service.customermodule.CustomerManager;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Aug 15, 2014 7:00:00 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class BulkCustomerUpdateController extends AdvanceFormController
{
	private static final Logger LOGGER = Logger.getLogger( BulkCustomerUpdateController.class );

	private ReferenceDataManager referenceDataManager;
	private MfsAccountManager 	mfsAccountManager; 
	private CustomerManager		customerManager;
	private AppUserManager		appUserManager;

	public BulkCustomerUpdateController()
	{
		setCommandName("bulkCustomerUpdateVo");
		setCommandClass(BulkCustomerUpdateVo.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected Map<String, Object> loadReferenceData(HttpServletRequest request) throws Exception
	{
		Map<String, Object> refDataMap = new HashMap<>(2);
		List<OlaCustomerAccountTypeModel> olaCustomerAccountTypeModelList = null;
		List<SegmentModel> segmentModelList = null;
		OlaCustomerAccountTypeModel olaCustomerAccountTypeModel = new OlaCustomerAccountTypeModel();
		olaCustomerAccountTypeModel.setIsCustomerAccountType(Boolean.TRUE);
		olaCustomerAccountTypeModel.setActive(Boolean.TRUE);
		ReferenceDataWrapper refDataWrapper = new ReferenceDataWrapperImpl(olaCustomerAccountTypeModel, "name", SortingOrder.ASC);
		try
		{
			refDataWrapper = referenceDataManager.getReferenceData(refDataWrapper);
			olaCustomerAccountTypeModelList = refDataWrapper.getReferenceDataList();

			SegmentModel segmentModel = new SegmentModel();
			segmentModel.setIsActive(Boolean.TRUE);
			refDataWrapper = new ReferenceDataWrapperImpl(segmentModel, "name", SortingOrder.ASC);
			refDataWrapper = referenceDataManager.getReferenceData(refDataWrapper);
			segmentModelList = refDataWrapper.getReferenceDataList();
		}
		catch (FrameworkCheckedException e)
		{
			LOGGER.error(e.getMessage(), e);
		}
		refDataMap.put("olaCustomerAccountTypeModelList", olaCustomerAccountTypeModelList);
		refDataMap.put("segmentModelList", segmentModelList);
		return refDataMap;
	}

	@Override
	protected BulkCustomerUpdateVo loadFormBackingObject(HttpServletRequest request) throws Exception
	{
		return new BulkCustomerUpdateVo();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected ModelAndView onCreate(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors) throws Exception
	{
		BulkCustomerUpdateVo bulkCustomerUpdateVo = (BulkCustomerUpdateVo)command;
		String updateField = ServletRequestUtils.getStringParameter(request, "update");
		String accTypeAppUserIds = ServletRequestUtils.getStringParameter(request, "accTypeAppUserIds");
		String segmentAppUserIds = ServletRequestUtils.getStringParameter(request, "segmentAppUserIds");
		
		if ( updateField.equalsIgnoreCase("segment") ){//bulk segment update request, segment will be updated in customer table
			if(bulkCustomerUpdateVo.getSegmentId() == null ){
				super.saveMessage(request, "Please select a segment which needs to be updated on selected records");
				return new ModelAndView(getSuccessView());
			}
			
			//update segment_id in customer table for selected appUserIds.
			Collection<String> appUserIdList = new ArrayList<String>(0);
			Collections.addAll(appUserIdList, segmentAppUserIds.split(","));
			List<Long> appUserIdLongList = new ArrayList<Long>(appUserIdList.size());
			for(String strAppUserId : appUserIdList){
				appUserIdLongList.add(Long.valueOf(strAppUserId));
			}
			List<CustomerModel> customers = appUserManager.findCustomersByAppUserIds(appUserIdLongList);
			appUserManager.updateCustomersWithAttribute("segmentId", bulkCustomerUpdateVo.getSegmentId().toString(), customers);
			
		}else{//bulk account type update request, account type will be updated in account and customer table
			if(bulkCustomerUpdateVo.getCustomerAccountTypeId() == null ){
				super.saveMessage(request, "Please select an account type which needs to be updated on selected records");
				return new ModelAndView(getSuccessView());
			}
			
			//update customer for account_type_id
			Collection<String> appUserIdList = new ArrayList<String>(0);
			Collections.addAll(appUserIdList, accTypeAppUserIds.split(","));
			List<Long> appUserIdLongList = new ArrayList<Long>(appUserIdList.size());
			for(String strAppUserId : appUserIdList){
				appUserIdLongList.add(Long.valueOf(strAppUserId));
			}
			List<CustomerModel> customers = appUserManager.findCustomersByAppUserIds(appUserIdLongList);
			try{
				appUserManager.updateCustomersWithAttribute("accountTypeId", bulkCustomerUpdateVo.getCustomerAccountTypeId().toString(), customers);
				//	update Account table for account type id
				appUserManager.updateAccountWithAccountTypeId(appUserIdLongList, bulkCustomerUpdateVo.getCustomerAccountTypeId());
			}catch(Exception e){
				super.saveMessage(request, e.getMessage());
				return new ModelAndView(getSuccessView());
			}
		}
		super.saveMessage(request, "Selected records are updated for new " + updateField);
		return new ModelAndView(getSuccessView());
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception
	{
		return null;
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
	{
		this.referenceDataManager = referenceDataManager;
	}

	public void setMfsAccountManager(MfsAccountManager mfsAccountManager) {
		this.mfsAccountManager = mfsAccountManager;
	}

	public void setCustomerManager(CustomerManager customerManager) {
		this.customerManager = customerManager;
	}

	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}

	

}
