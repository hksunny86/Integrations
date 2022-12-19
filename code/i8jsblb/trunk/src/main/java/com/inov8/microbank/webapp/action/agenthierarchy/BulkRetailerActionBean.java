package com.inov8.microbank.webapp.action.agenthierarchy;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

import au.com.bytecode.opencsv.CSVReader;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.RetailerModel;
import com.inov8.microbank.common.model.agenthierarchy.RegionModel;
import com.inov8.microbank.common.util.Constants;
import com.inov8.microbank.common.util.JSFContext;
import com.inov8.microbank.common.util.SessionBeanObjects;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.agenthierarchy.AgentHierarchyManager;

@ManagedBean(name="bulkRetailerBean")
@SessionScoped
public class BulkRetailerActionBean {
	
	private static final Log logger= LogFactory.getLog(BulkRetailerActionBean.class);
	
	@ManagedProperty(value="#{agentHierarchyManager}")
	private AgentHierarchyManager agentHierarchyManager;
	
	@ManagedProperty(value="#{referenceDataManager}")
	private ReferenceDataManager referenceDataManager;
	
	
	private List<SelectItem> distributorItems=new ArrayList<SelectItem>();
	private List<SelectItem> regionItems= new ArrayList<SelectItem>();
	private RetailerModel retailerModel=new RetailerModel();
	
	private ArrayList<UploadedFile> files = new ArrayList<UploadedFile>(0);
	private List<String[]> rows = new ArrayList<String[]>(0);
	private List<RetailerModel> validatedRetailerModels = new ArrayList<RetailerModel>(0);
	private List<RetailerModel> failedRetailerModels = new ArrayList<RetailerModel>(0);
	private List<String> errorList = new ArrayList<String>();
	private Boolean createMode = false;
	
	public void uploadFile(FileUploadEvent event) throws Exception {
        this.errorList.clear();
		UploadedFile item = event.getUploadedFile();
        files.add(item);
		CSVReader reader = new CSVReader(new InputStreamReader(item.getInputStream()));
        this.rows = reader.readAll();
        rows.remove(0);
        reader.close();
	}
	
	public void validateBulkFranchise() throws Exception {
		this.errorList.clear();
		this.setCreateMode(false);
		boolean validate = true;
		boolean createAllFranchisesInactive = false;
		String distributorName = "";
        String regionName = "";
		
        this.validatedRetailerModels.clear();
        this.failedRetailerModels.clear();
		
		if(this.retailerModel.getDistributorId()<1){
			validate= Boolean.FALSE;
			JSFContext.addErrorMessage(Constants.ErrorMessages.AH_FRANCHISE_DISTNAME_ERROR);
		}else{
			for(SelectItem item: this.distributorItems){
				if(((Long)item.getValue()).longValue() == retailerModel.getDistributorId().longValue()){
	                distributorName = item.getLabel();
					break;
				}
			}
		}
		if(this.retailerModel.getRegionModelId()<1){
			validate= Boolean.FALSE;
			JSFContext.addErrorMessage(Constants.ErrorMessages.AH_FRANCHISE_REGION_NAME_REQ_ERROR);
		}else{
			for(SelectItem item: this.regionItems){
				if(((Long)item.getValue()).longValue() == retailerModel.getRegionModelId().longValue()){
	                regionName = item.getLabel();
					break;
				}
			}
		}
		if(files == null || files.size() == 0){
			validate= Boolean.FALSE;
			JSFContext.addErrorMessage("File not uploaded.");
        }
		
		if(!validate){
			return;
		}
		
		// if region is inActive then create all franchises in inActive state
		if(!isActiveRegion(retailerModel.getRegionModelId())){
			createAllFranchisesInactive = true;
		}
		
		Set<String> nameList = new HashSet<String>();
		
        for (String[] row : rows) {
            int rowLength = row.length;
        	RetailerModel model = new RetailerModel();
        	model.setSerialNo(row[0]);
            if (rowLength == 21) {
                model.setValidRecord(true);
                    
                if(isEmpty(row[1])){
                	model.setValidRecord(false);
                	model.setFailureReason("Franchise/Branch Name Missing");
                	failedRetailerModels.add(model);
                	continue;
                }else if(row[1].length() > 50){
                	model.setValidRecord(false);
                	model.setFailureReason("Franchise/Branch Name cannot be more than 50 characters");
                	failedRetailerModels.add(model);
                	continue;
                }else{
                	model.setName(row[1].trim());
                }
                
                if(isEmpty(row[2])){
                	model.setValidRecord(false);
                	model.setFailureReason("Contact Name Missing");
                	failedRetailerModels.add(model);
                	continue;
                }else if(row[2].length() > 50){
                	model.setValidRecord(false);
                	model.setFailureReason("Contact Name cannot be more than 50 characters");
                	failedRetailerModels.add(model);
                	continue;
                }else{
                	model.setContactName(row[2].trim());
                }

                if(isEmpty(row[3])){
                	model.setValidRecord(false);
                	model.setFailureReason("Province Missing");
                	failedRetailerModels.add(model);
                	continue;
                }else if(row[3].length() > 50){
                	model.setValidRecord(false);
                	model.setFailureReason("Province cannot be more than 50 characters");
                	failedRetailerModels.add(model);
                	continue;
                }else{
                	model.setState(row[3].trim());
                }

                if(isEmpty(row[4])){
                	model.setValidRecord(false);
                	model.setFailureReason("City Missing");
                	failedRetailerModels.add(model);
                	continue;
                }else if(row[4].length() > 50){
                	model.setValidRecord(false);
                	model.setFailureReason("City cannot be more than 50 characters");
                	failedRetailerModels.add(model);
                	continue;
                }else{
                	model.setCity(row[4].trim());
                }

                if(isEmpty(row[5])){
                	model.setValidRecord(false);
                	model.setFailureReason("Current Address Missing");
                	failedRetailerModels.add(model);
                	continue;
                }else if(row[5].length() > 250){
                	model.setValidRecord(false);
                	model.setFailureReason("Current Address cannot be more than 250 characters");
                	failedRetailerModels.add(model);
                	continue;
                }else{
                	model.setAddress1(row[5].trim());
                }

                if(!isEmpty(row[6])){
                	if(row[6].length() > 250){
                    	model.setValidRecord(false);
                    	model.setFailureReason("Permanent Address cannot be more than 250 characters");
                    	failedRetailerModels.add(model);
                    	continue;
                    }else{
                    	model.setAddress2(row[6].trim());
                    }
                }
                
                if(isEmpty(row[7])){
                	model.setValidRecord(false);
                	model.setFailureReason("Contact No Missing");
                	failedRetailerModels.add(model);
                	continue;
                }else if(!StringUtils.isNumeric(row[7])){
                    	model.setValidRecord(false);
                    	model.setFailureReason("Contact No can only be numeric");
                    	failedRetailerModels.add(model);
                    	continue;
                }else if(row[7].trim().length() == 10){
              	  	String mob = "0" + row[7].trim();
              	  	model.setPhoneNo(mob);
                }else if(row[7].trim().length() == 11){
              	  	model.setPhoneNo(row[7].trim());
                }else{
                	model.setValidRecord(false);
                	model.setFailureReason("Invalid Contact No");
                	failedRetailerModels.add(model);
                	continue;
                }

                if(!isEmpty(row[8])){
                	if(!StringUtils.isNumeric(row[8])){
	                	model.setValidRecord(false);
	                	model.setFailureReason("Fax can only be numeric");
	                	failedRetailerModels.add(model);
	                	continue;
                	}else if(row[8].length() > 13){
                    	model.setValidRecord(false);
                    	model.setFailureReason("Fax cannot be more than 13 characters");
                    	failedRetailerModels.add(model);
                    	continue;
                	}else if(row[8].startsWith("0")){
                    	model.setFax(row[8].trim());
              	  	}else{
                    	model.setFax("0" + row[8].trim());
              	  	}
                }
                
                if(!isEmpty(row[9])){
                	if(!StringUtils.isNumeric(row[9])){
	                	model.setValidRecord(false);
	                	model.setFailureReason("Zip can only be numeric");
	                	failedRetailerModels.add(model);
	                	continue;
                	}else if(row[9].length() > 9){
                    	model.setValidRecord(false);
                    	model.setFailureReason("Zip Code cannot be more than 9 characters");
                    	failedRetailerModels.add(model);
                    	continue;
                	}else{
              	  		model.setZip(row[9].trim());
                	}
                }
                
                if(!isEmpty(row[10])){
                	if(row[10].length() > 50){
                    	model.setValidRecord(false);
                    	model.setFailureReason("Email cannot be more than 9 characters");
                    	failedRetailerModels.add(model);
                    	continue;
                	}else{
                  	  	model.setEmail(row[10].trim());
                	}
                }
                
                if(!isEmpty(row[11])){
                	if(row[11].length() > 250){
                    	model.setValidRecord(false);
                    	model.setFailureReason("Description cannot be more than 250 characters");
                    	failedRetailerModels.add(model);
                    	continue;
                	}else{
                  	  	model.setDescription(row[11].trim());
                	}
                }
                
                if(!isEmpty(row[12])){
                	if(row[12].length() > 250){
                    	model.setValidRecord(false);
                    	model.setFailureReason("Comments cannot be more than 250 characters");
                    	failedRetailerModels.add(model);
                    	continue;
                	}else{
                  	  	model.setComments(row[12].trim());
                	}
                }
                
                if(!isEmpty(row[13]) && row[13].trim().equals("1")){
                	model.setActive(createAllFranchisesInactive?false:true);
                }else{
                	model.setActive(false);
                }

                if(!isEmpty(row[14])){
                	if(row[14].length() > 50){
                    	model.setValidRecord(false);
                    	model.setFailureReason("Group Email cannot be more than 50 characters");
                    	failedRetailerModels.add(model);
                    	continue;
                	}else{
                  	  	model.setGroupEmail(row[14].trim());
                	}
                }
                
                if(!isEmpty(row[15])){
                	if(row[15].length() > 250){
                    	model.setValidRecord(false);
                    	model.setFailureReason("Group Description cannot be more than 250 characters");
                    	failedRetailerModels.add(model);
                    	continue;
                	}else{
                  	  	model.setGroupDesc(row[15].trim());
                	}
                }
                
                if(!isEmpty(row[16]) && row[16].trim().equals("1")){
                	model.setGroupActive(true);
                }else{
                	model.setGroupActive(false);
                }

                if(!isEmpty(row[17]) && row[17].trim().equals("1")){
                	model.setPermissionCreate(true);
                }else{
                	model.setPermissionCreate(false);
                }

                if(!isEmpty(row[18]) && row[18].trim().equals("1")){
                	model.setPermissionRead(true);
                }else{
                	model.setPermissionRead(false);
                }

                if(!isEmpty(row[19]) && row[19].trim().equals("1")){
                	model.setPermissionUpdate(true);
                }else{
                	model.setPermissionUpdate(false);
                }

                if(!isEmpty(row[20]) && row[20].trim().equals("1")){
                	model.setPermissionDelete(true);
                }else{
                	model.setPermissionDelete(false);
                }
                //synchronizing permissions
            	if(model.getPermissionCreate()){
                	model.setPermissionUpdate(true);
                	model.setPermissionRead(true);
                	model.setPermissionDelete(true);
            	}else if(model.getPermissionDelete()){
                	model.setPermissionUpdate(true);
                	model.setPermissionRead(true);
            	}else if(model.getPermissionUpdate()){
                	model.setPermissionRead(true);
            	}
                
                model.setDistributorId(retailerModel.getDistributorId());
                model.setDistributorName(distributorName);
                model.setRegionModelId(retailerModel.getRegionModelId());
                model.setRegionName(regionName);
                model.setCountry("1"); // 1 = Pakistan
                model.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                model.setCreatedByName(UserUtils.getCurrentUser().getFirstName()+" "+UserUtils.getCurrentUser().getLastName());
                model.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                model.setUpdatedOn(new Date());
                model.setCreatedOn(new Date());

    			if(!nameList.add(model.getName())){
    				model.setValidRecord(false);
                	model.setFailureReason("Duplicate Franchise/Branch Name in upload file");
                	failedRetailerModels.add(model);
                	continue;
    			}else if(agentHierarchyManager.isAlreadyExistsFranchiseName(model.getName())){
    				model.setValidRecord(false);
                	model.setFailureReason("Franchise/Branch Name already exists");
                	failedRetailerModels.add(model);
                	continue;
    			}
                
                validatedRetailerModels.add(model);
            } else {
            	model.setFailureReason("Inconsistant data");
            	failedRetailerModels.add(model);
            	logger.debug("Franchise Row Skipped, inconsistant data found at serial No: " + model.getSerialNo());
            }
        }

		if(validatedRetailerModels.size() == 0){
			JSFContext.addErrorMessage("No valid record found.");
			if(failedRetailerModels.size() > 0){
//				JSFContext.addErrorMessage("Failed Records details:");
				errorList.add("Failed Records details:");
				for(RetailerModel mod: failedRetailerModels){
//					JSFContext.addErrorMessage("Sr No. "+mod.getSerialNo()+" - " + mod.getFailureReason());
					errorList.add("Sr No. "+mod.getSerialNo()+" - " + mod.getFailureReason());
				}
			}

		}else{
			this.setCreateMode(true);
			if(failedRetailerModels.size() == 0){
				JSFContext.setInfoMessage("All " + validatedRetailerModels.size() + " Franchise(s) validated successfully.");
			}else{
				JSFContext.setInfoMessage(validatedRetailerModels.size() + " Franchise(s) validated successfully");
//				JSFContext.addErrorMessage("Failed Records details:");
				errorList.add("Failed Records details:");
				for(RetailerModel mod: failedRetailerModels){
//					JSFContext.addErrorMessage("Sr No. "+mod.getSerialNo()+" - " + mod.getFailureReason());
					errorList.add("Sr No. "+mod.getSerialNo()+" - " + mod.getFailureReason());
				}
			}
		}
    }	 

	public void processBulkFranchise() throws Exception {
		this.errorList.clear();
		if(validatedRetailerModels.size() == 0){
			JSFContext.addErrorMessage("No valid record found.");
		}else{
			List<RetailerModel> failedList = agentHierarchyManager.saveFranchiseBulk(validatedRetailerModels);
			if(validatedRetailerModels.size() == failedList.size()){
				JSFContext.addErrorMessage("Failed to create franchise(s).");
				failedRetailerModels.addAll(failedList);
//				JSFContext.addErrorMessage("Failed Records details:");
				errorList.add("Failed Records details:");
				for(RetailerModel mod: failedRetailerModels){
//					JSFContext.addErrorMessage("Sr No: "+mod.getSerialNo()+" Problem: " + mod.getFailureReason());
					errorList.add("Sr No: "+mod.getSerialNo()+" Problem: " + mod.getFailureReason());
				}
			}else{
				failedRetailerModels.addAll(failedList);
				if(failedRetailerModels.size() == 0){
					JSFContext.setInfoMessage("All " + validatedRetailerModels.size() + " Franchise(s) are sent for creation. Please check back in a little while.");
				}else{
					JSFContext.setInfoMessage((validatedRetailerModels.size() - failedList.size()) + " Franchise(s) are sent for creation. Please check back in a little while.");
//					JSFContext.addErrorMessage("Failed Records details:");
					errorList.add("Failed Records details:");
					for(RetailerModel mod: failedRetailerModels){
//						JSFContext.addErrorMessage("Sr No: "+mod.getSerialNo()+" Problem: " + mod.getFailureReason());
						errorList.add("Sr No: "+mod.getSerialNo()+" Problem: " + mod.getFailureReason());
					}
				}
			}
			this.setCreateMode(false);
			this.rows.clear();
	        this.files.clear();
	        this.validatedRetailerModels.clear();
	        this.failedRetailerModels.clear();
	        this.retailerModel = new RetailerModel();
		}
	}
	
	private boolean isActiveRegion(Long regionId) throws FrameworkCheckedException{
		boolean result = true;
		try{
			RegionModel regionModel = new RegionModel();
			regionModel.setRegionId(regionId);
			ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(regionModel, "regionName", SortingOrder.ASC);
			referenceDataWrapper.setBasePersistableModel(regionModel);
			referenceDataManager.getReferenceData(referenceDataWrapper);
			List<RegionModel> regionModelList = null;
			if (referenceDataWrapper.getReferenceDataList() != null) {
				regionModelList = referenceDataWrapper.getReferenceDataList();
				regionModel = regionModelList.get(0);
				if(!regionModel.getActive()){
					result = false;
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return result;
	}

	public void clearUploadForm(){
		this.setCreateMode(false);
		this.retailerModel= new RetailerModel();
		this.regionItems= new ArrayList<SelectItem>();
        this.files.clear();
		this.rows.clear();
        this.validatedRetailerModels.clear();
        this.failedRetailerModels.clear();
        this.errorList.clear();
	}

	public String clearUploadData() {
        this.errorList.clear();
        files.clear();
        return null;
    }
	
	public int getSize() {
        if (getFiles().size() > 0) {
            return getFiles().size();
        } else {
            return 0;
        }
    }
	
	private boolean isEmpty(String str){
		return (str==null || str.trim().equals(""))?true:false;
	}
	
	
	public BulkRetailerActionBean(){		
				
	}

	public String cancel(){
		SessionBeanObjects.removeAllSessionObjects();
		return Constants.ReturnCodes.CANCEL_FRANCHISE;
	}

	@PostConstruct
	public void loadSearchrefData(){
		if(logger.isDebugEnabled()){
			logger.debug("BulkRetailerActionBean.loadSearchrefData Starts");
		}
        this.errorList.clear();
		try {
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			searchBaseWrapper= agentHierarchyManager.loadFranchiseSearchRefData(searchBaseWrapper);
			List<DistributorModel> distributorModels = searchBaseWrapper.getCustomList().getResultsetList();
			this.distributorItems=new ArrayList<SelectItem>();
			for(DistributorModel model:distributorModels){
				this.distributorItems.add(new SelectItem(model.getDistributorId(),model.getName()));
			}
		} catch (FrameworkCheckedException e) {
			// TODO Auto-generated catch block
			logger.error(e);//e.printStackTrace();
		}
		if(logger.isDebugEnabled()){
			logger.debug("BulkRetailerActionBean.loadSearchrefData End");
		}
	}

	public void clearAllSessionObjects(){
		if (!FacesContext.getCurrentInstance().isPostback()) {
			SessionBeanObjects.removeAllSessionObjects();
		}
	}

	public void onChangeDistributor(AjaxBehaviorEvent e){
		try {
			SearchBaseWrapper searchBaseWrapper;
			searchBaseWrapper=agentHierarchyManager.findRegionsByDistributorId(retailerModel.getDistributorIdDistributorModel().getDistributorId());
			List<RegionModel> regionModels = searchBaseWrapper.getCustomList().getResultsetList();
			this.regionItems=new ArrayList<SelectItem>();
			for(RegionModel model: regionModels){
				this.regionItems.add(new SelectItem(model.getRegionId(),model.getRegionName()));
			}
		} catch (FrameworkCheckedException e1) {
			e1.printStackTrace();
		}
	}
	
	public RetailerModel getRetailerModel() {
		return retailerModel;
	}

	public void setRetailerModel(RetailerModel retailerModel) {
		this.retailerModel = retailerModel;
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

	public ArrayList<UploadedFile> getFiles() {
		return files;
	}

	public void setFiles(ArrayList<UploadedFile> files) {
		this.files = files;
	}

	public List<String[]> getRows() {
		return rows;
	}

	public void setRows(List<String[]> rows) {
		this.rows = rows;
	}

	public List<RetailerModel> getValidatedRetailerModels() {
		return validatedRetailerModels;
	}

	public void setValidatedRetailerModels(
			List<RetailerModel> validatedRetailerModels) {
		this.validatedRetailerModels = validatedRetailerModels;
	}

	public List<RetailerModel> getFailedRetailerModels() {
		return failedRetailerModels;
	}

	public void setFailedRetailerModels(List<RetailerModel> failedRetailerModels) {
		this.failedRetailerModels = failedRetailerModels;
	}

	public Boolean getCreateMode() {
		return createMode;
	}

	public void setCreateMode(Boolean createMode) {
		this.createMode = createMode;
	}
	
	public List<String> getErrorList() {
		return errorList;
	}

	public void setErrorList(List<String> errorList) {
		this.errorList = errorList;
	}

}
