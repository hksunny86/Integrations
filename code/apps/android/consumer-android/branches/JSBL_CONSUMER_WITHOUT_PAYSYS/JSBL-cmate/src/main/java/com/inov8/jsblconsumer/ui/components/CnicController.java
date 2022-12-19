package com.inov8.jsblconsumer.ui.components;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.BaseActivity;

public class CnicController {

    private EditText cnic1;
    private EditText cnic2;
    private EditText cnic3;
    private TextView lbl_invalidCnic;
    private TextView lblCnic;


    private boolean showLabel;

    public CnicController(View v, boolean showLabel, boolean isActionDone, Context ctx) {

        this.showLabel = showLabel;

        cnic1 = (EditText) v.findViewById(R.id.tv_cnic1);
        cnic2 = (EditText) v.findViewById(R.id.tv_cnic2);
        cnic3 = (EditText) v.findViewById(R.id.tv_cnic3);
        ((BaseActivity)ctx).disableCopyPaste(cnic1);
        ((BaseActivity)ctx).disableCopyPaste(cnic2);
        ((BaseActivity)ctx).disableCopyPaste(cnic3);
//        cnic2.setEnabled(false);
//        cnic3.setEnabled(false);
        lbl_invalidCnic = (TextView) v.findViewById(R.id.lbl_invalidCnic);
        lblCnic = (TextView) v.findViewById(R.id.lblCnic);

        if(showLabel) {
            lblCnic.setVisibility(View.VISIBLE);
        }

        if(isActionDone) {
            cnic3.setImeOptions(EditorInfo.IME_ACTION_DONE);
        } else {
            cnic3.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        }

        //TOdo
//
//        if(!TextUtils.isEmpty(ApplicationData.customerInfo.getCnic()) &&
//                ApplicationData.customerInfo.getCnic().length() == 13) {
//
//            cnic1.setText(ApplicationData.customerInfo.getCnic().substring(0,5));
//            cnic2.setText(ApplicationData.customerInfo.getCnic().substring(5,12));
//            cnic3.setText(ApplicationData.customerInfo.getCnic().substring(12));
//
//            cnic1.setEnabled(false);
//            cnic2.setEnabled(false);
//            cnic3.setEnabled(false);
//
//            cnic1.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.password_box_grey));
//            cnic2.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.password_box_grey));
//            cnic3.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.password_box_grey));
//        }

        enableButtons();
    }

    private void enableButtons(){

        cnic1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                lbl_invalidCnic.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().length() == 5) {

                    if (cnic2.getText().toString().length() < 7) {
                        cnic2.requestFocus();
//                        cnic2.setEnabled(true);
                        cnic2.setSelection(cnic2.getText().toString().length());
                    }
                    else
                    if (cnic3.getText().toString().length() < 1) {
                        cnic3.requestFocus();
//                        cnic3.setEnabled(true);
                        cnic3.setSelection(cnic3.getText().toString().length());
                    }
                    else {
                        cnic2.requestFocus();
//                        cnic2.setEnabled(true);
                        cnic2.setSelection(cnic2.getText().toString().length());
                    }
                }

                if(s.toString().length() == 6) {

                    if(cnic2.getText().toString().length() < 7) {
                        String cnic = cnic2.getText().toString() + cnic1.getText().toString().substring(5);
                        cnic2.setText(cnic);
                        cnic2.setSelection(cnic.length());
                    }
                    else
                    if(cnic3.getText().toString().length() < 1) {
                        String cnic = cnic1.getText().toString().substring(5);
                        cnic3.setText(cnic);
                        cnic3.setSelection(cnic.length());
                    }
                    else{
                        cnic1.setSelection(cnic1.getText().toString().length());
                    }

                    cnic1.setText(cnic1.getText().toString().substring(0,5));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        cnic2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                lbl_invalidCnic.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().length() == 7) {
                    cnic3.requestFocus();
//                    cnic3.setEnabled(true);
                    cnic3.setSelection(cnic3.getText().toString().length());
                }
                else
                if (s.toString().length() < 1) {
                    cnic1.requestFocus();
//                    cnic2.setEnabled(false);
                    cnic1.setSelection(cnic1.getText().toString().length());
                }
                else
                if(s.toString().length() == 8) {

                    if(cnic3.getText().toString().length() < 1) {
                        String cnic = cnic2.getText().toString().substring(7);
                        cnic3.setText(cnic);
                        cnic3.setSelection(cnic.length());
                    }

                    cnic2.setText(cnic2.getText().toString().substring(0,7));
                }
                else{
                    cnic2.setSelection(cnic2.getText().toString().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        cnic3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lbl_invalidCnic.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().length() < 1) {
                    cnic2.requestFocus();
//                    cnic3.setEnabled(false);
                    cnic2.setSelection(cnic2.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public String getCnic() {

        return cnic1.getText().toString() + cnic2.getText().toString() + cnic3.getText().toString();
    }

    public boolean isValidCnic() {

        String cnic =  cnic1.getText().toString() + cnic2.getText().toString() + cnic3.getText().toString();

        if(cnic.length() == 13) {
            return true;
        }
        else {

            if(showLabel) {
                lbl_invalidCnic.setVisibility(View.VISIBLE);
            }

            return false;
        }
    }
    public void requestFocus(){
        cnic1.requestFocus();
    }

    public  void changeCnicActionn()
    {
        cnic3.setImeOptions(EditorInfo.IME_ACTION_NEXT);
    }
    public void setCnicLabel(String cnicLabel){
        lblCnic.setText(cnicLabel);
    }

    public void setEnable() {
        cnic1.setEnabled(false);
        cnic2.setEnabled(false);
        cnic3.setEnabled(false);
    }
}
