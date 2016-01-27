package com.andrew.iscool.fuelrecords;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;


public class FuelListActivity extends ActionBarActivity {
    private Button mNewEntryButton;
    private ListView mFuelListView;
    private ArrayList<FuelEntry> mFuelData;
    private FuelListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fuel_list);
        mNewEntryButton = (Button) findViewById(R.id.newEntryButton);
        mFuelListView = (ListView) findViewById(R.id.fuelList);

        // set listeners
        mNewEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open activity that lets you make a new fuel entry
                // probably use same XML as edit fuel entry, but
                // a different wrapping activity
            }
        });

        mFuelListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // open the edit menu with and use this "position" index
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // read mFuelData from file using GSON

        // always have one dummy entry for now
        mFuelData = new ArrayList<FuelEntry>();
        FuelEntry fe = new FuelEntry(new Date(System.currentTimeMillis()), "Buylea", 200, "Normal", 34.5f, 70f);
        mFuelData.add(fe);

        // setup adapter
        mAdapter = new FuelListAdapter(getApplicationContext(), R.layout.fuel_row, mFuelData);
        mFuelListView.setAdapter(mAdapter);
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
}
