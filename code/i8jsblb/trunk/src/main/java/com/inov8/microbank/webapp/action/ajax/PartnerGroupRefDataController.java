package com.inov8.microbank.webapp.action.ajax;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.apache.commons.validator.GenericValidator;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.PartnerGroupModel;
import com.inov8.microbank.server.service.agenthierarchy.AgentHierarchyManager;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Feb 17, 2013 11:57:20 AM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class PartnerGroupRefDataController extends AjaxController
{
    //Autowired
    private AgentHierarchyManager agentHierarchyManager;

    @SuppressWarnings( "unchecked" )
    @Override
    public String getResponseContent( HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
        if( logger.isDebugEnabled() )
        {
            logger.debug("Start of getResponseContent of PartnerGroupRefDataController");
        }
        AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
        ajaxXmlBuilder.addItemAsCData("[Select]", "");

        String retailerIdStr = request.getParameter( "retailerId" );
        if( !GenericValidator.isBlankOrNull( retailerIdStr ) )
        {
            long retailerId = Long.parseLong( retailerIdStr );
            SearchBaseWrapper wrapper = agentHierarchyManager.findPartnerGroupsByRetailer( retailerId );

            CustomList<PartnerGroupModel> customList = wrapper.getCustomList();
            if( customList != null )
            {
                List<PartnerGroupModel> partnerGroupModelList = customList.getResultsetList();

                ajaxXmlBuilder.addItems( partnerGroupModelList, "name", "partnerGroupId" );
            }
        }
         
        if( logger.isDebugEnabled() )
        {
            logger.debug("End of getResponseContent of PartnerGroupRefDataController");
        }
        return ajaxXmlBuilder.toString();
    }

    public void setAgentHierarchyManager( AgentHierarchyManager agentHierarchyManager )
    {
        this.agentHierarchyManager = agentHierarchyManager;
    }

}
