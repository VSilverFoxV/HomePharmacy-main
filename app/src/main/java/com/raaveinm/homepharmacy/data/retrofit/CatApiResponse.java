package com.raaveinm.homepharmacy.data.retrofit;

import com.squareup.moshi.Json;

public class CatApiResponse {
    @Json(name = "url")
    public String relativeUrl;

    public String getRelativeUrl() {
        return relativeUrl;
    }
}