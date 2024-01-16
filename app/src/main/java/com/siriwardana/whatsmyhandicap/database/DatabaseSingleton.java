package com.siriwardana.whatsmyhandicap.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {User.class, Round.class, Hole.class}, version = 7)
@TypeConverters(Converters.class)
public abstract class DatabaseSingleton extends RoomDatabase {

    public  abstract UserDao UserDao();
    public abstract RoundDao RoundDao();
    public abstract HoleDao HoleDao();
    private static DatabaseSingleton INSTANCE;

    public static DatabaseSingleton getDBInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), DatabaseSingleton.class, "WMH_DB")
                    .allowMainThreadQueries()
                    .build();
        }

        return INSTANCE;
    }


}
