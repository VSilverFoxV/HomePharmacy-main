package com.raaveinm.homepharmacy.data.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CatApiService {
    @GET("/cat?json=true")
    Call<CatApiResponse> getRandomCat();
}