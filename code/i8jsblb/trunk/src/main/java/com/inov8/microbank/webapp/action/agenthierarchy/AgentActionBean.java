package com.inov8.microbank.webapp.action.agenthierarchy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.servlet.http.HttpServletRequest;
import javax.swing.tree.TreeNode;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.richfaces.component.UITree;
import org.richfaces.event.TreeSelectionChangeEvent;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.model.messagemodule.EmailMessage;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.microbank.common.exception.EmailServiceSendFailureException;
import com.inov8.microbank.common.jms.DestinationConstants;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.AreaModel;
import com.inov8.microbank.common.model.CityModel;
import com.inov8.microbank.common.model.DistributorLevelModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.DistrictModel;
import com.inov8.microbank.common.model.NatureOfBusinessModel;
import com.inov8.microbank.common.model.PartnerGroupDetailModel;
import com.inov8.microbank.common.model.PartnerGroupModel;
import com.inov8.microbank.common.model.PartnerModel;
import com.inov8.microbank.common.model.PermissionGroupDetailModel;
import com.inov8.microbank.common.model.PostalOfficeModel;
import com.inov8.microbank.common.model.ProductCatalogModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.RetailerModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.agenthierarchy.RegionModel;
import com.inov8.microbank.common.model.agenthierarchy.SalesHierarchyModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationHistoryModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.model.portal.partnergroupmodule.PartnerPermissionViewModel;
import com.inov8.microbank.common.model.retailermodule.RetailerContactListViewFormModel;
import com.inov8.microbank.common.model.retailermodule.RetailerContactSearchViewModel;
import com.inov8.microbank.common.model.usecasemodule.LevelCheckerModel;
import com.inov8.microbank.common.model.usecasemodule.UsecaseLevelModel;
import com.inov8.microbank.common.util.ActionAuthorizationConstantsInterface;
import com.inov8.microbank.common.util.AuthenticationUtil;
import com.inov8.microbank.common.util.AuthoirzationDetailEnum;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.Constants;
import com.inov8.microbank.common.util.JSFContext;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PermissionGroupConstants;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.SessionBeanObjects;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.facade.portal.authorizationmodule.ActionAuthorizationFacade;
import com.inov8.microbank.server.facade.portal.partnergroupmodule.PartnerGroupFacade;
import com.inov8.microbank.server.facade.usecasemodule.UsecaseFacade;
import com.inov8.microbank.server.service.addressmodule.AddressManager;
import com.inov8.microbank.server.service.agenthierarchy.AgentHierarchyManager;
import com.inov8.microbank.server.service.jms.JmsProducer;
import com.inov8.microbank.server.service.portal.authorizationmodule.ActionAuthorizationHistoryManager;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.ola.server.service.account.AccountManager;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import com.thoughtworks.xstream.XStream;


@ManagedBean(name="agentActionBean")
@SessionScoped
public class AgentActionBean 
{

	private static final Log logger = LogFactory.getLog(AgentActionBean.class);
	
	@ManagedProperty(value="#{agentHierarchyManager}") 
	private AgentHierarchyManager agentHierarchyManager;
	
	@ManagedProperty(value="#{referenceDataManager}")
	private ReferenceDataManager referenceDataManager;
	
	@ManagedProperty(value="#{smartMoneyAccountManager}")
	private SmartMoneyAccountManager smartMoneyAccountManager;
	
	@ManagedProperty(value="#{actionAuthorizationFacade}")
	protected ActionAuthorizationFacade actionAuthorizationFacade;
	
	@ManagedProperty(value="#{actionAuthorizationHistoryManager}")
	protected ActionAuthorizationHistoryManager actionAuthorizationHistoryManager;
	
	@ManagedProperty(value="#{usecaseFacade}")
	protected UsecaseFacade usecaseFacade;
	
	@ManagedProperty(value="#{jmsProducer}")
	protected JmsProducer jmsProducer;
	
	@ManagedProperty(value="#{mfsAccountManager}")
	private MfsAccountManager mfsAccountManager;
	
	@ManagedProperty(value="#{accountManager}")
	private AccountManager		accountManager;
	
	@ManagedProperty(value="#{userManager}") 
	private AppUserManager appUserManager;
	
	@ManagedProperty(value="#{addressManager}") 
	private AddressManager addressManager;
	
	@ManagedProperty(value="#{partnerGroupFacade}")
	private PartnerGroupFacade partnerGroupFacade;
	
	private RetailerContactListViewFormModel agentFormModel;

	private String parentAgentId;
	private String agentLevelId;
	private Long franchiseId;
	private String regionId;
	private String distributorId;

	// In case of edit agent, to check first name / Last Name is changed or not
	private String oldFirstName;
	private String oldLastName;

	private boolean childAgentFoundError;
	private boolean agentLevelIdError;
	private boolean parentSelectionError;

	private boolean saleUserHierarchyError;
	
	private boolean areaHierarchyError;
	
	private Long defaultValue = -1L;
	
	private List<SelectItem> distributors;
	private List<SelectItem> regions;
	private List<SelectItem> retailers;
	private List<SelectItem> agentLevels;
	private List<SelectItem> productCatalogs;
	private List<SelectItem> partnerGroups;
	private List<SelectItem> natureOfBusinesses;
	private List<SelectItem> districts;
	private List<SelectItem> cities;
	private List<SelectItem> postOffices;
	private List<SelectItem> parentAgents;
	
	private List<SelectItem> accountTypes; //added by Turab
	
	private List<RegionModel> regionModelList;
	private List<DistributorModel> distributorModelList;
	private List<RetailerModel> retailerModelList;
	private List<DistributorLevelModel> agentLevelModelList;
	private List<ProductCatalogModel> productCatalogModelList;
	private List<PartnerGroupModel> partnerGroupModelList;
	private List<NatureOfBusinessModel> natureOfBusinessModelList;
	private List<DistrictModel> districtsModelList;
	private List<CityModel> citiesModelList;
	private List<PostalOfficeModel> postOfficesModelList;
	private List<RetailerContactModel> parentAgentModelList;
	
	private List<TreeNode> areaNodes = new ArrayList<TreeNode>();
	private List<TreeNode> saleUserNodes = new ArrayList<TreeNode>();
    private TreeNode selectedNode;
    private TreeNode saleHierarchySelectedNode;
    private Boolean securityCheck= Boolean.FALSE;
    private Boolean editSecurityCheck=Boolean.FALSE;
    
	public AgentActionBean() {
		super();
	}

	public void loadSaleUserHierarchy(AjaxBehaviorEvent event)
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentActionBean.loadSaleUserHierarchy Start");
		}
		try 
		{
			this.saleUserHierarchyError = Boolean.FALSE;
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
					
					while(childSalesHierarchyModelList.size() > 0)
					{
						List<SalesHierarchyModel> tempSalesHierarchyModelList = new ArrayList<SalesHierarchyModel>();
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
	
	public void clearSaleHierarchyNodeSelection(ActionEvent event)
	{
		this.saleHierarchySelectedNode = null;
	}
	
	public void reRenderSaleHierarchyFields(ActionEvent event)
	{
		if(this.saleHierarchySelectedNode != null)
		{
			Node myNode = (Node)this.saleHierarchySelectedNode;
			this.agentFormModel.setSaleUserId(myNode.getId());
			this.agentFormModel.setSaleUserName(myNode.getName());
		}
	}
	
	@PostConstruct
	public void ini()
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentActionBean.ini Starts");
		}
		try
		{
			this.agentFormModel = new RetailerContactListViewFormModel();
			this.regions = new ArrayList<SelectItem>();
			this.retailers = new ArrayList<SelectItem>();
			this.agentLevels = new ArrayList<SelectItem>();
			this.distributors = new ArrayList<SelectItem>();
			this.productCatalogs = new ArrayList<SelectItem>();
			this.partnerGroups = new ArrayList<SelectItem>();
			this.natureOfBusinesses = new ArrayList<SelectItem>();
			this.districts = new ArrayList<SelectItem>();
			this.cities = new ArrayList<SelectItem>();
			this.postOffices = new ArrayList<SelectItem>();
			this.parentAgents = new ArrayList<SelectItem>();
			
			this.accountTypes = new ArrayList<SelectItem>();//added by Turab
			
			this.agentFormModel.setActive(Boolean.TRUE);
			this.agentFormModel.setEditMode(Boolean.FALSE);
			
			SearchBaseWrapper searchBaseWrapper = agentHierarchyManager.findAllDistributors();
			if(searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0)
			{
				distributorModelList = searchBaseWrapper.getCustomList().getResultsetList();
				
				for(DistributorModel model : distributorModelList)
				{
					if(model.getActive())	// Only Active Agent Network will be availabe to creat agents.
					{
						SelectItem item = new SelectItem();
						item.setLabel(model.getName());
						item.setValue(model.getDistributorId());
						this.distributors.add(item);
					}
				}
			}
			
			searchBaseWrapper = agentHierarchyManager.findAllProductCatalogs();
			if(searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0)
			{
				this.productCatalogModelList = searchBaseWrapper.getCustomList().getResultsetList();
				for(ProductCatalogModel model : productCatalogModelList)
				{
					SelectItem item = new SelectItem();
					item.setLabel(model.getName());
					item.setValue(model.getProductCatalogId());
					this.productCatalogs.add(item);
				}
			}
			
			/*searchBaseWrapper = agentHierarchyManager.findAllPartnerGroups();
			if(searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0)
			{
				this.partnerGroupModelList = searchBaseWrapper.getCustomList().getResultsetList();
				for(PartnerGroupModel model : partnerGroupModelList)
				{
					SelectItem item = new SelectItem();
					item.setLabel(model.getName());
					item.setValue(model.getPartnerGroupId());
					this.partnerGroups.add(item);
				}
			}*/
			
			NatureOfBusinessModel natureOfBusinessModel = new NatureOfBusinessModel();
			ReferenceDataWrapper natureOfBusinessReferenceDataWrapper = new ReferenceDataWrapperImpl(natureOfBusinessModel, "name", SortingOrder.ASC);
			natureOfBusinessReferenceDataWrapper.setBasePersistableModel(natureOfBusinessModel);
			referenceDataManager.getReferenceData(natureOfBusinessReferenceDataWrapper);
		    if (natureOfBusinessReferenceDataWrapper.getReferenceDataList() != null && natureOfBusinessReferenceDataWrapper.getReferenceDataList().size() > 0)
		    {
		    	this.natureOfBusinessModelList = natureOfBusinessReferenceDataWrapper.getReferenceDataList();
				for(NatureOfBusinessModel model : natureOfBusinessModelList)
				{
					boolean sectorFound = Boolean.FALSE;
					for(SelectItem item: this.natureOfBusinesses)
					{
						SelectItemGroup group = (SelectItemGroup)item;
						if(group.getLabel().equals(model.getMerchantGroup()))
						{
							sectorFound = Boolean.TRUE;
							break;
						}
					}
					
					if(!sectorFound)
					{
						SelectItemGroup group = new SelectItemGroup(model.getMerchantGroup());
						group.setNoSelectionOption(Boolean.TRUE);
						this.natureOfBusinesses.add(group);
					}
					
				}
				
				for(NatureOfBusinessModel model : natureOfBusinessModelList)
				{
					for(SelectItem item: this.natureOfBusinesses)
					{
						SelectItemGroup group = (SelectItemGroup)item;
						if(group.getLabel().equals(model.getMerchantGroup()))
						{
							SelectItem selectItem = new SelectItem();
							selectItem.setLabel(model.getName());
							selectItem.setValue(model.getNatureOfBusinessId());
							List<SelectItem> selectItemList = new ArrayList<SelectItem>(Arrays.asList(group.getSelectItems()));
							selectItemList.add(selectItem);
							SelectItem selectItemArray[] = selectItemList.toArray(new SelectItem[selectItemList.size()]);
							group.setSelectItems(selectItemArray);
							break;
						}
					}
				}
		    }
		    
		    ///added by turab to get reference data
		    OlaCustomerAccountTypeModel customerAccountTypeModel = new OlaCustomerAccountTypeModel();
		    customerAccountTypeModel.setActive(true);
		    customerAccountTypeModel.setIsCustomerAccountType(false); //added by Turab
		    ReferenceDataWrapper customerAccountTypeDataWrapper = new ReferenceDataWrapperImpl(customerAccountTypeModel, "name", SortingOrder.ASC);
		    customerAccountTypeDataWrapper.setBasePersistableModel(customerAccountTypeModel);
		    try
		    {
		      referenceDataManager.getReferenceData(customerAccountTypeDataWrapper);
		    }
		    catch (Exception e)
		    {
		    	logger.error(e.getMessage(), e);
		    }
		    
		    CopyOnWriteArrayList<OlaCustomerAccountTypeModel> customerAccountTypeList = null;
		    if (customerAccountTypeDataWrapper.getReferenceDataList() != null)
		    {
		    	customerAccountTypeList = new CopyOnWriteArrayList<OlaCustomerAccountTypeModel>(customerAccountTypeDataWrapper.getReferenceDataList());
		    	if(! CollectionUtils.isEmpty(customerAccountTypeList)){
	            	//remove special account types from screen. like settlemnt account type is used for commission settlemnt in OLA and walkin customer. it needs to be removed
	            	//because it is for system use only.
	            	removeSpecialAccountTypes(customerAccountTypeList);
	            }
		    	for(OlaCustomerAccountTypeModel model : customerAccountTypeList)
				{
					SelectItem item = new SelectItem();
					item.setLabel(model.getName());
					item.setValue(model.getCustomerAccountTypeId());
					this.accountTypes.add(item);
				}
		    }
		    
		    //end by turab reference data
		    DistrictModel districtModel = new DistrictModel();
		    ReferenceDataWrapper districtReferenceDataWrapper = new ReferenceDataWrapperImpl(districtModel, "name", SortingOrder.ASC);
		    districtReferenceDataWrapper.setBasePersistableModel(districtModel);
		    referenceDataManager.getReferenceData(districtReferenceDataWrapper);
		    if (districtReferenceDataWrapper.getReferenceDataList() != null && districtReferenceDataWrapper.getReferenceDataList().size() > 0)
		    {
		    	this.districtsModelList = districtReferenceDataWrapper.getReferenceDataList();
				for(DistrictModel model : districtsModelList)
				{
					SelectItem item = new SelectItem();
					item.setLabel(model.getName());
					item.setValue(model.getDistrictId());
					this.districts.add(item);
				}
		    }
		    
		    CityModel cityModel = new CityModel();
		    ReferenceDataWrapper cityReferenceDataWrapper = new ReferenceDataWrapperImpl(cityModel, "name", SortingOrder.ASC);
		    cityReferenceDataWrapper.setBasePersistableModel(cityModel);
		    referenceDataManager.getReferenceData(cityReferenceDataWrapper);
		    if (cityReferenceDataWrapper.getReferenceDataList() != null && cityReferenceDataWrapper.getReferenceDataList().size() > 0)
		    {
		    	this.citiesModelList = cityReferenceDataWrapper.getReferenceDataList();
				for(CityModel model : citiesModelList)
				{
					SelectItem item = new SelectItem();
					item.setLabel(model.getName());
					item.setValue(model.getCityId());
					this.cities.add(item);
				}
		    }
		    
		    PostalOfficeModel postalOfficeModel = new PostalOfficeModel();
		    ReferenceDataWrapper postalOfficeReferenceDataWrapper = new ReferenceDataWrapperImpl(postalOfficeModel, "name", SortingOrder.ASC);
		    postalOfficeReferenceDataWrapper.setBasePersistableModel(postalOfficeModel);
		    referenceDataManager.getReferenceData(postalOfficeReferenceDataWrapper);
		    if (postalOfficeReferenceDataWrapper.getReferenceDataList() != null && postalOfficeReferenceDataWrapper.getReferenceDataList().size() > 0)
		    {
		    	this.postOfficesModelList = postalOfficeReferenceDataWrapper.getReferenceDataList();
				for(PostalOfficeModel model : postOfficesModelList)
				{
					SelectItem item = new SelectItem();
					item.setLabel(model.getName());
					item.setValue(model.getPostalOfficeId());
					this.postOffices.add(item);
				}
		    }
		    
		    String retailerContactId = (String)JSFContext.getFromRequest("retailerContactId");
		    String distributorId = (String)JSFContext.getFromRequest("distributorId");
		    String isReSubmit =(String) JSFContext.getFromRequest("isReSubmit");
			
			
			/// Added for Resubmit Authorization Request 
			if(null!=isReSubmit && isReSubmit.equals("true")){
				String actionAuthorizationIdStr = (String)JSFContext.getFromRequest("authId");
				Long actionAuthorizationId = Long.parseLong(actionAuthorizationIdStr);
				ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(actionAuthorizationId);
				
				if(actionAuthorizationModel.getCreatedById().longValue()!=UserUtils.getCurrentUser().getAppUserId().longValue()){
					throw new FrameworkCheckedException("illegal operation performed");
				}
				
				XStream xstream = new XStream();
				RetailerContactListViewFormModel retailerContactListViewFormModel = (RetailerContactListViewFormModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
								
				this.agentFormModel =  retailerContactListViewFormModel;
				this.loadRegions();
				this.loadRetailers();
				this.loadAgentLevels();
				this.loadParentAgents();
				this.loadPartnerGroups();
			}
			///End Added for Resubmit Authorization Request
			else if(retailerContactId != null && distributorId == null)
		    {
		    	AppUserModel appUserModel = this.appUserManager.fetchL3Agent(Long.valueOf(retailerContactId));
		    	if(appUserModel != null)
		    	{
		    		/*searchBaseWrapper = new SearchBaseWrapperImpl();*/
		    		
		    		this.agentFormModel.setRetailerContactId(Long.valueOf(retailerContactId));
		    		this.agentFormModel.setFirstName(appUserModel.getFirstName());
		    		this.agentFormModel.setLastName(appUserModel.getLastName());
		    		this.agentFormModel.setEmail(appUserModel.getEmail());
		    		this.agentFormModel.setCnicNo(appUserModel.getNic());
		    		this.agentFormModel.setMobileNo(appUserModel.getMobileNo());
		    		this.agentFormModel.setCnicExpiryDate(appUserModel.getNicExpiryDate());
		    		
		    		RetailerContactModel retailerContactModel = this.agentHierarchyManager.findRetailerContactById(Long.valueOf(retailerContactId));
		    		
		    		this.agentFormModel.setApplicationNo(retailerContactModel.getApplicationNo());
		    		
		    		
					/*searchBaseWrapper.putObject("agentFormModel", this.agentFormModel);
					this.agentHierarchyManager.findL3Agent(searchBaseWrapper);
					
					this.oldFirstName = this.agentFormModel.getFirstName();
					this.oldLastName = this.agentFormModel.getLastName();
					
					BaseWrapper baseWrapper = new BaseWrapperImpl();
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
	        		}*/
					//this.agentFormModel.setEditMode(Boolean.TRUE);
		    	}
		    	else
		    	{
		    		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Agent not registered in the system."));
		    	}
		    }
		    
		    String createButton=PortalConstants.ADMIN_GP_CREATE+","+PortalConstants.PG_GP_CREATE+","+
                    PortalConstants.MNG_AGNTS_CREATE;
            securityCheck=AuthenticationUtil.checkRightsIfAny(createButton);
            
            String updateButton=PortalConstants.ADMIN_GP_UPDATE+","+PortalConstants.PG_GP_UPDATE+","+
                    PortalConstants.MNG_AGNTS_UPDATE;
			editSecurityCheck=AuthenticationUtil.checkRightsIfAny(updateButton);
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
			logger.debug("AgentActionBean.ini End");
		}
	}

	public void clearAllSessionObjects(){
		if (!FacesContext.getCurrentInstance().isPostback()) {
			SessionBeanObjects.removeAllSessionObjects();
		}
	}
	
	public String cancel()
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentActionBean.cancel Starts");
		}
		SessionBeanObjects.removeAllSessionObjects();
		if(logger.isDebugEnabled()){
			logger.debug("AgentActionBean.cancel End");
		}
		return Constants.ReturnCodes.CANCEL_AGENT;
	}
	
	public void clearAgentForm(ActionEvent event)
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentActionBean.clearAgentForm Starts");
		}
		this.agentFormModel = new RetailerContactListViewFormModel();
		this.regions.clear();
		this.retailers.clear();
		this.agentLevels.clear();
		selectedNode = null;
		this.agentFormModel.setActive(Boolean.TRUE);
		this.agentFormModel.setEditMode(Boolean.FALSE);
		if(logger.isDebugEnabled()){
			logger.debug("AgentActionBean.clearAgentForm End");
		}
	}

	public void deleteAgent(ActionEvent event)
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentActionBean.deleteAgent Starts");
		}
		try
		{
			//		TODO This Method need some changes
			
			String retailerContactId = (String)JSFContext.getFromRequest("retailerContactId");
			
			retailerContactId = "67302";
			
			this.agentFormModel.setRetailerContactId(Long.valueOf(retailerContactId));
			
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			searchBaseWrapper.putObject("agentFormModel", this.agentFormModel);
			this.agentHierarchyManager.deleteAgent(searchBaseWrapper);
			
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
			logger.debug("AgentActionBean.deleteAgent End");
		}
	}
	
	public String editAgent()
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentActionBean.editAgent from Agent Hierarchy Starts");
		}
		try
		{
			String retailerContactId = (String)JSFContext.getFromRequest("retailerContactId");
			String distributorId = (String)JSFContext.getFromRequest("distributorId");
			String regionId = (String)JSFContext.getFromRequest("regionId");
			
			if(retailerContactId != null && distributorId != null && regionId != null)
			{
				this.agentFormModel.setDistributorId(distributorId);
				this.agentFormModel.setRegionId(regionId);
				this.agentFormModel.setRetailerContactId(Long.valueOf(retailerContactId));
				
				this.loadRegions();
				this.loadRetailers();
				this.loadAgentLevels();
				
				SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
				searchBaseWrapper.putObject("agentFormModel", this.agentFormModel);
				this.agentHierarchyManager.findAgent(searchBaseWrapper);
				
				this.parentAgentId = this.agentFormModel.getParentRetailerContactId();
				this.agentLevelId = this.agentFormModel.getDistributorLevelId();
				this.franchiseId = this.agentFormModel.getRetailerId();
				this.regionId = this.agentFormModel.getRegionId();
				this.distributorId = this.agentFormModel.getDistributorId();
				this.oldFirstName = this.agentFormModel.getFirstName();
				this.oldLastName = this.agentFormModel.getLastName();
				
				List<RetailerContactModel> retailerContactModels = this.agentHierarchyManager.findChildRetailerContactsById(this.agentFormModel.getRetailerContactId());
				if(retailerContactModels != null && retailerContactModels.size() > 0)
				{
					this.childAgentFoundError = Boolean.TRUE;
				}
				
				BaseWrapper baseWrapper = new BaseWrapperImpl();
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
        		
				this.agentFormModel.setEditMode(Boolean.TRUE);
				this.loadParentAgents();
				this.loadPartnerGroups();
				
				if(this.agentFormModel.getParentRetailerContactId() != null && !this.agentFormModel.getParentRetailerContactId().equals("-1"))
				{
					for(RetailerContactModel retailerContactModel :this.parentAgentModelList)
					{
						if(this.agentFormModel.getParentRetailerContactId().equals(String.valueOf(retailerContactModel.getRetailerContactId())))
						{
							this.agentFormModel.setActiveDisabled(!retailerContactModel.getActive());
							break;
						}		
					}
				}
			}
		}
		catch(FrameworkCheckedException fce)
		{
			logger.error(fce);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(e);
		}
		if(logger.isDebugEnabled()){
			logger.debug("AgentActionBean.editAgent from Agent Hierarchy End");
		}
		return Constants.ReturnCodes.AGENT_HIERARCHY_ADD_AGENT;
	}
	
	public void onChangeActive(AjaxBehaviorEvent event)
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentActionBean.onChangeActive Start");
		}
		if(this.agentFormModel.isEditMode())
		{
			try 
			{
				List<RetailerContactModel> retailerContactModelList = this.agentHierarchyManager.findChildRetailerContactsById(this.agentFormModel.getRetailerContactId());
				for(RetailerContactModel retailerContactModel :retailerContactModelList)
				{
					if(retailerContactModel.getActive())
					{
						JSFContext.addErrorMessage(Constants.ErrorMessages.AH_AGENT_ACTIVE_ERROR);
						this.agentFormModel.setActive(Boolean.TRUE);
						break;
					}
				}
			} 
			catch (FrameworkCheckedException e) 
			{
				logger.error(e);
			}
		}
		if(logger.isDebugEnabled()){
			logger.debug("AgentActionBean.onChangeActive End");
		}
	}
	
	public String editAgent(RetailerContactSearchViewModel model)
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentActionBean.editAgent Start");
		}
		try
		{
			this.agentFormModel.setDistributorId(model.getDistributorId().toString());
			this.agentFormModel.setRegionId(model.getRegionId().toString());
			this.agentFormModel.setRetailerContactId(model.getRetailerContactId());
			this.agentFormModel.setCnicNo(model.getCnic());
			
			this.agentFormModel.setCnicExpiryDate(model.getCnicExpiryDate());
			
			/*this.agentFormModel.setDistributorId("1326");
			this.agentFormModel.setRegionId("103");
			this.agentFormModel.setRetailerContactId(67302L);*/
			
			this.loadRegions();
			this.loadRetailers();
			this.loadAgentLevels();
			
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			searchBaseWrapper.putObject("agentFormModel", this.agentFormModel);
			this.agentHierarchyManager.findAgent(searchBaseWrapper);
			
			this.parentAgentId = this.agentFormModel.getParentRetailerContactId();
			this.agentLevelId = this.agentFormModel.getDistributorLevelId();
			this.franchiseId = this.agentFormModel.getRetailerId();
			this.regionId = this.agentFormModel.getRegionId();
			this.distributorId = this.agentFormModel.getDistributorId();
			this.oldFirstName = this.agentFormModel.getFirstName();
			this.oldLastName = this.agentFormModel.getLastName();

			List<RetailerContactModel> retailerContactModels = this.agentHierarchyManager.findChildRetailerContactsById(this.agentFormModel.getRetailerContactId());
			if(retailerContactModels != null && retailerContactModels.size() > 0)
			{
				this.childAgentFoundError = Boolean.TRUE;
			}
			
			BaseWrapper baseWrapper = new BaseWrapperImpl();
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
    			if (smartMoneyAccountModel.getRetailerContactIdRetailerContactModel() != null){
    				this.agentFormModel.setAccountType(smartMoneyAccountModel.getRetailerContactIdRetailerContactModel().getOlaCustomerAccountTypeModelId());
    			}
    		}
			
			this.agentFormModel.setEditMode(Boolean.TRUE);
			//this.agentFormModel.setDisabled(Boolean.TRUE);
			this.loadParentAgents();
			this.loadPartnerGroups();
			
			if(null != this.parentAgentModelList && this.agentFormModel.getParentRetailerContactId() != null && !this.agentFormModel.getParentRetailerContactId().equals("null"))
			{
				for(RetailerContactModel retailerContactModel :this.parentAgentModelList)
				{
					if(this.agentFormModel.getParentRetailerContactId().equals(String.valueOf(retailerContactModel.getRetailerContactId())))
					{
						this.agentFormModel.setActiveDisabled(!retailerContactModel.getActive());
						break;
					}		
				}
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
			logger.debug("AgentActionBean.editAgent End");
		}
		return Constants.ReturnCodes.SEARCH_AGENT_ADD_AGENT;
	}
	
	public String saveOrUpdate()
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("AgentActionBean.saveOrUpdate Start");
		}
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		try
		{
			if(validate())
			{
				 Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
				 String resubmitRequestStr = params.get("resubmitRequest");
				 boolean resubmitRequest = false;
				 String actionAuthorizationIdStr = params.get("actionAuthorizationId");
				 Long  actionAuthorizationId = null;
				 if(null!=resubmitRequestStr && resubmitRequestStr.equals("true")){
					 actionAuthorizationId = Long.parseLong(actionAuthorizationIdStr);
					 resubmitRequest = Boolean.parseBoolean(resubmitRequestStr);
				 }
				UsecaseModel usecaseModel = usecaseFacade.loadUsecase(PortalConstants.RETAILER_CONTACT_FORM_USECASE_ID);			
				if(((!this.agentFormModel.isEditMode()) && usecaseModel.getIsAuthorizationEnable()) || resubmitRequest)////Excecutes If authorization enable for Create Agent 
				{
					XStream xstream = new XStream();
					RetailerContactListViewFormModel retailerContactListViewFormModel = this.agentFormModel;
					
					this.agentFormModel.setHead(Boolean.FALSE);
					for(DistributorLevelModel model: this.agentLevelModelList)
					{
						if(model.getDistributorLevelId().toString().equals(this.agentFormModel.getDistributorLevelId()) && model.getManagingLevelId() == null)
						{
							this.agentFormModel.setHead(Boolean.TRUE);
							break;
						}
					}
					this.agentFormModel.setUpdateAccountInfo(this.isFirstNameLastNameChanged());
					
					////////////Setting Reference Data/////
					if(null!=this.regionModelList){
						for(RegionModel model: this.regionModelList)
						{
							if(model.getRegionId().toString().equals(this.agentFormModel.getRegionId().toString()))
							{
								this.agentFormModel.setRegionName(model.getRegionName());
								break;
							}
						}
					}
					if(null!= this.distributorModelList){
						for(DistributorModel model: this.distributorModelList)
						{
							if(model.getDistributorId().toString().equals(this.agentFormModel.getDistributorId().toString()))
							{
								this.agentFormModel.setDistributorName(model.getName());
								break;
							}
						}
					}
					
				//by turab, if head agent selected add its franchise
				if(this.agentFormModel.getRetailerId().toString().equals("-2")){
					RetailerModel retailerModel = new RetailerModel();
					retailerModel.setActive(Boolean.TRUE);
					retailerModel.setAddress1("auto address1");
					retailerModel.setAddress2("auto address2");
					retailerModel.setAreaId(this.agentFormModel.getAreaId());
					retailerModel.setCity(this.agentFormModel.getCity());
					retailerModel.setComments(this.agentFormModel.getComments());
					retailerModel.setContactName("auto contact");
					retailerModel.setCountry("Pakistan");
					retailerModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
					retailerModel.setCreatedOn(new Date());
					retailerModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
					retailerModel.setUpdatedOn(new Date());
					retailerModel.setDescription(this.agentFormModel.getDescription());
					retailerModel.setDistributorId(Long.parseLong(this.agentFormModel.getDistributorId()));
					retailerModel.setName(this.agentFormModel.getUsername()+"_"+"Franchise");
					retailerModel.setPhoneNo(this.agentFormModel.getContactNo());
					retailerModel.setProductCatalogueId(Long.parseLong(this.agentFormModel.getProductCatalogId()));
					retailerModel.setRegionModelId(Long.parseLong(this.agentFormModel.getRegionId()));
//					retailerModel.setRetailerTypeId(1002L);
					retailerModel.setEditMode(Boolean.FALSE);
					//retailerModel.setVersionNo(0);
					this.agentFormModel.setRetailerName(retailerModel.getName());
					BaseWrapper baseWrapperFranchise = new BaseWrapperImpl();
					baseWrapperFranchise.setBasePersistableModel(retailerModel);
					
					baseWrapperFranchise.putObject("permissionGroupId", PermissionGroupConstants.RETAILOR);
					try{
						baseWrapperFranchise = agentHierarchyManager.saveOrUpdateFranchise(baseWrapperFranchise);
					}catch(Exception e){
						JSFContext.addErrorMessage("Frachnise already exists with this name.");
					}
					retailerModel = (RetailerModel) baseWrapperFranchise.getBasePersistableModel();
					this.agentFormModel.setRetailerId(retailerModel.getRetailerId());
					
				}else{
					if(null!= this.retailerModelList){
					for(RetailerModel model: this.retailerModelList)
					{
							if(model.getRetailerId().toString().equals(this.agentFormModel.getRetailerId().toString()))
							{
								this.agentFormModel.setRetailerName(model.getName());
								break;
							}
						}
					}
				}
					
				if(null!= this.agentLevelModelList){
					for(DistributorLevelModel model: this.agentLevelModelList)
					{
						if(model.getDistributorLevelId().toString().equals(this.agentFormModel.getDistributorLevelId().toString()))
						{
							this.agentFormModel.setDistributorLevelName(model.getName());
							break;
						}
					}
				}
				if(null!= this.productCatalogModelList){
					for(ProductCatalogModel model: this.productCatalogModelList)
					{
						if(model.getProductCatalogId().toString().equals(this.agentFormModel.getProductCatalogId().toString()))
						{
							this.agentFormModel.setProductCatalogName(model.getName());
							break;
						}
					}
				}
					
					//by turab, if head agent is selected add its UserGroup
					if(this.agentFormModel.getPartnerGroupId().toString().equals("-2")){
						Set<Long> permission = new HashSet<Long>();
						baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
						baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.USER_GROUP_CREATE_USECASE_ID));

						
						PartnerGroupModel partnerGroupModel = new PartnerGroupModel();
						//PartnerGroupPermissionListViewModel partnerGroupPermissionListViewModel = (PartnerGroupPermissionListViewModel)object;
						PartnerModel partnerGroupPermissionListViewModel = new PartnerModel();
						partnerGroupPermissionListViewModel = agentHierarchyManager.findPartnerByRetailerId(this.agentFormModel.getRetailerId());
//							partnerGroupPermissionListViewModel.setPartnerId(3518L); //yousaf temp	
						partnerGroupModel.setName(this.agentFormModel.getUsername()+"_"+"PartnerGroup");
						
						PartnerPermissionViewModel partnerPermissionViewModel = new PartnerPermissionViewModel();
						partnerPermissionViewModel.setPartnerId(partnerGroupModel.getPartnerId());
						partnerPermissionViewModel.setIsDefault(true);//Load Default permissions like home page & change password
						SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
						searchBaseWrapper.setBasePersistableModel(partnerPermissionViewModel);
						searchBaseWrapper = this.partnerGroupFacade.searchDefaultPartnerPermission(searchBaseWrapper);
						Iterator<PartnerPermissionViewModel> itrDP = searchBaseWrapper.getCustomList().getResultsetList().iterator();
						while(itrDP.hasNext())
						{
							PartnerPermissionViewModel ppModel = itrDP.next();
							PartnerGroupDetailModel partnerGroupDetailModel = new PartnerGroupDetailModel();
							partnerGroupDetailModel.setUserPermissionId(ppModel.getUserPermissionId());
							partnerGroupDetailModel.setReadAllowed(ppModel.getReadAvailable());
							partnerGroupDetailModel.setUpdateAllowed(ppModel.getUpdateAvailable());
							partnerGroupDetailModel.setDeleteAllowed(ppModel.getDeleteAvailable());
							partnerGroupDetailModel.setCreateAllowed(ppModel.getCreateAvailable());
							partnerGroupDetailModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
							partnerGroupDetailModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
							partnerGroupDetailModel.setCreatedOn(new Date());
							partnerGroupDetailModel.setUpdatedOn(new Date());
							
							if(permission.add(partnerGroupDetailModel.getUserPermissionId()))
							{
								partnerGroupModel.addPartnerGroupIdPartnerGroupDetailModel(partnerGroupDetailModel);//add default permissions like home page and change password
							}	
						}
						
						//select retailer default permissions and add them- turab Jan 09 2015
						PermissionGroupDetailModel retailerPermissionGroupDetail = new PermissionGroupDetailModel();
						retailerPermissionGroupDetail.setPermissionGroupId(PermissionGroupConstants.RETAILOR);
						ReferenceDataWrapper permissionGrpDtlReferenceDataWrapper = new ReferenceDataWrapperImpl(retailerPermissionGroupDetail, "permissionGroupDetailId", SortingOrder.ASC);
						permissionGrpDtlReferenceDataWrapper.setBasePersistableModel(retailerPermissionGroupDetail);
					    referenceDataManager.getReferenceData(permissionGrpDtlReferenceDataWrapper);
					    if(permissionGrpDtlReferenceDataWrapper.getReferenceDataList() != null && permissionGrpDtlReferenceDataWrapper.getReferenceDataList().size()>0){
					    	List<PermissionGroupDetailModel> list = permissionGrpDtlReferenceDataWrapper.getReferenceDataList();
					    	for(PermissionGroupDetailModel permissionGrpDtl : list){
					    		PartnerGroupDetailModel partnerGroupDetailModel = new PartnerGroupDetailModel();
								partnerGroupDetailModel.setUserPermissionId(permissionGrpDtl.getUserPermissionId());
								/*partnerGroupDetailModel.setReadAllowed(permissionGrpDtl.getUserPermissionIdUserPermissionModel().getReadAvailable());
								partnerGroupDetailModel.setUpdateAllowed(permissionGrpDtl.getUserPermissionIdUserPermissionModel().getUpdateAvailable());
								partnerGroupDetailModel.setDeleteAllowed(permissionGrpDtl.getUserPermissionIdUserPermissionModel().getDeleteAvailable());
								partnerGroupDetailModel.setCreateAllowed(permissionGrpDtl.getUserPermissionIdUserPermissionModel().getCreateAvailable());
								*/
								partnerGroupDetailModel.setReadAllowed(false);
								partnerGroupDetailModel.setUpdateAllowed(false);
								partnerGroupDetailModel.setDeleteAllowed(false);
								partnerGroupDetailModel.setCreateAllowed(false);
								
								partnerGroupDetailModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
								partnerGroupDetailModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
								partnerGroupDetailModel.setCreatedOn(new Date());
								partnerGroupDetailModel.setUpdatedOn(new Date());
								
								if(permission.add(partnerGroupDetailModel.getUserPermissionId()))
								{
									partnerGroupModel.addPartnerGroupIdPartnerGroupDetailModel(partnerGroupDetailModel);//add default permissions like home page and change password
								}
					    	}
					    }
						//end by turab Jan 09 2015
						
						partnerGroupModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
						partnerGroupModel.setCreatedOn(new Date());
						partnerGroupModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
						partnerGroupModel.setUpdatedOn(new Date());
						partnerGroupModel.setActive(Boolean.TRUE);
						partnerGroupModel.setEditable(Boolean.TRUE);
						partnerGroupModel.setPartnerId(partnerGroupPermissionListViewModel.getPartnerId());
						
						baseWrapper.setBasePersistableModel(partnerGroupModel);
						try
						{
							baseWrapper = this.partnerGroupFacade.createPartnerGroup(baseWrapper);
						}catch(FrameworkCheckedException fce)
						{			
							fce.printStackTrace();
					        throw fce;
						}

						this.agentFormModel.setPartnerGroupName(partnerGroupModel.getName());
						partnerGroupModel = (PartnerGroupModel) baseWrapper.getBasePersistableModel();
						this.agentFormModel.setPartnerGroupId(partnerGroupModel.getPartnerGroupId());
						
						
					}else{
					if(null!= this.partnerGroupModelList){
					for(PartnerGroupModel model: this.partnerGroupModelList)
					{
						if(model.getPartnerGroupId().toString().equals(this.agentFormModel.getPartnerGroupId().toString()))
						{
							this.agentFormModel.setPartnerGroupName(model.getName());
							break;
						}
					}
					}
				}
				if(null!= this.natureOfBusinessModelList){
					for(NatureOfBusinessModel model: this.natureOfBusinessModelList)
					{
						if(model.getNatureOfBusinessId().toString().equals(this.agentFormModel.getNatureOfBusiness().toString()))
						{
							this.agentFormModel.setNatureOfBusinessName(model.getName());
							break;
						}
					}
				}	
				if(null!= this.districtsModelList && (null!=this.agentFormModel.getDistrictTehsilTown())){
					for(DistrictModel model: this.districtsModelList)
					{
						if(model.getDistrictId().toString().equals(this.agentFormModel.getDistrictTehsilTown().toString()))
						{
							this.agentFormModel.setDistrictTehsilTownName(model.getName());
							break;
						}
					}
				}	
				if(null!= this.citiesModelList && (null!=this.agentFormModel.getCityVillage())){	
					for(CityModel model: this.citiesModelList)
					{
						if(model.getCityId().toString().equals(this.agentFormModel.getCityVillage().toString()))
						{
							this.agentFormModel.setCityVillageName(model.getName());
							break;
						}
					}
				}	
				if(null!= this.postOfficesModelList && (null!=this.agentFormModel.getPostOffice())){
					for(PostalOfficeModel model: this.postOfficesModelList)
					{
						if(model.getPostalOfficeId().toString().equals(this.agentFormModel.getPostOffice().toString()))
						{
							this.agentFormModel.setPostOfficeName(model.getName());
							break;
						}
					}
				}	
				if(this.parentAgentModelList != null && (null!=this.agentFormModel.getParentRetailerContactId()))
				{
					for(RetailerContactModel model: this.parentAgentModelList)
					{
						if(model.getRetailerContactId().toString().equals(this.agentFormModel.getParentRetailerContactId().toString()))
						{
							this.agentFormModel.setParentRetailerContactName(model.getName());
							break;
						}
					}	
				}
				List<SalesHierarchyModel> saleUserModelList;
				SearchBaseWrapper searchBaseWrapper = this.agentHierarchyManager.findAllSaleUsers();
				saleUserModelList=searchBaseWrapper.getCustomList().getResultsetList();
				if(null != saleUserModelList)
				{
					for(SalesHierarchyModel model: saleUserModelList)
					{
						if(model.getSalesHierarchyId().toString().equals(this.agentFormModel.getSaleUserId().toString()))
						{
							this.agentFormModel.setSaleUserName(model.getBankUserAppUserModel().getUsername());
							break;
						}
					}	
				}
				
					
					//////////////////////////////////////
					
					String refDataModelString= xstream.toXML(retailerContactListViewFormModel);
					HttpServletRequest request = (HttpServletRequest) (FacesContext.getCurrentInstance().getExternalContext().getRequest());
					Long nextAuthorizationLevel = usecaseFacade.getNextAuthorizationLevel(PortalConstants.RETAILER_CONTACT_FORM_USECASE_ID,new Long(0));
					if(nextAuthorizationLevel.intValue()<1)
					{
						baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
						baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.RETAILER_CONTACT_FORM_USECASE_ID);		
						
						this.agentFormModel.setUpdateAccountInfo(this.isFirstNameLastNameChanged());
						
						baseWrapper.putObject("agentFormModel", this.agentFormModel);
						this.agentHierarchyManager.saveOrUpdateAgent(baseWrapper);
						
						//reload the franchise and user group so that newly added can be listed in dropdown
						this.loadRetailers();
						this.loadPartnerGroups();
						
						this.agentFormModel = (RetailerContactListViewFormModel) baseWrapper.getObject("agentFormModel");
						
						this.parentAgentId = this.agentFormModel.getParentRetailerContactId();
						this.agentLevelId = this.agentFormModel.getDistributorLevelId();
						this.franchiseId = this.agentFormModel.getRetailerId();
						this.regionId = this.agentFormModel.getRegionId();
						this.distributorId = this.agentFormModel.getDistributorId();
						this.oldFirstName = this.agentFormModel.getFirstName();
						this.oldLastName = this.agentFormModel.getLastName();
						
						//update account table with ola_customer_account_type_id by turab
						
						/*OLAVO olaVo = new OLAVO(); //		commented by Rashid
					
						olaVo.setCnic(this.agentFormModel.getCnicNo());
						olaVo = mfsAccountManager.getAccountInfoFromOLA(this.agentFormModel.getCnicNo(), FinancialInstitutionConstants.OLA_FINANCIAL_INSTITUTION);

						olaVo.setCustomerAccountTypeId(this.agentFormModel.getAccountType());
						AccountModel accountModel = new AccountModel();
						accountModel.setAccountId(olaVo.getAccountId());
						baseWrapper = new BaseWrapperImpl() ;
						baseWrapper.setBasePersistableModel(accountModel) ;
						
						baseWrapper = accountManager.loadAccount(baseWrapper);
						accountModel = (AccountModel) baseWrapper.getBasePersistableModel();
						OlaCustomerAccountTypeModel customerAccountTypeModel = new OlaCustomerAccountTypeModel();
						customerAccountTypeModel.setCustomerAccountTypeId(olaVo.getCustomerAccountTypeId());
						accountModel.setCustomerAccountTypeModel(customerAccountTypeModel);
						accountModel.setUpdatedOn(new Date());
						baseWrapper.setBasePersistableModel(accountModel);
						baseWrapper = accountManager.updateAccount(baseWrapper);*/
						
						if(agentFormModel.isEditMode())
						{
							int versionNo = agentFormModel.getVersionNo();
							versionNo++;
							agentFormModel.setVersionNo(versionNo);
							int appUserVersionNo = agentFormModel.getAppUserVersionNo();
							appUserVersionNo++;
							agentFormModel.setAppUserVersionNo(appUserVersionNo);
							long appUserPartnerGroupVersionNo = agentFormModel.getAppUserPartnerGroupVersionNo();
							appUserPartnerGroupVersionNo++;
							agentFormModel.setAppUserPartnerGroupVersionNo(appUserPartnerGroupVersionNo);
						}
						agentFormModel.setEditMode(Boolean.TRUE);
								
						actionAuthorizationId = performActionWithAllIntimationLevels(nextAuthorizationLevel,"", refDataModelString, usecaseModel,actionAuthorizationId, request);
						JSFContext.setInfoMessage("Action is authorized against reference Action ID : "+actionAuthorizationId+". Agent [ID = " + agentFormModel.getUserDeviceAccountUserId() + "] is saved/updated successfully.");
					}
					else
					{									
						actionAuthorizationId = createAuthorizationRequest(nextAuthorizationLevel,"", refDataModelString, usecaseModel.getUsecaseId(),this.agentFormModel.getCnicNo(),resubmitRequest,actionAuthorizationId,request);
						JSFContext.setInfoMessage("Action is pending for approval against reference Action ID : "+actionAuthorizationId);
					}
				}
				else
				{	
					this.agentFormModel.setHead(Boolean.FALSE);
					
					////////turab
					//by turab, if head agent selected add its franchise
					if(this.agentFormModel.getRetailerId().toString().equals("-2")){
					RetailerModel retailerModel = new RetailerModel();
					retailerModel.setActive(Boolean.TRUE);
					retailerModel.setAddress1("auto address1");
					retailerModel.setAddress2("auto address2");
					retailerModel.setAreaId(this.agentFormModel.getAreaId());
					retailerModel.setCity(this.agentFormModel.getCity());
					retailerModel.setComments(this.agentFormModel.getComments());
					retailerModel.setContactName("auto contact");
					retailerModel.setCountry("Pakistan");
					retailerModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
					retailerModel.setCreatedOn(new Date());
					retailerModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
					retailerModel.setUpdatedOn(new Date());
					retailerModel.setDescription(this.agentFormModel.getDescription());
					retailerModel.setDistributorId(Long.parseLong(this.agentFormModel.getDistributorId()));
					retailerModel.setName(this.agentFormModel.getUsername()+"_"+"Franchise");
					retailerModel.setPhoneNo(this.agentFormModel.getContactNo());
					retailerModel.setProductCatalogueId(Long.parseLong(this.agentFormModel.getProductCatalogId()));
					retailerModel.setRegionModelId(Long.parseLong(this.agentFormModel.getRegionId()));
//					retailerModel.setRetailerTypeId(1002L);
					retailerModel.setEditMode(Boolean.FALSE);
					//retailerModel.setVersionNo(0);
					this.agentFormModel.setRetailerName(retailerModel.getName());
					BaseWrapper baseWrapperFranchise = new BaseWrapperImpl();
					baseWrapperFranchise.setBasePersistableModel(retailerModel);
					
					baseWrapperFranchise.putObject("permissionGroupId", PermissionGroupConstants.RETAILOR);
					try{
						baseWrapperFranchise = agentHierarchyManager.saveOrUpdateFranchise(baseWrapperFranchise);
					}catch(Exception e){
						JSFContext.addErrorMessage("Frachnise already exists with this name.");
					}
					retailerModel = (RetailerModel) baseWrapperFranchise.getBasePersistableModel();
					this.agentFormModel.setRetailerId(retailerModel.getRetailerId());
					
					}else{
						if(null != retailerModelList){
						for(RetailerModel model: this.retailerModelList)
						{
							if(model.getRetailerId().toString().equals(this.agentFormModel.getRetailerId().toString()))
							{
								this.agentFormModel.setRetailerName(model.getName());
								break;
							}
						}
						}
					}
					
					//by turab, if head agent is selected add its UserGroup
					if(this.agentFormModel.getPartnerGroupId().toString().equals("-2")){
						Set<Long> permission = new HashSet<Long>();
						BaseWrapper baseWrapperPartnerGroup = new BaseWrapperImpl();
						baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
						baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.USER_GROUP_CREATE_USECASE_ID));

						
						PartnerGroupModel partnerGroupModel = new PartnerGroupModel();
						PartnerModel partnerGroupPermissionListViewModel = new PartnerModel();		
						partnerGroupPermissionListViewModel = agentHierarchyManager.findPartnerByRetailerId(this.agentFormModel.getRetailerId());
//						partnerGroupPermissionListViewModel.setPartnerId(3518L); //yousaf temp	
						partnerGroupModel.setName(this.agentFormModel.getUsername()+"_"+"PartnerGroup");
						
						PartnerPermissionViewModel partnerPermissionViewModel = new PartnerPermissionViewModel();
						partnerPermissionViewModel.setPartnerId(partnerGroupModel.getPartnerId());
						partnerPermissionViewModel.setIsDefault(true);//Load Default permissions like home page & change password
						SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
						searchBaseWrapper.setBasePersistableModel(partnerPermissionViewModel);
						searchBaseWrapper = this.partnerGroupFacade.searchDefaultPartnerPermission(searchBaseWrapper);
						Iterator<PartnerPermissionViewModel> itrDP = searchBaseWrapper.getCustomList().getResultsetList().iterator();
						while(itrDP.hasNext())
						{
							PartnerPermissionViewModel ppModel = itrDP.next();
							PartnerGroupDetailModel partnerGroupDetailModel = new PartnerGroupDetailModel();
							partnerGroupDetailModel.setUserPermissionId(ppModel.getUserPermissionId());
							partnerGroupDetailModel.setReadAllowed(ppModel.getReadAvailable());
							partnerGroupDetailModel.setUpdateAllowed(ppModel.getUpdateAvailable());
							partnerGroupDetailModel.setDeleteAllowed(ppModel.getDeleteAvailable());
							partnerGroupDetailModel.setCreateAllowed(ppModel.getCreateAvailable());
							partnerGroupDetailModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
							partnerGroupDetailModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
							partnerGroupDetailModel.setCreatedOn(new Date());
							partnerGroupDetailModel.setUpdatedOn(new Date());
							
							if(permission.add(partnerGroupDetailModel.getUserPermissionId()))
							{
								partnerGroupModel.addPartnerGroupIdPartnerGroupDetailModel(partnerGroupDetailModel);//add default permissions like home page and change password
							}	
						}
						
						//select retailer default permissions and add them- turab Jan 09 2015
						PermissionGroupDetailModel retailerPermissionGroupDetail = new PermissionGroupDetailModel();
						retailerPermissionGroupDetail.setPermissionGroupId(PermissionGroupConstants.RETAILOR);
						ReferenceDataWrapper permissionGrpDtlReferenceDataWrapper = new ReferenceDataWrapperImpl(retailerPermissionGroupDetail, "permissionGroupDetailId", SortingOrder.ASC);
						permissionGrpDtlReferenceDataWrapper.setBasePersistableModel(retailerPermissionGroupDetail);
					    referenceDataManager.getReferenceData(permissionGrpDtlReferenceDataWrapper);
					    if(permissionGrpDtlReferenceDataWrapper.getReferenceDataList() != null && permissionGrpDtlReferenceDataWrapper.getReferenceDataList().size()>0){
					    	List<PermissionGroupDetailModel> list = permissionGrpDtlReferenceDataWrapper.getReferenceDataList();
					    	for(PermissionGroupDetailModel permissionGrpDtl : list){
					    		PartnerGroupDetailModel partnerGroupDetailModel = new PartnerGroupDetailModel();
								partnerGroupDetailModel.setUserPermissionId(permissionGrpDtl.getUserPermissionId());
								/*partnerGroupDetailModel.setReadAllowed(permissionGrpDtl.getUserPermissionIdUserPermissionModel().getReadAvailable());
								partnerGroupDetailModel.setUpdateAllowed(permissionGrpDtl.getUserPermissionIdUserPermissionModel().getUpdateAvailable());
								partnerGroupDetailModel.setDeleteAllowed(permissionGrpDtl.getUserPermissionIdUserPermissionModel().getDeleteAvailable());
								partnerGroupDetailModel.setCreateAllowed(permissionGrpDtl.getUserPermissionIdUserPermissionModel().getCreateAvailable());
								*/
								partnerGroupDetailModel.setReadAllowed(false);
								partnerGroupDetailModel.setUpdateAllowed(false);
								partnerGroupDetailModel.setDeleteAllowed(false);
								partnerGroupDetailModel.setCreateAllowed(false);
								
								partnerGroupDetailModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
								partnerGroupDetailModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
								partnerGroupDetailModel.setCreatedOn(new Date());
								partnerGroupDetailModel.setUpdatedOn(new Date());
								
								if(permission.add(partnerGroupDetailModel.getUserPermissionId()))
								{
									partnerGroupModel.addPartnerGroupIdPartnerGroupDetailModel(partnerGroupDetailModel);//add default permissions like home page and change password
								}
					    	}
					    }
						//end by turab Jan 09 2015
						
						partnerGroupModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
						partnerGroupModel.setCreatedOn(new Date());
						partnerGroupModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
						partnerGroupModel.setUpdatedOn(new Date());
						partnerGroupModel.setActive(Boolean.TRUE);
						partnerGroupModel.setEditable(Boolean.TRUE);
						partnerGroupModel.setPartnerId(partnerGroupPermissionListViewModel.getPartnerId());
						
						baseWrapper.setBasePersistableModel(partnerGroupModel);
						try
						{
							baseWrapper = this.partnerGroupFacade.createPartnerGroup(baseWrapper);
						}catch(FrameworkCheckedException fce)
						{			
							fce.printStackTrace();
					        throw fce;
						}

						this.agentFormModel.setPartnerGroupName(partnerGroupModel.getName());
						partnerGroupModel = (PartnerGroupModel) baseWrapper.getBasePersistableModel();
						this.agentFormModel.setPartnerGroupId(partnerGroupModel.getPartnerGroupId());
					}else{
						if(null!= this.partnerGroupModelList){
						for(PartnerGroupModel model: this.partnerGroupModelList)
						{
							if(model.getPartnerGroupId().toString().equals(this.agentFormModel.getPartnerGroupId().toString()))
							{
								this.agentFormModel.setPartnerGroupName(model.getName());
								break;
							}
						}
						}
					}
					
					
					///////end by turab
					
					for(DistributorLevelModel model: this.agentLevelModelList)
					{
						if(model.getDistributorLevelId().toString().equals(this.agentFormModel.getDistributorLevelId()) && model.getManagingLevelId() == null)
						{
							this.agentFormModel.setHead(Boolean.TRUE);
							break;
						}
					}
					
					this.agentFormModel.setUpdateAccountInfo(this.isFirstNameLastNameChanged());
					
					if(this.agentFormModel.isEditMode()){
						baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
						baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.RETAILER_CONTACT_FORM_UPDATE_USECASE_ID);
					}
					else
					{
						baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
						baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.RETAILER_CONTACT_FORM_USECASE_ID);
					}
					
					
					baseWrapper.putObject("agentFormModel", this.agentFormModel);
					this.agentHierarchyManager.saveOrUpdateAgent(baseWrapper);
					
					//reload the franchise and user group so that newly added can be listed in dropdown
					this.loadRetailers();
					this.loadPartnerGroups();
					
					this.agentFormModel = (RetailerContactListViewFormModel) baseWrapper.getObject("agentFormModel");
					
					this.parentAgentId = this.agentFormModel.getParentRetailerContactId();
					this.agentLevelId = this.agentFormModel.getDistributorLevelId();
					this.franchiseId = this.agentFormModel.getRetailerId();
					this.regionId = this.agentFormModel.getRegionId();
					this.distributorId = this.agentFormModel.getDistributorId();
					this.oldFirstName = this.agentFormModel.getFirstName();
					this.oldLastName = this.agentFormModel.getLastName();
					
					//update account table with ola_customer_account_type_id by turab
					
					
					/*OLAVO olaVo = new OLAVO();	// Commented by Rashid
				
					olaVo.setCnic(this.agentFormModel.getCnicNo());
					olaVo = mfsAccountManager.getAccountInfoFromOLA(this.agentFormModel.getCnicNo(), FinancialInstitutionConstants.OLA_FINANCIAL_INSTITUTION);

					olaVo.setCustomerAccountTypeId(this.agentFormModel.getAccountType());
					AccountModel accountModel = new AccountModel();
					accountModel.setAccountId(olaVo.getAccountId());
					baseWrapper = new BaseWrapperImpl() ;
					baseWrapper.setBasePersistableModel(accountModel) ;
					
					baseWrapper = accountManager.loadAccountByPK(baseWrapper);
					accountModel = (AccountModel) baseWrapper.getBasePersistableModel();
					OlaCustomerAccountTypeModel customerAccountTypeModel = new OlaCustomerAccountTypeModel();
					customerAccountTypeModel.setCustomerAccountTypeId(olaVo.getCustomerAccountTypeId());
					accountModel.setCustomerAccountTypeModel(customerAccountTypeModel);
					accountModel.setUpdatedOn(new Date());
					baseWrapper.setBasePersistableModel(accountModel);
					baseWrapper = accountManager.updateAccount(baseWrapper);*/
					
					if(agentFormModel.isEditMode())
					{
						int versionNo = agentFormModel.getVersionNo();
						versionNo++;
						agentFormModel.setVersionNo(versionNo);
						int appUserVersionNo = agentFormModel.getAppUserVersionNo();
						appUserVersionNo++;
						agentFormModel.setAppUserVersionNo(appUserVersionNo);
						long appUserPartnerGroupVersionNo = agentFormModel.getAppUserPartnerGroupVersionNo();
						appUserPartnerGroupVersionNo++;
						agentFormModel.setAppUserPartnerGroupVersionNo(appUserPartnerGroupVersionNo);
					}
					agentFormModel.setEditMode(Boolean.TRUE);
					JSFContext.setInfoMessage("Agent [ID = " + agentFormModel.getUserDeviceAccountUserId() + "] is saved/updated successfully.");
			    }	
			}
		}
		catch(FrameworkCheckedException fce)
		{
			if(!(fce.getMessage()==null) && fce.getMessage().equals("MobileNumUniqueException"))
			{
				JSFContext.addErrorMessage("Mobile number already exist.");
			}
			if(!(fce.getMessage()==null) && fce.getMessage().contains("already exist"))
			{
				JSFContext.addErrorMessage(fce.getMessage());
			}
			logger.error(fce);
			fce.printStackTrace();
		}
		catch(Exception e)
		{
			logger.error(e);
			e.printStackTrace();
		}
		if(logger.isDebugEnabled()){
			logger.debug("AgentActionBean.saveOrUpdate End");
		}
		return null;
	}
	
	private boolean validate()
	{
		boolean validated = Boolean.TRUE;
		boolean passwordFlag = Boolean.TRUE;
		/*if ( this.agentFormModel.getAccountType() != null && this.agentFormModel.getAccountType().equals(new Long(-1)) ){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Account Type is required."));
			validated = Boolean.FALSE;
		}*/
		
		if((this.agentFormModel.getDistributorId().equals("-1")))
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Agent Network is required."));
			validated = Boolean.FALSE;
		}
		
		if((this.agentFormModel.getRegionId().equals("-1")))
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Region is required."));
			validated = Boolean.FALSE;
		}
		
		if((this.agentFormModel.getRetailerId() == this.defaultValue))
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Franchise/Branch is required."));
			validated = Boolean.FALSE;
		}
		
		if((this.agentFormModel.getDistributorLevelId().equals("-1")))
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Agent Level is required."));
			validated = Boolean.FALSE;
		}
		else if(this.agentLevelIdError)
		{
			JSFContext.addErrorMessage("Multiple Head agents are not allowed under same branch/franchise.");
			validated = Boolean.FALSE;
		}
		else if(this.parentSelectionError)
		{
			JSFContext.addErrorMessage("No parent agent found.");
			validated = Boolean.FALSE;
		}
		
		
		/*if(this.childAgentFoundError)
		{
			JSFContext.addErrorMessage("Parent Agent can not be updated due to child agent found.");
			validated = Boolean.FALSE;
		}*/
		
		
		
		if(this.agentFormModel.getAreaId() == null || this.agentFormModel.getAreaId() <= 0)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Area Name is required."));
			validated = Boolean.FALSE;
		}

		/*if(CommonUtils.isEmpty(this.agentFormModel.getName().trim()))
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Agent Name is required."));
			validated = Boolean.FALSE;
		}*/
		/*if(CommonUtils.isEmpty(this.agentFormModel.getFirstName()))
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("First Name is required."));
			validated = Boolean.FALSE;
		}
		if(CommonUtils.isEmpty(this.agentFormModel.getLastName()))
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Last Name is required."));
			validated = Boolean.FALSE;
		}*/
		if(!this.agentFormModel.isEditMode())
		if(CommonUtils.isEmpty(this.agentFormModel.getUsername()))
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User Name is required."));
			validated = Boolean.FALSE;
		}
		else
		{
			try
			{
				if(!this.agentHierarchyManager.isUserNameUnique(this.agentFormModel.getUsername(), this.agentFormModel.getAppUserId()))
				{
					validated = Boolean.FALSE;
					JSFContext.addErrorMessage("User Name already exist.");
				}
			}
			catch(Exception fce)
			{
				logger.error(fce);
			}
		}
		
		if(this.agentFormModel.getAccountOpeningDate() == null)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Opening date is required."));
			validated = Boolean.FALSE;
		}
		else
		{
			Calendar cal = Calendar.getInstance();
			Date today = new Date();
			cal.setTime(today);
			cal.set(Calendar.HOUR_OF_DAY, 0);  
			cal.set(Calendar.MINUTE, 0);  
			cal.set(Calendar.SECOND, 0);  
			cal.set(Calendar.MILLISECOND, 0);  
			today = cal.getTime(); 
			if(this.agentFormModel.getAccountOpeningDate().after(today))
			{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Opening date cannot be future date."));
				validated = Boolean.FALSE;
			}
		}
		
		/*if(CommonUtils.isEmpty(this.agentFormModel.getZongMsisdn()))	//	This property is binded with Mobile No. label on Add Agent Screen.
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Mobile No is required."));
			validated = Boolean.FALSE;
		}*/
		
		/*if(CommonUtils.isEmpty(this.agentFormModel.getNtnNo()))
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("NTN No is required."));
			validated = Boolean.FALSE;
		}*/
		
		/*if(CommonUtils.isEmpty(this.agentFormModel.getCnicNo()))
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("CNIC No is required."));
			validated = Boolean.FALSE;
		}*/
		
		if(this.agentFormModel.getCnicExpiryDate() == null)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("CNIC Expiry Date is required."));
			validated = Boolean.FALSE;
		}
		else
		{
			Calendar cal = Calendar.getInstance();
			Date today = new Date();
			cal.setTime(today);
			cal.set(Calendar.HOUR_OF_DAY, 0);  
			cal.set(Calendar.MINUTE, 0);  
			cal.set(Calendar.SECOND, 0);  
			cal.set(Calendar.MILLISECOND, 0);  
			today = cal.getTime(); 
			if(this.agentFormModel.getCnicExpiryDate().before(today))
			{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("CNIC Expiry Date cannot be less than today."));
				validated = Boolean.FALSE;
			}
		}
		
		if(CommonUtils.isEmpty(this.agentFormModel.getContactNo()))
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Contact No is required."));
			validated = Boolean.FALSE;
		}
		
		/*if(CommonUtils.isEmpty(this.agentFormModel.getLandLineNo()))
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Landline No is required."));
			validated = Boolean.FALSE;
		}*/
		
		/*if(CommonUtils.isEmpty(this.agentFormModel.getBusinessName()))
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Business Name is required."));
			validated = Boolean.FALSE;
		}*/
		
		if(this.agentFormModel.getNatureOfBusiness() <= 0)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Nature of Business is required."));
			validated = Boolean.FALSE;
		}
		
		/*if(CommonUtils.isEmpty(this.agentFormModel.getShopNo()))
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Shop No is required."));
			validated = Boolean.FALSE;
		}
		
		if(CommonUtils.isEmpty(this.agentFormModel.getMarketPlaza()))
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Market/Plaza is required."));
			validated = Boolean.FALSE;
		}
		
		if(this.agentFormModel.getDistrictTehsilTown() <= 0)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("District/Tehsil/Town is required."));
			validated = Boolean.FALSE;
		}
		
		if(this.agentFormModel.getCityVillage() <= 0 )
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("City/Village is required."));
			validated = Boolean.FALSE;
		}
		
		if(this.agentFormModel.getPostOffice() <= 0 )
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Post Office is required."));
			validated = Boolean.FALSE;
		}
		
		if(CommonUtils.isEmpty(this.agentFormModel.getNtnNumber()))
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Business NTN No is required."));
			validated = Boolean.FALSE;
		}
		
		if(CommonUtils.isEmpty(this.agentFormModel.getNearestLandmark()))
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Nearest Landmark is required."));
			validated = Boolean.FALSE;
		}*/
		
		if(CommonUtils.isEmpty(this.agentFormModel.getPassword()))
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Password is required."));
			validated = Boolean.FALSE;
			passwordFlag = Boolean.FALSE;
		}
		if(CommonUtils.isEmpty(this.agentFormModel.getConfirmPassword()))
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Confirm Password is required."));
			validated = Boolean.FALSE;
			passwordFlag = Boolean.FALSE;
		}
		if(passwordFlag && !this.agentFormModel.getPassword().equals(this.agentFormModel.getConfirmPassword()))
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("The passwords you entered do not match. Please try again."));
			validated = Boolean.FALSE;
		}
		
		if(this.agentFormModel.getPartnerGroupId() == null || this.agentFormModel.getPartnerGroupId() == -1)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User Group is required."));
			validated = Boolean.FALSE;
		}
		
		
		if(this.agentFormModel.getProductCatalogId() == null || Long.valueOf(this.agentFormModel.getProductCatalogId()).longValue() <= 0)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Product Catalog is required."));
			validated = Boolean.FALSE;
		}
		
		/*if(com.inov8.microbank.common.util.CommonUtils.isEmpty(this.agentFormModel.getMobileNo()))		// 		this property is binded with MSISDN label on Add Agent Screen.
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("MSISDN is required."));
			validated = Boolean.FALSE;
		}
		else if(!this.agentFormModel.getMobileNo().startsWith("03"))
		{
			validated = Boolean.FALSE;
			JSFContext.addErrorMessage("MSISDN is invalid. It must start with 03");
		}
		else 
		{
			try
			{
				if(!this.agentHierarchyManager.isMobileNumUnique(this.agentFormModel.getMobileNo(), this.agentFormModel.getAppUserId()))
				{
					validated = Boolean.FALSE;
					JSFContext.addErrorMessage("MSISDN already exist.");
				}
			}
			catch(Exception fce)
			{
				logger.error(fce);
			}
		}*/
		

		if(GenericValidator.isBlankOrNull(this.agentFormModel.getShopNo()))
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Shop No is required."));
			validated = Boolean.FALSE;
		}
		if(GenericValidator.isBlankOrNull(this.agentFormModel.getMarketPlaza()))
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Market/Plaza is required."));
			validated = Boolean.FALSE;
		}
		
		if((this.agentFormModel.getDistrictTehsilTown().longValue()==-1))
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("District/Tehsil/Town is required."));
			validated = Boolean.FALSE;
		}
		
		if((this.agentFormModel.getCityVillage().longValue()==-1))
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("City/Village is required."));
			validated = Boolean.FALSE;
		}

		if(GenericValidator.isBlankOrNull(this.agentFormModel.getNearestLandmark()))
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Nearest Landmark is required."));
			validated = Boolean.FALSE;
		}
		
		if(this.agentFormModel.getDistributorLevelId().equals("-1"))
		{
			this.agentFormModel.setDistributorLevelId(null);
		}
		
		if(this.agentFormModel.getProductCatalogId().equals("-1"))
		{
			this.agentFormModel.setProductCatalogId(null);
		}
		
		if(this.agentFormModel.getParentRetailerContactId() == null || this.agentFormModel.getParentRetailerContactId().equals("-1"))
		{
			this.agentFormModel.setParentRetailerContactId(null);
		}
		
		if(this.agentFormModel.getNatureOfBusiness() == defaultValue)
		{
			this.agentFormModel.setNatureOfBusiness(null);
		}
		
		if(this.agentFormModel.getDistrictTehsilTown() == defaultValue)
		{
			this.agentFormModel.setDistrictTehsilTown(null);
		}

		if(this.agentFormModel.getCityVillage() == defaultValue)
		{
			this.agentFormModel.setCityVillage(null);
		}
		
		if(this.agentFormModel.getPostOffice() == defaultValue)
		{
			this.agentFormModel.setPostOffice(null);
		}
		
		return validated;
	}
	
	private Node findParentAreaNode(Node root, AreaModel areaModel)
	{
		Node parentAreaNode = null;
		if(root.getId().equals(areaModel.getParentAreaId()))
		{
			parentAreaNode = root;
		}
		else
		{
			Iterator<Node> areaNodesIterator = root.childrenIterator();
	        for(;areaNodesIterator.hasNext();)
	        {
	        	parentAreaNode = (Node)areaNodesIterator.next();
	            if(parentAreaNode.getId().equals(areaModel.getParentAreaId()))
	            {
	                break;
	            }
	            else
	            {
	            	parentAreaNode = findParentAreaNode(parentAreaNode, areaModel);
	            	if(parentAreaNode != null)
	            	{
	            		break;
	            	}
	            }
	        }
		}
		return parentAreaNode;
	}
	
	
	public void loadAreaNameHierarchy(AjaxBehaviorEvent event)
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentActionBean.loadAreaNameHierarchy Start");
		}
		try 
		{
			if(this.agentFormModel.getRegionId() == null || this.agentFormModel.getRegionId().equals("-1"))
			{
				JSFContext.addErrorMessage("Please select region.");
				this.areaHierarchyError = Boolean.TRUE;
			}
			else if(this.childAgentFoundError)
			{
				JSFContext.addErrorMessage("Parent Agent can not be updated due to child agent found.");
				this.areaHierarchyError = Boolean.TRUE;
			}
			else
			{
				this.areaHierarchyError = Boolean.FALSE;
				this.areaNodes.clear();
				List<AreaModel> areaModelList = null;
				SearchBaseWrapper searchBaseWrapper = this.agentHierarchyManager.findAreaNamesByRegionId(Long.valueOf(this.agentFormModel.getRegionId()));
				
				if(searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0)
				{
					areaModelList = searchBaseWrapper.getCustomList().getResultsetList();
					
					for(AreaModel areaModel:areaModelList)
					{
						if(areaModel.isActive())
						{
							if(areaModel.getUltimateParentAreaId() == null)
							{
								Node areaNode = new Node();
								areaNode.setName(areaModel.getName());
								areaNode.setId(areaModel.getAreaId());
						        this.areaNodes.add(areaNode);						        
							}
						}
					}
					
					
					List<AreaModel> childAreaModelList = new ArrayList<AreaModel>();
					for(TreeNode treeNode : this.areaNodes)
					{
						childAreaModelList.clear();
						Node ultimateNode = (Node)treeNode;
						for(AreaModel model:areaModelList)
						{
							if(ultimateNode.getId().equals(model.getUltimateParentAreaId()))
							{
								childAreaModelList.add(model);
							}
						}
						
						while(childAreaModelList.size() > 0)
						{
							List<AreaModel> tempAreaModelList = new ArrayList<AreaModel>();
							for(AreaModel childAreaModel:childAreaModelList)
							{
								Node perentNode = findParentAreaNode(ultimateNode, childAreaModel);
								if(perentNode != null)
								{
									Node areaNode = new Node();
									areaNode.setName(childAreaModel.getName());
									areaNode.setId(childAreaModel.getAreaId());
									perentNode.addChild(areaNode);
									tempAreaModelList.add(childAreaModel);
								}
							}
							if(tempAreaModelList.size() > 0 )
							{
								childAreaModelList.removeAll(tempAreaModelList);	
							}
							else if(childAreaModelList.size() > 0)
							{
								logger.debug("There are " + childAreaModelList.size() + " Area Name(s) not loaded in the hierarchy due to some issue");
								logger.debug("AreaId of one of them is: " + childAreaModelList.get(0).getAreaId());
								break;
							}
						}
					}
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
			logger.debug("AgentActionBean.loadAreaNameHierarchy End");
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
			Node areaNode = (Node)this.selectedNode;
			this.agentFormModel.setAreaId(areaNode.getId());
			this.agentFormModel.setAreaName(areaNode.getName());
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

	public void processSaleUserSelection(TreeSelectionChangeEvent event)
    {
        try
        {
        	List<Object> selection = new ArrayList<Object>(event.getNewSelection());
            Object currentSelectionKey = selection.get(0);
            UITree tree = (UITree) event.getSource();
     
            Object storedKey = tree.getRowKey();
            tree.setRowKey(currentSelectionKey);
            this.saleHierarchySelectedNode = (TreeNode) tree.getRowData();
            tree.setRowKey(storedKey);
        }
        catch(Exception e)
        {
			logger.error(e);
        }
    }
	
	
	public void onChangeRegion(AjaxBehaviorEvent event)
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentActionBean.onChangeRegion Start");
		}
		try
		{
			boolean proceed = Boolean.TRUE;
			if(this.agentFormModel.isEditMode())
			{
				List<RetailerContactModel> retailerContactModels = this.agentHierarchyManager.findChildRetailerContactsById(this.agentFormModel.getRetailerContactId());
				if(retailerContactModels != null && retailerContactModels.size() > 0)
				{
					proceed = Boolean.FALSE;
					JSFContext.addErrorMessage("Parent Agent can not be updated due to child agent found.");
					this.childAgentFoundError = Boolean.TRUE;
					this.agentFormModel.setRegionId(this.regionId);
				}
			}
			if(proceed)
			{
				this.partnerGroups.clear();
				this.retailers.clear();
				this.loadRetailers();
				this.agentLevels.clear();
				this.loadAgentLevels();
				this.parentAgents.clear();
				this.agentFormModel.setAreaId(null);
				this.agentFormModel.setAreaName("");
				this.selectedNode = null;
				
				for(RegionModel model: this.regionModelList)
				{
					if(model.getRegionId().equals(Long.valueOf(this.agentFormModel.getRegionId())))
					{
						this.agentFormModel.setActive(model.getActive());
						this.agentFormModel.setActiveDisabled(model.getActive() ? Boolean.FALSE : Boolean.TRUE);
						break;
					}
				}		
			}
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
			logger.debug("AgentActionBean.onChangeRegion End");
		}
	}
	
	public void onChangeDistributor(AjaxBehaviorEvent event)
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentActionBean.onChangeDistributor Start");
		}
		try
		{
			boolean proceed = Boolean.TRUE;
			if(this.agentFormModel.isEditMode())
			{
				List<RetailerContactModel> retailerContactModels = this.agentHierarchyManager.findChildRetailerContactsById(this.agentFormModel.getRetailerContactId());
				if(retailerContactModels != null && retailerContactModels.size() > 0)
				{
					proceed = Boolean.FALSE;
					JSFContext.addErrorMessage("Parent Agent can not be updated due to child agent found.");
					this.childAgentFoundError = Boolean.TRUE;
					this.agentFormModel.setDistributorId(this.distributorId);
				}
			}
			if(proceed)
			{
				this.regions.clear();
				this.retailers.clear();
				this.agentLevels.clear();
				this.parentAgents.clear();
				this.partnerGroups.clear();
				this.agentFormModel.setAreaId(null);
				this.agentFormModel.setAreaName("");
				this.selectedNode = null;
				this.loadRegions();
				
				for(DistributorModel model: this.distributorModelList)
				{
					if(model.getDistributorId().equals(Long.valueOf(this.agentFormModel.getDistributorId())))
					{
						this.agentFormModel.setActive(model.getActive());
						this.agentFormModel.setActiveDisabled(model.getActive() ? Boolean.FALSE : Boolean.TRUE);
						break;
					}
				}
			}
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
			logger.debug("AgentActionBean.onChangeDistributor End");
		}
	}
	
	public void onChangeParentAgent(AjaxBehaviorEvent event)
	{	
		if(logger.isDebugEnabled())
		{
			logger.debug("AgentActionBean.onChangeParentAgent Start");
		}
		try
		{
			boolean proceed = Boolean.TRUE;
			if(this.agentFormModel.isEditMode())
			{
				List<RetailerContactModel> retailerContactModels = this.agentHierarchyManager.findChildRetailerContactsById(this.agentFormModel.getRetailerContactId());
				if(retailerContactModels != null && retailerContactModels.size() > 0)
				{
					proceed = Boolean.FALSE;
					JSFContext.addErrorMessage("Parent Agent can not be updated due to child agent found.");
					this.childAgentFoundError = Boolean.TRUE;
					this.agentFormModel.setParentRetailerContactId(this.parentAgentId);
				}
			}
			
			if(proceed)
			{
				this.agentFormModel.setAreaId(null);
				this.agentFormModel.setAreaName("");
				this.selectedNode = null;
				for(RetailerContactModel model: this.parentAgentModelList)
				{
					if(model.getRetailerContactId().equals(Long.valueOf(this.agentFormModel.getParentRetailerContactId())))
					{
						this.agentFormModel.setActive(model.getActive());
						this.agentFormModel.setActiveDisabled(model.getActive() ? Boolean.FALSE : Boolean.TRUE);
						break;
					}
				}
			}
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
		if(logger.isDebugEnabled())
		{
			logger.debug("AgentActionBean.onChangeParentAgent End");
		}
	}
	
	public void loadParentAgents()
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentActionBean.loadParentAgents Starts");
		}
		try
		{
			parentAgents.clear();
			Long parentAgentLevel = null;
			for(DistributorLevelModel distributorLevelModel: this.agentLevelModelList)
			{
				if(distributorLevelModel.getDistributorLevelId().equals(Long.valueOf(this.agentFormModel.getDistributorLevelId())))
				{
					parentAgentLevel = distributorLevelModel.getManagingLevelId();
					break;
				}
			}
			
			if(parentAgentLevel != null)
			{
				SearchBaseWrapper searchBaseWrapper = agentHierarchyManager.findParentAgents(parentAgentLevel, this.agentFormModel.getRetailerId());
				if(searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0)
				{
					parentAgentModelList = searchBaseWrapper.getCustomList().getResultsetList();
					for(RetailerContactModel model : parentAgentModelList)
					{
						if(!model.getRetailerContactId().equals(this.agentFormModel.getRetailerContactId()))
						{
							SelectItem item = new SelectItem();
							item.setLabel(model.getName());
							item.setValue(model.getRetailerContactId());
							this.parentAgents.add(item);
						}
					}
					
					if(this.parentAgents.size() == 0)
					{
						JSFContext.addErrorMessage("No parent agent found.");
						this.parentSelectionError = Boolean.TRUE;
					}
				}
				else
				{
					JSFContext.addErrorMessage("No parent agent found.");
					this.parentSelectionError = Boolean.TRUE;
				}
			}
			else if(!this.agentFormModel.isEditMode() || this.agentFormModel.isEditMode() && !this.agentFormModel.getHead())
			{
				RetailerContactModel retailerContactModel = this.agentHierarchyManager.findHeadAgent(Long.valueOf(this.agentFormModel.getDistributorLevelId()), this.agentFormModel.getRetailerId());
				if(retailerContactModel != null)
				{
					JSFContext.addErrorMessage("Multiple Head agents are not allowed under same branch/franchise.");
					agentLevelIdError = Boolean.TRUE;
				}
			}
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
			logger.debug("AgentActionBean.loadParentAgents End");
		}
	}
	
	
	public void onChangeAgentLevel(AjaxBehaviorEvent event)
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentActionBean.onChangeAgentLevel Start");
		}
		try
		{
			boolean proceed = Boolean.TRUE;
			if(this.agentFormModel.isEditMode())
			{
				List<RetailerContactModel> retailerContactModels = this.agentHierarchyManager.findChildRetailerContactsById(this.agentFormModel.getRetailerContactId());
				if(retailerContactModels != null && retailerContactModels.size() > 0)
				{	
					proceed = Boolean.FALSE;
					JSFContext.addErrorMessage("Parent Agent can not be updated due to child agent found.");
					this.childAgentFoundError = Boolean.TRUE;
					this.agentFormModel.setDistributorLevelId(this.agentLevelId);
				}
			}
			
			if(proceed)
			{
				this.parentAgents.clear();
				this.agentFormModel.setAreaId(null);
				this.agentFormModel.setAreaName("");
				this.selectedNode = null;
				this.agentLevelIdError = Boolean.FALSE;
				this.parentSelectionError = Boolean.FALSE;
				//////////////////////turab start
				SelectItem autoRetailer = new SelectItem();
                autoRetailer.setLabel("AUTO_GENERATED");
                autoRetailer.setValue(-2L);
               	if(this.isHeadAgentLevel(Long.parseLong(this.agentFormModel.getDistributorLevelId())) && !this.isExists(this.retailers)){
               		this.retailers.add(autoRetailer);
               		this.agentFormModel.setRetailerId(-2L);
               	}
                
                SelectItem userGroup = new SelectItem();
                userGroup.setLabel("AUTO_GENERATED");
                userGroup.setValue(-2L);
                if(this.isHeadAgentLevel(Long.parseLong(this.agentFormModel.getDistributorLevelId())) && !this.isExists(this.partnerGroups)){
                	this.partnerGroups.add(userGroup);
                	this.agentFormModel.setPartnerGroupId(-2L);
                }
				/////////////////////end by turab
				loadParentAgents();

				Long distributorLevelId = Long.valueOf( this.agentFormModel.getDistributorLevelId() );
				if( !this.agentFormModel.isEditMode() )
                {
				    if( this.agentFormModel.getDistributorLevelId().equals("-1") || isHeadAgentLevel( distributorLevelId ) )
	                {
	                    this.agentFormModel.setRso( Boolean.FALSE );
	                    this.agentFormModel.setRsoDisabled( Boolean.TRUE );
	                }
				    else
				    {
				        this.agentFormModel.setRsoDisabled( Boolean.FALSE );
				    }
                }
				
				for(DistributorLevelModel model: this.agentLevelModelList)
				{
					if(model.getDistributorLevelId().equals(Long.valueOf(this.agentFormModel.getDistributorLevelId())))
					{
						this.agentFormModel.setActive(model.getActive());
						this.agentFormModel.setActiveDisabled(model.getActive() ? Boolean.FALSE : Boolean.TRUE);
						break;
					}
				}	
			}
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
			logger.debug("AgentActionBean.onChangeAgentLevel End");
		}
	}
	
	/**
	 * @author AtifHu
	 * @param event
	 */
	public void onChangeCity(AjaxBehaviorEvent event)
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentActionBean.onChangeCity Start");
		}
		try
		{
			this.postOffices=new ArrayList<>();
			
			PostalOfficeModel postalOfficeModel = new PostalOfficeModel();
			CityModel cityModel=new CityModel();
			cityModel.setCityId(this.agentFormModel.getCityVillage());
			postalOfficeModel.setCityModel(cityModel);
		    
			SearchBaseWrapper searchBaseWrapper=new SearchBaseWrapperImpl();
			searchBaseWrapper.setBasePersistableModel(postalOfficeModel);
			
			searchBaseWrapper	=	addressManager.getPostalOfficesByCity(searchBaseWrapper);
		    if (searchBaseWrapper.getCustomList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0)
		    {
		    	this.postOfficesModelList = searchBaseWrapper.getCustomList().getResultsetList();
				for(PostalOfficeModel model : postOfficesModelList)
				{
					SelectItem item = new SelectItem();
					item.setLabel(model.getName());
					item.setValue(model.getPostalOfficeId());
					this.postOffices.add(item);
				}
		    }
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(e);
		}
		if(logger.isDebugEnabled()){
			logger.debug("AgentActionBean.onChangeCity End");
		}
	}
	
	private void loadAgentLevels()
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentActionBean.loadAgentLevels Starts");
		}
		try
		{
			SearchBaseWrapper searchBaseWrapper = agentHierarchyManager.findAgentLevelsByRegionId(Long.valueOf(this.agentFormModel.getRegionId()));
			if(searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0)
			{
				this.agentLevels.clear();
				agentLevelModelList = searchBaseWrapper.getCustomList().getResultsetList();
				for(DistributorLevelModel model : agentLevelModelList)
				{
					if(model.getActive())
					{
						SelectItem item = new SelectItem();
						item.setLabel(model.getName());
						item.setValue(model.getDistributorLevelId());
						this.agentLevels.add(item);
					}
				}
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
			logger.debug("AgentActionBean.loadAgentLevels End");
		}
	}

	private boolean isHeadAgentLevel(long distributorLevelId)
    {
	    boolean result = false;
	    DistributorLevelModel selectedDistributorLevelModel = null;
        for( DistributorLevelModel distributorLevelModel : agentLevelModelList )
        {
            if( distributorLevelId == distributorLevelModel.getDistributorLevelId().intValue() )
            {
                selectedDistributorLevelModel = distributorLevelModel;
                break;
            }
        }

        if( selectedDistributorLevelModel !=null && selectedDistributorLevelModel.getManagingLevelId() == null )
        {
            result = true;
        }
        return result;
    }

	public void onChangeFranchise(AjaxBehaviorEvent event)
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentActionBean.onChangeFranchise Start");
		}
		
		try
		{
			boolean proceed = Boolean.TRUE;
			if(this.agentFormModel.isEditMode())
			{
				List<RetailerContactModel> retailerContactModels = this.agentHierarchyManager.findChildRetailerContactsById(this.agentFormModel.getRetailerContactId());
				if(retailerContactModels != null && retailerContactModels.size() > 0)
				{
					proceed = Boolean.FALSE;
					JSFContext.addErrorMessage("Parent Agent can not be updated due to child agent found.");
					this.childAgentFoundError = Boolean.TRUE;
					this.agentFormModel.setRetailerId(this.franchiseId);
				}
			}
		
			if(proceed)
			{
				this.partnerGroups.clear();
				this.parentAgents.clear();
				this.agentFormModel.setDistributorLevelId("-1");
				this.agentFormModel.setAreaId(null);
				this.agentFormModel.setAreaName("");
				this.selectedNode = null;
				loadPartnerGroups();
				for(RetailerModel model: this.retailerModelList)
				{
					if(model.getRetailerId().equals(this.agentFormModel.getRetailerId()))
					{
						this.agentFormModel.setActive(model.getActive());
						this.agentFormModel.setActiveDisabled(model.getActive() ? Boolean.FALSE : Boolean.TRUE);
						break;
					}
				}
			}
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
			logger.debug("AgentActionBean.onChangeFranchise Start");
		}
	}
	
	private void loadPartnerGroups()
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentActionBean.loadPartnerGroups Starts");
		}
		try
		{
			SearchBaseWrapper searchBaseWrapper = agentHierarchyManager.findPartnerGroupsByRetailer(this.agentFormModel.getRetailerId());
			if(searchBaseWrapper.getCustomList() != null && searchBaseWrapper.getCustomList().getResultsetList() != null)
			{
				this.partnerGroups.clear();
				this.partnerGroupModelList = searchBaseWrapper.getCustomList().getResultsetList();
				for(PartnerGroupModel model : partnerGroupModelList)
				{
					if(model.getActive())
					{
						SelectItem item = new SelectItem();
						item.setLabel(model.getName());
						item.setValue(model.getPartnerGroupId());
						this.partnerGroups.add(item);	
					}
				}
			}
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
			logger.debug("AgentActionBean.loadPartnerGroups End");
		}
	}
	
	
	
	public void loadRetailers()
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentActionBean.loadRetailers Starts");
		}
		try
		{
			SearchBaseWrapper searchBaseWrapper = agentHierarchyManager.findRetailersByRegionId(Long.valueOf(this.agentFormModel.getRegionId()));
			this.retailers.clear();
			if(searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0)
			{
				retailerModelList = searchBaseWrapper.getCustomList().getResultsetList();
				for(RetailerModel model : retailerModelList)
				{
					if(model.getActive())
					{
						SelectItem item = new SelectItem();
						item.setLabel(model.getName());
						item.setValue(model.getRetailerId());
						this.retailers.add(item);	
					}
				}
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
			logger.debug("AgentActionBean.loadRetailers End");
		}
	}
	
	public void loadRegions()
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentActionBean.loadRegions Starts");
		}
		try
		{
			SearchBaseWrapper searchBaseWrapper = agentHierarchyManager.findRegionsByDistributorId(Long.valueOf(this.agentFormModel.getDistributorId()));
			if(searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0)
			{
				regionModelList = searchBaseWrapper.getCustomList().getResultsetList();
				this.regions.clear();
				for(RegionModel regionModel : regionModelList)
				{
					if(regionModel.getActive())
					{
						SelectItem item = new SelectItem();
						item.setLabel(regionModel.getRegionName());
						item.setValue(regionModel.getRegionId());
						this.regions.add(item);
					}
				}
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
			logger.debug("AgentActionBean.loadRegions End");
		}
	}

	public boolean isLinkPaymentModeAllowed()
    {
	    return AuthenticationUtil.checkRightsIfAny( PortalConstants.LINK_PAY_MOD_CREATE+","+PortalConstants.PG_GP_CREATE );
    }

	private boolean isFirstNameLastNameChanged(){
		boolean result = false;
		
		if( (this.oldFirstName != null && !this.oldFirstName.equals(this.agentFormModel.getFirstName().trim())) 
				|| (this.oldLastName != null && !this.oldLastName.equals(this.agentFormModel.getLastName().trim()))){
			result = true;
		}
		return result;
	}
	
	public void setAgentHierarchyManager(AgentHierarchyManager agentHierarchyManager) {
		this.agentHierarchyManager = agentHierarchyManager;
	}

	public List<SelectItem> getDistributors() {
		return distributors;
	}

	public void setDistributors(List<SelectItem> distributors) {
		this.distributors = distributors;
	}

	public List<SelectItem> getRegions() {
		return regions;
	}

	public void setRegions(List<SelectItem> regions) {
		this.regions = regions;
	}

	public List<SelectItem> getRetailers() {
		return retailers;
	}

	public void setRetailers(List<SelectItem> retailers) {
		this.retailers = retailers;
	}

	public List<SelectItem> getAgentLevels() {
		return agentLevels;
	}

	public void setAgentLevels(List<SelectItem> agentLevels) {
		this.agentLevels = agentLevels;
	}

	public List<SelectItem> getProductCatalogs() {
		return productCatalogs;
	}

	public void setProductCatalogs(List<SelectItem> productCatalogs) {
		this.productCatalogs = productCatalogs;
	}

	public RetailerContactListViewFormModel getAgentFormModel() {
		return agentFormModel;
	}

	public void setAgentFormModel(RetailerContactListViewFormModel agentFormModel) {
		this.agentFormModel = agentFormModel;
	}

	public List<SelectItem> getPartnerGroups() {
		return partnerGroups;
	}

	public void setPartnerGroups(List<SelectItem> partnerGroups) {
		this.partnerGroups = partnerGroups;
	}

	public Long getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Long defaultValue) {
		this.defaultValue = defaultValue;
	}

	public List<SelectItem> getNatureOfBusinesses() {
		return natureOfBusinesses;
	}

	public void setNatureOfBusinesses(List<SelectItem> natureOfBusinesses) {
		this.natureOfBusinesses = natureOfBusinesses;
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public List<SelectItem> getDistricts() {
		return districts;
	}

	public void setDistricts(List<SelectItem> districts) {
		this.districts = districts;
	}

	public List<SelectItem> getCities() {
		return cities;
	}

	public void setCities(List<SelectItem> cities) {
		this.cities = cities;
	}

	public List<SelectItem> getPostOffices() {
		return postOffices;
	}

	public void setPostOffices(List<SelectItem> postOffices) {
		this.postOffices = postOffices;
	}

	public List<SelectItem> getParentAgents() {
		return parentAgents;
	}

	public void setParentAgents(List<SelectItem> parentAgents) {
		this.parentAgents = parentAgents;
	}

	public List<TreeNode> getAreaNodes() {
		return areaNodes;
	}

	public void setAreaNodes(List<TreeNode> areaNodes) {
		this.areaNodes = areaNodes;
	}

	public void setSmartMoneyAccountManager(
			SmartMoneyAccountManager smartMoneyAccountManager) {
		this.smartMoneyAccountManager = smartMoneyAccountManager;
	}

	public void setActionAuthorizationFacade(
			ActionAuthorizationFacade actionAuthorizationFacade) {
		this.actionAuthorizationFacade = actionAuthorizationFacade;
	}

	public void setActionAuthorizationHistoryManager(
			ActionAuthorizationHistoryManager actionAuthorizationHistoryManager) {
		this.actionAuthorizationHistoryManager = actionAuthorizationHistoryManager;
	}

	public void setUsecaseFacade(UsecaseFacade usecaseFacade) {
		this.usecaseFacade = usecaseFacade;
	}

	public void setJmsProducer(JmsProducer jmsProducer) {
		this.jmsProducer = jmsProducer;
	}

	public boolean isAreaHierarchyError() {
		return areaHierarchyError;
	}

	public void setAreaHierarchyError(boolean areaHierarchyError) {
		this.areaHierarchyError = areaHierarchyError;
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

	public String getOldFirstName() {
		return oldFirstName;
	}

	public void setOldFirstName(String oldFirstName) {
		this.oldFirstName = oldFirstName;
	}

	public String getOldLastName() {
		return oldLastName;
	}

	public void setOldLastName(String oldLastName) {
		this.oldLastName = oldLastName;
	}

	public List<SelectItem> getAccountTypes() {
		return accountTypes;
	}

	public void setAccountTypes(List<SelectItem> accountTypes) {
		this.accountTypes = accountTypes;
	}

	//method added by Turab; taken from MfsAccountController
	private void removeSpecialAccountTypes(CopyOnWriteArrayList<OlaCustomerAccountTypeModel> olaCustomerAccountTypeModelList){

    	//Iterator<OlaCustomerAccountTypeModel> it = olaCustomerAccountTypeModelList.iterator();
    	//So far only one special account type exists which is SETTLEMENT (id = 3L) 
    	for (OlaCustomerAccountTypeModel model : olaCustomerAccountTypeModelList) {
    		if(model.getCustomerAccountTypeId().longValue() == CustomerAccountTypeConstants.SETTLEMENT
    				|| model.getCustomerAccountTypeId().longValue() == UserTypeConstantsInterface.WALKIN_CUSTOMER){
    			olaCustomerAccountTypeModelList.remove(model);
    		}
    	}
    }
	
	////// Functions for Action Authorization
	protected Long createAuthorizationRequest(Long nextAuthorizationLevel,String comments,String refDataModelString,Long usecaseId,String referenceId,boolean resubmitRequest,Long actionAuthorizationId,HttpServletRequest request) throws FrameworkCheckedException{
		BaseWrapper baseWrapper= new BaseWrapperImpl();
		List<ActionAuthorizationModel> existingRequest;
		
		ActionAuthorizationModel actionAuthorizationModel = new ActionAuthorizationModel();
		if(null!=actionAuthorizationId){
			actionAuthorizationModel.setActionAuthorizationId(actionAuthorizationId);
		}	
		actionAuthorizationModel.setCreatedOn(new Date());
		actionAuthorizationModel.setEscalationLevel(nextAuthorizationLevel);
		actionAuthorizationModel.setActionStatusId(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL);
		actionAuthorizationModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		actionAuthorizationModel.setUsecaseId(usecaseId);
		actionAuthorizationModel.setComments(comments);
		actionAuthorizationModel.setReferenceData(refDataModelString);
		actionAuthorizationModel.setReferenceId(referenceId);
		baseWrapper.setBasePersistableModel(actionAuthorizationModel);
		
		existingRequest = actionAuthorizationFacade.checkExistingRequest(actionAuthorizationModel).getResultsetList();
		
		if(!existingRequest.isEmpty() && !resubmitRequest)
			throw new FrameworkCheckedException("Action authorization request with Action ID : "+ existingRequest.get(0).getActionAuthorizationId().toString()+" already exist.");

		baseWrapper = actionAuthorizationFacade.saveOrUpdate(baseWrapper);
		
		actionAuthorizationModel = (ActionAuthorizationModel) baseWrapper.getBasePersistableModel();
		
		for(long i =1;i<nextAuthorizationLevel;i++){	
			ActionAuthorizationHistoryModel actionAuthorizationHistoryModel= new ActionAuthorizationHistoryModel();
			actionAuthorizationHistoryModel.setActionAthorizationIdActionAuthorizationModel(actionAuthorizationModel);
			actionAuthorizationHistoryModel.setActionStatusId(ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED_INTIMATED);
			actionAuthorizationHistoryModel.setIntimatedOn(new Date());				
			actionAuthorizationHistoryModel.setEscalationLevel(i);
			
			// Setting Checker Names
			UsecaseLevelModel usecaseLevelModel= usecaseFacade.findUsecaseLevel(usecaseId,i);
			List<LevelCheckerModel> checkerList = usecaseFacade.getLevelCheckerModelList(usecaseLevelModel.getUsecaseLevelId());
			StringBuilder usernames = new StringBuilder();
			StringBuilder emailRecipients = new StringBuilder();
			for (LevelCheckerModel levelCheckerModel : checkerList) {
				usernames.append(levelCheckerModel.getRelationCheckerIdAppUserModel().getUsername()+",");
				if( (null!=(levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail())) && (!levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail().isEmpty())){
					if(GenericValidator.isEmail(levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail()))
						emailRecipients.append(levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail()+";");
				}
				
			}
			usernames.deleteCharAt(usernames.lastIndexOf(","));
			actionAuthorizationHistoryModel.setIntimatedTo(usernames.toString());
								
			baseWrapper.setBasePersistableModel(actionAuthorizationHistoryModel);
			baseWrapper = actionAuthorizationHistoryManager.saveOrUpdate(baseWrapper);
			
			// Sending Email Notification to all checkers
			
			sendNotificationEmail(emailRecipients.toString(),actionAuthorizationModel,request);
		}
		
		///Send Email to Current Level Checkers
		
		if(nextAuthorizationLevel>0){
		
			UsecaseLevelModel usecaseLevelModel= usecaseFacade.findUsecaseLevel(usecaseId,nextAuthorizationLevel);
			List<LevelCheckerModel> checkerList = usecaseFacade.getLevelCheckerModelList(usecaseLevelModel.getUsecaseLevelId());
			StringBuilder emailRecipients = new StringBuilder();
			for (LevelCheckerModel levelCheckerModel : checkerList) {
				if( (null!=(levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail())) && (!levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail().isEmpty())){
					if(GenericValidator.isEmail(levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail()))
						emailRecipients.append(levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail()+";");
				}		
			}
			sendNotificationEmail(emailRecipients.toString(),actionAuthorizationModel,request);
		}
		///End Send Notification Email to Current Level Checkers
		
		return actionAuthorizationModel.getActionAuthorizationId();
	}
	
	protected Long performActionWithAllIntimationLevels(Long nextAuthorizationLevel,String comments,String refDataModelString,UsecaseModel usecaseModel,Long actionAuthorizationId, HttpServletRequest request) throws FrameworkCheckedException{
		BaseWrapper baseWrapper= new BaseWrapperImpl();		
		UsecaseLevelModel usecaseLevelModel= usecaseFacade.findUsecaseLevel(usecaseModel.getUsecaseId(),usecaseModel.getEscalationLevels());
		List<LevelCheckerModel> checkerList = usecaseFacade.getLevelCheckerModelList(usecaseLevelModel.getUsecaseLevelId());
		StringBuilder checkers = new StringBuilder();

		for (LevelCheckerModel levelCheckerModel : checkerList) {
			checkers.append(levelCheckerModel.getRelationCheckerIdAppUserModel().getUsername());
			checkers.append(",");
		}
		checkers.deleteCharAt(checkers.lastIndexOf(","));
		
		ActionAuthorizationModel actionAuthorizationModel = new ActionAuthorizationModel();
		if(null!=actionAuthorizationId)
			actionAuthorizationModel.setActionAuthorizationId(actionAuthorizationId);
		actionAuthorizationModel.setEscalationLevel(usecaseModel.getEscalationLevels());
		actionAuthorizationModel.setActionStatusId(ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED_INTIMATED);
		actionAuthorizationModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		actionAuthorizationModel.setCreatedOn(new Date());
		actionAuthorizationModel.setUsecaseId(usecaseModel.getUsecaseId());
		actionAuthorizationModel.setComments(comments);
		actionAuthorizationModel.setReferenceData(refDataModelString);
		actionAuthorizationModel.setIntimatedOn(new Date());
		actionAuthorizationModel.setIntimatedTo(checkers.toString());
		baseWrapper.setBasePersistableModel(actionAuthorizationModel);
		baseWrapper = actionAuthorizationFacade.saveOrUpdate(baseWrapper);
		actionAuthorizationModel = (ActionAuthorizationModel) baseWrapper.getBasePersistableModel();
							
		for(long i =1;i<=usecaseModel.getEscalationLevels();i++){
			ActionAuthorizationHistoryModel actionAuthorizationHistoryModel= new ActionAuthorizationHistoryModel();
			actionAuthorizationHistoryModel.setActionAthorizationIdActionAuthorizationModel(actionAuthorizationModel);
			actionAuthorizationHistoryModel.setActionStatusId(ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED_INTIMATED);
			actionAuthorizationHistoryModel.setIntimatedOn(new Date());	
			actionAuthorizationHistoryModel.setEscalationLevel(i);
			
			// Setting Checker Names/emails
			usecaseLevelModel= usecaseFacade.findUsecaseLevel(usecaseModel.getUsecaseId(),i);
			checkerList = usecaseFacade.getLevelCheckerModelList(usecaseLevelModel.getUsecaseLevelId());
			StringBuilder usernames = new StringBuilder();
			StringBuilder emailRecipients = new StringBuilder();
			for (LevelCheckerModel levelCheckerModel : checkerList) {
				usernames.append(levelCheckerModel.getRelationCheckerIdAppUserModel().getUsername()+",");
				if( (null!=(levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail())) && (!levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail().isEmpty())){	
					if(GenericValidator.isEmail(levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail()))
						emailRecipients.append(levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail()+";");
				}
				
			}
			usernames.deleteCharAt(usernames.lastIndexOf(","));
			actionAuthorizationHistoryModel.setIntimatedTo(usernames.toString());
								
			baseWrapper.setBasePersistableModel(actionAuthorizationHistoryModel);
			baseWrapper = actionAuthorizationHistoryManager.saveOrUpdate(baseWrapper);
			
			// Sending Email Notification to all checkers
			
			sendNotificationEmail(emailRecipients.toString(),actionAuthorizationModel,request);
		}
		return actionAuthorizationModel.getActionAuthorizationId();
	}
	protected void sendNotificationEmail(String recipients,ActionAuthorizationModel model,HttpServletRequest request) throws FrameworkCheckedException{
		EmailMessage emailMessage = new EmailMessage();
		StringBuilder notificationtext= new StringBuilder();
		String detailUrl=null;
		
		emailMessage.setRecepients(recipients.split(";"));
		emailMessage.setSubject(MessageUtil.getMessage("actionAuthorization.emailSubject",new Object[]{model.getActionAuthorizationId().toString()}));
		
		if(model.getActionStatusId() == ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL) 
			notificationtext.append(MessageUtil.getMessage("actionAuthorization.emailText.actionPending",new Object[]{model.getEscalationLevel().toString()}));
			
		else if(model.getActionStatusId() == ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED || 
				model.getActionStatusId() == ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED_INTIMATED)
			notificationtext.append(MessageUtil.getMessage("actionAuthorization.emailText.actionApproved"));
		else if(model.getActionStatusId() == ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVAL_DENIED)
			notificationtext.append(MessageUtil.getMessage("actionAuthorization.emailText.actionDenied",new Object[]{model.getEscalationLevel().toString()}));
		else if(model.getActionStatusId() == ActionAuthorizationConstantsInterface.ACTION_STATUS_RESOLVED)
			notificationtext.append(MessageUtil.getMessage("actionAuthorization.emailText.actionResolved"));
		else if(model.getActionStatusId().longValue() == ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK.longValue())
			notificationtext.append(MessageUtil.getMessage("actionAuthorization.emailText.actionAssignedBack"));
		
		detailUrl = AuthoirzationDetailEnum.getUrlByUsecaseId(model.getUsecaseId());
		
		notificationtext.append(MessageUtil.getMessage("actionAuthorization.emailText.url",new Object[] {detailUrl})+model.getActionAuthorizationId().toString());
		emailMessage.setText(notificationtext.toString());
		
		try{
			jmsProducer.produce(emailMessage, DestinationConstants.EMAIL_DESTINATION);
		}catch(EmailServiceSendFailureException esx){
			throw new FrameworkCheckedException(esx.getMessage());
		}
	}
	
	private boolean isExists(List<SelectItem> list){
		boolean result = false;
		
		for(SelectItem item : list){
			if (item.getValue().toString().equals("-2")){
				result = true;
				break;
			}
		}
		
		return result;
	}

	public void setMfsAccountManager(MfsAccountManager mfsAccountManager) {
		this.mfsAccountManager = mfsAccountManager;
	}

	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}

	public boolean isSaleUserHierarchyError() {
		return saleUserHierarchyError;
	}

	public void setSaleUserHierarchyError(boolean saleUserHierarchyError) {
		this.saleUserHierarchyError = saleUserHierarchyError;
	}

	public TreeNode getSaleHierarchySelectedNode() {
		return saleHierarchySelectedNode;
	}

	public void setSaleHierarchySelectedNode(TreeNode saleHierarchySelectedNode) {
		this.saleHierarchySelectedNode = saleHierarchySelectedNode;
	}

	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}

	public List<TreeNode> getSaleUserNodes() {
		return saleUserNodes;
	}

	public void setSaleUserNodes(List<TreeNode> saleUserNodes) {
		this.saleUserNodes = saleUserNodes;
	}

	public void setAddressManager(AddressManager addressManager) {
		this.addressManager = addressManager;
	}

	public void setPartnerGroupFacade(PartnerGroupFacade partnerGroupFacade) {
		this.partnerGroupFacade = partnerGroupFacade;
	}
}
