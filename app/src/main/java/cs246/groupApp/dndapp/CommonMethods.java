package cs246.groupApp.dndapp;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class CommonMethods {

    public static void showCenterTopToast(Context context, String message, int length) {
        Toast toast= Toast.makeText(context, message, length);

        //  https://stackoverflow.com/questions/2506876/how-to-change-position-of-toast-in-android
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 120);

        toast.show();
    }
}