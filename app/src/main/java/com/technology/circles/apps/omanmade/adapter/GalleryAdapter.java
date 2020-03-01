package com.technology.circles.apps.omanmade.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.technology.circles.apps.omanmade.R;
import com.technology.circles.apps.omanmade.databinding.GalleryRowBinding;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> list;
    private Context context;
    private LayoutInflater inflater;

    public GalleryAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        GalleryRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.gallery_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;

        myHolder.binding.setUrl(list.get(position));



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public GalleryRowBinding binding;

        public MyHolder(@NonNull GalleryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }




}
