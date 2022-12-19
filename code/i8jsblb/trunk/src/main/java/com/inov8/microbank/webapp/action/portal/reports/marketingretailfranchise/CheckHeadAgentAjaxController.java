package com.inov8.microbank.webapp.action.portal.reports.marketingretailfranchise;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.apache.commons.validator.GenericValidator;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.model.DistributorLevelModel;
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
public class CheckHeadAgentAjaxController extends AjaxController
{
    //Autowired
    private DistributorLevelManager distributorLevelManager;

    @SuppressWarnings( "unchecked" )
    @Override
    public String getResponseContent( HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
        if( logger.isDebugEnabled() )
        {
            logger.debug("Start of getResponseContent of ParentAgentRefDataController");
        }
        AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
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
            ajaxXmlBuilder.addItem("isHead",isHead);
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

}
