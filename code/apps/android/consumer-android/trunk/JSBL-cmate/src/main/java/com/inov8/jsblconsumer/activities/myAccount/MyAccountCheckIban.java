package com.inov8.jsblconsumer.activities.myAccount;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.BaseActivity;
import com.inov8.jsblconsumer.activities.BaseCommunicationActivity;
import com.inov8.jsblconsumer.model.ProductModel;
import com.inov8.jsblconsumer.util.ApplicationData;
import com.inov8.jsblconsumer.util.Constants;

public class MyAccountCheckIban extends BaseActivity {
    private TextView textViewIbanNumber;
    private Button btnOk;
    ProductModel mProduct;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account_check_iban);
        try {
        fetchIntents();
        headerImplementation();
        bottomBarImplementation(MyAccountCheckIban.this, "");
        checkSoftKeyboardD();
        titleImplementation(null, "View My IBAN", "", this);
        textViewIbanNumber = findViewById(R.id.tvMyAccountIbanNumber);
        textViewIbanNumber.setText(ApplicationData.senderIban);
        btnOk = (Button) findViewById(R.id.btnokIbanNumber);



        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                goToMainMenu();
            }
        });
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    private void fetchIntents() {
        mProduct = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);
    }
}