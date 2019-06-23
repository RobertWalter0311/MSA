package com.group10.msa.MapObjects;


import java.sql.Time;
import java.util.ArrayList;
import java.util.Timer;

public class Guard extends Agent {

    private float direction;
    ArrayList<Tuple> waypoints = new ArrayList<Tuple>();
    private int i = 0;
    private boolean firstTime = true;
    private boolean explored = false;
    private int[][] world;
    private int[][] coverageWorld = new int[80][80];
    private ArrayList agentList;
    private Radio radio = new Radio();
    private double timeStart = 0;
    private boolean isInTower = false;
    public static boolean directComms = true;
    public static ArrayList<GuardTrail> trailList = new ArrayList<GuardTrail>();
    private double startTime = System.currentTimeMillis();
    public int ans[]= new int[2];



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

        //randomWaypoints();
        distributedWaypoints();
        //trafficWaypoints();
    }

    @Override
    public void plan() {

        if(!explored){
            if(bruteExplore()){
                explored = true;
            }
        }

        for (int j = 0; j <agentsworld[0].length ; j++) {
            for (int k = 0; k < agentsworld.length; k++) {
                System.out.print(agentsworld[k][j]);
            }
            System.out.println();
        }



        /*if(firstTime){
             //coverageWorld = new int[world[0].length][world.length];
        }


            speed = 1.4f;

//        if(agentTracker()){
//
//        }
//        else {
            //createCoverage();
//        if(!inProximity(35, 35)) {
//            aStarHeadTo(35, 35);
//        }
            patrol();
            createCoverage();
        //}
        setAudioRadius();
        firstTime = false;*/
    }

    public void createCoverage(){
        int currentX = (int)((this.getX()+5)/10);
        int currentY = (int)((this.getY()+5)/10);
        for (int j = -9; j < 10; j++) {
            for (int k = -9; k < 10; k++) {
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
        System.out.println(System.currentTimeMillis() - startTime);
    }

    public void patrol(){
        try {
            if (!inProximity(waypoints.get(i).waypointX, waypoints.get(i).waypointY)) {
                if(!aStarHeadTo(waypoints.get(i).waypointX, waypoints.get(i).waypointY)){
                    i++;
                }
            }
            else{
                //i = (int)((waypoints.size()-1)*Math.random());
                i++;
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

    public void distributedWaypoints(){
        for (int j = 2; j < world.length-2; j++) {
            for (int k = 2; k < world[0].length-2; k++) {
                if(j%10==0 && k%10==0 && world[k][j] != 9){
                    System.out.println("place waypoint" + k + " " + j);
                    placeWaypoint(k,j);
                }
            }
        }
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
                if(radiusDetection(this, ((Agent)(agentList.get(j)))) && (agentList.get(j)) instanceof Intruder){
                    System.out.println("found agent");


                    if(directComms) {
                        alertMessage(((Agent) (agentList.get(j))).getX(), ((Agent) (agentList.get(j))).getY());


                        if (probeLine(radio.getPos()[0], radio.getPos()[1])) {
                            swerveTo(radio.getPos()[0], radio.getPos()[1]);
                        } else {
                            aStarHeadTo(radio.getPos()[0], radio.getPos()[1]);
                        }
                    }

                    else if(!directComms){
                        GuardTrail trailNode = new GuardTrail(this, this.getX(), this.getY());

                        trailList.add(trailNode);

                        if (probeLine(((Agent)(agentList.get(j))).getX(), ((Agent)(agentList.get(j))).getY())) {
                            swerveTo(((Agent)(agentList.get(j))).getX(), ((Agent)(agentList.get(j))).getY());
                        } else {
                            aStarHeadTo(((Agent)(agentList.get(j))).getX(), ((Agent)(agentList.get(j))).getY());
                        }
                    }
                    if(Math.abs(((Agent)(agentList.get(j))).getX()-this.getX()) <1 && Math.abs(((Agent)(agentList.get(j))).getY()-this.getY()) < 1){
                        System.out.println("Agent caught");
                        //System.exit(-1);
                    }
                    return true;
                }
            }
        }
        if(directComms) {
            if (radio.getAlert()) {
                System.out.println("head to reported position");
                if (probeLine(radio.getPos()[0], radio.getPos()[1])) {
                    swerveTo(radio.getPos()[0], radio.getPos()[1]);
                } else {
                    aStarHeadTo(radio.getPos()[0], radio.getPos()[1]);
                }
                return true;
            }
        }
        else if(!directComms){
            if(lookForTrail()) {
                return true;
            }
        }

        return false;
    }

    public void alertMessage(float xpos, float ypos){
        float[] xyPos = {xpos, ypos};

        radio.setPos(xyPos);
        radio.setAlert(true);
    }



    public void inTower(){
        speed = 0;
    }

    public boolean lookForTrail(){
        GuardTrail maxNumber = new GuardTrail(this, -1000, -1000);
        maxNumber.setNumber(-1);
        for (int j = 0; j < trailList.size(); j++) {
            if(Math.abs(trailList.get(j).getX()-this.getX()) <100 && Math.abs(trailList.get(j).getY()-this.getY()) <100 && trailList.get(j).isExplored() == false && trailList.get(j).getTheGuard()!=this){
                System.out.println("Found trail");
                if(trailList.get(j).getNumber() > maxNumber.getNumber()){
                    maxNumber = trailList.get(j);
                    trailList.get(j).setExplored();
                }
            }
        }
        if(maxNumber.getX()>-1) {
            if (probeLine(maxNumber.getX(), maxNumber.getY())) {
                swerveTo(maxNumber.getX(), maxNumber.getY());
                return true;
            } else {
                aStarHeadTo(maxNumber.getX(), maxNumber.getY());
                return true;
            }
        }
        return false;
    }

    public void enterTower(){
        double nowTime = System.currentTimeMillis();

        if(timeStart == 0){
            timeStart = nowTime;
        }
        else if(nowTime-timeStart>3000) {
            isInTower = true;
            timeStart = 0;
        }
    }

    public void exitTower(){
        double nowTime = System.currentTimeMillis();

        if(timeStart == 0){
            timeStart = nowTime;
        }
        else if(nowTime-timeStart>3000) {
            isInTower = false;
            timeStart = 0;
        }
    }

    public boolean bruteExplore() {

        int posX = Math.round(this.getX() / 10);
        int posY = Math.round(this.getY() / 10);

        double distance = 30000;
        int minDX = 69;
        int minDY = 69;


//        int h = 3;
//
//        int tempI = posX - h;
//        if (tempI < 1)
        int tempI = 0;
//
//        int rangeI = posX + h;
//        if (rangeI > 78)
        int rangeI = 79;

//        int tempJ = posY - h;
//        if (tempJ < 1)
        int tempJ = 0;

//        int rangeJ = posY + h;
//        if (rangeJ > 78)
        int rangeJ = 79;
        int count = 0;

        for (int g = tempI; g < rangeI; g++) {
            for (int j = tempJ; j < rangeJ; j++) {

                float tempX = Math.abs(j-(this.getX()/10));
                float tempY = Math.abs(g-(this.getY()/10));

                double tempDistance = Math.sqrt((tempX*tempX)+(tempY*tempY));
                System.out.print(tempDistance + " ");

                if((j>0 && j<79)&&(g>0 && j<79)){
                if (tempDistance<distance && coverageWorld[j][g]==0 && world[j][g] != 9){
                    distance = tempDistance;
                    //System.out.println("I liike jeff");
                    minDX=j;
                    minDY=g;

                }
                }

                if(agentsworld[j][g] == 0){
                    count++;
                }
                //System.out.println(h);
                //if (coverageWorld[j][i] == 0) {

                    int k = g * 10;
                    if (k <= 10)
                        k = 11;
                    //System.out.println("k " + k);
                    int l = j * 10;
                    if (l <= 10)
                        l = 11;
                    //System.out.println("l " + l);



                        //for (int m = 0; m < world.length; m++) {
                            //for (int n = 0; n < world[0].length; n++) {
                              //  System.out.print(agentsworld[n][m] + " ");
                            //}
                            //System.out.println();
                        //}

                        //System.out.println("________________");




                                float[] target = {10*j, 10*g};
                                float[] currentPos = {this.getX(), this.getY()};
                                float[][] visionCorners = vision();
                                float[] b = {visionCorners[1][0], visionCorners[1][1]};
                                float[] c = {visionCorners[2][0], visionCorners[2][1]};

                                if(isInVisionField(target, currentPos, c, b)) {
                                    coverageWorld[j][g] = 1;
                                }





                //} else {
                    //h++;
                //}
            }
            System.out.println();
        }
        System.out.println("count " + count);
        if(count <=1) {
            System.out.println("It's done >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ;)");
            agentsworld = fillInBlanks(agentsworld);
            return true;
        }
        else{
        }
        if (!inProximity(minDX*10, minDY*10)) {
            System.out.println("Current destination: " + minDX + " , " + minDY + "    " + distance);
            if(probeLine(minDX*10, minDY*10)){
                swerveTo(minDX*10, minDY*10);
            }
            else {
                aStarHeadTo(minDX * 10, minDY * 10);
            }

        }

        System.out.println("make 1");

        if(inProximity(minDX*10, minDY*10)) {
            coverageWorld[minDX][minDY] = 1;
        }
        return false;
    }

    public int[][] fillInBlanks(int[][] givenWorld){
        for (int j = 0; j < givenWorld[0].length; j++) {
            for (int k = 0; k < givenWorld.length; k++) {
                if(givenWorld[k][j] == 0){
                    givenWorld[k][j] = 9;
                }
                System.out.print(givenWorld[k][j]);
            }
            System.out.println();
        }

        return givenWorld;
    }

}
