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
import com.dohro7.mobiledtrv2.utility.DateTimeUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DtrAdapter extends RecyclerView.Adapter<DtrAdapter.DtrViewHolder> {


    private List<DateLog> dtrLogs = new ArrayList<>();

    public DtrAdapter() {

    }


    //10:43:16 IN           Date 10
    //10:39:31 IN 10:40:11  Date 09

    /*
     * [0] = {"date":"Date 10","timeLogs":[{"time":"10:43:16","status":"IN"}]}
     *
     * */

    public void setList(List<TimeLogModel> list) {
        dtrLogs.clear();
        String temp_date = "";
        for (TimeLogModel timeLogModel : list) {

            if (!temp_date.equalsIgnoreCase(timeLogModel.date)) {
                temp_date = timeLogModel.date;
                DateLog dateLog = new DateLog();
                dateLog.date = timeLogModel.date;
                dtrLogs.add(0, dateLog);
            }
        }

        for (int i = 0; i < dtrLogs.size(); i++) {
            for (int j = 0; j < list.size(); j++) {
                if (dtrLogs.get(i).date.equalsIgnoreCase(list.get(j).date)) {
                    dtrLogs.get(i).timeLogModels.add(list.get(j));
                }

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
        /**
         TODO: Add color checker
         If status is uploaded change time color to green
         If it is undertime change it to red.
         NOTE: Top priority is the uploaded status.
         */
        int year = Integer.parseInt(dtrLogs.get(position).date.split("-")[0]);
        int month = Integer.parseInt(dtrLogs.get(position).date.split("-")[1]);
        int day = Integer.parseInt(dtrLogs.get(position).date.split("-")[2]);
        dtrViewHolder.dtr_date.setText(DateTimeUtility.getCurrentDateString(year, month, day));

        Log.e("Date", dtrLogs.get(position).date);
        clearViews(dtrViewHolder);

        List<TimeLogModel> list = dtrLogs.get(position).timeLogModels;
        for (TimeLogModel timeLogModel : list) {
            if (timeLogModel.status.equalsIgnoreCase("IN") && timeLogModel.getHour() < 12) {
                if (timeLogModel.getHour() > 7 && timeLogModel.getMinutes() > 0)
                    dtrViewHolder.am_in.setTextColor(Color.RED);
                dtrViewHolder.am_in.setText(timeLogModel.time);

                if (timeLogModel.uploaded == 1) {
                    dtrViewHolder.am_in.setTextColor(Color.GREEN);
                }
                continue;
            }
            if (timeLogModel.status.equalsIgnoreCase("OUT") && timeLogModel.getHour() < 17 &&
                    dtrViewHolder.am_out.getText().toString().isEmpty() && !dtrViewHolder.am_in.getText().toString().isEmpty()) {
                if (timeLogModel.getHour() < 12) dtrViewHolder.am_out.setTextColor(Color.RED);
                dtrViewHolder.am_out.setText(timeLogModel.time);

                if (timeLogModel.uploaded == 1) {
                    dtrViewHolder.am_out.setTextColor(Color.GREEN);
                }

                continue;
            }
            if (timeLogModel.status.equalsIgnoreCase("IN") && timeLogModel.getHour() > 11) {
                if (timeLogModel.getHour() > 12 && timeLogModel.getMinutes() > 0)
                    dtrViewHolder.pm_in.setTextColor(Color.RED);
                dtrViewHolder.pm_in.setText(timeLogModel.time);

                if (timeLogModel.uploaded == 1) {
                    dtrViewHolder.pm_in.setTextColor(Color.GREEN);
                }
                continue;
            }
            if (timeLogModel.status.equalsIgnoreCase("OUT")) {
                if (timeLogModel.getHour() < 17) dtrViewHolder.pm_out.setTextColor(Color.RED);
                dtrViewHolder.pm_out.setText(timeLogModel.time);

                if (timeLogModel.uploaded == 1) {
                    dtrViewHolder.pm_out.setTextColor(Color.GREEN);
                }
                continue;
            }
        }
    }

    @Override
    public int getItemCount() {
        return dtrLogs.size();
    }

    public void clearViews(DtrViewHolder dtrViewHolder) {
        dtrViewHolder.am_in.setText("");
        dtrViewHolder.am_out.setText("");
        dtrViewHolder.pm_in.setText("");
        dtrViewHolder.pm_out.setText("");

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
