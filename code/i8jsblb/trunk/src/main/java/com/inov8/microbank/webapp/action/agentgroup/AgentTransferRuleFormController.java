package com.inov8.microbank.webapp.action.agentgroup;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.AgentTaggingModel;
import com.inov8.microbank.common.model.AgentTransferRuleModel;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.vo.agentgroup.AgentTransferRuleVo;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;
import com.inov8.microbank.server.facade.CommonFacade;
import com.inov8.microbank.server.facade.ProductFacade;
import com.inov8.microbank.server.service.agentgroup.AgentTaggingManager;
import com.inov8.microbank.server.service.devicemodule.DeviceTypeManager;

public class AgentTransferRuleFormController extends AdvanceFormController
{	
	@Autowired
	private AppUserDAO appUserDAO;

    @Autowired
    private CommonFacade commonFacade;

    @Autowired
    private ProductFacade productFacade;
    
    @Autowired
    private DeviceTypeManager deviceTypeManager;

    @Autowired
    AgentTaggingManager agentTaggingManager;
    
    public AgentTransferRuleFormController()
    {
        setCommandName( "agentTransferRuleVo" );
        setCommandClass( AgentTransferRuleVo.class );
    }

    @SuppressWarnings("unchecked")
	@Override
    protected Map<String,Object> loadReferenceData( HttpServletRequest request ) throws Exception
    {
    	Map<String,Object> refDataMap = new HashMap<>(1);
    	List<AgentTaggingModel> agentGroupList = null;
    	ReferenceDataWrapper refDataWrapper = null;

    	AgentTaggingModel agentTaggingModel = new AgentTaggingModel();
    	agentTaggingModel.setStatus(Boolean.TRUE);
    	refDataWrapper = new ReferenceDataWrapperImpl(agentTaggingModel, "groupTitle", SortingOrder.ASC);
    	refDataWrapper = commonFacade.getReferenceData(refDataWrapper);
    	agentGroupList = refDataWrapper.getReferenceDataList();

    	refDataMap.put("agentGroupList", agentGroupList);
    	
        return refDataMap;
    }

    @Override
    protected AgentTransferRuleVo loadFormBackingObject( HttpServletRequest request ) throws Exception
    {
    	AgentTransferRuleVo agentTransferRuleVo = new AgentTransferRuleVo();
    	
    	List<AgentTransferRuleModel> agentTransferRuleModelList = agentTaggingManager.getAllAgentTransferRules();
    	if( CollectionUtils.isNotEmpty(agentTransferRuleModelList) ){
    		agentTransferRuleVo.setAgentTransferRuleModelList(agentTransferRuleModelList);
    	}
    	
    	//Add 1 object to make sure that table on screen has at least one row
    	if( CollectionUtils.isEmpty(agentTransferRuleVo.getAgentTransferRuleModelList()) ){
			agentTransferRuleVo.addAgentTransferRuleModel(new AgentTransferRuleModel());
		}
    	
        return agentTransferRuleVo;
    }

    @Override
    protected ModelAndView onCreate( HttpServletRequest request, HttpServletResponse response,
                                     Object command, BindException errors ) throws Exception
    {
    	AgentTransferRuleVo agentTransferRuleVo = (AgentTransferRuleVo) command;
    	Date now = new Date();
    	List<AgentTransferRuleModel> agentTransferRuleModelList = agentTransferRuleVo.getAgentTransferRuleModelList();
    	for(AgentTransferRuleModel agentTransferRuleModel : agentTransferRuleModelList)
    	{
			agentTransferRuleModel.setPrimaryKey(null);
			agentTransferRuleModel.setVersionNo(null);
    		agentTransferRuleModel.setCreatedOn(now);
    		agentTransferRuleModel.setUpdatedOn(now);
    		agentTransferRuleModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
			agentTransferRuleModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		}

    	try {
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long( PortalConstants.UPDATE_AGENT_TRANSFER_RULE_USECASE_ID ) );
			baseWrapper.putObject(AgentTransferRuleVo.class.getSimpleName(), agentTransferRuleVo);
			agentTaggingManager.saveAgentTransferRules(baseWrapper);
		} catch (Exception e) {
			e.printStackTrace();
			this.saveMessage(request, MessageUtil.getMessage("6075"));
	        return super.showForm(request, response, errors);
		}
    	
    	this.saveMessage(request, "Agent rules saved successfully.");
    	Map<String, Object> modelMap = new HashMap<>(2);
    	return new ModelAndView(getSuccessView(), modelMap );
    }

    @Override
    protected ModelAndView onUpdate( HttpServletRequest request, HttpServletResponse response,
                                     Object command, BindException errors ) throws Exception
    {

    	BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
		baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long( PortalConstants.UPDATE_AGENT_TRANSFER_RULE_USECASE_ID ) );
    	try{
    		agentTaggingManager.deleteAllAgentTransferRules(baseWrapper);
		}catch(Exception e){
			e.printStackTrace();
			this.saveMessage(request, MessageUtil.getMessage("6075"));
	        return super.showForm(request, response, errors);
		}
    	this.saveMessage(request, "All rules removed successfully.");

    	Map<String, Object> modelMap = new HashMap<>(2);
    	return new ModelAndView(getSuccessView(), modelMap );
    }
    
    public void setCommonFacade( CommonFacade commonFacade )
    {
        this.commonFacade = commonFacade;
    }

    public void setProductFacade(ProductFacade productFacade)
    {
		this.productFacade = productFacade;
	}

	public void setDeviceTypeManager(DeviceTypeManager deviceTypeManager) {
		this.deviceTypeManager = deviceTypeManager;
	}

	public void setAppUserDAO(AppUserDAO appUserDAO) {
		this.appUserDAO = appUserDAO;
	}

	public void setAgentTaggingManager(AgentTaggingManager agentTaggingManager) {
		this.agentTaggingManager = agentTaggingManager;
	}

}
