package com.group10.msa.MapObjects;

public class GuardTrail {

    private Guard theGuard;
    private float x;
    private float y;
    private int number;
    private boolean explored = false;
    public static int iterations = 0;

    public GuardTrail(Guard theGuard, float x, float y){
        this.theGuard = theGuard;
        this.x = x;
        this.y = y;
        this.number = iterations;
        iterations++;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Guard getTheGuard() {
        return theGuard;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setExplored() {
        explored = true;
    }

    public boolean isExplored() {
        return explored;
    }
}
