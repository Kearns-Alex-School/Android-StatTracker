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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This is the Character details page.
 * @author Alex Kearns, Kevin Marsh
 */
public class CharacterDetailsActivity extends AppCompatActivity {
    // used to help us call methods here with current variables from other places
    private static CharacterDetailsActivity instance;

    SharedPreferences SP;

    public Context context;
    public Character character;
    public File characterDir;

    public int ccGravity = Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL;
    public int tcGravity = Gravity.TOP|Gravity.CENTER_HORIZONTAL;

    String fileName;
    String currentMenu;

    @Override
    /**
     * When the activity starts, this function is called.
     * Expects that the main activity has placed two File objects and one String into the Intent Extras.
     * @see MainActivity#LoadCharacter(String)
     * @author Alex Kearns, Kevin Marsh
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_details);

        context = CharacterDetailsActivity.this;
        instance = this;

        SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        // get from intent
        characterDir = (File) Objects.requireNonNull(getIntent().getExtras()).get("charDir");
        fileName = (String) getIntent().getExtras().get("filename");
        currentMenu = Objects.requireNonNull(SP.getString(MainActivity.LOAD_CONTENT, "subStats"));

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

        // load our content list area
        updateContentList();
    }

    /**
     * Updates the sidebar stat list, and setup the top row of buttons
     * @param sidebar LinearLayout Object - The layout element for the sidebar
     * @author Alex Kearns
     */
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
        temp = "Health\n" + character.HPCurrent + "/" + character.HPMax;
        button.setText(temp);

        // update Armor Rating
        button = findViewById(R.id.Armor);
        temp = "Armor Rating\n" + character.ArmrRating;
        button.setText(temp);

        // update Speed button
        button = findViewById(R.id.Speed);
        temp = character.speed + "\n" + SP.getString(MainActivity.DIST_UNIT, "ft/rd");
        button.setText(temp);

        // update Context List Menu
        String temp2;
        switch (currentMenu)
        {
            case "subStats":
                currentMenu = "subStats";
                temp = "Inventory";
                temp2 = "Abilities";
                break;
            case "inventory":
                currentMenu = "inventory";
                temp = "Sub-Stats";
                temp2 = "Abilities";
                break;
            case "abilities":
                currentMenu = "abilities";
                temp = "Sub-Stats";
                temp2 = "Inventory";
                break;
            default:
                currentMenu = "subStats";
                temp = "Inventory";
                temp2 = "Abilities";
                break;
        }
        button = findViewById(R.id.ContextMenu1);
        button.setText(temp);

        button = findViewById(R.id.ContextMenu2);
        button.setText(temp2);
    }

    public void updateContentList() {
        ListView listView = findViewById(R.id.Content_Menu);

        switch (currentMenu)
        {
            case "subStats":
                final ArrayList<SubStatDataModel> subStatModels = new ArrayList<>();

                SubStatAdapter subStatAdapter = new SubStatAdapter(subStatModels, context);

                for (Item item: character.subStats) {
                    // create new model
                    SubStatDataModel newSubStat = new SubStatDataModel();

                    // add data to the model
                    newSubStat.setBonus(item.statBonus.bonus);
                    newSubStat.setName(item.name);
                    newSubStat.setStatName("(" + item.statBonus.name + ")");
                    newSubStat.setCharacter(character);

                    subStatModels.add(newSubStat);
                }

                listView.setAdapter(subStatAdapter);

                //https://stackoverflow.com/questions/6703390/listview-setonitemclicklistener-not-working-by-adding-button
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Get the selected item text from ListView
                        SubStatDataModel dataModel= subStatModels.get(position);

                        editSubStat(dataModel, false);
                    }
                });
                break;
            case "inventory":
                final ArrayList<InventoryDataModel> inventoryModels = new ArrayList<>();

                InventoryAdapter inventoryAdapter = new InventoryAdapter(inventoryModels, context);

                for (Item item: character.inventory)
                {
                    // create new model
                    InventoryDataModel newInventory = new InventoryDataModel();

                    // add data to the model
                    newInventory.setName(item.name);
                    newInventory.setDMG(item.DMG);
                    newInventory.setAMR(item.AMR);
                    newInventory.setStatBonus(item.statBonus);
                    newInventory.setBonus1(item.bonus1);
                    newInventory.setBonus2(item.bonus2);
                    newInventory.setNotes(item.notes);
                    newInventory.setCharacter(character);

                    inventoryModels.add(newInventory);
                }

                listView.setAdapter(inventoryAdapter);


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Get the selected item text from ListView
                        InventoryDataModel dataModel= inventoryModels.get(position);

                        editInventory(dataModel, false);
                    }
                });
                break;
            case "abilities":
                final ArrayList<InventoryDataModel> abilitiesModels = new ArrayList<>();

                InventoryAdapter abilitiesAdapter = new InventoryAdapter(abilitiesModels, context);

                for (Item item: character.abilities)
                {
                    // create new model
                    InventoryDataModel newAbilities = new InventoryDataModel();

                    // add data to the model
                    newAbilities.setName(item.name);
                    newAbilities.setDMG(item.DMG);
                    newAbilities.setAMR(item.AMR);
                    newAbilities.setStatBonus(item.statBonus);
                    newAbilities.setBonus1(item.bonus1);
                    newAbilities.setBonus2(item.bonus2);
                    newAbilities.setNotes(item.notes);
                    newAbilities.setCharacter(character);

                    abilitiesModels.add(newAbilities);
                }

                listView.setAdapter(abilitiesAdapter);


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Get the selected item text from ListView
                        InventoryDataModel dataModel= abilitiesModels.get(position);

                        editAbilities(dataModel, false);
                    }
                });
                break;
        }
    }

    /**
     * Dialog to create a new stat and add the new stat to the character's stat list.
     * @param v View object - passed in when a button is pressed.
     * @author Alex Kearns
     */
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
                writeFile(character.fileName, true);

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

    /**
     * Reads a JSON serialized character file, to load a saved character. If the file is blank,
     * a new character is created.
     * @param filename A String containing the name of the file. Must include the file extension.
     * @return Character.class object.
     * @author Kevin Marsh
     */
    public Character readFile(String filename) {
        // KM: not doing async right now since files are small.
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

        System.out.println(contentJson);

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

    /**
     * Writes the current instance of Character.class to a serialized JSON file.
     * @param filename A String containing the name of the file to be written. Must include the
     *                 file extension.
     * @author Kevin Marsh
     */
    public void writeFile(String filename, boolean update) {
        // KM: not doing async because of small files
        Gson gson = new Gson();
        String json = gson.toJson(character);

        File outputFile = new File(characterDir, filename);

        try {
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            outputStream.write(json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        CommonMethods.showCenterTopToast(context,"Saved Character", 0);

        // re-populate our side bar of stats
        if (update)
        {
            UpdateStatList((LinearLayout) findViewById(R.id.contentLLV));

            updateContentList();
        }
    }

    public void contentClicked(View view) {
        // switch the strings around
        String newCurrentMenu = "";
        String newButtonText = "";
        Button button = (Button) view;

        switch (button.getText().toString())
        {
            case "Sub-Stats":
                newCurrentMenu = "subStats";
                break;
            case "Inventory":
                newCurrentMenu = "inventory";
                break;
            case "Abilities":
                newCurrentMenu = "abilities";
                break;
        }

        switch (currentMenu)
        {
            case "subStats":
                newButtonText = "Sub-Stats";
                break;
            case "inventory":
                newButtonText = "Inventory";
                break;
            case "abilities":
                newButtonText = "Abilities";
                break;
        }

        // update the current and the button
        currentMenu = newCurrentMenu;
        button.setText(newButtonText);

        // update the content list with what is in the currentMenu variable
        updateContentList();
    }

    /**
     * Allows the character's name to be edited. Doing so also changes the filename.
     * @param view View object. Passed in from click event.
     * @author Alex Kearns
     */
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
        final EditText noteInput = dialog.findViewById((R.id.notesValue));
        input.setText(character.name);
        noteInput.setText(character.notes);

        // set up the Create button behavior
        Button b_Save = dialog.findViewById(R.id.Save);
        b_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create the variables that we need
                String newName = input.getText().toString();
                String newNotes = noteInput.getText().toString();
                String newFileName = newName + ".txt";

                // check to see if the file exists
                File file = new File(characterDir, newFileName);
                if(file.exists())
                {
                    // if it does, then check to see that we are not the same
                    Character otherFile = readFile(file.getName());

                    if(!otherFile.name.equals(character.name))
                    {
                        CommonMethods.showCenterTopToast(context,"Character " + newName + " already exists", 0);
                        return;
                    }
                }

                // assign our input
                textview.setText(newName);

                // update our character name
                character.name = newName;

                // get our current file
                File currentFile = new File (characterDir, character.fileName);

                // change our file name
                character.fileName = fileName = newFileName;

                character.notes = newNotes;

                // create our new file
                File newFile = new File (characterDir, character.fileName);

                // rename
                if(!currentFile.renameTo(newFile))
                {
                    CommonMethods.showCenterTopToast(context,"Problem renaming file.", 0);
                    return;
                }

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

    /**
     * Allows a stat in the stat list to be edited. Shows a dialog to the user.
     * @param view View object. Passed in from click event.
     * @author Alex Kearns
     */
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

        final String[] parsed = ((Button)view).getText().toString().split("\n");
        final String stat = parsed[0];

        // find the stat index to change
        int statIndex = -1;
        for (int index = 0; index < character.statList.size(); index++)
            if (character.statList.get(index).name.equals(stat))
                statIndex = index;

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

                String oldName = character.statList.get(fStatIndex).name;

                // assign our input
                character.statList.get(fStatIndex).name = name;
                character.statList.get(fStatIndex).value = value;
                character.statList.get(fStatIndex).bonus = bonus;

                // update all subStats
                for (Item item :character.subStats)
                    if (item.statBonus.name.equals(oldName))
                        item.statBonus = character.statList.get(fStatIndex);

                // update all inventory items
                for (Item item :character.inventory)
                    if (item.statBonus.name.equals(oldName))
                        item.statBonus = character.statList.get(fStatIndex);

                // save our data and re-load
                writeFile(character.fileName, true);

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
                writeFile(character.fileName, true);

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

    /**
     * Allows the character's health to be edited. Shows a dialog to the user.
     * @param view View object. Passed in from click event.
     * @author Alex Kearns
     */
    public void editHealth(View view) {
        // create our popup dialog
        final Dialog dialog = new Dialog(context);

        // have the keyboard show up once we have the ability
        //  https://stackoverflow.com/questions/4258623/show-soft-keyboard-for-dialog
        Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        // remove the title bar MUST BE CALLED BEFORE SETTING THE CONTENT VIEW!!
        // https://stackoverflow.com/questions/2644134/android-how-to-create-a-dialog-without-a-title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.editable_health);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);

        // set up the EditText behavior
        final EditText currentInput = dialog.findViewById((R.id.Current));
        final EditText maxInput = dialog.findViewById((R.id.Max));

        // we are only expecting to have three different values in this exact order
        String current = Integer.toString(character.HPCurrent);
        String max = Integer.toString(character.HPMax);

        currentInput.setText(current);
        maxInput.setText(max);

        // set up the Save button behavior
        Button b_Save = dialog.findViewById(R.id.Save);
        b_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // make sure we have something in each field
                if(currentInput.getText().toString().length() == 0 ||
                   maxInput.getText().toString().length() == 0 )
                {
                    // Inform the user that values empty
                    CommonMethods.showCenterTopToast(context, "Please enter all values", 0);
                    return;
                }

                int current = Integer.parseInt(currentInput.getText().toString());
                int max = Integer.parseInt(maxInput.getText().toString());

                // assign our input
                character.HPCurrent = current;
                character.HPMax = max;

                // save our data and re-load
                writeFile(character.fileName, true);

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

    /**
     * Allows the character's armor to be edited. Shows a dialog to the user.
     * @param view View object. Passed in from click event.
     * @author Alex Kearns
     */
    public void editArmor(View view) {
        // create our popup dialog
        final Dialog dialog = new Dialog(context);

        // have the keyboard show up once we have the ability
        //  https://stackoverflow.com/questions/4258623/show-soft-keyboard-for-dialog
        Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        // remove the title bar MUST BE CALLED BEFORE SETTING THE CONTENT VIEW!!
        // https://stackoverflow.com/questions/2644134/android-how-to-create-a-dialog-without-a-title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.editable_armorrating);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);

        // set up the EditText behavior
        final EditText ratingInput = dialog.findViewById((R.id.Rating));

        // we are only expecting to have three different values in this exact order
        String current = Integer.toString(character.ArmrRating);

        ratingInput.setText(current);

        // set up the Save button behavior
        Button b_Save = dialog.findViewById(R.id.Save);
        b_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // make sure we have something in each field
                if(ratingInput.getText().toString().length() == 0 )
                {
                    // Inform the user that values empty
                    CommonMethods.showCenterTopToast(context, "Please enter all values", 0);
                    return;
                }

                // assign our input
                character.ArmrRating = Integer.parseInt(ratingInput.getText().toString());

                // save our data and re-load
                writeFile(character.fileName, true);

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

    /**
     * Allows the character's speed to be edited. Shows a dialog to the user.
     * @param view View object. Passed in from click event.
     * @author Alex Kearns
     */
    public void editSpeed(View view) {
        // create our popup dialog
        final Dialog dialog = new Dialog(context);

        // have the keyboard show up once we have the ability
        //  https://stackoverflow.com/questions/4258623/show-soft-keyboard-for-dialog
        Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        // remove the title bar MUST BE CALLED BEFORE SETTING THE CONTENT VIEW!!
        // https://stackoverflow.com/questions/2644134/android-how-to-create-a-dialog-without-a-title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.editable_speed);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);

        // set up the EditText behavior
        final EditText speedInput = dialog.findViewById((R.id.Speed));

        // we are only expecting to have three different values in this exact order
        String current = Integer.toString(character.speed);

        speedInput.setText(current);

        // set up the Save button behavior
        Button b_Save = dialog.findViewById(R.id.Save);
        b_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // make sure we have something in each field
                if(speedInput.getText().toString().length() == 0 )
                {
                    // Inform the user that values empty
                    CommonMethods.showCenterTopToast(context, "Please enter all values", 0);
                    return;
                }

                // assign our input
                character.speed = Integer.parseInt(speedInput.getText().toString());

                // save our data and re-load
                writeFile(character.fileName, true);

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

    /**
     * Handles the dice roll behavior. Shows a dialog to the user.
     * @param view View object. Passed in from click event.
     * @author Alex Kearns
     */
    public void diceRoll(View view) {
        // create our popup dialog
        final Dialog dialog = new Dialog(context);

        // have the keyboard show up once we have the ability
        //  https://stackoverflow.com/questions/4258623/show-soft-keyboard-for-dialog
        Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // remove the title bar MUST BE CALLED BEFORE SETTING THE CONTENT VIEW!!
        // https://stackoverflow.com/questions/2644134/android-how-to-create-a-dialog-without-a-title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dice_roller);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);

        // set up the EditText behavior
        final EditText numSides = dialog.findViewById((R.id.Sides));
        final EditText numRolls = dialog.findViewById((R.id.Rolls));

        // set up our drop down menu
        final Spinner statBonus = dialog.findViewById(R.id.Bonus);

        List<String> stats = new ArrayList<>();
        final HashMap<String, Integer> map = new HashMap<>();

        Stat none = new Stat();
        none.name = "None";
        none.bonus = 0;
        stats.add(none.name);

        map.put(none.name, none.bonus);

        for (Stat stat : character.statList) {
            stats.add(stat.name);
            map.put(stat.name, stat.bonus);
        }

        // set out dropdown to the list.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, stats);

        statBonus.setAdapter(adapter);

        // replace with saved preferences
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
                    // inform the user that values empty
                    CommonMethods.showCenterTopToast(context, "Please enter all values", 0);
                    return;
                }

                int rolls = Integer.parseInt(numRolls.getText().toString());
                int sides = Integer.parseInt(numSides.getText().toString());

                Die die = new Die(sides);

                TextView results = dialog.findViewById(R.id.Results);
                String bonusStat = statBonus.getSelectedItem().toString();

                // roll our die
                results.append("Results of " + rolls + " rolls with " + sides + " sides  with bonus '" + bonusStat + "':\n");

                for (int index = 1; index <= rolls; index++) {
                    int bonus = map.get(bonusStat);
                    int roll = die.roll();
                    int total = bonus + roll;

                    results.append("Roll " + index + ": " + roll + " + (" + bonus + ") = " + total + "\n");
                }

                results.append("\n");

                if(SP.getBoolean(MainActivity.DICE_AUTOSCROLL, false))
                {
                    final ScrollView scroll = dialog.findViewById((R.id.Scoller));

                    // just use this line if you want to stay at the top of the roll
                    scroll.fullScroll(View.FOCUS_DOWN);
                }
            }
        });

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

        // check to see if we are going to auto roll or not
        if (SP.getBoolean(MainActivity.DICE_ROLLONLOAD, false))
            b_Roll.callOnClick();

        dialog.show();
    }

    public void addItem(View view) {
        switch (currentMenu)
        {
            case "subStats":
                editSubStat(new SubStatDataModel(), true);
                break;
            case "inventory":
                editInventory(new InventoryDataModel(), true);
                break;
            case "abilities":
                editAbilities(new InventoryDataModel(), true);
                break;
        }
    }

    public void editSubStat(SubStatDataModel data, final Boolean isNew) {
        // create our popup dialog
        final Dialog dialog = new Dialog(context);

        // have the keyboard show up once we have the ability
        //  https://stackoverflow.com/questions/4258623/show-soft-keyboard-for-dialog
        Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        // remove the title bar MUST BE CALLED BEFORE SETTING THE CONTENT VIEW!!
        // https://stackoverflow.com/questions/2644134/android-how-to-create-a-dialog-without-a-title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.editable_substat);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);

        // set up the EditText behavior
        final EditText nameInput = dialog.findViewById((R.id.NameValue));

        // set up our drop down menu
        final Spinner stats = dialog.findViewById(R.id.Stat);
        int statIndex = 0;

        List<String> list = new ArrayList<>();

        // grab all of the stats in the character
        for (int index = 0; index < character.statList.size(); index++) {
            if (character.statList.get(index).name.equals(data.getStatName()))
                statIndex = index;
            list.add(character.statList.get(index).name);
        }

        // set out dropdown to the list.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, list);
        stats.setAdapter(adapter);
        stats.setSelection(statIndex);

        // find the current stat index to change
        int subStatIndex = -1;

        for (int index = 0; index < character.subStats.size(); index++)
            if (character.subStats.get(index).name.equals(data.getName()))
                subStatIndex = index;

        final int fSubStatIndex = subStatIndex;

        nameInput.setText(data.getName());

        // set up the Save button behavior
        Button b_Save = dialog.findViewById(R.id.Save);
        b_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // make sure we have something in each field
                if(nameInput.getText().toString().length() == 0)
                {
                    // Inform the user that values empty
                    CommonMethods.showCenterTopToast(context, "Please enter all values", 0);
                    return;
                }

                String name = nameInput.getText().toString();

                // check to see that we do not clash with any other names
                for (int index = 0; index < character.subStats.size(); index++)
                {
                    // make sure the index is also different (same name as current index is acceptable)
                    if (character.subStats.get(index).name.equals(name) && index != fSubStatIndex)
                    {
                        // Inform the user that the name is empty
                        CommonMethods.showCenterTopToast(context, name + " already exist.", 0);
                        return;
                    }
                }

                // see if this is a new stat or an old one
                if(isNew)
                {
                    Item newSubStat = new Item();
                    newSubStat.name = name;
                    newSubStat.statBonus = character.statList.get(stats.getSelectedItemPosition());

                    character.subStats.add(newSubStat);
                } else {
                    character.subStats.get(fSubStatIndex).name = name;
                    character.subStats.get(fSubStatIndex).statBonus = character.statList.get(stats.getSelectedItemPosition());
                }

                // save our data and re-load
                writeFile(character.fileName, true);

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

    public void editInventory(InventoryDataModel data, final Boolean isNew) {
        // create our popup dialog
        final Dialog dialog = new Dialog(context);

        // have the keyboard show up once we have the ability
        //  https://stackoverflow.com/questions/4258623/show-soft-keyboard-for-dialog
        Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        // remove the title bar MUST BE CALLED BEFORE SETTING THE CONTENT VIEW!!
        // https://stackoverflow.com/questions/2644134/android-how-to-create-a-dialog-without-a-title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.editable_inventory);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);

        // set up the EditText behavior
        final EditText nameInput = dialog.findViewById((R.id.NameValue));
        final EditText dmgInput = dialog.findViewById((R.id.DMGValue));
        final EditText amrInput = dialog.findViewById((R.id.AMRValue));
        final EditText bns1Input = dialog.findViewById((R.id.BNS1Value));
        final EditText bns2Input = dialog.findViewById((R.id.BNS2));
        final EditText noteInput = dialog.findViewById((R.id.NotesValue));

        // set up our drop down menu
        final Spinner stats = dialog.findViewById(R.id.StatValues);
        int statIndex = 0;

        List<String> list = new ArrayList<>();

        // grab all of the stats in the character
        for (int index = 0; index < character.statList.size(); index++) {
            if (character.statList.get(index).name.equals(data.getStatBonus().name))
                statIndex = index;
            list.add(character.statList.get(index).name);
        }

        // set out dropdown to the list.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, list);
        stats.setAdapter(adapter);
        stats.setSelection(statIndex);

        // find the current stat index to change
        int inventoryIndex = -1;

        for (int index = 0; index < character.inventory.size(); index++)
            if (character.inventory.get(index).name.equals(data.getName()))
                inventoryIndex = index;

        final int fInventoryIndex = inventoryIndex;

        nameInput.setText(data.getName());

        String temp = Integer.toString(data.getDMG());
        dmgInput.setText(temp);

        temp = Integer.toString(data.getAMR());
        amrInput.setText(temp);

        temp = Integer.toString(data.getBonus1());
        bns1Input.setText(temp);

        temp = Integer.toString(data.getBonus2());
        bns2Input.setText(temp);
        noteInput.setText(data.getNotes());

        // set up the Save button behavior
        Button b_Save = dialog.findViewById(R.id.Save);
        b_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // make sure we have something in each field
                if(nameInput.getText().toString().length() == 0 ||
                   dmgInput.getText().toString().length() == 0 ||
                   amrInput.getText().toString().length() == 0 ||
                   bns1Input.getText().toString().length() == 0 ||
                   bns2Input.getText().toString().length() == 0)
                {
                    // Inform the user that values empty
                    CommonMethods.showCenterTopToast(context, "Please enter all values (notes are optional)", 0);
                    return;
                }

                String name = nameInput.getText().toString();
                int dmg = Integer.parseInt(dmgInput.getText().toString());
                int amr = Integer.parseInt(amrInput.getText().toString());
                int bns1 = Integer.parseInt(bns1Input.getText().toString());
                int bns2 = Integer.parseInt(bns2Input.getText().toString());
                String note = noteInput.getText().toString();

                // check to see that we do not clash with any other names
                for (int index = 0; index < character.inventory.size(); index++)
                {
                    // make sure the index is also different (same name as current index is acceptable)
                    if (character.inventory.get(index).name.equals(name) && index != fInventoryIndex)
                    {
                        // Inform the user that the name is empty
                        CommonMethods.showCenterTopToast(context, name + " already exist.", 0);
                        return;
                    }
                }

                // see if this is a new stat or an old one
                if(isNew)
                {
                    Item newInventory = new Item();
                    newInventory.name = name;
                    newInventory.DMG = dmg;
                    newInventory.AMR = amr;
                    newInventory.bonus1 = bns1;
                    newInventory.bonus2 = bns2;
                    newInventory.notes = note;

                    newInventory.statBonus = character.statList.get(stats.getSelectedItemPosition());

                    character.inventory.add(newInventory);
                } else {
                    character.inventory.get(fInventoryIndex).name = name;
                    character.inventory.get(fInventoryIndex).DMG = dmg;
                    character.inventory.get(fInventoryIndex).AMR = amr;
                    character.inventory.get(fInventoryIndex).bonus1 = bns1;
                    character.inventory.get(fInventoryIndex).bonus2 = bns2;
                    character.inventory.get(fInventoryIndex).notes = note;
                    character.inventory.get(fInventoryIndex).statBonus = character.statList.get(stats.getSelectedItemPosition());
                }

                // save our data and re-load
                writeFile(character.fileName, true);

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

    public void editAbilities(InventoryDataModel data, final Boolean isNew) {
        // create our popup dialog
        final Dialog dialog = new Dialog(context);

        // have the keyboard show up once we have the ability
        //  https://stackoverflow.com/questions/4258623/show-soft-keyboard-for-dialog
        Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        // remove the title bar MUST BE CALLED BEFORE SETTING THE CONTENT VIEW!!
        // https://stackoverflow.com/questions/2644134/android-how-to-create-a-dialog-without-a-title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.editable_inventory);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);

        // set up the EditText behavior
        final EditText nameInput = dialog.findViewById((R.id.NameValue));
        final EditText dmgInput = dialog.findViewById((R.id.DMGValue));
        final EditText amrInput = dialog.findViewById((R.id.AMRValue));
        final EditText bns1Input = dialog.findViewById((R.id.BNS1Value));
        final EditText bns2Input = dialog.findViewById((R.id.BNS2));
        final EditText noteInput = dialog.findViewById((R.id.NotesValue));

        // set up our drop down menu
        final Spinner stats = dialog.findViewById(R.id.StatValues);
        int statIndex = 0;

        List<String> list = new ArrayList<>();

        // grab all of the stats in the character
        for (int index = 0; index < character.statList.size(); index++) {
            if (character.statList.get(index).name.equals(data.getStatBonus().name))
                statIndex = index;
            list.add(character.statList.get(index).name);
        }

        // set out dropdown to the list.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, list);
        stats.setAdapter(adapter);
        stats.setSelection(statIndex);

        // find the current stat index to change
        int abilitiesIndex = -1;

        for (int index = 0; index < character.abilities.size(); index++)
            if (character.abilities.get(index).name.equals(data.getName()))
                abilitiesIndex = index;

        final int fAbilitiesIndex = abilitiesIndex;

        nameInput.setText(data.getName());

        String temp = Integer.toString(data.getDMG());
        dmgInput.setText(temp);

        temp = Integer.toString(data.getAMR());
        amrInput.setText(temp);

        temp = Integer.toString(data.getBonus1());
        bns1Input.setText(temp);

        temp = Integer.toString(data.getBonus2());
        bns2Input.setText(temp);
        noteInput.setText(data.getNotes());

        // set up the Save button behavior
        Button b_Save = dialog.findViewById(R.id.Save);
        b_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // make sure we have something in each field
                if(nameInput.getText().toString().length() == 0 ||
                        dmgInput.getText().toString().length() == 0 ||
                        amrInput.getText().toString().length() == 0 ||
                        bns1Input.getText().toString().length() == 0 ||
                        bns2Input.getText().toString().length() == 0)
                {
                    // Inform the user that values empty
                    CommonMethods.showCenterTopToast(context, "Please enter all values (notes are optional)", 0);
                    return;
                }

                String name = nameInput.getText().toString();
                int dmg = Integer.parseInt(dmgInput.getText().toString());
                int amr = Integer.parseInt(amrInput.getText().toString());
                int bns1 = Integer.parseInt(bns1Input.getText().toString());
                int bns2 = Integer.parseInt(bns2Input.getText().toString());
                String note = noteInput.getText().toString();

                // check to see that we do not clash with any other names
                for (int index = 0; index < character.abilities.size(); index++)
                {
                    // make sure the index is also different (same name as current index is acceptable)
                    if (character.abilities.get(index).name.equals(name) && index != fAbilitiesIndex)
                    {
                        // Inform the user that the name is empty
                        CommonMethods.showCenterTopToast(context, name + " already exist.", 0);
                        return;
                    }
                }

                // see if this is a new stat or an old one
                if(isNew)
                {
                    Item newAbilities = new Item();
                    newAbilities.name = name;
                    newAbilities.DMG = dmg;
                    newAbilities.AMR = amr;
                    newAbilities.bonus1 = bns1;
                    newAbilities.bonus2 = bns2;
                    newAbilities.notes = note;

                    newAbilities.statBonus = character.statList.get(stats.getSelectedItemPosition());

                    character.abilities.add(newAbilities);
                } else {
                    character.abilities.get(fAbilitiesIndex).name = name;
                    character.abilities.get(fAbilitiesIndex).DMG = dmg;
                    character.abilities.get(fAbilitiesIndex).AMR = amr;
                    character.abilities.get(fAbilitiesIndex).bonus1 = bns1;
                    character.abilities.get(fAbilitiesIndex).bonus2 = bns2;
                    character.abilities.get(fAbilitiesIndex).notes = note;
                    character.abilities.get(fAbilitiesIndex).statBonus = character.statList.get(stats.getSelectedItemPosition());
                }

                // save our data and re-load
                writeFile(character.fileName, true);

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
        writeFile(character.fileName, false);
    }

    protected void onResume() {
        super.onResume();
    }

    /**
     * Return to the previous activity when clicked
     * @param view View object. Passed in from click event.
     * @author Alex Kearns
     */
    public void homeClick(View view) {
        this.finish();
    }

    //https://stackoverflow.com/questions/17315842/how-to-call-a-method-in-mainactivity-from-another-class/25260829
    public static CharacterDetailsActivity getInstance() {
        return instance;
    }

}
