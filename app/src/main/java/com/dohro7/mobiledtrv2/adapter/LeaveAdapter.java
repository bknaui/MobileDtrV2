package com.dohro7.mobiledtrv2.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dohro7.mobiledtrv2.R;
import com.dohro7.mobiledtrv2.model.LeaveModel;
import com.dohro7.mobiledtrv2.model.OfficeOrderModel;

import java.util.ArrayList;
import java.util.List;

public class LeaveAdapter extends RecyclerView.Adapter<LeaveAdapter.LeaveViewHolder> {

    private List<LeaveModel> list = new ArrayList<>();

    public LeaveAdapter() {
    }

    public void setList(List<LeaveModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public LeaveViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.leave_item_layout, viewGroup, false);
        return new LeaveAdapter.LeaveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaveViewHolder officeViewHolder, int i) {
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class LeaveViewHolder extends RecyclerView.ViewHolder {
        public LeaveViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
