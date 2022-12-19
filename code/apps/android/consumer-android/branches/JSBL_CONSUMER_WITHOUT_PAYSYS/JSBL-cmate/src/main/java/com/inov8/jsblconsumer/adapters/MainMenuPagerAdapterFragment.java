package com.inov8.jsblconsumer.adapters;

import android.os.Bundle;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.CategoryMenuActivity;
import com.inov8.jsblconsumer.model.CategoryModel;

import com.inov8.jsblconsumer.util.Constants;

import java.util.ArrayList;

/**
 * Created by ALI REHAN on 10/10/2017.
 */

public class MainMenuPagerAdapterFragment extends Fragment {
    private View view;
    private ArrayList<CategoryModel> listCategories;

    private Bundle bundle;
    public Integer[] mThumbIds;

    public MainMenuPagerAdapterFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_menu, container, false);
        if (getArguments() != null) {
            bundle = getArguments();
            listCategories = (ArrayList<CategoryModel>) getArguments().getSerializable(Constants.IntentKeys.LIST_CATEGORIES);
            int position = getArguments().getInt(Constants.IntentKeys.MENU_ITEM_POS);
        }
        loadImages();
        populateMenu();
        return view;
    }

    private void populateMenu() {


        AppCompatImageView ivFirst = (AppCompatImageView) view.findViewById(R.id.ivFirst);
        AppCompatImageView ivSecond = (AppCompatImageView) view.findViewById(R.id.ivSecond);
        AppCompatImageView ivThird = (AppCompatImageView) view.findViewById(R.id.ivThird);
        TextView tvFirst = (TextView) view.findViewById(R.id.tvFirst);
        TextView tvSecond = (TextView) view.findViewById(R.id.tvSecond);
        TextView tvThird = (TextView) view.findViewById(R.id.tvThird);


        if (mThumbIds.length == 1) {
            ivFirst.setBackgroundResource(mThumbIds[0]);
            tvFirst.setText(listCategories.get(0).getName());

        } else if (mThumbIds.length == 2) {
            ivFirst.setBackgroundResource(mThumbIds[0]);
            ivSecond.setBackgroundResource(mThumbIds[1]);
            tvFirst.setText(listCategories.get(0).getName());
            tvSecond.setText(listCategories.get(1).getName());
        } else if (mThumbIds.length == 3) {
            ivFirst.setBackgroundResource(mThumbIds[0]);
            ivSecond.setBackgroundResource(mThumbIds[1]);
            ivThird.setBackgroundResource(mThumbIds[2]);
            tvFirst.setText(listCategories.get(0).getName());
            tvSecond.setText(listCategories.get(1).getName());
            tvThird.setText(listCategories.get(2).getName());
        }


//        ivFirst.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), SubMenuActivity.class);
//                intent.putExtras(bundle);
//                intent.putExtra(Constants.IntentKeys.LIST_CATEGORIES, listCategories);
//                intent.putExtra(Constants.IntentKeys.MENU_ITEM_POS, 0);
//                startActivity(intent);
//            }
//        });
//
//
//        ivSecond.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), SubMenuActivity.class);
//                intent.putExtras(bundle);
//                intent.putExtra(Constants.IntentKeys.LIST_CATEGORIES, listCategories);
//                intent.putExtra(Constants.IntentKeys.MENU_ITEM_POS, 1);
//                startActivity(intent);
//            }
//        });
//
//
//        ivThird.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), SubMenuActivity.class);
//                intent.putExtras(bundle);
//                intent.putExtra(Constants.IntentKeys.LIST_CATEGORIES, listCategories);
//                intent.putExtra(Constants.IntentKeys.MENU_ITEM_POS, 2);
//                startActivity(intent);
//            }
//        });
    }

    private void loadImages() {
        mThumbIds = new Integer[listCategories.size()];
        for (int i = 0; i < listCategories.size(); i++) {
            if (listCategories.get(i).getIcon().equals("")) {
                mThumbIds[i] = getContext().getResources().getIdentifier(
                        "main_icon_js_placeholder", "drawable",
                        getContext().getPackageName());
            } else {
                mThumbIds[i] = getContext().getResources().getIdentifier(
                        listCategories.get(i).getIcon()
                                .replace(".png", ""), "drawable",
                        getContext().getPackageName());
            }
        }
    }
}



