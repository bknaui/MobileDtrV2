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
import com.dohro7.mobiledtrv2.model.LeaveModel;
import com.dohro7.mobiledtrv2.viewmodel.LeaveViewModel;

import java.util.List;

public class LeaveFragment extends Fragment {
    private RecyclerView recyclerView;
    private LeaveViewModel leaveViewModel;
    private LeaveAdapter leaveAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        leaveAdapter = new LeaveAdapter();
        leaveViewModel = ViewModelProviders.of(this).get(LeaveViewModel.class);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.absence_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add) {
            LeaveModel leaveModel = new LeaveModel(0, "", "");
            leaveViewModel.insertLeave(leaveModel);
            Log.e("Insert", "Leave");
        }

        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.leave_fragment_layout, container, false);
        recyclerView = view.findViewById(R.id.leave_recycler_view);
        recyclerView.setLayoutManager(layoutManager);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                LeaveModel leaveModel = leaveViewModel.getListLiveData().getValue().get(viewHolder.getAdapterPosition());
                Toast.makeText(getContext(), leaveModel.type + " deleted", Toast.LENGTH_SHORT).show();
                leaveViewModel.deleteLeave(leaveModel);

            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(leaveAdapter);

        leaveViewModel.getListLiveData().observe(this, new Observer<List<LeaveModel>>() {
            @Override
            public void onChanged(List<LeaveModel> leaveModels) {
                leaveAdapter.setList(leaveModels);
            }
        });
        return view;
    }
}
