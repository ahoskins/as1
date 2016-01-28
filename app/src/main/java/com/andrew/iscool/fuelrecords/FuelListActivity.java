package com.andrew.iscool.fuelrecords;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

/*
* TODO:
* - UML diagram
* - document code (done)
* - make demo video
* - add APK file to repo
* - test running on lab machines (done)
* - unit tests (done)
* */

/**
* The main and only activity of the app.
*
* It launches a DialogFragment and communicates over
* the EntryDialogListener, that's why it implements this interface.
*
* This activity loads and saves the data from file, and stores the instance of the data.
* This pattern was chosen rather than a Singleton with data because there only needs to be one activity.
* */
public class FuelListActivity extends ActionBarActivity implements EntryDialog.EntryDialogListener {
    private static String FILENAME = "fuel.txt";

    // UI pieces
    private Button mNewEntryButton;
    private ListView mFuelListView;
    private TextView mTotalCost;

    // Other global things
    private ArrayList<FuelEntry> mFuelData;
    private FuelListAdapter mAdapter;
    private float mTotalCostValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fuel_list);
        mNewEntryButton = (Button) findViewById(R.id.newEntryButton);
        mFuelListView = (ListView) findViewById(R.id.fuelList);
        mTotalCost = (TextView) findViewById(R.id.totalCost);

        // load data from file and setup adapter
        readFromFile();
        mAdapter = new FuelListAdapter(getApplicationContext(), R.layout.fuel_row, mFuelData);
        mFuelListView.setAdapter(mAdapter);

        // set listeners
        mNewEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EntryDialog d = new EntryDialog();
                d.show(getSupportFragmentManager(), "create");
            }
        });

        mFuelListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // open the edit menu with and use this "position" index
                EntryDialog d = new EntryDialog();
                Bundle bundle = new Bundle();
                bundle.putInt("index", position);
                d.setArguments(bundle);
                d.show(getSupportFragmentManager(), "edit");
            }
        });

        updateTotalCost();
    }

    /*
    * Compute total cost of all fuel and display on UI
    * */
    private void updateTotalCost() {
        mTotalCostValue = 0;
        for (FuelEntry entry : mFuelData) {
            mTotalCostValue += entry.getCost();
        }
        mTotalCost.setText("Total Cost: " + String.format("%.2f", mTotalCostValue));
    }

    /*
    * Use GSON to read array from file and update mFuelData member variable
    * */
    private void readFromFile() {
        // if file exists, read in, otherwise it will be empty array
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<FuelEntry>>() {}.getType();

            mFuelData = gson.fromJson(in, listType);
        } catch (FileNotFoundException e) {
            mFuelData = new ArrayList<FuelEntry>();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /*
    * Save current contents of mFuelData to file
    * */
    private void saveToFile() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME, 0);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();
            gson.toJson(mFuelData, out);

            out.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * Listener methods from dialog
    * */

    /*
     *  Called when dialog is saved, and thus all passed data is valid (Dialog does the validation)
     *  Last argument, i, will be null if new entry, otherwise it will be the index of the entry to edit
     */
    @Override
    public void onDialogPositiveClick(Date date, String station, float odometer, String grade, float amount, float unitCost, Integer i) {
        FuelEntry fe = new FuelEntry(date, station, odometer, grade, amount, unitCost);

        if (i != null) {
            // FuelEntry is an immutable object, so replace at specified index
            mFuelData.set(i, fe);
        } else {
            // otherwise, it's a new entry, add it to the end
            mFuelData.add(fe);
        }


        updateTotalCost();

        // immediately write changes to file
        saveToFile();

        mAdapter.notifyDataSetChanged();
    }

    /*
    * When editing an entry, the DialogFragment keeps to know the field values of the FuelEntry we are editing
    * This is a getter for that.
    * */
    @Override
    public FuelEntry getFuelData(int i) {
        return this.mFuelData.get(i);
    }

}
