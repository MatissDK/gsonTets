package com.softdesign.gsontets.Network;

import com.softdesign.gsontets.Classes.Super;

import retrofit2.Call;
import retrofit2.http.GET;



public interface RestService {



    //http://api.openweathermap.org/data/2.5/weather?q=London,uk&units=metric&APPID=9fcb0b27afe412bb04919b5672a7250c

    //http://api.openweathermap.org/data/2.5/forecast?q=London,uk&units=metric&APPID=9fcb0b27afe412bb04919b5672a7250c

    @GET("forecast?q=London,uk&units=metric&APPID=9fcb0b27afe412bb04919b5672a7250c")
    Call<Super> getForcasts();
}
