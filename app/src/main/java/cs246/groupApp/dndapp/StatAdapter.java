package cs246.groupApp.dndapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

public class StatAdapter extends ArrayAdapter<StatDataModel> {
    private ArrayList<StatDataModel> dataSet;
    private Context context;

    private static class ViewHolder {
        TextView txtName;
        TextView txtPrimary;
        TextView txtBonus;
    }

    StatAdapter(ArrayList<StatDataModel> data, Context context) {
        super(context, R.layout.custom_layout_stat, data);
        this.dataSet = data;
        this.context = context;
    }

    /**
     * Sets up the view.
     * @param position Position of item in the array
     * @param convertView View object to be changed
     * @param parent Parent ViewGroup
     * @return returns the converted view object
     * @author Alex Kearns
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        StatDataModel dataModel = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        StatAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {
            viewHolder = new StatAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());

            convertView = inflater.inflate(R.layout.custom_layout_stat, parent, false);
            viewHolder.txtName = convertView.findViewById(R.id.customName);
            viewHolder.txtPrimary = convertView.findViewById(R.id.customValue);
            viewHolder.txtBonus = convertView.findViewById(R.id.customBonus);

            convertView.setTag(viewHolder);
        } else
            viewHolder = (StatAdapter.ViewHolder) convertView.getTag();

        
        // set up our name
        viewHolder.txtName.setText(Objects.requireNonNull(dataModel).getStat().name);

        // set up our primary
        if(dataModel.isShowPrimary()) {
            String value = Integer.toString(dataModel.getStat().value);
            viewHolder.txtPrimary.setText(value);
        }
        else
            viewHolder.txtPrimary.setVisibility(View.GONE);

        // set up our bonus
        if(dataModel.isShowBonus()) {
            String bonus = Integer.toString(dataModel.getStat().bonus);
            viewHolder.txtBonus.setText(bonus);
        }
        else
            viewHolder.txtBonus.setVisibility(View.GONE);

        // Return the completed view to render on screen
        return convertView;
    }
}
