package georgia.languagelandscape.fragments;

//import android.app.Fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

import georgia.languagelandscape.DialogActivity;
import georgia.languagelandscape.MapActivity;
import georgia.languagelandscape.data.Markers;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mGoogleMap;
    private double longitude = 0.0;
    private double latitude = 0.0;

    HashMap<String, String> markerMap = new HashMap<String, String>();

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleMap == null) {
            getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        longitude = getArguments().getDouble(MapActivity.GEO_LONGITUDE);
        latitude = getArguments().getDouble(MapActivity.GEO_LATITUDE);


        // adding marker

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude)).zoom(5).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));


        final ArrayList<Double> longitudes =Markers.getLongitudes();
        final ArrayList<Double> latitudes=Markers.getLongitudes();
        final ArrayList<String> titles=Markers.getTitles();

        String string = longitudes.toString();
        Log.d("cf", string);


        for (int i = 0; i < longitudes.size(); i++) {
            LatLng loc = new LatLng(latitudes.get(i), longitudes.get(i));
            String title;
            if(titles.get(i)!=null)
                title=titles.get(i);
            else
                title="da";
            final Marker marker=googleMap.addMarker(new MarkerOptions()
                    .position(loc)
                    .title(title));
            Log.d("cfd", title);
            String id;
            id=marker.getId();
            markerMap.put(id,title);
            Log.d("d", "1");
        }

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng latlng=marker.getPosition();
                String title=marker.getTitle();
                Markers.setCurrent_Title(title);
                Intent intent = new Intent(getActivity(), DialogActivity.class);
                startActivity(intent);
                return true;
            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}