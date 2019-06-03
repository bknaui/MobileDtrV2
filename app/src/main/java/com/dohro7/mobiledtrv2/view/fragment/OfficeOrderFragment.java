package com.dohro7.mobiledtrv2.view.fragment;

import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dohro7.mobiledtrv2.R;
import com.dohro7.mobiledtrv2.adapter.LeaveAdapter;
import com.dohro7.mobiledtrv2.adapter.OfficeOrderAdapter;
import com.dohro7.mobiledtrv2.model.OfficeOrderModel;
import com.dohro7.mobiledtrv2.viewmodel.OfficeOrderViewModel;

import java.util.List;

public class OfficeOrderFragment extends Fragment {

    private RecyclerView recyclerView;
    private OfficeOrderViewModel officeOrderViewModel;
    private OfficeOrderAdapter officeOrderAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        officeOrderViewModel = ViewModelProviders.of(this).get(OfficeOrderViewModel.class);
        officeOrderAdapter = new OfficeOrderAdapter();
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.so_fragment_layout, container, false);
        recyclerView = view.findViewById(R.id.office_recycler_view);
        recyclerView.setLayoutManager(layoutManager);
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

        officeOrderViewModel.getListLiveData().observe(this, new Observer<List<OfficeOrderModel>>() {
            @Override
            public void onChanged(List<OfficeOrderModel> officeOrderModels) {
                officeOrderAdapter.setList(officeOrderModels);
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
        if (item.getItemId() == R.id.add) {
            OfficeOrderModel officeOrderModel = new OfficeOrderModel(0, "", "");
            officeOrderViewModel.insertOfficeOrder(officeOrderModel);
            Log.e("Insert", "SO");
        }
        return super.onOptionsItemSelected(item);
    }


}
