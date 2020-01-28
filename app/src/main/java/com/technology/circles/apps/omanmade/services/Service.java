package com.technology.circles.apps.omanmade.services;


import com.technology.circles.apps.omanmade.models.App_Data_Model;
import com.technology.circles.apps.omanmade.models.PlaceGeocodeData;
import com.technology.circles.apps.omanmade.models.PlaceMapDetailsData;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
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
    Call<App_Data_Model> getsetting(@Header("lang") String lang);


}


