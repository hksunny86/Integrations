package com.inov8.jsblconsumer.adapters;


import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.inov8.jsblconsumer.model.CategoryModel;
import com.inov8.jsblconsumer.util.Constants;

import java.util.ArrayList;


/**
 * Created by ALI REHAN on 10/10/2017.
 */

public class MainMenuViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<CategoryModel> listAccountGroupModel = new ArrayList<>();
    private Bundle bundle, mBundle;
    private ArrayList<CategoryModel> categoryModelArrayList;

    public MainMenuViewPagerAdapter(FragmentManager manager, ArrayList<CategoryModel> listAccountGroupModel, Bundle bundle) {
        super(manager);
        this.listAccountGroupModel = listAccountGroupModel;
        this.bundle = new Bundle();
        this.bundle = bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public Fragment getItem(int position) {
        MainMenuPagerAdapterFragment mainMenuPagerAdapterFragment = new MainMenuPagerAdapterFragment();

        mBundle = new Bundle();
        categoryModelArrayList = new ArrayList<>();

        if (position + 1 == getCount()) {
            int left = listAccountGroupModel.size() - (position * 3);
            for (int i = position * 3; i < left + (position * 3); i++) {
                categoryModelArrayList.add(listAccountGroupModel.get(i));
            }
        } else {
            for (int i = position * 3; i < (position + 1) * 3; i++) {
                categoryModelArrayList.add(listAccountGroupModel.get(i));
            }
        }

        mBundle.putSerializable(Constants.IntentKeys.LIST_CATEGORIES, categoryModelArrayList);
        mBundle.putInt(Constants.IntentKeys.MENU_ITEM_POS, position);
        mainMenuPagerAdapterFragment.setArguments(mBundle);
        return mainMenuPagerAdapterFragment;

    }

    @Override
    public int getCount() {

        int count = listAccountGroupModel.size() % 3;
        if (count > 0) {
            return (listAccountGroupModel.size() / 3) + 1;
        } else {

            return listAccountGroupModel.size() / 3;
        }

//        return listAccountGroupModel.size();
//        return 0;
    }


//    @Override
//    public CharSequence getPageTitle(int position) {
//        return listAccountGroupModel.get(position).getAccountGroup();
//    }
}
