package com.inov8.microbank.webapp.action.portal.velocityrulemodule;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;

import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.microbank.common.model.DistributorLevelModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.server.service.agenthierarchy.AgentHierarchyManager;
import com.inov8.microbank.webapp.action.ajax.AjaxController;

public class DistributorReferenceDataController extends AjaxController
{
	private ReferenceDataManager referenceDataManager;
	
	@Autowired
	private AgentHierarchyManager	agentHierarchyManager;
	
	private final Log logger = LogFactory.getLog(this.getClass());

	public DistributorReferenceDataController()
	{
	}

	@SuppressWarnings("unchecked")
	public String getResponseContent(HttpServletRequest request,
			HttpServletResponse response)
	{

		/* This parameter specifies that what kind of reference data is required 
		 * Please refer to com.inov8.microbank.common.util.PortalConstants.REF_DATA_REQUEST_PARAM for parameter name
		 * and com.inov8.microbank.common.util.PortalConstants.REF_DATA_**** for the possible values
		 * 
		 * */
		if(logger.isDebugEnabled())
		{
			logger.debug("Start of getResponseContent of TransactionDetailSearchController");
			
		}
		
		 List<DistributorLevelModel> distributorLevelModelList = null;
		 DistributorLevelModel	distributorLevelModel = new DistributorLevelModel();
		 distributorLevelModel.setActive(true);
		
		try
		{
			Long distributorId = ServletRequestUtils.getRequiredLongParameter(request, "distributorId");
			if (null!= distributorId)
			{	
				// Comments by Rashid Startd
				/*distributorLevelModel.setDistributorId(distributorId);*/
				// Comments by Rashid Ends
				SearchBaseWrapper searchBaseWrapper=new SearchBaseWrapperImpl();
				searchBaseWrapper.setBasePersistableModel(new DistributorModel(distributorId));
				searchBaseWrapper	=	agentHierarchyManager.findDistributorLevelsByDistributorId(searchBaseWrapper);
				
				if(searchBaseWrapper.getCustomList()!=null && searchBaseWrapper.getCustomList().getResultsetList()!=null )
				{
					distributorLevelModelList	=	searchBaseWrapper.getCustomList().getResultsetList();
				}
				
			}
			AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
			ajaxXmlBuilder.addItemAsCData("---All---", "");
			
			if(logger.isDebugEnabled())
			{
				logger.debug("end of getResponseContent of TransactionDetailSearchController");
				
			}
			return ajaxXmlBuilder.addItems(distributorLevelModelList, "name", "distributorLevelId").toString();
			
		}

		catch (Exception e)
		{
			e.printStackTrace();
			String result = new AjaxXmlBuilder().addItemAsCData("---All---", "").toString();
			return result;
		}

	}

	public void setReferenceDataManager(
			ReferenceDataManager referenceDataManager)
	{
		this.referenceDataManager = referenceDataManager;
	}

	public void setAgentHierarchyManager(AgentHierarchyManager agentHierarchyManager) {
		this.agentHierarchyManager = agentHierarchyManager;
	}
	
}

