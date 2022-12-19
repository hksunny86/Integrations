package com.inov8.microbank.webapp.action.agenthierarchy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

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
import com.inov8.microbank.common.exception.EmailServiceSendFailureException;
import com.inov8.microbank.common.jms.DestinationConstants;
import com.inov8.microbank.common.model.CountryModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.PermissionGroupModel;
import com.inov8.microbank.common.model.RetailerModel;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.agenthierarchy.RegionModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationHistoryModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.model.portal.authorizationreferencedata.RetailerReferenceDataModel;
import com.inov8.microbank.common.model.retailermodule.RetailerContactListViewFormModel;
import com.inov8.microbank.common.model.usecasemodule.LevelCheckerModel;
import com.inov8.microbank.common.model.usecasemodule.UsecaseLevelModel;
import com.inov8.microbank.common.util.ActionAuthorizationConstantsInterface;
import com.inov8.microbank.common.util.AuthenticationUtil;
import com.inov8.microbank.common.util.AuthoirzationDetailEnum;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.Constants;
import com.inov8.microbank.common.util.GenericComparator;
import com.inov8.microbank.common.util.JSFContext;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.SessionBeanObjects;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.facade.portal.authorizationmodule.ActionAuthorizationFacade;
import com.inov8.microbank.server.facade.usecasemodule.UsecaseFacade;
import com.inov8.microbank.server.service.agenthierarchy.AgentHierarchyManager;
import com.inov8.microbank.server.service.jms.JmsProducer;
import com.inov8.microbank.server.service.portal.authorizationmodule.ActionAuthorizationHistoryManager;
import com.thoughtworks.xstream.XStream;

@ManagedBean(name="retailerBean")
@SessionScoped
public class RetailerActionBean {
	
	private static final Log logger= LogFactory.getLog(RetailerActionBean.class);
	
	@ManagedProperty(value="#{agentHierarchyManager}")
	private AgentHierarchyManager agentHierarchyManager;
	
	@ManagedProperty(value="#{referenceDataManager}")
	private ReferenceDataManager referenceDataManager;
	
	@ManagedProperty(value="#{actionAuthorizationFacade}")
	protected ActionAuthorizationFacade actionAuthorizationFacade;
	
	@ManagedProperty(value="#{actionAuthorizationHistoryManager}")
	protected ActionAuthorizationHistoryManager actionAuthorizationHistoryManager;
	
	@ManagedProperty(value="#{usecaseFacade}")
	protected UsecaseFacade usecaseFacade;	
	
	@ManagedProperty(value="#{jmsProducer}")
	protected JmsProducer jmsProducer;
	
	private List<DistributorModel> distributorModels=new ArrayList<DistributorModel>();
	private List<RegionModel> regionModels=new ArrayList<RegionModel>();
	private List<SelectItem> distributorItems=new ArrayList<SelectItem>();
	private List<SelectItem> regionItems= new ArrayList<SelectItem>();
	private RetailerModel retailerModel=new RetailerModel();
	private List<RetailerModel> retailerModels= new ArrayList<RetailerModel>();
	private List<SelectItem> countryItems= new ArrayList<SelectItem>();
	private Boolean editMode= Boolean.FALSE;
	private Boolean securityCheck= Boolean.FALSE;
	private Boolean editSecurityCheck=Boolean.FALSE;
	private int listSize;
	
	private int UNSORTED = 0;
	private int ASCENDING_ORDER = 1;
	private int DESCENDING_ORDER = 2;
	
	private int agentNetworkNameSortingOrder = UNSORTED;
	private int regionNameSortingOrder = UNSORTED;
	private int franchiseNameSortingOrder = UNSORTED;
	private int contactNameSortingOrder = UNSORTED;
	private int mobileNoSortingOrder = UNSORTED;
	private int emailSortingOrder = UNSORTED;
	private int statusSortingOrder = UNSORTED;
	
	private int sortingPointer = 0;
	
	public RetailerActionBean(){		
				
	}
	
	@PostConstruct
	public void loadSearchrefData(){
		if(logger.isDebugEnabled()){
			logger.debug("RetailerActionBean.loadSearchrefData Starts");
		}
		try {
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			searchBaseWrapper= agentHierarchyManager.loadFranchiseSearchRefData(searchBaseWrapper);
			distributorModels=searchBaseWrapper.getCustomList().getResultsetList();
			papolateDistributorItems();
			loadCountry();
			String createButton=PortalConstants.ADMIN_GP_CREATE+","+PortalConstants.PG_GP_CREATE+","+
                    PortalConstants.MNG_FRANCH_CREATE;
            securityCheck=AuthenticationUtil.checkRightsIfAny(createButton);
            
            String updateButton=PortalConstants.ADMIN_GP_UPDATE+","+PortalConstants.PG_GP_UPDATE+","+
                    PortalConstants.MNG_FRANCH_UPDATE;
			editSecurityCheck=AuthenticationUtil.checkRightsIfAny(updateButton);
					
			/// Added for Resubmit Authorization Request 
			
		    String isReSubmit =(String) JSFContext.getFromRequest("isReSubmit");
			
			if(null!=isReSubmit && isReSubmit.equals("true")){
				String actionAuthorizationIdStr = (String)JSFContext.getFromRequest("authId");
				Long actionAuthorizationId = Long.parseLong(actionAuthorizationIdStr);
				ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(actionAuthorizationId);
				
				if(actionAuthorizationModel.getCreatedById().longValue()!=UserUtils.getCurrentUser().getAppUserId().longValue()){
					throw new FrameworkCheckedException("illegal operation performed");
				}
				
				XStream xstream = new XStream();
				RetailerReferenceDataModel retailerReferenceDataModel = (RetailerReferenceDataModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
								
				populateRetailerModel(retailerReferenceDataModel);
				
				searchBaseWrapper=agentHierarchyManager.findRegionsByDistributorId(retailerModel.getDistributorIdDistributorModel().getDistributorId());
				regionModels=searchBaseWrapper.getCustomList().getResultsetList();
				papolateRegionItems();
			}
			///End Added for Resubmit Authorization Request
				
		} catch (FrameworkCheckedException e) {
			// TODO Auto-generated catch block
			logger.error(e);//e.printStackTrace();
		}
		if(logger.isDebugEnabled()){
			logger.debug("RetailerActionBean.loadSearchrefData End");
		}
	}

	public void sortByAgentNetworkName(ActionEvent e)
	{
		if(this.agentNetworkNameSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.retailerModels, new GenericComparator(Boolean.FALSE));
			this.agentNetworkNameSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.agentNetworkNameSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.retailerModels, new GenericComparator("index", Boolean.TRUE));
			this.agentNetworkNameSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.retailerModels, new GenericComparator("agentNetworkName", Boolean.TRUE));
			this.agentNetworkNameSortingOrder = this.ASCENDING_ORDER;
		}
		this.contactNameSortingOrder = UNSORTED;
		this.mobileNoSortingOrder = UNSORTED;
		this.emailSortingOrder = UNSORTED;
		this.statusSortingOrder = UNSORTED;
		this.regionNameSortingOrder = this.UNSORTED;
		this.franchiseNameSortingOrder = this.UNSORTED;
	}
	
	public void sortByContactName(ActionEvent e)
	{
		if(this.contactNameSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.retailerModels, new GenericComparator(Boolean.FALSE));
			this.contactNameSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.contactNameSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.retailerModels, new GenericComparator("index", Boolean.TRUE));
			this.contactNameSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.retailerModels, new GenericComparator("contactName", Boolean.TRUE));
			this.contactNameSortingOrder = this.ASCENDING_ORDER;
		}
		this.agentNetworkNameSortingOrder = this.UNSORTED;
		this.mobileNoSortingOrder = UNSORTED;
		this.emailSortingOrder = UNSORTED;
		this.statusSortingOrder = UNSORTED;
		this.regionNameSortingOrder = this.UNSORTED;
		this.franchiseNameSortingOrder = this.UNSORTED;
	}
	
	public void sortByMobileNo(ActionEvent e)
	{
		if(this.mobileNoSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.retailerModels, new GenericComparator(Boolean.FALSE));
			this.mobileNoSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.mobileNoSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.retailerModels, new GenericComparator("index", Boolean.TRUE));
			this.mobileNoSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.retailerModels, new GenericComparator("phoneNo", Boolean.TRUE));
			this.mobileNoSortingOrder = this.ASCENDING_ORDER;
		}
		this.agentNetworkNameSortingOrder = this.UNSORTED;
		this.contactNameSortingOrder = UNSORTED;
		this.emailSortingOrder = UNSORTED;
		this.regionNameSortingOrder = this.UNSORTED;
		this.franchiseNameSortingOrder = this.UNSORTED;
		this.statusSortingOrder = UNSORTED;
	}
	
	public void sortByEmail(ActionEvent e)
	{
		if(this.emailSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.retailerModels, new GenericComparator(Boolean.FALSE));
			this.emailSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.emailSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.retailerModels, new GenericComparator("index", Boolean.TRUE));
			this.emailSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.retailerModels, new GenericComparator("email", Boolean.TRUE));
			this.emailSortingOrder = this.ASCENDING_ORDER;
		}
		this.agentNetworkNameSortingOrder = this.UNSORTED;
		this.contactNameSortingOrder = UNSORTED;
		this.mobileNoSortingOrder = UNSORTED;
		this.regionNameSortingOrder = this.UNSORTED;
		this.franchiseNameSortingOrder = this.UNSORTED;
		this.statusSortingOrder = UNSORTED;
	}
	
	public void sortByRegionName(ActionEvent e)
	{
		if(this.regionNameSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.retailerModels, new GenericComparator(Boolean.FALSE));
			this.regionNameSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.regionNameSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.retailerModels, new GenericComparator("index", Boolean.TRUE));
			this.regionNameSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.retailerModels, new GenericComparator("regionModelName", Boolean.TRUE));
			this.regionNameSortingOrder = this.ASCENDING_ORDER;
		}
		this.agentNetworkNameSortingOrder = this.UNSORTED;
		this.contactNameSortingOrder = UNSORTED;
		this.mobileNoSortingOrder = UNSORTED;
		this.emailSortingOrder = UNSORTED;
		this.statusSortingOrder = UNSORTED;
		this.franchiseNameSortingOrder = this.UNSORTED;
	}
	
	public void sortByFranchiseName(ActionEvent e)
	{
		if(this.franchiseNameSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.retailerModels, new GenericComparator(Boolean.FALSE));
			this.franchiseNameSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.franchiseNameSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.retailerModels, new GenericComparator("index", Boolean.TRUE));
			this.franchiseNameSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.retailerModels, new GenericComparator("name", Boolean.TRUE));
			this.franchiseNameSortingOrder = this.ASCENDING_ORDER;
		}
		this.agentNetworkNameSortingOrder = this.UNSORTED;
		this.contactNameSortingOrder = UNSORTED;
		this.mobileNoSortingOrder = UNSORTED;
		this.emailSortingOrder = UNSORTED;
		this.statusSortingOrder = UNSORTED;
		this.regionNameSortingOrder = this.UNSORTED;
	}
	
	public void sortByStatus(ActionEvent e)
	{
		if(this.statusSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.retailerModels, new GenericComparator(Boolean.TRUE));
			this.statusSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.statusSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.retailerModels, new GenericComparator("index", Boolean.TRUE));
			this.statusSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.retailerModels, new GenericComparator("active", Boolean.FALSE));
			this.statusSortingOrder = this.ASCENDING_ORDER;
		}
		this.agentNetworkNameSortingOrder = this.UNSORTED;
		this.contactNameSortingOrder = UNSORTED;
		this.mobileNoSortingOrder = UNSORTED;
		this.emailSortingOrder = UNSORTED;
		this.regionNameSortingOrder = this.UNSORTED;
		this.franchiseNameSortingOrder = this.UNSORTED;
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
	        HSSFSheet sheet = wb.createSheet("Exported Franchise WorkBook");
	        HSSFCellStyle cellStyle = getCellStyle(wb); 
	        
	        HSSFRow row = sheet.createRow(0);
	        
	        HSSFCell cell = row.createCell(Short.valueOf("0"));
	        cell.setCellValue("Agent Network");
	        cell.setCellStyle(cellStyle);
	        
	        
	        cell = row.createCell(Short.valueOf("1"));
	        cell.setCellValue("Region");
	        cell.setCellStyle(cellStyle);
	        
	        cell = row.createCell(Short.valueOf("2"));
	        cell.setCellValue("Franchise/Branch Name");
	        cell.setCellStyle(cellStyle);
	        
	        cell = row.createCell(Short.valueOf("3"));
	        cell.setCellValue("Contact Name");
	        cell.setCellStyle(cellStyle);
	        
	        cell = row.createCell(Short.valueOf("4"));
	        cell.setCellValue("Mobile#");
	        cell.setCellStyle(cellStyle);
	        
	        cell = row.createCell(Short.valueOf("5"));
	        cell.setCellValue("Email");
	        cell.setCellStyle(cellStyle);
	        
	        cell = row.createCell(Short.valueOf("6"));
	        cell.setCellValue("Status");
	        cell.setCellStyle(cellStyle);
	        
	        
	        int rowCounter = 1;
	        for(RetailerModel model: this.retailerModels)
	        {
	        	row = sheet.createRow(rowCounter);
	        	rowCounter++;
	        	
	        	cell = row.createCell(Short.valueOf("0"));
	        	if(model.getRelationDistributorIdDistributorModel() != null)
	        	{
	        		cell.setCellValue(model.getRelationDistributorIdDistributorModel().getName());
	        	}
		        
		        cell = row.createCell(Short.valueOf("1"));
		        if(model.getRegionModel() != null)
		        {
		        	cell.setCellValue(model.getRegionModel().getRegionName());
		        }
		        
		        
		        cell = row.createCell(Short.valueOf("2"));
		        cell.setCellValue(model.getName());
		        
		        cell = row.createCell(Short.valueOf("3"));
		        cell.setCellValue(model.getContactName());
		        
		        cell = row.createCell(Short.valueOf("4"));
		        cell.setCellValue(model.getPhoneNo());
		        
		        cell = row.createCell(Short.valueOf("5"));
		        cell.setCellValue(model.getEmail());
		        
		        cell = row.createCell(Short.valueOf("6"));
		        if(model.getActive())
		        {
		        	cell.setCellValue("Active");
		        }	
		        else
		        {
		        	cell.setCellValue("In-Actvie");
		        }
		        
	        }
	        
	        FacesContext context = FacesContext.getCurrentInstance();
	        HttpServletResponse res = (HttpServletResponse) context.getExternalContext().getResponse();
	        res.setContentType("application/vnd.ms-excel");
	        res.setHeader("Content-disposition", "attachment;filename=franchise data.xls");

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

	public void clearAllSessionObjects(){
		if (!FacesContext.getCurrentInstance().isPostback()) {
			SessionBeanObjects.removeAllSessionObjects();
		}
	}

	public void papolateDistributorItems(){
		this.distributorItems=new ArrayList<SelectItem>();
		for(DistributorModel model:this.distributorModels){
			this.distributorItems.add(new SelectItem(model.getDistributorId(),model.getName()));
		}
	}
	
	public void onChangeDistributor(AjaxBehaviorEvent e){
		if(logger.isDebugEnabled()){
			logger.debug("RetailerActionBean.onChangeDistributor Starts");
		}
		try {
			SearchBaseWrapper searchBaseWrapper;
			searchBaseWrapper=agentHierarchyManager.findRegionsByDistributorId(retailerModel.getDistributorIdDistributorModel().getDistributorId());
			regionModels=searchBaseWrapper.getCustomList().getResultsetList();
			System.out.println(regionModels.size());
			papolateRegionItems();			
		} catch (FrameworkCheckedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(logger.isDebugEnabled()){
			logger.debug("RetailerActionBean.onChangeDistributor End");
		}
	}
	public void papolateRegionItems(){
		this.regionItems=new ArrayList<SelectItem>();
		for(RegionModel model:this.regionModels){
			this.regionItems.add(new SelectItem(model.getRegionId(),model.getRegionName()));
		}
	}
	
	public void clearSearchScreen(ActionEvent e){
		this.retailerModel=new RetailerModel();
		this.retailerModels= new ArrayList<RetailerModel>();
		this.listSize = 0;
		this.agentNetworkNameSortingOrder = this.UNSORTED;
		this.contactNameSortingOrder = UNSORTED;
		this.mobileNoSortingOrder = UNSORTED;
		this.emailSortingOrder = UNSORTED;
		this.regionNameSortingOrder = this.UNSORTED;
		this.franchiseNameSortingOrder = this.UNSORTED;
		this.statusSortingOrder = this.UNSORTED;
	}
	
	public void searchFranchise(){
		 if(logger.isDebugEnabled()){
				logger.debug("RetailerActionBean.searchFranchise Starts");
		 }
		 try 
		 {
			 String name = this.retailerModel.getName();
			 String phoneNo = this.retailerModel.getPhoneNo();
			 String contactName = this.retailerModel.getContactName();
			 String email = this.retailerModel.getEmail();
			 Long distributorId = this.retailerModel.getDistributorId();
			 Long regionId = this.retailerModel.getRegionModelId();
			 this.clearSearchScreen(null);
			 this.retailerModel.setName(name);
			 this.retailerModel.setContactName(contactName);
			 this.retailerModel.setPhoneNo(phoneNo);
			 this.retailerModel.setEmail(email);
			 this.retailerModel.setDistributorId(distributorId);
			 this.retailerModel.setRegionModelId(regionId);
			 
		 	if(CommonUtils.isEmpty(this.retailerModel.getName()))
			{
				this.retailerModel.setName(null);
			}
			if(CommonUtils.isEmpty(this.retailerModel.getPhoneNo()))
			{
				this.retailerModel.setPhoneNo(null);
			}
			if(CommonUtils.isEmpty(this.retailerModel.getContactName()))
			{
				this.retailerModel.setContactName(null);
			}
			if(CommonUtils.isEmpty(this.retailerModel.getEmail()))
			{
				this.retailerModel.setEmail(null);
			}
			if(this.retailerModel.getDistributorId()==null || this.retailerModel.getDistributorId()==0){
				this.retailerModel.setRelationDistributorIdDistributorModel(null);
			}
			if(this.retailerModel.getRegionModelId()==null || this.retailerModel.getRegionModelId()==0){
				this.retailerModel.setRegionModel(null);
			}
			 this.retailerModel.setActive(null);
			 this.retailerModels= new ArrayList<RetailerModel>();
			 SearchBaseWrapper searchBaseWrapper= new SearchBaseWrapperImpl();
			 searchBaseWrapper.setBasePersistableModel(this.retailerModel);		 
			 searchBaseWrapper= agentHierarchyManager.searchFranchiseByCriteria(searchBaseWrapper);
			 if(null!=searchBaseWrapper.getCustomList() && searchBaseWrapper.getCustomList().getResultsetList().size()>0)
			 {
				 retailerModels=(List<RetailerModel>)searchBaseWrapper.getCustomList().getResultsetList();
				 this.listSize = retailerModels.size();
				 for(RetailerModel model: retailerModels)
				 {
					model.setIndex(sortingPointer);
					sortingPointer++;
				 }
			 }			 
		} catch (FrameworkCheckedException e) {
			// TODO Auto-generated catch block
			logger.error(e);
		}		
		 if(logger.isDebugEnabled()){
				logger.debug("RetailerActionBean.searchFranchise End");
		 }
	}
	
	public String addFranchise(){		
			this.retailerModel= new RetailerModel();
			this.editMode=Boolean.FALSE;
			this.regionItems.clear();
			//loadCountry();
			return Constants.ReturnCodes.SEARCH_FRANCHISE_ADD_BRANCH;
		
	}
	public void loadCountry(){
		try{
			countryItems.clear();
			SearchBaseWrapper searchBaseWrapper= new SearchBaseWrapperImpl();		
			searchBaseWrapper=agentHierarchyManager.loadCountry(searchBaseWrapper);
			if(null!=searchBaseWrapper.getCustomList() && searchBaseWrapper.getCustomList().getResultsetList().size()>0){
				for(CountryModel countryModel:(List<CountryModel>)searchBaseWrapper.getCustomList().getResultsetList()){
					this.countryItems.add(new SelectItem(countryModel.getCountryId(),countryModel.getName()));
				}
			}
		}catch(FrameworkCheckedException e){
			logger.error(e);
		}
	}
	
	public void saveFranchise(){
		 if(logger.isDebugEnabled()){
				logger.debug("RetailerActionBean.saveFranchise Start");
		 }
		if(validateForm()){
		try {
				///Added for Resubmit Authorization Request
			
				Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
				String resubmitRequestStr = params.get("resubmitRequest");
				boolean resubmitRequest = false;
				String actionAuthorizationIdStr = params.get("actionAuthorizationId");
				Long  actionAuthorizationId = null;
				if(null!=resubmitRequestStr && resubmitRequestStr.equals("true")){
					actionAuthorizationId = Long.parseLong(actionAuthorizationIdStr);
					resubmitRequest = Boolean.parseBoolean(resubmitRequestStr);
				}
				///End Added for Resubmit Authorization Request
				
				BaseWrapper baseWrapper= new BaseWrapperImpl();
				UsecaseModel usecaseModel= null;
				
				if(!this.editMode){
					this.retailerModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
					this.retailerModel.setCreatedOn(new Date());
					usecaseModel = usecaseFacade.loadUsecase(PortalConstants.RETAILER_FORM_USECASE_ID);
					baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
					baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.RETAILER_FORM_USECASE_ID);
				}
				else{
					usecaseModel = usecaseFacade.loadUsecase(PortalConstants.RETAILER_FORM_UPDATE_USECASE_ID);
					baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
					baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.RETAILER_FORM_UPDATE_USECASE_ID);
				}
				this.retailerModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
				this.retailerModel.setUpdatedOn(new Date());
				this.retailerModel.setEditMode(this.editMode);
					
				this.retailerModel.setName(this.retailerModel.getName().trim());
				this.retailerModel.setContactName(this.retailerModel.getContactName().trim());
				this.retailerModel.setState(this.retailerModel.getState().trim());
				this.retailerModel.setCity(this.retailerModel.getCity().trim());
				this.retailerModel.setAddress1(this.retailerModel.getAddress1().trim());
				if(this.retailerModel.getAddress2() != null)
				{
					this.retailerModel.setAddress2(this.retailerModel.getAddress2().trim());
				}
				if(this.retailerModel.getEmail() != null)
				{
					this.retailerModel.setEmail(this.retailerModel.getEmail().trim());
				}
				if(this.retailerModel.getDescription() != null)
				{
					this.retailerModel.setDescription(this.retailerModel.getDescription().trim());
				}
				if(this.retailerModel.getComments() != null)
				{
					this.retailerModel.setComments(this.retailerModel.getComments().trim());
				}
				if(!usecaseModel.getIsAuthorizationEnable()){
					
					baseWrapper.setBasePersistableModel(this.retailerModel);
					
					PermissionGroupModel permissionGroupModel = new PermissionGroupModel();
					permissionGroupModel.setAppUserTypeId(UserTypeConstantsInterface.RETAILER);
					
					ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(permissionGroupModel, "name", SortingOrder.ASC);
					referenceDataWrapper.setBasePersistableModel(permissionGroupModel);
					referenceDataManager.getReferenceData(referenceDataWrapper);
					
					List<PermissionGroupModel> permissionGroupModelList = null;
					if (referenceDataWrapper.getReferenceDataList() != null) {
						permissionGroupModelList = referenceDataWrapper.getReferenceDataList();
						permissionGroupModel = permissionGroupModelList.get(0);
						baseWrapper.putObject("permissionGroupId", permissionGroupModel.getPermissionGroupId());
						agentHierarchyManager.saveOrUpdateFranchise(baseWrapper);
						this.retailerModel=(RetailerModel)baseWrapper.getBasePersistableModel();
						this.editMode = Boolean.TRUE;
						JSFContext.setInfoMessage(Constants.InfoMessages.AH_FRANCHISE_SAVE_SUCCESS_INFO);
						logger.info(Constants.InfoMessages.AH_FRANCHISE_SAVE_SUCCESS_INFO);	
					}
					else
					{
						JSFContext.addErrorMessage("Permission Group is missing in the system.");
					}
				}
				else
				{
					XStream xstream = new XStream();
					RetailerReferenceDataModel retailerReferenceDataModel = populateRefferenceDataModel(this.retailerModel);
					String refDataModelString= xstream.toXML(retailerReferenceDataModel);
					
					String  oldRetailerModelString=null; 
					
					if(usecaseModel.getUsecaseId() == PortalConstants.RETAILER_FORM_UPDATE_USECASE_ID)
					{			
						RetailerReferenceDataModel oldRetailerReferenceDataModel = new RetailerReferenceDataModel();
						RetailerModel oldRetailerModel = agentHierarchyManager.loadFranchise(retailerReferenceDataModel.getRetailerId());
						oldRetailerReferenceDataModel = populateRefferenceDataModel(oldRetailerModel);
						oldRetailerModelString= xstream.toXML(oldRetailerReferenceDataModel);
					}
					
					HttpServletRequest request = (HttpServletRequest) (FacesContext.getCurrentInstance().getExternalContext().getRequest());
					Long nextAuthorizationLevel = usecaseFacade.getNextAuthorizationLevel(usecaseModel.getUsecaseId(),new Long(0));
					if(nextAuthorizationLevel.intValue()<1)
					{
						baseWrapper.setBasePersistableModel(this.retailerModel);
						
						PermissionGroupModel permissionGroupModel = new PermissionGroupModel();
						permissionGroupModel.setAppUserTypeId(UserTypeConstantsInterface.RETAILER);
						
						ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(permissionGroupModel, "name", SortingOrder.ASC);
						referenceDataWrapper.setBasePersistableModel(permissionGroupModel);
						referenceDataManager.getReferenceData(referenceDataWrapper);
						
						List<PermissionGroupModel> permissionGroupModelList = null;
						if (referenceDataWrapper.getReferenceDataList() != null) {
							permissionGroupModelList = referenceDataWrapper.getReferenceDataList();
							permissionGroupModel = permissionGroupModelList.get(0);
							baseWrapper.putObject("permissionGroupId", permissionGroupModel.getPermissionGroupId());
							agentHierarchyManager.saveOrUpdateFranchise(baseWrapper);
							this.retailerModel=(RetailerModel)baseWrapper.getBasePersistableModel();
							this.editMode = Boolean.TRUE;
							
							actionAuthorizationId = performActionWithAllIntimationLevels(nextAuthorizationLevel,"", refDataModelString,oldRetailerModelString,usecaseModel,actionAuthorizationId ,request);							
							JSFContext.setInfoMessage("Action is authorized against reference Action ID : "+actionAuthorizationId+". "+Constants.InfoMessages.AH_FRANCHISE_SAVE_SUCCESS_INFO);
							logger.info(Constants.InfoMessages.AH_FRANCHISE_SAVE_SUCCESS_INFO);	
						}
						else
						{
							JSFContext.addErrorMessage("Permission Group is missing in the system.");
						}

					}
					else
					{
						String referenceRetailerId= "";
						if(null!=this.retailerModel.getRetailerId())
							referenceRetailerId = this.retailerModel.getRetailerId().toString();
						actionAuthorizationId = createAuthorizationRequest(nextAuthorizationLevel,"", refDataModelString,oldRetailerModelString,usecaseModel.getUsecaseId(),referenceRetailerId,resubmitRequest,actionAuthorizationId,request);
						JSFContext.setInfoMessage("Action is pending for approval against reference Action ID : "+actionAuthorizationId);
					}
				}
			} catch (FrameworkCheckedException e) {
				
				if(null!=e.getMessage() && e.getMessage().contains("already exist"))
					JSFContext.setInfoMessage(e.getMessage());
				e.printStackTrace();
			}
		}	
		 if(logger.isDebugEnabled()){
				logger.debug("RetailerActionBean.saveFranchise End");
		 }
	} 	
	public void clearFranchiseForm(){
		this.retailerModel= new RetailerModel();
		this.regionItems= new ArrayList<SelectItem>();
	}
	public Boolean validateForm(){
		Boolean validate= Boolean.TRUE;
		if(this.retailerModel.getDistributorId()<1){
			validate= Boolean.FALSE;
			JSFContext.addErrorMessage(Constants.ErrorMessages.AH_FRANCHISE_DISTNAME_ERROR);
		}
		if(this.retailerModel.getRegionModelId()<1){
			validate= Boolean.FALSE;
			JSFContext.addErrorMessage(Constants.ErrorMessages.AH_FRANCHISE_REGION_NAME_REQ_ERROR);
		}
		if(CommonUtils.isEmpty(this.retailerModel.getName())){
			validate= Boolean.FALSE;
			JSFContext.addErrorMessage(Constants.ErrorMessages.AH_FRANCHISE_BRANCHNAME_ERROR);
		}
		else if(!this.editMode)
		{
			try
			{
				if(!this.agentHierarchyManager.isRetailerNameUnique(this.retailerModel.getName()))
				{
					validate = Boolean.FALSE;
					JSFContext.addErrorMessage("Franchise/Branch Name already exist.");
				}
			}
			catch(Exception fce)
			{
				logger.error(fce);
			}
		}
		
		if(CommonUtils.isEmpty(this.retailerModel.getContactName())){
			validate= Boolean.FALSE;
			JSFContext.addErrorMessage(Constants.ErrorMessages.AH_FRANCHISE_CONTNAME_ERROR);
		}
		if(CommonUtils.isEmpty(this.retailerModel.getCity())){
			validate= Boolean.FALSE;
			JSFContext.addErrorMessage(Constants.ErrorMessages.AH_FRANCHISE_CITY_ERROR);
		}
		if(CommonUtils.isEmpty(this.retailerModel.getPhoneNo())){
			validate= Boolean.FALSE;
			JSFContext.addErrorMessage(Constants.ErrorMessages.AH_FRANCHISE_CONTACTNO_ERROR);
		}
		if(CommonUtils.isEmpty(this.retailerModel.getState())){
			validate= Boolean.FALSE;
			JSFContext.addErrorMessage(Constants.ErrorMessages.AH_FRANCHISE_PROVINCE_ERROR);
		}
		if(this.retailerModel.getAddress1().equals("")){
			validate= Boolean.FALSE;
			JSFContext.addErrorMessage(Constants.ErrorMessages.AH_FRANCHISE_ADDRESS_ERROR);
		}
		if(this.retailerModel.getCountry().equals("0")){
			validate= Boolean.FALSE;
			JSFContext.addErrorMessage(Constants.ErrorMessages.AH_FRANCHISE_COUNTRY_ERROR);
		}		
		return validate;
	}
	public String editFranchise(RetailerModel model){
		this.loadCountry();
		this.retailerModel=model;
		this.editMode=Boolean.TRUE;				
		this.regionItems.add(new SelectItem(model.getRegionModel().getRegionId(),model.getRegionModel().getRegionName()));		
		return Constants.ReturnCodes.SEARCH_FRANCHISE_ADD_BRANCH;
	}
	public void deleteFranchise(RetailerModel model){		
		try {
			BaseWrapper baseWrapper= new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(model);
			agentHierarchyManager.deleteFranchise(baseWrapper);
			for(RetailerModel retailerModel:retailerModels){
				if(retailerModel.getRetailerId().longValue()==model.getRetailerId().longValue()){
					this.retailerModels.remove(retailerModel);
				}
			}
			this.listSize = retailerModels.size();
		} catch (FrameworkCheckedException e) {
			// TODO Auto-generated catch block
			logger.error(e);
		}
	}
	public String cancel()
	{
		SessionBeanObjects.removeAllSessionObjects();
		return Constants.ReturnCodes.CANCEL_FRANCHISE;
	}
	public void onChangeActive(AjaxBehaviorEvent event){
		 if(logger.isDebugEnabled()){
				logger.debug("RetailerActionBean.onChangeActive Start");
		 }		 
		 if(this.editMode){
			 try {
				 if(agentHierarchyManager.isFranchiseRefExist(this.retailerModel.getRetailerId()))
				 {
					 this.retailerModel.setActive(Boolean.TRUE);
					JSFContext.addErrorMessage(Constants.ErrorMessages.AH_FRANCHISE_INACTIVE_ERROR); 
				 }
			} catch (FrameworkCheckedException e) {
				// TODO Auto-generated catch block
				logger.error(e);
			}
		 }
		 if(logger.isDebugEnabled()){
				logger.debug("RetailerActionBean.onChangeActive End");
		 }
		
	}

	public boolean isExportToXlsAllowed()
    {
        return AuthenticationUtil.checkRightsIfAny( PortalConstants.ADMIN_GP_READ +","+ PortalConstants.PG_GP_READ +","+ PortalConstants.EXPORT_XLS_READ );
    }

    public boolean isUserGroupsReadAllowed()
    {
        String userGrpPermission = PortalConstants.ADMIN_GP_CREATE + "," + PortalConstants.PG_GP_CREATE +","+ PortalConstants.MNG_USR_GRPS_CREATE;
        return AuthenticationUtil.checkRightsIfAny( userGrpPermission );
    }
    
    
//////Functions for Action Authorization
	protected Long createAuthorizationRequest(Long nextAuthorizationLevel,String comments,String refDataModelString,String oldRetailerModelString,Long usecaseId,String referenceId,boolean resubmitRequest,Long actionAuthorizationId,HttpServletRequest request) throws FrameworkCheckedException{
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
		actionAuthorizationModel.setOldReferenceData(oldRetailerModelString);
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
	
	protected Long performActionWithAllIntimationLevels(Long nextAuthorizationLevel,String comments,String refDataModelString,String oldRetailerModelString,UsecaseModel usecaseModel,Long actionAuthorizationId, HttpServletRequest request) throws FrameworkCheckedException{
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
		actionAuthorizationModel.setOldReferenceData(oldRetailerModelString);
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
//////End Functions for Action Authorization	
	private RetailerReferenceDataModel  populateRefferenceDataModel(RetailerModel retailerModel) throws FrameworkCheckedException{
		RetailerReferenceDataModel retailerReferenceDataModel = new RetailerReferenceDataModel();
		
		
		retailerReferenceDataModel.setCreatedById(retailerModel.getCreatedBy());	
		retailerReferenceDataModel.setCreatedOn(retailerModel.getCreatedOn());
		retailerReferenceDataModel.setRetailerId(retailerModel.getRetailerId());	
		retailerReferenceDataModel.setUpdatedById(retailerModel.getUpdatedBy()); 
		retailerReferenceDataModel.setUpdatedOn(retailerModel.getUpdatedOn());
		
		retailerReferenceDataModel.setDistributorId(retailerModel.getDistributorId());	
		
		for (DistributorModel distributorModel : this.distributorModels){		
			if(retailerModel.getDistributorId().longValue()==distributorModel.getDistributorId().longValue()){
				retailerReferenceDataModel.setDistributorName(distributorModel.getName());
			}	
		}
		
		retailerReferenceDataModel.setRetailerId(retailerModel.getRetailerId());
		retailerReferenceDataModel.setRegionId(retailerModel.getRegionModelId());
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl(); 
		searchBaseWrapper=agentHierarchyManager.findRegionsByDistributorId(retailerModel.getDistributorIdDistributorModel().getDistributorId());
		regionModels=searchBaseWrapper.getCustomList().getResultsetList();
		for (RegionModel regionModel : this.regionModels){		
			if(retailerModel.getRegionModelId().longValue()==regionModel.getRegionId().longValue()){
				retailerReferenceDataModel.setRegionName(regionModel.getRegionName());
			}	
		}
		
		retailerReferenceDataModel.setName(retailerModel.getName());
		retailerReferenceDataModel.setContactName(retailerModel.getContactName());
		retailerReferenceDataModel.setState(retailerModel.getState());
		retailerReferenceDataModel.setCountry(retailerModel.getCountry());
		retailerReferenceDataModel.setCity(retailerModel.getCity());
		retailerReferenceDataModel.setAddress1(retailerModel.getAddress1());
		retailerReferenceDataModel.setFax(retailerModel.getFax());
		retailerReferenceDataModel.setZip(retailerModel.getZip());
		retailerReferenceDataModel.setPhoneNo(retailerModel.getPhoneNo());
		retailerReferenceDataModel.setAddress2(retailerModel.getAddress2());
		retailerReferenceDataModel.setEmail(retailerModel.getEmail());
		retailerReferenceDataModel.setDescription(retailerModel.getDescription());
		retailerReferenceDataModel.setComments(retailerModel.getComments());
		retailerReferenceDataModel.setEditMode(retailerModel.getEditMode());
		retailerReferenceDataModel.setActive(retailerModel.getActive());
		retailerReferenceDataModel.setIndex(retailerModel.getIndex());
		retailerReferenceDataModel.setVersionNo(retailerModel.getVersionNo());
		return retailerReferenceDataModel;	
	} 
	
	private void  populateRetailerModel(RetailerReferenceDataModel retailerReferenceDataModel){
				
		this.retailerModel = new RetailerModel();		
		this.retailerModel.setCreatedBy(retailerReferenceDataModel.getCreatedById());	
		this.retailerModel.setCreatedOn(retailerReferenceDataModel.getCreatedOn());
		this.retailerModel.setRetailerId(retailerReferenceDataModel.getRetailerId());
		this.retailerModel.setUpdatedBy(retailerReferenceDataModel.getUpdatedById());
		this.retailerModel.setUpdatedOn(retailerReferenceDataModel.getUpdatedOn());	
		this.retailerModel.setDistributorId(retailerReferenceDataModel.getDistributorId());	
		this.retailerModel.setRetailerId(retailerReferenceDataModel.getRetailerId());
		this.retailerModel.setRegionModelId(retailerReferenceDataModel.getRegionId());
		this.retailerModel.setName(retailerReferenceDataModel.getName());
		this.retailerModel.setContactName(retailerReferenceDataModel.getContactName());
		this.retailerModel.setState(retailerReferenceDataModel.getState());
		this.retailerModel.setCountry(retailerReferenceDataModel.getCountry());
		this.retailerModel.setCity(retailerReferenceDataModel.getCity());
		this.retailerModel.setAddress1(retailerReferenceDataModel.getAddress1());
		this.retailerModel.setFax(retailerReferenceDataModel.getFax());
		this.retailerModel.setZip(retailerReferenceDataModel.getZip());
		this.retailerModel.setPhoneNo(retailerReferenceDataModel.getPhoneNo());
		this.retailerModel.setAddress2(retailerReferenceDataModel.getAddress2());
		this.retailerModel.setEmail(retailerReferenceDataModel.getEmail());
		this.retailerModel.setDescription(retailerReferenceDataModel.getDescription());
		this.retailerModel.setComments(retailerReferenceDataModel.getComments());
		this.retailerModel.setEditMode(retailerReferenceDataModel.isEditMode());
		this.editMode = retailerReferenceDataModel.isEditMode();
		this.retailerModel.setActive(retailerReferenceDataModel.getActive());	
		this.retailerModel.setIndex(retailerReferenceDataModel.getIndex());	
		this.retailerModel.setVersionNo(retailerReferenceDataModel.getVersionNo());
	} 
	

	public RetailerModel getRetailerModel() {
		return retailerModel;
	}

	public void setRetailerModel(RetailerModel retailerModel) {
		this.retailerModel = retailerModel;
	}

	public List<RetailerModel> getRetailerModels() {
		return retailerModels;
	}

	public void setRetailerModels(List<RetailerModel> retailerModels) {
		this.retailerModels = retailerModels;
	}
	
	public List<DistributorModel> getDistributorModels() {
		return distributorModels;
	}

	public void setDistributorModels(List<DistributorModel> distributorModels) {
		this.distributorModels = distributorModels;
	}

	public List<RegionModel> getRegionModels() {
		return regionModels;
	}

	public void setRegionModels(List<RegionModel> regionModels) {
		this.regionModels = regionModels;
	}

	public List<SelectItem> getDistributorItems() {
		return distributorItems;
	}

	public void setDistributorItems(List<SelectItem> distributorItems) {
		this.distributorItems = distributorItems;
	}

	public List<SelectItem> getRegionItems() {
		return regionItems;
	}

	public void setRegionItems(List<SelectItem> regionItems) {
		this.regionItems = regionItems;
	}
	public static Log getLogger() {
		return logger;
	}
	public void setAgentHierarchyManager(AgentHierarchyManager agentHierarchyManager) {
		this.agentHierarchyManager = agentHierarchyManager;
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
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
	public List<SelectItem> getCountryItems() {
		return countryItems;
	}

	public void setCountryItems(List<SelectItem> countryItems) {
		this.countryItems = countryItems;
	}	
	public Boolean getEditMode() {
		return editMode;
	}

	public void setEditMode(Boolean editMode) {
		this.editMode = editMode;
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

	public int getContactNameSortingOrder() {
		return contactNameSortingOrder;
	}

	public void setContactNameSortingOrder(int contactNameSortingOrder) {
		this.contactNameSortingOrder = contactNameSortingOrder;
	}

	public int getMobileNoSortingOrder() {
		return mobileNoSortingOrder;
	}

	public void setMobileNoSortingOrder(int mobileNoSortingOrder) {
		this.mobileNoSortingOrder = mobileNoSortingOrder;
	}

	public int getEmailSortingOrder() {
		return emailSortingOrder;
	}

	public void setEmailSortingOrder(int emailSortingOrder) {
		this.emailSortingOrder = emailSortingOrder;
	}

	public int getStatusSortingOrder() {
		return statusSortingOrder;
	}

	public void setStatusSortingOrder(int statusSortingOrder) {
		this.statusSortingOrder = statusSortingOrder;
	}
	
}
