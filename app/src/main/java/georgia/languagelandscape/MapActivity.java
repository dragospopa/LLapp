package georgia.languagelandscape;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import georgia.languagelandscape.fragments.AboutFragment;
import georgia.languagelandscape.fragments.HelpFragment;
import georgia.languagelandscape.fragments.MapFragment;
import georgia.languagelandscape.fragments.MyProjectsFragment;
import georgia.languagelandscape.fragments.NewProjectFragment;
import georgia.languagelandscape.fragments.ProfileFragment;

/**
 * MapActivity is where we control all the fragment transactions
 * based on navigation drawer selection.
 *
 * It also handles events from {@link MapFragment}.
 */
public class MapActivity extends BaseActivity
        implements MapFragment.mapListener {

    private static boolean active = false;
    private static double longitude = 27.0;
    private static double latitude = 25.0;
    public final static String GEO_LONGITUDE = "geo_longitude";
    public final static String GEO_LATITUDE = "geo_latitude";
    public final static String FRAGMENT_ID = "fragment_id";
//    public static enum Frags {MAP, FEED, NEW_PROJECT, MY_PROJECT, PROFILE, SETTINGS};
    public static final int FRAG_MAP = 2000;
    public static final int FRAG_FEED = 2001;
    public static final int FRAG_NEW_PROJECT = 2002;
    public static final int FRAG_MY_PROJECT = 2003;
    public static final int FRAG_PROFILE = 2004;
    public static final int FRAG_SETTINGS = 2005;
    public static final int FRAG_ABOUT = 2006;
    public static final int FRAG_HELP = 2007;

    private boolean canRecord = false;
    private int fragId;

    private MapFragment mapFragment= null;
    private FragmentManager fm = null;
    private FragmentTransaction ft = null;
    private LocationManager locationManager = null;
    private Location currentLocation = null;
    private LocationListener locationListener = null;
    private FloatingActionButton addFab;
    private FloatingActionButton projectFab;
    private Drawable addIcon;
    private Drawable recordIcon;
    private Drawable projectIcon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        super.onDrawerCreated();
        active = true;

        mapFragment = new MapFragment();
//        Bundle bundle = new Bundle();
//        bundle.putDouble(GEO_LONGITUDE, longitude);
//        bundle.putDouble(GEO_LATITUDE, latitude);
//        mapFragment.setArguments(bundle);
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
            Log.d("location", "getting last known location");
            longitude = currentLocation.getLongitude();
            latitude = currentLocation.getLatitude();
        }
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (isBetterLocation(location)){
                    Log.d("location", "better location find");
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();

                    Bundle bundle = new Bundle();
                    bundle.putDouble(GEO_LONGITUDE, longitude);
                    bundle.putDouble(GEO_LATITUDE, latitude);
                    mapFragment.setArguments(bundle);

                    try {
                        locationManager.removeUpdates(locationListener);
                    } catch (SecurityException e) {
                        Log.d("location", "could not removeUpdates");
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

        addFab = (FloatingActionButton) findViewById(R.id.addFab);
        projectFab = (FloatingActionButton) findViewById(R.id.projectFab);

        fragId = getIntent().getIntExtra(FRAGMENT_ID, FRAG_MAP);
        switchFragment(fragId);

        addIcon = getResources().getDrawable(R.drawable.ic_add_white_24dp);
        Bitmap bm = ((BitmapDrawable) addIcon).getBitmap();
        addIcon = new BitmapDrawable(
                getResources(),
                Bitmap.createScaledBitmap(
                        bm,
                        (int) Math.round(bm.getWidth() * 0.5),
                        (int) Math.round(bm.getHeight() * 0.5),
                        true));

        projectIcon = getResources().getDrawable(R.drawable.ic_queue_white_24dp);
        bm = ((BitmapDrawable) projectIcon).getBitmap();
        projectIcon = new BitmapDrawable(
                getResources(),
                Bitmap.createScaledBitmap(
                        bm,
                        (int) Math.round(bm.getWidth() * 0.3),
                        (int) Math.round(bm.getHeight() * 0.3),
                        true));

        recordIcon = getResources().getDrawable(R.drawable.ic_mic_none_white_24dp);
        bm = ((BitmapDrawable) recordIcon).getBitmap();
        recordIcon = new BitmapDrawable(
                getResources(),
                Bitmap.createScaledBitmap(
                        bm,
                        (int) Math.round(bm.getWidth() * 0.4),
                        (int) Math.round(bm.getHeight() * 0.4),
                        true));

        addFab.setImageDrawable(addIcon);
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canRecord) {
                    Intent intent = new Intent(MapActivity.this, RecordingActivity.class);
                    intent.putExtra(GEO_LONGITUDE, longitude);
                    intent.putExtra(GEO_LATITUDE, latitude);
                    startActivity(intent);
                } else {
                    projectFab.setVisibility(View.VISIBLE);
                    addFab.setImageDrawable(recordIcon);
                    canRecord = true;
                }
            }
        });

        projectFab.setImageDrawable(projectIcon);
        projectFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(FRAG_NEW_PROJECT);
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
        if (fragId == FRAG_NEW_PROJECT) {
            addFab.setVisibility(View.INVISIBLE);
            projectFab.setVisibility(View.INVISIBLE);
        } else {
            addFab.setVisibility(View.VISIBLE);
        }
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

    // TODO: switching fragment can be done within BaseActivity by checking whether MapActivity.isAlive()
    private void switchFragment(int fragId) {
        switch (fragId) {
            case FRAG_MAP:
                Bundle bundle = new Bundle();
                bundle.putDouble(GEO_LONGITUDE, longitude);
                bundle.putDouble(GEO_LATITUDE, latitude);
                mapFragment.setArguments(bundle);
                fm = getSupportFragmentManager();
                ft = fm.beginTransaction();
                ft.replace(R.id.content_replace, mapFragment);
                ft.commit();
                break;
            case FRAG_NEW_PROJECT:
                NewProjectFragment newProjectFragment= new NewProjectFragment();
                fm = getSupportFragmentManager();
                ft = fm.beginTransaction();
                ft.replace(R.id.content_replace, newProjectFragment);
                ft.commit();
                break;
            case FRAG_PROFILE:
                ProfileFragment profileFragment = new ProfileFragment();
                fm = getSupportFragmentManager();
                ft = fm.beginTransaction();
                ft.replace(R.id.content_replace, profileFragment);
                ft.commit();
                break;
            case FRAG_MY_PROJECT:
                MyProjectsFragment myProjectsFragment= new MyProjectsFragment();
                fm = getSupportFragmentManager();
                ft = fm.beginTransaction();
                ft.replace(R.id.content_replace, myProjectsFragment);
                ft.commit();
                break;
            case FRAG_ABOUT:
                AboutFragment aboutFragment= new AboutFragment();
                fm = getSupportFragmentManager();
                ft = fm.beginTransaction();
                ft.replace(R.id.content_replace, aboutFragment);
                ft.commit();
                break;
            case FRAG_HELP:
                HelpFragment helpFragment= new HelpFragment();
                fm = getSupportFragmentManager();
                ft = fm.beginTransaction();
                ft.replace(R.id.content_replace, helpFragment);
                ft.commit();
                break;
            default:
                break;
        }
    }

    @Override
    public void onMapClick(@Nullable LatLng latLng) {
        addFab.setImageDrawable(addIcon);
        projectFab.setVisibility(View.INVISIBLE);
        canRecord = false;
    }

    @Override
    public void onMapFragmentReady() {
        addFab.setVisibility(View.VISIBLE);
        addFab.setImageDrawable(addIcon);
        projectFab.setVisibility(View.INVISIBLE);
        canRecord = false;
    }

    @Override
    public void onCameraMove() {
        addFab.setImageDrawable(addIcon);
        projectFab.setVisibility(View.INVISIBLE);
        canRecord = false;
    }

    @Override
    public void onMarkerClick(Marker marker) {
        onMapClick(null);
        addFab.setVisibility(View.INVISIBLE);
    }
}
