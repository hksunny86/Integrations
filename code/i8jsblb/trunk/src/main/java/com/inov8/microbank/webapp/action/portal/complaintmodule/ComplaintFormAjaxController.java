package com.inov8.microbank.webapp.action.portal.complaintmodule;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.springframework.web.bind.ServletRequestUtils;

import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.microbank.common.model.ComplaintSubcategoryModel;
import com.inov8.microbank.webapp.action.ajax.AjaxController;

public class ComplaintFormAjaxController extends AjaxController{
    
	private ReferenceDataManager referenceDataManager;
//	private ComplaintManager complaintManager;
	
	@Override
	public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ComplaintSubcategoryModel complaintSubcategoryModel = new ComplaintSubcategoryModel();
//		ComplaintParameterModel complaintParameterModel = new ComplaintParameterModel();
		List<ComplaintSubcategoryModel> complaintSubcategoryModelList = null;
//		List<ComplaintParameterModel> complaintParamList = null;
		try{
			complaintSubcategoryModel.setComplaintCategoryId(ServletRequestUtils.getRequiredLongParameter(request, "complaintCategoryId"));
			complaintSubcategoryModel.setIsActive(true);
			ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
					complaintSubcategoryModel, "name", SortingOrder.ASC);
			referenceDataManager.getReferenceData(referenceDataWrapper);
//			CustomList<ComplaintSubcategoryModel> subcategoryList = complaintManager.searchComplaintSubcategory(complaintSubcategoryModel);
//			if(subcategoryList != null){
//				complaintSubcategoryModelList = subcategoryList.getResultsetList();
//			}
			if (referenceDataWrapper.getReferenceDataList() != null){
				complaintSubcategoryModelList = referenceDataWrapper.getReferenceDataList();
			}

			
			
//			complaintParameterModel.setcomplaintCategoryId(ServletRequestUtils.getRequiredLongParameter(request, "complaintCategoryId"));
//			complaintParameterModel.setIsActive(true);
//			referenceDataWrapper = new ReferenceDataWrapperImpl(complaintParameterModel, "parameterName", SortingOrder.ASC);
//			referenceDataManager.getReferenceData(referenceDataWrapper);
//			if (referenceDataWrapper.getReferenceDataList() != null){
//				complaintParamList = referenceDataWrapper.getReferenceDataList();
//			}
			
			
			
			
			AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
			
			if( complaintSubcategoryModelList.size() == 0 )
				ajaxXmlBuilder.addItemAsCData("", "") ;
			
			return ajaxXmlBuilder.addItems(complaintSubcategoryModelList, "name", "complaintSubcategoryId").toString();
		}catch(Exception e){
			e.printStackTrace();
			String result = new AjaxXmlBuilder().addItemAsCData(" ", "").toString();
			return result;
		}

	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}
	
//	public void setComplaintManager(ComplaintManager complaintManager) {
//		this.complaintManager = complaintManager;
//	}
    
}
