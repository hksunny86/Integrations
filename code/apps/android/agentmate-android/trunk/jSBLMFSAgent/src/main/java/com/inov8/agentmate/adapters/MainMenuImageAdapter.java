package com.inov8.agentmate.adapters;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.content.res.AppCompatResources;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.inov8.agentmate.activities.BOPCardIssuanceReIssuance;
import com.inov8.agentmate.activities.CategoryMenuActivity;
import com.inov8.agentmate.activities.SubMenuAcitivity;
import com.inov8.agentmate.activities.cashIn.CashInInputActivity;
import com.inov8.agentmate.activities.cashOut.CashOutInputActivity;
import com.inov8.agentmate.activities.collection.CollectionPaymentInputActivity;
import com.inov8.agentmate.activities.debitCard.DebitCardIssuance;
import com.inov8.agentmate.activities.dormancy.DormancyActivity;
import com.inov8.agentmate.activities.hra.HraRegistrationActivity1;
import com.inov8.agentmate.activities.openAccount.KhidmatCardRegistrationActivity;
import com.inov8.agentmate.activities.openAccount.OpenAccountBvsActivity;
import com.inov8.agentmate.activities.retailPayment.RetailPaymentInputActivity;
import com.inov8.agentmate.model.CategoryModel;
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;
import com.inov8.jsblmfs.R;

public class MainMenuImageAdapter extends BaseAdapter {
    private Context mContext;
    private Object[] objects = new Object[10];
    public int counter = 0;
    public boolean flag = false;
    private Bundle bundle;

    private ViewHolder holder = null;

    public MainMenuImageAdapter(Context c) {
        mContext = c;
        bundle = new Bundle();

        loadImages();
    }

    private void loadImages() {
        mThumbIds = new Integer[ApplicationData.listCategories.size()];
        for (int i = 0; i < ApplicationData.listCategories.size(); i++) {
            mThumbIds[i] = mContext.getResources().getIdentifier(
                    ApplicationData.listCategories.get(i).getIcon()
                            .replace(".png", ""), "drawable",
                    mContext.getPackageName());
        }
    }

    public int getCount() {
        return ApplicationData.listCategories.size();
    }

    public Object getItem(int position) {
        return objects[position];
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            final int pos = position;

            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.main_menu_grid_row, parent, false);

                holder = new ViewHolder();

                holder.image = (ImageView) convertView
                        .findViewById(R.id.menu_item_imageview);

                holder.text = (TextView) convertView
                        .findViewById(R.id.menu_item_textview);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.image.setImageDrawable(AppCompatResources.getDrawable(mContext, mThumbIds[position]));
            //holder.image.setImageResource(mThumbIds[position]);

            // holder.text.setText(mThumbText[position]);

            holder.image.setOnTouchListener(new OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                public boolean onTouch(View arg0, MotionEvent arg1) {

                    if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
                        ((ImageView) arg0).setColorFilter
                                (ContextCompat.getColor(mContext, R.color.main_menu_image_pressed));
                        //((ImageView) arg0)
                        //		.setImageResource(mThumbIds_hover[pos]);
                        return true;
                    }

                    if (arg1.getAction() == MotionEvent.ACTION_CANCEL) {
                        ((ImageView) arg0).setColorFilter
                                (ContextCompat.getColor(mContext, R.color.main_menu_image_normal));
                        //((ImageView) arg0).setImageResource(mThumbIds[pos]);
                    }

                    if (arg1.getAction() == MotionEvent.ACTION_UP) {
                        counter = counter + 1;
                        flag = true;
                        ((ImageView) arg0).setColorFilter
                                (ContextCompat.getColor(mContext, R.color.main_menu_image_normal));

//                        ((ImageView) arg0).setImageResource(mThumbIds[pos]);

                        if (flag) {
                            launchDynamicActivity(pos,
                                    ApplicationData.listCategories);
                        }
                        return false;
                    }
                    return true;
                }

            });
        } catch (Exception ex) {
            ex.getMessage();
            ex.printStackTrace();
        }

        return convertView;
    }

    private void launchDynamicActivity(final int position,
                                       final ArrayList<CategoryModel> listCategories) {
        Intent intent = null;

        CategoryModel category = listCategories.get(position);

        // if this is a product
        if (category.getIsProduct().equals("1")) {
            byte flowId = Byte.parseByte(category.getProductList().get(0)
                    .getFlowId());
            bundle.putByte(Constants.IntentKeys.FLOW_ID, flowId);

            switch (flowId) {
                case Constants.FLOW_ID_CASH_IN:
                    intent = new Intent(mContext, CashInInputActivity.class);
                    break;

                case Constants.FLOW_ID_BOP_CARD_ISSUANCE_RE_ISSUANCE:
                    intent = new Intent(mContext, BOPCardIssuanceReIssuance.class);
                    break;

                case Constants.FLOW_ID_HRA_REGISTRATION:
                    intent = new Intent(mContext, HraRegistrationActivity1.class);
                    break;

                case Constants.FLOW_ID_CASH_OUT_BY_IVR:
                    intent = new Intent(mContext, CashOutInputActivity.class);
                    break;

                case Constants.FLOW_ID_RETAIL_PAYMENT:
                    intent = new Intent(mContext, RetailPaymentInputActivity.class);
                    break;
                case Constants.FLOW_ID_DEBIT_CARD:
                    intent = new Intent(mContext,DebitCardIssuance.class);
                    break;
                case Constants.FLOW_ID_DORMANCY:
                    intent = new Intent(mContext, DormancyActivity.class);
                    break;
                case Constants.FLOW_ID_COLLECTION_PAYMENT:
                    intent = new Intent(mContext, CollectionPaymentInputActivity.class);
                    break;

                case Constants.FLOW_ID_HRA:
                    intent = new Intent(mContext, HraRegistrationActivity1.class);
                    break;

                case Constants.FLOW_ID_L0_L1:
                    intent = new Intent(mContext, OpenAccountBvsActivity.class);
                    break;

                    case Constants.FLOW_ID_BOP_KHIDMAT_CARD_REGISTRATION:
                    intent = new Intent(mContext, KhidmatCardRegistrationActivity.class);
                    break;

                case Constants.FLOW_ID_ACCOUNT_OPENING:
                    intent = new Intent(mContext,
                            OpenAccountBvsActivity.class);
                    intent.putExtra(Constants.IntentKeys.IS_RECEIVE_CASH,
                            Constants.IS_NOT_RECEIVE_CASH);

                    if (bundle != null) {
                        intent.putExtras(bundle);
                    }
                    break;

                case Constants.FLOW_ID_HRA_CASH_WITH_DRAWAL:
                    intent = new Intent(mContext,
                            CashOutInputActivity.class);
                    intent.putExtra(Constants.IntentKeys.CASH_OUT_TYPE,
                            Constants.CASH_OUT_BY_IVR);
                    break;

                default: {
                    Toast.makeText(mContext,
                            "Feature not implemented",
                            Toast.LENGTH_SHORT).show();
                    counter = 0;
                }
                break;
            }

            if (listCategories.get(position) != null &&
                    listCategories.get(position).getProductList() != null && intent != null)
                intent.putExtra(Constants.IntentKeys.PRODUCT_MODEL, listCategories
                        .get(position).getProductList().get(0));

            // if categories
        } else if (category.getCategoryList() != null
                && category.getCategoryList().size() > 0) {
            intent = new Intent(mContext, CategoryMenuActivity.class);
            intent.putExtra(Constants.IntentKeys.CATEGORY_MODEL,
                    category);
            // if multiple products
        } else if (category.getProductList() != null
                && category.getProductList().size() >= 1) {
            intent = new Intent(mContext, SubMenuAcitivity.class);
            intent.putExtra(Constants.IntentKeys.CATEGORY_MODEL, category);
        }

        if (intent != null) {
            intent.putExtras(bundle);
            mContext.startActivity(intent);
        }
    }

    public Integer[] mThumbIds;
    //public Integer[] mThumbIds_hover;

    class ViewHolder {
        public ImageView image;
        public TextView text;
    }
}