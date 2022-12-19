package com.inov8.agentmate.activities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.inov8.agentmate.util.AesEncryptor;
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.MpinInterface;
import com.inov8.agentmate.util.Utility;
import com.inov8.jsbl.sco.BuildConstants;
import com.inov8.jsbl.sco.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;

import static com.inov8.agentmate.util.Utility.testValidity;

public class BaseActivity extends Activity {
	public Bundle mBundle;
	public ProgressDialog loadingDialog;
	public TextView textViewAvailableBalance, txtviewName;
	protected ImageView btnHome;
	protected Button btnExit;
	public int mListItemHieght = 22;
	public int mListViewOptionItemHeight = 44;

	private int keyboardHeight = 0;
	private String encryptedPin;
	private String encryptedOtp;
	private long mDeBounce = 0;
	private float mActionDownX, mActionDownY = 0;
	private boolean isScrolling = false;
	private MpinInterface mpinInterface;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//  getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

		mBundle = getIntent().getExtras();
		if (mBundle == null) {
			mBundle = new Bundle();
		}
		screenDesity();
	}

	public void screenDesity() {
		int density = getResources().getDisplayMetrics().densityDpi;

		switch (density) {
			case DisplayMetrics.DENSITY_LOW:
				mListItemHieght = 22;
				mListViewOptionItemHeight = 47;
				break;
			case DisplayMetrics.DENSITY_MEDIUM:
				mListItemHieght = 22;
				break;
			case DisplayMetrics.DENSITY_HIGH:
				mListItemHieght = 22;
				break;
			case DisplayMetrics.DENSITY_XHIGH:
				mListItemHieght = 40;
				break;
			case DisplayMetrics.DENSITY_XXHIGH:
				mListItemHieght = 47;
				break;
		}
	}

	public void goToMainMenu() {
		Intent intent = new Intent(getApplicationContext(),
				MainMenuActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	public void goToMainMenu(Bundle bundle) {
		Intent intent = new Intent(getApplicationContext(),
				MainMenuActivity.class);
		intent.putExtras(bundle);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}

	public void loginPage() {
		Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		finish();
	}

	public void showConfirmExitDialog(View view) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		// set dialog options
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
								Intent intent = new Intent(BaseActivity.this,
										SignOutActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent);
								finish();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
					// set dialog message
				}).setMessage("Are you sure you want to exit?");

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	public void hideKeyboard(View view) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	public boolean haveInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
		}
		if (BuildConstants.isDummyFlow) {
			return true;
		}
		return false;
	}

	public void showLoading(String title, String message) {
		if (loadingDialog == null) {
			loadingDialog = new ProgressDialog(this);
			loadingDialog.setTitle(title);
			loadingDialog.setMessage(message);
			loadingDialog.setCancelable(false);
			loadingDialog.setIndeterminate(false);
		}
		if (!loadingDialog.isShowing())
			loadingDialog.show();
	}

	public void hideLoading() {
		if (loadingDialog != null) {
			loadingDialog.dismiss();
			loadingDialog.cancel();
		}
	}

	public void loadBalance() {
		textViewAvailableBalance = (TextView) findViewById(R.id.textViewAvailableBalance);
		String balance = ApplicationData.formattedBalance;
		textViewAvailableBalance.setText(Constants.CURRENCY + "." + balance);
	}

	public void loadName() {
		txtviewName = (TextView) findViewById(R.id.lblName);
		txtviewName.setText(ApplicationData.firstName + " "
				+ ApplicationData.lastName);
	}

	public void askMpin(final Bundle bundle, final Class<?> nextClass,
						final boolean closeAcitivty, final MpinInterface mpinInterface) {
		this.mpinInterface = mpinInterface;
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int height = displaymetrics.heightPixels;
		int setterHeight = 0;

		final Dialog dialog = new Dialog(BaseActivity.this,
				android.R.style.Theme_Translucent_NoTitleBar);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_mpin);
		dialog.setCancelable(!closeAcitivty);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(R.color.dialog_background));
		dialog.show();

		final RelativeLayout layoutRoot = (RelativeLayout) dialog
				.findViewById(R.id.layout_root);

		layoutRoot.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						Rect r = new Rect();
						layoutRoot.getWindowVisibleDisplayFrame(r);

						int screenHeight = layoutRoot.getRootView().getHeight();
						keyboardHeight = screenHeight - (r.bottom - r.top);
						//	AppLogger.i("Keyboard Size : " + keyboardHeight);
					}
				});

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		setterHeight = height - keyboardHeight;

		params.leftMargin = 10;
		params.rightMargin = 10;
		params.topMargin = (int) (setterHeight * 0.18);
		layoutRoot.setLayoutParams(params);

		final EditText userInput = (EditText) dialog
				.findViewById(R.id.editTextDialogUserInput);

		userInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.MAX_LENGTH_PIN)});

		Button btnOk = (Button) dialog.findViewById(R.id.btnOK);
		btnOk.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				hideKeyboard(arg0);
				if (!testValidity(userInput)) {
					return;
				} else if (userInput.getText().toString().length() < Constants.MAX_LENGTH_PIN) {
					userInput.setError(Constants.Messages.INVALID_PIN);
					return;
				}

				dialog.dismiss();
				mpinInterface.onMpinPopupClosed();

				try {
					String encryptedPin = AesEncryptor.encrypt(userInput
							.getText().toString());
					setEncryptedMpin(encryptedPin);
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (bundle != null & nextClass != null) {
					mpinProcess(bundle, nextClass);
				} else {
					mpinProcess(null, null);
				}
			}
		});

		Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				dialog.dismiss();
				if (closeAcitivty) {
					BaseActivity.this.finish();
				}
				mpinInterface.onMpinPopupClosed();
			}
		});
	}

	public void askMpin(final Bundle bundle, final Class<?> nextClass,
						final boolean closeAcitivty) {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int height = displaymetrics.heightPixels;
		int setterHeight = 0;

		final Dialog dialog = new Dialog(BaseActivity.this,
				android.R.style.Theme_Translucent_NoTitleBar);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_mpin);
		dialog.setCancelable(!closeAcitivty);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(R.color.dialog_background));
		dialog.show();

		final RelativeLayout layoutRoot = (RelativeLayout) dialog
				.findViewById(R.id.layout_root);

		layoutRoot.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						Rect r = new Rect();
						layoutRoot.getWindowVisibleDisplayFrame(r);

						int screenHeight = layoutRoot.getRootView().getHeight();
						keyboardHeight = screenHeight - (r.bottom - r.top);
						//	AppLogger.i("Keyboard Size : " + keyboardHeight);
					}
				});

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		setterHeight = height - keyboardHeight;

		params.leftMargin = 10;
		params.rightMargin = 10;
		params.topMargin = (int) (setterHeight * 0.18);
		layoutRoot.setLayoutParams(params);

		final EditText userInput = (EditText) dialog
				.findViewById(R.id.editTextDialogUserInput);

		userInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.MAX_LENGTH_PIN)});

		Button btnOk = (Button) dialog.findViewById(R.id.btnOK);
		btnOk.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				hideKeyboard(arg0);
				if (!testValidity(userInput)) {
					return;
				} else if (userInput.getText().toString().length() < Constants.MAX_LENGTH_PIN) {
					userInput.setError(Constants.Messages.INVALID_PIN);
					return;
				}

				dialog.dismiss();

				try {
					String encryptedPin = AesEncryptor.encrypt(userInput
							.getText().toString());
					setEncryptedMpin(encryptedPin);
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (bundle != null & nextClass != null) {
					mpinProcess(bundle, nextClass);
				} else {
					mpinProcess(null, null);
				}
			}
		});

		Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				dialog.dismiss();
				if (closeAcitivty) {
					BaseActivity.this.finish();
				}
			}
		});
	}

	public void askOtp(final Bundle bundle, final Class<?> nextClass,
					   final boolean closeAcitivty) {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int height = displaymetrics.heightPixels;
		int setterHeight = 0;

		final Dialog dialog = new Dialog(BaseActivity.this,
				android.R.style.Theme_Translucent_NoTitleBar);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_mpin);
		dialog.setCancelable(!closeAcitivty);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(R.color.dialog_background));
		dialog.show();

		final TextView headerText = (TextView) dialog
				.findViewById(R.id.heading_text);

		final TextView heading = (TextView) dialog
				.findViewById(R.id.textView1);

		headerText.setText("OTP Verification");
		heading.setText("Enter OTP");

		final RelativeLayout layoutRoot = (RelativeLayout) dialog
				.findViewById(R.id.layout_root);

		layoutRoot.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						Rect r = new Rect();
						layoutRoot.getWindowVisibleDisplayFrame(r);

						int screenHeight = layoutRoot.getRootView().getHeight();
						keyboardHeight = screenHeight - (r.bottom - r.top);
						//	AppLogger.i("Keyboard Size : " + keyboardHeight);
					}
				});

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		setterHeight = height - keyboardHeight;

		params.leftMargin = 10;
		params.rightMargin = 10;
		params.topMargin = (int) (setterHeight * 0.18);
		layoutRoot.setLayoutParams(params);

		final EditText userInput = (EditText) dialog
				.findViewById(R.id.editTextDialogUserInput);

		Button btnOk = (Button) dialog.findViewById(R.id.btnOK);
		btnOk.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				hideKeyboard(arg0);
				if (!testValidity(userInput)) {
					return;
				} else if (userInput.getText().toString().length() < Constants.MAX_LENGTH_THIRD_PARTY_OTP) {
					userInput.setError(Constants.Messages.INVALID_THIRD_PARTY_OTP);
					return;
				}

				dialog.dismiss();

				try {
					String encryptedPin = AesEncryptor.encrypt(userInput.getText().toString());
					setEncryptedOtp(encryptedPin);
				} catch (Exception e) {
					e.printStackTrace();
				}

				otpProcess(null, null);

			}
		});

		Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				dialog.dismiss();
				if (closeAcitivty) {
					BaseActivity.this.finish();
				}
			}
		});
	}

	public void otpProcess(final Bundle bundle, final Class<?> nextClass) {
		showLoading("Please Wait", "Processing...");
		Thread mThread = new Thread() {
			@Override
			public void run() {
				try {
					Looper.prepare();
					synchronized (this) {
						wait(1750);
					}
				} catch (InterruptedException ex) {
				}

				hideLoading();
				Intent intent = new Intent(BaseActivity.this, nextClass);
				if (bundle != null) {
					intent.putExtras(bundle);
				}
				startActivity(intent);
				finish();
			}
		};
		mThread.start();
	}


	public void mpinProcess(final Bundle bundle, final Class<?> nextClass) {
		showLoading("Please Wait", "Processing...");
		Thread mThread = new Thread() {
			@Override
			public void run() {
				try {
					Looper.prepare();
					synchronized (this) {
						wait(1750);
					}
				} catch (InterruptedException ex) {
				}

				hideLoading();
				Intent intent = new Intent(BaseActivity.this, nextClass);
				if (bundle != null) {
					intent.putExtras(bundle);
				}
				startActivity(intent);
				finish();
			}
		};
		mThread.start();
	}

	public String getEncryptedMpin() {
		return encryptedPin;
	}

	public String getEncryptedOtp() {
		return encryptedOtp;
	}

	public void setEncryptedMpin(String encryptedMpin) {
		encryptedPin = encryptedMpin;
	}

	public void setEncryptedOtp(String otp) {
		encryptedOtp = otp;
	}

	public void showRegistrationDialog(String title, String msg,
									   View.OnClickListener btnContinueListener,
									   View.OnClickListener btnRegisterListener) {
		final Dialog dialog = new Dialog(BaseActivity.this,
				android.R.style.Theme_Translucent_NoTitleBar);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_registration);
		dialog.show();

		final TextView headingText = (TextView) dialog
				.findViewById(R.id.heading_text);
		headingText.setText(title);

		final TextView msgText = (TextView) dialog.findViewById(R.id.msg_text);
		msgText.setText(msg);

		Button btnRegister = (Button) dialog.findViewById(R.id.btnRegister);
		if (btnRegisterListener != null) {
			btnRegister.setOnClickListener(btnRegisterListener);
		} else {
			btnRegister.setOnClickListener(new OnClickListener() {
				public void onClick(View arg0) {
					dialog.dismiss();
				}
			});
		}
		Button btnContinue = (Button) dialog.findViewById(R.id.btnContinue);
		if (btnContinueListener != null) {
			btnContinue.setOnClickListener(btnContinueListener);
		} else {

			btnContinue.setOnClickListener(new OnClickListener() {
				public void onClick(View arg0) {
					dialog.dismiss();
				}
			});
		}
	}

	public void showConfirmationDialog(final String title, final String msg,//calling this
									   final DialogInterface.OnClickListener negativelistner,
									   final String NoTxt,
									   final DialogInterface.OnClickListener positivelistner,
									   final String YesTxt) {

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				final AlertDialog.Builder alert = new AlertDialog.Builder(
						BaseActivity.this);

				alert.setTitle(title);
				alert.setMessage(msg);
				alert.setCancelable(false);

				alert.setPositiveButton(NoTxt, negativelistner);
				alert.setNegativeButton(YesTxt, positivelistner);

				final AlertDialog dialog = alert.create();
				dialog.show();
			}
		});

	}

	public void openMarketUrl() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri
				.parse(Constants.APP_DOWNLOAD_URL));
		startActivity(intent);
	}

	public void headerImplementation() {
		btnHome = (ImageView) findViewById(R.id.imageViewHome);
		btnExit = (Button) findViewById(R.id.buttonsignout);

		btnHome.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				hideKeyboard(v);
				finish();
			}
		});
		btnExit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showConfirmExitDialog(null);
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		SharedPreferences preferences = getSharedPreferences("Session_js_bank",
				MODE_PRIVATE);
		preferences.edit().putLong("pause_time", 0).commit();
	}

	@Override
	protected void onPause() {
		super.onPause();
		SharedPreferences preferences = getSharedPreferences("Session_js_bank",
				MODE_PRIVATE);
		long openTime = System.currentTimeMillis();
		preferences.edit().putLong("pause_time", openTime).commit();
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
	}

	@Override
	protected void onResume() {
		super.onResume();

		long sec = 1200 * 1000; // Milliseconds in a day. Define this as a
		// constant
		SharedPreferences preferences = getSharedPreferences("Session_js_bank",
				MODE_PRIVATE);
		long openTime = System.currentTimeMillis();
		long lastTime = preferences.getLong("pause_time", 0);

		if (lastTime != 0) {
			if ((openTime - lastTime) > sec) {
				// if any transaction is in process then close loading
				if (loadingDialog != null && loadingDialog.isShowing()) {
					hideLoading();

					Utility.createAlertDialog(
							Constants.Messages.SESSION_EXPIRED,
							Constants.Messages.ALERT_HEADING,
							BaseActivity.this,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
													int arg1) {
									loginPage();
								}
							});
				} else {
					// create alert dialog so user can safely signout
					Utility.createAlertDialog(
							Constants.Messages.SESSION_EXPIRED,
							Constants.Messages.ALERT_HEADING,
							BaseActivity.this,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
													int arg1) {
									Intent intent = new Intent(
											BaseActivity.this,
											SignOutActivity.class);
									intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									startActivity(intent);
									finish();
								}
							});
				}
			}
		}
	}

	public void gotoAccountOpening() {
		Intent intent = new Intent(getApplicationContext(),
				MainMenuActivity.class);
		intent.putExtra(Constants.IntentKeys.FLAG_TRANSITION,
				Constants.IntentKeys.FLAG_OPEN_ACCOUNT);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}

	public void gotoAccountOpening(String CMOB) {
		Intent intent = new Intent(getApplicationContext(),
				MainMenuActivity.class);
		intent.putExtra(Constants.IntentKeys.FLAG_TRANSITION,
				Constants.IntentKeys.FLAG_OPEN_ACCOUNT);
		intent.putExtra(Constants.IntentKeys.RCMOB, CMOB);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}

	public void gotoAccountOpening(String cMob, String cnic) {
		Intent intent = new Intent(getApplicationContext(),
				MainMenuActivity.class);
		intent.putExtra(Constants.IntentKeys.FLAG_TRANSITION,
				Constants.IntentKeys.FLAG_OPEN_ACCOUNT);
		intent.putExtra(Constants.IntentKeys.RCMOB, cMob);
		intent.putExtra(Constants.IntentKeys.RCNIC, cnic);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}

	public void addAutoKeyboardHideFunction() {
		View parentLayout = findViewById(R.id.parent_layout);
		parentLayout.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent ev) {
				hideKeyboard(view);
				return false;
			}
		});
	}

	public void addAutoKeyboardHideFunctionScrolling() {
		View parentLayout = findViewById(R.id.parent_layout);
		parentLayout.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent ev) {
				hideKeyboard(view);
				return false;
			}
		});

		View parentLayoutScroll = findViewById(R.id.scView);
		parentLayoutScroll.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				if (Math.abs(mDeBounce - motionEvent.getEventTime()) < 250) {
					// Ignore if it's been less then 250ms since
					// the item was last clicked
					return true;
				}

				if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
					isScrolling = true;

					mActionDownX = motionEvent.getX();
					mActionDownY = motionEvent.getY();
				}
				if (motionEvent.getAction() == MotionEvent.ACTION_MOVE
						&& (Math.abs(mActionDownX - motionEvent.getX()) > 6 || Math
						.abs(mActionDownY - motionEvent.getY()) > 6)) {

					isScrolling = false;
				}

				int intCurrentY = Math.round(motionEvent.getY());
				int intCurrentX = Math.round(motionEvent.getX());
				int intStartY = motionEvent.getHistorySize() > 0 ? Math
						.round(motionEvent.getHistoricalY(0)) : intCurrentY;
				int intStartX = motionEvent.getHistorySize() > 0 ? Math
						.round(motionEvent.getHistoricalX(0)) : intCurrentX;

				if ((motionEvent.getAction() == MotionEvent.ACTION_UP)
						&& (Math.abs(intCurrentX - intStartX) < 3)
						&& (Math.abs(intCurrentY - intStartY) < 3)) {
					if (mDeBounce > motionEvent.getDownTime()) {
						// Still got occasional duplicates without this
						return false;
					}

					// Handle the click
					if (isScrolling) {
						hideKeyboard(view);
					}

					mDeBounce = motionEvent.getEventTime();
					return false;
				}
				return false;
			}
		});
	}

	protected void deleteAgentmatePics() {
		new DeleteAgentmatePictures().execute();
	}

	protected class DeleteAgentmatePictures extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			showLoading("Please Wait", "Processing...");
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
						"AGENTmateApps");
				if (directory.isDirectory()) {
					String[] children = directory.list();
					for (int i = 0; i < children.length; i++) {
						new File(directory, children[i]).delete();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			hideLoading();
		}
	}

	public static class AppFingers {
		public static String RIGHT_THUMB = "RIGHT_THUMB";
		public static String RIGHT_INDEX = "RIGHT_INDEX";
		public static String RIGHT_MIDDLE = "RIGHT_MIDDLE";
		public static String RIGHT_RING = "RIGHT_RING";
		public static String RIGHT_LITTLE = "RIGHT_LITTLE";
		public static String LEFT_THUMB = "LEFT_THUMB";
		public static String LEFT_INDEX = "LEFT_INDEX";
		public static String LEFT_MIDDLE = "LEFT_MIDDLE";
		public static String LEFT_RING = "LEFT_RING";
		public static String LEFT_LITTLE = "LEFT_LITTLE";
	}

	public String getRespectiveFinger(String finger) {
		if (finger.equals("RIGHT_THUMB")) return "1";
		if (finger.equals("RIGHT_INDEX")) return "2";
		if (finger.equals("RIGHT_MIDDLE")) return "3";
		if (finger.equals("RIGHT_RING")) return "4";
		if (finger.equals("RIGHT_LITTLE")) return "5";

		if (finger.equals("LEFT_THUMB")) return "6";
		if (finger.equals("LEFT_INDEX")) return "7";
		if (finger.equals("LEFT_MIDDLE")) return "8";
		if (finger.equals("LEFT_RING")) return "9";
		if (finger.equals("LEFT_LITTLE")) return "10";
		return null;
	}

	public static String jsonPrettify(String jsonString) {
		JsonParser parser = new JsonParser();
		JsonObject json = parser.parse(jsonString).getAsJsonObject();

		Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
		String prettyJson = gson.toJson(json);
		return prettyJson;
	}

	protected void focusAndShowKeyboard(Context context, EditText editText) {
		editText.requestFocus();
		editText.setSelection(editText.getText().length());
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	protected void disableCopyPaste(final EditText editText) {
		editText.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				focusAndShowKeyboard(view.getContext(), editText);
				return true;
			}
		});
	}
}