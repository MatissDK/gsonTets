package com.softdesign.gsontets;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.softdesign.gsontets.Classes.Super;
import com.softdesign.gsontets.Network.DataManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private DataManager mDataManager;
    private ListView mListView;

    private static final String TAG = "TAG";
    private static final String ERROR = "ERROR CONNECTING TO SERVER";

    private static List<String> mListString = new ArrayList<>();

    private static List<com.softdesign.gsontets.Classes.List> mList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.listView_forecast);
        mDataManager = DataManager.getInstance();
        getData();
    }

    private void getData() {
        Call<Super> call = mDataManager.getForcasts();
        call.enqueue(new Callback<Super>() {
            @Override
            public void onResponse(Call<Super> call, Response<Super> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG,"yay");
                    getData(response);

                } else {
                    Log.d(TAG,"else");
                    //Toast.makeText(getApplicationContext(),ERROR,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Super> call, Throwable t) {
                //Toast.makeText(getApplicationContext(),ERROR,Toast.LENGTH_LONG).show();
                Log.d(TAG,t.toString());
            }
        });

    }
    
    private void getData(Response<Super> response){

        mList = response.body().getList();

        for (com.softdesign.gsontets.Classes.List a : mList) {
            mListString.add(a.getDtTxt());
        }


        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(getApplicationContext(),R.layout.list_item_forecast, R.id.list_item_forecast_textView, mListString);
        mListView.setAdapter(myAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView mText = (TextView) view;
                Toast.makeText(getApplicationContext(),mText.getText(),Toast.LENGTH_SHORT).show();
            }
        });

        /*for (Weather myWeather: response.body().getWeather()) {
            mTextView.setText(myWeather.getMain());
        }*/
    } 
}
