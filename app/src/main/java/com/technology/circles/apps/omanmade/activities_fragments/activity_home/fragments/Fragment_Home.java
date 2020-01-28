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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.technology.circles.apps.omanmade.R;
import com.technology.circles.apps.omanmade.activities_fragments.activity_home.HomeActivity;
import com.technology.circles.apps.omanmade.adapter.FeatureAdapter;
import com.technology.circles.apps.omanmade.databinding.FragmentHomeBinding;
import com.technology.circles.apps.omanmade.models.FeatureDataModel;
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

public class Fragment_Home extends Fragment {
    private FragmentHomeBinding binding;
    private HomeActivity activity;
    private Preferences preferences;
    private String lang;
    private List<FeatureDataModel.FeatureModel> featureModelList;
    private FeatureAdapter featureAdapter;


    public static Fragment_Home newInstance() {
        return new Fragment_Home();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home,container,false);
        initView();

        return binding.getRoot();
    }

    private void initView() {
        featureModelList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        Paper.init(activity);
        lang = Paper.book().read("lang","ar");
        binding.progBarFeature.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recViewFeature.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false));

        featureAdapter = new FeatureAdapter(featureModelList,activity,this);
        binding.recViewFeature.setAdapter(featureAdapter);

        getFeature();

    }

    private void getFeature() {

        Api.getService(Tags.base_url2).
                getFeatures().
                enqueue(new Callback<FeatureDataModel>() {
                    @Override
                    public void onResponse(Call<FeatureDataModel> call, Response<FeatureDataModel> response) {
                        binding.progBarFeature.setVisibility(View.GONE);

                        if (response.isSuccessful() && response.body() != null) {

                            featureModelList.clear();
                            featureModelList.addAll(response.body().getFeatured_lists());
                            if (featureModelList.size()>0)
                            {
                                featureAdapter.notifyDataSetChanged();
                                binding.tvNoData.setVisibility(View.GONE);
                            }else
                                {
                                    binding.tvNoData.setVisibility(View.VISIBLE);

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
                    public void onFailure(Call<FeatureDataModel> call, Throwable t) {
                        binding.progBarFeature.setVisibility(View.GONE);
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


}
