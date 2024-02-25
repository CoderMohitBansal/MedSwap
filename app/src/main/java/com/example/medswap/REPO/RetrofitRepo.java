package com.example.medswap.REPO;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitRepo {
    public static RetrofitRepo INSTANCE;
    public RetrofitAPI APIINTERFACE;
    final String API = "https://jsonplaceholder.typicode.com";
    public RetrofitRepo(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIINTERFACE = retrofit.create(RetrofitAPI.class);
    }
    public static RetrofitRepo getINSTANCE(){
        if(INSTANCE == null){
            INSTANCE = new RetrofitRepo();
        }
        return INSTANCE;
    }
}
