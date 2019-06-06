package com.dohro7.mobiledtrv2.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dohro7.mobiledtrv2.R;
import com.dohro7.mobiledtrv2.model.OfficeOrderModel;

import java.util.ArrayList;
import java.util.List;

public class OfficeOrderAdapter extends RecyclerView.Adapter<OfficeOrderAdapter.OfficeViewHolder> {

    private List<OfficeOrderModel> list = new ArrayList<>();

    public OfficeOrderAdapter() {

    }

    public void setList(List<OfficeOrderModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public OfficeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.so_item_layout, viewGroup, false);
        return new OfficeOrderAdapter.OfficeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfficeViewHolder officeViewHolder, int i) {
        officeViewHolder.soNumber.setText("S0# "+list.get(i).so_no);
        officeViewHolder.inclusiveDate.setText(list.get(i).inclusive_date);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class OfficeViewHolder extends RecyclerView.ViewHolder {
        TextView soNumber;
        TextView inclusiveDate;

        public OfficeViewHolder(@NonNull View itemView) {
            super(itemView);
            soNumber = itemView.findViewById(R.id.so_no);
            inclusiveDate = itemView.findViewById(R.id.so_date);
        }
    }
}
