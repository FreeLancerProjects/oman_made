package com.technology.circles.apps.omanmade.activities_fragments.activity_faqs;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.technology.circles.apps.omanmade.R;
import com.technology.circles.apps.omanmade.adapter.FagsAdapter;
import com.technology.circles.apps.omanmade.databinding.ActivityFaqsBinding;
import com.technology.circles.apps.omanmade.interfaces.Listeners;
import com.technology.circles.apps.omanmade.language.LanguageHelper;
import com.technology.circles.apps.omanmade.models.FaqsModel;
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

public class FaqsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityFaqsBinding binding;
    private String lang;
    private List<FaqsModel.Faqs> faqsLis;
    private FagsAdapter fagsAdapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_faqs);
        initView();

    }


    private void initView() {
        faqsLis = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        fagsAdapter = new FagsAdapter(faqsLis, this);
        binding.recView.setLayoutManager(new GridLayoutManager(this, 1));
        binding.recView.setAdapter(fagsAdapter);
        getFaqs();
    }

    public void getFaqs() {
        binding.progBar.setVisibility(View.VISIBLE);

        Api.getService(Tags.base_url2)
                .getFaqs(lang)
                .enqueue(new Callback<FaqsModel>() {
                    @Override
                    public void onResponse(Call<FaqsModel> call, Response<FaqsModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null && response.body().getFaqs() != null) {

                            faqsLis.addAll(response.body().getFaqs());
                            if (response.body().getFaqs().size() > 0) {
                                binding.tvNoData.setVisibility(View.GONE);
                                fagsAdapter.notifyDataSetChanged();

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
                                Toast.makeText(FaqsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(FaqsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                            }
                            try {
                                Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<FaqsModel> call, Throwable t) {
                        try {


                            binding.progBar.setVisibility(View.GONE);

                            Toast.makeText(FaqsActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();


                            try {
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(FaqsActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(FaqsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
