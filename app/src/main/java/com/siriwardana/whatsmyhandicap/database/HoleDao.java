package com.siriwardana.whatsmyhandicap.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface HoleDao {

    @Insert
    void insert(Hole... holes);

    @Update
    void update(Hole... holes);

    @Delete
    void delete(Hole hole);

    @Query("SELECT * FROM hole")
    List<Hole> getAllHoles();

    @Query("SELECT * FROM hole WHERE hole.round_id = :roundId ORDER BY hole.holeId ASC")
    List<Hole> getHolesByRound(int roundId);

    @Query("SELECT * FROM hole WHERE hole.round_id = :roundId " +
            "AND hole.hole_number = :holeNum ORDER BY hole.holeId DESC LIMIT 1")
    Hole getHoleByRound(int roundId, int holeNum);

    @Query("SELECT COUNT(hole_score) FROM hole WHERE hole.round_id = :roundId")
    int getRoundHoleCount(int roundId);
}
