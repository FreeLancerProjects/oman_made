package com.technology.circles.apps.omanmade.activities_fragments.activity_home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.material.tabs.TabLayout;
import com.technology.circles.apps.omanmade.R;
import com.technology.circles.apps.omanmade.activities_fragments.activity_about.AboutActivity;
import com.technology.circles.apps.omanmade.activities_fragments.activity_catalogue.CataLogueActivity;
import com.technology.circles.apps.omanmade.activities_fragments.activity_contact.ContactUsActivity;
import com.technology.circles.apps.omanmade.activities_fragments.activity_faqs.FaqsActivity;
import com.technology.circles.apps.omanmade.activities_fragments.activity_home.fragments.Fragment_Directory;
import com.technology.circles.apps.omanmade.activities_fragments.activity_home.fragments.Fragment_Home;
import com.technology.circles.apps.omanmade.activities_fragments.activity_home.fragments.Fragment_Industry;
import com.technology.circles.apps.omanmade.activities_fragments.activity_home.fragments.Fragment_Sponsor;
import com.technology.circles.apps.omanmade.activities_fragments.activity_packages.PackagesActivity;
import com.technology.circles.apps.omanmade.activities_fragments.activity_peie.PeieActivity;
import com.technology.circles.apps.omanmade.activities_fragments.activity_service.ServiceActivity;
import com.technology.circles.apps.omanmade.activities_fragments.activity_web_view.WebViewActivity;
import com.technology.circles.apps.omanmade.language.LanguageHelper;
import com.technology.circles.apps.omanmade.models.AppDataModel;
import com.technology.circles.apps.omanmade.remote.Api;
import com.technology.circles.apps.omanmade.tags.Tags;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.io.IOException;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private AHBottomNavigation ah_bottom_nav;
    private FragmentManager fragmentManager;
    private Fragment_Home fragment_home;
    private Fragment_Directory fragment_directory;
    private Fragment_Industry fragment_industry;
    private Fragment_Sponsor fragment_sponsor;
    private String lang;
    private AppDataModel appDataModel;
    ///////////////////////////////////////////////////
    private CardView cardViewMainHome, cardViewHome, cardViewDirectory, cardViewCreateList, cardViewProfile,
            cardViewMainAbout, cardViewCatalogue, cardViewService, cardViewPackage, cardViewAbout, cardViewPeie,
            cardViewMainSupport, cardViewFaq, cardViewContact,
            cardViewMainLegal, cardViewPrivacy, cardViewTerms;

    private ImageView arrow1, arrow2, arrow3, arrow4, imgFacebook, imgIntagram, imgWhatsApp, imgTwitter;
    private ExpandableLayout expandLayoutHome, expandLayoutAbout, expandLayoutSupport, expandLayoutLegal;
    private TabLayout tab;
    private TextView tvRate, tvTitle;
    private LinearLayout llHomeContent;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            DisplayFragmentHome();
        }

    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(ContextCompat.getColor(this, R.color.black));
        ah_bottom_nav = findViewById(R.id.ah_bottom_nav);
        setUpBottomNavigation();


        cardViewMainHome = findViewById(R.id.cardViewMainHome);
        cardViewHome = findViewById(R.id.cardViewHome);
        cardViewDirectory = findViewById(R.id.cardViewDirectory);
        cardViewCreateList = findViewById(R.id.cardViewCreateList);
        cardViewProfile = findViewById(R.id.cardViewProfile);
        cardViewMainAbout = findViewById(R.id.cardViewMainAbout);
        cardViewCatalogue = findViewById(R.id.cardViewCatalogue);
        cardViewService = findViewById(R.id.cardViewService);
        cardViewPackage = findViewById(R.id.cardViewPackage);
        cardViewAbout = findViewById(R.id.cardViewAbout);
        cardViewPeie = findViewById(R.id.cardViewPeie);
        cardViewMainSupport = findViewById(R.id.cardViewMainSupport);
        cardViewFaq = findViewById(R.id.cardViewFaq);
        cardViewContact = findViewById(R.id.cardViewContact);
        cardViewMainLegal = findViewById(R.id.cardViewMainLegal);
        cardViewPrivacy = findViewById(R.id.cardViewPrivacy);
        cardViewTerms = findViewById(R.id.cardViewTerms);
        arrow1 = findViewById(R.id.arrow1);
        arrow2 = findViewById(R.id.arrow2);
        arrow3 = findViewById(R.id.arrow3);
        arrow4 = findViewById(R.id.arrow4);
        imgFacebook = findViewById(R.id.imgFacebook);
        imgIntagram = findViewById(R.id.imgIntagram);
        imgWhatsApp = findViewById(R.id.imgWhatsApp);
        imgTwitter = findViewById(R.id.imgTwitter);
        expandLayoutHome = findViewById(R.id.expandLayoutHome);
        expandLayoutAbout = findViewById(R.id.expandLayoutAbout);
        expandLayoutSupport = findViewById(R.id.expandLayoutSupport);
        expandLayoutLegal = findViewById(R.id.expandLayoutLegal);
        llHomeContent = findViewById(R.id.llHomeContent);

        tab = findViewById(R.id.tab);
        tvRate = findViewById(R.id.tvRate);
        tvTitle = findViewById(R.id.tvTitle);

        tab.addTab(tab.newTab().setText("عربي"));
        tab.addTab(tab.newTab().setText("English"));

        if (lang.equals("ar")) {
            tab.getTabAt(0).select();
        } else {
            tab.getTabAt(1).select();

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawer.setElevation(0.0f);
        }

        drawer.setScrimColor(Color.TRANSPARENT);

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

                float slideX = drawerView.getWidth() * slideOffset;
                if (lang.equals("ar")) {
                    slideX = slideX * -1;
                }
                llHomeContent.setTranslationX(slideX);

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });


        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                drawer.closeDrawer(GravityCompat.START);


                int pos = tab.getPosition();
                if (pos == 0) {
                    RefreshActivity("ar");
                } else {
                    RefreshActivity("en");

                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        cardViewMainHome.setOnClickListener(view -> {


            expandLayoutSupport.collapse(true);
            expandLayoutLegal.collapse(true);
            expandLayoutAbout.collapse(true);
            if (expandLayoutHome.isExpanded()) {
                expandLayoutHome.collapse(true);
                arrow1.animate().rotationBy(-180).setDuration(500).start();
            } else {
                expandLayoutHome.expand(true);
                arrow1.animate().rotationBy(180).setDuration(500).start();
            }

        });

        cardViewMainAbout.setOnClickListener(view -> {

            expandLayoutSupport.collapse(true);
            expandLayoutLegal.collapse(true);
            expandLayoutHome.collapse(true);

            if (expandLayoutAbout.isExpanded()) {
                expandLayoutAbout.collapse(true);
                arrow2.animate().rotationBy(-180).setDuration(500).start();
            } else {
                expandLayoutAbout.expand(true);
                arrow2.animate().rotationBy(180).setDuration(500).start();
            }

        });

        cardViewMainSupport.setOnClickListener(view -> {

            expandLayoutAbout.collapse(true);
            expandLayoutLegal.collapse(true);
            expandLayoutHome.collapse(true);

            if (expandLayoutSupport.isExpanded()) {
                expandLayoutSupport.collapse(true);
                arrow3.animate().rotationBy(-180).setDuration(500).start();
            } else {
                expandLayoutSupport.expand(true);
                arrow3.animate().rotationBy(180).setDuration(500).start();
            }

        });

        cardViewMainLegal.setOnClickListener(view -> {

            expandLayoutAbout.collapse(true);
            expandLayoutSupport.collapse(true);
            expandLayoutHome.collapse(true);

            if (expandLayoutLegal.isExpanded()) {
                expandLayoutLegal.collapse(true);
                arrow4.animate().rotationBy(-180).setDuration(500).start();
            } else {
                expandLayoutLegal.expand(true);
                arrow4.animate().rotationBy(180).setDuration(500).start();
            }

        });

        cardViewContact.setOnClickListener(view -> {

            arrow3.animate().rotationBy(-180).setDuration(500).start();
            expandLayoutSupport.collapse(true);
            new Handler()
                    .postDelayed(() -> drawer.closeDrawer(GravityCompat.START), 500);

            new Handler()
                    .postDelayed(this::navigateToContactActivity, 1000);

        });

        cardViewHome.setOnClickListener(view -> {

            arrow1.animate().rotationBy(-180).setDuration(500).start();
            expandLayoutHome.collapse(true);
            new Handler()
                    .postDelayed(() -> {
                        drawer.closeDrawer(GravityCompat.START);
                        DisplayFragmentHome();
                    }, 500);


        });

        cardViewDirectory.setOnClickListener(view -> {
            arrow1.animate().rotationBy(-180).setDuration(500).start();

            expandLayoutHome.collapse(true);
            new Handler()
                    .postDelayed(() -> {
                        drawer.closeDrawer(GravityCompat.START);
                        DisplayFragmentDirectory(0,"",0,0,"","");
                    }, 500);


        });

        cardViewCreateList.setOnClickListener(view -> {
            arrow1.animate().rotationBy(-180).setDuration(500).start();

            expandLayoutHome.collapse(true);
            new Handler()
                    .postDelayed(() ->drawer.closeDrawer(GravityCompat.START), 500);


            new Handler()
                    .postDelayed(() -> {
                        String url;
                        if (lang.equals("ar"))
                        {
                            url = "https://www.omanmade.com/ar/ar-create/";
                        }else
                        {
                            url = "https://www.omanmade.com/create/";
                        }
                        Intent intent = new Intent(HomeActivity.this, WebViewActivity.class);
                        intent.putExtra("url",url);
                        startActivity(intent);
                    },1000);


        });

        cardViewProfile.setOnClickListener(view -> {
            arrow1.animate().rotationBy(-180).setDuration(500).start();

            expandLayoutHome.collapse(true);

            new Handler()
                    .postDelayed(() ->drawer.closeDrawer(GravityCompat.START),500);

            new Handler()
                    .postDelayed(() -> {

                        String url ="https://www.omanmade.com/ar/ar-profile/";

                        Intent intent = new Intent(HomeActivity.this, WebViewActivity.class);
                        intent.putExtra("url",url);
                        startActivity(intent);

                    }, 1000);


        });

        cardViewAbout.setOnClickListener(v -> {
            arrow2.animate().rotationBy(-180).setDuration(500).start();
            expandLayoutAbout.collapse(true);
            new Handler()
                    .postDelayed(() -> drawer.closeDrawer(GravityCompat.START), 500);


            new Handler()
                    .postDelayed(()->navigateToAboutActivity(1), 1000);


        });

        cardViewPrivacy.setOnClickListener(v -> {
            arrow4.animate().rotationBy(-180).setDuration(500).start();
            expandLayoutLegal.collapse(true);
            new Handler()
                    .postDelayed(() -> drawer.closeDrawer(GravityCompat.START), 500);


            new Handler()
                    .postDelayed(()->navigateToAboutActivity(2), 1000);


        });

        cardViewTerms.setOnClickListener(v -> {
            arrow4.animate().rotationBy(-180).setDuration(500).start();
            expandLayoutLegal.collapse(true);
            new Handler()
                    .postDelayed(() -> drawer.closeDrawer(GravityCompat.START), 500);


            new Handler()
                    .postDelayed(()->navigateToAboutActivity(3), 1000);


        });

        cardViewCatalogue.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CataLogueActivity.class);
            startActivity(intent);
        });

        cardViewPeie.setOnClickListener(v -> {

            arrow2.animate().rotationBy(-180).setDuration(500).start();
            expandLayoutAbout.collapse(true);
            new Handler()
                    .postDelayed(() -> drawer.closeDrawer(GravityCompat.START), 500);


            new Handler().postDelayed(this::navigateToPeieActivity, 1000);
        });
        cardViewFaq.setOnClickListener(v -> {
            arrow3.animate().rotationBy(-180).setDuration(500).start();
            expandLayoutSupport.collapse(true);

            new Handler().postDelayed(() -> drawer.closeDrawer(GravityCompat.START), 500);


            new Handler().postDelayed(this::navigateToFaqActivity, 1000);


        });

        cardViewPackage.setOnClickListener(v -> {

            arrow2.animate().rotationBy(-180).setDuration(500).start();
            expandLayoutAbout.collapse(true);
            new Handler()
                    .postDelayed(() -> drawer.closeDrawer(GravityCompat.START), 500);


            new Handler().postDelayed(this::navigateToPackageActivity, 1000);
        });


        cardViewService.setOnClickListener(v -> {

            arrow2.animate().rotationBy(-180).setDuration(500).start();
            expandLayoutAbout.collapse(true);
            new Handler()
                    .postDelayed(() -> drawer.closeDrawer(GravityCompat.START), 500);


            new Handler().postDelayed(this::navigateToServiceActivity, 1000);
        });

        imgFacebook.setOnClickListener(view -> {
            drawer.closeDrawer(GravityCompat.START);
            if (appDataModel!=null&&!appDataModel.getFacebook().isEmpty()){

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(appDataModel.getFacebook()));
                startActivity(intent);
            }else
                {
                    Toast.makeText(this, R.string.not_av, Toast.LENGTH_SHORT).show();
                }

        });

        imgIntagram.setOnClickListener(view -> {
            drawer.closeDrawer(GravityCompat.START);

            if (appDataModel!=null&&!appDataModel.getInstagram().isEmpty()){

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(appDataModel.getInstagram()));
                startActivity(intent);
            }else
            {
                Toast.makeText(this, R.string.not_av, Toast.LENGTH_SHORT).show();
            }

        });

        imgTwitter.setOnClickListener(view -> {
            drawer.closeDrawer(GravityCompat.START);

            if (appDataModel!=null&&!appDataModel.getTwitter().isEmpty()){

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(appDataModel.getTwitter()));
                startActivity(intent);
            }else
            {
                Toast.makeText(this, R.string.not_av, Toast.LENGTH_SHORT).show();
            }

        });

        imgWhatsApp.setOnClickListener(view -> {
            drawer.closeDrawer(GravityCompat.START);

            if (appDataModel!=null&&!appDataModel.getMob_number1().isEmpty()){

                String phone = appDataModel.getMob_number1().replace("(","").replace(")","").replaceAll("_","");

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone="+phone));
                startActivity(intent);
            }else
            {
                Toast.makeText(this, R.string.not_av, Toast.LENGTH_SHORT).show();
            }

        });


        tvRate.setOnClickListener(view -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
            }

        });

        getAbout();
    }


    private void getAbout() {

        Api.getService(Tags.base_url2)
                .getSetting(lang)
                .enqueue(new Callback<AppDataModel>() {
                    @Override
                    public void onResponse(Call<AppDataModel> call, Response<AppDataModel> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            appDataModel = response.body();
                        } else {
                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 500) {
                                Toast.makeText(HomeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(HomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AppDataModel> call, Throwable t) {
                        try {
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(HomeActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {
                        }
                    }
                });
    }


    private void navigateToServiceActivity() {

        Intent intent = new Intent(HomeActivity.this, ServiceActivity.class);
        startActivity(intent);
    }

    private void navigateToPackageActivity() {

        Intent intent = new Intent(HomeActivity.this, PackagesActivity.class);
        startActivity(intent);
    }

    private void navigateToPeieActivity() {
        Intent intent = new Intent(HomeActivity.this, PeieActivity.class);
        startActivity(intent);
    }

    private void navigateToAboutActivity(int type) {
        Intent intent = new Intent(HomeActivity.this, AboutActivity.class);
        intent.putExtra("type",type);
        startActivity(intent);
    }

    private void navigateToFaqActivity() {
        Intent intent = new Intent(HomeActivity.this, FaqsActivity.class);
        startActivity(intent);
    }

    private void navigateToContactActivity() {
        Intent intent = new Intent(this, ContactUsActivity.class);
        startActivity(intent);
    }

    private void setUpBottomNavigation()
    {

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(getString(R.string.home), R.drawable.ic_home);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getString(R.string.directory), R.drawable.ic_search);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(getString(R.string.indus_Area), R.drawable.ic_industry);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(getString(R.string.spons), R.drawable.ic_team);

        ah_bottom_nav.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        ah_bottom_nav.setDefaultBackgroundColor(ContextCompat.getColor(this, R.color.white));
        ah_bottom_nav.setTitleTextSizeInSp(12, 12);
        ah_bottom_nav.setForceTint(true);
        ah_bottom_nav.setAccentColor(ContextCompat.getColor(this, R.color.colorPrimary));
        ah_bottom_nav.setInactiveColor(ContextCompat.getColor(this, R.color.black));
        ah_bottom_nav.addItem(item1);
        ah_bottom_nav.addItem(item2);
        ah_bottom_nav.addItem(item3);
        ah_bottom_nav.addItem(item4);

        ah_bottom_nav.setOnTabSelectedListener((position, wasSelected) -> {
            switch (position) {
                case 0:
                    DisplayFragmentHome();
                    break;
                case 1:
                    DisplayFragmentDirectory(0,"",0,0,"","");
                    break;
                case 2:
                    DisplayFragmentIndustrialArea();
                    break;
                case 3:
                    DisplayFragmentSponsor();
                    break;


            }
            return false;
        });

        ah_bottom_nav.setCurrentItem(0, false);


    }
    private void DisplayFragmentHome() {
        if (fragment_home == null) {
            fragment_home = Fragment_Home.newInstance();
        }

        if (fragment_directory != null && fragment_directory.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_directory).commit();
        }

        if (fragment_industry != null && fragment_industry.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_industry).commit();
        }

        if (fragment_sponsor != null && fragment_sponsor.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_sponsor).commit();
        }


        if (fragment_home.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_home).commit();
            fragment_home.startTimer();

        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_home_container, fragment_home, "fragment_home").addToBackStack("fragment_home").commit();

        }
        ah_bottom_nav.setCurrentItem(0, false);

    }

    public void DisplayFragmentDirectory(int type,String query,int category_id,int location_id,String category_name,String location_name) {

        if (fragment_directory == null) {
            fragment_directory = Fragment_Directory.newInstance(query, category_id, location_id,category_name,location_name);
        }

        if (fragment_home != null && fragment_home.isAdded()) {
            fragment_home.stopTimer();
            fragmentManager.beginTransaction().hide(fragment_home).commit();
        }

        if (fragment_industry != null && fragment_industry.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_industry).commit();
        }

        if (fragment_sponsor != null && fragment_sponsor.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_sponsor).commit();
        }


        if (fragment_directory.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_directory).commit();
            if (type==1)
            {
                fragment_directory.updateSpinnersSelectedData(query,category_id,location_id);
                fragment_directory.checkSearch(query,category_id,location_id);
            }

        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_home_container, fragment_directory, "fragment_directory").addToBackStack("fragment_directory").commit();

        }


        ah_bottom_nav.setCurrentItem(1, false);


    }

    private void DisplayFragmentIndustrialArea() {


        if (fragment_industry == null) {
            fragment_industry = Fragment_Industry.newInstance();
        }

        if (fragment_home != null && fragment_home.isAdded()) {
            fragment_home.stopTimer();

            fragmentManager.beginTransaction().hide(fragment_home).commit();
        }

        if (fragment_directory != null && fragment_directory.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_directory).commit();
        }

        if (fragment_sponsor != null && fragment_sponsor.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_sponsor).commit();
        }


        if (fragment_industry.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_industry).commit();

        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_home_container, fragment_industry, "fragment_industry").addToBackStack("fragment_industry").commit();

        }
        ah_bottom_nav.setCurrentItem(2, false);


    }

    private void DisplayFragmentSponsor() {

        if (fragment_sponsor == null) {
            fragment_sponsor = Fragment_Sponsor.newInstance();
        }

        if (fragment_home != null && fragment_home.isAdded()) {
            fragment_home.stopTimer();

            fragmentManager.beginTransaction().hide(fragment_home).commit();
        }

        if (fragment_directory != null && fragment_directory.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_directory).commit();
        }

        if (fragment_industry != null && fragment_industry.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_industry).commit();
        }


        if (fragment_sponsor.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_sponsor).commit();

        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_home_container, fragment_sponsor, "fragment_sponsor").addToBackStack("fragment_sponsor").commit();

        }
        ah_bottom_nav.setCurrentItem(3, false);


    }

    public void setSearchData(int category_id,int location_id,String cat_name,String loc_name,String query)
    {
        if (fragment_home!=null&&fragment_home.isAdded())
        {
            fragment_home.setSearchData(category_id,location_id,cat_name,loc_name,query);
        }
    }


    public void RefreshActivity(String lang) {
        Paper.book().write("lang", lang);
        LanguageHelper.setNewLocale(this, lang);

        new Handler()
                .postDelayed(() -> {

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }, 1050);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }


    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);


        } else {
            if (fragment_home != null && fragment_home.isAdded() && fragment_home.isVisible()) {
                finish();

            } else {
                DisplayFragmentHome();
            }


        }
    }

}
