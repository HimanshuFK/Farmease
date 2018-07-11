package com.farmease.app;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.farmease.app.activity.MyAccountActivity;
import com.farmease.app.adapter.HomeAdapter;
import com.farmease.app.adapter.SlideImageAdapter;
import com.farmease.app.fragment.FragmentAccount;
import com.farmease.app.fragment.FragmentCategory;
import com.farmease.app.fragment.FragmentHome;
import com.farmease.app.helper.BottomNavigationViewHelper;
import com.farmease.app.location.LocationSearchActivity;
import com.farmease.app.model.Home;
import com.farmease.app.utility.Constants;
import com.farmease.app.utility.Utility;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    @BindView(R.id.txt_searchlocation)
    TextView tvSelectLocation;
    @BindView(R.id.img_search)
    ImageView img_search;
    @BindView(R.id.txt_category)
    TextView txt_category;
    private Unbinder unbinder;
    private final static int REQUEST_CHECK_SETTINGS_GPS=0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS=0x2;
    private Location mCurrentLocation;
    GoogleApiClient mGoogleApiClient;

    private Fragment fragment;

    //bottom navigation drawer
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if (!(fragment instanceof FragmentHome)) {
                        fragment = new FragmentHome();
                        txt_category.setVisibility(View.GONE);
                        tvSelectLocation.setVisibility(View.VISIBLE);
                        start_fragment(fragment);
                    }
                    return true;
                case R.id.navigation_dashboard:
                    if (!(fragment instanceof FragmentCategory)) {
                        fragment = new FragmentCategory();
                        txt_category.setText("Category");
                        txt_category.setVisibility(View.VISIBLE);
                        tvSelectLocation.setVisibility(View.GONE);
                        start_fragment(fragment);
                    }
                    return true;
                case R.id.navigation_czrt:
                    Toast.makeText(HomeActivity.this, "Coming Soon..", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.navigation_notifications:
                    if (!(fragment instanceof FragmentAccount)) {
                        fragment = new FragmentAccount();
                        txt_category.setText("My Profile");
                        txt_category.setVisibility(View.VISIBLE);
                        tvSelectLocation.setVisibility(View.GONE);
                        start_fragment(fragment);
                    }
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        setContentView(R.layout.activity_home);

        unbinder = ButterKnife.bind(this);
        tvSelectLocation.setOnClickListener(this);
        if (!(fragment instanceof FragmentHome)) {
            fragment = new FragmentHome();
            start_fragment(fragment);
        }
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(navigation);
    }

    private void start_fragment(Fragment frag) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.activity_content, frag);
        fragmentTransaction.commit();
    }


    private void getMyLocation(){
        if(mGoogleApiClient!=null) {
            if (mGoogleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(HomeActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    mCurrentLocation =                     LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setInterval(3000);
                    locationRequest.setFastestInterval(3000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);
                    builder.setAlwaysShow(true);
                    LocationServices.FusedLocationApi
                            .requestLocationUpdates(mGoogleApiClient, locationRequest, this);
                    PendingResult<LocationSettingsResult> result =
                            LocationServices.SettingsApi
                                    .checkLocationSettings(mGoogleApiClient, builder.build());
                    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

                        @Override
                        public void onResult(LocationSettingsResult result) {
                            final Status status = result.getStatus();
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:
                                    // All location settings are satisfied.
                                    // You can initialize location requests here.
                                    int permissionLocation = ContextCompat
                                            .checkSelfPermission(HomeActivity.this,
                                                    Manifest.permission.ACCESS_FINE_LOCATION);
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                        mCurrentLocation = LocationServices.FusedLocationApi
                                                .getLastLocation(mGoogleApiClient);
                                    }
                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    // Location settings are not satisfied.
                                    // But could be fixed by showing the user a dialog.
                                    try {
                                        // Show the dialog by calling startResolutionForResult(),
                                        // and check the result in onActivityResult().
                                        // Ask to turn on GPS automatically
                                        status.startResolutionForResult(HomeActivity.this,
                                                REQUEST_CHECK_SETTINGS_GPS);
                                    } catch (IntentSender.SendIntentException e) {
                                        // Ignore the error.
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    // Location settings are not satisfied.
                                    // However, we have no way
                                    // to fix the
                                    // settings so we won't show the dialog.
                                    // finish();
                                    break;
                            }
                        }
                    });
                }
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS_GPS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        getMyLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        finish();
                        break;
                }
                break;
        }
    }

    private void checkPermissions(){
        int permissionLocation = ContextCompat.checkSelfPermission(HomeActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        }else{
            getMyLocation();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        int permissionLocation = ContextCompat.checkSelfPermission(HomeActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            getMyLocation();
        }
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGoogleApiClient.disconnect();
    }

    public String getAddress(double latitude, double longitude) throws IOException {
        String city = null;
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());

            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            if (city==null){
                city=state;
            }
        } catch (IOException io) {
            io.printStackTrace();
        }

        return city;
    }

    private void updateLocationUI() {

        if (mCurrentLocation != null) {
            try {
                tvSelectLocation.setText(
                        getAddress(mCurrentLocation.getLatitude() ,mCurrentLocation.getLongitude()));
                Utility.savePref(HomeActivity.this, Constants.lat, String.valueOf(mCurrentLocation.getLatitude()));
                Utility.savePref(HomeActivity.this,Constants.lng, String.valueOf(mCurrentLocation.getLongitude()));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_searchlocation:
                startActivity(new Intent(HomeActivity.this, LocationSearchActivity.class));

                break;

        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
       updateLocationUI();
       // tvSelectLocation.setText(""+location.getLatitude()+","+location.getLongitude());
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        checkPermissions();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
