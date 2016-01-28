package com.andrew.iscool.fuelrecords;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
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
* - file stuff. load in onCreate and save in onStop (done)
* - format with correct number of decimal places
* - display total fuel cost and update dynamically
* */

public class FuelListActivity extends ActionBarActivity implements EntryDialog.EntryDialogListener {
    private static String FILENAME = "fuel.txt";

    private Button mNewEntryButton;
    private ListView mFuelListView;
    private ArrayList<FuelEntry> mFuelData;
    private FuelListAdapter mAdapter;
    private TextView mTotalCost;
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

        computeTotalCost();
    }

    @Override
    public void onStop() {
        super.onStop();

        saveToFile();
    }

    private void computeTotalCost() {
        mTotalCostValue = 0;
        for (FuelEntry entry : mFuelData) {
            mTotalCostValue += entry.getCost();
        }
        mTotalCost.setText("Total Cost: " + mTotalCostValue);
    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fuel_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // LISTENER FROM DIALOG

    @Override
    public void onDialogPositiveClick(Date date, String station, float odometer, String grade, float amount, float unitCost, Integer i) {
        FuelEntry fe = new FuelEntry(date, station, odometer, grade, amount, unitCost);

        // if not null, then replace, otherwise add to the end
        if (i != null) {
            mFuelData.set(i, fe);
        } else {
            mFuelData.add(fe);
        }

        computeTotalCost();

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public FuelEntry getFuelData(int i) {
        return this.mFuelData.get(i);
    }

}
