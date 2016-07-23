package momentum.c4q.abass.friendly.controller;

import android.content.Context;
import android.location.LocationListener;
import android.widget.EditText;

import com.google.android.gms.common.api.GoogleApiClient;

import momentum.c4q.abass.friendly.model.Contact;

/**
 * Created by Abass on 7/18/16.
 */
public interface Controller extends GoogleApiClient.ConnectionCallbacks {

    Contact createContact(EditText nameField, EditText numberField);

    void processContact(Contact contact);


}
