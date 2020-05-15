package com.example.coronaradar.API;


import com.example.coronaradar.Model.Corona;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface API {

    @GET("summary")
    Observable<Corona> getCorona();

}
