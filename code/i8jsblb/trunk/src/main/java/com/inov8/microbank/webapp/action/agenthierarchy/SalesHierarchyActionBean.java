package com.inov8.microbank.webapp.action.agenthierarchy;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
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
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.agenthierarchy.SalesHierarchyHistoryModel;
import com.inov8.microbank.common.model.agenthierarchy.SalesHierarchyModel;
import com.inov8.microbank.common.util.AuthenticationUtil;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.Constants;
import com.inov8.microbank.common.util.JSFContext;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.agenthierarchy.AgentHierarchyManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;

@ManagedBean(name="salesHierarchyActionBean")
@SessionScoped
public class SalesHierarchyActionBean {
	
private static final Log logger= LogFactory.getLog(SalesHierarchyActionBean.class);
	
	@ManagedProperty(value="#{agentHierarchyManager}") 
	private AgentHierarchyManager agentHierarchyManager;
	
	@ManagedProperty(value="#{userManager}") 
	private AppUserManager appUserManager;

	private SalesHierarchyModel salesHierarchyModel = new SalesHierarchyModel();
	
	private Boolean securityCheck= Boolean.FALSE;
	private Boolean editSecurityCheck=Boolean.FALSE;
	private Boolean deleteSecurityCheck=Boolean.FALSE;
	private Boolean alreadyAssociated=Boolean.FALSE;
		
	private List<AppUserModel> appUserModelList;
	private List<SelectItem> appUsers = new ArrayList<SelectItem>();

	private List<SalesHierarchyModel> saleHierarchyModelList;
	private List<SelectItem> ultimateAppUsers = new ArrayList<SelectItem>();
	
	private boolean saleUserHierarchyError;
	
	private List<TreeNode> saleUserNodes = new ArrayList<TreeNode>();
    private TreeNode selectedNode;
    
    private List<SalesHierarchyHistoryModel> salesUserHierarchyHistoryModels = new ArrayList<SalesHierarchyHistoryModel>();

    
    public Boolean validate(SalesHierarchyModel salesHierarchyModel) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("SalesHierarchyActionBean.validate Starts");
		}
		boolean validated = Boolean.TRUE;
		if(salesHierarchyModel.getBankUserId() == null || salesHierarchyModel.getBankUserId().equals(-1l))
		{
			JSFContext.addErrorMessage(Constants.ErrorMessages.AH_SALE_USER_NAME_ERROR);
			validated = Boolean.FALSE;
		}
		else
		{
			SearchBaseWrapper searchBaseWrapper = this.agentHierarchyManager.findSaleUserByBankUserId(this.salesHierarchyModel.getBankUserId());
			SalesHierarchyModel model = (SalesHierarchyModel)searchBaseWrapper.getBasePersistableModel(); 
			if(model != null && !model.getSalesHierarchyId().equals(this.salesHierarchyModel.getSalesHierarchyId()))
			{
				JSFContext.addErrorMessage(Constants.ErrorMessages.AH_SALE_USER_ALREADY_ASSOCIATED_ERROR);
				validated = Boolean.FALSE;
			}
		}
		
		if(CommonUtils.isEmpty(salesHierarchyModel.getRoleTitle()))
		{
			JSFContext.addErrorMessage(Constants.ErrorMessages.AH_ROLE_TITLE_ERROR);
			validated = Boolean.FALSE;
		}
		
		if(salesHierarchyModel.getUltimateParentBankUserId() != null && salesHierarchyModel.getUltimateParentBankUserId() > 0)
		{
			if(CommonUtils.isEmpty(salesHierarchyModel.getParentBankUserName()))
			{
				JSFContext.addErrorMessage(Constants.ErrorMessages.AH_PARENT_SALE_USER_REQ_ERROR);
				validated = Boolean.FALSE;
			}
		}
		
		if(logger.isDebugEnabled()){
			logger.debug("SalesHierarchyActionBean.validate End");
		}
		return validated;
	}
    
    public void saveSalesHierarchy()
	{
		if(logger.isDebugEnabled()){
			logger.debug("SalesHierarchyActionBean.saveSalesHierarchy Starts");
		}
		try {
			if(validate(this.salesHierarchyModel))
			{
				List<SalesHierarchyModel> salesHierarchyModels = agentHierarchyManager.findSalesHierarchyByBankUser(this.salesHierarchyModel.getBankUserId());
				
				this.salesHierarchyModel.setCreatedOn(new Date());
				this.salesHierarchyModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
				this.salesHierarchyModel.setUpdatedOn(new Date());
				this.salesHierarchyModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
				
				if(this.salesHierarchyModel.getUltimateParentBankUserId().equals(-1l))
				{
					this.salesHierarchyModel.setUltimateParentBankUserId(null);
				}
				
				if(this.salesHierarchyModel.getParentBankUserName() == null || this.salesHierarchyModel.getParentBankUserName().equals(""))
				{
					salesHierarchyModel.setParentBankUserId(null);	
				}
					
				BaseWrapper baseWrapper = new BaseWrapperImpl();
				baseWrapper.setBasePersistableModel(this.salesHierarchyModel);
				if(salesHierarchyModels != null && salesHierarchyModels.size() > 0){
					baseWrapper.putObject("oldSalesHierarchyModel", salesHierarchyModels.get(0));
				}
				baseWrapper=agentHierarchyManager.saveOrUpdateSalesHierarchy(baseWrapper);
				loadUltimateSaleUsers();
				this.salesHierarchyModel = new SalesHierarchyModel();
				JSFContext.setInfoMessage(Constants.InfoMessages.AH_SALES_HIER_SAVE_INFO);
				logger.info(Constants.InfoMessages.AH_SALES_HIER_SAVE_INFO);
			}
		} 
		catch (FrameworkCheckedException e1)
		{
			logger.error(e1);
		}
		catch(Exception e){
			logger.error(e);
			e.printStackTrace();
		}
		if(logger.isDebugEnabled()){
			logger.debug("SalesHierarchyActionBean.saveSalesHierarchy End");
		}
	}
    
    public void processSelection(TreeSelectionChangeEvent event)
    {
        try
        {
        	List<Object> selection = new ArrayList<Object>(event.getNewSelection());
            Object currentSelectionKey = selection.get(0);
            UITree tree = (UITree) event.getSource();
     
            Object storedKey = tree.getRowKey();
            tree.setRowKey(currentSelectionKey);
            this.selectedNode = (TreeNode) tree.getRowData();
            tree.setRowKey(storedKey);
        }
        catch(Exception e)
        {
			logger.error(e);
        }
    }
    
	public void clearNodeSelection(ActionEvent event)
	{
		this.selectedNode = null;
	}
	
	public void reRenderFields(ActionEvent event)
	{
		if(this.selectedNode != null)
		{
			Node myNode = (Node)this.selectedNode;
			this.salesHierarchyModel.setParentBankUserId(myNode.getId());
			this.salesHierarchyModel.setParentBankUserName(myNode.getName());
		}
	}
	
	@PostConstruct
	public void ini()
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("SalesHierarchyActionBean.ini Starts");
		}
		try
		{
			loadBankUsers();
			loadUltimateSaleUsers();
			configureSecuritySetting();
			String saleUserId = (String)JSFContext.getFromRequest("saleUserId");
			if(saleUserId != null)
			{
				SearchBaseWrapper searchBaseWrapper = this.agentHierarchyManager.findSaleUserByBankUserId(Long.valueOf(saleUserId));
	            this.salesHierarchyModel = (SalesHierarchyModel)searchBaseWrapper.getBasePersistableModel();
	            AppUserModel user = this.appUserManager.getUser(this.salesHierarchyModel.getParentBankUserId().toString());
	            String name = user.getFirstName();
				if(user.getLastName() != null)
				{
					name = name + " " + user.getLastName() ;
				}
				name  += " [ " + this.salesHierarchyModel.getRoleTitle();
				if(user.getEmployeeId() != null){
					name  += " - " + user.getEmployeeId();
				}
				name  += " ]";
				this.salesHierarchyModel.setParentBankUserName(name);
			}
		}
		catch(FrameworkCheckedException fce)
		{
			logger.error(fce);
		}
		catch(Exception e)
		{
			logger.error(e);
		}
		if(logger.isDebugEnabled()){
			logger.debug("SalesHierarchyActionBean.ini End");
		}
	}

	private void configureSecuritySetting()
	{
		String createButton=PortalConstants.ADMIN_GP_CREATE+","+PortalConstants.PG_GP_CREATE+","+
                PortalConstants.MNG_SALES_HIER_CREATE;
        securityCheck=AuthenticationUtil.checkRightsIfAny(createButton);
        
        String updateButton=PortalConstants.ADMIN_GP_UPDATE+","+PortalConstants.PG_GP_UPDATE+","+
                PortalConstants.MNG_SALES_HIER_UPDATE;
        editSecurityCheck=AuthenticationUtil.checkRightsIfAny(updateButton);
        String deleteButton=PortalConstants.ADMIN_GP_UPDATE+","+PortalConstants.PG_GP_UPDATE+","+
                PortalConstants.MNG_SALES_HIER_UPDATE;
		setDeleteSecurityCheck(AuthenticationUtil.checkRightsIfAny(deleteButton));
	}
	
	private void loadBankUsers() throws FrameworkCheckedException
	{
		SearchBaseWrapper searchBaseWrapper = appUserManager.findAppUsersByAppUserTypeId(UserTypeConstantsInterface.BANK);
		if(searchBaseWrapper.getCustomList() != null)
		{
			appUserModelList = searchBaseWrapper.getCustomList().getResultsetList();
			if(this.appUserModelList != null)
			{
				for(AppUserModel model:appUserModelList)
				{
					String name = model.getFirstName();
					if(model.getLastName() != null)
					{
						name = name + " " + model.getLastName();
					}
					if(model.getEmployeeId() != null){
						name  += " - " + model.getEmployeeId();
					}
					this.appUsers.add(new SelectItem(model.getAppUserId(), name ));
				}
			}
		}
	}
	
	private void loadUltimateSaleUsers() throws FrameworkCheckedException
	{
		SearchBaseWrapper searchBaseWrapper = this.agentHierarchyManager.findUltimateSaleUsers();
		if(searchBaseWrapper.getCustomList() != null)
		{
			this.ultimateAppUsers.clear();
			this.saleHierarchyModelList = searchBaseWrapper.getCustomList().getResultsetList();
			if(this.saleHierarchyModelList != null)
			{
				for(SalesHierarchyModel model:saleHierarchyModelList)
				{
					for(AppUserModel appUserModel: this.appUserModelList)
					{
						if(appUserModel.getAppUserId().equals(model.getBankUserId()))
						{
							String name = appUserModel.getFirstName();
							/*if(appUserModel.getLastName() != null)
							{
								name = name + " " + appUserModel.getLastName();
							}*/
							if(appUserModel.getLastName() != null)
							{
								name = name + " " + appUserModel.getLastName();
							}
							if(appUserModel.getEmployeeId() != null){
								name  += " - " + appUserModel.getEmployeeId();
							}
							this.ultimateAppUsers.add(new SelectItem(appUserModel.getAppUserId(), name));
							break;
						}
					}
				}
			}
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
	
	public void loadSaleUserHierarchy(AjaxBehaviorEvent event)
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentActionBean.loadSaleUserHierarchy Start");
		}
		try 
		{
			if(this.ultimateAppUsers.size() > 0 && (this.salesHierarchyModel.getUltimateParentBankUserId() == null || this.salesHierarchyModel.getUltimateParentBankUserId().equals(-1l)))
			{
				JSFContext.addErrorMessage("Please select ultimate parent sale user.");
				this.saleUserHierarchyError = Boolean.TRUE;
			}
			else
			{
				this.saleUserHierarchyError = Boolean.FALSE;
				this.saleUserNodes.clear();
				AppUserModel user = this.appUserManager.getUser(this.salesHierarchyModel.getUltimateParentBankUserId().toString());
				Node node = new Node();
				String name = user.getFirstName();
				if(user.getLastName() != null)
				{
					name = name + " " + user.getLastName() ;
				}
				
				SalesHierarchyModel shm = null;
				SearchBaseWrapper searchBaseWrapper = agentHierarchyManager.findSaleUserByBankUserId(user.getAppUserId());
				if(searchBaseWrapper.getBasePersistableModel() != null){
					shm = (SalesHierarchyModel)searchBaseWrapper.getBasePersistableModel();
					
					name  += " [ " + shm.getRoleTitle();
					if(user.getEmployeeId() != null){
						name  += " - " + user.getEmployeeId();
					}
					name  += " ]";
				}
				node.setName(name);
				node.setId(this.salesHierarchyModel.getUltimateParentBankUserId());
		        this.saleUserNodes.add(node);
				
				List<SalesHierarchyModel> childSalesHierarchyModelList = null;
				
				searchBaseWrapper = this.agentHierarchyManager.findSaleUsersByUltimateParentSaleUserId(this.salesHierarchyModel.getUltimateParentBankUserId());
				
				if(searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0)
				{
					childSalesHierarchyModelList = searchBaseWrapper.getCustomList().getResultsetList();
					while(childSalesHierarchyModelList.size() > 0)
					{
						List<SalesHierarchyModel> tempSalesHierarchyModelList = new ArrayList<SalesHierarchyModel>();
						for(SalesHierarchyModel childSalesHierarchyModel:childSalesHierarchyModelList)
						{
							Node perentNode = findParentNode((Node)node, childSalesHierarchyModel);
							if(perentNode != null)
							{
								user = this.appUserManager.getUser(childSalesHierarchyModel.getBankUserId().toString());
								Node childNode = new Node();
								name = user.getFirstName();
								if(user.getLastName() != null)
								{
									name = name + " " + user.getLastName();
								}
								name  += " [ " + childSalesHierarchyModel.getRoleTitle();
								if(user.getEmployeeId() != null){
									name  += " - " + user.getEmployeeId();
								}
								name  += " ]";
								childNode.setName(name);
								childNode.setId(childSalesHierarchyModel.getBankUserId());
								perentNode.addChild(childNode);
								tempSalesHierarchyModelList.add(childSalesHierarchyModel);
							}
						}
						if(tempSalesHierarchyModelList.size() > 0 )
						{
							childSalesHierarchyModelList.removeAll(tempSalesHierarchyModelList);	
						}
						else if(childSalesHierarchyModelList.size() > 0)
						{
							logger.debug("There are " + childSalesHierarchyModelList.size() + " sale user(s) not loaded in the hierarchy due to some issue");
							logger.debug("SalesHierarchyId of one of them is: " + childSalesHierarchyModelList.get(0).getSalesHierarchyId());
							break;
						}
					}
					
					
					
					/*for(SalesHierarchyModel childSalesHierarchyModel:childSalesHierarchyModelList)
					{
						Node perentNode = findParentNode((Node)node, childSalesHierarchyModel);
						if(perentNode != null)
						{
							user = this.appUserManager.getUser(childSalesHierarchyModel.getBankUserId().toString());
							Node childNode = new Node();
							name = user.getFirstName();
							if(user.getLastName() != null)
							{
								name = name + " " + user.getLastName();
							}
							name  += " [ " + childSalesHierarchyModel.getRoleTitle();
							if(user.getEmployeeId() != null){
								name  += " - " + user.getEmployeeId();
							}
							name  += " ]";
							childNode.setName(name);
							childNode.setId(childSalesHierarchyModel.getBankUserId());
							perentNode.addChild(childNode);
						}
					}*/    
				}
			}
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
	
	public void loadSaleUserHistory(AjaxBehaviorEvent event)
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentActionBean.loadSaleUserHistory Start");
		}
		this.salesUserHierarchyHistoryModels = new ArrayList<SalesHierarchyHistoryModel>();
		try 
		{
			AppUserModel user = this.appUserManager.getUser(this.salesHierarchyModel.getBankUserId().toString());
			String name = user.getFirstName();
			if(user.getLastName() != null)
			{
				name = name + " " + user.getLastName() ;
			}
			
			List<SalesHierarchyHistoryModel> historyList = null;
			SearchBaseWrapper searchBaseWrapper = agentHierarchyManager.findSaleUserHistoryByBankUserId(user.getAppUserId());
			if(searchBaseWrapper.getCustomList() != null){
				historyList = searchBaseWrapper.getCustomList().getResultsetList();
				this.salesUserHierarchyHistoryModels.addAll(historyList);
			}
			
			
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
	
	public void clearSalesHierarchyForm(ActionEvent e)
	{
		if(logger.isDebugEnabled()){
			logger.debug("SalesHierarchyActionBean.clearSalesHierarchyForm Starts");
		}
		
		this.salesHierarchyModel = new SalesHierarchyModel();
		
		if(logger.isDebugEnabled()){
			logger.debug("SalesHierarchyActionBean.clearSalesHierarchyForm End");
		}
	}	
	
	public void onChangeSaleUser(AjaxBehaviorEvent e){
		
		Long bankUserId = this.salesHierarchyModel.getBankUserId();
		this.salesHierarchyModel = new SalesHierarchyModel();
		if(bankUserId != null && bankUserId > 0){
			try{
				List<SalesHierarchyModel> salesHierarchyModels = agentHierarchyManager.findSalesHierarchyByBankUser(bankUserId);
				if(salesHierarchyModels != null && salesHierarchyModels.size() > 0){ //this user already associated::show it and display option buttons to retag/inactive
					this.salesHierarchyModel = salesHierarchyModels.get(0);
					this.alreadyAssociated=true;
				}else{
					this.salesHierarchyModel = new SalesHierarchyModel();
					this.salesHierarchyModel.setBankUserId(bankUserId);
					this.alreadyAssociated=false;
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
			
		}
	}
	
	public void onChangeActive(AjaxBehaviorEvent event)
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentActionBean.onChangeActive Start");
		}
		if(this.salesHierarchyModel.isEditMode())
		{
			try 
			{
				List<RetailerContactModel> retailerContactModelList = new ArrayList<RetailerContactModel>();// = this.agentHierarchyManager.findChildRetailerContactsById(this.salesHierarchyModel.getRetailerContactId());
				for(RetailerContactModel retailerContactModel :retailerContactModelList)
				{
					if(retailerContactModel.getActive())
					{
						JSFContext.addErrorMessage(Constants.ErrorMessages.AH_AGENT_ACTIVE_ERROR);
						//this.salesHierarchyModel.setActive(Boolean.TRUE);
						break;
					}
				}
			} 
			catch (Exception e) 
			{
				logger.error(e);
			}
		}
		if(logger.isDebugEnabled()){
			logger.debug("AgentActionBean.onChangeActive End");
		}
	}

	public String isValueChanged(String strOldVal, String strNewVal){
		if(strOldVal.equals(strNewVal))
			return "black";
		return "red";
	}
	
	public SalesHierarchyModel getSalesHierarchyModel() {
		return salesHierarchyModel;
	}

	public void setSalesHierarchyModel(SalesHierarchyModel salesHierarchyModel) {
		this.salesHierarchyModel = salesHierarchyModel;
	}

	public void setAgentHierarchyManager(AgentHierarchyManager agentHierarchyManager) {
		this.agentHierarchyManager = agentHierarchyManager;
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

	public void setDeleteSecurityCheck(Boolean deleteSecurityCheck) {
		this.deleteSecurityCheck = deleteSecurityCheck;
	}
	public List<SelectItem> getAppUsers() {
		return appUsers;
	}

	public void setAppUsers(List<SelectItem> appUsers) {
		this.appUsers = appUsers;
	}
	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}

	public List<SelectItem> getUltimateAppUsers() {
		return ultimateAppUsers;
	}

	public void setUltimateAppUsers(List<SelectItem> ultimateAppUsers) {
		this.ultimateAppUsers = ultimateAppUsers;
	}

	public boolean isSaleUserHierarchyError() {
		return saleUserHierarchyError;
	}

	public void setSaleUserHierarchyError(boolean saleUserHierarchyError) {
		this.saleUserHierarchyError = saleUserHierarchyError;
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

	public Boolean getAlreadyAssociated() {
		return alreadyAssociated;
	}

	public void setAlreadyAssociated(Boolean alreadyAssociated) {
		this.alreadyAssociated = alreadyAssociated;
	}

	public List<SalesHierarchyHistoryModel> getSalesUserHierarchyHistoryModels() {
		return salesUserHierarchyHistoryModels;
	}

	public void setSalesUserHierarchyHistoryModels(
			List<SalesHierarchyHistoryModel> salesUserHierarchyHistoryModels) {
		this.salesUserHierarchyHistoryModels = salesUserHierarchyHistoryModels;
	}
		
}
