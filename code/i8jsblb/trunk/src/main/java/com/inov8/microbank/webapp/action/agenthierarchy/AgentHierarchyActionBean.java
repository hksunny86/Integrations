package com.inov8.microbank.webapp.action.agenthierarchy;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.swing.tree.TreeNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.richfaces.component.UITree;
import org.richfaces.event.TreeSelectionChangeEvent;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.AreaModel;
import com.inov8.microbank.common.model.DistributorLevelModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.PartnerGroupModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.RetailerModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.agenthierarchy.DistRegHierAssociationModel;
import com.inov8.microbank.common.model.agenthierarchy.RegionModel;
import com.inov8.microbank.common.model.retailermodule.RetailerContactListViewFormModel;
import com.inov8.microbank.common.util.AgentNode;
import com.inov8.microbank.common.util.AuthenticationUtil;
import com.inov8.microbank.common.util.DistributorNode;
import com.inov8.microbank.common.util.FranchiseNode;
import com.inov8.microbank.common.util.JSFContext;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.RegionNode;
import com.inov8.microbank.common.util.SessionBeanObjects;
import com.inov8.microbank.server.service.agenthierarchy.AgentHierarchyManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;


@ManagedBean(name="agentHierarchyActionBean")
@SessionScoped
public class AgentHierarchyActionBean {

	private static final Log logger = LogFactory.getLog(AgentActionBean.class);
	
	@ManagedProperty(value="#{agentHierarchyManager}") 
	private AgentHierarchyManager agentHierarchyManager;

	@ManagedProperty(value="#{userDeviceAccountsManager}")
	private UserDeviceAccountsManager userDeviceAccountsManager;
	
	@ManagedProperty(value="#{smartMoneyAccountManager}")
	private SmartMoneyAccountManager smartMoneyAccountManager;
	
	private List<TreeNode> distributorNodes = new ArrayList<TreeNode>();
    private TreeNode selectedNode;
    
    private List<RetailerContactModel> retailerContactModelList;
    private List<RegionModel> regionModelList;
	private List<DistributorModel> distributorModelList;
	private List<RetailerModel> retailerModelList;
	private List<DistRegHierAssociationModel> distRegHierAssociationModelList;
	
    private RetailerContactListViewFormModel agentFormModel;
    
    private boolean loadFlag = Boolean.TRUE;
    private Boolean securityCheck= Boolean.FALSE;
    private Boolean editSecurityCheck=Boolean.FALSE;

    Map<Long, RetailerModel> retailersMap = new HashMap<Long, RetailerModel>();
    
	public AgentHierarchyActionBean() {
		super();
	}

	@PostConstruct
	public void ini()
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyActionBean.ini Starts");
		}
		try
		{
			this.agentFormModel = new RetailerContactListViewFormModel();
			
			SearchBaseWrapper searchBaseWrapper = agentHierarchyManager.findAllDistributors();
			if(searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0)
			{
				distributorModelList = searchBaseWrapper.getCustomList().getResultsetList();
			}
			else
			{loadFlag = Boolean.FALSE;}
			
			searchBaseWrapper = agentHierarchyManager.findAllRegions();
			if(searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0)
			{
				regionModelList = searchBaseWrapper.getCustomList().getResultsetList();
			}
			else
			{loadFlag = Boolean.FALSE;}
			
			searchBaseWrapper = agentHierarchyManager.findAllRetailers();
			if(searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0)
			{
				retailerModelList = searchBaseWrapper.getCustomList().getResultsetList();
			}
			else
			{loadFlag = Boolean.FALSE;}
			
			searchBaseWrapper = agentHierarchyManager.findAllRetailerContacts();
			if(searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0)
			{
				retailerContactModelList = searchBaseWrapper.getCustomList().getResultsetList();
			}
			else
			{loadFlag = Boolean.FALSE;}
			
			searchBaseWrapper = agentHierarchyManager.findAllRegionalHierarchyAssociations();
			if(searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0)
			{
				this.distRegHierAssociationModelList = searchBaseWrapper.getCustomList().getResultsetList();
			}
			else
			{loadFlag = Boolean.FALSE;}
			
			if(loadFlag)
			{
				loadAllAgentHierarchy();
			}
			else
			{
				JSFContext.addErrorMessage("Agent Hierarchy can not be loaded");
			}
			String createButton=PortalConstants.ADMIN_GP_CREATE+","+
                    PortalConstants.PG_GP_CREATE;
            securityCheck=AuthenticationUtil.checkRightsIfAny(createButton);
            
            String UpdateButton=PortalConstants.ADMIN_GP_UPDATE+","+PortalConstants.PG_GP_UPDATE+","+
                    PortalConstants.MNG_AGNTS_UPDATE;
            setEditSecurityCheck(AuthenticationUtil.checkRightsIfAny(UpdateButton));
		}
		catch(FrameworkCheckedException fce)
		{
			fce.printStackTrace();
			logger.error(fce);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(e);
		}
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyActionBean.ini End");
		}
	}	
	
	public void clearAllSessionObjects(){
		if (!FacesContext.getCurrentInstance().isPostback()) {
			SessionBeanObjects.removeAllSessionObjects();
		}
	}
	
	public void loadAllAgentHierarchy()
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyActionBean.loadAllAgentHierarchy Starts");
		}
		
		for(DistributorModel distributorModel :this.distributorModelList)
		{
			DistributorNode distributorNode = new DistributorNode();
			distributorNode.setLabel(distributorModel.getName());
			distributorNode.setValue(distributorModel.getDistributorId().toString());
			this.distributorNodes.add(distributorNode);
		}
		
		for(RegionModel regionModel: this.regionModelList)
		{
			for(DistRegHierAssociationModel model: this.distRegHierAssociationModelList)
			{
				if(regionModel.getRegionalHierarchyId().equals(model.getRegionalHierarchyId()))
				{
					DistributorNode distributorTreeNode = (DistributorNode)findDistributorNode(this.distributorNodes, model.getDistributorId().toString());
					RegionNode regionNode = new RegionNode();
					regionNode.setLabel(regionModel.getRegionName());
					regionNode.setValue(regionModel.getRegionId().toString());
					distributorTreeNode.addChild(regionNode);
				}
			}
		}
		
		
		for(RetailerModel retailerModel: this.retailerModelList)
		{
			DistributorNode distributorTreeNode = (DistributorNode)findDistributorNode(this.distributorNodes, retailerModel.getDistributorId().toString());
			
			RegionNode regionTreeNode = findRegionNode(distributorTreeNode, retailerModel.getRegionModelId());
			
			if(regionTreeNode != null)
			{
				FranchiseNode franchiseNode = new FranchiseNode();
				franchiseNode.setLabel(retailerModel.getName());
				franchiseNode.setValue(retailerModel.getRetailerId().toString());
				regionTreeNode.addChild(franchiseNode);
			}
			retailersMap.put(retailerModel.getRetailerId(), retailerModel);
		}
		
		
		for(RetailerContactModel retailerContactModel: this.retailerContactModelList)
		{
			RetailerModel retailerModel = retailersMap.get(retailerContactModel.getRetailerId());
			if(retailerModel != null)
			{
				DistributorNode distributorTreeNode = (DistributorNode)findDistributorNode(this.distributorNodes, retailerModel.getDistributorId().toString());
				RegionNode regionTreeNode = findRegionNode(distributorTreeNode, retailerModel.getRegionModelId());
				if(regionTreeNode != null)
				{
					FranchiseNode franchiseTreeNode = findFranchiseNode(regionTreeNode, retailerContactModel.getRetailerId().toString());
					if(franchiseTreeNode != null)
					{
						if(retailerContactModel.getParentRetailerContactModel() == null)
						{
							AgentNode agentNode = new AgentNode();
							agentNode.setLabel(retailerContactModel.getName());
							agentNode.setValue(retailerContactModel.getRetailerContactId().toString());
							franchiseTreeNode.addChild(agentNode);
						}
						else
						{
							AgentNode parentAgentNode = findParentAgentNode(franchiseTreeNode, retailerContactModel);
							if(parentAgentNode != null)
							{
								AgentNode agentNode = new AgentNode();
								agentNode.setLabel(retailerContactModel.getName());
								agentNode.setValue(retailerContactModel.getRetailerContactId().toString());
								parentAgentNode.addChild(agentNode);
							}
						}
					}
				}
			}
		}
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyActionBean.loadAllAgentHierarchy Ends");
		}
	}
	

	
	public void processSelection(TreeSelectionChangeEvent event)
    {
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyActionBean.processSelection Starts");
		}
        try
        {   
        	List<Object> selection = new ArrayList<Object>(event.getNewSelection());
            Object currentSelectionKey = selection.get(0);
            UITree tree = (UITree) event.getSource();
     
            Object storedKey = tree.getRowKey();
            tree.setRowKey(currentSelectionKey);
            this.selectedNode = (TreeNode) tree.getRowData();
            tree.setRowKey(storedKey);
            
            this.agentFormModel = new RetailerContactListViewFormModel();
            AgentNode agentNode = null;
            if(this.selectedNode instanceof AgentNode)
            {
            	agentNode = (AgentNode)this.selectedNode;
            	for(RetailerContactModel model: this.retailerContactModelList)
                {
                	if(model.getRetailerContactId().equals(Long.valueOf(agentNode.getValue())))
                	{
                		DistributorLevelModel distributorLevelModel = this.agentHierarchyManager.findDistributorLevelById(model.getDistributorLevelId());
                		this.agentFormModel.setDistributorLevelName(distributorLevelModel.getName());
                		AreaModel areaModel = this.agentHierarchyManager.findAreaById(model.getAreaId());
                		this.agentFormModel.setAreaName(areaModel.getName());
                		this.agentFormModel.setAreaId(areaModel.getAreaId());
                		this.agentFormModel.setActive(model.getActive());
                		AppUserModel appUserModel = this.agentHierarchyManager.findAppUserByRetailerContactId(model.getRetailerContactId());
                		this.agentFormModel.setFirstName(appUserModel.getFirstName());
                		this.agentFormModel.setLastName(appUserModel.getLastName());
                		this.agentFormModel.setContactNo(appUserModel.getMobileNo());
                		this.agentFormModel.setUsername(appUserModel.getUsername());
                		this.agentFormModel.setAppUserId(appUserModel.getAppUserId());
                		PartnerGroupModel partnerGroupModel = this.agentHierarchyManager.findPartnerGroupByAppUserId(appUserModel.getAppUserId());
                		this.agentFormModel.setPartnerGroupId(partnerGroupModel.getPartnerGroupId());
                		this.agentFormModel.setPartnerGroupName(partnerGroupModel.getName());
                		this.agentFormModel.setEditMode(Boolean.TRUE);
                		this.agentFormModel.setRetailerContactId(model.getRetailerContactId());
                		RetailerModel retailerModel = this.retailersMap.get(model.getRetailerId());
                		this.agentFormModel.setDistributorId(retailerModel.getDistributorId().toString());
                		this.agentFormModel.setRegionId(retailerModel.getRegionModelId().toString());
                		
                		BaseWrapper baseWrapper = new BaseWrapperImpl();
                		UserDeviceAccountsModel userDeviceAccountModel = new UserDeviceAccountsModel();
                		userDeviceAccountModel.setAppUserId(appUserModel.getAppUserId());
                		baseWrapper.setBasePersistableModel(userDeviceAccountModel);
                		baseWrapper = userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
                		userDeviceAccountModel = (UserDeviceAccountsModel)baseWrapper.getBasePersistableModel();
                		if(userDeviceAccountModel != null)
                		{
                			this.agentFormModel.setUserDeviceAccountUserId(userDeviceAccountModel.getUserId());
                		}
                		
                		
                		baseWrapper = new BaseWrapperImpl();
                		SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
                		smartMoneyAccountModel.setRetailerContactId(this.agentFormModel.getRetailerContactId());
                		smartMoneyAccountModel.setActive(Boolean.TRUE);
                		smartMoneyAccountModel.setDefAccount(Boolean.TRUE);
                		smartMoneyAccountModel.setDeleted(Boolean.FALSE);
                		baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
                		baseWrapper = this.smartMoneyAccountManager.searchSmartMoneyAccount(baseWrapper);
                		smartMoneyAccountModel = (SmartMoneyAccountModel)baseWrapper.getBasePersistableModel();
                		if(smartMoneyAccountModel != null)
                		{
                			this.agentFormModel.setAccountNick(smartMoneyAccountModel.getName());
                		}
                		break;
                	}
                }
            }
            else
            {
            	this.selectedNode = null;
            	JSFContext.addErrorMessage("Please select an agent node.");
            }
            	
        }
        catch(Exception e)
        {
        	e.printStackTrace();
			logger.error(e);
        }
        if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyActionBean.processSelection Ends");
		}
    }
	
	
	private AgentNode findParentAgentNode(TreeNode treeNode, RetailerContactModel agentModel)
	{
		AgentNode parentAgentNode = null;
		FranchiseNode franchiseTreeNode = null;
		AgentNode agentTreeNode = null;
		Iterator<AgentNode> agentNodesIterator = null;
		if(treeNode instanceof FranchiseNode)
		{
			franchiseTreeNode = (FranchiseNode)treeNode;
			agentNodesIterator = franchiseTreeNode.childrenIterator();
		}
		else if(treeNode instanceof AgentNode)
		{
			agentTreeNode = (AgentNode)treeNode;
			agentNodesIterator = agentTreeNode.childrenIterator();
		}
			
        for(;agentNodesIterator.hasNext();)
        {
        	parentAgentNode = (AgentNode)agentNodesIterator.next();
            if(parentAgentNode.getValue().equals(agentModel.getParentRetailerContactModelId().toString()))
            {
                break;
            }
            else
            {
            	parentAgentNode = findParentAgentNode(parentAgentNode, agentModel);
            	if(parentAgentNode != null)
            	{
            		break;
            	}
            }
        }
		return parentAgentNode;
	}
	
	private TreeNode findDistributorNode(List<TreeNode> distributorNodes, String distributorId)
	{
		DistributorNode distributorNode = null;
		for(TreeNode treeNode :distributorNodes)
		{
			distributorNode = (DistributorNode)treeNode;
			if(distributorNode.getValue().equals(distributorId))
            {
                break;
            }
		}
		
		return distributorNode;
	}
	
	private RegionNode findRegionNode(DistributorNode distributorTreeNode, Long regionId)
	{
		RegionNode regionNode = null;
		if(regionId != null)
		{	
			Iterator<RegionNode> regionNodesIterator = distributorTreeNode.childrenIterator();
	        for(;regionNodesIterator.hasNext();)
	        {
	            regionNode = (RegionNode)regionNodesIterator.next();
	            if(regionNode.getValue().equals(String.valueOf(regionId)))
	            {
	                break;
	            }
	        }
		}
		return regionNode;
	}
	
	private FranchiseNode findFranchiseNode(RegionNode regionTreeNode, String franchiseId)
	{
		FranchiseNode franchiseNode = null;
		Iterator<FranchiseNode> franchiseNodesIterator = regionTreeNode.childrenIterator();
        for(;franchiseNodesIterator.hasNext();)
        {
            franchiseNode = (FranchiseNode)franchiseNodesIterator.next();
            if(franchiseNode.getValue().equals(franchiseId))
            {
                break;
            }
        }
		return franchiseNode;
	}
	
	public boolean isManageAgentsReadAllowed()
    {
        String readPermission = PortalConstants.ADMIN_GP_READ;
        readPermission += "," + PortalConstants.PG_GP_READ;
        readPermission += "," + PortalConstants.MNG_AGNTS_READ;
        readPermission += "," + PortalConstants.CSR_GP_READ;
        readPermission += "," + PortalConstants.MNO_GP_READ;
        return AuthenticationUtil.checkRightsIfAny( readPermission );
    }

	public void setAgentHierarchyManager(AgentHierarchyManager agentHierarchyManager) {
		this.agentHierarchyManager = agentHierarchyManager;
	}

	public RetailerContactListViewFormModel getAgentFormModel() {
		return agentFormModel;
	}

	public void setAgentFormModel(RetailerContactListViewFormModel agentFormModel) {
		this.agentFormModel = agentFormModel;
	}

	public boolean isLoadFlag() {
		return loadFlag;
	}

	public void setLoadFlag(boolean loadFlag) {
		this.loadFlag = loadFlag;
	}

	public TreeNode getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}

	public void setUserDeviceAccountsManager(
			UserDeviceAccountsManager userDeviceAccountsManager) {
		this.userDeviceAccountsManager = userDeviceAccountsManager;
	}

	public void setSmartMoneyAccountManager(
			SmartMoneyAccountManager smartMoneyAccountManager) {
		this.smartMoneyAccountManager = smartMoneyAccountManager;
	}

	public List<TreeNode> getDistributorNodes() {
		return distributorNodes;
	}

	public void setDistributorNodes(List<TreeNode> distributorNodes) {
		this.distributorNodes = distributorNodes;
	}
	public Boolean getSecurityCheck() {
		return securityCheck;
	}

	public void setSecurityCheck(Boolean securityCheck) {
		this.securityCheck = securityCheck;
	}

	public Boolean getEditSecurityCheck() {
		return editSecurityCheck;
	}

	public void setEditSecurityCheck(Boolean editSecurityCheck) {
		this.editSecurityCheck = editSecurityCheck;
	}
	
}
