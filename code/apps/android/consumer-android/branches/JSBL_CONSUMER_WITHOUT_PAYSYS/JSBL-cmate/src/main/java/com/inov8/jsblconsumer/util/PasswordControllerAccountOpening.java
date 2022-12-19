package com.inov8.jsblconsumer.util;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.inov8.jsblconsumer.R;

import java.util.ArrayList;

public class PasswordControllerAccountOpening {

    private ArrayList<EditText> etList;
    private boolean isAlphaNumeric = false;
    private TextView tv_error;
    private boolean viewPasswordOperations = false;
    private boolean finalActionNext = false;
    private Context context;
    private EditText fPin;
    private EditText editText;
    private ImageButton btnViewPass;

    private View view;

    private InputFilter filter = new InputFilter() {
        public CharSequence filter(CharSequence src, int start,
                                   int end, Spanned dst, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                if (!"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".contains(src.charAt(i) + "")) {
                    return "";
                }
            }
            return null;
        }
    };

    public PasswordControllerAccountOpening(View v, boolean finalActionNext, boolean isAlphaNumeric, Context context) {

        this.context=context;
        view=v;
        etList = new ArrayList<>();
        editText = (EditText) v.findViewById(R.id.tv_pass4);
        etList.add((EditText) v.findViewById(R.id.tv_pass1));
        etList.add((EditText) v.findViewById(R.id.tv_pass2));
        etList.add((EditText) v.findViewById(R.id.tv_pass3));
        etList.add((EditText) v.findViewById(R.id.tv_pass4));
        etList.add((EditText) v.findViewById(R.id.tv_pass5));

        for (int i = 0; i < etList.size(); i++) {

            fPin = etList.get(i);
//            fPin.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        }

        for (int i = 1; i < etList.size(); i++) {

//            etList.get(i).setEnabled(false);
        }


        this.isAlphaNumeric = isAlphaNumeric;
        this.finalActionNext = finalActionNext;

        changeType(1);
        enableControl();
//        setLabelColor();

        if ((ImageButton) v.findViewById(R.id.showPassword) != null) {
            btnViewPass = (ImageButton) v.findViewById(R.id.showPassword);
            btnViewPass.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    int action = event.getActionMasked();

                    viewPasswordOperations = true;

                    switch (action) {

                        case MotionEvent.ACTION_DOWN:

                            viewPasswordOperations = true;
                            changeType(2);
                            break;
                        case MotionEvent.ACTION_UP:

                            viewPasswordOperations = false;
                            changeType(1);
                            break;
                        case MotionEvent.ACTION_OUTSIDE:

                            viewPasswordOperations = false;
                            changeType(1);
                            break;
                    }

                    return v.onTouchEvent(event);
                }
            });
        }
    }

    private void changeType(int type) {

        InputFilter inputFilter = new InputFilter.LengthFilter(2);

        for (int i = 0; i < etList.size(); i++) {

            if (type == 1) {

                if (isAlphaNumeric) {
                    etList.get(i).setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    etList.get(i).setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                }

            } else {
                if (isAlphaNumeric) {
                    etList.get(i).setInputType(InputType.TYPE_CLASS_TEXT);
                } else {
                    etList.get(i).setInputType(InputType.TYPE_CLASS_NUMBER);
                }
            }

            if (i == etList.size() - 1 && finalActionNext) {
                etList.get(i).setImeOptions(EditorInfo.IME_ACTION_NEXT);
            }

            etList.get(i).setFilters(new InputFilter[]{filter, inputFilter});
        }
    }

    public String getPassword() {

        String password = "";

        for (int i = 0; i < etList.size(); i++) {

            password = password + etList.get(i).getText().toString();
        }

        return password;
    }

    private void enableControl() {

        for (int i = 0; i < etList.size(); i++) {

            final int position = i;

            etList.get(position).addTextChangedListener(new TextWatcher() {

                String beforeChange = "";

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    if (tv_error != null) {
                        tv_error.setVisibility(View.VISIBLE);
                    }

                    beforeChange = etList.get(position).getText().toString();
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {

                    if (!viewPasswordOperations) {

                        int pos = position;

                        if (TextUtils.isEmpty(s.toString()) && !TextUtils.isEmpty(beforeChange)) {

                            for (int j = pos; j >= 0; j--) {

                                if (j - 1 > -1) {

                                    if (!TextUtils.isEmpty(etList.get(j - 1).getText().toString())) {

                                        etList.get(j - 1).requestFocus();
                                        etList.get(j - 1).setSelection(etList.get(j - 1).length());
//                                        etList.get(j).setEnabled(false);
                                        break;
                                    }
                                } else {

                                    etList.get(j).requestFocus();
                                    etList.get(j).setSelection(etList.get(j).length());
//                                    etList.get(j).setEnabled(false);
                                }
                            }

                        } else {
                            if (s.length() == 2) {

                                int index = 0;

                                if (s.toString().contains(beforeChange)) {
                                    index = s.toString().indexOf(beforeChange);
                                }

                                if (index == 1) {
                                    etList.get(pos).setText(s.toString().replace(beforeChange, ""));
                                } else {
                                    StringBuilder sb = new StringBuilder(s.toString());
                                    etList.get(pos).setText(sb.deleteCharAt(1).toString());

                                    if (pos + 1 < etList.size()) {
                                        StringBuilder sb2 = new StringBuilder(s.toString());
                                        etList.get(pos + 1).setText(sb2.deleteCharAt(0).toString());
                                    }
                                }
                            } else if (s.length() == 1) {
                                if (pos + 1 < etList.size()) {

                                    pos = pos + 1;
                                    etList.get(pos).requestFocus();
//                                    etList.get(pos).setEnabled(true);
                                }  else if(!finalActionNext&&pos+1==etList.size()){
                                    Utility.hideKeyboard(etList.get(pos),context);
                                }

                            }

                            etList.get(pos).setSelection(etList.get(pos).length());
//                            etList.get(pos).setEnabled(true);
                        }
                    }
                }

            });
        }
    }

    public void setTv_error(String error) {
        editText.setError(error);
    }

    public void setOTP(String passcode) {
        if (passcode.length() == 5) {
            etList.get(0).setText(String.valueOf(passcode.charAt(0)));
            etList.get(1).setText(String.valueOf(passcode.charAt(1)));
            etList.get(2).setText(String.valueOf(passcode.charAt(2)));
            etList.get(3).setText(String.valueOf(passcode.charAt(3)));
            etList.get(4).setText(String.valueOf(passcode.charAt(4)));
        }
    }

    public void setRequestFocus() {
        etList.get(0).requestFocus();
    }

    public void resetPassword() {
        for (int i = 0; i < etList.size(); i++) {

            fPin = etList.get(i);
            fPin.setText("");
        }
    }

    public  void setEnable(){
        for (int i = 1; i < etList.size(); i++) {

//            etList.get(i).setEnabled(false);
        }


    }

    public void setHeading(String strHeading){
       TextView lbl_password =(TextView) view.findViewById(R.id.lbl_password);
       lbl_password.setText(strHeading);
    }
}
