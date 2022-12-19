package com.inov8.microbank.webapp.action.agenthierarchy;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.RetailerModel;
import com.inov8.microbank.common.model.agenthierarchy.RegionModel;
import com.inov8.microbank.common.model.retailermodule.RetailerContactListViewFormModel;
import com.inov8.microbank.common.model.retailermodule.RetailerContactSearchViewModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.server.service.agenthierarchy.AgentHierarchyManager;
import com.inov8.ola.server.facade.AccountFacade;
import com.inov8.ola.server.service.account.AccountManager;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.dao.DataIntegrityViolationException;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ManagedBean(name="agentSearchBean")
@SessionScoped
public class SearchAgentActionBean {
	private final static Log logger=LogFactory.getLog(SearchAgentActionBean.class);
	@ManagedProperty(value="#{agentHierarchyManager}")
	private AgentHierarchyManager agentHierarchyManager;
	
	@ManagedProperty(value="#{accountManager}")
	private AccountManager accountManager;
	
	@ManagedProperty(value="#{accountFacade}")
	private AccountFacade accountFacade;
	
	private RetailerContactSearchViewModel agentFormModel;
	
	private List<RegionModel> regionList;
	private List<DistributorModel> distributorList;
	private List<RetailerModel> retailerList;
	private List<OlaCustomerAccountTypeModel> olaCustomerAccountList;
	private List<RetailerContactSearchViewModel> contactModelsList;
		
	private List<SelectItem> distributors= new ArrayList<SelectItem>();
	private List<SelectItem> olaAccountTypes= new ArrayList<SelectItem>();
	private List<SelectItem> regions;
	private List<SelectItem> retailers;
	private Boolean securityCheck= Boolean.FALSE;
	private Boolean editSecurityCheck=Boolean.FALSE;
	private int listSize;
	
	private int UNSORTED = 0;
	private int ASCENDING_ORDER = 1;
	private int DESCENDING_ORDER = 2;
	
	private int agentNetworkNameSortingOrder = UNSORTED;
	private int regionNameSortingOrder = UNSORTED;
	private int franchiseNameSortingOrder = UNSORTED;
	private int mobileNoSortingOrder = UNSORTED;
	private int statusSortingOrder = UNSORTED;
	private int agentIDSortingOrder = UNSORTED;
	private int agentNameSortingOrder = UNSORTED;
	private int userNameSortingOrder = UNSORTED;
	private int firstNameSortingOrder = UNSORTED;
	private int lastNameSortingOrder = UNSORTED;
	private int accountNickSortingOrder = UNSORTED;
	private int areaNameSortingOrder = UNSORTED;
	private int openCloseSortingOrder = UNSORTED;
	private int accountTypeSortingOrder = UNSORTED;
	private int saleUserNameSortingOrder = UNSORTED;
	private int roleTitleSortingOrder = UNSORTED;
	
	private int sortingPointer = 0;
	
	@PostConstruct
	public void init(){
		if(logger.isDebugEnabled()){
			logger.debug("SearchSearchAgentActionBean.init Start");
		}
		try {
			if(null != JSFContext.getFromRequest("infoMessage")){
				JSFContext.setInfoMessage(JSFContext.getFromRequest("infoMessage").toString());
			}
			agentFormModel= new RetailerContactSearchViewModel();
			SearchBaseWrapper searchBaseWrapper=agentHierarchyManager.findAllDistributors();
			distributorList= searchBaseWrapper.getCustomList().getResultsetList();
			for(DistributorModel model:distributorList){
				this.distributors.add(new SelectItem(model.getDistributorId(),model.getName()));
			}

	    	List<OlaCustomerAccountTypeModel> customerAccountTypeList = accountFacade.searchParentOlaCustomerAccountTypes(
	    			CustomerAccountTypeConstants.SETTLEMENT,CustomerAccountTypeConstants.WALK_IN_CUSTOMER);
			
            for(OlaCustomerAccountTypeModel model:customerAccountTypeList){
				this.olaAccountTypes.add(new SelectItem(model.getCustomerAccountTypeId(),model.getName()));
			}
			
			String createButton=PortalConstants.ADMIN_GP_CREATE+","+PortalConstants.PG_GP_CREATE+","+
                    PortalConstants.MNG_AGNTS_CREATE;
            securityCheck=AuthenticationUtil.checkRightsIfAny(createButton);
            
            String updateButton=PortalConstants.ADMIN_GP_UPDATE+","+PortalConstants.PG_GP_UPDATE+","+
                    PortalConstants.MNG_AGNTS_UPDATE;
			editSecurityCheck=AuthenticationUtil.checkRightsIfAny(updateButton);
		} catch (FrameworkCheckedException e) {
			// TODO Auto-generated catch block
			logger.error(e);
		}
		if(logger.isDebugEnabled()){
			logger.debug("SearchAgentActionBean.init End");
		}
	}
	
	public void sortBySaleUserName(ActionEvent e)
	{
		if(this.saleUserNameSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.contactModelsList, new GenericComparator(Boolean.FALSE));
			this.saleUserNameSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.saleUserNameSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.contactModelsList, new GenericComparator("index", Boolean.TRUE));
			this.saleUserNameSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.contactModelsList, new GenericComparator("saleUserName", Boolean.TRUE));
			this.saleUserNameSortingOrder = this.ASCENDING_ORDER;
		}
		this.mobileNoSortingOrder = UNSORTED;
		this.statusSortingOrder = UNSORTED;
		this.regionNameSortingOrder = this.UNSORTED;
		this.franchiseNameSortingOrder = this.UNSORTED;
		this.agentIDSortingOrder = UNSORTED;
		this.agentNameSortingOrder = UNSORTED;
		this.userNameSortingOrder = UNSORTED;
		this.firstNameSortingOrder = UNSORTED;
		this.lastNameSortingOrder = UNSORTED;
		this.accountNickSortingOrder = UNSORTED;
		this.areaNameSortingOrder = UNSORTED;
		this.openCloseSortingOrder=UNSORTED;
		this.accountTypeSortingOrder=UNSORTED;
		this.agentNetworkNameSortingOrder=UNSORTED;
		this.roleTitleSortingOrder=UNSORTED;
	}
	
	public void sortByroleTitle(ActionEvent e)
	{
		if(this.roleTitleSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.contactModelsList, new GenericComparator(Boolean.FALSE));
			this.roleTitleSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.roleTitleSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.contactModelsList, new GenericComparator("index", Boolean.TRUE));
			this.roleTitleSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.contactModelsList, new GenericComparator("roleTitle", Boolean.TRUE));
			this.roleTitleSortingOrder = this.ASCENDING_ORDER;
		}
		this.mobileNoSortingOrder = UNSORTED;
		this.statusSortingOrder = UNSORTED;
		this.regionNameSortingOrder = this.UNSORTED;
		this.franchiseNameSortingOrder = this.UNSORTED;
		this.agentIDSortingOrder = UNSORTED;
		this.agentNameSortingOrder = UNSORTED;
		this.userNameSortingOrder = UNSORTED;
		this.firstNameSortingOrder = UNSORTED;
		this.lastNameSortingOrder = UNSORTED;
		this.accountNickSortingOrder = UNSORTED;
		this.areaNameSortingOrder = UNSORTED;
		this.openCloseSortingOrder=UNSORTED;
		this.accountTypeSortingOrder=UNSORTED;
		this.agentNetworkNameSortingOrder=UNSORTED;
		this.saleUserNameSortingOrder=UNSORTED;
	}
	
	public void sortByAgentNetworkName(ActionEvent e)
	{
		if(this.agentNetworkNameSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.contactModelsList, new GenericComparator(Boolean.FALSE));
			this.agentNetworkNameSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.agentNetworkNameSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.contactModelsList, new GenericComparator("index", Boolean.TRUE));
			this.agentNetworkNameSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.contactModelsList, new GenericComparator("distributorName", Boolean.TRUE));
			this.agentNetworkNameSortingOrder = this.ASCENDING_ORDER;
		}
		this.mobileNoSortingOrder = UNSORTED;
		this.statusSortingOrder = UNSORTED;
		this.regionNameSortingOrder = this.UNSORTED;
		this.franchiseNameSortingOrder = this.UNSORTED;
		this.agentIDSortingOrder = UNSORTED;
		this.agentNameSortingOrder = UNSORTED;
		this.userNameSortingOrder = UNSORTED;
		this.firstNameSortingOrder = UNSORTED;
		this.lastNameSortingOrder = UNSORTED;
		this.accountNickSortingOrder = UNSORTED;
		this.areaNameSortingOrder = UNSORTED;
		this.openCloseSortingOrder=UNSORTED;
		this.accountTypeSortingOrder=UNSORTED;
		this.saleUserNameSortingOrder=UNSORTED;
		this.roleTitleSortingOrder=UNSORTED;
	}
	
	public void sortByAgentID(ActionEvent e)
	{
		if(this.agentIDSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.contactModelsList, new GenericComparator(Boolean.FALSE));
			this.agentIDSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.agentIDSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.contactModelsList, new GenericComparator("index", Boolean.TRUE));
			this.agentIDSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.contactModelsList, new GenericComparator("agentId", Boolean.TRUE));
			this.agentIDSortingOrder = this.ASCENDING_ORDER;
		}
		this.agentNetworkNameSortingOrder = this.UNSORTED;
		this.mobileNoSortingOrder = UNSORTED;
		this.statusSortingOrder = UNSORTED;
		this.regionNameSortingOrder = this.UNSORTED;
		this.franchiseNameSortingOrder = this.UNSORTED;
		this.agentNameSortingOrder = UNSORTED;
		this.userNameSortingOrder = UNSORTED;
		this.firstNameSortingOrder = UNSORTED;
		this.lastNameSortingOrder = UNSORTED;
		this.accountNickSortingOrder = UNSORTED;
		this.areaNameSortingOrder = UNSORTED;
		this.openCloseSortingOrder=UNSORTED;
		this.accountTypeSortingOrder=UNSORTED;
		this.saleUserNameSortingOrder=UNSORTED;
		this.roleTitleSortingOrder=UNSORTED;
	}
	
	public void sortByOpenCloseStatus(ActionEvent e)
	{
		if(this.openCloseSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.contactModelsList, new GenericComparator(Boolean.FALSE));
			this.openCloseSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.openCloseSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.contactModelsList, new GenericComparator("index", Boolean.TRUE));
			this.openCloseSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.contactModelsList, new GenericComparator("accountClosed", Boolean.TRUE));
			this.openCloseSortingOrder = this.ASCENDING_ORDER;
		}
		this.agentNetworkNameSortingOrder = this.UNSORTED;
		this.statusSortingOrder = UNSORTED;
		this.regionNameSortingOrder = this.UNSORTED;
		this.franchiseNameSortingOrder = this.UNSORTED;
		this.agentIDSortingOrder = UNSORTED;
		this.agentNameSortingOrder = UNSORTED;
		this.userNameSortingOrder = UNSORTED;
		this.firstNameSortingOrder = UNSORTED;
		this.lastNameSortingOrder = UNSORTED;
		this.accountNickSortingOrder = UNSORTED;
		this.areaNameSortingOrder = UNSORTED;
		this.mobileNoSortingOrder = UNSORTED;
		this.accountTypeSortingOrder=UNSORTED;
		this.saleUserNameSortingOrder=UNSORTED;
		this.roleTitleSortingOrder=UNSORTED;
	}
	
	public void sortByMobileNo(ActionEvent e)
	{
		if(this.mobileNoSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.contactModelsList, new GenericComparator(Boolean.FALSE));
			this.mobileNoSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.mobileNoSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.contactModelsList, new GenericComparator("index", Boolean.TRUE));
			this.mobileNoSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.contactModelsList, new GenericComparator("contactNo", Boolean.TRUE));
			this.mobileNoSortingOrder = this.ASCENDING_ORDER;
		}
		this.agentNetworkNameSortingOrder = this.UNSORTED;
		this.statusSortingOrder = UNSORTED;
		this.regionNameSortingOrder = this.UNSORTED;
		this.franchiseNameSortingOrder = this.UNSORTED;
		this.agentIDSortingOrder = UNSORTED;
		this.agentNameSortingOrder = UNSORTED;
		this.userNameSortingOrder = UNSORTED;
		this.firstNameSortingOrder = UNSORTED;
		this.lastNameSortingOrder = UNSORTED;
		this.accountNickSortingOrder = UNSORTED;
		this.areaNameSortingOrder = UNSORTED;
		this.openCloseSortingOrder=UNSORTED;
		this.accountTypeSortingOrder=UNSORTED;
		this.saleUserNameSortingOrder=UNSORTED;
		this.roleTitleSortingOrder=UNSORTED;
	}
	
	public void sortByAgentName(ActionEvent e)
	{
		if(this.agentNameSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.contactModelsList, new GenericComparator(Boolean.FALSE));
			this.agentNameSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.agentNameSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.contactModelsList, new GenericComparator("index", Boolean.TRUE));
			this.agentNameSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.contactModelsList, new GenericComparator("agentName", Boolean.TRUE));
			this.agentNameSortingOrder = this.ASCENDING_ORDER;
		}
		this.agentNetworkNameSortingOrder = this.UNSORTED;
		this.statusSortingOrder = UNSORTED;
		this.regionNameSortingOrder = this.UNSORTED;
		this.franchiseNameSortingOrder = this.UNSORTED;
		this.agentIDSortingOrder = UNSORTED;
		this.userNameSortingOrder = UNSORTED;
		this.firstNameSortingOrder = UNSORTED;
		this.lastNameSortingOrder = UNSORTED;
		this.accountNickSortingOrder = UNSORTED;
		this.areaNameSortingOrder = UNSORTED;
		this.mobileNoSortingOrder = this.UNSORTED;
		this.openCloseSortingOrder=UNSORTED;
		this.accountTypeSortingOrder=UNSORTED;
		this.saleUserNameSortingOrder=UNSORTED;
		this.roleTitleSortingOrder=UNSORTED;
	}
	
	public void sortByUserName(ActionEvent e)
	{
		if(this.userNameSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.contactModelsList, new GenericComparator(Boolean.FALSE));
			this.userNameSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.userNameSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.contactModelsList, new GenericComparator("index", Boolean.TRUE));
			this.userNameSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.contactModelsList, new GenericComparator("userName", Boolean.TRUE));
			this.userNameSortingOrder = this.ASCENDING_ORDER;
		}
		this.agentNetworkNameSortingOrder = this.UNSORTED;
		this.statusSortingOrder = UNSORTED;
		this.regionNameSortingOrder = this.UNSORTED;
		this.franchiseNameSortingOrder = this.UNSORTED;
		this.agentIDSortingOrder = UNSORTED;
		this.agentNameSortingOrder = UNSORTED;
		this.firstNameSortingOrder = UNSORTED;
		this.lastNameSortingOrder = UNSORTED;
		this.accountNickSortingOrder = UNSORTED;
		this.areaNameSortingOrder = UNSORTED;
		this.mobileNoSortingOrder = this.UNSORTED;
		this.openCloseSortingOrder=UNSORTED;
		this.accountTypeSortingOrder=UNSORTED;
		this.saleUserNameSortingOrder=UNSORTED;
		this.roleTitleSortingOrder=UNSORTED;
	}
	
	public void sortByAccountNick(ActionEvent e)
	{
		if(this.accountNickSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.contactModelsList, new GenericComparator(Boolean.FALSE));
			this.accountNickSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.accountNickSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.contactModelsList, new GenericComparator("index", Boolean.TRUE));
			this.accountNickSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.contactModelsList, new GenericComparator("accountNick", Boolean.TRUE));
			this.accountNickSortingOrder = this.ASCENDING_ORDER;
		}
		this.agentNetworkNameSortingOrder = this.UNSORTED;
		this.statusSortingOrder = UNSORTED;
		this.regionNameSortingOrder = this.UNSORTED;
		this.franchiseNameSortingOrder = this.UNSORTED;
		this.agentIDSortingOrder = UNSORTED;
		this.userNameSortingOrder = UNSORTED;
		this.agentNameSortingOrder = UNSORTED;
		this.lastNameSortingOrder = UNSORTED;
		this.firstNameSortingOrder = UNSORTED;
		this.areaNameSortingOrder = UNSORTED;
		this.mobileNoSortingOrder = this.UNSORTED;
		this.openCloseSortingOrder=UNSORTED;
		this.accountTypeSortingOrder=UNSORTED;
		this.saleUserNameSortingOrder=UNSORTED;
		this.roleTitleSortingOrder=UNSORTED;
	}
	
	public void sortByAreaName(ActionEvent e)
	{
		if(this.areaNameSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.contactModelsList, new GenericComparator(Boolean.FALSE));
			this.areaNameSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.areaNameSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.contactModelsList, new GenericComparator("index", Boolean.TRUE));
			this.areaNameSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.contactModelsList, new GenericComparator("areaName", Boolean.TRUE));
			this.areaNameSortingOrder = this.ASCENDING_ORDER;
		}
		this.agentNetworkNameSortingOrder = this.UNSORTED;
		this.statusSortingOrder = UNSORTED;
		this.regionNameSortingOrder = this.UNSORTED;
		this.franchiseNameSortingOrder = this.UNSORTED;
		this.agentIDSortingOrder = UNSORTED;
		this.userNameSortingOrder = UNSORTED;
		this.agentNameSortingOrder = UNSORTED;
		this.lastNameSortingOrder = UNSORTED;
		this.firstNameSortingOrder = UNSORTED;
		this.accountNickSortingOrder = UNSORTED;
		this.mobileNoSortingOrder = this.UNSORTED;
		this.openCloseSortingOrder=UNSORTED;
		this.accountTypeSortingOrder=UNSORTED;
		this.saleUserNameSortingOrder=UNSORTED;
		this.roleTitleSortingOrder=UNSORTED;
	}
	
	public void sortByFirstName(ActionEvent e)
	{
		if(this.firstNameSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.contactModelsList, new GenericComparator(Boolean.FALSE));
			this.firstNameSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.firstNameSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.contactModelsList, new GenericComparator("index", Boolean.TRUE));
			this.firstNameSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.contactModelsList, new GenericComparator("firstName", Boolean.TRUE));
			this.firstNameSortingOrder = this.ASCENDING_ORDER;
		}
		this.agentNetworkNameSortingOrder = this.UNSORTED;
		this.statusSortingOrder = UNSORTED;
		this.regionNameSortingOrder = this.UNSORTED;
		this.franchiseNameSortingOrder = this.UNSORTED;
		this.agentIDSortingOrder = UNSORTED;
		this.userNameSortingOrder = UNSORTED;
		this.agentNameSortingOrder = UNSORTED;
		this.lastNameSortingOrder = UNSORTED;
		this.accountNickSortingOrder = UNSORTED;
		this.areaNameSortingOrder = UNSORTED;
		this.mobileNoSortingOrder = this.UNSORTED;
		this.openCloseSortingOrder=UNSORTED;
		this.accountTypeSortingOrder=UNSORTED;
		this.saleUserNameSortingOrder=UNSORTED;
		this.roleTitleSortingOrder=UNSORTED;
	}
	
	public void sortByLastName(ActionEvent e)
	{
		if(this.lastNameSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.contactModelsList, new GenericComparator(Boolean.FALSE));
			this.lastNameSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.lastNameSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.contactModelsList, new GenericComparator("index", Boolean.TRUE));
			this.lastNameSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.contactModelsList, new GenericComparator("lastName", Boolean.TRUE));
			this.lastNameSortingOrder = this.ASCENDING_ORDER;
		}
		this.agentNetworkNameSortingOrder = this.UNSORTED;
		this.statusSortingOrder = UNSORTED;
		this.regionNameSortingOrder = this.UNSORTED;
		this.franchiseNameSortingOrder = this.UNSORTED;
		this.agentIDSortingOrder = UNSORTED;
		this.userNameSortingOrder = UNSORTED;
		this.agentNameSortingOrder = UNSORTED;
		this.firstNameSortingOrder = UNSORTED;
		this.accountNickSortingOrder = UNSORTED;
		this.areaNameSortingOrder = UNSORTED;
		this.mobileNoSortingOrder = this.UNSORTED;
		this.openCloseSortingOrder=UNSORTED;
		this.accountTypeSortingOrder=UNSORTED;
		this.saleUserNameSortingOrder=UNSORTED;
		this.roleTitleSortingOrder=UNSORTED;
	}
	
	public void sortByRegionName(ActionEvent e)
	{
		if(this.regionNameSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.contactModelsList, new GenericComparator(Boolean.FALSE));
			this.regionNameSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.regionNameSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.contactModelsList, new GenericComparator("index", Boolean.TRUE));
			this.regionNameSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.contactModelsList, new GenericComparator("regionName", Boolean.TRUE));
			this.regionNameSortingOrder = this.ASCENDING_ORDER;
		}
		this.agentNetworkNameSortingOrder = this.UNSORTED;
		this.statusSortingOrder = this.UNSORTED;
		this.agentNameSortingOrder = this.UNSORTED;
		this.franchiseNameSortingOrder = this.UNSORTED;
		this.agentIDSortingOrder = UNSORTED;
		this.userNameSortingOrder = UNSORTED;
		this.firstNameSortingOrder = UNSORTED;
		this.lastNameSortingOrder = UNSORTED;
		this.accountNickSortingOrder = UNSORTED;
		this.areaNameSortingOrder = UNSORTED;
		this.mobileNoSortingOrder = this.UNSORTED;
		this.openCloseSortingOrder=UNSORTED;
		this.accountTypeSortingOrder=UNSORTED;
		this.saleUserNameSortingOrder=UNSORTED;
		this.roleTitleSortingOrder=UNSORTED;
	}
	
	public void sortByFranchiseName(ActionEvent e)
	{
		if(this.franchiseNameSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.contactModelsList, new GenericComparator(Boolean.FALSE));
			this.franchiseNameSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.franchiseNameSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.contactModelsList, new GenericComparator("index", Boolean.TRUE));
			this.franchiseNameSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.contactModelsList, new GenericComparator("retailerName", Boolean.TRUE));
			this.franchiseNameSortingOrder = this.ASCENDING_ORDER;
		}
		this.agentNetworkNameSortingOrder = this.UNSORTED;
		this.statusSortingOrder = this.UNSORTED;
		this.agentNameSortingOrder = this.UNSORTED;
		this.regionNameSortingOrder = this.UNSORTED;
		this.agentIDSortingOrder = UNSORTED;
		this.userNameSortingOrder = UNSORTED;
		this.firstNameSortingOrder = UNSORTED;
		this.lastNameSortingOrder = UNSORTED;
		this.accountNickSortingOrder = UNSORTED;
		this.areaNameSortingOrder = UNSORTED;
		this.mobileNoSortingOrder = this.UNSORTED;
		this.openCloseSortingOrder=UNSORTED;
		this.accountTypeSortingOrder=UNSORTED;
		this.saleUserNameSortingOrder=UNSORTED;
		this.roleTitleSortingOrder=UNSORTED;
	}
	
	public void sortByStatus(ActionEvent e)
	{
		if(this.statusSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.contactModelsList, new GenericComparator(Boolean.TRUE));
			this.statusSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.statusSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.contactModelsList, new GenericComparator("index", Boolean.TRUE));
			this.statusSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.contactModelsList, new GenericComparator("active", Boolean.FALSE));
			this.statusSortingOrder = this.ASCENDING_ORDER;
		}
		this.agentNetworkNameSortingOrder = this.UNSORTED;
		this.agentNameSortingOrder = this.UNSORTED;
		this.franchiseNameSortingOrder = this.UNSORTED;
		this.agentIDSortingOrder = UNSORTED;
		this.regionNameSortingOrder = this.UNSORTED;
		this.userNameSortingOrder = UNSORTED;
		this.firstNameSortingOrder = UNSORTED;
		this.lastNameSortingOrder = UNSORTED;
		this.accountNickSortingOrder = UNSORTED;
		this.areaNameSortingOrder = UNSORTED;
		this.mobileNoSortingOrder = this.UNSORTED;
		this.openCloseSortingOrder=UNSORTED;
		this.accountTypeSortingOrder=UNSORTED;
		this.saleUserNameSortingOrder=UNSORTED;
		this.roleTitleSortingOrder=UNSORTED;
	}

	public void sortByAccountType(ActionEvent e)
	{
		if(this.accountTypeSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.contactModelsList, new GenericComparator(Boolean.FALSE));
			this.accountTypeSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.accountTypeSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.contactModelsList, new GenericComparator("index", Boolean.TRUE));
			this.accountTypeSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.contactModelsList, new GenericComparator("accountTypeName", Boolean.TRUE));
			this.accountTypeSortingOrder = this.ASCENDING_ORDER;
		}
		this.agentNetworkNameSortingOrder = this.UNSORTED;
		this.statusSortingOrder = UNSORTED;
		this.regionNameSortingOrder = this.UNSORTED;
		this.franchiseNameSortingOrder = this.UNSORTED;
		this.agentIDSortingOrder = UNSORTED;
		this.userNameSortingOrder = UNSORTED;
		this.agentNameSortingOrder = UNSORTED;
		this.lastNameSortingOrder = UNSORTED;
		this.firstNameSortingOrder = UNSORTED;
		this.accountNickSortingOrder = UNSORTED;
		this.mobileNoSortingOrder = this.UNSORTED;
		this.openCloseSortingOrder=UNSORTED;
		this.saleUserNameSortingOrder=UNSORTED;
		this.roleTitleSortingOrder=UNSORTED;
	}
	
	
	public void clearAllSessionObjects(){
		if (!FacesContext.getCurrentInstance().isPostback()) {
			SessionBeanObjects.removeAllSessionObjects();
		}
	}

	private HSSFCellStyle getCellStyle(HSSFWorkbook wb)
	{
		HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setColor(HSSFColor.BLACK.index);
        cellStyle.setWrapText(false);
        cellStyle.setFont(font);
        return cellStyle; 
	}
	
	public void exportToExcel()
	{
		try
		{
			HSSFWorkbook wb = new HSSFWorkbook();
	        HSSFSheet sheet = wb.createSheet("Exported Agent WorkBook");
	        HSSFCellStyle cellStyle = getCellStyle(wb); 
	        
	        HSSFRow row = sheet.createRow(0);
	        

	        HSSFCell cell = row.createCell(Short.valueOf("0"));
	        cell.setCellValue("Agent ID");
	        cell.setCellStyle(cellStyle);

	        cell = row.createCell(Short.valueOf("1"));
	        cell.setCellValue("Agent Network");
	        cell.setCellStyle(cellStyle);
	        
	        cell = row.createCell(Short.valueOf("2"));
	        cell.setCellValue("Region");
	        cell.setCellStyle(cellStyle);
	        
	        cell = row.createCell(Short.valueOf("3"));
	        cell.setCellValue("Area Name");
	        cell.setCellStyle(cellStyle);

			cell = row.createCell(Short.valueOf("4"));
			cell.setCellValue("Area Level");
			cell.setCellStyle(cellStyle);

			cell = row.createCell(Short.valueOf("5"));
	        cell.setCellValue("Role Title");
	        cell.setCellStyle(cellStyle);

	        cell = row.createCell(Short.valueOf("6"));
	        cell.setCellValue("Sales User Name");
	        cell.setCellStyle(cellStyle);

	        cell = row.createCell(Short.valueOf("7"));
	        cell.setCellValue("CNIC#");
	        cell.setCellStyle(cellStyle);

	        cell = row.createCell(Short.valueOf("8"));
	        cell.setCellValue("Mobile#");
	        cell.setCellStyle(cellStyle);
	        
	        cell = row.createCell(Short.valueOf("9"));
	        cell.setCellValue("Agent Name");
	        cell.setCellStyle(cellStyle);
	        
			cell = row.createCell(Short.valueOf("10"));
			cell.setCellValue("Agent Level");
			cell.setCellStyle(cellStyle);

	        cell = row.createCell(Short.valueOf("11"));
	        cell.setCellValue("User Name");
	        cell.setCellStyle(cellStyle);
	        
	        cell = row.createCell(Short.valueOf("12"));
	        cell.setCellValue("Account Nick");
	        cell.setCellStyle(cellStyle);
	        
	        cell = row.createCell(Short.valueOf("13"));
	        cell.setCellValue("Status");
	        cell.setCellStyle(cellStyle);
	        
	        cell = row.createCell(Short.valueOf("14"));
	        cell.setCellValue("Open/Close");
	        cell.setCellStyle(cellStyle);
	        
	        cell = row.createCell(Short.valueOf("15"));
	        cell.setCellValue("Account Type");
	        cell.setCellStyle(cellStyle);
	        
	        /*cell = row.createCell(Short.valueOf("3"));
	        cell.setCellValue("First Name");
	        cell.setCellStyle(cellStyle);
	        
	        cell = row.createCell(Short.valueOf("4"));
	        cell.setCellValue("Last Name");
	        cell.setCellStyle(cellStyle);*/

	        int rowCounter = 1;
	        for(RetailerContactSearchViewModel model: this.contactModelsList)
	        {
	        	row = sheet.createRow(rowCounter);
	        	rowCounter++;
	        	
	        	cell = row.createCell(Short.valueOf("0"));
		        cell.setCellValue(model.getAgentId());
		        
		        cell = row.createCell(Short.valueOf("1"));
		        cell.setCellValue(model.getDistributorName());
		        
		        cell = row.createCell(Short.valueOf("2"));
		        cell.setCellValue(model.getRegionName());
		        
		        /*cell = row.createCell(Short.valueOf("3"));
		        cell.setCellValue(model.getFirstName());
		        
		        cell = row.createCell(Short.valueOf("4"));
		        cell.setCellValue(model.getLastName());*/
		        
		        cell = row.createCell(Short.valueOf("3"));
		        cell.setCellValue(model.getAreaName());

				cell = row.createCell(Short.valueOf("4"));
				cell.setCellValue(model.getAreaLevelName());

				cell = row.createCell(Short.valueOf("5"));
		        cell.setCellValue(model.getRoleTitle());
		        
		        cell = row.createCell(Short.valueOf("6"));
		        cell.setCellValue(model.getSaleUserName());
		        
		        cell = row.createCell(Short.valueOf("7"));
		        cell.setCellValue(model.getCnic());
		        
		        cell = row.createCell(Short.valueOf("8"));
		        cell.setCellValue(model.getContactNo());
		        
		        cell = row.createCell(Short.valueOf("9"));
		        cell.setCellValue(model.getAgentName());
		        
		        cell = row.createCell(Short.valueOf("10"));
		        cell.setCellValue(model.getAgentLevelName());
		        
		        cell = row.createCell(Short.valueOf("11"));
		        cell.setCellValue(model.getUserName());	
		        
		        cell = row.createCell(Short.valueOf("12"));
		        cell.setCellValue(model.getAccountNick());
		        
		        cell = row.createCell(Short.valueOf("13"));
		        if(model.getActive())
		        {
		        	cell.setCellValue("Active");
		        }	
		        else
		        {
		        	cell.setCellValue("In-Actvie");
		        }
		        cell = row.createCell(Short.valueOf("14"));
		        cell.setCellValue(model.getAccountClosed());
		        
		        cell = row.createCell(Short.valueOf("15"));
		        cell.setCellValue(model.getOlaCustomerAccountTypeName());
	        }
	        
	        FacesContext context = FacesContext.getCurrentInstance();
	        HttpServletResponse res = (HttpServletResponse) context.getExternalContext().getResponse();
	        res.setContentType("application/vnd.ms-excel");
	        res.setHeader("Content-disposition", "attachment;filename=agents data.xls");

	        ServletOutputStream out = res.getOutputStream();
	        wb.write(out);
	        out.flush();
	        out.close();
	        FacesContext.getCurrentInstance().responseComplete();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void onChangeDistributor(AjaxBehaviorEvent event){
		try {
			
			regions= new ArrayList<SelectItem>();
			retailers= new ArrayList<SelectItem>();
			
			if(this.agentFormModel.getDistributorId()!=null)
			{
				SearchBaseWrapper searchBaseWrapper=agentHierarchyManager.findRegionsByDistributorId(this.agentFormModel.getDistributorId());
				
				if(searchBaseWrapper.getCustomList()!=null)
				{
					regionList=searchBaseWrapper.getCustomList().getResultsetList();
					
					for(RegionModel model:regionList){
						this.regions.add(new SelectItem(model.getRegionId(),model.getRegionName()));
					}
				}
				
			}
		} catch (FrameworkCheckedException e) {
			// TODO Auto-generated catch block
			logger.error(e);
		}
		catch (Exception e) {
			logger.error(e);
		}
		if(logger.isDebugEnabled()){
			logger.debug("SearchAgentActionBean.onChangeDistributor End");
		}
	}
	public void onChangeRegion(AjaxBehaviorEvent event){
		if(logger.isDebugEnabled()){
			logger.debug("SearchAgentActionBean.onChangeRegion Start");
		}
		try {
			retailers= new ArrayList<SelectItem>();
			
			if(this.agentFormModel.getRegionId()!=null )
			{
				SearchBaseWrapper searchBaseWrapper=agentHierarchyManager.findRetailersByRegionId(this.agentFormModel.getRegionId());
				
				if(searchBaseWrapper.getCustomList()!=null)
				{
					retailerList=searchBaseWrapper.getCustomList().getResultsetList();
					for(RetailerModel model:retailerList){
						this.retailers.add(new SelectItem(model.getRetailerId(),model.getName()));
					}
				}
			}
		} catch (FrameworkCheckedException e) {
			logger.error(e);
		}
		catch (Exception e) {
			logger.error(e);
		}
		if(logger.isDebugEnabled()){
			logger.debug("SearchAgentActionBean.onChangeRegion End");
		}
	}
	public void clearSearchAgentScreen(){
		if(logger.isDebugEnabled()){
			logger.debug("SearchAgentActionBean.clearSearchAgentScreen Start");
		}
		this.agentFormModel= new RetailerContactSearchViewModel();
		this.regions= new ArrayList<SelectItem>();
		this.retailers= new ArrayList<SelectItem>();	
		this.contactModelsList= new ArrayList<RetailerContactSearchViewModel>();
		this.listSize = 0;
		this.agentNetworkNameSortingOrder = this.UNSORTED;
		this.statusSortingOrder = UNSORTED;
		this.regionNameSortingOrder = this.UNSORTED;
		this.franchiseNameSortingOrder = this.UNSORTED;
		this.agentIDSortingOrder = UNSORTED;
		this.agentNameSortingOrder = UNSORTED;
		this.firstNameSortingOrder = UNSORTED;
		this.lastNameSortingOrder = UNSORTED;
		this.accountNickSortingOrder = UNSORTED;
		this.areaNameSortingOrder = UNSORTED;
		this.mobileNoSortingOrder = this.UNSORTED;
		this.userNameSortingOrder = this.UNSORTED;
		if(logger.isDebugEnabled()){
			logger.debug("SearchAgentActionBean.clearSearchAgentScreen End");
		}
	}
	
	public void searchAgent(){
		if(logger.isDebugEnabled()){
			logger.debug("SearchAgentActionBean.searchAgent Start");
		}
		try {
			if(this.agentFormModel.getRegionId()==null || this.agentFormModel.getRegionId()==0){
				this.agentFormModel.setRegionId(null);
			}
			if(this.agentFormModel.getDistributorId()==null || this.agentFormModel.getDistributorId()==0){
				this.agentFormModel.setDistributorId(null);
			}
			if(this.agentFormModel.getRetailerId()==null || this.agentFormModel.getRetailerId()==0){
				this.agentFormModel.setRetailerId(null);
			}
			if(this.agentFormModel.getOlaCustomerAccountTypeId() == null || this.agentFormModel.getOlaCustomerAccountTypeId() == 0){
				this.agentFormModel.setOlaCustomerAccountTypeId(null);
			}
			if(StringUtils.isEmpty(this.agentFormModel.getAgentName())){
				this.agentFormModel.setAgentName(null);
			}
			if(StringUtils.isEmpty(this.agentFormModel.getContactNo())){
				this.agentFormModel.setContactNo(null);
			}
			if(StringUtils.isEmpty(this.agentFormModel.getAgentId())){
				this.agentFormModel.setAgentId(null);
			}
			contactModelsList= new ArrayList<RetailerContactSearchViewModel>();
			SearchBaseWrapper searchBaseWrapper= new SearchBaseWrapperImpl();
			searchBaseWrapper.setBasePersistableModel(this.agentFormModel);
			searchBaseWrapper=agentHierarchyManager.searchAgentByCriteria(searchBaseWrapper);
			if(null!=searchBaseWrapper.getCustomList() && searchBaseWrapper.getCustomList().getResultsetList().size()>0)
			{
				contactModelsList= searchBaseWrapper.getCustomList().getResultsetList();
				this.listSize = contactModelsList.size();
				for(RetailerContactSearchViewModel model: contactModelsList)
				{
					model.setIndex(sortingPointer);
					sortingPointer++;
				}
			}
			
			logger.debug("Data Fetched");
		} catch (FrameworkCheckedException e) {
			// TODO Auto-generated catch block
			logger.error(e);
		}
		if(logger.isDebugEnabled()){
			logger.debug("SearchAgentActionBean.searchAgent End");
		}
	}
	public void deleteAgent(RetailerContactSearchViewModel model){
		if(logger.isDebugEnabled()){
			logger.debug("SearchAgentActionBean.deleteAgent Start");
		}
		try {
			RetailerContactListViewFormModel retailerContactListViewFormModel= new RetailerContactListViewFormModel();
			retailerContactListViewFormModel.setDistributorId(model.getDistributorId().toString());
			retailerContactListViewFormModel.setRegionId(model.getRegionId().toString());
			retailerContactListViewFormModel.setRetailerContactId(model.getRetailerContactId());
			
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			searchBaseWrapper.putObject("agentFormModel", retailerContactListViewFormModel);
			this.agentHierarchyManager.deleteAgent(searchBaseWrapper);
				for(RetailerContactSearchViewModel searchViewModel:this.contactModelsList){
					if(searchViewModel.getRetailerContactId().longValue()==model.getRetailerContactId().longValue()){
						this.contactModelsList.remove(searchViewModel);
					}
				}
				JSFContext.setInfoMessage(Constants.InfoMessages.AH_SEARCHAGENT_REMOVE_SUCCESS_INFO);
			} catch (FrameworkCheckedException e) {
				// TODO Auto-generated catch block
				logger.error(e);
			}catch(Exception e){
				logger.error(e);
				if(e instanceof DataIntegrityViolationException){
					JSFContext.addErrorMessage(Constants.ErrorMessages.AH_SEARCHAGENT_AGENTREFERENCEEXIST_ERROR);
				}
			}
		if(logger.isDebugEnabled()){
			logger.debug("SearchAgentActionBean.deleteAgent End");
		}
	}
	public void editAgent(RetailerContactSearchViewModel model){
		if(logger.isDebugEnabled()){
			logger.debug("SearchAgentActionBean.editAgent Start");
		}	
		if(logger.isDebugEnabled()){
			logger.debug("SearchAgentActionBean.editAgent End");
		}
	}
	public String addAgent(){
		if(logger.isDebugEnabled()){
			logger.debug("SearchAgentActionBean.addAgent Start");
		}	
		JSFContext.removeBean(Constants.AGENT_ACTION_BEAN);
		return Constants.ReturnCodes.SEARCH_AGENT_ADD_AGENT;
	}

	public boolean isExportToXlsAllowed()
    {
        return AuthenticationUtil.checkRightsIfAny( PortalConstants.ADMIN_GP_READ+","+ PortalConstants.PG_GP_READ+","+ PortalConstants.EXPORT_XLS_READ );
    }

    public boolean isAgentPrintFormAllowed()
    {
        return AuthenticationUtil.checkRightsIfAny( PortalConstants.ADMIN_GP_READ+","+ PortalConstants.PG_GP_READ+","+ PortalConstants.AGNT_PRINT_FORM_READ );
    }

	
	public RetailerContactSearchViewModel getAgentFormModel() {
		return agentFormModel;
	}
	public void setAgentFormModel(RetailerContactSearchViewModel agentFormModel) {
		this.agentFormModel = agentFormModel;
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
	public void setAgentHierarchyManager(AgentHierarchyManager agentHierarchyManager) {
		this.agentHierarchyManager = agentHierarchyManager;
	}
	public List<RegionModel> getRegionList() {
		return regionList;
	}
	public void setRegionList(List<RegionModel> regionList) {
		this.regionList = regionList;
	}
	public List<DistributorModel> getDistributorList() {
		return distributorList;
	}
	public void setDistributorList(List<DistributorModel> distributorList) {
		this.distributorList = distributorList;
	}
	public List<RetailerModel> getRetailerList() {
		return retailerList;
	}
	public void setRetailerList(List<RetailerModel> retailerList) {
		this.retailerList = retailerList;
	}
	public List<RetailerContactSearchViewModel> getContactModelsList() {
		return contactModelsList;
	}
	public void setContactModelsList(List<RetailerContactSearchViewModel> contactModelsList) {
		this.contactModelsList = contactModelsList;
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

	public int getListSize() {
		return listSize;
	}

	public void setListSize(int listSize) {
		this.listSize = listSize;
	}

	public int getAgentNetworkNameSortingOrder() {
		return agentNetworkNameSortingOrder;
	}

	public void setAgentNetworkNameSortingOrder(int agentNetworkNameSortingOrder) {
		this.agentNetworkNameSortingOrder = agentNetworkNameSortingOrder;
	}

	public int getRegionNameSortingOrder() {
		return regionNameSortingOrder;
	}

	public void setRegionNameSortingOrder(int regionNameSortingOrder) {
		this.regionNameSortingOrder = regionNameSortingOrder;
	}

	public int getFranchiseNameSortingOrder() {
		return franchiseNameSortingOrder;
	}

	public void setFranchiseNameSortingOrder(int franchiseNameSortingOrder) {
		this.franchiseNameSortingOrder = franchiseNameSortingOrder;
	}

	public int getMobileNoSortingOrder() {
		return mobileNoSortingOrder;
	}

	public void setMobileNoSortingOrder(int mobileNoSortingOrder) {
		this.mobileNoSortingOrder = mobileNoSortingOrder;
	}

	public int getStatusSortingOrder() {
		return statusSortingOrder;
	}

	public void setStatusSortingOrder(int statusSortingOrder) {
		this.statusSortingOrder = statusSortingOrder;
	}

	public int getAgentIDSortingOrder() {
		return agentIDSortingOrder;
	}

	public void setAgentIDSortingOrder(int agentIDSortingOrder) {
		this.agentIDSortingOrder = agentIDSortingOrder;
	}

	public int getAgentNameSortingOrder() {
		return agentNameSortingOrder;
	}

	public void setAgentNameSortingOrder(int agentNameSortingOrder) {
		this.agentNameSortingOrder = agentNameSortingOrder;
	}

	public int getUserNameSortingOrder() {
		return userNameSortingOrder;
	}

	public void setUserNameSortingOrder(int userNameSortingOrder) {
		this.userNameSortingOrder = userNameSortingOrder;
	}

	public int getFirstNameSortingOrder() {
		return firstNameSortingOrder;
	}

	public void setFirstNameSortingOrder(int firstNameSortingOrder) {
		this.firstNameSortingOrder = firstNameSortingOrder;
	}

	public int getLastNameSortingOrder() {
		return lastNameSortingOrder;
	}

	public void setLastNameSortingOrder(int lastNameSortingOrder) {
		this.lastNameSortingOrder = lastNameSortingOrder;
	}

	public int getAccountNickSortingOrder() {
		return accountNickSortingOrder;
	}

	public void setAccountNickSortingOrder(int accountNickSortingOrder) {
		this.accountNickSortingOrder = accountNickSortingOrder;
	}

	public int getAreaNameSortingOrder() {
		return areaNameSortingOrder;
	}

	public void setAreaNameSortingOrder(int areaNameSortingOrder) {
		this.areaNameSortingOrder = areaNameSortingOrder;
	}

	public List<SelectItem> getOlaAccountTypes() {
		return olaAccountTypes;
	}

	public void setOlaAccountTypes(List<SelectItem> olaAccountTypes) {
		this.olaAccountTypes = olaAccountTypes;
	}

	public List<OlaCustomerAccountTypeModel> getOlaCustomerAccountList() {
		return olaCustomerAccountList;
	}

	public void setOlaCustomerAccountList(
			List<OlaCustomerAccountTypeModel> olaCustomerAccountList) {
		this.olaCustomerAccountList = olaCustomerAccountList;
	}

	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}

	public int getAccountTypeSortingOrder() {
		return accountTypeSortingOrder;
	}

	public void setAccountTypeSortingOrder(int accountTypeSortingOrder) {
		this.accountTypeSortingOrder = accountTypeSortingOrder;
	}

	public int getSaleUserNameSortingOrder() {
		return saleUserNameSortingOrder;
	}

	public void setSaleUserNameSortingOrder(int saleUserNameSortingOrder) {
		this.saleUserNameSortingOrder = saleUserNameSortingOrder;
	}

	public int getRoleTitleSortingOrder() {
		return roleTitleSortingOrder;
	}

	public void setRoleTitleSortingOrder(int roleTitleSortingOrder) {
		this.roleTitleSortingOrder = roleTitleSortingOrder;
	}

	public void setAccountFacade(AccountFacade accountFacade) {
		this.accountFacade = accountFacade;
	}

	
	
}

