package com.stiven.deptsboard.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stiven.deptsboard.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LendFragment extends Fragment {


    public LendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lend, container, false);
    }

}
