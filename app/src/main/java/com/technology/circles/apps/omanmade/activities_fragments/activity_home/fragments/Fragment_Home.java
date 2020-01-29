package com.technology.circles.apps.omanmade.activities_fragments.activity_home.fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.technology.circles.apps.omanmade.R;
import com.technology.circles.apps.omanmade.activities_fragments.activity_home.HomeActivity;
import com.technology.circles.apps.omanmade.adapter.FeatureListingAdapter;
import com.technology.circles.apps.omanmade.adapter.SliderAdapter;
import com.technology.circles.apps.omanmade.adapter.SponsorAdapter;
import com.technology.circles.apps.omanmade.databinding.FragmentHomeBinding;
import com.technology.circles.apps.omanmade.models.FeatureListingDataModel;
import com.technology.circles.apps.omanmade.models.SliderModel;
import com.technology.circles.apps.omanmade.models.SponsorsModel;
import com.technology.circles.apps.omanmade.preferences.Preferences;
import com.technology.circles.apps.omanmade.remote.Api;
import com.technology.circles.apps.omanmade.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Home extends Fragment {
    private FragmentHomeBinding binding;
    private HomeActivity activity;
    private Preferences preferences;
    private String lang;
    private List<FeatureListingDataModel.FeatureModel> featureModelList;
    private FeatureListingAdapter featureListingAdapter;
    private SliderAdapter sliderAdapter;
    private SponsorAdapter sponsorAdapter;
    private List<SponsorsModel.Sponsors> sponsorsList;
    private Timer timer,timer2;
    private TimerTask timerTask,timerTask2;
    private int item = 0;




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
        sponsorsList = new ArrayList<>();
        featureModelList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        Paper.init(activity);
        lang = Paper.book().read("lang","ar");
        binding.progBarSlider.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.progBarSponsor.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.progBarFeature.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recViewFeature.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false));

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false) {
            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(activity) {
                    private static final float SPEED = 25f;// Change this value (default=25f)
                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return SPEED / displayMetrics.densityDpi;
                    }
                };
                smoothScroller.setTargetPosition(position);
                startSmoothScroll(smoothScroller);
            }

        };

        binding.recViewSponsor.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });

        binding.recViewSponsor.setLayoutManager(layoutManager);

        featureListingAdapter = new FeatureListingAdapter(featureModelList,activity,this);
        binding.recViewFeature.setAdapter(featureListingAdapter);





        getSlider();
        getSponsor();
        getFeature();




    }

    private void getSlider() {

        Api.getService(Tags.base_url2).
                geSlider().
                enqueue(new Callback<SliderModel>() {
                    @Override
                    public void onResponse(Call<SliderModel> call, Response<SliderModel> response) {
                        binding.progBarSlider.setVisibility(View.GONE);

                        if (response.isSuccessful() && response.body() != null) {

                          updateSliderUI(response.body());

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
                    public void onFailure(Call<SliderModel> call, Throwable t) {
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

    private void updateSliderUI(SliderModel sliderModel) {

        if (sliderModel.getSlides().size()>0)
        {
            binding.flSliderContainer.setVisibility(View.VISIBLE);
            sliderAdapter = new SliderAdapter(sliderModel.getSlides(),activity);
            binding.pager.setAdapter(sliderAdapter);
            binding.tab.setupWithViewPager(binding.pager);

            if (sliderModel.getSlides().size()>1)
            {
                timer = new Timer();
                timerTask = new MyTimerTask();
                timer.scheduleAtFixedRate(timerTask,6000,6000);


            }


        }else
            {
                binding.flSliderContainer.setVisibility(View.GONE);
            }
    }

    private void getFeature() {

        Api.getService(Tags.base_url2).
                getFeatures(lang).
                enqueue(new Callback<FeatureListingDataModel>() {
                    @Override
                    public void onResponse(Call<FeatureListingDataModel> call, Response<FeatureListingDataModel> response) {
                        binding.progBarFeature.setVisibility(View.GONE);

                        if (response.isSuccessful() && response.body() != null) {

                            featureModelList.clear();
                            featureModelList.addAll(response.body().getFeatured_lists());
                            if (featureModelList.size()>0)
                            {
                                featureListingAdapter.notifyDataSetChanged();
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
                    public void onFailure(Call<FeatureListingDataModel> call, Throwable t) {
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

    private void getSponsor() {

        Api.getService(Tags.base_url2).
                getSponsor().
                enqueue(new Callback<SponsorsModel>() {
                    @Override
                    public void onResponse(Call<SponsorsModel> call, Response<SponsorsModel> response) {
                        binding.progBarSponsor.setVisibility(View.GONE);

                        if (response.isSuccessful() && response.body() != null) {

                            sponsorsList.clear();
                            sponsorsList.addAll(response.body().getSponsors());


                            if (sponsorsList.size()>0)
                            {

                                sponsorAdapter = new SponsorAdapter(sponsorsList,activity,Fragment_Home.this);
                                binding.recViewSponsor.setAdapter(sponsorAdapter);

                                 binding.tvNoData1.setVisibility(View.GONE);

                                if (sponsorsList.size()>1)
                                {
                                    timer2 = new Timer();
                                    timerTask2 = new MyTimerTask2();
                                    timer2.scheduleAtFixedRate(timerTask2,0,1);

                                }
                            }else
                            {
                                binding.tvNoData1.setVisibility(View.VISIBLE);

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
                        binding.progBarSponsor.setVisibility(View.GONE);
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

    private class MyTimerTask extends TimerTask{
        @Override
        public void run() {
            activity.runOnUiThread(() -> {
                if (binding.pager.getCurrentItem()<binding.pager.getAdapter().getCount()-1)
                {
                    binding.pager.setCurrentItem(binding.pager.getCurrentItem()+1);
                }else
                    {
                        binding.pager.setCurrentItem(0);
                    }
            });
        }
    }

    private class MyTimerTask2 extends TimerTask{
        @Override
        public void run() {
            activity.runOnUiThread(() -> {
                item+=1;
                binding.recViewSponsor.smoothScrollToPosition(item);
            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (timer2!=null)
        {
            timer2.purge();
            timer2.cancel();

        }

        if (timerTask2!=null)
        {
            timerTask2.cancel();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (timer2!=null)
        {
            if (sponsorsList.size()>1)
            {
                timer2 = new Timer();
                timerTask2 = new MyTimerTask2();
                timer2.scheduleAtFixedRate(timerTask2,0,1);

            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (timer!=null){

            timer.cancel();
            timer.purge();

        }

        if (timerTask!=null)
        {
            timerTask.cancel();
        }

        if (timer2!=null){

            timer2.cancel();
            timer2.purge();

        }

        if (timerTask2!=null)
        {
            timerTask2.cancel();
        }
    }
}
