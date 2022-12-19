package com.inov8.microbank.webapp.action.retailermodule;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.springframework.web.bind.ServletRequestUtils;

import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.webapp.action.ajax.AjaxController;

public class RetailerFormRefDataController extends AjaxController {
	private ReferenceDataManager referenceDataManager;

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	@Override
	public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("*******************************************************");

		DistributorModel distributorModel = new DistributorModel();
		List<DistributorModel> distributorModelList = null;
		try {
			// Long areaId = request.getAttribute(arg0)
			distributorModel.setAreaId(ServletRequestUtils.getRequiredLongParameter(request, "areaId"));
			distributorModel.setActive(true);
			ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(distributorModel, "name", SortingOrder.ASC);
			referenceDataManager.getReferenceData(referenceDataWrapper);

			if (referenceDataWrapper.getReferenceDataList() != null) {
				distributorModelList = referenceDataWrapper.getReferenceDataList();
				removeNationalDistributor(distributorModelList);
			}
			AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();

			if (distributorModelList.size() == 0)
				ajaxXmlBuilder.addItemAsCData("", "");

			return ajaxXmlBuilder.addItems(distributorModelList, "name", "distributorId").toString();
		} catch (Exception e) {
			e.printStackTrace();
			String result = new AjaxXmlBuilder().addItemAsCData(" ", "").toString();
			return result;
		}

	}

	private void removeNationalDistributor(List<DistributorModel> distributorModelList) {
		if (distributorModelList != null) {
			for (Iterator iter = distributorModelList.iterator(); iter.hasNext();) {
				DistributorModel distributor = (DistributorModel) iter.next();
				if (true == distributor.getNational()) {
					iter.remove();
				}
			}
		}

	}

}
