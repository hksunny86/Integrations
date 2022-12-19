package com.inov8.agentmate.activities.openAccount;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.inov8.agentmate.activities.BaseActivity;
import com.inov8.agentmate.model.HttpResponseModel;
import com.inov8.agentmate.model.MessageModel;
import com.inov8.agentmate.model.ProductModel;
import com.inov8.agentmate.parser.XmlParser;
import com.inov8.agentmate.util.AppLogger;
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.PopupDialogs;
import com.inov8.agentmate.util.PreferenceConnector;
import com.inov8.agentmate.util.Utility;
import com.inov8.jsbl.sco.R;

public class OpenAccountPicInputUploadActivity extends BaseActivity {
	private TextView lblHeading;

	private Intent intentNext;
	private Button btnNextScreen;

	private boolean  flagCustomer = false,
            flagCnicFront = false;

	private boolean flagBtnCustomer = false,
            flagBtnCnicFront = false;

	private boolean flagUploadCustomer = false,
			flagUploadCnicFront = false;

	private AlertDialog alertDialog;
	private ViewGroup mainViewGroup;
    private ArrayList<Integer> imageList;

	/**
	 * Customer
	 */
	private RelativeLayout layoutCustomer;
	private Button btnCustomer, btnCustomerRetake, btnCustomerConfirm;
	private ImageView imageViewCustomer, imageViewProgressCustomer;
	private Uri uriCustomer;
	private String photoPathCustomer;
	private ProgressBar progressUploadCustomer;
	private TextView lblCustomer;
	private RelativeLayout relCustomerImage;

	/**
	 * CNIC Front
	 */
	private RelativeLayout layoutNICFront;
	private Button btnNICFront, btnNICFrontRetake, btnNICFrontConfirm;
	private ImageView imageViewNICFront, imageViewProgressNICFront;
	private Uri uriNICFront;
	private String photoPathNICFront;
	private ProgressBar progressUploadNICFront;
	private TextView lblNICFront;
	private RelativeLayout relNICFrontImage;

	private final String IMAGE_CUSTOMER = "0";
	private final String IMAGE_NIC_FRONT = "1";

	private ProductModel product;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_open_account_pic_input_upload);

		mainViewGroup = (ViewGroup) findViewById(R.id.scViewInLayout);

        try {
            product = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);
            imageList = mBundle.getIntegerArrayList(Constants.IntentKeys.OPEN_ACCOUNT_IMAGES_LIST);
        }catch (Exception e){
            e.printStackTrace();
        }

		lblHeading = (TextView) findViewById(R.id.lblHeading);
		lblHeading.setText("Account Opening");

		intentNext = new Intent(OpenAccountPicInputUploadActivity.this,
				OpenAccountConfirmationActivity.class);

        layoutCustomer = (RelativeLayout) findViewById(R.id.layoutCustomer);

		btnNextScreen = (Button) findViewById(R.id.btnNextScreen);
		btnNextScreen.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				AppLogger.i("##Customer" + flagCustomer);
				AppLogger.i("##NIC Front" + flagCnicFront);

				if (flagCustomer && flagCnicFront) {
					intentNext.putExtras(mBundle);
					startActivity(intentNext);
				} else {
					Toast.makeText(getApplicationContext(),
							"Please upload required images before proceeding.",
							Toast.LENGTH_LONG).show();
				}
			}
		});

		// Checking camera availability
		if (!isDeviceSupportCamera()) {
			Toast.makeText(getApplicationContext(),
					"Sorry! Your device doesn't support camera",
					Toast.LENGTH_LONG).show();
			// will close the app if the device does't have camera
			finish();
		}

		boolean mExternalStorageWriteable = false;
		boolean mExternalStorageAvailable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// We can read and write the media
			mExternalStorageAvailable = mExternalStorageWriteable = true;
            if(imageList!=null){
                if(imageList.get(0) == Constants.AccountOpenningImages.CUSTOMER){
                    loadCustomerLayout();
                }
                else if(imageList.get(0) == Constants.AccountOpenningImages.CNIC_FRONT){
                    layoutCustomer.setVisibility(View.GONE);
                    flagCustomer = true;
                    loadNICFrontLayout();
                }
            }
            else {
                loadCustomerLayout();
            }
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		} else {
			mExternalStorageAvailable = mExternalStorageWriteable = false;
		}

		if (!mExternalStorageAvailable || !mExternalStorageWriteable) {
			Toast.makeText(
					getApplicationContext(),
					"Sorry! SD Card is not present or may not be able to store pictures.",
					Toast.LENGTH_LONG).show();
			finish();
		}

		headerImplementation();
		addAutoKeyboardHideFunctionScrolling();
	}

	private void loadCustomerLayout() {
		layoutCustomer.setVisibility(View.VISIBLE);

		progressUploadCustomer = (ProgressBar) findViewById(R.id.progressUploadCustomer);
		progressUploadCustomer.getIndeterminateDrawable().setColorFilter(
				Color.parseColor("#3e4a64"),
				android.graphics.PorterDuff.Mode.MULTIPLY);
		progressUploadCustomer.setIndeterminate(true);
		progressUploadCustomer.invalidate();

		btnCustomer = (Button) findViewById(R.id.btnCustomer);
		btnCustomer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(
						OpenAccountPicInputUploadActivity.this,
						CameraActivity.class);
				mBundle.putString("PHOTO_TYPE", "customer");
				intent.putExtras(mBundle);
				startActivityForResult(intent,
						Constants.REQUEST_GET_DEVICE_CAMERA_FOR_CUSTOMER);
			}
		});

		btnCustomerRetake = (Button) findViewById(R.id.btnCustomerRetake);
		btnCustomerRetake.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!flagBtnCustomer) {
					flagBtnCustomer = true;
					Intent intent = new Intent(
							OpenAccountPicInputUploadActivity.this,
							CameraActivity.class);
					mBundle.putString("PHOTO_TYPE", "customer");
					intent.putExtras(mBundle);
					startActivityForResult(intent,
							Constants.REQUEST_GET_DEVICE_CAMERA_FOR_CUSTOMER);
				}
			}
		});

		btnCustomerConfirm = (Button) findViewById(R.id.btnCustomerConfirm);
		btnCustomerConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ((alertDialog == null)
						|| (alertDialog != null && !alertDialog.isShowing())) {
					showAlertPopup(IMAGE_CUSTOMER);
				}
			}
		});
	}

	private void loadNICFrontLayout() {
		layoutNICFront = (RelativeLayout) findViewById(R.id.layoutNICFront);
		layoutNICFront.setVisibility(View.VISIBLE);

		ScrollView scView = (ScrollView) findViewById(R.id.scView);
		scView.smoothScrollTo(0, scView.getBottom());

		progressUploadNICFront = (ProgressBar) findViewById(R.id.progressUploadNICFront);
		progressUploadNICFront.getIndeterminateDrawable().setColorFilter(
				Color.parseColor("#3e4a64"),
				android.graphics.PorterDuff.Mode.MULTIPLY);
		progressUploadNICFront.setIndeterminate(true);
		progressUploadNICFront.invalidate();

		btnNICFront = (Button) findViewById(R.id.btnNICFront);
		btnNICFront.requestFocus();
		btnNICFront.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(
						OpenAccountPicInputUploadActivity.this,
						CameraActivity.class);
				mBundle.putString("PHOTO_TYPE", "cnic_front");
				intent.putExtras(mBundle);
				startActivityForResult(intent,
						Constants.REQUEST_GET_DEVICE_CAMERA_FOR_CNIC_FRONT);
			}
		});

		btnNICFrontRetake = (Button) findViewById(R.id.btnNICFrontRetake);
		btnNICFrontRetake.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!flagBtnCnicFront) {
					flagBtnCnicFront = true;
					Intent intent = new Intent(
							OpenAccountPicInputUploadActivity.this,
							CameraActivity.class);
					mBundle.putString("PHOTO_TYPE", "cnic_front");
					intent.putExtras(mBundle);
					startActivityForResult(intent,
							Constants.REQUEST_GET_DEVICE_CAMERA_FOR_CNIC_FRONT);
				}
			}
		});

		btnNICFrontConfirm = (Button) findViewById(R.id.btnNICFrontConfirm);
		btnNICFrontConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ((alertDialog == null)
						|| (alertDialog != null && !alertDialog.isShowing())) {
					showAlertPopup(IMAGE_NIC_FRONT);
				}
			}
		});
	}

	public void showAlertPopup(final String type) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		// set dialog options
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();

								if (!haveInternet()) {
									runOnUiThread(new Runnable() {
										@Override
										public void run() {
											Toast.makeText(
													getApplication(),
													"Internet/Network not available.",
													Toast.LENGTH_SHORT).show();
											return;
										}
									});
								} else {
									switch (type) {
									case IMAGE_CUSTOMER:
										flagUploadCustomer = false;
										lblCustomer = (TextView) findViewById(R.id.lblCustomer);
										lblCustomer.setText("Customer photo is uploading...");
										relCustomerImage = (RelativeLayout) findViewById(R.id.relCustomerImage);
										relCustomerImage
												.setVisibility(View.GONE);
										progressUploadCustomer
												.setVisibility(ProgressBar.VISIBLE);
										imageViewProgressCustomer = (ImageView) findViewById(R.id.imageViewProgressCustomer);
										imageViewProgressCustomer
												.setVisibility(View.VISIBLE);
										imageViewProgressCustomer
												.setImageResource(R.drawable.up);

										imageViewProgressCustomer
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														if (flagUploadCustomer) {
															flagCustomer = false;
															flagBtnCustomer = false;
															imageViewProgressCustomer
																	.setVisibility(View.GONE);
															relCustomerImage
																	.setVisibility(View.VISIBLE);
															progressUploadCustomer
																	.setVisibility(ProgressBar.GONE);
															btnCustomer
																	.setVisibility(View.INVISIBLE);
															btnCustomerRetake
																	.setVisibility(View.VISIBLE);
															btnCustomerConfirm
																	.setVisibility(View.VISIBLE);
														}
													}
												});
										uploadImage(uriCustomer.getPath(), IMAGE_CUSTOMER);
                                        if(imageList!=null && imageList.size() == 1) {  // discrepant flow
                                            flagCnicFront = true;
                                            btnNextScreen.setVisibility(View.VISIBLE);
                                        }
                                        else
                                            loadNICFrontLayout();
										break;
									case IMAGE_NIC_FRONT:
										flagUploadCnicFront = false;
										lblNICFront = (TextView) findViewById(R.id.lblNICFront);
										lblNICFront.setText("CNIC Front photo is uploading...");
										relNICFrontImage = (RelativeLayout) findViewById(R.id.relNICFrontImage);
										relNICFrontImage
												.setVisibility(View.GONE);
										progressUploadNICFront
												.setVisibility(ProgressBar.VISIBLE);
										imageViewProgressNICFront = (ImageView) findViewById(R.id.imageViewProgressNICFront);
										imageViewProgressNICFront
												.setVisibility(View.VISIBLE);
										imageViewProgressNICFront
												.setImageResource(R.drawable.up);

										imageViewProgressNICFront
												.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														if (flagUploadCnicFront) {
															flagCnicFront = false;
															flagBtnCnicFront = false;
															relNICFrontImage
																	.setVisibility(View.VISIBLE);
															imageViewProgressNICFront
																	.setVisibility(View.GONE);
															progressUploadNICFront
																	.setVisibility(View.GONE);
															btnNICFront
																	.setVisibility(View.INVISIBLE);
															btnNICFrontRetake
																	.setVisibility(View.VISIBLE);
															btnNICFrontConfirm
																	.setVisibility(View.VISIBLE);
														}
													}
												});

										uploadImage(uriNICFront.getPath(), IMAGE_NIC_FRONT);
                                        btnNextScreen.setVisibility(View.VISIBLE);
										break;
									}
								}
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
					// set dialog message
				}).setMessage("Do you want to upload this picture?");

		// create alert dialog
		alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	/**
	 * Checking device has camera hardware or not
	 * */
	private boolean isDeviceSupportCamera() {
		if (getApplicationContext().getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			// this device has a camera
			return true;
		} else {
			// no camera on this device
			return false;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Bundle b = null;
		flagBtnCustomer = false;
		flagBtnCnicFront = false;

		if (resultCode == Activity.RESULT_OK)
		{
			if (data != null) {
				b = data.getExtras();
			}

			switch (requestCode) {
			case Constants.REQUEST_GET_DEVICE_CAMERA_FOR_CUSTOMER:
				photoPathCustomer = b.getString("PATH");
				Log.i("AGENTMATE", "##" + photoPathCustomer);
				imageViewCustomer = (ImageView) findViewById(R.id.imageViewCustomer);
				uriCustomer = Uri.parse(photoPathCustomer);
				mBundle.putString("customer", photoPathCustomer);
				imageViewCustomer.setImageResource(R.drawable.default_img);
				imageViewCustomer.setImageBitmap(Utility.getThumbnail(new File(
						uriCustomer.getPath()), 512, 512));

				btnCustomer.setVisibility(View.INVISIBLE);
				btnCustomerRetake.setVisibility(View.VISIBLE);
				btnCustomerConfirm.setVisibility(View.VISIBLE);
				break;

			case Constants.REQUEST_GET_DEVICE_CAMERA_FOR_CNIC_FRONT:
				photoPathNICFront = b.getString("PATH");
				imageViewNICFront = (ImageView) findViewById(R.id.imageViewNICFront);
				uriNICFront = Uri.parse(photoPathNICFront);
				imageViewNICFront.setImageResource(R.drawable.default_img);
				imageViewNICFront.setImageBitmap(Utility.getThumbnail(new File(
						uriNICFront.getPath()), 512, 512));
				mBundle.putString("cnic_front", photoPathNICFront);
				btnNICFront.setVisibility(View.INVISIBLE);
				btnNICFrontRetake.setVisibility(View.VISIBLE);
				btnNICFrontConfirm.setVisibility(View.VISIBLE);
				break;
			}
			super.onActivityResult(requestCode, resultCode, data);
		}
        else{
            if(data!=null && data.getStringExtra("RESULT").equals("ERROR"))
                PopupDialogs.alertDialog("Camera not available on device!",
                        PopupDialogs.Status.ERROR, OpenAccountPicInputUploadActivity.this,
                        new PopupDialogs.OnCustomClickListener() {
                            @Override
                            public void onClick(View v, Object obj) {
                            }
                        });
        }
	}

	private void uploadImage(final String path, final String type){
        new UploadImageTask().execute(path, type);
    }

    class UploadImageTask extends AsyncTask<String, Void, String> {

        HttpURLConnection urlConnection = null;
        protected String type = null;
        protected HttpResponseModel wrapper = new HttpResponseModel();

        protected String doInBackground(String... urls) {

            String path = urls[0];
            type = urls[1];
            String attachmentName = "file";
            String attachmentFileName = path.substring(path.lastIndexOf("/") + 1);
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;

            try {
                AppLogger.i("##Upload Start Time: "+ (new Date()).getTime() + "");

                String uploadUrl = Constants.PROTOCOL + Constants.BASE_URL;
                if (ApplicationData.isCustomIP) {
                    uploadUrl = setCustomIP();
                }

                File sourceFile = new File(path);
                FileInputStream fileInputStream = new FileInputStream(sourceFile);

                URL url = new URL(uploadUrl + "/upload");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setUseCaches(false);
                urlConnection.setRequestMethod("POST");
                urlConnection.setConnectTimeout(Constants.CONNECTION_TIMEOUT * 1000);
                urlConnection.setReadTimeout(Constants.CONNECTION_TIMEOUT * 1000);
                urlConnection.setRequestProperty("Connection", "Keep-Alive");
                urlConnection.setRequestProperty("ENCTYPE", "multipart/form-data");
                urlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                urlConnection.setRequestProperty("uploaded_file", attachmentFileName);

                dos = new DataOutputStream(urlConnection.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"" + attachmentName
                        + "\";filename=\"" + attachmentFileName + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                AppLogger.i("File Upload Response is : " + urlConnection.getResponseMessage()
                        + ": " + urlConnection.getResponseCode());

                fileInputStream.close();
                dos.flush();
                dos.close();

                return convertResponseToString(urlConnection);
            }
            catch (Exception e) {
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(OpenAccountPicInputUploadActivity.this,
                                Constants.Messages.EXCEPTION_SERVICE_UNAVAILABLE, Toast.LENGTH_SHORT).show();
                    }
                });
                return convertResponseToString(urlConnection);
            }
        }

        protected void onPostExecute(String uploadResponse) {
            AppLogger.i("##Upload End Time: "+ (new Date()).getTime() + "");
            wrapper.setXmlResponse(uploadResponse);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    switch (type) {
                        case IMAGE_CUSTOMER:
                            flagCustomer = processResponse(wrapper, type, lblCustomer);
                            flagUploadCustomer = true;
                            if (flagCustomer) {
                                lblCustomer.setText("Customer photo uploaded.");
                                imageViewProgressCustomer.setImageResource(R.drawable.accept);
                            } else {
                                removeByType(type);
                            }
                            progressUploadCustomer.setVisibility(ProgressBar.INVISIBLE);
                            break;

                        case IMAGE_NIC_FRONT:
                            flagCnicFront = processResponse(wrapper, type, lblNICFront);
                            flagUploadCnicFront = true;
                            if (flagCnicFront) {
                                lblNICFront.setText("CNIC front photo uploaded.");
                                imageViewProgressNICFront.setImageResource(R.drawable.accept);
                            }
                            else {
                                removeByType(type);
                            }
                            progressUploadNICFront.setVisibility(ProgressBar.INVISIBLE);
                            break;
                    }
                }
            });
        }
    }

    private String setCustomIP() {

        String url = PreferenceConnector.readString(getApplicationContext(),
                PreferenceConnector.CUSTOM_IP, Constants.PROTOCOL + Constants.BASE_URL);

        url=url.replace("/allpay.me", "");
        return url;
    }

    public String convertResponseToString(HttpURLConnection urlConnection){

        InputStream inputStream = null;
        BufferedReader reader = null;
        String responseText = null;

        try {
            inputStream = urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

            char charBuf[] = new char[20156];
            StringBuffer sb = new StringBuffer();
            int count;
            while ((count = reader.read(charBuf)) != -1) {
                sb.append(new String(charBuf, 0, count));
            }
            responseText = sb.toString();
            AppLogger.i(urlConnection.getURL().getHost() + "\n" + responseText
                    + "\n Status : " + urlConnection.getResponseCode());
            return responseText;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public boolean processResponse(HttpResponseModel response,
                                   String type, TextView txt) {
        try {
            XmlParser xmlParser = new XmlParser();
            Hashtable<?, ?> table = xmlParser.
                    convertXmlToTable(response.getXmlResponse());
            if(table==null){
                return false;
            }
            else if (table != null && table.containsKey("errs")) {
                List<?> list = (List<?>) table.get("errs");
                MessageModel message = (MessageModel) list.get(0);
                String code = message.getCode();
                AppLogger.i("##Error Code: " + code);
                txt.setText(message.getDescr());
                removeByType(type);
                return false;
            }
            else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(OpenAccountPicInputUploadActivity.this,
                    Constants.Messages.INTERNET_CONNECTION_PROBLEM,
                    Toast.LENGTH_LONG).show();
            removeByType(type);
            return false;
        }
    }


    private void removeByType(final String type) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (type) {
                    case IMAGE_CUSTOMER:
                        setLabelText(lblCustomer);
                        setRemoveImage(imageViewProgressCustomer);
                        progressUploadCustomer.setVisibility(View.GONE);
                        flagUploadCustomer = true;
                        break;
                    case IMAGE_NIC_FRONT:
                        setLabelText(lblNICFront);
                        setRemoveImage(imageViewProgressNICFront);
                        progressUploadNICFront.setVisibility(View.GONE);
                        flagUploadCnicFront = true;
                        break;
                }
                mainViewGroup.invalidate();
            }
        });
    }

    private void setLabelText(TextView txtView) {
        txtView.setText("Failed to upload the image.");
    }

    private void setRemoveImage(ImageView imgView) {
        imgView.setImageResource(R.drawable.accept);
    }
}