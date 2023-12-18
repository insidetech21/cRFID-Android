package com.example.crfid.data.retrofit;

import com.example.crfid.model.materialTagPairModel.MaterialTagPair_Item;
import com.example.crfid.model.materialTagPairModel.MaterialTagPair_Response;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public
interface ApiService {
    @GET("RF_MATTAGSet")
    Call<MaterialTagPair_Response> getMaterialTagPair(
            @Query("$filter") String filter,
            @Header("Accept") String acceptHeader, // Add the Accept header
            @Header("Content-Type") String contentTypeHeader ,// Add the Content-Type header
            @Header("x-csrf-token") String fetch
    );

    @POST("RF_MATTAGSet")
    Call<MaterialTagPair_Item> postMatTagPair(
            @Body MaterialTagPair_Item item,
            @Header("Accept") String acceptHeader, // Add the Accept header
            @Header("Content-Type") String contentTypeHeader,
            @Header("x-csrf-token") String token,
            @Header("Cookie") String finalcookie
    );


}
