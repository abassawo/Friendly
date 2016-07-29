package momentum.c4q.abass.friendly.controller;

import android.content.Context;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import momentum.c4q.abass.friendly.controller.location.AddressUtil;
import momentum.c4q.abass.friendly.controller.location.LocationController;
import momentum.c4q.abass.friendly.model.Contact;
import momentum.c4q.abass.friendly.model.ContactQuerier;

/**
 * Created by Abass on 7/13/16.set
 */
public class WidgetController extends LocationController implements Controller, AddressUtil.OnLocationParsedListener{
    private static String TAG = WidgetController.class.getSimpleName();
    final static String WIDGET_UPDATE_ACTION = "momentum.c4q.abass.friendly.intent.action.UPDATE_WIDGET";
    private ContactQuerier contactQuerier;
    private SmsManager smsMgr;
    private static Contact contact;
;



    public WidgetController(Context context) {
        super(context);
        contactQuerier = new ContactQuerier(context);
        smsMgr = SmsManager.getDefault();
    }

    public Contact createContact(EditText nameField, EditText numberField) {
        String name = nameField.getText().toString();
        String number = numberField.getText().toString();
        contact = new Contact(name, number);
        return contact;
    }

    @Override
    public void processContact(Contact contact) {
        String logMsg = "Processing Contact " + contact.getName() + "" + contact.getNumber();
        Log.d(TAG, logMsg);
        contactQuerier.addContact(contact);
        if (currentLocation != null) {
            AddressUtil friendly = new AddressUtil(getContext(), currentLocation, this);
        }else{
            Toast.makeText(getContext(), "Check Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendMessage(String message){
//        PendingIntent p1 = PendingIntent.getActivity(getContext(), (int) System.currentTimeMillis(), null, 0);
//        PendingIntent p2 = PendingIntent.getActivity(getContext(), (int) System.currentTimeMillis(), null, 0);
        smsMgr.sendTextMessage(contact.getNumber(), null, message, null, null);

    }

    @Override
    public void onSuccess(String address) {
        if(address.contains("null")){
            address.replace("null", "");
        }
        Log.d("Got an address", address);
        String message = contact.getMessage();
        sendMessage(message);


    }

    @Override
    public void onFailure() {
        Toast.makeText(getContext(), "Unable to find address", Toast.LENGTH_SHORT).show();
        String message = contact.getMessage();
        sendMessage(message);
    }
}