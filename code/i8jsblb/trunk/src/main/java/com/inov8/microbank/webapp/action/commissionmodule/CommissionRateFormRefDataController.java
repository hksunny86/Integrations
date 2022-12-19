package com.inov8.microbank.webapp.action.commissionmodule;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.springframework.web.bind.ServletRequestUtils;

import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.microbank.common.model.CommissionReasonModel;
import com.inov8.microbank.common.model.CommissionStakeholderModel;
import com.inov8.microbank.common.model.RetailerModel;
import com.inov8.microbank.common.model.StakeholderBankInfoModel;
import com.inov8.microbank.server.service.commissionstakeholdermodule.CommissionStakeholderManager;
import com.inov8.microbank.webapp.action.ajax.AjaxController;



public class CommissionRateFormRefDataController extends AjaxController {

	private ReferenceDataManager referenceDataManager;
	private CommissionStakeholderManager commissionStakeholderManager;
	
	public void setReferenceDataManager(
			ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	@Override
	public String getResponseContent(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

//		System.out.println("@@@@@@@@@@@@@@ getResponseContent() method called."
//				+ ServletRequestUtils.getRequiredStringParameter(request,
//						"actionType"));
		String actionType = ServletRequestUtils.getRequiredStringParameter(
				request, "actionType");
		AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();

		if ("1".equals(actionType)) {
			StakeholderBankInfoModel stakeholderBankInfoModel = new StakeholderBankInfoModel();
			stakeholderBankInfoModel
					.setCommissionStakeholderId(ServletRequestUtils
							.getRequiredLongParameter(request,
									"commissionStakeholderId"));
			stakeholderBankInfoModel.setActive(true);
			ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
					stakeholderBankInfoModel, "name", SortingOrder.ASC);
			referenceDataWrapper
					.setBasePersistableModel(stakeholderBankInfoModel);

			referenceDataManager.getReferenceData(referenceDataWrapper);
			List<StakeholderBankInfoModel> stakeholderBankInfoModelList = null;
			if (referenceDataWrapper.getReferenceDataList() != null) {
				stakeholderBankInfoModelList = referenceDataWrapper
						.getReferenceDataList();
			}

			if (stakeholderBankInfoModelList.size() == 0)
				ajaxXmlBuilder.addItemAsCData("", "");
			ajaxXmlBuilder.addItems(stakeholderBankInfoModelList, "name",
					"stakeholderBankInfoId").toString();

		}
		
		if ("2".equals(actionType)) {
			
			
			CommissionStakeholderModel commissionStakeholderModel = new CommissionStakeholderModel();
			commissionStakeholderModel.setCommissionStakeholderId(ServletRequestUtils.getRequiredLongParameter(request,"commissionStakeholderId"));
			
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			searchBaseWrapper.setBasePersistableModel(commissionStakeholderModel);

			// Load Reference Data For Commission Reason
			CommissionReasonModel commissionReasonModel = new CommissionReasonModel();
			commissionReasonModel.setActive(true);
			ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(commissionReasonModel,"name", SortingOrder.ASC);												
			referenceDataWrapper = new ReferenceDataWrapperImpl(commissionReasonModel, "name", SortingOrder.ASC);
			referenceDataWrapper.setBasePersistableModel(commissionReasonModel);
			try
			{
				referenceDataManager.getReferenceData(referenceDataWrapper);
			}
			catch (Exception e)
			{

			}
			List<CommissionReasonModel> commissionReasonModelList = null;
			if (referenceDataWrapper.getReferenceDataList() != null)
			{
				commissionReasonModelList = referenceDataWrapper.getReferenceDataList();
			}														
			
			List<CommissionReasonModel> tempCommissionReasonModelList = new ArrayList<CommissionReasonModel>();
			for (CommissionReasonModel model : commissionReasonModelList) {
				tempCommissionReasonModelList.add(model);
			}
			if(((CommissionStakeholderModel)commissionStakeholderManager.loadCommissionStakeholder(searchBaseWrapper).getBasePersistableModel()).getStakeholderTypeId()==5){				
				for (CommissionReasonModel model : commissionReasonModelList) {
						if(model.getCommissionReasonId()!=4 && model.getCommissionReasonId()!=8 && model.getCommissionReasonId()!=9)
							tempCommissionReasonModelList.remove(model);
				}						
			}
			else{
				for (CommissionReasonModel model : commissionReasonModelList) {
					if(model.getCommissionReasonId()==4 || model.getCommissionReasonId()==8 || model.getCommissionReasonId()==9)
						tempCommissionReasonModelList.remove(model);
				}										
			}
			commissionReasonModelList = tempCommissionReasonModelList;
			if (commissionReasonModelList.size() == 0)
				ajaxXmlBuilder.addItemAsCData("", "");
			
			ajaxXmlBuilder.addItems(commissionReasonModelList, "name","commissionReasonId").toString();			
			
		}
		if ("3".equals(actionType)){
			if ("".equals(request.getParameter("distributorId"))){
				ajaxXmlBuilder.addItemAsCData("", "");
				return ajaxXmlBuilder.toString();
			}
			Long distributorId = ServletRequestUtils.getRequiredLongParameter(request,"distributorId");
			if (distributorId != null){

				RetailerModel  retailerModel = new RetailerModel();
				retailerModel.setDistributorId(distributorId);
				retailerModel.setActive(true);
				ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
						retailerModel, "name", SortingOrder.ASC);
				referenceDataWrapper
						.setBasePersistableModel(retailerModel);

				referenceDataManager.getReferenceData(referenceDataWrapper);
				List<RetailerModel> retailerModelList = null;
				if (referenceDataWrapper.getReferenceDataList() != null) {
					retailerModelList = referenceDataWrapper
							.getReferenceDataList();
				}

				//if (retailerModelList.size() == 0)
					ajaxXmlBuilder.addItemAsCData("", "");
				ajaxXmlBuilder.addItems(retailerModelList, "name",
						"retailerId").toString();

			
			}
		}
		
		return ajaxXmlBuilder.toString();
	}
	
	public void setCommissionStakeholderManager(CommissionStakeholderManager commissionStakeholderManager){
		this.commissionStakeholderManager = commissionStakeholderManager;
	}
	
	
}
