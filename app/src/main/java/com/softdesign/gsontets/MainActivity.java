package com.softdesign.gsontets;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.softdesign.gsontets.Classes.Super;
import com.softdesign.gsontets.DB.DBconnection;
import com.softdesign.gsontets.Network.DataManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private DataManager mDataManager;
    private ListView mListView;

    private List<com.softdesign.gsontets.Classes.List> mListOfList = new ArrayList<>();
    private List<String> mListString = new ArrayList<>();

    private String mUnitType;
    private String mCity;


    private DBconnection myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDB = new DBconnection(getApplicationContext());

        boolean pka = myDB.insertData("divi name", "divi surname", 11);
        if (pka == true) {
            Toast.makeText(getApplicationContext(), "Yeah", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "NOOOO", Toast.LENGTH_SHORT).show();
        }

       /* mListView = (ListView) findViewById(R.id.listView_forecast);
        mDataManager = DataManager.getInstance();
        mListView.setOnItemClickListener(this);
        getData();
        Log.d(Constants.TAG, "create");*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.settings) {
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            return true;
        } else if (id == R.id.refresh) {
            getData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void getSharedPreferences() {

        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());

        mUnitType = prefs.getString(
                getString(R.string.pref_units_key),
                getString(R.string.pref_units_metric));

        mCity = prefs.getString(
                getString(R.string.pref_location_key),
                getString(R.string.pref_location_default));
    }


    private void getData() {
        getSharedPreferences();
        Call<Super> call = mDataManager.getForcasts(mCity);
        call.enqueue(new Callback<Super>() {
            @Override
            public void onResponse(Call<Super> call, Response<Super> response) {
                if (response.isSuccessful()) {
                    mListOfList = response.body().getList();
                    displayDateData();

                } else {
                    Toast.makeText(getApplicationContext(), Constants.ERROR, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Super> call, Throwable t) {
                Toast.makeText(getApplicationContext(), Constants.ERROR, Toast.LENGTH_LONG).show();
            }
        });

    }


    /**
     * display date (represented in day format)
     */

    //TODO need to add today as one of the options
    private void displayDateData() {

        mListString.clear();
        for (com.softdesign.gsontets.Classes.List listData : mListOfList) {
            if (listData.getDtTxt().contains(getString(R.string.midnight))) {
                mListString.add(listData.getDtTxt().substring(0, 10));
            }
        }

        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.list_item_forecast,
                R.id.list_item_forecast_textView, mListString);
        mListView.setAdapter(myAdapter);

    }


    /**
     * Prepare the weather high/lows for presentation.
     */
    private String formatHighLows(double high, double low, String unitType) {

        if (unitType.equals(getString(R.string.pref_units_imperial))) {
            high = (high * 1.8) + 32;
            low = (low * 1.8) + 32;
        } else if (!unitType.equals(getString(R.string.pref_units_metric))) {
            Log.d(Constants.TAG, "Unit type not found: " + unitType);
        }

        // For presentation, assume the user doesn't care about tenths of a degree.
        long roundedHigh = Math.round(high);
        long roundedLow = Math.round(low);

        String highLowStr = roundedHigh + "/" + roundedLow;
        return highLowStr;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView mText = (TextView) view;
        for (com.softdesign.gsontets.Classes.List obj : mListOfList) {
            if (obj.getDtTxt().contains(mText.getText() + getString(R.string.time))) {
                double temp_min = obj.getMain().getTempMin();
                double temp_max = obj.getMain().getTempMax();
                String toData = formatHighLows(temp_max, temp_min, mUnitType);

                Intent intent = new Intent(getApplicationContext(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, toData);
                startActivity(intent);

            }
        }
    }
}
