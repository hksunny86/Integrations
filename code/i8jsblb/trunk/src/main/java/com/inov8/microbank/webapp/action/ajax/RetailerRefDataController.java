package com.inov8.microbank.webapp.action.ajax;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.apache.commons.validator.GenericValidator;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.DistributorLevelModel;
import com.inov8.microbank.common.model.RetailerModel;
import com.inov8.microbank.server.service.agenthierarchy.AgentHierarchyManager;
import com.inov8.microbank.server.service.distributormodule.DistributorLevelManager;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Feb 17, 2013 11:57:20 AM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class RetailerRefDataController extends AjaxController
{
    //Autowired
    private AgentHierarchyManager agentHierarchyManager;
    private DistributorLevelManager distributorLevelManager;

    @SuppressWarnings( "unchecked" )
    @Override
    public String getResponseContent( HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
        if( logger.isDebugEnabled() )
        {
            logger.debug("Start of getResponseContent of RetailerRefDataController");
        }
        AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
       
        
       //No franchise should me loaded in case of head agent 
        String isHead = null;
        String agentLevelIdStr = request.getParameter( "agentLevelId" );
        if( !GenericValidator.isBlankOrNull( agentLevelIdStr ) )
        {
            long agentLevelId = Long.parseLong( agentLevelIdStr );
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            DistributorLevelModel distributorLevelModel = new DistributorLevelModel();
            distributorLevelModel.setDistributorLevelId(agentLevelId);
            baseWrapper.setBasePersistableModel(distributorLevelModel);
            baseWrapper = distributorLevelManager.loadDistributorLevel(baseWrapper);
            distributorLevelModel =  (DistributorLevelModel) baseWrapper.getBasePersistableModel();
            if(null == distributorLevelModel.getManagingLevelId()) {
            	isHead = "true";
            }else{
            	isHead = "false";
            }            
           
        }
        
        if(isHead == "true"){
        	ajaxXmlBuilder.addItemAsCData("[AUTO_GENERATED]", "-2");	
        }
        else
        {
        	ajaxXmlBuilder.addItemAsCData("[Select]", "");
            String regionIdStr = request.getParameter( "regionId" );
            if( !GenericValidator.isBlankOrNull( regionIdStr ) )
            {
                long regionId = Long.parseLong( regionIdStr );
                SearchBaseWrapper wrapper = agentHierarchyManager.findRetailersByRegionId( regionId );

                CustomList<RetailerModel> customList = wrapper.getCustomList();
                if( customList != null )
                {
                    List<RetailerModel> retailerModelList = customList.getResultsetList();

                    ajaxXmlBuilder.addItems( retailerModelList, "name", "retailerId" );
                }
            }
        	
        }
        
        
        if( logger.isDebugEnabled() )
        {
            logger.debug("End of getResponseContent of RetailerRefDataController");
        }
        return ajaxXmlBuilder.toString();
    }

    public void setAgentHierarchyManager( AgentHierarchyManager agentHierarchyManager )
    {
        this.agentHierarchyManager = agentHierarchyManager;
    }

	public DistributorLevelManager getDistributorLevelManager() {
		return distributorLevelManager;
	}

	public void setDistributorLevelManager(
			DistributorLevelManager distributorLevelManager) {
		this.distributorLevelManager = distributorLevelManager;
	}

}
