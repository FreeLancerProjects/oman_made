package com.technology.circles.apps.omanmade.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.technology.circles.apps.omanmade.R;
import com.technology.circles.apps.omanmade.activities_fragments.activity_home.fragments.Fragment_Industry;
import com.technology.circles.apps.omanmade.databinding.IndustrialAreaRow2Binding;
import com.technology.circles.apps.omanmade.models.IndustrialAreaDataModel;

import java.util.List;

public class IndustrialAreaAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<IndustrialAreaDataModel.IndustrialAreaModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;

    public IndustrialAreaAdapter2(List<IndustrialAreaDataModel.IndustrialAreaModel> list, Context context, Fragment fragment) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment = fragment;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        IndustrialAreaRow2Binding binding = DataBindingUtil.inflate(inflater, R.layout.industrial_area_row2, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;

        myHolder.binding.setModel(list.get(position));
        myHolder.itemView.setOnClickListener(view -> {
            if (fragment instanceof Fragment_Industry)
            {
                Fragment_Industry fragment_industry = (Fragment_Industry) fragment;
                fragment_industry.setItemData();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public IndustrialAreaRow2Binding binding;

        public MyHolder(@NonNull IndustrialAreaRow2Binding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
