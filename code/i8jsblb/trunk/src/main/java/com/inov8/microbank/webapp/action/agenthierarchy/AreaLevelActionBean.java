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
import javax.faces.model.SelectItem;
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

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.AreaModel;
import com.inov8.microbank.common.model.agenthierarchy.AreaLevelModel;
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


@ManagedBean(name="areaLevelActionBean")
@SessionScoped
public class AreaLevelActionBean {

	private static final Log logger= LogFactory.getLog(AreaLevelActionBean.class);
	@ManagedProperty(value="#{agentHierarchyManager}") 
	private AgentHierarchyManager agentHierarchyManager;	

	private AreaLevelModel areaLevelModel = new AreaLevelModel();
	private RegionalHierarchyModel regionalHierarchyModel;
	
	private List<RegionModel> regionModelList;
	private List<SelectItem> regions;
	private List<SelectItem> parentAreaLevels = new ArrayList<SelectItem>();
	private List<AreaLevelModel> areaLevelModelList = new ArrayList<AreaLevelModel>();
	private Boolean securityCheck= Boolean.FALSE;
	private Boolean editSecurityCheck=Boolean.FALSE;
	private Boolean deleteSecurityCheck=Boolean.FALSE;

	private int UNSORTED = 0;
	private int ASCENDING_ORDER = 1;
	private int DESCENDING_ORDER = 2;
	
	private int regionNameSortingOrder = UNSORTED;
	private int areaLevelNameSortingOrder = UNSORTED;
	private int parentAreaLevelNameSortingOrder = UNSORTED;
	private int statusSortingOrder = UNSORTED;
	
	private int sortingPointer = 0;
	
	private int listSize;
	
	public AreaLevelActionBean() {
		super();
	}


	@PostConstruct
	public void ini()
	{
		if(logger.isDebugEnabled()){
			logger.debug("AreaLevelActionBean.ini Starts");
		}
		try
		{
			this.regionalHierarchyModel = ((RegionalHierarchyActionBean)JSFContext.getBean(Constants.REGIONAL_HIERARCHY_ACTION_BEAN)).getRegionalHierarchyModel();		
			if(this.regionalHierarchyModel.getRegionalHierarchyId() != null)
			{
				SearchBaseWrapper searchBaseWrapper = agentHierarchyManager.findRegionsByRegionalHierarchyId(this.regionalHierarchyModel.getRegionalHierarchyId());
				if(searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0)
				{
					regionModelList = searchBaseWrapper.getCustomList().getResultsetList();
					regions = new ArrayList<SelectItem>();
					for(RegionModel regionModel : regionModelList)
					{
						SelectItem item = new SelectItem();
						item.setLabel(regionModel.getRegionName());
						item.setValue(regionModel.getRegionId());
						this.regions.add(item);
					}
				}
				else
				{
					regions = new ArrayList<SelectItem>();
				}
				
				searchBaseWrapper = agentHierarchyManager.findAreaLevelsByRegionalHierarchyId(this.regionalHierarchyModel.getRegionalHierarchyId());
				if(searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0)
				{
					this.areaLevelModelList = searchBaseWrapper.getCustomList().getResultsetList();
					this.listSize = this.areaLevelModelList.size();
					for(AreaLevelModel model: areaLevelModelList)
					{
						model.setIndex(sortingPointer);
						sortingPointer++;
					}
				}
				
				String createButton=PortalConstants.ADMIN_GP_CREATE+","+PortalConstants.PG_GP_CREATE+","+
                        PortalConstants.MNG_REG_HIER_CREATE;
                securityCheck=AuthenticationUtil.checkRightsIfAny(createButton);
                
                String updateButton=PortalConstants.ADMIN_GP_UPDATE+","+PortalConstants.PG_GP_UPDATE+","+
                        PortalConstants.MNG_REG_HIER_UPDATE;
                editSecurityCheck=AuthenticationUtil.checkRightsIfAny(updateButton);
                
                String deleteButton=PortalConstants.ADMIN_GP_UPDATE+","+PortalConstants.PG_GP_UPDATE+","+
                        PortalConstants.MNG_REG_HIER_UPDATE;
				setDeleteSecurityCheck(AuthenticationUtil.checkRightsIfAny(deleteButton));
			}
			else
			{
				HttpServletResponse response = (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
				HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
				response.sendRedirect(request.getContextPath()+"/home.html");
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
			logger.debug("AreaLevelActionBean.ini End");
		}
	}
	
	public void sortByRegionName(ActionEvent e)
	{
		if(this.regionNameSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.areaLevelModelList, new GenericComparator(Boolean.FALSE));
			this.regionNameSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.regionNameSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.areaLevelModelList, new GenericComparator("index", Boolean.TRUE));
			this.regionNameSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.areaLevelModelList, new GenericComparator("regionName", Boolean.TRUE));
			this.regionNameSortingOrder = this.ASCENDING_ORDER;
		}
		this.statusSortingOrder = UNSORTED;
		this.areaLevelNameSortingOrder = this.UNSORTED;
		this.parentAreaLevelNameSortingOrder = this.UNSORTED;
	}
	
	public void sortByAreaLevelName(ActionEvent e)
	{
		if(this.areaLevelNameSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.areaLevelModelList, new GenericComparator(Boolean.FALSE));
			this.areaLevelNameSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.areaLevelNameSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.areaLevelModelList, new GenericComparator("index", Boolean.TRUE));
			this.areaLevelNameSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.areaLevelModelList, new GenericComparator("areaLevelName", Boolean.TRUE));
			this.areaLevelNameSortingOrder = this.ASCENDING_ORDER;
		}
		this.statusSortingOrder = UNSORTED;
		this.regionNameSortingOrder = this.UNSORTED;
		this.parentAreaLevelNameSortingOrder = this.UNSORTED;
	}
	
	public void sortByParentAreaLevelName(ActionEvent e)
	{
		if(this.parentAreaLevelNameSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.areaLevelModelList, new GenericComparator(Boolean.FALSE));
			this.parentAreaLevelNameSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.parentAreaLevelNameSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.areaLevelModelList, new GenericComparator("index", Boolean.TRUE));
			this.parentAreaLevelNameSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.areaLevelModelList, new GenericComparator("parentAreaLevelName", Boolean.TRUE));
			this.parentAreaLevelNameSortingOrder = this.ASCENDING_ORDER;
		}
		this.statusSortingOrder = UNSORTED;
		this.regionNameSortingOrder = UNSORTED;
		this.areaLevelNameSortingOrder = this.UNSORTED;
	}
	
	public void sortByStatus(ActionEvent e)
	{
		if(this.statusSortingOrder == this.ASCENDING_ORDER)
		{
			this.statusSortingOrder = this.DESCENDING_ORDER;
			Collections.sort(this.areaLevelModelList, new GenericComparator(Boolean.TRUE));
			
		}
		else if(this.statusSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.areaLevelModelList, new GenericComparator("index", Boolean.TRUE));
			this.statusSortingOrder = this.UNSORTED;
		}
		else
		{
			this.statusSortingOrder = this.ASCENDING_ORDER;
			Collections.sort(this.areaLevelModelList, new GenericComparator("active", Boolean.FALSE));
		}
		this.regionNameSortingOrder = this.UNSORTED;
		this.areaLevelNameSortingOrder = this.UNSORTED;
		this.parentAreaLevelNameSortingOrder = this.UNSORTED;
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
	        HSSFSheet sheet = wb.createSheet("Exported Area Levels WorkBook");
	        HSSFCellStyle cellStyle = getCellStyle(wb); 
	        
	        HSSFRow row = sheet.createRow(0);
	        
	        HSSFCell cell = row.createCell(Short.valueOf("0"));
	        cell.setCellValue("Agent Network");
	        cell.setCellStyle(cellStyle);
	        
	        
	        cell = row.createCell(Short.valueOf("1"));
	        cell.setCellValue("Region");
	        cell.setCellStyle(cellStyle);
	        
	        cell = row.createCell(Short.valueOf("2"));
	        cell.setCellValue("Area Level");
	        cell.setCellStyle(cellStyle);
	        
	        cell = row.createCell(Short.valueOf("3"));
	        cell.setCellValue("Parent Area Level");
	        cell.setCellStyle(cellStyle);
	        
	        cell = row.createCell(Short.valueOf("4"));
	        cell.setCellValue("Status");
	        cell.setCellStyle(cellStyle);
	        
	        
	        
	        int rowCounter = 1;
	        for(AreaLevelModel model: this.areaLevelModelList)
	        {
	        	row = sheet.createRow(rowCounter);
	        	rowCounter++;
	        	
	        	cell = row.createCell(Short.valueOf("0"));
	        	RegionalHierarchyActionBean rhab = (RegionalHierarchyActionBean)JSFContext.getBean(Constants.REGIONAL_HIERARCHY_ACTION_BEAN);
	        	cell.setCellValue(rhab.getRegionalHierarchyModel().getRegionalHierarchyName());
		        
		        cell = row.createCell(Short.valueOf("1"));
		        cell.setCellValue(model.getRegion().getRegionName());
		        
		        cell = row.createCell(Short.valueOf("2"));
		        cell.setCellValue(model.getAreaLevelName());
		        
		        if(model.getParentAreaLevelId() != null)
		        {
			        cell = row.createCell(Short.valueOf("3"));
			        for(AreaLevelModel levelModel: this.areaLevelModelList)
			        {
			        	if(levelModel.getAreaLevelId().longValue() == model.getParentAreaLevelId().longValue())
			        	{
			        		cell.setCellValue(levelModel.getAreaLevelName());
			        		break;
			        	}
			        }
		        }
		        
		        cell = row.createCell(Short.valueOf("4"));
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
	        res.setHeader("Content-disposition", "attachment;filename=area levels data.xls");

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
	
	public void editAreaLevel(ActionEvent e)
	{
		if(logger.isDebugEnabled()){
			logger.debug("AreaLevelActionBean.editAreaLevel Starts");
		}
		try
		{
			if(this.areaLevelModel.isEditMode())
			{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(Constants.ErrorMessages.AH_AREALEVEL_SAVE_INLINE_ROW_ERROR));
			}
			else
			{
				int rowIndex = Integer.parseInt((String)JSFContext.getFromRequest("rowIndexValue"));
				AreaLevelModel localModel = this.areaLevelModelList.get(rowIndex);
				localModel.setDirty(Boolean.TRUE);
				localModel.setEditMode(Boolean.TRUE);
				this.areaLevelModel = (AreaLevelModel)localModel.clone();
				if(this.areaLevelModel.getParentAreaLevelId() == null) 		//	If parent area level is null, it means it itself is parent of all area levels.
				{
					for(RegionModel regionModel:this.regionModelList)
					{
						if(regionModel.getRegionId().longValue() == this.areaLevelModel.getRegionId().longValue())
						{
							this.areaLevelModel.setActiveDisabled(!regionModel.getActive());
							break;
						}
					}
					
					/*if(this.areaLevelModel.getRegion().getActive().equals(Boolean.FALSE))
					{
						this.areaLevelModel.setActiveDisabled(Boolean.TRUE);
						this.areaLevelModel.setActive(Boolean.FALSE);
					}
					else
					{
						this.areaLevelModel.setActiveDisabled(Boolean.FALSE);
					}*/
				}
				else
				{
					for(AreaLevelModel levelModel:this.areaLevelModelList)
					{
						if(levelModel.getAreaLevelId().longValue() == this.areaLevelModel.getParentAreaLevelId().longValue())
						{
							this.areaLevelModel.setActiveDisabled(!levelModel.getActive());
							break;
						}
					}
					
					/*if(!this.areaLevelModel.getParentAreaLevel().isActive())
					{
						this.areaLevelModel.setActiveDisabled(Boolean.TRUE);
						this.areaLevelModel.setActive(Boolean.FALSE);
					}
					else
					{
						this.areaLevelModel.setActiveDisabled(Boolean.FALSE);
					}*/
				}
				
				this.parentAreaLevels.clear();
				for(AreaLevelModel model: this.areaLevelModelList)
				{
					if(this.areaLevelModel.getParentAreaLevelId().equals(model.getAreaLevelId()))
					{
						SelectItem item = new SelectItem();
						item.setLabel(model.getAreaLevelName());
						item.setValue(model.getAreaLevelId());
						this.parentAreaLevels.add(item);
					}
				}
				for(AreaLevelModel model: this.areaLevelModelList)
				{
					if(this.areaLevelModel.getRegionId().equals(model.getRegionId()) && this.areaLevelModel.getAreaLevelId().equals(model.getParentAreaLevelId()))
					{
						this.areaLevelModel.setDisabled(Boolean.TRUE);
						break;
					}
				}
			}
		}
		catch(Exception ex)
		{
			logger.error(ex);
		}
		if(logger.isDebugEnabled()){
			logger.debug("AreaLevelActionBean.editAreaLevel End");
		}
	}
	
	public void areaLevelIntegrityCheck(AjaxBehaviorEvent event)
	{
		if(logger.isDebugEnabled()){
			logger.debug("AreaLevelActionBean.areaLevelIntegrityCheck End");
		}
		try
		{
			if(!this.areaLevelModel.getActive() && this.areaLevelModel.getAreaLevelId() != null)
			{
				boolean recordFoundFlag = this.agentHierarchyManager.areaLevelIntegrityCheck(this.areaLevelModel.getAreaLevelId());
				if(recordFoundFlag)
				{
					JSFContext.addErrorMessage(Constants.ErrorMessages.AH_AREALEVE_AREALEVE_INACTIVE_ERROR);
					this.areaLevelModel.setActive(Boolean.TRUE);
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
			logger.debug("AreaLevelActionBean.areaLevelIntegrityCheck End");
		}
	}
	
	public void checkParentalActiveStatus(AjaxBehaviorEvent event)
	{
		if(logger.isDebugEnabled()){
			logger.debug("AreaLevelActionBean.checkParentalActiveStatus Starts");
		}
		AreaLevelModel localModel = null;
		for(AreaLevelModel model :this.areaLevelModelList)
		{
			if(model.getAreaLevelId().equals(this.areaLevelModel.getParentAreaLevelId()))
			{
				localModel = model;
			}
		}
		
		if(localModel != null)
		{
			if(!localModel.getActive())
			{
				this.areaLevelModel.setActiveDisabled(Boolean.TRUE);
				this.areaLevelModel.setActive(Boolean.FALSE);
			}
			else
			{
				this.areaLevelModel.setActiveDisabled(Boolean.FALSE);
				this.areaLevelModel.setActive(Boolean.TRUE);
			}	
		}
		
		if(logger.isDebugEnabled()){
			logger.debug("AreaLevelActionBean.checkParentalActiveStatus End");
		}
	}
	
	private void loadParentAreaLevels()
	{
		this.parentAreaLevels.clear();
		if(this.areaLevelModel.getRegionId() != -1)
		{
			for(AreaLevelModel model: this.areaLevelModelList)
			{
				if(this.areaLevelModel.getRegionId().equals(model.getRegionId()))
				{
					SelectItem item = new SelectItem();
					item.setLabel(model.getAreaLevelName());
					item.setValue(model.getAreaLevelId());
					this.parentAreaLevels.add(item);
				}
			}
		}
		//this.parentLevelListSise = this.parentAreaLevels.size();
	}
	
	
	private void loadParentAreaLevel()
	{
		this.parentAreaLevels.clear();
		if(this.areaLevelModel.getRegionId() != -1)
		{
			for(AreaLevelModel model: this.areaLevelModelList)
			{
				if(this.areaLevelModel.getRegionId().equals(model.getRegionId()))
				{
					boolean childFound = Boolean.FALSE;
					for(AreaLevelModel innerModel: this.areaLevelModelList)
					{
						if(model.getAreaLevelId().equals(innerModel.getParentAreaLevelId()))
						{
							childFound = Boolean.TRUE;
							break;
						}
					}
					if(!childFound)
					{
						SelectItem item = new SelectItem();
						item.setLabel(model.getAreaLevelName());
						item.setValue(model.getAreaLevelId());
						this.parentAreaLevels.add(item);
						break;
					}
				}
			}
		}
		//this.parentLevelListSise = this.parentAreaLevels.size();
	}
	
	public void loadParentAreaLevels(AjaxBehaviorEvent e)
	{
		if(logger.isDebugEnabled()){
			logger.debug("AreaLevelActionBean.loadParentAreaLevels Starts");
		}
		loadParentAreaLevel();
		RegionModel localRegionModel = null;
		for(RegionModel model:this.regionModelList)
		{
			if(this.areaLevelModel.getRegionId().equals(model.getRegionId()))
			{
				localRegionModel = model;
				break;
			}
		}
		
		if(!localRegionModel.getActive())
		{
			this.areaLevelModel.setActiveDisabled(Boolean.TRUE);
			this.areaLevelModel.setActive(Boolean.FALSE);
		}
		else
		{
			this.areaLevelModel.setActiveDisabled(Boolean.FALSE);
			this.areaLevelModel.setActive(Boolean.TRUE);
		}
		
		if(logger.isDebugEnabled()){
			logger.debug("AreaLevelActionBean.loadParentAreaLevels End");
		}
	}
	
	public void resetAreaLevelForm(ActionEvent e)
	{
		if(logger.isDebugEnabled()){
			logger.debug("AreaLevelActionBean.resetAreaLevelForm Starts");
		}
		
		this.areaLevelModel = new AreaLevelModel();
		this.areaLevelModel.setRegionId(-1L);
		this.areaLevelModel.setParentAreaLevelId(-1L);
		this.parentAreaLevels.clear();
		for(AreaLevelModel model: this.areaLevelModelList)
		{
			model.setDirty(Boolean.FALSE);
			model.setEditMode(Boolean.FALSE);
		}
		
		if(logger.isDebugEnabled()){
			logger.debug("AreaLevelActionBean.resetAreaLevelForm End");
		}
	}
	
	public void deleteAreaLevel(ActionEvent event)
	{
		if(logger.isDebugEnabled()){
			logger.debug("AreaLevelActionBean.deleteAreaLevel Starts");
		}
		try
		{
			if(JSFContext.getFromRequest("rowIndexValue") != null && !((String)JSFContext.getFromRequest("rowIndexValue")).equals(""))
			{
				int rowIndex = Integer.parseInt((String)JSFContext.getFromRequest("rowIndexValue"));
				AreaLevelModel localModel = this.areaLevelModelList.get(rowIndex);
				boolean deletable = Boolean.TRUE;
				for(AreaLevelModel model: this.areaLevelModelList)
				{
					if(!model.getAreaLevelId().equals(localModel.getAreaLevelId()) && localModel.getAreaLevelId().equals(model.getParentAreaLevelId()))
					{
						deletable = Boolean.FALSE;
						break;
					}		
				}
				
				if(deletable)
				{
					SearchBaseWrapper searchBaseWrapper = this.agentHierarchyManager.findAreaNamesByAreaLevelId(localModel.getAreaLevelId());
					CustomList<AreaModel> customAreaModelList = searchBaseWrapper.getCustomList();
					if(customAreaModelList.getResultsetList() != null && customAreaModelList.getResultsetList().size() > 0)
					{
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(Constants.ErrorMessages.AH_AREALEVEL_REFERENCE_FOUND_ERROR));
					}
					else
					{
						BaseWrapper baseWrapper = new BaseWrapperImpl();
						baseWrapper.setBasePersistableModel(localModel);
						this.agentHierarchyManager.deleteAreaLevel(baseWrapper);
						JSFContext.setInfoMessage(Constants.InfoMessages.AH_AREALEVEL_DELETED_SUCCESS_INFO);
						logger.info(Constants.InfoMessages.AH_AREALEVEL_DELETED_SUCCESS_INFO);
						this.areaLevelModelList.remove(localModel);
						this.listSize = this.areaLevelModelList.size();
						this.areaLevelModel = new AreaLevelModel();
						this.parentAreaLevels.clear();
					}
				}
				else
				{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(Constants.ErrorMessages.AH_AREALEVEL_REFERENCE_FOUND_ERROR));
				}
			}
		}
		catch(Exception ex)
		{
			logger.error(ex);
		}
		if(logger.isDebugEnabled()){
			logger.debug("AreaLevelActionBean.deleteAreaLevel End");
		}
	}
	
	public void updateAreaLevel(ActionEvent event)
	{
		if(logger.isDebugEnabled()){
			logger.debug("AreaLevelActionBean.updateAreaLevel Starts");
		}
		try
		{
			if(validate())
			{
				BaseWrapper baseWrapper = new BaseWrapperImpl();
				this.areaLevelModel.setUpdatedOn(new Date(new Date().getTime()));
				this.areaLevelModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
				
				this.areaLevelModel.setAreaLevelName(this.areaLevelModel.getAreaLevelName().trim());
				if(this.areaLevelModel.getDescription() != null)
				{
					this.areaLevelModel.setDescription(this.areaLevelModel.getDescription().trim());
				}
				if(this.areaLevelModel.getComments() != null)
				{
					this.areaLevelModel.setComments(this.areaLevelModel.getComments().trim());
				}
				
				
				if(this.areaLevelModel.getParentAreaLevelId() != null && this.areaLevelModel.getParentAreaLevelId() == -1)
				{
					this.areaLevelModel.setParentAreaLevel(null);
				}
				
				for(RegionModel model : this.regionModelList)
				{
					if(this.areaLevelModel.getRegionId().equals(model.getRegionId()))
					{
						this.areaLevelModel.setRegion(model);
						break;
					}
				}
				
				if(!this.areaLevelModel.isDisabled() && this.areaLevelModel.getParentAreaLevelId() != null)
				{
					for(AreaLevelModel model :this.areaLevelModelList)
					{
						if(this.areaLevelModel.getParentAreaLevelId().equals(model.getAreaLevelId()))
						{
							this.areaLevelModel.setParentAreaLevel(model);
							break;
						}
					}
				}
				
				baseWrapper.setBasePersistableModel(this.areaLevelModel);
				agentHierarchyManager.saveOrUpdateAreaLevel(baseWrapper);
				this.areaLevelModel.setDirty(Boolean.FALSE);
				this.areaLevelModel.setEditMode(Boolean.FALSE);
				this.areaLevelModel.setDisabled(Boolean.FALSE);
				JSFContext.setInfoMessage(Constants.InfoMessages.AH_AREALEVEL_UPDATED_SUCCESS_INFO);
				logger.info(Constants.InfoMessages.AH_AREALEVEL_UPDATED_SUCCESS_INFO);
				int index = 0;
				for(AreaLevelModel model:this.areaLevelModelList)
				{
					if(model.isEditMode())
					{
						break;
					}
					index++;
				}
				this.areaLevelModelList.set(index, this.areaLevelModel);
				this.areaLevelModel = new AreaLevelModel();
				this.parentAreaLevels.clear();
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
			logger.debug("AreaLevelActionBean.updateAreaLevel End");
		}
	}
	
	public void saveAreaLevel(ActionEvent event)
	{
		if(logger.isDebugEnabled()){
			logger.debug("AreaLevelActionBean.saveAreaLevel Starts");
		}
		try
		{
			if(validate())
			{
				BaseWrapper baseWrapper = new BaseWrapperImpl();
				this.areaLevelModel.setRegionalHierarchyId(this.regionalHierarchyModel.getRegionalHierarchyId());
				this.areaLevelModel.setUpdatedOn(new Date(new Date().getTime()));
				this.areaLevelModel.setCreatedOn(new Date(new Date().getTime()));
				this.areaLevelModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
				this.areaLevelModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
				this.areaLevelModel.setIndex(this.sortingPointer++);
				this.areaLevelModel.setAreaLevelName(this.areaLevelModel.getAreaLevelName().trim());
				if(this.areaLevelModel.getDescription() != null)
				{
					this.areaLevelModel.setDescription(this.areaLevelModel.getDescription().trim());
				}
				if(this.areaLevelModel.getComments() != null)
				{
					this.areaLevelModel.setComments(this.areaLevelModel.getComments().trim());
				}
					
				
				if(this.areaLevelModel.getUltimateParentAreaLevelId() == null)
				{
					for(AreaLevelModel model :this.areaLevelModelList)
					{
						if(this.areaLevelModel.getRegionId().equals(model.getRegionId()) && model.getUltimateParentAreaLevel() == null)
						{
							this.areaLevelModel.setUltimateParentAreaLevelId(model.getAreaLevelId());
							break;
						}
					}
				}
				
				
				for(RegionModel model : this.regionModelList)
				{
					if(this.areaLevelModel.getRegionId().equals(model.getRegionId()))
					{
						this.areaLevelModel.setRegion(model);
						break;
					}
				}
				
				boolean flag = Boolean.FALSE;
				for(AreaLevelModel model :this.areaLevelModelList)
				{
					if(this.areaLevelModel.getParentAreaLevelId().equals(model.getAreaLevelId()))
					{
						this.areaLevelModel.setParentAreaLevel(model);
						flag = Boolean.TRUE;
						break;
					}
				}
				
				if(!flag)
				{
					this.areaLevelModel.setParentAreaLevelId(null);
				}
				
				baseWrapper.setBasePersistableModel(this.areaLevelModel);
				agentHierarchyManager.saveOrUpdateAreaLevel(baseWrapper);
				JSFContext.setInfoMessage(Constants.InfoMessages.AH_AREALEVEL_SAVED_SUCCESS_INFO);
				logger.info(Constants.InfoMessages.AH_AREALEVEL_SAVED_SUCCESS_INFO);
				this.areaLevelModelList.add(this.areaLevelModel);
				this.listSize = this.areaLevelModelList.size();
				this.parentAreaLevels.clear();
				SelectItem item = new SelectItem();
				item.setValue(this.areaLevelModel.getAreaLevelId());
				item.setLabel(this.areaLevelModel.getAreaLevelName());
				this.parentAreaLevels.add(item);
				this.areaLevelModel = new AreaLevelModel();
				//this.parentLevelListSise = this.parentAreaLevels.size();
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
			logger.debug("AreaLevelActionBean.saveAreaLevel End");
		}
	}
	
	private boolean validate()
	{
		boolean validated = Boolean.TRUE;
		
		if(this.areaLevelModel.getRegionId() == -1)
		{
			validated = Boolean.FALSE;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(Constants.ErrorMessages.AH_AREALEVEL_REGION_NAME_REQ_ERROR));
		}
		
		if(CommonUtils.isEmpty(this.areaLevelModel.getAreaLevelName()))
		{
			validated = Boolean.FALSE;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(Constants.ErrorMessages.AH_AREALEVEL_AREA_LEVEL_NAME_REQ_ERROR));
		}
		
		for(AreaLevelModel model: this.areaLevelModelList)
		{
			if(this.areaLevelModel.isEditMode() && this.areaLevelModel.getUltimateParentAreaLevel() != null && this.areaLevelModel.getRegionId().equals(model.getRegionId()) &&
					!this.areaLevelModel.getAreaLevelId().equals(model.getAreaLevelId()) && 
					this.areaLevelModel.getParentAreaLevelId() != null && this.areaLevelModel.getParentAreaLevelId() == -1)
			{
				validated = Boolean.FALSE;
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(Constants.ErrorMessages.AH_AREALEVEL_PARENT_AREA_LEVEL_REQ_ERROR));
				break;
			}
			else if(!this.areaLevelModel.isEditMode() && this.areaLevelModel.getRegionId().equals(model.getRegionId()) && 
					this.areaLevelModel.getParentAreaLevelId() != null && this.areaLevelModel.getParentAreaLevelId() == -1)
			{
				validated = Boolean.FALSE;
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(Constants.ErrorMessages.AH_AREALEVEL_PARENT_AREA_LEVEL_REQ_ERROR));
				break;
			}
		}
		
		for(AreaLevelModel model: this.areaLevelModelList)
		{
			if(this.areaLevelModel.getAreaLevelName().equals(model.getAreaLevelName()) && this.areaLevelModel.getRegionId().equals(model.getRegionId()) &&
					(this.areaLevelModel.getAreaLevelId() != null && !this.areaLevelModel.getAreaLevelId().equals(model.getAreaLevelId()) ||  
						this.areaLevelModel.getAreaLevelId() == null))
			{
				validated = Boolean.FALSE;
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(Constants.ErrorMessages.AH_AREALEVEL_AREA_LEVEL_NAME_DUP_ERROR));
				break;
			}
		}
		
		return validated;
	}
	
	public String next()
	{
		return Constants.ReturnCodes.AREA_LEVEL_AGENT_LEVEL;
	}
	
	public String previous()
	{
		JSFContext.removeBean(Constants.AREA_LEVEL_ACTION_BEAN);
		return Constants.ReturnCodes.AREA_LEVEL_REGION;
	}
	
	public String cancel()
	{
		SessionBeanObjects.removeAllSessionObjects();
		return Constants.ReturnCodes.CANCEL;
	}
	
	public List<SelectItem> getRegions() {
		return regions;
	}


	public void setRegions(List<SelectItem> regions) {
		this.regions = regions;
	}


	public void setAgentHierarchyManager(AgentHierarchyManager agentHierarchyManager) {
		this.agentHierarchyManager = agentHierarchyManager;
	}

	public AreaLevelModel getAreaLevelModel() {
		return areaLevelModel;
	}

	public void setAreaLevelModel(AreaLevelModel areaLevelModel) {
		this.areaLevelModel = areaLevelModel;
	}


	public List<AreaLevelModel> getAreaLevelModelList() {
		return areaLevelModelList;
	}


	public void setAreaLevelModelList(List<AreaLevelModel> areaLevelModelList) {
		this.areaLevelModelList = areaLevelModelList;
	}


	public List<SelectItem> getParentAreaLevels() {
		return parentAreaLevels;
	}


	public void setParentAreaLevels(List<SelectItem> parentAreaLevels) {
		this.parentAreaLevels = parentAreaLevels;
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


	public int getAreaLevelNameSortingOrder() {
		return areaLevelNameSortingOrder;
	}


	public void setAreaLevelNameSortingOrder(int areaLevelNameSortingOrder) {
		this.areaLevelNameSortingOrder = areaLevelNameSortingOrder;
	}


	public int getParentAreaLevelNameSortingOrder() {
		return parentAreaLevelNameSortingOrder;
	}


	public void setParentAreaLevelNameSortingOrder(
			int parentAreaLevelNameSortingOrder) {
		this.parentAreaLevelNameSortingOrder = parentAreaLevelNameSortingOrder;
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
