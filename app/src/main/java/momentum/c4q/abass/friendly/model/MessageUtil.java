package momentum.c4q.abass.friendly.model;

/**
 * Created by Abass on 7/22/16.
 */
public class MessageUtil {


    public static String makeMessage(Contact contact, boolean addressReady){
        String out = "Hi " + contact.getName() +
                ", It was nice meeting you.";
        if(addressReady) {
            out += " We met at " + contact.getAddressStr();
        }
        while(out.contains("null") || out.contains("United States")){
            out = out.replace("null", "");
            out.replace("United States", "");
        }
        return out;


    }
}
