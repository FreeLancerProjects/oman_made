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
import com.technology.circles.apps.omanmade.databinding.FeaturedCategoryRowBinding;
import com.technology.circles.apps.omanmade.models.FeaturedCategoryDataModel;

import java.util.List;

public class FeaturedCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<FeaturedCategoryDataModel.FeaturedCategoryModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;

    public FeaturedCategoryAdapter(List<FeaturedCategoryDataModel.FeaturedCategoryModel> list, Context context, Fragment fragment) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment = fragment;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        FeaturedCategoryRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.featured_category_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;

        myHolder.binding.setModel(list.get(position));

        myHolder.itemView.setOnClickListener(view -> {
            if (fragment instanceof Fragment_Home)
            {

                Fragment_Home fragment_home = (Fragment_Home) fragment;
                fragment_home.setItemDataFeatureCategory(list.get(myHolder.getAdapterPosition()));
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public FeaturedCategoryRowBinding binding;

        public MyHolder(@NonNull FeaturedCategoryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
