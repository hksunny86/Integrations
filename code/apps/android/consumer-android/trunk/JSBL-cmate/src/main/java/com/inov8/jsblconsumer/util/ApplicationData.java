package com.inov8.jsblconsumer.util;

import android.os.Bundle;

import com.inov8.jsblconsumer.model.AdModel;
import com.inov8.jsblconsumer.model.CardApplicantTypeModel;
import com.inov8.jsblconsumer.model.CardCategoryModel;
import com.inov8.jsblconsumer.model.CardRankModel;
import com.inov8.jsblconsumer.model.CardTypeModel;
import com.inov8.jsblconsumer.model.CategoryModel;
import com.inov8.jsblconsumer.model.FaqsModel;
import com.inov8.jsblconsumer.model.MbankModel;
import com.inov8.jsblconsumer.model.PaymentReasonModel;
import com.inov8.jsblconsumer.model.TpurpsModel;
import com.paysyslabs.instascan.Fingers;

import java.util.ArrayList;

public class ApplicationData {


    public static boolean isDummyFlow = BuildConstants.isDummyFlow;
    public static boolean isCustomIP = BuildConstants.isCustomIP;
    public static boolean isWebViewOpen = false;
    public static boolean isDestroyed = false;
    public static boolean isLogin = false;
    public static boolean isMpinSet = false;
    public static boolean isMyAccountOpen = false;
    public static boolean isLocatorViewOpen = false;

    public static int agentAccountType = 0;
    public static int pinRetryCount = 0;
    public static ArrayList<PaymentReasonModel> listPaymentReasons = null;

    public static int adType = 0;

    public static String videoLink;

    public static String webUrl;
    public static String mobileNetwork = null;
    public static String userId = null;
    public static String balance = null;
    public static String formattedBalance = null;
    public static String accoutTittle = null;
    public static String firstName = null;
    public static String fee = null;
    public static String lastName = null;
    public static String cnic = null;
    public static String mobileNo = null;
    public static String isPinChangeRequired = null;
    public static String customerMobileNumber = null;
    public static String bankId = null;
    public static String accountId = null;
    public static String faqVersion = null;
    public static String senderIban = null;
    public static Bundle mBundleCatalog = null;

    public static boolean isBvsEnabledDevice = false;
    public static String isAgentAllowedBvs = null;
    public static String bvsErrorMessage = null;
    public static boolean isCameraPreviewLoaded = false;

    public static ArrayList<AdModel> listAds;

    public static ArrayList<?> listBankAccounts = null;
    public static ArrayList<CategoryModel> listCategories = null;
    public static ArrayList<CategoryModel> listCategoriesMyAccount = null;
    public static ArrayList<MbankModel> listMbanks = null;
    public static ArrayList<TpurpsModel> listTpurps = null;
    public static ArrayList<CardTypeModel> listCardTypes = null;
    public static ArrayList<CardRankModel> listCardRanks = null;
    public static ArrayList<CardCategoryModel> listCardCategories = null;
    public static ArrayList<CardApplicantTypeModel> listCardApplicantTypes = null;
    public static ArrayList<FaqsModel> listFaqs = null;
    public static Fingers currentFinger = null;

    public static void resetPinRetryCount() {
        pinRetryCount = 0;
    }


    public static void resetData() {
        listBankAccounts = null;
        listCategories = null;
        listCategoriesMyAccount = null;
        listMbanks = null;
        listTpurps = null;
        listCardTypes = null;
        listCardRanks = null;
        listCardCategories = null;
        listCardApplicantTypes = null;
        listFaqs = null;
        currentFinger = null;
        listPaymentReasons = null;
        senderIban = null;
    }



}