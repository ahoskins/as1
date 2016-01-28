package com.andrew.iscool.fuelrecords;

import android.test.ActivityInstrumentationTestCase2;

import java.util.Date;

/**
 * Created by Andrew on 2016-01-28.
 */
public class FuelDataTests extends ActivityInstrumentationTestCase2 {
    public FuelDataTests() {
        super(FuelListActivity.class);
    }

    /*
    * Test the model, which is the FuelEntry class.  It is stored in an ArrayList
    * in the Activity (no point testing the whole list because it's a built-in Java library).
    * - test all the getters for FuelEntry, they are used a lot in EntryDialog and FuelListAdapter
    * - test the total cost for FuelEntry, it is used when instantiating a FuelEntry
    * */

    public void testGetDateFuelEntry() {
        Date date = new Date(System.currentTimeMillis());
        FuelEntry fe = new FuelEntry(date, "Shell", 100.2f, "Premium", 24.3f, 67.34f);

        assertTrue(fe.getDate() == date);
    }

    public void testGetLocationFuelEntry() {
        Date date = new Date(System.currentTimeMillis());
        FuelEntry fe = new FuelEntry(date, "Shell", 100.2f, "Premium", 24.3f, 67.34f);

        assertTrue(fe.getStation() == "Shell");
    }

    public void testGetOdometerFuelEntry() {
        Date date = new Date(System.currentTimeMillis());
        FuelEntry fe = new FuelEntry(date, "Shell", 100.2f, "Premium", 24.3f, 67.34f);

        assertTrue(fe.getOdometer() == 100.2f);
    }

    public void testGetGradeFuelEntry() {
        Date date = new Date(System.currentTimeMillis());
        FuelEntry fe = new FuelEntry(date, "Shell", 100.2f, "Premium", 24.3f, 67.34f);

        assertTrue(fe.getGrade() == "Premium");
    }

    public void testGetUnitCostFuelEntry() {
        Date date = new Date(System.currentTimeMillis());
        FuelEntry fe = new FuelEntry(date, "Shell", 100.2f, "Premium", 24.3f, 67.34f);

        assertTrue(fe.getUnitCost() == 67.34f);
    }

    public void testGetAmountFuelEntry() {
        Date date = new Date(System.currentTimeMillis());
        FuelEntry fe = new FuelEntry(date, "Shell", 100.2f, "Premium", 24.3f, 67.34f);

        assertTrue(fe.getAmount() == 24.3f);
    }

    public void testGetFuelEntryCost() {
        Date date = new Date(System.currentTimeMillis());
        FuelEntry fe = new FuelEntry(date, "Shell", 100.2f, "Premium", 24.3f, 67.34f);

        assertTrue(fe.getCost() == (24.3f * 67.34f) / 100);
    }

}
