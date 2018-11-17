package cs246.groupApp.dndapp;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class CharacterDetailsActivity extends AppCompatActivity {
    public Context context;
    public Character character;
    public File characterDir;

    String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_details);

        context = CharacterDetailsActivity.this;

        // get from intent
        characterDir = (File) getIntent().getExtras().get("dir");
        fileName = (String) getIntent().getExtras().get("filename");

        // read file
        this.character = readFile(fileName);

        // if there is a name, set it on the screen
        TextView titleText = findViewById(R.id.title);

        if (character.name.equals(""))
            character.name = fileName.replace(".txt", "");

        titleText.setText(character.name);

        // dynamically add to our scrollView
        //  https://stackoverflow.com/questions/14920535/how-to-add-more-than-one-button-to-scrollview-in-android
        LinearLayout linearLayout = findViewById(R.id.contentLLV);

        // because we dynamically add each parameter, we can then save them in the same order
        for (int index = 1; index <= 6; index++)
        {
            String temp = "Stat " + index + "\n9000\n+1";

            // Add Buttons
            Button button = new Button(context);
            button.setTextSize(10);
            button.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);
            button.setText(temp);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    editableButton(v);
                }
            });
            linearLayout.addView(button);
        }

        // Add TextView
        TextView textview = new TextView(context);
        textview.setText("Passive\nPerception\n10");
        textview.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);
        linearLayout.addView(textview);
    }

    // KM: not doing async right now since files are small.
    public Character readFile(String filename) {
        File file = new File(characterDir, filename);
        String contentJson = null;
        //create character object
        Character character;

        FileInputStream fis;
        try {
            fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();

            contentJson = new String(data, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // if file has contents, deserialize, otherwise create new character
        if (contentJson != null && !contentJson.equals("")) {
            Gson gson = new Gson();
            character = gson.fromJson(contentJson, Character.class);
        }
        else
            character = new Character();

        //save filename for later
        character.fileName = filename;

        Toast.makeText(this,"Loaded Character", Toast.LENGTH_SHORT).show();

        return character;
    }

    //KM: not doing async because of small files
    public void writeFile(String filename) {
        Gson gson = new Gson();
        String json = gson.toJson(this.character);

        File outputFile = new File(characterDir, filename);

        try {
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            outputStream.write(json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Toast.makeText(this,"Saved Character", Toast.LENGTH_SHORT).show();
    }

    public void editableTextView(final View view) {
        // create our popup dialog
        final Dialog dialog = new Dialog(context);

        // have the keyboard show up once we have the ability
        //  https://stackoverflow.com/questions/4258623/show-soft-keyboard-for-dialog
        Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        // remove the title bar MUST BE CALLED BEFORE SETTING THE CONTENT VIEW!!
        // https://stackoverflow.com/questions/2644134/android-how-to-create-a-dialog-without-a-title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.editable);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);

        // set up the EditText behavior
        final EditText input = dialog.findViewById((R.id.input));
        input.setText(((TextView)view).getText());

        // set up the Create button behavior
        Button b_Save = dialog.findViewById(R.id.Save);
        b_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // assign our input
                ((TextView)view).setText(input.getText());

                // update our character name
                character.name = input.getText().toString();

                // get our current file
                File currentFile = new File (characterDir, character.fileName);

                // change our file name
                character.fileName = fileName = input.getText().toString() + ".txt";

                // create our new file
                File newFile = new File (characterDir, character.fileName);

                // rename
                currentFile.renameTo(newFile);

                // close the screen
                dialog.dismiss();
            }
        });

        // set up the Cancel button
        Button b_Cancel = dialog.findViewById(R.id.Cancel);
        b_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // close the screen
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void editableButton(final View view) {
        // create our popup dialog
        final Dialog dialog = new Dialog(context);

        // have the keyboard show up once we have the ability
        //  https://stackoverflow.com/questions/4258623/show-soft-keyboard-for-dialog
        Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        // remove the title bar MUST BE CALLED BEFORE SETTING THE CONTENT VIEW!!
        // https://stackoverflow.com/questions/2644134/android-how-to-create-a-dialog-without-a-title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.editable);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);

        // set up the EditText behavior
        final EditText input = dialog.findViewById((R.id.input));

        final String[] parsed = ((Button)view).getText().toString().split("\n");
        input.setText(parsed[1]);

        // set up the Create button behavior
        Button b_Save = dialog.findViewById(R.id.Save);
        b_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // assign our input
                String results = parsed[0] + "\n" + input.getText() + "\n" + parsed[2];
                ((Button)view).setText(results);

                // close the screen
                dialog.dismiss();
            }
        });

        // set up the Cancel button
        Button b_Cancel = dialog.findViewById(R.id.Cancel);
        b_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // close the screen
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    protected void onPause() {
        super.onPause();
        writeFile(this.character.fileName);
    }

    protected void onResume() {
        super.onResume();
    }

    public void homeClick(View view) {
        this.finish();
    }

}
