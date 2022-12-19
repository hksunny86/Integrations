package com.inov8.agentmate.net;

import java.util.Hashtable;
import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

import com.inov8.agentmate.activities.BaseCommunicationActivity;
import com.inov8.agentmate.activities.LoginActivity;
import com.inov8.agentmate.model.HttpResponseModel;
import com.inov8.agentmate.model.MessageModel;
import com.inov8.agentmate.parser.XmlParser;
import com.inov8.agentmate.util.AppLogger;
import com.inov8.agentmate.util.AppMessages;
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;

public class HttpAsyncTask extends AsyncTask<String, Void, HttpResponseModel> {
    private BaseCommunicationActivity activity;
    private HttpResponseModel response = null;
    private HttpCalls httpCall = null;
    private int command = -1;

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
            case Constants.CMD_CASH_IN:
            case Constants.CMD_HRA_CASH_WITHDRAWAL:
            case Constants.CMD_CASH_OUT:
            case Constants.CMD_CASH_OUT_BY_TRX_ID:
            case Constants.CMD_FUNDS_TRANSFER_BLB2BLB:
            case Constants.CMD_FUNDS_TRANSFER_CNIC2BLB:
            case Constants.CMD_FUNDS_TRANSFER_BLB2CNIC:
//            case Constants.CMD_FUNDS_TRANSFER_CNIC2CNIC:
            case Constants.CMD_FUNDS_TRANSFER_BLB2CORE:
            case Constants.CMD_FUNDS_TRANSFER_CNIC2CORE:
            case Constants.CMD_BILL_PAYMENT:
            case Constants.CMD_TRANSFER_OUT:
            case Constants.CMD_TRANSFER_IN:
            case Constants.CMD_AGENT_TO_AGENT_TRANSFER:
            case Constants.CMD_RETAIL_PAYMENT:
            case Constants.CMD_OPEN_ACCOUNT:
            case Constants.CMD_MINI_STATMENT:
            case Constants.CMD_CHECK_BALANCE:
            case Constants.CMD_CHANGE_MPIN:
            case Constants.CMD_RECEIVE_MONEY_SENDER_REDEEM_PAYMENT:
            case Constants.CMD_RECEIVE_MONEY_PENDING_TRX_PAYMENT:
            case Constants.CMD_RECEIVE_MONEY_RECEIVE_CASH:
            case Constants.CMD_COLLECTION_PAYMENT_TRX:
            case Constants.CMD_OPEN_ACCOUNT_BVS:
            case Constants.CMD_L1_TO_HRA:
            case Constants.CMD_3RD_PARTY_CASH_OUT:
            case Constants.CMD_IBFT_AGENT_CONFORMATION:
            case Constants.CMD_DEBIT_CARD_CONFIRMATION:
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
                        response = httpCall.login(command, params[1], params[2],
                                params[3], params[4], params[5], params[6], params[7], params[8], params[9]);
                        break;

                    case Constants.CMD_CHECK_BALANCE:
                        response = httpCall.checkBalance(command, params[1],
                                params[2], params[3], params[4]);
                        break;

                    case Constants.CMD_CHANGE_PIN:
                    case Constants.CMD_CHANGE_MPIN:
                        response = httpCall.changePin(command, params[1],
                                params[2], params[3]);
                        break;
                    case Constants.CMD_SET_MPIN:
                        response = httpCall.setPin(command, params[1],
                                params[2]);
                        break;
                    case Constants.CMD_MINI_STATMENT:
                        response = httpCall.fetchMiniStatement(command, params[1],
                                params[2], params[3], params[4]);
                        break;

                    case Constants.CMD_3RD_PARTY_CASH_OUT_INFO:
                        response = httpCall.thirdPartyCashOutInfo(command, params[1], params[2], params[3]);
                        break;

                    case Constants.CMD_CHECK_BVS:
                        response = httpCall.checkBVS(command, params[1], params[2]);
                        break;

                    case Constants.CMD_3RD_PARTY_AGENT_BVS:
                        response = httpCall.agentBVS(command, params[1],
                                params[2], params[3], params[4], params[5], params[6], params[7], params[8], params[9]);
                        break;

                    case Constants.CMD_3RD_PARTY_CASH_OUT:
                        response = httpCall.thirdPartyCashOut(command, params[1],
                                params[2], params[3], params[4], params[5],
                                params[6], params[7], params[8], params[9],
                                params[10], params[11], params[12], params[13],
                                params[14],params[15],params[16], params[17], params[18], params[19], params[20],params[21],params[22],params[23],params[24]);
                        break;

                    case Constants.CMD_IBFT_AGENT:
                        response = httpCall.agentIBFTInfo(command,
                                params[1], params[2], params[3], params[4], params[5], params[6],params[7]);
                        break;

                    case Constants.CMD_IBFT_AGENT_CONFORMATION:
                        response = httpCall.agentIBFT(command,
                                params[2], params[3], params[4], params[5], params[6]
                                ,params[7],params[8],params[9],params[10],params[11]);
                        break;

                    case Constants.CMD_BANKS:
                        response = httpCall.bankList(command);
                        break;

                    case Constants.CMD_OPEN_ACCOUNT_VERIFY_CUSTOMER_REGISTRATION:
                        response = httpCall
                                .openAccountVerifyCustomerRegistration(command,
                                        params[1], params[2], params[3], params[4], params[5],params[6]);
                        break;
                    case Constants.CMD_DEBIT_CARD:
                        response = httpCall
                                .debitCardRequest(command,params[1], params[2]);
                        break;
                    case Constants.CMD_DEBIT_CARD_CONFIRMATION:
                        response = httpCall
                                .debitCardRequestConfirmation(command,
                                         params[2], params[3], params[4], params[5]);
                        break;
                    case Constants.CMD_INFO_L1_TO_HRA:
                        response = httpCall.openAccountVerifyL1TOHRACustomerRegistration(command,
                                        params[1], params[2]);
                        break;

                    case Constants.CMD_OPEN_ACCOUNT_BVS: // 121
                        response = httpCall.openAccountBvs(command,
                                params[1], params[2], params[3], params[4], params[5],
                                params[6], params[7], params[8], params[9],
                                params[10], params[11], params[12], params[13],
                                params[14], params[15], params[16], params[17],
                                params[18], params[19], params[20], params[21], params[22],
                                params[23], params[24], params[25], params[26],
                                params[27], params[28],
                                params[29], params[30], params[31], params[32],
                                params[33], params[34], params[35], params[36], params[37],
                                params[38], params[39], params[40], params[41],
                                params[42],params[43]
                        );
                        break;

                    case Constants.CMD_OPEN_ACCOUNT:  // 121
                        response = httpCall.openAccount(command, params[1],
                                params[2], params[3], params[4], params[5],
                                params[6], params[7], params[8], params[9],
                                params[10], params[11], params[12], params[13],
                                params[14], params[15], params[16], params[17],
                                params[18], params[19], params[20], params[21]);
                        break;

                    case Constants.CMD_L1_TO_HRA:  // 121
                        response = httpCall.openAccountL0ToL1(command, params[1],
                                params[2], params[3], params[4], params[5],
                                params[6], params[7], params[8], params[9],
                                params[10], params[11], params[12], params[13],
                                params[14], params[15], params[16], params[17],
                                params[18], params[19], params[20],params[21],params[22],params[23],params[24],params[25],params[26]);
                        break;

                    case Constants.CMD_RETAIL_PAYMENT_INFO:
                        response = httpCall.retailPaymentInfo(command, params[1],
                                params[2], params[3]);
                        break;

                    case Constants.CMD_COLLECTION_PAYMENT_INFO:
                        response = httpCall.collectionPaymentInfo(command, params[1],
                                params[2], params[3]);
                        break;

                    case Constants.CMD_COLLECTION_PAYMENT_TRX:
                        response = httpCall.collectionPayment(command,
                                params[2], params[3], params[4],
                                params[5], params[6], params[7],
                                params[8], params[9], params[10]);
                        break;

                    case Constants.CMD_RETAIL_PAYMENT:
                        response = httpCall.retailPayment(command, params[1],
                                params[2], params[3], params[4], params[5],
                                params[6], params[7], params[8]);
                        break;

                    case Constants.CMD_TRANSFER_IN_INFO:
                        response = httpCall.transferInInfo(command, params[1],
                                params[2], params[3], params[4]);
                        break;

                    case Constants.CMD_TRANSFER_IN:
                        response = httpCall.transferIn(command, params[1],
                                params[2], params[3], params[4], params[5],
                                params[6], params[7], params[8], params[9],
                                params[10]);
                        break;

                    case Constants.CMD_TRANSFER_OUT_INFO:
                        response = httpCall.transferOutInfo(command, params[1],
                                params[2], params[3], params[4], params[5]);
                        break;

                    case Constants.CMD_TRANSFER_OUT:
                        response = httpCall.transferOut(command, params[1],
                                params[2], params[3], params[4], params[5],
                                params[6], params[7], params[8], params[9],
                                params[10], params[11]);
                        break;

                    case Constants.CMD_FUNDS_TRANSFER_BLB2BLB_INFO:
                        response = httpCall.ftBlbToBlbInfo(command, params[1],
                                params[2], params[3], params[4], params[5]);
                        break;

                    case Constants.CMD_TRANS_PURPOSE_CODE:
                        response = httpCall.transPurposeCode(command);
                        break;

                    case Constants.CMD_FUNDS_TRANSFER_BLB2BLB:
                        response = httpCall.ftBlbToBlb(command, params[1],
                                params[2], params[3], params[4], params[5],
                                params[6], params[7], params[8], params[9]);
                        break;

                    case Constants.CMD_FUNDS_TRANSFER_BLB2CNIC_INFO:
                        response = httpCall.ftBlbToCnicInfo(command, params[1],
                                params[2], params[3], params[4], params[5],
                                params[6]);
                        break;

                    case Constants.CMD_FUNDS_TRANSFER_BLB2CNIC:
                        response = httpCall.ftBlbToCnic(command, params[1],
                                params[2], params[3], params[4], params[5],
                                params[6], params[7], params[8], params[9],
                                params[10], params[11]);
                        break;

                    case Constants.CMD_FUNDS_TRANSFER_CNIC2BLB_INFO:
                        response = httpCall.ftCnicToBlbInfo(command, params[1],
                                params[2], params[3], params[4], params[5],
                                params[6]);
                        break;

                    case Constants.CMD_FUNDS_TRANSFER_CNIC2BLB:
                        response = httpCall.ftCnicToBlb(command, params[1],
                                params[2], params[3], params[4], params[5],
                                params[6], params[7], params[8], params[9],
                                params[10], params[11], params[12]);
                        break;

                    case Constants.CMD_FUNDS_TRANSFER_CNIC2CNIC_INFO:
                        response = httpCall.ftCnicToCnicInfo(command, params[1],
                                params[2], params[3], params[4], params[5],
                                params[6], params[7]);
                        break;

                    case Constants.CMD_FUNDS_TRANSFER_CNIC2CNIC:
                        response = httpCall.ftCnicToCnic(command, params[1],
                                params[2], params[3], params[4], params[5],
                                params[6], params[7], params[8], params[9],
                                params[10], params[11], params[12],
                                params[13], params[14], params[15]);
                        break;

                    case Constants.CMD_FUNDS_TRANSFER_2CORE_INFO:
                        int productId = Integer.parseInt(params[1]);
                        switch (productId) {
                            case Constants.ProductIds.BLB2CORE:
                                response = httpCall.ftBlbToCoreInfo(command, params[1],
                                        params[2], params[3], params[4], params[5]);
                                break;
                            case Constants.ProductIds.CNIC2CORE:
                                response = httpCall.ftCnicToCoreInfo(command,
                                        params[1], params[2], params[3], params[4],
                                        params[5], params[6]);
                                break;
                        }
                        break;

                    case Constants.CMD_FUNDS_TRANSFER_BLB2CORE:
                        response = httpCall.ftBlbToCore(command, params[1],
                                params[2], params[3], params[4], params[5],
                                params[6], params[7], params[8], params[9],
                                params[10], params[11]);
                        break;

                    case Constants.CMD_FUNDS_TRANSFER_CNIC2CORE:
                        response = httpCall.ftCnicToCore(command, params[1],
                                params[2], params[3], params[4], params[5],
                                params[6], params[7], params[8], params[9],
                                params[10], params[11], params[12],
                                params[13]);
                        break;

                    case Constants.CMD_BILL_INQUIRY:
                        response = httpCall.billInquiry(command, params[1],
                                params[2], params[3], params[4], params[5],
                                params[6]);
                        break;

                    case Constants.CMD_BILL_PAYMENT:
                        response = httpCall.billPayment(command, params[1],
                                params[2], params[3], params[4], params[5],
                                params[6], params[7], params[8]);
                        break;

                    case Constants.CMD_CASH_IN_INFO:
                        response = httpCall.cashInInfo(command, params[1],
                                params[2], params[3], params[4]);
                        break;

                    case Constants.CMD_CASH_IN:
                        response = httpCall.cashIn(command, params[1], params[2],
                                params[3], params[4], params[5], params[6],
                                params[7], params[8], params[9]);
                        break;

                    case Constants.CMD_CASH_OUT_INFO:
                        response = httpCall.cashOutInfo(command, params[1],
                                params[2], params[3], params[4],params[5],params[6], params[7]);
                        break;

                    case Constants.CMD_CASH_OUT_BY_TRX_ID_INFO:
                        response = httpCall.cash_withdrawal_by_trxid_info(command, params[1],
                                params[2], params[3]);
                        break;

                    case Constants.CMD_CASH_OUT_BY_TRX_ID:
                        response = httpCall.cash_withdrawal_by_trxid(command, params[2],
                                params[3], params[4], params[5], params[6],
                                params[7], params[8], params[9]);
                        break;

                    case Constants.CMD_CASH_OUT:
                    case Constants.CMD_HRA_CASH_WITHDRAWAL:
                        response = httpCall.cashOut(command, params[1], params[2],
                                params[3], params[4], params[5], params[6],
                                params[7], params[8], params[9],params[10], params[11], params[12]);
                        break;

                    case Constants.CMD_AGENT_TO_AGENT_TRANSFER_INFO:
                        response = httpCall.agentToAgentTransferInfo(command,
                                params[1], params[2], params[3], params[4]);
                        break;

                    case Constants.CMD_AGENT_TO_AGENT_TRANSFER:
                        response = httpCall.agentToAgentTransfer(command,
                                params[1], params[2], params[3], params[4],
                                params[5], params[6], params[7], params[8]);
                        break;

                    case Constants.CMD_RECEIVE_MONEY_SENDER_REDEEM_INFO:
                        response = httpCall.receiveMoneySenderRedeemInfo(command,
                                params[1], params[2], params[3], params[4],
                                params[5]);
                        break;

                    case Constants.CMD_RECEIVE_MONEY_SENDER_REDEEM_PAYMENT:
                        response = httpCall.receiveMoneySenderRedeem(command,
                                params[1], params[2], params[3], params[4],
                                params[5], params[6], params[7], params[8],
                                params[9], params[10], params[11]);
                        break;

                    case Constants.CMD_RECEIVE_MONEY_RECEIVE_CASH_INFO:
                        response = httpCall.receiveMoneyReceiveCashInfo(command,
                                params[1], params[2], params[3], params[4],
                                params[5]);
                        break;

                    case Constants.CMD_RECEIVE_MONEY_PENDING_TRX_PAYMENT:
                        response = httpCall.receiveMoneyPendingTrxPayment(command,
                                params[1], params[2], params[3], params[4],
                                params[5], params[6], params[7], params[8],
                                params[9], params[10], params[11], params[12],
                                params[13]);
                        break;

                    case Constants.CMD_RECEIVE_MONEY_RECEIVE_CASH:
                        response = httpCall.receiveMoneyReceiveCash(command,
                                params[1], params[2], params[3], params[4],
                                params[5], params[6], params[7], params[8],
                                params[9], params[10], params[11], params[12],
                                params[13], params[14]);
                        break;

                    case Constants.CMD_OTP_VERIFICATION:
                        response = httpCall.otpVerification(command, params[1],
                                params[2], params[3], params[4]);
                        break;

                    case Constants.CMD_OPEN_ACCOUNT_NADRA_VERIFICATION:
                        response = httpCall.verifyMsisdnCnicImage(command,
                                params[1], params[2], params[3], params[4], params[5], params[6]);
                        break;

                    case Constants.CMD_OPEN_ACCOUNT_OTP_VERIFICATION:
                        response = httpCall.openAccountOtpVerification(command, params[1],
                                params[2], params[3], params[4]);
                        break;

                    case Constants.CMD_VERIFY_NADRA_FINGERPRINT:
                        response = httpCall.verifyNadraFingerPrint(command, params[1]);
                        break;

                    case Constants.CMD_SIGN_OUT:
                        response = httpCall.signOut(command);
                        break;

                    case Constants.CMD_VERIFY_PIN:
                        response = httpCall.verifyPin(command, params[1],
                                ApplicationData.pinRetryCount);
                        break;
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            response = new HttpResponseModel();
            response.setXmlResponse(getProcessingExceptionResponse());
          //response.setXmlResponse(getHandShakeExceptionResponse(AppMessages.EXCEPTION_SSL_HANDSHAKE));
            AppLogger.i(ex.getMessage());
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
                Hashtable<?, ?> table = xmlParser.convertXmlToTable(result
                        .getXmlResponse());
                if (table != null && table.containsKey(Constants.KEY_LIST_ERRORS)) {
                    List<?> list = (List<?>) table.get(Constants.KEY_LIST_ERRORS);
                    MessageModel message = (MessageModel) list.get(0);

                    if (message.getCode().equals(
                            Constants.ErrorCodes.SESSION_EXPIRED)
                            || message.getCode().equals(
                            Constants.ErrorCodes.CREDENTIALS_BLOCKED)) {

                        ApplicationData.resetPinRetryCount();

                        activity.createAlertDialog(message.getDescr(), "",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        Intent intent = new Intent(activity
                                                .getApplicationContext(),
                                                LoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                                Intent.FLAG_ACTIVITY_NEW_TASK);
                                        activity.startActivity(intent);
                                        activity.finish();
                                    }
                                });
                        return;
                    } else if (message.getCode().equals(
                            Constants.ErrorCodes.INTERNAL_SSL)) {
                        activity.showConfirmationDialog("Update AGENTmate!",
                                message.getDescr(),
                                null, "", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
                                        activity.hideLoading();
                                        activity.openMarketUrl();
                                    }
                                }, "Download"
                        );
                        return;
                    } else if (message.getCode().equals(
                            Constants.ErrorCodes.INTERNAL_CONNECTION)) {
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
                Hashtable<?, ?> table = xmlParser.convertXmlToTable(response.getXmlResponse());
                if (table != null && table.containsKey(Constants.KEY_LIST_ERRORS)) {
                    List<?> list = (List<?>) table
                            .get(Constants.KEY_LIST_ERRORS);
                    MessageModel message = (MessageModel) list.get(0);

                    String code = message.getCode();
                    AppLogger.i("##Error Code: " + code);
                    ApplicationData.pinRetryCount = ApplicationData.pinRetryCount + 1;
                } else if (table.get("DTID") != null) {
                    AppLogger.i("##DTID: " + table.get("DTID").toString());
                    return true;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            AppLogger.i("Reponse: " + response.getXmlResponse());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static String getProcessingExceptionResponse() {
        String responseMessage = "<msg id=\"-1\"><errors><error code=\""
                + Constants.ErrorCodes.EXCEPTION_ERROR + "\" level=\"0\">"
                + AppMessages.EXCEPTION_INVALID_RESPONSE
                + "</error></errors></msg>";
        return responseMessage;
    }

    private static String getHandShakeExceptionResponse(String message) {
        String responseMessage = "<msg id=\"-1\"><errors><error code=\""
                + Constants.ErrorCodes.INTERNAL_SSL + "\" level=\"2\">" + message
                + "</error></errors></msg>";
        return responseMessage;
    }
}