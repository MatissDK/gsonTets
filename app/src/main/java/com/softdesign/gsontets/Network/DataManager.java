package com.softdesign.gsontets.Network;

import com.softdesign.gsontets.Classes.Super;

import retrofit2.Call;



public class DataManager {

    private static DataManager INSTANCE = null;
    private RestService mRestService;

    public DataManager() {
        this.mRestService = ServiceGenerator.createService(RestService.class);
    }

    public static DataManager getInstance(){
        if(INSTANCE==null){
            INSTANCE = new DataManager();
        }
        return INSTANCE;
    }

    public Call<Super> getForcasts (){
        return mRestService.getForcasts();
    }
}
