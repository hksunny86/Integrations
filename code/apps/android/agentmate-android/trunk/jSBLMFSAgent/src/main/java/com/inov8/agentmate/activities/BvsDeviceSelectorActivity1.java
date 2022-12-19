package com.inov8.agentmate.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.inov8.agentmate.util.Constants;
import com.inov8.jsblmfs.R;

public class BvsDeviceSelectorActivity1 extends Activity {
	private Button paysys, suprema, p41m2, supremaSlim2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biometric_selector1);

        paysys = (Button) findViewById(R.id.paysys);
        suprema = (Button) findViewById(R.id.biometric);
        p41m2 = (Button) findViewById(R.id.p41m2);
        supremaSlim2 = (Button) findViewById(R.id.supremaSlim2);

        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }catch (Exception e){
            e.printStackTrace();
        }

        paysys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                intent.putExtra(Constants.IntentKeys.BIOMETRIC_FLOW, Constants.PAYSYS);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        suprema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                intent.putExtra(Constants.IntentKeys.BIOMETRIC_FLOW, Constants.SUPREMA);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        p41m2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                intent.putExtra(Constants.IntentKeys.BIOMETRIC_FLOW, Constants.P41M2);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        supremaSlim2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                intent.putExtra(Constants.IntentKeys.BIOMETRIC_FLOW, Constants.SUPREMA_SLIM_2);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed(){
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}