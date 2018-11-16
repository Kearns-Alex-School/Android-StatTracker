package cs246.groupApp.dndapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CharacterDetailsActivity extends AppCompatActivity {
    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_details);

        context = CharacterDetailsActivity.this;

        // dynamically add to our scrollView
        //  https://stackoverflow.com/questions/14920535/how-to-add-more-than-one-button-to-scrollview-in-android
        LinearLayout linearLayout = findViewById(R.id.contentLLV);

        // because we dynamically add each parameter, we can then save them in the same order
        for (int index = 1; index <= 6; index++)
        {
            String temp = "Stat " + index + " \n9000\n+1";

            // Add Buttons
            Button button = new Button(context);
            button.setTextSize(10);
            button.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);
            button.setText(temp);
            linearLayout.addView(button);
        }

        // Add Buttons
        TextView textview = new TextView(context);
        textview.setText("Passive\nPerception\n10");
        linearLayout.addView(textview);
    }
}
