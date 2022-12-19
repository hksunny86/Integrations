package com.inov8.microbank.webapp.action.portal.manualadjustmentmodule;

import au.com.bytecode.opencsv.CSVReader;

import com.inov8.microbank.common.model.BBAccountsViewModel;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.portal.bulkdisbursements.BulkManualAdjustmentModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.manualadjustmentmodule.ManualAdjustmentManager;
import com.inov8.microbank.webapp.action.authorizationmodule.AdvanceAuthorizationFormController;
import com.thoughtworks.xstream.XStream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
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

public class BulkManualAdjustmentController extends AdvanceAuthorizationFormController{

	private ManualAdjustmentManager manualAdjustmentManager;

	public BulkManualAdjustmentController() {
		setCommandName("bulkManualAdjustmentModel");
		setCommandClass(BulkManualAdjustmentModel.class);
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
		BulkManualAdjustmentModel model = new BulkManualAdjustmentModel();
		return model;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		
		List<BulkManualAdjustmentModel> bulkManualAdjustmentsModelList = null;
		List<BulkManualAdjustmentModel> bulkAdjustmentsModelList = null;
		Long batchId =null;
		
		
		try{
			List<BulkManualAdjustmentModel> dis = (List<BulkManualAdjustmentModel>) request.getSession().getAttribute("bulkManualAdjustmentsModelList");
			bulkAdjustmentsModelList = new ArrayList<>(dis.size());
			for(BulkManualAdjustmentModel bulkManualAdjustmentModel : dis ){
				if(bulkManualAdjustmentModel.getIsValid() == true){
					bulkManualAdjustmentModel.setIsApproved(true);
					bulkAdjustmentsModelList.add(bulkManualAdjustmentModel);
				}
							
			}
			request.getSession().removeAttribute("bulkManualAdjustmentsModelList");
			if(bulkAdjustmentsModelList != null && bulkAdjustmentsModelList.size() > 0){
				batchId = bulkAdjustmentsModelList.get(0).getBatchId();
				manualAdjustmentManager.createBulkAdjustments(bulkAdjustmentsModelList);
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
		return new ModelAndView(this.getSuccessView(), "bulkManualAdjustmentModel", new BulkManualAdjustmentModel());
	
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		BulkManualAdjustmentModel bulkManualAdjustmentModel = (BulkManualAdjustmentModel) command;
		MultipartFile csvFile = bulkManualAdjustmentModel.getCsvFile();
		String name = csvFile.getOriginalFilename();
		String rand = UUID.randomUUID().toString();
		File f = new File(name);
		
		List<BulkManualAdjustmentModel> bulkManualAdjustmentsModelList = null;
		
		try{
			csvFile.transferTo(f);
			String filePath = f.getAbsolutePath();
			bulkManualAdjustmentsModelList = parseCSVFile(filePath);	
			if(bulkManualAdjustmentsModelList.size()<1){
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

		Collections.sort(bulkManualAdjustmentsModelList, new BulkAdjustmentInvalidRecordsComparator());
		request.getSession().setAttribute("bulkManualAdjustmentsModelList", bulkManualAdjustmentsModelList);
		String view = "redirect:p_bulkmanualadjustmentpreview.html";
		return new ModelAndView(view);
	}

	private List<BulkManualAdjustmentModel> parseCSVFile(String filePath) throws FileNotFoundException, IOException, ParseException {
        List<BulkManualAdjustmentModel> bulkManualAdjustmentModels = null;
        
        CSVReader reader = new CSVReader(new FileReader(filePath)); // point the file from file system
        Date now = new Date();
        List<String[]> rows = reader.readAll();
        reader.close();
        bulkManualAdjustmentModels = new ArrayList<>(rows.size());
        Long batchNo = batchNoProvider();
        if(rows.size() >=2){
            rows.remove(0); // remove first row, as its title row.
            for (String[] row : rows) {
            	  BulkManualAdjustmentModel model = new BulkManualAdjustmentModel();
                  int rowLength = row.length;
                  model.setBatchId(batchNo);
                  if(rowLength != 1 && row[0] != ""){
                   if (rowLength ==7){
                	//  ManualAdjustmentModel manaualAdjustmentModel = new ManualAdjustmentModel();
                	  BBAccountsViewModel bBAccountsViewModel = new BBAccountsViewModel();
                	      model.setSrNo(row[0]);
                          model.setIsValid(true);
                          model.setCreatedOn(now);
                          model.setUpdatedOn(now);
                          model.setCreatedByAppUserModel(UserUtils.getCurrentUser());
                          model.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
                          model.setIsProcessed(false);
                          model.setVersionNo((long) 0);
                          model.setErrorDescription("");
            			 try{
                             if(row[1]!=null){
                           	  model.setTrxnId(row[1].trim());
                             }
                             if(row[2]!=null){
                  				  try{
                  					  model.setAdjustmentType(Long.parseLong(row[2]));
                  				  }catch(Exception e){
                  					  model.setAdjustmentType((long) 0);
                  				  }
                  			  }
                             model.setFromAccount(row[3]);
               			     model.setToAccount(row[4]);
               			  try {
               				model.setBalance(row[5].trim());
   							model.setAmount(parseAmount(row[5].trim()));
   						} catch (Exception e1) {
   							model.setAmount(0.0D);
   						}
               			  
            			 }catch(Exception exp){
            				 model.setValildationErrorMessage("Required data is missing in this row");
            				 model.setIsValid(false);
            				 bulkManualAdjustmentModels.add(model);
            				 continue;
            			 }
            			 
            			 try{
                       	  model.setDescription(row[6]);
                         }catch(Exception e){
                       	  model.setDescription("");
                         }
            			  
            			  
            			  bulkManualAdjustmentModels.add(model);

                             
                        //row[1] contains trxn id
                          if(StringUtils.isEmpty(row[1]==null?"":row[1].trim())){      
                        	  model.setIsValid(true);
                          }else{
                        	  try{
                        		 // model.setTrxnId(row[1].trim());
                        		  if(row[1].trim().length() <=15){
                        			 // model.setTrxnId(row[1].trim());
                                	  this.manualAdjustmentManager.validateTransactionCode(model.getTrxnId().toString());
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
                          
                          
                          //row[2] contains adjustment type , row[3] contains from account no and row[4] contains to account no
                          if((!StringUtils.isEmpty(row[2]==null?"":row[2].trim())) && (!StringUtils.isEmpty(row[3]==null?"":row[3].trim())) && (!StringUtils.isEmpty(row[4]==null?"":row[4].trim())) ){
                        	  if(!row[3].trim().equals(row[4].trim())){
                           	    if(model.getAdjustmentType() == ManualAdjustmentTypeConstants.BB_TO_BB.longValue()){    
                              	  try{
                              		  if(row[3].trim().length() <=24 && row[4].trim().length() <=24){
                              			  bBAccountsViewModel.setAccountNumber(EncryptionUtil.encryptWithDES(row[3].trim()));
                                  		  bBAccountsViewModel = manualAdjustmentManager.getBBAccountsViewModel(bBAccountsViewModel);
                                  		  if(bBAccountsViewModel == null){
                                  			  model.setIsValid(false);
                                  			  model.setValildationErrorMessage("Invalid From Account #");
                                  			  continue;
                                  		  }else{
                                  			  String errorMsg = manualAdjustmentManager.accountNumberHealthCheck(bBAccountsViewModel,"From ");
                                  			  if(!StringUtil.isNullOrEmpty(errorMsg)){
                                      			  model.setIsValid(false);
                                      			  model.setValildationErrorMessage(errorMsg);
                                      			  continue;
                                  			  }else{
                                      			  model.setFromAccountTitle(bBAccountsViewModel.getTitle());
                                  			  }
                                  		  }
                                  		  
                                  		  bBAccountsViewModel = new BBAccountsViewModel();
                                  		  bBAccountsViewModel.setAccountNumber(EncryptionUtil.encryptWithDES(row[4].trim()));
                                  		  bBAccountsViewModel = manualAdjustmentManager.getBBAccountsViewModel(bBAccountsViewModel);
                                  		  if(bBAccountsViewModel == null){
                                  			  model.setIsValid(false);
                                  			  model.setValildationErrorMessage("Invalid To Account #");
                                  			  continue;
                                  		  }else{
                                  			  String errorMsg = manualAdjustmentManager.accountNumberHealthCheck(bBAccountsViewModel, "To ");
                                  			  if(!StringUtil.isNullOrEmpty(errorMsg)){
                                      			  model.setIsValid(false);
                                      			  model.setValildationErrorMessage(errorMsg);
                                      			  continue;
                                  			  }else{
                                      			  model.setToAccountTitle(bBAccountsViewModel.getTitle());
                                  			  }
                                  		  }

                                  		  /*
                                  		  if(bBAccountsViewModel != null){
                                				model.setFromAccount(row[3].trim());
                                				model.setFromAccountTitle(bBAccountsViewModel.getTitle());
                                				bBAccountsViewModel = new BBAccountsViewModel();
                                				bBAccountsViewModel.setAccountNumber(EncryptionUtil.encryptWithDES(row[4].trim()));
                                    		    bBAccountsViewModel = manualAdjustmentManager.getBBAccountsViewModel(bBAccountsViewModel);


                                    		    if(bBAccountsViewModel == null){
                                    		    	model.setIsValid(false);
                                    		    	model.setValildationErrorMessage("Invalid To Account #");
                                    		    	continue;
                                    		    }else{
													model.setToAccountTitle(bBAccountsViewModel.getTitle());
												}
                                			}else if(bBAccountsViewModel == null){                            				
                                				model.setIsValid(false);
                                				model.setValildationErrorMessage("Invalid From Account #");
                                				continue;
                                			}
                                			*/
                              		  }else{
                              			  model.setIsValid(false);
                              			  model.setValildationErrorMessage("Account # length is Invalid");
                              			  continue;
                              		  }
                              		 
                              	  }catch(Exception exp){                          		 
                              		  model.setIsValid(false);
                                      model.setValildationErrorMessage("Invalid Account #");
                                  	  continue;
                              	  } 
                                }else if(model.getAdjustmentType() == ManualAdjustmentTypeConstants.BB_TO_CORE.longValue()){  
                              	  
                              	  try{
                              		  if(row[3].trim().length() <=24 && row[4].trim().length() <=24){
                              			  bBAccountsViewModel.setAccountNumber(EncryptionUtil.encryptWithDES(row[3].trim()));
                                  		  bBAccountsViewModel = manualAdjustmentManager.getBBAccountsViewModel(bBAccountsViewModel);
                                			if(bBAccountsViewModel != null ){
                                				model.setFromAccount(row[3].trim());
                                				model.setFromAccountTitle(bBAccountsViewModel.getTitle());
                                				model.setToAccount(row[4].trim());
                                			}else if(bBAccountsViewModel == null){                            				
                                				model.setIsValid(false);
                                				model.setValildationErrorMessage("Invalid From Account #");
                                				continue;
                                			}
                              		  }else{
                              			  model.setIsValid(false);
                            			  model.setValildationErrorMessage("Account # length is Invalid");
                              			  continue;
                              		  }
                              		 
                              	  }catch(Exception exp){                          		  
                              		  model.setIsValid(false);
                                  	  logger.error(exp.getMessage(),exp);
                                  	  model.setValildationErrorMessage("Invalid From Account #");
                                  	  continue;
                              	  } 
                            }else if(model.getAdjustmentType() == ManualAdjustmentTypeConstants.CORE_TO_BB.longValue()){
                          	  try{
                            		 if(row[3].trim().length() <=24 && row[4].trim().length() <=24){
                            			bBAccountsViewModel.setAccountNumber(EncryptionUtil.encryptWithDES(row[4].trim()));
                            		  bBAccountsViewModel = manualAdjustmentManager.getBBAccountsViewModel(bBAccountsViewModel);
                          			if(bBAccountsViewModel != null ){
                          				model.setToAccount(row[4].trim());
                        				model.setToAccountTitle(bBAccountsViewModel.getTitle());
                          				model.setFromAccount(row[3].trim());
                          			}else if(bBAccountsViewModel == null){                      				
                          				model.setIsValid(false);
                          			    model.setValildationErrorMessage("Invalid To Account #");
                          				continue;
                          			}
                            		 }else{
                            			 model.setIsValid(false);
                            			 model.setValildationErrorMessage("Invalid Account # length");
                            			 continue;
                            		 }
                          		  
                          	  }catch(Exception exp){                      		 
                          		  model.setIsValid(false);
                              	  logger.error(exp.getMessage(),exp);
                              	  model.setValildationErrorMessage("Invalid Account #");
                              	  continue;
                          	  } 
                            }else{                        	
                          	  model.setIsValid(false);
                          	  model.setValildationErrorMessage("Invalid Adjustment Type");
                          	  continue;
                            }
                           
                      }else{
                    	  model.setIsValid(false);
                    	  model.setValildationErrorMessage("From and To Account # are same");
                    	  continue;
                      }
                  }
                    else{
                  	  model.setIsValid(false);
                  	  model.setValildationErrorMessage("Invalid account or adjustment type");
                   	  continue;
                    }
                                
                          
                          // row[5]  contains amount
                          if(StringUtils.isEmpty(row[5]==null?"":row[5].trim())){                      	  
                        	  model.setIsValid(false);
                        	  model.setValildationErrorMessage("Invalid amount");
                        	  continue;
                          }else{
                        	  try{
                        		  
                        		  if(model.getBalance().length() > 3 && model.getBalance().indexOf(".") != -1  && (model.getBalance().length() -  model.getBalance().indexOf(".") > 3) ){
                        			  model.setIsValid(false);
                        			  logger.error("Amount should be max 2 decimal places");
                        			  model.setValildationErrorMessage("Amount should be max 2 decimal places");
                        			  continue;
                        		  }
                        		  if(model.getAmount() < 0.01D){                    			 
                        			model.setIsValid(false);
                      				logger.error("Amount cannot be less than 0.01");
                      				model.setValildationErrorMessage("Amount cannot be less than 0.01");
                      				continue;
                      			}
                      			if(model.getAmount() > 999999999999.99D){
                      				model.setIsValid(false);
                      				logger.error("Amount cannot be greater than 999999999999.99");
                      				model.setValildationErrorMessage("Amount cannot be greater than 999999999999.99");
                      				continue;
                      			}
    	                	  }catch(Exception exp){
    	                    	  logger.error(exp.getMessage(),exp);
    	                    	  model.setIsValid(false);
    	                    	  model.setValildationErrorMessage("Invalid Amount");
    	                    	  continue;
    	                	  }
                          }
                          
                  } else{
                        logger.debug("Data of this row in CSV file is not in  correct format  " + row.toString());
                        model.setIsValid(false);
                        model.setValildationErrorMessage("Data of this row in CSV file is not in  correct format");
                        try{
                        	model.setSrNo(row[0]);
                        	model.setTrxnId(row[1]);
                            model.setFromAccount(row[3]);
                            model.setToAccount(row[4]);
                            model.setBalance(row[5]);
                            model.setDescription(row[6]);
                        }catch(Exception e){
                        	model.setIsValid(false);	
                        }
                        
                        bulkManualAdjustmentModels.add(model);
                        
                  }
            }
          }
       }

        return bulkManualAdjustmentModels;
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
		
		List<BulkManualAdjustmentModel> bulkAdjustmentsModelList = null;
		List<BulkManualAdjustmentModel> dis = (List<BulkManualAdjustmentModel>) req.getSession().getAttribute("bulkManualAdjustmentsModelList");

		Long usecaseId= ServletRequestUtils.getLongParameter(req, "usecaseId");		
		Long actionAuthorizationId = null;
		Long batchId = null;
			
		BulkManualAdjustmentVOModel bulkManualAdjustmentVOModel = populateBulkManualAdjustmentVOModel(dis.get(0)); 
		
		try
		{				
						
			XStream xstream = new XStream();
			String refDataModelString= xstream.toXML(bulkManualAdjustmentVOModel);
			
			UsecaseModel usecaseModel = usecaseFacade.loadUsecase(usecaseId);
			Long nextAuthorizationLevel = usecaseFacade.getNextAuthorizationLevel(usecaseId,new Long(0));
			bulkAdjustmentsModelList = new ArrayList<>(dis.size());
			if(nextAuthorizationLevel.intValue()<1){
				for(BulkManualAdjustmentModel bulkManualAdjustmentModel : dis ){
					
					if(bulkManualAdjustmentModel.getIsValid() == true){
						bulkManualAdjustmentModel.setIsApproved(true);
						bulkAdjustmentsModelList.add(bulkManualAdjustmentModel);	
					}
								
				}
				req.getSession().removeAttribute("bulkManualAdjustmentsModelList");
				if(bulkAdjustmentsModelList != null && bulkAdjustmentsModelList.size() > 0){
					manualAdjustmentManager.createBulkAdjustments(bulkAdjustmentsModelList);
				
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
					for(BulkManualAdjustmentModel bulkManualAdjustmentModel : dis ) {
						if(bulkManualAdjustmentModel.getIsValid() == true){
						bulkManualAdjustmentModel.setActionAuthorizationId(actionAuthorizationId);
						bulkManualAdjustmentModel.setIsApproved(false);
						bulkAdjustmentsModelList.add(bulkManualAdjustmentModel);
						}
					}
					req.getSession().removeAttribute("bulkManualAdjustmentsModelList");
					if(bulkAdjustmentsModelList.size() > 0 && bulkAdjustmentsModelList !=null){
					    manualAdjustmentManager.createBulkAdjustments(bulkAdjustmentsModelList);
					}
				}
				this.saveMessage(req,"Action is pending for approval against reference Action ID : "+actionAuthorizationId);
			}
		}catch (Exception ex){
			logger.error(ex.getMessage(), ex);
			this.saveMessage(req, "Some error occurred while processing the CSV file");
			return super.showForm(req, res, errors);	
		}
		
		return new ModelAndView(this.getSuccessView(), "bulkManualAdjustmentModel", new BulkManualAdjustmentModel());
	}

	private BulkManualAdjustmentVOModel populateBulkManualAdjustmentVOModel(BulkManualAdjustmentModel bulkManualAdjustmentModel) {
		BulkManualAdjustmentVOModel bulkManualAdjustmentVOModel = new BulkManualAdjustmentVOModel();
		bulkManualAdjustmentVOModel.setBatchId(bulkManualAdjustmentModel.getBatchId());
		bulkManualAdjustmentVOModel.setCreatedOn(bulkManualAdjustmentModel.getCreatedOn());
		return bulkManualAdjustmentVOModel;
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

}
