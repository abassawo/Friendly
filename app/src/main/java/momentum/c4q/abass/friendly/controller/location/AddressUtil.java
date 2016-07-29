package momentum.c4q.abass.friendly.controller.location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Abass on 7/22/16.
 */
public class AddressUtil {
    private static final String TAG = AddressUtil.class.getSimpleName();
    private Location location;
    private static String addressStr = null;
    private Context context;
    private AddressParser parser = new AddressParser();
    private OnLocationParsedListener listener;

    public AddressUtil(Context context, Location location, OnLocationParsedListener listener) {
        this.listener = listener;
        this.context = context;
        this.location = location;
        parser.execute(location);
    }

    public String getAddressStr() {
        if(addressStr == null){
            parser.getAddressFromLocation(location);
            try {
                throw new Exception("Call parser.execute(location) before getting address str");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return addressStr;
    }


    private class AddressParser extends AsyncTask<Location, Void, String> {

        @Override
        protected String doInBackground(Location... locations) {
            return getAddressFromLocation(locations[0]);
        }

        public String getAddressFromLocation(Location location) {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            try {
                List<Address> addressList = geocoder.getFromLocation(
                        location.getLatitude(), location.getLongitude(), 1);
                if (addressList != null && addressList.size() > 0) {
                    Address address = addressList.get(0);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        sb.append(address.getAddressLine(i)).append("\n");
                    }
                    sb.append(address.getLocality()).append("\n");
                    sb.append(address.getPostalCode()).append("\n");
                    sb.append(address.getCountryName());
                    return sb.toString();
                }
            } catch (IOException e) {
                listener.onFailure();
                Log.e(TAG, "Unable connect to Geocoder", e);
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Toast.makeText(context, "Connecting to Geocoder services was cancelled ", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(String address) {
            super.onPostExecute(address);
            addressStr = address;
            listener.onSuccess(address);
        }
    }

    public interface OnLocationParsedListener{

        void onSuccess(String parsedAddress);
        void onFailure();

    }


}


