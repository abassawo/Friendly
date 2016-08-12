package momentum.c4q.abass.friendly.model;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.provider.ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE;
import static android.provider.ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME;

/**
 * Created by Abass on 7/18/16.
 */
public class PhoneBook {

    private final String TAG = PhoneBook.class.getSimpleName();
    private static PhoneBook INSTANCE;
    private  Context context;

    private PhoneBook(Context context){
        this.context = context;
    }

    public static PhoneBook getInstance(Context context) {
        if(INSTANCE == null){
            INSTANCE = new PhoneBook(context);
        }
        return INSTANCE;
    }


    public void addContact(final Contact contact) {
        ArrayList<ContentProviderOperation> ops = (ArrayList) getContentProvOperations(contact);
        // Asking the Contact provider to create a new contact
        try {
            context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Toast.makeText(context, "Contact info " + contact.getName() + " added.", Toast.LENGTH_SHORT).show();
    }



    private List<ContentProviderOperation> getContentProvOperations(Contact contact) {
        if (contact == null) return null;
        List<ContentProviderOperation> ops = new ArrayList<>();
        ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
        builder.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0);

        if(contact.getName() != null)
                builder.withValue(ContactsContract.Data.MIMETYPE, CONTENT_ITEM_TYPE);
                builder.withValue(DISPLAY_NAME, null).build();
                Log.d(TAG, "display name" + contact.getName());
        if(contact.getNumber() != null) {
            builder.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
            builder.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.getNumber());
            builder.withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        }
        if(contact.getEmail() != null){
            builder.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0);
            builder.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
            builder.withValue(ContactsContract.CommonDataKinds.Email.DATA, contact.getEmail());
            builder.withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK);
        }
        ops.add(builder.build());
        return ops;
    }


}

