package com.technology.circles.apps.omanmade.language;

import android.app.Application;
import android.content.Context;

import com.technology.circles.apps.omanmade.share.TypefaceUtil;


public class App extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LanguageHelper.updateResources(base,"en"));
    }

  public void onCreate() {
        super.onCreate();

        TypefaceUtil.setDefaultFont(this, "DEFAULT", "fonts/ar_font.ttf");
        TypefaceUtil.setDefaultFont(this, "MONOSPACE", "fonts/ar_font.ttf");
        TypefaceUtil.setDefaultFont(this, "SERIF", "fonts/ar_font.ttf");
        TypefaceUtil.setDefaultFont(this, "SANS_SERIF", "fonts/ar_font.ttf");



    }
}
