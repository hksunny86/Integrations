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
import javax.faces.model.SelectItem;
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

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.agenthierarchy.DistRegHierAssociationModel;
import com.inov8.microbank.common.model.agenthierarchy.RegionalHierarchyModel;
import com.inov8.microbank.common.util.AuthenticationUtil;
import com.inov8.microbank.common.util.Constants;
import com.inov8.microbank.common.util.GenericComparator;
import com.inov8.microbank.common.util.JSFContext;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.SessionBeanObjects;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.agenthierarchy.AgentHierarchyManager;


@ManagedBean(name="associateRegHierActionBean")
@SessionScoped
public class AssociateRegHierActionBean {

	private static final Log logger= LogFactory.getLog(AssociateRegHierActionBean.class);
	@ManagedProperty(value="#{agentHierarchyManager}") 
	private AgentHierarchyManager agentHierarchyManager;
	
	private List<DistributorModel> distributorModelList = null;
	private List<SelectItem> distributorList = new ArrayList<SelectItem>();
	private List<RegionalHierarchyModel> regionalHierarchyModelList = null;
	private List<SelectItem> regionalHierarchyList = new ArrayList<SelectItem>();
	private List<DistRegHierAssociationModel> distRegHierAssociationModelList = null;

	DistRegHierAssociationModel distRegHierAssociationModel = new DistRegHierAssociationModel();
	
	private Boolean securityCheck= Boolean.FALSE;
	private Boolean editSecurityCheck=Boolean.FALSE;
	private Boolean deleteSecurityCheck=Boolean.FALSE;
	
	private int listSize;
	
	private int UNSORTED = 0;
	private int ASCENDING_ORDER = 1;
	private int DESCENDING_ORDER = 2;
	
	private int regHierNameSortingOrder = UNSORTED;
	private int distributorNameSortingOrder = UNSORTED;
	private int descriptionSortingOrder = UNSORTED;
	
	private int sortingPointer = 0;
	
	public AssociateRegHierActionBean()
	{
	}
	
	@PostConstruct
	public void ini()
	{
		if(logger.isDebugEnabled()){
			logger.debug("AssociateRegHierActionBean.ini Starts");
		}
		try
		{
			SearchBaseWrapper searchBaseWrapper = agentHierarchyManager.loadRegHierAssociationData();
			if(searchBaseWrapper.getCustomList() != null)
			{
				List result= searchBaseWrapper.getCustomList().getResultsetList();
				this.distributorModelList = (ArrayList<DistributorModel>)result.get(0);
				this.regionalHierarchyModelList = (ArrayList<RegionalHierarchyModel>)result.get(1);
				if(this.distributorModelList != null)
				{
					for(DistributorModel model:distributorModelList){
						this.distributorList.add(new SelectItem(model.getDistributorId(),model.getName()));
					}
				}
				if(this.regionalHierarchyModelList != null)
				{
					for(RegionalHierarchyModel model:this.regionalHierarchyModelList){
						this.regionalHierarchyList.add(new SelectItem(model.getRegionalHierarchyId(),model.getRegionalHierarchyName()));
					}
				}
			}
			
			searchBaseWrapper = agentHierarchyManager.findRegHierAssociatedData();
			if(searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0)
			{
				this.distRegHierAssociationModelList = searchBaseWrapper.getCustomList().getResultsetList();
				
				this.listSize = this.distRegHierAssociationModelList.size();
				for(DistRegHierAssociationModel model: this.distRegHierAssociationModelList)
				{
					model.setIndex(sortingPointer);
					sortingPointer++;
					
					for(RegionalHierarchyModel regionalHierarchyModel:this.regionalHierarchyModelList){
						if(regionalHierarchyModel.getRegionalHierarchyId().equals(model.getRegionalHierarchyId()))
						{
							model.setRegionalHierarchyModel(regionalHierarchyModel);
							break;
						}
					}
					
					for(DistributorModel distributorModel:this.distributorModelList){
						if(distributorModel.getDistributorId().equals(model.getDistributorId()))
						{
							model.setDistributor(distributorModel);
							break;
						}
					}
				}
			}
			else
			{
				this.distRegHierAssociationModelList = new ArrayList<DistRegHierAssociationModel>();
				this.listSize = this.distRegHierAssociationModelList.size();
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
		catch(FrameworkCheckedException fce)
		{
			logger.error(fce);
		}
		catch(Exception e)
		{
			logger.error(e);
		}
		if(logger.isDebugEnabled()){
			logger.debug("AssociateRegHierActionBean.ini End");
		}
	}
	
	public void sortByDistributorName(ActionEvent e)
	{
		if(this.distributorNameSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.distRegHierAssociationModelList, new GenericComparator(Boolean.FALSE));
			this.distributorNameSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.distributorNameSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.distRegHierAssociationModelList, new GenericComparator("index", Boolean.TRUE));
			this.distributorNameSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.distRegHierAssociationModelList, new GenericComparator("distributorName", Boolean.TRUE));
			this.distributorNameSortingOrder = this.ASCENDING_ORDER;
		}
		this.descriptionSortingOrder = UNSORTED;
		this.regHierNameSortingOrder = UNSORTED;
	}
	
	public void sortByRegionalHierarchy(ActionEvent e)
	{
		if(this.regHierNameSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.distRegHierAssociationModelList, new GenericComparator(Boolean.FALSE));
			this.regHierNameSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.regHierNameSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.distRegHierAssociationModelList, new GenericComparator("index", Boolean.TRUE));
			this.regHierNameSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.distRegHierAssociationModelList, new GenericComparator("regionalHierarchyName", Boolean.TRUE));
			this.regHierNameSortingOrder = this.ASCENDING_ORDER;
		}
		this.distributorNameSortingOrder = this.UNSORTED;
		this.descriptionSortingOrder = this.UNSORTED;
	}
	
	public void sortByDescription(ActionEvent e)
	{
		if(this.descriptionSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.distRegHierAssociationModelList, new GenericComparator(Boolean.FALSE));
			this.descriptionSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.descriptionSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.distRegHierAssociationModelList, new GenericComparator("index", Boolean.TRUE));
			this.descriptionSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.distRegHierAssociationModelList, new GenericComparator("description", Boolean.TRUE));
			this.descriptionSortingOrder = this.ASCENDING_ORDER;
		}
		this.distributorNameSortingOrder = this.UNSORTED;
		this.regHierNameSortingOrder = this.UNSORTED;
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
	        cell.setCellValue("Regional Hierarchy");
	        cell.setCellStyle(cellStyle);
	        
	        cell = row.createCell(Short.valueOf("2"));
	        cell.setCellValue("Description");
	        cell.setCellStyle(cellStyle);
	        
	        
	        int rowCounter = 1;
	        for(DistRegHierAssociationModel model: this.distRegHierAssociationModelList)
	        {
	        	row = sheet.createRow(rowCounter);
	        	rowCounter++;
	        	
	        	cell = row.createCell(Short.valueOf("0"));
	        	cell.setCellValue(model.getDistributorName());
		        
		        cell = row.createCell(Short.valueOf("1"));
		        cell.setCellValue(model.getRegionalHierarchyName());
		        
		        cell = row.createCell(Short.valueOf("2"));
		        cell.setCellValue(model.getDescription());
	        }
	        
	        FacesContext context = FacesContext.getCurrentInstance();
	        HttpServletResponse res = (HttpServletResponse) context.getExternalContext().getResponse();
	        res.setContentType("application/vnd.ms-excel");
	        res.setHeader("Content-disposition", "attachment;filename=Regional Hierarchy Association data.xls");

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
	/*
	public void regionIntegrityCheck(AjaxBehaviorEvent event)
	{
		if(logger.isDebugEnabled()){
			logger.debug("AssociateRegHierActionBean.regionIntegrityCheck End");
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
			logger.debug("AssociateRegHierActionBean.regionIntegrityCheck End");
		}
	}*/
	
	public String cancel()
	{
		if(logger.isDebugEnabled()){
			logger.debug("AssociateRegHierActionBean.cancel Starts");
		}
		SessionBeanObjects.removeAllSessionObjects();
		if(logger.isDebugEnabled()){
			logger.debug("AssociateRegHierActionBean.cancel End");
		}
		return Constants.ReturnCodes.CANCEL_REG_HIER_ASSOCIATIONS;
	}
	
	public void saveOrUpdateAssociation(ActionEvent e)
	{
		if(logger.isDebugEnabled()){
			logger.debug("AssociateRegHierActionBean.addRegion Starts");
		}
		try
		{
			if(this.validate(this.distRegHierAssociationModel))
			{
				long theDate = new Date().getTime();
				BaseWrapper baseWrapper = new BaseWrapperImpl();
				if(distRegHierAssociationModel.isEditMode())											
				{
					this.distRegHierAssociationModel.setUpdatedOn(new Date(theDate));
					this.distRegHierAssociationModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
					baseWrapper.setBasePersistableModel(this.distRegHierAssociationModel);
					agentHierarchyManager.saveOrUpdateRegionalHierarchyAssociation(baseWrapper);
					for(DistributorModel model: this.distributorModelList)
					{
						if(model.getDistributorId().equals(this.distRegHierAssociationModel.getDistributorId()))
						{
							this.distRegHierAssociationModel.setDistributor(model);
							break;
						}
					}
					for(RegionalHierarchyModel model: this.regionalHierarchyModelList)
					{
						if(model.getRegionalHierarchyId().equals(this.distRegHierAssociationModel.getRegionalHierarchyId()))
						{
							this.distRegHierAssociationModel.setRegionalHierarchyModel(model);
							break;
						}
					}
					this.distRegHierAssociationModel.setEditMode(Boolean.FALSE);
					this.distRegHierAssociationModel = new DistRegHierAssociationModel();
					JSFContext.setInfoMessage(Constants.InfoMessages.AH_REG_HIER_ASSOCIATION_UPDATE_SUCCESS_INFO);
				}
				else
				{
					Boolean validated = Boolean.TRUE;
					for(DistRegHierAssociationModel associationModel: this.distRegHierAssociationModelList)
					{
						if(this.distRegHierAssociationModel.getDistributorId().equals(associationModel.getDistributorId()))
						{
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Agent Network '" + associationModel.getDistributorName() + "' is already associate with '" + associationModel.getRegionalHierarchyName() + "' regional hierarchy."));
							validated = Boolean.FALSE;
							break;
						}
					}
					
					if(validated)
					{
						this.distRegHierAssociationModel.setUpdatedOn(new Date(theDate));
						this.distRegHierAssociationModel.setCreatedOn(new Date(theDate));
						this.distRegHierAssociationModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
						this.distRegHierAssociationModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
						baseWrapper.setBasePersistableModel(this.distRegHierAssociationModel);
						agentHierarchyManager.saveOrUpdateRegionalHierarchyAssociation(baseWrapper);
						
						
						this.distRegHierAssociationModel.setIndex(this.sortingPointer++);
						for(DistributorModel model: this.distributorModelList)
						{
							if(model.getDistributorId().equals(this.distRegHierAssociationModel.getDistributorId()))
							{
								this.distRegHierAssociationModel.setDistributor(model);
							}
						}
						for(RegionalHierarchyModel model: this.regionalHierarchyModelList)
						{
							if(model.getRegionalHierarchyId().equals(this.distRegHierAssociationModel.getRegionalHierarchyId()))
							{
								this.distRegHierAssociationModel.setRegionalHierarchyModel(model);
							}
						}
						this.distRegHierAssociationModelList.add(this.distRegHierAssociationModel);
						this.listSize = this.distRegHierAssociationModelList.size();
						JSFContext.setInfoMessage(Constants.InfoMessages.AH_REG_HIER_ASSOCIATION_SAVED_SUCCESS_INFO);
						this.distRegHierAssociationModel = new DistRegHierAssociationModel();
					}
				}
			}
		}
		catch(Exception ex)
		{
			logger.error(ex);
		}
		if(logger.isDebugEnabled()){
			logger.debug("AssociateRegHierActionBean.addRegion End");
		}
	}
	
	public void deleteAssociation(ActionEvent e)
	{
		if(logger.isDebugEnabled()){
			logger.debug("AssociateRegHierActionBean.deleteAssociation Starts");
		}
		try
		{
			int rowIndex = Integer.valueOf((String)JSFContext.getFromRequest("rowIndexValue"));		
			DistRegHierAssociationModel model = this.distRegHierAssociationModelList.get(rowIndex);           						
			SearchBaseWrapper searchBaseWrapper = this.agentHierarchyManager.findRetailersByDistributorId(model.getDistributorId());
			if(searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0)
			{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(Constants.ErrorMessages.AH_REG_HIER_ASSOCIATION_REFERENCE_FOUND_ERROR));
			}
			else
			{
				BaseWrapper baseWrapper = new BaseWrapperImpl();
				baseWrapper.setBasePersistableModel(model);
				this.agentHierarchyManager.deleteRegionalHierarchyAssociation(baseWrapper);
				JSFContext.setInfoMessage(Constants.InfoMessages.AH_REG_HIER_ASSOCIATION_DELETED_SUCCESS_INFO);
				logger.info(Constants.InfoMessages.AH_REG_HIER_ASSOCIATION_DELETED_SUCCESS_INFO);
				this.distRegHierAssociationModelList.remove(model);
				this.listSize = this.distRegHierAssociationModelList.size();
				this.distRegHierAssociationModel = new DistRegHierAssociationModel();	
			}
		}
		catch(Exception ex)
		{
			logger.error(ex);
		}
		if(logger.isDebugEnabled()){
			logger.debug("AssociateRegHierActionBean.deleteAssociation End");
		}
	}
	
	public void editAssociation(ActionEvent e)
	{
		if(logger.isDebugEnabled()){
			logger.debug("AssociateRegHierActionBean.editAssociation Starts");
		}
		try
		{
			if(this.distRegHierAssociationModel.isEditMode())
			{
				JSFContext.addErrorMessage(Constants.ErrorMessages.AH_REG_HIER_ASSOCIATION_SAVE_INLINE_ROW_ERROR);
			}
			else
			{
				int rowIndex = Integer.valueOf((String)JSFContext.getFromRequest("rowIndexValue"));		
				this.distRegHierAssociationModel = this.distRegHierAssociationModelList.get(rowIndex);				
				
				if(agentHierarchyManager.checkAgentExistsForDistributor(distRegHierAssociationModel.getDistributorId())){
					JSFContext.addErrorMessage(Constants.ErrorMessages.AH_REG_HIER_ASSOCIATION_SAVE_INTEGRITY_ERROR);
				}else{
					this.distRegHierAssociationModel.setEditMode(Boolean.TRUE);
				}

			}
		}
		catch(Exception ex)
		{
			logger.error(ex);
		}
		if(logger.isDebugEnabled()){
			logger.debug("AssociateRegHierActionBean.editAssociation End");
		}
	}
	
	public void clearAssociateRegionalHierarchyForm(ActionEvent e)
	{
		if(logger.isDebugEnabled()){
			logger.debug("AssociateRegHierActionBean.clearAssociateRegionalHierarchyForm Starts");
		}
		SessionBeanObjects.removeAllSessionObjects();
	
//		if(this.distRegHierAssociationModel.isEditMode())
//		{
//			for(DistRegHierAssociationModel model: this.distRegHierAssociationModelList)
//			{
//				if(model.getDistRegHierId().equals(this.distRegHierAssociationModel.getDistRegHierId()))
//				{
//					model.setEditMode(Boolean.FALSE);
//					break;
//				}
//			}
//		}
		
//		this.distRegHierAssociationModel = new DistRegHierAssociationModel();
		
		if(logger.isDebugEnabled()){
			logger.debug("AssociateRegHierActionBean.clearAssociateRegionalHierarchyForm End");
		}
	}
	
	private boolean validate(DistRegHierAssociationModel paramModel)
	{
		if(logger.isDebugEnabled()){
			logger.debug("AssociateRegHierActionBean.validate Starts");
		}
		boolean validated = Boolean.TRUE;
		try
		{
			if(paramModel.getDistributorId().equals(-1l))
			{
				validated = Boolean.FALSE;
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(Constants.ErrorMessages.AH_DIST_DISTNAME_ERROR));
			}
			
			if(paramModel.getRegionalHierarchyId().equals(-1l))
			{
				validated = Boolean.FALSE;
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(Constants.ErrorMessages.AH_REG_HIER_NAME_ERROR));
			}
			
		}
		catch(Exception ex)
		{
			logger.error(ex);
		}
		if(logger.isDebugEnabled()){
			logger.debug("AssociateRegHierActionBean.validate End");
		}
		return validated;
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

	public int getListSize() {
		return listSize;
	}

	public void setListSize(int listSize) {
		this.listSize = listSize;
	}

	public List<SelectItem> getDistributorList() {
		return distributorList;
	}

	public void setDistributorList(List<SelectItem> distributorList) {
		this.distributorList = distributorList;
	}

	public List<SelectItem> getRegionalHierarchyList() {
		return regionalHierarchyList;
	}

	public void setRegionalHierarchyList(List<SelectItem> regionalHierarchyList) {
		this.regionalHierarchyList = regionalHierarchyList;
	}

	public List<DistRegHierAssociationModel> getDistRegHierAssociationModelList() {
		return distRegHierAssociationModelList;
	}

	public void setDistRegHierAssociationModelList(
			List<DistRegHierAssociationModel> distRegHierAssociationModelList) {
		this.distRegHierAssociationModelList = distRegHierAssociationModelList;
	}

	public DistRegHierAssociationModel getDistRegHierAssociationModel() {
		return distRegHierAssociationModel;
	}

	public void setDistRegHierAssociationModel(
			DistRegHierAssociationModel distRegHierAssociationModel) {
		this.distRegHierAssociationModel = distRegHierAssociationModel;
	}

	public int getRegHierNameSortingOrder() {
		return regHierNameSortingOrder;
	}

	public void setRegHierNameSortingOrder(int regHierNameSortingOrder) {
		this.regHierNameSortingOrder = regHierNameSortingOrder;
	}

	public int getDistributorNameSortingOrder() {
		return distributorNameSortingOrder;
	}

	public void setDistributorNameSortingOrder(int distributorNameSortingOrder) {
		this.distributorNameSortingOrder = distributorNameSortingOrder;
	}

	public int getDescriptionSortingOrder() {
		return descriptionSortingOrder;
	}

	public void setDescriptionSortingOrder(int descriptionSortingOrder) {
		this.descriptionSortingOrder = descriptionSortingOrder;
	}
	
}
