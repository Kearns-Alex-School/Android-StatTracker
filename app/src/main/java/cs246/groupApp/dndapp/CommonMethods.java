package cs246.groupApp.dndapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.widget.Toast;

import java.util.Objects;

/**
 * Class to encapsulate commonly used code.
 * @author Alex Kearns
 */
class CommonMethods {

    static SharedPreferences SP;

    /**
     * Shows a toast message in the center top of the screen.
     * @param context Context object.
     * @param message String containing message to be displayed.
     * @param length Length of time to show toast.
     * @author Alex Kearns
     */
    static void showCenterTopToast(Context context, String message, int length) {
        Toast toast= Toast.makeText(context, message, length);

        //  https://stackoverflow.com/questions/2506876/how-to-change-position-of-toast-in-android
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 120);

        toast.show();
    }

    /**
     * Allows us to update our theme across all of our activities universally
     * @param activity The activity that will be updated
     * @param recreate If we need to reload the activity (recreate())
     * @author Alex Kearns
     */
    static void updateTheme(Activity activity, Boolean recreate) {
        SP = PreferenceManager.getDefaultSharedPreferences(activity.getBaseContext());

        switch(Objects.requireNonNull(SP.getString(MainActivity.THEME_STYLE, "light"))) {
            case "light":
                activity.setTheme(R.style.AppThemeLight);
                break;

            case "dark":
                activity.setTheme(R.style.AppThemeDark);
                break;

            case "dndark":
                activity.setTheme(R.style.DnDark);
                break;
        }

        if (recreate)
            activity.recreate();
    }
}