package com.inov8.jsblconsumer.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.model.ProductModel;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> implements Filterable {
    private ArrayList<ProductModel> mArrayList;
    private ArrayList<ProductModel> mFilteredList;

    public DataAdapter(ArrayList<ProductModel> arrayList) {
        mArrayList = arrayList;
        mFilteredList = arrayList;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder viewHolder, int i) {

        viewHolder.tv_name.setText(mFilteredList.get(i).getName());
//        viewHolder.tv_version.setText(mFilteredList.get(i).getVer());
//        viewHolder.tv_api_level.setText(mFilteredList.get(i).getApi());
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = mArrayList;
                } else {

                    ArrayList<ProductModel> filteredList = new ArrayList<>();

                    for (ProductModel androidVersion : mArrayList) {

                        if (androidVersion.getName().toLowerCase().contains(charString) || androidVersion.getName().toLowerCase().contains(charString) || androidVersion.getName().toLowerCase().contains(charString)) {

                            filteredList.add(androidVersion);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<ProductModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name,tv_version,tv_api_level;
        public ViewHolder(View view) {
            super(view);

            tv_name = (TextView)view.findViewById(R.id.tv_name);
            tv_version = (TextView)view.findViewById(R.id.tv_version);
            tv_api_level = (TextView)view.findViewById(R.id.tv_api_level);

        }
    }

}