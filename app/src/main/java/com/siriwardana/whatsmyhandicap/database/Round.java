package com.siriwardana.whatsmyhandicap.database;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Round {

    @PrimaryKey(autoGenerate = true)
    public int roundId;

    @ColumnInfo(name = "user_id")
    public int userId;

    @ColumnInfo(name = "club_name")
    public String clubName;

    @ColumnInfo(name = "course_name")
    public String courseName;

    @ColumnInfo(name = "num_holes")
    public int numHoles;

    @ColumnInfo(name = "score")
    @Nullable
    public int score;

    public int getRoundId() {
        return roundId;
    }

    public void setRoundId(int roundId) {
        this.roundId = roundId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getNumHoles() {
        return numHoles;
    }

    public void setNumHoles(int numHoles) {
        this.numHoles = numHoles;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
