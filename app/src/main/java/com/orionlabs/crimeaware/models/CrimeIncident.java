package com.orionlabs.crimeaware.models;

import com.orionlabs.crimeaware.database.MyDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = MyDatabase.class)
public class CrimeIncident extends BaseModel {

    @PrimaryKey
    @Column
    String rdNo;
    @Column
    long beat;
    @Column
    long cpdDistrict;
    @Column
    String dateOccurred;
    @Column
    String lastUpdated;
    @Column
    String iucrDescription;
    @Column
    String locationDesc;
    @Column
    double xCoordinate;
    @Column
    double yCoordinate;
    @Column
    long districtCrimeFreq;

    public CrimeIncident(){

    }

    public String getRdNo() {
        return rdNo;
    }

    public void setRdNo(String rdNo) {
        this.rdNo = rdNo;
    }

    public long getBeat() {
        return beat;
    }

    public void setBeat(long beat) {
        this.beat = beat;
    }

    public long getCpdDistrict() {
        return cpdDistrict;
    }

    public void setCpdDistrict(long cpdDistrict) {
        this.cpdDistrict = cpdDistrict;
    }

    public String getDateOccurred() {
        return dateOccurred;
    }

    public void setDateOccurred(String dateOccurred) {
        this.dateOccurred = dateOccurred;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getIucrDescription() {
        return iucrDescription;
    }

    public void setIucrDescription(String iucrDescription) {
        this.iucrDescription = iucrDescription;
    }

    public String getLocationDesc() {
        return locationDesc;
    }

    public void setLocationDesc(String locationDesc) {
        this.locationDesc = locationDesc;
    }

    public double getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(double xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public double getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(double yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public void setyCoordinate(long yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public long getDistrictCrimeFreq() {
        return districtCrimeFreq;
    }

    public void setDistrictCrimeFreq(long districtCrimeFreq) {
        this.districtCrimeFreq = districtCrimeFreq;
    }
}
