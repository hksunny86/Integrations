package com.inov8.agentmate.util;

import android.bluetooth.BluetoothDevice;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.inov8.agentmate.model.BankModel;
import com.inov8.agentmate.model.CategoryModel;
import com.inov8.agentmate.model.CitiesModel;
import com.inov8.agentmate.model.PaymentReasonModel;
import com.inov8.jsbl.sco.BuildConstants;
import com.paysyslabs.instascan.Fingers;

public class ApplicationData {

    public static ArrayList<?> listBankAccounts = null;
	public static ArrayList<CategoryModel> listCategories = null;
	public static ArrayList<CitiesModel> listCities = null;
	public static ArrayList<PaymentReasonModel> listPaymentReasons = null;
	public static String accountOpenMinAmount=null;
    public static String accountOpenMaxAmount=null;
	public static ArrayList<BankModel> banks = null;

    public static String latitude=null;
    public static String longitude=null;

    public static boolean isDummyFlow = BuildConstants.isDummyFlow;
    public static boolean isCustomIP = BuildConstants.isCustomIP;

	public static boolean isLoginFlow = false;
	public static String userId = null;
	public static BluetoothDevice currentDevice = null;
	public static int errorCount = 0;
	public static String WSQ = null;
    public static String deviceId = null;
	public static String balance = null;
	public static String bvsdeviceModel = null;
	public static String formattedBalance = null;
	public static String firstName = null;
	public static String lastName = null;
	public static String cnic = null;
	public static String mobileNo = null;
	public static String isPinChangeRequired = null;
	public static String agentMobileNumber = null;
	public static String bankId = null;
	public static String accountId = null;
    public static String agentAreaName = null;
	public static boolean isLogin = false;
	public static boolean isCameraPreviewLoaded = false;
	public static boolean isTermsAndConditionAccepted = false;
	public static boolean isDestroyed = false;
	public static boolean isBluetoothPrinterConnected = false;
	public static int agentAccountType = 0;
	public static int pinRetryCount = 0;
    public static String webUrl = null;
    public static boolean isCnicExpiryRequired = false;
    public static String sessionId = "232322312124";

    public static boolean nadraCall = false;
	public static boolean nadraOtcCall = false;
    public static boolean isBvsEnabledDevice = false;
    public static boolean isPaysysEnabled = true;
    public static String isAgentAllowedBvs = null;
    public static String bvsErrorMessage = null;
	public static Fingers currentFinger = null;
	public static String currentFingerIndex = null;
    public static String bvsDeviceName = null;

	public static String getFormattedDate() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		return dateFormat.format(new Date());
	}

	public static String getFormattedDateShort() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
		return dateFormat.format(new Date());
	}

	public static String getFormattedTime() {
		DateFormat dateFormat = new SimpleDateFormat("hh:mm aaa");
		return dateFormat.format(new Date());
	}

	public static String getFormattedTimeWithSeconds() {
		DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss aaa");
		return dateFormat.format(new Date());
	}
	
	public static void resetPinRetryCount(){
		pinRetryCount = 0;
	}
}