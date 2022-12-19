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
import com.inov8.microbank.common.model.DistributorLevelModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.distributormodule.DistributorLevelListViewModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.distributormodule.DistributorLevelManager;

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

public class DistributorLevelFormController extends AdvanceFormController {
	private DistributorLevelManager distributorLevelManager;

	private ReferenceDataManager referenceDataManager;

	private Long id;

	public DistributorLevelFormController() {
		setCommandName("distributorLevelModel");
		setCommandClass(DistributorLevelModel.class);
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
		Map referenceDataMap = new HashMap();
		DistributorModel distributorModel = new DistributorModel();
		distributorModel.setActive(true);
		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
				distributorModel, "name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(distributorModel);
		referenceDataManager.getReferenceData(referenceDataWrapper);
		List<DistributorModel> distributorModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null) {
			distributorModelList = referenceDataWrapper.getReferenceDataList();
			referenceDataMap.put("distributorModelList", distributorModelList);
		}

		if (id == null) {
			if (distributorModelList != null && distributorModelList.size() > 0) {
				DistributorLevelListViewModel distributorLevelListViewModel = new DistributorLevelListViewModel();
				List<DistributorLevelListViewModel> distributorLevelModelList = null;

				distributorLevelListViewModel
						.setDistributorId(((DistributorModel) distributorModelList
								.get(0)).getDistributorId());
				//System.out.println( "**************" + ServletRequestUtils.getRequiredLongParameter(request, "distributorLevelId") );
				referenceDataWrapper = new ReferenceDataWrapperImpl(
						distributorLevelListViewModel, "ultimateLevelName",
						SortingOrder.ASC);
				referenceDataManager.getReferenceData(referenceDataWrapper);

				if (referenceDataWrapper.getReferenceDataList() != null) {
					distributorLevelModelList = referenceDataWrapper
							.getReferenceDataList();
					if (distributorLevelModelList != null
							&& distributorLevelModelList.size() > 0) {
						distributorLevelListViewModel = distributorLevelModelList
								.get(0);
						if (distributorLevelListViewModel
								.getUltimateManagingLevelId() != null) {
							httpServletRequest.setAttribute("ultamateLevelId",
									distributorLevelListViewModel
											.getUltimateManagingLevelId());
							httpServletRequest.setAttribute(
									"ultamateLevelName",
									distributorLevelListViewModel
											.getUltimateLevelName());
						} else {
							/*httpServletRequest.setAttribute("ultamateLevelId", " ");
							 httpServletRequest.setAttribute("ultamateLevelName", " ");*/
							httpServletRequest.setAttribute("ultamateLevelId",
									distributorLevelListViewModel
											.getDistributorLevelId());
							httpServletRequest.setAttribute(
									"ultamateLevelName",
									distributorLevelListViewModel
											.getDistributorLevelName());

						}

					}
					/*else
					 {
					 httpServletRequest.setAttribute("ultamateLevelId", " ");
					 httpServletRequest.setAttribute("ultamateLevelName", " ");
					 
					 }*/

				}

			} 

		

		/*referenceDataMap.put("managingDistributorLevelModelList",
		 distributorLevelModelList);*/

		/**
		 * code fragment to load reference data for Managing Distributor Level
		 */
		if (distributorModelList != null && distributorModelList.size() > 0) {
			DistributorLevelModel distributorLevelModel = new DistributorLevelModel();
			
			// commented by Rashid Starts
			/*distributorLevelModel.setDistributorId(((DistributorModel) distributorModelList.get(0)).getDistributorId());*/
			// commented by Rashid Ends
			
			
			
						referenceDataWrapper = new ReferenceDataWrapperImpl(
					distributorLevelModel, "name", SortingOrder.ASC);
			referenceDataWrapper.setBasePersistableModel(distributorLevelModel);

			referenceDataManager.getReferenceData(referenceDataWrapper);
			List<DistributorLevelModel> managingDistributorLevelModelList = null;
			if (referenceDataWrapper.getReferenceDataList() != null) {
				managingDistributorLevelModelList = referenceDataWrapper
						.getReferenceDataList();
				referenceDataMap.put("managingDistributorLevelModelList",
						managingDistributorLevelModelList);
			}

		}
		}
		return referenceDataMap;

	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest httpServletRequest)
			throws Exception {
		id = ServletRequestUtils.getLongParameter(httpServletRequest,
				"distributorLevelId");
		if (null != id) {
			if (log.isDebugEnabled()) {
				log.debug("id is not null....retrieving object from DB");
			}

			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			DistributorLevelModel distributorLevelModel = new DistributorLevelModel();
			DistributorLevelModel ultimateDistributorLevelModel = new DistributorLevelModel();
			distributorLevelModel.setDistributorLevelId(id);
			searchBaseWrapper.setBasePersistableModel(distributorLevelModel);
			searchBaseWrapper = this.distributorLevelManager
					.loadDistributorLevel(searchBaseWrapper);
			distributorLevelModel = (DistributorLevelModel) searchBaseWrapper
					.getBasePersistableModel();
			if (distributorLevelModel.getUltimateManagingLevelId() != null) {
				ultimateDistributorLevelModel
						.setDistributorLevelId(distributorLevelModel
								.getUltimateManagingLevelId());
				searchBaseWrapper
						.setBasePersistableModel(ultimateDistributorLevelModel);
				searchBaseWrapper = this.distributorLevelManager
						.loadDistributorLevel(searchBaseWrapper);

				ultimateDistributorLevelModel = (DistributorLevelModel) searchBaseWrapper
						.getBasePersistableModel();
				httpServletRequest.setAttribute("ultamateLevelId",
						distributorLevelModel.getUltimateManagingLevelId());
				httpServletRequest.setAttribute("ultamateLevelName",
						ultimateDistributorLevelModel.getName());

			} else {
				httpServletRequest.setAttribute("ultamateLevelId", "");
				httpServletRequest.setAttribute("ultamateLevelName", "");

			}
			
				DistributorLevelModel distributorManagingLevelModel = new DistributorLevelModel();
			
			// Commented by Rashid Starts
				/*distributorManagingLevelModel.setDistributorId(distributorLevelModel.getDistributorId());*/
			// Commented by Rashid Ends
			
				ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
					distributorLevelModel, "name", SortingOrder.ASC);
			referenceDataWrapper.setBasePersistableModel(distributorManagingLevelModel);

			referenceDataManager.getReferenceData(referenceDataWrapper);
			List<DistributorLevelModel> managingDistributorLevelModelList = null;
			if (referenceDataWrapper.getReferenceDataList() != null) {
				managingDistributorLevelModelList = referenceDataWrapper
						.getReferenceDataList();
				httpServletRequest.setAttribute("managingDistributorLevelModelList", managingDistributorLevelModelList);
				
			}

			

			return distributorLevelModel;
		} else {
			if (log.isDebugEnabled()) {
				log.debug("id is null....creating new instance of Model");
			}

			DistributorLevelModel model = new DistributorLevelModel();

			model.setComments("");

			return model;
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

	private ModelAndView createOrUpdate(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		try {
			BaseWrapper baseWrapper = new BaseWrapperImpl();

			long theDate = new Date().getTime();
			DistributorLevelModel distributorLevelModel = (DistributorLevelModel) command;
			if (null != id) {
				DistributorLevelModel dbutorLevelModel = new DistributorLevelModel();
				dbutorLevelModel.setDistributorLevelId(id);
				baseWrapper.setBasePersistableModel(dbutorLevelModel);
				baseWrapper = this.distributorLevelManager
						.loadDistributorLevel(baseWrapper);
				dbutorLevelModel = (DistributorLevelModel) baseWrapper
						.getBasePersistableModel();
				distributorLevelModel.setCreatedOn(dbutorLevelModel
						.getCreatedOn());
				distributorLevelModel.setUpdatedOn(new Date(theDate));
				distributorLevelModel.setUpdatedByAppUserModel(UserUtils
						.getCurrentUser());
				baseWrapper
						.setBasePersistableModel((DistributorLevelModel) command);
				baseWrapper = this.distributorLevelManager
						.updateDistributorLevel(baseWrapper);
			} else {
				distributorLevelModel.setUpdatedOn(new Date(theDate));
				distributorLevelModel.setCreatedOn(new Date(theDate));
				distributorLevelModel.setUpdatedByAppUserModel(UserUtils
						.getCurrentUser());
				distributorLevelModel.setCreatedByAppUserModel(UserUtils
						.getCurrentUser());
				baseWrapper
						.setBasePersistableModel((DistributorLevelModel) command);
				baseWrapper = this.distributorLevelManager
						.createOrUpdateDistributorLevel(baseWrapper);
			}

			//***Check if record already exists.
			if (null != baseWrapper.getBasePersistableModel()) { //if not found
				this.saveMessage(request, "Record saved successfully");
				ModelAndView modelAndView = new ModelAndView(this
						.getSuccessView());
				return modelAndView;
			} else {
				this.saveMessage(request,
						"Distributor Level name already exists.");
				return super.showForm(request, response, errors);
			}
		} catch (FrameworkCheckedException ex) {
			if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION == ex
					.getErrorCode()) {

				super.saveMessage(request,
						"Distributor Level name already exists.");
				return super.showForm(request, response, errors);
			} else if (ex.getMessage().equals("NoDistributorExist")) {
				super.saveMessage(request, "No distributor exist.");
				return super.showForm(request, response, errors);
			} else {

				super.saveMessage(request, "Record could not be saved.");
				return super.showForm(request, response, errors);
			}
		}catch (Exception ex) {
			super.saveMessage(request, "Record could not be saved.");
			return super.showForm(request, response, errors);
		}
	}

	public void setDistributorLevelManager(
			DistributorLevelManager distributorLevelManager) {
		this.distributorLevelManager = distributorLevelManager;
	}

	public void setReferenceDataManager(
			ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

}
