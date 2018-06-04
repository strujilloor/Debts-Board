package com.stiven.deptsboard.view.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stiven.deptsboard.R;
import com.stiven.deptsboard.model.Dept;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<Dept> mDataset;
    private int resource;
    private Activity activity;

    public MyAdapter(ArrayList<Dept> myDataset, int resource, Activity activity) {
        this.mDataset = myDataset;
        this.resource = resource; // the layout that contains the cardview in this example
        this.activity = activity;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name_card;
        public TextView amount_card;

        public ViewHolder(View v) {
            super(v);
            name_card = (TextView) v.findViewById(R.id.name_card);
            amount_card = (TextView) v.findViewById(R.id.amount_card);
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Dept dept = mDataset.get(position);
        holder.name_card.setText(dept.getName());
        holder.amount_card.setText(dept.getAmount());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void addToList(Dept dept){
        mDataset.add(dept);
    }


}