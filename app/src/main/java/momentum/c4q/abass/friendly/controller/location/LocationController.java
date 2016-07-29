package momentum.c4q.abass.friendly.controller.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.DetectedActivityResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import momentum.c4q.abass.friendly.controller.MainActivity;
import momentum.c4q.abass.friendly.model.Contact;

/**
 * Created by Abass on 7/22/16.
 */
public class LocationController extends LocationCallback implements LocationListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks{
    //    ResultCallback<DetectedActivityResult>,
    private static final String TAG = LocationController.class.getSimpleName();
    private LocationRequest locationRequest;
    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    private GoogleApiClient googleApiClient;
    private static Context context;
    public Location currentLocation;
    private LocationManager locationManager;

    private static final long MIN_DISTANCE_FOR_UPDATE = 10;
    private static final long MIN_TIME_FOR_UPDATE = 1000 * 60 * 2;


    public LocationController(Context context) {
        this.context = context;
        locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
//                .addApi(Awareness.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
    }

    public Context getContext() {
        return context;
    }

    public void startLocationAPI() {
        googleApiClient.connect();
        startLocationUpdates();
        Awareness.SnapshotApi.getDetectedActivity(googleApiClient);
    }

    public void stopLocationAPI() {
        // only stop if it's connected, otherwise we crash
        if (googleApiClient != null && googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, (LocationCallback) this);
            googleApiClient.disconnect();
        }
    }

    public Location getLocationfromMgr() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            Toast.makeText(getContext(), "Connect to internet", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (locationManager != null) {
            List<String> providers = locationManager.getProviders(true);
            locationManager.requestLocationUpdates(providers.get(0), MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE, (android.location.LocationListener) this);
            currentLocation = locationManager.getLastKnownLocation(providers.get(0));

        }
        return currentLocation;

    }


    public Location getCurrentLocation() {
        if(currentLocation == null){
            currentLocation = getLocationfromMgr();
        }
        return currentLocation;
    }





    protected void startLocationUpdates() {
        // Create the location request
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            //Execute location service call if user has explicitly granted ACCESS_FINE_LOCATION..
        }

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        // Note that this can be NULL if last location isn't already known.
        if (currentLocation != null) {
            // Print current location if not null
            Log.d("DEBUG", "current location: " + currentLocation.toString());
            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        }
        // Begin polling for new location updates.
        startLocationUpdates();
    }


    @Override
    public void onConnectionSuspended(int i) {
        switch (i) {
            case CAUSE_SERVICE_DISCONNECTED:
                Toast.makeText(context, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
                break;
            case CAUSE_NETWORK_LOST:
                Toast.makeText(context, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(context, "Location Services Failed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        currentLocation = location;
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }


}
