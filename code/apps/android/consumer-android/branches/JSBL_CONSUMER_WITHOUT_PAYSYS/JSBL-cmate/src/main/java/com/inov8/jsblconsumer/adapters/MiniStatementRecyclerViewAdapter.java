package com.inov8.jsblconsumer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.recyclerview.widget.RecyclerView;

import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.model.TransactionModel;

import java.util.ArrayList;

public class MiniStatementRecyclerViewAdapter extends RecyclerView.Adapter<MiniStatementRecyclerViewAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<String> accountStatementModelArrayList;
    private String accountStatementModel;

    public MiniStatementRecyclerViewAdapter(Context context,
                                            ArrayList<String> accountStatementModelArrayList) {
        super();
        this.context = context;
        this.accountStatementModelArrayList = accountStatementModelArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_mini_statement, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        accountStatementModel = accountStatementModelArrayList.get(position);

        try {

            if (position == getItemCount() - 1) {
                holder.rlMiniItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                     //   Toast.makeText(context, "posation" + position, Toast.LENGTH_LONG).show();
                    }
                });
                holder.llFirst.setVisibility(View.GONE);
                holder.llSecond.setVisibility(View.GONE);
                holder.tvMore.setVisibility(View.VISIBLE);

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
        if (accountStatementModelArrayList != null) {
            return accountStatementModelArrayList.size();
        }
        return 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvMore, tvDetails, tvDebit, tvTransactionValueDate, tvBalance, tvCredit;
        LinearLayout llFirst;
        RelativeLayout llSecond;
        RelativeLayout rlMiniItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvMore = (TextView) itemView.findViewById(R.id.tvMore);
            llFirst = (LinearLayout) itemView.findViewById(R.id.llFirst);
            llSecond = (RelativeLayout) itemView.findViewById(R.id.llSecond);
            rlMiniItem = (RelativeLayout) itemView.findViewById(R.id.rlMiniItem);
        }


    }
}