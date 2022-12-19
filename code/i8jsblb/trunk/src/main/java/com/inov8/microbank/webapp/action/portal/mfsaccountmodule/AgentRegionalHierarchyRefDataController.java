package com.inov8.microbank.webapp.action.portal.mfsaccountmodule;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.ServletRequestUtils;

import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.microbank.common.model.AreaModel;
import com.inov8.microbank.server.service.agenthierarchy.AgentHierarchyManager;
import com.inov8.microbank.webapp.action.ajax.AjaxController;

public class AgentRegionalHierarchyRefDataController extends AjaxController
{
	private ReferenceDataManager referenceDataManager;
	private AgentHierarchyManager agentHierarchyManager;
	
	private final Log logger = LogFactory.getLog(this.getClass());

	public AgentRegionalHierarchyRefDataController()
	{
	}

	@SuppressWarnings("unchecked")
	public String getResponseContent(HttpServletRequest request,
			HttpServletResponse response)
	{

		if(logger.isDebugEnabled())
		{
			logger.debug("Start of getResponseContent of AgentRegionalHierarchyRefDataController");
			
		}
		
		try{
			AreaModel areaModel = new AreaModel();
			areaModel.setActive(Boolean.TRUE);
			List<AreaModel> areaModelList = null;
			Boolean isArea = ServletRequestUtils.getRequiredBooleanParameter(request, "isArea");
			if(isArea){
				SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
				searchBaseWrapper = agentHierarchyManager.findAgentAreaByRegionId(ServletRequestUtils.getRequiredLongParameter(request, "parentId"));
				areaModelList = searchBaseWrapper.getCustomList().getResultsetList();
			}else{
				areaModel.setParentAreaId(ServletRequestUtils.getRequiredLongParameter(request, "parentId"));
				ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(areaModel, "name", SortingOrder.ASC);
				
				referenceDataManager.getReferenceData(referenceDataWrapper);
				
				if (referenceDataWrapper.getReferenceDataList() != null){
					areaModelList = referenceDataWrapper.getReferenceDataList();
				}				
			}

			AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
			ajaxXmlBuilder.addItemAsCData("[Select]", "");
			
			if(logger.isDebugEnabled()){
				logger.debug("end of getResponseContent of AgentRegionalHierarchyRefDataController");
				
			}
			return ajaxXmlBuilder.addItems(areaModelList, "name", "areaId").toString();
		}catch (Exception e){
			e.printStackTrace();
			String result = new AjaxXmlBuilder().addItemAsCData("[Select]", "").toString();
			return result;
		}
	}

	public void setReferenceDataManager(
			ReferenceDataManager referenceDataManager)
	{
		this.referenceDataManager = referenceDataManager;
	}

	public void setAgentHierarchyManager(AgentHierarchyManager agentHierarchyManager) {
		if (agentHierarchyManager != null) {
			this.agentHierarchyManager = agentHierarchyManager;
		}
	}
}
