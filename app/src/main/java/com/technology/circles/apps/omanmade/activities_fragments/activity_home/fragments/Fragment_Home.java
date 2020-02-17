package com.technology.circles.apps.omanmade.activities_fragments.activity_home.fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.technology.circles.apps.omanmade.R;
import com.technology.circles.apps.omanmade.activities_fragments.activity_home.HomeActivity;
import com.technology.circles.apps.omanmade.adapter.FeatureListingAdapter;
import com.technology.circles.apps.omanmade.adapter.FeaturedCategoryAdapter;
import com.technology.circles.apps.omanmade.adapter.IndustrialAreaAdapter;
import com.technology.circles.apps.omanmade.adapter.SliderAdapter;
import com.technology.circles.apps.omanmade.adapter.SpinnerLocationAdapter;
import com.technology.circles.apps.omanmade.adapter.SponsorAdapter;
import com.technology.circles.apps.omanmade.databinding.FragmentHomeBinding;
import com.technology.circles.apps.omanmade.models.FeatureListingDataModel;
import com.technology.circles.apps.omanmade.models.FeaturedCategoryDataModel;
import com.technology.circles.apps.omanmade.models.IndustrialAreaDataModel;
import com.technology.circles.apps.omanmade.models.SpinnerModel;
import com.technology.circles.apps.omanmade.models.SliderModel;
import com.technology.circles.apps.omanmade.models.SponsorsModel;
import com.technology.circles.apps.omanmade.preferences.Preferences;
import com.technology.circles.apps.omanmade.remote.Api;
import com.technology.circles.apps.omanmade.tags.Tags;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

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
    private List<IndustrialAreaDataModel.IndustrialAreaModel> industrialAreaModelList;
    private IndustrialAreaAdapter industrialAreaAdapter;
    private List<FeaturedCategoryDataModel.FeaturedCategoryModel> featuredCategoryModelList;
    private FeaturedCategoryAdapter featuredCategoryAdapter;
    private List<SpinnerModel> spinnerLocationModelList,spinnerCategoryListingModelList;

    private List<SpinnerModel> selectedListingModelList;

    private SpinnerLocationAdapter spinnerLocationAdapter,spinnerCategoryListingAdapter;
    private final int arLangCode = 732,enLangCode=730;
    private int selectedLangCode = arLangCode;

    private Timer timer,timer2;
    private TimerTask timerTask,timerTask2;

    private int category_id=0,location_id=0;
    private String query="";




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

        spinnerCategoryListingModelList = new ArrayList<>();
        selectedListingModelList = new ArrayList<>();
        spinnerLocationModelList = new ArrayList<>();
        sponsorsList = new ArrayList<>();
        featureModelList = new ArrayList<>();
        industrialAreaModelList = new ArrayList<>();
        featuredCategoryModelList = new ArrayList<>();

        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        Paper.init(activity);
        lang = Paper.book().read("lang","ar");
        binding.progBarSlider.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.progBarSponsor.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.progBarFeature.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.progBarIndustrialArea.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        binding.recViewFeature.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false));

        binding.recViewSponsor.setItemTransformer(new ScaleTransformer.Builder()
                .setMaxScale(1.00f)
                .setMinScale(0.8f)
                .setPivotX(Pivot.X.CENTER) // CENTER is a default one
                .setPivotY(Pivot.Y.BOTTOM) // CENTER is a default one
                .build());

        binding.recViewSponsor.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });


        featureListingAdapter = new FeatureListingAdapter(featureModelList,activity,this);
        binding.recViewFeature.setAdapter(featureListingAdapter);


        binding.recViewIndustrialArea.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false));
        industrialAreaAdapter = new IndustrialAreaAdapter(industrialAreaModelList,activity,this);
        binding.recViewIndustrialArea.setAdapter(industrialAreaAdapter);

        binding.recViewCategory.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false));
        featuredCategoryAdapter = new FeaturedCategoryAdapter(featuredCategoryModelList,activity,this);
        binding.recViewCategory.setAdapter(featuredCategoryAdapter);


        //////////////////////////

        if (lang.equals("ar")) {
            selectedLangCode = arLangCode;

            spinnerLocationModelList.add(new SpinnerModel(0, "الموقع", arLangCode));
            spinnerLocationModelList.add(new SpinnerModel(0, "اخرى", arLangCode));

            selectedListingModelList.add(new SpinnerModel(0, "التصنيف", arLangCode));

        } else {
            selectedLangCode = enLangCode;

            spinnerLocationModelList.add(new SpinnerModel(0, "Location", enLangCode));
            spinnerLocationModelList.add(new SpinnerModel(0, "Other", arLangCode));

            selectedListingModelList.add(new SpinnerModel(0, "Category", arLangCode));

        }

        spinnerLocationAdapter = new SpinnerLocationAdapter(spinnerLocationModelList,activity);
        binding.spinnerLocation.setAdapter(spinnerLocationAdapter);

        spinnerCategoryListingAdapter = new SpinnerLocationAdapter(selectedListingModelList,activity);
        binding.spinnerCategoryListing.setAdapter(spinnerCategoryListingAdapter);



        binding.spinnerCategoryListing.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    category_id = 0;
                } else {
                    category_id = selectedListingModelList.get(i).getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    location_id = 0;
                }else if (i==1){

                } else {
                    location_id = spinnerLocationModelList.get(i).getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ///////////////////////////


        binding.btnSearch.setOnClickListener(view -> {

            String query = binding.edtQuery.getText().toString().trim();
            if (query.isEmpty()&&category_id==0&&location_id==0)
            {
                activity.DisplayFragmentDirectory(0,query,category_id,location_id);
            }else
                {
                    activity.DisplayFragmentDirectory(1,query,category_id,location_id);

                }

        });

        getSlider();
        getSponsor();
        getFeature();
        getIndustrialArea();
        getFeaturedCategory();
        getLocation();
        getCategoryListing();



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
                                binding.recViewSponsor.setItemTransitionTimeMillis(500);

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

    private void getIndustrialArea() {

        Api.getService(Tags.base_url2).
                getIndustrialArea(lang).
                enqueue(new Callback<IndustrialAreaDataModel>() {
                    @Override
                    public void onResponse(Call<IndustrialAreaDataModel> call, Response<IndustrialAreaDataModel> response) {
                        binding.progBarIndustrialArea.setVisibility(View.GONE);

                        if (response.isSuccessful() && response.body() != null&&response.body().getIndustrialAreas()!=null) {

                            industrialAreaModelList.clear();
                            industrialAreaModelList.addAll(response.body().getIndustrialAreas());
                            if (industrialAreaModelList.size()>0)
                            {
                                industrialAreaAdapter.notifyDataSetChanged();
                                binding.tvNoData4.setVisibility(View.GONE);
                            }else
                            {
                                binding.tvNoData4.setVisibility(View.VISIBLE);

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

    private void getFeaturedCategory() {

        Api.getService(Tags.base_url2).
                getFeaturedCategory(lang).
                enqueue(new Callback<FeaturedCategoryDataModel>() {
                    @Override
                    public void onResponse(Call<FeaturedCategoryDataModel> call, Response<FeaturedCategoryDataModel> response) {
                        binding.progBarCategory.setVisibility(View.GONE);

                        if (response.isSuccessful() && response.body() != null&&response.body().getFeatured_cats()!=null) {

                            featuredCategoryModelList.clear();
                            featuredCategoryModelList.addAll(response.body().getFeatured_cats());
                            if (featuredCategoryModelList.size()>0)
                            {
                                featuredCategoryAdapter.notifyDataSetChanged();
                                binding.tvNoData3.setVisibility(View.GONE);
                            }else
                            {
                                binding.tvNoData3.setVisibility(View.VISIBLE);

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
                    public void onFailure(Call<FeaturedCategoryDataModel> call, Throwable t) {
                        binding.progBarCategory.setVisibility(View.GONE);
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

    private void getLocation() {

        Api.getService(Tags.base_url1).
                getLocation(selectedLangCode).
                enqueue(new Callback<List<SpinnerModel>>() {
                    @Override
                    public void onResponse(Call<List<SpinnerModel>> call, Response<List<SpinnerModel>> response) {

                        if (response.isSuccessful() && response.body() != null) {

                            if (response.body().size()>0)
                            {
                                spinnerLocationModelList.addAll(response.body());
                                activity.runOnUiThread(()->{
                                    spinnerLocationAdapter.notifyDataSetChanged();
                                });
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
                    public void onFailure(Call<List<SpinnerModel>> call, Throwable t) {
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

    private void getCategoryListing() {

        Api.getService(Tags.base_url1).
                getListingCategory().
                enqueue(new Callback<List<SpinnerModel>>() {
                    @Override
                    public void onResponse(Call<List<SpinnerModel>> call, Response<List<SpinnerModel>> response) {

                        if (response.isSuccessful() && response.body() != null) {

                            if (response.body().size()>0)
                            {
                                spinnerCategoryListingModelList.addAll(response.body());
                                updateSpinnerCategoryListing(spinnerCategoryListingModelList);
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
                    public void onFailure(Call<List<SpinnerModel>> call, Throwable t) {
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

    private void updateSpinnerCategoryListing(List<SpinnerModel> spinnerCategoryListingModelList) {


        if (spinnerCategoryListingModelList.size()>0)
        {
            if (lang.equals("ar"))
            {
                selectedListingModelList.addAll(getArCategoryListing());
                spinnerCategoryListingAdapter.notifyDataSetChanged();
            }else
                {
                    selectedListingModelList.addAll(getEnCategoryListing());
                    spinnerCategoryListingAdapter.notifyDataSetChanged();
                }


        }
    }

    private List<SpinnerModel> getArCategoryListing()
    {
        List<SpinnerModel> spinnerModelList = new ArrayList<>();
        int end = spinnerCategoryListingModelList.size()/2;

        for (int i =0; i<end;i++)
        {
            spinnerModelList.add(spinnerCategoryListingModelList.get(i));
        }

        return spinnerModelList;
    }


    private List<SpinnerModel> getEnCategoryListing()
    {
        List<SpinnerModel> spinnerModelList = new ArrayList<>();
        int start = spinnerCategoryListingModelList.size()/2;

        for (int i =start; i<spinnerCategoryListingModelList.size();i++)
        {
            Log.e("dididi",spinnerCategoryListingModelList.get(i).getId()+"_");
            spinnerModelList.add(spinnerCategoryListingModelList.get(i));
        }

        return spinnerModelList;
    }

    public void setItemData() {

        activity.DisplayFragmentDirectory(0,"",0,0);
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


                if (binding.recViewSponsor.getCurrentItem()<Integer.MAX_VALUE)
                {
                    try {
                        int item = binding.recViewSponsor.getCurrentItem()+1;
                        binding.recViewSponsor.smoothScrollToPosition(item);
                    }catch (Exception e){}





                }else
                    {
                        timerTask2.cancel();
                        timer2.cancel();
                        timer2.purge();

                        binding.recViewSponsor.postDelayed(() -> binding.recViewSponsor.smoothScrollToPosition(0),1000);

                        timer2 = new Timer();
                        timerTask2 = new MyTimerTask2();
                        timer2.scheduleAtFixedRate(timerTask2,0,500);
                    }
            });
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
