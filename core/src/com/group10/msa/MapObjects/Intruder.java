package com.group10.msa.MapObjects;

public class Intruder extends Agent {
    public Intruder(float xStart, float yStart, float startDir,int[][] world) {
        super(xStart, yStart, startDir,world);
    }

    @Override
    public void plan() {

        speed = 1.4f;

        if(!inProximity(400, 100)) {
            aStarHeadTo(400, 100);
        }
        setAudioRadius();

    }

}
