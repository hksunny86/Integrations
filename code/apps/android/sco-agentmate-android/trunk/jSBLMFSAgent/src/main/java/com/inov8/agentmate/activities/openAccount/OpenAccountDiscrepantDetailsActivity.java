package com.inov8.agentmate.activities.openAccount;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.inov8.agentmate.activities.BaseActivity;
import com.inov8.agentmate.util.Constants;
import com.inov8.jsbl.sco.R;

public class OpenAccountDiscrepantDetailsActivity extends BaseActivity {
	private View btnNext, btnCancel;
	private TextView lblHeading, lblComments;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_open_account_discrepant);

		lblHeading = (TextView) findViewById(R.id.lblHeading);
		lblHeading.setText("Discrepant Details");

		lblComments = (TextView) findViewById(R.id.lblComments);
		lblComments.setText(mBundle.getString(Constants.ATTR_CREG_COMMENT));

		btnNext = (Button) findViewById(R.id.btnNext);
		btnNext.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				Intent intent = new Intent(
						OpenAccountDiscrepantDetailsActivity.this,
						OpenAccountSecondInputDiscrepantActivity.class);
				intent.putExtras(mBundle);
				startActivity(intent);
			}
		});

		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				goToMainMenu();
			}
		});

		headerImplementation();
	}
}