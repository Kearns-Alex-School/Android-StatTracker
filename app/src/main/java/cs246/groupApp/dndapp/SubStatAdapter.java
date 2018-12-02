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

//https://www.journaldev.com/10416/android-listview-with-custom-adapter-example-tutorial
public class SubStatAdapter extends ArrayAdapter<SubStatDataModel> implements View.OnClickListener {

    private ArrayList<SubStatDataModel> dataSet;
    private Context context;

    private static class ViewHolder {
        TextView txtName;
        TextView txtStatName;
        TextView txtBonus;
        Button bDelete;
    }

    public SubStatAdapter(ArrayList<SubStatDataModel> data, Context context) {
        super(context, R.layout.custom_layout_substat, data);
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
        final SubStatDataModel dataModel = (SubStatDataModel) object;

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

                        for (int index = 0; index < character.subStats.size(); index++)
                            if (character.subStats.get(index).name.equals(dataModel.getName()))
                                character.subStats.remove(index);

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
        SubStatDataModel dataModel = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());

            convertView = inflater.inflate(R.layout.custom_layout_substat, parent, false);
            viewHolder.txtBonus = convertView.findViewById(R.id.Bonus);
            viewHolder.txtName = convertView.findViewById(R.id.Message);
            viewHolder.txtStatName = convertView.findViewById(R.id.StatName);
            viewHolder.bDelete = convertView.findViewById(R.id.Delete);

            // icon stuff possibly helpful in the near future
            /*viewHolder.info = (ImageView) convertView.findViewById(R.id.item_info);*/

            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        // check all of our custom stuff

        // bonus text
        if(dataModel.getShowBonus()) {
            viewHolder.txtBonus.setText(Integer.toString(dataModel.getBonus()));
            viewHolder.txtBonus.setTextSize(dataModel.getMainTextSize());
        }
        else
            viewHolder.txtBonus.setVisibility(View.GONE);

        // name text
        if(dataModel.getShowName()) {
            viewHolder.txtName.setText(dataModel.getName());
            viewHolder.txtName.setTextSize(dataModel.getMainTextSize());
        }
        else
            viewHolder.txtName.setVisibility(View.GONE);

        // stat name txt
        if(dataModel.getShowStatName()) {
            viewHolder.txtStatName.setText(dataModel.getStatName());
            viewHolder.txtStatName.setTextSize(dataModel.getSubTextSize());
        }
        else
            viewHolder.txtStatName.setVisibility(View.GONE);

        // delete button
        if(dataModel.getShowDelete()) {
            viewHolder.bDelete.setOnClickListener(this);
            viewHolder.bDelete.setTag(position);
        }
        else
            viewHolder.bDelete.setVisibility(View.GONE);

        // icon stuff possibly helpful in the near future
        /*viewHolder.info.setOnClickListener(this);
        viewHolder.info.setTag(position);*/

        // Return the completed view to render on screen
        return convertView;
    }
}