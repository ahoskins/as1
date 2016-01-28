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

public class FuelListActivity extends ActionBarActivity implements EntryDialog.EntryDialogListener {
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

        // TODO: LOAD FROM FILE, for now just dummy data

        mFuelData = new ArrayList<FuelEntry>();
        FuelEntry fe = new FuelEntry(new Date(System.currentTimeMillis()), "Buylea", 200, "Normal", 34.5f, 70f);
        mFuelData.add(fe);

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
    }

    @Override
    protected void onStart() {
        super.onStart();

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

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public FuelEntry getFuelData(int i) {
        return this.mFuelData.get(i);
    }

}
