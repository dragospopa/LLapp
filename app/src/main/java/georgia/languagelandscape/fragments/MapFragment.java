package georgia.languagelandscape.fragments;

//import android.app.Fragment;

import android.app.Dialog;
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

import georgia.languagelandscape.DialogActivity;
import georgia.languagelandscape.ForgotPassActivity;
import georgia.languagelandscape.MapActivity;
import georgia.languagelandscape.SignUpActivity;
import georgia.languagelandscape.data.Markers;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends SupportMapFragment implements OnMapReadyCallback{

    private GoogleMap mGoogleMap;
    private double longitude = 0.0;
    private double latitude = 0.0;

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





        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude)).zoom(5).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));


        Markers.addLongitude(45.657975);
        Markers.addLatitude(45.657975);

        Markers.addLongitude(-0.1324591);
        Markers.addLatitude(51.523229);


        ArrayList<Double> longitudes= Markers.getLongitudes();
        ArrayList<Double> latitudes= Markers.getLatitudes();
        ArrayList<String> titles=Markers.getTitles();

        String string=longitudes.toString();
        Log.d("cf",string);


        for(int i=0;i<longitudes.size();i++)
        {
            LatLng loc = new LatLng(latitudes.get(i), longitudes.get(i));
            googleMap.addMarker(new MarkerOptions()
                    .position(loc)
                    .title("yay"));

            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {


                                                   @Override
                                                   public boolean onMarkerClick(Marker marker) {
                                                       Intent intent = new Intent(getActivity(), DialogActivity.class);
                                                       // intent.putExtra(MapActivity.FRAGMENT_ID, MapActivity.FRAG_MAP);
                                                       startActivity(intent);
                                                       Log.d("cf","merge");
                                                       return true;
                                                   }
                                               });

            Log.d("d","1");
        }


    }

}

