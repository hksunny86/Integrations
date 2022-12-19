package com.inov8.microbank.webapp.action.portal.agentcashmodule;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import au.com.bytecode.opencsv.CSVReader;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.AgentOpeningBalModel;
import com.inov8.microbank.common.model.CashBankMappingModel;
import com.inov8.microbank.common.model.portal.agentcashmodule.AgentCashVOModel;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.portal.agentcashmovementmodule.AgentCashMovementManager;
import com.inov8.microbank.server.service.tillbalancemodule.TillBalanceManager;

public class CashMovementUploadController extends AdvanceFormController {

	private final static Log logger = LogFactory.getLog(CashMovementUploadController.class);
	private TillBalanceManager tillBalanceManager;
	private AgentCashMovementManager agentCashMovementManager;

	public CashMovementUploadController(){
		setCommandName("agentCashVOModel");
	    setCommandClass(AgentCashVOModel.class);
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest request)
			throws Exception {

		return null;
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest request)
			throws Exception {

		return new AgentCashVOModel();
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		
		try{
			List<CashBankMappingModel> dis = (List<CashBankMappingModel>) request.getSession().getAttribute("validatedList");
			request.getSession().removeAttribute("validatedList");
			
			if(dis != null && dis.size() > 0){
				for (CashBankMappingModel model : dis) {
	                SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
	                AgentOpeningBalModel criteria = new AgentOpeningBalModel();
	                criteria.setMsisdn(model.getAgentMobileNo());
	                criteria.setBalDate(model.getTransactionDate());
	                searchBaseWrapper.setBasePersistableModel(criteria);
	                try {
	              	  searchBaseWrapper = this.tillBalanceManager.searchAgentOpeningBalanceByExample(searchBaseWrapper);
	              	  AgentOpeningBalModel openingBalModel = (AgentOpeningBalModel)searchBaseWrapper.getCustomList().getResultsetList().get(0);
	              	  if(openingBalModel != null && openingBalModel.getAgentOpeningBalanceId() > 0){
	                        model.setAgentOpeningBalanceId(openingBalModel.getAgentOpeningBalanceId());
	                        model.setCreatedOn(new Date());
	                        openingBalModel.setUpdatedOn(new Date());
	                        openingBalModel.setUpdatedByAppUserModel(model.getCreatedByAppUserModel());
	                        
	                        if(model.getBankCreditAmount() != null && model.getBankCreditAmount() > 0){
	                            model.setBankBalAfterTx(openingBalModel.getRunningAccountBalance() + model.getBankCreditAmount());
	                            model.setTillBalAfterTx(openingBalModel.getRunningTillBalance());
	                            openingBalModel.setRunningAccountBalance(model.getBankBalAfterTx());
	                        }else if(model.getBankDebitAmount() != null && model.getBankDebitAmount() > 0){
	                            model.setBankBalAfterTx(openingBalModel.getRunningAccountBalance() - model.getBankDebitAmount());
	                            model.setTillBalAfterTx(openingBalModel.getRunningTillBalance());
	                            openingBalModel.setRunningAccountBalance(model.getBankBalAfterTx());
	                        }else if(model.getTillCreditAmount() != null && model.getTillCreditAmount() > 0){
	                            model.setBankBalAfterTx(openingBalModel.getRunningAccountBalance());
	                            model.setTillBalAfterTx(openingBalModel.getRunningTillBalance() + model.getTillCreditAmount());
	                            openingBalModel.setRunningTillBalance(model.getTillBalAfterTx());
	                        }else if(model.getTillDebitAmount() != null && model.getTillDebitAmount() > 0){
	                            model.setBankBalAfterTx(openingBalModel.getRunningAccountBalance());
	                            model.setTillBalAfterTx(openingBalModel.getRunningTillBalance() - model.getTillDebitAmount());
	                            openingBalModel.setRunningTillBalance(model.getTillBalAfterTx());
	                        }
	                        boolean result = this.agentCashMovementManager.createOrUpdateAgentCashRequiresNewTransaction(openingBalModel, model);
	              	  }
	                }catch(FrameworkCheckedException ex) {
	                	ex.printStackTrace();
	                }
				}
			
			}else{
				this.saveMessage(request, "No Record(s) found");
				return super.showForm(request, response, errors);	
			}
		}
		catch(Exception e){
			logger.error(e);
			this.saveMessage(request, "Records could not be saved");
			return super.showForm(request, response, errors);	
		}
		
		this.saveMessage(request, "Records saved Successfully");
		request.setAttribute("agentCashVOModel", new AgentCashVOModel());
		return new ModelAndView(this.getSuccessView());
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		
		AgentCashVOModel bD = (AgentCashVOModel)command;
        List<CashBankMappingModel> validCashBankMappings = new ArrayList<CashBankMappingModel>();
		
		MultipartFile csvFile = bD.getCsvFile();
		String name = csvFile.getOriginalFilename();
		String rand = UUID.randomUUID().toString();
//		File f = new File("D:/" + rand + "-" + name);
		File f = new File("/opt/BulkDisbursements/" + rand + "-" + name);
		
		List<CashBankMappingModel> dis = null;
		try{
//			long startTime = System.currentTimeMillis();
			csvFile.transferTo(f);
			dis = parseCSVFile(f.getAbsolutePath());
			try{
				f.delete(); // delete temporary file...
			}catch(Exception ex){
				ex.printStackTrace();
			}
//			long endTime = System.currentTimeMillis();
//			logger.info("***** Bulk Disbursments - Parsing Time for "+dis.size()+" salaries:"+((endTime-startTime)/1000)+" secs *****");
		}catch (Exception ex){
			logger.error(ex);
			this.saveMessage(request, "Some error occurred while processing the CSV file");
			return super.showForm(request, response, errors);	
		}

		if(dis == null || dis.size() == 0){
			this.saveMessage(request, "No valid record found in CSV file.");
			return super.showForm(request, response, errors);
		}else{
	        for (CashBankMappingModel mappingModel : dis) {
	        	if(mappingModel.getValidRecord()){
		        	validCashBankMappings.add(mappingModel);
	            }
	        }
		}
		
		request.getSession().setAttribute("validatedList", validCashBankMappings);
		request.setAttribute("agentCashVOModel", new AgentCashVOModel());
		request.setAttribute("isPreview", true);
		return new ModelAndView(this.getSuccessView(), "cashBankMappingModelList", dis);
	}
	
	private List<CashBankMappingModel> parseCSVFile(String filePath) throws FileNotFoundException, IOException, ParseException {
        List<CashBankMappingModel> cashBankMappingModels = new CopyOnWriteArrayList<CashBankMappingModel>();
        CSVReader reader = new CSVReader(new FileReader(filePath)); // point the file from file system
        Date currentDate = PortalDateUtils.parseStringAsDate(PortalDateUtils.currentFormattedDate("ddMMyy"),"ddMMyy");
        SimpleDateFormat format = new SimpleDateFormat("ddMMyy");
        List<String[]> rows = reader.readAll();
        reader.close();
        rows.remove(0); // remove first row, as its title row.
        boolean amountFound = false;
        for (String[] row : rows) {
              int rowLength = row.length;
              if (rowLength == 8) {
                      CashBankMappingModel model = new CashBankMappingModel();
                      model.setValidRecord(true);
                      
                      if(StringUtils.isEmpty(row[0]==null?"":row[0].trim())){
                    	  model.setValidRecord(false);
                      }else if(row[0].trim().length() != 6){
                    	  model.setValidRecord(false);
                      }else{
	                      try{
	                    	  Date trxDate = format.parse(row[0].trim());
		                      if(trxDate.after(currentDate)){
		                    	  model.setValidRecord(false);
		                      }
		                      model.setTransactionDate(trxDate);
	                	  }catch(Exception exp){
	                    	  model.setValidRecord(false);
	                		  System.out.println(exp.getMessage());
	                	  }
                      }
                	  if(!row[1].contains("E") && !row[1].contains("e")){
                		  model.setCscId(row[1]);
                	  }else{
                		  row[1]=BigDecimal.valueOf(Double.valueOf(row[1])).toPlainString();
                		  model.setCscId(row[1]);
                	  } 
                	  if(!row[2].contains("E") && !row[2].contains("e")){
                		  model.setAgentAccountNo(row[2]);
                		  if(row[2].length()>50){
                			  model.setValidRecord(false);  
                    	  }
                	  }else{
                		  row[2]=BigDecimal.valueOf(Double.valueOf(row[2])).toPlainString();
                		  model.setAgentAccountNo(row[2]);
                		  if(row[2].length()>50){
                			  model.setValidRecord(false);  
                    	  }
                	  }                      
                	  if(StringUtils.isEmpty(row[3]==null?"":row[3].trim())){
                    	  model.setValidRecord(false);
                      }else if(row[3].trim().length() == 10){
                    	  model.setAgentMobileNo("0"+row[3].trim());
                      }else if(row[3].trim().length() == 11){
                    	  model.setAgentMobileNo(row[3].trim());
                      }else{
                    	  model.setValidRecord(false);
                      }
                	  
                      amountFound = false;
                      if(!StringUtils.isEmpty(row[4]==null?"":row[4].trim())){
                    	  try{
                    		  //Double creditAmount = NumberFormat.getInstance().parse(row[4].trim()).doubleValue();
                    		  Double creditAmount = Double.valueOf(row[4].trim());
                    		  if(creditAmount > 0){
                    			  model.setBankCreditAmount(creditAmount);
                    			  model.setDescription("Transfer from main account");
                                  amountFound = true;
                    		  }else if(creditAmount < 0 ){
                    			  model.setValidRecord(false);
                    		  }
	                	  }catch(Exception exp){
	                    	  model.setValidRecord(false);
	                		  System.out.println(exp.getMessage());
	                	  }
                    	  if(row[4].length()>12){
                			  model.setValidRecord(false);  
                    	  }
                      }
                      
                      if(!StringUtils.isEmpty(row[5]==null?"":row[5].trim())){
                    	  try{
                    		  //Double debitAmount = NumberFormat.getInstance().parse(row[5].trim()).doubleValue();
                    		  Double debitAmount = Double.valueOf(row[5].trim());
                    		  if(debitAmount > 0){
                    			  if(amountFound){
                        			  model.setValidRecord(false);
                    			  }else{
                        			  model.setBankDebitAmount(debitAmount);
                        			  model.setDescription("Transfer to main account");
	                                  amountFound = true;
                    			  }
                    		  }else if(debitAmount < 0){
                    			  model.setValidRecord(false);
                    		  }
	                	  }catch(Exception exp){
	                    	  model.setValidRecord(false);
	                		  System.out.println(exp.getMessage());
	                	  }
                    	  if(row[5].length()>12){
                			  model.setValidRecord(false);  
                    	  }
                      }

                      if(!StringUtils.isEmpty(row[6]==null?"":row[6].trim())){
                    	  try{
                    		  //Double creditAmount = NumberFormat.getInstance().parse(row[6].trim()).doubleValue();
                    		  Double creditAmount = Double.valueOf(row[6].trim());
                    		  if(creditAmount > 0){
                    			  if(amountFound){
                        			  model.setValidRecord(false);
                    			  }else{
	                    			  model.setTillCreditAmount(creditAmount);
	                    			  model.setDescription("Physical addition");
	                                  amountFound = true;
                    			  }
                    		  }else if(creditAmount < 0){
                    			  model.setValidRecord(false);
                    		  }
	                	  }catch(Exception exp){
	                    	  model.setValidRecord(false);
	                		  System.out.println(exp.getMessage());
	                	  }
                    	  if(row[6].length()>12){
                			  model.setValidRecord(false);  
                    	  }
                      }

                      if(!StringUtils.isEmpty(row[7]==null?"":row[7].trim())){
                    	  try{
                    		  //Double debitAmount = NumberFormat.getInstance().parse(row[7].trim()).doubleValue();
                    		  Double debitAmount = Double.valueOf(row[7].trim());
                    		  if(debitAmount > 0){
                    			  if(amountFound){
                        			  model.setValidRecord(false);
                    			  }else{
                        			  model.setTillDebitAmount(debitAmount);
                        			  model.setDescription("Physical deposit to main A/c");
	                                  amountFound = true;
                    			  }
                    		  }else if(debitAmount < 0){
                    			  model.setValidRecord(false);
                    		  }
	                	  }catch(Exception exp){
	                    	  model.setValidRecord(false);
	                		  System.out.println(exp.getMessage());
	                	  }
                    	  if(row[7].length()>12){
                			  model.setValidRecord(false);  
                    	  }
                      }
                      
                      if(!amountFound){
                    	  model.setValidRecord(false);
                      }
                      
                      model.setCreatedByAppUserModel(UserUtils.getCurrentUser());
                      
                      if(model.getValidRecord()){
	                      SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
	                      AgentOpeningBalModel criteria = new AgentOpeningBalModel();
	                      criteria.setMsisdn(model.getAgentMobileNo());
	                      criteria.setBalDate(model.getTransactionDate());
	                      criteria.setAccountNumber(model.getAgentAccountNo());
	                      searchBaseWrapper.setBasePersistableModel(criteria);
	                      try {
	                    	  searchBaseWrapper = this.tillBalanceManager.searchAgentOpeningBalanceByExample(searchBaseWrapper);
	                  		  if(searchBaseWrapper.getCustomList().getResultsetList().size() ==0){
	                  			  model.setValidRecord(false);
	                  		  }
	                      }catch(FrameworkCheckedException ex) {
	                    	  ex.printStackTrace();
	                      }
                      }
                      cashBankMappingModels.add(model);
              } else {
                    logger.debug("Row Skipped, inconsistant data found:  " + row.toString());
              }
        }
        return cashBankMappingModels;
  }
	
	public void setTillBalanceManager (TillBalanceManager tillBalanceManager) {
		this.tillBalanceManager  = tillBalanceManager;
	}

	public void setAgentCashMovementManager(AgentCashMovementManager agentCashMovementManager) {
		this.agentCashMovementManager = agentCashMovementManager;
	}

}
