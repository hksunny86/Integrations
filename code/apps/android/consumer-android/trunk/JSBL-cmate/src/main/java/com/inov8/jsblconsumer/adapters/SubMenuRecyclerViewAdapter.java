package com.inov8.jsblconsumer.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.CategoryMenuActivity;
import com.inov8.jsblconsumer.activities.MainMenuActivity;
import com.inov8.jsblconsumer.activities.billPayment.BillPaymentActivity;
//import com.inov8.jsblconsumer.activities.debitCard.DebitCardIssuance;
import com.inov8.jsblconsumer.model.CategoryModel;
import com.inov8.jsblconsumer.util.Constants;

import java.util.ArrayList;

/**
 * Created by ALI REHAN on 12/11/2017.
 */

public class SubMenuRecyclerViewAdapter extends RecyclerView.Adapter<SubMenuRecyclerViewAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<CategoryModel> listCategories;
    private CategoryModel categoryModel;
    private MainMenuActivity mainMenuActivity;
    private Integer[] mThumbIds;

    public SubMenuRecyclerViewAdapter(Context context,
                                      ArrayList<CategoryModel> listCategories) {
        super();
        this.context = context;
        this.listCategories = listCategories;
        loadImages();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        categoryModel = listCategories.get(position);
        String strMenuIcon = "";
        int resId;

        try {


            strMenuIcon = listCategories.get(position).getIcon();


//            holder.appCompatImageView.setImageDrawable(AppCompatResources.getDrawable(context, mThumbIds[position]));

//            holder.appCompatImageView.setBackgroundResource(mThumbIds[position]);


            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    categoryModel = listCategories.get(position);
                    Intent intent;
                    if (listCategories.get(position).getId().equals("120")) {
//                        intent = new Intent(context, CategoryMenuActivity.class);
//                        mainMenuActivity.debitCardIssuenceCheck();
                        ((MainMenuActivity)context).debitCardIssuenceCheck(position);
                    } else {
                        intent = new Intent(context, CategoryMenuActivity.class);
                        intent.putExtra(Constants.IntentKeys.LIST_CATEGORIES, listCategories);
                        intent.putExtra(Constants.IntentKeys.MENU_ITEM_POS, position);
                        context.startActivity(intent);
                    }

                }
            });

            holder.textLogo.setText(categoryModel.getName());
//
//


            resId = context.getResources().getIdentifier(
                    strMenuIcon.replace(".png", ""),
                    "drawable", context.getPackageName());

            if (resId != 0) {
                holder.ivItemIcon.setBackgroundResource(resId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (listCategories != null) {
            return listCategories.size();
        }
        return 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textLogo, tvDetails, tvDebit, tvTransactionValueDate, tvBalance, tvCredit;
        LinearLayout linearLayout;
        AppCompatImageView ivItemIcon;


        public MyViewHolder(View itemView) {

            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.layout_details);
            textLogo = (TextView) itemView.findViewById(R.id.textLogo);
            ivItemIcon = (AppCompatImageView) itemView.findViewById(R.id.image);
            ivItemIcon.setBackgroundResource(R.drawable.main_icon_electricity);
        }

    }


    private void loadImages() {
        mThumbIds = new Integer[listCategories.size()];
        for (int i = 0; i < listCategories.size(); i++) {
            if (listCategories.get(i).getIcon().equals("")) {
                mThumbIds[i] = context.getResources().getIdentifier(
                        "main_icon_electricity", "drawable",
                        context.getPackageName());
            } else {

//                mThumbIds[i] = context.getResources().getIdentifier(
//                        "main_icon_electricity", "drawable",
//                        context.getPackageName());

                mThumbIds[i] = context.getResources().getIdentifier(
                        listCategories.get(i).getIcon()
                                .replace(".png", ""), "drawable",
                        context.getPackageName());
            }
        }
    }


}
