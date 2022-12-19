package com.inov8.jsblconsumer.activities.mangers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.inov8.jsblconsumer.util.AppLogger;

import org.apache.commons.lang3.StringUtils;


public class SmsReceiver extends BroadcastReceiver {

    private SmsUpdater mSmsUpdater;
    private String senderAddress;

    public SmsReceiver(SmsUpdater u) {
        super();
        mSmsUpdater = u;
    }

    public void onReceive(Context context, Intent intent) {
        AppLogger.i("OnReceive called.");

        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (Object aPdusObj : pdusObj) {
                    SmsMessage currentMessage = SmsMessage
                            .createFromPdu((byte[]) aPdusObj);
                    senderAddress = currentMessage
                            .getDisplayOriginatingAddress();
                    if (senderAddress.equals("8012") || senderAddress.equals("FonePay") ||
                            senderAddress.equals("Inov8") || senderAddress.equals("8012")) {
                        String message = currentMessage.getDisplayMessageBody();
                        // verification code from sms
                        String verificationCode = getVerificationCode(message);
                        mSmsUpdater.gotSms(verificationCode);
                    }
                }
            }
        } catch (Exception e) {
            AppLogger.e("Exception: " + e.getMessage());
        }
    }

    private String getVerificationCode(String message) {
        String code = null;


        if (message.toLowerCase().contains("one time pin is ")) {
            int index = message.toLowerCase().indexOf("one time pin is ");

            if (index != -1) {

                int start = index + 16;
                int length = 10;
                code = message.substring(start, start + length);
                return code;
            }
        } else if (message.contains("opening is")) {

            int index = message.indexOf("opening is ");

            if (index != -1) {
                int start = index + 8;

                int length = 4;
                if (senderAddress.equals("8012")) {
                    start = start + 3;
                    length = 5;
                }

                code = message.substring(start, start + length);
                return code;
            }


        } else if (message.contains("Authorization is")) {
            int index = message.indexOf("Authorization is");

            if (index != -1) {
                int start = index + 8;

                int length = 4;
                if (senderAddress.equals("8012")) {
                    start = start + 9;
                    length = 4;
                }

                code = message.substring(start, start + length);
                return code;
            }
        }
        else if (message.contains("Reset Login Pin is")) {
            int index = message.indexOf("Reset Login Pin is");



            if (index != -1) {
                int start = index + 8;

                int length = 4;
                if (senderAddress.equals("8012")) {
                    start = start + 11;
                    length = 5;
                }

                code = message.substring(start, start + length);
                return code;
            }
        }
        else {
            code = message;
        }
        Log.d("OTP", code);
        return code;
    }
}
