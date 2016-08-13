package momentum.c4q.abass.friendly;

import android.widget.EditText;

//import momentum.c4q.abass.friendly.controller.location.LocationHelper;
import momentum.c4q.abass.friendly.model.Contact;

/**
 * Created by c4q-ac29 on 8/9/16.
 */
public interface Mvc {

    public interface Model{
        void addContact(final Contact contact);

    }

    public interface View{

    }

    public interface Controller{
        Contact createContact(EditText nameField, EditText numberField);
        void processContact(Contact contact);
        void assignLocation(Contact contact, String location);
        void sendMessage(Contact contact, String message);
    }
}


