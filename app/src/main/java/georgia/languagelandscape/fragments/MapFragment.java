package georgia.languagelandscape.fragments;

//import android.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.WeakHashMap;

import georgia.languagelandscape.DialogActivity;
import georgia.languagelandscape.MapActivity;
import georgia.languagelandscape.data.Recording;
import georgia.languagelandscape.database.RecordingDataSource;


public class MapFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mGoogleMap;
    private Context context;
    private WeakHashMap<String, Recording> recordingMap = new WeakHashMap<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

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

        double longitude = getArguments().getDouble(MapActivity.GEO_LONGITUDE);
        double latitude = getArguments().getDouble(MapActivity.GEO_LATITUDE);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))
                .build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

        RecordingDataSource dataSource = new RecordingDataSource(context);
        dataSource.open();
        List<Recording> recordingsFromDB = dataSource.getAllRecordings();
        for (Recording recording : recordingsFromDB) {
            if (recording.isUploaded()) {
                Marker marker = googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(recording.getLatitude(), recording.getLongitude())));
                marker.setTag(recording.getRecordingID());
                recordingMap.put(recording.getRecordingID(), recording);
            }
        }
        dataSource.close();

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Recording recordingFromMarker = recordingMap.get(marker.getTag());
                Bundle bundle = new Bundle();
                bundle.putParcelable(Recording.PARCEL_KEY, recordingFromMarker);
                Intent intent = new Intent(context, DialogActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

                /*
                * default behavior is to show the info window and centre the focus to the marker
                * */
                return true;
            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}