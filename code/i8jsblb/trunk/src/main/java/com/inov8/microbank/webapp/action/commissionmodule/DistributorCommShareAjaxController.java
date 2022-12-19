package com.inov8.microbank.webapp.action.commissionmodule;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.microbank.common.model.agenthierarchy.DistributorCommissionShareDetailModel;
import com.inov8.microbank.common.model.agenthierarchy.DistributorCommissionShareModel;
import com.inov8.microbank.server.service.agenthierarchy.AgentNetworkCommShareManager;
import com.inov8.microbank.webapp.action.ajax.AjaxController;

public class DistributorCommShareAjaxController extends AjaxController {

	private ReferenceDataManager referenceDataManager;
	private AgentNetworkCommShareManager agentNetworkCommShareManager;

	@Override
	public String getResponseContent(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String distributorId = ServletRequestUtils.getStringParameter(request,
				"distributorId");
		String regionId = ServletRequestUtils.getStringParameter(request,
				"regionId");
		String productId = ServletRequestUtils.getStringParameter(request,
				"productId");
		String currentLevelId = ServletRequestUtils.getStringParameter(request,
				"currentLevelId");

		DistributorCommissionShareModel model = new DistributorCommissionShareModel();

		if (StringUtils.isNotEmpty(productId))
		{
			model.setProductId(Long.parseLong(productId));
		}
		else
		{
			model.setProductId(null);
		}
		if (StringUtils.isNotEmpty(currentLevelId))
		{
			model.setCurrentDistributorLevelId(Long.parseLong(currentLevelId));
		}
		if (StringUtils.isNotEmpty(distributorId))
		{
			model.setDistributorId(Long.parseLong(distributorId));
		}
		if (StringUtils.isNotEmpty(regionId))
		{
			model.setRegionId(Long.parseLong(regionId));
		}

		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel(model);
		
		CustomList<DistributorCommissionShareModel> list = agentNetworkCommShareManager
				.loadDistributorCommissionShare(searchBaseWrapper).getCustomList();

		StringBuilder xml=	new StringBuilder();

		if (list != null) {
			List<DistributorCommissionShareModel> distCommShModelList = list.getResultsetList();

			if (distCommShModelList != null && distCommShModelList.size() > 0) 
			{
				xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
				xml.append("<data>");

				DistributorCommissionShareModel distCommShareModel = distCommShModelList.get(0);
				
				xml.append("<item><name>").append("parentResult").append("</name>");
				xml.append("<value>").append(String.valueOf(distCommShareModel.getCommissionShare().doubleValue())).append("</value>");
				xml.append("</item>");

				xml.append("<item><name>").append("pk").append("</name>");
				xml.append("<value>").append(String.valueOf(distCommShareModel.getDistributorCommissionShareId())).append("</value>");
				xml.append("</item>");
				
				DistributorCommissionShareDetailModel distCommShareDetModel = new DistributorCommissionShareDetailModel();
				distCommShareDetModel.setDistributorCommissionShareIdModel(distCommShareModel);
				searchBaseWrapper = new SearchBaseWrapperImpl();
				searchBaseWrapper.setBasePersistableModel(distCommShareDetModel);

				CustomList<DistributorCommissionShareDetailModel> dtlList = agentNetworkCommShareManager.loadDistributorCommissionShareDetail(searchBaseWrapper).getCustomList();
				if (dtlList != null) 
				{
					List<DistributorCommissionShareDetailModel> distCommShDetailModelList = dtlList.getResultsetList();
					
					if (distCommShDetailModelList != null && distCommShDetailModelList.size() > 0) 
					{
						for (DistributorCommissionShareDetailModel dtl : distCommShDetailModelList) {
							xml.append("<item><name>").append("childResult").append("</name>");
							xml.append("<value>").append(String.valueOf(dtl.getCommissionShare())).append("</value>");
							xml.append("</item>");
						}
					}
				}
				
			}
			return xml.append("</data>").toString();
		}
		return "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><data></data>";
	}

	public ReferenceDataManager getReferenceDataManager() {
		return referenceDataManager;
	}

	public void setReferenceDataManager(
			ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public AgentNetworkCommShareManager getAgentNetworkCommShareManager() {
		return agentNetworkCommShareManager;
	}

	public void setAgentNetworkCommShareManager(
			AgentNetworkCommShareManager agentNetworkCommShareManager) {
		this.agentNetworkCommShareManager = agentNetworkCommShareManager;
	}
}
