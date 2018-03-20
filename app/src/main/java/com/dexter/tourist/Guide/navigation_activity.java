package com.dexter.tourist.Guide;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.dexter.tourist.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class navigation_activity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mGoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (googleServicesAvailabible()) {
            Toast.makeText(this, "Perfect!", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.navigation_activity);
            initMap();
        } else {
            Toast.makeText(this, "Layout Issue", Toast.LENGTH_SHORT).show();
        }
    }

    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    public boolean googleServicesAvailabible() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();

        } else {
            Toast.makeText(this, "Cannot Connect to Play Services", Toast.LENGTH_SHORT).show();

        }
        return false;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        //goToLocation(39.008224,-76.8984527);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }


        }

        mGoogleMap.setMyLocationEnabled(true);


        MarkerOptions options = new MarkerOptions()
                .title("Marker1")
                .position(new LatLng(26.9239, 75.8267));
        // .snippet("Helo");

        mGoogleMap.addMarker(options);

        MarkerOptions options2 = new MarkerOptions()
                .title("Marker2")
                .position(new LatLng(26.9241, 75.8267));
        //.snippet("Hi");

        mGoogleMap.addMarker(options2);

        MarkerOptions options3 = new MarkerOptions()
                .title("Marker3")
                .position(new LatLng(26.9243, 75.8267));

        mGoogleMap.addMarker(options3);
        MarkerOptions options4 = new MarkerOptions()
                .title("Marker4")
                .position(new LatLng(26.9245, 75.8267));

        mGoogleMap.addMarker(options4);
        MarkerOptions options5 = new MarkerOptions()
                .title("Marker4")
                .position(new LatLng(26.9245, 75.8267));

        mGoogleMap.addMarker(options5);

        mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View v = getLayoutInflater().inflate(R.layout.info_window, null);
                ImageView i = findViewById(R.id.image1);

                return v;
            }

            private void goToLocationZoom(double lat, double lng) {

                LatLng ll = new LatLng(lat, lng);
                CameraUpdate update = CameraUpdateFactory.newLatLng(ll);
                mGoogleMap.moveCamera(update);


            }


        });
    }
}
