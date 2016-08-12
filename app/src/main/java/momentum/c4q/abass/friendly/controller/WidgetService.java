package momentum.c4q.abass.friendly.controller;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Abass on 7/12/16.
 */
public class WidgetService extends IntentService {
    private static String TAG = WidgetService.class.getSimpleName();

    public WidgetService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
//        Intent intent = new Intent(context, MainActivity.class);

    }
}
