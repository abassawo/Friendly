package momentum.c4q.abass.friendly.model;

import android.support.annotation.Nullable;

import butterknife.BindString;
import butterknife.ButterKnife;
import momentum.c4q.abass.friendly.R;

/**
 * Created by Abass on 7/13/16.
 */
public class Contact {

    private String name, number, message;
    private String location;
    @Nullable String email;

    public Contact(String name, String number){
        this.name = name;
        this.number = number;
        this.email = null;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public void setLocation(String location){
        this.location = location;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
