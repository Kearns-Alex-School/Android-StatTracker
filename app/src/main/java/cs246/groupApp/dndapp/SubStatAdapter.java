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
    private Context mContext;
    Character character;

    private static class ViewHolder {
        TextView txtBonus;
        TextView txtName;
        TextView txtStatname;
        Button bDelete;
    }

    public SubStatAdapter(ArrayList<SubStatDataModel> data, Context context) {
        super(context, R.layout.substat_custom, data);
        this.dataSet = data;
        this.mContext=context;
    }

    @Override
    public void onClick(View v) {
        int position =(Integer) v.getTag();
        Object object = getItem(position);
        final SubStatDataModel dataModel = (SubStatDataModel) object;

        switch (v.getId()) {
            case R.id.Delete:
                // create our popup dialog
                final Dialog dialog = new Dialog(mContext);

                // have the keyboard show up once we have the ability
                //  https://stackoverflow.com/questions/4258623/show-soft-keyboard-for-dialog
                Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

                // remove the title bar MUST BE CALLED BEFORE SETTING THE CONTENT VIEW!!
                // https://stackoverflow.com/questions/2644134/android-how-to-create-a-dialog-without-a-title
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.confirm_delete);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(true);

                TextView title = dialog.findViewById(R.id.Title);
                String message = "Are you sure you want to delete \n'" + Objects.requireNonNull(dataModel).name + "'?";

                title.setText(message);

                Button b_Confirm = dialog.findViewById(R.id.Confirm);
                b_Confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // delete the item somehow!!!!!
                        Character character = dataModel.getCharacter();

                        for (int index = 0; index < character.subStats.size(); index++)
                            if (character.subStats.get(index).name.equals(dataModel.name))
                                character.subStats.remove(index);

                        dialog.dismiss();

                        //https://stackoverflow.com/questions/17315842/how-to-call-a-method-in-mainactivity-from-another-class/25260829
                        CharacterDetailsActivity.getInstance().writeFile(character.fileName, true);
                    }
                });

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

            convertView = inflater.inflate(R.layout.substat_custom, parent, false);
            viewHolder.txtBonus = convertView.findViewById(R.id.Bonus);
            viewHolder.txtName = convertView.findViewById(R.id.Name);
            viewHolder.txtStatname = convertView.findViewById(R.id.StatName);
            viewHolder.bDelete = convertView.findViewById(R.id.Delete);

            // icon stuff possibly helpful in the near future
            /*viewHolder.info = (ImageView) convertView.findViewById(R.id.item_info);*/

            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        // check all of our custom stuff

        // bonus text
        if(dataModel.getShowBonus()) {
            viewHolder.txtBonus.setText(dataModel.getBonus());
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
            viewHolder.txtStatname.setText(dataModel.getStatname());
            viewHolder.txtStatname.setTextSize(dataModel.getSubTextSize());
        }
        else
            viewHolder.txtStatname.setVisibility(View.GONE);

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
