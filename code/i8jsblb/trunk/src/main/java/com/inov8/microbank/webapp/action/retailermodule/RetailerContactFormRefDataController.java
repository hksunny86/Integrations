package com.inov8.microbank.webapp.action.retailermodule;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.springframework.web.bind.ServletRequestUtils;

import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.microbank.common.model.RetailerModel;
import com.inov8.microbank.webapp.action.ajax.AjaxController;

public class RetailerContactFormRefDataController extends AjaxController{
    
	private ReferenceDataManager referenceDataManager;
	
	@Override
	public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("*******************************************************");
		
		RetailerModel retailerModel = new RetailerModel();
		List<RetailerModel> retailerModelList = null;
		try{
			//Long areaId = request.getAttribute(arg0)
			retailerModel.setAreaId(ServletRequestUtils.getRequiredLongParameter(request, "areaId"));
			retailerModel.setActive(true);
			ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
					retailerModel, "name", SortingOrder.ASC);
			referenceDataManager.getReferenceData(referenceDataWrapper);

			if (referenceDataWrapper.getReferenceDataList() != null)
			{
				retailerModelList = referenceDataWrapper.getReferenceDataList();
			}
			AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
			
			if( retailerModelList.size() == 0 )
				ajaxXmlBuilder.addItemAsCData("", "") ;
			
			return ajaxXmlBuilder.addItems(retailerModelList, "name", "retailerId").toString();
		}
		catch(Exception e){
			e.printStackTrace();
			String result = new AjaxXmlBuilder().addItemAsCData(" ", "").toString();
			return result;
		}

	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}
    
}
