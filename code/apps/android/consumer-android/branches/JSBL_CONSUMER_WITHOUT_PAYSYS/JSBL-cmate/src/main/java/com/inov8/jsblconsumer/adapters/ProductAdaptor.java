package com.inov8.jsblconsumer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.activities.CategoryMenuActivity;
import com.inov8.jsblconsumer.model.ProductModel;
import com.inov8.jsblconsumer.util.Constants;

import java.util.HashMap;


public class ProductAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String[] values;
    private String[] logos;
    private HashMap<Integer, Object> optionsMap;

    private Context ctx;
    private int flowType;
    private OnItemClickListener demo;
    private AdapterView.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
         void onItemClick(ProductAdaptor.OriginalViewHolder btnCrossListener, int position, HashMap<Integer, Object> optionsMap,
                          int flowType, Context context);
    }

    public ProductAdaptor(Context context, String[] values, String[] logos, HashMap<Integer, Object> optionsMap, int flowType) {
        this.values = values;
        this.logos = logos;
        this.flowType = flowType;
        ctx = context;
        this.optionsMap = optionsMap;
        demo = new CategoryMenuActivity();
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image, icon;
        public TextView txtOption;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.image);
            icon = v.findViewById(R.id.icon);
            txtOption = v.findViewById(R.id.txtOption);
            lyt_parent = v.findViewById(R.id.cardView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_with_icon, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            Glide.with(ctx).load(Constants.PROD_IMG_URL + logos[position]).placeholder(R.drawable.bg).into(view.image);
            view.txtOption.setText(values[position]);
//            view.lyt_parent.setOnClickListener(view1 -> {
                demo.onItemClick(view ,position, optionsMap, flowType, ctx);
//            });
//           ((CategoryMenuActivity) ctx).onClickCalled(view ,position);
//            view.lyt_parent.setOnClickListener(view1 -> {
//                ((CategoryMenuActivity) ctx).onClickCalled(p, position);
//            });
        }
    }

    @Override
    public int getItemCount() {
        return values.length;
    }
}