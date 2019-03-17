package com.group10.msa.MapObjects;

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
        vision[1][0] = (float)(visionDistance*Math.cos((Math.PI/8)+direction)+x+5);
        vision[1][1] = (float)(visionDistance*Math.sin((Math.PI/8)+direction)+y+5);
        // last point
        vision[2][0] = (float)(visionDistance*Math.cos(-(Math.PI/8)+direction)+x+5);
        vision[2][1] = (float)(visionDistance*Math.sin(-(Math.PI/8) +direction)+y+5);
        float[] point = {80,80};
        visionField(vision);
        //System.out.println(isInVisionField(point, vision[0],vision[1]));
        return vision;
    }
    private void visionField(float[][] vision){
        //find biggest and smallest x,y values
        int maxX = 0, maxY = 0;
        int minX = 79, minY = 79;
        for(float[] i : vision) {
            if (((int) i[0] / 10) > maxX) maxX = ((int) i[0] / 10);
            if (((int) i[0] / 10) < minX) minX = ((int) i[0] / 10);
            if (((int) i[1] / 10) > maxY) maxY = ((int) i[1] / 10);
            if (((int) i[1] / 10) < minY) minY = ((int) i[1] / 10);
        }
        //starting from the smallest
        int[][] agentsVision = new int[maxX-minX+1][maxY-minY+1];
        int count = 0;
        for(int i = maxX; i >= minX;i--){
            for(int j = maxY; j >= minY;j--){
                float[] p= {i*10,j*10};
                if(isInVisionField(p, vision[0], vision[1])){
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

    }
    public boolean isInVisionField(float[] point, float[]a,float[]b){
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



        float angle = (float)Math.acos((a_point[0]*ab[0])+(a_point[1]*ab[1]));
       angle = (float) Math.toDegrees(angle);
       //System.out.println("ANGLE" + angle);
        //if(angle > 0 && angle <= (Math.PI/4)){
        if(angle >= 0 && angle <= 45){
            return true;}
        return false;
    }


}
