package com.siriwardana.whatsmyhandicap.helpers;

import com.siriwardana.whatsmyhandicap.database.Hole;
import com.siriwardana.whatsmyhandicap.database.Round;

import java.util.List;

public class RoundData {
    List<Hole> holes;

    Round round;

    public RoundData(List<Hole> holes, Round round) {
        this.holes = holes;
        this.round = round;
    }

    public List<Hole> getHoles() {
        return holes;
    }

    public void setHoles(List<Hole> holes) {
        this.holes = holes;
    }

    public Round getRound() {
        return round;
    }

    public void setRound(Round round) {
        this.round = round;
    }
}
