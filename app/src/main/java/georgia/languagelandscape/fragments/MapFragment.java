package georgia.languagelandscape.fragments;

//import android.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.WeakHashMap;

import georgia.languagelandscape.MapActivity;
import georgia.languagelandscape.MarkerDialogActivity;
import georgia.languagelandscape.R;
import georgia.languagelandscape.data.Recording;
import georgia.languagelandscape.database.RecordingDataSource;


public class MapFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mGoogleMap;
    private Context context;
    private WeakHashMap<String, Recording> recordingMap = new WeakHashMap<>();

    public interface mapListener {
        public void onMapClick(LatLng latLng);

        public void onMapFragmentReady();

        public void onCameraMove();

        public void onMarkerClick(Marker marker);
    }

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

        if (context instanceof mapListener) {
            ((mapListener) context).onMapFragmentReady();
        }
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
                        .position(new LatLng(recording.getLatitude(), recording.getLongitude()))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ll_logo_marker)));
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
                Intent intent = new Intent(context, MarkerDialogActivity.class);
                intent.putExtras(bundle);

                if (context instanceof mapListener) {
                    ((mapListener) context).onMarkerClick(marker);
                }

                startActivity(intent);

                return true;
            }
        });

        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (context instanceof mapListener) {
                    ((mapListener) context).onMapClick(latLng);
                } else {
                    Toast.makeText(
                            context,
                            "mapListener not implemented!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        mGoogleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                if (context instanceof mapListener) {
                    ((mapListener) context).onCameraMove();
                }
            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}