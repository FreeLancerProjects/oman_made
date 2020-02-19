package com.technology.circles.apps.omanmade.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.technology.circles.apps.omanmade.R;
import com.technology.circles.apps.omanmade.databinding.OpenHourRowBinding;
import com.technology.circles.apps.omanmade.models.BusinessDataModel;

import java.util.List;

public class OpenHourAdapter extends RecyclerView.Adapter<OpenHourAdapter.MyHolder> {

    private List<BusinessDataModel.OpeningHourList> list;
    private Context context;
    private LayoutInflater inflater;
    private String lang;

    public OpenHourAdapter(List<BusinessDataModel.OpeningHourList> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);


    }

    @NonNull
    @Override
    public OpenHourAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        OpenHourRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.open_hour_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull OpenHourAdapter.MyHolder holder, int position) {

        holder.binding.setModel(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public OpenHourRowBinding binding;

        public MyHolder(@NonNull OpenHourRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
