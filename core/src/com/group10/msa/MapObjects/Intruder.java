package com.group10.msa.MapObjects;

public class Intruder extends Agent {
    private int destination = 0;
    public Intruder(float xStart, float yStart, float startDir,int[][] world) {
        super(xStart, yStart, startDir,world);
    }

    @Override
    public void plan() {

        speed = 2.2f;
        if(destination == 0) {
            if (!inProximity(700, 700)) {
                aStarHeadTo(700, 700);
            }
            else{
                destination = 1;
            }
        }
        else{
            if (!inProximity(700, 100)) {
                aStarHeadTo(700, 100);
            }
            else{
                destination = 0;
            }
        }
        setAudioRadius();

    }

}
