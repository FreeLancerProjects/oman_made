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
import com.technology.circles.apps.omanmade.adapter.SponsorAdapter2;
import com.technology.circles.apps.omanmade.databinding.FragmentSponsorBinding;
import com.technology.circles.apps.omanmade.models.SponsorsModel;
import com.technology.circles.apps.omanmade.remote.Api;
import com.technology.circles.apps.omanmade.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Sponsor extends Fragment {
    private FragmentSponsorBinding binding;
    private HomeActivity activity;
    private String lang;
    private SponsorAdapter2 adapter;
    private List<SponsorsModel.Sponsors> sponsorsList;



    public static Fragment_Sponsor newInstance() {
        return new Fragment_Sponsor();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sponsor,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        sponsorsList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        lang = Paper.book().read("lang","ar");
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        binding.recView.setLayoutManager(new GridLayoutManager(activity,3));
        adapter = new SponsorAdapter2(sponsorsList,activity,this);
        binding.recView.setAdapter(adapter);
        getSponsor();
    }


    private void getSponsor() {

        Api.getService(Tags.base_url2).
                getSponsor().
                enqueue(new Callback<SponsorsModel>() {
                    @Override
                    public void onResponse(Call<SponsorsModel> call, Response<SponsorsModel> response) {
                        binding.progBar.setVisibility(View.GONE);

                        if (response.isSuccessful() && response.body() != null) {

                            sponsorsList.clear();
                            sponsorsList.addAll(response.body().getSponsors());


                            if (sponsorsList.size()>0)
                            {
                                adapter.notifyDataSetChanged();
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
                    public void onFailure(Call<SponsorsModel> call, Throwable t) {
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




}
