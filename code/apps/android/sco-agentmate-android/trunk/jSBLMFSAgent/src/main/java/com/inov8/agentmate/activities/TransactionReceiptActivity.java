package com.inov8.agentmate.activities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.inov8.agentmate.activities.fundsTransfer.ReceiveCashConfirmationActivity;
import com.inov8.agentmate.activities.openAccount.OpenAccountBvsActivity;

import com.inov8.agentmate.bluetoothPrinter.SpeedXBTPrinter;

import com.inov8.agentmate.model.ProductModel;
import com.inov8.agentmate.model.TransactionModel;
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.ListViewExpanded;
import com.inov8.agentmate.util.PopupDialogs;
import com.inov8.agentmate.util.Utility;
import com.inov8.jsbl.sco.R;

public class TransactionReceiptActivity extends BaseActivity {
    private String totalAmount, bvsFlag;
    private Byte flowId;
    private TextView txtAmount, txtPaidLabel;
    private String[] labels, data;
    private TransactionModel transaction;
    private ProductModel product;
    TextView txtViewAuthMessage;
    private boolean doShowRegistrationPopup = false;
    private byte paymentType;
    private Button btnPrint;
    private String is3rdPartyFT;
    private SpeedXBTPrinter speedXBTPrinter;

    Set<BluetoothDevice> devices;
    Map<String, BluetoothDevice> devicesList;
    String[] devicesAddress;
    int i;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_transaction_receipt);

        try {
            fetchIntents();

            txtPaidLabel = (TextView) findViewById(R.id.txtPaidlabel);
            txtAmount = (TextView) findViewById(R.id.txtAmount);
            txtAmount.setText(Utility.getFormattedCurrency(Utility.getFormatedAmount(totalAmount)));
            txtViewAuthMessage = (TextView) findViewById(R.id.textviewauthenticate);
            btnPrint = findViewById(R.id.btnPrint);

            ListViewExpanded listView = (ListViewExpanded) findViewById(R.id.dataList);

            if (flowId == Constants.FLOW_ID_3RD_PARTY_CASH_OUT) {
                btnPrint.setVisibility(View.VISIBLE);
                speedXBTPrinter = new SpeedXBTPrinter(this);
                connectToSpeedX();
                storeLogoToStorage();
            }

            switch (flowId) {
                case Constants.FLOW_ID_CASH_IN:
                    txtPaidLabel.setText("Cash In Successful");
                    labels = new String[]{"Customer Mobile No.", "Customer NIC",
                            "Transaction ID", "Amount", "Charges", "Date & Time"};
                    data = new String[]{transaction.getCmob(),
                            transaction.swcnic, transaction.getId(),
                            transaction.getTxamf() + Constants.CURRENCY,
                            transaction.getTpamf() + Constants.CURRENCY,
                            transaction.getDatef() + " " + transaction.getTimef()};
                    break;
                case Constants.FLOW_ID_CASH_OUT_BY_IVR:
                case Constants.FLOW_ID_HRA_CASH_WITH_DRAWAL:

                    if (product.getId().equals(Constants.PRODUCT_ID_CASH_WITH_DRAWAL)) {
                        txtPaidLabel.setText("HRA Cash withdrawal");
                    } else {
                        txtPaidLabel.setText("Cash Out Successful");
                    }


                    labels = new String[]{"Customer Mobile No.", "Customer NIC",
                            "Transaction ID", "Amount", "Charges", "Date & Time"};
                    data = new String[]{transaction.getCmob(),
                            transaction.swcnic, transaction.getId(),
                            transaction.getTxamf() + Constants.CURRENCY,
                            transaction.getTpamf() + Constants.CURRENCY,
                            transaction.getDatef() + " " + transaction.getTimef()};
                    break;

                case Constants.FLOW_ID_CASH_OUT_BY_TRX_ID:
                    txtPaidLabel.setText("Cash Out Successful");
                    labels = new String[]{"Customer Mobile No.", "Customer NIC",
                            "Customer Title", "Transaction ID", "Amount", "Charges", "Date & Time"};
                    data = new String[]{transaction.getCmob(),
                            transaction.swcnic, transaction.getActitle(), transaction.getId(),
                            transaction.getTxamf() + Constants.CURRENCY,
                            transaction.getTpamf() + Constants.CURRENCY,
                            transaction.getDatef() + " " + transaction.getTimef()};
                    break;

                case Constants.FLOW_ID_IBFT_AGENT:
                    txtPaidLabel.setText("Agent IBFT Successful");
                    labels = new String[]{"Sender Mobile No.", "Sender Account Title", "Receiver Account No.",
                            "Receiver Account Title", "Bank Name", "Transaction ID", "Amount", "Charges", "Date & Time"};
                    data = new String[]{transaction.getAmob(),
                            transaction.getBbacid(),
                            transaction.getCoreacno(),
                            transaction.getCoreacid(),
                            transaction.getBankName(),
                            transaction.getId(),
                            transaction.getTxamf() + Constants.CURRENCY,
                            transaction.getTpamf() + Constants.CURRENCY,
                            transaction.getDatef() + " " + transaction.getTimef()};
                    break;
                case Constants.FLOW_ID_3RD_PARTY_CASH_OUT:
                    txtPaidLabel.setText("3rd Party Cash Out Successful");
                    if (product != null && product.getId() != null) {
                        if (product.getId().equals(Constants.PRODUCT_ID_EOBI_CASH_OUT)) {

                            labels = new String[]{"Mobile Number", "Principle Amount",
                                    "Transaction ID", "Charges", "Date & Time"};
                            data = new String[]{transaction.getCmob(), transaction.getTxamf() + Constants.CURRENCY,
                                    transaction.getId(), transaction.getTpamf() + Constants.CURRENCY,
                                    transaction.getDatef() + " " + transaction.getTimef()};
                        } else {
                            labels = new String[]{"Customer Mobile No.", "Customer NIC",
                                    "Customer Title", "Transaction ID", "Amount", "Charges", "Date & Time"};
                            data = new String[]{transaction.getCmob(),
                                    transaction.getSwcnic(), transaction.getActitle(), transaction.getId(),
                                    transaction.getTxamf() + Constants.CURRENCY,
                                    transaction.getTpamf() + Constants.CURRENCY,
                                    transaction.getDatef() + " " + transaction.getTimef()};
                        }
                    } else {
                        labels = new String[]{"Customer Mobile No.", "Customer NIC",
                                "Customer Title", "Transaction ID", "Amount", "Charges", "Date & Time"};
                        data = new String[]{transaction.getCmob(),
                                transaction.getSwcnic(), transaction.getActitle(), transaction.getId(),
                                transaction.getTxamf() + Constants.CURRENCY,
                                transaction.getTpamf() + Constants.CURRENCY,
                                transaction.getDatef() + " " + transaction.getTimef()};
                    }

                    break;

                case Constants.FLOW_ID_AGENT_TO_AGENT:
                    txtPaidLabel.setText("Agent Transfer Successful");
                    labels = new String[]{"Receiver Mobile No.", "Receiver NIC",
                            "Transaction ID", "Amount", "Charges", "Date & Time"};
                    data = new String[]{transaction.getRamob(),
                            transaction.getRacnic(), transaction.getId(),
                            transaction.getTxamf() + Constants.CURRENCY,
                            transaction.getTpamf() + Constants.CURRENCY,
                            transaction.getDatef() + " " + transaction.getTimef()};
                    break;

                case Constants.FLOW_ID_TRANSFER_IN:
                    txtPaidLabel.setText("Transfer In Successful");
                    labels = new String[]{"Agent Mobile No.", "To Account",
                            "From Account", "Amount", "Charges", "Transaction ID",
                            "Date & Time"};
                    data = new String[]{transaction.getAmob(),
                            transaction.getBbacid(), transaction.getCoreacid(),
                            transaction.getTxamf() + Constants.CURRENCY,
                            transaction.getTpamf() + Constants.CURRENCY,
                            transaction.getId(), transaction.getDatef()};
                    break;

                case Constants.FLOW_ID_TRANSFER_OUT:
                    txtPaidLabel.setText("Transfer Out Successful");
                    labels = new String[]{"Agent Mobile No.", "To Account No.",
                            "To Account Title", "From Account", "Transaction ID",
                            "Amount", "Charges", "Date & Time"};
                    data = new String[]{transaction.getAmob(),
                            transaction.getCoreacid(), transaction.getCoreactl(),
                            transaction.getBbacid(), transaction.getId(),
                            transaction.getTxamf() + Constants.CURRENCY,
                            transaction.getTpamf() + Constants.CURRENCY,
                            transaction.getDatef() + " " + transaction.getTimef()};
                    break;

                case Constants.FLOW_ID_FT_BLB_TO_BLB:
                    txtPaidLabel.setText("Money Transfer Successful");

                    labels = new String[]{"Sender Mobile No.",
                            "Receiver Mobile No.", "Transaction ID", "Amount",
                            "Charges", "Date & Time"};
                    data = new String[]{transaction.getCmob(),
                            transaction.getRcmob(), transaction.getId(),
                            transaction.getTxamf() + Constants.CURRENCY,
                            transaction.getTpamf() + Constants.CURRENCY,
                            transaction.getDatef() + " " + transaction.getTimef()};
                    break;

                case Constants.FLOW_ID_FT_BLB_TO_CNIC:
                    txtPaidLabel.setText("Money Transfer Successful");
                    labels = new String[]{"Sender Mobile No.", "Receiver CNIC",
                            "Receiver Mobile No.", "Transaction ID", "Amount",
                            "Charges", "Date & Time"};
                    data = new String[]{transaction.getCmob(),
                            transaction.getRwcnic(), transaction.getRwmob(),
                            transaction.getId(),
                            transaction.getTxamf() + Constants.CURRENCY,
                            transaction.getTpamf() + Constants.CURRENCY,
                            transaction.getDatef() + " " + transaction.getTimef()};
                    break;

                case Constants.FLOW_ID_FT_CNIC_TO_BLB:
                    txtPaidLabel.setText("Money Transfer Successful");
                    labels = new String[]{"Sender CNIC", "Sender Mobile No.",
                            "Receiver Mobile No.", "Transaction ID", "Amount",
                            "Charges", "Date & Time"};
                    data = new String[]{transaction.getSwcnic(),
                            transaction.getSwmob(), transaction.getRcmob(),
                            transaction.getId(),
                            transaction.getTxamf() + Constants.CURRENCY,
                            transaction.getTpamf() + Constants.CURRENCY,
                            transaction.getDatef() + " " + transaction.getTimef()};
                    break;

                case Constants.FLOW_ID_FT_CNIC_TO_CNIC:
                    txtPaidLabel.setText("Money Transfer Successful");
                    labels = new String[]{"Sender CNIC", "Sender Mobile No.",
                            "Receiver CNIC", "Receiver Mobile No.",
                            "Transaction ID", "Amount", "Charges", "Date & Time"};
                    data = new String[]{transaction.getSwcnic(),
                            transaction.getSwmob(), transaction.getRwcnic(),
                            transaction.getRwmob(), transaction.getId(),
                            transaction.getTxamf() + Constants.CURRENCY,
                            transaction.getTpamf() + Constants.CURRENCY,
                            transaction.getDatef() + " " + transaction.getTimef()};
                    break;

                case Constants.FLOW_ID_FT_BLB_TO_CORE_AC:
                    txtPaidLabel.setText("Money Transfer Successful");
                    labels = new String[]{"Sender Mobile No.", "Account Number",
                            "Account Title", "Transaction ID", "Amount", "Charges",
                            "Date & Time"};
                    data = new String[]{transaction.getCmob(),
                            transaction.getCoreacid(), transaction.getCoreactl(),
                            transaction.getId(),
                            transaction.getTxamf() + Constants.CURRENCY,
                            transaction.getTpamf() + Constants.CURRENCY,
                            transaction.getDatef() + " " + transaction.getTimef()};
                    break;

                case Constants.FLOW_ID_FT_CNIC_TO_CORE_AC:
                    txtPaidLabel.setText("Money Transfer Successful");
                    labels = new String[]{"Sender CNIC", "Sender Mobile No.",
                            "Account Number", "Account Title", "Transaction ID",
                            "Amount", "Charges", "Date & Time"};
                    data = new String[]{transaction.getSwcnic(),
                            transaction.getSwmob(), transaction.getCoreacid(),
                            transaction.getCoreactl(), transaction.getId(),
                            transaction.getTxamf() + Constants.CURRENCY,
                            transaction.getTpamf() + Constants.CURRENCY,
                            transaction.getDatef() + " " + transaction.getTimef()};
                    break;

                case Constants.FLOW_ID_COLLECTION_PAYMENT:
                    txtPaidLabel.setText(transaction.getProductName() + " Successful");
                    labels = new String[]{"Customer Mobile No.", "Challan No.", "Transaction ID", "Amount",
                            "Charges", "Total Amount", "Date & Time"};
                    data = new String[]{transaction.getCmob(), transaction.getConsumer(),
                            transaction.getId(),
                            transaction.getTxamf() + Constants.CURRENCY,
                            transaction.getCamtf() + Constants.CURRENCY,
                            transaction.getTamtf() + Constants.CURRENCY,
                            transaction.getDatef()};
                    break;

                case Constants.FLOW_ID_BILL_PAYMENT_GAS:
                case Constants.FLOW_ID_BILL_PAYMENT_WATER_ELECTRICITY:
                case Constants.FLOW_ID_BILL_PAYMENT_MOBILE_POSTPAID:
                case Constants.FLOW_ID_BILL_PAYMENT_MOBILE_PREPAID:
                case Constants.FLOW_ID_BILL_PAYMENT_TELEPHONE:
                    txtPaidLabel.setText("Bill Payment Successful");

                    labels = new String[]{"Product Name", product.getLabel(),
                            "Customer Mobile No.", "Transaction ID", "Date & Time"};
                    data = new String[]{transaction.getProductName(),
                            transaction.getConsumer(), transaction.getCmob(),
                            transaction.getId(),
                            transaction.getDatef() + " " + transaction.getTimef()};
                    break;

                case Constants.FLOW_ID_RETAIL_PAYMENT:
                    txtPaidLabel.setText("Retail Payment Successful");
                    labels = new String[]{"Customer Mobile No.",
                            "Transaction ID", "Amount", "Charges", "Date & Time"};
                    data = new String[]{transaction.getCmob(),
                            transaction.getId(),
                            transaction.getTxamf() + Constants.CURRENCY,
                            transaction.getTpamf() + Constants.CURRENCY,
                            transaction.getDatef() + " " + transaction.getTimef()};
                    break;

                case Constants.FLOW_ID_BALANCE_INQUIRY:
                    txtPaidLabel.setText("Current Account Balance");
                    labels = new String[]{"Current Account Balance",
                            "Date & Time"};
                    data = new String[]{
                            totalAmount + Constants.CURRENCY,
                            ApplicationData.getFormattedDate() + " "
                                    + ApplicationData.getFormattedTime()};
                    break;

                case Constants.FLOW_ID_RM_SENDER_REDEEM:
                case Constants.FLOW_ID_RM_RECEIVE_CASH:
                    txtPaidLabel.setText("Receive Money Successful");
                    String productId = mBundle
                            .getString(Constants.IntentKeys.PRODUCT_ID);
                    if (productId.equals(Constants.ProductIds.BULK2CNIC + "")) {
                        labels = new String[]{"Receiver CNIC",
                                "Receiver Mobile No.", "Transaction ID",
                                "Date & Time"};
                        data = new String[]{
                                transaction.getRwcnic(),
                                transaction.getRwmob(),
                                transaction.getId(),
                                transaction.getDatef() + " "
                                        + transaction.getTimef()};
                    } else {
                        labels = new String[]{"Sender CNIC", "Sender Mobile No.",
                                "Receiver CNIC", "Receiver Mobile No.",
                                "Transaction ID", "Date & Time"};
                        data = new String[]{
                                transaction.getSwcnic(),
                                transaction.getSwmob(),
                                transaction.getRwcnic(),
                                transaction.getRwmob(),
                                transaction.getId(),
                                transaction.getDatef() + " "
                                        + transaction.getTimef()};
                    }
                    break;

                case Constants.FLOW_ID_RM_RECEIVE_CASH_NOT_REGISTERED:
                    txtPaidLabel.setText("Receive Money Successful");
                    productId = mBundle
                            .getString(Constants.IntentKeys.PRODUCT_ID);
                    if (productId.equals(Constants.ProductIds.BULK2CNIC + "")) {
                        labels = new String[]{"Receiver CNIC", "Receiver Mobile No.",
                                "Transaction ID", "Amount", "Date & Time"};
                        data = new String[]{transaction.getRwcnic(),
                                transaction.getRwmob(), transaction.getId(),
                                transaction.getTxamf() + Constants.CURRENCY,
                                transaction.getDatef() + " " + transaction.getTimef()};
                    } else {
                        labels = new String[]{"Sender CNIC", "Sender Mobile No.",
                                "Receiver CNIC", "Receiver Mobile No.",
                                "Transaction ID", "Amount", "Date & Time"};
                        data = new String[]{transaction.getSwcnic(),
                                transaction.getSwmob(), transaction.getRwcnic(),
                                transaction.getRwmob(), transaction.getId(),
                                transaction.getTxamf() + Constants.CURRENCY,
                                transaction.getDatef() + " " + transaction.getTimef()};
                    }
                    break;
            }

            updateBalance();

            populateReceipt(listView, labels, data);

            btnPrint.setOnClickListener(v -> {
                if (ApplicationData.isBluetoothPrinterConnected) {

                    if (flowId == Constants.FLOW_ID_3RD_PARTY_CASH_OUT && product.getId().equals(Constants.PRODUCT_ID_3RD_PARTY_CASH_OUT)) {
                        if (is3rdPartyFT.equals("1")) {
                            speedXBTPrinter.printBISPFTReceipt("", "Ehsaas Kafalat", "Ehsaas Kafalat payment has been successfully deposited in your  Bank Alfalah Wallet Account.",
                                    transaction.getDatef() + " " + transaction.getTimef(), transaction.getSwcnic(), ApplicationData.userId, transaction.getTxamf() + Constants.CURRENCY, "");
                        } else {
                            speedXBTPrinter.printBISPCashOutReceipt("", "Ehsaas Kafalat",
                                    transaction.getDatef() + " " + transaction.getTimef(), transaction.getSwcnic(), ApplicationData.userId, transaction.getTxamf() + Constants.CURRENCY, "");
                        }
                    }
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("Connect",
                                    (dialog, id) ->
                                            connectToSpeedX()).setMessage("You are not connected to bluetooth device");
                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();
                }
            });

            Button btnOK = (Button) findViewById(R.id.btnOK);
            btnOK.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//					if (doShowRegistrationPopup) {
//						showRegistrationPopup();
//					}
//					else {
                    goToMainMenu();
                    //	}
                }
            });

            ScrollView scView = (ScrollView) findViewById(R.id.scView);
            scView.scrollTo(0, 0);
            scView.smoothScrollTo(0, 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        headerImplementation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (speedXBTPrinter!=null)
        speedXBTPrinter.onResume();
    }

    private void fetchIntents() {
        transaction = (TransactionModel) mBundle
                .get(Constants.IntentKeys.TRANSACTION_MODEL);
        product = (ProductModel) mBundle
                .get(Constants.IntentKeys.PRODUCT_MODEL);
        flowId = mBundle.getByte(Constants.IntentKeys.FLOW_ID);
        doShowRegistrationPopup = mBundle
                .getBoolean(Constants.IntentKeys.SHOW_REGISTRATION_POPUP);
        paymentType = mBundle.getByte(Constants.IntentKeys.PAYMENT_TYPE);
        bvsFlag = mBundle.getString(Constants.IntentKeys.BVS_FLAG);
        is3rdPartyFT = (String) mBundle.get(Constants.IntentKeys.IS_CASH_WITHDRAW);

        if (transaction != null) {
            totalAmount = transaction.getTamtf();
        } else {
            totalAmount = "";
        }
    }

    @Override
    public void onBackPressed() {
//		if (doShowRegistrationPopup) {
//			showRegistrationPopup();
//		}
//		else {
        goToMainMenu();
        //	}
    }

    private void showRegistrationPopup() {
        showRegistrationDialog(
                Constants.Messages.ALERT_HEADING,
                Constants.Messages.REGISTERATION_REQUIRED
                        + transaction.getSwmob() + ".", new OnClickListener() { // Continue
                    @Override
                    public void onClick(View arg0) {
                        goToMainMenu();
                    }
                }, new OnClickListener() { // Register
                    @Override
                    public void onClick(View arg0) {
                        switch (flowId) {
                            case Constants.FLOW_ID_BILL_PAYMENT_GAS:
                            case Constants.FLOW_ID_BILL_PAYMENT_WATER_ELECTRICITY:
                            case Constants.FLOW_ID_BILL_PAYMENT_MOBILE_POSTPAID:
                            case Constants.FLOW_ID_BILL_PAYMENT_MOBILE_PREPAID:
                            case Constants.FLOW_ID_BILL_PAYMENT_TELEPHONE:
                                switch (paymentType) {
                                    case Constants.PAYMENT_TYPE_ACCOUNT:
                                        break;
                                    case Constants.PAYMENT_TYPE_CASH:
                                        gotoAccountOpening(transaction.getSwmob());
                                        break;
                                }
                                break;
                            case Constants.FLOW_ID_FT_CNIC_TO_BLB:
                            case Constants.FLOW_ID_FT_CNIC_TO_CNIC:
                            case Constants.FLOW_ID_FT_CNIC_TO_CORE_AC:
                                if (transaction.getSwcnic() != null)
                                    gotoAccountOpening(transaction.getSwmob(),
                                            transaction.getSwcnic());
                                break;
                            default:
                                gotoAccountOpening();
                                break;
                        }
                    }
                });
    }

    public void headerImplementation() {
        btnHome = (ImageView) findViewById(R.id.imageViewHome);
        btnExit = (Button) findViewById(R.id.buttonsignout);

        btnHome.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//				if (doShowRegistrationPopup) {
//					showRegistrationPopup();
//				}
//				else {
                hideKeyboard(v);
                goToMainMenu();
                //	}
            }
        });
        btnExit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showConfirmExitDialog(null);
            }
        });
    }

    private void updateBalance() {

        switch (flowId) {
            case Constants.FLOW_ID_CASH_OUT_BY_IVR:
            case Constants.FLOW_ID_BILL_PAYMENT_GAS:
            case Constants.FLOW_ID_BILL_PAYMENT_WATER_ELECTRICITY:
            case Constants.FLOW_ID_BILL_PAYMENT_MOBILE_POSTPAID:
            case Constants.FLOW_ID_BILL_PAYMENT_MOBILE_PREPAID:
            case Constants.FLOW_ID_BILL_PAYMENT_TELEPHONE:
                switch (paymentType) {
                    case Constants.PAYMENT_TYPE_ACCOUNT:
                        txtViewAuthMessage.setVisibility(View.VISIBLE);
                        break;
                    case Constants.PAYMENT_TYPE_CASH:
                        ApplicationData.formattedBalance = transaction.getBalf();
                        break;
                }
                break;
            case Constants.FLOW_ID_RETAIL_PAYMENT:
            case Constants.FLOW_ID_FT_BLB_TO_BLB:
            case Constants.FLOW_ID_FT_BLB_TO_CNIC:
            case Constants.FLOW_ID_FT_BLB_TO_CORE_AC:
                txtViewAuthMessage.setVisibility(View.VISIBLE);
                break;
            default:
                if (transaction.getBalf() != null)
                    ApplicationData.formattedBalance = transaction.getBalf();
                break;
        }
    }

    private void populateReceipt(ListViewExpanded listView, String[] labels,
                                 String[] data) {

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

        listView.setAdapter(adapter);
        Utility.getListViewSize(listView, this, mListItemHieght);
    }

    public void connectToSpeedX() {
        if (ApplicationData.currentDevice == null) {
            devices = speedXBTPrinter.getPairedDevices();
            devicesList = new HashMap<>();
            devicesAddress = new String[devices.size()];
            i = 0;
            for (BluetoothDevice device : devices) {
                devicesList.put(device.getName(), device);
                devicesAddress[i] = device.getName();
                i++;
            }

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select your device");
            builder.setSingleChoiceItems(devicesAddress, -1, (dialog, which) -> {
                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                speedXBTPrinter.connectDevice(devicesList.get(devicesAddress[selectedPosition]));
                dialog.dismiss();
                showLoading("Please Wait", "Connecting to bluetooth device.");
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            showLoading("Please Wait", "Connecting to bluetooth device.");
            speedXBTPrinter.connectDevice(ApplicationData.currentDevice);

        }
    }

    public void storeLogoToStorage() {
        try {
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.kafalat_logo);
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File file = new File(extStorageDirectory, "kafalat_logo.PNG");
            FileOutputStream outStream = null;

            outStream = new FileOutputStream(file);

            bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        } catch (
                IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        if (speedXBTPrinter!=null)
        speedXBTPrinter.onDestroy();
        super.onDestroy();
    }
}