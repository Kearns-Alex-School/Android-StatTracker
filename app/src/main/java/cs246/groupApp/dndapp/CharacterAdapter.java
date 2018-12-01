package cs246.groupApp.dndapp;

import android.app.Dialog;
import android.content.Context;
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

public class CharacterAdapter extends ArrayAdapter<CharacterDataModel> implements View.OnClickListener {
    private ArrayList<CharacterDataModel> dataSet;
    private Context mContext;

    private static class ViewHolder {
        TextView txtName;
        TextView txtNotes;
        Button bDelete;
    }

    public CharacterAdapter(ArrayList<CharacterDataModel> data, Context context) {
        super(context, R.layout.custom_layout_character, data);
        this.dataSet = data;
        this.mContext=context;
    }

    @Override
    public void onClick(View v) {
        // get the position that was clicked
        int position = (Integer) v.getTag();

        // convert it into an object
        Object object = getItem(position);

        // cast the object into our model
        final CharacterDataModel dataModel = (CharacterDataModel) object;

        // figure out which one we clicked
        switch (v.getId()) {
            case R.id.Delete:
                // create our popup dialog
                final Dialog dialog = new Dialog(mContext);

                // have the keyboard show up once we have the ability
                //  https://stackoverflow.com/questions/4258623/show-soft-keyboard-for-dialog
                Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

                // remove the title bar MUST BE CALLED BEFORE SETTING THE CONTENT VIEW!!
                // https://stackoverflow.com/questions/2644134/android-how-to-create-a-dialog-without-a-title
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.confirm_delete);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(true);

                TextView title = dialog.findViewById(R.id.Name);
                String message = "Are you sure you want to delete \n'" + Objects.requireNonNull(dataModel).getCharacter().name + "'?";

                title.setText(message);

                // set up our confirm button
                Button b_Confirm = dialog.findViewById(R.id.Confirm);
                b_Confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        // call our delete character from the MainActivity
                        //https://stackoverflow.com/questions/17315842/how-to-call-a-method-in-mainactivity-from-another-class/25260829
                        MainActivity.getInstance().deleteCharacter(dataModel.getCharacter());
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
        CharacterDataModel dataModel = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());

            convertView = inflater.inflate(R.layout.custom_layout_character, parent, false);
            viewHolder.txtName = convertView.findViewById(R.id.Name);
            viewHolder.txtNotes = convertView.findViewById(R.id.Notes);
            viewHolder.bDelete = convertView.findViewById(R.id.Delete);

            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        // set up our textViews
        viewHolder.txtName.setText(Objects.requireNonNull(dataModel).getCharacter().name);
        viewHolder.txtNotes.setText(dataModel.getCharacter().notes);

        // set up our delete button
        viewHolder.bDelete.setOnClickListener(this);
        viewHolder.bDelete.setTag(position);

        // Return the completed view to render on screen
        return convertView;
    }
}