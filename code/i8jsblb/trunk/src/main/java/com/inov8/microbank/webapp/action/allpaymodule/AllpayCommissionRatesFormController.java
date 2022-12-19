package com.inov8.microbank.webapp.action.allpaymodule;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import com.inov8.microbank.common.model.AllpayCommissionRateModel;
import com.inov8.microbank.common.model.AllpayCommissionReasonModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.RetailerModel;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.AllpayModule.AllpayCommissionRatesManager;

public class AllpayCommissionRatesFormController extends AdvanceFormController {
	private AllpayCommissionRatesManager allpayCommissionRatesManager;

	private ReferenceDataManager referenceDataManager;

	private Long id = null;

	public AllpayCommissionRatesFormController() {
		setCommandName("allpayCommissionRateModel");
		setCommandClass(AllpayCommissionRateModel.class);
	}
	  private void removeNationalDistributor(List<DistributorModel> distributorModelList) {
		  if (distributorModelList != null){
			for (Iterator iter = distributorModelList.iterator(); iter.hasNext();) {
				DistributorModel distributor =(DistributorModel)  iter.next();
				if (true == distributor.getNational()){
					iter.remove();
				}				
			}
	  }
			
		}
	@Override
	protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws Exception {
		DistributorModel nationalDistributor = getNationalDistributorModel ();
		
		id = ServletRequestUtils.getLongParameter(httpServletRequest, "allpayCommissionRateId");
		if (null != id) {
			if (log.isDebugEnabled()) {
				log.debug("id is not null....retrieving object from DB");
			}
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			AllpayCommissionRateModel commissionRateModel = new AllpayCommissionRateModel();
			commissionRateModel.setPrimaryKey(id);
			searchBaseWrapper.setBasePersistableModel(commissionRateModel);
			searchBaseWrapper = this.allpayCommissionRatesManager.loadCommissionRates(searchBaseWrapper);
			AllpayCommissionRateModel commRateModel = (AllpayCommissionRateModel) searchBaseWrapper.getBasePersistableModel();
			DecimalFormat dFormat = new DecimalFormat("###.##");
			// String rate = dFormat.format(commRateModel.getRate());
			// httpServletRequest.setAttribute("_rate", rate);
			//AllpayCommissionRateModel temp = (AllpayCommissionRateModel) searchBaseWrapper.getBasePersistableModel();
			commRateModel.setNationalDistributorName(nationalDistributor.getName());
			return commRateModel;
		} else {

			if (log.isDebugEnabled()) {
				log.debug("id is null....creating new instance of Model");
			}
			AllpayCommissionRateModel temp =  new AllpayCommissionRateModel();
			temp.setNationalDistributorId(nationalDistributor.getDistributorId());
			temp.setNationalDistributorName(nationalDistributor.getName());
			return temp;
		}
	}

	private DistributorModel getNationalDistributorModel() {		
		DistributorModel distributorModel = new DistributorModel();
		distributorModel.setActive(true);
		distributorModel.setNational(true);
		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(distributorModel, "name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(distributorModel);
		try {
			referenceDataManager.getReferenceData(referenceDataWrapper);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<DistributorModel> distributorModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null) {
			distributorModelList = referenceDataWrapper.getReferenceDataList();
		}
		return distributorModelList.get(0);	
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest request) throws Exception {
		Map referenceDataMap = new HashMap();
		ReferenceDataWrapper referenceDataWrapper = null;
		{
			// Load Reference Data For Product
			ProductModel productModel = new ProductModel();
			productModel.setActive(true);
			referenceDataWrapper = new ReferenceDataWrapperImpl(productModel, "name", SortingOrder.ASC);
			referenceDataWrapper.setBasePersistableModel(productModel);
			try {
				referenceDataManager.getReferenceData(referenceDataWrapper);
			} catch (Exception e) {

			}
			List<ProductModel> productModelList = null;
			if (referenceDataWrapper.getReferenceDataList() != null) {
				productModelList = referenceDataWrapper.getReferenceDataList();
			}
			referenceDataMap.put("productModelList", productModelList);
		}
		{
			// Load Reference Data For Distributor
			DistributorModel distributorModel = new DistributorModel();
			distributorModel.setActive(true);
			referenceDataWrapper = new ReferenceDataWrapperImpl(distributorModel, "name", SortingOrder.ASC);
			referenceDataWrapper.setBasePersistableModel(distributorModel);
			try {
				referenceDataManager.getReferenceData(referenceDataWrapper);
			} catch (Exception e) {
				e.printStackTrace();
			}
			List<DistributorModel> distributorModelList = null;
			if (referenceDataWrapper.getReferenceDataList() != null) {				
				distributorModelList = referenceDataWrapper.getReferenceDataList();
				removeNationalDistributor(distributorModelList);
			}
			referenceDataMap.put("distributorModelList", distributorModelList);
		}
		{
			RetailerModel retailerModel = new RetailerModel();
			if (id != null) {
				BaseWrapper bw = new BaseWrapperImpl();
				AllpayCommissionRateModel acrm = new AllpayCommissionRateModel();
				acrm.setAllpayCommissionRateId(id);
				bw.setBasePersistableModel(acrm);
				bw = this.allpayCommissionRatesManager.loadCommissionRates(bw);
				acrm = (AllpayCommissionRateModel) bw.getBasePersistableModel();
				retailerModel.setDistributorId(acrm.getDistributorId());
				// Load Reference Data For Retailer

				retailerModel.setActive(true);
				referenceDataWrapper = new ReferenceDataWrapperImpl(retailerModel, "name", SortingOrder.ASC);
				referenceDataWrapper.setBasePersistableModel(retailerModel);
				try {
					referenceDataManager.getReferenceData(referenceDataWrapper);
				} catch (Exception e) {

				}
				List<RetailerModel> retailerModelList = null;
				if (referenceDataWrapper.getReferenceDataList() != null) {
					retailerModelList = referenceDataWrapper.getReferenceDataList();
				}
				referenceDataMap.put("retailerModelList", retailerModelList);
			}
			
		}
		{
			// Load Reference Data For Allpay commission reasons
			AllpayCommissionReasonModel allpayCommissionReasonModel = new AllpayCommissionReasonModel();
			// allpayCommissionReasonModel.setActive(true);
			referenceDataWrapper = new ReferenceDataWrapperImpl(allpayCommissionReasonModel, "name", SortingOrder.ASC);
			referenceDataWrapper.setBasePersistableModel(allpayCommissionReasonModel);
			try {
				referenceDataManager.getReferenceData(referenceDataWrapper);
			} catch (Exception e) {
				e.printStackTrace();
			}
			List<AllpayCommissionReasonModel> allpayCommissionReasonModelList = null;
			if (referenceDataWrapper.getReferenceDataList() != null) {
				allpayCommissionReasonModelList = referenceDataWrapper.getReferenceDataList();
			}
			referenceDataMap.put("allpayCommissionReasonModelList", allpayCommissionReasonModelList);
		}
		return referenceDataMap;

	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		try{
		AllpayCommissionRateModel commissionRateModel = (AllpayCommissionRateModel) command;
		commissionRateModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		commissionRateModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		commissionRateModel.setActive(commissionRateModel.getActive() == null ? false : commissionRateModel.getActive());
		baseWrapper.setBasePersistableModel(commissionRateModel);
		baseWrapper = this.allpayCommissionRatesManager.createCommissionRates(baseWrapper);
		
			//System.out.println("asdf");
		
		if (null != baseWrapper.getBasePersistableModel()) {
			this.saveMessage(request, "Record saved successfully");
			ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
			return modelAndView;
		} else {
			return super.showForm(request, response, errors);
		}
	}catch (Exception e){
		if ("duplicateRecord".equals(e.getMessage())){
			this.saveMessage(request, "Duplicate record found");			
		}
		AllpayCommissionRateModel temp = ((AllpayCommissionRateModel)command);
		DistributorModel dm = getNationalDistributorModel();
		List<RetailerModel> retailerModelList =getRetailerModelList (temp.getDistributorId());
		request.setAttribute("retailerModelList", retailerModelList);
		temp.setNationalDistributorName(dm.getName());
		//loadFormBackingObject(request);
		//loadReferenceData(request);
		return super.showForm(request, response, errors);
	}
	
	}

	private List<RetailerModel> getRetailerModelList(Long distributorId) {
		RetailerModel retailerModel = new RetailerModel();
		retailerModel.setDistributorId(distributorId);
		// Load Reference Data For Retailer

		retailerModel.setActive(true);
		ReferenceDataWrapper referenceDataWrapper = null;
		referenceDataWrapper = new ReferenceDataWrapperImpl(retailerModel, "name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(retailerModel);
		try {
			referenceDataManager.getReferenceData(referenceDataWrapper);
		} catch (Exception e) {

		}
		List<RetailerModel> retailerModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null) {
			retailerModelList = referenceDataWrapper.getReferenceDataList();
		}
		return retailerModelList;
	}
	

	@Override
	protected ModelAndView onUpdate(HttpServletRequest httpServletRequest, HttpServletResponse response, Object object, BindException errors) throws Exception {
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		AllpayCommissionRateModel allpayCommissionRateModel = (AllpayCommissionRateModel) object;

		// String commissionReason =
		// httpServletRequest.getParameter("commissionReason");

		// if(commissionReason!=null && !commissionReason.equals(""))
		// commissionRateModel.setCommissionReasonId(Long.parseLong(commissionReason));

		try {
			Date nowDate = new Date();

			allpayCommissionRateModel.setUpdatedOn(nowDate);
			allpayCommissionRateModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			allpayCommissionRateModel.setActive(allpayCommissionRateModel.getActive() == null ? false : allpayCommissionRateModel.getActive());

			// *********** Load the commission rate model for the createdOn
			// problem
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			AllpayCommissionRateModel tempAllpayCommissionRateModel = new AllpayCommissionRateModel();
			tempAllpayCommissionRateModel.setPrimaryKey(id);
			searchBaseWrapper.setBasePersistableModel(allpayCommissionRateModel);
			tempAllpayCommissionRateModel = (AllpayCommissionRateModel) this.allpayCommissionRatesManager.loadCommissionRates(searchBaseWrapper).getBasePersistableModel();
			// ********************************************************************

			allpayCommissionRateModel.setCreatedOn(tempAllpayCommissionRateModel.getCreatedOn());
			allpayCommissionRateModel.setCreatedBy(tempAllpayCommissionRateModel.getCreatedBy());
			baseWrapper.setBasePersistableModel(allpayCommissionRateModel);
			baseWrapper = this.allpayCommissionRatesManager.updateCommissionRates(baseWrapper);

			if (null != baseWrapper.getBasePersistableModel()) {
				this.saveMessage(httpServletRequest, "Record saved successfully");
				ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
				return modelAndView;
			} else {
				this.saveMessage(httpServletRequest, "Commission Rate with same product, commission reason , from date, commission type and stakeholder already exists.");
				return super.showForm(httpServletRequest, response, errors);
			}
		} catch (FrameworkCheckedException ex) {
			DecimalFormat dFormat = new DecimalFormat("###.##");
			// String rate =
			// dFormat.format(allpayCommissionRateModel.getRate());
			// httpServletRequest.setAttribute("_rate", rate);
			// allpayCommissionRateModel.setRate(Double.valueOf(rate));
			if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION == ex.getErrorCode()) {
				super.saveMessage(httpServletRequest, "Record could not be updated");
				return super.showForm(httpServletRequest, response, errors);
			}
			throw ex;
		}
		catch (Exception ex) {
		
			super.saveMessage(httpServletRequest, MessageUtil.getMessage("6075"));
			return super.showForm(httpServletRequest, response, errors);
		}
	}

	public AllpayCommissionRatesManager getAllpayCommissionRatesManager() {
		return allpayCommissionRatesManager;
	}

	public void setAllpayCommissionRatesManager(AllpayCommissionRatesManager allpayCommissionRatesManager) {
		this.allpayCommissionRatesManager = allpayCommissionRatesManager;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ReferenceDataManager getReferenceDataManager() {
		return referenceDataManager;
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

}
