package com.technology.circles.apps.omanmade.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.technology.circles.apps.omanmade.R;

import java.util.List;

public class Gallery1Adapter extends PagerAdapter {
    private List<String> list;
    private Context context;

    public Gallery1Adapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.gallery_row,container,false);
        RoundedImageView image = view.findViewById(R.id.image);
        Picasso.with(context).load(Uri.parse(list.get(position))).into(image);

        container.addView(view);
        return view;

    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
