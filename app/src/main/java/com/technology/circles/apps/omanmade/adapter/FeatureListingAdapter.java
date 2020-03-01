package com.technology.circles.apps.omanmade.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.technology.circles.apps.omanmade.R;
import com.technology.circles.apps.omanmade.activities_fragments.activity_home.fragments.Fragment_Home;
import com.technology.circles.apps.omanmade.databinding.FeatureListingRowBinding;
import com.technology.circles.apps.omanmade.models.FeatureListingDataModel;

import java.util.List;

public class FeatureListingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<FeatureListingDataModel.FeatureModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;

    public FeatureListingAdapter(List<FeatureListingDataModel.FeatureModel> list, Context context, Fragment fragment) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment = fragment;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        FeatureListingRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.feature_listing_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;

        myHolder.binding.setModel(list.get(position));

        myHolder.itemView.setOnClickListener(view ->
        {
            FeatureListingDataModel.FeatureModel featureModel = list.get(myHolder.getAdapterPosition());
            if (fragment instanceof Fragment_Home)
            {
                Fragment_Home fragment_home = (Fragment_Home) fragment;
                fragment_home.setFeatureListingData(featureModel);
            }

        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public FeatureListingRowBinding binding;

        public MyHolder(@NonNull FeatureListingRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
