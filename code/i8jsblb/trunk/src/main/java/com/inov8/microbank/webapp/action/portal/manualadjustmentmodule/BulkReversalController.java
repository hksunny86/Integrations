package com.inov8.microbank.webapp.action.portal.manualadjustmentmodule;

import au.com.bytecode.opencsv.CSVReader;
import com.inov8.microbank.common.model.BBAccountsViewModel;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.portal.bulkdisbursements.BulkAutoReversalModel;
import com.inov8.microbank.common.model.portal.bulkdisbursements.BulkManualAdjustmentModel;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.server.service.manualadjustmentmodule.ManualAdjustmentManager;
import com.inov8.microbank.server.service.manualadjustmentmodule.ManualReversalManager;
import com.inov8.microbank.webapp.action.authorizationmodule.AdvanceAuthorizationFormController;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

//import com.inov8.microbank.common.model.BulkManualAdjustmentModel;

public class BulkReversalController extends AdvanceAuthorizationFormController{

	private ManualReversalManager manualReversalManager;

	private ManualAdjustmentManager manualAdjustmentManager;

	public BulkReversalController() {
		setCommandName("bulkAutoReversalModel");
		setCommandClass(BulkAutoReversalModel.class);
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
		// TODO Auto-generated method stub
		BulkAutoReversalModel model = new BulkAutoReversalModel();
		return model;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {



		List<BulkAutoReversalModel> bulkAutoReversalModelList = null;
		Long batchId =null;
		
		
		try{
			List<BulkAutoReversalModel> dis = (List<BulkAutoReversalModel>) request.getSession().getAttribute("bulkAutoReversalModelList");
			bulkAutoReversalModelList = new ArrayList<>(dis.size());
			for(BulkAutoReversalModel bulkAutoReversalModel : dis ){
				if(bulkAutoReversalModel.getIsValid() == true){
					bulkAutoReversalModel.setApproved(true);
					bulkAutoReversalModelList.add(bulkAutoReversalModel);
				}
							
			}
			request.getSession().removeAttribute("bulkAutoReversalModelList");
			if(bulkAutoReversalModelList != null && bulkAutoReversalModelList.size() > 0){
				batchId = bulkAutoReversalModelList.get(0).getBatchId();
				manualReversalManager.createBulkReversal(bulkAutoReversalModelList);
			}
			else
			{
				this.saveMessage(request, "No Record(s) found");
				return super.showForm(request, response, errors);	
			}
		}catch (Exception ex){
			logger.error(ex.getMessage(), ex);
			this.saveMessage(request, "Some error occurred while processing the CSV file");
			return super.showForm(request, response, errors);	
		}
		
		this.saveMessage(request, "Valid records saved successfully against  Batch ID: "+batchId);
		return new ModelAndView(this.getSuccessView(), "bulkAutoReversalModel", new BulkAutoReversalModel());
	
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		BulkAutoReversalModel bulkAutoReversalModel = (BulkAutoReversalModel) command;
		MultipartFile csvFile = bulkAutoReversalModel.getCsvFile();
		String name = csvFile.getOriginalFilename();
		String rand = UUID.randomUUID().toString();
		File f = new File(name);
		
		List<BulkAutoReversalModel> bulkAutoReversalModelList = null;
		
		try{
			csvFile.transferTo(f);
			String filePath = f.getAbsolutePath();
			bulkAutoReversalModelList = parseCSVFile(filePath);
			if(bulkAutoReversalModelList.size()<1){
				this.saveMessage(request, "No Record Found!");
				return super.showForm(request, response, errors);
			}
			try{
				f.delete(); // delete temporary file...
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}catch (Exception ex){
			logger.error(ex.getMessage(), ex);
			this.saveMessage(request, "Some error occurred while processing the CSV file");
			return super.showForm(request, response, errors);	
		}

		Collections.sort(bulkAutoReversalModelList, new BulkAutoRevresalInvalidRecordsComparator());
		request.getSession().setAttribute("bulkAutoReversalModelList", bulkAutoReversalModelList);
		String view = "redirect:p_bulkreversalpreview.html";
		return new ModelAndView(view);
	}

	private List<BulkAutoReversalModel> parseCSVFile(String filePath) throws FileNotFoundException, IOException, ParseException {
        List<BulkAutoReversalModel> bulkAutoReversalModels = null;
        
        CSVReader reader = new CSVReader(new FileReader(filePath)); // point the file from file system
        Date now = new Date();
        List<String[]> rows = reader.readAll();
        reader.close();
		bulkAutoReversalModels = new ArrayList<>(rows.size());
        Long batchNo = batchNoProvider();
        if(rows.size() >=2){
            rows.remove(0); // remove first row, as its title row.
            for (String[] row : rows) {
            	  BulkAutoReversalModel model = new BulkAutoReversalModel();
                   int rowLength = row.length;
                  model.setBatchId(batchNo);
                  if(rowLength != 1 && row[0] != ""){
                   if (rowLength ==2){
                	      model.setSrNo(row[0]);
                          model.setIsValid(true);
                          model.setCreatedOn(now);
                          model.setUpdatedOn(now);
                          model.setCreatedByAppUserModel(UserUtils.getCurrentUser());
                          model.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
					  	  model.setIsProcessed(false);
					   try{
                             if(row[1]!=null){
                           	  model.setTrxnId(row[1].trim());
                             }
            			 }catch(Exception exp){
            				 model.setValildationErrorMessage("Required data is missing in this row");
            				 model.setIsValid(false);
							 bulkAutoReversalModels.add(model);
            				 continue;
            			 }

					   bulkAutoReversalModels.add(model);

                             
                        //row[1] contains trxn id
                          if(StringUtils.isEmpty(row[1]==null?"":row[1].trim())){      
                        	  model.setIsValid(true);
                          }else{
                        	  try{
//                        		  model.setTrxnId(row[1].trim());
                        		  if(row[1].trim().length() <=15){
//                        			  model.setTrxnId(row[1].trim());
                                	  this.manualReversalManager.validateTransactionCode(model.getTrxnId().toString());
                        		  }else{
                        			  model.setValildationErrorMessage("Invalid Transaction ID");
                        			  model.setIsValid(false);
                        			  continue;
                        		  }
                        	  }catch(Exception e){
                        		  model.setIsValid(false);
                        		  model.setValildationErrorMessage("Invalid Transaction ID");
                        		  continue;
                        	  }

                          }
                  }
				   else{
                        logger.debug("Data of this row in CSV file is not in  correct format  " + row.toString());
                        model.setIsValid(false);
                        model.setValildationErrorMessage("Data of this row in CSV file is not in  correct format");
                        try{
//                        	model.setSrNo(row[0]);
                        	model.setTrxnId(row[1]);
//                            model.setFromAccount(row[3]);
//                            model.setToAccount(row[4]);
//                            model.setBalance(row[5]);
//                            model.setDescription(row[6]);
                        }catch(Exception e){
                        	model.setIsValid(false);	
                        }

					   bulkAutoReversalModels.add(model);
                        
                  }
            }
          }
       }

        return bulkAutoReversalModels;
  }
	
	private Double parseAmount(String amountStr) throws Exception{
		try{
			if(StringUtil.isNullOrEmpty(amountStr)){
				return 0.0D;
			}else{
				return Double.parseDouble(amountStr);
			}
		}catch(Exception e){
			throw new Exception();
		}
		
	}
	
	private Double formatAmount(Double amount){
		if(amount == null){
			
			return 0.0D;
		}else{
			String amt = Formatter.formatDouble(amount);
			return Double.parseDouble(amt);
		}
	}
	
	@Override
	protected ModelAndView onAuthorization(HttpServletRequest req, HttpServletResponse res,Object command, BindException errors) throws Exception {
		
		List<BulkAutoReversalModel> bulkAutoReversalModelList = null;
		List<BulkAutoReversalModel> dis = (List<BulkAutoReversalModel>) req.getSession().getAttribute("bulkAutoReversalModelList");

		Long usecaseId= ServletRequestUtils.getLongParameter(req, "usecaseId");		
		Long actionAuthorizationId = null;
		Long batchId = null;

		BulkReversalVOModel bulkReversalVOModel = populateBulkReversalVOModel(dis.get(0));
		
		try
		{				
						
			XStream xstream = new XStream();
			String refDataModelString= xstream.toXML(bulkReversalVOModel);
			
			UsecaseModel usecaseModel = usecaseFacade.loadUsecase(usecaseId);
			Long nextAuthorizationLevel = usecaseFacade.getNextAuthorizationLevel(usecaseId,new Long(0));
			bulkAutoReversalModelList = new ArrayList<>(dis.size());
			if(nextAuthorizationLevel.intValue()<1){
				for(BulkAutoReversalModel bulkAutoReversalModel : dis ){
					
					if(bulkAutoReversalModel.getIsValid() == true){
						bulkAutoReversalModel.setApproved(true);
						bulkAutoReversalModelList.add(bulkAutoReversalModel);
					}
								
				}
				req.getSession().removeAttribute("bulkManualAdjustmentsModelList");
				if(bulkAutoReversalModelList != null && bulkAutoReversalModelList.size() > 0){
					manualReversalManager.createBulkReversal(bulkAutoReversalModelList);
				
				}
				else
				{
					this.saveMessage(req, "No Record(s) found");
					return super.showForm(req, res, errors);	
				}


				//this.saveMessage(req, "Valid Records saved Successfully");
					
				actionAuthorizationId = performActionWithAllIntimationLevels(nextAuthorizationLevel,"", refDataModelString,null,usecaseModel,actionAuthorizationId,req);
				this.saveMessage(req,"Action is authorized successfully. Changes are saved against refernce Action ID : "+actionAuthorizationId);
			}else{									
				actionAuthorizationId = createAuthorizationRequest(nextAuthorizationLevel,"", refDataModelString,null,usecaseModel.getUsecaseId(),"",false,actionAuthorizationId,req);
					if(CollectionUtils.isNotEmpty(dis)){
					for(BulkAutoReversalModel bulkAutoReversalModel : dis ) {
						if(bulkAutoReversalModel.getIsValid() == true){
//							bulkAutoReversalModel.setActionAuthorizationId(actionAuthorizationId);
							bulkAutoReversalModel.setApproved(false);
						bulkAutoReversalModelList.add(bulkAutoReversalModel);
						}
					}
					req.getSession().removeAttribute("bulkAutoReversalModelList");
					if(bulkAutoReversalModelList.size() > 0 && bulkAutoReversalModelList !=null){
						manualReversalManager.createBulkReversal(bulkAutoReversalModelList);
					}
				}
				this.saveMessage(req,"Action is pending for approval against reference Action ID : "+actionAuthorizationId);
			}
		}catch (Exception ex){
			logger.error(ex.getMessage(), ex);
			this.saveMessage(req, "Some error occurred while processing the CSV file");
			return super.showForm(req, res, errors);	
		}
		
		return new ModelAndView(this.getSuccessView(), "bulkAutoReversalModel", new BulkAutoReversalModel());
	}

	private BulkReversalVOModel populateBulkReversalVOModel(BulkAutoReversalModel bulkAutoReversalModel) {
		BulkReversalVOModel bulkReversalVOModel = new BulkReversalVOModel();
		bulkReversalVOModel.setBatchId(bulkAutoReversalModel.getBatchId());
		bulkReversalVOModel.setCreatedOn(bulkAutoReversalModel.getCreatedOn());
		return bulkReversalVOModel;
	}

	private Long batchNoProvider(){
		SimpleDateFormat format = new SimpleDateFormat("yyMMddHmmss");
        String transdatetime = format.format(new Date());
        return Long.parseLong(transdatetime);
	}
	
	public void setManualAdjustmentManager(
				ManualAdjustmentManager manualAdjustmentManager) {
			this.manualAdjustmentManager = manualAdjustmentManager;
		}

	public void setManualReversalManager(
			ManualReversalManager manualReversalManager) {
		this.manualReversalManager = manualReversalManager;
	}
}
