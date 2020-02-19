package com.technology.circles.apps.omanmade.activities_fragments.activity_home.fragments;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.leochuan.CenterSnapHelper;
import com.leochuan.ScaleLayoutManager;
import com.leochuan.ScrollHelper;
import com.leochuan.ViewPagerLayoutManager;
import com.technology.circles.apps.omanmade.R;
import com.technology.circles.apps.omanmade.activities_fragments.activity_business_details.BusinessDetailsActivity;
import com.technology.circles.apps.omanmade.activities_fragments.activity_home.HomeActivity;
import com.technology.circles.apps.omanmade.adapter.FeatureListingAdapter;
import com.technology.circles.apps.omanmade.adapter.FeaturedCategoryAdapter;
import com.technology.circles.apps.omanmade.adapter.IndustrialAreaAdapter;
import com.technology.circles.apps.omanmade.adapter.SliderAdapter;
import com.technology.circles.apps.omanmade.adapter.SpinnerLocationAdapter;
import com.technology.circles.apps.omanmade.adapter.SponsorAdapter;
import com.technology.circles.apps.omanmade.databinding.DialogSpinnerBinding;
import com.technology.circles.apps.omanmade.databinding.FragmentHomeBinding;
import com.technology.circles.apps.omanmade.models.FeatureListingDataModel;
import com.technology.circles.apps.omanmade.models.FeaturedCategoryDataModel;
import com.technology.circles.apps.omanmade.models.IndustrialAreaDataModel;
import com.technology.circles.apps.omanmade.models.SliderModel;
import com.technology.circles.apps.omanmade.models.SpinnerModel;
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
    private List<IndustrialAreaDataModel.IndustrialAreaModel> industrialAreaModelList;
    private IndustrialAreaAdapter industrialAreaAdapter;
    private List<FeaturedCategoryDataModel.FeaturedCategoryModel> featuredCategoryModelList;
    private FeaturedCategoryAdapter featuredCategoryAdapter;
    private List<SpinnerModel> spinnerLocationModelList, spinnerCategoryListingModelList;
    private SpinnerLocationAdapter spinnerLocationAdapter, spinnerCategoryAdapter;
    private final int arLangCode = 732, enLangCode = 730;
    private int selectedLangCode = arLangCode;

    private Timer timer, timer2;
    private TimerTask timerTask, timerTask2;

    private int category_id = 0, location_id = 0;
    private String query = "";
    private ViewPagerLayoutManager layoutManager;
    private AlertDialog categoryDialog, locationDialog;
    private DialogSpinnerBinding dialogSpinnerCategoryBinding,dialogSpinnerLocationBinding;
    private int currentPageCategory = 1;
    private boolean isLoadingCategory = false;
    private int totalPageCategory = 1;
    private String cat_name="",loc_name="";


    public static Fragment_Home newInstance() {
        return new Fragment_Home();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        initView();

        return binding.getRoot();
    }

    private void initView() {

        spinnerCategoryListingModelList = new ArrayList<>();
        spinnerLocationModelList = new ArrayList<>();
        sponsorsList = new ArrayList<>();
        featureModelList = new ArrayList<>();
        industrialAreaModelList = new ArrayList<>();
        featuredCategoryModelList = new ArrayList<>();

        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");
        binding.progBarSlider.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.progBarSponsor.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.progBarFeature.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.progBarIndustrialArea.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        binding.recViewFeature.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));

        layoutManager = new ScaleLayoutManager(activity, 20);

        layoutManager.setInfinite(true);
        layoutManager.setMaxVisibleItemCount(3);
        new CenterSnapHelper().attachToRecyclerView(binding.recViewSponsor);


        featureListingAdapter = new FeatureListingAdapter(featureModelList, activity, this);
        binding.recViewFeature.setAdapter(featureListingAdapter);


        binding.recViewIndustrialArea.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        industrialAreaAdapter = new IndustrialAreaAdapter(industrialAreaModelList, activity, this);
        binding.recViewIndustrialArea.setAdapter(industrialAreaAdapter);

        binding.recViewCategory.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        featuredCategoryAdapter = new FeaturedCategoryAdapter(featuredCategoryModelList, activity, this);
        binding.recViewCategory.setAdapter(featuredCategoryAdapter);


        //////////////////////////

        if (lang.equals("ar")) {
            selectedLangCode = arLangCode;

            spinnerLocationModelList.add(new SpinnerModel(0, "الموقع", arLangCode));
            spinnerLocationModelList.add(new SpinnerModel(0, "اخرى", arLangCode));

            spinnerCategoryListingModelList.add(new SpinnerModel(0, "التصنيف", arLangCode));

        } else {
            selectedLangCode = enLangCode;

            spinnerLocationModelList.add(new SpinnerModel(0, "Location", enLangCode));
            spinnerLocationModelList.add(new SpinnerModel(0, "Other", arLangCode));

            spinnerCategoryListingModelList.add(new SpinnerModel(0, "Category", arLangCode));

        }

        binding.tvSpinnerLocation.setText(spinnerLocationModelList.get(0).getName());

        binding.tvSpinnerCategory.setText(spinnerCategoryListingModelList.get(0).getName());


        ///////////////////////////


        binding.btnSearch.setOnClickListener(view -> {

            String query = binding.edtQuery.getText().toString().trim();
            if (query.isEmpty() && category_id == 0 && location_id == 0) {
                activity.DisplayFragmentDirectory(0, query, category_id, location_id,cat_name,loc_name);
            } else {
                activity.DisplayFragmentDirectory(1, query, category_id, location_id,cat_name,loc_name);

            }

        });
        binding.spinnerCategory.setOnClickListener(view -> categoryDialog.show());

        binding.spinnerLocation.setOnClickListener(view -> locationDialog.show());

        spinnerCategoryAdapter = new SpinnerLocationAdapter(spinnerCategoryListingModelList, activity, this,1);
        createCategoryDialog();
        spinnerLocationAdapter = new SpinnerLocationAdapter(spinnerLocationModelList, activity, this,2);
        createLocationDialog();

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

        if (sliderModel.getSlides().size() > 0) {
            binding.flSliderContainer.setVisibility(View.VISIBLE);
            sliderAdapter = new SliderAdapter(sliderModel.getSlides(), activity);
            binding.pager.setAdapter(sliderAdapter);
            binding.tab.setupWithViewPager(binding.pager);

            if (sliderModel.getSlides().size() > 1) {
                timer = new Timer();
                timerTask = new MyTimerTask();
                timer.scheduleAtFixedRate(timerTask, 6000, 6000);


            }


        } else {
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
                            if (featureModelList.size() > 0) {
                                featureListingAdapter.notifyDataSetChanged();
                                binding.tvNoData2.setVisibility(View.GONE);
                            } else {
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


                            if (sponsorsList.size() > 0) {
                                binding.tvNoData1.setVisibility(View.GONE);

                                sponsorAdapter = new SponsorAdapter(sponsorsList, activity, Fragment_Home.this);

                                binding.recViewSponsor.setLayoutManager(layoutManager);
                                binding.recViewSponsor.setAdapter(sponsorAdapter);

                                startTimer();

                            } else {
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

                        if (response.isSuccessful() && response.body() != null && response.body().getIndustrialAreas() != null) {

                            industrialAreaModelList.clear();
                            industrialAreaModelList.addAll(response.body().getIndustrialAreas());
                            if (industrialAreaModelList.size() > 0) {
                                industrialAreaAdapter.notifyDataSetChanged();
                                binding.tvNoData4.setVisibility(View.GONE);
                            } else {
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

                        if (response.isSuccessful() && response.body() != null && response.body().getFeatured_cats() != null) {

                            featuredCategoryModelList.clear();
                            featuredCategoryModelList.addAll(response.body().getFeatured_cats());
                            if (featuredCategoryModelList.size() > 0) {
                                featuredCategoryAdapter.notifyDataSetChanged();
                                binding.tvNoData3.setVisibility(View.GONE);
                            } else {
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

                        dialogSpinnerLocationBinding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null) {

                            if (response.body().size() > 0) {
                                spinnerLocationModelList.addAll(response.body());
                                spinnerLocationAdapter.notifyDataSetChanged();
                            }else
                                {
                                    dialogSpinnerLocationBinding.tvNoData.setVisibility(View.GONE);

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
                            dialogSpinnerLocationBinding.progBar.setVisibility(View.GONE);

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
                getListingCategory(1).
                enqueue(new Callback<List<SpinnerModel>>() {
                    @Override
                    public void onResponse(Call<List<SpinnerModel>> call, Response<List<SpinnerModel>> response) {
                        dialogSpinnerCategoryBinding.progBar.setVisibility(View.GONE);
                        totalPageCategory = Integer.parseInt(response.headers().get("X-WP-TotalPages"));

                        if (totalPageCategory>1)
                        {
                            dialogSpinnerCategoryBinding.cardMore.setVisibility(View.VISIBLE);
                        }else
                            {
                                dialogSpinnerCategoryBinding.cardMore.setVisibility(View.GONE);

                            }
                        if (response.isSuccessful() && response.body() != null) {

                            if (response.body().size() > 0) {
                                updateSpinnerCategoryListing(response.body(), 1);
                            } else {
                                dialogSpinnerCategoryBinding.tvNoData.setVisibility(View.VISIBLE);
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

    private void loadMoreCategoryListing(int page) {
        Api.getService(Tags.base_url1).
                getListingCategory(page).
                enqueue(new Callback<List<SpinnerModel>>() {
                    @Override
                    public void onResponse(Call<List<SpinnerModel>> call, Response<List<SpinnerModel>> response) {

                        if (response.isSuccessful() && response.body() != null) {


                            if (response.body().size() > 0) {
                                currentPageCategory = page;
                                updateSpinnerCategoryListing(response.body(), 2);
                            } else {
                                dialogSpinnerCategoryBinding.tvNoData.setVisibility(View.VISIBLE);
                            }

                        } else {

                            spinnerCategoryListingModelList.remove(spinnerCategoryListingModelList.size() - 1);
                            spinnerCategoryAdapter.notifyItemRemoved(spinnerCategoryListingModelList.size() - 1);
                            isLoadingCategory = false;

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
                            spinnerCategoryListingModelList.remove(spinnerCategoryListingModelList.size() - 1);
                            spinnerCategoryAdapter.notifyItemRemoved(spinnerCategoryListingModelList.size() - 1);
                            isLoadingCategory = false;
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

    private void updateSpinnerCategoryListing(List<SpinnerModel> list, int type) {

        if (list.size() > 0) {
            int old_pos = list.size() - 1;
            if (lang.equals("ar")) {
                if (type ==2)
                {
                    this.spinnerCategoryListingModelList.remove(this.spinnerCategoryListingModelList.size() - 1);
                    spinnerCategoryAdapter.notifyItemRemoved(this.spinnerCategoryListingModelList.size() - 1);
                    isLoadingCategory = false;
                }
                this.spinnerCategoryListingModelList.addAll(getArCategoryListing(list));
                dialogSpinnerCategoryBinding.recView.postDelayed(() -> dialogSpinnerCategoryBinding.recView.smoothScrollToPosition(spinnerCategoryListingModelList.size()-1),100);
            } else {

                if (type ==2)
                {
                    this.spinnerCategoryListingModelList.remove(this.spinnerCategoryListingModelList.size() - 1);
                    spinnerCategoryAdapter.notifyItemRemoved(this.spinnerCategoryListingModelList.size() - 1);
                    isLoadingCategory = false;
                }
                this.spinnerCategoryListingModelList.addAll(getEnCategoryListing(list));
            }

            if (type == 1)// is normal data
            {
                spinnerCategoryAdapter.notifyDataSetChanged();

            } else // is load more
            {


                spinnerCategoryAdapter.notifyItemRangeChanged(old_pos, this.spinnerCategoryListingModelList.size());
            }


        }
    }

    private List<SpinnerModel> getArCategoryListing(List<SpinnerModel> list) {
        List<SpinnerModel> spinnerModelList = new ArrayList<>();

        for (int i = 0; i < (list.size() / 2); i++) {
            spinnerModelList.add(list.get(i));

        }

        return spinnerModelList;
    }


    private List<SpinnerModel> getEnCategoryListing(List<SpinnerModel> list) {
        List<SpinnerModel> spinnerModelList = new ArrayList<>();

        for (int i = ((list.size() / 2) + 1); i < list.size(); i++) {
            spinnerModelList.add(list.get(i));

        }

        return spinnerModelList;
    }


    private void createCategoryDialog()
    {

        categoryDialog = new AlertDialog.Builder(activity)
                .create();
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        dialogSpinnerCategoryBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dialog_spinner, null, false);
        dialogSpinnerCategoryBinding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        dialogSpinnerCategoryBinding.recView.setLayoutManager(manager);
        dialogSpinnerCategoryBinding.recView.setAdapter(spinnerCategoryAdapter);

        dialogSpinnerCategoryBinding.btnLoadMore.setOnClickListener(view ->
        {
            if (!isLoadingCategory) {

                int page = currentPageCategory + 1;
                if (page <= totalPageCategory) {
                    spinnerCategoryListingModelList.add(null);
                    spinnerCategoryAdapter.notifyItemInserted(spinnerCategoryListingModelList.size() - 1);
                    dialogSpinnerCategoryBinding.recView.postDelayed(() -> dialogSpinnerCategoryBinding.recView.smoothScrollToPosition(spinnerCategoryListingModelList.size()-1),100);

                    loadMoreCategoryListing(page);
                }else
                {
                    dialogSpinnerCategoryBinding.cardMore.setVisibility(View.GONE);

                }
            }
        });



        dialogSpinnerCategoryBinding.btnCancel.setOnClickListener(v -> categoryDialog.dismiss()

        );
        categoryDialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        categoryDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        categoryDialog.setCanceledOnTouchOutside(false);
        categoryDialog.setView(dialogSpinnerCategoryBinding.getRoot());

    }

    private void createLocationDialog()
    {

        locationDialog = new AlertDialog.Builder(activity)
                .create();
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        dialogSpinnerLocationBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dialog_spinner, null, false);
        dialogSpinnerLocationBinding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        dialogSpinnerLocationBinding.recView.setLayoutManager(manager);
        dialogSpinnerLocationBinding.recView.setAdapter(spinnerLocationAdapter);

        dialogSpinnerLocationBinding.btnLoadMore.setVisibility(View.GONE);


        dialogSpinnerLocationBinding.btnCancel.setOnClickListener(v -> locationDialog.dismiss()

        );
        locationDialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        locationDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        locationDialog.setCanceledOnTouchOutside(false);
        locationDialog.setView(dialogSpinnerLocationBinding.getRoot());

    }


    public void setItemData() {

        activity.DisplayFragmentDirectory(0, "", 0, 0,cat_name,loc_name);
    }

    public void setCategoryData(SpinnerModel spinnerModel, int adapterPosition) {
        if (adapterPosition!=0)
        {

            cat_name = spinnerModel.getName();
            this.category_id = spinnerModel.getId();
            binding.tvSpinnerCategory.setText(spinnerModel.getName());
            categoryDialog.dismiss();
        }


    }

    public void setLocationData(SpinnerModel spinnerModel, int adapterPosition) {


        if (adapterPosition!=0)
        {
            if (adapterPosition==1)
            {
                locationDialog.dismiss();

            }else
            {
                loc_name = spinnerModel.getName();
                location_id = spinnerModel.getId();
                binding.tvSpinnerLocation.setText(spinnerModel.getName());
                locationDialog.dismiss();
            }

        }
    }

    public void setFeaturedItemData(FeatureListingDataModel.FeatureModel featureModel) {

        Intent intent = new Intent(activity, BusinessDetailsActivity.class);

        intent.putExtra("web_id",String.valueOf(32010));
        intent.putExtra("from",1);
        startActivity(intent);

    }


    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            activity.runOnUiThread(() -> {
                if (binding.pager.getCurrentItem() < binding.pager.getAdapter().getCount() - 1) {
                    binding.pager.setCurrentItem(binding.pager.getCurrentItem() + 1);
                } else {
                    binding.pager.setCurrentItem(0);
                }
            });
        }
    }


    private class MyTimerTask2 extends TimerTask {
        @Override
        public void run() {


            activity.runOnUiThread(() -> {


                try {
                    int item = layoutManager.getCurrentPosition() + 1;

                    if (item < sponsorsList.size()) {
                        ScrollHelper.smoothScrollToTargetView(binding.recViewSponsor, binding.recViewSponsor.findViewHolderForLayoutPosition(item).itemView);

                    } else {
                        item = 0;
                        ScrollHelper.smoothScrollToTargetView(binding.recViewSponsor, binding.recViewSponsor.findViewHolderForLayoutPosition(item).itemView);

                    }
                } catch (Exception e) {
                }
            });
        }
    }


    public void stopTimer() {
        if (timer2 != null) {

            timer2.cancel();
            timer2.purge();

        }

        if (timerTask2 != null) {
            timerTask2.cancel();
        }
    }

    public void startTimer() {
        if (sponsorsList.size() > 1) {
            timer2 = new Timer();
            timerTask2 = new MyTimerTask2();
            timer2.scheduleAtFixedRate(timerTask2, 0, 2000);

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (timer != null) {

            timer.cancel();
            timer.purge();

        }

        if (timerTask != null) {
            timerTask.cancel();
        }

        stopTimer();
    }

    @Override
    public void onStart() {
        super.onStart();
        startTimer();
    }

    @Override
    public void onPause() {
        super.onPause();

        stopTimer();
    }
}
