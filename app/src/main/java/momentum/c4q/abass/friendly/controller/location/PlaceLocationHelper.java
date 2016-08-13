package momentum.c4q.abass.friendly.controller.location;

/**
 * Created by c4q-ac29 on 8/12/16.
 */

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;

/**
 * Created by Abass on 7/22/16.
 */
public class PlaceLocationHelper implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    private static final String TAG = PlaceLocationHelper.class.getSimpleName();
    private GoogleApiClient googleApiClient;
    private Context context;
    private OnPlaceListener listener;


    public PlaceLocationHelper(Context context, OnPlaceListener listener) {
        this.context = context;
        this.listener = listener;
        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
    }


    public void startLocationAPI() {
        googleApiClient.connect();
    }

    public void stopLocationAPI() {
        // only stop if it's connected, otherwise we crash
        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }
    }




    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        getPlacesAsync();
    }

    public void getPlacesAsync() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                .getCurrentPlace(googleApiClient, null);
        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                    Log.i(TAG, String.format("Place '%s' has likelihood: %g",
                            placeLikelihood.getPlace().getName(),
                            placeLikelihood.getLikelihood()));
                }

                Place place = likelyPlaces.get(0).getPlace();
                listener.onPlaceRecognized(place);
                likelyPlaces.release();


            }
        });

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
        listener.onFailure();
    }

    public boolean isConnected() {
        return googleApiClient.isConnected();
    }

    public boolean isConnecting() {
        return googleApiClient.isConnecting();
    }


    public interface OnPlaceListener {
        void onPlaceRecognized(Place place);
        void onFailure();
    }
}
