package com.group10.msa.MapObjects;

import java.util.ArrayList;

public class Agent {

    private float x;
    private float y;
    private float direction;
    private float visionDistance = 75;
    private int[][] world;

    public Agent(float xStart, float yStart, float startDir,int[][] world){
        this.x = xStart;
        this.y = yStart;
        this.direction = startDir;
        this.world = world;
    }

    //calculates x and y components needed to move (potentially diagonally) at 1.4 metres a second
    public void move(float speed){
        System.out.println(" x " + x + " y " + y);
        if( x > 790 || x < 0 || y > 790 || y < 0)
            direction *= Math.PI; // to be deleted, just for testing
        if(direction < Math.PI/2){
            x += speed*Math.sin(direction);
            y += speed*Math.cos(direction);
        }
        if(direction >= Math.PI/2 && direction < Math.PI){
            x += speed*Math.cos(direction - Math.PI/2);
            y -= speed*Math.sin(direction - Math.PI/2);
        }
        if(direction >= Math.PI && direction < (3*Math.PI)/2){
            x -= speed*Math.sin(direction - Math.PI);
            y -= speed*Math.cos(direction - Math.PI);
        }
        if(direction >= (3*Math.PI)/2){
            x -= speed*Math.cos(direction - (3*Math.PI)/2);
            y += speed*Math.sin(direction - (3*Math.PI)/2);
        }
    }

    //turns at PI/60 radians every frame
    //capable of determine which way it is fastest to turn, clockwise or anti-clockwise
    public void turn(float newDir){
        if(direction != newDir){
            if(Math.abs(direction-newDir) > Math.PI/60){
                if(newDir < direction){
                    if(Math.abs(direction-newDir) > Math.PI) {
                        direction += Math.PI / 60;
                    }
                    else{
                        direction -= Math.PI / 60;
                    }
                }
                else if(direction < newDir){
                    if(Math.abs(direction-newDir) > Math.PI) {
                        direction -= Math.PI / 60;
                    }
                    else{
                        direction += Math.PI / 60;
                    }
                }
            }
            else{
                direction = newDir;
            }
        }
        if(direction > 2* Math.PI){
            direction -= 2*Math.PI;
        }
    }



    public float getX(){
        return x;
    }

    public float getY() {
        return y;
    }
    public void newDirection(float newdirection){
        this.direction = newdirection;
    }
    public void changedirection(float change){
        this.direction += change;
    }
    public float getDirection(){ return direction;}
    public float coordToMetres(float coord){
        float metres = (float)0.05*coord;
        return  metres;
    }
    public float metresToCoord(float metres){
        float coord = (float)20*metres;
        return  metres;
    }

    public float[][] vision(){
        float[][] vision = new float[3][2];
        //first point are just our  x and y position
        vision[0][0] = this.x+5;
        vision[0][1] = this.y+5;
        //Next up upper point
        vision[1][0] = (float)(visionDistance*Math.cos((Math.PI/8)+(direction-Math.PI))+x+5);
        vision[1][1] = (float)(visionDistance*Math.sin((Math.PI/8)+(direction-Math.PI))+y+5);
        // last point
        vision[2][0] = (float)(visionDistance*Math.cos(-(Math.PI/8)+(direction-Math.PI))+x+5);
        vision[2][1] = (float)(visionDistance*Math.sin(-(Math.PI/8) +(direction-Math.PI))+y+5);
        for(float[] i : vision){
            System.out.println(" " + i[0] + " " + i[1]);
        }
        //visionField(vision);

        return vision;
    }
    public ArrayList<float[]> visionField(float[][] vision){
        //find biggest and smallest x,y values
        int maxX = 0, maxY = 0;
        int minX = 79, minY = 79;
        for(float[] i : vision) {
            if (((int) i[0] / 10) > maxX) maxX = ((int) i[0] / 10);
            if (((int) i[0] / 10) < minX) minX = ((int) i[0] / 10);
            if (((int) i[1] / 10) > maxY) maxY = ((int) i[1] / 10);
            if (((int) i[1] / 10) < minY) minY = ((int) i[1] / 10);
        }
        maxX += 1;
        maxY +=1;
        System.out.println("minx "+ minX + " miny " + minY + " maxx " + maxX + " maxy " +maxY );
        ArrayList<float[]> pointsVision = new ArrayList<float[]>();
        //starting from the smallest
        int[][] agentsVision = new int[maxX-minX+1][maxY-minY+1];
        int count = 0;
        for(int i = maxX; i >= minX;i--){
            for(int j = maxY; j >= minY;j--){
                float[] p= {i*10,j*10};
                //System.out.println(p[0] + " "+ p[1]);
                if(p[0]>=0 &&p[0] < 800 && p[1] >=0 && p[1] < 800 &&
                        isInVisionField(p, vision[0], vision[1],vision[2])){

                    pointsVision.add(p);
                    agentsVision[i-minX][j-minY] = world[i][j];
                    count++;
                }
            }
        }

        System.out.println(count);
        for(int i = 0; i < agentsVision.length; i ++){
            for(int j = 0; j < agentsVision[0].length; j++){
                System.out.print(agentsVision[i][j]);
            }
            System.out.println();
        }
        System.out.println("DIRECTION " + direction);
        return pointsVision;

    }
    public boolean isInVisionField(float[] point, float[]a,float[]b, float[] c){
        //vector from a to point, normalized to length 1
        float[] a_point = {(point[0]-a[0]),(point[1]-a[1])};
        float temp = (float)Math.sqrt((a_point[0]*a_point[0])+(a_point[1]*a_point[1]));
         if(temp > visionDistance) return false;
        a_point[0] = a_point[0]/temp;
        a_point[1] = a_point[1]/temp;

        //vector from a to b,normalized to
        float[]ab = {(b[0]-a[0]),(b[1]-a[1])};
        temp = (float) Math.sqrt((ab[0]*ab[0])+(ab[1]*ab[1]));
        ab[0] = ab[0]/temp;
        ab[1] = ab[1]/temp;
        //vector from a to c, normalized to length 1
        float[]ac = {(c[0]-a[0]),(c[1]-a[1])};
        temp = (float) Math.sqrt((ac[0]*ac[0])+(ac[1]*ac[1]));
        ac[0] = ac[0]/temp;
        ac[1] = ac[1]/temp;

        float angleAB = (float)Math.acos((a_point[0]*ab[0])+(a_point[1]*ab[1]));
       float angleAC =  (float)Math.acos((a_point[0]*ac[0])+(a_point[1]*ac[1]));
        if(angleAB >= 0 && angleAB <= (Math.PI/4) && angleAC >= 0 && angleAC <= (Math.PI/4)){

            return true;}
        return false;
    }


}
