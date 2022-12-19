package com.inov8.jsblconsumer.activities.billPayment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.BaseActivity;
import com.inov8.jsblconsumer.model.CategoryModel;
import com.inov8.jsblconsumer.model.ProductModel;
import com.inov8.jsblconsumer.util.Constants;

import java.util.ArrayList;

/**
 * Created by ALI REHAN on 12/12/2017.
 */

public class BillPaymentActivity extends BaseActivity {
    ArrayList<ProductModel> productModelArrayList;
    int menuItemPosition;
    private TextView tvBillCompany;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_bill_payments);
        SetUI();
        fetchintents();

//        tvBillCompany.setText(productModelArrayList.get(0).getName());
//        tvBillCompany.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(BillPaymentActivity.this, MainActivity.class);
//                intent.putExtra(Constants.IntentKeys.PRODUCT_MODEL, productModelArrayList);
//                startActivity(intent);
//            }
//        });


    }

    private void SetUI() {
        tvBillCompany = (TextView) findViewById(R.id.tvBillCompany);

    }

    private void fetchintents() {

        menuItemPosition = mBundle.getInt(Constants.IntentKeys.MENU_ITEM_POS);

        productModelArrayList = (ArrayList<ProductModel>) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);


    }
}
