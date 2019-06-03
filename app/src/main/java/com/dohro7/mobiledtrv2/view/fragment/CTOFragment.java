package com.dohro7.mobiledtrv2.view.fragment;

import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.dohro7.mobiledtrv2.R;
import com.dohro7.mobiledtrv2.adapter.CtoAdapter;
import com.dohro7.mobiledtrv2.model.CtoModel;
import com.dohro7.mobiledtrv2.viewmodel.CtoViewModel;

import java.util.List;

public class CTOFragment extends Fragment {

    private RecyclerView recyclerView;
    private CtoAdapter ctoAdapter;
    private CtoViewModel ctoViewModel;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ctoViewModel = ViewModelProviders.of(this).get(CtoViewModel.class);
        ctoAdapter = new CtoAdapter();
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.cto_fragment_layout, container, false);
        recyclerView = view.findViewById(R.id.cto_recycler_view);
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
                CtoModel ctoModel = ctoViewModel.getListLiveData().getValue().get(viewHolder.getAdapterPosition());
                Toast.makeText(getContext(), ctoModel.inclusive_date + " CTO deleted", Toast.LENGTH_SHORT).show();
                ctoViewModel.deleteCto(ctoModel);

            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(ctoAdapter);

        ctoViewModel.getListLiveData().observe(this, new Observer<List<CtoModel>>() {
            @Override
            public void onChanged(List<CtoModel> ctoModels) {
                ctoAdapter.setList(ctoModels);
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
            CtoModel ctoModel = new CtoModel(0, "");
            ctoViewModel.insertCto(ctoModel);
            Log.e("Insert", "CTO");
        }
        return super.onOptionsItemSelected(item);
    }
}
