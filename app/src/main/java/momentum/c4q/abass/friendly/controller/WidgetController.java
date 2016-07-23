package momentum.c4q.abass.friendly.controller;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.EditText;

import momentum.c4q.abass.friendly.R;
import momentum.c4q.abass.friendly.location.LocationController;
import momentum.c4q.abass.friendly.model.Contact;
import momentum.c4q.abass.friendly.model.ContactQuerier;

/**
 * Created by Abass on 7/13/16.
 */
public class WidgetController extends LocationController implements Controller{
    private static String TAG = WidgetController.class.getSimpleName();
    final static String WIDGET_UPDATE_ACTION = "momentum.c4q.abass.friendly.intent.action.UPDATE_WIDGET";
    private ContactQuerier contactQuerier;
    private SmsManager smsMgr;


    public WidgetController(Context context) {
        super(context);
        contactQuerier = new ContactQuerier(context);
        smsMgr = SmsManager.getDefault();
    }

    public Contact createContact(EditText nameField, EditText numberField) {
        String name = nameField.getText().toString();
        String number = numberField.getText().toString();
        return new Contact(name, number);
    }

    @Override
    public void processContact(Contact contact) {
        String logMsg = "Processing Contact " + contact.getName() + "" + contact.getNumber();
        Log.d(TAG, logMsg);
        String message = assignLocationAndFormMessage(contact);
        contactQuerier.addContact(contact);
        sendMessage(contact, message);
    }


    public void sendMessage(Contact contact, String message) {
        Intent intent = new Intent();
        Intent intent2 = new Intent();
        PendingIntent p1 = PendingIntent.getActivity(getContext(), (int) System.currentTimeMillis(), intent, 0);
        PendingIntent p2 = PendingIntent.getActivity(getContext(), (int) System.currentTimeMillis(), intent2, 0);
        smsMgr.sendTextMessage(contact.getNumber(), null, message, p1, p2);
    }

    public String assignLocationAndFormMessage(Contact contact) {
        String location = getLocation();
        contact.setLocation(location);
        StringBuilder messageStr = new StringBuilder();
        messageStr.append("Hi " + contact.getName() + " ");
        messageStr.append(getContext().getString(R.string.default_msg) + " " + location);
        Log.d(TAG, messageStr.toString());
        return messageStr.toString();
    }


}