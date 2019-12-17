package com.example.xyzreader.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(
        entities = {DatabaseBook.class},
        version = 1,
        exportSchema = false
)
public abstract class XYZDatabase extends RoomDatabase {

    private static final String LOG_TAG = XYZDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "recipes";
    private static XYZDatabase sInstance;

    public static XYZDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        XYZDatabase.class, XYZDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract XYZDao xyzDao();

}