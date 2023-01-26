/**
 * 
 */
package com.inov8.microbank.server.service.dailyjob;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.model.SettlementSchedulerModel;
import com.inov8.microbank.common.model.SettlementTransactionDetailModel;
import com.inov8.microbank.common.model.SettlementTransactionModel;
import com.inov8.microbank.common.model.StakeholderBankInfoModel;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.server.dao.dailyjob.SettlementSchedulerDAO;
import com.inov8.microbank.server.dao.dailyjob.SettlementTransactionDAO;
import com.inov8.microbank.server.service.settlementmodule.SettlementManager;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;

/**
 * @author kashefbasher
 */

public class FundTransferScheduler {

	private final Logger logger = Logger.getLogger(FundTransferScheduler.class);
	private final SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy_HHmm");
	private final String STML_TRX_ID_LIST = "STML_TRX_ID_LIST";
	private final String TOTAL_AMOUNT = "TOTAL_AMOUNT"; 
	private final String TOTAL_AMOUNT_NEG_DR = "TOTAL_AMOUNT_NEG_DR"; 
	private final String CR_ENTRY = "CR_ENTRY"; 
	private final String DR_ENTRY = "DR_ENTRY"; 
	private final static int FILE_ERROR = 0;
	private final static int FILE_CREATED = 1;
	private final static int FILE_ALREADY_EXIST = 2;
	private final static int FILE_BLANK_CREATED = 3;
	private final DecimalFormat decimalFormat = new DecimalFormat("0.00");

	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
	private StakeholderBankInfoManager stakeholderBankInfoManager = null;
	private SettlementTransactionDAO settlementTransactionDAO = null;
	private SettlementSchedulerDAO settlementSchedulerDAO = null;
	private SettlementManager settlementManager = null;
	private String filePath = null;
	private Integer scanDaysWithNoTransaction = 0;


	int i = 0;
	
	/**
	 * @throws FrameworkCheckedException
	 */
	private void process() throws FrameworkCheckedException {

		int id = (++i);
		
		logger.info("\n:-Started Fund Transfer Scheduler "+ id);
		
		long start = System.currentTimeMillis();
						
		Date currentDateTime = new Date();
		
		CopyOnWriteArrayList<StakeholderBankInfoModel> stakeholderBankInfoModelList = getStakeholderBankInfoModelList();		

		processPendingSettlements(currentDateTime, stakeholderBankInfoModelList);
				
		logger.info("\n:-Ended Fund Transfer Scheduler Took : "+ ((System.currentTimeMillis() - start)/1000) + " Second(s) "+ id);
	}
	
	
	/**
	 * @throws FrameworkCheckedException
	 */
	private void processPendingSettlements(Date currentDateTime, CopyOnWriteArrayList<StakeholderBankInfoModel> stakeholderBankInfoModelList) throws FrameworkCheckedException {
				
		List<SettlementTransactionModel> stmList = new ArrayList<SettlementTransactionModel>(0);
		
		for(StakeholderBankInfoModel stakeholderBankInfoModel : stakeholderBankInfoModelList) {

			List<SettlementTransactionModel> stmListCr = settlementTransactionDAO.getPendingSettlementTransactionList(stakeholderBankInfoModel, currentDateTime, Boolean.TRUE);
			logger.info("credit List size"+stmListCr.size());
			stmList.addAll(stmListCr);
			List<SettlementTransactionModel> stmListDr = settlementTransactionDAO.getPendingSettlementTransactionList(stakeholderBankInfoModel, currentDateTime, Boolean.FALSE);
			logger.info("credit List size"+stmListCr.size());

			stmList.addAll(stmListDr);			
		}

		Set<Date> dateSet = new HashSet<Date>(0);
		
		for(SettlementTransactionModel settlementTransactionModel : stmList) {
			logger.info("settlement transaction List"+stmList.size());

			DateTime toDate = new DateTime(settlementTransactionModel.getCreatedOn()).withTime(0, 0, 0, 0);
			dateSet.add(toDate.toDate());			
		}

		logger.info("\n:- Fund Transfer Scheduler "+dateSet.size()+ " Previous Pending Dates Found" );

		DateTime _currentDateTime = new DateTime(new Date()).withTime(0, 0, 0, 0);
		
		if(scanDaysWithNoTransaction != null) {
			
			for(int i=1; i<=scanDaysWithNoTransaction; i++) {
				DateTime dateTime_ = _currentDateTime.minusDays(i);
				dateSet.add(dateTime_.toDate());

				logger.info("\n:- Fund Transfer Scheduler Previous Dates to Scan "+ dateTime_.toString());
			}
		}		
		
		List<Date> dateList = new ArrayList<Date>(dateSet);

		Collections.sort(dateList);

		Collections.reverse(dateList);

		int index = 0;
		Boolean copyFiles = Boolean.TRUE; 
		
		for(Date date : dateList) {
			
			if(index != 0) {
				copyFiles = Boolean.FALSE; 
			}
			
			processDailySettlements(date, stakeholderBankInfoModelList, copyFiles);
			index++;
		}
	}
	
	/**
	 * @throws FrameworkCheckedException
	 */
	private void processDailySettlements(Date createdOn, CopyOnWriteArrayList<StakeholderBankInfoModel> stakeholderBankInfoModelList, Boolean copyFiles) throws FrameworkCheckedException {
		
		logger.info("\n:- Fund Transfer Scheduler Processing Date "+ createdOn);		
		
		List<SettlementSchedulerVO> settlementSchedulerVOList = new ArrayList<SettlementSchedulerVO>(0);
		
		for(StakeholderBankInfoModel stakeholderBankInfoModel : stakeholderBankInfoModelList) {
			
			SettlementSchedulerVO settlementSchedulerVO = processSettlementTransaction(stakeholderBankInfoModel, createdOn);
			
			if(!settlementSchedulerVO.getSettlementTransactionIdList().isEmpty()) {

				settlementSchedulerVOList.add(settlementSchedulerVO);
			}else {
				logger.info("settlement transaction list "+settlementSchedulerVO.getSettlementTransactionIdList().size());
			}
		}
		
		//logger.info("settlementSchedulerVOList for File Generation "+ settlementSchedulerVOList.size());
		
		Integer filesGenerated = isFileAlreadyExists(createdOn) ? FILE_ALREADY_EXIST : -1;
		
		if(filesGenerated.intValue() != FILE_ALREADY_EXIST) {

			filesGenerated = generateCSVFiles(settlementSchedulerVOList, createdOn, copyFiles);
		}
		
		updateSettlementTransaction(filesGenerated, settlementSchedulerVOList);
	}
	
	
	
	/**
	 * @param filesGenerated
	 * @param settlementSchedulerVOList
	 */
	private void updateSettlementTransaction(Integer filesGenerated, List<SettlementSchedulerVO> settlementSchedulerVOList) {
		
		for(SettlementSchedulerVO settlementSchedulerVO : settlementSchedulerVOList) {
			
			SettlementSchedulerModel settlementSchedulerModel = createSettlementSchedulerModel(settlementSchedulerVO.getStakeholderBankInfoModel().getPrimaryKey(), 
			settlementSchedulerVO.getStakeholderBankInfoModel().getPrimaryKey(), 0L, (settlementSchedulerVO.getTotalAmountCredit() + settlementSchedulerVO.getTotalAmountDebit()));
	
			List<List<Long>> subListCollection = getLists(settlementSchedulerVO.getSettlementTransactionIdList());			
			
			if(filesGenerated.intValue() != FILE_ERROR) {

				logger.debug("Updating with file status 1 : "+filesGenerated.intValue());
				
				settlementSchedulerModel.setStatus(Boolean.TRUE);
				settlementSchedulerModel = settlementSchedulerDAO.saveOrUpdate(settlementSchedulerModel);
				
				for(List<Long> subList : subListCollection) {
					
					settlementTransactionDAO.updateSettlementTransaction(subList.toArray(), settlementSchedulerModel.getSettlementSchedulerId(), 1L);
				}				
				
				saveSettlementTransactionDetail(settlementSchedulerVO);
				
			} else if(filesGenerated.intValue() == FILE_ERROR) {

				logger.debug("Updating with file status 0 : "+filesGenerated.intValue());
				
				settlementSchedulerModel.setStatus(Boolean.FALSE);
				settlementSchedulerModel = settlementSchedulerDAO.saveOrUpdate(settlementSchedulerModel);

				for(List<Long> subList : subListCollection) {
					
					settlementTransactionDAO.updateSettlementTransaction(subList.toArray(), settlementSchedulerModel.getSettlementSchedulerId(), 0L);				
				}
			}
		}		
	}
	
	
	private void saveSettlementTransactionDetail(SettlementSchedulerVO settlementSchedulerVO) {
		
    	String amountDebit = decimalFormat.format(settlementSchedulerVO.getAmountDebit());
    	String amountCredit = decimalFormat.format(settlementSchedulerVO.getAmountCredit());		
		
		SettlementTransactionDetailModel settlementTransactionDetailModel = new SettlementTransactionDetailModel();
		settlementTransactionDetailModel.setAuthUser(settlementSchedulerVO.getAuthUser());
		settlementTransactionDetailModel.setCompany(settlementSchedulerVO.getCompany());
		settlementTransactionDetailModel.setCreatedBy(2L);
		settlementTransactionDetailModel.setCreditMovement(Double.valueOf(amountCredit));
		settlementTransactionDetailModel.setDebitMovement(Double.valueOf(amountDebit));
		settlementTransactionDetailModel.setCurrency(settlementSchedulerVO.getCurrency());
		settlementTransactionDetailModel.setDaoBranch(settlementSchedulerVO.getBranch());
		settlementTransactionDetailModel.setDaoRegion(settlementSchedulerVO.getRegion());
		settlementTransactionDetailModel.setDaoRm(settlementSchedulerVO.getRm());
		settlementTransactionDetailModel.setInputUser(settlementSchedulerVO.getInputUser());
		settlementTransactionDetailModel.setOracleNumber(settlementSchedulerVO.getStakeholderBankInfoModel().getAccountNo());
		settlementTransactionDetailModel.setProduct(settlementSchedulerVO.getProductCode());
		settlementTransactionDetailModel.setSettlementDecription(settlementSchedulerVO.getDesc());
		settlementTransactionDetailModel.setTransactionReference(settlementSchedulerVO.getTransRef());
		settlementTransactionDetailModel.setStakeholderBankInfoId(settlementSchedulerVO.getStakeholderBankInfoModel().getStakeholderBankInfoId());	
		settlementTransactionDetailModel.setCreatedOn(new Date());
		
		try {
			
			settlementTransactionDetailModel.setSettlementDate(dateFormat.parse(settlementSchedulerVO.getDate()));
		
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		settlementTransactionDAO.saveOrUpdate(settlementTransactionDetailModel);
	}
	
	
	private Integer generateCSVFiles(List<SettlementSchedulerVO> settlementSchedulerVOList, Date createdOn, Boolean copyFiles) {

		Date nowDate = new Date();
		
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(createdOn);
		calendar.set(Calendar.HOUR, nowDate.getHours());
		calendar.set(Calendar.MINUTE, nowDate.getMinutes());
		Date fileDateTime = calendar.getTime();
		
		int fileStatus = -1;
		
		SettlementSchedulerVO settlementSchedulerVO = createDetailFile(settlementSchedulerVOList, fileDateTime, copyFiles);
		
		fileStatus = settlementSchedulerVO.getFileCreationStatus();
		
		if(fileStatus != this.FILE_ERROR) {

			logger.info("\n\n***** File On Date "+createdOn+" Size "+settlementSchedulerVOList.size());
			fileStatus = createHeaderFile(settlementSchedulerVO, fileDateTime, copyFiles);
		}
				
		return fileStatus;
	}
	
	
	
	/**
	 * @param settlementSchedulerVOList
	 * @return
	 */
	private SettlementSchedulerVO createDetailFile(List<SettlementSchedulerVO> settlementSchedulerVOList, Date createdOn, Boolean copyFiles) {

		String path = this.filePath + sdf.format(createdOn)+"_"+createdOn.getTime()+"_DETAIL.CSV";
		String copyPath = this.filePath +"DETAIL.CSV";
				
		String[] columns = {"SEQ.NO","DATE","ORACLE NO","DAO(REGION)","DAO(BRANCH)","DAO(RM)","COMPANY","PRODUCT","CURRENCY","DEBIT MOVEMENT","CREDIT MOVEMENT","INPUT.USER","AUTH.USER","DESC","TRANS.REF"};
		
		FileWriter writer = null;
		SettlementSchedulerVO schedulerVO = new SettlementSchedulerVO();

		try {
			logger.info("\n--------------- "+settlementSchedulerVOList.size() +" > "+path);
		    writer = new FileWriter(path);
	 
			if(!settlementSchedulerVOList.isEmpty()) {
			    
			    for(int i=0; i<columns.length; i++) {

				    writer.append(columns[i]);
				    
				    if(columns.length != i-1) {
				    	writer.append(',');
				    }
			    }

	            writer.append('\n');
	            
			} else {

			    schedulerVO.setFileCreationStatus(this.FILE_BLANK_CREATED);
			}
            
		    Double totalAmountDebit = 0.0D;
		    Double totalAmountCredit = 0.0D;
		    
		    int index = 1;
		    
		    for(SettlementSchedulerVO settlementSchedulerVO : settlementSchedulerVOList) {

		    	String amountDebit = decimalFormat.format(settlementSchedulerVO.getAmountDebit());
		    	String amountCredit = decimalFormat.format(settlementSchedulerVO.getAmountCredit());
		    	
			    writer.append(String.valueOf(index));
			    writer.append(',');
			    writer.append(settlementSchedulerVO.getDate());
			    writer.append(',');
			    writer.append(settlementSchedulerVO.getStakeholderBankInfoModel().getAccountNo());
			    writer.append(',');
			    writer.append(settlementSchedulerVO.getRegion());
			    writer.append(',');
			    writer.append(settlementSchedulerVO.getBranch());
			    writer.append(',');
			    writer.append(settlementSchedulerVO.getRm());
			    writer.append(',');
			    writer.append(settlementSchedulerVO.getCompany());
			    writer.append(',');
			    writer.append(settlementSchedulerVO.getProductCode());
			    writer.append(',');
			    writer.append(settlementSchedulerVO.getCurrency());
			    writer.append(',');
			    writer.append(amountDebit);
			    writer.append(',');
			    writer.append(amountCredit);
			    writer.append(',');
			    writer.append(settlementSchedulerVO.getInputUser());
			    writer.append(',');
			    writer.append(settlementSchedulerVO.getAuthUser());
			    writer.append(',');
			    writer.append(settlementSchedulerVO.getDesc());
			    writer.append(',');
			    writer.append(settlementSchedulerVO.getTransRef());
			    
			    writer.append('\n');	

			    totalAmountDebit += settlementSchedulerVO.getAmountDebit();
			    totalAmountCredit += settlementSchedulerVO.getAmountCredit();
			    
			    index++;
		    }	    
	 
		    writer.flush();

		    if(copyFiles) {
			    copyFiles(path, copyPath);	
		    }
		    
		    logger.info(i+" : Created File : "+path);
		    
 		    schedulerVO.setTotalAmountCredit(totalAmountCredit);
		    schedulerVO.setTotalAmountDebit(totalAmountDebit);
		    schedulerVO.setDate(dateFormat.format(createdOn));
		    logger.info("settlement Scheduler list  "+settlementSchedulerVOList.size());
		    schedulerVO.setTotalSeq(settlementSchedulerVOList.size());
		    schedulerVO.setFileCreationStatus(this.FILE_CREATED);
		    		
		} catch(IOException e) {
		    schedulerVO.setFileCreationStatus(this.FILE_ERROR);
		    e.printStackTrace();
		    
		} finally {

		    try {
				
		    	if(writer != null) {
					writer.close();
				}
				
				if(schedulerVO.getFileCreationStatus() == FILE_ERROR) {
				    Path newFile = FileSystems.getDefault().getPath(path);
					Files.delete(newFile);
				}
				
			} catch (Exception e) {
			    schedulerVO.setFileCreationStatus(this.FILE_ERROR);
				e.printStackTrace();
			}
		}
	
		return schedulerVO;
	}
	
	
	private Integer createHeaderFile(SettlementSchedulerVO settlementSchedulerVO, Date createdOn, Boolean copyFiles) {
		
		Integer fileStatus = -1;

		String path = this.filePath + sdf.format(createdOn)+"_"+createdOn.getTime()+"_HEADER.CSV";
		String copyPath = this.filePath + "HEADER.CSV";

		FileWriter writer = null;
		
		try {
			
			Boolean isEmptyList = (settlementSchedulerVO.getTotalSeq() == 0 ? Boolean.TRUE : Boolean.FALSE);

	    	String totalAmountDebit = decimalFormat.format(settlementSchedulerVO.getTotalAmountDebit());
	    	String totalAmountCredit = decimalFormat.format(settlementSchedulerVO.getTotalAmountCredit());			
			
		    writer = new FileWriter(path);

		    if(!isEmptyList) {
			    
			    writer.append(String.valueOf(settlementSchedulerVO.getTotalSeq()));
			    writer.append(',');
			    writer.append(settlementSchedulerVO.getDate());
			    writer.append(',');
			    writer.append(totalAmountDebit);
			    writer.append(',');
			    writer.append(totalAmountCredit);
			    writer.append('\n');
		 
			    writer.flush();
		    }

		    if(copyFiles) {
			    copyFiles(path, copyPath);
		    }

		    fileStatus = (isEmptyList ? FILE_BLANK_CREATED : FILE_CREATED);
		    
		} catch(IOException e) {
			fileStatus = FILE_ERROR;
		    e.printStackTrace();
		    
		} finally {
			
			try {
				
				if(writer != null) {
					writer.close();
				}
				
				if(fileStatus == FILE_ERROR) {
				    Path newFile = FileSystems.getDefault().getPath(path);
					Files.delete(newFile);
				}				
				
			} catch (IOException e) {
				fileStatus = FILE_ERROR;
				e.printStackTrace();
			}
		}		
		
		return fileStatus;		
	}
	
	
	private void copyFiles(String srcPath, String copyPath) throws IOException {
	    
	    File cFile = new File(copyPath);
	    
	    if(cFile.exists()) {
		    cFile.delete();
	    }
	    
	    FileUtils.copyFile(new File(srcPath), cFile);

	    logger.info(i+" : Created File : "+copyPath);	
	}
	
	/**
	 * @throws FrameworkCheckedException
	 */
	private SettlementSchedulerVO processSettlementTransaction(StakeholderBankInfoModel stakeholderBankInfoModel, Date createdOn) throws FrameworkCheckedException {
		
		logger.info("StakeholderBankInfoId : "+stakeholderBankInfoModel.getStakeholderBankInfoId());
		
		List<SettlementTransactionModel> creditResultList = null;
		List<SettlementTransactionModel> debitResultList = null;
		
		try {

			creditResultList = settlementManager.getSettlementTransactionModelList(stakeholderBankInfoModel.getStakeholderBankInfoId(), Boolean.TRUE, createdOn);
			logger.info("creditResultList "+ creditResultList.size());
		
			debitResultList = settlementManager.getSettlementTransactionModelList(stakeholderBankInfoModel.getStakeholderBankInfoId(), Boolean.FALSE, createdOn);
			logger.info("debitResultList "+ debitResultList.size());
		
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		Map<String, Object> crHashMap = sumTransactions(creditResultList, CR_ENTRY);
		Map<String, Object> drHashMap = sumTransactions(debitResultList, DR_ENTRY);
		
		Double totalAmountCr = (Double)crHashMap.get(TOTAL_AMOUNT);
		Double totalAmountDr = (Double)drHashMap.get(TOTAL_AMOUNT);
		Double negativeDrAmount = (Double)drHashMap.get(TOTAL_AMOUNT_NEG_DR);

		//	When Dr amount is lessthan 0, sum the Dr amount with Cr amount.
		if(totalAmountDr != null && negativeDrAmount > 0D) {

			totalAmountCr += negativeDrAmount;	
		}
		
		List<Long> settlementTransactionIdListCr = ((List<Long>) crHashMap.get(STML_TRX_ID_LIST));
		List<Long> settlementTransactionIdListDr = ((List<Long>) drHashMap.get(STML_TRX_ID_LIST));

		List<Long> settlementTransactionIdList = new ArrayList<>(0);
		settlementTransactionIdList.addAll(settlementTransactionIdListDr);
		settlementTransactionIdList.addAll(settlementTransactionIdListCr);
		
		SettlementSchedulerVO settlementSchedulerVO = new SettlementSchedulerVO();
		settlementSchedulerVO.setSettlementTransactionIdList(settlementTransactionIdList);
		settlementSchedulerVO.setStakeholderBankInfoModel(stakeholderBankInfoModel);
		settlementSchedulerVO.setAmountCredit(totalAmountCr);
		settlementSchedulerVO.setAmountDebit(totalAmountDr);
		settlementSchedulerVO.setDate(dateFormat.format(createdOn));
		settlementSchedulerVO.setBranch("9001"); //Shaheen Complex Branch
		settlementSchedulerVO.setRm("900");
		settlementSchedulerVO.setCompany("1");
		settlementSchedulerVO.setProductCode("1001");
		settlementSchedulerVO.setCurrency("PKR");
		settlementSchedulerVO.setRegion("1");
		
		return settlementSchedulerVO;
	}
	
	

	
	/**
	 * @param transactionList
	 * @retuList<FundTransferSchedulerVO> sumTransactions(List<SettlementTransactionModel> resultList)
	 */
	private Map<String, Object> sumTransactions(List<SettlementTransactionModel> resultList, String ENTRY) {

		List<Long> settlementTransactionIdList = new ArrayList<Long>(0);
		Double totalAmount = 0.0D;
		Double negativeDrAmount = 0.0D;
		
		Map<String, Object> hashMap = new HashMap<String, Object>(0);
		
		try {
			
			for(SettlementTransactionModel settlementTransactionModel : resultList) {

				Double transactionAmount = settlementTransactionModel.getAmount();
							
				if(ENTRY.equals(DR_ENTRY) && transactionAmount < 0D) {
					//to sum the negative amount when account amount is -ve, later we will add this amount in credit amount
					negativeDrAmount += (Math.abs(transactionAmount));
				
				} else {

					totalAmount += (transactionAmount);
				}
				
				settlementTransactionIdList.add(settlementTransactionModel.getSettlementTransactionID());
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}

		hashMap.put(TOTAL_AMOUNT_NEG_DR, negativeDrAmount);
		hashMap.put(TOTAL_AMOUNT, totalAmount);
		hashMap.put(STML_TRX_ID_LIST, settlementTransactionIdList);	
		
		return hashMap;
	} 
	
	
	/**
	 * @param fromStakeholderBankId
	 * @param toStakeholderBankId
	 * @param productId
	 * @param totalAmount
	 * @return
	 */
	private SettlementSchedulerModel createSettlementSchedulerModel(Long fromStakeholderBankId, Long toStakeholderBankId, Long productId, Double totalAmount) {
		
		SettlementSchedulerModel settlementSchedulerModel = new SettlementSchedulerModel();
		
		settlementSchedulerModel.setFromStakeholderBankId(fromStakeholderBankId);
		settlementSchedulerModel.setToStakeholderBankId(toStakeholderBankId);
		settlementSchedulerModel.setTotalAmount(totalAmount);
		settlementSchedulerModel.setProductId(null);
		settlementSchedulerModel.setRrn(null);
		settlementSchedulerModel.setStatus(null);
		settlementSchedulerModel.setCreatedBy(1L);
		settlementSchedulerModel.setCreatedOn(new Date());
		settlementSchedulerModel.setUpdatedBy(1L);
		settlementSchedulerModel.setUpdatedOn(new Date());
		
		return settlementSchedulerModel;
	}
	
	
	/**
	 * @return
	 * @throws FrameworkCheckedException 
	 */
	private CopyOnWriteArrayList<StakeholderBankInfoModel> getStakeholderBankInfoModelList() throws FrameworkCheckedException {
		
		CopyOnWriteArrayList<StakeholderBankInfoModel> stakeholderBankInfoModelList = new CopyOnWriteArrayList<StakeholderBankInfoModel>();

		List<StakeholderBankInfoModel> _stakeholderBankInfoModelList = stakeholderBankInfoManager.getStakeholderBankInfoModelList();
		
		for(StakeholderBankInfoModel _stakeholderBankInfoModel : _stakeholderBankInfoModelList) {
			
			//logger.info("\n Ready To load SHBI, Found Distinct _stakeholderBankInfo Id "+_stakeholderBankInfoModel.getStakeholderBankInfoId());
			
			StakeholderBankInfoModel stakeholderBankInfoModel = getStakeholderBankInfoModel(_stakeholderBankInfoModel.getStakeholderBankInfoId());
			
			if(stakeholderBankInfoModel != null && 
					!StringUtil.isNullOrEmpty(stakeholderBankInfoModel.getAccountNo()) && 
					!"-1".equalsIgnoreCase(stakeholderBankInfoModel.getAccountNo().trim())) {

				stakeholderBankInfoModelList.add(stakeholderBankInfoModel);
			}
		}
		
		return stakeholderBankInfoModelList;
	}
	
	
	/**
	 * @return
	 */
	private StakeholderBankInfoModel getStakeholderBankInfoModel(Long stakeholderBankInfoId) {
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel(new StakeholderBankInfoModel(stakeholderBankInfoId));
		
		StakeholderBankInfoModel _stakeholderBankInfoModel = null;
		
		try {

			searchBaseWrapper = stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper);
		
			if(searchBaseWrapper != null && searchBaseWrapper.getBasePersistableModel() != null) {
				_stakeholderBankInfoModel = (StakeholderBankInfoModel) searchBaseWrapper.getBasePersistableModel();
			}
			
		} catch (FrameworkCheckedException e) {
			e.printStackTrace();
		}	
		
		return _stakeholderBankInfoModel;
	}	

	
	/**
	 * @param longValueList
	 * @return
	 */
	private List<List<Long>> getLists(List<Long> longValueList) {

		List<List<Long>> subListCollection = new ArrayList<List<Long>>(0);
		
		Integer limit = 500;
		Integer size = longValueList.size();
		
		if(size>=limit) {

			Integer starter = 0;
			Integer chunk = size / limit;
			
			if((size % limit) > 0 ) {
				
				chunk += 1;
			}
			
			List<Long> subList = null;
			
			for(int i = 0; i<chunk; i++) {
				
				if(i+1 == chunk) {

					subList = longValueList.subList(starter, size);
					
				} else {
					int _limit = limit * (i+1);
					subList = longValueList.subList(starter, _limit);
					starter = _limit;
					
				}			

				subListCollection.add(subList);
			}	
			
		} else {
			
			subListCollection.add(longValueList);
		}
		
		return subListCollection;
	}
	
	
	private Date getCurrentDate() {
	
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.roll(Calendar.DATE, -1);			
		DateTime toDate = new DateTime(calendar.getTime()).withTime(0, 0, 0, 0);
		return toDate.toDate();	
	}
	
	
	private Boolean isFileAlreadyExists(Date date) {
		
		Boolean isFileExist = Boolean.FALSE;

		String fileNamePattern = sdf.format(date);
		fileNamePattern = fileNamePattern.split("_")[0];
		
		File folder = new File(this.filePath);
		
		for (File file : folder.listFiles()) {
		
			String fileName = file.getName();
			
			if(fileName.startsWith(fileNamePattern)) {
				
				isFileExist = Boolean.TRUE;
				break;
			}
		}
		
		if(isFileExist) {
			logger.info("\nFile (csv) "+fileNamePattern+"***** already generated on this server");
		}
		
		return isFileExist;
	}	
	

	public void setStakeholderBankInfoManager(StakeholderBankInfoManager stakeholderBankInfoManager) {
		this.stakeholderBankInfoManager = stakeholderBankInfoManager;
	}
	public void setSettlementSchedulerDAO(SettlementSchedulerDAO settlementSchedulerDAO) {
		this.settlementSchedulerDAO = settlementSchedulerDAO;
	}
	public void setSettlementTransactionDAO(SettlementTransactionDAO settlementTransactionDAO) {
		this.settlementTransactionDAO = settlementTransactionDAO;
	}
	public void setSettlementManager(SettlementManager settlementManager) {
		this.settlementManager = settlementManager;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public void setScanDaysWithNoTransaction(Integer scanDaysWithNoTransaction) {
		this.scanDaysWithNoTransaction = scanDaysWithNoTransaction;
	}
}


class SettlementSchedulerVO {

	private Double totalAmountCredit = 0.0D;
	private Double totalAmountDebit = 0.0D;
	private Double amountCredit = 0.0D;
	private Double amountDebit = 0.0D;
	private String productCode = "";
	private String date = "";
	private String region = "";
	private String branch = "";
	private String rm = "";
	private String company = "";
	private String currency = "";
	private String inputUser = "";
	private String authUser = "";
	private String desc = "";
	private String transRef = "";	
	private List<Long> settlementTransactionIdList = new ArrayList<>(0);
	private StakeholderBankInfoModel stakeholderBankInfoModel = null;
	
	private int fileCreationStatus = -1;
	
	private Integer totalSeq = 0;
	
	public Double getTotalAmountCredit() {
		return totalAmountCredit;
	}
	public void setTotalAmountCredit(Double totalAmountCredit) {
		this.totalAmountCredit = totalAmountCredit;
	}
	public Double getTotalAmountDebit() {
		return totalAmountDebit;
	}
	public void setTotalAmountDebit(Double totalAmountDebit) {
		this.totalAmountDebit = totalAmountDebit;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getRm() {
		return rm;
	}
	public void setRm(String rm) {
		this.rm = rm;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getInputUser() {
		return inputUser;
	}
	public void setInputUser(String inputUser) {
		this.inputUser = inputUser;
	}
	public String getAuthUser() {
		return authUser;
	}
	public void setAuthUser(String authUser) {
		this.authUser = authUser;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getTransRef() {
		return transRef;
	}
	public void setTransRef(String transRef) {
		this.transRef = transRef;
	}
	public StakeholderBankInfoModel getStakeholderBankInfoModel() {
		return stakeholderBankInfoModel;
	}
	public void setStakeholderBankInfoModel(StakeholderBankInfoModel stakeholderBankInfoModel) {
		this.stakeholderBankInfoModel = stakeholderBankInfoModel;
	}
	public Double getAmountCredit() {
		return amountCredit;
	}
	public void setAmountCredit(Double amountCredit) {
		this.amountCredit = amountCredit;
	}
	public Double getAmountDebit() {
		return amountDebit;
	}
	public void setAmountDebit(Double amountDebit) {
		this.amountDebit = amountDebit;
	}
	public Integer getTotalSeq() {
		return totalSeq;
	}
	public void setTotalSeq(Integer totalSeq) {
		this.totalSeq = totalSeq;
	}
	public List<Long> getSettlementTransactionIdList() {
		return settlementTransactionIdList;
	}
	public void setSettlementTransactionIdList(List<Long> settlementTransactionIdList) {
		this.settlementTransactionIdList = settlementTransactionIdList;
	}
	public int getFileCreationStatus() {
		return fileCreationStatus;
	}
	public void setFileCreationStatus(int fileCreationStatus) {
		this.fileCreationStatus = fileCreationStatus;
	}
}