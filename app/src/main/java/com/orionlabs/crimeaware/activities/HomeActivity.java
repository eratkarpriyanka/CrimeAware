package com.orionlabs.crimeaware.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.orionlabs.crimeaware.R;
import com.orionlabs.crimeaware.interfaces.ApiEndpointInterface;
import com.orionlabs.crimeaware.models.CrimeIncident;
import com.orionlabs.crimeaware.network.ServiceGenerator;
import com.orionlabs.crimeaware.utils.DateTimeUtils;
import com.orionlabs.crimeaware.utils.TempStaticVars;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private Button btnGo;

    private ProgressBar pbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnGo = (Button)findViewById(R.id.btn_go);
        btnGo.setOnClickListener(this);

        pbLoading = (ProgressBar) findViewById(R.id.pbLoading);
    }

    @Override
    public void onClick(View view) {


        showProgress();

        DateTimeUtils instance = DateTimeUtils.getInstance();
        String endDate = instance.getCurrentdate();
        String startDate = instance.getStartDate();
        ApiEndpointInterface apiService = ServiceGenerator.createService();
        Call<List<CrimeIncident>> call = apiService.requestCrimesList(startDate,endDate, TempStaticVars.sOffset,TempStaticVars.sMax);
        call.enqueue(new Callback<List<CrimeIncident>>() {
            @Override
            public void onResponse(Call<List<CrimeIncident>> call, Response<List<CrimeIncident>> response) {

                hideProgress();
                List<CrimeIncident> list = response.body();
                Log.d(TAG,"response is "+list.size());
                // add frequencies and  sort the crimelist received based on frequencies
                processCrimeList(list);
                launchNextScr();
            }

            @Override
            public void onFailure(Call<List<CrimeIncident>> call, Throwable t) {
                hideProgress();
                Log.d(TAG,"failure "+t);
            }
        });
    }

    /**
     *  calculate and add frequencies of crime to {@link CrimeIncident}
     *
     */
    private void processCrimeList(final List<CrimeIncident> crimeList) {

        Runnable mRunnableOnSeparateThread = new Runnable() {
            @Override
            public void run () {

                HashMap<Long,Integer> hmapCrimeFreq = new HashMap<>();
                for(int i=0; i <crimeList.size();i++){

                    CrimeIncident crimeIncident = crimeList.get(i);
                    crimeIncident.save();
                    long cpdDistrict = crimeIncident.getCpdDistrict();

                    // calculate freq of crime
                    if(hmapCrimeFreq.containsKey(cpdDistrict)) {
                        hmapCrimeFreq.put(cpdDistrict, hmapCrimeFreq.get(cpdDistrict)+1);
                    }else
                        hmapCrimeFreq.put(cpdDistrict,1);

                }
                Log.d(TAG,"HashMap "+hmapCrimeFreq);
                /**
                 *  code to add districtFreq and latlong for crimeIncident and save in DB
                 */
                for(int i=0;i<crimeList.size();i++) {

                    CrimeIncident crimeIncident = crimeList.get(i);
                    // get district corressponding from hashmap
                    long freq = hmapCrimeFreq.get(crimeIncident.getCpdDistrict());
                    crimeIncident.setDistrictCrimeFreq(freq);
                    crimeIncident.update();
                }

            }
        };

        new Thread(mRunnableOnSeparateThread).start();
    }

    /**
     * launch screen to show data on maps
     */
    private void launchNextScr() {

        Intent intent = new Intent(HomeActivity.this,MainActivity.class);
        startActivity(intent);
    }

    /**
     * show progress view
     */
    private void showProgress(){

        if(pbLoading!=null && !pbLoading.isShown()){
            pbLoading.setVisibility(View.VISIBLE);
        }
    }

    /**
     * hide progress view
     */
    private void hideProgress(){

        if(pbLoading!=null && pbLoading.isShown()){
            pbLoading.setVisibility(View.GONE);
        }
    }

}
