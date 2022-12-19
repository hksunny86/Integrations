package com.inov8.agentmate.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.inov8.agentmate.activities.BaseActivity;
import com.inov8.jsbl.sco.R;
import com.paysyslabs.instascan.AreaName;

import static com.paysyslabs.instascan.AreaName.AZAD_KASHMIR;
import static com.paysyslabs.instascan.AreaName.BALUCHISTAN;
import static com.paysyslabs.instascan.AreaName.FATA;
import static com.paysyslabs.instascan.AreaName.GILGIT_BALTISTAN;
import static com.paysyslabs.instascan.AreaName.ISLAMABAD;
import static com.paysyslabs.instascan.AreaName.KHYBER_PAKHTUNKHWA;
import static com.paysyslabs.instascan.AreaName.PUNJAB;
import static com.paysyslabs.instascan.AreaName.SINDH;

public class Utility {
	public static int getScaledPixels(int i, Activity context) {
		// measuring pixels
		int pixel = i;
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pixel * scale + 0.5f);
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

	public static String getFormattedDate(String inputDate){
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

	public static void createAlertDialog(final String reason,
			final String title, final Context con) {
		AlertDialog.Builder alert = new AlertDialog.Builder(con);
		alert.setTitle(title);
		alert.setCancelable(false);
		alert.setMessage(reason);
		alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog,
					int which) {
				dialog.cancel();
			}
		});
		alert.show();
	}
	
	public static void createAlertDialog(final String reason,
			final String title, final Activity ac,
			DialogInterface.OnClickListener lisnter) {
		AlertDialog.Builder alert = new AlertDialog.Builder(ac);
		alert.setTitle(title);
		alert.setCancelable(false);
		alert.setMessage(reason);
		alert.setPositiveButton("OK", lisnter);
		alert.show();
	}

	public static void createAlertDialog(final String reason,
			final String title, BaseActivity main,
			OnClickListener listenerForYes, OnClickListener listenerForNo) {
		AlertDialog.Builder alert = new AlertDialog.Builder(main);
		alert.setTitle(title);
		alert.setCancelable(true);
		alert.setMessage(reason);
		alert.setPositiveButton("Yes", listenerForYes);
		alert.setNegativeButton("No", listenerForNo);
		alert.show();
	}

	public static void getListViewSize(ListView listView, Context context,
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

	public static void showConfirmationDialog(Activity ac, String msg,
			DialogInterface.OnClickListener listnerYes) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ac);
		// set dialog options
		alertDialogBuilder.setCancelable(false)
				.setPositiveButton("Yes", listnerYes)
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
					// set dialog message
				}).setMessage(msg);

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	public static boolean isNull(String edittxt) {
		if (edittxt != null && !edittxt.equals("null") && !edittxt.equals(""))
			return false;
		else
			return true;
	}
	
	
	public static void createIPChangerDialog(final Context context) {
		// get prompts.xml view
		LayoutInflater li = LayoutInflater.from(context);
		View promptsView = li.inflate(R.layout.dialog_ip_changer, null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);

		// set prompts.xml to alertdialog builder
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
						
						String baseURL="http://" +userInput
								.getText().toString()+":8080/i8Microbank/allpay.me";
						
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
	
//	public static String getLocalIpv4Address(Context con) {
//        try {
//            String ipv4;
//            List<NetworkInterface> nilist = Collections.list(NetworkInterface
//                    .getNetworkInterfaces());
//            if (nilist.size() > 0) {
//                for (NetworkInterface ni : nilist) {
//                    List<InetAddress> ialist = Collections.list(ni
//                            .getInetAddresses());
//                    if (ialist.size() > 0) {
//                        for (InetAddress address : ialist) {
//                            if (!address.isLoopbackAddress()
//                                    && InetAddressUtils
//                                    .isIPv4Address(ipv4 = address
//                                            .getHostAddress())) {
//                                return ipv4;
//                            }
//                        }
//                    }
//
//                }
//            }
//
//        } catch (SocketException ex) {
//        	DialogInterface.OnClickListener listener=new OnClickListener() {
//
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					// TODO Auto-generated method stub
//
//				}
//			};
//            createAlertDialog(
//                    Constants.Messages.EXCEPTION_INVALID_RESPONSE,
//                    Constants.Messages.ALERT_HEADING, con);
//        }
//        return "";
//    }

    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    public static boolean isExpiryDateCorrect(String date) {
        String currentDate = getCurrentTimeStamp();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = sdf.parse(date);
            Date date2 = sdf.parse(currentDate);

            if(date1.after(date2)){
                return true;
            }
            else
                return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
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

    public static String bytes2HexString(byte[] b) {
        String ret = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase();
        }
        return ret;
    }

    public static void setAgentAreaName(Intent intent) {

        if (ApplicationData.agentAreaName != null) {
            switch (ApplicationData.agentAreaName) {
                case Constants.AreaName.PUNJAB:
                    intent.putExtra(Constants.IntentKeys.AREA_NAME, (AreaName) PUNJAB);
                    break;
                case Constants.AreaName.AZAD_KASHMIR:
                    intent.putExtra(Constants.IntentKeys.AREA_NAME, (AreaName) AZAD_KASHMIR);
                    break;
                case Constants.AreaName.BALUCHISTAN:
                    intent.putExtra(Constants.IntentKeys.AREA_NAME, (AreaName) BALUCHISTAN);
                    break;
                case Constants.AreaName.FATA:
                    intent.putExtra(Constants.IntentKeys.AREA_NAME, (AreaName) FATA);
                    break;
                case Constants.AreaName.GILGIT_BALTISTAN:
                    intent.putExtra(Constants.IntentKeys.AREA_NAME, (AreaName) GILGIT_BALTISTAN);
                    break;
                case Constants.AreaName.ISLAMABAD:
                    intent.putExtra(Constants.IntentKeys.AREA_NAME, (AreaName) ISLAMABAD);
                    break;
                case Constants.AreaName.KHYBER_PAKHTUNKHWA:
                    intent.putExtra(Constants.IntentKeys.AREA_NAME, (AreaName) KHYBER_PAKHTUNKHWA);
                    break;

                case Constants.AreaName.SINDH:
                    intent.putExtra(Constants.IntentKeys.AREA_NAME, (AreaName) SINDH);
                    break;
                default:
                    intent.putExtra(Constants.IntentKeys.AREA_NAME, (AreaName) PUNJAB);
                    break;
            }
        } else
            intent.putExtra(Constants.IntentKeys.AREA_NAME, PUNJAB);
    }

    public static ArrayList<String> getAllFingers(){

        ArrayList<String> allFingers = new ArrayList<String>();

        allFingers.add("RIGHT_THUMB");
        allFingers.add("RIGHT_INDEX");
        allFingers.add("RIGHT_MIDDLE");
        allFingers.add("RIGHT_RING");
        allFingers.add("RIGHT_LITTLE");
        allFingers.add("LEFT_THUMB");
        allFingers.add("LEFT_INDEX");
        allFingers.add("LEFT_MIDDLE");
        allFingers.add("LEFT_RING");
        allFingers.add("LEFT_LITTLE");

        return allFingers;
    }
}