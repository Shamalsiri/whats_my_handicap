package com.siriwardana.whatsmyhandicap.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "hole")
public class Hole {

    @PrimaryKey(autoGenerate = true)
    public int holeId;

    @ColumnInfo(name = "round_id")
    public int roundId;

    @ColumnInfo(name = "user_id")
    public int userId;

    @ColumnInfo(name = "hole_number")
    public int holeNumber;

    @ColumnInfo(name = "par")
    public int par;

    @ColumnInfo(name = "distance")
    public int distance;

    @ColumnInfo(name = "stroke_count")
    public int strokeCount;

    @ColumnInfo(name = "hole_score")
    public int holeScore;

    public int getHoleId() {
        return holeId;
    }

    public void setHoleId(int holeId) {
        this.holeId = holeId;
    }

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

    public int getHoleNumber() {
        return holeNumber;
    }

    public void setHoleNumber(int holeNumber) {
        this.holeNumber = holeNumber;
    }

    public int getPar() {
        return par;
    }

    public void setPar(int par) {
        this.par = par;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getStrokeCount() {
        return strokeCount;
    }

    public void setStrokeCount(int strokeCount) {
        this.strokeCount = strokeCount;
    }

    public int getHoleScore() {
        return holeScore;
    }

    public void setHoleScore(int holeScore) {
        this.holeScore = holeScore;
    }
}
