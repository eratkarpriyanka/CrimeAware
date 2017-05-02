package com.orionlabs.crimeaware.activities;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.kml.KmlLayer;
import com.google.maps.android.ui.IconGenerator;
import com.jhlabs.map.proj.ProjectionUtils;
import com.jhlabs.map.proj.StateProjection;
import com.orionlabs.crimeaware.R;
import com.orionlabs.crimeaware.app.CrimeAwareApp;
import com.orionlabs.crimeaware.models.CrimeIncident;
import com.orionlabs.crimeaware.models.CrimeIncident_Table;
import com.orionlabs.crimeaware.models.SearchedIncident;
import com.orionlabs.crimeaware.utils.maputils.IncidentItemRenderer;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = MainActivity.class.getSimpleName();
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Enable up icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if(mapFragment!=null) {
            mapFragment.getMapAsync(this);
        } else {
            Toast.makeText(this, "Error - Map Fragment was null!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.clear();
        setBorderOutline(googleMap);
        loadDataOnMap(googleMap);
        this.googleMap = googleMap;
    }

    /**
     *  Sort the incidents by frequency (descending order) and load each marker
     *  TODO can be optimized using ClusteredRenderer for marker as used for search keyword
     * @param googleMap
     */
    private void loadDataOnMap(final GoogleMap googleMap) {

        final Handler handler = new Handler();
        final LatLngBounds.Builder builder = new LatLngBounds.Builder();
        Runnable runnable = new Runnable() {

            int maxColor=255;
            long freq;
            int increment =75;
            @Override
            public void run() {

                // get the list of crimeincidents from higher to lower
                List<CrimeIncident> crimeList =  SQLite.select()
                        .from(CrimeIncident.class)
                        .orderBy(CrimeIncident_Table.districtCrimeFreq, false).queryList();

                if(crimeList!=null && crimeList.size()>0) {

                    //Log.d(TAG,"size of the list "+crimeList.size());
                    final long maxfreq=crimeList.get(0).getDistrictCrimeFreq();
                    final IconGenerator iconGenerator = new IconGenerator(MainActivity.this);
                    final Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_place_black_24dp, null);
                    /**
                     * TODO use cluster images instead to populate markers
                     */


                    for (int i = 0; i < crimeList.size();i++){

                        final CrimeIncident crimeIncident = crimeList.get(i);
                        final LatLng latlang = ProjectionUtils.getLatLng(CrimeAwareApp.sContext, StateProjection.ILLINOIS_STATE_PLANE,crimeIncident.getxCoordinate() , crimeIncident.getyCoordinate());
                        // add latlng into Search Incident

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                MarkerOptions markerOptions = new MarkerOptions();
                                freq = crimeIncident.getDistrictCrimeFreq();
                                long diff = (int)maxfreq-freq;
                                int rColor =  maxColor - ((int)diff*increment);
                                int color = Color.rgb(rColor,0,0);
                                drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                                iconGenerator.setBackground(drawable);
                                Marker marker = googleMap.addMarker(markerOptions.position(latlang)
                                        .icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon(""+freq))));
                                builder.include(marker.getPosition());

                            }
                        });

                    }
                }
            }
        };
        new Thread(runnable).start();
        loadCameraToPosition(googleMap,builder);
    }

    /**
     *  set camera to the position of markers
     * @param builder that includes position of markers
     */
    private void loadCameraToPosition(final GoogleMap googleMap,final LatLngBounds.Builder builder){

        /**
         * set camera to the marker position
         */
        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

                LatLngBounds bounds = builder.build();
                int padding = 50; // offset from edges of the map in pixel
                final CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds,padding);
                googleMap.moveCamera(cameraUpdate);
                googleMap.animateCamera(cameraUpdate);
            }
        });

    }

    private void setBorderOutline(GoogleMap googleMap) {

        try {
            KmlLayer layer = new KmlLayer(googleMap, R.raw.current, getApplicationContext());
            layer.addLayerToMap();
        } catch (XmlPullParserException e) {
            Log.e(TAG,"Exception parsing xml "+e);
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG,"Exception locating file "+e);
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here

                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                fetchSearchResult(query);
                searchView.clearFocus();
                fetchSearchResult(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                // fetchSearchResult(query);
                return true;
            }
        });
        // Expand the search view and request focus
        searchItem.expandActionView();
        searchView.requestFocus();
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // This is the up button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                // overridePendingTransition(R.animator.anim_left, R.animator.anim_right);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void fetchSearchResult(String query) {

        new SearchIncidentsTask().execute(query);
    }

    /**
     * Asysn Task to search for the incident keyword and update map
     */
    class SearchIncidentsTask extends AsyncTask<String,Void,Void>{

        List<SearchedIncident> searchedCrimeList ;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(googleMap == null) {
                return;
            }
            googleMap.clear();
            builder = new LatLngBounds.Builder();
            setBorderOutline(googleMap);

        }
        @Override
        protected Void doInBackground(String... params) {

            String query = params[0];
            // get the list of crimeincidents from higher to lower
            List<CrimeIncident> crimeList =  SQLite.select()
                    .from(CrimeIncident.class)
                    .where(CrimeIncident_Table.iucrDescription.like("%"+query+"%")).queryList();
            if(crimeList!=null) {

                searchedCrimeList = new ArrayList<>();
                for (int i = 0; i < crimeList.size(); i++){

                    CrimeIncident crimeIncident = crimeList.get(i);
                    final LatLng latlang = ProjectionUtils.getLatLng(CrimeAwareApp.sContext, StateProjection.ILLINOIS_STATE_PLANE,crimeIncident.getxCoordinate() , crimeIncident.getyCoordinate());
                    searchedCrimeList.add(new SearchedIncident(latlang));
                    Log.d(TAG," searched "+crimeIncident.getIucrDescription());
                    builder.include(latlang);
                }
                Log.d(TAG," searched list"+searchedCrimeList.size());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setUpClusterer(searchedCrimeList);
            loadCameraToPosition(googleMap,builder);
        }
    }

    private void setUpClusterer(List<SearchedIncident> searchedCrimeList) {

        // Declare a variable for the cluster manager.
        ClusterManager<SearchedIncident> clusterManager;
        // Initialize the manager
        clusterManager = new ClusterManager<SearchedIncident>(this, googleMap);

        clusterManager.addItems(searchedCrimeList);
        // Let the cluster manager know you've made changes
        clusterManager.cluster();
        clusterManager.setRenderer(new IncidentItemRenderer(CrimeAwareApp.sContext,googleMap,clusterManager));
    }

}