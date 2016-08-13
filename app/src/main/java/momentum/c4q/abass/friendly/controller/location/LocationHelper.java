//package momentum.c4q.abass.friendly.controller.location;
//
//import android.*;
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.google.android.gms.awareness.Awareness;
//import com.google.android.gms.awareness.snapshot.DetectedActivityResult;
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.common.api.ResultCallback;
//import com.google.android.gms.location.ActivityRecognitionResult;
//import com.google.android.gms.location.DetectedActivity;
//import com.google.android.gms.location.LocationCallback;
//import com.google.android.gms.location.LocationListener;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.maps.model.LatLng;
//
///**
// * Created by Abass on 7/22/16.
// */
//public class LocationHelper extends LocationCallback implements LocationListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
//    private static final String TAG = LocationHelper.class.getSimpleName();
//    private LocationRequest locationRequest;
//    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
//    private long FASTEST_INTERVAL = 2000; /* 2 sec */
//    private GoogleApiClient googleApiClient;
//    private Context context;
//    private Location currentLocation;
//    private OnLocationListener listener;
//
//
//    public LocationHelper(Context context, OnLocationListener listener) {
//        this.context = context;
//        this.listener = listener;
//        googleApiClient = new GoogleApiClient.Builder(context)
//                .addApi(LocationServices.API)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this).build();
//
//    }
//
//
//    public void startLocationAPI() {
//        if (googleApiClient.isConnected())
//            startLocationUpdates();
//        else googleApiClient.connect();
//    }
//
//    public void stopLocationAPI() {
//        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, (LocationCallback) this);
//        // only stop if it's connected, otherwise we crash
//        if (googleApiClient != null) {
//            googleApiClient.disconnect();
//        }
//    }
//
//
//    private void startLocationUpdates() {
//        // Create the location request
//        locationRequest = LocationRequest.create()
//                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//                .setInterval(UPDATE_INTERVAL)
//                .setFastestInterval(FASTEST_INTERVAL);
//        // Request location updates
//        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
//
//        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
//            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
//            //Execute location service call if user has explicitly granted ACCESS_FINE_LOCATION..
//        }
//
//    }
//
//
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        startLocationUpdates();
//    }
//
//
//    @Override
//    public void onConnectionSuspended(int i) {
//        switch (i) {
//            case CAUSE_SERVICE_DISCONNECTED:
//                Toast.makeText(context, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
//                break;
//            case CAUSE_NETWORK_LOST:
//                Toast.makeText(context, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
//                break;
//        }
//
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//        Toast.makeText(context, "Location Services Failed", Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//        String msg = "Updated Location: " +
//                Double.toString(location.getLatitude()) + "," +
//                Double.toString(location.getLongitude());
//        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
//        // Note that this can be NULL if last location isn't already known.
//        if (currentLocation != null) {
//            // Print current location if not null
//            Log.d("DEBUG", "current location: " + currentLocation.toString());
//            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
//            listener.onLocationRecognized(currentLocation);
//        }
//        // Begin polling for new location updates.
//
//        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
//        // You can now create a LatLng Object for use with maps
//        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        listener.onLocationRecognized(currentLocation); //fixme
//    }
//    public boolean isConnected() {
//        return googleApiClient.isConnected();
//    }
//
//    public boolean isConnecting() {
//        return googleApiClient.isConnecting();
//    }
//
//
//
//    public interface OnLocationListener{
//        void onLocationRecognized(Location location);
//        void onFailure();
//    }
//}
