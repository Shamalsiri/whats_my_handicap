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
    void update(Round... rounds);

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

    @Query("SELECT round.roundId FROM round WHERE round.user_id = :userId ORDER BY round.score ASC LIMIT 1")
    int getBestRoundIdByUser(int userId);

    @Query("SELECT * FROM round WHERE round.user_id = :userID ORDER BY round.score ASC LIMIT 8")
    List<Round> getBest8RoundsByUserID(int userID);

}
