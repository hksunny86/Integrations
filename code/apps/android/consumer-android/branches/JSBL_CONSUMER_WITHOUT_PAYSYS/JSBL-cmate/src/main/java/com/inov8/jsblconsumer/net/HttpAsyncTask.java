package com.inov8.jsblconsumer.net;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;

import com.inov8.jsblconsumer.activities.BaseActivity;
import com.inov8.jsblconsumer.activities.BaseCommunicationActivity;
import com.inov8.jsblconsumer.activities.MainMenuActivity;
import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.model.HttpResponseModel;
import com.inov8.jsblconsumer.model.MessageModel;
import com.inov8.jsblconsumer.parser.XmlParser;
import com.inov8.jsblconsumer.ui.components.PopupDialogs;
import com.inov8.jsblconsumer.util.AppLogger;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.ApplicationData;
import com.inov8.jsblconsumer.util.Constants;

import java.util.Hashtable;
import java.util.List;

public class HttpAsyncTask extends AsyncTask<String, Void, HttpResponseModel> {
    private BaseCommunicationActivity activity;
    private HttpResponseModel response = null;
    private HttpCalls httpCall = null;
    private int command = -1;
    private Dialog dialog;


    public HttpAsyncTask() {
    }

    public HttpAsyncTask(BaseCommunicationActivity activity) {
        this.activity = activity;
    }

    @Override
    protected HttpResponseModel doInBackground(String... params) {
        httpCall = HttpCalls.getInstance(activity);
        boolean verifyPinFlag = true;
        String pin = null;
        command = Integer.parseInt(params[0]);

        switch (command) {
            case Constants.CMD_CASH_WITHDRAWAL:
            case Constants.CMD_FUNDS_TRANSFER_BLB2BLB:
            case Constants.CMD_ADVANCE_LOAN:
            case Constants.CMD_FUNDS_TRANSFER_BLB2CNIC:
            case Constants.CMD_FUNDS_TRANSFER_BLB2CORE:
            case Constants.CMD_BILL_PAYMENT:
            case Constants.CMD_TRANSFER_OUT:
            case Constants.CMD_TRANSFER_IN:
            case Constants.CMD_RETAIL_PAYMENT:
            case Constants.CMD_BOOKME_PAYMENT:
            case Constants.CMD_MINI_STATMENT:
            case Constants.CMD_L1_TO_HRA:
            case Constants.CMD_SCHEDULE_PAYMENT:
//            case Constants.CMD_CHECK_BALANCE:
            case Constants.CMD_RETAIL_PAYMENT_MPASS:
//            case Constants.CMD_CHANGE_MPIN:
            case Constants.CMD_MY_LIMITS:
            case Constants.CMD_DEBIT_CARD_ISSUANCE:
            case Constants.CMD_DEBIT_CARD_ACTIVATION:
            case Constants.CMD_DEBIT_CARD_BLOCK:
            case Constants.CMD_ATM_PIN_GENERATE_CHANGE:
            case Constants.CMD_MINI_LOAD:
            case Constants.CMD_COLLECTION_PAYMENT:
            case Constants.CMD_DEBIT_CARD_CONFIRMATION:
            case Constants.CMD_HRA_TO_WALLET:
            case Constants.CMD_DEBIT_CARD_ACTIVATION_CHANGE_PIN:
            case Constants.CMD_DEBIT_CARD_BLOCK_TYPE:
//            case Constants.CMD_RETAIL_PAYMENT_VERIFICATION:
                pin = params[1];
                break;
        }

        try {
            if (pin != null) {
                verifyPinFlag = verifyPin(pin);
            }

            if (verifyPinFlag) {
                switch (command) {
                    case Constants.CMD_LOGIN:
                    case Constants.CMD_LOGIN_OTP_VERIFICATION:
                        response = httpCall.login(command, params[1], params[2],
                                params[3], params[4], params[5], params[6], params[7], params[8], params[9]);
                        break;

                    case Constants.CMD_DEVICE_UPDATE_OTP_VERIFICATION:
                        int action = Integer.parseInt(params[1]);
                        switch (action) {
                            case 0:
                                response = httpCall.otpVerification(command, params[1],
                                        params[2], params[3], params[4], params[5]);
                                break;
                            case 1:
                            case 2:
                                response = httpCall.otpVerification(command, params[1], params[2], params[3], params[4]);
                                break;
                        }
                        break;

                    case Constants.CMD_CASH_WITHDRAWAL:
                    case Constants.CMD_SIGN_OUT:
                    case Constants.CMD_DEBIT_CARD_BLOCK_INFO:
                        response = httpCall.simpleRequest(command);
                        break;

                    case Constants.CMD_CHECK_BALANCE:
                        response = httpCall.checkBalance(command, params[1],
                                params[2], params[3], params[4], params[5]);
                        break;

                    case Constants.CMD_TRANS_PURPOSE_CODE:
                        response = httpCall.transPurposeCode(command);
                        break;

                    case Constants.CMD_CHANGE_PIN:
                    case Constants.CMD_CHANGE_MPIN:
                        response = httpCall.changePin(command, params[1],
                                params[2], params[3]);
                        break;
                    case Constants.CMD_DEBIT_CARD:
                        ApplicationData.fee = null;
                        response = httpCall
                                .debitCardRequest(command);
                        break;
                    case Constants.CMD_ADVANCE_LOAN_INFO:
                        response = httpCall.advanceLoanInfo(command, params[1], params[2], params[3], params[4]);
                        break;
                    case Constants.CMD_DEBIT_CARD_CONFIRMATION:
                        if (ApplicationData.isLogin) {
                            response = httpCall
                                    .debitCardRequestConfirmation(command,
                                            params[2], params[3]);
                            break;
                        } else {
                            response = httpCall.debitCardRequestConfirmation(command,
                                            params[2], params[3], params[4], params[5]);
                            break;
                        }
                    case Constants.CMD_SET_MPIN:
                        response = httpCall.changePin(command, params[1], params[2]);
                        break;

                    case Constants.CMD_MY_LIMITS:
                        response = httpCall.myLimits(command, params[1]);
                        break;


                    case Constants.CMD_L1_TO_HRA:
                        response = httpCall.openAccountL0ToL1(command, params[1],
                                params[2], params[3], params[4], params[5],
                                params[6], params[7], params[8], params[9],
                                params[10], params[11], params[12], params[13],
                                params[14], params[15], params[16], params[17],
                                params[18], params[19], params[20], params[21], params[22], params[23], params[24], params[25]);
                        break;

                    case Constants.CMD_CUSTOMER_REGISTRATION:
                        response = httpCall.customerRegister(command, params[1], params[2], params[3]);
                        break;

                    case Constants.CMD_HRA_TO_WALLET_INFO:
                        response = httpCall.hraToWalletInfo(command, params[1], params[2]);
                        break;

                    case Constants.CMD_HRA_TO_WALLET:
                        response = httpCall.hraToWallet(command,  params[2], params[3], params[4], params[5],params[6]);
                        break;

                    case Constants.CMD_OPEN_ACCOUNT:

                        if(params[17].equals("0")) {
                            response = httpCall.openAccountNew(command, params[1],
                                    params[2], params[3], params[4], params[5],
                                    params[6]);
                        } else {
                            response = httpCall.openAccount(command, params[1],
                                    params[2], params[3], params[4], params[5],
                                    params[6], params[7], params[8], params[9]
                                    , params[10], params[11], params[12], params[13], params[14], params[15], params[16], params[17], params[18]);
                        }
                        break;


                    case Constants.CMD_CUSTOMER_REGISTER_OTP:
                        response = httpCall.customerRegisterOTP(command, params[1], params[2], params[3]);
                        break;


                    case Constants.CMD_CUSTOMER_TERMS_CONDITIONS:
                        response = httpCall.customerTermsCondition(command, params[1], params[2], params[3],
                                params[4], params[5], params[6], params[7], params[8], params[9], params[10]);
                        break;

                    case Constants.CMD_MINI_STATMENT:
                        response = httpCall.fetchMiniStatement(command, params[1],
                                params[2], params[3], params[4], params[5]);
                        break;

                    //rehan work is it

                    case Constants.VERIFY_ACCOUNT_REQUEST:
                        response = httpCall.VerifyAccountRequst(command, params[1],
                                params[2], params[3]);
                        break;

                    case Constants.ACCOUNT_OPENING_REQUEST:
                        response = httpCall.accountOpeningRequest(command,
                                params[2], params[3], params[4], params[5],
                                params[6], params[7], params[8], params[9], params[10], params[11], params[12], params[13], params[14], params[15]);
                        break;

                    case Constants.CMD_RETAIL_PAYMENT_INFO:
                        response = httpCall.retailPaymentInfo(command, params[1],
                                params[2], params[3]);
                        break;

                    case Constants.CMD_RETAIL_PAYMENT:
                        response = httpCall.retailPayment(command, params[1],
                                params[2], params[3], params[4], params[5],
                                params[6], params[7], params[8]);
                        break;

                    case Constants.CMD_TRANSFER_IN_INFO:
                        response = httpCall.transferInInfo(command, params[1],
                                params[2]);
                        break;

                    case Constants.CMD_TRANSFER_IN:
                        response = httpCall.transferIn(command,
                                params[2], params[3], params[4], params[5], params[6]);
                        break;

                    case Constants.CMD_TRANSFER_OUT_INFO:
                        response = httpCall.transferOutInfo(command, params[1], params[2]);
                        break;

                    case Constants.CMD_TRANSFER_OUT:
                        response = httpCall.transferOut(command,
                                params[2], params[3], params[4], params[5],
                                params[6], params[7], params[8], params[9], params[10]);
                        break;

                    case Constants.CMD_FUNDS_TRANSFER_BLB2BLB_INFO:
                        response = httpCall.ftBlbToBlbInfo(command, params[1],
                                params[2], params[3]);
                        break;

                    case Constants.CMD_FUNDS_TRANSFER_BLB2BLB:
                        response = httpCall.ftBlbToBlb(command, params[1],
                                params[2], params[3], params[4], params[5],
                                params[6], params[7]);
                        break;

                    case Constants.CMD_ADVANCE_LOAN:
                        response = httpCall.advanceLoan(command, params[1],
                                params[2], params[3], params[4], params[5],
                                params[6], params[7], params[8], params[9]);
                        break;

                    case Constants.CMD_INFO_L1_TO_HRA:
                        response = httpCall
                                .openAccountVerifyL1TOHRACustomerRegistration(command,
                                        params[1], params[2]);
                        break;

                    case Constants.CMD_FUNDS_TRANSFER_BLB2CNIC_INFO:
                        response = httpCall.ftBlbToCnicInfo(command, params[1],
                                params[2], params[3], params[4]);
                        break;

                    case Constants.CMD_FUNDS_TRANSFER_BLB2CNIC:
                        response = httpCall.ftBlbToCnic(command, params[1],
                                params[2], params[3], params[4], params[5],
                                params[6], params[7], params[8], params[9], params[10]);
                        break;

                    case Constants.CMD_FUNDS_TRANSFER_2CORE_INFO:
                        int productId = Integer.parseInt(params[1]);
                        switch (productId) {
                            case Constants.ProductIds.BLB2CORE:
                                response = httpCall.ftBlbToCoreInfo(command, params[1],
                                        params[2], params[3], params[4]);
                                break;

                            case Constants.ProductIds.BLB2IBFT:
                                response = httpCall.ftBlbToIbftInfo(command,
                                        params[1], params[2], params[3], params[4],
                                        params[5],params[6], params[7]);
                                break;
                        }
                        break;

                    case Constants.CMD_FUNDS_TRANSFER_BLB2CORE:
                        int Pid = Integer.parseInt(params[2]);
                        switch (Pid) {
                            case Constants.ProductIds.BLB2CORE:
                                response = httpCall.ftBlbToCore(command, params[1],
                                        params[2], params[3], params[4], params[5],
                                        params[6], params[7], params[8], params[9]);
                                break;
                            case Constants.ProductIds.BLB2IBFT:
                                response = httpCall.ftBlbToIbft(command,
                                        params[1], params[2], params[3], params[4],
                                        params[5], params[6], params[7], params[8],
                                        params[9], params[10], params[11], params[12],
                                        params[13], params[14]
                                        ,params[15],params[16],params[17],params[18],params[19],params[20]);
                                break;
                        }
                        break;

                    case Constants.CMD_MINI_LOAD_INFO:
                        response = httpCall.miniLoadInfo(command, params[1],
                                params[2], params[3]);
                        break;

                    case Constants.CMD_SCHEDULE_PAYMENT:
                        response = httpCall.schedulePayment(command, params[2]);
                        break;

                    case Constants.CMD_MINI_LOAD:
                        response = httpCall.miniLoad(command, params[1],
                                params[2], params[3], params[4], params[5],
                                params[6], params[7]);
                        break;


                    case Constants.CMD_COLLECTION_INFO:
                        response = httpCall.collectionPaymentInfo(command, params[1],
                                params[2], params[3]);
                        break;


                    case Constants.CMD_RETAIL_PAYMENT_MPASS:
                        response = httpCall.retailPaymenyMPass(command, params[2],
                                params[3], params[4], params[5]);
                        break;


                    case Constants.CMD_RETAIL_PAYMENT_VERIFICATION:
                        response = httpCall.retailPaymentMerchantVerification(command, params[1],
                                params[2], params[3]);
                        break;


                    case Constants.CMD_COLLECTION_PAYMENT:
                        response = httpCall.collectionPaymentConfirmation(command, params[2],
                                params[3], params[4], params[5], params[6], params[7], params[8]);
                        break;

                    case Constants.CMD_BILL_PAYMENT:
                        response = httpCall.billPayment(command, params[1],
                                params[2], params[3], params[4], params[5],
                                params[6], params[7], params[8]);
                        break;


                    case Constants.CMD_BILL_INQUIRY:
                        response = httpCall.billInquiry(command, params[1],
                                params[2], params[3], params[4], params[5],
                                params[6], params[7], params[8]);
                        break;

                    case Constants.CMD_DEBIT_CARD_ISSUANCE_INFO:
                        response = httpCall.debitCardIssuanceInfo(command,
                                params[1], params[2], params[3], params[4],
                                params[5], params[6], params[7], params[8]);
                        break;

                    case Constants.CMD_DEBIT_CARD_ISSUANCE:
                        response = httpCall.debitCardIssuance(command,
                                params[1], params[2], params[3], params[4],
                                params[5], params[6], params[7], params[8], params[9], params[10], params[11], params[12], params[13], params[14], params[15]);
                        break;

                    case Constants.CMD_DEBIT_CARD_ACTIVATION_INFO:
                        response = httpCall.debitCardActivationInfo(command,
                                params[1], params[2]);
                        break;

                    case Constants.CMD_DEBIT_CARD_ACTIVATION:
                        response = httpCall.debitCardActivation(command,
                                params[2], params[3], params[4]);
                        break;

                    case Constants.CMD_FAQS:
                        response = httpCall.faqs(command, params[1]);
                        break;

                    case Constants.CMD_LOCATOR:
                        response = httpCall.locator(command, params[1],
                                params[2], params[3], params[4], params[5], params[6]);
                        break;

                    case Constants.CMD_ATM_PIN_GENERATE_CHANGE_INFO:
                        response = httpCall.changeAndGeneratePinInfo(command, params[1]);
                        break;

                    case Constants.CMD_ATM_PIN_GENERATE_CHANGE:
                        response = httpCall.changeAtmPin(command, params[2],
                                params[3], params[4], params[5]);
                        break;

                    case Constants.CMD_GENERATE_MPIN:
                        response = httpCall.generateMPIN(command, params[1]);
                        break;


                    case Constants.CMD_FORGOT_PASSWORD_FIRST:
                    case Constants.CMD_REGENERATE_OTP:
                        response = httpCall.forgotPassword(command, params[1]);
                        break;

//                    case Constants.CMD_REGENERATE_OTP:
//                        response = httpCall.forgotPassword(command, params[1]);
//                        break;

                    case Constants.CMD_FORGOT_PASSWORD_SECOND:
                        response = httpCall.forgotPasswordConfirm(command, params[1], params[2], params[3], params[4]);
                        break;
                    case Constants.CMD_GET_BANKS:
                        response = httpCall.getBanks(command);
//                        response = httpCall.blockDebitCard(command, params[1], params[2]);
                        break;
                    case Constants.CMD_RESET_LOGIN_PIN:
                        response = httpCall.resetLoginPin(command, params[1], params[2]);
                        break;
                    case Constants.CMD_FORGOT_MPIN:
                        response = httpCall.forgotMpin(command, params[1], params[2]);
                        break;
                    case Constants.CMD_SET_MPIN_LATER:
                        response = httpCall.setMpinLater(command, params[1], params[2], params[3]);
                        break;
                    case Constants.CMD_BOOKME_INQUIRY:
                        response = httpCall.bookmeInquiry(command, params[1], params[2], params[3], params[4], params[5], params[6], params[7], params[8], params[9], params[10], params[11], params[12], params[13], params[14], params[15], params[16]);
                        break;
                    case Constants.CMD_BOOKME_PAYMENT:
                        response = httpCall.bookmePayment(command, params[2], params[3], params[4], params[5], params[6], params[7], params[8], params[9], params[10], params[11], params[12], params[13], params[14], params[15], params[16], params[17], params[18], params[19], params[20]);
                        break;
                    case Constants.CMD_DEBIT_CARD_ACTIVATION_CHANGE_PIN:
                        response = httpCall.debitCardActivationAndChangePin(command, params[2], params[3], params[4]);
                        break;
                    case Constants.CMD_DEBIT_CARD_BLOCK_TYPE:
                        response = httpCall.debitCardBlockType(command, params[2], params[3]);
                        break;

                    case Constants.CMD_DEBIT_CARD_INQUIRY:
                        response = httpCall.debitCardInquiry(command, params[1], params[2]);
                        break;


                    case Constants.CMD_DEBIT_CARD_BLOCK:
                        response = httpCall.simpleRequest(command);
//                        response = httpCall.blockDebitCard(command, params[1], params[2]);
                        break;
                }
            }
        } catch (Exception ex) {
            AppLogger.i(ex.getMessage());
            response = new HttpResponseModel();
            response.setXmlResponse(getHandShakeExceptionResponse(Constants.Messages.EXCEPTION_SSL_HANDSHAKE));
        }
        return response;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(HttpResponseModel result) {
        super.onPostExecute(result);

        if (result != null) {
            try {
                XmlParser xmlParser = new XmlParser();
                Hashtable<?, ?> table = xmlParser.convertXmlToTable(result.getXmlResponse());
                if (table != null && table.containsKey(Constants.KEY_LIST_ERRORS)) {
                    List<?> list = (List<?>) table.get(Constants.KEY_LIST_ERRORS);
                    MessageModel message = (MessageModel) list.get(0);

                    if (message.getCode().equals(Constants.ErrorCodes.SESSION_EXPIRED) ||
                            message.getCode().equals(Constants.ErrorCodes.CREDENTIALS_BLOCKED)) {

                        ApplicationData.resetPinRetryCount();


                        ((BaseActivity) activity).dialogGeneral = ((BaseActivity) activity).popupDialogs.createAlertDialog(message.getDescr(), AppMessages.ALERT_HEADING,
                                activity, activity.getString(R.string.ok), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ((BaseActivity) activity).dialogGeneral.dismiss();
                                        Intent intent = new Intent(activity.getApplicationContext(), MainMenuActivity.class);
                                        intent.putExtra(Constants.IntentKeys.FLAG_TRANSITION, Constants.IntentKeys.FLAG_LOGIN);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        activity.startActivity(intent);
                                        activity.finish();

                                    }
                                }, false, PopupDialogs.Status.ERROR, false, null);


//                        dialog = PopupDialogs.createAlertDialog(message.getDescr(),
//                                activity.getString(R.string.alertNotification),
//                                activity, new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        dialog.cancel();
//                                        Intent intent = new Intent(activity.getApplicationContext(), MainMenuActivity.class);
//                                        intent.putExtra(Constants.IntentKeys.FLAG_TRANSITION, Constants.IntentKeys.FLAG_LOGIN);
//                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                        activity.startActivity(intent);
//                                        activity.finish();
//                                    }
//                                }, PopupDialogs.Status.ERROR);

                        return;
                    } else if (message.getCode().equals(
                            Constants.ErrorCodes.INTERNAL_SSL)) {

                        ((BaseActivity) activity).dialogGeneral = ((BaseActivity) activity).popupDialogs.createAlertDialog(message.getDescr(), AppMessages.ALERT_HEADING,
                                activity, activity.getString(R.string.download), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ((BaseActivity) activity).dialogGeneral.dismiss();
                                        activity.hideLoading();
                                        activity.openMarketUrl();

                                    }
                                }, false, PopupDialogs.Status.ERROR, false, null);


                        return;
                    } else if (message.getCode().equals(
                            Constants.ErrorCodes.INTERNAL_SSL_HAND_SHAKE)) {

                        ((BaseActivity) activity).dialogGeneral = ((BaseActivity) activity).popupDialogs.createConfirmationDialog(message.getDescr(), AppMessages.ALERT_HEADING,
                                activity, "DOWNLOAD", "OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        activity.hideLoading();
                                        activity.openMarketUrl();
                                        ((BaseActivity) activity).dialogGeneral.dismiss();

                                    }
                                }, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        activity.hideLoading();
                                        ((BaseActivity) activity).dialogGeneral.dismiss();
                                    }
                                }, false, PopupDialogs.Status.ALERT, false);


                        return;
                    }
                }
            } catch (Exception e) {
                AppLogger.e(e);
            }
        }
        activity.processResponse(result);
    }

    private boolean verifyPin(String pin) {
        try {
            response = HttpCalls.getInstance(activity).verifyPin(
                    Constants.CMD_VERIFY_PIN, pin,
                    ApplicationData.pinRetryCount);

            try {
                XmlParser xmlParser = new XmlParser();
                Hashtable<?, ?> table = xmlParser.convertXmlToTable(response
                        .getXmlResponse());
                if (table != null
                        && table.containsKey(Constants.KEY_LIST_ERRORS)) {
                    List<?> list = (List<?>) table
                            .get(Constants.KEY_LIST_ERRORS);
                    MessageModel message = (MessageModel) list.get(0);

                    String code = message.getCode();
                    AppLogger.i("##Error Code: " + code);
                    if (message.getCode() != null && message.getLevel() != null && message.getCode().equals("9001") && message.getLevel().equals("4")) {

                        ApplicationData.pinRetryCount = ApplicationData.pinRetryCount + 1;
                    }


                } else if (table.get("DTID") != null) {
                    AppLogger.i("##DTID: " + table.get("DTID").toString());
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            AppLogger.i("Reponse: " + response.getXmlResponse());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static String getProcessingExceptionResponse() {
        String responseMessage = "<msg id=\"-1\"><errors><error code=\""
                + Constants.ErrorCodes.EXCEPTION_ERROR + "\" level=\"0\">" + AppMessages.EXCEPTION_PROCESSING_ERROR
                + "</error></errors></msg>";
        return responseMessage;
    }


    private static String getHandShakeExceptionResponse(String message) {
        String responseMessage = "<msg id=\"-1\"><errors><error code=\""
                + Constants.ErrorCodes.INTERNAL_SSL_HAND_SHAKE + "\" level=\"2\">" + message
                + "</error></errors></msg>";
        return responseMessage;
    }

}