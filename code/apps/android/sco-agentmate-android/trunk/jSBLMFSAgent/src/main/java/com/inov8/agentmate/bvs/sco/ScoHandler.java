package com.inov8.agentmate.bvs.sco;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cognaxon.WSQlib;
import com.inov8.agentmate.bvs.BvsController;
import com.inov8.agentmate.util.AppLogger;
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.PopupDialogs;
import com.machinezoo.sourceafis.FingerprintMatcher;
import com.machinezoo.sourceafis.FingerprintTemplate;
import com.seamfix.calculatenfiq.NFIQUtil;
import com.yn020.yn020host.HostAPI;

import org.jnbis.WSQDecoder;
import org.jnbis.WSQEncoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ScoHandler extends BvsController {

    Runnable FreeUpImageThead;
    HostAPI YN020Host = new HostAPI();
    HostAPI YN020host = new HostAPI();
    HostAPI tty = new HostAPI();
    String ttyMt;
    private Bitmap bm;
    short Fpid_1;
    byte EnrollStep = 0;
    Handler EnrollHandle = new Handler();
    byte[] AllDevUID = new byte[100];
    final byte Low = 0;
    Runnable EnrollThead;
    HostAPI YN020Hostimage = new HostAPI();
    Handler FreeUpImagehandle = new Handler();
    byte[] bmp_buff = new byte[242*266+(242%4)*266+1078];
    boolean IsShow = true,get_bmp = false;
    String fingerprintISO="";
    public static int minutiaecount = 0;
    private int fingerprintquality = 0;
    boolean firsttime=false;
    short Empty_id = 1;

    public ScoHandler(Activity activity){
        this.activity = activity;
        this.bvsListner = (BvsCompleteListner)activity;
    }

    @Override
    protected void onCreate() {

        Log.i("test","Called onCreate()");

        if (ApplicationData.bvsdeviceModel.equals("Aprix_Phat6")){
             ttyMt = tty.ttyMT3;
        } else {
            ttyMt = tty.ttyMT1;

        }
        FreeUPImageThreadHandler();
        EnrollThreadHandler();
        connectdevice();
        LEDControl();
    }

    @Override
    protected void onStart() {
        Log.i("test","Called onStart()");
    }

    @Override
    protected void onResume(){
        Log.i("test","Called onResume()");
        if(firsttime){
            SharedPreferences sharedPreferences= activity.getSharedPreferences("param", Activity.MODE_PRIVATE);
            int sel_ttys =sharedPreferences.getInt("ttys", 2);
            int sel_baud =sharedPreferences.getInt("baud", 2);
            int sel_connettype =sharedPreferences.getInt("type", 0);

            YN020Host.Communication_Type = (byte) sel_connettype;
            System.out.println("Communication_Type"+YN020Host.Communication_Type);
            byte tty =(byte) sel_ttys;
            YN020Host.dev = ttyMt;
            byte buadid = (byte) sel_baud;
            YN020Host.baud_rate = YN020Host.baud_rate_38400;

            if(YN020Host.Init_Device() != 0){
                Toast.makeText(activity, "Connect device fail...", Toast.LENGTH_SHORT).show();
            }
            else {
                byte[] Temp_UID = new byte[100];
                YN020Host.GetDevMessage(YN020Host.GetDevSgNum, Temp_UID);
                for(int i=0;i<YN020Host.GetDevSgNum[0];i++){
                }
            }

            SharedPreferences.Editor editor = activity.getSharedPreferences("param", Activity.MODE_PRIVATE).edit();
            editor.putInt("baudrate", YN020Host.baud_rate);
            editor.putString("devname", YN020Host.dev);
            editor.putInt("ttys", tty);
            editor.putInt("baud", buadid);
            editor.putInt("type", (int)sel_connettype);
            editor.commit();

            char rets  = (char) YN020host.GetDevMessage(YN020host.GetDevSgNum, AllDevUID);

            if(rets == 0){
                Sled_Contronl((byte) 1,Low);
                System.arraycopy(AllDevUID, 0,YN020host.Cur_UID , 0, 8);
//                for(int i=0;i<YN020host.GetDevSgNum[0];i++){
//                    ShowHex(AllDevUID, 8,8*i);
//                }
            }
        }
        firsttime=true;
    }

    @Override
    protected void scanImage() {

        bvsListner.setButtonEnabled(false);

        Fpid_1 = (short) Get_FpNo();
        if(Enroll_Fp(Fpid_1, (byte) 0, (byte) 3)!=0){
        }
        else{
            bvsListner.setInfoMessage("Please input your finger");
            EnrollStep = 1;
            EnrollHandle.post(EnrollThead);
        }

        short ret =  SetBaudrate(YN020Hostimage.baud_rate_921600);
        SharedPreferences sharedPreferences= activity.getSharedPreferences("param", Activity.MODE_PRIVATE);
        String devname =sharedPreferences.getString("devname", (new StringBuilder()).append("[/dev/").append(ttyMt).append("]").toString());

        if(ret == 0){
            YN020Hostimage.Communication_Type = YN020Hostimage.UART_COMMUNICATION;
            YN020Hostimage.dev = devname;
            YN020Hostimage.baud_rate = YN020Hostimage.baud_rate_921600;
            int imageret = YN020Hostimage.Init_Device();
            if( imageret == 0){
                FreeUpImagehandle.post(FreeUpImageThead);
            }
            else Toast.makeText(activity, "Connect device fail...ret="+imageret, Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(activity, "Set high speed mode failure,please Reconnect the devices", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause(){
        Log.i("test","Called onPause()");
    }

    @Override
    protected void onStop() {
        Log.i("test","Called onStop()");

        try{

        }catch(Exception r){

        }
        try{
            FreeUpImagehandle.post(FreeUpImageThead);
            FreeUpImagehandle.removeCallbacks(FreeUpImageThead);
            SharedPreferences sharedPreferences= activity.getSharedPreferences("param", Activity.MODE_PRIVATE);
            int baudrate =sharedPreferences.getInt("baudrate", 9);
            String devname =sharedPreferences.getString("devname", (new StringBuilder()).append("[/dev/").append(ttyMt).append("]").toString());
            YN020Hostimage.Communication_Type = YN020Hostimage.UART_COMMUNICATION;
            YN020Hostimage.dev = devname;
            YN020Hostimage.baud_rate = baudrate;
            if(SetBaudrate(baudrate) == 0){
                if(YN020Hostimage.Init_Device() == 0){
                }
                else Toast.makeText(activity, "Set low speed mode failure,please Reconnect  the devices", Toast.LENGTH_LONG).show();
                Log.e("","");
            }
            else Toast.makeText(activity, "Set low speed mode failure,please Reconnect  the devices", Toast.LENGTH_LONG).show();
        }
        catch(Exception y)
        {
        }
        YN020Host.Stop_Device();
    }

    @Override
    protected void onDestroy(){
        Log.i("test","Called onDestroy()");

        try{
        }catch(Exception r){}
        try{
            FreeUpImagehandle.post(FreeUpImageThead);
            FreeUpImagehandle.removeCallbacks(FreeUpImageThead);
            SharedPreferences sharedPreferences= activity.getSharedPreferences("param", Activity.MODE_PRIVATE);
            int baudrate =sharedPreferences.getInt("baudrate", 9);
            String devname =sharedPreferences.getString("devname", (new StringBuilder()).append("[/dev/").append(ttyMt).append("]").toString());
            YN020Hostimage.Communication_Type = YN020Hostimage.UART_COMMUNICATION;
            YN020Hostimage.dev = devname;
            YN020Hostimage.baud_rate = baudrate;
            if(SetBaudrate(baudrate) == 0){
                if(YN020Hostimage.Init_Device() == 0){
                }
                else Toast.makeText(activity, "Set low speed mode failure,please Reconnect  the devices", Toast.LENGTH_LONG).show();
                Log.e("","");
            }
            else Toast.makeText(activity, "Set low speed mode failure,please Reconnect  the devices", Toast.LENGTH_LONG).show();
        }
        catch(Exception y)
        {

        }
        YN020Host.Stop_Device();
    }

    @Override
    protected void onBackPressed() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    public void FreeUPImageThreadHandler()
    {
        FreeUpImageThead = new Runnable() {
            public void run() {
                byte ret = GetFpImage();
                if(ret == 0){
                    UpFpImage();
                    FreeUpImagehandle.postDelayed(this, 1000);
                }
                else
                {
                    FreeUpImagehandle.postDelayed(this, 400);
                }
            }
        };
    }

    public void EnrollThreadHandler()
    {
        EnrollThead = new Runnable() {
            byte ret ;
            int gg;
            public void run() {
                gg = 1;
                ret = GetFpImage();
                if(ret == 0){
                    if(EnrollStep == 1){
                        gg = Enroll_Fp(Fpid_1, (byte) 1, (byte) 3);
                        if(gg == 0){
                            EnrollStep = 2;
                            //Toast.makeText(getApplicationContext(), "two more.", Toast.LENGTH_SHORT).show();
                            EnrollStep = 5;
                           // Toast.makeText(activity, "FingerPrint Taken.", Toast.LENGTH_SHORT).show();
                            GetTemplate();

                            if(!fingerprintISO.equals("")) {
                             //  bvsListner.setButtonEnabled(true);
                             //  bvsListner.changeLayoutAfterFirstScan();
                             //  bvsListner.setInfoMessage("Fingerprint scanned successfully");
                            }

                            short ret =  SetBaudrate(YN020Hostimage.baud_rate_921600);
                            SharedPreferences sharedPreferences= activity.getSharedPreferences("param", Activity.MODE_PRIVATE);
                            String devname =sharedPreferences.getString("devname", (new StringBuilder()).append("[/dev/").append(ttyMt).append("]").toString());
                            if(ret == 0){
                                YN020Hostimage.Communication_Type = YN020Hostimage.UART_COMMUNICATION;
                                YN020Hostimage.dev = devname;
                                YN020Hostimage.baud_rate = YN020Hostimage.baud_rate_921600;
                                int imageret = YN020Hostimage.Init_Device();
                                if( imageret == 0){
                                    FreeUpImagehandle.post(FreeUpImageThead);
                                }
                            }
                            else {
                                Toast.makeText(activity, "Set high speed mode failure,please Reconnect  the devices", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                            Toast.makeText(activity, "please input your finger again.", Toast.LENGTH_SHORT).show();
                    }
                }
                EnrollHandle.postDelayed(this, 80);
                if(EnrollStep == 5)
                {
                    EnrollHandle.removeCallbacks(EnrollThead);
                }
            }
        };
    }

    ///////////////////////////////////////////////SDK Functions////////////////////////////////////////////////////////////////////

    public void connectdevice()
    {
        SharedPreferences sharedPreferences= activity.getSharedPreferences("param", Activity.MODE_PRIVATE);
        int sel_ttys =sharedPreferences.getInt("ttys", 2);
        int sel_baud =sharedPreferences.getInt("baud", 2);
        int sel_connettype =sharedPreferences.getInt("type", 0);
        YN020Host.Communication_Type = (byte) sel_connettype;
        System.out.println("Communication_Type"+YN020Host.Communication_Type);
        byte tty =(byte) sel_ttys;
        YN020Host.dev = ttyMt;
        byte buadid = (byte) sel_baud;
        YN020Host.baud_rate = YN020Host.baud_rate_38400;
        if(YN020Host.Init_Device() != 0){
            Toast.makeText(activity.getApplicationContext(), "Connect device fail...", Toast.LENGTH_SHORT).show();
        }
        else {
            byte[] Temp_UID = new byte[100];
            YN020Host.GetDevMessage(YN020Host.GetDevSgNum, Temp_UID);
            for(int i=0;i<YN020Host.GetDevSgNum[0];i++){
            }
            Log.e("Success ","Connect device success");
        }
        SharedPreferences.Editor editor = activity.getSharedPreferences("param", Activity.MODE_PRIVATE).edit();
        editor.putInt("baudrate", YN020Host.baud_rate);
        editor.putString("devname", YN020Host.dev);
        editor.putInt("ttys", tty);
        editor.putInt("baud", buadid);
        editor.putInt("type", (int)sel_connettype);
        editor.commit();
        ClearAllFp();
    }
    public byte ClearAllFp(){
        byte[] buff1 = new byte[2];
        byte[] buff2 = new byte[1];
        byte ret = YN020host.CommandProcess(YN020host.YN020_CLEAR_ALL_FP, buff1, buff2, buff2, buff2);
        short Fp_count = (short)(((buff1[0] & 0x00FF) << 8) | (0x00FF & buff1[1]));
        if(ret !=0 ) Toast.makeText(activity.getApplicationContext(), "Execution Failure", Toast.LENGTH_SHORT).show();
        else Log.e("","Device Initialized Successfully");
        return 0;
    }
    public void LEDControl()
    {
        char rets  = (char) YN020host.GetDevMessage(YN020host.GetDevSgNum, AllDevUID);
        if(rets == 0){
            Sled_Contronl((byte) 1,Low);
            System.arraycopy(AllDevUID, 0,YN020host.Cur_UID , 0, 8);
//            for(int i=0;i<YN020host.GetDevSgNum[0];i++){
//                ShowHex(AllDevUID, 8,8*i);
//            }
        }
    }
    public byte Sled_Contronl(byte state,byte mode){
        byte[] buff1 = new byte[1];
        byte[] buff2 = new byte[1];
        byte[] buff3 = new byte[1];
        byte ret = 1;
        if(state == 1){
            buff1[0] = (byte) 1;
            buff2[0] = (byte) mode;
            ret = YN020host.CommandProcess(YN020host.YN020_TOUCH_SENSING, buff1, buff2, buff3, buff3);
            buff1[0] = (byte) 2;
            ret = YN020host.CommandProcess(YN020host.YN020_SLED_CTRL, buff1, buff2, buff2, buff2);
            return ret;
        }
        else if(state == 0){
            buff1[0] = (byte) 0;
            buff2[0] = (byte) mode;
            ret = YN020host.CommandProcess(YN020host.YN020_TOUCH_SENSING, buff1, buff2, buff3, buff3);
            buff1[0] = (byte) 1;
            ret = YN020host.CommandProcess(YN020host.YN020_SLED_CTRL, buff1, buff2, buff2, buff2);
            return ret;
        }
        return ret;
    }

    public byte UpFpImage(){
        byte[] buff1 = new byte[1];
        byte ret = YN020host.CommandProcess(YN020host.YN020_UP_FP_IMAGE, buff1, buff1,bmp_buff, buff1);
        if(ret != 0);
        else { get_bmp = true;}
        IsShow = false;

//        bvsListner.setBitmap(bm);
        bm = BitmapFactory.decodeByteArray(bmp_buff, 0, bmp_buff.length);
        fingerprintquality = NFIQUtil.calculateNFIQUsingRawBytes(bmp_buff,bm.getWidth(),bm.getHeight()); // (activity,bm);

        new ComparingTask().execute();
        return ret;
    }

    class ComparingTask extends AsyncTask<Void, Void, String> {
        ProgressDialog pr;

        @Override
        protected String doInBackground(Void... params) {
            FingerprintTemplate fptemp = new FingerprintTemplate();
            FingerprintTemplate left = fptemp.create(bmp_buff);
            Log.i("SourceAFIS", "Left fingerprint loaded");
            FingerprintTemplate right = fptemp.create(bmp_buff);
            Log.i("SourceAFIS", "Right fingerprint loaded");

            double score = new FingerprintMatcher().index(left).match(right);
            Log.e("SourceAFIS ", "Score : " + score);

            return "Executed";
        }

        @Override
        protected void onPreExecute() {
            pr = new ProgressDialog(activity);
            pr.setMessage("Extracting Details");
            pr.show();

        }

        @Override
        protected void onPostExecute(String result) {
            if (pr.isShowing()) {
                pr.dismiss();
            }

            bvsListner.setNFIQuality(Integer.toString(fingerprintquality));
            bvsListner.setMinutiaeCount(Integer.toString(minutiaecount));
     //       Toast.makeText(activity,"NFIQ: " + fingerprintquality + " Minutia: " + minutiaecount,Toast.LENGTH_SHORT).show();
            ApplicationData.errorCount++;
            if (ApplicationData.errorCount < 3 && (minutiaecount < 13 || minutiaecount > 255 || fingerprintquality > 3 || fingerprintquality < 1)) {
                PopupDialogs.alertDialog("Poor fingerprint quality. Put your finger on scanner and press scan again.", PopupDialogs.Status.ERROR,
                        activity, new PopupDialogs.OnCustomClickListener() {
                            @Override
                            public void onClick(View v, Object obj) {

                            }

                        });
                bvsListner.setButtonEnabled(true);
                return;
            } else {

                if(!fingerprintISO.equals("")) {
                    bvsListner.setBitmap(bm);
                    bvsListner.setButtonEnabled(true);
                    bvsListner.changeLayoutAfterFirstScan();
                    bvsListner.setInfoMessage("Fingerprint scanned successfully");
                }
            }
            WSQEncode();

        }
    }

        public void WSQEncode() {
            try {
                float bitrate = 0.75f;
                WSQlib wsqlib = new WSQlib();
                byte[] data = wsqlib.WSQ_encode_stream(bmp_buff,bm.getWidth(),bm.getHeight(),bitrate,500,"");
           //     String str = new String(data, "UTF-8");
                byte hexi[] = Base64.encode(data, Base64.NO_WRAP);
                String hexString = new String(hexi);
                ApplicationData.WSQ = hexString;
                bvsListner.setWSQ("");
                Log.e("WSQ SCO: ",hexString);
              //  Toast.makeText(activity,"WSQ: " + str,Toast.LENGTH_SHORT).show();
            } catch (Exception a) {

            }
        }

        public byte Enroll_Fp(short Fp_ID, byte Step, byte Count) {
            byte[] buff1 = new byte[2];
            byte[] buff2 = new byte[1];
            byte[] buff3 = new byte[1];
            byte[] buff4 = new byte[2];
            buff1[0] = (byte) ((Fp_ID >> 8) & 0x00ff);
            buff1[1] = (byte) (Fp_ID & 0x00ff);
            buff2[0] = (byte) Step;
            buff3[0] = (byte) Count;
            buff4[0] = (byte) 0;
            byte ret = YN020host.CommandProcess(YN020host.YN020_ENROLL_FP, buff1, buff2, buff3, buff4);
            return ret;
        }

        public byte GetFpImage() {
            byte[] buff1 = new byte[1];
            byte ret;
            ret = YN020host.JNI_CommandProcess(YN020host.YN020_GET_FP_IMAGE, buff1, buff1, buff1, buff1, AllDevUID);
            return ret;
        }

        public String GetTemplate() {
            byte ret;
            byte p_nParam1 = 0;
            byte p_nParam2 = 1;
            byte[] buff1 = new byte[1];
            byte[] buff2 = new byte[1];
            byte[] buff3 = new byte[820];
            byte[] buff4 = new byte[4];
            buff1[0] = (byte) p_nParam1;
            buff2[0] = (byte) p_nParam2;
            String str = "fail";
            ret = Open_LED();
            if (ret != 0) {
            }
            ret = GetFpImage();
            if (ret != 0) {
            }
            ret = Generate_fpram();
            if (ret != 0) {
            }

            ret = YN020Host.JNI_CommandProcess(YN020Host.YN020_READ_FP_RAM, buff1, buff2, buff3, buff4, AllDevUID);

            int Temp_size = (int) (((buff4[0] & 0x000000FF) << 24) | ((buff4[1] & 0x000000FF) << 16) | ((buff4[2] & 0x000000FF) << 8) | (0x000000FF & buff4[3]));

            byte[] bufftemp = new byte[Temp_size];

            System.arraycopy(buff3, 0, bufftemp, 0, Temp_size);

            fingerprintISO = Base64.encodeToString(buff3, 0, Temp_size, Base64.NO_WRAP);

            AppLogger.i("Hex String  " + fingerprintISO);

            bvsListner.setTemplateType("ISO_19794_2");
            bvsListner.setHexString(fingerprintISO);

            refreshLED();
            return fingerprintISO;
        }

    public byte Generate_fpram(){
        byte ret;
        byte[] buff1 = new byte[1];
        byte[] buff2 = new byte[1];
        buff1[0] = (byte) 0;
        ret = YN020host.JNI_CommandProcess(YN020host.YN020_GENERATE_FP_RAM, buff1,buff2, buff2, buff2,AllDevUID);
        return ret;
    }
    public byte Open_LED(){
        byte ret;
        byte[] buff1 = new byte[1];
        byte[] buff2 = new byte[1];
        buff1[0] = (byte) 1;
        ret = YN020host.JNI_CommandProcess(YN020host.YN020_SLED_CTRL, buff1,buff2, buff2, buff2,AllDevUID);
        return ret;
    }
    public static String getHexString(byte[] b, int fplength) {
        String result = "";
        for (int i=0; i < fplength; i++) {
            result +=
                    Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        Log.e("Result ",result);
        return result;
    }
    public short SetBaudrate(int baudrate){
        byte[] buff1 = new byte[1];
        byte[] buff2 = new byte[1];
        buff1[0] = (byte) baudrate;
        byte ret = YN020host.CommandProcess(YN020host.YN020_SET_BAUDRATE, buff1, buff2, buff2, buff2);
        return ret;
    }
    public int GetEditVal(){
        String Strcmd="";
        Strcmd = "1";
        int ClearID = Integer.valueOf(Strcmd).intValue();
        Log.d("GetEditVal","EditVal = "+ClearID);
        return ClearID;
    }
    public void refreshLED()
    {
        FreeUpImagehandle.post(FreeUpImageThead);
        FreeUpImagehandle.removeCallbacks(FreeUpImageThead);
        SharedPreferences sharedPreferences= activity.getSharedPreferences("param", Activity.MODE_PRIVATE);
        int baudrate =sharedPreferences.getInt("baudrate", 9);
        String devname =sharedPreferences.getString("devname", (new StringBuilder()).append("[/dev/").append(ttyMt).append("]").toString());
        YN020Hostimage.Communication_Type = YN020Hostimage.UART_COMMUNICATION;
        YN020Hostimage.dev = devname;
        YN020Hostimage.baud_rate = baudrate;
        if(SetBaudrate(baudrate) == 0){
            if(YN020Hostimage.Init_Device() == 0){
            }
            else Toast.makeText(activity, "Set low speed mode failure,please Reconnect  the devices", Toast.LENGTH_LONG).show();
        }
        int sel_ttys =sharedPreferences.getInt("ttys", 2);
        int sel_baud =sharedPreferences.getInt("baud", 2);
        int sel_connettype =sharedPreferences.getInt("type", 0);
        YN020Host.Communication_Type = (byte) sel_connettype ;
        System.out.println("Communication_Type"+YN020Host.Communication_Type);
        byte tty =(byte) sel_ttys ;
        YN020Host.dev = ttyMt;
        byte buadid = (byte) sel_baud ;
        YN020Host.baud_rate = YN020Host.baud_rate_38400;
        if(YN020Host.Init_Device() != 0){
            Toast.makeText(activity, "Connect device fail...", Toast.LENGTH_SHORT).show();
        }
        else {
            byte[] Temp_UID = new byte[100];
            YN020Host.GetDevMessage(YN020Host.GetDevSgNum, Temp_UID);
            for(int i=0;i<YN020Host.GetDevSgNum[0];i++){
            }
            Log.e("","");
        }
        SharedPreferences.Editor editor = activity.getSharedPreferences("param", Activity.MODE_PRIVATE).edit();
        editor.putInt("baudrate", YN020Host.baud_rate);
        editor.putString("devname", YN020Host.dev);
        editor.putInt("ttys", tty);
        editor.putInt("baud", buadid);
        editor.putInt("type", (int)sel_connettype);
        editor.commit();
        char rets  = (char) YN020host.GetDevMessage(YN020host.GetDevSgNum, AllDevUID);
        if(rets == 0){
            Sled_Contronl((byte) 1,Low);
            System.arraycopy(AllDevUID, 0,YN020host.Cur_UID , 0, 8);
//            for(int i=0;i<YN020host.GetDevSgNum[0];i++){
//                ShowHex(AllDevUID, 8,8*i);
//            }
        }
    }
    public short Get_FpNo(){
        byte[] buff1 = new byte[2];
        byte[] buff2 = new byte[1];
        byte ret = YN020host.CommandProcess(YN020host.YN020_GET_EMPTY_FP_ID, buff1, buff2, buff2, buff2);
        Empty_id = (short)(((buff1[0] & 0x00FF) << 8) | (0x00FF & buff1[1]));
        if(ret != 0){
            return 1;
        }
        return Empty_id;
    }
}
