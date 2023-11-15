package com.trodev.wifibillmanageruser.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.trodev.wifibillmanageruser.R;
import com.trodev.wifibillmanageruser.models.SupportModels;


import java.util.ArrayList;

public class SupportAdapter extends RecyclerView.Adapter<SupportAdapter.MyHolder> {

    private Context context;
    private ArrayList<SupportModels> list;

    public SupportAdapter(Context context, ArrayList<SupportModels> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SupportAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.problem_item, parent, false);
        return new SupportAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SupportAdapter.MyHolder holder, int position) {

        SupportModels models = list.get(position);

        holder.nameTv.setText(models.getName());
        holder.useridTv.setText(models.getUser_id());
        holder.problemTv.setText(models.getProblem());
        holder.statusTv.setText(models.getStatus());
        holder.dateTv.setText(models.getDate() + " & " + models.getTime());

        holder.cardView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.slider));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView nameTv, problemTv, useridTv, statusTv, dateTv;
        CardView cardView;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            nameTv = itemView.findViewById(R.id.nameTv);
            problemTv = itemView.findViewById(R.id.problemTv);
            useridTv = itemView.findViewById(R.id.useridTv);
            dateTv = itemView.findViewById(R.id.dateTv);
            statusTv = itemView.findViewById(R.id.statusTv);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
