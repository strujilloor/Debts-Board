package com.stiven.deptsboard.view.fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.stiven.deptsboard.R;
import com.stiven.deptsboard.model.Debt;
import com.stiven.deptsboard.view.adapter.MyAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BorrowFragment extends Fragment {

    private static final String DEPT_NODE = "Debts";
    FloatingActionButton fab;

    private RecyclerView recyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private DatabaseReference databaseReference;

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
        ArrayList<Debt> debts = new ArrayList<>();
        debts.add(new Debt("Jason","300.000"));
        debts.add(new Debt("Carolina","200.000"));
        mAdapter = new MyAdapter(debts, R.layout.cardview_debt, this.getActivity());
        recyclerView.setAdapter(mAdapter);

        //real-time
        FirebaseDatabase.getInstance().setPersistenceEnabled(true); //Habilitar la persistencia de Firebase cuando este sin internet
        databaseReference = FirebaseDatabase.getInstance().getReference(); // estamos obteniendo: debts-board

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean wrapInScrollView = false;
                new MaterialDialog.Builder(getContext())
                        .title("Add Debt")
                        .customView(R.layout.custom_dialog, wrapInScrollView)
                        .positiveText("Add")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                View view = dialog.getCustomView();
                                EditText name_dialog = (EditText) view.findViewById(R.id.name_dialog);
                                EditText amount_dialog = (EditText) view.findViewById(R.id.amount_dialog);

                                String name = name_dialog.getText().toString();
                                String amount = amount_dialog.getText().toString();

//                                createDebt(name, amount);

                                mAdapter.addToList(new Debt(name, amount));

                                Toast.makeText(getActivity(), "Added [" + name +","+amount+"]",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
                showDepts();
            }
        });

        return view;
    }

//    public void createDebt(String name, String amount){
//        Debt debt = new Debt(databaseReference.push().getKey(), name, amount);
//        databaseReference.child(DEPT_NODE)
//                .child(debt.getId())
//                .setValue(debt);
//    }

    public void showDepts(){
        mAdapter.notifyDataSetChanged();
    }

}
