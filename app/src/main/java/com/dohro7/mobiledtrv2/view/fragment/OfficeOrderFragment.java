package com.dohro7.mobiledtrv2.view.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dohro7.mobiledtrv2.R;
import com.dohro7.mobiledtrv2.adapter.LeaveAdapter;
import com.dohro7.mobiledtrv2.adapter.OfficeOrderAdapter;
import com.dohro7.mobiledtrv2.model.LeaveModel;
import com.dohro7.mobiledtrv2.model.OfficeOrderModel;
import com.dohro7.mobiledtrv2.utility.DateTimeUtility;
import com.dohro7.mobiledtrv2.viewmodel.OfficeOrderViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.List;

public class OfficeOrderFragment extends Fragment {

    private RecyclerView recyclerView;
    private OfficeOrderViewModel officeOrderViewModel;
    private OfficeOrderAdapter officeOrderAdapter;
    private LinearLayoutManager layoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        officeOrderViewModel = ViewModelProviders.of(this).get(OfficeOrderViewModel.class);
        officeOrderAdapter = new OfficeOrderAdapter();
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.so_fragment_layout, container, false);
        recyclerView = view.findViewById(R.id.office_recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                OfficeOrderModel officeOrderModel = officeOrderViewModel.getListLiveData().getValue().get(viewHolder.getAdapterPosition());
                Toast.makeText(getContext(), "SO#: " + officeOrderModel.so_no + " deleted", Toast.LENGTH_SHORT).show();
                officeOrderViewModel.deleteOfficeOrder(officeOrderModel);

            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(officeOrderAdapter);

        view.findViewById(R.id.fab_so).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAddSoDialog();
            }
        });

        officeOrderViewModel.getListLiveData().observe(this, new Observer<List<OfficeOrderModel>>() {
            @Override
            public void onChanged(List<OfficeOrderModel> officeOrderModels) {
                officeOrderAdapter.setList(officeOrderModels);
            }
        });

        officeOrderViewModel.getMutableUploadError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (!s.equalsIgnoreCase("Successfully uploaded") && !s.equalsIgnoreCase("Nothing to upload")) {
                    Snackbar snackbar = Snackbar.make(view.findViewById(R.id.root), s, Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            officeOrderViewModel.uploadLogs();
                        }
                    });
                    snackbar.show();
                    return;
                }

                Snackbar snackbar = Snackbar.make(view.findViewById(R.id.root), s, Snackbar.LENGTH_SHORT);
                snackbar.setText(s);
                snackbar.show();
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.absence_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.upload) {
            officeOrderViewModel.uploadLogs();
        }
        return super.onOptionsItemSelected(item);
    }

    public void displayAddSoDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_add_so_1);
        final EditText dialogSoNo = dialog.findViewById(R.id.dialog_so_no);
        final TextView dialogSoFrom = dialog.findViewById(R.id.dialog_so_from);
        final TextView dialogSoTo = dialog.findViewById(R.id.dialog_so_to);

        final Calendar calendarFrom = Calendar.getInstance();
        final Calendar calendarTo = Calendar.getInstance();
        dialogSoFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dialogSoFrom.setText(year + "/" + DateTimeUtility.twoDigitFormat(month + 1) + "/" + DateTimeUtility.twoDigitFormat(dayOfMonth));
                        calendarTo.set(year, month, dayOfMonth);
                        //datePickerDialog.dismiss();
                    }
                }, calendarFrom.get(Calendar.YEAR), calendarFrom.get(Calendar.MONTH), calendarFrom.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });


        dialogSoTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dialogSoTo.setText(year + "/" + DateTimeUtility.twoDigitFormat(month + 1) + "/" + DateTimeUtility.twoDigitFormat(dayOfMonth));
                        //datePickerDialog.dismiss();
                    }
                }, calendarTo.get(Calendar.YEAR), calendarTo.get(Calendar.MONTH), calendarTo.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(calendarFrom.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        dialog.findViewById(R.id.dialog_btn_add_so).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String so_no = dialogSoNo.getText().toString();
                String inclusive_date = dialogSoFrom.getText().toString() + " - " + dialogSoTo.getText().toString();
                OfficeOrderModel officeOrderModel = new OfficeOrderModel(0, so_no, inclusive_date);
                officeOrderViewModel.insertOfficeOrder(officeOrderModel);
                dialog.dismiss();
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

}
