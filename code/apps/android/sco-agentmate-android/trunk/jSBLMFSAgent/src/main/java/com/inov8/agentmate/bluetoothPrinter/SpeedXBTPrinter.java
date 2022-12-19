package com.inov8.agentmate.bluetoothPrinter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.hoin.btsdk.BluetoothService;
import com.hoin.btsdk.PrintPic;
import com.inov8.agentmate.activities.BaseActivity;
import com.inov8.agentmate.util.ApplicationData;

import java.util.Set;

public class SpeedXBTPrinter {

    private byte[] cmd2;
    private Context context;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothService mService = null;
    private Set<BluetoothDevice> listDevice;
    private BluetoothDevice bluetoothDevice;

    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    switch (msg.arg1) {
                        case 0:
                        case 1:
                        case 2:
                            return;
                        case 3:
                            ApplicationData.currentDevice = bluetoothDevice;
                            ApplicationData.isBluetoothPrinterConnected = true;
                            ((BaseActivity) context).hideLoading();
                            Toast.makeText(context, "Connect successful", 0).show();
                            return;
                        default:
                            return;
                    }
                case 5:
                    ApplicationData.isBluetoothPrinterConnected = false;
                    ((BaseActivity) context).hideLoading();
                    Toast.makeText(context, "Device connection was lost", 0).show();
                    return;
                case 6:
                    ApplicationData.isBluetoothPrinterConnected = false;
                    ((BaseActivity) context).hideLoading();
                    Toast.makeText(context, "Unable to connect device", 0).show();
                    return;
                default:
                    return;
            }
        }
    };

    public SpeedXBTPrinter(Context context) {
        this.context = context;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        cmd2 = new byte[3];
        this.mService = new BluetoothService(context, this.mHandler);
        if (!this.mService.isAvailable()) {
            Toast.makeText(context, "Bluetooth is not available", 1).show();
        }
    }

    public void onResume() {
        if (!this.mService.isBTopen()) {
            enableBluetooth();
        }
    }

    public Set<BluetoothDevice> getPairedDevices() {
        listDevice = mService.getPairedDev();
        return listDevice;
    }

    public void connectDevice(BluetoothDevice device) {
        bluetoothDevice = device;
        mService.connect(device);

    }

    public void printBISPCashOutReceipt(String logoPath, String title, String dateAndTime, String beneficiaryCNIC, String agentId,
                                        String trxAmount, String helplineNumber) {
        printLogo();
        cmd2[0] = 27;
        cmd2[1] = 33;
        cmd2[2] = (byte) (cmd2[2] | 10);
        mService.write(cmd2);
        mService.sendMessage("        Ehsaas Kafalat        \n", "GBK");
        mService.sendMessage("Trx Date & Time:               ", "GBK");
        mService.sendMessage(dateAndTime + "\n", "GBK");
        mService.sendMessage("Beneficiary CNIC               ", "GBK");
        mService.sendMessage(beneficiaryCNIC.substring(0, 9) + "****" + "\n", "GBK");
        mService.sendMessage("Disbursing Agent ID            ", "GBK");
        mService.sendMessage(agentId + "\n", "GBK");
        mService.sendMessage("Trx Amount                     ", "GBK");
        mService.sendMessage(trxAmount + "\n", "GBK");
    }

    public void printBISPFTReceipt(String logoPath, String title, String description, String dateAndTime, String beneficiaryCNIC, String agentId,
                                   String trxAmount, String helplineNumber) {
        printLogo();
        cmd2[0] = 27;
        cmd2[1] = 33;
        cmd2[2] = (byte) (cmd2[2] | 10);
        mService.write(cmd2);
        mService.sendMessage("        Ehsaas Kafalat        \n", "GBK");
        mService.sendMessage(description + "\n", "GBK");
        mService.sendMessage("Trx Date & Time:               ", "GBK");
        mService.sendMessage(dateAndTime + "\n", "GBK");
        mService.sendMessage("Beneficiary CNIC               ", "GBK");
        mService.sendMessage(beneficiaryCNIC.substring(0, 9) + "****" + "\n", "GBK");
        mService.sendMessage("Disbursing Agent ID            ", "GBK");
        mService.sendMessage(agentId + "\n", "GBK");
        mService.sendMessage("Trx Amount                     ", "GBK");
        mService.sendMessage(trxAmount + "\n", "GBK");
        mService.sendMessage("JS Bank helpline number: 080078900", "GBK");
    }

    public void enableBluetooth() {
        if (!mBluetoothAdapter.isEnabled())
            mBluetoothAdapter.enable();
    }

    public void onDestroy() {
        if (this.mService != null) {
            this.mService.stop();
        }
        this.mService = null;
    }

    private void printLogo() {
        PrintPic pg = new PrintPic();
        pg.initCanvas(576);
        pg.initPaint();
        pg.drawImage(105.0f, 0.5f, Environment.getExternalStorageDirectory() + "/kafalat_logo.png");
        byte[] sendData = pg.printDraw();
        this.mService.write(sendData);
    }
}