package com.inov8.microbank.webapp.action.distributormodule;

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
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.AreaModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.PermissionGroupModel;
import com.inov8.microbank.common.util.DistributorConstants;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.distributormodule.DistributorManager;

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

public class DistributorFormController extends AdvanceFormController {
	private DistributorManager distributorManager;

	private ReferenceDataManager referenceDataManager;

	private Long id;

	public DistributorFormController() {
		setCommandName("distributorModel");
		setCommandClass(DistributorModel.class);
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest httpServletRequest)
			throws FrameworkCheckedException {
		if (log.isDebugEnabled()) {
			log.debug("Inside reference data");
		}

		/**
		 * code fragment to load reference data  for Distributor
		 *
		 */

		AreaModel areaModel = new AreaModel();
		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
				areaModel, "name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(areaModel);
		referenceDataManager.getReferenceData(referenceDataWrapper);
		List<AreaModel> areaModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null) {
			areaModelList = referenceDataWrapper.getReferenceDataList();
		}

		Map referenceDataMap = new HashMap();
		referenceDataMap.put("arearModelList", areaModelList);
		
		//load permission group for distributor
	    PermissionGroupModel permissionGroupModel = new PermissionGroupModel();
		permissionGroupModel.setAppUserTypeId(UserTypeConstantsInterface.DISTRIBUTOR);
		referenceDataWrapper = new ReferenceDataWrapperImpl(
				permissionGroupModel, "name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(permissionGroupModel);
		try {
		referenceDataManager.getReferenceData(referenceDataWrapper);
		} catch (FrameworkCheckedException ex1) {
			ex1.getStackTrace();
		}
		List<PermissionGroupModel> permissionGroupModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null) 
		{
			permissionGroupModelList = referenceDataWrapper.getReferenceDataList();
		}	
		referenceDataMap.put("permissionGroupModelList", permissionGroupModelList);	 		
		
		return referenceDataMap;
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest httpServletRequest)
			throws Exception {
		id = ServletRequestUtils.getLongParameter(httpServletRequest,
				"distributorId");
		if (null != id) {
			if (log.isDebugEnabled()) {
				log.debug("id is not null....retrieving object from DB");
			}

			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			DistributorModel distributorModel = new DistributorModel();
			distributorModel.setDistributorId(id);
			searchBaseWrapper.setBasePersistableModel(distributorModel);
			searchBaseWrapper = this.distributorManager
					.loadDistributor(searchBaseWrapper);
			
            Long permissionGroupIdInReq = (Long)searchBaseWrapper.getObject("permissionGroupId");
            httpServletRequest.setAttribute("permissionGroupIdInReq", permissionGroupIdInReq.toString());			
			
			return (DistributorModel) searchBaseWrapper
					.getBasePersistableModel();
		} else {
			if (log.isDebugEnabled()) {
				log.debug("id is null....creating new instance of Model");
			}

			return new DistributorModel();
		}
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object object,
			BindException bindException) throws Exception {
		return this.createOrUpdate(httpServletRequest, httpServletResponse,
				object, bindException);
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object object,
			BindException bindException) throws Exception {
		return this.createOrUpdate(httpServletRequest, httpServletResponse,
				object, bindException);
	}

	/**
	 *
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param command Object
	 * @param errors BindException
	 * @return ModelAndView
	 * @throws Exception
	 */
	private ModelAndView createOrUpdate(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		try {
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			long theDate = new Date().getTime();
			DistributorModel distributorModel = (DistributorModel) command;
			if (null != id) {

				DistributorModel tempDistributorModel = new DistributorModel();
				tempDistributorModel.setDistributorId(id);
				baseWrapper.setBasePersistableModel(tempDistributorModel);
				baseWrapper = this.distributorManager
						.loadDistributor(baseWrapper);
				tempDistributorModel = (DistributorModel) baseWrapper
						.getBasePersistableModel();
				distributorModel.setCreatedByAppUserModel(tempDistributorModel
						.getCreatedByAppUserModel());
				distributorModel.setCreatedOn(tempDistributorModel
						.getCreatedOn());
				distributorModel.setUpdatedOn(new Date(theDate));
				distributorModel.setUpdatedByAppUserModel(UserUtils
						.getCurrentUser());

			} else {
		   	    Long permissionGroupId = ServletRequestUtils.getLongParameter(request, "permissionGroupId");
		   	    baseWrapper.putObject("permissionGroupId", permissionGroupId);
				
				distributorModel.setUpdatedOn(new Date(theDate));
				distributorModel.setCreatedOn(new Date(theDate));
				distributorModel.setUpdatedByAppUserModel(UserUtils
						.getCurrentUser());
				distributorModel.setCreatedByAppUserModel(UserUtils
						.getCurrentUser());
			}

			distributorModel
					.setActive(distributorModel.getActive() == null ? false
							: distributorModel.getActive());
			distributorModel
			.setNational(distributorModel.getNational() == null ? false
					: distributorModel.getNational());

			baseWrapper.setBasePersistableModel(distributorModel);
			baseWrapper = this.distributorManager
					.createOrUpdateDistributor(baseWrapper);
			
			//***Check if record already exists.
			if (null != baseWrapper.getBasePersistableModel()) { //if not found
				this.saveMessage(request, "Record saved successfully");
				ModelAndView modelAndView = new ModelAndView(this
						.getSuccessView());
				return modelAndView;
			} else {
				this.saveMessage(request,
						"Distributor with the same name already exists.");
				return super.showForm(request, response, errors);
			}
		} catch (FrameworkCheckedException ex) {
			if (DistributorConstants.NATIONAL_DISTRIBUTOR_VIOLATION.equals(ex
					.getMessage()) ||  DistributorConstants.NATIONAL_DISTRIBUTOR_CHK.equals(ex
							.getMessage())  ||  DistributorConstants.NATIONAL_DISTRIBUTOR_ACTIVE_CHK.equals(ex
									.getMessage())){
				super.saveMessage(request, ex.getMessage());
				return super.showForm(request, response, errors);
			}
			if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION == ex
					.getErrorCode()) {

				super.saveMessage(request, "Record could not be saved.");
				return super.showForm(request, response, errors);
			}

			throw ex;
		}
		catch (Exception ex) {
			
			super.saveMessage(request, MessageUtil.getMessage("6075"));
			return super.showForm(request, response, errors);
		}

	}

	public void setDistributorManager(DistributorManager distributorManager) {
		this.distributorManager = distributorManager;
	}

	public void setReferenceDataManager(
			ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

}
