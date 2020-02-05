package com.technology.circles.apps.omanmade.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.technology.circles.apps.omanmade.R;
import com.technology.circles.apps.omanmade.databinding.SpinnerRowBinding;
import com.technology.circles.apps.omanmade.models.SpinnerModel;

import java.util.List;

public class SpinnerLocationAdapter extends BaseAdapter {
    private List<SpinnerModel> list;
    private Context context;


    public SpinnerLocationAdapter(List<SpinnerModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") SpinnerRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.spinner_row,viewGroup,false);
        binding.setName(list.get(i).getName());
        return binding.getRoot();
    }




}
