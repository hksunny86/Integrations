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
import com.inov8.microbank.common.model.DistributorLevelModel;
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

@ManagedBean(name="agentLevelBean")
@SessionScoped
public class DistributorLevelActionBean {
	private static final Log logger= LogFactory.getLog(DistributorLevelActionBean.class);
	
	@ManagedProperty(value="#{agentHierarchyManager}") 
	private AgentHierarchyManager agentHierarchyManager;
	private DistributorLevelModel levelModel= new DistributorLevelModel();	
	private List<DistributorLevelModel> leveList;
	
	private List<SelectItem>  regionItems= new ArrayList<SelectItem>();
	private List<SelectItem>  levelItems= new ArrayList<SelectItem>();		
	private List<RegionModel>  regionList= new ArrayList<RegionModel>();
	
	private List<DistributorLevelModel>  ultimateLevelList = new ArrayList<DistributorLevelModel>();
	private List<SelectItem>  ultimateLevelItems= new ArrayList<SelectItem>();
	
	private RegionalHierarchyModel regionalHierarchyModel;
	
	private Boolean parentLevelEnable=Boolean.TRUE;
	private Boolean regionalHierarchyActive=Boolean.TRUE;
	private Boolean securityCheck= Boolean.FALSE;
	private Boolean editSecurityCheck=Boolean.FALSE;
	private Boolean deleteSecurityCheck=Boolean.FALSE;
	private int listSize;
	
	private int UNSORTED = 0;
	private int ASCENDING_ORDER = 1;
	private int DESCENDING_ORDER = 2;
	
	private int regionNameSortingOrder = UNSORTED;
	private int agentLevelNameSortingOrder = UNSORTED;
	private int parentAgentLevelNameSortingOrder = UNSORTED;
	private int statusSortingOrder = UNSORTED;

	private int sortingPointer = 0;
	
	public DistributorLevelActionBean(){
		super();
	}
	
	@PostConstruct
	public void init(){
		if(logger.isDebugEnabled())
		{
			logger.debug("DitributorLevelActionBean.init Starts");
		}
		try
		{
			leveList= new ArrayList<DistributorLevelModel>();
			this.listSize = this.leveList.size();
			this.regionalHierarchyModel = ((RegionalHierarchyActionBean)JSFContext.getBean(Constants.REGIONAL_HIERARCHY_ACTION_BEAN)).getRegionalHierarchyModel();
			if(this.regionalHierarchyModel.getRegionalHierarchyId() != null)
			{
				SearchBaseWrapper searchBaseWrapper = agentHierarchyManager.loadDistributorLevelRefData(regionalHierarchyModel.getRegionalHierarchyId());
				if(searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0)
				{
					List<Object> result=searchBaseWrapper.getCustomList().getResultsetList();
					regionList=(List<RegionModel>)result.get(0);
					for(RegionModel model:regionList){
						this.regionItems.add(new SelectItem(model.getRegionId(), model.getRegionName())) ;
					}	
					leveList=(List<DistributorLevelModel>)result.get(1);
					this.listSize = this.leveList.size();
					for(DistributorLevelModel model: leveList)
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
		catch(FrameworkCheckedException ex)
		{
			logger.error(ex);
		}
		catch(Exception ex)
		{
			logger.error(ex);
		}
		if(logger.isDebugEnabled()){
			logger.debug("DitributorLevelActionBean.init Ends");
		}		
	}

	public void sortByRegionName(ActionEvent e)
	{
		if(this.regionNameSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.leveList, new GenericComparator(Boolean.FALSE));
			this.regionNameSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.regionNameSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.leveList, new GenericComparator("index", Boolean.TRUE));
			this.regionNameSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.leveList, new GenericComparator("regionName", Boolean.TRUE));
			this.regionNameSortingOrder = this.ASCENDING_ORDER;
		}
		this.statusSortingOrder = UNSORTED;
		this.agentLevelNameSortingOrder = this.UNSORTED;
		this.parentAgentLevelNameSortingOrder = this.UNSORTED;
	}
	
	public void sortByAgentLevelName(ActionEvent e)
	{
		if(this.agentLevelNameSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.leveList, new GenericComparator(Boolean.FALSE));
			this.agentLevelNameSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.agentLevelNameSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.leveList, new GenericComparator("index", Boolean.TRUE));
			this.agentLevelNameSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.leveList, new GenericComparator("name", Boolean.TRUE));
			this.agentLevelNameSortingOrder = this.ASCENDING_ORDER;
		}
		this.statusSortingOrder = UNSORTED;
		this.regionNameSortingOrder = this.UNSORTED;
		this.parentAgentLevelNameSortingOrder = this.UNSORTED;
	}
	
	public void sortByParentAgentLevelName(ActionEvent e)
	{
		if(this.parentAgentLevelNameSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.leveList, new GenericComparator(Boolean.FALSE));
			this.parentAgentLevelNameSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.parentAgentLevelNameSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.leveList, new GenericComparator("index", Boolean.TRUE));
			this.parentAgentLevelNameSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.leveList, new GenericComparator("parentAgentLevelName", Boolean.TRUE));
			this.parentAgentLevelNameSortingOrder = this.ASCENDING_ORDER;
		}
		this.statusSortingOrder = UNSORTED;
		this.regionNameSortingOrder = UNSORTED;
		this.agentLevelNameSortingOrder = this.UNSORTED;
	}
	
	public void sortByStatus(ActionEvent e)
	{
		if(this.statusSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.leveList, new GenericComparator(Boolean.TRUE));
			this.statusSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.statusSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.leveList, new GenericComparator("index", Boolean.TRUE));
			this.statusSortingOrder = this.UNSORTED;
		}
		else
		{
			
			Collections.sort(this.leveList, new GenericComparator("active", Boolean.FALSE));
			this.statusSortingOrder = this.ASCENDING_ORDER;
		}
		this.regionNameSortingOrder = this.UNSORTED;
		this.agentLevelNameSortingOrder = this.UNSORTED;
		this.parentAgentLevelNameSortingOrder = this.UNSORTED;
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
	        HSSFSheet sheet = wb.createSheet("Exported Agent Levels WorkBook");
	        HSSFCellStyle cellStyle = getCellStyle(wb); 
	        
	        HSSFRow row = sheet.createRow(0);
	        
	        HSSFCell cell = row.createCell(Short.valueOf("0"));
	        cell.setCellValue("Agent Network");
	        cell.setCellStyle(cellStyle);
	        
	        
	        cell = row.createCell(Short.valueOf("1"));
	        cell.setCellValue("Region");
	        cell.setCellStyle(cellStyle);
	        
	        cell = row.createCell(Short.valueOf("2"));
	        cell.setCellValue("Agent Level");
	        cell.setCellStyle(cellStyle);
	        
	        cell = row.createCell(Short.valueOf("3"));
	        cell.setCellValue("Parent Agent Level");
	        cell.setCellStyle(cellStyle);
	        
	        cell = row.createCell(Short.valueOf("4"));
	        cell.setCellValue("Status");
	        cell.setCellStyle(cellStyle);
	        
	        int rowCounter = 1;
	        for(DistributorLevelModel model: this.leveList)
	        {
	        	row = sheet.createRow(rowCounter);
	        	rowCounter++;
	        	
	        	cell = row.createCell(Short.valueOf("0"));
	        	RegionalHierarchyActionBean rhab = (RegionalHierarchyActionBean)JSFContext.getBean(Constants.REGIONAL_HIERARCHY_ACTION_BEAN);	        	
	        	cell.setCellValue(rhab.getRegionalHierarchyModel().getRegionalHierarchyName());
		        
		        cell = row.createCell(Short.valueOf("1"));
		        cell.setCellValue(model.getRegionModel().getRegionName());
		        
		        cell = row.createCell(Short.valueOf("2"));
		        cell.setCellValue(model.getName());

		        if(model.getRelationManagingLevelIdDistributorLevelModel() != null)
		        {
		        	cell = row.createCell(Short.valueOf("3"));
		        	cell.setCellValue(model.getRelationManagingLevelIdDistributorLevelModel().getName());
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
	
	public String cancel()
	{
		if(logger.isDebugEnabled()){
			logger.debug("DitributorLevelActionBean.cancel Starts");
		}
		SessionBeanObjects.removeAllSessionObjects();
		if(logger.isDebugEnabled()){
			logger.debug("DitributorLevelActionBean.cancel End");
		}
		return Constants.ReturnCodes.CANCEL;
	}
	
	public String previous()
	{
		JSFContext.removeBean(Constants.DISTRIBUTORLEVEL_ACTION_BEAN);
		return Constants.ReturnCodes.AGENT_LEVEL_AREA_LEVEL;
	}
	
	public String next()
	{
		return Constants.ReturnCodes.AGENT_LEVEL_AREA_NAME;
	}
	
	public void addDistributorLevels(){
		if(logger.isDebugEnabled()){
			logger.debug("DitributorLevelActionBean.addDistributorLevels Starts");
		}
		
		if(this.levelModel.getUltimateManagingLevelId() != null && this.levelModel.getUltimateManagingLevelId() == 0){
			this.levelModel.setUltimateManagingLevelId(null);
			this.levelModel.setUltimateManagingLevelIdDistributorLevelModel(null);
		}
		
		if(validate(this.levelModel)){
			if(!this.levelModel.isEditMode()){
				this.levelModel.setCreatedOn(new Date());
				this.levelModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
				this.levelModel.setIndex(this.sortingPointer++);
				
				this.levelModel.setName(this.levelModel.getName().trim());
				if(this.levelModel.getDescription() != null)
				{
					this.levelModel.setDescription(this.levelModel.getDescription().trim());
				}
				if(this.levelModel.getComments() != null)
				{
					this.levelModel.setComments(this.levelModel.getComments().trim());
				}
				
				for(RegionModel region:regionList){
					if(region.getRegionId().longValue()==this.levelModel.getRegionModel().getRegionId().longValue()){
						this.levelModel.setRegionModel(region);
					}
				}					
			}	
			for(DistributorLevelModel level:leveList){
				if(null!=this.levelModel.getManagingLevelIdDistributorLevelModel() && level.getDistributorLevelId().longValue()==this.levelModel.getManagingLevelId().longValue()){
					this.levelModel.setRelationManagingLevelIdDistributorLevelModel(level);
				}
			}
			/*if(this.leveList.size()>0)
			{
				if(null!=this.leveList.get(0).getUltimateManagingLevelId())
					this.levelModel.setUltimateManagingLevelId(this.leveList.get(0).getUltimateManagingLevelId());	
				else
					this.levelModel.setUltimateManagingLevelId(this.leveList.get(0).getDistributorLevelId());
			}*/
			
			/*for(DistributorLevelModel level:leveList)
			{
				if(!level.isEditMode() && 
					null!=level.getRegionModel() && 
					(level.getRegionModel().getRegionId().longValue()==this.levelModel.getRegionModel().getRegionId().longValue()))
				{
					if(null==level.getUltimateManagingLevelId())
						this.levelModel.setUltimateManagingLevelId(level.getDistributorLevelId());
					else
						this.levelModel.setUltimateManagingLevelId(level.getUltimateManagingLevelId());
					break;
				}
			}*/
			
			this.levelModel.setUpdatedOn(new Date());
			this.levelModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
			this.levelModel.setRegionalHierarchyId(this.regionalHierarchyModel.getRegionalHierarchyId());
			this.levelModel.setRegionalHierarchyModel(this.regionalHierarchyModel);
			if(null!= this.levelModel.getManagingLevelIdDistributorLevelModel() && this.levelModel.getManagingLevelId()<1){
				this.levelModel.setManagingLevelId(null);
			}
			try {
				BaseWrapper baseWrapper= new BaseWrapperImpl();
				baseWrapper.setBasePersistableModel(this.levelModel);		
				baseWrapper=agentHierarchyManager.saveOrUpdateDistributorLevel(baseWrapper);
				
				if(this.levelModel.isEditMode()){
					for(int i=0;i<leveList.size();i++){
						DistributorLevelModel model=this.leveList.get(i);
						if(model.getDistributorLevelId().longValue()==((DistributorLevelModel)baseWrapper.getBasePersistableModel()).getDistributorLevelId().longValue())
						{
							leveList.remove(i);
							((DistributorLevelModel)baseWrapper.getBasePersistableModel()).setEditMode(Boolean.FALSE);
							leveList.add(i,(DistributorLevelModel)baseWrapper.getBasePersistableModel());
							this.listSize = this.leveList.size();
						}
						else if(null!=model.getManagingLevelIdDistributorLevelModel() && model.getManagingLevelId().longValue()==
								((DistributorLevelModel)baseWrapper.getBasePersistableModel()).getDistributorLevelId().longValue()){
							model.setManagingLevelIdDistributorLevelModel(levelModel);
							leveList.remove(i);
							//((DistributorLevelModel)baseWrapper.getBasePersistableModel()).setEditMode(Boolean.FALSE);
							leveList.add(i,model);
							this.listSize = this.leveList.size();
						}
					}
				}else{
					leveList.add((DistributorLevelModel)baseWrapper.getBasePersistableModel());
					this.listSize = this.leveList.size();
				}
				/*this.levelItems.add(new SelectItem(((DistributorLevelModel)baseWrapper.getBasePersistableModel()).getDistributorLevelId()
						, ((DistributorLevelModel)baseWrapper.getBasePersistableModel()).getName())) ;*/
				//this.levelModel= new DistributorLevelModel();
				this.clearDistributorLevelForm(null);
				this.parentLevelEnable=Boolean.TRUE;
				JSFContext.setInfoMessage(Constants.InfoMessages.AH_DISTLEVEL_SAVE_SUCCESS_INFO);
				logger.info(Constants.InfoMessages.AH_DISTLEVEL_SAVE_SUCCESS_INFO);
			} catch (FrameworkCheckedException e) {
				// TODO Auto-generated catch block
				logger.error(e);
			}
			catch(Exception e){
				logger.error(e);
				e.printStackTrace();
			}
		}
		if(logger.isDebugEnabled()){
			logger.debug("DitributorLevelActionBean.addDistributorLevels End");
		}		
	}
	
	public void saveDistributorLevels(){
		if(logger.isDebugEnabled()){
			logger.debug("DitributorLevelActionBean.saveDistributorLevels Starts");
		}
		if(logger.isDebugEnabled()){
			logger.debug("DitributorLevelActionBean.saveDistributorLevels End");
		}		
	}
	
	public void editDistributorLevels(DistributorLevelModel model){
		if(logger.isDebugEnabled()){
			logger.debug("DitributorLevelActionBean.editDistributorLevels Starts");
		}
		try
		{
			//this.levelModel=model;
			if(!this.levelModel.isEditMode()){
				model.setEditMode(Boolean.TRUE);
				this.levelModel.setEditMode(Boolean.TRUE);
				this.levelModel.setName(model.getName());
				this.levelModel.setDescription(model.getDescription());
				this.levelModel.setComments(model.getComments());
				this.levelModel.setRegionModel(model.getRegionModel());
				this.levelModel.setRegionalHierarchyId(model.getRegionalHierarchyId());
				this.levelModel.setDistributorLevelId(model.getDistributorLevelId());
				this.levelModel.setManagingLevelId(model.getManagingLevelId());
				this.levelModel.setActive(model.getActive());
				this.levelModel.setUltimateManagingLevelId(model.getUltimateManagingLevelId());
				this.levelModel.setCreatedBy(model.getCreatedBy());
				this.levelModel.setCreatedOn(model.getCreatedOn());				
				this.levelModel.setVersionNo(model.getVersionNo());
				
				
				
				
				/*for(DistributorLevelModel levelmodel:leveList)
				{
					if(null!=levelmodel.getRegionModel() && null!=this.levelModel.getManagingLevelIdDistributorLevelModel() &&
							(this.levelModel.getRegionModel().getRegionId().longValue()== levelmodel.getRegionModel().getRegionId().longValue())
							&& (this.levelModel.getManagingLevelId().longValue()==levelmodel.getDistributorLevelId().longValue()))
						this.levelItems.add(new SelectItem(levelmodel.getDistributorLevelId(), levelmodel.getName())) ;
				}*/				
				
				this.levelItems.clear();
				this.ultimateLevelItems.clear();
				
				for(DistributorLevelModel distributorLevelModel:leveList)
				{
					if(this.levelModel.getRegionModel().getRegionId().longValue() == distributorLevelModel.getRegionModel().getRegionId().longValue() && 
							this.levelModel.getDistributorLevelId() != distributorLevelModel.getDistributorLevelId())
					{
						if(distributorLevelModel.getManagingLevelId() == null)
						{
							this.ultimateLevelItems.add(new SelectItem(distributorLevelModel.getDistributorLevelId(), distributorLevelModel.getName()));
						}
					}
				}
				
				
				if(model.getManagingLevelId() == null)
				{
					for(RegionModel regionModel:this.regionList)
					{
						if(regionModel.getRegionId().longValue() == model.getRegionModelId().longValue())
						{
							this.regionalHierarchyActive = regionModel.getActive();
							break;
						}
					}
				}
				
				if(model.getManagingLevelId() != null)
				{
					for(DistributorLevelModel distributorLevelModel:leveList)
					{
						if(distributorLevelModel.getDistributorLevelId().longValue() == model.getManagingLevelId().longValue() &&
								model.getDistributorLevelId() != distributorLevelModel.getDistributorLevelId())
						{
							this.regionalHierarchyActive = distributorLevelModel.getActive();
							break;
						}
					}
					
					
					for(DistributorLevelModel distributorLevelModel:leveList)
					{
						if(this.levelModel.getRegionModel().getRegionId().longValue() == distributorLevelModel.getRegionModel().getRegionId().longValue())
						{
							if(distributorLevelModel.getManagingLevelId() == null)
							{
								this.ultimateLevelItems.add(new SelectItem(distributorLevelModel.getDistributorLevelId(), distributorLevelModel.getName()));
							}
						}
						if(this.levelModel.getManagingLevelId().longValue() == distributorLevelModel.getDistributorLevelId().longValue())
						{
							this.levelItems.add(new SelectItem(distributorLevelModel.getDistributorLevelId(), distributorLevelModel.getName())) ;
						}
					}
					
					
					/*onChangeRegion(null);
					for(DistributorLevelModel distributorLevelModel:leveList){
						if(null!=distributorLevelModel.getRegionModel() && (this.levelModel.getRegionModel().getRegionId().longValue()== distributorLevelModel.getRegionModel().getRegionId().longValue()))
						{
							this.levelItems.add(new SelectItem(distributorLevelModel.getDistributorLevelId(), distributorLevelModel.getName())) ;
						}
					}*/
					
				}
				if(isReferenceExist(this.levelModel)){
					this.parentLevelEnable=Boolean.FALSE;
				}
				
			}else{
				JSFContext.addErrorMessage(Constants.InfoMessages.AH_DISTLEVEL_EDIT_FAILURE_INFO);
			}
		
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
			
		if(logger.isDebugEnabled()){
			logger.debug("DitributorLevelActionBean.editDistributorLevels End");
		}		
	}
	
	/*public void enableParentLevel(DistributorLevelModel model){
		for(DistributorLevelModel levelModel:leveList){
			if(null!=levelModel.getManagingLevelId() && (model.getDistributorLevelId().longValue()==levelModel.getManagingLevelId().longValue())){
				
			}
		}
	}*/
	
	public void deleteDistributorLevels(DistributorLevelModel model){
		if(logger.isDebugEnabled()){
			logger.debug("DitributorLevelActionBean.deleteDistributorLevels Starts");
		}
		try {
			if(!isReferenceExist(model)){
				BaseWrapper baseWrapper= new BaseWrapperImpl();
				baseWrapper.setBasePersistableModel(model);
				agentHierarchyManager.deleteDistributorLevel(baseWrapper);
				for(SelectItem item:levelItems){
					if(item.getValue()==model.getDistributorLevelId()){
						levelItems.remove(item);
						break;
					}
				}
				for(DistributorLevelModel level:leveList){
					if(level.getDistributorLevelId().longValue()==model.getDistributorLevelId().longValue()){
						this.leveList.remove(level);
						this.listSize = this.leveList.size();
						break;
					}
				}
				this.clearDistributorLevelForm(null);
				JSFContext.setInfoMessage(Constants.InfoMessages.AH_DISTLEVEL_DELETE_SUCCESS_INFO);
				//this.levelModel=new DistributorLevelModel();
			}
		} catch (FrameworkCheckedException e) {
			// TODO Auto-generated catch block
			logger.error(e);
		}catch(Exception e){
			if(e instanceof DataIntegrityViolationException){
				logger.error(e);
				JSFContext.addErrorMessage(Constants.ErrorMessages.AH_DISTLEVEL_REF_EXIST_ERROR);
			}
		}
		if(logger.isDebugEnabled()){
			logger.debug("DitributorLevelActionBean.deleteDistributorLevels End");
		}		
	}
	public Boolean isReferenceExist(DistributorLevelModel model){
		if(logger.isDebugEnabled()){
			logger.debug("DitributorLevelActionBean.isReferenceExist Starts");
		}
		for(DistributorLevelModel levelModel:leveList){
			if(null!=levelModel.getManagingLevelId() && (model.getDistributorLevelId().longValue()==levelModel.getManagingLevelId().longValue())){
				if(!model.isEditMode())
					JSFContext.addErrorMessage(Constants.InfoMessages.AH_DISTLEVEL_DELETE_FAILURE_INFO);
				return Boolean.TRUE;
			}
		}	
		if(logger.isDebugEnabled()){
			logger.debug("DitributorLevelActionBean.isReferenceExist End");
		}
		return Boolean.FALSE;		
	}
	
	public void clearDistributorLevelForm(ActionEvent event){
		if(logger.isDebugEnabled()){
			logger.debug("DitributorLevelActionBean.clearDistributorLevelForm Starts");
		}
			this.levelModel=new DistributorLevelModel();
			this.levelItems= new ArrayList<SelectItem>();
			this.ultimateLevelItems = new ArrayList<SelectItem>();
			this.regionalHierarchyActive=Boolean.TRUE;
		if(logger.isDebugEnabled()){
			logger.debug("DitributorLevelActionBean.clearDistributorLevelForm End");
		}		
	}
	
	public Boolean validate(DistributorLevelModel model){
		if(logger.isDebugEnabled()){
			logger.debug("DitributorLevelActionBean.validate Starts");
		}
		Boolean validate=Boolean.TRUE;
		if(CommonUtils.isEmpty(model.getName())){
			JSFContext.addErrorMessage(Constants.ErrorMessages.AH_DISTLEVEL_LEVELNAME_ERROR);
			validate=Boolean.FALSE;
		}
		if(null==model.getRegionModelId() || model.getRegionModelId()<1){
			JSFContext.addErrorMessage(Constants.ErrorMessages.AH_DISTLEVEL_Region_ERROR);
			validate=Boolean.FALSE;
		}
		if(levelItems.size()>0 && (null!=model.getManagingLevelId() && model.getManagingLevelId()<1) && !model.isEditMode()){
			JSFContext.addErrorMessage(Constants.ErrorMessages.AH_DISTLEVEL_PARENTLEVELNAME_ERROR);
			validate=Boolean.FALSE;
		}
		
		/*if(ultimateLevelItems.size()>0 && (null==model.getUltimateManagingLevelIdDistributorLevelModel() ||
					(null!=model.getUltimateManagingLevelIdDistributorLevelModel() && model.getUltimateManagingLevelId()<1))){
			JSFContext.addErrorMessage(Constants.ErrorMessages.AH_DISTLEVEL_ULTIMATE_PARENTLEVELNAME_ERROR);
			validate=Boolean.FALSE;
		}*/
		
		for(DistributorLevelModel levelModel:this.leveList){
			if(levelModel.getRegionModelId().longValue() == model.getRegionModelId().longValue() && levelModel.getName().equals(model.getName()))
			{
					if(model.isEditMode() && levelModel.getDistributorLevelId().longValue()!=model.getDistributorLevelId().longValue())
					{
						JSFContext.addErrorMessage("Agent level is already defined against the selected region.");
						validate=Boolean.FALSE;
						break;
					}else if(!model.isEditMode()){
						JSFContext.addErrorMessage("Agent level is already defined against the selected region.");
						validate=Boolean.FALSE;
						break;
					}
			}
		}
		if(!model.isEditMode()){		
			for(DistributorLevelModel levelModel:this.leveList){
				if(null!=levelModel.getManagingLevelId() && null!=model.getManagingLevelId() && (model.getManagingLevelId().longValue()==levelModel.getManagingLevelId().longValue())){
					//if(levelModel.getName().equals(model.getName())){
						JSFContext.addErrorMessage(Constants.ErrorMessages.AH_DISTLEVEL_LEVELNAMEEXIST_ERROR);
						validate=Boolean.FALSE;
					//}
				}
			}
		}else{
			for(DistributorLevelModel levelModel:this.leveList){
				if(null!=levelModel.getManagingLevelId() && null!=model.getManagingLevelId() && 
						 model.getRegionModelId().longValue()== levelModel.getRegionModelId().longValue() &&
						null!=model.getManagingLevelIdDistributorLevelModel() && model.getManagingLevelId().longValue()==0
						&& levelItems.size()>0){
					//if(levelModel.getName().equals(model.getName())){
					JSFContext.addErrorMessage(Constants.ErrorMessages.AH_DISTLEVEL_PARENTLEVELNAME_ERROR);
					validate=Boolean.FALSE;
					break;
					//}
				}
			}
		}
		if(logger.isDebugEnabled()){
			logger.debug("DitributorLevelActionBean.validate End");
		}
		return validate;
	}
	
	public void onChangeRegion(AjaxBehaviorEvent e){		
			try{
				this.levelModel.setManagingLevelId(null);
				levelItems = new ArrayList<SelectItem>();
				this.ultimateLevelList = agentHierarchyManager.loadDistributorLevelByRegionIdAndRegionHierarchyId(this.getLevelModel().getRegionModelId(), this.regionalHierarchyModel.getRegionalHierarchyId());
				this.ultimateLevelItems = new ArrayList<SelectItem>();
				for(DistributorLevelModel model : ultimateLevelList){
					this.ultimateLevelItems.add(new SelectItem(model.getDistributorLevelId(), model.getName()));
				}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}	
	
	public void  enableActiveonRegion()
	{
		
		
		RegionModel regionModel=null;
		for(RegionModel region:regionList)
		{
			if(null!=region && region.getRegionId()== this.levelModel.getRegionModel().getRegionId().longValue())
			{
				regionModel=region;
			}
		}
		this.setRegionalHierarchyActive(regionModel.getActive());
		if(this.regionalHierarchyActive){
			this.levelModel.setActive(Boolean.TRUE);
		}
		else{
			this.levelModel.setActive(Boolean.FALSE);
		}
	}
	
	public void onChangeParentLevel(AjaxBehaviorEvent e){	
		for(DistributorLevelModel model:leveList){
			if(null!=this.levelModel.getManagingLevelId() && (this.levelModel.getManagingLevelId().longValue()==model.getDistributorLevelId().longValue())){				
				this.setRegionalHierarchyActive(model.getActive());
				if(this.regionalHierarchyActive){
					this.levelModel.setActive(Boolean.TRUE);
				}
				else{
					this.levelModel.setActive(Boolean.FALSE);
				}
			}
		}
	}
	
	public void onChangeUltimateAgentLevel(AjaxBehaviorEvent e)
	{
		levelItems= new ArrayList<SelectItem>();
		if(this.leveList.size()<1){
			enableActiveonRegion();			
		}
		
		if(this.levelModel.getUltimateManagingLevelId() !=null && this.levelModel.getUltimateManagingLevelId().longValue() == 0L){
			this.levelModel.setManagingLevelId(null);
			return;
		}
		
		
		DistributorLevelModel parentLevel = null;
		for(DistributorLevelModel model:leveList)
		{
			if(this.levelModel.getUltimateManagingLevelId().equals(model.getDistributorLevelId()))
			{
				parentLevel = model;
				break;
			}
		}
		
		Boolean parentLevelFound = Boolean.FALSE;
		while(!parentLevelFound)
		{
			Boolean parentUpdated = Boolean.FALSE;
			for(DistributorLevelModel level:leveList)
			{
				if(parentLevel.getDistributorLevelId().equals(level.getManagingLevelId()))
				{
					parentLevel = level;
					parentUpdated = Boolean.TRUE;
					break;
				}
			}
			parentLevelFound = !parentUpdated;
		}
		this.levelItems.add(new SelectItem(parentLevel.getDistributorLevelId(), parentLevel.getName())) ;
		this.levelModel.setManagingLevelId(parentLevel.getDistributorLevelId());
		
		if(this.levelModel.getManagingLevelId() == null)
		{
			RegionModel regionModel=null;
			for(RegionModel region:regionList)
			{
				if(region.getRegionId()== this.levelModel.getRegionModel().getRegionId().longValue())
				{
					this.setRegionalHierarchyActive(region.getActive());
					break;
				}
			}
		}
		else
		{
			this.setRegionalHierarchyActive(parentLevel.getActive());
		}
		
		
		
		/*for(DistributorLevelModel model:leveList)
		{
			if(this.levelModel.getRegionModel().getRegionId().longValue()== model.getRegionModel().getRegionId().longValue())
			{
				Boolean isChildNode=Boolean.FALSE;
				for(DistributorLevelModel leafmodel:leveList)
				{
					if(null!= leafmodel.getManagingLevelId() && model.getDistributorLevelId().longValue()== leafmodel.getManagingLevelId().longValue())
					{
						isChildNode=Boolean.TRUE;
						break;
					}
				}
				if(!isChildNode)
				{
					this.levelItems.add(new SelectItem(model.getDistributorLevelId(), model.getName())) ;
					this.levelModel.setManagingLevelId(model.getDistributorLevelId());
					break;
				}
				RegionModel regionModel=null;
				for(RegionModel region:regionList ){
					if(null!=region && region.getRegionId()== this.levelModel.getRegionModel().getRegionId().longValue()){
						regionModel=region;
					}
				}
				this.setDistributorActive(regionModel.getActive());
				if(this.distributorActive){
					this.levelModel.setActive(Boolean.TRUE);
				}
				else{
					this.levelModel.setActive(Boolean.FALSE);
				}
				//enableActiveonRegion();
			}
			enableActiveonRegion();
		}*/
	
	}
	
	public void onChangeActive(AjaxBehaviorEvent event)
	{
		 if(logger.isDebugEnabled()){
				logger.debug("DitributorLevelActionBean.onChangeActive Start");
		 }		 
		 if(this.levelModel.isEditMode() && !this.levelModel.getActive())
		 {
			 try 
			 {
				 Boolean isExist=agentHierarchyManager.isAgentLevelRefExist(this.levelModel.getDistributorLevelId());
				 if(isExist)
				 {
					 JSFContext.addErrorMessage("Agent Level cannot be in-active due to reference data found in the system.");
					 this.levelModel.setActive(isExist);
				 }
				 else
				 {
					 for(DistributorLevelModel distributorLevelModel :this.leveList)
					 {
						 if(distributorLevelModel.getManagingLevelId() != null && this.levelModel.getDistributorLevelId().longValue() == distributorLevelModel.getManagingLevelId().longValue() && distributorLevelModel.getActive())
						 {
							 JSFContext.addErrorMessage("Agent Level cannot be in-active due to reference data found in the system.");
							 this.levelModel.setActive(Boolean.TRUE);
							 break;
						 }
					 }
				 }
			 }
			 catch (FrameworkCheckedException e) 
			 {
				// TODO Auto-generated catch block
				logger.error(e);
			 }
		 }
		 if(logger.isDebugEnabled()){
				logger.debug("DitributorLevelActionBean.onChangeActive End");
		 }
		
	}
	public List<SelectItem> getLevelItems() {
		return levelItems;
	}

	public void setLevelItems(List<SelectItem> levelItems) {
		this.levelItems = levelItems;
	}
	
	public DistributorLevelModel getLevelModel() {
		return levelModel;
	}

	public void setLevelModel(DistributorLevelModel levelModel) {
		this.levelModel = levelModel;
	}
	public List<SelectItem> getRegionItems() {
		return regionItems;
	}
	public void setRegionItems(List<SelectItem> regionItems) {
		this.regionItems = regionItems;
	}	
	public List<DistributorLevelModel> getLeveList() {
		return leveList;
	}
	public void setLeveList(List<DistributorLevelModel> leveList) {
		this.leveList = leveList;
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
	public Boolean getParentLevelEnable() {
		return parentLevelEnable;
	}

	public void setParentLevelEnable(Boolean parentLevelEnable) {
		this.parentLevelEnable = parentLevelEnable;
	}
	
	public Boolean getRegionalHierarchyActive() {
		return regionalHierarchyActive;
	}

	public void setRegionalHierarchyActive(Boolean regionalHierarchyActive) {
		this.regionalHierarchyActive = regionalHierarchyActive;
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

	public int getAgentLevelNameSortingOrder() {
		return agentLevelNameSortingOrder;
	}

	public void setAgentLevelNameSortingOrder(int agentLevelNameSortingOrder) {
		this.agentLevelNameSortingOrder = agentLevelNameSortingOrder;
	}

	public int getParentAgentLevelNameSortingOrder() {
		return parentAgentLevelNameSortingOrder;
	}

	public void setParentAgentLevelNameSortingOrder(
			int parentAgentLevelNameSortingOrder) {
		this.parentAgentLevelNameSortingOrder = parentAgentLevelNameSortingOrder;
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

	public List<DistributorLevelModel> getUltimateLevelList() {
		return ultimateLevelList;
	}

	public void setUltimateLevelList(List<DistributorLevelModel> ultimateLevelList) {
		this.ultimateLevelList = ultimateLevelList;
	}

	public List<SelectItem> getUltimateLevelItems() {
		return ultimateLevelItems;
	}

	public void setUltimateLevelItems(List<SelectItem> ultimateLevelItems) {
		this.ultimateLevelItems = ultimateLevelItems;
	}
	
}
