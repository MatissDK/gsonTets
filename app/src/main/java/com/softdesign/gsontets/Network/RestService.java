package com.softdesign.gsontets.Network;

import com.softdesign.gsontets.Classes.Super;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface RestService {


    @GET("forecast?&units=metric&APPID=9fcb0b27afe412bb04919b5672a7250c")
    Call<Super> getForcasts(
            @Query("q") String city
    );
}
