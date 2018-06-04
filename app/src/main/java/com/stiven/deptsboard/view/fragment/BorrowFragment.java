package com.stiven.deptsboard.view.fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.stiven.deptsboard.R;
import com.stiven.deptsboard.model.Dept;
import com.stiven.deptsboard.view.adapter.MyAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BorrowFragment extends Fragment {

    FloatingActionButton fab;

    private RecyclerView recyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public BorrowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_borrow, container, false);
        
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        // specify an MyAdapter
        ArrayList<Dept> depts = new ArrayList<>();
        depts.add(new Dept("Jason","300.000"));
        depts.add(new Dept("Carolina","200.000"));
        mAdapter = new MyAdapter(depts, R.layout.cardview_dept, this.getActivity());
        recyclerView.setAdapter(mAdapter);
        
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Add Button Pressed", Toast.LENGTH_SHORT).show();
            }
        });
        
        return view;
    }

    public void showDepts(){
        mAdapter.notifyDataSetChanged();
    }

}
