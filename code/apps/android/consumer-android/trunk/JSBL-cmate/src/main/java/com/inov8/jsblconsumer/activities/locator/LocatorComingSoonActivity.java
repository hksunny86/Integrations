package com.inov8.jsblconsumer.activities.locator;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.inov8.jsblconsumer.activities.BaseActivity;
import com.inov8.jsblconsumer.R;

public class LocatorComingSoonActivity extends BaseActivity {

    private TextView tvHeading, tvSubheading;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locator_coming_soon);
        headerImplementation();
        bottomBarImplementation(this,"");
        setUI();
    }
    @Override
    public void onBackPressed() {

        finish();
        super.onBackPressed();
    }
    private void setUI() {
        tvHeading = (TextView) findViewById(R.id.lblHeading);
        tvSubheading = (TextView) findViewById(R.id.lblSubHeading);
        btnHome.setVisibility(View.GONE);
        tvHeading.setText("Locator");
        tvSubheading.setVisibility(View.GONE);

    }
}
