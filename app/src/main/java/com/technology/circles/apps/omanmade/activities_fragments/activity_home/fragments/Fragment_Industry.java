package com.technology.circles.apps.omanmade.activities_fragments.activity_home.fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.technology.circles.apps.omanmade.R;
import com.technology.circles.apps.omanmade.activities_fragments.activity_home.HomeActivity;
import com.technology.circles.apps.omanmade.adapter.IndustrialAreaAdapter2;
import com.technology.circles.apps.omanmade.databinding.FragmentIndustryBinding;
import com.technology.circles.apps.omanmade.models.IndustrialAreaDataModel;
import com.technology.circles.apps.omanmade.preferences.Preferences;
import com.technology.circles.apps.omanmade.remote.Api;
import com.technology.circles.apps.omanmade.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Industry extends Fragment {
    private FragmentIndustryBinding binding;
    private HomeActivity activity;
    private Preferences preferences;
    private String lang;
    private List<IndustrialAreaDataModel.IndustrialAreaModel> industrialAreaModelList;
    private IndustrialAreaAdapter2 industrialAreaAdapter;



    public static Fragment_Industry newInstance() {
        return new Fragment_Industry();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_industry,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        industrialAreaModelList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        Paper.init(activity);
        lang = Paper.book().read("lang","ar");
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        binding.recView.setLayoutManager(new GridLayoutManager(activity,2));

        industrialAreaAdapter = new IndustrialAreaAdapter2(industrialAreaModelList,activity,this);
        binding.recView.setAdapter(industrialAreaAdapter);
        getIndustrialArea();
    }

    private void getIndustrialArea() {

        Api.getService(Tags.base_url2).
                getIndustrialArea(lang).
                enqueue(new Callback<IndustrialAreaDataModel>() {
                    @Override
                    public void onResponse(Call<IndustrialAreaDataModel> call, Response<IndustrialAreaDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);

                        if (response.isSuccessful() && response.body() != null&&response.body().getIndustrialAreas()!=null) {

                            industrialAreaModelList.clear();
                            industrialAreaModelList.addAll(response.body().getIndustrialAreas());
                            if (industrialAreaModelList.size()>0)
                            {
                                industrialAreaAdapter.notifyDataSetChanged();
                                binding.tvNoData2.setVisibility(View.GONE);
                            }else
                            {
                                binding.tvNoData2.setVisibility(View.VISIBLE);

                            }

                        } else {
                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<IndustrialAreaDataModel> call, Throwable t) {
                        binding.progBar.setVisibility(View.GONE);
                        try {
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {
                        }



                    }
                });

    }


    public void setItemData() {

        activity.DisplayFragmentDirectory(0,"",0,0,"","");
    }
}
