package com.technology.circles.apps.omanmade.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.technology.circles.apps.omanmade.R;
import com.technology.circles.apps.omanmade.activities_fragments.activity_home.fragments.Fragment_Directory;
import com.technology.circles.apps.omanmade.databinding.DirectoryRowBinding;
import com.technology.circles.apps.omanmade.models.DirectoryDataModel;

import java.util.List;

public class DirectoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DirectoryDataModel.DirectoryModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;

    public DirectoryAdapter(List<DirectoryDataModel.DirectoryModel> list, Context context, Fragment fragment) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment = fragment;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        DirectoryRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.directory_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;

        myHolder.binding.setModel(list.get(position));

        myHolder.itemView.setOnClickListener(view -> {
            if (fragment instanceof Fragment_Directory)
            {
                Fragment_Directory fragment_directory = (Fragment_Directory) fragment;
                DirectoryDataModel.DirectoryModel directoryModel = list.get(myHolder.getAdapterPosition());
                fragment_directory.setItemDirectoryData(directoryModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public DirectoryRowBinding binding;

        public MyHolder(@NonNull DirectoryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
