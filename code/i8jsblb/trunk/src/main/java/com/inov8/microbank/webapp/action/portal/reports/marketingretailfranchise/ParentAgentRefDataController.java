package com.inov8.microbank.webapp.action.portal.reports.marketingretailfranchise;

import java.util.List;

import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.apache.commons.validator.GenericValidator;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.DistributorLevelModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.util.JSFContext;
import com.inov8.microbank.server.service.agenthierarchy.AgentHierarchyManager;
import com.inov8.microbank.server.service.distributormodule.DistributorLevelManager;
import com.inov8.microbank.webapp.action.ajax.AjaxController;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Feb 17, 2013 12:19:15 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class ParentAgentRefDataController extends AjaxController
{
    //Autowired
    private DistributorLevelManager distributorLevelManager;
    private AgentHierarchyManager agentHierarchyManager;
    private List<RetailerContactModel> parentAgentModelList;

    @SuppressWarnings( "unchecked" )
    @Override
    public String getResponseContent( HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
        if( logger.isDebugEnabled() )
        {
            logger.debug("Start of getResponseContent of ParentAgentRefDataController");
        }
        AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
        ajaxXmlBuilder.addItemAsCData("[Select]", "");

        String agentLevelIdStr = request.getParameter( "agentLevelId" );
        String retailerIdStr = request.getParameter( "retailerId" );
        if( !GenericValidator.isBlankOrNull( agentLevelIdStr ) && !GenericValidator.isBlankOrNull( retailerIdStr ) )
        {
            long agentLevelId = Long.parseLong( agentLevelIdStr );
            long retailerId = Long.parseLong( retailerIdStr );
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            DistributorLevelModel distributorLevelModel = new DistributorLevelModel();
            distributorLevelModel.setDistributorLevelId(agentLevelId);
            baseWrapper.setBasePersistableModel(distributorLevelModel);
            baseWrapper = distributorLevelManager.loadDistributorLevel(baseWrapper);
            distributorLevelModel =  (DistributorLevelModel) baseWrapper.getBasePersistableModel();
            Long parentAgentLevel = distributorLevelModel.getManagingLevelId();
			if(parentAgentLevel != null) {
				SearchBaseWrapper searchBaseWrapper = agentHierarchyManager.findParentAgents(parentAgentLevel, retailerId);
				if(searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0) {
					parentAgentModelList = searchBaseWrapper.getCustomList().getResultsetList();
					ajaxXmlBuilder.addItems( parentAgentModelList, "name", "retailerContactId" );
				}
			}
        }

        if( logger.isDebugEnabled() )
        {
            logger.debug("End of getResponseContent of AgentLevelRefDataController");
        }
        return ajaxXmlBuilder.toString();
    }

	public void setDistributorLevelManager(
			DistributorLevelManager distributorLevelManager) {
		if (distributorLevelManager != null) {
			this.distributorLevelManager = distributorLevelManager;
		}
	}

	public void setAgentHierarchyManager(AgentHierarchyManager agentHierarchyManager) {
		if (agentHierarchyManager != null) {
			this.agentHierarchyManager = agentHierarchyManager;
		}
	}

}
