package momentum.c4q.abass.friendly.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Abass on 7/13/16.
 */
public class WidgetIntentReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(WidgetController.WIDGET_UPDATE_ACTION)) {
            //update widget UI;
        } else {
            context.startActivity(intent);
        }
    }
}
