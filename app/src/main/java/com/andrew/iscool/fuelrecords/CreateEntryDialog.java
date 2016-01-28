package com.andrew.iscool.fuelrecords;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Andrew on 2016-01-27.
 */
public class CreateEntryDialog extends DialogFragment {
    private EntryDialogListener mListener;
    private Integer mDataIndex;

    private EditText mDate;
    private EditText mStation;

    // use interface with parent
    public interface EntryDialogListener {
        void onDialogPositiveClick(Date d, String s, Integer index);
        FuelEntry getFuelData(int index);
    }

    public CreateEntryDialog() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDataIndex = getArguments() != null ? getArguments().getInt("index"): null;
    }

    @Override
    public void onAttach(Activity a) {
        super.onAttach(a);

        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (EntryDialogListener) a;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(a.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.edit_menu, null);

        mDate = (EditText) view.findViewById(R.id.date);
        mStation = (EditText) view.findViewById(R.id.station);

        // index is passed in from activity
        // if index is not null, access the data at that index and init the values
        if (mDataIndex != null) {
            // init the picks with the value at this index
            FuelEntry fe = mListener.getFuelData(mDataIndex);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
            mDate.setText(sdf.format(fe.getDate()));
            mStation.setText(fe.getStation());
        }

        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Fuel Entry")
                .setView(view)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // convert the date picker into a date object
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = null;
                        try {
                            date = sdf.parse(mDate.getText().toString());
                        } catch (ParseException e) {

                        }

                        // tell the activity
                        mListener.onDialogPositiveClick(date, mStation.getText().toString(), mDataIndex);
                        dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                });
        return builder.create();
    }



}
