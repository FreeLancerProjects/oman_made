package com.technology.circles.apps.omanmade.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.technology.circles.apps.omanmade.R;
import com.technology.circles.apps.omanmade.databinding.PakageRowBinding;
import com.technology.circles.apps.omanmade.models.PackageDataModel;

import java.util.List;

public class PackageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PackageDataModel.Packages> list;
    private Context context;
    private LayoutInflater inflater;

    public PackageAdapter(List<PackageDataModel.Packages> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        PakageRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.pakage_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;

        PackageDataModel.Packages model = list.get(position);
        myHolder.binding.setModel(model);

        if (model.getFeatures()!=null&&model.getFeatures().size()>0)
        {
            try {

                myHolder.binding.tvTitle1.setText(model.getFeatures().get(0).getTitle());
                myHolder.binding.tvDesc1.setText(model.getFeatures().get(0).getDesc());


                myHolder.binding.tvTitle2.setText(model.getFeatures().get(1).getTitle());
                myHolder.binding.tvDesc2.setText(model.getFeatures().get(1).getDesc());


                myHolder.binding.tvTitle3.setText(model.getFeatures().get(2).getTitle());
                myHolder.binding.tvDesc3.setText(model.getFeatures().get(2).getDesc());

            }catch (ArrayIndexOutOfBoundsException e)
            {

            }catch (Exception e){}
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public PakageRowBinding binding;

        public MyHolder(@NonNull PakageRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
