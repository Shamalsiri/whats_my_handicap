package com.siriwardana.whatsmyhandicap.helpers;

import android.content.Context;
import android.util.Log;

import com.siriwardana.whatsmyhandicap.database.DatabaseSingleton;
import com.siriwardana.whatsmyhandicap.database.Hole;

public class DataStorageHelper {

    DatabaseSingleton databaseSingleton;
    Context context;

    public DataStorageHelper(Context context) {
        this.context = context;
        databaseSingleton = DatabaseSingleton.getDBInstance(context);
    }

    /**
     * Save hole data to the database
     *
     * @param hole
     */
    public void saveHoleData(Hole hole) {
        Log.d("SSIRI", "Saving values for hole: " + hole.getHoleNumber());
        databaseSingleton.HoleDao().insert(hole);
    }
}
