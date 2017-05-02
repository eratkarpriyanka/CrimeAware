package com.orionlabs.crimeaware.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateTimeUtils {

    private static final String TAG = DateTimeUtils.class.getSimpleName();
    private final Calendar calendar;

    private static DateTimeUtils instance =null;

    public static DateTimeUtils getInstance(){

        if(instance==null){
            instance = new DateTimeUtils();
        }
        return instance;
    }

    public DateTimeUtils(){

        // this would default to now
        calendar = Calendar.getInstance();
    }

    public String getCurrentdate() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        return dateFormat.format(calendar.getTime());
    }

    public String getStartDate(){

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        calendar.add(Calendar.DAY_OF_MONTH, -30);
        return dateFormat.format(calendar.getTime());
    }
}
