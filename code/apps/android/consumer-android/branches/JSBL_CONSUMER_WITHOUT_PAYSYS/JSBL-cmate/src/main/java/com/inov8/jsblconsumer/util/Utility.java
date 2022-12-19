package com.inov8.jsblconsumer.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;

import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.ui.components.ListViewExpanded;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utility {
    public static int getScaledPixels(int i, Activity context) {
        // measuring pixels
        int pixel = i;
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pixel * scale + 0.5f);
    }
    public static String getFormattedDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(new Date());
    }

    public final static boolean isValidEmail(EditText etEmail) {
        if (etEmail.getText() == null) {
            etEmail.setError(AppMessages.INVALID_EMAIL_ID);
            return false;
        } else if (android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText()).matches()) {
            return true;
        } else {
            etEmail.setError(AppMessages.INVALID_EMAIL_ID);
            return false;
        }
    }

    public static String getFormattedDate(String inputDate){
        try {
            SimpleDateFormat temp = null;
            Date initDate = new SimpleDateFormat("dd-MM-yyyy").parse(inputDate);
            temp = new SimpleDateFormat("yyyy-MM-dd");
            return temp.format(initDate);
        }catch (Exception e){
            return null;
        }
    }


    public static String getFormattedDateShort() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        return dateFormat.format(new Date());
    }

    public static String getFormattedTime() {
        DateFormat dateFormat = new SimpleDateFormat("hh:mm aaa");
        return dateFormat.format(new Date());
    }

    public static String getFormattedTimeWithSeconds() {
        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss aaa");
        return dateFormat.format(new Date());
    }

    public static Bitmap getThumbnail(File f, int width, int height) {
        try {
            // final int THUMBNAIL_SIZE = Size;
            Runtime.getRuntime().freeMemory();
            System.gc();
            byte[] imageData = null;

            FileInputStream fis = new FileInputStream(f);
            Bitmap imageBitmap = BitmapFactory.decodeStream(fis);

            imageBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height,
                    false);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            imageData = baos.toByteArray();

            BitmapFactory.Options options = new BitmapFactory.Options();
            return BitmapFactory.decodeByteArray(imageData, 0,
                    imageData.length, options);

        } catch (Exception e) {
            return null;
        }
    }

    @SuppressLint("NewApi")
    public static String getFormatedAmount(String amt) {
        amt = getUnFormattedAmount(amt);
        double amount = Double.parseDouble(amt);
        DecimalFormat dec = new DecimalFormat("##,##0.00",
                DecimalFormatSymbols.getInstance());
        amt = dec.format(amount);
        return amt;
    }

    public static String getUnFormattedAmount(String amt) {
        amt = amt.replaceAll("[^\\d.]", "");
        return amt;
    }

    public static String removeComma(String amt) {
        amt = amt.replaceAll(",", "");
        return amt;
    }

    public static Spanned getFormattedCurrency(String currency) {
        String[] parts = currency.split("\\.");

        return Html.fromHtml("PKR " + parts[0] + "." + parts[1]);
    }

    public static long daysDifference(String day1, String day2) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yy");
        try {
            Date todayDate = format.parse(day1);
            Date passedDate = format.parse(day2);

            long duration = todayDate.getTime() - passedDate.getTime();

            return duration / (24 * 60 * 60 * 1000);

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static void getListViewSize(ListViewExpanded listView, Context context,
                                       int padding) {
        ListAdapter myListAdapter = listView.getAdapter();
        if (myListAdapter == null) {
            // do nothing return null
            return;
        }
        // set listAdapter in loop for getting final size
        int totalHeight = 0;
        // Log.d("numberofrow", "" + myListAdapter.getCount());
        for (int size = 0; size < myListAdapter.getCount(); size++) {

            View listItem = myListAdapter.getView(size, null, listView);
            if (listItem instanceof ViewGroup)
                listItem.setLayoutParams(new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT));
            /*
             * listItem.measure( MeasureSpec.makeMeasureSpec(0,
			 * MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0,
			 * MeasureSpec.UNSPECIFIED));
			 */

            // WindowManager wm = (WindowManager) context
            // .getSystemService(Context.WINDOW_SERVICE);
            // Display display = wm.getDefaultDisplay();

            DisplayMetrics metrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay()
                    .getMetrics(metrics);

            int screenWidth = metrics.widthPixels; // deprecated
            // int height = display.getHeight(); // deprecated

            int listViewWidth = screenWidth - 10 - 10;
            int widthSpec = MeasureSpec.makeMeasureSpec(listViewWidth,
                    MeasureSpec.AT_MOST);
            listItem.measure(widthSpec, 0);

            totalHeight += listItem.getMeasuredHeight();
        }
        // setting listview item in adapter
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (myListAdapter.getCount() - 1))
                + padding;
        listView.setLayoutParams(params);
        listView.requestLayout();
        // print height of adapter on log
        // Log.i("height of listItem:", String.valueOf(totalHeight));
    }

    public static boolean isNull(String edittxt) {
        if (edittxt != null && !edittxt.equals("null") && !edittxt.equals(""))
            return false;
        else
            return true;
    }

    public static boolean testValidity(EditText et){
        if(TextUtils.isEmpty(et.getText().toString())) {
            et.setError("Field must not be empty.");
            et.requestFocus();
            return false;
        }
        else
            return true;

    }


    public static void createIPChangerDialog(final Context context) {

        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.dialog_ip_changer, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);


        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);
        userInput.setText("172.29.12.");

        // set dialog message
        alertDialogBuilder
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // get user input and set it to result
                        // edit text

                        String baseURL = "http://" + userInput
                                .getText().toString() + "/i8Microbank/allpay.me";

                        PreferenceConnector.writeString(context,
                                PreferenceConnector.CUSTOM_IP, baseURL);

                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public static void hideKeyboard(View view, Context con) {
        InputMethodManager imm = (InputMethodManager) con
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String getFormattedDateAll(String inputDate){
        SimpleDateFormat temp = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setLenient(false);

        try {
            Date initDate = sdf.parse(inputDate);
            temp = new SimpleDateFormat("yyyy-MM-dd");
            return temp.format(initDate);
        }catch (Exception e){
            return inputDate;
        }
    }


    public static String getExtensionIcon(String icon) {
        if (icon != null) {
            if (icon.contains(".png") || icon.contains(".jpg")
                    || icon.contains(".jpeg")) {
                return icon;
            } else {
                return icon.concat(".png");
            }
        } else {
//            return Constants.URLs.fbImages + Constants.FB_I8_ICON_NAME;
            return "";
        }
    }

    public static long dateDifference(String startDate, String endDate) {
        try {
            Calendar startCal = Calendar.getInstance(), endCal = Calendar
                    .getInstance();

            startCal.setTime(new SimpleDateFormat("dd-MM-yyyy")
                    .parse(startDate));
            endCal.setTime(new SimpleDateFormat("dd-MM-yyyy").parse(endDate));

            int diffYear = endCal.get(Calendar.YEAR)
                    - startCal.get(Calendar.YEAR);
            int diffMonth = diffYear * 12 + endCal.get(Calendar.MONTH)
                    - startCal.get(Calendar.MONTH);
            int diffDay = endCal.get(Calendar.DAY_OF_YEAR) - startCal.get(Calendar.DAY_OF_YEAR);
            if (diffDay < 0) {
                diffMonth = diffMonth - 1;
            }

            return diffMonth;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}