package cs246.groupApp.dndapp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

public class InventoryAdapter extends ArrayAdapter<InventoryDataModel> implements View.OnClickListener {

    private ArrayList<InventoryDataModel> dataSet;
    private Context context;

    private static class ViewHolder {
        TextView txtName;
        TextView txtDMG;
        TextView txtAMR;
        TextView txtBNS1;
        TextView txtBNS2;
        TextView txtNotes;
        Button bDelete;
    }

    public InventoryAdapter(ArrayList<InventoryDataModel> data, Context context) {
        super(context, R.layout.custom_layout_inventory, data);
        this.dataSet = data;
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        // get the position that was clicked
        int position = (Integer) v.getTag();

        // convert it into an object
        Object object = getItem(position);

        // cast the object into our model
        final InventoryDataModel dataModel = (InventoryDataModel) object;

        // figure out which one we clicked
        switch (v.getId()) {
            case R.id.Delete:
                // create our popup dialog
                final Dialog dialog = new Dialog(context);

                // have the keyboard show up once we have the ability
                //  https://stackoverflow.com/questions/4258623/show-soft-keyboard-for-dialog
                Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

                // remove the title bar MUST BE CALLED BEFORE SETTING THE CONTENT VIEW!!
                // https://stackoverflow.com/questions/2644134/android-how-to-create-a-dialog-without-a-title
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.confirm_delete);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(true);

                TextView title = dialog.findViewById(R.id.Message);
                String message = "Are you sure you want to delete \n'" + Objects.requireNonNull(dataModel).getName() + "'?";

                title.setText(message);

                // set up our confirm button
                Button b_Confirm = dialog.findViewById(R.id.Confirm);
                b_Confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Character character = dataModel.getCharacter();

                        for (int index = 0; index < character.inventory.size(); index++)
                            if (character.inventory.get(index).name.equals(dataModel.getName()))
                                character.inventory.remove(index);

                        dialog.dismiss();

                        // call our writeFile from the CharacterDetailsActivity
                        //https://stackoverflow.com/questions/17315842/how-to-call-a-method-in-mainactivity-from-another-class/25260829
                        CharacterDetailsActivity.getInstance().writeFile(character.fileName, true);
                    }
                });

                // set up our cancel button
                Button b_Cancel = dialog.findViewById(R.id.Cancel);
                b_Cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // close the screen
                        dialog.dismiss();
                    }
                });

                dialog.show();

                break;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        InventoryDataModel dataModel = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());

            convertView = inflater.inflate(R.layout.custom_layout_inventory, parent, false);
            viewHolder.txtName = convertView.findViewById(R.id.Name);
            viewHolder.txtDMG = convertView.findViewById(R.id.DMG);
            viewHolder.txtAMR = convertView.findViewById(R.id.AMR);
            viewHolder.txtBNS1 = convertView.findViewById(R.id.BNS1);
            viewHolder.txtBNS2 = convertView.findViewById(R.id.BNS2);
            viewHolder.txtNotes = convertView.findViewById(R.id.Notes);
            viewHolder.bDelete = convertView.findViewById(R.id.Delete);

            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        // go through all of the custom stuff

        viewHolder.txtName.setText(Objects.requireNonNull(dataModel).getName());

        // check to see if we are a "Add New" item
        if(dataModel.getAddNew()) {
            TextView temp = convertView.findViewById(R.id.txtDMG);
            temp.setVisibility(View.GONE);
            viewHolder.txtDMG.setVisibility(View.GONE);

            temp = convertView.findViewById(R.id.txtAMR);
            temp.setVisibility(View.GONE);
            viewHolder.txtAMR.setVisibility(View.GONE);

            temp = convertView.findViewById(R.id.txtBNS1);
            temp.setVisibility(View.GONE);
            viewHolder.txtBNS1.setVisibility(View.GONE);

            temp = convertView.findViewById(R.id.txtBNS2);
            temp.setVisibility(View.GONE);
            viewHolder.txtBNS2.setVisibility(View.GONE);
            viewHolder.txtNotes.setVisibility(View.GONE);
            viewHolder.bDelete.setVisibility(View.GONE);

            viewHolder.txtName.setTextSize(15);
            viewHolder.txtName.setTypeface(null, Typeface.NORMAL);
        } else {
            String temp = "" + dataModel.getDMG();
            viewHolder.txtDMG.setText(temp);

            temp = "" + dataModel.getAMR();
            viewHolder.txtAMR.setText(temp);

            temp = "" + dataModel.getBonus1();
            viewHolder.txtBNS1.setText(temp);

            temp = "" + dataModel.getBonus2();
            viewHolder.txtBNS2.setText(temp);

            temp = "(" + dataModel.getStatBonus().name + ")  " + dataModel.getStatBonus().bonus +
                          "\n" +
                          "\n" + dataModel.getNotes();

            viewHolder.txtNotes.setText(temp);

            viewHolder.bDelete.setOnClickListener(this);
            viewHolder.bDelete.setTag(position);
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
