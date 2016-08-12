package momentum.c4q.abass.friendly.controller;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

import momentum.c4q.abass.friendly.Mvc;
import momentum.c4q.abass.friendly.R;
import momentum.c4q.abass.friendly.controller.location.LocationHelper;
import momentum.c4q.abass.friendly.model.Contact;
import momentum.c4q.abass.friendly.model.PhoneBook;

/**
 * Created by Abass on 7/13/16.
 */
public class WidgetController implements Mvc.Controller {

    private static String TAG = WidgetController.class.getSimpleName();
    final static String WIDGET_UPDATE_ACTION = "momentum.c4q.abass.friendly.intent.action.UPDATE_WIDGET";
    private PhoneBook phoneBook;
    private SmsManager smsMgr;
    private Context context;
    private Contact contact;
    private LocationHelper locationHelper;


    public WidgetController(Context context) {
        this.context = context;
        if(phoneBook == null) phoneBook = PhoneBook.getInstance(context);
        smsMgr = SmsManager.getDefault();
        this.locationHelper = new LocationHelper(context, this);
        locationHelper.startLocationAPI();


    }

    public Contact createContact(EditText nameField, EditText numberField) {
        String name = nameField.getText().toString();
        String number = numberField.getText().toString();
        this.contact = new Contact(name, number);
        return contact;
    }

    @Override
    public void processContact(Contact contact) {
        String logMsg = "Processing Contact " + contact.getName() + " " + contact.getNumber();
        Log.d(TAG, logMsg);
        phoneBook.addContact(contact);
        if(locationHelper.isConnected()) {
            locationHelper.getPlacesAsync();
        }else{
            if(!locationHelper.isConnecting()) {
               locationHelper.startLocationAPI();;
            }

        }
    }


    @Override
    public void assignLocation(Contact contact, String location) {
        contact.setLocation(location);
        StringBuilder messageStr = new StringBuilder();
        messageStr.append("Hi " + contact.getName() + " ");
        messageStr.append(context.getString(R.string.default_msg) + " " + location);
        Log.d(TAG, messageStr.toString());
        contact.setMessage(messageStr.toString());
    }


    public void sendMessage(Contact contact, String message) {
        Intent intent = new Intent();
        Intent intent2 = new Intent();
        PendingIntent p1 = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);
        PendingIntent p2 = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent2, 0);
        smsMgr.sendTextMessage(contact.getNumber(), null, message, p1, p2);
    }


    @Override
    public void onPlaceRecognized(Place place) {
        if(contact != null) {
            Log.d(TAG, "Found Place " + place.getName());
            assignLocation(contact, place.getAddress().toString());
            sendMessage(contact, contact.getMessage());
        }
        locationHelper.stopLocationAPI();
    }

    @Override
    public void onFailure() {
        Log.d(TAG, "Failed to recognize location");
        Toast.makeText(context, "Unable to find location ", Toast.LENGTH_SHORT).show();
        sendMessage(contact, contact.getMessage());
        locationHelper.stopLocationAPI();

    }
}