package com.inov8.jsblconsumer.util;

import static com.inov8.jsblconsumer.util.Constants.APP_DOWNLOAD_URL;

/**
 * Created by DELL on 11/9/2016.
 */

public class AppMessages {
    public static final String ERROR_GREATER_START_DATE = "Start date should not be greater than end date.";
    public static final String INVALID_EMAIL_ID = "Please enter valid email address.";
    public static final String EXCEPTION_PROCESSING_ERROR = "We are unable to process your request at the moment. Please try again later.";
    public static final String ERROR_EMPTY_FIELD = "The field must not be empty";
    public static final String ERROR_EMPTY_PIN = "PIN must not be empty";
    public static final String ERROR_EMPTY_PASSWORD = "Password must not be empty";
    public static final String COMING_SOON = "Coming Soon...";
    public static final String APPLICATION_SSL_EXPIRE = "Please download the new Consumer Application. The older version is now obsolete and will not work.";
    public static final String INTERNET_CONNECTION_PROBLEM = "There is no or poor internet connection. Please connect to stable internet connection and try again.";
    public static final String LOCATION_PERMISSION_NOT_AVAILABLE = "Location permission is disabled. To enable again go to settings.";
    public static final String HEADING_LOCATION_PERMISSION = "LOCATION PERMISSION";
    public static final String CAMERA_PERMISSION_NOT_AVAILABLE = "Camera permission is disabled. To enable again go to settings.";
    public static final String HEADING_CAMERA_PERMISSION = "CAMERA PERMISSION";

    public static final String EXCEPTION_HTTP_UNAVAILABLE = "Service unavailable due to technical difficulties. Please try again or contact service provider.";
    public static final String EXCEPTION_TIME_OUT = "This seems to be taking longer than usual. Please try again later.";
    public static final String EXCEPTION_INVALID_RESPONSE = "We are unable to process your request at the moment. Please try again later.";
    public static final String EXCEPTION = "Connection with the server can not be established at this time. Please try again or contact your service provider.";
    public static final String EXCEPTION_GENERIC = "Your session has expired. Please try again.";

    public static final String MBANKS_LIST_ERROR = "IBFT banks list not found.";
    public static final String TPURPS_LIST_ERROR = "Transaction purpose list not found.";
    public static final String FETCH_BALANCE_ERROR = "Unable to fetch the user balance.";
    public static final String BANK_ACCOUNT_ERROR = "Your branchless banking account is not linked. Please contact your service provider.";
    public static final String BILL_ALREADY_PAID = "Bill Already Paid.";
    public static final String NEW_LOGIN_PIN = "Your new pin has been sent to your register phone number.";
    public static final String CHALLAN_ALREADY_PAID = "Challan Already Paid.";
    public static final String INVLAID_SENDER_MOBILE_NUMBER = "Sender Mobile Number is Invalid.";
    public static final String INVLAID_RECEIVER_MOBILE_NUMBER = "Receiver Mobile Number is Invalid.";
    public static final String INVLAID_MOBILE_NUMBER = "Entered Mobile Number is Invalid.";
    public static final String INVLAID_ACCOUNT_NUMBER = "Entered Account Number is Invalid.";
    public static final String INVLAID_SENDER_CNIC = "Sender CNIC is invalid.";
    public static final String INVLAID_RECEIVER_CNIC = "Receiver CNIC is invalid.";
    public static final String INVALID_LOGIN_LENGTH = "Length should be of 11 digits.";
    public static final String INVALID_MOBILE_ID_LENGTH = "Length should be of 4 digits.";
    public static final String INVALID_MOBILE_NO_LENGTH = "Length should be of 7 digits.";
    public static final String INVALID_PIN_LENGTH = "PIN should be of 4 digits.";
    public static final String INVALID_LOGIN_PIN_LENGTH = "PIN should be of "+Constants.LOGIN_PIN_LENGTH+" digits.";
    public static final String INVALID_LOGIN_PASSWORD_LENGTH = "Password should be of "+Constants.LOGIN_PIN_LENGTH+" digits.";
    public static final String INVALID_ACCOUNT_OPEN_PIN_LENGTH = "PIN should be of "+Constants.ACCOUNT_OPENING_OTP_LENGTH+" digits.";
//    public static final String INVLAID_PASSWORD_LENGTH = "Length should be between 4 to 20 chars.";
public static final String INVLAID_PASSWORD_LENGTH = "Length should be 4 digits.";
    public static final String INVLAID_MPIN_LENGTH = "Length should be of 4 digits.";
    public static final String INVLAID_PASSWORD_TYPE = "Password should be a combination of alphanumeric chars.";
    public static final String PASSWORD_MISMATCH = "New and Confirm Password Mismatch.";
    public static final String PIN_MISMATCH = "PIN and Confirm PIN Mismatch.";

    public static final String MPIN_MISMATCH = "New and Confirm MPIN Mismatch.";
    public static final String ATM_PIN_MISMATCH = "New and Confirm ATM PIN Mismatch.";
    public static final String SAME_PASSWORDS = "New and Old Password cannot be same.";
    public static final String SAME_MPINS = "New and Old MPIN cannot be same.";
    public static final String SET_MPINS = "Dear Customer, you have not setup your MPIN yet, please set up the MPIN";
    public static final String SAME_ATM_PINS = "New and Old ATM PIN cannot be same.";
    public static final String ERROR_MULTIPLE = "You have entered invalid amount.";
    public static final String ERROR_AMOUNT_INVALID = "Invalid amount entered.";
    public static final String SESSION_EXPIRED = "Application Session has expired. Please Re-Login.";
    public static final String ALERT_HEADING = "Alert Notification";
    public static final String CONGR_HEADING = "Congratulations";
    public static final String DEBIT_CARD_REQUEST = "Debit Card Request";
    public static final String REGISTERATION_REQUIRED = "Would you like to register as a branchless banking customer for ";
    public static final String CANCEL_TRANSACTION = "Are you sure to cancel?";
    public static final String DEBIT_CARD_ISSUENCE = "Do you want to Request for Debit Card Issuance?";
    public static final String APPLICATION_VERSION_LEVEL_CRITICAL = "A new version of App is now available. For enhanced usability and feature please download new "
            + "version from " + APP_DOWNLOAD_URL;
    public static final String APPLICATION_VERSION_LEVEL_OBSOLETE = "Please download the new Customer Application "
            + "from " + APP_DOWNLOAD_URL + ". The older version is now obsolete and will not work.";
    public static final String PLEASE_AGREE_TERMS = "Please agree to the Terms & Conditions.";
    public static final String PIN_LENGTH = "Length should be of 4 digits.";
    public static final String ERROR_MOBILE_NO_START = "Mobile Number must start with 03";
    public static final String ERROR_MOBILE_NO_LENGTH = "Mobile No. should be of 11 digits";
    public static final String ERROR_CNIC_LENGTH = "CNIC should be of 13 digits";
    public static final String ERROR_INVALID_QR_CODE = "Invalid QR code !";
    public static final String ERROR_SCANNING_DATA = "No scan data received!";

    public static final String INVALID_CARD_EXPIRY = "Your cnic is expired. Please contact bank for details.";
    public static final String ALERT_ERROR = "Verification Error";
    public static final String CAMERA_NOT_AVAILABLE_ERROR = "Camera is required to perform Account Opening.";
    public static final String CAMERA_AUTO_FOCUS_ERROR = "Camera with auto focus is required to perform Account Opening.";
    public static final String OS_ERROR = "Minimum OS version 4.4 is required to perform Account Opening.";


    public static final String INVALID_MOBILE_NUMBER = "Length should be of 11 digits.";

    public static final String INVALID_MOBILE_NUMBER_FORMAT = "Mobile No. must start with 03.";
    public static final String DEVICE_NOT_SUPPORTED = "This Device is not supported.";
    public static final String INVALID_APP = "Application Security Compromised!! This is not a valid application.";
    public static final String INVALID_INSTALLER = "Application Security Compromised!! Invalid installer.";
    public static final String INVALID_ENVIRONMENT = "Application Security Compromised!! Invalid environment.";
    public static final String INVALID_DEBUGGABLE = "Application Security Compromised!!";
    public static final String DEVICE_ROOTED = "Device Rooted/Jail Broken!! Data Security Compromised. Any/all actions using this application can lose private data, and therefore result in the possibility of fraudulent activity on your account.";
}
