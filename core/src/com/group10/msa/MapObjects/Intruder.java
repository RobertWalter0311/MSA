package com.group10.msa.MapObjects;

public class Intruder extends Agent {
    public Intruder(float xStart, float yStart, float startDir,int[][] world) {
        super(xStart, yStart, startDir,world);
    }

    @Override
    public void plan() {


        if(!inProximity(340, 600)) {
            aStarHeadTo(340, 600);
        }
        setAudioRadius();

    }

}
