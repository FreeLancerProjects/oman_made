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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.technology.circles.apps.omanmade.R;
import com.technology.circles.apps.omanmade.activities_fragments.activity_home.HomeActivity;
import com.technology.circles.apps.omanmade.adapter.BusinessAdapter;
import com.technology.circles.apps.omanmade.adapter.DirectoryAdapter;
import com.technology.circles.apps.omanmade.adapter.SpinnerLocationAdapter;
import com.technology.circles.apps.omanmade.databinding.FragmentDirectoryBinding;
import com.technology.circles.apps.omanmade.models.BusinessDataModel;
import com.technology.circles.apps.omanmade.models.ClusterLocation;
import com.technology.circles.apps.omanmade.models.ClusterRender;
import com.technology.circles.apps.omanmade.models.DirectoryDataModel;
import com.technology.circles.apps.omanmade.models.MediaModel;
import com.technology.circles.apps.omanmade.models.SpinnerModel;
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

public class Fragment_Directory extends Fragment implements OnMapReadyCallback {
    private static final String TAG1="query";
    private static final String TAG2="cat_id";
    private static final String TAG3="loc_id";

    private FragmentDirectoryBinding binding;
    private HomeActivity activity;
    private Preferences preferences;
    private String lang;
    private SupportMapFragment fragment;
    private GoogleMap mMap;
    private List<SpinnerModel> spinnerLocationModelList, spinnerCategoryListingModelList;
    private List<SpinnerModel> selectedListingModelList;
    private SpinnerLocationAdapter spinnerLocationAdapter, spinnerCategoryListingAdapter;
    private final int arLangCode = 732, enLangCode = 730;
    private int selectedLangCode = arLangCode;
    private List<DirectoryDataModel.DirectoryModel> directoryModelList;
    private DirectoryAdapter directoryAdapter;
    private List<BusinessDataModel> businessDataModelList;
    private BusinessAdapter businessAdapter;
    private int currentPage = 1;
    private boolean isLoading = false;
    private String query = "";
    private int cat_id = 0, location_id = 0;
    private int searchType = 0;
    private int index =0;
    private List<BusinessDataModel> inCompleteBusinessDataModelList;

    private List<ClusterLocation> locationList;
    private ClusterManager clusterManager;
    private ClusterRender clusterRender;

    private Call<List<BusinessDataModel>> call,callLoadMore,searchCall,searchLoadMoreCall;
    private Call<MediaModel> callMedia,callLoadMoreMedia;

    public static Fragment_Directory newInstance(String query,int category_id,int location_id) {

        Bundle bundle = new Bundle();
        bundle.putString(TAG1,query);
        bundle.putInt(TAG2,category_id);
        bundle.putInt(TAG3,location_id);

        Fragment_Directory fragment_directory = new Fragment_Directory();
        fragment_directory.setArguments(bundle);
        return fragment_directory;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_directory, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        inCompleteBusinessDataModelList = new ArrayList<>();
        locationList = new ArrayList<>();
        businessDataModelList = new ArrayList<>();
        directoryModelList = new ArrayList<>();
        spinnerCategoryListingModelList = new ArrayList<>();
        selectedListingModelList = new ArrayList<>();
        spinnerLocationModelList = new ArrayList<>();

        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");
        binding.progBarSearch.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.progBarDirectory.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);


        binding.recViewDirectory.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        directoryAdapter = new DirectoryAdapter(directoryModelList, activity, this);
        binding.recViewDirectory.setAdapter(directoryAdapter);


        businessAdapter = new BusinessAdapter(businessDataModelList, activity, this);
        binding.recViewSearch.setLayoutManager(new GridLayoutManager(activity, 3));
        binding.recViewSearch.setAdapter(businessAdapter);

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

        spinnerLocationAdapter = new SpinnerLocationAdapter(spinnerLocationModelList, activity);
        binding.spinnerLocation.setAdapter(spinnerLocationAdapter);

        spinnerCategoryListingAdapter = new SpinnerLocationAdapter(selectedListingModelList, activity);
        binding.spinnerCategoryListing.setAdapter(spinnerCategoryListingAdapter);

        binding.recViewSearch.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastItem = ((GridLayoutManager) binding.recViewSearch.getLayoutManager()).findLastCompletelyVisibleItemPosition();

                if (dy > 0 && (lastItem == businessAdapter.getItemCount() - 1 || lastItem == businessAdapter.getItemCount() - 2) && !isLoading) {
                    int page = currentPage + 1;
                    isLoading = true;
                    businessDataModelList.add(null);
                    businessAdapter.notifyItemInserted(businessDataModelList.size() - 1);

                    if (searchType == 0) {
                        loadMoreBusiness(page);

                    } else if (searchType == 1) {
                        loadMoreSearch(query,page,cat_id,location_id);

                    }

                }
            }
        });

        ////////////////////////////////////////////////////

        binding.spinnerCategoryListing.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    cat_id = 0;
                } else {
                    cat_id = selectedListingModelList.get(i).getId();
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

        ////////////////////////////////////////////////////
        binding.btnSearch.setOnClickListener(view -> {

            query = binding.edtKeyword.getText().toString().trim();

            checkSearch(query, cat_id, location_id);
        });

        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            query = bundle.getString(TAG1);
            cat_id = bundle.getInt(TAG2);
            location_id = bundle.getInt(TAG3);

        }

        if (query.isEmpty()&&cat_id==0&&location_id==0)
        {
            getBusiness();

        }else
        {
            search(query,cat_id,location_id);
        }


        binding.tvClear.setOnClickListener(view -> {

            if (callMedia!=null)
            {
                callMedia.cancel();
            }
            if (callLoadMoreMedia!=null)
            {
                callLoadMoreMedia.cancel();
                isLoading = false;
            }


            if (searchCall!=null)
            {
                searchCall.cancel();
            }

            if (searchLoadMoreCall!=null)
            {
                searchLoadMoreCall.cancel();
                isLoading = false;

            }

            getBusiness();

        });
        initMap();
        getLocation();
        getCategoryListing();
        getDirectory();



    }

    private void getDirectory() {

        Api.getService(Tags.base_url2).
                getDirectory(lang).enqueue(new Callback<DirectoryDataModel>() {
            @Override
            public void onResponse(Call<DirectoryDataModel> call, Response<DirectoryDataModel> response) {
                binding.progBarDirectory.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {

                    directoryModelList.clear();
                    directoryModelList.addAll(response.body().getDirAds());


                    if (directoryModelList.size() > 0) {
                        directoryAdapter.notifyDataSetChanged();
                        binding.tvNoData1.setVisibility(View.GONE);


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
            public void onFailure(Call<DirectoryDataModel> call, Throwable t) {
                binding.progBarDirectory.setVisibility(View.GONE);
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

    private void getBusiness() {
        index = 0;

        binding.tvClear.setVisibility(View.GONE);
        businessDataModelList.clear();
        businessAdapter.notifyDataSetChanged();
        binding.progBarSearch.setVisibility(View.VISIBLE);
        binding.tvNoData2.setVisibility(View.GONE);
        searchType =0;

        if (searchCall!=null)
        {
            searchCall.cancel();
        }
        if (searchLoadMoreCall!=null)
        {
            searchLoadMoreCall.cancel();
        }
        if (callLoadMore!=null)
        {
            callLoadMore.cancel();
        }
        if (callMedia!=null)
        {
            callMedia.cancel();
        }

        if (callLoadMoreMedia!=null)
        {
            callLoadMoreMedia.cancel();
        }

       call = Api.getService(Tags.base_url1).getBusiness(lang, 1);

       call.enqueue(new Callback<List<BusinessDataModel>>() {
            @Override
            public void onResponse(Call<List<BusinessDataModel>> call, Response<List<BusinessDataModel>> response) {

                if (response.headers().get("X-WP-Total") != null) {
                    int count = Integer.parseInt(response.headers().get("X-WP-Total"));
                    binding.setCount(count);
                }
                if (response.isSuccessful() && response.body() != null) {

                    inCompleteBusinessDataModelList.clear();
                    inCompleteBusinessDataModelList.addAll(response.body());

                    if (inCompleteBusinessDataModelList.size() > 0) {
                        binding.tvNoData2.setVisibility(View.GONE);
                        getImages(inCompleteBusinessDataModelList.get(0).getFeatured_media());

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
            public void onFailure(Call<List<BusinessDataModel>> call, Throwable t) {
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

    private void getImages(int media_id) {

        callMedia = Api.getService(Tags.base_url1).getMedia(media_id);
        callMedia.enqueue(new Callback<MediaModel>() {
            @Override
            public void onResponse(Call<MediaModel> call, Response<MediaModel> response) {


                if (response.isSuccessful() && response.body() != null && response.body().getGuid() != null) {


                    BusinessDataModel model = inCompleteBusinessDataModelList.get(index);
                    model.setImagePath(response.body().getGuid().getRendered());
                    inCompleteBusinessDataModelList.set(index,model);
                    index++;
                    if (index<inCompleteBusinessDataModelList.size())
                    {
                        getImages(inCompleteBusinessDataModelList.get(index).getFeatured_media());
                    }else
                        {
                            binding.progBarSearch.setVisibility(View.GONE);

                            index=0;

                            businessDataModelList.clear();
                            businessDataModelList.addAll(inCompleteBusinessDataModelList);
                            businessAdapter.notifyDataSetChanged();
                            addMarker();
                        }

                }else if (response.code()==404){

                    BusinessDataModel model = inCompleteBusinessDataModelList.get(index);
                    model.setImagePath("");
                    inCompleteBusinessDataModelList.set(index,model);
                    index++;
                    if (index<inCompleteBusinessDataModelList.size())
                    {
                        getImages(inCompleteBusinessDataModelList.get(index).getFeatured_media());
                    }else
                    {
                        binding.progBarSearch.setVisibility(View.GONE);

                        index=0;

                        businessDataModelList.clear();
                        businessDataModelList.addAll(inCompleteBusinessDataModelList);
                        businessAdapter.notifyDataSetChanged();
                        addMarker();
                    }
                }else {
                    try {

                        Log.e("error", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onFailure(Call<MediaModel> call, Throwable t) {
                try {
                    binding.progBarSearch.setVisibility(View.GONE);

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

    private void getImagesLoadMore(int media_id) {

        callLoadMoreMedia = Api.getService(Tags.base_url1).
                getMedia(media_id);
        callLoadMoreMedia.enqueue(new Callback<MediaModel>() {
            @Override
            public void onResponse(Call<MediaModel> call, Response<MediaModel> response) {



                if (response.isSuccessful() && response.body() != null && response.body().getGuid() != null) {

                    BusinessDataModel model = inCompleteBusinessDataModelList.get(index);
                    model.setImagePath(response.body().getGuid().getRendered());
                    inCompleteBusinessDataModelList.set(index,model);
                    index++;
                    if (index<inCompleteBusinessDataModelList.size())
                    {

                        getImagesLoadMore(inCompleteBusinessDataModelList.get(index).getFeatured_media());
                    }else
                    {
                        index=0;

                        businessDataModelList.remove(businessDataModelList.size() - 1);
                        businessAdapter.notifyItemRemoved(businessDataModelList.size() - 1);
                        isLoading = false;
                        int oldPos = businessDataModelList.size() - 1;

                        businessDataModelList.addAll(inCompleteBusinessDataModelList);

                        Log.e("2","2");

                        if (inCompleteBusinessDataModelList.size() > 0) {
                            addMarker();
                            businessAdapter.notifyItemRangeChanged(oldPos, businessDataModelList.size() - 1);
                            currentPage += 1;

                            Log.e("3","3");


                        }

                    }

                }else if (response.code()==404){

                    BusinessDataModel model = inCompleteBusinessDataModelList.get(index);
                    model.setImagePath("");
                    inCompleteBusinessDataModelList.set(index,model);
                    index++;
                    Log.e("4","4");

                    if (index<inCompleteBusinessDataModelList.size())
                    {

                        getImagesLoadMore(inCompleteBusinessDataModelList.get(index).getFeatured_media());
                    }else
                    {
                        Log.e("5","5");


                        businessDataModelList.remove(businessDataModelList.size() - 1);
                        businessAdapter.notifyItemRemoved(businessDataModelList.size() - 1);
                        isLoading = false;
                        int oldPos = businessDataModelList.size() - 1;

                        index=0;
                        businessDataModelList.addAll(inCompleteBusinessDataModelList);


                        if (inCompleteBusinessDataModelList.size() > 0) {
                            Log.e("6","6");

                            addMarker();

                            businessAdapter.notifyItemRangeChanged(oldPos, businessDataModelList.size() - 1);
                            currentPage += 1;

                        }
                    }
                }else {
                    try {

                        Log.e("error", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onFailure(Call<MediaModel> call, Throwable t) {
                try {
                    if (businessDataModelList.get(businessDataModelList.size() - 1) == null) {
                        businessDataModelList.remove(businessDataModelList.size() - 1);
                        businessAdapter.notifyItemRemoved(businessDataModelList.size() - 1);
                        isLoading = false;
                    }

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

    private void loadMoreBusiness(int page) {
        index = 0;

        callLoadMore =  Api.getService(Tags.base_url1).getBusiness(lang, page);

        callLoadMore.enqueue(new Callback<List<BusinessDataModel>>() {
            @Override
            public void onResponse(Call<List<BusinessDataModel>> call, Response<List<BusinessDataModel>> response) {


                if (response.isSuccessful() && response.body() != null) {


                    inCompleteBusinessDataModelList.clear();
                    inCompleteBusinessDataModelList.addAll(response.body());

                    Log.e("6",inCompleteBusinessDataModelList.size()+"_");

                    if (inCompleteBusinessDataModelList.size()>0)
                    {
                        getImagesLoadMore(inCompleteBusinessDataModelList.get(0).getFeatured_media());

                    }





                } else if (response.code()==400){}else {
                    try {
                        businessDataModelList.remove(businessDataModelList.size() - 1);
                        businessAdapter.notifyItemRemoved(businessDataModelList.size() - 1);
                        isLoading = false;
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
            public void onFailure(Call<List<BusinessDataModel>> call, Throwable t) {

                if (businessDataModelList.get(businessDataModelList.size() - 1) == null) {
                    businessDataModelList.remove(businessDataModelList.size() - 1);
                    businessAdapter.notifyItemRemoved(businessDataModelList.size() - 1);
                    isLoading = false;
                }

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

                            if (response.body().size() > 0) {
                                spinnerLocationModelList.addAll(response.body());
                                activity.runOnUiThread(() -> {
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

                            if (response.body().size() > 0) {
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


        if (spinnerCategoryListingModelList.size() > 0) {
            if (lang.equals("ar")) {
                selectedListingModelList.addAll(getArCategoryListing());
                spinnerCategoryListingAdapter.notifyDataSetChanged();
            } else {
                selectedListingModelList.addAll(getEnCategoryListing());
                spinnerCategoryListingAdapter.notifyDataSetChanged();
            }






        }
    }

    private void updateSelectedLocation()
    {

        if (location_id!=0)
        {
            for (int i =0;i<spinnerLocationModelList.size();i++)
            {
                if (spinnerLocationModelList.get(i).getId()==location_id)
                {
                    binding.spinnerLocation.setSelection(i);
                    break;
                }
            }
        }
    }

    public void updateSpinnersSelectedData(String query,int category_id,int location_id)
    {
        this.query =query;
        this.cat_id = category_id;
        this.location_id=location_id;
        binding.edtKeyword.setText(query);

        if (selectedListingModelList.size()>0)
        {
            if (cat_id!=0)
            {
                for (int i =0;i<selectedListingModelList.size();i++)
                {

                    if (selectedListingModelList.get(i).getId()==cat_id)
                    {
                        binding.spinnerCategoryListing.setSelection(i);
                        break;
                    }
                }
            }
        }

        updateSelectedLocation();



    }

    public void checkSearch(String query, int cat_id, int location_id) {


        if (cat_id == 0 && location_id == 0) {
            if (query.isEmpty()) {
                binding.edtKeyword.setError(getString(R.string.field_req));
            } else {
                binding.edtKeyword.setError(null);
            }
        } else {

            binding.edtKeyword.setError(null);
            search(query, cat_id, location_id);


        }

    }


    private void search(String query, int cat_id, int location_id) {
        searchType = 1;
        index = 0;
        if (call!=null)
        {
            call.cancel();
        }
        if (callLoadMore!=null)
        {
            callLoadMore.cancel();
        }
        if (callMedia!=null)
        {
            callMedia.cancel();
        }

        if (callLoadMoreMedia!=null)
        {
            callLoadMoreMedia.cancel();
        }

        binding.progBarSearch.setVisibility(View.VISIBLE);
        binding.setCount(0);
        currentPage = 1;
        businessDataModelList.clear();
        businessAdapter.notifyDataSetChanged();
        binding.tvNoData2.setVisibility(View.GONE);


        String q =null;
        String loc_id=null;
        String category_id=null;

        if (!query.isEmpty())
        {
            q = query;

        }

        if (location_id!=0)
        {
            loc_id = String.valueOf(location_id);

        }

        if (cat_id!=0)
        {
            category_id = String.valueOf(cat_id);

        }



        searchCall = Api.getService(Tags.base_url1).search(lang, 1,category_id,loc_id,q);
        searchCall.enqueue(new Callback<List<BusinessDataModel>>() {
            @Override
            public void onResponse(Call<List<BusinessDataModel>> call, Response<List<BusinessDataModel>> response) {

                updateSpinnersSelectedData(query, cat_id, location_id);
                if (response.headers().get("X-WP-Total") != null) {
                    int count = Integer.parseInt(response.headers().get("X-WP-Total"));
                    binding.setCount(count);
                }
                if (response.isSuccessful() && response.body() != null) {

                    binding.tvClear.setVisibility(View.VISIBLE);

                    inCompleteBusinessDataModelList.clear();
                    inCompleteBusinessDataModelList.addAll(response.body());

                    if (inCompleteBusinessDataModelList.size() > 0) {


                        getImages(inCompleteBusinessDataModelList.get(0).getFeatured_media());
                        binding.tvNoData2.setVisibility(View.GONE);


                    } else {
                        binding.progBarSearch.setVisibility(View.GONE);

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
            public void onFailure(Call<List<BusinessDataModel>> call, Throwable t) {
                binding.progBarSearch.setVisibility(View.GONE);
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

    private void loadMoreSearch(String query, int page, int cat_id, int location_id) {
        index = 0;

        String q =null;
        String loc_id=null;
        String category_id=null;

        if (!query.isEmpty())
        {
            q = query;

        }

        if (location_id!=0)
        {
            loc_id = String.valueOf(location_id);

        }

        if (cat_id!=0)
        {
            category_id = String.valueOf(cat_id);

        }


        searchLoadMoreCall = Api.getService(Tags.base_url1).search(lang, page, category_id, loc_id, q);

        searchLoadMoreCall.enqueue(new Callback<List<BusinessDataModel>>() {
            @Override
            public void onResponse(Call<List<BusinessDataModel>> call, Response<List<BusinessDataModel>> response) {


                if (response.isSuccessful() && response.body() != null) {

                    inCompleteBusinessDataModelList.addAll(response.body());

                    if(inCompleteBusinessDataModelList.size()>0)
                    {
                        getImagesLoadMore(inCompleteBusinessDataModelList.get(0).getFeatured_media());

                    }

                }else if (response.code()==400)
                {
                    businessDataModelList.remove(businessDataModelList.size() - 1);
                    businessAdapter.notifyItemRemoved(businessDataModelList.size() - 1);
                    isLoading = false;
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
            public void onFailure(Call<List<BusinessDataModel>> call, Throwable t) {

                if (businessDataModelList.get(businessDataModelList.size() - 1) == null) {
                    businessDataModelList.remove(businessDataModelList.size() - 1);
                    businessAdapter.notifyItemRemoved(businessDataModelList.size() - 1);
                    isLoading = false;
                }

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


    private List<SpinnerModel> getArCategoryListing() {
        List<SpinnerModel> spinnerModelList = new ArrayList<>();
        int end = spinnerCategoryListingModelList.size() / 2;

        for (int i = 0; i < end; i++) {
            spinnerModelList.add(spinnerCategoryListingModelList.get(i));
        }

        return spinnerModelList;
    }


    private List<SpinnerModel> getEnCategoryListing() {
        List<SpinnerModel> spinnerModelList = new ArrayList<>();
        int start = spinnerCategoryListingModelList.size() / 2;

        for (int i = start; i < spinnerCategoryListingModelList.size(); i++) {
            spinnerModelList.add(spinnerCategoryListingModelList.get(i));
            Log.e("idddddd",spinnerCategoryListingModelList.get(i).getId()+"__");
        }

        return spinnerModelList;
    }





    private void initMap() {

        fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (fragment != null) {
            fragment.getMapAsync(this);

        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (googleMap != null) {
            mMap = googleMap;
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(activity, R.raw.maps));
            mMap.setTrafficEnabled(false);
            mMap.setBuildingsEnabled(false);
            mMap.setIndoorEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(false);
            mMap.getUiSettings().setCompassEnabled(false);
            mMap.getUiSettings().setTiltGesturesEnabled(false);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(31.958090,35.945808),10.0f));

            clusterManager = new ClusterManager(activity,mMap);
            mMap.setOnCameraIdleListener(clusterManager);
            mMap.setOnMarkerClickListener(clusterManager);
            mMap.setOnInfoWindowClickListener(clusterManager);

            clusterRender = new ClusterRender(activity,mMap,clusterManager);
            /*clusterManager.addItems(locationList);
            clusterManager.setRenderer(clusterRender);
            clusterManager.cluster();*/


        }

    }



    private void addMarker() {
        if (businessDataModelList.size()>0)
        {
            mMap.clear();
            clusterManager.clearItems();
            locationList.clear();

            for (BusinessDataModel model: businessDataModelList)
            {
                if (model.getCmb2().getListing_map_location()!=null)
                {
                    if (!model.getCmb2().getListing_map_location().getLatitude().isEmpty()&&model.getCmb2().getListing_map_location().getLongitude().isEmpty())
                    {
                        double lat =Double.parseDouble(model.getCmb2().getListing_map_location().getLatitude());
                        double lng =Double.parseDouble(model.getCmb2().getListing_map_location().getLongitude());

                        locationList.add(new ClusterLocation(model.getCmb2().getListing_map_location().getAddress(),new LatLng(lat,lng)));

                    }

                }
            }

            Log.e("ffff",locationList.size()+"__");
            if (locationList.size()>0)
            {
                clusterManager.addItems(locationList);
                clusterManager.setRenderer(clusterRender);
                clusterManager.cluster();
            }

        }

    }


    public void setItemData(BusinessDataModel model) {

        if (model.getCmb2().getListing_business_location().getListing_locations() instanceof String) {
            String s = (String) model.getCmb2().getListing_business_location().getListing_locations();
            Log.e("sssss", s + "__");
        } else if (model.getCmb2().getListing_business_location().getListing_locations() instanceof List) {
            List<String> loc = (List<String>) model.getCmb2().getListing_business_location().getListing_locations();
            Log.e("sss", loc.size() + "_");
        }

    }


}
