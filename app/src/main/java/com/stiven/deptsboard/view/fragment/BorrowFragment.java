package com.stiven.deptsboard.view.fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.stiven.deptsboard.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BorrowFragment extends Fragment {

    FloatingActionButton fab;

    public BorrowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_borrow, container, false);
        
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Add Button Pressed", Toast.LENGTH_SHORT).show();
            }
        });
        
        return view;
    }

}
