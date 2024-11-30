package com.siriwardana.whatsmyhandicap.helpers;

import android.content.Context;
import android.util.Log;

import com.siriwardana.whatsmyhandicap.database.DatabaseSingleton;
import com.siriwardana.whatsmyhandicap.database.Hole;
import com.siriwardana.whatsmyhandicap.database.Round;

import java.util.List;

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
    public void insertHoleData(Hole hole) {
        Log.d("SSIRI", "Saving values for hole: " + hole.getHoleNumber());
        databaseSingleton.HoleDao().insert(hole);
    }

    public Hole getHoleByRound(int roundId, int holeNumber) {
        return databaseSingleton.HoleDao().getHoleByRound(roundId, holeNumber);
    }

    public List<Hole> getHolesByRound(int roundId) {
        return databaseSingleton.HoleDao().getHolesByRound(roundId);
    }

    public void updateHole(Hole hole) {
        databaseSingleton.HoleDao().update(hole);
    }

    public void updateRound(Round round) {
        databaseSingleton.RoundDao().update(round);
    }

    public void updateRoundScore(int roundId) {
        Round round = databaseSingleton.RoundDao().getRoundById(roundId);
        List<Hole> holes = databaseSingleton.HoleDao().getHolesByRound(roundId);
        int numHoles = round.getNumHoles();
        Hole hole;
        int score = 0;
        for (int i = 0; i < numHoles; i++) {
            hole = holes.get(i);
            score = score + hole.getHoleScore();
        }

        round.setScore(score);
        updateRound(round);
    }

    public void deleteRound(Round round) {
        databaseSingleton.RoundDao().delete(round);
    }

    public void deleteHolesByRoundID(int roundID) {
        databaseSingleton.HoleDao().deleteHolesByRoundID(roundID);
    }

}
