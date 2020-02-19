package com.technology.circles.apps.omanmade.activities_fragments.activity_home.fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.technology.circles.apps.omanmade.databinding.DialogSpinnerBinding;
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
    private static final String TAG4="cat_name";
    private static final String TAG5="loc_name";

    private FragmentDirectoryBinding binding;
    private HomeActivity activity;
    private Preferences preferences;
    private String lang;
    private SupportMapFragment fragment;
    private GoogleMap mMap;

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

    private AlertDialog categoryDialog, locationDialog;

    private DialogSpinnerBinding dialogSpinnerCategoryBinding,dialogSpinnerLocationBinding;
    private int currentPageCategory = 1;
    private boolean isLoadingCategory = false;
    private int totalPageCategory = 1;
    private List<SpinnerModel> spinnerLocationModelList, spinnerCategoryListingModelList;
    private SpinnerLocationAdapter spinnerLocationAdapter, spinnerCategoryAdapter;

    private String cat_name="",loc_name="";
    public static Fragment_Directory newInstance(String query,int category_id,int location_id,String category_name,String location_name) {

        Bundle bundle = new Bundle();
        bundle.putString(TAG1,query);
        bundle.putInt(TAG2,category_id);
        bundle.putInt(TAG3,location_id);
        bundle.putString(TAG4,category_name);
        bundle.putString(TAG5,location_name);

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

            spinnerCategoryListingModelList.add(new SpinnerModel(0, "التصنيف", arLangCode));

        } else {
            selectedLangCode = enLangCode;

            spinnerLocationModelList.add(new SpinnerModel(0, "Location", enLangCode));
            spinnerLocationModelList.add(new SpinnerModel(0, "Other", arLangCode));

            spinnerCategoryListingModelList.add(new SpinnerModel(0, "Category", arLangCode));

        }


        binding.tvSpinnerLocation.setText(spinnerLocationModelList.get(0).getName());

        binding.tvSpinnerCategory.setText(spinnerCategoryListingModelList.get(0).getName());



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


        binding.spinnerCategory.setOnClickListener(view -> categoryDialog.show());

        binding.spinnerLocation.setOnClickListener(view -> locationDialog.show());

        spinnerCategoryAdapter = new SpinnerLocationAdapter(spinnerCategoryListingModelList, activity, this,1);
        createCategoryDialog();
        spinnerLocationAdapter = new SpinnerLocationAdapter(spinnerLocationModelList, activity, this,2);
        createLocationDialog();

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

            cat_name = bundle.getString(TAG4);
            loc_name = bundle.getString(TAG5);

        }

        if (cat_name.isEmpty())
        {
            binding.tvSpinnerCategory.setText(spinnerCategoryListingModelList.get(0).getName());
        }else
            {
                binding.tvSpinnerCategory.setText(cat_name);

            }

        if (loc_name.isEmpty())
        {
            binding.tvSpinnerLocation.setText(spinnerLocationModelList.get(0).getName());
        }else
        {
            binding.tvSpinnerLocation.setText(loc_name);

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
                        dialogSpinnerLocationBinding.progBar.setVisibility(View.GONE);

                        if (response.isSuccessful() && response.body() != null) {

                            if (response.body().size() > 0) {
                                spinnerLocationModelList.addAll(response.body());




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
                        dialogSpinnerLocationBinding.progBar.setVisibility(View.GONE);

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


    public void updateSpinnersSelectedData(String query,int category_id,int location_id)
    {
        this.query =query;
        this.cat_id = category_id;
        this.location_id=location_id;
        binding.edtKeyword.setText(query);





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
                if (model.getCmb2().getListing_business_location()!=null)
                {
                    if (!model.getCmb2().getListing_business_location().getListing_map_location().getLatitude().isEmpty()&&model.getCmb2().getListing_business_location().getListing_map_location().getLongitude().isEmpty())
                    {
                        double lat =Double.parseDouble(model.getCmb2().getListing_business_location().getListing_map_location().getLatitude());
                        double lng =Double.parseDouble(model.getCmb2().getListing_business_location().getListing_map_location().getLongitude());

                        locationList.add(new ClusterLocation(model.getCmb2().getListing_business_location().getListing_map_location().getAddress(),new LatLng(lat,lng)));

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


    public void setCategoryData(SpinnerModel spinnerModel) {
        this.cat_id = spinnerModel.getId();
        binding.tvSpinnerCategory.setText(spinnerModel.getName());
        categoryDialog.dismiss();

    }

    public void setLocationData(SpinnerModel spinnerModel, int adapterPosition) {

        if (adapterPosition!=0)
        {
            if (adapterPosition==1)
            {
                locationDialog.dismiss();

            }else
                {
                    location_id = spinnerModel.getId();
                    binding.tvSpinnerLocation.setText(spinnerModel.getName());
                    locationDialog.dismiss();
                }

        }
    }
}
