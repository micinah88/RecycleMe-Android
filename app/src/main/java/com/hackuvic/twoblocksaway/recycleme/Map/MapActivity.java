package com.hackuvic.twoblocksaway.recycleme.Map;

import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hackuvic.twoblocksaway.recycleme.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener, LocationListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private ArrayList<LatLng> latLngs = new ArrayList<>();
    private MarkerOptions markerOptions = new MarkerOptions();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        addLatLng();
    }

    public void addLatLng() {
        latLngs.add(new LatLng(48.4372825, -123.4016014));
        latLngs.add(new LatLng(48.446484, -123.517329));
        latLngs.add(new LatLng(48.459655, -123.499368));
        latLngs.add(new LatLng(48.484675, -123.387714));
        latLngs.add(new LatLng(48.468125, -123.362554));
        latLngs.add(new LatLng(48.461624, -123.334878));
        latLngs.add(new LatLng(48.860488, -123.510939));

        latLngs.add(new LatLng(48.854339, -123.517290));
        latLngs.add(new LatLng(48.487706, -123.314466));
        latLngs.add(new LatLng(48.653994, -123.411981));
        latLngs.add(new LatLng(48.395128, -123.701458));
        latLngs.add(new LatLng(48.384748, -123.692325));
        latLngs.add(new LatLng(48.434009, -123.365509));
        latLngs.add(new LatLng(48.537991, -123.462272));

        latLngs.add(new LatLng(48.439590, -123.358932));
        latLngs.add(new LatLng(48.453951, -123.375102));
        latLngs.add(new LatLng(48.448636, -123.369619));
        latLngs.add(new LatLng(48.480889, -123.388370));
        latLngs.add(new LatLng(48.456512, -123.442265));


    }

    @Override
    protected void onStart() {
        super.onStart();
        // 2
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 3
        if( mGoogleApiClient != null && mGoogleApiClient.isConnected() ) {
            mGoogleApiClient.disconnect();
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng myPlace = new LatLng(48.4634067, -123.3116935);  // this is New York
        //mMap.addMarker(new MarkerOptions().position(myPlace).title("My Favorite City"));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPlace, 12));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMarkerClickListener(this);
    }

    private void setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {android.Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        mMap.setMyLocationEnabled(true);

        LocationAvailability locationAvailability =
                LocationServices.FusedLocationApi.getLocationAvailability(mGoogleApiClient);
        if (null != locationAvailability && locationAvailability.isLocationAvailable()) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                LatLng currentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation
                        .getLongitude());
                //add pin at user's location
                placeMarkerOnMap(currentLocation);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12));
            }
        }
    }

    protected void placeMarkerOnMap(LatLng location) {
//        MarkerOptions markerOptions = new MarkerOptions().position(location);
//
//        String titleStr = getAddress(location);  // add these two lines
//        markerOptions.title(titleStr);
//
//        mMap.addMarker(markerOptions);

        for (LatLng points : latLngs) {
            markerOptions.position(points);
            markerOptions.title(getAddress(points));
            mMap.addMarker(markerOptions);
        }
    }

    private String getAddress( LatLng latLng ) {
        // 1
        Geocoder geocoder = new Geocoder( this, Locale.getDefault());
        List<Address> addresses;
        String address = "";
        try {
            // 2
            addresses = geocoder.getFromLocation( latLng.latitude, latLng.longitude, 1 );
            String street = addresses.get(0).getAddressLine(0); //0 to obtain first possible address
            address += street;
            //address = addresses.get(0);
            //for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
            //        addressText += (i == 0)?address.getAddressLine(i):("\n" + address.getAddressLine(i));
            //    }

            // 3
//            if (null != addresses && !addresses.isEmpty()) {
//                address = addresses.get(0);
//                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
//                    addressText += (i == 0)?address.getAddressLine(i):("\n" + address.getAddressLine(i));
//                }
//            }
        } catch (IOException e ) {
        }
        return address;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        setUpMap();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
