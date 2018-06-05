package com.stiven.deptsboard.view.adapter;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.stiven.deptsboard.R;
import com.stiven.deptsboard.model.Debt;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<Debt> mDataset;
    private int resource;
    private Activity activity;

    public MyAdapter(ArrayList<Debt> myDataset, int resource, Activity activity) {
        this.mDataset = myDataset;
        this.resource = resource; // the layout that contains the cardview in this example
        this.activity = activity;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name_card;
        public TextView amount_card;
        public CardView cardview;

        public ViewHolder(View v) {
            super(v);
            name_card = (TextView) v.findViewById(R.id.name_card);
            amount_card = (TextView) v.findViewById(R.id.amount_card);
            cardview = (CardView) v.findViewById(R.id.cardview_borrow);
        }

    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(this.resource, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Debt debt = mDataset.get(position);
        holder.name_card.setText(debt.getName());
        holder.amount_card.setText(debt.getAmount());

        holder.cardview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String id = debt.getId();
                mDataset.remove(position);
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                databaseReference.child("Debts").child(id).removeValue();

                Toast.makeText(activity, "Pressed", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void addToList(Debt debt){
        mDataset.add(debt);
    }


}