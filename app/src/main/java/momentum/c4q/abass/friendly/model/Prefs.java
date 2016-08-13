package momentum.c4q.abass.friendly.model;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by c4q-ac29 on 8/12/16.
 */
public class Prefs {

    private static final String PREFS_OWNER_NAME = "owner's_name";
    public static final String IS_FIRST_RUN = "first_run";
    private static final String TAG = "Prefs";

    public static String getOwnerName(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getString(PREFS_OWNER_NAME, "Abass"); //fixme
    }


    public static void setOwnerName(Context ctx, String name) {
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().putString(PREFS_OWNER_NAME, name).apply();
    }

    public static boolean isFirstRun(Context ctx) {
        boolean firstRun = PreferenceManager.getDefaultSharedPreferences(ctx).getBoolean(IS_FIRST_RUN, true);
        Log.d(TAG, "First run " + firstRun);
        return firstRun;
    }

    public static void disableWelcome(Context ctx) {
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().putBoolean(IS_FIRST_RUN, false).apply();
    }
}
