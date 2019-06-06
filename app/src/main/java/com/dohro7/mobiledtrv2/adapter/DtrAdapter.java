package com.dohro7.mobiledtrv2.adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dohro7.mobiledtrv2.R;
import com.dohro7.mobiledtrv2.model.DateLog;
import com.dohro7.mobiledtrv2.model.TimeLogModel;

import java.util.ArrayList;
import java.util.List;

public class DtrAdapter extends RecyclerView.Adapter<DtrAdapter.DtrViewHolder> {


    private List<DateLog> dtrLogs = new ArrayList<>();

    public DtrAdapter() {

    }

    public void setList(List<TimeLogModel> list) {
        dtrLogs.clear();

        DateLog dateLog = new DateLog();
        for (int i = 0; i < list.size(); i++) {
            TimeLogModel timeLogModel = list.get(i);

            if (dateLog.date.isEmpty()) {
                dateLog.date = timeLogModel.date;
                dateLog.timeLogModels.add(timeLogModel);
                if (i == list.size() - 1) {
                    dtrLogs.add(dateLog);
                }
                continue;
            }

            if (timeLogModel.date.equalsIgnoreCase(dateLog.date)) {
                dateLog.timeLogModels.add(timeLogModel);
            } else {
                dtrLogs.add(dateLog);
                dateLog = new DateLog();
                dateLog.date = timeLogModel.date;
                dateLog.timeLogModels.add(timeLogModel);
            }

            if (i == list.size() - 1) {
                dtrLogs.add(dateLog);
                continue;
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DtrViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dtr_item_layout, viewGroup, false);
        return new DtrViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DtrViewHolder dtrViewHolder, int position) { //i = position
        dtrViewHolder.dtr_date.setText(dtrLogs.get(position).date);

        List<TimeLogModel> list = dtrLogs.get(position).timeLogModels;

        for (TimeLogModel timeLogModel : list) {
            if (timeLogModel.status.equalsIgnoreCase("IN") && timeLogModel.getHourTime() < 12) {
                dtrViewHolder.am_in.setText(timeLogModel.time);
                continue;
            }
            if (timeLogModel.status.equalsIgnoreCase("OUT") && timeLogModel.getHourTime() < 17 &&
                    dtrViewHolder.am_out.getText().toString().isEmpty() && !dtrViewHolder.am_in.getText().toString().isEmpty()) {
                dtrViewHolder.am_out.setText(timeLogModel.time);
                continue;
            }
            if (timeLogModel.status.equalsIgnoreCase("IN") && timeLogModel.getHourTime() > 11) {
                dtrViewHolder.pm_in.setText(timeLogModel.time);
                continue;
            }
            if (timeLogModel.status.equalsIgnoreCase("OUT")) {
                dtrViewHolder.pm_out.setText(timeLogModel.time);
                continue;
            }
        }
    }

    @Override
    public int getItemCount() {
        return dtrLogs.size();
    }

    class DtrViewHolder extends RecyclerView.ViewHolder {

        TextView dtr_date;
        TextView am_in;
        TextView am_out;
        TextView pm_in;
        TextView pm_out;

        public DtrViewHolder(@NonNull View itemView) {
            super(itemView);
            dtr_date = itemView.findViewById(R.id.dtr_date);
            am_in = itemView.findViewById(R.id.am_in_value);
            am_out = itemView.findViewById(R.id.am_out_value);
            pm_in = itemView.findViewById(R.id.pm_in_value);
            pm_out = itemView.findViewById(R.id.pm_out_value);
        }
    }
}
