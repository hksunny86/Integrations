package com.inov8.microbank.webapp.action.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import au.com.bytecode.opencsv.CSVReader;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.customermodule.BulkUpdateCustomerScreeningModel;
import com.inov8.microbank.common.vo.BulkFilerNonFilerVO;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.webapp.action.customermodule.ScreeningPerformedInvalidRecordsComparator;
import com.inov8.ola.util.StringUtil;

public class BulkUpdateFilerNonFilerController extends AdvanceFormController {

	@Autowired
	private AppUserManager userManager;
	
	public BulkUpdateFilerNonFilerController() {
		this.setCommandClass(BulkFilerNonFilerVO.class);
		this.setCommandName("bulkFilerNonFilerVO");
	}
	
	@Override
	protected Object loadFormBackingObject(HttpServletRequest arg0)
			throws Exception {
		return new BulkFilerNonFilerVO();
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest arg0) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest req,
			HttpServletResponse res, Object arg2, BindException errors)
			throws Exception {
		List<AppUserModel> appUserModelsList = new ArrayList<AppUserModel>();
		List<BulkFilerNonFilerVO>  bulkFilerNonFilerVOList = (List<BulkFilerNonFilerVO>) req.getSession().getAttribute("bulkFilerNonFilerVOList");
		
		if(bulkFilerNonFilerVOList.size() > 0){
			for(BulkFilerNonFilerVO vo : bulkFilerNonFilerVOList){
				if(vo.getIsValid()){
					vo.getAppUserModel().setFiler(vo.getIsFiler());
					vo.getAppUserModel().setUpdatedOn(new Date());
					appUserModelsList.add(vo.getAppUserModel());
				}
			}
		}else{
			this.saveMessage(req, "No Record(s) Found to Update");
			return super.showForm(req, res, errors);
		}
		try{
			if(appUserModelsList.size() > 0){
				userManager.saveOrUpdateAppUserModels(appUserModelsList);
				
			}else{

				this.saveMessage(req, "No Record(s) Found to Update");
				return super.showForm(req, res, errors);
			
			}
		}catch (FrameworkCheckedException ex){
				logger.error(ex.getMessage(), ex);
				this.saveMessage(req, "Some error occurred while updating the records");
				return super.showForm(req, res, errors);	
			}
		
		this.saveMessage(req, "Valid records updated successfully");
		return new ModelAndView(this.getSuccessView(), "bulkFilerNonFilerVO", new BulkFilerNonFilerVO());
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest request,
			HttpServletResponse response, Object arg2, BindException errors)
			throws Exception {

		BulkFilerNonFilerVO bulkFilerNonFilerVO = (BulkFilerNonFilerVO) arg2;
		MultipartFile mFile = bulkFilerNonFilerVO.getCsvFile();
		
		String name = mFile.getOriginalFilename();
		File file =new File(name); 
		
		
		List<BulkFilerNonFilerVO> bulkFilerNonFilerVOList = null;
		
		try{
			mFile.transferTo(file);
			String filePath = file.getAbsolutePath();
			bulkFilerNonFilerVOList = parseCSVFile(filePath);
			if(bulkFilerNonFilerVOList.size()<1){
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
		
		Collections.sort(bulkFilerNonFilerVOList, new UpdateFilerInvalidRecordsComparator());
		request.getSession().setAttribute("bulkFilerNonFilerVOList", bulkFilerNonFilerVOList);
		String view = "redirect:p-bulkupdatefilernonfilerpreview.html";
		return new ModelAndView(view);
		
	}
	
	private List<BulkFilerNonFilerVO> parseCSVFile(String filePath) throws IOException{

		
		CSVReader csvReader = new CSVReader(new FileReader(filePath));
		
		List <String> cnicList = new ArrayList<String>();
		List <String[]> rows = csvReader.readAll();
		int count=0;
		csvReader.close();
		
		List<BulkFilerNonFilerVO> bulkFilerNonFilerVOList = new ArrayList<>(rows.size());
		
		if(rows.size() > 0){
			rows.remove(0); // remove first row, as its title row.
			for(String [] row : rows){
				
				BulkFilerNonFilerVO bulkFilerNonFilerVO = new BulkFilerNonFilerVO();
				AppUserModel appUserModel = null;
				int rowLength = row.length;
				if(rowLength == 3){
					count++;
					
					try{
						bulkFilerNonFilerVO.setSrNo(row[0]);
						bulkFilerNonFilerVO.setCnic(row[1]);
						bulkFilerNonFilerVO.setFiler(row[2]);
						if(row[2].equals("1")){
							bulkFilerNonFilerVO.setIsFiler(true);
						}else if(row[2].equals("0")) {
							bulkFilerNonFilerVO.setIsFiler(false);
						}
						
						bulkFilerNonFilerVO.setIsValid(true);
					}catch(Exception exp){
						bulkFilerNonFilerVO.setIsValid(false);
						bulkFilerNonFilerVO.setDescription("Required Data is missing");
						continue;
					}
					
					bulkFilerNonFilerVOList.add(bulkFilerNonFilerVO);
					
					if(!StringUtils.isEmpty(row[1]==null?"":row[1].trim())){
						if(count > 1){
							Boolean isDuplicate = cnicList.contains(row[1]);
							if(isDuplicate){
								bulkFilerNonFilerVO.setIsValid(false);
								bulkFilerNonFilerVO.setDescription("Duplicate cnic in file");
								continue;
							}else{
								cnicList.add(row[1]);
							}
						}else{
							cnicList.add(row[1]);
						}
					}
					
					if(StringUtils.isEmpty(row[2]==null?"":row[2].trim())){
						bulkFilerNonFilerVO.setIsValid(false);
						bulkFilerNonFilerVO.setDescription("Filer is Required");
						continue;
					}else if (!row[2].equals("0") && !row[2].equals("1")){
						bulkFilerNonFilerVO.setIsValid(false);
						bulkFilerNonFilerVO.setDescription("Filer value is incorrect");
						continue;
					}
					
					
					if(StringUtils.isEmpty(row[1]==null?"":row[1].trim())){
						bulkFilerNonFilerVO.setIsValid(false);
						bulkFilerNonFilerVO.setDescription("CNIC is Required");
						continue;
					}else{
							try {
								
							appUserModel = userManager.loadAppUserByCnicAndType(row[1]);
							if(null == appUserModel){
								bulkFilerNonFilerVO.setIsValid(false);
								bulkFilerNonFilerVO.setDescription("InValid CNIC");
								continue;
							}else{
								bulkFilerNonFilerVO.setAppUserModel(appUserModel);
							}
						} catch (FrameworkCheckedException e) {
							bulkFilerNonFilerVO.setIsValid(false);
							bulkFilerNonFilerVO.setDescription("Error Occurred");
							continue;
						}
					}
				}else{
                    logger.debug("Data of this row in CSV file is not in  correct format  " + row.toString());
                    bulkFilerNonFilerVO.setIsValid(false);
                    bulkFilerNonFilerVO.setDescription("Data of this row in CSV file is not in  correct format");
                    try{
                    	bulkFilerNonFilerVO.setSrNo(row[0]);
                    	bulkFilerNonFilerVO.setCnic(row[1]);
						if(row[2].equals("1")){
							bulkFilerNonFilerVO.setIsFiler(true);
						}else if(row[2].equals("0")){
							bulkFilerNonFilerVO.setIsFiler(false);
						}
                    }catch(Exception e){
                    	bulkFilerNonFilerVO.setIsValid(false);	
                    }
                    
                    bulkFilerNonFilerVOList.add(bulkFilerNonFilerVO);
				}
					
			}
		}	
		return bulkFilerNonFilerVOList;
	
	}

	public void setUserManager(AppUserManager userManager) {
		this.userManager = userManager;
	}

}
