package com.july.streetfighter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kakao.auth.Session;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private static final int PERMISSION_REQUEST_LOCATION = 1001;

    private FusedLocationProviderClient fusedLocationClient;
    private MapView mapView;

    private ImageButton btnProfile;
    private String id;
    private double lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        id = getIntent().getStringExtra(getString(R.string.user_id));
        getSupportActionBar().hide();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        /** KakaoMap **/
        mapView = new MapView(this);
        ViewGroup mapViewContainer = findViewById(R.id.map_view);
        mapView.setShowCurrentLocationMarker(true);
        mapViewContainer.addView(mapView);

        if(LocationPermissionGranted()){
            getLastKnownLocation();
        }else{
            requestLocationPermission();
        }

        initButton();

    }

    private void initButton() {
        btnProfile = findViewById(R.id.btn_profile);
        btnProfile.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_profile:
                if (id.equals(getString(R.string.non_member))){
                    MySharedPreferences.clearUserInfo(getApplicationContext());
                    redirectLoginActivity();
                }else{
                    redirectProfileActivity();
                }
                break;
            default:
                break;
        }
    }

    private void requestLocationPermission(){
        if(!LocationPermissionGranted()){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION);
        }
    }

    private boolean LocationPermissionGranted(){
        /** Permissinon has been granted **/
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        /** Permissinon has been denied **/
        else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_LOCATION: {
                /** on permission granted **/
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLastKnownLocation();
                } else {
                    /** on permission denied **/
                }
                return;
            }
            default:
                return;
        }
    }

    private void getLastKnownLocation(){
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                /** Got last known location. In some rare situations this can be null **/
                if (location != null) {
                    lat = location.getLatitude();
                    lon = location.getLongitude();
                    Log.d(TAG, "LAST KNOWN LOCATION : " + lat + ", " + lon);
                    moveToCurrentLocation();
                }
            }
        });
    }

    private void moveToCurrentLocation(){
        Log.d(TAG, "moveToCurrentLocation: ");
        MapPoint mp = MapPoint.mapPointWithGeoCoord(lat, lon);

        /** Move center of map to current location **/
        mapView.setMapCenterPoint(mp, true);

        /** Place a marker that points current location **/
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("현재 위치");
        //marker.setTag(0);
        marker.setMapPoint(mp);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
        //marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
        mapView.addPOIItem(marker);
    }

    private void redirectProfileActivity() {
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        intent.putExtra(getString(R.string.user_id), id);
        startActivity(intent);
    }

    private void redirectLoginActivity() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void requestLogout() {
        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                Session.getCurrentSession().close();
                MySharedPreferences.clearUserInfo(getApplicationContext());
                redirectLoginActivity();
            }
        });
    }

}
