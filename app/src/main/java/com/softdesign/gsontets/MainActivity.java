package com.softdesign.gsontets;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.softdesign.gsontets.Network.DataManager;
import com.softdesign.gsontets.Network.structure.MainWeather;
import com.softdesign.gsontets.Network.structure.Weather;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private DataManager mDataManager;
    private TextView mTextView;

    private static final String TAG = "TAG";
    private static final String ERROR = "ERROR CONNECTING TO SERVER";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.textView);

        mDataManager = DataManager.getInstance();
        getData();
    }

    private void getData() {
        Call<MainWeather> call = mDataManager.getForcasts();
        call.enqueue(new Callback<MainWeather>() {
            @Override
            public void onResponse(Call<MainWeather> call, Response<MainWeather> response) {
                if (response.isSuccessful()) {
                    getData(response);

                } else {
                    Toast.makeText(getApplicationContext(),ERROR,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MainWeather> call, Throwable t) {
                Toast.makeText(getApplicationContext(),ERROR,Toast.LENGTH_LONG).show();
            }
        });

    }
    
    private void getData(Response<MainWeather> response){
        for (Weather myWeather: response.body().getWeather()) {
            mTextView.setText(myWeather.getMain());
        }
    } 
}
