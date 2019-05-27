package com.group10.msa.MapObjects;


import java.util.ArrayList;

public class Guard extends Agent {

    private float direction;
    ArrayList<Tuple> waypoints = new ArrayList<Tuple>();
    private int i = 0;
    private boolean firstTime = true;
    private int[][] world;
    class Tuple {
        float waypointX, waypointY;
        Tuple(float waypointX, float waypointY){
            this.waypointX = waypointX;
            this.waypointY = waypointY;
        }
    }


    public Guard(float xStart, float yStart, float startDir,int[][] world) {
        super(xStart, yStart, startDir,world);
        this.world = world;
        this.direction = startDir;
        randomWaypoints();
    }

    @Override
    public void plan() {
        if(firstTime){

        }
        patrol();
        setAudioRadius();
        firstTime = false;

    }

    public void patrol(){
        if(Math.abs(getX()-waypoints.get(i).waypointX) > 2 && Math.abs(getY()-waypoints.get(i).waypointY) > 2) {
            aStarHeadTo(waypoints.get(i).waypointX, waypoints.get(i).waypointY);
        }
        else if(i<waypoints.size()){
            i++;
        }
        else{
            i = 0;
        }

    }

    public void placeWaypoint(int tileX, int tileY){
        Tuple t = new Tuple(10*tileX + 5, 10*tileY + 5);
        waypoints.add(t);
    }

    public void randomWaypoints(){
        for (int j = 0; j < world.length; j++) {
            for (int k = 0; k < world[0].length; k++) {
                double random = Math.random()*1;
                if(random < 0.005 && world[k][j] != 9){
                    System.out.println("place waypoint" + k + " " + j);
                    placeWaypoint(k,j);
                }
            }
        }
//        placeWaypoint(10,5);
//        placeWaypoint(50,70);
    }





    public int[] findCurrentArea(int[][] exploredArea){
        float x = this.getX();
        float y = this.getY();
        int currentAreaPosX = (int)(x/10);
        int currentAreaPosY = (int)(y/10);
        int[] coords = {currentAreaPosX, currentAreaPosY};
        return coords;
    }
}
