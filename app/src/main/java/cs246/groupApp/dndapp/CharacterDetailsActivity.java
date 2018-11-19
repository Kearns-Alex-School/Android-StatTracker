package cs246.groupApp.dndapp;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class CharacterDetailsActivity extends AppCompatActivity {
    SharedPreferences SP;

    public Context context;
    public Character character;
    public File characterDir;

    public int ccGravity = Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL;
    public int tcGravity = Gravity.TOP|Gravity.CENTER_HORIZONTAL;

    String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_details);

        context = CharacterDetailsActivity.this;

        SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        // get from intent
        characterDir = (File) Objects.requireNonNull(getIntent().getExtras()).get("dir");
        fileName = (String) getIntent().getExtras().get("filename");

        // read file
        this.character = readFile(fileName);

        // if there is a name, set it on the screen
        TextView titleText = findViewById(R.id.title);

        // add the character's name to the file if there was none
        if (character.name.equals(""))
            character.name = fileName.replace(".txt", "");

        // set the title to the character's name
        titleText.setText(character.name);

        // Set some constraints for Dice button
        Button button = findViewById(R.id.Dice);
        button.setPadding(0,button.getPaddingTop(),0,button.getPaddingBottom());
        button.setLineSpacing(-5,1);
        button.setGravity(tcGravity);

        // Set some constraints for Health button
        button = findViewById(R.id.Health);
        button.setPadding(0,button.getPaddingTop(),0,button.getPaddingBottom());
        button.setLineSpacing(-5,1);
        button.setGravity(tcGravity);

        // Set some constraints for Armor Rating button
        button = findViewById(R.id.Armor);
        button.setPadding(0,button.getPaddingTop(),0,button.getPaddingBottom());
        button.setLineSpacing(-5,1);
        button.setGravity(tcGravity);

        // Set some constraints for Speed button
        button = findViewById(R.id.Speed);
        button.setPadding(0,button.getPaddingTop(),0,button.getPaddingBottom());
        button.setLineSpacing(-5,1);
        button.setGravity(tcGravity);

        // populate our layout stats
        UpdateStatList((LinearLayout) findViewById(R.id.contentLLV));
    }

    public void UpdateStatList(final LinearLayout sidebar) {
        // remove any of the views that we currently are holding
        if (0 < sidebar.getChildCount())
            sidebar.removeAllViews();

        // dynamically add to our scrollView
        //  https://stackoverflow.com/questions/14920535/how-to-add-more-than-one-button-to-scrollview-in-android
        for (int index = 0; index < character.statList.size(); index++)
        {
            String temp = character.statList.get(index).name + "\n" +
                          character.statList.get(index).value + "\n" +
                          character.statList.get(index).bonus;

            // Add Button
            // AJK: Currently ~9/~7 lower/upper case letters are the longest before a newline
            Button button = new Button(context);
            button.setPadding(0,0,0,button.getPaddingBottom());
            button.setLineSpacing(-5,1);
            button.setTextSize(15);
            button.setGravity(ccGravity);
            button.setText(temp);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    editStat(v);
                }
            });
            sidebar.addView(button);
        }

        // finally create the button that will add a new stat to the list
        String temp = "Add Stat";
        Button button = new Button(context);
        button.setPadding(0,0,0,button.getPaddingBottom());
        button.setLineSpacing(-5,1);
        button.setGravity(ccGravity);
        button.setText(temp);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createStat(v);
            }
        });
        sidebar.addView(button);


        // update Health button
        button = findViewById(R.id.Health);
        temp = "Health  " + character.HPCurrent + "/" + character.HPMax;
        button.setText(temp);

        // update Armor Rating
        button = findViewById(R.id.Armor);
        temp = "Armor Rating\n" + character.ArmrRating;
        button.setText(temp);

        // update Speed button
        button = findViewById(R.id.Speed);
        temp = character.speed + " " + SP.getString(MainActivity.DIST_UNIT, "ft/rd");
        button.setText(temp);

        // TODO: 2018-11-17 AJK: Need to add background code to figure out which we are displaying first
        // update Context List Menu
        String temp2;
        switch (Objects.requireNonNull(SP.getString(MainActivity.LOAD_CONTENT, "subStats")))
        {
            case "subStats":
                temp = "Inventory";
                temp2 = "Magic";
                break;
            case "inventory":
                temp = "Sub-Stats";
                temp2 = "Magic";
                break;
            case "magic":
                temp = "Sub-Stats";
                temp2 = "Inventory";
                break;
            default:
                temp = "Inventory";
                temp2 = "Magic";
                break;
        }
        button = findViewById(R.id.ContextMenu1);
        button.setText(temp);

        button = findViewById(R.id.ContextMenu2);
        button.setText(temp2);
    }

    public void createStat(View v) {
        /// create our popup dialog
        final Dialog dialog = new Dialog(context);

        // have the keyboard show up once we have the ability
        //  https://stackoverflow.com/questions/4258623/show-soft-keyboard-for-dialog
        Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        // remove the title bar MUST BE CALLED BEFORE SETTING THE CONTENT VIEW!!
        // https://stackoverflow.com/questions/2644134/android-how-to-create-a-dialog-without-a-title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.editable_stat);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);

        // set up the EditText behavior
        final EditText nameInput = dialog.findViewById((R.id.StatNameValue));
        final EditText valueInput = dialog.findViewById((R.id.StatValue));
        final EditText bonusInput = dialog.findViewById((R.id.StatBonusValue));

        // set up the Save button behavior
        Button b_Save = dialog.findViewById(R.id.Save);
        b_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // make sure we have something in each field
                if(nameInput.getText().toString().length() == 0 ||
                   valueInput.getText().toString().length() == 0 ||
                   bonusInput.getText().toString().length() == 0)
                {
                    // Inform the user that values empty
                    CommonMethods.showCenterTopToast(context, "Please enter all values", 0);
                    return;
                }

                String name;
                int value;
                int bonus;

                // make sure we have valid values (checking for any letters or decimals in our ints)
                try {
                    name = nameInput.getText().toString();
                    value = Integer.parseInt(valueInput.getText().toString());
                    bonus = Integer.parseInt(bonusInput.getText().toString());
                } catch (Exception e) {
                    // Inform the user that the name is empty
                    CommonMethods.showCenterTopToast(context, "Please enter valid values", 0);
                    return;
                }

                // see if we already have a stat with this name
                for (int index = 0; index < character.statList.size(); index++)
                {
                    if (character.statList.get(index).name.equals(name))
                    {
                        // Inform the user that the name is empty
                        CommonMethods.showCenterTopToast(context, name + " already exist.", 0);
                        return;
                    }
                }

                // create out new stat
                Stat newStat = new Stat();
                newStat.name = name;
                newStat.value = value;
                newStat.bonus = bonus;

                // add the new stat to the character
                character.statList.add(newStat);

                // save our data and re-load
                writeFile(character.name + ".txt");

                // close the screen
                dialog.dismiss();
            }
        });

        // we want to remove this button since it is no use to us
        Button b_Remove = dialog.findViewById(R.id.Remove);
        b_Remove.setVisibility(View.GONE);

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

        CommonMethods.showCenterTopToast(this,"Loaded Character", 0);

        return character;
    }

    // KM: not doing async because of small files
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

        CommonMethods.showCenterTopToast(this,"Saved Character", 0);

        // re-populate our side bar of stats
        UpdateStatList((LinearLayout) findViewById(R.id.contentLLV));
    }

    public void editName(View view) {
        // create our popup dialog
        final Dialog dialog = new Dialog(context);
        final TextView textview = (TextView) view;

        // have the keyboard show up once we have the ability
        //  https://stackoverflow.com/questions/4258623/show-soft-keyboard-for-dialog
        Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        // remove the title bar MUST BE CALLED BEFORE SETTING THE CONTENT VIEW!!
        // https://stackoverflow.com/questions/2644134/android-how-to-create-a-dialog-without-a-title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.editable_title);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);

        // set up the EditText behavior
        final EditText input = dialog.findViewById((R.id.input));
        input.setText(textview.getText());

        // set up the Create button behavior
        Button b_Save = dialog.findViewById(R.id.Save);
        b_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // assign our input
                textview.setText(input.getText());

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

    public void editStat(View view) {
        // create our popup dialog
        final Dialog dialog = new Dialog(context);

        // have the keyboard show up once we have the ability
        //  https://stackoverflow.com/questions/4258623/show-soft-keyboard-for-dialog
        Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        // remove the title bar MUST BE CALLED BEFORE SETTING THE CONTENT VIEW!!
        // https://stackoverflow.com/questions/2644134/android-how-to-create-a-dialog-without-a-title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.editable_stat);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);

        // set up the EditText behavior
        final EditText nameInput = dialog.findViewById((R.id.StatNameValue));
        final EditText valueInput = dialog.findViewById((R.id.StatValue));
        final EditText bonusInput = dialog.findViewById((R.id.StatBonusValue));

        final String[] parsed = ((Button)view).getText().toString().split(" |\n");
        final String stat = parsed[0];

        // find the stat index to change
        int statIndex = -1;
        for (int index = 0; index < character.statList.size(); index++)
        {
            if (character.statList.get(index).name.equals(stat))
            {
                statIndex = index;
            }
        }

        // check to see if we did not find the index
        if(statIndex == -1)
        {
            // Inform the user that the stat does not exist (this 'should' never happen)
            CommonMethods.showCenterTopToast(context, stat + " does not exist.", 0);
            return;
        }

        // create a final copy that we can pass into the button methods
        final int fStatIndex = statIndex;

        // we are only expecting to have three different values in this exact order
        String name = character.statList.get(statIndex).name;
        String value = Integer.toString(character.statList.get(statIndex).value);
        String bonus = Integer.toString(character.statList.get(statIndex).bonus);

        nameInput.setText(name);
        valueInput.setText(value);
        bonusInput.setText(bonus);

        // set up the Save button behavior
        Button b_Save = dialog.findViewById(R.id.Save);
        b_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // make sure we have something in each field
                if(nameInput.getText().toString().length() == 0 ||
                        valueInput.getText().toString().length() == 0 ||
                        bonusInput.getText().toString().length() == 0)
                {
                    // Inform the user that values empty
                    CommonMethods.showCenterTopToast(context, "Please enter all values", 0);
                    return;
                }

                String name;
                int value;
                int bonus;

                // make sure we have valid values (checking for any letters or decimals in our ints)
                try {
                    name = nameInput.getText().toString();
                    value = Integer.parseInt(valueInput.getText().toString());
                    bonus = Integer.parseInt(bonusInput.getText().toString());
                } catch (Exception e) {
                    // Inform the user that the name is empty
                    CommonMethods.showCenterTopToast(context, "Please enter valid values", 0);
                    return;
                }

                // check to see that we do not clash with any other names
                for (int index = 0; index < character.statList.size(); index++)
                {
                    // make sure the index is also different (same name as current index is acceptable)
                    if (character.statList.get(index).name.equals(name) && index != fStatIndex)
                    {
                        // Inform the user that the name is empty
                        CommonMethods.showCenterTopToast(context, name + " already exist.", 0);
                        return;
                    }
                }

                // assign our input
                character.statList.get(fStatIndex).name = name;
                character.statList.get(fStatIndex).value = value;
                character.statList.get(fStatIndex).bonus = bonus;

                // save our data and re-load
                writeFile(character.fileName);

                // close the screen
                dialog.dismiss();
            }
        });

        // set up the Remove button behavior
        Button b_Remove = dialog.findViewById(R.id.Remove);
        b_Remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // remove our stat
                character.statList.remove(fStatIndex);

                // save our data and re-load
                writeFile(character.fileName);

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

    public void editHealth(View view) {

    }

    public void editArmor(View view) {

    }

    public void editSpeed(View view) {

    }

    public void diceRoll(View view) {
        // create our popup dialog
        final Dialog dialog = new Dialog(context);

        // have the keyboard show up once we have the ability
        //  https://stackoverflow.com/questions/4258623/show-soft-keyboard-for-dialog
        Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        // remove the title bar MUST BE CALLED BEFORE SETTING THE CONTENT VIEW!!
        // https://stackoverflow.com/questions/2644134/android-how-to-create-a-dialog-without-a-title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dice_roller);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);

        // set up the EditText behavior
        final EditText numSides = dialog.findViewById((R.id.Sides));
        final EditText numRolls = dialog.findViewById((R.id.Rolls));

        // AJK: Will need to replace with saved preferences
        numSides.setText(SP.getString(MainActivity.DICE_SIDES, "6"));
        numRolls.setText(SP.getString(MainActivity.DICE_ROLLS, "1"));

        // set up the Save button behavior
        Button b_Roll = dialog.findViewById(R.id.Roll);
        b_Roll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // make sure we have something in each field
                if(numRolls.getText().toString().length() == 0 ||
                        numSides.getText().toString().length() == 0)
                {
                    // Inform the user that values empty
                    CommonMethods.showCenterTopToast(context, "Please enter all values", 0);
                    return;
                }

                int rolls = Integer.parseInt(numRolls.getText().toString());
                int sides = Integer.parseInt(numSides.getText().toString());

                Die die = new Die(sides);
                TextView results = dialog.findViewById(R.id.Results);

                // check to see that we do not clash with any other names
                for (int index = 1; index <= rolls; index++)
                {
                    results.append("Roll " + index + ": " + die.roll() + "\n");
                }

                results.append("\n");
            }
        });

        // check to see if we are going to auto roll or not
        if (SP.getBoolean(MainActivity.DICE_ROLLONLOAD, false))
            b_Roll.callOnClick();

        // set up the Cancel button
        Button b_Clear = dialog.findViewById(R.id.Clear);
        b_Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // close the screen
                TextView results = dialog.findViewById(R.id.Results);
                results.setText("");
            }
        });

        // set up the Cancel button
        Button b_Close = dialog.findViewById(R.id.Close);
        b_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // close the screen
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    
    public void rightClicked(View view) {
        updateContentList(view, true);
    }

    public void leftClicked(View view) {
        updateContentList(view, false);
    }
    
    public void updateContentList(View view, Boolean isRight) {
        // TODO: 2018-11-17 AJK: Handle switching the content list here 
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
