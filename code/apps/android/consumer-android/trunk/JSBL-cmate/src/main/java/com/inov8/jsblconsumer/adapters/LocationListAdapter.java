package com.inov8.jsblconsumer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.inov8.jsblconsumer.model.LocationModel;
import com.inov8.jsblconsumer.ui.components.OnItemCustomClickListener;
import com.inov8.jsblconsumer.R;

import java.util.List;

public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.MyViewHolder> {

    private Context context;
    private List<LocationModel> listLocations;
    private OnItemCustomClickListener onItemCustomClickListener;

    public LocationListAdapter(Context context,
                               List<LocationModel> listLocations, OnItemCustomClickListener onItemCustomClickListener) {
        super();
        this.context = context;
        this.listLocations = listLocations;
        this.onItemCustomClickListener = onItemCustomClickListener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvAddress, tvDistance;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.lblLocationName);
            tvAddress = (TextView) itemView.findViewById(R.id.lblAddress);
            tvDistance = (TextView) itemView.findViewById(R.id.lblDistance);

        }


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_location, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        LocationModel location = (LocationModel) listLocations.get(position);

        holder.tvName.setText(location.getName());
        holder.tvAddress.setText(location.getAdd());
        holder.tvDistance.setText(location.getDistance());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemCustomClickListener.onItemClicked(v, position);
            }
        });

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (listLocations != null) {
            return listLocations.size();
        }
        return 0;
    }

}