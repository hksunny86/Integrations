package com.inov8.microbank.webapp.action.portal.reports.marketingretailfranchise;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.apache.commons.validator.GenericValidator;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.DistributorLevelModel;
import com.inov8.microbank.server.service.agenthierarchy.AgentHierarchyManager;
import com.inov8.microbank.webapp.action.ajax.AjaxController;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Feb 17, 2013 12:19:15 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class AgentLevelRefDataController extends AjaxController
{
    //Autowired
    private AgentHierarchyManager agentHierarchyManager;

    @SuppressWarnings( "unchecked" )
    @Override
    public String getResponseContent( HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
        if( logger.isDebugEnabled() )
        {
            logger.debug("Start of getResponseContent of AgentLevelRefDataController");
        }
        AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
        ajaxXmlBuilder.addItemAsCData("[Select]", "");

        String regionIdStr = request.getParameter( "regionId" );
        if( !GenericValidator.isBlankOrNull( regionIdStr ) )
        {
            long regionId = Long.parseLong( regionIdStr );
            SearchBaseWrapper wrapper = agentHierarchyManager.findAgentLevelsByRegionId( regionId );

            CustomList<DistributorLevelModel> customList = wrapper.getCustomList();
            if( customList != null )
            {
                List<DistributorLevelModel> distributorLevelModelList = customList.getResultsetList();
                ajaxXmlBuilder.addItems( distributorLevelModelList, "name", "distributorLevelId" );
            }
        }

        if( logger.isDebugEnabled() )
        {
            logger.debug("End of getResponseContent of AgentLevelRefDataController");
        }
        return ajaxXmlBuilder.toString();
    }

    public void setAgentHierarchyManager( AgentHierarchyManager agentHierarchyManager )
    {
        this.agentHierarchyManager = agentHierarchyManager;
    }

}
