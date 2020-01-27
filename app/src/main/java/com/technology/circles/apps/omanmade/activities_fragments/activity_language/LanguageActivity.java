package com.technology.circles.apps.omanmade.activities_fragments.activity_language;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.technology.circles.apps.omanmade.R;
import com.technology.circles.apps.omanmade.activities_fragments.activity_home.HomeActivity;
import com.technology.circles.apps.omanmade.databinding.ActivityLanguageBinding;
import com.technology.circles.apps.omanmade.language.LanguageHelper;
import com.technology.circles.apps.omanmade.preferences.Preferences;

import io.paperdb.Paper;

public class LanguageActivity extends AppCompatActivity {
    private ActivityLanguageBinding binding;
    private Preferences preferences;
    private String selected_language = "ar";

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang","ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_language);
        initView();
    }

    private void initView() {
        Paper.init(this);
        preferences = Preferences.newInstance();

        if(preferences.isLangSelected(this))
        {
            navigateToHomeActivity();
        }


        binding.rbAr.setOnClickListener(v -> selected_language = "ar"
        );

        binding.rbEn.setOnClickListener(v -> selected_language = "en"
        );
        binding.fab.setOnClickListener(v ->
                refreshActivity(selected_language)

        );

    }

    private void navigateToHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void refreshActivity(String selected_language) {

        Paper.book().write("lang",selected_language);
        preferences.saveSelectedLanguage(this);
        navigateToHomeActivity();
    }
}
