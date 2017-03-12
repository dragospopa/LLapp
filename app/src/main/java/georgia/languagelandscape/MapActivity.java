package georgia.languagelandscape;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

public class MapActivity extends BaseActivity{

    private static boolean active = false;
    private static double longitude = 0.0;
    private static double latitude = 0.0;
    public final static String GEO_LONGITUDE = "geo_longitude";
    public final static String GEO_LATITUDE = "geo_latitude";

    private MapFragment mapFragment= null;
    private FragmentManager fm = null;
    private LocationManager locationManager = null;
    private Location currentLocation = null;
    private LocationListener locationListener = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        super.onDrawerCreated();
        active = true;

        mapFragment = new MapFragment();

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MainActivity.REQUEST_LOCATION);
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (currentLocation != null) {
            longitude = currentLocation.getLongitude();
            latitude = currentLocation.getLatitude();
        }
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (isBetterLocation(location)){
                    longitude = currentLocation.getLongitude();
                    latitude = currentLocation.getLatitude();

                    Bundle bundle = new Bundle();
                    bundle.putDouble(GEO_LONGITUDE, longitude);
                    bundle.putDouble(GEO_LATITUDE, latitude);
                    mapFragment.setArguments(bundle);

                    try {
                        locationManager.removeUpdates(locationListener);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        Bundle bundle = new Bundle();
        bundle.putDouble(GEO_LONGITUDE, longitude);
        bundle.putDouble(GEO_LATITUDE, latitude);
        mapFragment.setArguments(bundle);

        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content_replace, mapFragment);
        ft.commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.homeFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this, RecordingActivity.class);
                intent.putExtra(GEO_LONGITUDE, longitude);
                intent.putExtra(GEO_LATITUDE, latitude);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        active = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        active = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        active = true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        active = true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MainActivity.REQUEST_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "location access granted", Toast.LENGTH_SHORT).show();
                }
                // TODO: do something if location access denied.
        }
    }

    public static double getLongitude() {
        return longitude;
    }

    public static double getLatitude() {
        return latitude;
    }

    public static boolean isActive() {
        return active;
    }

    private boolean isBetterLocation(Location location) {
        if (currentLocation == null) {
            return true;
        }

        // check whether the new location is meets accuracy criteria
        float accuracy = location.getAccuracy();
        return accuracy < 200;
    }
}
