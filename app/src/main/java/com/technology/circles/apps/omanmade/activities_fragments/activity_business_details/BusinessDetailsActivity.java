package com.technology.circles.apps.omanmade.activities_fragments.activity_business_details;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.technology.circles.apps.omanmade.R;
import com.technology.circles.apps.omanmade.activities_fragments.FragmentMapTouchListener;
import com.technology.circles.apps.omanmade.adapter.Gallery1Adapter;
import com.technology.circles.apps.omanmade.adapter.OpenHourAdapter;
import com.technology.circles.apps.omanmade.databinding.ActivityBusinessDetailsBinding;
import com.technology.circles.apps.omanmade.interfaces.Listeners;
import com.technology.circles.apps.omanmade.language.LanguageHelper;
import com.technology.circles.apps.omanmade.models.BusinessDataModel;
import com.technology.circles.apps.omanmade.models.MapLocationModel;
import com.technology.circles.apps.omanmade.remote.Api;
import com.technology.circles.apps.omanmade.tags.Tags;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessDetailsActivity extends AppCompatActivity implements Listeners.BackListener , OnMapReadyCallback{
    private ActivityBusinessDetailsBinding binding;
    private String lang;
    private FragmentMapTouchListener fragment;
    private GoogleMap mMap;
    private String web_id;
    private OpenHourAdapter openHourAdapter;
    private List<BusinessDataModel.OpeningHourList> openingHourLists;
    private List<String> gallery;
    private Gallery1Adapter galleryAdapter;
    private Timer timer;
    private TimerTask timerTask;
    private boolean isPlaying = false;
    private MapLocationModel mapLocationModel=null;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang","ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_business_details);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent()
    {
        Intent intent = getIntent();
        if (intent!=null)
        {
            if (intent.hasExtra("data"))
            {
                mapLocationModel = (MapLocationModel) intent.getSerializableExtra("data");
            }
            web_id = intent.getStringExtra("web_id");

            Log.e("wid",web_id);

        }
    }
    private void initView() {
        gallery = new ArrayList<>();
        openingHourLists = new ArrayList<>();

        Paper.init(this);
        lang = Paper.book().read("lang","ar");
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);


        initMap();


        getBusinessData();

        getBusinessData_Slider_FeatureListingGallery();

        binding.imageBack.setOnClickListener(view -> {

            if (gallery.size()>1)
            {
                binding.pager.setCurrentItem(binding.pager.getRealItem()-1);
            }

        });

        binding.imageForward.setOnClickListener(view -> {

            if (gallery.size()>1)
            {
                binding.pager.setCurrentItem(binding.pager.getRealItem()+1);
            }

        });

        binding.imagePlayPause.setOnClickListener(view -> {

            if (gallery.size()>1)
            {
                if (isPlaying)
                {
                    stopTimer();
                }else
                    {
                        startTimer();
                    }
            }

        });

        binding.recViewOpeningHours.setNestedScrollingEnabled(true);

        binding.scrollView.getParent().requestChildFocus(binding.scrollView,binding.scrollView);
    }




    private void initMap() {

        fragment = (FragmentMapTouchListener) getSupportFragmentManager().findFragmentById(R.id.map);
        if (fragment != null) {
            fragment.getMapAsync(this);

        }

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (googleMap != null) {
            mMap = googleMap;
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.maps));
            mMap.setTrafficEnabled(false);
            mMap.setBuildingsEnabled(false);
            mMap.setIndoorEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(false);
            mMap.getUiSettings().setCompassEnabled(false);
            mMap.getUiSettings().setTiltGesturesEnabled(false);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(20.687852357971384,56.02986674755812), 7.1496434f));

            fragment.setListener(() -> binding.scrollView.requestDisallowInterceptTouchEvent(true));



        }

    }

    private void addMarker(double lat,double lng ,String title)
    {
        mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title(title).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),13.5f));
    }

    private void getBusinessData()
    {

        Api.getService(Tags.base_url1).
                getBusinessByWebId(web_id).
                enqueue(new Callback<BusinessDataModel>() {
                    @Override
                    public void onResponse(Call<BusinessDataModel> call, Response<BusinessDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null) {
                            binding.tvNoData.setVisibility(View.GONE);
                            updateUI(response.body());

                        } else {
                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(BusinessDetailsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                            } else if (response.code()==404){

                                binding.tvNoData.setVisibility(View.VISIBLE);
                            }else {
                                Toast.makeText(BusinessDetailsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BusinessDataModel> call, Throwable t) {
                        try {
                            binding.progBar.setVisibility(View.GONE);

                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(BusinessDetailsActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(BusinessDetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {
                        }



                    }
                });
    }

    private void updateUI(BusinessDataModel businessDataModel)
    {


        binding.setModel(businessDataModel);
        binding.llContainer.setVisibility(View.VISIBLE);

        if (businessDataModel.getCmb2().getListing_business_location().getListing_map_location() instanceof String)
        {
            binding.llMap.setVisibility(View.GONE);

        }else
            {

                Map<String,String> map = (Map<String, String>) businessDataModel.getCmb2().getListing_business_location().getListing_map_location();

                String lat = map.get("latitude");

                String lng = map.get("longitude");

                String address = map.get("address");

                binding.tvAddress.setText(address);
                if (lat!=null&&!lat.isEmpty()&&lng!=null&&!lng.isEmpty())
                {
                    addMarker(Double.parseDouble(lat),Double.parseDouble(lng),address);
                    binding.llMap.setVisibility(View.VISIBLE);

                }else
                    {
                        if (mapLocationModel!=null&&!mapLocationModel.getLatitude().isEmpty()&&mapLocationModel.getLatitude()!=null)
                        {

                            binding.llMap.setVisibility(View.VISIBLE);
                            addMarker(Double.parseDouble(mapLocationModel.getLatitude()),Double.parseDouble(mapLocationModel.getLongitude()),mapLocationModel.getAddress());

                        }else
                            {
                                binding.llMap.setVisibility(View.GONE);

                            }


                    }
            }




        if (businessDataModel.getCmb2().getListing_business_opening_hours().getListing_opening_hours() instanceof String)
        {
            binding.tvNotAvailable.setVisibility(View.VISIBLE);
        } else if (businessDataModel.getCmb2().getListing_business_opening_hours().getListing_opening_hours() instanceof  List) {

            List<Map<String,String>> objectList = (List<Map<String, String>>) businessDataModel.getCmb2().getListing_business_opening_hours().getListing_opening_hours();

            for (Map<String,String> map :objectList)
            {
                String day = map.get("listing_day");
                String from = map.get("listing_time_from");
                String to = map.get("listing_time_to");

                BusinessDataModel.OpeningHourList model = new BusinessDataModel.OpeningHourList(day,from,to,"");

                openingHourLists.add(model);


            }

            openHourAdapter = new OpenHourAdapter(openingHourLists,this);
            binding.recViewOpeningHours.setLayoutManager(new LinearLayoutManager(this));
            binding.recViewOpeningHours.setAdapter(openHourAdapter);
            binding.tvNotAvailable.setVisibility(View.GONE);




        }





    }

    private void getBusinessData_FeaturedCategory_IndustrialArea()
    {
        Api.getService(Tags.base_url1).getBusinessByWebId2(lang, 1,web_id).
                enqueue(new Callback<List<BusinessDataModel>>() {
            @Override
            public void onResponse(Call<List<BusinessDataModel>> call, Response<List<BusinessDataModel>> response) {
                binding.progBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {

                    if (response.body().size()>0)
                    {

                        updateUI(response.body().get(0));
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
                        Toast.makeText(BusinessDetailsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                    } else if (response.code() == 404) {
                        binding.tvNoData.setVisibility(View.GONE);


                    } else {
                        Toast.makeText(BusinessDetailsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                    }
                }
            }

            @Override
            public void onFailure(Call<List<BusinessDataModel>> call, Throwable t) {
                binding.progBar.setVisibility(View.GONE);
                try {
                    if (t.getMessage() != null) {
                        Log.e("error", t.getMessage());
                        if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                            Toast.makeText(BusinessDetailsActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(BusinessDetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (Exception e) {
                }


            }
        });

    }

    private void getBusinessData_Slider_FeatureListingGallery()
    {
        Log.e("wid",web_id+"__");
        Api.getService(Tags.base_url1).
                getBusinessByWebIdGallery(web_id).
                enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {


                            try {
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                JSONObject cmb2 = jsonObject.getJSONObject("cmb2");
                                JSONObject listing_business_gallery = cmb2.getJSONObject("listing_business_gallery");
                                JSONObject listing_gallery = listing_business_gallery.getJSONObject("listing_gallery");




                                Map<String,String> map = new Gson().fromJson(listing_gallery.toString(), HashMap.class);

                                for (String key :map.keySet())
                                {
                                    gallery.add(map.get(key));
                                }

                                updateGalleryUI();






                            } catch (JSONException e) {
                                Log.e("dddd",e.getMessage()+"_");
                                binding.pager.setVisibility(View.GONE);
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(BusinessDetailsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                            } else if (response.code()==404){

                                binding.tvNoData.setVisibility(View.VISIBLE);
                            }else {
                                Toast.makeText(BusinessDetailsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {

                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(BusinessDetailsActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(BusinessDetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {
                        }



                    }
                });

    }

    private void getBusinessData_FeaturedCategory_IndustrialAreaGallery()
    {

        Api.getService(Tags.base_url1).
                getBusinessByWebId2Gallery(lang,1,web_id).
                enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {

                            try {
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                JSONObject cmb2 = jsonObject.getJSONObject("cmb2");
                                JSONObject listing_business_gallery = cmb2.getJSONObject("listing_business_gallery");
                                JSONObject listing_gallery = listing_business_gallery.getJSONObject("listing_gallery");

                                Map<String,String> map = new Gson().fromJson(listing_gallery.toString(), HashMap.class);

                                for (String key :map.keySet())
                                {
                                    gallery.add(map.get(key));
                                }

                                updateGalleryUI();


                            } catch (JSONException e) {
                                Log.e("dddd",e.getMessage()+"_");
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(BusinessDetailsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                            } else if (response.code()==404){

                                binding.tvNoData.setVisibility(View.VISIBLE);
                            }else {
                                Toast.makeText(BusinessDetailsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {

                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(BusinessDetailsActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(BusinessDetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {
                        }



                    }
                });
    }

    private void updateGalleryUI()
    {

        if (gallery.size()>0)
        {
            binding.pager.setVisibility(View.VISIBLE);

            galleryAdapter = new Gallery1Adapter(gallery,this);
            binding.pager.setAdapter(galleryAdapter);


            if (gallery.size()>1)
            {
                binding.llPlay.setVisibility(View.VISIBLE);
                startTimer();
            }
        }else
            {
                binding.pager.setVisibility(View.GONE);
            }

    }

    private void startTimer() {

        isPlaying = true;
        binding.imagePlayPause.setImageResource(R.drawable.ic_pause);
        timer = new Timer();
        timerTask = new MyTimerTask();
        timer.scheduleAtFixedRate(timerTask, 6000, 6000);
    }

    private void stopTimer() {
        isPlaying = false;
        binding.imagePlayPause.setImageResource(R.drawable.ic_play);

        if (timer != null) {

            timer.cancel();
            timer.purge();

        }

        if (timerTask != null) {
            timerTask.cancel();
        }
    }


    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(() -> {
                binding.pager.setCurrentItem(binding.pager.getRealItem() + 1);

            });
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();

        stopTimer();

    }


    @Override
    protected void onPause() {
        super.onPause();
        stopTimer();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (gallery.size()>1)
        {
            startTimer();
        }
    }

    @Override
    public void back() {
        finish();
    }

}
