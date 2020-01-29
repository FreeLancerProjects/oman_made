package com.technology.circles.apps.omanmade.services;


import com.technology.circles.apps.omanmade.models.AppDataModel;
import com.technology.circles.apps.omanmade.models.FaqsModel;
import com.technology.circles.apps.omanmade.models.FeatureListingDataModel;
import com.technology.circles.apps.omanmade.models.PackageDataModel;
import com.technology.circles.apps.omanmade.models.PlaceGeocodeData;
import com.technology.circles.apps.omanmade.models.PlaceMapDetailsData;
import com.technology.circles.apps.omanmade.models.SliderModel;
import com.technology.circles.apps.omanmade.models.SponsorsModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface Service {

    @GET("place/findplacefromtext/json")
    Call<PlaceMapDetailsData> searchOnMap(@Query(value = "inputtype") String inputtype,
                                          @Query(value = "input") String input,
                                          @Query(value = "fields") String fields,
                                          @Query(value = "language") String language,
                                          @Query(value = "key") String key
    );

    @GET("geocode/json")
    Call<PlaceGeocodeData> getGeoData(@Query(value = "latlng") String latlng,
                                      @Query(value = "language") String language,
                                      @Query(value = "key") String key);
    @GET("api/settings")
    Call<AppDataModel> getSetting(@Header("lang") String lang);

    @GET("api/slides")
    Call<SliderModel> geSlider();

    @GET("api/faqs")
    Call<FaqsModel> getFaqs(@Header("lang") String lang);

    @GET("api/sponsors")
    Call<SponsorsModel> getSponsor();

    @GET("api/featured-lists")
    Call<FeatureListingDataModel> getFeatures(@Header("lang")String lang);

    @GET("api/packages")
    Call<PackageDataModel> getPackages(@Header("lang")String lang);
}


