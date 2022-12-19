package com.inov8.jsblconsumer.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.inov8.jsblconsumer.model.HttpResponseModel;

import static org.bouncycastle.crypto.tls.ContentType.alert;

public abstract class BaseCommunicationActivity extends BaseActivity {
    public AlertDialog.Builder alert;

    public abstract void processRequest();

    public abstract void processResponse(HttpResponseModel response);

    public abstract void processNext();


    public void createAlertDialog(final String reason, final String title,
                                  DialogInterface.OnClickListener lisnter) {
        alert = new AlertDialog.Builder(BaseCommunicationActivity.this);
        alert.setTitle(title);
        alert.setCancelable(false);
        alert.setMessage(reason);
        alert.setPositiveButton("OK", lisnter);
        alert.show();
    }

}