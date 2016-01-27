package com.andrew.iscool.fuelrecords;

import java.util.Date;

/**
 * Created by Andrew on 2016-01-27.
 */
public class FuelEntry {
    private Date mDate;
    private String mStation;
    private float mOdometer;
    private String mGrade;
    private float mAmount;
    private float mUnitCost;

    // fuel cost is computer and no way to set it from the outside
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
        this.mCost = mUnitCost * mAmount;
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

    // SETTERS
    // no way to set the cost, it is computer by the class itself

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    public void setStation(String mStation) {
        this.mStation = mStation;
    }

    public void setOdometer(float mOdometer) {
        this.mOdometer = mOdometer;
    }

    public void setGrade(String mGrade) {
        this.mGrade = mGrade;
    }

    public void setAmount(float mAmount) {
        this.mAmount = mAmount;
        updateTotalCost();
    }

    public void setUnitCost(float mUnitCost) {
        this.mUnitCost = mUnitCost;
        updateTotalCost();
    }

    public Date getDate() {
        return mDate;
    }

    // PRIVATE

    private void updateTotalCost() {
        this.mCost = this.mUnitCost + this.mAmount;
    }
}
