package com.andrew.iscool.fuelrecords;

import java.util.Date;

/**
* POJO representing a single fuel entry.  This object is immutable.
 * This object encapsulates the computation of the cost for this fuel entry.
* */
public class FuelEntry {
    private Date mDate;
    private String mStation;
    private float mOdometer;
    private String mGrade;
    private float mAmount;
    private float mUnitCost;
    private float mCost;

    // CONSTRUCTOR

    // constructor, must provide all values, nothing is inferred
    public FuelEntry(Date mDate, String mStation, float mOdometer, String mGrade, float mAmount, float mUnitCost) {
        this.mDate = mDate;
        this.mStation = mStation;
        this.mOdometer = mOdometer;
        this.mGrade = mGrade;
        this.mAmount = mAmount;
        this.mUnitCost = mUnitCost;
        this.mCost = mUnitCost * mAmount / 100;
    }

    // GETTERS

    public String getStation() {
        return mStation;
    }

    public float getOdometer() {
        return mOdometer;
    }

    public String getGrade() {
        return mGrade;
    }

    public float getAmount() {
        return mAmount;
    }

    public float getUnitCost() {
        return mUnitCost;
    }

    public float getCost() {
        return mCost;
    }

    public Date getDate() {
        return mDate;
    }
}
