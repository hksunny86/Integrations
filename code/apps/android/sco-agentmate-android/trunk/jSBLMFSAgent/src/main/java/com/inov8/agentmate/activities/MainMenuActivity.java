package com.inov8.agentmate.activities;

import java.util.Hashtable;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.inov8.agentmate.activities.openAccount.OpenAccountBvsActivity;
import com.inov8.agentmate.adapters.MainMenuImageAdapter;
import com.inov8.agentmate.model.CategoryModel;
import com.inov8.agentmate.model.HttpResponseModel;
import com.inov8.agentmate.model.MessageModel;
import com.inov8.agentmate.net.HttpAsyncTask;
import com.inov8.agentmate.parser.XmlParser;
import com.inov8.agentmate.util.AppLogger;
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;
import com.inov8.jsbl.sco.R;

public class MainMenuActivity extends BaseCommunicationActivity {
	private MainMenuImageAdapter imageAdapter;
	private GridView gridview;

	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		ApplicationData.isLoginFlow =true;
		if (ApplicationData.isDestroyed) {
			this.finish();
		} else {
			setContentView(R.layout.activity_main_menu);

			try {
				gridview = (GridView) findViewById(R.id.gridview);

				imageAdapter = new MainMenuImageAdapter(this);
				gridview.setAdapter(imageAdapter);

				// refresh balance
				Button btnRefreshBalance = (Button) findViewById(R.id.buttonrefersh);
				btnRefreshBalance.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						askMpin(null, null, false);
					}
				});

				btnHome = (ImageView) findViewById(R.id.imageViewHome);
				btnHome.setVisibility(View.INVISIBLE);

				switch (ApplicationData.agentAccountType) {
					case Constants.AgentAccountType.NORMAL:
						loadBalance();
						break;
					case Constants.AgentAccountType.HANDLER:
						View layoutBalance = findViewById(R.id.layout_balance);
						layoutBalance.setVisibility(View.INVISIBLE);
						btnRefreshBalance.setVisibility(View.INVISIBLE);
						break;
				}

				loadName();
				headerImplementation();
			} catch (Exception ex) {
				ex.getMessage();
			}
		}

		if (getIntent().getExtras() != null
				&& getIntent().getExtras().getString(
				Constants.IntentKeys.FLAG_TRANSITION) != null) {
			AppLogger.i("Flag transition");
			AppLogger.i(getIntent().getExtras().toString());
			String intentKey = getIntent().getExtras().getString(
					Constants.IntentKeys.FLAG_TRANSITION);

			if (intentKey.equals(Constants.IntentKeys.FLAG_LOGIN)) {
				Intent intent = new Intent(MainMenuActivity.this,
						LoginActivity.class);
				startActivity(intent);
				finish();
			} else if (intentKey.equals(Constants.IntentKeys.FLAG_OPEN_ACCOUNT)) {
				for (CategoryModel cat : ApplicationData.listCategories) {

					if (cat.getId().equals("21")) {// cat id of account opening
						Intent intent = new Intent(MainMenuActivity.this,
								OpenAccountBvsActivity.class);
						mBundle.putByte(Constants.IntentKeys.FLOW_ID,
								Constants.FLOW_ID_ACCOUNT_OPENING);
						intent.putExtra(Constants.IntentKeys.PRODUCT_MODEL, cat
								.getProductList().get(0));
						intent.putExtras(mBundle);
						startActivity(intent);
					}
				}
			}
		}

		exitFromMainMenu();
	}

	private void exitFromMainMenu() {
		boolean sessionOut = mBundle
				.getBoolean(Constants.IntentKeys.SESSION_OUT);
		if (sessionOut) {
			startActivity(new Intent(MainMenuActivity.this, LoginActivity.class));
			finish();
		}

		mBundle.putBoolean(Constants.IntentKeys.SESSION_OUT, false);
	}

	@SuppressLint("NewApi")
	@Override
	protected void onResume() {
		super.onResume();

		imageAdapter.counter = 0; // avoid multiple tabs on main menu
		imageAdapter.flag = false;
		ApplicationData.isLoginFlow = true;
		ApplicationData.resetPinRetryCount();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public void mpinProcess(final Bundle bundle, final Class<?> nextClass) {
		processRequest();
	}

	@Override
	public void processRequest() {
		if (!haveInternet()) {
			Toast.makeText(getApplication(),
					Constants.Messages.INTERNET_CONNECTION_PROBLEM,
					Toast.LENGTH_SHORT).show();
			return;
		}

		showLoading("Please Wait", "Processing...");
		new HttpAsyncTask(MainMenuActivity.this).execute(
				Constants.CMD_CHECK_BALANCE + "", getEncryptedMpin(), "1",
				ApplicationData.userId, ApplicationData.accountId);
	}

	@Override
	public void processResponse(HttpResponseModel response) {
		try {
			XmlParser xmlParser = new XmlParser();
			Hashtable<?, ?> table = xmlParser.convertXmlToTable(response
					.getXmlResponse());
			if (table != null && table.containsKey(Constants.KEY_LIST_ERRORS)) {
				List<?> list = (List<?>) table.get(Constants.KEY_LIST_ERRORS);
				MessageModel message = (MessageModel) list.get(0);
				createAlertDialog(message.getDescr(), Constants.KEY_LIST_ALERT,
						false, message);
			} else {
				// app balance
				ApplicationData.formattedBalance = table.get(
						Constants.ATTR_BALF).toString();
				loadBalance();

			}
			hideLoading();
		} catch (Exception e) {
			hideLoading();
			e.printStackTrace();
		}
		hideLoading();
	}

	@Override
	public void processNext() {
	}

	public void headerImplementation() {
		btnHome = (ImageView) findViewById(R.id.imageViewHome);
		btnExit = (Button) findViewById(R.id.buttonsignout);

		btnHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hideKeyboard(v);
				// goToMainMenu();
			}
		});

		btnExit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showConfirmExitDialog(null);
			}
		});
	}
}