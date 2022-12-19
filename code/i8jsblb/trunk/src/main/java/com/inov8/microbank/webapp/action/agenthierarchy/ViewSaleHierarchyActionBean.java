package com.inov8.microbank.webapp.action.agenthierarchy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.agenthierarchy.SalesHierarchyModel;
import com.inov8.microbank.common.util.AuthenticationUtil;
import com.inov8.microbank.common.util.Constants;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.SessionBeanObjects;
import com.inov8.microbank.server.service.agenthierarchy.AgentHierarchyManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;

@ManagedBean(name="viewSaleHierarchyActionBean")
@SessionScoped
public class ViewSaleHierarchyActionBean {

	private static final Log logger = LogFactory.getLog(ViewSaleHierarchyActionBean.class);
	
	@ManagedProperty(value="#{agentHierarchyManager}") 
	private AgentHierarchyManager agentHierarchyManager;
	
	@ManagedProperty(value="#{userManager}") 
	private AppUserManager appUserManager;

	private Boolean securityCheck= Boolean.FALSE;
	private Boolean editSecurityCheck=Boolean.FALSE;
	private Boolean deleteSecurityCheck=Boolean.FALSE;
	
	private List<TreeNode> saleUserNodes = new ArrayList<TreeNode>();
    private TreeNode selectedNode;
	
    private SalesHierarchyModel salesHierarchyModel = new SalesHierarchyModel();
    
    private String saleUserName;
    private String parentSaleUserName;
    private String ultimateParentSaleUserName;
    private String roleTitle;
    private String empId;
    private Boolean active;
    
    private void configureSecuritySetting()
	{
		String createButton=PortalConstants.ADMIN_GP_CREATE+","+PortalConstants.PG_GP_CREATE+","+
                PortalConstants.MNG_SALES_HIER_READ;
        securityCheck=AuthenticationUtil.checkRightsIfAny(createButton);
        
        String updateButton=PortalConstants.ADMIN_GP_UPDATE+","+PortalConstants.PG_GP_UPDATE+","+
                PortalConstants.MNG_SALES_HIER_UPDATE;
        editSecurityCheck=AuthenticationUtil.checkRightsIfAny(updateButton);
        String deleteButton=PortalConstants.ADMIN_GP_UPDATE+","+PortalConstants.PG_GP_UPDATE+","+
                PortalConstants.MNG_SALES_HIER_UPDATE;
		setDeleteSecurityCheck(AuthenticationUtil.checkRightsIfAny(deleteButton));
	}
    
    
	
	public void processSaleUserSelection(TreeSelectionChangeEvent event)
    {
        try
        {
        	List<Object> selection = new ArrayList<Object>(event.getNewSelection());
            Object currentSelectionKey = selection.get(0);
            UITree tree = (UITree) event.getSource();
     
            Object storedKey = tree.getRowKey();
            tree.setRowKey(currentSelectionKey);
            this.selectedNode = (TreeNode) tree.getRowData();
            
            Node node = (Node)this.selectedNode;
            
            SearchBaseWrapper searchBaseWrapper = this.agentHierarchyManager.findSaleUserByBankUserId(node.getId());
            this.salesHierarchyModel = (SalesHierarchyModel)searchBaseWrapper.getBasePersistableModel();
            
            if(this.salesHierarchyModel.getRoleTitle() != null)
            {
            	this.roleTitle = this.salesHierarchyModel.getRoleTitle();	
            }
            else
            {
            	this.roleTitle = "";
            }
            //this.active = this.salesHierarchyModel.getActive();
            
            AppUserModel user = this.appUserManager.getUser(node.getId().toString());
            this.saleUserName = user.getFirstName() +  " " + user.getLastName();
            if(user.getEmployeeId() != null)
            {
            	this.empId = user.getEmployeeId().toString();
            }
            else
            {
            	this.empId = "";
            }
            if(this.salesHierarchyModel.getParentBankUserId() != null)
            {
            	user = this.appUserManager.getUser(this.salesHierarchyModel.getParentBankUserId().toString());
                this.parentSaleUserName = user.getFirstName() + " " + user.getLastName();	
            }
            else
            {
            	this.parentSaleUserName = "";
            }
            
            if(this.salesHierarchyModel.getUltimateParentBankUserId() != null)
            {
            	user = this.appUserManager.getUser(this.salesHierarchyModel.getUltimateParentBankUserId().toString());
                this.ultimateParentSaleUserName = user.getFirstName() + " " + user.getLastName();	
            }
            else
            {
            	this.ultimateParentSaleUserName = "";
            }
            
            tree.setRowKey(storedKey);
        }
        catch(Exception e)
        {
			logger.error(e);
        }
    }
    
    public void clearAllSessionObjects(){
		if (!FacesContext.getCurrentInstance().isPostback()) {
			SessionBeanObjects.removeAllSessionObjects();
		}
	}
	
    @PostConstruct
	public void ini()
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentActionBean.loadSaleUserHierarchy Start");
		}
		try 
		{
			//this.saleUserHierarchyError = Boolean.FALSE;
			this.saleUserNodes.clear();
			List<SalesHierarchyModel> saleUserModelList;
			SearchBaseWrapper searchBaseWrapper = this.agentHierarchyManager.findAllSaleUsers();
			if(searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0)
			{
				saleUserModelList = searchBaseWrapper.getCustomList().getResultsetList();
				
				for(SalesHierarchyModel model : saleUserModelList)
				{
					if(model.getUltimateParentBankUserId() == null)
					{
						AppUserModel user = this.appUserManager.getUser(model.getBankUserId().toString());
						Node node = new Node();
						String name = user.getFirstName();
						if(user.getLastName() != null)
						{
							name = name + " " + user.getLastName();
						}
						String empId = (user.getEmployeeId() == null) ? "" : " - " + user.getEmployeeId();
						node.setName(name  + " [ " + model.getRoleTitle() + empId + " ]");
						node.setId(model.getBankUserId());
				        this.saleUserNodes.add(node);			
					}
				}
			
				List<SalesHierarchyModel> childSalesHierarchyModelList = new ArrayList<SalesHierarchyModel>();
				for(TreeNode treeNode : this.saleUserNodes)
				{
					childSalesHierarchyModelList.clear();
					Node ultimateNode = (Node)treeNode;
					for(SalesHierarchyModel model:saleUserModelList)
					{
						if(ultimateNode.getId().equals(model.getUltimateParentBankUserId()))
						{
							childSalesHierarchyModelList.add(model);
						}
					}
					
					for(SalesHierarchyModel childSalesHierarchyModel:childSalesHierarchyModelList)
					{
						Node perentNode = findParentNode(ultimateNode, childSalesHierarchyModel);
						if(perentNode != null)
						{
							AppUserModel user = this.appUserManager.getUser(childSalesHierarchyModel.getBankUserId().toString());
							Node childNode = new Node();
							String name = user.getFirstName();
							if(user.getLastName() != null)
							{
								name = name + " " + user.getLastName();
							}
							
							String empId = (user.getEmployeeId() == null) ? "" : " - " + user.getEmployeeId();
							childNode.setName(name  + " [ " + childSalesHierarchyModel.getRoleTitle() + empId + " ]");
							childNode.setId(childSalesHierarchyModel.getBankUserId());
							perentNode.addChild(childNode);
						}
					}
				}				
			}
			configureSecuritySetting();
		} 
		catch (NumberFormatException e1) 
		{
			logger.error(e1);
		} 
		catch (FrameworkCheckedException e1) 
		{
			logger.error(e1);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e);
		}
		if(logger.isDebugEnabled()){
			logger.debug("AgentActionBean.loadSaleUserHierarchy End");
		}
	}
	
	private Node findParentNode(Node root, SalesHierarchyModel salesHierarchyModel)
	{
		Node parentNode = null;
		if(root.getId().equals(salesHierarchyModel.getParentBankUserId()))
		{
			parentNode = root;
		}
		else
		{
			Iterator<Node> nodesIterator = root.childrenIterator();
	        for(;nodesIterator.hasNext();)
	        {
	        	parentNode = (Node)nodesIterator.next();
	            if(parentNode.getId().equals(salesHierarchyModel.getParentBankUserId()))
	            {
	                break;
	            }
	            else
	            {
	            	parentNode = findParentNode(parentNode, salesHierarchyModel);
	            	if(parentNode != null)
	            	{
	            		break;
	            	}
	            }
	        }
		}
		return parentNode;
	}
	
	public List<TreeNode> getSaleUserNodes() {
		return saleUserNodes;
	}

	public void setSaleUserNodes(List<TreeNode> saleUserNodes) {
		this.saleUserNodes = saleUserNodes;
	}

	public TreeNode getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}

	public void setAgentHierarchyManager(AgentHierarchyManager agentHierarchyManager) {
		this.agentHierarchyManager = agentHierarchyManager;
	}

	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}

	public SalesHierarchyModel getSalesHierarchyModel() {
		return salesHierarchyModel;
	}

	public void setSalesHierarchyModel(SalesHierarchyModel salesHierarchyModel) {
		this.salesHierarchyModel = salesHierarchyModel;
	}

	public String getSaleUserName() {
		return saleUserName;
	}

	public void setSaleUserName(String saleUserName) {
		this.saleUserName = saleUserName;
	}

	public String getUltimateParentSaleUserName() {
		return ultimateParentSaleUserName;
	}

	public void setUltimateParentSaleUserName(String ultimateParentSaleUserName) {
		this.ultimateParentSaleUserName = ultimateParentSaleUserName;
	}

	public String getRoleTitle() {
		return roleTitle;
	}

	public void setRoleTitle(String roleTitle) {
		this.roleTitle = roleTitle;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getParentSaleUserName() {
		return parentSaleUserName;
	}

	public void setParentSaleUserName(String parentSaleUserName) {
		this.parentSaleUserName = parentSaleUserName;
	}
	public void setDeleteSecurityCheck(Boolean deleteSecurityCheck) {
		this.deleteSecurityCheck = deleteSecurityCheck;
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

	public Boolean getDeleteSecurityCheck() {
		return deleteSecurityCheck;
	}

	public String editSalesHierarchy()
	{
		
		return Constants.ReturnCodes.CREATE_SALES_HIERARCHY;
	}
}
