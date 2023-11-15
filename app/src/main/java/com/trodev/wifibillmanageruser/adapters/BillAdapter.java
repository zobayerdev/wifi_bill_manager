package com.trodev.wifibillmanageruser.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.trodev.wifibillmanageruser.R;
import com.trodev.wifibillmanageruser.activities.ReceiptGeneratorActivity;
import com.trodev.wifibillmanageruser.models.BillModels;

import java.util.ArrayList;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<BillModels> list;

    public BillAdapter(Context context, ArrayList<BillModels> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public BillAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bill_payment_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillAdapter.MyViewHolder holder, int position) {

        BillModels models = list.get(position);

        holder.nameTv.setText(models.getName());
        holder.packagesTv.setText(models.getPackages());
        holder.useridTv.setText(models.getUser_id());
        holder.dateTv.setText(models.getDate() + " & " +models.getTime());
        holder.monthTv.setText(models.getMonth() +" - "+ models.getYear());
        holder.mobileTv.setText(models.getMobile());


        holder.cardView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(),R.anim.slider));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ReceiptGeneratorActivity.class);

                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nameTv, packagesTv, useridTv, mobileTv, monthTv, dateTv ;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTv = itemView.findViewById(R.id.nameTv);
            packagesTv = itemView.findViewById(R.id.packagesTv);
            useridTv = itemView.findViewById(R.id.useridTv);
            mobileTv = itemView.findViewById(R.id.mobileTv);
            monthTv = itemView.findViewById(R.id.monthTv);
            dateTv = itemView.findViewById(R.id.dateTv);
            cardView = itemView.findViewById(R.id.cardView);

        }
    }
}