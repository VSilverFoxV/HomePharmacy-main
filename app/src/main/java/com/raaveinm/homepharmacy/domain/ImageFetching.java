package com.raaveinm.homepharmacy.domain;

import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.raaveinm.homepharmacy.data.retrofit.CatApiResponse;
import com.raaveinm.homepharmacy.data.retrofit.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageFetching {

    private String TAG = "ImageFetching";

    public void fetchAndLoadImage(ImageView loginImageView) {
        if (loginImageView == null) {
            Log.e(TAG, "loginImageView is null before fetching image.");
            return;
        }

        RetrofitInstance.api.getRandomCat().enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<CatApiResponse> call,
                    @NonNull Response<CatApiResponse> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    String imageUrlFromApi = response.body().getRelativeUrl();
                    if (imageUrlFromApi != null && !imageUrlFromApi.isEmpty()) {
                        ImageDownloader.loadImage(loginImageView, imageUrlFromApi);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CatApiResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "API call failed: ", t);
            }
        });
    }
}