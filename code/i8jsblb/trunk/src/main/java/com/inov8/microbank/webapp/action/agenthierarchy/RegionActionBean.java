package com.inov8.microbank.webapp.action.agenthierarchy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
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
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.model.agenthierarchy.RegionModel;
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


@ManagedBean(name="regionActionBean")
@SessionScoped
public class RegionActionBean {

	private static final Log logger= LogFactory.getLog(RegionActionBean.class);
	@ManagedProperty(value="#{agentHierarchyManager}") 
	private AgentHierarchyManager agentHierarchyManager;
	
	private RegionModel regionModel = new RegionModel();
	
	private List<RegionModel> regionModelList = null;
	private List<RegionModel> deletedRegionModelList = null;
	
	private RegionalHierarchyModel regionalHierarchyModel;

	private String backupRegionName;
	private Boolean securityCheck= Boolean.FALSE;
	private Boolean editSecurityCheck=Boolean.FALSE;
	private Boolean deleteSecurityCheck=Boolean.FALSE;
	
	private int listSize;
	
	private int UNSORTED = 0;
	private int ASCENDING_ORDER = 1;
	private int DESCENDING_ORDER = 2;
	
	private int regionNameSortingOrder = UNSORTED;
	private int statusSortingOrder = UNSORTED;

	private int sortingPointer = 0;
	
	public RegionActionBean()
	{
	}
	
	@PostConstruct
	public void ini()
	{
		if(logger.isDebugEnabled()){
			logger.debug("RegionActionBean.ini Starts");
		}
		try
		{
			deletedRegionModelList = new ArrayList<RegionModel>();
			this.regionalHierarchyModel = ((RegionalHierarchyActionBean)JSFContext.getBean(Constants.REGIONAL_HIERARCHY_ACTION_BEAN)).getRegionalHierarchyModel();	
			if(regionalHierarchyModel.getRegionalHierarchyId() != null)
			{
				SearchBaseWrapper searchBaseWrapper = agentHierarchyManager.findRegionsByRegionalHierarchyId(regionalHierarchyModel.getRegionalHierarchyId());
				if(searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0)
				{
					regionModelList = searchBaseWrapper.getCustomList().getResultsetList();
					this.listSize = this.regionModelList.size();
					for(RegionModel model: regionModelList)
					{
						model.setIndex(sortingPointer);
						sortingPointer++;
					}
				}
				else
				{
					regionModelList = new ArrayList<RegionModel>();
					this.listSize = this.regionModelList.size();
				}
				
				if(this.regionalHierarchyModel.getActive().equals(Boolean.FALSE))
				{
					this.regionModel.setActiveDisabled(Boolean.TRUE);
					this.regionModel.setActive(Boolean.FALSE);
				}
				else
				{
					this.regionModel.setActiveDisabled(Boolean.FALSE);
					this.regionModel.setActive(Boolean.TRUE);
				}
				
				String editUpdateButton=PortalConstants.ADMIN_GP_CREATE+","+PortalConstants.PG_GP_CREATE+","+
                        PortalConstants.MNG_AGNT_NET_CREATE;
				securityCheck=AuthenticationUtil.checkRightsIfAny(editUpdateButton);	
			}
			else
			{
				HttpServletResponse response = (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
				HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
				response.sendRedirect(request.getContextPath()+"/home.html");
			}
			
			if(this.regionalHierarchyModel.getActive().equals(Boolean.FALSE))
			{
				this.regionModel.setActiveDisabled(Boolean.TRUE);
				this.regionModel.setActive(Boolean.FALSE);
			}
			else
			{
				this.regionModel.setActiveDisabled(Boolean.FALSE);
				this.regionModel.setActive(Boolean.TRUE);
			}
			
			String createButton=PortalConstants.ADMIN_GP_CREATE+","+PortalConstants.PG_GP_CREATE+","+
                    PortalConstants.MNG_AGNT_NET_CREATE;
            securityCheck=AuthenticationUtil.checkRightsIfAny(createButton);
            
            String updateButton=PortalConstants.ADMIN_GP_UPDATE+","+PortalConstants.PG_GP_UPDATE+","+
                    PortalConstants.MNG_AGNT_NET_UPDATE;
            editSecurityCheck=AuthenticationUtil.checkRightsIfAny(updateButton);
            String deleteButton=PortalConstants.ADMIN_GP_UPDATE+","+PortalConstants.PG_GP_UPDATE+","+
                    PortalConstants.MNG_AGNT_NET_UPDATE;
			setDeleteSecurityCheck(AuthenticationUtil.checkRightsIfAny(deleteButton));
			
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
			logger.debug("RegionActionBean.ini End");
		}
	}
	
	public void sortByRegionName(ActionEvent e)
	{
		if(this.regionNameSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.regionModelList, new GenericComparator(Boolean.FALSE));
			this.regionNameSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.regionNameSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.regionModelList, new GenericComparator("index", Boolean.TRUE));
			this.regionNameSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.regionModelList, new GenericComparator("regionName", Boolean.TRUE));
			this.regionNameSortingOrder = this.ASCENDING_ORDER;
		}
		this.statusSortingOrder = UNSORTED;
	}
	
	public void sortByStatus(ActionEvent e)
	{
		if(this.statusSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.regionModelList, new GenericComparator(Boolean.TRUE));
			this.statusSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.statusSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.regionModelList, new GenericComparator("index", Boolean.TRUE));
			this.statusSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.regionModelList, new GenericComparator("active", Boolean.FALSE));
			this.statusSortingOrder = this.ASCENDING_ORDER;
		}
		this.regionNameSortingOrder = this.UNSORTED;
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
	        HSSFSheet sheet = wb.createSheet("Exported Regions WorkBook");
	        HSSFCellStyle cellStyle = getCellStyle(wb); 
	        
	        HSSFRow row = sheet.createRow(0);
	        
	        HSSFCell cell = row.createCell(Short.valueOf("0"));
	        cell.setCellValue("Agent Network");
	        cell.setCellStyle(cellStyle);
	        
	        
	        cell = row.createCell(Short.valueOf("1"));
	        cell.setCellValue("Region");
	        cell.setCellStyle(cellStyle);
	        
	        cell = row.createCell(Short.valueOf("2"));
	        cell.setCellValue("Status");
	        cell.setCellStyle(cellStyle);
	        
	        
	        int rowCounter = 1;
	        for(RegionModel model: this.regionModelList)
	        {
	        	row = sheet.createRow(rowCounter);
	        	rowCounter++;
	        	
	        	cell = row.createCell(Short.valueOf("0"));
	        	DistributorActionBean dab = (DistributorActionBean)JSFContext.getBean(Constants.DISTRIBUTOR_ACTION_BEAN);
	        	cell.setCellValue(dab.getDistributorVO().getName());
		        
		        cell = row.createCell(Short.valueOf("1"));
		        cell.setCellValue(model.getRegionName());
		        
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
	
	public void regionIntegrityCheck(AjaxBehaviorEvent event)
	{
		if(logger.isDebugEnabled()){
			logger.debug("RegionActionBean.regionIntegrityCheck End");
		}
		try
		{
			if(!this.regionModel.getActive() && this.regionModel.getRegionId() != null)
			{
				boolean recordFoundFlag = this.agentHierarchyManager.regionIntegrityCheck(this.regionModel.getRegionId());
				if(recordFoundFlag)
				{
					JSFContext.addErrorMessage(Constants.ErrorMessages.AH_REGION_REGION_INACTIVE_ERROR);
					this.regionModel.setActive(Boolean.TRUE);
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
			logger.debug("RegionActionBean.regionIntegrityCheck End");
		}
	}
	
	public String cancel()
	{
		if(logger.isDebugEnabled()){
			logger.debug("RegionActionBean.cancel Starts");
		}
		SessionBeanObjects.removeAllSessionObjects();
		if(logger.isDebugEnabled()){
			logger.debug("RegionActionBean.cancel End");
		}
		return Constants.ReturnCodes.CANCEL;
	}
	
	public String previous()
	{
		if(logger.isDebugEnabled()){
			logger.debug("RegionActionBean.previous Starts");
		}
		JSFContext.removeBean(Constants.REGION_ACTION_BEAN);
		if(logger.isDebugEnabled()){
			logger.debug("RegionActionBean.previous End");
		}
		return Constants.ReturnCodes.REGION_REG_HIER;
	}
	
	public String next()
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("RegionActionBean.next Starts");
		}
		String returnValue = Constants.ReturnCodes.REGION_AREA_LEVEL;
		if(logger.isDebugEnabled())
		{
			logger.debug("RegionActionBean.next End");
		}
		return returnValue;
	}
	
	public String saveAndNext()
	{
		if(logger.isDebugEnabled()){
			logger.debug("RegionActionBean.saveAndNext Starts");
		}
		String returnValue = null;
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		List<RegionModel> localModelList = new ArrayList<RegionModel>();
		try
		{
			if(this.regionModelList == null || this.regionModelList.size() == 0)
			{
				JSFContext.addErrorMessage(Constants.ErrorMessages.AH_REGION_REGION_ONE_REQ_ERROR);
				logger.info(Constants.ErrorMessages.AH_REGION_REGION_ONE_REQ_ERROR);
			}
			else
			{
				if(this.regionModel.isDirty())
				{
					JSFContext.addErrorMessage(Constants.ErrorMessages.AH_REGION_SAVE_INLINE_ROW_ERROR);
				}
				else
				{
					localModelList.addAll(this.regionModelList);
					if(this.deletedRegionModelList != null && this.deletedRegionModelList.size() > 0)
					{
						localModelList.addAll(this.deletedRegionModelList);
					}
					
					CustomList customList = new CustomList();
					customList.setResultsetList(localModelList);
					searchBaseWrapper.setCustomList(customList);
					agentHierarchyManager.saveOrUpdateRegion(searchBaseWrapper);
					this.regionModelList = searchBaseWrapper.getCustomList().getResultsetList();
					this.regionModelList.removeAll(this.deletedRegionModelList);
					this.deletedRegionModelList.clear();
					returnValue = Constants.ReturnCodes.REGION_AREA_LEVEL;
					JSFContext.setInfoMessage(Constants.InfoMessages.AH_REGION_REGION_SAVE_SUCCESS_INFO);
					logger.info(Constants.InfoMessages.AH_REGION_REGION_SAVE_SUCCESS_INFO);
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
			if(e instanceof DataIntegrityViolationException)
			{
				for(RegionModel model :this.deletedRegionModelList)
				{
					model.setDeleted(Boolean.FALSE);
				}
				for(RegionModel model:this.regionModelList)
				{
					if(model.getVersionNo() == 0)
					{
						model.setRegionId(null);
						model.setVersionNo(null);
					}
					else
					{
						int version = model.getVersionNo();
						version--;
						model.setVersionNo(version);
					}
				}	
				this.regionModelList.addAll(this.deletedRegionModelList);
				this.deletedRegionModelList.clear();
				JSFContext.addErrorMessage(Constants.ErrorMessages.AH_REGION_REGION_DELETE_FAILURE_ERROR);
				logger.info(Constants.ErrorMessages.AH_REGION_REGION_DELETE_FAILURE_ERROR);
			}
		}
		if(logger.isDebugEnabled()){
			logger.debug("RegionActionBean.saveAndNext End");
		}
		return returnValue;
	}
	
	public void addRegion(ActionEvent e)
	{
		if(logger.isDebugEnabled()){
			logger.debug("RegionActionBean.addRegion Starts");
		}
		try
		{
			if(this.validate(this.regionModel))
			{
				long theDate = new Date().getTime();
				
				this.regionModel.setRegionName(this.regionModel.getRegionName().trim());
				if(this.regionModel.getDescription() != null)
				{
					this.regionModel.setDescription(this.regionModel.getDescription().trim());
				}
				if(this.regionModel.getComments() != null)
				{
					this.regionModel.setComments(this.regionModel.getComments().trim());
				}
				
				if(regionModel.isDirty())											//		regionVO already referencing to object in regionVOList so no need to add
				{
					this.regionModel.setUpdatedOn(new Date(theDate));
					this.regionModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
					this.regionModel.setDirty(Boolean.FALSE);
					this.regionModel = new RegionModel();
					this.backupRegionName = null;
					JSFContext.setInfoMessage("The record has been updated in the grid but it will be updated in the system on next button.");
				}
				else
				{
					this.regionModel.setRegionalHierarchyId(this.regionalHierarchyModel.getRegionalHierarchyId());
					this.regionModel.setUpdatedOn(new Date(theDate));
					this.regionModel.setCreatedOn(new Date(theDate));
					this.regionModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
					this.regionModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
					this.regionModel.setIndex(this.sortingPointer++);
					this.regionModelList.add(this.regionModel);
					this.listSize = this.regionModelList.size();
					JSFContext.setInfoMessage("The record has been added in the grid but it will be saved in the system on next button.");
					this.regionModel = new RegionModel();
				}
				if(this.regionalHierarchyModel.getActive().equals(Boolean.FALSE))
				{
					this.regionModel.setActiveDisabled(Boolean.TRUE);
					this.regionModel.setActive(Boolean.FALSE);
				}
				else
				{
					this.regionModel.setActiveDisabled(Boolean.FALSE);
					this.regionModel.setActive(Boolean.TRUE);
				}
			}
		}
		catch(Exception ex)
		{
			logger.error(ex);
		}
		if(logger.isDebugEnabled()){
			logger.debug("RegionActionBean.addRegion End");
		}
	}
	
	public void deleteRegion(ActionEvent e)
	{
		if(logger.isDebugEnabled()){
			logger.debug("RegionActionBean.deleteRegion Starts");
		}
		try
		{
			int rowIndex = Integer.valueOf((String)JSFContext.getFromRequest("rowIndexValue"));		// 		TODO This code need verification.
			RegionModel model = this.regionModelList.get(rowIndex);           						// 		TODO This code need verification.
			if(model.getRegionId() != null && model.getRegionId() > 0)
			{
				model.setDeleted(Boolean.TRUE);
				this.deletedRegionModelList.add(model);
			}
			if(model.isDirty())
			{
				this.regionModel = new RegionModel();
			}
			this.regionModelList.remove(model);
			this.listSize = this.regionModelList.size();
			JSFContext.setInfoMessage("The record has been removed from the grid but it will be deleted from system on next button.");
			
		}
		catch(Exception ex)
		{
			logger.error(ex);
		}
		if(logger.isDebugEnabled()){
			logger.debug("RegionActionBean.deleteRegion End");
		}
	}
	
	public void editRegion(ActionEvent e)
	{
		if(logger.isDebugEnabled()){
			logger.debug("RegionActionBean.editRegion Starts");
		}
		try
		{
			if(this.regionModel.isDirty())
			{
				JSFContext.addErrorMessage(Constants.ErrorMessages.AH_REGION_SAVE_INLINE_ROW_ERROR);
			}
			else
			{
					int rowIndex = Integer.valueOf((String)JSFContext.getFromRequest("rowIndexValue"));		// 		TODO This code need verification.
					this.regionModel = this.regionModelList.get(rowIndex);				// 		TODO This code need verification.
					this.regionModel.setDirty(Boolean.TRUE);
					this.backupRegionName = this.regionModel.getRegionName();
					if(this.regionalHierarchyModel.getActive().equals(Boolean.FALSE))
					{
						this.regionModel.setActiveDisabled(Boolean.TRUE);
						this.regionModel.setActive(Boolean.FALSE);
					}
					else
					{
						this.regionModel.setActiveDisabled(Boolean.FALSE);
					}	
			}
		}
		catch(Exception ex)
		{
			logger.error(ex);
		}
		if(logger.isDebugEnabled()){
			logger.debug("RegionActionBean.editRegion End");
		}
	}
	
	public void clearRegionForm(ActionEvent e)
	{
		if(logger.isDebugEnabled()){
			logger.debug("RegionActionBean.clearRegionForm Starts");
		}
		if(this.regionModel.isDirty())
		{
			this.regionModel.setDirty(Boolean.FALSE);
			this.regionModel.setRegionName(this.backupRegionName);
		}
		
		this.regionModel = new RegionModel();
		this.backupRegionName = null;
		if(this.regionalHierarchyModel.getActive().equals(Boolean.FALSE))
		{
			this.regionModel.setActiveDisabled(Boolean.TRUE);
			this.regionModel.setActive(Boolean.FALSE);
		}
		else
		{
			this.regionModel.setActiveDisabled(Boolean.FALSE);
			this.regionModel.setActive(Boolean.TRUE);
		}
		
		if(logger.isDebugEnabled()){
			logger.debug("RegionActionBean.clearRegionForm End");
		}
	}
	
	private boolean validate(RegionModel paramModel)
	{
		if(logger.isDebugEnabled()){
			logger.debug("RegionActionBean.validate Starts");
		}
		boolean validated = Boolean.TRUE;
		try
		{
			if(CommonUtils.isEmpty(paramModel.getRegionName()))
			{
				validated = Boolean.FALSE;
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(Constants.ErrorMessages.AH_REGION_REGION_NAME_REQ_ERROR));
			}
			else
			{
				for(RegionModel localModel:this.regionModelList)
				{
					
					if(!localModel.isDirty() && paramModel.getRegionName().equals(localModel.getRegionName()))
					{
						
						validated = Boolean.FALSE;
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(Constants.ErrorMessages.AH_REGION_REGION_NAME_DUP_PRE_ERROR + localModel.getRegionName() + Constants.ErrorMessages.AH_REGION_REGION_NAME_DUP_POST_ERROR));
					}
				}
			}
		}
		catch(Exception ex)
		{
			logger.error(ex);
		}
		if(logger.isDebugEnabled()){
			logger.debug("RegionActionBean.validate End");
		}
		return validated;
	}
	
	public void setAgentHierarchyManager(AgentHierarchyManager agentHierarchyManager) {
		this.agentHierarchyManager = agentHierarchyManager;
	}

	public RegionModel getRegionModel() {
		return regionModel;
	}

	public void setRegionModel(RegionModel regionModel) {
		this.regionModel = regionModel;
	}

	public List<RegionModel> getRegionModelList() {
		return regionModelList;
	}

	public void setRegionModelList(List<RegionModel> regionModelList) {
		this.regionModelList = regionModelList;
	}

	public List<RegionModel> getDeletedRegionModelList() {
		return deletedRegionModelList;
	}

	public void setDeletedRegionModelList(List<RegionModel> deletedRegionModelList) {
		this.deletedRegionModelList = deletedRegionModelList;
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

	public int getListSize() {
		return listSize;
	}

	public void setListSize(int listSize) {
		this.listSize = listSize;
	}

	public int getRegionNameSortingOrder() {
		return regionNameSortingOrder;
	}

	public void setRegionNameSortingOrder(int regionNameSortingOrder) {
		this.regionNameSortingOrder = regionNameSortingOrder;
	}

	public int getStatusSortingOrder() {
		return statusSortingOrder;
	}

	public void setStatusSortingOrder(int statusSortingOrder) {
		this.statusSortingOrder = statusSortingOrder;
	}
	public RegionalHierarchyModel getRegionalHierarchyModel() {
		return regionalHierarchyModel;
	}

	public void setRegionalHierarchyModel(
			RegionalHierarchyModel regionalHierarchyModel) {
		this.regionalHierarchyModel = regionalHierarchyModel;
	}	
}
