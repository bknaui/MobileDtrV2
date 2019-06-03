package com.dohro7.mobiledtrv2.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dohro7.mobiledtrv2.R;
import com.dohro7.mobiledtrv2.model.CtoModel;

import java.util.ArrayList;
import java.util.List;

public class CtoAdapter extends RecyclerView.Adapter<CtoAdapter.CtoViewHolder> {

    private List<CtoModel> list = new ArrayList<>();

    public CtoAdapter() {
    }

    public void setList(List<CtoModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public CtoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cto_item_layout, viewGroup, false);
        return new CtoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CtoViewHolder ctoViewHolder, int i) { //i = position
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class CtoViewHolder extends RecyclerView.ViewHolder {


        public CtoViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
