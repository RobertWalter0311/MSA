package com.group10.msa.MapObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;

public class Agent {

    private float x;
    private float y;
    private float direction;
    //private boolean walking = true;
    public float speed;
    public float visionDistance = 150;
    public float visionDegree = (float)(Math.PI/4);
    private int[][] world;
    public Sprite sprite;
    public float audioRadius = 20;

    public Agent(float xStart, float yStart, float startDir,int[][] world){
        this.x = xStart;
        this.y = yStart;
        this.direction = startDir;
        this.world = world;
        Texture agent = new Texture(Gdx.files.internal("data/CropAgent.jpg"));
        TextureRegion tagent = new TextureRegion(agent,10,10);
        this.sprite = new Sprite(tagent);
        speed = 0;
    }

    //calculates x and y components needed to move (potentially diagonally) at 1.4 metres a second
    public void move(){
        //setAudioRadius();
        //System.out.println(" x " + x + " y " + y);
        //if( x > 790 || x < 0 || y > 790 || y < 0)
          //direction *= Math.PI; // to be deleted, just for testing
        int tempx = (int)(x+5+(metresToCoord(speed)*Math.cos(direction)));
        int tempy = (int)(y+5+(metresToCoord(speed)*Math.sin(direction)));
        tempx *=0.1;
        tempy *= 0.1;

        if(world[tempx][tempy] == 9) {
            System.out.println("TUUUURRRRN" + direction);

            turn ((float)Math.PI+ direction);
        }
        else{
            speed= 1.4f;
            x += metresToCoord(speed)*Math.cos(direction);
            y+= metresToCoord(speed)*Math.sin(direction);
        }


    }

    //turns at PI/60 radians every frame
    //capable of determine which way it is fastest to turn, clockwise or anti-clockwise
    public void turn(float newDir){
        if(direction != newDir){
            float turnSpeed = (float)(Math.PI / 60);
            if(speed <= 1.4f){
                turnSpeed = (float)(Math.PI / 60);
            }
            else{
                turnSpeed = (float)((1f/18f)*(Math.PI / 60f));
            }
            if(Math.abs(direction-newDir) > turnSpeed){
                if(newDir < direction) direction -= turnSpeed;
                else if(direction < newDir) direction += turnSpeed;
            }
            else{
                direction = newDir;
            }
        }

        if(direction > 2* Math.PI){
            direction -= 2*Math.PI;

        }
        else if(direction < 0){
            direction += 2* Math.PI;

        }
    }
    //experimental, moves straight to a target area
    public void headTo(float objx, float objy){
        speed = metresToCoord(1.4f);

        if(direction != getAngle(objx,objy)) {
            turn(getAngle(objx,objy));
        }

        else{
            if(Math.abs(x-objx) < 1 && Math.abs(y-objy) < 1) {
                speed = 0;
            }
        }

    }

    public void swerveTo(MapObject object){

        float objectX = object.getPos().x;
        float objectY = object.getPos().y;
        if(direction != getAngle(object)) {
            //speed = 1.4f;
            turn(getAngle(object));
        }
        if(Math.abs(x-objectX) < 1&& Math.abs(y-objectY) <1 ) {
            speed = 0;
        }


    }
    //finds angle of location relative to agent
    public float getAngle(MapObject object) {
        float[] obj = {(object.getPos().x-x), (object.getPos().y-y)};
        float temp = (float) Math.sqrt(obj[0]*obj[0] + obj[1]*obj[1]);
        obj[0] = obj[0]/temp;
        obj[1] = obj[1]/temp;

        float[] us={(x+5) - x,y -y};
        float temo = (float) Math.sqrt(us[0]*us[0] + us[1]*us[1]);
        us[0] = us[0]/temo;
        us[1] = us[1]/temo;

        float angle= (float)Math.acos((obj[0]*us[0])+(obj[1]*us[1]));
        return angle;
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
        return (float)20*metres/Gdx.graphics.getFramesPerSecond();
    }

    public float[][] vision(){
        float[][] vision = new float[3][2];
        //first point are just our  x and y position
        vision[0][0] = this.x+5;
        vision[0][1] = this.y+5;
        //Next up upper point
        vision[1][0] = (float)(visionDistance*Math.cos((visionDegree/2)+direction)+x+5);
        vision[1][1] = (float)(visionDistance*Math.sin((visionDegree/2)+direction)+y+5);
        // last point
        vision[2][0] = (float)(visionDistance*Math.cos(-(visionDegree/2)+direction)+x+5);
        vision[2][1] = (float)(visionDistance*Math.sin(-(visionDegree/2)+direction)+y+5);
       // visionField(vision);

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
        ArrayList<float[]> points = new ArrayList<float[]>();
        maxX += 1;
        maxY +=1;
        //System.out.println("minx "+ minX + " miny " + minY + " maxx " + maxX + " maxy " +maxY );
        //starting from the smallest
        int[][] agentsVision = new int[maxX-minX+1][maxY-minY+1];
        int count = 0;
        for(int i = maxX; i >= minX;i--){
            for(int j = maxY; j >= minY;j--){
                float[] p= {i*10,j*10};
                //System.out.println(p[0] + " "+ p[1]);
                if(p[0]>=0 &&p[0] < 800 && p[1] >=0 && p[1] < 800 &&
                        isInVisionField(p, vision[0], vision[1],vision[2])){
                    agentsVision[i-minX][j-minY] = world[i][j];
                    count++;
                    points.add(p);
                }
            }
        }
        for(int i = 0; i < agentsVision.length; i++){
            for(int j = 0; j < agentsVision[0].length;j++){
                if(agentsVision[i][j] == 0){
                    for(int k = 0; k < 10; k++){
                        for(int l = 0; l < 10; l++){
                            float[] p = {(((minX+i)*10)+k),(((minY+j)*10)+l)};
                            if(p[0]>=0 &&p[0] < 800 && p[1] >=0 && p[1] < 800 &&isInVisionField(p, vision[0],vision[1], vision[2])){
                                agentsVision[i][j] = world[minX+i][minY+j];
                                k = 10;
                                l=10;
                                count++;
                                points.add(p);
                            }
                        }
                    }
                }
            }
        }
        agentsVision[((int)vision[0][0]/10)-minX][((int)vision[0][1]/10)-minY] = 9;
        //System.out.println("Direction" + direction);
        /*for(int i = 0; i < agentsVision.length;i++){
            for(int j = 0; j < agentsVision[0].length; j++){
                System.out.print(agentsVision[i][j]);
            }
            System.out.println();
        }*/
        collisionDetection(agentsVision,minX,minY);
        return points;
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

    public void setAudioRadius(){
        if(speed < 0.5) audioRadius = 20;
        else if (speed < 1) audioRadius = 60;
        else if (speed < 2) audioRadius  = 100;
        else audioRadius = 200;
    }
    public float normalNoiseDetection(float dx,float dy){
        float direc = getAngle(dx,dy);
        float probab = (float) Math.random();
        if(probab < 0.6827 ){
            float rando = (float) (-10 + (Math.random() * ((10 - (-10)) + 1)));
            System.out.println("RESULT " +(direc +(float)(Math.toRadians(rando))));
            return direc +(float)(Math.toRadians(rando));
        }
        else if (probab < 0.9545){
            float rando = (float) (-20+ (Math.random() * ((20 - (-20)) + 1)));
            return direction +(float)(Math.toRadians(rando));
        }
        else if (probab < 0.9973){
            float rando = (float) (-30 + (Math.random() * ((30 - (-30)) + 1)));
            return direc +(float)(Math.toRadians(rando));
        }
        else{
            float rando = (float) (-40 + (Math.random() * ((40 - (-40)) + 1)));
            return direc +(float)(Math.toRadians(rando));
        }
    }

    public float getAngle(float noisx, float noisy) {
        float[] obj = {(noisx-x), (noisy-y)};
        float temp = (float) Math.sqrt(obj[0]*obj[0] + obj[1]*obj[1]);
        obj[0] = obj[0]/temp;
        obj[1] = obj[1]/temp;

        //horizontal line
        float[] us={1,0};//{(x+5) - x,y -y};
        float temo = (float) Math.sqrt(us[0]*us[0] + us[1]*us[1]);
        us[0] = us[0]/temo;
        us[1] = us[1]/temo;

        float angle= (float)Math.acos((obj[0]*us[0])+(obj[1]*us[1]));
        if(noisy < y) angle = (float) (2*Math.PI) - angle;
        //System.out.println(angle);
        return angle;
    }

    public void collisionDetection(int[][] agentsVision, int minX, int minY){
        for(int i = 0; i < agentsVision.length; i++){
            for (int j = 0; j < agentsVision[0].length; j++){
                if( agentsVision[i][j] == 9){
                    if(getAngle((minX+i)*10,(minY+j)*10)== direction ){
                        System.out.println("i'm going to hit that wall");
                    }
                }
                if (agentsVision[i][j] == 1){
                    turn(getAngle((minX+i)*10, (minY+j)* 10));
                }
            }
        }
    }
    public void plan (){
        if(world[(int)x/10][(int)y/10] == 1) speed = 0;
        else move();
        setAudioRadius();
        //

    }

}
