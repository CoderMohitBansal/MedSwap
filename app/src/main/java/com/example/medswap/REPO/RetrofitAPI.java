package com.example.medswap.REPO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitAPI {
    @GET("/users")
    Call<List<String>> getUsers();
}
