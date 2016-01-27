package com.andrew.iscool.fuelrecords;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Andrew on 2016-01-27.
 */
public class FuelListAdapter extends ArrayAdapter<FuelEntry> {
    Context mContext;
    ArrayList<FuelEntry> mData;

    // all the information is showed in each row
    // when you click on a row it opens the edit dialog for that

    public FuelListAdapter(Context context, int resource, ArrayList<FuelEntry> fuelList) {
        super(context, resource, fuelList);
        this.mData = fuelList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public FuelEntry getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.fuel_row, null);
        }
        TextView main = (TextView) vi.findViewById(R.id.main);
        TextView secondary = (TextView) vi.findViewById(R.id.secondary);

        FuelEntry data = mData.get(position);

        // Date - Station - Cost
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        main.setText(sdf.format(data.getDate()) + " - " +
                     data.getStation() + " - $" +
                     data.getCost());
        // Odometer - Grade - Amount - Unit Cost
        secondary.setText("Odometer: " + data.getOdometer() + "km - Grade: " +
                          data.getGrade() + " - Amount: " + data.getAmount() +
                          "L - Unit Cost: " + data.getUnitCost() + "cents/L");

        return vi;
    }
}
