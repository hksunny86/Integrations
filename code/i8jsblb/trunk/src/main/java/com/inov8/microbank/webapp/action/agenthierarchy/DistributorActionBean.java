package com.inov8.microbank.webapp.action.agenthierarchy;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.CountryModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.MnoModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.vo.agenthierarchy.DistributorVO;
import com.inov8.microbank.server.service.agenthierarchy.AgentHierarchyManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.EmailValidator;
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
import java.util.Date;
import java.util.List;


@ManagedBean(name="distributorActionBean")
@SessionScoped
public class DistributorActionBean {
	
	private static final Log logger= LogFactory.getLog(DistributorActionBean.class);
	@ManagedProperty(value="#{agentHierarchyManager}") 
	private AgentHierarchyManager agentHierarchyManager;	

	DistributorVO distributorVO = new DistributorVO();
	List<DistributorVO> distributorVOList = new ArrayList<DistributorVO>();
	private List<SelectItem> countryItems= new ArrayList<SelectItem>();
	private Boolean securityCheck= Boolean.FALSE;
	private Boolean editSecurityCheck=Boolean.FALSE;
	
	private String serverDate	=	PortalDateUtils.getServerDate();

	private int listSize;
	
	private int UNSORTED = 0;
	private int ASCENDING_ORDER = 1;
	private int DESCENDING_ORDER = 2;
	
	private int nameSortingOrder = UNSORTED;
	private int contactNameSortingOrder = UNSORTED;
	private int mobileNoSortingOrder = UNSORTED;
	private int emailSortingOrder = UNSORTED;
	private int addressSortingOrder = UNSORTED;
	private int statusSortingOrder = UNSORTED;
	
	private int sortingPointer = 0;
	
	public String cancel()
	{
		SessionBeanObjects.removeAllSessionObjects();
		return Constants.ReturnCodes.CANCEL_AGENT_NETWORK;
	}

	public void sortByName(ActionEvent e)
	{
		if(this.nameSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.distributorVOList, new GenericComparator(Boolean.FALSE));
			this.nameSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.nameSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.distributorVOList, new GenericComparator("index", Boolean.TRUE));
			this.nameSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.distributorVOList, new GenericComparator("name", Boolean.TRUE));
			this.nameSortingOrder = this.ASCENDING_ORDER;
		}
		this.contactNameSortingOrder = UNSORTED;
		this.mobileNoSortingOrder = UNSORTED;
		this.emailSortingOrder = UNSORTED;
		this.addressSortingOrder = UNSORTED;
		this.statusSortingOrder = UNSORTED;
	}
	
	public void sortByContactName(ActionEvent e)
	{
		if(this.contactNameSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.distributorVOList, new GenericComparator(Boolean.FALSE));
			this.contactNameSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.contactNameSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.distributorVOList, new GenericComparator("index", Boolean.TRUE));
			this.contactNameSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.distributorVOList, new GenericComparator("contactName", Boolean.TRUE));
			this.contactNameSortingOrder = this.ASCENDING_ORDER;
		}
		this.nameSortingOrder = this.UNSORTED;
		this.mobileNoSortingOrder = UNSORTED;
		this.emailSortingOrder = UNSORTED;
		this.addressSortingOrder = UNSORTED;
		this.statusSortingOrder = UNSORTED;
	}
	
	public void sortByMobileNo(ActionEvent e)
	{
		if(this.mobileNoSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.distributorVOList, new GenericComparator(Boolean.FALSE));
			this.mobileNoSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.mobileNoSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.distributorVOList, new GenericComparator("index", Boolean.TRUE));
			this.mobileNoSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.distributorVOList, new GenericComparator("phoneNo", Boolean.TRUE));
			this.mobileNoSortingOrder = this.ASCENDING_ORDER;
		}
		this.nameSortingOrder = this.UNSORTED;
		this.contactNameSortingOrder = UNSORTED;
		this.emailSortingOrder = UNSORTED;
		this.addressSortingOrder = UNSORTED;
		this.statusSortingOrder = UNSORTED;
	}
	
	public void sortByEmail(ActionEvent e)
	{
		if(this.emailSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.distributorVOList, new GenericComparator(Boolean.FALSE));
			this.emailSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.emailSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.distributorVOList, new GenericComparator("index", Boolean.TRUE));
			this.emailSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.distributorVOList, new GenericComparator("email", Boolean.TRUE));
			this.emailSortingOrder = this.ASCENDING_ORDER;
		}
		this.nameSortingOrder = this.UNSORTED;
		this.contactNameSortingOrder = UNSORTED;
		this.mobileNoSortingOrder = UNSORTED;
		this.addressSortingOrder = UNSORTED;
		this.statusSortingOrder = UNSORTED;
	}
	
	public void sortByAddress(ActionEvent e)
	{
		if(this.addressSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.distributorVOList, new GenericComparator(Boolean.FALSE));
			this.addressSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.addressSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.distributorVOList, new GenericComparator("index", Boolean.TRUE));
			this.addressSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.distributorVOList, new GenericComparator("address1", Boolean.TRUE));
			this.addressSortingOrder = this.ASCENDING_ORDER;
		}
		this.nameSortingOrder = this.UNSORTED;
		this.contactNameSortingOrder = UNSORTED;
		this.mobileNoSortingOrder = UNSORTED;
		this.emailSortingOrder = UNSORTED;
		this.statusSortingOrder = UNSORTED;
	}
	
	public void sortByStatus(ActionEvent e)
	{
		if(this.statusSortingOrder == this.ASCENDING_ORDER)
		{
			Collections.sort(this.distributorVOList, new GenericComparator(Boolean.TRUE));
			this.statusSortingOrder = this.DESCENDING_ORDER;
		}
		else if(this.statusSortingOrder == this.DESCENDING_ORDER)
		{
			Collections.sort(this.distributorVOList, new GenericComparator("index", Boolean.TRUE));
			this.statusSortingOrder = this.UNSORTED;
		}
		else
		{
			Collections.sort(this.distributorVOList, new GenericComparator("active", Boolean.FALSE));
			this.statusSortingOrder = this.ASCENDING_ORDER;
		}
		this.nameSortingOrder = this.UNSORTED;
		this.contactNameSortingOrder = UNSORTED;
		this.mobileNoSortingOrder = UNSORTED;
		this.emailSortingOrder = UNSORTED;
		this.addressSortingOrder = UNSORTED;
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
	        HSSFSheet sheet = wb.createSheet("Exported Agent Network WorkBook");
	        HSSFCellStyle cellStyle = getCellStyle(wb); 
	        
	        HSSFRow row = sheet.createRow(0);
	        
	        HSSFCell cell = row.createCell(Short.valueOf("0"));
	        cell.setCellValue("Agent Network");
	        cell.setCellStyle(cellStyle);
	        
	        
	        cell = row.createCell(Short.valueOf("1"));
	        cell.setCellValue("Contact Name");
	        cell.setCellStyle(cellStyle);
	        
	        cell = row.createCell(Short.valueOf("2"));
	        cell.setCellValue("Mobile#");
	        cell.setCellStyle(cellStyle);
	        
	        cell = row.createCell(Short.valueOf("3"));
	        cell.setCellValue("Email");
	        cell.setCellStyle(cellStyle);
	        
	        cell = row.createCell(Short.valueOf("4"));
	        cell.setCellValue("Address");
	        cell.setCellStyle(cellStyle);
	        
	        cell = row.createCell(Short.valueOf("5"));
	        cell.setCellValue("Status");
	        cell.setCellStyle(cellStyle);
	        
	        
	        int rowCounter = 1;
	        for(DistributorModel model: this.distributorVOList)
	        {
	        	row = sheet.createRow(rowCounter);
	        	rowCounter++;
	        	
	        	cell = row.createCell(Short.valueOf("0"));
		        cell.setCellValue(model.getName());
		        
		        cell = row.createCell(Short.valueOf("1"));
		        cell.setCellValue(model.getContactName());
		        
		        cell = row.createCell(Short.valueOf("2"));
		        cell.setCellValue(model.getPhoneNo());
		        
		        cell = row.createCell(Short.valueOf("3"));
		        cell.setCellValue(model.getEmail());
		        
		        cell = row.createCell(Short.valueOf("4"));
		        cell.setCellValue(model.getAddress1());
		        
		        cell = row.createCell(Short.valueOf("5"));
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
	        res.setHeader("Content-disposition", "attachment;filename=agent network data.xls");

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
	
	public String editDistributor()
	{
		if(logger.isDebugEnabled()){
			logger.debug("DistributorActionBean.editDistributor Starts");
		}
		String returnCode = null;
		try
		{
			String distributorId = (String)JSFContext.getFromRequest("distributorId");
			System.out.println("distributorId: " + distributorId);
			SearchBaseWrapper searchBaseWrapper = agentHierarchyManager.findDistributorsById(Long.valueOf(distributorId));
			DistributorVO vo = (DistributorVO)searchBaseWrapper.getBasePersistableModel();
			if(vo != null)
			{
				vo.setBankList(this.distributorVO.getBankList());
				vo.setMnoList(this.distributorVO.getMnoList());
				vo.setbankId(vo.getBankId());
				vo.setMnoId(vo.getMnoId());
				this.distributorVO = vo;
				this.distributorVO.setEditMode(Boolean.TRUE);
			}
			returnCode = Constants.ReturnCodes.SEARCH_DIST_ADD_DIST;
		}
		catch(FrameworkCheckedException fce)
		{
			logger.error(fce);
		}
		if(logger.isDebugEnabled()){
			logger.debug("DistributorActionBean.editDistributor End");
		}
		return returnCode;
	}
	
	public void clearAllSessionObjects(){
		if (!FacesContext.getCurrentInstance().isPostback()) {
			SessionBeanObjects.removeAllSessionObjects();
		}
		
		serverDate=PortalDateUtils.getServerDate();
	}

	public String addDistributor()
	{
		if(logger.isDebugEnabled()){
			logger.debug("DistributorActionBean.addDistributor Starts");
		}
		distributorVO= new DistributorVO();
		distributorVO.setEditMode(Boolean.FALSE);
		this.loadRefdata();
		return Constants.ReturnCodes.SEARCH_DIST_ADD_DIST;
	}
    
	public void searchDistributorsByCriteria(ActionEvent e)
	{
		if(logger.isDebugEnabled()){
			logger.debug("DistributorActionBean.searchDistributorsByCriteria Starts");
		}
		try
		{
			String name = this.distributorVO.getName();
			String phoneNo = this.distributorVO.getPhoneNo();
			String contactName = this.distributorVO.getContactName();
			String email = this.distributorVO.getEmail();
			List<SelectItem> distributorModels=this.distributorVO.getBankList();
			List<SelectItem> mnoModels=this.distributorVO.getMnoList();
			this.clearDistributorForm();
			this.distributorVO.setName(name);
			this.distributorVO.setPhoneNo(phoneNo);
			this.distributorVO.setContactName(contactName);
			this.distributorVO.setEmail(email);
			
			if(CommonUtils.isEmpty(this.distributorVO.getName()))
			{
				this.distributorVO.setName(null);
			}
			if(this.distributorVO.getPhoneNo().equals(""))
			{
				this.distributorVO.setPhoneNo(null);
			}
			if(this.distributorVO.getContactName().equals(""))
			{
				this.distributorVO.setContactName(null);
			}
			if(this.distributorVO.getEmail().equals(""))
			{
				this.distributorVO.setEmail(null);
			}			
			this.distributorVO.setActive(null);
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			searchBaseWrapper.setBasePersistableModel(distributorVO);
			agentHierarchyManager.searchDistributorsByCriteria(searchBaseWrapper);
			if(searchBaseWrapper.getCustomList() != null )
			{
				distributorVOList = searchBaseWrapper.getCustomList().getResultsetList();
				this.listSize = distributorVOList.size();
				for(DistributorModel model: distributorVOList)
				{
					model.setIndex(sortingPointer);
					sortingPointer++;
				}
			}
			else
			{
				this.listSize = 0;
				distributorVOList = new ArrayList<DistributorVO>();
				JSFContext.setInfoMessage("No Record Found.");
			}
			this.distributorVO.setBankList(distributorModels);
			this.distributorVO.setMnoList(mnoModels);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			logger.error(ex);
		}

		if(logger.isDebugEnabled()){
			logger.debug("DistributorActionBean.searchDistributorsByCriteria End");
		}
	}
	
	public void clearDistributorForm()
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("DistributorActionBean.clearDistributorForm Starts");
		}
		
		this.distributorVO = new DistributorVO();
		this.distributorVOList = new ArrayList<DistributorVO>();
		this.listSize = 0;
		this.nameSortingOrder = this.UNSORTED;
		this.contactNameSortingOrder = UNSORTED;
		this.mobileNoSortingOrder = UNSORTED;
		this.emailSortingOrder = UNSORTED;
		this.addressSortingOrder = UNSORTED;
		this.statusSortingOrder = UNSORTED;
		
		if(logger.isDebugEnabled()){
			logger.debug("DistributorActionBean.clearDistributorForm End");
		}
	}
		
	public List<DistributorVO> getdistributorVOList() {
		return distributorVOList;
	}


	public void setdistributorVOList(List<DistributorVO> distributorVOList) {
		this.distributorVOList = distributorVOList;
	}
	@PostConstruct
	public void ini()
	{
		this.loadRefdata();
		this.loadCountry();
		String createButton=PortalConstants.ADMIN_GP_CREATE+","+PortalConstants.PG_GP_CREATE+","+
                PortalConstants.MNG_AGNT_NET_CREATE;
        securityCheck=AuthenticationUtil.checkRightsIfAny(createButton);
        
        String updateButton=PortalConstants.ADMIN_GP_UPDATE+","+PortalConstants.PG_GP_UPDATE+","+
                PortalConstants.MNG_AGNT_NET_UPDATE;
        editSecurityCheck=AuthenticationUtil.checkRightsIfAny(updateButton);
	}
	public void loadRefdata(){
		if(logger.isDebugEnabled()){
			logger.debug("DistributorActionBean.loadRefdata Starts");
		}
		try {
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();		
			agentHierarchyManager.loadRefrenceData(searchBaseWrapper);
			List result= searchBaseWrapper.getCustomList().getResultsetList();
			this.papolateBankItems(((CustomList<BankModel>)result.get(0)).getResultsetList());
			this.papolateMNOItems(((CustomList<MnoModel>)result.get(1)).getResultsetList());
			
		} catch (FrameworkCheckedException e) {
			// TODO Auto-generated catch block
			logger.error(e);
		}
		if(logger.isDebugEnabled()){
			logger.debug("DistributorActionBean.loadRefdata End");
		}
	}
	public void papolateBankItems(List<BankModel> bankList){
		if(logger.isDebugEnabled()){
			logger.debug("DistributorActionBean.papolateBankItems Starts");
		}
		for(BankModel model:bankList){
			this.distributorVO.getBankList().add(new SelectItem(model.getBankId(),model.getName()));
		}	
		if(logger.isDebugEnabled()){
			logger.debug("DistributorActionBean.papolateBankItems End");
		}
	}
	
	public void papolateMNOItems(List<MnoModel> mnoList){	
		if(logger.isDebugEnabled()){
			logger.debug("DistributorActionBean.papolateMNOItems Starts");
		}
		for(MnoModel model:mnoList){
			if(model.getActive())
				this.distributorVO.getMnoList().add(new SelectItem(model.getMnoId(),model.getName()));
		}	
		if(logger.isDebugEnabled()){
			logger.debug("DistributorActionBean.papolateMNOItems End");
		}
	}
	
	/*public String next()
	{
		if(logger.isDebugEnabled()){
			logger.debug("DistributorActionBean.saveDistributor Starts");
		}
		String returnCode = Constants.ReturnCodes.DISTRIBUTOR_REGION;
		if(logger.isDebugEnabled()){
			logger.debug("DistributorActionBean.saveDistributor End");
		}
		return returnCode;
	}*/
	
	public void saveDistributor(){
		if(logger.isDebugEnabled()){
			logger.debug("DistributorActionBean.saveDistributor Starts");
		}
		try {
			if(validate(distributorVO)){
			
				if(null==distributorVO.getBankId() || distributorVO.getBankId()<1){
					distributorVO.setbankId(null);
				}
				if(null==distributorVO.getMnoId() || distributorVO.getMnoId()<1l){
					distributorVO.setMnoId(null);
				}
				
				
				DistributorModel model= new DistributorModel();
				model.setActive(distributorVO.getActive());
				model.setAddress1(distributorVO.getAddress1().trim());
				if( distributorVO.getAddress2()!= null)
				{
					model.setAddress2(distributorVO.getAddress2().trim());
				}
				if(distributorVO.getCity() != null)
				{
					model.setCity(distributorVO.getCity().trim());
				}
				
				if(distributorVO.getComments() != null)
				{
					model.setComments(distributorVO.getComments().trim());
				}
				
				model.setContactName(distributorVO.getContactName().trim());
				model.setCountry(distributorVO.getCountry());
				if(distributorVO.getDescription() != null)
				{
					model.setDescription(distributorVO.getDescription().trim());
				}
				
				if(distributorVO.getEmail() != null)
				{
					model.setEmail(distributorVO.getEmail().trim());
				}
				
				model.setFax(distributorVO.getFax());
				model.setName(distributorVO.getName().trim());
				model.setPhoneNo(distributorVO.getPhoneNo());
				if(distributorVO.getState() != null)
				{
					model.setState(distributorVO.getState().trim());
				}
				model.setVersionNo(distributorVO.getVersionNo());
				
				model.setNational(Boolean.FALSE);
				model.setNationalString("abc");
				model.setZip(distributorVO.getZip());
				model.setbankId(distributorVO.getBankId());
				model.setMnoId(distributorVO.getMnoId());
				if(distributorVO.isEditMode() || null!=distributorVO.getDistributorId())
				{
					model.setDistributorId(distributorVO.getDistributorId());
				}
				else
				{
					distributorVO.setCreatedOn(new Date());
					distributorVO.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
				}
				model.setUpdatedOn(new Date());
				model.setUpdatedBy(distributorVO.getCreatedBy());
				model.setCreatedBy(distributorVO.getCreatedBy());
				model.setCreatedOn(distributorVO.getCreatedOn());
				BaseWrapper baseWrapper = new BaseWrapperImpl();
				baseWrapper.setBasePersistableModel(model);					
				baseWrapper=agentHierarchyManager.saveOrUpdateDistributor(baseWrapper);
				distributorVO.setDistributorId(((DistributorModel)baseWrapper.getBasePersistableModel()).getDistributorId());
				distributorVO.setVersionNo(((DistributorModel)baseWrapper.getBasePersistableModel()).getVersionNo());
				distributorVO.setEditMode(Boolean.TRUE);
				JSFContext.setInfoMessage(Constants.InfoMessages.AH_DIST_DISTSAVE_INFO);
				logger.info(Constants.InfoMessages.AH_DIST_DISTSAVE_INFO);
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
				JSFContext.addErrorMessage(Constants.InfoMessages.AH_DIST_DISTNAME_INFO);
				logger.info(Constants.InfoMessages.AH_DIST_DISTNAME_INFO);
			}
		}
		if(logger.isDebugEnabled()){
			logger.debug("DistributorActionBean.saveDistributor End");
		}
	}
	
	public Boolean validate(DistributorVO distributorVO)
	{
		if(logger.isDebugEnabled()){
			logger.debug("DistributorActionBean.validate Starts");
		}
		boolean validated = Boolean.TRUE;
		if(CommonUtils.isEmpty(distributorVO.getName())){
			JSFContext.addErrorMessage(Constants.ErrorMessages.AH_DIST_DISTNAME_ERROR);
			validated = Boolean.FALSE;
		}
		if(distributorVO.getMnoId() == null || distributorVO.getMnoId() == 0L)
		{
			JSFContext.addErrorMessage(Constants.ErrorMessages.AH_DIST_SERVICE_OPERATOR_ERROR);
			validated = Boolean.FALSE;
		}
		if(CommonUtils.isEmpty(distributorVO.getContactName())){
			JSFContext.addErrorMessage(Constants.ErrorMessages.AH_DIST_CONTNAME_ERROR);
			validated = Boolean.FALSE;
		}
		if(CommonUtils.isEmpty(distributorVO.getAddress1())){
			JSFContext.addErrorMessage(Constants.ErrorMessages.AH_DIST_ADDRESS_ERROR);
			validated = Boolean.FALSE;
		}		
		if(!(distributorVO.getEmail().equals(null) || distributorVO.getEmail().equals("")) && !(EmailValidator.getInstance().isValid(distributorVO.getEmail()))){			
			JSFContext.addErrorMessage(Constants.ErrorMessages.AH_DIST_EMAILFORMAT_ERROR);
			validated = Boolean.FALSE;
		}	
		if(logger.isDebugEnabled()){
			logger.debug("DistributorActionBean.validate End");
		}
		return validated;
	}
	
	public void resetDistributorFields(ActionEvent e){
		if(logger.isDebugEnabled()){
			logger.debug("DistributorActionBean.resetDistributorFields Starts");
		}
		distributorVO.setActive(Boolean.FALSE);
		distributorVO.setName("");
		distributorVO.setContactName("");
		distributorVO.setAddress1("");
		distributorVO.setAddress2("");
		distributorVO.setCity("");
		distributorVO.setbankId(null);
		distributorVO.setMnoId(null);
		distributorVO.setPhoneNo("");
		distributorVO.setEmail("");
		distributorVO.setComments("");
		distributorVO.setFax("");
		distributorVO.setZip("");
		distributorVO.setDescription("");
		distributorVO.setState("");
		distributorVO.setCountry("");
		if(logger.isDebugEnabled()){
			logger.debug("DistributorActionBean.resetDistributorFields End");
		}
	}
	
	public void onChangeActive(AjaxBehaviorEvent event){
		if(this.distributorVO.isEditMode()){
			try {
				Boolean isExist=agentHierarchyManager.doesRegionsExistOfDistributor(this.distributorVO.getDistributorId());
				if(isExist){
					JSFContext.addErrorMessage(Constants.ErrorMessages.AH_DIST_ACTIVE_ERROR);
					this.distributorVO.setActive(Boolean.TRUE);
				}
			} catch (FrameworkCheckedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
	/*public void onChangeBank(AjaxBehaviorEvent event){
		this.distributorVO.setMnoId(0l);
	}
	public void onChangeMNO(AjaxBehaviorEvent event){
		this.distributorVO.setbankId(0l);
	}*/
	public void loadCountry(){
		try{
			this.countryItems.clear();
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

	public boolean isExportToXlsAllowed()
	{
	    return AuthenticationUtil.checkRightsIfAny( PortalConstants.EXPORT_XLS_READ );
	}

	public DistributorVO getDistributorVO() {
		return distributorVO;
	}

	public void setDistributorVO(DistributorVO distributorVO) {
		this.distributorVO = distributorVO;
	}

	public List<DistributorVO> getDistributorVOList() {
		return distributorVOList;
	}

	public void setDistributorVOList(List<DistributorVO> distributorVOList) {
		this.distributorVOList = distributorVOList;
	}
		
	public void setAgentHierarchyManager(AgentHierarchyManager agentHierarchyManager) {
		this.agentHierarchyManager = agentHierarchyManager;
	}
	public List<SelectItem> getCountryItems() {
		return countryItems;
	}

	public void setCountryItems(List<SelectItem> countryItems) {
		this.countryItems = countryItems;
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

	public int getNameSortingOrder() {
		return nameSortingOrder;
	}

	public void setNameSortingOrder(int nameSortingOrder) {
		this.nameSortingOrder = nameSortingOrder;
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

	public int getAddressSortingOrder() {
		return addressSortingOrder;
	}

	public void setAddressSortingOrder(int addressSortingOrder) {
		this.addressSortingOrder = addressSortingOrder;
	}

	public int getStatusSortingOrder() {
		return statusSortingOrder;
	}

	public void setStatusSortingOrder(int statusSortingOrder) {
		this.statusSortingOrder = statusSortingOrder;
	}

	public String getServerDate() {
		return serverDate;
	}

	public void setServerDate(String serverDate) {
		this.serverDate = serverDate;
	}
	
}
