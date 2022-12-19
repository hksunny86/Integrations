package com.inov8.agentmate.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatImageView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.inov8.agentmate.activities.openAccount.OpenAccountBvsActivity;
import com.inov8.agentmate.util.AppLogger;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.Utility;
import com.inov8.jsbl.sco.R;
import com.paysyslabs.instascan.Fingers;

import org.w3c.dom.Text;

import static com.inov8.agentmate.util.Utility.testValidity;

public class PopupDialogs {
	private static Dialog dialog;

    public static Dialog createAlertDialog(final String msg, final String headerText,
                                           final Status status, final Context context,
                                           final OnCustomClickListener clickListener){
        dialog = createDialog(context, status, false, R.layout.dialog_alert);

        TextView headerMessage = (TextView) dialog.findViewById(R.id.textViewAlertHeading);
        TextView message = (TextView) dialog.findViewById(R.id.textViewMessage);

        dialog.setCancelable(false);

        headerMessage.setText(headerText);
        message.setText(msg);

        final Button btnOk = (Button) dialog.findViewById(R.id.btnOK);

        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                clickListener.onClick(v, "1");
            }
        });
        return dialog;
    }
    public static Dialog createConfirmationDialog(final String msg, final String headerText,
                                                  final Status status, final Context context,
                                                  final OnCustomClickListener clickListener){
        dialog = createDialog(context, status, false, R.layout.dialog_confirm);

        TextView headerMessage = (TextView) dialog.findViewById(R.id.textViewAlertHeading);
        TextView message = (TextView) dialog.findViewById(R.id.textViewMessage);

        dialog.setCancelable(false);

        headerMessage.setText(headerText);
        message.setText(msg);

        final Button btnOk = (Button) dialog.findViewById(R.id.btnOK);

        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                clickListener.onClick(v, "1");
            }
        });
        final Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                clickListener.onClick(v, "1");
            }
        });
        return dialog;
    }

    public static Dialog alertDialog(final String msg, final Status status, final Context context,
                                        final OnCustomClickListener customClickListener) {
        dialog = createDialog(context, status, false, R.layout.dialog_alert);

        TextView message = (TextView) dialog.findViewById(R.id.textViewMessage);

        message.setText(msg);

        final Button btnOk = (Button) dialog.findViewById(R.id.btnOK);

        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                customClickListener.onClick(v, "1");
            }
        });
        return dialog;
    }

    public static Dialog otpPopupDialog(String code, final String msg, Status status,
                                        final Context context, final OnCustomClickListener verifyPinListener) {

        dialog = createDialog(context, status, false, R.layout.dialog_otp);

        final TextView txtLabel = (TextView) dialog.findViewById(R.id.txtLabel);
        final EditText txtPin = (EditText) dialog.findViewById(R.id.txtPin);
        final Button btnOk = (Button) dialog.findViewById(R.id.btnOK);
        final Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        final Button btnResend = (Button) dialog.findViewById(R.id.btnResend);
        final Button btnRegenerate = (Button) dialog.findViewById(R.id.btnRegenerate);

        txtLabel.setText(msg);

        if (code != null && code.equals("9029")) {
            btnOk.setVisibility(View.GONE);
            btnResend.setVisibility(View.GONE);
            btnRegenerate.setVisibility(View.VISIBLE);
            btnRegenerate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verifyOtp(txtPin, verifyPinListener, dialog, v);
                }
            });
            btnCancel.setVisibility(View.VISIBLE);
            btnCancel.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        } else if (code != null
                && (code.equals("9025") || code.equals("9026")
                || code.equals("9027") || code.equals("9028"))) {
            txtPin.setVisibility(View.VISIBLE);
            btnRegenerate.setVisibility(View.GONE);
            btnOk.setVisibility(View.VISIBLE);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!testValidity(txtPin)) {
                        return;
                    }
                    verifyOtp(txtPin, verifyPinListener, dialog, v);
                }
            });
            btnCancel.setVisibility(View.VISIBLE);
            btnCancel.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            btnResend.setVisibility(View.VISIBLE);
            btnResend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verifyOtp(txtPin, verifyPinListener, dialog, v);
                }
            });
        } else {
            btnOk.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
            btnResend.setVisibility(View.VISIBLE);
            btnRegenerate.setVisibility(View.GONE);
            btnResend.setText("OK");
            btnResend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToLogin(verifyPinListener, dialog, v);
                }
            });
        }
        return dialog;
    }

    public static Dialog otpDialog(Status status, final Context context,
                                   final OnCustomClickListener verifyPinListener) {

        dialog = createDialog(context, status, false, R.layout.dialog_otp);

        final TextView txtLabel = (TextView) dialog.findViewById(R.id.txtLabel);
        final EditText txtPin = (EditText) dialog.findViewById(R.id.txtPin);
        final Button btnOk = (Button) dialog.findViewById(R.id.btnOK);
        final Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        dialog.findViewById(R.id.layoutHeader).setBackgroundColor(R.color.orange);
        final TextView txtHeader = (TextView) dialog.findViewById(R.id.textViewAlertHeading);
        txtHeader.setText("OTP Verification");

        txtLabel.setText("Enter OTP");
        txtLabel.setGravity(Gravity.LEFT);
        txtPin.setVisibility(View.VISIBLE);

        txtPin.setFilters(new InputFilter[] { new InputFilter.LengthFilter
                (Constants.MAX_LENGTH_VERIFICATION_OTP) });

        txtPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length() == 5){
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(txtPin.getWindowToken(), 0);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if(s.length() == 5){
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(txtPin.getWindowToken(), 0);
                }
        }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnOk.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.VISIBLE);

        btnCancel.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!testValidity(txtPin)) {
                    return;
                }

                if(txtPin.getText().toString().length() < Constants.MAX_LENGTH_VERIFICATION_OTP){
                    txtPin.setError(Constants.Messages.INVALID_VERIFICATION_TRX_CODE);
                }
                dialog.dismiss();
                verifyOtp(txtPin, verifyPinListener, dialog, v);
            }
        });

        return dialog;
    }

    public static Dialog bvsFingerIndexDialog(final Context context, final OnCustomClickListener clickListener){

        dialog = createDialog(context, null, false, R.layout.dialog_bvs_finger_index);

        final Spinner fingerSpinner = (Spinner) dialog.findViewById(R.id.fingersSpinner);

        ArrayAdapter<Fingers> fingersArrayAdapter = new ArrayAdapter<Fingers>
                (context, android.R.layout.simple_spinner_item, Fingers.values());
        fingersArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fingerSpinner.setAdapter(fingersArrayAdapter);
        fingerSpinner.setSelection(1);

        final Button btnContinue = (Button) dialog.findViewById(R.id.btnContinue);

        btnContinue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Fingers f = (Fingers) fingerSpinner.getSelectedItem();
                clickListener.onClick(v, f);
            }
        });

        return dialog;
    }

    public static Dialog conventionalAccountOpeningDialog(final Context context, final OnCustomClickListener clickListener){

        dialog = createDialog(context, null, false, R.layout.dialog_conventional_account_opening);

        final Button btnYes = (Button) dialog.findViewById(R.id.btnYes);
        final Button btnRetry = (Button) dialog.findViewById(R.id.btnRetry);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                clickListener.onClick(v, "1");
            }
        });

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                clickListener.onClick(v, "2");
            }
        });

        return dialog;
    }

    public static Dialog bvsAccountOpeningPopupDialog(final String msg, final Context context,
                                                      final OnCustomClickListener verifyPinListener) {
        dialog = createDialog(context, Status.LOCK, false, R.layout.dialog_bvs_account_opening);

        final TextView txtLabel = (TextView) dialog.findViewById(R.id.txtLabel);

        txtLabel.setText(msg);

        final Button btnYes = (Button) dialog.findViewById(R.id.btnYes);
        final Button btnNo = (Button) dialog.findViewById(R.id.btnNo);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                verifyPinListener.onClick(v, "1");
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                verifyPinListener.onClick(v, "2");
            }
        });

        return dialog;
    }

	private static void goToLogin(OnCustomClickListener customeOnclickListner,
			Dialog dialog, View v) {
		dialog.dismiss();
		customeOnclickListner.onClick(v, null);
	}

	private static void verifyOtp(EditText txtPin,
			OnCustomClickListener customeOnclickListner, Dialog dialog, View v) {
		dialog.dismiss();
		customeOnclickListner.onClick(v, txtPin.getText().toString());
	}

	private static Dialog createDialog(final Context context, Status status,
			Boolean cancel, int dialogId) {
		dialog = new Dialog(context,
				android.R.style.Theme_Translucent_NoTitleBar) {

			@Override
			public void onBackPressed() {
			}
		};

		dialog.setContentView(dialogId);
		dialog.setCancelable(cancel);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(R.color.dialog_background));

		AppCompatImageView headerImage = (AppCompatImageView) dialog
				.findViewById(R.id.imageViewAlert);
		RelativeLayout layoutHeader = (RelativeLayout) dialog
				.findViewById(R.id.layoutHeader);

        if(status!=null) {
            switch (status) {
                case INFO:
                    headerImage.setImageResource(R.drawable.info_popup_icon);
                    break;

                case SUCCESS:
                    headerImage.setImageResource(R.drawable.success_popup_icon);
                    break;

                case ERROR:
                    headerImage.setImageResource(R.drawable.failure_popup_icon);
                    break;

                case ALERT:
                    headerImage.setImageResource(R.drawable.alert_popup_icon);
                    break;

                case LOCK:
                    headerImage.setImageResource(R.drawable.mpin_popup_icon);
                    break;

                default:
                    break;
            }
        }

		try {
			dialog.show();
		} catch (WindowManager.BadTokenException btexp) {
			btexp.printStackTrace();
			AppLogger.e(btexp);
		}

		return dialog;
	}

	public enum Status {
		INFO, SUCCESS, ERROR, ALERT, LOCATOR, DISTANCE, LOCK;
	}

	public interface OnCustomClickListener {
		public void onClick(View v, Object obj);
	}
}