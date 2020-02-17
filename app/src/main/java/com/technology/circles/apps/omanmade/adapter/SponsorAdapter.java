package com.technology.circles.apps.omanmade.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.technology.circles.apps.omanmade.R;
import com.technology.circles.apps.omanmade.databinding.SponsorRowBinding;
import com.technology.circles.apps.omanmade.models.SponsorsModel;

import java.util.List;

public class SponsorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SponsorsModel.Sponsors> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;

    public SponsorAdapter(List<SponsorsModel.Sponsors> list, Context context, Fragment fragment) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment = fragment;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        SponsorRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.sponsor_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;

        int pos = position % list.size();
        myHolder.binding.setModel(list.get(pos));



    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE/8;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public SponsorRowBinding binding;

        public MyHolder(@NonNull SponsorRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
