package com.inov8.agentmate.activities.collection;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.inov8.agentmate.activities.BaseCommunicationActivity;
import com.inov8.agentmate.activities.TransactionReceiptActivity;
import com.inov8.agentmate.model.HttpResponseModel;
import com.inov8.agentmate.model.MessageModel;
import com.inov8.agentmate.model.ProductModel;
import com.inov8.agentmate.model.TransactionInfoModel;
import com.inov8.agentmate.model.TransactionModel;
import com.inov8.agentmate.net.HttpAsyncTask;
import com.inov8.agentmate.parser.XmlParser;
import com.inov8.agentmate.util.AppMessages;
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.ListViewExpanded;
import com.inov8.agentmate.util.MpinInterface;
import com.inov8.agentmate.util.Utility;
import com.inov8.jsblmfs.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class CollectionPaymentConfirmationActivity extends
        BaseCommunicationActivity implements MpinInterface{
	private TextView lblHeading, lblAlert;
	private String strPid, strProductName, strConsumerNo, strMobileNo,
            strStatus, strDueDate, strDueDateF,
            strAmountF, strChargesF, strTotalAmountF;
	private Button btnNext, btnCancel;
	private String labels[], data[];
	private TransactionInfoModel transactionInfo;
    private ProductModel product;
	private byte flowId;
	private boolean btnPressed = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_confirm_funds_transfer);

        fetchIntents();

        lblHeading = (TextView) findViewById(R.id.lblHeading);
        lblHeading.setText("Confirm Transaction");

        lblAlert = (TextView) findViewById(R.id.lblAlert);
        lblAlert.setText(Constants.Messages.VERIFY_CUSTOMER_DETAILS);

        try {
            strPid = transactionInfo.getPid();
            strProductName = transactionInfo.getPname();
            strConsumerNo = transactionInfo.getConsumer();
            strAmountF = transactionInfo.getTxamf();
            strChargesF = transactionInfo.getTpamf();
            strTotalAmountF = transactionInfo.getTamtf();
            strMobileNo = transactionInfo.getCmob();
            strStatus = transactionInfo.getStatus();
            strDueDate = transactionInfo.getDuedate();
            strDueDateF = transactionInfo.getDuedatef();

            labels = new String[]{"Transaction Type", "Challan No.", "Customer Mobile No.",
                    "Status", "Due Date", "Amount", "Charges", "Total Amount"};

            data = new String[]{strProductName, strConsumerNo, strMobileNo,
                    strStatus.equals("0")? "Un-Paid": "Paid",
                    strDueDate, strAmountF + Constants.CURRENCY,
                    strChargesF + Constants.CURRENCY,
                    strTotalAmountF + Constants.CURRENCY};

            List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

            for (int i = 0; i < data.length; i++) {
                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("label", labels[i]);
                hm.put("data", data[i]);
                aList.add(hm);
            }

            String[] from = {"label", "data"};
            int[] to = {R.id.txtLabel, R.id.txtData};
            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList,
                    R.layout.listview_layout_with_data, from, to);

            ListViewExpanded listView = (ListViewExpanded) findViewById(R.id.dataList);
            listView.setAdapter(adapter);

            Utility.getListViewSize(listView, this, mListItemHieght);

            btnNext = (Button) findViewById(R.id.btnNext);
            btnNext.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    btnNext.setEnabled(false);
                    if(!btnPressed) {
                        btnPressed = true;
                        askMpin(null, null,
                                false, CollectionPaymentConfirmationActivity.this);
                    }
                }
            });

            btnCancel = (Button) findViewById(R.id.btnCancel);
            btnCancel.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    Utility.showConfirmationDialog(
                            CollectionPaymentConfirmationActivity.this,
                            AppMessages.CANCEL_TRANSACTION,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    goToMainMenu();
                                }
                            });
                }
            });
            addAutoKeyboardHideFunction();
            headerImplementation();
            addAutoKeyboardHideFunctionScrolling();
        }
        catch (Exception e){
            createAlertDialog(AppMessages.EXCEPTION_INVALID_RESPONSE, AppMessages.ALERT_HEADING);
            e.printStackTrace();
        }
    }

    @Override
    public void onMpinPopupClosed(){
        btnNext.setEnabled(true);
        btnPressed = false;
    }

	public void mpinProcess(final Bundle bundle, final Class<?> nextClass) {
	    btnPressed = false;
        processRequest();
    }

	@Override
	public void processRequest() {
		if (!haveInternet()) {
			Toast.makeText(getApplication(),
					AppMessages.INTERNET_CONNECTION_PROBLEM,
					Toast.LENGTH_SHORT).show();
			return;
		}

		showLoading("Please Wait", "Processing...");

        new HttpAsyncTask(CollectionPaymentConfirmationActivity.this).execute(
                Constants.CMD_COLLECTION_PAYMENT_TRX + "",
                getEncryptedMpin(), strPid, strProductName, strConsumerNo,
                strStatus, strDueDate, strMobileNo, transactionInfo.getTxam(),
                transactionInfo.getTpam(), transactionInfo.getTamt());
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
                createAlertDialog(message.getDescr(), AppMessages.ALERT_HEADING, (dialogInterface, i) -> {
                    goToMainMenu();
                });
			} else {
				if (table.containsKey(Constants.KEY_LIST_TRANS)) {
					@SuppressWarnings("unchecked")
					List<TransactionModel> list = (List<TransactionModel>) table
							.get(Constants.KEY_LIST_TRANS);
					TransactionModel transaction = list.get(0);

					Intent intent = new Intent(getApplicationContext(),
							TransactionReceiptActivity.class);

					intent.putExtra(Constants.IntentKeys.TRANSACTION_MODEL, transaction);
					intent.putExtras(mBundle);
					startActivity(intent);
				}
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

    @Override
    public void headerImplementation() {
        btnHome = (ImageView) findViewById(R.id.imageViewHome);
        btnExit = (Button) findViewById(R.id.buttonsignout);

        if (!ApplicationData.isLogin) {
            btnExit.setVisibility(View.GONE);
        }
        btnHome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                ApplicationData.webUrl = null;
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

    private void fetchIntents() {
        try {
            product = (ProductModel) mBundle.get(Constants.IntentKeys.PRODUCT_MODEL);
            flowId = mBundle.getByte(Constants.IntentKeys.FLOW_ID);
            transactionInfo = (TransactionInfoModel) mBundle
                    .get(Constants.IntentKeys.TRANSACTION_INFO_MODEL);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}