package com.inov8.microbank.webapp.action.customermodule;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import au.com.bytecode.opencsv.CSVReader;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.customermodule.BulkUpdateCustomerScreeningModel;
import com.inov8.microbank.common.model.portal.bulkdisbursements.BulkManualAdjustmentModel;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.customermodule.CustomerManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.webapp.action.portal.manualadjustmentmodule.BulkAdjustmentInvalidRecordsComparator;
import com.inov8.ola.util.StringUtil;

public class BulkUpdateCustomerScreeningController extends AdvanceFormController {
	
	@Autowired
	AppUserManager userManager;
	@Autowired
	CustomerManager customerManager;
	
	@SuppressWarnings("deprecation")
	public BulkUpdateCustomerScreeningController(){
		this.setCommandClass(BulkUpdateCustomerScreeningModel.class);
		this.setCommandName("bulkUpdateCustomerScreeningModel");
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest request)
			throws Exception {
		BulkUpdateCustomerScreeningModel model = new BulkUpdateCustomerScreeningModel();
		return model;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		List<CustomerModel> customerModelList = new ArrayList<CustomerModel>();
		List<Long> customerIdList = new ArrayList<Long>();
		List<Boolean> isScreeningPerformedList = new ArrayList<Boolean>();
		int ORACLE_LIMIT = 1000;
		
		List<BulkUpdateCustomerScreeningModel> list = (List<BulkUpdateCustomerScreeningModel>) request.getSession().getAttribute("bulkUpdateCustomerScreeningModelsLsit");
		
		if(list.size() > 0){
			for(BulkUpdateCustomerScreeningModel model : list){
				if(model.getIsValid()){
					customerIdList.add(Long.valueOf(model.getCustomerId()));
					isScreeningPerformedList.add(model.getIsScreeningPerformed());
				}
			}
	    		if(CollectionUtils.isNotEmpty(customerIdList))
	    		{
		    		if(customerIdList.size() <= ORACLE_LIMIT){
		    			customerModelList = customerManager.getCustomerModelListByCustomerIDs(customerIdList);	
		    		}else{//handle oracle limit of 1000 IN CLAUSE
		    			List<Long>[] chunks = this.chunks(customerIdList, ORACLE_LIMIT);
		    			for (List<Long> inClauseByThousands : chunks) {
		    				customerModelList.addAll(customerManager.getCustomerModelListByCustomerIDs(inClauseByThousands));
		    				
		    			}
		    		}
	    		}else{
				this.saveMessage(request, "No Record(s) found");
				return super.showForm(request, response, errors);
			}
		}else{
			this.saveMessage(request, "No Record(s) found");
			return super.showForm(request, response, errors);
		}
		
		try{
			if(CollectionUtils.isNotEmpty(customerModelList)){
				for(int i=0 ; i<customerModelList.size() ; i++){
					customerModelList.get(i).setUpdatedOn(new Date());
					customerModelList.get(i).setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
					customerModelList.get(i).setScreeningPerformed(isScreeningPerformedList.get(i));
				}
				customerManager.updateCustomerModels(customerModelList);
			}
		}catch(FrameworkCheckedException exp){
			this.saveMessage(request, "Error occurred while updating records");
			return super.showForm(request, response, errors);
		}

		
		this.saveMessage(request, "Valid records updated successfully");
		return new ModelAndView(this.getSuccessView(), "bulkUpdateCustomerScreeningModel", new BulkUpdateCustomerScreeningModel());
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		BulkUpdateCustomerScreeningModel bulkUpdateCustomerScreeningModel = (BulkUpdateCustomerScreeningModel) command;
		MultipartFile mFile = bulkUpdateCustomerScreeningModel.getCsvFile();
		
		String name = mFile.getOriginalFilename();
		File file =new File(name); 
		
		
		List<BulkUpdateCustomerScreeningModel> bulkUpdateCustomerScreeningModelsLsit = null;
		
		try{
			mFile.transferTo(file);
			String filePath = file.getAbsolutePath();
			bulkUpdateCustomerScreeningModelsLsit = parseCSVFile(filePath);
			if(bulkUpdateCustomerScreeningModelsLsit.size()<1){
				this.saveMessage(request, "No Record Found!");
				return super.showForm(request, response, errors);
			}
			try{
				file.delete(); // delete temporary file...
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}catch (Exception ex){
			logger.error(ex.getMessage(), ex);
			this.saveMessage(request, "Some error occurred while processing the CSV file");
			return super.showForm(request, response, errors);	
		}
		
		Collections.sort(bulkUpdateCustomerScreeningModelsLsit, new ScreeningPerformedInvalidRecordsComparator());
		request.getSession().setAttribute("bulkUpdateCustomerScreeningModelsLsit", bulkUpdateCustomerScreeningModelsLsit);
		String view = "redirect:p-updatecustomerscreeningpreview.html";
		return new ModelAndView(view);
		
	}
	
	private List<BulkUpdateCustomerScreeningModel> parseCSVFile(String filePath) throws FileNotFoundException, IOException, ParseException{
		
		CSVReader csvReader = new CSVReader(new FileReader(filePath));
		
		List <String[]> rows = csvReader.readAll();
		csvReader.close();
		
		List<BulkUpdateCustomerScreeningModel> bulkUpdateCustomerScreeningModelsList = new ArrayList<>(rows.size());
		List <String> cnicList = new ArrayList<String>();
		int count=0;
		
		if(rows.size() > 0){
			rows.remove(0); // remove first row, as its title row.
			for(String [] row : rows){
				
				BulkUpdateCustomerScreeningModel bulkUpdateCustomerScreeningModel = new BulkUpdateCustomerScreeningModel();
				String customerId = null;
				int rowLength = row.length;
				if(rowLength == 3){
					count++;
					
					try{
						bulkUpdateCustomerScreeningModel.setSrNo(row[0]);
						bulkUpdateCustomerScreeningModel.setCnic(row[1]);
						bulkUpdateCustomerScreeningModel.setScreeningPerformed(row[2]);
						if(row[2].equals("1")){
							bulkUpdateCustomerScreeningModel.setIsScreeningPerformed(true);
							bulkUpdateCustomerScreeningModel.setScreeningPerformed("Yes");
						}else if(row[2].equals("0")) {
							bulkUpdateCustomerScreeningModel.setIsScreeningPerformed(false);
							bulkUpdateCustomerScreeningModel.setScreeningPerformed("No");
						}
						
						bulkUpdateCustomerScreeningModel.setIsValid(true);
					}catch(Exception exp){
						bulkUpdateCustomerScreeningModel.setIsValid(false);
						bulkUpdateCustomerScreeningModel.setDescription("Required Data is missing");
						continue;
					}
					
					bulkUpdateCustomerScreeningModelsList.add(bulkUpdateCustomerScreeningModel);
					
					if(!StringUtils.isEmpty(row[1]==null?"":row[1].trim())){
						if(count > 1){
							Boolean isDuplicate = cnicList.contains(row[1]);
							if(isDuplicate){
								bulkUpdateCustomerScreeningModel.setIsValid(false);
								bulkUpdateCustomerScreeningModel.setDescription("Duplicate cnic in file");
								continue;
							}else{
								cnicList.add(row[1]);
							}
						}else{
							cnicList.add(row[1]);
						}
					}
					
					if(StringUtils.isEmpty(row[2]==null?"":row[2].trim())){
						bulkUpdateCustomerScreeningModel.setIsValid(false);
						bulkUpdateCustomerScreeningModel.setDescription("Screening Performed is Required");
						continue;
					}else if (!row[2].equals("0") && !row[2].equals("1")){
						bulkUpdateCustomerScreeningModel.setIsValid(false);
						bulkUpdateCustomerScreeningModel.setDescription("Screening Performed value is incorrect");
						continue;
					}
					
					
					if(StringUtils.isEmpty(row[1]==null?"":row[1].trim())){
						bulkUpdateCustomerScreeningModel.setIsValid(false);
						bulkUpdateCustomerScreeningModel.setDescription("CNIC is Required");
						continue;
					}else{
						try {
							Boolean isValid = CommonUtils.isValidCnic(row[1]);
							if(isValid){
								customerId = userManager.getCustomerId(row[1]);
								if(StringUtil.isNullOrEmpty(customerId)){
									bulkUpdateCustomerScreeningModel.setIsValid(false);
									bulkUpdateCustomerScreeningModel.setDescription("CNIC doesn't exist");
									continue;
								}else{
									bulkUpdateCustomerScreeningModel.setCustomerId(customerId);
								}
							}else{
								bulkUpdateCustomerScreeningModel.setIsValid(false);
								bulkUpdateCustomerScreeningModel.setDescription("Invalid CNIC");
								continue;
								
							}} catch (Exception e) {
								bulkUpdateCustomerScreeningModel.setIsValid(false);
								bulkUpdateCustomerScreeningModel.setDescription("CNIC doesn't exist");
								continue;
							}
							
							
					}
				}else{
                    logger.debug("Data of this row in CSV file is not in  correct format  " + row.toString());
                    bulkUpdateCustomerScreeningModel.setIsValid(false);
                    bulkUpdateCustomerScreeningModel.setDescription("Data of this row in CSV file is not in  correct format");
                    try{
                    	bulkUpdateCustomerScreeningModel.setSrNo(row[0]);
						bulkUpdateCustomerScreeningModel.setCnic(row[1]);
						if(row[2].equals("1")){
							bulkUpdateCustomerScreeningModel.setIsScreeningPerformed(true);
						}else if(row[2].equals("0")){
							bulkUpdateCustomerScreeningModel.setIsScreeningPerformed(false);
						}
                    }catch(Exception e){
                    	 bulkUpdateCustomerScreeningModel.setIsValid(false);	
                    }
                    
                    bulkUpdateCustomerScreeningModelsList.add(bulkUpdateCustomerScreeningModel);
				}
					
			}
		}	
		return bulkUpdateCustomerScreeningModelsList;
	}

	private List[] chunks(final List<Long> pList, final int pSize)  
    {  
        if(pList == null || pList.size() == 0 || pSize == 0) return new List[] {};  
        if(pSize < 0) return new List[] { pList };  
   
        // Calculate the number of batches  
        int numBatches = (pList.size() / pSize) + 1;  
   
        // Create a new array of Lists to hold the return value  
        List[] batches = new List[numBatches];  
   
        for(int index = 0; index < numBatches; index++)  
        {  
            int count = index + 1;  
            int fromIndex = Math.max(((count - 1) * pSize), 0);  
            int toIndex = Math.min((count * pSize), pList.size());  
            batches[index] = pList.subList(fromIndex, toIndex);  
        }  
   
        return batches;  
    }

	public void setAppUserManager(AppUserManager userManager) {
		this.userManager = userManager;
	}

	public void setCustomerManager(CustomerManager customerManager) {
		this.customerManager = customerManager;
	}

}
