package com.inov8.jsblconsumer.ui.components;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.BaseActivity;
import com.inov8.jsblconsumer.activities.openAccount.OpenAccountBvsOtpActivity;
import com.inov8.jsblconsumer.util.AesEncryptor;
import com.inov8.jsblconsumer.util.AppLogger;
import com.inov8.jsblconsumer.util.AppMessages;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.util.PasswordController;
import com.inov8.jsblconsumer.util.Utility;

import static com.inov8.jsblconsumer.ui.components.PopupDialogs.Status.ALERT;
import static com.inov8.jsblconsumer.ui.components.PopupDialogs.Status.SUCCESS;

public class PopupDialogs {
    private static Dialog dialog;
    private Dialog mDialog;
    private Context context;
    public PasswordController passwordController;
    private boolean isOpen = false;


    public PopupDialogs(Context context) {
        this.context = context;
    }



    public  Dialog createDialogMore(final Context context) {
        mDialog = new Dialog(context,
                android.R.style.Theme_Translucent_NoTitleBar) {

            @Override
            public void onBackPressed() {
                dismiss();
            }

            @Override
            public boolean onTouchEvent(@NonNull MotionEvent event) {
                this.dismiss();
                return true;
            }
        };

//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(R.color.dialog_background));
        mDialog.setContentView(R.layout.dialog_more);
        mDialog.setCancelable(true);

        try {
            mDialog.show();
        } catch (WindowManager.BadTokenException btexp) {
            btexp.printStackTrace();
            AppLogger.e(btexp);
        }

        return mDialog;

    }



    public Dialog createConfirmationDialog(String strReason, final String strTitle, Context context, String btnOkText, String btnCancelText,
                                           View.OnClickListener listenerOk, View.OnClickListener listenerCancel, boolean isCancellable, Status status, boolean isBtnCrossVisible) {

        mDialog = createDialogJs(context, status, isCancellable, R.layout.layout_dialog_confirm);

        TextView tvHeading = (TextView) mDialog.findViewById(R.id.tvAlertHeading);
        TextView tvMessage = (TextView) mDialog.findViewById(R.id.tvMessage);
        Button btnOk = (Button) mDialog.findViewById(R.id.btnOK);
        Button btnCancel = (Button) mDialog.findViewById(R.id.btnCancel);
        AppCompatImageView btnClose = (AppCompatImageView) mDialog.findViewById(R.id.btnClose);

        AppCompatImageView ivAlertIcon = (AppCompatImageView) mDialog.findViewById(R.id.ivAlertIcon);


        if (isBtnCrossVisible) {
            btnClose.setVisibility(View.VISIBLE);
        } else {
            btnClose.setVisibility(View.GONE);
        }

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });


        tvHeading.setText(strTitle);
        tvMessage.setText(strReason);
        btnOk.setText(btnOkText);
        btnCancel.setText(btnCancelText);

        if (listenerOk != null) {
            btnOk.setOnClickListener(listenerOk);
        } else {
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialog.dismiss();
                }
            });
        }

        if (listenerCancel != null) {
            btnCancel.setOnClickListener(listenerCancel);
        } else {
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialog.dismiss();
                }
            });
        }
        return mDialog;
    }


    public Dialog createAlertDialog(String strReason, final String strTitle, Context context, String btnText,
                                    View.OnClickListener listener, boolean isCancellable, Status status,
                                    boolean isBtnCrossVisible, View.OnClickListener btnCrossListener) {

        mDialog = createDialogJs(context, status, isCancellable, R.layout.dialog_alert_new);

        TextView tvHeading = (TextView) mDialog.findViewById(R.id.tvAlertHeading);
        TextView tvMessage = (TextView) mDialog.findViewById(R.id.tvMessage);
        Button btnOk = (Button) mDialog.findViewById(R.id.btnOK);
        AppCompatImageView btnClose = (AppCompatImageView) mDialog.findViewById(R.id.btnClose);
        AppCompatImageView ivAlertIcon = (AppCompatImageView) mDialog.findViewById(R.id.ivAlertIcon);


        if (isBtnCrossVisible) {
            btnClose.setVisibility(View.VISIBLE);
        } else {
            btnClose.setVisibility(View.GONE);
        }

        if (btnCrossListener != null) {
            btnClose.setOnClickListener(btnCrossListener);
        } else {
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialog.dismiss();
                }
            });
        }


        tvHeading.setText(strTitle);
        Spanned text = Html.fromHtml(strReason);
        tvMessage.setText(text);
        btnOk.setText(btnText);


        if (listener != null) {
            btnOk.setOnClickListener(listener);
        } else {
            btnClose.setVisibility(View.GONE);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialog.dismiss();
                }
            });
        }

        return mDialog;
    }


    private Dialog createDialogJs(Context context, Status status, Boolean cancel, int layout) {


        if (!isOpen) {
            dismissDialogJS();
        }

        mDialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(R.color.dialog_background));
        mDialog.setContentView(layout);

        if (status != null) {

            AppCompatImageView headerImage = (AppCompatImageView) mDialog.findViewById(R.id.ivAlertIcon);

            mDialog.setCancelable(cancel);

            switch (status) {
                case INFO:
                    headerImage.setImageResource(R.drawable.popup_alert_icon);
                    break;

                case SUCCESS:
                    headerImage.setImageResource(R.drawable.popup_success_message);
//                    headerImage.setImageResource(R.drawable.popup_success_icon);
                    break;

                case ERROR:
                    headerImage.setImageResource(R.drawable.popup_alert_icon);
                    break;
//
                case ALERT:
                    headerImage.setImageResource(R.drawable.popup_alert_icon);
                    break;
//
//                case VERIFY:
//                    headerImage.setImageResource(R.drawable.popup_fpin_icon);
//                    break;
//

                default:
                    break;

            }
        }
        mDialog.show();

        return mDialog;
    }

    public void dismissDialogJS() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    public Dialog otpPopupDialog(String code, final String msg, final Context context,
                                 final OnCustomClickListener verifyPinListener, boolean isLogin) {
//        dialog = createDialog(context, ALERT, false, R.layout.dialog_otp);


        mDialog = createDialogJs(context, ALERT, false, R.layout.layout_dialog_otp);
        final TextView txtLabel = (TextView) mDialog.findViewById(R.id.txtLabel);
        final TextView txtHeading = (TextView) mDialog.findViewById(R.id.tvAlertHeading);
        final TextView txtMessage = (TextView) mDialog.findViewById(R.id.tvMessage);
        final View txtPin = (View) mDialog.findViewById(R.id.layoutFPin);
        final Button btnOk = (Button) mDialog.findViewById(R.id.btnOK);
        final AppCompatImageView btnCancel = (AppCompatImageView) mDialog.findViewById(R.id.btnClose);
        final Button btnResend = (Button) mDialog.findViewById(R.id.btnResend);
        final Button btnRegenerate = (Button) mDialog.findViewById(R.id.btnRegenerate);
        txtLabel.setText(msg);

        if (isLogin) {
            txtHeading.setText("PIN Verification");
            txtMessage.setText("Enter PIN");
        }

        passwordController = new PasswordController(mDialog.findViewById(R.id.layoutFPin), false, false, context);

        if (code != null && code.equals(Constants.ErrorCodes.DEVICE_UPDATE_OTP_EXPIRE_ERROR)) {
            btnOk.setVisibility(View.GONE);
            btnResend.setVisibility(View.GONE);
            txtPin.setVisibility(View.GONE);
            txtMessage.setVisibility(View.GONE);
            btnRegenerate.setVisibility(View.VISIBLE);
            btnRegenerate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verifyOtp(passwordController.getPassword(), verifyPinListener, mDialog, v);
                }
            });
            btnCancel.setVisibility(View.VISIBLE);
            btnCancel.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });
        } else if (code != null && (code.equals(Constants.ErrorCodes.LOGIN_OTP_VERIFICATION)
                || code.equals(Constants.ErrorCodes.LOGIN_OTP_ERROR))) {
//            txtPin.setVisibility(View.VISIBLE);
            btnRegenerate.setVisibility(View.GONE);
            btnOk.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.VISIBLE);
            btnCancel.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (TextUtils.isEmpty(passwordController.getPassword())) {

                        isOpen = true;
                        ((BaseActivity) context).dialogGeneral = ((BaseActivity) context).popupDialogs.createAlertDialog(AppMessages.ERROR_EMPTY_PIN, AppMessages.ALERT_HEADING,
                                context, context.getString(R.string.ok), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        isOpen = false;
                                        ((BaseActivity) context).dialogGeneral.dismiss();
                                    }
                                }, false, PopupDialogs.Status.ERROR, false, null);

//                    userInput.setError(AppMessages.ERROR_EMPTY_FIELD);
                        return;
                    } else if (passwordController.getPassword().length() < 4) {
                        isOpen = true;
                        ((BaseActivity) context).dialogGeneral = ((BaseActivity) context).popupDialogs.createAlertDialog(AppMessages.PIN_LENGTH, AppMessages.ALERT_HEADING,
                                context, context.getString(R.string.ok), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        isOpen = false;
                                        ((BaseActivity) context).dialogGeneral.dismiss();
                                    }
                                }, false, PopupDialogs.Status.ERROR, false, null);

//                    userInput.setError(AppMessages.PIN_LENGTH);
                        return;
                    }


//                    if (TextUtils.isEmpty(txtPin.getText())) {
//                        txtPin.setError(AppMessages.ERROR_EMPTY_FIELD);
//                        return;
//                    } else if (txtPin.getText().toString().length() < 4) {
//                        txtPin.setError(AppMessages.PIN_LENGTH);
//                        return;
//                    }
                    verifyOtp(passwordController.getPassword(), verifyPinListener, mDialog, v);
                }
            });
            btnCancel.setVisibility(View.VISIBLE);
            btnCancel.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });
            btnResend.setVisibility(View.GONE);
        } else if (code != null && (code.equals(Constants.ErrorCodes.DEVICE_UPDATE_OTP_VERIFICATION) ||
                code.equals(Constants.ErrorCodes.DEVICE_UPDATE_OTP_RESEND) ||
                code.equals(Constants.ErrorCodes.DEVICE_UPDATE_OTP_REGENERATION) ||
                code.equals(Constants.ErrorCodes.DEVICE_UPDATE_OTP_ERROR))) {
//            txtPin.setVisibility(View.VISIBLE);
            btnRegenerate.setVisibility(View.GONE);
            btnOk.setVisibility(View.VISIBLE);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (TextUtils.isEmpty(passwordController.getPassword())) {
//                    userInput.setError(AppMessages.ERROR_EMPTY_FIELD);
                        isOpen = true;
                        ((BaseActivity) context).dialogGeneral = ((BaseActivity) context).popupDialogs.createAlertDialog(AppMessages.ERROR_EMPTY_PIN, AppMessages.ALERT_HEADING,
                                context, context.getString(R.string.ok), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        isOpen = false;
                                        ((BaseActivity) context).dialogGeneral.dismiss();
                                    }
                                }, false, PopupDialogs.Status.ERROR, false, null);
                        return;
                    } else if (passwordController.getPassword().length() < 4) {
                        isOpen = true;
                        ((BaseActivity) context).dialogGeneral = ((BaseActivity) context).popupDialogs.createAlertDialog(AppMessages.PIN_LENGTH, AppMessages.ALERT_HEADING,
                                context, context.getString(R.string.ok), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        isOpen = false;
                                        ((BaseActivity) context).dialogGeneral.dismiss();
                                    }
                                }, false, PopupDialogs.Status.ERROR, false, null);
//                    userInput.setError(AppMessages.PIN_LENGTH);
                        return;
                    }

//                    if (TextUtils.isEmpty(txtPin.getText())) {
//                        txtPin.setError(AppMessages.ERROR_EMPTY_FIELD);
//                        return;
//                    } else if (txtPin.getText().toString().length() < 4) {
//                        txtPin.setError(AppMessages.PIN_LENGTH);
//                        return;
//                    }
                    verifyOtp(passwordController.getPassword(), verifyPinListener, mDialog, v);
                }
            });
            btnCancel.setVisibility(View.VISIBLE);
            btnCancel.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });
            btnResend.setVisibility(View.VISIBLE);
            btnResend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verifyOtp(passwordController.getPassword(), verifyPinListener, mDialog, v);
                }
            });
        } else {

            mDialog.dismiss();
//          ((BaseActivity) context).popupDialogs.createAlertDialog(msg, AppMessages.ALERT_HEADING,
//                    context, context.getString(R.string.ok), new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            ((BaseActivity) context).popupDialogs.dismissDialogJS();
//                        }
//                    }, false, PopupDialogs.Status.ERROR, false, null);

            PopupDialogs.createAlertDialog(msg,
                    AppMessages.ALERT_HEADING, context, Status.SUCCESS);

        }
        return mDialog;
    }

    private static void goToLogin(OnCustomClickListener customeOnclickListner, Dialog dialog, View v) {
        dialog.dismiss();
        customeOnclickListner.onClick(v, null);
    }

    private void verifyOtp(String txtPin,
                           OnCustomClickListener customeOnclickListner, Dialog dialog,
                           View v) {

//        if (txtPin.getVisibility() == View.VISIBLE) {
//            if (TextUtils.isEmpty(txtPin.getText())) {
//                txtPin.setError(AppMessages.ERROR_EMPTY_FIELD);
//                return;
//            } else if (txtPin.getText().toString().length() < 4) {
//                txtPin.setError(AppMessages.PIN_LENGTH);
//                return;
//            }
//        }
        dialog.dismiss();
        customeOnclickListner.onClick(v, txtPin);
    }

    ///////////////////////////////////////////Below Popup Dialogs are copied from Soneri///////////////
    public static Dialog createAlertDialog(final String reason,
                                           final String title, final Context context,
                                           android.view.View.OnClickListener listener, Status status) {

        dialog = createDialog(context, status, false, R.layout.dialog_alert);

        TextView heading = (TextView) dialog.findViewById(R.id.textViewAlertHeading);
        TextView message = (TextView) dialog.findViewById(R.id.textViewMessage);

        heading.setText(title);
        message.setText(reason);

        Button btnCancel = (Button) dialog.findViewById(R.id.btnOK);
        btnCancel.setOnClickListener(listener);
        return dialog;
    }

    public static Dialog createAlertDialog(final String reason,
                                           final String title, final Context context,
                                           android.view.View.OnClickListener listener, String strBtnText, Status status) {

        dialog = createDialog(context, status, false, R.layout.dialog_alert);

        TextView heading = (TextView) dialog
                .findViewById(R.id.textViewAlertHeading);
        TextView message = (TextView) dialog.findViewById(R.id.textViewMessage);

        heading.setText(title);
        message.setText(reason);

        Button btnOk = (Button) dialog.findViewById(R.id.btnOK);
        btnOk.setText(strBtnText);
        btnOk.setOnClickListener(listener);
        return dialog;
    }

    public static Dialog createSuccessDialog(String strHeading, String strReason, Context context,
                                             android.view.View.OnClickListener listener, Status status) {

        dialog = createDialog(context, status, false, R.layout.dialog_success);

        Button btnOk = (Button) dialog.findViewById(R.id.btnOK);
        TextView textView = (TextView) dialog.findViewById(R.id.tvMessage);
        TextView tvHeading = (TextView) dialog.findViewById(R.id.tvHeading);
        tvHeading.setText(strHeading);
        textView.setText(strReason);
        btnOk.setOnClickListener(listener);
        return dialog;
    }

    public static Dialog createAlertDialog(final String reason,
                                           final String title, final Context context, Status status) {
        dialog = createDialog(context, status, true, R.layout.dialog_alert);

        TextView heading = (TextView) dialog
                .findViewById(R.id.textViewAlertHeading);
        TextView message = (TextView) dialog.findViewById(R.id.textViewMessage);

        heading.setText(title);
        message.setText(reason);

        Button btnOk = (Button) dialog.findViewById(R.id.btnOK);
        btnOk.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!BaseActivity.isLogin) {
//                    dialog.dismiss();
//                } else if (BaseActivity.navigateToFirstScreen) {
//                    ((BaseActivity) context).onBackPressed();
//                } else {
                dialog.dismiss();
//                }
            }
        });
        return dialog;
    }

    public static void createAlertDialog(final String reason,
                                         final String title, final Context context, Status status,
                                         final Dialog parentDialog) {
        dialog = createDialog(context, status, true, R.layout.dialog_alert);

        TextView heading = (TextView) dialog
                .findViewById(R.id.textViewAlertHeading);
        TextView message = (TextView) dialog.findViewById(R.id.textViewMessage);

        heading.setText(title);
        message.setText(reason);

        Button btnOk = (Button) dialog.findViewById(R.id.btnOK);
        btnOk.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (parentDialog != null) {
                    dialog = parentDialog;
                    dialog.show();
                }
            }
        });
    }

    public static Dialog createConfirmationDialog(final String reason,
                                                  final String title, final Context context,
                                                  android.view.View.OnClickListener listner_yes) {

        dialog = createDialog(context, Status.INFO, false, R.layout.dialog_confirm);

        TextView heading = (TextView) dialog.findViewById(R.id.textViewAlertHeading);
        TextView message = (TextView) dialog.findViewById(R.id.textViewMessage);

        heading.setText(title);
        message.setText(reason);

        Button btyes = (Button) dialog.findViewById(R.id.btnYes);
        btyes.setOnClickListener(listner_yes);
        Button btnno = (Button) dialog.findViewById(R.id.btnNo);
        btnno.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }

    public static Dialog createConfirmationDialog(final String reason,
                                                  final String title, final Context context,
                                                  android.view.View.OnClickListener yesListener, String yesTitle,
                                                  Status status) {

        dialog = createDialog(context, status, false, R.layout.dialog_confirm);

        TextView heading = (TextView) dialog.findViewById(R.id.textViewAlertHeading);
        TextView message = (TextView) dialog.findViewById(R.id.textViewMessage);

        heading.setText(title);
        message.setText(reason);

        Button btnYes = (Button) dialog.findViewById(R.id.btnYes);
        btnYes.setText(yesTitle);
        btnYes.setOnClickListener(yesListener);
        Button btnNo = (Button) dialog.findViewById(R.id.btnNo);
        btnNo.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }

    public static Dialog createConfirmationDialog(final String reason,
                                                  final String title, final Context context,
                                                  android.view.View.OnClickListener yesListener, String yesTitle,
                                                  String noTitle, Status status) {

        dialog = createDialog(context, status, false, R.layout.dialog_confirm);

        TextView heading = (TextView) dialog.findViewById(R.id.textViewAlertHeading);
        TextView message = (TextView) dialog.findViewById(R.id.textViewMessage);

        heading.setText(title);
        message.setText(reason);

        Button btYes = (Button) dialog.findViewById(R.id.btnYes);
        btYes.setText(yesTitle);
        btYes.setOnClickListener(yesListener);

        Button btnNo = (Button) dialog.findViewById(R.id.btnNo);
        btnNo.setText(noTitle);
        btnNo.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }


    public static Dialog regOtp(final String reason,
                                final String title, final Context context, android.view.View.OnClickListener listener
            , Status status) {

        dialog = createDialog(context, status, false, R.layout.dialog_mpin);
        TextView headingText = (TextView) dialog.findViewById(R.id.heading_text);
        TextView tvTitle = (TextView) dialog.findViewById(R.id.textView1);

        headingText.setText(reason);
        tvTitle.setText(title);
        EditText editText = (EditText) dialog.findViewById(R.id.editTextDialogUserInput);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
        Button btnOk = (Button) dialog.findViewById(R.id.btnOK);
        btnOk.setOnClickListener(listener);

        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        return dialog;
    }

    public static Dialog createConfirmationDialog(final String reason,
                                                  final String title, final Context context,
                                                  android.view.View.OnClickListener yesListener, String yesTitle,
                                                  android.view.View.OnClickListener noListener, String noTitle,
                                                  Status status) {
        dialog = createDialog(context, status, false, R.layout.dialog_confirm);

        TextView heading = (TextView) dialog.findViewById(R.id.textViewAlertHeading);
        TextView message = (TextView) dialog.findViewById(R.id.textViewMessage);

        heading.setText(title);
        message.setText(reason);

        ScrollView scView = (ScrollView) dialog.findViewById(R.id.scroll_view);
        scView.fullScroll(ScrollView.FOCUS_UP);
        scView.smoothScrollTo(0, 0);
        scView.scrollTo(0, 0);

        Button btYes = (Button) dialog.findViewById(R.id.btnYes);
        btYes.setText(yesTitle);
        btYes.setOnClickListener(yesListener);
        Button btnNo = (Button) dialog.findViewById(R.id.btnNo);
        btnNo.setText(noTitle);
        btnNo.setOnClickListener(noListener);
        return dialog;
    }

    public static Dialog mpinPopupDialog(final Context context,
                                         final OnCustomClickListener verifyPinListener,
                                         final OnCustomClickListener generatePinListener) {
        dialog = createDialog(context, Status.LOCK, false, R.layout.dialog_mpin);

        final EditText txtPin = (EditText) dialog.findViewById(R.id.txtPin);

        final Button btnOk = (Button) dialog.findViewById(R.id.btnOK);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyPin(txtPin, verifyPinListener, dialog, context, v);
            }
        });

//        final Button btnGeneratePin = (Button) dialog
//                .findViewById(R.id.btnGeneratePin);
//        btnGeneratePin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                generatePinDialog(dialog, generatePinListener, context, v);
//            }
//        });

        final Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        return dialog;
    }

    private static void verifyPin(EditText txtPin,
                                  OnCustomClickListener customeOnclickListner, Dialog dialog,
                                  Context context, View v) {
        if (TextUtils.isEmpty(txtPin.getText())) {
            txtPin.setError(AppMessages.ERROR_EMPTY_FIELD);
            return;
        } else if (txtPin.length() < Constants.FieldLength.MPIN) {
            txtPin.setError(AppMessages.INVLAID_MPIN_LENGTH);
            return;
        }

        Utility.hideKeyboard(v, context);
        dialog.dismiss();
        customeOnclickListner.onClick(v, txtPin.getText().toString());
    }

    public static Dialog createRadioDialog(final String title,
                                           RadioGroup.OnCheckedChangeListener listner, final Context context,
                                           Status status, int radioId) {

        RadioGroup radioGroup = null;
        dialog = createDialog(context, status, true,
                R.layout.dialog_locator_layout);

        TextView heading = (TextView) dialog
                .findViewById(R.id.textViewAlertHeading);
        ImageButton btnCross = (ImageButton) dialog.findViewById(R.id.btnCross);
        btnCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        heading.setText(title);

        if (status.equals(Status.LOCATOR)) {
            radioGroup = (RadioGroup) dialog
                    .findViewById(R.id.rgLocatorOptions);
        } else if (status.equals(Status.DISTANCE)) {
            radioGroup = (RadioGroup) dialog
                    .findViewById(R.id.rgDistanceOptions);
        }

        radioGroup.setVisibility(View.VISIBLE);
        radioGroup.setOnCheckedChangeListener(listner);
        radioGroup.check(radioId);
        return dialog;
    }

    public static Dialog conventionalAccountOpeningDialog(final Context context, final OnCustomClickListener clickListener) {

        dialog = createDialog(context, ALERT, false, R.layout.dialog_conventional_account_opening);

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

    private static Dialog createDialog(final Context context, Status status,
                                       Boolean cancel, int dialogId) {
        dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar) {

        };

//        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(R.color.dialog_background));
        dialog.setContentView(dialogId);
        dialog.setCancelable(cancel);

//        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        AppCompatImageView headerImage = (AppCompatImageView) dialog.findViewById(R.id.imageViewAlert);
        RelativeLayout layoutHeader = (RelativeLayout) dialog.findViewById(R.id.layoutHeader);

        if (status != null) {
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

    public static void dialogDismiss() {
        dialog.dismiss();
    }
}