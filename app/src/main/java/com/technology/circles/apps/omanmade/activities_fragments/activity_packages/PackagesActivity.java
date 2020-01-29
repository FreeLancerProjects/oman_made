package com.technology.circles.apps.omanmade.activities_fragments.activity_packages;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.technology.circles.apps.omanmade.R;
import com.technology.circles.apps.omanmade.adapter.PackageAdapter;
import com.technology.circles.apps.omanmade.databinding.ActivityPackagesBinding;
import com.technology.circles.apps.omanmade.interfaces.Listeners;
import com.technology.circles.apps.omanmade.language.LanguageHelper;
import com.technology.circles.apps.omanmade.models.PackageDataModel;
import com.technology.circles.apps.omanmade.remote.Api;
import com.technology.circles.apps.omanmade.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PackagesActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityPackagesBinding binding;
    private String lang;
    private List<PackageDataModel.Packages> packagesList;
    private PackageAdapter adapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_packages);
        initView();

    }


    private void initView() {
        packagesList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        adapter = new PackageAdapter(packagesList,this);
        binding.recView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        binding.recView.setAdapter(adapter);
        getPackages();
    }

    public void getPackages() {
        binding.progBar.setVisibility(View.VISIBLE);

        Api.getService(Tags.base_url2)
                .getPackages(lang)
                .enqueue(new Callback<PackageDataModel>() {
                    @Override
                    public void onResponse(Call<PackageDataModel> call, Response<PackageDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null && response.body().getPackages() != null) {

                            packagesList.addAll(response.body().getPackages());
                            if (response.body().getPackages().size() > 0) {
                                binding.tvNoData.setVisibility(View.GONE);
                                adapter.notifyDataSetChanged();

                            } else {
                                binding.tvNoData.setVisibility(View.VISIBLE);

                            }
                        } else {

                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(PackagesActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(PackagesActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                            }
                            try {
                                Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<PackageDataModel> call, Throwable t) {
                        try {


                            binding.progBar.setVisibility(View.GONE);

                            Toast.makeText(PackagesActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();


                            try {
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(PackagesActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(PackagesActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        } catch (Exception e) {
                        }
                    }
                });

    }

    @Override
    public void back() {
        finish();
    }
}
