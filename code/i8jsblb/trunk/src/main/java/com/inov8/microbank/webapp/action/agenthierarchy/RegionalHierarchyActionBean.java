package com.inov8.microbank.webapp.action.agenthierarchy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.dao.DataIntegrityViolationException;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.model.agenthierarchy.RegionalHierarchyModel;
import com.inov8.microbank.common.util.AuthenticationUtil;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.Constants;
import com.inov8.microbank.common.util.GenericComparator;
import com.inov8.microbank.common.util.JSFContext;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.SessionBeanObjects;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.agenthierarchy.AgentHierarchyManager;

@ManagedBean(name="regionalHierarchyActionBean")
@SessionScoped
public class RegionalHierarchyActionBean {
	
	private static final Log logger= LogFactory.getLog(RegionalHierarchyActionBean.class);
	
	@ManagedProperty(value="#{agentHierarchyManager}") 
	private AgentHierarchyManager agentHierarchyManager;
	
	private RegionalHierarchyModel regionalHierarchyModel = new RegionalHierarchyModel();
	
	private Boolean securityCheck= Boolean.FALSE;
	private Boolean editSecurityCheck=Boolean.FALSE;

	private int UNSORTED = 0;
	private int ASCENDING_ORDER = 1;
	private int DESCENDING_ORDER = 2;
	
	private int nameSortingOrder = UNSORTED;
	private int statusSortingOrder = UNSORTED;

	private int listSize;
	
	private int sortingPointer = 0;

	List<RegionalHierarchyModel> regionalHierarchyModelList = new ArrayList<RegionalHierarchyModel>();
	
	public RegionalHierarchyActionBean() 
	{
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	public void ini()
	{
		String createButton=PortalConstants.ADMIN_GP_CREATE+","+PortalConstants.PG_GP_CREATE+","+
                PortalConstants.MNG_REG_HIER_CREATE;
        securityCheck=AuthenticationUtil.checkRightsIfAny(createButton);
        
        String updateButton=PortalConstants.ADMIN_GP_UPDATE+","+PortalConstants.PG_GP_UPDATE+","+
                PortalConstants.MNG_REG_HIER_UPDATE;
        editSecurityCheck=AuthenticationUtil.checkRightsIfAny(updateButton);
        this.regionalHierarchyModel.setActive(Boolean.TRUE);
        
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
	        HSSFSheet sheet = wb.createSheet("Exported Regional Hierarchy WorkBook");
	        HSSFCellStyle cellStyle = getCellStyle(wb); 
	        
	        HSSFRow row = sheet.createRow(0);
	        
	        HSSFCell cell = row.createCell(Short.valueOf("0"));
	        cell.setCellValue("Regional Hierarchy");
	        cell.setCellStyle(cellStyle);
	        
	        
	        cell = row.createCell(Short.valueOf("1"));
	        cell.setCellValue("Description");
	        cell.setCellStyle(cellStyle);
	        
	        cell = row.createCell(Short.valueOf("2"));
	        cell.setCellValue("Status");
	        cell.setCellStyle(cellStyle);
	        
	        
	        int rowCounter = 1;
	        for(RegionalHierarchyModel model: this.regionalHierarchyModelList)
	        {
	        	row = sheet.createRow(rowCounter);
	        	rowCounter++;
	        	
	        	cell = row.createCell(Short.valueOf("0"));
		        cell.setCellValue(model.getRegionalHierarchyName());
		        
		        cell = row.createCell(Short.valueOf("1"));
		        cell.setCellValue(model.getDescription());
		        
		        cell = row.createCell(Short.valueOf("2"));
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
	        res.setHeader("Content-disposition", "attachment;filename=regional hierarchy data.xls");

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

	public String editRegionalHierarchy()
	{
		if(logger.isDebugEnabled()){
			logger.debug("RegionalHierarchyActionBean.editRegionalHierarchy Starts");
		}
		String returnCode = null;
		try
		{
			String regionalHierarchyId = (String)JSFContext.getFromRequest("regionalHierarchyId");
			System.out.println("regionalHierarchyId: " + regionalHierarchyId);
			SearchBaseWrapper searchBaseWrapper = agentHierarchyManager.findRegionalHierarchyById(Long.valueOf(regionalHierarchyId));
			this.regionalHierarchyModel= (RegionalHierarchyModel)searchBaseWrapper.getBasePersistableModel();
			if(this.regionalHierarchyModel != null)
			{
				this.regionalHierarchyModel.setEditMode(Boolean.TRUE);
			}
			returnCode = Constants.ReturnCodes.SEARCH_REG_HIER_ADD_REG_HIER;
		}
		catch(FrameworkCheckedException fce)
		{
			logger.error(fce);
		}
		if(logger.isDebugEnabled()){
			logger.debug("RegionalHierarchyActionBean.editRegionalHierarchy End");
		}
		return returnCode;
	}
	
	public void searchRegionalHierarchyByCriteria(ActionEvent e)
	{
		if(logger.isDebugEnabled()){
			logger.debug("RegionalHierarchyActionBean.searchRegionalHierarchyByCriteria Starts");
		}
		try
		{
			String name = this.regionalHierarchyModel.getRegionalHierarchyName();
			this.clearRegionalHierarchyForm();
			this.regionalHierarchyModel.setRegionalHierarchyName(name);
			
			if(CommonUtils.isEmpty(this.regionalHierarchyModel.getRegionalHierarchyName()))
			{
				this.regionalHierarchyModel.setRegionalHierarchyName(null);
			}
			this.regionalHierarchyModel.setActive(null);
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			searchBaseWrapper.setBasePersistableModel(this.regionalHierarchyModel);
			agentHierarchyManager.searchRegionalHierarchyByCriteria(searchBaseWrapper);
			if(searchBaseWrapper.getCustomList() != null )
			{
				this.regionalHierarchyModelList = searchBaseWrapper.getCustomList().getResultsetList();
				this.listSize = regionalHierarchyModelList.size();
				for(RegionalHierarchyModel model: regionalHierarchyModelList)
				{
					model.setIndex(sortingPointer);
					sortingPointer++;
				}
			}
			else
			{
				this.listSize = 0;
				this.regionalHierarchyModelList= new ArrayList<RegionalHierarchyModel>();
				JSFContext.setInfoMessage("No Record Found.");
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			logger.error(ex);
		}
		if(logger.isDebugEnabled()){
			logger.debug("RegionalHierarchyActionBean.searchRegionalHierarchyByCriteria End");
		}
	}
	
	public boolean isExportToXlsAllowed()
	{
	    return AuthenticationUtil.checkRightsIfAny( PortalConstants.EXPORT_XLS_READ );
	}
	
	public String addRegionalHierarchy()
	{
		if(logger.isDebugEnabled()){
			logger.debug("RegionalHierarchyActionBean.addRegionalHierarchy Starts");
		}
		this.regionalHierarchyModel = new RegionalHierarchyModel();
		regionalHierarchyModel.setEditMode(Boolean.FALSE);
		return Constants.ReturnCodes.SEARCH_REG_HIER_ADD_REG_HIER;
	}
	
	public String next()
	{
		if(logger.isDebugEnabled()){
			logger.debug("RegionalHierarchyActionBean.next Starts");
		}
		String returnCode = Constants.ReturnCodes.REGIONAL_HIERARCHY_REGION;
		if(logger.isDebugEnabled()){
			logger.debug("RegionalHierarchyActionBean.next End");
		}
		return returnCode;
	}
	
	public String saveRegionalHierarchy()
	{
		if(logger.isDebugEnabled()){
			logger.debug("RegionalHierarchyActionBean.saveRegionalHierarchy Starts");
		}
		String returnCode = null;
		try {
			if(validate(this.regionalHierarchyModel))
			{
				regionalHierarchyModel.setCreatedOn(new Date());
				regionalHierarchyModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
				regionalHierarchyModel.setUpdatedOn(new Date());
				regionalHierarchyModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
				 	  
				BaseWrapper baseWrapper = new BaseWrapperImpl();
				baseWrapper.setBasePersistableModel(regionalHierarchyModel);
				baseWrapper=agentHierarchyManager.saveOrUpdateRegionalHierarchy(baseWrapper);
				returnCode = Constants.ReturnCodes.REGIONAL_HIERARCHY_REGION;
				JSFContext.setInfoMessage(Constants.InfoMessages.AH_REG_HIER_SAVE_INFO);
				logger.info(Constants.InfoMessages.AH_REG_HIER_SAVE_INFO);
			}
		} 
		catch (FrameworkCheckedException e1)
		{
			logger.error(e1);
		}
		catch(Exception e){
			logger.error(e);
			e.printStackTrace();
			if(e instanceof DataIntegrityViolationException){
				JSFContext.addErrorMessage(Constants.InfoMessages.AH_REG_HIER_NAME_INFO);
				logger.info(Constants.InfoMessages.AH_REG_HIER_NAME_INFO);
				if(!regionalHierarchyModel.isEditMode())
					regionalHierarchyModel.setRegionalHierarchyId(null);
			}
		}
		
		if(logger.isDebugEnabled()){
			logger.debug("RegionalHierarchyActionBean.saveRegionalHierarchy End");
		}
		return returnCode;
	}
	
	public Boolean validate(RegionalHierarchyModel regionalHierarchyModel)
	{
		if(logger.isDebugEnabled()){
			logger.debug("RegionalHierarchyActionBean.validate Starts");
		}
		boolean validated = Boolean.TRUE;
		if(CommonUtils.isEmpty(regionalHierarchyModel.getRegionalHierarchyName())){
			JSFContext.addErrorMessage(Constants.ErrorMessages.AH_REG_HIER_NAME_ERROR);
			validated = Boolean.FALSE;
		}
		if(logger.isDebugEnabled()){
			logger.debug("RegionalHierarchyActionBean.validate End");
		}
		return validated;
	}
	
	public String cancel()
	{
		SessionBeanObjects.removeAllSessionObjects();
		return Constants.ReturnCodes.CANCEL_REG_HIER;
	}
	
	public void onChangeActive(AjaxBehaviorEvent event)
	{
		if(this.regionalHierarchyModel.isEditMode()){
			try {
				Boolean isExist=agentHierarchyManager.doesRegionsExistOfRegionalHierarchy(this.regionalHierarchyModel.getRegionalHierarchyId());
				if(isExist){
					JSFContext.addErrorMessage(Constants.ErrorMessages.AH_REG_HIER_ACTIVE_ERROR);
					this.regionalHierarchyModel.setActive(Boolean.TRUE);
				}
			} catch (FrameworkCheckedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
	
	public void resetRegionalHierarchyFields(ActionEvent e){
		if(logger.isDebugEnabled()){
			logger.debug("RegionalHierarchyActionBean.resetRegionalHierarchyFields Starts");
		}
		regionalHierarchyModel.setActive(Boolean.FALSE);
		regionalHierarchyModel.setRegionalHierarchyName("");
		regionalHierarchyModel.setComments("");
		regionalHierarchyModel.setDescription("");
		if(logger.isDebugEnabled()){
			logger.debug("RegionalHierarchyActionBean.resetRegionalHierarchyFields End");
		}
	}
	
	public void sortByName(ActionEvent e)
	{
		if(this.nameSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.regionalHierarchyModelList, new GenericComparator(Boolean.FALSE));
			this.nameSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.nameSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.regionalHierarchyModelList, new GenericComparator("index", Boolean.TRUE));
			this.nameSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.regionalHierarchyModelList, new GenericComparator("regionalHierarchyName", Boolean.TRUE));
			this.nameSortingOrder = this.ASCENDING_ORDER;
		}
		this.statusSortingOrder = UNSORTED;
	}
	
	
	public void sortByStatus(ActionEvent e)
	{
		if(this.statusSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.regionalHierarchyModelList, new GenericComparator(Boolean.TRUE));
			this.statusSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.statusSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.regionalHierarchyModelList, new GenericComparator("index", Boolean.TRUE));
			this.statusSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.regionalHierarchyModelList, new GenericComparator("active", Boolean.FALSE));
			this.statusSortingOrder = this.ASCENDING_ORDER;
		}
		this.nameSortingOrder = this.UNSORTED;
	}
	
	public void clearRegionalHierarchyForm()
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("DistributorActionBean.clearRegionalHierarchyForm Starts");
		}
		
		this.regionalHierarchyModel = new RegionalHierarchyModel();
		this.regionalHierarchyModelList = new ArrayList<RegionalHierarchyModel>();
		this.listSize = 0;
		this.nameSortingOrder = this.UNSORTED;
		this.statusSortingOrder = UNSORTED;
		
		if(logger.isDebugEnabled()){
			logger.debug("DistributorActionBean.clearRegionalHierarchyForm End");
		}
	}
	
	public void clearAllSessionObjects(){
		if (!FacesContext.getCurrentInstance().isPostback()) {
			SessionBeanObjects.removeAllSessionObjects();
		}
	}

	public RegionalHierarchyModel getRegionalHierarchyModel() {
		return regionalHierarchyModel;
	}

	public void setRegionalHierarchyModel(
			RegionalHierarchyModel regionalHierarchyModel) {
		this.regionalHierarchyModel = regionalHierarchyModel;
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

	public void setAgentHierarchyManager(AgentHierarchyManager agentHierarchyManager) {
		this.agentHierarchyManager = agentHierarchyManager;
	}

	public List<RegionalHierarchyModel> getRegionalHierarchyModelList() {
		return regionalHierarchyModelList;
	}

	public void setRegionalHierarchyModelList(
			List<RegionalHierarchyModel> regionalHierarchyModelList) {
		this.regionalHierarchyModelList = regionalHierarchyModelList;
	}
	public int getListSize() {
		return listSize;
	}

	public void setListSize(int listSize) {
		this.listSize = listSize;
	}
	public int getNameSortingOrder() {
		return nameSortingOrder;
	}

	public void setNameSortingOrder(int nameSortingOrder) {
		this.nameSortingOrder = nameSortingOrder;
	}

	public int getStatusSortingOrder() {
		return statusSortingOrder;
	}

	public void setStatusSortingOrder(int statusSortingOrder) {
		this.statusSortingOrder = statusSortingOrder;
	}
}
