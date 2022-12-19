package com.inov8.agentmate.activities.openAccount;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.inov8.agentmate.activities.BaseCommunicationActivity;
import com.inov8.agentmate.model.HttpResponseModel;
import com.inov8.agentmate.model.MessageModel;
import com.inov8.agentmate.model.ProductModel;
import com.inov8.agentmate.net.HttpAsyncTask;
import com.inov8.agentmate.parser.XmlParser;
import com.inov8.agentmate.util.AppLogger;
import com.inov8.agentmate.util.Constants;
import com.inov8.jsblmfs.R;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import static com.inov8.agentmate.util.Utility.testValidity;

public class OpenAccountMobileInputActivity extends BaseCommunicationActivity {
	private TextView lblField1, lblField2, lblField3, lblField4;
	private EditText mobileNo, confirmMobileNo, cnicEditText,
			cnicConfirmEditText;
	private Button btnNext;
	private TextView lblHeading;
	private String txtMobileNo, txtConfirmMobileNo, txtCnic, txtConfirmCnic;
	private String msisdn, cnic, cname, cusRegStateId, cusRegState, cnicExpiry, rcmob, rwcnic, trxid;
	private boolean bulkRegister = false;
	private ProductModel product;
	private int isReceiveCash;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private boolean cameraPermission = false;
    private boolean storagePermission = false;

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_input_general);

        checkPermissions();
        deleteAgentmatePics();

        try {
            product = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);
            rcmob = mBundle.getString(Constants.IntentKeys.RCMOB);
            rwcnic = mBundle.getString(Constants.IntentKeys.RCNIC);
            trxid = mBundle.getString(Constants.IntentKeys.TRXID);
            isReceiveCash = mBundle.getInt(Constants.IntentKeys.IS_RECEIVE_CASH);

            lblHeading = (TextView) findViewById(R.id.lblHeading);
            lblHeading.setText("Account Opening");

            lblField1 = (TextView) findViewById(R.id.lblField1);
            lblField1.setText("Customer MSISDN");
            lblField1.setVisibility(View.VISIBLE);

            lblField2 = (TextView) findViewById(R.id.lblField2);
            lblField2.setText("Confirm MSISDN");
            lblField2.setVisibility(View.VISIBLE);

            lblField3 = (TextView) findViewById(R.id.lblField3);
            lblField3.setText("CNIC");
            lblField3.setVisibility(View.VISIBLE);

            lblField4 = (TextView) findViewById(R.id.lblField4);
            lblField4.setText("Confirm CNIC");
            lblField4.setVisibility(View.VISIBLE);

            mobileNo = (EditText) findViewById(R.id.input1);
            mobileNo.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
            mobileNo.setVisibility(View.VISIBLE);
            disableCopyPaste(mobileNo);

            confirmMobileNo = (EditText) findViewById(R.id.input2);
            confirmMobileNo.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
            confirmMobileNo.setVisibility(View.VISIBLE);
            disableCopyPaste(confirmMobileNo);

            if (rcmob != null) {
                mobileNo.setText(rcmob);
                confirmMobileNo.setText(rcmob);
                mobileNo.setEnabled(false);
                confirmMobileNo.setEnabled(false);
            }

            cnicEditText = (EditText) findViewById(R.id.input3);
            cnicEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
            cnicEditText.setVisibility(View.VISIBLE);
            disableCopyPaste(cnicEditText);

            cnicConfirmEditText = (EditText) findViewById(R.id.input4);
            cnicConfirmEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
            cnicConfirmEditText.setVisibility(View.VISIBLE);
            disableCopyPaste(cnicConfirmEditText);

            if (rwcnic != null) {
                cnicEditText.setText(rwcnic);
                cnicConfirmEditText.setText(rwcnic);
                cnicEditText.setEnabled(false);
                cnicConfirmEditText.setEnabled(false);
            }

            btnNext = (Button) findViewById(R.id.btnNext);
            btnNext.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    if(!cameraPermission)
                    {
                        checkPermissions();
                        return;
                    }

                    if(!storagePermission)
                    {
                        checkPermissions();
                        return;
                    }

                    // verifying mobile no.
                    if (!testValidity(mobileNo)) {
                        return;
                    }
                    txtMobileNo = mobileNo.getText().toString();
                    if (txtMobileNo.length() < Constants.MAX_LENGTH_MOBILE) {
                        mobileNo.setError(Constants.Messages.INVALID_MOBILE_NUMBER);
                        return;
                    }
                    if (txtMobileNo != null && (txtMobileNo.charAt(0) != '0' || txtMobileNo.charAt(1) != '3')) {
                        mobileNo.setError(Constants.Messages.INVALID_MOBILE_NUMBER_FORMAT);
                        return;
                    }

                    if(!testValidity(confirmMobileNo)){
                        return;
                    }
                    txtConfirmMobileNo = confirmMobileNo.getText().toString();
                    if (txtConfirmMobileNo.length() < Constants.MAX_LENGTH_MOBILE) {
                        confirmMobileNo.setError(Constants.Messages.INVALID_MOBILE_NUMBER);
                        return;
                    }
                    if (txtConfirmMobileNo != null && (txtConfirmMobileNo.charAt(0) != '0' || txtConfirmMobileNo.charAt(1) != '3')) {
                        confirmMobileNo.setError(Constants.Messages.INVALID_MOBILE_NUMBER_FORMAT);
                        return;
                    }

                    if (!(txtMobileNo).equals(txtConfirmMobileNo)) {
                        Toast.makeText(OpenAccountMobileInputActivity.this,
                                "Mobile No. and Confirm Mobile No. Mismatch",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    msisdn = txtMobileNo;

                    // verify cnic
                    if(!testValidity(cnicEditText)){
                        return;
                    }
                    if (cnicEditText.length() < Constants.MAX_LENGTH_CNIC) {
                        cnicEditText.setError(Constants.Messages.INVALID_CNIC);
                        return;
                    }
                    txtCnic = cnicEditText.getText().toString();

                    if(!testValidity(cnicConfirmEditText)){
                        return;
                    }
                    if (cnicConfirmEditText.length() < Constants.MAX_LENGTH_CNIC) {
                        cnicConfirmEditText.setError(Constants.Messages.INVALID_CNIC);
                        return;
                    }
                    txtConfirmCnic = cnicConfirmEditText.getText().toString();

                    if (!(txtCnic).equals(txtConfirmCnic)) {
                        Toast.makeText(OpenAccountMobileInputActivity.this,
                                "CNIC and Confirm CNIC Mismatch",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // preparing command and data for server call
                    msisdn = txtMobileNo;
                    cnic = txtCnic;
                    processRequest();
                }
            });
            headerImplementation();
            addAutoKeyboardHideFunctionScrolling();
        }catch (Exception e){
            e.printStackTrace();
            createAlertDialog(Constants.Messages.EXCEPTION_SERVICE_UNAVAILABLE, Constants.KEY_LIST_ALERT);
        }
	}

	@Override
	public void processRequest() {
		if (!haveInternet()) {
			Toast.makeText(getApplication(), Constants.Messages.INTERNET_CONNECTION_PROBLEM,
					Toast.LENGTH_SHORT).show();
			return;
		}

		try {
			showLoading("Please Wait", "Processing...");
			new HttpAsyncTask(OpenAccountMobileInputActivity.this).execute(
					Constants.CMD_OPEN_ACCOUNT_VERIFY_CUSTOMER_REGISTRATION+"", msisdn, cnic,
                    trxid, isReceiveCash+"", "0");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void processResponse(HttpResponseModel response) {
		try {
			XmlParser xmlParser = new XmlParser();
			Hashtable<?, ?> table = xmlParser.convertXmlToTable(response
					.getXmlResponse());
			if (table != null && table.containsKey("errs")) {
				List<?> list = (List<?>) table.get("errs");
				MessageModel message = (MessageModel) list.get(0);
				hideLoading();
				createAlertDialog(message.getDescr(), "Alert");

				String code = message.getCode();
				AppLogger.i("##Error Code: " + code);
			}
			else
			    {
				boolean isNext = true;
				if (table != null
						&& table.containsKey(Constants.ATTR_CREG_STATE)) {
					cusRegState = (String) table.get(Constants.ATTR_CREG_STATE);
					cusRegStateId = (String) table
							.get(Constants.ATTR_CREG_STATE_ID);
					if (cusRegStateId
							.equals(Constants.REGISTRATION_STATE_BULK_REQUEST_RECEIVED)) {
						msisdn = (String) table.get(Constants.ATTR_CMOB);
						cnic = (String) table.get(Constants.ATTR_CNIC);
						cname = (String) table.get(Constants.ATTR_CNAME);
						cnicExpiry = (String) table.get(Constants.ATTR_CNIC_EXP);
						bulkRegister = true;
					}
					if (cusRegStateId
							.equals(Constants.REGISTRATION_STATE_DISCREPANT)) {
						isNext = false;
						msisdn = (String) table.get(Constants.ATTR_CMOB);
						cnic = (String) table.get(Constants.ATTR_CNIC);
						cname = (String) table.get(Constants.ATTR_CNAME);
						cnicExpiry = (String) table.get(Constants.ATTR_CNIC_EXP);

						Intent intent = new Intent(
								OpenAccountMobileInputActivity.this,
								OpenAccountDiscrepantDetailsActivity.class);

						// images list
						ArrayList<Integer> imagesFlags = new ArrayList<Integer>();

						if (((String) table
								.get(Constants.ATTR_TERMS_PHOTO_FLAG))
								.equals("1")) {
							imagesFlags
									.add(Constants.AccountOpenningImages.TERMS);
						}
						if (((String) table
								.get(Constants.ATTR_CUSTOMER_PHOTO_FLAG))
								.equals("1")) {
							imagesFlags
									.add(Constants.AccountOpenningImages.CUSTOMER);
						}

						if (((String) table
								.get(Constants.ATTR_SIGNATURE_PHOTO_FLAG))
								.equals("1")) {
							imagesFlags
									.add(Constants.AccountOpenningImages.SIGNATURE);
						}
						if (((String) table
								.get(Constants.ATTR_CNIC_FRONT_PHOTO_FLAG))
								.equals("1")) {
							imagesFlags
									.add(Constants.AccountOpenningImages.CNIC_FRONT);
						}
						if (((String) table
								.get(Constants.ATTR_CNIC_BACK_PHOTO_FLAG))
								.equals("1")) {
							imagesFlags
									.add(Constants.AccountOpenningImages.CNIC_BACK);
						}
						if (((String) table
								.get(Constants.ATTR_L1_FORM_PHOTO_FLAG))
								.equals("1")) {
							imagesFlags
									.add(Constants.AccountOpenningImages.L1_FORM);
						}

						intent.putIntegerArrayListExtra(
								Constants.IntentKeys.OPEN_ACCOUNT_IMAGES_LIST,
								imagesFlags);
						intent.putExtra(Constants.ATTR_CMSISDN, msisdn);
						intent.putExtra(Constants.ATTR_CNIC, cnic);
						mBundle.putString(Constants.ATTR_CNAME, cname);
						intent.putExtra(Constants.ATTR_CREG_STATE, cusRegState);
						intent.putExtra(Constants.ATTR_CREG_STATE_ID,
								cusRegStateId);
						intent.putExtra(Constants.ATTR_CREG_COMMENT,
								(String) table.get(Constants.ATTR_CREG_COMMENT));
						intent.putExtra(Constants.ATTR_DEPOSIT_AMT + "_server",
								(String) table.get(Constants.ATTR_DEPOSIT_AMT));
						intent.putExtra(Constants.ATTR_CUST_ACC_TYPE
								+ "_server", (String) table
								.get(Constants.ATTR_CUST_ACC_TYPE));
						intent.putExtra(Constants.ATTR_CDOB + "_server",
								(String) table.get(Constants.ATTR_CDOB));
						intent.putExtra(Constants.ATTR_CNIC_EXP + "_server",
								cnicExpiry);

						startActivity(intent);
						hideLoading();

					} else if (cusRegStateId
							.equals(Constants.REGISTRATION_STATE_REQUEST_RECEIVED)) {
						hideLoading();
						createAlertDialog("Request Already Received.", "Alert");
						isNext = false;
					}
				}

				if (isNext) {
					processNext();
				}
			}
		} catch (Exception e) {
			hideLoading();
			e.printStackTrace();
            createAlertDialog(Constants.Messages.EXCEPTION_INVALID_RESPONSE, Constants.KEY_LIST_ALERT);
		}
	}

	@Override
	public void processNext() {
		Intent intent = new Intent(OpenAccountMobileInputActivity.this,
				OpenAccountSecondInputActivity.class);
		intent.putExtra(Constants.ATTR_CMSISDN, msisdn);
		intent.putExtra(Constants.ATTR_CNIC, cnic);
		intent.putExtra(Constants.ATTR_CNIC_EXP + "_server", cnicExpiry);
		mBundle.putString(Constants.ATTR_CNAME, cname);
		intent.putExtra(Constants.ATTR_CREG_STATE, cusRegState);
		intent.putExtra(Constants.ATTR_CREG_STATE_ID, cusRegStateId);
		intent.putExtra("isBulkRegister", bulkRegister);
		intent.putExtras(mBundle);

		startActivity(intent);
		hideLoading();
	}

    private void checkPermissions() {

        if (Build.VERSION.SDK_INT < 23) {
            cameraPermission = true;
            storagePermission = true;
            return;
        }

        int hasCameraPermission = checkSelfPermission(Manifest.permission.CAMERA);
        int hasStoragePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        else {
            cameraPermission = true;
            if (hasStoragePermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }
            else
            {
                storagePermission = true;
            }
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(OpenAccountMobileInputActivity.this)
                .setTitle(Constants.KEY_LIST_ALERT)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS: {

                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    int grantResult = grantResults[i];

                    if (permission.equals(Manifest.permission.CAMERA)) {
                        if (grantResult == PackageManager.PERMISSION_GRANTED) {
                            cameraPermission = true;
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    REQUEST_CODE_ASK_PERMISSIONS);
                        } else {
                            // Permission Denied
                            //Toast.makeText(OpenAccountMobileInputActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();

                            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                                showMessageOKCancel("You need to allow access to Camera to take Photos for Account Opening.",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                requestPermissions(new String[]{Manifest.permission.CAMERA},
                                                        REQUEST_CODE_ASK_PERMISSIONS);
                                            }
                                        });
                            }
                            else
                            {
                                Toast.makeText(OpenAccountMobileInputActivity.this, "Please allow camera permissions from app settings to proceed further.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        if (grantResult == PackageManager.PERMISSION_GRANTED) {
                            storagePermission = true;
                        } else {
                            // Permission Denied
                            //Toast.makeText(OpenAccountMobileInputActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();

                            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                showMessageOKCancel("You need to allow Storage access to save Account Opening Photos",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                        REQUEST_CODE_ASK_PERMISSIONS);
                                            }
                                        });
                            }
                            else
                            {
                                Toast.makeText(OpenAccountMobileInputActivity.this, "Please allow storage permissions from app settings to proceed further", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

	@Override
	public void onBackPressed() {
		goToMainMenu();
	}
}