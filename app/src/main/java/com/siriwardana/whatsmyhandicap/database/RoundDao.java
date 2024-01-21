package com.siriwardana.whatsmyhandicap.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RoundDao {

    @Insert
    void insert(Round... rounds);

    @Update
    void update (Round... rounds);

    @Delete
    void delete(Round round);

    @Query("SELECT * FROM round")
    List<Round> getAllRounds();

    @Query("SELECT * FROM round WHERE round.user_id = :userId ORDER BY round.roundId DESC")
    List<Round> getRoundsByUser(int userId);

    @Query("SELECT round.roundId FROM round ORDER BY round.roundId DESC")
    int getLatestRoundId();

    @Query("SELECT * FROM round WHERE round.roundId = :roundId")
    Round getRoundById(int roundId);

}
