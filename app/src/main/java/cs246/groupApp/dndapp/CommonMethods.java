package cs246.groupApp.dndapp;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Class to encapsulate commonly used code.
 * @author Alex Kearns
 */
class CommonMethods {

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
}