package com.technology.circles.apps.omanmade.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.technology.circles.apps.omanmade.R;
import com.technology.circles.apps.omanmade.activities_fragments.activity_home.fragments.Fragment_Directory;
import com.technology.circles.apps.omanmade.activities_fragments.activity_home.fragments.Fragment_Home;
import com.technology.circles.apps.omanmade.databinding.ProgressLoadMoreBinding;
import com.technology.circles.apps.omanmade.databinding.SpinnerRowBinding;
import com.technology.circles.apps.omanmade.models.SpinnerModel;

import java.util.List;

public class SpinnerLocationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int LOAD_DATA = 1;
    private final int LOAD_MORE = 2;


    private List<SpinnerModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;
    private int type;

    public SpinnerLocationAdapter(List<SpinnerModel> list, Context context, Fragment fragment,int type) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment = fragment;
        this.type = type;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == LOAD_DATA) {
            SpinnerRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.spinner_row, parent, false);
            return new MyHolder(binding);
        } else {
            ProgressLoadMoreBinding binding = DataBindingUtil.inflate(inflater, R.layout.progress_load_more, parent, false);
            return new LoadMoreHolder(binding);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyHolder) {

            MyHolder myHolder = (MyHolder) holder;
            myHolder.binding.setName(list.get(myHolder.getAdapterPosition()).getName());

           myHolder.itemView.setOnClickListener(view -> {

               SpinnerModel spinnerModel = list.get(myHolder.getAdapterPosition());
               if (fragment instanceof Fragment_Home)
               {
                   Fragment_Home fragment_home = (Fragment_Home) fragment;
                   if (type==1)
                   {
                       fragment_home.setCategoryData(spinnerModel,myHolder.getAdapterPosition());
                   }else if (type ==2)
                   {
                       fragment_home.setLocationData(spinnerModel,myHolder.getAdapterPosition());

                   }
               }else if (fragment instanceof Fragment_Directory)
               {
                   Fragment_Directory fragment_directory = (Fragment_Directory) fragment;

                   if (type==1)
                   {
                       fragment_directory.setCategoryData(spinnerModel,myHolder.getAdapterPosition());
                   }else if (type ==2)
                   {
                       fragment_directory.setLocationData(spinnerModel,myHolder.getAdapterPosition());

                   }
               }

           });

        } else if (holder instanceof LoadMoreHolder) {
            LoadMoreHolder myHolder = (LoadMoreHolder) holder;
            myHolder.binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
            myHolder.binding.progBar.setIndeterminate(true);

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public SpinnerRowBinding binding;

        public MyHolder(@NonNull SpinnerRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


    public class LoadMoreHolder extends RecyclerView.ViewHolder {
        private ProgressLoadMoreBinding binding;

        public LoadMoreHolder(ProgressLoadMoreBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position) == null) {
            return LOAD_MORE;
        } else {
            return LOAD_DATA;
        }
    }



}
