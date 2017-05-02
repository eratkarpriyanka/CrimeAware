package com.orionlabs.crimeaware.database;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = MyDatabase.NAME, version = MyDatabase.VERSION)
public class MyDatabase {
    public static final String NAME = "CrimeAwareDb";

    public static final int VERSION = 1;
}
