package com.group10.msa.MapObjects;

public class Agent {

    private float x;
    private float y;
    private float direction;
    private boolean walking = true;
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
        if(speed == metresToCoord((float)1.4)){
            walking = true;
        }
        else{
            walking = false;
        }
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
            float turnSpeed = (float)(Math.PI / 60);
            if(walking == true){
                turnSpeed = (float)(Math.PI / 60);
            }
            else{
                turnSpeed = (float)((1f/18f)*(Math.PI / 60f));
                System.out.println(turnSpeed);
            }
            if(Math.abs(direction-newDir) > turnSpeed){
                if(newDir < direction){
                    if(Math.abs(direction-newDir) > Math.PI) {
                        direction += turnSpeed;
                    }
                    else{
                        direction -= turnSpeed;
                    }
                }
                else if(direction < newDir){
                    if(Math.abs(direction-newDir) > Math.PI) {
                        direction -= turnSpeed;
                    }
                    else{
                        direction += turnSpeed;
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
        else if(direction < 0){
            direction += 2* Math.PI;
        }
    }
    //experimental, moves straight to a target area
    public void headTo(MapObject object){
        float objectX = object.getPos().x;
        float objectY = object.getPos().y;
        if(direction != getAngle(object)) {
            turn(getAngle(object));
        }

        else{
            if(Math.abs(x-objectX) > 1 && Math.abs(y-objectY) > 1) {
                move(metresToCoord(1.4f));
            }
        }

    }

    public void swerveTo(MapObject object){
        float objectX = object.getPos().x;
        float objectY = object.getPos().y;
        if(direction != getAngle(object)) {
            turn(getAngle(object));
            System.out.println(getAngle(object));
        }


        if(Math.abs(x-objectX) > 1 && Math.abs(y-objectY) > 1 ) {
            move(metresToCoord(1.4f));
        }


    }
    //finds angle of location relative to agent
    public float getAngle(MapObject object) {
        if(object.getPos().x >= x && object.getPos().y >= y) {
            return (float) (Math.atan((object.getPos().x - x) / (object.getPos().y - y)));
        }
        else if(object.getPos().x >= x && object.getPos().y <= y){
            return (float) (Math.PI/2f + Math.atan((-object.getPos().y + y) / (object.getPos().x - x)));
        }
        else if(object.getPos().x <= x && object.getPos().y <= y){
            return (float) (Math.PI + Math.atan((-object.getPos().x + x) / (-object.getPos().y + y)));
        }
        else if(object.getPos().x <= x && object.getPos().y >= y){
            return (float) ((3f/2f)*Math.PI + Math.atan((object.getPos().y - y) / (-object.getPos().x + x)));
        }
        else {
            return 0;
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
    public void visionField(float[][] vision){
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
                }
            }
        }
        for(int i = 0; i < agentsVision.length; i++){
            for(int j = 0; j < agentsVision[0].length;j++){
                if(agentsVision[i][j] == 0){
                    for(int k = 0; k < 10; k++){
                        for(int l = 0; l < 10; l++){
                            float[] p = {(((minX+i)*10)+k),(((minY+j)*10)+l)};
                            if(isInVisionField(p, vision[0],vision[1], vision[2])){
                                agentsVision[i][j] = world[minX+i][minY+j];
                                k = 10;
                                l=10;
                                count++;
                            }
                        }
                    }
                }
            }
        }
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
