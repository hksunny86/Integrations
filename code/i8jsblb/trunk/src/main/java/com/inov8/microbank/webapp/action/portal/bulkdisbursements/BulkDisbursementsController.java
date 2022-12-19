package com.inov8.microbank.webapp.action.portal.bulkdisbursements;

import static com.inov8.microbank.common.util.PortalDateUtils.CSV_DATE_FORMAT;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import au.com.bytecode.opencsv.CSVReader;

import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.BulkDisbursementsModel;
import com.inov8.microbank.common.model.PaymentTypeModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.portal.bulkdisbursements.BulkDisbursementsVOModel;
import com.inov8.microbank.common.util.AccountCreationStatusEnum;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.SmsSender;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.facade.ProductFacade;
import com.inov8.microbank.disbursement.service.BulkDisbursementsManager;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;

@SuppressWarnings("deprecation")
public class BulkDisbursementsController extends AdvanceFormController {

	private final static Log logger = LogFactory.getLog(BulkDisbursementsController.class);
	
	private BulkDisbursementsManager bulkDisbursementsManager;
	private MessageSource messageSource;
	private SmsSender smsSender;
	private MfsAccountManager mfsAccountManager;
	private ReferenceDataManager referenceDataManager;
	private ProductFacade productFacade;
	
	public BulkDisbursementsController(){
		setCommandName("bulkDisbursementsVOModel");
	    setCommandClass(BulkDisbursementsVOModel.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Map<String,Object> loadReferenceData(HttpServletRequest request) throws Exception
	{
		Map<String,Object> referenceDataMap = new HashMap<String,Object>(2);
		ReferenceDataWrapper referenceDataWrapper = null;
		List<ProductModel> productModelList = productFacade.loadProductsByIds("name", SortingOrder.ASC, ProductConstantsInterface.BULK_DISBURSEMENT, ProductConstantsInterface.BULK_PAYMENT);
		PaymentTypeModel paymentTypeModel = new PaymentTypeModel();
		paymentTypeModel.setProductId(ProductConstantsInterface.BULK_PAYMENT);
		List<PaymentTypeModel> paymentTypeModelList = null;
		referenceDataWrapper = new ReferenceDataWrapperImpl( paymentTypeModel, "name", SortingOrder.ASC );
        referenceDataManager.getReferenceData(referenceDataWrapper);
        paymentTypeModelList = referenceDataWrapper.getReferenceDataList();
        
        referenceDataMap.put("paymentTypeModelList", paymentTypeModelList);
		referenceDataMap.put("productModelList", productModelList);
		return referenceDataMap;
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest request)
			throws Exception {
		return new BulkDisbursementsVOModel();
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	protected ModelAndView onCreate(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		
		try{
			long startTime = System.currentTimeMillis();
			List<BulkDisbursementsModel> dis = (List<BulkDisbursementsModel>) request.getSession().getAttribute("bulkDisbursementsModelList");
			request.getSession().removeAttribute("bulkDisbursementsModelList");

			if(dis != null && dis.size() > 0){
				Long productId = ServletRequestUtils.getRequiredLongParameter(request, "productId");
				if(ProductConstantsInterface.BULK_DISBURSEMENT.longValue() == productId)
				{
					dis = loadAppUserModels(dis);
//					bulkDisbursementsManager.createOrUpdateBulkDisbursements(dis);

					BulkDisbursementSMSSender thread = new BulkDisbursementSMSSender(smsSender, messageSource, dis);
					thread.start();
				}
				else
				{
//					dis = loadAppUserModels(dis);
//					bulkDisbursementsManager.createOrUpdateBulkPayments(dis);
					
//					BulkDisbursementSMSSender thread = new BulkDisbursementSMSSender(smsSender, messageSource, dis);
//					thread.start();
				}
			}
			else
			{
				this.saveMessage(request, "No Record(s) found");
				return super.showForm(request, response, errors);	
			}

			long endTime = System.currentTimeMillis();
			logger.info("***** Bulk Disbursements - Time to create "+dis.size()+" records :"+((endTime-startTime)/1000)+" secs *****");
		}
		catch(Exception e)
		{
			logger.error(e);
			this.saveMessage(request, "Records could not be saved");
			return super.showForm(request, response, errors);	
		}
		this.saveMessage(request, "Records saved Successfully");
		return new ModelAndView(this.getSuccessView(), "bulkDisbursementsVOModel", new BulkDisbursementsVOModel());
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception
	{
		BulkDisbursementsVOModel bulkDisbursementsVOModel = (BulkDisbursementsVOModel)command;
		
        Long productId = bulkDisbursementsVOModel.getProductId();
		MultipartFile csvFile = bulkDisbursementsVOModel.getCsvFile();
		String name = csvFile.getOriginalFilename();
		String rand = UUID.randomUUID().toString();
//  	    File f = new File("D:/BulkDisbursements/" + rand + "-" + name);
		File f = new File("/opt/BulkDisbursements/" + rand + "-" + name);
		
		List<BulkDisbursementsModel> bulkDisbursementsModelList = null;
		try{
			long startTime = System.currentTimeMillis();
			csvFile.transferTo(f);
			String filePath = f.getAbsolutePath();
			if(ProductConstantsInterface.BULK_DISBURSEMENT.longValue() == productId)
			{
				bulkDisbursementsModelList = parseCSVFile(filePath,productId,bulkDisbursementsVOModel.getSourceACNo());
				if(bulkDisbursementsModelList != null && bulkDisbursementsModelList.size() > 0)
				{
					long endTime = System.currentTimeMillis();
					logger.info("***** Bulk Disbursments - Parsing Time for "+bulkDisbursementsModelList.size()+" records:"+((endTime-startTime)/1000)+" secs *****");

					bulkDisbursementsModelList = this.mfsAccountManager.validateMobileNos(bulkDisbursementsModelList);
				}
			}
			else
			{
				bulkDisbursementsModelList = new BulkPaymentsCsvFileParser().parseBulkPaymentsCsvFile(filePath, bulkDisbursementsVOModel);
				long endTime = System.currentTimeMillis();
				logger.info("Bulk Payments: Parsing Time for "+bulkDisbursementsModelList.size()+" records:"+((endTime-startTime)/1000)+" secs");

				startTime = System.currentTimeMillis();
				bulkDisbursementsModelList = this.mfsAccountManager.isAlreadyRegistered(bulkDisbursementsModelList);
				endTime = System.currentTimeMillis();
				logger.info("Bulk Payments: Time(secs) to validate customer/agent existence from db " + (endTime-startTime)/1000 );
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

		boolean allRecordsValid = true;
		if(bulkDisbursementsModelList == null || bulkDisbursementsModelList.size() == 0){
			this.saveMessage(request, "No valid record found in CSV file.");
			return super.showForm(request, response, errors);
		}else{
	        for (BulkDisbursementsModel bulkModel : bulkDisbursementsModelList) {
	        	if(!bulkModel.getValidRecord()){
	        		allRecordsValid = false;
		        	break;
	            }
	        }
		}
		
		Collections.sort(bulkDisbursementsModelList, new BulkDisbursementInvalidRecordsComparator()); 
		request.getSession().setAttribute("bulkDisbursementsModelList", bulkDisbursementsModelList);
		String view = "redirect:p_bulkdisbursementspreview.html?productId="+productId+"&allRecordsValid="+allRecordsValid;
		return new ModelAndView(view);
	}
	
	private List<BulkDisbursementsModel> parseCSVFile(String filePath, Long productId, String sourceACNo) throws FileNotFoundException, IOException, ParseException {
        List<BulkDisbursementsModel> bulkDisbursementsModels = null;
        CSVReader reader = new CSVReader(new FileReader(filePath)); // point the file from file system
        Date now = new Date();
        Date currentDate = PortalDateUtils.parseStringAsDate(PortalDateUtils.currentFormattedDate(CSV_DATE_FORMAT),CSV_DATE_FORMAT);
        List<String[]> rows = reader.readAll();
        reader.close();
        bulkDisbursementsModels = new ArrayList<>(rows.size());
        rows.remove(0); // remove first row, as its title row.
        for (String[] row : rows) {
              int rowLength = row.length;
              if (rowLength == 9) {
                      BulkDisbursementsModel model = new BulkDisbursementsModel();
                      model.setValidRecord(true);
                      
                      if(StringUtils.isEmpty(row[1]==null?"":row[1].trim())){
                    	  model.setValidRecord(false);
                      }else{
                    	  model.setEmployeeNo(row[1].trim());
                      }
                      
                      if(StringUtils.isEmpty(row[2]==null?"":row[2].trim())){
                    	  model.setValidRecord(false);
                      }else{
                    	  model.setName(row[2].trim());
                      }
                      
                      if(StringUtils.isEmpty(row[3]==null?"":row[3].trim())){
                    	  model.setValidRecord(false);
                      }else if(row[3].trim().length() == 10){
                    	  String mob = "0" + row[3].trim();
                    	  model.setMobileNo(mob);
                      }else{
                    	  model.setMobileNo(row[3].trim());
                      }
                      if ( !CommonUtils.isValidMobileNo(model.getMobileNo()) ) {
						model.setValidRecord(Boolean.FALSE);
					  }
//******** Following check will be executed on complete list for better performance
//                      if(!mobileNumberExists(model.getMobileNo())){
//                    	  continue;
//                      }
                      if(StringUtils.isEmpty(row[4]==null?"":row[4].trim())){
                    	  model.setValidRecord(false);
                      }else{
                    	  try{
                    		  model.setServiceId(Long.parseLong(row[4].trim()));
                    	  }catch(Exception exp){
                        	  model.setValidRecord(false);
                        	  logger.error(exp.getMessage());
                    	  }
                      }
                      
                      if(StringUtils.isEmpty(row[5]==null?"":row[5].trim())){
                    	  model.setValidRecord(false);
                      }else{
                    	  model.setChequeNo(row[5].trim());
                      }
                      
                      if(StringUtils.isEmpty(row[6]==null?"":row[6].trim())){
                    	  model.setValidRecord(false);
                      }else{
                    	  try{
                    		  model.setAmount(NumberFormat.getInstance().parse(row[6].trim()).doubleValue());
	                	  }catch(Exception exp){
	                    	  model.setValidRecord(false);
	                    	  logger.error(exp.getMessage());
	                	  }
                      }
                      
                      if(StringUtils.isEmpty(row[6]==null?"":row[6].trim())){
                    	  model.setValidRecord(false);
                      }else if(row[7].trim().length() != 6){
                    	  model.setValidRecord(false);
                      }else{
	                      try{
	                    	  model.setPaymentDateStr(row[7].trim());
	                    	  Date paymentDate = PortalDateUtils.parseStringAsDate(model.getPaymentDateStr(), CSV_DATE_FORMAT);
		                      if(paymentDate.before(currentDate)){
		                    	  model.setValidRecord(false);
		                      }
		                      model.setPaymentDate(paymentDate);
	                	  }catch(Exception exp){
	                    	  model.setValidRecord(false);
	                		  logger.error(exp.getMessage());
	                	  }
                      }
                      model.setBatchNumber(""+getBatchNo());
                      model.setCreationDate(now);
                      model.setDeleted(Boolean.FALSE);
                      model.setPosted(Boolean.FALSE);
                      model.setSettled(Boolean.FALSE);
                      if(StringUtils.isEmpty(row[8].trim())){
                    	  model.setValidRecord(false);
                      }else{
                    	  model.setDescription(row[8].trim());
                      }
                      model.setCreatedByAppUserModel(UserUtils.getCurrentUser());
                      model.setUpdatedOn(new Date());
                      model.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
                      model.setProductId(productId);
                      if(!StringUtils.isEmpty(sourceACNo)){
                    	  model.setSourceACNo(sourceACNo);
                      }
                      model.setLimitApplicable(Boolean.FALSE);
                      model.setPayCashViaCnic(Boolean.FALSE);
                      model.setAccountCreationStatus(AccountCreationStatusEnum.SUCCESSFUL.toString());
                      bulkDisbursementsModels.add(model);
              } else {
                    logger.debug("Row Skipped, inconsistant data found:  " + row.toString());
              }
        }
        return bulkDisbursementsModels;
  }
	
	private List<BulkDisbursementsModel> loadAppUserModels(List<BulkDisbursementsModel> bulkDisbursementsModelList)
	{
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		for (BulkDisbursementsModel bulkDisbursementsModel : bulkDisbursementsModelList)
		{
			AppUserModel appUserModel = new AppUserModel();
			appUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
			appUserModel.setMobileNo(bulkDisbursementsModel.getMobileNo());
			searchBaseWrapper.setBasePersistableModel(appUserModel);
			searchBaseWrapper = mfsAccountManager.loadAppUserByMobileNumberAndTypeHQL(searchBaseWrapper);
			AppUserModel customerAppUserModel = (AppUserModel) searchBaseWrapper.getBasePersistableModel();
			if(customerAppUserModel != null){				
				bulkDisbursementsModel.setCnic(customerAppUserModel.getNic());
				bulkDisbursementsModel.setAppUserId(customerAppUserModel.getAppUserId());
			}
		}
		return bulkDisbursementsModelList;
	}

	private Long getBatchNo(){
		SimpleDateFormat format = new SimpleDateFormat("yyMMddHmmss");
        String transdatetime = format.format(new Date());
//        return new Integer(transdatetime);
        return Long.parseLong(transdatetime);
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public SmsSender getSmsSender() {
		return smsSender;
	}

	public void setSmsSender(SmsSender smsSender) {
		this.smsSender = smsSender;
	}

	public BulkDisbursementsManager getBulkDisbursementsManager() {
		return bulkDisbursementsManager;
	}

	public void setBulkDisbursementsManager(
			BulkDisbursementsManager bulkDisbursementsManager) {
		this.bulkDisbursementsManager = bulkDisbursementsManager;
	}
	
	public void setMfsAccountManager(MfsAccountManager mfsAccountManager) {
		this.mfsAccountManager = mfsAccountManager;
	}

	public void setProductFacade(ProductFacade productFacade) {
		this.productFacade = productFacade;
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}
}
