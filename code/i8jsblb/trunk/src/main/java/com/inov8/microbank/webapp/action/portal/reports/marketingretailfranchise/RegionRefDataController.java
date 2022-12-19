package com.inov8.microbank.webapp.action.portal.reports.marketingretailfranchise;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.apache.commons.validator.GenericValidator;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.agenthierarchy.RegionModel;
import com.inov8.microbank.server.service.agenthierarchy.AgentHierarchyManager;
import com.inov8.microbank.webapp.action.ajax.AjaxController;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Feb 17, 2013 11:57:20 AM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class RegionRefDataController extends AjaxController
{
    //Autowired
    private AgentHierarchyManager agentHierarchyManager;

    @SuppressWarnings( "unchecked" )
    @Override
    public String getResponseContent( HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
        if( logger.isDebugEnabled() )
        {
            logger.debug("Start of getResponseContent of RegionRefDataController");
        }
        AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
        ajaxXmlBuilder.addItemAsCData("[Select]", "");

        String distributorIdStr = request.getParameter( "distributorId" );
        if( !GenericValidator.isBlankOrNull( distributorIdStr ) )
        {
            long distributorId = Long.parseLong( distributorIdStr );
            SearchBaseWrapper wrapper = agentHierarchyManager.findRegionsByDistributorId( distributorId );

            CustomList<RegionModel> customList = wrapper.getCustomList();
            if( customList != null )
            {
                List<RegionModel> regionModelList = customList.getResultsetList();

                ajaxXmlBuilder.addItems( regionModelList, "regionName", "regionId" );
            }
        }

        String regionalHierarchicalId = request.getParameter( "regionalHierarchicalId" );
        if( !GenericValidator.isBlankOrNull( regionalHierarchicalId ) )
        {
            SearchBaseWrapper wrapper = agentHierarchyManager.findRegionsByRegionalHierarchyId(Long.parseLong(regionalHierarchicalId));

            CustomList<RegionModel> customList = wrapper.getCustomList();
            if( customList != null )
            {
                List<RegionModel> regionModelList = customList.getResultsetList();

                ajaxXmlBuilder.addItems( regionModelList, "regionName", "regionId" );
            }
        }

        if( logger.isDebugEnabled() )
        {
            logger.debug("End of getResponseContent of RegionRefDataController");
        }
        return ajaxXmlBuilder.toString();
    }

    public void setAgentHierarchyManager( AgentHierarchyManager agentHierarchyManager )
    {
        this.agentHierarchyManager = agentHierarchyManager;
    }

}
