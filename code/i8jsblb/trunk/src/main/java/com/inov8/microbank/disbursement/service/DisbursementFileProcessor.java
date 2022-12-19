package com.inov8.microbank.disbursement.service;

import au.com.bytecode.opencsv.CSVReader;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.integration.common.model.AccountModel;
import com.inov8.integration.common.model.LimitModel;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.disbursement.vo.BulkDisbursementsVOModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.disbursement.model.BulkDisbursementsFileInfoModel;
import com.inov8.microbank.disbursement.util.DisbursementStatusConstants;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;

import com.inov8.ola.server.dao.account.AccountDAO;
import com.inov8.ola.server.dao.ledger.LedgerDAO;
import com.inov8.ola.server.dao.limit.LimitDAO;
import com.inov8.ola.server.service.limit.LimitManager;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import com.inov8.ola.util.LimitTypeConstants;
import com.inov8.ola.util.TransactionTypeConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import java.io.FileReader;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.inov8.microbank.common.util.PortalDateUtils.CSV_DATE_FORMAT;

public class DisbursementFileProcessor implements Runnable {

    private final static Log logger = LogFactory.getLog(DisbursementFileProcessor.class);

    private MfsAccountManager mfsAccountManager;
    private BulkDisbursementsManager bulkDisbursementsManager;
    private BulkDisbursementsVOModel bulkDisbursementsVOModel;
    private BulkDisbursementsFileInfoModel fileInfoModel;
    private AccountDAO accountDAO;
    private LedgerDAO ledgerDAO;
    private LimitManager limitManager;

    public DisbursementFileProcessor(BulkDisbursementsFileInfoModel fileInfoModel, BulkDisbursementsVOModel bulkDisbursementsVOModel,
                                     MfsAccountManager mfsAccountManager, BulkDisbursementsManager bulkDisbursementsManager, AccountDAO accountDAO,
                                     LedgerDAO ledgerDAO, LimitManager limitManager) {

        this.bulkDisbursementsManager = bulkDisbursementsManager;
        this.mfsAccountManager = mfsAccountManager;
        this.bulkDisbursementsVOModel = bulkDisbursementsVOModel;
        this.fileInfoModel = fileInfoModel;
        this.accountDAO = accountDAO;
        this.ledgerDAO = ledgerDAO;
        this.limitManager = limitManager;
    }

    public void run() {

        CopyOnWriteArrayList<String[]> recordList = new CopyOnWriteArrayList<>();
        int step = 1;
        try {
            fileInfoModel.setStatus(DisbursementStatusConstants.STATUS_PARSING);
            bulkDisbursementsManager.saveUpdateBulkDisbursementsFileInfoModel(fileInfoModel); //status update

            long start = System.currentTimeMillis();

            parseFile(recordList);
            bulkDisbursementsVOModel.setRecordList(recordList);

            long end = System.currentTimeMillis();
            double diff = (end - start) / 1000.0d;
            logger.info("Disbursement : File parsing completed at : " + new Date(end) + " total time taken : " + diff + " seconds");

            step = 2;
            logger.info("Disbursement : Saving Bulk Disbursement File Info");
            bulkDisbursementsManager.saveUpdateBulkDisbursementsFileInfoModel(fileInfoModel); //status update

            logger.info("Disbursement : Saving Bulk Disbursement Models");
            bulkDisbursementsManager.saveBulkDisbursementsModelList(bulkDisbursementsVOModel, fileInfoModel);
            logger.info("Disbursement : File Processed");
        } catch (Exception e) {

            switch (step) {
                case 1:
                    fileInfoModel.setStatus(DisbursementStatusConstants.STATUS_PARSING_FAILED);
                    bulkDisbursementsManager.saveUpdateBulkDisbursementsFileInfoModel(fileInfoModel);
                    break;

                case 2:
                    fileInfoModel.setStatus(DisbursementStatusConstants.STATUS_SAVING_FAILED);
                    bulkDisbursementsManager.saveUpdateBulkDisbursementsFileInfoModel(fileInfoModel);
                    break;
            }

            e.printStackTrace();
        } finally {
            logger.info("Batch : " + fileInfoModel.getBatchNumber() + " Total : " + fileInfoModel.getTotalRecords() +
                    " Valid : " + fileInfoModel.getValidRecords() + " Invalid : " + fileInfoModel.getInValidRecords());
        }
    }

    private void parseFile(CopyOnWriteArrayList<String[]> recordList) throws Exception {

        long serviceId = bulkDisbursementsVOModel.getServiceId();
        if (ServiceConstantsInterface.BULK_DISB_ACC_HOLDER == serviceId) {
            parseAcHolderFile(recordList, bulkDisbursementsVOModel);
        } else if (ServiceConstantsInterface.BULK_DISB_NON_ACC_HOLDER == serviceId) {
            parseNonAcHolderFile(recordList, bulkDisbursementsVOModel);
        }
    }

    public void parseNonAcHolderFile(CopyOnWriteArrayList<String[]> recordList, BulkDisbursementsVOModel bulkDisbursementsVOModel) throws Exception {

        CSVReader reader = new CSVReader(new FileReader(fileInfoModel.getFilePath())); // point the file from file system

        Date now = new Date();
        Date currentDate = PortalDateUtils.parseStringAsDate(PortalDateUtils.currentFormattedDate(CSV_DATE_FORMAT), CSV_DATE_FORMAT);
        List<String[]> csvRows = reader.readAll();
        reader.close();

        String[] columns = csvRows.get(0);
        csvRows.remove(0); // remove first row, as its title row.

        int validRecords = 0;
        int invalidCount = 0;
        Double totalAmount = 0D;
        Double totalCharges = 0D;
        Double totalFED = 0D;
        List<Double> amounts = getUniqueAmounts(csvRows, true);
        Map<Double, List<Double>> commissionAmountsMap = getCommissionAmountsMap(amounts);
        List<String> cnicList = new ArrayList<String>();//getAccountControlManager().loadBlacklistedCNICList();
        for (String[] row : csvRows) {
            String[] csvRecord = new String[34];
            int rowLength = row.length;
            boolean isValid;

            csvRecord[8] = "1"; // default to true

            if (rowLength != 9) {
                csvRecord[8] = "0";
                csvRecord[9] = "In consistent record, 9 values are required";
            }

            validateString(columns[1], StringUtil.trimToFitInDB(row[1], 100), 0, csvRecord); // registration
            validateString(columns[2], StringUtil.trimToFitInDB(row[2], 100), 1, csvRecord); // name
            validateCNIC(columns[3], StringUtil.trimToFitInDB(row[3], 20), 21, csvRecord); // cnic
            validateMobile(columns[4], StringUtil.trimToFitInDB(row[4], 20), 2, csvRecord); // mobile
            validateString(columns[5], StringUtil.trimToFitInDB(row[5], 20), 3, csvRecord); // cheque no
            validateAmount(columns[6], row[6], 4, csvRecord); // amount
            validateDate(row[7], 5, csvRecord, currentDate); // date
            validateString(columns[8], row[8], 6, csvRecord); // desc

            isValid = checkDuplicate(recordList, csvRecord, true, cnicList); // check duplicate

            csvRecord[7] = String.valueOf(bulkDisbursementsVOModel.getServiceId());

            csvRecord[10] = String.valueOf(bulkDisbursementsVOModel.getProductId());
            csvRecord[11] = fileInfoModel.getBatchNumber();
            csvRecord[12] = "0";
            csvRecord[13] = "0";
            csvRecord[14] = "0";

            csvRecord[15] = PortalDateUtils.formatDate(now, "ddMMyy");
            csvRecord[16] = PortalDateUtils.formatDate(now, "ddMMyy");
            csvRecord[17] = bulkDisbursementsVOModel.getCreatedByAppUserModel().getAppUserId().toString();
            csvRecord[18] = bulkDisbursementsVOModel.getCreatedByAppUserModel().getAppUserId().toString();

            csvRecord[19] = CommonUtils.getDefaultIfNull(bulkDisbursementsVOModel.getBiometricVerification(), false) ? "1" : "0";
            csvRecord[20] = CommonUtils.getDefaultIfNull(bulkDisbursementsVOModel.getLimitApplicable(), false) ? "1" : "0";
            csvRecord[26] = CommonUtils.getDefaultIfNull(bulkDisbursementsVOModel.getPayCashViaCnic(), false) ? "1" : "0";
            csvRecord[30] = "0";
            csvRecord[31] = "0";
            csvRecord[32] = "0";

            if (isValid) {
                List<Double> list = commissionAmountsMap.get(Double.valueOf(csvRecord[4]));
                Double fed = 0.0;
                Double charges = 0.0;

                if (list.size() == 2) {
                    if (null != list.get(0)) {
                        fed = list.get(0);
                    }
                    charges = list.get(1);

                    totalCharges += charges;
                    totalFED += fed;
                    totalAmount += Double.valueOf(csvRecord[4]);

                    //validRecords++;
                } else {
                    csvRecord[8] = "0";
                    csvRecord[9] = "Error in charges calculation";

                    //invalidCount++;
                    isValid = false;
                }

                csvRecord[31] = String.valueOf(fed); // fed
                csvRecord[32] = String.valueOf(charges); // charges
            } else {
                //invalidCount++;
                isValid = false;
            }

            if (isValid) {
                validRecords++;
            } else {
                invalidCount++;
            }

            csvRecord[33] = bulkDisbursementsVOModel.getIsApproved();
            recordList.add(csvRecord);
        }

        fileInfoModel.setTotalCharges(totalCharges);
        fileInfoModel.setTotalFed(totalFED);
        fileInfoModel.setTotalAmount(totalAmount);

        fileInfoModel.setTotalRecords((long) recordList.size());
        fileInfoModel.setValidRecords((long) validRecords);
        fileInfoModel.setInValidRecords((long) invalidCount);

        bulkDisbursementsVOModel.setInvalidCount(invalidCount);
    }

    private void parseAcHolderFile(CopyOnWriteArrayList<String[]> recordList, BulkDisbursementsVOModel bulkDisbursementsVOModel) throws Exception {
        CSVReader reader = new CSVReader(new FileReader(fileInfoModel.getFilePath())); // point the file from file system

        Date now = new Date();
        Date currentDate = PortalDateUtils.parseStringAsDate(PortalDateUtils.currentFormattedDate(CSV_DATE_FORMAT), CSV_DATE_FORMAT);
        List<String[]> csvRows = reader.readAll();
        reader.close();

        String[] columns = csvRows.get(0);
        csvRows.remove(0); // remove first row, as its title row.

        int validRecords = 0;
        int invalidCount = 0;
        Double validRecordTotalAmount = 0D;
        Double totalAmount = 0D;
        Double totalCharges = 0D;
        Double totalFED = 0D;
        List<Double> amounts = getUniqueAmounts(csvRows, false);
        Map<Double, List<Double>> commissionAmountsMap = getCommissionAmountsMap(amounts);

        for (String[] row : csvRows) {
            String[] csvRecord = new String[34];
            boolean isValid;
            int rowLength = row.length;
            csvRecord[8] = "1";
            if (rowLength != 8) {
                csvRecord[8] = "0";
                csvRecord[9] = "In consistent record, 8 values are required";
            }

            validateString(columns[1], StringUtil.trimToFitInDB(row[1], 100), 0, csvRecord); // registration
            validateString(columns[2], StringUtil.trimToFitInDB(row[2], 100), 1, csvRecord); // name
            validateMobile(columns[3], StringUtil.trimToFitInDB(row[3], 20), 2, csvRecord); // mobile
            validateString(columns[4], StringUtil.trimToFitInDB(row[4], 20), 3, csvRecord); // cheque no
            validateAmount(columns[5], row[5], 4, csvRecord); // amount
            validateDate(row[6], 5, csvRecord, currentDate); // date
            validateString(columns[7], row[7], 6, csvRecord); // desc

            isValid = checkDuplicate(recordList, csvRecord, false, null); // check duplicate

//			Double consumedBalance = ledgerDAO.getDailyConsumedBalance(accountId, TransactionTypeConstants.CREDIT, new Date(), null);
//			if(consumedBalance + row[5] > limitModel.getMaximum()){
//				responseCode = "09"; // Your entered amount will exceed the customer's Maximum transaction Credit Limit per Day, please try again.
//				logger.error("Your entered amount will exceed the customer's Maximum transaction Credit Limit per Day, please try again.");
//			}

            csvRecord[7] = String.valueOf(bulkDisbursementsVOModel.getServiceId());
            csvRecord[10] = String.valueOf(bulkDisbursementsVOModel.getProductId());
            csvRecord[11] = (fileInfoModel.getBatchNumber());
            csvRecord[12] = "0";
            csvRecord[13] = "0";
            csvRecord[14] = "0";

            csvRecord[15] = PortalDateUtils.formatDate(now, "ddMMyy");
            csvRecord[16] = PortalDateUtils.formatDate(now, "ddMMyy");
            csvRecord[17] = bulkDisbursementsVOModel.getCreatedByAppUserModel().getAppUserId().toString();
            csvRecord[18] = bulkDisbursementsVOModel.getCreatedByAppUserModel().getAppUserId().toString();

            csvRecord[19] = CommonUtils.getDefaultIfNull(bulkDisbursementsVOModel.getBiometricVerification(), false) ? "1" : "0";
            csvRecord[20] = CommonUtils.getDefaultIfNull(bulkDisbursementsVOModel.getLimitApplicable(), false) ? "1" : "0";
            csvRecord[30] = "0";
            csvRecord[31] = "0";    //fed
            csvRecord[32] = "0";    //charges

            // select only valid records for further processing
            if (isValid) {
                List<Double> list = commissionAmountsMap.get(Double.valueOf(csvRecord[4]));
                Double fed = 0.0;
                Double charges = 0.0;

                if (list.size() == 2) {
                    if (null != list.get(0)) {
                        fed = list.get(0);
                    }
                    charges = list.get(1);

                    totalCharges += charges;
                    totalFED += fed;
                    validRecordTotalAmount += Double.valueOf(csvRecord[4]);

                    //validRecords++;
                } else {
                    csvRecord[8] = "0";
                    csvRecord[9] = "Error in charges calculation";

                    //invalidCount++;
                    isValid = false;
                }

                csvRecord[31] = String.valueOf(fed); // fed
                csvRecord[32] = String.valueOf(charges); // charges
            } else {
                //invalidCount++;
                if (Double.valueOf(csvRecord[4]) > 200000d || Double.valueOf(csvRecord[4]) < 1d) {
                    csvRecord[8] = "0";
                    csvRecord[9] = "Amount should not be greater than 200k";
                }
                isValid = false;
            }

            //Checking CustomerAccountType of the given mobileNo
            String mobileNo = StringUtil.trimToFitInDB(row[3], 20);
            AppUserModel appUserModel = getCommonCommandManager().loadAppUserByMobileAndType(mobileNo);

            if (appUserModel != null) {
                CustomerModel customerModel = getCommonCommandManager().getCustomerModelById(appUserModel.getCustomerId());

                if (isValid) {
                    if (appUserModel.getAccountStateId().equals(AccountStateConstants.ACCOUNT_STATE_COLD) ||
                            appUserModel.getAccountStateId().equals(AccountStateConstants.ACCOUNT_STATE_DORMANT)) {
                        isValid = true;
                    } else {
                        isValid = false;
                        csvRecord[8] = "0";
                        csvRecord[9] = "Invalid Account State";
                    }
                }
                if (customerModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_0) ||
                        customerModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.HRA)) {
                    isValid = false;
                    csvRecord[8] = "0";
                    csvRecord[9] = "Invalid Account Level";
                }

                if (appUserModel.getRegistrationStateId().equals(RegistrationStateConstants.BLACK_LISTED)) {
                    isValid = false;
                    csvRecord[8] = "0";
                    csvRecord[9] = "Account is Blacklisted";
                }

                AccountModel accountModel = accountDAO.getAccountModelByCnicAndCustomerAccountTypeAndStatusId(appUserModel.getNic(), customerModel.getCustomerAccountTypeId(), OlaStatusConstants.ACCOUNT_STATUS_ACTIVE);
                String responseCode = "";

                responseCode = verifyDailyLimitForCredit(new Date(), Double.valueOf(row[5]), accountModel.getAccountId(), customerModel.getCustomerAccountTypeId(), null);
                if (!responseCode.equals("00")) {
                    isValid = false;
                    csvRecord[8] = "0";
                    csvRecord[9] = "Daily Credit Limit Reached";
                }
                else if (responseCode.equals("00")) {
                    responseCode = verifyMonthlyLimitForCredit(new Date(), Double.valueOf(row[5]), accountModel.getAccountId(), customerModel.getCustomerAccountTypeId(), null);
                    if (!responseCode.equals("00")) {
                        isValid = false;
                        csvRecord[8] = "0";
                        csvRecord[9] = "Monthly Credit Limit Reached";
                    }
                }
            } else {
                isValid = false;
                csvRecord[8] = "0";
                csvRecord[9] = "Customer Does Not Exist";
            }

            if (isValid) {
                validRecords++;
            } else {
                invalidCount++;
            }

            csvRecord[33] = bulkDisbursementsVOModel.getIsApproved();

            totalAmount += Double.valueOf(csvRecord[4]);
            recordList.add(csvRecord);
        }

        fileInfoModel.setTotalCharges(totalCharges);
        fileInfoModel.setTotalFed(totalFED);
        fileInfoModel.setTotalAmount(totalAmount);

        bulkDisbursementsVOModel.setInvalidCount(invalidCount);

        fileInfoModel.setTotalRecords((long) recordList.size());
        fileInfoModel.setValidRecords((long) validRecords);
        fileInfoModel.setInValidRecords((long) invalidCount);
    }

    private List<Double> getUniqueAmounts(List<String[]> csvRows, boolean nonAcHolder) {
        List<Double> amounts = new ArrayList<>(10);

        if (nonAcHolder) {
            for (String[] row : csvRows) {
                Double individualAmount = Double.valueOf(row[6]);
                if (!amounts.contains(individualAmount) && row.length == 9) {
                    amounts.add(individualAmount);
                }
            }
        } else {
            for (String[] row : csvRows) {
                Double individualAmount = Double.valueOf(row[5]);
                if (!amounts.contains(individualAmount) && row.length == 8) {
                    amounts.add(individualAmount);
                }
            }
        }

        return amounts;
    }

    private Map<Double, List<Double>> getCommissionAmountsMap(List<Double> amounts) throws FrameworkCheckedException {
        Map<Double, List<Double>> commissionAmountsMap = new HashMap<>(10);
        ThreadLocalAppUser.setAppUserModel(bulkDisbursementsVOModel.getCreatedByAppUserModel());
        ProductModel productModel = new ProductModel(bulkDisbursementsVOModel.getProductId());
        productModel.setServiceId(bulkDisbursementsVOModel.getServiceId());
        // TODO this should be source account Tax Regime
        Long taxRegimeModelId = 3L; //Islamabad FED;
        Long segmentId = CommissionConstantsInterface.DEFAULT_SEGMENT_ID;

        for (Double txAmount : amounts) {
            List<Double> commissionAmounts = new ArrayList<>(2);
            try {
                CommissionWrapper commissionWrapper = this.bulkDisbursementsManager.calculateCommission(productModel, txAmount, segmentId, Boolean.FALSE, null, Boolean.FALSE, taxRegimeModelId);
                CommissionAmountsHolder commissionAmountsHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
                commissionAmounts.add(commissionAmountsHolder.getStakeholderCommissionsMap().get(CommissionConstantsInterface.FED_STAKE_HOLDER_ID));
                commissionAmounts.add(commissionAmountsHolder.getTransactionProcessingAmount());
            } catch (Exception e) {
                logger.error(e.getMessage());

                e.printStackTrace();
            }

            commissionAmountsMap.put(txAmount, commissionAmounts);
        }

        return commissionAmountsMap;
    }

    private void validateString(String title, String input, int valueIndex, String[] csvRecord) {
        String extractedValue = StringUtil.trimToEmpty(input);
        boolean isValid = !extractedValue.isEmpty();
        populatedRecord(title, extractedValue, isValid, valueIndex, csvRecord);
    }

    private void validateCNIC(String title, String input, int valueIndex, String[] csvRecord) {
        String extractedValue = StringUtil.trimToEmpty(input);
        boolean isValid = !extractedValue.isEmpty();
        isValid = isValid && CommonUtils.isValidCnic(extractedValue);
        populatedRecord(title, extractedValue, isValid, valueIndex, csvRecord);

    }

    private void validateMobile(String title, String input, int valueIndex, String[] csvRecord) {
        String extractedValue = StringUtil.trimToEmpty(input);
        boolean isValid = !extractedValue.isEmpty();
        if (isValid && extractedValue.length() == 10 && !extractedValue.startsWith("0")) {
            extractedValue = "0" + extractedValue;
        }

        isValid = isValid && CommonUtils.isValidMobileNo(extractedValue);

        populatedRecord(title, extractedValue, isValid, valueIndex, csvRecord);
    }

    private void validateAmount(String title, String input, int valueIndex, String[] csvRecord) {
        String extractedValue = StringUtil.trimToEmpty(input);
        boolean isValid = !extractedValue.isEmpty();
        Double amount = 0.0;
        String failureReason = null;

        if (isValid) {
            try {
                amount = CommonUtils.getDoubleOrDefaultValue(extractedValue);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }

            if (amount > 2000000d || amount < 1d) {
                isValid = false;
            }
//			isValid = amount > 1d;

            if (!isValid) {
                failureReason = "Invalid " + title;
            }
        } else {
            failureReason = title + " is required.";
            extractedValue = "0";
        }

        populatedRecord(extractedValue, isValid, valueIndex, csvRecord, failureReason);
    }

    private void validateDate(String input, int valueIndex, String[] csvRecord, Date currentDate) {
        String extractedValue = StringUtil.trimToEmpty(input);
        boolean isValid = !extractedValue.isEmpty();

        String failureReason = null;
        if (!isValid) {
            failureReason = "Payment date is required";
            extractedValue = "010100";
        } else if (extractedValue.length() != 6) {
            isValid = false;
            failureReason = "Invalid Payment Date";
        } else {
            try {
                Date paymentDate = PortalDateUtils.parseStringAsDate(extractedValue, CSV_DATE_FORMAT);
                if (paymentDate.before(currentDate)) {
                    isValid = false;
                    failureReason = "Invalid Payment Date";
                }
            } catch (Exception exp) {
                isValid = false;
                failureReason = "Invalid Payment Date";

                logger.error(exp.getMessage());
            }
        }

        populatedRecord(extractedValue, isValid, valueIndex, csvRecord, failureReason);
    }

    private void populatedRecord(String title, String extractedValue, boolean isValid, int valueIndex, String[] csvRecord) {
        String failureReason = null;
        if (extractedValue.isEmpty()) {
            extractedValue = " - ";
            failureReason = title + " is required";
        }

        csvRecord[valueIndex] = extractedValue;

        if (!isValid) {
            csvRecord[8] = "0";

            if (csvRecord[9] == null)
                csvRecord[9] = failureReason != null ? failureReason : "Invalid " + title;
        }
    }

    private void populatedRecord(String extractedValue, boolean isValid, int valueIndex, String[] csvRecord, String reason) {
        csvRecord[valueIndex] = extractedValue;

        if (!isValid) {
            csvRecord[8] = "0";

            if (csvRecord[9] == null)
                csvRecord[9] = reason;
        }
    }

    private boolean checkDuplicate(List<String[]> recordList, String[] csvRecord, boolean walkIn, List<String> blackListedCnic) throws Exception {
        boolean isValid = CommonUtils.convertToBoolean(csvRecord[8]);
        if (!isValid) {
            return false;
        }

        String cnic = csvRecord[21];
        String mobileNo = csvRecord[2];
        for (String[] record : recordList) {

            if (walkIn) {
                if (record[21].equalsIgnoreCase(cnic)) {
                    csvRecord[8] = "0";
                    if (csvRecord[9] == null)
                        csvRecord[9] = "Duplicate CNIC";

                    break;
                }
            }

            if (record[2].equalsIgnoreCase(mobileNo)) {
                csvRecord[8] = "0";
                if (csvRecord[9] == null)
                    csvRecord[9] = "Duplicate Mobile No";

                break;
            }
        }

        isValid = CommonUtils.convertToBoolean(csvRecord[8]);
        if (!isValid) {
            return false;
        }

        if (walkIn) {
            if (blackListedCnic.contains(cnic)) {
                isValid = false;
                csvRecord[8] = "0";
                if (csvRecord[9] == null)
                    csvRecord[9] = "CNIC is blacklisted";
            } else {
                // validate CNIC & Mobile with existing data
                int result = mfsAccountManager.validateCNIC(cnic, mobileNo);
                switch (result) {
                    case 0:
                        isValid = false;
                        csvRecord[8] = "0";
                        if (csvRecord[9] == null)
                            csvRecord[9] = "Mobile/CNIC is already in use";
                        break;

                    case 1:
                        isValid = false;
                        csvRecord[8] = "0";
                        if (csvRecord[9] == null)
                            csvRecord[9] = "CNIC is blacklisted";
                        break;

                    case 2:
                        break;    // walk-in needs to be created
                    case 3:
                        csvRecord[24] = "Update Required";
                        break; // CNIC matches but mobile no update is required
                    case 4:
                        csvRecord[24] = "Successful";
                        break; // walk-in with same CNIC & Mobile already exists
                }
            }
        } else {
            // validate Mobile with existing data
            int result = mfsAccountManager.validateMobileNo(csvRecord[2], fileInfoModel.getAppUserTypeId());
            switch (result) {
                case 0:
                    isValid = false;
                    csvRecord[8] = "0";
                    if (csvRecord[9] == null)
                        csvRecord[9] = "No record found";
                    break;

                case 1:
                    isValid = false;
                    csvRecord[8] = "0";
                    if (csvRecord[9] == null)
                        csvRecord[9] = "User is blacklisted";
                    break;

                case 2:
                    isValid = false;
                    csvRecord[8] = "0";
                    if (csvRecord[9] == null)
                        csvRecord[9] = "A/c is closed";
                    break;

//				case 3 :
//					isValid = false;
//					csvRecord[8] = "0";
//					if (csvRecord[9] == null)
//						csvRecord[9] = "Invalid Registration state";
//					break;
            }
        }

        return isValid;
    }

/*	private AccountControlManager getAccountControlManager() {
		ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
		return (AccountControlManager) applicationContext.getBean("accountControlManager");
	}*/

    private String verifyMonthlyLimitForCredit(Date transactionDateTime, Double amountToAdd, Long accountId, Long customerAccountTypeId, Long handlerId) throws FrameworkCheckedException {
        String responseCode = "";
        try {
            LimitModel limitModel = new LimitModel();
            if (customerAccountTypeId.equals(CustomerAccountTypeConstants.BLINK)) {
                BlinkCustomerLimitModel blinkCustomerLimitModel = this.limitManager.getBlinkCustomerLimitByTransactionType(TransactionTypeConstants.CREDIT, LimitTypeConstants.MONTHLY, customerAccountTypeId, accountId);
                if (blinkCustomerLimitModel != null) {
                    limitModel.setMaximum(Double.valueOf(blinkCustomerLimitModel.getMaximum()));
                    if (blinkCustomerLimitModel.getIsApplicable() == 1) {
                        limitModel.setIsApplicable(true);
                    }
                    limitModel.setCustomerAccountTypeId(blinkCustomerLimitModel.getCustomerAccTypeId());
                }
            } else {

                limitModel = this.limitManager.getLimitByTransactionType(TransactionTypeConstants.CREDIT, LimitTypeConstants.MONTHLY, customerAccountTypeId);
            }
//			LimitModel limitModel = this.limitManager.getLimitByTransactionType(TransactionTypeConstants.CREDIT,LimitTypeConstants.MONTHLY,customerAccountTypeId);
            if (limitModel != null) {
                if (limitModel.getIsApplicable() && limitModel.getMaximum() != null) {
                    Calendar startCalendar = Calendar.getInstance();
                    startCalendar.setTime(transactionDateTime);
                    startCalendar.set(Calendar.DAY_OF_MONTH, 1);
                    PortalDateUtils.resetTime(startCalendar);
                    Date startDate = startCalendar.getTime();
                    //commenting Consumed Balance to Only get them monthly balance
//					Double consumedBalance = ledgerDAO.getConsumedBalanceByDateRange(accountId,TransactionTypeConstants.CREDIT, startDate, transactionDateTime, handlerId);
                    Double consumedBalance = 0.0;
                    if (consumedBalance != null) {
                        if (consumedBalance + amountToAdd > limitModel.getMaximum()) {
                            responseCode = "11";// Your entered amount will exceed the customer's Maximum transaction Credit Limit per Month, please try again.
                            logger.error("Your entered amount will exceed the customer's Maximum transaction Credit Limit per Month, please try again.");
                        } else {
                            responseCode = "00";//Success Message
                        }
                    }
                } else {
                    responseCode = "00"; //Success Message when limit is not applicable
                }
            } else {
                responseCode = "10"; // No Limit is defined for this data (Monthly Limit for Credit).
                logger.error("No Limit is defined for this data (Monthly Limit for Credit.");
            }
        } catch (Exception ex) {
            logger.error("Error in AccountManagerImpl.verifyMonthlyLimitForCredit() :: " + ex.getMessage() + " :: Exception " + ex);
            responseCode = "26";
        }
        return responseCode;
    }

    private String verifyDailyLimitForCredit(Date transactionDateTime, Double amountToAdd, Long accountId, Long customerAccountTypeId, Long handlerId) throws FrameworkCheckedException {
        logger.info("Start of verifyDailyLimitForCredit at Time :: " + new Date());
        String responseCode = "";
        try {
            LimitModel limitModel = new LimitModel();
            if (customerAccountTypeId.equals(CustomerAccountTypeConstants.BLINK)) {
                BlinkCustomerLimitModel blinkCustomerLimitModel = this.limitManager.getBlinkCustomerLimitByTransactionType(TransactionTypeConstants.CREDIT, LimitTypeConstants.DAILY, customerAccountTypeId, accountId);
                if (blinkCustomerLimitModel != null) {
                    limitModel.setMaximum(Double.valueOf(blinkCustomerLimitModel.getMaximum()));
                    if (blinkCustomerLimitModel.getIsApplicable() == 1) {
                        limitModel.setIsApplicable(true);
                    }
                    limitModel.setCustomerAccountTypeId(blinkCustomerLimitModel.getCustomerAccTypeId());
                }
            } else {
                limitModel = this.limitManager.getLimitByTransactionType(TransactionTypeConstants.CREDIT, LimitTypeConstants.DAILY, customerAccountTypeId);
            }
//			LimitModel limitModel = this.limitManager.getLimitByTransactionType(TransactionTypeConstants.CREDIT, LimitTypeConstants.DAILY,customerAccountTypeId);
            if (limitModel != null) {

                if (limitModel.getIsApplicable() && limitModel.getMaximum() != null) {

                    //commenting Consumed Balance to Only get them monthly balance
//                    Double consumedBalance = ledgerDAO.getDailyConsumedBalance(accountId, TransactionTypeConstants.CREDIT, transactionDateTime, handlerId);
                    Double consumedBalance = 0.0;

                    if (consumedBalance != null) {
                        //commenting amountToAdd Balance to Only get them monthly balance
                        if (consumedBalance + 0.0 > limitModel.getMaximum()) {
                            responseCode = "09"; // Your entered amount will exceed the customer's Maximum transaction Credit Limit per Day, please try again.
                            logger.error("Your entered amount will exceed the customer's Maximum transaction Credit Limit per Day, please try again.");
                        } else {
                            responseCode = "00"; //Success Message
                        }
                    }
                } else {
                    responseCode = "00"; //Success Message when limit is not applicable
                }
            } else {
                responseCode = "08"; // No Limit is defined for this data (Daily Limit for Credit).
                logger.error("No Limit is defined for this data (Daily Limit for Credit).");
            }
        } catch (Exception e) {
            logger.error("Error in AccountManagerImpl.verifyDailyLimitForCredit() :: " + e.getMessage() + " :: Exception " + e);
            responseCode = "25";
        }
        logger.info("End of verifyDailyLimitForCredit at Time :: " + new Date());
        return responseCode;
    }

    private CommonCommandManager getCommonCommandManager() {
        ApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        CommonCommandManager commonCommandManager = (CommonCommandManager) webApplicationContext.getBean("commonCommandManager");
        return commonCommandManager;
    }
}