package ca.uwaterloo.camevent;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
//import android.widget.Toast;

public class MapsActivity extends AppCompatActivity
        implements
        OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMap.OnMarkerClickListener
{
    private GoogleApiClient client;
    private GoogleMap mMap;
    /**
     * Request code for location permission request.
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    /**
     * Flag indicating whether a requested permission has been denied after returning in
     */
    private boolean mPermissionDenied = false;
    private String locationname="";
    private String date="";
    private String titlekey="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                locationname= "";
                date="";
                titlekey="";
            } else {
                locationname= extras.getString("buildingData");
                date=extras.getString("fromDateData");
                titlekey=extras.getString("keyData");
            }
        } else {
            locationname= (String) savedInstanceState.getSerializable("buildingData");
            date= (String) savedInstanceState.getSerializable("fromDateData");
            titlekey= (String) savedInstanceState.getSerializable("keyData");
        }
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        EventDBHandler eventdb = new EventDBHandler(this);
        List<Eventinfo> eventinfos;
        if (locationname.equals("") & date.equals("") & titlekey.equals(""))
        {
            eventinfos = eventdb.getAllEvents();
            for(int i=0;i<eventinfos.size();i++) {
                //Log.d("running", String.valueOf(eventinfos.size()));
                if(eventinfos.get(i).getEventLatitude()!="0"&eventinfos.get(i).getEventLongitude()!="0"){
                    //int positionchange =i;
                    // List<Marker> markers = new ArrayList<>();
                    Marker marker=mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(eventinfos.get(i).getEventLatitude()),Double.parseDouble(eventinfos.get(i).getEventLongitude())))
                            .title(eventinfos.get(i).getEventTitle())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.waterloo))
                    );
                    marker.setTag(0);
                    //markers.add(marker);
                }
            }
        }
        else
        {
            eventinfos=eventdb.getAllEventsbysearch(locationname,titlekey,date);
            for(int i=0;i<eventinfos.size();i++) {
                //Log.d("running", String.valueOf(eventinfos.size()));
                if(eventinfos.get(i).getEventLatitude()!="0"&eventinfos.get(i).getEventLongitude()!="0"){
                    //int positionchange =i;
                    // List<Marker> markers = new ArrayList<>();
                    Marker marker=mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(eventinfos.get(i).getEventLatitude())+0.0001*i,Double.parseDouble(eventinfos.get(i).getEventLongitude())))
                            .title(eventinfos.get(i).getEventTitle())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.waterloo))
                    );
                    marker.setTag(0);
                    //markers.add(marker);
                }
            }
        }

        // Set a listener for marker click.
        mMap.setOnMarkerClickListener(this);
        enableMyLocation();
        mMap.setOnMyLocationButtonClickListener(this);
        //setUpClusterer();
    }

    public boolean onMarkerClick(final Marker marker) {
        /**
         * Called when the user clicks a marker.
         */
        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();
        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            Toast.makeText(this,
                    marker.getTitle() +
                            " has been clicked ",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, DisplayWaterlooActivity.class);
            intent.putExtra("Database Data",marker.getTitle());
            startActivity(intent);
        }
        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    private void enableMyLocation() {
        /**
         * Enables the My Location layer if the fine location permission has been granted.
         */
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else {
            if (mMap != null) {
                // Access to the location has been granted to the app.
                mMap.setMyLocationEnabled(true);
            }
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "Locating", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        //GoogleMap.getMyLocation();
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    private void showMissingPermissionError() {
        /**
         * Displays a dialog with error message explaining that the location permission is missing.
         */
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://ca.uwaterloo.camevent/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://ca.uwaterloo.camevent/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
