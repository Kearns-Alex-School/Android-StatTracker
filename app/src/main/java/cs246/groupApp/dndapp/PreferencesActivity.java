package cs246.groupApp.dndapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PreferencesActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";

    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        context = PreferencesActivity.this;
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        // prevent the keyboard from showing right away
        //  https://stackoverflow.com/questions/9732761/prevent-the-keyboard-from-displaying-on-activity-start
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // dynamically add to our scrollView
        //  https://stackoverflow.com/questions/14920535/how-to-add-more-than-one-button-to-scrollview-in-android
        LinearLayout linearLayout = findViewById(R.id.contentLLV);

        // because we dynamically add each parameter, we can then save them in the same order
        // AJK: NEED TO REPLACE WITH OUT PREFERENCES THAT WE WANT TO KEEP TRACK OF
        for (int index = 1; index <= 10; index++)
        {
            String temp = "Parameter " + index;

            // Add Buttons
            TextView text = new TextView(context);
            text.setText(temp);
            linearLayout.addView(text);

            // Add Buttons
            EditText editText = new EditText(context);
            editText.setHint(temp);
            linearLayout.addView(editText);
        }
    }

    public void saveClick(View view) {
        // because we dynamically add each parameter, we can then save them in the same order
        // grab all of the EditTexts in the layout and go through them all
        //  https://stackoverflow.com/questions/7784418/get-all-child-views-inside-linearlayout-at-once
        LinearLayout linearLayout = findViewById(R.id.contentLLV);

        final int childCount = linearLayout.getChildCount();
        for (int i = 0; i < childCount; i++)
        {
            View tempView = linearLayout.getChildAt(i);

            // AJK: We can do it this way or we can hard code that the first one is always a TextView
            // and the next is the EditText. Testing will probably help us out here.
            if (tempView instanceof EditText) {
                EditText edittext = (EditText) tempView;

                // AJK: UPDATE PARAMETER ASSOCIATED WITH THIS SPOT. WE NEED TO TEST THIS OUT ONCE WE CAN!!!!!!
                String value = edittext.getText().toString();

                Toast toast = Toast.makeText(context, value, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, i * 50);
                toast.show();
            }
        }

        this.finish();
    }

    public void cancelClick(View view) {
        this.finish();
    }
}
