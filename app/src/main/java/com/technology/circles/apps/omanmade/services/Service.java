package com.technology.circles.apps.omanmade.services;


import com.technology.circles.apps.omanmade.models.AppDataModel;
import com.technology.circles.apps.omanmade.models.BusinessDataModel;
import com.technology.circles.apps.omanmade.models.DirectoryDataModel;
import com.technology.circles.apps.omanmade.models.FaqsModel;
import com.technology.circles.apps.omanmade.models.FeatureListingDataModel;
import com.technology.circles.apps.omanmade.models.FeaturedCategoryDataModel;
import com.technology.circles.apps.omanmade.models.IndustrialAreaDataModel;
import com.technology.circles.apps.omanmade.models.MediaModel;
import com.technology.circles.apps.omanmade.models.SpinnerModel;
import com.technology.circles.apps.omanmade.models.PackageDataModel;
import com.technology.circles.apps.omanmade.models.PlaceGeocodeData;
import com.technology.circles.apps.omanmade.models.PlaceMapDetailsData;
import com.technology.circles.apps.omanmade.models.ServiceDataModel;
import com.technology.circles.apps.omanmade.models.SliderModel;
import com.technology.circles.apps.omanmade.models.SponsorsModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
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
    Call<FeatureListingDataModel> getFeatures(@Header("lang") String lang);

    @GET("api/packages")
    Call<PackageDataModel> getPackages(@Header("lang") String lang);


    @FormUrlEncoded
    @POST("api/contact")
    Call<ResponseBody> getSendContact(@Field("name") String name,
                                      @Field("email") String email,
                                      @Field("subject") String subject,
                                      @Field("body") String body
    );

    @GET("api/services")
    Call<ServiceDataModel> getService(@Header("lang") String lang);

    @GET("api/industrial-areas")
    Call<IndustrialAreaDataModel> getIndustrialArea(@Header("lang") String lang);

    @GET("api/featured-cats")
    Call<FeaturedCategoryDataModel> getFeaturedCategory(@Header("lang") String lang);

    @GET("wp-json/wp/v2/locations")
    Call<List<SpinnerModel>> getLocation(@Query("parent") int lang);

    @GET("wp-json/wp/v2/listing_categories")
    Call<List<SpinnerModel>> getListingCategory(@Query("page") int page);


    @GET("api/dir-ads")
    Call<DirectoryDataModel> getDirectory(@Header("lang") String lang);

    @GET("wp-json/wp/v2/business")
    Call<List<BusinessDataModel>> getBusiness(@Query("lang") String lang, @Query("page") int page);

    @GET("wp-json/wp/v2/media/{media_id}")
    Call<MediaModel> getMedia(@Path("media_id") int media_id);

    @GET("wp-json/wp/v2/business")
    Call<List<BusinessDataModel>> searchWithQuery(@Query("lang") String lang,
                                                  @Query("page") int page,
                                                  @Query("search") String search
    );


    @GET("wp-json/wp/v2/business")
    Call<List<BusinessDataModel>> search(@Query("lang") String lang,
                                         @Query("page") int page,
                                         @Query("listing_categories") String listing_categories,
                                         @Query("locations") String locations,
                                         @Query("search") String search

    );

    @GET("wp-json/wp/v2/business/{web_id}")
    Call<BusinessDataModel> getBusinessByWebId(@Path("web_id") String web_id);

    @GET("wp-json/wp/v2/business")
    Call<List<BusinessDataModel>> getBusinessByWebId2(@Query("lang") String lang,
                                                      @Query("page") int page,
                                                      @Query("listing_categories") String listing_categories

    );

}

