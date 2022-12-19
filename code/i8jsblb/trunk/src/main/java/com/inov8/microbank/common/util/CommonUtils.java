package com.inov8.microbank.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.P2PDetailModel;
import com.inov8.microbank.common.model.TransactionDetailMasterModel;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;

import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CommonUtils {
    public static final Log logger = LogFactory.getLog(CommonUtils.class);

    private static Set<String> invalidPinSet = new HashSet<String>();

    static {
        invalidPinSet.add("0000");
        invalidPinSet.add("1111");
        invalidPinSet.add("2222");
        invalidPinSet.add("3333");
        invalidPinSet.add("4444");
        invalidPinSet.add("5555");
        invalidPinSet.add("6666");
        invalidPinSet.add("7777");
        invalidPinSet.add("8888");
        invalidPinSet.add("9999");
        invalidPinSet.add("1234");
        invalidPinSet.add("2345");
        invalidPinSet.add("3456");
        invalidPinSet.add("4567");
        invalidPinSet.add("5678");
        invalidPinSet.add("6789");
        invalidPinSet.add("9876");
        invalidPinSet.add("8765");
        invalidPinSet.add("7654");
        invalidPinSet.add("6543");
        invalidPinSet.add("5432");
        invalidPinSet.add("4321");
        invalidPinSet.add("3210");
    }

    public static boolean isEmpty(String a) {
        return ((a == null) || (a.trim().equals("")));
    }

    public static void bindCustomDateEditor(ServletRequestDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    public static boolean isInvalidCharacters(String string) {
        String invalidCharacters = "'`~#^&*<>|[]?!@$%*()";
        for (int i = 0; i < string.length(); i++) {
            if (i + 1 <= string.length()) {
                if (invalidCharacters.contains(string.subSequence(i, i + 1))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String generateNumber(int length) {
        Random generator = new Random();
        StringBuffer pin = new StringBuffer();
        for (int i = 0; i < length; i++) {
            pin.append(generator.nextInt(9));
        }
        return pin.toString();
    }

    public static void main(String arg[]) {
        isValidNicExpiry("2019-09-01");
    }

    public static String maskWalkinCustomerCNIC(String cnic) {
        if (cnic != null && !cnic.equals("")) {
            cnic = cnic + CommandFieldConstants.WALK_IN_CUSTOMER_CNIC_SUFFIX;
        }

        return cnic;

    }

    public static Double roundTwoDecimals(Double value) {
        Double roundedValue = new Double(0.0);
        if (value != null) {
            DecimalFormat twoDForm = new DecimalFormat("#.##");
            roundedValue = Double.valueOf(twoDForm.format(value));
        }

        return roundedValue;
    }

    public static Double getDoubleOrDefaultValue(Double value) {
        return value == null ? 0.0 : value;
    }

    public static Double getDoubleOrDefaultValue(Object value) {

        if (value != null) {
            if (value instanceof String) {
                String str = (String) value;
                if (str.trim().length() > 0)
                    return Double.valueOf(str.trim());
            } else if (value instanceof Double) {
                return (Double) value;
            }
        }

        return 0.0;
    }

    public static Double getDoubleFromCommaSeparated(String x) throws ParseException {
        x = x.replaceAll(",", "");
        return (Double.parseDouble(x));
    }

    public static boolean isValidMobileNo(String mobileNo) {
        boolean isValid = false;
        isValid = !GenericValidator.isBlankOrNull(mobileNo) && mobileNo.length() == 11 && mobileNo.startsWith("03") && StringUtils.isNumeric(mobileNo);
        return isValid;
    }

    public static boolean isTwoLeggedProduct(Long productId, Long serviceId) {
        boolean isTwoLegged = false;

        if (CommonUtils.getDefaultIfNull(serviceId, -1l) != -1l) {
            if (serviceId.longValue() == ServiceConstantsInterface.BULK_DISB_NON_ACC_HOLDER)
                isTwoLegged = true;
        }

        // if service is two legged transaction, no need to verify product
        if (isTwoLegged)
            return isTwoLegged;

        if (productId.longValue() == ProductConstantsInterface.ACCOUNT_TO_CASH
                || productId.longValue() == ProductConstantsInterface.CASH_TRANSFER
                || productId.longValue() == ProductConstantsInterface.ACCOUNT_OPENING) {

            isTwoLegged = true;
        }

        return isTwoLegged;
    }

    public static boolean isValidCnic(String cnic) {
        boolean invalid = false;
        invalid = GenericValidator.isBlankOrNull(cnic) || cnic.length() != 13 || !StringUtils.isNumeric(cnic);

        if (invalid)
            return false;

        String start[] = null;
        String end[] = null;
        String invalidStarts = MessageUtil.getMessage("cnic.invalid.start");
        if (!StringUtil.isNullOrEmpty(invalidStarts)) {
            start = invalidStarts.split(",");
        }

        String invalidEnds = MessageUtil.getMessage("cnic.invalid.end");
        if (!StringUtil.isNullOrEmpty(invalidEnds)) {
            end = invalidEnds.split(",");
        }

        if (start != null && start.length != 0) {
            for (String str : start) {
                if (cnic.startsWith(str.trim()))
                    return false;
            }
        }

        if (end != null && end.length != 0) {
            for (String str : end) {
                if (cnic.endsWith(str.trim()))
                    return false;
            }
        }

        return true;
    }

    public static String formatAmount(Double value) {
        String roundedValue = "0.00";
        if (value != null) {
            DecimalFormat twoDForm = new DecimalFormat("0.00");
            roundedValue = twoDForm.format(value);
        }
        return roundedValue;
    }

    /**
     * A utility method used for deleting a file specified.
     * Requires a valid file path that is to be deleted.
     *
     * @param filePath String
     * @return boolean
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        return file.delete();
    }

    /**
     * A utility method used for deleting files specified.
     * Requires a valid file path that is to be deleted.
     *
     * @param filePaths String[]
     * @return boolean
     */
    public static boolean deleteFiles(String filePaths[]) {
        boolean filesDeleted = filePaths != null ? true : false;
        for (int i = 0; i < filePaths.length; i++) {
            filesDeleted = false;
            File file = new File(filePaths[i]);
            try {
                filesDeleted = file.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return filesDeleted;
    }

    public static Double formatAmountTwoDecimal(Double amount) {
        if (amount == null) {
            return 0.0D;
        } else {
            String amt = Formatter.formatDouble(amount);
            return Double.parseDouble(amt);
        }
    }

    public static String formatAmountTwoDecimalString(String amount) {
        if (amount == null) {
            return String.valueOf(0.0D);
        } else {
            String amt = Formatter.formatDouble(Double.valueOf(amount));
            return amt;
        }
    }

    public static Double formatAmountOneDecimal(Double amount) {
        if (amount == null) {
            return 0.0D;
        } else {
            String amt = Formatter.formatOneDouble(amount);
            return Double.parseDouble(amt);
        }
    }

    public static String generateOneTimePin(int length) {
        String pin = null;

        do {
            pin = generateNumber(length);
        }
        while (!validateOneTimePin(pin));

        return pin;
    }

    /**
     * @param pin
     * @return returns true if pin is validated otherwise false
     */
    public static boolean validateOneTimePin(String pin) {
        return pin != null && !"".equals(pin) && !invalidPinSet.contains(pin);
    }

    public static P2PDetailModel prepareP2PDetailModel(TransactionDetailMasterModel oldModel, P2PDetailModel p2pDetailModel) {
        P2PDetailModel oldP2PDetailModel = new P2PDetailModel();

        oldP2PDetailModel.setSenderMobile(oldModel.getSaleMobileNo());
        oldP2PDetailModel.setSenderCNIC(oldModel.getSenderCnic());
        oldP2PDetailModel.setRecipientMobile(oldModel.getRecipientMobileNo());
        oldP2PDetailModel.setRecipientCNIC(oldModel.getRecipientCnic());
        oldP2PDetailModel.setTransactionCode(p2pDetailModel.getTransactionCode());
        oldP2PDetailModel.setAgent1Id(p2pDetailModel.getAgent1Id());
        oldP2PDetailModel.setAgent1MobileNo(p2pDetailModel.getAgent1MobileNo());
        oldP2PDetailModel.setAgent1Name(p2pDetailModel.getAgent1Name());
        oldP2PDetailModel.setComments(p2pDetailModel.getComments());

        return oldP2PDetailModel;
    }

    public static String getBillingMonthFromDueDate(Date dueDate) {
        String strDueDate = "";
        if (dueDate != null) {
            LocalDate lc = new LocalDate(dueDate);
            DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM yyyy");
            strDueDate = formatter.print(lc);
        }
        return strDueDate;
    }

    public static boolean convertToBoolean(String str) {
        boolean result = false;
        if (!StringUtil.isNullOrEmpty(str)) {
            str = str.trim();

            result = str.equalsIgnoreCase("TRUE") || str.equalsIgnoreCase("T")
                    || str.equalsIgnoreCase("YES") || str.equalsIgnoreCase("Y")
                    || str.equals("1");
        }

        return result;
    }

    public static String convertToBit(Boolean value) {
        return value != null && value ? "1" : "0";
    }

    public static <T> T getDefaultIfNull(T actualValue, T defaultValue) {
        return actualValue == null ? defaultValue : actualValue;
    }

    //********************************************************************
    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static void copyNotNullBeanProperties(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

    public static String getParameter(BaseWrapper baseWrapper, String fieldName) {
        Object object = baseWrapper.getObject(fieldName);

        return object != null ? object.toString().trim() : "";
    }


    public static String decryptPin(String pin, byte encryption_type) {
        switch (encryption_type) {
            case EncryptionUtil.ENCRYPTION_TYPE_RSA:
                if (null != pin && !pin.equals("")) {
                    pin = StringUtil.replaceSpacesWithPlus(pin);
                    pin = EncryptionUtil.doDecrypt(XMLConstants.ENCRYPTION_KEY, pin);
                }
                break;
            case EncryptionUtil.ENCRYPTION_TYPE_AES:
                if (null != pin && !pin.equals("")) {
                    pin = StringUtil.replaceSpacesWithPlus(pin);
                    pin = EncryptionUtil.decryptWithAES(XMLConstants.AES_ENCRYPTION_KEY, pin);
                }
                break;
        }

        return new String(pin);
    }

    /**
     * This method adjusts transaction and total amounts with inclusive charges (if applied) based on third party charges configurations.
     *
     * @param wrapper
     * @return
     */
    public static Map<String, Double> amountsWithIncChargesAdjustment(WorkFlowWrapper wrapper) {

        CommissionAmountsHolder commissionAmountsHolder = wrapper.getCommissionAmountsHolder();
        return adjustAmounts(commissionAmountsHolder.getTransactionAmount(),
                commissionAmountsHolder.getTotalAmount(),
                commissionAmountsHolder.getTotalInclusiveAmount(),
                commissionAmountsHolder.getTransactionProcessingAmount(),
                wrapper.getInclChargesCheck());
    }

    private static Map<String, Double> adjustAmounts(Double txAmount, Double totalAmount, Double inclusiveCharges, Double exclusiveCharges, Boolean thirdPartyCharges) {
        txAmount = getDoubleOrDefaultValue(txAmount);
        totalAmount = getDoubleOrDefaultValue(totalAmount);
        inclusiveCharges = getDoubleOrDefaultValue(inclusiveCharges);
        exclusiveCharges = getDoubleOrDefaultValue(exclusiveCharges);
        Double totalInclExclCharges = exclusiveCharges;

        if (thirdPartyCharges == null || !thirdPartyCharges) {
            txAmount -= inclusiveCharges;
            totalAmount -= inclusiveCharges;
            totalInclExclCharges += inclusiveCharges;
        } else {
            inclusiveCharges = 0d;
        }

        Map<String, Double> map = new HashMap<>(4);
        map.put(CommandFieldConstants.KEY_TXAM, txAmount);
        map.put(CommandFieldConstants.KEY_TAMT, totalAmount);
        map.put(CommandFieldConstants.KEY_INCC, inclusiveCharges);
        map.put(CommandFieldConstants.KEY_INCC_EXCC, totalInclExclCharges);

        return map;
    }

    public static String decorateProductName(String productName) {
        String returnProductName = productName;
        if (!StringUtil.isNullOrEmpty(productName)) {
            if (productName.endsWith(" Bill")) {
                returnProductName = productName.substring(0, productName.length() - 5);
            } else if (productName.endsWith(" - Customer")) {
                returnProductName = productName.substring(0, productName.length() - 11);
            }
        }

        return returnProductName;
    }

    public static Boolean isTwoDecimalPlacesAmount(String input) {
        Boolean isTwoDecimalAmount = true;
        if (StringUtil.isNullOrEmpty(input)) {

        } else {
            if (input.length() > 3 && input.indexOf(".") != -1 && (input.length() - input.indexOf(".")) > 3) {
                isTwoDecimalAmount = false;
            }
        }
        return isTwoDecimalAmount;
    }

    public static String escapeUnicode(String input) {
        logger.info("Input string is " + input);
        StringBuilder b = new StringBuilder(input.length());
        java.util.Formatter f = new java.util.Formatter(b);
        for (char c : input.toCharArray()) {
            if (c < 128) {
                b.append(c);
            } else {
                f.format("\\%04x", (int) c);
            }
        }
        logger.info("Output string is " + b.toString());
        return b.toString();
    }

    public static boolean isValidNicExpiry(String dateToValidate) {
        Date date = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            date = simpleDateFormat.parse(dateToValidate);
        } catch (ParseException ex) {
            logger.error("Error while validating CNIC Expiry Date :: " + ex.getMessage(), ex);
        }
        if (date == null)
            return false;
        else if (date.after(new Date()))
            return true;
        else
            return false;
    }

    public static boolean isValidDate(String dateToValidate, Boolean validateAfterCheck, Boolean isDOB) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            date = sdf.parse(dateToValidate);
            if (!dateToValidate.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        if (date != null && validateAfterCheck && !isDOB) {
            if (!date.after(new Date()))
                return false;
        } else if (date != null && !validateAfterCheck && !isDOB) {
            if (date.after(new Date()))
                return false;
        } else if (date != null && isDOB) {
            Calendar a = getCalendar(date);
            Calendar b = getCalendar(new Date());
            int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
            if (diff < 18)
                date = null;
        }
        return date != null;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }

    public static String getJSON(Object obj) {

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("JSON Parsing Error in getJSON() ", e);
        }
        return null;
    }

    public static void checkAgeLimit(String year, String month, String day, Integer ageLimit) throws CommandException {
        LocalDate birthdate = new LocalDate(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
        LocalDate now = new LocalDate();
        Period period = new Period(birthdate, now, PeriodType.yearMonthDay());
        try {
            if (period.getYears() < ageLimit) {
                throw new CommandException("Customer does not Open Account Due to age " + ageLimit, ErrorCodes.AGELIMIT_ACCOUNT_BLOCK, ErrorLevel.MEDIUM);
            } else {
                logger.info("Your age is " + period.getYears() + "an your age Limit is " + ageLimit + "So, You can perform Action");
            }
        } catch (CommandException e) {
            throw new CommandException(MessageUtil.getMessage("MINOR.ACCOUNT.OPENING"), ErrorCodes.AGELIMIT_ACCOUNT_BLOCK, ErrorLevel.MEDIUM);
        }
    }

    public static void checkCnicExpiry(String dateToValidate) throws CommandException {
        Date date = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                date = simpleDateFormat.parse(dateToValidate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (date == null) {
                throw new CommandException("YOUR CNIC IS EXPIRE,SO YOU CANNOT OPEN ACCOUNT " + dateToValidate, ErrorCodes.CNIC_EXPIRE_ACCOUNT_BLOCK, ErrorLevel.MEDIUM);
            } else if (date.after(new Date())) {
                logger.info("Your Cnic Is Not Expire");
            } else {
                throw new CommandException("YOUR CNIC IS EXPIRE,SO YOU CANNOT OPEN ACCOUNT " + dateToValidate, ErrorCodes.CNIC_EXPIRE_ACCOUNT_BLOCK, ErrorLevel.MEDIUM);
            }
        } catch (CommandException e) {
            throw new CommandException(MessageUtil.getMessage("CNIC.EXPIRE.ACCOUNT.OPENING"), ErrorCodes.CNIC_EXPIRE_ACCOUNT_BLOCK, ErrorLevel.MEDIUM);
        }
    }
}
