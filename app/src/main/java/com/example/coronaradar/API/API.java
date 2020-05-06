package com.example.coronaradar.API;


import com.example.coronaradar.Model.Corona;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface API {

    @Headers({"x-rapidapi-host:covid-19-data.p.rapidapi.com","x-rapidapi-key:0904b15f55msh1c1552b93e3040bp12b225jsn9b2c9499dd6b"})
    @GET("all")
    Observable<List<Corona>> getCorona();

}
