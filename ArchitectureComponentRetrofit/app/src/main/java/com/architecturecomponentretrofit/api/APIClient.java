package com.architecturecomponentretrofit.api;

import com.architecturecomponentretrofit.model.ModelInfo;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIClient {
    @GET("getappyandroidteam/")
    Call<ModelInfo> getEmpInfo();
}