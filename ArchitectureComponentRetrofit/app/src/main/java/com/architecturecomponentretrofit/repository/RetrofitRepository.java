package com.architecturecomponentretrofit.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.architecturecomponentretrofit.api.APIClient;
import com.architecturecomponentretrofit.model.ModelInfo;

import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitRepository {

    public static final String BASE_URL = "http://adhishlal.com/api/";

    private static MutableLiveData<ModelInfo> data = new MutableLiveData<>();

    private static Retrofit retrofit=null;

    public static Retrofit getRetrofitClient(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .callbackExecutor(Executors.newSingleThreadExecutor())//excute call back in background thread
                    .build();
        }
        return retrofit;
    }

    public static LiveData<ModelInfo> getIntData()
    {
        return data;
    }

    public static void getEmpInfo()
    {
        Log.e("getEmpInfo ","PROCESSING IN THREAD BEFORE RETROFIT CALL"+Thread.currentThread().getName());

        Call<ModelInfo> call = getRetrofitClient().create(APIClient.class).getEmpInfo();

        //rest service call runs on background thread and Callback also runs on background thread

        call.enqueue(new Callback<ModelInfo>() {
            @Override
            public void onResponse(Call<ModelInfo> call, Response<ModelInfo> response) {
                ModelInfo modelInfo = response.body();
                //use postValue since it is running on background thread.
                data.postValue(modelInfo);
                Log.e("OnResponse","PROCESSING IN THREAD IN RETROFIT RESPONSE HANDLER "+Thread.currentThread().getName());

            }

            @Override
            public void onFailure(Call<ModelInfo> call, Throwable t) {
                Log.e("OnFailure","Error  retrofit ");
            }
        });

    }


}
