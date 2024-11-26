package com.siriwardana.whatsmyhandicap;

public class RoundData {
    int hole;
    int par;
    int strokes;
    int totalStrokes;

    public RoundData(int hole, int par, int strokes, int totalStrokes) {
        this.hole = hole;
        this.par = par;
        this.strokes = strokes;
        this.totalStrokes = totalStrokes;
    }

    public int getHole() {
        return hole;
    }

    public void setHole(int hole) {
        this.hole = hole;
    }

    public int getPar() {
        return par;
    }

    public void setPar(int par) {
        this.par = par;
    }

    public int getStrokes() {
        return strokes;
    }

    public void setStrokes(int strokes) {
        this.strokes = strokes;
    }

    public int getTotalStrokes() {
        return totalStrokes;
    }

    public void setTotalStrokes(int totalStrokes) {
        this.totalStrokes = totalStrokes;
    }
}
