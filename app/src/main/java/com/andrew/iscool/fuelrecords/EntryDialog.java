package com.andrew.iscool.fuelrecords;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
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
public class EntryDialog extends DialogFragment {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
    private EntryDialogListener mListener;
    private Integer mDataIndex;
    private AlertDialog mAlertDialog;

    private EditText mDate;
    private EditText mStation;
    private EditText mOdometer;
    private EditText mGrade;
    private EditText mAmount;
    private EditText mUnitCost;

    private Date mDateValue;
    private String mStationValue;
    private Float mOdometerValue;
    private String mGradeValue;
    private Float mAmountValue;
    private Float mUnitCostValue;

    // use interface with parent
    public interface EntryDialogListener {
        void onDialogPositiveClick(Date date,
                                   String station,
                                   float odometer,
                                   String grade,
                                   float amount,
                                   float unitCost,
                                   Integer index);
        FuelEntry getFuelData(int index);
    }

    public EntryDialog() {}

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

    private void initEditTexts(View view) {
        // get reference to them all
        mDate = (EditText) view.findViewById(R.id.date);
        mStation = (EditText) view.findViewById(R.id.station);
        mOdometer = (EditText) view.findViewById(R.id.odometer);
        mGrade = (EditText) view.findViewById(R.id.grade);
        mAmount = (EditText) view.findViewById(R.id.amount);
        mUnitCost = (EditText) view.findViewById(R.id.unit_cost);

        // if editing, init the values
        if (mDataIndex != null) {
            FuelEntry fe = mListener.getFuelData(mDataIndex);

            mDate.setText(sdf.format(fe.getDate()));
            mDateValue = fe.getDate();
            mStation.setText(fe.getStation());
            mStationValue = fe.getStation();
            mOdometer.setText(Float.toString(fe.getOdometer()));
            mOdometerValue = fe.getOdometer();
            mGrade.setText(fe.getGrade());
            mGradeValue = fe.getGrade();
            mAmount.setText(Float.toString(fe.getAmount()));
            mAmountValue = fe.getAmount();
            mUnitCost.setText(Float.toString(fe.getUnitCost()));
            mUnitCostValue = fe.getUnitCost();
        }

        mDate.addTextChangedListener((new GenericTextWatcher(mDate)));
        mOdometer.addTextChangedListener((new GenericTextWatcher(mOdometer)));
        mAmount.addTextChangedListener((new GenericTextWatcher(mAmount)));
        mUnitCost.addTextChangedListener((new GenericTextWatcher(mUnitCost)));
        mStation.addTextChangedListener(new GenericTextWatcher(mStation));
        mGrade.addTextChangedListener(new GenericTextWatcher(mGrade));

    }

    private void checkIfSaveEnabled() {
        // if all buttons filled out validly, then enable save button
        if (mDateValue != null && mOdometerValue != null && mAmountValue != null
                && mUnitCostValue != null && mGrade.getText().length() != 0
                && mStation.getText().length() != 0) {
            mAlertDialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // android hates being cool, so it makes me wait till here till I can call this
        if (mDataIndex != null) {
            // if creating, then start off being disabled
            mAlertDialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(true);
        } else {
            mAlertDialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.edit_menu, null);

        initEditTexts(view);

        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Fuel Entry")
                .setView(view)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // tell the activity the updated entry
                        // all values are non-null and non-empty string, or else this button would be disabled
                        mListener.onDialogPositiveClick(mDateValue,
                                mStationValue,
                                mOdometerValue,
                                mGradeValue,
                                mAmountValue,
                                mUnitCostValue,
                                mDataIndex);

                        dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                });
        mAlertDialog =  builder.create();

        return mAlertDialog;
    }

    private class GenericTextWatcher implements TextWatcher {
        private EditText view;

        public GenericTextWatcher(EditText view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable t) {
            String text = t.toString();

            switch (view.getId()) {
                case R.id.date:
                    try {
                        mDateValue = sdf.parse(text);
                        // check if all values are non null
                        checkIfSaveEnabled();
                    } catch (ParseException e) {
                        // set to null
                        // disable save button
                        mDate.setError("Required field (yyyy-mm-dd)");
                        mDateValue = null;
                        mAlertDialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
                    }
                    break;
                case R.id.odometer:
                    try {
                        mOdometerValue = Float.parseFloat(text);
                        checkIfSaveEnabled();
                    } catch (NumberFormatException e) {
                        mOdometer.setError("Required field (number)");
                        mOdometerValue = null;
                        mAlertDialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
                    }
                    break;
                case R.id.amount:
                    try {
                        mAmountValue = Float.parseFloat(text);
                        checkIfSaveEnabled();
                    } catch (NumberFormatException e) {
                        mAmount.setError("Required field (number)");
                        mAmountValue = null;
                        mAlertDialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
                    }
                    break;
                case R.id.unit_cost:
                    try {
                        mUnitCostValue = Float.parseFloat(text);
                        checkIfSaveEnabled();
                    } catch (NumberFormatException e) {
                        mUnitCost.setError("Required field (number)");
                        mUnitCostValue = null;
                        mAlertDialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
                    }
                    break;
                case R.id.station:
                    mStationValue = text;
                    if (text.length() == 0) {
                        mAlertDialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
                        mStation.setError("Required field (string)");
                    }
                    checkIfSaveEnabled();
                    break;
                case R.id.grade:
                    mGradeValue = text;
                    if (text.length() == 0) {
                        mAlertDialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
                        mGrade.setError("Required field (string)");
                    }
                    checkIfSaveEnabled();
                    break;
            }
        }
    }

}
