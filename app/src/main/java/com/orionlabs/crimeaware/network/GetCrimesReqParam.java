package com.orionlabs.crimeaware.network;

public class GetCrimesReqParam {

    long offset;
    long max;
    String dateOccuredStart;
    String dateOccuredEnd;

    public GetCrimesReqParam(String dateOccuredStart, String dateOccuredEnd,long offset,long max){

        this.dateOccuredStart = dateOccuredStart;
        this.dateOccuredEnd = dateOccuredEnd;
        this.offset = offset;
        this.max = max;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public String getDateOccuredStart() {
        return dateOccuredStart;
    }

    public void setDateOccuredStart(String dateOccuredStart) {
        this.dateOccuredStart = dateOccuredStart;
    }

    public String getDateOccuredEnd() {
        return dateOccuredEnd;
    }

    public void setDateOccuredEnd(String dateOccuredEnd) {
        this.dateOccuredEnd = dateOccuredEnd;
    }
}
