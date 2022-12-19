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
import org.springframework.dao.DataIntegrityViolationException;

import com.inov8.framework.common.exception.FrameworkCheckedException;
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

@ManagedBean(name="areaNameActionBean")
@SessionScoped
public class AreaNameActionBean {

	private static final Log logger= LogFactory.getLog(AreaLevelActionBean.class);
	@ManagedProperty(value="#{agentHierarchyManager}") 
	private AgentHierarchyManager agentHierarchyManager;
	
	private RegionalHierarchyModel regionalHierarchyModel;
	private AreaModel areaNameModel;
	
	private List<RegionModel> regionModelList;
	private List<SelectItem> regions;
	
	private List<AreaLevelModel> areaLevelList=new ArrayList<>();
	private List<SelectItem> levelItems= new ArrayList<SelectItem>();
	

	private List<SelectItem> parentAreaNames = new ArrayList<SelectItem>();
	private List<AreaModel> areaNameList = new ArrayList<AreaModel>();
	
	private Boolean areaActiveEnable=Boolean.TRUE;
	private Boolean securityCheck= Boolean.FALSE;
	private Boolean editSecurityCheck=Boolean.FALSE;
	private Boolean deleteSecurityCheck=Boolean.FALSE;
	
	private int UNSORTED = 0;
	private int ASCENDING_ORDER = 1;
	private int DESCENDING_ORDER = 2;
	
	private int regionNameSortingOrder = UNSORTED;
	private int areaLevelNameSortingOrder = UNSORTED;
	private int areaNameSortingOrder = UNSORTED;
	private int parentAreaNameSortingOrder = UNSORTED;
	private int statusSortingOrder = UNSORTED;
	
	private int sortingPointer = 0;
	
	private int listSize;
	
	public AreaNameActionBean() {
		super();
	}

	@PostConstruct
	public void ini()
	{
		if(logger.isDebugEnabled()){
			logger.debug("AreaNameActionBean.ini Starts");
		}
		try
		{
			areaNameModel = new AreaModel();
			this.regionalHierarchyModel = ((RegionalHierarchyActionBean)JSFContext.getBean(Constants.REGIONAL_HIERARCHY_ACTION_BEAN)).getRegionalHierarchyModel();
			if(this.regionalHierarchyModel.getRegionalHierarchyId() != null)
			{
				SearchBaseWrapper searchBaseWrapper = agentHierarchyManager.loadAreaNamesRefData(this.regionalHierarchyModel.getRegionalHierarchyId());
				if(searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0)
				{
					regionModelList = (List<RegionModel>)searchBaseWrapper.getCustomList().getResultsetList().get(0);
					regions = new ArrayList<SelectItem>();
					for(RegionModel regionModel : regionModelList)
					{
						SelectItem item = new SelectItem();
						item.setLabel(regionModel.getRegionName());
						item.setValue(regionModel.getRegionId());
						this.regions.add(item);
					}
					areaNameList=(List<AreaModel>)searchBaseWrapper.getCustomList().getResultsetList().get(1);
					this.listSize = this.areaNameList.size();
					for(AreaModel model: this.areaNameList)
					{
						model.setIndex(sortingPointer);
						sortingPointer++;
					}
				}
				else
				{
					regions = new ArrayList<SelectItem>();
				}
				
				String editUpdateButton=PortalConstants.ADMIN_GP_CREATE+","+PortalConstants.PG_GP_CREATE+","+
                        PortalConstants.MNG_REG_HIER_CREATE;
                securityCheck=AuthenticationUtil.checkRightsIfAny(editUpdateButton);
            }
            else
            {
                HttpServletResponse response = (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
                HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
                response.sendRedirect(request.getContextPath()+"/home.html");
            }
            
            String createButton=PortalConstants.ADMIN_GP_CREATE+","+PortalConstants.PG_GP_CREATE+","+
                    PortalConstants.MNG_REG_HIER_CREATE;
            securityCheck=AuthenticationUtil.checkRightsIfAny(createButton);
            
            String updateButton=PortalConstants.ADMIN_GP_UPDATE+","+PortalConstants.PG_GP_UPDATE+","+
                    PortalConstants.MNG_REG_HIER_UPDATE;
            editSecurityCheck=AuthenticationUtil.checkRightsIfAny(updateButton);
            String deleteButton=PortalConstants.ADM_USR_MGMT_DELETE+","+PortalConstants.PG_GP_UPDATE+","+
                    PortalConstants.MNG_REG_HIER_UPDATE;
			setDeleteSecurityCheck(AuthenticationUtil.checkRightsIfAny(deleteButton));
			
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
			logger.debug("AreaNameActionBean.ini End");
		}
	}
	
	public void sortByRegionName(ActionEvent e)
	{
		if(this.regionNameSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.areaNameList, new GenericComparator(Boolean.FALSE));
			this.regionNameSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.regionNameSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.areaNameList, new GenericComparator("index", Boolean.TRUE));
			this.regionNameSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.areaNameList, new GenericComparator("regionName", Boolean.TRUE));
			this.regionNameSortingOrder = this.ASCENDING_ORDER;
		}
		this.statusSortingOrder = this.UNSORTED;
		this.areaLevelNameSortingOrder = this.UNSORTED;
		this.areaNameSortingOrder = this.UNSORTED;
		this.parentAreaNameSortingOrder = this.UNSORTED;
		
	}
	
	public void sortByAreaLevelName(ActionEvent e)
	{
		if(this.areaLevelNameSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.areaNameList, new GenericComparator(Boolean.FALSE));
			this.areaLevelNameSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.areaLevelNameSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.areaNameList, new GenericComparator("index", Boolean.TRUE));
			this.areaLevelNameSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.areaNameList, new GenericComparator("areaLevelName", Boolean.TRUE));
			this.areaLevelNameSortingOrder = this.ASCENDING_ORDER;
		}
		this.statusSortingOrder = UNSORTED;
		this.regionNameSortingOrder = this.UNSORTED;
		this.areaNameSortingOrder = this.UNSORTED;
		this.parentAreaNameSortingOrder = this.UNSORTED;
	}
	
	public void sortByParentAreaName(ActionEvent e)
	{
		if(this.parentAreaNameSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.areaNameList, new GenericComparator(Boolean.FALSE));
			this.parentAreaNameSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.parentAreaNameSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.areaNameList, new GenericComparator("index", Boolean.TRUE));
			this.parentAreaNameSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.areaNameList, new GenericComparator("parentAreaName", Boolean.TRUE));
			this.parentAreaNameSortingOrder = this.ASCENDING_ORDER;
		}
		this.statusSortingOrder = this.UNSORTED;
		this.regionNameSortingOrder = this.UNSORTED;
		this.areaLevelNameSortingOrder = this.UNSORTED;
		this.areaNameSortingOrder = this.UNSORTED;
	}
	
	public void sortByAreaName(ActionEvent e)
	{
		if(this.areaNameSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.areaNameList, new GenericComparator(Boolean.FALSE));
			this.areaNameSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.areaNameSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.areaNameList, new GenericComparator("index", Boolean.TRUE));
			this.areaNameSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.areaNameList, new GenericComparator("name", Boolean.TRUE));
			this.areaNameSortingOrder = this.ASCENDING_ORDER;
		}
		this.statusSortingOrder = UNSORTED;
		this.regionNameSortingOrder = UNSORTED;
		this.areaLevelNameSortingOrder = this.UNSORTED;
		this.parentAreaNameSortingOrder = this.UNSORTED;
	}
	
	public void sortByStatus(ActionEvent e)
	{
		if(this.statusSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.areaNameList, new GenericComparator(Boolean.TRUE));
			this.statusSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.statusSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.areaNameList, new GenericComparator("index", Boolean.TRUE));
			this.statusSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.areaNameList, new GenericComparator("active", Boolean.FALSE));
			this.statusSortingOrder = this.ASCENDING_ORDER;
		}
		this.regionNameSortingOrder = this.UNSORTED;
		this.areaLevelNameSortingOrder = this.UNSORTED;
		this.parentAreaNameSortingOrder = this.UNSORTED;
		this.areaNameSortingOrder = this.UNSORTED;
	}
	
	public void loadAreaLevels(AjaxBehaviorEvent event){
		if(logger.isDebugEnabled()){
			logger.debug("AreaNameActionBean.loadAreaLevels Start");
		}
		this.areaLevelList= new ArrayList<AreaLevelModel>();
		this.levelItems= new ArrayList<SelectItem>();
		try {
			SearchBaseWrapper searchBaseWrapper=agentHierarchyManager.loadAreaLevelByRegion(this.areaNameModel.getRegionModelId());
			if(null!=searchBaseWrapper.getCustomList() && searchBaseWrapper.getCustomList().getResultsetList().size()>0 ){
				this.areaLevelList=searchBaseWrapper.getCustomList().getResultsetList();
			}
			for(AreaLevelModel levelModel:areaLevelList){
				this.levelItems.add(new SelectItem(levelModel.getAreaLevelId(), levelModel.getAreaLevelName()));
			}
			for(RegionModel regionModel:this.regionModelList)
			{
				if(regionModel.getRegionId().equals(this.areaNameModel.getRegionModelId()))
				{
					this.areaActiveEnable=regionModel.getActive();
					this.areaNameModel.setActive(regionModel.getActive());
					break;
				}
			}
			
		} catch (FrameworkCheckedException e) {
			logger.error(e);
		}
		if(logger.isDebugEnabled()){
			logger.debug("AreaNameActionBean.loadAreaLevels End");
		}
		
	}
	
	public void saveAreaName(){
		if(logger.isDebugEnabled()){
			logger.debug("AreaNameActionBean.saveAreaName Start");
		}
		if(validate()){
			if(!this.areaNameModel.isEditMode()){
				this.areaNameModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
				this.areaNameModel.setCreatedOn(new Date());
				this.areaNameModel.setIndex(sortingPointer++);
			}	
			this.areaNameModel.setRegionalHierarchyId(this.regionalHierarchyModel.getRegionalHierarchyId());
			this.areaNameModel.setRegionalHierarchyModel(this.regionalHierarchyModel);
			this.areaNameModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
			this.areaNameModel.setUpdatedOn(new Date());
			
			this.areaNameModel.setName(this.areaNameModel.getName().trim());
			if(this.areaNameModel.getDescription() != null)
			{
				this.areaNameModel.setDescription(this.areaNameModel.getDescription());
			}
			if(this.areaNameModel.getComments() != null)
			{
				this.areaNameModel.setComments(this.areaNameModel.getComments().trim());
			}
			
			
			if(this.areaNameModel.getParentAreaId()<1){
				this.areaNameModel.setRelationParentAreaIdAreaModel(null);
			}else{
				for(AreaModel areaModel:areaNameList){
					if(this.areaNameModel.getParentAreaId().longValue()==areaModel.getAreaId().longValue()){
						this.areaNameModel.setRelationParentAreaIdAreaModel(areaModel);
					}
				}
			}
			
			if(this.areaNameModel.getParentAreaId() == null)
			{
				this.areaNameModel.setUltimateParentAreaId(null);
			}
			else 
			{
				for(AreaModel areaModel:areaNameList)
				{
					
					if(this.areaNameModel.getParentAreaId().equals(areaModel.getAreaId()) && areaModel.getParentAreaId() != null)
					{
						this.areaNameModel.setUltimateParentAreaId(areaModel.getUltimateParentAreaId());
						break;
					}
					else if(this.areaNameModel.getParentAreaId().equals(areaModel.getAreaId()) && areaModel.getParentAreaId() == null)
					{
						this.areaNameModel.setUltimateParentAreaId(areaModel.getAreaId());
						break;
					}
				}	
			}	
				
			
			for(RegionModel region:regionModelList){
				if(region.getRegionId().longValue()==this.areaNameModel.getRegionModelId().longValue()){
					this.areaNameModel.setRegionModel(region);
				}
			}
			for(AreaLevelModel levelModel:areaLevelList){
				if(levelModel.getAreaLevelId().longValue()==this.areaNameModel.getAreaLevelModelId().longValue()){
					this.areaNameModel.setAreaLevelModel(levelModel);
				}
			}
			try {				
				BaseWrapper baseWrbapper= new BaseWrapperImpl();
				baseWrbapper.setBasePersistableModel(this.areaNameModel);			
				agentHierarchyManager.saveOrUpdateAreaName(baseWrbapper);
				JSFContext.setInfoMessage(Constants.InfoMessages.AH_AREANAME_SAVE_SUCCESS_INFO);
				if(!areaNameModel.isEditMode()){					
					this.parentAreaNames.add(new SelectItem(((AreaModel)baseWrbapper.getBasePersistableModel()).getAreaId(),
						((AreaModel)baseWrbapper.getBasePersistableModel()).getName()));
				}
				if(this.areaNameModel.isEditMode()){
					for(int i=0;i<areaNameList.size();i++){
						AreaModel model= areaNameList.get(i);
						if(model.getAreaId().longValue()==this.areaNameModel.getAreaId().longValue()){
							this.areaNameList.remove(i);
							this.areaNameModel.setEditMode(Boolean.FALSE);
							this.areaNameList.add(i, this.areaNameModel);
							this.listSize = this.areaNameList.size();
							break;
						}
					}
				}else{
					this.areaNameList.add((AreaModel)baseWrbapper.getBasePersistableModel());
					this.listSize = this.areaNameList.size();
				}
				
				this.resetAreaNameForm();				
			} catch (FrameworkCheckedException e) {
				logger.error(e);
			}
		}
		if(logger.isDebugEnabled()){
			logger.debug("AreaNameActionBean.saveAreaName End");
		}
		
	}
	public Boolean validate(){
		Boolean validate=Boolean.TRUE;
		if(CommonUtils.isEmpty(this.areaNameModel.getName())){
			validate=Boolean.FALSE;
			JSFContext.addErrorMessage(Constants.ErrorMessages.AH_AREANAME_AREANAME_ERROR);
		}
		if(this.areaNameModel.getRegionModelId()<1){
			validate=Boolean.FALSE;
			JSFContext.addErrorMessage(Constants.ErrorMessages.AH_AREANAME_REGION_ERROR);
		}
		if(this.areaNameModel.getAreaLevelModelId()<1){
			validate=Boolean.FALSE;
			JSFContext.addErrorMessage(Constants.ErrorMessages.AH_AREANAME_AREALEVELNAME_ERROR);
		}
		for(AreaLevelModel levelModel:areaLevelList){
			if(levelModel.getAreaLevelId().longValue()==this.areaNameModel.getAreaLevelModelId().longValue()){
				this.areaNameModel.setAreaLevelModel(levelModel);
			}
		}
		Boolean isParentExist=Boolean.TRUE;
		if(null!=this.areaNameModel.getAreaLevelModel().getParentAreaLevel()){
			isParentExist=Boolean.FALSE;
			for( AreaModel model:areaNameList){
				if(this.areaNameModel.getAreaLevelModel().getParentAreaLevelId().longValue()==model.getAreaLevelModelId().longValue()){
					isParentExist=Boolean.TRUE;
					break;
				}
			}
		}
		if(!isParentExist){
			JSFContext.addErrorMessage(Constants.ErrorMessages.AH_AREANAME_PARENT_EXIST_ERROR);
			validate= Boolean.FALSE;
		}
		if(null!=this.areaNameModel.getAreaLevelModel().getParentAreaLevel() && this.areaNameModel.getParentAreaId()<1){
			JSFContext.addErrorMessage("Parent area name is required.");
			validate= Boolean.FALSE;
		}
		/*if(this.areaNameModel.getParentAreaId()<1){
			validate=Boolean.FALSE;
		}*/
		return validate;
	}
	public void updateAreaName(AreaModel model){
		if(logger.isDebugEnabled()){
			logger.debug("AreaNameActionBean.updateAreaName Start");
		}
			model.setEditMode(Boolean.TRUE);
			this.areaNameModel.setEditMode(Boolean.TRUE);			
			this.areaNameModel.setAreaId(model.getAreaId());
			this.areaNameModel.setName(model.getName());
			this.areaNameModel.setComments(model.getComments());
			this.areaNameModel.setDescription(model.getDescription());
			this.areaNameModel.setCreatedBy(model.getCreatedBy());
			this.areaNameModel.setCreatedOn(model.getCreatedOn());
			this.areaNameModel.setRegionModel(model.getRegionModel());
			this.areaNameModel.setAreaLevelModel(model.getAreaLevelModel());
			this.areaNameModel.setParentAreaIdAreaModel(model.getParentAreaIdAreaModel());
			this.areaNameModel.setVersionNo(model.getVersionNo());
			this.levelItems.add(new SelectItem(model.getAreaLevelModel().getAreaLevelId(), model.getAreaLevelModel().getAreaLevelName()));			
			loadParentNames();
			
			if(model.getParentAreaId() == null)
			{
				for(AreaLevelModel areaLevelModel :this.areaLevelList)
				{
					if(areaLevelModel.getAreaLevelId().longValue() == model.getAreaLevelModelId().longValue())
					{
						this.areaActiveEnable = areaLevelModel.getActive();
						break;
					}
				}
			}
			else
			{
				for(AreaModel areaModel:this.areaNameList)
				{
					if(areaModel.getAreaId().longValue() == model.getParentAreaId().longValue())
					{
						this.areaActiveEnable = areaModel.isActive();
						break;
					}
				}
			}
			
			this.areaNameModel.setActive(model.isActive());
			if(isRefeerenceExist(this.areaNameModel))
				this.areaNameModel.setDisabled(Boolean.TRUE);
		if(logger.isDebugEnabled()){
			logger.debug("AreaNameActionBean.updateAreaName End");
		}
		
	}
	public void deleteAreaName(AreaModel model){
		if(logger.isDebugEnabled()){
			logger.debug("AreaNameActionBean.deleteAreaName Start");
		}
		if(!isRefeerenceExist(model)){
			try {
				BaseWrapper baseWrapper= new BaseWrapperImpl();
				baseWrapper.setBasePersistableModel(model);		
				agentHierarchyManager.deleteAreaName(baseWrapper);
				for( AreaModel areaModel:areaNameList){
					if(areaModel.getAreaId().longValue()==model.getAreaId().longValue()){
						this.areaNameList.remove(areaModel);
						this.listSize = this.areaNameList.size();
						break;
					}
				}
				for(SelectItem item:parentAreaNames){
					if(model.getAreaId()== item.getValue()){
						parentAreaNames.remove(item);
						break;
					}
				}
				this.resetAreaNameForm();
				JSFContext.setInfoMessage(Constants.InfoMessages.AH_AREANAME_DELETE_SUCCESS_INFO);
			} catch (FrameworkCheckedException e) {
				// TODO Auto-generated catch block
				logger.error(e);
			}catch (Exception e) {
				// TODO: handle exception
				if(e instanceof DataIntegrityViolationException){
					JSFContext.addErrorMessage(Constants.ErrorMessages.AH_AREANAME_REF_EXIST_ERROR);
				}
				logger.error(e);
			}	
		}
		else{
			JSFContext.addErrorMessage(Constants.ErrorMessages.AH_AREANAME_AREANAMEEXIST_ERROR);
		}
		if(logger.isDebugEnabled()){
			logger.debug("AreaNameActionBean.deleteAreaName End");
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
	        HSSFSheet sheet = wb.createSheet("Exported Area Names WorkBook");
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
	        cell.setCellValue("Area Name");
	        cell.setCellStyle(cellStyle);
	        
	        cell = row.createCell(Short.valueOf("4"));
	        cell.setCellValue("Parent Area Name");
	        cell.setCellStyle(cellStyle);
	        
	        cell = row.createCell(Short.valueOf("5"));
	        cell.setCellValue("Status");
	        cell.setCellStyle(cellStyle);
	        
	        int rowCounter = 1;
	        for(AreaModel model: this.areaNameList)
	        {
	        	row = sheet.createRow(rowCounter);
	        	rowCounter++;
	        	
	        	cell = row.createCell(Short.valueOf("0"));
	        	RegionalHierarchyActionBean rhab = (RegionalHierarchyActionBean)JSFContext.getBean(Constants.REGIONAL_HIERARCHY_ACTION_BEAN);
	        	cell.setCellValue(rhab.getRegionalHierarchyModel().getRegionalHierarchyName());
		        
		        cell = row.createCell(Short.valueOf("1"));
		        cell.setCellValue(model.getRegionModel().getRegionName());
		        
		        cell = row.createCell(Short.valueOf("2"));
		        cell.setCellValue(model.getAreaLevelModel().getAreaLevelName());
		        
		        cell = row.createCell(Short.valueOf("3"));
		        cell.setCellValue(model.getName());
		        
		        if(model.getRelationParentAreaIdAreaModel() != null)
		        {
			        cell = row.createCell(Short.valueOf("4"));
			        cell.setCellValue(model.getRelationParentAreaIdAreaModel().getName());
		        }
		        
		        cell = row.createCell(Short.valueOf("5"));
		        if(model.isActive())
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
	
	public Boolean isRefeerenceExist(AreaModel model){
		for(AreaModel areaModel:areaNameList){
			if(null!=areaModel.getParentAreaId() && (model.getAreaId().longValue()==areaModel.getParentAreaId()))
				return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	public void resetAreaNameForm(){
		if(logger.isDebugEnabled()){
			logger.debug("AreaNameActionBean.clearForm Start");
		}
			this.areaNameModel= new AreaModel();
			levelItems= new ArrayList<SelectItem>();
			parentAreaNames= new ArrayList<SelectItem>();
			areaActiveEnable= Boolean.TRUE;
		if(logger.isDebugEnabled()){
			logger.debug("AreaNameActionBean.clearForm End");
		}
	}
	
	public void loadParentNames(AjaxBehaviorEvent event){
		if(logger.isDebugEnabled()){
			logger.debug("AreaNameActionBean.loadParentNames Start");
		}		
		loadParentNames();
		if(logger.isDebugEnabled()){
			logger.debug("AreaNameActionBean.loadParentNames End");
		}
	}
	
	
	private void loadParentNames(){
		
		parentAreaNames= new ArrayList<SelectItem>();
		
		for(AreaLevelModel levelModel:areaLevelList){
			if(this.areaNameModel.getAreaLevelModelId().longValue()==levelModel.getAreaLevelId()){
				this.areaNameModel.setAreaLevelModel(levelModel);
				break;
			}
		}
		
		for(AreaModel model:areaNameList){
			if( null!=this.areaNameModel.getAreaLevelModel().getParentAreaLevel() && null!=model.getAreaLevelModel().getParentAreaLevel()){
				if(!this.areaNameModel.isEditMode() && model.getAreaLevelModelId().longValue()==this.areaNameModel.getAreaLevelModel().getParentAreaLevelId().longValue())
					this.parentAreaNames.add(new SelectItem(model.getAreaId(),model.getName()));
				else if(this.areaNameModel.isEditMode() &&
						model.getAreaLevelModelId().longValue()==this.areaNameModel.getAreaLevelModel().getParentAreaLevelId().longValue())
					this.parentAreaNames.add(new SelectItem(model.getAreaId(),model.getName()));
				this.areaActiveEnable=this.areaNameModel.getAreaLevelModel().getActive();
				this.areaNameModel.setActive(model.isActive());
			}/*else if( null!=this.areaNameModel.getAreaLevelModel().getParentAreaLevel() && null!=model.getAreaLevelModel().getParentAreaLevel() &&
					model.getAreaLevelModel().getParentAreaLevelId().longValue()==this.areaNameModel.getAreaLevelModel().getParentAreaLevelId().longValue()){
				if(!this.areaNameModel.isEditMode())
					this.parentAreaNames.add(new SelectItem(model.getAreaId(),model.getName()));
				this.areaActiveEnable=this.areaNameModel.getAreaLevelModel().isActive();
				this.areaNameModel.setActive(model.isActive());
			}*/
			else if(null!=this.areaNameModel.getAreaLevelModel().getParentAreaLevel() && null== model.getAreaLevelModel().getParentAreaLevel() &&
					model.getAreaLevelModelId().longValue()==this.areaNameModel.getAreaLevelModel().getParentAreaLevelId().longValue()){
				//if(!this.areaNameModel.isEditMode() && null!=model.getParentAreaId() )
					this.parentAreaNames.add(new SelectItem(model.getAreaId(),model.getName()));
					this.areaNameModel.setActive(model.isActive());
					this.areaActiveEnable=this.areaNameModel.getAreaLevelModel().getActive();
			}else if(null==this.areaNameModel.getAreaLevelModel().getParentAreaLevel() && null== model.getAreaLevelModel().getParentAreaLevel() 
					&& model.getAreaLevelModelId().longValue()==this.areaNameModel.getAreaLevelModelId().longValue() ){
				
			    if(this.areaNameModel.isEditMode() && this.areaNameModel.getAreaId().longValue()!=model.getAreaId().longValue() ){
			    	if( null!=this.areaNameModel.getParentAreaIdAreaModel() && this.areaNameModel.getParentAreaId()>1)
			    		this.parentAreaNames.add(new SelectItem(model.getAreaId(),model.getName()));
			    }
				else if(!this.areaNameModel.isEditMode() && null!=this.areaNameModel.getAreaLevelModel().getParentAreaLevel())
					this.parentAreaNames.add(new SelectItem(model.getAreaId(),model.getName()));
					this.areaNameModel.setActive(model.isActive());
					this.areaActiveEnable=this.areaNameModel.getAreaLevelModel().getActive();
			}
		}
		this.areaNameModel.setActive(this.areaNameModel.getAreaLevelModel().getActive());
		this.areaActiveEnable=this.areaNameModel.getAreaLevelModel().getActive();
	}
	
	public void checkParentalActiveStatus(AjaxBehaviorEvent event){
		
		for(AreaLevelModel areaLevelModel:this.areaLevelList)
		{
			if(areaLevelModel.getAreaLevelId().longValue() == this.areaNameModel.getAreaLevelModelId().longValue())
			{
				if(areaLevelModel.getActive())
				{
					for(AreaModel model:areaNameList)
					{
						if(model.getAreaId().longValue()==this.areaNameModel.getParentAreaId().longValue())
						{
							this.areaNameModel.setActive(model.isActive());
							areaActiveEnable=model.isActive();
							break;
						}
					}
				}
				break;
			}
		}
		
		
	}
	/*public void checkChildStatus(){
		if(this.areaNameModel.isEditMode()){
			for(AreaModel model:areaNameList){
				if(null!=model.getRelationParentAreaIdAreaModel() && model.getParentAreaId().longValue()==this.areaNameModel.getAreaId().longValue()){
					this.areaNameModel.setActive(model.isActive());
				}
			}
		}
	}*/
	public String previous()
	{
		JSFContext.removeBean(Constants.AREA_NAME_ACTION_BEAN);
		return Constants.ReturnCodes.AREA_NAME_AGENT_LEVEL;
	}
	
	public String cancel()
	{
		SessionBeanObjects.removeAllSessionObjects();
		return Constants.ReturnCodes.CANCEL;
	}
	
	public String finish()
	{
		SessionBeanObjects.removeAllSessionObjects();
		return Constants.ReturnCodes.AREA_NAME_FINISH;
		
	}
	public void onChangeActive(AjaxBehaviorEvent event){
		 if(logger.isDebugEnabled()){
				logger.debug("AreaNameActionBean.onChangeActive Start");
		 }		 
		 if(this.areaNameModel.isEditMode() && !this.areaNameModel.isActive())
		 {
			 try {
				 Boolean isExist=agentHierarchyManager.isAreaNameRefExist(this.areaNameModel.getAreaId()); // TODO make sure there are active agents associated with area name.
				 if(isExist){
					 this.areaNameModel.setActive(isExist);
					 JSFContext.addErrorMessage("Area Name cannot be in-active due to reference data found in the system.");
				 }
				 else
				 {
					 for(AreaModel areaModel :this.areaNameList)
					 {
						 if(areaModel.getParentAreaId() != null && this.areaNameModel.getAreaId().longValue() == areaModel.getParentAreaId().longValue() && areaModel.isActive())
						 {
							 JSFContext.addErrorMessage("Area Name cannot be in-active due to reference data found in the system.");
							 this.areaNameModel.setActive(Boolean.TRUE);
							 break;
						 }
					 }
				 }
			} catch (FrameworkCheckedException e) {
				// TODO Auto-generated catch block
				logger.error(e);
			}
		 }
		 if(logger.isDebugEnabled()){
				logger.debug("AreaNameActionBean.onChangeActive End");
		 }
		
	}
	
	public void setAgentHierarchyManager(AgentHierarchyManager agentHierarchyManager) {
		this.agentHierarchyManager = agentHierarchyManager;
	}
	
	public AreaModel getAreaNameModel() {
		return areaNameModel;
	}

	public void setAreaNameModel(AreaModel areaNameModel) {
		this.areaNameModel = areaNameModel;
	}

	public List<SelectItem> getRegions() {
		return regions;
	}

	public void setRegions(List<SelectItem> regions) {
		this.regions = regions;
	}

	public List<SelectItem> getParentAreaNames() {
		return parentAreaNames;
	}

	public void setParentAreaNames(List<SelectItem> parentAreaNames) {
		this.parentAreaNames = parentAreaNames;
	}

	public List<AreaModel> getAreaNameList() {
		return areaNameList;
	}

	public void setAreaNameList(List<AreaModel> areaNameList) {
		this.areaNameList = areaNameList;
	}
	
	public List<AreaLevelModel> getAreaLevelList() {
		return areaLevelList;
	}

	public void setAreaLevelList(List<AreaLevelModel> areaLevelList) {
		this.areaLevelList = areaLevelList;
	}

	public List<SelectItem> getLevelItems() {
		return levelItems;
	}

	public void setLevelItems(List<SelectItem> levelItems) {
		this.levelItems = levelItems;
	}
	public Boolean getAreaActiveEnable() {
		return areaActiveEnable;
	}

	public void setAreaActiveEnable(Boolean areaActiveEnable) {
		this.areaActiveEnable = areaActiveEnable;
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

	public int getAreaNameSortingOrder() {
		return areaNameSortingOrder;
	}

	public void setAreaNameSortingOrder(int areaNameSortingOrder) {
		this.areaNameSortingOrder = areaNameSortingOrder;
	}

	public int getParentAreaNameSortingOrder() {
		return parentAreaNameSortingOrder;
	}

	public void setParentAreaNameSortingOrder(int parentAreaNameSortingOrder) {
		this.parentAreaNameSortingOrder = parentAreaNameSortingOrder;
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
