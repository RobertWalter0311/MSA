package com.group10.msa.MapObjects;


import java.sql.Time;
import java.util.ArrayList;
import java.util.Timer;

public class Guard extends Agent {

    private float direction;
    ArrayList<Tuple> waypoints = new ArrayList<Tuple>();
    private int i = 2;
    private boolean firstTime = true;
    private int[][] world;
    private int[][] coverageWorld;
    private ArrayList agentList;

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
        //trafficWaypoints();
    }

    @Override
    public void plan() {
        if(firstTime){
             //coverageWorld = new int[world[0].length][world.length];
        }
        if(agentTracker()){

        }
        else {


            //createCoverage();
//        if(!inProximity(35, 35)) {
//            aStarHeadTo(35, 35);
//        }
            patrol();
        }
        setAudioRadius();
        firstTime = false;
    }

    public void createCoverage(){
        int currentX = (int)((this.getX()+5)/10);
        int currentY = (int)((this.getY()+5)/10);
        for (int j = -4; j < 5; j++) {
            for (int k = -4; k < 5; k++) {
                if(currentX + k < coverageWorld[0].length && currentX + k >= 0 && currentY + j < coverageWorld.length && currentY + j >= 0){
                    coverageWorld[currentX+k][currentY+j] = 1;
                }
            }
        }
        int amount = 0;
        for (int j = 0; j < coverageWorld.length; j++) {
            for (int k = 0; k < coverageWorld[0].length; k++) {
                if(coverageWorld[k][j] == 1){
                    amount++;
                }
            }
        }
        System.out.println(amount);
    }

    public void patrol(){
        try {
            if (!inProximity(waypoints.get(i).waypointX, waypoints.get(i).waypointY)) {
                if(!aStarHeadTo(waypoints.get(i).waypointX, waypoints.get(i).waypointY)){
                    i++;
                }
            }
            else{
                i = (int)((waypoints.size()-1)*Math.random());
            }
        }
        catch (IndexOutOfBoundsException exception){
            System.out.println("for some reason, out of bounds");
            move();
        }
//        else if(i<waypoints.size() - 1){
//            if(Math.abs(getX()-waypoints.get(i+1).waypointX) < 22 && Math.abs(getY()-waypoints.get(i+1).waypointY) < 22){
//                i++;
//            }
//            i++;
//        }


    }

    public void placeWaypoint(int tileX, int tileY){
        Tuple t = new Tuple(10*tileX + 5, 10*tileY + 5);
        waypoints.add(t);
    }

    public void randomWaypoints(){
        for (int j = 2; j < world.length-2; j++) {
            for (int k = 2; k < world[0].length-2; k++) {
                double random = Math.random()*1;
                if(random < 0.156 && world[k][j] != 9){
                    System.out.println("place waypoint" + k + " " + j);
                    placeWaypoint(k,j);
                }
            }
        }
//        placeWaypoint(10,5);
//        placeWaypoint(50,70);
    }

    public void trafficWaypoints() {
        double time1  = System.currentTimeMillis();
        ArrayList pathList = new ArrayList();
        for (int j = 2; j < world.length - 2; j += 5) {
            for (int k = 2; k < world[0].length - 2; k += 5) {
                if (world[k][j] != 9) {
                    float randomDestX = (float) ((Math.random() * (0.95) * 800) + 20);
                    float randomDestY = (float) ((Math.random() * (0.95) * 800) + 20);
                    ArrayList newPath = aStarTraffic(k, j, randomDestX, randomDestY);
                    pathList.add(newPath);
                }
            }
        }
        int[][] checkingMap = new int[80][80];
        for (int j = 0; j < pathList.size(); j++) {
            try {
                for (int k = 0; k < ((ArrayList) (pathList.get(j))).size(); k++) {
                    int xc = ((Node) (((ArrayList) (pathList.get(j))).get(k))).getXcoords();
                    int yc = ((Node) (((ArrayList) (pathList.get(j))).get(k))).getYcoords();
                    checkingMap[xc][yc]++;
                }
            } catch (NullPointerException exception) {
            }
        }
        for (int j = 0; j < checkingMap.length; j++) {
            for (int k = 0; k < checkingMap[0].length; k++) {
                System.out.print(checkingMap[k][j] + " ");
            }
            System.out.println();
        }
        int[][] mostTravelled = find10Highest(checkingMap, 10);
        for (int j = 0; j < mostTravelled.length; j++) {
//            if(Math.random()*1<0.3333) {
                placeWaypoint(mostTravelled[j][0], mostTravelled[j][1]);
//            }
        }
        double time2 = System.currentTimeMillis();
        System.out.println(time2-time1);
    }

    public int[][] find10Highest(int[][] checkingMap, int choiceSize){
        int m;
        int large[] = new int[choiceSize];
        int array[][] = checkingMap;
        int max = 0;
        int[] index = {0,0};
        int[][] indices = new int[choiceSize][2];
        for (int j = 0; j < choiceSize; j++) {
            max = array[0][0];
            index[0] = 0;
            index[1] = 0;
            for (m = 0; m < array.length; m++) {
                for (int k = 0; k < array[0].length; k++) {
                    if (max < array[k][m]) {
                        if(world[k][m] == 9){
                            array[k][m] = Integer.MIN_VALUE;
                        }
                        if(world[k][m] != 9) {
                            max = array[k][m];
                            index[0] = k;
                            index[1] = m;
                        }
                    }
                }

            }
            large[j] = max;
            array[index[0]][index[1]] = Integer.MIN_VALUE;
            
            System.out.println("Largest " + j +  ": " + large[j]);
            indices[j][0] = index[0];
            indices[j][1] = index[1];
            System.out.println(indices[j][0] + "  " + indices[j][1]);
        }
        return indices;
    }

    public int[][] find10Lowest(int[][] checkingMap, int choiceSize){
        int m;
        int small[] = new int[choiceSize];
        int array[][] = checkingMap;
        int min = 10;
        int[] index = {0,0};
        int[][] indices = new int[choiceSize][2];
        for (int j = 0; j < choiceSize; j++) {
            if(world[0][0] == 9){
                array[0][0] = Integer.MAX_VALUE;
            }
            min = array[0][0];
            index[0] = 0;
            index[1] = 0;
            for (m = 0; m < array.length; m++) {
                for (int k = 0; k < array[0].length; k++) {
                    if(world[k][m] == 9 || k<2 || k>77 || m<2 || m>77 || array[k][m] == 0){
                        array[k][m] = Integer.MAX_VALUE;
                    }
                    if (min > array[k][m]) {
                        min = array[k][m];
                        index[0] = k;
                        index[1] = m;
                    }
                }

            }
            small[j] = min;
            array[index[0]][index[1]] = Integer.MAX_VALUE;

            System.out.println("Smallest " + j +  ": " + small[j]);
            indices[j][0] = index[0];
            indices[j][1] = index[1];
            System.out.println(indices[j][0] + "  " + indices[j][1]);
        }
        return indices;
    }







    public int[] findCurrentArea(int[][] exploredArea){
        float x = this.getX();
        float y = this.getY();
        int currentAreaPosX = (int)(x/10);
        int currentAreaPosY = (int)(y/10);
        int[] coords = {currentAreaPosX, currentAreaPosY};
        return coords;
    }

    @Override
    public void setAgentList(ArrayList aList){
        this.agentList = aList;
    }

    public boolean agentTracker(){
        if(agentList!=null){
            for (int j = 0; j < agentList.size(); j++) {
                System.out.println("Agents:");
                System.out.println(((Agent)(agentList.get(j))).getX() + "      " + ((Agent)(agentList.get(j))).getY());
                if(radiusDetection(this, ((Agent)(agentList.get(j))))){
                    System.out.println("found agent");
                    aStarHeadTo(((Agent)(agentList.get(j))).getX(), ((Agent)(agentList.get(j))).getY());
                    if(Math.abs(((Agent)(agentList.get(j))).getX()-this.getX()) <0.5 && Math.abs(((Agent)(agentList.get(j))).getY()-this.getY()) < 0.5){
                        System.out.println("Agent caught");
                        System.exit(-1);
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
